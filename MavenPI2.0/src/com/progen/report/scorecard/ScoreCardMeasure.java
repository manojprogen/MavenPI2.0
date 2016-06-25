/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author progen
 */
public class ScoreCardMeasure {

    private String measId;
    private String dimId;
    private String dimValue;
    private HashMap<String, ArrayList<ScoreCardMeasureMetrics>> measMetricsLst;

    public String getMeasId() {
        return measId;
    }

    public String getDimId() {
        return dimId;
    }

    public String getDimValue() {
        return dimValue;
    }

    public void setDimId(String dimId) {
        this.dimId = dimId;
    }

    public void setDimValue(String dimValue) {
        this.dimValue = dimValue;
    }

    public void setMeasId(String measId) {
        this.measId = measId;
    }

    public HashMap<String, ArrayList<ScoreCardMeasureMetrics>> getMeasMetricsLst() {
        return measMetricsLst;
    }

    public void setMeasMetricsLst(HashMap<String, ArrayList<ScoreCardMeasureMetrics>> measMetricsLst) {
        this.measMetricsLst = measMetricsLst;
    }
//    public String getYtdValue(String timeLevel)
//    {
//          return measMetrics.get(timeLevel).getYtdValue();
//    }
//    public String getQtdValue(String timeLevel)
//    {
//          return measMetrics.get(timeLevel).getQtdValue();
//    }
//    public String getMtdValue(String timeLevel)
//    {
//          return measMetrics.get(timeLevel).getMtdValue();
//    }
//    public String getYtdValue() {
//        return ytdValue;
//    }
//
//    public void setYtdValue(String ytdValue) {
//        this.ytdValue = ytdValue;
//    }
//
//    public String getMtdValue() {
//        return mtdValue;
//    }
//
//    public void setMtdValue(String mtdValue) {
//        this.mtdValue = mtdValue;
//    }
//
//    public String getQtdValue() {
//        return qtdValue;
//    }
//
//    public void setQtdValue(String qtdValue) {
//        this.qtdValue = qtdValue;
//    }
}
