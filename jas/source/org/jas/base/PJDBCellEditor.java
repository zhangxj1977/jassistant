package org.jas.base;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * database table cell editor
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class PJDBCellEditor extends DefaultCellEditor {

	public PJDBCellEditor(JTextField textField) {
		super(textField);
	}

	public PJDBCellEditor(JCheckBox checkBox) {
		super(checkBox);
	}

	public PJDBCellEditor(JComboBox comboBox) {
		super(comboBox);
	}

	/**
	 * for valid the input value
	 */
	public boolean stopCellEditing() {
		PJEditorTextField editorCompo = (PJEditorTextField) getComponent();
		if (!editorCompo.verifyValue()) {
			return false;
		}

		return super.stopCellEditing();
	}
}