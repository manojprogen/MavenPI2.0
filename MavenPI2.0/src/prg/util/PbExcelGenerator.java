/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportParameter;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.colorgroup.ColorGroup;
import com.progen.report.data.DataFacade;
import com.progen.report.data.RowViewTableBuilder;
import com.progen.report.data.TableBuilder;
import com.progen.report.display.ExcelTableBodyDisplay;
import com.progen.report.display.ExcelTableSubTotalDisplay;
import com.progen.report.display.table.TableDisplay;
import com.progen.report.display.util.NumberFormatter;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import com.progen.targetview.db.PbTargetExcelXml;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.CellView;
import jxl.HeaderFooter;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WriteException;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import prg.db.Container;
import prg.db.ContainerConstants;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import org.apache.log4j.*;

/**
 * @filename PbExcelGenerator
 *
 * @author santhosh.kumar@progenbusiness.com @date Nov 29, 2009, 5:10:34 PM
 */
public class PbExcelGenerator {

    public static Logger logger = Logger.getLogger(PbExcelGenerator.class);
    private String reportName = "";
    private ProgenDataSet ret = null;
    private String fileName = null;
    private HttpServletResponse response = null;
    private String[] types = null;
    private String[] columns = null;
    private String[] displayColumns = null;
    private String[] displayLabels = null;
    private String displayType = null;
    Date date = new Date();
    String DATE_FORMAT = "MM/dd/yyyy";
    String DATE_FORMAT1 = "dd/MM/yyyy"; //added by Nazneen for date format..
//    String DATE_FORMAT = "dd/MM/yyyy";
//    String DATE_FORMAT = "yyyy/MM/dd";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT1);
    private String[] filePaths = null;
    private HttpServletRequest request = null;
    private String logoPath = null;
    ByteArrayOutputStream bos = null;
    ServletOutputStream outputstream = null;
    Label[] labels = null;
    //common code for all
    WritableWorkbook writableWorkbook = null;
    Workbook Workbook1 = null;
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
    //added by Amar
    //added by dinanath
    SXSSFWorkbook hhhWorkbook = null;
    SXSSFSheet hhhSheet = null;
    CellStyle hhhStyle = null;
    Row hhhRow = null;
    Cell hhhCell = null;
    //commented by Dinanath
    SXSSFWorkbook hhhhWorkbook = null;
    SXSSFSheet hhhhSheet = null;
    CellStyle hhhhStyle = null;
    Row hhhhRow = null;
    Cell hhhhCell = null;
//    org.apache.poi.ss.usermodel.Workbook hhhhWorkbook = null;
//    org.apache.poi.ss.usermodel.Cell hhhhCell = null;
//    org.apache.poi.ss.usermodel.CellStyle hhhhStyle = null;
//    org.apache.poi.ss.usermodel.Sheet hhhhSheet = null;
//    org.apache.poi.ss.usermodel.Row hhhhRow = null;
    //commented by Dinanath
//    SXSSFCell hhhhCell = null;
//    SXSSFCellStyle hhhhStyle = null;
//    SXSSFRow hhhhRow = null;
//    SXSSFColor hhhhColor = null;
//    SXSSFDataFormat hhhhFormat = null;
    //added by Dinanath
//    XSSFWorkbook hhhhWorkbook = null;
//    XSSFSheet hhhhSheet = null;
//    XSSFCell hhhhCell = null;
//    XSSFCellStyle hhhhStyle = null;
//    XSSFRow hhhhRow = null;
//    XSSFColor hhhhColor = null;
//    XSSFDataFormat hhhhFormat = null;
    //endded by Dinanath
    HSSFWorkbook hhWorkbook = null;
    HSSFSheet hhSheet = null;
    XSSFWorkbook hWorkbook = null;
    XSSFSheet hSheet = null;
    HSSFCell hhCell = null;
    HSSFCellStyle hhStyle = null;
    HSSFRow hhRow = null;
    HSSFColor hhColor = null;
    HSSFDataFormat hhFormat = null;
    XSSFCell hCell = null;
    XSSFCellStyle hStyle = null;
    XSSFRow hRow = null;
    XSSFColor hColor = null;
    XSSFDataFormat hFormat = null;
    int rowStart = 4;
    FileInputStream inStream = null;
    //added by bharathi reddy for targets
    int rowStartTarget = 5;
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
    private String paramType = null;
    private String endRange = null;
    private String headerTitle = "";
    private ArrayList timeDetailsArray;
    private ArrayList filterValues;
    int maxRows = 65500;//65500
    int countOfSheets = 1;
    BigDecimal multiplier = new BigDecimal("100");
    private HashMap ColorCodeMap = null;
    Number n = null;
    private int fromRow;
    private int toRow;
    private int sheet = 0;
    private int actualtoRow;
    //added by Amar
    private int totalRepCount = 0;
    private int sheetNumber = 0;
    private int lineNumber = 0;
    private String operType = "";
    private int colNumber = 0;
    private String isHeader = "";
    private String gTotal = "";
    WritableCell cell;
    private ColorGroup colorGroup;
    private ReportParameter repParameter;
    private ArrayList<String> sortColumns = new ArrayList<String>();
    private char[] sortTypes;
    private char[] sortDataTypes;
    Container container = null;
    HashMap<String, BigDecimal> subTotal = new HashMap<String, BigDecimal>();

    //ends
    public PbExcelGenerator() {
    }

    public void setContainer(Container container) {
        this.container = container;

    }

    public void createExcel() {
        bos = new ByteArrayOutputStream();

        columnCount = displayColumns.length;
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        try {
            actualtoRow = toRow;
            if (fromRow == 0 && toRow == 0) {
                toRow = ret.getViewSequence().size();

            }
            //checking whether records are more than 65000 so that to create required  number of sheets
            if (toRow > 65000) {
                sheet = toRow / 65000;
                toRow = 65000;
            }

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            writableWorkbook = Workbook.createWorkbook(bos, wbSettings);
            //to create multiple sheets
            for (int j = 0; j <= sheet; j++) {
                HeaderFooter header = new HeaderFooter(getHeaderTitle());
                HeaderFooter footer = new HeaderFooter(getHeaderTitle());

                PbReportCollection collect = container.getReportCollect();
                ArrayList<String> sortCols = null;
                char[] sortTypes = null;//ArrayList sortTypes = null;
                char[] sortDataTypes = null;

                if (collect.reportRowViewbyValues.get(0) != null && collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME")) {
                } else {
                    sortCols = container.getSortColumns();
                    String sort = "";
                    if (sortCols != null && !sortCols.isEmpty()) {
                        sortCols = container.getSortColumns();
                        if (!sortCols.isEmpty()) {
                            sortTypes = container.getSortTypes();
                            sortDataTypes = container.getSortDataTypes();
                            ProgenDataSet retObj = container.getRetObj();
                            ArrayList rowSequence = new ArrayList();
                            if (container.isTopBottomSet()) {
                                int topbottomCount = container.getTopBottomCount();
                                if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                                    sort = "1";
//                         container.setSortColumn(sortCols, sort);
                                    if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                        rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                    } else {
                                        rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                    }
//                              retObj.setViewSequence(rowSequence);
                                } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                                    sort = "0";
//                            container.setSortColumn(sortColumn, sort);
                                    if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                        rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                    } else {
                                        rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                    }

                                }
                            } else {
                                rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                            }
                            retObj.setViewSequence(rowSequence);

//                container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes));
//                rowCount = rowSequence.size();
                        } else {
                            ArrayList tableMeasure = container.getTableMeasure();
                            if (tableMeasure != null) {
                                ArrayList sortColumn = new ArrayList();
                                sortColumn.add("A_" + tableMeasure.get(0).toString());
                                char[] sortType = new char[1];//new String[1];
                                sortType[0] = ' ';
                                char[] sortdataType = new char[1];
                                sortdataType[0] = 'N';
                                container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortColumn, sortType, sortdataType));
                            }
                        }
                    }
                }

//            if ((ret.getRowCount() % maxRows) == 0) {
//                countOfSheets = ret.getRowCount() / maxRows;
//            } else {
//                countOfSheets = (ret.getRowCount() / maxRows) + 1;
//            }
//            writableSheets = new WritableSheet[countOfSheets];
//
//            for(int k=0;k<writableSheets.length;k++){
//                writableSheets[k] = writableWorkbook.createSheet(getReportName().replace("_", " ") + k, k);
//            }
                writableSheet = writableWorkbook.createSheet(getReportName().replace("_", " ") + (j + 1), j);
                writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                //writableCellFormat.setBackground(Colour.GRAY_25);
                writableCellFormat.setWrap(false);

                /*
                 * CellView TitleCellView = new CellView(); CellView
                 * HeadingCellView = new CellView(); TitleCellView.setSize(256 *
                 * 50); HeadingCellView.setSize(256 * 20);
                 *
                 * writableSheet.setColumnView(0, TitleCellView);
                 * writableSheet.setColumnView(1, HeadingCellView);
                 *
                 *
                 * writableSheet.setRowView(0, (20 * 20), false);
                 * writableSheet.setRowView(1, (20 * 20), false);
                 * writableSheet.setRowView(2, (20 * 20), false);
                 *
                 * titleLabel.setCellFormat(writableCellFormat);
                 * reportLabel.setCellFormat(writableCellFormat);
                 * dateLabel.setCellFormat(writableCellFormat);
                 */
                //Start of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
                Date FromDate;
                Date ToDate;
                Date CmpFromDate;
                Date CmpToDate;
                // [Day, PRG_STD, 06/25/2013, Month, Last Period]
//             System.out.println("timeDetailsArray-----------------"+timeDetailsArray.toString());

                //end of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
                if (this.getParamType().equalsIgnoreCase("withParameters")) {
                    titleLabel = new Label(0, 0, getHeaderTitle());
                    reportLabel = new Label(0, 1, "Report Title  : " + getReportName().replace("_", " "));
//            dateLabel = new Label(0, 2, "Created Date : " + sdf.format(date));
                    dateLabel = new Label(0, 2, "Created Date : " + sdf1.format(date));
                } else {
                    reportLabel = new Label(0, 1, "Report Title  : " + getReportName().replace("_", " "));
//                dateLabel = new Label(0, 2, "Created Date : " + sdf.format(date));
                    dateLabel = new Label(0, 2, "Created Date : " + sdf1.format(date));
                }

                ArrayList<String> values = new ArrayList<String>();
                values.add(getReportName().replace("_", " "));
                values.add(sdf.format(date));
//            if(!timeDetailsArray.isEmpty()){
//                for(int i=0;i<values.size();i++){
//                    if(timeDetailsArray.contains(i))
//                    timeDetailsArray.remove(values.get(i));
//                }
//            }

                ArrayList extraHeaderRows = new ArrayList();
                if (this.getParamType() == null || this.getParamType().equalsIgnoreCase("withParameters")) {
                    if (timeDetailsArray != null && timeDetailsArray.size() != 0) {
                        if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                            //Start of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
//                    extraHeaderRows.add("Date : " + timeDetailsArray.get(2));
//                    extraHeaderRows.add("Duration : " + timeDetailsArray.get(3));
//                    extraHeaderRows.add("Compare with : " + timeDetailsArray.get(4));
                            //[Day, PRG_STD, 06/25/2013, Month, Last Period]
                            FromDate = sdf.parse(timeDetailsArray.get(2).toString());
//                    ToDate = sdf.parse(timeDetailsArray.get(3).toString());
//                    CmpFromDate = sdf.parse(timeDetailsArray.get(4).toString());
//                    CmpToDate = sdf.parse(timeDetailsArray.get(5).toString());
                            extraHeaderRows.add("Date : " + sdf1.format(FromDate));
                            extraHeaderRows.add("Duration : " + timeDetailsArray.get(3));
                            extraHeaderRows.add("Compare with : " + timeDetailsArray.get(4));
                            //end of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY

                            if (filterValues != null && !filterValues.isEmpty()) {
                                for (int i = 0; i < filterValues.size(); i++) {
                                    extraHeaderRows.add("Filter Applyed On  " + filterValues.get(i));
                                }
                            }

                        } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                            //Start of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
//                     extraHeaderRows.add("FromDate : " + timeDetailsArray.get(2));
//                     extraHeaderRows.add("ToDate : " + timeDetailsArray.get(3));
//                     extraHeaderRows.add("CompareFromDate : " + timeDetailsArray.get(4));
//                     extraHeaderRows.add("CompareTodate : " + timeDetailsArray.get(5));
                            FromDate = sdf.parse(timeDetailsArray.get(2).toString());
                            ToDate = sdf.parse(timeDetailsArray.get(3).toString());
                            CmpFromDate = sdf.parse(timeDetailsArray.get(4).toString());
                            CmpToDate = sdf.parse(timeDetailsArray.get(5).toString());
                            extraHeaderRows.add("FromDate : " + sdf1.format(FromDate));
                            extraHeaderRows.add("ToDate : " + sdf1.format(ToDate));
                            extraHeaderRows.add("CompareFromDate : " + sdf1.format(CmpFromDate));
                            extraHeaderRows.add("CompareTodate : " + sdf1.format(CmpToDate));
                            //end of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY

                            if (timeDetailsArray.size() > 5) {
                                for (int i = 6; i < timeDetailsArray.size(); i++) {
                                    if (timeDetailsArray.get(4) == null && timeDetailsArray.get(5) == null) {
                                        timeDetailsArray.remove(4);
                                        timeDetailsArray.remove(5);
                                    }

                                    if (filterValues != null && !filterValues.isEmpty()) {
                                        for (int k = 0; k < filterValues.size(); k++) {
                                            extraHeaderRows.add("Filter Applyed On  " + filterValues.get(k));
                                        }
                                    }

                                }
                            }
                        } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                            extraHeaderRows.add("");
                        } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_RANGE")) {
                            extraHeaderRows.add("");
                        } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                            extraHeaderRows.add("");
                        } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_CMP")) {
                            extraHeaderRows.add("");
                        } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                            extraHeaderRows.add("");
                        }
                    }
                    int counter = 2;
                    Label label1 = null;
                    for (int i = 0; i < extraHeaderRows.size(); i++) {
                        label1 = new Label(0, ++counter, "" + extraHeaderRows.get(i));
                        label1.setCellFormat(writableCellFormat);
                        writableSheet.addCell(label1);
                        rowStart = counter + 2;
                        rowStartTarget = rowStart + 2;
                    }
                }

                for (int i = 0; i < rowStart - 2; i++) {
                    writableSheet.setRowView(i, (20 * 20), false);
                }
                if (this.getParamType() == null || this.getParamType().equalsIgnoreCase("withParameters")) {
                    titleLabel.setCellFormat(writableCellFormat);
                    reportLabel.setCellFormat(writableCellFormat);
                    dateLabel.setCellFormat(writableCellFormat);
                } else {
                    reportLabel.setCellFormat(writableCellFormat);
                    dateLabel.setCellFormat(writableCellFormat);
                }

                if (this.getParamType() == null || this.getParamType().equalsIgnoreCase("withParameters")) {
                    writableSheet.addCell(titleLabel);
                    writableSheet.addCell(reportLabel);
                    writableSheet.addCell(dateLabel);
                } else {
                    writableSheet.addCell(reportLabel);
                    writableSheet.addCell(dateLabel);
                }

                if (displayType.equalsIgnoreCase("Report")) {
                    buildTable();
                } else if (displayType.equalsIgnoreCase("Graph")) {
                    buildGraph();
                } else if (displayType.equalsIgnoreCase("ReportAndGraph")) {
                    buildGraph();
                    buildTable();
                } else {
                    buildTable();
                }
                toRow = toRow + 65000;
                fromRow = fromRow + 65000;

            }
            writableWorkbook.write();
            writableWorkbook.close();

            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(bos.size());

            outputstream = response.getOutputStream();
            bos.writeTo(outputstream);
            outputstream.flush();

        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
    }

    private void buildTable() throws Exception {
        boolean isCrossTabReport = false;
        if (container.getReportCollect().reportColViewbyValues != null && container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        PbReportCollection collect = container.getReportCollect();
        ArrayList list = collect.reportColViewbyValues;

        for (int i = 0; i < columnCount; i++) {
            if (isCrossTabReport) {
                DataFacade facade = new DataFacade(container);  //by Ram
                if (facade.container.isHideMsrHeading()) {
                    String str = displayLabels[i];
                    str = str.replace("[", "").replace("]", "");
                    String[] st = str.split(",");
                    label = new Label(i, rowStart, st[0]);
                } else {
                    label = new Label(i, rowStart, displayLabels[i]);
                }
            } else if (displayLabels[i].contains("Prev Month") && this.getAggregationForSubtotal(displayColumns[i]).contains("#T")) {
                label = new Label(i, rowStart, container.getMonthNameforTrailingFormula(i));
            } else {
                label = new Label(i, rowStart, displayLabels[i]);
            }
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);
            writableCellFormat.setAlignment(Alignment.GENERAL);
            if (i >= container.getViewByCount()) {
                String alignment = null;
                if (isCrossTabReport) {
                    if (crosstabMeasureId.containsKey(displayColumns[i]) && container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString()) != null) {
                        alignment = container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString());
                    }
                } else if (container.getMeasureAlign(displayColumns[i]) != null) {
                    alignment = container.getMeasureAlign(displayColumns[i]).toString();
                }
                if (alignment != null) {
                    if (alignment.equalsIgnoreCase("left")) {
                        writableCellFormat.setAlignment(Alignment.LEFT);
                    } else if (alignment.equalsIgnoreCase("center")) {
                        writableCellFormat.setAlignment(Alignment.CENTRE);
                    } else if (alignment.equalsIgnoreCase("right")) {
                        writableCellFormat.setAlignment(Alignment.RIGHT);
                    } else {
                        writableCellFormat.setAlignment(Alignment.GENERAL);
                    }
                }
            }
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);

            HeadingCellView = new CellView();
            HeadingCellView.setSize(256 * 20);
            writableSheet.setColumnView(i, HeadingCellView);
        }

        //  System.out.println("***"+fromRow+"....."+toRow);
        if (container.getViewByCount() > 1 && actualtoRow < 65000) {
//            System.out.println("****actualtoRow....."+actualtoRow);
            DataFacade facade = new DataFacade(container);
            facade.setCtxPath(container.getReportCollect().ctxPath);
            TableBuilder tableBldr = new RowViewTableBuilder(facade);
            if (fromRow == 0 && toRow == 0) {
                tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
            } else {
                tableBldr.setFromAndToRow(fromRow, toRow);
            }
            TableDisplay bodyHelper = null;
            TableDisplay subTotalHelper = null;
            bodyHelper = new ExcelTableBodyDisplay(tableBldr);
            bodyHelper.setHeadlineflag("Export");
            bodyHelper.setWritableSheet(writableSheet);
            bodyHelper.setWritableWorkbook(writableWorkbook);
            subTotalHelper = new ExcelTableSubTotalDisplay(tableBldr);
            subTotalHelper.setWritableSheet(writableSheet);
            subTotalHelper.setWritableWorkbook(writableWorkbook);
            bodyHelper.setNext(subTotalHelper);
            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(bodyHelper.generateOutputHTML());
        } else {

            //writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
//        writableCellFormat = new WritableCellFormat(writableFont);
//        writableCellFormat.setWrap(true);
            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);

//         if (fromRow == 0 && toRow == 0){
//             toRow = ret.getViewSequence().size();
//         }
            PbReportTableBD reportTableBD = new PbReportTableBD();
            reportTableBD.searchDataSet(this.container);
            ArrayList<Integer> sortSequence = null;
            if (sortTypes != null && sortDataTypes != null) {
                sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
            } else {
                sortSequence = ret.getViewSequence();
            }

//         if(toRow>65000){
//             toRow1=65000;
//         }
//
            DataFacade facade1 = new DataFacade(container);
            int k = 0;
            for (int i = fromRow; i < toRow; i++) {
                if (i < actualtoRow) {
                    for (int j = 0; j < columnCount; j++) {
                        String colName = displayColumns[j];
                        String discolName = displayLabels[j];
                        //int PerCentColumnIndex = colName.lastIndexOf("_percentwise");
                        Colour BGColor = null;

                        //added by Dinanath for color group
                        TableBuilder builder = null;
                        BigDecimal subtotalval = null;
                        String bgColor = null;
                        int actualRow = facade1.getActualRow(i);
                        Colour clr = null;
                        try {
                            bgColor = facade1.getColor(actualRow, colName, subtotalval);
                        } catch (Exception e) {
                            // logger.error("Exception: ",e);
                        }
                        try {
                            clr = DataSnapshotGenerator.getNearestColour(bgColor);
                        } catch (NumberFormatException e) {
                            clr = DataSnapshotGenerator.getNearestColour("#ffffff");
                        } catch (Exception e) {
                        }
                        writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                        cf2 = new WritableCellFormat(writableFont, NumberFormats.THOUSANDS_FLOAT);
                        cf2.setBackground(clr);
                        //cf2.setBorder(Border.ALL, BorderLineStyle.THIN);
                        cf2.setWrap(true);
                        cf2.setAlignment(Alignment.GENERAL);

                        writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                        writableCellFormat = new WritableCellFormat(writableFont);
                        writableCellFormat.setBackground(clr);
                        //writableCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                        writableCellFormat.setWrap(true);
//endded by Dinanath
//                writableCellFormat = new WritableCellFormat(writableFont);
//                writableCellFormat.setWrap(true);
                        writableCellFormat.setAlignment(Alignment.GENERAL);
                        if (j >= container.getViewByCount()) {
                            String alignment = null;
                            if (isCrossTabReport) {
                                if (crosstabMeasureId.containsKey(colName) && container.getTextAlign(crosstabMeasureId.get(colName).toString()) != null) {
                                    alignment = container.getTextAlign(crosstabMeasureId.get(colName).toString());
                                }
                            } else if (container.getTextAlign(colName) != null) {
                                alignment = container.getTextAlign(colName);
                            }
                            if (alignment != null && !alignment.equalsIgnoreCase("")) {
                                if (alignment.equalsIgnoreCase("left")) {
                                    writableCellFormat.setAlignment(Alignment.LEFT);
                                    cf2.setAlignment(Alignment.LEFT);
                                } else if (alignment.equalsIgnoreCase("center")) {
                                    writableCellFormat.setAlignment(Alignment.CENTRE);
                                    cf2.setAlignment(Alignment.CENTRE);
                                } else if (alignment.equalsIgnoreCase("right")) {
                                    writableCellFormat.setAlignment(Alignment.RIGHT);
                                    cf2.setAlignment(Alignment.RIGHT);
                                } else {
                                    writableCellFormat.setAlignment(Alignment.GENERAL);
                                    cf2.setAlignment(Alignment.GENERAL);
                                }
                            }
                        }

                        if ("D".equals(types[j])) {
                            //Start of code by Manik for date format change during export
                            try {
                                String dateformat1 = container.getDateFormatt(colName);
                                SimpleDateFormat sdf1;
                                String format = "";
                                if (dateformat1 == null || "null".equals(dateformat1)) {
                                    format = "yyyy-MMM-dd HH:mm:ss";
                                } else {
                                    format = dateformat1;
                                }
                                sdf1 = new SimpleDateFormat(format);

                                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yy");
                                String date1 = ret.getFieldValueDateString(sortSequence.get(i), colName);
                                Date date2 = sdf2.parse(date1);
                                String formatedDate = sdf1.format(date2);
                                cell = new Label(j, k + rowStart + 1, formatedDate);
                                //Start of code by manik for date format change during export
                                // cell = new Label(j, k + rowStart + 1, ret.getFieldValueDateString(sortSequence.get(i), colName));
                                cell.setCellFormat(writableCellFormat);
                            } catch (Exception ex) {
                                logger.error("Exception: ", ex);
                            }
                        } else if ("N".equals(types[j]) && !container.getReportCollect().reportRowViewbyValues.contains(colName.replace("A_", ""))) {
                            if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                                String formattedData = "";
                                String snbrSymbol = "";
                                String nbrSymbol = "";
                                if (ret.getFieldValueBigDecimal(sortSequence.get(i), colName) != null) {
//                            String element = container.getDisplayColumns().get(j);
                                    String element = colName;
                                    HashMap NFMap = new HashMap();
                                    NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                                    // HashMap<String,String> crosstabMeasureId=((PbReturnObject)container.getRetObj()).crosstabMeasureId;
                                    if (NFMap != null) {
                                        if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                            if (crosstabMeasureId.containsKey(element) && NFMap.get(crosstabMeasureId.get(element)) != null) {
                                                nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                            }
                                        } else if (NFMap.get(element) != null) {
                                            nbrSymbol = String.valueOf(NFMap.get(element));
                                        }
                                    }
//                             snbrSymbol = container.symbol.get(container.getDisplayColumns().get(j));
                                    snbrSymbol = container.symbol.get(colName);
                                    int precision = container.getRoundPrecisionForMeasure(element);
                                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(element)) {
                                        precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                                    }
                                    if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                        formattedData = ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString();
                                    } else if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                        formattedData = this.convertDataToTime(new BigDecimal(ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString()));
                                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                        formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                        formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                        // formattedData = snbrSymbol+""+formattedData;
                                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                        formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                        if (snbrSymbol.equalsIgnoreCase("%")) {
                                            formattedData = formattedData + "" + snbrSymbol;
                                        } else {
                                            formattedData = snbrSymbol + "" + formattedData;
                                        }
                                    } else {
                                        formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
//                          HashMap colProps = container.getColumnProperties();
//                          if (colProps != null && colProps.containsKey(element)) {
//                          ArrayList propList = (ArrayList) colProps.get(element);
//                           String colSymbol = (String) propList.get(7);
//                           if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) {
//                            if (colSymbol.equalsIgnoreCase("%")) {
//                              formattedData = formattedData + colSymbol;
//                           } else {
//                           formattedData = colSymbol + formattedData;
//                          }
//                          }
//                        }
                                    }
                                }
                                if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    cell = new Label(j, k + rowStart + 1, formattedData, writableCellFormat);
                                } else if (!sortSequence.isEmpty()) {
                                    if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                        cell = new Label(j, k + rowStart + 1, formattedData, writableCellFormat);
                                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                        cell = new Label(j, k + rowStart + 1, formattedData, writableCellFormat);
                                    } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                        cell = new Number(j, k + rowStart + 1, Double.parseDouble((formattedData).replace(",", "")), cf2);
                                    } else {
                                        cell = new Label(j, k + rowStart + 1, formattedData, cf2);
                                    }
                                } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    cell = new Label(j, k + rowStart + 1, formattedData, writableCellFormat);
                                } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                    cell = new Number(j, k + rowStart + 1, Double.parseDouble((formattedData).replace(",", "")), cf2);
                                } else {
                                    cell = new Label(j, k + rowStart + 1, formattedData, cf2);
                                } // break;
                            } else {
//                        if ( PerCentColumnIndex != -1 )
//                        {
//                            colName = colName.replace("_percentwise", "");
//                            cell = new Number(j, i + rowStart + 1, Double.parseDouble((ret.getFieldValueBigDecimal(i, colName).divide(ret.getColumnGrandTotalValue(colName), MathContext.DECIMAL64).multiply(multiplier)).toString()),cf2);
//                        }
//                        else if ( RTMeasureElement.getMeasureType(colName) == RTMeasureElement.RANK )
//                        {
                                //changed by sruthi for % wise excel
                                String nbrSymbol = "";
                                String formattedData = "";
                                int precision = container.getRoundPrecisionForMeasure(colName);
                                if (!sortSequence.isEmpty()) {
                                    nbrSymbol = container.getNumberSymbol(colName);

                                    if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("%") && !nbrSymbol.equalsIgnoreCase("")) {
                                        formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName), nbrSymbol, precision);
                                        cell = new Label(j, k + rowStart + 1, formattedData, cf2);
                                    } else {
                                        cell = new Number(j, k + rowStart + 1, ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName).doubleValue(), cf2);
                                    }
                                } else {
                                    nbrSymbol = container.getNumberSymbol(colName);
                                    if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("%") && !nbrSymbol.equalsIgnoreCase("")) {
                                        formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueRuntimeMeasure(i, colName), nbrSymbol, precision);
                                        cell = new Label(j, k + rowStart + 1, formattedData, cf2);
                                    } else {
                                        cell = new Number(j, k + rowStart + 1, ret.getFieldValueRuntimeMeasure(i, colName).doubleValue(), cf2);
                                    }
                                }
//                        }

                            }
                            //ended by sruthi
//                    cell.setCellFormat(writableCellFormat);
//                    BGColor = getColor(colName, ret.getFieldValueString(i, colName));
                            HashMap<String, Integer> RGBColerCodes = null;
                            if (!sortSequence.isEmpty()) {
                                RGBColerCodes = getColor(colName, ret.getFieldValueString(sortSequence.get(i), colName));
                            } else {
                                RGBColerCodes = getColor(colName, ret.getFieldValueString(i, colName));
                            }
                            Collection<Integer> collection = RGBColerCodes.values();
                            TreeSet<Integer> treeSet = new TreeSet<Integer>();
                            for (Integer val : collection) {
                                treeSet.add(val);
                            }
                            String tempStr = "";
                            for (String string : RGBColerCodes.keySet()) {
                                if (RGBColerCodes.get(string) == treeSet.last()) {
                                    tempStr = string;
                                }
                            }

                            if (!RGBColerCodes.isEmpty()) {
                                writableCellFormatForColorCode = new WritableCellFormat(writableFont);
                                writableCellFormatForColorCode.setWrap(true);
                                if (tempStr.equalsIgnoreCase("RED")) {
                                    if (RGBColerCodes.get("RED") >= 239 && RGBColerCodes.get("RED") < 244) {
                                        writableWorkbook.setColourRGB(Colour.ORANGE, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                        writableCellFormatForColorCode.setBackground(Colour.ORANGE);
                                    } else if (RGBColerCodes.get("RED") > 244 && RGBColerCodes.get("RED") <= 248) {
                                        writableWorkbook.setColourRGB(Colour.YELLOW, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                        writableCellFormatForColorCode.setBackground(Colour.YELLOW);
                                    } else {
                                        writableWorkbook.setColourRGB(Colour.RED, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                        writableCellFormatForColorCode.setBackground(Colour.RED);
                                    }
                                } else if (tempStr.equalsIgnoreCase("GREEN")) {
                                    writableWorkbook.setColourRGB(Colour.GREEN, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.GREEN);
                                } else if (RGBColerCodes.get("BLUE") > 244 && RGBColerCodes.get("BLUE") <= 248) {
                                    writableWorkbook.setColourRGB(Colour.VIOLET, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.VIOLET);
                                } else if (RGBColerCodes.get("BLUE") >= 103 && RGBColerCodes.get("BLUE") < 244) {
                                    writableWorkbook.setColourRGB(Colour.INDIGO, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.INDIGO);
                                } else {
                                    writableWorkbook.setColourRGB(Colour.BLUE, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.BLUE);
                                }

                                cell.setCellFormat(writableCellFormatForColorCode);
                            }

                        } else {
                            if (!sortSequence.isEmpty()) {
                                cell = new Label(j, k + rowStart + 1, ret.getFieldValueString(sortSequence.get(i), colName));
                            } else {
                                cell = new Label(j, k + rowStart + 1, ret.getFieldValueString(i, colName));
                            }
                            cell.setCellFormat(writableCellFormat);
                        }
                        writableSheet.addCell(cell);
                        colName = null;
                        writableCellFormatForColorCode = null;
                    }
                }
                k++;
            }

//       if(ret.getRowCount()==k){
            this.generateSubTotalHtml(k, cf2);
