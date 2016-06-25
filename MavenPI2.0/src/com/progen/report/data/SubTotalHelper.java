/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author progen
 */
public class SubTotalHelper {

    DataFacade facade;
    HashMap<String, Integer> STcrosscolmap1 = new HashMap<String, Integer>();

    public SubTotalHelper(DataFacade facade) {
        this.facade = facade;


    }

    public void doSubTotalCalculations(SubTotalHolder subTotalHolder, String dimValue, String measId, BigDecimal value, BigDecimal rowcount) {
        BigDecimal currentSum;
        if (facade.isNetTotalRequired()) {
            //  if(measId.contains("_pwst")) {
//            if(RTMeasureElement.isRunTimeMeasure(measId)) {
//                currentSum=new BigDecimal("100");
//            }
            if (measId.contains("_pwst")) {
                currentSum = new BigDecimal("100");
            } else {
                currentSum = this.doSum(subTotalHolder, measId, value);
            }
            subTotalHolder.setSubTotal(dimValue, measId, currentSum);
            //Started by Nazneen on 21 April 2014 to calculate MIN AND MAX value of ST
            subTotalHolder.setSubTotalMinMaxVal(measId, value, dimValue);
            //end of code by Nazneen on 21 April 2014 to calculate MIN AND MAX value of ST
            if (facade.isCatAvgRequired()) {
                subTotalHolder.setDataForCategoryAvg(dimValue, measId, currentSum, rowcount);

            } else if (facade.isCatAvgRequired()) {
                currentSum = this.doSum(subTotalHolder, measId, value);
                subTotalHolder.setDataForCategoryAvg(dimValue, measId, currentSum, rowcount);
            }
            if (facade.isMedianRequired(measId)) {
                subTotalHolder.setDataForMedian(dimValue, measId, value);
            }
            if (facade.isMeanRequired(measId)) {
                subTotalHolder.setDataForMean(dimValue, measId, value);
            }
            if (facade.isStdDeviationRequired(measId)) {
                subTotalHolder.setDataForStdDev(dimValue, measId, value);
            }
            if (facade.isVarianceRequired(measId)) {
                subTotalHolder.setDataForVariance(dimValue, measId, value);
            }
            if (facade.isCategoryMaxRequired()) {
                subTotalHolder.setDataForCategoryMax(dimValue, measId, value);
            }
            if (facade.isCategoryMinRequired()) {
                subTotalHolder.setDataForCategoryMin(dimValue, measId, value);
            }

        } else if (facade.isCatAvgRequired()) {
            currentSum = this.doSum(subTotalHolder, measId, value);
            subTotalHolder.setDataForCategoryAvg(dimValue, measId, currentSum, rowcount);
        } else if (facade.isCustomTotalRequired()) {
            String mappedTo = facade.getmappedTo();
            if (mappedTo != null && mappedTo.equalsIgnoreCase("ST")) {
                currentSum = this.doSum(subTotalHolder, measId, value);
                subTotalHolder.setSubTotal(dimValue, measId, currentSum);
            } else if (mappedTo != null && mappedTo.equalsIgnoreCase("CATMAX")) {
                subTotalHolder.setDataForCategoryMax(dimValue, measId, value);
            } else if (mappedTo != null && mappedTo.equalsIgnoreCase("CATMIN")) {
                subTotalHolder.setDataForCategoryMin(dimValue, measId, value);
            } else if (mappedTo != null && mappedTo.equalsIgnoreCase("CATAVG")) {
                currentSum = this.doSum(subTotalHolder, measId, value);
                subTotalHolder.setDataForCategoryAvg(dimValue, measId, currentSum, rowcount);
            }
        } //added by Mayank for variance, median n all.
        else {
            if (facade.isMedianRequired(measId)) {
                subTotalHolder.setDataForMedian(dimValue, measId, value);
            }
            if (facade.isMeanRequired(measId)) {
                subTotalHolder.setDataForMean(dimValue, measId, value);
            }
            if (facade.isStdDeviationRequired(measId)) {
                subTotalHolder.setDataForStdDev(dimValue, measId, value);
            }
            if (facade.isVarianceRequired(measId)) {
                subTotalHolder.setDataForVariance(dimValue, measId, value);
            }
        }
        // end on 21-July-2015
        // 
        if (facade.isTopBottomWithOthersEnable()) {//for generting subtotalvalues if subtotal is unchecked case
            currentSum = this.doSum(subTotalHolder, measId, value);
            subTotalHolder.setSubTotal(dimValue, measId, currentSum);
        }
    }

