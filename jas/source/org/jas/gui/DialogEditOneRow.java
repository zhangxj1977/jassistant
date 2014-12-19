package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.jas.base.PJDialogBase;
import org.jas.base.PJEditorTextField;
import org.jas.base.RollOverButton;
import org.jas.common.PJConst;
import org.jas.util.ImageManager;
import org.jas.util.StringUtil;

/**
 * edit one record dialog
 *
 * @author ’£@ŠwŒR
 * @version 1.0
 */
public class DialogEditOneRow extends PJDialogBase {
	ImageIcon iconRefreshRecord = ImageManager.createImageIcon("refreshtable.gif");
	int tableType = PJConst.TABLE_TYPE_TABLE;
	Vector nameVector = null;
	Vector commentVector = null;
	Vector rowData = null;
	Vector typeVector = null;
	Vector sizeVector = null;
	Vector keyVector = null;
	Vector editorVector = null;
	int row = -1;

	JPanel panelMain = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();
	JPanel panelTop = new JPanel();
	JPanel panelCenter = new JPanel();
	JPanel panelBottom = new JPanel();
	BorderLayout borderLayout3 = new BorderLayout();
	Border border1;
	JToolBar toolBarTop = new JToolBar();
	JScrollPane scpMain = new JScrollPane();
	BorderLayout borderLayout4 = new BorderLayout();
	Border border2;
	JToolBar toolBarBottom = new JToolBar();
	BorderLayout borderLayout5 = new BorderLayout();
	RollOverButton btnRefresh = new RollOverButton();
	JButton btnOK = new JButton();
	JButton btnCancel = new JButton();
	Component horizontalGlue = Box.createHorizontalGlue();
	JPanel panelEditMain = new JPanel();
	GridLayout gridLayoutMain = new GridLayout();

