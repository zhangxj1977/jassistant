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
     * �e�[�u����
     */
    String tableName = null;

    /**
     * �f�[�^���X�g
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
     * @param tableName �Z�b�g���� tableName
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
     * @param data �Z�b�g���� data
     */
    public void setData(Vector data) {
        this.data = data;
    }
}
