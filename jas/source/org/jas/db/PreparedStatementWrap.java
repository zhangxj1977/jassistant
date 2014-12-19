package org.jas.db;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.jas.util.StringUtil;

/**
 * an adapter that implementation PreparedStatement
 *
 * @author Zhang XueJun
 * @version 1.0
 */
public class PreparedStatementWrap implements PreparedStatement {

	/**
	 * real PreparedStatement
	 */
	PreparedStatement pstmt;

	public PreparedStatementWrap(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public ResultSet executeQuery() throws SQLException {
		return pstmt.executeQuery();
	}

	public int executeUpdate() throws SQLException {
		return pstmt.executeUpdate();
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		pstmt.setNull(parameterIndex, sqlType);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		pstmt.setBoolean(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		pstmt.setByte(parameterIndex, x);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		pstmt.setShort(parameterIndex, x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		pstmt.setInt(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		pstmt.setLong(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		pstmt.setFloat(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		pstmt.setDouble(parameterIndex, x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		pstmt.setBigDecimal(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		if (StringUtil.isEncodeEnabled()) {
			pstmt.setString(parameterIndex,
							StringUtil.convertString(x,
													 StringUtil.getToEncode(),
													 StringUtil.getFromEncode()));
		} else {
			pstmt.setString(parameterIndex, x);
		}
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		pstmt.setBytes(parameterIndex, x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		pstmt.setDate(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		pstmt.setTime(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		pstmt.setTimestamp(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		pstmt.setAsciiStream(parameterIndex, x, length);
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		pstmt.setUnicodeStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		pstmt.setBinaryStream(parameterIndex, x, length);
	}

	public void clearParameters() throws SQLException {
		pstmt.clearParameters();
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		pstmt.setObject(parameterIndex, x, targetSqlType, scale);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		pstmt.setObject(parameterIndex, x, targetSqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		pstmt.setObject(parameterIndex, x);
	}

	public boolean execute() throws SQLException {
		return pstmt.execute();
	}

	public void addBatch() throws SQLException {
		pstmt.addBatch();
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		pstmt.setCharacterStream(parameterIndex, reader, length);
	}

	public void setRef(int i, Ref x) throws SQLException {
		pstmt.setRef(i, x);
	}

	public void setBlob(int i, Blob x) throws SQLException {
		pstmt.setBlob(i, x);
	}

	public void setClob(int i, Clob x) throws SQLException {
		pstmt.setClob(i, x);
	}

	public void setArray(int i, Array x) throws SQLException {
		pstmt.setArray(i, x);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return pstmt.getMetaData();
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		pstmt.setDate(parameterIndex, x, cal);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		pstmt.setTime(parameterIndex, x, cal);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		pstmt.setTimestamp(parameterIndex, x, cal);
	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		pstmt.setNull(paramIndex, sqlType, typeName);
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return pstmt.executeQuery(sql);
	}

	public int executeUpdate(String sql) throws SQLException {
		return pstmt.executeUpdate(sql);
	}

	public void close() throws SQLException {
		pstmt.close();
	}

	public int getMaxFieldSize() throws SQLException {
		return pstmt.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		pstmt.setMaxFieldSize(max);
	}

	public int getMaxRows() throws SQLException {
		return pstmt.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		pstmt.setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		pstmt.setEscapeProcessing(enable);
	}

	public int getQueryTimeout() throws SQLException {
		return pstmt.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		pstmt.setQueryTimeout(seconds);
	}

	public void cancel() throws SQLException {
		pstmt.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return pstmt.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		pstmt.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		pstmt.setCursorName(name);
	}

	public boolean execute(String sql) throws SQLException {
		return pstmt.execute(sql);
	}

	public ResultSet getResultSet() throws SQLException {
		return pstmt.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		return pstmt.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return pstmt.getMoreResults();
	}

	public void setFetchDirection(int direction) throws SQLException {
		pstmt.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return pstmt.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		pstmt.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return pstmt.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return pstmt.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return pstmt.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		pstmt.addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		pstmt.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		return pstmt.executeBatch();
	}

	public Connection getConnection() throws SQLException {
		return pstmt.getConnection();
	}



	////////////////////////////// for jdk1.4 ///////////////////////////////////
	public void setURL(int parameterIndex, java.net.URL x) throws SQLException {
		;
	}
	public boolean getMoreResults(int current) throws SQLException {
		return true;
	}
	public ResultSet getGeneratedKeys() throws SQLException {
		return null;
	}
	public int getResultSetHoldability() throws SQLException {
		return 0;
	}
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return 0;
	}
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return 0;
	}
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		return 0;
	}
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return true;
	}
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return true;
	}
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		return true;
	}

	/** this method only in jdk1.4, if you compiled with jdk1.4, uncommented it */
	public java.sql.ParameterMetaData getParameterMetaData() throws SQLException {
		return null;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object unwrap(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAsciiStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
}