	public DialogEditOneRow(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogEditOneRow() {
		this(Main.getMDIMain(), "Edit Row", true);
	}

	void jbInit() throws Exception {
		border1 = BorderFactory.createEmptyBorder(2, 0, 2, 0);
		border2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(148, 145, 140),new Color(103, 101, 98));
		panelMain.setLayout(borderLayout1);
		this.setModal(true);
		this.getContentPane().setLayout(borderLayout2);
		panelTop.setBorder(border1);
		panelTop.setLayout(borderLayout3);
		panelCenter.setLayout(borderLayout4);
		scpMain.setBorder(border2);
		panelBottom.setLayout(borderLayout5);
		toolBarTop.setFloatable(false);
		toolBarBottom.setFloatable(false);
		btnRefresh.setMaximumSize(new Dimension(27, 27));
		btnRefresh.setMinimumSize(new Dimension(27, 27));
		btnRefresh.setPreferredSize(new Dimension(32, 32));
		btnRefresh.setToolTipText("Refresh Current Record Data");
		btnRefresh.setMargin(new Insets(0, 0, 0, 0));
		btnRefresh.setVerifyInputWhenFocusTarget(false);
		btnRefresh.setIcon(iconRefreshRecord);
		btnRefresh.setVerifyInputWhenFocusTarget(false);
		btnOK.setText("OK");
		btnOK.setMnemonic('O');
		btnOK.setMaximumSize(new Dimension(45, 22));
		btnOK.setMinimumSize(new Dimension(45, 22));
		btnOK.setPreferredSize(new Dimension(45, 22));
		btnOK.setMargin(new Insets(0, 0, 0, 0));
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancel_actionPerformed(e);
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.setMaximumSize(new Dimension(45, 22));
		btnCancel.setMinimumSize(new Dimension(45, 22));
		btnCancel.setPreferredSize(new Dimension(45, 22));
		btnCancel.setMargin(new Insets(0, 0, 0, 0));
		btnCancel.setVerifyInputWhenFocusTarget(false);
		getContentPane().add(panelMain, BorderLayout.CENTER);
		panelMain.add(panelTop,  BorderLayout.NORTH);
		panelTop.add(toolBarTop, BorderLayout.CENTER);
		toolBarTop.add(btnRefresh, null);
		panelMain.add(panelCenter, BorderLayout.CENTER);
		panelCenter.add(scpMain, BorderLayout.CENTER);
		scpMain.getViewport().add(panelEditMain, null);
		panelEditMain.setLayout(gridLayoutMain);
		gridLayoutMain.setHgap(2);
		gridLayoutMain.setVgap(2);
		panelMain.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.add(toolBarBottom, BorderLayout.CENTER);
		toolBarBottom.add(btnOK, null);
		toolBarBottom.add(horizontalGlue, null);
		toolBarBottom.add(btnCancel, null);
		btnRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRefresh_actionPerformed(e);
			}
		});
	}

	/**
	 * override setVisible
	 */
	public void setVisible(boolean b) {
		if (b) {
			pack();
			initLocation(Main.getMDIMain());
		}
		super.setVisible(b);
	}

	/**
	 * reset the ui data
	 */
	void refreshData() {
		int columnCounts = nameVector.size();
		editorVector = new Vector(columnCounts);

		gridLayoutMain.setColumns(2);
		gridLayoutMain.setRows(columnCounts);

		int viewHeight = columnCounts * 20;
		int totalHeight = viewHeight;
		if (viewHeight > 550) {
			viewHeight = 550;
		}

		int labelNameWidth = 0;
		int valueSize = 0;
		for (int i = 0; i < columnCounts; i++) {
			String columnName = (String) nameVector.get(i);
			String comment = (String) commentVector.get(i);
			if (comment != null && !comment.equals(columnName)) {
				columnName = columnName + "(" + comment + ")";
			}
			Class type = (Class) typeVector.get(i);

			int columnNameWidth = columnName.getBytes().length * 8 + 8;

			if (columnNameWidth > labelNameWidth) {
				labelNameWidth = columnNameWidth;
			}

			int columnValueSize = 0;
			if (type == java.sql.Timestamp.class
					|| type == java.sql.Date.class) {
				columnValueSize = 15 * 8 + 8;
			} else {
				columnValueSize = ((Integer) sizeVector.get(i)).intValue() * 8 + 8;
			}
			if (columnValueSize > valueSize) {
				valueSize = columnValueSize;
			}
		}

		if (valueSize > 500) {
			valueSize = 500;
		}

		panelCenter.setMinimumSize(new Dimension(labelNameWidth + valueSize + 20, viewHeight + 20));
		panelCenter.setPreferredSize(new Dimension(labelNameWidth + valueSize + 20, viewHeight + 20));
		panelEditMain.setPreferredSize(new Dimension(labelNameWidth + valueSize, totalHeight));
		panelEditMain.setMinimumSize(new Dimension(labelNameWidth + valueSize, totalHeight));

		for (int i = 0; i < columnCounts; i++) {
			String columnName = (String) nameVector.get(i);
			String comment = (String) commentVector.get(i);
			if (comment != null && !comment.equals(columnName)) {
				columnName = columnName + "(" + comment + ")";
			}
			Class type = (Class) typeVector.get(i);
			Object value = rowData.get(i);
			int columnValueSize = 0;
			if (type == java.sql.Timestamp.class
					|| type == java.sql.Date.class) {
				columnValueSize = 15 * 8 + 8;
			} else {
				columnValueSize = ((Integer) sizeVector.get(i)).intValue() * 8 + 8;
			}
			if (columnValueSize > 500) {
				columnValueSize = 500;
			}


			JPanel oneColumnPanel = new JPanel(new BorderLayout());

			JLabel lblName = new JLabel();
			lblName.setText(columnName);
			lblName.setHorizontalAlignment(SwingConstants.RIGHT);
			lblName.setPreferredSize(new Dimension(labelNameWidth, 20));
			lblName.setMaximumSize(new Dimension(labelNameWidth, 20));
			oneColumnPanel.add(lblName, BorderLayout.WEST);

			PJEditorTextField editor = new PJEditorTextField(type);
			editor.setText(StringUtil.getStringValue(type, value));
			editor.setPreferredSize(new Dimension(columnValueSize, 20));
			editor.setMaximumSize(new Dimension(columnValueSize, 20));
			editor.setMinimumSize(new Dimension(columnValueSize, 20));
			if (type == Object.class || PJConst.TABLE_TYPE_TABLE != tableType) {
				editor.setEditable(false);
			}
			oneColumnPanel.add(editor, BorderLayout.CENTER);
			editorVector.add(editor);

			JLabel lblAjust = new JLabel();
			lblAjust.setText(" ");
			lblAjust.setPreferredSize(new Dimension(valueSize - columnValueSize, 20));
			lblAjust.setMinimumSize(new Dimension(valueSize - columnValueSize, 20));
			oneColumnPanel.add(lblAjust, BorderLayout.EAST);

			panelEditMain.add(oneColumnPanel);
		}
	}

	/**
	 * update current record
	 */
	void updateRecord() {
		int columnCounts = nameVector.size();
		boolean shouldUpdate = false;

		try {
			Vector newRowData = new Vector(columnCounts + 1);
			for (int i = 0; i < columnCounts; i++) {
				Class type = (Class) typeVector.get(i);
				Object value = rowData.get(i);

				PJEditorTextField editor = (PJEditorTextField) editorVector.get(i);
				Object newValue = StringUtil.getConvertValueOfType(type, editor.getText());
				newRowData.add(newValue);
				if ((newValue == null && newValue != value)
						|| (newValue != null && !newValue.equals(value))) {
					shouldUpdate = true;
				}
			}

			// add key vector
			newRowData.add(rowData.get(columnCounts));

			if (shouldUpdate) {
				fireParamTransferEvent(new Object[]{new Integer(row), newRowData}, PJConst.WINDOW_EDITONEROW);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * init ui from data
	 */
	public void initUI(Vector nameVector, Vector commentVector, Vector typeVector,
						Vector sizeVector, Vector keyVector,
						Vector rowData, int row, int tableType) {
		this.nameVector = nameVector;
		this.commentVector = commentVector;
		this.typeVector = typeVector;
		this.sizeVector = sizeVector;
		this.keyVector = keyVector;
		this.rowData = rowData;
		this.row = row;
		this.tableType = tableType;

		if (PJConst.TABLE_TYPE_TABLE != tableType) {
			btnOK.setEnabled(false);
			setTitle("View Row");
		}

		refreshData();
	}

	/**
	 * refresh current record data
	 */
	void btnRefresh_actionPerformed(ActionEvent e) {
		int columnCounts = nameVector.size();

		for (int i = 0; i < columnCounts; i++) {
			Class type = (Class) typeVector.get(i);
			Object value = rowData.get(i);
			PJEditorTextField editor = (PJEditorTextField) editorVector.get(i);

			editor.setText(StringUtil.getStringValue(type, value));
		}
	}

	/**
	 * post edit and commit the row data.
	 */
	void btnOK_actionPerformed(ActionEvent e) {
		dispose();
		updateRecord();
	}

	/**
	 * cancel edit and discard edit
	 */
	void btnCancel_actionPerformed(ActionEvent e) {
		dispose();
	}
}
