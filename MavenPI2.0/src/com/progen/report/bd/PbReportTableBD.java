/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.bd;

import com.progen.db.ProgenDataSet;
import com.progen.report.ReportParameter;
import com.progen.report.SearchFilter;
import com.progen.report.SearchFilterColumn;
import com.progen.report.SortColumn;
import com.progen.report.db.ReportTableDAO;
import com.progen.report.excel.*;
import com.progen.report.util.sort.DataSetFilter;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.CreateKPIFromReport;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import prg.db.Container;
import prg.db.ContainerConstants;
import prg.db.ContainerConstants.SortOrder;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class PbReportTableBD {

    public static Logger logger = Logger.getLogger(PbReportTableBD.class);
    ReportTemplateDAO DAO = new ReportTemplateDAO();

    public void storeComment(Container container, int row, int col, String comment) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();
        String measId = container.getDisplayColumns().get(col);
        ExcelCellFormatGroup excelGroup = container.getExcelCellGroup();
        ExcelCellFormat excelCell = excelGroup.createExcelCell(row, measId, repParam);
        excelCell.setComment(comment);
    }

    public void clearComment(Container container, int row, int col) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();
        String measId = container.getDisplayColumns().get(col);
        ExcelCellFormatGroup excelGroup = container.getExcelCellGroup();
        ExcelCellFormat excelCell = excelGroup.getExcelCell(row, measId, repParam);
        if (excelCell != null) {
            excelCell.setComment(null);
        }
    }

    public void storeFormat(Container container, int row, int col, String bgColor, String fontColor) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();
        String measId = container.getDisplayColumns().get(col);
        ExcelCellFormatGroup excelGroup = container.getExcelCellGroup();
        ExcelCellFormat excelCell = excelGroup.createExcelCell(row, measId, repParam);
        excelCell.setBgColor(bgColor);
        excelCell.setFontColor(fontColor);
    }

    public void clearFormat(Container container, int row, int col) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();
        String measId = container.getDisplayColumns().get(col);
        ExcelCellFormatGroup excelGroup = container.getExcelCellGroup();
        ExcelCellFormat excelCell = excelGroup.getExcelCell(row, measId, repParam);
        if (excelCell != null) {
            excelCell.setBgColor(null);
            excelCell.setFontColor(null);
        }
    }

    public void addExcelColumn(Container container, int col, String colName) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();
        String measId = container.getDisplayColumns().get(col + 1);
        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();

        //Create and initialize the runtime column
        RunTimeExcelColumn rtColumn = excelColGroup.createRunTimeColumn(measId, colName);
        rtColumn.setColumnPersisted(false);

        ArrayList data = new ArrayList();
        ArrayList<BigDecimal> rtData = new ArrayList<BigDecimal>();
        int rowCount = container.getRetObj().getRowCount();
        for (int i = 0; i < rowCount; i++) {
            data.add(0);
            rtData.add(BigDecimal.ZERO);
        }

        rtColumn.addColumn(repParam, data);
        container.getRtExcelColumns().add(measId);
        container.getRetObj().setRuntimeMeasure(measId, rtData);
    }

    public void updateExcelColumn(Container container, int col, String data, int fromRow, int toRow) {
        String measId = container.getDisplayColumns().get(col);
        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
        ArrayList colData = null;
        RunTimeExcelColumn excelColumn = excelColGroup.getRunTimeColumn(measId);
        ArrayList refData = new ArrayList();

        //ExcelColumn excelColumn = excelColGroup.getExcelColumn(measId, repParam);
        if (excelColumn != null) {
            ExcelColumnGroupBuilder grpBuilder = new ExcelColumnGroupBuilder();
            colData = grpBuilder.parseExcelColumnXML(data);
            ArrayList<Integer> viewSequence = container.getRetObj().getViewSequence();

            if (viewSequence == null || viewSequence.isEmpty()) {
                excelColumn.setData(colData, fromRow, toRow);
            } else {
                int rowCount = container.getRetObj().getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    refData.add(0);
                }
                for (int i = 0; i < colData.size(); i++) {
                    refData.set(viewSequence.get(i), colData.get(i));
                }

                excelColumn.setData(refData, fromRow, toRow);
            }

            //Update the runtime measure map in return object
            int rowCount = container.getRetObj().getRowCount();
            ArrayList<BigDecimal> newList = new ArrayList<BigDecimal>(rowCount);
            for (int i = 0; i < rowCount; i++) {
                Object obj = excelColumn.getData(i);
                double dblValue = 0;
                try {
                    dblValue = Double.parseDouble(obj.toString());
                } catch (Exception e) {
                }
                newList.add(new BigDecimal(dblValue));
            }
            container.getRetObj().setRuntimeMeasure(measId, newList);

            if (!(excelColumn.isColumnPersisted())) {
                DAO.insertRunTimeExcelColumn(container, excelColumn);
            }
            int rptId = Integer.parseInt(container.getReportId());
            DAO.insertExcelColumns(container, rptId, true);
        }

        if (container.isTargetEntryApplicable() && container.isTargetColumn(measId)) {
            //Enter the data into the target table as well
            ProgenDataSet retObj = container.getRetObj();
            StringBuilder targetData = new StringBuilder();
            String timeLevel = container.getTimeLevel();
            String actualMeasId = container.getDisplayColumns().get(col - 1);
            if (actualMeasId.startsWith("A_")) {
                actualMeasId = actualMeasId.replace("A_", "");
            }
            int rowCount = retObj.getRowCount();
            targetData.append("<target>");
            for (int i = 0; i < rowCount; i++) {
                String dimValue = retObj.getFieldValueString(i, 0);
                String targetValue = (String) refData.get(i);
                targetData.append("<data>");
                targetData.append("<period>").append(dimValue).append("</period>");
                targetData.append("<value>").append(targetValue).append("</value>");
                targetData.append("</data>");
            }
            targetData.append("</target>");
            ReportTableDAO tableDAO = new ReportTableDAO();
            tableDAO.insertTargetValues(actualMeasId, timeLevel, targetData.toString());
        }
    }

    public ArrayList<Integer> searchDataSet(ProgenDataSet retObj, SearchFilter searchFilter) {

        Object[][] data = retObj.retrieveDataBasedOnViewSeq(searchFilter.getFilterColumns());
        ArrayList<Integer> viewSeq = retObj.getViewSequence();

        DataSetFilter filter = new DataSetFilter();
        filter.setSearchFilter(searchFilter);
        filter.setData(data, viewSeq);
        viewSeq = filter.searchDataSet();

        ArrayList<String> topBtmCols = searchFilter.getTopBtmFilterColumns();


        int topBottomCount;


        ArrayList<Integer> currentViewSeq = retObj.getViewSequence();


        for (String topBtmColumn : topBtmCols) {
            SearchFilterColumn filterColumn = searchFilter.getFilterColumn(topBtmColumn);

            ArrayList<SortColumn> sortColumns = filterColumn.getSortColumns();

            SortColumn topBtmSortColumn;
            topBottomCount = (Integer) filterColumn.getValue();
            if (filterColumn.getCondition().equals("TOP")) {
                topBtmSortColumn = new SortColumn(topBtmColumn, SortOrder.DESCENDING);
            } else {
                topBtmSortColumn = new SortColumn(topBtmColumn, SortOrder.ASCENDING);
            }

            sortColumns.add(topBtmSortColumn);

            viewSeq = retObj.findTopBottom(sortColumns, topBottomCount);
            retObj.setViewSequence(viewSeq);
        }
        retObj.setViewSequence(currentViewSeq);
        //viewSeq = retObj.findTopBottom(sortCols, sortTypes, sortDataTypes, topBottomCount);

        return viewSeq;
    }

    public void searchDataSet(Container container) {
        ArrayList<String> srchColumns;
        ArrayList<String> displayColumns;
        char[] colDataTypes;
        ProgenDataSet retObj = container.getRetObj();
        int rowCount = retObj.getRowCount();
retObj.groupColumns=container.getGroupColumns1();
        srchColumns = container.getSearchColumns();
        displayColumns = container.getDisplayColumns();
        if (srchColumns != null && !srchColumns.isEmpty()) {
            for (int i = 0; i < srchColumns.size(); i++) {
                if (displayColumns != null && !displayColumns.isEmpty() && !displayColumns.contains(srchColumns.get(i))) {
                    srchColumns.remove(i);
                }
            }
        }
        if (srchColumns != null && !srchColumns.isEmpty()) {
            colDataTypes = container.getColumnDataTypes(srchColumns);
            Object[][] data = new Object[rowCount][srchColumns.size()];
            data = retObj.retrieveData(srchColumns, colDataTypes);

            ArrayList<Integer> filteredSequence = null;
            DataSetFilter filter;
            filter = new DataSetFilter();
            filter.setData(data, retObj.getViewSequence());

            filteredSequence = filter.searchDataSet(container.getSearchConditions(), container.getSearchValues());
            retObj.setViewSequence(filteredSequence);
            rowCount = filteredSequence.size();
            if (Integer.parseInt(container.getPagesPerSlide()) > rowCount) {
                container.setPagesPerSlide(((Integer) rowCount).toString());
            }

        }

        ArrayList<String> srchConditions = container.getSearchConditions();
        ArrayList<String> srchColumn = container.getSearchColumns();
        ArrayList<Object> srchValue = container.getSearchValues();
        ArrayList<String> sortCols = container.getSortColumns(); //bhargavi
        int index = 0;
        String topBtmType = null;
        boolean doFilter;
        int topBottomCount = 0;
        String topBtmMode = null;
        String sortType = "0";

        //start of code by bhargavi for sorting
        if (!sortCols.isEmpty()) {
            ArrayList<SortColumn> sortColumns = new ArrayList<SortColumn>();
            ArrayList<Integer> rowSequence;
            char[] sortDataTypes = null;
//sortTypes = container.getSortTypes();
            sortDataTypes = container.getSortDataTypes();
           sortCols = container.getSortColumns();
            char[] sortTypes = container.getSortTypes();
            SortColumn sortColumn;
            int counter = 0;
            for (String column : sortCols) {
                if (sortTypes[counter] == '0') {
                    sortColumn = new SortColumn(column, SortOrder.ASCENDING);
                } else {
                    sortColumn = new SortColumn(column, SortOrder.DESCENDING);
                }
                sortColumns.add(sortColumn);
                counter++;
                rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);
                retObj.setViewSequence(rowSequence);
            }
        }

        for (String condition : srchConditions) {

            doFilter = false;

            if ("TOP".equals(condition) || "BTM".equals(condition)) {
                doFilter = true;
                if (srchValue.get(index) instanceof String) {
                    topBtmMode = ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE;
                    String srchValueString = srchValue.get(index).toString();
                    srchValueString = srchValueString.substring(0, srchValueString.length() - 1);
                    topBottomCount = Integer.parseInt(srchValueString);
                } else {
                    topBtmMode = ContainerConstants.TOP_BOTTOM_MODE_ABSOLUTE;
                    topBottomCount = ((BigDecimal) srchValue.get(index)).intValue();

                }
                if ("TOP".equals(condition)) {
                    topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS;
                    //added by sruhti for logical operation after sorting and grouping
//                    String groupid = container.getGroupColumns().toString().replace("[", "").replace("]", "");
//                    ArrayList<String> sortColsid = null;
//                    char[] sortTypesid = null;
//                    if (groupid != null && !groupid.toString().isEmpty()) {
//                        sortColsid = container.getExplicitSortColumns();
//                        sortTypesid = container.getExplicitSortTypes();
//                        if (sortColsid.contains(srchColumn.get(index))) {
//                            int indx = sortColsid.indexOf(srchColumn.get(index));
//                            String sort = sortTypesid[indx] + "";
//                            sortType = sort;
//                        } else {
//                            sortType = "1";
//                        }
//                    } else //ended by sruthi
//                    {
                        sortType = "1";
                   // }
                } else {
                    topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS;
                    //added by sruhti for logical operation after sorting and grouping
//                    String groupid = container.getGroupColumns().toString().replace("[", "").replace("]", "");
//                    ArrayList<String> sortColsid = null;
//                    char[] sortTypesid = null;
//                    if (groupid != null && !groupid.toString().isEmpty()) {
//                        sortColsid = container.getExplicitSortColumns();
//                        sortTypesid = container.getExplicitSortTypes();
//                        if (sortColsid.contains(srchColumn.get(index))) {
//                            int indx = sortColsid.indexOf(srchColumn.get(index));
//                            String sort = sortTypesid[indx] + "";
//                            sortType = sort;
//                        } else {
//                            sortType = "0";
//                        }
//                    } else //ended by sruthi
//                    {
                        sortType = "0";
                  //  }
                }
            }
            if (doFilter) {
                container.setSortColumnTopBottom(srchColumn.get(index), sortType);
                container.setTopBottomColumn(topBtmType, topBtmMode, topBottomCount);

                ArrayList<SortColumn> sortColumns = new ArrayList<SortColumn>();


                sortCols = container.getSortColumns(); //commented by bhargavi
                char[] sortTypes = container.getSortTypes();
                SortColumn sortColumn;
                int counter = 0;
                for (String column : sortCols) {
                    if (sortTypes[counter] == '0') {
                        sortColumn = new SortColumn(column, SortOrder.ASCENDING);
                    } else {
                        sortColumn = new SortColumn(column, SortOrder.DESCENDING);
                    }
                    sortColumns.add(sortColumn);
                    counter++;
                     if (retObj.groupColumns!= null && !retObj.groupColumns.toString().isEmpty()) {
                        ArrayList<String> sortCols1 = null;
                         ArrayList<Integer> rowSequence;
                        char[] sortDataTypes1 = null;
                        char[] sortTypes1=null;
                                sortDataTypes1 = container.getColumnDataTypes(sortCols);
                                  sortTypes1 = container.getExplicitSortTypes();
                                ArrayList<Integer> sortedrowSequence = retObj.getViewSequence();
                                retObj.search=container.getsearch();
                                if(sortTypes1!=null){
                                rowSequence = retObj.sortDataSetForSubTotal(sortCols, sortTypes1, sortDataTypes1, container);
                                retObj.setViewSequence(rowSequence);
                                }
                    }
                }

                ArrayList<Integer> viewSeq;
                if (topBtmMode.equals(ContainerConstants.TOP_BOTTOM_MODE_ABSOLUTE)) {
                    viewSeq = retObj.findTopBottom(sortColumns, topBottomCount);
                } else {
                    viewSeq = retObj.findTopBottomPercentWise(sortColumns, topBottomCount);
                }
                retObj.setViewSequence(viewSeq);
            }
            //start of code by bhargavi for sorting
            if (!sortCols.isEmpty()) {
                ArrayList<SortColumn> sortColumns = new ArrayList<SortColumn>();
                ArrayList<Integer> rowSequence;
                char[] sortDataTypes = null;
//sortTypes = container.getSortTypes();
                sortDataTypes = container.getSortDataTypes();
                  sortCols = container.getSortColumns();
                char[] sortTypes = container.getSortTypes();
                SortColumn sortColumn;
                int counter = 0;
                for (String column : sortCols) {
                    if (sortTypes[counter] == '0') {
                        sortColumn = new SortColumn(column, SortOrder.ASCENDING);
                    } else {
                        sortColumn = new SortColumn(column, SortOrder.DESCENDING);
                    }
                    sortColumns.add(sortColumn);
                    counter++;
                    
                     //added by sruthi for grouping,sorting and top bottom
//                    if (retObj.groupColumns!= null && !retObj.groupColumns.toString().isEmpty()) {
//                        ArrayList<String> sortCols1 = null;
//                        char[] sortDataTypes1 = null;
//                                sortDataTypes1 = container.getColumnDataTypes(sortCols);
//                                ArrayList<Integer> sortedrowSequence = retObj.getViewSequence();
//                                rowSequence = retObj.sortDataSetForSubTotal(sortCols, sortTypes, sortDataTypes1, container);
//                    }else{
                    rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);
                  //  }
                    //ended by sruthi
                    retObj.setViewSequence(rowSequence);
                }
            }
            //end of code by bhargavi for sorting
            index++;
        }
    }

    public void findTopBottom() {
    }

    public void exportRTExcelColumns(Container container, OutputStream out) {
        FileOutputStream fos = null;

        ProgenDataSet retObj = container.getRetObj();
        HSSFWorkbook wb = new HSSFWorkbook();

        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
        List<RunTimeExcelColumn> rtColList = excelColGroup.getRunTimeColumnList();
        int noOfRows = retObj.getRowCount() + 1;    // No of rows plus the column header
        int noOfCols = rtColList.size() + 1;    //Number of runtime columns plus the row viewby column
        Short boldWt = 10;
        int viewByCount = (container.getViewByCount());

        if (rtColList != null && rtColList.size() > 0) {
            HSSFSheet sheet = wb.createSheet("Sheet1");
            HSSFRow row = sheet.createRow(0);
            HSSFFont font = wb.createFont();
            font.setBoldweight(boldWt);

            //Add Column Heading
            ArrayList displayLabels = container.getDisplayLabels();
            ArrayList<String> displayColumns = container.getDisplayColumns();

            //Create the header row content for rowviewbys
            for (int i = 0; i < viewByCount; i++) {
                HSSFCell cell = row.createCell(i);
                String dimHdr = (String) displayLabels.get(i);
                HSSFRichTextString rtStr = new HSSFRichTextString(dimHdr);
                rtStr.applyFont(font);
                cell.setCellValue(rtStr);
            }

            //Create the header row content for measures
            for (int i = 0; i < rtColList.size(); i++) {
                String measId = rtColList.get(i).getMeasureId();
                int index = displayColumns.indexOf(measId);
                String measLbl = (String) displayLabels.get(index);
                HSSFRichTextString rtStr = new HSSFRichTextString(measLbl);
                rtStr.applyFont(font);
                HSSFCell cell = row.createCell(i + viewByCount);
                cell.setCellValue(rtStr);
            }

            //Add Data
            for (int i = 1; i < noOfRows; i++) {
                row = sheet.createRow(i);
                for (int j = 0; j < noOfCols; j++) {
                    HSSFCell cell = row.createCell(j);

                    if (j < viewByCount) {  //Add dimension data
                        HSSFRichTextString str = new HSSFRichTextString(retObj.getFieldValueString(i - 1, j));
                        cell.setCellValue(str);
                    } else {  //Get the runtime data and populate
                        Object val = rtColList.get(j - viewByCount).getData(i - 1);
                        try {
                            Double d = Double.parseDouble(val.toString());
                            cell.setCellValue(d);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }

        try {
            wb.write(out);
        } catch (IOException e) {
            logger.error("Exception:", e);
        }
    }

    public boolean importRTExcelColumns(Container container, InputStream fileInputStream) {
        boolean success = true;
        HSSFWorkbook wb = null;
        ProgenDataSet retObj = container.getRetObj();
        ArrayList<String> displayCols = container.getDisplayColumns();
        ArrayList displayLbls = container.getDisplayLabels();

        //Create the WorkBook object from the excel file
        try {
            //FileInputStream fis = new FileInputStream("/home/progen/Desktop/TestSpreadsheeet.xls");
            wb = new HSSFWorkbook(fileInputStream);
            //fis.close();
        } catch (IOException e) {
            logger.error("Exception:", e);
        }

        if (wb != null) {
            ExcelColumnGroup colGroup = container.getExcelColumnGroup();
            List<RunTimeExcelColumn> rtCols = colGroup.getRunTimeColumnList();
            HSSFSheet sheet = wb.getSheetAt(0);

            int rowCount = retObj.getRowCount();
            int excelRows = sheet.getLastRowNum();
            int rowViewByCount = (container.getViewByCount());
            int colCount = container.getRtExcelColumns().size() + rowViewByCount;

            if (excelRows > 0) {    //Proceed only if the number of rows in excel is greater than zero

                Map<String, ArrayList> rtData = new HashMap<String, ArrayList>();
                List<String> importMeasureNameList = new ArrayList<String>();
                List<String> importMeasureIdList = new ArrayList<String>();

                //Populate the import measure List
                Iterator<Row> rowIter = sheet.rowIterator();
                Row r = rowIter.next();
                int index = -1;
                for (Iterator<Cell> cellIter = r.cellIterator(); cellIter.hasNext();) {
                    Cell c = cellIter.next();
                    index++;
                    if (index < rowViewByCount) {
                        continue;
                    }
                    String measName = c.getRichStringCellValue().toString();
                    int measIndex = displayLbls.indexOf(measName);
                    if (measIndex < 0) {
                        success = false;
                        break;
                    }
                    String measId = displayCols.get(measIndex);

                    importMeasureIdList.add(measId);
                    importMeasureNameList.add(measName);

                    ArrayList data = new ArrayList(rowCount);
                    for (int i = 0; i < rowCount; i++) {
                        data.add(null);
                    }
                    rtData.put(measId, data);
                }

                if (!success) {
                    return success;
                }

                //Get the data from the excel file
                while (rowIter.hasNext()) {
                    Row row = rowIter.next();
                    int colsInRow = row.getLastCellNum();

                    //Check the number of columns in the uploaded file and make sure it is in sync with the container data
                    if (colsInRow > colCount) {
                        success = false;
                        break;
                    } else {
                        int rowIndex = -1;
                        List<String> dimValues = new ArrayList<String>();
                        for (Iterator<Cell> cellIter = row.cellIterator(); cellIter.hasNext();) {
                            Cell cell = cellIter.next();

                            int colIndex = cell.getColumnIndex();

                            // Dimension Data. Get the row id of the element from return object. This 
                            // will be used for storing data in runtime excel columns
                            if (colIndex < rowViewByCount) {
                                String excelData = cell.getRichStringCellValue().toString();
                                dimValues.add(excelData);
                                if (dimValues.size() == rowViewByCount) {
                                    for (int i = 0; i < retObj.getRowCount(); i++) {
                                        int j;
                                        for (j = 0; j < dimValues.size(); j++) {
                                            String temp = retObj.getFieldValueString(i, j);
                                            if (!(temp.equalsIgnoreCase(dimValues.get(j)))) {
                                                break;
                                            }
                                        }
                                        if (j == dimValues.size()) {
                                            rowIndex = i;
                                        }
                                    }

                                    //Clear the measure data in the row Index for all the measures
                                    if (rowIndex != -1) {
                                        Set<String> keySet = rtData.keySet();
                                        Iterator<String> iter = keySet.iterator();
                                        while (iter.hasNext()) {
                                            String key = iter.next();
                                            ArrayList lst = rtData.get(key);
                                            lst.set(rowIndex, "");
                                        }
                                    }
                                }
                            } else {
                                colIndex -= rowViewByCount;
                                String measId = importMeasureIdList.get(colIndex);
                                if (rowIndex != -1) {
                                    Object obj;
                                    if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                        obj = cell.getNumericCellValue();
                                    } else {
                                        obj = "";
                                    }
                                    rtData.get(measId).set(rowIndex, obj);
                                }
                            }
                        }
                    }
                }

                if (!success) {
                    return success;
                }

                //Set the excel column data in container and retObj.rtMeasMap
                List<String> rtColNames = container.getRtExcelColumns();
                for (String measId : rtColNames) {
                    if (rtData.containsKey(measId)) {
                        //Set the data in RunTime ExcelColumn
                        RunTimeExcelColumn rtCol = colGroup.getRunTimeColumn(measId);
                        ArrayList data = rtData.get(measId);
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i) != null) {
                                rtCol.setData(data.get(i), i);
                            }
                        }

                        ArrayList<BigDecimal> newList = new ArrayList<BigDecimal>(rowCount);
                        for (int i = 0; i < rowCount; i++) {
                            newList.add(BigDecimal.ZERO);
                        }

                        for (int i = 0; i < rowCount; i++) {
                            Object obj = rtCol.getData(i);
                            double dblValue = 0;
                            try {
                                dblValue = Double.parseDouble(obj.toString());
                            } catch (Exception e) {
                            }
                            newList.set(i, new BigDecimal(dblValue));
                        }

                        container.getRetObj().setRuntimeMeasure(measId, newList);
                    }
                }
            }
        }
        return success;
    }

    public ArrayList<Integer> searchDataSet(CreateKPIFromReport createKPIFromReport, PbReturnObject returnObj) {
        ArrayList<String> srchColumns = new ArrayList<String>();
        ArrayList<String> srchConditions = new ArrayList<String>();
        ArrayList<Object> srchValue = new ArrayList<Object>();
        Object[] srchCols = createKPIFromReport.getSrchColumn();
        Object[] srchCnds = createKPIFromReport.getSrchConditions();
        Object[] srchVals = createKPIFromReport.getSrchValue();
        for (int i = 0; i < srchCnds.length; i++) {
            srchColumns.add((String) srchCols[i]);
            srchConditions.add((String) srchCnds[i]);
            srchValue.add(srchVals[i]);
        }
        char[] colDataTypes;
        ProgenDataSet retObj = returnObj;
        int rowCount = retObj.getRowCount();
        ArrayList<Integer> filteredSequence = null;

        if (srchColumns != null && !srchColumns.isEmpty()) {
            colDataTypes = createKPIFromReport.getColDataTypes();
            Object[][] data = new Object[rowCount][srchColumns.size()];
            data = retObj.retrieveData(srchColumns, colDataTypes);


            DataSetFilter filter;
            filter = new DataSetFilter();
            filter.setData(data, retObj.getViewSequence());

            filteredSequence = filter.searchDataSet(srchConditions, srchValue);
            retObj.setViewSequence(filteredSequence);
            rowCount = filteredSequence.size();


        }

        return filteredSequence;

    }
    // Added for exclude zero by Amar

    public void searchDataSetToExcludeZero(Container container) {
        ArrayList<String> srchColumns;
        ArrayList<String> displayColumns;
        char[] colDataTypes;
        ProgenDataSet retObj = container.getRetObj();
        int rowCount = retObj.getRowCount();
        srchColumns = container.getSearchColumns();
        displayColumns = container.getDisplayColumns();
        if (srchColumns != null && !srchColumns.isEmpty()) {
            for (int i = 0; i < srchColumns.size(); i++) {
                if (displayColumns != null && !displayColumns.isEmpty() && !displayColumns.contains(srchColumns.get(i))) {
                    srchColumns.remove(i);
                }
            }
        }
        if (srchColumns != null && !srchColumns.isEmpty()) {
            colDataTypes = container.getColumnDataTypes(srchColumns);
            Object[][] data = new Object[rowCount][srchColumns.size()];
            data = retObj.retrieveData(srchColumns, colDataTypes);
            ArrayList<Integer> filteredSequence = null;
            DataSetFilter filter;
            filter = new DataSetFilter();
            filter.setData(data, retObj.getViewSequence());
            filteredSequence = filter.searchDataSet(container.getSearchConditions(), container.getSearchValues());
            retObj.setViewSequence(filteredSequence);
            rowCount = filteredSequence.size();
            if (Integer.parseInt(container.getPagesPerSlide()) > rowCount) {
                container.setPagesPerSlide(((Integer) rowCount).toString());
            }
        }
    }
    // end of code
}
