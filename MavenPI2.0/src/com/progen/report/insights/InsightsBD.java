/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.insights;

import com.google.common.base.Joiner;
import com.progen.report.KPIElement;
import com.progen.report.query.DataSet;
import com.progen.report.query.DataSetHelper;
import com.progen.report.query.PbReportQuery;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbMail;
import prg.util.PbMailParams;

/**
 *
 * @author progen
 */
public class InsightsBD extends PbDb {

    public static Logger logger = Logger.getLogger(InsightsBD.class);
    public static final String VIEW_INCREASING_VALUES = "increase";
    public static final String VIEW_DECREASING_VALUES = "decrease";
    public static final String VIEW_ALL_VALUES = "both";

    public String getInitialInsights(InsightParams insightParams, String dispType, InsightBaseDetails baseDetails, String changedMeasures) {


        StringBuilder output = new StringBuilder();
        ArrayList rowviewByValues = new ArrayList();
        ArrayList colviewByValues = new ArrayList();
        ArrayList<String> dispMeasures = new ArrayList<String>();
        ArrayList<String> dispMeasureNames = new ArrayList<String>();

//        if (baseDetails.getDrillMap() == null){
//            DrillMaps drillMapHelper = new DrillMaps();
//            baseDetails.setDrillMap(drillMapHelper.getDrillForReport(baseDetails.getReportId(), baseDetails.getUserId(), baseDetails.getDimensionIds().get(0).toString(),
//                                   baseDetails.getParameters()));
//        }

        StringBuilder paramValsSB = new StringBuilder();
        if (baseDetails.getParameters() != null && !baseDetails.getParameters().isEmpty()) {
            Set keySet = baseDetails.getParameters().keySet();
            Iterator keyIter = keySet.iterator();

            while (keyIter.hasNext()) {
                String id = (String) keyIter.next();
                String val = (String) baseDetails.getParameters().get(id);
                val = val.replace(",", "~");
                paramValsSB.append(",").append(id).append(":").append(val);
            }
        }
        String paramVals = "";
        if (paramValsSB.length() > 0) {
            paramVals = paramValsSB.substring(1);
        }

        ArrayList<String> qryCols = new ArrayList<String>();
        ArrayList<String> qryAggrs = new ArrayList<String>();
        ArrayList<String> qryColsNames = new ArrayList<String>();
        String[] insightChangedCols = null;


        //populateKPIElementList(baseDetails.getKpiElement(), qryCols, qryAggrs);
        if (changedMeasures != null) {
            insightChangedCols = changedMeasures.split(",");
            for (int i = 0; i < insightChangedCols.length; i++) {
                dispMeasures.add(insightChangedCols[i]);
            }
            baseDetails.setDispMeasures(dispMeasures);
        }

        LinkedHashMap params = baseDetails.getParameters();
        PbReportQuery repQuery = new PbReportQuery();
        //  PbReturnObject kpiRetObj = container.getKpiRetObj();
        PbReturnObject kpiRetObj = new PbReturnObject();
        //if (!qryCols.isEmpty())
        {
            repQuery.setRowViewbyCols(rowviewByValues);
            repQuery.setColViewbyCols(colviewByValues);
//            repQuery.setRowViewbyCols(new ArrayList());
//            repQuery.setColViewbyCols(new ArrayList());
            repQuery.setParamValue(params);
//            if(changedMeasures!=null && !changedMeasures.equalsIgnoreCase("null"))
//            {
//                if(!(changedMeasures.equals("")))
//                {
            //insightChangedCols = changedMeasures.split(",");
            Map dataHelper = DataSetHelper.getAggregationMap(baseDetails.getDispMeasures());
            for (int i = 0; i < baseDetails.getDispMeasures().size(); i++) {
                String aggType = dataHelper.get(baseDetails.getDispMeasures().get(i)).toString();
                if (!(qryCols.contains(baseDetails.getDispMeasures().get(i)))) {
                    qryCols.add(baseDetails.getDispMeasures().get(i));
                }
                qryAggrs.add(aggType);

            }
            //}
            // }
            repQuery.setQryColumns(qryCols);
            repQuery.setColAggration(qryAggrs);
            repQuery.setTimeDetails(baseDetails.getTimeDetailsArray());
            repQuery.isKpi = true;

            kpiRetObj = repQuery.getPbReturnObject(String.valueOf(qryCols.get(0)));
            for (int j = 0; j < qryCols.size(); j++) {
                qryColsNames.add(repQuery.NonViewByMap.get("A_" + qryCols.get(j)).toString());
            }
            for (int k = 0; k < qryColsNames.size(); k++) {
                dispMeasureNames.add(qryColsNames.get(k));
            }
            if (changedMeasures != null && !changedMeasures.equalsIgnoreCase("null")) {
                baseDetails.setDispMeasureNames(dispMeasureNames);
            }
            //session.setAttribute("measureNames", qryColsNames);
        }
        ArrayList paramIds = baseDetails.getDimensionIds();
        ArrayList paramNames = baseDetails.getDimensionNames();

//        Set<String> paramIdsSet = new LinkedHashSet<String>();
//        Set<String> paramNamesSet = new LinkedHashSet<String>();
//
//        for (int i=0;i<paramIds.size();i++){
//            paramIdsSet.add((String) paramIds.get(i));
//            paramNamesSet.add((String) paramNames.get(i));
//        }

//        Iterator<String> paramIdsIter = paramIdsSet.iterator();
//        Iterator<String> paramNamesIter = paramNamesSet.iterator();

        String parameterRegion = getParameterText(paramIds, paramNames, params);

        output.append(parameterRegion);

        output.append("<table border=\"1\" id=\"tablesorter\"  class=\"tablesorter\" style=\"border-collapse:collapse\">");
        output.append("<thead>");
        output.append("<tr>");
        output.append("<th/>");
        output.append("<th align=\"center\">Dimension</th>");

        for (int k = 0; k < qryColsNames.size(); k++) {
            output.append("<th align=\"center\">").append(qryColsNames.get(k)).append("</th>");
        }

        output.append("</tr>");
        output.append("</thead>");
        output.append("<tbody>");

        // For Each Dimension add the kpi values
        // while(paramIdsIter.hasNext() && paramNamesIter.hasNext())
        for (int j = 0; j < paramIds.size(); j++) {
            String id = (String) paramIds.get(j);
            String divId = id + "Div";
            String childDivId = id + "ChildDiv";
            String name = (String) paramNames.get(j);
            output.append("<tr id='").append(divId).append("'>");
            output.append("<td class=\"collapsible\" rowspan=\"2\" width=\"20px\">");
            output.append("<a class=\"collapsed\" onclick=\"loadChildData('" + divId + "','" + id + "','" + childDivId + "','" + paramVals + "');\"></a></td>");
            output.append("<td>").append(name).append("</td>");
            for (int i = 0; i < qryCols.size(); i++) {
                output.append("<td align='right'>").append(kpiRetObj.getModifiedNumber(kpiRetObj.getFieldValueBigDecimal(0, "A_" + qryCols.get(i)), "", -1)).append("</td>");
            }

            output.append("</tr>");
//            if(changedMeasures!=null )
//            {
//                 if(!(changedMeasures.equals("")) && !changedMeasures.equalsIgnoreCase("null"))
//                 {
            //int newMeasureCount=insightChangedCols.length;
            int newMeasureCount = baseDetails.getDispMeasures().size();
            output.append("<tr class=\"expand-child\"><td style=\"display: none;\" colspan='").append(2 + newMeasureCount).append("'>");
//                 }
//                 else
//                output.append("<tr class=\"expand-child\"><td style=\"display: none;\" colspan='6'>");
//            }
//             else
//                output.append("<tr class=\"expand-child\"><td style=\"display: none;\" colspan='6'>");

            output.append("<div id='").append(childDivId).append("'></div>");
            output.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
            output.append("</td></tr>");
        }

        output.append("</tbody>");
        output.append("</table>");

        return output.toString();
    }

