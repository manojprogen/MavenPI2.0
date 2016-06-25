/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.google.common.collect.ArrayListMultimap;
import com.progen.db.POIDataSet;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTDimensionElement;
import com.progen.query.RTMeasureElement;
import com.progen.report.ColorHelper;
import com.progen.report.PbReportCollection;
import com.progen.report.colorgroup.ColorCode;
import com.progen.report.colorgroup.ColorCodeRule;
import com.progen.report.colorgroup.ColorGroup;
import com.progen.report.display.util.NumberFormatter;
import java.math.BigDecimal;
import java.util.Collection;
import prg.db.Container;
import prg.db.PbReturnObject;
import com.progen.report.excel.ExcelCellFormat;
import com.progen.report.excel.ExcelCellFormatGroup;
import com.progen.report.excel.ExcelColumnGroup;
import com.progen.report.excel.RunTimeExcelColumn;
import com.progen.report.query.PbReportQuery;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.users.PrivilegeManager;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.ContainerConstants;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class DataFacade implements Serializable {

    public static Logger logger = Logger.getLogger(DataFacade.class);
    public Container container;
    private ProgenDataSet qryData;
    private PbReturnObject displayedSet = null;
    private PbReturnObject displayedSetwithGT = null;
    private String ctxPath;
    private String userId;
    private PbReportQuery repQry = new PbReportQuery();
    public HashMap<String, Container> refreshconatinerMap = new HashMap<String, Container>();
    private static final long serialVersionUID = 75264711446228L;
    public boolean isMsrbasedBusTemplate;
    private int actualrow = 0;
    public HashMap<String, ArrayList<BigDecimal>> rowdataMap = new HashMap();
    private HashMap<String, HashMap<Double, String>> colorcodeMap = new HashMap();
    private int noOfSubTotalsSubmitted = 0;
    private ArrayList<String> Measures = new ArrayList<String>();
    private int rowForSTRank = 0;
    private int rowForRTTimeDimension = 0;

    public DataFacade(Container container) {

        this.container = container;

        if (container.getSortRetObj() != null) {
            this.qryData = container.getSortRetObj();
        } else {
            this.qryData = container.getRetObj();
        }

    }

    public void setCtxPath(String ctxPath) {
        this.ctxPath = ctxPath;
    }

    public final int getViewByCount() {
        return (container.getViewByCount());
    }

    public final HashMap getparameterHash() {
        return (container.getparameterHash());
    }

    public final int getColumnCount() {
        return container.getDisplayColumns().size();
    }

    public int getRowCount() {
        return qryData.getViewSequence().size();//getRowCount();
    }

    public String getGrandTotalDisplayPosition() {
        if (container.isReportCrosstab()) {
            return container.getCrosstabGrandTotalDisplayPosition();
        } else {
            return "";
        }
    }

    public String getSortImageStyle(String columnName) {
        ArrayList<String> sortCols = null;
        sortCols = container.getSortColumns();
        char[] sortTypes;
        sortTypes = container.getSortTypes();
        String styleStr = null;
        String ascStyle = null;
        String desStyle = null;
        String dispStyle = null;
        int ind = 0;
        styleStr = "<img  src='" + ctxPath + "/TableDisplay/Images/bg.gif'>";
        ascStyle = "<img  src='" + ctxPath + "/TableDisplay/Images/desc.gif'>";
        desStyle = "<img  src='" + ctxPath + "/TableDisplay/Images/asc.gif'>";
        if (sortCols != null && sortCols.contains(columnName)) {
            ind = sortCols.indexOf(columnName);
            //srtType = String.valueOf(sortTypes.get(ind));
            if ('0' == sortTypes[ind]) //"0".equals(srtType)) {
            {
                dispStyle = ascStyle;
            } else if ('1' == sortTypes[ind]) //"1".equals(srtType)) {
            {
                dispStyle = desStyle;
            }

        } else {
            dispStyle = styleStr;
        }

        return dispStyle;
    }

    public String getColor(int columnNo, String columnName, BigDecimal subtotal) {
        HashMap ColorCodeMap = null;
        String bgColor = "";
        boolean isColorPresent = false;
        ColorGroup colorGroup = null;
        ColorCode colorCode = null;
        colorGroup = container.getColorGroup();
        PbReportViewerBD repBD = new PbReportViewerBD();
        String columnNameR = columnName;
//        
        boolean rowflag = false;
        rowflag = colorGroup.isRowWiseColor();
        BigDecimal maxVal = BigDecimal.ZERO;
        BigDecimal minVal = BigDecimal.ZERO;
        BigDecimal diff = BigDecimal.ZERO;
        BigDecimal avgVal = BigDecimal.ZERO;
        BigDecimal crosstab = BigDecimal.ZERO;

        List<BigDecimal> minAvgMax = container.getReportCollect().getMinmaxavgValues();
        if (!minAvgMax.isEmpty() && container.getDisplayColumns().get(0).equals(container.getReportCollect().getElemntId())) {
            maxVal = maxVal.add(minAvgMax.get(0));
            minVal = minVal.add(minAvgMax.get(1));
            diff = diff.add(maxVal.subtract(minVal));
        } else if (this.getColumnMaximumValue(columnName) == null || this.getColumnMinimumValue(columnName) == null) {
            maxVal = maxVal.add(BigDecimal.ZERO);
            minVal = minVal.add(BigDecimal.ZERO);
            diff = diff.add(maxVal.subtract(minVal));
        } else {

            maxVal = maxVal.add(this.getColumnMaximumValue(columnName));
            minVal = minVal.add(this.getColumnMinimumValue(columnName));
            diff = diff.add(maxVal.subtract(minVal));
            if (this.container.getViewByCount() >= 1 && this.getAggregationType(columnName) != null && !this.getAggregationType(columnName).toString().equalsIgnoreCase("null") && this.getAggregationType(columnName).toString().equalsIgnoreCase("avg")) {
                if (subtotal != null) {
                    avgVal = subtotal;
                } else {
                    avgVal = this.getColumnAverageValue(columnName);
                }
            } else {
                avgVal = this.getColumnAverageValue(columnName);
            }
            if (container.isReportCrosstab()) {
                String actualMsrs = ((PbReturnObject) container.getRetObj()).crosstabMeasureId.get(columnNameR);
                int length = getColumnCount() - (1) - ((this.Measures.size() - 1) - this.Measures.indexOf(actualMsrs));
                int Actrow = this.getActualrow();
                String CtAvgdata = this.getFormattedMeasureData(Actrow, length);
                if (CtAvgdata != null && !CtAvgdata.isEmpty() && !CtAvgdata.equalsIgnoreCase("null")) {
                    crosstab = new BigDecimal(CtAvgdata.replaceAll(",", "").replaceAll("%", ""));//bhargavi
                }
//           System.out.println("ct data"+CtAvgdata);
            }
        }
        maxVal = this.getColumnMaximumValue(columnName);
        minVal = this.getColumnMinimumValue(columnName);
        diff = new BigDecimal(0);
        if (maxVal != null && minVal != null) {
            diff = maxVal.subtract(minVal);
        }
        double value = 0.0;
        int originalMeasIndex;
        String measure = "";
        if (container.isReportCrosstab()) {

//            originalMeasIndex = repBD.findMeasureIndexInCT(container,columnName);
//            originalMeasIndex = originalMeasIndex - (container.getViewByCount());
//            ArrayList measureList = (ArrayList)container.getTableHashMap().get("Measures");
//            measure = (String) measureList.get(originalMeasIndex);
            measure = ((PbReturnObject) container.getRetObj()).crosstabMeasureId.get(columnName);
            if (colorGroup.isIsAvgBased() || colorGroup.isMinMaxBased() || colorGroup.isIsPercentBased()) {
                columnName = ((PbReturnObject) container.getRetObj()).crosstabMeasureId.get(columnName);
            } else if (container.getColumnViewByName().trim().equalsIgnoreCase("Ddate")) {
                columnName = ((PbReturnObject) container.getRetObj()).crosstabMeasureId.get(columnName);
            }
        }
        if (rowflag) {
            isColorPresent = colorGroup.isColorCodePresent(container.getDisplayColumns().get(0));
        }
       
        //Added By Ram 2 May 2016 for CrossTab Across Current Data
         if (Container.isColorAppyAcrossCurrentData()) {
            if (container.isReportCrosstab()) {
                if (measure.contains("A_")) {
                    isColorPresent = colorGroup.isColorCodePresent(columnName, container.getReportParameter(), measure);
                    colorCode = colorGroup.getColorCodeObj(columnName, container.getReportParameter(), measure);
                } else {
                    isColorPresent = colorGroup.isColorCodePresent(columnName, container.getReportParameter(), "A_" + measure);
                    colorCode = colorGroup.getColorCodeObj(columnName, container.getReportParameter(), "A_" + measure);
                }
            } 
        }
         else{
             if(colorGroup.getEleId().contains(columnName)) {
            //added by Dinanath
            if (container.isReportCrosstab()) {
                if (measure.contains("A_")) {
                    isColorPresent = colorGroup.isColorCodePresent(columnName, container.getReportParameter(), measure);
                    colorCode = colorGroup.getColorCodeObj(columnName, container.getReportParameter(), measure);
                } else {
                    isColorPresent = colorGroup.isColorCodePresent(columnName, container.getReportParameter(), "A_" + measure);
                    colorCode = colorGroup.getColorCodeObj(columnName, container.getReportParameter(), "A_" + measure);
                }
            } else {
                isColorPresent = colorGroup.isColorCodePresent(columnName, container.getReportParameter(), measure);
                colorCode = colorGroup.getColorCodeObj(columnName, container.getReportParameter(), measure);
            }
        }
    }
        if (isColorPresent) {
            if (!RTMeasureElement.isRunTimeMeasure(columnNameR) && "".equals(qryData.getFieldValueString(columnNo, columnNameR))) {
                bgColor = "";
            } else {
                if (RTMeasureElement.isRunTimeMeasure(columnNameR)) {
                    value = Double.parseDouble(this.qryData.getFieldValueRuntimeMeasure(columnNo, columnName).toString());
                } else {
                    value = Double.parseDouble(qryData.getFieldValueString(columnNo, columnNameR));
                }
                if (rowflag) {
                    String dimentionValue = qryData.getFieldValueString(columnNo, container.getDisplayColumns().get(0));
                    colorGroup.setDimentionValue(dimentionValue);

                }
                if (container.isReportCrosstab()) {

                    if (colorCode != null && colorCode.isMinMaxBased()) {
                        ArrayList<String> RowDatavalues = new ArrayList();
                        if (!columnNameR.contains("_")) {
                            bgColor = colorcodeMap.get(columnName).get(value);
                        } else {
                            bgColor = "";
                        }

                    } else if (colorCode != null && colorCode.isIsAvgBased()) {
                        ArrayList<String> a1 = container.getReportCollect().reportRowViewbyValues;
                        StringBuffer ViewbyColumnBf = new StringBuffer();
                        for (int c = 0; c < a1.size(); c++) {
                            ViewbyColumnBf.append(a1.get(c));
                        }
                        String currentViewby = ViewbyColumnBf.toString();
                        //Modified by Ram 20/04/2016 Apply color for all parameter.
                        if (!columnNameR.contains("_")) {
                        if (measure.contains("A_")) {
                                bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), measure, diff, crosstab, currentViewby,Container.isColorAppyForAllParameters());
                        } else {//added by Dinanath
                                bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), "A_" + measure, diff, crosstab, currentViewby,Container.isColorAppyForAllParameters());
                        }

                    } else {
                            bgColor = "";
                        }
                    } else if (colorCode != null && colorCode.isIsPercentBased()) {
                        ArrayList<String> a1 = container.getReportCollect().reportRowViewbyValues;
                        StringBuffer ViewbyColumnBf = new StringBuffer();
                        for (int c = 0; c < a1.size(); c++) {
                            ViewbyColumnBf.append(a1.get(c));
                        }
                        String currentViewby = ViewbyColumnBf.toString();
                        if (!columnNameR.contains("_")) {
                            if (measure.contains("A_")) {
                                //Modified by Ram 20/04/2016
                                bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), measure, diff, avgVal, currentViewby,Container.isColorAppyForAllParameters());
//                                bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), measure, diff, crosstab, currentViewby);
                            } else {//added by Dinanath
                                bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), "A_" + measure, diff, avgVal, currentViewby,Container.isColorAppyForAllParameters());
