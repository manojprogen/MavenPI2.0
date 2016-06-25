
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.display.table.TableCellSpan;
import com.progen.report.excel.ExcelCellFormat;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 *
 * @author arun
 */
public class RowViewTableBuilder extends TableBuilder {

    public static Logger logger = Logger.getLogger(RowViewTableBuilder.class);

    public RowViewTableBuilder(DataFacade facade) {
        super(facade);
    }

    @Override
    public TableHeaderRow[] getHeaderRowData() {
        TableHeaderBuilder hdrBldr;
        if (facade.isReportCrosstab()) {
            hdrBldr = new CrosstabHeaderBuilder(facade);
            hdrBldr.setRowViewMode(TableBuilderConstants.MULTI_ROW_VIEW_MODE);
        } else {
            hdrBldr = new NonCtHeaderBuilder(facade);
        }
        return hdrBldr.getHeaderRowData();
    }

    @Override
    public TableMenuRow getMenuRowData() {
        TableMenuRow row = new TableMenuRow();

        ArrayList<String> displayCols = facade.getDisplayColumns();
        ArrayList<Boolean> isDisplayed = new ArrayList<Boolean>();
        HashMap<String, Boolean> isLocked = new HashMap<String, Boolean>();
        ArrayList<Boolean> isFormulaMeasure = new ArrayList<Boolean>();

        row.isCrosstab = facade.isReportCrosstab();
        row.isRowWisePercentDisplayed = row.isCrosstab && facade.isDynamicRowsDisplayedInCrosstab();
        row.viewByCount = facade.getViewByCount();
        row.userTypeAdmin = facade.getuserTypeAdmin();
        row.paramhashmapPA = facade.getparameterHash();
        row.dataTypes = facade.getDataTypes();
        row.displayLabels = facade.getDisplayLabels();
        row.reportId = facade.getReportId();
        row.ctxPath = facade.getContextPath();
        row.isExcelDisplay = facade.isExcelDisplay();
        row.menuPrivileges = facade.getMenuPrivileges();
        row.drillAcrossSupported = facade.isDrillAcrossSupported();
        row.columnCount = facade.columnCount();
        row.desigvalue = facade.getDesign();
        for (int column = 0; column < getColumnCount(); column++) {
            isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));
            isFormulaMeasure.add(facade.isFormulaMeasure(facade.getColumnId(column)));
            isLocked.put(facade.getColumnId(column), facade.isLockdataset(facade.getColumnId(column)));
        }
        row.lockdataset = isLocked;
        row.isDisplayed = isDisplayed;
        row.isFormulaMsr = isFormulaMeasure;
        row.setRowDataIds(displayCols);
        return row;
    }

    @Override
    public TableSearchRow getSearchData() {
        TableSearchRow row = new TableSearchRow();

        ArrayList srchColumns = facade.getSearchColumns();
        ArrayList<String> srchConditions = facade.getSearchConditions();
        ArrayList<Object> srchValues = facade.getSearchValues();
        ArrayList<String> srchData = new ArrayList<String>();
        String srchVal;
        ArrayList<Boolean> isDisplayed = new ArrayList<Boolean>();

        int srchInd;

        for (int column = 0; column < getColumnCount(); column++) {
            srchInd = srchColumns.indexOf(facade.getDisplayColumns().get(column));
            if (srchInd >= 0) {
                srchVal = String.valueOf(srchConditions.get(srchInd) + " " + srchValues.get(srchInd));
            } else {
                srchVal = "";
            }
            isDisplayed.add(facade.isColumnVisible(facade.getColumnId(column)));
            srchData.add(srchVal);
        }
        row.setRowData(srchData);
        row.isDisplayed = isDisplayed;
        row.setRowDataIds(facade.getDisplayColumns());
        row.setReportId(facade.getReportId());
        row.setPath(facade.getContextPath());
        row.setDataType(facade.getDataTypes());
        return row;
    }

    @Override
    public TableSubtotalRow[] getSubtotalRowDataLastRow() {
        if (facade.isAnySubtotallingRequired() && facade.getRowCount() > 0) {
            this.computedSubTotLst.clear();
            if (facade.getViewByCount() > 1) {
                for (int i = facade.getViewByCount() - 2; i >= 0; i--) {
                    //increment parent span
                    if (i < facade.getViewByCount() - 2) {
                        for (int k = i; k < facade.getViewByCount() - 2; k++) {
                            this.subTotalHolder[i].incrementRowCountForSubTotal();
                        }
                    }
                    this.computedSubTotLst.add(this.subTotalHolder[i]);
                }
            }
        }
        return getSubtotalRowData();
    }

    @Override
    public TableSubtotalRow[] getSubtotalRowData() {
        List<TableSubtotalRow> subTotalRowLst = new ArrayList<TableSubtotalRow>();

        SubTotalHolder subHolder = null;
        TableCellSpan span;
        int rowCount;

        this.reOrderSubTotalList();

        for (int subIndex = 0; subIndex < computedSubTotLst.size(); subIndex++) {
            if (facade.isNetTotalRequired()) {
                subTotalRowLst.add(createSubTotalRowsForSubTotalType("ST", subIndex));
            }

            if (facade.isMedianRequired()) {
                subTotalRowLst.add(createSubTotalRowsForSubTotalType("MED", subIndex));
            }

            if (facade.isMeanRequired()) {
                subTotalRowLst.add(createSubTotalRowsForSubTotalType("MEA", subIndex));
            }

            if (facade.isStdDeviationRequired()) {
                subTotalRowLst.add(createSubTotalRowsForSubTotalType("STDDEV", subIndex));
            }

            if (facade.isVarianceRequired()) {
                subTotalRowLst.add(createSubTotalRowsForSubTotalType("VAR", subIndex));
            }
            //added by Govardhan
            if (!super.isIsCountingRowsAfterFilter()) {
                subHolder = super.computedSubTotLst.get(subIndex);
                rowCount = subHolder.getRowCount();
                String FirstRowId = super.getFirstRowAfterSubTtlFiltr();
                if (facade.getViewByCount() == 3 && facade.getIsSubToTalSearchFilterApplied() && subIndex == 1) {
                    span = new TableCellSpan(FirstRowId, rowCount, 0);
                } else {
                    span = new TableCellSpan(subHolder.getFirstCellId(), rowCount, 0);
                }
                super.cellSpan.add(span);
            }
            //ended by Govardhan
        }

        return (TableSubtotalRow[]) subTotalRowLst.toArray(new TableSubtotalRow[]{});
    }

    @Override
    public TableSubtotalRow[] getGrandtotalRowData() {
        ArrayList<TableSubtotalRow> subTotalRowLst = new ArrayList<TableSubtotalRow>();
        TableDataRow dataRow = new TableDataRow();
        ArrayList<Object> rowData = new ArrayList<Object>();
        if (facade.getRowCount() > 0) {
            if (facade.isTopBottomWithOthersEnable()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("TOPOTHERS"));
            }
            if (facade.isNetTotalRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("CATST"));
            }
            if (facade.isGrandTotalRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("GT"));
            } else {
                createGrandTotalRowForSubTotalType("GT");
            }
