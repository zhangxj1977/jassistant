package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import org.jas.base.FileClassLoader;
import org.jas.base.PJTableCellRender;
import org.jas.base.RollOverButton;
import org.jas.common.PJConst;
import org.jas.common.Refreshable;
import org.jas.db.DBParser;
import org.jas.model.ColumnDescriptionData;
import org.jas.util.FileManager;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;
import org.jas.util.PropertyManager;
import org.jas.util.StringUtil;
import org.jas.util.TemplateManager;

/**
 *
 * @author 張　学軍
 * @version 1.0
 */
public class PanelBeanCreate extends JPanel implements Refreshable {
	BorderLayout rightBorderLayout = new BorderLayout();
	JPanel panelCenter = new JPanel();
	JPanel panelBottom = new JPanel();
	JScrollPane scpColoumnDesc = new JScrollPane();
	BorderLayout layoutTableBorderLayout = new BorderLayout();
	JTable tblColumnDesc = new JTable();
	TitledBorder titledBorder1;
	JLabel lblPackageName = new JLabel();
	JTextField txtPackageName = new JTextField();
	JButton btnCreate = new JButton();
	static FileDialog fileDialog;
	ImageIcon iconImportFromFile = ImageManager.createImageIcon("openfile.gif");
	ImageIcon iconSaveToFile = ImageManager.createImageIcon("savetabledata.gif");
	ImageIcon iconAddColumn = ImageManager.createImageIcon("addrow.gif");
	ImageIcon iconRefresh = ImageManager.createImageIcon("refreshtable.gif");
	ImageIcon iconRemoveColumn = ImageManager.createImageIcon("deleterow.gif");
	ImageIcon iconSelectedRow = ImageManager.createImageIcon("selectedrow.gif");
	ImageIcon iconUparrow = ImageManager.createImageIcon("uparrow.gif");
	ImageIcon iconDownarrow = ImageManager.createImageIcon("downarrow.gif");
	JLabel lblDescription = new JLabel();
	JTextField txtClassDescription = new JTextField();
	ButtonCellRender buttonCellRender = new ButtonCellRender();
	LabelCellRender labelCellRender = new LabelCellRender();
	TableSelectionRowListener selectionRowListener = new TableSelectionRowListener();
	PJTableCellRender dbTableCellRender = new PJTableCellRender();
	BorderLayout borderLayoutTopPanel = new BorderLayout();
	RollOverButton btnImportFromFile = new RollOverButton();
	RollOverButton btnSaveToFile = new RollOverButton();
	RollOverButton btnAddColumn = new RollOverButton();
	RollOverButton btnRefresh = new RollOverButton();
	RollOverButton btnUpMove = new RollOverButton();
	RollOverButton btnDownMove = new RollOverButton();
	RollOverButton btnRemoveColumn = new RollOverButton();
	JToolBar toolBarTop = new JToolBar();
	boolean refreshable = true;
	ButtonMenuActionListener buttonMenuActionListener = new ButtonMenuActionListener();