//                                bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), "A_" + measure, diff, crosstab, currentViewby);
                            }

                        } else {
                            bgColor = "";
                        }
                    } else {
                        ArrayList<String> a1 = container.getReportCollect().reportRowViewbyValues;
                        StringBuffer ViewbyColumnBf = new StringBuffer();
                        for (int c = 0; c < a1.size(); c++) {
                            ViewbyColumnBf.append(a1.get(c));
                    }
                        String currentViewby = ViewbyColumnBf.toString();
                        if (measure.contains("A_")) {
                            //Modified by Ram 20/04/2016
                            bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), measure, diff, currentViewby,Container.isColorAppyForAllParameters());
                        } else {//added by Dinanath
                            bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), "A_" + measure, diff, currentViewby,Container.isColorAppyForAllParameters());
                        }
                    }
                } else {

                    ArrayList<String> a1 = container.getReportCollect().reportRowViewbyValues;
                    StringBuffer ViewbyColumnBf = new StringBuffer();
                    for (int c = 0; c < a1.size(); c++) {
                        ViewbyColumnBf.append(a1.get(c));
                    }
                    String currentViewby = ViewbyColumnBf.toString();
                    //Modified by Ram 20/04/2016
                    if (colorCode != null && colorCode.isIsAvgBased()) {
                        bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), "", diff, avgVal, currentViewby,Container.isColorAppyForAllParameters());
                    } else if (colorCode != null && colorCode.isIsPercentBased()) {//added by Dinanath
                        bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), "", diff, avgVal, currentViewby,Container.isColorAppyForAllParameters());
                    } else {
                        bgColor = colorGroup.getColor(columnName, value, container.getReportParameter(), "", diff, currentViewby, avgVal,colorCode,Container.isColorAppyForAllParameters());
                    }
                }
            }
            //ColorConstants colorContsants=ColorConstants.getInstance();
            //BigDecimal diffVal=maxVal.subtract(minVal);
            //bgColor=ColorConstants.getGradientColor(qryData.getFieldValueBigDecimal(columnNo, columnName), new BigDecimal(1831609),"");
        }
        return bgColor;
    }

    public ExcelCellFormat getExcelCellProperty(int actualRow, String columnName) {
        ExcelCellFormatGroup excelCellGroup = container.getExcelCellGroup();
        ExcelCellFormat cell = excelCellGroup.getExcelCell(actualRow, columnName, container.getReportParameter());

        return cell;
    }

    public boolean isEditable(String columnName) {
        if (RTMeasureElement.isRunTimeExcelColumn(columnName)) {
            return true;
        }
        return false;
    }

    public RunTimeExcelColumn getRTExcelColumn(String columnName) {
        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
        RunTimeExcelColumn col = excelColGroup.getRunTimeColumn(columnName);
        return col;
    }
    public BigDecimal getFormattedMeasureDatarank(int actualRow, String element) {
        String formattedData = "";
        Object objData = null;
        try {
            objData = qryData.getFieldValue(actualRow, element);

        int column = container.getDisplayColumns().indexOf(element);
        formattedData = objData.toString();
        BigDecimal value = new BigDecimal(formattedData.replaceAll(",", ""));
        formattedData = this.formatMeasureData(value, column);
        } catch (Exception e) {
            formattedData = "";
        }
        if(element.contains("_rank") ||element.contains("_PYtdrank")|| element.contains("_PQtdrank")|| element.contains("_PMtdrank")|| element.contains("_Qtdrank")|| element.contains("_Ytdrank")){
//int formattedData1=Integer.parseInt(formattedData);
if(!formattedData.toString().equalsIgnoreCase("")){
int i1 = new BigDecimal(formattedData).setScale(0, RoundingMode.HALF_UP).intValueExact();
formattedData=String.valueOf(i1);
}
                    }
        return new BigDecimal(formattedData);
    }
    public String getFormattedMeasureData(int actualRow, int column) {
        String formattedData;
        String element = container.getDisplayColumns().get(column);
        if (RTMeasureElement.isRunTimeExcelColumn(element)) {
            formattedData = "";
            RunTimeExcelColumn col = getRTExcelColumn(element);
            if (col != null) {
                Object objData = qryData.getFieldValue(actualRow, element);
                if (objData != null) {
                    if (objData instanceof BigDecimal) {
                        formattedData = this.formatMeasureData((BigDecimal) objData, "");
                        ((PbReturnObject) container.getRetObj()).setRuntimeMeasureData(element, (BigDecimal) objData);
                    } else {
                        if ("".equalsIgnoreCase(objData.toString())) {
                            ((PbReturnObject) container.getRetObj()).setRuntimeMeasureData(element, BigDecimal.ZERO);
                        }
                        formattedData = objData.toString();
                    }
                }
                /*
                 * Object objData = col.getData(actualRow); if(objData != null){
                 * formattedData = objData.toString(); if (!(isExcelDisplay())
                 * && formattedData.startsWith("=")){ formattedData = "0"; }
                }
                 */
            }
        }
        else if (RTMeasureElement.isRunTimeMeasure(element)&& (element.contains("_MTD")|| element.contains("_QTD")|| element.contains("_YTD")|| element.contains("_PMTD")|| element.contains("_PQTD")|| element.contains("_PYTD")
                ||element.contains("_MOM")||element.contains("_QOQ")||element.contains("_YOY")||element.contains("_MOYM")||element.contains("_QOYQ")||element.contains("_MOMPer")||element.contains("_QOQPer")||element.contains("_YOYPer")||element.contains("_MOYMPer")||element.contains("_QOYQPer")
                ||element.contains("_PYMTD")||element.contains("_PYQTD")||element.contains("_WTD")||element.contains("_PWTD")||element.contains("_PYWTD")||element.contains("_WOWPer")||element.contains("_WOYWPer")||element.contains("_WOW")||element.contains("_WOYW"))) {
            formattedData = "";
            Object objData = null;
            try{
            objData = qryData.getFieldValue(actualRow, element);
            }catch(Exception e){
                ArrayList<BigDecimal> value  = container.getRetObj().getRunTimeMeasureData(element);
                BigDecimal valueDecimal = value.get(column);
                formattedData = this.formatMeasureData(valueDecimal, column);
            }
                if (objData != null) {
                    if ("".equalsIgnoreCase(objData.toString())) {
                           ((PbReturnObject) container.getRetObj()).setRuntimeMeasureData(element, BigDecimal.ZERO);
                            formattedData = objData.toString();
                        }else{
                        formattedData = objData.toString();
                        BigDecimal value = new BigDecimal(formattedData.replaceAll(",", ""));
                        formattedData = this.formatMeasureData(value, column);
                       ((PbReturnObject) container.getRetObj()).setRuntimeMeasureData(element, value);
                    }
                }
        }
        else if (container.getDataTypes().get(column).toString().equals("N")) {
            try {
                BigDecimal measData = this.getMeasureData(actualRow, element);
                formattedData = this.formatMeasureData(measData, column);
            } catch (Exception ex) {
                formattedData = qryData.getFieldValueString(actualRow, element);
            }
        } else if (container.getDataTypes().get(column).toString().equals("D")) {
            try {
                formattedData = qryData.getFieldValueDateString(actualRow, element);
                formattedData = this.formatMeasureData(new BigDecimal(formattedData), column);

            } catch (Exception ex) {
                formattedData = qryData.getFieldValueString(actualRow, element);
            }

        } else {

            //commented by anitha
//                try{
//                formattedData = qryData.getFieldValueString(actualRow, element);
//                formattedData = this.formatMeasureData(new BigDecimal(formattedData), column);
//                }catch (Exception ex)
//                  {
            formattedData = qryData.getFieldValueString(actualRow, element);
//            }

        }
                    if(element.contains("_rank") ||element.contains("_PYtdrank")|| element.contains("_PQtdrank")|| element.contains("_PMtdrank")|| element.contains("_Qtdrank")|| element.contains("_Ytdrank")){
if(!formattedData.toString().equalsIgnoreCase("")){
int i1 = new BigDecimal(formattedData).setScale(0, RoundingMode.HALF_UP).intValueExact();
formattedData=String.valueOf(i1);
}
                    }
        return formattedData;

    }

    public String getFormattedMeasureDataForTimeDB(int actualRow, int column) {
        String formattedData;
        String element = container.DisplayColsfortimeDB[column];

        try {
            BigDecimal measData = this.getMeasureData(actualRow, element);
            formattedData = this.formatMeasureDataForTimeDB(measData, column);
        } catch (Exception ex) {
            formattedData = qryData.getFieldValueString(actualRow, element);
        }

        return formattedData;

    }

    public String formatMeasureDataForTimeDB(BigDecimal measData, int column) {
        String formattedData = "";
        BigDecimal bd = new BigDecimal("1");
        if (measData != null) {
            String element = container.DisplayColsfortimeDB[column];
            //element.split("_")[2].replace(element, element);
            //added by sruthi for columnproperties
            String array[] = element.split("_");
            element = array[0] + "_" + array[1];
            //ended bys ruthi
            String nbrSymbol = container.getNumberSymbol(element);
            int precision = 0;
            if (RTMeasureElement.isRunTimeMeasure(element)) {
                precision = container.getRoundPrecisionForMeasure(RTMeasureElement.getOriginalColumn(element));

            } else {
                precision = container.getRoundPrecisionForMeasure(element);
            }

            if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                formattedData = measData.toString();
            } else {
                formattedData = measData.toString();
                if (this.istimeConversionEnable(element)) {
                    formattedData = this.convertDataToTime(measData);
                } else {
                    formattedData = NumberFormatter.getModifiedNumberFormat(measData, nbrSymbol, precision);
                    if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("%")) {
                        BigDecimal data;
                        if (formattedData.contains(",")) {
                            data = new BigDecimal(String.valueOf(formattedData).replaceAll(",", ""));
                        } else {
                            data = new BigDecimal(String.valueOf(formattedData));
                        }
                        if (data.compareTo(bd) == 0 || data.compareTo(bd) == 1) {
                            formattedData = NumberFormatter.getModifiedNumber(measData, nbrSymbol, precision);
                        } else {
                            formattedData = NumberFormatter.getModifiedNumber(measData, null, precision);
                        }
                    }
                }

                //modified by anitha
                if (formattedData != null && !formattedData.equalsIgnoreCase("")) {
                    Float f = Float.parseFloat(formattedData.replaceAll(",", "").replaceAll("%", ""));
                    if (f == 0 && container.isMaskZeros()) {
                        formattedData = "";
                    }
                }
                //end of code by anitha
                if (container.isMaskZeros() && formattedData.trim().equalsIgnoreCase("0")) {
                    formattedData = "";
                }
            }
        }
        return formattedData;
    }
