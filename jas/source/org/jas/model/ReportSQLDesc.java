package org.jas.model;

import java.io.Serializable;

/**
 * @author sji_zhang_x
 *
 */
@SuppressWarnings("serial")
public class ReportSQLDesc implements Serializable {

    /**
     * SQL名
     */
    private String name = null;

    /**
     * SQL説明文
     */
    private String desc = null;

    /**
     * SQL本体
     */
    private String text = null;

    /**
     * スキップフラッグ
     */
    private boolean skip = false;


    /**
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name セットする name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return desc
     */
    public String getDesc() {
        return desc;
    }


    /**
     * @param desc セットする desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }


    /**
     * @return text
     */
    public String getText() {
        return text;
    }


    /**
     * @param text セットする text
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     * @return skip
     */
    public boolean isSkip() {
        return skip;
    }


    /**
     * @param skip セットする skip
     */
    public void setSkip(boolean skip) {
        this.skip = skip;
    }


    /**
     * ディバグ用のメッソド
     *
     * @return String 全部フィールドの値
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("name:        " + name + "\n");
        sb.append("desc:        " + desc + "\n");
        sb.append("text:        " + text + "\n");
        sb.append("skip:        " + skip + "\n");

        return sb.toString();
    }

}
