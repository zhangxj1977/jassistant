package org.jas.common;

/**
 * refreshable interface
 *
 *
 * @author 張　学軍
 * @version 1.0
 */

public interface Refreshable {

	/**
	 * パラメータ設定
	 */
	public void setParam(String tableType, String tableName);

	/**
	 * refresh method
	 */
	public void resetData();

	/**
	 * set refreshable flag
	 */
	public void setRefreshable(boolean b);

	/**
	 * check it is need to refresh
	 */
	public boolean isReFreshable();

	/**
	 * clear all table data
	 * Refreshable method
	 */
	public void clearData();
}