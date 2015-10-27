/**
 * 
 */
package org.jas.model;

import java.util.ArrayList;

/**
 * Ú‘±‚²‚Æ‚É•Û‘¶‚³‚ê‚éİ’èî•ñ
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
     * @param favorTableList ƒZƒbƒg‚·‚é favorTableList
     */
    public void setFavorTableList(ArrayList<String> favorTableList) {
        this.favorTableList = favorTableList;
    }

}
