package org.jas.base;

import javax.swing.JComboBox;

/**
 * for jdk1.4 bugs
 *
 * @author ���@�w�R
 * @version 1.0
 */

public class PJEditableComboBox extends JComboBox {

	public boolean isFocusTraversable() {
		return false;
	}
}