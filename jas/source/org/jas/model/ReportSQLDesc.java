package org.jas.model;

import java.io.Serializable;

/**
 * @author sji_zhang_x
 *
 */
@SuppressWarnings("serial")
public class ReportSQLDesc implements Serializable {

    /**
     * SQL��
     */
    private String name = null;

    /**
     * SQL������
     */
    private String desc = null;

    /**
     * SQL�{��
     */
    private String text = null;

    /**
     * �X�L�b�v�t���b�O
     */
    private boolean skip = false;


    /**
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name �Z�b�g���� name
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
     * @param desc �Z�b�g���� desc
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
     * @param text �Z�b�g���� text
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
     * @param skip �Z�b�g���� skip
     */
    public void setSkip(boolean skip) {
        this.skip = skip;
    }


    /**
     * �f�B�o�O�p�̃��b�\�h
     *
     * @return String �S���t�B�[���h�̒l
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
