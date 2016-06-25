/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.data;

import com.progen.report.display.DisplayParameters;
import com.progen.report.display.table.TableCellSpan;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.util.stat.StatUtil;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author arun
 */
public abstract class TableBuilder {

    public static Logger logger = Logger.getLogger(TableBuilder.class);
    protected SubTotalHolder[] subTotalHolder;
    protected SubTotalHelper subTotalHelper;
    protected ArrayList<SubTotalHolder> computedSubTotLst;
    protected Set<TableCellSpan> cellSpan;
    protected int fromRow;
    protected int toRow;
    public DataFacade facade;
    protected int totalRows;
    protected String getFromOneview;
    protected String htmlCellHeight;
    public HashMap<String, String> subTotalDeviationMap = new HashMap<String, String>();
    public HashMap<String, String> subTotalHolderMap = new HashMap<String, String>();
    public String subTotalKey = null;
    public ArrayList<String> subTotalKeyArray = new ArrayList<String>();
    int zerocolumnrowcount = 0;
    public ArrayList TableDataRowListSubFiltered = new ArrayList();
    public ArrayList TableDataRowListSubunFiltered = new ArrayList();
    int STzerocolumnrowcount = 0;
    int Zerocolumnrowcrosstab = 0;
    String FirstRowAfterSubTtlFiltr = "";
    public HashMap<String, Integer> zerorepMap = new HashMap<String, Integer>();
    public HashMap<String, Integer> STzerorepMap = new HashMap<String, Integer>();
    public HashMap<String, String> zerorepcalcMap = new HashMap<String, String>();
    private boolean isCountingRowsAfterFilter = false;
    String keycs = "";
    String elemid = "";
    private Set<String> repcalc;
    String repcalcvalue = "";
    String repcalcrossvalue = "";
    Set repcalcST = new HashSet();
    int ST;
    public int cntOfSt = 0;//Bhargavi
    public boolean lastRow = false;
    public ArrayList<TableDataRow> filteredSubtotalTblRowsList = new ArrayList<TableDataRow>();
    public ArrayList<TableSubtotalRow> filteredSubtotalRowsList = new ArrayList<TableSubtotalRow>();
    public ArrayList<TableDataRow> filteredSubtotalRowsListForGrandTotal = new ArrayList<TableDataRow>();
    public HashMap<String, BigDecimal> otherval = new HashMap<String, BigDecimal>(); //added by sruthi otherval for runningTotal on 14 Nov 14
    public HashMap<String, BigDecimal> SummarizedMeasureGTVals = new HashMap<String, BigDecimal>();
    public HashMap<String, BigDecimal> SummarizedMeasureGTAvgVals = new HashMap<String, BigDecimal>();

    public TableBuilder(DataFacade facade) {
        this.facade = facade;
        this.subTotalHolder = new SubTotalHolder[this.getViewByCount()];
        this.subTotalHelper = new SubTotalHelper(this.facade);
        this.computedSubTotLst = new ArrayList<SubTotalHolder>();
        this.cellSpan = new HashSet<TableCellSpan>();
    }

    public void setFromAndToRow(int fromRow, int toRow) {
        this.fromRow = fromRow;
        this.toRow = toRow;
    }

    public int getFromRow() {
        return this.fromRow;
    }

    public int getToRow() {
        return this.toRow;
    }

    public int getViewByCount() {
        return facade.getViewByCount();
    }

    public abstract int getColumnCount();

    public abstract int getFromColumn();

    public int getRowCount() {
        return facade.getRowCount();
    }

    public int getTotalRowCount() {
        return this.totalRows;
    }

    public void setTotalRowCount(int totalRowCount) {
        this.totalRows = totalRowCount;
    }
    //start of code by Nazneen for sub total deviation

    public String getSubTotalkey() {
        return this.subTotalKey;
    }

    public void setSubTotalkey(String subTotalKey) {
        this.subTotalKey = subTotalKey;
    }
    //end of code by Nazneen for sub total deviation

    protected void reOrderSubTotalList() {
        Collections.sort(this.computedSubTotLst);
    }

    protected TableSubtotalRow createGrandTotalRowForSubTotalType(String stType) {
        TableSubtotalRow subTotalRow = new TableSubtotalRow();
        ArrayList<Object> rowData = new ArrayList<Object>();
        ArrayList<Boolean> displayStyle = new ArrayList<Boolean>();
        ArrayList<String> cssClassSubTotal = new ArrayList<String>();
        ArrayList<BigDecimal> measData = new ArrayList<BigDecimal>();
        ArrayList<String> textAligns = new ArrayList<String>();
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
        HashMap<String, String> finalCrossTabReportDrillMap = ((PbReturnObject) facade.container.getRetObj()).finalCrossTabReportDrillMap;
        ArrayList<String> reportDrillList = new ArrayList<String>();
        for (int i = 0; i < getViewByCount(); i++) {
            if (i == 0) {
                if (stType.contains("customTotal")) {
                    rowData.add(facade.getcustomTotalName());
                    stType = stType.replace("customTotal", "");
//                }else if(!facade.getoriginalTotalName().equalsIgnoreCase("none") && stType.equalsIgnoreCase(facade.getoriginalTotalName())){
//                  rowData.add(facade.getRenamedTotalName());
//                }else
                } else if (facade.getRenameDetails() != null && facade.getRenameDetails().get(stType) != null) {

                    rowData.add(facade.getRenameDetails().get(stType));

                } else {
                    rowData.add(this.getDisplayNameForSubtotalType(stType));
                }
            } else {
                rowData.add("");
            }
            reportDrillList.add("#");
//            displayStyle.add(Boolean.TRUE);
            displayStyle.add(facade.isColumnVisible(facade.getColumnId(i)));
        }


//             STzerorepMap=facade.container.getSTcrosscolmap();
//          if( STzerorepMap.containsKey(elemid)){
//             STzerocolumnrowcount=STzerorepMap.get(elemid);
//          } else{
//              STzerocolumnrowcount=0;
//          }
        //modified by anitha for grand total in transpose table
        int columnSize = getColumnCount();
        if (facade.isTransposed()) {
            columnSize = facade.container.getDisplayColumns().size();
        }

        for (int i = getViewByCount(); i < columnSize; i++) {
            //added by shravani for include & exclude
            zerorepcalcMap = facade.container.getReportCollect().crosscolmap1;
            BigDecimal subTotalValue = null;
            String stelemid = facade.getColumnId(i);
            String steleleid = crosstabMeasureId.get(facade.getColumnId(i));
            STzerorepMap = facade.container.getSTcrosscolmap();
            if (STzerorepMap.containsKey(stelemid)) {
                STzerocolumnrowcount = STzerorepMap.get(stelemid);
//                STzerocolumnrowcount = STzerocolumnrowcount/2;
            } else {
                STzerocolumnrowcount = 0;
            }
            repcalcST = zerorepcalcMap.keySet();
            Iterator itcalcst = repcalcST.iterator();
            while (itcalcst.hasNext()) {
                String key1 = itcalcst.next().toString();
                if (key1.equalsIgnoreCase(stelemid)) {
//             if(key1.equalsIgnoreCase(steleleid))
                    repcalcvalue = zerorepcalcMap.get(stelemid).toString();
                    break;
//        if(repcalcvalue.equalsIgnoreCase("null") || repcalcvalue.isEmpty() ||repcalcvalue.equalsIgnoreCase("")
                } else if (key1.equalsIgnoreCase(steleleid)) {

                    repcalcvalue = zerorepcalcMap.get(key1).toString();
                    break;
                } else {
                    repcalcvalue = "";
                }
            }
            //end of code by shravani
            if (stType.equals("LR")) {
                TableDataRow Value = this.getRowData(toRow - 1);
                String data = Value.rowData.get(i).toString();
                rowData.add(data);
            } else {
                if (facade.container.getDataTypes().get(i) != null && facade.container.getDataTypes().get(i).toString().equalsIgnoreCase("N")) {
                    subTotalValue = this.getSubTotalValue(this.subTotalHolder[getViewByCount() - 1], i, stType);
//          start of code By Nazneen for AVG measures GT and ST
                    String summarisedType = "SUMMARISED";
                    summarisedType = facade.container.summarizedHashmap.get(facade.getColumnId(i));
                    if (summarisedType != null) {
                        if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                            summarisedType = "NORMAL_AVG";
                        } else {
                            summarisedType = "SUMMARISED";
                        }
                    } else {
                        summarisedType = "SUMMARISED";
                    }
                    //commented By Nazneen
//            if(!facade.getAggregationForSubtotal(facade.getColumnId(i)).equalsIgnoreCase("") && summarisedType!=null && !summarisedType.equalsIgnoreCase("SUMMARISED"))
//             {
//                 if(facade.getAggregationForSubtotal(facade.getColumnId(i)).equalsIgnoreCase("avg")&&facade.getRowCount()!=0)
//                 subTotalValue=subTotalValue.divide(new BigDecimal(facade.getRowCount()),RoundingMode.HALF_UP);
//             }
                    if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                        if (stType.equals("ST") || stType.equals("CATST")) {
                            if (this.subTotalHolder[getViewByCount() - 1].rowCount != 0) {
                                ST = (this.subTotalHolder[getViewByCount() - 1].rowCount);
                            }
//                     if((this.subTotalHolder[getViewByCount() - 1].rowCount!=0)== STzerocolumnrowcount)
                            //added by shravani for include & exclude
                            if (!facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Exclude 0")) {
                                if ((this.subTotalHolder[getViewByCount() - 1].rowCount != 0) && ST != STzerocolumnrowcount) {
                                    subTotalValue = subTotalValue.divide(new BigDecimal((this.subTotalHolder[getViewByCount() - 1].rowCount) - STzerocolumnrowcount), RoundingMode.HALF_UP);
                                }
                            } else if (!facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Include 0")) {
                                subTotalValue = subTotalValue.divide(new BigDecimal(this.subTotalHolder[getViewByCount() - 1].rowCount), RoundingMode.HALF_UP);
                            } else if (facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Exclude 0")) {
                                if ((this.subTotalHolder[getViewByCount() - 1].rowCount != 0) && ST != STzerocolumnrowcount) {


                                    subTotalValue = subTotalValue.divide(new BigDecimal((this.subTotalHolder[getViewByCount() - 1].rowCount) - STzerocolumnrowcount), RoundingMode.HALF_UP);
                                }
                            } else if (facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Include 0")) {
                                subTotalValue = subTotalValue.divide(new BigDecimal(this.subTotalHolder[getViewByCount() - 1].rowCount), RoundingMode.HALF_UP);
                            } else {
                                subTotalValue = subTotalValue.divide(new BigDecimal(this.subTotalHolder[getViewByCount() - 1].rowCount), RoundingMode.HALF_UP);
                            }
                            //end of code for include&exclude 0
                        } else {
                            //modified on 24Jan14
//                    if(facade.getRowCount()!=0){
//                        subTotalValue=subTotalValue.divide(new BigDecimal(facade.getRowCount()),RoundingMode.HALF_UP);
//                    }
                            int rowCnt = 0;
                            if (facade.container.getIsSearchFilterApplied()) {
                                if (facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")) {
                                    if (this.subTotalHolder[getViewByCount() - 1] != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(i)) != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                        rowCnt = facade.container.getGTRowCntBeforSearchFilter() - this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(i));
                                    } else {//modified by bhargavi
                                        rowCnt = facade.container.gettrwcnt();
                                    }
                                } else {
                                    rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                                }

                            } else {
                                // modified by Amar on 3/2/2014
                                //if(facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")){
                                if (repcalcvalue.equalsIgnoreCase("Exclude 0")) {
                                    if (this.subTotalHolder[getViewByCount() - 1] != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(i)) != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                        //rowCnt = facade.getRowCount() - this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(i));
                                        rowCnt = facade.getRowCount() - this.getZeroRowCount(facade.getRowCount(), i, stelemid);
                                    } else if (this.subTotalHolder[getViewByCount() - 1] != null && this.getZeroRowCount(facade.getRowCount(), i, stelemid) > 0) {
                                        rowCnt = facade.getRowCount() - this.getZeroRowCount(facade.getRowCount(), i, stelemid);
                                    } else {
//                                rowCnt = facade.getRowCount();
                                        //start of code by bhargavi on 1/05/14
                                        if ("1".equals(facade.container.stype) || "0".equals(facade.container.stype)) {
                                            rowCnt = facade.container.getTotalRowCount();
                                        } else {
                                            rowCnt = facade.getRowCount() - this.getZeroRowCount(facade.getRowCount(), i, stelemid);
                                            facade.container.setTotalRowCount(rowCnt);
                                        }//end of code by bhargavi
                                    }
                                } else {
                                    //modified by bhargavi on 1/05/14
                                    if ("1".equals(facade.container.stype) || "0".equals(facade.container.stype)) {
                                        rowCnt = facade.container.gettrwcnt();
                                    } else {
                                        rowCnt = facade.getRowCount();
                                        facade.container.settrwcnt(rowCnt);
                                    }
                                }

                                if (rowCnt == 0) {
                                    if (facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")) {
                                        if (this.subTotalHolder[getViewByCount() - 1] != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap != null && facade.getColumnId(i) != null) {
                                            if (this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount - this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(i));
                                            } else {
                                                if (this.subTotalHolder[getViewByCount() - 1] != null) {
                                                    rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
                                                }
                                            }
                                        } else {
                                            if (this.subTotalHolder[getViewByCount() - 1] != null) {
                                                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
                                            }
                                        }
                                    } else {
                                        if (this.subTotalHolder[getViewByCount() - 1] != null) {
                                            rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
                                        }
                                    }

                                }
                            }

                            if (rowCnt != 0) {
//                        if (!facade.container.isReportCrosstab() && facade.container.getAvgcalculationtype().equalsIgnoreCase("Exclude0")) {
//                            //subTotalValue=subTotalValue.divide(new BigDecimal(rowCnt-zerocolumnrowcount),RoundingMode.HALF_UP);
//                            subTotalValue = subTotalValue.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
//                        } else {
                                subTotalValue = subTotalValue.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
//                        }
                            }
                        }
                    }
            if ( stelemid.contains("_rank") || stelemid.contains("_Qtdrank") || stelemid.contains("_Ytdrank") || stelemid.contains("_PMtdrank") || stelemid.contains("_PQtdrank") || stelemid.contains("_PYtdrank") ) {

                subTotalValue=null;
            }
//          end of code By Nazneen for AVG measures GT and ST
                    HashMap NFMap = new HashMap();
                    String nbrSymbol = "";
                    String Symbol = "";
