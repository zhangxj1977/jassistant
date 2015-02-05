package org.jas.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.jas.common.PJConst;
import org.jas.db.format.BlancoSqlFormatter;
import org.jas.db.format.BlancoSqlRule;
import org.jas.util.UIUtil;

/**
 *  SQL Script Pane,
 *  support synax highlight
 *
 * @author 張　学軍
 * @version 1.0
 */
public class PJSQLTextPane extends JTextPane {

	/**
	 * current connection all table names
	 */
	public static String[] tableNames = null;

	/**
	 * function names
	 */
	public static String[] functionNames = null;

	/** UndoManager that we add edits to. */
	private UndoManager undo = new UndoManager();
	private UndoableEditListener undoHandler = new UndoHandler();
	private UndoAction undoAction = new UndoAction();
	private RedoAction redoAction = new RedoAction();

	/**
	 * the doc
	 */
	private StyledDocument doc = getStyledDocument();

	/**
	 * can register the undo redo button
	 */
	private JButton btnUndo = null;
	private JButton btnRedo = null;


	/**
	 * default constructor
	 */
	public PJSQLTextPane() {
		addKeyListener(new TextPaneKeyListener());
		doc.addUndoableEditListener(undoHandler);
		createStyles();
		setDefaultFontStyle();
	}

	/**
	 * default constructor
	 */
	public PJSQLTextPane(JButton btnUndo, JButton btnRedo) {
		this();
		setUndoRedoButton(btnUndo, btnRedo);
	}

	/**
	 * set undo redo button
	 */
	public void setUndoRedoButton(JButton btnUndo, JButton btnRedo) {
		this.btnUndo = btnUndo;
		this.btnRedo = btnRedo;

		if (btnUndo != null) {
			btnUndo.addActionListener(undoAction);
		}
		if (btnRedo != null) {
			btnRedo.addActionListener(redoAction);
		}
	}

	/**
	 * Resets the undo manager.
	 */
	public void resetUndoManager() {
		undo.discardAllEdits();
		undoAction.update();
		redoAction.update();
	}

	/**
	 * select one line
	 */
	public void selectLine(int line) {
		Element root = doc.getDefaultRootElement();
		Element para = root.getElement(line);

		if (para != null) {
			int start = para.getStartOffset();
			int end = para.getEndOffset() - 1;

			select(start, end);
		}
	}

