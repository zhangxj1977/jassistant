/**
 * 
 */
package org.jas.model;

import java.util.ArrayList;

/**
 * 接続ごとに保存される設定情報
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
     * @param favorTableList セットする favorTableList
     */
    public void setFavorTableList(ArrayList<String> favorTableList) {
        this.favorTableList = favorTableList;
    }

}
