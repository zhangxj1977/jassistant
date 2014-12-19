package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

import org.jas.base.DBTableModel;
import org.jas.base.PJDBDataTable;
import org.jas.base.PJDBDataTableRowHeader;
import org.jas.base.PJDBDataTableRowHeaderRender;
import org.jas.base.PJSQLTextPane;
import org.jas.base.PJTableCellRender;
import org.jas.base.PJTableRowHeaderModel;
import org.jas.base.RollOverButton;
import org.jas.common.PJConst;
import org.jas.db.DBParser;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;

/**
 *
 *
 *
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class PanelSQLScript extends JSplitPane {
	ImageIcon iconSQLExecute = ImageManager.createImageIcon("sqlexecute.gif");
	ImageIcon iconCommit = ImageManager.createImageIcon("commit.gif");
	ImageIcon iconUndo = ImageManager.createImageIcon("undo.gif");
	ImageIcon iconRedo = ImageManager.createImageIcon("redo.gif");
	ImageIcon iconToUpper = ImageManager.createImageIcon("touppercase.gif");
	ImageIcon iconToLower = ImageManager.createImageIcon("tolowcase.gif");
	ImageIcon iconSQLBuilder = ImageManager.createImageIcon("sqlbuilder.gif");
	ImageIcon iconShowTables = ImageManager.createImageIcon("showtables.gif");
	ImageIcon iconEditOneRow = ImageManager.createImageIcon("editonerow.gif");
	ImageIcon iconResultSave = ImageManager.createImageIcon("savetabledata.gif");
	BorderLayout mainBrderLayout = new BorderLayout();
	JPanel panelSQLScriptInput = new JPanel();
	JPanel panelSQLResult = new JPanel();
	BorderLayout borderLayoutInput = new BorderLayout();
	BorderLayout borderLayoutResult = new BorderLayout();
	JPanel panelInput = new JPanel();
	JScrollPane scpSQLScriptInput = new JScrollPane();
	JLabel rowHeaderLabel = new JLabel();
	BorderLayout borderLayoutScriptInput = new BorderLayout();
	PJSQLTextPane txtPanelSQLScript = new PJSQLTextPane();
	JToolBar toolBarResult = new JToolBar();
	JScrollPane scpResultTable = new JScrollPane();
	ScrollChangeListener scrollChangeListener = new ScrollChangeListener();
	JTable tblScriptResult = new PJDBDataTable();
	PJTableCellRender dbTableCellRender = new PJTableCellRender();
	Border border1;
	String lastSQL = null;
	TitledBorder titledBorder1;
	JToolBar toolBarSQL = new JToolBar();
	JButton btnLeftTopCorner = new JButton();
	PJDBDataTableRowHeader rowHeader = new PJDBDataTableRowHeader(tblScriptResult);
	TableSelectionRowListener selectionRowListener = new TableSelectionRowListener();
	RollOverButton btnExecuteSQL = new RollOverButton();
	RollOverButton btnUndo = new RollOverButton();
	RollOverButton btnRedo = new RollOverButton();
	RollOverButton btnCommit = new RollOverButton();
	RollOverButton btnToUpper = new RollOverButton();
	RollOverButton btnToLower = new RollOverButton();
	RollOverButton btnSQLBuilder = new RollOverButton();
	RollOverButton btnShowUsefulInfo = new RollOverButton();
	RollOverButton btnSaveResult = new RollOverButton();

	public PanelSQLScript() {
		try {
			jbInit();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		border1 = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.gray,Color.white,Color.white,new Color(103, 101, 98));
		toolBarSQL.setFloatable(false);
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.setBorder(border1);
		panelSQLScriptInput.setLayout(borderLayoutInput);
		panelSQLResult.setLayout(borderLayoutResult);
		panelInput.setLayout(borderLayoutScriptInput);
		btnExecuteSQL.setToolTipText("execute the current sql script  SHIFT-F9");
		btnExecuteSQL.setIcon(iconSQLExecute);
		btnExecuteSQL.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeSQLPerformed();
			}
		});
		btnUndo.setIcon(iconUndo);
		btnUndo.setToolTipText("undo last change  CTRL-Z");
		btnUndo.setEnabled(false);
		btnRedo.setEnabled(false);
		btnRedo.setToolTipText("redo last undo  CTRL-Y");
		btnRedo.setIcon(iconRedo);
		btnCommit.setToolTipText("commit  CTRL-F9");
		btnCommit.setIcon(iconCommit);
		btnCommit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitPerformed();
			}
		});
		btnToUpper.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toUpperPerformed();
			}
		});
		btnToUpper.setIcon(iconToUpper);
		btnToUpper.setToolTipText("To Upper Case ALT-F5");
		btnToLower.setToolTipText("To Lower Case Shift-F5");
		btnToLower.setIcon(iconToLower);
		btnToLower.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toLowerPerformed();
			}
		});
		btnSQLBuilder.setIcon(iconSQLBuilder);
		btnSQLBuilder.setToolTipText("SQL Format");
		btnSQLBuilder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sqlBuilderPerformed();
			}
		});
		btnShowUsefulInfo.setIcon(iconShowTables);
		btnShowUsefulInfo.setToolTipText("Select Tables, Columns, Functions");
		btnShowUsefulInfo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTablesPerformed();
			}
		});
		panelSQLScriptInput.add(panelInput,  BorderLayout.CENTER);
		panelInput.add(scpSQLScriptInput,  BorderLayout.CENTER);
		panelSQLScriptInput.add(toolBarSQL, BorderLayout.NORTH);
		scpSQLScriptInput.getViewport().add(txtPanelSQLScript, null);
		txtPanelSQLScript.setUndoRedoButton(btnUndo, btnRedo);
		txtPanelSQLScript.addKeyListener(new TextPaneKeyListener());
		scpSQLScriptInput.getViewport().addChangeListener(scrollChangeListener);
		rowHeaderLabel.setPreferredSize(new Dimension(15, 200));
		rowHeaderLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				rowHeader_Clicked(e);
			}
		});
		scpSQLScriptInput.setRowHeaderView(rowHeaderLabel);
		btnSaveResult.setIcon(iconResultSave);
		toolBarResult.setFloatable(false);
		toolBarResult.add(btnSaveResult);
		btnSaveResult.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableModel model = tblScriptResult.getModel();
				if (model instanceof DBTableModel) {
					DBTableModel dbModel = (DBTableModel) model;
					DialogSaveResultData dialogSaveResultData = new DialogSaveResultData();
					dialogSaveResultData.initResources(dbModel, lastSQL);
					dialogSaveResultData.setVisible(true);
				}
			}
		});
		panelSQLResult.add(toolBarResult, BorderLayout.NORTH);
		panelSQLResult.add(scpResultTable, BorderLayout.CENTER);
		scpResultTable.getViewport().add(tblScriptResult, null);
		tblScriptResult.setDefaultRenderer(Object.class, dbTableCellRender);
		tblScriptResult.getSelectionModel().addListSelectionListener(selectionRowListener);
		tblScriptResult.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scpResultTable.setRowHeaderView(rowHeader);
		rowHeader.getSelectionModel().addListSelectionListener(selectionRowListener);
		btnLeftTopCorner.setIcon(iconEditOneRow);
		btnLeftTopCorner.setPreferredSize(new Dimension(20, 20));
		btnLeftTopCorner.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectRow = tblScriptResult.getSelectedRow();
				viewRowData(selectRow);
			}
		});
		scpResultTable.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, btnLeftTopCorner);
		toolBarSQL.add(btnExecuteSQL, null);
		toolBarSQL.add(btnCommit, null);
		JToolBar.Separator separator1 = new JToolBar.Separator(new Dimension(2, 28));
		separator1.setBorder(BorderFactory.createEtchedBorder());
		toolBarSQL.add(separator1, null);
		toolBarSQL.add(btnUndo, null);
		toolBarSQL.add(btnRedo, null);
		JToolBar.Separator separator2 = new JToolBar.Separator(new Dimension(2, 28));
		separator2.setBorder(BorderFactory.createEtchedBorder());
		toolBarSQL.add(separator2, null);
		toolBarSQL.add(btnToUpper, null);
		toolBarSQL.add(btnToLower, null);
		JToolBar.Separator separator3 = new JToolBar.Separator(new Dimension(2, 28));
		separator3.setBorder(BorderFactory.createEtchedBorder());
		toolBarSQL.add(separator3, null);
		toolBarSQL.add(btnSQLBuilder, null);
		toolBarSQL.add(btnShowUsefulInfo, null);
		this.setBottomComponent(panelSQLResult);
		this.setTopComponent(panelSQLScriptInput);
		this.setDividerLocation(250);
	}

	/**
	 * init current session table and view names
	 */
	void resetPanel() {
		txtPanelSQLScript.setText("");
		setEmptyTableModel();
		if (Main.getMDIMain().getConnection() != null) {
			txtPanelSQLScript.setEnabled(true);
			btnCommit.setEnabled(true);
			btnExecuteSQL.setEnabled(true);
		} else {
			txtPanelSQLScript.setEnabled(false);
			btnCommit.setEnabled(false);
			btnExecuteSQL.setEnabled(false);
		}

		txtPanelSQLScript.resetUndoManager();
	}

	/**
	 * reset the default font style
	 */
	void resetDefaultFontStyle() {
		txtPanelSQLScript.resetDefaultFontStyle();
	}

	/**
	 * set empty table data
	 */
	void setEmptyTableModel() {
		Vector tempData = new Vector();
		Vector columnNameVector = new Vector();
		Vector commentVector = new Vector();
		Vector typeVector = new Vector();
		Vector sizeVector = new Vector();
		Vector keyVector = new Vector();
		Vector oneDataVector = new Vector();

		columnNameVector.add("       ");
		typeVector.add(String.class);
		sizeVector.add(new Integer(20));
		keyVector.add(new Boolean(false));
        commentVector.add("");
		oneDataVector.add("     ");

		tempData.add(columnNameVector);
		tempData.add(typeVector);
		tempData.add(sizeVector);
		tempData.add(keyVector);
        tempData.add(commentVector);
		tempData.add(oneDataVector);

		PJTableRowHeaderModel rowHeaderModel = new PJTableRowHeaderModel(rowHeader, tempData);
		rowHeader.setModel(rowHeaderModel);

		rowHeaderModel.resetTable();
		rowHeader.getColumnModel().getColumn(0).setCellRenderer(new PJDBDataTableRowHeaderRender());

		DBTableModel dataModel = new DBTableModel(tblScriptResult, null, tempData, PJConst.TABLE_TYPE_READONLY);
		tblScriptResult.setModel(dataModel);

		tblScriptResult.getColumnModel().getColumn(0).setPreferredWidth(120);

		tblScriptResult.repaint();
	}

	/**
	 * execute sql action perform
	 */
	void executeSQLPerformed() {
		String sql;
		String selectSql = txtPanelSQLScript.getSelectedText();
		if (selectSql != null && !selectSql.trim().equals("")) {
			sql = selectSql;
		} else {
			sql = txtPanelSQLScript.getText();
		}

		processResultShow(sql);
		lastSQL = sql;
		txtPanelSQLScript.requestFocus();
	}

	/**
	 * commit sql action perform
	 */
	void commitPerformed() {
		try {
			Connection conn = Main.getMDIMain().getConnection();
			if (conn != null) {
				conn.commit();
			}
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC202E", se.getMessage());
			return;
		}

		txtPanelSQLScript.requestFocus();
	}

	/**
	 * to upper case
	 */
	void toUpperPerformed() {
		txtPanelSQLScript.toUpperCase();
	}

	/**
	 * to lower case
	 */
	void toLowerPerformed() {
		txtPanelSQLScript.toLowerCase();
	}

	/**
	 * show sql builder window
	 */
	void sqlBuilderPerformed() {
		txtPanelSQLScript.formatSql();
	}

	/**
	 * open show tables window
	 */
	void showTablesPerformed() {
		DialogSelectTableColumns dialogSelectTableColumns = new DialogSelectTableColumns(this);
		dialogSelectTableColumns.setVisible(true);
	}

	/**
	 * edit one row
	 */
	void viewRowData(int selectRow) {
		if (selectRow < 0) {
			return;
		}

		TableModel model = tblScriptResult.getModel();
		if (model instanceof DBTableModel) {
			DBTableModel dbModel = (DBTableModel) model;
			Vector columnName = dbModel.getRealColumnName();
			Vector commentVector = dbModel.getColumnComment();
			Vector typeData = dbModel.getColumnJavaType();
			Vector sizeVector = dbModel.getSizeVector();
			Vector keyVector = dbModel.getKeyVector();
			Vector rowData = dbModel.getRowData(selectRow);

			DialogEditOneRow dialogEditOneRow = new DialogEditOneRow();
			dialogEditOneRow.initUI(columnName, commentVector, typeData,
									sizeVector, keyVector,
									rowData, selectRow, PJConst.TABLE_TYPE_READONLY);
			dialogEditOneRow.setVisible(true);
		}
	}

	/**
	 * row header click envent
	 * to select the current line
	 */
	void rowHeader_Clicked(MouseEvent e) {
		Point viewPosition = scpSQLScriptInput.getViewport().getViewPosition();
		Point clickedPosition = e.getPoint();
		Font defaultFont = txtPanelSQLScript.getFont();
		FontMetrics fontMetrics = txtPanelSQLScript.getFontMetrics(defaultFont);
		int fontHeight = fontMetrics.getHeight();
		int line = (int) (viewPosition.getY() + clickedPosition.getY()) / fontHeight;

		txtPanelSQLScript.selectLine(line);
	}

	/**
	 * table selection listener
	 */
	class TableSelectionRowListener implements javax.swing.event.ListSelectionListener {

		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			Object source = e.getSource();

			if (!e.getValueIsAdjusting()) {
				if (source == tblScriptResult.getSelectionModel()) {
					int selectedRow = tblScriptResult.getSelectedRow();
					if (rowHeader.getSelectedRow() != selectedRow && selectedRow >= 0) {
						rowHeader.changeSelection(selectedRow, 0, false, false);
					}

					if (selectedRow < 0) {
						rowHeader.clearSelection();
					}
				} else if (source == rowHeader.getSelectionModel()) {
					int selectedRow = rowHeader.getSelectedRow();

					if (selectedRow >= 0) {
						if (tblScriptResult.getSelectedRow() != selectedRow) {
							tblScriptResult.changeSelection(selectedRow, 0, false, false);
						}
					} else {
						tblScriptResult.clearSelection();
					}
				}
			}
		}
	}

	/**
	 * when scrolled reset the row header
	 *
	 */
	class ScrollChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			int totalHeight = (int) scpSQLScriptInput.getViewport().getViewSize().getHeight();

			rowHeaderLabel.setPreferredSize(new Dimension(15, totalHeight));
			scpSQLScriptInput.setRowHeaderView(rowHeaderLabel);
			scpSQLScriptInput.repaint();
		}
	}

	/**
	 * process query result show
	 */
	void processResultShow(String sql) {
		if (sql == null || sql.trim().equals("")) {
			return;
		}

		try {
			long beginTime = System.currentTimeMillis();
			Object value = DBParser.getResultByScript(Main.getMDIMain().getConnection(), sql);
			long endTime = System.currentTimeMillis();
			long executeTime = endTime - beginTime;

			if (value instanceof Vector) {
				Vector data = (Vector) value;
				DBTableModel dataModel = new DBTableModel(tblScriptResult, null, data, PJConst.TABLE_TYPE_READONLY);
				tblScriptResult.setModel(dataModel);

				PJTableRowHeaderModel rowHeaderModel = new PJTableRowHeaderModel(rowHeader, data);
				rowHeader.setModel(rowHeaderModel);

				if (data != null && data.size() > 3) {
					rowHeaderModel.resetTable();
					rowHeader.getColumnModel().getColumn(0).setCellRenderer(new PJDBDataTableRowHeaderRender());
					dataModel.resetTable();
					Main.getMDIMain().setStatusText((data.size() - 4) + " rows returned: " + executeTime + " ms");
				}
			} else if (value instanceof Integer) {
				int affectRows = ((Integer) value).intValue();
				Main.getMDIMain().setStatusText(affectRows + " rows updated or inserted: " + executeTime + " ms");
				setEmptyTableModel();
			}
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC202E", se.getMessage());
			return;
		}
	}

	/**
	 * text pane key listener
	 * it can do highlight, undo redo....
	 */
	class TextPaneKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_F9 && e.isShiftDown()) {
				executeSQLPerformed();
			} else if (keyCode == KeyEvent.VK_F9 && e.isControlDown()) {
				commitPerformed();
			}
		}
	}
}

