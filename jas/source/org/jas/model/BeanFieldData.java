package org.jas.model;

import org.jdom.Element;

/**
 *
 *
 *
 *
 * @author ���@�w�R
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
	 * �f�B�t�H���g�R���X�g���N�V����
	 */
	public BeanFieldData() {}


	/**
	 * columnName���擾����
	 *
	 * @return String columnName
	 */
	public String getColumnName() {
		return this.columnName;
	}

	/**
	 * columnName��ݒ肷��
	 *
	 * @param columnName �V����columnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * upperColumnName���擾����
	 *
	 * @return String upperColumnName
	 */
	public String getUpperColumnName() {
		return this.upperColumnName;
	}

	/**
	 * upperColumnName��ݒ肷��
	 *
	 * @param upperColumnName �V����upperColumnName
	 */
	public void setUpperColumnName(String upperColumnName) {
		this.upperColumnName = upperColumnName;
	}

	/**
	 * columnType���擾����
	 *
	 * @return String columnType
	 */
	public String getColumnType() {
		return this.columnType;
	}

	/**
	 * columnType��ݒ肷��
	 *
	 * @param columnType �V����columnType
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	/**
	 * comment���擾����
	 *
	 * @return String comment
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * comment��ݒ肷��
	 *
	 * @param comment �V����comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * needGetter���擾����
	 *
	 * @return boolean needGetter
	 */
	public boolean getNeedGetter() {
		return this.needGetter;
	}

	/**
	 * needGetter��ݒ肷��
	 *
	 * @param needGetter �V����needGetter
	 */
	public void setNeedGetter(boolean needGetter) {
		this.needGetter = needGetter;
	}

	/**
	 * needSetter���擾����
	 *
	 * @return boolean needSetter
	 */
	public boolean getNeedSetter() {
		return this.needSetter;
	}

	/**
	 * needSetter��ݒ肷��
	 *
	 * @param needSetter �V����needSetter
	 */
	public void setNeedSetter(boolean needSetter) {
		this.needSetter = needSetter;
	}

	/**
	 * defaultValue���擾����
	 *
	 * @return String defaultValue
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * defaultValue��ݒ肷��
	 *
	 * @param defaultValue �V����defaultValue
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * primaryKey���擾����
	 *
	 * @return boolean primaryKey
	 */
	public boolean getPrimaryKey() {
		return this.primaryKey;
	}

	/**
	 * primaryKey��ݒ肷��
	 *
	 * @param primaryKey �V����primaryKey
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}


	/**
	 * �f�B�o�O�p�̃��b�\�h
	 *
	 * @return String �S���t�B�[���h�̒l
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