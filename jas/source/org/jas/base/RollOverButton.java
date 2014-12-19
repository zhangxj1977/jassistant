package org.jas.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class RollOverButton extends JButton {
	RollOverMouseListener buttonRollListener = new RollOverMouseListener();
	Border rollBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED,
										Color.white,
										SystemColor.control,
										SystemColor.control,
										new Color(103, 101, 98));
	Border pressedBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
										Color.white,
										SystemColor.control,
										SystemColor.control,
										new Color(103, 101, 98));
	EmptyBorder emptyBorder = new EmptyBorder(2, 2, 2, 2);

	public RollOverButton() {
		super();
		init();
	}

	public RollOverButton(Icon icon) {
		super(icon);
		init();
	}

	public RollOverButton(String text, Icon icon) {
		super(text, icon);
		init();
	}

	public RollOverButton(String text) {
		super(text);
		init();
	}

	private void init() {
		this.addMouseListener(buttonRollListener);
		setBorder(null);
		setMargin(new Insets(0, 0, 0, 0));
		setMaximumSize(new Dimension(27, 27));
		setMinimumSize(new Dimension(27, 27));
		setPreferredSize(new Dimension(32, 32));
	}

	public void updateUI() {
		super.updateUI();
		setBorder(null);
	}

	/**
	 * icon button roll over mouse listener
	 *
	 */
	class RollOverMouseListener extends MouseAdapter {
		public void mouseEntered(MouseEvent e) {
			JButton button = (JButton) e.getSource();
			if (button.isEnabled()) {
				button.setBorder(rollBorder);
			}
		}

		public void mouseExited(MouseEvent e) {
			JButton button = (JButton) e.getSource();
			button.setBorder(emptyBorder);
		}

		public void mousePressed(MouseEvent e) {
			JButton button = (JButton) e.getSource();
			if (button.isEnabled()) {
				button.setBorder(pressedBorder);
			}
		}

		public void mouseReleased(MouseEvent e) {
			JButton button = (JButton) e.getSource();
			if (button.isEnabled()) {
				button.setBorder(rollBorder);
			}
		}
	}
}