//added by Bhargavi for kpi Dashboard Measure values

    public BigDecimal getFormattedMeasureDataForkpi(int actualRow, int column) {

        BigDecimal measData = BigDecimal.ZERO;
        String element = container.getDisplayColumns().get(column);

        if (container.getDataTypes().get(column).toString().equals("N")) {
            measData = this.getMeasureData(actualRow, element);
        }

        return measData;
    }

    public String formatMeasureDataForkpi(BigDecimal measData, String element) {
        String formattedData = "";
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        BigDecimal bd = new BigDecimal("1");
        if (measData != null) {
            //String element = crosstabMeasureId.get(element1);
            String nbrSymbol = container.getNumberSymbol(element);
            int precision = 0;
            if (RTMeasureElement.isRunTimeMeasure(element)) {
                precision = container.getRoundPrecisionForMeasure(RTMeasureElement.getOriginalColumn(element));
            } else {
                precision = container.getRoundPrecisionForMeasure(element);
            }
            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
                if (container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null && ((List<String>) summarizedmMesMap.get("summerizedQryeIds")) != null && ((List<String>) summarizedmMesMap.get("summerizedQryeIds")).contains(element.replace("A_", ""))) {
                    precision = container.getRoundPrecisionForMeasure(element);
                } else {
                    precision = container.getRoundPrecisionForMeasure(element);
                }
            }
            if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                formattedData = measData.toString();
            } else {
                formattedData = measData.toString();
                if (this.istimeConversionEnable(element)) {
                    formattedData = this.convertDataToTime(measData);
                } else {
                    formattedData = NumberFormatter.getModifiedNumberFormat(measData, nbrSymbol, precision);
                    if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("%")) {
                        BigDecimal data;
                        if (formattedData.contains(",")) {
                            data = new BigDecimal(String.valueOf(formattedData).replaceAll(",", ""));
                        } else {
                            data = new BigDecimal(String.valueOf(formattedData));
                        }

                        if (data.compareTo(bd) == 0 || data.compareTo(bd) == 1) {
                            formattedData = NumberFormatter.getModifiedNumber(measData, nbrSymbol, precision);
                        } else {
                            formattedData = NumberFormatter.getModifiedNumber(measData, null, precision);
                        }
                    }
                }

            }
            //modified by anitha
            if (formattedData != null && !formattedData.equalsIgnoreCase("")) {
                Float f = Float.parseFloat(formattedData.replaceAll(",", "").replaceAll("%", ""));
                if (f == 0 && container.isMaskZeros()) {
                    formattedData = "";
                }
            }
            //end of code by anitha
            if (container.isMaskZeros() && formattedData.trim().equalsIgnoreCase("0")) {
                formattedData = "";
            }
        }
        return formattedData;
    }
//end of code By Bhargavi

    public boolean isDrillAcrossSupported() {
        return container.isDrillAcrossSupported();
    }

    public String formatMeasureData(BigDecimal measData, int column) {
        String formattedData = "";
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        BigDecimal bd = new BigDecimal("1");
        //added by krishan for cross tab
        String elementid = container.getDisplayColumns().get(column);
        String ColId = elementid.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_rankST", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "")
                .replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_PYtdrank", "").replace("_PQtdrank", "").replace("_PMtdrank", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "");
        String numberformate = "N";
        ArrayList singleColProp = null;
        HashMap ColumnProperties = null;
        ColumnProperties = (container.getColumnProperties() == null) ? new HashMap() : container.getColumnProperties();
        if (container.isReportCrosstab()) {
            if (crosstabMeasureId != null && crosstabMeasureId.containsKey(ColId)) {
                ColId = (String) crosstabMeasureId.get(ColId);
            }
            //added by sruthi for numberformate crosstab
            if (ColumnProperties.containsKey(ColId)) {
                singleColProp = (ArrayList) ColumnProperties.get(ColId);
                numberformate = singleColProp.get(9).toString();
            }
            //ended by sruthi
        } else {
            ////added by sruthi for numberformate
            singleColProp = (ArrayList) ColumnProperties.get(ColId);
            try {
                numberformate = singleColProp.get(9).toString();
            } catch (Exception e) {
                numberformate = "";
            }
            //String  numberformate=String.valueOf(singleColProp);
        }      //ended by sruthi
        if (measData != null) {
            String element = container.getDisplayColumns().get(column);
            String nbrSymbol = container.getNumberSymbol(element);
            int precision = 0;
            if (RTMeasureElement.isRunTimeMeasure(element)) {
                precision = container.getRoundPrecisionForMeasure(RTMeasureElement.getOriginalColumn(element));
                // added by Govardhan for percentwise to have rounding precision 2
                if (precision == 0 && (element.contains("_percentwise") || element.contains("_pwst"))) {
                    precision = 2;
                }
            } else {
                precision = container.getRoundPrecisionForMeasure(element);
            }
            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
                if (container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null && ((List<String>) summarizedmMesMap.get("summerizedQryeIds")) != null && ((List<String>) summarizedmMesMap.get("summerizedQryeIds")).contains(element.replace("A_", ""))) {
                    precision = container.getRoundPrecisionForMeasure(element);
                } else {
                    precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                }
            }
            //int precision = container.getRoundPrecisionForMeasure(element);
            if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                formattedData = measData.toString();
            } else {
                formattedData = measData.toString();
                if (this.istimeConversionEnable(element)) {
                    formattedData = this.convertDataToTime(measData);
                    // 
                } else {
                    formattedData = NumberFormatter.getModifiedNumberFormat(measData, nbrSymbol, precision);
                    if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("%")) {
                        BigDecimal data;
                        if (formattedData.contains(",")) {
                            data = new BigDecimal(String.valueOf(formattedData).replaceAll(",", ""));
                        } else {
                            data = new BigDecimal(String.valueOf(formattedData));
                        }
                        // 
                        if (data.compareTo(bd) == 0 || data.compareTo(bd) == 1) {
                            //added by sruthi for numberformate
                            if (!numberformate.equalsIgnoreCase("Y")) {
                                formattedData = NumberFormatter.getModifiedNumber(measData, nbrSymbol, precision);
                            } else {
                                formattedData = NumberFormatter.getModifiedNumberFormat(measData, nbrSymbol, precision);
                            }
                            //ended by sruthi
                        } else if (!numberformate.equalsIgnoreCase("Y")) {
                            formattedData = NumberFormatter.getModifiedNumber(measData, nbrSymbol, precision);
                        } else {
                            formattedData = NumberFormatter.getModifiedNumberFormat(measData, nbrSymbol, precision);
                        }
                    }
                }

