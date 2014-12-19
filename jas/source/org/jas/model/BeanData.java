package org.jas.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.jdom.Element;

/**
 *
 * the data bean structure
 *
 *
 * @author 張　学軍
 * @version 1.0
 */

public class BeanData implements Serializable {

	/**
	 * packageDefine
	 */
	private String packageDefine = null;

	/**
	 * importClassesDefine
	 */
	private String importClassesDefine = null;

	/**
	 * classDescription
	 */
	private String classDescription = null;

	/**
	 * author
	 */
	private String author = null;

	/**
	 * version
	 */
	private String version = null;

	/**
	 * className
	 */
	private String className = null;

	/**
	 * needToString
	 */
	private boolean needToString = true;

	/**
	 * needDefaultConstructor
	 */
	private boolean needDefaultConstructor = true;

	/**
	 * beanFieldDataList
	 */
	private ArrayList beanFieldDataList = null;

	/**
	 * serializable
	 */
	private String serializable = null;

	/**
	 * commentNullToName
	 */
	private boolean commentNullToName = true;


	/**
	 * ディフォルトコンストラクション
	 */
	public BeanData() {}


	/**
	 * packageDefineを取得する
	 *
	 * @return String packageDefine
	 */
	public String getPackageDefine() {
		return this.packageDefine;
	}

	/**
	 * packageDefineを設定する
	 *
	 * @param packageDefine 新しいpackageDefine
	 */
	public void setPackageDefine(String packageDefine) {
		this.packageDefine = packageDefine;
	}

	/**
	 * importClassesDefineを取得する
	 *
	 * @return String importClassesDefine
	 */
	public String getImportClassesDefine() {
		return this.importClassesDefine;
	}

	/**
	 * importClassesDefineを設定する
	 *
	 * @param importClassesDefine 新しいimportClassesDefine
	 */
	public void setImportClassesDefine(String importClassesDefine) {
		this.importClassesDefine = importClassesDefine;
	}

	/**
	 * classDescriptionを取得する
	 *
	 * @return String classDescription
	 */
	public String getClassDescription() {
		return this.classDescription;
	}

	/**
	 * classDescriptionを設定する
	 *
	 * @param classDescription 新しいclassDescription
	 */
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	/**
	 * authorを取得する
	 *
	 * @return String author
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * authorを設定する
	 *
	 * @param author 新しいauthor
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * versionを取得する
	 *
	 * @return String version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * versionを設定する
	 *
	 * @param version 新しいversion
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * classNameを取得する
	 *
	 * @return String className
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * classNameを設定する
	 *
	 * @param className 新しいclassName
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * needToStringを取得する
	 *
	 * @return boolean needToString
	 */
	public boolean getNeedToString() {
		return this.needToString;
	}

	/**
	 * needToStringを設定する
	 *
	 * @param needToString 新しいneedToString
	 */
	public void setNeedToString(boolean needToString) {
		this.needToString = needToString;
	}

	/**
	 * needDefaultConstructorを取得する
	 *
	 * @return boolean needDefaultConstructor
	 */
	public boolean getNeedDefaultConstructor() {
		return this.needDefaultConstructor;
	}

	/**
	 * needDefaultConstructorを設定する
	 *
	 * @param needDefaultConstructor 新しいneedDefaultConstructor
	 */
	public void setNeedDefaultConstructor(boolean needDefaultConstructor) {
		this.needDefaultConstructor = needDefaultConstructor;
	}

	/**
	 * beanFieldDataListを取得する
	 *
	 * @return ArrayList beanFieldDataList
	 */
	public ArrayList getBeanFieldDataList() {
		return this.beanFieldDataList;
	}

	/**
	 * beanFieldDataListを設定する
	 *
	 * @param beanFieldDataList 新しいbeanFieldDataList
	 */
	public void setBeanFieldDataList(ArrayList beanFieldDataList) {
		this.beanFieldDataList = beanFieldDataList;
	}

	/**
	 * serializableを取得する
	 *
	 * @return String serializable
	 */
	public String getSerializable() {
		return this.serializable;
	}

	/**
	 * serializableを設定する
	 *
	 * @param serializable 新しいserializable
	 */
	public void setSerializable(String serializable) {
		this.serializable = serializable;
	}

	/**
	 * commentNullToNameを取得する
	 *
	 * @return boolean commentNullToName
	 */
	public boolean getCommentNullToName() {
		return this.commentNullToName;
	}

	/**
	 * commentNullToNameを設定する
	 *
	 * @param commentNullToName 新しいcommentNullToName
	 */
	public void setCommentNullToName(boolean commentNullToName) {
		this.commentNullToName = commentNullToName;
	}


	/**
	 * ディバグ用のメッソド
	 *
	 * @return String 全部フィールドの値
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("packageDefine:		" + packageDefine + "\n");
		sb.append("importClassesDefine:		" + importClassesDefine + "\n");
		sb.append("classDescription:		" + classDescription + "\n");
		sb.append("author:		" + author + "\n");
		sb.append("version:		" + version + "\n");
		sb.append("className:		" + className + "\n");
		sb.append("needToString:		" + needToString + "\n");
		sb.append("needDefaultConstructor:		" + needDefaultConstructor + "\n");
		sb.append("beanFieldDataList:		" + beanFieldDataList + "\n");
		sb.append("serializable:		" + serializable + "\n");
		sb.append("commentNullToName:		" + commentNullToName + "\n");

		return sb.toString();
	}

    /**
     * get Element
     *
     */
    public Element toElement() {
		Element rootElement = new Element("root");

		Element classNameElement = new Element("class_name");
		classNameElement.setText(className);
		rootElement.addContent(classNameElement);

		Element packageDefineElement = new Element("package_define");
		packageDefineElement.setText(packageDefine);
		rootElement.addContent(packageDefineElement);

		Element importClassesDefineElement = new Element("");

		Element classDescriptionElement = new Element("class_description");
		classDescriptionElement.setText(classDescription);
		rootElement.addContent(classDescriptionElement);

		Element authorElement = new Element("author");
		Element versionElement = new Element("version");
		Element needToStringElement = new Element("tostring");
		Element needDefaultConstructorElement = new Element("constructor");
		Element beanFieldDataList = null;
		Element serializableElement = new Element("serializable");

		return rootElement;
	}
}