//            }

        }

    }

    private void buildGraph() throws Exception {
        // Image image = null;
        File file = null;
        int columnStart = 0;
        //FileInputStream fis = null;
        //byte[] bytes = null;
        for (int fileCount = 0; fileCount < getFilePaths().length; fileCount++) {
            int index = getFilePaths()[fileCount].indexOf("jfreechart");
            file = new File(System.getProperty("java.io.tmpdir") + "/" + getFilePaths()[fileCount].substring(index));
            /*
             * fis = new FileInputStream(file); bytes = new
             * byte[fis.available()]; while (-1 != fis.read(bytes)) { } image =
             * Image.getInstance(bytes);
             */
            if (file != null) {
                writableImage = new WritableImage(columnStart, rowStart, 10, 10, file);
                writableSheet.addImage(writableImage);
                columnStart += 12;
                rowStart = rowStart + 12;
            }
        }
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public ProgenDataSet getRet() {
        return ret;
    }

    public void setRet(ProgenDataSet ret) {
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
    //Added by Amar

    public void setTotalValidReport(int count) {
        this.totalRepCount = count;
    }

    public void setSheetNumber(int sheet) {
        this.sheetNumber = sheet;
    }

    public void setLineNumber(int line) {
        this.lineNumber = line;
    }

    public void setOperationType(String opType) {
        this.operType = opType;
    }

    public String getOperationType() {
        return operType;
    }
    // Added by Amar

    public void setColNumber(int col) {
        this.colNumber = col;

    }

    public int getColNumber() {
        return colNumber;
    }

    public void setHeader(String head) {
        this.isHeader = head;
    }

    public String getHeader() {
        return isHeader;
    }

    public void setGTotal(String gt) {
        this.gTotal = gt;
    }

    public String getGTotal() {
        return gTotal;
    }
// end of code

    public void createExcelForTarget() throws SQLException, Exception {

        PbTargetExcelXml pbTargetExcelXml = new PbTargetExcelXml();
        bos = new ByteArrayOutputStream();
        titleLabel = new Label(0, 0, "pi EE");
        reportLabel = new Label(0, 1, "Target Title  : " + getTargetName().replace("_", " "));
        dateLabel = new Label(0, 2, "Created Date : " + sdf.format(date));
        timeRangeLabel = new Label(0, 3, "Data From: " + startRange + " To " + endRange);

        int targetDetId = pbTargetExcelXml.generateTargetDetId();
        setFileName("Target-" + targetId + "-" + targetDetId + ".xls");

        String fName = getFileName();

        columnCount = displayColumns.length;
        rowCount = targetdisplayRows.length;

        String nonallIds = "";
        HashMap nonAll = getNonAllVals();
        String val[] = (String[]) nonAll.keySet().toArray(new String[0]);
        String k = "";
        String v = "";
        for (int p = 0; p < val.length; p++) {
            k = val[p];
            v = nonAll.get(k).toString();
            // if(!v.equalsIgnoreCase("-1")||!v.equalsIgnoreCase("All"))
            nonallIds = nonallIds + "~" + k + "=" + v;
        }
        if (nonallIds.length() > 0) {
            nonallIds = nonallIds.substring(1);
        }
        String restrictingTotalVal = "";

        String resVals[] = (String[]) RestrictingTotal.keySet().toArray(new String[0]);
        for (int u = 0; u < resVals.length; u++) {
            String key = resVals[u];
            String val2 = "";
            if (RestrictingTotal.containsKey(key)) {
                long v2 = 0;
                v2 = ((Long) RestrictingTotal.get(key)).longValue();
                restrictingTotalVal = restrictingTotalVal + "~" + key + "=" + v2;
            }
        }
        if (restrictingTotalVal.length() > 0) {
            restrictingTotalVal = restrictingTotalVal.substring(1);
        }

        String insertExcelData = "insert into target_excelMaster (TARGET_EXCEL_DET,TARGET_ID,PERIODTYPE,PRIMARYVIEWBY,"
                + " SECVIEWBY,NONALLVALUE,FILENAME,ROWVIEWBYVALUES,COLUMNVIEWBYVALUES,TIMEVAL,TARGET_BASIS,DATAQUERY,DATAQUERY2,PARENTDATAQUERY,PARENTDATAQUERY2,GRANDTOTAL) "
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        OraclePreparedStatement opstmt = null;
        Connection connection = ProgenConnection.getInstance().getConnection();
        opstmt = (OraclePreparedStatement) connection.prepareStatement(insertExcelData);

//        opstmt.setString(1, "" + targetDetId + "");
        opstmt.setString(1, String.valueOf(targetDetId));
        opstmt.setString(2, targetId);
        opstmt.setString(3, periodType);
        opstmt.setString(4, primaryAnalyze);
        opstmt.setString(5, secAnalyze);
        opstmt.setString(6, nonallIds);
        opstmt.setString(7, fName);
        opstmt.setStringForClob(8, "");
        opstmt.setStringForClob(9, "");
        opstmt.setString(10, getStartRange());
        opstmt.setString(11, getTargetBasis());

        opstmt.setStringForClob(12, dataQuery);
        opstmt.setStringForClob(13, dataQuery2);
        opstmt.setStringForClob(14, parentDataQ);
        opstmt.setStringForClob(15, parentDataQ2);
        opstmt.setStringForClob(16, restrictingTotalVal);

        int rows = opstmt.executeUpdate();
        pbTargetExcelXml.setPrimList(primList);
        pbTargetExcelXml.setSecList(secList);

//        pbTargetExcelXml.createDocumentForPrim("" + targetDetId + "");
//        pbTargetExcelXml.createDocumentForSec("" + targetDetId + "");
        pbTargetExcelXml.createDocumentForPrim(String.valueOf( targetDetId ));
        pbTargetExcelXml.createDocumentForSec(String.valueOf( targetDetId) );

        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);
        try {
            writableWorkbook = Workbook.createWorkbook(bos, new WorkbookSettings());
            writableSheet = writableWorkbook.createSheet(getTargetName().replace("_", " "), 0);

            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(false);
            writableSheet.setRowView(0, (20 * 20), false);
            writableSheet.setRowView(1, (20 * 20), false);
            writableSheet.setRowView(2, (20 * 20), false);
            writableSheet.setRowView(3, (20 * 20), false);

            titleLabel.setCellFormat(writableCellFormat);
            reportLabel.setCellFormat(writableCellFormat);
            dateLabel.setCellFormat(writableCellFormat);
            /*
             * writableSheet.addCell(titleLabel);
             * writableSheet.addCell(reportLabel);
             * writableSheet.addCell(dateLabel);
             */

            timeRangeLabel.setCellFormat(writableCellFormat);

            writableSheet.addCell(titleLabel);
            writableSheet.addCell(reportLabel);
            writableSheet.addCell(dateLabel);
            writableSheet.addCell(timeRangeLabel);

            buildTargetTable();
            writableWorkbook.write();

            writableWorkbook.close();
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(bos.size());
            outputstream = response.getOutputStream();

            bos.writeTo(outputstream);

            outputstream.flush();
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
    }

    private void buildTargetTable() throws Exception {
        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        writableCellFormat = new WritableCellFormat(writableFont);
        writableCellFormat.setBackground(Colour.GRAY_25);
        writableCellFormat.setWrap(true);
        label = new Label(0, rowStartTarget, "");
        label.setCellFormat(writableCellFormat);
        writableSheet.addCell(label);
        HeadingCellView = new CellView();
        HeadingCellView.setSize(256 * 20);
        writableSheet.setColumnView(0, HeadingCellView);

        label = new Label(0, rowStartTarget, PrimaryViewByName);
        label.setCellFormat(writableCellFormat);
        writableSheet.addCell(label);

        for (int i = 0; i < columnCount; i++) {

            label = new Label(i + 1, rowStartTarget, displayColumns[i]);
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);
            HeadingCellView = new CellView();
            HeadingCellView.setSize(256 * 20);
            writableSheet.setColumnView(i + 1, HeadingCellView);

        }

        if (!primaryAnalyze.equalsIgnoreCase("0") && !secAnalyze.equalsIgnoreCase("Time")) {

            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            label = new Label(columnCount + 1, rowStartTarget, "Restricting Total.");
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);

            label = new Label(columnCount + 2, rowStartTarget, "Current Total.");
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);

            label = new Label(columnCount + 3, rowStartTarget, "Difference");
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);
        }

        if (primaryAnalyze.equalsIgnoreCase("0")) {

            if (!RestrictingTotal.isEmpty()) {
                label = new Label(columnCount + 2, rowStartTarget + 1, "Restricting Total");
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
            }
        }

        writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
        writableCellFormat = new WritableCellFormat(writableFont);
        writableCellFormat.setWrap(true);
        String value = "";
        String key = "";

        for (int i = 0; i < rowCount; i++) {
            // for (int j = 0; j <1; j++) {

            // label = new Label(i, i + rowStartTarget+1,targetdisplayRows[i]);
            label = new Label(0, 1 + rowStartTarget + i, targetdisplayRows[i]);
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);
            value = "";
            if (targetBasis.equalsIgnoreCase("Absolute")) {
                for (int m = 0; m < columnCount; m++) {

                    key = targetdisplayRows[i] + ":" + displayColumns[m];
                    if (targetOriMap.containsKey(key)) {
                        value = String.valueOf(targetOriMap.get(key));
                    }

                    if (value.equalsIgnoreCase("0")) {
                        value = "";
                    }

                    if (!value.equalsIgnoreCase("")) {
                        Number number = new Number(m + 1, i + rowStartTarget + 1, Long.parseLong(value));
                        writableSheet.addCell(number);
                    } else {
                        label = new Label(m + 1, i + rowStartTarget + 1, value.trim());
                        label.setCellFormat(writableCellFormat);
                        writableSheet.addCell(label);
                    }

                    // label = new Label(m + 1, i + rowStartTarget + 1, value.trim());
                    // label.setCellFormat(writableCellFormat);
                    // writableSheet.addCell(label);
                }
            } else if (targetBasis.equalsIgnoreCase("Percent")) {

                for (int m = 0; m < columnCount; m++) {

                    long rTotal = 0;
                    key = targetdisplayRows[i] + ":" + displayColumns[m];
                    if (RestrictingTotal.containsKey(displayColumns[m])) {
                        rTotal = ((Long) RestrictingTotal.get(displayColumns[m])).longValue();

                    }
                    if (targetOriMap.containsKey(key)) {
                        value = String.valueOf(targetOriMap.get(key));
                    }

                    if (value.equalsIgnoreCase("0")) {
                        value = "";
                    }

                    double per = 0;
                    if (!value.equalsIgnoreCase("")) {
                        per = Double.parseDouble(value) / rTotal * 100;
                    }
                    if (per != 0) {
                        Number number = new Number(m + 1, i + rowStartTarget + 1, per);
                        writableSheet.addCell(number);
                    } //label = new Label(m + 1, i + rowStartTarget + 1, ""+per+"");
                    else {
                        label = new Label(m + 1, i + rowStartTarget + 1, "");
                        label.setCellFormat(writableCellFormat);
                        writableSheet.addCell(label);
                    }
                    //label = new Label(m + 1, i + rowStartTarget + 1, "");
                    // label.setCellFormat(writableCellFormat);
                    // writableSheet.addCell(label);
                }
            }

            //overalltotal when prim is overalltotal
            if (primaryAnalyze.equalsIgnoreCase("0")) {
                long val = ((Long) RestrictingTotal.get("OverAll")).longValue();
//                String v = "" + val + "";
                String v = String.valueOf( val );
                // label = new Label(columnCount + 1, 1 + rowStartTarget + i, v);
                // label.setCellFormat(writableCellFormat);
                // writableSheet.addCell(label);
                if (!v.equalsIgnoreCase("")) {
                    Number number = new Number(columnCount + 1, 1 + rowStartTarget + i, Long.parseLong(v));
                    writableSheet.addCell(number);
                } else {
                    label = new Label(columnCount + 1, 1 + rowStartTarget + i, v);
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);
                }
            }
            if (!primaryAnalyze.equalsIgnoreCase("0") && !secAnalyze.equalsIgnoreCase("Time")) {
                long val = 0;
                if (RestrictingTotal.containsKey(targetdisplayRows[i])) {
                    val = ((Long) RestrictingTotal.get(targetdisplayRows[i])).longValue();
                }
//                String v = "" + val + "";
                String v = String.valueOf( val );
                // label = new Label(columnCount + 1, i + rowStartTarget + 1, v);
                //label.setCellFormat(writableCellFormat);
                //writableSheet.addCell(label);
                if (!v.equalsIgnoreCase("")) {
                    Number number = new Number(columnCount + 1, i + rowStartTarget + 1, Long.parseLong(v));
                    writableSheet.addCell(number);
                } else {
                    label = new Label(columnCount + 1, i + rowStartTarget + 1, v);
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);
                }
                String currentTotForm = "";
                int p = 0;
                for (int m = 0; m < columnCount; m++) {
                    String totPrefix = "";
                    p = i + rowStartTarget + 2;
                    if (m == 0) {
                        totPrefix = "b";
                    }
                    if (m == 1) {
                        totPrefix = "c";
                    }
                    if (m == 2) {
                        totPrefix = "d";
                    }
                    if (m == 3) {
                        totPrefix = "e";
                    }
                    if (m == 4) {
                        totPrefix = "f";
                    }
                    if (m == 5) {
                        totPrefix = "g";
                    }
                    if (m == 6) {
                        totPrefix = "h";
                    }
                    if (m == 7) {
                        totPrefix = "i";
                    }
                    if (m == 8) {
                        totPrefix = "j";
                    }
                    if (m == 9) {
                        totPrefix = "k";
                    }
                    if (m == 10) {
                        totPrefix = "l";
                    }
                    if (m == 11) {
                        totPrefix = "m";
                    }
                    if (m == 12) {
                        totPrefix = "n";
                    }
                    if (m == 13) {
                        totPrefix = "o";
                    }
                    if (m == 14) {
                        totPrefix = "p";
                    }
                    if (m == 15) {
                        totPrefix = "q";
                    }
                    if (m == 16) {
                        totPrefix = "r";
                    }
                    if (m == 17) {
                        totPrefix = "s";
                    }
                    if (m == 18) {
                        totPrefix = "t";
                    }
                    if (m == 19) {
                        totPrefix = "u";
                    }
                    if (m == 20) {
                        totPrefix = "v";
                    }
                    if (m == 21) {
                        totPrefix = "w";
                    }
                    if (m == 22) {
                        totPrefix = "x";
                    }
                    if (m == 23) {
                        totPrefix = "y";
                    }
                    if (m == 24) {
                        totPrefix = "z";
                    }
                    currentTotForm = currentTotForm + "+" + totPrefix + p;

                }
                if (currentTotForm.length() > 0) {
                    currentTotForm = currentTotForm.substring(1);
                }
                Formula f = new Formula(columnCount + 2, i + rowStartTarget + 1, currentTotForm);
                writableSheet.addCell(f);
                // //////////////////////////.println(" currentTotForm =-"+currentTotForm);
                String diffFormula = "";
                String diffPrefix2 = "";
                String diffPrefix3 = "";
                int p2 = columnCount + 1;
                int p3 = columnCount + 1;
                if (p2 == 1) {
                    diffPrefix2 = "b";
                    diffPrefix3 = "c";
                }
                if (p2 == 2) {
                    diffPrefix2 = "c";
                    diffPrefix3 = "d";
                }
                if (p2 == 3) {
                    diffPrefix2 = "d";
                    diffPrefix3 = "e";
                }
                if (p2 == 4) {
                    diffPrefix2 = "e";
                    diffPrefix3 = "f";
                }
                if (p2 == 5) {
                    diffPrefix2 = "f";
                    diffPrefix3 = "g";
                }
                if (p2 == 6) {
                    diffPrefix2 = "g";
                    diffPrefix3 = "h";
                }
                if (p2 == 7) {
                    diffPrefix2 = "h";
                    diffPrefix3 = "i";
                }
                if (p2 == 8) {
                    diffPrefix2 = "i";
                    diffPrefix3 = "j";
                }
                if (p2 == 9) {
                    diffPrefix2 = "j";
                    diffPrefix3 = "k";
                }
                if (p2 == 10) {
                    diffPrefix2 = "k";
                    diffPrefix3 = "l";
                }
                if (p2 == 11) {
                    diffPrefix2 = "l";
                    diffPrefix3 = "m";
                }
                if (p2 == 12) {
                    diffPrefix2 = "m";
                    diffPrefix3 = "n";
                }
                if (p2 == 13) {
                    diffPrefix2 = "n";
                    diffPrefix3 = "o";
                }
                if (p2 == 14) {
                    diffPrefix2 = "o";
                    diffPrefix3 = "p";
                }
                if (p2 == 15) {
                    diffPrefix2 = "p";
                    diffPrefix3 = "q";
                }
                if (p2 == 16) {
                    diffPrefix2 = "q";
                    diffPrefix3 = "r";
                }
                if (p2 == 17) {
                    diffPrefix2 = "r";
                    diffPrefix3 = "s";
                }
                if (p2 == 18) {
                    diffPrefix2 = "s";
                    diffPrefix3 = "t";
                }
                if (p2 == 19) {
                    diffPrefix2 = "t";
                    diffPrefix3 = "u";
                }
                if (p2 == 20) {
                    diffPrefix2 = "u";
                    diffPrefix3 = "v";
                }
                if (p2 == 21) {
                    diffPrefix2 = "v";
                    diffPrefix3 = "w";
                }
                if (p2 == 22) {
                    diffPrefix2 = "w";
                    diffPrefix3 = "x";
                }
                if (p2 == 23) {
                    diffPrefix2 = "x";
                    diffPrefix3 = "y";
                }
                if (p2 == 24) {
                    diffPrefix2 = "y";
                    diffPrefix3 = "z";
                }
                if (p2 == 25) {
                    diffPrefix2 = "z";
                    diffPrefix3 = "aa";
                }

                int t1 = i + rowStartTarget + 2;
                int t2 = i + rowStartTarget + 2;
                diffFormula = diffPrefix2 + t1 + "-" + diffPrefix3 + t2;
                Formula f2 = new Formula(columnCount + 3, i + rowStartTarget + 1, diffFormula);
                writableSheet.addCell(f2);

            }
        }

        if (!primaryAnalyze.equalsIgnoreCase("0") && secAnalyze.equalsIgnoreCase("Time")) {
            label = new Label(0, rowCount + rowStartTarget + 1, "Restricting Total");
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);

            if (targetBasis.equalsIgnoreCase("Absolute")) {
                for (int i = 0; i < columnCount; i++) {
                    long val = 0;
                    if (RestrictingTotal.containsKey(displayColumns[i])) {
                        val = ((Long) RestrictingTotal.get(displayColumns[i])).longValue();
                    }
//                    String v = "" + val + "";
                    String v =String.valueOf( val) ;
                    if (v.equalsIgnoreCase("0")) {
                        v = "";
                    }

                    // label = new Label(i + 1, 1 + rowCount + rowStartTarget, v);
                    // label.setCellFormat(writableCellFormat);
                    // writableSheet.addCell(label);
                    if (!v.equalsIgnoreCase("")) {
                        Number number = new Number(i + 1, 1 + rowCount + rowStartTarget, Long.parseLong(v));
                        writableSheet.addCell(number);
                    } else {
                        label = new Label(i + 1, 1 + rowCount + rowStartTarget, v);
                        label.setCellFormat(writableCellFormat);
                        writableSheet.addCell(label);
                    }
                }
            } else if (targetBasis.equalsIgnoreCase("Percent")) {
                for (int i = 0; i < columnCount; i++) {
                    long val = 0;
                    if (RestrictingTotal.containsKey(displayColumns[i])) {
                        val = ((Long) RestrictingTotal.get(displayColumns[i])).longValue();
                    }
//                    String v = "" + val + "";
                    String v = String.valueOf(val );
                    long perVal = 100;
                    if (v.equalsIgnoreCase("0")) {
                        v = "";
                        perVal = 0;
                    }

                    //  label = new Label(i + 1, 1 + rowCount + rowStartTarget, ""+perVal+"");
                    //  label.setCellFormat(writableCellFormat);
                    //  writableSheet.addCell(label);
                    if (perVal != 0) {
                        Number number = new Number(i + 1, 1 + rowCount + rowStartTarget, perVal);
                        writableSheet.addCell(number);
                    }
                }
            }
        }

        //added on 15-02-2010 for difference and Current Total start
        if (!primaryAnalyze.equalsIgnoreCase("0") && secAnalyze.equalsIgnoreCase("Time")) {
            if (targetBasis.equalsIgnoreCase("Absolute")) {
                label = new Label(0, rowCount + rowStartTarget + 2, "Current Total");
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
                long tot = 0;

                for (int m = 0; m < columnCount; m++) {
                    String CFormula = "";
                    String prefix = "";
                    if (m == 0) {
                        prefix = "b";
                    } else if (m == 1) {
                        prefix = "c";
                    } else if (m == 2) {
                        prefix = "d";
                    } else if (m == 3) {
                        prefix = "e";
                    } else if (m == 4) {
                        prefix = "f";
                    } else if (m == 5) {
                        prefix = "g";
                    } else if (m == 6) {
                        prefix = "h";
                    } else if (m == 7) {
                        prefix = "i";
                    } else if (m == 8) {
                        prefix = "j";
                    } else if (m == 9) {
                        prefix = "k";
                    } else if (m == 10) {
                        prefix = "l";
                    } else if (m == 11) {
                        prefix = "m";
                    } else if (m == 12) {
                        prefix = "n";
                    } else if (m == 13) {
                        prefix = "o";
                    } else if (m == 14) {
                        prefix = "p";
                    } else if (m == 15) {
                        prefix = "q";
                    } else if (m == 16) {
                        prefix = "r";
                    } else if (m == 17) {
                        prefix = "s";
                    } else if (m == 18) {
                        prefix = "t";
                    } else if (m == 19) {
                        prefix = "u";
                    } else if (m == 20) {
                        prefix = "v";
                    } else if (m == 21) {
                        prefix = "w";
                    } else if (m == 22) {
                        prefix = "x";
                    } else if (m == 23) {
                        prefix = "y";
                    } else if (m == 24) {
                        prefix = "z";
                    }

                    tot = 0;
                    for (int i = 0; i < rowCount; i++) {
                        key = targetdisplayRows[i] + ":" + displayColumns[m];
                        if (targetOriMap.containsKey(key)) {
                            value = String.valueOf(targetOriMap.get(key));
                        }

                        if (value.equalsIgnoreCase("0")) {
                            value = "";
                        }
                        if (!value.equalsIgnoreCase("")) {
                            tot = (long) (tot + Double.parseDouble(value));
                        }
                        int v = 1 + rowStartTarget + i + 1;
                        CFormula = CFormula + "+" + prefix + v;
                    }
                    CFormula = CFormula.substring(1);

                    Formula f = new Formula(m + 1, rowCount + rowStartTarget + 2, CFormula);
                    writableSheet.addCell(f);
                    //label = new Label(m+1, rowCount + rowStartTarget + 2,CFormula);
                    // label.setCellFormat(writableCellFormat);
                    // writableSheet.addCell(label);
                }

                label = new Label(0, rowCount + rowStartTarget + 3, "Difference");
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);

                // for difference
                String diffPrefix = "";
                String difFormula = "";
                for (int i = 0; i < columnCount; i++) {
                    diffPrefix = "";
                    difFormula = "";
                    if (i == 0) {
                        diffPrefix = "b";
                    } else if (i == 1) {
                        diffPrefix = "c";
                    } else if (i == 2) {
                        diffPrefix = "d";
                    } else if (i == 3) {
                        diffPrefix = "e";
                    } else if (i == 4) {
                        diffPrefix = "f";
                    } else if (i == 5) {
                        diffPrefix = "g";
                    } else if (i == 6) {
                        diffPrefix = "h";
                    } else if (i == 7) {
                        diffPrefix = "i";
                    } else if (i == 8) {
                        diffPrefix = "j";
                    } else if (i == 9) {
                        diffPrefix = "k";
                    } else if (i == 10) {
                        diffPrefix = "l";
                    } else if (i == 11) {
                        diffPrefix = "m";
                    } else if (i == 12) {
                        diffPrefix = "n";
                    } else if (i == 13) {
                        diffPrefix = "o";
                    } else if (i == 14) {
                        diffPrefix = "p";
                    } else if (i == 15) {
                        diffPrefix = "q";
                    } else if (i == 16) {
                        diffPrefix = "r";
                    } else if (i == 17) {
                        diffPrefix = "s";
                    } else if (i == 18) {
                        diffPrefix = "t";
                    } else if (i == 19) {
                        diffPrefix = "u";
                    } else if (i == 20) {
                        diffPrefix = "v";
                    } else if (i == 21) {
                        diffPrefix = "w";
                    } else if (i == 22) {
                        diffPrefix = "x";
                    } else if (i == 23) {
                        diffPrefix = "y";
                    } else if (i == 24) {
                        diffPrefix = "z";
                    }

                    int bb = rowStartTarget + rowCount + 2;
                    int bb2 = rowStartTarget + rowCount + 3;
                    difFormula = diffPrefix + bb + "-" + diffPrefix + bb2;

                    // label = new Label(i + 1, 3 + rowCount + rowStartTarget,difFormula);
                    // label.setCellFormat(writableCellFormat);
                    // WritableCellFormat c = new WritableCellFormat();
                    // writableSheet.addCell(label);
                    Formula f = new Formula(i + 1, 3 + rowCount + rowStartTarget, difFormula);
                    writableSheet.addCell(f);

                }
            } else if (targetBasis.equalsIgnoreCase("Percent")) {
                label = new Label(0, rowCount + rowStartTarget + 2, "Current Total");
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
                long tot = 0;

                for (int m = 0; m < columnCount; m++) {
                    String CFormula = "";
                    String prefix = "";
                    if (m == 0) {
                        prefix = "b";
                    } else if (m == 1) {
                        prefix = "c";
                    } else if (m == 2) {
                        prefix = "d";
                    } else if (m == 3) {
                        prefix = "e";
                    } else if (m == 4) {
                        prefix = "f";
                    } else if (m == 5) {
                        prefix = "g";
                    } else if (m == 6) {
                        prefix = "h";
                    } else if (m == 7) {
                        prefix = "i";
                    } else if (m == 8) {
                        prefix = "j";
                    } else if (m == 9) {
                        prefix = "k";
                    } else if (m == 10) {
                        prefix = "l";
                    } else if (m == 11) {
                        prefix = "m";
                    } else if (m == 12) {
                        prefix = "n";
                    } else if (m == 13) {
                        prefix = "o";
                    } else if (m == 14) {
                        prefix = "p";
                    } else if (m == 15) {
                        prefix = "q";
                    } else if (m == 16) {
                        prefix = "r";
                    } else if (m == 17) {
                        prefix = "s";
                    } else if (m == 18) {
                        prefix = "t";
                    } else if (m == 19) {
                        prefix = "u";
                    } else if (m == 20) {
                        prefix = "v";
                    } else if (m == 21) {
                        prefix = "w";
                    } else if (m == 22) {
                        prefix = "x";
                    } else if (m == 23) {
                        prefix = "y";
                    } else if (m == 24) {
                        prefix = "z";
                    }

                    tot = 0;
                    for (int i = 0; i < rowCount; i++) {
                        key = targetdisplayRows[i] + ":" + displayColumns[m];
                        if (targetOriMap.containsKey(key)) {
                            value = String.valueOf(targetOriMap.get(key));
                        }

                        if (value.equalsIgnoreCase("0")) {
                            value = "";
                        }
                        if (!value.equalsIgnoreCase("")) {
                            tot = (long) (tot + Double.parseDouble(value));
                        }
                        int v = 1 + rowStartTarget + i + 1;
                        CFormula = CFormula + "+" + prefix + v;
                    }
                    CFormula = CFormula.substring(1);

                    // label = new Label(m+1, rowCount + rowStartTarget + 2,CFormula);
                    // label.setCellFormat(writableCellFormat);
                    //writableSheet.addCell(label);
                    Formula f = new Formula(m + 1, rowCount + rowStartTarget + 2, CFormula);
                    writableSheet.addCell(f);

                }

                label = new Label(0, rowCount + rowStartTarget + 3, "Difference");
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);

                // for difference
                String diffPrefix = "";
                String difFormula = "";
                for (int i = 0; i < columnCount; i++) {
                    diffPrefix = "";
                    difFormula = "";
                    if (i == 0) {
                        diffPrefix = "b";
                    } else if (i == 1) {
                        diffPrefix = "c";
                    } else if (i == 2) {
                        diffPrefix = "d";
                    } else if (i == 3) {
                        diffPrefix = "e";
                    } else if (i == 4) {
                        diffPrefix = "f";
                    } else if (i == 5) {
                        diffPrefix = "g";
                    } else if (i == 6) {
                        diffPrefix = "h";
                    } else if (i == 7) {
                        diffPrefix = "i";
                    } else if (i == 8) {
                        diffPrefix = "j";
                    } else if (i == 9) {
                        diffPrefix = "k";
                    } else if (i == 10) {
                        diffPrefix = "l";
                    } else if (i == 11) {
                        diffPrefix = "m";
                    } else if (i == 12) {
                        diffPrefix = "n";
                    } else if (i == 13) {
                        diffPrefix = "o";
                    } else if (i == 14) {
                        diffPrefix = "p";
                    } else if (i == 15) {
                        diffPrefix = "q";
                    } else if (i == 16) {
                        diffPrefix = "r";
                    } else if (i == 17) {
                        diffPrefix = "s";
                    } else if (i == 18) {
                        diffPrefix = "t";
                    } else if (i == 19) {
                        diffPrefix = "u";
                    } else if (i == 20) {
                        diffPrefix = "v";
                    } else if (i == 21) {
                        diffPrefix = "w";
                    } else if (i == 22) {
                        diffPrefix = "x";
                    } else if (i == 23) {
                        diffPrefix = "y";
                    } else if (i == 24) {
                        diffPrefix = "z";
                    }

                    int bb = rowStartTarget + rowCount + 2;
                    int bb2 = rowStartTarget + rowCount + 3;
                    difFormula = diffPrefix + bb + "-" + diffPrefix + bb2;

                    // label = new Label(i + 1, 3 + rowCount + rowStartTarget,difFormula);
                    // label.setCellFormat(writableCellFormat);
                    // WritableCellFormat c = new WritableCellFormat();
                    // writableSheet.addCell(label);
                    Formula f = new Formula(i + 1, 3 + rowCount + rowStartTarget, difFormula);
                    writableSheet.addCell(f);

                }
            }

        }

        //added on 15-02-2010 for diffenrence and Current Total over
        if (!primaryAnalyze.equalsIgnoreCase("0") && !secAnalyze.equalsIgnoreCase("Time")) {
            long grandTotal = 0;
            String gTotal = "";
            label = new Label(0, rowCount + rowStartTarget + 1, "Restricting Total");
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);
            for (int i = 0; i < columnCount; i++) {
                long val = 0;
                if (RestrictingTotal.containsKey(displayColumns[i])) {
                    val = ((Long) RestrictingTotal.get(displayColumns[i])).longValue();
                }
//                String v = "" + val + "";
                String v = String.valueOf(val );
                grandTotal = grandTotal + val;
                //WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
                // Number n = new Number(2,1,3.1415926535,cf2);

                //label = new Label(i + 1, 1 + rowCount + rowStartTarget,v);
                //label.setCellFormat(writableCellFormat);
                //writableSheet.addCell(label);
                if (!v.equalsIgnoreCase("")) {
                    Number number = new Number(i + 1, 1 + rowCount + rowStartTarget, Long.parseLong(v));
                    writableSheet.addCell(number);
                } else {
                    label = new Label(i + 1, 1 + rowCount + rowStartTarget, v);
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);
                }
            }
            gTotal = "" + grandTotal + "";

            // label = new Label(columnCount + 1, 1 + rowCount + rowStartTarget, gTotal);
            // label.setCellFormat(writableCellFormat);
            // writableSheet.addCell(label);
            if (!gTotal.equalsIgnoreCase("")) {
                Number number = new Number(columnCount + 1, 1 + rowCount + rowStartTarget, Long.parseLong(gTotal));
                writableSheet.addCell(number);
            } else {
                label = new Label(columnCount + 1, 1 + rowCount + rowStartTarget, gTotal);
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
            }
            label = new Label(0, rowCount + rowStartTarget + 2, "Current Total");
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);

            String cuFormula = "";
            String cPrefixS = "";
            int c1 = rowCount + rowStartTarget + 2;

            for (int i = 0; i < columnCount; i++) {
                int s = rowStartTarget + 2;
                int g = rowCount + rowStartTarget + 2;

                cuFormula = "";
                if (i == 0) {
                    cPrefixS = "b";
                }
                if (i == 1) {
                    cPrefixS = "c";
                }
                if (i == 2) {
                    cPrefixS = "d";
                }
                if (i == 3) {
                    cPrefixS = "e";
                }
                if (i == 4) {
                    cPrefixS = "f";
                }
                if (i == 5) {
                    cPrefixS = "g";
                }
                if (i == 6) {
                    cPrefixS = "h";
                }
                if (i == 7) {
                    cPrefixS = "i";
                }
                if (i == 8) {
                    cPrefixS = "j";
                }
                if (i == 9) {
                    cPrefixS = "k";
                }
                if (i == 10) {
                    cPrefixS = "l";
                }
                if (i == 11) {
                    cPrefixS = "m";
                }
                if (i == 12) {
                    cPrefixS = "n";
                }
                if (i == 13) {
                    cPrefixS = "o";
                }
                if (i == 14) {
                    cPrefixS = "p";
                }
                if (i == 15) {
                    cPrefixS = "q";
                }
                if (i == 16) {
                    cPrefixS = "r";
                }
                if (i == 17) {
                    cPrefixS = "s";
                }
                if (i == 18) {
                    cPrefixS = "t";
                }
                if (i == 19) {
                    cPrefixS = "u";
                }
                if (i == 20) {
                    cPrefixS = "v";
                }
                if (i == 21) {
                    cPrefixS = "w";
                }

                if (i == 22) {
                    cPrefixS = "x";
                }
                if (i == 23) {
                    cPrefixS = "y";
                }
                if (i == 24) {
                    cPrefixS = "z";
                }
                for (; s < g; s++) {
                    cuFormula = cuFormula + "+" + cPrefixS + s;
                }
                //////////////////////////.println(" cuFormula - "+cuFormula);
                Formula f3 = new Formula(i + 1, 2 + rowCount + rowStartTarget, cuFormula);
                writableSheet.addCell(f3);

            }

            label = new Label(0, rowCount + rowStartTarget + 3, "Difference");
            label.setCellFormat(writableCellFormat);
            writableSheet.addCell(label);
            String dFormula = "";
            String prefix1 = "";
            int v1 = 0;
            int v2 = 0;
            v1 = rowCount + rowStartTarget + 2;
            v2 = rowCount + rowStartTarget + 3;
            for (int i = 0; i < columnCount; i++) {
                dFormula = "";
                if (i == 0) {
                    prefix1 = "b";
                }
                if (i == 1) {
                    prefix1 = "c";
                }
                if (i == 2) {
                    prefix1 = "d";
                }
                if (i == 3) {
                    prefix1 = "e";
                }
                if (i == 4) {
                    prefix1 = "f";
                }
                if (i == 5) {
                    prefix1 = "g";
                }
                if (i == 6) {
                    prefix1 = "h";
                }
                if (i == 7) {
                    prefix1 = "i";
                }
                if (i == 8) {
                    prefix1 = "j";
                }
                if (i == 9) {
                    prefix1 = "k";
                }
                if (i == 10) {
                    prefix1 = "l";
                }
                if (i == 11) {
                    prefix1 = "m";
                }
                if (i == 12) {
                    prefix1 = "n";
                }
                if (i == 13) {
                    prefix1 = "o";
                }
                if (i == 14) {
                    prefix1 = "p";
                }
                if (i == 15) {
                    prefix1 = "q";
                }
                if (i == 16) {
                    prefix1 = "r";
                }
                if (i == 17) {
                    prefix1 = "s";
                }
                if (i == 18) {
                    prefix1 = "t";
                }
                if (i == 19) {
                    prefix1 = "u";
                }
                if (i == 20) {
                    prefix1 = "v";
                }
                if (i == 21) {
                    prefix1 = "w";
                }
                if (i == 22) {
                    prefix1 = "x";
                }
                if (i == 23) {
                    prefix1 = "y";
                }
                if (i == 24) {
                    prefix1 = "z";
                }

                dFormula = dFormula + prefix1 + v1 + "-" + prefix1 + v2;
                //////////////////////////.println(" dFormula - "+dFormula);
                Formula f4 = new Formula(i + 1, 3 + rowCount + rowStartTarget, dFormula);
                writableSheet.addCell(f4);
            }
        }
    }

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

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public HashMap<String, Integer> getColor(String columnName, String endvalu) {
        HashMap<String, Integer> RGBcolorCodes = new HashMap<String, Integer>();
        BigDecimal maxVal = ret.getColumnMaximumValue(columnName);
        BigDecimal minVal = ret.getColumnMinimumValue(columnName);
        BigDecimal diff = maxVal.subtract(minVal);
        int originalMeasIndex;
        String measure = "";

        PbReportViewerBD repBD = new PbReportViewerBD();
        if (container.isReportCrosstab()) {
            originalMeasIndex = repBD.findMeasureIndexInCT(container, columnName);
            originalMeasIndex = originalMeasIndex - (container.getViewByCount());
            ArrayList measureList = container.getTableDisplayMeasures();
            if (originalMeasIndex >= 0) {
                measure = (String) measureList.get(originalMeasIndex);
            }
        }
        if (colorGroup.isColorCodePresent(columnName, repParameter, measure)) {
            String color = colorGroup.getColor(columnName, new Double(endvalu), repParameter, measure, diff);
            if (!color.equalsIgnoreCase("")) {
                int intValue = Integer.parseInt(color.replace("#", ""), 16);
                Color aColor = new Color(intValue);
                Color colr = new Color(aColor.getRed(), aColor.getGreen(), aColor.getBlue());
                RGBcolorCodes.put("RED", aColor.getRed());
                RGBcolorCodes.put("GREEN", aColor.getGreen());
                RGBcolorCodes.put("BLUE", aColor.getBlue());
                String hexStr = Integer.toHexString(aColor.getRGB());
//        System.out.println("color\t"+colr.toString());
//          setColourRGB(Colour c,aColor.getRed(),aColor.getGreen(),aColor.getBlue());

//        System.out.println("sddd\t"+Colour.getInternalColour(intValue).getDescription());
//    return new Colour(intValue,color.replace("#", ""),aColor.getRed(),aColor.getGreen(),aColor.getBlue());
            }
        }
        return RGBcolorCodes;
    }

