package org.jas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import org.jas.common.PJConst;
import org.jas.model.BeanData;
import org.jas.model.BeanFieldData;

/**
 *
 *
 * @author 張　学軍
 * @version 1.0
 */

public class TemplateManager {

	/**
	 * template file content
	 */
	StringBuffer sb = new StringBuffer();

	/**
	 * BeanData object
	 */
	BeanData beanData = new BeanData();

	/**
	 * 画面テーブルデータ
	 */
	Vector tableData = new Vector();

	/**
	 * import classes map
	 */
	HashMap importClassesMap = new HashMap();

	/**
	 * paren panel
	 */
	String packageName = "";

	/**
	 * class description
	 */
	String classDescription = "";

	/**
	 * java bean file
	 */
	File beanFile;


	/**
	 * default constructor
	 */
	public TemplateManager(String packageName, String classDescription,
							String configPath, Vector tableData, File beanFile) {
		this.packageName = packageName;
		this.classDescription = classDescription;
		this.tableData = tableData;
		this.beanFile = beanFile;
		initTemplate(configPath);
		initBeanData();
	}

	/**
	 * template file 初期化
	 */
	private void initTemplate(String configPath) {
		try {
			String defaultTemplateFile;
			Locale currentLocale = Locale.getDefault();
			if (currentLocale.equals(Locale.JAPAN)) {
				defaultTemplateFile = PJConst.DEFAULT_TEMPLATEFILE_JA_PATH;
			} else if (currentLocale.equals(Locale.CHINA)) {
				defaultTemplateFile = PJConst.DEFAULT_TEMPLATEFILE_CN_PATH;
			} else {
				defaultTemplateFile = PJConst.DEFAULT_TEMPLATEFILE_EN_PATH;
			}
			File templateFile = new File(configPath, "databean_template.properties");
			if (!templateFile.exists()) {
				InputStream in = FileManager.getResourcePath(defaultTemplateFile).openStream();
				sb = FileManager.readInputStream(in);
				if (!templateFile.exists()) {
					(new File(configPath)).mkdirs();
				}
				FileManager.writeFile(sb.toString(), templateFile);
			} else {
				sb = FileManager.readInputStream(new FileInputStream(templateFile));
			}
		} catch (Exception me) {
			MessageManager.showMessage("MCSTC002E", me.getMessage());
		}
	}