//
                    NFMap = (HashMap) facade.container.getTableHashMap().get("NFMap");
                    nbrSymbol = facade.container.symbol.get(facade.getColumnId(i));

                    if (NFMap != null) {
//             nbrSymbol = String.valueOf(NFMap.get(element));
                        if (facade.container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                            if (NFMap.get(crosstabMeasureId.get(facade.getColumnId(i))) != null) {
                                Symbol = String.valueOf(NFMap.get(crosstabMeasureId.get(facade.getColumnId(i))));
                            }
                        } else {
                            if (NFMap.get(facade.getColumnId(i)) != null) {
                                Symbol = String.valueOf(NFMap.get(facade.getColumnId(i)));
                            }
                        }

                    }
                    if (stType.equals("GT")) {
                        String formattedData = "";

                        //String nbrSymbol = facade.container.getNumberSymbol(facade.getColumnId(i));
                        int precision = facade.container.getRoundPrecisionForMeasure(facade.getColumnId(i));
                        if (subTotalValue == null) {
                            if (isCrossTab() && facade.getColumnId(i).contains("A_")) {
                                String eleId = crosstabMeasureId.get(facade.getColumnId(i));
                                facade.container.setsubGtVal(eleId, "");
                            } else {
                                facade.container.setsubGtVal(facade.getColumnId(i), "");
                            }
                        } else {
                            //start of code by Bhargavi for col gt as avg for sum measures
                            if (isCrossTab() && facade.getColumnId(i).contains("A_")) {
                                if (facade.container.getCTcolGtAggType(facade.getColumnId(i)).equalsIgnoreCase("AVG")) {
                                    int rowCnt = facade.getRowCount() - this.getZeroRowCount(facade.getRowCount(), i, stelemid);
                                    subTotalValue = subTotalValue.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                }
                            }
                            //end of code by Bhargavi for col gt as avg for sum measures
                            formattedData = NumberFormatter.getModifiedNumberFormat(subTotalValue, Symbol, precision);
                            if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("null")) {
                                if (nbrSymbol.equalsIgnoreCase("%")) {
                                    formattedData = formattedData + " " + nbrSymbol;
                                } else {
                                    formattedData = nbrSymbol + " " + formattedData;
                                }

                            }
                            if (Symbol != null && !Symbol.equalsIgnoreCase("Nf")) {
                                if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("null")) {
                                    if (nbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = formattedData;
                                    }
                                } else {
                                    formattedData = formattedData + " " + Symbol;
                                }
                            }
                            if (isCrossTab() && facade.getColumnId(i).contains("A_")) {
                                String eleId = crosstabMeasureId.get(facade.getColumnId(i));
                                facade.container.setsubGtVal(eleId, formattedData);
                            } else {
                                facade.container.setsubGtVal(facade.getColumnId(i), formattedData);
                            }
                        }
                    }
                    if (subTotalValue == null) {
                        rowData.add("");
                    } else if (facade.getDataTypes().get(i).equalsIgnoreCase("D")) {
                        rowData.add("");
                    } else {
                        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("null")) {
                            if (Symbol != null && !Symbol.equalsIgnoreCase("Nf")) {
                                if (nbrSymbol.equalsIgnoreCase("%")) {
                                    rowData.add(facade.formatMeasureData(subTotalValue, i) + " " + nbrSymbol);
                                } else {
                                    rowData.add(nbrSymbol + "" + facade.formatMeasureData(subTotalValue, i));
                                }
                                // rowData.add(nbrSymbol + "" + facade.formatMeasureData(subTotalValue, i) + " " + Symbol);
                            } else {
                                if (nbrSymbol.equalsIgnoreCase("%")) {
                                    rowData.add(facade.formatMeasureData(subTotalValue, i) + " " + nbrSymbol);
                                } else {
                                    rowData.add(nbrSymbol + "" + facade.formatMeasureData(subTotalValue, i));
                                }
                            }
                        } else {
//                    if(Symbol!=null && !Symbol.equalsIgnoreCase("Nf"))
//                    rowData.add(facade.formatMeasureData(subTotalValue, i) + " " + Symbol);
//                    else
                            rowData.add(facade.formatMeasureData(subTotalValue, i));
                        }
                    }
                } else {
                    if (isCrossTab() && facade.getColumnId(i).contains("A_")) {
                        String eleId = crosstabMeasureId.get(facade.getColumnId(i));
                        facade.container.setsubGtVal(eleId, "");
                    } else {
                        facade.container.setsubGtVal(facade.getColumnId(i), "");
                    }
                    rowData.add("");
                }
            }
            String key = "";
            if (stType.equals("ST") || stType.equals("CATST")) {
                key = "ST_" + facade.getColumnId(i);
            } else {
                key = "GT_" + facade.getColumnId(i);
            }
            SummarizedMeasureGTVals.put(key, subTotalValue);
            String finalrepDrillUrl = facade.getContextPath() + "/reportViewer.do?reportBy=viewReport";

            displayStyle.add(facade.isColumnVisible(facade.getColumnId(i)));
           // if (facade.container.getReportDrillMap(facade.getDisplayColumns().get(i)) != null && !facade.container.getReportDrillMap(facade.getDisplayColumns().get(i)).isEmpty() && !facade.container.getReportDrillMap(facade.getDisplayColumns().get(i)).toString().equalsIgnoreCase("0")) {
                if (facade.container.isReportCrosstab()) {
                    if (finalCrossTabReportDrillMap.containsKey(facade.getDisplayColumns().get(i))) {
                     if (!facade.container.getReportCollect().reportDrillMap.containsKey(facade.getDisplayColumns().get(i))) {
                    reportDrillList.add("#");
                } else if (facade.container.getReportDrillMap(facade.getDisplayColumns().get(i)) != null && facade.container.getReportDrillMap(facade.getDisplayColumns().get(i)).equalsIgnoreCase("0")) {
                    reportDrillList.add("#");
                } else {
                    finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(facade.getDisplayColumns().get(i));
                     reportDrillList.add(finalrepDrillUrl);
                    }
                    }
                } else {
                    if (!facade.container.getReportCollect().reportDrillMap.containsKey(facade.getDisplayColumns().get(i))) {
                    reportDrillList.add("#");
                } else if (facade.container.getReportDrillMap(facade.getDisplayColumns().get(i)) != null && facade.container.getReportDrillMap(facade.getDisplayColumns().get(i)).equalsIgnoreCase("0")) {
                    reportDrillList.add("#");
                } else {
                    finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(facade.getDisplayColumns().get(i));
                     reportDrillList.add(finalrepDrillUrl);
                }
                }
          //  } else {
            //    reportDrillList.add("#");
          //  }

        }
        for (int i = 0; i < columnSize; i++) {
            if (i < getViewByCount()) {
                cssClassSubTotal.add("subTotalCell");
            } else {
                cssClassSubTotal.add("tabFooter");
            }

        }
        for (int i = 0; i < facade.getDisplayColumns().size(); i++) {
            textAligns.add(facade.getTextAlign(0, facade.getDisplayColumns().get(i)));
        }

        subTotalRow.setRowData(rowData);
        subTotalRow.setSubtotalType(stType);
        subTotalRow.setRowDataIds(facade.getDisplayColumns());
        subTotalRow.setDisplayStyle(displayStyle);
        subTotalRow.setCssClass(cssClassSubTotal);
        subTotalRow.setTextAlign(textAligns);

        //added by Govardhan for updating Grand total after subtotal filter applied

        if (stType.equalsIgnoreCase("CATST") && (getViewByCount() == 2 || getViewByCount() == 3) && getIsSubToTalSearchFilterApplied()) {
            subTotalRow = this.subTotalHelper.UpdatePageTotal(subTotalRow, filteredSubtotalRowsList, this.TableDataRowListSubFiltered);
        }
        //end of code by Govardhan
        subTotalRow.setReportDrillList(reportDrillList);
        DisplayParameters dispGrand = new DisplayParameters();
        ArrayList<String> MeasureVals = new ArrayList();
        ArrayList measureIdsList = facade.container.getTableDisplayMeasures();
        ArrayList<String> MeasureNames = facade.container.getReportMeasureNames();
        //added by Nazneen on 20/11/14 for not showing GT in Display GT Region for hidden measures
        ArrayList hideMsrs = facade.container.getReportCollect().getHideMeasures();
        if (hideMsrs != null) {
            if (!hideMsrs.isEmpty()) {
                String measureList = "";
                String hideMeasList = "";
                for (int j = 0; j < measureIdsList.size(); j++) {
                    for (int i = 0; i < hideMsrs.size(); i++) {
                        measureList = measureIdsList.get(j).toString().replace("A_", "");
                        hideMeasList = hideMsrs.get(i).toString().replace("A_", "");
                        if (measureList.equalsIgnoreCase(hideMeasList)) {
                            measureIdsList.remove(j);
                            MeasureNames.remove(j);
                            break;
                        }
                    }
                }
            }
        }
        //ended by Nazneen on 20/11/14 for not showing GT in Display GT Region for hidden measures

        for (int i = 0; i < measureIdsList.size(); i++) {
            MeasureVals.add((String.valueOf(facade.container.getsubGtVal((String) measureIdsList.get(i)))));
        }

        facade.container.setGrandTotalSectionDisplay(dispGrand.getGrandTotalRegion(MeasureNames, measureIdsList, MeasureVals));

        return subTotalRow;
    }

    protected TableSubtotalRow createSubTotalRowsForSubTotalType(String stType, int subIndex) {
        ArrayList<Object> rowData;
        ArrayList<Boolean> displayStyle;
        ArrayList<BigDecimal> measData;
        TableSubtotalRow subTotalRow;
        int viewByColumn;
        SubTotalHolder subHolder = null;
        ArrayList<String> cssClassSubTotal = new ArrayList<String>();

        subHolder = this.computedSubTotLst.get(subIndex);
        rowData = new ArrayList<Object>();
        displayStyle = new ArrayList<Boolean>();
        measData = new ArrayList<BigDecimal>();

        viewByColumn = subHolder.getViewColumnNumber();
        for (int i = 0; i < viewByColumn; i++) {
            rowData.add("");
            displayStyle.add(Boolean.FALSE);
            measData.add(BigDecimal.ZERO);
        }
        for (int i = viewByColumn; i < getViewByCount(); i++) {
            if (i == viewByColumn) {
                rowData.add(subHolder.getDimensionValue());
//                displayStyle.add(Boolean.TRUE);
                displayStyle.add(facade.isColumnVisible(facade.getColumnId(i)));
            } else if (i == viewByColumn + 1) {
                rowData.add(this.getDisplayNameForSubtotalType(stType));
//                displayStyle.add(Boolean.TRUE);
                displayStyle.add(facade.isColumnVisible(facade.getColumnId(i)));
            } else {
                rowData.add("");
                displayStyle.add(facade.isColumnVisible(facade.getColumnId(i)));
            }
            measData.add(BigDecimal.ZERO);

        }
        //start of code by Nazneen for sub total deviation
        String subTotalVals = "";
        String subTotalkey = "";
        String subTotalkeyNew = "";
//        int index = subHolder.firstCellId.lastIndexOf("_");
//        String cellId = subHolder.firstCellId.substring(index);

//        modified by Nazneen on 9 Jan 2014
//        if(getViewByCount()==2) {
//            subTotalkey = subHolder.viewColNum+"~"+subHolder.getDimensionValue();
//         }
//        if(getViewByCount()>2){
        if (getViewByCount() > 1) {
            String subTotalKey = "";
            String subTotalKeyNew = "";
            if (getSubTotalkey() != null && !getSubTotalkey().equalsIgnoreCase("")) {
                subTotalKey = getSubTotalkey().substring(1);
                subTotalKeyNew = getSubTotalkey().substring(1);
            }
//            modified by Nazneen on 9 Jan 2014
//            subTotalkey = subHolder.viewColNum+"~"+subTotalKey+"~"+subHolder.getDimensionValue();
            if (subHolder.viewColNum == 1) {
                cntOfSt++;
            }
            subTotalkey = subHolder.viewColNum + "~" + subTotalKey;
        }
        //end of code by Nazneen for sub total deviation
        for (int i = getViewByCount(); i < getColumnCount(); i++) {
            BigDecimal subTotalValue = this.getSubTotalValue(subHolder, i, stType);
//             start of code By Nazneen for AVG measures GT and ST
            String summarisedType = "SUMMARISED";
            summarisedType = facade.container.summarizedHashmap.get(facade.getColumnId(i));
            if (summarisedType != null) {
                if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                    summarisedType = "NORMAL_AVG";
                } else {
                    summarisedType = "SUMMARISED";
                }
            } else {
                summarisedType = "SUMMARISED";
            }
            if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                //modified on 15 jan 2014 by nazneen
//               if(stType.equals("ST") || stType.equals("CATST")){
//                if(subHolder.rowCount!=0)
//                    subTotalValue=subTotalValue.divide(new BigDecimal(subHolder.rowCount),RoundingMode.HALF_UP);
//               }
//             else {
//                if(facade.getRowCount()!=0)
//                    subTotalValue=subTotalValue.divide(new BigDecimal(facade.getRowCount()),RoundingMode.HALF_UP);
//             }

                if (getViewByCount() > 1) {
                    int viewByCnt = getViewByCount();
                    int colNum = subHolder.viewColNum;
                    int finalVal = viewByCnt - (colNum + 2);
                    //modified on 24Jan14
                    int rowCnt = 0;
                    if (stType.equals("ST") || stType.equals("CATST")) {
                        if (facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")) {
                            if (subHolder.zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                rowCnt = subHolder.rowCount - subHolder.zeroRowCountMap.get(facade.getColumnId(i));
                            } else {
                                rowCnt = subHolder.rowCount;
                            }
                        } else {
                            rowCnt = subHolder.rowCount;
                        }

                    } else {
                        if (facade.container.getIsSearchFilterApplied()) {
                            if (facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")) {
                                if (subHolder.zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                    rowCnt = facade.container.getGTRowCntBeforSearchFilter() - subHolder.zeroRowCountMap.get(facade.getColumnId(i));
                                } else {
                                    rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                                }
                            } else {
                                rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                            }

                        } else {
                            if (facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")) {
                                if (subHolder.zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                    rowCnt = facade.getRowCount() - subHolder.zeroRowCountMap.get(facade.getColumnId(i));
                                } else {
                                    rowCnt = facade.getRowCount();
                                }
                            } else {
                                rowCnt = facade.getRowCount();
                            }

                        }
                    }
//                  //modified by Bhargavi
//                    rowCnt = rowCnt - finalVal;
//                    if (rowCnt != 0) {
//                        subTotalValue = subTotalValue.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
//                    }
                    if (subHolder.viewColNum == 1) {
                        rowCnt = rowCnt - finalVal;
                    } else {
                        rowCnt = rowCnt - cntOfSt;
                    }
                    if (rowCnt != 0) {
                        subTotalValue = subTotalValue.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                        if (subHolder.viewColNum == 0) {
                            cntOfSt = 0;
                        }
                    }
                } else {
                    if (stType.equals("ST") || stType.equals("CATST")) {
                        if (subHolder.rowCount != 0) {
                            if (facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")) {
                                if (subHolder.zeroRowCountMap != null) {
                                    if (subHolder.zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                        subTotalValue = subTotalValue.divide(new BigDecimal(subHolder.rowCount - subHolder.zeroRowCountMap.get(facade.getColumnId(i))), RoundingMode.HALF_UP);
                                    } else {
                                        subTotalValue = subTotalValue.divide(new BigDecimal(subHolder.rowCount), RoundingMode.HALF_UP);
                                    }
                                } else {
                                    subTotalValue = subTotalValue.divide(new BigDecimal(subHolder.rowCount), RoundingMode.HALF_UP);
                                }
                            } else {
                                subTotalValue = subTotalValue.divide(new BigDecimal(subHolder.rowCount), RoundingMode.HALF_UP);
                            }

                        }
                    } else {
                        int rowCnt = 0;
                        if (facade.container.getIsSearchFilterApplied()) {
                            if (facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")) {
                                if (subHolder.zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                    rowCnt = facade.container.getGTRowCntBeforSearchFilter() - subHolder.zeroRowCountMap.get(facade.getColumnId(i));
                                } else {
                                    rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                                }
                            } else {
                                rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                            }

                        } else {
                            if (facade.container.getaveragecalculationtype(facade.getColumnId(i)).equalsIgnoreCase("Exclude 0")) {
                                if (subHolder.zeroRowCountMap.get(facade.getColumnId(i)) != null) {
                                    rowCnt = facade.getRowCount() - subHolder.zeroRowCountMap.get(facade.getColumnId(i));
                                } else {
                                    rowCnt = facade.getRowCount();
                                }
                            } else {
                                rowCnt = facade.getRowCount();
                            }
                        }
                        if (rowCnt != 0) {
                            subTotalValue = subTotalValue.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                        }
                    }
                }
            }
            String key = "";
            if (stType.equals("ST") || stType.equals("CATST")) {
                key = "ST_" + facade.getColumnId(i);
            } else {
                key = "GT_" + facade.getColumnId(i);
            }
            SummarizedMeasureGTVals.put(key, subTotalValue);
            //end of code By Nazneen for AVG measures GT and ST
            //added by sruthi for symbol in subtotal
            String nbrSymbol = facade.container.symbol.get(facade.getColumnId(i));
            String cellData = facade.formatMeasureData(subTotalValue, i);
            if (nbrSymbol != null) {
                if (nbrSymbol.equalsIgnoreCase("%")) {
                    cellData = cellData + "" + nbrSymbol;
                } else {
                    cellData = nbrSymbol + "" + cellData;
                }
            } //ended by sruthi
            rowData.add(cellData);
            //    rowData.add(facade.formatMeasureData(subTotalValue, i));
            measData.add(subTotalValue);
            displayStyle.add(facade.isColumnVisible(facade.getColumnId(i)));
            //added by Nazneen for sub total deviation
            if (getViewByCount() > 1) {
                subTotalVals = subTotalVals + "~" + facade.formatMeasureData(subTotalValue, i);
            }
        }
        //added by Nazneen for sub total deviation
        if (getViewByCount() > 1) {
            for (int i = 0; i < subTotalKeyArray.size(); i++) {
                if (subTotalKeyArray.get(i) != null && !subTotalKeyArray.get(i).equalsIgnoreCase("")) {
                    subTotalkey = subTotalKeyArray.get(i).substring(1);
                    subTotalkeyNew = subTotalKeyArray.get(i).substring(1);
                }
                subTotalkey = subHolder.viewColNum + "~" + subTotalkey;

                int endIndex = subTotalkeyNew.lastIndexOf("~");
                if (endIndex != -1) {
                    subTotalkeyNew = subTotalkeyNew.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                    subTotalkeyNew = subTotalkeyNew + "~subtotal";
                }




                if (!subTotalDeviationMap.containsKey(subTotalkey)) {
                    subTotalDeviationMap.put(subTotalkey, subTotalVals);
                }
                if (!subTotalHolderMap.containsKey(subTotalkeyNew)) {
                    subTotalHolderMap.put(subTotalkeyNew, subTotalVals);
                }
            }
        }
        subTotalKeyArray.clear();

        for (int i = 0; i < getColumnCount(); i++) {
            if (i < getViewByCount()) {
                cssClassSubTotal.add("subTotalCell");
            } else {
                cssClassSubTotal.add("tabFooter");
            }
        }
        subTotalRow = new TableSubtotalRow();
        subTotalRow.setRowData(rowData);
        subTotalRow.setSubtotalType(stType);
        //
        subTotalRow.setRowDataIds(facade.getDisplayColumns());
        subTotalRow.setDisplayStyle(displayStyle);
        subTotalRow.setCssClass(cssClassSubTotal);
        subTotalRow.setRawNumericMeasData(measData);
        if (facade.isReportCrosstab() && facade.isDynamicRowsDisplayedInCrosstab()) {
            if (stType.equals("ST")) {
                CtDynamicRowTableHelper dynamicRowHelper = new CtDynamicRowTableHelper(facade);
                subTotalRow = dynamicRowHelper.createDynamicSubtotalRow(subTotalRow);
            }
        }
        return subTotalRow;
    }

    protected int getZeroRowCount(int rowCount, int col, String colId) {
        int zeroRowCount = 0;
        TableDataRow tableRow;
        for (int i = 0; i < rowCount; i++) {
            //tableRow = (TableDataRow)this.getRowData(i);
            BigDecimal value = facade.getMeasureDataForComputation(i, colId);
            //  BigDecimal value=(BigDecimal)tableRow.getRowData(col);
            BigDecimal bd = new BigDecimal("0");
            if (value.compareTo(bd) == 0) {
                zeroRowCount = zeroRowCount + 1;
            }
        }
        return zeroRowCount;
    }

    protected String getDisplayNameForSubtotalType(String stType) {
        if (stType.equals("ST")) {
            return "Sub Total";
        } else if (stType.equals("CATST")) {
            return "Sub Total";
        } else if (stType.equals("MED")) {
            return "Median";
        } else if (stType.equals("GT")) {
            return "Grand Total";
        } else if (stType.equals("GTMED")) {
            return "Overall Median";
        } else if (stType.equals("GTMEA")) {
            return "Overall Mean";
        } else if (stType.equals("GTSTD")) {
            return "Overall Standard Deviation";
        } else if (stType.equals("MEA")) {
            return "Mean";
        } else if (stType.equals("STDDEV")) {
            return "Standard Deviation";
        } else if (stType.equals("OVEMAX")) {
            return "Overall Max";
        } else if (stType.equals("OVEMIN")) {
            return "Overall Min";
        } else if (stType.equals("AVG")) {
            return "Overall Average";
        } else if (stType.equals("CATMAX")) {
            return "Category Max";
        } else if (stType.equals("CATMIN")) {
            return "Category Min";
        } else if (stType.equals("VAR")) {
            return "Variance";
        } else if (stType.equals("GTVAR")) {
            return "Overall Variance";
        } else if (stType.equals("ROWCNT")) {
            return "Row Count";
        } else if (stType.equals("CATAVG")) {
            return "Category Average";
        } else if (stType.equals("TOPOTHERS")) {
            return "Others";
        } else {
            return "";
        }
    }

    public BigDecimal getSubTotalValue(SubTotalHolder stHolder, int column, String stType) {
        //commented by Nazneen
//        if ( (stType.equals("ST") || stType.equals("CATST"))&& facade.isColumnPropertyVisible(facade.getColumnId(column),stType) )
//            return stHolder.getSubTotalValue(facade.getColumnId(column));
//               start of code by Nazneen for calculating SUB TOTAL ,if the measure is of Type Avg or of type 4 or summarized
        facade.container.summarizedHashmap.clear();
        if (facade.isAnySubtotallingRequired() || facade.isCustomTotalRequired()) {
            if ((stType.equals("ST") || stType.equals("CATST")) && facade.isColumnPropertyVisible(facade.getColumnId(column), stType)) {
                return getSubTotalValueForSummMeasures(stHolder, column, stType);
            }
        }
//        end of code by Nazneen for calculating SUB TOTAL ,if the measure is of Type Avg or of type 4 or summarized
        if (stType.equals("MED") || stType.equals("GTMED")) {
            if (facade.isMedianRequired(facade.getColumnId(column))) {
                return stHolder.getMedianValue(facade.getColumnId(column));
            } else {
                return null;
            }
        }
        if (stType.equals("MEA") || stType.equals("GTMEA")) {
            if (facade.isMeanRequired(facade.getColumnId(column))) {
                return stHolder.getMeanValue(facade.getColumnId(column));
            } else {
                return null;
            }
        }
        if (stType.equals("STDDEV") || stType.equals("GTSTD")) {
            if (facade.isStdDeviationRequired(facade.getColumnId(column))) {
                return stHolder.getStdDevValue(facade.getColumnId(column));
            } else {
                return null;
            }
        }
        if (stType.equals("VAR") || stType.equals("GTVAR")) {
            if (facade.isVarianceRequired(facade.getColumnId(column))) {
                return stHolder.getVarianceValue(facade.getColumnId(column));
            } else {
                return null;
            }
        }
        //Calculate GT
        //commented by Nazneen
//        if (stType.equals("GT")&& facade.isColumnPropertyVisible(facade.getColumnId(column),stType)) {
//            if(facade.container.getKpiQrycls()!=null&& !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_","").trim())){
//                //
//                if(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column))==null)
//                    return BigDecimal.ZERO;
//                else if(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase(""))
//                    return BigDecimal.ZERO;
//                else
//                return  new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//
//        }
//            else if(facade.container.isReportCrosstab()){
//            if(facade.container.getKpiQrycls()!=null&& !facade.container.getKpiQrycls().isEmpty()){
//               HashMap <String,ArrayList> nonViewByMapNew =null;
//               nonViewByMapNew=((PbReturnObject)facade.container.getRetObj()).nonViewByMapNew;
//               if(nonViewByMapNew!=null && !nonViewByMapNew.isEmpty()){
//                   String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[","").replace("]", "").trim();
//                   if(facade.container.retObjHashmap.get(keyset)!=null)
//                   return new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
//                   else
//                       return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//               }
//            }else{
//                return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//            }
//        }
//            else{
//            return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//             }
//            }
//          start of code by Nazneen for calculating GT ,if the measure is of Type Avg or of type 4 or summarized
        if (stType.equals("GT") && facade.isColumnPropertyVisible(facade.getColumnId(column), stType)) {
            return getGTValueForSummMeasures(stHolder, column, stType);
        } //         end of code by Nazneen for calculating GT ,if the measure is of Type Avg or of type 4 or summarized
        else if (stType.equals("GT") && !facade.isGrandTotalRequired()) {
            if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())) {
                //
                if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
                    return BigDecimal.ZERO;
                } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
                    return BigDecimal.ZERO;
                } else {
                    return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                }

            } else {
                return facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
        }

        if (stType.equals("AVG")) {
            return facade.getColumnAverageValue(facade.getColumnId(column));
        }
        if (stType.equals("OVEMAX")) {
            return facade.getColumnMaximumValue(facade.getColumnId(column));

        }
        if (stType.equals("OVEMIN")) {
            return facade.getColumnMinimumValue(facade.getColumnId(column));
        }

        if (stType.equals("CATMAX")) {
            if (facade.isCategoryMaxRequired()) {
                return stHolder.getDataForCategoryMax(facade.getColumnId(column));
            } else if (facade.isCustomTotalRequired() && facade.getmappedTo().equalsIgnoreCase("CATMAX")) {
                return stHolder.getDataForCategoryMax(facade.getColumnId(column));
            } else {
                return null;
            }
        }
        if (stType.equals("CATMIN")) {
            if (facade.isCategoryMinRequired()) {
                return stHolder.getDataForCategoryMin(facade.getColumnId(column));
            } else if (facade.isCustomTotalRequired() && facade.getmappedTo().equalsIgnoreCase("CATMIN")) {
                return stHolder.getDataForCategoryMin(facade.getColumnId(column));
            } else {
                return null;
            }
        }
        if (stType.equals("ROWCNT")) {
            if (facade.isRowCountRequired()) {
                return new BigDecimal(stHolder.getRowCount());
            } else {
                return null;
            }
        }
        if (stType.equals("CATAVG")) {
            if (facade.isCatAvgRequired()) {
                return stHolder.getDataForCategoryAvg(facade.getColumnId(column));
            } else if (facade.isCustomTotalRequired() && facade.getmappedTo().equalsIgnoreCase("CATAVG")) {
                return stHolder.getDataForCategoryAvg(facade.getColumnId(column));
            } else {
                return null;
            }
        }
        if (stType.equals("TOPOTHERS")) {
            BigDecimal gtVal = facade.getColumnGrandTotalValue(facade.getColumnId(column));
            BigDecimal subTotalVal = stHolder.getSubTotalValue(facade.getColumnId(column));
            //added by sruthi othervalue for running total on 14 Nov 14
            String rtid = facade.getColumnId(column);
            otherval.put(facade.getColumnId(column), gtVal.subtract(subTotalVal));
            facade.container.setOtherValue(otherval);
            boolean rtval = rtid.contains("_rt");
            if (rtval) {
                String splitrtid = rtid.replace("_rt", "");
                HashMap<String, BigDecimal> normalid = facade.container.getOtherValue();
                BigDecimal normalother = normalid.get(splitrtid);
                BigDecimal rtVal = StatUtil.STAT_HELPER.RunningTopTotal(facade.container.getRunningTotal(), normalother);
                return rtVal;
            } else //ended by sruthi on 14 Nov 14
            {
                return gtVal.subtract(subTotalVal);
            }
        }

        return null;
    }

    public Set<TableCellSpan> getCellSpans() {
        return this.cellSpan;
    }

    public void addRowMapping(int displayRow, int actualRow) {
        facade.addRowMapping(displayRow, actualRow);
    }

    public void removeCellSpanFromSet(TableCellSpan cellSpan) {
        this.cellSpan.remove(cellSpan);
    }

    public boolean isRTExcelColsAvailable() {
        return facade.isRTExcelColsAvailable();
    }

    public boolean isTargetEntryApplicable() {
        return facade.isTargetEntryApplicable();
    }

    public abstract TableHeaderRow[] getHeaderRowData();

    public abstract TableMenuRow getMenuRowData();

    public abstract TableSearchRow getSearchData();

    public abstract TableDataRow getRowData(int rowNum);

    public abstract TableDataRow getRowData1(int rowNum);

    public abstract TableSubtotalRow[] getSubtotalRowData();

    public abstract TableSubtotalRow[] getGrandtotalRowData();

    public abstract TableSubtotalRow[] getSubtotalRowDataLastRow();

    public String getDisplayFormula(String formula) {
        return facade.getDisplayFormula(formula);
    }

    public void setSubTotalRows(String measId, Integer subTotalRow) {
        this.facade.setSubTotRow(measId, subTotalRow);
    }

    public boolean isDrillAcrossSupported() {
        return facade.isDrillAcrossSupported();
    }

    public boolean isTransposed() {
        return facade.isTransposed();
    }

    public String getNumberSymbol(String elementId) {
        return facade.getNumberSymbol(elementId);
    }

    public String getModifiedNumberFormatSymbol(String nbrSymbol) {
        return facade.getModifiedNumberFormatSymbol(nbrSymbol);
    }

    public HashMap<String, ArrayList<String>> getTransposeFormatMap() {
        return facade.getTransposeFormatMap();
    }

    public String getmeasureAlign(String element) {
        String align = "center";
        align = facade.getmesureAlign(element);
        return align;
    }

    public boolean getDrillMeasure() {
        return facade.getDrillMeasure();
    }

    public String getReportId() {
        return facade.getReportId();
    }
