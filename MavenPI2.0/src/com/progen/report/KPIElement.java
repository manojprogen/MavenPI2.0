/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import java.io.Serializable;

/**
 *
 * @author progen
 */
public class KPIElement implements Serializable {

    private String elementId;
    private String refElementId;
    private String refElementType;
    private String aggregationType;
    private String elementName;
    private boolean isGroupElement = false;
    private String targetElement;
    private static final long serialVersionUID = 75264711556289L;

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getRefElementId() {
        return refElementId;
    }

    public void setRefElementId(String refElementId) {
        this.refElementId = refElementId;
    }

    public String getRefElementType() {
        return refElementType;
    }

    public void setRefElementType(String refElementType) {
        this.refElementType = refElementType;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KPIElement other = (KPIElement) obj;
        if (this.elementId != null && other.elementId != null && this.elementId.equals(other.elementId)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public boolean isIsGroupElement() {
        return isGroupElement;
    }

    public void setIsGroupElement(boolean isGroupElement) {
        this.isGroupElement = isGroupElement;
    }

    public String getTargetElement() {
        return targetElement;
    }

    public void setTargetElement(String targetElement) {
        this.targetElement = targetElement;
    }
}
