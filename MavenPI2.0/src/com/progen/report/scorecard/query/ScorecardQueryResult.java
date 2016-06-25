/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.query;

import com.google.common.base.Predicate;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ScorecardQueryResult {

    private String scoreMemberId;
    private PbReturnObject retObj;
    private PbReturnObject priorRetObj;
    private int noOfDays;
    private int priorNoOfDays;

    public PbReturnObject getPriorRetObj() {
        return priorRetObj;
    }

    public void setPriorRetObj(PbReturnObject priorRetObj) {
        this.priorRetObj = priorRetObj;
    }

    public PbReturnObject getRetObj() {
        return retObj;
    }

    public void setRetObj(PbReturnObject retObj) {
        this.retObj = retObj;
    }

    public String getScoreMemberId() {
        return scoreMemberId;
    }

    public void setScoreMemberId(String scoreMemberId) {
        this.scoreMemberId = scoreMemberId;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public int getPriorNoOfDays() {
        return priorNoOfDays;
    }

    public void setPriorNoOfDays(int priorNoOfDays) {
        this.priorNoOfDays = priorNoOfDays;
    }

    public static Predicate<ScorecardQueryResult> getResultForMember(final String memberId) {
        Predicate<ScorecardQueryResult> scoreCardQuery = new Predicate<ScorecardQueryResult>() {

            public boolean apply(ScorecardQueryResult input) {
                if (input.getScoreMemberId() != null && memberId.equals(input.getScoreMemberId())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return scoreCardQuery;
    }
}
