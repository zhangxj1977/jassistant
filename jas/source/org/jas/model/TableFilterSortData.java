package org.jas.model;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 *
 * @author     ���@�w�R
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
	 * sort value���擾����
	 *
	 * @return ArrayList sort value
	 */
	public Vector getSort() {
		return this.sort;
	}

	/**
	 * sort value��ݒ肷��
	 *
	 * @param sort �V����sort value
	 */
	public void setSort(Vector sort) {
		this.sort = sort;
	}

	/**
	 * filter value���擾����
	 *
	 * @return String filter value
	 */
	public String getFilter() {
		return this.filter;
	}

	/**
	 * filter value��ݒ肷��
	 *
	 * @param filter �V����filter value
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * �f�B�o�O�p�̃��b�\�h
	 *
	 * @return String �S���t�B�[���h�̒l
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("sort:		" + sort + "\n");
		sb.append("filter:		" + filter + "\n");

		return sb.toString();
	}
}