//added by sruthi for header color
    public String getmeasureColor(String element) {
        String color = "";
        color = facade.getmesureColor(element);
        return color;
    }
//ended by sruthi
   //added by sruthi for background color in tablecolumn pro 
      public String getmeasurebgColor(String element) {
        String bgcolor = "";
        bgcolor = facade.getmesurebgColor(element);
        return bgcolor;
    }//ended by sruthi
    public String gettextColor(String element) {
        String color = "";
        color = facade.getTextScriptColor(element);
        return color;
    }

    public String getContextPath() {
        return facade.getContextPath();
    }

    public String getMeasureElemId(int colNum) {
        return facade.getMeasureId(colNum);
    }

    public String getDimension(int colNum) {
        int actualRow = facade.getActualRow(colNum);
        String elementId = facade.getDisplayColumns().get(0);
        String dimensionName = facade.getDimensionData(actualRow, elementId);
        return dimensionName;
    }

    public String getAdhocDrillType() {
        return facade.getAdhocDrillType();
    }

    public boolean isTreeTableDisplay() {
        return facade.isTreeTableDisplay();
    }

    public boolean isParameterDrill() {
        return facade.isParameterDrill();
    }

    public boolean isCrossTab() {
        return facade.isReportCrosstab();
    }

    public boolean isSerialNumDisplay() {
        return facade.isSerialNumDisplay();
    }

    public String getMeasureDrillType() {
        return facade.getMeasureDrillType();
    }

    public int getSubTotalTopBottomCount() {
        return facade.getSubTotalTopBottomCount();
    }

    public String getGetFromOneview() {
        return getFromOneview;
    }

    public void setGetFromOneview(String getFromOneview) {
        this.getFromOneview = getFromOneview;
    }

    public String getMsrDrillReportSelection() {
        return facade.container.getReportCollect().msrDrillReportSelection;
    }

    public String getReportDrillMap(String elementId) {
        return facade.container.getReportDrillMap(elementId);
    }

    public int getInceresedColcount() {
        return facade.getIncreaseColCount();
    }

    public boolean isSplitby() {
        return facade.container.isSplitBy();
    }

    public String getHtmlCellHeight() {
        return htmlCellHeight;
    }

    public void setHtmlCellHeight(String htmlCellHeight) {
        this.htmlCellHeight = htmlCellHeight;
    }

    public HashMap getmodifymeasureAttrChng() {
        return facade.container.getmodifymeasureAttrChng();
    }

    public String getrowSuffix() {
        return facade.container.getrowSuffix();
    }

    public HashMap getisrenamed() {
        return facade.container.getisrenamed();
    }
//      public HashMap getNFMap(){
//       return (HashMap)facade.container.getTableHashMap().get("NFMap");
//   }

    public BigDecimal getSubTotalVal(SubTotalHolder stHolder, int column) {


        if (facade.container.isReportCrosstab()) {
//   HashMap<String,String> crosstabMeasureId=((PbReturnObject)facade.container.getRetObj()).crosstabMeasureId;
//   ArrayList<String> list=new ArrayList<String>();
//   if(facade.container.getCurrAndPriorValOfMsrMap()!=null && !facade.container.getCurrAndPriorValOfMsrMap().isEmpty() && facade.container.getCurrAndPriorValOfMsrMap().containsKey(crosstabMeasureId.get(facade.getColumnId(column)).toString())){
//     list=(ArrayList<String>)facade.container.getCurrAndPriorValOfMsrMap().get(crosstabMeasureId.get(facade.getColumnId(column)).toString());
//
//   }
            return stHolder.getSubTotalValue(facade.getColumnId(column));
        } else {
            //  
            if (facade.container.getCurrAndPriorValOfMsrMap() != null && !facade.container.getCurrAndPriorValOfMsrMap().isEmpty() && facade.container.getCurrAndPriorValOfMsrMap().containsKey(facade.getColumnId(column).replace("A_", ""))) {
                ArrayList<String> list = facade.container.getCurrAndPriorValOfMsrMap().get(facade.getColumnId(column).replace("A_", ""));
                BigDecimal curr = BigDecimal.ZERO;
                BigDecimal prior = BigDecimal.ZERO;
                BigDecimal crctVal = BigDecimal.ZERO;
                BigDecimal sub = BigDecimal.ZERO;
                curr = stHolder.getSubTotalValue("A_" + list.get(0));
                prior = stHolder.getSubTotalValue("A_" + list.get(1));
                //added by Nazneen
                if (curr != null && prior != null) {
                    if (prior.intValue() != 0) {
                        sub = curr.subtract(prior);
                        crctVal = (sub.divide(prior, MathContext.DECIMAL32)).multiply(new BigDecimal(100));
                    } else {
                        crctVal = curr;
                    }

                    return crctVal;
                } else {
                    return stHolder.getSubTotalValue(facade.getColumnId(column));
                }
            } else {
                return stHolder.getSubTotalValue(facade.getColumnId(column));
            }
        }

    }

    //added by nazneen
    public BigDecimal getComputeFormulaVal(SubTotalHolder stHolder, int column, String tempFormula, String mysqlString, String stType) {
        PbDb pbdb = new PbDb();
        String formula = "";
        String summerizedid = facade.getColumnId(column);//added by sruthi for hideGtZero
        HashMap<String, BigDecimal> summerizedval = new HashMap<String, BigDecimal>();//added by sruthi for hideGtZero
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            tempFormula = "SELECT " + tempFormula;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            mysqlString = mysqlString.substring(1);
            tempFormula = "SELECT " + tempFormula + " FROM (SELECT " + mysqlString + ") A";
        } else {
            tempFormula = "SELECT " + tempFormula + " FROM DUAL";
        }
        PbReturnObject retobj2 = null;
        try {
            if (tempFormula.contains("CURRENT") || tempFormula.contains("PRIOR")) {
                if (stType.equalsIgnoreCase("GT")) {
                    return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//                    if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
//                        return BigDecimal.ZERO;
//                    } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
//                        return BigDecimal.ZERO;
//                    } else {
//                        return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                    }
//                     if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())) {
//                        if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
//                            return BigDecimal.ZERO;
//                        } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
//                            return BigDecimal.ZERO;
//                        } else {
//                            return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                        }
//                    } else {
//                        return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//                    }
                } else {
                    return this.getSubTotalVal(stHolder, column);
                }
            }
            retobj2 = pbdb.execSelectSQL(tempFormula);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retobj2 != null && retobj2.getRowCount() > 0) {
            formula = retobj2.getFieldValueString(0, 0);
            if (formula.equalsIgnoreCase("")) {
                formula = "0";
            }
            BigDecimal subTotalVal = new BigDecimal(formula);
            subTotalVal = subTotalVal.setScale(4, RoundingMode.CEILING);
            //  System.out.println("subTotalVal...    subTotalVal.setScale(4, RoundingMode.CEILING)...."+subTotalVal);
            summerizedval.put(summerizedid, subTotalVal);//added by sruthi for hideGtZero
            facade.container.setSummerizedGt(summerizedval);//added by sruthi for hideGtZero
            return subTotalVal;
        } else {
            if (stType.equalsIgnoreCase("GT")) {
                return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//                if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
//                    return BigDecimal.ZERO;
//                } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
//                    return BigDecimal.ZERO;
//                } else {
//                    return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                }
//                 if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())) {
//                //
//                if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
//                    return BigDecimal.ZERO;
//                } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
//                    return BigDecimal.ZERO;
//                } else {
//                    return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                }
//                } else {
//                    return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//                }
            } else {
                return this.getSubTotalVal(stHolder, column);
            }
        }
    }