    private BigDecimal doSum(SubTotalHolder subTotalHolder, String measId, BigDecimal value) {
        BigDecimal current = subTotalHolder.getSubTotalValue(measId);
        BigDecimal bd = new BigDecimal("0");
        int STzerocountct = 0;
        if (current == null) {
            current = new BigDecimal(0);
        }
        if (value.compareTo(bd) == 0) {
            STzerocountct = STzerocountct + 1;
        }
        if (STcrosscolmap1.get(measId) != null) {
            STzerocountct = STzerocountct + STcrosscolmap1.get(measId);
        }
        STcrosscolmap1.put(measId, STzerocountct);
        facade.container.setSTcrosscolmap(STcrosscolmap1);
        return current.add(value);

    }

    public void doZeroCount(SubTotalHolder subTotalHolder, String measId) {
        Integer zroCnt = subTotalHolder.getZeroRowCntMap(measId);
        zroCnt++;
        subTotalHolder.setZeroRowCntMap(measId, zroCnt);
    }

    public TableSubtotalRow UpdatePageTotal(TableSubtotalRow subTotalRow, ArrayList<TableSubtotalRow> filteredSubtotalRowsList, ArrayList<TableDataRow> Rowlist) {
        TableSubtotalRow subTotalRowTemp = new TableSubtotalRow();
        subTotalRowTemp = subTotalRow;
        HashMap<String, BigDecimal> SubTotalMap = new HashMap<String, BigDecimal>();
        BigDecimal SubTotalsSubmitted = new BigDecimal(facade.getNoOfSubTotalsSubmitted());
        HashMap<String, String> getExistingPageTotals = new HashMap<String, String>();
        for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
            String ElementId = facade.getColumnId(j);
            SubTotalMap.put(ElementId, new BigDecimal("0"));
            getExistingPageTotals.put(ElementId, subTotalRow.getRowData(j));
        }

        for (int i = 0; i < Rowlist.size(); i++) {
            TableDataRow removedSUbTotalRow = Rowlist.get(i);
            for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
                String CurrentElementId = facade.getColumnId(j);

                String RowValue = removedSUbTotalRow.getRowData(j);
                RowValue = RowValue.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
                RowValue = RowValue.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
                RowValue = RowValue.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
                //BigDecimal value=new BigDecimal(RowValue);
                BigDecimal value = new BigDecimal("0");
                if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N") && RowValue.trim() != null && !RowValue.trim().equalsIgnoreCase("") && !RowValue.trim().equalsIgnoreCase("null")) {
                    value = new BigDecimal(RowValue.trim());
                }
                BigDecimal CurrentValue = SubTotalMap.get(CurrentElementId).add(value);
                SubTotalMap.put(CurrentElementId, CurrentValue);
            }
        }
        // 
        ArrayList<Object> rowData = subTotalRow.getRowData();
        for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
            //
            String summarisedType = "SUMMARISED";
            summarisedType = facade.container.summarizedHashmap.get(facade.getColumnId(j));
            if (summarisedType != null) {
                if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                    summarisedType = "NORMAL_AVG";
                } else {
                    summarisedType = "SUMMARISED";
                }
            } else {
                summarisedType = "SUMMARISED";
            }
            String StringVal = rowData.get(j).toString();

            StringVal = StringVal.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
            StringVal = StringVal.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
            StringVal = StringVal.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
            BigDecimal ExistingValue = new BigDecimal("0");
            if (rowData.get(j).toString().isEmpty() || rowData.get(j).toString().equalsIgnoreCase("")) {
            } else {
                ExistingValue = new BigDecimal(StringVal.trim());
            }
            BigDecimal valueAfterFiltr = new BigDecimal("0");
            if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                if (Rowlist.size() != 0) {
                    BigDecimal sumval = new BigDecimal("0.00");
                    for (int u = 0; u < Rowlist.size(); u++) {
                        String RowValue1 = Rowlist.get(u).getRowData(j);
                        RowValue1 = RowValue1.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
                        RowValue1 = RowValue1.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
                        RowValue1 = RowValue1.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
                        BigDecimal value2 = new BigDecimal("0");
                        if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N") && RowValue1.trim() != null && !RowValue1.trim().equalsIgnoreCase("") && !RowValue1.trim().equalsIgnoreCase("null")) {
                            value2 = new BigDecimal(RowValue1.trim());
                        }
                        sumval = sumval.add(value2);
                    }

