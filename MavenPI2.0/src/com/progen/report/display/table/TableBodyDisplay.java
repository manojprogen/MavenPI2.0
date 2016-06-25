/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.google.common.collect.Iterables;
import com.progen.report.ColorHelper;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableDataRow;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.data.TableSubtotalRow;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import prg.db.PbReturnObject;
import prg.util.ProgenTimeDefinition;

/**
 *
 * @author progen
 */
public class TableBodyDisplay extends TableDisplay {

    public static Logger logger = Logger.getLogger(TableBodyDisplay.class);
    boolean isfilterLastRow = false;
    int filteredcount = 0;
    boolean isFirstRowDeleted = false;
    String FirstrowCount = "";
    int RunningTtlOfSubmittedlist = 0;
    int RowCount = 0;
    int rowcountAfterSubtotalfilter = 0;
    int subTotalRowCount = 0;
    int firstRowId = 0;
    int ActualRowsFrSbTtl = 0;
    List<String> filterList = new ArrayList<String>();
    ArrayList<TableSubtotalRow> filteredSubtotalRowsList = new ArrayList<TableSubtotalRow>();

    public TableBodyDisplay(TableBuilder builder) {
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
        StringBuilder body = new StringBuilder();
        TableDataRow tableRow;
        TableDataRow tableRowTempData;
        int fromRow, toRow;
        int batchCount = 0;
        int SubTotalListCount = 0;
        int FirstViewBySubTotalListCount = 0;

        ArrayList<TableDataRow> rowLst = new ArrayList<TableDataRow>();
        ArrayList<TableDataRow> rowLstTemp = new ArrayList<TableDataRow>();
        String flag = this.getHeadlineflag();
        //aded by Nazneen
        int ViewByCount = 0;
        int ActualRow = 0;
        int filteredRowCount = 0;
        RowCount = tableBldr.getFromRow();

        ViewByCount = tableBldr.getViewByCount();
        TableDataRow tableRowTemp;
        TableDataRow tableRowTemp1;

        Set<String> DistinctViewBys = new HashSet<String>();
        HashMap<Integer, Integer> PageAndRowsMap = new HashMap<Integer, Integer>();

        TableDataRow tableRowTemp2 = null;
        TableDataRow tableRowTempForDistinctViewBys = null;
        ArrayList<TableSubtotalRow> subTotalRowLst = new ArrayList<TableSubtotalRow>();
        String isoneview = tableBldr.getGetFromOneview();
        fromRow = tableBldr.getFromRow();
        toRow = tableBldr.getToRow();
        TableHeaderRow[] headerRows;
        ArrayList<String> SList = new ArrayList();
        boolean Strue = true;
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        int stCount = 0;
        int rowsPerPage1 = toRow - fromRow;
        if (rowsPerPage1 == 0) {
            rowsPerPage1 = 1;
        }
        int NumberofPagesPage1 = (int) Math.floor(tableBldr.facade.container.getRetObj().getViewSequence().size() / rowsPerPage1);
        if (fromRow == 0) {
            tableBldr.facade.container.setTotalNumberOfPages(NumberofPagesPage1);

        }
        int CurrentPageNumber1 = fromRow / rowsPerPage1;
        if (CurrentPageNumber1 > tableBldr.facade.container.getTotalNumberOfPages()) {
            CurrentPageNumber1 = tableBldr.facade.container.getTotalNumberOfPages();
        }
        if (CurrentPageNumber1 != 0) {
            int CurrentpageSartRowCount = 0;
            for (int s = 1; s <= CurrentPageNumber1; s++) {
                if (tableBldr.facade.container.getPageAndRowsMap().get(s) != null) {
                    CurrentpageSartRowCount = CurrentpageSartRowCount + tableBldr.facade.container.getPageAndRowsMap().get(s);
                }
            }
            tableBldr.facade.container.setCurrentpageSartRowCount(CurrentpageSartRowCount);
        } else {

            tableBldr.facade.container.setCurrentpageSartRowCount(0);
        }
        rowcountAfterSubtotalfilter = tableBldr.facade.container.getCurrentpageSartRowCount();
        if (isoneview != null && isoneview.equalsIgnoreCase("fromOneview")) {
//     toRow=colCount;
        } else {
            body.append("</thead>");
        }
        if (flag != null) {
            if (flag.equalsIgnoreCase("fromHeadline")) {
                body.append("<tbody>");
            } else {
                body.append("<tbody id=\"myTableBody\" style=\"overflow-x:hidden\">");
            }
        }

        //Code added  by govardhan  for countinng the total number of rows satisfied the subtotal filter condition hhhhhhh
        //modified by Dinanath for showing trendIcon
        if (tableBldr.facade.container.isToShowTrendIcon()) {
            if (!tableBldr.isTransposed()) {
                for (int fRow = 0; fRow < tableBldr.facade.container.getRetObj().getViewSequence().size(); fRow++) {
                    tableRow = (TableDataRow) tableBldr.getRowData(fRow);
                    if (!filterList.contains(tableRow.getRowData(0))) {
                        filterList.add(tableRow.getRowData(0));

                    }
                }
            }
        }

        if (ViewByCount > 1 && fromRow == 0 && tableBldr.getIsSubToTalSearchFilterApplied()) {

            int count = tableBldr.facade.container.getRetObj().getViewSequence().size();
            int rowsPerPage = toRow - fromRow;
            int NumberofPagesPage = (int) Math.floor(count / rowsPerPage);

            tableBldr.setIsCountingRowsAfterFilter(true);
            for (int rowCount = fromRow; rowCount < count; rowCount++) {

//                String keyVal1Temp = "";
                StringBuilder keyVal1Temp = new StringBuilder(1000);
                tableRowTempData = (TableDataRow) tableBldr.getRowData(rowCount);
                if (rowCount == 0 || rowLstTemp.isEmpty()) {
                    tableRowTemp1 = tableRowTempData;
                } else {

                    tableRowTemp1 = rowLstTemp.get(rowLstTemp.size() - 1);
                }
                for (int i = 0; i < ViewByCount; i++) {
//                    keyVal1Temp = keyVal1Temp + "~" + tableRowTemp1.getRowData(i);
                    keyVal1Temp.append("~").append(tableRowTemp1.getRowData(i));
                }
                tableBldr.subTotalKeyArray.add(keyVal1Temp.toString());

                if (tableRowTempData.printSubTotals() && tableRowTempData.isSubTotalFoundForFirstViewBy()) {
                    String tempkeyVal = "";

                    if (rowCount == 0 || rowLstTemp.isEmpty()) {
                        tableRowTemp1 = tableRowTempData;

                    } else {
                        tableRowTemp1 = rowLstTemp.get(rowLstTemp.size() - 1);

                    }
                    for (int i = 0; i < ViewByCount; i++) {
//                        keyVal = keyVal+"~"+tableRow.getRowData(i);
                        tempkeyVal = tempkeyVal + "~" + tableRowTemp1.getRowData(i);

                    }
                    tableBldr.setSubTotalkey(tempkeyVal);
                    ArrayList<String> displayColumns = tableBldr.facade.container.getDisplayColumns();
                    int index = displayColumns.indexOf(tableBldr.getSubTotalSrchColumns().get(0));

                    TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
//                 tableRowTempData.setSubtotalCount(subtotalRows.length);
//                ArrayList<String> Values=subtotalRows[0].getRowData(index);
                    ArrayList<String> conditions = tableBldr.getSubTotalSrchCondition();

                    ArrayList<String> SearchValue = tableBldr.getSubTotalSrchValue();
                    boolean isfiltered = true;
                    if (subtotalRows.length != 0) {
                        isfiltered = tableBldr.isFilteredSubTotalGroup(displayColumns, subtotalRows[0], tableBldr.getSubTotalSrchColumns(), SearchValue, conditions);
                    }
                    if (isfiltered) {
                        filteredRowCount = filteredRowCount + rowLstTemp.size();
//                      rowLstTemp.clear();

                    } else {

                        for (int x = rowLstTemp.size() - 1; x >= 0; x--) {
                            tableRowTemp2 = rowLstTemp.get(x);
                            if (tableRowTemp2.printSubTotals()) {
                                rowLstTemp.remove(x);
                                break;
                            } else {
                                rowLstTemp.remove(x);
                            }
                        }
                        filteredRowCount = filteredRowCount + rowLstTemp.size();
                    }
                    for (int lrows = 0; lrows < rowLstTemp.size(); lrows++) {

                        DistinctViewBys.add(rowLstTemp.get(lrows).getRowData(0));

                    }
                    rowLstTemp.clear();


                } else if (tableRowTempData.printSubTotals()) {

                    String tempkeyVal = "";

                    if (rowCount == 0 || rowLstTemp.isEmpty()) {
                        tableRowTemp1 = tableRowTempData;
                    } else {
                        tableRowTemp1 = rowLstTemp.get(rowLstTemp.size() - 1);
                    }
                    for (int i = 0; i < ViewByCount; i++) {
//                        keyVal = keyVal+"~"+tableRow.getRowData(i);
                        tempkeyVal = tempkeyVal + "~" + tableRowTemp1.getRowData(i);

                    }
                    tableBldr.setSubTotalkey(tempkeyVal);

                    ArrayList<String> displayColumns = tableBldr.facade.container.getDisplayColumns();
                    int index = displayColumns.indexOf(tableBldr.getSubTotalSrchColumns().get(0));
                    TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
//                    tableRowTempData.setSubtotalCount(subtotalRows.length);
//                ArrayList<String> Values=subtotalRows[0].getRowData(index);
                    ArrayList<String> conditions = tableBldr.getSubTotalSrchCondition();

                    ArrayList<String> SearchValue = tableBldr.getSubTotalSrchValue();
                    boolean isfiltered = true;
                    if (subtotalRows.length != 0) {
                        isfiltered = tableBldr.isFilteredSubTotalGroup(displayColumns, subtotalRows[0], tableBldr.getSubTotalSrchColumns(), SearchValue, conditions);
                    }
                    if (isfiltered) {
                    } else {

                        for (int x = rowLstTemp.size() - 1; x >= 0; x--) {
                            tableRowTemp2 = rowLstTemp.get(x);
                            if (tableRowTemp2.printSubTotals()) {
                                rowLstTemp.remove(x);
                                break;
                            } else {
                                rowLstTemp.remove(x);
                            }
                        }
                    }

                }
                rowLstTemp.add(tableRowTempData);
                if (rowCount != 0) {
                    if (rowCount % rowsPerPage == 0) {
                        int CurrentPageNumber = rowCount / rowsPerPage;
                        PageAndRowsMap.put(CurrentPageNumber, filteredRowCount);

                    }
                }
                if (rowCount == count - 1) {

                    PageAndRowsMap.put(NumberofPagesPage + 1, filteredRowCount);

                }

            }

            if (!rowLstTemp.isEmpty()) {
                String tempkeyVal2 = "";
                if (ViewByCount > 1) {
                    tableRowTemp1 = rowLstTemp.get(rowLstTemp.size() - 1);
                    for (int i = 0; i < ViewByCount; i++) {
                        tempkeyVal2 = tempkeyVal2 + "~" + tableRowTemp1.getRowData(i);

                    }
                    tableBldr.subTotalKeyArray.add(tempkeyVal2);
                    tableBldr.setSubTotalkey(tempkeyVal2);
                }

                ArrayList<String> displayColumns = tableBldr.facade.container.getDisplayColumns();
                int index = displayColumns.indexOf(tableBldr.getSubTotalSrchColumns().get(0));
                TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
                ArrayList<String> conditions = tableBldr.getSubTotalSrchCondition();
                TableSubtotalRow[] subTotalRow = tableBldr.getSubtotalRowDataLastRow();
                ArrayList<String> SearchValue = tableBldr.getSubTotalSrchValue();
                boolean isfiltered = true;
                if (subTotalRow.length != 0) {
                    isfiltered = tableBldr.isFilteredSubTotalGroup(displayColumns, subTotalRow[0], tableBldr.getSubTotalSrchColumns(), SearchValue, conditions);
                }
                if (isfiltered) {
                    filteredRowCount = filteredRowCount + rowLstTemp.size();
                } else {
                    for (int x = rowLstTemp.size() - 1; x >= 0; x--) {
                        tableRowTemp2 = rowLstTemp.get(x);
                        if (tableRowTemp2.printSubTotals()) {
                            rowLstTemp.remove(x);
                            break;
                        } else {
                            rowLstTemp.remove(x);
                        }
                    }
                    filteredRowCount = filteredRowCount + rowLstTemp.size();
                }

                for (int lrows = 0; lrows < rowLstTemp.size(); lrows++) {

                    DistinctViewBys.add(rowLstTemp.get(lrows).getRowData(0));

                }

            }

            tableBldr.facade.container.setDistinctViewBys(DistinctViewBys);
            tableBldr.facade.container.setTotalRowCountAfterSubFilter(filteredRowCount);//To give the number of rows displayed in the fron end
            //
            tableBldr.facade.container.setPageAndRowsMap(PageAndRowsMap);

        }

//         if(tableBldr.getIsSubToTalSearchFilterApplied() && (ViewByCount==2 || ViewByCount==3) ){
//        }
        //
        tableBldr.resetInitialProperties();

        tableBldr.subTotalKeyArray.clear();
        tableBldr.setIsCountingRowsAfterFilter(false);
        //Code by govardhan ended for countinng the total number of rows satisfied the filter condition
        int tBCount = 0;
        int iSTCount = 0;
        for (int row = fromRow; row < toRow; row++) {
            tableRow = (TableDataRow) tableBldr.getRowData(row);
            tBCount++;
            //start of code By Nazneen for sub total deviation
            String keyVal1 = "";
            if (tableRow.printSubTotals() && ((ViewByCount == 2) || ((!tableRow.isSubTotalFoundForFirstViewBy() && ViewByCount == 3))) && tableBldr.isIsSubtotalTopBottom()) {
                SubTotalListCount = SubTotalListCount + 1;
            } else if (tableRow.isSubTotalFoundForFirstViewBy() && tableBldr.isIsSubtotalTopBottom()) {
                SubTotalListCount = 0;
            }
            if (ViewByCount > 1) {

                if (row == 0 || rowLst.isEmpty() || (filteredcount == 0 && tableBldr.getIsSubToTalSearchFilterApplied() && ViewByCount != 3)) {
//                     if(row==0 || !tableRow.printSubTotals() && !tableRow.isSubTotalFoundForFirstViewBy()){
                    tableRowTemp = tableRow;
                } else {
                    tableRowTemp = rowLst.get(rowLst.size() - 1);
                }
                for (int i = 0; i < ViewByCount; i++) {
                    keyVal1 = keyVal1 + "~" + tableRowTemp.getRowData(i);
                }
                if (!tableBldr.isIsSubtotalTopBottom() || SubTotalListCount <= tableBldr.getSubTotalTopBottomCount()) {
                    tableBldr.subTotalKeyArray.add(keyVal1);
                }
            }
            //end of code by Nazneen for sub total deviation
            //&& (!tableBldr.isIsSubtotalTopBottom()||SubTotalListCount<=tableBldr.getSubTotalTopBottomCount())
            if (tableRow.printSubTotals() && tableRow.isSubTotalFoundForFirstViewBy()) {
                isFirstRowDeleted = false;
                stCount++;
                //start of code By Nazneen for sub total deviation
                String keyVal = "";
//                modified by Nazneen on 9 Jan 2014
//                if(ViewByCount>2){
                if (ViewByCount > 1 || rowLst.isEmpty()) {
                    if (row == 0 || rowLst.size() == 0 || (filteredcount == 0 && tableBldr.getIsSubToTalSearchFilterApplied())) {
//                     if(row==0 || !tableRow.printSubTotals() && !tableRow.isSubTotalFoundForFirstViewBy()){
                        tableRowTemp = tableRow;
                    } else {
                        tableRowTemp = rowLst.get(rowLst.size() - 1);
                    }
//                     modified by Nazneen on 9 Jan 2014
//                    for (int i=0;i<ViewByCount-2;i++){
                    for (int i = 0; i < ViewByCount; i++) {
//                        keyVal = keyVal+"~"+tableRow.getRowData(i);
                        keyVal = keyVal + "~" + tableRowTemp.getRowData(i);

                    }
                    tableBldr.setSubTotalkey(keyVal);
                }
                //end of code by Nazneen for sub total deviation
                if (!tableBldr.getIsSubToTalSearchFilterApplied()) {

                    TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
                    tableRow.setSubtotalCount(subtotalRows.length);
                    if (tableBldr.isIsSubtotalTopBottom() && iSTCount >= tableBldr.getSubTotalTopBottomCount()) {
                        if (subtotalRows.length >= 2) {
                            subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows[1]));
                        } else {
                            subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                        }
                    } else {
                        subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                    }
                    //this.addCellSpanToRows(rowLst); // by Amar
                    if (ViewByCount == 2 && tableBldr.isIsSubtotalTopBottom()) {

                        List sublist = new ArrayList();
                        // 
                        if (rowLst.size() < tableBldr.getSubTotalTopBottomCount()) {
                            sublist = new ArrayList<TableDataRow>(rowLst.subList(0, rowLst.size()));
                        } else {
                            sublist = new ArrayList<TableDataRow>(rowLst.subList(0, tableBldr.getSubTotalTopBottomCount()));
                        }
                        this.addCellSpanToRows((ArrayList<TableDataRow>) sublist);
//                    this.addCellSpanToRows((ArrayList<TableDataRow>) rowLst);
                        if (tableBldr.isIsSubtotalTopBottom()) {
                            subTotalRowCount = subTotalRowCount + sublist.size();
                            //                       subTotalRowCount = subTotalRowCount + rowLst.size();
                        }
                        // Code for top/bottom by Amar
                        if (stCount > tableBldr.getSubTotalTopBottomCount()) {
                            subTotalRowCount = subTotalRowCount - sublist.size();
                        } else {
                            body.append(this.generateHtmlForRows((ArrayList<TableDataRow>) sublist, subTotalRowLst));
                            //body.append(this.generateHtmlForRows((ArrayList<TableDataRow>) rowLst,subTotalRowLst));
                            if (stCount == tableBldr.getSubTotalTopBottomCount()) {
                                body.append(this.generateSubTotalHTML(subtotalRows));
                            }

                        }
                        // by Amar      //body.append(this.generateHtmlForRows((ArrayList<TableDataRow>) sublist,subTotalRowLst));
                    } else {
                        // (!||SubTotalListCount<=tableBldr.getSubTotalTopBottomCount())
//                    if(tableBldr.isIsSubtotalTopBottom() && stCount>tableBldr.getSubTotalTopBottomCount()){
//                        this.addCellSpanToRows(rowLst);
//                    }else{
                        // Code added for top/bottom by Amar
//                        if(tableBldr.isIsSubtotalTopBottom() && stCount==tableBldr.getSubTotalTopBottomCount()){
//                            //TableSubtotalRow[] subtotalRowsNew = tableBldr.getSubtotalRowData();
//                            //tableRow.setSubtotalCount(subtotalRows.length);
//                            if(tBCount>tableBldr.getSubTotalTopBottomCount()+1 && iSTCount < tableBldr.getSubTotalTopBottomCount()) {
//                                int extCnt = tBCount-(tableBldr.getSubTotalTopBottomCount()+1);
//                                for(int i=0;i<extCnt;i++){
//                                    rowLst.remove(rowLst.size()-1);
//                                }
//                                //Added by Amar
//                    }else{
//                                int extCnt = tBCount-1;
//                                for(int i=0;i<extCnt;i++){
//                                    rowLst.remove(rowLst.size()-1);
//                                }
//                    }
//                            this.addCellSpanToRows(rowLst);
//                           // subTotalRowCount = subTotalRowCount + rowLst.size();
//                body.append(this.generateHtmlForRows(rowLst,subTotalRowLst));
//                            if (subtotalRows != null) {
//                                if (tableBldr.isIsSubtotalTopBottom()) {
//                                    if (iSTCount < tableBldr.getSubTotalTopBottomCount()) {
//                                        body.append(this.generateSubTotalHTML(subtotalRows));
//                                    }else if(stCount==tableBldr.getSubTotalTopBottomCount()){
//                                        body.append(this.generateSubTotalHTML(subTotalRowLst.toArray(new TableSubtotalRow[0])));
//                                    }
//                                }else
//                                    body.append(this.generateSubTotalHTML(subtotalRows));
//                            }
//                            // end of code
//                        }else{
                        if (tableBldr.isIsSubtotalTopBottom()) {
                            if (tBCount > tableBldr.getSubTotalTopBottomCount() + 1) {
                                if (iSTCount < tableBldr.getSubTotalTopBottomCount()) {
                                    int extCnt = tBCount - (tableBldr.getSubTotalTopBottomCount() + 1);
                                    for (int i = 0; i < extCnt; i++) {
                                        rowLst.remove(rowLst.size() - 1);
                                    }
                                } else {
                                    int extCnt = tBCount - 1;
                                    for (int i = 0; i < extCnt; i++) {
                                        rowLst.remove(rowLst.size() - 1);
                                    }
                                }
                            } else {
                                if (iSTCount >= tableBldr.getSubTotalTopBottomCount()) {
                                    int extCnt = tBCount - 1;
                                    for (int i = 0; i < extCnt; i++) {
                                        rowLst.remove(rowLst.size() - 1);
                                    }
                                }
                            }
                        }
//                            if(tableBldr.isIsSubtotalTopBottom()){
//                                //                      subTotalRowCount = subTotalRowCount + sublist.size();
//                                subTotalRowCount = subTotalRowCount + rowLst.size();
//                            }
                        this.addCellSpanToRows(rowLst);
                        body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));
                        //}
                        //}
                        tBCount = 1;
                    }

