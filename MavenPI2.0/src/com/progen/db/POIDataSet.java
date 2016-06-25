/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.db;

import com.google.common.collect.ArrayListMultimap;
import com.progen.query.RTDimensionElement;
import com.progen.query.RTMeasureElement;
import com.progen.query.RunTimeMeasure;
import com.progen.report.data.DataFacade;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.excel.ExcelCell;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class POIDataSet extends ProgenDataSet {

    public static Logger logger = Logger.getLogger(POIDataSet.class);
    private HSSFWorkbook workbook = new HSSFWorkbook();
    public ArrayList<String> cols = null;
    //Stores display row and actual row mapping
    private Map<Integer, Integer> rowMapping = new LinkedHashMap<Integer, Integer>();
    private Map<String, String> displayFormula = new HashMap<String, String>();
    private ArrayListMultimap<String, String> formulaDependency = ArrayListMultimap.create();
    FormulaEvaluator evaluator;
    HSSFSheet sheet;
    List<String> copyCells = new ArrayList<String>();
    private List<Integer> excColumnTypesInt;
    private List<Integer> excColumnSizes;
    private List<String> excColumnTypes;
    private List<BigDecimal> excGrandTotals;
    private List<BigDecimal> excAvgTotals;
    private List<BigDecimal> excMax;
    private List<BigDecimal> excMin;

    public POIDataSet(ResultSet rset) {
        prepareObject(rset);
    }

    public POIDataSet(ResultSet rset, boolean processGT) {
        this.processGT = processGT;
        prepareObject(rset);
    }

    public void prepareObject(ResultSet rset) {
        try {
            int size = 0;
            if (rset.getType() == ResultSet.TYPE_SCROLL_INSENSITIVE) {
                while (rset.next()) {
                    size++;
                }
            }
            ResultSetMetaData meta = rset.getMetaData();
            colCount = meta.getColumnCount();
            cols = new ArrayList<String>(colCount);
            columnTypes = new String[colCount];

            excColumnTypesInt = new ArrayList<Integer>(colCount);
            excColumnSizes = new ArrayList<Integer>(colCount);
            excColumnTypes = new ArrayList<String>(colCount);
            excGrandTotals = new ArrayList<BigDecimal>(colCount);
            excAvgTotals = new ArrayList<BigDecimal>(colCount);
            excMax = new ArrayList<BigDecimal>(colCount);
            excMin = new ArrayList<BigDecimal>(colCount);

            columnTypesInt = new Integer[colCount];
            columnSizes = new int[colCount];
            grandTotals = new BigDecimal[colCount];
            avgTotals = new BigDecimal[colCount];
            max = new BigDecimal[colCount];
            min = new BigDecimal[colCount];

            columnOverAllMaximums = new TreeMap();
            columnOverAllMinimums = new TreeMap();
            columnAverages = new TreeMap();
            columnGrandTotals = new TreeMap();
            rowGrandTotals = new TreeMap();

            for (int i = 0; i < colCount; i++) {
                if (rset.getType() == ResultSet.TYPE_SCROLL_INSENSITIVE) {
                    hMap.put(meta.getColumnName(i + 1), new ArrayList(size));
                } else {
                    hMap.put(meta.getColumnName(i + 1), new ArrayList());
                }

                cols.add(meta.getColumnName(i + 1));

                columnTypes[i] = meta.getColumnTypeName(i + 1);
                columnTypesInt[i] = meta.getColumnType(i + 1);
                columnSizes[i] = meta.getColumnDisplaySize(i + 1);
                grandTotals[i] = BigDecimal.ZERO;
                avgTotals[i] = BigDecimal.ZERO;
                max[i] = BigDecimal.ZERO;
                min[i] = BigDecimal.ZERO;

                excColumnTypesInt.add(meta.getColumnType(i + 1));
                excColumnSizes.add(meta.getColumnDisplaySize(i + 1));
                excColumnTypes.add(meta.getColumnTypeName(i + 1));
                excGrandTotals.add(BigDecimal.ZERO);
                excAvgTotals.add(BigDecimal.ZERO);
                excMax.add(BigDecimal.ZERO);
                excMin.add(BigDecimal.ZERO);
            }
            HSSFSheet sheet1 = workbook.createSheet();
            this.sheet = sheet1;
            int rowNum = 0;
            while (rset.next()) {
                HSSFRow row = sheet1.createRow(rowNum);
                rowNum++;
                BigDecimal RowGrandTotal = BigDecimal.ZERO;
                for (int i = 0; i < colCount; i++) {
                    HSSFCell cell = row.createCell(i);

                    if (meta.getColumnType(i + 1) == Types.BLOB) {
//                        ((ArrayList) (hMap.get(cols[i]))).add(rset.getBlob(cols[i]));
                        cell.setCellValue(rset.getBlob(cols.get(i)).toString());
                    } else if (meta.getColumnType(i + 1) == Types.CLOB) {
//                        ((ArrayList) (hMap.get(cols[i]))).add(rset.getClob(cols[i]));
                        cell.setCellValue(rset.getClob(cols.get(i)).toString());
                    } else if (meta.getColumnType(i + 1) == Types.DATE) {
//                        ((ArrayList) (hMap.get(cols[i]))).add(rset.getTimestamp(cols[i]));
                        cell.setCellValue(rset.getTimestamp(cols.get(i)));
                    } else if (meta.getColumnType(i + 1) == Types.VARCHAR) {
                        cell.setCellValue(rset.getString(cols.get(i)));
                    } else {
                        Object Obj = rset.getObject(cols.get(i));
//                        ((ArrayList) (hMap.get(cols[i]))).add(Obj);
                        BigDecimal bdecimal = null;
                        if (Obj != null) {
                            bdecimal = rset.getBigDecimal(cols.get(i));
                        } else {
                            bdecimal = BigDecimal.ZERO;
                        }
                        cell.setCellValue(bdecimal.doubleValue());

                        if (processGT) {
                            if (meta.getColumnType(i + 1) == Types.BIGINT || meta.getColumnType(i + 1) == Types.DECIMAL || meta.getColumnType(i + 1) == Types.DOUBLE || meta.getColumnType(i + 1) == Types.FLOAT || meta.getColumnType(i + 1) == Types.INTEGER || meta.getColumnType(i + 1) == Types.NUMERIC || meta.getColumnType(i + 1) == Types.REAL || meta.getColumnType(i + 1) == Types.SMALLINT || meta.getColumnType(i + 1) == Types.TINYINT) {
                                //code to build max,min,acg and grand total of entire record set
                                if (rowCount == 0) {
                                    grandTotals[i] = bdecimal;
                                    max[i] = bdecimal;
                                    min[i] = bdecimal;

                                    excGrandTotals.set(i, bdecimal);
                                    excMax.set(i, bdecimal);
                                    excMin.set(i, bdecimal);
                                } else {
                                    grandTotals[i] = grandTotals[i].add(bdecimal);
                                    max[i] = max[i].max(bdecimal);
                                    min[i] = min[i].min(bdecimal);

                                    excGrandTotals.set(i, grandTotals[i]);
                                    excMax.set(i, max[i]);
                                    excMin.set(i, min[i]);
                                }
                                //code to buiold row wise grand total
                                if (i == 0) {
                                    RowGrandTotal = bdecimal;
                                } else {
                                    RowGrandTotal = RowGrandTotal.add(bdecimal);
                                }
                                bdecimal = null;
                            }
                        }
                    }
                }
                if (processGT) {
                    rowGrandTotals.put("RowGrandTotal_" + rowCount, RowGrandTotal);
                }
                rowCount++;
            }
            if (rowCount != 0 && processGT) {
                BigDecimal dividend = new BigDecimal(String.valueOf(rowCount));

                for (int colIndex = 0; colIndex < colCount; colIndex++) {
                    columnOverAllMaximums.put(cols.get(colIndex), max[colIndex]);
                    columnOverAllMinimums.put(cols.get(colIndex), min[colIndex]);
                    columnGrandTotals.put(cols.get(colIndex), grandTotals[colIndex]);
                    avgTotals[colIndex] = grandTotals[colIndex].divide(dividend, MathContext.DECIMAL64);
                    columnAverages.put(cols.get(colIndex), avgTotals[colIndex]);
                }
                dividend = null;
            }
            this.initializeViewSequence();

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public Object[][] retrieveData(ArrayList<String> sortColumns, char[] rowDataTypes) {
        Object cellData;
        Object[][] data = new Object[this.viewSequence.size()][cols.size()];
        int j;

        for (int i = 0; i < this.viewSequence.size(); i++) {
            j = 0;
            for (String column : sortColumns) {
                if (rowDataTypes[j] == 'N') {
                    cellData = this.getMeasureColumnData(column, this.viewSequence.get(i));
                } else {
                    cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                }
                //cellData = this.getFieldValueString(this.viewSequence.get(i),column);
                data[i][j] = cellData;
                j++;
            }
        }
        return data;
    }

    private BigDecimal getMeasureColumnData(String column, int row) {
        BigDecimal data;
        if (RTMeasureElement.isRunTimeMeasure(column)) {
            data = this.getFieldValueRuntimeMeasure(row, column);
        } else {
            data = this.getFieldValueBigDecimal(row, column);
        }
        return data;
    }

    private String getDimensionColumnData(String column, int row) {
        String data;
        if (RTDimensionElement.isRunTimeDimension(column)) {
            data = this.getFieldValueRuntimeDimension(row, column);
        } else {
            data = this.getFieldValueString(row, column);
        }
        return data;
    }

    public ArrayList<Object> retrieveData(String measEleId) {
        ArrayList<Object> measList = new ArrayList<Object>();
        int index = cols.indexOf(measEleId);
        if (index >= 0) {
            for (Iterator<Row> rowIter = sheet.rowIterator(); rowIter.hasNext();) {
                Row row = rowIter.next();
                Cell cell = row.getCell(index);

                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    measList.add(cell.getStringCellValue());
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    measList.add(new BigDecimal(cell.getNumericCellValue()));
                }
            }
        }
        return measList;
    }

    public Object[][] retrieveDataBasedOnViewSeq(ArrayList<String> columns) {
        Object cellData;
        Object[][] data = new Object[this.viewSequence.size()][columns.size()];
        int[] colIndexArr = new int[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            colIndexArr[i] = cols.indexOf(columns.get(i));
        }

        for (int i = 0; i < this.viewSequence.size(); i++) {
            int rowIndex = this.viewSequence.get(i);
            HSSFRow row = sheet.getRow(rowIndex);
            for (int j = 0; j < colIndexArr.length; j++) {
                HSSFCell cell = row.getCell(colIndexArr[j]);

                if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    data[i][j] = new BigDecimal(cell.getNumericCellValue());
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    data[i][j] = cell.getStringCellValue();
                }
            }
        }
        return data;
    }

    public ArrayList<BigDecimal> retrieveDataBasedOnViewSeq(String measEleId) {
        ArrayList<BigDecimal> measList = new ArrayList<BigDecimal>();
        int index = cols.indexOf(measEleId);
        if (index >= 0) {
            for (int i = 0; i < this.viewSequence.size(); i++) {
                int rowIndex = this.viewSequence.get(i);
                HSSFRow row = sheet.getRow(rowIndex);
                HSSFCell cell = row.getCell(index);

                if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    measList.add(new BigDecimal(cell.getNumericCellValue()));
                }
            }
        }
        return measList;
    }

    public void setRunTimeMeasureData(String measure, RunTimeMeasure rtMeasure) {
        if (this.rtMeasMap == null) {
            this.rtMeasMap = (new HashMap<String, RunTimeMeasure>());
        }
        this.rtMeasMap.put(measure, rtMeasure);
    }

    public String getFieldValueString(int row, String colName) {
        int index = cols.indexOf(colName);
        return getFieldValueString(row, index);
    }

    public String getFieldValueString(int row, int column) {
        String val = "";
        if (row < 0 || column < 0) {
            return val;
        }
        HSSFRow hsfrow = sheet.getRow(row);
        if (hsfrow != null) {
            HSSFCell cell = hsfrow.getCell(column);
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                val = Double.toString(cell.getNumericCellValue());
            } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                val = cell.getStringCellValue();
            } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                evaluator.evaluateFormulaCell(cell);
                val = String.valueOf(cell.getNumericCellValue());
            }
        }
        return val;
    }

    public BigDecimal getFieldValueRuntimeMeasure(int row, String measEleId) {
        if (RTMeasureElement.isRunTimeExcelColumn(measEleId)) {
            return getFieldValueBigDecimal(row, measEleId);
        } else if (RTMeasureElement.isRunTimeMeasure(measEleId)) {
            RunTimeMeasure rtMeasure = this.rtMeasMap.get(measEleId);
            if (rtMeasure != null) {
                return rtMeasure.getData(row);
            }
        }
        return new BigDecimal(0);
    }

    public BigDecimal getFieldValueBigDecimal(int row, String colName) {
        int index = cols.indexOf(colName);
        return getFieldValueBigDecimal(row, index);
    }

    public BigDecimal getFieldValueBigDecimal(int row, int colNo) {
        BigDecimal val = null;
        if (row < 0 || colNo < 0) {
            return val;
        }
        HSSFRow hsfrow = sheet.getRow(row);
        if (hsfrow != null) {
            HSSFCell cell = hsfrow.getCell(colNo);
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                val = new BigDecimal(cell.getNumericCellValue());
            } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                evaluator.evaluateFormulaCell(cell);
                val = new BigDecimal(cell.getNumericCellValue());
            } else {
                val = new BigDecimal(0);
            }
        }
        return val;
    }

    public String getFieldValueRuntimeDimension(int row, String dimEleId) {
        if (RTDimensionElement.isRunTimeDimension(dimEleId)) {
            return this.rtDimMap.get(dimEleId).getData(row);
        }
        return "";
    }

    public String getFieldValueDateString(int row, String colName) {
        int index = cols.indexOf(colName);
        return getFieldValueDateString(row, index);
    }

    public String getFieldValueDateString(int row, int col) {
        String dateVal = null;
        if (row < 0 || col < 0) {
            return dateVal;
        }
        HSSFRow hsfrow = sheet.getRow(row);
        if (hsfrow != null) {
            HSSFCell cell = hsfrow.getCell(col);
            Date date = cell.getDateCellValue();
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            dateVal = sdf.format(date);
        }
        return dateVal;
    }

    public BigDecimal getColumnAverageValue(String colName) {
        try {
            if (RTMeasureElement.isRunTimeMeasure(colName)) {
                return this.rtMeasMap.get(colName).getAverage();
            } else {
                return (BigDecimal) (columnAverages.get(colName));
            }
        } catch (Exception e) {
            return new BigDecimal("0");
        }
    }

    public BigDecimal getColumnAverageValue(int col) {
        String colName = cols.get(col);
        return getColumnAverageValue(colName);
    }

    public BigDecimal getColumnMinimumValue(int column) {
        String colName = cols.get(column);
        return getColumnMinimumValue(colName);
    }

    public BigDecimal getColumnMinimumValue(String colName) {
        try {
            if (RTMeasureElement.isRunTimeMeasure(colName)) {
                return this.rtMeasMap.get(colName).getMinimum();
            } else {
                return (BigDecimal) (columnOverAllMinimums.get(colName));
            }
        } catch (Exception e) {
            return new BigDecimal("0");
        }
    }

    public BigDecimal getColumnMaximumValue(int column) {
        String colName = cols.get(column);
        return getColumnMaximumValue(colName);
    }

    public BigDecimal getColumnMaximumValue(String colName) {
        try {
            if (RTMeasureElement.isRunTimeMeasure(colName)) {
                return this.rtMeasMap.get(colName).getMaximum();
            } else {
                return (BigDecimal) (columnOverAllMaximums.get(colName));
            }
        } catch (Exception e) {
            return new BigDecimal("0");
        }
    }

    public BigDecimal getColumnGrandTotalValue(String colName) {
        try {
            if (RTMeasureElement.isRunTimeExcelColumn(colName)) {
                return (BigDecimal) (columnGrandTotals.get(colName));
            } else if (RTMeasureElement.isRunTimeMeasure(colName)) {
                return this.rtMeasMap.get(colName).getGrandTotal();
            } else {
                return (BigDecimal) (columnGrandTotals.get(colName));
            }
        } catch (Exception e) {
            return new BigDecimal("0");
        }
    }

    public BigDecimal getColumnGrandTotalValue(int column) {
        String colName = cols.get(column);
        return getColumnGrandTotalValue(colName);
    }

    public ArrayList<BigDecimal> retrieveNumericData(String measEleId) {
        ArrayList<BigDecimal> measList = new ArrayList<BigDecimal>();
        int index = cols.indexOf(measEleId);
        if (index >= 0) {
            for (Iterator<Row> rowIter = sheet.rowIterator(); rowIter.hasNext();) {
                Row row = rowIter.next();
                Cell cell = row.getCell(index);

                if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    measList.add(new BigDecimal(cell.getNumericCellValue()));
                } else {
                    measList.add(BigDecimal.ZERO);
                }
            }
        }
        return measList;
    }
