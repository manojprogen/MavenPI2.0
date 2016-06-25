/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.bd;

import com.progen.charts.ChartInfo;
import com.progen.report.ReportParameter;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.query.TrendDataSet;
import com.progen.report.query.TrendDataSetHelper;
import com.progen.report.scorecard.Score;
import com.progen.report.scorecard.ScoreCard;
import com.progen.report.scorecard.ScoreCardMember;
import com.progen.report.scorecard.ScorecardComponent;
import com.progen.report.scorecard.db.ScorecardDAO;
import com.progen.report.scorecard.query.ScorecardQueryDetails;
import com.progen.report.scorecard.query.ScorecardQueryResult;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jfree.chart.JFreeChart;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ScorecardHistoryBuilder {

    public String loadScorecardHistory(HttpServletRequest request, PrintWriter out, String scardId, String scardMemId, String userId, String timeLevel) {
        StringBuilder history = new StringBuilder();
        HttpSession session = request.getSession();
        String contextPath = request.getContextPath();
        ScorecardBuilder builder = new ScorecardBuilder();
        List<String> scardIds = new ArrayList<String>();
        scardIds.add(scardId);
        ScorecardDAO dao = new ScorecardDAO();
        List<ScoreCard> scorecardList = dao.getScoreCards(scardIds);
        Map<String, Double> histScores = null;

        if (scorecardList != null && !(scorecardList.isEmpty())) {
            ScoreCard scorecard = scorecardList.get(0);

            histScores = buildScorecardHistory(scorecard, scardMemId, timeLevel, userId);

            if (histScores != null) {
                PbReturnObject scoreRetObj = createScoreReturnObject(histScores);
                TrendDataSet scoreTrendDataSet = new TrendDataSet(scoreRetObj);
                String chartPath = createHistoryChart(session, out, scoreRetObj);
                chartPath = contextPath + "/" + chartPath;

                BigDecimal threeMonthsAvg = scoreTrendDataSet.findLastNPeriodAverage("Score", 3);
                BigDecimal sixMonthsAvg = scoreTrendDataSet.findLastNPeriodAverage("Score", 6);
                BigDecimal nineMonthsAvg = scoreTrendDataSet.findLastNPeriodAverage("Score", 9);
                BigDecimal twelveMonthsAvg = scoreTrendDataSet.findLastNPeriodAverage("Score", 12);
                BigDecimal maxInPeriod = scoreTrendDataSet.findMaxInPeriod("Score");
                BigDecimal minInPeriod = scoreTrendDataSet.findMinInPeriod("Score");

                //Create the chart area
                history.append("<div id='historyChart'>");
                history.append("<center>");
                history.append("<img border=\"0\" align=\"top\" src=\"").append(chartPath).append("\"");
                history.append("</center>");
                history.append("</div>");

                //Create the corresponding table area
                history.append("<div id='historyTable' style='display:none'>");
                history.append("<table border=\"1\" class=\"tablesorter\"  style=\"border-collapse: collapse\">");
                history.append("<thead><tr>");
                history.append("<th>Period</th>");
                history.append("<th>Score</th>");
                history.append("</tr></thead><tbody>");
                for (int i = 0; i < scoreRetObj.getRowCount(); i++) {
                    history.append("<tr>");
                    history.append("<td>").append(scoreRetObj.getFieldValueString(i, 0)).append("</td>");
                    history.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(scoreRetObj.getFieldValueBigDecimal(i, 1))).append("</td>");
                    history.append("</tr>");
                }
                history.append("</tbody></table></div>");

                history.append("<table><tr>");
                history.append("<td valign=\"top\" style=\"width: 50%;\" class=\"myHead\">Maximum in period</td>");
                history.append("<td><input type=\"text\" readonly value=\"").append(NumberFormatter.getModifiedNumber(maxInPeriod)).append("\" ></td>");
                history.append("<td valign=\"top\" style=\"width: 50%;\" class=\"myHead\">Minimum in period</td>");
                history.append("<td><input type=\"text\" readonly value=\"").append(NumberFormatter.getModifiedNumber(minInPeriod)).append("\"></td>");
                history.append("</tr><tr>");
                history.append("<td valign=\"top\" style=\"width: 50%;\" class=\"myHead\">Average in 3 ").append(timeLevel).append("</td>");
                history.append("<td><input type=\"text\" readonly value=\"").append(NumberFormatter.getModifiedNumber(threeMonthsAvg)).append("\"></td>");
                history.append("<td valign=\"top\" style=\"width: 50%;\" class=\"myHead\">Average in 6 ").append(timeLevel).append("</td>");
                history.append("<td><input type=\"text\" readonly value=\"").append(NumberFormatter.getModifiedNumber(sixMonthsAvg)).append("\"></td>");
                history.append("</tr></table>");
            }
        }

        return history.toString();
    }

    private Map<String, Double> buildScorecardHistory(ScoreCard scorecard, String scardMemId, String timeLevel, String userId) {
        Map<String, Double> histScores = null;

        if (scorecard.isNestedScorecard()) {
            histScores = buildNestedScorecardHistory(scorecard, scardMemId, timeLevel, userId);
        } else if (scorecard.isDimensionBasedScorecard()) {
            histScores = buildDimensionScorecardHistory(scorecard, scardMemId, timeLevel, userId);
        } else {
            histScores = buildMeasureScorecardHistory(scorecard, scardMemId, timeLevel, userId);
        }

        return histScores;
    }

    private Map<String, Double> buildNestedScorecardHistory(ScoreCard scorecard, String scardMemId, String timeLevel, String userId) {
        Map<String, Double> histScores = new LinkedHashMap<String, Double>();
        boolean membersAvailable = false;

        for (ScorecardComponent component : scorecard.getMemberList()) {
            if (component instanceof ScoreCardMember) {
                membersAvailable = true;
            }
        }

        if (membersAvailable) {
            //Get the historic score of the members
            buildMeasureScorecardHistory(scorecard, scardMemId, timeLevel, userId);
        }

        for (ScorecardComponent component : scorecard.getMemberList()) {
            if (component instanceof ScoreCard) {
                ScoreCard childScard = (ScoreCard) component;
                Map<String, Double> scScore = buildScorecardHistory(childScard, null, timeLevel, userId);
                childScard.setHistoricScores(scScore);
            }
        }

        scorecard.generateHistoricScores();
        histScores = scorecard.getHistoricScores();
        return histScores;
    }

    private Map<String, Double> buildMeasureScorecardHistory(ScoreCard scorecard, String scardMemId, String timeLevel, String userId) {
        Map<String, Double> histScores = new LinkedHashMap<String, Double>();
        ScorecardBuilder builder = new ScorecardBuilder();
        String folderId = scorecard.getFolderId();
        ArrayList<ScorecardQueryDetails> details = new ArrayList<ScorecardQueryDetails>();
        for (ScorecardComponent comp : scorecard.getMemberList()) {
            if (comp instanceof ScoreCardMember) {
                details.addAll((Collection<? extends ScorecardQueryDetails>) comp.getQueryDetails());
            }
        }
        List<String> measIds = new ArrayList<String>();
        for (ScorecardQueryDetails qd : details) {
            measIds.add(qd.getMeasElementId());
        }

        TrendDataSet dataSet = TrendDataSetHelper.buildTrendDataSet(measIds, timeLevel, userId, folderId);
        PbReturnObject retObj = dataSet.getReturnObject();
        Map<String, Integer> noOfDays = dataSet.getNoOfDays();

        List<ScorecardQueryResult> queryResults = new ArrayList<ScorecardQueryResult>();
        for (ScorecardComponent comp : scorecard.getMemberList()) {
            if (comp instanceof ScoreCardMember) {
                ScoreCardMember member = (ScoreCardMember) comp;
                String memberId = member.getMemberId();
                ScorecardQueryResult result = new ScorecardQueryResult();
                result.setScoreMemberId(memberId);
                result.setPriorRetObj(null);
                result.setPriorNoOfDays(0);
                result.setRetObj(retObj);

                queryResults.add(result);
            }
        }

        for (int i = 0; i < retObj.getRowCount(); i++) {
        }
        // For each row, create a new score object, store the results
        while (retObj.getRowCount() > 0) {
            String period = retObj.getFieldValueString(0, 0);
            int periodDays = noOfDays.get(period);
            for (ScorecardQueryResult result : queryResults) {
                result.setNoOfDays(periodDays);
            }
            builder.buildScoreObject(scorecard, queryResults);

            for (ScorecardComponent component : scorecard.getMemberList()) {
                Score score = component.getScore();
                if (score != null) {
                    Double currScore = score.getCurrentScore();
                    Map<String, Double> compHistoricScores = component.getHistoricScores();
                    if (compHistoricScores == null) {
                        compHistoricScores = new LinkedHashMap<String, Double>();
                        component.setHistoricScores(compHistoricScores);
                    }
                    compHistoricScores.put(period, currScore);
                }
            }

            if (scardMemId == null || "".equals(scardMemId)) {
                Score score = scorecard.getScore();
                histScores.put(period, score.getCurrentScore());
            } else {
                ScorecardComponent comp = scorecard.getScorecardComponent(scardMemId);
                Score score = comp.getScore();
                histScores.put(period, score.getCurrentScore());

            }

            retObj.deleteRow(0);

        }
        return histScores;
    }

    private Map<String, Double> buildDimensionScorecardHistory(ScoreCard scorecard, String scardMemId, String timeLevel, String userId) {
        Map<String, Double> histScores = new LinkedHashMap<String, Double>();
        ScorecardBuilder builder = new ScorecardBuilder();
        String folderId = scorecard.getFolderId();
        Iterable<ScorecardQueryDetails> details = scorecard.getQueryDetails();
        List<String> measIds = new ArrayList<String>();
        for (ScorecardQueryDetails qd : details) {

            if (!(measIds.contains(qd.getMeasElementId()))) {
                measIds.add(qd.getMeasElementId());
            }
        }

        String dimId = "";
        StringBuilder dimValues = new StringBuilder();
        List<String> dimValList = new ArrayList<String>();

        for (ScorecardComponent comp : scorecard.getMemberList()) {
            if (comp instanceof ScoreCardMember) {
                ScoreCardMember member = (ScoreCardMember) comp;
                dimId = member.getDimElementId();
                dimValList.add(member.getDimValue());
//                dimValues.append(",").append(member.getDimValue());
            }
        }

//        if (dimValues.length() > 0)
//            dimValues.replace(0, 1, "");

        ReportParameter repParam = new ReportParameter();
        repParam.setParameter(dimId, dimValList, false);
        ArrayList<String> rowViewBys = new ArrayList<String>();
        rowViewBys.add(dimId);
        repParam.setViewBys(rowViewBys, new ArrayList<String>());

        HashMap<String, TrendDataSet> datasets = TrendDataSetHelper.buildTrendDataSet(measIds, repParam, timeLevel, userId, folderId);
        int noOfPeriods = 0;
        List<String> periodList = new ArrayList<String>();

        Map<String, PbReturnObject> periodReturnObjects = new LinkedHashMap<String, PbReturnObject>();
        Map<String, Integer> noOfDays = null;
        Set<String> dimValuesSet = datasets.keySet();
        Iterator<String> dimValuesIter = dimValuesSet.iterator();
        dimId = "A_" + dimId;
        while (dimValuesIter.hasNext()) {
            String dimValue = dimValuesIter.next();
            TrendDataSet trendDataSet = datasets.get(dimValue);
            noOfDays = trendDataSet.getNoOfDays();
            PbReturnObject trendRetObj = trendDataSet.getReturnObject();

            String[] colNames = trendRetObj.getColumnNames();
            for (int i = 0; i < trendRetObj.getRowCount(); i++) {
                String periodName = trendRetObj.getFieldValueString(i, 0);
                PbReturnObject periodRetObj = periodReturnObjects.get(periodName);
                if (periodRetObj == null) {
                    periodRetObj = new PbReturnObject();
                    periodReturnObjects.put(periodName, periodRetObj);
                    ArrayList<String> cols = new ArrayList<String>();
                    cols.add(dimId);
                    for (int j = 1; j < colNames.length; j++) {
                        cols.add(colNames[j]);
                    }
                    periodRetObj.setColumnNames((String[]) cols.toArray(new String[0]));
                }

                periodRetObj.setFieldValue(dimId, dimValue);
                for (int j = 1; j < colNames.length; j++) {
                    periodRetObj.setFieldValue(colNames[j], trendRetObj.getFieldValue(i, colNames[j]));
                }
                periodRetObj.addRow();
            }
        }

        Set<String> periodKeySet = periodReturnObjects.keySet();
        Iterator<String> periodKeyIter = periodKeySet.iterator();

        while (periodKeyIter.hasNext()) {

            String periodName = periodKeyIter.next();
            PbReturnObject retObj = periodReturnObjects.get(periodName);

            List<ScorecardQueryResult> queryResults = new ArrayList<ScorecardQueryResult>();
            for (ScorecardComponent comp : scorecard.getMemberList()) {
                if (comp instanceof ScoreCardMember) {
                    ScoreCardMember member = (ScoreCardMember) comp;
                    String memberId = member.getMemberId();
                    ScorecardQueryResult result = new ScorecardQueryResult();
                    result.setScoreMemberId(memberId);
                    result.setPriorRetObj(null);
                    result.setPriorNoOfDays(0);
                    result.setRetObj(retObj);
                    if (noOfDays != null) {
                        result.setNoOfDays(noOfDays.get(periodName));
                    }
                    queryResults.add(result);
                }
            }

            builder.buildScoreObject(scorecard, queryResults);

            for (ScorecardComponent component : scorecard.getMemberList()) {
                Score score = component.getScore();
                if (score != null) {
                    Double currScore = score.getCurrentScore();
                    Map<String, Double> compHistoricScores = component.getHistoricScores();
                    if (compHistoricScores == null) {
                        compHistoricScores = new LinkedHashMap<String, Double>();
                        component.setHistoricScores(compHistoricScores);
                    }
                    compHistoricScores.put(periodName, currScore);
                }
            }

            if (scardMemId == null || "".equals(scardMemId)) {
                Score score = scorecard.getScore();
                histScores.put(periodName, score.getCurrentScore());
            } else {
                ScorecardComponent comp = scorecard.getScorecardComponent(scardMemId);
                Score score = comp.getScore();
                histScores.put(periodName, score.getCurrentScore());
            }
        }
        return histScores;
    }

    private String createHistoryChart(HttpSession session, PrintWriter out, PbReturnObject retObj) {

        ChartInfo chartInfo = new ChartInfo();
        JFreeChart chart = chartInfo.generateChartDataSet(retObj, "Score", 1, "Time", "Score", true, true);
        String chartPath = chartInfo.generateChartPath(session, out, chart, 650, 300);
        return chartPath;
    }

    private PbReturnObject createScoreReturnObject(Map<String, Double> historyMap) {
        PbReturnObject retObj = new PbReturnObject();
        ArrayList<String> colNames = new ArrayList<String>();
        colNames.add("Time");
        colNames.add("Score");

        retObj.setColumnNames((String[]) colNames.toArray(new String[0]));

        Set<String> keySet = historyMap.keySet();
        Iterator<String> keyIter = keySet.iterator();

        while (keyIter.hasNext()) {
            String period = keyIter.next();
            Double score = historyMap.get(period);
            retObj.setFieldValue("Time", period);
            retObj.setFieldValue("Score", score);
            retObj.addRow();
        }
        retObj.resetViewSequence();
        return retObj;
    }
}