//start of code by Nazneen for sub total deviation

    public BigDecimal getSubTotalVal(int column, String stype, String dimData, int rowViewByCount) {
//     return this.getSubTotalValue(this.subTotalHolder[getViewByCount() - 1], column, stype);//for single row view by
//      return this.getSubTotalValue(this.subTotalHolder[0], column, stype);
//        
//        

        if (rowViewByCount == 1) {
            return this.getSubTotalValue(this.subTotalHolder[getViewByCount() - 1], column, stype);//for single row view by
        } //      else if(rowViewByCount==2){
        //        SubTotalHolder subHolder = null;
        //        subHolder = this.computedSubTotLst.get(0);//for 2 row view by
        //        return this.getSubTotalValue(subHolder, column, stype);
        //      }
        else {
            String key = rowViewByCount - 2 + "~" + dimData.substring(1);
            if (subTotalDeviationMap != null && !subTotalDeviationMap.isEmpty()) {
                String val = subTotalDeviationMap.get(key);
                if (val != null && !val.equalsIgnoreCase("")) {
                    int len1 = val.length();
                    if (val.startsWith("~")) {
                        val = "0" + val;
                    }
                    if (val.endsWith("~")) {
                        val = val + "0";
                    }
                    while (val.contains("~~")) {
                        val = val.replace("~~", "~0~");
                    }
                    String subTotalValArray[] = val.split("~");
                    int index = column - rowViewByCount;
                    String subTotalVals = subTotalValArray[index + 1];
                    subTotalVals = subTotalVals.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "");
                    subTotalVals = subTotalVals.replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "");
                    subTotalVals = subTotalVals.replace("C", "").replace("ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬", "").replace("ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¥", "").replace("ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬", "").replace("ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¥", "").replace(" ", "").replace(" ", "").replace(" ", "");
//                    BigDecimal subTotalVal = null;
//                    subTotalVal = new BigDecimal(subTotalVals);
                    BigDecimal subTotalVal = BigDecimal.ZERO;
                    if (subTotalVals != null && !subTotalVals.equalsIgnoreCase("null") && !subTotalVals.equalsIgnoreCase("")) {
                        subTotalVal = new BigDecimal(subTotalVals);
                    }
                    return subTotalVal;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public String getSubTotalDeviation(String elementId) {
        return facade.container.getSubTotalDeviation(elementId);
    }

    public String getMeasureType(String elementId) {
        return facade.container.getmeasureType(elementId);
    }
//   end of code by Nazneen for sub total deviation
//   start of code by Nazneen for calculating SUB TOTAL,if the measure is of Type Avg or of type 4 or summarized

    public BigDecimal getSubTotalValueForSummMeasures(SubTotalHolder stHolder, int column, String stType) {
        //for calculating SUB TOTAL for normal reports,if the measure is of Type Avg or of type 4 or summarized
        String eleId = "";
        String tempFormula = "";
        String refferedElements = "";
        String userColType = "";
        String refElementType = "";
        String aggType = "";
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = null;
        boolean isRunTime = false;
        String crossEleId = "";
        if (isCrossTab()) {
            crossEleId = facade.getColumnId(column);
            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
            eleId = crosstabMeasureId.get(facade.getColumnId(column));
            if (crossEleId.contains("_percentwise") || crossEleId.contains("_rank") || crossEleId.contains("_wf") || crossEleId.contains("_wtrg") || crossEleId.contains("_rt") || crossEleId.contains("_pwst") || crossEleId.contains("_excel") || crossEleId.contains("_excel_target") || crossEleId.contains("_deviation_mean") || crossEleId.contains("_gl") || crossEleId.contains("_userGl") || crossEleId.contains("_timeBased") || crossEleId.contains("_changedPer") || crossEleId.contains("_glPer")|| crossEleId.contains("_Qtdrank") || crossEleId.contains("_Ytdrank") || crossEleId.contains("_PMtdrank") || crossEleId.contains("_PQtdrank") || crossEleId.contains("_PYtdrank") ) {
                crossEleId = crossEleId.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "");
                isRunTime = true;
                if (eleId == null || eleId == "null" || eleId == "") {
                    eleId = crosstabMeasureId.get(crossEleId);
                }
            }
        } else {
            eleId = facade.getColumnId(column);
        }
        if (eleId != null && eleId != "null" & eleId != "") {
            eleId = eleId.replace("A_", "");
            //on 22Jan14 for run time measure of type AVG
            if (eleId.contains("_percentwise") || eleId.contains("_rank") || eleId.contains("_wf") || eleId.contains("_wtrg") || eleId.contains("_rt") || eleId.contains("_pwst") || eleId.contains("_excel") || eleId.contains("_excel_target") || eleId.contains("_deviation_mean") || eleId.contains("_gl") || eleId.contains("_userGl") || eleId.contains("_timeBased") || eleId.contains("_changedPer") || eleId.contains("_glPer")|| eleId.contains("_Qtdrank") || eleId.contains("_Ytdrank") || eleId.contains("_PMtdrank") || eleId.contains("_PQtdrank") || eleId.contains("_PYtdrank")  ) {
                eleId = eleId.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_rankST", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "");
                isRunTime = true;
            }
            // on 22Jan14 for run time measure of type AVG
            String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
            try {
                retobj = pbdb.execSelectSQL(qry);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            if (retobj != null && retobj.getRowCount() > 0) {
                tempFormula = retobj.getFieldValueString(0, 0);
                refferedElements = retobj.getFieldValueString(0, 1);
                userColType = retobj.getFieldValueString(0, 2);
                tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");
                refElementType = retobj.getFieldValueString(0, 3);
                aggType = retobj.getFieldValueString(0, 4);
            } else {
                return this.getSubTotalVal(stHolder, column);
            }

            //added by Nazneen on 21 April 2014 to calculate ST for MIN AND MAX type Measures
            if (!facade.container.isReportCrosstab()) {
                if (aggType.equalsIgnoreCase("MIN") || aggType.toUpperCase().contains("MAX")) {
                    BigDecimal STMinMaxValue = BigDecimal.ZERO;
                    String dimValue = stHolder.getDimensionValue();
                    if (aggType.toUpperCase().contains("MIN")) {
                        STMinMaxValue = stHolder.getSubTotalMinVal(facade.getColumnId(column), dimValue);
                    } else if (aggType.toUpperCase().contains("MAX")) {
                        STMinMaxValue = stHolder.getSubTotalMaxVal(facade.getColumnId(column), dimValue);
                    }
                    STMinMaxValue = STMinMaxValue.setScale(2, RoundingMode.CEILING);
                    return STMinMaxValue;
                }
            }
            //ended by Nazneen on 21 April 2014 to calculate ST for MIN AND MAX type Measures
//            else{
//                if (aggType.equalsIgnoreCase("MIN") || aggType.toUpperCase().contains("MAX")) {
//                
//                }
//            }

            //on 22Jan14 for run time measure of type AVG
            if (isRunTime) {
                if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                }
                return this.getSubTotalVal(stHolder, column);
            }
            //22Jan14 for run time measure of type AVG
            //for trailing avg measures
            if (userColType.equalsIgnoreCase("TIMECALUCULATED") && aggType.toUpperCase().contains("avg")) {
                facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                return this.getSubTotalVal(stHolder, column);
            }
        } else {
            return this.getSubTotalVal(stHolder, column);
        }
        //if(facade.container.getKpiQrycls()!=null&& !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_","").trim())){
        if (!facade.container.isReportCrosstab()) {
            if (eleId != null && eleId != "null" & eleId != "") {
                eleId = eleId.replace("A_", "");
                try {
                    if (!userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                        facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                    }
                    //added by Nazneen 0n 21 Apr for calculating sub total for elements of type 4 and summarized for normal reports
//                    if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
                    if (refElementType.equalsIgnoreCase("4")) {
                        facade.container.summarizedHashmap.put(facade.getColumnId(column), "NOT_AVG");
                        return getSTForChangePerOfSummTabMeasures(eleId, column, stHolder);
                    }
                    //ended by Nazneen 0n 21 Apr for calculating sub total for elements of type 4 and summarized for normal reports

//                   for calculating SUB TOTAL for normal reports,if the measure is of type summarized
                    if (userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("AVG")) {
                        String refEleArray[] = refferedElements.split(",");
                        int len = refEleArray.length;
                        int flag = 1;
                        String mysqlString = "";
                        for (int i = 0; i < len; i++) {
                            String elementId = refEleArray[i];
                            String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE,USER_COL_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                            PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                            if (retobj1 != null && retobj1.getRowCount() > 0) {
                                String bussColName = retobj1.getFieldValueString(0, 0);
                                String aggrType = retobj1.getFieldValueString(0, 1);
                                String userColType1 = retobj1.getFieldValueString(0, 2);
//                                if (tempFormula.contains(bussColName)) {
                                if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                    String newEleID = "A_" + elementId;
//                                    tempFormula = tempFormula.replace(bussColName, newEleID);
                                    tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                    BigDecimal subTotalValueForEle = null;
//                                    String key = "ST_"+newEleID;
                                    //if(SummarizedMeasureGTVals!=null && SummarizedMeasureGTVals.get(key)!=null){
                                    // subTotalValueForEle = SummarizedMeasureGTVals.get(key);
                                    // } else {
                                    subTotalValueForEle = stHolder.getSubTotalValue(newEleID);
                                    // }
                                    if (subTotalValueForEle == null) {
                                        flag = 0;
                                    }
                                    if (flag == 1) {
                                        subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                                        if (!userColType1.equalsIgnoreCase("SUMMARIZED") && aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
//                                            subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(stHolder.rowCount), RoundingMode.HALF_UP);
//                                        }
                                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                            mysqlString = mysqlString + "," + subTotalValueForEle + " AS '" + newEleID + "'";//modified by Dinanath
                                        } else {
                                            tempFormula = tempFormula.replace(newEleID, subTotalValueForEle.toString());
                                        }
                                    } else {
                                        return this.getSubTotalVal(stHolder, column);
                                    }
                                }
                            } else {
                                return this.getSubTotalVal(stHolder, column);
                            }
                        }
                        //Calculate formula
                        if (!tempFormula.equalsIgnoreCase("")) {
                            facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                            BigDecimal stValue = getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
                            SummarizedMeasureGTVals.put("ST_" + facade.getColumnId(column), stValue);
                            return stValue;
                        } else {
                            return this.getSubTotalVal(stHolder, column);
                        }
                    } else {
                        return this.getSubTotalVal(stHolder, column);
                    }
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                    return this.getSubTotalVal(stHolder, column);
                }

            } else {
                return this.getSubTotalVal(stHolder, column);
            }
        } // calculate ST for Cross Tab reports
        else if (facade.container.isReportCrosstab()) {
            HashMap<String, ArrayList> nonViewByMapNew = null;
            nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
            eleId = crosstabMeasureId.get(facade.getColumnId(column));
            if (eleId != null && eleId != "null" & eleId != "") {
                eleId = eleId.replace("A_", "");
                try {
                    if (!userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                        facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                    }
                    String mysqlString = "";
                    String refEleArray[] = refferedElements.split(",");
                    int len = refEleArray.length;
                    int flag = 1;
                    //added by Nazneen 0n 21 Apr for calculating sub total for elements of type 4 and summarized for cross Tab
//                    modified by Bhargavi
//                    if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
                    if (refElementType.equalsIgnoreCase("4")) {
                        facade.container.summarizedHashmap.put(facade.getColumnId(column), "NOT_AVG");
                        return getSTForChangePerOfSummCrossTabMeasures(eleId, crosstabMeasureId, column, stHolder, nonViewByMapNew);
                    }
                    //ended by Nazneen 0n 21 Apr for calculating sub total for elements of type 4 and summarized for cross Tab

                    //for calculating SUB TOTAL for cross tab,if the measure is of Type Avg or of type 4
//                if(facade.container.getKpiQrycls()!=null&& !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(eleId.trim())){
                    if (aggType.equalsIgnoreCase("AVG")) {
                        //if measure is of type summarized
                        if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                            refEleArray = refferedElements.split(",");
                            len = refEleArray.length;
                            flag = 1;
                            mysqlString = "";
                            //for calculating subtotal for avg cols like A_11,A_12 which are of gt type
                            if (facade.getColumnId(column).contains("A_")) {
                                for (int i = 0; i < len; i++) {
                                    String elementId = refEleArray[i];
                                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE,USER_COL_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                    PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                                        String bussColName = retobj1.getFieldValueString(0, 0);
                                        String aggrType = retobj1.getFieldValueString(0, 1);
                                        String userColType1 = retobj1.getFieldValueString(0, 2);
//                                        if (tempFormula.contains(bussColName)) {
                                        if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                            String newEleID = "A_" + elementId;
//                                            tempFormula = tempFormula.replace(bussColName, newEleID);
                                            tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                            BigDecimal subTotalValueForEle = null;
                                            for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                                String key = e.getKey();
                                                String value = e.getValue();
                                                if (value.equalsIgnoreCase(newEleID) && key.contains("A_")) {
                                                    String keys = "ST_" + key;
                                                    if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.get(keys) != null) {
                                                        subTotalValueForEle = SummarizedMeasureGTVals.get(keys);
                                                    } else {
                                                        subTotalValueForEle = stHolder.getSubTotalValue(key);
                                                    }
                                                    break;
                                                }
                                            }
                                            if (subTotalValueForEle == null) {
                                                flag = 0;
                                            }
                                            if (flag == 1) {
                                                subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                                                if (!userColType1.equalsIgnoreCase("SUMMARIZED") && aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
//                                                    subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(stHolder.rowCount), RoundingMode.HALF_UP);
//                                                }
                                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                    mysqlString = mysqlString + "," + subTotalValueForEle + " AS '" + newEleID + "'";//modified by Dinanath
                                                } else {
                                                    tempFormula = tempFormula.replace(newEleID, subTotalValueForEle.toString());
                                                }
                                            } else {
                                                return this.getSubTotalVal(stHolder, column);
                                            }
                                        }
                                    } else {
                                        return this.getSubTotalVal(stHolder, column);
                                    }
                                }
                                //Calculate formula
                                if (!tempFormula.equalsIgnoreCase("")) {
                                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                                    BigDecimal stValue = getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
                                    SummarizedMeasureGTVals.put("ST_" + facade.getColumnId(column), stValue);
                                    return stValue;
                                } else {
                                    return this.getSubTotalVal(stHolder, column);
                                }
                            } else {
                                //for calculating subtotal for avg cols like A1,A2,A3 which are not of gt type
                                if (nonViewByMapNew.get(facade.getColumnId(column)) != null) {
                                    String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
                                    String mainKeySetArray1[] = keyset.split(",");
                                    String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
                                    for (int k = 0; k < mainKeySetArray1.length - 1; k++) {
                                        String val1 = mainKeySetArray1[k];
                                        mainKeySetArray[k] = mainKeySetArray1[k];
                                    }

                                    for (int i = 0; i < len; i++) {
                                        int flag1 = 0;
                                        flag = 0;
                                        BigDecimal subTotalValueForEle = null;
                                        String elementId = refEleArray[i];
                                        String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE,USER_COL_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                        PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                        if (retobj1.getRowCount() > 0) {
                                            String bussColName = retobj1.getFieldValueString(0, 0);
                                            String aggrType = retobj1.getFieldValueString(0, 1);
                                            String userColType1 = retobj1.getFieldValueString(0, 2);
//                                            if (tempFormula.contains(bussColName)) {
                                            if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                String newEleID = "A_" + elementId;
//                                                tempFormula = tempFormula.replace(bussColName, newEleID);
                                                tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                for (String key1 : nonViewByMapNew.keySet()) {
                                                    if (flag1 == 0) {
                                                        String tempKeyset = nonViewByMapNew.get(key1).toString().replace("[", "").replace("]", "").trim();
                                                        String tempKeysetArray1[] = tempKeyset.split(",");
                                                        boolean cmprStr1 = true;
                                                        for (int l = 0; l < tempKeysetArray1.length; l++) {
                                                            if (!tempKeysetArray1[l].equals(mainKeySetArray1[l])) {
                                                                cmprStr1 = false;
                                                                break;
                                                            }
                                                        }
                                                        if (cmprStr1 == false) {
                                                            if (tempKeysetArray1.length == mainKeySetArray1.length) {
                                                                String tempKeysetArray[] = new String[tempKeysetArray1.length - 1];
                                                                for (int k = 0; k < tempKeysetArray1.length - 1; k++) {
                                                                    tempKeysetArray[k] = tempKeysetArray1[k];
                                                                }
                                                                boolean cmprStr = true;
                                                                for (int m = 0; m < tempKeysetArray.length; m++) {
                                                                    if (!tempKeysetArray[m].equals(mainKeySetArray[m])) {
                                                                        cmprStr = false;
                                                                        break;
                                                                    }
                                                                }
                                                                if (cmprStr) {
                                                                    for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                                                        String key = e.getKey();
                                                                        String value = e.getValue();
                                                                        if (value.equalsIgnoreCase(newEleID) && key.equalsIgnoreCase(key1)) {
                                                                            String keys = "ST_" + key;
                                                                            if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.get(keys) != null) {
                                                                                subTotalValueForEle = SummarizedMeasureGTVals.get(keys);
                                                                            } else {
                                                                                subTotalValueForEle = stHolder.getSubTotalValue(key);
                                                                            }

                                                                            flag = 1;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (subTotalValueForEle == null) {
                                                                        flag = 0;
                                                                    }
                                                                    if (flag == 1) {
//                                                        subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                                                        tempFormula = tempFormula.replace(newEleID,subTotalValueForEle.toString());
                                                                        flag1 = 1;
                                                                    }
//                                                      else { return this.getSubTotalVal(stHolder, column); }
                                                                }
                                                            }
                                                        }
                                                    }
//                                 if(flag==1)
//                                     break;
                                                }
                                                if (flag == 1) {
                                                    subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                                                    if (!userColType1.equalsIgnoreCase("SUMMARIZED") && aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
//                                                        subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(stHolder.rowCount), RoundingMode.HALF_UP);
//                                                    }
                                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                        mysqlString = mysqlString + "," + subTotalValueForEle + " AS '" + newEleID + "'";
                                                    } else {
                                                        tempFormula = tempFormula.replace(newEleID, subTotalValueForEle.toString());
                                                    }
                                                    flag1 = 1;
                                                } else {
                                                    return this.getSubTotalVal(stHolder, column);
                                                }
                                            }
                                        } else {
                                            return this.getSubTotalVal(stHolder, column);
                                        }
                                    }
                                    //Calculate formula
                                    if (!tempFormula.equalsIgnoreCase("")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                                        BigDecimal stValue = getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
                                        SummarizedMeasureGTVals.put("ST_" + facade.getColumnId(column), stValue);
                                        return stValue;
                                    } else {
                                        return this.getSubTotalVal(stHolder, column);
                                    }
                                } else {
                                    return this.getSubTotalVal(stHolder, column);
                                }
                            }
                        } else {
                            return this.getSubTotalVal(stHolder, column);
                        }

                    } else {
                        return this.getSubTotalVal(stHolder, column);
                    }
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                    return this.getSubTotalVal(stHolder, column);
                }
            } else {
                return this.getSubTotalVal(stHolder, column);
            }

        } else {
            return this.getSubTotalVal(stHolder, column);
        }
    }
