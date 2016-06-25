/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.progen.query.RTMeasureElement;
import com.progen.query.RunTimeMeasure;
import com.progen.report.SearchFilter;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.util.sort.DataSetFilter;
import com.progen.report.util.stat.StatUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Set;
import prg.db.Container;

/**
 *
 * @author arun
 */
public class RunTimeMeasCalculator {

    DataFacade facade;

    public RunTimeMeasCalculator(DataFacade facade) {
        this.facade = facade;
    }

    public RunTimeMeasure computeRunTimeMeasure(String measure, RTMeasureElement measType, String dimension, ArrayList<String> rowViewBys, String measureType, String symbol, int precision) {

        RunTimeMeasure rtMeasure = null;
        String actualMeas = RTMeasureElement.getOriginalColumn(measure);
        if (measType == RTMeasureElement.RANK || measType == RTMeasureElement.QTDRANK || measType == RTMeasureElement.YTDRANK || measType == RTMeasureElement.PMTDRANK || measType == RTMeasureElement.PQTDRANK || measType == RTMeasureElement.PYTDRANK) {
          // changed by sandeep for Qtd ,Mtd,Ytd Ranks
            if(measType == RTMeasureElement.QTDRANK){
                actualMeas=actualMeas+"_QTD_Qtdrank";
            }else if(measType == RTMeasureElement.YTDRANK){
                  actualMeas=actualMeas+"_YTD_Ytdrank";
            
            }else if(measType == RTMeasureElement.PMTDRANK){
                  actualMeas=actualMeas+"_PMTD_PMtdrank";
            
            }else if(measType == RTMeasureElement.PQTDRANK){
                  actualMeas=actualMeas+"_PQTD_PQtdrank";
            
            }else if(measType == RTMeasureElement.PYTDRANK){
                  actualMeas=actualMeas+"_PYTD_PYtdrank";
            }
            Container container =facade.container;
                        if(actualMeas.contains("_PYtdrank")|| actualMeas.contains("_PQtdrank")|| actualMeas.contains("_PMtdrank")|| actualMeas.contains("_Qtdrank")|| actualMeas.contains("_Ytdrank")){
 rtMeasure = calculateRankpre(actualMeas,container);
                            
                        }else{
            //end of sandeep code
            rtMeasure = calculateRank(actualMeas);
                        }
        } else if (measType == RTMeasureElement.PERCENTWISE) {
            rtMeasure = calculatePercentWise(actualMeas);
        } else if (measType == RTMeasureElement.RUNNINGTOTAL) {
            rtMeasure = calculateRunningTotal(actualMeas);
        } else if (measType == RTMeasureElement.PERCENTWISESUBTOTAL) {
            if (rowViewBys.size() < 3) {
                rtMeasure = calculatePercentWiseForSubtotal(actualMeas, dimension);
            } else {
                rtMeasure = calculatePercentWiseForSubtotalMulViews(actualMeas, rowViewBys);
            }
        } else if (measType == RTMeasureElement.DEVIATIONFROMMEAN) {
            rtMeasure = calculateDeviationFromMean(actualMeas);
        } else if (measType == RTMeasureElement.GOALSEEK) {
            rtMeasure = calculateGoalSeek(actualMeas);
        } else if (measType == RTMeasureElement.USERGOALSEEK) {
            rtMeasure = calculateGoalSeekadhoc(actualMeas);
        } else if (measType == RTMeasureElement.TIMEBASED) {
            rtMeasure = calculateGoalSeekIndivTime(actualMeas);
        } else if (measType == RTMeasureElement.TIMECHANGEDPER) {
            rtMeasure = calculateGoalSeekTimePercent(actualMeas);
        } else if (measType == RTMeasureElement.USERGOALPERCENT) {
            rtMeasure = calculateUserGoalPercent(actualMeas);
        } //        start of code by Nazneen For Rank For ST in May 2014
        else if (measType == RTMeasureElement.RANKST) {
            rtMeasure = calculateRankForSubtotal(actualMeas, dimension, rowViewBys, measureType, symbol, precision);
        }
//        end of code by Nazneen For Rank For ST in May 2014

        return rtMeasure;
    }

    public RunTimeMeasure calculateRank(String measure) {
        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = StatUtil.STAT_HELPER.Rank(facade.retrieveMeasureData(measure));
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);
        return rtMeasure;
    }