//    public Colour getColor(String disColumnName, String currentValue) {
//        String bgColor = "";
//        String[] StrColors = {"Red", "Orange", "Green"};
//        if (getColorCodeMap() != null) {
//            HashMap tempMap = null;
//            tempMap = (HashMap) ColorCodeMap.get(disColumnName);
//            if (tempMap != null) {
//                String[] colorCodes = (String[]) tempMap.get("colorCodes");
//                String[] operators = (String[]) tempMap.get("operators");
//                String[] sValues = (String[]) tempMap.get("sValues");
//                String[] eValues = (String[]) tempMap.get("eValues");
//
//                if (colorCodes != null && sValues != null && sValues != null) {
//                    for (int h = 0; h <
//                            colorCodes.length; h++) {
//                        if (operators[h].equalsIgnoreCase(">")) {
//                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
//                                if (Double.parseDouble(currentValue) > Double.parseDouble(sValues[h])) {
//                                    bgColor = colorCodes[h];
//                                }
//                            }
//                        } else if (operators[h].equalsIgnoreCase("<")) {
//                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
//                                if (Double.parseDouble(currentValue) < Double.parseDouble(sValues[h])) {
//                                    bgColor = colorCodes[h];
//                                }
//                            }
//                        } else if (operators[h].equalsIgnoreCase(">=")) {
//                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
//                                if (Double.parseDouble(currentValue) >= Double.parseDouble(sValues[h])) {
//                                    bgColor = colorCodes[h];
//                                }
//                            }
//                        } else if (operators[h].equalsIgnoreCase("<=")) {
//                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
//                                if (Double.parseDouble(currentValue) <= Double.parseDouble(sValues[h])) {
//                                    bgColor = colorCodes[h];
//                                }
//                            }
//                        } else if (operators[h].equalsIgnoreCase("=")) {
//                            if (sValues[h] != null && !"".equalsIgnoreCase(sValues[h])) {
//                                if (Double.parseDouble(currentValue) == Double.parseDouble(sValues[h])) {
//                                    bgColor = colorCodes[h];
//                                }
//                            } else if (operators[h].equalsIgnoreCase("!=")) {
//                                if (Double.parseDouble(currentValue) != Double.parseDouble(sValues[h])) {
//                                    bgColor = colorCodes[h];
//                                }
//                            }
//                        } else if (operators[h].equalsIgnoreCase("<>")) {
//                            if (sValues[h] != null && eValues[h] != null && !"".equalsIgnoreCase(eValues[h]) && !"".equalsIgnoreCase(sValues[h])) {
//                                if ((Double.parseDouble(sValues[h]) < Double.parseDouble(currentValue)) && (Double.parseDouble(currentValue) < Double.parseDouble(eValues[h]))) {
//                                    bgColor = colorCodes[h];
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (bgColor.equalsIgnoreCase("Red")) {
//            return Colour.RED;
//        } else if (bgColor.equalsIgnoreCase("Orange")) {
//            return Colour.ORANGE;
//        } else if (bgColor.equalsIgnoreCase("Green")) {
//            return Colour.GREEN;
//        } else {
//            return null;
//        }
//    }
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
     * } catch (Exception exp) { logger.error("Exception: ",exp); } return bos;
     *
     * }
     */
    public ColorGroup getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(ColorGroup colorGroup) {
        this.colorGroup = colorGroup;
    }

    public ReportParameter getRepParameter() {
        return repParameter;
    }

    public void setRepParameter(ReportParameter repParameter) {
        this.repParameter = repParameter;
    }

    //added for sequence in excel
    public ArrayList<String> getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(ArrayList<String> sortColumns) {
        this.sortColumns = sortColumns;
    }

    public char[] getSortTypes() {
        return sortTypes;
    }

    public void setSortTypes(char[] sortTypes) {
        this.sortTypes = sortTypes;
    }

    public char[] getSortDataTypes() {
        return sortDataTypes;
    }

    public void setSortDataTypes(char[] sortDataTypes) {
        this.sortDataTypes = sortDataTypes;
    }

    public void generateSubTotalHtml(int k, WritableCellFormat cf2) {

        ArrayList displayValues = new ArrayList();
        ArrayList viewBys = new ArrayList();
        displayValues = (ArrayList) container.getDisplayColumns().clone();
        viewBys = (ArrayList) container.getDisplayTypes().clone();

        //  Start of code by Nazneen for hidding measures during Excel download report
        //ArrayList disCols = new ArrayList();
        ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
        String eleIdss = "";
        if (container.isReportCrosstab()) {
            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;

            for (Object hiddenCol : hiddenCols) {
                for (int i = 0; i < displayValues.size(); i++) {
                    eleIdss = crosstabMeasureId.get(displayValues.get(i));
                    if (eleIdss != null && !eleIdss.equalsIgnoreCase("null") && !eleIdss.equalsIgnoreCase("")) {
                        if (hiddenCols.contains(eleIdss.replace("A_", ""))) {
                            displayValues.remove(i);
                            viewBys.remove(i);
                        }
                    }
                }
            }
        } else {
            for (Object hiddenCol : hiddenCols) {
                int index = displayValues.indexOf("A_" + hiddenCol.toString());
                if (index != -1) {
                    displayValues.remove(index);
                    viewBys.remove(index);
                }
            }
        }
        // End of code by Nazneen for hidding measures during Excel download report

        //started by Amar
        String aggType = "";
        String refferedElements = "";
        String userColType = "";
        String refElementType = "";
        String tempFormula = "";
        boolean isRunTime = false;
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = null;
        DataFacade facade = new DataFacade(container);
        ArrayList<String> hidemeasurecount = facade.container.getReportCollect().getHideMeasures();
        ArrayList<Integer> index = new ArrayList<Integer>();
        // end by Amar
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }
        if (hidemeasurecount.size() > 0) {
            for (int i = 0; i < hidemeasurecount.size(); i++) {
                index.add(facade.container.getDisplayColumns().indexOf("A_" + hidemeasurecount.get(i)));
            }
        }

        for (int i = 0; i < 2; i++) {
            int actualcol = 0;
            for (int col = 0; col < displayValues.size(); col++) {
                if (!index.contains(col)) {
                    if (viewBys.get(col).toString().equalsIgnoreCase("T")) {
//                 BigDecimal grandToatal=(BigDecimal)ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                        String formattedData = "";
                        String element = container.getDisplayColumns().get(col);
                        String nbrSymbol = "";
                        HashMap NFMap = new HashMap();
                        NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
                        if (NFMap != null) {
                            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                if (NFMap.get(crosstabMeasureId.get(element)) != null) {
                                    nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                }
                            } else if (NFMap.get(element) != null) {
                                nbrSymbol = String.valueOf(NFMap.get(element));
                            }
                        }
                        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
                        //Added by Amar
                        if (RTMeasureElement.isRunTimeMeasure(element)) {
                            nbrSymbol = RTMeasureElement.getMeasureType(element).getNumberSymbol();
                        }
                        //end of code
                        int precision = container.getRoundPrecisionForMeasure(element);
                        if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.get(element) != null) {
                            precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                            element = element.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
                            element = crosstabMeasureId.get(element);
                        }

                        BigDecimal grandToatal;
                        // Modified by Amar
                        if (element != null && !element.equalsIgnoreCase("null") && !element.equalsIgnoreCase("")) {
                            String eleId = element.replace("A_", "");

                            if (eleId.contains("_percentwise") || eleId.contains("_rank") || eleId.contains("_wf") || eleId.contains("_wtrg") || eleId.contains("_rt") || eleId.contains("_pwst") || eleId.contains("_excel") || eleId.contains("_excel_target") || eleId.contains("_deviation_mean") || eleId.contains("_gl") || eleId.contains("_userGl") || eleId.contains("_timeBased") || eleId.contains("_changedPer") || eleId.contains("_glPer")) {
                                eleId = eleId.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
                                isRunTime = true;
                            }

                            String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
                            try {
                                retobj = pbdb.execSelectSQL(qry);
                            } catch (SQLException ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retobj != null && retobj.getRowCount() > 0) {
                                tempFormula = retobj.getFieldValueString(0, 0);
                                aggType = retobj.getFieldValueString(0, 4);
                                refferedElements = retobj.getFieldValueString(0, 1);
                                userColType = retobj.getFieldValueString(0, 2);
                                refElementType = retobj.getFieldValueString(0, 3);
                                tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");
                            } else {
                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                            }
                            if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                            }
                            if (!container.isReportCrosstab()) {
                                if (isRunTime) {
                                    if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    }
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    //modified by anitha
                                } else if (userColType.equalsIgnoreCase("TIMECALUCULATED") && aggType.toUpperCase().contains("avg")) {
                                    facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));

                                } else if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
                                    if (!facade.container.getKpiQrycls().contains(facade.getColumnId(col).replace("A_", "").trim())) {

                                        grandToatal = (BigDecimal) getGTForChangePerOfSummTabMeasures(eleId, col);

                                    } else if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
                                        //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                        if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
                                            grandToatal = BigDecimal.ZERO;
                                        } else if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
                                            grandToatal = BigDecimal.ZERO;
                                        } else {
                                            grandToatal = new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
                                        }

                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(displayValues.get(col).toString());
                                    }
                                } else if (userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("AVG")) {
                                    String refEleArray[] = refferedElements.split(",");
                                    int len = refEleArray.length;
                                    int flag = 1;
                                    String mysqlString = "";
                                    for (int j = 0; j < len; j++) {
                                        String elementId = refEleArray[j];
                                        String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                        PbReturnObject retobj1 = null;
                                        try {
                                            retobj1 = pbdb.execSelectSQL(getBussColName);
                                        } catch (SQLException ex) {
                                            logger.error("Exception: ", ex);
                                        }
                                        if (retobj1 != null && retobj1.getRowCount() > 0) {
                                            String bussColName = retobj1.getFieldValueString(0, 0);
                                            String aggrType = retobj1.getFieldValueString(0, 1);
                                            if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                String newEleID = "A_" + elementId;
                                                tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                BigDecimal grandTotalValueForEle = null;
                                                grandTotalValueForEle = facade.getColumnGrandTotalValue(newEleID);
                                                if (grandTotalValueForEle == null) {
                                                    flag = 0;
                                                }
                                                if (flag == 1) {
                                                    grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                                    if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                        grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                    }
                                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                        mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                                    } else {
                                                        tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                    }
                                                } else {
                                                    grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    }
                                    //Calculate formula
                                    if (!tempFormula.equalsIgnoreCase("")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "OTHERS");
                                        grandToatal = (BigDecimal) getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                    } else {
                                        grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                    }
                                } else if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
                                    //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                    if (aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")) {
                                        //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");

                                        grandToatal = ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                        int rowCnt = 0;
                                        if (facade.container.getReportCollect().crosscolmap1.get(facade.getColumnId(col)).toString().equalsIgnoreCase("Exclude 0")) {
                                            rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), col, facade.getColumnId(col));
                                        } else {
                                            rowCnt = facade.getRowCount();
                                        }
                                        grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                    } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
                                        grandToatal = BigDecimal.ZERO;
                                    } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
                                        grandToatal = BigDecimal.ZERO;
                                    } else {
                                        grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
                                    }
                                } else {
                                    grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                                }
                            } else if (container.isReportCrosstab()) {

                                HashMap<String, ArrayList> nonViewByMapNew = null;
                                nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
                                try {
                                    if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    }
//                        if (facade.container.getKpiQrycls().contains(eleId.trim())) {
                                    if (eleId != null && eleId != "null" && eleId != "" && aggType.equalsIgnoreCase("avg")) {
                                        //for calculating gt for avg cols like A_10,A_11 which are of gt type and is summarized
                                        if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                                            String refEleArray[] = refferedElements.split(",");
                                            int len = refEleArray.length;
                                            int flag = 1;
                                            String mysqlString = "";
                                            if (facade.getColumnId(col).contains("A_")) {
                                                for (int j = 0; j < len; j++) {
                                                    String elementId = refEleArray[j];
                                                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                                    PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                        BigDecimal grandTotalValueForEle = null;
                                                        String bussColName = retobj1.getFieldValueString(0, 0);
                                                        String aggrType = retobj1.getFieldValueString(0, 1);
                                                        if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                            String newEleID = "A_" + elementId;
                                                            tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                            for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                                                String key = e.getKey();
                                                                String value = e.getValue();
                                                                if (value.equalsIgnoreCase(newEleID) && key.contains("A_")) {
                                                                    grandTotalValueForEle = facade.getColumnGrandTotalValue(key);
                                                                    break;
                                                                }
                                                            }
                                                            if (grandTotalValueForEle == null) {
                                                                flag = 0;
                                                            }
                                                            if (flag == 1) {
                                                                grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                                                if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                                    grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                                }
                                                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                                    mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                                                } else {
                                                                    tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                                }
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                            }
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                    }
                                                }
                                                //Calculate formula value
                                                if (!tempFormula.equalsIgnoreCase("")) {
                                                    grandToatal = getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            } else //for calculating gt for avg cols like A1,A2,A3 which are not of gt type
                                            if (nonViewByMapNew.get(facade.getColumnId(col)) != null) {
                                                String keyset = nonViewByMapNew.get(facade.getColumnId(col)).toString().replace("[", "").replace("]", "").trim();
                                                String mainKeySetArray1[] = keyset.split(",");
                                                String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
                                                for (int m = 0; m < mainKeySetArray1.length - 1; m++) {
                                                    String val1 = mainKeySetArray1[m];
                                                    mainKeySetArray[m] = mainKeySetArray1[m];
                                                }

                                                for (int j = 0; j < len; j++) {
                                                    int flag1 = 0;
                                                    flag = 0;
                                                    BigDecimal grangTotalValueForEle = null;
                                                    String elementId = refEleArray[j];
                                                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                                    PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                        String bussColName = retobj1.getFieldValueString(0, 0);
                                                        String aggrType = retobj1.getFieldValueString(0, 1);
                                                        if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                            String newEleID = "A_" + elementId;
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
                                                                            for (int m = 0; m < tempKeysetArray1.length - 1; m++) {
                                                                                tempKeysetArray[m] = tempKeysetArray1[m];
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
                                                                                        grangTotalValueForEle = facade.getColumnGrandTotalValue(key);
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
                                                                if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                                    grangTotalValueForEle = grangTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                                }
                                                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                                    mysqlString = mysqlString + "," + grangTotalValueForEle + " AS " + newEleID;
                                                                } else {
                                                                    tempFormula = tempFormula.replace(newEleID, grangTotalValueForEle.toString());
                                                                }
                                                                flag1 = 1;
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                            }
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                    }
                                                }
                                                //Calculate formula value
                                                if (!tempFormula.equalsIgnoreCase("")) {
                                                    grandToatal = getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            int rowCnt = 0;
                                            if (facade.container.getReportCollect().crosscolmap1.get(element).toString().equalsIgnoreCase("Exclude 0")) {
                                                rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), col, facade.getColumnId(col));
                                            } else {
                                                rowCnt = facade.getRowCount();
                                            }
                                            grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                        }
                                    } else if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
                                        if (nonViewByMapNew.get(facade.getColumnId(col)) != null) {
                                            String keyset = nonViewByMapNew.get(facade.getColumnId(col)).toString().replace("[", "").replace("]", "").trim();
                                            if (facade.container.retObjHashmap.get(keyset) != null) {
                                                grandToatal = new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    }
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                }

                            } else {
                                grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                            }
                            //end of code by bhargavi
                            // end of code bu Amar
                            if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                formattedData = grandToatal.toString();
                            } else if (container.gettimeConversion(displayValues.get(col).toString()) != null && container.gettimeConversion(displayValues.get(col).toString()).equalsIgnoreCase("Y")) {
                                formattedData = this.convertDataToTime(grandToatal);
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                // formattedData = snbrSymbol+""+formattedData;
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                if (snbrSymbol.equalsIgnoreCase("%")) {
                                    formattedData = formattedData + "" + snbrSymbol;
                                } else {
                                    formattedData = snbrSymbol + "" + formattedData;
                                }
                            } else {
                                formattedData = NumberFormatter.getModifiedNumber(grandToatal, nbrSymbol, precision);
//                          HashMap colProps = container.getColumnProperties();
//                          if (colProps != null && colProps.containsKey(element)) {
//                          ArrayList propList = (ArrayList) colProps.get(element);
//                           String colSymbol = (String) propList.get(7);
//                           if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) {
//                            if (colSymbol.equalsIgnoreCase("%")) {
//                              formattedData = formattedData + colSymbol;
//                           } else {
//                           formattedData = colSymbol + formattedData;
//                          }
//                          }
//                        }
                            }
                            //cell = new Number(col, i+k+rowStart+1, Double.parseDouble(formattedData),cf2);

                            if (container.gettimeConversion(displayValues.get(col).toString()) != null && container.gettimeConversion(displayValues.get(col).toString()).equalsIgnoreCase("Y")) {
                                cell = new Label(actualcol, i + k + rowStart + 1, formattedData, cf2);
                            } else if (formattedData.contains("%")) {
                                cell = new Label(actualcol, i + k + rowStart + 1, formattedData, cf2);
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    cell = new Label(actualcol, i + k + rowStart + 1, formattedData, cf2);
                                } else {
                                    cell = new Number(actualcol, i + k + rowStart + 1, Double.parseDouble((formattedData).replace(",", "")), cf2);
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                //added by Dinanath for color group
                                try {
                                    WritableCellFormat cf3 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
                                    writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                                    cf3 = new WritableCellFormat(writableFont, NumberFormats.THOUSANDS_FLOAT);
                                    cf3.setBackground(Colour.WHITE);
                                    cf3.setWrap(true);
                                    cf3.setAlignment(Alignment.GENERAL);
                                    cell = new Label(actualcol, i + k + rowStart + 1, formattedData, cf3);
                                } catch (Exception e) {
                                    logger.error("Exception: ", e);
                                }
                                //endded by Dinanath
                                //cell = new Label(actualcol, i + k + rowStart + 1, formattedData,cf2);
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                //added by Dinanath for color group
                                try {
                                    WritableCellFormat cf3 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
                                    writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                                    cf3 = new WritableCellFormat(writableFont, NumberFormats.THOUSANDS_FLOAT);
                                    cf3.setBackground(Colour.WHITE);
                                    cf3.setWrap(true);
                                    cf3.setAlignment(Alignment.GENERAL);
                                    cell = new Label(actualcol, i + k + rowStart + 1, formattedData, cf3);
                                } catch (Exception e) {
                                    logger.error("Exception: ", e);
                                }
                                //endded by Dinanath
                                //cell = new Label(actualcol, i + k + rowStart + 1, formattedData,cf2);
                            } else {
                                //added by Dinanath for color group
                                try {
                                    WritableCellFormat cf3 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
                                    writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                                    cf3 = new WritableCellFormat(writableFont, NumberFormats.THOUSANDS_FLOAT);
                                    cf3.setBackground(Colour.WHITE);
                                    cf3.setWrap(true);
                                    cf3.setAlignment(Alignment.GENERAL);
                                    cell = new Number(actualcol, i + k + rowStart + 1, Double.parseDouble(formattedData.replace(",", "")), cf3);
                                } catch (Exception e) {
                                    logger.error("Exception: ", e);
                                }
                                //endded by Dinanath
//                            cell = new Number(actualcol, i + k + rowStart + 1, Double.parseDouble(formattedData.replace(",", "")), cf2);//commented by Dinanath
                            }
                        }
                    } else if (i == 0) {
                        cell = new Label(actualcol, i + k + rowStart + 1, "Grand Total");
                    } else {
//                     cell = new Label(col, i+k+rowStart+1, "Category Sub Total");
                        cell = new Label(actualcol, i + k + rowStart + 1, "Sub Total");         // Modified from Category Sub Total to Sub Total by Amar
                    }
//                cell.setCellFormat(writableCellFormat);

                    try {
                        writableSheet.addCell(cell);
                    } catch (WriteException ex) {
                        logger.error("Exception: ", ex);
                    }
                    actualcol++;
                }
            }
        }
        //added by sruthi for overallavg,overallmax,overallmin
        k = k + 2;
        int j = 0;
        BigDecimal othersValue = null;
        if (facade.isAverageRequired()) {
            BigDecimal overallavg;
            for (int i = 0; i < 1; i++) {

                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (viewBys.get(col).toString().equalsIgnoreCase("T")) {
                        othersValue = (BigDecimal) facade.getColumnAverageValue(displayValues.get(col).toString());
                        this.getOthersData(othersValue, col, cf2, startcol, i, k, j);
                    } else {
                        if (i == 0) {
                            cell = new Label(startcol, j + i + k + rowStart + 1, "OverallAvg");
                            try {
                                writableSheet.addCell(cell);
                            } catch (WriteException ex) {
                                logger.error("Exception: ", ex);
                            }
                        }
                    }
                    startcol++;
                }
            }
            j++;
        }
        if (facade.isOverAllMaxRequired()) {
            BigDecimal overAllMax;
            for (int i = 0; i < 1; i++) {
                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (viewBys.get(col).toString().equalsIgnoreCase("T")) {
                        othersValue = (BigDecimal) facade.getColumnMaximumValue(displayValues.get(col).toString());
                        this.getOthersData(othersValue, col, cf2, startcol, i, k, j);
                    } else {
                        if (i == 0) {
                            cell = new Label(startcol, j + i + k + rowStart + 1, "OverAllMax");
                            try {
                                writableSheet.addCell(cell);
                            } catch (WriteException ex) {
                                logger.error("Exception: ", ex);
                            }
                        }
                    }
                    startcol++;
                }
            }
            j++;
        }
        if (facade.isOverAllMinRequired()) {
            for (int i = 0; i < 1; i++) {
                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (viewBys.get(col).toString().equalsIgnoreCase("T")) {
                        othersValue = (BigDecimal) facade.getColumnMinimumValue(displayValues.get(col).toString());
                        this.getOthersData(othersValue, col, cf2, startcol, i, k, j);
                    } else {
                        if (i == 0) {
                            cell = new Label(startcol, j + i + k + rowStart + 1, "OverAllMin");
                            try {
                                writableSheet.addCell(cell);
                            } catch (WriteException ex) {
                                logger.error("Exception: ", ex);
                            }
                        }
                    }
                    startcol++;
                }
            }
            j++;
        }
    }

    public BigDecimal getOthersData(BigDecimal othersValue, int col, WritableCellFormat cf2, int startcol, int i, int k, int j) {
        String nbrSymbol = "";
        String formattedData = "";
        String element = container.getDisplayColumns().get(col);
        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
        int precision = container.getRoundPrecisionForMeasure(element);
        formattedData = NumberFormatter.getModifiedNumber(othersValue, nbrSymbol, precision);
        cell = new Number(startcol, i + j + k + rowStart + 1, Double.parseDouble(formattedData.replace(",", "")), cf2);
        try {
            writableSheet.addCell(cell);
        } catch (WriteException ex) {
            logger.error("Exception: ", ex);
        }

        return null;
    }
//ended by sruthi

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String convertDataToTime(BigDecimal measData) {

        String time = "";
        BigDecimal hours = new BigDecimal("0");
        BigDecimal myremainder = new BigDecimal("0");
        BigDecimal minutes = new BigDecimal("0");
        BigDecimal seconds = new BigDecimal("0");
        BigDecimal var3600 = new BigDecimal("3600");
        BigDecimal var60 = new BigDecimal("60");

        hours = measData.divide(var3600, BigDecimal.ROUND_FLOOR);
        myremainder = measData.remainder(var3600);
        minutes = myremainder.divide(var60, BigDecimal.ROUND_FLOOR);
        seconds = myremainder.remainder(var60);
        DecimalFormat formatter = new DecimalFormat("##");
        time = formatter.format(hours) + ":" + formatter.format(minutes) + ":" + formatter.format(seconds);
        return time;
    }

    /**
     * @return the filterValues
     */
    public ArrayList getFilterValues() {
        return filterValues;
    }

    /**
     * @param filterValues the filterValues to set
     */
    public void setFilterValues(ArrayList filterValues) {
        this.filterValues = filterValues;
    }

    public String queryExcelDownload(PbReturnObject retObj, String connId) {
        ServletOutputStream outputstream = null;
        Label[] labels = null;
        //common code for all
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 3;
        String[] columnNames;
        ServletWriterTransferObject swt = null;
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        try {
            swt = ServletUtilities.createBufferedWriter("test", "xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(false);
            Connection con = ProgenConnection.getInstance().getConnectionByConId(connId);
            columnNames = retObj.getColumnNames();

            int colCount = 0;
            int sheet = 0;
            int fromRow = 0;
            if (fromRow == 0 && colCount == 0) {
                colCount = retObj.getRowCount();

            }

            if (colCount > 65000) {
                sheet = colCount / 65000;
                colCount = 65000;
            }
            for (int k = 0; k <= sheet; k++) {
                writableSheet = writableWorkbook.createSheet("test" + (k + 1), k);
                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setBackground(Colour.GRAY_25);
                writableCellFormat.setWrap(true);

                WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

                label = new Label(0, 0, "Progen Business solution");
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
                for (int i = 0; i < columnNames.length; i++) {
                    label = new Label(i, rowStart, columnNames[i]);
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);

                    HeadingCellView = new CellView();
                    HeadingCellView.setSize(256 * 20);
                    writableSheet.setColumnView(i, HeadingCellView);
                }

                writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setWrap(true);

                if (retObj != null) {
                    int row = 0;
                    for (int i = fromRow; i < colCount && i < retObj.getRowCount(); i++) {
                        for (int j = 0; j < columnNames.length; j++) {
                            cell = new Label(j, row + rowStart + 1, retObj.getFieldValueString(i, j), cf2);
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                        row++;
                    }
                }
                colCount = colCount + 65000;
                fromRow = fromRow + 65000;
            }

            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        return swt.fileName;
    }

    public String getAggregationForSubtotal(String measId) {

        if (container.getReportCollect().reportQryElementIds.indexOf(measId.replace("A_", "")) != -1) {
            return container.getReportCollect().reportQryAggregations.get(container.getReportCollect().reportQryElementIds.indexOf(measId.replace("A_", "")));
        } else {
            return "";
        }
    }
//added By Dinanath for XLSM

    public void createExcelForReportsXLSM(int count, String file, String excelFileName) throws FileNotFoundException {
        bos = new ByteArrayOutputStream();
        File tempFileIn = null;
        String[] filepathIn = null;
        String testingFile = "srs";
        if (count == 1) {
            File myFile = new File(file);
            inStream = new FileInputStream(file);

        } else {
            filepathIn = file.replaceAll("[()]", "").split(excelFileName.replace("[()]", ""));
            tempFileIn = new File(filepathIn[0] + "Temp" + excelFileName);
            inStream = new FileInputStream(tempFileIn);
        }
        String[] filepath = file.replaceAll("[()]", "").split(excelFileName.replace("[()]", ""));
        columnCount = displayColumns.length;
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);
        try {
            actualtoRow = toRow;
            if (fromRow == 0 && toRow == 0) {
                toRow = ret.getViewSequence().size();
            }
            //checking whether records are more than 65000 so that to create required  number of sheets
            if (toRow > 65000) {
                sheet = toRow / 65000;
                toRow = 65000;
            }
            try {
//                 File file = new File("E:/temp/sxssf1.xlsx");
//            OPCPackage pkg = OPCPackage.open(new FileInputStream(file.getAbsolutePath()));
                OPCPackage pkg = OPCPackage.open(inStream);
                XSSFWorkbook xssfwb = new XSSFWorkbook(pkg);
                hhhhWorkbook = new SXSSFWorkbook(xssfwb, 100); // keep 100 rows in memory, exceeding rows will be flushed to disk
                hhhhSheet = (SXSSFSheet) hhhhWorkbook.getSheetAt(sheetNumber);
//                hhhhWorkbook = new XSSFWorkbook(inStream);
//                hhhhSheet = hhhhWorkbook.getSheetAt(sheetNumber);
            } catch (IOException e) {
                logger.error("Exception: ", e);
            }
            HeaderFooter header = new HeaderFooter(getHeaderTitle());
            HeaderFooter footer = new HeaderFooter(getHeaderTitle());
            PbReportCollection collect = container.getReportCollect();
            ArrayList<String> sortCols = null;
            char[] sortTypes = null;//ArrayList sortTypes = null;
            char[] sortDataTypes = null;

            if (collect.reportRowViewbyValues.get(0) != null && collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME")) {
            } else {
                sortCols = container.getSortColumns();
                String sort = "";
                if (sortCols != null && !sortCols.isEmpty()) {
                    sortCols = container.getSortColumns();
                    if (!sortCols.isEmpty()) {
                        sortTypes = container.getSortTypes();
                        sortDataTypes = container.getSortDataTypes();
                        ProgenDataSet retObj = container.getRetObj();
                        ArrayList rowSequence = new ArrayList();
                        if (container.isTopBottomSet()) {
                            int topbottomCount = container.getTopBottomCount();
                            if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                                sort = "1";
//                         container.setSortColumn(sortCols, sort);
                                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                } else {
                                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                }
//                              retObj.setViewSequence(rowSequence);
                            } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                                sort = "0";
//                            container.setSortColumn(sortColumn, sort);
                                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                } else {
                                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                }

                            }
                        } else {
                            rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                        }
                        retObj.setViewSequence(rowSequence);
//                container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes));
//                rowCount = rowSequence.size();
                    } else {
                        ArrayList tableMeasure = container.getTableMeasure();
                        if (tableMeasure != null) {
                            ArrayList sortColumn = new ArrayList();
                            sortColumn.add("A_" + tableMeasure.get(0).toString());
                            char[] sortType = new char[1];//new String[1];
                            sortType[0] = ' ';
                            char[] sortdataType = new char[1];
                            sortdataType[0] = 'N';
                            container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortColumn, sortType, sortdataType));
                        }
                    }
                }
            }
            ArrayList<String> values = new ArrayList<String>();
            values.add(getReportName().replace("_", " "));
            values.add(sdf.format(date));
//            for (int i = 0; i < rowStart - 2; i++) {
//                writableSheet.setRowView(i, (20 * 20), false);
//            }
            toRow = container.getRetObj().getViewSequence().size();
            if (isHeader.equalsIgnoreCase("on")) {
                buildReportTableXLSM();
            } else {
                buildExportReportTableXLSM();
            }