//  start of code by Nazneen for calculating SUB TOTAL,if the measure is of Type Avg or of type 4 or summarized
//   start of code by Nazneen for calculating GT,if the measure is of Type Avg or of type 4 or summarized

    public BigDecimal getGTValueForSummMeasures(SubTotalHolder stHolder, int column, String stType) {
        String eleId = "";
        String tempFormula = "";
        String refferedElements = "";
        String userColType = "";
        String refElementType = "";
        String aggType = "";
        PbDb pbdb = new PbDb();
        boolean isRunTime = false;
        String crossEleId = "";
        PbReturnObject retobj = null;
        //get element id starts
        if (facade.container.isReportCrosstab()) {
            crossEleId = facade.getColumnId(column);
            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
            Set repcalc = new HashSet();
            eleId = crosstabMeasureId.get(facade.getColumnId(column));
            if (crossEleId.contains("_percentwise") || crossEleId.contains("_rankST") || crossEleId.contains("_rank") || crossEleId.contains("_wf") || crossEleId.contains("_wtrg") || crossEleId.contains("_rt") || crossEleId.contains("_pwst") || crossEleId.contains("_excel") || crossEleId.contains("_excel_target") || crossEleId.contains("_deviation_mean") || crossEleId.contains("_gl") || crossEleId.contains("_userGl") || crossEleId.contains("_timeBased") || crossEleId.contains("_changedPer") || crossEleId.contains("_glPer")|| crossEleId.contains("_MTD")|| crossEleId.contains("_QTD")|| crossEleId.contains("_YTD")|| crossEleId.contains("_PMTD")|| crossEleId.contains("_PQTD")|| crossEleId.contains("_PYTD")
                    ||crossEleId.contains("_MOM")||crossEleId.contains("_QOQ")||crossEleId.contains("_YOY")||crossEleId.contains("_MOYM")||crossEleId.contains("_QOYQ")||crossEleId.contains("_MOMPer")||crossEleId.contains("_QOQPer")||crossEleId.contains("_YOYPer")||crossEleId.contains("_MOYMPer")||crossEleId.contains("_QOYQPer") || crossEleId.contains("_Qtdrank") || crossEleId.contains("_Ytdrank") || crossEleId.contains("_PMtdrank") || crossEleId.contains("_PQtdrank") || crossEleId.contains("_PYtdrank")||crossEleId.contains("_PYMTD")||crossEleId.contains("_PYQTD")
                    ||crossEleId.contains("_WTD")||crossEleId.contains("_PWTD")||crossEleId.contains("_PYWTD")||crossEleId.contains("_WOW")||crossEleId.contains("_WOYW")||crossEleId.contains("_WOWPer")||crossEleId.contains("_WOYWPer")) {
                crossEleId = crossEleId.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                        .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                        .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "");
                isRunTime = true;
                if (eleId == null || eleId == "null" || eleId == "") {
                    eleId = crosstabMeasureId.get(crossEleId);
                }
            }
        } else {
            eleId = facade.getColumnId(column);
        }
        //get element id ends
        if (eleId != null && eleId != "null" & eleId != "") {
            elemid = eleId;
            eleId = eleId.replace("A_", "");
            //added by shravani for exclude 0,include 0 for gt in normal report
            zerorepMap = facade.container.getRetObj().zerocountmap;
            if (zerorepMap != null && zerorepMap.containsKey(elemid)) {
                zerocolumnrowcount = zerorepMap.get(elemid);
            } else {
                zerocolumnrowcount = 0;
            }
            zerorepcalcMap = facade.container.getReportCollect().crosscolmap1;
            repcalc = zerorepcalcMap.keySet();
            Iterator itcalc = repcalc.iterator();
            while (itcalc.hasNext()) {
                String key = itcalc.next().toString();
                if (key.equalsIgnoreCase(elemid)) {
                    repcalcvalue = zerorepcalcMap.get(key).toString();
                    break;
                } else {
                    repcalcvalue = "";
                }
            }
            //end of code by shravani for include &exclude 0
            //on 22Jan14 for run time measure of type AVG
            //modified by anitha
            String element_id = "";
            if (eleId.contains("_percentwise") || eleId.contains("_rank") || eleId.contains("_wf") || eleId.contains("_wtrg") || eleId.contains("_rt") || eleId.contains("_pwst") || eleId.contains("_excel") || eleId.contains("_excel_target") || eleId.contains("_deviation_mean") || eleId.contains("_gl") || eleId.contains("_userGl") || eleId.contains("_timeBased") || eleId.contains("_changedPer") || eleId.contains("_glPer")|| eleId.contains("_MTD")|| eleId.contains("_QTD")|| eleId.contains("_YTD")|| eleId.contains("_PMTD")|| eleId.contains("_PQTD")|| eleId.contains("_PYTD")
                    ||eleId.contains("_MOM")||eleId.contains("_QOQ")||eleId.contains("_YOY")||eleId.contains("_MOYM")||eleId.contains("_QOYQ")||eleId.contains("_MOMPer")||eleId.contains("_QOQPer")||eleId.contains("_YOYPer")||eleId.contains("_MOYMPer")||eleId.contains("_QOYQPer") || eleId.contains("_Qtdrank") || eleId.contains("_Ytdrank") || eleId.contains("_PMtdrank") || eleId.contains("_PQtdrank") || eleId.contains("_PYtdrank") ||eleId.contains("_PYMTD")||eleId.contains("_PYQTD")
                    ||eleId.contains("_WTD")||eleId.contains("_PWTD")||eleId.contains("_PYWTD")||eleId.contains("_WOW")||eleId.contains("_WOYW")||eleId.contains("_WOWPer")||eleId.contains("_WOYWPer")) {
                element_id = eleId;
                eleId = eleId.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                        .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                        .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "");
                isRunTime = true;
            }

            //22Jan14 for run time measure of type AVG
            String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
            try {
                retobj = pbdb.execSelectSQL(qry);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            if (retobj != null && retobj.getRowCount() > 0) {
                tempFormula = retobj.getFieldValueString(0, 0);
                refferedElements = retobj.getFieldValueString(0, 1);
                userColType = retobj.getFieldValueString(0, 2);
                refElementType = retobj.getFieldValueString(0, 3);
                //modified by anitha
                if(isRunTime == true && (element_id.contains("_MOMPer")||element_id.contains("_QOQPer")||element_id.contains("_YOYPer")||element_id.contains("_MOYMPer")||element_id.contains("_QOYQPer")||element_id.contains("_WOWPer")||element_id.contains("_WOYWPer")))
                    aggType = "avg";
                else
                aggType = retobj.getFieldValueString(0, 4);
                tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");
            } else {
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
            //added by Bhargavi on 5th August 2015 to calculate GT for MIN AND MAX type Measures
            if (aggType.toUpperCase().contains("MIN")) {
                return facade.getColumnMinimumValue(facade.getColumnId(column));
            } else if (aggType.toUpperCase().contains("MAX")) {
                return facade.getColumnMaximumValue(facade.getColumnId(column));
            }
            //ended by Bhargavi on 5th August 2015 to calculate GT for MIN AND MAX type Measures


//                on 22Jan14 for run time measure of type AVG
            if (isRunTime) {
                if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                }
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
//                    on 22Jan14 for run time measure of type AVG
            //for trailing avg measures
            if (userColType.equalsIgnoreCase("TIMECALUCULATED") && (aggType.contains("avg") || aggType.contains("AVG"))) {
                facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
        } else {
            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
        }
        //for calculating GT if the measure is of Type Avg or of type 4 or is summarized
        if (eleId != null && !eleId.equalsIgnoreCase("") && !eleId.equalsIgnoreCase("null") && !facade.container.isReportCrosstab()) {
//        if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())) {
//                if(facade.container.getKpiQrycls().contains(eleId.trim()) && aggType.equalsIgnoreCase("avg") && userColType.equalsIgnoreCase("summarized") || userColType.equalsIgnoreCase("calculated")){
            if (!userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
            }
            if (aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")) {
//                if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())) {
//                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
//               if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
//                    return BigDecimal.ZERO;
//                } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
//                    return BigDecimal.ZERO;
//                } else {
//                    return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                }
//                } else {
//                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
//                    return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//                }
                facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                return facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
            //added by Nazneen 0n 21 Apr for calculating GT for elements of type 4 and summarized for normal reports
//            if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
            if (refElementType.equalsIgnoreCase("4")) {
                facade.container.summarizedHashmap.put(facade.getColumnId(column), "NOT_AVG");
                return getGTForChangePerOfSummTabMeasures(eleId, column, stHolder);
//                if(!facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())){
//                return getGTForChangePerOfSummTabMeasures(eleId, column, stHolder);
//                } else {
////                    if(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) != null && !facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("") && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())){
//                        facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
////                        return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
////                    } else {
////                        return BigDecimal.ZERO;
////                    }
//                    if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())) {
//                        //
//                        if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
//                            return BigDecimal.ZERO;
//                        } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
//                            return BigDecimal.ZERO;
//                        } else {
//                            return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                        }
//
//                    } else {
//                        return facade.getColumnGrandTotalValue(facade.getColumnId(column));
//                    }
//                }
//                 if(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column))==null){
//                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
//                    return BigDecimal.ZERO;
//                } else if(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")){
//                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
//                    return BigDecimal.ZERO;
//               } else {
//                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
//                return  new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//               }
            }
            //ended by Nazneen 0n 21 Apr for calculating GT for elements of type 4 and summarized for normal reports

//               for summarized measures
            if (aggType.equalsIgnoreCase("avg") && userColType.equalsIgnoreCase("SUMMARIZED")) {
                String refEleArray[] = refferedElements.split(",");
                int len = refEleArray.length;
                int flag = 1;
                String mysqlString = "";
                for (int i = 0; i < len; i++) {
                    String elementId = refEleArray[i];
                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE,USER_COL_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                    PbReturnObject retobj1 = null;
                    try {
                        retobj1 = pbdb.execSelectSQL(getBussColName);
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                        String bussColName = retobj1.getFieldValueString(0, 0);
                        String aggrType = retobj1.getFieldValueString(0, 1);
                        String userColType1 = retobj1.getFieldValueString(0, 2);
//                        if(tempFormula.contains(bussColName)){
                        if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                            String newEleID = "A_" + elementId;
//                            tempFormula = tempFormula.replace(bussColName, newEleID);
                            tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                            BigDecimal grandTotalValueForEle = null;
                            String keys = "GT_" + newEleID;
                            if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.get(keys) != null) {
                                grandTotalValueForEle = SummarizedMeasureGTVals.get(keys);
                            } else {
                                grandTotalValueForEle = facade.getColumnGrandTotalValue(newEleID);
                            }

                            if (grandTotalValueForEle == null) {
                                flag = 0;
                            }
                            if (flag == 1) {
                                grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                                if (!userColType1.equalsIgnoreCase("SUMMARIZED") && aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
//                                    grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
//                                }
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    mysqlString = mysqlString + "," + grandTotalValueForEle + " AS '" + newEleID + "'";//modified by Dinanath
                                } else {
                                    tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                }
                            } else {
                                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                            }
                        }
                    } else {
                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                    }
                }
                //Calculate formula
                if (!tempFormula.equalsIgnoreCase("")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                    BigDecimal stValue = getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
                    SummarizedMeasureGTVals.put("GT_" + facade.getColumnId(column), stValue);
                    return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "GT");
                } else {
                    return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                }

            } //
            //            else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
            //                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
            //                return BigDecimal.ZERO;
            //            } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
            //                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
            //                return BigDecimal.ZERO;
            //            } else {
            //                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
            //                return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
            //            }
            //            else if(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) != null && !facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("") && !facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("null") && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())){
            //                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
            //                return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
            //            } else {
            //                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
            //                return BigDecimal.ZERO;
            //            }
            //            else if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(facade.getColumnId(column).replace("A_", "").trim())) {
            //                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
            //                if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
            //                    return BigDecimal.ZERO;
            //                } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
            //                    return BigDecimal.ZERO;
            //                } else {
            //                    return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
            //                }
            //
            //            }
            else {
                facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                return facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }

        } //GT , if report is crosstab
        else if (facade.container.isReportCrosstab()) {
            HashMap<String, ArrayList> nonViewByMapNew = null;
            nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
            eleId = crosstabMeasureId.get(facade.getColumnId(column));
            //added by shravani for include & exclude 0
            Set repcalcross = new HashSet();
            keycs = facade.getDisplayColumns().get(column);
            TreeMap tm = new TreeMap();
            String ctkey = "";
            tm = facade.container.getRetObj().getcolumnGrandTotalscross();
            Set a2b = new HashSet();
            a2b = tm.keySet();
            Iterator iter = a2b.iterator();
            while (iter.hasNext()) {
                String rowname = iter.next().toString();
                if (rowname.equalsIgnoreCase(keycs)) {
                    ctkey = keycs;
                }
            }
            if (!tm.get(ctkey).toString().equalsIgnoreCase(null)) {
                Zerocolumnrowcrosstab = Integer.parseInt(tm.get(ctkey).toString());
            } else {
                Zerocolumnrowcrosstab = 0;
            }
            repcalcross = zerorepcalcMap.keySet();
            Iterator itcalcross = repcalc.iterator();
            while (itcalcross.hasNext()) {
                String keys = itcalcross.next().toString();

                if (keys.equalsIgnoreCase(ctkey)) {
//          if(keys.equalsIgnoreCase(eleId))
                    repcalcrossvalue = zerorepcalcMap.get(ctkey).toString();
                    break;
//         if(repcalcrossvalue.equalsIgnoreCase("null") || repcalcrossvalue.isEmpty() || repcalcrossvalue.equalsIgnoreCase(""))
                } else if (keys.equalsIgnoreCase(eleId)) {
                    repcalcrossvalue = zerorepcalcMap.get(keys).toString();
                    break;
                } else {
                    repcalcrossvalue = "";
                }
            }
            //end of code by shravani for include &exclude 0

            if (eleId != null && eleId != "null" & eleId != "") {
                eleId = eleId.replace("A_", "");
                //for trailing avg measures
                if (userColType.equalsIgnoreCase("TIMECALUCULATED") && aggType.toUpperCase().contains("avg")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                    if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
                        if (nonViewByMapNew.get(facade.getColumnId(column)) != null) {
                            String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
                            if (facade.container.retObjHashmap.get(keyset) != null) {
                                return new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
                            } else {
                                return facade.getColumnGrandTotalValue(facade.getColumnId(column));
                            }
                        } else {
                            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                        }
                    } else {
                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                    }
                }
            } else {
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
//            if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty()) {
            if (eleId != null && eleId != "null" & eleId != "") {
                if (eleId != null && eleId != "null" & eleId != "") {
                    eleId = eleId.replace("A_", "");
                    //for calculating GT if the measure is of Type Avg
                    try {
                        if (!userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                            facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                        }
                        //for calculating Change %
                        //for cols like A_10,A_11 which are of gt type
                        if (refElementType.equalsIgnoreCase("4")) {
                            facade.container.summarizedHashmap.put(facade.getColumnId(column), "NOT_AVG");
                            if (facade.getColumnId(column).contains("A_")) {
                                return getGTForChangePerOfSummTabMeasuresCrossTab(eleId, column, stHolder);
//                                if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
//                                    if (nonViewByMapNew.get(facade.getColumnId(column)) != null) {
//                                        String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
//                                        if (facade.container.retObjHashmap.get(keyset) != null) {
//                                            return new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
//                                        } else {
//                                             return getGTForChangePerOfSummTabMeasuresCrossTab(eleId, column, stHolder);
//                                        }
//                                    } else {
//                                         return getGTForChangePerOfSummTabMeasuresCrossTab(eleId, column, stHolder);
//                                    }
//                                } else {
//                                    return getGTForChangePerOfSummTabMeasuresCrossTab(eleId, column, stHolder);
//                                }
                            } else {
                                return getGTForChangePerOfSummTabMeasuresCrossTab1(eleId, column, stHolder);
//                                if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
//                                    if (nonViewByMapNew.get(facade.getColumnId(column)) != null) {
//                                        String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
//                                        if (facade.container.retObjHashmap.get(keyset) != null) {
//                                            return new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
//                                        } else {
//                                             return getGTForChangePerOfSummTabMeasuresCrossTab1(eleId, column, stHolder);
//                                        }
//                                    } else {
//                                         return getGTForChangePerOfSummTabMeasuresCrossTab1(eleId, column, stHolder);
//                                    }
//                                } else {
//                                    return getGTForChangePerOfSummTabMeasuresCrossTab1(eleId, column, stHolder);
//                                }
                            }
                        }
//                        if (facade.container.getKpiQrycls().contains(eleId.trim())) {
                        if (eleId != null && eleId != "null" && eleId != "" && aggType.equalsIgnoreCase("avg")) {
                            //for calculating gt for avg cols like A_10,A_11 which are of gt type and is summarized
                            if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                                String refEleArray[] = refferedElements.split(",");
                                int len = refEleArray.length;
                                int flag = 1;
                                String mysqlString = "";
                                String newEleID = "";
                                if (facade.getColumnId(column).contains("A_")) {
                                    for (int i = 0; i < len; i++) {
                                        String elementId = refEleArray[i];
                                        String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE,USER_COL_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                        PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                        if (retobj1 != null && retobj1.getRowCount() > 0) {
                                            BigDecimal grandTotalValueForEle = null;
                                            String bussColName = retobj1.getFieldValueString(0, 0);
                                            String aggrType = retobj1.getFieldValueString(0, 1);
                                            String userColType1 = retobj1.getFieldValueString(0, 2);
//                                            if (tempFormula.contains(bussColName)) {
                                            if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                newEleID = "A_" + elementId;
//                                                tempFormula = tempFormula.replace(bussColName, newEleID);
                                                tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                                    String key = e.getKey();
                                                    String value = e.getValue();
                                                    if (value.equalsIgnoreCase(newEleID) && key.contains("A_")) {
                                                        String keys = "GT_" + key;
                                                        if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.get(keys) != null) {
                                                            grandTotalValueForEle = SummarizedMeasureGTVals.get(keys);
                                                        } else {
                                                            grandTotalValueForEle = facade.getColumnGrandTotalValue(key);
                                                        }

                                                        break;
                                                    }
                                                }
                                                if (grandTotalValueForEle == null) {
                                                    flag = 0;
                                                }
                                                if (flag == 1) {
                                                    grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                                                    if (!userColType1.equalsIgnoreCase("SUMMARIZED") && aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
//                                                        grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
//                                                    }
                                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                        mysqlString = mysqlString + "," + grandTotalValueForEle + " AS '" + newEleID + "'";
                                                    } else {
                                                        tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                    }
                                                } else {
                                                    return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                                }
                                            }
                                        } else {
                                            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                        }
                                    }
                                    //Calculate formula value
                                    if (!tempFormula.equalsIgnoreCase("")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                                        BigDecimal grandTotalValueForEle = null;
                                        grandTotalValueForEle = getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "GT");
                                        SummarizedMeasureGTVals.put("GT_" + facade.getColumnId(column), grandTotalValueForEle);
                                        return grandTotalValueForEle;
//                                        return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "GT");
                                    } else {
                                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                    }
                                } else {
                                    //for calculating gt for avg cols like A1,A2,A3 which are not of gt type
                                    if (nonViewByMapNew.get(facade.getColumnId(column)) != null) {
                                        String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
                                        String mainKeySetArray1[] = keyset.split(",");
                                        String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
                                        for (int k = 0; k < mainKeySetArray1.length - 1; k++) {
                                            String val1 = mainKeySetArray1[k];
                                            mainKeySetArray[k] = mainKeySetArray1[k];
                                        }

                                        for (int i = 0; i < len; i++) {
                                            int flag1 = 0;
                                            flag = 0;
                                            BigDecimal grangTotalValueForEle = null;
                                            String elementId = refEleArray[i];
                                            String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE,USER_COL_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                            PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                            if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                String bussColName = retobj1.getFieldValueString(0, 0);
                                                String aggrType = retobj1.getFieldValueString(0, 1);
                                                String userColType1 = retobj1.getFieldValueString(0, 2);
//                                                if (tempFormula.contains(bussColName)) {
                                                if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                    newEleID = "A_" + elementId;
//                                                    tempFormula = tempFormula.replace(bussColName, newEleID);
                                                    tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                    for (String key1 : nonViewByMapNew.keySet()) {
                                                        if (flag1 == 0) {
                                                            String tempKeyset = nonViewByMapNew.get(key1).toString().replace("[", "").replace("]", "").trim();
                                                            String tempKeysetArray1[] = tempKeyset.split(",");
                                                            boolean cmprStr1 = true;
                                                            for (int l = 0; l < tempKeysetArray1.length; l++) {
                                                                if (!tempKeysetArray1[l].equals(mainKeySetArray1[l])) {
                                                                    cmprStr1 = false;
                                                                    break;
                                                                }
                                                            }
                                                            if (cmprStr1 == false) {
                                                                if (tempKeysetArray1.length == mainKeySetArray1.length) {
                                                                    String tempKeysetArray[] = new String[tempKeysetArray1.length - 1];
                                                                    for (int k = 0; k < tempKeysetArray1.length - 1; k++) {
                                                                        tempKeysetArray[k] = tempKeysetArray1[k];
                                                                    }
                                                                    boolean cmprStr = true;
                                                                    for (int m = 0; m < tempKeysetArray.length; m++) {
                                                                        if (!tempKeysetArray[m].equals(mainKeySetArray[m])) {
                                                                            cmprStr = false;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (cmprStr) {
                                                                        for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                                                            String key = e.getKey();
                                                                            String value = e.getValue();
                                                                            if (value.equalsIgnoreCase(newEleID) && key.equalsIgnoreCase(key1)) {
                                                                                String keys = "GT_" + key;
                                                                                if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.get(keys) != null) {
                                                                                    grangTotalValueForEle = SummarizedMeasureGTVals.get(keys);
                                                                                } else {
                                                                                    grangTotalValueForEle = facade.getColumnGrandTotalValue(key);
                                                                                }

                                                                                flag = 1;
                                                                                break;
                                                                            }
                                                                        }
                                                                        if (grangTotalValueForEle == null) {
                                                                            flag = 0;
                                                                        }
                                                                        if (flag == 1) {
                                                                            flag1 = 1;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (flag == 1) {
                                                        grangTotalValueForEle = grangTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                                                        if (!userColType1.equalsIgnoreCase("SUMMARIZED") && aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
//                                                            grangTotalValueForEle = grangTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
//                                                        }
                                                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                            mysqlString = mysqlString + "," + grangTotalValueForEle + " AS '" + newEleID + "'";//modified by Dinanath
                                                        } else {
                                                            tempFormula = tempFormula.replace(newEleID, grangTotalValueForEle.toString());
                                                        }
                                                        flag1 = 1;
                                                    } else {
                                                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                                    }
                                                }
                                            } else {
                                                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                            }
                                        }
                                        //Calculate formula value
                                        if (!tempFormula.equalsIgnoreCase("")) {
                                            facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
//                                            return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "GT");
                                            BigDecimal grandTotalValueForEle = null;
                                            grandTotalValueForEle = getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "GT");
                                            SummarizedMeasureGTVals.put("GT_" + facade.getColumnId(column), grandTotalValueForEle);
                                            return grandTotalValueForEle;
                                        } else {
                                            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                        }
                                    } else {
                                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                    }
                                }
                            } else {
                                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                            }
                        } else {
                            if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
                                if (nonViewByMapNew.get(facade.getColumnId(column)) != null) {
                                    String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
                                    if (facade.container.retObjHashmap.get(keyset) != null) {
                                        return new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
                                    } else {
                                        return facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                    }
                                } else {
                                    return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                                }
                            } else {
                                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                            }
                        }
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                    }
                } else {
                    return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                }
            } else {
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
        } else {
            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
        }
    }
