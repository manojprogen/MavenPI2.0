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
public class ColorHelper implements Serializable {

    private String depElementId;
    private String evalMethod;
    private static final long serialVersionUID = 75264999556228L;

    public String getDependentMeasure() {
        return depElementId;
    }

    public void setDependentMeasure(String elementID) {
        this.depElementId = elementID;
    }

    public String getEvaluationMethod() {
        return evalMethod;
    }

    public void setEvaluationMethod(String elementType) {
        this.evalMethod = elementType;
    }
}