//            writableWorkbook.write();
//            writableWorkbook.close();
//            Workbook1.close();
            inStream.close();

            if (count < totalRepCount) {
//                OutputStream out=new FileOutputStream(file);
//                hWorkbook.write(out);
//                out.flush();
//                out.close();
                File tempFile = new File(filepath[0] + "Temp" + excelFileName);
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                }
                OutputStream outTemp = new FileOutputStream(tempFile);
                hhhhWorkbook.write(outTemp);
                outTemp.flush();
                outTemp.close();
            } else if (operType.isEmpty()) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment;filename= \"" + excelFileName + "\"");
                outputstream = response.getOutputStream();
                hhhhWorkbook.write(outputstream);
                outputstream.flush();
                outputstream.close();
                //bos.flush();
                if (count > 1) {
                    if (tempFileIn.delete()) {
                        System.out.println("Temp file: " + tempFileIn.getName() + " is deleted!");
                    } else {
                        System.out.println("Delete operation is failed.");
                    }
                }
            } else {
                File tempFileFinal = new File(filepath[0] + "Temp" + excelFileName);
                if (!tempFileFinal.exists()) {
                    tempFileFinal.createNewFile();
                }
                OutputStream outTemp = new FileOutputStream(tempFileFinal);
                hhhhWorkbook.write(outTemp);
                outTemp.flush();
                outTemp.close();
            }
            System.out.println("XLSM file has downloaded successfully");
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
    }

    //added By Dinanath for Multiple Excel Export
    public void createExcelForReportsXLSX(int count, String file, String excelFileName) throws FileNotFoundException {
        bos = new ByteArrayOutputStream();
        File tempFileIn = null;
        String[] filepathIn = null;
        String testingFile = "srs";
        if (count == 1) {
            File myFile = new File(file);
            inStream = new FileInputStream(file);
        } else {
            filepathIn = file.replaceAll("[()]", "").split(excelFileName.replace("[()]", ""));
            tempFileIn = new File(filepathIn[0] + "Temp" + excelFileName);
            inStream = new FileInputStream(tempFileIn);
        }
//         File myFile=new File(file);
//            inStream = new FileInputStream(file);

        String[] filepath = file.replaceAll("[()]", "").split(excelFileName.replace("[()]", ""));
        columnCount = displayColumns.length;
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        try {
            actualtoRow = toRow;
            if (fromRow == 0 && toRow == 0) {
                toRow = ret.getViewSequence().size();
            }
            //checking whether records are more than 65000 so that to create required  number of sheets
            if (toRow > 65000) {
                sheet = toRow / 65000;
                toRow = 65000;
            }
            hWorkbook = new XSSFWorkbook(inStream);
            hSheet = hWorkbook.getSheetAt(sheetNumber);
            HeaderFooter header = new HeaderFooter(getHeaderTitle());
            HeaderFooter footer = new HeaderFooter(getHeaderTitle());
            PbReportCollection collect = container.getReportCollect();
            ArrayList<String> sortCols = null;
            char[] sortTypes = null;//ArrayList sortTypes = null;
            char[] sortDataTypes = null;

            if (collect.reportRowViewbyValues.get(0) != null && collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME")) {
            } else {
                sortCols = container.getSortColumns();
                String sort = "";
                if (sortCols != null && !sortCols.isEmpty()) {
                    sortCols = container.getSortColumns();
                    if (!sortCols.isEmpty()) {
                        sortTypes = container.getSortTypes();
                        sortDataTypes = container.getSortDataTypes();
                        ProgenDataSet retObj = container.getRetObj();
                        ArrayList rowSequence = new ArrayList();
                        if (container.isTopBottomSet()) {
                            int topbottomCount = container.getTopBottomCount();
                            if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                                sort = "1";
//                         container.setSortColumn(sortCols, sort);
                                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                } else {
                                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                }
//                              retObj.setViewSequence(rowSequence);
                            } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                                sort = "0";
//                            container.setSortColumn(sortColumn, sort);
                                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                } else {
                                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                }

                            }
                        } else {
                            rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                        }
                        retObj.setViewSequence(rowSequence);

                    } else {
                        ArrayList tableMeasure = container.getTableMeasure();
                        if (tableMeasure != null) {
                            ArrayList sortColumn = new ArrayList();
                            sortColumn.add("A_" + tableMeasure.get(0).toString());
                            char[] sortType = new char[1];//new String[1];
                            sortType[0] = ' ';
                            char[] sortdataType = new char[1];
                            sortdataType[0] = 'N';
                            container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortColumn, sortType, sortdataType));
                        }
                    }
                }
            }
            ArrayList<String> values = new ArrayList<String>();
            values.add(getReportName().replace("_", " "));
            values.add(sdf.format(date));
//            for (int i = 0; i < rowStart - 2; i++) {
//                writableSheet.setRowView(i, (20 * 20), false);
//            }
            toRow = container.getRetObj().getViewSequence().size();
            if (isHeader.equalsIgnoreCase("on")) {
                buildReportTableXLSX();
            } else {
                buildExportReportTableXLSX();
            }
//            writableWorkbook.write();
//            writableWorkbook.close();
//            Workbook1.close();
            inStream.close();

            if (count < totalRepCount) {
//                OutputStream out=new FileOutputStream(file);
//                hWorkbook.write(out);
//                out.flush();
//                out.close();
                File tempFile = new File(filepath[0] + "Temp" + excelFileName);
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                }
                OutputStream outTemp = new FileOutputStream(tempFile);
                hWorkbook.write(outTemp);
                outTemp.flush();
                outTemp.close();
            } else if (operType.isEmpty()) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment;filename= \"" + excelFileName + "\"");
                outputstream = response.getOutputStream();
                hWorkbook.write(outputstream);
                outputstream.flush();
                outputstream.close();
                //bos.flush();
                if (count > 1) {
                    if (tempFileIn.delete()) {
                        System.out.println("Temp file: " + tempFileIn.getName() + " is deleted!");
                    } else {
                        System.out.println("Delete operation is failed.");
                    }
                }
            } else {
                File tempFileFinal = new File(filepath[0] + "Temp" + excelFileName);
                if (!tempFileFinal.exists()) {
                    tempFileFinal.createNewFile();
                }
                OutputStream outTemp = new FileOutputStream(tempFileFinal);
                hWorkbook.write(outTemp);
                outTemp.flush();
                outTemp.close();
            }

//System.out.println("XLSX file has downloaded successfully");
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
    }
    //added by Dinanath for xls

    public void createExcelForReportsXLS(int count, String file, String excelFileName) throws FileNotFoundException {
        bos = new ByteArrayOutputStream();
        File tempFileIn = null;
        String[] filepathIn = null;
        String testingFile = "srs";
        if (count == 1) {
            File myFile = new File(file);
            inStream = new FileInputStream(file);
        } else {
            filepathIn = file.replaceAll("[()]", "").split(excelFileName.replace("[()]", ""));
            tempFileIn = new File(filepathIn[0] + "Temp" + excelFileName);
            inStream = new FileInputStream(tempFileIn);
        }

        String[] filepath = file.replaceAll("[()]", "").split(excelFileName.replace("[()]", ""));
        columnCount = displayColumns.length;
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        try {
            actualtoRow = toRow;
            if (fromRow == 0 && toRow == 0) {
                toRow = ret.getViewSequence().size();
            }
            //checking whether records are more than 65000 so that to create required  number of sheets
            if (toRow > 65000) {
                sheet = toRow / 65000;
                toRow = 65000;
            }
            hhWorkbook = new HSSFWorkbook(inStream);
            hhSheet = hhWorkbook.getSheetAt(sheetNumber);
            HeaderFooter header = new HeaderFooter(getHeaderTitle());
            HeaderFooter footer = new HeaderFooter(getHeaderTitle());
            PbReportCollection collect = container.getReportCollect();
            ArrayList<String> sortCols = null;
            char[] sortTypes = null;//ArrayList sortTypes = null;
            char[] sortDataTypes = null;

            if (collect.reportRowViewbyValues.get(0) != null && collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME")) {
            } else {
                sortCols = container.getSortColumns();
                String sort = "";
                if (sortCols != null && !sortCols.isEmpty()) {
                    sortCols = container.getSortColumns();
                    if (!sortCols.isEmpty()) {
                        sortTypes = container.getSortTypes();
                        sortDataTypes = container.getSortDataTypes();
                        ProgenDataSet retObj = container.getRetObj();
                        ArrayList rowSequence = new ArrayList();
                        if (container.isTopBottomSet()) {
                            int topbottomCount = container.getTopBottomCount();
                            if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                                sort = "1";
//                         container.setSortColumn(sortCols, sort);
                                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                } else {
                                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                }
//                              retObj.setViewSequence(rowSequence);
                            } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                                sort = "0";
//                            container.setSortColumn(sortColumn, sort);
                                if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                    rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                                } else {
                                    rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                                }

                            }
                        } else {
                            rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                        }
                        retObj.setViewSequence(rowSequence);

//                container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes));
//                rowCount = rowSequence.size();
                    } else {
                        ArrayList tableMeasure = container.getTableMeasure();
                        if (tableMeasure != null) {
                            ArrayList sortColumn = new ArrayList();
                            sortColumn.add("A_" + tableMeasure.get(0).toString());
                            char[] sortType = new char[1];//new String[1];
                            sortType[0] = ' ';
                            char[] sortdataType = new char[1];
                            sortdataType[0] = 'N';
                            container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortColumn, sortType, sortdataType));
                        }
                    }
                }
            }
            ArrayList<String> values = new ArrayList<String>();
            values.add(getReportName().replace("_", " "));
            values.add(sdf.format(date));
//            for (int i = 0; i < rowStart - 2; i++) {
//                writableSheet.setRowView(i, (20 * 20), false);
//            }
            toRow = container.getRetObj().getViewSequence().size();
            if (isHeader.equalsIgnoreCase("on")) {
                buildReportTableXLS();
            } else {
                buildExportReportTableXLS();
            }
//            writableWorkbook.write();
//            writableWorkbook.close();
//            Workbook1.close();
            inStream.close();

            if (count < totalRepCount) {
//                OutputStream out=new FileOutputStream(file);
//                hWorkbook.write(out);
//                out.flush();
//                out.close();
                File tempFile = new File(filepath[0] + "Temp" + excelFileName);
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                }
                OutputStream outTemp = new FileOutputStream(tempFile);
                hhWorkbook.write(outTemp);
                outTemp.flush();
                outTemp.close();
            } else if (operType.isEmpty()) {
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment;filename= \"" + excelFileName + "\"");
                outputstream = response.getOutputStream();
                hhWorkbook.write(outputstream);
                outputstream.flush();
                outputstream.close();
                //bos.flush();
                if (count > 1) {
                    if (tempFileIn.delete()) {
                        System.out.println("Temp file: " + tempFileIn.getName() + " is deleted!");
                    } else {
                        System.out.println("Delete operation is failed.");
                    }
                }
            } else {
                File tempFileFinal = new File(filepath[0] + "Temp" + excelFileName);
                if (!tempFileFinal.exists()) {
                    tempFileFinal.createNewFile();
                }
                OutputStream outTemp = new FileOutputStream(tempFileFinal);
                hhWorkbook.write(outTemp);
                outTemp.flush();
                outTemp.close();
            }
            System.out.println("XLS file is downloaded successfully");
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
    }
//endded by Dinanath

    public static int[] getRGB(final String rgb) {
        final int[] ret = new int[3];
        for (int i = 0; i < 3; i++) {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }
    //added by Dinanath for xls

    private void buildReportTableXLS() throws Exception {
        boolean isCrossTabReport = false;
        if (container.getReportCollect().reportColViewbyValues != null && container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        hhStyle = hhWorkbook.createCellStyle();
        // get the color which most closely matches the color you want to use
//added by Dinanath for grey color
        HSSFPalette palette1 = hhWorkbook.getCustomPalette();
        HSSFColor myColor1 = palette1.findSimilarColor(211, 211, 211);
        HSSFColor borderColorHeader = palette1.findSimilarColor(224, 224, 224);
// get the palette index of that color
        short palIndex1 = myColor1.getIndex();
        short borderColorHeader1 = borderColorHeader.getIndex();
// code to get the style for the cell goes here
        hhStyle.setFillBackgroundColor(palIndex1);
        hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
        hhStyle.setFillForegroundColor(palIndex1);
        hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        hhStyle.setLeftBorderColor(borderColorHeader1);
        hhStyle.setRightBorderColor(borderColorHeader1);
        hhStyle.setTopBorderColor(borderColorHeader1);
        hhStyle.setBottomBorderColor(borderColorHeader1);
//endded by Dinanath
        //hStyle.setAlignment(Alignment.CENTRE);
        int columnViewByCount = Integer.parseInt(container.getColumnViewByCount());
        hhRow = hhSheet.createRow(lineNumber);
        lineNumber++;
        for (int i = 0; i < columnCount; i++) {
            if (isCrossTabReport) {
                // label = new Label(i, lineNumber, displayLabels[i]);
                // hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
                //Added by Ram for Hide Measure Headings 15Feb2016
                if (container.isHideMsrHeading()) {
                    hhCell = hhRow.createCell(i + colNumber);
                    String str = displayLabels[i];
                    str = str.replace("[", "").replace("]", "");
                    String[] st = str.split(",");
                    hhCell.setCellValue(st[0]);
                } else {
                    hhCell = hhRow.createCell(i + colNumber);
                    hhCell.setCellValue(displayLabels[i]);
                } //End Ram code
            } else if (displayLabels[i].contains("Prev Month") && this.getAggregationForSubtotal(displayColumns[i]).contains("#T")) {
                // label = new Label(i, lineNumber, container.getMonthNameforTrailingFormula(i));
                //   hCell=hRow.createCell(i);
                //  hCell.setCellValue(container.getMonthNameforTrailingFormula(i));
                hhCell = hhRow.createCell(i + colNumber);
                hhCell.setCellValue(displayLabels[i]);
            } else {
                //label = new Label(i, lineNumber, displayLabels[i]);
                //hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
                hhCell = hhRow.createCell(i + colNumber);
                hhCell.setCellValue(displayLabels[i]);
            }
            hhCell.setCellStyle(hhStyle);
            if (i >= container.getViewByCount()) {
                String alignment = null;
                if (isCrossTabReport) {
                    if (crosstabMeasureId.containsKey(displayColumns[i]) && container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString()) != null) {
                        alignment = container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString());
                    }
                } else if (container.getMeasureAlign(displayColumns[i]) != null) {
                    alignment = container.getMeasureAlign(displayColumns[i]).toString();
                }

            }

        }

        WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
        DataFacade facade1 = new DataFacade(container);
        PbReportTableBD reportTableBD = new PbReportTableBD();
        reportTableBD.searchDataSet(this.container);
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }
        int k = 0;
        HSSFCellStyle xStyle = null;
        hhStyle = hhWorkbook.createCellStyle();//added by Dinanath for colorgroup
        ArrayList<String> al = null;
        for (int i = fromRow; i < toRow; i++) {
            if (i < actualtoRow) {
                hhRow = hhSheet.createRow(k + lineNumber);
                if (isCrossTabReport == true) {
                    al = new ArrayList<>();
                }
                for (int j = 0; j < columnCount; j++) {
                    String colName = displayColumns[j];
                    String discolName = displayLabels[j];
                    Colour BGColor = null;
                    //added by Dinanath forcolorgroup ssssss
                    Short numOfCellStyle = hhWorkbook.getNumCellStyles();
                    BigDecimal subtotalval = null;
                    String bgColor = null;
                    int actualRow = facade1.getActualRow(i);
                    //for cross tab code added by Dinanath
                    if (isCrossTabReport == true) {
                        try {
                            al.add(colName);
                            facade1.setMeasures(al);
                        } catch (Exception e) {
                        }
                    }
                    Colour clr = null;
                    try {
                        bgColor = facade1.getColor(actualRow, colName, subtotalval);
                    } catch (ArrayIndexOutOfBoundsException e) {
                         logger.error("Exception: ",e);
                    } catch (Exception ex) {
                         logger.error("Exception: ",ex);
                    }

                    try {
                        int rgb[] = getRGB(bgColor.replace("#", ""));
                        int rgbRed = rgb[0];
                        int rgbGreen = rgb[1];
                        int rgbBlue = rgb[2];
                    } catch (Exception e) {
                          logger.error("Exception: ",e);
                    }
                    if (bgColor != null && !bgColor.isEmpty()) {
                        if (numOfCellStyle < 4000) {
                            hhStyle = hhWorkbook.createCellStyle();
                            int rgbRed = 255, rgbGreen = 0, rgbBlue = 0;
                            try {
                                int rgb[] = getRGB(bgColor.replace("#", ""));
                                rgbRed = rgb[0];
                                rgbGreen = rgb[1];
                                rgbBlue = rgb[2];
                            } catch (Exception e) {
                                  logger.error("Exception: ",e);
                            }
                            HSSFPalette palette = hhWorkbook.getCustomPalette();
                            HSSFColor myColor = palette.findSimilarColor(rgbRed, rgbGreen, rgbBlue);
                            short palIndex = myColor.getIndex();
                            HSSFColor borderColor1 = palette1.findSimilarColor(224, 224, 224);
                            short borderIndexColor = borderColor1.getIndex();
                            hhStyle.setFillBackgroundColor(palIndex);
                            //Added by Ram 15June2015 for white font.
                            HSSFFont font = hhWorkbook.createFont();
                            font.setColor(HSSFColor.WHITE.index);
                            hhStyle.setFont(font);
                            //end Ram code
                            hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
                            hhStyle.setFillForegroundColor(palIndex);
                            hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                            hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setLeftBorderColor(borderIndexColor);
                            hhStyle.setRightBorderColor(borderIndexColor);
                            hhStyle.setTopBorderColor(borderIndexColor);
                            hhStyle.setBottomBorderColor(borderIndexColor);

                        } else {
                            HSSFPalette palette = hhWorkbook.getCustomPalette();
                            HSSFColor myColor = palette.findSimilarColor(255, 255, 255);
                            short palIndex = myColor.getIndex();
                            HSSFColor borderColor1 = palette1.findSimilarColor(224, 224, 224);
                            short borderIndexColor = borderColor1.getIndex();
                            hhStyle.setFillBackgroundColor(palIndex);
                            hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
                            hhStyle.setFillForegroundColor(palIndex);
                            hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                            hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setLeftBorderColor(borderIndexColor);
                            hhStyle.setRightBorderColor(borderIndexColor);
                            hhStyle.setTopBorderColor(borderIndexColor);
                            hhStyle.setBottomBorderColor(borderIndexColor);

                        }
                    } else if (numOfCellStyle < 4000) {
                        hhStyle = hhWorkbook.createCellStyle();
                        HSSFPalette palette = hhWorkbook.getCustomPalette();
                        HSSFColor myColor = palette.findSimilarColor(255, 255, 255);
                        short palIndex = myColor.getIndex();
                        HSSFColor borderColor1 = palette1.findSimilarColor(224, 224, 224);
                        short borderIndexColor = borderColor1.getIndex();
                        hhStyle.setFillBackgroundColor(palIndex);
                        hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
                        hhStyle.setFillForegroundColor(palIndex);
                        hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setLeftBorderColor(borderIndexColor);
                        hhStyle.setRightBorderColor(borderIndexColor);
                        hhStyle.setTopBorderColor(borderIndexColor);
                        hhStyle.setBottomBorderColor(borderIndexColor);

                    } else {
                        HSSFPalette palette = hhWorkbook.getCustomPalette();
                        HSSFColor myColor = palette.findSimilarColor(255, 255, 255);
                        short palIndex = myColor.getIndex();
                        HSSFColor borderColor1 = palette1.findSimilarColor(224, 224, 224);
                        short borderIndexColor = borderColor1.getIndex();
                        hhStyle.setFillBackgroundColor(palIndex);
                        hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
                        hhStyle.setFillForegroundColor(palIndex);
                        hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setLeftBorderColor(borderIndexColor);
                        hhStyle.setRightBorderColor(borderIndexColor);
                        hhStyle.setTopBorderColor(borderIndexColor);
                        hhStyle.setBottomBorderColor(borderIndexColor);

                    }
//endded by Dinanath

                    if (j >= container.getViewByCount()) {
                        String alignment = null;
                        if (isCrossTabReport) {
                            if (crosstabMeasureId.containsKey(colName) && container.getTextAlign(crosstabMeasureId.get(colName).toString()) != null) {
                                alignment = container.getTextAlign(crosstabMeasureId.get(colName).toString());
                            }
                        } else if (container.getTextAlign(colName) != null) {
                            alignment = container.getTextAlign(colName);
                        }
                        if (alignment != null && !alignment.equalsIgnoreCase("")) {
                            if (alignment.equalsIgnoreCase("left")) {
                                //                writableCellFormat.setAlignment(Alignment.LEFT);
                                //                cf2.setAlignment(Alignment.LEFT);
                            } else if (alignment.equalsIgnoreCase("center")) {
                                //                writableCellFormat.setAlignment(Alignment.CENTRE);
                                //                cf2.setAlignment(Alignment.CENTRE);
                            } else if (alignment.equalsIgnoreCase("right")) {
                                //              writableCellFormat.setAlignment(Alignment.RIGHT);
                                //               cf2.setAlignment(Alignment.RIGHT);
                            } else {
                                //               writableCellFormat.setAlignment(Alignment.GENERAL);
                                //                cf2.setAlignment(Alignment.GENERAL);  }
                            }
                        }
                    }

                    if ("D".equals(types[j])) {
                        hhCell = hhRow.createCell(j + colNumber);
                        hhCell.setCellValue(ret.getFieldValueDateString(sortSequence.get(i), colName));
                        hhCell.setCellStyle(hhStyle);//added by Dinanath
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueDateString(sortSequence.get(i), colName));
                        //cell.setCellFormat(writableCellFormat);
                    } else if ("N".equals(types[j]) && !container.getReportCollect().reportRowViewbyValues.contains(colName.replace("A_", ""))) {
                        if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                            String formattedData = "";
                            String snbrSymbol = "";
                            String nbrSymbol = "";
                            if (ret.getFieldValueBigDecimal(sortSequence.get(i), colName) != null) {
                                //String element = container.getDisplayColumns().get(j);
                                String element = displayColumns[j];
                                HashMap NFMap = new HashMap();
                                NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                                if (NFMap != null) {
                                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                        if (crosstabMeasureId.containsKey(element) && NFMap.get(crosstabMeasureId.get(element)) != null) {
                                            nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                        }
                                    } else if (NFMap.get(element) != null) {
                                        nbrSymbol = String.valueOf(NFMap.get(element));
                                    }
                                }
                                // Code Added by Amar to add snbrSymbol
                                HashMap colProps = container.getColumnProperties();
                                ArrayList propList;
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                    propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                                } else {
                                    propList = (ArrayList) colProps.get(element);
                                }
                                if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                                    String colSymbol = "";
                                    if (propList != null && propList.get(7) != null) {
                                        colSymbol = (String) propList.get(7);
                                    }
                                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                                        container.symbol.put(element, colSymbol);
                                    }
                                }