    public String getInsightForDimension(InsightBaseDetails baseDetails, InsightParams insightParams, String dimId, String dispType) {
        String output = generateTable(insightParams, dispType, baseDetails);
        return output;
    }

    public String getInsightForDimensionDrill(InsightBaseDetails baseDetails, InsightParams insightParams, String dispType) {

        if (insightParams.getChildDimId() == null || "".equals(insightParams.getChildDimId())) {
            insightParams.setChildDimId(baseDetails.getDrillMap().get(insightParams.getDimId()));
        }

        String output = generateTable(insightParams, dispType, baseDetails);
        return output;
    }

    private String generateTable(InsightParams insightParams, String dispType, InsightBaseDetails baseDetails) {
        String pmName = "";
        StringBuilder pmFunction = new StringBuilder();
        StringBuilder drillHtml = new StringBuilder();
        StringBuilder output = new StringBuilder();
        boolean drillAvailable = false;
        ArrayList paramIds = baseDetails.getDimensionIds();
        ArrayList paramNames = baseDetails.getDimensionNames();
        String dimName = "";
        String paramId = null;
        String firstDimId = null;
        String dimId = insightParams.getDimId();

        String nextLevel = "odd";
        if (insightParams.isIsOddLevel()) {
            nextLevel = "even";
        }

        String tempDimId = insightParams.getChildDimId();
        if (tempDimId == null) {
            tempDimId = insightParams.getDimId();
        }

        if (insightParams.getChildDimId() != null) {
            insightParams.setMasterDimId(insightParams.getMasterDimId() + "," + insightParams.getChildDimId());
        } else {
            insightParams.setMasterDimId(insightParams.getDimId());
        }


        HashMap paramMap = getParamMap(insightParams.getParameter());

        if (insightParams.getDimValue() != null) {

            paramMap.put(dimId, insightParams.getDimValue());
//            HashMap changedParamMap=new HashMap();
//             Set keySet = paramMap.keySet();
//            Iterator keyIter = keySet.iterator();
//
//            while (keyIter.hasNext()){
//                String id = (String) keyIter.next();
//                String val = (String) paramMap.get(id);
//                val=val.replace(",", "~");
//                changedParamMap.put(id, val);
//            }
            insightParams.setParameter(convertToString(paramMap));
        }

        for (int j = 0; j < paramNames.size(); j++) {
            paramId = paramIds.get(j).toString();
            pmName = paramNames.get(j).toString();

            if (insightParams.getMasterDimId() != null) {
                if (!insightParams.getMasterDimId().contains(paramId)) {
                    if (firstDimId == null) {
                        firstDimId = paramId;
                    }
                    pmFunction.append("<li><a class=\"collapsed\" onclick=\"").append("loadChildDrillData('DIV_ID','").append(insightParams.getMasterDimId()).append("','").append(tempDimId).append("','DIMENSION_DATA','CHILD_DIV_ID','").append(insightParams.getParameter()).append("','").append(nextLevel).
                            append("','").append(paramId).append("',true);\"").append(">").append(pmName).append("</a></li>");
                }
            }
        }

        if (pmFunction.length() > 0) {
            drillHtml.append("<ul class=\"dropDownMenu\">");
            drillHtml.append("<li><a href=\"#\" style=\"background-color:#FFFFFF\">DIMENSION_DATA</a>");
            drillHtml.append("<ul style=\"width:20x\">");
            drillHtml.append(pmFunction);
            drillHtml.append("</ul></li></ul>");
        }
        String drillHtmlStr = drillHtml.toString();

        ArrayList<String> qryCols = new ArrayList<String>();
        ArrayList<String> qryAggrs = new ArrayList<String>();
        List<String> qryColsNames = new ArrayList<String>();
        String[] insightChangedCols = null;
//          if(baseDetails.getChangedMeasures()==null ||baseDetails.getChangedMeasures().equalsIgnoreCase("null"))
//        populateKPIElementList(baseDetails.getKpiElement(), qryCols, qryAggrs);


        PbReportQuery repQuery = new PbReportQuery();
        ArrayList<String> rowViewBys = new ArrayList<String>();
        ArrayList<String> colViewBys = new ArrayList<String>();

//        LinkedHashMap params = baseDetails.getParameters();

        int dimIndex = -1;
        if (insightParams.getChildDimId() == null) {

            drillAvailable = isDrillAvailable(baseDetails.getDrillMap(), dimId);
            rowViewBys.add(dimId);
            dimIndex = paramIds.indexOf(dimId);
        } else {
            drillAvailable = isDrillAvailable(baseDetails.getDrillMap(), insightParams.getChildDimId());
            rowViewBys.add(insightParams.getChildDimId());
            dimIndex = paramIds.indexOf(insightParams.getChildDimId());
        }
        if (dimIndex >= 0) {
            dimName = (String) paramNames.get(dimIndex);
        }
        // if(changedChildMeasures!=null&& !changedChildMeasures.equalsIgnoreCase("null"))
        // {
        //  if(!(changedChildMeasures.equals("")))
        //  {
        //insightChangedCols = changedChildMeasures.split(",");
        Map dataHelper = DataSetHelper.getAggregationMap(baseDetails.getDispMeasures());
        for (int i = 0; i < baseDetails.getDispMeasures().size(); i++) {
            String aggType = dataHelper.get(baseDetails.getDispMeasures().get(i)).toString();
            if (!(qryCols.contains(baseDetails.getDispMeasures().get(i)))) {
                qryCols.add(baseDetails.getDispMeasures().get(i));
            }
            qryAggrs.add(aggType);
        }
        //  }
        // }
        String indicator = "";
        if (baseDetails.getDispMeasures().contains(baseDetails.getIndicatorMeasure())) {
            indicator = "A_" + baseDetails.getIndicatorMeasure();
        } else {
            baseDetails.setIndicatorMeasure(baseDetails.getDispMeasures().get(0));
            indicator = "A_" + baseDetails.getDispMeasures().get(0);
        }

        String sortString = null;
        if (dispType.equalsIgnoreCase(VIEW_DECREASING_VALUES)) {
            sortString = "order by A_" + baseDetails.getIndicatorMeasure();
        } else if (dispType.equalsIgnoreCase(VIEW_INCREASING_VALUES)) {
            sortString = "order by A_" + baseDetails.getIndicatorMeasure() + " desc";
        }

        DataSetHelper helper = new DataSetHelper.DataSetHelperBuilder().measIds(qryCols).measAggrs(qryAggrs).rowViewBys(rowViewBys).colViewBys(colViewBys).paramValues(paramMap).timeDetails(baseDetails.getTimeDetailsArray()).userId(baseDetails.getUserId()).bizRole(baseDetails.getBizRoles()).orderBys(sortString).build();
        DataSet dataSet = helper.getDataSet();
        PbReturnObject retObj = dataSet.getData();
        qryColsNames = dataSet.getMeasNames();

        String oddStyle = "";

        if (insightParams.isIsOddLevel()) {
            oddStyle = " style=\"background-color: gainsboro\" ";
        }

        output.append("<table border=\"1\" id=\"tablesorter\"  class=\"tablesorter\" style=\"border-collapse:collapse;border-left-style: hidden;border-right-style: hidden;\">");

        if (!qryCols.isEmpty()) {

            if (retObj != null && retObj.getRowCount() > 0) {
                output.append("<thead>");
                output.append("<tr>");
                if (drillAvailable || !drillHtmlStr.isEmpty()) {
                    if (insightParams.isIsOddLevel()) {
                        output.append("<th style=\"background-color: gainsboro\"/>");
                    } else {
                        output.append("<th />");
                    }
                }
                output.append("<th align=\"center\"" + oddStyle + ">").append(dimName).append("</th>");
                for (int k = 0; k < qryColsNames.size(); k++) {
                    output.append("<th align=\"center\"" + oddStyle + ">").append(qryColsNames.get(k)).append("</th>");
                }

                output.append("<th align=\"center\"").append(oddStyle).append("/>");
                output.append("<th align=\"center\"").append(oddStyle).append("/>");
                output.append("</tr>");
                output.append("</thead>");
                output.append("<tbody>");



                for (int i = 0; i < retObj.getRowCount(); i++) {


                    // BigDecimal changePercVal = retObj.getFieldValueBigDecimal(i, 4);
                    BigDecimal changePercVal = retObj.getFieldValueBigDecimal(i, indicator);
                    String[] amount = new String[retObj.getColumnCount()];
                    for (int a = 0; a < retObj.getColumnCount(); a++) {
                        amount[a] = retObj.getModifiedNumber(retObj.getFieldValueBigDecimal(i, a), "", -1);
                    }

                    if (isRowApplicable(dispType, changePercVal)) {
                        String dimData = retObj.getFieldValueString(i, 0);
                        String temp = dimData.replace(" ", "");
                        temp = temp.replace(".", "");
                        temp = temp.replace("(", "");
                        temp = temp.replace(")", "");
                        String str = UUID.randomUUID().toString();
                        String divId = str + "Div";
                        String childDivId = str + "ChildDiv";
                        String lightIcon = getLightImage(changePercVal);
                        output.append("<tr id='").append(divId).append("'>");
                        String tempStr = drillHtmlStr;
                        tempStr = tempStr.replaceAll("CHILD_DIV_ID", childDivId);
                        tempStr = tempStr.replaceAll("DIV_ID", divId);
                        tempStr = tempStr.replaceAll("DIMENSION_DATA", dimData);
                        if (drillAvailable || (!tempStr.isEmpty())) {
                            output.append("<td class=\"collapsible\" rowspan=\"2\" width=\"20px\" style=\"border-bottom-style:hidden;\">");
                            if (drillAvailable) {
                                firstDimId = "";
                            }
                            if (insightParams.getChildDimId() == null) {
                                output.append("<a class=\"collapsed\" onclick=\"loadChildDrillData('").append(divId).append("','").append(insightParams.getMasterDimId()).append("','").append(dimId).append("','").append(dimData).append("','").append(childDivId).append("','").append(insightParams.getParameter()).append("','").append(nextLevel).append("','").append(firstDimId).append("',false);\"></a></td>");
                            } else {
                                output.append("<a class=\"collapsed\" onclick=\"loadChildDrillData('").append(divId).append("','").append(insightParams.getMasterDimId()).append("','").append(insightParams.getChildDimId()).append("','").append(dimData).append("','").append(childDivId).append("','").append(insightParams.getParameter()).append("','").append(nextLevel).append("','").append(firstDimId).append("',false);\"></a></td>");
                            }


                        }

                        if (tempStr.isEmpty()) {
                            output.append("<td style=\"border-left-style: hidden;\">" + dimData + "</td>");
                        } else {
                            output.append("<td style=\"border-left-style: hidden;\">" + tempStr + "</td>");
                        }

                        for (int m = 1; m < retObj.getColumnCount(); m++) {
                            output.append("<td style=\"border-left-style: hidden;\" align='right'>").append(retObj.getModifiedNumber(retObj.getFieldValueBigDecimal(i, m), "", -1)).append("</td>");
                        }

                        output.append("<td style=\"border-left-style: hidden;border-right-style: hidden;\" align='center'>").append(lightIcon).append("</td>");
                        output.append("<td align='center'><a onClick=\"openMailDialog('").append(dimData).append("','").append(dimName).append("','").append(Joiner.on("~").join(amount)).append("'").append(")\"><img src=\"icons pinvoke/mail.png\"/></a></td>");
                        output.append("</tr>");
                        if (drillAvailable || (!tempStr.isEmpty())) {
//                           if(changedChildMeasures!=null&& !changedChildMeasures.equalsIgnoreCase("null"))
//                            {
//                                if(!(changedChildMeasures.equals("")))
//                                {
                            // int newMsrCount=insightChangedCols.length;
                            int newMsrCount = baseDetails.getDispMeasures().size();
                            output.append("<tr class=\"expand-child\"><td style=\"display: none;border-style: hidden;\" colspan='").append(3 + newMsrCount).append("'>");
//                                }
//                            else
                            // output.append("<tr class=\"expand-child\"><td style=\"display: none;border-style: hidden;\" colspan='7'>");
//                            }
//                             else
                            // output.append("<tr class=\"expand-child\"><td style=\"display: none;border-style: hidden;\" colspan='7'>");
                            output.append("<div id='").append(childDivId).append("prgBar").append("'></div>");
                            output.append("<div id='").append(childDivId).append("'></div>");
                            output.append("</td></tr>");
                        }
                    }
                }
                output.append("</tbody>");
            }
        }

        output.append("</table>");
        return output.toString();
    }

