package org.jas.base;

import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class PJTableHeaderUI extends javax.swing.plaf.basic.BasicTableHeaderUI {

	protected void installListeners() {
		super.installListeners();

		MouseInputHandler mouseInputListener = new MouseInputHandler();
		header.addMouseListener(mouseInputListener);
	}

	public class MouseInputHandler extends MouseInputAdapter {

		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			int clickCount = e.getClickCount();

			if (header == null) return;

			JTable thisTable  = header.getTable();
			FontMetrics fm = thisTable.getFontMetrics(thisTable.getFont());
			TableColumnModel columnModel = header.getColumnModel();
			int index = getResizingColumn(p, columnModel.getColumnIndexAtX(p.x));
			int rows = thisTable.getRowCount();
			int maxLength = 0;

			if (clickCount == 2 && !e.isControlDown()) {
				if (index != -1 && rows > 0) {
					if (canResize(index)) {
						for (int i=0; i<rows; i++) {
							Object objValue = thisTable.getValueAt(i, index);
							if (objValue instanceof java.lang.String) {
								int thisLen = fm.stringWidth((String) objValue);
								maxLength = Math.max(maxLength, thisLen);
							}
						}
						if (maxLength > 0)
							thisTable.getColumnModel().getColumn(index).setPreferredWidth(maxLength + 7);
					}
				}
			} else if (clickCount == 3 || (clickCount == 2 && e.isControlDown())) {
				TableModel model = thisTable.getModel();
				if (model instanceof DBTableModel) {
					((DBTableModel) model).restoreColumnWidth(index);
				}
			}
		}

		private boolean canResize(int index) {
			TableColumn tblColumn = header.getColumnModel().getColumn(index);
			return (tblColumn != null) && header.getResizingAllowed() && tblColumn.getResizable();
		}

		private int getResizingColumn(Point p, int column) {
			if (column == -1) {
				return -1;
			}
			Rectangle r = header.getHeaderRect(column);
			r.grow(-3, 0);
			if (r.contains(p)) {
				return -1;
			}
			int midPoint = r.x + r.width/2;
			int columnIndex = (p.x < midPoint) ? column - 1 : column;

			return columnIndex;
		}

	}
}