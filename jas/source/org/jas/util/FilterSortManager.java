package org.jas.util;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import org.jas.model.TableFilterSortData;

/**
 *
 * @author í£Å@äwåR
 * @version 1.0
 */
public class FilterSortManager {

	/**
	 * key is the connection string
	 * value is also a HashMap, the map's key is tablename
	 * value is a TableFilterSortData object
	 */
	static HashMap filterSortMap = new HashMap();


	/**
	 * default constructor
	 */
	public FilterSortManager() {}

	/**
	 * init the orginal filter and sort
	 */
	public static synchronized void init(File file) {
		if (file.exists()) {
			try {
				filterSortMap = (HashMap) FileManager.readObjectFromFile(file);
			} catch (Exception e) {}
		}
	}

	/**
	 * update the filter and sort value
	 */
	public static void updateFile(File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileManager.printObjectToFile(file, filterSortMap);
		} catch (Exception e) {}
	}

	/**
	 * show whether it has filter or sort
	 */
	public static boolean hasFilterAndSort(String connectionStr, String table) {
		String filter = getFilter(connectionStr, table);
		if (filter != null && !filter.equals("")) {
			return true;
		}
		String sort = getSort(connectionStr, table);
		if (sort != null && !sort.equals("")) {
			return true;
		}

		return false;
	}

	/**
	 * get the sort string
	 */
	public static String getSort(String connectionStr, String table) {
		HashMap tableFSMap = (HashMap) filterSortMap.get(connectionStr);

		if (tableFSMap != null) {
			TableFilterSortData tableFSData = (TableFilterSortData) tableFSMap.get(table);

			if (tableFSData != null) {
				Vector sortList = tableFSData.getSort();

				if (sortList == null || sortList.isEmpty()) {
					return null;
				}

				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < sortList.size(); i++) {
					sb.append(sortList.get(i));
					if (i < sortList.size() - 1) {
						sb.append(",");
					}
				}

				return sb.toString();
			}
		}

		return null;
	}

	/**
	 * get the sort as vector
	 */
	public static Vector getSortVector(String connectionStr, String table) {
		HashMap tableFSMap = (HashMap) filterSortMap.get(connectionStr);

		if (tableFSMap != null) {
			TableFilterSortData tableFSData = (TableFilterSortData) tableFSMap.get(table);

			if (tableFSData != null) {
				return tableFSData.getSort();
			}
		}

		return null;
	}

	/**
	 * set sort data from the gui
	 */
	public static void setSort(String connectionStr, String table, Vector sortData) {
		HashMap tableFSMap = (HashMap) filterSortMap.get(connectionStr);

		if (tableFSMap == null) {
			tableFSMap = new HashMap();
			filterSortMap.put(connectionStr, tableFSMap);
		}

		TableFilterSortData tableFSData = (TableFilterSortData) tableFSMap.get(table);
		if (tableFSData == null) {
			tableFSData = new TableFilterSortData();
			tableFSMap.put(table, tableFSData);
		}

		tableFSData.setSort(sortData);
	}

	/**
	 * get the table filter
	 */
	public static String getFilter(String connectionStr, String table) {
		HashMap tableFSMap = (HashMap) filterSortMap.get(connectionStr);

		if (tableFSMap != null) {
			TableFilterSortData tableFSData = (TableFilterSortData) tableFSMap.get(table);

			if (tableFSData != null) {
				return tableFSData.getFilter();
			}
		}

		return null;
	}

	/**
	 * set the filter to the table
	 */
	public static void setFilter(String connectionStr, String table, String filter) {
		HashMap tableFSMap = (HashMap) filterSortMap.get(connectionStr);

		if (tableFSMap == null) {
			tableFSMap = new HashMap();
			filterSortMap.put(connectionStr, tableFSMap);
		}

		TableFilterSortData tableFSData = (TableFilterSortData) tableFSMap.get(table);
		if (tableFSData == null) {
			tableFSData = new TableFilterSortData();
			tableFSMap.put(table, tableFSData);
		}

		tableFSData.setFilter(filter);
	}

	/**
	 * clear filter and sorter by connectionStr
	 *
	 */
	public static void clearFilterSort(String connectionStr) {
		HashMap tableFSMap = (HashMap) filterSortMap.get(connectionStr);

		if (tableFSMap != null) {
			filterSortMap.remove(connectionStr);
		}
	}
}