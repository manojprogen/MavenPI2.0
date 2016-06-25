/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.google.common.base.Predicate;
import com.progen.report.util.stat.StatUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author progen
 */
public class SubTotalHolder implements Comparable {

    HashMap<String, BigDecimal> subTotal;
    HashMap<String, ArrayList<BigDecimal>> median;
    HashMap<String, ArrayList<BigDecimal>> mean;
    HashMap<String, ArrayList<BigDecimal>> stdDev;
    HashMap<String, ArrayList<BigDecimal>> variance;
    HashMap<String, BigDecimal> categoryMax;
    HashMap<String, BigDecimal> categoryMin;
    HashMap<String, BigDecimal> categoryAvg;
    String dimValue;
    int viewColNum;
    int rowCount;
    String firstCellId;
    int zeroRowCount;
    HashMap<String, Integer> zeroRowCountMap = new HashMap<String, Integer>();
    HashMap<String, BigDecimal> minSTValMap = new HashMap<String, BigDecimal>();
    HashMap<String, BigDecimal> maxSTValMap = new HashMap<String, BigDecimal>();

    public SubTotalHolder() {
        subTotal = new HashMap<String, BigDecimal>();
        median = new HashMap<String, ArrayList<BigDecimal>>();
        mean = new HashMap<String, ArrayList<BigDecimal>>();
        stdDev = new HashMap<String, ArrayList<BigDecimal>>();
        categoryMax = new HashMap<String, BigDecimal>();
        categoryMin = new HashMap<String, BigDecimal>();
        variance = new HashMap<String, ArrayList<BigDecimal>>();
        rowCount = 0;
        categoryAvg = new HashMap<String, BigDecimal>();
    }

    public void setSubTotal(String dimValue, String measId, BigDecimal subTotal) {
        this.subTotal.put(measId, subTotal);
        this.dimValue = dimValue;
    }

    public void setDataForMedian(String dimValue, String measId, BigDecimal data) {
        ArrayList<BigDecimal> medianData = this.median.get(measId);
        if (medianData == null) {
            medianData = new ArrayList<BigDecimal>();
        }
        medianData.add(data);
        this.median.put(measId, medianData);
        this.dimValue = dimValue;
    }

    public void setDataForMean(String dimValue, String measId, BigDecimal data) {
        ArrayList<BigDecimal> meanData = this.mean.get(measId);
        if (meanData == null) {
            meanData = new ArrayList<BigDecimal>();
        }
        meanData.add(data);
        this.mean.put(measId, meanData);
        this.dimValue = dimValue;
    }

    public void setDataForStdDev(String dimValue, String measId, BigDecimal data) {
        ArrayList<BigDecimal> stdDevData = this.stdDev.get(measId);
        if (stdDevData == null) {
            stdDevData = new ArrayList<BigDecimal>();
        }
        stdDevData.add(data);
        this.stdDev.put(measId, stdDevData);
        this.dimValue = dimValue;
    }

    public void setDataForVariance(String dimValue, String measId, BigDecimal data) {
        ArrayList<BigDecimal> varianceData = this.variance.get(measId);
        if (varianceData == null) {
            varianceData = new ArrayList<BigDecimal>();
        }
        varianceData.add(data);
        this.variance.put(measId, varianceData);
        this.dimValue = dimValue;
    }

    public BigDecimal getSubTotalValue(String measId) {
        return this.subTotal.get(measId);
    }

    public BigDecimal getMeanValue(String measId) {
        ArrayList<BigDecimal> meanData = this.mean.get(measId);
        double meanValue = StatUtil.STAT_HELPER.Mean(meanData);
        meanData.clear();
        BigDecimal meanValueBd = new BigDecimal(meanValue);
        return meanValueBd;
    }

    public BigDecimal getMedianValue(String measId) {
        ArrayList<BigDecimal> medianData = this.median.get(measId);
        double medianValue = StatUtil.STAT_HELPER.Median(medianData);
        medianData.clear();
        BigDecimal medianValueBd = new BigDecimal(medianValue);
        return medianValueBd;
    }

