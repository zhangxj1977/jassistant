package org.jas.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.jas.util.UIUtil;

/**
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class PJInternalFrame extends JInternalFrame implements ActionListener, InternalFrameListener {

	/**
	 * the windows menu of the JMenuBar
	 */
	JMenu windowMenu = null;

	/**
	 * frame icon
	 */
	Icon icon = null;

	/**
	 * internal frame menu group
	 */
	static ButtonGroup windowMenuGroup = new ButtonGroup();

	/**
	 * default constructor
	 *
	 */
	public PJInternalFrame(String title, JComponent panelMain, Icon icon, JMenu winMenu) {
		super(title, true, true, true, true);

		setFrameIcon(icon);
		this.windowMenu = winMenu;
		this.icon = icon;

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panelMain, BorderLayout.CENTER);
		addInternalFrameListener(this);
	}


	/**
	 * Creates a JRadioButtonMenuItem for the window menu
	 */
	public void createWindowMenuItem(String title) {
		if (windowMenu != null) {
			JMenuItem mnuItem = new JRadioButtonMenuItem(title);
			mnuItem.setIcon(icon);
			windowMenuGroup.add(mnuItem);
			mnuItem.addActionListener(this);
			windowMenu.add(mnuItem);
		}
	}

	/**
	 * when frame activate select the frame menu
	 *
	 */
	public void setSelectWindowMenu(String title) {
		if (windowMenu != null) {
			JMenuItem menuItem = findMenuItem(title);
			if (menuItem != null) {
				menuItem.setSelected(true);
			}
		}
	}

	/**
	 * remove one menu item with the title
	 */
	public void removeWindowMenuItem(String title) {
		if (windowMenu != null) {
			JMenuItem menuItem = findMenuItem(title);
			if (menuItem != null) {
				windowMenu.remove(menuItem);
				windowMenuGroup.remove(menuItem);
			}
		}
	}

	/**
	 * find this internal frame window menu by title
	 *
	 */
	JMenuItem findMenuItem(String title) {
		if (windowMenu != null) {
			Component[] items = windowMenu.getMenuComponents();
			for (int i = 0; i < items.length; i++) {
				if (items[i] instanceof JRadioButtonMenuItem) {
					JRadioButtonMenuItem oneItem = (JRadioButtonMenuItem) items[i];
					if (oneItem.getText().equals(title)) {
						return oneItem;
					}
				}
			}
		}

		return null;
	}


	/**
	 * when menu action performed, show this frame
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			this.setSelected(true);
			this.show();
		} catch (java.beans.PropertyVetoException pve) {}

		if (UIUtil.isJDK140() && UIUtil.isWindowLF()) {
			KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
			this.requestFocus();
		}
	}

	/**
	 * Invoked when internal frame has been opened.
	 */
	public void internalFrameOpened(InternalFrameEvent e) {
		createWindowMenuItem(getTitle());
	}

	/**
	 * Invoked when internal frame is in the process of being closed.
	 * The close operation can be overridden at this point.
	 */
	public void internalFrameClosing(InternalFrameEvent e) {}

	/**
	 * Invoked when internal frame has been closed.
	 */
	public void internalFrameClosed(InternalFrameEvent e) {
		removeWindowMenuItem(getTitle());
	}

	/**
	 * Invoked when internal frame is iconified.
	 */
	public void internalFrameIconified(InternalFrameEvent e) {}

	/**
	 * Invoked when internal frame is de-iconified.
	 */
	public void internalFrameDeiconified(InternalFrameEvent e) {}

	/**
	 * Invoked when an internal frame is activated.
	 */
	public void internalFrameActivated(InternalFrameEvent e) {
		setSelectWindowMenu(getTitle());
	}

	/**
	 * Invoked when an internal frame is de-activated.
	 */
	public void internalFrameDeactivated(InternalFrameEvent e) {}
}