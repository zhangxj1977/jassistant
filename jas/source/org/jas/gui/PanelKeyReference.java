package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jas.base.PJTableCellRender;
import org.jas.base.PJTableHeaderUI;
import org.jas.common.Refreshable;
import org.jas.db.DBParser;
import org.jas.util.MessageManager;

/**
 *
 *
 *
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class PanelKeyReference extends JPanel implements Refreshable {
	BorderLayout mainBorderLayout = new BorderLayout();
	JSplitPane mainSplitPanel = new JSplitPane();
	JPanel panelImportKeys = new JPanel();
	JPanel panelExportKeys = new JPanel();
	BorderLayout borderLayoutImportKey = new BorderLayout();
	BorderLayout borderLayoutExportKey = new BorderLayout();
	TitledBorder titledBorderImportKeys;
	TitledBorder titledBorderExportKeys;
	JScrollPane scpImportKeys = new JScrollPane();
	JTable tblImportKeys = new JTable();
	JScrollPane scpExportKeys = new JScrollPane();
	JTable tblExportKeys = new JTable();
	PJTableCellRender dbTableCellRender = new PJTableCellRender();
	String tableName = null;
	String tableType = null;
	boolean refreshable = true;

	public PanelKeyReference() {
		try {
			jbInit();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		titledBorderImportKeys = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Imported Keys");
		titledBorderExportKeys = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Exported Keys");
		this.setPreferredSize(new Dimension(600, 520));
		this.setLayout(mainBorderLayout);
		mainSplitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainSplitPanel.setBorder(null);
		mainSplitPanel.setPreferredSize(new Dimension(600, 495));
		mainSplitPanel.setBottomComponent(panelExportKeys);
		mainSplitPanel.setLastDividerLocation(240);
		mainSplitPanel.setTopComponent(panelImportKeys);
		panelImportKeys.setLayout(borderLayoutImportKey);
		panelExportKeys.setLayout(borderLayoutExportKey);
		panelImportKeys.setBorder(titledBorderImportKeys);
		panelExportKeys.setBorder(titledBorderExportKeys);
		this.add(mainSplitPanel, BorderLayout.CENTER);
		panelImportKeys.add(scpImportKeys, BorderLayout.CENTER);
		scpImportKeys.getViewport().add(tblImportKeys, null);
		panelExportKeys.add(scpExportKeys, BorderLayout.CENTER);
		scpExportKeys.getViewport().add(tblExportKeys, null);
		mainSplitPanel.setDividerLocation(240);
		tblImportKeys.setRowHeight(18);
		tblImportKeys.setDefaultRenderer(Object.class, dbTableCellRender);
		tblImportKeys.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblImportKeys.setColumnSelectionAllowed(false);
		tblImportKeys.getTableHeader().setReorderingAllowed(false);
		tblImportKeys.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblImportKeys.getTableHeader().setUI(new PJTableHeaderUI());
		tblExportKeys.setRowHeight(18);
		tblExportKeys.setDefaultRenderer(Object.class, dbTableCellRender);
		tblExportKeys.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblExportKeys.setColumnSelectionAllowed(false);
		tblExportKeys.getTableHeader().setReorderingAllowed(false);
		tblExportKeys.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblExportKeys.getTableHeader().setUI(new PJTableHeaderUI());
	}

	// table header
	String[] importTableHeader = {"FKCOLUMN_NAME", "PKTABLE_NAME", "PKCOLUMN_NAME", "FK_NAME", "PK_NAME"};
	String[] exportTableHeader = {"PKCOLUMN_NAME", "FKTABLE_NAME", "FKCOLUMN_NAME", "FK_NAME", "PK_NAME"};
	int[] columnWidth = new int[]{150, 120, 150, 100, 100};


	/**
	 * set param that to search data
	 */
	public void setParam(String beanType, String tableName) {
		this.tableType = beanType;
		this.tableName = tableName;
	}

	/**
	 * get data from db and reset the table
	 */
	public void resetData() {
		try {
			Vector[] vecKeys = DBParser.getImportExportKeys(Main.getMDIMain().getConnection(), tableName);
			resetImportKeysTable(vecKeys[0]);
			resetExportKeysTable(vecKeys[1]);
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC202E", se.getMessage());
			return;
		}
	}

	/**
	 * set refreshable flag
	 * Refreshable method
	 */
	public void setRefreshable(boolean b) {
		this.refreshable = b;
	}

	/**
	 * check it is need to refresh
	 * Refreshable method
	 */
	public boolean isReFreshable() {
		return refreshable;
	}

	/**
	 * clear all table data
	 * Refreshable method
	 */
	public void clearData() {
		TableModel modelImport = tblImportKeys.getModel();
		TableModel modelExport = tblExportKeys.getModel();

		if (modelImport instanceof KeyTableModel) {
			((KeyTableModel) modelImport).removeAllRows();
		}
		if (modelExport instanceof KeyTableModel) {
			((KeyTableModel) modelExport).removeAllRows();
		}
	}

	/**
	 * refresh imported keys table
	 */
	void resetImportKeysTable(Vector data) {
		Vector vecHeader = new Vector();

		for (int i = 0; i < importTableHeader.length; i++) {
			vecHeader.add(importTableHeader[i]);
		}

		KeyTableModel model = new KeyTableModel(data, vecHeader);

		tblImportKeys.setModel(model);

		for (int i = 0; i < columnWidth.length; i++) {
			tblImportKeys.getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
		}

		tblImportKeys.repaint();
	}

	/**
	 * refresh exported keys table
	 */
	void resetExportKeysTable(Vector data) {
		Vector vecHeader = new Vector();

		for (int i = 0; i < exportTableHeader.length; i++) {
			vecHeader.add(exportTableHeader[i]);
		}

		KeyTableModel model = new KeyTableModel(data, vecHeader);
		tblExportKeys.setModel(model);

		for (int i = 0; i < columnWidth.length; i++) {
			tblExportKeys.getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
		}

		tblExportKeys.repaint();
	}


	/**
	 * key table model
	 *
	 */
	class KeyTableModel extends DefaultTableModel {
		public KeyTableModel(Vector data, Vector header) {
			super(data, header);
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public void removeAllRows() {
			int rows = super.dataVector.size();
			if (rows > 0){
				fireTableRowsDeleted(0, rows - 1);
				super.dataVector.removeAllElements();
			}
		}
	}
}