//            HashMap colProps = container.getColumnProperties();
//                if (colProps != null && colProps.containsKey(element)) {
//                ArrayList propList = (ArrayList) colProps.get(element);
//                String colSymbol = (String) propList.get(7);
//                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) {
//                        if (colSymbol.equalsIgnoreCase("%")) {
//                            formattedData = formattedData + colSymbol;
//                        } else {
//                    formattedData = colSymbol + formattedData;
//                }
//            }
//                }
            }

            //added by krshan pratap 
            if ((container.datashow).equalsIgnoreCase("Indian Based")) {
//                String symbol=null;
                formattedData = NumberFormatter.getModifiedNumberReport(measData, nbrSymbol, precision);
            }
            //modified by anitha
            if (formattedData != null && !formattedData.equalsIgnoreCase("")) {
                formattedData = formattedData.replace(nbrSymbol, "");
                Float f = Float.parseFloat(formattedData.replaceAll(",", "").replaceAll("%", ""));
                if (f == 0 && container.isMaskZeros()) {
                    formattedData = "";
                }else{
                    if(nbrSymbol!=null && !nbrSymbol.equalsIgnoreCase("Ab") && !nbrSymbol.equalsIgnoreCase("nf")){
                    if (!numberformate.equalsIgnoreCase("Y")) {
                              formattedData = formattedData+nbrSymbol;
                         }else
                             formattedData = formattedData; 
                    }
                }
            }
            //end of code by anitha
            if (container.isMaskZeros() && formattedData.trim().equalsIgnoreCase("0")) {
                formattedData = "";
            }
            //String symbol = container.getn
        }
        return formattedData;
    }

    public String formatMeasureData(BigDecimal measData, String nbrSymbol) {
        String formattedData = "";
        if (measData != null) {
            formattedData = NumberFormatter.getModifiedNumber(measData, nbrSymbol);
        }
        return formattedData;
    }

    public String getColumnId(int column) {
        ArrayList<String> displayCols = container.getDisplayColumns();
        return displayCols.get(column);
    }

    public BigDecimal getMeasureData(int row, String columnName) {
        BigDecimal numericData = null;
        if (columnName.contains("rankST")) {
            row = this.getRowForSTRank();
        }
        if(columnName.contains("_MTD")||columnName.contains("_QTD")||columnName.contains("_YTD")||columnName.contains("_PMTD")||columnName.contains("_PQTD")||columnName.contains("_PYTD")||columnName.contains("_PMTD")||columnName.contains("_PQTD")||columnName.contains("_PYTD")
                    ||columnName.contains("_MOM")||columnName.contains("_QOQ")||columnName.contains("_YOY")||columnName.contains("_MOYM")||columnName.contains("_QOYQ")||columnName.contains("_MOMPer")||columnName.contains("_QOQPer")||columnName.contains("_YOYPer")||columnName.contains("_MOYMPer")||columnName.contains("_QOYQPer")||columnName.contains("_PYMTD")||columnName.contains("_PYQTD")
                    ||columnName.contains("_WTD")||columnName.contains("_PWTD")||columnName.contains("_PYWTD")||columnName.contains("_WOWPer")||columnName.contains("_WOYWPer")||columnName.contains("_WOW")||columnName.contains("_WOYW")){
            row = this.getRowForRTTimeDimension();
        }
        if (RTMeasureElement.isRunTimeMeasure(columnName)) {
            try{
            numericData = qryData.getFieldValueRuntimeMeasure(row, columnName);
            }catch(Exception e){
                String formattedData = "";
                row = this.getActualrow();
                Object objData = qryData.getFieldValue(row, columnName);
                if (objData != null) {
                    if ("".equalsIgnoreCase(objData.toString())) {                            
                            formattedData = objData.toString();
                        }else{
                        formattedData = objData.toString();
                        int column = container.getDisplayColumns().indexOf(columnName);
                        numericData = new BigDecimal(formattedData.replaceAll(",", ""));                                              
                    }
                }                
            }                                   
            //added by anitha for MTD, QTD, YTD in AO Report
            if(columnName.contains("_MTD")||columnName.contains("_QTD")||columnName.contains("_YTD")||columnName.contains("_PMTD")||columnName.contains("_PQTD")||columnName.contains("_PYTD")||columnName.contains("_PMTD")||columnName.contains("_PQTD")||columnName.contains("_PYTD")
                    ||columnName.contains("_MOM")||columnName.contains("_QOQ")||columnName.contains("_YOY")||columnName.contains("_MOYM")||columnName.contains("_QOYQ")||columnName.contains("_MOMPer")||columnName.contains("_QOQPer")||columnName.contains("_YOYPer")||columnName.contains("_MOYMPer")||columnName.contains("_QOYQPer")||columnName.contains("_PYMTD")||columnName.contains("_PYQTD")
                    ||columnName.contains("_WTD")||columnName.contains("_PWTD")||columnName.contains("_PYWTD")||columnName.contains("_WOWPer")||columnName.contains("_WOYWPer")||columnName.contains("_WOW")||columnName.contains("_WOYW")){
            try{
            if(numericData.toString().equalsIgnoreCase(BigDecimal.ZERO.toString())){
                numericData = qryData.getFieldValueBigDecimal(row, columnName);
                if(numericData==null){
                   numericData =new BigDecimal(qryData.getFieldValueString(row, columnName));
                }
            }}catch(Exception e){
                numericData = qryData.getFieldValueBigDecimal(row, columnName);
            }
            }
            //end of code by anitha for MTD, QTD, YTD in AO Report
        } else {
            numericData = qryData.getFieldValueBigDecimal(row, columnName);
        }

        return numericData;
    }

    public BigDecimal getMeasureDataForComputation(int row, String columnName) {
        BigDecimal measData = this.getMeasureData(row, columnName);
        if (measData == null) {
            measData = BigDecimal.ZERO;
        }
        return measData;
    }

    public String getDrillAcrossData(int row) {
        String data = "";
        ArrayList<String> dispCols = getDisplayColumns();
        for (int i = 0; i < getViewByCount(); i++) {
            String colName = dispCols.get(i);
            if (RTDimensionElement.isRunTimeDimension(colName)) {
                continue;
            } else {
                data = data + "," + colName + ":" + qryData.getFieldValueString(row, colName);
                break;  //remove this break statement if more than one dimension should be included for drill across
            }
        }
        if (data.length() > 0) {
            data = data.substring(1);
        }
        return data;
    }

    public String getDimensionData(int row, String columnName) {
        String cellData;
        if (columnName.equalsIgnoreCase("Time")) {
            columnName = columnName.toUpperCase();
        }

        if (RTDimensionElement.isRunTimeDimension(columnName)) {
            cellData = qryData.getFieldValueRuntimeDimension(row, columnName);
        } else {
            cellData = qryData.getFieldValueString(row, columnName);
        }

        return cellData;
    }

    public String getDateData(int row, String columnName) {
        String cellData = "";
        if (columnName.equalsIgnoreCase("Time")) {
            columnName = columnName.toUpperCase();
            cellData = qryData.getFieldValueDateString(row, columnName);
        }
        //
        if (cellData.isEmpty()) {
            cellData = qryData.getFieldValueString(row, columnName);
        }
        return cellData;
    }

    public boolean isNetTotalRequired() {
        return container.getNetTotalReq();
    }

    public boolean isOverAllMaxRequired() {
        return container.getOverAllMaxValueReq();
    }

    public boolean isOverAllMinRequired() {
        return container.getOverAllMinValueReq();
    }

    public boolean isAverageRequired() {
        return container.getAvgTotalReq();
    }

    public boolean isCategoryMaxRequired() {
        return container.getCatMaxValueReq();
    }

    public boolean isCategoryMinRequired() {
        return container.getCatMinValueReq();
    }

    public boolean isMedianRequired(String measId) {
        if (this.isMedianRequired()) {
            if (container.getStatFuncForMeas(measId).contains(Container.MEDIAN)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isMeanRequired(String measId) {
        if (this.isMeanRequired()) {
            if (container.getStatFuncForMeas(measId).contains(Container.MEAN)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isAnySubtotallingRequired() {
        return (this.isMeanRequired()
                || this.isMedianRequired()
                || this.isStdDeviationRequired()
                || this.isNetTotalRequired()
                || this.isVarianceRequired()
                || this.isCategoryMaxRequired()
                || this.isCategoryMinRequired()
                || this.isRowCountRequired()
                || this.isCatAvgRequired()
                || this.isTopBottomWithOthersEnable());

    }

    public boolean isStdDeviationRequired(String measId) {
        if (this.isStdDeviationRequired()) {
            if (container.getStatFuncForMeas(measId).contains(Container.STANDARD_DEVIATION)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isVarianceRequired(String measId) {
        if (this.isVarianceRequired()) {
            if (container.getStatFuncForMeas(measId).contains(Container.VARIANCE)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isGrandTotalRequired() {
        return container.getGrandTotalReq();
    }

    public boolean isGrandTotalRequired(String measId) {
        if (isGrandTotalRequired()) {
            if (RTMeasureElement.isRunTimeMeasure(measId)) {
                return RTMeasureElement.getMeasureType(measId).isGrandTotalSupported();
            } else {
                return true;
            }

        }
        return false;

    }

    public boolean isMedianRequired() {
        Set<String> statFuncSet = container.getStatFuncsForReport();
        return this.isStatFuncReqd(Container.MEDIAN, statFuncSet);
    }

    public boolean isMeanRequired() {
        Set<String> statFuncSet = container.getStatFuncsForReport();
        return this.isStatFuncReqd(Container.MEAN, statFuncSet);
    }

    public boolean isStdDeviationRequired() {
        Set<String> statFuncSet = container.getStatFuncsForReport();
        return this.isStatFuncReqd(Container.STANDARD_DEVIATION, statFuncSet);
    }

    public boolean isVarianceRequired() {
        Set<String> statFuncSet = container.getStatFuncsForReport();
        return this.isStatFuncReqd(Container.VARIANCE, statFuncSet);
    }

    public boolean isStatFuncReqd(String funcName, Set<String> statFuncSet) {
        boolean isStatFuncReqd = false;
        if (statFuncSet != null) {
            for (String statFunc : statFuncSet) {
                if (funcName.equals(statFunc)) {
                    isStatFuncReqd = true;
                    break;
                }
            }
        }
        return isStatFuncReqd;
    }

    public boolean isColumnVisible(String column) {
        HashMap columnVisibility = container.getColumnsVisibility();
        ArrayList<String> hideMsrlist = container.getReportCollect().getHideMeasures();
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
//        
        ArrayList<String> hideViewbyList = container.getReportCollect().getHideViewbys();
        String col = column;
        if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(column) && hideMsrlist.contains(crosstabMeasureId.get(column).toString().replace("A_", ""))) {
            return false;
        } else if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(column) && hideViewbyList.contains(crosstabMeasureId.get(column).toString().replace("A_", ""))) {
            return false;
        } else if (hideMsrlist != null && !hideMsrlist.isEmpty() && hideMsrlist.contains(col.replace("A_", ""))) {
            return false;
        } else if (hideViewbyList != null && !hideViewbyList.isEmpty() && hideViewbyList.contains(col.replace("A_", ""))) {
            return false;
        } else if ("none".equals(columnVisibility.get(column))) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isSignApplicable(String column) {
        ColorHelper signHelper = container.getSignForMeasure(column);
        if (signHelper != null) {
            return true;
        } else {
            return false;
        }

//        ArrayList sign=container.getSigns();
//        if(sign.contains(column))
//            return true;
//        else
//            return false;
    }

    public boolean isFontColorApplicable(String column) {
        ColorHelper colorHelper = container.getFontColorForMeasure(column);
        if (colorHelper != null) {
            return true;
        } else {
            return false;
        }
    }

//    public void initializeGraphDataSet(int rowNum) {
//        if (displayedSet == null || displayedSetwithGT == null) {
//            displayedSet = new PbReturnObject();
//            displayedSetwithGT = new PbReturnObject();
//            ArrayList<String> originalCols = container.getOriginalColumns();
//            displayedSet.setColumnNames((String[]) originalCols.toArray(new String[0]));
//            displayedSetwithGT.setColumnNames((String[]) originalCols.toArray(new String[0]));
//            container.setDisplayColumnsInColumnPagination(container.getDisplayColumns());
//            container.setDisplayLabelsInColumnPagination(container.getDisplayLabels());
//        }
//        int actualRow = qryData.getViewSequence().get(rowNum);
//        String columnName;
//        for (int i = 0; i < container.getOriginalColumns().size(); i++) {
//            columnName = container.getOriginalColumns().get(i);
//            if (i < getViewByCount()) {
//                if (columnName.equalsIgnoreCase("Time")) {
//                    columnName = columnName.toUpperCase();
//                }
//
//                if (!RTDimensionElement.isRunTimeDimension(columnName)) {
//                    displayedSet.setFieldValue(columnName, qryData.getFieldValueString(actualRow, columnName));
//                    displayedSetwithGT.setFieldValue(columnName, qryData.getFieldValueString(actualRow, columnName));
//                }
//            } else {
//                if (!RTMeasureElement.isRunTimeMeasure(columnName)) {
//                    displayedSet.setFieldValue(columnName, qryData.getFieldValueBigDecimal(actualRow, columnName));
//                    displayedSetwithGT.setFieldValue(columnName, qryData.getFieldValueBigDecimal(actualRow, columnName));
//                }
//            }
//        }
//        displayedSet.addRow();
//        displayedSetwithGT.addRow();
////       if(rowNum==getToRow()-1)
////       {
//        container.setDisplayedSet(displayedSet);
//        container.setDisplayedSetWithGT(displayedSetwithGT);
////       }
//    }
    public String getColumnEdgeLabel(int colLayer) {
        return container.getReportCollect().getElementName((String) container.getReportCollect().reportColViewbyValues.get(colLayer));
    }

    public ArrayList<String> getDisplayColumns() {
        return container.getDisplayColumns();
    }

    public boolean isReportCrosstab() {
        return container.isReportCrosstab();
    }

    public ArrayList<String> getDataTypes() {
        return container.getDataTypes();
    }

    public ArrayList<String> getDisplayLabels() {
        return container.getDisplayLabels();
    }

    public int getMeasurePosition() {
        return container.getMeasurePosition();
    }

    public String getReportId() {
        return container.getReportId();
    }

    public int getActualRow(int rowNum) {
        return qryData.getViewSequence().get(rowNum);
    }

    public ArrayList<String> getLinks() {
        return container.getLinks();
    }

    public ArrayList<String> getDisplayTypes() {
        return container.getDisplayTypes();
    }

    public ArrayList<String> getSearchColumns() {
        return container.getSearchColumns();
    }

    public ArrayList<String> getSearchConditions() {
        return container.getSearchConditions();
    }

    public ArrayList<Object> getSearchValues() {
        return container.getSearchValues();
    }

    public BigDecimal getColumnAverageValue(String columnId) {
        return qryData.getColumnAverageValue(columnId);
    }

    public BigDecimal getColumnMaximumValue(String columnId) {
        return qryData.getColumnMaximumValue(columnId);
    }

    public BigDecimal getColumnMinimumValue(String columnId) {
        return qryData.getColumnMinimumValue(columnId);
    }

    public BigDecimal getColumnGrandTotalValue(String columnId) {
        return qryData.getColumnGrandTotalValue(columnId);
    }

    public int getReportMeasureCount() {
        return container.getReportMeasureCount();
    }

    public ArrayListMultimap<Integer, Integer> getCrosstabColumnSpan() {
        return container.getCrosstabColumnSpan();
    }

    public int getColumnLayerCount() {
        if (this.isReportCrosstab()) {
            return this.getCrosstabColumnSpan().keySet().size() + 1;
        } else {
            return 1;
        }
    }

    public ArrayList<String> getTableDisplayMeasures() {
        return container.getTableDisplayMeasures();
    }

    public String getContextPath() {
        return this.ctxPath;
    }

    public void sortDataSet(ArrayList<String> sortColumns, char[] sortTypes, char[] sortDataTypes) {
        ArrayList<Integer> viewSequence = this.qryData.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        this.qryData.setViewSequence(viewSequence);

    }

    public String getFontColorForMeasure(String measure, int actualRow) {
        String elementId = "";
        String evalMethod = "";
        if (container.isFontColorSetForMeasure(measure)) {
            ColorHelper colorHelper = container.getFontColorForMeasure(measure);
            elementId = colorHelper.getDependentMeasure();
            evalMethod = colorHelper.getEvaluationMethod();
            if (evalMethod.equalsIgnoreCase("WHATIF")) {
                if (this.getMeasureDataForComputation(actualRow, elementId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) < 0) {
                    return "green";
                } else if (this.getMeasureDataForComputation(actualRow, elementId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) > 0) {
                    return "red";
                } else {
                    return "black";
                }
            }
        }
        return "";
    }

    public String getSignStyleforMeasure(String measure, int actualRow) {
        BigDecimal bd = new BigDecimal("0");
        String depMeasId = "";
        String signEvaluationMethod = "";
        String measType = getmeasureType(measure);
        //modified by Nazneen
//        if(measType==null || measType.equalsIgnoreCase("null")){
        if (measType == null || measType.equalsIgnoreCase("null") || measType.equalsIgnoreCase("none")) {
            measType = "Standard";
        }

        if (container.isSignSetForMeasure(measure)) {
            ColorHelper signHelper = container.getSignForMeasure(measure);
            depMeasId = signHelper.getDependentMeasure();
            signEvaluationMethod = signHelper.getEvaluationMethod();
            if (measType.equalsIgnoreCase("Standard")) {
                if (signEvaluationMethod.equalsIgnoreCase("RANK_PRIOR")) {
                    if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) > 0) {
                        return "positive";
                    } else if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) < 0) {
                        return "negative";
                    } else {
                        return null;
                    }
                } else if (signEvaluationMethod.equalsIgnoreCase("Prior")) {
                    if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) < 0) {
                        return "positive";
                    } else if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) > 0) {
                        return "negative";
                    } else {
                        return null;
                    }
                } else if (signEvaluationMethod.equalsIgnoreCase("Change") || signEvaluationMethod.equalsIgnoreCase("Change%")) {
                    if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(bd) > 0) {
                        return "positive";
                    } else if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(bd) < 0) {
                        return "negative";
                    } else {
                        return null;
                    }
                } else if (signEvaluationMethod.equalsIgnoreCase("RTCompare") ) {
                    if (this.getMeasureDataForComputationRT(actualRow, depMeasId).compareTo(this.getMeasureDataForComputationRT(actualRow, measure)) < 0) {
                        return "positive";
                    } else if (this.getMeasureDataForComputationRT(actualRow, depMeasId).compareTo(this.getMeasureDataForComputationRT(actualRow, measure)) > 0) {
                        return "negative";
                    } else {
                        return null;
                }
                }
            } else if (signEvaluationMethod.equalsIgnoreCase("RANK_PRIOR")) {
                if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) < 0) {
                    return "positive";
                } else if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) > 0) {
                    return "negative";
                } else {
                    return null;
                }
            } else if (signEvaluationMethod.equalsIgnoreCase("Prior")) {
                if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) > 0) {
                    return "positive";
                } else if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(this.getMeasureDataForComputation(actualRow, measure)) < 0) {
                    return "negative";
                } else {
                    return null;
                }

            } else if (signEvaluationMethod.equalsIgnoreCase("Change") || signEvaluationMethod.equalsIgnoreCase("Change%")) {
                if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(bd) < 0) {
                    return "positive";
                } else if (this.getMeasureDataForComputation(actualRow, depMeasId).compareTo(bd) > 0) {
                    return "negative";
                } else {
                    return null;
                }

            }

        }

        return "";
    }

    public ArrayList<String> getReportMeasureNames() {
        return container.getReportMeasureNames();
    }

    public ArrayList<BigDecimal> retrieveMeasureData(String measId) {
        return qryData.retrieveNumericData(measId);
    }
    public ArrayList<BigDecimal> retrieveMeasureDatarank(String measId,Container container) {
        return qryData.retrieveNumericDatarank(measId,container);
    }

    public ArrayList<BigDecimal> retrieveNumericDataForMultiTime(String measeId, String groupId, String aggType) {
        return qryData.retrieveNumericDataForMultiTime(measeId, groupId, aggType);
    }

    public Object[][] retrieveDataBasedOnViewSequence(ArrayList<String> columns) {
        return qryData.retrieveDataBasedOnViewSeq(columns);
    }

    public ArrayList<BigDecimal> retrieveDataBasedOnViewSeq(String measEleId) {
        return qryData.retrieveDataBasedOnViewSeq(measEleId);
    }

    public ArrayList<Integer> getViewSequence() {
        return qryData.getViewSequence();
    }

    public boolean isDynamicRowsDisplayedInCrosstab() {
        if (container.isReportCrosstab()) {
            return container.isDynamicRowsDisplayedInCrosstab();
        } else {
            return false;
        }
    }

    public boolean isCrosstabHeaderWrapped() {
        return container.isCrosstabHeaderWrapped();
    }

    public boolean isExcelDisplay() {
        if (ContainerConstants.EXCEL_DISPLAY.equalsIgnoreCase(container.getTableDisplayMode())) {
            return true;
        } else {
            return false;
        }
    }

    public HashSet<String> getMenuPrivileges() {

        HashSet<String> privileges = new HashSet<String>();

        String menuPrivs[] = TableMenuFunctions.COMPCODES;
        for (String priv : menuPrivs) {
            if (userId != null && !userId.equalsIgnoreCase("")) {
                if (PrivilegeManager.isComponentEnabledForUser("REPORT", priv, Integer.parseInt(userId))) {
                    privileges.add(priv);
                }
            } else {
                privileges.add(priv);
            }
        }
        return privileges;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static DataFacade getFacade(Container container) {
        DataFacade facade = new DataFacade(container);
        return facade;
    }

    public int getActualRowCount() {
        return qryData.getRowCount();
    }

    public boolean isColumnPropertyVisible(String column, String type) {
        if (!isReportCrosstab()) {
            if (container.getColumnProperties().get(column) == null) {
                return true;
            } else {
                ArrayList<String> singleColumnPropsLst = new ArrayList<String>();
                if (column.contains("_percentwise") || column.contains("_pwst") || column.contains("_rank") || column.contains("_PYtdrank")|| column.contains("_PQtdrank")|| column.contains("_PMtdrank")|| column.contains("_Qtdrank")|| column.contains("_Ytdrank")|| column.contains("_wf") || column.contains("_wtrg") || column.contains("_rt") || column.contains("_excel") || column.contains("_excel_target") || column.contains("_deviation_mean") || column.contains("_gl") || column.contains("_userGl") || column.contains("_timeBased") || column.contains("_changedPer") || column.contains("_glPer")) {
                    singleColumnPropsLst = (ArrayList<String>) container.getColumnProperties().get(column.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_PYtdrank", "").replace("_PQtdrank", "").replace("_PMtdrank", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_rankST", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", ""));
                    if (singleColumnPropsLst == null) {
                        singleColumnPropsLst = (ArrayList<String>) container.getColumnProperties().get(column);
                    }
                } else {
                    singleColumnPropsLst = (ArrayList<String>) container.getColumnProperties().get(column);
                }
                if (type.equals("GT")) {
                    return isVisible(singleColumnPropsLst.get(0));
                } else if (type.equals("ST") || type.equals("CATST")) {
                    return isVisible(singleColumnPropsLst.get(1));
                } else if (type.equals("AVG")) {
                    return isVisible(singleColumnPropsLst.get(2));
                } else if (type.equals("OVEMAX")) {
                    return isVisible(singleColumnPropsLst.get(3));
                } else if (type.equals("OVEMIN")) {
                    return isVisible(singleColumnPropsLst.get(4));
                } else if (type.equals("CATMAX")) {
                    return isVisible(singleColumnPropsLst.get(5));
                } else if (type.equals("CATMIN")) {
                    return isVisible(singleColumnPropsLst.get(6));
                } else if (type.equals("ROWCNT")) {
                    return isVisible(singleColumnPropsLst.get(7));
                } else if (type.equals("CATAVG")) {
                    return isVisible(singleColumnPropsLst.get(8));
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    private boolean isVisible(String value) {

        if ("Y".equalsIgnoreCase(value)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isIsMeasureGroupEnable() {
        return container.isMeasureGroupEnable();
    }

    public boolean isGroupedMeasure(String measuID) {
        return container.isGroupedMeasure(measuID);
    }

    String getMeasureGrpName(String measuID) {
        return container.getMeasureGrpName(measuID);
    }

    public int getMesGrpColumnSpan(String measuID) {
        return container.getMesGrpColumnSpan(measuID);
    }

    public String getMeasureName(String measuID) {
        return container.getGrpMeasureName(measuID);
    }

    Boolean isFormulaMeasure(String measureId) {
        return container.isFormulaMeasure(measureId);
    }

    public void addRowMapping(int displayRow, int actualRow) {
        if (qryData instanceof POIDataSet) {
            ((POIDataSet) qryData).addRowMapping(displayRow, actualRow);
        }
    }

    public boolean isRTExcelColsAvailable() {
        List<String> excelCols = container.getRtExcelColumns();
        if (excelCols != null && !excelCols.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isTargetEntryApplicable() {
        return container.isTargetEntryApplicable();
    }

    public String getformula(int actualRow, String element) {
        if (RTMeasureElement.isRunTimeExcelColumn(element)) {
            if (qryData instanceof POIDataSet) {
                return ((POIDataSet) qryData).getFormula(actualRow, element);
            }
        }
        return null;
    }

    String getDisplayFormula(String formula) {
        if (qryData instanceof POIDataSet) {
            return ((POIDataSet) qryData).changeToDisplayFormula(formula);
        }
        return formula;
    }

    public void setSubTotRow(String measId, Integer row) {
        container.setSubTotRows(measId, row);
    }

    public boolean isIndicatorEnabled(String element) {
        boolean indicatorEnabled = container.isIndicatorEnabled(element);
        return indicatorEnabled;
    }

    public String getIndicatorImage(int actualRow, String element) {
        BigDecimal maxVal = qryData.getColumnMaximumValue(element);
        BigDecimal minVal = qryData.getColumnMinimumValue(element);

        BigDecimal measVal = qryData.getFieldValueBigDecimal(actualRow, element);
        double value = 0;
        if (measVal != null) {
            value = measVal.doubleValue();
        }

        String formattedValue = NumberFormatter.getModifiedNumber(value, "", -1);
        String lightIcon = "";
        String measure1 = container.getmeasureType(element);
        if (measure1.equalsIgnoreCase("Standard")) {
            if (value < 0) {
                lightIcon = "<img src='../icons pinvoke/status-busy.png' title='" + formattedValue + "'></img>";
            } else if (value > 0) {
                lightIcon = "<img src='../icons pinvoke/status.png' title='" + formattedValue + "'></img>";
            } else {
                lightIcon = "<img src='../icons pinvoke/status-offline.png' title='" + formattedValue + "'></img>";
            }

            return lightIcon;
        } else if (value > 0) {
            lightIcon = "<img src='../icons pinvoke/status-busy.png' title='" + formattedValue + "'></img>";
        } else if (value < 0) {
            lightIcon = "<img src='../icons pinvoke/status.png' title='" + formattedValue + "'></img>";
        } else {
            lightIcon = "<img src='../icons pinvoke/status-offline.png' title='" + formattedValue + "'></img>";
        }

        return lightIcon;

    }

    public String getAggregationForSubtotal(String measId) {

        if (container.getReportCollect().reportQryElementIds.indexOf(measId.replace("A_", "")) != -1) {
            return container.getReportCollect().reportQryAggregations.get(container.getReportCollect().reportQryElementIds.indexOf(measId.replace("A_", "")));
        } else {
            return "";
        }
    }

    public boolean isTransposed() {
        return container.isTransposed();
    }

    public HashMap<String, ArrayList<String>> getTransposeFormatMap() {
        return container.getReportCollect().getTransposeFormatMap();
    }

    public String getNumberSymbol(String elementId) {
        return container.getNumberSymbol(elementId);
    }

    public String getModifiedNumberFormatSymbol(String nbrSymbol) {
        return NumberFormatter.getModifiedNumberFormatSymbol(nbrSymbol);
    }

    public List<String> getGoalSeekData(String measEleId) {
        container.getRetObj().resetViewSequence();
        return container.getReportCollect().getGoalseek().get(measEleId);
    }

    public List<String> getpercentValues(String measEleId) {
        container.getRetObj().resetViewSequence();
        return container.getReportCollect().getPercentColValues().get(measEleId);
    }

    public List<String> getPercent(String measEleId) {
        return container.getReportCollect().getGoalandPercent().get(measEleId);
    }

    public List<String> getGoalValue(String measEleId) {
        return container.getReportCollect().getGoalandPercent().get(measEleId);
    }

    public boolean isRowCountRequired() {
        return container.isRowCountReq();
    }

    public boolean getDesign() {
        return container.getReportCollect().isDesign();
    }

    public int columnCount() {
        return container.getReportCollect().reportColViewbyValues.size();
    }

    public List<String> getTimeMeasurValues(String measEleId) {
        container.getRetObj().resetViewSequence();
        return container.getReportCollect().getMeasurValues().get(measEleId);
    }

    public List<String> getTimePercentValues(String measEleId) {
        container.getRetObj().resetViewSequence();
        return container.getReportCollect().getTimeBaseInvidualper().get(measEleId);
    }

    public ArrayList<Integer> getViewSequenceNumbers() {
        container.getRetObj().resetViewSequence();
        return container.getRetObj().getViewSequence();
    }

    public Boolean isScriptColorEnable(String elementId) {
        Boolean isscriptcolorenable = false;
        String val = container.getscriptIndicator(elementId);
        if (val != null && val.equalsIgnoreCase("Y")) {
            isscriptcolorenable = true;
        }
        return isscriptcolorenable;
    }

    public String getFontColorForscript(double celldata, String measureType, String element) {
        String depMeasId = "";
        String signEvaluationMethod = "";
        ColorHelper signHelper = container.getSignForMeasure(element);
        depMeasId = signHelper.getDependentMeasure();
        signEvaluationMethod = signHelper.getEvaluationMethod();

        String meastype = "";
        String color = "";
        Double val = celldata;
        meastype = measureType;
        if (meastype != null && !meastype.equalsIgnoreCase("")) {
            if (meastype.equalsIgnoreCase("Standard")) {
                if (val > 0) {
                    color = "Green";
                } else if (val < 0) {
                    color = "Red";
                } else if (val == -0.0) {
                    color = "Red";
                } else {
                    color = "Black";
                }
            } else if (val > 0) {
                color = "Red";
            } else if (val < 0) {
                color = "Green";
            } else if (val == -0.0) {
                color = "Green";
            } else {
                color = "Black";
            }
        } else if (val > 0) {
            color = "Green";
        } else if (val < 0) {
            color = "Red";
        } else if (val == -0.0) {
            color = "Red";
        } else {
            color = "Black";
        }

        return color;
    }
  //added by sruthi for viewbytablecolumnproperties
   public String getViewbyAlign(int actualRow, String element){
          String align = "";
          align = container.getviewbyAlignment(element);
          return  align;
     }
   public String getViewbydataAlign(int actualRow, String element){
          String align = "";
          align = container.getViewbydataAlignments(element);
          return  align;
     }
   public String getViewbyAlignment(String element){
       return container.getviewbyAlignment(element);
     }
   public String getViewbydataAlignment(String element){
          return container.getViewbydataAlignments(element);
     } //ended by sruthi
    public String getTextAlign(int actualRow, String element) {
        String align = "";
        align = container.getTextAlign(element);
        return align;
    }

    public String getmeasureType(String element) {
        String measureType = "";
        measureType = container.getmeasureType(element);
        return measureType;
    }

    public String getmesureAlign(String element) {
        String measurealign = "";
        measurealign = container.getMeasureAlign(element);
        return measurealign;
    }

//added by sruthi for header font color
    public String getmesureColor(String element) {
        String measureacolor = "";
        measureacolor = container.getMeasureColor(element);
        return measureacolor;
    }
//ended by sruthi
//added by sruthi for text font color

    public String getTextScriptColor(String element) {
        String color = "";
        color = container.getTextColor(element);
        return color;
    }
//ended by sruthi

    public boolean getDrillMeasure() {
        return container.isMeasDrill();
    }
    //added by sruthi for background color in tablecolumn pro
 public String getmesurebgColor(String element) {
        String measurebgcolor = "";
        measurebgcolor = container.getMeasureBgColor(element);
        return measurebgcolor;
    }//ended by sruthi
    public String getMeasureId(int calval) {
        ArrayList<String> keys = new ArrayList<String>();
        Set set = container.getReportCollect().getNonViewByMap().keySet();
//           Iterator itr = set.iterator();
//            while(itr.hasNext()){
//                keys.add((String) itr.next());
//            }
        keys.addAll(set);
        //PbReturnObject dataset=(PbReturnObject) container.getRetObj();
        return keys.get(calval);
    }

    public ArrayList getReportParameters() {
        ArrayList<String> Parameters = new ArrayList<String>();
        if (container.getParametersHashMap().get("Parameters") != null) {
            Parameters.addAll((ArrayList) container.getParametersHashMap().get("Parameters"));
            Parameters.add("TIME");
            if (!this.isReportCrosstab()) {
                Parameters.add("Day");
                Parameters.add("Week");
                Parameters.add("Month");
                Parameters.add("Qtr");
                Parameters.add("Year");
                Parameters.add("Time Drill");
            }
            return Parameters;
            //return (ArrayList) container.getParametersHashMap().get("Parameters");
        } else {
            return null;
        }
    }

    public ArrayList getReportParameterNames() {
        ArrayList<String> ParametersNames = new ArrayList<String>();
        if (container.getParametersHashMap().get("ParametersNames") != null) {
            ParametersNames.addAll((ArrayList) container.getParametersHashMap().get("ParametersNames"));
            ParametersNames.add("Time");
            if (!this.isReportCrosstab()) {
                ParametersNames.add("Daily");
                ParametersNames.add("Weekly");
                ParametersNames.add("Monthly");
                ParametersNames.add("Quarterly");
                ParametersNames.add("Yearly");
                ParametersNames.add("Time Drill");
            }
            return ParametersNames;
            // return (ArrayList) container.getParametersHashMap().get("ParametersNames");
        } else {
            return null;
        }
    }

    public String getViewbyId() {
        String viewById = null;
        // 
        if (!container.getReportCollect().getReportViewByMain().keySet().isEmpty() && container.getReportCollect().getReportViewByMain().keySet() != null) {
            Set<String> viewByIdSet = container.getReportCollect().reportViewByMain.keySet();
            ArrayList<String> viewByValues = container.getReportCollect().reportRowViewbyValues;
            for (String viewByIdKey : viewByIdSet) {
                //  
                String elementIdForViewId = container.getReportCollect().reportViewByMain.get(viewByIdKey).get(2);
                //if reportIncomingParameters is null then take default value
                if (elementIdForViewId == null) {
                    elementIdForViewId = container.getReportCollect().reportViewByMain.get(viewByIdKey).get(1);
                }
                viewById = viewByIdKey;
                if (viewByValues.get(0).equalsIgnoreCase(elementIdForViewId)) {
                    break;
                }
            }
        }
        return viewById;
    }

    public String getadhocParamUrl() {
        String viewById = null;
        //HashMap<String,String> map=new HashMap<String, String>();
        StringBuffer sb = new StringBuffer();
        if (!container.getReportCollect().getReportViewByMain().keySet().isEmpty() && container.getReportCollect().getReportViewByMain().keySet() != null) {
            Set<String> viewByIdSet = container.getReportCollect().reportViewByMain.keySet();
            ArrayList<String> viewByValues = container.getReportCollect().reportRowViewbyValues;
            ArrayList<String> colviewByValues = container.getReportCollect().reportColViewbyValues;
            ArrayList<String> viewByIdSetclone = new ArrayList<String>();
            for (String viewByIdKey : viewByIdSet) {
                viewByIdSetclone.add(viewByIdKey);
            }

            for (String viewByIdKey : viewByIdSet) {
                //ss 
                String elementIdForViewId = container.getReportCollect().reportViewByMain.get(viewByIdKey).get(2);
                if (elementIdForViewId == null) {
                    elementIdForViewId = container.getReportCollect().reportViewByMain.get(viewByIdKey).get(1);
                }
                viewById = viewByIdKey;
                if (viewByValues.contains(elementIdForViewId)) {
                    sb.append("&CBOVIEW_BY" + viewById + "=" + elementIdForViewId);
                } else if (colviewByValues.contains(elementIdForViewId)) {
                    sb.append("&CBOVIEW_BY" + viewById + "=" + elementIdForViewId);
                } else if (viewByIdSetclone != null && viewByValues.size() > viewByIdSetclone.indexOf(viewByIdKey)) {
                    sb.append("&CBOVIEW_BY" + viewById + "=" + viewByValues.get(viewByIdSetclone.indexOf(viewByIdKey)));
                }

                //map.put(viewById,elementIdForViewId);
            }

        }
        if (sb != null) {
            return sb.toString();
        } else {
            return null;
        }
    }

    public String getAdhocDrillType() {
        return container.getAdhocDrillType();
    }

    public boolean isTreeTableDisplay() {
        return container.isTreeTableDisplay();
    }

    public boolean isParameterDrill() {
        return container.isParameterDrill();
    }

    public LinkedHashMap<String, String> getReportParameterValues() {
        return container.getReportCollect().reportParametersValues;
    }

    public HashMap<String, String> getDrillMap() {
        return container.getReportCollect().getDrillMap();
    }

    public HashMap getrepLinks() {
        return container.getRepLinks();
    }

    public boolean isdrillvalues() {
        return container.isdrillvalues();
    }

    public Boolean istimeConversionEnable(String elementId) {
        Boolean istimeConversionenable = false;
        String val = container.gettimeConversion(elementId);
        if (val != null && val.equalsIgnoreCase("Y")) {
            istimeConversionenable = true;
        }
        return istimeConversionenable;
    }

    public String convertDataToTime(BigDecimal measData) {

        String time = "";
        BigDecimal hours = new BigDecimal("0");
        BigDecimal myremainder = new BigDecimal("0");
        BigDecimal minutes = new BigDecimal("0");
        BigDecimal seconds = new BigDecimal("0");
        BigDecimal var3600 = new BigDecimal("3600");
        BigDecimal var60 = new BigDecimal("60");

        hours = measData.divide(var3600, BigDecimal.ROUND_FLOOR);
        BigDecimal truncatedhrs = hours.setScale(0, BigDecimal.ROUND_DOWN);
        myremainder = measData.remainder(var3600);
        minutes = myremainder.divide(var60, BigDecimal.ROUND_FLOOR);
        BigDecimal truncatedmins = minutes.setScale(0, BigDecimal.ROUND_DOWN);
        seconds = myremainder.remainder(var60);
        BigDecimal truncatedsec = seconds.setScale(0, BigDecimal.ROUND_DOWN);
        DecimalFormat formatter = new DecimalFormat("##");
        time = formatter.format(truncatedhrs) + ":" + formatter.format(truncatedmins) + ":" + formatter.format(truncatedsec);
        return time;
    }

    public String getDateSubStringValues(String elementId) {
        String dateValues = "";
        dateValues = container.getDateSubStringValues(elementId);
        return dateValues;
    }

    public String getDateandTimeOptions(String elementId) {

        String dateOptions = null;
        dateOptions = container.getDateandTimeOptions(elementId);
        return dateOptions;
    }

    public BigDecimal getKPIColumnGrandTotalValue(String columnId) {

        if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(columnId)) {
            return new BigDecimal(container.getKpiRetObj().getFieldValueString(0, "A_" + columnId));

        } else {
            return qryData.getColumnGrandTotalValue(columnId);
        }

    }

    public String getDateFormatt(String elementId) {
        String dateFormat = "";
        dateFormat = container.getDateFormatt(elementId);
        return dateFormat;
    }

    public boolean isCatAvgRequired() {
        return container.getCatAvgTotalReq();
    }

    public BigDecimal getrowcount() {
        return new BigDecimal(qryData.getRowCount());
    }

    public boolean isSerialNumDisplay() {
        return container.isSerialNumDisplay();
    }

    public String getMeasureDrillType() {
        return container.getMeasureDrillType();
    }

    public int getSubTotalTopBottomCount() {
        return container.getSubTotalTopBottomCount();
    }

    public boolean isOverAllAvgRequired() {
        return container.getAvgTotalReq();
    }

    public boolean isCustomTotalRequired() {
        return container.isCustomTotEnabled();
    }

    public String getcustomTotalName() {
        return container.getCustTotName();
    }

    public String getmappedTo() {
        return container.getMappedTo();
    }

    public boolean isRenameTotal() {
        return container.isRenameTotal();
    }

    public String getoriginalTotalName() {
        return container.getoriginalTotalName();
    }

    public String getRenamedTotalName() {
        return container.getRenamedTotalName();
    }

    public HashMap<String, String> getRenameDetails() {
        return container.getRenameDetails();
    }

    public void increseColCount(int count) {
        container.increaseColCount(count);
    }

    public int getIncreaseColCount() {
        return container.getRunTimeColCount();
    }

    public void setrunTimeColCount(int runtimeColCount) {
        container.setrunTimeColCount(runtimeColCount);
    }

    public Object getAggregationType(String elemId) {
        return container.getAggregationType(elemId);
    }

    public String getuserTypeAdmin() {
        return container.getuserTypeAdmin();
    }

    public String getSymbol(String elementId) {
        return container.getSymbol(elementId);
    }

    public String getAlignment(String elementId) {
        return container.getAlignment(elementId);
    }

    public String getFontcolor(String elementId) {
        return container.getFontcolor(elementId);
    }

    public String getBGcolor(String elementId) {
        return container.getBGcolor(elementId);
    }

    public String getNegative_val(String elementId) {
        return container.getNegative_val(elementId);
    }

    public String getNo_format(String elementId) {
        return container.getNo_format(elementId);
    }

    public String getRound(String elementId) {
        return container.getRound(elementId);
    }

    public boolean isLockdataset(String elementId) {
        return container.isLockdataset(elementId);
    }

    public boolean isTopBottomWithOthersEnable() {
        if (container.getTopBottomMode() != null && container.getTopBottomMode().equalsIgnoreCase("TopRowsWithOthers")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param refreshconatinerMap the refreshconatinerMap to set
     */
    public void setRefreshconatinerMap(String viewbyId, Container container) {
//        
        String filepath = null;
        String filename = null;
        Container tempcontainer = new Container();
        filepath = System.getProperty("java.io.tmpdir");
        filename = "ContainerDetails" + container.getReportId() + "_" + viewbyId + ".txt";
        String action = "";
        PbReportViewerBD viewerBd = new PbReportViewerBD();
        try {
            tempcontainer.writeFileDetails(filepath, filename, container);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        try {
            tempcontainer = (Container) tempcontainer.readFileDetails(filepath, filename);
            PbReportCollection collect = tempcontainer.getReportCollect();
            collect.reportRowViewbyValues.clear();
            collect.reportRowViewbyValues.add(viewbyId);
            tempcontainer.setReportCollect(collect);
            viewerBd.prepareReport(action, tempcontainer, tempcontainer.getReportId(), userId, new HashMap());
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        this.refreshconatinerMap.put(viewbyId, tempcontainer);
    }

    public void CheckCurrentAndPriorMsrs() {
        ArrayList<String> Measures = container.getReportCollect().reportQryElementIds;
        for (int i = 0; i < Measures.size(); i++) {
            //modified by anitha
            boolean flag = checkforChangepercentMsr(Measures.get(i).toString().replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                    .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_PYMTD", "").replace("_PYQTD", "")
                    .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", ""));
            if (flag) {
                ArrayList<String> list = new ArrayList<String>();
                list = getCuurentAndPriorElementIDs(Measures.get(i).toString());
                if (list != null && !list.isEmpty()) {
                    HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                    if (Measures.containsAll(list)) {
                        map.put(Measures.get(i).toString(), list);
                        container.setCurrAndPriorValOfMsrMap(map);
                    }

                }
            }
        }
    }

    public boolean checkforChangepercentMsr(String elementid) {
        boolean flag = false;
        String query = "select REF_ELEMENT_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementid;
        PbDb pbdb = new PbDb();
        PbReturnObject retobj;
        try {
            retobj = pbdb.execSelectSQL(query);
            if (retobj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4")) {
                flag = true;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return flag;
    }

    public ArrayList<String> getCuurentAndPriorElementIDs(String elementId) {
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + elementId + ") order by ref_element_type ";
        PbDb pbdb = new PbDb();
        ArrayList<String> elementIdlist = new ArrayList<String>();
        try {
            PbReturnObject retobj = pbdb.execSelectSQL(query);
            if (retobj != null && retobj.getRowCount() > 0) {
                for (int j = 0; j < retobj.getRowCount(); j++) {
                    if (retobj.getFieldValueString(j, 1).equalsIgnoreCase("1") || retobj.getFieldValueString(j, 1).equalsIgnoreCase("2")) {
                        elementIdlist.add(retobj.getFieldValueString(j, 0));
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return elementIdlist;
    }

    /**
     * @return the isMsrbasedBusTemplate
     */
    public boolean isIsMsrbasedBusTemplate() {
        return isMsrbasedBusTemplate;
    }

    /**
     * @param isMsrbasedBusTemplate the isMsrbasedBusTemplate to set
     */
    public void setIsMsrbasedBusTemplate(boolean isMsrbasedBusTemplate) {
        this.isMsrbasedBusTemplate = isMsrbasedBusTemplate;
    }
    //start of code by Nazneen for sub total deviation

    public Boolean isSubTotalDeviationEnable(String elementId) {
        Boolean isSubTotalDeviationEnable = false;
        String val = container.getSubTotalDeviation(elementId);
        if (val != null && val.equalsIgnoreCase("Y")) {
            isSubTotalDeviationEnable = true;
        }
        return isSubTotalDeviationEnable;
    }

    public int getGTValueOfZeroCount(String colName) {
        try {
            if (RTMeasureElement.isRunTimeMeasure(colName)) {
                return 0;
            } else {
                return (Integer) this.qryData.columnGrandTotalsZeroCount.get(colName);
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @return the actualrow
     */
    public int getActualrow() {
        return actualrow;
    }

    /**
     * @param actualrow the actualrow to set
     */
    public void setActualrow(int actualrow) {
        this.actualrow = actualrow;
    }

    /**
     * @return the colorcodeMap
     */
    public HashMap<String, HashMap<Double, String>> getColorcodeMap() {
        return colorcodeMap;
    }

    /**
     * @param colorcodeMap the colorcodeMap to set
     */
    public void setColorcodeMap(HashMap<String, HashMap<Double, String>> colorcodeMap) {
        this.colorcodeMap = colorcodeMap;
    }

    public double getValue(int rowNum, String element) {

        double value = Double.parseDouble(qryData.getFieldValueString(rowNum, element));
        return value;
    }

    /**
     * @return the Measures
     */
    public ArrayList<String> getMeasures() {
        return Measures;
    }

    /**
     * @param Measures the Measures to set
     */
    public void setMeasures(ArrayList<String> Measures) {
        this.Measures = Measures;
    }

    public boolean isIsSubtotalTopBottom() {
        return container.isIsSubtotalTopBottom();
    }

    public boolean getIsSubToTalSearchFilterApplied() {
        return container.getIsSubToTalSearchFilterApplied();
    }

    public ArrayList<String> getSubTotalSrchColumns() {
        return container.getSubTotalSrchColumns();
    }

    /**
     * @return the subTotalSrchCondition
     */
    public ArrayList<String> getSubTotalSrchCondition() {
        return container.getSubTotalSrchCondition();
    }

    /**
     * @return the subTotalSrchValue
     */
    public ArrayList<String> getSubTotalSrchValue() {
        return container.getSubTotalSrchValue();
    }

    public int getRowForSTRank() {
        return rowForSTRank;
    }

    public void setRowForSTRank(int rowForSTRank) {
        this.rowForSTRank = rowForSTRank;
    }

    /**
     * @return the noOfSubTotalsSubmitted
     */
    public int getNoOfSubTotalsSubmitted() {
        return noOfSubTotalsSubmitted;
    }

    /**
     * @param noOfSubTotalsSubmitted the noOfSubTotalsSubmitted to set
     */
    public void setNoOfSubTotalsSubmitted(int noOfSubTotalsSubmitted) {
        this.noOfSubTotalsSubmitted = noOfSubTotalsSubmitted;
    }

    /**
     * @return the rowForRTTimeDimension
     */
    public int getRowForRTTimeDimension() {
        return rowForRTTimeDimension;
}

    /**
     * @param rowForRTTimeDimension the rowForRTTimeDimension to set
     */
    public void setRowForRTTimeDimension(int rowForRTTimeDimension) {
        this.rowForRTTimeDimension = rowForRTTimeDimension;
    }
    
    public BigDecimal getMeasureDataForComputationRT(int row, String columnName) {
        BigDecimal measData = this.getMeasureDataRT(row, columnName);
        if (measData == null) {
            measData = BigDecimal.ZERO;
}
        return measData;
    }
    
    public BigDecimal getMeasureDataRT(int row, String columnName) {
        BigDecimal numericData = null;
        if (RTMeasureElement.isRunTimeMeasure(columnName)) {
                String formattedData = "";                
                Object objData = qryData.getFieldValue(row, columnName);
                if (objData != null) {
                    if ("".equalsIgnoreCase(objData.toString())) {                            
                            formattedData = objData.toString();
                        }else{
                        formattedData = objData.toString();
                        int column = container.getDisplayColumns().indexOf(columnName);
                        numericData = new BigDecimal(formattedData.replaceAll(",", ""));                                              
                    }
                }                                                                           
        } else {
            numericData = qryData.getFieldValueBigDecimal(row, columnName);
        }

        return numericData;
    }
}
    
