package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jas.base.PJTableCellRender;
import org.jas.base.RollOverButton;
import org.jas.common.Refreshable;
import org.jas.gui.PanelReport.IndexCellRender;
import org.jas.model.ReportTemplate;
import org.jas.util.DateUtil;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author 張　学軍
 * @version 1.0
 */
@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public class PanelDataHistory extends JPanel implements Refreshable {

    ImageIcon iconRemoveRow = ImageManager.createImageIcon("deleterow.gif");
    ImageIcon iconImport = ImageManager.createImageIcon("importfile.gif");

    JPanel panelMain = new JPanel();
    JPanel panelBottom = new JPanel();

    JSplitPane slpMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JPanel panelHistory = new JPanel();
    JPanel panelResult = new JPanel();
    JTabbedPane tbpResult = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

    JButton btnAllExecute = new JButton();
    JButton btnExport = new JButton();
    JButton btnImport = new JButton();

    ButtonGroup grpExportType = new ButtonGroup();
    JRadioButton rdoExportExcel = new JRadioButton();
    JRadioButton rdoExportCsv = new JRadioButton();
    JCheckBox chkExportComment = new JCheckBox();

    String currentReportName = null;
    String currentType = "";

    ReportTemplate reportTemplate = new ReportTemplate();
    static File dataDir = new File("data");

    JTable tblHistory = new JTable();
    HistoryTableModel historyModel = new HistoryTableModel();
    PJTableCellRender defaultCellRender = new PJTableCellRender();
    String[] historyHeaders = new String[]{"No", "実行時間", "備考"};
    int[] historyWidth = new int[]{40, 160, 300};
    Vector vecHistoryData = new Vector();
    int IDX_HISTNDX = 0;
    int IDX_HISTTIM = 1;
    int IDX_HISTMEM = 2;

    static {
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public PanelDataHistory() {
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
        panelBottom.setLayout(null);
        this.add(panelMain, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
        panelBottom.setPreferredSize(new Dimension(this.getWidth(), 40));

        ImageIcon iconExport = ImageManager.createImageIcon("exporttofile.gif");
        btnExport.setText("エクスポート");
        btnExport.setIcon(iconExport);
        btnExport.setBounds(15, 10, 120, 25);
        btnExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportResultData();
            }
        });
        panelBottom.add(btnExport);

        rdoExportExcel.setText("Excel");
        rdoExportCsv.setText("CSV");
        grpExportType.add(rdoExportExcel);
        grpExportType.add(rdoExportCsv);
        rdoExportExcel.setSelected(true);

        rdoExportExcel.setBounds(140, 10, 60, 25);
        rdoExportCsv.setBounds(200, 10, 60, 25);
        panelBottom.add(rdoExportExcel);
        panelBottom.add(rdoExportCsv);

        chkExportComment.setText("論理名");
        chkExportComment.setBounds(260, 10, 60, 25);
        chkExportComment.setSelected(true);
        panelBottom.add(chkExportComment);

        btnImport.setText("リストア");
        btnImport.setIcon(iconImport);
        btnImport.setBounds(420, 10, 120, 25);
        btnImport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restoreResultData();
            }
        });
        panelBottom.add(btnImport);

        panelMain.add(slpMain, BorderLayout.CENTER);
        slpMain.setDividerSize(5);
        slpMain.setTopComponent(panelHistory);
        slpMain.setBottomComponent(panelResult);
        slpMain.setBorder(new BevelBorder(BevelBorder.LOWERED));

        initPanelHistory();
        initPanelResult();

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                reLayout();
            }
        });
    }

    private void initPanelHistory() {
        panelHistory.setLayout(new BorderLayout());
        panelHistory.setBorder(new TitledBorder("履歴一覧"));
        
        JToolBar toolBarHistory = new JToolBar();
        JScrollPane scpHistory = new JScrollPane();
        panelHistory.add(toolBarHistory, BorderLayout.NORTH);
        panelHistory.add(scpHistory, BorderLayout.CENTER);

        toolBarHistory.setFloatable(false);
        RollOverButton btnDeleteHistory = new RollOverButton();
        btnDeleteHistory.setIcon(iconRemoveRow);
        btnDeleteHistory.setToolTipText("履歴を削除");
        toolBarHistory.add(btnDeleteHistory);
        
        scpHistory.getViewport().add(tblHistory);
        tblHistory.setModel(historyModel);
        tblHistory.setRowHeight(18);
        tblHistory.setDefaultRenderer(Object.class, defaultCellRender);
        tblHistory.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblHistory.setColumnSelectionAllowed(false);
        tblHistory.getTableHeader().setReorderingAllowed(false);
        tblHistory.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        for (int i = 0; i < historyWidth.length; i++) {
            tblHistory.getColumnModel().getColumn(i).setPreferredWidth(historyWidth[i]);
        }
        tblHistory.getColumn(historyHeaders[IDX_HISTNDX]).setCellRenderer(new IndexCellRender());
        tblHistory.getColumn(historyHeaders[IDX_HISTNDX]).setMaxWidth(60);
        tblHistory.getColumn(historyHeaders[IDX_HISTTIM]).setMaxWidth(200);
    }

    private void initPanelResult() {
        panelResult.setLayout(new BorderLayout());
        panelResult.setBorder(new TitledBorder("結果詳細"));

        panelResult.add(tbpResult, BorderLayout.CENTER);
    }


    void processCurrentResult(String name) {
    }

    /**
     * add one history data
     */
    private void addHistoryData() {
    }

    private void removeSelectData() {
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
    }

    public void reLayout() {
        double dlMain = 0.6d;
        slpMain.setSize(new Dimension(this.getWidth(), this.getHeight() - 40));
        slpMain.setDividerLocation(dlMain);
    }

    public void refreshDisplay() {
        File templateFile = new File(dataDir, currentReportName + ".xml");
        if (templateFile.exists()) {
            InputStreamReader isr = null;
            try {
                isr = new InputStreamReader(new FileInputStream(templateFile), "UTF-8");
                XStream xs = new XStream();
                reportTemplate = (ReportTemplate) xs.fromXML(isr);
            } catch (Exception e) {
                MessageManager.showMessage("MCSTC203E", e.getMessage());
                e.printStackTrace();
            } finally {
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (Exception e) {}
                }
            }
        }
        restoreGUIData();
        reLayout();
    }

    private void restoreGUIData() {
    }

    private void saveGUIData() {
    }

    void exportResultData() {
        int cnt = tbpResult.getTabCount();
        if (cnt < 1) {
            MessageManager.showMessage("MCSTC303E", "");
            return;
        }

        if (rdoExportExcel.isSelected()) {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet(currentReportName);

            int startRow = 0;
            for (int i = 0; i < cnt; i++) {
                PanelSQLResult pnlResult =
                        (PanelSQLResult) tbpResult.getComponentAt(i);

                startRow = pnlResult.exportToExcel(sheet, startRow, chkExportComment.isSelected());
                if (i < cnt - 1) {
                    startRow++;
                }
            }

            FileOutputStream fos = null;
            try {
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyyMMddHHmmss");
                String ts = sdfDateTime.format(DateUtil.getSysDateTimestamp());
                final File tempFile = File.createTempFile(currentReportName + "_" + ts, ".xlsx");
                fos = new FileOutputStream(tempFile);
                wb.write(fos);
                wb.close();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            Desktop.getDesktop().open(tempFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MessageManager.showMessage("MCSTC203E", e.getMessage());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                MessageManager.showMessage("MCSTC203E", e.getMessage());
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {}
                }
            }

        } else if (rdoExportCsv.isSelected()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cnt; i++) {
                PanelSQLResult pnlResult =
                        (PanelSQLResult) tbpResult.getComponentAt(i);

                pnlResult.exportToCSV(sb, chkExportComment.isSelected());

                if (i < cnt - 1) {
                    sb.append("\r\n");
                }
            }
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(sb.toString());
            clipboard.setContents(stringSelection, stringSelection);

            MessageManager.showMessage("MCSTC304I", "");
        }
    }

    void restoreResultData() {
        tbpResult.removeAll();
    }

    /**
     * テーブルのモードクラス定義
     */
    class HistoryTableModel extends AbstractTableModel {
        // default constructor
        public HistoryTableModel() {
            super();
        }

        public int getColumnCount() {
            return historyHeaders.length;
        }

        public int getRowCount() {
            return vecHistoryData.size();
        }

        public Object getValueAt(int row, int col) {
            return ((Vector) vecHistoryData.get(row)).get(col);
        }

        public String getColumnName(int column) {
            return historyHeaders[column];
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
            ((Vector) vecHistoryData.get(row)).set(column, aValue);
        }

        public void addRow(Vector rowData) {
            int rows = vecHistoryData.size();
            fireTableRowsInserted(0, rows + 1);
            vecHistoryData.add(rowData);
        }

        public void addRows(Vector rowDatas) {
            vecHistoryData.addAll(rowDatas);
            fireTableRowsInserted(0, getRowCount());
        }

        public void addRow(int index, Vector rowData) {
            int rows = vecHistoryData.size();
            fireTableRowsInserted(0, rows + 1);
            vecHistoryData.add(index, rowData);
        }

        public void removeRow(int row) {
            int rows = vecHistoryData.size();
            if (rows > 0){
                fireTableRowsDeleted(0, rows - 1);
                vecHistoryData.remove(row);
            }
        }

        public void removeAllRows() {
            int rows = vecHistoryData.size();
            if (rows > 0){
                fireTableRowsDeleted(0, rows - 1);
                vecHistoryData.removeAllElements();
            }
        }

        public void moveRow(int row, boolean up) {
            Vector vecRec = (Vector) vecHistoryData.get(row);

            fireTableRowsUpdated(0, vecHistoryData.size());

            if (up) {
                if (row > 0) {
                    Vector toVecRec = (Vector) vecHistoryData.get(row - 1);
                    vecHistoryData.set(row - 1, vecRec);
                    vecHistoryData.set(row, toVecRec);
                }
            } else {
                if (row < vecHistoryData.size() - 1) {
                    Vector toVecRec = (Vector) vecHistoryData.get(row + 1);
                    vecHistoryData.set(row + 1, vecRec);
                    vecHistoryData.set(row, toVecRec);
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
    class HistoryCellEditorListener implements CellEditorListener {
        public void editingStopped(ChangeEvent e) {
            //TODO
        }

        public void editingCanceled(ChangeEvent e) {}
    }

}
