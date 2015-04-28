package org.jas.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.jas.common.PJConst;
import org.jas.model.ColumnDescriptionData;
import org.jas.model.TableDefineData;
import org.jas.util.DBUtil;
import org.jas.util.FileManager;
import org.jas.util.FilterSortManager;
import org.jas.util.ResourceManager;
import org.jas.util.StringUtil;
import org.jas.util.UIUtil;

/**
 *
 * @author 張　学軍
 * @version 1.0
 */

public class DBParser {

    /**
     * get all table names
     *
     * @param DB connection
     * @return TableDefineData list
     */
    public static ArrayList getTableLists(Connection conn) throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        DatabaseMetaData dbMetaData = conn.getMetaData();
        String userName = dbMetaData.getUserName();
        ResultSet rs;

        if (isDB(dbMetaData, "Oracle")
                || isDB(dbMetaData, "jConnect")) {
            rs = dbMetaData.getTables(null, userName, null, PJConst.TABLE_TYPES);
        } else {
            rs = dbMetaData.getTables(null, null, null, PJConst.TABLE_TYPES);
        }

        ArrayList<TableDefineData> tableList = new ArrayList<TableDefineData>();
        while (rs.next()) {
            TableDefineData tableData = new TableDefineData();

            String tableName = rs.getString("TABLE_NAME");
            String schem = rs.getString("TABLE_SCHEM");
            String tableType = rs.getString("TABLE_TYPE");

            if (isDB(dbMetaData, "IBM ")) {
                tableData.setTableName(schem + "." + tableName);
            } else {
                tableData.setTableName(tableName);
            }
            tableData.setTableType(tableType);
            tableData.setTableSchem(schem);

            tableList.add(tableData);
        }

        HashMap<String, String> commentMap = null;
        if (isDB(dbMetaData, "Oracle")) {
            commentMap = getOracleTableComments(conn);
        } else if (isDB(dbMetaData, "IBM ")) {
            commentMap = getDB2TableComments(conn);
        }
        if (commentMap != null) {
            for (TableDefineData tableData : tableList) {
                String comment = commentMap.get(tableData.getTableName().toUpperCase());
                tableData.setComment(comment);
            }
        }

        if (rs != null) {
            rs.close();
        }