//            if ( facade.isNetTotalRequired() )
//            {
//                subTotalRowLst.add(createGrandTotalRowForSubTotalType("CATST"));
//            }


            if (facade.isMedianRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("GTMED"));
            }
            if (facade.isMeanRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("GTMEA"));
            }
            if (facade.isStdDeviationRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("GTSTD"));
            }
            if (facade.isVarianceRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("GTVAR"));
            }
            if (facade.isAverageRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("AVG"));
            }
            if (facade.isOverAllMaxRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("OVEMAX"));
            }
            if (facade.isOverAllMinRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("OVEMIN"));
            }
            if (facade.isCategoryMaxRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("CATMAX"));
            }
            if (facade.isCategoryMinRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("CATMIN"));
            }
            if (facade.isRowCountRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("ROWCNT"));
            }
            if (facade.isCatAvgRequired()) {
                subTotalRowLst.add(createGrandTotalRowForSubTotalType("CATAVG"));
            }
            if (facade.isCustomTotalRequired()) {
                if (facade.getmappedTo().equalsIgnoreCase("LR")) {
                    if (subTotalRowLst.size() > 0) {
                        subTotalRowLst.add(createGrandTotalRowForSubTotalType("customTotal" + subTotalRowLst.get(subTotalRowLst.size() - 1).getSubtotalType()));
                    } else {
                        subTotalRowLst.add(createGrandTotalRowForSubTotalType("customTotalLR"));
                    }
                } else {
                    subTotalRowLst.add(createGrandTotalRowForSubTotalType("customTotal" + facade.getmappedTo()));
                }
            }

        }
        return (TableSubtotalRow[]) subTotalRowLst.toArray(new TableSubtotalRow[]{});
    }

    @Override
    public TableDataRow getRowData(int rowNum) {
        TableDataRow row = new TableDataRow();
        ArrayList dataTypes = facade.getDataTypes();
        ArrayList<String> displayCols = facade.getDisplayColumns();
        ArrayList<Object> rowData = new ArrayList<Object>();
        ArrayList<String> colorList = new ArrayList<String>();
        ArrayList<String> cssClass = new ArrayList<String>();
        ArrayList<String> anchors = new ArrayList<String>();
        ArrayList<String> fontColorList = new ArrayList<String>();
        ArrayList<String> commentList = new ArrayList<String>();
        ArrayList<Boolean> editableList = new ArrayList<Boolean>();
        ArrayList<String> formulaList = new ArrayList<String>();
        ArrayList<String> imagePathList = new ArrayList<String>();
        ArrayList<String> textAlignList = new ArrayList<String>();
        HashMap<String, ArrayList<Double>> rowdataMap = new HashMap();

        int actualRow = facade.getActualRow(rowNum);
        facade.setActualrow(actualRow);
        facade.setRowForSTRank(rowNum);
        facade.setRowForRTTimeDimension(rowNum);
        ReportTemplateDAO dao = new ReportTemplateDAO();
        Object cellData = "";
        String bgColor = "";
        String fontColor = "";
        String measureType = "";
        boolean isDimension;
        String css = "";
        String tempUrl = "";
        String cellData2 = "";
        ArrayList displayTypes = facade.getDisplayTypes();
        ArrayList links = facade.getLinks();
        HashMap replinks = facade.getrepLinks();
        Boolean editable = false;
        String formula = null;
        String imagePath = null;
        String textAlign = null;
        Set<String> repvalues;
        //  String drillAcrossData=facade.getDrillAcrossData(actualRow);
        String drillAcrossData = "";
        String currentViewBy = "";
//        String adhocUrl = null; //for adhoc drill coded by swati
        StringBuilder adhocUrl = new StringBuilder(1000);
        String adhocTimeurl = null;
        String msrDrillUrl = "";  //for msr drill purpose of multirow by coded by swati
        // for report drill coded by swati
        String reportDrillUrl = "";
//        PbReportViewerDAO daoonnsrattr=new PbReportViewerDAO();
//                HashMap mapformsrattr=daoonnsrattr.modifyMeasureAttrreport(pbReportId,facade.container);
//                facade.container.setmodifymeasureAttrChnge(mapformsrattr);
        StringBuffer tempReportDrill = new StringBuffer();
        HashMap<String, ArrayList> nonViewByMapNew = null;
        ArrayList<String> colVallist = new ArrayList<String>();
        List<String> summarizedlist = null;
        nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
        String colViewby = null;
        boolean isCrossTabReport = false;
        if (facade.container.getReportCollect().reportColViewbyValues != null && facade.container.getReportCollect().reportColViewbyValues.size() > 0) {
            colViewby = (String) facade.container.getReportCollect().reportColViewbyValues.get(0);
            isCrossTabReport = true;
        }
        ArrayList<String> reportDrillList = new ArrayList<String>();
        ArrayList<String> selfreportDrillList = new ArrayList<String>();
        //written by swati
        HashMap<String, String> finalCrossTabReportDrillMap = ((PbReturnObject) facade.container.getRetObj()).finalCrossTabReportDrillMap;
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;

        TreeSet<Integer> index = new TreeSet<Integer>();
        ArrayList<Integer> val1 = new ArrayList<Integer>();
        ArrayList<String> Measures = new ArrayList<String>();
//        if(!facade.container.getAdhocDrillType().equalsIgnoreCase("none")){
        if (facade.getDisplayColumns().get(0).equals("TIME")) {
            for (int i = 0; i < facade.getViewSequence().size(); i++) {
                index.add(facade.getActualRow(i));
            }
            val1.addAll(index);
            actualRow = facade.getViewSequence().get(rowNum);
            drillAcrossData = facade.getDrillAcrossData(actualRow);
        }
//       }else{
//            if(facade.getDisplayColumns().get(0).equals("TIME")){
//       for(int i=0;i<facade.getViewSequence().size();i++){
//           index.add(facade.getActualRow(i));
//       }
//       val1.addAll(index);
//       actualRow=facade.getViewSequence().get(rowNum);
//       drillAcrossData=facade.getDrillAcrossData(actualRow);
//       }
        //      }
        for (int i = 0; i < getColumnCount(); i++) {
            if (i < facade.getViewByCount()) {
                isDimension = true;
            } else {
                isDimension = false;
            }
            if (isDimension) {
                String element = displayCols.get(i);
                if (replinks != null && !replinks.isEmpty()) {
                    repvalues = replinks.keySet();
                    if (repvalues.contains(element.replace("A_", "").trim())) {
                        currentViewBy = dao.getCurrentViewbysofRep(replinks.get(element.replace("A_", "").trim()).toString());
                    }
                }
            } else {
                if (isCrossTabReport) {
                    String element = displayCols.get(i);
                    if ("D".equals(dataTypes.get(i))) {
                        //
                        if (!element.contains("_")) {
                            String actualMsrs = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId.get(element);
                            if (!Measures.contains(actualMsrs)) {
                                Measures.add(actualMsrs);
                            }
                            ArrayList<Double> RowDatavalues = new ArrayList();
                            BigDecimal value = BigDecimal.ZERO;
                            RowDatavalues = rowdataMap.get(actualMsrs);
                            if (RowDatavalues == null) {
                                RowDatavalues = new ArrayList();
                            }
//                     double RwValue =facade.getValue(actualRow, element);
//                             facade.getFormattedMeasureData(rowNum, i);
//                    if(!CtAvgdata.isEmpty()){
//                          value=new BigDecimal(CtAvgdata.replaceAll(",", ""));
//                     }
                            cellData = facade.getDateData(actualRow, element);
                            // cellDataList.clear();
                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");  //fetched format
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); //new format..
                            Date date = null;
                            try {
                                if (cellData != null && !cellData.toString().equalsIgnoreCase("null") && !cellData.toString().isEmpty() && !cellData.toString().equalsIgnoreCase("")) {
                                    date = format1.parse(cellData.toString().trim());
                                    cellData = format2.format(date);
                                    cellData2 = cellData.toString();
                                    String cellData1 = cellData.toString();
                                    cellData1 = cellData1.trim();
                                    String[] dateandtime = cellData1.split(" ");      //added by mayank on 12/05/14(Split date and time.)
                                    cellData = dateandtime[0];
                                } else {
                                    cellData = "";
                                }
                                // cellDataList.add(cellData);

                            } catch (ParseException ex) {
                                logger.error("Exception:", ex);
                            }
                            //RowDatavalues.add(cellData);
                            //Collections.sort(RowDatavalues);
                            rowdataMap.put(actualMsrs, RowDatavalues);

//                    facade.rowdataMap=rowdataMap;
                        }
                        //

                    } else {
                        //String element = displayCols.get(i);
                        if (!element.contains("_")) {
                            String actualMsrs = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId.get(element);
                            if (!Measures.contains(actualMsrs)) {
                                Measures.add(actualMsrs);
                            }
                            ArrayList<Double> RowDatavalues = new ArrayList();
                            BigDecimal value = BigDecimal.ZERO;
                            RowDatavalues = rowdataMap.get(actualMsrs);
                            if (RowDatavalues == null) {
                                RowDatavalues = new ArrayList();
                            }
                            double RwValue = 0;

                            if (!"C".equals(dataTypes.get(i))) {
                                RwValue = facade.getValue(actualRow, element);


                                facade.getFormattedMeasureData(rowNum, i);
//                    if(!CtAvgdata.isEmpty()){
//                          value=new BigDecimal(CtAvgdata.replaceAll(",", ""));
//                     }

                                RowDatavalues.add(RwValue);
                                Collections.sort(RowDatavalues);
                                rowdataMap.put(actualMsrs, RowDatavalues);
                            }

//                    facade.rowdataMap=rowdataMap;
                        }
                    }
                }
            }
        }
        HashMap<String, HashMap<Double, String>> colorcodeMap = new HashMap();
        colorcodeMap = Setcolorcodes(rowdataMap);
        facade.setColorcodeMap(colorcodeMap);
        facade.setMeasures(Measures);
        int flag = 0;
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        List cellDataList = new ArrayList();
        List cellDataLst = new ArrayList();
        String selfReportDimurl = "";

        for (int i = 0; i < getColumnCount(); i++) {
            //
            String selfReportDrill = "";
            String finalrepDrillUrl = facade.getContextPath() + "/reportViewer.do?reportBy=viewReport";
            String element = displayCols.get(i);
            editable = false;
            formula = null;
            imagePath = null;
            textAlign = null;
            if (i < facade.getViewByCount()) {
                isDimension = true;
            } else {
                isDimension = false;
            }
            String elementcr="" ;
  if (isCrossTabReport) {
     elementcr=element.replace("A_","");

  }
            if (isDimension) {
                if (element.equalsIgnoreCase("TIME")|| elementcr.equalsIgnoreCase("TIME")) {
                    String temp[] = String.valueOf(links.get(0)).split("&");
                    for (int x = 2; x < temp.length; x++) {
                        adhocUrl.append("&").append(temp[x]);
//                    adhocUrl+="&"+temp[x];
                    }
                }
                if ("D".equals(dataTypes.get(i))) {
                    cellData = facade.getDateData(actualRow, element);
                    cellDataList.clear();
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");  //fetched format
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); //new format..
                    Date date = null;
                    try {
                        if (cellData != null && !cellData.toString().equalsIgnoreCase("null") && !cellData.toString().isEmpty() && !cellData.toString().equalsIgnoreCase("")) {
                            date = format1.parse(cellData.toString().trim());
                            cellData = format2.format(date);
                            cellData2 = cellData.toString();
                            String cellData1 = cellData.toString();
                            cellData1 = cellData1.trim();
                            String[] dateandtime = cellData1.split(" ");      //added by mayank on 12/05/14(Split date and time.)
                            cellData = dateandtime[0];
                        } else {
                            cellData = "";
                        }
                    } catch (ParseException ex) {
                        logger.error("Exception:", ex);
                    }

                    cellDataList.add(cellData);
                    if (element.equalsIgnoreCase("TIME")) {
//                        adhocUrl+=cellData;
                        adhocUrl.append(cellData);
                        msrDrillUrl += "," + element.replace("A_", "") + "=" + cellData;
                        reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(facade.container.getReportCollect().timeDetailsArray) + cellData + "&DDrillAcross=Y";
                    } else {
                        //start of sandeep code for drill to report with time viewby issue track id-->TA_R_79
                        ArrayList viewbynames1 = (ArrayList) facade.container.getViewByColNames();
                        if (viewbynames1 != null && !viewbynames1.isEmpty() && viewbynames1.contains("Month Year") || viewbynames1.contains("Qtr Year") || viewbynames1.contains("Qtr") || viewbynames1.contains("Year") || viewbynames1.contains("Month")) {
                            ArrayList timeDetailsArray = new ArrayList();
                            adhocUrl.append("&CBOARP").append(element.replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType)));
//                    adhocUrl+="&CBOARP" + element.replace("A_", "") + "="+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType));
//                        System.out.println("java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType))---"+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType)));
                            msrDrillUrl += "," + element.replace("A_", "") + "=" + cellData;

                            drillAcrossData += "#" + element + ":" + cellData;
                            selfReportDimurl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                            if (facade.container.getReportCollect().timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(0).toString());
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(1).toString());
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(2).toString());
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(3).toString());
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(4).toString());
                                String paramObject = "";
                                String year = "";
                                String finalvalue = "";
                                if (facade.container.getReportCollect().timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                                    year = facade.container.getNewUiqr();
                                } else if (facade.container.getReportCollect().timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                                    year = facade.container.getNewUIyr();
                                } else {
                                    year = facade.container.getReportCollect().timeDetailsArray.get(2).toString().split("/")[2];
                                }
                                if (cellData != null) {
                                    Type listOfTestObject = new TypeToken<String>() {
                                    }.getType();

                                    paramObject = gson.toJson(cellData, listOfTestObject);

                                }
                                if (viewbynames1.contains("Qtr") || viewbynames1.contains("Qtr Year")) {
                                    timeDetailsArray.set(3, "Qtr");
                                    String[] dateandtime = paramObject.split("-");
                                    if (viewbynames1.contains("Qtr Year")) {

                                        finalvalue = "Q" + dateandtime[1].trim() + "-" + dateandtime[2].trim();
                                    }
                                    if (viewbynames1.contains("Qtr")) {
                                        finalvalue = "Q" + dateandtime[1].trim() + "-" + year.trim();
                                    }
                                    Object cellData1 = "";
                                    cellData1 = (Object) finalvalue;
                                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Month") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Year")) {
                                        timeDetailsArray.set(4, "Last Qtr");
                                    } else if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Same Month Last Year")) {
                                        timeDetailsArray.set(4, "Same Qtr Last Year");
                                    }
                                    reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(timeDetailsArray) + cellData1 + "&DDrillAcross=Y";
                                } else if (viewbynames1.contains("Month") || viewbynames1.contains("Month Year")) {
                                    timeDetailsArray.set(3, "Month");
                                    String[] dateandtime = paramObject.split("-");
                                    if (viewbynames1.contains("Month Year")) {

                                        finalvalue = dateandtime[0].trim() + "-" + dateandtime[1].trim();
                                    }
                                    if (viewbynames1.contains("Month")) {
                                        finalvalue = dateandtime[0].trim() + "-" + year.trim();
                                    }
                                    Object cellData1 = "";
                                    cellData1 = (Object) finalvalue;
                                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Year")) {
                                        timeDetailsArray.set(4, "Last Month");
                                    } else if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Same Qtr Last Year")) {
                                        timeDetailsArray.set(4, "Same Month Last Year");
                                    }
                                    reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(timeDetailsArray) + cellData1 + "&DDrillAcross=Y";
                                } else {
                                    timeDetailsArray.set(3, "Year");
                                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Month")) {
                                        timeDetailsArray.set(4, "Last Year");
                                    }
                                    reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(timeDetailsArray) + cellData + "&DDrillAcross=Y";
                                }
                            } else {
                                adhocUrl.append("&CBOARP").append(element.replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType)));
