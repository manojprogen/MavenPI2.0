/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.progen.query.RTMeasureElement;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import prg.util.PbDisplayLabel;

/**
 *
 * @author progen
 */
public class TableHeaderDisplay extends TableDisplay {

    public static Logger logger = Logger.getLogger(TableHeaderDisplay.class);

    public TableHeaderDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public void setHeadlineflag(String Headlineflag) {
        super.headlineflag = Headlineflag;
    }

    public String getHeadlineflag() {
        return super.headlineflag;
    }

    @Override
    public void setFromOneviewflag(String fromOneview) {
        super.fromOneviewflag = fromOneview;
    }

    public String getFromOneviewflag() {
        return super.fromOneviewflag;
    }

    @Override
    public StringBuilder generateHTML() {

        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        Boolean flag11 = false;

        String imageSetting = "";
        String headerBgColorNew = "";
        String clickInformation = "";
        String elmntInformationNew = "";
        String tableParameter="";
        clickInformation = tableBldr.facade.container.getONClickInformation();
        String elmntInformation = tableBldr.facade.container.getElementIdvalue();
        String resetColorAll = tableBldr.facade.container.getReSetColor();
        String clearInformation = tableBldr.facade.container.getclearInformation();
        String sortType = tableBldr.facade.container.getSubtotalsort();
        String elmntSub = tableBldr.facade.container.getIdsubtotal();
        String refreshSortImage = tableBldr.facade.container.getRefreshGr();
        if (elmntInformation != null) {
            elmntInformationNew = elmntInformation.replace("A_", "");

        }
        String headerBgColor = tableBldr.facade.container.getHeaderBgColor();
        try {
            if (headerBgColor != null && headerBgColor != "") {
                if (!headerBgColor.contains("null") && !headerBgColor.contains("##") && !headerBgColor.equals("#ffffff") && !headerBgColor.contains("ffffff")) {
                    headerBgColorNew = "background-color:" + headerBgColor;

                } else {
                    headerBgColorNew = "background-color: #f1f1f1";
                }
            } else {
                headerBgColorNew = "background-color: #f1f1f1";
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

////edited by manik for popup
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        int wid = toolkit.getScreenSize().width;
//        int hei = toolkit.getScreenSize().height;
//     //   
//        int height = (hei / 2);
//      //  

        StringBuilder header = new StringBuilder();
        String finalrepDrillUrl = tableBldr.facade.getContextPath();

//        header.append("<div class=\"scrollable\" id=\"progenTableDiv\" style=\"display:'';width:1248px;height: 680px;overflow:auto\">");
//        header.append("<div class=\"scrollable\" id=\"progenTableDiv\" style=\"display:'';width:98%;height: " + height + "px;overflow:auto\">");
//end
//        header.append("<div class=\"scrollable\" id=\"progenTableDiv\" style=\"display:'';width:98%;height:auto;overflow:auto\">");

        header.append("<Table ID=\"progenTable\" class=\"prgtable\"  style=\"width:100%;height:0%;\" CELLPADDING=\"0\" CELLSPACING=\"0\">");
        header.append("<thead id=\"theaddiv\">");
        String heading;
        String id;
        String sortImage;
        int rowSpan;
        int colSpan;
        String dispStyle;
        int rowIndex = 0;
        int layer;
        String mcolor = "";
        String align;
        String backGorundColoroNclick = "";
        String headerrLinks;
        String headerrRepLinks;
        String elementId = null;
        String def_id = "";

        boolean isCrossTabReport = false;
        PbDisplayLabel dispLab = PbDisplayLabel.getPbDisplayLabel();

        HashMap<String, String> crossalignment = new HashMap<String, String>();
        crossalignment = tableBldr.facade.container.getcrossalign();
        //modified by Dinanath
        String separator;
        if (crossalignment == null) {
            separator = null;
        } else {
            separator = crossalignment.get("l1") + crossalignment.get("l2");
        }
         boolean isDimension;
        String l1Separator = "";
        String l1HSeparator = "";
        String l2Separator = "";

        if (separator != null && separator != "") {
            switch (separator.substring(0, 1)) {
                case "H":
                    l1HSeparator = "border-bottom:1px solid #808080";
                    break;
                case "V":
                    l1Separator = "border-right:1px solid #808080";
                    break;
                case "B":
                    l1Separator = "border-right:1px solid #808080;border-bottom:1px solid #808080;";
                    break;
                default:
                    break;
            }

            switch (separator.substring(1, 2)) {
                case "E":
                    l2Separator = "border-right:1px solid #808080";
                    break;
                default:
                    break;
            }
            if (separator.equals("HE")) {
                l1HSeparator = "";
                l1Separator = "border-bottom:1px solid #808080";
            }
        }
        if (resetColorAll != null) {
            if (resetColorAll.equals("Y")) {
                headerBgColorNew = "background-color: #f1f1f1";
            }
        }
        if (dispLab != null) {

            def_id = dispLab.getDefaultCompanyId();
        }

        // String def_id = tableBldr.facade.container.getDefaultCompanyId();
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) tableBldr.facade.container.getRetObj()).crosstabMeasureId;
        if (tableBldr.facade.container.getReportCollect().reportColViewbyValues != null && tableBldr.facade.container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        String eId = null;
        if (tableBldr.isSplitby()) {
            colCount += tableBldr.getInceresedColcount();
        }
        //start of code by mayank for multi view by transpose..
        int countRecords = 0;
        int rowCount = headerRows.length;
        int rCnt = 0;
        for (TableHeaderRow headerRow : headerRows) {
            rCnt++;
            int countmeasure = tableBldr.facade.container.getTableMeasureNames().size();
            countmeasure = countmeasure + tableBldr.getViewByCount() - 1;
            // 
            if (tableBldr.isTransposed()) {
                for (int row1 = 0; row1 <= tableBldr.facade.getViewByCount() - 1; row1++) {

                    header.append("<tr>");


                    if (tableBldr.isDrillAcrossSupported() && rowIndex == 0) {
                        header.append("<th rowspan='").append(headerRows.length).append("' class=\"tableHeaderStyle headerText\" style=\"" + headerBgColorNew + "\"  width=\"5\">Select</th>");
//                        rowIndex++;
                    }
//                    if (tableBldr.isSerialNumDisplay()) {
//                        header.append("<th class=\"tableHeaderStyle\"></th>");
//                    }
                    for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                        heading = headerRow.getRowData(countRecords);
                        if (heading.equalsIgnoreCase("") && tableBldr.facade.container.getReportCollect().ReportLayout.equalsIgnoreCase("KPI")) {
                            heading = "KPI";  //added by mohit for kpi and none
                        }
                        sortImage = headerRow.getSortImagePath(i);
                        id = headerRow.getID(i);
                        if( i < tableBldr.getViewByCount())
                            isDimension = true;
                            else
                          isDimension = false;
                        rowSpan = headerRow.getRowSpan(i);
                        colSpan = headerRow.getColumnSpan(i);
                        dispStyle = headerRow.getDisplayStyle(i);
                        layer = headerRow.getLayer();
                        eId = id.replace("_" + layer + "_H", "");
                        //added by sruthi for font color
                        if (isCrossTabReport && crosstabMeasureId.containsKey(eId)) {
                            mcolor = tableBldr.getmeasureColor(crosstabMeasureId.get(eId).toString());
                        } else {
                            mcolor = tableBldr.getmeasureColor(id.replace("_" + layer + "_H", ""));
                        }
                        //endedby sruthi
                        if (isCrossTabReport && crosstabMeasureId.containsKey(eId)) {
                           if(isDimension)
                            align=tableBldr.facade.getViewbyAlign(i, eId);
                            else
                            align = tableBldr.getmeasureAlign(crosstabMeasureId.get(eId).toString());
                        } else {
                            if(isDimension)
                               align=tableBldr.facade.getViewbyAlign(i, id.replace("_" + layer + "_H", ""));
                            else
                            align = tableBldr.getmeasureAlign(id.replace("_" + layer + "_H", ""));
                        }
                          //added by sruthi for bgcolor in tablecolumn pro
                        if (isCrossTabReport && crosstabMeasureId.containsKey(eId)) {
                            headerBgColor = tableBldr.getmeasurebgColor(crosstabMeasureId.get(eId).toString());
                            if (headerBgColor != null && headerBgColor != "") {
                           if (!headerBgColor.contains("null") && !headerBgColor.contains("##") && !headerBgColor.equals("#ffffff") && !headerBgColor.contains("ffffff")) {
                         headerBgColorNew = "background-color:" + headerBgColor;
                        } else {
                         headerBgColorNew = "background-color: #f1f1f1";
                            }
                         } else {
                            headerBgColorNew = "background-color: #f1f1f1";
                             }
                        } else {
                            headerBgColor = tableBldr.getmeasurebgColor(id.replace("_" + layer + "_H", ""));
                               if (headerBgColor != null && headerBgColor != "") {
                           if (!headerBgColor.contains("null") && !headerBgColor.contains("##") && !headerBgColor.equals("#ffffff") && !headerBgColor.contains("ffffff")) {
                         headerBgColorNew = "background-color:" + headerBgColor;
                        } else {
                         headerBgColorNew = "background-color: #f1f1f1";
                            }
                         } else {
                            headerBgColorNew = "background-color: #f1f1f1";
                             }

                        }
                        //endedby sruthi
                        headerrLinks = headerRow.getHeaderLinks();
                        headerrRepLinks = headerRow.getHeaderRepLinks();

//                 if(tableBldr.getisrenamed()!= null && tableBldr.getisrenamed().size()!=0){
                        String ChangedId = id;
                        ChangedId.replace("_" + layer + "_H", "");
                        ChangedId.replace("A_", "");
                        if (tableBldr.getisrenamed().get(id.replace("_" + layer + "_H", "")) != "" && tableBldr.getisrenamed().get(id.replace("_" + layer + "_H", "")) == null) {
//                 if(tableBldr.getmodifymeasureAttrChng()!=null){
//                  HashMap elmntmap = null;
//                   String elmntname = "";
//                   elmntmap = (HashMap)tableBldr.getmodifymeasureAttrChng().get(id.replace("_"+layer+"_H", ""));
//                        if(elmntmap != null){
//                           elmntname = String.valueOf(elmntmap.get("elementname"));
//                           if(elmntname!=null &&!elmntname.equalsIgnoreCase("") ){
//                                heading=elmntname;
//
//                           }
//                 }
//                 }
                        }
//                 }

                        header.append("<th ID=\"" + id + "\"  class=\"tableHeaderStyle\"  style=\"" + headerBgColorNew + ";display:" + dispStyle + ";\" rowspan=" + rowSpan + " colspan=" + colSpan + ">");
                        header.append("<table width=\"100%\" border=\"0\">");
                        header.append("<tr valign=\"top\">");
                        //header.append("<td valign=\"top\" style=\"background-color:#b4d9ee\">");
                        if (sortImage != null && !"".equals(sortImage)) {
                            if (tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")) != null && !tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")).equalsIgnoreCase("") && tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")) != null && !tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")).equalsIgnoreCase("%") && !tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")).equalsIgnoreCase("NF")) {
                                if (tableBldr.getmodifymeasureAttrChng() != null) {
                                    HashMap NFMap = null;
                                    HashMap symbolsMap = null;
                                    String Suffix_symbol = "";
//                          symbolsMap=tableBldr.getNFMap();
                                    NFMap = (HashMap) tableBldr.getmodifymeasureAttrChng().get(id.replace("_" + layer + "_H", ""));

                                    header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + "\" class=\"headerText gFontFamily gFontSize12\" align=\"" + align + "\">" + heading + "(" + tableBldr.getModifiedNumberFormatSymbol(tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", ""))) + ")");
//                             }
                                } else {
                                    header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + "\" class=\"headerText gFontFamily gFontSize12\" align=\"" + align + "\">" + heading + "(" + tableBldr.getModifiedNumberFormatSymbol(tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", ""))) + ")");
                                }
                                header.append("</td>");
                            } else {
                                if (i == 0 && tableBldr.isParameterDrill()) {
                                    if (headerrRepLinks != null) {
                                        header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + "\" class=\"headerText gFontFamily gFontSize12\" align=\"" + align + "\" width=\"100%\">");
                                        header.append("<A href=\"javascript:parent.submiturls('").append(headerrRepLinks).append("')\"").append("  target='_parent' style='text-decoration:none'>");
                                        header.append("<B style=\"" + headerBgColorNew + "\">" + heading + "</B>");
                                        header.append("</A>");
                                        header.append("</td>");

                                    } else {
                                        header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + "\"  class=\"headerText gFontFamily gFontSize12\" align=\"" + align + "\" width=\"100%\">");
                                        header.append("<A href=\"javascript:parent.submiturls('").append(headerrLinks).append("')\"").append("  target='_parent' style='text-decoration:none'>");
                                        header.append("<B style=\"" + headerBgColorNew + "\">" + heading + "</B>");
                                        header.append("</A>");
                                        header.append("</td>");
                                    }
                                } else {
                                    // header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" class=\"headerText\" align=\""+align+"\" width=\"100%\">" + heading + "</td>");
                                    // code written by swati
                                    if (i < tableBldr.getViewByCount()) {
                                        header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + "\"  class=\"headerText gFontFamily gFontSize12\" align=\"" + align + "\" width=\"100%\">");

                                        header.append("<A href=\"javascript:openDimDialog('").append(tableBldr.getContextPath()).append("','").append(id.replace("_0_H", "")).append("','").append(tableBldr.getReportId()).append("')\"").append("  target='_parent' style='text-decoration:none'>");
                                        header.append("<span><B style=\"" + headerBgColorNew + "\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span></B></span>");
                                        header.append("</A>");
                                        header.append("</td>");
                                    } else {
                                        header.append("<td valign=\"top\" style=\"" + headerBgColorNew + "\" id=\"tdRef_" + id + "\" class=\"headerText gFontFamily gFontSize12\" align=\"" + align + "\" width=\"100%\"><span style=\" " + backGorundColoroNclick + ";display:inline-table;\"> " + heading + "</span></td>");
                                    }
                                }
                            }
                            header.append("<td  valign=\"top\" align=\"right\" width=\"5%\" style=\"" + headerBgColorNew + "\" class=\"tabBgcolor\">").append("</td>");
                        } else {
                            header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + "\" class=\"headerText gFontFamily gFontSize12\" align=\"center\" width=\"100%\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span>");
                            header.append("</td>");
                            //header.append("<td  valign=\"top\" align=\"right\" width=\"1%\" style=\"background-color:#b4d9ee\">").append("</td>");
                        }
                        header.append("</tr>");
                        header.append("</table>");
                        header.append("</th>");
