/**
 * 
 */
package org.jas.model;

import java.sql.Timestamp;

/**
 * @author sji_zhang_x
 *
 */
public class ReportResultRecord {

    /**
     * レポート名
     */
    String reportName = null;

    /**
     * 実行時間
     */
    Timestamp execTime = null;

    /**
     * 備考
     */
    String memo = null;

    /**
     * 
     */
    public ReportResultRecord() {
    }

    /**
     * @return reportName
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @param reportName セットする reportName
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * @return execTime
     */
    public Timestamp getExecTime() {
        return execTime;
    }

    /**
     * @param execTime セットする execTime
     */
    public void setExecTime(Timestamp execTime) {
        this.execTime = execTime;
    }

    /**
     * @return memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo セットする memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
}
