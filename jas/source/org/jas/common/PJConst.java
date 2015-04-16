package org.jas.common;

import java.io.Serializable;

/**
 *
 * @author 張　学軍
 * @version 1.0
 */

public interface PJConst extends Serializable {

	/**
	 * 画面フラグ定義
	 */
	public static final int WINDOW_OPENCONNECTION    = 0;
	public static final int WINDOW_OPTIONSCONFIG     = 1;
	public static final int WINDOW_CONFIGCONNECTION  = 2;
	public static final int WINDOW_CONFIGGRIDFONT    = 3;
	public static final int WINDOW_CONFIGSQLFONT     = 4;
	public static final int WINDOW_CONFIGFILTERSORT  = 5;
	public static final int WINDOW_EDITONEROW        = 6;
	public static final int WINDOW_FINDGRIDDATA      = 7;
	public static final int WINDOW_EDITONECOLUMN     = 8;
	public static final int WINDOW_BATCHINSERTROWS   = 9;
	public static final int WINDOW_IMPORTTABLEDATA   = 10;
	public static final int WINDOW_SELECTCOLUMNS     = 11;
	public static final int WINDOW_FILTERTABLES      = 12;

	/**
	 * resultset fetch buffer size
	 */
	public static final int RESULT_FETCH_BUFFER = 150;

	/**
	 * 配置定義
	 */
	public static final String DEFAULT_CONFIG_DIR        = "./config/";
	public static final String DEFAULT_CONFIGFILE_PATH   = "/org/jas/util/resource/config/configuration.properties";
	public static final String DEFAULT_TEMPLATEFILE_JA_PATH = "/org/jas/util/resource/config/databean_template_ja.properties";
	public static final String DEFAULT_TEMPLATEFILE_CN_PATH = "/org/jas/util/resource/config/databean_template_cn.properties";
	public static final String DEFAULT_TEMPLATEFILE_EN_PATH = "/org/jas/util/resource/config/databean_template_en.properties";

	public static final String HELP_CONTENT   = "/org/jas/util/resource/help/help.txt";
	public static final String HELP_IMAGE_DIR   = "/org/jas/util/resource/help/images/";

	public static final String DEFAULT_MESSAGEFILE_JA_PATH = "org.jas.util.resource.messages.japanese";
	public static final String DEFAULT_MESSAGEFILE_CN_PATH = "org.jas.util.resource.messages.chinese";
	public static final String DEFAULT_MESSAGEFILE_EN_PATH = "org.jas.util.resource.messages.english";

	public static final String COLUMN_NAME_CASE_NO      = "0";
	public static final String COLUMN_NAME_CASE_LOWCASE = "1";
	public static final String COLUMN_NAME_CASE_UPCASE  = "2";

	public static final String OPTIONS_COLUMN_COMMENT_DEFAULT_NULL = "0";
	public static final String OPTIONS_COLUMN_COMMENT_DEFAULT_NAME = "1";

	public static final String OPTIONS_TRUE  = "1";
	public static final String OPTIONS_FALSE = "0";

	public static final String[] DEFAULT_DATABASE_DRIVER = {
															"oracle.jdbc.driver.OracleDriver"
														};
	public static final String[] DEFAULT_DATABASE_URL = {
															"jdbc:oracle:thin:@hostname:1521:orcl",
															"jdbc:oracle:oci8:@database"
														};

	/**
	 * 配置名定義
	 */
	public static final String DATABASE_CONNECTIONS           = "database.connection";
	public static final String DATABASE_NAMES                 = "database.name";

	public static final String OPTIONS_COLUMN_NAME_CASE       = "options.column.name.case";

	public static final String OPTIONS_COLUMN_METHOD_GETTER   = "options.column.method.getter";
	public static final String OPTIONS_COLUMN_METHOD_SETTER   = "options.column.method.setter";
	public static final String OPTIONS_COLUMN_METHOD_TOSTRING = "options.column.method.tostring";

	public static final String OPTIONS_COLUMN_COMMENT_DEFAULT = "options.column.comment.default";
	public static final String OPTIONS_COLUMN_COMMENT_DEFAULTNAMEIFNULL = "options.column.comment.defaultnameifnull";

	public static final String OPTIONS_COLUMN_BEANINFO_DEFAULTCONSTRUCTOR = "options.column.bean.defaultconstructor";
	public static final String OPTIONS_COLUMN_BEANINFO_SERIALIZABLE = "options.column.bean.serializable";

	public static final String OPTIONS_DATABASE_ENCODE_ENABLED = "options.database.encode.enabled";
	public static final String OPTIONS_DATABASE_ENCODE_FROM = "options.database.encode.from";
	public static final String OPTIONS_DATABASE_ENCODE_TO = "options.database.encode.to";

	public static final String OPTIONS_DATABASE_RESULT_FETCH_BUFFER = "options.database.result.fetch.buffer";

	public static final String OPTIONS_VIEW_GRIDFONT_NAME = "options.view.gridfont.name";
	public static final String OPTIONS_VIEW_GRIDFONT_STYLE = "options.view.gridfont.style";
	public static final String OPTIONS_VIEW_GRIDFONT_SIZE = "options.view.gridfont.size";

	public static final String OPTIONS_VIEW_SQLFONT_NAME = "options.view.sqlfont.name";
	public static final String OPTIONS_VIEW_SQLFONT_STYLE = "options.view.sqlfont.style";
	public static final String OPTIONS_VIEW_SQLFONT_SIZE = "options.view.sqlfont.size";

