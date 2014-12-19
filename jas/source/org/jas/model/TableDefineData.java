package org.jas.model;

import java.io.Serializable;

/**
 *
 * @author 張　学軍
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
	 * ディフォルトコンストラクション
	 */
	public TableDefineData() {}


	/**
	 * tableTypeを取得する
	 *
	 * @return String tableType
	 */
	public String getTableType() {
		return this.tableType;
	}

	/**
	 * tableTypeを設定する
	 *
	 * @param tableType 新しいtableType
	 */
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	/**
	 * tableNameを取得する
	 *
	 * @return String tableName
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * tableNameを設定する
	 *
	 * @param tableName 新しいtableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * tableSchemを取得する
	 *
	 * @return String tableSchem
	 */
	public String getTableSchem() {
		return this.tableSchem;
	}

	/**
	 * tableSchemを設定する
	 *
	 * @param tableSchem 新しいtableSchem
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
     * @param comment セットする comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }


    /**
	 * ディバグ用のメッソド
	 *
	 * @return String 全部フィールドの値
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("tableType:		" + tableType + "\n");
		sb.append("tableName:		" + tableName + "\n");

		return sb.toString();
	}
}