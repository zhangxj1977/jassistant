package org.jas.base;


import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;

public class PJBasicOptionPaneUI extends BasicOptionPaneUI {

	/**
	 * Creates a new PJBasicOptionPaneUI instance.
	 */
	public static ComponentUI createUI(JComponent x) {
		return new PJBasicOptionPaneUI();
	}

	/**
	 * Returns the message to display from the JOptionPane the receiver is
	 * providing the look and feel for.
	 */
	protected Object getMessage() {
		inputComponent = null;
		if (optionPane != null) {
			if (optionPane.getWantsInput()) {
				/* Create a user comopnent to capture the input. If the
				selectionValues are non null the component and there
				are < 20 values it'll be a combobox, if non null and
				>= 20, it'll be a list, otherwise it'll be a textfield. */
				Object             message = optionPane.getMessage();
				Object[]           sValues = optionPane.getSelectionValues();
				Object             inputValue = optionPane.getInitialSelectionValue();
				JComponent         toAdd;

				if (sValues != null) {
					if (sValues.length < 20) {
						JComboBox            cBox = new JComboBox();

						for(int counter = 0, maxCounter = sValues.length;
							counter < maxCounter; counter++) {
							cBox.addItem(sValues[counter]);
						}
						if (inputValue != null) {
							cBox.setSelectedItem(inputValue);
						}
						inputComponent = cBox;
						toAdd = cBox;
					} else {
						JList                list = new JList(sValues);
						JScrollPane          sp = new JScrollPane(list);

						list.setVisibleRowCount(10);
						if(inputValue != null)
						list.setSelectedValue(inputValue, true);
						toAdd = sp;
						inputComponent = list;
					}

				} else {
					PJEditorTextField         tf = new PJEditorTextField(null);

					if (inputValue != null) {
						String inputString = inputValue.toString();
						tf.setText(inputString);
						tf.setSelectionStart(0);
						tf.setSelectionEnd(inputString.length());
					}
					toAdd = inputComponent = tf;
				}

				Object[]           newMessage;

				if (message == null) {
					newMessage = new Object[1];
					newMessage[0] = toAdd;

				} else {
					newMessage = new Object[2];
					newMessage[0] = message;
					newMessage[1] = toAdd;
				}
				return newMessage;
			}
			return optionPane.getMessage();
		}
		return null;
	}

}
