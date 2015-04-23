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

    /* (非 Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReportTemplate [paramList=" + paramList + ", sqlList="
                + sqlList + "]";
    }

}
