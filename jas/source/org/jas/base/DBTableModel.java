package org.jas.base;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;

import org.jas.common.PJConst;
import org.jas.util.StringUtil;

/**
 * テーブルのモードクラス定義
 *
 * @author 張　学軍
 * @version 1.0
 */

public class DBTableModel extends AbstractTableModel {
	// the first line is column name
	// the second line is the type
	Vector data = new Vector();

	/**
	 * the flag array that show whether the column to be shown.
	 */
	boolean[] showColumnFlags;

	/**
	 * current associate talbe
	 */
	JTable table = null;

	/**
	 * row header
	 */
	JTable rowHeader = null;

	/**
	 * the table type (table, view);
	 * view data should not be edited
	 */
	int tableType = PJConst.TABLE_TYPE_TABLE;

	/**
	 * the size of extra data before the data
	 * 1:   columnName vector
	 * 2:   type class vector
	 * 3:   size vector
	 * 4:   key vector
	 * 5:   comment vector
	 */
	int extraTopSize = 5;

	/**
	 * is the content changed
	 */
	public boolean isChanged = false;

	/**
	 * if the row is editing, the orginal value is stored in
	 * this Vector. use to restore the row.
	 */
	public Vector orgRowData = null;

	/**
	 * show comment
	 */
	public boolean isShowComment = false;

	/**
	 * default constructor, empty
	 */
	public DBTableModel() {
		super();
	}

	/**
	 * constructor with a table
	 */
	public DBTableModel(JTable table, JTable rowHeader,
						Vector data, int tableType) {
		this(data);
		this.table = table;
		this.rowHeader = rowHeader;
		this.tableType = tableType;
	}

	/**
	 * constructor with a table, enable select columns
	 */
	public DBTableModel(JTable table, JTable rowHeader,
						Vector data, int tableType,
						boolean[] showColumnFlags) {
		this(data);
		this.table = table;
		this.rowHeader = rowHeader;
		this.tableType = tableType;

		if (showColumnFlags != null) {
			this.showColumnFlags = showColumnFlags;
		}
	}

	/*
	 * default constructor with data
	 */
	public DBTableModel(Vector data) {
		super();
		this.data = data;
		initShowColumnFlags();
	}

	/**
	 * set show column flags
	 *
	 */
	public void setShowColumnFlags(boolean[] showColumnFlags) {
		if (showColumnFlags != null) {
			this.showColumnFlags = showColumnFlags;
		}
		fireTableStructureChanged();
		resetTable();
	}

	/**
	 * set cell editor
	 *
	 */
	public void setCellEditor(CellEditorListener cellEditorListener) {
		for (int i = 0; i < getColumnCount(); i++) {
			int col = getRealColumnIndex(i);
			PJEditorTextField editorComp = new PJEditorTextField(getColumnJavaType(col));
			editorComp.setBorder(null);
			PJDBCellEditor defaultCellEditor = new PJDBCellEditor(editorComp);
			defaultCellEditor.addCellEditorListener(cellEditorListener);
			table.getColumnModel().getColumn(i).setCellEditor(defaultCellEditor);
		}
	}

	/**
	 * init show flags
	 *
	 */
	private void initShowColumnFlags() {
		showColumnFlags = new boolean[((Vector) data.get(0)).size()];

		for (int i = 0; i < showColumnFlags.length; i++) {
			showColumnFlags[i] = true;
		}
	}

	/**
	 * get the real column index by show index
	 */
	public int getRealColumnIndex(int col) {
		int count = -1;
		for (int i = 0; i < showColumnFlags.length; i++) {
			if (showColumnFlags[i]) {
				if (++count == col) {
					return i;
				}
			}
		}

		return 0;
	}

	/**
	 * get shown column index by real col
	 */
	private int getShowColumnIndex(int realCol) {
		int count = -1;
		for (int i = 0; i < showColumnFlags.length; i++) {
			if (showColumnFlags[i]) {
				++count;
				if (i == realCol) {
					return count;
				}
			}
		}

		return 0;
	}

	/**
	 * These methods always need to be implemented.
	 */
	public int getColumnCount() {
		if (data == null || data.isEmpty()) {
			return 0;
		}

		int count = 0;
		for (int i = 0; i < showColumnFlags.length; i++) {
			if (showColumnFlags[i]) {
				count++;
			}
		}

		return count;
	}