    public BigDecimal getStdDevValue(String measId) {
        ArrayList<BigDecimal> stdDevData = this.stdDev.get(measId);
        double stdDevValue = StatUtil.STAT_HELPER.StandardDeviation(stdDevData);
        stdDevData.clear();
        BigDecimal stdDevValueBd = new BigDecimal(stdDevValue);
        return stdDevValueBd;
    }

    public BigDecimal getVarianceValue(String measId) {
        ArrayList<BigDecimal> varianceData = this.variance.get(measId);
        double varianceValue = StatUtil.STAT_HELPER.Variance(varianceData);
        varianceData.clear();
        BigDecimal varValueBd = new BigDecimal(varianceValue);
        return varValueBd;
    }

    public void setDataForCategoryMax(String dimValue, String measId, BigDecimal data) {
//        BigDecimal stdDevData = this.categoryMax.get(measId);
        if (this.categoryMax.isEmpty()) {
            categoryMax.put(measId, data);
            this.dimValue = dimValue;
        } else {
            if (categoryMax.get(measId) == null) {
                categoryMax.put(measId, data);
            } else if (this.categoryMax.get(measId).compareTo(data) < 0) {
                categoryMax.put(measId, data);
            } else {
                categoryMax.put(measId, this.categoryMax.get(measId));
            }
            this.dimValue = dimValue;
        }
    }

    public BigDecimal getDataForCategoryMax(String measId) {
        BigDecimal catMax = this.categoryMax.get(measId);
//        categoryMax.clear();
        return catMax;
    }

    public void setDataForCategoryMin(String dimValue, String measId, BigDecimal data) {
        if (this.categoryMin.isEmpty()) {
            categoryMin.put(measId, data);
            this.dimValue = dimValue;
        } else {
            if (categoryMin.get(measId) == null) {
                categoryMin.put(measId, data);
            } else if (this.categoryMin.get(measId).compareTo(data) > 0) {
                categoryMin.put(measId, data);
            } else {
                categoryMin.put(measId, this.categoryMin.get(measId));
            }
            this.dimValue = dimValue;
        }
    }

    public BigDecimal getDataForCategoryMin(String measId) {
        BigDecimal catMin = this.categoryMin.get(measId);
//      categoryMin.clear();
        return catMin;
    }

    public void setFirstCellId(String cellId) {
        this.firstCellId = cellId;
    }

    public String getFirstCellId() {
        return this.firstCellId;
    }

    public void incrementRowCount() {
        this.rowCount++;
    }

