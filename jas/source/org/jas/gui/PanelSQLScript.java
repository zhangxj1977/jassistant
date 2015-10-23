package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
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
import org.jas.util.StringUtil;

/**
 *
 *
 *
 *
 * @author ���@�w�R
 * @version 1.0
 */
public class PanelSQLScript extends JSplitPane {
    ImageIcon iconSQLExecute = ImageManager.createImageIcon("sqlexecute.gif");
    ImageIcon iconSQLExecuteMulti = ImageManager.createImageIcon("sqlexecutemulti.gif");
    ImageIcon iconCommit = ImageManager.createImageIcon("commit.gif");
    ImageIcon iconUndo = ImageManager.createImageIcon("undo.gif");
    ImageIcon iconRedo = ImageManager.createImageIcon("redo.gif");
    ImageIcon iconToUpper = ImageManager.createImageIcon("touppercase.gif");
    ImageIcon iconToLower = ImageManager.createImageIcon("tolowcase.gif");
    ImageIcon iconSQLBuilder = ImageManager.createImageIcon("sqlbuilder.gif");
    ImageIcon iconShowTables = ImageManager.createImageIcon("showtables.gif");
    ImageIcon iconEditOneRow = ImageManager.createImageIcon("editonerow.gif");
    ImageIcon iconResultSave = ImageManager.createImageIcon("savetabledata.gif");
    ImageIcon iconReplaceParams = ImageManager.createImageIcon("replaceparams.gif");
    ImageIcon iconClearTextResult = ImageManager.createImageIcon("clearresult.gif");
    BorderLayout mainBrderLayout = new BorderLayout();
    JPanel panelSQLLeft = new JPanel();
    JPanel panelSQLRight = new JPanel();
    JPanel panelSQLScriptInput = new JPanel();
    JPanel panelSQLTableResult = new JPanel();
    JPanel panelSQLTextResult = new JPanel();
    BorderLayout borderLayoutInput = new BorderLayout();
    BorderLayout borderLayoutResult = new BorderLayout();
    JSplitPane panelSplitInput = new JSplitPane();
    JScrollPane scpSQLScriptInput = new JScrollPane();
    JScrollPane scpSQLParamsInput = new JScrollPane();
    JToolBar toolBarParamsInput = new JToolBar();
    JLabel rowHeaderLabel = new JLabel();
    BorderLayout borderLayoutScriptInput = new BorderLayout();
    PJSQLTextPane txtPanelSQLScript = new PJSQLTextPane();
    JTextPane txtPanelSQLParam = new JTextPane();
    JToolBar toolBarTableResult = new JToolBar();
    JToolBar toolBarTextResult = new JToolBar();
    JScrollPane scpResultTable = new JScrollPane();
    ScrollChangeListener scrollChangeListener = new ScrollChangeListener();
    JTable tblTableResult = new PJDBDataTable();
    PJTableCellRender dbTableCellRender = new PJTableCellRender();
    Border border1;
    JTextArea txtScriptResult = new JTextArea();
    JScrollPane scpScriptResult = new JScrollPane();
    JTabbedPane tbpResult = new JTabbedPane();
    String lastSQL = null;
    TitledBorder titledBorder1;
    JToolBar toolBarSQL = new JToolBar();
    JButton btnLeftTopCorner = new JButton();
    PJDBDataTableRowHeader rowHeader = new PJDBDataTableRowHeader(tblTableResult);
    TableSelectionRowListener selectionRowListener = new TableSelectionRowListener();
    RollOverButton btnExecuteSQL = new RollOverButton();
    RollOverButton btnExecuteSQLMulti = new RollOverButton();
    RollOverButton btnUndo = new RollOverButton();
    RollOverButton btnRedo = new RollOverButton();
    RollOverButton btnCommit = new RollOverButton();
    RollOverButton btnToUpper = new RollOverButton();
    RollOverButton btnToLower = new RollOverButton();
    RollOverButton btnSQLBuilder = new RollOverButton();
    RollOverButton btnShowUsefulInfo = new RollOverButton();
    RollOverButton btnSaveResult = new RollOverButton();
    RollOverButton btnClearTextResult = new RollOverButton();
    JCheckBox chkShowReplaceParam = new JCheckBox("�ϐ��u��");
    RollOverButton btnReplaceParam = new RollOverButton();

    JCheckBox chkSepComma = new JCheckBox("�J���}�i,�j");
    JCheckBox chkSepSpace = new JCheckBox("�X�y�[�X");
    JCheckBox chkSepCustom = new JCheckBox("�w��");
    JTextField txtSepInput = new JTextField(", ");