    private void populateKPIElementList(List<KPIElement> kpiElements, ArrayList<String> qryCols, ArrayList<String> qryAggrs) {

        for (KPIElement elem : kpiElements) {
            qryCols.add(elem.getElementId());
            qryAggrs.add(elem.getAggregationType());
        }
    }

    public boolean isDrillAvailable(Map<String, String> drillMap, String dimId) {
        boolean drillAvailable = false;
        if (drillMap.containsKey(dimId)) {
            String drillVal = drillMap.get(dimId);
            if (drillVal != null && !(drillVal.equalsIgnoreCase(dimId))) {
                drillAvailable = true;
            }
        }
        return drillAvailable;
    }

    public boolean isDrillAvailable(Map<String, String> drillMap, String dimId, ArrayList<String> parameters) {
        boolean drillAvailable = false;
        if (drillMap.containsKey(dimId)) {
            String drillVal = drillMap.get(dimId);
            if (drillVal != null && !(drillVal.equalsIgnoreCase(dimId)) && parameters.contains(drillVal)) {
                drillAvailable = true;
            }
        }
        return drillAvailable;
    }

    private String getLightImage(BigDecimal val) {
        String lightIcon = "<img src='icons pinvoke/status-offline.png'></img>";
        if (val != null) {
            double measVal = val.doubleValue();
            if (measVal < 0) {
                lightIcon = "<img src='icons pinvoke/status-busy.png'></img>";
            } else if (measVal > 0) {
                lightIcon = "<img src='icons pinvoke/status.png'></img>";
            }
        }
        return lightIcon;
    }

