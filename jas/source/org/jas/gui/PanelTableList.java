package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import org.jas.model.TableDefineData;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;
import org.jas.util.ResourceManager;

/**
 *
 *
 * @author ���@�w�R
 * @version 1.0
 */

public class PanelTableList extends JPanel implements ParamTransferListener {
	ImageIcon iconTableClearFilterSort = ImageManager.createImageIcon("filtersortnothing.gif");
	ImageIcon iconTableFilterSort = ImageManager.createImageIcon("filtersort.gif");
	ImageIcon iconCopy = ImageManager.createImageIcon("copy.gif");
	ImageIcon iconDeleteAllTableData = ImageManager.createImageIcon("deletealltabledata.gif");
	JPopupMenu tableNamesPopupMenu = new JPopupMenu();
	JMenuItem mnuItemCopyTableName = new JMenuItem("�e�[�u�����R�s�[");
	JMenuItem mnuItemDeletaTableData = new JMenuItem("clear table data");
	ButtonMenuActionListener buttonMenuActionListener = new ButtonMenuActionListener();
	ShowPopupMouseListener showPopupMouseListener = new ShowPopupMouseListener();
	BorderLayout leftPanelBorderLayout = new BorderLayout();
	JTabbedPane tabbedPanelMain = new JTabbedPane();
	JPanel panelTables = new JPanel();
	JPanel panelViews = new JPanel();
	JPanel panelNewBeans = new JPanel();
	JList listTableNames = new JList();
	JScrollPane scpTableList = new JScrollPane();
	JLabel lblTableCounts = new JLabel();
	JScrollPane scpViewList = new JScrollPane();
	JLabel lblViewCounts = new JLabel();
	JList lstViewNames = new JList();
	JScrollPane scpNewBeanList = new JScrollPane();
	JLabel lblNewBeanCounts = new JLabel();
	JList lstNewBeanList = new JList();
	BorderLayout panelTablesBorderLayout = new BorderLayout();
	BorderLayout panelViewsBorderLayout = new BorderLayout();
	BorderLayout panelNewBeansBorderLayout = new BorderLayout();
	JPanel panelTop = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JComboBox cmbConnectionURL = new JComboBox();
	JToolBar toolBarTables = new JToolBar();
	RollOverButton btnTablesFilter = new RollOverButton();
    ImageIcon iconSearchTable = ImageManager.createImageIcon("editonerow.gif");
    JTextField txtSearchTable = new JTextField();
    RollOverButton btnSearchTable = new RollOverButton();
	PanelSQLBrowser parent;
	HashMap hideTableItems = new HashMap();
	PanelRight lastSelectTable = null;
	PanelRight lastSelectView = null;

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
		scpTableList.setPreferredSize(new Dimension(200, 400));
		scpTableList.setBorder(BorderFactory.createLoweredBevelBorder());
		listTableNames.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				listTableNames_SelectChanged(e);
			}
		});
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
		this.setPreferredSize(new Dimension(200, 400));
		this.setMinimumSize(new Dimension(200, 0));
		this.setLayout(leftPanelBorderLayout);
		lblTableCounts.setHorizontalAlignment(SwingConstants.CENTER);
		lblTableCounts.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTableCounts.setText("0 Tables");
		lblViewCounts.setHorizontalAlignment(SwingConstants.CENTER);
		lblViewCounts.setHorizontalTextPosition(SwingConstants.CENTER);
		lblViewCounts.setText("0 Views");
		lblNewBeanCounts.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewBeanCounts.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewBeanCounts.setText("0 Beans");
		scpViewList.setBorder(BorderFactory.createLoweredBevelBorder());
		scpNewBeanList.setBorder(BorderFactory.createLoweredBevelBorder());
		panelTop.setPreferredSize(new Dimension(265, 20));
		panelTop.setLayout(borderLayout1);
		cmbConnectionURL.setMaximumSize(new Dimension(125, 20));
		cmbConnectionURL.setMinimumSize(new Dimension(125, 20));
		cmbConnectionURL.setPreferredSize(new Dimension(125, 20));
		cmbConnectionURL.setVisible(false);
		leftPanelBorderLayout.setVgap(3);
		toolBarTables.setBorder(null);
		toolBarTables.setFloatable(false);
		this.add(tabbedPanelMain,  BorderLayout.CENTER);
		tabbedPanelMain.setPreferredSize(new Dimension(200, 400));
		tabbedPanelMain.setMinimumSize(new Dimension(200, 0));
		tabbedPanelMain.add(panelTables, tabbedPaneTitles[0]);
		panelTables.add(scpTableList, BorderLayout.CENTER);
		panelTables.add(lblTableCounts, BorderLayout.SOUTH);
		panelTables.add(toolBarTables, BorderLayout.NORTH);
		scpTableList.getViewport().add(listTableNames);
		btnTablesFilter.setIcon(iconTableClearFilterSort);
		btnTablesFilter.setToolTipText("filter tables");
		btnTablesFilter.addActionListener(buttonMenuActionListener);
		toolBarTables.add(btnTablesFilter);

        txtSearchTable.setToolTipText("�e�[�u�����N�C�b�N����");
        txtSearchTable.setMaximumSize(new Dimension((int) txtSearchTable.getMaximumSize().getWidth(), 20));
        txtSearchTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTable();
            }
        });
        txtSearchTable.setBackground(SystemColor.control);
        toolBarTables.add(txtSearchTable, null);
        btnSearchTable.setIcon(iconSearchTable);
        btnSearchTable.setToolTipText("�e�[�u�����N�C�b�N����");
        toolBarTables.add(btnSearchTable);
        btnSearchTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTable();
            }
        });
		
		tabbedPanelMain.add(panelViews,  tabbedPaneTitles[1]);
		tabbedPanelMain.add(panelNewBeans,  tabbedPaneTitles[2]);
		panelViews.add(scpViewList,  BorderLayout.CENTER);
		scpViewList.getViewport().add(lstViewNames, null);
		panelViews.add(lblViewCounts,  BorderLayout.SOUTH);
		panelNewBeans.add(scpNewBeanList, BorderLayout.CENTER);
		scpNewBeanList.getViewport().add(lstNewBeanList, null);
		panelNewBeans.add(lblNewBeanCounts,  BorderLayout.SOUTH);
		//this.add(panelTop, BorderLayout.NORTH);
		//panelTop.add(cmbConnectionURL, BorderLayout.CENTER);
		tabbedPanelMain.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tabbedPanelMain_stateChanged(e);
			}
		});

		tableNamesPopupMenu.add(mnuItemCopyTableName);
		//tableNamesPopupMenu.add(mnuItemDeletaTableData);

		mnuItemCopyTableName.setIcon(iconCopy);
		mnuItemCopyTableName.addActionListener(buttonMenuActionListener);
		mnuItemDeletaTableData.setIcon(iconDeleteAllTableData);
		mnuItemDeletaTableData.setToolTipText("Delete all the table data with filter");
		mnuItemDeletaTableData.addActionListener(buttonMenuActionListener);

		listTableNames.addMouseListener(showPopupMouseListener);
	}

	/*************************************************************************************
	/* �Ɩ��p�G���A
	/************************************************************************************/
	ChangeConnectionListener changeConnectionListener = new ChangeConnectionListener();
	PJTableListModel tableListModel = new PJTableListModel();
	PJTableListModel viewListModel = new PJTableListModel();
	PJTableListModel newBeanListModel = new PJTableListModel();

	String[] tabbedPaneTitles = {"�e�[�u��", "�r���[", "Bean"};


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
		initCmbConnectionURL();
		if (conn == null) {
			tableListModel.setDataSet(null);
			viewListModel.setDataSet(null);
		} else {
			ArrayList tableList = DBParser.getTableLists(conn);
			setListsValue(tableList);
		}
		setCountStatus();
		setTabbedStatus(0);
		listTableNames.clearSelection();
		lstViewNames.clearSelection();
		listTableNames.repaint();
		lstViewNames.repaint();

		ResourceManager.resetTableNameList(getTableList());
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
	 * parse name to lists
	 */
	private void setListsValue(ArrayList tableList) {
		ArrayList tableNameList = new ArrayList();
		ArrayList viewNameList = new ArrayList();

		for (int i=0; i<tableList.size(); i++) {
			TableDefineData tableData = (TableDefineData) tableList.get(i);

			if (tableData != null) {
				if (tableData.getTableType().equalsIgnoreCase(PJConst.TABLE_TYPES[0])) {
					tableNameList.add(tableData.getTableName());
				} else if (tableData.getTableType().equalsIgnoreCase(PJConst.TABLE_TYPES[1])) {
					viewNameList.add(tableData.getTableName());
				}
			}
		}

		tableListModel.setDataSet(tableNameList, null);
		viewListModel.setDataSet(viewNameList);

		setFilterTables(null);
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

		if (selectedValue != null) {
			PanelRight existsPanel = parent.getSelectedRightPanel();

			if (existsPanel == null || !selectedValue.equals(existsPanel.getTableName())) {
				existsPanel = new PanelRight();

				PanelColumnDesc panelColumnDesc = new PanelColumnDesc();
				panelColumnDesc.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
				existsPanel.panelColumnDesc = panelColumnDesc;

				PanelKeyReference panelKeyReference = new PanelKeyReference();
				panelKeyReference.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
				existsPanel.panelKeyReference = panelKeyReference;

				PanelBeanCreate panelBeanCreate = new PanelBeanCreate();
				panelBeanCreate.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
				existsPanel.panelBeanCreate = panelBeanCreate;

				PanelTableModify panelTableModify = new PanelTableModify();
				panelTableModify.setParam(PJConst.BEAN_TYPE_TABLE, selectedValue);
				existsPanel.panelTableModify = panelTableModify;

				existsPanel.setTableName(PJConst.BEAN_TYPE_TABLE, selectedValue);
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

			PanelBeanCreate existsBeanPanel = parent.getExistsRightBeanPanel(PJConst.BEAN_TYPE_NEWBEAN, selectedValue);
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
	/* ���������p�G���A
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
			}

			if (p.getX() + menuWidth > screenSize.getWidth()) {
				showX = showX - menuWidth;
			}
			if (p.getY() + menuHeight > screenSize.getHeight()) {
				showY = showY - menuHeight;
			}

			tableNamesPopupMenu.show((JComponent) obj, showX, showY);
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

	void listTableNames_keyPressed(KeyEvent e) {
		String selectedValue = (String) listTableNames.getSelectedValue();

		if (selectedValue != null) {
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_DELETE) {
				/*
				if (MessageManager.showMessage("MCSTC007Q") == 0) {
					deleteSelectedNewBean(PJConst.BEAN_TYPE_TABLE, selectedValue);
				}
				*/
			} else if (keyCode == KeyEvent.VK_C && e.isControlDown()) {
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

	private synchronized void copy(String content) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		JTextArea tempArea = new JTextArea(content);
		tempArea.selectAll();
		tempArea.copy();
	}

	/**
	 * �J�ډ�ʂ���̃C�x���g
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