package org.jas.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractListModel;

/**
 *
 *
 *
 *
 * @author ’£@ŠwŒR
 * @version 1.0
 */

public class PJTableListModel extends AbstractListModel {
	private Collection items;

	private HashMap hideItems = null;

	public PJTableListModel() {}

	public void setDataSet(Collection dataSet) {
		items = dataSet;
		fireContentsChanged(this, 0, getSize());
	}

	public void setDataSet(Collection dataSet, HashMap hideItems) {
		this.hideItems = hideItems;
		this.items = dataSet;
		fireContentsChanged(this, 0, getSize());
	}

	public PJTableListModel(Collection dataSet) {
		setDataSet(dataSet);
	}

	public PJTableListModel(Collection dataSet, HashMap hideItems) {
		this.hideItems = hideItems;
		setDataSet(dataSet);
	}

	public void setHideItems(HashMap hideItems) {
		this.hideItems = hideItems;
		fireContentsChanged(this, 0, getSize());
	}

	public HashMap getHideItems() {
		return this.hideItems;
	}

	public int getSize() {
		if (items == null) {
			return 0;
		} else {
			if (hideItems == null) {
				return items.size();
			} else {
				Iterator it = items.iterator();
				int count = 0;

				while (it.hasNext()) {
					if (!hideItems.containsKey(it.next())) {
						count++;
					}
				}

				return count;
			}
		}
	}

	public void addDataSet(Collection dataSet) {
		Iterator it = dataSet.iterator();

		while (it.hasNext()) {
			add(it.next());
		}
	}

	public void add(Object o) {
		if (!contains(o)) {
			items.add(o);
			fireContentsChanged(this, 0, getSize());
		}
	}

	public void remove(Object o) {
		if (contains(o)) {
			items.remove(o);
			fireContentsChanged(this, 0, getSize());
		}
	}

	public boolean contains(Object o) {
		return items.contains(o);
	}

	public Collection getDataSet() {
		return items;
	}

	public Object getElementAt(int index) {
		Object tmpObject;

		if (getSize() > 0 && index >= 0 && index < getSize()) {
			Iterator it = items.iterator();
			int i = 0;

			while (it.hasNext()) {
				tmpObject = it.next();

				if (hideItems != null && hideItems.containsKey(tmpObject)) {
					continue;
				}

				if ((i++) == index) {
					return (String) tmpObject;
				}
			}
		}

		return null;
	}
	
	public int like(String o, int lastSearchIdx) {
	    String obj;
        Iterator it = items.iterator();
        int i = 0;

        while (it.hasNext()) {
            obj = (String) it.next();

            if (hideItems != null && hideItems.containsKey(obj)) {
                continue;
            }

            if (i > lastSearchIdx
                    && obj.toUpperCase().indexOf(o.toUpperCase()) >= 0) {
                return i;
            }
            i++;
        }

	    return -1;
	}
}