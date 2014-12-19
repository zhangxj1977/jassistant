package org.jas.model;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 *
 * @author     張　学軍
 * @version    1.0
 */
public class TableFilterSortData implements Serializable {

	/**
	 * sort value
	 */
	private Vector sort = null;

	/**
	 * filter value
	 */
	private String filter = null;


	/**
	 * sort valueを取得する
	 *
	 * @return ArrayList sort value
	 */
	public Vector getSort() {
		return this.sort;
	}

	/**
	 * sort valueを設定する
	 *
	 * @param sort 新しいsort value
	 */
	public void setSort(Vector sort) {
		this.sort = sort;
	}

	/**
	 * filter valueを取得する
	 *
	 * @return String filter value
	 */
	public String getFilter() {
		return this.filter;
	}

	/**
	 * filter valueを設定する
	 *
	 * @param filter 新しいfilter value
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * ディバグ用のメッソド
	 *
	 * @return String 全部フィールドの値
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("sort:		" + sort + "\n");
		sb.append("filter:		" + filter + "\n");

		return sb.toString();
	}
}
