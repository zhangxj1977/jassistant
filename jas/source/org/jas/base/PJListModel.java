package org.jas.base;

import java.util.Vector;

import javax.swing.AbstractListModel;


/**
 * list model for filter and sort
 *
 * @author í£Å@äwåR
 * @version 1.0
 */

public class PJListModel extends AbstractListModel {

	Vector data = new Vector();

	public PJListModel() { }

	public int getSize() {
		return data.size();
	}

	public Object getElementAt(int index) {
		if (index < 0 || index >= getSize()) {
			return null;
		}

		return data.get(index);
	}

	public void addElement(Object obj) {
		data.add(obj);
		fireIntervalAdded(this, 0, getSize());
	}

	public boolean contains(Object obj) {
		return data.contains(obj);
	}

	public void insertElement(int i, Object obj) {
		data.add(i, obj);
		fireIntervalAdded(this, 0, getSize());
	}

	public void removeElement(int index) {
		data.remove(index);
		fireIntervalRemoved(this, 0, getSize());
	}

	public void removeElement(Object obj) {
		data.remove(obj);
		fireIntervalRemoved(this, 0, getSize());
	}

	public void clear() {
		data.clear();
		fireIntervalRemoved(this, 0, getSize());
	}

	public int indexOf(Object obj) {
		return data.indexOf(obj);
	}

	public void moveUp(int index) {
		if (index > 0 && getSize() > 1) {
			Object from = data.get(index);
			Object to = data.get(index - 1);
			data.set(index, to);
			data.set(index - 1, from);
		}
		fireContentsChanged(this, 0, getSize());
	}

	public void moveDown(int index) {
		if (index < getSize() - 1 && getSize() > 1) {
			Object from = data.get(index);
			Object to = data.get(index + 1);
			data.set(index, to);
			data.set(index + 1, from);
		}
		fireContentsChanged(this, 0, getSize());
	}

	public void resetData(Vector newData) {
		if (newData == null) {
			data = new Vector();
		} else {
			data = newData;
		}

		fireContentsChanged(this, 0, getSize());
	}

	public Vector getData() {
		return data;
	}
}