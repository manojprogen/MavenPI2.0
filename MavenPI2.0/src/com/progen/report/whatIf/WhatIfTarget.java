/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

import com.google.common.base.Predicate;
import com.progen.query.RTMeasureElement;
import com.progen.report.formula.Formula;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author progen
 */
public class WhatIfTarget {

    private String targetElementId = "A_WF_TRG_";
    Formula targetFormula;
    private String targetMeasName;

    // private String reportId;
    public WhatIfTarget(int elmtCount, String measureName, String formula) {

        this.targetMeasName = measureName;
        this.targetElementId = targetElementId + elmtCount;
        this.targetFormula = new Formula(formula);


    }

    public WhatIfTarget(String targetMeasName) {
        this.targetMeasName = targetMeasName;
    }

    public boolean equals(Object whatIfTarget) {
        if (whatIfTarget != null && whatIfTarget instanceof WhatIfTarget) {
            if (this.getTargetMeasureName().equals(((WhatIfTarget) whatIfTarget).getTargetMeasureName())) {
                return true;
            }
        }
        return false;
    }

    public String getTargetMeasureName() {
        return targetMeasName;
    }

    public void upDateWhatIfTarget(String formula) {
        this.targetFormula.updateFormula(formula);
    }

    public Formula getTargetFormula() {
        return this.targetFormula;
    }

    public String getTargetMeasureId() {
        return this.targetElementId;
    }

    public String toXML() {
        StringBuilder whatIfTargetSb = new StringBuilder();
        whatIfTargetSb.append("<measure>");
        whatIfTargetSb.append("<elementID>").append(getTargetMeasureId());
        whatIfTargetSb.append("</elementID>");
        whatIfTargetSb.append("<measureName>").append(getTargetMeasureName());
        whatIfTargetSb.append("</measureName>");
        whatIfTargetSb.append("<formula>");
        whatIfTargetSb.append("<evaluation>").append(getTargetFormula().getFormulaExpr());
        whatIfTargetSb.append("</evaluation>");
        whatIfTargetSb.append("</formula>");
        whatIfTargetSb.append("</measure>");

        return whatIfTargetSb.toString();

    }

    public boolean isMeasurePresentInFormula(String trgtmeas) {
        trgtmeas = trgtmeas + RTMeasureElement.WHATIF.getColumnType();
        Set<String> rhsMeasures = new HashSet<String>();
        boolean result = false;
        rhsMeasures = getTargetFormula().getFormulaMeasureSet();

        return rhsMeasures.contains(trgtmeas);
    }

    public static Predicate<WhatIfTarget> getTargetMeasurePredicate(final String trgtMeasId) {
        final String containerTrgtMeasId = trgtMeasId + RTMeasureElement.WHATIFTARGET.getColumnType();
        Predicate<WhatIfTarget> targetPredicate = new Predicate<WhatIfTarget>() {

            public boolean apply(WhatIfTarget input) {
                if (input.getTargetFormula().getFormulaMeasureSet().contains(containerTrgtMeasId) || input.getTargetMeasureId().equalsIgnoreCase(trgtMeasId)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return targetPredicate;
    }

    public void setTargetMeasName(String targetMeasName) {
        this.targetMeasName = targetMeasName;
    }
}
