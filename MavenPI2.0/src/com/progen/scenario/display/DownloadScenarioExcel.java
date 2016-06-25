/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scenario.display;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.CellView;
import jxl.HeaderFooter;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.*;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 *
 * @author sreekanth
 */
public class DownloadScenarioExcel {

    public static Logger logger = Logger.getLogger(DownloadScenarioExcel.class);
    private String reportName = "";
    private PbReturnObject ret = null;
    private String fileName = null;
    private HttpServletResponse response = null;
    private String[] types = null;
    private String[] columns = null;
    private String[] displayColumns = null;
    private String[] displayLabels = null;
    private String displayType = null;
    Date date = new Date();
    String DATE_FORMAT = "MM/dd/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    private String[] filePaths = null;
    private HttpServletRequest request = null;
    private String logoPath = null;
    ByteArrayOutputStream bos = null;
    ServletOutputStream outputstream = null;
    Label[] labels = null;
    //common code for all
    WritableWorkbook writableWorkbook = null;
    WritableSheet writableSheet = null;
    WritableSheet[] writableSheets = null;
    WritableImage writableImage = null;
    WritableFont writableFont = null;
    WritableCellFormat writableCellFormat = null;
    WritableCellFormat writableCellFormatForColorCode = null;
    Label titleLabel = null;
    Label reportLabel = null;
    Label dateLabel = null;
    Label label = null;
    Label timeRangeLabel = null;
    int columnCount;
    CellView HeadingCellView = null;
    NumberFormat nFormat = null;
    int rowStart = 4;
    //added by bharathi reddy for targets
    private String targetName = "";
    private String[] targetdisplayRows = null;
    private HashMap targetOriMap = null;
    int rowCount;
    //a dded on 15feb10
    private String targetBasis = null;
    private String parentDataQ = null;
    private String parentDataQ2 = null;
    private String dataQuery2 = null;
    private String dataQuery = null;
    private String targetId = null;
    private String periodType = null;
    private String primaryAnalyze = null;
    private String secAnalyze = null;
    private HashMap nonAllVals = null;
    private ArrayList primList = null;
    private ArrayList secList = null;
    private String startRange = null;
    private HashMap RestrictingTotal = null;
    private String PrimaryViewByName = null;
    private String endRange = null;
    private String headerTitle = "";
    private ArrayList timeDetailsArray;
    int maxRows = 65500;//65500
    int countOfSheets = 1;
    BigDecimal multiplier = new BigDecimal("100");
    private HashMap ColorCodeMap = null;

    //ends
    public DownloadScenarioExcel() {
    }

    public void createExcel() {
        bos = new ByteArrayOutputStream();


        columnCount = displayLabels.length;
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        try {
            HeaderFooter header = new HeaderFooter(getHeaderTitle());
            HeaderFooter footer = new HeaderFooter(getHeaderTitle());

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            writableWorkbook = Workbook.createWorkbook(bos, wbSettings);
            writableSheet = writableWorkbook.createSheet(getReportName().replace("_", " "), 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            //writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(false);


            titleLabel = new Label(0, 0, getHeaderTitle());
            reportLabel = new Label(0, 1, "Scenario Title  : " + getReportName().replace("_", " "));
            dateLabel = new Label(0, 2, "Created Date : " + sdf.format(date));

            for (int i = 0; i < rowStart - 2; i++) {
                writableSheet.setRowView(i, (20 * 20), false);
            }

            titleLabel.setCellFormat(writableCellFormat);
            reportLabel.setCellFormat(writableCellFormat);
            dateLabel.setCellFormat(writableCellFormat);

            writableSheet.addCell(titleLabel);
            writableSheet.addCell(reportLabel);
            writableSheet.addCell(dateLabel);
            buildTable();
            writableWorkbook.write();
            writableWorkbook.close();
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(bos.size());
            outputstream = response.getOutputStream();
            bos.writeTo(outputstream);
            outputstream.flush();
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
    }

    private void buildTable() throws Exception {

        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        writableCellFormat = new WritableCellFormat(writableFont);
        writableCellFormat.setBackground(Colour.GREY_25_PERCENT);
        writableCellFormat.setWrap(true);

        String[] tempArray = null;

        for (int i = 0; i < displayLabels.length; i++) {
            // ////.println("Lable:\t" + displayLabels[i].replace("^", "%").trim());
            label = new Label(i, rowStart, displayLabels[i].replace("^", "%").trim());
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);

            HeadingCellView = new CellView();
            HeadingCellView.setSize(256 * 30);
            writableSheet.setColumnView(i, HeadingCellView);
        }

        writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
        writableCellFormat = new WritableCellFormat(writableFont);
        writableCellFormat.setWrap(true);
        for (int rCount = 0; rCount < displayColumns.length; rCount++) {
            tempArray = displayColumns[rCount].split("~");
            //////.println("view by:\t"+displayColumns[rCount]);
            for (int cCount = 0; cCount < tempArray.length; cCount++) {
                label = new Label(cCount, 1 + rowStart + rCount, tempArray[cCount].replace("^", ","));
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);

                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 30);
                writableSheet.setColumnView(cCount, HeadingCellView);

            }
            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

        }





    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public PbReturnObject getRet() {
        return ret;
    }