	/**
	 * init bean data from properties and tables
	 */
	private void initBeanData() {
		beanData.setBeanFieldDataList(getBeanFieldDataList());

		if (packageName.trim().length() > 0) {
			beanData.setPackageDefine("package " + packageName + ";\n");
		} else {
			beanData.setPackageDefine("");
		}
		beanData.setImportClassesDefine(getImportClasses());
		beanData.setClassDescription(classDescription);
		beanData.setAuthor(PropertyManager.getProperty(PJConst.OPTIONS_GENERAL_USER_AUTHOR));
		beanData.setVersion(PropertyManager.getProperty(PJConst.OPTIONS_GENERAL_USER_VERSION));
		beanData.setClassName(beanFile.getName().substring(0, beanFile.getName().lastIndexOf(".java")));

		beanData.setCommentNullToName(PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULTNAMEIFNULL)));
		beanData.setNeedDefaultConstructor(PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_BEANINFO_DEFAULTCONSTRUCTOR)));
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_BEANINFO_SERIALIZABLE))) {
			beanData.setSerializable("implements Serializable ");
		} else {
			beanData.setSerializable("");
		}

		beanData.setNeedToString(PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_METHOD_TOSTRING)));
	}


	/**
	 * convert the table data to
	 * create bean need data
	 */
	private ArrayList getBeanFieldDataList() {
		ArrayList columnDataList = new ArrayList();

		for (int i=0; i<tableData.size(); i++) {
			Vector vecOneColumn = (Vector) tableData.get(i);

			String columnName = (String) vecOneColumn.get(1);
			String columnType = (String) vecOneColumn.get(2);
			String defaultValue = (String) vecOneColumn.get(3);
			boolean needGetter = ((Boolean) vecOneColumn.get(4)).booleanValue();
			boolean needSetter = ((Boolean) vecOneColumn.get(5)).booleanValue();
			String comment = (String) vecOneColumn.get(6);
			boolean isPrimaryKey = ((Boolean) vecOneColumn.get(7)).booleanValue();

			if (columnName == null || columnName.equals("") ||
				columnType == null || columnType.equals("")) {
				continue;
			}
			columnName = columnName.substring(0, 1).toLowerCase() + columnName.substring(1);

			int classPos = columnType.lastIndexOf(".");
			if (classPos > 0) {
				String importPackage = columnType.substring(0, classPos);
				if (!"java.lang".equals(importPackage)) {
					importClassesMap.put(columnType, "");
				}
				columnType = columnType.substring(classPos + 1);
			}
			String upperColumnName = columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

			if (columnType.equals("String") && !defaultValue.equals("null")) {
				defaultValue = "\"" + defaultValue + "\"";
			} else if (columnType.equals("char")) {
				defaultValue = "\'" + defaultValue + "\'";
			}

			if (comment == null || comment.equals("")) {
				if (beanData.getCommentNullToName()) {
					comment = columnName;
				}
			}

			BeanFieldData oneBeanFieldData = new BeanFieldData();

			oneBeanFieldData.setColumnName(columnName);
			oneBeanFieldData.setColumnType(columnType);
			oneBeanFieldData.setDefaultValue(defaultValue);
			oneBeanFieldData.setUpperColumnName(upperColumnName);
			oneBeanFieldData.setNeedGetter(needGetter);
			oneBeanFieldData.setNeedSetter(needSetter);
			oneBeanFieldData.setComment(comment);
			oneBeanFieldData.setPrimaryKey(isPrimaryKey);

			columnDataList.add(oneBeanFieldData);
		}

		return columnDataList;
	}

	/**
	 * get need import class string
	 */
	private String getImportClasses() {
		StringBuffer sbImportClasses = new StringBuffer();

		Iterator it = importClassesMap.keySet().iterator();
		while (it.hasNext()) {
			String strImportClass = (String) it.next();
			sbImportClasses.append("import " + strImportClass + ";\n");
		}

		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_BEANINFO_SERIALIZABLE))) {
			sbImportClasses.append("import java.io.Serializable;" + "\n");
		}

		return sbImportClasses.toString();
	}


	/**
	 * create java file
	 */
	public void createJavaBean() throws IllegalArgumentException, IOException {
		String fileString = processReplace(sb.toString());
		FileManager.writeFile(fileString, beanFile);
	}


	/**
	 * process package define
	 */
	private String processReplace(String templateContent) throws IllegalArgumentException {
		StringBuffer returnContent = new StringBuffer(templateContent.length());
		int contentLength = templateContent.length();

		int posEnd = 0;
		for (int index = 0; index < contentLength; index ++) {
			char currentChar = templateContent.charAt(index);
			if (currentChar == '<') {
				if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_BEGIN)) {
					posEnd = templateContent.indexOf(PJConst.TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_END, index);
					if (posEnd < 0) {
						throw new IllegalArgumentException(PJConst.TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_END);
					}
					if (beanData.getNeedDefaultConstructor()) {
						index = index + PJConst.TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_BEGIN.length() - 1;
					} else {
						index = posEnd + PJConst.TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_END.length() - 1;
					}
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IF_TOSTRING_NEED_BEGIN)) {
					posEnd = templateContent.indexOf(PJConst.TEMPLATE_TAG_IF_TOSTRING_NEED_END, index);
					if (posEnd < 0) {
						throw new IllegalArgumentException(PJConst.TEMPLATE_TAG_IF_TOSTRING_NEED_END);
					}
					if (beanData.getNeedToString()) {
						index = index + PJConst.TEMPLATE_TAG_IF_TOSTRING_NEED_BEGIN.length() - 1;
					} else {
						index = posEnd + PJConst.TEMPLATE_TAG_IF_TOSTRING_NEED_END.length() - 1;
					}
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_LOOP_COLUMN_BEGIN)) {
					posEnd = templateContent.indexOf(PJConst.TEMPLATE_TAG_LOOP_COLUMN_END, index);
					if  (posEnd < 0) {
						throw new IllegalArgumentException(PJConst.TEMPLATE_TAG_LOOP_COLUMN_END);
					}
					index = index + PJConst.TEMPLATE_TAG_LOOP_COLUMN_BEGIN.length();
					returnContent.append(processLoopColumnReplace(templateContent.substring(index, posEnd)));
					index = posEnd - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_END)) {
					index = index + PJConst.TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_END.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IF_TOSTRING_NEED_END)) {
					index = index + PJConst.TEMPLATE_TAG_IF_TOSTRING_NEED_END.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_LOOP_COLUMN_END)) {
					index = index + PJConst.TEMPLATE_TAG_LOOP_COLUMN_END.length() - 1;
				} else {
					returnContent.append(currentChar);
				}
			} else if (currentChar == '$') {
				if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_CREATE_DATE)) {
					returnContent.append(DateUtil.toString(new Date(System.currentTimeMillis())));
					index = index + PJConst.TEMPLATE_TAG_CREATE_DATE.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_PACKAGE_DEFINE)) {
					returnContent.append(beanData.getPackageDefine());
					index = index + PJConst.TEMPLATE_TAG_PACKAGE_DEFINE.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IMPORT_CLASSES_DEFINE)) {
					returnContent.append(beanData.getImportClassesDefine());
					index = index + PJConst.TEMPLATE_TAG_IMPORT_CLASSES_DEFINE.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_CLASS_DESCRIPTION)) {
					returnContent.append(beanData.getClassDescription());
					index = index + PJConst.TEMPLATE_TAG_CLASS_DESCRIPTION.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_AUTHOR)) {
					returnContent.append(beanData.getAuthor());
					index = index + PJConst.TEMPLATE_TAG_AUTHOR.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_VERSION)) {
					returnContent.append(beanData.getVersion());
					index = index + PJConst.TEMPLATE_TAG_VERSION.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_CLASS_NAME)) {
					returnContent.append(beanData.getClassName());
					index = index + PJConst.TEMPLATE_TAG_CLASS_NAME.length() - 1;
				} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_SERIALIZABLE)) {
					returnContent.append(beanData.getSerializable());
					index = index + PJConst.TEMPLATE_TAG_SERIALIZABLE.length() - 1;
				} else {
					returnContent.append(currentChar);
				}
			} else {
				returnContent.append(currentChar);
			}
		}

		return returnContent.toString();
	}


	/**
	 * process loop_column tag
	 *
	 */
	private String processLoopColumnReplace(String templateContent) throws IllegalArgumentException {
		StringBuffer returnContent = new StringBuffer();
		ArrayList columnList = beanData.getBeanFieldDataList();
		int contentLength = templateContent.length();

		for (int i = 0; i < columnList.size(); i++) {
			StringBuffer replacedContent = new StringBuffer();
			BeanFieldData oneColumn = (BeanFieldData) columnList.get(i);

			int posEnd = 0;
			for (int index = 0; index < contentLength; index ++) {
				char currentChar = templateContent.charAt(index);
				if (currentChar == '<') {
					if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IF_GETTER_NEED_BEGIN)) {
						posEnd = templateContent.indexOf(PJConst.TEMPLATE_TAG_IF_GETTER_NEED_END, index);
						if (posEnd < 0) {
							throw new IllegalArgumentException(PJConst.TEMPLATE_TAG_IF_GETTER_NEED_END);
						}
						if (oneColumn.getNeedGetter()) {
							index = index + PJConst.TEMPLATE_TAG_IF_GETTER_NEED_BEGIN.length() - 1;
						} else {
							index = posEnd + PJConst.TEMPLATE_TAG_IF_GETTER_NEED_END.length() - 1;
						}
					} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IF_SETTER_NEED_BEGIN)) {
						posEnd = templateContent.indexOf(PJConst.TEMPLATE_TAG_IF_SETTER_NEED_BEGIN, index);
						if (posEnd < 0) {
							throw new IllegalArgumentException(PJConst.TEMPLATE_TAG_IF_SETTER_NEED_BEGIN);
						}
						if (oneColumn.getNeedSetter()) {
							index = index + PJConst.TEMPLATE_TAG_IF_SETTER_NEED_BEGIN.length() - 1;
						} else {
							index = posEnd + PJConst.TEMPLATE_TAG_IF_SETTER_NEED_END.length() - 1;
						}
					} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IF_GETTER_NEED_END)) {
						index = index + PJConst.TEMPLATE_TAG_IF_GETTER_NEED_END.length() - 1;
					} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_IF_SETTER_NEED_END)) {
						index = index + PJConst.TEMPLATE_TAG_IF_SETTER_NEED_END.length() - 1;
					} else {
						replacedContent.append(currentChar);
					}
				} else if (currentChar == '$') {
					if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_COLUMN_NAME)) {
						replacedContent.append(oneColumn.getColumnName());
						index = index + PJConst.TEMPLATE_TAG_COLUMN_NAME.length() - 1;
					} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_COLUMN_TYPE)) {
						replacedContent.append(oneColumn.getColumnType());
						index = index + PJConst.TEMPLATE_TAG_COLUMN_TYPE.length() - 1;
					} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_UPPER_COLUMN_NAME)) {
						replacedContent.append(oneColumn.getUpperColumnName());
						index = index + PJConst.TEMPLATE_TAG_UPPER_COLUMN_NAME.length() - 1;
					} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_COLUMN_COMMENT)) {
						replacedContent.append(oneColumn.getComment());
						index = index + PJConst.TEMPLATE_TAG_COLUMN_COMMENT.length() - 1;
					} else if (templateContent.substring(index).startsWith(PJConst.TEMPLATE_TAG_DEFAULT_VALUE)) {
						replacedContent.append(oneColumn.getDefaultValue());
						index = index + PJConst.TEMPLATE_TAG_DEFAULT_VALUE.length() - 1;
					} else {
						replacedContent.append(currentChar);
					}
				} else {
					replacedContent.append(currentChar);
				}
			}

			returnContent.append(replacedContent.toString());
		}

		return returnContent.toString();
	}

}