package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import org.jas.base.PJDialogBase;
import org.jas.base.PJEditorTextField;
import org.jas.base.RollOverButton;
import org.jas.common.PJConst;
import org.jas.util.ImageManager;
import org.jas.util.UIUtil;

/**
 *
 *
 * @author 張　学軍
 * @version 1.0
 */
public class DialogEditOneCell extends PJDialogBase {
	ImageIcon iconLoadFromFile = ImageManager.createImageIcon("importfile.gif");
	ImageIcon iconSaveToFile = ImageManager.createImageIcon("exporttofile.gif");
	JPanel panelMain = new JPanel();
	BorderLayout borderLayoutMain = new BorderLayout();
	JToolBar toolBarTop = new JToolBar();
	JPanel panelCenter = new JPanel();
	JScrollPane scpAreaMain = new JScrollPane();
	BorderLayout borderLayoutEdit = new BorderLayout();
	RollOverButton btnLoadFromFile = new RollOverButton(iconLoadFromFile);
	RollOverButton btnSaveToFile = new RollOverButton(iconSaveToFile);
	JToolBar toolBarBottom = new JToolBar();
	JButton btnOK = new JButton();
	JButton btnCancel = new JButton();
	Component horizontalGlue = Box.createHorizontalGlue();
	ButtonActionListener buttonActionListener = new ButtonActionListener();
	static FileDialog fileDialog;
	JTextArea txtAreaEdit = null;
	PJEditorTextField txtFieldEdit = null;
	Class type = null;
	int row = -1;
	int col = -1;

