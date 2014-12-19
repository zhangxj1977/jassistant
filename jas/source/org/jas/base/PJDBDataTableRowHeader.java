package org.jas.base;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * ＤＢデータ用のテーブルRowヘーダ
 *
 * @author 張　学軍
 * @version 1.0
 */
public class PJDBDataTableRowHeader extends JTable {

	JTable mainTable = null;

	public PJDBDataTableRowHeader() {
		this(null, null, null);
	}

	public PJDBDataTableRowHeader(JTable mainTable) {
		this();
		this.mainTable = mainTable;
	}

	public PJDBDataTableRowHeader(TableModel dm) {
		this(dm, null, null);
	}

	public PJDBDataTableRowHeader(TableModel dm, TableColumnModel cm) {
		this(dm, cm, null);
	}

	public PJDBDataTableRowHeader(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm,cm,sm);

		setRowHeight(18);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setColumnSelectionAllowed(false);
		getTableHeader().setReorderingAllowed(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setGridColor(Color.black);
	}

	protected void processMouseEvent(MouseEvent e) {
		if (e.getClickCount() > 0) {
			if (mainTable != null && mainTable instanceof PJDBDataTable) {
				DefaultCellEditor editor = (DefaultCellEditor) mainTable.getCellEditor();
				if (editor != null) {
					PJEditorTextField editorCompo = (PJEditorTextField) editor.getComponent();
					if (!editorCompo.verifyValue()) {
						e.consume();
						return;
					}
				}
			}
		}

		super.processMouseEvent(e);
	}
}