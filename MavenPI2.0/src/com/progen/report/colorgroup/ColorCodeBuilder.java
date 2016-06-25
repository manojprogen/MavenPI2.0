/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.colorgroup;

import com.progen.db.ProgenDataSet;
import com.progen.report.ReportParameter;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import prg.db.Container;
import utils.db.ProgenConnection;
import org.apache.log4j.*;

/**
 *
 * @author Administrator
 */
public class ColorCodeBuilder {
    public static Logger logger=Logger.getLogger(ColorCodeBuilder.class);
//modified by Dinanath for color grouping
    public ColorGroup buildColorCode(String reportId, Container container) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        int repID = Integer.parseInt(reportId);
        String colorQry = "SELECT clr.MEASURE_ID,  clr.ROW_ELEMENTS,  clr.COL_ELEMENTS,  clr.PARAMETER,  clr.COLOR_CODE,  clr.CROSSTABMEASURE_ID,  clr.GRADIENT_BASED, clr.ISAVGBASED, clr.ISMINMAXBASED,clr.ispercentbased, mstr.viewby_values,mstr.ELEMT_IDS FROM PRG_AR_REPORT_COLORS clr,  prg_ar_report_master mstr WHERE mstr.Report_id=clr.report_id AND mstr.report_id  =" + repID;
        LinkedHashMap paramHM = new LinkedHashMap();
        ColorGroup colGroup = container.getColorGroup();
        int noOfDays=container.getNoOfDays();//added by Dinanath
        boolean rowWiseFlag = false;
        ColorCode colorCode;
        String measureID;
        ArrayList rowElements;
        ArrayList colElements;
        String crossTabMes = "";
        ArrayList<ColorCodeRule> colorCodeRulesLst = null;
        ReportParameter repParam;
        List<String> mes = new ArrayList<String>();
        try {
            pbro = pbdb.execSelectSQL(colorQry);
            String colNames[] = pbro.getColumnNames();
            //modified by Dinanath for colorgrouping
            if (pbro != null && pbro.getRowCount() > 0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    repParam = new ReportParameter();
                    measureID = pbro.getFieldValueString(i, "MEASURE_ID");
                    rowElements = this.getRowViewBys(pbro.getFieldValueString(i, "ROW_ELEMENTS"));
                    colElements = this.getColViewBys(pbro.getFieldValueString(i, "COL_ELEMENTS"));
                    String ViewbyColumn = colGroup.getViewByColumnValues();
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        //changed by Dinanath
                        paramHM = this.parseReportParamXML(pbro.getFieldValueString(i, "PARAMETER"));
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        //changed by dinanath
                        paramHM = this.parseReportParamXML(pbro.getFieldValueClobString(i, "PARAMETER"));
                    } else {
                        paramHM = this.parseReportParamXML(pbro.getFieldValueClobString(i, "PARAMETER"));
                    }
                    String isAvgBased = pbro.getFieldValueString(i, 7);
                    String isMinMaxBased = pbro.getFieldValueString(i, 8);
                    String isPercentBased = pbro.getFieldValueString(i, "ispercentbased");
                    //modified by Dinanath for converting xml code
                    boolean fromOpen=true;
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        colorCodeRulesLst = this.parseColorCodeXML(pbro.getFieldValueString(i, "COLOR_CODE"), ViewbyColumn, isAvgBased, isMinMaxBased,noOfDays,fromOpen,isPercentBased);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        colorCodeRulesLst = this.parseColorCodeXML(pbro.getFieldValueClobString(i, "COLOR_CODE"), ViewbyColumn, isAvgBased, isMinMaxBased,noOfDays,fromOpen,isPercentBased);
                    } else {
                        colorCodeRulesLst = this.parseColorCodeXML(pbro.getFieldValueClobString(i, "COLOR_CODE"), ViewbyColumn, isAvgBased, isMinMaxBased,noOfDays,fromOpen,isPercentBased);
                    }
                    String Rowviewby = colorCodeRulesLst.get(0).viewBy;
                    repParam.setReportParametersSV(paramHM);
                    repParam.setViewBys(rowElements, colElements);
                    crossTabMes = pbro.getFieldValueString(i, "CROSSTABMEASURE_ID");

