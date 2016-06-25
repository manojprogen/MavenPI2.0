/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

import com.google.common.base.Predicate;

/**
 *
 * @author progen
 */
public class MeasureGroupColumn {

    private String measID;
    private String alternateLabel;

    public MeasureGroupColumn(String measID, String alternateLabel) {
        this.measID = measID;
        this.alternateLabel = alternateLabel;
    }

    public static Predicate<MeasureGroupColumn> getMeasureGroupColumnPredicate(final String measID) {
        Predicate<MeasureGroupColumn> predicate = new Predicate<MeasureGroupColumn>() {

            public boolean apply(MeasureGroupColumn input) {
                if (input.getMeasID().equals(measID)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;

    }

    /**
     * @return the measID
     */
    public String getMeasID() {
        return measID;
    }

    public String getAlternateLabel() {
        return alternateLabel;
    }
}
