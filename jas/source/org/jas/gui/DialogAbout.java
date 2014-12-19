package org.jas.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jas.base.PJDialogBase;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;

/**
 *
 * @author í£Å@äwåR
 * @version 1.0
 */

public class DialogAbout extends PJDialogBase {
	JPanel panelMain = new JPanel();
	JLabel lblIcon = new JLabel();
	JLabel lblDescription = new JLabel();
	JLabel lblAuthor = new JLabel();
	JLabel lblCorp = new JLabel();
	JLabel lblVersion = new JLabel();
	JButton btnOK = new JButton();
	ImageIcon iconHead = ImageManager.createImageIcon("system.gif");

	public DialogAbout(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogAbout() {
		this(null, "About", true);
	}

	public DialogAbout(Frame parent) {
		this(Main.getMDIMain(), "About", true);
	}

	public void setVisible(boolean b) {
		if (b) {
			initLocation(Main.getMDIMain());
		}
		super.setVisible(b);
	}

	void jbInit() throws Exception {
		panelMain.setLayout(null);
		this.setResizable(false);
		setSize(new Dimension(343, 160));
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcon.setHorizontalTextPosition(SwingConstants.CENTER);
		lblIcon.setIcon(iconHead);
		lblIcon.setBounds(new Rectangle(8, 10, 100, 110));
		lblDescription.setText("JDBDevelop Assistant");
		lblDescription.setBounds(new Rectangle(120, 20, 190, 16));
		lblVersion.setText("Version: 1.1   Build Date: 2013/12/01");
		lblVersion.setBounds(new Rectangle(120, 36, 190, 16));
		lblAuthor.setText("Author:   " + MessageManager.getMessage("AUTHOR_NAME"));
		lblAuthor.setBounds(new Rectangle(120, 52, 190, 16));
		lblCorp.setText(MessageManager.getMessage("AUTHOR_CORP"));
		lblCorp.setBounds(new Rectangle(165, 68, 145, 16));
		btnOK.setMargin(new Insets(1, 1, 1, 1));
		btnOK.setText("OK");
		btnOK.setBounds(new Rectangle(200, 105, 50, 20));
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});
		getContentPane().add(panelMain);
		panelMain.add(lblIcon, null);
		panelMain.add(btnOK, null);
		panelMain.add(lblDescription, null);
		panelMain.add(lblVersion, null);
		panelMain.add(lblAuthor, null);
		panelMain.add(lblCorp, null);
	}


	void btnOK_actionPerformed(ActionEvent e) {
		dispose();
	}
}