                    colorCode = colGroup.createColorCode(measureID, repParam, crossTabMes, Rowviewby.trim());

                    if (isAvgBased.equalsIgnoreCase("true")) {
                        colorCode.setIsAvgBased(true);
                        colGroup.setIsAvgBased(true);

                    }
                    if (isPercentBased.equalsIgnoreCase("true")) {
                        colorCode.setIsPercentBased(true);
                        colGroup.setIsPercentBased(false);

                    }
                    if (isMinMaxBased.equalsIgnoreCase("true")) {
                        colorCode.setMinMaxBased(true);
                        colGroup.setMinMaxBased(true);

                    }
                    //modified  by Dinanath for color group
                    int index = colorCodeRulesLst.indexOf(colorCode);
                    colorCode.setViewByColumnValues(colorCodeRulesLst.get(0).viewBy);
                    colGroup.setViewByColumnValues(Rowviewby);
                    if (pbro.getFieldValueString(i, "GRADIENT_BASED").equalsIgnoreCase("Y")) {
                        colorCode.setGradientBased(true);
                    } else {
                        colorCode.setGradientBased(false);
                    }
                    colorCode.setColorCodeRule(colorCodeRulesLst);
                    if (container.isReportCrosstab()) {
                        colorCode.setCrosstabMeasure(crossTabMes);
                    } else {
                        crossTabMes = "";
                    }
                    //Code added by Amar
                    String eleVales = pbro.getFieldValueString(i, "ELEMT_IDS");
                    if (eleVales.isEmpty()) {
                        if (!mes.contains(measureID)) {
                            mes.add(measureID);
                        }
                    } else {
                        String elems[] = eleVales.split(",");
                        List list = new ArrayList<String>(Arrays.asList(elems));
                        for (Object val : list) {
                            if (!mes.contains(val)) {
                                mes.add(val.toString());
                            }
                        }
                        //mes.addAll(new ArrayList(Arrays.asList(elems)));
                        if (!mes.contains(measureID)) {
                            mes.add(measureID);
                        }
                    }