//   end of code by Nazneen for calculating SUB TOTAL,if the measure is of Type Avg or of type 4 or summarized

    public ArrayList<String> getReportParameters() {
        return facade.getReportParameters();
    }

    public ArrayList<String> getReportParameterNames() {
        return facade.getReportParameterNames();
    }

    public BigDecimal getGTAVGVal(int columnnum, String columnname) {
        BigDecimal subtotal = getSubTotalValue(this.subTotalHolder[getViewByCount() - 1], columnnum, "GT");
        String stType = "GT";
        String summarisedType = "SUMMARISED";
        summarisedType = facade.container.summarizedHashmap.get(facade.getColumnId(columnnum));
        if (summarisedType != null) {
            if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
                summarisedType = "NORMAL_AVG";
            } else {
                summarisedType = "SUMMARISED";
            }
        } else {
            summarisedType = "SUMMARISED";
        }
        //commented By Nazneen
//            if(!facade.getAggregationForSubtotal(facade.getColumnId(i)).equalsIgnoreCase("") && summarisedType!=null && !summarisedType.equalsIgnoreCase("SUMMARISED"))
//             {
//                 if(facade.getAggregationForSubtotal(facade.getColumnId(i)).equalsIgnoreCase("avg")&&facade.getRowCount()!=0)
//                 subTotalValue=subTotalValue.divide(new BigDecimal(facade.getRowCount()),RoundingMode.HALF_UP);
//             }
        if (summarisedType.equalsIgnoreCase("NORMAL_AVG")) {
            if (stType.equals("ST") || stType.equals("CATST")) {
                if (this.subTotalHolder[getViewByCount() - 1].rowCount != 0) {
                    ST = (this.subTotalHolder[getViewByCount() - 1].rowCount);
                }
//                     if((this.subTotalHolder[getViewByCount() - 1].rowCount!=0)== STzerocolumnrowcount)
                //added by shravani for include & exclude
                if (!facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Exclude 0")) {
                    if ((this.subTotalHolder[getViewByCount() - 1].rowCount != 0) && ST != STzerocolumnrowcount) {
                        subtotal = subtotal.divide(new BigDecimal((this.subTotalHolder[getViewByCount() - 1].rowCount) - STzerocolumnrowcount), RoundingMode.HALF_UP);
                    }
                } else if (!facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Include 0")) {
                    subtotal = subtotal.divide(new BigDecimal(this.subTotalHolder[getViewByCount() - 1].rowCount), RoundingMode.HALF_UP);
                } else if (facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Exclude 0")) {
                    if ((this.subTotalHolder[getViewByCount() - 1].rowCount != 0) && ST != STzerocolumnrowcount) {


                        subtotal = subtotal.divide(new BigDecimal((this.subTotalHolder[getViewByCount() - 1].rowCount) - STzerocolumnrowcount), RoundingMode.HALF_UP);
                    }
                } else if (facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Include 0")) {
                    subtotal = subtotal.divide(new BigDecimal(this.subTotalHolder[getViewByCount() - 1].rowCount), RoundingMode.HALF_UP);
                } else {
                    subtotal = subtotal.divide(new BigDecimal(this.subTotalHolder[getViewByCount() - 1].rowCount), RoundingMode.HALF_UP);
                }
                //end of code for include&exclude 0
            } else {
                //modified on 24Jan14
//                    if(facade.getRowCount()!=0){
//                        subTotalValue=subTotalValue.divide(new BigDecimal(facade.getRowCount()),RoundingMode.HALF_UP);
//                    }
                int rowCnt = 0;
                if (facade.container.getIsSearchFilterApplied()) {
                    if (facade.container.getaveragecalculationtype(facade.getColumnId(columnnum)).equalsIgnoreCase("Exclude 0")) {
                        if (this.subTotalHolder[getViewByCount() - 1] != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(columnnum)) != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(columnnum)) != null) {
                            rowCnt = facade.container.getGTRowCntBeforSearchFilter() - this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(columnnum));
                        }
                    } else {
                        rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                    }

                } else {
                    if (facade.container.getaveragecalculationtype(facade.getColumnId(columnnum)).equalsIgnoreCase("Exclude 0")) {

                        if (this.subTotalHolder[getViewByCount() - 1] != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(columnnum)) != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(columnnum)) != null) {
                            rowCnt = facade.getRowCount() - this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(columnnum));
                        } else {
                            rowCnt = facade.getRowCount();
                        }
                    } else {
                        rowCnt = facade.getRowCount();
                    }

                    if (rowCnt == 0) {
                        if (facade.container.getaveragecalculationtype(facade.getColumnId(columnnum)).equalsIgnoreCase("Exclude 0")) {
                            if (this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(columnnum)) != null) {
                                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount - this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(facade.getColumnId(columnnum));
                            } else {
                                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
                            }
                        } else {
                            rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
                        }

                    }
                }

                if (rowCnt != 0) {
//                        if (!facade.container.isReportCrosstab() && facade.container.getAvgcalculationtype().equalsIgnoreCase("Exclude0")) {
//                            //subTotalValue=subTotalValue.divide(new BigDecimal(rowCnt-zerocolumnrowcount),RoundingMode.HALF_UP);
//                            subTotalValue = subTotalValue.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
//                        } else {
                    subtotal = subtotal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
//                        }
                }
            }
        }

        return subtotal;
    }
    //added by Nazneen on 21 April 2014 for calculating ST of Summarized measures of type 4 in Normal Report

    public BigDecimal getSTForChangePerOfSummTabMeasures(String eleId, int column, SubTotalHolder stHolder) {
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = new PbReturnObject();
        String tempFormula = "(((CASE WHEN SUM(CURRENT) IS NULL THEN 0 ELSE SUM(CURRENT)*1.0 END )-(CASE WHEN SUM(PRIOR) IS NULL THEN 0 ELSE SUM(PRIOR)*1.0 END ))/(CASE WHEN SUM(PRIOR) = 0  THEN NULL ELSE SUM(PRIOR)*1.0 END ))*100";
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE,AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN "
                + " (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + eleId + ") AND REF_ELEMENT_TYPE in (1,2)";
        String mysqlString = "";
        try {
            retobj = pbdb.execSelectSQL(query);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    int flag = 1;
                    BigDecimal subTotalValueForEle = null;
                    String newEleID = "A_" + retobj.getFieldValueString(i, 0);
                    String keys = "ST_" + newEleID;
                    if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.containsKey(keys)) {
                        subTotalValueForEle = SummarizedMeasureGTVals.get(keys);
                    } else {
                        subTotalValueForEle = stHolder.getSubTotalValue(newEleID);
                    }
                    if (subTotalValueForEle == null) {
                        flag = 0;
                    }
                    if (flag == 1) {
                        subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                        if (retobj.getFieldValueString(i, 2).equalsIgnoreCase("AVG") || retobj.getFieldValueString(i, 2).toUpperCase().contains("AVG")) {
//                            subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(stHolder.rowCount), RoundingMode.HALF_UP);
//                        }
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            mysqlString = mysqlString + "," + subTotalValueForEle + " AS '" + retobj.getFieldValueString(i, 0) + "'";//modified by Dinanath
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", subTotalValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", subTotalValueForEle.toString());
                            }
                        } else {
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", subTotalValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", subTotalValueForEle.toString());
                            }
                        }
                    } else {
                        return this.getSubTotalVal(stHolder, column);
                    }
                }
                //Calculate formula
                if (!tempFormula.equalsIgnoreCase("")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                    return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
                } else {
                    return this.getSubTotalVal(stHolder, column);
                }
            } else {
                return this.getSubTotalVal(stHolder, column);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            return this.getSubTotalVal(stHolder, column);
        }
    }
    //added by Nazneen on 21 April 2014 for calculating ST of Summarized measures of type 4 in Cross Tab

    public BigDecimal getSTForChangePerOfSummCrossTabMeasures(String eleId, HashMap<String, String> crosstabMeasureId, int column, SubTotalHolder stHolder, HashMap<String, ArrayList> nonViewByMapNew) {
        PbDb pbdb = new PbDb();
        String mysqlString = "";
        String tempFormula = "(((CASE WHEN SUM(CURRENT) IS NULL THEN 0 ELSE SUM(CURRENT)*1.0 END )-(CASE WHEN SUM(PRIOR) IS NULL THEN 0 ELSE SUM(PRIOR)*1.0 END ))/(CASE WHEN SUM(PRIOR) = 0  THEN NULL ELSE SUM(PRIOR)*1.0 END ))*100";
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE,AGGREGATION_TYPE,USER_COL_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN "
                + " (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + eleId + ") AND REF_ELEMENT_TYPE in (1,2)";
        //for calculating subtotal for avg cols like A_11,A_12 which are of gt type
        int flag = 0;
        //  if (facade.getColumnId(column).contains("A_")) {
