package org.jas.util;

import java.awt.Font;

import org.jas.common.PJConst;


/**
 * UIツール
 *
 * @author　張　学軍
 * @version 1.0
 */

public class UIUtil {

	final public static int GRID_FONT = 0;
	final public static int SQL_FONT = 1;

	/**
	 * get default font from configuration
	 */
	public static Font getDefaultFont(int flag) {
		String fontName = null;
		String strFontStyle  = null;
		String strFontSize  = null;

		if (flag == GRID_FONT) {
			fontName = PropertyManager.getProperty(PJConst.OPTIONS_VIEW_GRIDFONT_NAME);
			strFontStyle = PropertyManager.getProperty(PJConst.OPTIONS_VIEW_GRIDFONT_STYLE);
			strFontSize = PropertyManager.getProperty(PJConst.OPTIONS_VIEW_GRIDFONT_SIZE);
		} else if (flag == SQL_FONT) {
			fontName = PropertyManager.getProperty(PJConst.OPTIONS_VIEW_SQLFONT_NAME);
			strFontStyle = PropertyManager.getProperty(PJConst.OPTIONS_VIEW_SQLFONT_STYLE);
			strFontSize = PropertyManager.getProperty(PJConst.OPTIONS_VIEW_SQLFONT_SIZE);
		}

		if (fontName == null || fontName.equals("")) {
			fontName = "dialog";
		}

		int fontStyle = Font.PLAIN;
		if (strFontStyle != null) {
			if (strFontStyle.equals("Plain")) {
				fontStyle = Font.PLAIN;
			} else if (strFontStyle.equals("Bold")) {
				fontStyle = Font.BOLD;
			} else if (strFontStyle.equals("Italic")) {
				fontStyle = Font.ITALIC;
			} else if (strFontStyle.equals("Bold Italic")) {
				fontStyle = Font.BOLD + Font.ITALIC;
			}
		}

		int fontSize = 12;
		try {
			fontSize = Integer.parseInt(strFontSize);
		} catch (NumberFormatException nfe) {
			fontSize = 12;
		}

		return new Font(fontName, fontStyle, fontSize);
	}

	/**
	 * get default fetch size
	 *
	 */
	public static int getDefaultFetchSize() {
		String strFetchSize = PropertyManager.getProperty(PJConst.OPTIONS_DATABASE_RESULT_FETCH_BUFFER);

		if (strFetchSize == null || strFetchSize.equals("")) {
			return PJConst.RESULT_FETCH_BUFFER;
		}

		int fetchSize = PJConst.RESULT_FETCH_BUFFER;
		try {
			fetchSize = Integer.parseInt(strFetchSize);
		} catch (NumberFormatException nfe) {
			fetchSize = PJConst.RESULT_FETCH_BUFFER;
		}

		return fetchSize;
	}

	/**
	 * get MDI main frame
	 */
	public static org.jas.gui.FrmMain getMDIMain() {
		return org.jas.gui.Main.getMDIMain();
	}


	/**
	 * show whether jdk version is 1.4
	 *
	 */
	public static boolean isJDK140() {
		String ver = System.getProperty("java.version");
		if (ver.compareTo("1.4") >= 0 && ver.compareTo("1.4.1") < 0) {
			return true;
		}

		return false;
	}

	/**
	 * show whether jdk version is 1.4 or more
	 *
	 */
	public static boolean isJDK140Later() {
		String ver = System.getProperty("java.version");
		if (ver.compareTo("1.4") >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * show whether jdk version is 1.5 or more
	 *
	 */
	public static boolean isJDK150Later() {
		String ver = System.getProperty("java.version");
		if (ver.compareTo("1.5") >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * show whether current look and feel is windows.
	 * if true, need to fix the bugs of jdk1.40
	 */
	public static boolean isWindowLF() {
		String index = PropertyManager.getProperty(PJConst.OPTIONS_DEFAULT_LOOKANDFEEL);

		if ("0".equals(index)) {
			return true;
		}

		return false;
	}
}