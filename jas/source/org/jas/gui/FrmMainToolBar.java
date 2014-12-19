package org.jas.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;

import org.jas.base.RollOverButton;
import org.jas.util.ImageManager;

/**
 * main frame tool bar
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class FrmMainToolBar extends JToolBar {
	FrmMain parent = null;
	RollOverButton toolBarOpenConnection = new RollOverButton();
	RollOverButton toolBarCloseConnection = new RollOverButton();
	RollOverButton toolBarSchemaBrowser = new RollOverButton();
	RollOverButton toolBarSQLEdit = new RollOverButton();
	RollOverButton toolBarNewJavaBean = new RollOverButton();
	RollOverButton toolBarOptions = new RollOverButton();
	ImageIcon iconNewJavaBean = ImageManager.createImageIcon("newbean.gif");
	ImageIcon iconOpenConnection = ImageManager.createImageIcon("open.gif");
	ImageIcon iconCloseConnection = ImageManager.createImageIcon("close.gif");
	ImageIcon iconCopy = ImageManager.createImageIcon("copy.gif");
	ImageIcon iconPaste = ImageManager.createImageIcon("paste.gif");
	ImageIcon iconCut = ImageManager.createImageIcon("cut.gif");
	ImageIcon iconDelete = ImageManager.createImageIcon("delete.gif");
	ImageIcon iconOptions = ImageManager.createImageIcon("options.gif");
	ImageIcon iconDataBrowse = ImageManager.createImageIcon("databrowse.gif");
	ImageIcon iconSQLInput = ImageManager.createImageIcon("sqlscript.gif");

	public FrmMainToolBar(FrmMain parent) {
		this.parent = parent;
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)));
		toolBarOpenConnection.setToolTipText("Open Database Connection");
		toolBarOpenConnection.setIcon(iconOpenConnection);
		toolBarOpenConnection.setMargin(new Insets(1, 1, 1, 1));
		toolBarOpenConnection.addActionListener(parent.buttonMenuActionListener);
		toolBarCloseConnection.setMargin(new Insets(1, 1, 1, 1));
		toolBarCloseConnection.addActionListener(parent.buttonMenuActionListener);
		toolBarCloseConnection.setToolTipText("Close Database Connection");
		toolBarCloseConnection.setIcon(iconCloseConnection);
		toolBarSQLEdit.setToolTipText("Open SQL Edit Window  ctrl + F12");
		toolBarSQLEdit.setIcon(iconSQLInput);
		toolBarSQLEdit.setEnabled(false);
		toolBarSQLEdit.setMargin(new Insets(1, 1, 1, 1));
		toolBarSQLEdit.addActionListener(parent.buttonMenuActionListener);
		toolBarSchemaBrowser.addActionListener(parent.buttonMenuActionListener);
		toolBarSchemaBrowser.setToolTipText("Open Schema Browser Window  ctrl + F11");
		toolBarSchemaBrowser.setIcon(iconDataBrowse);
		toolBarSchemaBrowser.setEnabled(false);
		toolBarSchemaBrowser.setMargin(new Insets(1, 1, 1, 1));
		toolBarNewJavaBean.addActionListener(parent.buttonMenuActionListener);
		toolBarNewJavaBean.setMargin(new Insets(1, 1, 1, 1));
		toolBarNewJavaBean.setIcon(iconNewJavaBean);
		toolBarNewJavaBean.setToolTipText("Create New Java Bean");
		toolBarOptions.setToolTipText("Configuration");
		toolBarOptions.setIcon(iconOptions);
		toolBarOptions.setMargin(new Insets(1, 1, 1, 1));
		toolBarOptions.addActionListener(parent.buttonMenuActionListener);

		this.add(toolBarOpenConnection, null);
		this.add(toolBarCloseConnection, null);
		JToolBar.Separator separator1 = new JToolBar.Separator(new Dimension(2, 28));
		separator1.setBorder(BorderFactory.createEtchedBorder());
		this.add(separator1, null);
		this.add(toolBarSchemaBrowser, null);
		this.add(toolBarSQLEdit, null);
		this.add(toolBarNewJavaBean, null);
		JToolBar.Separator separator2 = new JToolBar.Separator(new Dimension(2, 28));
		separator2.setBorder(BorderFactory.createEtchedBorder());
		this.add(separator2, null);
		this.add(toolBarOptions, null);
		JToolBar.Separator separator3 = new JToolBar.Separator(new Dimension(2, 28));
		separator3.setBorder(BorderFactory.createEtchedBorder());
	}
}