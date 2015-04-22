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
    public boolean processResultShow(String sql) {
        if (sql == null || sql.trim().equals("")) {
            return false;
        }

        try {
            Object value = DBParser.getResultByScript(Main.getMDIMain().getConnection(), sql, true);

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


}