// end of code
                                snbrSymbol = container.symbol.get(displayColumns[j]);
                                int precision = container.getRoundPrecisionForMeasure(element);
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(element)) {
                                    precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                                }
                                if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                    formattedData = ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString();
                                } else if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    formattedData = this.convertDataToTime(new BigDecimal(ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString()));
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                    // formattedData = snbrSymbol+""+formattedData;
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    if (snbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = formattedData + "" + snbrSymbol;
                                    } else {
                                        formattedData = snbrSymbol + "" + formattedData;
                                    }
                                } else {
                                    formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);

                                }
                            }
                            if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                            } else if (!sortSequence.isEmpty()) {
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(formattedData);
                                    hhCell.setCellStyle(hhStyle);
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(formattedData);
                                    hhCell.setCellStyle(hhStyle);
                                } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                    //cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
                                    hhCell.setCellStyle(hhStyle);
                                } else {
                                    //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(formattedData);
                                    hhCell.setCellStyle(hhStyle);
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(formattedData);
                                hhCell.setCellStyle(hhStyle);
                            } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                // cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(formattedData);
                                hhCell.setCellStyle(hhStyle);
                            } else {
                                //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(formattedData);
                                hhCell.setCellStyle(hhStyle);
                            }
                        } else {
                            //changed by sruthi % wise for excel
                            String Symbol = "";
                            Symbol = container.getNumberSymbol(displayColumns[j]);
                            String formattedData = "";
                            String elementid = displayColumns[j];
                            int precision1 = container.getRoundPrecisionForMeasure(elementid);
                            if (!sortSequence.isEmpty()) {
//    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(sortSequence.get(i),colName).doubleValue(),cf2);
                                if (Symbol != null && Symbol.equalsIgnoreCase("%") && !Symbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName), Symbol, precision1);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(formattedData);
                                    hhCell.setCellStyle(hhStyle);
                                } else {
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName).doubleValue());
                                    hhCell.setCellStyle(hhStyle);//added by Dinanath
                                }
                            } else //    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(i,colName).doubleValue(),cf2);
                            if (Symbol != null && Symbol.equalsIgnoreCase("%") && !Symbol.equalsIgnoreCase("")) {
                                formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueRuntimeMeasure(i, colName), Symbol, precision1);
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(formattedData);
                                hhCell.setCellStyle(hhStyle);
                            } else {
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(ret.getFieldValueRuntimeMeasure(i, colName).doubleValue());
                                hhCell.setCellStyle(hhStyle);
                            }//ended by sruthi
                        }
                        HashMap<String, Integer> RGBColerCodes = null;
                        if (!sortSequence.isEmpty()) {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(sortSequence.get(i), colName));
                        } else {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(i, colName));
                        }
                        Collection<Integer> collection = RGBColerCodes.values();
                        TreeSet<Integer> treeSet = new TreeSet<Integer>();
                        for (Integer val : collection) {
                            treeSet.add(val);
                        }
                        String tempStr = "";
                        for (String string : RGBColerCodes.keySet()) {
                            if (RGBColerCodes.get(string) == treeSet.last()) {
                                tempStr = string;
                            }
                        }

                        if (!RGBColerCodes.isEmpty()) {
                            writableCellFormatForColorCode = new WritableCellFormat(writableFont);
                            writableCellFormatForColorCode.setWrap(true);
                            if (tempStr.equalsIgnoreCase("RED")) {
                                if (RGBColerCodes.get("RED") >= 239 && RGBColerCodes.get("RED") < 244) {
                                    writableWorkbook.setColourRGB(Colour.ORANGE, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.ORANGE);
                                } else if (RGBColerCodes.get("RED") > 244 && RGBColerCodes.get("RED") <= 248) {
                                    writableWorkbook.setColourRGB(Colour.YELLOW, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.YELLOW);
                                } else {
                                    writableWorkbook.setColourRGB(Colour.RED, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.RED);
                                }
                            } else if (tempStr.equalsIgnoreCase("GREEN")) {
                                writableWorkbook.setColourRGB(Colour.GREEN, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                writableCellFormatForColorCode.setBackground(Colour.GREEN);
                            } else if (RGBColerCodes.get("BLUE") > 244 && RGBColerCodes.get("BLUE") <= 248) {
                                writableWorkbook.setColourRGB(Colour.VIOLET, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                writableCellFormatForColorCode.setBackground(Colour.VIOLET);
                            } else if (RGBColerCodes.get("BLUE") >= 103 && RGBColerCodes.get("BLUE") < 244) {
                                writableWorkbook.setColourRGB(Colour.INDIGO, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                writableCellFormatForColorCode.setBackground(Colour.INDIGO);
                            } else {
                                writableWorkbook.setColourRGB(Colour.BLUE, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                writableCellFormatForColorCode.setBackground(Colour.BLUE);
                            }

                            //cell.setCellFormat(writableCellFormatForColorCode);
                        }

                    } else if (!sortSequence.isEmpty()) {
                        //  cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(sortSequence.get(i), colName));
                        hhCell = hhRow.createCell(j + colNumber);
                        hhCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
                        hhCell.setCellStyle(hhStyle);

                    } else {
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(i, colName));
                        hhCell = hhRow.createCell(j + colNumber);
                        hhCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
                        hhCell.setCellStyle(hhStyle);

                    } //cell.setCellFormat(writableCellFormat);
                    //writableSheet.addCell(cell);
                    colName = null;
                    writableCellFormatForColorCode = null;
                }
            }
            k++;
        }

        if (gTotal.equalsIgnoreCase("on")) {
            this.generateSubTotalHtmlForExportReportXLS(k);
        }

        // }
    }
    //added by Dinanath for xls

    public void generateSubTotalHtmlForExportReportXLS(int k) {

        ArrayList displayValues = new ArrayList();
        ArrayList viewBys = new ArrayList();
        ArrayList viewBys1 = new ArrayList();
        displayValues = container.getDisplayColumns();
//        for(int i=0;i<displayColumns.length;i++){
//            displayValues.add(displayColumns[i]);
//        }
        viewBys1 = container.getDisplayTypes();
        for (int l = 0; l < types.length; l++) {
            viewBys.add(types[l]);
        }
        if (displayValues.size() > viewBys.size()) {
            for (int i = 0; i < displayValues.size(); i++) {
                if (i >= viewBys.size()) {
                    viewBys.add("N");
                }
            }
        }
        //started by Amar
        String aggType = "";
        String refferedElements = "";
        String userColType = "";
        String refElementType = "";
        String tempFormula = "";
        boolean isRunTime = false;
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = null;
        DataFacade facade = new DataFacade(container);
        ArrayList<String> hidemeasurecount = facade.container.getReportCollect().getHideMeasures();
        ArrayList<Integer> index = new ArrayList<Integer>();
        // end by Amar
        // code added by Amar

        //code Added by Amar
        String eleIdss = "";
        if (container.isReportCrosstab()) {
            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
            for (Object hiddenCol : hidemeasurecount) {
                for (int i = 0; i < displayValues.size(); i++) {
                    eleIdss = crosstabMeasureId.get(displayValues.get(i));
                    if (eleIdss != null && !eleIdss.equalsIgnoreCase("null") && !eleIdss.equalsIgnoreCase("")) {
                        if (hidemeasurecount.contains(eleIdss.replace("A_", ""))) {
                            index.add(i);
                        }
                        // index.add
                    }
                }
            }
        } else if (hidemeasurecount.size() > 0) {
            for (int i = 0; i < hidemeasurecount.size(); i++) {
                index.add(facade.container.getDisplayColumns().indexOf("A_" + hidemeasurecount.get(i)));
            }
        }
        // end of code
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }

        //  for (int i = 0; i < 2; i++) {
        int actualcol = 0;
        if (facade.isGrandTotalRequired()) {
            hhRow = hhSheet.createRow(k + lineNumber);
            for (int col = 0; col < displayValues.size(); col++) {
                if (!index.contains(col) && col < 256) {//modified by Dinanath
                    if (viewBys.get(col).toString().equalsIgnoreCase("N")) {
//                 BigDecimal grandToatal=(BigDecimal)ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                        String formattedData = "";
                        String element = container.getDisplayColumns().get(col);
                        String nbrSymbol = "";
                        HashMap NFMap = new HashMap();
                        NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
                        if (NFMap != null) {
                            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                if (NFMap.get(crosstabMeasureId.get(element)) != null) {
                                    nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                }
                            } else if (NFMap.get(element) != null) {
                                nbrSymbol = String.valueOf(NFMap.get(element));
                            }
                        }
                        if (container.getNumberSymbol(element) != null && !container.getNumberSymbol(element).equalsIgnoreCase("") && !container.getNumberSymbol(element).isEmpty()) {
                            nbrSymbol = container.getNumberSymbol(element);
                        }
                        //Added by Amar
                        if (RTMeasureElement.isRunTimeMeasure(element)) {
                            nbrSymbol = RTMeasureElement.getMeasureType(element).getNumberSymbol();
                        }
                        //end of code
                        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
                        int precision = container.getRoundPrecisionForMeasure(element);
                        if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.get(element) != null) {
                            precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                            element = element.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
                            element = crosstabMeasureId.get(element);
                        }

                        BigDecimal grandToatal;
                        // Modified by Amar
                        if (element != null && !element.equalsIgnoreCase("null") && !element.equalsIgnoreCase("") && !element.isEmpty()) {
                            String eleId = element.replace("A_", "");
                            if (eleId.contains("_percentwise") || eleId.contains("_rank") || eleId.contains("_wf") || eleId.contains("_wtrg") || eleId.contains("_rt") || eleId.contains("_pwst") || eleId.contains("_excel") || eleId.contains("_excel_target") || eleId.contains("_deviation_mean") || eleId.contains("_gl") || eleId.contains("_userGl") || eleId.contains("_timeBased") || eleId.contains("_changedPer") || eleId.contains("_glPer")) {
                                eleId = eleId.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
                                isRunTime = true;
                            }
                            String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
                            try {
                                retobj = pbdb.execSelectSQL(qry);
                            } catch (SQLException ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retobj != null && retobj.getRowCount() > 0) {
                                tempFormula = retobj.getFieldValueString(0, 0);
                                refferedElements = retobj.getFieldValueString(0, 1);
                                userColType = retobj.getFieldValueString(0, 2);
                                refElementType = retobj.getFieldValueString(0, 3);
                                aggType = retobj.getFieldValueString(0, 4);
                                tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");

                            }

//                     if (i == 0) {
//                        if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
//                            //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                             if(aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")){
//                                 //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
//                                 grandToatal = ret.getColumnGrandTotalValue(facade.getColumnId(col));
//                             }else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
//                                grandToatal = BigDecimal.ZERO;
//                            } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
//                                grandToatal = BigDecimal.ZERO;
//                            } else {
//                                grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
//                            }
//                        } else {
//                            grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
//                        }
//                    //} else {
//                      //  grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
//                    //}
//                     if(aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")){
//                        int rowCnt = 0;
//                            rowCnt=facade.getRowCount();
//                             grandToatal=grandToatal.divide(new BigDecimal(rowCnt),RoundingMode.HALF_UP);
//                         }
                            // end of code bu Amar
                            if (!container.isReportCrosstab()) {
                                //code added by Dinanath
                                if (isRunTime) {
                                    if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    }
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    //modified by anitha
                                } else if (userColType.equalsIgnoreCase("TIMECALUCULATED") && (aggType.contains("avg") || aggType.contains("AVG"))) {
                                    facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));

                                } else if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
                                    if (!facade.container.getKpiQrycls().contains(facade.getColumnId(col).replace("A_", "").trim())) {

                                        grandToatal = (BigDecimal) getGTForChangePerOfSummTabMeasures(eleId, col);

                                    } else if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
                                        //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                        if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
                                            grandToatal = BigDecimal.ZERO;
                                        } else if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
                                            grandToatal = BigDecimal.ZERO;
                                        } else {
                                            grandToatal = new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
                                        }

                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(displayValues.get(col).toString());
                                    }
                                } else if (userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("AVG")) {
                                    String refEleArray[] = refferedElements.split(",");
                                    int len = refEleArray.length;
                                    int flag = 1;
                                    String mysqlString = "";
                                    for (int j = 0; j < len; j++) {
                                        String elementId = refEleArray[j];
                                        String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                        PbReturnObject retobj1 = null;
                                        try {
                                            retobj1 = pbdb.execSelectSQL(getBussColName);
                                        } catch (SQLException ex) {
                                            logger.error("Exception: ", ex);
                                        }
                                        if (retobj1 != null && retobj1.getRowCount() > 0) {
                                            String bussColName = retobj1.getFieldValueString(0, 0);
                                            String aggrType = retobj1.getFieldValueString(0, 1);
                                            if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                String newEleID = "A_" + elementId;
                                                tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                BigDecimal grandTotalValueForEle = null;
                                                grandTotalValueForEle = facade.getColumnGrandTotalValue(newEleID);
                                                if (grandTotalValueForEle == null) {
                                                    flag = 0;
                                                }
                                                if (flag == 1) {
                                                    grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                                    if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                        grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                    }
                                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                        mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                                    } else {
                                                        tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                    }
                                                } else {
                                                    grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    }
                                    //Calculate formula
                                    if (!tempFormula.equalsIgnoreCase("")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "OTHERS");
                                        grandToatal = (BigDecimal) getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                    } else {
                                        grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                    }
                                } else if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
                                    //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                    if (aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")) {
                                        //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");

                                        grandToatal = ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                        int rowCnt = 0;
                                        if (facade.container.getReportCollect().crosscolmap1.get(facade.getColumnId(col)).toString().equalsIgnoreCase("Exclude 0")) {
                                            rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), col, facade.getColumnId(col));
                                        } else {
                                            rowCnt = facade.getRowCount();
                                        }
                                        grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                    } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
                                        grandToatal = BigDecimal.ZERO;
                                    } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
                                        grandToatal = BigDecimal.ZERO;
                                    } else {
                                        grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
                                    }
                                } else {
                                    grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                                }
                                //added code by anitha
                                if (userColType.equalsIgnoreCase("TIMECALUCULATED") && (aggType.contains("avg") || aggType.contains("AVG"))) {
                                    int rowCnt = 0;
                                    if ("1".equals(facade.container.stype) || "0".equals(facade.container.stype)) {
                                        rowCnt = facade.container.gettrwcnt();
                                    } else {
                                        rowCnt = facade.getRowCount();
                                        facade.container.settrwcnt(rowCnt);
                                    }

                                    if (rowCnt != 0) {
                                        precision = container.getRoundPrecisionForMeasure(displayColumns[col]);
                                        grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                        grandToatal = new BigDecimal(NumberFormatter.getModifiedNumberFormat(grandToatal, "", precision));
                                        formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                    }
                                }
                                //end of code by anitha
                            } else if (container.isReportCrosstab()) {

                                HashMap<String, ArrayList> nonViewByMapNew = null;
                                nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
                                try {
                                    if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    }
//                        if (facade.container.getKpiQrycls().contains(eleId.trim())) {
                                    if (eleId != null && eleId != "null" && eleId != "" && aggType.equalsIgnoreCase("avg")) {
                                        //for calculating gt for avg cols like A_10,A_11 which are of gt type and is summarized
                                        if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                                            String refEleArray[] = refferedElements.split(",");
                                            int len = refEleArray.length;
                                            int flag = 1;
                                            String mysqlString = "";
                                            if (facade.getColumnId(col).contains("A_")) {
                                                for (int j = 0; j < len; j++) {
                                                    String elementId = refEleArray[j];
                                                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                                    PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                        BigDecimal grandTotalValueForEle = null;
                                                        String bussColName = retobj1.getFieldValueString(0, 0);
                                                        String aggrType = retobj1.getFieldValueString(0, 1);
                                                        if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                            String newEleID = "A_" + elementId;
                                                            tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                            for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                                                String key = e.getKey();
                                                                String value = e.getValue();
                                                                if (value.equalsIgnoreCase(newEleID) && key.contains("A_")) {
                                                                    grandTotalValueForEle = facade.getColumnGrandTotalValue(key);
                                                                    break;
                                                                }
                                                            }
                                                            if (grandTotalValueForEle == null) {
                                                                flag = 0;
                                                            }
                                                            if (flag == 1) {
                                                                grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                                                if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                                    grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                                }
                                                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                                    mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                                                } else {
                                                                    tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                                }
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                            }
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                    }
                                                }
                                                //Calculate formula value
                                                if (!tempFormula.equalsIgnoreCase("")) {
                                                    grandToatal = getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            } else //for calculating gt for avg cols like A1,A2,A3 which are not of gt type
                                            if (nonViewByMapNew.get(facade.getColumnId(col)) != null) {
                                                String keyset = nonViewByMapNew.get(facade.getColumnId(col)).toString().replace("[", "").replace("]", "").trim();
                                                String mainKeySetArray1[] = keyset.split(",");
                                                String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
                                                for (int m = 0; m < mainKeySetArray1.length - 1; m++) {
                                                    String val1 = mainKeySetArray1[m];
                                                    mainKeySetArray[m] = mainKeySetArray1[m];
                                                }

                                                for (int j = 0; j < len; j++) {
                                                    int flag1 = 0;
                                                    flag = 0;
                                                    BigDecimal grangTotalValueForEle = null;
                                                    String elementId = refEleArray[j];
                                                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                                    PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                        String bussColName = retobj1.getFieldValueString(0, 0);
                                                        String aggrType = retobj1.getFieldValueString(0, 1);
                                                        if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                            String newEleID = "A_" + elementId;
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
                                                                            for (int m = 0; m < tempKeysetArray1.length - 1; m++) {
                                                                                tempKeysetArray[m] = tempKeysetArray1[m];
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
                                                                                        grangTotalValueForEle = facade.getColumnGrandTotalValue(key);
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
                                                                if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                                    grangTotalValueForEle = grangTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                                }
                                                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                                    mysqlString = mysqlString + "," + grangTotalValueForEle + " AS " + newEleID;
                                                                } else {
                                                                    tempFormula = tempFormula.replace(newEleID, grangTotalValueForEle.toString());
                                                                }
                                                                flag1 = 1;
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                            }
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                    }
                                                }
                                                //Calculate formula value
                                                if (!tempFormula.equalsIgnoreCase("")) {
                                                    grandToatal = getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            int rowCnt = 0;
                                            if (facade.container.getReportCollect().crosscolmap1.get(element).toString().equalsIgnoreCase("Exclude 0")) {
                                                rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), col, facade.getColumnId(col));
                                            } else {
                                                rowCnt = facade.getRowCount();
                                            }
                                            grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                        }
                                    } else if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
                                        if (nonViewByMapNew.get(facade.getColumnId(col)) != null) {
                                            String keyset = nonViewByMapNew.get(facade.getColumnId(col)).toString().replace("[", "").replace("]", "").trim();
                                            if (facade.container.retObjHashmap.get(keyset) != null) {
                                                grandToatal = new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    }
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                }

                            } else {
                                grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                            }
                            if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                formattedData = grandToatal.toString();
                            } else if (container.gettimeConversion(displayValues.get(col).toString()) != null && container.gettimeConversion(displayValues.get(col).toString()).equalsIgnoreCase("Y")) {
                                formattedData = this.convertDataToTime(grandToatal);
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                // formattedData = snbrSymbol+""+formattedData;
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                if (snbrSymbol.equalsIgnoreCase("%")) {
                                    formattedData = formattedData + "" + snbrSymbol;
                                } else {
                                    formattedData = snbrSymbol + "" + formattedData;
                                }
                            } else {
                                formattedData = NumberFormatter.getModifiedNumber(grandToatal, nbrSymbol, precision);
                            }
                            //cell = new Number(col, i+k+rowStart+1, Double.parseDouble(formattedData),cf2);
                            if (container.gettimeConversion(displayValues.get(col).toString()) != null && container.gettimeConversion(displayValues.get(col).toString()).equalsIgnoreCase("Y")) {
                                //cell = new Label(col, i+k+rowStart+1, formattedData,cf2);
                            } else if (formattedData.contains("%")) {
                                //cell = new Label(col, i + k + rowStart + 1, formattedData, cf2);
                                hhCell = hhRow.createCell(actualcol + colNumber);
                                hhCell.setCellValue(formattedData);
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                                    hhCell = hhRow.createCell(actualcol + colNumber);
                                    hhCell.setCellValue(formattedData);
                                } else {
                                    //cell = new Number(col, i + k + rowStart + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                    hhCell = hhRow.createCell(actualcol + colNumber);
                                    hhCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                                hhCell = hhRow.createCell(actualcol + colNumber);
                                hhCell.setCellValue(formattedData);
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                                hhCell = hhRow.createCell(actualcol + colNumber);
                                hhCell.setCellValue(formattedData);
                            } else {
                                //cell = new Number(col, i + k + rowStart + 1, Double.parseDouble(formattedData.replace(",", "")), cf2);
                                hhCell = hhRow.createCell(actualcol + colNumber);
                                hhCell.setCellValue(Double.parseDouble(formattedData.replace(",", "")));
                            }
                        }
                    } else {
                        //  if(i==0){
                        //cell = new Label(col, i+k+rowStart+1, "Grand Total");
                        hhCell = hhRow.createCell(col + colNumber);
                        hhCell.setCellValue("Grand Total");
//}
//                    else{
//                         //cell = new Label(col, i+k+rowStart+1, "Sub Total");         // Modified from Category Sub Total to Sub Total by Amar
//                        hCell= hRow.createCell(col+colNumber);
//                        hCell.setCellValue("Sub Total");
//                    }
                    }
//                cell.setCellFormat(writableCellFormat);

//             try {
//                 //writableSheet.addCell(cell);
//             } catch (WriteException ex) {
//                 logger.error("Exception: ",ex);
//            }
                    actualcol++;
                }
            }
            k = k + 1;
        }

        //added by sruthi for overallavg,overallmax,overallmin
        int j = 0;
        BigDecimal othersValue = null;
        if (facade.isAverageRequired()) {
            hhRow = hhSheet.createRow(k + lineNumber);
            BigDecimal overallavg;
            for (int i = 0; i < 1; i++) {

                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (!index.contains(col)) {
                        if (viewBys.get(col).toString().equalsIgnoreCase("T") || viewBys.get(col).toString().equalsIgnoreCase("N")) {
                            othersValue = (BigDecimal) facade.getColumnAverageValue(displayValues.get(col).toString());
                            this.getOthersDataS(othersValue, col, hhRow, startcol, displayValues);
                        } else {
                            if (i == 0) {
                                hhCell = hhRow.createCell(startcol);
                                hhCell.setCellValue("OverAllAvg");
                            }
                        }
                        startcol++;
                    }
                }
            }
            j++;
        }
        if (facade.isOverAllMaxRequired()) {
            BigDecimal overAllMax;
            hhRow = hhSheet.createRow(k + lineNumber + j);
            for (int i = 0; i < 1; i++) {
                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (!index.contains(col)) {
                        if (viewBys.get(col).toString().equalsIgnoreCase("T") || viewBys.get(col).toString().equalsIgnoreCase("N")) {
                            othersValue = (BigDecimal) facade.getColumnMaximumValue(displayValues.get(col).toString());
                            this.getOthersDataS(othersValue, col, hhRow, startcol, displayValues);
                        } else {
                            if (i == 0) {
                                hhCell = hhRow.createCell(startcol);
                                hhCell.setCellValue("OverAllMax");
                            }
                        }
                        startcol++;
                    }
                }
            }
            j++;
        }
        if (facade.isOverAllMinRequired()) {
            hhRow = hhSheet.createRow(k + lineNumber + j);
            for (int i = 0; i < 1; i++) {
                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (!index.contains(col)) {
                        if (viewBys.get(col).toString().equalsIgnoreCase("T") || viewBys.get(col).toString().equalsIgnoreCase("N")) {
                            othersValue = (BigDecimal) facade.getColumnMinimumValue(displayValues.get(col).toString());
                            this.getOthersDataS(othersValue, col, hhRow, startcol, displayValues);
                        } else {
                            if (i == 0) {
                                hhCell = hhRow.createCell(startcol);
                                hhCell.setCellValue("OverAll Min");
                            }
                        }
                        startcol++;
                    }
                }
            }
            j++;
        }
        //}
    }

    private void buildReportTableXLSM() throws Exception {
        boolean isCrossTabReport = false;
        if (container.getReportCollect().reportColViewbyValues != null && container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        // writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

//         hhhhStyle = hhhhWorkbook.createCellStyle();
        //added by Dinanath for color group
//         hhhhStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
//         hhhhStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
//         hhhhStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
//         hhhhStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//         hhhhStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//         hhhhStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
//         hhhhStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
//         hhhhStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        //endded by Dinanath for color group
        //hStyle.setAlignment(Alignment.CENTRE);
        int columnViewByCount = Integer.parseInt(container.getColumnViewByCount());
        hhhhRow = hhhhSheet.createRow(lineNumber);
        lineNumber++;
        for (int i = 0; i < columnCount; i++) {
            if (isCrossTabReport) {
                // label = new Label(i, lineNumber, displayLabels[i]);
                // hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
                hhhhCell = hhhhRow.createCell(i + colNumber);
                hhhhCell.setCellValue(displayLabels[i]);
            } else if (displayLabels[i].contains("Prev Month") && this.getAggregationForSubtotal(displayColumns[i]).contains("#T")) {
                // label = new Label(i, lineNumber, container.getMonthNameforTrailingFormula(i));
                //   hCell=hRow.createCell(i);
                //  hCell.setCellValue(container.getMonthNameforTrailingFormula(i));
                hhhhCell = hhhhRow.createCell(i + colNumber);
                hhhhCell.setCellValue(displayLabels[i]);
            } else {
                //label = new Label(i, lineNumber, displayLabels[i]);
                //hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
                hhhhCell = hhhhRow.createCell(i + colNumber);
                hhhhCell.setCellValue(displayLabels[i]);
            }
//            hhhhCell.setCellStyle(hhhhStyle);
//           writableCellFormat = new WritableCellFormat(writableFont);
//           writableCellFormat.setBackground(Colour.GRAY_25);
//           writableCellFormat.setWrap(true);
//            writableCellFormat.setAlignment(Alignment.GENERAL);

//            if (i >= container.getViewByCount()) {
//                String alignment = null;
//                if (isCrossTabReport) {
//                    if (crosstabMeasureId.containsKey(displayColumns[i]) && container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString()) != null) {
//                        alignment = container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString());
//                    }
//                } else {
//                    if (container.getMeasureAlign(displayColumns[i]) != null) {
//                        alignment = container.getMeasureAlign(displayColumns[i]).toString();
//                    }
//                }
//              if(alignment!=null){
//                if(alignment.equalsIgnoreCase("left"))
//                writableCellFormat.setAlignment(Alignment.LEFT);
//                else if(alignment.equalsIgnoreCase("center"))
//                writableCellFormat.setAlignment(Alignment.CENTRE);
//                else if(alignment.equalsIgnoreCase("right"))
//                writableCellFormat.setAlignment(Alignment.RIGHT);
//                else
//                  writableCellFormat.setAlignment(Alignment.GENERAL);
//            }
//            }
            //label.setCellFormat(writableCellFormat);
            // writableSheet.addCell(label);
            //HeadingCellView = new CellView();
            //HeadingCellView.setSize(256 * 20);
            // writableSheet.setColumnView(i, HeadingCellView);
        }

//        if(container.getViewByCount()>1 && actualtoRow<65000){
//        DataFacade facade = new DataFacade(container);
//        facade.setCtxPath(container.getReportCollect().ctxPath);
//        TableBuilder tableBldr = new RowViewTableBuilder(facade);
//        if (fromRow == 0 && toRow == 0)
//            tableBldr.setFromAndToRow(0,container.getRetObj().getViewSequence().size());
//        else
//            tableBldr.setFromAndToRow(fromRow, toRow);
//        TableDisplay bodyHelper = null;
//        TableDisplay subTotalHelper = null;
//        bodyHelper = new ExportExcelTableBodyDisplay(tableBldr);
//        bodyHelper.setHeadlineflag("Export");
//        //bodyHelper.setWritableSheet(writableSheet);
//        bodyHelper.setHSheet(hSheet);
//       // bodyHelper.setWritableWorkbook(writableWorkbook);
//        bodyHelper.setHWorkbook(hWorkbook);
//        bodyHelper.setStartRow(lineNumber);
//        subTotalHelper=new ExportExcelTableSubTotalDisplay(tableBldr);
//        //subTotalHelper.setWritableSheet(writableSheet);
//        //subTotalHelper.setWritableWorkbook(writableWorkbook);
//          subTotalHelper.setHSheet(hSheet);
//          subTotalHelper.setHWorkbook(hWorkbook);
//          subTotalHelper.setStartRow(lineNumber);
//          bodyHelper.setNext(subTotalHelper);
//        StringBuilder tableHtml = new StringBuilder();
//        tableHtml.append(bodyHelper.generateOutputHTML());
//       }else{
        //writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
        //WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
//        WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
//        DataFacade facade1 = new DataFacade(container);
        PbReportTableBD reportTableBD = new PbReportTableBD();
        reportTableBD.searchDataSet(this.container);
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }
        int k = 0;
//        XSSFCellStyle xStyle = null;

        for (int i = fromRow; i < toRow; i++) {
            if (i < actualtoRow) {
                hhhhRow = hhhhSheet.createRow(k + lineNumber);
                for (int j = 0; j < columnCount; j++) {
                    String colName = displayColumns[j];
                    String discolName = displayLabels[j];
//                    Colour BGColor = null;
                    //added by Dinanath for colorgroup xlsxandxlsm
//                    Short numOfCellStyle = hhhhWorkbook.getNumCellStyles();
//                    BigDecimal subtotalval = null;
//                    String bgColor = null;
//                    int actualRow = facade1.getActualRow(i);
//                    Colour clr = null;
//                    try {
//                        bgColor = facade1.getColor(actualRow, colName, subtotalval);
//                    } catch (Exception e) {
//                        logger.error("Exception: ",e);
//                    }
//                    if (bgColor != null && !bgColor.isEmpty()) {
                    //if(numOfCellStyle<4000){
//                    hhhhStyle = hhhhWorkbook.createCellStyle();
//                    hhhhStyle.setFillBackgroundColor((short)11);
//                    hhhhStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
//                    hhhhStyle.setFillForegroundColor((short)11);
//                    hhhhStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//                    hhhhStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//                    hhhhStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
//                    hhhhStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
//                    hhhhStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                    //}
//                    } else {
//                    hhhhStyle = hhhhWorkbook.createCellStyle();
//                    hhhhStyle.setFillBackgroundColor((short)11);
//                    hhhhStyle.setFillPattern(XSSFCellStyle.NO_FILL);
//                    hhhhStyle.setFillForegroundColor((short)11);
//                    hhhhStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//                    hhhhStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//                    hhhhStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
//                    hhhhStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
//                    hhhhStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//                    }
//endded by Dinanath
                    //     cf2 = new WritableCellFormat(writableFont,NumberFormats.THOUSANDS_FLOAT);
                    //    cf2.setWrap(true);
                    //     cf2.setAlignment(Alignment.GENERAL);

                    //      writableCellFormat = new WritableCellFormat(writableFont);
                    //      writableCellFormat.setWrap(true);
                    //      writableCellFormat.setAlignment(Alignment.GENERAL);
//                    if (j >= container.getViewByCount()) {
//                        String alignment = null;
//                        if (isCrossTabReport) {
//                            if (crosstabMeasureId.containsKey(colName) && container.getTextAlign(crosstabMeasureId.get(colName).toString()) != null) {
//                                alignment = container.getTextAlign(crosstabMeasureId.get(colName).toString());
//                            }
//                        } else {
//                            if (container.getTextAlign(colName) != null) {
//                                alignment = container.getTextAlign(colName);
//                            }
//                        }
//                        if (alignment != null && !alignment.equalsIgnoreCase("")) {
//                            if (alignment.equalsIgnoreCase("left")) {
//                                //                writableCellFormat.setAlignment(Alignment.LEFT);
//                                //                cf2.setAlignment(Alignment.LEFT);
//                            } else if (alignment.equalsIgnoreCase("center")) {
//                                //                writableCellFormat.setAlignment(Alignment.CENTRE);
//                                //                cf2.setAlignment(Alignment.CENTRE);
//                            } else if (alignment.equalsIgnoreCase("right")) {
//                                //              writableCellFormat.setAlignment(Alignment.RIGHT);
//                                //               cf2.setAlignment(Alignment.RIGHT);
//                            } else {
//                                //               writableCellFormat.setAlignment(Alignment.GENERAL);
//                                //                cf2.setAlignment(Alignment.GENERAL);  }
//                            }
//                        }
//                    }
                    if ("D".equals(types[j])) {
                        hhhhCell = hhhhRow.createCell(j + colNumber);
                        hhhhCell.setCellValue(ret.getFieldValueDateString(sortSequence.get(i), colName));
//                    hhhhCell.setCellStyle(hhhhStyle);//added by Dinanath
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueDateString(sortSequence.get(i), colName));
                        //cell.setCellFormat(writableCellFormat);
                    } else if ("N".equals(types[j]) && !container.getReportCollect().reportRowViewbyValues.contains(colName.replace("A_", ""))) {
                        if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                            String formattedData = "";
                            String snbrSymbol = "";
                            String nbrSymbol = "";
                            if (ret.getFieldValueBigDecimal(sortSequence.get(i), colName) != null) {
                                //String element = container.getDisplayColumns().get(j);
                                String element = displayColumns[j];
                                HashMap NFMap = new HashMap();
                                NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                                if (NFMap != null) {
                                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                        if (crosstabMeasureId.containsKey(element) && NFMap.get(crosstabMeasureId.get(element)) != null) {
                                            nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                        }
                                    } else if (NFMap.get(element) != null) {
                                        nbrSymbol = String.valueOf(NFMap.get(element));
                                    }
                                }
                                // Code Added by Amar to add snbrSymbol
                                HashMap colProps = container.getColumnProperties();
                                ArrayList propList;
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                    propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                                } else {
                                    propList = (ArrayList) colProps.get(element);
                                }
                                if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                                    String colSymbol = "";
                                    if (propList != null && propList.get(7) != null) {
                                        colSymbol = (String) propList.get(7);
                                    }
                                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                                        container.symbol.put(element, colSymbol);
                                    }
                                }
// end of code
                                snbrSymbol = container.symbol.get(displayColumns[j]);
                                int precision = container.getRoundPrecisionForMeasure(element);
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(element)) {
                                    precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                                }
                                if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                    formattedData = ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString();
                                } else if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    formattedData = this.convertDataToTime(new BigDecimal(ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString()));
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                    // formattedData = snbrSymbol+""+formattedData;
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    if (snbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = formattedData + "" + snbrSymbol;
                                    } else {
                                        formattedData = snbrSymbol + "" + formattedData;
                                    }
                                } else {
                                    formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
//                          HashMap colProps = container.getColumnProperties();
//                          if (colProps != null && colProps.containsKey(element)) {
//                          ArrayList propList = (ArrayList) colProps.get(element);
//                           String colSymbol = (String) propList.get(7);
//                           if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) {
//                            if (colSymbol.equalsIgnoreCase("%")) {
//                              formattedData = formattedData + colSymbol;
//                           } else {
//                           formattedData = colSymbol + formattedData;
//                          }
//                          }
//                        }
                                }
                            }
                            if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                            } else if (!sortSequence.isEmpty()) {
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hhhhCell = hhhhRow.createCell(j + colNumber);
                                    hhhhCell.setCellValue(formattedData);
//                               hhhhCell.setCellStyle(hhhhStyle);
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hhhhCell = hhhhRow.createCell(j + colNumber);
                                    hhhhCell.setCellValue(formattedData);
//                               hhhhCell.setCellStyle(hhhhStyle);
                                } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                    //cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                    hhhhCell = hhhhRow.createCell(j + colNumber);
                                    hhhhCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
//                               hhhhCell.setCellStyle(hhhhStyle);
                                } else {
                                    //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                    hhhhCell = hhhhRow.createCell(j + colNumber);
                                    hhhhCell.setCellValue(formattedData);
//                               hhhhCell.setCellStyle(hhhhStyle);
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                hhhhCell = hhhhRow.createCell(j + colNumber);
                                hhhhCell.setCellValue(formattedData);
//                               hhhhCell.setCellStyle(hhhhStyle);
                            } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                // cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                hhhhCell = hhhhRow.createCell(j + colNumber);
                                hhhhCell.setCellValue(formattedData);
//                               hhhhCell.setCellStyle(hhhhStyle);
                            } else {
                                //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                hhhhCell = hhhhRow.createCell(j + colNumber);
                                hhhhCell.setCellValue(formattedData);
//                               hhhhCell.setCellStyle(hhhhStyle);
                            }
                        } else if (!sortSequence.isEmpty()) {
//    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(sortSequence.get(i),colName).doubleValue(),cf2);
                            hhhhCell = hhhhRow.createCell(j + colNumber);
                            hhhhCell.setCellValue(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName).doubleValue());
//                            hhhhCell.setCellStyle(hhhhStyle);
                        } else {
                            //    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(i,colName).doubleValue(),cf2);
                            hhhhCell = hhhhRow.createCell(j + colNumber);
                            hhhhCell.setCellValue(ret.getFieldValueRuntimeMeasure(i, colName).doubleValue());
//                            hhhhCell.setCellStyle(hhhhStyle);
                        }
//                        HashMap<String, Integer> RGBColerCodes = null;
//                        if (!sortSequence.isEmpty()) {
//                            RGBColerCodes = getColor(colName, ret.getFieldValueString(sortSequence.get(i), colName));
//                        } else {
//                            RGBColerCodes = getColor(colName, ret.getFieldValueString(i, colName));
//                        }
//                        Collection<Integer> collection = RGBColerCodes.values();
//                        TreeSet<Integer> treeSet = new TreeSet<Integer>();
//                        for (Integer val : collection) {
//                            treeSet.add(val);
//                        }
//                        String tempStr = "";
//                        for (String string : RGBColerCodes.keySet()) {
//                            if (RGBColerCodes.get(string) == treeSet.last()) {
//                                tempStr = string;
//                            }
//                        }

//                        if (!RGBColerCodes.isEmpty()) {
//                            writableCellFormatForColorCode = new WritableCellFormat(writableFont);
//                            writableCellFormatForColorCode.setWrap(true);
//                            if (tempStr.equalsIgnoreCase("RED")) {
//                                if (RGBColerCodes.get("RED") >= 239 && RGBColerCodes.get("RED") < 244) {
//                                    writableWorkbook.setColourRGB(Colour.ORANGE, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                                    writableCellFormatForColorCode.setBackground(Colour.ORANGE);
//                                } else if (RGBColerCodes.get("RED") > 244 && RGBColerCodes.get("RED") <= 248) {
//                                    writableWorkbook.setColourRGB(Colour.YELLOW, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                                    writableCellFormatForColorCode.setBackground(Colour.YELLOW);
//                                } else {
//                                    writableWorkbook.setColourRGB(Colour.RED, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                                    writableCellFormatForColorCode.setBackground(Colour.RED);
//                                }
//                            } else if (tempStr.equalsIgnoreCase("GREEN")) {
//                                writableWorkbook.setColourRGB(Colour.GREEN, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                                writableCellFormatForColorCode.setBackground(Colour.GREEN);
//                            } else {
//                                if (RGBColerCodes.get("BLUE") > 244 && RGBColerCodes.get("BLUE") <= 248) {
//                                    writableWorkbook.setColourRGB(Colour.VIOLET, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                                    writableCellFormatForColorCode.setBackground(Colour.VIOLET);
//                                } else if (RGBColerCodes.get("BLUE") >= 103 && RGBColerCodes.get("BLUE") < 244) {
//                                    writableWorkbook.setColourRGB(Colour.INDIGO, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                                    writableCellFormatForColorCode.setBackground(Colour.INDIGO);
//                                } else {
//                                    writableWorkbook.setColourRGB(Colour.BLUE, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                                    writableCellFormatForColorCode.setBackground(Colour.BLUE);
//                                }
//
//                            }
//
//
//                            //cell.setCellFormat(writableCellFormatForColorCode);
//                        }
                    } else if (!sortSequence.isEmpty()) {
                        //  cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(sortSequence.get(i), colName));
                        hhhhCell = hhhhRow.createCell(j + colNumber);
                        hhhhCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
//                        hhhhCell.setCellStyle(hhhhStyle);

                    } else {
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(i, colName));
                        hhhhCell = hhhhRow.createCell(j + colNumber);
                        hhhhCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
//                        hhhhCell.setCellStyle(hhhhStyle);

                    } //cell.setCellFormat(writableCellFormat);
                    //writableSheet.addCell(cell);
                    colName = null;
                    //  writableCellFormatForColorCode = null;
                }
            }
            k++;
        }

        if (gTotal.equalsIgnoreCase("on")) {
            this.generateSubTotalHtmlForExportReportXLSM(k);
        }

        // }
    }

    private void buildReportTableXLSX() throws Exception {
        boolean isCrossTabReport = false;
        if (container.getReportCollect().reportColViewbyValues != null && container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        int viewSeqCount = ret.getViewSequence().size();
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        hStyle = hWorkbook.createCellStyle();
        //added by Dinanath for color group
        hStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        hStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
        hStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        hStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        hStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        hStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        hStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        hStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        hStyle.setRightBorderColor((short) 31);
        hStyle.setLeftBorderColor((short) 31);
        hStyle.setTopBorderColor((short) 31);
        hStyle.setBottomBorderColor((short) 31);

        //endded by Dinanath for color group
        //hStyle.setAlignment(Alignment.CENTRE);
        int columnViewByCount = Integer.parseInt(container.getColumnViewByCount());
        hRow = hSheet.createRow(lineNumber);
        lineNumber++;
        for (int i = 0; i < columnCount; i++) {
            if (isCrossTabReport) {
                // label = new Label(i, lineNumber, displayLabels[i]);
                // hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
                // Added by Ram for Hide Measure Headings 15Feb2016
                if (container.isHideMsrHeading()) {
                    hCell = hRow.createCell(i + colNumber);
                    String str = displayLabels[i];
                    str = str.replace("[", "").replace("]", "");
                    String[] st = str.split(",");
                    hCell.setCellValue(st[0]);
                } else {
                    hCell = hRow.createCell(i + colNumber);
                    hCell.setCellValue(displayLabels[i]);
                } //Endded by Ram
            } else if (displayLabels[i].contains("Prev Month") && this.getAggregationForSubtotal(displayColumns[i]).contains("#T")) {
                // label = new Label(i, lineNumber, container.getMonthNameforTrailingFormula(i));
                //   hCell=hRow.createCell(i);
                //  hCell.setCellValue(container.getMonthNameforTrailingFormula(i));
                hCell = hRow.createCell(i + colNumber);
                hCell.setCellValue(displayLabels[i]);
            } else {
                //label = new Label(i, lineNumber, displayLabels[i]);
                //hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
                hCell = hRow.createCell(i + colNumber);
                hCell.setCellValue(displayLabels[i]);
            }
            hCell.setCellStyle(hStyle);
//           writableCellFormat = new WritableCellFormat(writableFont);
//           writableCellFormat.setBackground(Colour.GRAY_25);
//           writableCellFormat.setWrap(true);
//            writableCellFormat.setAlignment(Alignment.GENERAL);

            if (i >= container.getViewByCount()) {
                String alignment = null;
                if (isCrossTabReport) {
                    if (crosstabMeasureId.containsKey(displayColumns[i]) && container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString()) != null) {
                        alignment = container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString());
                    }
                } else if (container.getMeasureAlign(displayColumns[i]) != null) {
                    alignment = container.getMeasureAlign(displayColumns[i]).toString();
                }
//              if(alignment!=null){
//                if(alignment.equalsIgnoreCase("left"))
//                writableCellFormat.setAlignment(Alignment.LEFT);
//                else if(alignment.equalsIgnoreCase("center"))
//                writableCellFormat.setAlignment(Alignment.CENTRE);
//                else if(alignment.equalsIgnoreCase("right"))
//                writableCellFormat.setAlignment(Alignment.RIGHT);
//                else
//                  writableCellFormat.setAlignment(Alignment.GENERAL);
//            }
            }
            //label.setCellFormat(writableCellFormat);
            // writableSheet.addCell(label);

            //HeadingCellView = new CellView();
            //HeadingCellView.setSize(256 * 20);
            // writableSheet.setColumnView(i, HeadingCellView);
        }

