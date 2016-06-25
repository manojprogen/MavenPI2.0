/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableSubtotalRow;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class TableSubtotalDisplay extends TableDisplay {

    public TableSubtotalDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public void setFromOneviewflag(String fromOneview) {
        super.fromOneviewflag = fromOneview;
    }

    public String getFromOneviewflag() {
        return super.fromOneviewflag;
    }
    String gtColorNew = "";
    String subColorNew = "";

    @Override
    public StringBuilder generateHTML() {
        StringBuilder subTotalHtml = new StringBuilder();
        TableSubtotalRow[] subTotalRow;// = tableBldr.getSubtotalRowDataLastRow();
        //subTotalHtml.append(this.generateSubTotalHtml(subTotalRow));
        String isoneview = tableBldr.getGetFromOneview();
        if (isoneview != null && isoneview.equalsIgnoreCase("fromOneview")) {
        } else {
            subTotalRow = tableBldr.getGrandtotalRowData();
            for (int i = 0; i < subTotalRow.length; i++) {
                if (tableBldr.getIsSubToTalSearchFilterApplied() && subTotalRow[i].getSubtotalType().equalsIgnoreCase("GT")) {

                    subTotalRow[i] = tableBldr.updateGrandTotalAfterSubFilter(subTotalRow[i]);

                }
            }
//        for ( int i=0; i<subTotalRow.length; i++ )
//        {
//            row = subTotalRow[i];
//            subTotalHtml.append("<Tr ID=\"").append(row.getSubtotalType()).append("\" ");
//            for ( int col=0; col < colCount; col++ )
//            {
//                subTotalHtml.append("<Td  ID=\"").append(row.getID(col)).append("\" align=\"").append(row.getAlignment(col)).append("\">");
//                subTotalHtml.append("<B>");
//                subTotalHtml.append(row.getRowData(col));
//                subTotalHtml.append("</B></Td>");
//            }
//            subTotalHtml.append("</Tr>");
//        }
            subTotalHtml.append(this.generateSubTotalHtml(subTotalRow));
        }
        subTotalHtml.append("</tbody>");

        return super.parentHtml.append(subTotalHtml);
    }

    protected StringBuilder generateSubTotalHtml(TableSubtotalRow[] subTotalRow) {
        boolean isCrossTabReport = false;
        if (tableBldr.facade.container.getReportCollect().reportColViewbyValues != null && tableBldr.facade.container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) tableBldr.facade.container.getRetObj()).crosstabMeasureId;
        TableSubtotalRow row;
        StringBuilder subTotalHtml = new StringBuilder();
        String cssClass = "";
        int colCount;
        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;";
        String textAlign = "";
        String reportDrillUrl = "";
        String msrId = "";
        String adhocUrl = "";
        Type tarType;
        Gson gson = new Gson();
        for (int i = 0; i < subTotalRow.length; i++) {
            tarType = new TypeToken<List<String>>() {
            }.getType();
            List cellDataList = new ArrayList();
            row = subTotalRow[i];
            colCount = row.getColumnCount();
            subTotalHtml.append("<Tr ID=\"").append(row.getSubtotalType()).append("\">");
            if (tableBldr.isDrillAcrossSupported()) //                 added by prabal for removing sequence td
            {
                subTotalHtml.append("<td width=\"5\"/>");
            }

//                 if(!tableBldr.getAdhocDrillType().equalsIgnoreCase("none"))
//                subTotalHtml.append("<td width=\"5\"/>").append("</td>");
            cellDataList.add(row.getRowData(0));
            adhocUrl = "&CBOARP" + ((row.getID(0).replace("A_", "")).replace("_ST", "")) + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
//                
            if (!tableBldr.getAdhocDrillType().equalsIgnoreCase("none") && !tableBldr.isTransposed()) {
//                commented by manik to hide + adhoc drill sign
//                subTotalHtml.append("<td width=\"5\">");
//                subTotalHtml.append("<ul class=\"dropDownMenu\"><li><span class=\"ui-icon ui-icon-plusthick\"  title=\"Enable Adhoc Drill\" ></span><ul>");
//                 if(tableBldr.getReportParameters()!=null){
//                     tableBldr.getReportParameters().remove("Time Drill");//
//                     for(int k=0;k<tableBldr.getReportParameters().size();k++){
//                       //  
////                        if(!tableBldr.getReportParameters().get(k).equalsIgnoreCase(row.getID(0).replace("A_",""))){
//                            subTotalHtml.append("<li><a onclick=\"javascript:viewAdhocDrill('");
////                            subTotalHtml.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableBldr.getReportParameters().get(k)).append("','").append(row.getRowData(0)).append("','").append(row.getViewbyId()).append("','").append(row.getID(0).replace("_ST", "")).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(java.net.URLEncoder.encode(adhocUrl)).append("','").append(tableBldr.facade.getadhocParamUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(tableBldr.getReportParameterNames().get(k)).append("</a></li>");
//                            subTotalHtml.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableBldr.getReportParameters().get(k)).append("','").append(row.getRowData(0)).append("','").append(row.getViewbyId()).append("','").append(row.getID(0).replace("_ST", "")).append("','drillside','").append(adhocUrl).append("','").append(tableBldr.facade.getadhocParamUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(tableBldr.getReportParameterNames().get(k)).append("</a></li>");
////                           }//if
//                     }//for
//                 }
//                subTotalHtml.append("</ul></li></ul></td>");
            }
            /*
             * this code removed by prabal for removing sequence and select from
             * gt row
             */
//            if(tableBldr.isSerialNumDisplay())
//                subTotalHtml.append("<td width=\"5\"/>").append("</td>");

            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                HashMap<String, ArrayList<String>> fontHeaderMaps = null;
                ArrayList fontheaderpro = null;
                String headersize = "";
                int size = 12;
                String textColor = "";
                fontHeaderMaps = (tableBldr.facade.container.getTableColumnProperties() == null) ? new HashMap() : tableBldr.facade.container.getTableColumnProperties();
                msrId = row.getMeasrId(col);
                if (col >= tableBldr.facade.container.getViewByCount()) {
                    if (isCrossTabReport) {
                        if (crosstabMeasureId.containsKey(msrId) && crosstabMeasureId.get(msrId) != null) {
                            if (tableBldr.facade.getTextAlign(col, crosstabMeasureId.get(msrId).toString()) != null) {
                                textAlign = tableBldr.facade.getTextAlign(col, crosstabMeasureId.get(msrId).toString());
                            } else {
                                textAlign = "right";
                            }
                        } else {
                            textAlign = "right";
                        }
                    } else {
                        if (tableBldr.facade.getTextAlign(col, msrId) != null) {
                            textAlign = tableBldr.facade.getTextAlign(col, msrId);
                        } else {
                            textAlign = "right";
                        }
                    }
                }
                if (col >= tableBldr.facade.container.getViewByCount()) {
                    if (isCrossTabReport) {
                        if (fontHeaderMaps != null && !fontHeaderMaps.isEmpty()) {
                            if (crosstabMeasureId.containsKey(msrId) && crosstabMeasureId.get(msrId) != null) {
                                String ColId = (String) crosstabMeasureId.get(msrId);
                                if (fontHeaderMaps.containsKey(ColId)) {
                                    fontheaderpro = fontHeaderMaps.get(ColId);
                                    headersize = fontheaderpro.get(0).toString();
                                    size = Integer.parseInt(headersize.trim());
                                }
                            }
                        }
                        if (crosstabMeasureId.get(msrId) != null) {//added by Dinanath for handling null   
                            textColor = tableBldr.gettextColor(crosstabMeasureId.get(msrId).toString());
                        } else {
                            textColor = "";
                        }

                    } else {
                        if (fontHeaderMaps != null && !fontHeaderMaps.isEmpty()) {
                            if (fontHeaderMaps.containsKey(msrId)) {
                                fontheaderpro = fontHeaderMaps.get(msrId);
                                headersize = fontheaderpro.get(0).toString();
                                size = Integer.parseInt(headersize.trim());
                            }
                        }
                        textColor = tableBldr.gettextColor(msrId);
                    }
                }
                if (tableBldr.getMeasureDrillType() != null && !tableBldr.getMeasureDrillType().equalsIgnoreCase("null") && tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrill") && row.reportDrillList != null && col < row.reportDrillList.size()) {
                    reportDrillUrl = row.getReportDrillList(col);
                      if ( ! "#".equals(reportDrillUrl) ){
                    reportDrillUrl=row.getReportDrillList(col)+"&reportDrill=Y";
                }
                }
                cssClass = row.getCssClass(col);
//                 textAlign = row.getTextAlign(col);
//                 if(textAlign==null || textAlign.equalsIgnoreCase("")){
//                     textAlign = "center";
//                 }
                // 
                //Added by Ram for None ViewBy 08Desc2015
                if (msrId.equalsIgnoreCase("A_None")) {
                    subTotalHtml.append("<Td  ID=\"").append(row.getID(col)).append("\" ").append("align=\"").append(textAlign).append("\" ").append("class=\"").append(cssClass).append("\"").append(" style=\"font-size:" + size + "px'; display:").append("none").append(";").append("\">").append("<Font style=\" font-size:" + size + "px;color:" + textColor + ";\">");
                } else {//edited by mohit Gupta for gt and sub tatal background color display
                    if (row.getSubtotalType().equalsIgnoreCase("GT")) {
                        String gtColor = tableBldr.facade.container.getGrandTotalBGColor();
                        String subColor = tableBldr.facade.container.getSubTotalBGColor();
                        String resetColorAll = tableBldr.facade.container.getReSetColor();

                        if (gtColor == null) {
                            gtColor = "";
                        } else {
                            gtColor = gtColor;
                        }
                        if (subColor == null) {
                            subColor = "";
                        } else {
                            subColor = subColor;
                        }
                        if (resetColorAll != null) {
                            if (resetColorAll.equals("Y")) {
                                subColor = "background-color: #f1f1f1";
                                gtColor = "background-color: #f1f1f1";
                            }

                        }

                        subTotalHtml.append("<Td  ID=\"").append(row.getID(col)).append("\" ").append("align=\"").append(textAlign).append("\" ").append("class=\"").append(cssClass).append("\"").append("style=\'display:" + row.getDisplayStyle(col) + "; font-size:" + size + "px; background-color:" + gtColor + "; border-bottom: 1px solid gray; '").append("\">").append("<Font style=\"font-size:" + size + "px;color:" + textColor + ";\">");
                        // .append(" style=\"display:").append(row.getDisplayStyle(col)).append(";")
//                         .append(" style=\"display:").append(row.getDisplayStyle(col)).append(";").append("font-size:").append(size).append("px;").append("background-color:").append(gtColorNew).append(";").append("border-bottom: 1px solid gray").append(";")
//                       .append("style=\'display:"+row.getDisplayStyle(col)+"; font-size:"+size+"px; background-color:"+gtColorNew+"; border-bottom: 1px solid gray;').

                    } else {
                        String subColor = tableBldr.facade.container.getSubTotalBGColor();
                        String resetColorAll = tableBldr.facade.container.getReSetColor();
                        if (row.getSubtotalType().equalsIgnoreCase("ST")) {

                            if (subColor == null) {
                                subColor = "";
                            } else {
                                subColor = subColor;
                            }
                        }
                        if (resetColorAll != null) {
                            if (resetColorAll.equals("Y")) {
                                subColor = "background-color: #f1f1f1";

                            }

                        }
                        subTotalHtml.append("<Td  ID=\"").append(row.getID(col)).append("\" ").append("align=\"").append(textAlign).append("\" ").append("class=\"").append(cssClass).append("\"") // .append(" style=\"display:").append(row.getDisplayStyle(col)).append(";")
                                .append("style=\'display:" + row.getDisplayStyle(col) + ";font-size:" + size + "px;background-color:" + subColor + ";border-bottom: 1px solid gray;'").append("\">").append("<Font style=\" font-size:" + size + "px;color:" + textColor + ";\">");

                    }
                }
                String Rowdata = row.getRowData(col);
                if (Rowdata.equalsIgnoreCase("-0") || Rowdata.equalsIgnoreCase("-0.") || Rowdata.equalsIgnoreCase("-00.") || Rowdata.equalsIgnoreCase("-0.0") || Rowdata.equalsIgnoreCase("-0.00")) {
                    Rowdata = "0";
                }
                if (Rowdata.equalsIgnoreCase("-00.0") || Rowdata.equalsIgnoreCase("-00.00") || Rowdata.equalsIgnoreCase("-00.") || Rowdata.equalsIgnoreCase("-00")) {
                    Rowdata = "0";
                }
                if (Rowdata.contains("-00.000") || Rowdata.contains("-0.000")) {
                    Rowdata = "0";
                }

                if (tableBldr.getMeasureDrillType() != null && tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrill") && row.reportDrillList != null && col < row.reportDrillList.size()) {
                    if (!"#".equals(reportDrillUrl)) {
                      if(tableBldr.getMsrDrillReportSelection().equalsIgnoreCase("multi report")){
                        reportDrillUrl=reportDrillUrl.replace("&REPORTID="+tableBldr.getReportDrillMap(row.getMeasrId(col)),"");
                        subTotalHtml.append("<A href=\"javascript:multireportMsrDrill('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(java.net.URLEncoder.encode(reportDrillUrl)).append("','").append(row.getMeasrId(col)).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(Rowdata).append("</A>");
                        }
                       else
                       subTotalHtml.append("<A href=\"javascript:parent.submiturlsinNewTab('").append(java.net.URLEncoder.encode(reportDrillUrl)).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(Rowdata).append("</A>");
                     //   subTotalHtml.append(Rowdata);
                    } else {


                        subTotalHtml.append(Rowdata).append("</Font>");
                    }
                } else {
                    subTotalHtml.append(Rowdata).append("</Font>");
                }
                subTotalHtml.append(spaceAftrNo);
                subTotalHtml.append("</Td>");
            }
            subTotalHtml.append("</Tr>");
        }
        return subTotalHtml;
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }
}