//                         adhocUrl+="&CBOARP" + element.replace("A_", "") + "="+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType));
//                        System.out.println("java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType))---"+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType)));
                                msrDrillUrl += "," + element.replace("A_", "") + "=" + cellData;
                                reportDrillUrl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                                drillAcrossData += "#" + element + ":" + cellData;
                                selfReportDimurl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                            }
                        } else {
                            adhocUrl.append("&CBOARP").append(element.replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType)));
//                    adhocUrl+="&CBOARP" + element.replace("A_", "") + "="+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType));
                            msrDrillUrl += "," + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                            reportDrillUrl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                            drillAcrossData += "#" + element + ":" + cellData;
                            selfReportDimurl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                        }
                    }
                } else {
                    //start of sandeep code for drill to report with time viewby issue track id-->TA_R_79
                    ArrayList viewbynames1 = (ArrayList) facade.container.getViewByColNames();
                    cellData = facade.getDimensionData(actualRow, element);
                    cellDataList.clear();
                    cellDataList.add(cellData);
                    if (element.equalsIgnoreCase("TIME") || elementcr.equalsIgnoreCase("TIME")) {
//                        adhocUrl+=cellData;
                        adhocUrl.append(cellData);
                        msrDrillUrl += "," + element.replace("A_", "") + "=" + cellData;
                        reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(facade.container.getReportCollect().timeDetailsArray) + cellData + "&DDrillAcross=Y";
                    } else {
                        if (viewbynames1 != null && !viewbynames1.isEmpty() && viewbynames1.contains("Month Year") || viewbynames1.contains("Qtr Year") || viewbynames1.contains("Qtr") || viewbynames1.contains("Year") || viewbynames1.contains("Month")) {
                            ArrayList timeDetailsArray = new ArrayList();
                            adhocUrl.append("&CBOARP").append(element.replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType)));
//                    adhocUrl+="&CBOARP" + element.replace("A_", "") + "="+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType));
//                        System.out.println("java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType))---"+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType)));
                            msrDrillUrl += "," + element.replace("A_", "") + "=" + cellData;

                            drillAcrossData += "#" + element + ":" + cellData;
                            selfReportDimurl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                            if (facade.container.getReportCollect().timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(0).toString());
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(1).toString());
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(2).toString());
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(3).toString());
                                timeDetailsArray.add(facade.container.getReportCollect().timeDetailsArray.get(4).toString());
                                String paramObject = "";
                                String year = "";
                                String finalvalue = "";
                                if (facade.container.getReportCollect().timeDetailsArray.get(3).toString().equalsIgnoreCase("Qtr")) {
                                    year = facade.container.getNewUiqr();
                                } else if (facade.container.getReportCollect().timeDetailsArray.get(3).toString().equalsIgnoreCase("Year")) {
                                    year = facade.container.getNewUIyr();
                                } else {
                                    year = facade.container.getReportCollect().timeDetailsArray.get(2).toString().split("/")[2];
                                }
                                if (cellData != null) {
                                    Type listOfTestObject = new TypeToken<String>() {
                                    }.getType();

                                    paramObject = gson.toJson(cellData, listOfTestObject);

                                }
                                if (viewbynames1.contains("Qtr") || viewbynames1.contains("Qtr Year")) {
                                    timeDetailsArray.set(3, "Qtr");
                                    String[] dateandtime = paramObject.split("-");
                                    if (viewbynames1.contains("Qtr Year")) {

                                        finalvalue = "Q" + dateandtime[1].trim() + "-" + dateandtime[2].trim();
                                    }
                                    if (viewbynames1.contains("Qtr")) {
                                        finalvalue = "Q" + dateandtime[1].trim() + "-" + year.trim();
                                    }
                                    Object cellData1 = "";
                                    cellData1 = (Object) finalvalue;
                                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Month") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Year")) {
                                        timeDetailsArray.set(4, "Last Qtr");
                                    } else if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Same Month Last Year")) {
                                        timeDetailsArray.set(4, "Same Qtr Last Year");
                                    }
                                    reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(timeDetailsArray) + cellData1 + "&DDrillAcross=Y";
                                } else if (viewbynames1.contains("Month") || viewbynames1.contains("Month Year")) {
                                    timeDetailsArray.set(3, "Month");
                                    String[] dateandtime = paramObject.split("-");
                                    if (viewbynames1.contains("Month Year")) {

                                        finalvalue = dateandtime[0].trim() + "-" + dateandtime[1].trim();
                                    }
                                    if (viewbynames1.contains("Month")) {
                                        finalvalue = dateandtime[0].trim() + "-" + year.trim();
                                    }
                                    Object cellData1 = "";
                                    cellData1 = (Object) finalvalue;
                                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Year")) {
                                        timeDetailsArray.set(4, "Last Month");
                                    } else if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Same Qtr Last Year")) {
                                        timeDetailsArray.set(4, "Same Month Last Year");
                                    }
                                    reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(timeDetailsArray) + cellData1 + "&DDrillAcross=Y";
                                } else {
                                    timeDetailsArray.set(3, "Year");
                                    if (timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Qtr") || timeDetailsArray.get(4).toString().equalsIgnoreCase("Last Month")) {
                                        timeDetailsArray.set(4, "Last Year");
                                    }
                                    reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(timeDetailsArray) + cellData + "&DDrillAcross=Y";
                                }
                            } else {
                                adhocUrl.append("&CBOARP").append(element.replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType)));
//                         adhocUrl+="&CBOARP" + element.replace("A_", "") + "="+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType));
                                msrDrillUrl += "," + element.replace("A_", "") + "=" + cellData;
                                reportDrillUrl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                                drillAcrossData += "#" + element + ":" + cellData;
                                selfReportDimurl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                            }
                        } else {
                            adhocUrl.append("&CBOARP").append(element.replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType)));
//                    adhocUrl+="&CBOARP" + element.replace("A_", "") + "="+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType));
//                        System.out.println("java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType))---"+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType)));
                            msrDrillUrl += "," + element.replace("A_", "") + "=" + cellData;
                            reportDrillUrl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                            drillAcrossData += "#" + element + ":" + cellData;
                            selfReportDimurl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                        }
                    }

                }
                bgColor = "";
                css = "dimensionCell";
                if ("H".equalsIgnoreCase(String.valueOf(displayTypes.get(i)))) {

                    if (replinks != null && !replinks.isEmpty()) {
                        repvalues = replinks.keySet();
                        if (repvalues.contains(element.replace("A_", "").trim())) {
//                        tempUrl = "reportViewer.do?reportBy=viewReport&REPORTID="+replinks.get(element.replace("A_", "").trim())+"&CBOVIEW_BY"+currentViewBy.trim()+"="+element.replace("A_", "").trim();
                            tempUrl = "reportViewer.do?reportBy=viewReport&REPORTID=" + replinks.get(element.replace("A_", "").trim()) + "&CBOARP" + element.replace("A_", "").trim() + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
                        } else {
                            String dimData = facade.getDimensionData(actualRow, element); //.replace(" ", "%20").replace("&", "%26")
                            List<String> dimDataList = new ArrayList<String>();
                            dimDataList.add(dimData);
                            tempUrl = java.net.URLEncoder.encode(String.valueOf(links.get(i)) + gson.toJson(dimDataList, tarType));
                        }

                    } else {
                        String dimData = facade.getDimensionData(actualRow, element); //.replace(" ", "%20").replace("&", "%26")
                        List<String> dimDataList = new ArrayList<String>();
                        dimDataList.add(dimData);
                        tempUrl = java.net.URLEncoder.encode(String.valueOf(links.get(i)) + gson.toJson(dimDataList, tarType));
                    }
                    adhocTimeurl = String.valueOf(links.get(i));
                    anchors.add(tempUrl);
                } else {
                    anchors.add("#");
                }