public RunTimeMeasure calculateRankpre(String measure,Container container) {
        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = StatUtil.STAT_HELPER.Rank(facade.retrieveMeasureDatarank(measure,container));
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);
        return rtMeasure;
    }
    public RunTimeMeasure calculatePercentWise(String measure) {
        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = this.getPercentWiseOfColumn(measure);
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);
        return rtMeasure;
    }

    private RunTimeMeasure calculateDeviationFromMean(String actualMeas) {
        ArrayList<BigDecimal> deviationData = new ArrayList<BigDecimal>();
        BigDecimal avg = facade.getColumnAverageValue(actualMeas);

        ArrayList<BigDecimal> actualData = facade.retrieveMeasureData(actualMeas);
        BigDecimal percent = new BigDecimal(100);

        for (BigDecimal measData : actualData) {
            deviationData.add(measData.subtract(avg, MathContext.DECIMAL64).divide(avg, MathContext.DECIMAL64).multiply(percent));
        }

        RunTimeMeasure rtMeasure = new RunTimeMeasure(deviationData);
        return rtMeasure;
    }

    public RunTimeMeasure calculateRunningTotal(String measure) {
        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = StatUtil.STAT_HELPER.RunningTotal(facade.retrieveDataBasedOnViewSeq(measure));
        facade.container.setRunningTotal(runTimeMeasData);//added by sruthi otherval for running total on 14 Nov 14
        ArrayList<BigDecimal> newList = new ArrayList<BigDecimal>(facade.getActualRowCount());
        for (int i = 0; i < facade.getActualRowCount(); i++) {
            newList.add(BigDecimal.ZERO);
        }
        ArrayList<Integer> viewSequence = facade.getViewSequence();
        for (int i = 0; i < viewSequence.size(); i++) {
            newList.add(viewSequence.get(i), runTimeMeasData.get(i));
            newList.remove(viewSequence.get(i) + 1);
        }
        RunTimeMeasure rtMeasure = new RunTimeMeasure(newList);
        return rtMeasure;
    }

    public RunTimeMeasure calculatePercentWiseForSubtotal(String measure, String dimension) {
        ArrayList<BigDecimal> percentWise = new ArrayList<BigDecimal>(facade.getRowCount());

        ArrayList<String> columns = new ArrayList<String>();
        columns.add(dimension);
        columns.add(measure);

        Object[][] data = facade.retrieveDataBasedOnViewSequence(columns);
        ArrayList<Integer> viewSeq = facade.getViewSequence();

        DataSetFilter filter = new DataSetFilter();
        filter.setData(data, viewSeq);

        Set<Object> columnValues = filter.getUniqueValuesInColumn(1);

        SearchFilter searchFilter;

        //get first set
        for (Object value : columnValues) {
            searchFilter = new SearchFilter();
            searchFilter.add(dimension, "EQ", value);
            filter.setSearchFilter(searchFilter);
            viewSeq = filter.searchDataSet();
            percentWise.addAll(this.findPercentWiseForSet(viewSeq, measure));
        }


        RunTimeMeasure rtMeasure = new RunTimeMeasure(this.normalizeToNaturalOrder(percentWise, facade.getViewSequence()));
        return rtMeasure;
    }

    public RunTimeMeasure calculatePercentWiseForSubtotalMulViews(String measure, ArrayList dimension) {
        ArrayList<BigDecimal> percentWise = new ArrayList<BigDecimal>(facade.getRowCount());

        ArrayList<String> columns = new ArrayList<String>();
        for (int i = 0; i < dimension.size() - 1; i++) {
            columns.add("A_" + dimension.get(i));
        }
        columns.add(measure);

        Object[][] data = facade.retrieveDataBasedOnViewSequence(columns);
        ArrayList<Integer> viewSeq = facade.getViewSequence();

        DataSetFilter filter = new DataSetFilter();
        filter.setData(data, viewSeq);




        SearchFilter searchFilter;
        SearchFilter searchFilter1;

        Set<Object> columnValues = filter.getUniqueValuesInColumn(1);
        //get first set
        for (Object value : columnValues) {
            searchFilter = new SearchFilter();
            // searchFilter.add(dimension, "EQ", value);
            // filter.setSearchFilter(searchFilter);
            for (int i = 2; i <= columns.size() - 1; i++) {
                Set<Object> columnValues1 = filter.getUniqueValuesInColumn(i);
                for (Object value1 : columnValues1) {
                    searchFilter1 = new SearchFilter();
                    searchFilter1.add("A_" + dimension.get(0), "EQ", value);
                    searchFilter1.add("A_" + dimension.get(1), "EQ", value1);
                    filter.setSearchFilter(searchFilter1);
                    viewSeq = filter.searchDataSet();
                    percentWise.addAll(this.findPercentWiseForSet(viewSeq, measure));
                }
            }

        }


        RunTimeMeasure rtMeasure = new RunTimeMeasure(this.normalizeToNaturalOrder(percentWise, facade.getViewSequence()));
        return rtMeasure;
    }

    ArrayList<BigDecimal> normalizeToNaturalOrder(ArrayList<BigDecimal> data, ArrayList<Integer> viewSeq) {
        int actualRow;
        ArrayList<BigDecimal> percentWiseReOrdered = new ArrayList<BigDecimal>(facade.getActualRowCount());
        for (int i = 0; i < facade.getActualRowCount(); i++) {
            percentWiseReOrdered.add(BigDecimal.ZERO);
        }

        for (int i = 0; i < viewSeq.size(); i++) {
            actualRow = viewSeq.get(i);
            percentWiseReOrdered.set(actualRow, data.get(i));

        }
        return percentWiseReOrdered;
    }

    private ArrayList<BigDecimal> findPercentWiseForSet(ArrayList<Integer> viewSeq, String measure) {
        BigDecimal sum = BigDecimal.ZERO;

        //find subtotal
        for (int i = 0; i < viewSeq.size(); i++) {
            sum = sum.add(facade.getMeasureData(viewSeq.get(i), measure));
        }

        if (sum.compareTo(BigDecimal.valueOf(0.0)) == 0) {
            sum = BigDecimal.ONE;
        }


        if (sum == BigDecimal.ZERO) {
            sum = BigDecimal.ONE;
        }

        //find percentwise
        ArrayList<BigDecimal> percentWise = new ArrayList<BigDecimal>();
        BigDecimal data;
        for (int i = 0; i < viewSeq.size(); i++) {
            data = facade.getMeasureData(viewSeq.get(i), measure).divide(sum, MathContext.DECIMAL64);
            data = data.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);
            percentWise.add(data);
        }
        return percentWise;
    }

    private ArrayList<BigDecimal> getPercentWiseOfColumn(String measEleId) {
        ArrayList<BigDecimal> percentWiseData = new ArrayList<BigDecimal>();
        BigDecimal total = facade.getColumnGrandTotalValue(measEleId);
        if (total != null && total.intValue() == 0) {
            total = new BigDecimal("1");
        }
        ArrayList<BigDecimal> actualData = facade.retrieveMeasureData(measEleId);
        BigDecimal percent = new BigDecimal(100);

        for (BigDecimal measData : actualData) {
            if (total != null) {
                percentWiseData.add(measData.divide(total, MathContext.DECIMAL64).multiply(percent));
            }
        }

        return percentWiseData;
    }

    public RunTimeMeasure calculateGoalSeek(String measure) {
        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = this.getGoalSeekColumn(measure);
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);
        return rtMeasure;
    }

    private ArrayList<BigDecimal> getGoalSeekColumn(String measEleId) {
        ArrayList<BigDecimal> goalSeekData = new ArrayList<BigDecimal>();

        ArrayList<BigDecimal> actualData = facade.retrieveMeasureData(measEleId);
        ArrayList<BigDecimal> percentWiseData = new ArrayList<BigDecimal>();
        BigDecimal percent = new BigDecimal(100);
        BigDecimal data = new BigDecimal(BigInteger.ZERO);
        BigDecimal total = facade.getColumnGrandTotalValue(measEleId);
        if (total.intValue() == 0) {
            total = new BigDecimal("1");
        }

        for (BigDecimal measData : actualData) {
            percentWiseData.add(measData.divide(total, MathContext.DECIMAL64).multiply(percent));
        }
        for (BigDecimal measData : percentWiseData) {
            data = getGoalSeekData(measData, measEleId);
            goalSeekData.add(data);
        }
        return goalSeekData;
    }

    private BigDecimal getGoalSeekData(BigDecimal measData, String measEleId) {
        ArrayList<String> percetData = new ArrayList<String>();
        percetData.addAll(facade.getGoalSeekData(measEleId));
        BigDecimal data = measData.multiply(new BigDecimal((percetData.get(0)).replace(",", "")), MathContext.DECIMAL64).divide(new BigDecimal(100));
        return data;
    }

    public RunTimeMeasure calculateGoalSeekadhoc(String measure) {
        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = this.getGoalSeekColumnlist(measure);
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);
        return rtMeasure;
    }

    private ArrayList<BigDecimal> getGoalSeekColumnlist(String measEleId) {

        ArrayList<BigDecimal> goalSeekData = new ArrayList<BigDecimal>();

        ArrayList<String> percentData = new ArrayList<String>();
        percentData.addAll((facade.getpercentValues(measEleId)));

        BigDecimal total = facade.getColumnGrandTotalValue(measEleId);
        String goalValue = facade.getGoalValue(measEleId).get(0);
        String goalPercent = facade.getPercent(measEleId).get(1);
        if (total.intValue() == 0) {
            total = new BigDecimal("1");
        }
        if (!goalPercent.equalsIgnoreCase("")) {
            BigDecimal goalPercentValue = total.add(total.multiply(new BigDecimal(goalPercent), MathContext.DECIMAL64).divide(new BigDecimal(100)));
            for (int i = 0; i < percentData.size(); i++) {
                goalSeekData.add(goalPercentValue.multiply(new BigDecimal(percentData.get(i)), MathContext.DECIMAL64).divide(new BigDecimal(100)));
            }
        } else if (!goalValue.equalsIgnoreCase("")) {
            for (int i = 0; i < percentData.size(); i++) {
                goalSeekData.add(new BigDecimal(goalValue.replace(",", "")).multiply(new BigDecimal(percentData.get(i)), MathContext.DECIMAL64).divide(new BigDecimal(100)));
            }
        } else {
            for (int i = 0; i < percentData.size(); i++) {
                goalSeekData.add(total.multiply(new BigDecimal(percentData.get(i)), MathContext.DECIMAL64).divide(new BigDecimal(100)));
            }

        }

        return goalSeekData;
    }

    public RunTimeMeasure calculateGoalSeekIndivTime(String measure) {

        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = this.getGoalSeekTimelist(measure);
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);

        return rtMeasure;
    }

    private ArrayList<BigDecimal> getGoalSeekTimelist(String measEleId) {
        ArrayList<BigDecimal> goalSeekData = new ArrayList<BigDecimal>();

        ArrayList<String> percentData = new ArrayList<String>();
        percentData.addAll((facade.getTimeMeasurValues(measEleId)));
        ArrayList<Integer> ViewByValues = new ArrayList<Integer>();
        ViewByValues = facade.getViewSequenceNumbers();
        ArrayList<String> newValues = new ArrayList<String>();
        newValues.addAll(percentData);

        for (int i = 0; i < percentData.size(); i++) {
            newValues.add(ViewByValues.get(i), percentData.get(i));
            goalSeekData.add(new BigDecimal(percentData.get(i).replace(",", "")));
        }
        return goalSeekData;
    }

    public RunTimeMeasure calculateGoalSeekTimePercent(String measure) {

        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = this.getGoalSeekIndPercent(measure);
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);

        return rtMeasure;
    }

    private ArrayList<BigDecimal> getGoalSeekIndPercent(String measEleId) {
        ArrayList<BigDecimal> goalSeekData = new ArrayList<BigDecimal>();
        ArrayList<String> viewBys = new ArrayList<String>();
        String viewByval = facade.getDisplayColumns().get(0);
        ArrayList<Integer> ViewByValues = new ArrayList<Integer>();
        ViewByValues = facade.getViewSequenceNumbers();
        ArrayList<String> newValues = new ArrayList<String>();

        ArrayList<String> percentData = new ArrayList<String>();
        percentData.addAll((facade.getTimePercentValues(measEleId)));
        newValues.addAll(percentData);

        for (int i = 0; i < percentData.size(); i++) {
            newValues.add(ViewByValues.get(i), percentData.get(i));
            goalSeekData.add(new BigDecimal(percentData.get(i).replace(",", "")));
        }
        return goalSeekData;
    }

    public RunTimeMeasure calculateUserGoalPercent(String measure) {
        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = this.userGoalPercent(measure);
        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);

        return rtMeasure;
    }

    private ArrayList<BigDecimal> userGoalPercent(String measEleId) {
        ArrayList<BigDecimal> goalSeekData = new ArrayList<BigDecimal>();

        ArrayList<String> percentData = new ArrayList<String>();
        percentData.addAll((facade.getpercentValues(measEleId)));

        for (int i = 0; i < percentData.size(); i++) {
            goalSeekData.add(new BigDecimal(percentData.get(i)));
        }


        return goalSeekData;
    }
