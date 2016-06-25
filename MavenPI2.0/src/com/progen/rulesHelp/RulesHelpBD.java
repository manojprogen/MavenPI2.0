/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.rulesHelp;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import prg.db.PbReturnObject;

public class RulesHelpBD {

    public String getDimentionsforRule(int folderId) {
        PbReturnObject dimentionObject = new PbReturnObject();
        RulesHelpDAO helpDAO = new RulesHelpDAO();
        String[] colNames = null;

        String MbrName = "";
        String elementid = "";
        StringBuilder outerBuffer = new StringBuilder("");
        dimentionObject = helpDAO.getDimentionsforRule(folderId);
        colNames = dimentionObject.getColumnNames();
        for (int i = 0; i < dimentionObject.getRowCount(); i++) {
            MbrName = dimentionObject.getFieldValueString(i, colNames[11]);
            elementid = dimentionObject.getFieldValueString(i, colNames[1]);

            outerBuffer.append("<li class='navtitle-hover DimensionULClass' id='" + elementid + "'  style='width: 200px; height: auto; color: white;'>");
            outerBuffer.append("<table><tr><td style='color: black;'>" + MbrName + "</td></tr></table>");
            outerBuffer.append("</li>");

        }

        return outerBuffer.toString();
    }

    public String getDimentionsforRule(String[] dimentionsIDs, String[] dimentionNames) {
        String MbrName = "";
        String elementid = "";
        StringBuilder outerBuffer = new StringBuilder("");
        for (int i = 0; i < dimentionsIDs.length; i++) {
            MbrName = dimentionNames[i];
            elementid = dimentionsIDs[i];

            outerBuffer.append("<li class='navtitle-hover DimensionULClass' id='" + elementid + "'  style='width: 200px; height: auto; color: white;'>");
            outerBuffer.append("<table><tr><td style='color: black;'>" + MbrName + "</td></tr></table>");
            outerBuffer.append("</li>");
        }

        return outerBuffer.toString();
    }

    public String getMeasureForRule(int folderId, String contextPath) {
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        ArrayList<String> tempList = new ArrayList<String>();
        return reportTemplateDAO.getMeasures(Integer.toString(folderId), tempList, contextPath);
    }

    public String getMeasureForRule(String[] measureNames, String[] measureIds, String contextPath) {
        StringBuilder outerBuffer = new StringBuilder("");
        outerBuffer.append("<li class='closed' id='Measures '>");
        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
        outerBuffer.append("<span style='font-family:verdana;'>Measures</span>");
        outerBuffer.append("<ul id='factName-Measures'>");
        String viewFormulaClass = "";
        for (int i = 0; i < measureNames.length; i++) {
            outerBuffer.append("<li class='closed'>");
            outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
            viewFormulaClass = "formulaViewMenu";
            outerBuffer.append("<span class='" + viewFormulaClass + "' id='" + measureIds[i] + "'  title='" + measureNames[i] + "' style='font-family:verdana;'>" + measureNames[i] + "</span>");
            outerBuffer.append("</li>");
        }
        outerBuffer.append("</ul>");
        outerBuffer.append("</li>");
        return null;
    }

