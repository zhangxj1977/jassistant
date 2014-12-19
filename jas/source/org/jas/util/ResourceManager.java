package org.jas.util;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.jas.common.PJConst;
import org.jas.db.DBParser;

/**
 * config and get resources manager
 *
 * @author í£Å@äwåR
 * @version 1.0
 */

public class ResourceManager {

	/**
	 * cached table name list
	 */
	static String[] tableList = null;

	/**
	 * jar file class loader
	 */
	static URLClassLoader ucl = new URLClassLoader(new URL[]{});

	/**
	 * get current connection table name list
	 */
	public static String[] getTableNameList() {
		return tableList;
	}

	/**
	 * reset new table list
	 */
	public static void resetTableNameList(String[] newTableList) {
		tableList = newTableList;
	}

	/**
	 * get current session all function names
	 */
	public static String[] getFunctions() {
		String[] nameList = null;

		try {
			ArrayList functionList = DBParser.getFunctions(UIUtil.getMDIMain().getConnection());
			nameList = new String[functionList.size()];

			for (int i = 0; i < functionList.size(); i++) {
				nameList[i] = (String) functionList.get(i);
			}
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC202E", se.getMessage());
		}

		return nameList;
	}

	/**
	 * get connection
	 */
	public static Connection getConnection(String connectionURL)
										throws SQLException {
		String userName = "";
		String password = "";
		String connectionName = "";
		int passwordIndex = connectionURL.indexOf("/");
		int tnsIndex = connectionURL.indexOf("@");

		if (passwordIndex > 0) {
			userName = connectionURL.substring(0, passwordIndex);
		}
		if (tnsIndex > 0) {
			password = connectionURL.substring(passwordIndex + 1, tnsIndex);
			connectionName = connectionURL.substring(tnsIndex + 1);
		} else {
			password = connectionURL.substring(passwordIndex + 1);
		}
		// some db does not need password
		if (password != null && password.equals("")) {
			password = null;
		}

		HashMap connectionNameMap = getConnectionNames();
		String[] connectionDriverURL = (String[]) connectionNameMap.get(connectionName);

		return DBUtil.getConnection(connectionDriverURL[0],
									connectionDriverURL[1],
									userName, password);
	}

	/**
	 * get previous connections
	 */
	public static ArrayList getPreviousConnections() {
		return PropertyManager.getProperty(PJConst.DATABASE_CONNECTIONS, true, true);
	}

	/**
	 * get database config name
	 */
	public static HashMap getConnectionNames() {
		ArrayList connectionNameLists = PropertyManager.getProperty(PJConst.DATABASE_NAMES, true, true);
		HashMap connectionNameMap = new HashMap();

		for (int i = 0; i < connectionNameLists.size(); i++) {
			String oneConnection = (String) connectionNameLists.get(i);
			String connectionName = "";
			String connectionDriver = "";
			String connectionURL = "";

			if (oneConnection != null && !oneConnection.equals("")) {
				int indexName = oneConnection.indexOf("/");
				if (indexName > 0) {
					connectionName = oneConnection.substring(0, indexName);

					int indexDriver = oneConnection.indexOf("/", indexName + 1);
					if (indexDriver > 0) {
						connectionDriver = oneConnection.substring(indexName + 1, indexDriver);

						connectionURL = oneConnection.substring(indexDriver + 1);
					}

					connectionNameMap.put(connectionName, new String[]{connectionDriver, connectionURL});
				}
			}
		}

		return connectionNameMap;
	}

	/**
	 * get default look and feed class
	 */
	public static String getDefaultLookAndFeel() {
		String metal    = "javax.swing.plaf.metal.MetalLookAndFeel";
		String motif    = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		String windows  = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		String index = PropertyManager.getProperty(PJConst.OPTIONS_DEFAULT_LOOKANDFEEL);

		if ("0".equals(index)) {
			return windows;
		} else if ("1".equals(index)) {
			return motif;
		} else if ("2".equals(index)) {
			return metal;
		}

		String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();

		if (windows.equals(systemLookAndFeelClassName)) {
			PropertyManager.setProperty(PJConst.OPTIONS_DEFAULT_LOOKANDFEEL, "0");
		} else if (motif.equals(systemLookAndFeelClassName)) {
			PropertyManager.setProperty(PJConst.OPTIONS_DEFAULT_LOOKANDFEEL, "1");
		} else if(metal.equals(systemLookAndFeelClassName)) {
			PropertyManager.setProperty(PJConst.OPTIONS_DEFAULT_LOOKANDFEEL, "2");
		}

		return systemLookAndFeelClassName;
	}

	/**
	 * get look and feed class by index
	 */
	public static String getLookAndFeelByIndex(String index) {
		String metal    = "javax.swing.plaf.metal.MetalLookAndFeel";
		String motif    = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		String windows  = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

		if ("0".equals(index)) {
			return windows;
		} else if ("1".equals(index)) {
			return motif;
		} else if ("2".equals(index)) {
			return metal;
		}

		return null;
	}