//           BigDecimal sumval=ExistingValue.multiply(new BigDecimal(TtlRows-1));
//           BigDecimal SubtractVal=SubTotalMap.get(facade.getColumnId(j));
//           BigDecimal DivideValtVal=new BigDecimal(facade.getNoOfSubTotalsSubmitted()-filteredSubtotalRowsList.size());
//       if((TtlRows-filteredSubtotalRowsList.size()-1)>0){
//           valueAfterFiltr=((ExistingValue.multiply(new BigDecimal(TtlRows-1))).subtract(SubTotalMap.get(facade.getColumnId(j)))).divide(new BigDecimal(TtlRows-filteredSubtotalRowsList.size()-1), RoundingMode.HALF_UP);
//               }else{
//                          valueAfterFiltr=((ExistingValue.multiply(new BigDecimal(TtlRows-1))).subtract(SubTotalMap.get(facade.getColumnId(j)))).divide(new BigDecimal(1), RoundingMode.HALF_UP);
//               }

                    valueAfterFiltr = sumval.divide(new BigDecimal(Rowlist.size()), RoundingMode.HALF_UP);
                } else {
                    valueAfterFiltr = ExistingValue;
                }
            } else {
                valueAfterFiltr = SubTotalMap.get(facade.getColumnId(j));
            }
            if (rowData.get(j).toString().isEmpty() || rowData.get(j).toString().equalsIgnoreCase("")) {
            } else {
                if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N")) {
                    rowData.remove(j);
                    String FormattedValue = facade.formatMeasureData(valueAfterFiltr, j);
                    rowData.add(j, FormattedValue);
                }
            }
        }

        subTotalRow.setRowData(rowData);
        return subTotalRow;
    }

    public TableSubtotalRow UpdatePageTotalRow(TableSubtotalRow subTotalRow, ArrayList<TableDataRow> filteredSubtotalRowsList, int TtlRows, ArrayList<TableDataRow> Rowlist) {
        TableSubtotalRow subTotalRowTemp = new TableSubtotalRow();
        subTotalRowTemp = subTotalRow;
        HashMap<String, BigDecimal> SubTotalMap = new HashMap<String, BigDecimal>();
        BigDecimal SubTotalsSubmitted = new BigDecimal(facade.getNoOfSubTotalsSubmitted());
        HashMap<String, String> getExistingPageTotals = new HashMap<String, String>();
        for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
            String ElementId = facade.getColumnId(j);
            SubTotalMap.put(ElementId, new BigDecimal("0"));
            getExistingPageTotals.put(ElementId, subTotalRow.getRowData(j));
        }

        for (int i = 0; i < Rowlist.size(); i++) {
            TableDataRow removedSUbTotalRow = Rowlist.get(i);
            for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
                String CurrentElementId = facade.getColumnId(j);
                //
                String RowValue = removedSUbTotalRow.getRowData(j);
                RowValue = RowValue.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
                RowValue = RowValue.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
                RowValue = RowValue.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
                //BigDecimal value=new BigDecimal(RowValue);
                BigDecimal value = new BigDecimal("0");
                if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N") && RowValue.trim() != null && !RowValue.trim().equalsIgnoreCase("") && !RowValue.trim().equalsIgnoreCase("null")) {
                    value = new BigDecimal(RowValue.trim());
                }
                BigDecimal CurrentValue = SubTotalMap.get(CurrentElementId).add(value);
                SubTotalMap.put(CurrentElementId, CurrentValue);
            }
        }
        // 
        ArrayList<Object> rowData = subTotalRow.getRowData();
        for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
            //
            String summarisedType = "SUMMARISED";
            summarisedType = facade.container.summarizedHashmap.get(facade.getColumnId(j));
            if (summarisedType != null) {
                if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                    summarisedType = "NORMAL_AVG";
                } else {
                    summarisedType = "SUMMARISED";
                }
            } else {
                summarisedType = "SUMMARISED";
            }
            String StringVal = rowData.get(j).toString();
            //
            StringVal = StringVal.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
            StringVal = StringVal.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
            StringVal = StringVal.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
            BigDecimal ExistingValue = new BigDecimal("0");
            if (rowData.get(j).toString().isEmpty() || rowData.get(j).toString().equalsIgnoreCase("")) {
            } else {
                ExistingValue = new BigDecimal(StringVal.trim());
            }
            BigDecimal valueAfterFiltr = new BigDecimal("0");
            if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                if (filteredSubtotalRowsList.size() != 0) {
                    BigDecimal sumval = new BigDecimal("0.00");
                    for (int u = 0; u < Rowlist.size(); u++) {
                        String RowValue1 = Rowlist.get(u).getRowData(j);
                        RowValue1 = RowValue1.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
                        RowValue1 = RowValue1.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
                        RowValue1 = RowValue1.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
                        BigDecimal value2 = new BigDecimal("0");
                        if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N") && RowValue1.trim() != null && !RowValue1.trim().equalsIgnoreCase("") && !RowValue1.trim().equalsIgnoreCase("null")) {
                            value2 = new BigDecimal(RowValue1.trim());
                        }
                        sumval = sumval.add(value2);

                        // sumval=sumval.add(new BigDecimal(RowValue1));
                    }