	public DialogEditOneCell(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogEditOneCell() {
		this(Main.getMDIMain(), "", true);
	}

	void jbInit() throws Exception {
		panelMain.setLayout(borderLayoutMain);
		panelCenter.setLayout(borderLayoutEdit);
		toolBarTop.setFloatable(false);
		btnOK.setMaximumSize(new Dimension(45, 22));
		btnOK.setMinimumSize(new Dimension(45, 22));
		btnOK.setPreferredSize(new Dimension(45, 22));
		btnOK.setMargin(new Insets(0, 0, 0, 0));
		btnOK.setText("OK");
		btnOK.setMnemonic('O');
		btnOK.addActionListener(buttonActionListener);
		btnCancel.setMaximumSize(new Dimension(45, 22));
		btnCancel.setMinimumSize(new Dimension(45, 22));
		btnCancel.setPreferredSize(new Dimension(45, 22));
		btnCancel.setMargin(new Insets(0, 0, 0, 0));
		btnCancel.setText("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.addActionListener(buttonActionListener);
		btnCancel.setVerifyInputWhenFocusTarget(false);
		btnLoadFromFile.setPreferredSize(new Dimension(32, 32));
		btnLoadFromFile.setMaximumSize(new Dimension(27, 27));
		btnLoadFromFile.setMinimumSize(new Dimension(27, 27));
		btnLoadFromFile.setToolTipText("load from file");
		btnLoadFromFile.addActionListener(buttonActionListener);
		btnSaveToFile.setPreferredSize(new Dimension(32, 32));
		btnSaveToFile.setMaximumSize(new Dimension(27, 27));
		btnSaveToFile.setMinimumSize(new Dimension(27, 27));
		btnSaveToFile.setToolTipText("save to file");
		btnSaveToFile.addActionListener(buttonActionListener);
		toolBarTop.add(btnLoadFromFile, null);
		toolBarTop.add(btnSaveToFile, null);
		getContentPane().add(panelMain);
		panelMain.add(toolBarTop, BorderLayout.NORTH);
		panelMain.add(panelCenter, BorderLayout.CENTER);
		panelMain.add(toolBarBottom,  BorderLayout.SOUTH);
		toolBarBottom.setFloatable(false);
		toolBarBottom.add(btnOK, null);
		toolBarBottom.add(horizontalGlue, null);
		toolBarBottom.add(btnCancel, null);
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
	 * init default value.
	 */
	public void initResources(int row, int col,
							String columnName, Class type,
							String strValue, String beanType) {
		setTitle("Edit Column " + columnName);

		if (type == null) {
			type = Object.class;
		}

		this.type = type;
		this.row = row;
		this.col = col;

		if (type == String.class) {
			panelMain.setPreferredSize(new Dimension(350, 300));
			txtAreaEdit = new JTextArea();
			txtAreaEdit.setFont(UIUtil.getDefaultFont(UIUtil.GRID_FONT));
			panelCenter.add(scpAreaMain, BorderLayout.CENTER);
			scpAreaMain.getViewport().add(txtAreaEdit);
			txtAreaEdit.setText(strValue);
			txtAreaEdit.requestFocus();
			if (beanType.equals(PJConst.BEAN_TYPE_VIEW)) {
				txtAreaEdit.setEditable(false);
				btnOK.setEnabled(false);
			}
			btnLoadFromFile.setEnabled(false);
			btnSaveToFile.setEnabled(false);
		} else if (type == Object.class) {
			panelMain.setPreferredSize(new Dimension(300, 80));
			txtFieldEdit = new PJEditorTextField(null);
			panelCenter.add(txtFieldEdit, BorderLayout.CENTER);
			txtFieldEdit.setEditable(false);
			btnSaveToFile.setEnabled(true);
			if (beanType.equals(PJConst.BEAN_TYPE_VIEW)) {
				btnLoadFromFile.setEnabled(false);
			} else {
				btnSaveToFile.setEnabled(true);
			}
		} else {
			panelMain.setPreferredSize(new Dimension(250, 80));
			txtFieldEdit = new PJEditorTextField(type);
			panelCenter.add(txtFieldEdit, BorderLayout.CENTER);
			txtFieldEdit.setText(strValue);
			txtFieldEdit.requestFocus();
			if (beanType.equals(PJConst.BEAN_TYPE_VIEW)) {
				txtFieldEdit.setEditable(false);
				btnOK.setEnabled(false);
			}
			btnLoadFromFile.setEnabled(false);
			btnSaveToFile.setEnabled(false);
		}
	}

	/**
	 * when ok button clicked, process column value update
	 */
	void updateColumnValue() {
		if (type == String.class) {
			String areaText = txtAreaEdit.getText();
			fireParamTransferEvent(new Object[]{String.valueOf(row), String.valueOf(col), areaText, type}, PJConst.WINDOW_EDITONECOLUMN);
		} else if (type == Object.class) {
			String opText = txtFieldEdit.getText();
			if (opText != null && !opText.equals("")) {
				fireParamTransferEvent(new Object[]{String.valueOf(row), String.valueOf(col), opText, type}, PJConst.WINDOW_EDITONECOLUMN);
			}
		} else {
			String fieldText = txtFieldEdit.getText();
			fireParamTransferEvent(new Object[]{String.valueOf(row), String.valueOf(col), fieldText, type}, PJConst.WINDOW_EDITONECOLUMN);
		}
	}

	/**
	 * load from file
	 */
	void loadFromFile() {
		if (fileDialog == null) {
			fileDialog = new FileDialog(Main.getMDIMain());
		}
		fileDialog.setMode(FileDialog.LOAD);
		fileDialog.show();
		String file = fileDialog.getFile();
		if (file == null) {
			return;
		}
		String dir = fileDialog.getDirectory();

		txtFieldEdit.setText("Load from: " + (dir + file));
	}

	/**
	 * save to file
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
		String dir = fileDialog.getDirectory();

		txtFieldEdit.setText("Save to: " + (dir + file));
	}


	/*************************************************************************************
	/* 事件処理用エリア
	/************************************************************************************/
	class ButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();

			if (obj == btnLoadFromFile) {
				loadFromFile();
			} else if (obj ==  btnSaveToFile) {
				saveToFile();
			} else if (obj == btnCancel) {
				dispose();
			} else if (obj == btnOK) {
				updateColumnValue();
				dispose();
			}
		}
	}

}