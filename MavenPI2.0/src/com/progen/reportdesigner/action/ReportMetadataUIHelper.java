/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.action;

import com.google.common.base.Joiner;
import com.progen.report.PbReportCollection;
import com.progen.report.query.QueryTimeHelper;
import com.progen.userlayer.db.UserLayerDAO;
import prg.db.PbReturnObject;
import org.apache.log4j.*;

/**
 *
 * @author progen
 */
public class ReportMetadataUIHelper {

    public static Logger logger = Logger.getLogger(ReportMetadataUIHelper.class);

    public String showMetaData(PbReportCollection collect) {
        StringBuilder tableBuild = new StringBuilder();
        //     try {
        //collect.getParamMetaData(true);
        Iterable<String> measures = collect.getMeasures();
        Iterable<String> dimensions = collect.getDimensions();
        getTimeDetails(collect);
        StringBuilder elements = new StringBuilder();
        String elementIds;
        if (measures.iterator().hasNext()) {
            elements = Joiner.on(",").appendTo(elements, measures);
        }
        if (dimensions.iterator().hasNext()) {
            if (measures.iterator().hasNext()) {
                elements.append(",");
            }
            elements = Joiner.on(",").appendTo(elements, dimensions);
        }
        elementIds = elements.toString();

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        PbReturnObject retObj = new PbReturnObject();
        retObj = userLayerDAO.getTableDetails(elementIds);
        tableBuild.append("<table id=\"role\" align=\"center\"><tr><td align=\"center\" class=\"myHead\" style=\"width:70%\">Role       : ");
        tableBuild.append(retObj.getFieldValueString(0, "FOLDER_NAME")).append("</td></tr>");
        tableBuild.append(getTimeDetails(collect));
        tableBuild.append("</table>");
        if (dimensions.iterator().hasNext()) {
            tableBuild.append(buildDimensions(retObj));
        }
        if (measures.iterator().hasNext()) {
            tableBuild.append(buildMeasures(retObj));
        }
        // tableBuild=buildTable(retObj,collect);

        // } catch (Exception ex) {
        //  logger.error("Exception:",ex);
        //    }
        return tableBuild.toString();


    }

    private String getTimeDetails(PbReportCollection collect) {
        //ArrayList<String> timeDetailsArray=new ArrayList<String>();
        StringBuilder timeDetails = new StringBuilder();
        String timeInfo = collect.timeDetailsArray.get(1).toString();
        String type = QueryTimeHelper.getTimeDetails(timeInfo);

        if (!collect.avoidProgenTime) {
            timeDetails.append("<tr><td align=\"center\" class=\"myHead\" style=\"width:70%\">Time Level :").append(collect.timeDetailsArray.get(0)).append("</td></tr>");
            timeDetails.append("<tr><td align=\"center\" class=\"myHead\" style=\"width:70%\">Type   :").append(type).append("</td></tr>");

        }
        return timeDetails.toString();

    }

    private String buildDimensions(PbReturnObject retObj) {

        StringBuilder dimensionBuild = new StringBuilder();
        String dimension = "";
        dimensionBuild.append(" <table align=\"center\" id=\"dimheader\"><tr><td><h5>Dimensions</h5></td></tr></table><table id=\"dimensiondetails\" class=\"tablesorter\"><thead><tr><th>Name</th><th>Business Table Name</th><th>Business Column Name</th><th>User Column Name</th></tr></thead><tbody>");


        if (retObj != null && retObj.getRowCount() > 0) {

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimension = retObj.getFieldValueString(i, "IS_DIMENSION");
                if (dimension.equalsIgnoreCase("Y")) {
                    dimensionBuild.append("<tr><td>").append(retObj.getFieldValueString(i, "DISP_NAME")).append("</td><td>").append(retObj.getFieldValueString(i, "BUSS_TABLE_NAME")).append("</td><td>").
                            append(retObj.getFieldValueString(i, "BUSS_COL_NAME")).append("</td><td>").append(retObj.getFieldValueString(i, "USER_COL_NAME")).
                            append("</td></tr>");
                }

            }
        }
        dimensionBuild.append("</tbody></table>");
        return dimensionBuild.toString();

    }

    private String buildMeasures(PbReturnObject retObj) {

        StringBuilder measureBuild = new StringBuilder();
        String fact = "";
        String actualColFormula = "";
        String displayFormula = "";
        StringBuilder display = new StringBuilder();
        if (retObj != null && retObj.getRowCount() > 0) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                actualColFormula = retObj.getFieldValueString(i, "ACTUAL_COL_FORMULA");
                displayFormula = retObj.getFieldValueString(i, "DISPLAY_FORMULA");
                if (actualColFormula != null && displayFormula != null) {
                    display.append(retObj.getFieldValueString(i, "DISPLAY_FORMULA"));
                }

            }
        }
        measureBuild.append(" <table align=\"center\" id=\"measureheader\"><tr><td><h5>Measures</h5></td></tr></table><table id=\"measuredetails\" class=\"tablesorter\"><thead><tr><th>Name</th><th>Business Table Name</th><th>Business Column Name</th><th>User Column Name</th><th>Aggregation Type</th>");//<th>Display Formula</th></tr></thead><tbody>");
        if (display.length() > 0) {
            measureBuild.append("<th>Formula</th>");
        }
        measureBuild.append("</tr></thead><tbody>");

        if (retObj != null && retObj.getRowCount() > 0) {

            for (int i = 0; i < retObj.getRowCount(); i++) {
                actualColFormula = retObj.getFieldValueString(i, "ACTUAL_COL_FORMULA");
                displayFormula = retObj.getFieldValueString(i, "DISPLAY_FORMULA");
                fact = retObj.getFieldValueString(i, "IS_FACT");
                if (fact.equalsIgnoreCase("Y")) {
                    measureBuild.append("<tr><td>").append(retObj.getFieldValueString(i, "USER_COL_DESC")).append("</td><td>").append(retObj.getFieldValueString(i, "BUSS_TABLE_NAME")).append("</td><td>").
                            append(retObj.getFieldValueString(i, "BUSS_COL_NAME")).append("</td><td>").append(retObj.getFieldValueString(i, "USER_COL_NAME")).append("</td><td>").
                            append(retObj.getFieldValueString(i, "AGGREGATION_TYPE")).append("</td>");
                    if (display.length() > 0) {
                        measureBuild.append("<td>");
                        measureBuild.append(retObj.getFieldValueString(i, "DISPLAY_FORMULA"));
                        measureBuild.append("</td>");
                    }
                    measureBuild.append("</tr>");

                }

            }
        }
        measureBuild.append("</tbody></table>");
        return measureBuild.toString();


    }
}