	public int getRowCount() {
		if (data == null || data.isEmpty()) {
			return 0;
		}

		return data.size() - extraTopSize;
	}

	public Object getValueAt(int row, int col) {
		if (data == null || data.isEmpty()) {
			return null;
		}

		col = getRealColumnIndex(col);
		Object value = ((Vector) data.get(row + extraTopSize)).get(col);
		Class javaType = getColumnJavaType(col);

		return StringUtil.getStringValue(javaType, value);
	}

	public int findRowData(Vector valueVector, int beginIndex,
							boolean isCaseSensitive, boolean isPartial) {
		while (beginIndex < getRowCount()) {
			for (int i = 0; i < valueVector.size(); i++) {
				boolean thisFind = false;
				String[] oneCond = (String[]) valueVector.get(i);

				int colIndex = Integer.parseInt(oneCond[0]);
				String value = oneCond[1];
				String orgValue = (String) getValueAt(beginIndex, colIndex);

				if (orgValue == null) {
					break;
				}
				if (isCaseSensitive) {
					if ((!isPartial && orgValue.equals(value))
							|| (isPartial && orgValue.indexOf(value) >= 0)) {
						thisFind = true;
					}
				} else {
					if ((!isPartial && orgValue.equalsIgnoreCase(value))
							|| (isPartial && orgValue.toUpperCase().indexOf(value.toUpperCase()) >= 0)) {
						thisFind = true;
					}
				}

				if (thisFind) {
					if (i == valueVector.size() - 1) {
						return beginIndex;
					} else {
						continue;
					}
				}

				break;
			}

			beginIndex++;
		}

		return -1;
	}

	public Vector getRowData(int row) {
		if (data == null || data.isEmpty()) {
			return null;
		}

		return (Vector) data.get(row + extraTopSize);
	}

	public void setRowData(int row, Vector newData) {
		if (data == null || data.isEmpty()) {
			return;
		}

		data.set(row + extraTopSize, newData);
	}

	public Vector getColumnJavaType() {
		if (data == null || data.isEmpty()) {
			return null;
		}

		return (Vector) data.get(1);
	}

	public Class getColumnJavaType(int col) {
		if (data == null || data.isEmpty()) {
			return null;
		}

        col = getRealColumnIndex(col);
        return (Class) ((Vector) data.get(1)).get(col);
	}

	public Vector getColumnName() {
		if (isShowComment) {
			return getColumnComment();
		}

		if (data == null || data.isEmpty()) {
			return null;
		}

		return (Vector) data.get(0);
	}

	public String getColumnName(int column) {
		if (isShowComment) {
			return getColumnComment(column);
		}

		if (data == null || data.isEmpty()) {
			return "";
		}

		column = getRealColumnIndex(column);
		return (String) ((Vector) data.get(0)).get(column);
	}

	public Vector getRealColumnName() {
		if (data == null || data.isEmpty()) {
			return null;
		}

		return (Vector) data.get(0);
	}

	public String getRealColumnName(int column) {
		if (data == null || data.isEmpty()) {
			return "";
		}

		column = getRealColumnIndex(column);
		return (String) ((Vector) data.get(0)).get(column);
	}

	public Vector getColumnComment() {
		if (data == null || data.isEmpty()) {
			return null;
		}

		return (Vector) data.get(4);
	}

	public String getColumnComment(int column) {
		if (data == null || data.isEmpty()) {
			return "";
		}

		column = getRealColumnIndex(column);
		return (String) ((Vector) data.get(4)).get(column);
	}

	public Class getColumnClass(int c) {
		return Object.class;
	}

	public Vector getKeyVector() {
		if (data == null || data.isEmpty()) {
			return null;
		}

		return (Vector) data.get(3);
	}

	public Vector getSizeVector() {
		if (data == null || data.isEmpty()) {
			return null;
		}

		return (Vector) data.get(2);
	}

	public boolean isCellEditable(int row, int col) {
		if (tableType == PJConst.TABLE_TYPE_VIEW) {
			return false;
		} else if (tableType == PJConst.TABLE_TYPE_TABLE) {
			if (getColumnJavaType(col) == Object.class) {
				return false;
			}

			return true;
		}

		return false;
	}

