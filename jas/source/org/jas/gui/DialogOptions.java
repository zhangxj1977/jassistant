package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jas.base.PJDialogBase;
import org.jas.base.PJEditableComboBox;
import org.jas.base.PJListModel;
import org.jas.base.PJTableCellRender;
import org.jas.common.PJConst;
import org.jas.common.ParamTransferEvent;
import org.jas.common.ParamTransferListener;
import org.jas.util.MessageManager;
import org.jas.util.PropertyManager;
import org.jas.util.ResourceManager;
import org.jas.util.StringUtil;
import org.jas.util.UIUtil;

/**
 *
 *
 *
 *
 * @author 張　学軍
 * @version 1.0
 */
public class DialogOptions extends PJDialogBase implements ParamTransferListener {
	JPanel panelMain = new JPanel();
	BorderLayout mainBorderLayout = new BorderLayout();
	JPanel panelTop = new JPanel();
	JPanel panelCenter = new JPanel();
	JPanel panelBottom = new JPanel();
	BorderLayout centerBorderLayout = new BorderLayout();
	JTabbedPane tabbedPaneOptions = new JTabbedPane();
	JPanel subTabPanelDataBean = new JPanel();
	JPanel subTabPanelGeneral = new JPanel();
	JPanel subTabPanelDataBase = new JPanel();
	JPanel subTabPanelClassPath = new JPanel();
	JPanel subTabPanelView = new JPanel();
	JScrollPane scpClassPathListLeft = new JScrollPane();
	JList listClassPathListLeft = new JList();
	PJListModel listClassPathListLeftModel = new PJListModel();
	JPanel panelClassPathRight = new JPanel();
	JButton btnAddClassPath = new JButton();
	JButton btnRemoveClassPath = new JButton();
	GridLayout bottomBridLayout = new GridLayout();
	JPanel bottomLeftPanel = new JPanel();
	JPanel bottomRightPanel = new JPanel();
	GridLayout bottomRightGridLayout = new GridLayout();
	JButton btnOK = new JButton();
	JButton btnSave = new JButton();
	JButton bntCancel = new JButton();
	GridLayout panelDataBeanGridLayout = new GridLayout();
	JPanel panelDataBeanUp = new JPanel();
	JPanel panelDataBeanDown = new JPanel();
	TitledBorder titledBorder1;
	TitledBorder titledBorder2;
	GridLayout gridLayout1 = new GridLayout();
	JPanel panelDataBeanUpRight = new JPanel();
	JPanel panelDataBeanUpLeft = new JPanel();
	ButtonGroup btnGroupColumnName = new ButtonGroup();
	JRadioButton rdoDataBeanNameNo = new JRadioButton();
	JRadioButton rdoDataBeanNameLowcase = new JRadioButton();
	JRadioButton rdoDataBeanNameUpCase = new JRadioButton();
	TitledBorder titledBorder3;
	GridLayout gridLayout2 = new GridLayout();
	TitledBorder titledBorder4;
	GridLayout gridLayout3 = new GridLayout();
	JCheckBox chkSetter = new JCheckBox();
	JCheckBox chkGetter = new JCheckBox();
	JCheckBox chkToString = new JCheckBox();
	GridLayout gridLayout4 = new GridLayout();
	JPanel panelDataBeanDownRight = new JPanel();
	JPanel panelDataBeanDownLeft = new JPanel();
	TitledBorder titledBorder5;
	GridLayout gridLayout5 = new GridLayout();
	JRadioButton rdoCommentDefaultNull = new JRadioButton();
	JRadioButton rdoCommentDefaultName = new JRadioButton();
	JCheckBox chkCommentNameIfNull = new JCheckBox();
	ButtonGroup btnGroupComment = new ButtonGroup();
	GridLayout gridLayout6 = new GridLayout();
	JPanel panelGeneralTop = new JPanel();
	JPanel panelGeneralBottom = new JPanel();
	TitledBorder titledBorder6;
	GridLayout gridLayout7 = new GridLayout();
	TitledBorder titledBorder7;
	JCheckBox chkSerialize = new JCheckBox();
	JCheckBox chkDefaultConstructor = new JCheckBox();
	GridLayout gridLayout8 = new GridLayout();
	JPanel panelGeneralRight = new JPanel();
	JPanel panelGeneralTopLeft = new JPanel();
	GridLayout gridLayout9 = new GridLayout();
	JLabel lblUserName = new JLabel();
	JTextField txtAuthorName = new JTextField();
	JLabel lblVersion = new JLabel();
	JTextField txtVersion = new JTextField();
	GridLayout gridLayoutViewMain = new GridLayout();
	JPanel panelViewFont = new JPanel();
	JPanel panelView_1 = new JPanel();
	JLabel lblViewGridFont = new JLabel();
	JLabel lblViewGridFontName = new JLabel();
	JButton btnViewGridFontBrowse = new JButton();
	BorderLayout borderLayoutPanelView_1 = new BorderLayout();
	JLabel lblViewSQLFont = new JLabel();
	JLabel lblViewSQLSciptFontName = new JLabel();
	JButton btnViewSQLFontBrowse = new JButton();
	BorderLayout borderLayoutPanelView_2 = new BorderLayout();
	JPanel panelView_2 = new JPanel();
	TitledBorder titledBorder8;
	GridLayout gridLayout10 = new GridLayout();
	GridLayout gridLayoutDataBase = new GridLayout();
	JPanel panelDataBaseData = new JPanel();
	BorderLayout borderLayoutPanelClassPath = new BorderLayout();
	TitledBorder titledBorder9;
	JPanel panelDataBaseEncode = new JPanel();
	TitledBorder titledBorder10;
	JPanel jPanel4 = new JPanel();
	JLabel lblDataBaseFetchSize = new JLabel();
	JTextField txtDataBaseFetchSize = new JTextField();
	JCheckBox chkEncodeEnabled = new JCheckBox();
	PJEditableComboBox cmbDBEncode = new PJEditableComboBox();
	PJEditableComboBox cmbGridEncode = new PJEditableComboBox();
	JLabel lblDBEncode = new JLabel();
	JLabel lblGridEncode = new JLabel();
	JLabel jLabel1 = new JLabel();
	static FileDialog fileDialog;

