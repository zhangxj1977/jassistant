package org.jas.base;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jas.util.MessageManager;
import org.jas.util.StringUtil;
import org.jas.util.UIUtil;

/**
 *
 *
 * @author ’£@ŠwŒR
 * @version 1.0
 */
public class PJEditorTextField extends JTextField {

	/**
	 * the type of this field
	 */
	Class type = Object.class;

	/**
	 * for jdk1.4, when open dialog has a bug.
	 */
	boolean openDialog = false;

	/**
	 * verify object to check this field input value
	 */
	CheckInputVerifier checkInputVerifier = new CheckInputVerifier();

	/**
	 * key adapter for input constraint
	 */
	NumberKeyAdapter numberKeyAdapter = new NumberKeyAdapter();
	DateKeyAdapter dateKeyAdapter = new DateKeyAdapter();


	/**
	 * default constructor
	 */
	public PJEditorTextField() {
		this(null);
	}

	/**
	 * default constructor, width type
	 */
	public PJEditorTextField(Class type) {
		this.type = type;
		this.setInputVerifier(checkInputVerifier);

		resetType(type);

		setFont(UIUtil.getDefaultFont(UIUtil.GRID_FONT));
	}

	/**
	 * reset type of the text field.
	 *
	 */
	public void resetType(Class type) {
		this.type = type;

		this.removeKeyListener(numberKeyAdapter);
		this.removeKeyListener(dateKeyAdapter);

		if (type != null) {
			if (Number.class.equals(type.getSuperclass())) {
				this.addKeyListener(numberKeyAdapter);
			} else if (java.util.Date.class.equals(type.getSuperclass())) {
				this.addKeyListener(dateKeyAdapter);
			}
		}
	}

	/**
	 * verify the input value
	 *
	 */
	public boolean verifyValue() {
		try {
			StringUtil.getConvertValueOfType(type, getText());
		} catch (Exception e) {
			openDialog = true;
			MessageManager.showMessage("MCSTC008E", type.getName());
			openDialog = false;
			return false;
		}

		return true;
	}

	/**
	 * verify the input value
	 */
	class CheckInputVerifier extends InputVerifier {
		public boolean verify(JComponent input) {
			return verifyValue();
		}

		public boolean shouldYieldFocus(JComponent input) {
			if (openDialog) {
				return true;
			} else {
				return verify(input);
			}
		}
	}

	/**
	 * Number Key Adapter
	 *
	 */
	class NumberKeyAdapter extends KeyAdapter {

		public void keyTyped(KeyEvent keEvt) {
			char cKey = keEvt.getKeyChar();

			if ((cKey >= '0' && cKey <= '9') || cKey == KeyEvent.VK_BACK_SPACE || cKey == '.') {
				return;
			}

			keEvt.consume();
		}
	}

	/**
	 * Date Key Adapter
	 *
	 */
	class DateKeyAdapter extends KeyAdapter {

		public void keyTyped(KeyEvent keEvt) {
			char cKey = keEvt.getKeyChar();

			if ((cKey >= '0' && cKey <= '9')
					|| cKey == ':'
					|| cKey == '/'
					|| cKey == ' '
					|| cKey == KeyEvent.VK_BACK_SPACE) {
				return;
			}

			keEvt.consume();
		}
	}

}