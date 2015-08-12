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
     * ���|�[�g��
     */
    String reportName = null;

    /**
     * ���s����
     */
    Timestamp execTime = null;

    /**
     * ���l
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
     * @param reportName �Z�b�g���� reportName
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
     * @param execTime �Z�b�g���� execTime
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
     * @param memo �Z�b�g���� memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
}