                    container.getReportCollect().setGlobalValues(mes);
                    colGroup.setEleId(container.getReportCollect().getGlobalValues());
                    String values = pbro.getFieldValueString(i, "VIEWBY_VALUES");
                    if (!values.equals("")) {
                        String[] valusarr = values.split(",");
                        container.getReportCollect().setViewByValues(Arrays.asList(valusarr));

                        rowWiseFlag = true;
                        colorCode.setRowWiseColor(rowWiseFlag);
                        colGroup.setRowWiseColor(rowWiseFlag);
                        colGroup.setRowViewByValues(container.getReportCollect().getViewByValues());

                        ArrayList<BigDecimal> minandMaxvalues = new ArrayList<BigDecimal>();
                        List<String> viewBys = container.getReportCollect().getViewByValues();
                        ProgenDataSet datset = container.getRetObj();
                        ArrayList<String> viewByValueList = new ArrayList();
                        for (int k = 0; k < datset.getViewSequence().size(); k++) {
                            viewByValueList.add(datset.getFieldValueString(datset.getViewSequence().get(k), container.getDisplayColumns().get(0)));
                        }
                        TreeSet<BigDecimal> rowmaxandMin = new TreeSet<BigDecimal>();
                        for (int p = 0; p < viewByValueList.size(); p++) {
                            if (viewBys.contains(viewByValueList.get(p))) {
                                for (int j = 1; j < container.getDisplayColumns().size(); j++) {
                                    rowmaxandMin.add(datset.getFieldValueBigDecimal(p, j));
                                }
                            }
                        }
                        BigDecimal sum = BigDecimal.ZERO;
                        Iterator itr = rowmaxandMin.iterator();
                        while (itr.hasNext()) {
                            sum = sum.add((BigDecimal) itr.next());
                        }
                        BigDecimal avg = new BigDecimal(Integer.valueOf(sum.intValue()) / (rowmaxandMin.size()));
                        minandMaxvalues.add(rowmaxandMin.first());
                        minandMaxvalues.add(avg);
                        minandMaxvalues.add(rowmaxandMin.last());
                        container.getReportCollect().setMinmaxavgValues(minandMaxvalues);
                    }

                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ",ex);
        }
        return colGroup;
    }

    public ArrayList getRowViewBys(String rowViewBys) {
        ArrayList rowList = new ArrayList();
        String rowViews[] = rowViewBys.split(",");
        for (String row : rowViews) {
            rowList.add(row);
        }
        return rowList;
    }

    public ArrayList<String> getColViewBys(String colViewBys) {
        ArrayList colList = new ArrayList();
        String colViews[] = colViewBys.split(",");
        for (String col : colViews) {
            colList.add(col);
        }
        return colList;
    }

    public LinkedHashMap parseReportParamXML(String paramXml) {
        LinkedHashMap paramHashMap = new LinkedHashMap();
        Document paramDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            paramDocument = builder.build(new ByteArrayInputStream(paramXml.toString().getBytes()));
            root = paramDocument.getRootElement();
            List reportParameters = root.getChildren("ReportParameter");
            for (int i = 0; i < reportParameters.size(); i++) {
                Element repParam = (Element) reportParameters.get(i);
                List elementList = repParam.getChildren("ElementId");
                List valueList = repParam.getChildren("Value");
                Element element = (Element) elementList.get(0);
                Element value = (Element) valueList.get(0);

                paramHashMap.put(element.getText(), value.getText().replace("[", "").replace("]", ""));
            }
        } catch (Exception e) {
            logger.error("Exception: ",e);
        }
        return paramHashMap;
    }

    public ArrayList<ColorCodeRule> parseColorCodeXML(String colorCodeXml, String viewbycolumn, String isAvgBased, String isMinMaxBased,int noOfDays,boolean fromOpen,String isPercentBased) {
        HashMap colorMap = new HashMap();
        Document colorDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            colorDocument = builder.build(new ByteArrayInputStream(colorCodeXml.toString().getBytes()));
            root = colorDocument.getRootElement();
            List colorRules = root.getChildren("ColorCodeRule");
//            String[] colorCodes = new String[colorRules.size()];
//            String[] operators = new String[colorRules.size()];
//            String[] sValues = new String[colorRules.size()];
//            String[] eValues = new String[colorRules.size()];
            ArrayList<String> colorCodesLst = new ArrayList<String>();
            ArrayList<String> operatorsLst = new ArrayList<String>();
            ArrayList<String> stValuesLst = new ArrayList<String>();
            ArrayList<String> endValuesLst = new ArrayList<String>();
            ArrayList<String> ViewbyLst = new ArrayList<String>();
            for (int i = 0; i < colorRules.size(); i++) {
                Element colorEle = (Element) colorRules.get(i);
                List colorCodeList = colorEle.getChildren("ColorCode");
                List operatorList = colorEle.getChildren("Operator");
                List strtValueList = colorEle.getChildren("StrtValue");
                List endValueList = colorEle.getChildren("EndValue");
                List viewByList = colorEle.getChildren("viewBy");
                Element colorCode = (Element) colorCodeList.get(0);
                Element operator = (Element) operatorList.get(0);
                Element startValue = (Element) strtValueList.get(0);
                Element viewBy = (Element) viewByList.get(0);
//                colorCodes[i] = colorCode.getText();
//                operators[i] = operator.getText();
//                sValues[i] = startValue.getText();
                colorCodesLst.add(colorCode.getText());
                operatorsLst.add(operator.getText());
                stValuesLst.add(startValue.getText());
                ViewbyLst.add(viewBy.getText());

                Element endValue = null;
                if (endValueList != null && endValueList.size() != 0) {
                    endValue = (Element) endValueList.get(0);
//                    eValues[i]=endValue.getText();
                    endValuesLst.add(endValue.getText());
                } else {
                    endValuesLst.add(null);
                }
            }
//            colorMap.put("colorCodes", colorCodes);
//            colorMap.put("operators", operators);
//            colorMap.put("sValues", sValues);
//            colorMap.put("eValues", eValues);
//            colorMap.put("eValues", eValues);
            if (isMinMaxBased.equalsIgnoreCase("true")) {
                return ColorCodeBuilder.buildMinMaxColorCode(colorCodesLst, operatorsLst, stValuesLst, endValuesLst, noOfDays, ViewbyLst.get(0).toString());

            } else if (isAvgBased.equalsIgnoreCase("true")) {

                return ColorCodeBuilder.buildAvgColorCode(colorCodesLst, operatorsLst, stValuesLst, endValuesLst, noOfDays, ViewbyLst.get(0).toString());
            } else if (isPercentBased.equalsIgnoreCase("true")) {

                return ColorCodeBuilder.buildPercentColorCode(colorCodesLst, operatorsLst, stValuesLst, endValuesLst, noOfDays, ViewbyLst.get(0).toString());

            } else {
                return ColorCodeBuilder.buildColorCode(colorCodesLst, operatorsLst, stValuesLst, endValuesLst, noOfDays, ViewbyLst.get(0).toString(),fromOpen);
            }
        } catch (Exception e) {
            logger.error("Exception: ",e);
        }
        return null;
        //  return colorMap;
    }

    public static ArrayList<ColorCodeRule> buildColorCode(List<String> colorCodes, List<String> operators, List<String> sValues, List<String> eValues, int noOfDays, String ViewBy,boolean fromOpen) {
        Double[] strtvalues = null;
        Double[] endValues = null;
        ArrayList<ColorCodeRule> colorCodeRuleLst = new ArrayList<ColorCodeRule>();
        // ColorGroup coloGroup=new ColorGroup();
        int i = 0;
        if (sValues != null && sValues.size() > 0) {
            strtvalues = new Double[sValues.size()];
            //modified by Dinanath
            for (String svalue : sValues) {
                if (svalue != null && !"".equalsIgnoreCase(svalue)) {
                    if (noOfDays == 0) {
                        strtvalues[i]=(Double.parseDouble(svalue)); 
                    }else if(noOfDays!=0 && fromOpen==true) {
                        strtvalues[i]=(Double.parseDouble(svalue));
                    }else{
                        strtvalues[i] = (Double.parseDouble(svalue))/noOfDays;
                    }
                } else {
                    strtvalues[i] = (Double.parseDouble(svalue));
                }
                i++;
            }
        }
        endValues = new Double[operators.size()];
        if (eValues != null && eValues.size() > 0) {
            i = 0;
            //modified by Dinanath
            for (String evalue : eValues) {
                if (evalue != null && !"".equalsIgnoreCase(evalue)) {
                    if (noOfDays == 0) {
                        endValues[i] = (Double.parseDouble(evalue));
                    }else if(noOfDays!=0 && fromOpen==true) {
                        endValues[i]=(Double.parseDouble(evalue));
                    } else {
                        endValues[i]=(Double.parseDouble(evalue))/noOfDays;
                    }
                }
                i++;
            }
        }
        if (operators != null && operators.size() > 0) {
            for (int k = 0; k < operators.size(); k++) {
                if (endValues[k] == null) {
                    endValues[k] = 0.0;
                    colorCodeRuleLst.add(buildColorCodeRule(colorCodes.get(k), strtvalues[k], endValues[k], MathOperator.getMathOperator(operators.get(k)), ViewBy));
                } else {
                    colorCodeRuleLst.add(buildColorCodeRule(colorCodes.get(k), strtvalues[k], endValues[k], MathOperator.getMathOperator(operators.get(k)), ViewBy));
                }
            }
        }
        return colorCodeRuleLst;
    }

    public static ArrayList<ColorCodeRule> buildAvgColorCode(List<String> colorCodes, List<String> operators, List<String> sValues, List<String> eValues, int noOfDays, String ViewBy) {
        Double[] strtvalues = null;
        Double[] endValues = null;
        ArrayList<ColorCodeRule> colorCodeRuleLst = new ArrayList<ColorCodeRule>();
        // ColorGroup coloGroup=new ColorGroup();
        int i = 0;
        if (sValues != null && sValues.size() > 0) {
            strtvalues = new Double[sValues.size()];
            for (String svalue : sValues) {
                if (svalue != null && !"".equalsIgnoreCase(svalue)) {

                    strtvalues[i] = (Double.parseDouble(svalue.replaceAll("[^0-9.]", "")));
                }
                i++;
            }
        }
        endValues = new Double[operators.size()];
        if (eValues != null && eValues.size() > 0) {
            i = 0;
            for (String evalue : eValues) {
                if (evalue != null && !"".equalsIgnoreCase(evalue)) {
                    endValues[i] = (Double.parseDouble(evalue.replaceAll("[^0-9.]", "")));
                }
                i++;
            }
        }
        if (operators != null && operators.size() > 0) {
            for (int k = 0; k < operators.size(); k++) {
                if (endValues[k] == null) {
                    endValues[k] = 0.0;
                    colorCodeRuleLst.add(buildColorCodeRule(colorCodes.get(k), strtvalues[k], endValues[k], MathOperator.getMathOperator(operators.get(k)), ViewBy));
                } else {
                    colorCodeRuleLst.add(buildColorCodeRule(colorCodes.get(k), strtvalues[k], endValues[k], MathOperator.getMathOperator(operators.get(k)), ViewBy));
                }
            }
        }
        return colorCodeRuleLst;
    }
    //added by Dinanath for below and above avg
     public static ArrayList<ColorCodeRule> buildPercentColorCode(List<String> colorCodes, List<String> operators, List<String> sValues, List<String> eValues, int noOfDays, String ViewBy) {
        Double[] strtvalues = null;
        Double[] endValues = null;
        ArrayList<ColorCodeRule> colorCodeRuleLst = new ArrayList<ColorCodeRule>();
        // ColorGroup coloGroup=new ColorGroup();
        int i = 0;
        if (sValues != null && sValues.size() > 0) {
            strtvalues = new Double[sValues.size()];
            for (String svalue : sValues) {
                if (svalue != null && !"".equalsIgnoreCase(svalue)) {
                    strtvalues[i] = (Double.parseDouble(svalue.replace("Rs.", "").replaceAll("[^0-9.]", "")));
                }
                i++;
            }
        }
        endValues = new Double[operators.size()];
        if (eValues != null && eValues.size() > 0) {
            i = 0;
            for (String evalue : eValues) {
                if (evalue != null && !"".equalsIgnoreCase(evalue)) {
                    endValues[i] = (Double.parseDouble(evalue.replace("Rs.", "").replaceAll("[^0-9.]", "")));
                }
                i++;
            }
        }
        if (operators != null && operators.size() > 0) {
            for (int k = 0; k < operators.size(); k++) {
                if (endValues[k] == null) {
                    endValues[k] = 0.0;
                    colorCodeRuleLst.add(buildColorCodeRule(colorCodes.get(k), strtvalues[k], endValues[k], MathOperator.getMathOperator(operators.get(k)), ViewBy));
                } else {
                    colorCodeRuleLst.add(buildColorCodeRule(colorCodes.get(k), strtvalues[k], endValues[k], MathOperator.getMathOperator(operators.get(k)), ViewBy));
                }
            }
        }
        return colorCodeRuleLst;
    }

    private static ColorCodeRule buildColorCodeRule(String colorCode, Double strtValue, Double endValue, MathOperator operator, String viewBy) {

        ColorCodeRule colorCodeRule = new ColorCodeRule(colorCode, strtValue, endValue, operator, viewBy);
        return colorCodeRule;
    }

    public static ArrayList<ColorCodeRule> buildMinMaxColorCode(List<String> colorCodes, List<String> operators, List<String> sValues, List<String> eValues, int noOfDays, String ViewBy) {

        ArrayList<ColorCodeRule> colorCodeRuleLst = new ArrayList<ColorCodeRule>();
        colorCodeRuleLst.add(buildColorCodeRule("", 0.00, 0.00, MathOperator.getMathOperator(""), true));
        return colorCodeRuleLst;
    }

    private static ColorCodeRule buildColorCodeRule(String colorCode, Double strtValue, Double endValue, MathOperator operator, boolean IsMinMax) {

        ColorCodeRule colorCodeRule = new ColorCodeRule(colorCode, strtValue, endValue, operator, IsMinMax);
        return colorCodeRule;
    }
}