//            PbReturnObject retobj;
//            try {
//                retobj = pbdb.execSelectSQL(query);
//                if (retobj.rowCount > 0) {
//                    for (int i = 0; i < retobj.getRowCount(); i++) {
//                        for (String key1 : nonViewByMapNew.keySet()) {
//                        BigDecimal subTotalValueForEle = null;
//                        String newEleID = "A_" + retobj.getFieldValueString(i, 0);
//                        for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
//                            String key = e.getKey();
//                            String value = e.getValue();
//                            if (value.equalsIgnoreCase(newEleID) && key.equalsIgnoreCase(key1)) {
//                                subTotalValueForEle = stHolder.getSubTotalValue(key);
//                                break;
//                            }
//                        }

        if (nonViewByMapNew.get(facade.getColumnId(column)) != null) {
            String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
            String mainKeySetArray1[] = keyset.split(",");
            String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
            for (int k = 0; k < mainKeySetArray1.length - 1; k++) {
                mainKeySetArray[k] = mainKeySetArray1[k];
            }
//                if(mainKeySetArray[0].equalsIgnoreCase("Grand Total"))
//                {
//                    return this.getSubTotalVal(stHolder, column);
//                }
            PbReturnObject retobj;
            try {
                retobj = pbdb.execSelectSQL(query);
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    int flag1 = 0;
                    flag = 0;
                    String newEleID = "A_" + retobj.getFieldValueString(i, 0);
                    BigDecimal subTotalValueForEle = null;
                    for (String key1 : nonViewByMapNew.keySet()) {
                        if (flag1 == 0) {
                            String tempKeyset = nonViewByMapNew.get(key1).toString().replace("[", "").replace("]", "").trim();
                            String tempKeysetArray1[] = tempKeyset.split(",");
                            boolean cmprStr1 = true;
                            for (int l = 0; l < tempKeysetArray1.length; l++) {
                                if (!tempKeysetArray1[l].equals(mainKeySetArray1[l])) {
                                    cmprStr1 = false;
                                    break;
                                }
                            }
                            if (cmprStr1 == false) {
                                if (tempKeysetArray1.length == mainKeySetArray1.length) {
                                    String tempKeysetArray[] = new String[tempKeysetArray1.length - 1];
                                    for (int k = 0; k < tempKeysetArray1.length - 1; k++) {
                                        tempKeysetArray[k] = tempKeysetArray1[k];
                                    }
                                    boolean cmprStr = true;
                                    for (int m = 0; m < tempKeysetArray.length; m++) {
                                        if (!tempKeysetArray[m].equals(mainKeySetArray[m])) {
                                            cmprStr = false;
                                            break;
                                        }
                                    }
                                    if (cmprStr) {
                                        for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                            String key = e.getKey();
                                            String value = e.getValue();
                                            if (value.equalsIgnoreCase(newEleID) && key.equalsIgnoreCase(key1)) {
                                                String keys = "ST_" + key;
                                                if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.containsKey(keys)) {
                                                    subTotalValueForEle = SummarizedMeasureGTVals.get(keys);
                                                } else {
                                                    subTotalValueForEle = stHolder.getSubTotalValue(key);
                                                }
                                                flag = 1;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (subTotalValueForEle == null) {
                        flag = 0;
                    }
                    if (flag == 1) {
                        subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                            if (!retobj.getFieldValueString(i, 3).equalsIgnoreCase("SUMMARIZED") && retobj.getFieldValueString(i, 2).equalsIgnoreCase("AVG") || retobj.getFieldValueString(i, 2).toUpperCase().contains("AVG")) {
//                                subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(stHolder.rowCount), RoundingMode.HALF_UP);
//                            }

                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            mysqlString = mysqlString + "," + subTotalValueForEle + " AS '" + retobj.getFieldValueString(i, 0) + "'";//modified by Dinanath
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", subTotalValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", subTotalValueForEle.toString());
                            }
                        } else {
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", subTotalValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", subTotalValueForEle.toString());
                            }
                        }
                    } else {
                        return this.getSubTotalVal(stHolder, column);
                    }
                }
                //Calculate formula

                if (!tempFormula.equalsIgnoreCase("")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                    return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
                } else {
                    return this.getSubTotalVal(stHolder, column);
                }
//                } else {
//                    return this.getSubTotalVal(stHolder, column);
//                }
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
                return this.getSubTotalVal(stHolder, column);
            }
        } else {
            return this.getSubTotalVal(stHolder, column);
        }
    }
//       else {
//            //for calculating subtotal for avg cols like A1,A2,A3 which are not of gt type
//            if (nonViewByMapNew.get(facade.getColumnId(column)) != null) {
//                String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
//                String mainKeySetArray1[] = keyset.split(",");
//                String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
//                for (int k = 0; k < mainKeySetArray1.length - 1; k++) {
//                    String val1 = mainKeySetArray1[k];
//                    mainKeySetArray[k] = mainKeySetArray1[k];
//                }
//                PbReturnObject retobj;
//                try {
//                    retobj = pbdb.execSelectSQL(query);
//                    for (int i = 0; i < retobj.getRowCount(); i++) {
//                        int flag1 = 0;
//                        flag = 0;
//                         String newEleID = "A_" + retobj.getFieldValueString(i, 0);
//                        BigDecimal subTotalValueForEle = null;
//                        for (String key1 : nonViewByMapNew.keySet()) {
//                            if (flag1 == 0) {
//                                String tempKeyset = nonViewByMapNew.get(key1).toString().replace("[", "").replace("]", "").trim();
//                                String tempKeysetArray1[] = tempKeyset.split(",");
//                                boolean cmprStr1 = true;
//                                for (int l = 0; l < tempKeysetArray1.length; l++) {
//                                    if (!tempKeysetArray1[l].equals(mainKeySetArray1[l])) {
//                                        cmprStr1 = false;
//                                        break;
//                                    }
//                                }
//                                if (cmprStr1 == false) {
//                                    if (tempKeysetArray1.length == mainKeySetArray1.length) {
//                                        String tempKeysetArray[] = new String[tempKeysetArray1.length - 1];
//                                        for (int k = 0; k < tempKeysetArray1.length - 1; k++) {
//                                            tempKeysetArray[k] = tempKeysetArray1[k];
//                                        }
//                                        boolean cmprStr = true;
//                                        for (int m = 0; m < tempKeysetArray.length; m++) {
//                                            if (!tempKeysetArray[m].equals(mainKeySetArray[m])) {
//                                                cmprStr = false;
//                                                break;
//                                            }
//                                        }
//                                        if (cmprStr) {
//                                            for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
//                                                String key = e.getKey();
//                                                String value = e.getValue();
//                                                if (value.equalsIgnoreCase(newEleID) && key.equalsIgnoreCase(key1)) {
//                                                    subTotalValueForEle = stHolder.getSubTotalValue(key);
//                                                    flag = 1;
//                                                    break;
//                                                }
//                                            }
//                                            if (subTotalValueForEle == null) {
//                                                flag = 0;
//                                            }
//                                            if (flag == 1) {
//                                                flag1 = 1;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        if (flag == 1) {
//                            subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
//                            if (retobj.getFieldValueString(i, 2).equalsIgnoreCase("AVG") || retobj.getFieldValueString(i, 2).toUpperCase().contains("AVG")) {
//                                subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(stHolder.rowCount), RoundingMode.HALF_UP);
//                            }
//                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                                mysqlString = mysqlString + "," + subTotalValueForEle + " AS " + newEleID;
//                            } else {
//                                tempFormula = tempFormula.replace(newEleID, subTotalValueForEle.toString());
//                            }
//                            flag1 = 1;
//                        } else {
//                            return this.getSubTotalVal(stHolder, column);
//                        }
//                    }
//                    if (!tempFormula.equalsIgnoreCase("")) {
//                        facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
//                        return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
//                    } else {
//                        return this.getSubTotalVal(stHolder, column);
//                    }
//
//                } catch (SQLException ex) {
//                    logger.error("Exception:",ex);
//                      return this.getSubTotalVal(stHolder, column);
//                }
//            } else {
//                return this.getSubTotalVal(stHolder, column);
//            }
//        }

    // }
    public BigDecimal getGTForChangePerOfSummTabMeasures(String eleId, int column, SubTotalHolder stHolder) {
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = new PbReturnObject();
        String tempFormula = "(((CASE WHEN SUM(CURRENT) IS NULL THEN 0 ELSE SUM(CURRENT)*1.0 END )-(CASE WHEN SUM(PRIOR) IS NULL THEN 0 ELSE SUM(PRIOR)*1.0 END ))/(CASE WHEN SUM(PRIOR) = 0  THEN NULL ELSE SUM(PRIOR)*1.0 END ))*100";
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE,AGGREGATION_TYPE,USER_COL_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN "
                + " (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + eleId + ") AND REF_ELEMENT_TYPE in (1,2)";
        String mysqlString = "";
        try {
            retobj = pbdb.execSelectSQL(query);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    int flag = 1;
                    BigDecimal gtValueForEle = null;
                    String newEleID = "A_" + retobj.getFieldValueString(i, 0);
                    String keys = "GT_" + newEleID;
                    if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.containsKey(keys)) {
                        gtValueForEle = SummarizedMeasureGTVals.get(keys);
                    } else {
                        gtValueForEle = facade.getColumnGrandTotalValue(newEleID);
                    }

                    if (gtValueForEle == null) {
                        flag = 0;
                    }
                    if (flag == 1) {
                        gtValueForEle = gtValueForEle.setScale(2, RoundingMode.CEILING);
//                       if (!retobj.getFieldValueString(i, 3).equalsIgnoreCase("SUMMARIZED") && retobj.getFieldValueString(i, 2).equalsIgnoreCase("AVG") || retobj.getFieldValueString(i, 2).toUpperCase().contains("AVG")) {
//                            gtValueForEle = gtValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
//                        }
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            mysqlString = mysqlString + "," + gtValueForEle + " AS '" + retobj.getFieldValueString(i, 0) + "'";//modified by Dinanath for syntax error
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        } else {
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        }
                    } else {
                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                    }
                }
                //Calculate formula
                if (!tempFormula.equalsIgnoreCase("")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                    return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
                } else {
                    return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                }
            } else {
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
        }
    }

    public BigDecimal getGTForChangePerOfNormalMeasures(String eleId, int column, SubTotalHolder stHolder) {
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = new PbReturnObject();
        String tempFormula = "(((CASE WHEN SUM(CURRENT) IS NULL THEN 0 ELSE SUM(CURRENT)*1.0 END )-(CASE WHEN SUM(PRIOR) IS NULL THEN 0 ELSE SUM(PRIOR)*1.0 END ))/(CASE WHEN SUM(PRIOR) = 0  THEN NULL ELSE SUM(PRIOR)*1.0 END ))*100";
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE,AGGREGATION_TYPE,USER_COL_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN "
                + " (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + eleId + ") AND REF_ELEMENT_TYPE in (1,2)";
        String mysqlString = "";
        try {
            retobj = pbdb.execSelectSQL(query);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    int flag = 1;
                    BigDecimal gtValueForEle = null;
                    String newEleID = "A_" + retobj.getFieldValueString(i, 0);
                    String keys = "GT_" + newEleID;
                    if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.containsKey(keys)) {
                        gtValueForEle = SummarizedMeasureGTVals.get(keys);
                    } else {
                        gtValueForEle = facade.getColumnGrandTotalValue(newEleID);
                    }
                    if (gtValueForEle == null) {
                        flag = 0;
                    }
                    if (flag == 1) {
                        gtValueForEle = gtValueForEle.setScale(2, RoundingMode.CEILING);
//                        if (!retobj.getFieldValueString(i, 3).equalsIgnoreCase("SUMMARIZED") && retobj.getFieldValueString(i, 2).equalsIgnoreCase("AVG") || retobj.getFieldValueString(i, 2).toUpperCase().contains("AVG")) {
//                            gtValueForEle = gtValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
//                        }
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            mysqlString = mysqlString + "," + gtValueForEle + " AS '" + retobj.getFieldValueString(i, 0) + "'";//added by Dinanath for syntax error issue
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        } else {
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        }
                    } else {
                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                    }
                }
                //Calculate formula
                if (!tempFormula.equalsIgnoreCase("")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                    return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "ST");
                } else {
                    return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                }
            } else {
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
        }
    }

    public boolean isIsSubtotalTopBottom() {
        return facade.isIsSubtotalTopBottom();
    }

    public boolean getIsSubToTalSearchFilterApplied() {
        return facade.getIsSubToTalSearchFilterApplied();
    }

    public ArrayList<String> getSubTotalSrchColumns() {
        return facade.getSubTotalSrchColumns();
    }

    /**
     * @return the subTotalSrchCondition
     */
    public ArrayList<String> getSubTotalSrchCondition() {
        return facade.getSubTotalSrchCondition();
    }

    /**
     * @return the subTotalSrchValue
     */
    public ArrayList<String> getSubTotalSrchValue() {
        return facade.getSubTotalSrchValue();
    }

//Method to check whether the subtotal satisfies the search filter condition By Govardhan
    public boolean isFilteredSubTotalGroup(ArrayList<String> displayColumns, TableSubtotalRow subtotalRows, ArrayList<String> SrchColumns, ArrayList<String> Values, ArrayList<String> conditions) {

        boolean isfiltered = true;
        for (int j = getViewByCount(); j < displayColumns.size(); j++) {

            if (SrchColumns.contains(displayColumns.get(j))) {
                int index = SrchColumns.indexOf(displayColumns.get(j));
                BigDecimal ActualValue = new BigDecimal(subtotalRows.getRowData(j).replace("C", "").replace("ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬", "").replace("ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¥", "").replaceAll(",", "").replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "").replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "").replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "").replace("C", "").replace("ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬", "").replace("ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¥", "").replace(" ", ""));

                BigDecimal SearchValue = new BigDecimal(Values.get(index));
                String SearchCondition = conditions.get(index);
                int res = ActualValue.compareTo(SearchValue);

                if (SearchCondition.equalsIgnoreCase("GT")) {
                    if (res == 1) {
                        isfiltered = true;
                    } else {
                        return false;
                    }
                } else if (SearchCondition.equalsIgnoreCase("GE")) {
                    if (res == 1 || res == 0) {
                        isfiltered = true;
                    } else {
                        return false;
                    }

                } else if (SearchCondition.equalsIgnoreCase("LT")) {
                    if (res == -1) {
                        isfiltered = true;
                    } else {
                        return false;
                    }

                } else if (SearchCondition.equalsIgnoreCase("LE")) {
                    if (res == -1 || res == 0) {
                        isfiltered = true;
                    } else {
                        return false;
                    }

                } else if (SearchCondition.equalsIgnoreCase("EQ")) {
                    if (res == 0) {
                        isfiltered = true;
                    } else {
                        return false;
                    }

                }



            }



        }


        return isfiltered;
    }

    public void setFirstRowAfterSubTtlFiltr(String Clumnid) {

        this.FirstRowAfterSubTtlFiltr = Clumnid;

    }

    public String getFirstRowAfterSubTtlFiltr() {

        return this.FirstRowAfterSubTtlFiltr;
    }

    /**
     * @return the isCountingRowsAfterFilter
     */
    public boolean isIsCountingRowsAfterFilter() {
        return isCountingRowsAfterFilter;
    }

    /**
     * @param isCountingRowsAfterFilter the isCountingRowsAfterFilter to set
     */
    public void setIsCountingRowsAfterFilter(boolean isCountingRowsAfterFilter) {
        this.isCountingRowsAfterFilter = isCountingRowsAfterFilter;
    }
