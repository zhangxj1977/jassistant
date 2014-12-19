package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jas.base.PJInternalFrame;
import org.jas.base.PJSQLTextPane;
import org.jas.common.PJConst;
import org.jas.common.ParamTransferEvent;
import org.jas.common.ParamTransferListener;
import org.jas.util.DBUtil;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;
import org.jas.util.PropertyManager;
import org.jas.util.ResourceManager;
import org.jas.util.StringUtil;
import org.jas.util.UIUtil;

/**
 * main frame
 *
 * @author 張　学軍
 * @version 1.0
 */
public class FrmMain extends JFrame implements ParamTransferListener {
	ButtonMenuActionListener buttonMenuActionListener = new ButtonMenuActionListener();
	ChangeLookAndFeelAction changeLookAndFeelAction = new ChangeLookAndFeelAction();

	ImageIcon iconSystemIcon = ImageManager.createImageIcon("systemicon.gif");
	ImageIcon iconSchemaBrowse = ImageManager.createImageIcon("databrowse.gif");
	ImageIcon iconSQLEdit = ImageManager.createImageIcon("sqlscript.gif");
	FrmMainMenuBar menuBarMain = new FrmMainMenuBar(this);
	FrmMainToolBar mainToolBar = new FrmMainToolBar(this);
	JPanel mainStatusBar = new JPanel();
	JLabel lblStatus = new JLabel();

	JDesktopPane desktop = new JDesktopPane();
	PanelSQLBrowser sqlBrowserPanel = null;
	PanelSQLScript sqlScriptPanel = null;
	JInternalFrame jiSQLBrowser = null;
	JInternalFrame jiSQLScript = null;

