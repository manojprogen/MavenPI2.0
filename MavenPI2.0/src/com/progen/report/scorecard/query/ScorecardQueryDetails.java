/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.query;

import com.google.common.base.Predicate;

/**
 *
 * @author progen
 */
public class ScorecardQueryDetails {

    private String measElementId;
    private String paramDimId;
    private String paramValue;
    private String scoreCardMemberId;
    private String targetElementId;

    public String getParamDimId() {
        return paramDimId;
    }

    public void setParamDimId(String paramDimId) {
        this.paramDimId = paramDimId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getMeasElementId() {
        return measElementId;
    }

    public void setMeasElementId(String measElementId) {
        this.measElementId = measElementId;
    }

    public String getScoreCardMemberId() {
        return scoreCardMemberId;
    }

    public void setScoreCardMemberId(String scoreCardMemberId) {
        this.scoreCardMemberId = scoreCardMemberId;
    }

    public static Predicate<ScorecardQueryDetails> getKPIBasedScorecardMembers() {
        Predicate<ScorecardQueryDetails> scoreCardQuery = new Predicate<ScorecardQueryDetails>() {

            public boolean apply(ScorecardQueryDetails input) {
                if (input.getParamDimId() == null) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return scoreCardQuery;
    }

    public static Predicate<ScorecardQueryDetails> getDimensionBasedScorecardMembers() {
        Predicate<ScorecardQueryDetails> scoreCardQuery = new Predicate<ScorecardQueryDetails>() {

            public boolean apply(ScorecardQueryDetails input) {
                if (input.getParamDimId() != null) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return scoreCardQuery;
    }

    public static Predicate<ScorecardQueryDetails> getDimensionBasedScorecardMembers(final String dimensionId) {
        Predicate<ScorecardQueryDetails> scoreCardQuery = new Predicate<ScorecardQueryDetails>() {

            public boolean apply(ScorecardQueryDetails input) {
                if (input.getParamDimId() != null && dimensionId.equals(input.getParamDimId())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return scoreCardQuery;
    }

    public String getTargetElementId() {
        return targetElementId;
    }

    public void setTargetElementId(String targetElementId) {
        this.targetElementId = targetElementId;
    }
}
