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
     * �p�����[�^��
     */
    private String name = null;

    /**
     * �p�����[�^�l
     */
    private String value = null;


    /**
     * �p�����[�^�����擾����
     *
     * @return String �p�����[�^��
     */
    public String getName() {
        return this.name;
    }

    /**
     * �p�����[�^����ݒ肷��
     *
     * @param name �V�����p�����[�^��
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * �p�����[�^�l���擾����
     *
     * @return String �p�����[�^�l
     */
    public String getValue() {
        return this.value;
    }

    /**
     * �p�����[�^�l��ݒ肷��
     *
     * @param value �V�����p�����[�^�l
     */
    public void setValue(String value) {
        this.value = value;
    }


    /**
     * �f�B�o�O�p�̃��b�\�h
     *
     * @return String �S���t�B�[���h�̒l
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("name:        " + name + "\n");
        sb.append("value:        " + value + "\n");

        return sb.toString();
    }
   
}
