package org.jas.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

/**
 *
 *
 *
 *
 * @author ���@�w�R
 * @version 1.0
 */

public class MessageManager implements Serializable {

	/**
	 * properties�A�@key��value��ۑ����܂�
	 */
	private static Properties properties = new Properties();

	/**
	 * override PropertyManager
	 */
	public MessageManager() { }


	/**
	 * ������
	 *
	 * @param in �t�@�C��stream
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
	 * ������
	 *
	 * @param in property�t�@�C��as resourceBundle
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
	 * property���擾����
	 */
	public static String getProperty(String key) {
		Object value = properties.get(key);
		if (value == null) {
			return key;
		}
		return (String) value;
	}


	/**
	 * property��ݒ肷��
	 */
	public static synchronized void setProperty(String key, String value) {
		properties.put(key, value);
	}


	/**
	 * ���b�Z�[�W�ɂ̓p�����[�^��ϊ�����
	 *
	 * @param key String : MSCT0001E
	 * @param params String[] %1 %2��replace, limitted to 1..9
	 *
	 * @return message���e
	 */
	public static String getProperty(String key, String[] params) {
		String value = getProperty(key);
		StringBuffer sb = new StringBuffer();

		if (params == null) return value;

		int paramCounts = params.length;

		int index = 0;
		int pos = 0;
		while ((pos = value.indexOf("%", index)) != -1) {
			sb.append(value.substring(index, pos));
			char paramIndex = value.charAt(pos + 1);
			if (Character.isDigit(paramIndex) && String.valueOf(paramIndex).getBytes().length == 1) {
				sb.append(params[Integer.parseInt(String.valueOf(paramIndex)) - 1]);
				index = pos + 2;
			} else {
				index = pos + 1;
			}
		}
		sb.append(value.substring(index));

		return sb.toString();
	}

	/**
	 * ���b�Z�[�W���e���擾����
	 *
	 * @param messageID
	 * @param params String[]
	 * @return message���e
	 */
	public static String getMessage(String messageID, String[] params) {
		return getProperty(messageID, params);
	}

	/**
	 * ���b�Z�[�W���e���擾����
	 *
	 * @param messageID
	 * @param params String
	 * @return message���e
	 */
	public static String getMessage(String messageID, String params) {
		return getProperty(messageID, new String[]{params});
	}

	/**
	 * ���b�Z�[�W���e���擾����
	 *
	 * @param messageID
	 * @return message���e
	 */
	public static String getMessage(String messageID) {
		return getProperty(messageID, null);
	}

	/**
	 * ���b�Z�[�W�\��
	 *
	 * @param message���e
	 * @param messageType
	 */
	public static int showMessage(String message, int messageType) {
		int retValue = -1;
		int optionType = JOptionPane.DEFAULT_OPTION;

		if (messageType == JOptionPane.QUESTION_MESSAGE) {
			optionType = JOptionPane.YES_NO_OPTION;
		}

		retValue = JOptionPane.showOptionDialog(UIUtil.getMDIMain(),
												message,
												"",
												optionType,
												messageType,
												null,
												null,
												null);

		return retValue;
	}

	/**
	 * ���b�Z�[�W�\��, �G���[�^�C�v�̓��b�Z�[�WID��
	 *
	 * @param message ID
	 * @param params String array
	 */
	public static int showMessage(String messageID, String[] params) {
		int messageType = JOptionPane.ERROR_MESSAGE;
		String message = "";

		if (messageID != null && messageID.startsWith("MCSTC")) {
			if (messageID.length() > 8) {
				message = getMessage(messageID, params);
				String strErrorType = messageID.substring(8, 9);
				if ( strErrorType.equals("I") ) {
					messageType = JOptionPane.INFORMATION_MESSAGE;
				} else if ( strErrorType.equals("W") ) {
					messageType = JOptionPane.WARNING_MESSAGE;
				} else if ( strErrorType.equals("E") ) {
					messageType = JOptionPane.ERROR_MESSAGE;
				} else if ( strErrorType.equals("F") ) {
					messageType = JOptionPane.ERROR_MESSAGE;
				} else if ( strErrorType.equals("Q") ) {
					messageType = JOptionPane.QUESTION_MESSAGE;
				}
			}
		}

		return showMessage(message, messageType);
	}


	/**
	 * ���b�Z�[�W�\��, �G���[�^�C�v�̓��b�Z�[�WID��
	 *
	 * @param message ID
	 * @param param String
	 */
	public static int showMessage(String messageID, String param) {
		return showMessage(messageID, new String[]{param});
	}

	/**
	 * ���b�Z�[�W�\��, �G���[�^�C�v�̓��b�Z�[�WID��
	 *
	 * @param message ID
	 */
	public static int showMessage(String messageID) {
		return showMessage(messageID, (String[]) null);
	}

	/**
	 * �G���[���b�Z�[�W�\��
	 *
	 * @param message���e
	 */
	public static void showErrorMessage(String message) {
		showMessage(message, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Warn���b�Z�[�W�\��
	 *
	 * @param message���e
	 */
	public static void showWarningMessage(String message) {
		showMessage(message, JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Infomation���b�Z�[�W�\��
	 *
	 * @param message���e
	 */
	public static void showInfoMessage(String message) {
		showMessage(message, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Question���b�Z�[�W�\��
	 *
	 * @param message���e
	 */
	public static int showQuestionMessage(String message) {
		return showMessage(message, JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * input dialog
	 */
	public static String showInputDialog(String message, String title) {
		return JOptionPane.showInputDialog(UIUtil.getMDIMain(), message, title, JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * show composite input dialog
	 *
	 */
	public static Object showInputDialog(Object message,
	                                     String title) {
		return JOptionPane.showInputDialog(UIUtil.getMDIMain(),
				                         message,
				                         title,
				                         JOptionPane.QUESTION_MESSAGE);
	}
}