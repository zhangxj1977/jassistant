package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.jas.base.RollOverButton;
import org.jas.util.FilterSortManager;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;

/**
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class PanelRightToolBarPanel extends JPanel {
	ImageIcon iconRefreshTableList = ImageManager.createImageIcon("refreshtablellist.gif");
	ImageIcon iconRefreshRightPanel = ImageManager.createImageIcon("refreshrightpanel.gif");
	ImageIcon iconClearFilterSort = ImageManager.createImageIcon("filtersortnothing.gif");
	BorderLayout borderLayout1 = new BorderLayout();
	JToolBar toolBar = new JToolBar();
	RollOverButton btnRefreshRightPanel = new RollOverButton();
	RollOverButton btnRefreshTableList = new RollOverButton();
	RollOverButton btnClearFilterSort = new RollOverButton();
	Component horizontalGlue = Box.createHorizontalGlue();
	JLabel lblTableName = new JLabel();

	public PanelRightToolBarPanel() {
		try {
			jbInit();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		this.setPreferredSize(new Dimension(600, 20));
		this.setLayout(borderLayout1);
		toolBar.setBorder(null);
		toolBar.setFloatable(false);
		btnRefreshRightPanel.setEnabled(true);
		btnRefreshRightPanel.setMaximumSize(new Dimension(27, 27));
		btnRefreshRightPanel.setMinimumSize(new Dimension(27, 27));
		btnRefreshRightPanel.setPreferredSize(new Dimension(32, 32));
		btnRefreshRightPanel.setToolTipText("refresh the all right panel.");
		btnRefreshRightPanel.setIcon(iconRefreshRightPanel);
		btnRefreshRightPanel.setMargin(new Insets(0, 0, 0, 0));
		btnRefreshRightPanel.setVerifyInputWhenFocusTarget(false);
		btnRefreshTableList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRefreshTableList_actionPerformed(e);
			}
		});
		btnRefreshTableList.setMargin(new Insets(0, 0, 0, 0));
		btnRefreshTableList.setIcon(iconRefreshTableList);
		btnRefreshTableList.setToolTipText("refresh the left table and view list ");
		btnRefreshTableList.setPreferredSize(new Dimension(32, 32));
		btnRefreshTableList.setMinimumSize(new Dimension(27, 27));
		btnRefreshTableList.setMaximumSize(new Dimension(27, 27));
		btnRefreshTableList.setEnabled(true);
		btnClearFilterSort.setMargin(new Insets(0, 0, 0, 0));
		btnClearFilterSort.setIcon(iconClearFilterSort);
		btnClearFilterSort.setToolTipText("clear all table\'s filter and sort");
		btnClearFilterSort.setPreferredSize(new Dimension(32, 32));
		btnClearFilterSort.setMinimumSize(new Dimension(27, 27));
		btnClearFilterSort.setMaximumSize(new Dimension(27, 27));
		btnClearFilterSort.setEnabled(true);
		btnClearFilterSort.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClearFilterSort_actionPerformed(e);
			}
		});
		btnRefreshRightPanel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRefreshRightPanel_actionPerformed(e);
			}
		});
		lblTableName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTableName.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTableName.setText("");
		lblTableName.setPreferredSize(new Dimension(380, 20));
		this.add(toolBar, BorderLayout.CENTER);
		toolBar.add(btnRefreshTableList, null);
		toolBar.add(btnRefreshRightPanel, null);
		toolBar.add(btnClearFilterSort, null);
		toolBar.add(horizontalGlue, null);
		toolBar.add(lblTableName, null);
	}

	/**
	 * refresh left panel lists
	 *
	 */
	void btnRefreshTableList_actionPerformed(ActionEvent e) {
		try {
			Main.getMDIMain().sqlBrowserPanel.leftPanel.refreshTableList(Main.getMDIMain().getConnection());
		} catch (SQLException se) {
			MessageManager.showMessage("MCSTC202E", se.getMessage());
		}
	}

	void btnRefreshRightPanel_actionPerformed(ActionEvent e) {
		refreshRightPanel();
	}

	void btnClearFilterSort_actionPerformed(ActionEvent e) {
		if (MessageManager.showMessage("MCSTC011Q") == 0) {
			String currentConnURL = Main.getMDIMain().currentConnURL;
			FilterSortManager.clearFilterSort(currentConnURL);
			refreshRightPanel();
		}
	}

	void refreshRightPanel() {
		PanelRight rightPanel = (PanelRight) Main.getMDIMain().sqlBrowserPanel.dataMainSplitPane.getRightComponent();
		if (rightPanel != null) {
			rightPanel.enabledReFresh();
			rightPanel.refreshSelected();
		}
	}
}