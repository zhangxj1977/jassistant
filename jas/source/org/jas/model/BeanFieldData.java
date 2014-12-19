package org.jas.model;

import org.jdom.Element;

/**
 *
 *
 *
 *
 * @author 張　学軍
 * @version 1.0
 */
public class BeanFieldData {

	/**
	 * columnName
	 */
	private String columnName = null;

	/**
	 * upperColumnName
	 */
	private String upperColumnName = null;

	/**
	 * columnType
	 */
	private String columnType = null;

	/**
	 * comment
	 */
	private String comment = null;

	/**
	 * needGetter
	 */
	private boolean needGetter = true;

	/**
	 * needSetter
	 */
	private boolean needSetter = true;

	/**
	 * defaultValue
	 */
	private String defaultValue = null;

	/**
	 * primaryKey
	 */
	private boolean primaryKey = false;


	/**
	 * ディフォルトコンストラクション
	 */
	public BeanFieldData() {}


	/**
	 * columnNameを取得する
	 *
	 * @return String columnName
	 */
	public String getColumnName() {
		return this.columnName;
	}

	/**
	 * columnNameを設定する
	 *
	 * @param columnName 新しいcolumnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * upperColumnNameを取得する
	 *
	 * @return String upperColumnName
	 */
	public String getUpperColumnName() {
		return this.upperColumnName;
	}

	/**
	 * upperColumnNameを設定する
	 *
	 * @param upperColumnName 新しいupperColumnName
	 */
	public void setUpperColumnName(String upperColumnName) {
		this.upperColumnName = upperColumnName;
	}

	/**
	 * columnTypeを取得する
	 *
	 * @return String columnType
	 */
	public String getColumnType() {
		return this.columnType;
	}

	/**
	 * columnTypeを設定する
	 *
	 * @param columnType 新しいcolumnType
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	/**
	 * commentを取得する
	 *
	 * @return String comment
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * commentを設定する
	 *
	 * @param comment 新しいcomment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * needGetterを取得する
	 *
	 * @return boolean needGetter
	 */
	public boolean getNeedGetter() {
		return this.needGetter;
	}

	/**
	 * needGetterを設定する
	 *
	 * @param needGetter 新しいneedGetter
	 */
	public void setNeedGetter(boolean needGetter) {
		this.needGetter = needGetter;
	}

	/**
	 * needSetterを取得する
	 *
	 * @return boolean needSetter
	 */
	public boolean getNeedSetter() {
		return this.needSetter;
	}

	/**
	 * needSetterを設定する
	 *
	 * @param needSetter 新しいneedSetter
	 */
	public void setNeedSetter(boolean needSetter) {
		this.needSetter = needSetter;
	}

	/**
	 * defaultValueを取得する
	 *
	 * @return String defaultValue
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * defaultValueを設定する
	 *
	 * @param defaultValue 新しいdefaultValue
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * primaryKeyを取得する
	 *
	 * @return boolean primaryKey
	 */
	public boolean getPrimaryKey() {
		return this.primaryKey;
	}

	/**
	 * primaryKeyを設定する
	 *
	 * @param primaryKey 新しいprimaryKey
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}


	/**
	 * ディバグ用のメッソド
	 *
	 * @return String 全部フィールドの値
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("columnName:		" + columnName + "\n");
		sb.append("upperColumnName:		" + upperColumnName + "\n");
		sb.append("columnType:		" + columnType + "\n");
		sb.append("comment:		" + comment + "\n");
		sb.append("needGetter:		" + needGetter + "\n");
		sb.append("needSetter:		" + needSetter + "\n");
		sb.append("defaultValue:		" + defaultValue + "\n");
		sb.append("primaryKey:		" + primaryKey + "\n");

		return sb.toString();
	}


    /**
     * create xml element node
     *
     */
    public Element toElement() {
    	Element columnElement = new Element("column");

		Element columnNameElement = new Element("name");
		columnNameElement.setText(columnName);
		columnElement.addContent(columnNameElement);

		Element upperColumnNameElement = new Element("uppername");
		upperColumnNameElement.setText(upperColumnName);
		columnElement.addContent(upperColumnNameElement);

		Element columnTypeElement = new Element("type");
		columnTypeElement.setText(columnType);
		columnElement.addContent(columnTypeElement);

		Element commentElement = new Element("comment");
		commentElement.setText(comment);
		columnElement.addContent(commentElement);

		if (needGetter) {
			Element needGetterElement = new Element("getter");
			columnElement.addContent(needGetterElement);
		}

		if (needSetter) {
			Element needSetterElement = new Element("setter");
			columnElement.addContent(needSetterElement);
		}

		Element defaultValueElement = new Element("default");
		defaultValueElement.setText(defaultValue);
		columnElement.addContent(defaultValueElement);

		if (primaryKey) {
			Element primaryKeyElement = new Element("key");
			columnElement.addContent(primaryKeyElement);
		}

		return columnElement;
	}
}