package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import org.jas.base.PJDialogBase;
import org.jas.common.PJConst;

/**
 * filter tables
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class DialogFilterTables extends PJDialogBase {
	JPanel panelMain = new JPanel();
	Border borderMainEmpty;
	BorderLayout borderLayoutMain = new BorderLayout();
	JToolBar toolBarBottom = new JToolBar();
	JScrollPane scpTableList = new JScrollPane();
	JPanel panelTableList = new JPanel();
	JButton btnCancel = new JButton();
	JButton btnOK = new JButton();
	Component horizontalGlue = Box.createHorizontalGlue();
	JPanel panelBottom = new JPanel();
	BorderLayout borderLayoutBottom = new BorderLayout();
	JCheckBox chkSelectAll = new JCheckBox();

	public DialogFilterTables(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogFilterTables() {
		this(Main.getMDIMain(), "Filter Tables", true);
	}

	void jbInit() throws Exception {
		borderMainEmpty = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		this.setResizable(false);
		panelMain.setPreferredSize(new Dimension(250, 400));
		panelMain.setLayout(borderLayoutMain);
		panelMain.setBorder(borderMainEmpty);
		btnOK.setText("OK");
		btnOK.setMnemonic('O');
		btnOK.setMaximumSize(new Dimension(45, 22));
		btnOK.setMinimumSize(new Dimension(45, 22));
		btnOK.setPreferredSize(new Dimension(45, 22));
		btnOK.setMargin(new Insets(0, 0, 0, 0));
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCancel_actionPerformed(e);
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setMnemonic('C');
		btnCancel.setMaximumSize(new Dimension(45, 22));
		btnCancel.setMinimumSize(new Dimension(45, 22));
		btnCancel.setPreferredSize(new Dimension(45, 22));
		btnCancel.setMargin(new Insets(0, 0, 0, 0));
		panelBottom.setLayout(borderLayoutBottom);
		borderLayoutMain.setVgap(4);
		toolBarBottom.setBorder(null);
		toolBarBottom.setFloatable(false);
		chkSelectAll.setText("Select/Unselect All");
		chkSelectAll.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chkSelectAll_itemStateChanged(e);
			}
		});
		getContentPane().add(panelMain);
		toolBarBottom.add(btnOK, null);
		toolBarBottom.add(horizontalGlue, null);
		toolBarBottom.add(btnCancel, null);
		panelMain.add(chkSelectAll, BorderLayout.NORTH);
		panelMain.add(scpTableList, BorderLayout.CENTER);
		panelMain.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.add(toolBarBottom, BorderLayout.CENTER);
		panelTableList.setLayout(new BoxLayout(panelTableList, BoxLayout.Y_AXIS));
		scpTableList.getViewport().add(panelTableList);
	}

	public void setVisible(boolean b) {
		if (b) {
			initLocation(Main.getMDIMain());
		}
		super.setVisible(b);
	}

	/**
	 * init tables
	 */
	void initResources(Collection allTables, HashMap hideItems) {
		Iterator it = allTables.iterator();

		while (it.hasNext()) {
			String oneName = (String) it.next();
			JCheckBox oneCmbColumn = new JCheckBox(oneName);

			if (hideItems != null && hideItems.containsKey(oneName)) {
				oneCmbColumn.setSelected(false);
			} else {
				oneCmbColumn.setSelected(true);
			}

			panelTableList.add(oneCmbColumn);
		}
	}

	/**
	 * process find grid data
	 */
	void btnOK_actionPerformed(ActionEvent e) {
		dispose();

		Component[] compos = panelTableList.getComponents();
		HashMap hideItems = new HashMap();
		for (int i = 0; i < compos.length; i++) {
			if (compos[i] instanceof JCheckBox) {
				if (!((JCheckBox) compos[i]).isSelected()) {
					hideItems.put(((JCheckBox) compos[i]).getText(), "");
				}
			}
		}

		fireParamTransferEvent(hideItems, PJConst.WINDOW_FILTERTABLES);
	}

	/**
	 * cancel find data
	 */
	void btnCancel_actionPerformed(ActionEvent e) {
		dispose();
	}

	/**
	 * set select all or unselect all
	 */
	void chkSelectAll_itemStateChanged(ItemEvent e) {
		Component[] compos = panelTableList.getComponents();
		for (int i = 0; i < compos.length; i++) {
			if (compos[i] instanceof JCheckBox) {
				if (chkSelectAll.isSelected()) {
					((JCheckBox) compos[i]).setSelected(true);
				} else {
					((JCheckBox) compos[i]).setSelected(false);
				}
			}
		}
	}
}
