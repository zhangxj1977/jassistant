package org.jas.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ReportTemplate implements Serializable {

    /**
     * レポート名前
     */
    private String name = null;

    /**
     * パラメータリスト
     */
    private ArrayList<ReportParam> paramList = null;

    /**
     * SQL文リスト
     */
    private ArrayList<ReportSQLDesc> sqlList = null;

    /**
     * 出力タイプ excel/csv
     */
    private String exportType = null;
    
    /**
     * 論理名出力フラグ
     */
    private boolean isOutComment = true;

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
     * @return paramList
     */
    public ArrayList<ReportParam> getParamList() {
        return paramList;
    }

    /**
     * @param paramList セットする paramList
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
     * @param sqlList セットする sqlList
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
     * @param exportType セットする exportType
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
     * @param isOutComment セットする isOutComment
     */
    public void setOutComment(boolean isOutComment) {
        this.isOutComment = isOutComment;
    }

    /* (非 Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReportTemplate [paramList=" + paramList + ", sqlList="
                + sqlList + "]";
    }

}
