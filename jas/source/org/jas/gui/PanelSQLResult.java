package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jas.base.DBTableModel;
import org.jas.base.PJDBDataTable;
import org.jas.base.PJDBDataTableRowHeader;
import org.jas.base.PJDBDataTableRowHeaderRender;
import org.jas.base.PJTableCellRender;
import org.jas.base.PJTableRowHeaderModel;
import org.jas.common.PJConst;
import org.jas.db.DBParser;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;
import org.jas.util.StringUtil;

/**
 * SQLŽÀsŒ‹‰Ê
 *
 * @author zhangxj
 *
 */
@SuppressWarnings({"serial", "rawtypes"})
public class PanelSQLResult extends JPanel {

    ImageIcon iconEditOneRow = ImageManager.createImageIcon("editonerow.gif");
    JScrollPane scpResultTable = new JScrollPane();
    JTable tblScriptResult = new PJDBDataTable();
    PJTableCellRender dbTableCellRender = new PJTableCellRender();
    JButton btnLeftTopCorner = new JButton();
    PJDBDataTableRowHeader rowHeader = new PJDBDataTableRowHeader(tblScriptResult);
    TableSelectionRowListener selectionRowListener = new TableSelectionRowListener();

    String name = null;
    String desc = null;

    public PanelSQLResult() {

        try {
            jbInit();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        setLayout(new BorderLayout());
        this.add(scpResultTable, BorderLayout.CENTER);
        scpResultTable.getViewport().add(tblScriptResult, null);
        tblScriptResult.setDefaultRenderer(Object.class, dbTableCellRender);
        tblScriptResult.getSelectionModel().addListSelectionListener(selectionRowListener);
        tblScriptResult.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblScriptResult.setColumnSelectionAllowed(true);
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
    }

    /**
     * process query result show
     */
    public boolean processResultShow(String name, String desc, String sql) {
        if (sql == null || sql.trim().equals("")) {
            return false;
        }
        this.name = name;
        this.desc = desc;

        try {
            Object value = DBParser.getResultByScript(Main.getMDIMain().getConnection(), sql, true, name);

            if (value instanceof Vector) {
                Vector data = (Vector) value;
                DBTableModel dataModel = new DBTableModel(tblScriptResult, null, data, PJConst.TABLE_TYPE_READONLY);
                tblScriptResult.setModel(dataModel);

                PJTableRowHeaderModel rowHeaderModel = new PJTableRowHeaderModel(rowHeader, data);
                rowHeader.setModel(rowHeaderModel);

                if (data != null && data.size() > 4) {
                    rowHeaderModel.resetTable();
                    rowHeader.getColumnModel().getColumn(0).setCellRenderer(new PJDBDataTableRowHeaderRender());
                    dataModel.resetTable();
                }
                return true;
            } else {
                MessageManager.showMessage("MCSTC019E");
            }

        } catch (SQLException se) {
            MessageManager.showMessage("MCSTC202E", se.getMessage());
        }
        return false;
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

    public int exportToExcel(Sheet s, int startRow, boolean isExportComment) {
        CellStyle titleCs = s.getWorkbook().createCellStyle();
        Font f1 = s.getWorkbook().createFont();
        f1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        f1.setFontName("‚l‚r ‚oƒSƒVƒbƒN");
        titleCs.setFont(f1);

        Row r = s.createRow(startRow++);
        Cell c = r.createCell(0);
        c.setCellStyle(titleCs);

        String title = StringUtil.nvl(name);
        if (desc != null && !"".equals(desc.trim())) {
            title = title + "(" + StringUtil.nvl(desc) + ")";
        }
        c.setCellValue(title);

        TableModel model = tblScriptResult.getModel();
        if (model instanceof DBTableModel) {
            DBTableModel dbModel = (DBTableModel) model;
            Vector columnRemark = dbModel.getColumnComment();
            Vector columnName = dbModel.getRealColumnName();
            Vector typeData = dbModel.getColumnJavaType();

            CellStyle headerCs = s.getWorkbook().createCellStyle();
            headerCs.setBorderLeft(CellStyle.BORDER_THIN);
            headerCs.setBorderTop(CellStyle.BORDER_THIN);
            headerCs.setBorderRight(CellStyle.BORDER_THIN);
            headerCs.setBorderBottom(CellStyle.BORDER_THIN);
            headerCs.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            headerCs.setFillPattern(CellStyle.SOLID_FOREGROUND);

            // header
            if (isExportComment) {
                r = s.createRow(startRow++);
                for (int i = 0; i < columnRemark.size(); i++) {
                    setCellValue(r, i, (String) columnRemark.get(i), headerCs);
                }
            }
            r = s.createRow(startRow++);
            for (int i = 0; i < columnName.size(); i++) {
                setCellValue(r, i, (String) columnName.get(i), headerCs);
            }

            CellStyle dataCs = s.getWorkbook().createCellStyle();
            dataCs.setBorderLeft(CellStyle.BORDER_THIN);
            dataCs.setBorderTop(CellStyle.BORDER_THIN);
            dataCs.setBorderRight(CellStyle.BORDER_THIN);
            dataCs.setBorderBottom(CellStyle.BORDER_THIN);

            // data
            int rowCount = dbModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                r = s.createRow(startRow++);
                Vector rowData = dbModel.getRowData(i);
                for (int j = 0; j < columnName.size(); j++) {
                    String strValue = StringUtil.getStringValue((Class) typeData.get(j),
                            rowData.get(j), false);
                    setCellValue(r, j, strValue, dataCs);
                }
            }
        }

        return startRow;
    }

    private void setCellValue(Row r, int col, String o, CellStyle cs) {
        Cell c = r.createCell(col);
        c.setCellStyle(cs);
        c.setCellValue(o);
    }

    public void exportToCSV(StringBuilder sb, boolean isExportComment) {
        sb.append(StringUtil.nvl(name));
        if (desc != null && !"".equals(desc.trim())) {
            sb.append("(" + StringUtil.nvl(desc) + ")");
        }
        sb.append("\r\n");

        TableModel model = tblScriptResult.getModel();
        if (model instanceof DBTableModel) {
            DBTableModel dbModel = (DBTableModel) model;
            Vector columnRemark = dbModel.getColumnComment();
            Vector columnName = dbModel.getRealColumnName();
            Vector typeData = dbModel.getColumnJavaType();
            
            // header
            if (isExportComment) {
                for (int i = 0; i < columnRemark.size(); i++) {
                    sb.append(columnRemark.get(i));
                    if (i < columnRemark.size() - 1) {
                        sb.append("\t");
                    }
                }
                sb.append("\r\n");
            }
            for (int i = 0; i < columnName.size(); i++) {
                sb.append(columnName.get(i));
                if (i < columnName.size() - 1) {
                    sb.append("\t");
                }
            }
            sb.append("\r\n");
            
            // data
            int rowCount = dbModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                Vector rowData = dbModel.getRowData(i);
                for (int j = 0; j < columnName.size(); j++) {
                    String strValue = StringUtil.getStringValue((Class) typeData.get(j),
                            rowData.get(j), false);
                    sb.append(strValue);
                    if (j < columnName.size() - 1) {
                        sb.append("\t");
                    }
                }
                sb.append("\r\n");
            }
        }
    }
}