//            if(colCount==i+1)
//                colCount++;
                        countRecords++;
                    }
                    if (tableBldr.facade.isGrandTotalRequired()) {
                        //code modified by anitha for grand total
                        header.append("<th class=\"tableHeaderStyle\" style=\"").append(headerBgColorNew).append(";test-align:center;\" >Grand Total</th>");
                    }
                    header.append("</tr>");
                }
            } //end of code by mayank(TRANSPOSE)
            else {
                header.append("<tr>");
                if (!tableBldr.getAdhocDrillType().equalsIgnoreCase("none") && !tableBldr.isTransposed()) {
//            header.append("<th width=\"5\" class=\"tableHeaderStyle\"></th>");
//                commented by manik to hide + adhoc drill sign
//             header.append("<th width=\"5\" class=\"tableHeaderStyle\">");
//             header.append("<ul class=\"dropDownMenu\"><li><span class=\"ui-icon ui-icon-plusthick\"  title=\"Enable Adhoc Drill\" ></span><ul>");
//             if(headerRow.getReportParameters()!=null){
//                     for(int i=0;i<headerRow.getReportParameters().size();i++){
//                       //  
//                         if(headerRow.getReportParameterNames().contains(headerRow.getRowData(0)))
//                            elementId=headerRow.getReportParameters().get(headerRow.getReportParameterNames().indexOf(headerRow.getRowData(0)));
//                       // if(!headerRow.getReportParameters().get(i).equalsIgnoreCase(headerRow.getColumnId(0).replace("A_",""))){
//                            header.append("<li><a onclick=\"javascript:viewAdhocDrillforTableHeader('");
//                            header.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(headerRow.getReportParameters().get(i)).append("','").append(elementId).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(headerRow.getViewbyId()).append("','").append(headerRow.getViewbyCount()).append("','").append(tableBldr.isCrossTab()).append("','").append(headerRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(headerRow.getReportParameterNames().get(i)).append("</a></li>");
//                           // header.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(headerRow.getReportParameters().get(i)).append("','").append(headerRow.getViewbyId()).append("','").append(headerRow.getColumnId(headerRow.getViewbyCount()-1)).append("','").append(tableBldr.getAdhocDrillType()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(headerRow.getReportParameterNames().get(i)).append("</a></li>");
//                        // }//if
//                     }//for
//             }
//             header.append("</ul></li></ul></th>");
                }


//            if(tableBldr.isTransposed())
//            {
//                header.append("<th class=\"tableHeaderStyle\"></th>");
//            }
                if (tableBldr.isDrillAcrossSupported() && rowIndex == 0) {
                    header.append("<th rowspan='").append(headerRows.length).append("' class=\"tableHeaderStyle headerText\" style=\"" + headerBgColorNew + "\" width=\"5\">Select</th>");
                    rowIndex = 1;
                }
                if (tableBldr.isCrossTab()) {
                    header.append("<th rowspan='").append(headerRows.length).append("' class=\"tableHeaderStyle headerText\"  style=\"display:none;\"   width=\"5\">Select</th>");
                }



//                if (tableBldr.isSerialNumDisplay()) {
//                    header.append("<th class=\"tableHeaderStyle\"></th>");
//                }
                for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                    heading = headerRow.getRowData(i);
                    sortImage = headerRow.getSortImagePath(i);
                    id = headerRow.getID(i);
                    String headingNew = "";
                    HashMap<String, ArrayList<String>> fontHeaderMaps = null;
                    ArrayList fontheaderpro = null;
                    String headersize = "";
                    int size = 12;
                    //Code added by Amar for display label based on company on Oct 6 , 2015
                     if( i < tableBldr.getViewByCount())
                            isDimension = true;
                            else
                          isDimension = false;
                    if (rCnt == rowCount) {
                        String ele_id = "";

                        if (tableBldr.facade.container.isReportCrosstab()) {
                            if (i < tableBldr.getViewByCount()) {
                                String[] idPart = id.split("_");
                                ele_id = idPart[1];
                                if (dispLab != null && dispLab.getColDisplayWithCompany(def_id, ele_id) != null) {
                                    heading = dispLab.getColDisplayWithCompany(def_id, ele_id);
                                }
                            } else {
                                String[] idPart = id.split("_");
                                ele_id = idPart[0];
                                String nId = crosstabMeasureId.get(ele_id);
                                if (dispLab != null && nId != null && dispLab.getColDisplayWithCompany(def_id, nId.replace("A_", "")) != null) {
                                    heading = dispLab.getColDisplayWithCompany(def_id, nId.replace("A_", ""));
                                }
                            }
                        } else {
                            String[] idPart = id.split("_");
                            ele_id = idPart[1];
                            if (dispLab != null && dispLab.getColDisplayWithCompany(def_id, ele_id) != null) {
                                heading = dispLab.getColDisplayWithCompany(def_id, ele_id);
                            }
                        }
                    }

                    //end of code
//                 
//                 
                    rowSpan = headerRow.getRowSpan(i);
                    colSpan = headerRow.getColumnSpan(i);
                    dispStyle = headerRow.getDisplayStyle(i);
                    layer = headerRow.getLayer();
                    eId = id.replace("_" + layer + "_H", "");
                    ArrayList colViews = tableBldr.facade.container.getReportCollect().reportColViewbyValues; 
                    ArrayList CEPNames = (ArrayList) tableBldr.facade.container.getTableHashMap().get("CEPNames");
                    if (rCnt == rowCount) {
                        
                    }else{
                        if (tableBldr.facade.container.isReportCrosstab()) {
                            if (i < tableBldr.getViewByCount()) {
                                try{                                                                
                                if(i<colViews.size()&&rowCount>colViews.size() && heading.equalsIgnoreCase(CEPNames.get(rCnt-1).toString())){
                                    id = "A_"+colViews.get(rCnt-1)+"_" + layer + "_H";
                                    eId = id.replace("_" + layer + "_H", "");                                
                                }
                                }catch(Exception e){
                                    
                                }
                            }
                    }
                    }                    
                    //added by sruthi for headercolor
                    if (isCrossTabReport && crosstabMeasureId.containsKey(eId)) {
                        mcolor = tableBldr.getmeasureColor(crosstabMeasureId.get(eId).toString());
                    } else {
                        mcolor = tableBldr.getmeasureColor(id.replace("_" + layer + "_H", ""));
                    }
                    //ended by sruthi
                    if (isCrossTabReport && crosstabMeasureId.containsKey(eId)) {
                          if(isDimension)
                            align=tableBldr.facade.getViewbyAlign(i, eId);
                            else
                            align = tableBldr.getmeasureAlign(crosstabMeasureId.get(eId).toString());
                        } else {
                            if(isDimension)
                               align=tableBldr.facade.getViewbyAlign(i, id.replace("_" + layer + "_H", ""));
                            else
                            align = tableBldr.getmeasureAlign(id.replace("_" + layer + "_H", ""));
                    }
                    //added by sruthi for bg in tablecolumn pro
                      if (isCrossTabReport && crosstabMeasureId.containsKey(eId)) {
                            headerBgColor = tableBldr.getmeasurebgColor(crosstabMeasureId.get(eId).toString());
                            if (headerBgColor != null && headerBgColor != "") {
                           if (!headerBgColor.contains("null") && !headerBgColor.contains("##") && !headerBgColor.equals("#ffffff") && !headerBgColor.contains("ffffff")) {
                         headerBgColorNew = "background-color:" + headerBgColor;
                        } else {
                         headerBgColorNew = "background-color: #f1f1f1";
                            }
                         } else {
                            headerBgColorNew = "background-color: #f1f1f1";
                             }
                        } else {
                            headerBgColor = tableBldr.getmeasurebgColor(id.replace("_" + layer + "_H", ""));
                               if (headerBgColor != null && headerBgColor != "") {
                           if (!headerBgColor.contains("null") && !headerBgColor.contains("##") && !headerBgColor.equals("#ffffff") && !headerBgColor.contains("ffffff")) {
                         headerBgColorNew = "background-color:" + headerBgColor;
                        } else {
                         headerBgColorNew = "background-color: #f1f1f1";
                            }
                         } else {
                            headerBgColorNew = "background-color: #f1f1f1";
                             }

                        }//ended by sruthi
                    headerrLinks = headerRow.getHeaderLinks();
                    headerrRepLinks = headerRow.getHeaderRepLinks();
                    String eleid = eId.toString().replace("A_", "");
                    //added by Ram for None ViewBys
                    if (eleid.equalsIgnoreCase("None")) {
                        dispStyle = "None";
                    }
                    //ended by Ram 
                    String ulid = "filterview" + eleid;
                    String tdid = "tdRef_" + id;
//                 if(tableBldr.getisrenamed()!= null && tableBldr.getisrenamed().size()!=0){
                    String ChangedId = id;
                    ChangedId.replace("_" + layer + "_H", "");
                    ChangedId.replace("A_", "");
                    if (/*
                             * tableBldr.getisrenamed().get(id.replace("_"+layer+"_H",
                             * ""))!="true" &&
                             */tableBldr.getisrenamed().get(id.replace("_" + layer + "_H", "")) != "" && tableBldr.getisrenamed().get(id.replace("_" + layer + "_H", "")) == null) {
//                 if(tableBldr.getmodifymeasureAttrChng()!=null){
//                  HashMap elmntmap = null;
//                   String elmntname = "";
//                   elmntmap = (HashMap)tableBldr.getmodifymeasureAttrChng().get(id.replace("_"+layer+"_H", ""));
//                        if(elmntmap != null){
//                           elmntname = String.valueOf(elmntmap.get("elementname"));
//                           if(elmntname!=null &&!elmntname.equalsIgnoreCase("") ){
//                                heading=elmntname;
//
//                           }
//                 }
//                 }
                    }
//                 }
                    String flagviewby = "";
                    String reset = "false";
                    String submenu = "left";
                    String gtCrossTabLastButOneCol = "";
                    if (RTMeasureElement.isRunTimeMeasure(eId)) {
                        reset = "true";
                    }
                    if (i < tableBldr.getViewByCount()) {
                        flagviewby = "viewby";
                    } else {
                        flagviewby = "measure";
                    }
                    if (i <=4) {
                        submenu = "firstmeasure";
                    }
                    if (colCount - 1 == i) {
                        submenu = "lastmeasure";
                    }
                    //added by anitha for crosstab lastbutone measure
                    if (colCount - 2 == i) {
                        gtCrossTabLastButOneCol = "gtfirstmeasure";
                    }
                    //end of code by anitha for crosstab lastbutone measure

                    if (tableBldr.getViewByCount() > 1) {
                        if (i == 0) {
                            submenu = "groupmeasure";
                        }
                    }
                    //  header.append("<th ID=\"" + id + "\"  class=\"tableHeaderStyle\"  style=\"display:"+dispStyle+";\" rowspan="+rowSpan+" colspan="+colSpan+">");
                    //Modified by Govardhan to hide the Measure heading and should have only column view by values as heading
                    if (tableBldr.facade.container.isReportCrosstab() && i >= tableBldr.getViewByCount() && headerRow == headerRows[0] && tableBldr.facade.container.isHideMsrHeading() && tableBldr.facade.container.isReportCrosstab()) {
                        header.append("<th ID=\"" + id + "\"  class='tableHeaderStyle repTblMnuPr'  style=\"" + headerBgColorNew + " ;display:" + dispStyle + "; \" rowspan=2 colspan=" + colSpan + ">");
                        header.append("<ul class='dropdown-menu repTblMnu hideDropDown ' id=\"filterview" + eleid + "\" style='background-color:#fdfdfd;width:auto;height:auto;left:;top:;' ></ul>");

                    } else {

                        if (tableBldr.facade.container.isReportCrosstab() && headerRows.length > 1 && i >= tableBldr.getViewByCount() && headerRow == headerRows[1] && tableBldr.facade.container.isHideMsrHeading()) {
                            header.append("<th ID=\"" + id + "\"  class='tableHeaderStyle repTblMnuPr'  style=\"" + headerBgColorNew + " ;display:none; \" rowspan=" + rowSpan + " colspan=" + colSpan + ">");

                        } else {
                            if (tableBldr.facade.container.isReportCrosstab()) {
                                if (i < tableBldr.getViewByCount()) {
//     sortImage="<img style='width: 11px; height: 11px;' class='dropdown-toggle' src='"+tableBldr.getContextPath()+"/images/arrow_down.png' alt='Options' name=\""+eleid+"\" onclick='showfilterview(\""+heading+"\",\""+eleid+"\",\""+ulid+"\",\""+tdid+"\",\""+reset+"\")'>";
                                    //header.append("<th ID=\"" + id + "\"  class=\"tableHeaderStyle repTblMnuPr\"  style=\"border-right:1px solid #000; display:"+dispStyle+";\" rowspan="+rowSpan+" colspan="+colSpan+">");
                                    if (headerRow.getLayer() == 0) {
                                        header.append("<th ID=\"" + id + "\"  class='tableHeaderStyle repTblMnuPr'  style=\"" + headerBgColorNew + " ;" + l1Separator + " ;display:" + dispStyle + ";\" rowspan=" + rowSpan + " colspan=" + colSpan + ">");
                                    } else {
                                        header.append("<th ID=\"" + id + "\"  class='tableHeaderStyle repTblMnuPr'  style=\"" + headerBgColorNew + ";" + l2Separator + " ;display:" + dispStyle + "; \" rowspan=" + rowSpan + " colspan=" + colSpan + ">");
                                    }
                                    if (rCnt == rowCount) {
                                        header.append("<ul  class='dropdown-menu repTblMnu hideDropDown' id=\"filterview" + eleid + "\" style='background-color:#fdfdfd;width:auto;height:auto;left:;top:;' ></ul>");
                                    } else if (tableBldr.facade.container.isReportCrosstab()&&i < tableBldr.getViewByCount()&&i<colViews.size()&&rowCount>colViews.size()&&CEPNames.size()>= rCnt&& heading.equalsIgnoreCase(CEPNames.get(rCnt-1).toString())){
                                         if(rowCount-1==rCnt){
                                        flagviewby = "ctviewby";
                                          header.append("<ul  class='dropdown-menu repTblMnu hideDropDown' id=\"filterview" + eleid + "\" style='background-color:#fdfdfd;width:auto;height:auto;left:;top:;' ></ul>");  
                                    }
                                    }
                                } else {

                                    if (countmeasure == i || rCnt == 1) {
                                        //header.append("<th ID=\"" + id + "\"  class='tableHeaderStyle repTblMnuPr'  style=\"border-right:1px solid #000; display:" + dispStyle + ";\" rowspan=" + rowSpan + " colspan=" + colSpan + ">");
                                        if (headerRow.getLayer() == 0) {
                                            header.append("<th ID=\"" + id + "\"  class='tableHeaderStyle repTblMnuPr'  style=\"" + headerBgColorNew + ";" + l1Separator + "; display:" + dispStyle + ";\" rowspan=" + rowSpan + " colspan=" + colSpan + ">");
                                        } else {
                                            header.append("<th ID=\"" + id + "\"  class='tableHeaderStyle repTblMnuPr'  style=\"" + headerBgColorNew + ";" + l2Separator + "; display:" + dispStyle + ";\" rowspan=" + rowSpan + " colspan=" + colSpan + ">");
                                        }
                                        if (countmeasure == i) {
                                            header.append("<ul class='dropdown-menu repTblMnu hideDropDown' id=\"filterview" + eleid + "\" style='background-color:#fdfdfd;width:auto;height:auto;left:;top:;' ></ul>");
                                        }
                                        countmeasure = countmeasure + tableBldr.facade.container.getTableMeasureNames().size();
                                    } else {
                                        header.append("<th ID=\"" + id + "\"  class=\"tableHeaderStyle repTblMnuPr\"  style=\"" + headerBgColorNew + "; display:" + dispStyle + ";\" rowspan=" + rowSpan + " colspan=" + colSpan + ">");
                                        header.append("<ul class='dropdown-menu repTblMnu hideDropDown' id=\"filterview" + eleid + "\" style='background-color:#fdfdfd;width:auto;height:auto;left:;top:;' ></ul>");
                                    }

                                }
                            } else {

//                             if(i < tableBldr.getViewByCount()){

//     sortImage="<img style='width: 11px; height: 11px;' class='dropdown-toggle' src='"+tableBldr.getContextPath()+"/images/arrow_down.png' alt='Options' name=\""+eleid+"\" onclick='showfilterview(\""+heading+"\",\""+eleid+"\",\""+ulid+"\",\""+tdid+"\",\""+reset+"\")'>";
//                            }
//                 Edited by Faiz Ansari
                                header.append("<th ID=\"" + id + "\"  class='tableHeaderStyle repTblMnuPr'  style=\"" + headerBgColorNew + ";display:" + dispStyle + ";\" rowspan=" + rowSpan + " colspan=" + colSpan + ">");
                                header.append("<ul class='dropdown-menu repTblMnu hideDropDown' id=\"filterview" + eleid + "\" style='background-color:#fdfdfd;width:auto;height:auto;left:;top:;' ></ul>");
//                 End!!!
                            }
                        }
                    }
                    //end of code by Govardhan
                    header.append("<table width=\"100%\" height=\"100%\" border=\"0\">");
                    header.append("<tr valign=\"top\">");
                    //header.append("<td valign=\"top\" style=\"background-color:#b4d9ee\">");
                    if (sortImage != null && !"".equals(sortImage)) {
                        if (tableBldr.isCrossTab()) {
                            imageSetting = "vertical-align:top";
                        } else {

                            imageSetting = "vertical-align:middle";
                        }

                        //Added By manoj
                        try {

                              if (clickInformation != null && !clickInformation.isEmpty() && elmntInformationNew != null && !elmntInformationNew.isEmpty()) {
                                if (clickInformation.equals("Progen") && eleid.equals(elmntInformationNew) && clearInformation.equals("true")) {
                                    backGorundColoroNclick = "border-bottom:3px solid #8BC34A;";
                                    flag11 = true;

                                } else {
                                    backGorundColoroNclick = "";
                                }
                            }else if(i==0){
                                 backGorundColoroNclick = "border-bottom:3px solid #8BC34A;";
                            }else {
                                backGorundColoroNclick = "border-bottom:";
                            }
                             if (refreshSortImage != null && refreshSortImage.equals("true")){
                                backGorundColoroNclick = "border-bottom:";
                                }
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                        }
                        //added by sruthi for tablecolum pro
                        String elementid = tableBldr.facade.container.getDisplayColumns().get(i);
                        String ColId = elementid.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_rankST", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "");
                        fontHeaderMaps = (tableBldr.facade.container.getTableColumnProperties() == null) ? new HashMap() : tableBldr.facade.container.getTableColumnProperties();
                        if (tableBldr.facade.container.isReportCrosstab()) {
                            if (fontHeaderMaps != null && !fontHeaderMaps.isEmpty()) {
                                if (crosstabMeasureId != null && crosstabMeasureId.containsKey(elementid)) {
                                    ColId = (String) crosstabMeasureId.get(elementid);
                                    if (fontHeaderMaps.containsKey(ColId)) {
                                        fontheaderpro = fontHeaderMaps.get(ColId);
                                        headersize = (String) fontheaderpro.get(1);
                                        size = Integer.parseInt(headersize.trim());
                                    }
                                }
                            }
                        } else {
                            if (fontHeaderMaps != null && !fontHeaderMaps.isEmpty()) {
                                if (fontHeaderMaps.containsKey(ColId)) {
                                    fontheaderpro = fontHeaderMaps.get(ColId);
                                    headersize = (String) fontheaderpro.get(1);
                                    size = Integer.parseInt(headersize.trim());
                                }
                            }
                        }//ended by sruthi
                        if (tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")) != null && !tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")).equalsIgnoreCase("") && tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")) != null && !tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")).equalsIgnoreCase("%") && !tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", "")).equalsIgnoreCase("NF")) {
                            if (tableBldr.getmodifymeasureAttrChng() != null) {
                                HashMap NFMap = null;
                                HashMap symbolsMap = null;
                                String Suffix_symbol = "";
//                          symbolsMap=tableBldr.getNFMap();
                                NFMap = (HashMap) tableBldr.getmodifymeasureAttrChng().get(id.replace("_" + layer + "_H", ""));
                                //added by sruthi for numberformat
//                            ArrayList singleColProp = null;
//                            HashMap ColumnProperties = null;
//                          ColumnProperties=  (tableBldr.facade.container.getColumnProperties() == null) ? new HashMap() : tableBldr.facade.container.getColumnProperties();
//                             singleColProp = (ArrayList) ColumnProperties.get(id.replace("_"+layer+"_H", ""));
//                            String  numberformate=String.valueOf(singleColProp.get(9));
                                //String elementid = tableBldr.facade.container.getDisplayColumns().get(i);
                                //String ColId = elementid.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_rankST", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
                                String numberformate = "N";
                                HashMap<String, String> customHeader = null;
                                String customdata = "";//added by sruthi for custom header
                                ArrayList singleColProp = null;
                                HashMap ColumnProperties = null;

                                ColumnProperties = (tableBldr.facade.container.getColumnProperties() == null) ? new HashMap() : tableBldr.facade.container.getColumnProperties();
                                customHeader = (tableBldr.facade.container.getCustomHeader() == null) ? new HashMap() : tableBldr.facade.container.getCustomHeader();
                                fontHeaderMaps = (tableBldr.facade.container.getTableColumnProperties() == null) ? new HashMap() : tableBldr.facade.container.getTableColumnProperties();
                                if (tableBldr.facade.container.isReportCrosstab()) {
                                    if (crosstabMeasureId != null && crosstabMeasureId.containsKey(elementid)) {
                                        ColId = (String) crosstabMeasureId.get(elementid);
                                    } else {
                                        ColId = elementid;
                                    }
                                    ////added by sruthi for numberformate
                                    if (ColumnProperties.containsKey(ColId)) {
                                        singleColProp = (ArrayList) ColumnProperties.get(ColId);
                                        numberformate = singleColProp.get(9).toString();
                                        if (customHeader.containsKey(ColId))//added by sruthi for custom header
                                        {
                                            // if(customHeader.get(ColId)!=null && customHeader.get(ColId)!="null")
                                            customdata = customHeader.get(ColId);

                                        }
                                    }
                                    if (fontHeaderMaps != null && !fontHeaderMaps.isEmpty()) {
                                        if (fontHeaderMaps.containsKey(ColId)) {
                                            fontheaderpro = fontHeaderMaps.get(ColId);
                                            headersize = (String) fontheaderpro.get(1);
                                            size = Integer.parseInt(headersize.trim());
                                        }
                                    }//ended by sruthi
                                    //ended by sruthi
                                } else {
                                    ColId = elementid;
                                    ////added by sruthi for numberformate
                                    singleColProp = (ArrayList) ColumnProperties.get(ColId);
                                    numberformate = singleColProp.get(9).toString();
                                    if (customHeader.containsKey(ColId))//added by sruthi for custom header
                                    {
                                        // if(customHeader.get(ColId)!=null){
                                        customdata = customHeader.get(ColId);//ended by sruthi
                                        // }
                                    }
                                    if (fontHeaderMaps != null && !fontHeaderMaps.isEmpty()) {
                                        if (fontHeaderMaps.containsKey(ColId)) {
                                            fontheaderpro = fontHeaderMaps.get(ColId);
                                            headersize = fontheaderpro.get(1).toString();
                                            // if(!headersize.equalsIgnoreCase("10")){
                                            size = Integer.parseInt(headersize.trim());
                                            //}
                                        }
                                    }//ended by sruthi
                                    //String  numberformate=String.valueOf(singleColProp);
                                }
//ended by sruthi
//                        if(NFMap != null){
//                            Suffix_symbol=symbolsMap.get(id.replace("_"+layer+"_H", "")).toString();
//                            if(Suffix_symbol==null){
//                            Suffix_symbol=String.valueOf(NFMap.get("suffix_symbol"));
//                             if(Suffix_symbol.equalsIgnoreCase("") || Suffix_symbol.equalsIgnoreCase("none")){
//                                   Suffix_symbol = String.valueOf(NFMap.get("no_format"));
//                             }
//                            }
//
////                           if(!Suffix_symbol.equalsIgnoreCase("")&& !Suffix_symbol.equalsIgnoreCase("none")){
////                               String headerSuffix="";
////                               if(Suffix_symbol.equalsIgnoreCase("k")){
////                                   headerSuffix="Thousands";
////                               }else if(Suffix_symbol.equalsIgnoreCase("L")){
////                                    headerSuffix="Lakhs";
////                               }else if(Suffix_symbol.equalsIgnoreCase("M")){
////                                    headerSuffix="Millions";
////                               }else if(Suffix_symbol.equalsIgnoreCase("c")||Suffix_symbol.equalsIgnoreCase("cr")){
////                                    headerSuffix="Crores";
////                               }
////                               header.append("<td valign=\"top\" align=\"center\" id=\"tdRef_" + id + "\" class=\"headerText\" >" + heading+ "("+headerSuffix+")" );
////                           }else{
//                               header.append("<td valign=\"top\" align=\"center\" id=\"tdRef_" + id + "\" class=\"headerText\" >"+heading );
////                           }
//                           }else{
                                //added by sruthi for numberformate
                                if (!tableBldr.getModifiedNumberFormatSymbol(tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", ""))).isEmpty() && tableBldr.getModifiedNumberFormatSymbol(tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", ""))) != null && numberformate.equalsIgnoreCase("Y")) {
                                    if (!customdata.equalsIgnoreCase(""))//added by sruthi for custom header
                                    {
                                        header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\" class=\"headerText \" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' align=\"" + align + "\"style=\" font-size:" + size + "px;\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span>" + "(" + customdata + ")"+"</Font>");
                                    } else //ended by sruthi
                                    {
                                        tableParameter="true";
                                        header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\" class=\"headerText\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' align=\"" + align + "\"style=\" font-size:" + size + "px;\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span>" + "(" + tableBldr.getModifiedNumberFormatSymbol(tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", ""))) + ")"+"</Font>");
                                    }
                                } else {
                                    header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText\" align=\"" + align + "\" style=\" font-size:" + size + "px;\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span>"+"</Font>");
                                }
                                //ended by sruthi