//           BigDecimal sumval=ExistingValue.multiply(new BigDecimal(TtlRows-1));
//           BigDecimal SubtractVal=SubTotalMap.get(facade.getColumnId(j));
//           BigDecimal DivideValtVal=new BigDecimal(facade.getNoOfSubTotalsSubmitted()-filteredSubtotalRowsList.size());
//       if((TtlRows-filteredSubtotalRowsList.size()-1)>0){
//           valueAfterFiltr=((ExistingValue.multiply(new BigDecimal(TtlRows-1))).subtract(SubTotalMap.get(facade.getColumnId(j)))).divide(new BigDecimal(TtlRows-filteredSubtotalRowsList.size()-1), RoundingMode.HALF_UP);
//               }else{
//                          valueAfterFiltr=((ExistingValue.multiply(new BigDecimal(TtlRows-1))).subtract(SubTotalMap.get(facade.getColumnId(j)))).divide(new BigDecimal(1), RoundingMode.HALF_UP);
//               }

                    valueAfterFiltr = sumval.divide(new BigDecimal(Rowlist.size()), RoundingMode.HALF_UP);
                } else {
                    valueAfterFiltr = ExistingValue;
                }
            } else {
                valueAfterFiltr = SubTotalMap.get(facade.getColumnId(j));
            }
            if (rowData.get(j).toString().isEmpty() || rowData.get(j).toString().equalsIgnoreCase("")) {
            } else {
                if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N")) {
                    rowData.remove(j);
                    String FormattedValue = facade.formatMeasureData(valueAfterFiltr, j);
                    rowData.add(j, FormattedValue);
                }
            }
        }

        subTotalRow.setRowData(rowData);
        return subTotalRow;
    }

    public TableSubtotalRow UpdateGrandTotalRow(TableSubtotalRow subTotalRow, ArrayList<TableDataRow> filteredSubtotalRowsList, ArrayList<TableDataRow> Rowlist) {
        TableSubtotalRow subTotalRowTemp = new TableSubtotalRow();
        subTotalRowTemp = subTotalRow;
        HashMap<String, BigDecimal> SubTotalMap = new HashMap<String, BigDecimal>();
        BigDecimal SubTotalsSubmitted = new BigDecimal(facade.getNoOfSubTotalsSubmitted());
        HashMap<String, String> getExistingPageTotals = new HashMap<String, String>();
        for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
            String ElementId = facade.getColumnId(j);
            SubTotalMap.put(ElementId, new BigDecimal("0"));
            getExistingPageTotals.put(ElementId, subTotalRow.getRowData(j));
        }

        for (int i = 0; i < Rowlist.size(); i++) {
            TableDataRow removedSUbTotalRow = Rowlist.get(i);
            for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
                String CurrentElementId = facade.getColumnId(j);
                // 
                String RowValue = removedSUbTotalRow.getRowData(j);
                RowValue = RowValue.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
                RowValue = RowValue.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
                RowValue = RowValue.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
                //BigDecimal value=new BigDecimal(RowValue);
                BigDecimal value = new BigDecimal("0");
                if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N") && RowValue.trim() != null && !RowValue.trim().equalsIgnoreCase("") && !RowValue.trim().equalsIgnoreCase("null")) {
                    value = new BigDecimal(RowValue.trim());
                }
                BigDecimal CurrentValue = SubTotalMap.get(CurrentElementId).add(value);
                SubTotalMap.put(CurrentElementId, CurrentValue);
            }
        }
        // 
        ArrayList<Object> rowData = subTotalRow.getRowData();
        for (int j = facade.getViewByCount(); j < facade.getColumnCount(); j++) {
            //
            String summarisedType = "SUMMARISED";
            summarisedType = facade.container.summarizedHashmap.get(facade.getColumnId(j));
            if (summarisedType != null) {
                if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                    summarisedType = "NORMAL_AVG";
                } else {
                    summarisedType = "SUMMARISED";
                }
            } else {
                summarisedType = "SUMMARISED";
            }
            String StringVal = rowData.get(j).toString();
            // 
            StringVal = StringVal.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
            StringVal = StringVal.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
            StringVal = StringVal.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
            BigDecimal ExistingValue = new BigDecimal("0");
            if (rowData.get(j).toString().isEmpty() || rowData.get(j).toString().equalsIgnoreCase("")) {
            } else {
                ExistingValue = new BigDecimal(StringVal.trim());
            }
            BigDecimal valueAfterFiltr = new BigDecimal("0");
            if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                if (Rowlist.size() != 0) {
                    BigDecimal sumval = new BigDecimal("0.00");
                    for (int u = 0; u < Rowlist.size(); u++) {
                        String RowValue1 = Rowlist.get(u).getRowData(j);
                        RowValue1 = RowValue1.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
                        RowValue1 = RowValue1.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
                        RowValue1 = RowValue1.replace("C", "").replace("Ã¢â€šÂ¬", "").replace("Ã‚Â¥", "").replace("â‚¬", "").replace("Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
                        BigDecimal value2 = new BigDecimal("0");
                        if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N") && RowValue1.trim() != null && !RowValue1.trim().equalsIgnoreCase("") && !RowValue1.trim().equalsIgnoreCase("null")) {
                            value2 = new BigDecimal(RowValue1.trim());
                        }
                        sumval = sumval.add(value2);
                    }

//           BigDecimal sumval=ExistingValue.multiply(new BigDecimal(TtlRows-1));
//           BigDecimal SubtractVal=SubTotalMap.get(facade.getColumnId(j));
//           BigDecimal DivideValtVal=new BigDecimal(facade.getNoOfSubTotalsSubmitted()-filteredSubtotalRowsList.size());
//       if((TtlRows-filteredSubtotalRowsList.size()-1)>0){
//           valueAfterFiltr=((ExistingValue.multiply(new BigDecimal(TtlRows-1))).subtract(SubTotalMap.get(facade.getColumnId(j)))).divide(new BigDecimal(TtlRows-filteredSubtotalRowsList.size()-1), RoundingMode.HALF_UP);
//               }else{
//                          valueAfterFiltr=((ExistingValue.multiply(new BigDecimal(TtlRows-1))).subtract(SubTotalMap.get(facade.getColumnId(j)))).divide(new BigDecimal(1), RoundingMode.HALF_UP);
//               }
                    // 
                    valueAfterFiltr = sumval.divide(new BigDecimal(Rowlist.size()), RoundingMode.HALF_UP);
                } else {
                    valueAfterFiltr = ExistingValue;
                }
            } else {
                valueAfterFiltr = SubTotalMap.get(facade.getColumnId(j));
            }
            if (rowData.get(j).toString().isEmpty() || rowData.get(j).toString().equalsIgnoreCase("")) {
            } else {
                if (facade.container.getDataTypes().get(j) != null && facade.container.getDataTypes().get(j).toString().equalsIgnoreCase("N")) {
                    rowData.remove(j);
                    String FormattedValue = facade.formatMeasureData(valueAfterFiltr, j);
                    rowData.add(j, FormattedValue);
                }
            }
        }

        subTotalRow.setRowData(rowData);
        return subTotalRow;
    }
}
