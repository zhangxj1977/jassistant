package org.jas.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * このクラスはProperty fileからPropertiesを管理します
 *
 * @author 張　学軍
 * @version 1.0
 */
public class PropertyManager implements Serializable {

	/**
	 * properties、　keyとvalueを保存します
	 */
	private static Properties properties = new Properties();

	/**
	 * ディフォルトConstructor
	 */
    public PropertyManager() {
    }


	/**
	 * 初期化
	 *
	 * @param in ファイルstream
	 */
	public static synchronized void init(InputStream in) throws IOException {

		InputStreamReader streamReader = new InputStreamReader(in);
		LineNumberReader lineReader = new LineNumberReader(streamReader);

		String line;
		while ((line = lineReader.readLine()) != null) {
			line = line.trim();
		    if (!line.equals("") && !line.startsWith("#")) {
				int sepPos = line.indexOf("=");
				if (sepPos > 0) {
				    String key = line.substring(0, sepPos);
					String value = line.substring(sepPos + 1);
				    properties.put(key.trim(), value.trim());
				}
		    }
		}
	}


	/**
	 * 初期化
	 *
	 * @param in propertyファイルas resourceBundle
	 */
	public static synchronized void init(String in) throws MissingResourceException {
        ResourceBundle bundle = ResourceBundle.getBundle(in);
        Enumeration enums = bundle.getKeys();
        Object key = null;
        Object value = null;
        while ( enums.hasMoreElements() ) {
            key = enums.nextElement();
            value = bundle.getString(key.toString());
            properties.put(key, value);
        }
    }


	/**
	 * propertyを取得する
	 */
	public static String getProperty(String key) {
		return (String) properties.get(key);
	}

	/**
	 * propertyを取得する
	 * 前方一致できる
	 */
	public static ArrayList getProperty(String key, boolean forward) {
		return getProperty(key, forward, false);
	}

	/**
	 * propertyを取得する,前方一致できるかつソートできる
	 */
	public static ArrayList getProperty(String key, boolean forward, boolean sort) {
		ArrayList retValue = new ArrayList();

		if (!forward) {
			retValue.add(getProperty(key));
			return retValue;
		}

		ArrayList keyNameList = new ArrayList();
		Set keySet = properties.keySet();
		Iterator keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			String keyName = (String) keyIterator.next();
			if (keyName.startsWith(key)) {
				keyNameList.add(keyName);
			}
		}

		if (sort) {
			for (int i=0; i<keyNameList.size(); i++) {
				retValue.add(null);
			}
		    for (int i=0; i<keyNameList.size(); i++) {
				String keyName = (String) keyNameList.get(i);
				int leftBrace = keyName.lastIndexOf("[");
				int rightBrace = keyName.lastIndexOf("]");
				int index = 0;
				if (leftBrace > 0 && rightBrace > leftBrace) {
					try {
						String strIndex = keyName.substring(leftBrace + 1, rightBrace);
					    index = Integer.parseInt(strIndex);
					} catch (Exception e) {
						index = 0;
					}
				}
				retValue.set(index, getProperty(keyName));
			}
		} else {
			for (int i=0; i<keyNameList.size(); i++) {
				retValue.add(getProperty((String) keyNameList.get(i)));
			}
		}

		return retValue;
	}


	/**
	 * propertyを設定する
	 */
	public static synchronized void setProperty(String key, String value) {
		properties.put(key, value);
	}


	/**
	 * property削除
	 */
	public static synchronized void removeProperty(String key, boolean forward) {
		if (!forward) {
			properties.remove(key);
			return;
		}

		ArrayList keyNameList = new ArrayList();
		Set keySet = properties.keySet();
		Iterator keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			String keyName = (String) keyIterator.next();
			if (keyName.startsWith(key)) {
				keyNameList.add(keyName);
			}
		}

		for (int i=0; i<keyNameList.size(); i++) {
			properties.remove(keyNameList.get(i));
		}
	}


	/**
	 * property to  ファイル
	 *
	 * @param output stream
	 */
	public static synchronized void outputProperty(OutputStream out) throws IOException {
		Set keySet = properties.keySet();
		Iterator keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			String keyName = (String) keyIterator.next();
			String keyValue = (String) getProperty(keyName);

			if (keyValue != null) {
				out.write(keyName.getBytes());
				out.write("=".getBytes());
				out.write(keyValue.getBytes());
				out.write("\n".getBytes());
			}
		}

		out.flush();
		out.close();
	}
}