	public DialogOptions(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			initResource();
			updateUIOptions();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogOptions() {
		this(Main.getMDIMain(), "Options", true);
	}

	public void setVisible(boolean b) {
		if (b) {
			initLocation(Main.getMDIMain());
		}
		super.setVisible(b);
	}

	void jbInit() throws Exception {
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Field");
		titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Others");
		titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Field Case");
		titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Bean Method");
		titledBorder5 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Comment");
		titledBorder6 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"User Info");
		titledBorder7 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Bean Info");
		titledBorder8 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Font");
		titledBorder9 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Data");
		titledBorder10 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Encode Convert");
		setSize(new Dimension(350, 350));
		panelMain.setLayout(mainBorderLayout);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setTitle("Options");
		mainBorderLayout.setVgap(5);
		panelCenter.setLayout(centerBorderLayout);
		panelCenter.setPreferredSize(new Dimension(350, 300));
		panelBottom.setPreferredSize(new Dimension(350, 25));
		panelBottom.setLayout(bottomBridLayout);
		bottomBridLayout.setColumns(3);
		bottomBridLayout.setHgap(10);
		bottomRightPanel.setLayout(bottomRightGridLayout);
		btnOK.setMargin(new Insets(0, 0, 0, 0));
		btnOK.setText("OK");
		btnOK.setMnemonic('O');
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});
		btnSave.setMargin(new Insets(0, 0, 0, 0));
		btnSave.setText("Apply");
		btnSave.setMnemonic('A');
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSave_actionPerformed(e);
			}
		});
		bottomRightGridLayout.setColumns(3);
		bottomRightGridLayout.setHgap(5);
		bntCancel.setMargin(new Insets(0, 0, 0, 0));
		bntCancel.setText("Cancel");
		bntCancel.setMnemonic('C');
		bntCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bntCancel_actionPerformed(e);
			}
		});
		subTabPanelDataBean.setLayout(panelDataBeanGridLayout);
		panelDataBeanGridLayout.setRows(2);
		panelDataBeanGridLayout.setColumns(1);
		panelDataBeanGridLayout.setVgap(5);
		panelDataBeanUp.setBorder(titledBorder1);
		panelDataBeanUp.setLayout(gridLayout1);
		panelDataBeanDown.setBorder(titledBorder2);
		panelDataBeanDown.setLayout(gridLayout4);
		gridLayout1.setColumns(2);
		gridLayout1.setHgap(10);
		rdoDataBeanNameNo.setSelected(true);
		rdoDataBeanNameNo.setText("Original Name");
		rdoDataBeanNameLowcase.setText("To Lowcase");
		rdoDataBeanNameUpCase.setText("To Up Case");
		panelDataBeanUpRight.setBorder(titledBorder4);
		panelDataBeanUpRight.setLayout(gridLayout3);
		gridLayout3.setRows(3);
		gridLayout3.setColumns(1);
		gridLayout3.setVgap(3);
		chkSetter.setText("Setter");
		chkGetter.setText("Getter");
		chkToString.setText("toString");
		gridLayout4.setColumns(2);
		gridLayout4.setHgap(10);
		panelDataBeanDownLeft.setBorder(titledBorder5);
		panelDataBeanDownLeft.setLayout(gridLayout5);
		gridLayout5.setRows(3);
		gridLayout5.setColumns(1);
		gridLayout5.setVgap(3);
		rdoCommentDefaultNull.setSelected(true);
		rdoCommentDefaultNull.setText("Default Null");
		rdoCommentDefaultName.setText("Default Column Name");
		subTabPanelGeneral.setLayout(gridLayout6);
		gridLayout6.setRows(2);
		gridLayout6.setColumns(1);
		gridLayout6.setVgap(5);
		panelGeneralTop.setBorder(titledBorder6);
		panelGeneralTop.setLayout(gridLayout8);
		panelDataBeanDownRight.setLayout(gridLayout7);
		panelDataBeanDownRight.setBorder(titledBorder7);
		gridLayout7.setRows(3);
		gridLayout7.setColumns(1);
		gridLayout7.setVgap(3);
		chkSerialize.setText("Serializable");
		chkDefaultConstructor.setText("Default Constructor");
		gridLayout8.setColumns(2);
		gridLayout8.setHgap(10);
		panelGeneralTopLeft.setLayout(gridLayout9);
		gridLayout9.setRows(4);
		gridLayout9.setColumns(3);
		gridLayout9.setVgap(3);
		lblUserName.setText("Bean Author:");
		lblVersion.setText("Bean Version:");
		subTabPanelView.setLayout(gridLayoutViewMain);
		gridLayoutViewMain.setRows(3);
		gridLayoutViewMain.setColumns(1);
		gridLayoutViewMain.setVgap(10);
		panelView_1.setLayout(borderLayoutPanelView_1);
		lblViewGridFont.setText("Grid Font:     ");
		lblViewGridFontName.setBorder(BorderFactory.createEtchedBorder());
		btnViewGridFontBrowse.setText("Browse");
		btnViewGridFontBrowse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnViewGridFontBrowse_actionPerformed(e);
			}
		});
		borderLayoutPanelView_1.setHgap(5);
		panelViewFont.setLayout(gridLayout10);
		lblViewSQLFont.setText("SQL Font:     ");
		lblViewSQLSciptFontName.setBorder(BorderFactory.createEtchedBorder());
		btnViewSQLFontBrowse.setText("Browse");
		btnViewSQLFontBrowse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnViewSQLFontBrowse_actionPerformed(e);
			}
		});
		borderLayoutPanelView_2.setHgap(5);
		panelView_2.setLayout(borderLayoutPanelView_2);
		panelViewFont.setBorder(titledBorder8);
		gridLayout10.setRows(2);
		gridLayout10.setColumns(1);
		gridLayout10.setVgap(2);
		subTabPanelDataBase.setLayout(gridLayoutDataBase);
		gridLayoutDataBase.setRows(4);
		panelDataBaseData.setBorder(titledBorder9);
		panelDataBaseData.setLayout(null);
		panelDataBaseEncode.setBorder(titledBorder10);
		panelDataBaseEncode.setLayout(null);
		jPanel4.setLayout(null);
		jPanel4.setBounds(new Rectangle(6, 22, 333, 41));
		lblDataBaseFetchSize.setText("Fetch Buffer Size:");
		lblDataBaseFetchSize.setBounds(new Rectangle(5, 8, 102, 20));
		txtDataBaseFetchSize.setText("300");
		txtDataBaseFetchSize.setBounds(new Rectangle(105, 6, 50, 23));
		chkEncodeEnabled.setText("Enabled");
		chkEncodeEnabled.setBounds(new Rectangle(13, 36, 71, 25));
		chkEncodeEnabled.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				chkEncodeEnabled_StateChanged(e);
			}
		});
		cmbDBEncode.setBounds(new Rectangle(112, 39, 88, 20));
		cmbDBEncode.setEditable(true);
		cmbGridEncode.setBounds(new Rectangle(215, 39, 88, 21));
		cmbGridEncode.setEditable(true);
		lblDBEncode.setText("DB Encode:");
		lblDBEncode.setBounds(new Rectangle(112, 17, 86, 19));
		lblGridEncode.setBounds(new Rectangle(215, 17, 80, 19));
		lblGridEncode.setText("Grid Encode:");
		jLabel1.setText("(150~600)");
		jLabel1.setBounds(new Rectangle(165, 7, 63, 23));
		btnGroupComment.add(rdoCommentDefaultNull);
		btnGroupComment.add(rdoCommentDefaultName);
		chkCommentNameIfNull.setSelected(true);
		chkCommentNameIfNull.setText("Use Name If Null");
		btnGroupColumnName.add(rdoDataBeanNameNo);
		btnGroupColumnName.add(rdoDataBeanNameLowcase);
		btnGroupColumnName.add(rdoDataBeanNameUpCase);
		panelDataBeanUpLeft.setBorder(titledBorder3);
		panelDataBeanUpLeft.setLayout(gridLayout2);
		gridLayout2.setRows(3);
		gridLayout2.setColumns(1);
		scpClassPathListLeft.setPreferredSize(new Dimension(240, 300));
		scpClassPathListLeft.setMinimumSize(new Dimension(240, 300));
		scpClassPathListLeft.getViewport().add(listClassPathListLeft);
		listClassPathListLeft.setModel(listClassPathListLeftModel);
		listClassPathListLeft.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listClassPathListLeft.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				setRemoveButtonStatus();
			}
		});
		panelClassPathRight.setPreferredSize(new Dimension(60, 300));
		panelClassPathRight.setMinimumSize(new Dimension(60, 300));
		panelClassPathRight.setLayout(null);
		subTabPanelClassPath.setLayout(borderLayoutPanelClassPath);
		subTabPanelClassPath.add(scpClassPathListLeft, BorderLayout.CENTER);
		subTabPanelClassPath.add(panelClassPathRight, BorderLayout.EAST);
		subTabPanelClassPath.setBorder(new EmptyBorder(3, 3, 3, 3));
		borderLayoutPanelClassPath.setHgap(5);
		btnAddClassPath.setText("Add");
		btnAddClassPath.setMargin(new Insets(0, 0, 0, 0));
		btnAddClassPath.setBounds(5, 20, 50, 20);
		btnAddClassPath.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddClassPath_actionPerformed(e);
			}
		});
		btnRemoveClassPath.setText("Remove");
		btnRemoveClassPath.setMargin(new Insets(0, 0, 0, 0));
		btnRemoveClassPath.setBounds(5, 50, 50, 20);
		btnRemoveClassPath.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemoveClassPath_actionPerformed(e);
			}
		});
		panelClassPathRight.add(btnAddClassPath);
		panelClassPathRight.add(btnRemoveClassPath);
		getContentPane().add(panelMain, BorderLayout.CENTER);
		panelMain.add(panelTop,  BorderLayout.NORTH);
		panelMain.add(panelCenter, BorderLayout.CENTER);
		panelCenter.add(tabbedPaneOptions, BorderLayout.CENTER);
		panelMain.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.add(bottomLeftPanel, null);
		panelBottom.add(bottomRightPanel, null);
		bottomRightPanel.add(btnOK, null);
		bottomRightPanel.add(btnSave, null);
		bottomRightPanel.add(bntCancel, null);
		tabbedPaneOptions.add(subTabPanelDataBean,  "Bean");
		tabbedPaneOptions.add(subTabPanelDataBase,      "DataBase");
		tabbedPaneOptions.add(subTabPanelView,  "View");
		tabbedPaneOptions.add(subTabPanelClassPath,  "JDBCPackage");
		tabbedPaneOptions.add(subTabPanelGeneral,  "General");
		subTabPanelDataBean.add(panelDataBeanUp, null);
		subTabPanelDataBean.add(panelDataBeanDown, null);
		panelDataBeanDown.add(panelDataBeanDownLeft, null);
		panelDataBeanDown.add(panelDataBeanDownRight, null);
		panelDataBeanDownRight.add(chkSerialize, null);
		panelDataBeanDownRight.add(chkDefaultConstructor, null);
		panelDataBeanUp.add(panelDataBeanUpLeft, null);
		panelDataBeanUp.add(panelDataBeanUpRight, null);
		panelDataBeanUpRight.add(chkGetter, null);
		panelDataBeanUpRight.add(chkSetter, null);
		panelDataBeanUpLeft.add(rdoDataBeanNameNo, null);
		panelDataBeanUpLeft.add(rdoDataBeanNameLowcase, null);
		panelDataBeanUpLeft.add(rdoDataBeanNameUpCase, null);
		panelDataBeanUpRight.add(chkToString, null);
		panelDataBeanDownLeft.add(rdoCommentDefaultNull, null);
		panelDataBeanDownLeft.add(rdoCommentDefaultName, null);
		panelDataBeanDownLeft.add(chkCommentNameIfNull, null);
		subTabPanelGeneral.add(panelGeneralTop, null);
		panelGeneralTop.add(panelGeneralTopLeft, null);
		panelGeneralTop.add(panelGeneralRight, null);
		subTabPanelGeneral.add(panelGeneralBottom, null);
		panelGeneralTopLeft.add(lblUserName, null);
		panelGeneralTopLeft.add(txtAuthorName, null);
		panelGeneralTopLeft.add(lblVersion, null);
		panelGeneralTopLeft.add(txtVersion, null);
		subTabPanelView.add(panelViewFont, null);
		panelView_1.add(lblViewGridFont, BorderLayout.WEST);
		panelView_1.add(lblViewGridFontName, BorderLayout.CENTER);
		panelView_1.add(btnViewGridFontBrowse, BorderLayout.EAST);
		panelViewFont.add(panelView_2, null);
		panelView_2.add(lblViewSQLFont, BorderLayout.WEST);
		panelView_2.add(lblViewSQLSciptFontName, BorderLayout.CENTER);
		panelView_2.add(btnViewSQLFontBrowse, BorderLayout.EAST);
		panelViewFont.add(panelView_1, null);
		subTabPanelDataBase.add(panelDataBaseData, null);
		panelDataBaseData.add(jPanel4, null);
		jPanel4.add(txtDataBaseFetchSize, null);
		jPanel4.add(lblDataBaseFetchSize, null);
		jPanel4.add(jLabel1, null);
		subTabPanelDataBase.add(panelDataBaseEncode, null);
		panelDataBaseEncode.add(chkEncodeEnabled, null);
		panelDataBaseEncode.add(cmbDBEncode, null);
		panelDataBaseEncode.add(lblDBEncode, null);
		panelDataBaseEncode.add(cmbGridEncode, null);
		panelDataBaseEncode.add(lblGridEncode, null);
	}

	private void initResource() {
		String[] defaultEncodes = {
			"Shift_JIS", "ISO8859_1",
			"UTF-8", "GBK", "GB2312",
			"EUC-JP"
		};

		for (int i = 0; i < defaultEncodes.length; i++) {
			cmbDBEncode.addItem(defaultEncodes[i]);
			cmbGridEncode.addItem(defaultEncodes[i]);
		}
	}

	/**
	 * 画面の表示設定
	 */
	private void updateUIOptions() {
		////////////////////////////////////////
		// Column Panel
		////////////////////////////////////////
		// Column name
		if (PJConst.COLUMN_NAME_CASE_NO.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_NAME_CASE))) {
			rdoDataBeanNameNo.setSelected(true);
		} else if (PJConst.COLUMN_NAME_CASE_LOWCASE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_NAME_CASE))) {
			rdoDataBeanNameLowcase.setSelected(true);
		} else if (PJConst.COLUMN_NAME_CASE_UPCASE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_NAME_CASE))) {
			rdoDataBeanNameUpCase.setSelected(true);
		}

		// bean method
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_METHOD_GETTER))) {
			chkGetter.setSelected(true);
		} else {
			chkGetter.setSelected(false);
		}
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_METHOD_SETTER))) {
			chkSetter.setSelected(true);
		} else {
			chkSetter.setSelected(false);
		}
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_METHOD_TOSTRING))) {
			chkToString.setSelected(true);
		} else {
			chkToString.setSelected(false);
		}

		// comment
		if (PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT_NAME.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT))) {
			rdoCommentDefaultName.setSelected(true);
		} else if (PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT_NULL.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT))) {
			rdoCommentDefaultNull.setSelected(true);
		}
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULTNAMEIFNULL))) {
			chkCommentNameIfNull.setSelected(true);
		} else {
			chkCommentNameIfNull.setSelected(false);
		}

		// bean info
		// bean info config
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_BEANINFO_DEFAULTCONSTRUCTOR))) {
			chkDefaultConstructor.setSelected(true);
		} else {
			chkDefaultConstructor.setSelected(false);
		}
		if (PJConst.OPTIONS_TRUE.equals(PropertyManager.getProperty(PJConst.OPTIONS_COLUMN_BEANINFO_SERIALIZABLE))) {
			chkSerialize.setSelected(true);
		} else {
			chkSerialize.setSelected(false);
		}

		////////////////////////////////////////
		// DataBase Panel
		////////////////////////////////////////
		int fetchSize = UIUtil.getDefaultFetchSize();
		txtDataBaseFetchSize.setText(String.valueOf(fetchSize));

		boolean isEncodeEnabled = StringUtil.isEncodeEnabled();
		chkEncodeEnabled.setSelected(isEncodeEnabled);
		cmbDBEncode.setEnabled(isEncodeEnabled);
		cmbGridEncode.setEnabled(isEncodeEnabled);
		cmbDBEncode.setSelectedItem(StringUtil.getFromEncode());
		cmbGridEncode.setSelectedItem(StringUtil.getToEncode());

		////////////////////////////////////////
		// View Panel
		////////////////////////////////////////
		String[] paramGrid = {
			PropertyManager.getProperty(PJConst.OPTIONS_VIEW_GRIDFONT_NAME),
			PropertyManager.getProperty(PJConst.OPTIONS_VIEW_GRIDFONT_STYLE),
			PropertyManager.getProperty(PJConst.OPTIONS_VIEW_GRIDFONT_SIZE)
		};
		setFontText(lblViewGridFontName, paramGrid);

		String[] paramSQL = {
			PropertyManager.getProperty(PJConst.OPTIONS_VIEW_SQLFONT_NAME),
			PropertyManager.getProperty(PJConst.OPTIONS_VIEW_SQLFONT_STYLE),
			PropertyManager.getProperty(PJConst.OPTIONS_VIEW_SQLFONT_SIZE)
		};
		setFontText(lblViewSQLSciptFontName, paramSQL);


		////////////////////////////////////////
		// ClassPath Panel
		////////////////////////////////////////
		btnRemoveClassPath.setEnabled(false);
		ArrayList preJarLists = PropertyManager.getProperty(PJConst.OPTIONS_CLASSPATH_JARS, true, true);
		Vector preData = new Vector();
		preData.addAll(preJarLists);
		listClassPathListLeftModel.resetData(preData);

		////////////////////////////////////////
		// general Panel
		////////////////////////////////////////
		txtAuthorName.setText(PropertyManager.getProperty(PJConst.OPTIONS_GENERAL_USER_AUTHOR));
		txtVersion.setText(PropertyManager.getProperty(PJConst.OPTIONS_GENERAL_USER_VERSION));
	}


	/*************************************************************************************
	/* 業務用エリア
	/************************************************************************************/
	private boolean saveOptions() {
		if (!checkValidy()) {
			return false;
		}

		////////////////////////////////////////
		// data bean Panel
		////////////////////////////////////////
		// field name config
		if (rdoDataBeanNameNo.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_NAME_CASE, PJConst.COLUMN_NAME_CASE_NO);
		} else if (rdoDataBeanNameLowcase.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_NAME_CASE, PJConst.COLUMN_NAME_CASE_LOWCASE);
		} else if (rdoDataBeanNameUpCase.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_NAME_CASE, PJConst.COLUMN_NAME_CASE_UPCASE);
		}

		// bean method config
		if (chkGetter.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_METHOD_GETTER, PJConst.OPTIONS_TRUE);
		} else {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_METHOD_GETTER, PJConst.OPTIONS_FALSE);
		}
		if (chkSetter.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_METHOD_SETTER, PJConst.OPTIONS_TRUE);
		} else {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_METHOD_SETTER, PJConst.OPTIONS_FALSE);
		}
		if (chkToString.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_METHOD_TOSTRING, PJConst.OPTIONS_TRUE);
		} else {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_METHOD_TOSTRING, PJConst.OPTIONS_FALSE);
		}

		// comment config
		if (rdoCommentDefaultName.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT, PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT_NAME);
		} else if (rdoCommentDefaultNull.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT, PJConst.OPTIONS_COLUMN_COMMENT_DEFAULT_NULL);
		}
		if (chkCommentNameIfNull.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULTNAMEIFNULL, PJConst.OPTIONS_TRUE);
		} else {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_COMMENT_DEFAULTNAMEIFNULL, PJConst.OPTIONS_FALSE);
		}

		// bean info config
		if (chkDefaultConstructor.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_BEANINFO_DEFAULTCONSTRUCTOR, PJConst.OPTIONS_TRUE);
		} else {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_BEANINFO_DEFAULTCONSTRUCTOR, PJConst.OPTIONS_FALSE);
		}
		if (chkSerialize.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_BEANINFO_SERIALIZABLE, PJConst.OPTIONS_TRUE);
		} else {
			PropertyManager.setProperty(PJConst.OPTIONS_COLUMN_BEANINFO_SERIALIZABLE, PJConst.OPTIONS_FALSE);
		}

		////////////////////////////////////////
		// ClassPath Panel
		////////////////////////////////////////
		Vector newData = listClassPathListLeftModel.getData();
		PropertyManager.removeProperty(PJConst.OPTIONS_CLASSPATH_JARS, true);
		for (int i = 0; i < newData.size(); i++) {
			String oneJar = (String) newData.get(i);
			PropertyManager.setProperty(PJConst.OPTIONS_CLASSPATH_JARS +"[" + i + "]", oneJar);
		}
		ResourceManager.initClassLoader();

		////////////////////////////////////////
		// database Panel
		////////////////////////////////////////
		if (chkEncodeEnabled.isSelected()) {
			PropertyManager.setProperty(PJConst.OPTIONS_DATABASE_ENCODE_ENABLED, "1");
		} else {
			PropertyManager.setProperty(PJConst.OPTIONS_DATABASE_ENCODE_ENABLED, "0");
		}
		PropertyManager.setProperty(PJConst.OPTIONS_DATABASE_RESULT_FETCH_BUFFER, txtDataBaseFetchSize.getText());
		PropertyManager.setProperty(PJConst.OPTIONS_DATABASE_ENCODE_FROM, StringUtil.nvl((String) cmbDBEncode.getSelectedItem()));
		PropertyManager.setProperty(PJConst.OPTIONS_DATABASE_ENCODE_TO, StringUtil.nvl((String) cmbGridEncode.getSelectedItem()));

		////////////////////////////////////////
		// general Panel
		////////////////////////////////////////
		PropertyManager.setProperty(PJConst.OPTIONS_GENERAL_USER_AUTHOR, txtAuthorName.getText());
		PropertyManager.setProperty(PJConst.OPTIONS_GENERAL_USER_VERSION, txtVersion.getText());

		return true;
	}

	/**
	 * check the input validity
	 */
	boolean checkValidy() {
		String strFetchSize = txtDataBaseFetchSize.getText();
		int fetchSize = 0;
		try {
			fetchSize = Integer.parseInt(strFetchSize);
			if (fetchSize < 150 || fetchSize > 600) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nfe) {
			MessageManager.showMessage("MCSTC008E", "Number(150~600)");
			tabbedPaneOptions.setSelectedComponent(subTabPanelDataBase);
			txtDataBaseFetchSize.requestFocus();
			return false;
		}

		if (chkEncodeEnabled.isSelected()) {
			String strFromEncode = (String) cmbDBEncode.getSelectedItem();
			String strToEncode = (String) cmbGridEncode.getSelectedItem();

			if (strFromEncode != null && !strFromEncode.equals("")) {
				if (!StringUtil.isEncodeValidy(strFromEncode)) {
					MessageManager.showMessage("MCSTC010E", strFromEncode);
					tabbedPaneOptions.setSelectedComponent(subTabPanelDataBase);
					cmbDBEncode.requestFocus();
					return false;
				}
			}
			if (strToEncode != null && !strToEncode.equals("")) {
				if (!StringUtil.isEncodeValidy(strToEncode)) {
					MessageManager.showMessage("MCSTC010E", strToEncode);
					tabbedPaneOptions.setSelectedComponent(subTabPanelDataBase);
					cmbGridEncode.requestFocus();
					return false;
				}
			}
		}

		return true;
	}

	/*************************************************************************************
	/* 事件処理用エリア
	/************************************************************************************/
	void bntCancel_actionPerformed(ActionEvent e) {
		dispose();
	}

	void btnOK_actionPerformed(ActionEvent e) {
		if (!saveOptions()) {
			return;
		}

		dispose();
	}

	void btnSave_actionPerformed(ActionEvent e) {
		saveOptions();
	}

	void btnAddClassPath_actionPerformed(ActionEvent e) {
		if (fileDialog == null) {
			fileDialog = new FileDialog(Main.getMDIMain(), "Select Jar File");
			fileDialog.setFilenameFilter(new JarFileFilter());
			fileDialog.setMode(FileDialog.LOAD);
		}
		fileDialog.show();
		String file = fileDialog.getFile();
		if (file == null) {
			return;
		}

		String fullPath = fileDialog.getDirectory() + file;

		if (!listClassPathListLeftModel.contains(fullPath)) {
			listClassPathListLeftModel.addElement(fullPath);
			listClassPathListLeft.setSelectedValue(fullPath, true);
		}
	}

	void btnRemoveClassPath_actionPerformed(ActionEvent e) {
		Object selectedValue = listClassPathListLeft.getSelectedValue();

		if (selectedValue != null) {
			listClassPathListLeftModel.removeElement(selectedValue);
			listClassPathListLeft.clearSelection();
			listClassPathListLeft.repaint();
		}
	}

	/**
	 * set remove button status
	 */
	void setRemoveButtonStatus() {
		if (listClassPathListLeft.getSelectedValue() != null) {
			btnRemoveClassPath.setEnabled(true);
		} else {
			btnRemoveClassPath.setEnabled(false);
		}
	}

	class JarFileFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			if (name.endsWith(".jar") || name.endsWith(".zip")) {
				return true;
			}
			return false;
		}
	}

	void btnViewSQLFontBrowse_actionPerformed(ActionEvent e) {
		DialogFontChooser dialogFontChooser = new DialogFontChooser();
		dialogFontChooser.setOpFlag(PJConst.WINDOW_CONFIGSQLFONT);
		dialogFontChooser.addParamTransferListener(this);
		dialogFontChooser.setVisible(true);
		dialogFontChooser.removeParamTransferListener(this);
	}

	void btnViewGridFontBrowse_actionPerformed(ActionEvent e) {
		DialogFontChooser dialogFontChooser = new DialogFontChooser();
		dialogFontChooser.setOpFlag(PJConst.WINDOW_CONFIGGRIDFONT);
		dialogFontChooser.addParamTransferListener(this);
		dialogFontChooser.setVisible(true);
		dialogFontChooser.removeParamTransferListener(this);
	}

	void chkEncodeEnabled_StateChanged(ChangeEvent e) {
		if (chkEncodeEnabled.isSelected()) {
			cmbDBEncode.setEnabled(true);
			cmbGridEncode.setEnabled(true);
		} else {
			cmbDBEncode.setEnabled(false);
			cmbGridEncode.setEnabled(false);
		}
	}

	/**
	 * font choose set param
	 */
	public void paramTransfered(ParamTransferEvent pe) {
		int opFlag = pe.getOpFlag();

		switch (opFlag) {
			case PJConst.WINDOW_CONFIGGRIDFONT:
				updateConfigGridFont(pe.getParam());
				break;
			case PJConst.WINDOW_CONFIGSQLFONT:
				updateConfigSQLFont(pe.getParam());
				break;
			default :
				break;
		}
	}

	void updateConfigGridFont(Object param) {
		String[] params = (String[]) param;

		PropertyManager.setProperty(PJConst.OPTIONS_VIEW_GRIDFONT_NAME, params[0]);
		PropertyManager.setProperty(PJConst.OPTIONS_VIEW_GRIDFONT_STYLE, params[1]);
		PropertyManager.setProperty(PJConst.OPTIONS_VIEW_GRIDFONT_SIZE, params[2]);

		setFontText(lblViewGridFontName, params);

		PJTableCellRender.updateFont();
	}

	void updateConfigSQLFont(Object param) {
		String[] params = (String[]) param;

		PropertyManager.setProperty(PJConst.OPTIONS_VIEW_SQLFONT_NAME, params[0]);
		PropertyManager.setProperty(PJConst.OPTIONS_VIEW_SQLFONT_STYLE, params[1]);
		PropertyManager.setProperty(PJConst.OPTIONS_VIEW_SQLFONT_SIZE, params[2]);

		setFontText(lblViewSQLSciptFontName, params);

		if (Main.getMDIMain().sqlScriptPanel != null) {
			Main.getMDIMain().sqlScriptPanel.resetDefaultFontStyle();
		}
	}

	void setFontText(JLabel lblField, String[] params) {
		String fontName = params[0];
		if (fontName == null || fontName.equals("")) {
			fontName = "dialog";
		}

		String strFontStyle = "Plain";
		try {
			int fontStyle = Integer.parseInt(params[1]);

			if (fontStyle == Font.PLAIN) {
				strFontStyle = "Plain";
			} else if (fontStyle == Font.BOLD) {
				strFontStyle = "Bold";
			} else if(fontStyle == Font.ITALIC) {
				strFontStyle = "Italic";
			} else if (fontStyle == Font.BOLD + Font.ITALIC) {
				strFontStyle = "Bold Italic";
			}
		} catch (NumberFormatException nfe) {
			strFontStyle = "Plain";
		}

		String strFontSize = params[2];
		if (strFontSize == null || strFontSize.equals("")) {
			strFontSize = "12";
		}

		lblField.setText(fontName + ", " + strFontStyle + ", " + strFontSize);
	}
}