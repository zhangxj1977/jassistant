package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.jas.base.PJDBCellEditor;
import org.jas.base.PJEditorTextField;
import org.jas.base.PJSQLTextPane;
import org.jas.base.PJTableCellRender;
import org.jas.base.RollOverButton;
import org.jas.common.Refreshable;
import org.jas.util.ImageManager;

/**
 *
 * @author 張　学軍
 * @version 1.0
 */
@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public class PanelReport extends JPanel implements Refreshable {

    ImageIcon iconAddRow = ImageManager.createImageIcon("addrow.gif");
    ImageIcon iconRemoveRow = ImageManager.createImageIcon("deleterow.gif");
    ImageIcon iconUparrow = ImageManager.createImageIcon("uparrow.gif");
    ImageIcon iconDownarrow = ImageManager.createImageIcon("downarrow.gif");

    JPanel panelMain = new JPanel();
    JPanel panelBottom = new JPanel();
    JPanel panelBottomLeft = new JPanel();
    JPanel panelBottomRight = new JPanel();

    JSplitPane slpMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JPanel panelInput = new JPanel();
    JPanel panelResult = new JPanel();
    JTabbedPane tbpResult = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

    JSplitPane slpDesign = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JPanel panelSQLDesc = new JPanel();
    JPanel panelSQLEdit = new JPanel();
    JToolBar toolBarSQLEdit = new JToolBar();
    JScrollPane scpSQLEdit = new JScrollPane();
    PJSQLTextPane txtSQLEdit = new PJSQLTextPane();

    JSplitPane slpDesc = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JPanel panelParam = new JPanel();
    JToolBar toolBarParam = new JToolBar();
    JScrollPane scpParam = new JScrollPane();
    JTable tblParam = new JTable();
    JPanel panelSQLList = new JPanel();
    JToolBar toolBarSQLList = new JToolBar();
    JScrollPane scpSQLList = new JScrollPane();
    JTable tblSQLList = new JTable();

    JLabel lblSqlName = new JLabel();
    JLabel lblSqlDesc = new JLabel();

    RollOverButton btnAddParam = new RollOverButton();
    RollOverButton btnDeleteParam = new RollOverButton();
    RollOverButton btnUpParam = new RollOverButton();
    RollOverButton btnDownParam = new RollOverButton();

    RollOverButton btnAddSQL = new RollOverButton();
    RollOverButton btnDeleteSQL = new RollOverButton();
    RollOverButton btnUpSQL = new RollOverButton();
    RollOverButton btnDownSQL = new RollOverButton();

    JButton btnSave = new JButton();
    JButton btnAllExecute = new JButton();
    JButton btnExport = new JButton();

    ButtonGroup grpExportType = new ButtonGroup();
    JRadioButton rdoExportExcel = new JRadioButton();
    JRadioButton rdoExportCsv = new JRadioButton();

    SQLCellEditorListener sqlCellEditorListener = new SQLCellEditorListener();

    String currentReportName = null;
    String currentType = "";


    public PanelReport() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setLayout(new BorderLayout());

        panelMain.setLayout(new BorderLayout());
        panelBottom.setLayout(new GridLayout(1, 2));
        this.add(panelMain, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
        panelBottom.setPreferredSize(new Dimension(this.getWidth(), 40));
        panelBottomLeft.setLayout(null);
        panelBottomRight.setLayout(null);
        panelBottom.add(panelBottomLeft);
        panelBottom.add(panelBottomRight);

        ImageIcon iconSave = ImageManager.createImageIcon("savetabledata.gif");
        btnSave.setText("保存");
        btnSave.setIcon(iconSave);
        btnSave.setBounds(15, 10, 80, 25);
        panelBottomLeft.add(btnSave);

        ImageIcon iconSQLAllExec = ImageManager.createImageIcon("sqlexecute.gif");
        btnAllExecute.setText("一括実行");
        btnAllExecute.setIcon(iconSQLAllExec);
        btnAllExecute.setBounds(15, 10, 100, 25);
        panelBottomRight.add(btnAllExecute);

        ImageIcon iconExport = ImageManager.createImageIcon("exporttofile.gif");
        btnExport.setText("エクスポート");
        btnExport.setIcon(iconExport);
        btnExport.setBounds(120, 10, 120, 25);
        panelBottomRight.add(btnExport);

        rdoExportExcel.setText("Excel");
        rdoExportCsv.setText("CSV");
        grpExportType.add(rdoExportExcel);
        grpExportType.add(rdoExportCsv);
        rdoExportExcel.setSelected(true);

        rdoExportExcel.setBounds(245, 10, 60, 25);
        rdoExportCsv.setBounds(305, 10, 60, 25);
        panelBottomRight.add(rdoExportExcel);
        panelBottomRight.add(rdoExportCsv);

        panelMain.add(slpMain, BorderLayout.CENTER);
        slpMain.setDividerSize(5);
        slpMain.setTopComponent(panelInput);
        slpMain.setBottomComponent(panelResult);

        panelResult.setLayout(new BorderLayout());
        panelResult.add(tbpResult);

        panelInput.setLayout(new BorderLayout());
        panelInput.add(slpDesign);
        slpDesign.setDividerSize(5);
        slpDesign.setTopComponent(panelSQLDesc);
        slpDesign.setBottomComponent(panelSQLEdit);

        panelSQLDesc.setLayout(new BorderLayout());
        panelSQLDesc.add(slpDesc);
        slpDesc.setDividerSize(5);
        slpDesc.setLeftComponent(panelParam);
        slpDesc.setRightComponent(panelSQLList);

        slpMain.setBorder(new BevelBorder(BevelBorder.LOWERED));
        slpDesign.setBorder(new EmptyBorder(1, 1, 1, 1));
        slpDesc.setBorder(new EmptyBorder(1, 1, 1, 1));

        initPanelParam();
        initPanelSQLDesc();
        initPanelSQLEdit();
        initPanelResult();

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                reLayout();
            }
        });
    }

    PJTableCellRender defaultCellRender = new PJTableCellRender();
    String[] paramHeaders = new String[]{"No", "名前", "値"};
    int[] paramColumnWidth = new int[]{20, 140, 140};
    Vector vecParamData = new Vector();
    ParamTableModel paramTblModel = new ParamTableModel();

    private void initPanelParam() {
        btnAddParam.setIcon(iconAddRow);
        btnAddParam.setToolTipText("パラメータを追加");
        btnDeleteParam.setIcon(iconRemoveRow);
        btnDeleteParam.setToolTipText("パラメータを削除");
        btnUpParam.setIcon(iconUparrow);
        btnUpParam.setToolTipText("パラメータを上に移動");
        btnDownParam.setIcon(iconDownarrow);
        btnDownParam.setToolTipText("パラメータを下に移動");

        toolBarParam.setFloatable(false);
        toolBarParam.add(btnAddParam);
        toolBarParam.add(btnDeleteParam);
        toolBarParam.addSeparator();
        toolBarParam.add(btnUpParam);
        toolBarParam.add(btnDownParam);

        scpParam.getViewport().add(tblParam);
        panelParam.setBorder(new TitledBorder("パラメータ"));
        panelParam.setLayout(new BorderLayout());
        panelParam.add(toolBarParam, BorderLayout.NORTH);
        panelParam.add(scpParam, BorderLayout.CENTER);

        tblParam.setModel(paramTblModel);
        tblParam.setRowHeight(18);
        tblParam.setDefaultRenderer(Object.class, defaultCellRender);
        tblParam.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblParam.setColumnSelectionAllowed(false);
        tblParam.getTableHeader().setReorderingAllowed(false);
        tblParam.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        tblParam.getColumn(paramHeaders[0]).setCellRenderer(new IndexCellRender());

        for (int i = 0; i < paramColumnWidth.length; i++) {
            tblParam.getColumnModel().getColumn(i).setPreferredWidth(paramColumnWidth[i]);
        }

        btnAddParam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDefaultParamRow();
            }
        });
        btnDeleteParam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectParamRow();
            }
        });
        btnDownParam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveParamRowDown();
            }
        });
        btnUpParam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveParamRowUp();
            }
        });

        tblParam.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                setBtnParamEnable();
            }
        });
        setBtnParamEnable();
    }

    private void setBtnParamEnable() {
        if (tblParam.getSelectedRowCount() > 0) {
            btnDeleteParam.setEnabled(true);
            btnDownParam.setEnabled(true);
            btnUpParam.setEnabled(true);
        } else {
            btnDeleteParam.setEnabled(false);
            btnDownParam.setEnabled(false);
            btnUpParam.setEnabled(false);
        }
    }

    String[] sqlHeaders = new String[]{"No","ｽｷｯﾌﾟ", "名前", "備考", "SQL本体"};
    int[] sqlColumnWidth = new int[]{20, 50, 100, 150, 300};
    Vector vecSqlData = new Vector();
    SQLTableModel sqlTblModel = new SQLTableModel();
    int IDX_SQLINDX = 0;
    int IDX_SQLSKIP = 1;
    int IDX_SQLNAME = 2;
    int IDX_SQLDESC = 3;
    int IDX_SQLCOL = 4;

    private void initPanelSQLDesc() {
        btnAddSQL.setIcon(iconAddRow);
        btnAddSQL.setToolTipText("SQL文を追加");
        btnDeleteSQL.setIcon(iconRemoveRow);
        btnDeleteSQL.setToolTipText("SQL文を削除");
        btnUpSQL.setIcon(iconUparrow);
        btnUpSQL.setToolTipText("SQL文を上に移動");
        btnDownSQL.setIcon(iconDownarrow);
        btnDownSQL.setToolTipText("SQL文を下に移動");

        toolBarSQLList.setFloatable(false);
        toolBarSQLList.add(btnAddSQL);
        toolBarSQLList.add(btnDeleteSQL);
        toolBarSQLList.addSeparator();
        toolBarSQLList.add(btnUpSQL);
        toolBarSQLList.add(btnDownSQL);

        scpSQLList.getViewport().add(tblSQLList);

        panelSQLList.setBorder(new TitledBorder("SQL一覧"));
        panelSQLList.setLayout(new BorderLayout());
        panelSQLList.add(toolBarSQLList, BorderLayout.NORTH);
        panelSQLList.add(scpSQLList, BorderLayout.CENTER);

        tblSQLList.setModel(sqlTblModel);
        tblSQLList.setRowHeight(18);
        tblSQLList.setDefaultRenderer(Object.class, defaultCellRender);
        tblSQLList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSQLList.setColumnSelectionAllowed(false);
        tblSQLList.getTableHeader().setReorderingAllowed(false);
        tblSQLList.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        tblSQLList.getColumn(sqlHeaders[0]).setCellRenderer(new IndexCellRender());

        for (int i = 0; i < sqlColumnWidth.length; i++) {
            tblSQLList.getColumnModel().getColumn(i).setPreferredWidth(sqlColumnWidth[i]);
        }
        tblSQLList.getColumnModel().getColumn(IDX_SQLINDX).setMinWidth(sqlColumnWidth[0]);
        tblSQLList.getColumnModel().getColumn(IDX_SQLINDX).setMaxWidth(sqlColumnWidth[0]);
        tblSQLList.getColumnModel().getColumn(IDX_SQLSKIP).setMinWidth(sqlColumnWidth[1]);
        tblSQLList.getColumnModel().getColumn(IDX_SQLSKIP).setMaxWidth(sqlColumnWidth[1]);

        PJEditorTextField editorComp = new PJEditorTextField(String.class);
        editorComp.setBorder(null);
        PJDBCellEditor defaultCellEditor = new PJDBCellEditor(editorComp);
        defaultCellEditor.addCellEditorListener(sqlCellEditorListener);
        tblSQLList.getColumnModel().getColumn(IDX_SQLNAME).setCellEditor(defaultCellEditor);
        tblSQLList.getColumnModel().getColumn(IDX_SQLDESC).setCellEditor(defaultCellEditor);

        btnAddSQL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDefaultSQLRow();
            }
        });
        btnDeleteSQL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectSQLRow();
            }
        });
        btnUpSQL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveSQLRowUp();
            }
        });
        btnDownSQL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveSQLRowDown();
            }
        });

        tblSQLList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                setSQLEnable();

                Runnable laterDo = new Runnable() {
                    public void run() {
                        setSQLEditContent();
                    }
                };
                SwingUtilities.invokeLater(laterDo);
            }
        });
        setSQLEnable();
    }

    private void setSQLEnable() {
        if (tblSQLList.getSelectedRowCount() > 0) {
            btnDeleteSQL.setEnabled(true);
            btnUpSQL.setEnabled(true);
            btnDownSQL.setEnabled(true);
            txtSQLEdit.setEnabled(true);

        } else {
            btnDeleteSQL.setEnabled(false);
            btnUpSQL.setEnabled(false);
            btnDownSQL.setEnabled(false);
            txtSQLEdit.setEnabled(false);
        }
    }

    int selectedSQLRow = -1;

    private void setSQLEditContent() {
        int selectedRow = tblSQLList.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        selectedSQLRow = selectedRow;
        String sql = (String) tblSQLList.getValueAt(selectedRow, IDX_SQLCOL);
        txtSQLEdit.setText(sql);
        txtSQLEdit.resetDefaultFontStyle();
        setSQLLabel();
    }

    private void setSQLLabel() {
        int selectedRow = tblSQLList.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        String sqlName = (String) tblSQLList.getValueAt(selectedRow, IDX_SQLNAME);
        String sqlDesc = (String) tblSQLList.getValueAt(selectedRow, IDX_SQLDESC);
        lblSqlName.setText(sqlName);
        lblSqlDesc.setText(sqlDesc);
    }

    private void saveSQLEditContent() {
        if (selectedSQLRow < 0) {
            return;
        }
        String sql = txtSQLEdit.getText();
        tblSQLList.setValueAt(sql, selectedSQLRow, IDX_SQLCOL);
        tblSQLList.repaint();
    }

    private void initPanelSQLEdit() {

        ImageIcon iconSQLExecute = ImageManager.createImageIcon("sqlexecute.gif");
        ImageIcon iconUndo = ImageManager.createImageIcon("undo.gif");
        ImageIcon iconRedo = ImageManager.createImageIcon("redo.gif");
        ImageIcon iconSQLFormat = ImageManager.createImageIcon("sqlbuilder.gif");

        RollOverButton btnExecuteSQL = new RollOverButton(iconSQLExecute);
        RollOverButton btnUndo = new RollOverButton(iconUndo);
        RollOverButton btnRedo = new RollOverButton(iconRedo);
        RollOverButton btnSQLFormat = new RollOverButton(iconSQLFormat);

        JLabel lblNotice = new JLabel("　　パラメータ書き方：#9999#、'#abcd#'");
        lblNotice.setForeground(Color.blue);

        toolBarSQLEdit.setFloatable(false);
        toolBarSQLEdit.add(btnExecuteSQL);
        toolBarSQLEdit.addSeparator();
        toolBarSQLEdit.add(btnUndo);
        toolBarSQLEdit.add(btnRedo);
        toolBarSQLEdit.addSeparator();
        toolBarSQLEdit.add(btnSQLFormat);
        toolBarSQLEdit.addSeparator();
        toolBarSQLEdit.add(lblNotice);

        JPanel toolBarSQLIdenty = new JPanel();
        toolBarSQLIdenty.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 8));
        JPanel toolBarBoth = new JPanel();
        toolBarBoth.setLayout(new GridLayout(1, 2));
        toolBarBoth.add(toolBarSQLEdit);
        toolBarBoth.add(toolBarSQLIdenty);

        toolBarSQLIdenty.add(lblSqlName);
        toolBarSQLIdenty.add(lblSqlDesc);

        scpSQLEdit.getViewport().add(txtSQLEdit);
        panelSQLEdit.setLayout(new BorderLayout());
        panelSQLEdit.setBorder(new TitledBorder("SQL編集"));
        panelSQLEdit.add(toolBarBoth, BorderLayout.NORTH);
        panelSQLEdit.add(scpSQLEdit, BorderLayout.CENTER);

        txtSQLEdit.setEnabled(false);
        txtSQLEdit.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                saveSQLEditContent();
            }

            public void focusGained(FocusEvent e) {
            }
        });

        btnExecuteSQL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processCurrentSQL();
            }
        });
    }

    void processCurrentSQL() {
        String selectSql = txtSQLEdit.getText();
        if (selectSql != null && !selectSql.trim().equals("")) {
            PanelSQLResult panelResult = new PanelSQLResult();
            boolean b = panelResult.processResultShow(selectSql);

            if (b) {
                String title = lblSqlName.getText();
                int idx = tbpResult.indexOfTab(title);
                if (idx >= 0) {
                    tbpResult.setComponentAt(idx, panelResult);
                } else {
                    tbpResult.add(title, panelResult);
                    idx = tbpResult.indexOfTab(title);
                }
                tbpResult.setSelectedIndex(idx);
            }
        }
    }

    private void initPanelResult() {

        panelResult.setLayout(new BorderLayout());
        panelResult.setBorder(new TitledBorder("実行結果"));

        panelResult.add(tbpResult, BorderLayout.CENTER);
    }


    /**
     * add one default param row
     */
    private void addDefaultParamRow() {
        Vector vecOneRecord = new Vector();

        vecOneRecord.add("");
        vecOneRecord.add("");
        vecOneRecord.add("");
        int selectedRow = tblParam.getSelectedRow();
        if (selectedRow >= 0) {
            paramTblModel.addRow(tblParam.getSelectedRow(), vecOneRecord);
        } else {
            paramTblModel.addRow(vecOneRecord);
            selectedRow = paramTblModel.getRowCount() - 1;
        }

        tblParam.changeSelection(selectedRow, 0, false, false);
    }

    private void removeSelectParamRow() {
        if (tblParam.getSelectedRow() >= 0) {
            paramTblModel.removeRow(tblParam.getSelectedRow());
        }
        tblParam.repaint();
    }

    /**
     * move row down
     */
    void moveParamRowDown() {
        int orgRow = tblParam.getSelectedRow();

        if (orgRow >= 0) {
            paramTblModel.moveRow(tblParam.getSelectedRow(), false);
            if (orgRow < paramTblModel.getRowCount() - 1) {
                tblParam.changeSelection(orgRow + 1, 0, false, false);
            }
        }
    }

    /**
     * move row up
     */
    void moveParamRowUp() {
        int orgRow = tblParam.getSelectedRow();

        if (orgRow >= 0) {
            paramTblModel.moveRow(tblParam.getSelectedRow(), true);

            if (orgRow > 0) {
                tblParam.changeSelection(orgRow - 1, 0, false, false);
            }
        }
    }

    /**
     * add one default param row
     */
    private void addDefaultSQLRow() {
        Vector vecOneRecord = new Vector();

        vecOneRecord.add("");
        vecOneRecord.add(new Boolean(false));
        vecOneRecord.add("");
        vecOneRecord.add("");
        vecOneRecord.add("");
        int selectedRow = tblSQLList.getSelectedRow();
        if (selectedRow >= 0) {
            sqlTblModel.addRow(tblSQLList.getSelectedRow(), vecOneRecord);
        } else {
            sqlTblModel.addRow(vecOneRecord);
            selectedRow = sqlTblModel.getRowCount() - 1;
        }

        tblSQLList.changeSelection(selectedRow, 0, false, false);
    }

    private void removeSelectSQLRow() {
        if (tblSQLList.getSelectedRow() >= 0) {
            sqlTblModel.removeRow(tblSQLList.getSelectedRow());
        }
        tblSQLList.repaint();
    }

    /**
     * move row down
     */
    void moveSQLRowDown() {
        int orgRow = tblSQLList.getSelectedRow();

        if (orgRow >= 0) {
            sqlTblModel.moveRow(tblSQLList.getSelectedRow(), false);
            if (orgRow < sqlTblModel.getRowCount() - 1) {
                tblSQLList.changeSelection(orgRow + 1, 0, false, false);
            }
        }
    }

    /**
     * move row up
     */
    void moveSQLRowUp() {
        int orgRow = tblSQLList.getSelectedRow();

        if (orgRow >= 0) {
            sqlTblModel.moveRow(tblSQLList.getSelectedRow(), true);

            if (orgRow > 0) {
                tblSQLList.changeSelection(orgRow - 1, 0, false, false);
            }
        }
    }

    public void setParam(String type, String name) {
        this.currentType = type;
        this.currentReportName = name;
    }

    public void resetData() {
    }

    public void setRefreshable(boolean b) {
    }

    public boolean isReFreshable() {
        return false;
    }

    public void clearData() {
        // TODO
    }

    public void reLayout() {
        double dlMain = 0.6d;
        double dlSql = 0.5d;
        slpMain.setSize(new Dimension(this.getWidth(), this.getHeight() - 40));
        slpMain.setDividerLocation(dlMain);
        slpDesign.setSize(new Dimension(this.getWidth(), (int) ((this.getHeight() - 40) * dlMain)));
        slpDesign.setDividerLocation(dlSql);
        slpDesc.setSize(new Dimension(slpDesign.getWidth(),  (int) (slpDesign.getHeight() * dlSql)));
        slpDesc.setDividerLocation(300);
    }

    public void refreshDisplay() {
        // TODO データロード

        reLayout();
    }


    /**
     * テーブルのモードクラス定義
     */
    class ParamTableModel extends AbstractTableModel {
        // default constructor
        public ParamTableModel() {
            super();
        }

        public int getColumnCount() {
            return paramHeaders.length;
        }

        public int getRowCount() {
            return vecParamData.size();
        }

        public Object getValueAt(int row, int col) {
            return ((Vector) vecParamData.get(row)).get(col);
        }

        public String getColumnName(int column) {
            return paramHeaders[column];
        }

        public Class getColumnClass(int c) {
            Object value = getValueAt(0, c);
            if (value != null) {
                return value.getClass();
            }
            return Object.class;
        }

        public boolean isCellEditable(int row, int col) {
            return (col != 0);
        }

        public void setValueAt(Object aValue, int row, int column) {
            ((Vector) vecParamData.get(row)).set(column, aValue);
        }

        public void addRow(Vector rowData) {
            int rows = vecParamData.size();
            fireTableRowsInserted(0, rows + 1);
            vecParamData.add(rowData);
        }

        public void addRows(Vector rowDatas) {
            vecParamData.addAll(rowDatas);
            fireTableRowsInserted(0, getRowCount());
        }

        public void addRow(int index, Vector rowData) {
            int rows = vecParamData.size();
            fireTableRowsInserted(0, rows + 1);
            vecParamData.add(index, rowData);
        }

        public void removeRow(int row) {
            int rows = vecParamData.size();
            if (rows > 0){
                fireTableRowsDeleted(0, rows - 1);
                vecParamData.remove(row);
            }
        }

        public void removeAllRows() {
            int rows = vecParamData.size();
            if (rows > 0){
                fireTableRowsDeleted(0, rows - 1);
                vecParamData.removeAllElements();
            }
        }

        public void moveRow(int row, boolean up) {
            Vector vecRec = (Vector) vecParamData.get(row);

            fireTableRowsUpdated(0, vecParamData.size());

            if (up) {
                if (row > 0) {
                    Vector toVecRec = (Vector) vecParamData.get(row - 1);
                    vecParamData.set(row - 1, vecRec);
                    vecParamData.set(row, toVecRec);
                }
            } else {
                if (row < vecParamData.size() - 1) {
                    Vector toVecRec = (Vector) vecParamData.get(row + 1);
                    vecParamData.set(row + 1, vecRec);
                    vecParamData.set(row, toVecRec);
                }
            }
        }
    };

    /**
     * テーブルのモードクラス定義
     */
    class SQLTableModel extends AbstractTableModel {
        // default constructor
        public SQLTableModel() {
            super();
        }

        public int getColumnCount() {
            return sqlHeaders.length;
        }

        public int getRowCount() {
            return vecSqlData.size();
        }

        public Object getValueAt(int row, int col) {
            return ((Vector) vecSqlData.get(row)).get(col);
        }

        public String getColumnName(int column) {
            return sqlHeaders[column];
        }

        public Class getColumnClass(int c) {
            Object value = getValueAt(0, c);
            if (value != null) {
                return value.getClass();
            }
            return Object.class;
        }

        public boolean isCellEditable(int row, int col) {
            return (col != 0 && col != 4);
        }

        public void setValueAt(Object aValue, int row, int column) {
            ((Vector) vecSqlData.get(row)).set(column, aValue);
        }

        public void addRow(Vector rowData) {
            int rows = vecSqlData.size();
            fireTableRowsInserted(0, rows);
            vecSqlData.add(rowData);
        }

        public void addRows(Vector rowDatas) {
            vecSqlData.addAll(rowDatas);
            fireTableRowsInserted(0, getRowCount());
        }

        public void addRow(int index, Vector rowData) {
            int rows = vecSqlData.size();
            fireTableRowsInserted(0, rows);
            vecSqlData.add(index, rowData);
        }

        public void removeRow(int row) {
            int rows = vecSqlData.size();
            if (rows > 0){
                fireTableRowsDeleted(0, rows - 1);
                vecSqlData.remove(row);
            }
        }

        public void removeAllRows() {
            int rows = vecSqlData.size();
            if (rows > 0){
                fireTableRowsDeleted(0, rows - 1);
                vecSqlData.removeAllElements();
            }
        }

        public void moveRow(int row, boolean up) {
            Vector vecRec = (Vector) vecSqlData.get(row);

            fireTableRowsUpdated(0, vecSqlData.size());

            if (up) {
                if (row > 0) {
                    Vector toVecRec = (Vector) vecSqlData.get(row - 1);
                    vecSqlData.set(row - 1, vecRec);
                    vecSqlData.set(row, toVecRec);
                }
            } else {
                if (row < vecSqlData.size() - 1) {
                    Vector toVecRec = (Vector) vecSqlData.get(row + 1);
                    vecSqlData.set(row + 1, vecRec);
                    vecSqlData.set(row, toVecRec);
                }
            }
        }
    };

    /**
     * the first count button render
     */
    class IndexCellRender extends javax.swing.table.DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table,
                                               Object value,
                                               boolean isSelected,
                                               boolean hasFocus,
                                               int row,
                                               int column) {

            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            lbl.setText(String.valueOf(row + 1));

            return lbl;
        }
    }

    /**
     * CellEditorListener for deafult edotr
     */
    class SQLCellEditorListener implements CellEditorListener {
        public void editingStopped(ChangeEvent e) {
            setSQLLabel();
        }

        public void editingCanceled(ChangeEvent e) {}
    }

}