//                fontColorList.add("");
                fontColor = "";
                commentList.add(null);
                 //added by sruthi for viewbytablecolumnpro
                if(facade.getViewbydataAlignment(element)==null || facade.getViewbydataAlignment(element).toString().equalsIgnoreCase("")){
                if(isCrossTabReport){
                    if(facade.getViewbydataAlign(actualRow, crosstabMeasureId.get(element))!=null){
                        //
                        if(crosstabMeasureId.get(element)!=null){
                if(facade.getViewbydataAlign(actualRow, crosstabMeasureId.get(element).toString())!=null){
                    textAlign = facade.getViewbydataAlign(actualRow, crosstabMeasureId.get(element).toString());
                     if(textAlign!=null && textAlign.equalsIgnoreCase("")){
                        if(!isDimension){
                            textAlign = "Center";
            }
                    }
                }
                        }else{
                           textAlign = "Center";
                        }
                    }
                }else{
                     if(facade.getViewbydataAlign(actualRow, element)!=null){
                    textAlign = facade.getViewbydataAlign(actualRow, element);
                    if(textAlign==null || textAlign.equalsIgnoreCase("")){
                        if(!isDimension){
                            textAlign = "Center";
                        }
                    }
                }
                }
                }else{
                    if(facade.getViewbydataAlignment(element).toString().equalsIgnoreCase("")){
                     if(facade.getViewbydataAlign(actualRow, element)!=null){
                    textAlign = facade.getViewbydataAlign(actualRow, element);
                    if(textAlign==null || textAlign.equalsIgnoreCase("")){
                        if(!isDimension){
                            textAlign = "Center";
                        }
                    }
                }
                }else{
                    textAlign=facade.getViewbydataAlignment(element);
                    }
                    }//ended by sruthi
            } else {
                //written by swati.
                if (facade.container.isReportCrosstab()) {
                    if (finalCrossTabReportDrillMap.containsKey(element)) {
                        finalrepDrillUrl += "&REPORTID=" + finalCrossTabReportDrillMap.get(element);
                    }
                } else {
                    finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(element);
                }
                // 
                if (nonViewByMapNew != null && nonViewByMapNew.size() > 0 && nonViewByMapNew.containsKey(element)) {
//                    cellDataList.clear();
//                    cellDataList.add(cellData);
                    cellDataLst.clear();
                    colVallist = (ArrayList<String>) nonViewByMapNew.get(element);
                    cellDataLst.add((String) colVallist.get(0));
                    if (colViewby.equalsIgnoreCase("TIME")) {
                        tempReportDrill.append(facade.container.getReportCollect().resolveTimeDrill(facade.container.getReportCollect().timeDetailsArray) + (String) colVallist.get(0) + "&DDrillAcross=Y");
                    } else {
                        if (facade.container.getSummerizedTableHashMap() != null && !facade.container.getSummerizedTableHashMap().isEmpty()) {
                            HashMap summarizedmMesMap = facade.container.getSummerizedTableHashMap();
                            summarizedlist = (List<String>) summarizedmMesMap.get("summerizedQryeIds");
                        }
                        if (summarizedlist == null || !summarizedlist.contains(element.toString().replace("A_", ""))) {
                            tempReportDrill.append("&CBOARP" + colViewby + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataLst, tarType)));
                        }
                    }
                    selfReportDrill += "&CBOARP" + colViewby + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataLst, tarType));

                }

                // started by Mayank...
                if ("D".equals(dataTypes.get(i))) {
                    cellData = facade.getDateData(actualRow, element);
                    cellDataList.clear();
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");  //fetched format
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); //new format..
                    Date date = null;
                    try {
                        if (cellData != null && !cellData.toString().equalsIgnoreCase("null") && !cellData.toString().isEmpty() && !cellData.toString().equalsIgnoreCase("")) {
                            date = format1.parse(cellData.toString().trim());
                            cellData = format2.format(date);
                            cellData2 = cellData.toString();
                            String cellData1 = cellData.toString();
                            cellData1 = cellData1.trim();
                            String[] dateandtime = cellData1.split(" ");      //added by mayank on 12/05/14(Split date and time.)
                            cellData = dateandtime[0];
                        } else {
                            cellData = "";
                        }
                        cellDataList.add(cellData);

                    } catch (ParseException ex) {
                        logger.error("Exception:", ex);
                    }
                } else {
                    cellData = facade.getFormattedMeasureData(actualRow, i);
                    cellDataList.clear();
                    cellDataList.add(cellData);
                    //

                }
                // end of code..
                fontColor = "";
                //cellData = facade.getFormattedMeasureData(actualRow, i);
                BigDecimal measData = facade.getMeasureData(actualRow, element);// changed by veena
//                if (facade.container.getmodifymeasureAttrChng() != null && facade.container.getmodifymeasureAttrChng().size() != 0) {
                HashMap NFMap = new HashMap();
                String nbrSymbol = "";
                String Symbol = "";
                HashMap colProps = facade.container.getColumnProperties();
                ArrayList propList;
                if (isCrossTabReport && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.get(element) != null) {
                    propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                } else {
                    propList = (ArrayList) colProps.get(element);
                }
                if (propList != null && (colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                    String colSymbol = (String) propList.get(7);
                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                        facade.container.symbol.put(element, colSymbol);
                    }
                }
//
                NFMap = (HashMap) facade.container.getTableHashMap().get("NFMap");
                nbrSymbol = facade.container.symbol.get(element);

                if (NFMap != null) {
//             nbrSymbol = String.valueOf(NFMap.get(element));
                    if (isCrossTabReport && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                        if (NFMap.get(crosstabMeasureId.get(element)) != null) {
                            Symbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                        }
                    } else if (NFMap.get(element) != null) {
                        Symbol = String.valueOf(NFMap.get(element));
                    }

                }
//               cellData=Symbol+cellData;
                //modified by anitha
              if(nbrSymbol!=null && cellData!= ""){
                    if (nbrSymbol.equalsIgnoreCase("%")) {
                        cellData = cellData + "" + nbrSymbol;
                    } else {
                        cellData = nbrSymbol + "" + cellData;
                    }
                }
//              if(Symbol!=null && !Symbol.equalsIgnoreCase("Nf")){
//              if(nbrSymbol!=null && nbrSymbol.equalsIgnoreCase("%"))
//                  cellData=cellData;
//                 else
//                cellData=cellData+" "+Symbol;
////                  cellData=cellData;
//              }

//                }
                if (facade.getBGcolor(element) == null || facade.getBGcolor(element).toString().equalsIgnoreCase("")) {
                    BigDecimal subtotalval = null;
                    if (facade.container.getViewByCount() <= 1 && facade.getAggregationType(element) != null && !facade.getAggregationType(element).toString().equalsIgnoreCase("null") && facade.getAggregationType(element).toString().equalsIgnoreCase("avg")) {
                        subtotalval = getGTAVGVal(i, element);
                    } else if (facade.getAggregationType(element) != null && !facade.getAggregationType(element).toString().equalsIgnoreCase("null") && facade.getAggregationType(element).toString().equalsIgnoreCase("avg") && !facade.isAnySubtotallingRequired()) {
                        subtotalval = getGTAVGVal(i, element);
                    } else {
                        // subtotalval = getCatSTVal(i,element) ;
                    }

                    bgColor = facade.getColor(actualRow, element, subtotalval);
                } else {
                    bgColor = facade.getBGcolor(element);
                }
                if (facade.getFontcolor(element) == null || facade.getFontcolor(element).toString().equalsIgnoreCase("")) {
                    if ("N".equals(dataTypes.get(i))) {
                        css = "measureNumericCell";
                        if (facade.isSignApplicable(element)) {
                            String signStyle = facade.getSignStyleforMeasure(element, actualRow);
                            if ("positive".equals(signStyle)) {
                                if (facade.container.getPriorEnableFlag())//changed  by sruthi for prior measure icons
                                {
                                    css = "measureNumericCellPositiveGnf";
                                } else {
                                    css = "measureNumericCellPositive";
                                }
                            } else if ("negative".equals(signStyle)) {
                                if (facade.container.getPriorEnableFlag()) {
                                    css = "measureNumericCellNegativeGnf";
                                } else {
                                    css = "measureNumericCellNegative";//ended by sruthi
                                }
                            } else {
                                css = "measureNumericCell";
                            }
                        }
                        if (facade.isFontColorApplicable(element)) {
                            fontColor = facade.getFontColorForMeasure(element, actualRow);
                            //fontColorList.add(fontColor);
                        } else {
                            //fontColorList.add("");
                        }
                        if (facade.isScriptColorEnable(element)) {
                            measureType = facade.getmeasureType(element);
                            //added by nazneen
                            if (measureType == null || measureType.equalsIgnoreCase("null") || measureType.equalsIgnoreCase("none")) {
                                measureType = "Standard";
                            }
//                        fontColor = facade.getFontColorForscript(measData.doubleValue(),measureType,element);
                            if (measureType.equalsIgnoreCase("Standard")) {
                                if (css.equalsIgnoreCase("measureNumericCellPositive")) {
                                    fontColor = "Green";
                                } else if (css.equalsIgnoreCase("measureNumericCellNegative")) {
                                    fontColor = "Red";
                                } //                        else{  fontColor = "black"; }
                                else {
                                    if (measData.doubleValue() > 0) {
                                        fontColor = "Green";
                                    } else if (measData.doubleValue() < 0) {
                                        fontColor = "Red";
                                    } else {
                                        fontColor = "black";
                                    }
                                }
                            } else {
                                if (css.equalsIgnoreCase("measureNumericCellPositive")) {
                                    fontColor = "Red";
                                } else if (css.equalsIgnoreCase("measureNumericCellNegative")) {
                                    fontColor = "Green";
                                } //                        else{  fontColor = "black"; }
                                else {
                                    if (measData.doubleValue() > 0) {
                                        fontColor = "Red";
                                    } else if (measData.doubleValue() < 0) {
                                        fontColor = "Green";
                                    } else {
                                        fontColor = "black";
                                    }
                                }
                            }
                        }
                    } else {

                        css = "measureNonNumericCell";
                        if (facade.isScriptColorEnable(element)) {
                            measureType = facade.getmeasureType(element);
                            //added by Nazneen
                            if (measureType == null || measureType.equalsIgnoreCase("null") || measureType.equalsIgnoreCase("none")) {
                                measureType = "Standard";
                            }
                            fontColor = facade.getFontColorForscript(measData.doubleValue(), measureType, element);
                        }
                        //fontColorList.add("");
                    }
                } else {
                    if ("N".equals(dataTypes.get(i))) {
                        css = "measureNumericCell";
                        if (facade.isSignApplicable(element)) {
                            String signStyle = facade.getSignStyleforMeasure(element, actualRow);
                            if ("positive".equals(signStyle)) {
                                css = "measureNumericCellPositive";
                            } else if ("negative".equals(signStyle)) {
                                css = "measureNumericCellNegative";
                            } else {
                                css = "measureNumericCell";
                            }
                        }
                    } else {
                        css = "measureNonNumericCell";
                    }
                    fontColor = facade.getFontcolor(element);
                }
                anchors.add("#");

                if (facade.isIndicatorEnabled(element)) {
                    imagePath = facade.getIndicatorImage(actualRow, element);
                    css = "measureNumericIndicator";
                }
                if (facade.getAlignment(element) == null || facade.getAlignment(element).toString().equalsIgnoreCase("")) {
                    if (isCrossTabReport) {
                        if (facade.getTextAlign(actualRow, crosstabMeasureId.get(element)) != null) {
                            //
                            if (crosstabMeasureId.get(element) != null) {
                                if (facade.getTextAlign(actualRow, crosstabMeasureId.get(element).toString()) != null) {
                                    textAlign = facade.getTextAlign(actualRow, crosstabMeasureId.get(element).toString());
                                    if (textAlign != null && textAlign.equalsIgnoreCase("")) {
                                        if (!isDimension) {
                                            textAlign = "right";
                                        }
                                    }
                                }
                            } else {
                                textAlign = "right";
                            }
                        }
                    } else {
                        if (facade.getTextAlign(actualRow, element) != null) {
                            textAlign = facade.getTextAlign(actualRow, element);
                            if (textAlign == null || textAlign.equalsIgnoreCase("")) {
                                if (!isDimension) {
                                    textAlign = "right";
                                }
                            }
                        }
                    }
                } else {
                    if (facade.getAlignment(element).toString().equalsIgnoreCase("")) {
                        if (facade.getTextAlign(actualRow, element) != null) {
                            textAlign = facade.getTextAlign(actualRow, element);
                            if (textAlign == null || textAlign.equalsIgnoreCase("")) {
                                if (!isDimension) {
                                    textAlign = "right";
                                }
                            }
                        }
                    } else {
                        textAlign = facade.getAlignment(element);
                    }
                }

                if (facade.isExcelDisplay()) {
                    //Check whethere there's any Excel Cell Property for this Cell
                    ExcelCellFormat cell = facade.getExcelCellProperty(actualRow, element);
                    if (cell != null) {
                        if (cell.getFontColor() != null) {
                            fontColor = cell.getFontColor().toString();
                        }
                        if (cell.getBgColor() != null) {
                            bgColor = cell.getBgColor().toString();
                        }
                        commentList.add(cell.getComment());
                        if (cell.getComment() != null) {
                            css = css + " jSheetCommentWindow";
                        }
                    } else {
                        commentList.add(null);
                    }

                    editable = facade.isEditable(element);
                    formula = facade.getformula(actualRow, element);
                    /*
                     * RunTimeExcelColumn col =
                     * facade.getRTExcelColumn(element); if (col != null){
                     * editable = true; cellData = col.getData(actualRow);
                    }
                     */
                }
            }

