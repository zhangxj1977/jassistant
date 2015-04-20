/**
 * 
 */
package org.jas.model;

import java.io.Serializable;

/**
 * @author zhang
 *
 */
@SuppressWarnings("serial")
public class ReportParam implements Serializable {

    /**
     * パラメータ名
     */
    private String name = null;

    /**
     * パラメータ値
     */
    private String value = null;


    /**
     * パラメータ名を取得する
     *
     * @return String パラメータ名
     */
    public String getName() {
        return this.name;
    }

    /**
     * パラメータ名を設定する
     *
     * @param name 新しいパラメータ名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * パラメータ値を取得する
     *
     * @return String パラメータ値
     */
    public String getValue() {
        return this.value;
    }

    /**
     * パラメータ値を設定する
     *
     * @param value 新しいパラメータ値
     */
    public void setValue(String value) {
        this.value = value;
    }


    /**
     * ディバグ用のメッソド
     *
     * @return String 全部フィールドの値
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("name:        " + name + "\n");
        sb.append("value:        " + value + "\n");

        return sb.toString();
    }
   
}