	public static final String OPTIONS_CLASSPATH_JARS = "options.classpath.jars";

	public static final String OPTIONS_GENERAL_USER_AUTHOR = "options.general.user.author";
	public static final String OPTIONS_GENERAL_USER_VERSION = "options.general.user.version";

	public static final String OPTIONS_DEFAULT_LOOKANDFEEL = "default.lookandfeel";

	/**
	 * database 情報定義
	 */
	public static final int TABLE_TYPE_TABLE = 0;
	public static final int TABLE_TYPE_VIEW = 1;
	public static final int TABLE_TYPE_READONLY = 2;

	public static final String[] TABLE_TYPES = {"TABLE", "VIEW", "READONLY"};


	/**
	 * データBeanタイプ定義
	 * 0:  table
	 * 1:  view
	 * 2:  new bean
	 */
	public static final String BEAN_TYPE_TABLE = "Table";
	public static final String BEAN_TYPE_VIEW = "View";
	public static final String BEAN_TYPE_NEWBEAN = "NewBean";
    public static final String BEAN_TYPE_REPORT = "Report";

	/**
	 * テンプレートタグ名定義
	 */
	public static final String TEMPLATE_TAG_PACKAGE_DEFINE = "$PACKAGE_DEFINE";
	public static final String TEMPLATE_TAG_IMPORT_CLASSES_DEFINE = "$IMPORT_CLASSES_DEFINE";
	public static final String TEMPLATE_TAG_CLASS_DESCRIPTION = "$CLASS_DESCRIPTION";
	public static final String TEMPLATE_TAG_CREATE_DATE = "$CREATE_DATE";
	public static final String TEMPLATE_TAG_AUTHOR = "$AUTHOR";
	public static final String TEMPLATE_TAG_VERSION = "$VERSION";
	public static final String TEMPLATE_TAG_CLASS_NAME = "$CLASS_NAME";
	public static final String TEMPLATE_TAG_SERIALIZABLE = "$SERIALIZABLE";
	public static final String TEMPLATE_TAG_COLUMN_NAME = "$COLUMN_NAME";
	public static final String TEMPLATE_TAG_COLUMN_TYPE = "$COLUMN_TYPE";
	public static final String TEMPLATE_TAG_DEFAULT_VALUE = "$DEFAULT_VALUE";
	public static final String TEMPLATE_TAG_UPPER_COLUMN_NAME = "$UPPER_COLUMN_NAME";
	public static final String TEMPLATE_TAG_COLUMN_COMMENT = "$COLUMN_COMMENT";
	public static final String TEMPLATE_TAG_LOOP_COLUMN_BEGIN = "<LOOP_COLUMN>";
	public static final String TEMPLATE_TAG_LOOP_COLUMN_END = "</LOOP_COLUMN>";
	public static final String TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_BEGIN = "<IF_DEFAULT_CONSTRUCTOR>";
	public static final String TEMPLATE_TAG_IF_DEFAULT_CONSTRUCTOR_END = "</IF_DEFAULT_CONSTRUCTOR>";
	public static final String TEMPLATE_TAG_IF_GETTER_NEED_BEGIN = "<IF_GETTER_NEED>";
	public static final String TEMPLATE_TAG_IF_GETTER_NEED_END = "</IF_GETTER_NEED>";
	public static final String TEMPLATE_TAG_IF_SETTER_NEED_BEGIN = "<IF_SETTER_NEED>";
	public static final String TEMPLATE_TAG_IF_SETTER_NEED_END = "</IF_SETTER_NEED>";
	public static final String TEMPLATE_TAG_IF_TOSTRING_NEED_BEGIN = "<IF_TOSTRING>";
	public static final String TEMPLATE_TAG_IF_TOSTRING_NEED_END = "</IF_TOSTRING>";

	public static final String[] SQL92_KEYWORDS = {
		"ADD", "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AT",
		"AUTHORIZATION", "BEGIN", "BETWEEN", "BY", "CASCADE",
		"CASE", "CHECK", "CHECKPOINT", "CLOSE", "COMMIT", "CONNECT",
		"CONSTRAINT", "CONTINUE", "CREATE", "CURRENT", "CURSOR",
		"DATABASE", "DECLARE", "DEFAULT", "DELETE", "DESC", "DISTINCT",
		"DOUBLE", "DROP", "ELSE", "END", "ESCAPE", "EXCEPT", "EXCLUSIVE",
		"EXEC", "EXECUTE", "EXISTS", "EXIT", "FETCH", "FOR", "FOREIGN",
		"FROM", "FUNCTION", "GOTO", "GRANT", "GROUP", "HAVING", "IF",
		"IN", "INDEX", "INSERT", "INTERSECT", "INTO", "IS", "KEY",
		"LEVEL", "LIKE", "LOCK", "MODIFY", "NEW", "NOT", "NULL",
		"OF", "OFF", "ON", "ONLINE", "ONLY", "OPEN", "OPTION", "OR",
		"ORDER", "OUT", "PARTITION", "PLAN", "PRECISION", "PRIMARY",
		"PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "REFERENCES",
		"REPLACE", "RETURN", "REVOKE", "ROLE", "ROLLBACK", "ROWS",
		"SCHEMA", "SELECT", "SET", "SHARED", "SOME", "STATISTICS",
		"TABLE", "TEMPORARY", "TO", "TRANSACTION", "TRIGGER", "TRUNCATE",
		"UNION", "UNIQUE", "UPDATE", "USE", "VALUES", "VIEW",
		"WHEN", "WHERE", "WHILE", "WITH", "WORK" };
}