//            if ("N".equals(dataTypes.get(i))) {
//                cellData = this.getFormattedMeasureData(actualRow, element);
//                bgColor=this.getColor(actualRow, element);
//            } else if ("C".equals(dataTypes.get(i))) {
//                cellData = this.getDimensionData(actualRow, element);
//                bgColor="";
//            } else if ("D".equals(dataTypes.get(i))) {
//                cellData = this.getDateData(actualRow, element);
//                bgColor="";
//            }
            //added by Nazneen  for Text color = white when Color group is applied
            if (bgColor != null && !bgColor.equalsIgnoreCase("") && !bgColor.equalsIgnoreCase("null") && !bgColor.equalsIgnoreCase(" ")) {
                fontColor = "white";
            }
            fontColorList.add(fontColor);
//            if(cellData.equals("0"))
//            rowData.add("");
//            else

//            String substrparams=container.getDateSubStringValues();
//           if(!substrparams.equals("")) {
//               String dateSubStrvalue=container.getDateSubStringValues();
//               String[] dateSubStrvalues=dateSubStrvalue.split(",");
//            String cellData1=cellData.toString();
//          cellData =cellData1.substring(Integer.parseInt(dateSubStrvalues[0]),Integer.parseInt(dateSubStrvalues[1]));
//           }
            String dateandtimeoption = "";
            if (facade.container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                dateandtimeoption = facade.getDateandTimeOptions(crosstabMeasureId.get(element));
            } else {
                dateandtimeoption = facade.getDateandTimeOptions(element);
            }
            if (dateandtimeoption != null) {
                if (dateandtimeoption.equals("Only Date") && cellData != null && !cellData.toString().equalsIgnoreCase("") && !cellData.toString().equalsIgnoreCase("0")) {
                    //modified by Mayank
                    //String cellData1=cellData.toString();
                    //cellData1 = cellData1.trim();
                    String cellData1 = cellData2.toString();
                    if (cellData1 != null && !cellData1.equalsIgnoreCase("null") && !cellData1.equalsIgnoreCase("") && !cellData1.isEmpty()) {
                        cellData1 = cellData1.trim();
                        String[] dateandtime = cellData1.split(" ");
                        // cellData=dateandtime[0];
                        cellData = cellData1;
                    } else {
                        cellData = "";
                    }
                }
            }
            if (dateandtimeoption != null) {
                if (dateandtimeoption.equals("Only Time") && cellData != null && !cellData.toString().equalsIgnoreCase("") && !cellData.toString().equalsIgnoreCase("0")) {
                    //modified by Mayank
//                String cellData1=cellData.toString();
                    String cellData1 = cellData2.toString();
                    if (cellData1 != null && !cellData1.equalsIgnoreCase("null") && !cellData1.equalsIgnoreCase("") && !cellData1.isEmpty()) {
                        cellData1 = cellData1.trim();
                        String[] dateandtime = cellData1.split(" ");
                        if (dateandtime.length >= 2) {
                            cellData = dateandtime[1];
                        }
                    } else {
                        cellData = "";
                    }
                }
            }
            String dateSubStrvalue = null;
            dateSubStrvalue = facade.getDateSubStringValues(element);
            //if((!"".equals(dateSubStrvalue)) && (dateSubStrvalue!=null) && (flag>0))
            if (dateSubStrvalue != null && !dateSubStrvalue.equalsIgnoreCase("null") && !"".equals(dateSubStrvalue)) {

                String[] dateSubStrvalues = dateSubStrvalue.split(",");
                String cellData1 = cellData.toString();
                cellData1 = cellData1.trim();



                int length = cellData1.length();
                int startIndex = Integer.parseInt(dateSubStrvalues[0]);
                int endIndex = Integer.parseInt(dateSubStrvalues[1]);
                if (endIndex < length) {
                    cellData = cellData1.toString().substring(startIndex, endIndex);
                }
            }
            String element1 = "";
            if (facade.container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                String crosstabelemen = crosstabMeasureId.get(element);
                if (crosstabelemen != null && !crosstabelemen.equalsIgnoreCase("null")) {
                    element1 = crosstabelemen;
                }
            }
            if (element1 != null && element1.equalsIgnoreCase("")) {
                element1 = element;
            }
            if (facade.getDateFormatt(element1) != null && !facade.getDateFormatt(element1).equalsIgnoreCase("null") && !"".equals(facade.getDateFormatt(element1)) && cellData != null && !cellData.toString().equalsIgnoreCase("") && !cellData.toString().equalsIgnoreCase("0")) {
                String dateformat = facade.getDateFormatt(element1);
                cellData = facade.getDateData(actualRow, element);
                String cellData1 = cellData.toString();
                cellData1 = cellData1.trim();
                cellData1 = cellData1.split(" ")[0];
                SimpleDateFormat format1;
//            if(element1!=null && !element1.equalsIgnoreCase("") && element1.equalsIgnoreCase("TIME")){
//               format1 = new SimpleDateFormat("MM-dd-yyyy");
//            }else{
                format1 = new SimpleDateFormat("yyyy-MM-dd");
//            }

                SimpleDateFormat format2 = new SimpleDateFormat(dateformat);
                Date date = null;
                try {
                    if (cellData1 != null && !cellData1.equalsIgnoreCase("null") && !cellData1.isEmpty() && !cellData1.equalsIgnoreCase("")) {
                        date = format1.parse(cellData1.toString().trim());
                        cellData = format2.format(date);
                    } else {
                        cellData = "";
                    }
                } catch (ParseException ex) {
                    logger.error("Exception:", ex);
                }

            }
            flag = 1;
            //start of code by Nazneen
            if (element.contains("rankST")) {
                if (cellData.toString().contains(".")) {
                    int cnt = cellData.toString().indexOf(".");
                    cellData = cellData.toString().substring(0, cnt);
                }
            }
            if (cellData.toString().equalsIgnoreCase("-0") || cellData.toString().equalsIgnoreCase("-0.") || cellData.toString().equalsIgnoreCase("-00.") || cellData.toString().equalsIgnoreCase("-0.0") || cellData.toString().equalsIgnoreCase("-0.00")) {
                cellData = "0";
            }
            if (cellData.toString().equalsIgnoreCase("-00.0") || cellData.toString().equalsIgnoreCase("-00.00") || cellData.toString().equalsIgnoreCase("-00.") || cellData.toString().equalsIgnoreCase("-00")) {
                cellData = "0";
            }
            if (cellData.toString().contains("-00.000") || cellData.toString().contains("-0.000")) {
                cellData = "0";
            }
            //End of code by Nazneen
            rowData.add(cellData);
            colorList.add(bgColor);
            cssClass.add(css);
            editableList.add(editable);
            formulaList.add(formula);
            imagePathList.add(imagePath);
            textAlignList.add(textAlign);
            finalrepDrillUrl += reportDrillUrl + tempReportDrill.toString() + "&reportDrill=Y";//reportDrill=Y means assigned report is not there in session in that it is useful calling updatecollection  for passing date from assigning report to assigned report
            //
            //written by swati
            if (facade.container.isReportCrosstab()) {

                if (!finalCrossTabReportDrillMap.containsKey(element)) {
                    reportDrillList.add("#");
                } else if (finalCrossTabReportDrillMap.get(element) != null && finalCrossTabReportDrillMap.get(element).equalsIgnoreCase("0")) {
                    reportDrillList.add("#");
                } else {
                    reportDrillList.add(finalrepDrillUrl);
                }
            } else {
                if (!facade.container.getReportCollect().reportDrillMap.containsKey(element)) {
                    reportDrillList.add("#");
                } else if (facade.container.getReportDrillMap(element) != null && facade.container.getReportDrillMap(element).equalsIgnoreCase("0")) {
                    reportDrillList.add("#");
                } else {
                    reportDrillList.add(finalrepDrillUrl);
                }
            }
            tempReportDrill = new StringBuffer();
            selfreportDrillList.add(selfReportDimurl + selfReportDrill);
        }
        row.setRowData(rowData);
        row.setRowDataIds(facade.getDisplayColumns());
        row.setRowNumber(actualRow);
        this.computeSubtotals(row);
        row.setColorList(colorList);
        row.setCssClass(cssClass);
        row.setAnchors(anchors);
        row.setFontColors(fontColorList);
        row.setCommentList(commentList);
        row.setEditableList(editableList);
        row.setFormulaList(formulaList);
        row.setImagePath(imagePathList);
        if (!drillAcrossData.equalsIgnoreCase("")) {
            row.setDrillAcrossData(drillAcrossData.substring(1));
        } else {
            row.setDrillAcrossData(drillAcrossData);
        }
        row.setTextAlign(textAlignList);
        row.setReportParameters(facade.getReportParameters());
        row.setReportParameterNames(facade.getReportParameterNames());
        row.setViewbyId(facade.getViewbyId());
        row.setReportParameterValues(facade.getReportParameterValues());
        row.setViewbyCount(facade.getViewByCount());
        row.setparameterHash(facade.getparameterHash());
        //row.setAdhocUrl(adhocUrl.replace("null", ""));
        row.setAdhocTimeUrl(adhocTimeurl);
        row.setAdhocParamDrillUrl(facade.getadhocParamUrl());
        row.setMsrdrillUrl(msrDrillUrl.substring(1));
        row.setReportDrillList(reportDrillList);
//        row.setSelfReportDrill(selfReportDrill);
        row.setSelfreportDrillList(selfreportDrillList);