//        if(container.getViewByCount()>1 && actualtoRow<65000){
//        DataFacade facade = new DataFacade(container);
//        facade.setCtxPath(container.getReportCollect().ctxPath);
//        TableBuilder tableBldr = new RowViewTableBuilder(facade);
//        if (fromRow == 0 && toRow == 0)
//            tableBldr.setFromAndToRow(0,container.getRetObj().getViewSequence().size());
//        else
//            tableBldr.setFromAndToRow(fromRow, toRow);
//        TableDisplay bodyHelper = null;
//        TableDisplay subTotalHelper = null;
//        bodyHelper = new ExportExcelTableBodyDisplay(tableBldr);
//        bodyHelper.setHeadlineflag("Export");
//        //bodyHelper.setWritableSheet(writableSheet);
//        bodyHelper.setHSheet(hSheet);
//       // bodyHelper.setWritableWorkbook(writableWorkbook);
//        bodyHelper.setHWorkbook(hWorkbook);
//        bodyHelper.setStartRow(lineNumber);
//        subTotalHelper=new ExportExcelTableSubTotalDisplay(tableBldr);
//        //subTotalHelper.setWritableSheet(writableSheet);
//        //subTotalHelper.setWritableWorkbook(writableWorkbook);
//          subTotalHelper.setHSheet(hSheet);
//          subTotalHelper.setHWorkbook(hWorkbook);
//          subTotalHelper.setStartRow(lineNumber);
//          bodyHelper.setNext(subTotalHelper);
//        StringBuilder tableHtml = new StringBuilder();
//        tableHtml.append(bodyHelper.generateOutputHTML());
//       }else{
        //writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
        //WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
        WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
        DataFacade facade1 = new DataFacade(container);
        PbReportTableBD reportTableBD = new PbReportTableBD();
        reportTableBD.searchDataSet(this.container);
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }
        int k = 0;
        XSSFCellStyle xStyle = null;
        ArrayList<String> al = null;
        for (int i = fromRow; i < toRow; i++) {
            if (i < actualtoRow) {
                hRow = hSheet.createRow(k + lineNumber);
                if (isCrossTabReport == true) {
                    al = new ArrayList<>();
                }
                for (int j = 0; j < columnCount; j++) {
                    String colName = displayColumns[j];
                    String discolName = displayLabels[j];
                    Colour BGColor = null;
                    //added by Dinanath for colorgroup xlsxandxlsm
                    Short numOfCellStyle = hWorkbook.getNumCellStyles();
                    BigDecimal subtotalval = null;
                    String bgColor = null;
                    int actualRow = facade1.getActualRow(i);
                    //for cross tab code added by Dinanath
                    if (isCrossTabReport == true) {
                        try {
                            al.add(colName);
                            facade1.setMeasures(al);
                        } catch (Exception e) {
                        }
                    }
                    Colour clr = null;
                    try {
                        bgColor = facade1.getColor(actualRow, colName, subtotalval);
                    } catch (Exception e) {
                        // logger.error("Exception: ",e);
                    }
                    if (bgColor != null && !bgColor.isEmpty()) {
                        //if(numOfCellStyle<4000){
                        hStyle = hWorkbook.createCellStyle();
                        hStyle.setFillBackgroundColor(new XSSFColor(Color.decode(bgColor)));
                        //Added by Ram 15June2015 for white font.
                        XSSFFont font = hWorkbook.createFont();
                        font.setColor(IndexedColors.WHITE.getIndex());
                        hStyle.setFont(font);
                        //end Ram code
                        hStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
                        hStyle.setFillForegroundColor(new XSSFColor(Color.decode(bgColor)));
                        hStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                        hStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                        //added by Dinanath for grey color
                        hStyle.setLeftBorderColor((short) 31);
                        hStyle.setRightBorderColor((short) 31);
                        hStyle.setTopBorderColor((short) 31);
                        hStyle.setBottomBorderColor((short) 31);
                        //}
                    } else {
                        hStyle = hWorkbook.createCellStyle();
                        hStyle.setFillBackgroundColor(new XSSFColor(Color.decode("#FFFFFF")));
                        hStyle.setFillPattern(XSSFCellStyle.NO_FILL);
                        hStyle.setFillForegroundColor(new XSSFColor(Color.decode("#FFFFFF")));
                        hStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                        hStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                        hStyle.setLeftBorderColor((short) 31);
                        hStyle.setRightBorderColor((short) 31);
                        hStyle.setTopBorderColor((short) 31);
                        hStyle.setBottomBorderColor((short) 31);
                    }
//endded by Dinanath

                    if (j >= container.getViewByCount()) {
                        String alignment = null;
                        if (isCrossTabReport) {
                            if (crosstabMeasureId.containsKey(colName) && container.getTextAlign(crosstabMeasureId.get(colName).toString()) != null) {
                                alignment = container.getTextAlign(crosstabMeasureId.get(colName).toString());
                            }
                        } else if (container.getTextAlign(colName) != null) {
                            alignment = container.getTextAlign(colName);
                        }
                        if (alignment != null && !alignment.equalsIgnoreCase("")) {
                            if (alignment.equalsIgnoreCase("left")) {
                                //                writableCellFormat.setAlignment(Alignment.LEFT);
                                //                cf2.setAlignment(Alignment.LEFT);
                            } else if (alignment.equalsIgnoreCase("center")) {
                                //                writableCellFormat.setAlignment(Alignment.CENTRE);
                                //                cf2.setAlignment(Alignment.CENTRE);
                            } else if (alignment.equalsIgnoreCase("right")) {
                                //              writableCellFormat.setAlignment(Alignment.RIGHT);
                                //               cf2.setAlignment(Alignment.RIGHT);
                            } else {
                                //               writableCellFormat.setAlignment(Alignment.GENERAL);
                                //                cf2.setAlignment(Alignment.GENERAL);  }
                            }
                        }
                    }

                    if ("D".equals(types[j])) {
                        hCell = hRow.createCell(j + colNumber);
                        hCell.setCellValue(ret.getFieldValueDateString(sortSequence.get(i), colName));
                        hCell.setCellStyle(hStyle);//added by Dinanath
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueDateString(sortSequence.get(i), colName));
                        //cell.setCellFormat(writableCellFormat);
                    } else if ("N".equals(types[j]) && !container.getReportCollect().reportRowViewbyValues.contains(colName.replace("A_", ""))) {
                        if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                            String formattedData = "";
                            String snbrSymbol = "";
                            String nbrSymbol = "";
                            if (ret.getFieldValueBigDecimal(sortSequence.get(i), colName) != null) {
                                //String element = container.getDisplayColumns().get(j);
                                String element = displayColumns[j];
                                HashMap NFMap = new HashMap();
                                NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                                if (NFMap != null) {
                                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                        if (crosstabMeasureId.containsKey(element) && NFMap.get(crosstabMeasureId.get(element)) != null) {
                                            nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                        }
                                    } else if (NFMap.get(element) != null) {
                                        nbrSymbol = String.valueOf(NFMap.get(element));
                                    }
                                }
                                // Code Added by Amar to add snbrSymbol
                                HashMap colProps = container.getColumnProperties();
                                ArrayList propList;
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                    propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                                } else {
                                    propList = (ArrayList) colProps.get(element);
                                }
                                if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                                    String colSymbol = "";
                                    if (propList != null && propList.get(7) != null) {
                                        colSymbol = (String) propList.get(7);
                                    }
                                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                                        container.symbol.put(element, colSymbol);
                                    }
                                }
// end of code
                                snbrSymbol = container.symbol.get(displayColumns[j]);
                                int precision = container.getRoundPrecisionForMeasure(element);
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(element)) {
                                    precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                                }
                                if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                    formattedData = ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString();
                                } else if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    formattedData = this.convertDataToTime(new BigDecimal(ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString()));
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                    // formattedData = snbrSymbol+""+formattedData;
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    if (snbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = formattedData + "" + snbrSymbol;
                                    } else {
                                        formattedData = snbrSymbol + "" + formattedData;
                                    }
                                } else {
                                    formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
//                          HashMap colProps = container.getColumnProperties();
//                          if (colProps != null && colProps.containsKey(element)) {
//                          ArrayList propList = (ArrayList) colProps.get(element);
//                           String colSymbol = (String) propList.get(7);
//                           if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) {
//                            if (colSymbol.equalsIgnoreCase("%")) {
//                              formattedData = formattedData + colSymbol;
//                           } else {
//                           formattedData = colSymbol + formattedData;
//                          }
//                          }
//                        }
                                }
                            }
                            if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                            } else if (!sortSequence.isEmpty()) {
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hCell = hRow.createCell(j + colNumber);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hStyle);
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hCell = hRow.createCell(j + colNumber);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hStyle);
                                } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                    //cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                    hCell = hRow.createCell(j + colNumber);
                                    hCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
                                    hCell.setCellStyle(hStyle);
                                } else {
                                    //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                    hCell = hRow.createCell(j + colNumber);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hStyle);
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                hCell = hRow.createCell(j + colNumber);
                                hCell.setCellValue(formattedData);
                                hCell.setCellStyle(hStyle);
                            } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                // cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                hCell = hRow.createCell(j + colNumber);
                                hCell.setCellValue(formattedData);
                                hCell.setCellStyle(hStyle);
                            } else {
                                //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                hCell = hRow.createCell(j + colNumber);
                                hCell.setCellValue(formattedData);
                                hCell.setCellStyle(hStyle);
                            }
                        } else if (!sortSequence.isEmpty()) {
//    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(sortSequence.get(i),colName).doubleValue(),cf2);
                            hCell = hRow.createCell(j + colNumber);
                            hCell.setCellValue(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName).doubleValue());
                            hCell.setCellStyle(hStyle);
                        } else {
                            //    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(i,colName).doubleValue(),cf2);
                            hCell = hRow.createCell(j + colNumber);
                            hCell.setCellValue(ret.getFieldValueRuntimeMeasure(i, colName).doubleValue());
                            hCell.setCellStyle(hStyle);
                        }
                        HashMap<String, Integer> RGBColerCodes = null;
                        if (!sortSequence.isEmpty()) {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(sortSequence.get(i), colName));
                        } else {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(i, colName));
                        }
                        Collection<Integer> collection = RGBColerCodes.values();
                        TreeSet<Integer> treeSet = new TreeSet<Integer>();
                        for (Integer val : collection) {
                            treeSet.add(val);
                        }
                        String tempStr = "";
                        for (String string : RGBColerCodes.keySet()) {
                            if (RGBColerCodes.get(string) == treeSet.last()) {
                                tempStr = string;
                            }
                        }

                        if (!RGBColerCodes.isEmpty()) {
                            writableCellFormatForColorCode = new WritableCellFormat(writableFont);
                            writableCellFormatForColorCode.setWrap(true);
                            if (tempStr.equalsIgnoreCase("RED")) {
                                if (RGBColerCodes.get("RED") >= 239 && RGBColerCodes.get("RED") < 244) {
                                    writableWorkbook.setColourRGB(Colour.ORANGE, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.ORANGE);
                                } else if (RGBColerCodes.get("RED") > 244 && RGBColerCodes.get("RED") <= 248) {
                                    writableWorkbook.setColourRGB(Colour.YELLOW, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.YELLOW);
                                } else {
                                    writableWorkbook.setColourRGB(Colour.RED, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                    writableCellFormatForColorCode.setBackground(Colour.RED);
                                }
                            } else if (tempStr.equalsIgnoreCase("GREEN")) {
                                writableWorkbook.setColourRGB(Colour.GREEN, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                writableCellFormatForColorCode.setBackground(Colour.GREEN);
                            } else if (RGBColerCodes.get("BLUE") > 244 && RGBColerCodes.get("BLUE") <= 248) {
                                writableWorkbook.setColourRGB(Colour.VIOLET, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                writableCellFormatForColorCode.setBackground(Colour.VIOLET);
                            } else if (RGBColerCodes.get("BLUE") >= 103 && RGBColerCodes.get("BLUE") < 244) {
                                writableWorkbook.setColourRGB(Colour.INDIGO, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                writableCellFormatForColorCode.setBackground(Colour.INDIGO);
                            } else {
                                writableWorkbook.setColourRGB(Colour.BLUE, RGBColerCodes.get("RED"), RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
                                writableCellFormatForColorCode.setBackground(Colour.BLUE);
                            }

                            //cell.setCellFormat(writableCellFormatForColorCode);
                        }

                    } else if (!sortSequence.isEmpty()) {
                        //  cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(sortSequence.get(i), colName));
                        hCell = hRow.createCell(j + colNumber);
                        hCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
                        hCell.setCellStyle(hStyle);

                    } else {
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(i, colName));
                        hCell = hRow.createCell(j + colNumber);
                        hCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
                        hCell.setCellStyle(hStyle);

                    } //cell.setCellFormat(writableCellFormat);
                    //writableSheet.addCell(cell);
                    colName = null;
                    writableCellFormatForColorCode = null;
                }
            }
            k++;
        }

        if (gTotal.equalsIgnoreCase("on") && viewSeqCount > 0) {
            this.generateSubTotalHtmlForExportReportXLSX(k);
        }

        // }
    }

    //Added by Amar to generate report table excel for scheduled export report
    private void buildExportReportTableXLS() throws Exception {
        boolean isCrossTabReport = false;
        if (container.getReportCollect().reportColViewbyValues != null && container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        int columnViewByCount = Integer.parseInt(container.getColumnViewByCount());
        //hRow= hSheet.createRow(lineNumber);
        for (int i = 0; i < columnCount; i++) {
            if (isCrossTabReport) {
                // label = new Label(i, lineNumber, displayLabels[i]);
                // hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
            } else if (displayLabels[i].contains("Prev Month") && this.getAggregationForSubtotal(displayColumns[i]).contains("#T")) {
                // label = new Label(i, lineNumber, container.getMonthNameforTrailingFormula(i));
                //   hCell=hRow.createCell(i);
                //  hCell.setCellValue(container.getMonthNameforTrailingFormula(i));
            } else {
                //label = new Label(i, lineNumber, displayLabels[i]);
                //hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
            }
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);
            writableCellFormat.setAlignment(Alignment.GENERAL);
            if (i >= container.getViewByCount()) {
                String alignment = null;
                if (isCrossTabReport) {
                    if (crosstabMeasureId.containsKey(displayColumns[i]) && container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString()) != null) {
                        alignment = container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString());
                    }
                } else if (container.getMeasureAlign(displayColumns[i]) != null) {
                    alignment = container.getMeasureAlign(displayColumns[i]).toString();
                }
                if (alignment != null) {
                    if (alignment.equalsIgnoreCase("left")) {
                        writableCellFormat.setAlignment(Alignment.LEFT);
                    } else if (alignment.equalsIgnoreCase("center")) {
                        writableCellFormat.setAlignment(Alignment.CENTRE);
                    } else if (alignment.equalsIgnoreCase("right")) {
                        writableCellFormat.setAlignment(Alignment.RIGHT);
                    } else {
                        writableCellFormat.setAlignment(Alignment.GENERAL);
                    }
                }
            }
            //label.setCellFormat(writableCellFormat);
            // writableSheet.addCell(label);

            //HeadingCellView = new CellView();
            //HeadingCellView.setSize(256 * 20);
            // writableSheet.setColumnView(i, HeadingCellView);
        }

//        if(container.getViewByCount()>2 && actualtoRow<65000){
//        DataFacade facade = new DataFacade(container);
//        facade.setCtxPath(container.getReportCollect().ctxPath);
//        TableBuilder tableBldr = new RowViewTableBuilder(facade);
//        if (fromRow == 0 && toRow == 0)
//            tableBldr.setFromAndToRow(0,container.getRetObj().getViewSequence().size());
//        else
//            tableBldr.setFromAndToRow(fromRow, toRow);
//        TableDisplay bodyHelper = null;
//        TableDisplay subTotalHelper = null;
//        bodyHelper = new ExportExcelTableBodyDisplay(tableBldr);
//        bodyHelper.setHeadlineflag("Export");
//        //bodyHelper.setWritableSheet(writableSheet);
//        bodyHelper.setHSheet(hSheet);
//       // bodyHelper.setWritableWorkbook(writableWorkbook);
//        bodyHelper.setHWorkbook(hWorkbook);
//        bodyHelper.setStartRow(lineNumber);
//        subTotalHelper=new ExportExcelTableSubTotalDisplay(tableBldr);
//        //subTotalHelper.setWritableSheet(writableSheet);
//        //subTotalHelper.setWritableWorkbook(writableWorkbook);
//          subTotalHelper.setHSheet(hSheet);
//          subTotalHelper.setHWorkbook(hWorkbook);
//          subTotalHelper.setStartRow(lineNumber);
//          bodyHelper.setNext(subTotalHelper);
//        StringBuilder tableHtml = new StringBuilder();
//        tableHtml.append(bodyHelper.generateOutputHTML());
//       }else{
        //writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
        //WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_FLOAT);
        DataFacade facade1 = new DataFacade(container);
        PbReportTableBD reportTableBD = new PbReportTableBD();
        reportTableBD.searchDataSet(this.container);
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }
        int k = 0;
        hhStyle = hhWorkbook.createCellStyle();
        long incr = 0;
        ArrayList<String> al = null;
        for (int i = fromRow; i < toRow; i++) {
            if (i < actualtoRow) {
                hhRow = hhSheet.createRow(k + lineNumber);
                if (isCrossTabReport == true) {
                    al = new ArrayList<>();
                }
                for (int j = 0; j < columnCount; j++) {
                    incr++;

                    String colName = displayColumns[j];
                    String discolName = displayLabels[j];
                    Colour BGColor = null;

                    //added by Dinanath for color group jjjj
                    Short numOfCellStyle = hhWorkbook.getNumCellStyles();
                    //System.out.println("Num of cellStyle in if : "+numOfCellStyle);
                    BigDecimal subtotalval = null;
                    String bgColor = null;
                    int actualRow = facade1.getActualRow(i);
                    //for cross tab code added by Dinanath
                    if (isCrossTabReport == true) {
                        try {
                            al.add(colName);
                            facade1.setMeasures(al);
                        } catch (Exception e) {
                        }
                    }
                    Colour clr = null;
                    try {
                        bgColor = facade1.getColor(actualRow, colName, subtotalval);
                    } catch (Exception e) {
                        logger.error("Exception: ", e);
                    }
                    if (bgColor != null && !bgColor.isEmpty()) {
                        if (numOfCellStyle < 4000) {
                            hhStyle = hhWorkbook.createCellStyle();
                            int rgbRed = 255, rgbGreen = 0, rgbBlue = 0;
                            try {
                                int rgb[] = getRGB(bgColor.replace("#", ""));
                                rgbRed = rgb[0];
                                rgbGreen = rgb[1];
                                rgbBlue = rgb[2];
                            } catch (Exception e) {
                                logger.error("Exception: ", e);
                            }
                            //int rgb = new Color(Color.decode(bgColor).getRGB()).getRGB();
                            //hhStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
                            // Short num = hhWorkbook.getNumCellStyles();
                            HSSFPalette palette1 = hhWorkbook.getCustomPalette();
                            HSSFColor myColor = palette1.findSimilarColor(rgbRed, rgbGreen, rgbBlue);
                            short palIndex = myColor.getIndex();
                            HSSFColor borderIndexColorObj = palette1.findSimilarColor(224, 224, 224);
                            short borderIndexColor = borderIndexColorObj.getIndex();
                            hhStyle.setFillBackgroundColor(palIndex);
                            //Added by Ram 15June2015 for white font.
                            HSSFFont font = hhWorkbook.createFont();
                            font.setColor(HSSFColor.WHITE.index);
                            hhStyle.setFont(font);
                            //end Ram code
                            hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
                            hhStyle.setFillForegroundColor(palIndex);
                            hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                            hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setLeftBorderColor(borderIndexColor);
                            hhStyle.setRightBorderColor(borderIndexColor);
                            hhStyle.setTopBorderColor(borderIndexColor);
                            hhStyle.setBottomBorderColor(borderIndexColor);
                            //hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                            //hhStyle.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);
                        } else {
                            HSSFPalette palette = hhWorkbook.getCustomPalette();
                            HSSFColor myColor = palette.findSimilarColor(255, 255, 255);
                            short palIndex = myColor.getIndex();
                            HSSFColor borderIndexColorObj = palette.findSimilarColor(224, 224, 224);
                            short borderIndexColor = borderIndexColorObj.getIndex();
                            hhStyle.setFillBackgroundColor(palIndex);
                            hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
                            hhStyle.setFillForegroundColor(palIndex);
                            hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                            hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                            hhStyle.setLeftBorderColor(borderIndexColor);
                            hhStyle.setRightBorderColor(borderIndexColor);
                            hhStyle.setTopBorderColor(borderIndexColor);
                            hhStyle.setBottomBorderColor(borderIndexColor);
                        }
                    } else if (numOfCellStyle < 4000) {
                        hhStyle = hhWorkbook.createCellStyle();
                        HSSFPalette palette = hhWorkbook.getCustomPalette();
                        HSSFColor myColor = palette.findSimilarColor(255, 255, 255);
                        short palIndex = myColor.getIndex();
                        HSSFColor borderIndexColorObj = palette.findSimilarColor(224, 224, 224);
                        short borderIndexColor = borderIndexColorObj.getIndex();
                        hhStyle.setFillBackgroundColor(palIndex);
                        hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
                        hhStyle.setFillForegroundColor(palIndex);
                        hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setLeftBorderColor(borderIndexColor);
                        hhStyle.setRightBorderColor(borderIndexColor);
                        hhStyle.setTopBorderColor(borderIndexColor);
                        hhStyle.setBottomBorderColor(borderIndexColor);
                    } else {
                        HSSFPalette palette = hhWorkbook.getCustomPalette();
                        HSSFColor myColor = palette.findSimilarColor(255, 255, 255);
                        short palIndex = myColor.getIndex();
                        HSSFColor borderIndexColorObj = palette.findSimilarColor(224, 224, 224);
                        short borderIndexColor = borderIndexColorObj.getIndex();
                        hhStyle.setFillBackgroundColor(palIndex);
                        hhStyle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
                        hhStyle.setFillForegroundColor(palIndex);
                        hhStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        hhStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                        hhStyle.setLeftBorderColor(borderIndexColor);
                        hhStyle.setRightBorderColor(borderIndexColor);
                        hhStyle.setTopBorderColor(borderIndexColor);
                        hhStyle.setBottomBorderColor(borderIndexColor);
                    }
//endded by Dinanath

                    if (j >= container.getViewByCount()) {
                        String alignment = null;
                        if (isCrossTabReport) {
                            if (crosstabMeasureId.containsKey(colName) && container.getTextAlign(crosstabMeasureId.get(colName).toString()) != null) {
                                alignment = container.getTextAlign(crosstabMeasureId.get(colName).toString());
                            }
                        } else if (container.getTextAlign(colName) != null) {
                            alignment = container.getTextAlign(colName);
                        }
                        if (alignment != null && !alignment.equalsIgnoreCase("")) {
                            if (alignment.equalsIgnoreCase("left")) {
                                //                writableCellFormat.setAlignment(Alignment.LEFT);
                                //                cf2.setAlignment(Alignment.LEFT);
                            } else if (alignment.equalsIgnoreCase("center")) {
                                //                writableCellFormat.setAlignment(Alignment.CENTRE);
                                //                cf2.setAlignment(Alignment.CENTRE);
                            } else if (alignment.equalsIgnoreCase("right")) {
                                //              writableCellFormat.setAlignment(Alignment.RIGHT);
                                //               cf2.setAlignment(Alignment.RIGHT);
                            } else {
                                //               writableCellFormat.setAlignment(Alignment.GENERAL);
                                //                cf2.setAlignment(Alignment.GENERAL);  }
                            }
                        }
                    }

                    if ("D".equals(types[j])) {
                        hhCell = hhRow.createCell(j + colNumber);
                        hhCell.setCellValue(ret.getFieldValueDateString(sortSequence.get(i), colName));
                        hhCell.setCellStyle(hhStyle);//added by Dinanath
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueDateString(sortSequence.get(i), colName));
                        //cell.setCellFormat(writableCellFormat);
                    } else if ("N".equals(types[j]) && !container.getReportCollect().reportRowViewbyValues.contains(colName.replace("A_", ""))) {
                        if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                            String formattedData = "";
                            String snbrSymbol = "";
                            String nbrSymbol = "";
                            if (ret.getFieldValueBigDecimal(sortSequence.get(i), colName) != null) {
                                //String element = container.getDisplayColumns().get(j);
                                String element = displayColumns[j];
                                HashMap NFMap = new HashMap();
                                NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                                if (NFMap != null) {
                                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                        if (crosstabMeasureId.containsKey(element) && NFMap.get(crosstabMeasureId.get(element)) != null) {
                                            nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                        }
                                    } else if (NFMap.get(element) != null) {
                                        nbrSymbol = String.valueOf(NFMap.get(element));
                                    }
                                }
                                // Code Added by Amar to add snbrSymbol
                                HashMap colProps = container.getColumnProperties();
                                ArrayList propList;
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                    propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                                } else {
                                    propList = (ArrayList) colProps.get(element);
                                }
                                if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                                    String colSymbol = "";
                                    if (propList != null && propList.get(7) != null) {
                                        colSymbol = (String) propList.get(7);
                                    }
                                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                                        container.symbol.put(element, colSymbol);
                                    }
                                }
// end of code
                                snbrSymbol = container.symbol.get(displayColumns[j]);
                                int precision = container.getRoundPrecisionForMeasure(element);
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(element)) {
                                    precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                                }
                                if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                    formattedData = ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString();
                                } else if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    formattedData = this.convertDataToTime(new BigDecimal(ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString()));
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                    // formattedData = snbrSymbol+""+formattedData;
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    if (snbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = formattedData + "" + snbrSymbol;
                                    } else {
                                        formattedData = snbrSymbol + "" + formattedData;
                                    }
                                } else {
                                    formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
//                          HashMap colProps = container.getColumnProperties();
//                          if (colProps != null && colProps.containsKey(element)) {
//                          ArrayList propList = (ArrayList) colProps.get(element);
//                           String colSymbol = (String) propList.get(7);
//                           if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) {
//                            if (colSymbol.equalsIgnoreCase("%")) {
//                              formattedData = formattedData + colSymbol;
//                           } else {
//                           formattedData = colSymbol + formattedData;
//                          }
//                          }
//                        }
                                }
                            }
                            if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                            } else if (!sortSequence.isEmpty()) {
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(formattedData);
                                    hhCell.setCellStyle(hhStyle);//added by Dinanath
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(formattedData);
                                    hhCell.setCellStyle(hhStyle);//added by Dinanath
                                } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                    //cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
                                    hhCell.setCellStyle(hhStyle);//added by Dinanath
                                } else {
                                    //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(formattedData);
                                    hhCell.setCellStyle(hhStyle);//added by Dinanath
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(formattedData);
                                hhCell.setCellStyle(hhStyle);//added by Dinanath
                            } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                // cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(formattedData);
                                hhCell.setCellStyle(hhStyle);//added by Dinanath
                            } else {
                                //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(formattedData);
                                hhCell.setCellStyle(hhStyle);//added by Dinanath
                            }
                        } else {
                            //changed by sruthi % wise for excel
                            String Symbol = "";
                            Symbol = container.getNumberSymbol(displayColumns[j]);
                            String formattedData = "";
                            String elementid = displayColumns[j];
                            int precision1 = container.getRoundPrecisionForMeasure(elementid);
                            if (!sortSequence.isEmpty()) {
//    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(sortSequence.get(i),colName).doubleValue(),cf2);
                                if (Symbol != null && Symbol.equalsIgnoreCase("%") && !Symbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName), Symbol, precision1);
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(formattedData);
                                    hhCell.setCellStyle(hhStyle);
                                } else {
                                    hhCell = hhRow.createCell(j + colNumber);
                                    hhCell.setCellValue(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName).doubleValue());
                                    hhCell.setCellStyle(hhStyle);//added by Dinanath
                                }
                            } else //    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(i,colName).doubleValue(),cf2);
                            if (Symbol != null && Symbol.equalsIgnoreCase("%") && !Symbol.equalsIgnoreCase("")) {
                                formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueRuntimeMeasure(i, colName), Symbol, precision1);
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(formattedData);
                                hhCell.setCellStyle(hhStyle);
                            } else {
                                hhCell = hhRow.createCell(j + colNumber);
                                hhCell.setCellValue(ret.getFieldValueRuntimeMeasure(i, colName).doubleValue());
                                hhCell.setCellStyle(hhStyle);//added by Dinanath
                            }
                        }
                        //ended by sruthi
                        HashMap<String, Integer> RGBColerCodes = null;
                        if (!sortSequence.isEmpty()) {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(sortSequence.get(i), colName));
                        } else {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(i, colName));
                        }
                        Collection<Integer> collection = RGBColerCodes.values();
                        TreeSet<Integer> treeSet = new TreeSet<Integer>();
                        for (Integer val : collection) {
                            treeSet.add(val);
                        }
                        String tempStr = "";
                        for (String string : RGBColerCodes.keySet()) {
                            if (RGBColerCodes.get(string) == treeSet.last()) {
                                tempStr = string;
                            }
                        }

                    } else if (!sortSequence.isEmpty()) {
                        //  cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(sortSequence.get(i), colName));
                        hhCell = hhRow.createCell(j + colNumber);
                        hhCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
                        hhCell.setCellStyle(hhStyle);//added by Dinanath
                    } else {
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(i, colName));
                        hhCell = hhRow.createCell(j + colNumber);
                        hhCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
                        hhCell.setCellStyle(hhStyle);//added by Dinanath
                    } //cell.setCellFormat(writableCellFormat);
                    //writableSheet.addCell(cell);
                    colName = null;
                    writableCellFormatForColorCode = null;
                }
            }
            k++;
        }

        if (gTotal.equalsIgnoreCase("on")) {
            this.generateSubTotalHtmlForExportReportXLS(k);
        }

        //}
    }

    private void buildExportReportTableXLSX() throws Exception {
        boolean isCrossTabReport = false;
        if (container.getReportCollect().reportColViewbyValues != null && container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        int columnViewByCount = Integer.parseInt(container.getColumnViewByCount());
        int viewSeqCount = ret.getViewSequence().size();
        //hRow= hSheet.createRow(lineNumber);
        for (int i = 0; i < columnCount; i++) {
            if (isCrossTabReport) {
                // label = new Label(i, lineNumber, displayLabels[i]);
                // hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
            } else if (displayLabels[i].contains("Prev Month") && this.getAggregationForSubtotal(displayColumns[i]).contains("#T")) {
                // label = new Label(i, lineNumber, container.getMonthNameforTrailingFormula(i));
                //   hCell=hRow.createCell(i);
                //  hCell.setCellValue(container.getMonthNameforTrailingFormula(i));
            } else {
                //label = new Label(i, lineNumber, displayLabels[i]);
                //hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
            }
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);
            writableCellFormat.setAlignment(Alignment.GENERAL);
            if (i >= container.getViewByCount()) {
                String alignment = null;
                if (isCrossTabReport) {
                    if (crosstabMeasureId.containsKey(displayColumns[i]) && container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString()) != null) {
                        alignment = container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString());
                    }
                } else if (container.getMeasureAlign(displayColumns[i]) != null) {
                    alignment = container.getMeasureAlign(displayColumns[i]).toString();
                }
                if (alignment != null) {
                    if (alignment.equalsIgnoreCase("left")) {
                        writableCellFormat.setAlignment(Alignment.LEFT);
                    } else if (alignment.equalsIgnoreCase("center")) {
                        writableCellFormat.setAlignment(Alignment.CENTRE);
                    } else if (alignment.equalsIgnoreCase("right")) {
                        writableCellFormat.setAlignment(Alignment.RIGHT);
                    } else {
                        writableCellFormat.setAlignment(Alignment.GENERAL);
                    }
                }
            }

        }
        PbReportTableBD reportTableBD = new PbReportTableBD();
        reportTableBD.searchDataSet(this.container);
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }
        int k = 0;
        DataFacade facade1 = new DataFacade(container);
        ArrayList<String> al = null;
        for (int i = fromRow; i < toRow; i++) {
            if (i < actualtoRow) {
                hRow = hSheet.createRow(k + lineNumber);
                if (isCrossTabReport == true) {
                    al = new ArrayList<>();
                }
                for (int j = 0; j < columnCount; j++) {
                    String colName = displayColumns[j];
                    String discolName = displayLabels[j];
                    Colour BGColor = null;
                    //added by Dinanath for colorgroup xlsx
                    BigDecimal subtotalval = null;
                    String bgColor = null;
                    int actualRow = facade1.getActualRow(i);
                    //for cross tab code added by Dinanath
                    if (isCrossTabReport == true) {
                        try {
                            al.add(colName);
                            facade1.setMeasures(al);
                        } catch (Exception e) {
                        }
                    }
                    Colour clr = null;
                    try {
                        bgColor = facade1.getColor(actualRow, colName, subtotalval);
                    } catch (Exception e) {
                        logger.error("Exception: ", e);
                    }
                    if (bgColor != null && !bgColor.isEmpty()) {
                        hStyle = hWorkbook.createCellStyle();
                        hStyle.setFillBackgroundColor(new XSSFColor(Color.decode(bgColor)));
                        //Added by Ram 15June2015 for white font.
                        XSSFFont font = hWorkbook.createFont();
                        font.setColor(IndexedColors.WHITE.getIndex());
                        hStyle.setFont(font);
                        //end Ram code
                        hStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
                        hStyle.setFillForegroundColor(new XSSFColor(Color.decode(bgColor)));
                        hStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                        hStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
                        hStyle.setRightBorderColor((short) 31);
                        hStyle.setLeftBorderColor((short) 31);
                        hStyle.setTopBorderColor((short) 31);
                        hStyle.setBottomBorderColor((short) 31);
                    } else {
                        hStyle = hWorkbook.createCellStyle();
                        hStyle.setFillBackgroundColor(new XSSFColor(Color.decode("#FFFFFF")));
                        hStyle.setFillPattern(XSSFCellStyle.NO_FILL);
                        hStyle.setFillForegroundColor(new XSSFColor(Color.decode("#FFFFFF")));
                        hStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                        hStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                        hStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
                        hStyle.setRightBorderColor((short) 31);
                        hStyle.setLeftBorderColor((short) 31);
                        hStyle.setTopBorderColor((short) 31);
                        hStyle.setBottomBorderColor((short) 31);
                    }
//endded by Dinanath

                    if (j >= container.getViewByCount()) {
                        String alignment = null;
                        if (isCrossTabReport) {
                            if (crosstabMeasureId.containsKey(colName) && container.getTextAlign(crosstabMeasureId.get(colName).toString()) != null) {
                                alignment = container.getTextAlign(crosstabMeasureId.get(colName).toString());
                            }
                        } else if (container.getTextAlign(colName) != null) {
                            alignment = container.getTextAlign(colName);
                        }
                        if (alignment != null && !alignment.equalsIgnoreCase("")) {
                            if (alignment.equalsIgnoreCase("left")) {
                                //                writableCellFormat.setAlignment(Alignment.LEFT);
                                //                cf2.setAlignment(Alignment.LEFT);
                            } else if (alignment.equalsIgnoreCase("center")) {
                                //                writableCellFormat.setAlignment(Alignment.CENTRE);
                                //                cf2.setAlignment(Alignment.CENTRE);
                            } else if (alignment.equalsIgnoreCase("right")) {
                                //              writableCellFormat.setAlignment(Alignment.RIGHT);
                                //               cf2.setAlignment(Alignment.RIGHT);
                            } else {
                                //               writableCellFormat.setAlignment(Alignment.GENERAL);
                                //                cf2.setAlignment(Alignment.GENERAL);  }
                            }
                        }
                    }

                    if ("D".equals(types[j])) {
                        hCell = hRow.createCell(j + colNumber);
                        hCell.setCellValue(ret.getFieldValueDateString(sortSequence.get(i), colName));
                        hCell.setCellStyle(hStyle);
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueDateString(sortSequence.get(i), colName));
                        //cell.setCellFormat(writableCellFormat);
                    } else if ("N".equals(types[j]) && !container.getReportCollect().reportRowViewbyValues.contains(colName.replace("A_", ""))) {
                        if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                            String formattedData = "";
                            String snbrSymbol = "";
                            String nbrSymbol = "";
                            if (ret.getFieldValueBigDecimal(sortSequence.get(i), colName) != null) {
                                //String element = container.getDisplayColumns().get(j);
                                String element = displayColumns[j];
                                HashMap NFMap = new HashMap();
                                NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                                if (NFMap != null) {
                                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                        if (crosstabMeasureId.containsKey(element) && NFMap.get(crosstabMeasureId.get(element)) != null) {
                                            nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                        }
                                    } else if (NFMap.get(element) != null) {
                                        nbrSymbol = String.valueOf(NFMap.get(element));
                                    }
                                }
                                // Code Added by Amar to add snbrSymbol
                                HashMap colProps = container.getColumnProperties();
                                ArrayList propList;
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                    propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                                } else {
                                    propList = (ArrayList) colProps.get(element);
                                }
                                if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                                    String colSymbol = "";
                                    if (propList != null && propList.get(7) != null) {
                                        colSymbol = (String) propList.get(7);
                                    }
                                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                                        container.symbol.put(element, colSymbol);
                                    }
                                }