//        Start of code by Nazneen in May 2012 for Rank On ST
    public RunTimeMeasure calculateRankForSubtotal(String measure, String dimension, ArrayList<String> rowViewBys, String measureType, String symbol, int precision) {
        ArrayList<BigDecimal> rankWise = new ArrayList<BigDecimal>(facade.getRowCount());
        ArrayList<String> columns = new ArrayList<String>();
        for (int i = 0; i < rowViewBys.size(); i++) {
            columns.add("A_" + rowViewBys.get(i));
        }
//        columns.add(dimension);
        columns.add(measure);
        Object[][] data = facade.retrieveDataBasedOnViewSequence(columns);
        ArrayList<Integer> viewSeq = facade.getViewSequence();
        for (int i = 0; i < data.length; i++) {
            if (data[i].length > 2) {
//                    String temp = "";
                StringBuilder temp = new StringBuilder(300);
                int j = 0;
                for (j = 0; j < rowViewBys.size() - 1; j++) {
//                        temp = temp +"~"+data[i][j];
                    temp.append("~").append(data[i][j]);
                }
//                    temp = temp.substring(1);
                data[i][0] = temp.toString().substring(1);
                data[i][1] = data[i][j + 1];
            }
        }
        DataSetFilter filter = new DataSetFilter();
        filter.setData(data, viewSeq);
        Set<Object> columnValues = filter.getUniqueValuesInColumn(1);
        SearchFilter searchFilter;
        //get first set
        for (Object value : columnValues) {
            searchFilter = new SearchFilter();
            searchFilter.add(dimension, "EQ", value);
            filter.setSearchFilter(searchFilter);
            viewSeq = filter.searchDataSet();
            rankWise.addAll(this.findRankForSet(viewSeq, measure, measureType, symbol, precision));
        }
//        RunTimeMeasure rtMeasure = new RunTimeMeasure(this.normalizeToNaturalOrder(percentWise, facade.getViewSequence()));
//        RunTimeMeasure rtMeasure = new RunTimeMeasure(rankWise);
        return new RunTimeMeasure(rankWise);
    }

    private ArrayList<BigDecimal> findRankForSet(ArrayList<Integer> viewSeq, String measure, String measureType, String symbol, int precision) {
        BigDecimal sum = BigDecimal.ZERO;
        //find subtotal
        for (int i = 0; i < viewSeq.size(); i++) {
            sum = sum.add(facade.getMeasureData(viewSeq.get(i), measure));
        }

        if (sum.compareTo(BigDecimal.valueOf(0.0)) == 0) {
            sum = BigDecimal.ONE;
        }
        if (sum == BigDecimal.ZERO) {
            sum = BigDecimal.ONE;
        }

        //find percentwise
        ArrayList<BigDecimal> percentWise = new ArrayList<BigDecimal>();
        BigDecimal data;
        RunTimeMeasure rtMeasure = null;
        for (int i = 0; i < viewSeq.size(); i++) {
            data = facade.getMeasureData(viewSeq.get(i), measure);
//            data = facade.getMeasureData(viewSeq.get(i), measure).divide(sum, MathContext.DECIMAL64);
            //data = data.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);

//            percentWise.add(data);
            String formatedData = NumberFormatter.getModifiedNumber(data, symbol, precision);
            formatedData = formatedData.replace(",", "").replace("K", "").replace("M", "").replace("L", "").replace("%", "").replace("Cr", "").replace("Abs", "").replace("A", "");
            BigDecimal newDataVal = new BigDecimal(formatedData.replace(",", ""));
            percentWise.add(newDataVal);
//            String newData = data.toString();
//            if(newData.contains(".")){
//                int dotVal = newData.indexOf(".");
//                newData = newData.substring(0, dotVal);
//                BigDecimal newDataVal = new BigDecimal(newData);
//                 percentWise.add(newDataVal);
//            }
//             else {
//                 percentWise.add(data);
//             }

        }
        percentWise = calculateRankForST(percentWise, measureType);
        return percentWise;
    }

    public ArrayList<BigDecimal> calculateRankForST(ArrayList<BigDecimal> data, String measureType) {
        ArrayList<BigDecimal> runTimeMeasData = null;
        runTimeMeasData = StatUtil.STAT_HELPER.RankOnST(data, measureType);
//        RunTimeMeasure rtMeasure = new RunTimeMeasure(runTimeMeasData);
        return runTimeMeasData;
    }
//        end of code by Nazneen in May 2012 for Rank On ST
}