    public boolean saveRule(ArrayList<String> paramList) {
        Object[] object = new Object[5];
        object[0] = paramList.get(2);
        object[1] = paramList.get(3);
        object[2] = paramList.get(1);
        object[3] = paramList.get(0);
        String[] measureIDs = paramList.get(6).split(",");
        RuleProperty ruleProperty = new RuleProperty();
        String actualRuleString = paramList.get(4);
        String[] actualRuleArray = actualRuleString.split(",");
        String displayRuleString = paramList.get(5).replaceAll(" ", "~");
        String[] displayArray = displayRuleString.split(",");
        String joinRuleAct = Joiner.on(" ").join(actualRuleArray);
        String joinRuleDisp = Joiner.on(" ").join(displayArray);
        String[] dimIDs = paramList.get(8).split(",");
        String[] dimMembers = paramList.get(9).split("~");
        Map<String, String> reportParms = new HashMap<String, String>();
        if (dimMembers[0] == "") {
            for (int count = 0; count < dimIDs.length; count++) {
                reportParms.put(dimIDs[count], dimMembers[count].substring(0, dimMembers[count].length()));

            }
        } else {
            for (int count = 0; count < dimIDs.length; count++) {
                reportParms.put(dimIDs[count], dimMembers[count].substring(0, dimMembers[count].length() - 1));

            }
        }

        ruleProperty.setActualRule(joinRuleAct);
        ruleProperty.setDisplayRule(joinRuleDisp);
        ruleProperty.setMeasureIDs(measureIDs);
        ruleProperty.setRuleOn(paramList.get(7));
        ruleProperty.setReportParms(reportParms);
        Gson gson = new Gson();
        String gsonString = gson.toJson(ruleProperty);
        object[4] = gsonString;
        RulesHelpDAO rulesHelpDAO = new RulesHelpDAO();
        boolean status = rulesHelpDAO.saveRule(object);
        return status;
    }

    public String getFilterDetails(String refid) {
        PbReturnObject filterDetILSObject = new PbReturnObject();
        RulesHelpDAO rulesHelpDAO = new RulesHelpDAO();
        filterDetILSObject = rulesHelpDAO.getFilterDetails(refid);
        Gson gson = new Gson();
        RuleProperty ruleProperty = null;
        StringBuilder filterJson = new StringBuilder();
        if (filterDetILSObject.getRowCount() > 0) {
            ruleProperty = gson.fromJson(filterDetILSObject.getFieldValueClobString(0, "RULE_DETAILS"), RuleProperty.class);
            String actValRule = ruleProperty.getActualRule();
            String[] actValRuleArray = actValRule.split(" ");
            StringBuilder tempSb = new StringBuilder();
            tempSb.append("[");
            for (int i = 0; i < actValRuleArray.length; i++) {
                if (actValRuleArray[i].trim() == null ? "" != null : !actValRuleArray[i].trim().equals("")) {
                    tempSb.append("\"" + actValRuleArray[i] + "\"");
                    if (i < actValRuleArray.length - 1) {
                        tempSb.append(",");
                    }
                }

            }
            tempSb.append("]");
            String displRule = ruleProperty.getDisplayRule();
            String[] displayRuleArray = displRule.split(" ");
            StringBuilder tempSb1 = new StringBuilder();
            tempSb1.append("[");
            for (int j = 0; j < displayRuleArray.length; j++) {
                if (displayRuleArray[j].trim() == null ? "" != null : !displayRuleArray[j].trim().equals("")) {
                    tempSb1.append("\"" + displayRuleArray[j].replace("~", " ") + "\"");
                    if (j < displayRuleArray.length - 1) {
                        tempSb1.append(",");
                    }
                }
            }
            tempSb1.append("]");
            String[] measureIds = ruleProperty.getMeasureIDs();
            StringBuilder tempSb2 = new StringBuilder();
            tempSb2.append("[");
            for (int k = 0; k < measureIds.length; k++) {
                tempSb2.append("\"" + measureIds[k] + "\"");
                if (k < measureIds.length - 1) {
                    tempSb2.append(",");
                }
            }
            tempSb2.append("]");
            filterJson.append("{");
            filterJson.append("\"ruleName\":").append("[\"").append(filterDetILSObject.getFieldValueString(0, "RULE_NAME")).append("\"],").append("\"actValRule\":").append(tempSb).append(",").append("\"displayRule\":").append(tempSb1).append(",").append("\"measureIds\":").append(tempSb2);
            filterJson.append("}");
        }
        return filterJson.toString();
    }

    public boolean deleteRule(String refid, String ruleType) {
        RulesHelpDAO rulesHelpDAO = new RulesHelpDAO();
        boolean status = rulesHelpDAO.deleteRule(refid, ruleType);
        return status;
    }

    public String getruleDimMembers(String element_id, String path) {
        RulesHelpDAO rulesHelpDAO = new RulesHelpDAO();

        return rulesHelpDAO.getruleDimMembers(element_id, path);
    }
}
