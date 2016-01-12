package org.jas.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.jas.base.PJDialogBase;
import org.jas.base.PJEditorTextField;
import org.jas.common.PJConst;
import org.jas.db.DBParser;
import org.jas.util.FileManager;
import org.jas.util.MessageManager;
import org.jas.util.StringUtil;

/**
 * import table data from clipboard or csv file
 *
 * @author 張　学軍
 * @version 1.0
 */
public class DialogImportTableData extends PJDialogBase {
    JPanel panelContent = new JPanel();
    JPanel panelFrom = new JPanel();
    JPanel panelButton = new JPanel();
    JButton btnOK = new JButton();
    JButton btnCancel = new JButton();
    JButton btnAbort = new JButton();
    TitledBorder titledBorderFrom;
    PJEditorTextField txtFilePath = new PJEditorTextField();
    JButton btnBrowseFile = new JButton();
    JProgressBar progressBarImport = new JProgressBar();
    JRadioButton rdoImportFromFile = new JRadioButton();
    JRadioButton rdoImportFromClipBoard = new JRadioButton();
    JCheckBox chkIncludeHeader = new JCheckBox();
    ButtonGroup btnGroupFrom = new ButtonGroup();
    ButtonGroup btnGroupFormat = new ButtonGroup();
    ButtonActionListener buttonActionListener = new ButtonActionListener();
    JPanel panelFormat = new JPanel();
    TitledBorder titledBorder1;
    JRadioButton rdoFormatTab = new JRadioButton();
    JRadioButton rdoFormatCommar = new JRadioButton();
    JRadioButton rdoFormatSQL = new JRadioButton();
    JRadioButton rdoFormatOther = new JRadioButton();
    JTextField txtFormatOther = new JTextField();
    FileDialog fileDialog = null;
    String tableName;
    Vector columnName;
    Vector typeData;
    ThreadDoInsert threadDoInsert = null;
    boolean shouldBreak = false;
    boolean processedInsert = false;