	/**
	 * A utility function that layers on top of the LookAndFeel's
	 * isSupportedLookAndFeel() method. Returns true if the LookAndFeel
	 * is supported. Returns false if the LookAndFeel is not supported
	 * and/or if there is any kind of error checking if the LookAndFeel
	 * is supported.
	 *
	 * The L&F menu will use this method to detemine whether the various
	 * L&F options should be active or inactive.
	 *
	 */
	public static boolean isAvailableLookAndFeel(String index) {
		String laf = getLookAndFeelByIndex(index);

		try {
			Class lnfClass = Class.forName(laf);
			LookAndFeel newLAF = (LookAndFeel) (lnfClass.newInstance());

			return newLAF.isSupportedLookAndFeel();
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * UIFonts init
	 */
	public static void initLookAndFeel() {
		UIManager.put("ClassLoader", org.jas.gui.Main.class.getClassLoader());
		UIManager.getLookAndFeelDefaults().put("ClassLoader", org.jas.gui.Main.class.getClassLoader());

		// for jdk1.4.  here 1.3 and 1.4 have the same face
		javax.swing.plaf.FontUIResource defaultFontResource = new javax.swing.plaf.FontUIResource("Dialog", Font.PLAIN, 11);
		javax.swing.plaf.FontUIResource defaultPlusFontResource = new javax.swing.plaf.FontUIResource("Dialog", Font.PLAIN, 12);
		UIManager.put("Button.font", defaultFontResource);
		UIManager.put("Label.font", defaultFontResource);
		UIManager.put("RadioButton.font", defaultFontResource);
		UIManager.put("CheckBox.font", defaultFontResource);
		UIManager.put("ComboBox.font", defaultFontResource);
		UIManager.put("List.font", defaultFontResource);
		UIManager.put("MenuBar.font", defaultPlusFontResource);
		UIManager.put("MenuItem.font", defaultPlusFontResource);
		UIManager.put("RadioButtonMenuItem.font", defaultFontResource);
		UIManager.put("CheckBoxMenuItem.font", defaultFontResource);
		UIManager.put("Menu.font", defaultPlusFontResource);
		UIManager.put("PopupMenu.font", defaultPlusFontResource);
		UIManager.put("Panel.font", defaultFontResource);
		UIManager.put("OptionPane.font", defaultFontResource);
		UIManager.put("Panel.font", defaultFontResource);
		UIManager.put("TabbedPane.font", defaultFontResource);
		UIManager.put("Table.font", defaultFontResource);
		UIManager.put("TableHeader.font", defaultFontResource);
		UIManager.put("TitledBorder.font", defaultPlusFontResource);
		UIManager.put("ToolBar.font", defaultPlusFontResource);
		UIManager.put("TextPane.font", defaultPlusFontResource);

		UIManager.put("OptionPaneUI", "org.jas.base.PJBasicOptionPaneUI");

		ListCellRenderer defaultListRender = (ListCellRenderer) (UIManager.get("List.cellRenderer"));
		if (defaultListRender != null) {
			ListCellRenderer commonListRender = new DefaultListCellRenderer();
			UIManager.put("List.cellRenderer", commonListRender);
		}
	}


	/**
	 * message properties èâä˙âª
	 */
	public static void initMessages() {
		try {
			Locale currentLocale = Locale.getDefault();

			if (currentLocale.equals(Locale.JAPAN)) {
				MessageManager.init(PJConst.DEFAULT_MESSAGEFILE_JA_PATH);
			} else if (currentLocale.equals(Locale.CHINA)) {
				MessageManager.init(PJConst.DEFAULT_MESSAGEFILE_CN_PATH);
			} else {
				MessageManager.init(PJConst.DEFAULT_MESSAGEFILE_EN_PATH);
			}
		} catch (MissingResourceException me) {
			me.printStackTrace();
		}
	}

	/**
	 * config fileèâä˙âª
	 */
	public static void initConfiguration(String configPath, String defaultConfigFile) {
		try {
			File configFile = new File(configPath, "config.properties");
			if (!configFile.exists()) {
				PropertyManager.init(FileManager.getResourcePath(defaultConfigFile).openStream());
				updateConfiguration(configPath);
			} else {
				PropertyManager.init(new FileInputStream(configFile));
			}

			File filterSortFile = new File(configPath, "filtersort.ser");
			FilterSortManager.init(filterSortFile);
		} catch (MissingResourceException me) {
			me.printStackTrace();
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}


	/**
	 * rewrite the properties to file
	 */
	public static void updateConfiguration(String configPath) {
		try {
			File configFile = new File(configPath, "config.properties");
			if (!configFile.exists()) {
				(new File(configPath)).mkdirs();
			}
			configFile.createNewFile();
			PropertyManager.outputProperty(new FileOutputStream(configFile));

			File filterSortFile = new File(configPath, "filtersort.ser");
			FilterSortManager.updateFile(filterSortFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * template file èâä˙âª
	 */
	public static void initTemplate(String configPath) {
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
				StringBuffer sb = FileManager.readInputStream(in);
				if (!templateFile.exists()) {
					(new File(configPath)).mkdirs();
				}
				FileManager.writeFile(sb.toString(), templateFile);
			}
		} catch (Exception me) {
			me.printStackTrace();
		}
	}


	/**
	 * init dynamic class loader
	 *
	 */
	public static void initClassLoader() {
		ArrayList preJarLists = PropertyManager.getProperty(PJConst.OPTIONS_CLASSPATH_JARS, true, true);

		try {
			if (preJarLists != null && !preJarLists.isEmpty()) {
				URL[] urls = new URL[preJarLists.size()];

				for (int i = 0; i < preJarLists.size(); i++) {
					URL oneURL = new URL("jar:file:" + (String) preJarLists.get(i) + "!/");
					urls[i] = oneURL;
				}

				ucl = URLClassLoader.newInstance(urls, org.jas.gui.Main.class.getClassLoader());
			} else {
				ucl = URLClassLoader.newInstance(new URL[]{}, org.jas.gui.Main.class.getClassLoader());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * load dynamic jar files
	 *
	 */
	public static Class loadDynamicJar(String className) throws Exception {
		return ucl.loadClass(className);
	}
}