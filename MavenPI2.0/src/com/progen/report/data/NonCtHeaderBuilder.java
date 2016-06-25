/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.progen.query.RTMeasureElement;
import com.progen.report.display.table.TableCellSpan;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.ProgenTimeDefinition;

/**
 *
 * @author arun
 */
public class NonCtHeaderBuilder extends TableHeaderBuilder {

    public static Logger logger = Logger.getLogger(NonCtHeaderBuilder.class);

    public NonCtHeaderBuilder(DataFacade facade) {
        super(facade);
    }

    @Override
    public TableHeaderRow[] getHeaderRowData() {

        ArrayList<TableHeaderRow> headerRowLst = new ArrayList<TableHeaderRow>();
        TableHeaderRow row;
        TableHeaderRow grpRow = null;
        ArrayList<Boolean> isDisplayed;
        boolean isMeasureGroupEnable = false;
        int layer = 0;
        int headerCount = 0;
        ArrayList<String> columnHeadings = new ArrayList<String>();
        ArrayList<String> displayImageList = new ArrayList<String>();

        ArrayList links = facade.getLinks();
        HashMap replinks = facade.getrepLinks();
        Set<String> repvalues;
        ArrayList<String> displayCols = facade.getDisplayColumns();
//        ArrayList<String> displayColIds = facade.getDisplayLabels();
        ArrayList<String> anchors = new ArrayList<String>();
        ArrayList<String> displayGraphLabels = new ArrayList<String>();
        ArrayList timeDetailsArray = facade.container.getReportCollect().timeDetailsArray;

        int viewByCount = facade.getViewByCount();
        ArrayList displayLabels = facade.getDisplayLabels();
        ArrayList<String> hiddenMeasureId = new ArrayList<String>();
        String hiddenMeasureName = "";
        Map<String, String> mapValue = new LinkedHashMap<String, String>();
        isMeasureGroupEnable = facade.isIsMeasureGroupEnable();
        if (isMeasureGroupEnable) {
            headerCount = 1;
        }
        row = new TableHeaderRow();
        row.setLayerNumber(0);
        isDisplayed = new ArrayList<Boolean>();
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = null;
        //Added By Prabal 6-feb
        String tab_str = "";
        String totalEleId = "";//by Ram
        tab_str = facade.container.getDefault_tab(Container.def_key);
        if(tab_str==null)
            tab_str = "";
        //Added By Prabal 6-feb
        if (tab_str != null && (tab_str.equalsIgnoreCase("table") || tab_str.equalsIgnoreCase("report") || tab_str.isEmpty())) {//Added By Prabal 6-feb
            if (!isMeasureGroupEnable) {

                hiddenMeasureId = facade.container.getReportCollect().getHideMeasures();
                if (hiddenMeasureId != null && hiddenMeasureId.size() != 0 && !hiddenMeasureId.get(0).isEmpty()) {
                    for (int measId = 0; measId < hiddenMeasureId.size(); measId++) {
                        hiddenMeasureName = facade.container.getMeasureName(hiddenMeasureId.get(measId));
                        if (hiddenMeasureName != null) {
                            if (hiddenMeasureName.contains("Prev Month")) {
                                if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                                    mapValue.put(hiddenMeasureName.trim(), facade.container.getMonthNameforTrailingFormulaOnColName(hiddenMeasureName));
                                    displayGraphLabels.add(hiddenMeasureName.trim());
                                }
                            } else if (hiddenMeasureName.contains("Prev Year")) {
                                if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                                    mapValue.put(hiddenMeasureName.trim(), facade.container.getYearNameforTrailingFormulaOnColName(hiddenMeasureName));
                                    displayGraphLabels.add(hiddenMeasureName.trim());
                                }
                            } else if (hiddenMeasureName.contains("Prev Day")) {
                                if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                                    mapValue.put(hiddenMeasureName.trim(), facade.container.getDayNameforTrailingFormulaOnColName(hiddenMeasureName));
                                    displayGraphLabels.add(hiddenMeasureName.trim());
                                }
                            } else if (hiddenMeasureName.contains("Prev Quarter")) {
                                if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                                    mapValue.put(hiddenMeasureName.trim(), facade.container.getQuaterNameforTrailingFormulaOnColName(hiddenMeasureName).trim());
                                    displayGraphLabels.add(hiddenMeasureName.trim());
                                }
                            } else if (hiddenMeasureName.contains("YTD") || hiddenMeasureName.contains("CYD") || hiddenMeasureName.contains("QTD") || hiddenMeasureName.contains("MTD")) {
                                if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                                    mapValue.put(hiddenMeasureName.trim(), facade.container.getQuaterNameforTimeFormulaOnColName(hiddenMeasureName));
                                    displayGraphLabels.add(hiddenMeasureName.trim());
                                }
                            } else if (hiddenMeasureName.contains("Prev Month")) {
                                if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                                    mapValue.put(hiddenMeasureName.trim(), facade.container.getMonthNameforTrailingFormulaOnColName(hiddenMeasureName));
                                    displayGraphLabels.add(hiddenMeasureName.trim());
                                }
                            } else {
                                if (!mapValue.containsKey(hiddenMeasureName.trim())) {
                                    mapValue.put(hiddenMeasureName.trim(), hiddenMeasureName);
                                    displayGraphLabels.add(hiddenMeasureName.trim());
                                }
                            }
                        }
                    }
                }
                //Added by Ram
                for (int column = 0; column < facade.getColumnCount(); column++) {
                    String eleId = facade.getColumnId(column);
                    eleId = eleId.replace("A_", "");
                    //modified by anitha for mtd qtd ytd on measures in AO report
                    if (eleId.contains("_percentwise") || eleId.contains("_rank") || eleId.contains("_wf") || eleId.contains("_wtrg") || eleId.contains("_rt") || eleId.contains("_pwst") || eleId.contains("_excel") || eleId.contains("_excel_target") || eleId.contains("_deviation_mean") || eleId.contains("_gl") || eleId.contains("_userGl") || eleId.contains("_timeBased") || eleId.contains("_changedPer") || eleId.contains("_glPer") || eleId.contains("_MTD")|| eleId.contains("_QTD")|| eleId.contains("_YTD")|| eleId.contains("_PMTD")|| eleId.contains("_PQTD")|| eleId.contains("_PYTD")
                            ||eleId.contains("_MOM")||eleId.contains("_QOQ")||eleId.contains("_YOY")||eleId.contains("_MOYM")||eleId.contains("_QOYQ")||eleId.contains("_MOMPer")||eleId.contains("_QOQPer")||eleId.contains("_YOYPer")||eleId.contains("_MOYMPer")||eleId.contains("_QOYQPer")|| eleId.contains("_PYtdrank")|| eleId.contains("_PQtdrank")|| eleId.contains("_PMtdrank")|| eleId.contains("_Qtdrank")|| eleId.contains("_Ytdrank")|| eleId.contains("_PYMTD") || eleId.contains("_PYQTD")
                            ||eleId.contains("_WTD")||eleId.contains("_PWTD")||eleId.contains("_PYWTD")||eleId.contains("_WOW")||eleId.contains("_WOYW")||eleId.contains("_WOWPer")||eleId.contains("_WOYWPer")) {
                        eleId = eleId.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                                .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_PYtdrank", "").replace("_PQtdrank", "").replace("_PMtdrank", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                                .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "");

                    }
                    if (!eleId.equalsIgnoreCase("TIME") && !eleId.equalsIgnoreCase("KPI") && !eleId.equalsIgnoreCase("None")) {
                        if (column == 0) {
                            totalEleId = eleId;
                        } else {
                            totalEleId = totalEleId + "," + eleId;
                        }
                    }

                }
                HashMap elementMap = new HashMap();
                String qry = "select  element_id,USER_COL_TYPE,SUB_FOLDER_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID in (" + totalEleId + ")";
                try {
                    retobj = pbdb.execSelectSQL(qry);
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
                if (retobj != null && retobj.getRowCount() > 0) {
                    for (int i = 0; i < retobj.getRowCount(); i++) {
                        String key = retobj.getFieldValueString(i, 0);
                        String Value = retobj.getFieldValueString(i, 1) + ":" + retobj.getFieldValueString(i, 2);
                        elementMap.put(key, Value);
                    }
                }

                //End ram code
                for (int column = 0; column < facade.getColumnCount(); column++) {
                    String eleId = facade.getColumnId(column);
                    eleId = eleId.replace("A_", "");
                    String userColType = "";
                    String subFolderType = "";
                    if (eleId.contains("_percentwise") || eleId.contains("_rank") || eleId.contains("_wf") || eleId.contains("_wtrg") || eleId.contains("_rt") || eleId.contains("_pwst") || eleId.contains("_excel") || eleId.contains("_excel_target") || eleId.contains("_deviation_mean") || eleId.contains("_gl") || eleId.contains("_userGl") || eleId.contains("_timeBased") || eleId.contains("_changedPer") || eleId.contains("_glPer") || eleId.contains("_MTD")|| eleId.contains("_QTD")|| eleId.contains("_YTD")|| eleId.contains("_PMTD")|| eleId.contains("_PQTD")|| eleId.contains("_PYTD")
                         ||eleId.contains("_MOM")||eleId.contains("_QOQ")||eleId.contains("_YOY")||eleId.contains("_MOYM")||eleId.contains("_QOYQ")||eleId.contains("_MOMPer")||eleId.contains("_QOQPer")||eleId.contains("_YOYPer")||eleId.contains("_MOYMPer")||eleId.contains("_QOYQPer")  || eleId.contains("_PYtdrank")|| eleId.contains("_PQtdrank")|| eleId.contains("_PMtdrank")|| eleId.contains("_Qtdrank")|| eleId.contains("_Ytdrank")|| eleId.contains("_PYMTD") || eleId.contains("_PYQTD") 
                            ||eleId.contains("_WTD")||eleId.contains("_PWTD")||eleId.contains("_PYWTD")||eleId.contains("_WOW")||eleId.contains("_WOYW")||eleId.contains("_WOWPer")||eleId.contains("_WOYWPer")) {
                        eleId = eleId.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                                .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_PYtdrank", "").replace("_PQtdrank", "").replace("_PMtdrank", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                                .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "");

                    }
                    if (!eleId.equalsIgnoreCase("TIME") && !eleId.equalsIgnoreCase("KPI") && !eleId.equalsIgnoreCase("None")) {
                        // String qry = "select  USER_COL_TYPE,SUB_FOLDER_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
                        //      String qry = "select  USER_COL_TYPE,SUB_FOLDER_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID in ("+totalEleId+")";
//                try {
//                    retobj = pbdb.execSelectSQL(qry);
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                }
//             if (retobj != null && retobj.getRowCount() > 0) {
//                userColType = retobj.getFieldValueString(0, 0);
//                subFolderType = retobj.getFieldValueString(0, 1);
//            } 
                        if (elementMap != null && elementMap.size() > 0) {
                            String str = elementMap.get(eleId.replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                                    .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_PYMTD", "").replace("_PYQTD", "")
                                    .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "")).toString();
                            String[] elmtString = str.split(":");
                            userColType = elmtString[0];
                            subFolderType = elmtString[1];
                        }
                    }

                    if (displayLabels.get(column) != null) {

                        if (displayLabels.get(column).toString().contains("Prev Month") && (userColType.equalsIgnoreCase("SUMMARIZED") || facade.getAggregationForSubtotal(facade.getColumnId(column)).contains("#T"))) {
                            columnHeadings.add(facade.container.getMonthNameforTrailingFormula(column));
                        } else if (displayLabels.get(column).toString().contains("Prev Year") && (userColType.equalsIgnoreCase("SUMMARIZED") || facade.getAggregationForSubtotal(facade.getColumnId(column)).contains("#T"))) { //added by sruthi
                            columnHeadings.add(facade.container.getYearNameforTrailingFormula(column));//ended by sruthi
                        } else if (displayLabels.get(column).toString().contains("Prev Day") && (userColType.equalsIgnoreCase("SUMMARIZED") || facade.getAggregationForSubtotal(facade.getColumnId(column)).contains("#T"))) { //added by bhargavi
                            columnHeadings.add(facade.container.getDayNameforTrailingFormula(column));
                        } else if (displayLabels.get(column).toString().contains("Prev Quarter") && (userColType.equalsIgnoreCase("SUMMARIZED") || facade.getAggregationForSubtotal(facade.getColumnId(column)).contains("#T"))) { //added by bhargavi
                            columnHeadings.add(facade.container.getQuaterNameforTrailingFormula(column));
//                if(timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD") && timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")){
//                 String ddate = timeDetailsArray.get(2).toString();
//                         Date date = null;
//                         final SimpleDateFormat df = new SimpleDateFormat( "MM/dd/yyyy" );
//                        try {
//                            date = df.parse(ddate);
//                        } catch (ParseException ex) {
//                            logger.error("Exception:",ex);
//                        }
//                                           final java.util.Calendar cal = GregorianCalendar.getInstance();
//                          cal.setTime(date);
//                            String actualName = displayLabels.get(column).toString();
//                           // String tralingFormName = actualName.substring(7,actualName.length()).replaceAll("(", "").replaceAll(")", "").trim();
//                            String monthNum = "-"+actualName.split("-")[1].replace(")", "").trim();
//                            int prevMonthNum = Integer.parseInt(monthNum);
//                            cal.add( GregorianCalendar.MONTH, prevMonthNum );
//                            String monthName = new DateFormatSymbols().getMonths()[cal.get(java.util.Calendar.MONTH)]+ "-"+cal.get(java.util.Calendar.YEAR);
//                            int strtPos = displayLabels.get(column).toString().indexOf("(");
//                            int endPos = displayLabels.get(column).toString().indexOf(")");
//                            String replacedStr = displayLabels.get(column).toString().substring(strtPos+1, endPos);
//                            columnHeadings.add(displayLabels.get(column).toString().replace(replacedStr, monthName));    
//                }else{
//                columnHeadings.add(displayLabels.get(column).toString());
//                }  
                        }else if(facade.getColumnId(column).contains("_MTD")||facade.getColumnId(column).contains("_QTD")||facade.getColumnId(column).contains("_YTD")||
                                 facade.getColumnId(column).contains("_PMTD")||facade.getColumnId(column).contains("_PQTD")||facade.getColumnId(column).contains("_PYTD")||
                                 facade.getColumnId(column).contains("_MOMPer")||facade.getColumnId(column).contains("_QOQPer")||facade.getColumnId(column).contains("_YOYPer")||facade.getColumnId(column).contains("_MOYMPer")||facade.getColumnId(column).contains("_QOYQPer")||
                                 facade.getColumnId(column).contains("_MOM")||facade.getColumnId(column).contains("_QOQ")||facade.getColumnId(column).contains("_YOY")||facade.getColumnId(column).contains("_MOYM")||facade.getColumnId(column).contains("_QOYQ")||facade.getColumnId(column).contains("_PYMTD")||facade.getColumnId(column).contains("_PYQTD")
                                ||facade.getColumnId(column).contains("_WTD")||facade.getColumnId(column).contains("_PWTD")||facade.getColumnId(column).contains("_PYWTD")||facade.getColumnId(column).contains("_WOW")||facade.getColumnId(column).contains("_WOYW")||facade.getColumnId(column).contains("_WOWPer")||facade.getColumnId(column).contains("_WOYWPer")){
                            ProgenTimeDefinition pTimeDef = ProgenTimeDefinition.getInstance(facade.container.getReportId(), facade.container, "");
                            Map<String, String> timeDefinition = new HashMap<String, String>();
                            timeDefinition= pTimeDef.getTimeDefinition();                            
                            columnHeadings.add(facade.container.getHeaderNameforRTMeasure(column,timeDefinition));
                        }else if(facade.getColumnId(column).contains("_Qtdrank")||facade.getColumnId(column).contains("_Ytdrank")||facade.getColumnId(column).contains("_PMtdrank")||
                                 facade.getColumnId(column).contains("_PQtdrank")||facade.getColumnId(column).contains("_PYtdrank")){
                            //columnHeadings.add(displayLabels.get(column).toString());
                           
                              ProgenTimeDefinition pTimeDef = ProgenTimeDefinition.getInstance(facade.container.getReportId(), facade.container, "");
                            Map<String, String> timeDefinition = new HashMap<String, String>();
                            timeDefinition= pTimeDef.getTimeDefinition();                            
                            columnHeadings.add(facade.container.getHeaderNameforRTMeasure(column,timeDefinition));
                            
                        }
                        else if (displayLabels.get(column).toString().contains("Prior ytd") || displayLabels.get(column).toString().contains("YTD") || displayLabels.get(column).toString().contains("CYD") || displayLabels.get(column).toString().contains("QTD") || displayLabels.get(column).toString().contains("MTD")) {
                            ProgenTimeDefinition pTimeDef = ProgenTimeDefinition.getInstance(facade.container.getReportId(), facade.container, "");
                            Map<String, String> timeDefinition = new HashMap<String, String>();
                            timeDefinition= pTimeDef.getTimeDefinition();                            
                            columnHeadings.add(facade.container.getHeaderNameforRTMeasure(column,timeDefinition));
                        } else if (facade.container.isShowStTimePeriod() && subFolderType.equalsIgnoreCase("Facts")) {
                            columnHeadings.add(facade.container.getStdTimePeriodName(column));
                        } else if(column>=facade.getViewByCount()&&facade.container.getAOId()!=null&&!facade.container.getAOId().isEmpty()) {
                            ProgenTimeDefinition pTimeDef = ProgenTimeDefinition.getInstance(facade.container.getReportId(), facade.container, "");
                            Map<String, String> timeDefinition = new HashMap<String, String>();
                            timeDefinition= pTimeDef.getTimeDefinition();
                            String globalDateFlagForheader = facade.container.getGlobalDateFlagForheader();
                            columnHeadings.add(facade.container.getHeaderNameforAONormalMeasure(column,timeDefinition,globalDateFlagForheader));
                        } else {
                            columnHeadings.add(displayLabels.get(column).toString());
                        }
                        isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));
                        displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
                        if (!mapValue.containsKey(displayLabels.get(column).toString().trim())) {
                            mapValue.put(displayLabels.get(column).toString().trim(), columnHeadings.get(column).toString()); //added by mayank
                            displayGraphLabels.add(displayLabels.get(column).toString().trim());            // added by mayank
                        }

                    } else {
                        columnHeadings.add("");
                    }
                }
                String element = displayCols.get(0);
                String tempUrl = "";
                if (replinks != null && !replinks.isEmpty()) {
                    repvalues = replinks.keySet();
                    if (repvalues.contains(element.replace("A_", "").trim())) {
//                        tempUrl = "reportViewer.do?reportBy=viewReport&REPORTID="+replinks.get(element.replace("A_", "").trim())+"&CBOVIEW_BY"+currentViewBy.trim()+"="+element.replace("A_", "").trim();
                        tempUrl = "reportViewer.do?reportBy=viewReport&REPORTID=" + replinks.get(element.replace("A_", "").trim());
                        anchors.add(tempUrl);
                        row.setHeaderRepLinks(String.valueOf(anchors.get(0)));
                    } else {
                        anchors.add(tempUrl);
                        row.setHeaderRepLinks(String.valueOf(links.get(0)).split("&CBOARP")[0]);
                    }

                }


                facade.container.setTimeDetailsMap(mapValue);
                facade.container.setDisplayGraphLabels(displayGraphLabels); // added by mayank
                row.setHeaderLinks(String.valueOf(links.get(0)).split("&CBOARP")[0]);

                row.setRowDataIds(facade.getDisplayColumns());
                row.setRowData(columnHeadings);
                row.setSortImagePath(displayImageList);
                row.setDisplayStyle(isDisplayed);
                row.setReportParameters(facade.getReportParameters());
                row.setReportParameterNames(facade.getReportParameterNames());
                row.setViewbyId(facade.getViewbyId());
                row.setReportParameterValues(facade.getReportParameterValues());
                row.setViewbyCount(facade.getViewByCount());
                row.setAdhocParamDrillUrl(facade.getadhocParamUrl());
                headerRowLst.add(row);
            } else {
                for (int layerCount = 0; layerCount <= headerCount; layerCount++) {
                    row = new TableHeaderRow();
                    row.setLayerNumber(0);
                    isDisplayed = new ArrayList<Boolean>();
                    boolean isGroupedMeasure = false;
                    HashSet<TableCellSpan> cellSpans = new HashSet<TableCellSpan>();
                    for (int column = 0; column < facade.getColumnCount(); column++) {

                        if (displayLabels.get(column) != null) {
                            if (layerCount == 0) {
                                isGroupedMeasure = facade.isGroupedMeasure((facade.getColumnId(column)));
                                if (!isGroupedMeasure) {
                                    TableCellSpan tableCellSpan = new TableCellSpan(facade.getColumnId(column) + "_" + layer + "_H", 2, 1);
                                    cellSpans.add(tableCellSpan);
                                    columnHeadings.add(displayLabels.get(column).toString());
                                    isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));
                                    displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
                                } else {
                                    TableCellSpan tableCellSpan = new TableCellSpan(facade.getColumnId(column) + "_" + layer + "_H", 1, facade.getMesGrpColumnSpan(facade.getColumnId(column)));
                                    cellSpans.add(tableCellSpan);
                                    if (!columnHeadings.contains(facade.getMeasureGrpName(facade.getColumnId(column)))) {
                                        columnHeadings.add(facade.getMeasureGrpName(facade.getColumnId(column)));
                                        isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));
                                        displayImageList.add("");
                                    } else {
                                        columnHeadings.add(displayLabels.get(column).toString());
                                        isDisplayed.add(false);
                                        displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));
                                    }
                                }
                            } else {

                                if (facade.isGroupedMeasure((facade.getColumnId(column)))) {
                                    columnHeadings.add(facade.getMeasureName(facade.getColumnId(column)));
                                } else {
                                    columnHeadings.add(displayLabels.get(column).toString());
                                }
                                isDisplayed.add(facade.isGroupedMeasure((facade.getColumnId(column))));
                                displayImageList.add(facade.getSortImageStyle(facade.getColumnId(column)));

                            }
                        } else {
                            columnHeadings.add("");
                        }

                    }
                    row.setRowDataIds(facade.getDisplayColumns());
                    row.setRowData(columnHeadings);
                    row.setSortImagePath(displayImageList);
                    row.setDisplayStyle(isDisplayed);
                    row.setCellSpans(cellSpans);
                    headerRowLst.add(row);
                    columnHeadings = new ArrayList<String>();
                    displayImageList = new ArrayList<String>();
                    isDisplayed = new ArrayList<Boolean>();


                }
            }
            //end of //Added By Prabal 6-feb
        }

        return headerRowLst.toArray(new TableHeaderRow[]{});
    }
}