//        row.setDrillMap(facade.getDrillMap());
        ArrayList<Boolean> displayStyle = new ArrayList<Boolean>();
        Predicate stHolderPredicate;
        int j;
        if (facade.isAnySubtotallingRequired()) {
            for (j = 0; j < facade.getViewByCount() - 1; j++) {
                stHolderPredicate = SubTotalHolder.getFirstCellIdPredicate(row.getID(j));
                Iterator stHolderIter = Iterables.filter(Arrays.asList(subTotalHolder), stHolderPredicate).iterator();

                if (stHolderIter.hasNext()) {
                    displayStyle.add(facade.isColumnVisible(row.getMeasrId(j)));
//                    displayStyle.add(Boolean.TRUE);
                } else {
                    displayStyle.add(Boolean.FALSE);
                }
            }
        } else {
            for (j = 0; j < facade.getViewByCount() - 1; j++) {
                displayStyle.add(facade.isColumnVisible(facade.getColumnId(j)));
            }
//                displayStyle.add(Boolean.TRUE);
        }

        j = facade.getViewByCount() - 1;
//        displayStyle.add(Boolean.TRUE);
        displayStyle.add(facade.isColumnVisible(facade.getColumnId(j)));// value j is here j = facade.getViewByCount() - 1;

        for (j = facade.getViewByCount(); j < getColumnCount(); j++) {
            displayStyle.add(facade.isColumnVisible(facade.getColumnId(j)));
        }
        row.setDisplayStyle(displayStyle);

        //facade.initializeGraphDataSet(rowNum);
        if (!this.computedSubTotLst.isEmpty() && rowNum != 0) {
            row.setPrintSubTotals(true);
            try {
                Iterables.find(computedSubTotLst, SubTotalHolder.isSubtotalFoundForFirstRowView());
                row.setSubTotalFoundForFirstViewBy(true);
            } catch (NoSuchElementException nse) {
            }
        }

        if (facade.isReportCrosstab() && facade.isDynamicRowsDisplayedInCrosstab()) {
            CtDynamicRowTableHelper dynamicRowHelper = new CtDynamicRowTableHelper(facade);
            row = dynamicRowHelper.createDynamicRow(row);
        }
        return row;
    }

    public TableDataRow getRowData1(int rowNum) {
        TableDataRow row = new TableDataRow();
        ArrayList dataTypes = facade.getDataTypes();
        ArrayList<String> displayCols = facade.getDisplayColumns();
        ArrayList<Object> rowData = new ArrayList<Object>();
        ArrayList<String> commentList = new ArrayList<String>();
        ArrayList<Boolean> editableList = new ArrayList<Boolean>();
        ArrayList<String> formulaList = new ArrayList<String>();
        ArrayList<String> imagePathList = new ArrayList<String>();
        HashMap<String, ArrayList<Double>> rowdataMap = new HashMap();
        int actualRow = facade.getActualRow(rowNum);
        facade.setActualrow(actualRow);
        facade.setRowForSTRank(rowNum);
        ReportTemplateDAO dao = new ReportTemplateDAO();
        Object cellData = "";
        String measureType = "";
        boolean isDimension;
        String css = "";
        String cellData2 = "";
        ArrayList displayTypes = facade.getDisplayTypes();
        ArrayList links = facade.getLinks();
        HashMap replinks = facade.getrepLinks();
        Boolean editable = false;
        String formula = null;
        String imagePath = null;

        Set<String> repvalues;
        String currentViewBy = "";
        PbReportViewerDAO daoonnsrattr = new PbReportViewerDAO();
//                HashMap mapformsrattr=daoonnsrattr.modifyMeasureAttrreport(pbReportId,facade.container);
//                facade.container.setmodifymeasureAttrChnge(mapformsrattr);
        StringBuffer tempReportDrill = new StringBuffer();
        HashMap<String, ArrayList> nonViewByMapNew = null;
        ArrayList<String> colVallist = new ArrayList<String>();
        List<String> summarizedlist = null;
        nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
        String colViewby = null;
        boolean isCrossTabReport = false;
        if (facade.container.getReportCollect().reportColViewbyValues != null && facade.container.getReportCollect().reportColViewbyValues.size() > 0) {
            colViewby = (String) facade.container.getReportCollect().reportColViewbyValues.get(0);
            isCrossTabReport = true;
        }
        HashMap<String, String> finalCrossTabReportDrillMap = ((PbReturnObject) facade.container.getRetObj()).finalCrossTabReportDrillMap;
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
        TreeSet<Integer> index = new TreeSet<Integer>();
        ArrayList<Integer> val1 = new ArrayList<Integer>();
        ArrayList<String> Measures = new ArrayList<String>();
        if (facade.getDisplayColumns().get(0).equals("TIME")) {
            for (int i = 0; i < facade.getViewSequence().size(); i++) {
                index.add(facade.getActualRow(i));
            }
            val1.addAll(index);
            actualRow = facade.getViewSequence().get(rowNum);

        }

        for (int i = 0; i < getColumnCount(); i++) {
            if (i < facade.getViewByCount()) {
                isDimension = true;
            } else {
                isDimension = false;
            }
            if (isDimension) {
                String element = displayCols.get(i);
                if (replinks != null && !replinks.isEmpty()) {
                    repvalues = replinks.keySet();
                    if (repvalues.contains(element.replace("A_", "").trim())) {
                        currentViewBy = dao.getCurrentViewbysofRep(replinks.get(element.replace("A_", "").trim()).toString());
                    }
                }
            } else {
                if (isCrossTabReport) {
                    String element = displayCols.get(i);
                    if (!element.contains("_")) {
                        String actualMsrs = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId.get(element);
                        if (!Measures.contains(actualMsrs)) {
                            Measures.add(actualMsrs);
                        }
                        ArrayList<Double> RowDatavalues = new ArrayList();
                        BigDecimal value = BigDecimal.ZERO;
                        RowDatavalues = rowdataMap.get(actualMsrs);
                        if (RowDatavalues == null) {
                            RowDatavalues = new ArrayList();
                        }
                        double RwValue = facade.getValue(actualRow, element);
                        facade.getFormattedMeasureData(rowNum, i);
//                    if(!CtAvgdata.isEmpty()){
//                          value=new BigDecimal(CtAvgdata.replaceAll(",", ""));
//                     }

                        RowDatavalues.add(RwValue);
                        Collections.sort(RowDatavalues);
                        rowdataMap.put(actualMsrs, RowDatavalues);

//                    facade.rowdataMap=rowdataMap;
                    }
                }
            }
        }
        HashMap<String, HashMap<Double, String>> colorcodeMap = new HashMap();
        colorcodeMap = Setcolorcodes(rowdataMap);
        facade.setColorcodeMap(colorcodeMap);
        facade.setMeasures(Measures);
        int flag = 0;
        Gson gson = new Gson();
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        List cellDataList = new ArrayList();
        List cellDataLst = new ArrayList();
        String selfReportDimurl = "";
        for (int i = 0; i < getColumnCount(); i++) {
            //
            String selfReportDrill = "";
            String finalrepDrillUrl = facade.getContextPath() + "/reportViewer.do?reportBy=viewReport";
            String element = displayCols.get(i);
            editable = false;
            formula = null;
            imagePath = null;

            if (i < facade.getViewByCount()) {
                isDimension = true;
            } else {
                isDimension = false;
            }

            if (isDimension) {

                if ("D".equals(dataTypes.get(i))) {
                    cellData = facade.getDateData(actualRow, element);
                    cellDataList.clear();
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");  //fetched format
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); //new format..
                    Date date = null;
                    try {
                        if (cellData != null && !cellData.toString().equalsIgnoreCase("null") && !cellData.toString().isEmpty() && !cellData.toString().equalsIgnoreCase("")) {
                            date = format1.parse(cellData.toString().trim());
                            cellData = format2.format(date);
                            cellData2 = cellData.toString();
                            String cellData1 = cellData.toString();
                            cellData1 = cellData1.trim();
                            String[] dateandtime = cellData1.split(" ");      //added by mayank on 12/05/14(Split date and time.)
                            cellData = dateandtime[0];
                        } else {
                            cellData = "";
                        }
                    } catch (ParseException ex) {
                        logger.error("Exception:", ex);
                    }
                    cellDataList.add(cellData);
                } else {
                    cellData = facade.getDimensionData(actualRow, element);
                    cellDataList.clear();
                    cellDataList.add(cellData);
                }

                css = "dimensionCell";
                if ("H".equalsIgnoreCase(String.valueOf(displayTypes.get(i)))) {

                    if (replinks != null && !replinks.isEmpty()) {
                        repvalues = replinks.keySet();
                        if (repvalues.contains(element.replace("A_", "").trim())) {
//                        tempUrl = "reportViewer.do?reportBy=viewReport&REPORTID="+replinks.get(element.replace("A_", "").trim())+"&CBOVIEW_BY"+currentViewBy.trim()+"="+element.replace("A_", "").trim();
                        } else {
                            String dimData = facade.getDimensionData(actualRow, element); //.replace(" ", "%20").replace("&", "%26")
                            List<String> dimDataList = new ArrayList<String>();
                            dimDataList.add(dimData);
                        }

                    } else {
                        String dimData = facade.getDimensionData(actualRow, element); //.replace(" ", "%20").replace("&", "%26")
                        List<String> dimDataList = new ArrayList<String>();
                        dimDataList.add(dimData);
                    }

                } else {
                    commentList.add(null);
                }
            } else {
                //written by swati.
                if (facade.container.isReportCrosstab()) {
                    if (finalCrossTabReportDrillMap.containsKey(element)) {
                        finalrepDrillUrl += "&REPORTID=" + finalCrossTabReportDrillMap.get(element);
                    }
                } else {
                    finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(element);
                }
                // 
                if (nonViewByMapNew != null && nonViewByMapNew.size() > 0 && nonViewByMapNew.containsKey(element)) {
//                    cellDataList.clear();
//                    cellDataList.add(cellData);
                    cellDataLst.clear();
                    colVallist = (ArrayList<String>) nonViewByMapNew.get(element);
                    cellDataLst.add((String) colVallist.get(0));
                    if (colViewby.equalsIgnoreCase("TIME")) {
                        tempReportDrill.append(facade.container.getReportCollect().resolveTimeDrill(facade.container.getReportCollect().timeDetailsArray) + (String) colVallist.get(0) + "&DDrillAcross=Y");
                    } else {
                        if (facade.container.getSummerizedTableHashMap() != null && !facade.container.getSummerizedTableHashMap().isEmpty()) {
                            HashMap summarizedmMesMap = facade.container.getSummerizedTableHashMap();
                            summarizedlist = (List<String>) summarizedmMesMap.get("summerizedQryeIds");
                        }
                        if (summarizedlist == null || !summarizedlist.contains(element.toString().replace("A_", ""))) {
                            tempReportDrill.append("&CBOARP" + colViewby + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataLst, tarType)));
                        }
                    }
                    selfReportDrill += "&CBOARP" + colViewby + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataLst, tarType));

                }

                // started by Mayank...
                if ("D".equals(dataTypes.get(i))) {
                    cellData = facade.getDateData(actualRow, element);
                    cellDataList.clear();
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");  //fetched format
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); //new format..
                    Date date = null;
                    try {
                        if (cellData != null && !cellData.toString().equalsIgnoreCase("null") && !cellData.toString().isEmpty() && !cellData.toString().equalsIgnoreCase("")) {
                            date = format1.parse(cellData.toString().trim());
                            cellData = format2.format(date);
                            cellData2 = cellData.toString();
                            String cellData1 = cellData.toString();
                            cellData1 = cellData1.trim();
                            String[] dateandtime = cellData1.split(" ");      //added by mayank on 12/05/14(Split date and time.)
                            cellData = dateandtime[0];
                        } else {
                            cellData = "";
                        }
                        cellDataList.add(cellData);

                    } catch (ParseException ex) {
                        logger.error("Exception:", ex);
                    }
                } else {
                    cellData = facade.getFormattedMeasureData(actualRow, i);
                    cellDataList.clear();
                    cellDataList.add(cellData);
                    //

                }
                // end of code..

                cellData = facade.getFormattedMeasureData(actualRow, i);
                BigDecimal measData = facade.getMeasureData(actualRow, element);// changed by veena
