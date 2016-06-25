/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.util.stat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Median;
import org.apache.commons.math.stat.ranking.NaturalRanking;

/**
 *
 * @author arun
 */
public enum StatUtil {

    STAT_HELPER;

    /**
     * Computes Standard Deviation for given bunch of data
     *
     * @param data
     * @return standard deviation in double format
     */
    public double StandardDeviation(ArrayList<BigDecimal> data) {
        double[] statData = this.convertBigDToDouble(data);

        StandardDeviation sd = new StandardDeviation();
        double stDeviation = sd.evaluate(statData);
        return stDeviation;
    }

    public double Correlation(ArrayList<BigDecimal> data1, ArrayList<BigDecimal> data2) {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        if (data1.size() != data2.size()) {
            if (data1.size() > data2.size()) {
                for (int i = data2.size(); i < data1.size(); i++) {
                    data2.add(BigDecimal.ZERO);
                }
            } else {
                for (int i = data1.size(); i < data2.size(); i++) {
                    data1.add(BigDecimal.ZERO);
                }
            }
        }
        double[] statData1 = this.convertBigDToDouble(data1);
        double[] statData2 = this.convertBigDToDouble(data2);
        double corrValue = 0;
        corrValue = pc.correlation(statData1, statData2);
        return corrValue;
    }

    /**
     * Computes Median for given bunch of data
     *
     * @param data
     * @return median in double format
     */
    public double Median(ArrayList<BigDecimal> data) {
        double[] statData = this.convertBigDToDouble(data);

        Median md = new Median();
        double median = md.evaluate(statData);
        return median;
    }

    /**
     * Computes Mean for given bunch of data
     *
     * @param data
     * @return mean in double format
     */
    public double Mean(ArrayList<BigDecimal> data) {
        double[] statData = this.convertBigDToDouble(data);

        Mean me = new Mean();
        double mean = me.evaluate(statData);
        return mean;
    }

    public double Variance(ArrayList<BigDecimal> data) {
        double[] statData = this.convertBigDToDouble(data);

        Variance var = new Variance();
        double variance = var.evaluate(statData);
        return variance;
    }

    private double[] convertBigDToDouble(ArrayList<BigDecimal> data) {
        double[] statData = new double[data.size()];
        int i = 0;
        for (BigDecimal aData : data) {
            statData[i++] = aData.doubleValue();
        }
        return statData;
    }

    private ArrayList<Integer> convertDoubleToIntLst(double[] data) {
        ArrayList<Integer> dataLst = new ArrayList<Integer>(data.length);

        for (int i = 0; i < data.length; i++) {
            dataLst.add((int) data[i]);
        }

        return dataLst;
    }

    public ArrayList<BigDecimal> Rank(ArrayList<BigDecimal> data) {
        double[] statData = this.convertBigDToDouble(data);

        NaturalRanking natRanking = new NaturalRanking();
        double[] rankData = natRanking.rank(statData);

        ArrayList<Integer> naturalRankList = this.convertDoubleToIntLst(rankData);

        int maxRank = 0;
        for (int rank : naturalRankList) {
            if (rank > maxRank) {
                maxRank = rank;
            }
        }
        maxRank++;

        ArrayList<BigDecimal> rankList = new ArrayList<BigDecimal>();
        for (int rank : naturalRankList) {
            rankList.add(new BigDecimal(maxRank - rank));
        }
        return rankList;

    }

    public ArrayList<BigDecimal> RunningTotal(ArrayList<BigDecimal> data) {
        ArrayList<BigDecimal> runningTotals = new ArrayList<BigDecimal>(data.size());
        BigDecimal sum = new BigDecimal(0);

        for (BigDecimal measValue : data) {
            sum = sum.add(measValue);
            runningTotals.add(sum);
        }
        return runningTotals;
    }
//        Start of code by Nazneen in May 2012 for Rank On ST

    public ArrayList<BigDecimal> RankOnST(ArrayList<BigDecimal> data, String measureType) {
        double[] statData = this.convertBigDToDouble(data);

        NaturalRanking natRanking = new NaturalRanking();
        double[] rankData = natRanking.rank(statData);

        Set<BigDecimal> ValueSet = new HashSet<BigDecimal>(data);
        double[] statData1 = this.convertBigDToDouble(new ArrayList<BigDecimal>(ValueSet));
        double[] rankData1 = natRanking.rank(statData1);
        HashMap map = new HashMap();
        for (int i = 0; i < statData1.length; i++) {
            map.put(statData1[i], rankData1[i]);
        }
        for (int i = 0; i < statData.length; i++) {
            if (map.containsKey(statData[i])) {
                String val = map.get(statData[i]).toString();
                rankData[i] = Double.parseDouble(val);
            }
        }
//       int rnk = 1;
//       for(int i=1;i<statData.length;i++){
//           if(rankData[i]==i){
//               rankData[i] = rnk;
//           }
//       }

        //start of code by Nazneen for same values
        boolean flag = true;
        for (int i = 0; i < statData.length; i++) {
            if (statData[0] != statData[i]) {
                flag = false;
                break;
            }
        }
        if (flag) {
            for (int i = 0; i < statData.length; i++) {
                rankData[i] = 1;
            }
        }
        //end of code by Nazneen for same values

        ArrayList<Integer> naturalRankList = this.convertDoubleToIntLst(rankData);

        int maxRank = 0;
        for (int rank : naturalRankList) {
            if (rank > maxRank) {
                maxRank = rank;
            }
        }
        maxRank++;

        ArrayList<BigDecimal> rankList = new ArrayList<BigDecimal>();
        for (int rank : naturalRankList) {
            rankList.add(new BigDecimal(maxRank - rank));
        }
        if (measureType.contains("non") || measureType.contains("Non")) {
            rankList.clear();
            for (int rank : naturalRankList) {
                rankList.add(new BigDecimal(rank));
            }
        } else {
//            rankList.clear();
//            for ( int rank : naturalRankList )
//            {
//                rankList.add(new BigDecimal(rank));
//            }
        }
        return rankList;

    }
//        Start of code by Nazneen in May 2012 for Rank On ST

    //added by sruthi otherval for running total on 14 Nov 14 
    public BigDecimal RunningTopTotal(ArrayList<BigDecimal> runningdata, BigDecimal normalother) {
        ArrayList<BigDecimal> runningTopTotal = new ArrayList<BigDecimal>();
        BigDecimal runningsum = new BigDecimal(0);
        for (int i = 0; i <= runningdata.size(); i++) {
            if (i == runningdata.size()) {
                String runval = runningdata.get(i - 1).toString();
                BigDecimal runningval = new BigDecimal(runval);
                runningsum = normalother.add(runningval);
            }

        }
        return runningsum;
    }
//ended by sruthi
}
