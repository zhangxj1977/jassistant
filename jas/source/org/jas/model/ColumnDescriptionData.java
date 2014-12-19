package org.jas.model;

import java.io.Serializable;

/**
 * �e�[�u��description�f�[�^
 *
 * @author ���@�w�R
 * @version 1.0
 */

public class ColumnDescriptionData implements Serializable {

	/**
	 * the designated column's name
	 */
	private String columnName = null;

	/**
	 * the designated column's database-specific type name.
	 */
	private String columnTypeName = null;

	/**
	 * the designated column's SQL type
	 */
	private int columnType = 0;

	/**
	 * the designated column's normal maximum width in characters
	 */
	private int columnSize = 0;

	/**
	 * the designated column's number of decimal digits
	 */
	private int precision = 0;

	/**
	 * define whether the column is nullable
	 */
	private String isNullable = "Yes";

	/**
	 * defined the column is primary key, return the sequence of the primary key
	 */
	private int primaryKeySeq = 0;

	/**
	 * comment
	 */
	private String comment = null;
	
	/**
	 * default value
	 */
	private String defaultValue = null;

	/**
	 * �f�B�t�H���g�R���X�g���N�V����
	 */
	public ColumnDescriptionData() {}


	/**
	 * the designated column's name���擾����
	 *
	 * @return String the designated column's name
	 */
	public String getColumnName() {
		return this.columnName;
	}

	/**
	 * the designated column's name��ݒ肷��
	 *
	 * @param columnName �V����the designated column's name
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * the designated column's database-specific type name.���擾����
	 *
	 * @return String the designated column's database-specific type name.
	 */
	public String getColumnTypeName() {
		return this.columnTypeName;
	}

	/**
	 * the designated column's database-specific type name.��ݒ肷��
	 *
	 * @param columnTypeName �V����the designated column's database-specific type name.
	 */
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	/**
	 * the designated column's SQL type���擾����
	 *
	 * @return int the designated column's SQL type
	 */
	public int getColumnType() {
		return this.columnType;
	}

	/**
	 * the designated column's SQL type��ݒ肷��
	 *
	 * @param columnType �V����the designated column's SQL type
	 */
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	/**
	 * the designated column's normal maximum width in characters���擾����
	 *
	 * @return int the designated column's normal maximum width in characters
	 */
	public int getColumnSize() {
		return this.columnSize;
	}

	/**
	 * the designated column's normal maximum width in characters��ݒ肷��
	 *
	 * @param columnSize �V����the designated column's normal maximum width in characters
	 */
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	/**
	 * the designated column's number of decimal digits���擾����
	 *
	 * @return int the designated column's number of decimal digits
	 */
	public int getPrecision() {
		return this.precision;
	}

	/**
	 * the designated column's number of decimal digits��ݒ肷��
	 *
	 * @param precision �V����the designated column's number of decimal digits
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * define whether the column is nullable���擾����
	 *
	 * @return String define whether the column is nullable
	 */
	public String getIsNullable() {
		return this.isNullable;
	}

	/**
	 * define whether the column is nullable��ݒ肷��
	 *
	 * @param isNullable �V����define whether the column is nullable
	 */
	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	/**
	 * defined the column is primary key, return the sequence of the primary key���擾����
	 *
	 * @return int defined the column is primary key, return the sequence of the primary key
	 */
	public int getPrimaryKeySeq() {
		return this.primaryKeySeq;
	}

	/**
	 * defined the column is primary key, return the sequence of the primary key��ݒ肷��
	 *
	 * @param primaryKeySeq �V����defined the column is primary key, return the sequence of the primary key
	 */
	public void setPrimaryKeySeq(int primaryKeySeq) {
		this.primaryKeySeq = primaryKeySeq;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	/**
	 * @return defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}


	/**
	 * @param defaultValue �Z�b�g���� defaultValue
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	/**
	 * �f�B�o�O�p�̃��b�\�h
	 *
	 * @return String �S���t�B�[���h�̒l
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("columnName:		" + columnName + "\n");
		sb.append("columnTypeName:		" + columnTypeName + "\n");
		sb.append("columnType:		" + columnType + "\n");
		sb.append("columnSize:		" + columnSize + "\n");
		sb.append("precision:		" + precision + "\n");
		sb.append("isNullable:		" + isNullable + "\n");
		sb.append("primaryKeySeq:		" + primaryKeySeq + "\n");
		sb.append("comment:		" + comment + "\n");

		return sb.toString();
	}
}