//end of code by Govardhan

    //Method added by govadhan for re-initializing the properties after the the for loop on whole data to get number of records
    //   filtered...
    public void resetInitialProperties() {
        this.subTotalHolder = new SubTotalHolder[this.getViewByCount()];
        this.subTotalHelper = new SubTotalHelper(this.facade);
        this.computedSubTotLst = new ArrayList<SubTotalHolder>();
        this.cellSpan = new HashSet<TableCellSpan>();
    }

    //end of code
    public TableSubtotalRow UpdatePageTotalRow(TableSubtotalRow subTotalRow, ArrayList<TableDataRow> filteredSubtotalRowsList, int RowCount, ArrayList<TableDataRow> Rowlist) {


        return this.subTotalHelper.UpdatePageTotalRow(subTotalRow, filteredSubtotalRowsList, RowCount, Rowlist);
    }

    public TableSubtotalRow updateGrandTotalAfterSubFilter(TableSubtotalRow subTotalRow) {


        return this.subTotalHelper.UpdateGrandTotalRow(subTotalRow, this.filteredSubtotalRowsListForGrandTotal, this.TableDataRowListSubunFiltered);
    }

    public TableSubtotalRow UpdatePageTotal(TableSubtotalRow subTotalRow, ArrayList<TableSubtotalRow> filteredSubtotalRowsList) {

        return this.subTotalHelper.UpdatePageTotal(subTotalRow, filteredSubtotalRowsList, this.TableDataRowListSubFiltered);
    }

    public BigDecimal getGTForChangePerOfSummTabMeasuresCrossTab(String eleId, int column, SubTotalHolder stHolder) {
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = new PbReturnObject();
        String tempFormula = "(((CASE WHEN SUM(CURRENT) IS NULL THEN 0 ELSE SUM(CURRENT)*1.0 END )-(CASE WHEN SUM(PRIOR) IS NULL THEN 0 ELSE SUM(PRIOR)*1.0 END ))/(CASE WHEN SUM(PRIOR) = 0  THEN NULL ELSE SUM(PRIOR)*1.0 END ))*100";
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE,AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN "
                + " (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + eleId + ") AND REF_ELEMENT_TYPE in (1,2)";
        String mysqlString = "";
        try {
            retobj = pbdb.execSelectSQL(query);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    int flag = 1;
                    BigDecimal gtValueForEle = null;
                    String newEleID = "A_" + retobj.getFieldValueString(i, 0);
                    ///////////////////////////////////////////////////////
                    HashMap<String, ArrayList> nonViewByMapNew = null;
                    nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
                    HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
                    for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                        String key = e.getKey();
                        String value = e.getValue();
                        String keys = "GT_" + key;
//                        if(SummarizedMeasureGTVals!=null && SummarizedMeasureGTVals.containsKey(Summkey)){
//                            gtValueForEle = SummarizedMeasureGTVals.get(Summkey);
//                            break;
//                        }
                        if (value.equalsIgnoreCase(newEleID) && key.contains("A_")) {
                            if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.containsKey(keys)) {
                                gtValueForEle = SummarizedMeasureGTVals.get(keys);
                            } else {
                                gtValueForEle = facade.getColumnGrandTotalValue(key);
                            }
                            break;
                        }
                    }

                    ////////////////////////////////////////////////////////
//                    gtValueForEle = facade.getColumnGrandTotalValue(newEleID);
                    if (gtValueForEle == null) {
                        flag = 0;
                    }
                    if (flag == 1) {
                        gtValueForEle = gtValueForEle.setScale(2, RoundingMode.CEILING);
//                        if (retobj.getFieldValueString(i, 2).equalsIgnoreCase("AVG") || retobj.getFieldValueString(i, 2).toUpperCase().contains("AVG")) {
//                            gtValueForEle = gtValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
//                        }
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            mysqlString = mysqlString + "," + gtValueForEle + " AS '" + retobj.getFieldValueString(i, 0) + "'";//modified by Dinanath for syntax error issue
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        } else {
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        }
                    } else {
                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                    }
                }
                //Calculate formula
                if (!tempFormula.equalsIgnoreCase("")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                    return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "GT");
                } else {
                    return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                }
            } else {
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
        }
    }

    public BigDecimal getGTForChangePerOfSummTabMeasuresCrossTab1(String eleId, int column, SubTotalHolder stHolder) {
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = new PbReturnObject();
        String tempFormula = "(((CASE WHEN SUM(CURRENT) IS NULL THEN 0 ELSE SUM(CURRENT)*1.0 END )-(CASE WHEN SUM(PRIOR) IS NULL THEN 0 ELSE SUM(PRIOR)*1.0 END ))/(CASE WHEN SUM(PRIOR) = 0  THEN NULL ELSE SUM(PRIOR)*1.0 END ))*100";
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE,AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN "
                + " (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + eleId + ") AND REF_ELEMENT_TYPE in (1,2)";
        String mysqlString = "";
        try {
            retobj = pbdb.execSelectSQL(query);
            if (retobj.rowCount > 0) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    int flag1 = 0;
                    int flag = 0;
                    BigDecimal gtValueForEle = null;
                    String newEleID = "A_" + retobj.getFieldValueString(i, 0);
                    ///////////////////////////////////////////////////////
                    HashMap<String, ArrayList> nonViewByMapNew = null;
                    nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
                    HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
                    String keyset = nonViewByMapNew.get(facade.getColumnId(column)).toString().replace("[", "").replace("]", "").trim();
                    String mainKeySetArray1[] = keyset.split(",");
                    String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];

                    for (int k = 0; k < mainKeySetArray1.length - 1; k++) {
                        String val1 = mainKeySetArray1[k];
                        mainKeySetArray[k] = mainKeySetArray1[k];
                    }

//                    String keys = "GT_"+newEleID.replace("A_", "");
//                        if(SummarizedMeasureGTVals!=null && SummarizedMeasureGTVals.containsKey(keys)){
//                            gtValueForEle = SummarizedMeasureGTVals.get(keys);
//                            flag = 1;
//                        } else {

                    for (String key1 : nonViewByMapNew.keySet()) {
                        if (flag1 == 0) {
                            String tempKeyset = nonViewByMapNew.get(key1).toString().replace("[", "").replace("]", "").trim();
                            String tempKeysetArray1[] = tempKeyset.split(",");
                            boolean cmprStr1 = true;
                            for (int l = 0; l < tempKeysetArray1.length; l++) {
                                if (!tempKeysetArray1[l].equals(mainKeySetArray1[l])) {
                                    cmprStr1 = false;
                                    break;
                                }
                            }
                            if (cmprStr1 == false) {
                                if (tempKeysetArray1.length == mainKeySetArray1.length) {
                                    String tempKeysetArray[] = new String[tempKeysetArray1.length - 1];
                                    for (int k = 0; k < tempKeysetArray1.length - 1; k++) {
                                        tempKeysetArray[k] = tempKeysetArray1[k];
                                    }
                                    boolean cmprStr = true;
                                    for (int m = 0; m < tempKeysetArray.length; m++) {
                                        if (!tempKeysetArray[m].equals(mainKeySetArray[m])) {
                                            cmprStr = false;
                                            break;
                                        }
                                    }
                                    if (cmprStr) {
                                        for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                            String key = e.getKey();
                                            String value = e.getValue();
                                            if (value.equalsIgnoreCase(newEleID) && key.equalsIgnoreCase(key1)) {
                                                String keys = "GT_" + key1;
                                                if (SummarizedMeasureGTVals != null && SummarizedMeasureGTVals.containsKey(keys)) {
                                                    gtValueForEle = SummarizedMeasureGTVals.get(keys);
                                                } else {
                                                    gtValueForEle = facade.getColumnGrandTotalValue(key);
                                                }
                                                flag = 1;
                                                break;
                                            }
                                        }
                                        if (gtValueForEle == null) {
                                            flag = 0;
                                        }
                                        if (flag == 1) {
                                            flag1 = 1;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ////////////////////////////////////////////////////////
//                    gtValueForEle = facade.getColumnGrandTotalValue(newEleID);
                    if (gtValueForEle == null) {
                        flag = 0;
                    }
                    if (flag == 1) {
                        gtValueForEle = gtValueForEle.setScale(2, RoundingMode.CEILING);
//                        if (retobj.getFieldValueString(i, 2).equalsIgnoreCase("AVG") || retobj.getFieldValueString(i, 2).toUpperCase().contains("AVG")) {
//                            gtValueForEle = gtValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
//                        }
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            mysqlString = mysqlString + "," + gtValueForEle + " AS '" + retobj.getFieldValueString(i, 0) + "'";//modified by Dinanath for syntax error issue
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        } else {
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        }
                    } else {
                        return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                    }
                }
                //Calculate formula
                if (!tempFormula.equalsIgnoreCase("")) {
                    facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                    return getComputeFormulaVal(stHolder, column, tempFormula, mysqlString, "GT");
                } else {
                    return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
                }
            } else {
                return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            return this.facade.getColumnGrandTotalValue(facade.getColumnId(column));
        }
    }

    public int getSTRowCountForSubTotalType1(SubTotalHolder subHolder, String newEleID, String stType) {
        int rowCnt = 0;
        if (getViewByCount() > 1) {
            rowCnt = 0;
            int viewByCnt = getViewByCount();
            int colNum = subHolder.viewColNum;
            int finalVal = viewByCnt - (colNum + 2);
            if (stType.equals("ST") || stType.equals("CATST")) {
                if (facade.container.getaveragecalculationtype(newEleID).equalsIgnoreCase("Exclude 0")) {
                    if (subHolder.zeroRowCountMap.get(newEleID) != null) {
                        rowCnt = subHolder.rowCount - subHolder.zeroRowCountMap.get(newEleID);
                    } else {
                        rowCnt = subHolder.rowCount;
                    }
                } else {
                    rowCnt = subHolder.rowCount;
                }

            } else {
                if (facade.container.getIsSearchFilterApplied()) {
                    if (facade.container.getaveragecalculationtype(newEleID).equalsIgnoreCase("Exclude 0")) {
                        if (subHolder.zeroRowCountMap.get(newEleID) != null) {
                            rowCnt = facade.container.getGTRowCntBeforSearchFilter() - subHolder.zeroRowCountMap.get(newEleID);
                        } else {
                            rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                        }
                    } else {
                        rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                    }

                } else {
                    if (facade.container.getaveragecalculationtype(newEleID).equalsIgnoreCase("Exclude 0")) {
                        if (subHolder.zeroRowCountMap.get(newEleID) != null) {
                            rowCnt = facade.getRowCount() - subHolder.zeroRowCountMap.get(newEleID);
                        } else {
                            rowCnt = facade.getRowCount();
                        }
                    } else {
                        rowCnt = facade.getRowCount();
                    }

                }
            }
            if (subHolder.viewColNum == 1) {
                rowCnt = rowCnt - finalVal;
            } else {
                rowCnt = rowCnt - cntOfSt;
            }
        } else {
            if (stType.equals("ST") || stType.equals("CATST")) {
                if (subHolder.rowCount != 0) {
                    if (facade.container.getaveragecalculationtype(newEleID).equalsIgnoreCase("Exclude 0")) {
                        if (subHolder.zeroRowCountMap != null) {
                            if (subHolder.zeroRowCountMap.get(newEleID) != null) {
                                rowCnt = subHolder.rowCount - subHolder.zeroRowCountMap.get(newEleID);
                            } else {
                                rowCnt = subHolder.rowCount;
                            }
                        } else {
                            rowCnt = subHolder.rowCount;
                        }
                    } else {
                        rowCnt = subHolder.rowCount;
                    }

                }
            } else {
                rowCnt = 0;
                if (facade.container.getIsSearchFilterApplied()) {
                    if (facade.container.getaveragecalculationtype(newEleID).equalsIgnoreCase("Exclude 0")) {
                        if (subHolder.zeroRowCountMap.get(newEleID) != null) {
                            rowCnt = facade.container.getGTRowCntBeforSearchFilter() - subHolder.zeroRowCountMap.get(newEleID);
                        } else {
                            rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                        }
                    } else {
                        rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                    }

                } else {
                    if (facade.container.getaveragecalculationtype(newEleID).equalsIgnoreCase("Exclude 0")) {
                        if (subHolder.zeroRowCountMap.get(newEleID) != null) {
                            rowCnt = facade.getRowCount() - subHolder.zeroRowCountMap.get(newEleID);
                        } else {
                            rowCnt = facade.getRowCount();
                        }
                    } else {
                        rowCnt = facade.getRowCount();
                    }
                }
            }
        }
        return rowCnt;
    }

    public int getGTRowCountForSubTotalType1(SubTotalHolder subHolder, String newEleId, String stType) {
        int rowCnt = 0;
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
        String stelemid = newEleId;
//        String steleleid = crosstabMeasureId.get(facade.getColumnId(i));
        if (stType.equals("ST") || stType.equals("CATST")) {
            if (this.subTotalHolder[getViewByCount() - 1].rowCount != 0) {
                ST = (this.subTotalHolder[getViewByCount() - 1].rowCount);
            }
            if (!facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Exclude 0")) {
                if ((this.subTotalHolder[getViewByCount() - 1].rowCount != 0) && ST != STzerocolumnrowcount) {
                    rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount - STzerocolumnrowcount;
                }
            } else if (!facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Include 0")) {
                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
            } else if (facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Exclude 0")) {
                if ((this.subTotalHolder[getViewByCount() - 1].rowCount != 0) && ST != STzerocolumnrowcount) {
                    rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount - STzerocolumnrowcount;
                }
            } else if (facade.container.isReportCrosstab() && repcalcvalue.equalsIgnoreCase("Include 0")) {
                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
            } else {
                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
            }
        } else {
            rowCnt = 0;
            if (facade.container.getIsSearchFilterApplied()) {
                if (facade.container.getaveragecalculationtype(newEleId).equalsIgnoreCase("Exclude 0")) {
                    if (this.subTotalHolder[getViewByCount() - 1] != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(newEleId) != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(newEleId) != null) {
                        rowCnt = facade.container.getGTRowCntBeforSearchFilter() - this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(newEleId);
                    } else {
                        rowCnt = facade.container.gettrwcnt();
                    }
                } else {
                    rowCnt = facade.container.getGTRowCntBeforSearchFilter();
                }

            } else {
                if (repcalcvalue.equalsIgnoreCase("Exclude 0")) {
                    if (this.subTotalHolder[getViewByCount() - 1] != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(newEleId) != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(newEleId) != null) {
                        rowCnt = facade.getRowCount() - this.getZeroRowCount(facade.getRowCount(), 0, stelemid);
                    } else if (this.subTotalHolder[getViewByCount() - 1] != null && this.getZeroRowCount(facade.getRowCount(), 0, stelemid) > 0) {
                        rowCnt = facade.getRowCount() - this.getZeroRowCount(facade.getRowCount(), 0, stelemid);
                    } else {
                        if ("1".equals(facade.container.stype) || "0".equals(facade.container.stype)) {
                            rowCnt = facade.container.getTotalRowCount();
                        } else {
                            rowCnt = facade.getRowCount() - this.getZeroRowCount(facade.getRowCount(), 0, stelemid);
                            facade.container.setTotalRowCount(rowCnt);
                        }
                    }
                } else {
                    if ("1".equals(facade.container.stype) || "0".equals(facade.container.stype)) {
                        rowCnt = facade.container.gettrwcnt();
                    } else {
                        rowCnt = facade.getRowCount();
                        facade.container.settrwcnt(rowCnt);
                    }
                }

                if (rowCnt == 0) {
                    if (facade.container.getaveragecalculationtype(newEleId).equalsIgnoreCase("Exclude 0")) {
                        if (this.subTotalHolder[getViewByCount() - 1] != null && this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap != null && newEleId != null) {
                            if (this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(newEleId) != null) {
                                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount - this.subTotalHolder[getViewByCount() - 1].zeroRowCountMap.get(newEleId);
                            } else {
                                if (this.subTotalHolder[getViewByCount() - 1] != null) {
                                    rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
                                }
                            }
                        } else {
                            if (this.subTotalHolder[getViewByCount() - 1] != null) {
                                rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
                            }
                        }
                    } else {
                        if (this.subTotalHolder[getViewByCount() - 1] != null) {
                            rowCnt = this.subTotalHolder[getViewByCount() - 1].rowCount;
                        }
                    }

                }
            }

        }
        return rowCnt;
    }
}
