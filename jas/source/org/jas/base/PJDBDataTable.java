package org.jas.base;

import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * ＤＢデータ用のテーブル
 *
 * @author 張　学軍
 * @version 1.0
 */

public class PJDBDataTable extends JTable {

	public PJDBDataTable() {
		this(null, null, null);
	}

	public PJDBDataTable(TableModel dm) {
		this(dm, null, null);
	}

	public PJDBDataTable(TableModel dm, TableColumnModel cm) {
		this(dm, cm, null);
	}

	public PJDBDataTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm,cm,sm);

		setRowHeight(18);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setColumnSelectionAllowed(false);
		getTableHeader().setReorderingAllowed(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setUI(new PJTableHeaderUI());
		setDefaultRenderer(Object.class, new PJTableCellRender());
	}

	public void updateUI() {
		super.updateUI();
		getTableHeader().updateUI();
	}

	protected void processMouseEvent(MouseEvent e) {
		if (e.getClickCount() > 0) {
			DefaultCellEditor editor = (DefaultCellEditor) getCellEditor();
			if (editor != null) {
				PJEditorTextField editorCompo = (PJEditorTextField) editor.getComponent();
				if (!editorCompo.verifyValue()) {
					e.consume();
					return;
				}
			}
		}

		super.processMouseEvent(e);
	}
}