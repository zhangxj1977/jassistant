package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.jas.base.PJDialogBase;
import org.jas.base.PJEditableComboBox;
import org.jas.base.PJEditorTextField;
import org.jas.base.PJListModel;
import org.jas.base.PJSQLTextPane;
import org.jas.common.PJConst;
import org.jas.util.FilterSortManager;
import org.jas.util.ImageManager;
import org.jas.util.StringUtil;

/**
 *
 *
 *
 *
 * @author ’£@ŠwŒR
 * @version 1.0
 */

public class DialogFilterSort extends PJDialogBase {
	JPanel panelMain = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel panelTop = new JPanel();
	JButton btnClearSort = new JButton();
	JPanel panelCenter = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JTabbedPane tabbedPaneCenter = new JTabbedPane();
	JPanel panelSort = new JPanel();
	JPanel panelFilter = new JPanel();
	Border border1;
	JButton btnClearFilter = new JButton();
	JButton btnCancel = new JButton();
	JButton btnOK = new JButton();
	BorderLayout borderLayout3 = new BorderLayout();
	Border border2;
	JPanel panelSortWest = new JPanel();
	JPanel panelSortCenter = new JPanel();
	JPanel panelSortEast = new JPanel();
	BorderLayout borderLayout4 = new BorderLayout();
	JScrollPane scpSortOrgColumns = new JScrollPane();
	JScrollPane scpSortSortedColumns = new JScrollPane();
	BorderLayout borderLayout5 = new BorderLayout();
	JList lstSortOrgColumns = new JList();
	PJListModel lstSortOrgColumnsModel = new PJListModel();
	JList lstSortSortedColumns = new JList();
	PJListModel lstSortSortedColumnsModel = new PJListModel();
	JButton btnRemoveSort = new JButton();
	JButton btnSortASCAdd = new JButton();
	JButton btnSortDESCAdd = new JButton();
	JButton btnSortUpMove = new JButton();
	JButton btnSortDownMove = new JButton();
	BorderLayout borderLayout6 = new BorderLayout();
	Border border3;
	JPanel panelFilterNorth = new JPanel();
	JPanel panelFilterCenter = new JPanel();
	BorderLayout borderLayout7 = new BorderLayout();
	JScrollPane scpFilterCenter = new JScrollPane();
	PJSQLTextPane txtFilterScript = new PJSQLTextPane();
	PJEditableComboBox cmbColumns = new PJEditableComboBox();
	PJEditableComboBox cmbOperator = new PJEditableComboBox();
	PJEditorTextField txtValue1 = new PJEditorTextField(null);
	JButton btnAddLeftBrace = new JButton();
	JButton btnAddtoFilter = new JButton();
	JButton btnAndIntoFilter = new JButton();
	JButton btnOrIntoFilter = new JButton();
	PJEditorTextField txtValue2 = new PJEditorTextField(null);
	JButton btnAddRightBrace = new JButton();
	ImageIcon iconRemoveSort = ImageManager.createImageIcon("removesort.gif");
	ImageIcon iconAddSort = ImageManager.createImageIcon("addsort.gif");
	ImageIcon iconMoveSortUp = ImageManager.createImageIcon("movesortup.gif");
	ImageIcon iconMoveSortDown = ImageManager.createImageIcon("movesortdown.gif");
	Vector orginalVector = null;
	String tableName = null;