// end of code
                                snbrSymbol = container.symbol.get(displayColumns[j]);
                                int precision = container.getRoundPrecisionForMeasure(element);
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(element)) {
                                    precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                                }
                                if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                    formattedData = ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString();
                                } else if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    formattedData = this.convertDataToTime(new BigDecimal(ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString()));
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                    // formattedData = snbrSymbol+""+formattedData;
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    if (snbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = formattedData + "" + snbrSymbol;
                                    } else {
                                        formattedData = snbrSymbol + "" + formattedData;
                                    }
                                } else {
                                    formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
//                          HashMap colProps = container.getColumnProperties();
//                          if (colProps != null && colProps.containsKey(element)) {
//                          ArrayList propList = (ArrayList) colProps.get(element);
//                           String colSymbol = (String) propList.get(7);
//                           if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) {
//                            if (colSymbol.equalsIgnoreCase("%")) {
//                              formattedData = formattedData + colSymbol;
//                           } else {
//                           formattedData = colSymbol + formattedData;
//                          }
//                          }
//                        }
                                }
                            }
                            if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                            } else if (!sortSequence.isEmpty()) {
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hCell = hRow.createCell(j + colNumber);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hStyle);
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hCell = hRow.createCell(j + colNumber);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hStyle);
                                } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                    //cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                    hCell = hRow.createCell(j + colNumber);
                                    hCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
                                    hCell.setCellStyle(hStyle);
                                } else {
                                    //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                    hCell = hRow.createCell(j + colNumber);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hStyle);
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                hCell = hRow.createCell(j + colNumber);
                                hCell.setCellValue(formattedData);//
                                hCell.setCellStyle(hStyle);//added by Dinanath for colorgroup
                            } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                // cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                hCell = hRow.createCell(j + colNumber);
                                hCell.setCellValue(formattedData);
                                hCell.setCellStyle(hStyle);//added by Dinanath for colorgroup
                            } else {
                                //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                hCell = hRow.createCell(j + colNumber);
                                hCell.setCellValue(formattedData);
                                hCell.setCellStyle(hStyle);//added by Dinanath for colorgroup
                            }
                        } else if (!sortSequence.isEmpty()) {
//    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(sortSequence.get(i),colName).doubleValue(),cf2);
                            hCell = hRow.createCell(j + colNumber);
                            hCell.setCellValue(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName).doubleValue());
                            hCell.setCellStyle(hStyle);//added by Dinanath for colorgroup
                        } else {
                            //    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(i,colName).doubleValue(),cf2);
                            hCell = hRow.createCell(j + colNumber);
                            hCell.setCellValue(ret.getFieldValueRuntimeMeasure(i, colName).doubleValue());
                            hCell.setCellStyle(hStyle);//added by Dinanath for colorgroup
                        }
                        HashMap<String, Integer> RGBColerCodes = null;
                        if (!sortSequence.isEmpty()) {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(sortSequence.get(i), colName));
                        } else {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(i, colName));
                        }
                        Collection<Integer> collection = RGBColerCodes.values();
                        TreeSet<Integer> treeSet = new TreeSet<Integer>();
                        for (Integer val : collection) {
                            treeSet.add(val);
                        }
                        String tempStr = "";
                        for (String string : RGBColerCodes.keySet()) {
                            if (RGBColerCodes.get(string) == treeSet.last()) {
                                tempStr = string;
                            }
                        }
                    } else if (!sortSequence.isEmpty()) {
                        //  cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(sortSequence.get(i), colName));
                        hCell = hRow.createCell(j + colNumber);
                        hCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
                        hCell.setCellStyle(hStyle);//added by Dinanath for colorgroup
                    } else {
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(i, colName));
                        hCell = hRow.createCell(j + colNumber);
                        hCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
                        hCell.setCellStyle(hStyle);//added by Dinanath for colorgroup
                    } //cell.setCellFormat(writableCellFormat);
                    //writableSheet.addCell(cell);
                    colName = null;
                    writableCellFormatForColorCode = null;
                }
            }
            k++;
        }

        if (gTotal.equalsIgnoreCase("on") && viewSeqCount > 0) {
            this.generateSubTotalHtmlForExportReportXLSX(k);
        }

        //}
    }

    private void buildExportReportTableXLSM() throws Exception {
        boolean isCrossTabReport = false;
        if (container.getReportCollect().reportColViewbyValues != null && container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
//        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

        int columnViewByCount = Integer.parseInt(container.getColumnViewByCount());
        //hRow= hSheet.createRow(lineNumber);
        for (int i = 0; i < columnCount; i++) {
            if (isCrossTabReport) {
                // label = new Label(i, lineNumber, displayLabels[i]);
                // hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
            } else if (displayLabels[i].contains("Prev Month") && this.getAggregationForSubtotal(displayColumns[i]).contains("#T")) {
                // label = new Label(i, lineNumber, container.getMonthNameforTrailingFormula(i));
                //   hCell=hRow.createCell(i);
                //  hCell.setCellValue(container.getMonthNameforTrailingFormula(i));
            } else {
                //label = new Label(i, lineNumber, displayLabels[i]);
                //hCell=hRow.createCell(i);
                //hCell.setCellValue(displayLabels[i]);
            }
//           writableCellFormat = new WritableCellFormat(writableFont);
//           writableCellFormat.setBackground(Colour.GRAY_25);
//           writableCellFormat.setWrap(true);
//            writableCellFormat.setAlignment(Alignment.GENERAL);
            if (i >= container.getViewByCount()) {
                String alignment = null;
                if (isCrossTabReport) {
                    if (crosstabMeasureId.containsKey(displayColumns[i]) && container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString()) != null) {
                        alignment = container.getMeasureAlign(crosstabMeasureId.get(displayColumns[i]).toString());
                    }
                } else if (container.getMeasureAlign(displayColumns[i]) != null) {
                    alignment = container.getMeasureAlign(displayColumns[i]).toString();
                }
                if (alignment != null) {
                    if (alignment.equalsIgnoreCase("left")) {
                        writableCellFormat.setAlignment(Alignment.LEFT);
                    } else if (alignment.equalsIgnoreCase("center")) {
                        writableCellFormat.setAlignment(Alignment.CENTRE);
                    } else if (alignment.equalsIgnoreCase("right")) {
                        writableCellFormat.setAlignment(Alignment.RIGHT);
                    } else {
                        writableCellFormat.setAlignment(Alignment.GENERAL);
                    }
                }
            }

        }
        PbReportTableBD reportTableBD = new PbReportTableBD();
        reportTableBD.searchDataSet(this.container);
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }
        int k = 0;
//        System.out.println("line number: " + lineNumber);
        DataFacade facade1 = new DataFacade(container);
        for (int i = fromRow; i < toRow; i++) {
            if (i < actualtoRow) {
                hhhhRow = hhhhSheet.createRow(k + lineNumber);
                for (int j = 0; j < columnCount; j++) {
                    String colName = displayColumns[j];
                    String discolName = displayLabels[j];
                    Colour BGColor = null;
                    //added by Dinanath for colorgroup xlsxandxlsm
//                    BigDecimal subtotalval = null;
//                    String bgColor = null;
//                    int actualRow = facade1.getActualRow(i);
//                    Colour clr = null;
//                    try {
//                    //    bgColor = facade1.getColor(actualRow, colName, subtotalval);
//                    } catch (Exception e) {
//                        logger.error("Exception: ",e);
//                    }
//                    if (bgColor != null && !bgColor.isEmpty()) {
//                hhhhStyle =hhhhWorkbook.createCellStyle();
//                hhhhStyle.setFillBackgroundColor((short)11);
//                hhhhStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
//                hhhhStyle.setFillForegroundColor((short)11);
//                hhhhStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//                hhhhStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//                hhhhStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
//                hhhhStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//                hhhhStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
//                    } else {
//                   hhhhStyle =hhhhWorkbook.createCellStyle();
//                   hhhhStyle.setFillBackgroundColor((short)11);
//                   hhhhStyle.setFillPattern(XSSFCellStyle.NO_FILL);
//                   hhhhStyle.setFillForegroundColor((short)11);
//                   hhhhStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//                   hhhhStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//                   hhhhStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
//                   hhhhStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//                   hhhhStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
//                    }
//endded by Dinanath

                    if (j >= container.getViewByCount()) {
                        String alignment = null;
                        if (isCrossTabReport) {
                            if (crosstabMeasureId.containsKey(colName) && container.getTextAlign(crosstabMeasureId.get(colName).toString()) != null) {
                                alignment = container.getTextAlign(crosstabMeasureId.get(colName).toString());
                            }
                        } else if (container.getTextAlign(colName) != null) {
                            alignment = container.getTextAlign(colName);
                        }
                        if (alignment != null && !alignment.equalsIgnoreCase("")) {
                            if (alignment.equalsIgnoreCase("left")) {
                                //                writableCellFormat.setAlignment(Alignment.LEFT);
                                //                cf2.setAlignment(Alignment.LEFT);
                            } else if (alignment.equalsIgnoreCase("center")) {
                                //                writableCellFormat.setAlignment(Alignment.CENTRE);
                                //                cf2.setAlignment(Alignment.CENTRE);
                            } else if (alignment.equalsIgnoreCase("right")) {
                                //              writableCellFormat.setAlignment(Alignment.RIGHT);
                                //               cf2.setAlignment(Alignment.RIGHT);
                            } else {
                                //               writableCellFormat.setAlignment(Alignment.GENERAL);
                                //                cf2.setAlignment(Alignment.GENERAL);  }
                            }
                        }
                    }

                    if ("D".equals(types[j])) {
                        hhhhCell = hhhhRow.createCell(j + colNumber);
                        hhhhCell.setCellValue(ret.getFieldValueDateString(sortSequence.get(i), colName));
//                    hhhhCell.setCellStyle(hhhhStyle);
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueDateString(sortSequence.get(i), colName));
                        //cell.setCellFormat(writableCellFormat);
                    } else if ("N".equals(types[j]) && !container.getReportCollect().reportRowViewbyValues.contains(colName.replace("A_", ""))) {
                        if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                            String formattedData = "";
                            String snbrSymbol = "";
                            String nbrSymbol = "";
                            if (ret.getFieldValueBigDecimal(sortSequence.get(i), colName) != null) {
                                //String element = container.getDisplayColumns().get(j);
                                String element = displayColumns[j];
                                HashMap NFMap = new HashMap();
                                NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                                if (NFMap != null) {
                                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                        if (crosstabMeasureId.containsKey(element) && NFMap.get(crosstabMeasureId.get(element)) != null) {
                                            nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                        }
                                    } else if (NFMap.get(element) != null) {
                                        nbrSymbol = String.valueOf(NFMap.get(element));
                                    }
                                }
                                // Code Added by Amar to add snbrSymbol
                                HashMap colProps = container.getColumnProperties();
                                ArrayList propList;
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                    propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                                } else {
                                    propList = (ArrayList) colProps.get(element);
                                }
                                if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                                    String colSymbol = "";
                                    if (propList != null && propList.get(7) != null) {
                                        colSymbol = (String) propList.get(7);
                                    }
                                    if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                                        container.symbol.put(element, colSymbol);
                                    }
                                }
// end of code
                                snbrSymbol = container.symbol.get(displayColumns[j]);
                                int precision = container.getRoundPrecisionForMeasure(element);
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.containsKey(element)) {
                                    precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                                }
                                if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                    formattedData = ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString();
                                } else if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    formattedData = this.convertDataToTime(new BigDecimal(ret.getFieldValueBigDecimal(sortSequence.get(i), colName).toString()));
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                    // formattedData = snbrSymbol+""+formattedData;
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumberFormat(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
                                    if (snbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = formattedData + "" + snbrSymbol;
                                    } else {
                                        formattedData = snbrSymbol + "" + formattedData;
                                    }
                                } else {
                                    formattedData = NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i), colName), nbrSymbol, precision);
//                          HashMap colProps = container.getColumnProperties();
//                          if (colProps != null && colProps.containsKey(element)) {
//                          ArrayList propList = (ArrayList) colProps.get(element);
//                           String colSymbol = (String) propList.get(7);
//                           if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) {
//                            if (colSymbol.equalsIgnoreCase("%")) {
//                              formattedData = formattedData + colSymbol;
//                           } else {
//                           formattedData = colSymbol + formattedData;
//                          }
//                          }
//                        }
                                }
                            }
                            if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                            } else if (!sortSequence.isEmpty()) {
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hhhhCell = hhhhRow.createCell(j + colNumber);
                                    hhhhCell.setCellValue(formattedData);
//                                hhhhCell.setCellStyle(hhhhStyle);
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    //cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                    hhhhCell = hhhhRow.createCell(j + colNumber);
                                    hhhhCell.setCellValue(formattedData);
//                                hhhhCell.setCellStyle(hhhhStyle);
                                } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                    //cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                    hhhhCell = hhhhRow.createCell(j + colNumber);
                                    hhhhCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
//                                hhhhCell.setCellStyle(hhhhStyle);
                                } else {
                                    //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                    hhhhCell = hhhhRow.createCell(j + colNumber);
                                    hhhhCell.setCellValue(formattedData);
//                                hhhhCell.setCellStyle(hhhhStyle);
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                // cell = new Label(j, k + lineNumber + 1, formattedData,writableCellFormat);
                                hhhhCell = hhhhRow.createCell(j + colNumber);
                                hhhhCell.setCellValue(formattedData);//
//                               hhhhCell.setCellStyle(hhhhStyle);//added by Dinanath for colorgroup
                            } else if (formattedData != null && formattedData != "" && !formattedData.equalsIgnoreCase("")) {
                                // cell = new Number(j, k + lineNumber + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                hhhhCell = hhhhRow.createCell(j + colNumber);
                                hhhhCell.setCellValue(formattedData);
//                               hhhhCell.setCellStyle(hhhhStyle);//added by Dinanath for colorgroup
                            } else {
                                //cell = new Label(j, k + lineNumber + 1,formattedData,cf2);
                                hhhhCell = hhhhRow.createCell(j + colNumber);
                                hhhhCell.setCellValue(formattedData);
//                               hhhhCell.setCellStyle(hhhhStyle);//added by Dinanath for colorgroup
                            }
                        } else if (!sortSequence.isEmpty()) {
//    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(sortSequence.get(i),colName).doubleValue(),cf2);
                            hhhhCell = hhhhRow.createCell(j + colNumber);
                            hhhhCell.setCellValue(ret.getFieldValueRuntimeMeasure(sortSequence.get(i), colName).doubleValue());
//                            hhhhCell.setCellStyle(hhhhStyle);//added by Dinanath for colorgroup
                        } else {
                            //    cell = new Number(j, k + lineNumber + 1, ret.getFieldValueRuntimeMeasure(i,colName).doubleValue(),cf2);
                            hhhhCell = hhhhRow.createCell(j + colNumber);
                            hhhhCell.setCellValue(ret.getFieldValueRuntimeMeasure(i, colName).doubleValue());
//                            hhhhCell.setCellStyle(hhhhStyle);//added by Dinanath for colorgroup
                        }
                        HashMap<String, Integer> RGBColerCodes = null;
                        if (!sortSequence.isEmpty()) {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(sortSequence.get(i), colName));
                        } else {
                            RGBColerCodes = getColor(colName, ret.getFieldValueString(i, colName));
                        }
                        Collection<Integer> collection = RGBColerCodes.values();
                        TreeSet<Integer> treeSet = new TreeSet<Integer>();
                        for (Integer val : collection) {
                            treeSet.add(val);
                        }
                        String tempStr = "";
                        for (String string : RGBColerCodes.keySet()) {
                            if (RGBColerCodes.get(string) == treeSet.last()) {
                                tempStr = string;
                            }
                        }

//                    if (!RGBColerCodes.isEmpty()) {
//                        writableCellFormatForColorCode = new WritableCellFormat(writableFont);
//                        writableCellFormatForColorCode.setWrap(true);
//                        if(tempStr.equalsIgnoreCase("RED")){
//                            if(RGBColerCodes.get("RED")>=239 && RGBColerCodes.get("RED") < 244){
//                            writableWorkbook.setColourRGB(Colour.ORANGE, RGBColerCodes.get("RED"),RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                            writableCellFormatForColorCode.setBackground(Colour.ORANGE);
//                            }else if(RGBColerCodes.get("RED")>244 && RGBColerCodes.get("RED")<=248){
//                            writableWorkbook.setColourRGB(Colour.YELLOW, RGBColerCodes.get("RED"),RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                            writableCellFormatForColorCode.setBackground(Colour.YELLOW);
//                            }else{
//                            writableWorkbook.setColourRGB(Colour.RED, RGBColerCodes.get("RED"),RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                            writableCellFormatForColorCode.setBackground(Colour.RED);
//                            }
//                        } else if (tempStr.equalsIgnoreCase("GREEN")) {
//                            writableWorkbook.setColourRGB(Colour.GREEN, RGBColerCodes.get("RED"),RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                            writableCellFormatForColorCode.setBackground(Colour.GREEN);
//                        }else{
//                            if( RGBColerCodes.get("BLUE")>244 &&  RGBColerCodes.get("BLUE") <=248){
//                            writableWorkbook.setColourRGB(Colour.VIOLET, RGBColerCodes.get("RED"),RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                            writableCellFormatForColorCode.setBackground(Colour.VIOLET);
//                            }else if(RGBColerCodes.get("BLUE")>=103 && RGBColerCodes.get("BLUE")<244){
//                            writableWorkbook.setColourRGB(Colour.INDIGO, RGBColerCodes.get("RED"),RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                            writableCellFormatForColorCode.setBackground(Colour.INDIGO);
//                            }else{
//                            writableWorkbook.setColourRGB(Colour.BLUE, RGBColerCodes.get("RED"),RGBColerCodes.get("GREEN"), RGBColerCodes.get("BLUE"));
//                            writableCellFormatForColorCode.setBackground(Colour.BLUE);
//                            }
//
//                        }
//
//
//                        //cell.setCellFormat(writableCellFormatForColorCode);
//                    }
                    } else if (!sortSequence.isEmpty()) {
                        //  cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(sortSequence.get(i), colName));
                        hhhhCell = hhhhRow.createCell(j + colNumber);
                        hhhhCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
//                        hhhhCell.setCellStyle(hhhhStyle);//added by Dinanath for colorgroup
                    } else {
                        //cell = new Label(j, k + lineNumber + 1, ret.getFieldValueString(i, colName));
                        hhhhCell = hhhhRow.createCell(j + colNumber);
                        hhhhCell.setCellValue(ret.getFieldValueString(sortSequence.get(i), colName));
//                        hhhhCell.setCellStyle(hhhhStyle);//added by Dinanath for colorgroup
                    } //cell.setCellFormat(writableCellFormat);
                    //writableSheet.addCell(cell);
                    colName = null;
                    writableCellFormatForColorCode = null;
                }
            }
            k++;
        }

        if (gTotal.equalsIgnoreCase("on")) {
            this.generateSubTotalHtmlForExportReportXLSM(k);
        }

        //}
    }
    //added by Dinanath for xlsm

    public void generateSubTotalHtmlForExportReportXLSM(int k) {

        ArrayList displayValues = new ArrayList();
        ArrayList viewBys = new ArrayList();
        ArrayList viewBys1 = new ArrayList();
        displayValues = container.getDisplayColumns();
//        for(int i=0;i<displayColumns.length;i++){
//            displayValues.add(displayColumns[i]);
//        }
        viewBys1 = container.getDisplayTypes();
        for (int l = 0; l < types.length; l++) {
            viewBys.add(types[l]);
        }
        if (displayValues.size() > viewBys.size()) {
            for (int i = 0; i < displayValues.size(); i++) {
                if (i >= viewBys.size()) {
                    viewBys.add("N");
                }
            }
        }
        //started by Amar
        String aggType = "";
        String refferedElements = "";
        String userColType = "";
        String refElementType = "";
        String tempFormula = "";
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = null;
        DataFacade facade = new DataFacade(container);
        ArrayList<String> hidemeasurecount = facade.container.getReportCollect().getHideMeasures();
        ArrayList<Integer> index = new ArrayList<Integer>();
        // end by Amar
        // code added by Amar
        if (hidemeasurecount.size() > 0) {
            for (int i = 0; i < hidemeasurecount.size(); i++) {
                index.add(facade.container.getDisplayColumns().indexOf("A_" + hidemeasurecount.get(i)));
            }
        }
        // end of code
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }

        //  for (int i = 0; i < 2; i++) {
        int actualcol = 0;
        hhhhRow = hhhhSheet.createRow(k + lineNumber);
        for (int col = 0; col < displayValues.size(); col++) {
            if (!index.contains(col)) {
                if (viewBys.get(col).toString().equalsIgnoreCase("N")) {
//                 BigDecimal grandToatal=(BigDecimal)ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                    String formattedData = "";
                    String element = container.getDisplayColumns().get(col);
                    String nbrSymbol = "";
                    HashMap NFMap = new HashMap();
                    NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                    HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
                    if (NFMap != null) {
                        if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                            if (NFMap.get(crosstabMeasureId.get(element)) != null) {
                                nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                            }
                        } else if (NFMap.get(element) != null) {
                            nbrSymbol = String.valueOf(NFMap.get(element));
                        }
                    }
                    String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
                    int precision = container.getRoundPrecisionForMeasure(element);
                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.get(element) != null) {
                        precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                        element = crosstabMeasureId.get(element);
                    }

                    BigDecimal grandToatal;
                    // Modified by Amar
                    String eleId = element.replace("A_", "");
                    String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
                    try {
                        retobj = pbdb.execSelectSQL(qry);
                    } catch (SQLException ex) {
                        logger.error("Exception: ", ex);
                    }
                    if (retobj != null && retobj.getRowCount() > 0) {
                        tempFormula = retobj.getFieldValueString(0, 0);
                        refferedElements = retobj.getFieldValueString(0, 1);
                        userColType = retobj.getFieldValueString(0, 2);
                        refElementType = retobj.getFieldValueString(0, 3);
                        aggType = retobj.getFieldValueString(0, 4);
                        tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");

                    }

//                     if (i == 0) {
//                        if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
//                            //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                             if(aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")){
//                                 //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
//                                 grandToatal = ret.getColumnGrandTotalValue(facade.getColumnId(col));
//                             }else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
//                                grandToatal = BigDecimal.ZERO;
//                            } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
//                                grandToatal = BigDecimal.ZERO;
//                            } else {
//                                grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
//                            }
//                        } else {
//                            grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
//                        }
//                    //} else {
//                      //  grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
//                    //}
//                     if(aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")){
//                        int rowCnt = 0;
//                            rowCnt=facade.getRowCount();
//                             grandToatal=grandToatal.divide(new BigDecimal(rowCnt),RoundingMode.HALF_UP);
//                         }
                    // end of code bu Amar
                    if (!container.isReportCrosstab()) {
                        if (userColType.equalsIgnoreCase("TIMECALUCULATED") && (aggType.contains("avg") || aggType.contains("AVG"))) {
                            facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));

                        } else if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
                            if (!facade.container.getKpiQrycls().contains(facade.getColumnId(col).replace("A_", "").trim())) {

                                grandToatal = (BigDecimal) getGTForChangePerOfSummTabMeasures(eleId, col);

                            } else if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
                                //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
                                    grandToatal = BigDecimal.ZERO;
                                } else if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
                                    grandToatal = BigDecimal.ZERO;
                                } else {
                                    grandToatal = new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
                                }

                            } else {
                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(displayValues.get(col).toString());
                            }
                        } else if (userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("AVG")) {
                            String refEleArray[] = refferedElements.split(",");
                            int len = refEleArray.length;
                            int flag = 1;
                            String mysqlString = "";
                            for (int j = 0; j < len; j++) {
                                String elementId = refEleArray[j];
                                String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                PbReturnObject retobj1 = null;
                                try {
                                    retobj1 = pbdb.execSelectSQL(getBussColName);
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                }
                                if (retobj1 != null && retobj1.getRowCount() > 0) {
                                    String bussColName = retobj1.getFieldValueString(0, 0);
                                    String aggrType = retobj1.getFieldValueString(0, 1);
                                    if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                        String newEleID = "A_" + elementId;
                                        tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                        BigDecimal grandTotalValueForEle = null;
                                        grandTotalValueForEle = facade.getColumnGrandTotalValue(newEleID);
                                        if (grandTotalValueForEle == null) {
                                            flag = 0;
                                        }
                                        if (flag == 1) {
                                            grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                            if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                            }
                                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                            } else {
                                                tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    }
                                } else {
                                    grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                }
                            }
                            //Calculate formula
                            if (!tempFormula.equalsIgnoreCase("")) {
                                facade.container.summarizedHashmap.put(facade.getColumnId(col), "OTHERS");
                                grandToatal = (BigDecimal) getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                            } else {
                                grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                            }
                        } else if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
                            //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                            if (aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")) {
                                //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");

                                grandToatal = ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                int rowCnt = 0;
                                if (facade.container.getReportCollect().crosscolmap1.get(facade.getColumnId(col)).toString().equalsIgnoreCase("Exclude 0")) {
                                    rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), col, facade.getColumnId(col));
                                } else {
                                    rowCnt = facade.getRowCount();
                                }
                                grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                            } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
                                grandToatal = BigDecimal.ZERO;
                            } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
                                grandToatal = BigDecimal.ZERO;
                            } else {
                                grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
                            }
                        } else {
                            grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                        }
                        //added code by anitha
                                if (userColType.equalsIgnoreCase("TIMECALUCULATED") && (aggType.contains("avg") || aggType.contains("AVG"))) {
                                    int rowCnt = 0;
                                    if ("1".equals(facade.container.stype) || "0".equals(facade.container.stype)) {
                                        rowCnt = facade.container.gettrwcnt();
                                    } else {
                                        rowCnt = facade.getRowCount();
                                        facade.container.settrwcnt(rowCnt);
                                    }

                                    if (rowCnt != 0) {
                                        precision = container.getRoundPrecisionForMeasure(displayColumns[col]);
                                        grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                        grandToatal = new BigDecimal(NumberFormatter.getModifiedNumberFormat(grandToatal, "", precision));
                                        formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                    }
                                }
                                //end of code by anitha
                    } else if (container.isReportCrosstab()) {

                        HashMap<String, ArrayList> nonViewByMapNew = null;
                        nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
                        try {
                            if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                            }
//                        if (facade.container.getKpiQrycls().contains(eleId.trim())) {
                            if (eleId != null && eleId != "null" && eleId != "" && aggType.equalsIgnoreCase("avg")) {
                                //for calculating gt for avg cols like A_10,A_11 which are of gt type and is summarized
                                if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                                    String refEleArray[] = refferedElements.split(",");
                                    int len = refEleArray.length;
                                    int flag = 1;
                                    String mysqlString = "";
                                    if (facade.getColumnId(col).contains("A_")) {
                                        for (int j = 0; j < len; j++) {
                                            String elementId = refEleArray[j];
                                            String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                            PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                            if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                BigDecimal grandTotalValueForEle = null;
                                                String bussColName = retobj1.getFieldValueString(0, 0);
                                                String aggrType = retobj1.getFieldValueString(0, 1);
                                                if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                    String newEleID = "A_" + elementId;
                                                    tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                    for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                                        String key = e.getKey();
                                                        String value = e.getValue();
                                                        if (value.equalsIgnoreCase(newEleID) && key.contains("A_")) {
                                                            grandTotalValueForEle = facade.getColumnGrandTotalValue(key);
                                                            break;
                                                        }
                                                    }
                                                    if (grandTotalValueForEle == null) {
                                                        flag = 0;
                                                    }
                                                    if (flag == 1) {
                                                        grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                                        if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                            grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                        }
                                                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                            mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                                        } else {
                                                            tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                    }
                                                }
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            }
                                        }
                                        //Calculate formula value
                                        if (!tempFormula.equalsIgnoreCase("")) {
                                            grandToatal = getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    } else //for calculating gt for avg cols like A1,A2,A3 which are not of gt type
                                    if (nonViewByMapNew.get(facade.getColumnId(col)) != null) {
                                        String keyset = nonViewByMapNew.get(facade.getColumnId(col)).toString().replace("[", "").replace("]", "").trim();
                                        String mainKeySetArray1[] = keyset.split(",");
                                        String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
                                        for (int m = 0; m < mainKeySetArray1.length - 1; m++) {
                                            String val1 = mainKeySetArray1[m];
                                            mainKeySetArray[m] = mainKeySetArray1[m];
                                        }

                                        for (int j = 0; j < len; j++) {
                                            int flag1 = 0;
                                            flag = 0;
                                            BigDecimal grangTotalValueForEle = null;
                                            String elementId = refEleArray[j];
                                            String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                            PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                            if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                String bussColName = retobj1.getFieldValueString(0, 0);
                                                String aggrType = retobj1.getFieldValueString(0, 1);
                                                if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                    String newEleID = "A_" + elementId;
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
                                                                    for (int m = 0; m < tempKeysetArray1.length - 1; m++) {
                                                                        tempKeysetArray[m] = tempKeysetArray1[m];
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
                                                                                grangTotalValueForEle = facade.getColumnGrandTotalValue(key);
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
                                                        if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                            grangTotalValueForEle = grangTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                        }
                                                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                            mysqlString = mysqlString + "," + grangTotalValueForEle + " AS " + newEleID;
                                                        } else {
                                                            tempFormula = tempFormula.replace(newEleID, grangTotalValueForEle.toString());
                                                        }
                                                        flag1 = 1;
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                    }
                                                }
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            }
                                        }
                                        //Calculate formula value
                                        if (!tempFormula.equalsIgnoreCase("")) {
                                            grandToatal = getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    }
                                } else {
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    int rowCnt = 0;
                                    if (facade.container.getReportCollect().crosscolmap1.get(element).toString().equalsIgnoreCase("Exclude 0")) {
                                        rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), col, facade.getColumnId(col));
                                    } else {
                                        rowCnt = facade.getRowCount();
                                    }
                                    grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                }
                            } else if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
                                if (nonViewByMapNew.get(facade.getColumnId(col)) != null) {
                                    String keyset = nonViewByMapNew.get(facade.getColumnId(col)).toString().replace("[", "").replace("]", "").trim();
                                    if (facade.container.retObjHashmap.get(keyset) != null) {
                                        grandToatal = new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    }
                                } else {
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                }
                            } else {
                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                            }
                        } catch (SQLException ex) {
                            logger.error("Exception: ", ex);
                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                        }

                    } else {
                        grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                    }
                    if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                        formattedData = grandToatal.toString();
                    } else if (container.gettimeConversion(displayValues.get(col).toString()) != null && container.gettimeConversion(displayValues.get(col).toString()).equalsIgnoreCase("Y")) {
                        formattedData = this.convertDataToTime(grandToatal);
                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                        formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                        formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                        // formattedData = snbrSymbol+""+formattedData;
                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                        formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                        if (snbrSymbol.equalsIgnoreCase("%")) {
                            formattedData = formattedData + "" + snbrSymbol;
                        } else {
                            formattedData = snbrSymbol + "" + formattedData;
                        }
                    } else {
                        try {
                            formattedData = NumberFormatter.getModifiedNumber(grandToatal, nbrSymbol, precision);
                        } catch (NullPointerException e) {
                            logger.error("Exception: ", e);
                        }
                    }
                    //cell = new Number(col, i+k+rowStart+1, Double.parseDouble(formattedData),cf2);
                    if (container.gettimeConversion(displayValues.get(col).toString()) != null && container.gettimeConversion(displayValues.get(col).toString()).equalsIgnoreCase("Y")) {
                        //cell = new Label(col, i+k+rowStart+1, formattedData,cf2);
                    } else if (formattedData.contains("%")) {
                        //cell = new Label(col, i + k + rowStart + 1, formattedData, cf2);
                        hhhhCell = hhhhRow.createCell(actualcol + colNumber);
                        hhhhCell.setCellValue(formattedData);
                        if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                            //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                            hhhhCell = hhhhRow.createCell(actualcol + colNumber);
                            hhhhCell.setCellValue(formattedData);
                        } else {
                            //cell = new Number(col, i + k + rowStart + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                            hhhhCell = hhhhRow.createCell(actualcol + colNumber);
                            hhhhCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
                        }
                    } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                        //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                        hhhhCell = hhhhRow.createCell(actualcol + colNumber);
                        hhhhCell.setCellValue(formattedData);
                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                        //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                        hhhhCell = hhhhRow.createCell(actualcol + colNumber);
                        hhhhCell.setCellValue(formattedData);
                    } else {
                        //cell = new Number(col, i + k + rowStart + 1, Double.parseDouble(formattedData.replace(",", "")), cf2);
                        hhhhCell = hhhhRow.createCell(actualcol + colNumber);
                        hhhhCell.setCellValue(Double.parseDouble(formattedData.replace(",", "")));
                    }

                } else {
                    //  if(i==0){
                    //cell = new Label(col, i+k+rowStart+1, "Grand Total");
                    hhhhCell = hhhhRow.createCell(col + colNumber);
                    hhhhCell.setCellValue("Grand Total");
//}
//                    else{
//                         //cell = new Label(col, i+k+rowStart+1, "Sub Total");         // Modified from Category Sub Total to Sub Total by Amar
//                        hCell= hRow.createCell(col+colNumber);
//                        hCell.setCellValue("Sub Total");
//                    }
                }