    public void incrementRowCountForSubTotal() {

        if (!this.subTotal.isEmpty()) {
            this.rowCount++;
        }
        if (!this.median.isEmpty()) {
            this.rowCount++;
        }
        if (!this.mean.isEmpty()) {
            this.rowCount++;
        }
        if (!this.stdDev.isEmpty()) {
            this.rowCount++;
        }
        if (!this.variance.isEmpty()) {
            this.rowCount++;
        }
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public boolean compareTo(String dimValue) {
        if (this.dimValue == null) {
            return true;
        } else if (this.dimValue.equals(dimValue)) {
            return true;
        } else {
            return false;
        }
    }

    public void setViewColumnNumber(int viewColNum) {
        this.viewColNum = viewColNum;
    }

    public int getViewColumnNumber() {
        return this.viewColNum;
    }

    public String getDimensionValue() {
        return this.dimValue;
    }

    @Override
    public String toString() {
        return "SubTotalHolder " + this.dimValue + " " + this.viewColNum + " " + this.firstCellId;
    }

    public static Predicate getFirstCellIdPredicate(final String cellId) {
        Predicate<SubTotalHolder> stHolder = new Predicate<SubTotalHolder>() {

            public boolean apply(SubTotalHolder input) {
                if (input.getFirstCellId().equals(cellId)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return stHolder;
    }

    public static Predicate<SubTotalHolder> isSubtotalFoundForFirstRowView() {
        Predicate<SubTotalHolder> stHolder = new Predicate<SubTotalHolder>() {

            @Override
            public boolean apply(SubTotalHolder input) {
                if (input.getViewColumnNumber() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return stHolder;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof SubTotalHolder) {
            if (this.viewColNum < ((SubTotalHolder) o).getViewColumnNumber()) {
                return 1;
            } else if (this.viewColNum == ((SubTotalHolder) o).getViewColumnNumber()) {
                return 0;
            } else {
                return -1;
            }

        }
        return -1;
    }

    public BigDecimal getDataForCategoryAvg(String measId) {
        BigDecimal catAvg = this.categoryAvg.get(measId);
        return catAvg;
    }

    public void setDataForCategoryAvg(String dimValue, String measId, BigDecimal subtotal, BigDecimal rowcount) {
        BigDecimal catAvgdata = this.categoryAvg.get(measId);
        BigDecimal catAvg;
//          if(catAvgdata==null)
        catAvg = new BigDecimal(0);
//          else
//            catAvg = catAvgdata;
        catAvg = subtotal.divide(new BigDecimal(this.rowCount + 1), 2, BigDecimal.ROUND_HALF_UP);
        this.categoryAvg.put(measId, catAvg);
        this.dimValue = dimValue;
    }

    public void setZeroRowCntMap(String measId, Integer zeroRowCount) {
        this.zeroRowCountMap.put(measId, zeroRowCount);
    }

    public Integer getZeroRowCntMap(String measId) {
        if (this.zeroRowCountMap != null && this.zeroRowCountMap.get(measId) != null) {
            return this.zeroRowCountMap.get(measId);
        } else {
            return 0;
        }
    }
    //added by Nazneen on 21 April 2014 to calculate ST for MIN AND MAX type Measures

    public void setSubTotalMinMaxVal(String measId, BigDecimal value, String dimValue) {
        BigDecimal bgVal = BigDecimal.ZERO;
        String key = measId + "~" + dimValue;
        //calculate Min Value
//       if(dimValue!=null && !dimValue.equalsIgnoreCase("null") && !dimValue.equalsIgnoreCase("")){
        if (this.minSTValMap != null && this.minSTValMap.get(key) != null) {
            bgVal = this.minSTValMap.get(key);
            int res = 0;
            res = bgVal.compareTo(value);
            if (res == 1) // First Value is Greater
            {
                bgVal = value;
            }
            this.minSTValMap.put(key, bgVal);
        } else {
            this.minSTValMap.put(key, value);
        }
        //Calculate Max Value
        if (this.maxSTValMap != null && this.maxSTValMap.get(key) != null) {
            bgVal = this.maxSTValMap.get(key);
            int res = 0;
            res = bgVal.compareTo(value);
            if (res == -1) // First Value is Greater
            {
                bgVal = value;
            }
            this.maxSTValMap.put(key, bgVal);
        } else {
            this.maxSTValMap.put(key, value);
        }
//     }

//     if(this.minSTVal==null)
//            this.minSTVal = value;
//         if(this.maxSTVal==null)
//              this.maxSTVal = value;
//        int res = 0;
//        //calculate min value
//        res = this.minSTVal.compareTo(value);
//        if( res == 1 ) // First Value is Greater
//            this.minSTVal = value;
//        //calculate max value
//         res = this.maxSTVal.compareTo(value);
//         if( res == -1 ) //Second value is Greater
//            this.maxSTVal = value;
//        this.minSTValMap.put(measId+"~"+dimValue,this.minSTVal);
//        this.maxSTValMap.put(measId+"~"+dimValue,this.maxSTVal);
    }

    public BigDecimal getSubTotalMinVal(String measId, String dimValue) {
        String key = measId + "~" + dimValue;
        if (this.minSTValMap != null && this.minSTValMap.get(key) != null) {
            return this.minSTValMap.get(key);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getSubTotalMaxVal(String measId, String dimValue) {
        String key = measId + "~" + dimValue;
        if (this.maxSTValMap != null && this.maxSTValMap.get(key) != null) {
            return this.maxSTValMap.get(key);
        } else {
            return BigDecimal.ZERO;
        }
    }
    //ended by Nazneen on 21 April 2014 to calculate ST for MIN AND MAX type Measures
}
