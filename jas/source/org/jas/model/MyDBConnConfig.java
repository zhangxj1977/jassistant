/**
 * 
 */
package org.jas.model;

import java.util.ArrayList;

/**
 * �ڑ����Ƃɕۑ������ݒ���
 * 
 * @author sji_zhang_x
 *
 */
public class MyDBConnConfig {

    ArrayList<String> favorTableList = new ArrayList<String>();

    /**
     * 
     */
    public MyDBConnConfig() {
    }

    /**
     * @return favorTableList
     */
    public ArrayList<String> getFavorTableList() {
        return favorTableList;
    }

    /**
     * @param favorTableList �Z�b�g���� favorTableList
     */
    public void setFavorTableList(ArrayList<String> favorTableList) {
        this.favorTableList = favorTableList;
    }

}
