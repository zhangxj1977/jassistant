/**
 * 
 */
package org.jas.model;

import java.util.Vector;

/**
 * @author sji_zhang_x
 *
 */
public class ReportDataDetail {

    /**
     * テーブル名
     */
    String tableName = null;

    /**
     * データリスト
     */
    Vector data = null;

    /**
     * 
     */
    public ReportDataDetail() {
    }


    /**
     * @return tableName
     */
    public String getTableName() {
        return tableName;
    }


    /**
     * @param tableName セットする tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    /**
     * @return data
     */
    public Vector getData() {
        return data;
    }

    /**
     * @param data セットする data
     */
    public void setData(Vector data) {
        this.data = data;
    }
}
