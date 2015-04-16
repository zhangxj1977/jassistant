package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.jas.common.PJConst;
import org.jas.util.MessageManager;

/**
 *
 * @author 張　学軍
 * @version 1.0
 */
public class PanelSQLBrowser extends JPanel {
	BorderLayout borderLayoutMain = new BorderLayout();
	JSplitPane dataMainSplitPane = new JSplitPane();
	PanelTableList leftPanel = new PanelTableList();
	PanelRight rightPanel = new PanelRight();
	Border borderMainSplit = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.gray,Color.white,Color.white,new Color(103, 101, 98));

	public PanelSQLBrowser() {
		try {
			jbInit();
			initUI();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		this.setLayout(borderLayoutMain);
		this.add(dataMainSplitPane, BorderLayout.CENTER);
		dataMainSplitPane.setVisible(true);
		//dataMainSplitPane.setDividerLocation(200);
		dataMainSplitPane.setBorder(borderMainSplit);
		dataMainSplitPane.setDividerSize(5);
		dataMainSplitPane.setLeftComponent(leftPanel);
		dataMainSplitPane.setRightComponent(rightPanel);
        dataMainSplitPane.setPreferredSize(new Dimension(200, dataMainSplitPane.getHeight()));
	}

	void initUI() {
		leftPanel.setParent(this);

		if (Main.getMDIMain().getConnection() == null) {
			rightPanel.disableToolBar();
		}

		tabbedPanelMap.put(PJConst.BEAN_TYPE_TABLE, panelTablesColumnDescMap);
		tabbedPanelMap.put(PJConst.BEAN_TYPE_VIEW, panelViewsColumnDescMap);
		tabbedPanelMap.put(PJConst.BEAN_TYPE_NEWBEAN, panelNewBeansColumnDescMap);
        tabbedPanelMap.put(PJConst.BEAN_TYPE_REPORT, panelReportMap);
	}

	/*************************************************************************************
	/* 業務用エリア
	/************************************************************************************/
	HashMap tabbedPanelMap = new HashMap();
	HashMap panelTablesColumnDescMap = new HashMap();
	HashMap panelViewsColumnDescMap = new HashMap();
	HashMap panelNewBeansColumnDescMap = new HashMap();
    HashMap panelReportMap = new HashMap();


	/**
	 * create new java bean that does not connect to database
	 */
	public void createNewBean(String beanName) {
		leftPanel.createNewBean(beanName);
	}

	/**
	 * get exists right panel, previous table description has been cached.
	 */
	public JPanel getExistsRightBeanPanel(String beanType, String beanName) {
		HashMap tableMap = (HashMap) tabbedPanelMap.get(beanType);

		if (tableMap != null) {
			return (JPanel) tableMap.get(beanName);
		}

		return null;
	}

	/**
	 * delete one right panel
	 */
	public void removeRightPanel(String beanType, String beanName) {
		HashMap tableMap = (HashMap) tabbedPanelMap.get(beanType);

		if (tableMap != null) {
			tableMap.remove(beanName);
		}

		dataMainSplitPane.setRightComponent(rightPanel);
	}

	/**
	 * cached new right bean panel.
	 */
	public void saveRightBeanPanel(String beanType, String beanName, JPanel newPanel) {
		HashMap tableMap = (HashMap) tabbedPanelMap.get(beanType);

		if (tableMap != null) {
			tableMap.put(beanName, newPanel);
		}
	}

	/**
	 * show the right panel.
	 */
	public void showRightPanel(PanelRight newPanel) {
		dataMainSplitPane.setRightComponent(newPanel);
        dataMainSplitPane.setDividerLocation(dataMainSplitPane.getDividerLocation());
	}

	/**
	 * get current selected right panel
	 *
	 */
	public PanelRight getSelectedRightPanel() {
		PanelRight rightPanel = (PanelRight) dataMainSplitPane.getRightComponent();
		return rightPanel;
	}


	public void panelBeanCreateCopy() {
		PanelRight rightPanel = (PanelRight) dataMainSplitPane.getRightComponent();

		if (rightPanel != null) {
			PanelBeanCreate panelBeanCreate = rightPanel.panelBeanCreate;

			if (panelBeanCreate != null) {
				panelBeanCreate.copy_Performed();
			}
		}
	}

	public void panelBeanCreatePaste() {
		PanelRight rightPanel = (PanelRight) dataMainSplitPane.getRightComponent();

		if (rightPanel != null) {
			PanelBeanCreate panelBeanCreate = rightPanel.panelBeanCreate;

			if (panelBeanCreate != null) {
				panelBeanCreate.paste_Performed();
			}
		}
	}

	public void panelBeanCreateCut() {
		PanelRight rightPanel = (PanelRight) dataMainSplitPane.getRightComponent();

		if (rightPanel != null) {
			PanelBeanCreate panelBeanCreate = rightPanel.panelBeanCreate;

			if (panelBeanCreate != null) {
				panelBeanCreate.cut_Performed();
			}
		}
	}

	public void panelBeanCreateDelete() {
		PanelRight rightPanel = (PanelRight) dataMainSplitPane.getRightComponent();

		if (rightPanel != null) {
			PanelBeanCreate panelBeanCreate = rightPanel.panelBeanCreate;

			if (panelBeanCreate != null) {
				panelBeanCreate.delete_Performed();
			}
		}
	}

	/**
	 * refresh table name list
	 */
	public void refreshTableNameList(Connection conn) {
		try {
			leftPanel.refreshTableList(conn);
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC202E", se.getMessage());
		}
	}


	/**
	 * clear all cached right panels
	 */
	public void cleanAllRightPanel() {
		showRightPanel(rightPanel);
		panelTablesColumnDescMap.clear();
		panelViewsColumnDescMap.clear();
	}


	public void updateAllPanelUI() {
		SwingUtilities.updateComponentTreeUI(leftPanel);
		SwingUtilities.updateComponentTreeUI(rightPanel);
		leftPanel.repaint();

		Iterator beanTypeIterator = tabbedPanelMap.keySet().iterator();
		while (beanTypeIterator.hasNext()) {
			HashMap tableMap = (HashMap) tabbedPanelMap.get(beanTypeIterator.next());

			if (tableMap != null) {
				Iterator listIterator = tableMap.keySet().iterator();
				while (listIterator.hasNext()) {
					PanelRight rightPanel = (PanelRight) tableMap.get(listIterator.next());
					updateOneRightPanelUI(rightPanel);
				}
			}
		}
	}

	private void updateOneRightPanelUI(PanelRight rightPanel) {
		if (rightPanel != null) {
			SwingUtilities.updateComponentTreeUI(rightPanel);
			if (rightPanel.toolBarPanel != null) {
				SwingUtilities.updateComponentTreeUI(rightPanel.toolBarPanel);
				rightPanel.toolBarPanel.repaint();
			}
			if (rightPanel.panelColumnDesc != null) {
				SwingUtilities.updateComponentTreeUI(rightPanel.panelColumnDesc);
				rightPanel.panelColumnDesc.repaint();
			}
            if (rightPanel.panelIndexInfos != null) {
                SwingUtilities.updateComponentTreeUI(rightPanel.panelIndexInfos);
                rightPanel.panelIndexInfos.repaint();
            }
			if (rightPanel.panelKeyReference != null) {
				SwingUtilities.updateComponentTreeUI(rightPanel.panelKeyReference);
				rightPanel.panelKeyReference.repaint();
			}
			if (rightPanel.panelBeanCreate != null) {
				SwingUtilities.updateComponentTreeUI(rightPanel.panelBeanCreate);
				rightPanel.panelBeanCreate.repaint();
			}
			if (rightPanel.panelTableModify != null) {
				SwingUtilities.updateComponentTreeUI(rightPanel.panelTableModify);
				rightPanel.panelTableModify.repaint();
			}
		}
	}

}