    public DialogImportTableData(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public DialogImportTableData() {
        this(Main.getMDIMain(), "Import Table Data", true);
    }

    void jbInit() throws Exception {
        titledBorderFrom = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"From");
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Format");
        panelContent.setLayout(null);
        this.setResizable(false);
        panelContent.setMaximumSize(new Dimension(300, 270));
        panelContent.setMinimumSize(new Dimension(300, 270));
        panelContent.setPreferredSize(new Dimension(300, 270));
        panelFrom.setBorder(titledBorderFrom);
        panelFrom.setBounds(new Rectangle(10, 10, 282, 110));
        panelFrom.setLayout(null);
        panelButton.setBounds(new Rectangle(10, 240, 282, 25));
        panelButton.setLayout(null);
        btnOK.setMargin(new Insets(0, 0, 0, 0));
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setBounds(new Rectangle(52, 0, 55, 22));
        btnCancel.setBounds(new Rectangle(172, 0, 55, 22));
        btnCancel.setMargin(new Insets(0, 0, 0, 0));
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnAbort.setText("Abort");
        btnAbort.setMargin(new Insets(0, 0, 0, 0));
        btnAbort.setMnemonic('A');
        btnAbort.setBounds(new Rectangle(112, 0, 55, 22));
        btnAbort.setEnabled(false);
        txtFilePath.setBounds(new Rectangle(85, 23, 158, 21));
        btnBrowseFile.setMargin(new Insets(0, 0, 0, 0));
        btnBrowseFile.setText("...");
        btnBrowseFile.setBounds(new Rectangle(247, 23, 24, 22));
        progressBarImport.setBounds(new Rectangle(10, 210, 282, 15));
        rdoImportFromFile.setText("File");
        rdoImportFromFile.setBounds(new Rectangle(15, 25, 69, 18));
        rdoImportFromClipBoard.setText("Clipboard");
        rdoImportFromClipBoard.setBounds(new Rectangle(15, 46, 76, 25));
        chkIncludeHeader.setText("1行名はカラム名");
        chkIncludeHeader.setBounds(new Rectangle(15, 76, 156, 25));
        chkIncludeHeader.setSelected(false);
        panelFormat.setBorder(titledBorder1);
        panelFormat.setBounds(new Rectangle(10, 125, 282, 80));
        panelFormat.setLayout(null);
        rdoFormatCommar.setBounds(new Rectangle(13, 19, 120, 19));
        rdoFormatCommar.setText("カンマ(,)区切り");
        rdoFormatCommar.setToolTipText("");
        rdoFormatCommar.setMargin(new Insets(0, 0, 0, 0));
        rdoFormatTab.setToolTipText("");
        rdoFormatTab.setText("タブ区切り");
        rdoFormatTab.setBounds(new Rectangle(135, 19, 127, 19));
        rdoFormatSQL.setBounds(new Rectangle(10, 47, 110, 19));
        rdoFormatSQL.setText("インサートSQL");
        rdoFormatSQL.setToolTipText("");
        rdoFormatOther.setBounds(new Rectangle(135, 47, 80, 19));
        rdoFormatOther.setText("その他");
        rdoFormatOther.setToolTipText("");
        txtFormatOther.setBounds(new Rectangle(195, 47, 63, 21));
        getContentPane().add(panelContent);
        panelFrom.add(txtFilePath, null);
        panelFrom.add(btnBrowseFile, null);
        panelFrom.add(rdoImportFromFile, null);
        panelFrom.add(rdoImportFromClipBoard, null);
        panelFrom.add(chkIncludeHeader, null);
        panelButton.add(btnOK, null);
        panelButton.add(btnAbort, null);
        panelButton.add(btnCancel, null);
        panelContent.add(progressBarImport, null);
        panelContent.add(panelButton, null);
        panelContent.add(panelFormat, null);
        panelFormat.add(rdoFormatTab, null);
        panelFormat.add(rdoFormatCommar, null);
        panelFormat.add(txtFormatOther, null);
        panelFormat.add(rdoFormatSQL, null);
        panelFormat.add(rdoFormatOther, null);
        panelContent.add(panelFrom, null);

        btnGroupFrom.add(rdoImportFromFile);
        btnGroupFrom.add(rdoImportFromClipBoard);
        rdoImportFromFile.setSelected(true);

        btnGroupFormat.add(rdoFormatTab);
        btnGroupFormat.add(rdoFormatCommar);
        btnGroupFormat.add(rdoFormatSQL);
        btnGroupFormat.add(rdoFormatOther);
        rdoFormatCommar.setSelected(true);

        btnOK.addActionListener(buttonActionListener);
        btnCancel.addActionListener(buttonActionListener);
        btnAbort.addActionListener(buttonActionListener);
        btnBrowseFile.addActionListener(buttonActionListener);
        
        rdoImportFromClipBoard.setSelected(true);
        rdoFormatTab.setSelected(true);
        chkIncludeHeader.setSelected(true);
    }

    public void setVisible(boolean b) {
        if (b) {
            initLocation(Main.getMDIMain());
        }
        super.setVisible(b);
    }