//                if (facade.container.getmodifymeasureAttrChng() != null && facade.container.getmodifymeasureAttrChng().size() != 0) {
                HashMap NFMap = new HashMap();
                String nbrSymbol = "";
                String Symbol = "";
                HashMap colProps = facade.container.getColumnProperties();
                ArrayList propList;
                if (isCrossTabReport && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                    propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                } else {
                    propList = (ArrayList) colProps.get(element);
                }
                if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                    String colSymbol = (String) propList.get(7);
                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                        facade.container.symbol.put(element, colSymbol);
                    }
                }
//
                NFMap = (HashMap) facade.container.getTableHashMap().get("NFMap");
                nbrSymbol = facade.container.symbol.get(element);

                if (NFMap != null) {
//             nbrSymbol = String.valueOf(NFMap.get(element));
                    if (isCrossTabReport && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                        if (NFMap.get(crosstabMeasureId.get(element)) != null) {
                            Symbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                        }
                    } else if (NFMap.get(element) != null) {
                        Symbol = String.valueOf(NFMap.get(element));
                    }

                }
//               cellData=Symbol+cellData;
                if (nbrSymbol != null) {
                    if (nbrSymbol.equalsIgnoreCase("%")) {
                        cellData = cellData + "" + nbrSymbol;
                    } else {
                        cellData = nbrSymbol + "" + cellData;
                    }
                }
//              if(Symbol!=null && !Symbol.equalsIgnoreCase("Nf")){
//              if(nbrSymbol!=null && nbrSymbol.equalsIgnoreCase("%"))
//                  cellData=cellData;
//                 else
//                cellData=cellData+" "+Symbol;
////                  cellData=cellData;
//              }

//                }
                if (facade.getBGcolor(element) == null || facade.getBGcolor(element).toString().equalsIgnoreCase("")) {
                    BigDecimal subtotalval = null;
                    if (facade.container.getViewByCount() <= 1 && facade.getAggregationType(element) != null && !facade.getAggregationType(element).toString().equalsIgnoreCase("null") && facade.getAggregationType(element).toString().equalsIgnoreCase("avg")) {
                        subtotalval = getGTAVGVal(i, element);
                    } else if (facade.getAggregationType(element) != null && !facade.getAggregationType(element).toString().equalsIgnoreCase("null") && facade.getAggregationType(element).toString().equalsIgnoreCase("avg") && !facade.isAnySubtotallingRequired()) {
                        subtotalval = getGTAVGVal(i, element);
                    } else {
                        // subtotalval = getCatSTVal(i,element) ;
                    }


                } else {
                }
                if (facade.getFontcolor(element) == null || facade.getFontcolor(element).toString().equalsIgnoreCase("")) {
                    if ("N".equals(dataTypes.get(i))) {
                        css = "measureNumericCell";
                        if (facade.isSignApplicable(element)) {
                            String signStyle = facade.getSignStyleforMeasure(element, actualRow);
                            if ("positive".equals(signStyle)) {
                                css = "measureNumericCellPositive";
                            } else if ("negative".equals(signStyle)) {
                                css = "measureNumericCellNegative";
                            } else {
                                css = "measureNumericCell";
                            }
                        }
                        if (facade.isFontColorApplicable(element)) {
                            //fontColorList.add(fontColor);
                        } else {
                            //fontColorList.add("");
                        }
                        if (facade.isScriptColorEnable(element)) {
                            measureType = facade.getmeasureType(element);
                            //added by nazneen
                            if (measureType == null || measureType.equalsIgnoreCase("null") || measureType.equalsIgnoreCase("none")) {
                                measureType = "Standard";
                            }
//                        fontColor = facade.getFontColorForscript(measData.doubleValue(),measureType,element);
                            if (measureType.equalsIgnoreCase("Standard")) {
                            } else {
                            }
                        }
                    } else {

                        css = "measureNonNumericCell";
                        if (facade.isScriptColorEnable(element)) {
                            measureType = facade.getmeasureType(element);
                            //added by Nazneen
                            if (measureType == null || measureType.equalsIgnoreCase("null") || measureType.equalsIgnoreCase("none")) {
                                measureType = "Standard";
                            }
                        }
                        //fontColorList.add("");
                    }
                } else {
                    if ("N".equals(dataTypes.get(i))) {
                        css = "measureNumericCell";
                        if (facade.isSignApplicable(element)) {
                            String signStyle = facade.getSignStyleforMeasure(element, actualRow);
                            if ("positive".equals(signStyle)) {
                                css = "measureNumericCellPositive";
                            } else if ("negative".equals(signStyle)) {
                                css = "measureNumericCellNegative";
                            } else {
                                css = "measureNumericCell";
                            }
                        }
                    } else {
                        css = "measureNonNumericCell";
                    }
                }

                if (facade.isIndicatorEnabled(element)) {
                    imagePath = facade.getIndicatorImage(actualRow, element);
                    css = "measureNumericIndicator";
                }

                if (facade.isExcelDisplay()) {
                    //Check whethere there's any Excel Cell Property for this Cell
                    ExcelCellFormat cell = facade.getExcelCellProperty(actualRow, element);


                    editable = facade.isEditable(element);
                    formula = facade.getformula(actualRow, element);

                }
            }


            String dateandtimeoption = "";
            if (facade.container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                dateandtimeoption = facade.getDateandTimeOptions(crosstabMeasureId.get(element));
            } else {
                dateandtimeoption = facade.getDateandTimeOptions(element);
            }
            if (dateandtimeoption != null) {
                if (dateandtimeoption.equals("Only Date") && cellData != null && !cellData.toString().equalsIgnoreCase("") && !cellData.toString().equalsIgnoreCase("0")) {
                    //modified by Mayank
                    //String cellData1=cellData.toString();
                    //cellData1 = cellData1.trim();
                    String cellData1 = cellData2.toString();
                    if (cellData1 != null && !cellData1.equalsIgnoreCase("null") && !cellData1.equalsIgnoreCase("") && !cellData1.isEmpty()) {
                        cellData1 = cellData1.trim();
                        String[] dateandtime = cellData1.split(" ");
                        // cellData=dateandtime[0];
                        cellData = cellData1;
                    } else {
                        cellData = "";
                    }
                }
            }
            if (dateandtimeoption != null) {
                if (dateandtimeoption.equals("Only Time") && cellData != null && !cellData.toString().equalsIgnoreCase("") && !cellData.toString().equalsIgnoreCase("0")) {
                    //modified by Mayank
//                String cellData1=cellData.toString();
                    String cellData1 = cellData2.toString();
                    if (cellData1 != null && !cellData1.equalsIgnoreCase("null") && !cellData1.equalsIgnoreCase("") && !cellData1.isEmpty()) {
                        cellData1 = cellData1.trim();
                        String[] dateandtime = cellData1.split(" ");
                        if (dateandtime.length >= 2) {
                            cellData = dateandtime[1];
                        }
                    } else {
                        cellData = "";
                    }
                }
            }
            String dateSubStrvalue = null;
            dateSubStrvalue = facade.getDateSubStringValues(element);
            //if((!"".equals(dateSubStrvalue)) && (dateSubStrvalue!=null) && (flag>0))
            if (dateSubStrvalue != null && !dateSubStrvalue.equalsIgnoreCase("null") && !"".equals(dateSubStrvalue)) {

                String[] dateSubStrvalues = dateSubStrvalue.split(",");
                String cellData1 = cellData.toString();
                cellData1 = cellData1.trim();



                int length = cellData1.length();
                int startIndex = Integer.parseInt(dateSubStrvalues[0]);
                int endIndex = Integer.parseInt(dateSubStrvalues[1]);
                if (endIndex < length) {
                    cellData = cellData1.toString().substring(startIndex, endIndex);
                }
            }
            String element1 = "";
            if (facade.container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                String crosstabelemen = crosstabMeasureId.get(element);
                if (crosstabelemen != null && !crosstabelemen.equalsIgnoreCase("null")) {
                    element1 = crosstabelemen;
                }
            }
            if (element1 != null && element1.equalsIgnoreCase("")) {
                element1 = element;
            }
            if (facade.getDateFormatt(element1) != null && !facade.getDateFormatt(element1).equalsIgnoreCase("null") && !"".equals(facade.getDateFormatt(element1)) && cellData != null && !cellData.toString().equalsIgnoreCase("") && !cellData.toString().equalsIgnoreCase("0")) {
                String dateformat = facade.getDateFormatt(element1);
                cellData = facade.getDateData(actualRow, element);
                String cellData1 = cellData.toString();
                cellData1 = cellData1.trim();
                cellData1 = cellData1.split(" ")[0];
                SimpleDateFormat format1;
//            if(element1!=null && !element1.equalsIgnoreCase("") && element1.equalsIgnoreCase("TIME")){
//               format1 = new SimpleDateFormat("MM-dd-yyyy");
//            }else{
                format1 = new SimpleDateFormat("yyyy-MM-dd");
//            }

                SimpleDateFormat format2 = new SimpleDateFormat(dateformat);
                Date date = null;
                try {
                    if (cellData1 != null && !cellData1.equalsIgnoreCase("null") && !cellData1.isEmpty() && !cellData1.equalsIgnoreCase("")) {
                        date = format1.parse(cellData1.toString().trim());
                        cellData = format2.format(date);
                    } else {
                        cellData = "";
                    }
                } catch (ParseException ex) {
                    logger.error("Exception:", ex);
                }

            }
            flag = 1;
            //start of code by Nazneen
            if (element.contains("rankST")) {
                if (cellData.toString().contains(".")) {
                    int cnt = cellData.toString().indexOf(".");
                    cellData = cellData.toString().substring(0, cnt);
                }
            }
            if (cellData.toString().equalsIgnoreCase("-0") || cellData.toString().equalsIgnoreCase("-0.") || cellData.toString().equalsIgnoreCase("-00.") || cellData.toString().equalsIgnoreCase("-0.0") || cellData.toString().equalsIgnoreCase("-0.00")) {
                cellData = "0";
            }
            if (cellData.toString().equalsIgnoreCase("-00.0") || cellData.toString().equalsIgnoreCase("-00.00") || cellData.toString().equalsIgnoreCase("-00.") || cellData.toString().equalsIgnoreCase("-00")) {
                cellData = "0";
            }
            if (cellData.toString().contains("-00.000") || cellData.toString().contains("-0.000")) {
                cellData = "0";
            }
            //End of code by Nazneen
            rowData.add(cellData);
            editableList.add(editable);
            formulaList.add(formula);
            imagePathList.add(imagePath);
        }
        row.setRowData(rowData);
        row.setRowDataIds(facade.getDisplayColumns());
        row.setRowNumber(actualRow);
        this.computeSubtotals(row);
        row.setCommentList(commentList);
        row.setEditableList(editableList);
        row.setFormulaList(formulaList);
        row.setImagePath(imagePathList);
        row.setReportParameters(facade.getReportParameters());
        row.setReportParameterNames(facade.getReportParameterNames());
        row.setViewbyId(facade.getViewbyId());
        row.setReportParameterValues(facade.getReportParameterValues());
        row.setViewbyCount(facade.getViewByCount());
        row.setparameterHash(facade.getparameterHash());
        row.setAdhocParamDrillUrl(facade.getadhocParamUrl());


