package org.jas.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.jas.util.StringUtil;

/**
 * an wrap to Statement
 *
 * @author Zhang XueJun
 * @version 1.0
 */
public class StatementWrap implements Statement {

	/**
	 * real statement
	 */
	Statement stmt;

	public StatementWrap(Statement stmt) {
		this.stmt = stmt;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		if (StringUtil.isEncodeEnabled()) {
			return stmt.executeQuery(
							StringUtil.convertString(sql,
													 StringUtil.getToEncode(),
													 StringUtil.getFromEncode()));
		} else {
			return stmt.executeQuery(sql);
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		if (StringUtil.isEncodeEnabled()) {
			return stmt.executeUpdate(
							StringUtil.convertString(sql,
													 StringUtil.getToEncode(),
													 StringUtil.getFromEncode()));
		} else {
			return stmt.executeUpdate(sql);
		}
	}

	public void close() throws SQLException {
		stmt.close();
	}

	public int getMaxFieldSize() throws SQLException {
		return stmt.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		stmt.setMaxFieldSize(max);
	}
	public int getMaxRows() throws SQLException {
		return stmt.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		stmt.setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		stmt.setEscapeProcessing(enable);
	}

	public int getQueryTimeout() throws SQLException {
		return stmt.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		stmt.setQueryTimeout(seconds);
	}

	public void cancel() throws SQLException {
		stmt.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return stmt.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		stmt.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		stmt.setCursorName(name);
	}

	public boolean execute(String sql) throws SQLException {
		if (StringUtil.isEncodeEnabled()) {
			return stmt.execute(
							StringUtil.convertString(sql,
													 StringUtil.getToEncode(),
													 StringUtil.getFromEncode()));
		} else {
			return stmt.execute(sql);
		}
	}

	public ResultSet getResultSet() throws SQLException {
		return stmt.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		return stmt.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return stmt.getMoreResults();
	}

	public void setFetchDirection(int direction) throws SQLException {
		stmt.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return stmt.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		stmt.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return stmt.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return stmt.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return stmt.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		stmt.addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		stmt.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		return stmt.executeBatch();
	}

	public Connection getConnection() throws SQLException {
		return stmt.getConnection();
	}

	////////////////////////////// for jdk1.4 ///////////////////////////////////
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

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPoolable(boolean poolable) throws SQLException {
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
	
}