                    //body.append(generateSubTotalHTML(subtotalRows));
                    rowLst.clear();
                } else {              //Code for SubTotal search filtering By Govardhan
                    ArrayList<String> displayColumns = tableBldr.facade.container.getDisplayColumns();
                    int index = displayColumns.indexOf(tableBldr.getSubTotalSrchColumns().get(0));
                    if (!rowLst.isEmpty()) {
                        tableBldr.setFirstRowAfterSubTtlFiltr(rowLst.get(0).getID(0));
                    }

                    TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
                    tableRow.setSubtotalCount(subtotalRows.length);
//                ArrayList<String> Values=subtotalRows[0].getRowData(index);
                    ArrayList<String> conditions = tableBldr.getSubTotalSrchCondition();

                    ArrayList<String> SearchValue = tableBldr.getSubTotalSrchValue();
                    boolean isfiltered = tableBldr.isFilteredSubTotalGroup(displayColumns, subtotalRows[0], tableBldr.getSubTotalSrchColumns(), SearchValue, conditions);
                    if (ViewByCount != 3) {
                        if (isfiltered) {

                            subTotalRowCount = subTotalRowCount + rowLst.size();
                            subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                            this.addCellSpanToRows(rowLst);
                            body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));
                            filteredcount = filteredcount + 1;
                        }

                    } else {
                        if (isfiltered) {

                            subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                            this.addCellSpanToRows(rowLst);
                            filteredcount = filteredcount + 1;
                        } else {
                            for (int x = rowLst.size() - 1; x >= 0; x--) {
                                tableRowTemp2 = rowLst.get(x);
                                if (tableRowTemp2.printSubTotals()) {
                                    rowLst.remove(x);
                                    break;
                                } else {
                                    rowLst.remove(x);
                                }
                            }
//                     if( rowLst.isEmpty()){
//                     tableRow.setSubtotalCount(subtotalRows.length+1);
//                     }
                            if (subtotalRows.length == 2 && !rowLst.isEmpty()) {
                                subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows[1]));
                            }
                            this.addCellSpanToRows(rowLst);
                        }

                        if (!rowLst.isEmpty() || ViewByCount != 3) {
//                   if(RunningTtlOfSubmittedlist>0){
                            RunningTtlOfSubmittedlist = RunningTtlOfSubmittedlist + 1;
                            body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));
                            if (!rowLst.isEmpty() && ViewByCount == 3) {
                                filteredcount = filteredcount + 1;
                            }
//                      }

                        }
                    }

                    rowLst.clear();
                }
                //end of code by Govardhan
                //subTotalRowLst.clear();
                iSTCount = 0;
            } //            else if ( tableRow.printSubTotals() && (!tableBldr.isIsSubtotalTopBottom()||SubTotalListCount<=tableBldr.getSubTotalTopBottomCount()))
            else if (tableRow.printSubTotals()) //if( tableRow.printSubTotals() && (!tableBldr.isIsSubtotalTopBottom()||SubTotalListCount<=tableBldr.getSubTotalTopBottomCount()))
            {
                //start of code By Nazneen
                iSTCount++;
                String keyVal = "";
//                modified by Nazneen on 9 Jan 2014
//                if(ViewByCount>2){
                if (ViewByCount > 1) {
                    if (tableBldr.isIsSubtotalTopBottom()) {
                        if (iSTCount <= tableBldr.getSubTotalTopBottomCount()) {
                            if (row == 0 || rowLst.isEmpty()) {
//                     if(row==0 || !tableRow.printSubTotals() && !tableRow.isSubTotalFoundForFirstViewBy()){
                                tableRowTemp = tableRow;
                            } else {
                                tableRowTemp = rowLst.get(rowLst.size() - 1);
                            }
//                     modified by Nazneen on 9 Jan 2014
//                    for (int i=0;i<ViewByCount-2;i++){
                            for (int i = 0; i < ViewByCount; i++) {
//                        keyVal = keyVal+"~"+tableRow.getRowData(i);
                                keyVal = keyVal + "~" + tableRowTemp.getRowData(i);

                            }
                            tableBldr.setSubTotalkey(keyVal);
                        }
                    } else {
                        if (row == 0 || rowLst.isEmpty()) {
//                     if(row==0 || !tableRow.printSubTotals() && !tableRow.isSubTotalFoundForFirstViewBy()){
                            tableRowTemp = tableRow;
                        } else {
                            tableRowTemp = rowLst.get(rowLst.size() - 1);
                        }
//                     modified by Nazneen on 9 Jan 2014
//                    for (int i=0;i<ViewByCount-2;i++){
                        for (int i = 0; i < ViewByCount; i++) {
//                        keyVal = keyVal+"~"+tableRow.getRowData(i);
                            keyVal = keyVal + "~" + tableRowTemp.getRowData(i);

                        }
                        tableBldr.setSubTotalkey(keyVal);
                    }
                }
                //end of code by Nazneen
                if (!tableBldr.getIsSubToTalSearchFilterApplied()) {
                    TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
                    if (tableBldr.isIsSubtotalTopBottom()) {
                        if (iSTCount <= tableBldr.getSubTotalTopBottomCount()) {
                            tableRow.setSubtotalCount(subtotalRows.length);
                            this.addCellSpanToRows(rowLst);
                            subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                        } else {
                        }

                    } else {
                        tableRow.setSubtotalCount(subtotalRows.length);
                        this.addCellSpanToRows(rowLst);
                        subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));

                    }
                    if (tableBldr.isIsSubtotalTopBottom()) {
                        if (tBCount > tableBldr.getSubTotalTopBottomCount() + 1 && iSTCount <= tableBldr.getSubTotalTopBottomCount()) {
                            int extCnt = tBCount - (tableBldr.getSubTotalTopBottomCount() + 1);
                            for (int i = 0; i < extCnt; i++) {
                                rowLst.remove(rowLst.size() - 1);
                            }

                        } else if (tBCount < tableBldr.getSubTotalTopBottomCount() + 1 && iSTCount <= tableBldr.getSubTotalTopBottomCount()) {
                        } else {
                            int extCnt = tBCount - 1;//-(tableBldr.getSubTotalTopBottomCount()+1);
                            for (int i = 0; i < extCnt; i++) {
                                rowLst.remove(rowLst.size() - 1);
                            }

                        }
                        tBCount = 1;
                    }
                    //this.addCellSpanToRows(rowLst);
                } else {//code added by govardhan for subtotal filtering
                    ArrayList<String> displayColumns = tableBldr.facade.container.getDisplayColumns();
                    int index = displayColumns.indexOf(tableBldr.getSubTotalSrchColumns().get(0));
                    TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
                    tableRow.setSubtotalCount(subtotalRows.length);
//                ArrayList<String> Values=subtotalRows[0].getRowData(index);
                    ArrayList<String> conditions = tableBldr.getSubTotalSrchCondition();

                    ArrayList<String> SearchValue = tableBldr.getSubTotalSrchValue();
                    boolean isfiltered = tableBldr.isFilteredSubTotalGroup(displayColumns, subtotalRows[0], tableBldr.getSubTotalSrchColumns(), SearchValue, conditions);
                    if (isfiltered) {

                        subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                        this.addCellSpanToRows(rowLst);

                    } else {

                        for (int x = rowLst.size() - 1; x >= 0; x--) {
                            tableRowTemp2 = rowLst.get(x);
                            if (tableRowTemp2.printSubTotals()) {
                                if (tableRowTemp2.isSubTotalFoundForFirstViewBy()) {
                                    isFirstRowDeleted = true;
                                    FirstrowCount = Integer.toString(tableRowTemp2.getRowNumber());


                                }
                                rowLst.remove(x);

                                break;
                            } else {
                                rowLst.remove(x);
                            }
                        }
                        if (rowLst.isEmpty()) {

                            tableRow.setSubtotalCount(subtotalRows.length + 1);
                        }
                        this.addCellSpanToRows(rowLst);

                    }

                }//code by Govardhan ended for subtotal filtering
            } else if (tableRow.printSubTotals() && tableRow.isSubTotalFoundForFirstViewBy() && (ViewByCount == 2 || ViewByCount == 3) && tableBldr.isIsSubtotalTopBottom() && SubTotalListCount == tableBldr.getSubTotalTopBottomCount() + 1) {

                isfilterLastRow = true;
                String keyVal = "";
//                modified by Nazneen on 9 Jan 2014
//                if(ViewByCount>2){
                if (ViewByCount > 1 || rowLst.isEmpty()) {
                    if (row == 0) {
//                     if(row==0 || !tableRow.printSubTotals() && !tableRow.isSubTotalFoundForFirstViewBy()){
                        tableRowTemp = tableRow;
                    } else {
                        tableRowTemp = rowLst.get(rowLst.size() - 1);
                    }
                    for (int i = 0; i < ViewByCount; i++) {
                        keyVal = keyVal + "~" + tableRowTemp.getRowData(i);
                    }
                    tableBldr.setSubTotalkey(keyVal);
                }
                //end of code by Nazneen for sub total deviation

                TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
                tableRow.setSubtotalCount(subtotalRows.length);
                subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                this.addCellSpanToRows(rowLst);
                body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));

                rowLst.clear();
                rowLst.add(tableRow);
            }