	/**
	 * text pane key listener
	 * it can do highlight, undo redo....
	 */
	class TextPaneKeyListener extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			highLightParse();
		}

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_Z && e.isControlDown()) {
				processUndo();
			} else if (keyCode == KeyEvent.VK_Y && e.isControlDown()) {
				processRedo();
			} else if (keyCode == KeyEvent.VK_F5 && e.isAltDown()) {
				toUpperCase();
			} else if (keyCode == KeyEvent.VK_F5 && e.isShiftDown()) {
				toLowerCase();
			}
		}
	}

	/**
	 * undo redo hanler
	 *
	 */
	class UndoHandler implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			undo.addEdit(e.getEdit());
			undoAction.update();
			redoAction.update();
		}
	}

	/**
	 * Undo Action
	 */
	class UndoAction extends AbstractAction {
		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			processUndo();
		}

		protected void update() {
			if (undo.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getUndoPresentationName());
				if (btnUndo != null) {
					btnUndo.setEnabled(true);
				}
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
				if (btnUndo != null) {
					btnUndo.setEnabled(false);
				}
			}
		}
	}

	/**
	 * Redo Action
	 */
	class RedoAction extends AbstractAction {
		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			processRedo();
		}

		protected void update() {
			if (undo.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getRedoPresentationName());
				if (btnRedo != null) {
					btnRedo.setEnabled(true);
				}
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
				if (btnRedo != null) {
					btnRedo.setEnabled(false);
				}
			}
		}
	}

	/**
	 * process undo action
	 */
	public void processUndo() {
		try {
			undo.undo();
			highLightParse();
		} catch (CannotUndoException ex) {
			ex.printStackTrace();
		}
		undoAction.update();
		redoAction.update();
		requestFocus();
	}

	/**
	 * process redo action
	 */
	public void processRedo() {
		try {
			undo.redo();
			highLightParse();
		} catch (CannotRedoException ex) {
			ex.printStackTrace();
		}
		redoAction.update();
		undoAction.update();
		requestFocus();
	}

	/**
	 * to upper case
	 */
	public void toUpperCase() {
		String selText = getSelectedText();
		if (selText != null && !selText.trim().equals("")) {
			int selStart = getSelectionStart();
			int selEnd = getSelectionEnd();
			replaceSelection(selText.toUpperCase());
			hightLight(selStart, selEnd);
			select(selStart, selEnd);
		}

		requestFocus();
	}

	/**
	 * to lower case
	 */
	public void toLowerCase() {
		String selText = getSelectedText();
		if (selText != null && !selText.trim().equals("")) {
			int selStart = getSelectionStart();
			int selEnd = getSelectionEnd();
			replaceSelection(selText.toLowerCase());
			hightLight(selStart, selEnd);
			select(selStart, selEnd);
		}

		requestFocus();
	}

	/**
	 * format sql
	 */
	public void formatSql() {
		String selText = getSelectedText();
		if (selText != null && !selText.trim().equals("")) {
			int selStart = getSelectionStart();
			int selEnd = getSelectionEnd();
			
			BlancoSqlRule rule = new BlancoSqlRule();
			rule.setFunctionNames(functionNames);
			BlancoSqlFormatter formatter = new BlancoSqlFormatter(rule);
			try {
				String strFormatted = formatter.format(selText);
				replaceSelection(strFormatted);
				
				hightLight(selStart, selStart + strFormatted.length());
				select(selStart, selStart + strFormatted.length());
			} catch (Exception e) {
				e.printStackTrace();
				replaceSelection(selText);
			}
		}
		requestFocus();
	}
	
	/**
	 * mybatis形式のパラメータ置換
	 */
	public void replaceParams(String[] params) {
        //System.out.println(Arrays.toString(params));
	    for (int i = 0; i < params.length; i++) {
            String tmp = params[i];
            int startPos = tmp.lastIndexOf('('); 
            int endPos = tmp.lastIndexOf(')');
            if (endPos > startPos) {
                String type = tmp.substring(startPos + 1, endPos);
                String value = tmp.substring(0, startPos);
                if ("Integer".equals(type)
                        || "Long".equals(type)
                        || "Short".equals(type)
                        || "BigDecimal".equals(type)
                        || "BigInteger".equals(type)
                        || "Float".equals(type)
                        || "Double".equals(type)) {
                    tmp = value;
                } else {
                    tmp = "'" + value + "'";
                }
                params[i] = tmp;
            }
        }
        String selText = getSelectedText();
        if (selText != null && !selText.trim().equals("")) {
            int selStart = getSelectionStart();
            String strFormatted = selText;
            int cnt = 0;
            int findPos = strFormatted.indexOf('?');
            while (findPos > 0) {
                if (cnt < params.length) {
                    strFormatted = strFormatted.replaceFirst("\\?", params[cnt]);
                } else {
                    break;
                }
                cnt++;
                findPos = strFormatted.indexOf('?');
            }
            replaceSelection(strFormatted);
            hightLight(selStart, selStart + strFormatted.length());
            select(selStart, selStart + strFormatted.length());
        }
        requestFocus();
	}

	void createStyles() {
		Style s = addStyle("keyword", null);
		StyleConstants.setForeground(s, Color.blue);

		s = addStyle("tablename", null);
		StyleConstants.setForeground(s, Color.orange.darker());

		s = addStyle("function", null);
		StyleConstants.setForeground(s, Color.magenta.darker());

		s = addStyle("operator", null);
		StyleConstants.setForeground(s, Color.pink.darker());

		s = addStyle("quoted", null);
		StyleConstants.setForeground(s, Color.red);

		s = addStyle("comment", null);
		StyleConstants.setItalic(s, true);
		StyleConstants.setForeground(s, Color.green.darker());
	}

	void setDefaultFontStyle() {
		Style s = addStyle("normal", null);
		StyleConstants.setForeground(s, Color.black);

		Font defaultFont = UIUtil.getDefaultFont(UIUtil.SQL_FONT);
		setFont(defaultFont);

		StyleConstants.setFontSize(s, defaultFont.getSize());
		StyleConstants.setFontFamily(s, defaultFont.getFamily());

		StyleConstants.setBold(s, false);
		StyleConstants.setItalic(s, false);
		if (defaultFont.isBold()) {
			StyleConstants.setBold(s, true);
		} else if (defaultFont.isItalic()) {
			StyleConstants.setItalic(s, true);
		} else if (defaultFont.isPlain()) {
			StyleConstants.setBold(s, false);
			StyleConstants.setItalic(s, false);
		}
	}

	/**
	 * when font changed, reset the pane
	 */
	public void resetDefaultFontStyle() {
		setDefaultFontStyle();
		hightLight(0, getText().length());
	}

	/**
	 * insert string into the postion
	 */
	public void appendString(String text) {
		setText(getText() + text);
		hightLight(0, getText().length());
	}

	/**
	 * override paste and hightlight
	 */
	public void paste() {
		super.paste();
		hightLight(0, getText().length());
	}

	/**
	 * hightlight key words
	 *
	 */
	void highLightParse() {
		doc.removeUndoableEditListener(undoHandler);

		try {
			Element root = doc.getDefaultRootElement();
			int cursorPos = getCaretPosition();
			int line = root.getElementIndex(cursorPos);
			Element para = root.getElement(line);
			highLightParseLineElement(para);
		} catch (Exception ee) {
			ee.printStackTrace();
		}

		doc.addUndoableEditListener(undoHandler);
	}

	void hightLight(int start, int end) {
		doc.removeUndoableEditListener(undoHandler);

		try {
			Element root = doc.getDefaultRootElement();
			int beginLine = root.getElementIndex(start);
			int endLine = root.getElementIndex(end);

			for (int i = beginLine; i <= endLine; i++) {
				Element para = root.getElement(i);
				highLightParseLineElement(para);
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}

		doc.addUndoableEditListener(undoHandler);
	}

	void highLightParseLineElement(Element para) throws BadLocationException {
		if (para != null) {
			int start = para.getStartOffset();
			int end = para.getEndOffset() - 1;
			if (start > end) {
				return;
			}
			String s = doc.getText(start, end - start);

			// first set normal
			doc.setCharacterAttributes(start, end - start, getStyle("normal"), false);

			int wordBegin = -1;
			int wordEnd = -1;

			for (int i = 0; i < s.length(); i++) {
				char curChar = s.charAt(i);

				if (curChar == '-'
						&& i < s.length() -1
						&& s.charAt(i + 1) == '-') {
					doc.setCharacterAttributes(start + i, s.length() - i, getStyle("comment"), false);
					break;
				}

				if (isOperatorChar(curChar)) {
					doc.setCharacterAttributes(start + i, 1, getStyle("operator"), false);
					wordBegin = -1;
					wordEnd = -1;
					continue;
				} else if (isWordChar(curChar)) {
					if (wordBegin == -1) {
						wordBegin = i;
						continue;
					} else {
						if (i == s.length() - 1) {
							wordEnd = i + 1;
						}
					}
				} else if (wordBegin >= 0) {
					wordEnd = i;
				} else {
					continue;
				}

				if (wordBegin >= 0 && wordEnd > wordBegin) {
					String oneWord = s.substring(wordBegin, wordEnd);

					if (wordBegin > 0 && wordEnd < s.length()
							&& s.charAt(wordBegin - 1) == '\''
							&& s.charAt(wordEnd) == '\'') {
						doc.setCharacterAttributes(start + wordBegin - 1, wordEnd - wordBegin + 2, getStyle("quoted"), false);
					} else if (isSQL92Keyword(oneWord)) {
						doc.setCharacterAttributes(start + wordBegin, wordEnd - wordBegin, getStyle("keyword"), false);
					} else if (isTableName(oneWord)) {
						doc.setCharacterAttributes(start + wordBegin, wordEnd - wordBegin, getStyle("tablename"), false);
					} else if (isFunction(oneWord)) {
						if (wordEnd < s.length()) {
							char nextChar = s.charAt(wordEnd);
							if (nextChar == '(' || nextChar == ' ') {
								doc.setCharacterAttributes(start + wordBegin, wordEnd - wordBegin, getStyle("function"), false);
							}
						}
					}

					wordBegin = -1;
					wordEnd = -1;
				}
			}
		}
	}

	/**
	 * check the word is a sql92 keyword
	 */
	boolean isSQL92Keyword(String word) {
		for (int i = 0; i < PJConst.SQL92_KEYWORDS.length; i++) {
			if (PJConst.SQL92_KEYWORDS[i].equalsIgnoreCase(word)) {
				return true;
			}
		}

		return false;
	}

	boolean isTableName(String word) {
		if (tableNames != null) {
			for (int i = 0; i < tableNames.length; i++) {
				if (tableNames[i].equalsIgnoreCase(word)) {
					return true;
				}
			}
		}

		return false;
	}

	boolean isFunction(String word) {
		if (functionNames != null) {
			for (int i = 0; i < functionNames.length; i++) {
				if (functionNames[i].equalsIgnoreCase(word)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * check the char is a english char
	 */
	boolean isWordChar(char c) {
		if ((c >= 'A' && c <= 'Z')
				|| (c >= 'a' && c <= 'z')
				|| (c >= '0' && c <= '9')
				|| c == '_' || c == '$') {
			return true;
		}

		return false;
	}

	/**
	 * check whether the char is a operator char
	 */
	boolean isOperatorChar(char c) {
		if (c == '<' || c == '>'
				|| c == '!' || c == '=') {
			return true;
		}

		return false;
	}
}