	public void setValueAt(Object aValue, int row, int column) {
		if (data == null || data.isEmpty()) {
			return;
		}

		column = getRealColumnIndex(column);

		if (table != null) {
			try {
				Class javaType = getColumnJavaType(column);
				Object orgValue = ((Vector) data.get(row + extraTopSize)).get(column);
				Object orgStrValue = StringUtil.getStringValue(javaType, orgValue);

				if (aValue == null || aValue.equals("")) {
					if (orgStrValue == null || orgStrValue.equals("")) {
						isChanged = false;
						return;
					}
				} else if (aValue.equals(orgStrValue)) {
					isChanged = false;
					return;
				}

				aValue = StringUtil.getConvertValueOfType(javaType, aValue);
			} catch (Exception e) {
				return;
			}
		}

		if (orgRowData == null) {
			orgRowData = (Vector) getRowData(row).clone();
		}
		isChanged = true;

		((Vector) data.get(row + extraTopSize)).set(column, aValue);
	}

	public void addRow(Vector rowData) {
		if (data == null || data.isEmpty()) {
			return;
		}

		fireTableRowsInserted(getRowCount(), getRowCount() + 1);
		data.add(rowData);
	}

	public void addRows(Vector rowsData) {
		if (data == null || data.isEmpty()) {
			return;
		}

		fireTableRowsInserted(getRowCount(), getRowCount() + rowsData.size());
		data.addAll(rowsData);
	}

	public void insertRow(Vector rowData, int row) {
		if (rowData == null || rowData.isEmpty()) {
			return;
		}

		fireTableRowsInserted(0, getRowCount() + 1);
		data.add(row + extraTopSize, rowData);
	}

	public void removeRow(int row) {
		if (data == null || data.isEmpty()) {
			return;
		}

		fireTableRowsDeleted(0, getRowCount() - 1);
		data.remove(row + extraTopSize);
	}

	public void removeAllRows() {
		if (data == null || data.isEmpty()) {
			return;
		}

		int rows = getRowCount();
		if (rows > 0) {
			fireTableRowsDeleted(0, rows - 1);
			for (int i = rows + extraTopSize - 1; i >= extraTopSize; i--) {
				data.remove(i);
			}
		}
	}

	/**
	 * reset table show
	 */
	public void resetTable() {
		Vector typeVector = (Vector) data.get(1);
		Vector sizeVector = (Vector) data.get(2);

		for (int i = 0; i < sizeVector.size(); i++) {
			String name = getColumnName(i);
			Class type = (Class) typeVector.get(i);
			int displaySize = ((Integer) sizeVector.get(i)).intValue();

			if (!showColumnFlags[i]) {
				continue;
			}
			restoreColumnWidth(i, displaySize, name, type);
		}

		table.repaint();
	}

	/**
	 * restore one column size.
	 *
	 */
	public void restoreColumnWidth(int col) {
		if (col < 0) {
			return;
		}

		col = getShowColumnIndex(col);
		if (!showColumnFlags[col]) {
			return;
		}

		Vector typeVector = (Vector) data.get(1);
		Vector sizeVector = (Vector) data.get(2);

		String name = getColumnName(col);
		Class type = (Class) typeVector.get(col);
		int displaySize = ((Integer) sizeVector.get(col)).intValue();

		restoreColumnWidth(col, displaySize, name, type);
	}

	/**
	 * restore one column size.
	 *
	 */
	private void restoreColumnWidth(int col, int displaySize, String name, Class type) {
		if (type == java.sql.Timestamp.class
				|| type == java.sql.Date.class) {
			displaySize = 15;
		}
		if (displaySize < name.getBytes().length) {
			displaySize = name.getBytes().length;
		}
		if (displaySize > 30) {
			displaySize = 30;
		}

		col = getShowColumnIndex(col);
		table.getColumnModel().getColumn(col).setPreferredWidth(displaySize * 8 + 7);
	}
	
	public DBTableModel copy() {
		DBTableModel newModel = new DBTableModel();
		newModel.data = data;
		newModel.showColumnFlags = showColumnFlags;
		newModel.table = table;
		newModel.rowHeader = rowHeader;
		newModel.tableType = tableType;
		newModel.isChanged = isChanged;
		newModel.isShowComment = isShowComment;
		newModel.orgRowData = orgRowData;

		return newModel;
	}
}