//            if(Strue){
            //if(!tableBldr.isIsSubtotalTopBottom()||SubTotalListCount<=tableBldr.getSubTotalTopBottomCount()){
            rowLst.add(tableRow);
//            }
            batchCount++;
            //}
            if (batchCount >= TableDisplay.BATCH_SIZE && !tableBldr.facade.container.getNetTotalReq()) {
                batchCount = 0;
                this.addCellSpanToRows(rowLst);
                body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));
                if (this.getHeadlineflag() != null && this.getHeadlineflag().equalsIgnoreCase("Export")) {
                } else {
                    this.flushToOutputStream(super.parentHtml.append(body));
                }
                body = new StringBuilder();
                super.parentHtml = new StringBuilder();
                rowLst.clear();
                subTotalRowLst.clear();
            }
        }

        if (!rowLst.isEmpty()) {
            //start of code By Nazneen for sub total deviation
            String keyVal = "";
//                modified by Nazneen on 9 Jan 2014
//                if(ViewByCount>2){

            if (ViewByCount > 1) {
                tableRowTemp = rowLst.get(rowLst.size() - 1);
//                    modified by Nazneen on 9 Jan 2014
//                    for (int i=0;i<ViewByCount-2;i++){
                for (int i = 0; i < ViewByCount; i++) {
//                        keyVal = keyVal+"~"+tableRow.getRowData(i);
                    keyVal = keyVal + "~" + tableRowTemp.getRowData(i);

                }
                tableBldr.subTotalKeyArray.add(keyVal);
                tableBldr.setSubTotalkey(keyVal);
            }

            //end of code by Nazneen for sub total deviation
            if (!tableBldr.getIsSubToTalSearchFilterApplied()) {
                TableSubtotalRow[] subTotalRow = tableBldr.getSubtotalRowDataLastRow();
                //this.addCellSpanToRows(rowLst);
                if (isoneview != null && isoneview.equalsIgnoreCase("fromOneview")) {
                    this.addCellSpanToRows(rowLst);
                    body.append(this.generateHtmlForRows1(rowLst, subTotalRowLst));
                } else {
                    if (ViewByCount == 2 && tableBldr.isIsSubtotalTopBottom()) {
                        this.addCellSpanToRows(rowLst);
                        List sublist = new ArrayList();
                        // 
                        if (rowLst.size() < tableBldr.getSubTotalTopBottomCount()) {

                            //                   code modified for subtotal top/bottom by Amar
                            sublist = new ArrayList<TableDataRow>(rowLst.subList(0, rowLst.size()));
                        } else {
                            sublist = new ArrayList<TableDataRow>(rowLst.subList(0, tableBldr.getSubTotalTopBottomCount()));
                        }
                        this.addCellSpanToRows((ArrayList<TableDataRow>) sublist);
//                     this.addCellSpanToRows((ArrayList<TableDataRow>)  rowLst);
                        if (tableBldr.isIsSubtotalTopBottom() && stCount > tableBldr.getSubTotalTopBottomCount()) {
                        } else {
                            body.append(this.generateHtmlForRows((ArrayList<TableDataRow>) sublist, subTotalRowLst));
                        }
                        //body.append(this.generateHtmlForRows((ArrayList<TableDataRow>) rowLst,subTotalRowLst));
                    } else {

                        // (!||SubTotalListCount<=tableBldr.getSubTotalTopBottomCount())
//               if(tableBldr.isIsSubtotalTopBottom() && stCount>tableBldr.getSubTotalTopBottomCount()){
//                   this.addCellSpanToRows(rowLst);
//               }else{
                        if (tableBldr.isIsSubtotalTopBottom()) {
                            if (tBCount > tableBldr.getSubTotalTopBottomCount()) {
                                int extCnt = tBCount - (tableBldr.getSubTotalTopBottomCount());
                                for (int i = 0; i < extCnt; i++) {
                                    rowLst.remove(rowLst.size() - 1);
                                }
                                // this.addCellSpanToRows(rowLst);
                            }
                            tBCount = 1;
                        }
//                   if(tableBldr.isIsSubtotalTopBottom()){
//                       //                       subTotalRowCount = subTotalRowCount + sublist.size();
//                       subTotalRowCount = subTotalRowCount + rowLst.size();
//                   }
                        this.addCellSpanToRows(rowLst);
                        // end of code
                        body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));
                        //}
                    }
                }

                if (subTotalRow != null) {
                    // Added by Amar for top/bottom
//                         if(tableBldr.isIsSubtotalTopBottom() && stCount>tableBldr.getSubTotalTopBottomCount()){
//                         }else  // end of code
                    body.append(this.generateSubTotalHTML(subTotalRow));
                }
                rowLst.clear();
            } else {
                //Code if applied for SubTotal Search Filtering By Govardhan
                ArrayList<String> displayColumns = tableBldr.facade.container.getDisplayColumns();
                int index = displayColumns.indexOf(tableBldr.getSubTotalSrchColumns().get(0));
                tableBldr.setFirstRowAfterSubTtlFiltr(rowLst.get(0).getID(0));
                TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
//                 tableRow.setSubtotalCount(subtotalRows.length);
//                ArrayList<String> Values=subtotalRows[0].getRowData(index);
                ArrayList<String> conditions = tableBldr.getSubTotalSrchCondition();
                TableSubtotalRow[] subTotalRow = tableBldr.getSubtotalRowDataLastRow();
                ArrayList<String> SearchValue = tableBldr.getSubTotalSrchValue();
//                
                boolean isfiltered = true;
                if (subTotalRow.length != 0) {
                    isfiltered = tableBldr.isFilteredSubTotalGroup(displayColumns, subTotalRow[0], tableBldr.getSubTotalSrchColumns(), SearchValue, conditions);
                }
                if (isfiltered) {
                    if (ViewByCount != 3) {
                        subTotalRowCount = subTotalRowCount + rowLst.size();
                    }
                    subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                    this.addCellSpanToRows(rowLst);
//                  isfilterLastRow=true;
                    tableBldr.TableDataRowListSubFiltered.addAll(rowLst);
                    body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));
                    filteredcount = filteredcount + 1;
                    if (subTotalRow != null && subTotalRow.length >= 2) {
                        subTotalRow[1] = tableBldr.UpdatePageTotalRow(subTotalRow[1], tableBldr.filteredSubtotalTblRowsList, ActualRowsFrSbTtl, rowLst);
                    }
                    // subtotalRows[1] = tableBldr.UpdatePageTotalRow(subtotalRows[1], tableBldr.filteredSubtotalTblRowsList,ActualRowsFrSbTtl,rowLst);
                    body.append(this.generateSubTotalHTML(subTotalRow));
                } else {
                    if (ViewByCount == 2) {
                        subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subtotalRows));
                        this.addCellSpanToRows(rowLst);
                        isfilterLastRow = true;
                        // body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));
                    } else {
                        for (int x = rowLst.size() - 1; x >= 0; x--) {
                            tableRowTemp2 = rowLst.get(x);
                            if (tableRowTemp2.printSubTotals()) {
                                rowLst.remove(x);
                                break;
                            } else {
                                rowLst.remove(x);
                            }
                        }
                        if (subTotalRow.length == 2 && !rowLst.isEmpty()) {
                            subTotalRowLst.addAll(Arrays.<TableSubtotalRow>asList(subTotalRow[1]));
                        }
                        this.addCellSpanToRows(rowLst);
                        if (!rowLst.isEmpty()) {
                            tableBldr.TableDataRowListSubFiltered.addAll(rowLst);
                            body.append(this.generateHtmlForRows(rowLst, subTotalRowLst));
                        }
                        ArrayList<TableDataRow> rowLst1 = new ArrayList<TableDataRow>();
                        if (tableRowTemp2 != null) {
                            rowLst1.add(tableRowTemp2);
                            isfilterLastRow = true;
                            tableBldr.lastRow = true;
                            tableBldr.TableDataRowListSubFiltered.addAll(rowLst1);
                            body.append(this.generateHtmlForRows(rowLst1, subTotalRowLst));
                        }
                        TableSubtotalRow[] Strow = new TableSubtotalRow[1];
                        subTotalRow[1] = tableBldr.UpdatePageTotalRow(subTotalRow[1], tableBldr.filteredSubtotalTblRowsList, ActualRowsFrSbTtl, rowLst);

                        Strow[0] = subTotalRow[1];
                        if (!rowLst.isEmpty()) {
                            body.append(this.generateSubTotalHTML(Strow));
                        }
                    }
                }
                rowLst.clear();
            }//ended Code By Govardhan.P
        }
        if ((tableBldr.getIsSubToTalSearchFilterApplied() || tableBldr.isIsSubtotalTopBottom()) && (ViewByCount == 2 || ViewByCount == 3)) {

            tableBldr.facade.container.setSubTotalRowCount(subTotalRowCount);//To give the number of rows displayed in the fron end
            int rowsPerPage = toRow - fromRow;
            int NumberofPagesPage = (int) Math.floor(tableBldr.facade.container.getRetObj().getViewSequence().size() / rowsPerPage);
            int CurrentPageNumber = fromRow / rowsPerPage + 1;
            tableBldr.facade.container.updatePageAndRowsMap(CurrentPageNumber, subTotalRowCount);
            // 
//                  PageAndRowsMap.put(CurrentPageNumber,filteredRowCount);
        }

        return super.parentHtml.append(body);
    }

    protected StringBuilder generateSubTotalHTML(TableSubtotalRow[] subTotalRow) {
        TableSubtotalDisplay subTotalDisplay = new TableSubtotalDisplay(tableBldr);
        return subTotalDisplay.generateSubTotalHtml(subTotalRow);
    }

    protected void addCellSpanToRows(ArrayList<TableDataRow> rows) {
        Set<TableCellSpan> cellSpanSet = tableBldr.getCellSpans();
        Set<TableCellSpan> cellSpanSetToRemove = new HashSet<TableCellSpan>();
        int nm = 0;
        HashSet<TableCellSpan> cellSpanSetToAdd;
        for (int x = 0; x < rows.size(); x++) {
            TableDataRow rw = rows.get(x);
            if (rw.printSubTotals() && !rw.isSubTotalFoundForFirstViewBy() && !tableBldr.getFirstRowAfterSubTtlFiltr().equalsIgnoreCase(rw.getID(0))) {
                nm = nm + 1;
            }
        }

        int dimColCount = tableBldr.getViewByCount() - 1;
        if (!cellSpanSet.isEmpty()) {
            for (TableDataRow row : rows) {
                cellSpanSetToAdd = new HashSet<TableCellSpan>();
                for (int column = 0; column < dimColCount; column++) {
                    String rowID = "";
                    if (isFirstRowDeleted) {
                        rowID = row.getID(column);

                        //rowID=rowID.replace(Integer.toString(row.getRowNumber()),FirstrowCount);
                    } else {
                        rowID = row.getID(column);
                    }
                    Iterator<TableCellSpan> spanSet = Iterables.filter(cellSpanSet, TableCellSpan.getCellIdPredicate(rowID)).iterator();
                    if (spanSet.hasNext()) {
                        TableCellSpan span;

                        span = spanSet.next();
                        if (row == rows.get(0) && column == 0 && (tableBldr.isIsSubtotalTopBottom() || tableBldr.getIsSubToTalSearchFilterApplied())) {
                            if (tableBldr.getViewByCount() == 3) {
                                span.setRowSpan(rows.size() + nm + 1);
                            } else {
                                span.setRowSpan(rows.size());
                            }

                        }
                        cellSpanSetToAdd.add(span);
                        cellSpanSetToRemove.add(span);
                    }
                }
                for (TableCellSpan span : cellSpanSetToAdd) {
                    row.addCellSpan(span);
                }
            }
            for (TableCellSpan cellSpan : cellSpanSetToRemove) {
                tableBldr.removeCellSpanFromSet(cellSpan);
            }
        }
    }

    protected void flushToOutputStream(StringBuilder body) {
        try {
            //
            out.write(body.toString()); //.toString());
            out.flush();
            //out.clearBuffer();
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
    }

    protected StringBuilder generateHtmlForRows(ArrayList<TableDataRow> rowLst, ArrayList<TableSubtotalRow> subtotalRowLst) {
        //Added By Ram
//        ReportTemplateDAO rto=new ReportTemplateDAO();
//        PbReturnObject retObj = null;
//        String lookupViewBys=tableBldr.facade.container.getLookupViewBys();
//        retObj=rto.getLookupData(lookupViewBys);
//        HashMap lookupdata=new HashMap();
//        if(retObj !=null){
//        for(int i=0;i<retObj.getRowCount();i++)
//        {
//            lookupdata.put(retObj.getFieldValueString(i, 0), retObj.getFieldValueString(i, 1));
//        }
//        }
        HashMap lookupdata = new HashMap();
        lookupdata = tableBldr.facade.container.getFilterLookupOriginalToNew();
        //Ended by Ram


        StringBuilder body = new StringBuilder();
        StringBuilder tempBody = new StringBuilder();
        String cssClass = "";
        int colCount = tableBldr.getColumnCount();
        String displayStyle;
        String data;
        String id;
        String bgColor = "";
        String href;
        String textColor = "";
//        String spaceAftrNo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        String spaceAftrNo = "";
        String imagePath = null;
        Boolean editable = false;
        int from = 0;
        int subTotalCount = 0;
        int usedUpSubtotals = 0;
        int rowId = 0;
        List<String> transposeFormatList = null;
        int rowCount = tableBldr.getFromRow();
        String reportDrillUrl = null;
        String selfReportDrillUrl = null;
        boolean flag = true;
        if (tableBldr.isSplitby()) {
            colCount += tableBldr.getInceresedColcount();
        }
        if (!rowLst.isEmpty()) {
            firstRowId = rowLst.get(0).getRowNumber();
        }

        TableSubtotalRow[] subTotalRow = null;

        //modified by anitha for grandtotal in transpose
        if (tableBldr.isTransposed()) {
            try {
                subTotalRow = tableBldr.getGrandtotalRowData();
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }

// added by krishan pratap

        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int countRecords = 0;
        //String heading;
        ArrayList<String> heading = new ArrayList<String>();
        ArrayList<String> headingid = new ArrayList<String>();
        ArrayList<String> name = new ArrayList<String>();
        int viewcount = tableBldr.getViewByCount();
        HashMap<String, String> rowviewbymap = new HashMap();
        HashMap<String, String> crosstabMeasureId = null;
        ArrayList measureIdsList = tableBldr.facade.container.getTableDisplayMeasures();
        ArrayList<String> MeasureNames = tableBldr.facade.container.getReportMeasureNames();
        ArrayList<String> name3 = new ArrayList<String>();
        HashMap<String, String> meas = new HashMap();
//added by krishan pratap
        if (!tableBldr.isCrossTab()) {
            for (TableHeaderRow headerRow : headerRows) {

                for (int i = tableBldr.getFromColumn(); i < viewcount; i++) {
                    heading.add(headerRow.getRowData(countRecords));
                    countRecords++;
                }
            }


        } else {
            crosstabMeasureId = ((PbReturnObject) tableBldr.facade.container.getRetObj()).crosstabMeasureId;
            ArrayList<String> row = tableBldr.facade.container.getReportCollect().reportRowViewbyValues;
            ArrayList columnviewby = tableBldr.facade.container.getViewByColNames();
            for (int i = 0; i < row.size(); i++) {
                rowviewbymap.put(row.get(i), (String) columnviewby.get(i));
            }

        }
        for (int i = 0; i < measureIdsList.size(); i++) {
            meas.put((String) measureIdsList.get(i), MeasureNames.get(i));
            String measureIdsList1 = (String) measureIdsList.get(i);
            // String value=tableBldr.facade.container.getreportDrillMaptooltip(measureIdsList1);
            //

            String measureIdsList2 = measureIdsList1.replace("A_", "");

            name3.add(measureIdsList2);

        }

        //ended by krishan pratap
        for (TableDataRow tableRow : rowLst) {
//            flag = true;
            if (tableBldr.isTransposed()) {
                transposeFormatList = tableBldr.getTransposeFormatMap().get("rowId" + rowId);
            }
//          commented by Nazneen
//          subTotalCount = generateSubtotal(tableRow, subtotalRowLst, from, body);
            if (!tableBldr.getIsSubToTalSearchFilterApplied() || filteredcount != 0) {
                if (!tableBldr.getIsSubToTalSearchFilterApplied() || tableBldr.getViewByCount() != 3 || RunningTtlOfSubmittedlist > 1) {
                    subTotalCount = generateSubtotal(tableRow, subtotalRowLst, from, body);
                }
            }
            RunningTtlOfSubmittedlist = RunningTtlOfSubmittedlist + 1;
            filteredcount = filteredcount + 1;

            if (!isfilterLastRow) {

                if (tableBldr.getViewByCount() == 3) {
                    subTotalRowCount = subTotalRowCount + 1;
                }

//            subTotalCount = generateSubtotal(tableRow, subtotalRowLst, from, tempBody);
                if (subTotalCount != 0) {
                    usedUpSubtotals += subTotalCount;
                    from += subTotalCount;
                }
//            Modified by Faiz Ansari for Table Viewbys click Menu
//            body.append("<Tr onMouseOut=\"parent.mouseOut(this)\" onMouseOver=\"parent.mouseOn(this)\" id=\"rowId"+rowId+"\">");
                body.append("<Tr id=\"rowId" + rowId + "\">");
                if (tableBldr.isDrillAcrossSupported()) {
                    body.append("<td width=\"5\" align='center'><input name='drillAcross' type='checkbox' id=\"ID_").append(tableRow.getDrillAcrossData()).append("\" onclick=\"javascript:addDrillAcrossVal('" + tableRow.getDrillAcrossData() + "','ID_" + tableRow.getDrillAcrossData() + "')\" value='").append(tableRow.getDrillAcrossData()).append("'></td>");
                }
//              if(tableBldr.isTransposed())
//               {
//                   body.append("<td><a href=\"javascript:setRowAttribute('"+rowId);
//                   if(transposeFormatList!=null)
//                       body.append("','").append(transposeFormatList.get(0)).append("','").append(transposeFormatList.get(1));
//                       body.append("')\" class=\"ui-icon ui-icon-pencil\" title=\"Edit\"></a></td>");
//
//                }
                //  
                if (!tableBldr.getAdhocDrillType().equalsIgnoreCase("none") && !tableBldr.isTransposed()) {
                    // body.append("<td width=\"5\"><a class=\" ui-icon ui-icon-gear\" title=\"Enable Adhoc Drill\"></a></td>");
//            commented  by manik for removal of + adhoc drill sign
//             body.append("<td width=\"5\">");
//             body.append("<ul class=\"dropDownMenu\"><li><span class=\"ui-icon ui-icon-plusthick\"  title=\"Enable Adhoc Drill\" ></span><ul>");
//             if(tableRow.getReportParameters()!=null){
//                 if(!tableRow.getColumnId(tableRow.getViewbyCount()-1).contains("TIME")){
//                   tableRow.getReportParameters().remove("Time Drill");
//                   tableRow.getReportParameterNames().remove("Time Drill");
//                 }
//                     for(int i=0;i<tableRow.getReportParameters().size();i++){
//                       //  
//                        if(!tableRow.getReportParameters().get(i).equalsIgnoreCase(tableRow.getColumnId(0).replace("A_",""))){
//                            body.append("<li><a onclick=\"javascript:viewAdhocDrill('");
//                            if(tableBldr.getAdhocDrillType().equalsIgnoreCase("drillside"))
//                            body.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getReportParameters().get(i)).append("','").append(tableRow.getRowData(tableRow.getViewbyCount()-1)).append("','").append(tableRow.getViewbyId()).append("','").append(tableRow.getColumnId(tableRow.getViewbyCount()-1)).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(tableRow.getAdhocUrl()).append("','").append(tableRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(tableRow.getReportParameterNames().get(i)).append("</a></li>");
//                            else
//                            body.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getReportParameters().get(i)).append("','").append(tableRow.getRowData(tableRow.getViewbyCount()-1)).append("','").append(tableRow.getViewbyId()).append("','").append(tableRow.getColumnId(tableRow.getViewbyCount()-1)).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(tableRow.getAdhocTimeUrl()).append("','").append(tableRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(tableRow.getReportParameterNames().get(i)).append("</a></li>");
//                         }//if
//                     }//for
//             }
//             body.append("</ul></li></ul></td>");
                }
                if (tableBldr.isSerialNumDisplay()) {
//                   if (!tableBldr.getIsSubToTalSearchFilterApplied()) {
//                 body.append("<td width=\"5\">").append(RowCount+1).append("</td>");//bcoz rowcount starts from 0 i.e increased to 1
//                }else{
//                  body.append("<td width=\"5\">").append(rowcountAfterSubtotalfilter+1).append("</td>");//bcoz rowcount starts from 0 i.e increased to 1
//
//                }
//
//                   if (!tableBldr.getIsSubToTalSearchFilterApplied()) {
//                 body.append("<td width=\"5\">").append("</td>");//bcoz rowcount starts from 0 i.e increased to 1
//                }else{
//                  body.append("<td width=\"5\">").append("</td>");//bcoz rowcount starts from 0 i.e increased to 1
//
//                }
                }
                String dimData = "";
                String dimData1 = "";
                String dimFilter = "";
                String viewname = "";
                String title = "";
                String title1 = "";
                String dateTooltip = "";
                String RTComparisionwith = "";
                String prevtooltipvalue = "";
                String prevmeasname = "";
                int ViewByCount = 0;
                ViewByCount = tableBldr.getViewByCount();
                for (int col = tableBldr.getFromColumn(); col < colCount; col++) {

                    //added by Nazneen
//                TableSubtotalRow[] subTotalRow = tableBldr.getSubtotalRowDataLastRow();
//                TableSubtotalDisplay subTotalDisplay = new TableSubtotalDisplay(tableBldr);
//                 TableSubtotalRow row1;
//                for ( int i=0; i<subTotalRow.length; i++ ){
//
//                    row = subTotalRow[i];
//                    colCount = row.getColumnCount();
//                }

                    data = tableRow.getRowData(col);

                    //Added By Ram

                    if (lookupdata.containsKey(data)) {

                        data = (String) lookupdata.get(data);

                    }
                    //End by Ram
                    HashMap<String, ArrayList<String>> fontHeaderMaps = null; //added by sruthi for tablecolumn pro
                    ArrayList fontheaderpro = null; //added by sruthi for tablecolumn pro
                    String headersize = ""; //added by sruthi for tablecolumn pro
                    int size = 12; //added by sruthi for tablecolumn pro
                    String[] colid = tableRow.getID(col).split("_");
                    String elementid = "";
                    if (tableBldr.facade.container.isReportCrosstab()) {
                        elementid = colid[0];
                    } else {
                        elementid = colid[1];
                    }
                    //   data = tableRow.getRowData(col);
                    fontHeaderMaps = (tableBldr.facade.container.getTableColumnProperties() == null) ? new HashMap() : tableBldr.facade.container.getTableColumnProperties();
                    if (tableBldr.facade.container.isReportCrosstab()) {
                        if (fontHeaderMaps != null && !fontHeaderMaps.isEmpty()) {
                            if (fontHeaderMaps.containsKey(elementid)) {
                                fontheaderpro = fontHeaderMaps.get(elementid);
                                headersize = fontheaderpro.get(0).toString();
                                size = Integer.parseInt(headersize.trim());
                            }
                        }
                    } else {
                        if (fontHeaderMaps != null && !fontHeaderMaps.isEmpty()) {
                            if (fontHeaderMaps.containsKey("A_" + elementid)) {
                                fontheaderpro = fontHeaderMaps.get("A_" + elementid);
                                headersize = fontheaderpro.get(0).toString();
                                size = Integer.parseInt(headersize.trim());
                            }
                        }
                    }//ended by sruthi
                    if (ViewByCount == 3 && col == 0 && tableBldr.getIsSubToTalSearchFilterApplied() && tableBldr.getFirstRowAfterSubTtlFiltr().equalsIgnoreCase(tableRow.getID(0))) {
                        displayStyle = "";
                    } else {
                        displayStyle = tableRow.getDisplayStyle(col);
                    }

                    id = tableRow.getID(col);
                    cssClass = tableRow.getCssClass(col);
                    editable = tableRow.isEditable(col);
                    imagePath = tableRow.getImagePath(col);

                    //start of code by Nazneen for sub total deviation
//                if(col<=ViewByCount-2){
//                if(col<=ViewByCount-1){
//                   dimData = dimData+"~"+data;
//                   //started by Mayank on 11 Nov 2014 for adding tooltip
//                   if(dimData1=="")
//                   {
//                       dimFilter=data;
//                       dimData1=data;
//                   }else
//                   dimData1 = dimData1+","+data;
//                    }//ended by Mayank on 11 Nov 2014 for adding tooltip
                    String subTotalDeviation = "";
                    String measureType = "Standard";
                    String newId = id.replace("A_", "");
                    int index = newId.indexOf("_");
                    // 
                    String elementId = newId.substring(0, index);
                    subTotalDeviation = tableBldr.getSubTotalDeviation("A_" + elementId);
                    measureType = tableBldr.getMeasureType("A_" + elementId);
                    ArrayList<String> newUIfilters = new ArrayList<String>();
                    HashMap<String, ArrayList<String>> newUiFilter1 = new HashMap<String, ArrayList<String>>();
                    if (ViewByCount > col) {
                        newUiFilter1 = this.tableBldr.facade.container.getnewUiFilter();
                        newUIfilters = newUiFilter1.get(elementId);
                        if (newUIfilters != null && !newUIfilters.isEmpty()) {
                            if (newUIfilters.contains(data)) {
                            } else {
                                newUIfilters.add(data);
                            }
                        } else {
                            newUIfilters = new ArrayList<String>();
                            newUIfilters.add(data);
                        }
                        newUiFilter1.put(elementId, (ArrayList<String>) newUIfilters);
                        this.tableBldr.facade.container.setnewUiFilter(newUiFilter1);
                    }

                    //   String name1=tableBldr.facade.getMeasureName("A_"+elementId);

                    // ArrayList<String> MeasureNames = tableBldr.facade.container.getReportMeasureNames();
                    // ArrayList  columnviewby= tableBldr.facade.container.getViewByColNames();
                    // ArrayList rowviewbylist=tableBldr.facade.container.getRowViewList();
                    // ArrayList measureIdsList = tableBldr.facade.container.getTableDisplayMeasures();
                    //  HashMap<String,String>meas=new HashMap();
                    //  HashMap<String,String> crosstabMeasureId=((PbReturnObject)tableBldr.facade.container.getRetObj()).crosstabMeasureId;
                    //    ArrayList<String> columnname=tableBldr.facade.container.getColumnViewList();
                    //  String elementid = tableBldr.facade.container.getDisplayColumns().get(col);




//              for(int i=0;i<measureIdsList.size();i++){
//                  meas.put((String)measureIdsList.get(i), MeasureNames.get(i));
//                   String measureIdsList1=(String)measureIdsList.get(i);
//                   String value=tableBldr.facade.container.getreportDrillMaptooltip(measureIdsList1);
//                   //
//
//                    String measureIdsList2=measureIdsList1.replace("A_", "");
//
//                     name3.add(measureIdsList2);
//
//
//
//              }
                    // String viewbi1=tableBldr.facade.getViewbyId();

                    if (tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrill") || tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrillPopUp")) {
                        reportDrillUrl = tableRow.getReportDrillList(col);
                    }
                    selfReportDrillUrl = tableRow.getSelfreportDrillList(col);
                    //Start of code by Bhargavi on 28th july 2015
                    if (this.tableBldr.isCrossTab()) {
                        if (this.tableBldr.facade.container.isSummarizedMeasuresEnabled() && this.tableBldr.facade.container.getSummerizedTableHashMap().get("summerizedQryeIds") != null) {
                            ArrayList suumList = this.tableBldr.facade.container.getSummerizedTableHashMap().get("summerizedQryeIds");
                            if (!suumList.contains(id.split("_")[1]) && id.contains("A_")) {
                                reportDrillUrl = "#";
                            }
                        } else if (id.contains("A_")) {
                            reportDrillUrl = "#";
                        }
                    }


                    // added by krishan pratap for tooltip
                    if (!tableBldr.isCrossTab()) {
                        if (!name3.contains(elementId)) {
                            if (col <= ViewByCount - 1) {
                                dimData = dimData + "~" + data;
                                name.add(data);
                                if (dimData1 == "") {

                                    dimFilter = data;

                                    for (int i = 0; i < 1; i++) {
                                        viewname = heading.get(i);
                                        title = viewname + " : " + data;
                                    }
                                    dimData1 = title;
                                } else {
                                    viewname = heading.get(col);

                                    title1 = viewname + " : " + data;
                                    title1 = dimData1 + "\n" + title1;

                                    dimData1 = title1;

                                }
                            }
                        } else {

                            if (prevtooltipvalue != "" && prevtooltipvalue != null) {

                                dimData1 = dimData1.replace(prevtooltipvalue, "");
                                dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0, dimData1.lastIndexOf("\n")) : dimData1;
                                //int lastindes=dimData1.lastIndexOf(dimData1);
                                // dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0,dimData1.lastIndexOf("\n")) : dimData1;
                            }
                            if (prevmeasname != "" && prevmeasname != elementId && prevmeasname != null) {
                                dimData1 = dimData1.replace(title1, "");
                                dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0, dimData1.lastIndexOf("\n")) : dimData1;

                            }

                            String measname = (String) meas.get("A_" + elementId);
                            prevmeasname = elementId;
                            title1 = measname + " : " + data;
                            String compareFrom = "";
                            String compareTo = "";
                            String[] timeDetails=new String[3];
                    int f=0;
                    String datetype="";
                            ArrayList<String> timeinfo = tableBldr.facade.container.getReportCollect().timeDetailsArray;
                     StringTokenizer st = new StringTokenizer(timeinfo.get(2),"/");
                     while (st.hasMoreTokens()) {
                         timeDetails[f]=(st.nextToken());
                         f++;
                     }
                                ArrayList months = new ArrayList();
                                months.add(0, " ");
                                months.add(1, "Jan");
                                months.add(2, "Feb");
                                months.add(3, "Mar");
                                months.add(4, "Apr");
                                months.add(5, "May");
                                months.add(6, "Jun");
                                months.add(7, "Jul");
                                months.add(8, "Aug");
                                months.add(9, "Sep");
                                months.add(10, "Oct");
                                months.add(11, "Nov");
                                months.add(12, "Dec");
                            if (cssClass.equalsIgnoreCase("measureNumericCellPositive") || cssClass.equalsIgnoreCase("measureNumericCellNegative")) {
                                //added by anitha for date tooltip purpose
                                if(tableBldr.facade.container.getAOId()!=null && !tableBldr.facade.container.getAOId().equalsIgnoreCase("")){                                                                       
                                    try {
                                        ProgenTimeDefinition timeDefObj = new ProgenTimeDefinition(tableBldr.facade.container.getReportId(), tableBldr.facade.container,"");
                                        Map<String, String> timeDefinition = new HashMap<String, String>();
                                        timeDefinition= timeDefObj.getTimeDefinition(); 
                                        ColorHelper signHelper = tableBldr.facade.container.getSignForMeasure("A_"+elementid);
                                        RTComparisionwith="";
                                        if(signHelper!=null){
                                        String depMeasId = signHelper.getDependentMeasure();
                                        int column = tableBldr.facade.container.getDisplayColumns().indexOf(depMeasId);
                                        if(column!=-1){
                                        RTComparisionwith = "Measure Compared With:";
                                        RTComparisionwith += tableBldr.facade.container.getHeaderNameforRTMeasure(column,timeDefinition);                                                                               
                                        }
                                        }
                                    } catch (SQLException ex) {
                                        java.util.logging.Logger.getLogger(TableBodyDisplay.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (FileNotFoundException ex) {
                                        java.util.logging.Logger.getLogger(TableBodyDisplay.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                HashMap timeDetailsHashmap = new HashMap();
                                ArrayList timeotherval = new ArrayList();
                                compareFrom = "Compare From: ";
                                compareTo = "Compare To: ";
                                if (timeinfo.get(1).equalsIgnoreCase("PRG_STD")) {
                                    timeDetailsHashmap = tableBldr.facade.container.getReportCollect().getTimememdetails();
                                    if (timeDetailsHashmap != null) {
                                        timeotherval = (ArrayList) timeDetailsHashmap.get("PR_DAY_DENOM");
                                    }
                                    if (timeotherval != null) {
                                        if (timeotherval.get(0).toString().contains("-")) {

                                            String monthValue1 = months.get(Integer.parseInt((String) timeotherval.get(0).toString().split(" ")[0].split("-")[1])).toString();
                                            String monthValue2 = months.get(Integer.parseInt((String) timeotherval.get(1).toString().split(" ")[0].split("-")[1])).toString();
                                            String monthValue3 = months.get(Integer.parseInt((String) timeotherval.get(2).toString().split(" ")[0].split("-")[1])).toString();
                                            String monthValue4 = months.get(Integer.parseInt((String) timeotherval.get(3).toString().split(" ")[0].split("-")[1])).toString();
                                            String stdate = timeotherval.get(0).toString().split(" ")[0].split("-")[2] + "-" + monthValue1 + "-" + timeotherval.get(0).toString().split(" ")[0].split("-")[0];
                                            String enddate = timeotherval.get(1).toString().split(" ")[0].split("-")[2] + "-" + monthValue2 + "-" + timeotherval.get(1).toString().split(" ")[0].split("-")[0];
                                            String stdatecmp = timeotherval.get(2).toString().split(" ")[0].split("-")[2] + "-" + monthValue3 + "-" + timeotherval.get(2).toString().split(" ")[0].split("-")[0];
                                            String enddatecmp = timeotherval.get(3).toString().split(" ")[0].split("-")[2] + "-" + monthValue4 + "-" + timeotherval.get(3).toString().split(" ")[0].split("-")[0];
                                            compareFrom = compareFrom + stdate + " To " + enddate;
                                            compareTo = compareTo + stdatecmp + " To " + enddatecmp;
                                        } else {
                                            String monthValue1 = months.get(Integer.parseInt((String) timeotherval.get(0).toString().split(" ")[0].split("/")[0])).toString();
                                            String monthValue2 = months.get(Integer.parseInt((String) timeotherval.get(1).toString().split(" ")[0].split("/")[0])).toString();
                                            String monthValue3 = months.get(Integer.parseInt((String) timeotherval.get(2).toString().split(" ")[0].split("/")[0])).toString();
                                            String monthValue4 = months.get(Integer.parseInt((String) timeotherval.get(3).toString().split(" ")[0].split("/")[0])).toString();
                                            String stdate = timeotherval.get(0).toString().split(" ")[0].split("/")[1] + "-" + monthValue1 + "-" + timeotherval.get(0).toString().split(" ")[0].split("/")[2];
                                            String enddate = timeotherval.get(1).toString().split(" ")[0].split("/")[1] + "-" + monthValue2 + "-" + timeotherval.get(1).toString().split(" ")[0].split("/")[2];
                                            String stdatecmp = timeotherval.get(2).toString().split(" ")[0].split("/")[1] + "-" + monthValue3 + "-" + timeotherval.get(2).toString().split(" ")[0].split("/")[2];
                                            String enddatecmp = timeotherval.get(3).toString().split(" ")[0].split("/")[1] + "-" + monthValue4 + "-" + timeotherval.get(3).toString().split(" ")[0].split("/")[2];
                                            compareFrom = compareFrom + stdate + " To " + enddate;
                                            compareTo = compareTo + stdatecmp + " To " + enddatecmp;
                                        }
                                    }
                                    dateTooltip = compareFrom + "\n" + compareTo;
                                    dimData1 = dimData1 + "\n" + title1;
                                    dimData1 = dimData1 + "\n" + dateTooltip;
                                    if(RTComparisionwith!=null &&!RTComparisionwith.equalsIgnoreCase(""))
                                        dimData1 = dimData1 + "\n" + RTComparisionwith;
                                } else if(timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE")){
                                    String mon1 = timeDetails[0];
                                    String yr1 = timeDetails[2];
                                    String date2 = "";
                                    String date3 = "";
                                    String date4 = "";
                                    String month2 = "";
                                    String year2 = "";
                                    String mon2 = "";
                                    String common1 = "";
                                    String common2 = "";
                                    String comyr1 = "";
                                    String comyr2 = "";
                                    String yr2 = "";
                                    String compare = "";
                                    String date=timeDetails[1];
                                    String month = timeDetails[0];
                                    String year = timeDetails[2];
                                    compareFrom = "Compare From: ";
                                    compareTo = "Compare To: ";
                                    String rgCompFrom1 = date+"-"+months.get(Integer.parseInt(month))+"-"+year;
                                        f = 0;
                                        if(timeinfo.get(3).contains("/"))
                                            st = new StringTokenizer(timeinfo.get(3), "/");
                                        else if(timeinfo.get(3).contains("-"))
                                            st = new StringTokenizer(timeinfo.get(3), "-");
                                        while (st.hasMoreTokens()) {
                                            timeDetails[f] = (st.nextToken());
                                            f++;
                                        }
                                        date2 = timeDetails[1];
                                        month2 = timeDetails[0];
                                        year2 = timeDetails[2];
                                        String rgCompFrom2 = date2+"-"+months.get(Integer.parseInt(month2))+"-"+year2;

                                        f = 0;
                                        if (timeinfo.get(4) == null) {
                                            timeinfo.set(4, timeinfo.get(2));
                                        }
                                        if (timeinfo.get(5) == null) {
                                            timeinfo.set(5, timeinfo.get(3));
                                        }
                                        if(timeinfo.get(4).contains("/"))
                                            st = new StringTokenizer(timeinfo.get(4), "/");
                                        else if(timeinfo.get(4).contains("-"))
                                            st = new StringTokenizer(timeinfo.get(4), "-");
                                        while (st.hasMoreTokens()) {
                                            timeDetails[f] = (st.nextToken());
                                            f++;
                                        }
                                        date3 = timeDetails[1];
                                        common1 = timeDetails[0];
                                        comyr1 = timeDetails[2];
                                        String rgCompFrom3 = date3+"-"+months.get(Integer.parseInt(common1))+"-"+comyr1;
                                        f = 0;
                                        if(timeinfo.get(5).contains("/"))
                                            st = new StringTokenizer(timeinfo.get(5), "/");
                                        else if(timeinfo.get(5).contains("-"))
                                            st = new StringTokenizer(timeinfo.get(5), "-");
                                        while (st.hasMoreTokens()) {
                                            timeDetails[f] = (st.nextToken());
                                            f++;
                                        }
                                        date4 = timeDetails[1];
                                        common2 = timeDetails[0];
                                        comyr2 = timeDetails[2];
                                        String rgCompFrom4 = date4+"-"+months.get(Integer.parseInt(common2))+"-"+comyr2;
                                        compareFrom = compareFrom+rgCompFrom1+" TO "+rgCompFrom2;
                                        compareTo = compareTo+rgCompFrom3+" TO "+rgCompFrom4;
                                        dateTooltip = compareFrom + "\n" + compareTo;
                                        dimData1 = dimData1 + "\n" + title1;
                                        dimData1 = dimData1 + "\n" + dateTooltip;
                                        if(RTComparisionwith!=null &&!RTComparisionwith.equalsIgnoreCase(""))
                                        dimData1 = dimData1 + "\n" + RTComparisionwith;
                                    //end of code by anitha for date tooltip purpose
                                } else {
                                    dimData1 = dimData1 + "\n" + title1;
                                }
                            } else {
                                dimData1 = dimData1.replace(dateTooltip, "").replace(RTComparisionwith, "");
                                dimData1 = dimData1.replace("\n\n\n", "\n").replace("\n\n", "\n");
                                dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0, dimData1.lastIndexOf("\n")) : dimData1;
                                dimData1 = dimData1 + "\n" + title1;
                            }
                            if (tableBldr.facade.container.getreportDrillMaptooltip("A_" + elementId) != "" && tableBldr.facade.container.getreportDrillMaptooltip("A_" + elementId) != null) {
//                          if(prevtooltipvalue!="" && prevtooltipvalue != null){
//                                dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0,dimData1.lastIndexOf("\n")) : dimData1;
//                                dimData1=dimData1.replace(prevtooltipvalue, "");
//                                //int lastindes=dimData1.lastIndexOf(dimData1);
//                               // dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0,dimData1.lastIndexOf("\n")) : dimData1;
//                   }
//                  if(prevmeasname!="" && prevmeasname!=elementId &&prevmeasname!=null ){
//                              dimData1=dimData1.replace(title1, "");
//                               dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0,dimData1.lastIndexOf("\n")) : dimData1;
//
//                   }

                                if (tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrill") || tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrillPopUp")) {

                                    prevtooltipvalue = tableBldr.facade.container.getreportDrillMaptooltip("A_" + elementId);
                                    dimData1 = dimData1 + "\n" + prevtooltipvalue;
                                }
                            }

                        }
                    } else {
//                    if(col<=ViewByCount-1){
//                   dimData = dimData+"~"+data;
//                   //started by Mayank on 11 Nov 2014 for adding tooltip
//                   if(dimData1=="")
//                   {
//                       dimFilter=data;
//                       dimData1=data;
//                   }else
//                   dimData1 = dimData1+","+data;
//                    }



                        if (!elementId.contains("A")) {
                            if (!name3.contains(elementId)) {
                                if (col <= ViewByCount - 1) {
                                    dimData = dimData + "~" + data;
                                    name.add(data);
                                    if (dimData1 == "") {
                                        dimFilter = data;
                                        viewname = rowviewbymap.get(elementId);
                                        title = viewname + " : " + data;
                                        dimData1 = title;
                                    } else {

                                        viewname = rowviewbymap.get(elementId);
                                        title1 = viewname + " : " + data;
                                        title1 = dimData1 + "\n" + title1;
                                        dimData1 = title1;
                                    }

                                } else {
                                    if (prevtooltipvalue != "" && prevtooltipvalue != null) {

                                        dimData1 = dimData1.replace(prevtooltipvalue, "");
                                        dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0, dimData1.lastIndexOf("\n")) : dimData1;
                                        //int lastindes=dimData1.lastIndexOf(dimData1);
                                        // dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0,dimData1.lastIndexOf("\n")) : dimData1;
                                    }

                                    if (prevmeasname != "" && prevmeasname != elementId && prevmeasname != null) {
                                        dimData1 = dimData1.replace(title1, "");
                                        dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0, dimData1.lastIndexOf("\n")) : dimData1;

                                    }
                                    String crossmeasnameid = (String) crosstabMeasureId.get("A_" + elementId);
                                    String measname = (String) meas.get(crossmeasnameid);
                                    prevmeasname = crossmeasnameid;
                                    title1 = measname + " : " + data;
                                    dimData1 = dimData1 + "\n" + title1;

                                }

                            }
                        } else {

                            if (!elementId.contains("A")) {


                                if (dimData1 == "") {
                                    dimFilter = data;
                                    viewname = rowviewbymap.get(elementId);
                                    title = viewname + " : " + data;
                                    dimData1 = title;
                                } else {

                                    viewname = rowviewbymap.get(elementId);
                                    title1 = viewname + " : " + data;
                                    title1 = dimData1 + "\n" + title1;
                                    dimData1 = title1;
                                }


                            }

                            String crossmeasnameid = (String) crosstabMeasureId.get(elementId);
                            if (prevtooltipvalue != "" && prevtooltipvalue != null) {

                                dimData1 = dimData1.replace(prevtooltipvalue, "");
                                dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0, dimData1.lastIndexOf("\n")) : dimData1;
                                //int lastindes=dimData1.lastIndexOf(dimData1);
                                // dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0,dimData1.lastIndexOf("\n")) : dimData1;
                            }
                            if (prevmeasname != "" && prevmeasname != elementId && prevmeasname != null) {
                                dimData1 = dimData1.replace(title1, "");
                                dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0, dimData1.lastIndexOf("\n")) : dimData1;

                            }

                            String measname = (String) meas.get(crossmeasnameid);
                            prevmeasname = crossmeasnameid;
                            title1 = measname + " : " + data;
                            dimData1 = dimData1 + "\n" + title1;
                            if (prevtooltipvalue != "" && prevtooltipvalue != null) {
                                dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0, dimData1.lastIndexOf("\n")) : dimData1;
                                dimData1 = dimData1.replace(prevtooltipvalue, "");
                                //int lastindes=dimData1.lastIndexOf(dimData1);
                                // dimData1 = (dimData1.endsWith("\n")) ? dimData1.substring(0,dimData1.lastIndexOf("\n")) : dimData1;
                            }
                            if (tableBldr.facade.container.getreportDrillMaptooltip(crossmeasnameid) != "" && tableBldr.facade.container.getreportDrillMaptooltip(crossmeasnameid) != null) {
                                if (!"#".equals(reportDrillUrl)) {
                                    prevtooltipvalue = tableBldr.facade.container.getreportDrillMaptooltip(crossmeasnameid);
                                    dimData1 = dimData1 + "\n" + prevtooltipvalue;
                                }
                            }
                        }


                    }
                    //ended by krishan pratap for tooltip






//               BigDecimal subTotalValue = TableBuilder.getSubTotalValue(subTotalHolder[col - 1], i, "ST");

                    //end of code by bhargavi
                    //

                    bgColor = tableRow.getColor(col);
                    //added by Nazneen on 8May14 for disable ST Dev properties for rank with ST measures
                    if (newId.contains("rankST")) {
                        subTotalDeviation = null;
                    }
                    //ended by Nazneen on 8May14 for disable ST Dev properties for rank with ST measures
                    //start of code by Nazneen
                    if (subTotalDeviation != null && !subTotalDeviation.equalsIgnoreCase("") && !subTotalDeviation.equalsIgnoreCase("null") && subTotalDeviation.equalsIgnoreCase("Y")) {
                        BigDecimal subTotalValue = null;
                        if (!cssClass.equalsIgnoreCase("dimensionCell")) {
                            subTotalValue = tableBldr.getSubTotalVal(col, "CATST", dimData, ViewByCount);
                            if (subTotalValue != null) {
                                String newData = data.replace(",", "").replace("$", "").replace("Rs", "").replace("Euro", "").replace("Yen", "").replace("%", "").replace("AED", "").replace("K", "").replace("M", "").replace("L", "").replace("C", "").replace(" ", "");
//                        subTotalValue = subTotalValue.setScale(2, RoundingMode.CEILING);
//
//                        String valTemp = subTotalValue.toString();
//                        if(valTemp.contains(".")){
//                            int indexVal = valTemp.indexOf(".");
//                            if(valTemp.length()>=indexVal+4){
//                            String indexs = valTemp.substring(indexVal+3,indexVal+4);
//                            int newIndex = Integer.parseInt(indexs);
//                            if(newIndex>=5){
//                                subTotalValue = subTotalValue.setScale(2, RoundingMode.CEILING);
//                            }
//                            else
//                                subTotalValue = subTotalValue.setScale(2, RoundingMode.FLOOR);
//                            }
//                        }

                                BigDecimal newDataVal = new BigDecimal(newData);
                                int res = newDataVal.compareTo(subTotalValue); // compare newDataVal with subTotalValue
                                if (res == 0) {
                                    bgColor = "";
                                } else if (res == 1) {
                                    if (measureType.equalsIgnoreCase("Standard")) {
                                        bgColor = "#008000"; //green color
                                    } else {
                                        bgColor = "#FF0000"; //red color
                                    }
                                } else if (res == -1) {
                                    if (measureType.equalsIgnoreCase("Standard")) {
                                        bgColor = "#FF0000"; //red color
                                    } else {
                                        bgColor = "#008000"; //green color
                                    }
                                }
                            }
                        }
                    } else {
                        //end of code by Nazneen
                        //start of code for colorGroup by veena
                        if (ViewByCount >= 2 && tableBldr.facade.isAnySubtotallingRequired() && !tableBldr.facade.container.isReportCrosstab()) {
                            BigDecimal subTotalValue = null;
                            if (!cssClass.equalsIgnoreCase("dimensionCell")) {
                                subTotalValue = tableBldr.getSubTotalVal(col, "CATST", dimData, ViewByCount);
                            }
                            if (elementId.contains("A")) {
                                bgColor = tableBldr.facade.getColor(tableRow.getRowNumber(), elementId, subTotalValue);
                            } else {
                                bgColor = tableBldr.facade.getColor(tableRow.getRowNumber(), "A_" + elementId, subTotalValue);
                            }
                        }
                        //end of code by veena
                    }



                    if (!cssClass.equalsIgnoreCase("dimensionCell1")) {
                        textColor = tableRow.getFontColors(col);
                        //added by sruthi for fontcolor
                        boolean isCrossTabReport = false;
                        if (tableBldr.facade.container.getReportCollect().reportColViewbyValues != null && tableBldr.facade.container.getReportCollect().reportColViewbyValues.size() > 0) {
                            isCrossTabReport = true;
                        }
                        if (isCrossTabReport && crosstabMeasureId.containsKey(elementid)) {
                            String scripttextColor = tableBldr.gettextColor(crosstabMeasureId.get(elementid).toString());
                            if (scripttextColor != "") {
                                textColor = scripttextColor;
                            }
                        } else {
                            String normaltextColor = tableBldr.gettextColor("A_" + elementid);
                            if (normaltextColor != "") {
                                textColor = normaltextColor;
                            }
                        }
                        //ended by sruthi

                    }
                    //added by Nazneen  for Text color = white when Color group is applied
                    if (bgColor != null && !bgColor.equalsIgnoreCase("") && !bgColor.equalsIgnoreCase("null") && !bgColor.equalsIgnoreCase(" ")) {
                        textColor = "white";
                    }
                    //added by Nazneen for Text color = white when ST Dev is applied
                    if (subTotalDeviation != null && !subTotalDeviation.equalsIgnoreCase("") && !subTotalDeviation.equalsIgnoreCase("null") && subTotalDeviation.equalsIgnoreCase("Y")) {
                        if (bgColor == "#FF0000" || bgColor == "#008000") {
                            textColor = "white";
                        }
                    }
                    //ended by Nazneen for Text color = white when ST Dev is applied
                    //Added by Ram for None ViewBys
                    if (elementId.equalsIgnoreCase("None")) {
                        displayStyle = "None";
                    }
                    //Ended by Ram
                    if (!cssClass.equalsIgnoreCase("dimensionCell1")) {
                        if (ViewByCount == 2 && tableBldr.isIsSubtotalTopBottom() && col == 0) {
                            body.append("<Td id='").append(id).append("'").append(" class='").append(cssClass).append("'").append(" rowspan=").append(rowLst.size()).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" style='display:").append(displayStyle).append(";");
                        } else if (ViewByCount == 3 && tableBldr.isIsSubtotalTopBottom() && col == 1) {
                            if (tableRow.getRowSpan(col) >= tableBldr.getSubTotalTopBottomCount()) {
                                body.append("<Td id='").append(id).append("'").append(" class='").append(cssClass).append("'").append(" rowspan=").append(tableBldr.getSubTotalTopBottomCount()).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" style='display:").append(displayStyle).append(";");
                            } else {
                                body.append("<Td id='").append(id).append("'").append(" class='").append(cssClass).append("'").append("title='" + dimData1 + "'") //added by Mayank on 11 Nov 2014 for adding tooltip
                                        //                            .append("onmouseover=\"tooltipDisplay('"+id+"','"+dimData+"')\"")
                                        .append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" style='display:").append(displayStyle).append(";");
                            }
