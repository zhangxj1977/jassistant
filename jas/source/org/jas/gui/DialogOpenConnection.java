package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jas.base.PJDialogBase;
import org.jas.base.PJTableListModel;
import org.jas.common.PJConst;
import org.jas.common.ParamTransferEvent;
import org.jas.common.ParamTransferListener;
import org.jas.util.DBUtil;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;
import org.jas.util.PropertyManager;
import org.jas.util.ResourceManager;
import org.jas.util.StringUtil;

/**
 *
 *
 *
 *
 * @author 張　学軍
 * @version 1.0
 */

public class DialogOpenConnection extends PJDialogBase implements ParamTransferListener {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    JPanel panelMain = new JPanel();
	JLabel lblPreConnection = new JLabel();
	JTextField txtConnectionName = new JTextField();
	JButton btnRemoveConnection = new JButton();
	JScrollPane scpConnections = new JScrollPane();
	EncryptListCellRenderer listCellRender = new EncryptListCellRenderer();
	JList listConnections = new JList();
	JLabel lblConnectionName = new JLabel();
	JLabel lblUserName = new JLabel();
	JTextField txtUser = new JTextField();
	JLabel lblPassword = new JLabel();
	JPasswordField txtPassword = new JPasswordField();
	JButton btnConfigConnection = new JButton();
	JButton btnOK = new JButton();
	JButton btnCancel = new JButton();
	JLabel lblDescription = new JLabel();
	Border border1;
	JLabel lblIcon = new JLabel();
	ImageIcon iconSystem = ImageManager.createImageIcon("system.gif");