//                             }
                            } else {
                                header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText\" align=\"" + align + "\" style=\" font-size:" + size + "px;\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span>(" + tableBldr.getModifiedNumberFormatSymbol(tableBldr.getNumberSymbol(id.replace("_" + layer + "_H", ""))) + ")"+"</Font>");
                            }
                            header.append("</td>");
                        } else {
                            if (i == 0 && tableBldr.isParameterDrill()) {
                                if (headerrRepLinks != null) {
                                    header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText\" align=\"" + align + "\" width=\"100%\" style=\" font-size:" + size + "px;\">");
//                         commented by manik for adhoc drill(start of code)
//                            header.append("<A href=\"javascript:parent.submiturls('").append(headerrRepLinks).append("')\"").append("  target='_parent' style='text-decoration:none'>");
//                            header.append("<B>"+heading+"</B>");
//                            header.append("</A>");
                                    header.append("<ul class=\"dropDownMenuad1\"><li style=style=\"" + headerBgColorNew + ";\" class=\"headerText\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span></Font><ul>");
                                    if (headerRow.getReportParameters() != null) {
                                        for (int j = 0; j < headerRow.getReportParameters().size(); j++) {
                                            if (headerRow.getReportParameterNames().contains(headerRow.getRowData(0))) {
                                                elementId = headerRow.getReportParameters().get(headerRow.getReportParameterNames().indexOf(headerRow.getRowData(0)));
                                            }
                                            header.append("<li><a onclick=\"javascript:viewAdhocDrillforTableHeader('");
                                            header.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(headerRow.getReportParameters().get(j)).append("','").append(elementId).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(headerRow.getViewbyId()).append("','").append(headerRow.getViewbyCount()).append("','").append(tableBldr.isCrossTab()).append("','").append(headerRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(headerRow.getReportParameterNames().get(j)).append("</a></li>");
                                        }
                                    }
                                    header.append("</ul></li></ul>");
                                    header.append("</td>");

                                } else {
                                    header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText\" align=\"" + align + "\" width=\"100%\"style=\" font-size:" + size + "px;\">");
//                        commented by manik for adhoc drill(start of code)
//                            header.append("<A href=\"javascript:parent.submiturls('").append(headerrLinks).append("')\"").append("  target='_parent' style='text-decoration:none'>");
//                            header.append("<B>"+heading+"</B>");
//                            header.append("</A>");
                                    header.append("<ul class=\"dropDownMenuad1\"><li style=\"" + headerBgColorNew + " class=\"headerText \"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span></Font><ul>");
                                    if (headerRow.getReportParameters() != null) {
                                        for (int j = 0; j < headerRow.getReportParameters().size(); j++) {
                                            if (headerRow.getReportParameterNames().contains(headerRow.getRowData(0))) {
                                                elementId = headerRow.getReportParameters().get(headerRow.getReportParameterNames().indexOf(headerRow.getRowData(0)));
                                            }
                                            header.append("<li><a onclick=\"javascript:viewAdhocDrillforTableHeader('");
                                            header.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(headerRow.getReportParameters().get(j)).append("','").append(elementId).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(headerRow.getViewbyId()).append("','").append(headerRow.getViewbyCount()).append("','").append(tableBldr.isCrossTab()).append("','").append(headerRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(headerRow.getReportParameterNames().get(j)).append("</a></li>");
                                        }
                                    }
                                    header.append("</ul></li></ul>");
                                    //end of code written by manik
                                    header.append("</td>");
                                }
                            } else {
                                // header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" class=\"headerText\" align=\""+align+"\" width=\"100%\">" + heading + "</td>");
                                // code written by swati
                                if (i < tableBldr.getViewByCount()) {
                                    header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText\" align=\"" + align + "\" width=\"100%\" style=\" font-size:" + size + "px;\">");
//         commented by manik for adhoc drill(start of code)
//                            header.append("<A href=\"javascript:openDimDialog('").append(tableBldr.getContextPath()).append("','").append(id.replace("_0_H","")).append("','").append(tableBldr.getReportId()).append("')\"").append("  target='_parent' style='text-decoration:none'>");
//                            header.append("<span><B>"+heading+"</B></span>");
//                            header.append("</A>");
                                    header.append("<ul class=\"dropDownMenuad1\"><li style=\"" + headerBgColorNew + ";\" class=\"headerText \"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span></Font><ul>");


//added by dinanath for adhoc drill
                                    int p = 0;
                                    List<Integer> al = new ArrayList<Integer>();
                                    if (headerRow.getReportParameters() != null) {
                                        while (p < headerRow.getReportParameters().size()) {
                                            al.add(headerRow.getReportParameterNames().get(p).length());
                                            p++;
                                        }
                                    }
                                    //al.add(12);
                                    int max = 0;



                                    if (headerRow.getReportParameters() != null) {
                                        for (int j = 0; j < headerRow.getReportParameters().size(); j++) {
                                            if (headerRow.getReportParameterNames().contains(headerRow.getRowData(0))) {
                                                elementId = headerRow.getReportParameters().get(headerRow.getReportParameterNames().indexOf(headerRow.getRowData(0)));
                                            }

                                            for (int h = 2; h < al.size() + 2; h = h + 3) {
                                                max = al.get(h - 2);
                                                for (int s = h - 2; s <= h && s < al.size(); s++) {
                                                    if (al.get(s) >= max) {
                                                        max = al.get(s);
                                                    }
                                                }

                                                int g = max + 12;
                                                for (int s = h - 2; s <= h && s < al.size(); s++) {
                                                    String headingNew1 = headerRow.getReportParameterNames().get(s);
                                                    try {
                                                        //code modified by Amar for display label on Oct 8, 2015
                                                        if (dispLab != null && dispLab.getColDisplayWithCompany(def_id, headerRow.getReportParameters().get(s)) != null) {
                                                            headingNew1 = dispLab.getColDisplayWithCompany(def_id, headerRow.getReportParameters().get(s));
                                                        }
                                                    } catch (Exception ed) {
                                                        logger.error("Exception:", ed);
                                                    }
                                                    header.append("<li><a class=\"gFontFamily gFontSize12\" onclick=\"javascript:viewAdhocDrillforTableHeader('");
                                                    header.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(headerRow.getReportParameters().get(s)).append("','").append(elementId).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(headerRow.getViewbyId()).append("','").append(headerRow.getViewbyCount()).append("','").append(tableBldr.isCrossTab()).append("','").append(headerRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none;height:" + g + "px;'>").append(headingNew1).append("</a></li>");

                                                }//end of code
                                            }
                                            break;
                                        }//for
                                    }
                                    header.append("</ul></li></ul>");
                                    //end of code written by manik
                                    header.append("</td>");
                                } else //                                if((sortImage.equals("<img  src='"+finalrepDrillUrl+"/TableDisplay/Images/asc.gif'>")) ||(sortImage.equals("<img  src='"+finalrepDrillUrl+"/TableDisplay/Images/desc.gif'>")) )
                                //                 {
                                ////                     .append(sortImage).append(line)
                                //                     header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\""+headerBgColorNew+";"+backGorundColoroNclick1+";\"  name=\""+eleid.replaceAll("A", "")+"\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\")' class=\"headerText tblHdWd\" align=\"" + align + "\" width=\"100%\"style=\" font-size:"+size+"px;\"><div style='display:inline-flex;text-align:center;'><span class=\"headerText \" style=\" font-size:"+size+"px;\"><span style='text-align:center'>").append("</span><font style=\"display: inline-flex;\" color=\"" + mcolor + "\"> " + heading + "</font></span>").append("</div></td>");
                                //                 }
                                //                 else 
                                {
                                    if (flag11.equals(true)) {
                                        header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\"  name=\"" + eleid.replaceAll("A", "") + "\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText tblHdWd\" align=\"" + align + "\" width=\"100%\"style=\" font-size:" + size + "px;\"><div><span class=\"headerText \" style=\" font-size:" + size + "px;\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span></font></span></div></td>");

                                    } else {
                                        header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\"  name=\"" + eleid.replaceAll("A", "") + "\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText tblHdWd\" align=\"" + align + "\" width=\"100%\"style=\" font-size:" + size + "px;\"><div><span class=\"headerText \" style=\" font-size:" + size + "px;\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\"><span style=\" " + backGorundColoroNclick + ";display: inline-table;\"> " + heading + "</span></font></span></div></td>");
                                    }
                                }
                            }
                            header.append("<td  valign=\"top\" align=\"right\" width=\"5%\" style=\"" + headerBgColorNew + ";\" class=\"tabBgcolor\">").append("</td>");
                        }
                    } else {
                        if (tableBldr.facade.container.isReportCrosstab() && headerRows.length > 1 && i >= tableBldr.getViewByCount() && headerRow == headerRows[0] && tableBldr.facade.container.isHideMsrHeading() && id.contains("A_") && !heading.equalsIgnoreCase("grand total")) {
                            /*
                             * if we have Hibrid msrs in Cross Tab and we hide
                             * the measure heading then Msr name should come for
                             * Hibrid Measures
                             */

                            header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";\" name=\"" + eleid.replace("A", "") + "\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText gFontFamily gFontSize12\" align=\"" + align + "\" width=\"100%\" style=\"font-size:" + size + "px;\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\">" + headerRows[1].getRowData(i) + "");
                        } else {
                            header.append("<td valign=\"top\" id=\"tdRef_" + id + "\" style=\"" + headerBgColorNew + ";" + l1HSeparator + "\"  name=\"" + eleid.replace("A", "") + "\" onclick='showfilterview(event,\"" + heading + "\",\"" + eleid + "\",\"" + ulid + "\",\"" + tdid + "\",\"" + flagviewby + "\",\"" + reset + "\",\"" + submenu + "\",\"" + isCrossTabReport + "\",\"" + gtCrossTabLastButOneCol + "\",\"" + tableParameter + "\")' class=\"headerText gFontFamily gFontSize12\" align=\"" + align + "\" width=\"100%\" style=\"font-size:" + size + "px;\"><Font style=\" font-size:"+size+"px;color:"+mcolor+";\">" + heading + "");
                        }
                        header.append("</font></td>");
                        //header.append("<td  valign=\"top\" align=\"right\" width=\"1%\" style=\"background-color:#b4d9ee\">").append("</td>");
                    }
                    header.append("</tr>");
                    header.append("</table>");
                    header.append("</th>");
                }
                header.append("</tr>");
            }

        }
        return super.parentHtml.append(header);
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }
}
