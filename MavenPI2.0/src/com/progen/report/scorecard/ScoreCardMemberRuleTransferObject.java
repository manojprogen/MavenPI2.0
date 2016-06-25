/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

import java.util.ArrayList;

/**
 *
 * @author progen
 */
public class ScoreCardMemberRuleTransferObject {

    public String measureId;
    public String dimensionId;
    public String dimensionValue;
    public String measureName;
    public String unit;
    public String period;
    public String scoreBasis;
    public String memberType;
    public String scoreCardId;
    public String userId;
    public ArrayList<Double> scores;
    public ArrayList<String> operators;
    public ArrayList<Double> stValues;
    public ArrayList<Double> endValues;
    public double targetMeasureValue;
    public String targetMeasureType;
}