//                cell.setCellFormat(writableCellFormat);

//             try {
//                 //writableSheet.addCell(cell);
//             } catch (WriteException ex) {
//                 logger.error("Exception: ",ex);
//            }
                actualcol++;
            }
        }
        //}
    }
    //added by Dinanath for xlsx

    public void generateSubTotalHtmlForExportReportXLSX(int k) {

        ArrayList displayValues = new ArrayList();
        ArrayList viewBys = new ArrayList();
        ArrayList viewBys1 = new ArrayList();
        displayValues = container.getDisplayColumns();
//        for(int i=0;i<displayColumns.length;i++){
//            displayValues.add(displayColumns[i]);
//        }
        viewBys1 = container.getDisplayTypes();
        for (int l = 0; l < types.length; l++) {
            viewBys.add(types[l]);
        }
        if (displayValues.size() > viewBys.size()) {
            for (int i = 0; i < displayValues.size(); i++) {
                if (i >= viewBys.size()) {
                    viewBys.add("N");
                }
            }
        }
        //started by Amar
        String aggType = "";
        String refferedElements = "";
        String userColType = "";
        String refElementType = "";
        String tempFormula = "";
        boolean isRunTime = false;
        PbDb pbdb = new PbDb();
        PbReturnObject retobj = null;
        DataFacade facade = new DataFacade(container);
        ArrayList<String> hidemeasurecount = facade.container.getReportCollect().getHideMeasures();
        ArrayList<Integer> index = new ArrayList<Integer>();
        // end by Amar
        // code added by Amar
        String eleIdss = "";
        if (container.isReportCrosstab()) {
            HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
            for (Object hiddenCol : hidemeasurecount) {
                for (int i = 0; i < displayValues.size(); i++) {
                    eleIdss = crosstabMeasureId.get(displayValues.get(i));
                    if (eleIdss != null && !eleIdss.equalsIgnoreCase("null") && !eleIdss.equalsIgnoreCase("")) {
                        if (hidemeasurecount.contains(eleIdss.replace("A_", ""))) {
                            index.add(i);
                        }
                        // index.add
                    }
                }
            }
        } else if (hidemeasurecount.size() > 0) {
            for (int i = 0; i < hidemeasurecount.size(); i++) {
                index.add(facade.container.getDisplayColumns().indexOf("A_" + hidemeasurecount.get(i)));
            }
        }
        // end of code
        ArrayList<Integer> sortSequence = null;
        if (sortTypes != null && sortDataTypes != null) {
            sortSequence = ret.sortDataSet(sortColumns, sortTypes, sortDataTypes);
        } else {
            sortSequence = ret.getViewSequence();
        }

        //  for (int i = 0; i < 2; i++) {
        int actualcol = 0;
        if (facade.isGrandTotalRequired()) {
            hRow = hSheet.createRow(k + lineNumber);
            for (int col = 0; col < displayValues.size(); col++) {
                if (!index.contains(col)) {
                    if (viewBys.get(col).toString().equalsIgnoreCase("N")) {
//                 BigDecimal grandToatal=(BigDecimal)ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                        String formattedData = "";
                        String element = container.getDisplayColumns().get(col);
                        String nbrSymbol = "";
                        HashMap NFMap = new HashMap();
                        NFMap = (HashMap) container.getTableHashMap().get("NFMap");
                        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
                        if (NFMap != null) {
                            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                if (NFMap.get(crosstabMeasureId.get(element)) != null) {
                                    nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                                }
                            } else if (NFMap.get(element) != null) {
                                nbrSymbol = String.valueOf(NFMap.get(element));
                            }
                        }
                        //Added by Amar
                        if (RTMeasureElement.isRunTimeMeasure(element)) {
                            nbrSymbol = RTMeasureElement.getMeasureType(element).getNumberSymbol();
                        }
                        //end of code
                        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
                        int precision = container.getRoundPrecisionForMeasure(element);
                        if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty() && crosstabMeasureId.get(element) != null) {
                            precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                            element = crosstabMeasureId.get(element);
                        }

                        BigDecimal grandToatal = new BigDecimal(0);
                        // Modified by Amar
                        if (element != null && !element.equalsIgnoreCase("null") && !element.equalsIgnoreCase("") && !element.isEmpty()) {
                            String eleId = element.replace("A_", "");
                            if (eleId.contains("_percentwise") || eleId.contains("_rank") || eleId.contains("_wf") || eleId.contains("_wtrg") || eleId.contains("_rt") || eleId.contains("_pwst") || eleId.contains("_excel") || eleId.contains("_excel_target") || eleId.contains("_deviation_mean") || eleId.contains("_gl") || eleId.contains("_userGl") || eleId.contains("_timeBased") || eleId.contains("_changedPer") || eleId.contains("_glPer")) {
                                eleId = eleId.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
                                isRunTime = true;
                            }
                            String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
                            try {
                                retobj = pbdb.execSelectSQL(qry);
                            } catch (SQLException ex) {
                                logger.error("Exception: ", ex);
                            }
                            if (retobj != null && retobj.getRowCount() > 0) {
                                tempFormula = retobj.getFieldValueString(0, 0);
                                refferedElements = retobj.getFieldValueString(0, 1);
                                userColType = retobj.getFieldValueString(0, 2);
                                refElementType = retobj.getFieldValueString(0, 3);
                                aggType = retobj.getFieldValueString(0, 4);
                                tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");

                            }

//                     if (i == 0) {
//                        if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
//                            //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                             if(aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")){
//                                 //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
//                                 grandToatal = ret.getColumnGrandTotalValue(facade.getColumnId(col));
//                             }else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
//                                grandToatal = BigDecimal.ZERO;
//                            } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
//                                grandToatal = BigDecimal.ZERO;
//                            } else {
//                                grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
//                            }
//                        } else {
//                            grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
//                        }
//                    //} else {
//                      //  grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
//                    //}
//                     if(aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")){
//                        int rowCnt = 0;
//                            rowCnt=facade.getRowCount();
//                             grandToatal=grandToatal.divide(new BigDecimal(rowCnt),RoundingMode.HALF_UP);
//                         }
                            // end of code bu Amar
                            if (!container.isReportCrosstab()) {

                                if (isRunTime) {
                                    if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    }
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    //modified by anitha
                                } else if (userColType.equalsIgnoreCase("TIMECALUCULATED")&& (aggType.contains("avg") || aggType.contains("AVG"))) {
                                    facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));

                                } else if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
                                    if (!facade.container.getKpiQrycls().contains(facade.getColumnId(col).replace("A_", "").trim())) {

                                        grandToatal = (BigDecimal) getGTForChangePerOfSummTabMeasures(eleId, col);

                                    } else if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
                                        //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                        if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
                                            grandToatal = BigDecimal.ZERO;
                                        } else if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
                                            grandToatal = BigDecimal.ZERO;
                                        } else {
                                            grandToatal = new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
                                        }

                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(displayValues.get(col).toString());
                                    }
                                } else if (userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("AVG")) {
                                    String refEleArray[] = refferedElements.split(",");
                                    int len = refEleArray.length;
                                    int flag = 1;
                                    String mysqlString = "";
                                    for (int j = 0; j < len; j++) {
                                        String elementId = refEleArray[j];
                                        String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                        PbReturnObject retobj1 = null;
                                        try {
                                            retobj1 = pbdb.execSelectSQL(getBussColName);
                                        } catch (SQLException ex) {
                                            logger.error("Exception: ", ex);
                                        }
                                        if (retobj1 != null && retobj1.getRowCount() > 0) {
                                            String bussColName = retobj1.getFieldValueString(0, 0);
                                            String aggrType = retobj1.getFieldValueString(0, 1);
                                            if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                String newEleID = "A_" + elementId;
                                                tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                BigDecimal grandTotalValueForEle = null;
                                                grandTotalValueForEle = facade.getColumnGrandTotalValue(newEleID);
                                                if (grandTotalValueForEle == null) {
                                                    flag = 0;
                                                }
                                                if (flag == 1) {
                                                    grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                                    if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                        grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                    }
                                                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                        mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                                    } else {
                                                        tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                    }
                                                } else {
                                                    grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    }
                                    //Calculate formula
                                    if (!tempFormula.equalsIgnoreCase("")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "OTHERS");
                                        grandToatal = (BigDecimal) getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                    } else {
                                        grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                    }
                                } else if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(col).toString().replace("A_", "").trim())) {
                                    //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                    if (aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")) {
                                        //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");

                                        grandToatal = ret.getColumnGrandTotalValue(facade.getColumnId(col));
                                        int rowCnt = 0;
                                        if (facade.container.getReportCollect().crosscolmap1.get(facade.getColumnId(col)).toString().equalsIgnoreCase("Exclude 0")) {
                                            rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), col, facade.getColumnId(col));
                                        } else {
                                            rowCnt = facade.getRowCount();
                                        }
                                        grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                    } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()) == null) {
                                        grandToatal = BigDecimal.ZERO;
                                    } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()).equalsIgnoreCase("")) {
                                        grandToatal = BigDecimal.ZERO;
                                    } else {
                                        grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(col).toString()));
                                    }
                                } else {
                                    grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                                }
                                //added code by anitha
                                if (userColType.equalsIgnoreCase("TIMECALUCULATED") && (aggType.contains("avg") || aggType.contains("AVG"))) {
                                    int rowCnt = 0;
                                    if ("1".equals(facade.container.stype) || "0".equals(facade.container.stype)) {
                                        rowCnt = facade.container.gettrwcnt();
                                    } else {
                                        rowCnt = facade.getRowCount();
                                        facade.container.settrwcnt(rowCnt);
                                    }

                                    if (rowCnt != 0) {
                                        precision = container.getRoundPrecisionForMeasure(displayColumns[col]);
                                        grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                        grandToatal = new BigDecimal(NumberFormatter.getModifiedNumberFormat(grandToatal, "", precision));
                                        formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                    }
                                }
                                //end of code by anitha
                            } else if (container.isReportCrosstab()) {

                                HashMap<String, ArrayList> nonViewByMapNew = null;
                                nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
                                try {
                                    if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(col), "NORMAL_AVG");
                                    }
//                        if (facade.container.getKpiQrycls().contains(eleId.trim())) {
                                    if (eleId != null && eleId != "null" && eleId != "" && aggType.equalsIgnoreCase("avg")) {
                                        //for calculating gt for avg cols like A_10,A_11 which are of gt type and is summarized
                                        if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                                            String refEleArray[] = refferedElements.split(",");
                                            int len = refEleArray.length;
                                            int flag = 1;
                                            String mysqlString = "";
                                            if (facade.getColumnId(col).contains("A_")) {
                                                for (int j = 0; j < len; j++) {
                                                    String elementId = refEleArray[j];
                                                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                                    PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                        BigDecimal grandTotalValueForEle = null;
                                                        String bussColName = retobj1.getFieldValueString(0, 0);
                                                        String aggrType = retobj1.getFieldValueString(0, 1);
                                                        if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                            String newEleID = "A_" + elementId;
                                                            tempFormula = tempFormula.toUpperCase().replace(bussColName.toUpperCase(), newEleID);
                                                            for (Map.Entry<String, String> e : crosstabMeasureId.entrySet()) {
                                                                String key = e.getKey();
                                                                String value = e.getValue();
                                                                if (value.equalsIgnoreCase(newEleID) && key.contains("A_")) {
                                                                    grandTotalValueForEle = facade.getColumnGrandTotalValue(key);
                                                                    break;
                                                                }
                                                            }
                                                            if (grandTotalValueForEle == null) {
                                                                flag = 0;
                                                            }
                                                            if (flag == 1) {
                                                                grandTotalValueForEle = grandTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                                                if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                                    grandTotalValueForEle = grandTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                                }
                                                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                                    mysqlString = mysqlString + "," + grandTotalValueForEle + " AS " + newEleID;
                                                                } else {
                                                                    tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                                }
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                            }
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                    }
                                                }
                                                //Calculate formula value
                                                if (!tempFormula.equalsIgnoreCase("")) {
                                                    grandToatal = getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            } else //for calculating gt for avg cols like A1,A2,A3 which are not of gt type
                                            if (nonViewByMapNew.get(facade.getColumnId(col)) != null) {
                                                String keyset = nonViewByMapNew.get(facade.getColumnId(col)).toString().replace("[", "").replace("]", "").trim();
                                                String mainKeySetArray1[] = keyset.split(",");
                                                String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
                                                for (int m = 0; m < mainKeySetArray1.length - 1; m++) {
                                                    String val1 = mainKeySetArray1[m];
                                                    mainKeySetArray[m] = mainKeySetArray1[m];
                                                }

                                                for (int j = 0; j < len; j++) {
                                                    int flag1 = 0;
                                                    flag = 0;
                                                    BigDecimal grangTotalValueForEle = null;
                                                    String elementId = refEleArray[j];
                                                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                                    PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                                                        String bussColName = retobj1.getFieldValueString(0, 0);
                                                        String aggrType = retobj1.getFieldValueString(0, 1);
                                                        if (tempFormula.toUpperCase().contains(bussColName.toUpperCase())) {
                                                            String newEleID = "A_" + elementId;
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
                                                                            for (int m = 0; m < tempKeysetArray1.length - 1; m++) {
                                                                                tempKeysetArray[m] = tempKeysetArray1[m];
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
                                                                                        grangTotalValueForEle = facade.getColumnGrandTotalValue(key);
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
                                                                if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                                                    grangTotalValueForEle = grangTotalValueForEle.divide(new BigDecimal(facade.getRowCount()), RoundingMode.HALF_UP);
                                                                }
                                                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                                    mysqlString = mysqlString + "," + grangTotalValueForEle + " AS " + newEleID;
                                                                } else {
                                                                    tempFormula = tempFormula.replace(newEleID, grangTotalValueForEle.toString());
                                                                }
                                                                flag1 = 1;
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                            }
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                    }
                                                }
                                                //Calculate formula value
                                                if (!tempFormula.equalsIgnoreCase("")) {
                                                    grandToatal = getComputeFormulaVal(col, tempFormula, mysqlString, "GT");
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                                }
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            int rowCnt = 0;
                                            if (facade.container.getReportCollect().crosscolmap1.get(element).toString().equalsIgnoreCase("Exclude 0")) {
                                                rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), col, facade.getColumnId(col));
                                            } else {
                                                rowCnt = facade.getRowCount();
                                            }
                                            grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                        }
                                    } else if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
                                        if (nonViewByMapNew.get(facade.getColumnId(col)) != null) {
                                            String keyset = nonViewByMapNew.get(facade.getColumnId(col)).toString().replace("[", "").replace("]", "").trim();
                                            if (facade.container.retObjHashmap.get(keyset) != null) {
                                                grandToatal = new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                        }
                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                    }
                                } catch (SQLException ex) {
                                    logger.error("Exception: ", ex);
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(col));
                                }

                            } else {
                                grandToatal = (BigDecimal) ret.getColumnGrandTotalValue(displayValues.get(col).toString());
                            }
                            if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                formattedData = grandToatal.toString();
                            } else if (container.gettimeConversion(displayValues.get(col).toString()) != null && container.gettimeConversion(displayValues.get(col).toString()).equalsIgnoreCase("Y")) {
                                formattedData = this.convertDataToTime(grandToatal);
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
                                formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                formattedData = snbrSymbol + "" + formattedData + "" + nbrSymbol;
                                // formattedData = snbrSymbol+""+formattedData;
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                                if (snbrSymbol.equalsIgnoreCase("%")) {
                                    formattedData = formattedData + "" + snbrSymbol;
                                } else {
                                    formattedData = snbrSymbol + "" + formattedData;
                                }
                            } else {
                                try {
                                    formattedData = NumberFormatter.getModifiedNumber(grandToatal, nbrSymbol, precision);
                                } catch (NullPointerException e) {
                                    logger.error("Exception: ", e);
                                }
                            }
                            //cell = new Number(col, i+k+rowStart+1, Double.parseDouble(formattedData),cf2);
                            if (container.gettimeConversion(displayValues.get(col).toString()) != null && container.gettimeConversion(displayValues.get(col).toString()).equalsIgnoreCase("Y")) {
                                //cell = new Label(col, i+k+rowStart+1, formattedData,cf2);
                            } else if (formattedData.contains("%")) {
                                //cell = new Label(col, i + k + rowStart + 1, formattedData, cf2);
                                hCell = hRow.createCell(actualcol + colNumber);
                                hCell.setCellValue(formattedData);
                                if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                    //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                                    hCell = hRow.createCell(actualcol + colNumber);
                                    hCell.setCellValue(formattedData);
                                } else {
                                    //cell = new Number(col, i + k + rowStart + 1, Double.parseDouble((formattedData).replace(",", "")),cf2);
                                    hCell = hRow.createCell(actualcol + colNumber);
                                    hCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
                                }
                            } else if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                                hCell = hRow.createCell(actualcol + colNumber);
                                hCell.setCellValue(formattedData);
                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                //cell = new Label(col, i + k + rowStart + 1, formattedData,cf2);
                                hCell = hRow.createCell(actualcol + colNumber);
                                hCell.setCellValue(formattedData);
                            } else {
                                //cell = new Number(col, i + k + rowStart + 1, Double.parseDouble(formattedData.replace(",", "")), cf2);
                                hCell = hRow.createCell(actualcol + colNumber);
                                hCell.setCellValue(Double.parseDouble(formattedData.replace(",", "")));
                            }
                        }
                    } else {
                        //  if(i==0){
                        //cell = new Label(col, i+k+rowStart+1, "Grand Total");
                        hCell = hRow.createCell(col + colNumber);
                        hCell.setCellValue("Grand Total");
//}
//                    else{
//                         //cell = new Label(col, i+k+rowStart+1, "Sub Total");         // Modified from Category Sub Total to Sub Total by Amar
//                        hCell= hRow.createCell(col+colNumber);
//                        hCell.setCellValue("Sub Total");
//                    }
                    }
//                cell.setCellFormat(writableCellFormat);

//             try {
//                 //writableSheet.addCell(cell);
//             } catch (WriteException ex) {
//                 logger.error("Exception: ",ex);
//            }
                    actualcol++;
                }
            }
            k = k + 1;
        }

        //added by sruthi for overallavg,overallmax,overallmin
        int j = 0;
        BigDecimal othersValue = null;
        if (facade.isAverageRequired()) {
            hRow = hSheet.createRow(k + lineNumber);
            BigDecimal overallavg;
            for (int i = 0; i < 1; i++) {

                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (!index.contains(col)) {
                        if (viewBys.get(col).toString().equalsIgnoreCase("T") || viewBys.get(col).toString().equalsIgnoreCase("N")) {
                            othersValue = (BigDecimal) facade.getColumnAverageValue(displayValues.get(col).toString());
                            //end of code
                            this.getOthersDataX(othersValue, col, hRow, startcol, displayValues);
                        } else if (i == 0) {
                            hCell = hRow.createCell(startcol);
                            hCell.setCellValue("OverAllAvg");
                        }
                        startcol++;
                    }
                }
            }
            j++;
        }
        if (facade.isOverAllMaxRequired()) {
            BigDecimal overAllMax;
            hRow = hSheet.createRow(k + lineNumber + j);
            for (int i = 0; i < 1; i++) {
                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (!index.contains(col)) {
                        if (viewBys.get(col).toString().equalsIgnoreCase("T") || viewBys.get(col).toString().equalsIgnoreCase("N")) {
                            othersValue = (BigDecimal) facade.getColumnMaximumValue(displayValues.get(col).toString());
                            this.getOthersDataX(othersValue, col, hRow, startcol, displayValues);
                        } else {
                            if (i == 0) {
                                hCell = hRow.createCell(startcol);
                                hCell.setCellValue("OverAllMax");
                            }
                        }
                        startcol++;
                    }
                }
            }
            j++;
        }
        if (facade.isOverAllMinRequired()) {
            hRow = hSheet.createRow(k + lineNumber + j);
            for (int i = 0; i < 1; i++) {
                int startcol = 0;
                for (int col = 0; col < displayValues.size(); col++) {
                    if (!index.contains(col)) {
                        if (viewBys.get(col).toString().equalsIgnoreCase("T") || viewBys.get(col).toString().equalsIgnoreCase("N")) {
                            othersValue = (BigDecimal) facade.getColumnMinimumValue(displayValues.get(col).toString());
                            this.getOthersDataX(othersValue, col, hRow, startcol, displayValues);
                        } else {
                            if (i == 0) {
                                hCell = hRow.createCell(startcol);
                                hCell.setCellValue("OverAll Min");
                            }
                        }
                        startcol++;
                    }
                }
            }
            j++;
        }
        //}
    }

    public BigDecimal getComputeFormulaVal(int column, String tempFormula, String mysqlString, String stType) {
        PbDb pbdb = new PbDb();
        String formula = "";
        ArrayList displayValues = new ArrayList();
        displayValues = container.getDisplayColumns();
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            tempFormula = "SELECT " + tempFormula;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            if (mysqlString != null && !mysqlString.isEmpty()) {
                mysqlString = mysqlString.substring(1);
            }
            tempFormula = "SELECT " + tempFormula + " FROM (SELECT " + mysqlString + ") A";
        } else {
            tempFormula = "SELECT " + tempFormula + " FROM DUAL";
        }
        PbReturnObject retobj2 = null;
        try {
            if (tempFormula.contains("CURRENT") || tempFormula.contains("PRIOR")) {
                if (stType.equalsIgnoreCase("GT")) {
//                    if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
//                        return BigDecimal.ZERO;
//                    } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
//                        return BigDecimal.ZERO;
//                    } else {
//                        return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                    }
                    if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(column).toString().replace("A_", "").trim())) {
                        if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(column).toString()) == null) {
                            return BigDecimal.ZERO;
                        } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(column).toString()).equalsIgnoreCase("")) {
                            return BigDecimal.ZERO;
                        } else {
                            return new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(column).toString()));
                        }
                    } else {
                        return ret.getColumnGrandTotalValue(displayValues.get(column).toString());
                    }
                } else {
                    return BigDecimal.ZERO;
                }
            }
            retobj2 = pbdb.execSelectSQL(tempFormula);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        if (retobj2 != null && retobj2.getRowCount() > 0) {
            formula = retobj2.getFieldValueString(0, 0);
            if (formula.equalsIgnoreCase("")) {
                formula = "0";
            }
            BigDecimal subTotalVal = new BigDecimal(formula);
            subTotalVal = subTotalVal.setScale(2, RoundingMode.CEILING);
            return subTotalVal;
        } else if (stType.equalsIgnoreCase("GT")) {
//                if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)) == null) {
//                    return BigDecimal.ZERO;
//                } else if (facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)).equalsIgnoreCase("")) {
//                    return BigDecimal.ZERO;
//                } else {
//                    return new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
//                }
            if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(column).toString().replace("A_", "").trim())) {
                //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(column).toString()) == null) {
                    return BigDecimal.ZERO;
                } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(column).toString()).equalsIgnoreCase("")) {
                    return BigDecimal.ZERO;
                } else {
                    return new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(column).toString()));
                }
            } else {
                return ret.getColumnGrandTotalValue(displayValues.get(column).toString());
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getGTForChangePerOfSummTabMeasures(String eleId, int column) {
        PbDb pbdb = new PbDb();
        ArrayList displayValues = new ArrayList();
        displayValues = container.getDisplayColumns();
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
                    gtValueForEle = ret.getColumnGrandTotalValue(newEleID);
                    if (gtValueForEle == null) {
                        flag = 0;
                    }
                    if (flag == 1) {
                        gtValueForEle = gtValueForEle.setScale(2, RoundingMode.CEILING);
                        if (retobj.getFieldValueString(i, 2).equalsIgnoreCase("AVG") || retobj.getFieldValueString(i, 2).toUpperCase().contains("AVG")) {
                            gtValueForEle = gtValueForEle.divide(new BigDecimal(ret.getRowCount()), RoundingMode.HALF_UP);
                        }
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            mysqlString = mysqlString + "," + gtValueForEle + " AS " + retobj.getFieldValueString(i, 0);
                            if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                                tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                            } else {
                                tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                            }
                        } else if (retobj.getFieldValueString(i, 1).equalsIgnoreCase("1")) {
                            tempFormula = tempFormula.replace("CURRENT", gtValueForEle.toString());
                        } else {
                            tempFormula = tempFormula.replace("PRIOR", gtValueForEle.toString());
                        }
                    } else {
                        return this.ret.getColumnGrandTotalValue(displayValues.get(column).toString());
                    }
                }
                //Calculate formula
                if (!tempFormula.equalsIgnoreCase("")) {
                    container.summarizedHashmap.put(displayValues.get(column).toString(), "OTHERS");
                    return getComputeFormulaVal(column, tempFormula, mysqlString, "GT");
                } else {
                    return this.ret.getColumnGrandTotalValue(displayValues.get(column).toString());
                }
            } else {
                return this.ret.getColumnGrandTotalValue(displayValues.get(column).toString());
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
            return this.ret.getColumnGrandTotalValue(displayValues.get(column).toString());
        }
    }

    protected int getZeroRowCount(Container container, int rowCount, int col, String colId) {
        int zeroRowCount = 0;
        DataFacade facade = new DataFacade(container);
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

    //code added by Sruthi on Sep 4,2015
    public BigDecimal getOthersDataX(BigDecimal othersValue, int col, XSSFRow hRow, int startcol, ArrayList displayValues) {
        //String nbrSymbol="";
        String formattedData = "";

        // String element = container.getDisplayColumns().get(col);
        //Code added by Amar
        HashMap NFMap = new HashMap();
        String element = displayValues.get(col).toString();
        int precision = container.getRoundPrecisionForMeasure(element);
        String nbrSymbol = "";
        NFMap = (HashMap) container.getTableHashMap().get("NFMap");
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        if (NFMap != null) {
            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                if (NFMap.get(crosstabMeasureId.get(element)) != null) {
                    nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                }
            } else if (NFMap.get(element) != null) {
                nbrSymbol = String.valueOf(NFMap.get(element));
            }
        }
        //Added by Amar
        if (RTMeasureElement.isRunTimeMeasure(element)) {
            nbrSymbol = RTMeasureElement.getMeasureType(element).getNumberSymbol();
        }
        //end of code
        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
        if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
            formattedData = othersValue.toString();
        } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
            formattedData = NumberFormatter.getModifiedNumberFormat(othersValue, nbrSymbol, precision);
            formattedData = snbrSymbol + "" + othersValue + "" + nbrSymbol;
            formattedData = snbrSymbol + "" + formattedData;
        } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
            formattedData = NumberFormatter.getModifiedNumberFormat(othersValue, nbrSymbol, precision);
            if (snbrSymbol.equalsIgnoreCase("%")) {
                formattedData = formattedData + "" + snbrSymbol;
            } else {
                formattedData = snbrSymbol + "" + formattedData;
            }
        } else {
            try {
                formattedData = NumberFormatter.getModifiedNumber(othersValue, nbrSymbol, precision);
            } catch (NullPointerException e) {
                logger.error("Exception: ", e);
            }
        }
        hCell = hRow.createCell(startcol);
        //hCell.setCellValue(Double.parseDouble(formattedData.replace(",", "")));
        hCell.setCellValue(formattedData);
        return null;
    }

    public BigDecimal getOthersDataS(BigDecimal othersValue, int col, HSSFRow hRow, int startcol, ArrayList displayValues) {
        //String nbrSymbol="";
        String formattedData = "";
        //Code added by Amar
        HashMap NFMap = new HashMap();
        String element = displayValues.get(col).toString();
        int precision = container.getRoundPrecisionForMeasure(element);
        String nbrSymbol = "";
        NFMap = (HashMap) container.getTableHashMap().get("NFMap");
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        if (NFMap != null) {
            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                if (NFMap.get(crosstabMeasureId.get(element)) != null) {
                    nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(element)));
                }
            } else if (NFMap.get(element) != null) {
                nbrSymbol = String.valueOf(NFMap.get(element));
            }
        }
        //Added by Amar
        if (RTMeasureElement.isRunTimeMeasure(element)) {
            nbrSymbol = RTMeasureElement.getMeasureType(element).getNumberSymbol();
        }
        //end of code
        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
        if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
            formattedData = othersValue.toString();
        } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("") && !nbrSymbol.equalsIgnoreCase("")) {
            formattedData = NumberFormatter.getModifiedNumberFormat(othersValue, nbrSymbol, precision);
            formattedData = snbrSymbol + "" + othersValue + "" + nbrSymbol;
            formattedData = snbrSymbol + "" + formattedData;
        } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
            formattedData = NumberFormatter.getModifiedNumberFormat(othersValue, nbrSymbol, precision);
            if (snbrSymbol.equalsIgnoreCase("%")) {
                formattedData = formattedData + "" + snbrSymbol;
            } else {
                formattedData = snbrSymbol + "" + formattedData;
            }
        } else {
            try {
                formattedData = NumberFormatter.getModifiedNumber(othersValue, nbrSymbol, precision);
            } catch (NullPointerException e) {
                logger.error("Exception: ", e);
            }
        }
        hhCell = hRow.createCell(startcol);
        //hCell.setCellValue(Double.parseDouble(formattedData.replace(",", "")));
        hhCell.setCellValue(formattedData);
        return null;
    }
//ended by sruthi
}