    public void setRet(PbReturnObject ret) {
        this.ret = ret;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getDisplayColumns() {
        return displayColumns;
    }

    public void setDisplayColumns(String[] displayColumns) {
        this.displayColumns = displayColumns;
    }

    public String[] getDisplayLabels() {
        return displayLabels;
    }

    public void setDisplayLabels(String[] displayLabels) {
        this.displayLabels = displayLabels;
    }

    public String[] getTargetDisplayRows() {
        return targetdisplayRows;
    }

    public void setTargetDisplayRows(String[] targetdisplayRows) {
        this.targetdisplayRows = targetdisplayRows;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String[] getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String[] filePaths) {
        this.filePaths = filePaths;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    //added by bharathi reddy for targets
    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

//
    public HashMap getTargetOriMap() {
        return targetOriMap;
    }

    public void setTargetOriMap(HashMap targetOriMap) {
        this.targetOriMap = targetOriMap;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getPrimaryAnalyze() {
        return primaryAnalyze;
    }

    public void setPrimaryAnalyze(String primaryAnalyze) {
        this.primaryAnalyze = primaryAnalyze;
    }

    public String getSecAnalyze() {
        return secAnalyze;
    }

    public void setSecAnalyze(String secAnalyze) {
        this.secAnalyze = secAnalyze;
    }

    public HashMap getNonAllVals() {
        return nonAllVals;
    }

    public void setNonAllVals(HashMap nonAllVals) {
        this.nonAllVals = nonAllVals;
    }

    public ArrayList getPrimList() {
        return primList;
    }

    public void setPrimList(ArrayList primList) {
        this.primList = primList;
    }

    public ArrayList getSecList() {
        return secList;
    }

    public void setSecList(ArrayList secList) {
        this.secList = secList;
    }

    public String getStartRange() {
        return startRange;
    }

    public void setStartRange(String startRange) {
        this.startRange = startRange;
    }

    public HashMap getRestrictingTotal() {
        return RestrictingTotal;
    }

    public void setRestrictingTotal(HashMap RestrictingTotal) {
        this.RestrictingTotal = RestrictingTotal;
    }

    public String getPrimaryViewByName() {
        return PrimaryViewByName;
    }

    public void setPrimaryViewByName(String PrimaryViewByName) {
        this.PrimaryViewByName = PrimaryViewByName;
    }

    public String getEndRange() {
        return endRange;
    }

    public void setEndRange(String endRange) {
        this.endRange = endRange;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList getTimeDetailsArray() {
        return timeDetailsArray;
    }

    public void setTimeDetailsArray(ArrayList timeDetailsArray) {
        this.timeDetailsArray = timeDetailsArray;
    }

    public String getTargetBasis() {
        return targetBasis;
    }

    public void setTargetBasis(String targetBasis) {
        this.targetBasis = targetBasis;
    }

    public String getParentDataQ() {
        return parentDataQ;
    }

    public void setParentDataQ(String parentDataQ) {
        this.parentDataQ = parentDataQ;
    }

    public String getParentDataQ2() {
        return parentDataQ2;
    }

    public void setParentDataQ2(String parentDataQ2) {
        this.parentDataQ2 = parentDataQ2;
    }

    public String getDataQuery2() {
        return dataQuery2;
    }

    public void setDataQuery2(String dataQuery2) {
        this.dataQuery2 = dataQuery2;
    }

    public String getDataQuery() {
        return dataQuery;
    }

    public void setDataQuery(String dataQuery) {
        this.dataQuery = dataQuery;
    }

    public HashMap getColorCodeMap() {
        return ColorCodeMap;
    }

    public void setColorCodeMap(HashMap ColorCodeMap) {
        this.ColorCodeMap = ColorCodeMap;
    }

    public Colour getColor(String disColumnName, String currentValue) {
        String bgColor = "";
        String[] StrColors = {"Red", "Orange", "Green"};
        if (getColorCodeMap() != null) {
            HashMap tempMap = null;
            tempMap = (HashMap) ColorCodeMap.get(disColumnName);
            if (tempMap != null) {
                String[] colorCodes = (String[]) tempMap.get("colorCodes");
                String[] operators = (String[]) tempMap.get("operators");
                String[] sValues = (String[]) tempMap.get("sValues");
                String[] eValues = (String[]) tempMap.get("eValues");

                if (colorCodes != null && sValues != null && sValues != null) {
                    for (int h = 0; h
                            < colorCodes.length; h++) {
                        if (operators[h].equalsIgnoreCase(">")) {
                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
                                if (Double.parseDouble(currentValue) > Double.parseDouble(sValues[h])) {
                                    bgColor = colorCodes[h];
                                }
                            }
                        } else if (operators[h].equalsIgnoreCase("<")) {
                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
                                if (Double.parseDouble(currentValue) < Double.parseDouble(sValues[h])) {
                                    bgColor = colorCodes[h];
                                }
                            }
                        } else if (operators[h].equalsIgnoreCase(">=")) {
                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
                                if (Double.parseDouble(currentValue) >= Double.parseDouble(sValues[h])) {
                                    bgColor = colorCodes[h];
                                }
                            }
                        } else if (operators[h].equalsIgnoreCase("<=")) {
                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
                                if (Double.parseDouble(currentValue) <= Double.parseDouble(sValues[h])) {
                                    bgColor = colorCodes[h];
                                }
                            }
                        } else if (operators[h].equalsIgnoreCase("=")) {
                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
                                if (Double.parseDouble(currentValue) == Double.parseDouble(sValues[h])) {
                                    bgColor = colorCodes[h];
                                }
                            } else if (operators[h].equalsIgnoreCase("!=")) {
                                if (Double.parseDouble(currentValue) != Double.parseDouble(sValues[h])) {
                                    bgColor = colorCodes[h];
                                }
                            }
                        } else if (operators[h].equalsIgnoreCase("<>")) {
                            if (sValues[h] != null && eValues[h] != null && !"".equalsIgnoreCase(eValues[h]) && !"".equalsIgnoreCase(sValues[h])) {
                                if ((Double.parseDouble(sValues[h]) < Double.parseDouble(currentValue)) && (Double.parseDouble(currentValue) < Double.parseDouble(eValues[h]))) {
                                    bgColor = colorCodes[h];
                                }
                            }
                        }
                    }
                }
            }
        }