	public DialogOpenConnection(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			initPreviousConnections();
			initConnectionNames();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogOpenConnection() {
		this(Main.getMDIMain(), "接続", true);
	}

	public void setVisible(boolean b) {
		if (b) {
			initLocation(Main.getMDIMain());
		}
		super.setVisible(b);
	}

	void jbInit() throws Exception {
		border1 = BorderFactory.createEtchedBorder(Color.white,new Color(178, 178, 178));
		panelMain.setLayout(null);
		this.setSize(new Dimension(411, 311));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setResizable(false);
		this.setTitle("接続");
		lblPreConnection.setPreferredSize(new Dimension(120, 15));
		lblPreConnection.setHorizontalAlignment(SwingConstants.LEFT);
		lblPreConnection.setText("接続履歴");
		lblPreConnection.setBounds(new Rectangle(15, 20, 122, 20));
		btnRemoveConnection.setMargin(new Insets(0, 0, 0, 0));
		btnRemoveConnection.setText("削除");
		btnRemoveConnection.setMnemonic('R');
		btnRemoveConnection.setBounds(new Rectangle(145, 16, 55, 20));
		btnRemoveConnection.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemoveConnection_actionPerformed(e);
			}
		});
		scpConnections.setBounds(new Rectangle(15, 42, 186, 229));
		lblConnectionName.setText("接続名:");
		lblConnectionName.setBounds(new Rectangle(220, 22, 114, 18));
		txtConnectionName.setToolTipText("");
		txtConnectionName.setBounds(new Rectangle(220, 43, 170, 20));
		lblUserName.setBounds(new Rectangle(220, 72, 67, 14));
		lblUserName.setText("ユーザ:");
		txtUser.setBounds(new Rectangle(220, 89, 170, 20));
		txtUser.setToolTipText("");
		lblPassword.setText("パスワード:");
		lblPassword.setBounds(new Rectangle(220, 114, 69, 18));
		txtPassword.setToolTipText("");
		txtPassword.setBounds(new Rectangle(220, 134, 170, 20));
		btnOK.setMargin(new Insets(1, 1, 1, 1));
		btnOK.setText("Ok");
		btnOK.setMnemonic('O');
		btnOK.setBounds(new Rectangle(328, 177, 62, 22));
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});
		btnCancel.setBounds(new Rectangle(328, 204, 62, 22));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancel_actionPerformed(e);
			}
		});
		btnCancel.setMargin(new Insets(1, 1, 1, 1));
		btnCancel.setText("Cancel");
		btnCancel.setMnemonic('C');
		lblDescription.setFont(new java.awt.Font("Dialog", 2, 12));
		lblDescription.setForeground(Color.gray);
		lblDescription.setText("Free to use this tool!");
		lblDescription.setBounds(new Rectangle(278, 255, 116, 21));
		listConnections.setBorder(border1);
		listConnections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listConnections.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
				    processConnect();
				}
		    }
        });
		listConnections.setCellRenderer(listCellRender);
		lblIcon.setOpaque(true);
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setHorizontalTextPosition(SwingConstants.CENTER);
		lblIcon.setIcon(iconSystem);
		lblIcon.setBounds(new Rectangle(221, 156, 105, 98));
		btnConfigConnection.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnConfigConnection_actionPerformed(e);
			}
		});
		btnConfigConnection.setBounds(new Rectangle(334, 16, 55, 20));
		btnConfigConnection.setText("接続設定");
		btnConfigConnection.setMnemonic('N');
		btnConfigConnection.setToolTipText("Add or Edit JDBC Driver and Connection URL");
		btnConfigConnection.setMargin(new Insets(0, 0, 0, 0));
		panelMain.add(txtConnectionName, null);
		panelMain.add(btnRemoveConnection, null);
		panelMain.add(lblPreConnection, null);
		panelMain.add(scpConnections, null);
		panelMain.add(lblConnectionName, null);
		panelMain.add(lblUserName, null);
		panelMain.add(txtUser, null);
		panelMain.add(lblPassword, null);
		panelMain.add(txtPassword, null);
		panelMain.add(btnConfigConnection, null);
		panelMain.add(btnOK, null);
		panelMain.add(btnCancel, null);
		panelMain.add(lblIcon, null);
		panelMain.add(lblDescription, null);
		scpConnections.getViewport().add(listConnections, null);
		this.getContentPane().add(panelMain, BorderLayout.CENTER);
		listConnections.setModel(preConnectionListModel);
		listConnections.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				listConnections_SelectChanged(e);
			}
		});
		setDefaultButton(btnOK);
	}

	/*************************************************************************************
	/* 業務用エリア
	/************************************************************************************/
	ArrayList preConnections = new ArrayList();
	HashMap connectionNameMap = new HashMap();
	PJTableListModel preConnectionListModel = new PJTableListModel();

	/**
	 * init previous connections
	 */
	private void initPreviousConnections() {
		preConnections = ResourceManager.getPreviousConnections();
		preConnectionListModel.setDataSet(preConnections);
		if (preConnections.size() > 0) {
			listConnections.setSelectedIndex(0);
		}
	}

	/**
	 * init database config name
	 */
	private void initConnectionNames() {
		connectionNameMap = ResourceManager.getConnectionNames();
	}

	/**
	 * refresh connection names
	 */
	private void refreshConnectionNames() {

		if (!connectionNameMap.isEmpty()) {
			connectionNameMap.clear();
		}

		initConnectionNames();
	}

	/**
	 * process connect to db
	 */
	private void processConnect() {
		String connectionName = txtConnectionName.getText();
		String userName = txtUser.getText().trim();
		String password = String.valueOf(txtPassword.getPassword());
		Connection conn = null;

		if (connectionName == null || connectionName.equals("")) {
			MessageManager.showMessage("MCSTC001E", "Connection Name");
			txtConnectionName.requestFocus();
			return;
		}
		// some db does not need user
		if (userName == null || userName.equals("")) {
		    userName = null;
		}
		// some db does not need password
		if (password != null && password.equals("")) {
			password = null;
		}

		String[] connectionDriverURL = (String[]) connectionNameMap.get(connectionName);
		if (connectionDriverURL == null || connectionDriverURL.length < 2) {
			MessageManager.showMessage("MCSTC005E", connectionName);
			txtPassword.requestFocus();
			return;
		}

		try {
			conn = DBUtil.getConnection(connectionDriverURL[0], connectionDriverURL[1], userName, password);
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC201E", se.getMessage());
			return;
		}

		String connURL = StringUtil.nvl(userName) + "/" + StringUtil.nvl(password) + "@" + connectionName;
		processAddConnections(connURL);
		fireParamTransferEvent(new Object[]{connURL, conn, userName + "@" + connectionName}, PJConst.WINDOW_OPENCONNECTION);
		dispose();
	}

	/**
	 * add new connections to config file
	 */
	private void processAddConnections(String url) {
		for (int i = 0; i < preConnections.size(); i++) {
			String preURL = (String) preConnections.get(i);
			if (url.equalsIgnoreCase(preURL)) {
				preConnections.remove(i);
				preConnections.add(0, url);
				processUpdateConnections();
				return;
			}
		}
		preConnections.add(0, url);

		// update properties
		processUpdateConnections();
	}

	private void processUpdateConnections() {
		PropertyManager.removeProperty(PJConst.DATABASE_CONNECTIONS, true);
		for (int i = 0; i < preConnections.size(); i++) {
			String oneURL = (String) preConnections.get(i);
			PropertyManager.setProperty(PJConst.DATABASE_CONNECTIONS +"[" + i + "]", oneURL);
		}
	}

	void refreshConnectionName(String connectionName) {
		listConnections.clearSelection();
		txtConnectionName.setText(connectionName);
	}

	/*************************************************************************************
	/* 事件処理用エリア
	/************************************************************************************/
	void btnCancel_actionPerformed(ActionEvent e) {
		this.dispose();
	}

	void btnOK_actionPerformed(ActionEvent e) {
		processConnect();
	}

	void btnRemoveConnection_actionPerformed(ActionEvent e) {
		int selectedIndex = listConnections.getSelectedIndex();
		if (selectedIndex >= 0) {
			preConnectionListModel.remove(preConnections.get(selectedIndex));
		}
		listConnections.clearSelection();
		// update properties
		processUpdateConnections();
	}

	void btnConfigConnection_actionPerformed(ActionEvent e) {
		String currentConnectionName = txtConnectionName.getText();
		DialogConfigConnection dialogConfigConnection = new DialogConfigConnection(currentConnectionName);
		dialogConfigConnection.addParamTransferListener(this);
		dialogConfigConnection.setVisible(true);
		dialogConfigConnection.remove(this);
	}

	void listConnections_SelectChanged(ListSelectionEvent e) {
		String userName = "";
		String password = "";
		String connectionName = "";

		int selectedIndex = listConnections.getSelectedIndex();
		if (selectedIndex >= 0) {
			String connectionURL = (String) preConnections.get(selectedIndex);
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
			btnRemoveConnection.setEnabled(true);
		} else {
			btnRemoveConnection.setEnabled(false);
		}

		txtUser.setText(userName);
		txtPassword.setText(password);
		txtConnectionName.setText(connectionName);
	}


	/**
	 * 遷移画面からのイベント
	 *
	 * @param pe ParamTransferEvent
	 */
	public void paramTransfered(ParamTransferEvent pe) {
		int opFlag = pe.getOpFlag();

		switch (opFlag) {
			case PJConst.WINDOW_CONFIGCONNECTION:
				refreshConnectionName((String) pe.getParam());
				refreshConnectionNames();
				break;
			default :
				break;
		}
	}

	/**
	 * パスワード非表示
	 *
	 */
	class EncryptListCellRenderer extends DefaultListCellRenderer {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /* (非 Javadoc)
         * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);

            if (value == null) {
                return label;
            }
            
            String connectionURL = (String) value.toString();
            int passwordIndex = connectionURL.indexOf("/");
            int tnsIndex = connectionURL.indexOf("@");
            String connectionName = "";
            String userName = "";
            if (passwordIndex > 0) {
                userName = connectionURL.substring(0, passwordIndex);
            }
            if (tnsIndex > 0) {
                connectionName = connectionURL.substring(tnsIndex + 1);
            }
            label.setText(userName + "@" + connectionName);

            return label;
        }
	    
	}
}