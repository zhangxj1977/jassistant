package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jas.base.PJTableListModel;
import org.jas.base.RollOverButton;
import org.jas.common.PJConst;
import org.jas.common.ParamTransferEvent;
import org.jas.common.ParamTransferListener;
import org.jas.db.DBParser;
import org.jas.model.MyDBConnConfig;
import org.jas.model.TableDefineData;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;
import org.jas.util.ResourceManager;

import com.thoughtworks.xstream.XStream;

/**
 *
 *
 * @author 張　学軍
 * @version 1.0
 */

public class PanelTableList extends JPanel implements ParamTransferListener {
    ImageIcon iconTableClearFilterSort = ImageManager.createImageIcon("filtersortnothing.gif");
    ImageIcon iconTableFilterSort = ImageManager.createImageIcon("filtersort.gif");
    ImageIcon iconCopy = ImageManager.createImageIcon("copy.gif");
    ImageIcon iconDeleteAllTableData = ImageManager.createImageIcon("deletealltabledata.gif");
    ImageIcon iconAddFavor = ImageManager.createImageIcon("addfavor.gif");
    ImageIcon iconDelFavor = ImageManager.createImageIcon("delfavor.gif");
    ImageIcon iconClearFavor = ImageManager.createImageIcon("clearfavor.gif");
    JPopupMenu tableNamesPopupMenu = new JPopupMenu();
    JMenuItem mnuItemCopyTableName = new JMenuItem("テーブル名コピー");
    JMenuItem mnuItemDeletaTableData = new JMenuItem("clear table data");
    JMenuItem mnuItemAddFavorTableName = new JMenuItem("お気に入りに追加");
    JMenuItem mnuItemDeleteFavorTableName = new JMenuItem("お気に入りから削除");
    JMenuItem mnuItemClearFavorTableName = new JMenuItem("お気に入りをすべて削除");
    ButtonMenuActionListener buttonMenuActionListener = new ButtonMenuActionListener();
    ShowPopupMouseListener showPopupMouseListener = new ShowPopupMouseListener();
    BorderLayout leftPanelBorderLayout = new BorderLayout();
    JTabbedPane tabbedPanelMain = new JTabbedPane();
    JPanel panelTables = new JPanel();
    JPanel panelViews = new JPanel();
    JPanel panelNewBeans = new JPanel();
    JPanel panelReport = new JPanel();
    JList listTableNames = new JList();
    JScrollPane scpTableList = new JScrollPane();
    JLabel lblTableCounts = new JLabel();
    JScrollPane scpViewList = new JScrollPane();
    JLabel lblViewCounts = new JLabel();
    JList lstViewNames = new JList();
    JScrollPane scpNewBeanList = new JScrollPane();
    JLabel lblNewBeanCounts = new JLabel();
    JList lstNewBeanList = new JList();
    JList lstReportList = new JList();
    JLabel lblReportCounts = new JLabel();
    JScrollPane scpReportList = new JScrollPane();
    JPopupMenu reportPopupMenu = new JPopupMenu();
    JMenuItem mnuItemCopyReportName = new JMenuItem("レポート複製");
    BorderLayout panelTablesBorderLayout = new BorderLayout();
    BorderLayout panelViewsBorderLayout = new BorderLayout();
    BorderLayout panelNewBeansBorderLayout = new BorderLayout();
    BorderLayout panelReportBorderLayout = new BorderLayout();

    JToolBar toolBarReportTop = new JToolBar();
    RollOverButton btnNewReport = new RollOverButton();
    ImageIcon iconNewReport = ImageManager.createImageIcon("newreport.gif");
    RollOverButton btnDeleteReport = new RollOverButton();
    ImageIcon iconDeleteReport = ImageManager.createImageIcon("delete.gif");
    JPanel panelTop = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JComboBox cmbConnectionURL = new JComboBox();
    JToolBar toolBarTablesTop = new JToolBar();
    JToolBar toolBarTablesBottom = new JToolBar();
    RollOverButton btnTablesFilter = new RollOverButton();
    JCheckBox chkShowComment = new JCheckBox("論理名表示");
    ImageIcon iconSearchTable = ImageManager.createImageIcon("editonerow.gif");
    JTextField txtSearchTable = new JTextField();
    RollOverButton btnSearchTable = new RollOverButton();
    PanelSQLBrowser parent;
    HashMap hideTableItems = new HashMap();
    PanelRight lastSelectTable = null;
    PanelRight lastSelectView = null;
    static boolean isShowComment = false;