        if (bgColor.equalsIgnoreCase("Red")) {
            return Colour.RED;
        } else if (bgColor.equalsIgnoreCase("Orange")) {
            return Colour.ORANGE;
        } else if (bgColor.equalsIgnoreCase("Green")) {
            return Colour.GREEN;
        } else {
            return null;
        }
    }
    /*
     * public ByteArrayOutputStream Downloadexl(String tablename, String[]
     * columnNames) throws IOException, WriteException, Exception {
     * ByteArrayOutputStream bos = null; int maxRows = 65500;//65500 try { bos =
     * new ByteArrayOutputStream(); StringBuffer sbuffer = new
     * StringBuffer("select distinct ");
     *
     * for (int i = 0; i < columnNames.length; i++) { if (i ==
     * (columnNames.length - 1)) { sbuffer.append(columnNames[i] + " "); } else
     * { sbuffer.append(columnNames[i] + " , "); } } sbuffer.append(" from " +
     * tablename);
     *
     * Connection con = ProgenConnection.getCustomerConn();
     *
     * PbReturnObject prgr = execSelectSQL(sbuffer.toString(), con);
     *
     * int columnStart = 0; int rowStart = 0;
     *
     * WorkbookSettings wbSettings = new WorkbookSettings();
     *
     * wbSettings.setLocale(new Locale("en", "EN")); WritableWorkbook workbook =
     * Workbook.createWorkbook(bos, wbSettings); WritableFont writableFont = new
     * WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
     * WritableCellFormat writableCellFormat = new
     * WritableCellFormat(writableFont); writableCellFormat.setWrap(false);
     *
     *
     * WritableFont writableFont1 = new WritableFont(WritableFont.ARIAL, 10,
     * WritableFont.NO_BOLD); WritableCellFormat writableCellFormat1 = new
     * WritableCellFormat(writableFont1); writableCellFormat1.setWrap(false);
     *
     * WritableSheet[] excelSheets = null; int countOfSheets = 1;
     *
     * if ((prgr.getRowCount() % maxRows) == 0) { countOfSheets =
     * prgr.getRowCount() / maxRows; } else { countOfSheets =
     * (prgr.getRowCount() / maxRows) + 1; } excelSheets = new
     * WritableSheet[countOfSheets];
     *
     * Label label = null; int fromRow = 0; int toRow = 0;
     *
     * for (int i = 0; i < excelSheets.length; i++) { excelSheets[i] =
     * workbook.createSheet("Report_" + i, i); rowStart = 0; columnStart = 0;
     * label = new Label(columnStart, rowStart, tablename);
     * label.setCellFormat(writableCellFormat); excelSheets[i].addCell(label);
     * ++rowStart; for (String str : columnNames) { label = new
     * Label(++columnStart, rowStart, str);
     * label.setCellFormat(writableCellFormat); excelSheets[i].addCell(label); }
     * for (String str : columnNames) { label = new Label(++columnStart,
     * rowStart, str + "(Modifieds)"); label.setCellFormat(writableCellFormat);
     * excelSheets[i].addCell(label); } if (i == 0) { fromRow = 0; toRow =
     * fromRow + maxRows; } else { fromRow = toRow + 1; toRow = fromRow +
     * maxRows; } if (toRow > prgr.getRowCount()) { toRow = prgr.getRowCount();
     * }//displaying boduy
     *
     * for (int j = fromRow; j < toRow; j++) { int colStart = 0; ++rowStart; for
     * (String str : columnNames) { label = new Label(colStart++, rowStart,
     * prgr.getFieldValueString(j, str));
     * label.setCellFormat(writableCellFormat1); excelSheets[i].addCell(label);
     * } for (String str : columnNames) { label = new Label(++colStart,
     * rowStart, prgr.getFieldValueString(j, str));
     * label.setCellFormat(writableCellFormat1); excelSheets[i].addCell(label);
     * } } } workbook.write(); workbook.close();
     *
     * } catch (Exception exp) { logger.error("Exception:",exp); } return bos;
     *
     * }
     */
}