	/**
	 * default constructor
	 *
	 */
	public FrmMain() {
		try {
			jbInit();
			initOther();
			pack();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setDefaultCloseOperation(3);
		this.setJMenuBar(menuBarMain);
		this.setTitle("JDBDevelop Assistant");
		this.setIconImage(iconSystemIcon.getImage());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				this_windowOpened(e);
			}
			public void windowClosed(WindowEvent e) {
				this_windowClosed(e);
			}
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});
		this.setSize(800, 600);
		desktop.setBackground(SystemColor.control.darker());
		this.getContentPane().add(desktop, BorderLayout.CENTER);
		desktop.setPreferredSize(new Dimension(800, 520));
		this.getContentPane().add(mainToolBar, BorderLayout.NORTH);
		mainStatusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.gray,Color.white,Color.white,Color.gray));
		mainStatusBar.setPreferredSize(new Dimension(600, 20));
		mainStatusBar.setLayout(new BorderLayout());
		this.getContentPane().add(mainStatusBar,  BorderLayout.SOUTH);
		mainStatusBar.add(lblStatus, BorderLayout.CENTER);
	}

	private void initLocation() {
		Dimension frameSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation(((screenSize.width - frameSize.width) / 2), ((screenSize.height - frameSize.height) / 2));
	}

	public void setVisible(boolean b) {
		if (b) {
			initLocation();
		}

		super.setVisible(b);
	}


	/**
	 * other init
	 * according connection, set button, menu status
	 */
	private void initOther() {
		setConnectionStatus();
	}

	/*************************************************************************************
	/* 業務用エリア
	/************************************************************************************/
	Connection conn = null;
	String currentConnURL = null;
	String extTitile = "";

	/*************************************************************************************
	/* 事件処理用エリア
	/************************************************************************************/
	class ButtonMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();

			if (obj == menuBarMain.mnuFileItemNewJavaBean || obj == mainToolBar.toolBarNewJavaBean) {
				createNewBean();
			} else if (obj ==  menuBarMain.mnuFileItemOpen || obj == mainToolBar.toolBarOpenConnection) {
				showOpenConnectionDialog();
			} else if (obj == menuBarMain.mnuFileItemClose || obj == mainToolBar.toolBarCloseConnection) {
				if (MessageManager.showMessage("MCSTC013Q") != 0) {
					return;
				}
				closeConnection();
			} else if (obj == menuBarMain.mnuFileItemExit) {
				ResourceManager.updateConfiguration(Main.configPath);
				dispose();
				System.exit(0);
			} else if (obj == menuBarMain.mnuEditItemCopy) {
				sqlBrowserPanel.panelBeanCreateCopy();
			} else if (obj == menuBarMain.mnuEditItemPaste) {
				sqlBrowserPanel.panelBeanCreatePaste();
			} else if (obj == menuBarMain.mnuEditItemCut) {
				sqlBrowserPanel.panelBeanCreateCut();
			} else if (obj == menuBarMain.mnuEditItemDelete) {
				sqlBrowserPanel.panelBeanCreateDelete();
			} else if (obj == menuBarMain.mnuViewItemOption || obj == mainToolBar.toolBarOptions) {
				showOptionsDialog();
			} else if (obj == menuBarMain.mnuHelpItemAbout) {
				showAboutDialog();
			} else if (obj == menuBarMain.mnuDatabaseItemSchemaBrowser || obj == mainToolBar.toolBarSchemaBrowser) {
				showDataBrowse();
			} else if (obj == menuBarMain.mnuDatabaseItemSQLEdit || obj == mainToolBar.toolBarSQLEdit) {
				showSQLScript();
			} else if (obj == menuBarMain.mnuItemAutoCommitOnOff) {
				setAutoCommitOnOff(menuBarMain.mnuItemAutoCommitOnOff.isSelected());
			} else if (obj == menuBarMain.mnuDatabaseItemCommit) {
				commit();
			} else if (obj == menuBarMain.mnuDatabaseItemRollback) {
				rollback();
			} else if (obj == menuBarMain.mnuWindowItemCascade) {
				arrangeInternalFrames("0");
			} else if (obj == menuBarMain.mnuWindowItemTileHorizontal) {
				arrangeInternalFrames("1");
			} else if (obj == menuBarMain.mnuWindowItemTileVertical) {
				arrangeInternalFrames("2");
			} else if (obj == menuBarMain.mnuWindowItemCloseAllFrames) {
				hideAllIFrame();
			}
		}
	}

	/**
	 * window closed action
	 */
	void this_windowClosed(WindowEvent e) {
		closeConnection();
		ResourceManager.updateConfiguration(Main.configPath);
	}

	/**
	 * window closing event action
	 */
	void this_windowClosing(WindowEvent e) {
		closeConnection();
		ResourceManager.updateConfiguration(Main.configPath);
	}

	/**
	 * window opend action
	 */
	void this_windowOpened(WindowEvent e) {
	    /*
		if (!UIUtil.isJDK140Later()) {
			try {
				Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
				Robot robot = new Robot();
				robot.mouseMove((int) scrSize.getWidth() / 2 + 378, (int) scrSize.getHeight() / 2 - 296);
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		*/

		showOpenConnectionDialog();
	}

	/**
	 * create new bean
	 */
	void createNewBean() {
		String beanName = MessageManager.showInputDialog("Please input bean name.", "Need bean Name");

		if (beanName != null && !beanName.trim().equals("")) {
			showDataBrowse();
			sqlBrowserPanel.createNewBean(beanName);
		}
	}

	/**
	 * show sql script editor internal frame
	 *
	 */
	void showSQLScript() {
		if (jiSQLScript == null || jiSQLScript.isClosed()) {
			if (sqlScriptPanel == null) {
				sqlScriptPanel = new PanelSQLScript();
			}
			jiSQLScript = new PJInternalFrame("SQL Edit Window",
										sqlScriptPanel,
										iconSQLEdit,
										menuBarMain.mnuWindow);
			jiSQLScript.setBounds(100, 80, 640, 420);
			desktop.add(jiSQLScript, BorderLayout.CENTER);

			if (!jiSQLScript.isMaximum()) {
				try {
					jiSQLScript.setMaximum(true);
				} catch (java.beans.PropertyVetoException pve) {}
			}
		}
		try {
			jiSQLScript.setSelected(true);
			jiSQLScript.show();
		} catch (java.beans.PropertyVetoException e) {}

		if (UIUtil.isJDK140() && UIUtil.isWindowLF()) {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
			jiSQLScript.requestFocus();
		}

		sqlScriptPanel.txtPanelSQLScript.requestFocus();
	}

	/**
	 * show schema browser internal frame
	 *
	 */
	void showDataBrowse() {
		if (jiSQLBrowser == null || jiSQLBrowser.isClosed()) {
			if (sqlBrowserPanel == null) {
				sqlBrowserPanel = new PanelSQLBrowser();
			}
			jiSQLBrowser = new PJInternalFrame("Schema Browser",
											sqlBrowserPanel,
											iconSchemaBrowse,
											menuBarMain.mnuWindow);
			jiSQLBrowser.setBounds(10, 10, 640, 420);
			desktop.add(jiSQLBrowser, BorderLayout.CENTER);

			if (!jiSQLBrowser.isMaximum()) {
				try {
					jiSQLBrowser.setMaximum(true);
				} catch (java.beans.PropertyVetoException pve) {}
			}
		}
		try {
			jiSQLBrowser.setSelected(true);
			jiSQLBrowser.show();
		} catch (java.beans.PropertyVetoException e) {}

		if (UIUtil.isJDK140() && UIUtil.isWindowLF()) {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
			jiSQLBrowser.requestFocus();
		}
	}

	/**
	 * when close the connection, it will hide all frames
	 *
	 */
	void hideAllIFrame() {
		try {
			if (jiSQLBrowser != null) {
				jiSQLBrowser.dispose();
				jiSQLBrowser.setClosed(true);
			}
			if (jiSQLScript != null) {
				jiSQLScript.dispose();
				jiSQLScript.setClosed(true);
			}

			desktop.removeAll();
		} catch (java.beans.PropertyVetoException pve) {}
	}

	/**
	 * rearrage Internal Frames in this desktop
	 *
	 */
	void arrangeInternalFrames(String index) {
		JInternalFrame[] frames = desktop.getAllFrames();

		Dimension allSize = desktop.getSize();
		int width = (int) allSize.getWidth();
		int height = (int) allSize.getHeight();

		for (int i = 0; i < frames.length; i++) {
			JInternalFrame oneFrame = frames[i];

			if ("0".equals(index)) {
				oneFrame.setBounds(10 + i * 50, 10 + 50 * i, 640, 420);
			} else if ("1".equals(index)) {
				oneFrame.setBounds( i * ((int) width / frames.length),
									0,
									(int) (width / frames.length),
									height);
			} else if ("2".equals(index)) {
				oneFrame.setBounds( 0,
									i * ((int) height / frames.length),
									width,
									(int) height / frames.length);
			}
		}
	}

	/**
	 * reset sql script panel.
	 * clear or init table names and functions used to hightlight
	 */
	void resetSQLScriptPanel() {
		if (sqlScriptPanel != null) {
			sqlScriptPanel.resetPanel();
		}

		if (getConnection() != null) {
			PJSQLTextPane.tableNames = ResourceManager.getTableNameList();
			PJSQLTextPane.functionNames = ResourceManager.getFunctions();
		} else {
			PJSQLTextPane.tableNames = null;
			PJSQLTextPane.functionNames = null;
		}
	}

	/**
	 * show open connection dialog
	 */
	void showOpenConnectionDialog() {
		DialogOpenConnection dialogOpenConn = new DialogOpenConnection();
		dialogOpenConn.addParamTransferListener(this);
		dialogOpenConn.setVisible(true);
		dialogOpenConn.removeParamTransferListener(this);
	}

	/**
	 * show about dialog
	 */
	void showAboutDialog() {
		DialogAbout dialogAbout = new DialogAbout(this);
		dialogAbout.setVisible(true);
	}

	/**
	 * show help content dialog
	 */
	void showHelpContentDialog() {
		DialogHelp dialogHelp = new DialogHelp();
		dialogHelp.setVisible(true);
	}

	/**
	 * show options dialog
	 */
	void showOptionsDialog() {
		DialogOptions dialogOptions = new DialogOptions();
		dialogOptions.addParamTransferListener(this);
		dialogOptions.setVisible(true);
		dialogOptions.removeParamTransferListener(this);
	}

	/**
	 * when connection opened or closed.
	 * to changed ui component status
	 *
	 */
	void setConnectionStatus() {
		if (conn != null) {
			menuBarMain.mnuFileItemOpen.setEnabled(false);
			menuBarMain.mnuFileItemClose.setEnabled(true);
			menuBarMain.mnuDatabaseItemSchemaBrowser.setEnabled(true);
			menuBarMain.mnuDatabaseItemSQLEdit.setEnabled(true);
			mainToolBar.toolBarOpenConnection.setEnabled(false);
			mainToolBar.toolBarCloseConnection.setEnabled(true);
			mainToolBar.toolBarSchemaBrowser.setEnabled(true);
			mainToolBar.toolBarSQLEdit.setEnabled(true);
			menuBarMain.mnuItemAutoCommitOnOff.setSelected(true);
			setAutoCommitOnOff(true);
			if (DBUtil.isTransactionSupported(conn)) {
				menuBarMain.mnuItemAutoCommitOnOff.setEnabled(true);
			} else {
				menuBarMain.mnuItemAutoCommitOnOff.setEnabled(false);
			}
		} else {
			menuBarMain.mnuFileItemOpen.setEnabled(true);
			menuBarMain.mnuFileItemClose.setEnabled(false);
			menuBarMain.mnuDatabaseItemSchemaBrowser.setEnabled(false);
			menuBarMain.mnuDatabaseItemSQLEdit.setEnabled(false);
			mainToolBar.toolBarOpenConnection.setEnabled(true);
			mainToolBar.toolBarCloseConnection.setEnabled(false);
			mainToolBar.toolBarSchemaBrowser.setEnabled(false);
			mainToolBar.toolBarSQLEdit.setEnabled(false);
			menuBarMain.mnuItemAutoCommitOnOff.setEnabled(false);
			menuBarMain.mnuDatabaseItemCommit.setEnabled(false);
			menuBarMain.mnuDatabaseItemRollback.setEnabled(false);
		}
	}

	/**
	 * menu and toolbar button, copy, cut, paste, delete status
	 * by the column selection status
	 */
	public void setColumnOperationStatus(boolean enabled) {
		if (enabled) {
			menuBarMain.mnuEditItemCopy.setEnabled(true);
			menuBarMain.mnuEditItemPaste.setEnabled(true);
			menuBarMain.mnuEditItemCut.setEnabled(true);
			menuBarMain.mnuEditItemDelete.setEnabled(true);
		} else {
			menuBarMain.mnuEditItemCopy.setEnabled(false);
			menuBarMain.mnuEditItemPaste.setEnabled(false);
			menuBarMain.mnuEditItemCut.setEnabled(false);
			menuBarMain.mnuEditItemDelete.setEnabled(false);
		}
	}

	/**
	 * Stores the current L&F, and calls updateLookAndFeel, below
	 */
	public void setLookAndFeel(String index) {
		index = StringUtil.nvl(index);
		PropertyManager.setProperty(PJConst.OPTIONS_DEFAULT_LOOKANDFEEL, index);

		try {
			UIManager.setLookAndFeel(ResourceManager.getDefaultLookAndFeel());
			SwingUtilities.updateComponentTreeUI(this);
			if (sqlScriptPanel != null) {
				SwingUtilities.updateComponentTreeUI(sqlScriptPanel);
				sqlScriptPanel.repaint();
			}
			if (sqlBrowserPanel != null) {
				sqlBrowserPanel.updateAllPanelUI();
			}
			repaint();
		} catch (Exception ex) {
			MessageManager.showMessage("MCSTC002E", ex.getMessage());
		}
	}


	/**
	 * change look and feel action
	 */
	class ChangeLookAndFeelAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			String index = e.getActionCommand();
			if (index == null || index.equals("")) {
				index = "";
			}

			setLookAndFeel(index);
		}
	}

	/**
	 * 遷移画面からのイベント
	 *
	 * @param pe ParamTransferEvent
	 */
	public void paramTransfered(ParamTransferEvent pe) {
		int opFlag = pe.getOpFlag();

		switch (opFlag) {
			case PJConst.WINDOW_OPENCONNECTION:
				Object[] obj = (Object[]) pe.getParam();
				this.currentConnURL = (String) obj[0];
				Connection curConn = (Connection) obj[1];
				this.extTitile = (String) obj[2];
				setConnection(curConn);
				
				if (this.extTitile != null) {
				    setTitle("JDBDevelop Assistant (" + extTitile + ")");
				}
				break;
			default :
				break;
		}

	}

	/**
	 * DB Connectionを設定する
	 */
	void setConnection(Connection conn) {
		this.conn = conn;

		setConnectionStatus();

		showDataBrowse();
		if (sqlBrowserPanel != null) {
			sqlBrowserPanel.refreshTableNameList(conn);
		}

		resetSQLScriptPanel();
	}

	/**
	 * close connection
	 */
	void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException se) {
				MessageManager.showMessage("MCSTC202E", se.getMessage());
			}
		}
		conn = null;
		currentConnURL = null;

		setConnectionStatus();

		if (sqlBrowserPanel != null) {
			sqlBrowserPanel.refreshTableNameList(conn);
			sqlBrowserPanel.cleanAllRightPanel();
		}
		setColumnOperationStatus(false);
		resetSQLScriptPanel();
		hideAllIFrame();
	}

	/**
	 * set auto commit on or off
	 */
	void setAutoCommitOnOff(boolean onOff) {
		if (conn != null) {
			try {
				conn.setAutoCommit(onOff);

				if (onOff) {
					menuBarMain.mnuDatabaseItemCommit.setEnabled(false);
					menuBarMain.mnuDatabaseItemRollback.setEnabled(false);
				} else {
					menuBarMain.mnuDatabaseItemCommit.setEnabled(true);
					menuBarMain.mnuDatabaseItemRollback.setEnabled(true);
				}
			} catch (SQLException se) {
				MessageManager.showMessage("MCSTC202E", se.getMessage());
			}
		}
	}

	/**
	 * commit current session
	 */
	void commit() {
		if (conn != null) {
			try {
				conn.commit();
			} catch (SQLException se) {
				MessageManager.showMessage("MCSTC202E", se.getMessage());
			}
		}
	}

	/**
	 * rollback current session
	 */
	void rollback() {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException se) {
				MessageManager.showMessage("MCSTC202E", se.getMessage());
			}
		}
	}

	/**
	 * get current connection
	 *
	 */
	public Connection getConnection() {
		return this.conn;
	}

	/**
	 * set window status text
	 *
	 */
	public void setStatusText(String text) {
		lblStatus.setText(text);
	}
}