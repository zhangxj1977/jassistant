package org.jas.model;

import java.io.Serializable;

/**
 *
 * @author ���@�w�R
 * @version 1.0
 */
public class TableDefineData implements Serializable {

    /**
	 * table types
	 *
	 * TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
	 * "LOCAL TEMPORARY", "ALIAS", "SYNONYM
	 */
	private String tableType = null;

	/**
	 * tableName
	 */
	private String tableName = null;

	/**
	 * tableName
	 */
	private String tableSchem = null;

	/**
	 * comment
	 */
	private String comment = null;
	
	/**
	 * �f�B�t�H���g�R���X�g���N�V����
	 */
	public TableDefineData() {}


	/**
	 * tableType���擾����
	 *
	 * @return String tableType
	 */
	public String getTableType() {
		return this.tableType;
	}

	/**
	 * tableType��ݒ肷��
	 *
	 * @param tableType �V����tableType
	 */
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	/**
	 * tableName���擾����
	 *
	 * @return String tableName
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * tableName��ݒ肷��
	 *
	 * @param tableName �V����tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * tableSchem���擾����
	 *
	 * @return String tableSchem
	 */
	public String getTableSchem() {
		return this.tableSchem;
	}

	/**
	 * tableSchem��ݒ肷��
	 *
	 * @param tableSchem �V����tableSchem
	 */
	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}


	/**
     * @return comment
     */
    public String getComment() {
        return comment;
    }


    /**
     * @param comment �Z�b�g���� comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }


    /**
	 * �f�B�o�O�p�̃��b�\�h
	 *
	 * @return String �S���t�B�[���h�̒l
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("tableType:		" + tableType + "\n");
		sb.append("tableName:		" + tableName + "\n");

		return sb.toString();
	}
}