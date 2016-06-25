/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.template.util;

import com.template.meta.ManagementTemplateMeta;
import java.util.List;
import java.util.Map;


/**
 *
 * @author mayank
 */
public class TemplateContainer {
    
    private ManagementTemplateMeta reportMeta;
     private Map<String, List<Map<String, String>>> dbrdData;
    /**
     * @return the reportMeta
     */
    public ManagementTemplateMeta getReportMeta() {
        return reportMeta;
    }

    /**
     * @param reportMeta the reportMeta to set
     */
    public void setReportMeta(ManagementTemplateMeta reportMeta) {
        this.reportMeta = reportMeta;
    }

    /**
     * @return the dbrdData
     */
    public Map<String, List<Map<String, String>>> getDbrdData() {
        return dbrdData;
    }

    /**
     * @param dbrdData the dbrdData to set
     */
    public void setDbrdData(Map<String, List<Map<String, String>>> dbrdData) {
        this.dbrdData = dbrdData;
    }

    
}