public ArrayList<BigDecimal> retrieveNumericDatarank(String measEleId,Container container) {
        ArrayList<BigDecimal> dataLst = new ArrayList<BigDecimal>(this.rowCount);
        DataFacade facade = new DataFacade(container);
        if (RTMeasureElement.isRunTimeMeasure(measEleId)) {
            if(measEleId.contains("_PYtdrank")|| measEleId.contains("_PQtdrank")|| measEleId.contains("_PMtdrank")|| measEleId.contains("_Qtdrank")|| measEleId.contains("_Ytdrank")){
                  if (this.rtMeasMap != null) {
                      for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueRuntimeMeasure(i, measEleId));}
                  }else{
                      measEleId=measEleId.replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PMtdrank", "").replace("_Qtdrank", "").replace("_Ytdrank", "");
                     String measrid=measEleId;
                      for (int i = 0; i < this.rowCount; i++) {

                dataLst.add(facade.getFormattedMeasureDatarank(i,measrid));}
            
                  }
                  
            }else{
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueRuntimeMeasure(i, measEleId));
            }
            }
        } else {
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueBigDecimal(i, measEleId));
            }
        }
        return dataLst;
    }
    public String[] getColumnNames() {
        return (String[]) this.cols.toArray(new String[0]);
    }

    public String[] getColumnTypes() {
        return columnTypes;
    }

    public Object getFieldValue(int rowIndex, String colName) {
        Object retVal = null;

        HSSFRow row = sheet.getRow(rowIndex);
        int colIndex = this.cols.indexOf(colName);
        HSSFCell cell = row.getCell(colIndex);
        evaluator = new HSSFFormulaEvaluator(workbook);

        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            retVal = BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            retVal = cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            evaluator.evaluateFormulaCell(cell);
            retVal = new BigDecimal(cell.getNumericCellValue());
        }

        return retVal;
    }

    public void addComment(String userId, int row, int col, String commentTxt) {
        HSSFRow hssfRow = sheet.getRow(row);
        HSSFCell cell = hssfRow.getCell(col);

        CreationHelper factory = workbook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();

        ClientAnchor anchor = factory.createClientAnchor();
        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(commentTxt);
        comment.setString(str);
        comment.setAuthor(userId);
        cell.setCellComment(comment);
    }

    public void clearComment(int row, int col) {
        HSSFRow hssfRow = sheet.getRow(row);
        HSSFCell cell = hssfRow.getCell(col);
        cell.removeCellComment();
    }

    public void clearFormat(int row, int col) {
        HSSFRow hssfRow = sheet.getRow(row);
        HSSFCell cell = hssfRow.getCell(col);
        cell.setCellStyle(null);
    }

    public void addFormat(int row, int col, String bgColor, String fontColor) {
        HSSFRow hssfRow = sheet.getRow(row);
        HSSFCell cell = hssfRow.getCell(col);

        HSSFCellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setFillBackgroundColor(bg);

    }

    public void addColumn(int col, String colName) {
        cols.add(col + 1, colName);

        //add the corresponding aggregation columns

//        excColumnTypesInt.add();
//        excColumnSizes.add(meta.getColumnDisplaySize(i+1));
//        excColumnTypes.add(meta.getColumnTypeName(i+1));

        excGrandTotals.add(col + 1, BigDecimal.ZERO);
        excAvgTotals.add(col + 1, BigDecimal.ZERO);
        excMax.add(col + 1, BigDecimal.ZERO);
        excMin.add(col + 1, BigDecimal.ZERO);

        for (int i = 0; i < rowCount; i++) {
            HSSFRow row = sheet.getRow(i);
            HSSFCell newCell = row.createCell(colCount);
            int j;
            for (j = colCount - 1; j > col; j--) {
                //Create a new cell which should have the contents of the previous column
                HSSFCell oldCell = row.getCell(j);
                newCell = row.getCell(j + 1);
                cloneCell(newCell, oldCell);
            }
            newCell = row.getCell(j + 1);
            resetCell(newCell);
        }
        colCount++;
    }

    private void resetCell(Cell cell) {
        cell.removeCellComment();
        CellStyle style = workbook.createCellStyle();
        cell.setCellStyle(style);
        cell.setCellValue(0);
    }

    /**
     * Given a sheet, this method deletes a column from a sheet and moves all
     * the columns to the right of it to the left one cell.
     *
     * Note, this method will not update any formula references.
     *
     * @param sheet
     * @param column
     */
    public void deleteColumn(int columnToDelete) {
        int maxColumn = 0;
        for (int r = 0; r < sheet.getLastRowNum() + 1; r++) {
            Row row = sheet.getRow(r);

            // if no row exists here; then nothing to do; next!
            if (row == null) {
                continue;
            }

            // if the row doesn't have this many columns then we are good; next!
            int lastColumn = row.getLastCellNum();
            if (lastColumn > maxColumn) {
                maxColumn = lastColumn;
            }

            if (lastColumn < columnToDelete) {
                continue;
            }

            for (int x = columnToDelete + 1; x < lastColumn + 1; x++) {
                Cell oldCell = row.getCell(x - 1);
                if (oldCell != null) {
                    row.removeCell(oldCell);
                }

                Cell nextCell = row.getCell(x);
                if (nextCell != null) {
                    Cell newCell = row.createCell(x - 1, nextCell.getCellType());
                    cloneCell(newCell, nextCell);
                }
            }
        }

        // Adjust the column widths
        for (int c = 0; c < maxColumn; c++) {
            sheet.setColumnWidth(c, sheet.getColumnWidth(c + 1));
        }
    }

    /*
     * Takes an existing Cell and merges all the styles and forumla into the new
     * one
     */
    private static void cloneCell(Cell cNew, Cell cOld) {
        cNew.setCellComment(cOld.getCellComment());
        cNew.setCellStyle(cOld.getCellStyle());

        switch (cOld.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN: {
                cNew.setCellValue(cOld.getBooleanCellValue());
                break;
            }
            case Cell.CELL_TYPE_NUMERIC: {
                cNew.setCellValue(cOld.getNumericCellValue());
                break;
            }
            case Cell.CELL_TYPE_STRING: {
                cNew.setCellValue(cOld.getStringCellValue());
                break;
            }
            case Cell.CELL_TYPE_ERROR: {
                cNew.setCellValue(cOld.getErrorCellValue());
                break;
            }
            case Cell.CELL_TYPE_FORMULA: {
                cNew.setCellFormula(cOld.getCellFormula());
                break;
            }
        }

    }

    public String clearValue(int row, int col, String address) {
        String retVal = "";
        HSSFRow hssfRow = sheet.getRow(row);
        HSSFCell cell = hssfRow.getCell(col);

        List<ExcelCell> vals = new ArrayList<ExcelCell>();
        String actualCellAddress = getActualCellAddress(address);

        cell.setCellValue("");
        cell.setCellFormula(null);

//        ExcelCell excelCell = new ExcelCell();
//        excelCell.setCellAddress(actualCellAddress);
//        excelCell.setValue(null);
//        vals.add(excelCell);

        if (displayFormula.containsKey(address)) {
            displayFormula.remove(address);
        }
        removeDependencies(address);
        vals = getDependentCellValues(vals, actualCellAddress);
        retVal = getReturnValueJSON(vals);

        return retVal;
    }

    public String addValue(int row, int col, String cellAddress, String formulaVal) {
        String retVal = formulaVal;
        HSSFRow hssfRow = sheet.getRow(row);
        HSSFCell cell = hssfRow.getCell(col);
        removeDependencies(cellAddress);
        List<ExcelCell> vals = new ArrayList<ExcelCell>();
        String actualCellAddress = getActualCellAddress(cellAddress);

        if (formulaVal.startsWith("=")) {
            formulaVal = formulaVal.toUpperCase();
            displayFormula.put(cellAddress, formulaVal);
            formulaVal = formulaVal.substring(1);
            formulaVal = changeToPOIFormula(formulaVal);



            addDependencies(actualCellAddress, formulaVal);
            cell.setCellFormula(formulaVal);
            vals = getDependentCellValues(vals, actualCellAddress);
            retVal = getReturnValueJSON(vals);
        } else if (isNumber(formulaVal)) {
            double value = Double.parseDouble(formulaVal);
            cell.setCellFormula(null);
            displayFormula.remove(cellAddress);
            cell.setCellValue(value);

            vals = getDependentCellValues(vals, actualCellAddress);
            retVal = getReturnValueJSON(vals);
        } else {
            cell.setCellValue(formulaVal);
            ExcelCell excelCell = new ExcelCell();
            excelCell.setCellAddress(actualCellAddress);
            excelCell.setValue(formulaVal);
            vals.add(excelCell);

        }

        return retVal;
    }

    private List<ExcelCell> getDependentCellValues(List<ExcelCell> depVals, String cellAddress) {

        int[] location = parseLocation(cellAddress);
        HSSFRow row = this.sheet.getRow(location[0]);
        HSSFCell cell = row.getCell(location[1]);

        ExcelCell excelCell = new ExcelCell();
        excelCell.setCellAddress(cellAddress);

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA || cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            evaluator.evaluateFormulaCell(cell);
            if (cell.getCellType() != Cell.CELL_TYPE_ERROR) {
                double value = cell.getNumericCellValue();
                excelCell.setValue(value);
                depVals.add(excelCell);

                List<String> depCells = formulaDependency.get(cellAddress);
                if (depCells != null && !depCells.isEmpty()) {
                    for (String cellAddr : depCells) {
                        getDependentCellValues(depVals, cellAddr);
                    }
                }
            } else {
                excelCell.setValue("error");
            }
            if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                excelCell.setFormula(cell.getCellFormula());
            }
        } else {
            excelCell.setValue("");
            excelCell.setFormula(null);
            depVals.add(excelCell);
        }


        return depVals;
    }

    public double evaluateFormula(String formula, int row, int col, String actualAddress, boolean isActualFormula) {
        int[] location = parseLocation(actualAddress);
        HSSFRow hssfRow = this.sheet.createRow(rowCount + 1);
        HSSFCell cell = hssfRow.createCell(location[1]);
        double value = 0.0;
        String actualFormula = "";
        String modifiedNumber = "";
        try {


            if (!isActualFormula) {
                formula = formula.substring(1);
                actualFormula = changeToPOIFormula(formula);
            } else {
                actualFormula = formula;
            }
//        String actualFormula="SUM(C2:C10)";

            List<ExcelCell> vals = new ArrayList<ExcelCell>();
//        for(String addr:displayCellAddr)
//          vals=getDependentCellValues(vals, getActualCellAddress(addr));

//        addDependencies(colName+(rowCount+1), formula);
            cell.setCellFormula(actualFormula);
//        vals=getDependentCellValues(vals, colName+(rowCount+2));
            evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            evaluator.evaluateFormulaCell(cell);
            value = cell.getNumericCellValue();

            sheet.removeRow(hssfRow);
        } catch (Exception e) {
            sheet.removeRow(hssfRow);
            logger.error("Exception:", e);

        }
//        modifiedNumber=NumberFormatter.getModifiedNumber(value, "", -1);
        return value;
    }

    private String getReturnValueJSON(List<ExcelCell> dependentVals) {
        StringBuilder json = new StringBuilder();
        json.append("{\"CellValues\":[");

        StringBuilder cellValJson = new StringBuilder();
        for (ExcelCell cell : dependentVals) {
            String cellAddress = cell.getCellAddress();
            String formula = cell.getFormula();
            String displayAddress = getDisplayAddress(cellAddress);
            Object value = cell.getValue();
            if (value != null && !"".equals(value)) {
                double val = Double.parseDouble(value.toString());
                value = NumberFormatter.getModifiedNumber(val, "", -1);
            } else {
                value = "";
            }

            cellValJson.append(",{");
            cellValJson.append("\"address\":").append("\"" + displayAddress + "\",");
            cellValJson.append("\"value\":").append("\"" + value.toString() + "\"");

            if (displayFormula.containsKey(displayAddress)) {
                cellValJson.append(",\"formula\":").append("\"" + displayFormula.get(displayAddress) + "\"");
            }
            cellValJson.append("}");
        }

        json.append(cellValJson.substring(1));
        json.append("]}");

        return json.toString();
    }

    private int[] parseLocation(String cellAddress) {
        int[] loc = new int[2];
        String colName = "";
        Pattern pattern = Pattern.compile("[A-Z]+");
        Matcher matcher = pattern.matcher(cellAddress);

        if (matcher.find()) {
            colName = matcher.group();
            loc[1] = CellReference.convertColStringToIndex(colName);
        }

        cellAddress = cellAddress.replace(colName, "");
        loc[0] = Integer.parseInt(cellAddress);
        loc[0]--;
        return loc;
    }

    public boolean isNumber(String val) {
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void addRowMapping(int displayRow, int actualRow) {
        rowMapping.put(displayRow, actualRow);
    }

    private void removeDependencies(String cellAddress) {
        ArrayList<String> dependentValues = new ArrayList<String>();
        String cell = cellAddress.replace("cell_", "");
        cell = changeToPOIFormula(cell);
        Set<String> dependentKeys = formulaDependency.keySet();
        Iterator<String> iterDependentKeys = dependentKeys.iterator();

        while (iterDependentKeys.hasNext()) {
            String depKey = iterDependentKeys.next();
            List<String> keyVal = formulaDependency.get(depKey);
            for (int j = 0; j < keyVal.size(); j++) {
                if (keyVal.get(j).equalsIgnoreCase(cell)) {
                    dependentValues.add(depKey);
                }
            }
        }
        for (int i = 0; i < dependentValues.size(); i++) {
            formulaDependency.remove(dependentValues.get(i), cell);
        }

    }

    private void addDependencies(String cellAddress, String formula) {
        Pattern pattern = Pattern.compile("[A-Z]+[0-9]+");
        Matcher matcher = pattern.matcher(formula);
        ArrayList<String> cellAddresses = new ArrayList<String>();
        ArrayList<String> cellAddrs = new ArrayList<String>();

        Set<String> tokens = new LinkedHashSet<String>();

        while (matcher.find()) {

            String address = matcher.group();
            tokens.add(address);
            cellAddresses.add(address);

        }

        Iterator<String> iter = tokens.iterator();
        if (formula.contains(":")) {
            cellAddrs = getAddresses(cellAddresses);
            for (int i = 0; i < cellAddrs.size(); i++) {
                formulaDependency.put(cellAddrs.get(i), cellAddress);
            }
        } else {
            while (iter.hasNext()) {
                String token = iter.next();
                formulaDependency.put(token, cellAddress);
            }
        }
    }

    public String changeToPOIFormula(String formula) {
        String tempFormula = formula;
        Pattern pattern = Pattern.compile("[A-Z]+[0-9]+");
        Matcher matcher = pattern.matcher(formula);

        Set<String> tokens = new LinkedHashSet<String>();

        while (matcher.find()) {
            String address = matcher.group();
            tokens.add(address);
        }

        Iterator<String> iter = tokens.iterator();
        while (iter.hasNext()) {
            String token = iter.next();
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m = p.matcher(token);

            while (m.find()) {
                String dispRowStr = m.group();
                Integer dispRow = Integer.parseInt(dispRowStr);
                Integer actualRow = rowMapping.get(dispRow) + 1;
                String newAddress = token.replace(dispRowStr, actualRow.toString());
                tempFormula = tempFormula.replace(token, newAddress);
            }
        }
        return tempFormula;
    }

    public String changeToDisplayFormula(String formula) {
        String tempFormula = formula;
        Pattern pattern = Pattern.compile("[A-Z]+[0-9]+");
        Matcher matcher = pattern.matcher(formula);

        Set<String> tokens = new LinkedHashSet<String>();

        while (matcher.find()) {
            String address = matcher.group();
            tokens.add(address);
        }

        Iterator<String> iter = tokens.iterator();
        while (iter.hasNext()) {
            String actualAddress = iter.next();
            String dispAddress = getDisplayAddress(actualAddress);
            tempFormula = tempFormula.replace(actualAddress, dispAddress);
        }
        return tempFormula;
    }

    private String getDisplayAddress(String address) {
        String dispAddress = "";
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(address);

        if (m.find()) {
            String rowNum = m.group();
            Integer actualRow = Integer.parseInt(rowNum);
            Integer dispRow = getDisplayRow(actualRow - 1);
            if (dispRow != null) {
                dispAddress = address.replace(rowNum, dispRow.toString());
            } else {
                dispAddress = address;
            }
        }
        return dispAddress;
    }

    public Integer getDisplayRow(Integer actualRow) {
        Integer displayRow = null;
        if (rowMapping.containsValue(actualRow)) {
            Set<Integer> keySet = rowMapping.keySet();
            Iterator<Integer> keyIter = keySet.iterator();
            while (keyIter.hasNext()) {
                Integer key = keyIter.next();
                Integer value = rowMapping.get(key);
                if (actualRow.equals(value)) {
                    displayRow = key;
                    break;
                }
            }
        }
        return displayRow;
    }

    public void copyCells(String address) {
        String[] addressArr = address.split("~");
        copyCells.clear();
        for (String cellAddr : addressArr) {
            if (!"".equals(cellAddr)) {
                copyCells.add(getActualCellAddress(cellAddr));
            }
        }
    }

    public String pasteCells(String cellAddress) {
        String json = "";
        Cell pasteCell = this.getCellByDisplayAddress(cellAddress);
        String actualCellAddress = getActualCellAddress(cellAddress);
        List<ExcelCell> vals = new ArrayList<ExcelCell>();

        for (String cellAddr : copyCells) {
            Cell copyCell = this.getCellByActualAddress(cellAddr);
            if (copyCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                String formulaVal = copyCell.getCellFormula();

                int[] copyRowCol = parseLocation(cellAddr);
                int[] pasteRowCol = parseLocation(actualCellAddress);

                int rowDiff = pasteRowCol[0] - copyRowCol[0];
                int colDiff = pasteRowCol[1] - copyRowCol[1];

                formulaVal = changeCopyFormula(formulaVal, rowDiff, colDiff);
                displayFormula.put(cellAddress, "=" + changeToDisplayFormula(formulaVal));
                removeDependencies(cellAddress);
                addDependencies(actualCellAddress, formulaVal);
                pasteCell.setCellFormula(formulaVal);

                vals = getDependentCellValues(vals, actualCellAddress);
                json = getReturnValueJSON(vals);
            } else if (copyCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                double value = copyCell.getNumericCellValue();
                pasteCell.setCellValue(value);

                vals = getDependentCellValues(vals, actualCellAddress);
                json = getReturnValueJSON(vals);
            } else {
                String cellVal = copyCell.getStringCellValue();
                pasteCell.setCellValue(cellVal);
                ExcelCell excelCell = new ExcelCell();
                excelCell.setCellAddress(actualCellAddress);
                excelCell.setValue(cellVal);
                vals.add(excelCell);
                json = getReturnValueJSON(vals);
            }
        }

        return json.toString();
    }

    private Cell getCellByDisplayAddress(String cellAddress) {
        String actualCellAddress = getActualCellAddress(cellAddress);
        int[] cellLocation = parseLocation(actualCellAddress);
        Row row = this.sheet.getRow(cellLocation[0]);
        Cell cell = row.getCell(cellLocation[1]);
        return cell;
    }

    private Cell getCellByActualAddress(String cellAddress) {
        int[] cellLocation = parseLocation(cellAddress);
        Row row = this.sheet.getRow(cellLocation[0]);
        Cell cell = row.getCell(cellLocation[1]);
        return cell;
    }

    public String getActualCellAddress(String address) {
        return changeToPOIFormula(address);
    }

    public void setExcelRuntimeMeasure(String measId, ArrayList<Object> newList) {
        int colIndex = cols.indexOf(measId);
        for (int i = 0; i < rowCount; i++) {
            BigDecimal bdecimal = BigDecimal.ZERO;
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(colIndex);

            String value = newList.get(i) == null ? null : newList.get(i).toString();

            if (value != null) {
                if (value.startsWith("=")) {
                    value = value.substring(1);
                    value = value.toUpperCase();

                    cell.setCellFormula(value);

                    evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    evaluator.evaluateFormulaCell(cell);
                    bdecimal = new BigDecimal(cell.getNumericCellValue());
                } else if (isNumber(value)) {
                    cell.setCellValue(Double.parseDouble(value));
                    bdecimal = new BigDecimal(value);
                } else {
                    cell.setCellValue(value);
                }
            }

            if (i == 0) {
                excGrandTotals.set(colIndex, bdecimal);
                excMax.set(colIndex, bdecimal);
                excMin.set(colIndex, bdecimal);
            } else {
                excGrandTotals.set(colIndex, excGrandTotals.get(colIndex).add(bdecimal));
                excMax.set(colIndex, excMax.get(colIndex).max(bdecimal));
                excMin.set(colIndex, excMin.get(colIndex).min(bdecimal));
            }
        }

        BigDecimal dividend = new BigDecimal(String.valueOf(rowCount));
        columnOverAllMaximums.put(cols.get(colIndex), excMax.get(colIndex));
        columnOverAllMinimums.put(cols.get(colIndex), excMin.get(colIndex));
        columnGrandTotals.put(cols.get(colIndex), excGrandTotals.get(colIndex));
        excAvgTotals.set(colIndex, excGrandTotals.get(colIndex).divide(dividend, MathContext.DECIMAL64));
        columnAverages.put(cols.get(colIndex), excAvgTotals.get(colIndex));
    }

    private String changeCopyFormula(String formulaVal, int rowDiff, int colDiff) {

        String tempFormula = formulaVal;
        Pattern pattern = Pattern.compile("[A-Z]+[0-9]+");
        Matcher matcher = pattern.matcher(formulaVal);

        Set<String> tokens = new LinkedHashSet<String>();

        while (matcher.find()) {
            String address = matcher.group();
            tokens.add(address);
        }

        Iterator<String> iter = tokens.iterator();
        while (iter.hasNext()) {
            String token = iter.next();
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m = p.matcher(token);

            while (m.find()) {
                String dispRowStr = m.group();
                Integer dispRow = Integer.parseInt(dispRowStr);
                Integer actualRow = dispRow + rowDiff;
                String tempToken = token.replace(dispRowStr, "");
                int colNo = CellReference.convertColStringToIndex(tempToken);
                colNo = colNo + colDiff;
                tempToken = CellReference.convertNumToColString(colNo);
                tempToken = tempToken + actualRow;

//                String newAddress = token.replace(dispRowStr, actualRow.toString());
                tempFormula = tempFormula.replace(token, tempToken);
            }
        }

        return tempFormula;
    }

    public String getFormula(int row, String measId) {
        String formula = null;
        if (RTMeasureElement.isRunTimeExcelColumn(measId)) {
            int colIndex = cols.indexOf(measId);
            HSSFRow hssfRow = sheet.getRow(row);
            HSSFCell cell = hssfRow.getCell(colIndex);

            if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                formula = cell.getCellFormula();
                formula = "=" + formula;
            }
        }
        return formula;
    }

    private ArrayList<String> getAddresses(ArrayList<String> cellAddrs) {

        int[] startArray;
        int[] endArray;
        ArrayList<String> returnAddrs = new ArrayList<String>();
        startArray = parseLocation(cellAddrs.get(0));
        endArray = parseLocation(cellAddrs.get(1));
        for (int i = startArray[0]; i <= endArray[0]; i++) {
            for (int j = startArray[1]; j <= endArray[1]; j++) {
                int suff = i + 1;
                String tempToken = CellReference.convertNumToColString(j);
                String address = tempToken + suff;
                returnAddrs.add(address);
            }
        }

        return returnAddrs;
    }

    public ArrayList<BigDecimal> retrieveNumericDataForMultiTime(String measeId, String groupId, String aggType) {
        ArrayList<BigDecimal> dataLst = new ArrayList<BigDecimal>(this.rowCount);
        String prevGroupHeader = null;
        BigDecimal measVal = null;
        BigDecimal groupHeaderVal = null;
        ArrayList<BigDecimal> groupValList = new ArrayList<BigDecimal>();
        if (RTMeasureElement.isRunTimeMeasure(measeId)) {
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueRuntimeMeasure(i, measeId));
            }

        } else {
            for (int i = 0; i < this.rowCount; i++) {
                measVal = this.getFieldValueBigDecimal(i, measeId);
                String currGroupHeader = getFieldValueString(i, groupId);
                if (prevGroupHeader != null && prevGroupHeader.equalsIgnoreCase(currGroupHeader)) {
                    dataLst.add(measVal);
                    groupValList.add(measVal);
                } else if (prevGroupHeader != null && !prevGroupHeader.equalsIgnoreCase(currGroupHeader)) {
                    groupHeaderVal = getAggrigationResult(groupValList, aggType);
                    dataLst.add(groupHeaderVal);
                    dataLst.add(measVal);
                    groupValList = new ArrayList<BigDecimal>();
                    groupValList.add(measVal);
                    prevGroupHeader = currGroupHeader;

                }
                if (prevGroupHeader == null) {
                    prevGroupHeader = currGroupHeader;
                    dataLst.add(measVal);
                    groupValList.add(measVal);
                }
            }
            if (!groupValList.isEmpty()) {
                groupHeaderVal = getAggrigationResult(groupValList, aggType);
                dataLst.add(groupHeaderVal);
            }
        }

        return dataLst;
    }

    public BigDecimal getAggrigationResult(ArrayList<BigDecimal> valList, String aggType) {
        BigDecimal result = new BigDecimal(0);
        if (aggType.equalsIgnoreCase("avg")) {
            for (BigDecimal val : valList) {
                result = result.add(val);
            }
            result = result.divide(new BigDecimal(valList.size()));
        } else if (aggType.equalsIgnoreCase("sum")) {
            for (BigDecimal val : valList) {
                result = result.add(val);
            }
        } else if (aggType.equalsIgnoreCase("min")) {
            result = (BigDecimal) Collections.min(valList);
        } else if (aggType.equalsIgnoreCase("max")) {
            result = (BigDecimal) Collections.max(valList);
        } else if (aggType.equalsIgnoreCase("count")) {
        }
        return result;
    }
    //added by anitha
            public Object[][] retrieveData(ArrayList<String> sortColumns, char[] rowDataTypes,Container container) {
        // long startTime = System.currentTimeMillis();
        Object cellData;
        Object[][] data = new Object[this.viewSequence.size()][sortColumns.size()];
        int j;

        for (int i = 0; i < this.viewSequence.size(); i++) {
            j = 0;
            for (String column : sortColumns) {
                if (rowDataTypes[j] == 'N') {
                    cellData = this.getMeasureColumnData(column, this.viewSequence.get(i),container);//this.getFieldValueBigDecimal(i,column);
                } else if (rowDataTypes[j] == 'C') {//
                    try {
                        String data1 = "";
                        if (column.equalsIgnoreCase("A_TIME") || column.equalsIgnoreCase("A_O_TIME") || column.equalsIgnoreCase("TIME")) {
                            data1 = (this.getDimensionColumnDate(column, this.viewSequence.get(i)));
                        } else {
                            data1 = this.getDimensionColumnData(column, this.viewSequence.get(i));
}
                        cellData = data1;
                    } catch (Exception exception) {
                        cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                    }

                } else {
                    try {
                        BigDecimal bigDecimal1 = new BigDecimal(this.getDimensionColumnData(column, this.viewSequence.get(i)));
                        cellData = bigDecimal1; //hardcoding not good assuming C is Dimension
                    } catch (Exception exception) {
                        //
                        cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                    }
                }
                data[i][j] = cellData;
                j++;
            }
        }
        // long endTime = System.currentTimeMillis();
//        
        return data;
    }
             private BigDecimal getMeasureColumnData(String element, int row,Container container) {
        BigDecimal data;
        DataFacade dfacade = new DataFacade(container);
        try{
        if (RTMeasureElement.isRunTimeMeasure(element)&& (element.contains("_MTD")|| element.contains("_QTD")|| element.contains("_YTD")|| element.contains("_PMTD")|| element.contains("_PQTD")|| element.contains("_PYTD")
                ||element.contains("_MOM")||element.contains("_QOQ")||element.contains("_YOY")||element.contains("_MOYM")||element.contains("_QOYQ")||element.contains("_MOMPer")||element.contains("_QOQPer")||element.contains("_YOYPer")||element.contains("_MOYMPer")||element.contains("_QOYQPer")
                ||element.contains("_PYMTD")||element.contains("_PYQTD")||element.contains("_WTD")||element.contains("_PWTD")||element.contains("_PYWTD")||element.contains("_WOWPer")||element.contains("_WOYWPer")||element.contains("_WOW")||element.contains("_WOYW"))) {
            data = (BigDecimal) this.getFieldValue(row, element);
        }else if (RTMeasureElement.isRunTimeMeasure(element)){
            data = this.getFieldValueRuntimeMeasure(row, element);
        }else {
            data = this.getFieldValueBigDecimal(row, element);
        }}catch(Exception e){
            data = dfacade.getMeasureDataForComputationRT(row, element);
        }
        return data;
    }

    private String getDimensionColumnDate(String column, int row) {
        return this.getFieldValueStringDate(row, column);
    }

    private String getFieldValueStringDate(int row, String colName) {
         try {
            if (colName.equalsIgnoreCase("A_TIME")) {
                colName = "A_O_TIME";
            }
            String date = (String) ((ArrayList) hMap.get(colName)).get(row);
            return date;
        } catch (Exception e) {
            return null;
        }
    }
    //end of code by anitha
}