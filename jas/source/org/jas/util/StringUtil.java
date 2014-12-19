package org.jas.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jas.common.PJConst;

/**
 * Stringツール
 *
 * @author　張　学軍
 * @version 1.0
 */
public class StringUtil {

	/**
	 * get encode converted string
	 */
	public static String convertString(String sOrg, String fromEncode, String toEncode) {
		if (sOrg == null || sOrg.equals("")) {
			return null;
		}

		try {
			byte[] buf;
			if (fromEncode != null && !fromEncode.equals("")) {
				buf = sOrg.getBytes(fromEncode);
			} else {
				buf = sOrg.getBytes();
			}

			if (toEncode != null && !toEncode.equals("")) {
				return new String(buf, toEncode);
			} else {
				return new String(buf);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * change null string to empty ""
	 */
	public static String nvl(String str) {
		if (str == null) {
			return "";
		}

		return str;
	}

	/**
	 * return the string left is 0
	 *
	 */
	public static String getPadZeroString(String str, int len) {
		if (str == null || str.equals("")) return "";
		if (str.length() >= len) return str;
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < len - str.length(); i++) {
			sb.append("0");
		}
		sb.append(str);

		return sb.toString();
	}

	/**
	 * test the given encode is validy
	 *
	 */
	public static boolean isEncodeValidy(String encode) {
		try {
			"test".getBytes(encode);
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	/**
	 * if the database data encode enabled
	 *
	 */
	public static boolean isEncodeEnabled() {
		String strEnabled = PropertyManager.getProperty(PJConst.OPTIONS_DATABASE_ENCODE_ENABLED);

		return "1".equals(strEnabled);
	}

	/**
	 * get database from encode
	 */
	public static String getFromEncode() {
		String fromEncode = PropertyManager.getProperty(PJConst.OPTIONS_DATABASE_ENCODE_FROM);

		return fromEncode;
	}

	/**
	 * get database to encode
	 */
	public static String getToEncode() {
		String toEncode = PropertyManager.getProperty(PJConst.OPTIONS_DATABASE_ENCODE_TO);

		return toEncode;
	}


	/**
	 * get type of java class according to sql type
	 *
	 */
	public static Class getJavaTypeBySQLType(int sqlType,
										   int index,
										   ResultSetMetaData rsmd)
								throws SQLException {
		switch (sqlType) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
					return String.class;
			case Types.NUMERIC:
			case Types.DECIMAL:
					int displaySize = rsmd.getColumnDisplaySize(index + 1);
					int precision = rsmd.getPrecision(index + 1);
					if (precision > 0) {
						displaySize = precision;
					}
					if (rsmd.getScale(index + 1) > 0 || displaySize > 18) {
						if (displaySize > 18) {
							return BigDecimal.class;
						} else {
							return Double.class;
						}
					} else {
						return Long.class;
					}
			case Types.BIT:
					return Boolean.class;
			case Types.TINYINT:
					return Byte.class;
			case Types.SMALLINT:
					return Short.class;
			case Types.INTEGER:
					return Integer.class;
			case Types.BIGINT:
					return Long.class;
			case Types.REAL:
					return Float.class;
			case Types.FLOAT:
			case Types.DOUBLE:
					return Double.class;
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
					return Object.class;
			case Types.DATE:
					return Date.class;
			case Types.TIME:
					return Time.class;
			case Types.TIMESTAMP:
					return Timestamp.class;
			default:
					return Object.class;
		}
	}

	/**
	 * get sql type by java class
	 *
	 */
	public static int getSQLTypeByJavaType(Class javaType)
								throws SQLException {
		if (javaType == String.class) {
			return Types.VARCHAR;
		} else if (javaType == Long.class || javaType == Long.TYPE) {
			return Types.NUMERIC;
		} else if (javaType == Boolean.class || javaType == Boolean.TYPE) {
			return Types.BIT;
		} else if (javaType == Byte.class || javaType == Byte.TYPE) {
			return Types.TINYINT;
		} else if (javaType == Short.class || javaType == Short.TYPE) {
			return Types.SMALLINT;
		} else if (javaType == Integer.class || javaType == Integer.TYPE) {
			return Types.INTEGER;
		} else if (javaType == Long.class || javaType == Long.TYPE) {
			return Types.BIGINT;
		} else if (javaType == Float.class || javaType == Float.TYPE) {
			return Types.REAL;
		} else if (javaType == Double.class || javaType == Double.TYPE) {
			return Types.DOUBLE;
		} else if (javaType == BigDecimal.class) {
			return Types.DECIMAL;
		} else if (javaType == Date.class) {
			return Types.DATE;
		} else if (javaType == Time.class) {
			return Types.TIME;
		} else if (javaType == Timestamp.class) {
			return Types.TIMESTAMP;
		} else {
			return Types.VARCHAR;
		}
	}

	/**
	 * get specify the type value of the input text
	 */
	public static Object getConvertValueOfType(Class javaType, Object value) throws Exception {
		if (value == null || value.equals("")) {
			return null;
		}

		try {
			if (javaType == String.class
					|| javaType == Object.class
					|| javaType == Boolean.class) {
				return value;
			} else if (javaType == Byte.class) {
				return new Byte(value.toString());
			} else if (javaType == Short.class) {
				return new Short(value.toString());
			} else if (javaType == Integer.class) {
				return new Integer(value.toString());
			} else if (javaType == Long.class) {
				return new Long(value.toString());
			} else if (javaType == Float.class) {
				return new Float(value.toString());
			} else if (javaType == Double.class) {
				return new Double(value.toString());
			} else if (javaType == BigDecimal.class) {
				return new BigDecimal(value.toString());
			} else if (javaType == Date.class) {
				return DateUtil.toSqlDate(value.toString());
			} else if (javaType == Time.class) {
				return DateUtil.toSqlTime(value.toString());
			} else if (javaType == Timestamp.class) {
				return DateUtil.toSqlTimestamp(value.toString(), DateUtil.DATE_FORMAT_DATETIME);
			} else {
				// must set value by other way
				return null;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * get specify the type value of the input text by increased number
	 */
	public static Object getIncreaseConvertValueOfType(Class javaType, Object value,
													int i, int totalLen)
											throws Exception {
		try {
			if (javaType == String.class) {
				return value + getPadZeroString(String.valueOf(i), totalLen);
			} else if (javaType == Byte.class) {
				return new Byte((byte) (new Byte(value.toString()).byteValue() + (byte) i));
			} else if (javaType == Short.class) {
				return new Short((byte) (new Short(value.toString()).shortValue() + (short) i));
			} else if (javaType == Integer.class) {
				return new Integer(new Integer(value.toString()).intValue() + i);
			} else if (javaType == Long.class) {
				return new Long(new Long(value.toString()).longValue() + i);
			} else if (javaType == Float.class) {
				return new Float(new Float(value.toString()).floatValue() + i);
			} else if (javaType == Double.class) {
				return new Double(new Double(value.toString()).doubleValue() + i);
			} else if (javaType == BigDecimal.class) {
				return new BigDecimal(value.toString()).add(new BigDecimal(i));
			} else if (javaType == Date.class) {
				return new java.sql.Date(DateUtil.toSqlDate(value.toString()).getTime() + i * 1000 * 60 * 60 * 24);
			} else if (javaType == Time.class) {
				return new java.sql.Time(DateUtil.toSqlTime(value.toString()).getTime() + i * 1000);
			} else if (javaType == Timestamp.class) {
				return new java.sql.Timestamp(DateUtil.toSqlTimestamp(value.toString(), DateUtil.DATE_FORMAT_DATETIME).getTime() + i * 1000);
			} else {
				// must set value by other way
				return null;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * get string value by the java type and object value
	 * with not quotation
	 * 
	 * @param javaType
	 * @param value
	 * @return
	 */
	public static String getStringValue(Class javaType, Object value) {
		return getStringValue(javaType, value, false);
	}
	
	/**
	 * get string value by the java type and object value
	 *
	 */
	public static String getStringValue(Class javaType, Object value, boolean isNeedQuoation) {
		if (value == null) {
			return null;
		}

		if (javaType == String.class
				|| javaType == Object.class
				|| javaType == Boolean.class) {
			if (isNeedQuoation) {
				return "\"" + value.toString() + "\"";
			} else {
				return value.toString();
			}
		} else if (javaType == Byte.class
				|| javaType == Short.class
				|| javaType == Integer.class
				|| javaType == Long.class) {
			return value.toString();
		} else if (javaType == Float.class
				|| javaType == Double.class) {
			DecimalFormat fmt = new DecimalFormat("###.##################");
			return fmt.format(((Number) value).doubleValue());
		} else if (javaType == BigDecimal.class) {
			if (UIUtil.isJDK150Later()) {
				return ((BigDecimal) value).toPlainString();
			}
			return value.toString();
		} else if (javaType == Date.class) {
			if (isNeedQuoation) {
				return "\"" + DateUtil.toSqlTimestampString(new Timestamp(((Date) value).getTime()), DateUtil.DATE_FORMAT_DATEONLY) + "\"";
			} else {
				return DateUtil.toSqlTimestampString(new Timestamp(((Date) value).getTime()), DateUtil.DATE_FORMAT_DATEONLY);
			}
		} else if (javaType == Time.class) {
			if (isNeedQuoation) {
				return "\"" + DateUtil.toSqlTimestampString(new Timestamp(((Time) value).getTime()), DateUtil.DATE_FORMAT_TIMEONLY) + "\"";
			} else {
				return DateUtil.toSqlTimestampString(new Timestamp(((Time) value).getTime()), DateUtil.DATE_FORMAT_TIMEONLY);
			}
		} else if (javaType == Timestamp.class) {
			if (isNeedQuoation) {
				return "\"" + DateUtil.toSqlTimestampString((Timestamp) value, DateUtil.DATE_FORMAT_DATETIME) + "\"";
			} else {
				return DateUtil.toSqlTimestampString((Timestamp) value, DateUtil.DATE_FORMAT_DATETIME);
			}
		} else {
			return new String("[Object]");
		}
	}

	/**
	 * Concat the file path name like dir1/dir2/filename
	 *
	 * @param prefix dir 1
	 * @param suffix dir 2
	 *
	 * @return full path
	 */
	public static String concatPath(String prefix, String suffix) {
		char sepratorUnix = '/';
		char sepratorWin = '\\';
		char seprator = sepratorUnix;

		if (prefix == null) {
			prefix = ".";
		}
		if (suffix == null) {
			suffix = "";
		}

		if (prefix.indexOf(sepratorWin) >= 0) {
			seprator = sepratorWin;
			suffix = suffix.replace(sepratorUnix, seprator);
		} else if (prefix.indexOf(sepratorUnix) >= 0) {
			suffix = suffix.replace(sepratorWin, seprator);
		}

		String strSep = String.valueOf(seprator);
		if ((prefix.endsWith(strSep) && !suffix.startsWith(strSep))
				|| (!prefix.endsWith(strSep) && suffix.startsWith(strSep))) {
			return prefix + suffix;
		} else if (prefix.endsWith(strSep) && suffix.startsWith(strSep)) {
			return prefix + suffix.substring(strSep.length());
		} else {
			return prefix + seprator + suffix;
		}
	}

	/**
	 * convert comma separated string to a list
	 */
	public static ArrayList getListFromString(String str, String sep) {
		if (str == null || str.equals("")) {
			return null;
		}

		ArrayList list = new ArrayList();

		StringTokenizer st;
		if (sep == null || sep.equals("")) {
			st = new StringTokenizer(str);
		} else {
			st = new StringTokenizer(str, sep, true);
		}

		boolean lastIsSep = str.startsWith(sep);
		while (st.hasMoreTokens()) {
			String ele = st.nextToken();
			if (!ele.equals(sep)) {
				list.add(ele);
				lastIsSep = false;
			} else {
				if (lastIsSep) {
					list.add(null);
				}
				lastIsSep = true;
			}
		}
		if (lastIsSep) {
			list.add(null);
		}

		return list;
	}

	/**
	 * convert default separated string to a list
	 */
	public static ArrayList getListFromString(String str) {
		return getListFromString(str, null);
	}

	/**
	 * join the row data list with the delimited char
	 *
	 */
	public static String joinList(Vector rowVector, char sep) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < rowVector.size(); i++) {
			sb.append(nvl((String) rowVector.get(i)));
			if (i < rowVector.size() - 1) {
				sb.append(sep);
			}
		}

		return sb.toString();
	}

	/**
	 * join the row data list with the delimited char
	 *
	 */
	public static String joinList(Vector rowVector, Vector typeVector, char sep, boolean isNeedQuotation) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < typeVector.size(); i++) {
			sb.append(nvl(getStringValue((Class) typeVector.get(i), rowVector.get(i), isNeedQuotation)));
			if (i < typeVector.size() - 1) {
				sb.append(sep);
			}
		}

		return sb.toString();
	}

	/**
	 * join one row data as html table format string
	 *
	 */
	public static String joinHtmlString(Vector columnNameVector, Vector dataVector, Vector typeVector,
										Vector sizeVector, boolean isHeader) {
		StringBuffer sb = new StringBuffer();

		String tagCell = "td";
		if (isHeader) {
			tagCell = "th";
		}

		sb.append("<tr>");
		for (int i = 0; i < typeVector.size(); i++) {
			sb.append("<" + tagCell + " width=" + getTDWidthSize((Class) typeVector.get(i), ((Integer) sizeVector.get(i)).intValue()) + ">");
			if (isHeader) {
				sb.append(columnNameVector.get(i));
			} else {
				sb.append(parseHtmlString(getStringValue((Class) typeVector.get(i), dataVector.get(i))));
			}
			sb.append("</" + tagCell + ">");
		}
		sb.append("</tr>");

		return sb.toString();
	}

	/**
	 * join one row data as insert sql script
	 *
	 */
	public static String joinSqlString(Vector nameVector, Vector dataVector, Vector typeVector,
										Vector sizeVector, String tableName, String driverName) {
		StringBuffer sb = new StringBuffer();

		sb.append("insert into " + tableName + "(");
		sb.append(joinList(nameVector, ','));
		sb.append(") values(");

		for (int i = 0; i < typeVector.size(); i++) {
			Class type = (Class) typeVector.get(i);
			String value = getStringValue(type, dataVector.get(i));

			if (value == null) {
				sb.append("null");
			} else {
				if (type.getSuperclass() == java.lang.Number.class) {
					sb.append(value);
				} else if (type.getSuperclass() == java.util.Date.class
						&& driverName != null &&
						driverName.indexOf("Oracle") >= 0) {
					if (type == java.sql.Time.class) {
						sb.append("to_date('" + value + "', 'HH24:MI:SS')");
					} else if (type == java.sql.Date.class) {
						sb.append("to_date('" + value + "', 'yyyy/mm/dd)");
					} else if (type == java.sql.Timestamp.class) {
						sb.append("to_date('" + value + "', 'yyyy/mm/dd HH24:MI:SS')");
					}
				} else {
					sb.append("'" + value + "'");
				}
			}

			if (i < typeVector.size() - 1) {
				sb.append(",");
			}
		}

		sb.append(");");

		return sb.toString();
	}

	/**
	 * get html table td width
	 *
	 */
	public static int getTDWidthSize(Class type, int displaySize) {
		if (type == java.sql.Timestamp.class
				|| type == java.sql.Date.class) {
			displaySize = 15;
		}
		if (displaySize > 30) {
			displaySize = 30;
		}
		return displaySize * 8 + 7;
	}

	/**
	 * convert html special char
	 *
	 */
	public static String parseHtmlString(String str) {
		if (str == null || str.equals("")) {
			return "&nbsp;";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c == '<') {
				sb.append("&lt;");
				continue;
			} else if (c == '>') {
				sb.append("&gt;");
				continue;
			}

			sb.append(c);
		}

		return sb.toString();
	}

	/**
	 * replace needEscapeStr to escapeStr + needEscapeStr in str
	 *
	 */
	public static String replaceString(String str, char needEscapeStr, String escapeStr) {
		if (str == null || str.equals("") || escapeStr == null) {
			return str;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c == needEscapeStr) {
				sb.append(escapeStr + c);
				continue;
			}

			sb.append(c);
		}

		return sb.toString();
	}

	/**
	 * get html form field name from html file
	 *
	 * @param content
	 */
	public static ArrayList getHtmlFormFieldName(String content) {
		ArrayList retArrayList = new ArrayList();

		retArrayList.addAll(getFormFieldName(content, "input", "name"));
		retArrayList.addAll(getFormFieldName(content, "select", "name"));

		return retArrayList;
	}

	/**
	 * get struts form field name from jsp file
	 *
	 * @param content
	 */
	public static ArrayList getStrutsFormFieldName(String content) {
		ArrayList retArrayList = new ArrayList();

		retArrayList.addAll(getHtmlFormFieldName(content));
		retArrayList.addAll(getFormFieldName(content, "html:text", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:hidden", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:multibox", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:checkbox", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:password", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:radio", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:select", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:textarea", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:options", "property"));
		retArrayList.addAll(getFormFieldName(content, "html:options", "labelproperty"));
		retArrayList.addAll(getFormFieldName(content, "bean:write", "property"));

		return retArrayList;
	}

	/**
	 * get form field name from html file or jsp file
	 *
	 * @param content
	 */
	public static ArrayList getFormFieldName(String content, String tag, String name) {
		ArrayList nameSet = new ArrayList();
		int beginPos = 0;
		int findPos = -1;
		String findTag = "<" + tag;

		String tempContent = content.toLowerCase();
		while ((findPos = tempContent.indexOf(findTag, beginPos)) != -1) {
			int fieldEndPos = tempContent.indexOf(">", findPos + findTag.length());

			if (fieldEndPos < 0) {
				break;
			}

			String oneField = content.substring(findPos, fieldEndPos + 1);
			String oneFieldName = getFieldName(oneField, name);

			if (oneFieldName != null) {
				if (!nameSet.contains(oneFieldName)) {
					nameSet.add(oneFieldName);
				}
			}

			beginPos = fieldEndPos + 1;
		}

		return nameSet;
	}

	/**
	 * get one fieldName from field
	 *
	 */
	private static String getFieldName(String oneField, String name) {
		String tempOneField = oneField.toLowerCase();

		// find the name tag
		int nameTagPos = tempOneField.indexOf(" " + name);
		if (nameTagPos < 0) {
			return null;
		}

		int equalCharPos = oneField.indexOf("=", nameTagPos + name.length() + 1);
		if (equalCharPos < 0) {
			return null;
		}

		int beginName = -1;
		int endName = -1;
		for (int i = equalCharPos + 1; i < oneField.length(); i++) {
			char c = oneField.charAt(i);

			if (c != ' ') {
				if (c == '>') {
					if (oneField.charAt(i - 1) == '/') {
						endName = i - 1;
					} else {
						endName = i;
					}
					break;
				}
				if (beginName == -1) {
					beginName = i;
					continue;
				}
			} else if (beginName != -1) {
				endName = i;
				break;
			}
		}

		if (beginName < endName) {
			String tempName = oneField.substring(beginName, endName);
			tempName = tempName.trim();

			if (tempName.startsWith("\'") || tempName.startsWith("\"")) {
				tempName = tempName.substring(1);
			}

			if (tempName.endsWith("\'") || tempName.endsWith("\"")) {
				tempName = tempName.substring(0, tempName.length() - 1);
			}

			return tempName.trim();
		}

		return null;
	}


	/**
	 * test
	 *
	 */
	public static void main(String args[]) throws Exception {
		StringBuffer sb = FileManager.readInputStream(args[0]);

		ArrayList resultFields = new ArrayList();

		if (args[0].endsWith(".htm") || args[0].endsWith(".html")) {
			resultFields = getHtmlFormFieldName(sb.toString());
		} else if (args[0].endsWith(".jsp")) {
			resultFields = getStrutsFormFieldName(sb.toString());
		} else if (args[0].endsWith(".class")) {
			org.jas.base.FileClassLoader myClassLoader = new org.jas.base.FileClassLoader();
			Class obj = myClassLoader.loadClass(args[0]);
			java.lang.reflect.Field[] fields = obj.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				resultFields.add(fields[i]);
			}
		}

		for (int i = 0; i < resultFields.size(); i++) {
			System.out.println(resultFields.get(i));
		}
	}
}