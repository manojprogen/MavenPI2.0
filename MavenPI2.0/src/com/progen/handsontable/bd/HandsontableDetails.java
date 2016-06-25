/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.handsontable.bd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author progen
 */
public class HandsontableDetails implements Serializable {

    private static final long serialVersionUID = 752647152776147L;
    private static Object formaulaMap;
    private String reportName;
    private String userId;
    private String reportId;
    public List measureNames = new ArrayList();
    public List reportTableData = new ArrayList();
    public HashMap<String, HashMap> formulaMap = new HashMap<String, HashMap>();

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
