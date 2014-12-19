package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import org.jas.base.PJDialogBase;
import org.jas.db.DBParser;
import org.jas.model.ColumnDescriptionData;
import org.jas.util.MessageManager;
import org.jas.util.ResourceManager;

/**
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class DialogSelectTableColumns extends PJDialogBase {
	PanelSQLScript scriptPanel = null;
	JPanel panelMain = new JPanel();
	BorderLayout borderLayoutMain = new BorderLayout();
	JToolBar toolBarTop = new JToolBar();
	JPanel panelCenter = new JPanel();
	BorderLayout borderLayoutList = new BorderLayout();
	JButton btnCancel = new JButton();
	JButton btnAppend = new JButton();
	JButton btnClipboard = new JButton();
	JTabbedPane tabbedPaneMain = new JTabbedPane();
	JPanel panelSelectTables = new JPanel();
	JPanel panelSelectColumns = new JPanel();
	BorderLayout borderLayoutTable = new BorderLayout();
	BorderLayout borderLayoutColumn = new BorderLayout();
	JScrollPane scpSelectTable = new JScrollPane();
	JList listTableNames = new JList();
	JScrollPane scpSelectColumns = new JScrollPane();
	JList listColumns = new JList();
	JComboBox cmbTables = new JComboBox();
	JPanel panelFunctions = new JPanel();
	BorderLayout borderLayoutFunctions = new BorderLayout();
	JScrollPane scpFunctions = new JScrollPane();
	JList listFunctions = new JList();
	MouseSelectListener mouseSelectListener = new MouseSelectListener();

	public DialogSelectTableColumns(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			initUI();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogSelectTableColumns() {
		this(null, "Select Table, Column, Function Names", true);
	}

	public DialogSelectTableColumns(PanelSQLScript scriptPanel) {
		this(Main.getMDIMain(), "Select Table, Column, Function Names", false);
		this.scriptPanel = scriptPanel;
	}

	void jbInit() throws Exception {
		panelMain.setLayout(borderLayoutMain);
		panelCenter.setLayout(borderLayoutList);
		btnCancel.setMargin(new Insets(0, 0, 0, 0));
		btnCancel.setText("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancel_actionPerformed(e);
			}
		});
		btnAppend.setMargin(new Insets(0, 0, 0, 0));
		btnAppend.setText("Append");
		btnAppend.setMnemonic('A');
		btnAppend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAppend_actionPerformed(e);
			}
		});
		btnClipboard.setMargin(new Insets(0, 0, 0, 0));
		btnClipboard.setText("Clipboard");
		btnClipboard.setMnemonic('B');
		btnClipboard.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClipboard_actionPerformed(e);
			}
		});
		panelSelectTables.setLayout(borderLayoutTable);
		panelSelectColumns.setLayout(borderLayoutColumn);
		borderLayoutColumn.setVgap(3);
		borderLayoutMain.setVgap(3);
		panelFunctions.setLayout(borderLayoutFunctions);
		toolBarTop.setFloatable(false);
		getContentPane().add(panelMain);
		panelMain.add(toolBarTop, BorderLayout.NORTH);
		panelMain.add(panelCenter, BorderLayout.CENTER);
		panelCenter.add(tabbedPaneMain, BorderLayout.CENTER);
		toolBarTop.add(btnClipboard, null);
		toolBarTop.add(btnAppend, null);
		toolBarTop.add(btnCancel, null);
		panelSelectTables.add(scpSelectTable, BorderLayout.CENTER);
		scpSelectTable.getViewport().add(listTableNames, null);
		listTableNames.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listTableNames.addMouseListener(mouseSelectListener);
		panelSelectColumns.add(scpSelectColumns, BorderLayout.CENTER);
		scpSelectColumns.getViewport().add(listColumns, null);
		listColumns.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listColumns.addMouseListener(mouseSelectListener);
		panelSelectColumns.add(cmbTables, BorderLayout.NORTH);
		panelFunctions.add(scpFunctions, BorderLayout.CENTER);
		scpFunctions.getViewport().add(listFunctions, null);
		listFunctions.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listFunctions.addMouseListener(mouseSelectListener);
		tabbedPaneMain.add(panelSelectTables, "table");
		tabbedPaneMain.add(panelSelectColumns,  "column");
		tabbedPaneMain.add(panelFunctions,  "function");
	}

	public void setVisible(boolean b) {
		if (b) {
			initLocation(Main.getMDIMain());
		}
		super.setVisible(b);
	}

	/**
	 * init ui data
	 */
	void initUI() {
		// table name list
		String[] tableNames = ResourceManager.getTableNameList();
		listTableNames.setListData(tableNames);

		// column table names
		cmbTables.addItem("");
		for (int i = 0; i < tableNames.length; i++) {
			cmbTables.addItem(tableNames[i]);
		}
		cmbTables.addItemListener(new ChangeTableListener());

		// function list
		String[] functions = ResourceManager.getFunctions();
		listFunctions.setListData(functions);
	}


	/**
	 * get selected text and to be appended
	 */
	String getSelectedText() {
		int selectedIndex = tabbedPaneMain.getSelectedIndex();
		Object[] values = null;

		if (selectedIndex == 0) {
			values = listTableNames.getSelectedValues();
		} else if (selectedIndex == 1) {
			values = listColumns.getSelectedValues();
		} else if (selectedIndex == 2) {
			values = listFunctions.getSelectedValues();
		}

		StringBuffer sb = new StringBuffer("\n");
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				sb.append(values[i]);
				if (i < values.length - 1) {
					sb.append(",\n");
				}
			}
		}

		return sb.toString();
	}

	/**
	 * get column name list from a table
	 */
	Vector getColumnList(String tableName) {
		Vector columnVector = null;
		ArrayList columnListData = null;

		try {
			columnListData = DBParser.getColumnDescription(Main.getMDIMain().getConnection(), tableName);
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC202E", se.getMessage());
			return null;
		}

		if (columnListData != null && !columnListData.isEmpty()) {
			columnVector = new Vector(columnListData.size());

			for (int i = 0; i < columnListData.size(); i++) {
				ColumnDescriptionData columnData = (ColumnDescriptionData) columnListData.get(i);
				columnVector.add(columnData.getColumnName());
			}
		}

		return columnVector;
	}

	/**
	 * change connection listener
	 */
	class ChangeTableListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() != event.SELECTED) {
				return;
			}

			String tableName = (String) cmbTables.getSelectedItem();

			if (tableName != null && !tableName.equals("")) {
				listColumns.setListData(getColumnList(tableName));
			} else {
				listColumns.setListData((Vector) null);
			}
		}
	}

	/**
	 * mouse double click listener
	 */
	class MouseSelectListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() > 1) {
				appendSelectText();
			}
		}
	}

	/**
	 * append selected text
	 */
	void appendSelectText() {
		if (scriptPanel.isShowing()) {
			scriptPanel.txtPanelSQLScript.appendString(getSelectedText());
		}
	}

	/**
	 * put selected items to clipboard
	 */
	void btnClipboard_actionPerformed(ActionEvent e) {
		JTextArea tempArea = new JTextArea(getSelectedText());
		tempArea.selectAll();
		tempArea.copy();
	}

	/**
	 * append selected items to the textpane
	 */
	void btnAppend_actionPerformed(ActionEvent e) {
		appendSelectText();
	}

	/**
	 * cancel
	 */
	void btnCancel_actionPerformed(ActionEvent e) {
		dispose();
	}
}