    private boolean isRowApplicable(String dispType, BigDecimal val) {
        if (val == null) {
            return false;
        }

        double measVal = val.doubleValue();
        if ("both".equalsIgnoreCase(dispType)) {
            return true;
        } else if ("decrease".equalsIgnoreCase(dispType) && measVal < 0) {
            return true;
        } else if ("increase".equalsIgnoreCase(dispType) && measVal > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<KPIElement> getKPIElements(String elementId) {
        if (elementId.startsWith("A_")) {
            elementId = elementId.replace("A_", "");
        }
        List<KPIElement> kpiElements = new ArrayList<KPIElement>();
        try {
            PbReturnObject retObj = null;
            String finalQuery = "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE, user_col_desc  from  PRG_USER_ALL_INFO_DETAILS"
                    + " where REF_ELEMENT_ID = (select ref_element_id from PRG_USER_ALL_INFO_DETAILS where element_id = " + elementId + ")"
                    + " order by REF_ELEMENT_TYPE asc";
            retObj = execSelectSQL(finalQuery);

            if (retObj != null && retObj.getRowCount() > 0) {
                String[] ColNames = retObj.getColumnNames();
                for (int looper = 0; looper < retObj.getRowCount(); looper++) {
                    String refElementId = retObj.getFieldValueString(looper, ColNames[1]);
                    KPIElement kpiElem = new KPIElement();
                    kpiElem.setElementId(retObj.getFieldValueString(looper, ColNames[0]));
                    kpiElem.setRefElementId(refElementId);
                    kpiElem.setRefElementType(retObj.getFieldValueString(looper, ColNames[2]));
                    kpiElem.setAggregationType(retObj.getFieldValueString(looper, ColNames[3]));
                    kpiElem.setElementName(retObj.getFieldValueString(looper, ColNames[4]));
                    kpiElements.add(kpiElem);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return kpiElements;
    }

    private String getParameterText(ArrayList paramIds, ArrayList paramNames, LinkedHashMap paramValues) {
        StringBuilder output = new StringBuilder();

//        Set<String> paramIdsSet = new LinkedHashSet<String>();
//        Set<String> paramNamesSet = new LinkedHashSet<String>();
//
//        for (int i=0;i<paramIds.size();i++){
//            paramIdsSet.add((String) paramIds.get(i));
//            paramNamesSet.add((String) paramNames.get(i));
//        }
//
//        Iterator<String> idsIter = paramIdsSet.iterator();
//        Iterator<String> namesIter = paramNamesSet.iterator();

        output.append("<div id=\"paramsDiv\">");

        for (int i = 0; i < paramIds.size(); i++) {
            String paramId = (String) paramIds.get(i);
            String paramName = (String) paramNames.get(i);

            String paramVal = (String) paramValues.get(paramId);

            output.append("<font style=\"font-weight:bold\">").append(paramName).append(" : </font>");
            output.append(paramVal).append("  ");
        }

        output.append("</div>");

        return output.toString();
    }

    private HashMap getParamMap(String insightParams) {
        HashMap map = new HashMap();
        if (insightParams != null && !("".equals(insightParams))) {
            String[] dimValPairs = insightParams.split(",");

            for (String pair : dimValPairs) {
                String[] nameVal = pair.split(":");
                nameVal[1] = nameVal[1].replace("~", ",");
                map.put(nameVal[0], nameVal[1]);
            }
        }
        return map;
    }

    public Map<String, String> makeDrillMap(String drillMapStr) {

        String allValues[] = drillMapStr.split(",");
        Map<String, String> drillMap = new HashMap<String, String>();
        for (int i = 0; i < allValues.length; i++) {
            String val = allValues[i];
            if (val != null && !("".equals(val))) {
                String indVal[] = val.split("=");
                drillMap.put(indVal[0], indVal[1]);
            }
        }
        return drillMap;
    }

    public void sendMail(String toAddress, String subject, String mailContent, String dimName, String dimData, String amount, HttpSession session) {

        String[] amounts = amount.split("~");
        PbMailParams params = new PbMailParams();
        ArrayList<String> measureNames = (ArrayList<String>) session.getAttribute("measureNames");
        StringBuilder completeContent = new StringBuilder();
        completeContent.append("<html><body>");
        completeContent.append("<font style=\"color: #000000\">");
        completeContent.append(mailContent);
        completeContent.append("</font>");
        completeContent.append("<br/> <br/>");
        completeContent.append("<table border=1 class=\"tablesorter\" style=\"border-collapse:collapse;\">");
        completeContent.append("<thead><tr>");
        completeContent.append("<th align=center style=\"background-color: gainsboro;color :rgb(51, 102, 153)\" width=\"180px\">");
        completeContent.append(dimName);
        completeContent.append("</th>");
        for (String msrNm : measureNames) {
            completeContent.append("<th align=center style=\"background-color: gainsboro;color :rgb(51, 102, 153)\" width=\"180px\">");
            completeContent.append(msrNm);
            completeContent.append("</th>");
        }
        completeContent.append("</tr></thead><tbody><tr>");
        completeContent.append("<td align='left' style=\"color: #2A5DB0\" >");
        completeContent.append(dimData);
        completeContent.append("</td>");
        for (int i = 1; i < amounts.length; i++) {
            completeContent.append("<td align='right'>");
            completeContent.append(amounts[i]);
            completeContent.append("</td>");
        }

        completeContent.append("</tr>");
        completeContent.append("</tbody></table>");


        completeContent.append("</body></html>");
        params.setBodyText(completeContent.toString());
        params.setToAddr(toAddress);
        params.setSubject(subject);
        params.setHasAttach(true);

        PbMail pbMail = new PbMail(params);
        try {
            boolean status = pbMail.sendMail();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    private String convertToString(HashMap paramMap) {
        StringBuilder params = new StringBuilder();
        Set keySet = paramMap.keySet();
        Iterator iter = keySet.iterator();

        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) paramMap.get(key);
            value = value.replace(",", "~");
            params.append(key).append(":").append(value).append(",");
        }

        params.replace(params.length() - 1, params.length(), "");
        return params.toString();
    }

    public LinkedHashMap getParameters(LinkedHashMap reportParametersValues, HashMap insightParams) {
        LinkedHashMap params = (LinkedHashMap) reportParametersValues.clone();

        if (insightParams != null && !insightParams.isEmpty()) {
            Set keySet = insightParams.keySet();
            Iterator iter = keySet.iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = (String) insightParams.get(key);

                params.put(key, value);
            }
        }

        return params;
    }

    String getSelectedMeasures(InsightBaseDetails baseDetails) {

        StringBuilder selectedMeasures = new StringBuilder();
        for (int i = 0; i < baseDetails.getDispMeasures().size(); i++) {
            selectedMeasures.append(" <li id='GrpCol" + baseDetails.getDispMeasures().get(i) + "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
            selectedMeasures.append("<table id='GrpTab" + baseDetails.getDispMeasures().get(i) + "'>");
            selectedMeasures.append(" <tr>");
            selectedMeasures.append(" <td >");
            selectedMeasures.append("  <a href=\"javascript:deleteColumn('GrpCol" + baseDetails.getDispMeasures().get(i) + "')\" class=\"ui-icon ui-icon-close\"></a>");
            selectedMeasures.append("</td>");
            selectedMeasures.append("<td style=\"color:black\">" + baseDetails.getDispMeasureNames().get(i) + "</td>");
            selectedMeasures.append("</tr>");
            selectedMeasures.append("</table>");
            selectedMeasures.append("</li>");
        }
//        if(baseDetails.getChangedMeasures()!=null)
//        {
//            selectedMeasures.append(" <li id='GrpCol" + baseDetails.getChangedMeasures()+ "' style='width:auto;height:auto;color:white' class='navtitle-hover'>");
//            selectedMeasures.append("<table id='GrpTab" + baseDetails.getKpiElement().get(i).getElementId()  + "'>");
//            selectedMeasures.append(" <tr>");
//            selectedMeasures.append(" <td >");
//            selectedMeasures.append("  <a href=\"javascript:deleteColumn('GrpCol" +baseDetails.getKpiElement().get(i).getElementId()  + "')\" class=\"ui-icon ui-icon-close\"></a>");
//            selectedMeasures.append("</td>");
//            selectedMeasures.append("<td style=\"color:black\">" + baseDetails.getKpiElement().get(i).getElementName() + "</td>");
//            selectedMeasures.append("</tr>");
//            selectedMeasures.append("</table>");
//            selectedMeasures.append("</li>");
//        }
        return selectedMeasures.toString();
    }

    String getIndicatorMeasures(InsightBaseDetails baseDetails) {
        StringBuilder indicatorMeasures = new StringBuilder();

        for (int i = 0; i < baseDetails.getDispMeasures().size(); i++) {
            indicatorMeasures.append("<option value=\"").append(baseDetails.getDispMeasures().get(i)).append("\">");
            indicatorMeasures.append(baseDetails.getDispMeasureNames().get(i)).append("</option>");
        }

        return indicatorMeasures.toString();
    }
}
