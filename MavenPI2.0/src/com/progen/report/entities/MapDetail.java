/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

import java.util.List;

/**
 *
 * @author progen
 */
public class MapDetail extends Report {

    private List<String> primaryMeasure;
    private List<String> supportingMeasures;
    private List<QueryDetail> queryDetails;

    public List<String> getPrimaryMeasure() {
        return primaryMeasure;
    }

    public void setPrimaryMeasure(List<String> primaryMeasure) {
        this.primaryMeasure = primaryMeasure;
    }

    public List<String> getSupportingMeasures() {
        return supportingMeasures;
    }

    public void setSupportingMeasures(List<String> supportingMeasures) {
        this.supportingMeasures = supportingMeasures;
    }

    public void setQueryDetails(List<QueryDetail> queryDetails) {
        this.queryDetails = queryDetails;
    }

    @Override
    public List<QueryDetail> getQueryDetails() {
        return this.queryDetails;
    }
}
