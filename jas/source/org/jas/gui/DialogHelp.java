package org.jas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jas.common.PJConst;
import org.jas.util.FileManager;
import org.jas.util.ImageManager;
import org.jas.util.MessageManager;

/**
 *
 *
 *
 *
 * @author í£Å@äwåR
 * @version 1.0
 */

public class DialogHelp extends JFrame {
	JPanel panelMain = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JToolBar toolBarBottom = new JToolBar();
	JButton btnOK = new JButton();
	JSplitPane scpMain = new JSplitPane();
	JScrollPane scpLeft = new JScrollPane();
	JScrollPane scpRight = new JScrollPane();
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Help Index");
	JTree treeIndex = new JTree(top);
	JTextPane txtContent = new JTextPane();
	StyledDocument doc = txtContent.getStyledDocument();
	ArrayList nameIndexList = new ArrayList();
	ArrayList showNameList = new ArrayList();
	ArrayList iconList = new ArrayList();
	ArrayList contentList = new ArrayList();
	boolean isInit = false;

	public DialogHelp() {
		try {
			jbInit();
			initContent();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		setTitle("Help Content");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		panelMain.setPreferredSize(new Dimension(750, 480));
		panelMain.setLayout(borderLayout1);
		btnOK.setText("OK");
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnOK_actionPerformed(e);
			}
		});
		toolBarBottom.setFloatable(false);
		txtContent.setText("");
		scpLeft.setPreferredSize(new Dimension(180, 460));
		scpLeft.setMinimumSize(new Dimension(180, 460));
		getContentPane().add(panelMain);
		panelMain.add(toolBarBottom,  BorderLayout.SOUTH);
		panelMain.add(scpMain, BorderLayout.CENTER);
		scpMain.add(scpLeft, JSplitPane.LEFT);
		scpLeft.getViewport().add(treeIndex, null);
		scpMain.add(scpRight, JSplitPane.RIGHT);
		scpRight.getViewport().add(txtContent, null);
		scpRight.setPreferredSize(new Dimension(570, 460));
		toolBarBottom.add(btnOK, null);
		scpMain.setDividerLocation(180);

		txtContent.setFont(new Font("SimSun", Font.PLAIN, 12));
		txtContent.setBackground(new Color(255, 255, 193));
		txtContent.setEditable(false);
		treeIndex.setCellRenderer(new HelpTreeCellRender());
		treeIndex.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				processHelpShow(e);
			}
		});
	}

	public void setVisible(boolean b) {
		if (b) {
			pack();
			initLocation(Main.getMDIMain());
		}
		super.setVisible(b);
	}

	private void initLocation(Frame parent) {
		Point x = parent.getLocation();
		Dimension screenSize = parent.getSize();
		Dimension frameSize = getSize();
		if (x.x <= 0) {
			x.x  = 0;
			x.y = 0;
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		}
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation(((screenSize.width - frameSize.width) / 2) + x.x, ((screenSize.height - frameSize.height) / 2) + x.y);
	}

	void initContent() {
		if (!isInit) {
			try {
				ArrayList orgContentList = FileManager.readInputStream(PJConst.HELP_CONTENT, "GBK");

				for (int i = 0; i < orgContentList.size(); i++) {
					String oneContent = (String) orgContentList.get(i);

					int firstSep = oneContent.indexOf("=");
					int secondSep = oneContent.indexOf("=", firstSep + 1);
					int thirdSep = oneContent.indexOf("=", secondSep + 1);

					nameIndexList.add(oneContent.substring(0, firstSep));
					showNameList.add(oneContent.substring(firstSep + 1, secondSep));
					iconList.add(oneContent.substring(secondSep + 1, thirdSep));
					contentList.add(oneContent.substring(thirdSep + 1));
				}
				isInit = true;
			} catch (IOException e) {
				MessageManager.showMessage("MCSTC002E", e.getMessage());
			}
		}

		for (int i = 0; i < showNameList.size(); i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(showNameList.get(i));
			top.add(node);
		}

		treeIndex.expandRow(0);
	}


	void processHelpShow(TreeSelectionEvent e) {
		/*
		try {
			txtContent.setPage(FileManager.getResourcePath("/org/jas/util/resource/helphtml/index.html"));
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		*/
		int[] selectRows = treeIndex.getSelectionRows();

		if (selectRows.length > 0) {
			try {
				int selectedIndex = selectRows[0] - 1;
				if (selectedIndex >= 0) {
					txtContent.setText("");
					String iconNames = (String) iconList.get(selectedIndex);
					StringTokenizer st = new StringTokenizer(iconNames, ",");
					while (st.hasMoreTokens()) {
						String iconName = st.nextToken();
						ImageIcon icon = ImageManager.createImageIcon(PJConst.HELP_IMAGE_DIR, iconName);
						Style s = txtContent.addStyle(iconName, null);
						StyleConstants.setIcon(s, icon);
						doc.insertString(doc.getLength(), " ", s);
					}

					Style s = txtContent.addStyle("normal", null);
					StyleConstants.setForeground(s, Color.black);

					String content = (String) contentList.get(selectedIndex);
					StringBuffer sb = new StringBuffer();
					int beginIndex = 0;
					int breakLineIndex = -1;
					while ((breakLineIndex = content.indexOf("\\n", beginIndex)) >= 0) {
						sb.append(content.substring(beginIndex, breakLineIndex) + "\n");
						beginIndex = breakLineIndex + 2;
					}
					sb.append(content.substring(beginIndex) + "\n");
					doc.insertString(doc.getLength(), "\n\n" + sb.toString(), s);
				} else {
					txtContent.setText("");
				}
				txtContent.select(0, 0);
			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}
	}


	void btnOK_actionPerformed(ActionEvent e) {
		dispose();
	}


	/**
	 * help cell render
	 *
	 */
	class HelpTreeCellRender extends DefaultTreeCellRenderer {
		Font simSunFont = new Font("SimSun", Font.PLAIN, 12);

		public Component getTreeCellRendererComponent(JTree tree,
                                              Object value,
                                              boolean selected,
                                              boolean expanded,
                                              boolean leaf,
                                              int row,
                                              boolean hasFocus) {
			setFont(simSunFont);

			return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
	}
}