        return tableList;
    }

    private static HashMap<String, String> getDB2TableComments(
            Connection conn) throws SQLException {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select tabschema, tabname, remarks from syscat.tables where remarks is not null");
        while (rs.next()) {
            resultMap.put((rs.getString("tabschema").trim() + "." + rs.getString("tabname")).toUpperCase(), rs.getString("remarks"));
        }
        rs.close();
        stmt.close();

        return resultMap;
    }

    private static HashMap<String, String> getOracleTableComments(
            Connection conn) throws SQLException {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select table_name, comments from user_tab_comments where comments is not null");
        while (rs.next()) {
            resultMap.put(rs.getString("table_name").toUpperCase(), rs.getString("comments"));
        }
        rs.close();
        stmt.close();

        return resultMap;
    }

    /**
     *
     */
    private static boolean isDB(DatabaseMetaData dbMetaData, String key) throws SQLException {
        String driverName = dbMetaData.getDriverName();
        return (driverName != null && driverName.indexOf(key) >= 0);
    }

    private String getDB2DefaultSchema(Connection conn) throws SQLException {
        String defaultSchema = null;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select current_schema from sysibm.sysdummy1");
        if (rs.next()) {
            defaultSchema = rs.getString("current_schema");
        }
        rs.close();
        stmt.close();
        return defaultSchema;
    }

    /**
     * get table data
     *
     * @return Object[0] the resultset
     *          Object[1] the data in Vector
     */
    public static Object[] getTableData(Connection conn, String tableName,
                                        String connURL, String extraOrder,
                                        String extraFilter)
                                    throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        DatabaseMetaData dbMetaData = conn.getMetaData();

        // get primary key description
        HashMap keyMap = getPrimaryKeyMap(dbMetaData, tableName);

        Statement stmt = new StatementWrap(conn.createStatement());
        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName);

        String simpleSQL = sql.toString();

        try {
            String filter = FilterSortManager.getFilter(connURL, tableName);
            if (extraFilter != null && !extraFilter.equals("")) {
                if (filter != null && !filter.equals("")) {
                    filter = extraFilter + " AND " + filter;
                } else {
                    filter = extraFilter;
                }
            }
            if (filter != null && !filter.equals("")) {
                sql.append(" WHERE " + filter);
            }

            String sort = FilterSortManager.getSort(connURL, tableName);
            if (extraOrder != null && !extraOrder.equals("")) {
                if (sort != null && !sort.equals("")) {
                    sort = extraOrder + ", " + sort;
                } else {
                    sort = extraOrder;
                }
            }
            if (sort != null && !sort.equals("")) {
                sql.append(" ORDER BY " + sort);
            }

            if (isDB(dbMetaData, "jConnect")) {
                setSybaseRowCount(conn, 1000);
            }

//			if (isDB(dbMetaData, "IBM ")) {
//				sql.append(" WITH UR ");
//			}

            ResultSet rs = new ResultSetWrap(stmt.executeQuery(sql.toString()), stmt);

            if (isDB(dbMetaData, "jConnect")) {
                setSybaseRowCount(conn, 0);
            }

            Vector data = getQueryData(dbMetaData, conn, tableName, rs, UIUtil.getDefaultFetchSize(), keyMap);

            return new Object[]{rs, data, new Boolean(true)};
        } catch (SQLException se) {
        }

        String errorFilterSQL = simpleSQL + " WHERE 1 = 0 ";

        ResultSet rs = new ResultSetWrap(stmt.executeQuery(errorFilterSQL), stmt);
        Vector data = getQueryData(dbMetaData, conn, tableName, rs, UIUtil.getDefaultFetchSize(), keyMap);

        return new Object[]{rs, data, new Boolean(false)};
    }

    /**
     * for sybase option
     *
     * @param conn
     * @param count
     * @throws SQLException
     */
    private static void setSybaseRowCount(Connection conn, int count)
            throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("set rowcount " + count);
        stmt.close();
    }

    /**
     * get total row count
     */
    public static int getTotalRowCount(Connection conn, String tableName, String connURL)
                                    throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        Statement stmt = null;
        try {
            stmt = new StatementWrap(conn.createStatement());
            StringBuffer sql = new StringBuffer("SELECT count(*) FROM " + tableName);

            String filter = FilterSortManager.getFilter(connURL, tableName);
            if (filter != null && !filter.equals("")) {
                sql.append(" WHERE " + filter);
            }

            ResultSet rs = stmt.executeQuery(sql.toString());
            rs.next();

            return rs.getInt(1);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * fetch the result from the resultset to a list
     */
    private static Vector getQueryData(DatabaseMetaData dbMetaData,
            Connection conn, String tableName, ResultSet rs, int limit, HashMap keyMap)
                throws SQLException {
        Vector data = new Vector();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Vector nameVector = new Vector();
        Vector commentVector = new Vector();
        Vector typeVector = new Vector();
        Vector sizeVector = new Vector();
        Vector keyVector = new Vector();

        for (int i = 0; i < columnCount; i++) {
            String columnName = rsmd.getColumnLabel(i + 1);
            Class typeClass = StringUtil.getJavaTypeBySQLType(rsmd.getColumnType(i + 1), i, rsmd);

            nameVector.add(columnName);
            commentVector.add(columnName);
            typeVector.add(typeClass);
            int displaySize = rsmd.getColumnDisplaySize(i + 1);
            int precision = 0;
            if (typeClass != Object.class) {
                precision = rsmd.getPrecision(i + 1);
            }
            if (precision > 0) {
                sizeVector.add(new Integer(precision));
            } else {
                sizeVector.add(new Integer(displaySize));
            }
            // convert kyemap column name to column index
            if (keyMap != null) {
                if (keyMap.isEmpty() || keyMap.get(columnName.toUpperCase()) != null) {
                    if (typeClass != java.lang.Object.class) {
                        keyVector.add(new Boolean(true));
                    } else {
                        keyVector.add(new Boolean(false));
                    }
                } else {
                    keyVector.add(new Boolean(false));
                }
            } else {
                keyVector.add(new Boolean(false));
            }
        }
        data.add(nameVector);
        data.add(typeVector);
        data.add(sizeVector);
        data.add(keyVector);
        data.add(commentVector);

        if (tableName != null) {
            if (isDB(dbMetaData, "IBM ")) {
                int sep = tableName.indexOf(".");
                String scheme = null;
                if (sep >= 0) {
                    scheme = tableName.substring(0, sep);
                    tableName = tableName.substring(sep + 1);
                } else {
                    scheme = getDB2CurrentSchema(conn);
                }
                if (scheme != null) {
                    HashMap extRemarks = getDB2ColumnRemarks(conn, scheme, tableName);
                    for (int i = 0; i < commentVector.size(); i++) {
                        if (extRemarks.get(commentVector.get(i)) != null) {
                            commentVector.set(i, extRemarks.get(commentVector.get(i)));
                        }
                    }
                }
            } else if (isDB(dbMetaData, "Oracle")) {

                HashMap extRemarks = getOracleColumnRemarks(conn, tableName);
                for (int i = 0; i < commentVector.size(); i++) {
                    if (extRemarks.get(commentVector.get(i)) != null) {
                        commentVector.set(i, extRemarks.get(commentVector.get(i)));
                    }
                }
            }
        }

        data.addAll(getMoreQueryData(rs, limit, keyVector));

        return data;
    }

    private static String getDB2CurrentSchema(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select current_schema from sysibm.sysdummy1");
        String schema = null;
        if (rs.next()) {
            schema = rs.getString(1);
        }
        rs.close();
        stmt.close();

        return schema;
    }

    /**
     * get more data from the resultset
     */
    public static Vector getMoreQueryData(ResultSet rs, int limit, Vector keyVector)
                                        throws SQLException {
        Vector data = new Vector();

        if (rs == null) {
            return data;
        }

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            int count = 0;
            while (rs.next()) {
                Vector oneData = new Vector();

                Vector keyValueVector = null;
                if (keyVector != null && !keyVector.isEmpty()) {
                    keyValueVector = new Vector();
                }

                for (int i = 0; i < columnCount; i++) {
                    int columnType = rsmd.getColumnType(i + 1);
                    Object value = getValue(columnType, i, rs, rsmd);

                    if (keyVector != null && ((Boolean) keyVector.get(i)).booleanValue()) {
                        keyValueVector.add(value);
                    }

                    oneData.add(value);
                }

                oneData.add(keyValueVector);
                data.add(oneData);

                if (limit > 0 && ++count >= limit) {
                    break;
                }
            }
        } catch (SQLException se) {
            int errorCode = se.getErrorCode();
            if (errorCode != -4470) {
                throw se;
            }
        }

        return data;
    }

    /**
     * get the value from resultset by the type.
     */
    private static Object getValue(int sqlType, int index,
                                   ResultSet rs, ResultSetMetaData rsmd)
                                throws SQLException {
        Object value = null;

        switch (sqlType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                    value = rs.getString(index + 1);
                    break;
            case Types.NUMERIC:
            case Types.DECIMAL:
                    int displaySize = rsmd.getColumnDisplaySize(index + 1);
                    int precision = rsmd.getPrecision(index + 1);
                    if (precision > 0) {
                        displaySize = precision;
                    }
                    if (rsmd.getScale(index + 1) > 0 || displaySize > 18) {
                        if (displaySize > 18) {
                            value = rs.getBigDecimal(index + 1);
                        } else {
                            value = new Double(rs.getDouble(index + 1));
                        }
                    } else {
                        value = new Long(rs.getLong(index + 1));
                    }
                    break;
            case Types.BIT:
                    value = new Boolean(rs.getBoolean(index + 1));
                    break;
            case Types.TINYINT:
                    value = new Byte(rs.getByte(index + 1));
                    break;
            case Types.SMALLINT:
                    value = new Short(rs.getShort(index + 1));
                    break;
            case Types.INTEGER:
                    value = new Integer(rs.getInt(index + 1));
                    break;
            case Types.BIGINT:
                    value = new Long(rs.getLong(index + 1));
                    break;
            case Types.REAL:
                    value = new Float(rs.getFloat(index + 1));
                    break;
            case Types.FLOAT:
            case Types.DOUBLE:
                    value = new Double(rs.getDouble(index + 1));
                    break;
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                    value = new String("[Object]");
                    break;
            case Types.DATE:
                    value = rs.getDate(index + 1);
                    break;
            case Types.TIME:
                    value = rs.getTime(index + 1);
                    break;
            case Types.TIMESTAMP:
                    value = rs.getTimestamp(index + 1);
                    break;
            default:
                    value = new String("[Object]");
        }
        if (rs.wasNull()) {
            value = null;
        }

        return value;
    }

    private static HashMap getDB2ColumnRemarks(Connection conn, String schema, String tableName)
            throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("select colname, remarks from syscat.columns where tabschema=? and tabname=?");
        pstmt.setString(1, schema);
        pstmt.setString(2, tableName);

        HashMap remarks = new HashMap();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            remarks.put(rs.getString("colname").toUpperCase(), rs.getString("remarks"));
        }
        rs.close();
        pstmt.close();

        return remarks;
    }

    private static HashMap getOracleColumnRemarks(Connection conn, String tableName)
            throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("select column_name, comments from user_col_comments where table_name=?");
        pstmt.setString(1, tableName);

        HashMap remarks = new HashMap();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            remarks.put(rs.getString("column_name").toUpperCase(), rs.getString("comments"));
        }
        rs.close();
        pstmt.close();

        return remarks;
    }

    /**
     * get the table column description
     *
     * @param DB Connection
     * @param tableName
     * @return ColumnDescriptionData collection
     * @Exception SQLException
     */
    public static ArrayList getColumnDescription(Connection conn, String tableName) throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        DatabaseMetaData dbMetaData = conn.getMetaData();

        // get primary key description
        HashMap keyMap = getPrimaryKeyMap(dbMetaData, tableName);

        // get other descriptions
        String userName = dbMetaData.getUserName();
        String driverName = dbMetaData.getDriverName();
        ResultSet columnSet;

        HashMap extRemarks = null;

        if (isDB(dbMetaData, "Oracle")
                || isDB(dbMetaData, "jConnect")) {
            columnSet = dbMetaData.getColumns(null, userName, tableName, null);
            if (isDB(dbMetaData, "Oracle")) {
                extRemarks = getOracleColumnRemarks(conn, tableName);
            }
        } else if (isDB(dbMetaData, "IBM ")) {
            int sep = tableName.indexOf(".");
            String scheme = null;
            if (sep >= 0) {
                scheme = tableName.substring(0, sep);
                tableName = tableName.substring(sep + 1);
            }
            if (scheme != null) {
                extRemarks= getDB2ColumnRemarks(conn, scheme, tableName);
            }
            columnSet = dbMetaData.getColumns(null, scheme, tableName, null);
        } else {
            columnSet = dbMetaData.getColumns(null, null, tableName, null);
        }

        ArrayList descList = new ArrayList();

        while (columnSet.next()) {
            String thisTableName = columnSet.getString("TABLE_NAME");
            if (!tableName.equals(thisTableName)) {
                continue;
            }

            ColumnDescriptionData descData = new ColumnDescriptionData();

            String columnName = columnSet.getString("COLUMN_NAME");
            int columnType = columnSet.getInt("DATA_TYPE");
            String columnTypeName = columnSet.getString("TYPE_NAME");

            int columnSize = 0;
            columnSize = columnSet.getInt("COLUMN_SIZE");

            String isNullable = "YES";
            if (isDB(dbMetaData, "SymfoWARE")) {
                isNullable = columnSet.getString("NULLABLE");
                isNullable = "1".equals(isNullable) ? "YES" : "NO";
            } else {
                isNullable = columnSet.getString("IS_NULLABLE");
            }

            int precision = 0;
            if (isDB(dbMetaData, "SymfoWARE")) {
                precision = columnSet.getInt("REMARKS");
            } else {
                precision = columnSet.getInt("DECIMAL_DIGITS");
            }

            descData.setColumnName(columnName);
            descData.setColumnType(columnType);
            descData.setColumnTypeName(columnTypeName);
            if (columnType == Types.DECIMAL || columnType == Types.NUMERIC) {
                descData.setPrecision(precision);
            }
            descData.setColumnSize(columnSize);
            descData.setIsNullable(isNullable);

            Object objKeySeq = keyMap.get(columnName.toUpperCase());
            if (objKeySeq != null) {
                int keySeq = ((Integer) objKeySeq).intValue();
                descData.setPrimaryKeySeq(keySeq);
            }

            descData.setComment(columnSet.getString("REMARKS"));
            if (extRemarks != null) {
                String remark = (String) extRemarks.get(columnName.toUpperCase());
                if (remark != null) {
                    descData.setComment(remark);
                }
            }

            // DB2のみ検証済み（Oracleがエラーになる）
            if (isDB(dbMetaData, "IBM ")) {
                descData.setDefaultValue(columnSet.getString("COLUMN_DEF"));
            }

            descList.add(descData);
        }
        columnSet.close();

        return descList;
    }

    public static HashMap getColumnRemark(Connection conn, String tableName) throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        DatabaseMetaData dbMetaData = conn.getMetaData();

        // get other descriptions
        String userName = dbMetaData.getUserName();
        String driverName = dbMetaData.getDriverName();

        HashMap extRemarks = null;

        if (isDB(dbMetaData, "Oracle")
                || isDB(dbMetaData, "jConnect")) {
            if (isDB(dbMetaData, "Oracle")) {
                extRemarks = getOracleColumnRemarks(conn, tableName);
            }
        } else if (isDB(dbMetaData, "IBM ")) {
            int sep = tableName.indexOf(".");
            String scheme = null;
            if (sep >= 0) {
                scheme = tableName.substring(0, sep);
                tableName = tableName.substring(sep + 1);
            }
            if (scheme != null) {
                extRemarks= getDB2ColumnRemarks(conn, scheme, tableName);
            }
        } else {
            extRemarks = new HashMap();
        }

        return extRemarks;
    }

    /**
     * get primary key map
     * the key is the column name, value is the key sequence
     */
    public static HashMap getPrimaryKeyMap(DatabaseMetaData dbMetaData, String tableName)
                                        throws SQLException {
        HashMap keyMap = new HashMap();

        // get primary key description
        String driverName = dbMetaData.getDriverName();
        String userName = dbMetaData.getUserName();
        ResultSet keyRs;

        if (isDB(dbMetaData, "Oracle")) {
            keyRs = dbMetaData.getPrimaryKeys(null, userName, tableName);
            getPrimaryKeyMap(keyRs, keyMap, tableName);
        } else if (isDB(dbMetaData, "SQLServer") || isDB(dbMetaData, "mssqlserver")) {
            keyRs = dbMetaData.getPrimaryKeys(null, "dbo", tableName);
            getPrimaryKeyMap(keyRs, keyMap, tableName);
        } else if (isDB(dbMetaData, "SymfoWARE")) {
            ResultSet schRs = dbMetaData.getSchemas();
            while (schRs.next()) {
                String oneSchema = schRs.getString(1);
                keyRs = dbMetaData.getPrimaryKeys(null, oneSchema, tableName);
                getPrimaryKeyMap(keyRs, keyMap, tableName);
            }
        } else if (isDB(dbMetaData, "IBM ")) {
            int sep = tableName.indexOf(".");
            String scheme = null;
            if (sep >= 0) {
                scheme = tableName.substring(0, sep);
                tableName = tableName.substring(sep + 1);
            }
            keyRs = dbMetaData.getPrimaryKeys(null, scheme, tableName);
            getPrimaryKeyMap(keyRs, keyMap, tableName);
        } else if (isDB(dbMetaData, "sybase") || isDB(dbMetaData, "jConnect")) {
            keyRs = dbMetaData.getPrimaryKeys(null, null, tableName);
            getPrimaryKeyMap(keyRs, keyMap, tableName);
        } else {
            keyRs = dbMetaData.getPrimaryKeys(null, "", tableName);
            getPrimaryKeyMap(keyRs, keyMap, tableName);
        }

        return keyMap;
    }

    private static void getPrimaryKeyMap(ResultSet keyRs, HashMap keyMap, String tableName) throws SQLException {
        while (keyRs.next()) {
            String thisTableName = keyRs.getString("TABLE_NAME");
            if (!tableName.equals(thisTableName)) {
                continue;
            }

            String keyName = keyRs.getString("COLUMN_NAME");
            int keySeq = keyRs.getInt("KEY_SEQ");
            keyMap.put(keyName.toUpperCase(), new Integer(keySeq));
        }
        keyRs.close();
    }

    /**
     * get all functions of the current session
     *
     */
    public static ArrayList getFunctions(Connection conn) throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        ArrayList functions = new ArrayList();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        String numFunc = dbMetaData.getNumericFunctions();
        String strFunc = dbMetaData.getStringFunctions();
        String sysFunc = dbMetaData.getSystemFunctions();
        String tdtFunc = dbMetaData.getTimeDateFunctions();

        ArrayList numList = StringUtil.getListFromString(numFunc, ",");
        ArrayList strList = StringUtil.getListFromString(strFunc, ",");
        ArrayList sysList = StringUtil.getListFromString(sysFunc, ",");
        ArrayList tdtList = StringUtil.getListFromString(tdtFunc, ",");

        if (numList != null) {
            functions.addAll(numList);
        }
        if (strList != null) {
            functions.addAll(strList);
        }
        if (sysList != null) {
            functions.addAll(sysList);
        }
        if (tdtList != null) {
            functions.addAll(tdtList);
        }

        return functions;
    }

    public static Vector getIndexes(Connection conn, String tableName)
                throws SQLException {

        DatabaseMetaData dbMetaData = conn.getMetaData();

        // get other descriptions
        String userName = dbMetaData.getUserName();
        String driverName = dbMetaData.getDriverName();
        ResultSet columnSet;

        String schema = null;

        if (isDB(dbMetaData, "Oracle")
                || isDB(dbMetaData, "jConnect")) {
            schema = userName;
        } else if (isDB(dbMetaData, "IBM ")) {
            int sep = tableName.indexOf(".");
            if (sep >= 0) {
                schema = tableName.substring(0, sep);
                tableName = tableName.substring(sep + 1);
            }
        }

        Vector result = new Vector();
        ResultSet rsIdx = dbMetaData.getIndexInfo("", schema, tableName, false, false);
        while (rsIdx.next()) {
            if (DatabaseMetaData.tableIndexStatistic == rsIdx.getInt("TYPE")) {
                continue;
            }
            Vector row = new Vector();
            row.add(rsIdx.getString("INDEX_NAME"));
            if (rsIdx.getBoolean("NON_UNIQUE")) {
                row.add("N");
            } else {
                row.add("Y");
            }
            row.add(rsIdx.getString("COLUMN_NAME"));
            row.add(rsIdx.getString("ORDINAL_POSITION"));
            if ("A".equals(rsIdx.getString("ASC_OR_DESC"))) {
                row.add("ASC");
            } else {
                row.add("DESC");
            }
            result.add(row);
        }
        rsIdx.close();

        return result;
    }

    /**
     * get import and export key descriptions
     *
     */
    public static Vector[] getImportExportKeys(Connection conn, String tableName)
                                        throws SQLException {
        return new Vector[]{getImportKeys(conn, tableName), getExportKeys(conn, tableName)};
    }

    /**
     * get import keys
     *
     */
    public static Vector getImportKeys(Connection conn, String tableName)
                                        throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        DatabaseMetaData dbMetaData = conn.getMetaData();
        String userName = dbMetaData.getUserName();
        String driverName = dbMetaData.getDriverName();
        ResultSet rs;
        Vector importKeyVector = new Vector();

        if (isDB(dbMetaData, "Oracle")) {
            rs = dbMetaData.getImportedKeys(null, userName, tableName);
        } else if (isDB(dbMetaData, "SymfoWARE")) {
            return importKeyVector;
        } else if (isDB(dbMetaData, "IBM ")) {
            int sep = tableName.indexOf(".");
            String scheme = null;
            if (sep >= 0) {
                scheme = tableName.substring(0, sep);
                tableName = tableName.substring(sep + 1);
            }
            rs = dbMetaData.getImportedKeys(null, scheme, tableName);
        } else {
            rs = dbMetaData.getImportedKeys(null, null, tableName);
        }
        while (rs.next()) {
            Vector oneImVec = new Vector();

            oneImVec.add(rs.getString("FKCOLUMN_NAME"));
            oneImVec.add(rs.getString("PKTABLE_NAME"));
            oneImVec.add(rs.getString("PKCOLUMN_NAME"));
            oneImVec.add(rs.getString("FK_NAME"));
            oneImVec.add(rs.getString("PK_NAME"));

            importKeyVector.add(oneImVec);
        }
        rs.close();

        return importKeyVector;
    }


    /**
     * get export keys
     *
     */
    public static Vector getExportKeys(Connection conn, String tableName)
                                        throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        DatabaseMetaData dbMetaData = conn.getMetaData();
        String userName = dbMetaData.getUserName();
        String driverName = dbMetaData.getDriverName();
        ResultSet rs;
        Vector exportKeyVector = new Vector();

        if (isDB(dbMetaData, "Oracle")) {
            rs = dbMetaData.getExportedKeys(null, userName, tableName);
        } else if (isDB(dbMetaData, "SymfoWARE")) {
            return exportKeyVector;
        } else if (isDB(dbMetaData, "IBM ")) {
            int sep = tableName.indexOf(".");
            String scheme = null;
            if (sep >= 0) {
                scheme = tableName.substring(0, sep);
                tableName = tableName.substring(sep + 1);
            }
            rs = dbMetaData.getExportedKeys(null, scheme, tableName);
        } else {
            rs = dbMetaData.getExportedKeys(null, null, tableName);
        }
        while (rs.next()) {
            Vector oneImVec = new Vector();

            oneImVec.add(rs.getString("PKCOLUMN_NAME"));
            oneImVec.add(rs.getString("FKTABLE_NAME"));
            oneImVec.add(rs.getString("FKCOLUMN_NAME"));
            oneImVec.add(rs.getString("FK_NAME"));
            oneImVec.add(rs.getString("PK_NAME"));

            exportKeyVector.add(oneImVec);
        }
        rs.close();

        return exportKeyVector;
    }

    /**
     * execute the sql and return the result
     * the result may be a resultset or a int.
     * 
     * tableName:used to get logic name 
     */
    public static Object getResultByScript(Connection conn, String sql, boolean isQueryOnly, String tableName) throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        if (tableName != null) {
            tableName = tableName.toUpperCase();
        }

        Object retValue;
        Statement stmt = null;

        sql = sql.trim();
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }

        try {
            stmt = conn.createStatement();
            if (isQueryOnly) {
                ResultSet rs = new ResultSetWrap(stmt.executeQuery(sql), stmt);
                retValue = getQueryData(conn.getMetaData(), conn, tableName, rs, -1, null);
            } else {
                boolean isResultSet = stmt.execute(sql);

                if (isResultSet) {
                    ResultSet rs = new ResultSetWrap(stmt.getResultSet(), stmt);
                    retValue = getQueryData(conn.getMetaData(), conn, tableName, rs, -1, null);
                } else {
                    retValue = new Integer(stmt.getUpdateCount());
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return retValue;
    }

    /**
     * update row data
     *
     */
    public static boolean updateRowData(Connection conn, Vector columnName,
                                        Vector typeClass, Vector keyVector,
                                        Vector rowData, Vector orgRowData, String tableName)
                                throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        String driverName = DBUtil.getDriverName(conn);

        Vector keyValueVector = (Vector) rowData.get(rowData.size() - 1);
        if (keyValueVector == null || keyValueVector.isEmpty()) {
            throw new SQLException("key must not be empty");
        }
        Vector keyTypeVector = new Vector();

        StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET ");
        Vector updColumns = new Vector();
        for (int i = 0; i < columnName.size(); i++) {
            if ((Class) typeClass.get(i) == Object.class) {
                continue;
            }
            // skip no changed column
            if (orgRowData != null && orgRowData.size() > 0) {
                if (rowData.get(i) == orgRowData.get(i)
                    || (rowData.get(i) != null && rowData.get(i).equals(orgRowData.get(i)))) {
                    continue;
                }
            }
            updColumns.add(columnName.get(i) + " = ?");
        }
        sql.append(StringUtil.joinList(updColumns, ','));

        sql.append(" WHERE 1 = 1 ");

        int keyIndex = 0;
        for (int i = 0; i < columnName.size(); i++) {
            if (((Boolean) keyVector.get(i)).booleanValue()) {
                Object keyValue = keyValueVector.get(keyIndex++);
                Class type = (Class) typeClass.get(i);
                if (keyValue != null) {
                    if (type == java.sql.Timestamp.class
                            && driverName.indexOf("PostgreSQL") >= 0) {
                        sql.append("AND date_trunc('second', " + columnName.get(i) + ") = '" + StringUtil.getStringValue(type, keyValue) + "'");
                    } else {
                        sql.append("AND " + columnName.get(i) + " = ? ");
                    }
                    keyTypeVector.add(type);
                } else {
                    sql.append("AND " + columnName.get(i) + " IS NULL ");
                    keyTypeVector.add(null);
                }
            }
        }

        System.out.println(sql);

        PreparedStatement pstmt = null;
        try {
            // process params set
            pstmt = new PreparedStatementWrap(conn.prepareStatement(sql.toString()));
            int index = 0;
            for (int i = 0; i < rowData.size() - 1; i++) {
                if ((Class) typeClass.get(i) == Object.class) {
                    continue;
                }
                // skip no changed column
                if (orgRowData != null && orgRowData.size() > 0) {
                    if (rowData.get(i) == orgRowData.get(i)
                        || (rowData.get(i) != null && rowData.get(i).equals(orgRowData.get(i)))) {
                        continue;
                    }
                }
                setParameter(pstmt, ++index, (Class) typeClass.get(i), rowData.get(i));
            }
            // where condition param set
            for (int i = 0; i < keyValueVector.size(); i++) {
                Object keyValue = keyValueVector.get(i);
                Class type = (Class) keyTypeVector.get(i);
                if (keyValue != null) {
                    if (type == java.sql.Timestamp.class
                            && driverName.indexOf("PostgreSQL") >= 0) {
                        continue;
                    }
                    setParameter(pstmt, ++index,
                                type,
                                keyValue);
                }
            }

            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * update one column with same value
     */
    public static void updateOneColumn(Connection conn,
                    String columnName,
                    Object columnValue,
                    Class columnType,
                    String tableName)
                throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET ");
        sql.append(columnName + " = ? ");

        PreparedStatement pstmt = null;
        try {
            // process params set
            pstmt = new PreparedStatementWrap(conn.prepareStatement(sql.toString()));
            setParameter(pstmt, 1, columnType, columnValue);

            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * update object column from file
     */
    public static void updateObjectColumn(Connection conn, String columnName,
                                            String filePath, Vector columnNameVector,
                                            Vector typeVector, Vector keyVector,
                                            Vector keyValueVector, String tableName)
                                    throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET ");
        sql.append(columnName + " = ? ");

        StringBuffer whereClause = new StringBuffer();
        whereClause.append(" WHERE 1 = 1 ");
        Vector keyTypeVector = new Vector();
        for (int i = 0; i < columnNameVector.size(); i++) {
            if (((Boolean) keyVector.get(i)).booleanValue()) {
                whereClause.append("AND " + columnNameVector.get(i) + " = ? ");
                keyTypeVector.add(typeVector.get(i));
            }
        }
        sql.append(whereClause.toString());

        //System.out.println(sql);

        PreparedStatement pstmt = null;
        try {
            // process params set
            pstmt = conn.prepareStatement(sql.toString());
            int index = 0;

            // read from file
            File file = new File(filePath);

            // for oracle xmltype and clob
            DatabaseMetaData dbMetaData = conn.getMetaData();
            String driverName = dbMetaData.getDriverName();
            if (driverName != null && driverName.indexOf("Oracle") >= 0) {
                String typeName = getColumnTypeName(conn, columnName, tableName);

                if ("XMLTYPE".equalsIgnoreCase(typeName)) {
                    Class xmlTypeClass = ResourceManager.loadDynamicJar("oracle.xdb.XMLType");
                    Method createXMLMethod = xmlTypeClass.getMethod("createXML", new Class[]{Connection.class, String.class});
                    String xmlString = readXMLFile(file);
                    Object o = createXMLMethod.invoke(xmlTypeClass, new Object[]{conn, xmlString});
                    pstmt.setObject(++index, o);
                } else if ("CLOB".equalsIgnoreCase(typeName)) {
                    boolean orgAutoCommit = conn.getAutoCommit();
                    conn.setAutoCommit(false);

                    String oraSql = "UPDATE " + tableName + " SET " + columnName + " = EMPTY_CLOB() " + whereClause.toString();
                    PreparedStatement oraPstmt = conn.prepareStatement(oraSql);
                    // where condition param set
                    int oraIndex = 0;
                    for (int i = 0; i < keyValueVector.size(); i++) {
                        setParameter(oraPstmt, ++oraIndex,
                                    (Class) keyTypeVector.get(i),
                                    keyValueVector.get(i));
                    }
                    oraPstmt.execute();
                    oraPstmt.close();

                    oraSql = "SELECT " + columnName + " FROM " + tableName + whereClause.toString() + " FOR UPDATE";
                    oraPstmt = conn.prepareStatement(oraSql);
                    oraIndex = 0;
                    for (int i = 0; i < keyValueVector.size(); i++) {
                        setParameter(oraPstmt, ++oraIndex,
                                    (Class) keyTypeVector.get(i),
                                    keyValueVector.get(i));
                    }
                    ResultSet rs = oraPstmt.executeQuery();
                    if (rs.next()) {
                        Clob bodyClob = rs.getClob(1);
                        Class oracleClob = ResourceManager.loadDynamicJar("oracle.sql.CLOB");
                        Method putClobMethod = oracleClob.getMethod("putString", new Class[]{Long.TYPE, String.class});
                        String clobString = "";
                        if (file.getName().lastIndexOf(".xml") > 0
                                || file.getName().lastIndexOf(".xsl") > 0) {
                            clobString = readXMLFile(file);
                        } else {
                            clobString = readAscFile(file);
                        }
                        putClobMethod.invoke(bodyClob, new Object[]{new Integer(1), clobString});
                    }
                    oraPstmt.close();
                    conn.commit();
                    conn.setAutoCommit(orgAutoCommit);
                    return;
                }
            } else {
                FileInputStream fis = new FileInputStream(filePath);
                pstmt.setBinaryStream(++index, fis, (int) file.length());
            }

            // where condition param set
            for (int i = 0; i < keyValueVector.size(); i++) {
                setParameter(pstmt, ++index,
                            (Class) keyTypeVector.get(i),
                            keyValueVector.get(i));
            }

            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    // for oracle
    private static String getColumnTypeName(Connection conn, String columnName, String tableName) throws SQLException {
        ArrayList desc = getColumnDescription(conn, tableName);
        for (int i = 0; i < desc.size(); i++) {
            ColumnDescriptionData descData = (ColumnDescriptionData) desc.get(i);

            if (columnName.equalsIgnoreCase(descData.getColumnName())) {
                return descData.getColumnTypeName();
            }
        }
        return "";
    }
    private static String readXMLFile(File file) throws IOException {
        String s = readAscFile(file);
        /*
        try {
            if (s.indexOf("UTF-8") >= 0) {
                s = readWithReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            } else if (s.indexOf("EUC") >= 0) {
                s = readWithReader(new InputStreamReader(new FileInputStream(file), "EUC_JP"));
            } else if (s.indexOf("Shift_JIS") >= 0) {
                s = readWithReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"));
            }
        } catch (Exception e) {}
        */
        return s;
    }
    private static String readAscFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        return readWithReader(br);
    }
    private static String readWithReader(Reader reader) throws IOException {
        int c;
        StringBuffer sb = new StringBuffer();
        while ((c = reader.read()) != -1) {
            sb.append((char) c);
        }
        reader.close();
        return sb.toString();
    }
    // end for oracle

    /**
     * save object column to file
     */
    public static void saveObjectColumn(Connection conn, String columnName,
                                        String filePath, Vector columnNameVector,
                                        Vector typeVector, Vector keyVector,
                                        Vector keyValueVector, String tableName)
                                    throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(columnName);
        sql.append(" FROM " + tableName);
        StringBuffer whereClause = new StringBuffer();
        whereClause.append(" WHERE 1 = 1 ");
        Vector keyTypeVector = new Vector();
        for (int i = 0; i < columnNameVector.size(); i++) {
            if (((Boolean) keyVector.get(i)).booleanValue()) {
                whereClause.append("AND " + columnNameVector.get(i) + " = ? ");
                keyTypeVector.add(typeVector.get(i));
            }
        }
        sql.append(whereClause.toString());

        //System.out.println(sql);

        PreparedStatement pstmt = null;
        try {
            // for oracle xmltype and clob
            DatabaseMetaData dbMetaData = conn.getMetaData();
            String driverName = dbMetaData.getDriverName();
            if (driverName != null && driverName.indexOf("Oracle") >= 0) {
                String typeName = getColumnTypeName(conn, columnName, tableName);

                if ("XMLTYPE".equalsIgnoreCase(typeName)
                        || "CLOB".equalsIgnoreCase(typeName)) {
                    boolean orgAutoCommit = conn.getAutoCommit();
                    conn.setAutoCommit(false);

                    String oraSql = "";
                    if ("XMLTYPE".equalsIgnoreCase(typeName)) {
                        oraSql = "SELECT t." + columnName + ".getClobVal() FROM " + tableName + " t " + whereClause.toString() + " FOR UPDATE";
                    } else {
                        oraSql = "SELECT " + columnName + " FROM " + tableName + whereClause.toString() + " FOR UPDATE";
                    }
                    pstmt = new PreparedStatementWrap(conn.prepareStatement(oraSql));
                    int oraIndex = 0;
                    // where condition param set
                    for (int i = 0; i < keyValueVector.size(); i++) {
                        setParameter(pstmt, ++oraIndex,
                                    (Class) keyTypeVector.get(i),
                                    keyValueVector.get(i));
                    }
                    pstmt.execute();
                    ResultSet rs = pstmt.getResultSet();
                    if (rs.next()) {
                        Clob bodyClob = rs.getClob(1);
                        if (bodyClob != null) {
                            Reader char_stream = bodyClob.getCharacterStream();
                            String s = readWithReader(char_stream);
                            FileManager.writeFile(s, new File(filePath));
                        }
                    }

                    conn.setAutoCommit(orgAutoCommit);
                }
            } else {
                // process params set
                pstmt = new PreparedStatementWrap(conn.prepareStatement(sql.toString()));
                int index = 0;

                // where condition param set
                for (int i = 0; i < keyValueVector.size(); i++) {
                    setParameter(pstmt, ++index,
                                (Class) keyTypeVector.get(i),
                                keyValueVector.get(i));
                }

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    InputStream is = rs.getBinaryStream(columnName);
                    FileOutputStream fos = new FileOutputStream(filePath);
                    FileManager.pipeStream(is, fos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * insert row data
     *
     */
    public static boolean insertRowData(Connection conn, Vector columnName,
                                        Vector typeClass, Vector rowData,
                                        String tableName)
                                throws SQLException {

        PreparedStatement pstmt = null;
        try {
            pstmt = getInsertStatement(conn, columnName, typeClass, rowData, tableName);
            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * get insert statement
     *
     */
    public static PreparedStatement getInsertStatement(Connection conn, Vector columnName,
                                                    Vector typeClass, Vector rowData,
                                                    String tableName)
                                            throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        StringBuffer sql = new StringBuffer("INSERT INTO " + tableName + "(");
        Vector insColumns = new Vector();
        Vector insColumnsParam = new Vector();
        for (int i = 0; i < columnName.size(); i++) {
            if ((Class) typeClass.get(i) == Object.class) {
                continue;
            }

            if (rowData.get(i) == null) {
                continue;
            }

            insColumns.add(columnName.get(i));
            insColumnsParam.add("?");
        }
        sql.append(StringUtil.joinList(insColumns, ','));
        sql.append(") VALUES (" + StringUtil.joinList(insColumnsParam, ',') + ")");

        // System.out.println(sql);

        // process params set
        PreparedStatement pstmt = new PreparedStatementWrap(conn.prepareStatement(sql.toString()));
        int index = 0;
        for (int i = 0; i < rowData.size() - 1; i++) {
            if ((Class) typeClass.get(i) == Object.class) {
                continue;
            }
            if (rowData.get(i) == null) {
                continue;
            }

            setParameter(pstmt, ++index, (Class) typeClass.get(i), rowData.get(i));
        }

        return pstmt;
    }

    /**
     * delete row data
     *
     */
    public static boolean deleteRowData(Connection conn, Vector columnName,
                                        Vector typeClass, Vector keyVector,
                                        Vector rowData, String tableName)
                                    throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        String driverName = DBUtil.getDriverName(conn);

        Vector keyValueVector = (Vector) rowData.get(rowData.size() - 1);
        if (keyValueVector == null || keyValueVector.isEmpty()) {
            throw new SQLException("key must not be empty");
        }
        Vector keyTypeVector = new Vector();

        StringBuffer sql = new StringBuffer("DELETE FROM " + tableName);

        sql.append(" WHERE 1 = 1 ");

        int keyIndex = 0;
        for (int i = 0; i < columnName.size(); i++) {
            if (((Boolean) keyVector.get(i)).booleanValue()) {
                Object keyValue = keyValueVector.get(keyIndex++);
                Class type = (Class) typeClass.get(i);
                if (keyValue != null) {
                    if (type == java.sql.Timestamp.class
                            && driverName.indexOf("PostgreSQL") >= 0) {
                        sql.append("AND date_trunc('second', " + columnName.get(i) + ") = '" + StringUtil.getStringValue(type, keyValue) + "'");
                    } else {
                        sql.append("AND " + columnName.get(i) + " = ? ");
                    }
                    keyTypeVector.add(type);
                } else {
                    sql.append("AND " + columnName.get(i) + " IS NULL ");
                    keyTypeVector.add(null);
                }
            }
        }

        // System.out.println(sql);

        PreparedStatement pstmt = null;
        try {
            // process params set
            pstmt = new PreparedStatementWrap(conn.prepareStatement(sql.toString()));

            int index = 0;
            // where condition param set
            for (int i = 0; i < keyValueVector.size(); i++) {
                Object keyValue = keyValueVector.get(i);
                Class type = (Class) keyTypeVector.get(i);
                if (keyValue != null) {
                    if (type == java.sql.Timestamp.class
                            && driverName.indexOf("PostgreSQL") >= 0) {
                        continue;
                    }
                    setParameter(pstmt, ++index,
                                type,
                                keyValue);
                }
            }

            return pstmt.executeUpdate() > 0;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * delete all table data
     *
     */
    public static void deleteAllData(Connection conn, String tableName,
                                     String connURL, boolean withFilter)
                                throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null");
        }

        StringBuffer sql = new StringBuffer("DELETE FROM " + tableName);

        if (withFilter) {
            String filter = FilterSortManager.getFilter(connURL, tableName);
            if (filter != null && !filter.equals("")) {
                sql.append(" WHERE " + filter);
            }
        }

        PreparedStatement pstmt = null;
        try {
            // process params set
            pstmt = new PreparedStatementWrap(conn.prepareStatement(sql.toString()));

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    /**
     * set the parameter
     */
    public static void setParameter(PreparedStatement pstmt, int index,
                                     Class javaType, Object value)
                                throws SQLException {
        if (value == null) {
            pstmt.setNull(index, StringUtil.getSQLTypeByJavaType(javaType));
            return;
        }

        if (value instanceof String) {
            pstmt.setString(index, value.toString());
        } else if (value instanceof Byte) {
            pstmt.setByte(index, ((Byte) value).byteValue());
        } else if (value instanceof Short) {
            pstmt.setShort(index, ((Short) value).shortValue());
        } else if (value instanceof Integer) {
            pstmt.setInt(index, ((Integer) value).intValue());
        } else if (value instanceof Long) {
            pstmt.setLong(index, ((Long) value).longValue());
        } else if (value instanceof Float) {
            pstmt.setFloat(index, ((Float) value).floatValue());
        } else if (value instanceof Double) {
            pstmt.setDouble(index, ((Double) value).doubleValue());
        } else if (value instanceof BigDecimal) {
            pstmt.setBigDecimal(index, (BigDecimal) value);
        } else if (value instanceof Date) {
            pstmt.setDate(index, (Date) value);
        } else if (value instanceof Time) {
            pstmt.setTime(index, (Time) value);
        } else if (value instanceof Timestamp) {
            pstmt.setTimestamp(index, (Timestamp) value);
        } else {
            pstmt.setObject(index, value);
        }
    }

}