package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jas.common.Refreshable;

/**
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class PanelRight extends JPanel {
	BorderLayout mainBorderLayout = new BorderLayout();
	JTabbedPane tabPanelMain = new JTabbedPane();
	PanelColumnDesc panelColumnDesc = null;
	PanelIndexInfos panelIndexInfos = null;
	PanelKeyReference panelKeyReference = null;
	PanelBeanCreate panelBeanCreate = null;
    PanelReport panelReport= null;
    PanelDataHistory panelHistory= null;
	PanelTableModify panelTableModify = null;
	int selectedIndex = 0;
	StateChangeListener stateChangeListener = new StateChangeListener();
	BorderLayout borderLayout1 = new BorderLayout();
	PanelRightToolBarPanel toolBarPanel = new PanelRightToolBarPanel();
	Border border1;
    String tableType = null;
	String tableName = null;
	static HashMap<String, Integer> selectedMap = new HashMap<String, Integer>();

	public PanelRight() {
		try {
			jbInit();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		border1 = BorderFactory.createEmptyBorder(2,0,0,0);
		this.setPreferredSize(new Dimension(600, 520));
		this.setLayout(mainBorderLayout);
		mainBorderLayout.setVgap(3);
		toolBarPanel.setBorder(border1);
		this.add(toolBarPanel, BorderLayout.NORTH);
		this.add(tabPanelMain,  BorderLayout.CENTER);
	}

	/**
	 * set table or view or bean label name.
	 *
	 */
	void setTableName(String tableType, String tableName) {
		this.tableName = tableName;
		this.tableType = tableType;
		toolBarPanel.lblTableName.setText(tableType + ": " + (tableName == null ? "" : tableName));
	}

	/**
	 * get current processed table name
	 *
	 */
	String getTableName() {
		return tableName;
	}

	/**
	 * if the connection is null,
	 * the tool bar should be disabled
	 */
	void disableToolBar() {
		toolBarPanel.btnRefreshRightPanel.setEnabled(false);
		toolBarPanel.btnRefreshTableList.setEnabled(false);
		toolBarPanel.btnClearFilterSort.setEnabled(false);
	}

	/**
	 * do layout the tab panel
	 *
	 */
	void packAll() {
		if (panelColumnDesc != null) {
			tabPanelMain.add(" Columns ", panelColumnDesc);
		}
        if (panelIndexInfos != null) {
            tabPanelMain.add("Indexes", panelIndexInfos);
        }
		if (panelKeyReference != null) {
			tabPanelMain.add("Constraints", panelKeyReference);
		}
		if (panelBeanCreate != null) {
			tabPanelMain.add("  Bean  ", panelBeanCreate);
		}
		if (panelTableModify != null) {
			tabPanelMain.add("  Data  ", panelTableModify);
		}
        if (panelReport != null) {
            tabPanelMain.add("  Report  ", panelReport);
        }
        if (panelHistory != null) {
            tabPanelMain.add("  History  ", panelHistory);
        }
		tabPanelMain.addChangeListener(stateChangeListener);
	}

	/**
	 * get currently selected component
	 */
	public Component getSelectComponent() {
		return tabPanelMain.getSelectedComponent();
	}

	/**
	 * set selected the same index panel
	 */
	void setSelected() {
	    selectedIndex = selectedMap.get(tableType) == null ? 0 : selectedMap.get(tableType);
		if (tabPanelMain.getTabCount() > selectedIndex) {
			tabPanelMain.setSelectedIndex(selectedIndex);
			refreshSelected();
		}
	}

	/**
	 * refresh selected panel
	 */
	void refreshSelected() {
		Object obj = tabPanelMain.getSelectedComponent();

		if (obj != null && obj instanceof Refreshable) {
			Refreshable refresh = ((Refreshable) obj);
			if (refresh.isReFreshable()) {
				refresh.setRefreshable(false);
				refresh.resetData();
			}
		}
	}

	/**
	 * reenable the to be refresh panel
	 */
	void enabledReFresh() {
		if (panelColumnDesc != null) {
			panelColumnDesc.setRefreshable(true);
		}
        if (panelIndexInfos != null) {
            panelIndexInfos.setRefreshable(true);
        }
		if (panelKeyReference != null) {
			panelKeyReference.setRefreshable(true);
		}
		if (panelTableModify != null) {
			panelTableModify.setRefreshable(true);
		}
	}

	/**
	 * for release memory
	 */
	void clearSelected() {
		if (panelColumnDesc != null) {
			panelColumnDesc.clearData();
		}
        if (panelIndexInfos != null) {
            panelIndexInfos.clearData();
        }
		if (panelKeyReference != null) {
			panelKeyReference.clearData();
		}
		if (panelTableModify != null) {
			panelTableModify.clearData();
		}
	}

	/**
	 * tab pane selected changed listener
	 *
	 */
	class StateChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			selectedIndex = tabPanelMain.getSelectedIndex();
			selectedMap.put(tableType, selectedIndex);
			refreshSelected();
		}
	}
}