//        row.setDrillMap(facade.getDrillMap());
        ArrayList<Boolean> displayStyle = new ArrayList<Boolean>();
        Predicate stHolderPredicate;
        int j;
        if (facade.isAnySubtotallingRequired()) {
            for (j = 0; j < facade.getViewByCount() - 1; j++) {
                stHolderPredicate = SubTotalHolder.getFirstCellIdPredicate(row.getID(j));
                Iterator stHolderIter = Iterables.filter(Arrays.asList(subTotalHolder), stHolderPredicate).iterator();

                if (stHolderIter.hasNext()) {
                    displayStyle.add(facade.isColumnVisible(row.getMeasrId(j)));
//                    displayStyle.add(Boolean.TRUE);
                } else {
                    displayStyle.add(Boolean.FALSE);
                }
            }
        } else {
            for (j = 0; j < facade.getViewByCount() - 1; j++) {
                displayStyle.add(facade.isColumnVisible(facade.getColumnId(j)));
            }
//                displayStyle.add(Boolean.TRUE);
        }

        j = facade.getViewByCount() - 1;
//        displayStyle.add(Boolean.TRUE);
        displayStyle.add(facade.isColumnVisible(facade.getColumnId(j)));// value j is here j = facade.getViewByCount() - 1;

        for (j = facade.getViewByCount(); j < getColumnCount(); j++) {
            displayStyle.add(facade.isColumnVisible(facade.getColumnId(j)));
        }
        row.setDisplayStyle(displayStyle);

        //facade.initializeGraphDataSet(rowNum);
        if (!this.computedSubTotLst.isEmpty() && rowNum != 0) {
            row.setPrintSubTotals(true);
            try {
                Iterables.find(computedSubTotLst, SubTotalHolder.isSubtotalFoundForFirstRowView());
                row.setSubTotalFoundForFirstViewBy(true);
            } catch (NoSuchElementException nse) {
            }
        }
        if (facade.isReportCrosstab() && facade.isDynamicRowsDisplayedInCrosstab()) {
            CtDynamicRowTableHelper dynamicRowHelper = new CtDynamicRowTableHelper(facade);
            row = dynamicRowHelper.createDynamicRow(row);
        }
        return row;
    }

    protected void computeSubtotals(TableDataRow row) {
        int viewByCount = facade.getViewByCount();
        ArrayList<String> displayCols = facade.getDisplayColumns();
        ArrayList<String> dataTypes = facade.getDataTypes();
        String dimValue;
        int firstAggMeasureCol = -1;

        SubTotalHolder stHolder;
        if (facade.isAnySubtotallingRequired() || facade.isCustomTotalRequired()) {
            this.computedSubTotLst.clear();
            for (int j = viewByCount - 1; j >= 0; j--) {
                if (this.subTotalHolder[j] == null) {
                    this.subTotalHolder[j] = new SubTotalHolder();
                    this.subTotalHolder[j].setFirstCellId(row.getID(j));
                }
                stHolder = this.subTotalHolder[j];
                for (int col = viewByCount; col < getColumnCount(); col++) {
                    if (dataTypes.get(col).equals("N") || dataTypes.get(col).equals("C")) {
                        if (firstAggMeasureCol == -1) {
                            firstAggMeasureCol = col;
                        }
                        if (j == viewByCount - 1) {
                            if (facade.container.getaveragecalculationtype(row.getColumnId(col).toString()) != null && facade.container.getaveragecalculationtype(row.getColumnId(col).toString()).equalsIgnoreCase("Exclude 0") && facade.getMeasureDataForComputation(row.getRowNumber(), row.getColumnId(col)).equals(BigDecimal.ZERO)) {
                                this.subTotalHelper.doZeroCount(stHolder, row.getColumnId(col).toString());
                            }
                            this.subTotalHelper.doSubTotalCalculations(stHolder, null, displayCols.get(col), facade.getMeasureDataForComputation(row.getRowNumber(), row.getColumnId(col)), facade.getrowcount());
                        } else {
                            dimValue = facade.getDimensionData(row.getRowNumber(), displayCols.get(j));
                            if (col == firstAggMeasureCol) //first measure which aggregatable
                            {
                                this.updateSubtotalList(row);
                            }
                            stHolder = this.subTotalHolder[j];
                            if (facade.getMeasureDataForComputation(row.getRowNumber(), row.getColumnId(col)).equals(BigDecimal.ZERO)) {
                                this.subTotalHelper.doZeroCount(stHolder, row.getColumnId(col).toString());
                            }
                            this.subTotalHelper.doSubTotalCalculations(stHolder, dimValue, displayCols.get(col), facade.getMeasureDataForComputation(row.getRowNumber(), row.getColumnId(col)), facade.getrowcount());
                        }

                    }
                }
                stHolder.incrementRowCount();
                stHolder.setViewColumnNumber(j);
            }
        }
    }

    private void updateSubtotalList(TableDataRow row) {
        String dimValue;
        int rowNumber = row.getRowNumber();
        ArrayList<String> displayCols = facade.getDisplayColumns();
        SubTotalHolder stHolder;
        for (int i = 0; i < facade.getViewByCount() - 1; i++) {
            dimValue = facade.getDimensionData(rowNumber, displayCols.get(i));
            if (this.subTotalHolder[i] != null && !this.subTotalHolder[i].compareTo(dimValue)) {
                //increment all parents
                for (int j = i - 1; j >= 0; j--) {
                    for (int k = i; k < facade.getViewByCount() - 1; k++) {
                        this.subTotalHolder[j].incrementRowCountForSubTotal();
                    }
                }

                //flush out all childrens
                if ((i > 0) || (this.subTotalHolder[1].getFirstCellId() != null && i == 0)) {
                    for (int j = facade.getViewByCount() - 2; j >= i; j--) {
                        stHolder = this.subTotalHolder[j];
                        for (int incr = j; incr < facade.getViewByCount() - 2; incr++) {
                            stHolder.incrementRowCountForSubTotal();
                        }
                        this.computedSubTotLst.add(stHolder);
                        this.subTotalHolder[j] = new SubTotalHolder();
                        this.subTotalHolder[j].setFirstCellId(row.getID(j));
                        this.subTotalHolder[j].setViewColumnNumber(i);

                    }
                } else {
                    this.subTotalHolder[0].incrementRowCountForSubTotal();
                    this.computedSubTotLst.add(this.subTotalHolder[0]);
                    this.subTotalHolder[0] = new SubTotalHolder();
                    this.subTotalHolder[0].setFirstCellId(row.getID(0));
                    this.subTotalHolder[0].setViewColumnNumber(i);
                }
            }
        }
    }

    @Override
    public int getColumnCount() {
        return facade.getColumnCount();
    }

    @Override
    public int getFromColumn() {
        return 0;
    }

    public HashMap<String, HashMap<Double, String>> Setcolorcodes(HashMap<String, ArrayList<Double>> rowdataMap) {
        HashMap<String, HashMap<Double, String>> colorCodeMap = new HashMap<String, HashMap<Double, String>>();
        ArrayList<String> KeyList = new ArrayList(rowdataMap.keySet());
        for (int i = 0; i < KeyList.size(); i++) {
            ArrayList<Double> DataLIst = rowdataMap.get(KeyList.get(i));
            int length = (DataLIst.size() / 3);
            int Firstlength = length;
            int lastlength = length;
            int midlength = (DataLIst.size() - (2 * length));
            if (midlength < 0) {
                midlength = 0;
            }
            ArrayList<String> ClrCode = getColorCodes(Firstlength, midlength, lastlength);

            HashMap<Double, String> msrColorMap = new HashMap();
            for (int k = 0; k < DataLIst.size(); k++) {

                msrColorMap.put(DataLIst.get(k), ClrCode.get(k));
            }

            colorCodeMap.put(KeyList.get(i), msrColorMap);

        }

        return colorCodeMap;
    }

    public ArrayList<String> getColorCodes(int Flength, int Mlength, int Llength) {

        ArrayList codes = new ArrayList();
        ArrayList redcodes = new ArrayList();
        ArrayList yellowcodes = new ArrayList();
        ArrayList greencodes = new ArrayList();

        redcodes.add("#E60000");
        redcodes.add("#FF3333");
        redcodes.add("#FF6666");
        redcodes.add("#FF9999");
        redcodes.add("#FFCCCC");

        yellowcodes.add("#FFA319");
        yellowcodes.add("#FFE0B2");
        yellowcodes.add("#FFEDB2");
        yellowcodes.add("#FFf3B2");

        greencodes.add("#006e1b");
        greencodes.add("#00e439");
        greencodes.add("#47ff75");
        greencodes.add("#a9ffbe");

        for (int i = 0; i < Flength; i++) {
            if (greencodes.size() > i) {
                if (greencodes.get(i) != null) {
                    codes.add(greencodes.get(i));
                } else {
                    codes.add(greencodes.get(greencodes.size() - 1));
                }
            } else {
                codes.add(greencodes.get(greencodes.size() - 1));
            }
        }
        for (int j = 0; j < Mlength; j++) {
            if (yellowcodes.size() > j) {
                if (yellowcodes.get(j) != null) {
                    codes.add(yellowcodes.get(j));
                } else {
                    codes.add(yellowcodes.get(yellowcodes.size() - 1));
                }
            } else {
                codes.add(yellowcodes.get(yellowcodes.size() - 1));
            }
        }
        for (int k = 0; k < Llength; k++) {
            if (redcodes.size() > k) {
                if (redcodes.get(k) != null) {
                    codes.add(redcodes.get(k));
                } else {
                    codes.add(redcodes.get(redcodes.size() - 1));
                }
            } else {
                codes.add(redcodes.get(redcodes.size() - 1));
            }
        }

        return codes;
    }
}