    /**
     * get param from parent
     *
     */
    public void initResources(String tableName, Vector columnName, Vector typeData) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.typeData = typeData;
    }

    /**
     * get row datas from file or clipboard
     *
     */
    ArrayList getRowDatas() throws Exception {
        ArrayList rowDatas = new ArrayList();
        String content;

        if (rdoImportFromFile.isSelected()) {
            String fileName = txtFilePath.getText();
            content = FileManager.readInputStream(fileName).toString();
        } else {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            content = (String) clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor);
        }

        String sep = null;
        if (rdoFormatTab.isSelected()) {
            sep = "\t";
        } else if (rdoFormatCommar.isSelected()) {
            sep = ",";
        } else if (rdoFormatOther.isSelected()) {
            sep = txtFormatOther.getText();
        }

        ArrayList lines = StringUtil.getListFromString(content, "\n");
        for (int i = 0; i < lines.size(); i++) {
            String oneLine = (String) lines.get(i);

            if (!rdoFormatSQL.isSelected()) {
                ArrayList oneRow = StringUtil.getListFromString(oneLine, sep);
                rowDatas.add(oneRow);
            } else {
                rowDatas.add(oneLine);
            }
        }

        return rowDatas;
    }

    /**
     * process import data
     *
     */
    void processImport() {
        try {
            ArrayList rowDatas = getRowDatas();

            processedInsert = true;

            threadDoInsert = new ThreadDoInsert(rowDatas);
            threadDoInsert.start();
        } catch (Exception e) {
            MessageManager.showMessage("MCSTC202E", e.getMessage());
        }
    }

    /**
     * select import from file
     *
     */
    void selectImportFromFile() {
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

        txtFilePath.setText(dir + file);
    }


    /**
     * use a thread to do save
     *
     */
    class ThreadDoInsert extends Thread {
        ArrayList rowDatas = null;

        /**
         * default constructor
         */
        public ThreadDoInsert(ArrayList rowDatas) {
            this.rowDatas = rowDatas;
        }

        /**
         * start method
         */
        public void run() {
            btnOK.setEnabled(false);
            btnAbort.setEnabled(true);
            progressBarImport.setMaximum(rowDatas.size());

            Vector selectedColumns = columnName;
            Vector selectedTypes = typeData;
            int i = 0;
            if (chkIncludeHeader.isSelected()) {
                // convert selected columns
                if (rowDatas.size() > 0) {
                    selectedColumns = new Vector((ArrayList) rowDatas.get(0));
                }
                // convert selected types
                selectedTypes = new Vector(selectedColumns.size());
                for (int ii = 0; ii < selectedColumns.size(); ii++) {
                    int orgIndex = -1;
                    for (int jj = 0; jj < columnName.size(); jj++) {
                        String oneName = (String) columnName.get(jj);

                        if (oneName.equalsIgnoreCase((String) selectedColumns.get(ii))) {
                            orgIndex = jj;
                            break;
                        }
                    }

                    if (orgIndex == -1) {
                        selectedTypes.add(Object.class);
                    } else {
                        selectedTypes.add(typeData.get(orgIndex));
                    }
                }

                i++;
            }

            try {
                for (; i < rowDatas.size(); i++) {
                    if (shouldBreak) {
                        return;
                    }

                    try {
                        if (!rdoFormatSQL.isSelected()) {
                            ArrayList oneRow = (ArrayList) rowDatas.get(i);
                            Vector convertedRow = new Vector();

                            if (oneRow == null || oneRow.isEmpty()
                                    || oneRow.size() != selectedColumns.size()) {
                                System.out.println("LINE:" + (i + 1) + " DATA is wrong. " + oneRow);
                                continue;
                            }

                            for (int j = 0; j < oneRow.size(); j++) {
                                Object columnValue = oneRow.get(j);
                                Class type = (Class) selectedTypes.get(j);

                                convertedRow.add(StringUtil.getConvertValueOfType(type, columnValue));
                            }
                            // the last is key vector,here should be null
                            convertedRow.add(null);

                            boolean result = DBParser.insertRowData(Main.getMDIMain().getConnection(), selectedColumns,
                                                    selectedTypes, convertedRow, tableName);

                            if (!result) {
                                System.out.println("LINE:" + (i + 1) + " Can not insert into database.");
                            }
                        } else {
                            String sql = (String) rowDatas.get(i);
                            if (sql != null && !sql.trim().equals("")) {
                                DBParser.getResultByScript(Main.getMDIMain().getConnection(), sql, false, null);
                            }
                        }
                        progressBarImport.setValue(i);
                    } catch (Exception e) {
                        errorMessage = e.getMessage();
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                if (MessageManager.showMessage("MCSTC016Q", errorMessage) == 0) {
                                    shouldBreak = false;
                                } else {
                                    shouldBreak = true;
                                }
                            }
                        });
                    }
                }

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        closeWindow();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressBarImport.setValue(0);
                btnOK.setEnabled(true);
                btnAbort.setEnabled(false);
                shouldBreak = false;
            }
        }
    }

    String errorMessage = null;

    /**
     * close the window
     *
     */
    void closeWindow() {
        dispose();

        if (processedInsert) {
            shouldBreak = true;
            fireParamTransferEvent(null, PJConst.WINDOW_IMPORTTABLEDATA);
        }
    }

    /*************************************************************************************
    /* 事件処理用エリア
    /************************************************************************************/
    class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();

            if (obj == btnOK) {
                processImport();
            } else if (obj ==  btnCancel) {
                closeWindow();
            } else if (obj == btnAbort) {
                shouldBreak = true;
            } else if (obj == btnBrowseFile) {
                selectImportFromFile();
            }
        }
    }
}
