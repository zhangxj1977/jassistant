package org.jas.model;

import java.io.Serializable;

/**
 * テーブルdescriptionデータ
 *
 * @author 張　学軍
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
	 * ディフォルトコンストラクション
	 */
	public ColumnDescriptionData() {}


	/**
	 * the designated column's nameを取得する
	 *
	 * @return String the designated column's name
	 */
	public String getColumnName() {
		return this.columnName;
	}

	/**
	 * the designated column's nameを設定する
	 *
	 * @param columnName 新しいthe designated column's name
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * the designated column's database-specific type name.を取得する
	 *
	 * @return String the designated column's database-specific type name.
	 */
	public String getColumnTypeName() {
		return this.columnTypeName;
	}

	/**
	 * the designated column's database-specific type name.を設定する
	 *
	 * @param columnTypeName 新しいthe designated column's database-specific type name.
	 */
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	/**
	 * the designated column's SQL typeを取得する
	 *
	 * @return int the designated column's SQL type
	 */
	public int getColumnType() {
		return this.columnType;
	}

	/**
	 * the designated column's SQL typeを設定する
	 *
	 * @param columnType 新しいthe designated column's SQL type
	 */
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	/**
	 * the designated column's normal maximum width in charactersを取得する
	 *
	 * @return int the designated column's normal maximum width in characters
	 */
	public int getColumnSize() {
		return this.columnSize;
	}

	/**
	 * the designated column's normal maximum width in charactersを設定する
	 *
	 * @param columnSize 新しいthe designated column's normal maximum width in characters
	 */
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	/**
	 * the designated column's number of decimal digitsを取得する
	 *
	 * @return int the designated column's number of decimal digits
	 */
	public int getPrecision() {
		return this.precision;
	}

	/**
	 * the designated column's number of decimal digitsを設定する
	 *
	 * @param precision 新しいthe designated column's number of decimal digits
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * define whether the column is nullableを取得する
	 *
	 * @return String define whether the column is nullable
	 */
	public String getIsNullable() {
		return this.isNullable;
	}

	/**
	 * define whether the column is nullableを設定する
	 *
	 * @param isNullable 新しいdefine whether the column is nullable
	 */
	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	/**
	 * defined the column is primary key, return the sequence of the primary keyを取得する
	 *
	 * @return int defined the column is primary key, return the sequence of the primary key
	 */
	public int getPrimaryKeySeq() {
		return this.primaryKeySeq;
	}

	/**
	 * defined the column is primary key, return the sequence of the primary keyを設定する
	 *
	 * @param primaryKeySeq 新しいdefined the column is primary key, return the sequence of the primary key
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
	 * @param defaultValue セットする defaultValue
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	/**
	 * ディバグ用のメッソド
	 *
	 * @return String 全部フィールドの値
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