    double d_location = 1.0d;

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
        panelSQLLeft.setLayout(new BorderLayout());
        panelSQLRight.setLayout(new BorderLayout());
        panelSQLScriptInput.setLayout(borderLayoutInput);
        panelSQLTableResult.setLayout(borderLayoutResult);
        panelSQLTextResult.setLayout(new BorderLayout());
        panelSplitInput.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        btnExecuteSQL.setToolTipText("�P��SQL�����s�B Ctrl+Enter");
        btnExecuteSQL.setIcon(iconSQLExecute);
        btnExecuteSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeSQLPerformed();
            }
        });
        btnExecuteSQLMulti.setToolTipText("<html>�����X�N���v�g�A�����s�BF5 <br/>���u�J���} + ���s�v�ŋ�؂�");
        btnExecuteSQLMulti.setIcon(iconSQLExecuteMulti);
        btnExecuteSQLMulti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeSQLMultiPerformed();
            }
        });
        btnUndo.setIcon(iconUndo);
        btnUndo.setToolTipText("���ɖ߂� Ctrl+Z");
        btnUndo.setEnabled(false);
        btnRedo.setEnabled(false);
        btnRedo.setToolTipText("��蒼�� Ctrl+Y");
        btnRedo.setIcon(iconRedo);
        btnCommit.setToolTipText("�R�~�b�g Ctrl+F9");
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
        btnToUpper.setToolTipText("�啶�� Alt+F6");
        btnToLower.setToolTipText("������ Alt+F7");
        btnToLower.setIcon(iconToLower);
        btnToLower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toLowerPerformed();
            }
        });
        btnSQLBuilder.setIcon(iconSQLBuilder);
        btnSQLBuilder.setToolTipText("SQL���`");
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

        chkShowReplaceParam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetSplitLayout();
            }
        });
        btnReplaceParam.setIcon(iconReplaceParams);
        btnReplaceParam.setToolTipText("�o�C���h�ϐ��u���B�J���}��؂�B(type)�`���Ή�");
        btnReplaceParam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doReplaceParams();
            }
        });
        panelSQLScriptInput.add(toolBarSQL, BorderLayout.NORTH);
        panelSQLScriptInput.add(panelSplitInput,  BorderLayout.CENTER);
        panelSplitInput.setLeftComponent(panelSQLLeft);
        panelSplitInput.setRightComponent(panelSQLRight);

        panelSQLLeft.add(scpSQLScriptInput,  BorderLayout.CENTER);
        panelSQLRight.add(scpSQLParamsInput, BorderLayout.CENTER);
        panelSQLRight.add(toolBarParamsInput, BorderLayout.NORTH);
        toolBarParamsInput.add(chkSepComma, null);
        toolBarParamsInput.add(chkSepSpace, null);
        toolBarParamsInput.add(chkSepCustom, null);
        toolBarParamsInput.add(txtSepInput, null);
        toolBarParamsInput.setFloatable(false);
        chkSepComma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chkSepComma.isSelected()) {
                    chkSepSpace.setSelected(false);
                    chkSepCustom.setSelected(false);
                }
            }
        });
        chkSepSpace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chkSepSpace.isSelected()) {
                    chkSepComma.setSelected(false);
                    chkSepCustom.setSelected(false);
                }
            }
        });
        chkSepCustom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chkSepCustom.isSelected()) {
                    chkSepComma.setSelected(false);
                    chkSepSpace.setSelected(false);
                }
            }
        });
        chkSepCustom.setSelected(true);
        scpSQLScriptInput.getViewport().add(txtPanelSQLScript, null);
        scpSQLParamsInput.getViewport().add(txtPanelSQLParam, null);
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
        btnSaveResult.setToolTipText("�₢���킹���ʕۑ�");
        toolBarTableResult.setFloatable(false);
        toolBarTableResult.add(btnSaveResult);
        btnSaveResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TableModel model = tblTableResult.getModel();
                if (model instanceof DBTableModel) {
                    DBTableModel dbModel = (DBTableModel) model;
                    DialogSaveResultData dialogSaveResultData = new DialogSaveResultData();
                    dialogSaveResultData.initResources(dbModel, lastSQL);
                    dialogSaveResultData.setVisible(true);
                }
            }
        });
        panelSQLTableResult.add(toolBarTableResult, BorderLayout.NORTH);
        panelSQLTableResult.add(scpResultTable, BorderLayout.CENTER);
        scpResultTable.getViewport().add(tblTableResult, null);
        tblTableResult.setDefaultRenderer(Object.class, dbTableCellRender);
        tblTableResult.getSelectionModel().addListSelectionListener(selectionRowListener);
        tblTableResult.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblTableResult.setColumnSelectionAllowed(true);
        scpResultTable.setRowHeaderView(rowHeader);
        rowHeader.getSelectionModel().addListSelectionListener(selectionRowListener);
        btnLeftTopCorner.setIcon(iconEditOneRow);
        btnLeftTopCorner.setPreferredSize(new Dimension(20, 20));
        btnLeftTopCorner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectRow = tblTableResult.getSelectedRow();
                viewRowData(selectRow);
            }
        });
        scpResultTable.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, btnLeftTopCorner);
        toolBarSQL.add(btnExecuteSQL, null);
        toolBarSQL.add(btnExecuteSQLMulti, null);
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

        JToolBar.Separator separator4 = new JToolBar.Separator(new Dimension(2, 28));
        separator4.setBorder(BorderFactory.createEtchedBorder());
        toolBarSQL.add(separator4, null);
        toolBarSQL.add(chkShowReplaceParam, null);
        toolBarSQL.add(btnReplaceParam, null);
        
        txtScriptResult.setEditable(false);
        scpScriptResult.getViewport().add(txtScriptResult);
        btnClearTextResult.setIcon(iconClearTextResult);
        btnClearTextResult.setToolTipText("�o�͓��e�N���A");
        btnClearTextResult.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtScriptResult.setText("");
            }
        });
        toolBarTextResult.setFloatable(false);
        toolBarTextResult.add(btnClearTextResult);
        panelSQLTextResult.add(toolBarTextResult, BorderLayout.NORTH);
        panelSQLTextResult.add(scpScriptResult, BorderLayout.CENTER);
        
        tbpResult.addTab("�₢���킹����", iconSQLExecute, panelSQLTableResult);
        tbpResult.addTab("�X�N���v�g�o��", iconSQLExecuteMulti, panelSQLTextResult);
        this.setBottomComponent(tbpResult);
        this.setTopComponent(panelSQLScriptInput);
        this.setDividerLocation(250);

        this.addComponentListener(new MyComponentAdapter());
    }

    class MyComponentAdapter extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            resetSplitLayout();
        }
    }

    public void resetSplitLayout() {
        if (chkShowReplaceParam.isSelected()) {
            d_location = 0.8d;
            btnReplaceParam.setEnabled(true);
        } else {
            d_location = 1.0d;
            btnReplaceParam.setEnabled(false);
        }
        panelSplitInput.setDividerLocation(d_location);
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
            btnExecuteSQLMulti.setEnabled(true);
        } else {
            txtPanelSQLScript.setEnabled(false);
            btnCommit.setEnabled(false);
            btnExecuteSQL.setEnabled(false);
            btnExecuteSQLMulti.setEnabled(false);
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

        DBTableModel dataModel = new DBTableModel(tblTableResult, null, tempData, PJConst.TABLE_TYPE_READONLY);
        tblTableResult.setModel(dataModel);

        tblTableResult.getColumnModel().getColumn(0).setPreferredWidth(120);

        tblTableResult.repaint();
    }

    void doReplaceParams() {
        String strParams = txtPanelSQLParam.getText();
        if (strParams == null || strParams.trim().equals("")) {
            return;
        }
        String sep = ",";
        if (chkSepComma.isSelected()) {
            sep = ",";
        } else if (chkSepSpace.isSelected()) {
            sep = " ";
        } else if (chkSepCustom.isSelected()) {
            sep = txtSepInput.getText();
        }
        String[] lstParams = strParams.split(sep);
        txtPanelSQLScript.replaceParams(lstParams);
    }

    /**
     * execute sql action perform
     */
    void executeSQLPerformed() {
        String sql;
        String selectSql = txtPanelSQLScript.getSelectedText();
        int orgSelectStart = -1;
        int orgSelectEnd = -1;
        int orgCaret = txtPanelSQLScript.getCaretPosition();
        if (selectSql != null && !selectSql.trim().equals("")) {
            sql = selectSql;
            orgSelectStart = txtPanelSQLScript.getSelectionStart();
            orgSelectEnd = txtPanelSQLScript.getSelectionEnd();
        } else {
            sql = txtPanelSQLScript.getText();
            sql = sql.replaceAll("\r\n", "\n");
            if (sql.trim().equals("")) {
                return;
            }
            int caret = txtPanelSQLScript.getCaretPosition();
            if (caret >= 0) {
                int prevLine = sql.lastIndexOf("\n", caret - 1);
                if (prevLine >= 0) {
                    caret = prevLine + 1;
                } else {
                    caret = 0;
                }
                int endPos = sql.length();
                Pattern pattern = Pattern.compile(";(\\s*)\n");
                Matcher matcher = pattern.matcher(sql);
                if (matcher.find(caret)) {
                    endPos = matcher.start();
                }

                int startPos = sql.lastIndexOf(";", caret - 1);
                while (startPos >= 0) {
                    if (matcher.find(startPos)) {
                        if (matcher.start() == startPos) {
                            startPos = matcher.end();
                            break;
                        } else {
                            startPos = sql.lastIndexOf(";", startPos - 1);
                        }
                    }
                }
                if (startPos < 0) {
                    startPos = 0;
                }

                sql = sql.substring(startPos, endPos);
                txtPanelSQLScript.setSelectionStart(startPos);
                txtPanelSQLScript.setSelectionEnd(endPos);
            }
        }

        txtPanelSQLScript.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        tbpResult.setSelectedComponent(panelSQLTableResult);

        ThreadDoSQLExecute t = new ThreadDoSQLExecute(
                sql, orgSelectStart, orgSelectEnd, orgCaret);
        t.start();
    }

    class ThreadDoSQLExecute extends Thread {
        String sql = null;
        int orgSelectStart = -1;
        int orgSelectEnd = -1;
        int orgCaret = -1;
        public ThreadDoSQLExecute(String sql,
                int orgSelectStart, int orgSelectEnd, int orgCaret) {
            this.sql = sql;
            this.orgSelectStart = orgSelectStart;
            this.orgSelectEnd = orgSelectEnd;
            this.orgCaret = orgCaret;
        }
        public void run() {
            try {
                final Object obj = processExecuteSql(sql);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        processResultShow(obj);
                    }
                });
                lastSQL = sql;
            } finally {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (orgSelectEnd > 0) {
                            txtPanelSQLScript.setSelectionStart(orgSelectStart);
                            txtPanelSQLScript.setSelectionEnd(orgSelectEnd);
                        } else {
                            if (orgCaret > -1) {
                                txtPanelSQLScript.select(orgCaret, orgCaret);
                            }
                        }
                        txtPanelSQLScript.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                        txtPanelSQLScript.requestFocus();
                    }
                });
            }
        }
    }

    void executeSQLMultiPerformed() {
        String sql;
        String selectSql = txtPanelSQLScript.getSelectedText();
        int orgSelectStart = -1;
        int orgSelectEnd = -1;
        int orgCaret = txtPanelSQLScript.getCaretPosition();
        if (selectSql != null && !selectSql.trim().equals("")) {
            sql = selectSql;
            orgSelectStart = txtPanelSQLScript.getSelectionStart();
            orgSelectEnd = txtPanelSQLScript.getSelectionEnd();
        } else {
            sql = txtPanelSQLScript.getText();
        }

        txtPanelSQLScript.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        tbpResult.setSelectedComponent(panelSQLTextResult);

        String[] sqlArr = sql.split(";(\\s*)(\r?)\n");
        ThreadDoMultiSqlExecute t = new ThreadDoMultiSqlExecute(
                sqlArr, orgSelectStart, orgSelectEnd, orgCaret);
        t.start();
    }

    class ThreadDoMultiSqlExecute extends Thread {
        String[] sqlArr = null;
        int orgSelectStart = -1;
        int orgSelectEnd = -1;
        int orgCaret = -1;
        public ThreadDoMultiSqlExecute(String[] sqlArr,
                int orgSelectStart, int orgSelectEnd, int orgCaret) {
            this.sqlArr = sqlArr;
            this.orgSelectStart = orgSelectStart;
            this.orgSelectEnd = orgSelectEnd;
            this.orgCaret = orgCaret;
        }

        /**
         * start method
         */
        public void run() {
            try {
                if (sqlArr == null || sqlArr.length == 0) return;
                for (String oneSql : sqlArr) {
                    if (oneSql == null || oneSql.trim().equals("")) {
                        continue;
                    }
                    processScriptResultShow(oneSql);
                    appendResultText("");
                }
            } finally {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (orgSelectEnd > 0) {
                            txtPanelSQLScript.setSelectionStart(orgSelectStart);
                            txtPanelSQLScript.setSelectionEnd(orgSelectEnd);
                        } else {
                            if (orgCaret > -1) {
                                txtPanelSQLScript.select(orgCaret, orgCaret);
                            }
                        }
                        txtPanelSQLScript.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                        txtPanelSQLScript.requestFocus();
                    }
                });
            }
        }
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

        TableModel model = tblTableResult.getModel();
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
                if (source == tblTableResult.getSelectionModel()) {
                    int selectedRow = tblTableResult.getSelectedRow();
                    if (rowHeader.getSelectedRow() != selectedRow && selectedRow >= 0) {
                        rowHeader.changeSelection(selectedRow, 0, false, false);
                    }

                    if (selectedRow < 0) {
                        rowHeader.clearSelection();
                    }
                } else if (source == rowHeader.getSelectionModel()) {
                    int selectedRow = rowHeader.getSelectedRow();

                    if (selectedRow >= 0) {
                        if (tblTableResult.getSelectedRow() != selectedRow) {
                            tblTableResult.changeSelection(selectedRow, 0, false, false);
                        }
                    } else {
                        tblTableResult.clearSelection();
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

    void processScriptResultShow(String sql) {
        if (sql == null || sql.trim().equals("")) {
            return;
        }

        try {
            long beginTime = System.currentTimeMillis();
            Object value = DBParser.getResultByScript(Main.getMDIMain().getConnection(), sql, false, null);
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - beginTime;

            if (value instanceof Vector) {
                Vector data = (Vector) value;
                if (data != null && data.size() > 5) {
                    String header = StringUtil.joinList((Vector) data.get(0), '\t');
                    appendResultText(header);
                    for (int i = 5; i < data.size(); i++) {
                        appendResultText(StringUtil.joinList((Vector) data.get(i), '\t'));
                    }
                    appendResultText(StringUtil.getPadString("----------", '-', header.length()));
                    appendResultText((data.size() - 5) + "���������܂����B");
                } else {
                    appendResultText("�������ʂ�0���ł��B");
                }
            } else if (value instanceof Integer) {
                int affectRows = ((Integer) value).intValue();
                String action = "";
                String upperSql = sql.trim().toUpperCase();
                if (upperSql.startsWith("UPDATE")) {
                    action = "�X�V";
                } else if (upperSql.startsWith("INSERT")) {
                    action = "�}��";
                } else if (upperSql.startsWith("DELETE")) {
                    action = "�폜";
                }
                appendResultText(affectRows + "��" + action + "���܂����B");
            }
        } catch (SQLException se) {
            appendResultText(se.getMessage());
            return;
        }
    }

    void appendResultText(final String txt) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    txtScriptResult.setText(txtScriptResult.getText() + "\n" + txt);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * process query result
     */
    Object processExecuteSql(String sql) {
        if (sql == null || sql.trim().equals("")) {
            return null;
        }

        try {
            long beginTime = System.currentTimeMillis();
            Object value = DBParser.getResultByScript(Main.getMDIMain().getConnection(), sql, false, null);
            long endTime = System.currentTimeMillis();
            executeTime = endTime - beginTime;

            return value;
        } catch (Exception se) {
            return se;
        }
    }

    long executeTime = -1;

    /**
     * process query result show
     */
    void processResultShow(Object value) {
        if (value == null) {
            return;
        }

        try {
            if (value instanceof Vector) {
                Vector data = (Vector) value;
                DBTableModel dataModel = new DBTableModel(tblTableResult, null, data, PJConst.TABLE_TYPE_READONLY);
                tblTableResult.setModel(dataModel);

                PJTableRowHeaderModel rowHeaderModel = new PJTableRowHeaderModel(rowHeader, data);
                rowHeader.setModel(rowHeaderModel);

                if (data != null && data.size() > 4) {
                    rowHeaderModel.resetTable();
                    rowHeader.getColumnModel().getColumn(0).setCellRenderer(new PJDBDataTableRowHeaderRender());
                    dataModel.resetTable();
                    Main.getMDIMain().setStatusText((data.size() - 5) + " rows returned: " + executeTime + " ms");
                }
            } else if (value instanceof Integer) {
                int affectRows = ((Integer) value).intValue();
                Main.getMDIMain().setStatusText(affectRows + " rows updated or inserted: " + executeTime + " ms");
                setEmptyTableModel();
            } else if (value instanceof SQLException) {
                MessageManager.showMessage("MCSTC202E", ((SQLException) value).getMessage());
            } else if (value instanceof Exception) {
                MessageManager.showMessage("MCSTC203E", ((Exception) value).getMessage());
            }
        } finally {
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
            } else if (keyCode == KeyEvent.VK_F5) {
                executeSQLMultiPerformed();
            } else if (keyCode == KeyEvent.VK_ENTER && e.isControlDown()) {
                executeSQLPerformed();
            }
        }
    }
}
