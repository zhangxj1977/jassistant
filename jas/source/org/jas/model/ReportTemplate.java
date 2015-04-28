package org.jas.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ReportTemplate implements Serializable {

    /**
     * ���|�[�g���O
     */
    private String name = null;

    /**
     * �p�����[�^���X�g
     */
    private ArrayList<ReportParam> paramList = null;

    /**
     * SQL�����X�g
     */
    private ArrayList<ReportSQLDesc> sqlList = null;

    /**
     * �o�̓^�C�v excel/csv
     */
    private String exportType = null;
    
    /**
     * �_�����o�̓t���O
     */
    private boolean isOutComment = true;

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
     * @return paramList
     */
    public ArrayList<ReportParam> getParamList() {
        return paramList;
    }

    /**
     * @param paramList �Z�b�g���� paramList
     */
    public void setParamList(ArrayList<ReportParam> paramList) {
        this.paramList = paramList;
    }

    /**
     * @return sqlList
     */
    public ArrayList<ReportSQLDesc> getSqlList() {
        return sqlList;
    }

    /**
     * @param sqlList �Z�b�g���� sqlList
     */
    public void setSqlList(ArrayList<ReportSQLDesc> sqlList) {
        this.sqlList = sqlList;
    }

    /**
     * @return exportType
     */
    public String getExportType() {
        return exportType;
    }

    /**
     * @param exportType �Z�b�g���� exportType
     */
    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    /**
     * @return isOutComment
     */
    public boolean isOutComment() {
        return isOutComment;
    }

    /**
     * @param isOutComment �Z�b�g���� isOutComment
     */
    public void setOutComment(boolean isOutComment) {
        this.isOutComment = isOutComment;
    }

    /* (�� Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReportTemplate [paramList=" + paramList + ", sqlList="
                + sqlList + "]";
    }

}
