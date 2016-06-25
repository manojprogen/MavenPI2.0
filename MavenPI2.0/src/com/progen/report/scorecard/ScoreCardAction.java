/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

import java.util.Date;

/**
 *
 * @author progen
 */
public class ScoreCardAction {

    int scardActionDetailId;
    int scorecardId;
    int memberId;
    String actionItemName;
    double score;
    String actionType;
    String actionDetail;
    String impact;
    Date actionDate;
    Date startDate;
    Date endDate;

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date strtDate) {
        this.startDate = strtDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getActionDetail() {
        return actionDetail;
    }

    public void setActionDetail(String actionDetail) {
        this.actionDetail = actionDetail;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getScorecardId() {
        return scorecardId;
    }

    public void setScorecardId(int scorecardId) {
        this.scorecardId = scorecardId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getScardActionDetailId() {
        return scardActionDetailId;
    }

    public void setScardActionDetailId(int scardActionDetailId) {
        this.scardActionDetailId = scardActionDetailId;
    }

    public String getActionItemName() {
        return actionItemName;
    }

    public void setActionItemName(String actionItemName) {
        this.actionItemName = actionItemName;
    }
}