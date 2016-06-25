/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author progen
 */
public class ReportParameterValue implements Serializable {

    private static final long serialVersionUID = 2766680598121909L;
    String parameterElementId;
    List<String> parameterValue;
    boolean excluded;

    public String getElementId() {
        return parameterElementId;
    }

    public List<String> getParameterValues() {
        return parameterValue;
    }

    public ReportParameterValue(String elementId) {
        this.parameterElementId = elementId;
        //this.parameterValue = new ArrayList<String>();
        this.excluded = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && (o instanceof ReportParameterValue)) {
            //code modified by Amar
//            if ( this.parameterElementId.equals(((ReportParameterValue)o).getElementId())
//                 && this.parameterValue.equals(((ReportParameterValue)o).getParameterValues())
//                 && this.excluded == ((ReportParameterValue)o).excluded )
//            {
//                return true;
//            }
            if (this.parameterElementId.equals(((ReportParameterValue) o).getElementId())
                    && this.excluded == ((ReportParameterValue) o).excluded) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object clone() {
        ReportParameterValue cloneParamVal = new ReportParameterValue(this.getElementId());
        cloneParamVal.updateParamterValue(this.parameterValue);
        return cloneParamVal;
    }

    public static Predicate<ReportParameterValue> getReportParameterPredicate(final String paramEleId) {
        Predicate<ReportParameterValue> predicate = new Predicate<ReportParameterValue>() {

            public boolean apply(ReportParameterValue input) {
                if (input.getElementId().equals(paramEleId)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    public void updateParamterValue(List<String> value) {
        this.parameterValue = value;
    }

    public void updateParamterAsExcluded(boolean excluded) {
        this.excluded = excluded;
    }

    @Override
    public String toString() {
        return ("Parameter Element " + this.parameterElementId + " Value " + this.parameterValue);

    }

    public String toXml() {
        StringBuffer repValueXml = new StringBuffer("");
        repValueXml.append("<ReportParameter>");
        if (parameterElementId != null) {
            repValueXml.append("<ElementId>");
            repValueXml.append(parameterElementId);
            repValueXml.append("</ElementId>");
        }
        if (parameterValue != null) {
            repValueXml.append("<Value>");
            repValueXml.append(parameterValue);
            repValueXml.append("</Value>");
        }
        repValueXml.append("</ReportParameter>");
        return repValueXml.toString();
    }
}
