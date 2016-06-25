/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

/**
 *
 * @author progen
 */
public class WhatIfDependantMeasure {

    private String measEleId;
    private String relatedMeasure;
    private boolean isStandard = true;

    public WhatIfDependantMeasure(String dependantMeas, String relatedMeas) {
        this.measEleId = dependantMeas;
        this.relatedMeasure = relatedMeas;
    }

    public WhatIfDependantMeasure(String depenDantMeasEle) {
        this.measEleId = depenDantMeasEle;
    }

    public String getDependantMeasure() {
        return measEleId;
    }

    public String getRelatedMeasure() {
        return relatedMeasure;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof WhatIfDependantMeasure) {
            if (this.getDependantMeasure().equals(((WhatIfDependantMeasure) o).getDependantMeasure())) {
                return true;
            }
        }
        return false;
    }

    public String toXML() {
        StringBuilder dependantXml = new StringBuilder();
        dependantXml.append("<measure>");
        dependantXml.append("<dependantMeasure>" + getDependantMeasure());
        dependantXml.append("</dependantMeasure>");
        dependantXml.append("<relatedMeasure>" + getRelatedMeasure());
        dependantXml.append("</relatedMeasure>");
        dependantXml.append("<stdNonStd>" + isStandard);
        dependantXml.append("</stdNonStd>");
        dependantXml.append("</measure>");
        return dependantXml.toString();
    }

    /**
     * @return the isStandard
     */
    public boolean isIsStandard() {
        return isStandard;
    }

    /**
     * @param isStandard the isStandard to set
     */
    public void setIsStandard(boolean isStandard) {
        this.isStandard = isStandard;
    }
}