	public DialogFilterSort(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogFilterSort() {
		this(Main.getMDIMain(), "Filter and Sort", true);
	}

	void jbInit() throws Exception {
		border1 = BorderFactory.createEmptyBorder(5,5,5,5);
		border2 = BorderFactory.createEmptyBorder(2,2,2,2);
		border3 = BorderFactory.createEmptyBorder(2,2,2,2);
		panelMain.setLayout(borderLayout1);
		panelMain.setBorder(border1);
		setSize(500, 300);
		panelTop.setLayout(null);
		btnClearSort.setMaximumSize(new Dimension(60, 20));
		btnClearSort.setMinimumSize(new Dimension(60, 20));
		btnClearSort.setPreferredSize(new Dimension(60, 20));
		btnClearSort.setMargin(new Insets(0, 0, 0, 0));
		btnClearSort.setText("Clear Sort");
		btnClearSort.setMnemonic('S');
		btnClearSort.setBounds(new Rectangle(239, 3, 67, 27));
		btnClearSort.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClearSort_actionPerformed(e);
			}
		});
		panelCenter.setLayout(borderLayout2);
		this.setModal(true);
		this.setResizable(false);
		this.setTitle("Filter and Sort");
		borderLayout1.setVgap(5);
		panelTop.setPreferredSize(new Dimension(500, 30));
		btnClearFilter.setBounds(new Rectangle(310, 3, 67, 27));
		btnClearFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClearFilter_actionPerformed(e);
			}
		});
		btnClearFilter.setText("Clear Filter");
		btnClearFilter.setMnemonic('F');
		btnClearFilter.setMargin(new Insets(0, 0, 0, 0));
		btnClearFilter.setPreferredSize(new Dimension(60, 20));
		btnClearFilter.setMinimumSize(new Dimension(60, 20));
		btnClearFilter.setMaximumSize(new Dimension(60, 20));
		btnCancel.setBounds(new Rectangle(399, 2, 46, 27));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancel_actionPerformed(e);
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.setMargin(new Insets(0, 0, 0, 0));
		btnCancel.setPreferredSize(new Dimension(60, 20));
		btnCancel.setMinimumSize(new Dimension(60, 20));
		btnCancel.setMaximumSize(new Dimension(60, 20));
		btnOK.setMaximumSize(new Dimension(60, 20));
		btnOK.setMinimumSize(new Dimension(60, 20));
		btnOK.setPreferredSize(new Dimension(60, 20));
		btnOK.setMargin(new Insets(0, 0, 0, 0));
		btnOK.setText("OK");
		btnOK.setMnemonic('O');
		btnOK.setBounds(new Rectangle(447, 2, 46, 27));
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});
		panelSort.setLayout(borderLayout3);
		borderLayout3.setHgap(3);
		panelSort.setBorder(border2);
		panelSortWest.setLayout(borderLayout4);
		panelSortWest.setPreferredSize(new Dimension(200, 250));
		panelSortEast.setPreferredSize(new Dimension(200, 250));
		panelSortEast.setLayout(borderLayout5);
		panelSortCenter.setLayout(null);
		btnRemoveSort.setIcon(iconRemoveSort);
		btnRemoveSort.setMargin(new Insets(0, 0, 0, 0));
		btnRemoveSort.setBounds(new Rectangle(55, 20, 25, 25));
		btnRemoveSort.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemoveSort_actionPerformed(e);
			}
		});
		btnSortASCAdd.setHorizontalAlignment(SwingConstants.RIGHT);
		btnSortASCAdd.setHorizontalTextPosition(SwingConstants.LEFT);
		btnSortASCAdd.setIcon(iconAddSort);
		btnSortASCAdd.setMargin(new Insets(0, 0, 0, 0));
		btnSortASCAdd.setText("ASC");
		btnSortASCAdd.setBounds(new Rectangle(15, 70, 65, 25));
		btnSortASCAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSortASCAdd_actionPerformed(e);
			}
		});
		btnSortDESCAdd.setHorizontalAlignment(SwingConstants.RIGHT);
		btnSortDESCAdd.setHorizontalTextPosition(SwingConstants.LEFT);
		btnSortDESCAdd.setIcon(iconAddSort);
		btnSortDESCAdd.setMargin(new Insets(0, 0, 0, 0));
		btnSortDESCAdd.setText("DESC");
		btnSortDESCAdd.setBounds(new Rectangle(15, 100, 65, 25));
		btnSortDESCAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSortDESCAdd_actionPerformed(e);
			}
		});
		btnSortUpMove.setIcon(iconMoveSortUp);
		btnSortUpMove.setMargin(new Insets(0, 0, 0, 0));
		btnSortUpMove.setBounds(new Rectangle(55, 140, 25, 25));
		btnSortUpMove.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSortUpMove_actionPerformed(e);
			}
		});
		btnSortDownMove.setIcon(iconMoveSortDown);
		btnSortDownMove.setMargin(new Insets(0, 0, 0, 0));
		btnSortDownMove.setBounds(new Rectangle(55, 170, 25, 25));
		btnSortDownMove.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSortDownMove_actionPerformed(e);
			}
		});
		panelFilter.setLayout(borderLayout6);
		panelFilter.setBorder(border3);
		borderLayout6.setVgap(3);
		panelFilterNorth.setPreferredSize(new Dimension(500, 60));
		panelFilterNorth.setLayout(null);
		panelFilterCenter.setLayout(borderLayout7);
		cmbColumns.setBounds(new Rectangle(10, 8, 168, 22));
		cmbColumns.setEditable(true);
		cmbOperator.setBounds(new Rectangle(194, 8, 87, 22));
		cmbOperator.setEditable(true);
		txtValue1.setBounds(new Rectangle(297, 8, 120, 23));
		btnAddLeftBrace.setMargin(new Insets(0, 0, 0, 0));
		btnAddLeftBrace.setText("Add (");
		btnAddLeftBrace.setBounds(new Rectangle(432, 6, 54, 24));
		btnAddLeftBrace.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddLeftBrace_actionPerformed(e);
			}
		});
		btnAddtoFilter.setMargin(new Insets(0, 0, 0, 0));
		btnAddtoFilter.setText("Add to Filter");
		btnAddtoFilter.setBounds(new Rectangle(10, 34, 85, 23));
		btnAddtoFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddtoFilter_actionPerformed(e);
			}
		});
		btnAndIntoFilter.setBounds(new Rectangle(103, 35, 85, 22));
		btnAndIntoFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAndIntoFilter_actionPerformed(e);
			}
		});
		btnAndIntoFilter.setMargin(new Insets(0, 0, 0, 0));
		btnAndIntoFilter.setText("AND into Filter");
		btnOrIntoFilter.setBounds(new Rectangle(196, 35, 85, 22));
		btnOrIntoFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOrIntoFilter_actionPerformed(e);
			}
		});
		btnOrIntoFilter.setMargin(new Insets(0, 0, 0, 0));
		btnOrIntoFilter.setText("OR into Filter");
		txtValue2.setBounds(new Rectangle(297, 36, 120, 23));
		btnAddRightBrace.setBounds(new Rectangle(432, 35, 54, 24));
		btnAddRightBrace.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddRightBrace_actionPerformed(e);
			}
		});
		btnAddRightBrace.setMargin(new Insets(0, 0, 0, 0));
		btnAddRightBrace.setText("Add )");
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
		panelMain.add(panelTop, BorderLayout.NORTH);
		panelMain.add(panelCenter,  BorderLayout.CENTER);
		panelCenter.add(tabbedPaneCenter, BorderLayout.CENTER);
		tabbedPaneCenter.add(panelSort,  "Sort");
		panelSort.add(panelSortWest, BorderLayout.WEST);
		panelSort.add(panelSortCenter, BorderLayout.CENTER);
		panelSortCenter.add(btnSortDESCAdd, null);
		panelSortCenter.add(btnRemoveSort, null);
		panelSortCenter.add(btnSortASCAdd, null);
		panelSortCenter.add(btnSortUpMove, null);
		panelSortCenter.add(btnSortDownMove, null);
		panelSort.add(panelSortEast,  BorderLayout.EAST);
		panelSortEast.add(scpSortSortedColumns,  BorderLayout.CENTER);
		scpSortSortedColumns.getViewport().add(lstSortSortedColumns, null);
		tabbedPaneCenter.add(panelFilter,  "Filter");
		panelFilter.add(panelFilterNorth, BorderLayout.NORTH);
		panelFilterNorth.add(cmbColumns, null);
		panelFilterNorth.add(btnAddLeftBrace, null);
		panelFilterNorth.add(btnAddRightBrace, null);
		panelFilterNorth.add(txtValue1, null);
		panelFilterNorth.add(txtValue2, null);
		panelFilterNorth.add(cmbOperator, null);
		panelFilterNorth.add(btnAddtoFilter, null);
		panelFilterNorth.add(btnAndIntoFilter, null);
		panelFilterNorth.add(btnOrIntoFilter, null);
		panelFilter.add(panelFilterCenter,  BorderLayout.CENTER);
		panelFilterCenter.add(scpFilterCenter, BorderLayout.CENTER);
		scpFilterCenter.getViewport().add(txtFilterScript, null);
		panelTop.add(btnOK, null);
		panelTop.add(btnCancel, null);
		panelTop.add(btnClearFilter, null);
		panelTop.add(btnClearSort, null);
		panelSortWest.add(scpSortOrgColumns,  BorderLayout.CENTER);
		scpSortOrgColumns.getViewport().add(lstSortOrgColumns, null);
		lstSortOrgColumns.setModel(lstSortOrgColumnsModel);
		lstSortOrgColumns.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstSortOrgColumns.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 1) {
					addSortColumn("ASC");
				}
			}
		});
		lstSortSortedColumns.setModel(lstSortSortedColumnsModel);
		lstSortSortedColumns.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void setVisible(boolean b) {
		if (b) {
			initLocation(Main.getMDIMain());
		}
		super.setVisible(b);
	}


	/**
	 * init the default values
	 * and the show lists
	 */
	public void initResources(Vector columns, String tableName) {
		this.tableName = tableName;
		this.orginalVector = columns;
		String connURL = Main.getMDIMain().currentConnURL;

		// init sorted columns
		Vector orgSort = FilterSortManager.getSortVector(connURL, tableName);
		if (orgSort != null) {
			orgSort = (Vector) orgSort.clone();
		}
		lstSortSortedColumnsModel.resetData(orgSort);

		// init orginal columns
		Vector orgColumns = (Vector) columns.clone();
		if (orgSort != null) {
			for (int i = 0; i < orgSort.size(); i++) {
				String sortedColumn = getColumnNameFromSortValue((String) orgSort.get(i));
				orgColumns.remove(sortedColumn);
			}
		}
		lstSortOrgColumnsModel.resetData(orgColumns);

		// filter columns combobox init
		cmbColumns.addItem("");
		for (int i = 0; i < columns.size(); i++) {
			cmbColumns.addItem(columns.get(i));
		}

		// operators init
		String[] operators = {"=", "<>", ">", ">=", "<", "<=",
							"LIKE", "BETWEEN", "IS NOT NULL", "IS NULL"};
		cmbOperator.addItem("");
		for (int i = 0; i < operators.length; i++) {
			cmbOperator.addItem(operators[i]);
		}

		// init filter value
		String orgFilter = FilterSortManager.getFilter(connURL, tableName);
		txtFilterScript.appendString(StringUtil.nvl(orgFilter));
	}

	/**
	 * update filter and sort from gui
	 */
	void updateFilterSort() {
		Vector sortData = lstSortSortedColumnsModel.getData();
		String filter = txtFilterScript.getText();
		String connURL = Main.getMDIMain().currentConnURL;

		if (sortData != null) {
			sortData = (Vector) sortData.clone();
		}
		FilterSortManager.setSort(connURL, tableName, sortData);

		if (filter != null) {
			filter = filter.trim();
		}
		FilterSortManager.setFilter(connURL, tableName, filter);
	}

	/**
	 * close the dialog
	 */
	void btnCancel_actionPerformed(ActionEvent e) {
		dispose();
	}

	/**
	 * ok button click
	 */
	void btnOK_actionPerformed(ActionEvent e) {
		updateFilterSort();
		dispose();
		fireParamTransferEvent(null, PJConst.WINDOW_CONFIGFILTERSORT);
	}

	/**
	 * clear sort performed
	 */
	void btnClearSort_actionPerformed(ActionEvent e) {
		lstSortSortedColumnsModel.clear();
		lstSortOrgColumnsModel.resetData((Vector) orginalVector.clone());
		lstSortSortedColumns.repaint();
		lstSortOrgColumns.repaint();
	}

	/**
	 * clear filter performed
	 */
	void btnClearFilter_actionPerformed(ActionEvent e) {
		txtFilterScript.setText("");
	}

	/**
	 * add left brace "("
	 */
	void btnAddLeftBrace_actionPerformed(ActionEvent e) {
		addText("(", false);
	}

	/**
	 * add right brace ")"
	 */
	void btnAddRightBrace_actionPerformed(ActionEvent e) {
		addText(")", false);
	}

	/**
	 * add to filter
	 */
	void btnAddtoFilter_actionPerformed(ActionEvent e) {
		String condition = getCondition();
		if (condition.length() > 0) {
			if (txtFilterScript.getText().trim().length() > 0) {
				addText("AND " + condition + " ", true);
			} else {
				addText(condition + " ", true);
			}
		}
	}

	/**
	 * and add to filter
	 */
	void btnAndIntoFilter_actionPerformed(ActionEvent e) {
		String condition = getCondition();
		if (condition.length() > 0) {
			addText("AND " + condition + " ", true);
		}
	}

	/**
	 * or add to filter
	 */
	void btnOrIntoFilter_actionPerformed(ActionEvent e) {
		String condition = getCondition();
		if (condition.length() > 0) {
			addText("OR " + condition + " ", true);
		}
	}

	/**
	 * concat the '
	 */
	String getEscapedValue(String value) {
		if (value == null || value.equals("")) {
			return value;
		}

		try {
			int intValue = Integer.parseInt(value);
			return value;
		} catch (Exception e) {}

		if (!value.startsWith("'")) {
			value = "'" + value;
		}
		if (!value.endsWith("'")) {
			value = value + "'";
		}

		return value;
	}

	/**
	 * concat the condition from the gui
	 */
	String getCondition() {
		String columnName = StringUtil.nvl((String) cmbColumns.getSelectedItem());
		String operatorValue = StringUtil.nvl((String) cmbOperator.getSelectedItem());
		String value1 = getEscapedValue(txtValue1.getText());
		String value2 = getEscapedValue(txtValue2.getText());

		StringBuffer condition = new StringBuffer();
		condition.append(columnName);
		condition.append(" ");
		if ("BETWEEN".equalsIgnoreCase(operatorValue)) {
			condition.append(operatorValue);
			condition.append(" ");
			condition.append(value1);
			condition.append(" AND ");
			condition.append(value2);
		} else if ("IS NOT NULL".equalsIgnoreCase(operatorValue)) {
			condition.append(operatorValue);
		} else if ("IS NULL".equalsIgnoreCase(operatorValue)) {
			condition.append(operatorValue);
		} else {
			condition.append(operatorValue);
			condition.append(" ");
			condition.append(value1);
		}

		return condition.toString().trim();
	}

	/**
	 * add specified text into text pane
	 */
	void addText(String text, boolean wrap) {
		int caretPos = txtFilterScript.getText().length();
		text = StringUtil.nvl(text);

		if (wrap) {
			if (txtFilterScript.getText().trim().equals("")) {
				txtFilterScript.appendString(text);
			} else if (txtFilterScript.getText().trim().endsWith("\n")) {
				txtFilterScript.appendString(text);
			} else {
				txtFilterScript.appendString("\n" + text);
			}
		} else {
			txtFilterScript.appendString(text);
		}
	}

	/**
	 * remove the added sort column
	 */
	void btnRemoveSort_actionPerformed(ActionEvent e) {
		String selectColumn = (String) lstSortSortedColumns.getSelectedValue();

		if (selectColumn != null && !selectColumn.equals("")) {
			String orgColumn = getColumnNameFromSortValue(selectColumn);
			if (orgColumn == null) {
				return;
			}

			lstSortSortedColumnsModel.removeElement(selectColumn);

			int insertIndex = getOrgColumnIndex(orgColumn);

			if (insertIndex >= 0) {
				lstSortOrgColumnsModel.insertElement(insertIndex, orgColumn);
			} else {
				lstSortOrgColumnsModel.addElement(orgColumn);
			}

			lstSortSortedColumns.repaint();
			lstSortOrgColumns.repaint();
		}
	}

	/**
	 * get column name from sort value
	 */
	String getColumnNameFromSortValue(String sortValue) {
		int sep = sortValue.lastIndexOf(" ");
		if (sep < 0) {
			return null;
		}

		return sortValue.substring(0, sep);
	}

	/**
	 * get orginal column index
	 */
	int getOrgColumnIndex(String orgColumn) {
		int orgColumnIndex = orginalVector.indexOf(orgColumn);

		for (int i = orgColumnIndex + 1; i < orginalVector.size(); i++) {
			int insertIndex = lstSortOrgColumnsModel.indexOf(orginalVector.get(i));
			if (insertIndex >= 0) {
				return insertIndex;
			}
		}

		return -1;
	}

	/**
	 * add asc sort
	 */
	void btnSortASCAdd_actionPerformed(ActionEvent e) {
		addSortColumn("ASC");
	}

	/**
	 * add desc sort
	 */
	void btnSortDESCAdd_actionPerformed(ActionEvent e) {
		addSortColumn("DESC");
	}

	/**
	 * add asc or desc sort
	 */
	void addSortColumn(String sortDirection) {
		String selectColumn = (String) lstSortOrgColumns.getSelectedValue();
		if (selectColumn != null) {
			lstSortSortedColumnsModel.addElement(selectColumn + " " + sortDirection);
			lstSortOrgColumnsModel.removeElement(selectColumn);
			lstSortSortedColumns.repaint();
			lstSortOrgColumns.repaint();
		}
	}

	/**
	 * move the selected sort value up
	 */
	void btnSortUpMove_actionPerformed(ActionEvent e) {
		int selectIndex = lstSortSortedColumns.getSelectedIndex();
		Object selectColumn = lstSortSortedColumns.getSelectedValue();

		if (selectIndex >= 0) {
			lstSortSortedColumnsModel.moveUp(selectIndex);
			lstSortSortedColumns.setSelectedValue(selectColumn, true);
			lstSortSortedColumns.repaint();
		}
	}

	/**
	 * move the selected sort value down
	 */
	void btnSortDownMove_actionPerformed(ActionEvent e) {
		int selectIndex = lstSortSortedColumns.getSelectedIndex();
		Object selectColumn = lstSortSortedColumns.getSelectedValue();

		if (selectIndex >= 0) {
			lstSortSortedColumnsModel.moveDown(selectIndex);
			lstSortSortedColumns.setSelectedValue(selectColumn, true);
			lstSortSortedColumns.repaint();
		}
	}

}