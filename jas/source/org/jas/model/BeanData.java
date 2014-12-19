package org.jas.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.jdom.Element;

/**
 *
 * the data bean structure
 *
 *
 * @author ���@�w�R
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
	 * �f�B�t�H���g�R���X�g���N�V����
	 */
	public BeanData() {}


	/**
	 * packageDefine���擾����
	 *
	 * @return String packageDefine
	 */
	public String getPackageDefine() {
		return this.packageDefine;
	}

	/**
	 * packageDefine��ݒ肷��
	 *
	 * @param packageDefine �V����packageDefine
	 */
	public void setPackageDefine(String packageDefine) {
		this.packageDefine = packageDefine;
	}

	/**
	 * importClassesDefine���擾����
	 *
	 * @return String importClassesDefine
	 */
	public String getImportClassesDefine() {
		return this.importClassesDefine;
	}

	/**
	 * importClassesDefine��ݒ肷��
	 *
	 * @param importClassesDefine �V����importClassesDefine
	 */
	public void setImportClassesDefine(String importClassesDefine) {
		this.importClassesDefine = importClassesDefine;
	}

	/**
	 * classDescription���擾����
	 *
	 * @return String classDescription
	 */
	public String getClassDescription() {
		return this.classDescription;
	}

	/**
	 * classDescription��ݒ肷��
	 *
	 * @param classDescription �V����classDescription
	 */
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	/**
	 * author���擾����
	 *
	 * @return String author
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * author��ݒ肷��
	 *
	 * @param author �V����author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * version���擾����
	 *
	 * @return String version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * version��ݒ肷��
	 *
	 * @param version �V����version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * className���擾����
	 *
	 * @return String className
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * className��ݒ肷��
	 *
	 * @param className �V����className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * needToString���擾����
	 *
	 * @return boolean needToString
	 */
	public boolean getNeedToString() {
		return this.needToString;
	}

	/**
	 * needToString��ݒ肷��
	 *
	 * @param needToString �V����needToString
	 */
	public void setNeedToString(boolean needToString) {
		this.needToString = needToString;
	}

	/**
	 * needDefaultConstructor���擾����
	 *
	 * @return boolean needDefaultConstructor
	 */
	public boolean getNeedDefaultConstructor() {
		return this.needDefaultConstructor;
	}

	/**
	 * needDefaultConstructor��ݒ肷��
	 *
	 * @param needDefaultConstructor �V����needDefaultConstructor
	 */
	public void setNeedDefaultConstructor(boolean needDefaultConstructor) {
		this.needDefaultConstructor = needDefaultConstructor;
	}

	/**
	 * beanFieldDataList���擾����
	 *
	 * @return ArrayList beanFieldDataList
	 */
	public ArrayList getBeanFieldDataList() {
		return this.beanFieldDataList;
	}

	/**
	 * beanFieldDataList��ݒ肷��
	 *
	 * @param beanFieldDataList �V����beanFieldDataList
	 */
	public void setBeanFieldDataList(ArrayList beanFieldDataList) {
		this.beanFieldDataList = beanFieldDataList;
	}

	/**
	 * serializable���擾����
	 *
	 * @return String serializable
	 */
	public String getSerializable() {
		return this.serializable;
	}

	/**
	 * serializable��ݒ肷��
	 *
	 * @param serializable �V����serializable
	 */
	public void setSerializable(String serializable) {
		this.serializable = serializable;
	}

	/**
	 * commentNullToName���擾����
	 *
	 * @return boolean commentNullToName
	 */
	public boolean getCommentNullToName() {
		return this.commentNullToName;
	}

	/**
	 * commentNullToName��ݒ肷��
	 *
	 * @param commentNullToName �V����commentNullToName
	 */
	public void setCommentNullToName(boolean commentNullToName) {
		this.commentNullToName = commentNullToName;
	}


	/**
	 * �f�B�o�O�p�̃��b�\�h
	 *
	 * @return String �S���t�B�[���h�̒l
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