    public PanelTableList() {
        try {
            jbInit();
            initCmbConnectionURL();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setParent(PanelSQLBrowser parent) {
        this.parent = parent;
    }

    void jbInit() throws Exception {
        panelNewBeans.setLayout(panelNewBeansBorderLayout);
        panelViews.setLayout(panelViewsBorderLayout);
        panelTables.setLayout(panelTablesBorderLayout);
        panelReport.setLayout(panelReportBorderLayout);
        scpTableList.setPreferredSize(new Dimension(200, 400));
        scpTableList.setBorder(BorderFactory.createLoweredBevelBorder());
        listTableNames.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                listTableNames_SelectChanged(e);
            }
        });
        listTableNames.setCellRenderer(new MyListCellRenderer());
        listTableNames.setModel(tableListModel);
        listTableNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listTableNames.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                listTableNames_keyPressed(e);
            }
        });
        lstViewNames.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                lstViewNames_SelectChanged(e);
            }
        });
        lstViewNames.setModel(viewListModel);
        lstViewNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstViewNames.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lstViewNames_keyPressed(e);
            }
        });
        lstNewBeanList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                lstNewBeanList_SelectChanged(e);
            }
        });
        lstNewBeanList.setModel(newBeanListModel);
        lstNewBeanList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstNewBeanList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lstNewBeanList_keyPressed(e);
            }
        });
        lstReportList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                lstReportList_SelectChanged(e);
            }
        });
        lstReportList.setModel(reportListModel);
        lstReportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setPreferredSize(new Dimension(200, 400));
        this.setMinimumSize(new Dimension(200, 0));
        this.setLayout(leftPanelBorderLayout);
        lblTableCounts.setHorizontalTextPosition(SwingConstants.CENTER);
        lblTableCounts.setText("0 Tables");
        lblViewCounts.setHorizontalAlignment(SwingConstants.CENTER);
        lblViewCounts.setHorizontalTextPosition(SwingConstants.CENTER);
        lblViewCounts.setText("0 Views");
        lblNewBeanCounts.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewBeanCounts.setHorizontalTextPosition(SwingConstants.CENTER);
        lblNewBeanCounts.setText("0 Beans");
        lblReportCounts.setHorizontalAlignment(SwingConstants.CENTER);
        lblReportCounts.setHorizontalTextPosition(SwingConstants.CENTER);
        lblReportCounts.setText("0 Reports");
        scpViewList.setBorder(BorderFactory.createLoweredBevelBorder());
        scpNewBeanList.setBorder(BorderFactory.createLoweredBevelBorder());
        scpReportList.setBorder(BorderFactory.createLoweredBevelBorder());
        panelTop.setPreferredSize(new Dimension(265, 20));
        panelTop.setLayout(borderLayout1);
        cmbConnectionURL.setMaximumSize(new Dimension(125, 20));
        cmbConnectionURL.setMinimumSize(new Dimension(125, 20));
        cmbConnectionURL.setPreferredSize(new Dimension(125, 20));
        cmbConnectionURL.setVisible(false);
        leftPanelBorderLayout.setVgap(3);
        toolBarTablesTop.setBorder(null);
        toolBarTablesTop.setFloatable(false);
        toolBarTablesBottom.setBorder(null);
        toolBarTablesBottom.setFloatable(false);
        this.add(tabbedPanelMain,  BorderLayout.CENTER);
        tabbedPanelMain.setPreferredSize(new Dimension(200, 400));
        tabbedPanelMain.setMinimumSize(new Dimension(200, 0));
        tabbedPanelMain.add(panelTables, tabbedPaneTitles[0]);
        panelTables.add(scpTableList, BorderLayout.CENTER);
        panelTables.add(toolBarTablesBottom, BorderLayout.SOUTH);
        panelTables.add(toolBarTablesTop, BorderLayout.NORTH);

        scpTableList.getViewport().add(listTableNames);
        btnTablesFilter.setIcon(iconTableClearFilterSort);
        btnTablesFilter.setToolTipText("filter tables");
        btnTablesFilter.addActionListener(buttonMenuActionListener);
        toolBarTablesTop.add(btnTablesFilter);
        txtSearchTable.setToolTipText("テーブル名クイック検索");
        txtSearchTable.setMaximumSize(new Dimension((int) txtSearchTable.getMaximumSize().getWidth(), 20));
        txtSearchTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTable();
            }
        });
        txtSearchTable.setBackground(SystemColor.control);
        toolBarTablesTop.add(txtSearchTable, null);
        btnSearchTable.setIcon(iconSearchTable);
        btnSearchTable.setToolTipText("テーブル名クイック検索");
        toolBarTablesTop.add(btnSearchTable);
        btnSearchTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTable();
            }
        });
        chkShowComment.setSelected(isShowComment);
        chkShowComment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isShowComment = chkShowComment.isSelected();
                listTableNames.repaint();
            }
        });
        toolBarTablesBottom.add(chkShowComment, null);
        toolBarTablesBottom.add(lblTableCounts, null);
        lblTableCounts.setMaximumSize(new Dimension(150, 20));
        lblTableCounts.setHorizontalAlignment(SwingConstants.RIGHT);

        tabbedPanelMain.add(panelViews,  tabbedPaneTitles[1]);
        tabbedPanelMain.add(panelNewBeans,  tabbedPaneTitles[2]);
        tabbedPanelMain.add(panelReport,  tabbedPaneTitles[3]);
        panelViews.add(scpViewList,  BorderLayout.CENTER);
        scpViewList.getViewport().add(lstViewNames, null);
        panelViews.add(lblViewCounts,  BorderLayout.SOUTH);
        panelNewBeans.add(scpNewBeanList, BorderLayout.CENTER);
        scpNewBeanList.getViewport().add(lstNewBeanList, null);
        panelNewBeans.add(lblNewBeanCounts,  BorderLayout.SOUTH);

        panelReport.add(scpReportList, BorderLayout.CENTER);
        scpReportList.getViewport().add(lstReportList, null);
        panelReport.add(lblReportCounts,  BorderLayout.SOUTH);
        //this.add(panelTop, BorderLayout.NORTH);
        //panelTop.add(cmbConnectionURL, BorderLayout.CENTER);
        tabbedPanelMain.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                tabbedPanelMain_stateChanged(e);
            }
        });

        tableNamesPopupMenu.add(mnuItemCopyTableName);
        //tableNamesPopupMenu.add(mnuItemDeletaTableData);
        tableNamesPopupMenu.add(mnuItemAddFavorTableName);
        tableNamesPopupMenu.add(mnuItemDeleteFavorTableName);
        tableNamesPopupMenu.add(mnuItemClearFavorTableName);

        reportPopupMenu.add(mnuItemCopyReportName);
        mnuItemCopyReportName.setIcon(iconCopy);
        mnuItemCopyReportName.addActionListener(buttonMenuActionListener);

        btnNewReport.addActionListener(buttonMenuActionListener);
        btnNewReport.setMargin(new Insets(1, 1, 1, 1));
        btnNewReport.setIcon(iconNewReport);
        btnNewReport.setToolTipText("レポート新規作成");

        btnDeleteReport.addActionListener(buttonMenuActionListener);
        btnDeleteReport.setMargin(new Insets(1, 1, 1, 1));
        btnDeleteReport.setIcon(iconDeleteReport);
        btnDeleteReport.setToolTipText("レポート削除");
        panelReport.add(toolBarReportTop, BorderLayout.NORTH);
        toolBarReportTop.setBorder(null);
        toolBarReportTop.setFloatable(false);
        toolBarReportTop.add(btnNewReport);
        toolBarReportTop.add(btnDeleteReport);

        mnuItemCopyTableName.setIcon(iconCopy);
        mnuItemCopyTableName.addActionListener(buttonMenuActionListener);
        mnuItemDeletaTableData.setIcon(iconDeleteAllTableData);
        mnuItemDeletaTableData.setToolTipText("Delete all the table data with filter");
        mnuItemDeletaTableData.addActionListener(buttonMenuActionListener);

        mnuItemAddFavorTableName.setIcon(iconAddFavor);
        mnuItemDeleteFavorTableName.setIcon(iconDelFavor);
        mnuItemClearFavorTableName.setIcon(iconClearFavor);

        mnuItemAddFavorTableName.setToolTipText("お気に入りに追加し、先頭に表示されます。");
        mnuItemDeleteFavorTableName.setToolTipText("お気に入りから削除し、元の位置に表示されます。");
        mnuItemClearFavorTableName.setToolTipText("お気に入りすべて項目を削除します。");

        mnuItemAddFavorTableName.addActionListener(buttonMenuActionListener);
        mnuItemDeleteFavorTableName.addActionListener(buttonMenuActionListener);
        mnuItemClearFavorTableName.addActionListener(buttonMenuActionListener);

        listTableNames.addMouseListener(showPopupMouseListener);
        lstReportList.addMouseListener(showPopupMouseListener);

        refreshReportList();
    }

    /*************************************************************************************
    /* 業務用エリア
    /************************************************************************************/
    ChangeConnectionListener changeConnectionListener = new ChangeConnectionListener();
    PJTableListModel tableListModel = new PJTableListModel();
    PJTableListModel viewListModel = new PJTableListModel();
    PJTableListModel newBeanListModel = new PJTableListModel();
    PJTableListModel reportListModel = new PJTableListModel();

    // 元テーブルリスト
    ArrayList<TableDefineData> tableList = new ArrayList<TableDefineData>();
    // お気に入りリスト
    ArrayList<String> favaorTblList = new ArrayList<String>();

    String[] tabbedPaneTitles = {"テーブル", "ビュー", "Bean", "レポート"};


    /**
     * init conntions combobox
     */
    private void initCmbConnectionURL() {
        cmbConnectionURL.removeItemListener(changeConnectionListener);
        cmbConnectionURL.removeAllItems();
        ArrayList connectionList = ResourceManager.getPreviousConnections();

        if (connectionList != null) {
            for (int i = 0; i < connectionList.size(); i++) {
                cmbConnectionURL.addItem(connectionList.get(i));
            }
        }
        if (parent != null) {
            cmbConnectionURL.setSelectedItem(Main.getMDIMain().currentConnURL);
        } else {
            cmbConnectionURL.setSelectedItem(null);
        }

        cmbConnectionURL.addItemListener(changeConnectionListener);
    }

    private void initConfig() {
        restoreConfig();
    }

    /**
     * change connection listener
     */
    class ChangeConnectionListener implements ItemListener {
        public void itemStateChanged(ItemEvent event) {
            if (event.getStateChange() != event.SELECTED) {
                return;
            }

            if (MessageManager.showMessage("MCSTC012Q") != 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        cmbConnectionURL.removeItemListener(changeConnectionListener);
                        cmbConnectionURL.setSelectedItem(Main.getMDIMain().currentConnURL);
                        cmbConnectionURL.addItemListener(changeConnectionListener);
                    }
                });
                return;
            }

            String newConnURL = (String) cmbConnectionURL.getSelectedItem();

            Connection conn = null;
            if (newConnURL != null) {
                try {
                    conn = ResourceManager.getConnection(newConnURL);
                } catch (SQLException se) {
                    MessageManager.showMessage("MCSTC201E", se.getMessage());
                }
            }

            Main.getMDIMain().closeConnection();
            Main.getMDIMain().currentConnURL = newConnURL;
            Main.getMDIMain().setConnection(conn);
        }
    }

    /**
     * refresh the lists
     * if the conn is null then clear the lists
     */
    public void refreshTableList(Connection conn) throws SQLException {
        initConfig();
        initCmbConnectionURL();
        if (conn == null) {
            tableListModel.setDataSet(null);
            viewListModel.setDataSet(null);
        } else {
            tableList = DBParser.getTableLists(conn);
            setListsValue(tableList);
        }
        setCountStatus();
        setTabbedStatus(0);
        listTableNames.clearSelection();
        lstViewNames.clearSelection();
        listTableNames.repaint();
        lstViewNames.repaint();

        if (tableList != null) {
            String[] tblNames = new String[tableList.size()];
            for (int i = 0; i < tableList.size(); i++) {
                tblNames[i] = ((TableDefineData) tableList.get(i)).getTableName();
            }

            ResourceManager.resetTableNameList(tblNames);
        }
    }

    /**
     * refresh the report list
     */
    public void refreshReportList() throws SQLException {
        File configDir = new File(Main.configPath, "report");
        if (!configDir.exists()) {
            return;
        }

        ArrayList<String> reportList = new ArrayList<String>();
        String[] files = configDir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        for (String f : files) {
            f = f.substring(0, f.lastIndexOf(".xml"));
            reportList.add(f);
        }

        reportListModel.setDataSet(reportList);
        lstReportList.repaint();

        lblReportCounts.setText(String.valueOf(reportListModel.getSize()) + " " + tabbedPaneTitles[3]);
    }

    /**
     * new bean lists, the list does not need connect to database
     */
    public void createNewBean(String newBeanName) {
        Collection beanList = newBeanListModel.getDataSet();
        if (beanList == null) {
            ArrayList newBeanNameList = new ArrayList();
            newBeanListModel.setDataSet(newBeanNameList);
        }

        if (newBeanListModel.contains(newBeanName)) {
            lstNewBeanList.setSelectedValue(newBeanName, true);
        } else {
            newBeanListModel.add(newBeanName);
            lstNewBeanList.setSelectedValue(newBeanName, true);
        }
        setCountStatus();
        setTabbedStatus(1);
    }

    /**
     * new report lists
     */
    public void createReport() {
        String reportName = MessageManager.showInputDialog(
                "レポート名前を入力してください。", "名前入力");
        if (reportName == null || reportName.trim().equals("")) {
            return;
        }

        Collection reportList = reportListModel.getDataSet();
        if (reportList == null) {
            ArrayList newReportNameList = new ArrayList();
            reportListModel.setDataSet(newReportNameList);
        }

        if (reportListModel.contains(reportName)) {
            lstReportList.setSelectedValue(reportName, true);
        } else {
            reportListModel.add(reportName);
            lstReportList.setSelectedValue(reportName, true);
        }

        lblReportCounts.setText(String.valueOf(reportListModel.getSize()) + " " + tabbedPaneTitles[3]);
    }

    public void copyReport(String orinal) {
        String newReportName = MessageManager.showInputDialog(
                "新しいレポート名前を入力してください。", "名前入力");
        if (newReportName == null || newReportName.trim().equals("")) {
            return;
        }

        Collection reportList = reportListModel.getDataSet();
        if (reportList == null) {
            ArrayList newReportNameList = new ArrayList();
            reportListModel.setDataSet(newReportNameList);
        }

        if (reportListModel.contains(newReportName)) {
            lstReportList.setSelectedValue(newReportName, true);
        } else {

            PanelReport existsBeanPanel = (PanelReport)
                    parent.getExistsRightBeanPanel(PJConst.BEAN_TYPE_REPORT, orinal);
            if (existsBeanPanel != null) {
                boolean b = existsBeanPanel.copyReport(newReportName);
                if (b) {
                    reportListModel.add(newReportName);
                    lstReportList.setSelectedValue(newReportName, true);
                }
            }
        }

        lblReportCounts.setText(String.valueOf(reportListModel.getSize()) + " " + tabbedPaneTitles[3]);
    }

    public void deleteReport() {
        String selectedValue = (String) lstReportList.getSelectedValue();

        if (selectedValue != null) {
            if (MessageManager.showMessage("MCSTC305Q") != 0) {
                return;
            }
            PanelReport existsBeanPanel = (PanelReport)
                    parent.getExistsRightBeanPanel(PJConst.BEAN_TYPE_REPORT, selectedValue);
            if (existsBeanPanel != null) {
                existsBeanPanel.deleteReport();
                parent.removeRightPanel(PJConst.BEAN_TYPE_REPORT, selectedValue);
                reportListModel.remove(selectedValue);
                lstReportList.clearSelection();
                lstReportList.repaint();
            }
        }
        lblReportCounts.setText(String.valueOf(reportListModel.getSize()) + " " + tabbedPaneTitles[3]);
    }

    private void addFavorTable(String name) {
        String rawName = getNoCommentStr(name);
        if (!favaorTblList.contains(rawName)) {
            favaorTblList.add(rawName);
        }
        setListsValue(tableList);
        listTableNames.setSelectedValue(name, true);
        saveConfig();
    }

    private void delFavorTable(String name) {
        name = getNoCommentStr(name);
        favaorTblList.remove(name);
        setListsValue(tableList);
        saveConfig();
    }

    private void clearFavorTable() {
        favaorTblList.clear();
        setListsValue(tableList);
        saveConfig();
    }


    static File configDir = new File(Main.configPath, "conn");

    static {
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    private void saveConfig() {
        (new ThreadDoSave()).start();
    }
    
    class ThreadDoSave extends Thread {
        public void run() {
            MyDBConnConfig myConfig = new MyDBConnConfig();
            myConfig.setFavorTableList(favaorTblList);

            String connName = Main.getMDIMain().currentConnName;

            File conFile = new File(configDir, connName + ".xml");
            OutputStreamWriter fos = null;
            try {
                fos = new OutputStreamWriter(new FileOutputStream(conFile),  "UTF-8");
                XStream xs = new XStream();
                xs.toXML(myConfig, fos);
            } catch (final Exception e) {
                e.printStackTrace();
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {                   
                        public void run() {
                            MessageManager.showMessage("MCSTC203E", e.getMessage());
                        }
                    });
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {}
                }
            }
        }       
    }

    private void restoreConfig() {
        favaorTblList.clear();
        MyDBConnConfig myConfig = null;
        String connName = Main.getMDIMain().currentConnName;
        File conFile = new File(configDir, connName + ".xml");
        if (conFile.exists()) {
            InputStreamReader isr = null;
            try {
                isr = new InputStreamReader(new FileInputStream(conFile), "UTF-8");
                XStream xs = new XStream();
                myConfig = (MyDBConnConfig) xs.fromXML(isr);
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
            if (myConfig != null) {
                favaorTblList = myConfig.getFavorTableList();
            }
        }
    }

    /**
     * parse name to lists
     */
    private void setListsValue(ArrayList tableList) {
        ArrayList tableNameList = new ArrayList();
        ArrayList tableNameListFavaor = new ArrayList();
        ArrayList viewNameList = new ArrayList();

        for (int i=0; i<tableList.size(); i++) {
            TableDefineData tableData = (TableDefineData) tableList.get(i);

            if (tableData != null) {
                if (tableData.getTableType().equalsIgnoreCase(PJConst.TABLE_TYPES[0])) {
                    String showTableName = tableData.getTableName();
                    if (tableData.getComment() != null && !"".equals(tableData.getComment())) {
                        showTableName = tableData.getTableName() + "(" + tableData.getComment() + ")";  
                    }
                    if (favaorTblList.contains(tableData.getTableName())) {
                        tableNameListFavaor.add(showTableName);
                    } else {
                        tableNameList.add(showTableName);
                    }
                } else if (tableData.getTableType().equalsIgnoreCase(PJConst.TABLE_TYPES[1])) {
                    viewNameList.add(tableData.getTableName());
                }
            }
        }
        tableNameList.addAll(0, tableNameListFavaor);

        tableListModel.setDataSet(tableNameList, null);
        viewListModel.setDataSet(viewNameList);

        listTableNames.clearSelection();
        listTableNames.repaint();
    }

    /**
     * set hide items
     *
     */
    void setFilterTables(HashMap hideItems) {
        tableListModel.setHideItems(hideItems);
        listTableNames.clearSelection();
        listTableNames.repaint();

        if (hideItems != null && hideItems.size() > 0) {
            btnTablesFilter.setIcon(iconTableFilterSort);
        } else {
            btnTablesFilter.setIcon(iconTableClearFilterSort);
        }
    }

    int searchedIdx = -1;
    String preSearch = "";
    void searchTable() {
        String txtForSearch = txtSearchTable.getText();
        if ("".equals(txtForSearch.trim())) {
            preSearch = "";
            searchedIdx = -1;
            return;
        }
        int i = searchedIdx;
        if (!preSearch.equals(txtForSearch)) {
            i = -1;
        }
        preSearch = txtForSearch;

        searchedIdx = tableListModel.like(txtForSearch, i);
        if (searchedIdx >= 0) {
            listTableNames.setSelectedIndex(searchedIdx);
            listTableNames.ensureIndexIsVisible(searchedIdx);
        }
    }

    /**
     * set counts
     */
    private void setCountStatus() {
        lblTableCounts.setText(String.valueOf(tableListModel.getSize()) + " " + tabbedPaneTitles[0]);
        lblViewCounts.setText(String.valueOf(viewListModel.getSize()) + " " + tabbedPaneTitles[1]);
        lblNewBeanCounts.setText(String.valueOf(newBeanListModel.getSize()) + " " + tabbedPaneTitles[2]);
    }

    /**
     * set selected tabb
     */
    private void setTabbedStatus(int flag) {
        if (flag == 0) {
            tabbedPanelMain.setSelectedIndex(0);
        } else if (flag == 1) {
            tabbedPanelMain.setSelectedIndex(2);
        }
    }

    /**
     * refresh selected table panel
     */
    private void refreshSelectedTableName() {
        String selectedValue = (String) listTableNames.getSelectedValue();
        String fullName = selectedValue;

        if (selectedValue != null) {
            selectedValue = getNoCommentStr(selectedValue);
            PanelRight existsPanel = parent.getSelectedRightPanel();

            if (existsPanel == null || !selectedValue.equals(existsPanel.getTableName())) {
                existsPanel = new PanelRight();

                PanelColumnDesc panelColumnDesc = new PanelColumnDesc();
                panelColumnDesc.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
                existsPanel.panelColumnDesc = panelColumnDesc;

                PanelIndexInfos panelIndexInfos = new PanelIndexInfos();
                panelIndexInfos.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
                existsPanel.panelIndexInfos = panelIndexInfos;

                PanelKeyReference panelKeyReference = new PanelKeyReference();
                panelKeyReference.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
                existsPanel.panelKeyReference = panelKeyReference;

                PanelBeanCreate panelBeanCreate = new PanelBeanCreate();
                panelBeanCreate.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
                existsPanel.panelBeanCreate = panelBeanCreate;

                PanelTableModify panelTableModify = new PanelTableModify();
                panelTableModify.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
                existsPanel.panelTableModify = panelTableModify;

                existsPanel.setTableName(PJConst.BEAN_TYPE_TABLE, fullName);
                existsPanel.packAll();
            }
            parent.showRightPanel(existsPanel);

            if (lastSelectTable != existsPanel) {
                existsPanel.setSelected();

                if (lastSelectTable != null) {
                    lastSelectTable.clearSelected();
                }

                lastSelectTable = existsPanel;
            }
        } else {
            parent.showRightPanel(parent.rightPanel);
        }
    }

    /**
     * refresh selected view panel
     */
    private void refreshSelectedViewName() {
        String selectedValue = (String) lstViewNames.getSelectedValue();

        if (selectedValue != null) {
            PanelRight existsPanel = new PanelRight();

            PanelColumnDesc panelColumnDesc = new PanelColumnDesc();
            panelColumnDesc.setParam(PJConst.BEAN_TYPE_VIEW, selectedValue);
            existsPanel.panelColumnDesc = panelColumnDesc;

            PanelKeyReference panelKeyReference = new PanelKeyReference();
            panelKeyReference.setParam(PJConst.BEAN_TYPE_VIEW, selectedValue);
            existsPanel.panelKeyReference = panelKeyReference;

            PanelBeanCreate panelBeanCreate = new PanelBeanCreate();
            panelBeanCreate.setParam(PJConst.BEAN_TYPE_VIEW, selectedValue);
            existsPanel.panelBeanCreate = panelBeanCreate;

            PanelTableModify panelTableModify = new PanelTableModify();
            panelTableModify.setParam(PJConst.BEAN_TYPE_VIEW, selectedValue);
            existsPanel.panelTableModify = panelTableModify;

            existsPanel.setTableName(PJConst.BEAN_TYPE_VIEW, selectedValue);
            existsPanel.packAll();
            parent.showRightPanel(existsPanel);

            if (lastSelectView != existsPanel) {
                existsPanel.setSelected();

                if (lastSelectView != null) {
                    lastSelectView.clearSelected();
                }

                lastSelectView = existsPanel;
            }
        } else {
            parent.showRightPanel(parent.rightPanel);
        }
    }

    /**
     * refresh selected new bean panel
     */
    private void refreshSelectedNewBean() {
        String selectedValue = (String) lstNewBeanList.getSelectedValue();

        if (selectedValue != null) {
            PanelRight existsPanel = new PanelRight();

            PanelBeanCreate existsBeanPanel = (PanelBeanCreate)
                    parent.getExistsRightBeanPanel(PJConst.BEAN_TYPE_NEWBEAN, selectedValue);
            if (existsBeanPanel != null) {
                existsPanel.panelBeanCreate = existsBeanPanel;
            } else {
                PanelBeanCreate panelBeanCreate = new PanelBeanCreate();
                panelBeanCreate.setParam(PJConst.BEAN_TYPE_NEWBEAN, selectedValue);
                panelBeanCreate.refreshDisplay();
                existsPanel.panelBeanCreate = panelBeanCreate;
                parent.saveRightBeanPanel(PJConst.BEAN_TYPE_NEWBEAN, selectedValue, panelBeanCreate);
            }

            existsPanel.setTableName(PJConst.BEAN_TYPE_NEWBEAN, selectedValue);
            existsPanel.packAll();
            existsPanel.disableToolBar();
            parent.showRightPanel(existsPanel);
        } else {
            parent.showRightPanel(parent.rightPanel);
        }
    }

    /**
     * refresh selected report panel
     */
    private void refreshSelectedReport() {
        String selectedValue = (String) lstReportList.getSelectedValue();

        if (selectedValue != null) {
            PanelRight existsPanel = (PanelRight)
                    parent.getExistsRightBeanPanel(PJConst.BEAN_TYPE_REPORT, selectedValue);
            
            if (existsPanel == null) {
                existsPanel = new PanelRight(); 
                PanelReport panelReport = new PanelReport();
                panelReport.setSize(800, this.getHeight() - 40);
                panelReport.setParam(PJConst.BEAN_TYPE_REPORT, selectedValue);
                panelReport.refreshDisplay();
                existsPanel.panelReport = panelReport;

                //TODO
//                PanelDataHistory panelHistory = new PanelDataHistory();
//                panelHistory.setSize(800, this.getHeight() - 40);
//                panelHistory.setParam(PJConst.BEAN_TYPE_REPORT, selectedValue);
//                panelHistory.refreshDisplay();
//                existsPanel.panelHistory = panelHistory;

                parent.saveRightBeanPanel(PJConst.BEAN_TYPE_REPORT, selectedValue, existsPanel);
            }

            existsPanel.setTableName(PJConst.BEAN_TYPE_REPORT, selectedValue);
            existsPanel.packAll();
            existsPanel.disableToolBar();
            parent.showRightPanel(existsPanel);
        } else {
            parent.showRightPanel(parent.rightPanel);
        }
    }

    /**
     * delete selected new bean panel
     */
    private void deleteSelectedNewBean(String beanType, String beanName) {
        parent.removeRightPanel(beanType, beanName);

        if (PJConst.BEAN_TYPE_TABLE.equals(beanType)) {
            tableListModel.remove(beanName);
            listTableNames.clearSelection();
            listTableNames.repaint();
        } else if (PJConst.BEAN_TYPE_VIEW.equals(beanType)) {
            viewListModel.remove(beanName);
            lstViewNames.clearSelection();
            lstViewNames.repaint();
        } else if (PJConst.BEAN_TYPE_NEWBEAN.equals(beanType)) {
            newBeanListModel.remove(beanName);
            lstNewBeanList.clearSelection();
            lstNewBeanList.repaint();
        }

        setCountStatus();
    }

    /*************************************************************************************
    /* 事件処理用エリア
    /************************************************************************************/
    /**
     * button action
     */
    class ButtonMenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object item = e.getSource();

            if (item == btnTablesFilter) {
                showTablesFilter();
            } else if (item == mnuItemDeletaTableData) {
                deleteSelectedTableData();
            } else if (item == mnuItemCopyTableName) {
                String selectedValue = (String) listTableNames.getSelectedValue();
                copy(selectedValue);
            } else if (item == btnNewReport) {
                createReport();
            } else if (item == btnDeleteReport) {
                deleteReport();
            } else if (item == mnuItemCopyReportName) {
                String selectedValue = (String) lstReportList.getSelectedValue();
                copyReport(selectedValue);
            } else if (item == mnuItemAddFavorTableName) {
                String selectedValue = (String) listTableNames.getSelectedValue();
                addFavorTable(selectedValue);
            } else if (item == mnuItemDeleteFavorTableName) {
                String selectedValue = (String) listTableNames.getSelectedValue();
                delFavorTable(selectedValue);
            } else if (item == mnuItemClearFavorTableName) {
                clearFavorTable();
            }
        }
    }

    
    /**
     * table list mouse click event
     * show popupmenu
     *
     */
    class ShowPopupMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            showPopupMenu(e);
        }
    }

    /**
     * show filter tables dialog
     *
     */
    void showTablesFilter() {
        DialogFilterTables dialogFilterTables = new DialogFilterTables();
        dialogFilterTables.initResources(tableListModel.getDataSet(), tableListModel.getHideItems());
        dialogFilterTables.addParamTransferListener(this);
        dialogFilterTables.setVisible(true);
        dialogFilterTables.removeParamTransferListener(this);
    }

    /**
     * clear selected table data
     *
     */
    void deleteSelectedTableData() {
        String selectedValue = (String) listTableNames.getSelectedValue();

        if (selectedValue != null) {
            PanelRight existsPanel = parent.getSelectedRightPanel();
            if (existsPanel != null) {
                PanelTableModify panelTableModify = existsPanel.panelTableModify;

                if (panelTableModify != null) {
                    panelTableModify.deleteAllData();
                }
            }
        }
    }

    /**
     * show popup menu by right mouse click
     */
    void showPopupMenu(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            Object obj = e.getSource();

            if (obj == listTableNames) {
                int orgSelectedIndex = listTableNames.getSelectedIndex();
                int clickIndex = listTableNames.locationToIndex(e.getPoint());

                if (clickIndex != orgSelectedIndex) {
                    listTableNames.setSelectedIndex(clickIndex);
                }
            } else if (obj == lstReportList) {
                int orgSelectedIndex = lstReportList.getSelectedIndex();
                int clickIndex = lstReportList.locationToIndex(e.getPoint());

                if (clickIndex != orgSelectedIndex) {
                    lstReportList.setSelectedIndex(clickIndex);
                }
            }

            // avoid out of window, compute the show point
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Point p = e.getPoint();
            SwingUtilities.convertPointToScreen(p, (Component) obj);
            int showX = e.getX();
            int showY = e.getY();
            int menuHeight = 0;
            int menuWidth = 0;

            if (obj == listTableNames) {
                menuHeight = tableNamesPopupMenu.getHeight();
                menuWidth = tableNamesPopupMenu.getWidth();
            } else if (obj == lstReportList) {
                menuHeight = reportPopupMenu.getHeight();
                menuWidth = reportPopupMenu.getWidth();
            }

            if (p.getX() + menuWidth > screenSize.getWidth()) {
                showX = showX - menuWidth;
            }
            if (p.getY() + menuHeight > screenSize.getHeight()) {
                showY = showY - menuHeight;
            }

            if (obj == listTableNames) {
                tableNamesPopupMenu.show((JComponent) obj, showX, showY);
                
                mnuItemAddFavorTableName.setEnabled(false);
                mnuItemDeleteFavorTableName.setEnabled(false);

                String selectedValue = (String) listTableNames.getSelectedValue();
                String name = getNoCommentStr(selectedValue);
                if (!favaorTblList.contains(name)) {
                    mnuItemAddFavorTableName.setEnabled(true);
                } else {
                    mnuItemDeleteFavorTableName.setEnabled(true);
                }
            } else if (obj == lstReportList) {
                reportPopupMenu.show((JComponent) obj, showX, showY);
            }
        }
    }

    /**
     * select tables action
     *
     */
    void listTableNames_SelectChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            refreshSelectedTableName();
        }
    }

    void lstViewNames_SelectChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            refreshSelectedViewName();
        }
    }

    void lstNewBeanList_SelectChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            refreshSelectedNewBean();
        }
    }

    void lstReportList_SelectChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            refreshSelectedReport();
        }
    }

    void listTableNames_keyPressed(KeyEvent e) {
        String selectedValue = (String) listTableNames.getSelectedValue();

        if (selectedValue != null) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_C && e.isControlDown()) {
                copy(selectedValue);
            }
        }
    }

    void lstViewNames_keyPressed(KeyEvent e) {
        String selectedValue = (String) lstViewNames.getSelectedValue();

        if (selectedValue != null) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_DELETE) {
                if (MessageManager.showMessage("MCSTC007Q") == 0) {
                    deleteSelectedNewBean(PJConst.BEAN_TYPE_VIEW, selectedValue);
                }
            } else if (keyCode == KeyEvent.VK_C && e.isControlDown()) {
                copy(selectedValue);
            }
        }
    }

    void lstNewBeanList_keyPressed(KeyEvent e) {
        String selectedValue = (String) lstNewBeanList.getSelectedValue();

        if (selectedValue != null) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_DELETE) {
                if (MessageManager.showMessage("MCSTC007Q") == 0) {
                    deleteSelectedNewBean(PJConst.BEAN_TYPE_NEWBEAN, selectedValue);
                }
            } else if (keyCode == KeyEvent.VK_C && e.isControlDown()) {
                copy(selectedValue);
            }
        }
    }

    void tabbedPanelMain_stateChanged(ChangeEvent e) {
        refreshSelectdRightPanel();
    }

    public void refreshSelectdRightPanel() {
        int selectedIndex = tabbedPanelMain.getSelectedIndex();

        Main.getMDIMain().setColumnOperationStatus(false);

        if (selectedIndex == 0) {
            refreshSelectedTableName();
        } else if (selectedIndex == 1) {
            refreshSelectedViewName();
        } else if (selectedIndex == 2) {
            refreshSelectedNewBean();
        } else if (selectedIndex == 3) {
            refreshSelectedReport();
        }
    }

    String[] getTableList() {
        ArrayList list = new ArrayList();
        Collection tableCollection = tableListModel.getDataSet();
        Collection viewCollection = viewListModel.getDataSet();

        if (tableCollection != null && !tableCollection.isEmpty()) {
            list.addAll(tableCollection);
        }
        if (viewCollection != null && !viewCollection.isEmpty()) {
            list.addAll(viewCollection);
        }

        String[] nameList = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            nameList[i] = (String) list.get(i);
        }

        return nameList;
    }

    class MyListCellRenderer extends DefaultListCellRenderer {

        /* (非 Javadoc)
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            String noCommentStr = getNoCommentStr((String) value);
            if (!isShowComment) {
                value = noCommentStr;
            }

            Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (favaorTblList.contains(noCommentStr)) {
                if (isSelected) {
                    comp.setBackground(list.getSelectionBackground());
                    comp.setForeground(list.getSelectionForeground());
                } else {
                    comp.setBackground(list.getBackground());
                    comp.setForeground(Color.red);
                }
            }

            return comp;
        }
    }

    private String getNoCommentStr(String content) {
        if (content != null) {
            int pos = content.indexOf("(");
            if (pos > 0) {
                content = content.substring(0, pos);
            }
        }
        return content;
    }

    private synchronized void copy(String content) {
        if (!isShowComment) {
            content = getNoCommentStr(content);
        }

        JTextArea tempArea = new JTextArea(content);
        tempArea.selectAll();
        tempArea.copy();
    }

    /**
     * 遷移画面からのイベント
     *
     * @param pe ParamTransferEvent
     */
    public void paramTransfered(ParamTransferEvent pe) {
        int opFlag = pe.getOpFlag();

        switch (opFlag) {
            case PJConst.WINDOW_FILTERTABLES:
                Object param = (Object) pe.getParam();
                setFilterTables((HashMap) param);
                break;
            default:
                break;
        }
    }
}