	public PanelBeanCreate() {
		try {
			jbInit();
			initTableColumn();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Create Java Bean");
		this.setLayout(rightBorderLayout);
		panelCenter.setLayout(layoutTableBorderLayout);
		panelBottom.setBorder(titledBorder1);
		panelBottom.setLayout(null);
		this.setPreferredSize(new Dimension(600, 520));
		lblPackageName.setPreferredSize(new Dimension(30, 20));
		lblPackageName.setHorizontalAlignment(SwingConstants.LEFT);
		lblPackageName.setText("Package:");
		lblPackageName.setBounds(new Rectangle(10, 25, 67, 17));
		txtPackageName.setPreferredSize(new Dimension(100, 20));
		txtPackageName.setBounds(new Rectangle(84, 21, 230, 22));
		btnCreate.setMargin(new Insets(0, 0, 0, 0));
		btnCreate.setText("Create");
		btnCreate.setBounds(new Rectangle(322, 47, 63, 25));
		btnCreate.addActionListener(buttonMenuActionListener);
		lblDescription.setText("Description:");
		lblDescription.setBounds(new Rectangle(9, 52, 69, 21));
		txtClassDescription.setBounds(new Rectangle(84, 49, 230, 22));
		txtClassDescription.setPreferredSize(new Dimension(100, 20));
		btnImportFromFile.setText("");
		btnImportFromFile.setIcon(iconImportFromFile);
		btnImportFromFile.addActionListener(buttonMenuActionListener);
		btnImportFromFile.setToolTipText("<html>import from html, jsp, <br>class file or previous saved file");
		btnSaveToFile.setText("");
		btnSaveToFile.setIcon(iconSaveToFile);
		btnSaveToFile.addActionListener(buttonMenuActionListener);
		btnSaveToFile.setToolTipText("<html>save to file .bean, <br>this can be imported and restored");
		btnAddColumn.setText("");
		btnAddColumn.setIcon(iconAddColumn);
		btnAddColumn.addActionListener(buttonMenuActionListener);
		btnAddColumn.setToolTipText("add one line description");
		btnRefresh.setToolTipText("refresh descriptions from database");
		btnRefresh.setText("");
		btnRefresh.setIcon(iconRefresh);
		btnRefresh.setEnabled(false);
		btnRefresh.addActionListener(buttonMenuActionListener);
		btnUpMove.setToolTipText("move line up");
		btnUpMove.setActionCommand("Up");
		btnUpMove.setIcon(iconUparrow);
		btnUpMove.addActionListener(buttonMenuActionListener);
		btnDownMove.addActionListener(buttonMenuActionListener);
		btnDownMove.setToolTipText("move line down");
		btnDownMove.setActionCommand("Down");
		btnDownMove.setIcon(iconDownarrow);
		btnRemoveColumn.setText("");
		btnRemoveColumn.setIcon(iconRemoveColumn);
		btnRemoveColumn.addActionListener(buttonMenuActionListener);
		btnRemoveColumn.setEnabled(false);
		btnRemoveColumn.setToolTipText("remove one line description");
		toolBarTop.setFloatable(false);
		panelBottom.add(lblPackageName, null);
		panelBottom.add(txtPackageName, null);
		panelBottom.add(lblDescription, null);
		panelBottom.add(txtClassDescription, null);
		panelBottom.add(btnCreate, null);
		this.add(toolBarTop, BorderLayout.NORTH);
		this.add(panelCenter, BorderLayout.CENTER);
		panelCenter.add(scpColoumnDesc,  BorderLayout.CENTER);
		this.add(panelBottom, BorderLayout.SOUTH);
		scpColoumnDesc.getViewport().add(tblColumnDesc, null);
		tblColumnDesc.setModel(dataModel);
		tblColumnDesc.setRowHeight(18);
		tblColumnDesc.setDefaultRenderer(Object.class, dbTableCellRender);
		tblColumnDesc.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tblColumnDesc.setColumnSelectionAllowed(true);
		tblColumnDesc.getTableHeader().setReorderingAllowed(false);
		tblColumnDesc.getSelectionModel().addListSelectionListener(selectionRowListener);
		tblColumnDesc.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		tblColumnDesc.registerKeyboardAction(new PasteAction(),
											 KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false),
											 JComponent.WHEN_FOCUSED);
		tblColumnDesc.registerKeyboardAction(new DeleteAction(),
											 KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false),
											 JComponent.WHEN_FOCUSED);
		tblColumnDesc.registerKeyboardAction(new CopyAction(),
											 KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false),
											 JComponent.WHEN_FOCUSED);
		tblColumnDesc.registerKeyboardAction(new CutAction(),
											 KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK, false),
											 JComponent.WHEN_FOCUSED);
		toolBarTop.add(btnImportFromFile, null);
		toolBarTop.add(btnSaveToFile, null);
		toolBarTop.add(btnRefresh, null);
		toolBarTop.add(btnAddColumn, null);
		toolBarTop.add(btnRemoveColumn, null);
		toolBarTop.add(btnDownMove, null);
		toolBarTop.add(btnUpMove, null);
		panelBottom.setPreferredSize(new Dimension(400, 90));
	}


	/**
	 * default for the right panel
	 */
	public void clearAll() {
		btnRefresh.setEnabled(false);
		btnRemoveColumn.setEnabled(false);
		btnDownMove.setEnabled(false);
		btnUpMove.setEnabled(false);
		btnAddColumn.setEnabled(false);
		btnCreate.setEnabled(false);
		txtPackageName.setEnabled(false);
		txtClassDescription.setEnabled(false);
		tblColumnDesc.setEnabled(false);
	}

	/*************************************************************************************
	/* 業務用エリア
	/************************************************************************************/
	// Table init
	String currentTableName = null;
	String currentBeanType = "";
	DBDescTableModel dataModel = new DBDescTableModel();
	String[] tableColumnHeaders = new String[]{" ", "Name", "Type", "Value", "Getter", "Setter", "Comment", "isPrimaryKey"};
	int needColumn = 7;
	int[] columnWidth = new int[]{5, 110, 85, 20, 20, 20, 140};
	int[] BUTTON_COLUMN = {0};
	int[] CHECKBOX_COLUMN = {4, 5};
	int[] LABELTEXT_COLUMN = {1, 6};
	int[] COMBOBOX_COLUMN = {2};
	Vector tableData = new Vector();
	public final String[] columnTypeClassNames = new String[]
						{   "java.lang.String", "java.lang.Integer", "java.lang.Long", "java.lang.Short",
							"java.lang.Byte", "java.lang.Float", "java.lang.Double", "java.lang.Boolean",
							"java.sql.Timestamp", "java.util.Date", "java.lang.Object",	"byte[]",
							"java.util.ArrayList", "java.util.Vector", "int", "long", "short", "char",
							"byte", "float", "double", "boolean"};

	private void initTableColumn() {

		for (int i = 0; i < columnWidth.length; i++) {
			tblColumnDesc.getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
		}

		JComboBox cmbClassNames = new JComboBox(columnTypeClassNames);
		cmbClassNames.setEditable(true);
		tblColumnDesc.getColumn(tableColumnHeaders[COMBOBOX_COLUMN[0]]).setCellEditor(new DefaultCellEditor(cmbClassNames));

		tblColumnDesc.getColumnModel().getColumn(BUTTON_COLUMN[0]).setCellRenderer(buttonCellRender);
		tblColumnDesc.getColumnModel().getColumn(LABELTEXT_COLUMN[0]).setCellRenderer(labelCellRender);
		tblColumnDesc.getColumnModel().getColumn(LABELTEXT_COLUMN[1]).setCellRenderer(labelCellRender);
	}

	/**
	 * table data init
	 */
	public void setTableColumnDescData(ArrayList listData) {
		dataModel.removeAllRows();

		if (listData != null && !listData.isEmpty()) {
			Boolean getterBool = convertGetter();
			Boolean setterBool = convertSetter();

			for (int i=0; i<listData.size(); i++) {
				Vector vecOneRecord = new Vector();

				ColumnDescriptionData oneData = (ColumnDescriptionData) listData.get(i);

				boolean isPrimaryKey = oneData.getPrimaryKeySeq() > 0;
				String columnName = convertColumnName(oneData.getColumnName(), PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_NAME_CASE));
				String columnClass = convertColumnClass(oneData.getColumnType(), oneData.getPrecision() > 0 ? oneData.getPrecision() : oneData.getColumnSize());
				String columnComment = convertComment(oneData.getComment(), columnName, PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT));

				if (isPrimaryKey) {
					ImageIcon iconPrimaryKey = ImageManager.createImageIcon("primarykey.gif");
					vecOneRecord.add(iconPrimaryKey);
				} else {
					vecOneRecord.add(String.valueOf(i + 1));
				}
				vecOneRecord.add(columnName);
				vecOneRecord.add(columnClass);
				vecOneRecord.add("null");
				vecOneRecord.add(getterBool);
				vecOneRecord.add(setterBool);
				vecOneRecord.add(columnComment);
				vecOneRecord.add(new Boolean(isPrimaryKey));

				dataModel.addRow(vecOneRecord);
			}

			for (int i=0; i<columnWidth.length; i++) {
				tblColumnDesc.getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
			}
		}
	}

	/**
	 * add one default row
	 */
	private void addDefaultRow() {
		Vector vecOneRecord = new Vector();

		vecOneRecord.add(String.valueOf(tblColumnDesc.getRowCount() + 1));
		vecOneRecord.add("");
		vecOneRecord.add(columnTypeClassNames[0]);
		vecOneRecord.add("null");
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_METHOD_GETTER))) {
			vecOneRecord.add(new Boolean(true));
		} else {
			vecOneRecord.add(new Boolean(false));
		}
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_METHOD_SETTER))) {
			vecOneRecord.add(new Boolean(true));
		} else {
			vecOneRecord.add(new Boolean(false));
		}

		vecOneRecord.add(null);
		vecOneRecord.add(new Boolean(false));

		int selectedRow = tblColumnDesc.getSelectedRow();
		if (selectedRow >= 0 && tblColumnDesc.getSelectedColumns()[0] == BUTTON_COLUMN[0]) {
			dataModel.addRow(tblColumnDesc.getSelectedRow(), vecOneRecord);
		} else {
			dataModel.addRow(vecOneRecord);
			selectedRow = dataModel.getRowCount() - 1;
		}

		dataModel.rellocateIndexColumn();

		tblColumnDesc.changeSelection(selectedRow, 0, false, false);
	}

	/**
	 * Convert column name label to prefrence case
	 *
	 * @param label column name
	 * @param flag 1: no convert, 2: to lowcase, 3: to upcase
	 *
	 * @return new column name
	 */
	private String convertColumnName(String name, String flag) {
		if (PJConst.COLUMN_NAME_CASE_NO.equals(flag)) {
			return name;
		} else if (PJConst.COLUMN_NAME_CASE_LOWCASE.equals(flag)) {
			name = name.toLowerCase();
			int pos = 0;
			while ((pos = name.indexOf('_')) > 0) {
				if (pos + 1 < name.length()) {
					name = name.substring(0, pos)
							+ name.substring(pos + 1, pos + 2).toUpperCase()
							+ name.substring(pos + 2);
				} else {
					name = name.substring(0, pos);
				}
			}
			return name;
		} else if (PJConst.COLUMN_NAME_CASE_UPCASE.equals(flag)) {
			return name.toUpperCase();
		}

		return name;
	}

	/**
	 * convert getter method
	 */
	private Boolean convertGetter() {
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_METHOD_GETTER))) {
			return (new Boolean(true));
		} else {
			return (new Boolean(false));
		}
	}

	/**
	 * convert getter method
	 */
	private Boolean convertSetter() {
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_METHOD_SETTER))) {
			return (new Boolean(true));
		} else {
			return (new Boolean(false));
		}
	}

	/**
	 * convert comment value
	 */
	private String convertComment(String remark, String column, String flag) {
		if (remark != null) {
			return remark;
		}
		if (PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT_NAME.equals(flag)) {
			return column;
		} else if (PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT_NULL.equals(flag)) {
			return null;
		}

		return null;
	}

	/**
	 * set param that to search data
	 */
	public void setParam(String beanType, String tableName) {
		this.currentBeanType = beanType;
		this.currentTableName = tableName;
	}

	/**
	 * search column descriptions from database
	 *
	 * @param tableName
	 */
	public void resetData() {
		ArrayList columnListData = null;

		try {
			Connection conn = Main.getMDIMain().getConnection();
			columnListData = DBParser.getColumnDescription(conn, currentTableName);
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC202E", se.getMessage());
			return;
		}

		setTableColumnDescData(columnListData);

		// 画面表示設定
		refreshDisplay();
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
		;
	}


	/**
	 * 画面表示設定
	 */
	public void refreshDisplay() {
		if (currentTableName == null || currentTableName.equals("") || currentBeanType.equals(PJConst.BEAN_TYPE_NEWBEAN)) {
			btnRefresh.setEnabled(false);
		} else {
			btnRefresh.setEnabled(true);
		}

		tblColumnDesc.clearSelection();
		btnRemoveColumn.setEnabled(false);
		btnDownMove.setEnabled(false);
		btnUpMove.setEnabled(false);
		tblColumnDesc.repaint();
	}


	/*************************************************************************************
	/* 事件処理用エリア
	/************************************************************************************/
	class ButtonMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();

			if (obj == btnRefresh) {
				resetData();
			} else if (obj == btnRemoveColumn) {
				removeColumn();
			} else if (obj == btnDownMove) {
				moveRowDown();
			} else if (obj == btnUpMove) {
				moveRowUp();
			} else if (obj == btnAddColumn) {
				addDefaultRow();
			} else if (obj == btnImportFromFile) {
				importFromFile();
			} else if (obj == btnSaveToFile) {
				saveToFile();
			} else if (obj == btnCreate) {
				createBean();
			}
		}
	}

	// remove button action
	void removeColumn() {
		if (tblColumnDesc.getSelectedRow() >= 0) {
			dataModel.removeRow(tblColumnDesc.getSelectedRow());
			dataModel.rellocateIndexColumn();
		}
		refreshDisplay();
	}

	/**
	 * move row down
	 */
	void moveRowDown() {
		int orgRow = tblColumnDesc.getSelectedRow();

		if (orgRow >= 0) {
			dataModel.moveRow(tblColumnDesc.getSelectedRow(), false);
			dataModel.rellocateIndexColumn();
			if (orgRow < dataModel.getRowCount() - 1) {
				tblColumnDesc.changeSelection(orgRow + 1, 0, false, false);
			}
		}
	}

	/**
	 * move row up
	 */
	void moveRowUp() {
		int orgRow = tblColumnDesc.getSelectedRow();

		if (orgRow >= 0) {
			dataModel.moveRow(tblColumnDesc.getSelectedRow(), true);
			dataModel.rellocateIndexColumn();

			if (orgRow > 0) {
				tblColumnDesc.changeSelection(orgRow - 1, 0, false, false);
			}
		}
	}

	/**
	 * import from files
	 *
	 */
	void importFromFile() {
		if (fileDialog == null) {
			fileDialog = new FileDialog(Main.getMDIMain());
		}
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.show();
		String file = fileDialog.getFile();
		if (file == null) {
			return;
		}

		try {
			String directory = fileDialog.getDirectory();
			File f = new File(directory, file);
			String fileName = f.getCanonicalPath();

			if (fileName.endsWith(".bean")) {
				Vector preTableData = (Vector) FileManager.readObjectFromFile(f);
				dataModel.removeAllRows();
				dataModel.addRows(preTableData);
			} else {
				ArrayList resultFields = new ArrayList();

				if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
					String fileContent = FileManager.readInputStream(fileName).toString();
					resultFields = StringUtil.getHtmlFormFieldName(fileContent);
				} else if (fileName.endsWith(".jsp")) {
					String fileContent = FileManager.readInputStream(fileName).toString();
					resultFields = StringUtil.getStrutsFormFieldName(fileContent);
				} else if (fileName.endsWith(".class")) {
					FileClassLoader fileClassLoader = new FileClassLoader();
					Class obj = fileClassLoader.loadClass(fileName);
					java.lang.reflect.Field[] fields = obj.getDeclaredFields();
					for (int i = 0; i < fields.length; i++) {
						resultFields.add(fields[i]);
					}
				}

				ArrayList columnListData = new ArrayList();
				for (int i = 0; i < resultFields.size(); i++) {
					ColumnDescriptionData oneData = new ColumnDescriptionData();
					Object oneField = resultFields.get(i);

					if (oneField instanceof java.lang.String) {
						oneData.setColumnName((String) resultFields.get(i));
						oneData.setColumnType(Types.VARCHAR);
					} else if (oneField instanceof java.lang.reflect.Field) {
						oneData.setColumnName(((Field) oneField).getName());
						oneData.setColumnType(StringUtil.getSQLTypeByJavaType(((Field) oneField).getType()));
					}
					columnListData.add(oneData);
				}

				setTableColumnDescData(columnListData);
			}

			// 画面表示設定
			refreshDisplay();
		} catch (Exception ex) {
			MessageManager.showMessage("MCSTC002E", ex.getMessage());
			return;
		}
	}

	/**
	 * save to file
	 *
	 */
	void saveToFile() {
		if (fileDialog == null) {
			fileDialog = new FileDialog(Main.getMDIMain());
		}
		fileDialog.setMode(FileDialog.SAVE);
		fileDialog.show();
		String file = fileDialog.getFile();
		if (file == null) {
			return;
		}

		if (!file.endsWith(".bean")) {
			file = file + ".bean";
		}

		try {
			String directory = fileDialog.getDirectory();
			File f = new File(directory, file);
			FileManager.printObjectToFile(f, tableData);
		} catch (Exception ex) {
			MessageManager.showMessage("MCSTC002E", ex.getMessage());
			return;
		}
	}

	/**
	 * create action
	 */
	void createBean() {
		if (fileDialog == null) {
			fileDialog = new FileDialog(Main.getMDIMain());
		}
		fileDialog.setMode(FileDialog.SAVE);
		fileDialog.show();
		String file = fileDialog.getFile();
		if (file == null) {
			return;
		}

		if (!file.endsWith(".java")) {
			file = file + ".java";
		}
		String directory = fileDialog.getDirectory();
		File f = new File(directory, file);

		TemplateManager templateManager = new TemplateManager(txtPackageName.getText(),
							txtClassDescription.getText(),
							Main.configPath,
							tableData, f);

		try {
			templateManager.createJavaBean();
		} catch (IllegalArgumentException ille) {
			MessageManager.showMessage("MCSTC003E", ille.getMessage());
			return;
		} catch (Exception ex) {
			MessageManager.showMessage("MCSTC002E", ex.getMessage());
			return;
		}

		MessageManager.showInfoMessage(MessageManager.getMessage("MCSTC301I"));
	}

	/**
	 * convert database type to java class type
	 *
	 * @param dbTypeName database type name
	 * @param size number size
	 * @return java class to the column.
	 */
	private String convertColumnClass(int columnType, int size) {
		String columnClass = "java.lang.Object";

		// map db type and convert to java class type
		switch (columnType) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				{
					return columnTypeClassNames[0];
				}
			case Types.NUMERIC:
			case Types.DECIMAL:
				{
					if (size < 10) {
						return columnTypeClassNames[1];
					} else if (size < 19) {
						return columnTypeClassNames[2];
					} else {
						return columnTypeClassNames[6];
					}
				}
			case Types.BIT:
				{
					return columnTypeClassNames[7];
				}
			case Types.TINYINT:
				{
					return columnTypeClassNames[4];
				}
			case Types.SMALLINT:
				{
					return columnTypeClassNames[3];
				}
			case Types.INTEGER:
				{
					return columnTypeClassNames[1];
				}
			case Types.BIGINT:
				{
					return columnTypeClassNames[2];
				}
			case Types.REAL:
				{
					return columnTypeClassNames[5];
				}
			case Types.DOUBLE:
				{
					return columnTypeClassNames[6];
				}
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				{
					return columnTypeClassNames[11];
				}
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				{
					return columnTypeClassNames[8];
				}
		}

		return columnClass;
	}


	/**
	 * テーブルのモードクラス定義
	 */
	class DBDescTableModel extends AbstractTableModel {
		// default constructor
		public DBDescTableModel() {
			super();
		}

		// These methods always need to be implemented.
		public int getColumnCount() {
			return tableColumnHeaders.length - 1;
		}

		public int getRowCount() {
			return tableData.size();
		}

		public Object getValueAt(int row, int col) {
			return ((Vector) tableData.get(row)).get(col);
		}

		// The default implementations of these methods in
		// AbstractTableModel would work, but we can refine them.
		public String getColumnName(int column) {
			return tableColumnHeaders[column];
		}

		public Class getColumnClass(int c) {
			Object value = getValueAt(0, c);
			if (value != null) {
				return value.getClass();
			}
			return Object.class;
		}

		public boolean isCellEditable(int row, int col) {
			return (col != BUTTON_COLUMN[0]);
		}

		public void setValueAt(Object aValue, int row, int column) {
			((Vector) tableData.get(row)).set(column, aValue);
		}

		public void addRow(Vector rowData) {
			int rows = tableData.size();
			fireTableRowsInserted(0, rows + 1);
			tableData.add(rowData);
		}

		public void addRows(Vector rowDatas) {
			tableData.addAll(rowDatas);
			fireTableRowsInserted(0, getRowCount());
		}

		public void addRow(int index, Vector rowData) {
			int rows = tableData.size();
			fireTableRowsInserted(0, rows + 1);
			tableData.add(index, rowData);
		}

		public void removeRow(int row) {
			int rows = tableData.size();
			if (rows > 0){
				fireTableRowsDeleted(0, rows - 1);
				tableData.remove(row);
			}
		}

		public void removeAllRows() {
			int rows = tableData.size();
			if (rows > 0){
				fireTableRowsDeleted(0, rows - 1);
				tableData.removeAllElements();
			}
		}

		public void rellocateIndexColumn() {
			for (int i = 0; i < tableData.size(); i++) {
				Vector vecRec = (Vector) tableData.get(i);
				if (vecRec != null && !vecRec.isEmpty()) {
					Boolean isPrimaryKey = (Boolean) vecRec.get(vecRec.size() - 1);

					if (isPrimaryKey != null && isPrimaryKey.booleanValue()) {
						ImageIcon iconPrimaryKey = ImageManager.createImageIcon("primarykey.gif");
						vecRec.set(0, iconPrimaryKey);
					} else {
						vecRec.set(0, String.valueOf(i + 1));
					}
				}
			}
		}

		public void moveRow(int row, boolean up) {
			Vector vecRec = (Vector) tableData.get(row);

			fireTableRowsUpdated(0, tableData.size());

			if (up) {
				if (row > 0) {
					Vector toVecRec = (Vector) tableData.get(row - 1);
					tableData.set(row - 1, vecRec);
					tableData.set(row, toVecRec);
				}
			} else {
				if (row < tableData.size() - 1) {
					Vector toVecRec = (Vector) tableData.get(row + 1);
					tableData.set(row + 1, vecRec);
					tableData.set(row, toVecRec);
				}
			}
		}
	};


	/**
	 * the first count button render
	 */
	class ButtonCellRender extends JToggleButton implements javax.swing.table.TableCellRenderer {
		Border rollBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED,
										Color.white,
										SystemColor.control,
										SystemColor.control,
										new Color(103, 101, 98));
		EmptyBorder emptyBorder = new EmptyBorder(2, 2, 2, 2);

		public Component getTableCellRendererComponent(JTable table,
											   Object value,
											   boolean isSelected,
											   boolean hasFocus,
											   int row,
											   int column) {

			this.setMargin(new Insets(0, 0, 0, 0));
			if (value instanceof ImageIcon) {
				this.setIcon((ImageIcon) value);
				this.setText("");
			} else {
				this.setText((String) value);
				this.setIcon(null);
			}
			this.setPreferredSize(new Dimension(5, 15));
			this.setHorizontalAlignment(SwingConstants.CENTER);

			if (hasFocus && isSelected) {
				table.getSelectionModel().setAnchorSelectionIndex(row);
				table.getSelectionModel().setLeadSelectionIndex(row);
				table.setColumnSelectionInterval(column, table.getColumnCount() - 1);
				this.setText("");
				this.setIcon(iconSelectedRow);
				this.setBorder(emptyBorder);
			} else {
				selectionChanged();
				if (isSelected) {
					table.setColumnSelectionInterval(column, table.getColumnCount() - 1);
					this.setText("");
					this.setIcon(iconSelectedRow);
					this.setBorder(emptyBorder);
				} else  {
					if (value instanceof ImageIcon) {
						this.setText("");
						this.setIcon((ImageIcon) value);
					} else {
						this.setText((String) value);
						this.setIcon(null);
					}
					this.setBorder(rollBorder);
				}
			}

			return this;
		}
	}


	/**
	 * the first count button render
	 */
	class LabelCellRender extends javax.swing.table.DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
											   Object value,
											   boolean isSelected,
											   boolean hasFocus,
											   int row,
											   int column) {

			selectionChanged();

			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}

	/**
	 * table selection listener
	 */
	class TableSelectionRowListener implements javax.swing.event.ListSelectionListener {

		public void valueChanged(javax.swing.event.ListSelectionEvent event) {
			selectionChanged();
		}
	}

	/**
	 * the table selection changed
	 */
	private void selectionChanged() {
		int[] selectedRows = tblColumnDesc.getSelectedRows();
		int[] selectedColumns = tblColumnDesc.getSelectedColumns();

		if (selectedRows.length == 1 && selectedColumns.length > 0 && selectedColumns[0] == BUTTON_COLUMN[0]) {
			btnRemoveColumn.setEnabled(true);
			btnDownMove.setEnabled(true);
			btnUpMove.setEnabled(true);
		} else {
			btnRemoveColumn.setEnabled(false);
			btnDownMove.setEnabled(false);
			btnUpMove.setEnabled(false);
		}

		if (selectedRows.length > 0 && selectedColumns.length == 1 &&
			(selectedColumns[0] == LABELTEXT_COLUMN[0] || selectedColumns[0] == LABELTEXT_COLUMN[1])) {
			setColumnOperationStatus(true);
		} else {
			setColumnOperationStatus(false);
		}
	}

	/**
	 * menu edit and toolbar copy, cut, paste, delete
	 * enabled or disabled
	 */
	private void setColumnOperationStatus(boolean enabled) {
		Main.getMDIMain().setColumnOperationStatus(enabled);
	}

	/**
	 * copy action
	 */
	class CopyAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			copy_Performed();
		}
	}


	/**
	 * paste action
	 */
	class PasteAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			paste_Performed();
		}
	}

	/**
	 * delete action
	 */
	class DeleteAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			delete_Performed();
		}
	}

	/**
	 * cut action
	 */
	class CutAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			cut_Performed();
		}
	}

	/**
	 * perform copy action
	 */
	public void copy_Performed() {
		copy();
		tblColumnDesc.repaint();
	}

	/**
	 * perform paste action
	 */
	public void paste_Performed() {
		paste();
		tblColumnDesc.repaint();
	}

	/**
	 * perform cut action
	 */
	public void cut_Performed() {
		copy();
		deleteSelected();
		tblColumnDesc.repaint();
	}

	/**
	 * perform delete action
	 */
	public void delete_Performed() {
		deleteSelected();
		tblColumnDesc.repaint();
	}

	/**
	 * process copy data
	 */
	private synchronized void copy() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		int[] selectedColumns = tblColumnDesc.getSelectedColumns();
		int[] selectedRows = tblColumnDesc.getSelectedRows();

		if (selectedColumns == null || selectedColumns.length != 1 ||
			selectedRows == null || selectedRows.length == 0 ||
			(selectedColumns[0] != LABELTEXT_COLUMN[0] && selectedColumns[0] != LABELTEXT_COLUMN[1])) {
			return;
		}

		StringBuffer copiedData = new StringBuffer();
		for (int i = 0; i < selectedRows.length; i++) {
			String columnValue = (String) dataModel.getValueAt(selectedRows[i], selectedColumns[0]);
			if (columnValue == null) {
				columnValue = "";
			}
			copiedData.append(columnValue + "\n");
		}
		JTextArea tempArea = new JTextArea(copiedData.toString());
		tempArea.selectAll();
		tempArea.copy();
	}

	/**
	 * process paste data
	 */
	private synchronized void paste() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		String pasteData = null;
		try {
			pasteData = (String) clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor);
		} catch (Exception ex) {
			MessageManager.showMessage("MCSTC002E", ex.getMessage());
		}

		if (pasteData != null) {
			processReplaceColumnData(pasteData);
		}
	}

	/**
	 * convert the pasted data to column data
	 */
	private synchronized void processReplaceColumnData(String pasteData) {
		int[] selectedColumns = tblColumnDesc.getSelectedColumns();
		int[] selectedRows = tblColumnDesc.getSelectedRows();

		if (pasteData == null ||
			selectedColumns == null || selectedColumns.length != 1 ||
			selectedRows == null || selectedRows.length == 0 ||
			(selectedColumns[0] != LABELTEXT_COLUMN[0] && selectedColumns[0] != LABELTEXT_COLUMN[1])) {
			return;
		}
		Object[] copiedLinesData = getCopiedContents(pasteData);
		int copiedLineCounts = copiedLinesData.length;

		if (selectedRows.length > 1) {
			if (copiedLinesData.length > selectedRows.length) {
				if (MessageManager.showMessage("MCSTC004Q") != 0) {
					return;
				}
			}
			for (int i=0; i<selectedRows.length; i++) {
				if (i < copiedLineCounts) {
					dataModel.setValueAt(copiedLinesData[i], selectedRows[i], selectedColumns[0]);
				}
			}
		} else {
			int firstSelectedRow = selectedRows[0];
			int allRows = tblColumnDesc.getRowCount();
			if (copiedLinesData.length > allRows - firstSelectedRow) {
				if (MessageManager.showMessage("MCSTC004Q") != 0) {
					return;
				}
			}
			for (int i=0; i<copiedLineCounts; i++) {
				if (firstSelectedRow + i < allRows) {
					dataModel.setValueAt(copiedLinesData[i], firstSelectedRow + i, selectedColumns[0]);
				}
			}
		}
	}

	/**
	 * copyデータはString[]オブジェクトに生成
	 */
	private synchronized Object[] getCopiedContents(String content) {
		if (content == null) {
			return null;
		}

		StringBuffer newContent = new StringBuffer("");
		StringTokenizer st = new StringTokenizer(content, "\n", true);

		Vector returnVec = new Vector();
		boolean lastIsLineBread = false;
		while(st.hasMoreElements()) {
			String sElement = st.nextToken();
			if (sElement.equals("\n")) {
				if (lastIsLineBread) {
					returnVec.add("");
				}
				lastIsLineBread = true;
			} else {
				lastIsLineBread = false;
				returnVec.add(sElement);
			}
		}
		return returnVec.toArray();
	}


	/**
	 * delete selected rows
	 */
	private synchronized void deleteSelected() {
		int[] selectedColumns = tblColumnDesc.getSelectedColumns();
		int[] selectedRows = tblColumnDesc.getSelectedRows();

		if (selectedColumns == null || selectedColumns.length != 1 ||
			selectedRows == null || selectedRows.length == 0 ||
			(selectedColumns[0] != LABELTEXT_COLUMN[0] && selectedColumns[0] != LABELTEXT_COLUMN[1])) {
			return;
		}

		for (int i=0; i<selectedRows.length; i++) {
			dataModel.setValueAt(null, selectedRows[i], selectedColumns[0]);
		}
	}
}
