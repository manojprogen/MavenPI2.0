/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.bd;

import com.progen.db.POIDataSet;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.report.ReportParameter;
import com.progen.report.db.ReportTableDAO;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.excel.ExcelCellFormat;
import com.progen.report.excel.ExcelCellFormatGroup;
import com.progen.report.excel.ExcelColumnGroup;
import com.progen.report.excel.RunTimeExcelColumn;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class ExcelBD {

    public static Logger logger = Logger.getLogger(ExcelBD.class);

    public void storeComment(String userId, Container container, int row, int col, String commentTxt) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();
        String measId = container.getDisplayColumns().get(col);
        ExcelCellFormatGroup excelGroup = container.getExcelCellGroup();
        ExcelCellFormat excelCell = excelGroup.createExcelCell(row, measId, repParam);
        excelCell.setComment(commentTxt);

        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        dataSet.addComment(userId, row, col, commentTxt);
    }

    public void clearComment(Container container, int row, int col) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();
        String measId = container.getDisplayColumns().get(col);
        ExcelCellFormatGroup excelGroup = container.getExcelCellGroup();
        ExcelCellFormat excelCell = excelGroup.getExcelCell(row, measId, repParam);
        if (excelCell != null) {
            excelCell.setComment(null);
        }

        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        dataSet.clearComment(row, col);
    }

    public void storeFormat(Container container, int row, int col, String bgColor, String fontColor) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();
        String measId = container.getDisplayColumns().get(col);
        ExcelCellFormatGroup excelGroup = container.getExcelCellGroup();
        ExcelCellFormat excelCell = excelGroup.createExcelCell(row, measId, repParam);
        excelCell.setBgColor(bgColor);
        excelCell.setFontColor(fontColor);

        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        dataSet.addFormat(row, col, bgColor, fontColor);
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
        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        dataSet.clearFormat(row, col);

    }

    public void addExcelColumn(Container container, int col, String measId, String colName) {
        ReportParameter repParam = (ReportParameter) container.getReportParameter().clone();

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

        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        dataSet.addColumn(col, measId);
    }

    public String updateValue(Container container, int row, int col, String cellAddress, String formulaVal) {
        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        String retVal = dataSet.addValue(row, col, cellAddress, formulaVal);
//        BigDecimal grandTotal=BigDecimal.ZERO;
        String json = "";
        if (formulaVal.startsWith("=")) {
            formulaVal = formulaVal.toUpperCase();
            formulaVal = dataSet.changeToPOIFormula(formulaVal);
        }
        ReportParameter repParam = container.getReportParameter();
        String measId = container.getDisplayColumns().get(col);
        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
//        RunTimeExcelColumn excelColumn = excelColGroup.getRunTimeColumn(measId);
        RunTimeExcelColumn excelColumn = excelColGroup.getRunTimeColumn(measId, (ReportParameter) repParam.clone());

        if (excelColumn != null) {
            excelColumn.setData(formulaVal, row);

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
            json = displayFormula(container, row, col, cellAddress, formulaVal, newList);
            dataSet.setRuntimeMeasure(measId, newList);

        }

        return retVal + "~" + json;
    }

    public String displayRTColumnAggregations(int fromRow, int rowCount, String colName) {
//        String formula="";
        StringBuilder formula = new StringBuilder();
        for (int i = fromRow; i < rowCount; i++) {
            formula.append(colName + (i + 1));
//            formula+=colName+(i+1);
            if (i != rowCount - 1) {
//                formula+="+";
                formula.append("+");
            }
        }
        return formula.toString();
    }

    public String displayFormula(Container container, int row, int col, String cellAddress, String formulaVal, ArrayList<BigDecimal> rtMeasLst) {
        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        String measId = container.getDisplayColumns().get(col);
//        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
//        RunTimeExcelColumn excelColumn = excelColGroup.getRunTimeColumn(measId);

//        String modifiedST="";
//        String modifiedGT="";
        int subTotalRow = 0;
//        int grandTotRow=0;
        String colName = "";
//        String modifiedCat="";
        if (RTMeasureElement.isRunTimeExcelColumn(measId)) {

            List<Integer> subTotalRows = container.getSubTotRows().get(measId);
//            subTotalRows.add(3);
//            subTotalRows.add(7);
            String actualAddress = dataSet.getActualCellAddress(cellAddress);
            colName = String.valueOf(actualAddress.charAt(0));
            int j = 0;
            String formula = "";
            int actualRow = dataSet.getDisplayRow(row);
            ArrayList<String> displayCellAddr = new ArrayList<String>();
            for (int i = 0; i < subTotalRows.size() - 1; i++) {
                displayCellAddr.clear();
                formula = "";
                if (i == 0) {
                    j = 1;
                } else {
                    j = subTotalRows.get(i - 1);
                }

                for (; j < subTotalRows.get(i) - 1; j++) {
                    formula += "+" + colName + (j + 1);
                    displayCellAddr.add(colName + (j + 1));
                }

                if (subTotalRows.get(i) > actualRow) {
                    subTotalRow = subTotalRows.get(i);
                    if (!formula.equalsIgnoreCase("")) {
                        formula = formula.substring(1);
                        formula = "=" + formula;
                    }
                    break;
                }
            }
//          dataSet.addValue(row, col, cellAddress, formula);
//         grandTotRow=subTotalRows.get(subTotalRows.size()-2   );
            TreeMap gtMap = dataSet.getColumnGrandTotals();

            int displayRowCount = Integer.parseInt(container.getPagesPerSlide());
            if (displayRowCount > dataSet.getRowCount()) {
                displayRowCount = dataSet.getRowCount();
            }
            double subTotal = dataSet.evaluateFormula(formula, row, col, actualAddress, false);
            displayCellAddr.clear();
            String actualFormula = displayRTColumnAggregations(container.getFromRow(), displayRowCount, colName);
            double catSubTotal = dataSet.evaluateFormula(actualFormula, row, col, actualAddress, true);
            actualFormula = displayRTColumnAggregations(0, container.getRetObj().getRowCount(), colName);
            double grandTotal = dataSet.evaluateFormula(actualFormula, row, col, actualAddress, true);
            gtMap.put(measId, new BigDecimal(grandTotal));
            dataSet.setColumnGrandTotals(gtMap);
            return buildJsonForDisplayRTColumnAggregations(subTotalRow, subTotal, grandTotal, colName, measId, catSubTotal, rtMeasLst, dataSet.getViewSequence());
        }
        return "";
    }

    public String buildJsonForDisplayRTColumnAggregations(int subTotRow, double subTotValue, double grandTotValue, String cellName, String measId, double catSubTot, ArrayList<BigDecimal> measData, ArrayList<Integer> viewSeq) {
        StringBuilder builder = new StringBuilder();

        String modifiedMax = NumberFormatter.getModifiedNumber(getOverallMax(measData), "", -1);
        String modifiedMin = NumberFormatter.getModifiedNumber(getOverallMin(measData), "", -1);
        String modifiedAvg = NumberFormatter.getModifiedNumber(getAverage(measData, new BigDecimal(grandTotValue)), "", -1);
        String modifidCatMax = NumberFormatter.getModifiedNumber(getCategoryMax(measData, viewSeq), "", -1);
        String modifidCatMin = NumberFormatter.getModifiedNumber(getCategoryMin(measData, viewSeq), "", -1);

        String modifiedST = NumberFormatter.getModifiedNumber(subTotValue, "", -1);
        String modifiedGT = NumberFormatter.getModifiedNumber(grandTotValue, "", -1);
        String modifiedCat = NumberFormatter.getModifiedNumber(catSubTot, "", -1);

        String gtCellId = measId + "_GT";
        String subTotCellId = measId + "_ST_" + cellName + subTotRow;
        String catSubTotCellId = measId + "_CATST";
        String overallMaxId = measId + "_OVEMAX";
        String catMaxId = measId + "_CATMAX";
        String overallMinId = measId + "_OVEMIN";
        String catMinId = measId + "_CATMIN";
        String avgId = measId + "_AVG";

        builder.append("{ \"SubTotal\":[ { \"address\":").append("\"").append(subTotCellId).append("\",");
        builder.append(" \"value\":").append("\"").append(modifiedST).append("\" } ],");

        builder.append(" \"GrandTotal\":[ { \"address\":").append("\"").append(gtCellId).append("\",");
        builder.append(" \"value\":").append("\"").append(modifiedGT).append("\" } ] ,");

        builder.append(" \"CategoryTotal\":[ { \"address\":").append("\"").append(catSubTotCellId).append("\",");
        builder.append(" \"value\":").append("\"").append(modifiedCat).append("\" } ] ,");

        builder.append(" \"CategoryMax\":[ { \"address\":").append("\"").append(catMaxId).append("\",");
        builder.append(" \"value\":").append("\"").append(modifidCatMax).append("\" } ] ,");

        builder.append(" \"CategoryMin\":[ { \"address\":").append("\"").append(catMinId).append("\",");
        builder.append(" \"value\":").append("\"").append(modifidCatMin).append("\" } ] ,");

        builder.append(" \"OverallMax\":[ { \"address\":").append("\"").append(overallMaxId).append("\",");
        builder.append(" \"value\":").append("\"").append(modifiedMax).append("\" } ] ,");

        builder.append(" \"OverallMin\":[ { \"address\":").append("\"").append(overallMinId).append("\",");
        builder.append(" \"value\":").append("\"").append(modifiedMin).append("\" } ] ,");

        builder.append(" \"Average\":[ { \"address\":").append("\"").append(avgId).append("\",");
        builder.append(" \"value\":").append("\"").append(modifiedAvg).append("\" } ] }");
        return builder.toString();
    }

    public BigDecimal getOverallMax(ArrayList<BigDecimal> measValLst) {
        BigDecimal max = BigDecimal.ZERO;
        for (BigDecimal val : measValLst) {
            max = max.max(val);
        }
        return max;
    }

    public BigDecimal getOverallMin(ArrayList<BigDecimal> measValLst) {
        BigDecimal min = BigDecimal.ZERO;
        for (BigDecimal val : measValLst) {
            min = min.min(val);
        }
        return min;
    }

    public BigDecimal getAverage(ArrayList<BigDecimal> measValLst, BigDecimal grandTotal) {
        BigDecimal average = new BigDecimal(0);
        average = grandTotal.divide(new BigDecimal(measValLst.size()), MathContext.DECIMAL64);
        return average;
    }

    public BigDecimal getCategoryMax(ArrayList<BigDecimal> measValLst, ArrayList<Integer> viewSequence) {
        BigDecimal catMax = new BigDecimal(0);
        for (int i = 0; i < viewSequence.size(); i++) {
            catMax = catMax.max(measValLst.get(viewSequence.get(i)));
        }
        return catMax;
    }

    public BigDecimal getCategoryMin(ArrayList<BigDecimal> measValLst, ArrayList<Integer> viewSequence) {
        BigDecimal catMin = new BigDecimal(0);
        for (int i = 0; i < viewSequence.size(); i++) {
            catMin = catMin.min(measValLst.get(viewSequence.get(i)));
        }
        return catMin;
    }

    public String clearValue(Container container, int row, int col, String address) {
        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        String retVal = dataSet.clearValue(row, col, address);

        String measId = container.getDisplayColumns().get(col);
        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
        RunTimeExcelColumn excelColumn = excelColGroup.getRunTimeColumn(measId);

        if (excelColumn != null) {
            excelColumn.setData("", row);

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
        }
        return retVal;
    }

    public void copyCells(Container container, String address) {
        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        dataSet.copyCells(address);
    }

    public String pasteCells(Container container, String address) {
        POIDataSet dataSet = (POIDataSet) container.getRetObj();
        String retVal = dataSet.pasteCells(address);
        return retVal;
    }

    public void updateExcelColumns(Container container) {
        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
        List<String> excelCols = container.getRtExcelColumns();
        for (String measId : excelCols) {
            RunTimeExcelColumn excelColumn = excelColGroup.getRunTimeColumn(measId);
            ArrayList refData = excelColumn.getData();

            ReportTemplateDAO DAO = new ReportTemplateDAO();
            if (!(excelColumn.isColumnPersisted())) {
                DAO.insertRunTimeExcelColumn(container, excelColumn);
            }
            int rptId = Integer.parseInt(container.getReportId());
            DAO.insertExcelColumns(container, rptId, true);

            //Enter the data into the target table as well
            if (container.isTargetEntryApplicable() && container.isTargetColumn(measId)) {
                int measIndex = container.getDisplayColumns().indexOf(measId);
                ProgenDataSet retObj = container.getRetObj();
                StringBuilder targetData = new StringBuilder();
                String timeLevel = container.getTimeLevel();
                String actualMeasId = container.getDisplayColumns().get(measIndex - 1);
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
    }

    public void exportRTExcelColumns(Container container, OutputStream out) {
        FileOutputStream fos = null;

        ProgenDataSet retObj = container.getRetObj();
        HSSFWorkbook wb = new HSSFWorkbook();

        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
        List<String> excelCols = container.getRtExcelColumns();
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
//                        Object val = rtColList.get(j-viewByCount).getData(i-1);
                        Object val = retObj.getFieldValue(i - 1, excelCols.get(j - viewByCount));
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

                        ((POIDataSet) container.getRetObj()).setExcelRuntimeMeasure(measId, data);
                    }

                }
            }
        }
        return success;
    }
}