//                   }else if(ViewByCount==3 &&tableBldr.isIsSubtotalTopBottom() && col==0){
//                       if(tableRow.getRowSpan(col)>=tableBldr.getSubTotalTopBottomCount()){
//                           body.append("<Td id='").append(id).append("'").append(" class='").append(cssClass)
//                               .append("'")
//                               .append(" rowspan=").append(rowLst.size())
//                               .append(" colspan=").append(tableRow.getColumnSpan(col))
//                               .append(" style='display:").append(displayStyle).append(";");
//                   }else{
//                   body.append("<Td id='").append(id).append("'").append(" class='").append(cssClass)
//                   .append("'")
//                   .append("title='"+dimData1+"'") //added by Mayank on 11 Nov 2014 for adding tooltip
////                            .append("onmouseover=\"tooltipDisplay('"+id+"','"+dimData+"')\"")
//                   .append(" rowspan=").append(tableRow.getRowSpan(col))
//                   .append(" colspan=").append(tableRow.getColumnSpan(col))
//                   .append(" style='display:").append(displayStyle).append(";");
//               }
                        } else if (ViewByCount == 3 && tableBldr.isIsSubtotalTopBottom() && col == 0) {
                            body.append("<Td id='").append(id).append("'").append(" class='").append(cssClass).append("'").append("title='" + dimData1 + "'") //added by Mayank on 11 Nov 2014 for adding tooltip
                                    //                            .append("onmouseover=\"tooltipDisplay('"+id+"','"+dimData+"')\"")
                                    .append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" style='display:").append(displayStyle).append(";");
                        } else {
                            body.append("<Td id='").append(id).append("'").append(" class='").append(cssClass).append("'").append("title='" + dimData1 + "'") //added by Mayank on 11 Nov 2014 for adding tooltip
                                    //                            .append("onmouseover=\"tooltipDisplay('"+id+"','"+dimData+"')\"")
                                    .append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" style='display:").append(displayStyle).append(";");
                        }
                    } else {
                        body.append("<Td id='").append(id).append("'").append(" class='").append("headerText1").append("'").append(" rowspan=").append(tableRow.getRowSpan(col)).append(" colspan=").append(tableRow.getColumnSpan(col)).append(" style='text-align:left;display:").append(displayStyle).append(";");
                    }
                    if (tableRow.getTextAlign(col) != null) {
                        body.append("text-align:").append(tableRow.getTextAlign(col)).append(";");
                    }
                    if (transposeFormatList != null) {
                        body.append("text-align:").append(transposeFormatList.get(0)).append(";");
                    }
                    body.append("background-color:").append(bgColor).append("'");
                    body.append(">");
                    href = tableRow.getAnchors(col);

                    if (data.equalsIgnoreCase("-0") || data.equalsIgnoreCase("-0.") || data.equalsIgnoreCase("-00.") || data.equalsIgnoreCase("-0.0") || data.equalsIgnoreCase("-0.00")) {
                        data = "0";
                    }
                    if (data.equalsIgnoreCase("-00.0") || data.equalsIgnoreCase("-00.00") || data.equalsIgnoreCase("-00.") || data.equalsIgnoreCase("-00")) {
                        data = "0";
                    }
                    if (data.contains("-00.000") || data.contains("-0.000")) {
                        data = "0";
                    }
                    if (!"#".equals(href)) {
                        //body.append("<A href=\"javascript:parent.submiturls('").append(tableRow.getAnchors(col)).append("')\"").append("  target='_parent' style='text-decoration:none'>");//changed by govardhan for opening report in new tab
                        //manik
//                   body.append("<A href=\"javascript:parent.submiturlsinNewTab('").append(tableRow.getAnchors(col)).append("')\"").append("  target='_parent' style='text-decoration:none'>");
//                   Modified by Faiz Ansari
                        body.append("<ul class=\"dropDownMenuad2\"><li  style=\"text-align:"+tableRow.getTextAlign(col)+";font-size:"+size+"px;color:"+textColor+";\" class=\"gFontFamily gFontSize12\" onclick='showTSubMenu(event,\"" + id + "\")'  >").append(data.replaceAll("\\p{Pd}", "-")); //to replace em-dash with a basic '-'
                        body.append("<ul  onmouseleave='hideTSubMenu()' class='viewBySubMenu hideDropDown'>");
                        if (tableRow.getReportParameters() != null) {
                            if (!tableRow.getColumnId(tableRow.getViewbyCount() - 1).contains("TIME")) {
                                tableRow.getReportParameters().remove("Time Drill");
                                tableRow.getReportParameterNames().remove("Time Drill");
                            }
                            //added by dinanath for AdhocDrill dropdown
                            int j = 0;
                            int ng = 0;
                            List<Integer> al = new ArrayList<Integer>();
//                            while(j<tableRow.getReportParameters().size())
//                            {
//                                         al.add(tableRow.getReportParameterNames().get(j).length());
//                                         j++;
//                                }
//                             for(int v = 0; v < al.size(); v++) {
//                                        
//                                }
                            //al.add(12);
                            int max = 0;
                            List al2 = new ArrayList();
//ended by dinanath
                            for (int i = 0; i < tableRow.getReportParameters().size(); i++) {
                                //  
//                        if(!tableRow.getReportParameters().get(i).equalsIgnoreCase(tableRow.getColumnId(0).replace("A_",""))){
                                if (!tableRow.getReportParameters().get(i).equalsIgnoreCase(tableRow.getColumnId(0).replace("A_", ""))) {
//                            body.append("<li><a onclick=\"javascript:viewAdhocDrill('");//commented by Dinanath
                                    if (tableBldr.getAdhocDrillType().equalsIgnoreCase("drillside")) {
                                        body.append("<li><a onclick=\"javascript:viewAdhocDrill('");
                                        body.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getReportParameters().get(i)).append("','").append(tableRow.getRowData(tableRow.getViewbyCount() - 1)).append("','").append(tableRow.getViewbyId()).append("','").append(tableRow.getColumnId(tableRow.getViewbyCount() - 1)).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(tableRow.getAdhocUrl()).append("','").append(tableRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(tableRow.getReportParameterNames().get(i)).append("</a></li>");
                                    } else {
                                        //body.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getReportParameters().get(i)).append("','").append(tableRow.getRowData(tableRow.getViewbyCount()-1)).append("','").append(tableRow.getViewbyId()).append("','").append(tableRow.getColumnId(tableRow.getViewbyCount()-1)).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(tableRow.getAdhocTimeUrl()).append("','").append(tableRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(tableRow.getReportParameterNames().get(i)).append("</a></li>");

//                            pn[m]=tableRow.getReportParameterNames().get(i);//+" h dfdsf sdfds sdfds dsfds sdfds dsfds sdfsdf";
                                        al.add(tableRow.getReportParameterNames().get(i).length());

//                            for(int h=2;h<al.size()+2;h=h+3)
//                            {             max=al.get(h-2);
//                                           for(int s=h-2;s<=h&&s<al.size();s++){
//                                                    if(al.get(s)>=max)
//                                                    max=al.get(s);
//                                            }
//                                            al2.add(max);
//                                            int g=max+12;
//                                          for(int s=h-2;s<=h&&s<al.size();s++){
//                                                
//
//                                        body.append("<li><a onclick=\"javascript:viewAdhocDrill('");
//                                        body.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getReportParameters().get(s)).append("','").append(tableRow.getRowData(tableRow.getViewbyCount()-1)).append("','").append(tableRow.getViewbyId()).append("','").append(tableRow.getColumnId(tableRow.getViewbyCount()-1)).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(tableRow.getAdhocTimeUrl()).append("','").append(tableRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none;word-wrap: break-word;height:"+g+"px;'>").append(tableRow.getReportParameterNames().get(s)).append("</a></li>");
//
//                                        }
//                            }
                                    }
                                }//if
                                // break;
                            }//for
                            for (int h = 2; h < al.size() + 2; h = h + 3) {
                                max = al.get(h - 2);
                                for (int s = h - 2; s <= h && s < al.size(); s++) {
                                    if (al.get(s) >= max) {
                                        max = al.get(s);
                                    }
                                }
                                al2.add(max);
                                int g = max + 12;
                                for (int s = h - 2; s <= h && s < al.size(); s++) {
                                    // 
                                    // added by  krishan Pratap
                                    body.append("<li><a onclick=\"javascript:viewAdhocDrill('");
                                    body.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getReportParameters().get(s)).append("','").append(data).append("','").append(tableRow.getViewbyId()).append("','").append(elementId).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(tableRow.getAdhocTimeUrl()).append("','").append(tableRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none;word-wrap: break-word;height:" + g + "px;'>").append(tableRow.getReportParameterNames().get(s)).append("</a></li>");
                                    // body.append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getReportParameters().get(s)).append("','").append(tableRow.getRowData(tableRow.getViewbyCount()-1)).append("','").append(tableRow.getViewbyId()).append("','").append(tableRow.getColumnId(tableRow.getViewbyCount()-1)).append("','").append(tableBldr.getAdhocDrillType()).append("','").append(tableRow.getAdhocTimeUrl()).append("','").append(tableRow.getAdhocParamDrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none;word-wrap: break-word;height:"+g+"px;'>").append(tableRow.getReportParameterNames().get(s)).append("</a></li>");


                                }
                            }//endof for
                        }
                        body.append("</ul></li></ul>");



//                   body.append("</A>");
                    } else {
                        if (imagePath != null) {
                            body.append(imagePath);
                        } else {
                            body.append("<div class=\"gFontFamily gFontSize12 comWidth widthClass" + col + "\" style='display:inline-flex;font-size:" + size + "px;float:" + tableRow.getTextAlign(col) + "'  color='").append(textColor).append("'");
//                        body.append("<Font class=\"gFontFamily gFontSize12\" style='font-size:"+size+"px;'  color='").append(textColor).append("'");
                            if (transposeFormatList != null) {
                                body.append(" style='font-weight:").append(transposeFormatList.get(1)).append(";'");
                            }
//                                 else if(tableBldr.getDrillMeasure()){
//                               //body.append(">").append("<A href=\"javascript:parent.drillMeasure('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableBldr.getMeasureElemId(col)).append("')\"").append("  target='_parent' style='text-decoration:none'>");
//                               body.append(">").append("<A href=\"javascript:parent.drillMeasure('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getMeasrId(col)).append("','").append(tableRow.getMsrdrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none'>");
//                             //  body.append(">").append("<A href=\"javascript:parent.drillMeasure('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getMeasrId(col)).append("','").append(tableBldr.getDimension(rowCount)).append("')\"").append("  target='_parent' style='text-decoration:none'>");
//                              body.append(data);
//                              body.append("</A>").append("</Font>");
//                               }
                            if (tableBldr.getMeasureDrillType().equalsIgnoreCase("MeasureDrill")) {
                                body.append("><span>").append("<A href=\"javascript:parent.drillMeasure('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getMeasrId(col)).append("','").append(tableRow.getMsrdrillUrl()).append("')\"").append("  target='_parent' style='text-decoration:none><span class=\"gFontFamily gFontSize12\" style='font-weight: bold;font-size:" + size + "px;'>").append(data).append("</span></A>").append("</span></div>");
                            } else if (tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrill")) {
                                if (!"#".equals(reportDrillUrl)) {
                                    if (tableBldr.getMsrDrillReportSelection().equalsIgnoreCase("multi report")) {
                                        //    
                                        reportDrillUrl = reportDrillUrl.replace("&REPORTID=" + tableBldr.getReportDrillMap(tableRow.getMeasrId(col)), "");
                                        body.append("><span>").append("<A href=\"javascript:multireportMsrDrill('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(java.net.URLEncoder.encode(reportDrillUrl)).append("','").append(tableRow.getMeasrId(col)).append("')\"").append("  target='_parent' class=\"gFontFamily gFontSize12\" style='text-decoration:none;font-size:" + size + "px;'>").append(data).append("</A>").append("</span></div>");
                                    } else if (tableBldr.isDrillAcrossSupported()) {
                                        body.append("><span>").append("<A href=\"javascript:reportDrill('").append(java.net.URLEncoder.encode(reportDrillUrl)).append("')\"").append("  target='_parent' class=\"gFontFamily gFontSize12\" style='text-decoration:none;font-size:" + size + "px;'>").append(data).append("</A>").append("</span></div>");

                                    } else {
                                        // added by sandeep
                                        //modified by anitha
                                        if (tableRow.getViewbyCount() > 0) {
                                            if (tableBldr.facade.container.getViewByColNames().contains("TIME")||tableBldr.facade.container.getViewByColNames().contains("Time") ) {
                                                if (tableBldr.facade.container.getReportCollect().timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                                                    String compare = tableBldr.facade.container.getReportCollect().timeDetailsArray.get(4).toString();
                                                    String prgcompare = tableBldr.facade.container.getReportCollect().timeDetailsArray.get(3).toString();
//                                   if(tableBldr.facade.container.getReportCollect().timeDetailsArray.get(3).toString().equalsIgnoreCase("Month")){
                                                    reportDrillUrl = reportDrillUrl.replace("Day", prgcompare).replace("Month", prgcompare).replace("year", prgcompare).replace("Qtr", prgcompare).replace("Week", prgcompare);
                                                    reportDrillUrl += "&CBO_PRG_COMPARE=" + compare;
//reportDrillUrl=reportDrillUrl.replace("&CBO_PRG_COMPARE=Day",prgcompare);
//finalrepDrillUrl+="&REPORTID="+tableBldr.getReportDrillMap(tableRow.getMeasrId(col))+"&reportDrill=Y&DDrillAcross=Y&DDrill=Y&DrillMonth="+data+"&CBO_PRG_PERIOD_TYPE="+prgcompare+"&CBO_PRG_COMPARE="+compare;;
//reportDrillUrl=finalrepDrillUrl;
//reportDrillUrl+="&DDrill=Y&DrillMonth="+data+"&CBO_PRG_PERIOD_TYPE="+prgcompare+"&CBO_PRG_COMPARE="+compare;

                                                }

                                            } else {
                                                if (tableBldr.getMsrDrillReportSelection().equalsIgnoreCase("multi report")) {
//                             reportDrillUrl=reportDrillUrl.replace("&REPORTID="+tableBldr.getReportDrillMap(tableRow.getMeasrId(col)),"");
                                                }
                                            }
                                        }
                                        // end of sandeep code
                                        //body.append(">").append("<A href=\"javascript:parent.submiturls('").append(java.net.URLEncoder.encode(reportDrillUrl)).append("')\"").append("  target='_parent' style='text-decoration:none'>").append(data).append("</A>").append("</Font>");//changed by govardhan for opening report in new tab
                                        body.append("><span>").append("<A href=\"javascript:parent.submiturlsinNewTab('").append(java.net.URLEncoder.encode(reportDrillUrl)).append("')\"").append("  target='_parent' class=\"gFontFamily gFontSize12\" style='text-decoration:none'>").append(data).append("</A>").append("</span></div>");
                                    }

                                } else {
                                    body.append("><span>").append("<Font style=\" font-size:"+size+"px;color:"+textColor+";\">").append(data).append("</Font></span></div>");
                                }
                            } else if (tableBldr.getMeasureDrillType().equalsIgnoreCase("RelatedMeasures")) {
                                body.append("><span>").append("<A href=\"javascript:parent.relatedMeasures('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(tableRow.getMeasrId(col)).append("','").append(tableRow.getMsrdrillUrl()).append("')\"").append("  target='_parent' class=\"gFontFamily gFontSize12\" style='text-decoration:none;font-size:" + size + "px;'>").append(data).append("</A>").append("</span></div>");
                            } else if (tableBldr.getMeasureDrillType().equalsIgnoreCase("ReportDrillPopUp")) {
                                if (!"#".equals(reportDrillUrl) && !tableBldr.getMsrDrillReportSelection().equalsIgnoreCase("multi report")) {
                                    reportDrillUrl = reportDrillUrl.replace("=viewReport", "=viewReportPopUp");
                                    body.append("><span>").append("<A href=\"javascript:parent.reportDrillPopUp('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(java.net.URLEncoder.encode(reportDrillUrl)).append("')\"").append("  target='_parent' class=\"gFontFamily gFontSize12\" style='text-decoration:none;font-size:" + size + "px;'>").append(data).append("</A>").append("<</span></div>");
                                } else {
                                    body.append("><span>").append(data).append("</span></div>");
                                }

                            } else if (tableBldr.getMeasureDrillType().equalsIgnoreCase("SelfDrill")) {
//                        int size = tableBldr.facade.container.getReportCollect().reportColViewbys.size();
//                        if(tableBldr.facade.container.getReportCollect().reportColViewbyValues!=null && tableBldr.facade.container.getReportCollect().reportColViewbyValues.size()>0)
                                body.append("><span>").append("<A href=\"javascript:parent.reportSelfDrill('").append(tableBldr.getReportId()).append("','").append(tableBldr.getContextPath()).append("','").append(java.net.URLEncoder.encode(selfReportDrillUrl)).append("','").append(tableRow.getViewbyId()).append("')\"").append("  target='_parent' class=\"gFontFamily gFontSize12\" style='text-decoration:none;font-size:" + size + "px;'><span style='font-family: verdana;font-weight: bold;font-size: 8pt;'>").append(data).append("</span></A>").append("</span></div>");
//                        else
//                          body.append(">").append(data).append("</Font>");
                            } else {
                                if (cssClass.equalsIgnoreCase("dimensionCell1")) {
                                    body.append("><span class=\"gFontFamily gFontSize12\" style=';font-weight: bold;text-align:left;color:black; '>").append(data).append("</span></div>");
                                } else {
                                    if (tableBldr.isTransposed()) {
                                        body.append("><span><center>").append(data).append("</center></span></div>");
                                    } else {
                                        body.append("><span>").append(data).append("</span></div>");
                                    }
                                }
                            }
                        }
                        if (!editable) {
                            body.append(spaceAftrNo);
                        }
                    }
                    //added by Dinanath for showing trendIcon
                    if (tableBldr.facade.container.isToShowTrendIcon()) {
                        if (!tableBldr.isTransposed()) {//hhh
                            if (col == 0) {
                                // added by Mayank for direct open trend section and apply the filter

                                body.append("<img width='15' height='15' class='trendIcon' style='float:right;cursor:pointer;' onclick='applyFilterToTrend(\"" + tableBldr.facade.getColumnId(col) + "\",\"" + dimFilter + "\"," + JSONArray.toJSONString(filterList) + ")' src='../images/trend.png'/>");
                            }
                        }
                    }
                    body.append("</Td>");
                }

                //code modified by anitha for grand total on transpose report
                try {
                    if (tableBldr.isTransposed()) {
                        String hideMeas = rowLst.get(this.RowCount).getDisplayStyle(0);
                    if (this.tableBldr.facade.isGrandTotalRequired() && hideMeas != null && !hideMeas.equalsIgnoreCase("none")) {                        
                            {
                                body.append("<Td id='GT").append("'").append(" class='measureNumericCell").append("").append("'").append(" rowspan=").append("1").append(" colspan=").append("1").append(" style='background-color: #fff;font-size:12px;font-weight: bold;border-bottom: 1px solid #eee;text-align:").append(subTotalRow[0].getTextAlign(rowId + 1)).append(";").append("'");
                                body.append(">").append(subTotalRow[0].getRowData().get(rowId + 1)).append("</Td>");
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("Exception:", e);
                }
                //end of code by anitha for grand total on transpose report

                RowCount++;
                rowcountAfterSubtotalfilter++;
                body.append("</Tr>");
                rowId = rowId + 1;
//            if(flag){
//                tempBody.append(body);
//            }
            }
        }

        updateSubtotalList(usedUpSubtotals, subtotalRowLst);
        return body;
    }

    protected void updateSubtotalList(int usedUpSubtotals, ArrayList<TableSubtotalRow> subtotalRowLst) {
        for (int i = 0; i < usedUpSubtotals; i++) {
            subtotalRowLst.remove(0);
        }
    }

    public StringBuilder generateHtmlForRows1(ArrayList<TableDataRow> rowLst, ArrayList<TableSubtotalRow> subtotalRowLst) {
        //
        String flag = this.getHeadlineflag();
        StringBuilder body = new StringBuilder();
        int colCount = tableBldr.getColumnCount();
        String displayStyle;
        String data;
        String id;
        String bgColor = "";
        String href;
        String textColor = "";
        String cssClass = "";
        String spaceAftrNo = "";
        int from = 0;
        int subTotalCount = 0;
        String fromOneview = "";
        String getFromtableBuilder = "";
        fromOneview = this.getFromOneviewflag();
        getFromtableBuilder = tableBldr.getGetFromOneview();
        int usedUpSubtotals = 0;
        TableSubtotalRow[] subTotalRow;// = tableBldr.getSubtotalRowDataLastRow();
        //subTotalHtml.append(this.generateSubTotalHtml(subTotalRow));
        subTotalRow = tableBldr.getGrandtotalRowData();
        TableSubtotalRow row = null;
        int count = tableBldr.getViewByCount();
        String cssClass1 = null;
        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        for (int col1 = tableBldr.getFromColumn(); col1 < colCount; col1++) {
            String grvalue = null;
            String id2 = null;
            boolean crs = false;
            for (int i = 0; i < subTotalRow.length; i++) {
                row = subTotalRow[i];
                grvalue = row.getRowData(col1);
                id2 = row.getID(col1);
                cssClass1 = row.getCssClass(col1);
                String displaystyle = row.getDisplayStyle(col1);
                break;
            }

            String heading = null;
            String id1 = null;
            int rowSpan = 0;
            int colSpan = 0;
            String dispStyle = null;
//            TableHeaderRow[] headerRows;

//        headerRows = tableBldr.getHeaderRowData();
            int toRow = tableBldr.getToRow();

//        int colCount = tableBldr.getColumnCount();toRow

            body.append("<tr>");
//            for (int col =0 ; col < toRow; col++) {
            for (TableHeaderRow headerRow : headerRows) {
                if (col1 < headerRow.getRowData().size()) {
                    heading = headerRow.getRowData(col1);
                }
//               int cn = headerRow.getViewbyCount();
                crs = tableBldr.isCrossTab();

                id1 = headerRow.getID(col1);
                rowSpan = headerRow.getRowSpan(col1);
                colSpan = headerRow.getColumnSpan(col1);
                dispStyle = headerRow.getDisplayStyle(col1);
                if (heading != null) {
                    if (col1 == 0) {
                        body.append("<td id='").append(id1).append("'").append("\" style=\"background-color:#B4D9EE; font-family: verdana;font-weight: bold;font-size: 8pt;text-align:left;color:black;display:" + dispStyle + ";  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"><b>");//.append(" class='").append(cssClass)

                    } else {
                        if (count > 1) {
                            if (col1 == 1) {
                                body.append("<td id='").append(id1).append("'").append("\" style=\"background-color:#B4D9EE; font-family: verdana;font-weight: bold;font-size: 8pt;text-align:left;color:black;display:" + dispStyle + ";  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"><b>");//.append(" class='").append(cssClass)

                            } else {
                                body.append("<td id='").append(id1).append("'").append("\" style=\"font-family: verdana;font-weight: bold;font-size: 8pt;text-align:left;color:black; display:" + dispStyle + ";  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"><b>");//.append(" class='").append(cssClass)

                            }
                        } else {
                            body.append("<td id='").append(id1).append("'").append("\" style=\"font-family: verdana;font-weight: bold;font-size: 8pt;text-align:left;color:black; display:" + dispStyle + ";  rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"><b>");//.append(" class='").append(cssClass)
                        }
                    }
                    body.append("<font size='1px' face='verdana' style='font-weight: bold;' align='center'>").append(heading).append("</font>");
//                       body.append("<Font color=").append(textColor).append(">").append(heading).append("</Font>");
                    body.append("</td>");
//                     break;
                } else {
                    break;
                }
            }
            for (int col = 0; col < toRow; col++) {
                for (TableDataRow tableRow : rowLst) {

                    subTotalCount = generateSubtotal(tableRow, subtotalRowLst, from, body);
                    if (subTotalCount != 0) {
                        usedUpSubtotals += subTotalCount;
                        from += subTotalCount;
                    }
                    data = tableRow.getRowData(col1);
                    displayStyle = tableRow.getDisplayStyle(col1);
                    id = tableRow.getID(col1);
                    bgColor = tableRow.getColor(col1);
                    textColor = tableRow.getFontColors(col1);
                    cssClass = tableRow.getCssClass(col1);

                    if (!tableRow.getDisplayStyle(col1).equals("none")) {
                        if (cssClass.equalsIgnoreCase("dimensionCell")) {
                            body.append("<td id='").append(id).append("'");//.append(" class='").append(cssClass)
                            if ((getFromtableBuilder == null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview == null || fromOneview.equalsIgnoreCase(""))) {
                                if (tableBldr.getHtmlCellHeight() != null && !tableBldr.getHtmlCellHeight().equalsIgnoreCase("")) {
                                    //body.append(" height=").append(tableBldr.getHtmlCellHeight()).append("px").append("\"");
                                    body.append(" rowspan=").append(tableRow.getRowSpan(col1)).append(" colspan=").append(tableRow.getColumnSpan(col1)).append(" height=").append(tableBldr.getHtmlCellHeight()).append("px").append("  style='font-size:12px;text-align:right;background-color:#E6E6E6;color:#336699;text-align:left;");
                                } else {
                                    body.append(" rowspan=").append(tableRow.getRowSpan(col1)).append(" colspan=").append(tableRow.getColumnSpan(col1)).append("  style='font-size:12px;text-align:right;background-color:#E6E6E6;color:#336699;text-align:left;");
                                }
                            } else {
                                body.append(" rowspan=").append(tableRow.getRowSpan(col1)).append(" colspan=").append(tableRow.getColumnSpan(col1)).append("  style='font-size:8px;background-color:#B4D9EE;text-align:left;");
                            }
                            if (!("\"\"").equalsIgnoreCase(displayStyle)) {
                                body.append(" display:").append(displayStyle).append(";");
                            }

                            body.append("'>");
                            body.append("<font size='1px' face='verdana' style='font-weight: bold;' align='center'>").append(data).append("</font>");
//                    if((getFromtableBuilder==null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview==null || fromOneview.equalsIgnoreCase(""))){
//                    body.append("<Font color=").append(textColor).append(">").append(data).append("</Font>");
//                    }else{
//                        body.append(data);
//                    }
                            body.append(spaceAftrNo);
                            body.append("</td>");
                        } else {
                            body.append("<td id='").append(id).append("'");//.append(" class='").append(cssClass)
                            if ((getFromtableBuilder == null || getFromtableBuilder.equalsIgnoreCase("")) && (fromOneview == null || fromOneview.equalsIgnoreCase(""))) {
                                if (tableBldr.getHtmlCellHeight() != null && !tableBldr.getHtmlCellHeight().equalsIgnoreCase("")) {
                                    body.append(" rowspan=").append(tableRow.getRowSpan(col1)).append(" colspan=").append(tableRow.getColumnSpan(col1)).append(" height=").append(tableBldr.getHtmlCellHeight()).append("px").append("  style='font-size:12px;background-color:	#F5F5F5;color:#000000;text-align:right;");
                                } else {
                                    body.append(" rowspan=").append(tableRow.getRowSpan(col1)).append(" colspan=").append(tableRow.getColumnSpan(col1)).append("  style='font-size:12px;background-color:	#F5F5F5;color:#000000;text-align:right;");
                                }
                            } else {
                                body.append(" rowspan=").append(tableRow.getRowSpan(col1)).append(" colspan=").append(tableRow.getColumnSpan(col1)).append("  style='font-size:8px;text-align:right;");
                            }
                            if (!("").equalsIgnoreCase(bgColor)) {
                                body.append("background-color:").append(bgColor).append(";");
                            }
                            if (!("\"\"").equalsIgnoreCase(displayStyle)) {
                                body.append(" display:").append(displayStyle).append(";");
                            }
                            body.append("'>");
                            body.append("<Font color=").append(textColor).append(">").append(data).append("</Font>");
                            body.append(spaceAftrNo);
                            body.append("</td>");
                        }
                    }
                }
                break;
            }
            if (crs == true) {
            } else {
                if (!tableBldr.facade.isColumnVisible(tableBldr.facade.getDisplayColumns().get(col1))) {
                } else {
                    if (col1 == 0) {
                        body.append("<td id='").append(id2).append("'").append("  style='background-color:#B4D9EE;text-align:left;");
                        body.append(" rowspan=").append(row.getRowSpan(col1)).append(" colspan=").append(row.getColumnSpan(col1)).append("  style='text-align:right;background-color:#B4D9EE;color:#336699;text-align:left;");
                        body.append("<Font color=").append(textColor).append(">").append(grvalue).append("</Font>");
                        body.append("</td>");
                    } else {
                        body.append("<td id='").append(id2).append("'");
//              body.append(" rowspan=").append(row.getRowSpan(col1)).append(" colspan=").append(row.getColumnSpan(col1)).append("  style='font-size:8px;color:#336699;text-align:left;");
                        body.append("<Font color=").append(textColor).append(">").append(grvalue).append("</Font>");
                        body.append("</td>");
                    }
                }
            }
            body.append("</tr>");

        }
        this.updateSubtotalList(usedUpSubtotals, subtotalRowLst);
        return body;
    }

    protected int generateSubtotal(TableDataRow tableRow, ArrayList<TableSubtotalRow> subtotalRowLst, int from, StringBuilder body) {
        int usedUpSubtotals = 0;
        int subTotalCount = 0;
        if (tableRow.printSubTotals() && !subtotalRowLst.isEmpty()) {
            subTotalCount = tableRow.getSubtotalCount();
            if (tableBldr.getIsSubToTalSearchFilterApplied()) {
                if (subTotalCount >= subtotalRowLst.size()) {
                    subTotalCount = subtotalRowLst.size() - 1 - from;
                }
                if ((subTotalCount + from) < 2 && tableRow.printSubTotals() && (tableRow.isSubTotalFoundForFirstViewBy() || (isFirstRowDeleted && firstRowId == tableRow.getRowNumber())) && tableBldr.getViewByCount() == 3) {

                    subTotalCount = 2;
                } else if ((subTotalCount + from) == 0 && (tableBldr.getViewByCount() == 2 || tableBldr.getViewByCount() == 3)) {
                    subTotalCount = 1;
                }

            }
            TableSubtotalRow[] subTotalRows = subtotalRowLst.subList(from, subTotalCount + from).toArray(new TableSubtotalRow[0]);
            body.append(generateSubTotalHTML(subTotalRows));
            from += subTotalCount;
            usedUpSubtotals += subTotalCount;
        }
        return usedUpSubtotals;
    }

    @Override
    protected void setParentHtml(StringBuilder parentHtml) {
        super.parentHtml = parentHtml;
    }
    /*
     * private StringBuilder substituteCellSpans(StringBuilder body) {
     * ProgenLog.log(ProgenLog.FINE,this,"flushHtmlToStream", "Enter
     * flushHtmlToStream "+System.currentTimeMillis()); Set<TableCellSpan>
     * cellSpanSet = facade.getCellSpans(); Set<TableCellSpan>
     * cellSpanSetToRemove = new HashSet<TableCellSpan>(); int count = 2;
     * Pattern cellIdPattern; Matcher matcher;
     *
     * StringBuilder pattern = new StringBuilder(); StringBuilder replacement =
     * new StringBuilder();
     *
     *
     * for ( TableCellSpan cellSpan : cellSpanSet ) {
     * pattern.append("'").append(cellSpan.getCellId()).append("'");
     * replacement.append("'").append(cellSpan.getCellId()).append("'
     * rowspan=").append(cellSpan.getRowSpan()).append(" ");
     * cellSpanSetToRemove.add(cellSpan); cellIdPattern =
     * Pattern.compile(pattern.toString()); matcher =
     * cellIdPattern.matcher(body); body = new
     * StringBuilder(matcher.replaceAll(replacement.toString())); pattern = new
     * StringBuilder(); replacement = new StringBuilder(); }
     *
     * for ( TableCellSpan cellSpan : cellSpanSetToRemove ) {
     * facade.removeCellSpanFromSet(cellSpan); }
     * ProgenLog.log(ProgenLog.FINE,this,"flushHtmlToStrem", "Exit
     * flushHtmlToStream "+System.currentTimeMillis()); return body;
    }
     */
}
