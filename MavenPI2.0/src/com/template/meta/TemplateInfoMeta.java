/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.template.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mayank
 */
public class TemplateInfoMeta implements Serializable{
      private static final long serialVersionUID = 75264713556229L;
      private String templateId;
      private String templateName;
      private String templateDesc;
      private List<String> measure;
      private List<String>measureIds;
      private Map<String,List<String>> measureNameMap;
      private Map<String,List<String>> measureIdMap;
      private Map<String,List<String>> pageMap;
      private Map<String,List<String>> pageIdMap;

    /**
     * @return the templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return the templateDesc
     */
    public String getTemplateDesc() {
        return templateDesc;
    }

    /**
     * @param templateDesc the templateDesc to set
     */
    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    /**
     * @return the measure
     */
    public List<String> getMeasure() {
        return measure;
    }

    /**
     * @param measure the measure to set
     */
    public void setMeasure(List<String> measure) {
        this.measure = measure;
    }

    /**
     * @return the measureIds
     */
    public List<String> getMeasureIds() {
        return measureIds;
    }

    /**
     * @param measureIds the measureIds to set
     */
    public void setMeasureIds(List<String> measureIds) {
        this.measureIds = measureIds;
    }

    /**
     * @return the measureNameMap
     */
    public Map<String,List<String>> getMeasureNameMap() {
        return measureNameMap;
    }

    /**
     * @param measureNameMap the measureNameMap to set
     */
    public void setMeasureNameMap(Map<String,List<String>> measureNameMap) {
        this.measureNameMap = measureNameMap;
    }

    /**
     * @return the measureIdMap
     */
    public Map<String,List<String>> getMeasureIdMap() {
        return measureIdMap;
    }

    /**
     * @param measureIdMap the measureIdMap to set
     */
    public void setMeasureIdMap(Map<String,List<String>> measureIdMap) {
        this.measureIdMap = measureIdMap;
    }

    /**
     * @return the pageMap
     */
    public Map<String,List<String>> getPageMap() {
        return pageMap;
    }

    /**
     * @param pageMap the pageMap to set
     */
    public void setPageMap(Map<String,List<String>> pageMap) {
        this.pageMap = pageMap;
    }

    /**
     * @return the pageIdMap
     */
    public Map<String,List<String>> getPageIdMap() {
        return pageIdMap;
    }

    /**
     * @param pageIdMap the pageIdMap to set
     */
    public void setPageIdMap(Map<String,List<String>> pageIdMap) {
        this.pageIdMap = pageIdMap;
    }
}
