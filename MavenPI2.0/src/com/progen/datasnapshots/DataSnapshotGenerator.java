
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.datasnapshots;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.report.NewProductNameHelper;
import com.progen.report.PbReportCollection;
import com.progen.report.data.DataFacade;
import com.progen.report.data.RowViewTableBuilder;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableBuilder.*;
import com.progen.report.display.HtmlTableBodyDisplay;
import com.progen.report.display.HtmlTableHeaderDisplay;
import com.progen.report.display.HtmlTableSubTotalDisplay;
import com.progen.report.display.table.TableDisplay;
import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import prg.db.Container;
import java.util.Iterator;
import java.util.ArrayList;
import com.progen.report.ReportParameterValue;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.display.CDTableBodyDisplay;
import com.progen.report.display.CDTableHeaderDisplay;
import com.progen.report.display.CDTableSubTotalDisplay;
import com.progen.report.display.CSVTableBodyDisplay;
import com.progen.report.display.CSVTableHeaderDisplay;
import com.progen.report.display.CSVTableSubTotalDisplay;
import com.progen.report.display.TSTableBodyDisplay;
import com.progen.report.display.TSTableHeaderDisplay;
import com.progen.report.display.TSTableSubTotalDisplay;
import com.progen.report.display.XMLTableBodyDisplay;
import com.progen.report.display.XMLTableHeaderDisplay;
import com.progen.report.display.XMLTableSubTotalDisplay;
import com.progen.report.query.DataSetHelper;
import com.progen.report.query.PbReportQuery;
import com.sap.mw.jco.IMiddleware.IServer;
import java.util.Map;
import prg.db.PbReturnObject;
import java.io.OutputStream;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;
import org.jfree.chart.block.CenterArrangement;
import org.jfree.ui.Align;
import prg.db.ReportTablePropertyBuilder;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.display.util.NumberFormatter;
import com.progen.reportview.bd.PbReportViewerBD;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import prg.db.ContainerConstants;
import prg.util.PbMail;
import prg.util.PbMailParams;
import com.progen.report.MultiPeriodKPI;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.PdfTableBodyDisplay;
import com.progen.report.display.PdfTableSubTotalDisplay;
import com.progen.report.kpi.KPIBuilderScheduler;
import com.progen.report.pbDashboardCollection;
import com.progen.reportdesigner.bd.ReportTemplateBD;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.scheduler.ReportSchedule;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import prg.db.PbDb;
import java.sql.SQLException;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.WriteException;
import utils.db.ProgenConnection;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.streaming.SXSSFCell;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.log4j.*;
import java.text.DecimalFormat;

/**
 *
 * @author arun
 */
public class DataSnapshotGenerator {

    public static Logger logger = Logger.getLogger(DataSnapshotGenerator.class);
    public String delimiter;
    public String textIdentifier;
    public String paramType;
    public String htmlCellHeight;
    private int fromRow;
    private int toRow;
    private String ScheduleFileName;
    PbMailParams params = null;//added by dinanath
    PbMail mailer = null;
    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
    Font fontHead = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
    ByteArrayOutputStream bos = null;
    private static Font redFont = new Font(Font.TIMES_ROMAN, 15,
            Font.NORMAL, Color.BLUE);

    public String getFileName(String reportId, String userId) {
        return ServletUtilities.prefix + reportId + "_" + userId + "_DS";
    }
    //added by Dinanath for converting hashcode values of color into actual color

    public static Colour getNearestColour(String strColor) {
        Color cl = Color.decode(strColor);
        Colour color = null;
        Colour[] colors = Colour.getAllColours();
        if (colors != null && colors.length > 0) {
            Colour crtColor = null;

            int[] rgb = null;
            int diff = 0;
            int mindiff = 999;
            for (int i = 0; i < colors.length; i++) {
                crtColor = colors[i];
                rgb = new int[3];
                rgb[0] = crtColor.getDefaultRGB().getRed();
                rgb[1] = crtColor.getDefaultRGB().getGreen();
                rgb[2] = crtColor.getDefaultRGB().getBlue();

                diff = Math.abs(rgb[0] - cl.getRed()) + Math.abs(rgb[1] - cl.getGreen()) + Math.abs(rgb[2] - cl.getBlue());
//diff=Math.abs(rgb[0]-cl.getRed())+Math.abs(rgb[1]-cl.getGreen());
                if (diff < mindiff) {
                    mindiff = diff;
                    color = crtColor;
                }
            }
        }
        if (color == null) {
            color = Colour.BLACK;
        }
        return color;
    }

    public String generateXmlSnapshot(Container container, String userId) {
        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        ArrayList<String> colViewbys = new ArrayList<String>();
        ArrayList<String> rowViewbys = collect.reportParamIds;
        ArrayList qryCols = container.getTableMeasure();
        ArrayList<String> qryAggrs = new ArrayList<String>();
        ArrayList timeDetailsArray = collect.timeDetailsArray;

        Map dataHelper = DataSetHelper.getAggregationMap(qryCols);
        for (int i = 0; i < qryCols.size(); i++) {
            String aggType = dataHelper.get(qryCols.get(i)).toString();
            qryAggrs.add(aggType);

        }

        ArrayList<String> columnIds = new ArrayList<String>();
        ArrayList columnNames = new ArrayList();

        ProgenDataSet retObj = new PbReturnObject();
        StringBuilder xmlRetObj = new StringBuilder();
        PbReportQuery repQuery = new PbReportQuery();

        for (int k = 0; k < rowViewbys.size(); k++) {
            columnIds.add("A_" + rowViewbys.get(k));
            columnNames.add(collect.reportParamNames.get(k));
        }
        for (int l = 0; l < container.getTableMeasure().size(); l++) {
            columnIds.add("A_" + container.getTableMeasure().get(l));
            columnNames.add(container.getTableMeasureNames().get(l));
        }
        repQuery.setColViewbyCols(colViewbys);
        repQuery.setRowViewbyCols(rowViewbys);
        repQuery.setParamValue(collect.reportParametersValues);
        repQuery.setQryColumns(qryCols);
        repQuery.setColAggration(qryAggrs);
        repQuery.setTimeDetails(timeDetailsArray);
        retObj = repQuery.getPbReturnObject((String) qryCols.get(0));
        xmlRetObj.append("<RETURNOBJECT>").append("<COLUMN_DETAILS>");
        for (int i = 0; i < columnIds.size(); i++) {
            xmlRetObj.append("<COLUMN>");
            xmlRetObj.append("<COLUMN_NAME>").append(columnNames.get(i)).append("</COLUMN_NAME>");
            xmlRetObj.append("<COLUMN_ID>").append(columnIds.get(i)).append("</COLUMN_ID>");
            xmlRetObj.append("</COLUMN>");

        }
        xmlRetObj.append("</COLUMN_DETAILS>");
        for (int m = 0; m < rowViewbys.size(); m++) {
            xmlRetObj.append("<DIMENSION>");
            xmlRetObj.append("<DIMENSION_ID>A_").append(rowViewbys.get(m)).append("</DIMENSION_ID>");
            xmlRetObj.append("<DIMENSION_NAME>").append(collect.reportParamNames.get(m)).append("</DIMENSION_NAME>");
            xmlRetObj.append("</DIMENSION>");
        }
        for (int n = 0; n < container.getTableMeasure().size(); n++) {
            xmlRetObj.append("<MEASURE>");
            xmlRetObj.append("<MEASURE_ID>A_").append(container.getTableMeasure().get(n)).append("</MEASURE_ID>");
            xmlRetObj.append("<MEASURE_NAME>").append(container.getTableMeasureNames().get(n)).append("</MEASURE_NAME>");
            xmlRetObj.append("</MEASURE>");
        }


        xmlRetObj.append("<BODY>");
        for (int j = 0; j < retObj.getRowCount(); j++) {
            xmlRetObj.append("<ROW>");
            for (int k = 0; k < columnIds.size(); k++) {
                xmlRetObj.append("<COLUMN_DATA>").append(retObj.getFieldValueString(j, k).replace("&", "&amp;")).append("</COLUMN_DATA>");
            }
            xmlRetObj.append("</ROW>");
        }
        xmlRetObj.append("</BODY>").append("</RETURNOBJECT>");
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId), "xml");
            Writer writer = swt.writer;
            writer.write(xmlRetObj.toString());
            writer.flush();
            writer.close();
            swt.setReportName(collect.reportName);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;

    }

    public String generateAndStoreHtmlSnapshot(Container container, String userId, String from) throws ParseException {
        // Start of code by Nazneen for hidding measures during Excel download report
//            ArrayList cols = (ArrayList) container.getDisplayColumns().clone();
//                ArrayList disCols = (ArrayList) container.getDisplayLabels().clone();
//                ArrayList dTypes = (ArrayList) container.getDataTypes().clone();
//                 ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
//                 HashMap<String, String> crosstabMeasureId = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
//                             if (container.isReportCrosstab() || crosstabMeasureId!=null && !crosstabMeasureId.isEmpty()) {
//                                    String eleIdss = "";
//                            for (Object hiddenCol : hiddenCols) {
//                                     for(int i=0;i<cols.size();i++){
//                                         eleIdss = crosstabMeasureId.get(cols.get(i));
//                                         if(eleIdss!=null && !eleIdss.equalsIgnoreCase("null") && !eleIdss.equalsIgnoreCase("")){
//                                         if(hiddenCols.contains(eleIdss.replace("A_", ""))){
//                                             cols.remove(i);
//                                            disCols.remove(i);
//                                            dTypes.remove(i);
//                                         }}
//                                     }
//                                 }
//                             } else {
//                              for (Object hiddenCol : hiddenCols) {
//                                int index = cols.indexOf("A_" + hiddenCol.toString());
//                                if (index != -1) {
//                                    cols.remove(index);
//                                    disCols.remove(index);
//                                    dTypes.remove(index);
//                                }
//                            }}
//                 container.setDisplayLabels(disCols);
//            container.setDisplayColumns(cols);
//            container.setDisplayTypes(dTypes);
//            //Added By Amar to hidden measure while applying sorting
//            container.setDataTypes(dTypes);
//            //end of code
//// End of code by Nazneen for hidding measures during Excel download report
        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        tableBldr.setHtmlCellHeight(this.htmlCellHeight);
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        displayHelper = new HtmlTableHeaderDisplay(tableBldr);
        displayHelper.setHeadlineflag(from);
        bodyHelper = new HtmlTableBodyDisplay(tableBldr);
        subTotalHelper = new HtmlTableSubTotalDisplay(tableBldr);
        ServletWriterTransferObject swt = null;
        if (from.equalsIgnoreCase("fromdynamicheadline")) {
            displayHelper.setNext(bodyHelper).setNext(subTotalHelper);
            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(displayHelper.generateOutputHTML());
            return tableHtml.toString();
        } else {
            try {
                if (from.equalsIgnoreCase("dailyScheduleReport")) {
                    swt = ServletUtilities.createBufferedWriter(this.getScheduleFileName(), "html");
                } else {
                    swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId), "html");
                }
                Writer writer = swt.writer;
                if (this.paramType != null) {
                    String reportHTML = "";
                    if (this.paramType.equalsIgnoreCase("withParameters")) {
                        reportHTML = generateReportInfo(container);
                    } else {
                        reportHTML = generateReportInfowithoutParam(container);
                    }
                    writer.write(reportHTML);
                    writer.flush();
                } else {
                    String reportHTML = generateReportInfo(container);
                    writer.write(reportHTML);
                    writer.flush();
                }

                displayHelper.setWriter(writer);
                displayHelper.setNext(bodyHelper).setNext(subTotalHelper);

                StringBuilder tableHtml = new StringBuilder();
                tableHtml.append(displayHelper.generateOutputHTML());

                writer.write(tableHtml.toString());
                writer.flush();
                writer.close();
                swt.setReportName(collect.reportName);

            } catch (IOException ex) {
                //code Added By Amar
                if (container.getReportCollect().getLogReadWriterObject() != null) {
                    StringWriter str = new StringWriter();
                    PrintWriter writer = new PrintWriter(str);
//                    ex.printStackTrace(writer);
                    container.getReportCollect().setLogBoolean(true);
                    try {
                        container.getReportCollect().getLogReadWriterObject().fileWriter(str.getBuffer().toString());
                    } catch (IOException eo) {
                    }
                }//end of code
                logger.error("Exception:", ex);
            }
            return swt.fileName;
        }
    }

    public void addSliceToSnapshot(Container container, String userId, String fileName) {
        PbReportCollection collect = container.getReportCollect();

        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        displayHelper = new HtmlTableHeaderDisplay(tableBldr);
        bodyHelper = new HtmlTableBodyDisplay(tableBldr);
        subTotalHelper = new HtmlTableSubTotalDisplay(tableBldr);
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.getBufferedWriterForAppend(fileName);
            Writer writer = swt.writer;

            displayHelper.setWriter(writer);
            displayHelper.setNext(bodyHelper).setNext(subTotalHelper);

            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append("</table><br><table>");
            tableHtml.append(displayHelper.generateOutputHTML());
            tableHtml.append("</table><br>");
            writer.write(tableHtml.toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

    private String generateJS() {
        StringBuilder jsBuilder = new StringBuilder("");
        jsBuilder.append("");
        jsBuilder.append("<script>");
        jsBuilder.append("function showParams() {");
        jsBuilder.append("document.getElementById('paramsTable').style.display='';");
        jsBuilder.append("}");
        jsBuilder.append("function hideParams() {");
        jsBuilder.append("document.getElementById('paramsTable').style.display='none';");
        jsBuilder.append("}");
        jsBuilder.append("</script>");
        jsBuilder.append("");
        return jsBuilder.toString();
    }

    private String generateReportInfo(Container container) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
        ArrayList timeDetailsArray = null;
        ArrayList<String> paramNames = new ArrayList<String>();
        sdf.applyPattern("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        String headerTitle = dataSnapshotDAO.getHeaderTitleForSnapshot();
        timeDetailsArray = container.getTimeDetailsArray();

        StringBuilder repInfoBuilder = new StringBuilder("");
        repInfoBuilder.append(this.generateJS());
        repInfoBuilder.append("<table id='parameterRegion' style='background-color:#26466D;color:white;width:50%'>");
        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td colspan=\"2\" align='center'><h2 >").append(headerTitle).append("</h2></td>");
        repInfoBuilder.append("</tr>");

        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td style='width:50%;font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Report Title: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(container.getReportName().replace("_", " ")).append("</b></td>");
        repInfoBuilder.append("</tr>");

        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Created Date: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(sdf1.format(calendar.getTime())).append("</b></td>");
        repInfoBuilder.append("</tr>");
        if (timeDetailsArray != null && !timeDetailsArray.isEmpty()) {
            if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                String date1 = timeDetailsArray.get(2).toString().trim();
                Date date11 = sdf2.parse(date1);
                String dateFormattedDate1 = sdf1.format(date11);
//                if(timeDetailsArray.get(4).toString().equalsIgnoreCase("Week")){
//                repInfoBuilder.append("<tr>");
//                repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>StartDate: </b> </td><td style='font-size:14px;'>").append(timeDetailsArray.get(2)).append("</td>");
//                repInfoBuilder.append("</tr>");
//                repInfoBuilder.append("<tr>");
//                repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>EndDate : </b> </td><td style='font-size:14px;'>").append(timeDetailsArray.get(3)).append("</td>");
//                repInfoBuilder.append("</tr>");
//                 repInfoBuilder.append("<tr>");
//                repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Duration : </b> </td><td style='font-size:14px;'>").append(timeDetailsArray.get(4)).append("</td>");
//                repInfoBuilder.append("</tr>");
//                repInfoBuilder.append("<tr>");
//                repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Compare with: </b> </td><td style='font-size:14px;'>").append(timeDetailsArray.get(5)).append("</td>");
//                repInfoBuilder.append("</tr>");
//             }
//                else{
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b> Date: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(dateFormattedDate1).append("</b></td>");
                repInfoBuilder.append("</tr>");
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b> Duration: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(timeDetailsArray.get(3)).append("</b></td>");
                repInfoBuilder.append("</tr>");
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Compare With: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(timeDetailsArray.get(4)).append("</b></td>");
                repInfoBuilder.append("</tr>");

                //}
            }
            if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                String date1 = timeDetailsArray.get(2).toString().trim();
                String date2 = timeDetailsArray.get(3).toString().trim();
                String date3 = timeDetailsArray.get(4).toString().trim();
                String date4 = timeDetailsArray.get(5).toString().trim();
                Date date11 = sdf2.parse(date1);
                Date date12 = sdf2.parse(date2);
                Date date13 = sdf2.parse(date3);
                Date date14 = sdf2.parse(date4);
                String dateFormattedDate1 = sdf1.format(date11);
                String dateFormattedDate2 = sdf1.format(date12);
                String dateFormattedDate3 = sdf1.format(date13);
                String dateFormattedDate4 = sdf1.format(date14);
//                timeDetailsArray.add(2, dateFormattedDate1);
//                timeDetailsArray.add(3, dateFormattedDate2);
//                timeDetailsArray.add(4, dateFormattedDate3);
//                timeDetailsArray.add(5, dateFormattedDate4);
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Start Date: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(dateFormattedDate1).append("</b></td>");
                repInfoBuilder.append("</tr>");
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>End Date: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(dateFormattedDate2).append("</b></td>");
                repInfoBuilder.append("</tr>");
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>CompareFromDate: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(dateFormattedDate3).append("</b></td>");
                repInfoBuilder.append("</tr>");
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>CompareToDate: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(dateFormattedDate4).append("</b></td>");
                repInfoBuilder.append("</tr>");
            }
        }
        ArrayList filterValues = new ArrayList();
        String viewbyvalues = "";
        String[] values = new String[container.getReportCollect().paramValueList.size()];
        if (container.getReportCollect().paramValueList != null) {
            for (int k = 0; k < container.getReportCollect().paramValueList.size(); k++) {
                values = container.getReportCollect().paramValueList.get(k).toString().split(":");

                viewbyvalues = values[1];

                if (!viewbyvalues.contains("[All]")) {
                    String value = (String) container.getReportCollect().paramValueList.get(k);
                    if (!filterValues.contains(value)) {
                        filterValues.add(value);
                    }
                }
            }
        }

        ArrayList<String> valu = new ArrayList<String>();
        valu.add(container.getReportName().replace("_", " "));
        valu.add(sdf.format(calendar.getTime()));
//            if(!timeDetailsArray.isEmpty()){
//                for(int i=0;i<valu.size();i++){
//                    if(timeDetailsArray.contains(i))
//                    timeDetailsArray.remove(valu.get(i));
//                }
//            }



        if (filterValues != null) {
            for (int i = 0; i < filterValues.size(); i++) {
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 14px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Filter Applied On: </b> </td><td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>").append(filterValues.get(i)).append("</b></td>");
                repInfoBuilder.append("</tr>");
            }
        }

        repInfoBuilder.append("</table>");
        repInfoBuilder.append("<br/><br/>");
        //the below code commented for hiding paramters and values while exporting report as html format

//        repInfoBuilder.append("<a style='cursor:pointer' onclick='showParams()'><u>Show Parameters</u></a>");
////        repInfoBuilder.append("<input type=button  class='navtitle-hover' value='Show Parameters' onClick='showParams()'>");
//        repInfoBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;<a style='cursor:pointer' onclick=hideParams()><u>Hide Parameters</u></a>");
////        repInfoBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;<input type=button  class='navtitle-hover' value='Hide Parameters' onClick='hideParams()'>");
//        repInfoBuilder.append("<br/>");
//
//        //repInfoBuilder.append("<div style='display:none;' id='paramsDiv'>");
//       // 
//        paramNames = (ArrayList) container.getParametersHashMap().get("ParametersNames");
//
//        Set<ReportParameterValue> repParamSet = container.getReportParameter().getReportParameterValues();
//        Iterator<ReportParameterValue> it = repParamSet.iterator();
//        repInfoBuilder.append("<br/>");
//        repInfoBuilder.append("<table border='1' style='display:none;-moz-border-radius: 6px 6px 6px 6px;border-collapse: collapse; padding: 1em;' id='paramsTable' >");
//        repInfoBuilder.append("<tr>");
//        repInfoBuilder.append("<td style= 'font-size:14px;background-color:#B4D9EE;'>Parameter Names</td>");
//        for (int i = 0; i < paramNames.size(); i++) {
//            repInfoBuilder.append("<td style='font-size: 12px'><b>");
//            repInfoBuilder.append(paramNames.get(i));
//            repInfoBuilder.append("</b></td>");
//        }
//        repInfoBuilder.append("</tr>");
//        repInfoBuilder.append("<tr>");
//        repInfoBuilder.append("<td style='font-size:14px;background-color:#B4D9EE;'>Parameter Values</td>");
//        while (it.hasNext()) {
//            repInfoBuilder.append("<td>");
//            repInfoBuilder.append(it.next().getParameterValues());
//            repInfoBuilder.append("</td>");
//        }
//        repInfoBuilder.append("</tr>");
//
//        repInfoBuilder.append("</table>");
//        //  repInfoBuilder.append("</div>");
//
//        repInfoBuilder.append("<br/>");
        return repInfoBuilder.toString();
    }

    public String generateAndStoreCSVSnapshot(Container container, String userId, String csvNtopar) {
        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        ArrayList<String> timedetails = new ArrayList<String>();
        ArrayList<String> parameterDertails = new ArrayList<String>();
        if (!csvNtopar.equalsIgnoreCase("CSN")) {
            timedetails = container.getTimeDetailsArray();
            //if(timedetails.size()<6){
            if (!timedetails.contains(container.getReportDesc())) {
                timedetails.add(container.getReportDesc());
                SimpleDateFormat sdf = new SimpleDateFormat();
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                sdf.applyPattern("MM/dd/yyyy");
                timedetails.add(sdf1.format(Calendar.getInstance().getTime()));
            }
            String viewbyvalues = "";
            String[] values = new String[collect.paramValueList.size()];
            if (collect.paramValueList != null) {
                for (int k = 0; k < collect.paramValueList.size(); k++) {
                    values = collect.paramValueList.get(k).toString().split(":");

                    viewbyvalues = values[1];

                    if (!viewbyvalues.equalsIgnoreCase("[All]")) {
                        String value = (String) collect.paramValueList.get(k);
                        if (!parameterDertails.contains(value)) {
                            parameterDertails.add(value);
                        }
                    }
                }
            }
        } else {
            timedetails.add("wihtoutParam");
            if (!timedetails.contains(container.getReportDesc())) {
                timedetails.add(container.getReportDesc());
                SimpleDateFormat sdf = new SimpleDateFormat();
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                sdf.applyPattern("MM/dd/yyyy");
                timedetails.add(sdf1.format(Calendar.getInstance().getTime()));
            }
        }
        displayHelper = new CSVTableHeaderDisplay(tableBldr, timedetails, parameterDertails);
        bodyHelper = new CSVTableBodyDisplay(tableBldr);
        subTotalHelper = new CSVTableSubTotalDisplay(tableBldr);
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId), "csv");
            Writer writer = swt.writer;
//                String reportHTML=generateReportInfo(container);
//                writer.write(reportHTML);
//                writer.flush();

            displayHelper.setWriter(writer);
            displayHelper.setNext(bodyHelper).setNext(subTotalHelper);

            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(displayHelper.generateOutputHTML());
            writer.write(tableHtml.toString());
            writer.flush();
            writer.close();
            swt.setReportName(collect.reportName);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;
    }

    public String generateAndStoreCSVSnapshotXtend(Container container, String userId, String csvNtopar) {
        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        ArrayList<String> timedetails = new ArrayList<String>();
        ArrayList<String> parameterDertails = new ArrayList<String>();
        if (!csvNtopar.equalsIgnoreCase("CSN")) {
            timedetails = container.getTimeDetailsArray();
            //if(timedetails.size()<6){
            if (!timedetails.contains(container.getReportDesc())) {
                timedetails.add(container.getReportDesc());
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("MM/dd/yyyy");
                timedetails.add(sdf.format(Calendar.getInstance().getTime()));
            }
            String viewbyvalues = "";
            String[] values = new String[collect.paramValueList.size()];
            if (collect.paramValueList != null) {
                for (int k = 0; k < collect.paramValueList.size(); k++) {
                    values = collect.paramValueList.get(k).toString().split(":");

                    viewbyvalues = values[1];

                    if (!viewbyvalues.equalsIgnoreCase("[All]")) {
                        String value = (String) collect.paramValueList.get(k);
                        if (!parameterDertails.contains(value)) {
                            parameterDertails.add(value);
                        }
                    }
                }
            }
        } else {
            timedetails.add("wihtoutParam");
            if (!timedetails.contains(container.getReportDesc())) {
                timedetails.add(container.getReportDesc());
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("MM/dd/yyyy");
                timedetails.add(sdf.format(Calendar.getInstance().getTime()));
            }
        }
//        displayHelper = new XtendCsvHeaderHelper(tableBldr, timedetails,parameterDertails);
//        bodyHelper = new CSVTableBodyDisplay(tableBldr);
//        subTotalHelper = new CSVTableSubTotalDisplay(tableBldr);
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId), "csv");
            String srt = this.getFileName(reportId, userId);
            Writer writer = swt.writer;
//                String reportHTML=generateReportInfo(container);
//                writer.write(reportHTML);
//                writer.flush();
//
//             String folderName1 = getFolderName(reportId);
            ProgenDataSet pbretObj = container.getRetObj();
            File file = new File(srt);
//        FileWriter fw;
//            fw = new FileWriter(file);
            for (int m = 0; m < pbretObj.rowCount; m++) {
                String br = "";
                if (m == 0) {
                    for (int n = 0; n < pbretObj.getColumnNames().length; n++) {
                        if (n != 0) {
                            br += ",";
                        }
                        br += (container.getDisplayLabels().get(n)).toString().replace(",", "");
                    }

//                    br += System.lineSeparator();
                    br += "/";
                }
                for (int k = 0; k < pbretObj.getColumnNames().length; k++) {
                    if (k != 0) {
                        br += ",";
                    }
                    br += (pbretObj.getFieldValueString(m, k)).toString().replace(",", "");
                    if (k == pbretObj.getColumnNames().length - 1) {
//                        br += System.lineSeparator();
                        br += "/";
                    }
                }
                writer.append(br);
            }
            writer.flush();
            writer.close();




//            displayHelper.setWriter(writer);
//            displayHelper.setNext(bodyHelper);
////                    .setNext(subTotalHelper);
//
//            StringBuilder tableHtml = new StringBuilder();
//            tableHtml.append(displayHelper.generateOutputHTML());
//            writer.write(tableHtml.toString());
//            writer.flush();
//            writer.close();
            swt.setReportName(collect.reportName);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;
    }

    public String generateAndStoreTSSnapshot(Container container, String userId) {
        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;

        ArrayList<String> timedetails = new ArrayList<String>();

        timedetails = container.getTimeDetailsArray();
//        if(timedetails.size()<6){
        if (!timedetails.contains(container.getReportDesc())) {
            timedetails.add(container.getReportDesc());
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("MM/dd/yyyy");
            timedetails.add(sdf.format(Calendar.getInstance().getTime()));
        }
        String viewbyvalues = "";
        String[] values = new String[collect.paramValueList.size()];
        if (collect.paramValueList != null) {
            for (int k = 0; k < collect.paramValueList.size(); k++) {
                values = collect.paramValueList.get(k).toString().split(":");

                viewbyvalues = values[1];

                if (!viewbyvalues.equalsIgnoreCase("All")) {
                    String value = (String) collect.paramValueList.get(k);
                    if (!timedetails.contains(value)) {
                        timedetails.add(value);
                    }
                }
            }
        }

        displayHelper = new TSTableHeaderDisplay(tableBldr, timedetails);
        bodyHelper = new TSTableBodyDisplay(tableBldr);
        subTotalHelper = new TSTableSubTotalDisplay(tableBldr);
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId), "txt");
            Writer writer = swt.writer;
//                String reportHTML=generateReportInfo(container);
//                writer.write(reportHTML);
//                writer.flush();

            displayHelper.setWriter(writer);
            displayHelper.setNext(bodyHelper).setNext(subTotalHelper);

            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(displayHelper.generateOutputHTML());
            writer.write(tableHtml.toString());
            writer.flush();
            writer.close();
            swt.setReportName(collect.reportName);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;
    }

    public String generateAndStoreCDSnapshot(Container container, String userId) {

        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;

        ArrayList<String> timedetails = new ArrayList<String>();

        timedetails = container.getTimeDetailsArray();
//        if(timedetails.size()<6){
        if (!timedetails.contains(container.getReportDesc())) {
            timedetails.add(container.getReportDesc());
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("MM/dd/yyyy");
            timedetails.add(sdf.format(Calendar.getInstance().getTime()));
        }
        String viewbyvalues = "";
        String[] values = new String[collect.paramValueList.size()];
        if (collect.paramValueList != null) {
            for (int k = 0; k < collect.paramValueList.size(); k++) {
                values = collect.paramValueList.get(k).toString().split(":");

                viewbyvalues = values[1];

                if (!viewbyvalues.equalsIgnoreCase("All")) {
                    String value = (String) collect.paramValueList.get(k);
                    if (!timedetails.contains(value)) {
                        timedetails.add(value);
                    }
                }
            }
        }

        displayHelper = new CDTableHeaderDisplay(tableBldr, timedetails);
        displayHelper.setDelimiter(delimiter);
        displayHelper.setTextIdentifier(textIdentifier);
        bodyHelper = new CDTableBodyDisplay(tableBldr);
        bodyHelper.setDelimiter(delimiter);
        bodyHelper.setTextIdentifier(textIdentifier);
        subTotalHelper = new CDTableSubTotalDisplay(tableBldr);
        subTotalHelper.setDelimiter(delimiter);
        subTotalHelper.setTextIdentifier(textIdentifier);
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId), "txt");
            Writer writer = swt.writer;
//                String reportHTML=generateReportInfo(container);
//                writer.write(reportHTML);
//                writer.flush();

            displayHelper.setWriter(writer);
            displayHelper.setNext(bodyHelper).setNext(subTotalHelper);

            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(displayHelper.generateOutputHTML());
            writer.write(tableHtml.toString());
            writer.flush();
            writer.close();
            swt.setReportName(collect.reportName);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getTextIdentifier() {
        return textIdentifier;
    }

    public void setTextIdentifier(String textIdentifier) {
        this.textIdentifier = textIdentifier;
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

    public String generateAndStoreXMLSnapshot(Container container, String userId) {
        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        displayHelper = new XMLTableHeaderDisplay(tableBldr);
        bodyHelper = new XMLTableBodyDisplay(tableBldr);
        subTotalHelper = new XMLTableSubTotalDisplay(tableBldr);
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId), "xml");
            Writer writer = swt.writer;
//                String reportHTML=generateReportInfo(container);
//                writer.write(reportHTML);
//                writer.flush();

            displayHelper.setWriter(writer);
            displayHelper.setNext(bodyHelper).setNext(subTotalHelper);

            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(displayHelper.generateOutputHTML());
            writer.write(tableHtml.toString());
            writer.flush();
            writer.close();
            swt.setReportName(collect.reportName);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;
    }

    public ArrayList<String> generateKPIHtmlFile(String[] reportIds, String userId, String KPIHtml) {

        ArrayList<String> filenames = new ArrayList<String>();

        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter("kpi" + reportIds[0], "html");
            Writer writer = swt.writer;

            writer.write(KPIHtml);

            writer.flush();
            writer.close();
            swt.setReportName("kpi" + reportIds[0]);
            filenames.add(swt.fileName);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

//        return swt.fileName;
        return filenames;
    }

    public String generateReportInfoPdf(Container container) {                        //for exporting Reprot as PDF Format in Scheduler

        ProgenDataSet dataset = container.getRetObj();
        //dataset.resetViewSequence();
        ArrayList<String> cols = new ArrayList<String>();
        cols = container.getDisplayColumns();

        ArrayList<String> dTypes = new ArrayList<String>();
        dTypes = container.getDataTypes();
        ArrayList<String> rowView = new ArrayList<String>();
//      //  Start of code by Manik for hidding measures during share report
//         //ArrayList disCols = new ArrayList();
//         ArrayList<String> disCols = container.getDisplayLabels();
//          disCols = container.getDisplayLabels();
//                            ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
//                            for (Object hiddenCol : hiddenCols) {
//                                int index = cols.indexOf("A_" + hiddenCol.toString());
//                                if (index != -1) {
//                                    cols.remove(index);
//                                    disCols.remove(index);
//                                    dTypes.remove(index);
//                                }
//                            }
//       // End of code by Manik for hidding measures during share report
        //  Start of code by Nazneen for hidding measures during Excel download report
// Start of code by Nazneen for hidding measures during Excel download report
        // ArrayList cols = (ArrayList) container.getDisplayColumns().clone();
        ArrayList disCols = (ArrayList) container.getDisplayLabels().clone();
        //   ArrayList dTypes = (ArrayList) container.getDataTypes().clone();
        ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
        HashMap<String, String> crosstabMeasureId2 = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        if (container.isReportCrosstab() || crosstabMeasureId2 != null && !crosstabMeasureId2.isEmpty()) {
            String eleIdss = "";
            for (Object hiddenCol : hiddenCols) {
                for (int i = 0; i < cols.size(); i++) {
                    eleIdss = crosstabMeasureId2.get(cols.get(i));
                    if (eleIdss != null && !eleIdss.equalsIgnoreCase("null") && !eleIdss.equalsIgnoreCase("")) {
                        if (hiddenCols.contains(eleIdss.replace("A_", ""))) {
                            cols.remove(i);
                            disCols.remove(i);
                            dTypes.remove(i);
                        }
                    }
                }
            }
        } else {
            for (Object hiddenCol : hiddenCols) {
                int index = cols.indexOf("A_" + hiddenCol.toString());
                if (index != -1) {
                    cols.remove(index);
                    disCols.remove(index);
                    dTypes.remove(index);
                }
            }
        }
        container.setDisplayLabels(disCols);
        container.setDisplayColumns(cols);
        container.setDisplayTypes(dTypes);
        //Added By Amar to hidden measure while applying sorting
        container.setDataTypes(dTypes);
        //end of code
        // End of code by Nazneen for hidding measures during Excel download report
        // End of code by Nazneen for hidding measures during Excel download report

        String[] displayLabels = new String[dTypes.size()];
        for (int i = 0; i < dTypes.size(); i++) {
            displayLabels[i] = String.valueOf(disCols.get(i));
        }

        SimpleDateFormat sdf = new SimpleDateFormat();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");

        ArrayList<String> paramNames = new ArrayList<String>();
        sdf.applyPattern("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        String headerTitle = dataSnapshotDAO.getHeaderTitleForSnapshot();
        ArrayList timeDetailsArray = container.getTimeDetailsArray();

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateNow = formatter.format(currentDate.getTime());

        Document document = null;
        document = new Document(PageSize.B2, 50, 50, 50, 50);
        // ByteArrayOutputStream bos = null;
        PdfWriter writer = null;
        String reportId = container.getReportCollect().reportId;

        ServletWriterTransferObject swt = null;

        //new FileOutputStream(swt.fileName)
        //String fileName = dateNow+"_"+container.getReportCollect().reportId+"_"+timeDetailsArray.get(2).toString().replace("/", ":")+".pdf";
        try {
            if (this.getScheduleFileName() != null) {
                swt = ServletUtilities.createBufferedWriter(this.getScheduleFileName(), "pdf");
            } else {
                swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId), "pdf");
            }

            // bos = new ByteArrayOutputStream();
            writer = PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName));
            document.open();

            document.add(new Paragraph(dataSnapshotDAO.getHeaderTitleForSnapshot(), redFont));
            // document.add(new Paragraph(Chunk.NEWLINE));
            document.add(new Paragraph("Report Title     :" + container.getReportName().replace("_", " ")));
            document.add(new Paragraph("Created on      :" + sdf1.format(calendar.getTime())));


            ArrayList extraHeaderRows = new ArrayList();
            if (timeDetailsArray != null) {
                if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
//                      if(timeDetailsArray.get(4).toString().equalsIgnoreCase("Week")){
//                        extraHeaderRows.add("StartDate        : " + timeDetailsArray.get(2));
//                        extraHeaderRows.add("EndDate         : " + timeDetailsArray.get(3));
//                        extraHeaderRows.add("Duration          : " + timeDetailsArray.get(4));
//                        extraHeaderRows.add("Compare with  : " + timeDetailsArray.get(5));
//                        }
//                        else{
                    String date1 = timeDetailsArray.get(2).toString().trim();
                    Date date11 = sdf2.parse(date1);
                    String dateFormattedDate1 = sdf1.format(date11);

                    extraHeaderRows.add("Date                 : " + dateFormattedDate1);
                    extraHeaderRows.add("Duration           : " + timeDetailsArray.get(3));
                    extraHeaderRows.add("Compare with   : " + timeDetailsArray.get(4));
                    //  }
                } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    String date1 = timeDetailsArray.get(2).toString().trim();
                    String date2 = timeDetailsArray.get(3).toString().trim();
                    String date3 = timeDetailsArray.get(4).toString().trim();
                    String date4 = timeDetailsArray.get(5).toString().trim();
                    Date date11 = sdf2.parse(date1);
                    Date date12 = sdf2.parse(date2);
                    Date date13 = sdf2.parse(date3);
                    Date date14 = sdf2.parse(date4);
                    String dateFormattedDate1 = sdf1.format(date11);
                    String dateFormattedDate2 = sdf1.format(date12);
                    String dateFormattedDate3 = sdf1.format(date13);
                    String dateFormattedDate4 = sdf1.format(date14);
                    extraHeaderRows.add("StartDate        : " + dateFormattedDate1);
                    extraHeaderRows.add("EndDate          : " + dateFormattedDate2);
                    extraHeaderRows.add("Duration          : " + dateFormattedDate3);
                    extraHeaderRows.add("Compare with  : " + dateFormattedDate4);
                }
            }
            for (int i = 0; i < extraHeaderRows.size(); i++) {
                document.add(new Paragraph("" + extraHeaderRows.get(i)));
            }
            document.add(new Paragraph(Chunk.NEWLINE));
            document.add(new Paragraph(Chunk.NEWLINE));

            PdfPTable table = null;
            PdfPCell cell = null;
            NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
            nFormat.setMaximumFractionDigits(1);
            nFormat.setMinimumFractionDigits(1);

            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            int columnCount = disCols.size();
            float colWidth = width / columnCount;
            float[] columnDefinitionSize = new float[columnCount];

            for (int i = 0; i < columnCount; i++) {
                columnDefinitionSize[i] = 100.0f;
            }

            table = new PdfPTable(columnDefinitionSize);
            table.setSpacingBefore(10.0f);
            table.getDefaultCell().setBorder(1);
            table.setTotalWidth(100.0f * disCols.size());
            table.setLockedWidth(false);
            table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);

            for (int i = 0; i < columnCount; i++) {
                cell = new PdfPCell(new Phrase(displayLabels[i], fontHead));
                cell.setBackgroundColor(Color.lightGray);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setNoWrap(false);
                table.addCell(cell);

            }
            //added by sruthi for share pdf 24/01/2015
            DataFacade facade = new DataFacade(container);
            TableBuilder tableBldr = new RowViewTableBuilder(facade);
            if (container.getViewByCount() > 1) {
                int fromrow = 0;
                int torow = dataset.getViewSequence().size();
                TableDisplay bodyHelper = null;
                TableDisplay subTotalHelper = null;
                tableBldr.setFromAndToRow(fromrow, torow);
                bodyHelper = new PdfTableBodyDisplay(tableBldr);
                bodyHelper.setHeadlineflag("Export");
                bodyHelper.setPdfTable(table);
                subTotalHelper = new PdfTableSubTotalDisplay(tableBldr);
                subTotalHelper.setPdfTable(table);
                bodyHelper.setNext(subTotalHelper);
                StringBuilder tableHtml = new StringBuilder();
                tableHtml.append(bodyHelper.generateOutputHTML());
            } else {    //ended by sruthi
                for (int i = 0; i < dataset.getViewSequence().size(); i++) {
                    for (int j = 0; j < columnCount; j++) {
                        String colName = cols.get(j);
//                    String discolName = disCols.get(j);
                        Color BGColor = null;
                        int PerCentColumnIndex = colName.lastIndexOf("_percentwise");
                        String formattedData = "";
                        if ("C".equals(dTypes.get(j))) {
                            cell = new PdfPCell(new Phrase(dataset.getFieldValueString(i, colName), font));
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        } else if ("N".equals(dTypes.get(j))) {
                            String snbrSymbol = "";
                            String nbrSymbol = "";
//                        String element = container.getDisplayColumns().get(j);
                            String element = colName;
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
                            HashMap colProps = container.getColumnProperties();
                            ArrayList propList;
                            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                            } else {
                                propList = (ArrayList) colProps.get(element);
                            }
                            if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                                String colSymbol = (String) propList.get(7);
                                if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                                    container.symbol.put(element, colSymbol);
                                }
                            }
//                      snbrSymbol = container.symbol.get(container.getDisplayColumns().get(j));
                            snbrSymbol = container.symbol.get(colName);
                            int precision = container.getRoundPrecisionForMeasure(element);
                            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                            }
                            if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                                if ((nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) && (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase(""))) {
                                    if (snbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(i, colName), nbrSymbol, precision) + "" + snbrSymbol;
                                    } else {
                                        formattedData = snbrSymbol + "" + NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(i, colName), nbrSymbol, precision);
                                    }
                                } else if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(i, colName), nbrSymbol, precision);
                                } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                    if (snbrSymbol.equalsIgnoreCase("%")) {
                                        formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(i, colName), nbrSymbol, precision) + "" + snbrSymbol;
                                    } else {
                                        formattedData = snbrSymbol + "" + NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(i, colName), nbrSymbol, precision);
                                    }
                                } else {
                                    formattedData = dataset.getFieldValueBigDecimal(i, colName).toString();
                                }
                                cell = new PdfPCell(new Phrase(formattedData, font));

                            } else {
                                if (RTMeasureElement.getMeasureType(colName) == RTMeasureElement.PERCENTWISE) {
//                            colName = colName.replace("_percentwise", "");
//                            cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueBigDecimal(i, colName).divide(dataset.getColumnGrandTotalValue(colName), MathContext.DECIMAL64).multiply(new BigDecimal(100))), font));
                                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueRuntimeMeasure(i, colName)), font));
                                } else if (RTMeasureElement.getMeasureType(colName) == RTMeasureElement.RANK) {
                                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueRuntimeMeasure(i, colName)), font));
                                } else if (RTMeasureElement.getMeasureType(colName) == RTMeasureElement.RUNNINGTOTAL) {
                                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueRuntimeMeasure(i, colName)), font));
                                } else if (RTMeasureElement.getMeasureType(colName) == RTMeasureElement.GOALSEEK) {
                                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueRuntimeMeasure(i, colName)), font));
                                } else if (RTMeasureElement.getMeasureType(colName) == RTMeasureElement.USERGOALSEEK) {
                                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueRuntimeMeasure(i, colName)), font));
                                } else if (RTMeasureElement.getMeasureType(colName) == RTMeasureElement.TIMEBASED) {
                                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueRuntimeMeasure(i, colName)), font));
                                } else if (RTMeasureElement.getMeasureType(colName) == RTMeasureElement.TIMECHANGEDPER) {
                                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueRuntimeMeasure(i, colName)), font));
                                } else if (RTMeasureElement.getMeasureType(colName) == RTMeasureElement.USERGOALPERCENT) {
                                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getFieldValueRuntimeMeasure(i, colName)), font));
                                }
                            }
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

                        } else {
                            cell = new PdfPCell(new Phrase(dataset.getFieldValueString(i, colName), font));
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                        }
                        cell.setNoWrap(false);
                        table.addCell(cell);

                        colName = null;
                    }
                }
            }
            //display grandTotal
            for (int i = 0; i < columnCount; i++) {
                String colName = cols.get(i);
                if ("C".equals(dTypes.get(i))) {
                    cell = new PdfPCell(new Phrase("Grand Total", font));
                } else if ("N".equals(dTypes.get(i))) {
                    String formattedData = "";
                    String snbrSymbol = "";
                    String nbrSymbol = "";
//                        String element = container.getDisplayColumns().get(i);
                    String element = colName;
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
                    HashMap colProps = container.getColumnProperties();
                    ArrayList propList;
                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                        propList = (ArrayList) colProps.get(crosstabMeasureId.get(element));
                    } else {
                        propList = (ArrayList) colProps.get(element);
                    }
                    if ((colProps != null && colProps.containsKey(element)) || (colProps != null && colProps.containsKey(crosstabMeasureId.get(element)))) {
                        String colSymbol = (String) propList.get(7);
                        if (!("N".equalsIgnoreCase(colSymbol) || "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol) || " ".equalsIgnoreCase(colSymbol))) {
                            container.symbol.put(element, colSymbol);
                        }
                    }
//                      snbrSymbol = container.symbol.get(container.getDisplayColumns().get(i));
                    snbrSymbol = container.symbol.get(colName);
                    int precision = container.getRoundPrecisionForMeasure(element);
                    if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                        precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                    }
                    if ((nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) && (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase(""))) {
                        if (snbrSymbol.equalsIgnoreCase("%")) {
                            formattedData = NumberFormatter.getModifiedNumber(dataset.getColumnGrandTotalValue(colName), nbrSymbol, precision) + "" + snbrSymbol;
                        } else {
                            formattedData = snbrSymbol + "" + NumberFormatter.getModifiedNumber(dataset.getColumnGrandTotalValue(colName), nbrSymbol, precision);
                        }
                    } else if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
                        formattedData = NumberFormatter.getModifiedNumber(dataset.getColumnGrandTotalValue(colName), nbrSymbol, precision);
                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                        if (snbrSymbol.equalsIgnoreCase("%")) {
                            formattedData = NumberFormatter.getModifiedNumber(dataset.getColumnGrandTotalValue(colName), nbrSymbol, precision) + "" + snbrSymbol;
                        } else {
                            formattedData = snbrSymbol + "" + NumberFormatter.getModifiedNumber(dataset.getColumnGrandTotalValue(colName), nbrSymbol, precision);
                        }
                    } else {
                        formattedData = dataset.getColumnGrandTotalValue(colName).toString();
                    }
                    cell = new PdfPCell(new Phrase(formattedData, font));
                } else {
                    cell = new PdfPCell(new Phrase(nFormat.format(dataset.getColumnGrandTotalValue(colName)), font));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                //System.out.println("***...grandTotal " + dataset.getColumnGrandTotalValue(colName));
                cell.setNoWrap(false);
                table.addCell(cell);

            }
            document.add(table);
            document.close();
        } catch (DocumentException de) {
            //code Added By Amar
            if (container.getReportCollect().getLogReadWriterObject() != null) {
                StringWriter str = new StringWriter();
                PrintWriter writern = new PrintWriter(str);
//                de.printStackTrace(writern);
                container.getReportCollect().setLogBoolean(true);
                try {
                    container.getReportCollect().getLogReadWriterObject().fileWriter(str.getBuffer().toString());
                } catch (IOException eo) {
                }
            }//end of code
            logger.error("Exception:", de);
        } catch (Exception e) {
            //code Added By Amar
            if (container.getReportCollect().getLogReadWriterObject() != null) {
                StringWriter str = new StringWriter();
                PrintWriter writerm = new PrintWriter(str);
//                e.printStackTrace(writerm);
                container.getReportCollect().setLogBoolean(true);
                try {
                    container.getReportCollect().getLogReadWriterObject().fileWriter(str.getBuffer().toString());
                } catch (IOException eo) {
                }
            }//end of code
            logger.error("Exception:", e);
        }

        return swt.fileName;

    }

    public String generateReportInfoExcel(Container container) {                     //for exporting Reprot as EXCEL Format in Scheduler

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
        WritableCell cell = null;
        Number n = null;
        BigDecimal multiplier = new BigDecimal("100");
        int maxRows = 65500;//65500
        int countOfSheets = 1;
        Date date = new Date();
        int rowStart = 4;
        int rowStartTarget = 5;

        ProgenDataSet dataset = container.getRetObj();
        //dataset.resetViewSequence();
        ArrayList<String> cols = new ArrayList<String>();
        cols = container.getDisplayColumns();
        ArrayList<String> dTypes = new ArrayList<String>();
        dTypes = container.getDataTypes();
        ArrayList<String> rowView = new ArrayList<String>();
        int viewByCnt = container.getViewByCount();
//        //  Start of code by Manik for hidding measures during share report
//         //ArrayList disCols = new ArrayList();
//         ArrayList<String> disCols = container.getDisplayLabels();
//          disCols = container.getDisplayLabels();
//                            ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
//                            for (Object hiddenCol : hiddenCols) {
//                                int index = cols.indexOf("A_" + hiddenCol.toString());
//                                if (index != -1) {
//                                    cols.remove(index);
//                                    disCols.remove(index);
//                                    dTypes.remove(index);
//                                }
//                            }
//
//
//       // End of code by Manik for hidding measures during share report
//     //  Start of code by Nazneen for hidding measures during Excel download report
//         //ArrayList disCols = new ArrayList();
        // Start of code by Nazneen for hidding measures during Excel download report
        // ArrayList cols = (ArrayList) container.getDisplayColumns().clone();
        ArrayList disCols = (ArrayList) container.getDisplayLabels().clone();
        //   ArrayList dTypes = (ArrayList) container.getDataTypes().clone();
        ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
        HashMap<String, String> crosstabMeasureId1 = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        if (container.isReportCrosstab() || crosstabMeasureId1 != null && !crosstabMeasureId1.isEmpty()) {
            String eleIdss = "";
            for (Object hiddenCol : hiddenCols) {
                for (int i = 0; i < cols.size(); i++) {
                    eleIdss = crosstabMeasureId1.get(cols.get(i));
                    if (eleIdss != null && !eleIdss.equalsIgnoreCase("null") && !eleIdss.equalsIgnoreCase("")) {
                        if (hiddenCols.contains(eleIdss.replace("A_", ""))) {
                            cols.remove(i);
                            disCols.remove(i);
                            dTypes.remove(i);
                        }
                    }
                }
            }
        } else {
            for (Object hiddenCol : hiddenCols) {
                int index = cols.indexOf("A_" + hiddenCol.toString());
                if (index != -1) {
                    cols.remove(index);
                    disCols.remove(index);
                    dTypes.remove(index);
                }
            }
        }
        container.setDisplayLabels(disCols);
        container.setDisplayColumns(cols);
        container.setDisplayTypes(dTypes);
        //Added By Amar to hidden measure while applying sorting
        container.setDataTypes(dTypes);
        //end of code
        // End of code by Nazneen for hidding measures during Excel download report
        // End of code by Nazneen for hidding measures during Excel download report

// added by nazneen when dtypes are less that display Columns
        if (dTypes.size() < disCols.size()) {
            for (int k = 0; k < disCols.size(); k++) {
                if (dTypes.size() < disCols.size()) {
                    dTypes.add("N");
                }
            }
        }
        if (dTypes.size() > disCols.size()) {
            int dTSize = dTypes.size();
            for (int k = 0; k < dTSize; k++) {
                if (dTypes.size() > disCols.size()) {
                    dTypes.remove(dTypes.size() - 1);
                }
            }
        }
        //modified by Dinanath for getting prev-month and next month correctly
        DataFacade facade1 = new DataFacade(container);
        String[] displayLabels = new String[dTypes.size()];

        for (int i = 0; i < dTypes.size(); i++) {
            //Added by Ram for Hide Measure Headings 15Feb2016
            String dispColsSecond = "";
            if (container.isReportCrosstab()) {
                if (container.isHideMsrHeading()) {
                    String str = String.valueOf(disCols.get(i));
                    str = str.replace("[", "").replace("]", "");
                    String[] st = str.split(",");
                    int l;
                    for (l = 0; l < st.length; l++) {
                        if (!st[l].equalsIgnoreCase(" ")) {
                            break;
                        }
                    }
                    dispColsSecond = st[l];
                } else {
                    dispColsSecond = String.valueOf(disCols.get(i));
                }
            } else //end Ram code
            {
                dispColsSecond = String.valueOf(disCols.get(i));
            }
            if (dispColsSecond.contains("Prev Month")) {
                displayLabels[i] = String.valueOf(facade1.container.getMonthNameforTrailingFormulaOnColName(dispColsSecond));
            } else {
                displayLabels[i] = String.valueOf(dispColsSecond);
            }

//         displayLabels[i] = String.valueOf(disCols.get(i));
        }

        SimpleDateFormat sdf = new SimpleDateFormat();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
        ArrayList timeDetailsArray = null;
        ArrayList<String> paramNames = new ArrayList<String>();
        sdf.applyPattern("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        String headerTitle = dataSnapshotDAO.getHeaderTitleForSnapshot();
        timeDetailsArray = container.getTimeDetailsArray();

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateNow = formatter.format(currentDate.getTime());

        bos = new ByteArrayOutputStream();

        //String fileName = dateNow+"_"+container.getReportCollect().reportId+"_"+timeDetailsArray.get(2).toString().replace("/", ":")+".xls";
        columnCount = disCols.size();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        String reportId = container.getReportCollect().reportId;

        ServletWriterTransferObject swt = null;

        try {
            if (this.getScheduleFileName() != null) {
                swt = ServletUtilities.createBufferedWriter(this.getScheduleFileName(), "xls");
            } else {
                swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId), "xls");
            }

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            //Code Added By Amar
            FileOutputStream fileOut = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName);
            //end of code
            //writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);
            writableWorkbook = Workbook.createWorkbook(fileOut, wbSettings);
            // writableSheet = writableWorkbook.createSheet(container.getReportName().replace("_", " "), 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            //writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(false);

//            titleLabel = new Label(0, 0, dataSnapshotDAO.getHeaderTitleForSnapshot());
//            reportLabel = new Label(0, 1, "Report Title           : " + container.getReportName().replace("_", " "));
//            dateLabel = new Label(0, 2, "Created Date        : " + sdf.format(date));
//            System.out.println("titleLabel: "+titleLabel+"reportLabel: "+reportLabel+"dateLabel: "+dateLabel);
            ArrayList extraHeaderRows = new ArrayList();
            if (timeDetailsArray != null && timeDetailsArray.size() != 0) {
                if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
//                        if(timeDetailsArray.get(4).toString().equalsIgnoreCase("Week")){
//                        extraHeaderRows.add("StartDate                : " + timeDetailsArray.get(2));
//                        extraHeaderRows.add("EndDate                : " + timeDetailsArray.get(3));
//                        extraHeaderRows.add("Duration               : " + timeDetailsArray.get(4));
//                        extraHeaderRows.add("Compare with      : " + timeDetailsArray.get(5));
//                        }
//                        else{
                    String date1 = timeDetailsArray.get(2).toString().trim();
                    Date date11 = sdf2.parse(date1);
                    String dateFormattedDate1 = sdf1.format(date11);

                    extraHeaderRows.add("Date                        : " + dateFormattedDate1);
                    extraHeaderRows.add("Duration                 : " + timeDetailsArray.get(3));
                    extraHeaderRows.add("Compare with       : " + timeDetailsArray.get(4));
                    // }
                } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    String date1 = timeDetailsArray.get(2).toString().trim();
                    String date2 = timeDetailsArray.get(3).toString().trim();
                    String date3 = timeDetailsArray.get(4).toString().trim();
                    String date4 = timeDetailsArray.get(5).toString().trim();
                    Date date11 = sdf2.parse(date1);
                    Date date12 = sdf2.parse(date2);
                    Date date13 = sdf2.parse(date3);
                    Date date14 = sdf2.parse(date4);
                    String dateFormattedDate1 = sdf1.format(date11);
                    String dateFormattedDate2 = sdf1.format(date12);
                    String dateFormattedDate3 = sdf1.format(date13);
                    String dateFormattedDate4 = sdf1.format(date14);
                    extraHeaderRows.add("StartDate               : " + dateFormattedDate1);
                    extraHeaderRows.add("EndDate                : " + dateFormattedDate2);
                    extraHeaderRows.add("Duration                : " + dateFormattedDate3);
                    extraHeaderRows.add("Compare with       : " + dateFormattedDate4);
                }
            }

            int counter = 2;
            int colCount = 0;
            int sheet = 0;
            int fromRow = 0;
            if (fromRow == 0 && colCount == 0) {
                colCount = dataset.getViewSequence().size();

            }
            ArrayList<Integer> viewsequence = dataset.getViewSequence();
            if (colCount > 65000) {
                sheet = colCount / 65000;
                colCount = 65000;
            }
            Label label1 = null;
            for (int k = 0; k <= sheet; k++) {
                //added by Nazneen
                writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setWrap(false);
                rowStart = 4;
                counter = 2;
                //end of code by Nazneen
                String sheetName = "";

                titleLabel = new Label(0, 0, dataSnapshotDAO.getHeaderTitleForSnapshot());
                reportLabel = new Label(0, 1, "Report Title           : " + container.getReportName().replace("_", " "));
                dateLabel = new Label(0, 2, "Created Date        : " + sdf1.format(date));
//            System.out.println("titleLabel: "+titleLabel+"reportLabel: "+reportLabel+"dateLabel: "+dateLabel);

                sheetName = container.getReportName().replace("_", " ");

//                  writableSheet = writableWorkbook.createSheet(container.getReportName().replace("_", " ")+" "+k, k);
                writableSheet = writableWorkbook.createSheet(sheetName, k);
                String sName = writableSheet.getName();

                //added by Nazneen for changing sheet name for Multiple sheets
                int len = sName.length();
                if (k > 0) {
                    if (k < 10) {
                        sName = sName.substring(0, len - 2);
                    } else {
                        sName = sName.substring(0, len - 3);
                    }
                    sName = sName + "_" + k;
                    writableSheet.setName(sName);
                }
                //end of code by Nazneen

                for (int i = 0; i < extraHeaderRows.size(); i++) {
                    label1 = new Label(0, ++counter, "" + extraHeaderRows.get(i));
                    label1.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label1);
                    rowStart = counter + 2;
                    rowStartTarget = rowStart + 2;
                }

                for (int i = 0; i < rowStart - 2; i++) {
                    writableSheet.setRowView(i, (20 * 20), false);
                }

                titleLabel.setCellFormat(writableCellFormat);
                reportLabel.setCellFormat(writableCellFormat);
                dateLabel.setCellFormat(writableCellFormat);

                writableSheet.addCell(titleLabel);
                writableSheet.addCell(reportLabel);
                writableSheet.addCell(dateLabel);

                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setBackground(Colour.GRAY_25);
                writableCellFormat.setWrap(true);

                WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

                for (int i = 0; i < columnCount; i++) {
                    label = new Label(i, rowStart, displayLabels[i]);
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);

                    HeadingCellView = new CellView();
                    HeadingCellView.setSize(256 * 20);
                    writableSheet.setColumnView(i, HeadingCellView);
                }

//            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
//            writableCellFormat = new WritableCellFormat(writableFont);
//             Colour clr=getNearestColour("#0c8d4a");
//                       writableCellFormat.setBackground(clr);
//                       writableCellFormat.setWrap(true);


                int row = 0;
                for (int i = fromRow; i < colCount && i < viewsequence.size(); i++) {
                    for (int j = 0; j < columnCount; j++) {
                        String flag = "";
                        String colName = cols.get(j);
//                    String discolName = disCols.get(j);
                        int PerCentColumnIndex = colName.lastIndexOf("_percentwise");
                        String formattedData = "";
                        //added by Dinanath for color group
                        TableBuilder builder = null;
                        BigDecimal subtotalval = null;
                        String bgColor = null;
                        int actualRow = facade1.getActualRow(i);

                        Colour clr = null;
                        Color cl = null;
                        try {
                            bgColor = facade1.getColor(actualRow, colName, subtotalval);
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                        }
                        try {
                            clr = getNearestColour(bgColor);
                            if (clr != null) {
                                flag = "Y";
                            }
                            //  cl = Color.decode (bgColor);
                        } catch (Exception e) {
                            clr = getNearestColour("#ffffff");
                            //System.out.println("No color");
                        }


                        writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                        writableCellFormat = new WritableCellFormat(writableFont);
                        writableCellFormat.setBackground(clr);
                        //Added By Ram 12June2015 for white font on Report
                        if (flag == "Y") {
                            writableFont.setColour(Colour.WHITE);
                        }
                        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GREY_25_PERCENT);
                        //end Ram Code
                        writableCellFormat.setWrap(true);
//endded by Dinanath
                        if ("C".equals(dTypes.get(j))) {
                            cell = new Label(j, row + rowStart + 1, dataset.getFieldValueString(viewsequence.get(i), colName));
                            cell.setCellFormat(writableCellFormat);
                        } else if ("N".equals(dTypes.get(j))) {
                            String snbrSymbol = "";
                            String nbrSymbol = "";
//                        String element = container.getDisplayColumns().get(j);
                            String element = colName;
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
//                      snbrSymbol = container.symbol.get(container.getDisplayColumns().get(j));
                            snbrSymbol = container.symbol.get(colName);
                            int precision = container.getRoundPrecisionForMeasure(element);
                            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                            }
                            if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                                ///.println("ret.getFieldValueBigDecimal(i, colName)-----"+ret.getFieldValueBigDecimal(i, colName));
                                //label = new Label(j, i + rowStart + 1, nFormat.format(ret.getFieldValueBigDecimal(i, colName)));
                                if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    formattedData = this.convertDataToTime(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName));
                                    cell = new Label(j, row + rowStart + 1, formattedData, cf2);
                                } else {
                                    if ((nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) && (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase(""))) {
                                        if (snbrSymbol.equalsIgnoreCase("%")) {
                                            formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision) + "" + snbrSymbol;
                                        } else {
                                            formattedData = snbrSymbol + "" + NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision);
                                        }
                                    } else if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
                                        formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision);
                                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                        if (snbrSymbol.equalsIgnoreCase("%")) {
                                            formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision) + "" + snbrSymbol;
                                        } else {
                                            formattedData = snbrSymbol + "" + NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision);
                                        }
                                    }
                                    if ((nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) || (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase(""))) {
                                        cell = new Label(j, row + rowStart + 1, formattedData, cf2);
                                    } else {
                                        cell = new Number(j, row + rowStart + 1, Double.parseDouble((dataset.getFieldValueBigDecimal(viewsequence.get(i), colName)).toString()), cf2);
                                    }
                                }    // break;
                            } else {
                                //if ( PerCentColumnIndex != -1 )
               /*
                                 * if (RTMeasureElement.getMeasureType(colName)
                                 * == RTMeasureElement.PERCENTWISE) { //colName
                                 * = colName.replace("_percentwise", ""); //cell
                                 * = new Number(j, i + rowStart + 1,
                                 * Double.parseDouble((dataset.getFieldValueBigDecimal(i,
                                 * colName).divide(dataset.getColumnGrandTotalValue(colName),
                                 * MathContext.DECIMAL64).multiply(multiplier)).toString()),cf2);
                                 * cell = new Number(j, row + rowStart + 1,
                                 * dataset.getFieldValueRuntimeMeasure(viewsequence.get(i),
                                 * colName).doubleValue(), cf2); } else if
                                 * (RTMeasureElement.getMeasureType(colName) ==
                                 * RTMeasureElement.RANK) { cell = new Number(j,
                                 * row + rowStart + 1,
                                 * dataset.getFieldValueRuntimeMeasure(viewsequence.get(i),
                                 * colName).doubleValue(), cf2); } else if
                                 * (RTMeasureElement.getMeasureType(colName) ==
                                 * RTMeasureElement.GOALSEEK) { cell = new
                                 * Number(j, row + rowStart + 1,
                                 * dataset.getFieldValueRuntimeMeasure(viewsequence.get(i),
                                 * colName).doubleValue(), cf2); } else if
                                 * (RTMeasureElement.getMeasureType(colName) ==
                                 * RTMeasureElement.USERGOALSEEK) { cell = new
                                 * Number(j, row + rowStart + 1,
                                 * dataset.getFieldValueRuntimeMeasure(viewsequence.get(i),
                                 * colName).doubleValue(), cf2); } else if
                                 * (RTMeasureElement.getMeasureType(colName) ==
                                 * RTMeasureElement.RUNNINGTOTAL) { cell = new
                                 * Number(j, row + rowStart + 1,
                                 * dataset.getFieldValueRuntimeMeasure(viewsequence.get(i),
                                 * colName).doubleValue(), cf2); }
                                 */
                                //cell = new Number(j, row + rowStart + 1, dataset.getFieldValueRuntimeMeasure(viewsequence.get(i), colName).doubleValue(), cf2);
                                nbrSymbol = container.getNumberSymbol(element);
                                if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("%") && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueRuntimeMeasure(viewsequence.get(i), colName), nbrSymbol, precision);
                                    cell = new Label(j, row + rowStart + 1, formattedData, cf2);
                                } else {
                                    cell = new Number(j, row + rowStart + 1, Double.parseDouble((dataset.getFieldValueRuntimeMeasure(viewsequence.get(i), colName)).toString()), cf2);
                                }

                            }
                            cell.setCellFormat(writableCellFormat);

                        } else {
                            cell = new Label(j, row + rowStart + 1, dataset.getFieldValueString(viewsequence.get(i), colName));
                            cell.setCellFormat(writableCellFormat);
                        }
                        writableSheet.addCell(cell);
                        colName = null;
                        writableCellFormatForColorCode = null;
                    }
                    row++;
                }
                colCount = colCount + 65000;
                fromRow = fromRow + 65000;
                //display grandTotal
                // String formattedData="";
                //start of code by bhargavi for grandtotal on 15th Dec 2014
                String aggType = "";
                String refferedElements = "";
                String userColType = "";
                String refElementType = "";
                String tempFormula = "";
                ArrayList displayValues = new ArrayList();
                boolean isRunTime = false;
                PbDb pbdb = new PbDb();
                PbReturnObject retobj = null;
                DataFacade facade = new DataFacade(container);
                displayValues = container.getDisplayColumns();
                //changed by sruthi to display the subtotal
                for (int p = 0; p < 1; p++) {
                    if (facade.isGrandTotalRequired()) {
                        for (int i = 0; i < columnCount; i++) {

                            if ("N".equals(dTypes.get(i))) {
                                String formattedData = "";
                                String element = container.getDisplayColumns().get(i);
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
                                String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(i));
                                int precision = container.getRoundPrecisionForMeasure(element);
                                if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                    precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                                    element = element.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
                                    element = crosstabMeasureId.get(element);
                                }

                                BigDecimal grandToatal;
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
                                        logger.error("Exception:", ex);
                                    }
                                    if (retobj != null && retobj.getRowCount() > 0) {
                                        tempFormula = retobj.getFieldValueString(0, 0);
                                        aggType = retobj.getFieldValueString(0, 4);
                                        refferedElements = retobj.getFieldValueString(0, 1);
                                        userColType = retobj.getFieldValueString(0, 2);
                                        refElementType = retobj.getFieldValueString(0, 3);
                                        tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");
                                    } else {
                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                    }
                                    if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(i), "NORMAL_AVG");
                                    }
                                    if (!container.isReportCrosstab()) {
                                        if (isRunTime) {
                                            if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {
                                                facade.container.summarizedHashmap.put(facade.getColumnId(i), "NORMAL_AVG");
                                            }
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));

                                        } else if (userColType.equalsIgnoreCase("TIMECALUCULATED") && (aggType.contains("avg") || aggType.contains("AVG"))) {
                                            facade.container.summarizedHashmap.put(facade.getColumnId(i), "NORMAL_AVG");
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));

                                        } else if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
                                            if (!facade.container.getKpiQrycls().contains(facade.getColumnId(i).replace("A_", "").trim())) {

                                                grandToatal = (BigDecimal) getGTForChangePerOfSummTabMeasures(container, eleId, i);

                                            } else {
                                                if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(displayValues.get(i).toString().replace("A_", "").trim())) {
                                                    //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                                    if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()) == null) {
                                                        grandToatal = BigDecimal.ZERO;
                                                    } else if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()).equalsIgnoreCase("")) {
                                                        grandToatal = BigDecimal.ZERO;
                                                    } else {
                                                        grandToatal = new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()));
                                                    }

                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(displayValues.get(i).toString());
                                                }
                                            }
                                        } else if (userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("AVG")) {
                                            String refEleArray[] = refferedElements.split(",");
                                            int len1 = refEleArray.length;
                                            int flag = 1;
                                            StringBuilder mysqlString = new StringBuilder();
                                            for (int j = 0; j < len1; j++) {
                                                String elementId = refEleArray[j];
                                                String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                                PbReturnObject retobj1 = null;
                                                try {
                                                    retobj1 = pbdb.execSelectSQL(getBussColName);
                                                } catch (SQLException ex) {
                                                    logger.error("Exception:", ex);
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
                                                                mysqlString.append(mysqlString).append(",").append(grandTotalValueForEle).append(" AS ").append(newEleID);
                                                            } else {
                                                                tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                            }
                                                        } else {
                                                            grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(facade.getColumnId(i));
                                                        }
                                                    }
                                                } else {
                                                    grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(facade.getColumnId(i));
                                                }
                                            }
                                            //Calculate formula
                                            if (!tempFormula.equalsIgnoreCase("")) {
                                                facade.container.summarizedHashmap.put(facade.getColumnId(i), "OTHERS");
                                                grandToatal = (BigDecimal) getComputeFormulaVal(container, i, tempFormula, mysqlString.toString(), "GT");
                                            } else {
                                                grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(facade.getColumnId(i));
                                            }
                                        } else if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(i).toString().replace("A_", "").trim())) {
                                            //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                            if (aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")) {
                                                //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                                                grandToatal = dataset.getColumnGrandTotalValue(facade.getColumnId(i));
                                                int rowCnt = 0;
                                                if (facade.container.getReportCollect().crosscolmap1.get(facade.getColumnId(i)).toString().equalsIgnoreCase("Exclude 0")) {
                                                    rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), i, facade.getColumnId(i));
                                                } else {
                                                    rowCnt = facade.getRowCount();
                                                }
                                                grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                            } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()) == null) {
                                                grandToatal = BigDecimal.ZERO;
                                            } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()).equalsIgnoreCase("")) {
                                                grandToatal = BigDecimal.ZERO;
                                            } else {
                                                grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()));
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(displayValues.get(i).toString());
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
                                                grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                            }
                                        }
                                        //end of code by anitha
                                    } else if (container.isReportCrosstab()) {

                                        HashMap<String, ArrayList> nonViewByMapNew = null;
                                        nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
                                        try {
                                            if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                                facade.container.summarizedHashmap.put(facade.getColumnId(i), "NORMAL_AVG");
                                            }
//                        if (facade.container.getKpiQrycls().contains(eleId.trim())) {
                                            if (eleId != null && eleId != "null" && eleId != "" && aggType.equalsIgnoreCase("avg")) {
                                                //for calculating gt for avg cols like A_10,A_11 which are of gt type and is summarized
                                                if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                                                    String refEleArray[] = refferedElements.split(",");
                                                    int lenght1 = refEleArray.length;
                                                    int flag = 1;
                                                    StringBuilder mysqlString = new StringBuilder();
                                                    if (facade.getColumnId(i).contains("A_")) {
                                                        for (int j = 0; j < lenght1; j++) {
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
                                                                            mysqlString.append(mysqlString).append(",").append(grandTotalValueForEle).append(" AS ").append(newEleID);
                                                                        } else {
                                                                            tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                                        }
                                                                    } else {
                                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                                    }
                                                                }
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                            }
                                                        }
                                                        //Calculate formula value
                                                        if (!tempFormula.equalsIgnoreCase("")) {
                                                            grandToatal = getComputeFormulaVal(container, i, tempFormula, mysqlString.toString(), "GT");
                                                        } else {
                                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                        }
                                                    } else {
                                                        //for calculating gt for avg cols like A1,A2,A3 which are not of gt type
                                                        if (nonViewByMapNew.get(facade.getColumnId(i)) != null) {
                                                            String keyset = nonViewByMapNew.get(facade.getColumnId(i)).toString().replace("[", "").replace("]", "").trim();
                                                            String mainKeySetArray1[] = keyset.split(",");
                                                            String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
                                                            for (int m = 0; m < mainKeySetArray1.length - 1; m++) {
                                                                String val1 = mainKeySetArray1[m];
                                                                mainKeySetArray[m] = mainKeySetArray1[m];
                                                            }

                                                            for (int j = 0; j < lenght1; j++) {
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
                                                                                mysqlString.append(mysqlString).append(",").append(grangTotalValueForEle).append(" AS ").append(newEleID);
                                                                            } else {
                                                                                tempFormula = tempFormula.replace(newEleID, grangTotalValueForEle.toString());
                                                                            }
                                                                            flag1 = 1;
                                                                        } else {
                                                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                                        }
                                                                    }
                                                                } else {
                                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                                }
                                                            }
                                                            //Calculate formula value
                                                            if (!tempFormula.equalsIgnoreCase("")) {
                                                                grandToatal = getComputeFormulaVal(container, i, tempFormula, mysqlString.toString(), "GT");
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                            }
                                                        } else {
                                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                        }
                                                    }
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                    int rowCnt = 0;
                                                    if (facade.container.getReportCollect().crosscolmap1.get(element) != null) {
                                                        if (facade.container.getReportCollect().crosscolmap1.get(element).toString().equalsIgnoreCase("Exclude 0")) {
                                                            rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), i, facade.getColumnId(i));
                                                        }
                                                    } else {
                                                        rowCnt = facade.getRowCount();
                                                    }
                                                    grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                                }
                                            } else {
                                                if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
                                                    if (nonViewByMapNew.get(facade.getColumnId(i)) != null) {
                                                        String keyset = nonViewByMapNew.get(facade.getColumnId(i)).toString().replace("[", "").replace("]", "").trim();
                                                        if (facade.container.retObjHashmap.get(keyset) != null) {
                                                            grandToatal = new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
                                                        } else {
                                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                    }
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                }
                                            }
                                        } catch (SQLException ex) {
                                            logger.error("Exception:", ex);
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                        }


                                    } else {
                                        grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(displayValues.get(i).toString());
                                    }
//
//                      //end of code by bhargavi
                                    if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                        formattedData = grandToatal.toString();
                                    } else if (container.gettimeConversion(displayValues.get(i).toString()) != null && container.gettimeConversion(displayValues.get(i).toString()).equalsIgnoreCase("Y")) {
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
                                    if (container.gettimeConversion(displayValues.get(i).toString()) != null && container.gettimeConversion(displayValues.get(i).toString()).equalsIgnoreCase("Y")) {
                                        cell = new Label(i, k + rowStart + 1, formattedData, cf2);
                                    } else {
                                        if (formattedData.contains("%")) {
                                            cell = new Label(i, row + rowStart + 1, formattedData, cf2);
                                            if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                                cell = new Label(i, row + rowStart + 1, formattedData, cf2);
                                            } else {
                                                cell = new Number(i, row + rowStart + 1, Double.parseDouble((formattedData).replace(",", "")), cf2);
                                            }
                                        } else {
                                            if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                                cell = new Label(i, row + rowStart + 1, formattedData, cf2);
                                            } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                                cell = new Label(i, row + rowStart + 1, formattedData, cf2);
                                            } else {
                                                cell = new Number(i, row + rowStart + 1, Double.parseDouble(formattedData.replace(",", "")), cf2);
                                            }

                                        }


                                    }
                                }
                            } else {
                                if (p == 0 && i < viewByCnt) {
                                    cell = new Label(i, row + rowStart + 1, "Grand Total");
                                    cell.setCellFormat(writableCellFormat);
                                } else {
                                    cell = new Label(i, row + rowStart + 1, "Sub Total");
                                    cell.setCellFormat(writableCellFormat);
                                }
                            }
////         }else {
////                    if(String.valueOf(dataset.getColumnGrandTotalValue(colName))!=null && !String.valueOf(dataset.getColumnGrandTotalValue(colName)).equalsIgnoreCase("null") && !String.valueOf(dataset.getColumnGrandTotalValue(colName)).equalsIgnoreCase("")){
////                cell = new Number(i, row + rowStart + 1, Double.parseDouble(String.valueOf(dataset.getColumnGrandTotalValue(colName))),cf2);
////                    } else {
////                        cell = new Number(i, row + rowStart + 1, Double.parseDouble("0"),cf2);
////                    }
////                }
                            try {
                                writableSheet.addCell(cell);
                            } catch (WriteException ex) {
                                //code Added By Amar
                                if (container.getReportCollect().getLogReadWriterObject() != null) {
                                    StringWriter str = new StringWriter();
                                    PrintWriter writer = new PrintWriter(str);

                                    container.getReportCollect().setLogBoolean(true);
                                    try {
                                        container.getReportCollect().getLogReadWriterObject().fileWriter(str.getBuffer().toString());
                                    } catch (IOException eo) {
                                    }
                                }//end of code
                                logger.error("Exception:", ex);
                            }
                            // System.out.println("***...grandTotal "+dataset.getColumnGrandTotalValue(colName));
                            //commented by Nazneen
//                label.setCellFormat(writableCellFormat);
//                writableSheet.addCell(cell);
                        }
                        row++;//code added  by Sruthi
                    }
                }
                //added by sruthi to display overallavg,overallmin,overallmax
                BigDecimal othersValue = null;
                if (facade.isAverageRequired()) {
                    for (int i = 0; i < 1; i++) {
                        int startcol = 0;
                        for (int col = 0; col < cols.size(); col++) {
                            if ("N".equals(dTypes.get(col))) {
                                othersValue = (BigDecimal) facade.getColumnAverageValue(cols.get(col).toString());
                                this.getOthersData(othersValue, rowStart, row, i, cf2, col, container, cell, writableCellFormat, writableSheet);
                            } else if (i == 0) {
                                cell = new Label(i, row + rowStart + 1, "Overall Average");
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        }
                        row++;
                    }
                }
                if (facade.isOverAllMaxRequired()) {
                    for (int i = 0; i < 1; i++) {
                        for (int col = 0; col < cols.size(); col++) {
                            if ("N".equals(dTypes.get(col))) {
                                othersValue = (BigDecimal) facade.getColumnMaximumValue(cols.get(col).toString());
                                this.getOthersData(othersValue, rowStart, row, i, cf2, col, container, cell, writableCellFormat, writableSheet);
                            } else if (i == 0) {
                                cell = new Label(i, row + rowStart + 1, "OverallMax");
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        }
                        row++;
                    }
                }
                if (facade.isOverAllMinRequired()) {
                    for (int i = 0; i < 1; i++) {
                        for (int col = 0; col < cols.size(); col++) {
                            if ("N".equals(dTypes.get(col))) {
                                othersValue = (BigDecimal) facade.getColumnMinimumValue(cols.get(col).toString());
                                this.getOthersData(othersValue, rowStart, row, i, cf2, col, container, cell, writableCellFormat, writableSheet);
                            } else if (i == 0) {
                                cell = new Label(i, row + rowStart + 1, "OverallMin");
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        }
                        row++;
                    }
                }
//ended by sruhti
//end of code by bhargavi for grandtotal on 15th Dec 2014

            }
            writableWorkbook.write();
            writableWorkbook.close();
            fileOut.close();

        } catch (Exception exp) {
            //code Added By Amar
            if (container.getReportCollect().getLogReadWriterObject() != null) {
                StringWriter str = new StringWriter();
                PrintWriter writer = new PrintWriter(str);
//                exp.printStackTrace(writer);
                container.getReportCollect().setLogBoolean(true);
                try {
                    container.getReportCollect().getLogReadWriterObject().fileWriter(str.getBuffer().toString());
                } catch (IOException eo) {
                }
            }//end of code
            logger.error("Exception:", exp);
        }

        return swt.fileName;
    }

    //Added By Ram 11Aug15 for Normal scedule report in XLSX.
    public String generateReportInfoExcelX(Container container) throws Exception {                     //for exporting Reprot as EXCEL Format in Scheduler
        int columnCount;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        Number n = null;
        BigDecimal multiplier = new BigDecimal("100");
        Date date = new Date();
        int rowStart = 4;
        int rowStartTarget = 5;
        XSSFRow hRow = null;
        XSSFCell hCell = null;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFColor hColor = null;
        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet("  ");



        ProgenDataSet dataset = container.getRetObj();
        //dataset.resetViewSequence();
        ArrayList<String> cols = new ArrayList<String>();
        cols = container.getDisplayColumns();
        ArrayList<String> dTypes = new ArrayList<String>();
        dTypes = container.getDataTypes();
        ArrayList<String> rowView = new ArrayList<String>();

        ArrayList disCols = (ArrayList) container.getDisplayLabels().clone();
        //   ArrayList dTypes = (ArrayList) container.getDataTypes().clone();
        ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
        HashMap<String, String> crosstabMeasureId1 = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
        if (container.isReportCrosstab() || crosstabMeasureId1 != null && !crosstabMeasureId1.isEmpty()) {
            String eleIdss = "";
            for (Object hiddenCol : hiddenCols) {
                for (int i = 0; i < cols.size(); i++) {
                    eleIdss = crosstabMeasureId1.get(cols.get(i));
                    if (eleIdss != null && !eleIdss.equalsIgnoreCase("null") && !eleIdss.equalsIgnoreCase("")) {
                        if (hiddenCols.contains(eleIdss.replace("A_", ""))) {
                            cols.remove(i);
                            disCols.remove(i);
                            dTypes.remove(i);
                        }
                    }
                }
            }
        } else {
            for (Object hiddenCol : hiddenCols) {
                int index = cols.indexOf("A_" + hiddenCol.toString());
                if (index != -1) {
                    cols.remove(index);
                    disCols.remove(index);
                    dTypes.remove(index);
                }
            }
        }
        container.setDisplayLabels(disCols);
        container.setDisplayColumns(cols);
        container.setDisplayTypes(dTypes);

        container.setDataTypes(dTypes);
//Added by Amar on Oct 16, 2015
        if (dTypes.size() < disCols.size()) {
            for (int k = 0; k < disCols.size(); k++) {
                if (dTypes.size() < disCols.size()) {
                    dTypes.add("N");
                }
            }
        }
        if (dTypes.size() > disCols.size()) {
            int dTSize = dTypes.size();
            for (int k = 0; k < dTSize; k++) {
                if (dTypes.size() > disCols.size()) {
                    dTypes.remove(dTypes.size() - 1);
                }
            }
        }
        //end of code
        //modified by Dinanath for getting prev-month and next month correctly
        DataFacade facade1 = new DataFacade(container);
        String[] displayLabels = new String[dTypes.size()];
        for (int i = 0; i < dTypes.size(); i++) {
            String dispColsSecond = String.valueOf(disCols.get(i));
            if (dispColsSecond.contains("Prev Month")) {
                displayLabels[i] = String.valueOf(facade1.container.getMonthNameforTrailingFormulaOnColName(dispColsSecond));
            } else {
                displayLabels[i] = String.valueOf(dispColsSecond);
            }

//         displayLabels[i] = String.valueOf(disCols.get(i));
        }

        SimpleDateFormat sdf = new SimpleDateFormat();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
        ArrayList timeDetailsArray = null;
        ArrayList<String> paramNames = new ArrayList<String>();
        sdf.applyPattern("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
//        String headerTitle = dataSnapshotDAO.getHeaderTitleForSnapshot();
        timeDetailsArray = container.getTimeDetailsArray();

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateNow = formatter.format(currentDate.getTime());

        bos = new ByteArrayOutputStream();

        //String fileName = dateNow+"_"+container.getReportCollect().reportId+"_"+timeDetailsArray.get(2).toString().replace("/", ":")+".xls";
        columnCount = disCols.size();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        String reportId = container.getReportCollect().reportId;

        ServletWriterTransferObject swt = null;
        int viewByCnt = container.getViewByCount();




        try {
            if (this.getScheduleFileName() != null) {
                swt = ServletUtilities.createBufferedWriter(this.getScheduleFileName(), "xlsx");
            } else {
                swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId), "xlsx");
            }

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            //Code Added By Amar
            FileOutputStream fileOut = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName);

            ArrayList extraHeaderRows = new ArrayList();
            if (timeDetailsArray != null && timeDetailsArray.size() != 0) {
                if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {

                    String date1 = timeDetailsArray.get(2).toString().trim();
                    Date date11 = sdf2.parse(date1);
                    String dateFormattedDate1 = sdf1.format(date11);

                    extraHeaderRows.add("Date                    : " + dateFormattedDate1);
                    extraHeaderRows.add("Duration              : " + timeDetailsArray.get(3));
                    extraHeaderRows.add("Compare with     : " + timeDetailsArray.get(4));

                } else if (timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    String date1 = timeDetailsArray.get(2).toString().trim();
                    String date2 = timeDetailsArray.get(3).toString().trim();
                    String date3 = timeDetailsArray.get(4).toString().trim();
                    String date4 = timeDetailsArray.get(5).toString().trim();
                    Date date11 = sdf2.parse(date1);
                    Date date12 = sdf2.parse(date2);
                    Date date13 = sdf2.parse(date3);
                    Date date14 = sdf2.parse(date4);
                    String dateFormattedDate1 = sdf1.format(date11);
                    String dateFormattedDate2 = sdf1.format(date12);
                    String dateFormattedDate3 = sdf1.format(date13);
                    String dateFormattedDate4 = sdf1.format(date14);
                    extraHeaderRows.add("StartDate               : " + dateFormattedDate1);
                    extraHeaderRows.add("EndDate                : " + dateFormattedDate2);
                    extraHeaderRows.add("Duration                : " + dateFormattedDate3);
                    extraHeaderRows.add("Compare with       : " + dateFormattedDate4);
                }
            }

            //  int counter = 2;
            int colCount = 0;
            int sheet = 0;
            int fromRow = 0;
            if (fromRow == 0 && colCount == 0) {
                colCount = dataset.getViewSequence().size();

            }
            ArrayList<Integer> viewsequence = dataset.getViewSequence();
            if (colCount > 65000) {
                sheet = colCount / 65000;
                colCount = 65000;
            }
            for (int k = 0; k <= sheet; k++) {
                XSSFCellStyle hhStyle = workbook.createCellStyle();
                hhStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
                hhStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
                hhStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                hhStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                hhStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                hhStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
                hhStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
                hhStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                hhStyle.setRightBorderColor((short) 31);
                hhStyle.setLeftBorderColor((short) 31);
                hhStyle.setTopBorderColor((short) 31);
                hhStyle.setBottomBorderColor((short) 31);

                //      XSSFFont font = hWorkbook.createFont();
//                            font.setColor(IndexedColors.WHITE.getIndex());
//                            font.setBoldweight(Font.);
//                            hStyle.setFont(font);

                XSSFFont font = workbook.createFont();
                font.setFontHeightInPoints((short) 15);
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                hhStyle.setFont(font);

                String[] displayHeader = new String[3];
                displayHeader[0] = dataSnapshotDAO.getHeaderTitleForSnapshot();
                displayHeader[1] = "Report Title          : " + container.getReportName().replace("_", " ");
                displayHeader[2] = "Created Date       : " + sdf1.format(date);

                XSSFCellStyle hhhhStyle = workbook.createCellStyle();
                font = workbook.createFont();
                font.setFontHeightInPoints((short) 15);
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                hhhhStyle.setFont(font);

                spreadsheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
                spreadsheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
                spreadsheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
                spreadsheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 2));
                spreadsheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 2));
                spreadsheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));


                for (int i = 0; i < displayHeader.length; i++) {
                    hRow = spreadsheet.createRow(i);
                    hCell = hRow.createCell(0);
                    hCell.setCellValue(displayHeader[i]);
                    hCell.setCellStyle(hhhhStyle);
                }
                rowStart = 3;
                for (int i = 0; i < extraHeaderRows.size(); i++) {
                    hRow = spreadsheet.createRow(i + rowStart);
                    hCell = hRow.createCell(0);
                    hCell.setCellValue((String) extraHeaderRows.get(i));
                    hCell.setCellStyle(hhhhStyle);
                }


                rowStart = 7;
                //   counter = 2;

                for (int i = 0; i < 1; i++) {
                    hRow = spreadsheet.createRow(rowStart);
                    for (int j = 0; j < columnCount; j++) {
                        hCell = hRow.createCell(j);
                        //by Ram for Hide Measure Headings 15Feb2016
                        if (container.isReportCrosstab()) {
                            if (container.isHideMsrHeading()) {
                                String str = displayLabels[j];
                                str = str.replace("[", "").replace("]", "");
                                String[] st = str.split(",");
                                int l;
                                for (l = 0; l < st.length; l++) {
                                    if (!st[l].equalsIgnoreCase(" ")) {
                                        break;
                                    }
                                }
                                hCell.setCellValue(st[l]);
                            } else {
                                hCell.setCellValue(displayLabels[j]);
                            }
                        } else {
                            hCell.setCellValue(displayLabels[j]);
                        }
                        hCell.setCellStyle(hhStyle);
                    }

                }

                fromRow = 8;
                int row = 0;
                for (int i = 0; i < viewsequence.size(); i++) {
//                System.out.println("i:"+ i);
                    hRow = spreadsheet.createRow(i + fromRow);
                    for (int j = 0; j < columnCount; j++) {
                        String colName = cols.get(j);
                        spreadsheet.autoSizeColumn(j);
                        String formattedData = "";
                        //added by Dinanath for color group
                        TableBuilder builder = null;
                        BigDecimal subtotalval = null;
                        String bgColor = null;
                        int actualRow = facade1.getActualRow(i);
                        try {
                            bgColor = facade1.getColor(actualRow, colName, subtotalval);
                        } catch (Exception e) {
                            logger.error("Exception:", e);
                        }
                        //    writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                        XSSFCellStyle hhhStyle = workbook.createCellStyle();
                        if (bgColor != null && !bgColor.isEmpty()) {

                            hhhStyle.setFillPattern(XSSFCellStyle.LEAST_DOTS);
                            hhhStyle.setFillForegroundColor(new XSSFColor(Color.decode(bgColor)));
                            hhhStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
                            hhhStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                            hhhStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
                            hhhStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
                            hhhStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                            hhhStyle.setLeftBorderColor((short) 31);
                            hhhStyle.setRightBorderColor((short) 31);
                            hhhStyle.setTopBorderColor((short) 31);
                            hhhStyle.setBottomBorderColor((short) 31);

                            //end Ram Code

                        }

                        if ("C".equals(dTypes.get(j))) {
                            hCell = hRow.createCell(j);
                            hCell.setCellValue(dataset.getFieldValueString(viewsequence.get(i), colName));
                            hCell.setCellStyle(hhhStyle);
//                        cell = new Label(j, row + rowStart + 1, dataset.getFieldValueString(viewsequence.get(i), colName));
//                        cell.setCellFormat(writableCellFormat);
                        } else if ("N".equals(dTypes.get(j))) {
                            hCell = hRow.createCell(j);
                            String snbrSymbol = "";
                            String nbrSymbol = "";
//                        String element = container.getDisplayColumns().get(j);
                            String element = colName;
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
                            snbrSymbol = container.symbol.get(container.getDisplayColumns().get(j));
                            snbrSymbol = container.symbol.get(colName);
                            int precision = container.getRoundPrecisionForMeasure(element);
                            if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                                precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                            }
                            if (!RTMeasureElement.isRunTimeMeasure(colName)) {
                                ///.println("ret.getFieldValueBigDecimal(i, colName)-----"+ret.getFieldValueBigDecimal(i, colName));
                                //label = new Label(j, i + rowStart + 1, nFormat.format(ret.getFieldValueBigDecimal(i, colName)));
                                if (container.gettimeConversion(colName) != null && container.gettimeConversion(colName).equalsIgnoreCase("Y")) {
                                    formattedData = this.convertDataToTime(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName));
                                    //   cell = new Label(j, row + rowStart + 1, formattedData , cf2);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hhhStyle);
                                } else {
                                    if ((nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) && (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase(""))) {
                                        if (snbrSymbol.equalsIgnoreCase("%")) {
                                            formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision) + "" + snbrSymbol;
                                        } else {
                                            formattedData = snbrSymbol + "" + NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision);
                                        }
                                    } else if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
                                        formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision);
                                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                        if (snbrSymbol.equalsIgnoreCase("%")) {
                                            formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision) + "" + snbrSymbol;
                                        } else {
                                            formattedData = snbrSymbol + "" + NumberFormatter.getModifiedNumber(dataset.getFieldValueBigDecimal(viewsequence.get(i), colName), nbrSymbol, precision);
                                        }
                                    }
                                    if ((nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) || (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase(""))) {
                                        //  cell = new Label(j, row + rowStart + 1, formattedData, cf2);
                                        hCell.setCellValue(formattedData);
                                        hCell.setCellStyle(hhhStyle);
                                    } else {
                                        //  cell = new Number(j, row + rowStart + 1, Double.parseDouble((dataset.getFieldValueBigDecimal(viewsequence.get(i), colName)).toString()), cf2);
                                        hCell.setCellValue(Double.parseDouble((dataset.getFieldValueBigDecimal(viewsequence.get(i), colName)).toString()));
                                        hCell.setCellStyle(hhhStyle);
                                    }
                                }    // break;
                            } else {

                                nbrSymbol = container.getNumberSymbol(element);
                                if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("%") && !nbrSymbol.equalsIgnoreCase("")) {
                                    formattedData = NumberFormatter.getModifiedNumber(dataset.getFieldValueRuntimeMeasure(viewsequence.get(i), colName), nbrSymbol, precision);
                                    //    cell = new Label(j, row + rowStart + 1,formattedData, cf2);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hhhStyle);
                                } else {
                                    //  cell = new Number(j, row + rowStart + 1,Double.parseDouble(( dataset.getFieldValueRuntimeMeasure(viewsequence.get(i), colName)).toString()), cf2);
                                    hCell.setCellValue(Double.parseDouble((dataset.getFieldValueRuntimeMeasure(viewsequence.get(i), colName)).toString()));
                                    hCell.setCellStyle(hhhStyle);
                                }

                            }

                        } else {
                            //hCell = new Label(j, row + rowStart + 1, dataset.getFieldValueString(viewsequence.get(i), colName));
                            //hCell.setCellFormat(writableCellFormat);
                            hCell = hRow.createCell(j);
                            hCell.setCellValue((dataset.getFieldValueString(viewsequence.get(i), colName)).toString());
                            hCell.setCellStyle(hhhStyle);
                        }

                    }
                    row++;
                }

                colCount = colCount + 65000;
                fromRow = fromRow + 65000;
                //display grandTotal
                // String formattedData="";
                //start of code by bhargavi for grandtotal on 15th Dec 2014
                String aggType = "";
                String refferedElements = "";
                String userColType = "";
                String refElementType = "";
                String tempFormula = "";
                ArrayList displayValues = new ArrayList();
                boolean isRunTime = false;
                PbDb pbdb = new PbDb();
                PbReturnObject retobj = null;
                DataFacade facade = new DataFacade(container);
                displayValues = container.getDisplayColumns();

                hRow = spreadsheet.createRow(row + rowStart + 1);
                XSSFCellStyle hhhhhStyle = workbook.createCellStyle();
                font = workbook.createFont();
                font.setFontHeightInPoints((short) 15);
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                hhhhhStyle.setFont(font);
                row++;
                for (int i = 0; i < columnCount; i++) {

                    if ("N".equals(dTypes.get(i))) {
                        String formattedData = "";
                        String element = container.getDisplayColumns().get(i);
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
                        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(i));
                        int precision = container.getRoundPrecisionForMeasure(element);
                        if (container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                            precision = container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
                            element = element.replace("_percentwise", "").replace("_rankST", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
                            element = crosstabMeasureId.get(element);
                        }

                        BigDecimal grandToatal;
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
                                logger.error("Exception:", ex);
                            }
                            if (retobj != null && retobj.getRowCount() > 0) {
                                tempFormula = retobj.getFieldValueString(0, 0);
                                aggType = retobj.getFieldValueString(0, 4);
                                refferedElements = retobj.getFieldValueString(0, 1);
                                userColType = retobj.getFieldValueString(0, 2);
                                refElementType = retobj.getFieldValueString(0, 3);
                                tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");
                            } else {
                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                            }
                            if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                facade.container.summarizedHashmap.put(facade.getColumnId(i), "NORMAL_AVG");
                            }
                            if (!container.isReportCrosstab()) {
                                if (isRunTime) {
                                    if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(i), "NORMAL_AVG");
                                    }
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                    //modified by anitha
                                } else if (userColType.equalsIgnoreCase("TIMECALUCULATED") && (aggType.contains("avg")||aggType.contains("AVG"))) {
                                    facade.container.summarizedHashmap.put(facade.getColumnId(i), "NORMAL_AVG");
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));

                                } else if (userColType.equalsIgnoreCase("SUMMARIZED") && refElementType.equalsIgnoreCase("4")) {
                                    if (!facade.container.getKpiQrycls().contains(facade.getColumnId(i).replace("A_", "").trim())) {

                                        grandToatal = (BigDecimal) getGTForChangePerOfSummTabMeasures(container, eleId, i);

                                    } else {
                                        if (facade.container.getKpiQrycls() != null && !facade.container.getKpiQrycls().isEmpty() && facade.container.getKpiQrycls().contains(displayValues.get(i).toString().replace("A_", "").trim())) {
                                            //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                            if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()) == null) {
                                                grandToatal = BigDecimal.ZERO;
                                            } else if (facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()).equalsIgnoreCase("")) {
                                                grandToatal = BigDecimal.ZERO;
                                            } else {
                                                grandToatal = new BigDecimal(facade.container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()));
                                            }

                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(displayValues.get(i).toString());
                                        }
                                    }
                                } else if (userColType.equalsIgnoreCase("SUMMARIZED") && aggType.equalsIgnoreCase("AVG")) {
                                    String refEleArray[] = refferedElements.split(",");
                                    int len1 = refEleArray.length;
                                    int flag = 1;

                                    StringBuilder mysqlString = new StringBuilder();
                                    for (int j = 0; j < len1; j++) {
                                        String elementId = refEleArray[j];
                                        String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                        PbReturnObject retobj1 = null;
                                        try {
                                            retobj1 = pbdb.execSelectSQL(getBussColName);
                                        } catch (SQLException ex) {
                                            logger.error("Exception:", ex);
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
                                                        mysqlString.append(mysqlString).append(",").append(grandTotalValueForEle).append(" AS ").append(newEleID);
                                                    } else {
                                                        tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                    }
                                                } else {
                                                    grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(facade.getColumnId(i));
                                                }
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(facade.getColumnId(i));
                                        }
                                    }
                                    //Calculate formula
                                    if (!tempFormula.equalsIgnoreCase("")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(i), "OTHERS");
                                        grandToatal = (BigDecimal) getComputeFormulaVal(container, i, tempFormula, mysqlString.toString(), "GT");
                                    } else {
                                        grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(facade.getColumnId(i));
                                    }
                                } else if (container.getKpiQrycls() != null && !container.getKpiQrycls().isEmpty() && container.getKpiQrycls().contains(displayValues.get(i).toString().replace("A_", "").trim())) {
                                    //System.out.println("values"+facade.container.getKpiRetObj().getFieldValueString(0, facade.getColumnId(column)));
                                    if (aggType.equalsIgnoreCase("avg") && !refElementType.equalsIgnoreCase("4") && !userColType.equalsIgnoreCase("SUMMARIZED")) {
                                        //facade.container.summarizedHashmap.put(facade.getColumnId(column), "NORMAL_AVG");
                                        grandToatal = dataset.getColumnGrandTotalValue(facade.getColumnId(i));
                                        int rowCnt = 0;
                                        if (facade.container.getReportCollect().crosscolmap1.get(facade.getColumnId(i)).toString().equalsIgnoreCase("Exclude 0")) {
                                            rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), i, facade.getColumnId(i));
                                        } else {
                                            rowCnt = facade.getRowCount();
                                        }
                                        grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                    } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()) == null) {
                                        grandToatal = BigDecimal.ZERO;
                                    } else if (container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()).equalsIgnoreCase("")) {
                                        grandToatal = BigDecimal.ZERO;
                                    } else {
                                        grandToatal = new BigDecimal(container.getKpiRetObj().getFieldValueString(0, displayValues.get(i).toString()));
                                    }
                                } else {
                                    grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(displayValues.get(i).toString());
                                }
                                //added code by anitha
                     if (userColType.equalsIgnoreCase("TIMECALUCULATED")&& (aggType.contains("avg")||aggType.contains("AVG"))) {
                            int rowCnt = 0;
                                    if ("1".equals(facade.container.stype) || "0".equals(facade.container.stype)) {
                                        rowCnt = facade.container.gettrwcnt();
                                    } else {
                                        rowCnt = facade.getRowCount();
                                        facade.container.settrwcnt(rowCnt);
                                    }

                            if (rowCnt != 0) {
                                    precision = container.getRoundPrecisionForMeasure(cols.get(i));
                                grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                               grandToatal =  new BigDecimal(NumberFormatter.getModifiedNumberFormat(grandToatal, "", precision));
                                formattedData = NumberFormatter.getModifiedNumberFormat(grandToatal, nbrSymbol, precision);
                            }
                         }
                     //end of code by anitha
                            } else if (container.isReportCrosstab()) {

                                HashMap<String, ArrayList> nonViewByMapNew = null;
                                nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
                                try {
                                    if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                        facade.container.summarizedHashmap.put(facade.getColumnId(i), "NORMAL_AVG");
                                    }
//                        if (facade.container.getKpiQrycls().contains(eleId.trim())) {
                                    if (eleId != null && eleId != "null" && eleId != "" && aggType.equalsIgnoreCase("avg")) {
                                        //for calculating gt for avg cols like A_10,A_11 which are of gt type and is summarized
                                        if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                                            String refEleArray[] = refferedElements.split(",");
                                            int lenght1 = refEleArray.length;
                                            int flag = 1;
                                            StringBuilder mysqlString = new StringBuilder();
                                            if (facade.getColumnId(i).contains("A_")) {
                                                for (int j = 0; j < lenght1; j++) {
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
                                                                    mysqlString.append(mysqlString).append(",").append(grandTotalValueForEle).append(" AS ").append(newEleID);
                                                                } else {
                                                                    tempFormula = tempFormula.replace(newEleID, grandTotalValueForEle.toString());
                                                                }
                                                            } else {
                                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                            }
                                                        }
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                    }
                                                }
                                                //Calculate formula value
                                                if (!tempFormula.equalsIgnoreCase("")) {
                                                    grandToatal = getComputeFormulaVal(container, i, tempFormula, mysqlString.toString(), "GT");
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                }
                                            } else {
                                                //for calculating gt for avg cols like A1,A2,A3 which are not of gt type
                                                if (nonViewByMapNew.get(facade.getColumnId(i)) != null) {
                                                    String keyset = nonViewByMapNew.get(facade.getColumnId(i)).toString().replace("[", "").replace("]", "").trim();
                                                    String mainKeySetArray1[] = keyset.split(",");
                                                    String mainKeySetArray[] = new String[mainKeySetArray1.length - 1];
                                                    for (int m = 0; m < mainKeySetArray1.length - 1; m++) {
                                                        String val1 = mainKeySetArray1[m];
                                                        mainKeySetArray[m] = mainKeySetArray1[m];
                                                    }

                                                    for (int j = 0; j < lenght1; j++) {
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
                                                                        mysqlString.append(mysqlString).append(",").append(grangTotalValueForEle).append(" AS ").append(newEleID);
                                                                    } else {
                                                                        tempFormula = tempFormula.replace(newEleID, grangTotalValueForEle.toString());
                                                                    }
                                                                    flag1 = 1;
                                                                } else {
                                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                                }
                                                            }
                                                        } else {
                                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                        }
                                                    }
                                                    //Calculate formula value
                                                    if (!tempFormula.equalsIgnoreCase("")) {
                                                        grandToatal = getComputeFormulaVal(container, i, tempFormula, mysqlString.toString(), "GT");
                                                    } else {
                                                        grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                    }
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                }
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                            int rowCnt = 0;
                                            if (facade.container.getReportCollect().crosscolmap1.get(element).toString().equalsIgnoreCase("Exclude 0")) {
                                                rowCnt = facade.getRowCount() - this.getZeroRowCount(container, facade.getRowCount(), i, facade.getColumnId(i));
                                            } else {
                                                rowCnt = facade.getRowCount();
                                            }
                                            grandToatal = grandToatal.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                        }
                                    } else {
                                        if (nonViewByMapNew != null && !nonViewByMapNew.isEmpty()) {
                                            if (nonViewByMapNew.get(facade.getColumnId(i)) != null) {
                                                String keyset = nonViewByMapNew.get(facade.getColumnId(i)).toString().replace("[", "").replace("]", "").trim();
                                                if (facade.container.retObjHashmap.get(keyset) != null) {
                                                    grandToatal = new BigDecimal(facade.container.retObjHashmap.get(keyset).toString());
                                                } else {
                                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                                }
                                            } else {
                                                grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                            }
                                        } else {
                                            grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                        }
                                    }
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                    grandToatal = (BigDecimal) facade.getColumnGrandTotalValue(facade.getColumnId(i));
                                }


                            } else {
                                grandToatal = (BigDecimal) dataset.getColumnGrandTotalValue(displayValues.get(i).toString());
                            }
//
//                      //end of code by bhargavi
                            if (nbrSymbol != null && nbrSymbol.equalsIgnoreCase("nf")) {
                                formattedData = grandToatal.toString();
                            } else if (container.gettimeConversion(displayValues.get(i).toString()) != null && container.gettimeConversion(displayValues.get(i).toString()).equalsIgnoreCase("Y")) {
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

                            if (container.gettimeConversion(displayValues.get(i).toString()) != null && container.gettimeConversion(displayValues.get(i).toString()).equalsIgnoreCase("Y")) {
                                hCell = hRow.createCell(i);
                                hCell.setCellValue(formattedData);
                                hCell.setCellStyle(hhhhhStyle);
                            } else {
                                if (formattedData.contains("%")) {
                                    hCell = hRow.createCell(i);
                                    hCell.setCellValue(formattedData);
                                    hCell.setCellStyle(hhhhhStyle);
                                    if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                        hCell = hRow.createCell(i);
                                        hCell.setCellValue(formattedData);
                                        hCell.setCellStyle(hhhhhStyle);
                                    } else {

                                        hCell = hRow.createCell(i);
                                        hCell.setCellValue(Double.parseDouble((formattedData).replace(",", "")));
                                        hCell.setCellStyle(hhhhhStyle);
                                    }
                                } else {
                                    if ((snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase(""))) {
                                        hCell = hRow.createCell(i);
                                        hCell.setCellValue(formattedData);
                                        hCell.setCellStyle(hhhhhStyle);
                                    } else if (snbrSymbol != null && !snbrSymbol.equalsIgnoreCase("")) {
                                        hCell = hRow.createCell(i);
                                        hCell.setCellValue(formattedData);
                                        hCell.setCellStyle(hhhhhStyle);
                                    } else {
                                        //  cell = new Number(i, row + rowStart + 1, Double.parseDouble(formattedData.replace(",", "")), cf2);
                                        hCell = hRow.createCell(i);
                                        hCell.setCellValue(Double.parseDouble(formattedData.replace(",", "")));
                                        hCell.setCellStyle(hhhhhStyle);
                                    }

                                }

                            }
                        }
                    } else {
                        //  cell  = new Label(i, row + rowStart + 1,"Grand Total");
                        hCell = hRow.createCell(i);
                        if (i < viewByCnt) {
                            hCell.setCellValue("Grand Total");
                        } else {
                            hCell.setCellValue(" ");
                        }
                        hCell.setCellStyle(hhhhhStyle);
                    }

                }


                //added by sruthi to display overallavg,overallmin,overallmax
                BigDecimal othersValue = null;
                if (facade.isAverageRequired()) {
                    hRow = spreadsheet.createRow(row + rowStart + 1);
                    for (int i = 0; i < 1; i++) {
                        int startcol = 0;
                        for (int col = 0; col < cols.size(); col++) {
                            if ("N".equals(dTypes.get(col))) {
                                othersValue = (BigDecimal) facade.getColumnAverageValue(cols.get(col).toString());
                                this.getOthersDataX(othersValue, rowStart, row, i, hRow, col, container);
                            } else if (i == 0) {
                                hCell = hRow.createCell(col);
                                hCell.setCellValue("OverAllAvg");
                            }
                        }
                        row++;
                    }
                }
                if (facade.isOverAllMaxRequired()) {
                    hRow = spreadsheet.createRow(row + rowStart + 1);
                    for (int i = 0; i < 1; i++) {
                        for (int col = 0; col < cols.size(); col++) {
                            if ("N".equals(dTypes.get(col))) {
                                othersValue = (BigDecimal) facade.getColumnMaximumValue(cols.get(col).toString());
                                this.getOthersDataX(othersValue, rowStart, row, i, hRow, col, container);
                            } else if (i == 0) {
                                hCell = hRow.createCell(col);
                                hCell.setCellValue("OverAllMax");
                            }
                        }
                        row++;
                    }
                }
                if (facade.isOverAllMinRequired()) {
                    hRow = spreadsheet.createRow(row + rowStart + 1);
                    for (int i = 0; i < 1; i++) {
                        for (int col = 0; col < cols.size(); col++) {
                            if ("N".equals(dTypes.get(col))) {
                                othersValue = (BigDecimal) facade.getColumnMinimumValue(cols.get(col).toString());
                                this.getOthersDataX(othersValue, rowStart, row, i, hRow, col, container);
                            } else if (i == 0) {
                                hCell = hRow.createCell(col);
                                hCell.setCellValue("OverAllMIn");
                            }
                        }
                        row++;
                    }
                }
//ended by sruhti
            }

///////////////////////////////////////////////

            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception exp) {
            //code Added By Amar
            if (container.getReportCollect().getLogReadWriterObject() != null) {
                StringWriter str = new StringWriter();
                PrintWriter writer = new PrintWriter(str);
//                exp.printStackTrace(writer);
                container.getReportCollect().setLogBoolean(true);
                try {
                    container.getReportCollect().getLogReadWriterObject().fileWriter(str.getBuffer().toString());
                } catch (IOException eo) {
                }
            }//end of code
            logger.error("Exception:", exp);
        }
        return swt.fileName;
    }
//Added by Ram 26June2015

    public String generateReportInfoCSV(Container container, String userId) {                     //for exporting Reprot as EXCEL Format in Scheduler

        PbReportCollection collect = container.getReportCollect();
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        ArrayList<String> timedetails = new ArrayList<String>();
        ArrayList<String> parameterDertails = new ArrayList<String>();
//        if (!csvNtopar.equalsIgnoreCase("CSN")) {
        timedetails = container.getTimeDetailsArray();
        //if(timedetails.size()<6){
        if (!timedetails.contains(container.getReportDesc())) {
            timedetails.add(container.getReportDesc());
            SimpleDateFormat sdf = new SimpleDateFormat();
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            sdf.applyPattern("MM/dd/yyyy");
            timedetails.add(sdf1.format(Calendar.getInstance().getTime()));
        }
        String viewbyvalues = "";
        String[] values = new String[collect.paramValueList.size()];
        if (collect.paramValueList != null) {
            for (int k = 0; k < collect.paramValueList.size(); k++) {
                values = collect.paramValueList.get(k).toString().split(":");

                viewbyvalues = values[1];

                if (!viewbyvalues.equalsIgnoreCase("[All]")) {
                    String value = (String) collect.paramValueList.get(k);
                    if (!parameterDertails.contains(value)) {
                        parameterDertails.add(value);
                    }
                }
            }
        }
//        }
//        else{
//            timedetails.add("wihtoutParam");
//            if (!timedetails.contains(container.getReportDesc())) {
//                timedetails.add(container.getReportDesc());
//                SimpleDateFormat sdf = new SimpleDateFormat();
//                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
//                sdf.applyPattern("MM/dd/yyyy");
//                timedetails.add(sdf1.format(Calendar.getInstance().getTime()));
//            }

        displayHelper = new CSVTableHeaderDisplay(tableBldr, timedetails, parameterDertails);
        bodyHelper = new CSVTableBodyDisplay(tableBldr);
        subTotalHelper = new CSVTableSubTotalDisplay(tableBldr);
        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(reportId, userId), "csv");
            Writer writer = swt.writer;
//                String reportHTML=generateReportInfo(container);
//                writer.write(reportHTML);
//                writer.flush();

            displayHelper.setWriter(writer);
            displayHelper.setNext(bodyHelper).setNext(subTotalHelper);

            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(displayHelper.generateOutputHTML());
            writer.write(tableHtml.toString());
            writer.flush();
            writer.close();
            swt.setReportName(collect.reportName);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;
    }

//end Ram code
    public String getFileName(String reportId) {
        return ServletUtilities.prefix + reportId + "_DS";
    }

    public String generateNewProductHtml(NewProductNameHelper newProdData, String from) {

        ServletWriterTransferObject swt = null;

        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(newProdData.getGoalNameType().get(0)), "html");
            Writer writer = swt.writer;
            String reportHTML = getNewProductParameter(newProdData);
            writer.write(reportHTML);
            writer.flush();
            swt.setReportName(newProdData.getGoalNameType().get(0));

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;
    }

    private String getNewProductParameter(NewProductNameHelper newProdData) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        String headerTitle = "Progen Business Solutions";

        StringBuilder repInfoBuilder = new StringBuilder("");

        repInfoBuilder.append("<html><body>");
        repInfoBuilder.append("<table>");
        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td><h4>").append(headerTitle).append("</h4></td>");
        repInfoBuilder.append("</tr>");

        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Goal Report: </b> </td><td style='font-size:14px;'>").append(newProdData.getGoalNameType().get(0)).append("</td>");
        repInfoBuilder.append("</tr>");

        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Created Date: </b> </td><td style='font-size:14px;'>").append(sdf.format(calendar.getTime())).append("</td>");
        repInfoBuilder.append("</tr>");

        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Model Name: </b> </td><td style='font-size:14px;'>").append(newProdData.getMeasures().get(newProdData.getMeasures().size() - 1)).append("</td>");
        repInfoBuilder.append("</tr>");
        repInfoBuilder.append("</table>");
        repInfoBuilder.append("<br/><br/>");

        repInfoBuilder.append("<Table ID=\"progenTable\"  border='1' style='border-collapse:separate;border-spacing:0px;-moz-border-radius: 6px 6px 6px 6px;' CELLPADDING=\"0\" CELLSPACING=\"1\">");
        repInfoBuilder.append("<thead id=\"theaddiv\">");


        newProdData.getMeasures().remove(newProdData.getMeasures().size() - 1);

        repInfoBuilder.append("<tr>");
        for (int i = 0; i < newProdData.getMeasures().size(); i++) {
            repInfoBuilder.append("<th style=\"background-color:#B4D9EE;\"  rowspan=\"1\" colspan=\"1\">");//colspan="+colSpan+"
            repInfoBuilder.append("<table width=\"100%\" border=\"0\">");
            repInfoBuilder.append("<tr valign=\"top\">");
            repInfoBuilder.append("<td valign=\"top\" id=\"tdRef_").append("\" style='font-size: 12px;-moz-border-radius: 6px 6px 6px 6px;' align=\"center\" width=\"100%\"><b>").append(newProdData.getMeasures().get(i)).append("</b>");
            repInfoBuilder.append("</td>");
            repInfoBuilder.append("</tr>");
            repInfoBuilder.append("</table>");
            repInfoBuilder.append("</th>");
        }
        repInfoBuilder.append("</tr>");

        repInfoBuilder.append("</thead>");

        if (!newProdData.getGoalChangeValue().isEmpty()) {
            for (int i = 0; i < newProdData.getViewByNameValue().size(); i++) {
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 12px; background-color: #B4D9EE; rowspan=\"1\" colspan=\"1\"; align=\"left\">").append("<font color=''>").append(newProdData.getViewByNameValue().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getCurrentValue().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getPriorValue().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getChangedpercent().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getGoalChangePernt().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getGoalChangeValue().get(i)).append("</font>").append("</td>");
                repInfoBuilder.append("</tr>");
            }
        } else {
            for (int i = 0; i < newProdData.getViewByNameValue().size(); i++) {
                repInfoBuilder.append("<tr>");
                repInfoBuilder.append("<td style='font-size: 12px; background-color: #B4D9EE; rowspan=\"1\" colspan=\"1\"; align=\"left\">").append("<font color=''>").append(newProdData.getViewByNameValue().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getCurrentValue().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getPriorValue().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getChangedpercent().get(i)).append("</font>").append("</td>").append("<td style='font-size: 12px; rowspan=\"1\" colspan=\"1\"; text-align=\"right\">").append("<font color=''>").append(newProdData.getGoalChangePernt().get(i)).append("</font>").append("</td>");
                repInfoBuilder.append("</tr>");
            }
        }

        repInfoBuilder.append("</table>");
        repInfoBuilder.append("</body></html>");


        return repInfoBuilder.toString();
    }

    public String generateNewProductExcel(NewProductNameHelper newProdData, String from) {

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
        Label modelName = null;
        Label timeRangeLabel = null;
        int columnCount;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        Number n = null;
        BigDecimal multiplier = new BigDecimal("100");
        int maxRows = 65500;//65500
        int countOfSheets = 1;
        Date date = new Date();
        int rowStart = 6;
        int rowStartTarget = 5;


        SimpleDateFormat sdf = new SimpleDateFormat();
        ArrayList timeDetailsArray = null;
        ArrayList<String> paramNames = new ArrayList<String>();
        sdf.applyPattern("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        String headerTitle = dataSnapshotDAO.getHeaderTitleForSnapshot();
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateNow = formatter.format(currentDate.getTime());

        bos = new ByteArrayOutputStream();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;

        try {

            swt = ServletUtilities.createBufferedWriter(this.getFileName(newProdData.getGoalNameType().get(0)), "xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableSheet = writableWorkbook.createSheet(newProdData.getGoalNameType().get(0), 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            //writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(false);

            titleLabel = new Label(0, 0, dataSnapshotDAO.getHeaderTitleForSnapshot());
            reportLabel = new Label(0, 1, "Goal Title               : " + newProdData.getGoalNameType().get(0));
            dateLabel = new Label(0, 2, "Created Date        : " + sdf.format(date));
            modelName = new Label(0, 3, "Model Name         : " + newProdData.getMeasures().get(newProdData.getMeasures().size() - 1));

            newProdData.getMeasures().remove(newProdData.getMeasures().size() - 1);

            for (int i = 0; i < rowStart - 2; i++) {
                writableSheet.setRowView(i, (20 * 20), false);
            }

            titleLabel.setCellFormat(writableCellFormat);
            reportLabel.setCellFormat(writableCellFormat);
            dateLabel.setCellFormat(writableCellFormat);
            modelName.setCellFormat(writableCellFormat);

            writableSheet.addCell(titleLabel);
            writableSheet.addCell(reportLabel);
            writableSheet.addCell(dateLabel);
            writableSheet.addCell(modelName);

            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);

            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

            for (int i = 0; i < newProdData.getMeasures().size(); i++) {
                label = new Label(i, rowStart, newProdData.getMeasures().get(i));
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);

                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 20);
                writableSheet.setColumnView(i, HeadingCellView);
            }

            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

            if (!newProdData.getGoalChangeValue().isEmpty()) {
                for (int i = 0; i < newProdData.getViewByNameValue().size(); i++) {
                    for (int j = 0; j < newProdData.getMeasures().size(); j++) {

                        if (j == 0) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getViewByNameValue().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 1) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getCurrentValue().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 2) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getPriorValue().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 3) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getChangedpercent().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 4) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getGoalChangePernt().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 5) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getGoalChangeValue().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }

                        writableSheet.addCell(cell);
                    }
                }
            } else {
                for (int i = 0; i < newProdData.getViewByNameValue().size(); i++) {
                    for (int j = 0; j < newProdData.getMeasures().size(); j++) {

                        if (j == 0) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getViewByNameValue().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 1) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getCurrentValue().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 2) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getPriorValue().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 3) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getChangedpercent().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }
                        if (j == 4) {
                            cell = new Label(j, i + rowStart + 1, newProdData.getGoalChangePernt().get(i), cf2);
                            cell.setCellFormat(writableCellFormat);
                        }


                        writableSheet.addCell(cell);
                    }
                }
            }


            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }

        return swt.fileName;

    }

    public String measureDrill(String[] string, String repId, PbReturnObject retObj) {

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
        Label modelName = null;
        Label timeRangeLabel = null;
        int columnCount;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        Number n = null;
        BigDecimal multiplier = new BigDecimal("100");
        int maxRows = 65500;//65500
        int countOfSheets = 1;
        Date date = new Date();
        int rowStart = 0;
        int rowStartTarget = 5;


        SimpleDateFormat sdf = new SimpleDateFormat();
        ArrayList timeDetailsArray = null;
        ArrayList<String> paramNames = new ArrayList<String>();
        sdf.applyPattern("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        String headerTitle = dataSnapshotDAO.getHeaderTitleForSnapshot();
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateNow = formatter.format(currentDate.getTime());

        bos = new ByteArrayOutputStream();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;

        try {

            swt = ServletUtilities.createBufferedWriter(this.getFileName(repId), "xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableSheet = writableWorkbook.createSheet(repId, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            //writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(false);


            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);

            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

            for (int i = 0; i < string.length; i++) {
                label = new Label(i, rowStart, string[i]);
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);

                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 20);
                writableSheet.setColumnView(i, HeadingCellView);
            }

            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

            for (int i = 0; i < retObj.getRowCount(); i++) {
                for (int j = 0; j < string.length; j++) {


                    cell = new Label(j, i + rowStart + 1, retObj.getFieldValueString(i, string[j]), cf2);
                    cell.setCellFormat(writableCellFormat);


                    writableSheet.addCell(cell);
                }
            }

            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }

        return swt.fileName;

    }

    public String getScheduleFileName() {
        return ScheduleFileName;
    }

    public void setScheduleFileName(String ScheduleFileName) {
        this.ScheduleFileName = ScheduleFileName;
    }
//Surender

    public StringBuilder generateReportTable(Container container, String userId, String higth, String width) {
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

//        sortTypes = container.getSortTypes();
//        sortDataTypes = container.getSortDataTypes();
//        //srchObj = retObj.sort(sortCols, sortTypes, sortDataTypes);
//        //srchObj = retObj.sort(sortCols, sortTypes, container.getDataTypes());//commented by santhosh.kumar@progenbusiness.com for the purpose of percent of column on 08-02-2010
//        //srchObj = retObj.sortModified(sortCols, sortTypes, container.getDataTypes(), container.getOriginalColumns());
//
//        //container.setSortRetObj(srchObj);
//        //retObj = null;
//        //retObj = srchObj;
//        rowSequence = retObj.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
//        retObj.setViewSequence(rowSequence);
//        rowCount = rowSequence.size();
//        container.resetTopBottom();
//        String istranseposse=session.
        String istranseposse = container.getFromBkp();
        String fromOneview = "fromOneview";
        String reportId = collect.reportId;
        DataFacade facade = new DataFacade(container);
        facade.setUserId(userId);
        facade.setCtxPath(collect.ctxPath);
        TableBuilder tableBldr = new RowViewTableBuilder(facade);
        if (fromRow == 0 && toRow == 0) {
            tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
        } else {
            tableBldr.setFromAndToRow(fromRow, toRow);
        }
        if (istranseposse != null && istranseposse.equalsIgnoreCase("true")) {
            tableBldr.setGetFromOneview(fromOneview);
        }
        TableDisplay displayHelper = null;
        TableDisplay bodyHelper = null;
        TableDisplay subTotalHelper = null;
        displayHelper = new HtmlTableHeaderDisplay(tableBldr);
        displayHelper.setFromOneviewflag(fromOneview);
        bodyHelper = new HtmlTableBodyDisplay(tableBldr);
        bodyHelper.setFromOneviewflag(fromOneview);
        subTotalHelper = new HtmlTableSubTotalDisplay(tableBldr);
        subTotalHelper.setFromOneviewflag(fromOneview);
        StringBuilder tableHtml = new StringBuilder();
//        tableHtml.append("<br><center><table style=\"width:").append(width).append("px;").append("height:").append(higth).append("px;\"");
        tableHtml.append("<br><center><table class=\"tablesorter\" cellspacing=\"1\" style=\"width:").append(Integer.parseInt(width) - 10).append("px;\"");
        tableHtml.append(">");
        displayHelper.setNext(bodyHelper).setNext(subTotalHelper);

        tableHtml.append(displayHelper.generateOutputHTML());
        tableHtml.append("</table></center>");

        return tableHtml;
    }

    public String htmlgernaration(String oneiviewId, String oneviewName, String oneviewHtmlCode) throws Exception {

        ServletWriterTransferObject swt = null;
        try {

            swt = ServletUtilities.createBufferedWriter(oneiviewId, "html");
            Writer writer = swt.writer;

//                String reportHTML=generateReportInfo(container);
            writer.write(oneviewHtmlCode);
            writer.flush();

            writer.close();

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;

//        Document pdfDocument = new Document(PageSize.B2, 50, 50, 50, 50);
////         document = new Document(PageSize.B2, 50, 50, 50, 50);
//        Reader htmlreader = new BufferedReader(new InputStreamReader(
//                                 new FileInputStream("/home/progen/Desktop/structure.html")));
//
//// OutputStream - enhÃ¤lt nachher geparste daten
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfWriter.getInstance(pdfDocument, baos);
//
////Style
//        StyleSheet styles = new StyleSheet();
//        styles.loadTagStyle("body", "font", "Bitstream Vera Sans");
//// styles.loadTagStyle("body", "size", "30px");
//// styles.loadTagStyle("body", "color", "blue");
//
//        pdfDocument.open();
//
//        ArrayList p = HTMLWorker.parseToList(htmlreader, styles);
//        for (int k = 0; k < p.size(); ++k) {
//            Element e = (Element) p.get(k);
//            pdfDocument.add(e);
//        }
//        pdfDocument.close();
//
//        byte[] bs = baos.toByteArray();
//
////codiert pdfString in base64
//        String pdfBase64 = Base64.encodeBytes(bs); //output
//
////Test File -_> zur Ã¼berprÃ¼fung
//        File pdfFile = new File("/home/progen/Desktop/final2.pdf");
//        FileOutputStream out = new FileOutputStream(pdfFile);
//        out.write(bs);
//        out.close();

//        return swt.fileName;


    }

    public String targetMeasureExcel(String elementID, String eleName, List<String> targetPeriodDetails, HashMap test, int selectYear, List<String> targetValues, boolean fromExcelDownload) {

        ByteArrayOutputStream bos = null;
        ServletOutputStream outputstream = null;
        Label[] labels = null;
        //common code for all
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableSheet[] writableSheets = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 0;
        ArrayList<String> colNames = new ArrayList<String>();
        colNames.add(eleName);
        //colNames.add(targetPeriodDetails.get(0) + " to " + targetPeriodDetails.get(targetPeriodDetails.size() - 1));
        colNames.add("Targets");
        for (int i = 1; i <= selectYear; i++) {
            colNames.add("Year1-" + i);
        }

        bos = new ByteArrayOutputStream();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ArrayList prevValues = new ArrayList();

        ServletWriterTransferObject swt = null;

        try {

            swt = ServletUtilities.createBufferedWriter(this.getFileName(elementID), "xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableSheet = writableWorkbook.createSheet(elementID, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            //writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(false);

            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);

            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

            for (int i = 0; i < colNames.size(); i++) {
                label = new Label(i, rowStart, colNames.get(i));
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);

                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 20);
                writableSheet.setColumnView(i, HeadingCellView);
            }

            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

            for (int i = 0; i < targetPeriodDetails.size(); i++) {
                for (int j = 0; j < colNames.size(); j++) {

                    if (j == 0) {
                        cell = new Label(j, i + rowStart + 1, targetPeriodDetails.get(i), cf2);
                    } else if (j == 1) {
                        if (fromExcelDownload && !targetValues.isEmpty()) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(targetValues.get(i)), cf2);
                        } else {
                            cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                        }
                    } else if (j == 2) {
                        ArrayList firstValue = (ArrayList) test.get(j - 1);
                        if (!firstValue.isEmpty() && firstValue.size() > i) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                        } else {
                            cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                        }
                    } else if (j == 3) {
                        ArrayList firstValue = (ArrayList) test.get(j - 1);
                        if (!firstValue.isEmpty() && firstValue.size() > i) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                        } else {
                            cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                        }
                    } else if (j == 4) {
                        ArrayList firstValue = (ArrayList) test.get(j - 1);
                        if (!firstValue.isEmpty() && firstValue.size() > i) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                        } else {
                            cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                        }
                    } else if (j == 5) {
                        ArrayList firstValue = (ArrayList) test.get(j - 1);
                        if (!firstValue.isEmpty() && firstValue.size() > i) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                        } else {
                            cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                        }
                    } else if (j == 6) {
                        ArrayList firstValue = (ArrayList) test.get(j - 1);
                        if (!firstValue.isEmpty() && firstValue.size() > i) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                        } else {
                            cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                        }
                    }

                    cell.setCellFormat(writableCellFormat);

                    writableSheet.addCell(cell);
                }
            }

            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }

        return swt.fileName;

    }

    public String targetMeasureExcel(String eleId, String eleName, List<String> targetPeriodDetails, List<String> targetValues, List<String> publishType) {
        ByteArrayOutputStream bos = null;
        ServletOutputStream outputstream = null;
        Label[] labels = null;
        //common code for all
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableSheet[] writableSheets = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 0;
        ArrayList<String> colNames = new ArrayList<String>();
        colNames.add(eleName);
        // colNames.add(targetPeriodDetails.get(0) + " to " + targetPeriodDetails.get(targetPeriodDetails.size() - 1));
        colNames.add("Targets");
        colNames.add("Publish Type");

        bos = new ByteArrayOutputStream();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;
        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(eleId), "xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableSheet = writableWorkbook.createSheet(eleId, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            //writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(false);

            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);

            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

            for (int i = 0; i < colNames.size(); i++) {
                label = new Label(i, rowStart, colNames.get(i));
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 20);
                writableSheet.setColumnView(i, HeadingCellView);
            }
            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

            for (int i = 0; i < targetPeriodDetails.size(); i++) {
                for (int j = 0; j < colNames.size(); j++) {
                    if (j == 0) {
                        if (!targetPeriodDetails.isEmpty()) {
                            cell = new Label(j, i + rowStart + 1, targetPeriodDetails.get(i), cf2);
                        } else {
                            cell = new Label(j, i + rowStart + 1, "", cf2);
                        }
                    } else if (j == 1) {
                        if (!targetValues.isEmpty()) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(targetValues.get(i)), cf2);
                        } else {
                            cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                        }
                    } else if (j == 2) {
                        if (!publishType.isEmpty()) {
                            cell = new Label(j, i + rowStart + 1, publishType.get(i), cf2);
                        } else {
                            cell = new Label(j, i + rowStart + 1, "", cf2);
                        }
                    }
                    cell.setCellFormat(writableCellFormat);
                    writableSheet.addCell(cell);
                }
            }
            writableWorkbook.write();
            writableWorkbook.close();
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return swt.fileName;
    }

    public String downLoadRegionViewExcelSheet(String elementID, String regionName, PbReturnObject retOjb, ArrayList regionNames, ArrayList monthNames, int noofRolling, String eleName) {
        ByteArrayOutputStream bos = null;
        ServletOutputStream outputstream = null;
        Label[] labels = null;
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableSheet[] writableSheets = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 0;
        List<String> colNames1 = new ArrayList<String>(monthNames);

        colNames1.add(regionName);
        List<String> colNames = new ArrayList<String>();

        bos = new ByteArrayOutputStream();
        for (int i = colNames1.size() - 1; i >= 0; i--) {
            if (!colNames.contains(colNames1.get(i))) {
                colNames.add(colNames1.get(i));
            }
        }
        int noofMonthsData = 0;
        int noofMonths = colNames.size() - 1;
        if (noofMonths == 1) {
            noofMonthsData = retOjb.getRowCount();
        } else {
            noofMonthsData = retOjb.getRowCount() / noofRolling;
        }
        colNames.add(eleName + " Total");
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;
        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(elementID), "xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableSheet = writableWorkbook.createSheet(elementID, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            //writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(false);
            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);
            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

            for (int i = 0; i < colNames.size(); i++) {
                label = new Label(i, rowStart, colNames.get(i));
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 20);
                writableSheet.setColumnView(i, HeadingCellView);
            }
            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

            for (int i = 0; i < noofMonthsData; i++) {
                double grandTotal = 0.0;
                for (int j = 0; j < colNames.size(); j++) {

                    if (j == 0) {
                        cell = new Label(j, i + rowStart + 1, retOjb.getFieldValueString(i, 0), cf2);
                    }
                    if (noofRolling == 2) {
                        if (noofMonths == 1) {
                            if (j == 1) {
                                cell = new Label(j, i + rowStart + 1, "", cf2);
                                cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString(i, 4)), cf2);
                                grandTotal += Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4));
                            }
                        } else {
                            if (j == 1) {
                                cell = new Label(j, i + rowStart + 1, "", cf2);
                                //cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4)), cf2);
//                            System.out.println("****%^*$%^$%*^$%^$%"+Double.parseDouble(retOjb.getFieldValueString(i, 4)));
//                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4));
                            }
                            if (j == 2) {
                                cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString(i, 4)), cf2);
                                grandTotal += Double.parseDouble(retOjb.getFieldValueString(i, 4));
                            }
                        }
//                        if (j == 2) {
//                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString(i, 4)), cf2);
//                            grandTotal += Double.parseDouble(retOjb.getFieldValueString(i, 4));
//                        }

                    }
                    if (noofRolling == 3) {
                        if (j == 3) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString(i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString(i, 4));
                        }
                        if (j == 2) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4));
                        }
                        if (j == 1) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4));
                        }
                    }
                    if (noofRolling == 4) {
                        if (j == 4) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString(i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString(i, 4));
                        }
                        if (j == 3) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4));
                        }
                        if (j == 2) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4));
                        }
                        if (j == 1) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((3 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((3 * noofMonthsData) + i, 4));
                        }
                    }
                    if (noofRolling == 5) {
                        if (j == 5) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString(i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString(i, 4));
                        }
                        if (j == 4) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4));
                        }
                        if (j == 3) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4));
                        }
                        if (j == 2) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((3 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((3 * noofMonthsData) + i, 4));
                        }
                        if (j == 1) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((4 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((4 * noofMonthsData) + i, 4));
                        }
                    }
                    if (noofRolling == 6) {
                        if (j == 6) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString(i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString(i, 4));
                        }
                        if (j == 5) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4));
                        }
                        if (j == 4) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4));
                        }
                        if (j == 3) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((3 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((3 * noofMonthsData) + i, 4));
                        }
                        if (j == 2) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((4 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((4 * noofMonthsData) + i, 4));
                        }
                        if (j == 1) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((5 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((5 * noofMonthsData) + i, 4));
                        }
                    }
                    if (noofRolling == 7) {
                        if (j == 7) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString(i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString(i, 4));
                        }
                        if (j == 6) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4));
                        }
                        if (j == 5) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((2 * noofMonthsData) + i, 4));
                        }
                        if (j == 4) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((3 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((4 * noofMonthsData) + i, 4));
                        }
                        if (j == 3) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((4 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((1 * noofMonthsData) + i, 4));
                        }
                        if (j == 2) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((5 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((5 * noofMonthsData) + i, 4));
                        }
                        if (j == 1) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(retOjb.getFieldValueString((6 * noofMonthsData) + i, 4)), cf2);
                            grandTotal += Double.parseDouble(retOjb.getFieldValueString((6 * noofMonthsData) + i, 4));
                        }
                    }
                    if (j == colNames.size() - 1) {
                        cell = new Number(j, i + rowStart + 1, grandTotal, cf2);
                    }
                    cell.setCellFormat(writableCellFormat);
                    writableSheet.addCell(cell);
                }
            }

            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return swt.fileName;
    }

    public String momYoyExcelDownLoad(String eleId, HashMap discreMonthMap, HashMap rollingYearMap, ArrayList discreteMonthsviewBys, int selectYear) {
        ByteArrayOutputStream bos = null;
        ServletOutputStream outputstream = null;
        Label[] labels = null;
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableSheet[] writableSheets = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 0;

        List<String> colNames = new ArrayList<String>();
        if (selectYear != 0) {
            for (int i = 1; i <= selectYear; i++) {
                colNames.add("Roling Year-" + i);
            }
        }
        if (!discreteMonthsviewBys.isEmpty()) {
            for (int i = 0; i < discreteMonthsviewBys.size(); i++) {
                colNames.add(discreteMonthsviewBys.get(i).toString());
            }
        }
        bos = new ByteArrayOutputStream();

        ArrayList rollingYear1 = new ArrayList();
        ArrayList rollingYear2 = new ArrayList();
        ArrayList rollingYear3 = new ArrayList();
        ArrayList rollingYear4 = new ArrayList();
        ArrayList rollingYear5 = new ArrayList();

        ArrayList rollingMonth1 = new ArrayList();
        ArrayList rollingMonth2 = new ArrayList();
        ArrayList rollingMonth3 = new ArrayList();
        ArrayList rollingMonth4 = new ArrayList();
        ArrayList rollingMonth5 = new ArrayList();
        ArrayList rollingMonth6 = new ArrayList();
        if (!discreMonthMap.isEmpty()) {
            for (int i = 0; i < discreMonthMap.size(); i++) {
                if (discreMonthMap.size() == 1) {
                    rollingMonth1 = (ArrayList) discreMonthMap.get(i);
                }
                if (discreMonthMap.size() == 2) {
                    rollingMonth2 = (ArrayList) discreMonthMap.get(i);
                }
                if (discreMonthMap.size() == 3) {
                    rollingMonth3 = (ArrayList) discreMonthMap.get(i);
                }
                if (discreMonthMap.size() == 4) {
                    rollingMonth4 = (ArrayList) discreMonthMap.get(i);
                }
                if (discreMonthMap.size() == 5) {
                    rollingMonth5 = (ArrayList) discreMonthMap.get(i);
                }
                if (discreMonthMap.size() == 6) {
                    rollingMonth6 = (ArrayList) discreMonthMap.get(i);
                }
            }
        }
        if (!rollingYearMap.isEmpty()) {
            for (int i = 0; i < rollingYearMap.size(); i++) {
                if (rollingYearMap.size() == 1) {
                    rollingYear1 = (ArrayList) rollingYearMap.get(i);
                }
                if (rollingYearMap.size() == 2) {
                    if (i == 0) {
                        rollingYear1 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 1) {
                        rollingYear2 = (ArrayList) rollingYearMap.get(i);
                    }
                }
                if (rollingYearMap.size() == 3) {
                    if (i == 0) {
                        rollingYear1 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 1) {
                        rollingYear2 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 2) {
                        rollingYear3 = (ArrayList) rollingYearMap.get(i);
                    }
                }
                if (rollingYearMap.size() == 4) {
                    if (i == 0) {
                        rollingYear1 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 1) {
                        rollingYear2 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 2) {
                        rollingYear3 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 3) {
                        rollingYear4 = (ArrayList) rollingYearMap.get(i);
                    }
                }
                if (rollingYearMap.size() == 5) {
                    if (i == 0) {
                        rollingYear1 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 1) {
                        rollingYear2 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 2) {
                        rollingYear3 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 3) {
                        rollingYear4 = (ArrayList) rollingYearMap.get(i);
                    }
                    if (i == 4) {
                        rollingYear5 = (ArrayList) rollingYearMap.get(i);
                    }
                }
            }
        }
        TreeSet noofYoyMom = new TreeSet();

        if (!rollingMonth1.isEmpty()) {
            noofYoyMom.add(rollingMonth1.size());
        }
        if (!rollingMonth2.isEmpty()) {
            noofYoyMom.add(rollingMonth2.size());
        }
        if (!rollingMonth3.isEmpty()) {
            noofYoyMom.add(rollingMonth3.size());
        }
        if (!rollingMonth4.isEmpty()) {
            noofYoyMom.add(rollingMonth4.size());
        }
        if (!rollingMonth5.isEmpty()) {
            noofYoyMom.add(rollingMonth5.size());
        }
        if (!rollingMonth6.isEmpty()) {
            noofYoyMom.add(rollingMonth6.size());
        }

        if (!rollingYear1.isEmpty()) {
            noofYoyMom.add(rollingYear1.size());
        }
        if (!rollingYear2.isEmpty()) {
            noofYoyMom.add(rollingYear2.size());
        }
        if (!rollingYear3.isEmpty()) {
            noofYoyMom.add(rollingYear3.size());
        }
        if (!rollingYear4.isEmpty()) {
            noofYoyMom.add(rollingYear4.size());
        }
        if (!rollingYear5.isEmpty()) {
            noofYoyMom.add(rollingYear5.size());
        }
        int maxValue = 0;
        if (!noofYoyMom.isEmpty()) {
            maxValue = Integer.parseInt(noofYoyMom.last().toString());
        }

        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;
        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(eleId), "xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableSheet = writableWorkbook.createSheet(eleId, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            //writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(false);
            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);
            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

            for (int i = 0; i < colNames.size(); i++) {
                label = new Label(i, rowStart, colNames.get(i));
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 20);
                writableSheet.setColumnView(i, HeadingCellView);
            }
            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

            for (int i = 0; i < maxValue; i++) {
                int k = 0;
                for (int j = 0; j < colNames.size(); j++) {
                    if (selectYear != 0) {
                        if (selectYear == 1) {
                            if (j == 0) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            }
                        }
                        if (selectYear == 2) {
                            if (j == 0) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 1) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            }
                        }
                        if (selectYear == 3) {
                            if (j == 0) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 1) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 2) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            }
                        }
                        if (selectYear == 4) {
                            if (j == 0) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 1) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 2) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 3) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            }
                        }
                        if (selectYear == 5) {
                            if (j == 0) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 1) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 2) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 3) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            } else if (j == 4) {
                                ArrayList firstValue = (ArrayList) rollingYearMap.get(j);
                                if (!firstValue.isEmpty() && firstValue.size() > i) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                } else {
                                    cell = new Label(j, i + rowStart + 1, "", cf2);
                                }
                                k++;
                            }
                        }
                    }
                    if (!discreteMonthsviewBys.isEmpty()) {
                        if (k == 0) {
                            if (discreteMonthsviewBys.size() == 1) {
                                if (j == 0) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 2) {
                                if (j == 0) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 3) {
                                if (j == 0) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 4) {
                                if (j == 0) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 4) {
                                if (j == 0) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(4);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                        }
                        if (k == 1) {
                            if (discreteMonthsviewBys.size() == 1) {
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 2) {
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 3) {
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 4) {
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 5) {
                                if (j == 1) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(4);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                        }
                        if (k == 2) {
                            if (discreteMonthsviewBys.size() == 1) {
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 2) {
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 3) {
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 4) {
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 5) {
                                if (j == 2) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(4);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                        }
                        if (k == 3) {
                            if (discreteMonthsviewBys.size() == 1) {
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 2) {
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 3) {
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 4) {
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 5) {
                                if (j == 3) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(4);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                        }
                        if (k == 4) {
                            if (discreteMonthsviewBys.size() == 1) {
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 2) {
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 3) {
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 4) {
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 5) {
                                if (j == 4) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 8) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(4);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                        }
                        if (k == 5) {
                            if (discreteMonthsviewBys.size() == 1) {
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 2) {
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 3) {
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 4) {
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 8) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 5) {
                                if (j == 5) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 8) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 9) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(4);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                        }
                        if (k == 6) {
                            if (discreteMonthsviewBys.size() == 1) {
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 2) {
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 3) {
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 8) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 4) {
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 8) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 9) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 5) {
                                if (j == 6) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 8) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 9) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 10) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(4);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                            if (discreteMonthsviewBys.size() == 6) {
                                if (j == 7) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(0);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 8) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(1);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 9) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(2);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 10) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(3);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 11) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(4);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                                if (j == 12) {
                                    ArrayList firstValue = (ArrayList) discreMonthMap.get(5);
                                    if (!firstValue.isEmpty() && firstValue.size() > i) {
                                        cell = new Number(j, i + rowStart + 1, Double.parseDouble(firstValue.get(i).toString()), cf2);
                                    } else {
                                        cell = new Label(j, i + rowStart + 1, "", cf2);
                                    }
                                }
                            }
                        }
                    }
                    cell.setCellFormat(writableCellFormat);
                    writableSheet.addCell(cell);

                }
            }
            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return swt.fileName;
    }

    public String downLoadMonthWeekLevleData(String elementID, String regionNmae, ArrayList regionNames, String eleName, PbReturnObject pbretObj, String monthTargetVal, ArrayList testFordayandAllocation) {
        ByteArrayOutputStream bos = null;
        ServletOutputStream outputstream = null;
        Label[] labels = null;
        //common code for all
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableSheet[] writableSheets = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 0;
        ArrayList<String> colNames = new ArrayList<String>();
        ArrayList<String> percentVal = new ArrayList<String>();
        colNames.add(regionNmae);
        percentVal.add(regionNmae);
        if (pbretObj.rowCount > 0) {
            for (int i = 0; i < pbretObj.rowCount; i++) {
                colNames.add(pbretObj.getFieldValueString(i, "MONTH_WEEKS"));
                percentVal.add(pbretObj.getFieldValueString(i, "TARGET_WEEK_VALUE"));
            }
        }
        colNames.add("Total");
        percentVal.add("Total");

        bos = new ByteArrayOutputStream();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;

        try {

            swt = ServletUtilities.createBufferedWriter(this.getFileName(elementID), "xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableSheet = writableWorkbook.createSheet(eleName, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(false);

            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);

            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

            for (int i = 0; i < colNames.size(); i++) {
                label = new Label(i, rowStart, colNames.get(i));
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);
                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 20);
                writableSheet.setColumnView(i, HeadingCellView);
            }

            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

            for (int i = 0; i <= regionNames.size(); i++) {
                double total = 0.0;
                for (int j = 0; j < colNames.size(); j++) {
                    double indialVal = 0.0;
                    if (j != 0 && i != regionNames.size() && j != colNames.size() - 1) {
                        if (!percentVal.get(j).toString().equalsIgnoreCase("")) {
                            indialVal = Double.parseDouble(percentVal.get(j)) / regionNames.size();
                        } else {
                            indialVal = 0.0;
                        }
                    }
                    if (i == regionNames.size()) {
                        if (i == regionNames.size() && j == 0) {
                            cell = new Label(j, i + rowStart + 1, "GRAND TOTAL", cf2);
                        } else if (i == regionNames.size() && j == colNames.size() - 1) {
                            cell = new Number(j, i + rowStart + 1, Double.parseDouble(monthTargetVal), cf2);
                        } else {
                            if (!percentVal.get(j).toString().equalsIgnoreCase("")) {
                                cell = new Number(j, i + rowStart + 1, Double.parseDouble(percentVal.get(j)), cf2);
                            } else {
                                cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                            }
                        }
                    } else {
                        if (j == 0) {
                            cell = new Label(j, i + rowStart + 1, regionNames.get(i).toString(), cf2);
                        } else if (j == colNames.size() - 1) {
                            cell = new Number(j, i + rowStart + 1, total, cf2);
                        } else {
                            cell = new Number(j, i + rowStart + 1, indialVal, cf2);
                            total = total + indialVal;
                        }
                    }
                    cell.setCellFormat(writableCellFormat);
                    writableSheet.addCell(cell);
                }
            }
            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }

        return swt.fileName;
    }

    public String downLoadMonthDayLevleData(String elementID, String regionNmae, ArrayList regionNames, String eleName, PbReturnObject pbretObj, String monthTargetVal, ArrayList testFordayandAllocation) {
        ByteArrayOutputStream bos = null;
        ServletOutputStream outputstream = null;
        Label[] labels = null;
        //common code for all
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableSheet[] writableSheets = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 0;
        ArrayList<String> colNames = new ArrayList<String>();
        ArrayList<String> percentVal = new ArrayList<String>();
        colNames.add(regionNmae);
        percentVal.add(regionNmae);
        if (pbretObj.rowCount > 0) {
            for (int i = 0; i < pbretObj.rowCount; i++) {
                colNames.add(pbretObj.getFieldValueString(i, "MONTH_DATES"));
                percentVal.add(pbretObj.getFieldValueString(i, "TARGET_LEVEL_VALUE"));
            }
        }
        colNames.add("Total");
        percentVal.add("Total");

        bos = new ByteArrayOutputStream();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;

        try {

            swt = ServletUtilities.createBufferedWriter(this.getFileName(elementID), "xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            writableSheet = writableWorkbook.createSheet(eleName, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(false);

            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);

            WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

            for (int i = 0; i < colNames.size(); i++) {
                label = new Label(i, rowStart, colNames.get(i));
                label.setCellFormat(writableCellFormat);
                writableSheet.addCell(label);

                HeadingCellView = new CellView();
                HeadingCellView.setSize(256 * 20);
                writableSheet.setColumnView(i, HeadingCellView);
            }

            writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(true);

            for (int i = 0; i <= regionNames.size(); i++) {

                double total = 0.0;
                for (int j = 0; j < colNames.size(); j++) {
                    if (testFordayandAllocation.get(1).toString().equalsIgnoreCase("autoallocationPercent")) {
                        double indialVal = Double.parseDouble(monthTargetVal) / regionNames.size();
                        if (i == regionNames.size()) {
                            if (i == regionNames.size() && j == 0) {
                                cell = new Label(j, i + rowStart + 1, "GRAND TOTAL", cf2);
                            } else if (i == regionNames.size() && j == colNames.size() - 1) {
                                cell = new Number(j, i + rowStart + 1, Double.parseDouble(monthTargetVal), cf2);
                            } else {
                                cell = new Number(j, i + rowStart + 1, ((indialVal * Double.parseDouble(percentVal.get(j))) / 100) * regionNames.size(), cf2);
                            }
                        } else {
                            if (j == 0) {
                                cell = new Label(j, i + rowStart + 1, regionNames.get(i).toString(), cf2);
                            } else if (j == colNames.size() - 1) {
                                cell = new Number(j, i + rowStart + 1, total, cf2);
                            } else {
                                double val = (indialVal * Double.parseDouble(percentVal.get(j))) / 100;
                                cell = new Number(j, i + rowStart + 1, val, cf2);
                                total = total + val;
                            }
                        }
                    } else {
                        double indialVal = 0.0;

                        if (j != 0 && i != regionNames.size() && j != colNames.size() - 1) {
                            if (!percentVal.get(j).toString().equalsIgnoreCase("")) {
                                indialVal = Double.parseDouble(percentVal.get(j)) / regionNames.size();
                            } else {
                                indialVal = 0.0;
                            }
                        }
                        if (i == regionNames.size()) {
                            if (i == regionNames.size() && j == 0) {
                                cell = new Label(j, i + rowStart + 1, "GRAND TOTAL", cf2);
                            } else if (i == regionNames.size() && j == colNames.size() - 1) {
                                cell = new Number(j, i + rowStart + 1, Double.parseDouble(monthTargetVal), cf2);
                            } else {
                                if (!percentVal.get(j).toString().equalsIgnoreCase("")) {
                                    cell = new Number(j, i + rowStart + 1, Double.parseDouble(percentVal.get(j)), cf2);
                                } else {
                                    cell = new Number(j, i + rowStart + 1, 0.0, cf2);
                                }
                            }
                        } else {
                            if (j == 0) {
                                cell = new Label(j, i + rowStart + 1, regionNames.get(i).toString(), cf2);
                            } else if (j == colNames.size() - 1) {
                                cell = new Number(j, i + rowStart + 1, total, cf2);
                            } else {
                                cell = new Number(j, i + rowStart + 1, indialVal, cf2);
                                total = total + indialVal;
                            }
                        }
                    }
                    cell.setCellFormat(writableCellFormat);
                    writableSheet.addCell(cell);
                }
            }
            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }

        return swt.fileName;
    }

    public String generateOneviewMeasureTimeData(String eleId, String userId, String measurName, List<String> detailValues, List<String> attacheDetails, boolean dsbordorOneview) {
        ServletWriterTransferObject swt = null;
        try {
            swt = ServletUtilities.createBufferedWriter(this.getFileName(eleId, userId), "html");
            Writer writer = swt.writer;
            StringBuilder tableHtml = new StringBuilder();
            writer.write(tableHtml.append(getOneviewMeasureDetail(measurName, detailValues, attacheDetails, dsbordorOneview)).toString());
            writer.flush();
            writer.close();
            swt.setReportName(measurName);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return swt.fileName;
    }

    public String getOneviewMeasureDetail(String measurName, List<String> detailValues, List<String> attacheDetails, boolean dsbordorOneview) {
        StringBuilder val = new StringBuilder();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy");
        ArrayList headType = new ArrayList();
        for (int i = 0; i < attacheDetails.size(); i++) {
            if (attacheDetails.get(i).equalsIgnoreCase("Year")) {
                headType.add("YTD");
            } else if (attacheDetails.get(i).equalsIgnoreCase("Qtr")) {
                headType.add("QTD");
            } else if (attacheDetails.get(i).equalsIgnoreCase("Month")) {
                headType.add("MTD");
            } else if (attacheDetails.get(i).equalsIgnoreCase("Week")) {
                headType.add("WTD");
            } else if (attacheDetails.get(i).equalsIgnoreCase("Day")) {
                headType.add("Daily");
            } else if (attacheDetails.get(i).equalsIgnoreCase("deviation")) {
                headType.add("Dev Percentage");
            } else if (attacheDetails.get(i).equalsIgnoreCase("target")) {
                headType.add("Target Value");
            }
        }
        if (dsbordorOneview) {
            headType.add("Kpi");
        } else {
            headType.add("Measure");
        }
        detailValues.add(measurName);

        String tdStyle = "font-family:Verdana, Arial, Helvetica, sans-serif;font-size:10pt;font-weight:bold;color:rgb(51, 102, 153);padding-left:12px;background-color:gainsboro;";
        String background = "font-family:Verdana, Arial, Helvetica, sans-serif;font-size:10pt;font-weight:bold;color:rgb(51, 102, 153);padding-left:12px;";
        val.append("<html><body>");
        val.append("<br><table><tr>");
//         val.append("<tr><td>").append(new DataSnapshotDAO().getHeaderTitleForSnapshot()).append("</td></tr>");
//         val.append("<tr><td>").append("Created Date: ").append(sdf.format(date));
        val.append("</tr></table><br><br>");

        val.append("<table border='1'  width='100%' style='border-collapse:collapse;'>");
        val.append("<thead><tr>");
        for (int i = headType.size() - 1; i >= 0; i--) {
            val.append("<th width='12%' height='30' style=\"").append(tdStyle).append("\">").append(headType.get(i)).append("</th>");
        }
        val.append("</tr></thead><tbody>");

        StringBuilder oneviewAlerts = new StringBuilder();
        StringBuilder singleRow = new StringBuilder();
        singleRow.append("<tr>");
        for (int i = detailValues.size() - 1; i >= 0; i--) {
            if (i == detailValues.size() - 1) {
                singleRow.append("<td align='left' height='30' style=\"").append(background).append("\">").append(detailValues.get(i)).append("</td>");
            } else {
                singleRow.append("<td align='left' height='30'>").append(detailValues.get(i)).append("</td>");
            }
        }
        singleRow.append("</tr>");
        oneviewAlerts.append(val);
        oneviewAlerts.append(singleRow);
        oneviewAlerts.append("</tbody></table></body></html>");

        return oneviewAlerts.toString();
    }

    public String generateReportInfowithoutParam(Container container) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        ArrayList timeDetailsArray = null;
        ArrayList<String> paramNames = new ArrayList<String>();
        sdf.applyPattern("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        String headerTitle = dataSnapshotDAO.getHeaderTitleForSnapshot();
        timeDetailsArray = container.getTimeDetailsArray();

        StringBuilder repInfoBuilder = new StringBuilder("");
        repInfoBuilder.append(this.generateJS());
        repInfoBuilder.append("<table id='parameterRegion'>");
//        repInfoBuilder.append("<tr>");
//        repInfoBuilder.append("<td><h4>").append(headerTitle).append("</h4></td>");
//        repInfoBuilder.append("</tr>");

        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Report Title: </b> </td><td style='font-size:14px;'>").append(container.getReportName().replace("_", " ")).append("</td>");
        repInfoBuilder.append("</tr>");

        repInfoBuilder.append("<tr>");
        repInfoBuilder.append("<td style='font-size: 13px;background-color: rgb(230, 230, 230);color:#336699;font-family: verdana;text-align: left;'><b>Created Date: </b> </td><td style='font-size:14px;'>").append(sdf.format(calendar.getTime())).append("</td>");
        repInfoBuilder.append("</tr>");
        repInfoBuilder.append("</table>");
        repInfoBuilder.append("<br/><br/>");
        return repInfoBuilder.toString();
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

    public void sendMail(String fileName, String mailIds) {
        String dir = System.getProperty("java.io.tmpdir") + "/";
        PbMailParams params = null;
        PbMail mailer = null;
        ArrayList<String> attch = new ArrayList<String>();

//               String str=new String(dir.concat(fileName));
        String str = dir.concat(fileName);

        attch.add(str);

        String toAddress = mailIds;

        params = new PbMailParams();
        params.setBodyText("");
        params.setToAddr(toAddress);
        params.setSubject("ScheduleFiles");
        params.setHasAttach(true);
        params.setAttachFile(attch);

        try {
            mailer = new PbMail(params);
            mailer.sendMail();
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public StringBuilder generateTopBottomTable(Container container, String userId, String higth, String width) {
        StringBuilder htmlVal = new StringBuilder();
        PbReportViewerBD viewerbd = new PbReportViewerBD();
        String reportId = container.getReportId();
        viewerbd.generateMutliPeriodReturnObject(reportId, container, userId);
        String perBy = container.getTopBottomTableHashMap().get("TopBottomVal");
        String measureBy = container.getTopBottomTableHashMap().get("TopBottomMsr");
        MultiPeriodKPI pbretObj1 = container.getMultiPeriodKPI();
        ReportTemplateBD bD = new ReportTemplateBD();
        ArrayList REPNames = (ArrayList) container.getTableHashMap().get("REPNames");
        ArrayList displayCols = container.getDisplayColumns();
        ArrayList displayLabels = container.getDisplayLabels();
        String selectedMsrName = "";
        if (displayCols.contains(measureBy)) {
            selectedMsrName = String.valueOf(displayLabels.get(displayCols.indexOf(measureBy)));
        }
        boolean isTop = true;
        int endCount = 5;
        String monthval = "";
        String yearval = "";
        String Qrtval = "";
        String dayval = "";
        String weekval = "";
        if (perBy != null && !perBy.isEmpty() && (perBy.equalsIgnoreCase("Bottom5") || perBy.equalsIgnoreCase("Bottom10") || perBy.equalsIgnoreCase("Bottom25"))) {
            isTop = false;
        }
        if (perBy != null && !perBy.isEmpty() && (perBy.equalsIgnoreCase("Bottom5") || perBy.equalsIgnoreCase("Top5"))) {
            endCount = 5;
        }
        if (perBy != null && !perBy.isEmpty() && (perBy.equalsIgnoreCase("Bottom10") || perBy.equalsIgnoreCase("Top10"))) {
            endCount = 10;
        }
        if (perBy != null && !perBy.isEmpty() && (perBy.equalsIgnoreCase("Bottom25") || perBy.equalsIgnoreCase("Top25"))) {
            endCount = 25;
        }
        if (pbretObj1 != null && pbretObj1.getMonthObject() != null) {
            monthval = bD.TopBottom(reportId, pbretObj1.getMonthObject(), container, isTop, endCount);
        }
        if (pbretObj1 != null && pbretObj1.getYearObject() != null) {
            yearval = bD.TopBottom(reportId, pbretObj1.getYearObject(), container, isTop, endCount);
        }
        if (pbretObj1 != null && pbretObj1.getQuarterObject() != null) {
            Qrtval = bD.TopBottom(reportId, pbretObj1.getQuarterObject(), container, isTop, endCount);
        }
        if (pbretObj1 != null && pbretObj1.getWeekObject() != null) {
            weekval = bD.TopBottom(reportId, pbretObj1.getWeekObject(), container, isTop, endCount);
        }
        if (pbretObj1 != null && pbretObj1.getDayObject() != null) {
            dayval = bD.TopBottom(reportId, pbretObj1.getDayObject(), container, isTop, endCount);
        }
        if (weekval == null || weekval.equalsIgnoreCase("") || weekval.isEmpty()) {
//                                  Weekheader="Week(Month)";
            weekval = monthval;
        }
        htmlVal.append("<br><center><table style='width:").append(Integer.parseInt(width) - 10).append("px;'  cellspacing='0' border='1px;' border-color='rgb(230, 238, 238);'>");
//          htmlVal.append("<thead style='height: 25;background-color: lightgray; background: linear-gradient(to bottom, #D5E3E4 0%, #CCDEE0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent;'>");
//          htmlVal.append("<tr>");
//          htmlVal.append("<td><table><tr><td align='left'>").append((String)REPNames.get(0)).append("</td><td align='right'>Day(").append(selectedMsrName).append(")</td></tr></table></td>");
////          htmlVal.append("<th style=\"color: black;font-size: 11px;font-weight: bold; width: 20%\">Day </th>");
//          htmlVal.append("<th style=\"color: black;font-size: 11px;font-weight: bold; width: 20%\">Week</th>");
//          htmlVal.append("<th style=\"color: black;font-size: 11px;font-weight: bold; width: 20%\">Month</th>");
//          htmlVal.append("<th style=\"color: black;font-size: 11px;font-weight: bold; width: 20%\">Qtr</th>");
//          htmlVal.append("<th style=\"color: black;font-size: 11px;font-weight: bold; width: 20%\">Year</th>");
//          htmlVal.append("</tr>");
//          htmlVal.append("</thead>");
        htmlVal.append("<thead>");
        htmlVal.append("<tr style=\"background-color: rgb(230, 238, 238);\">");
        htmlVal.append("<td><table style='background-color: rgb(230, 238, 238);width:100%;'><tr><td align='left'>").append((String) REPNames.get(0)).append("</td><td align='center'>Day(").append(selectedMsrName).append(")</td></tr></table></td>");
        htmlVal.append("<td><table style='background-color: rgb(230, 238, 238);width:100%;'><tr><td align='left'>").append((String) REPNames.get(0)).append("</td><td align='center'>Week(").append(selectedMsrName).append(")</td></tr></table></td>");
        htmlVal.append("<td><table style='background-color: rgb(230, 238, 238);width:100%;'><tr><td align='left'>").append((String) REPNames.get(0)).append("</td><td align='center'>Month(").append(selectedMsrName).append(")</td></tr></table></td>");
        htmlVal.append("<td><table style='background-color: rgb(230, 238, 238);width:100%;'><tr><td align='left'>").append((String) REPNames.get(0)).append("</td><td align='center'>Qtr(").append(selectedMsrName).append(")</td></tr></table></td>");
        htmlVal.append("<td><table style='background-color: rgb(230, 238, 238);width:100%;'><tr><td align='left'>").append((String) REPNames.get(0)).append("</td><td align='center'>Year(").append(selectedMsrName).append(")</td></tr></table></td>");
        htmlVal.append("</tr>");
        htmlVal.append("</thead>");
        htmlVal.append("<tbody style=\" height: 262px;   overflow: auto;  width: 100%;\">");
        htmlVal.append("<tr valign=\"top\" style=\"height: 75%\">");
        htmlVal.append("<td><table style='line-height:10px;width:100%' cellpadding='0' cellspacing='0'>").append(dayval).append("</table></td>");
        htmlVal.append("<td><table style='line-height:10px;width:100%' cellpadding='0' cellspacing='0'>").append(weekval).append("</table></td>");
        htmlVal.append("<td><table style='line-height:10px;width:100%' cellpadding='0' cellspacing='0'>").append(monthval).append("</table></td>");
        htmlVal.append("<td><table style='line-height:10px;width:100%' cellpadding='0' cellspacing='0'>").append(Qrtval).append("</table></td>");
        htmlVal.append("<td><table style='line-height:10px;width:100%' cellpadding='0' cellspacing='0'>").append(yearval).append("</table></td>");
        htmlVal.append("</tr>");
        htmlVal.append("</tbody>");
        htmlVal.append("</table>");
        return htmlVal;
    }
    // code added by bhargavi for grandtotal on 15th Dec 2014

    public BigDecimal getComputeFormulaVal(Container container, int column, String tempFormula, String mysqlString, String stType) {
        PbDb pbdb = new PbDb();
        String formula = "";
        ProgenDataSet ret = container.getRetObj();
        ArrayList displayValues = new ArrayList();
        displayValues = container.getDisplayColumns();
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
            logger.error("Exception:", ex);
        }
        if (retobj2 != null && retobj2.getRowCount() > 0) {
            formula = retobj2.getFieldValueString(0, 0);
            if (formula.equalsIgnoreCase("")) {
                formula = "0";
            }
            BigDecimal subTotalVal = new BigDecimal(formula);
            subTotalVal = subTotalVal.setScale(2, RoundingMode.CEILING);
            return subTotalVal;
        } else {
            if (stType.equalsIgnoreCase("GT")) {
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
    }

    public BigDecimal getGTForChangePerOfSummTabMeasures(Container container, String eleId, int column) {
        PbDb pbdb = new PbDb();
        ArrayList displayValues = new ArrayList();
        ProgenDataSet ret = container.getRetObj();
        displayValues = container.getDisplayColumns();
        PbReturnObject retobj = new PbReturnObject();
        String tempFormula = "(((CASE WHEN SUM(CURRENT) IS NULL THEN 0 ELSE SUM(CURRENT)*1.0 END )-(CASE WHEN SUM(PRIOR) IS NULL THEN 0 ELSE SUM(PRIOR)*1.0 END ))/(CASE WHEN SUM(PRIOR) = 0  THEN NULL ELSE SUM(PRIOR)*1.0 END ))*100";
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE,AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN "
                + " (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + eleId + ") AND REF_ELEMENT_TYPE in (1,2)";
        StringBuilder mysqlString = new StringBuilder();
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
                            mysqlString.append(mysqlString).append(",").append(gtValueForEle).append(" AS ").append(retobj.getFieldValueString(i, 0));
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
                        return ret.getColumnGrandTotalValue(displayValues.get(column).toString());
                    }
                }
                //Calculate formula
                if (!tempFormula.equalsIgnoreCase("")) {
                    container.summarizedHashmap.put(displayValues.get(column).toString(), "OTHERS");
                    return getComputeFormulaVal(container, column, tempFormula, mysqlString.toString(), "GT");
                } else {
                    return ret.getColumnGrandTotalValue(displayValues.get(column).toString());
                }
            } else {
                return ret.getColumnGrandTotalValue(displayValues.get(column).toString());
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            return ret.getColumnGrandTotalValue(displayValues.get(column).toString());
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
//end of code by bhargavi for grandtotal on 15th Dec 2014

    public void alertsMailForKPIDashboard(ReportSchedule schedule, String reportId, String userId) {
        Container container = null;
        String result = "";
        pbDashboardCollection collect = null;
        HashMap DBKPIHashMap = null;
        try {
            container = new DataSnapshotBD().generateKpidsbSchedulerContainer(reportId, userId);
            DBKPIHashMap = container.getDBKPIHashMap();
            collect = (pbDashboardCollection) container.getReportCollect();
            collect.setDBKPIHashMap(DBKPIHashMap);
            collect.reportId = reportId;//here reportId is DashBoard Id
            collect.reportIncomingParameters = container.getRepReqParamsHashMap();
            KPIBuilderScheduler kpibuilderScheduler = new KPIBuilderScheduler();
            kpibuilderScheduler.setElemntIdForMail(schedule.getElementID());
            kpibuilderScheduler.setSchedulerFlag(false);

            try {
                result = kpibuilderScheduler.processSingleKpiScheduler(container, schedule.getkPIScheduleHelper().getKpiMasterId(), collect.kpiQuery, "Y", schedule.getkPIScheduleHelper().getDashLetId(), reportId, false, collect, userId, "false");
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        try {
            //code written by Dinanath for applying alert as on 10/08/2015

            ArrayList viewBy = container.getViewBy();
            String dashBoardName = container.getDbrdName();
            String allHeaderName = (String) container.getCustomKPIHeaderNames();
            String allHeaderNameValue[] = allHeaderName.split(",");
            ArrayList allValues = new ArrayList();
//                   allValues.add(allHeaderName);
            ArrayList<ArrayList<String>> allValuesContent = (ArrayList) container.getCalculativeVal();
            ArrayList<ArrayList<String>> allValuesOfGT = (ArrayList) container.getGtVal();
            for (int m = 0; m < allValuesOfGT.size(); m++) {
                ArrayList<String> allValuesOnThisGT = (ArrayList<String>) allValuesOfGT.get(m);
                allValuesContent.add(allValuesOnThisGT);
            }
            String kpiElementIds = (String) container.getKPIElementIds();
            String kpiElementMesureName = (String) container.getKPIElementMesureName();
            String kpiElementIds1 = kpiElementIds.replaceFirst(",", "");
            String kpiElementIds2[] = kpiElementIds1.split(",");
            String kpiElementMesureName2[] = kpiElementMesureName.split(",");

            String setViewByName = schedule.getViewByName();
            String elementId = schedule.getElementID();
            String firstViewByName = schedule.getFirstViewByName();
            String secondViewByName = schedule.getSecondViewByName();

            String[] startValue = schedule.getStartValue();
            String[] endValue = schedule.getEndValue();
            String[] opratorValue = schedule.getOperatorSymbol();
            String[] alertType = schedule.getAlertType();
            String[] selectedColumn = schedule.getColumnHeaderNames();
            StringBuffer completeMessageHeader = new StringBuffer();
            StringBuffer completeMessageFooter = new StringBuffer();
            boolean isHeaderMsg = false;
            boolean isFooterMsg = false;
            String isHeaderLogoOn = schedule.getIsHeaderLogoOn();
            String isOptionalHeaderTextOn = schedule.getIsOptionalHeaderTextOn();
            String isOptionalFooterTextOn = schedule.getIsOptionalFooterTextOn();
            String isHtmlSignatureOn = schedule.getIsHtmlSignatureOn();
            String isFooterLogoOn = schedule.getIsFooterLogoOn();
            try {


                if (isHeaderLogoOn != null && isHeaderLogoOn.equalsIgnoreCase("on")) {
                    completeMessageHeader.append("<div style='width:100%;height:30%;'><img src=\"cid:image\" height=\"160\" width=\"1100\"></div>");
                    isHeaderMsg = true;
                }
                if (isOptionalHeaderTextOn != null && isOptionalHeaderTextOn.equalsIgnoreCase("on")) {
                    String filePath2 = "c://usr/local/cache";
                    String htmlSignatureFile = "HtmlOptionalHeaderForScheduler.html";
                    BufferedReader br2 = null;
                    try {
                        br2 = new BufferedReader(new FileReader(filePath2 + "/" + htmlSignatureFile));


                        String line = br2.readLine();
                        while (line != null) {
                            completeMessageHeader.append(line);
                            line = br2.readLine();
                        }
                        isHeaderMsg = true;
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    } finally {
                        if (br2 != null) {
                            br2.close();
                        }
                    }
                }


                if (isOptionalFooterTextOn != null && isOptionalFooterTextOn.equalsIgnoreCase("on")) {
                    String filePath2 = "c://usr/local/cache";
                    String htmlSignatureFile = "HtmlOptionalFooterForScheduler.html";
                    BufferedReader br2 = null;
                    try {
                        br2 = new BufferedReader(new FileReader(filePath2 + "/" + htmlSignatureFile));


                        String line = br2.readLine();
                        while (line != null) {
                            completeMessageFooter.append(line);
                            line = br2.readLine();
                        }
                        isFooterMsg = true;
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    } finally {
                        if (br2 != null) {
                            br2.close();
                        }
                    }
                }
                if (isHtmlSignatureOn != null && isHtmlSignatureOn.equalsIgnoreCase("on")) {
                    String filePath2 = "c://usr/local/cache";
                    String htmlSignatureFile = "HtmlSignatureForScheduler.html";
                    BufferedReader br2 = null;
                    try {
                        br2 = new BufferedReader(new FileReader(filePath2 + "/" + htmlSignatureFile));
                        String line = br2.readLine();
                        while (line != null) {
                            completeMessageFooter.append(line);
                            line = br2.readLine();
                        }
                        isFooterMsg = true;
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    } finally {
                        if (br2 != null) {
                            br2.close();
                        }
                    }
                }
                if (isFooterLogoOn != null && isFooterLogoOn.equalsIgnoreCase("on")) {
                    completeMessageFooter.append("<div style='width:100%;height:50px;'><table><tr><td><img src=\"cid:pidina_image\" height=\"100\" width=\"200\"/></td><td style=\"width: 750px;font-size:30px\"><center> Powered By ProGen Business Solutions</center></td><td><img src=\"cid:progendina_image\" height=\"100\" width=\"200\"/></td></tr></table></div>");
                    isFooterMsg = true;
                }
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
            params = new PbMailParams();
            String toAddress = schedule.getReportmailIds();
            params.setToAddr(toAddress);
            params.setSubject(schedule.getSchedulerName());
            params.setHasAttach(false);
            params.setIncludeURLMessage(false);
            params.setIsHeaderLogoOn(isHeaderLogoOn);
            params.setIsFooterLogoOn(isFooterLogoOn);
            if (viewBy.size() == 2 && !firstViewByName.isEmpty() && !secondViewByName.isEmpty()) {
                if (firstViewByName.equalsIgnoreCase("GTValueAlertsTrue1") && secondViewByName.equalsIgnoreCase("GTValueAlertsTrue2")) {
                } else {
                    for (int i = 0; i < kpiElementMesureName2.length; i++) {//first for loop
                        if (kpiElementMesureName2[i].equalsIgnoreCase(setViewByName)) {
                            for (int m = 0; m < allValuesContent.size(); m++) {
                                ArrayList<String> allValuesOnThisMesure = (ArrayList<String>) allValuesContent.get(m);
                                if (allValuesOnThisMesure.toString().contains(setViewByName) && (allValuesOnThisMesure.toString().contains(firstViewByName) && allValuesOnThisMesure.toString().contains(secondViewByName))) {
                                    for (int n = 0; n < selectedColumn.length; n++) {
                                        for (int j = 0; j < allValuesOnThisMesure.size(); j++) {//allV
                                            if (allHeaderNameValue[j].equalsIgnoreCase(selectedColumn[n])) {
//                                      for(int k=0;k<opratorValue.size();k++){
                                                if (allValuesOnThisMesure.get(j).toString().contains("--")) {
                                                    StringBuilder message = new StringBuilder();

                                                    message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append(" </u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                    message.append("<h3>  ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(allHeaderNameValue[1]).append(" : ").append(secondViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which could not be compared with the value of ").append(startValue[n]).append("</h3><br/>");
                                                    message.append("</div>");
                                                    if (isHeaderMsg == true) {
                                                        message.append(completeMessageHeader).append(message);
                                                    }
                                                    if (isFooterMsg == true) {
                                                        message.append(message).append(completeMessageFooter);
                                                    }
                                                    params.setBodyText(message.toString());

                                                    try {
                                                        mailer = new PbMail(params);
                                                        mailer.sendMail();
                                                    } catch (Exception e) {
                                                        logger.error("Exception:", e);
                                                    }
                                                } else {
//                                   String firstNumber=null;
                                                    StringBuilder firstNumber = new StringBuilder();
//                                   String secondNumber=null;
                                                    StringBuilder secondNumber = new StringBuilder();
                                                    Double firstNum = null;
                                                    Double secondNum = null;
                                                    if (allValuesOnThisMesure.get(j).toString().contains("-")) {
                                                        firstNumber.append(allValuesOnThisMesure.get(j).toString().replaceAll("[^\\d.]", ""));
                                                        firstNumber.append("-").append(firstNumber);
                                                    } else {
                                                        firstNumber.append(allValuesOnThisMesure.get(j).toString().replaceAll("[^\\d.]", ""));
                                                    }
                                                    if (startValue[n].toString().contains("-")) {
                                                        secondNumber.append(startValue[n].toString().replaceAll("[^\\d.]", ""));
                                                        secondNumber.append("-").append(secondNumber);
                                                    } else {
                                                        secondNumber.append(startValue[n].toString().replaceAll("[^\\d.]", ""));
                                                    }

                                                    if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("K")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("L")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 100000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("M")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 10000000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("K")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 100000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("L")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 100000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 100000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("M")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 100000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 100000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 10000000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("K")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("L")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 100000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("M")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 10000000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("K")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 10000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("L")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 10000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 100000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("M")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 10000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 10000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 10000000;
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 100000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 1000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                    } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        firstNum *= 10000000;
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                    } else if (startValue[n].toString().toUpperCase().contains("K")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000;
                                                    } else if (startValue[n].toString().toUpperCase().contains("L")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 100000;
                                                    } else if (startValue[n].toString().toUpperCase().contains("M")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 1000000;
                                                    } else if (startValue[n].toString().toUpperCase().contains("Cr")) {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                        secondNum *= 10000000;
                                                    } else {
                                                        firstNum = Double.parseDouble(firstNumber.toString().isEmpty() ? "0" : firstNumber.toString());
                                                        secondNum = Double.parseDouble(secondNumber.toString().isEmpty() ? "0" : secondNumber.toString());
                                                    }
                                                    if (opratorValue[n].toString().equalsIgnoreCase(">")) {
                                                        if (firstNum > secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                            StringBuilder message = new StringBuilder();
                                                            message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : " + allHeaderNameValue[j] + "</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                            if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                                message.append("<h3> " + firstViewByName + " " + setViewByName + " at " + allHeaderNameValue[j] + " : " + allValuesOnThisMesure.get(j) + " which is greater than the value of " + startValue[n] + "</h3><br/>");
                                                            } else {
                                                                message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(allHeaderNameValue[1]).append(" : ").append(secondViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than the value of ").append(startValue[n]).append("</h3><br/>");
                                                            }
                                                            message.append("</div>");
                                                            if (isHeaderMsg == true) {
                                                                message.append(completeMessageHeader).append(message);
                                                            }
                                                            if (isFooterMsg == true) {
                                                                message.append(message).append(completeMessageFooter);
                                                            }
                                                            params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                            try {
                                                                mailer = new PbMail(params);
                                                                mailer.sendMail();
                                                            } catch (Exception e) {
                                                                logger.error("Exception:", e);
                                                            }
                                                        }
                                                    } else if (opratorValue[n].toString().equalsIgnoreCase("<")) {
                                                        if (firstNum < secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                            StringBuilder message = new StringBuilder();
                                                            message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                            if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                                message.append("<h3> " + firstViewByName + " " + setViewByName + " at " + allHeaderNameValue[j] + " : " + allValuesOnThisMesure.get(j) + " which is less than the value of " + startValue[n] + "</h3><br/>");
                                                            } else {
                                                                message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(allHeaderNameValue[1]).append(" : ").append(secondViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is less than the value of ").append(startValue[n]).append("</h3><br/>");
                                                            }
                                                            message.append("</div>");
                                                            if (isHeaderMsg == true) {
                                                                message.append(completeMessageHeader).append(message);
                                                            }
                                                            if (isFooterMsg == true) {
                                                                message.append(message).append(completeMessageFooter);
                                                            }
                                                            params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                            try {
                                                                mailer = new PbMail(params);
                                                                mailer.sendMail();
                                                            } catch (Exception e) {
                                                                logger.error("Exception:", e);
                                                            }
                                                        }
                                                    } else if (opratorValue[n].toString().equalsIgnoreCase(">=")) {

                                                        if (firstNum >= secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                            StringBuilder message = new StringBuilder();
                                                            message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: " + alertType[n] + "</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : " + setViewByName + "</h3>");
                                                            if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                                message.append("<h3> ").append(firstViewByName).append("  ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                            } else {
                                                                message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(allHeaderNameValue[1]).append(" : ").append(secondViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                            }
                                                            message.append("</div>");
                                                            if (isHeaderMsg == true) {
                                                                message.append(completeMessageHeader).append(message);
                                                            }
                                                            if (isFooterMsg == true) {
                                                                message.append(message).append(completeMessageFooter);
                                                            }
                                                            params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                            try {
                                                                mailer = new PbMail(params);
                                                                mailer.sendMail();
                                                            } catch (Exception e) {
                                                                logger.error("Exception:", e);
                                                            }
                                                        }
                                                    } else if (opratorValue[n].toString().equalsIgnoreCase("<=")) {
                                                        if (firstNum <= secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                            StringBuilder message = new StringBuilder();
                                                            message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                            if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                                message.append("<h3>").append(firstViewByName).append(" ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is less than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                            } else {
                                                                message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(allHeaderNameValue[1]).append(" : ").append(secondViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is less than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                            }

                                                            message.append("</div>");
                                                            if (isHeaderMsg == true) {
                                                                message.append(completeMessageHeader).append(message);
                                                            }
                                                            if (isFooterMsg == true) {
                                                                message.append(message).append(completeMessageFooter);
                                                            }
                                                            params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                            try {
                                                                mailer = new PbMail(params);
                                                                mailer.sendMail();
                                                            } catch (Exception e) {
                                                                logger.error("Exception:", e);
                                                            }
                                                        }
                                                    } else if (opratorValue[n].toString().equalsIgnoreCase("=")) {
                                                        if (firstNum == secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                            StringBuilder message = new StringBuilder();
                                                            message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                            if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                                message.append("<h3> ").append(firstViewByName).append(" ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                            } else {
                                                                message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(allHeaderNameValue[1]).append(" : ").append(secondViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                            }
                                                            message.append("</div>");
                                                            if (isHeaderMsg == true) {
                                                                message.append(completeMessageHeader).append(message);
                                                            }
                                                            if (isFooterMsg == true) {
                                                                message.append(message).append(completeMessageFooter);
                                                            }
                                                            params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                            try {
                                                                mailer = new PbMail(params);
                                                                mailer.sendMail();
                                                            } catch (Exception e) {
                                                                logger.error("Exception:", e);
                                                            }
                                                        }
                                                    } else if (opratorValue[n].toString().equalsIgnoreCase("!=")) {
                                                        if (firstNum < secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                            StringBuilder message = new StringBuilder();
                                                            message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                            if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                                message.append("<h3>  " + firstViewByName + " " + setViewByName + " at " + allHeaderNameValue[j] + " : " + allValuesOnThisMesure.get(j) + " which is not equal to the value of " + startValue[n] + "</h3><br/>");
                                                            } else {
                                                                message.append("<h3> " + allHeaderNameValue[0] + " : " + firstViewByName + ", " + allHeaderNameValue[1] + " : " + secondViewByName + ", " + setViewByName + " at " + allHeaderNameValue[j] + " : " + allValuesOnThisMesure.get(j) + " which is not equal to the value of " + startValue[n] + "</h3><br/>");
                                                            }
                                                            message.append("</div>");
                                                            if (isHeaderMsg == true) {
                                                                message.append(completeMessageHeader).append(message);
                                                            }
                                                            if (isFooterMsg == true) {
                                                                message.append(message).append(completeMessageFooter);
                                                            }
                                                            params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                            try {
                                                                mailer = new PbMail(params);
                                                                mailer.sendMail();
                                                            } catch (Exception e) {
                                                                logger.error("Exception:", e);
                                                            }
                                                        }
                                                    } else if (opratorValue[n].toString().equalsIgnoreCase("<>")) {
                                                        String thirdNumber = null;
                                                        Double thirdNum = null;
                                                        thirdNumber = endValue[n].toString().replaceAll("[^\\d.]", "");
                                                        if (endValue[n].toString().toUpperCase().contains("K")) {
                                                            thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                            thirdNum *= 1000;
                                                        } else if (endValue[n].toString().toUpperCase().contains("L")) {
                                                            thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                            thirdNum *= 100000;
                                                        } else if (endValue[n].toString().toUpperCase().contains("M")) {
                                                            thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                            thirdNum *= 1000000;
                                                        } else if (endValue[n].toString().toUpperCase().contains("Cr")) {
                                                            thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                            thirdNum *= 10000000;
                                                        } else {
                                                            thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        }


                                                        if (firstNum > secondNum && firstNum < thirdNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                            StringBuilder message = new StringBuilder();
                                                            message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                            if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                                message.append("<h3> ").append(firstViewByName).append("  ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than the value of ").append(startValue[n]).append(" and less than the value of ").append(endValue[n]).append("</h3>");
                                                            } else {
                                                                message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(allHeaderNameValue[1]).append(" : ").append(secondViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than the value of ").append(startValue[n]).append(" and less than the value of ").append(endValue[n]).append("</h3>");
                                                            }

                                                            message.append("</div>");
                                                            if (isHeaderMsg == true) {
                                                                message.append(completeMessageHeader).append(message);
                                                            }
                                                            if (isFooterMsg == true) {
                                                                message.append(message).append(completeMessageFooter);
                                                            }
                                                            params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                            try {
                                                                mailer = new PbMail(params);
                                                                mailer.sendMail();
                                                            } catch (Exception e) {
                                                                logger.error("Exception:", e);
                                                            }
                                                        }
                                                    }
                                                }//else
                                            }//
                                        }
//                               }
                                    }//allV
                                }
                            }
                        }
                    }//end of first for loop
                }
            }//end first if
            else if (viewBy.size() == 1 && !firstViewByName.isEmpty()) {
                for (int i = 0; i < kpiElementMesureName2.length; i++) {//first for loop
                    if (kpiElementMesureName2[i].equalsIgnoreCase(setViewByName)) {
                        for (int m = 0; m < allValuesContent.size(); m++) {
                            ArrayList<String> allValuesOnThisMesure = (ArrayList<String>) allValuesContent.get(m);
                            if (allValuesOnThisMesure.toString().contains(setViewByName) && allValuesOnThisMesure.toString().contains(firstViewByName)) {
                                for (int n = 0; n < selectedColumn.length; n++) {
                                    for (int j = 0; j < allValuesOnThisMesure.size(); j++) {//allV
                                        if (allHeaderNameValue[j].equalsIgnoreCase(selectedColumn[n])) {
//                                      for(int k=0;k<opratorValue.size();k++){
                                            if (allValuesOnThisMesure.get(j).toString().contains("--")) {
//                                       String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                StringBuilder message = new StringBuilder();
                                                message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                    message.append("<h3>  ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which could not be compared with the value of ").append(startValue[n]).append("</h3><br/>");
                                                } else {
                                                    message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which could not be compared with the value of ").append(startValue[n]).append("</h3><br/>");
                                                }

                                                message.append("</div>");
                                                if (isHeaderMsg == true) {
                                                    message.append(completeMessageHeader).append(message);
                                                }
                                                if (isFooterMsg == true) {
                                                    message.append(message).append(completeMessageFooter);
                                                }
                                                params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                try {
                                                    mailer = new PbMail(params);
                                                    mailer.sendMail();
                                                } catch (Exception e) {
                                                    logger.error("Exception:", e);
                                                }
                                            } else {
                                                String firstNumber = null;
                                                String secondNumber = null;
                                                Double firstNum = null;
                                                Double secondNum = null;
                                                if (allValuesOnThisMesure.get(j).toString().contains("-")) {
                                                    firstNumber = allValuesOnThisMesure.get(j).toString().replaceAll("[^\\d.]", "");
                                                    firstNumber = "-" + firstNumber;
                                                } else {
                                                    firstNumber = allValuesOnThisMesure.get(j).toString().replaceAll("[^\\d.]", "");
                                                }
                                                if (startValue[n].toString().contains("-")) {
                                                    secondNumber = startValue[n].toString().replaceAll("[^\\d.]", "");
                                                    secondNumber = "-" + secondNumber;
                                                } else {
                                                    secondNumber = startValue[n].toString().replaceAll("[^\\d.]", "");
                                                }
                                                if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                } else if (startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                }
                                                if (opratorValue[n].toString().equalsIgnoreCase(">")) {
                                                    if (firstNum > secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                            message.append("<h3>  ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than the value of ").append(startValue[n]).append("</h3><br/>");
                                                        } else {
                                                            message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than the value of ").append(startValue[n]).append("</h3><br/>");
                                                        }
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());

                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("<")) {
                                                    if (firstNum < secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                            message.append("<h3>  ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is less than the value of ").append(startValue[n]).append("</h3><br/>");
                                                        } else {
                                                            message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is less than the value of ").append(startValue[n]).append("</h3><br/>");
                                                        }

                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());

                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase(">=")) {

                                                    if (firstNum >= secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                            message.append("<h3>  ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        } else {
                                                            message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        }

                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("<=")) {
                                                    if (firstNum <= secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                            message.append("<h3>  ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is less than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        } else {
                                                            message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is less than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        }
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
                                                        params.setToAddr(toAddress);
                                                        params.setSubject(schedule.getSchedulerName());
                                                        params.setHasAttach(false);
                                                        params.setIncludeURLMessage(false);
                                                        params.setIsHeaderLogoOn(isHeaderLogoOn);
                                                        params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("=")) {
                                                    if (firstNum == secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                            message.append("<h3>  ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        } else {
                                                            message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        }

                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("!=")) {
                                                    if (firstNum < secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                            message.append("<h3>  ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is not equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        } else {
                                                            message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is not equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        }

                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("<>")) {
                                                    String thirdNumber = null;
                                                    Double thirdNum = null;
                                                    thirdNumber = endValue[n].toString().replaceAll("[^\\d.]", "");
                                                    if (endValue[n].toString().toUpperCase().contains("K")) {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        thirdNum *= 1000;
                                                    } else if (endValue[n].toString().toUpperCase().contains("L")) {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        thirdNum *= 100000;
                                                    } else if (endValue[n].toString().toUpperCase().contains("M")) {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        thirdNum *= 1000000;
                                                    } else if (endValue[n].toString().toUpperCase().contains("Cr")) {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        thirdNum *= 10000000;
                                                    } else {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                    }
                                                    if (firstNum > secondNum && firstNum < thirdNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: " + alertType[n] + "</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : " + setViewByName + "</h3>");
                                                        if (firstViewByName.equalsIgnoreCase("Grand Total")) {
                                                            message.append("<h3>  ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than the value of ").append(startValue[n]).append(" and less than the value of ").append(endValue[n]).append("</h3>");
                                                        } else {
                                                            message.append("<h3> ").append(allHeaderNameValue[0]).append(" : ").append(firstViewByName).append(", ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than the value of ").append(startValue[n]).append(" and less than the value of ").append(endValue[n]).append("</h3>");
                                                        }
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                }
                                            }//else
                                        }//
                                    }
//                               }
                                }//allV
                            }
                        }
                    }
                }//end of first for loop
            } else if (viewBy.isEmpty()) {
                for (int i = 0; i < kpiElementMesureName2.length; i++) {//first for loop
                    if (kpiElementMesureName2[i].equalsIgnoreCase(setViewByName)) {
                        for (int m = 0; m < allValuesContent.size(); m++) {
                            ArrayList<String> allValuesOnThisMesure = (ArrayList<String>) allValuesContent.get(m);
                            if (allValuesOnThisMesure.toString().contains(setViewByName)) {
                                for (int n = 0; n < selectedColumn.length; n++) {
                                    for (int j = 0; j < allValuesOnThisMesure.size(); j++) {//allV
                                        if (allHeaderNameValue[j].equalsIgnoreCase(selectedColumn[n])) {
//                                      for(int k=0;k<opratorValue.size();k++){
                                            if (allValuesOnThisMesure.get(j).toString().contains("--")) {
//                                       String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                StringBuilder message = new StringBuilder();
                                                message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                message.append("<h3> " + setViewByName + " at " + allHeaderNameValue[j] + " : " + allValuesOnThisMesure.get(j) + " which could not be compared with the value of " + startValue[n] + "</h3><br/>");
                                                message.append("</div>");
                                                if (isHeaderMsg == true) {
                                                    message.append(completeMessageHeader).append(message);
                                                }
                                                if (isFooterMsg == true) {
                                                    message.append(message).append(completeMessageFooter);
                                                }
                                                params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                try {
                                                    mailer = new PbMail(params);
                                                    mailer.sendMail();
                                                } catch (Exception e) {
                                                    logger.error("Exception:", e);
                                                }
                                            } else {
                                                String firstNumber = null;
                                                String secondNumber = null;
                                                Double firstNum = null;
                                                Double secondNum = null;
                                                if (allValuesOnThisMesure.get(j).toString().contains("-")) {
                                                    firstNumber = allValuesOnThisMesure.get(j).toString().replaceAll("[^\\d.]", "");
                                                    firstNumber = "-" + firstNumber;
                                                } else {
                                                    firstNumber = allValuesOnThisMesure.get(j).toString().replaceAll("[^\\d.]", "");
                                                }
                                                if (startValue[n].toString().contains("-")) {
                                                    secondNumber = startValue[n].toString().replaceAll("[^\\d.]", "");
                                                    secondNumber = "-" + secondNumber;
                                                } else {
                                                    secondNumber = startValue[n].toString().replaceAll("[^\\d.]", "");
                                                }
                                                if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr") && startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 100000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 1000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                } else if (allValuesOnThisMesure.get(j).toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    firstNum *= 10000000;
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                } else if (startValue[n].toString().toUpperCase().contains("K")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000;
                                                } else if (startValue[n].toString().toUpperCase().contains("L")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 100000;
                                                } else if (startValue[n].toString().toUpperCase().contains("M")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 1000000;
                                                } else if (startValue[n].toString().toUpperCase().contains("Cr")) {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                    secondNum *= 10000000;
                                                } else {
                                                    firstNum = Double.parseDouble(firstNumber.isEmpty() ? "0" : firstNumber);
                                                    secondNum = Double.parseDouble(secondNumber.isEmpty() ? "0" : secondNumber);
                                                }
                                                if (opratorValue[n].toString().equalsIgnoreCase(">")) {
                                                    if (firstNum > secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        message.append("<h3>  ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than the value of ").append(startValue[n]).append("</h3><br/>");
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("<")) {
                                                    if (firstNum < secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        message.append("<h3> " + setViewByName + " at " + allHeaderNameValue[j] + " : " + allValuesOnThisMesure.get(j) + " which is less than the value of " + startValue[n] + "</h3><br/>");
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase(">=")) {

                                                    if (firstNum >= secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        message.append("<h3> ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is greater than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("<=")) {
                                                    if (firstNum <= secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        message.append("<h3> ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is less than or equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("=")) {
                                                    if (firstNum == secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        message.append("<h3> ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("!=")) {
                                                    if (firstNum < secondNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        message.append("<h3> ").append(setViewByName).append(" at ").append(allHeaderNameValue[j]).append(" : ").append(allValuesOnThisMesure.get(j)).append(" which is not equal to the value of ").append(startValue[n]).append("</h3><br/>");
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                } else if (opratorValue[n].toString().equalsIgnoreCase("<>")) {
                                                    String thirdNumber = null;
                                                    Double thirdNum = null;
                                                    if (endValue[n].toString().contains("-")) {
                                                        thirdNumber = endValue[n].toString().replaceAll("[^\\d.]", "");
                                                        thirdNumber = "-" + thirdNumber;
                                                    } else {
                                                        thirdNumber = endValue[n].toString().replaceAll("[^\\d.]", "");
                                                    }

                                                    if (endValue[n].toString().toUpperCase().contains("K")) {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        thirdNum *= 1000;
                                                    } else if (endValue[n].toString().toUpperCase().contains("L")) {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        thirdNum *= 100000;
                                                    } else if (endValue[n].toString().toUpperCase().contains("M")) {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        thirdNum *= 1000000;
                                                    } else if (endValue[n].toString().toUpperCase().contains("Cr")) {
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                        thirdNum *= 10000000;
                                                    } else {
                                                        thirdNumber = endValue[n].toString().replaceAll("[^\\d.]", "");
                                                        thirdNum = Double.parseDouble(thirdNumber.isEmpty() ? "0" : thirdNumber);
                                                    }
                                                    if (firstNum > secondNum && firstNum < thirdNum) {
//                                           String toAddress = schedule.getReportmailIds();
//                                           params = new PbMailParams();
                                                        StringBuilder message = new StringBuilder();
                                                        message.append("<div style='background-color:#00688B;color:white'>").append("Dear Sir/Madam,").append("<center><label style='color:white;font-size:17px;'><u>KPI Alert Status for ").append(dashBoardName).append("</u></label></center>").append("<h2 style='color:red'>Alert Type: ").append(alertType[n]).append("</h2>").append("<h3>Column : ").append(allHeaderNameValue[j]).append("</h3>").append("<h3>Measure : ").append(setViewByName).append("</h3>");
                                                        message.append("<h3> " + setViewByName + " at " + allHeaderNameValue[j] + " : " + allValuesOnThisMesure.get(j) + " which is greater than the value of " + startValue[n] + " and less than the value of " + endValue[n] + "</h3>");
                                                        message.append("</div>");
                                                        if (isHeaderMsg == true) {
                                                            message.append(completeMessageHeader).append(message);
                                                        }
                                                        if (isFooterMsg == true) {
                                                            message.append(message).append(completeMessageFooter);
                                                        }
                                                        params.setBodyText(message.toString());
//                                           params.setToAddr(toAddress);
//                                           params.setSubject(schedule.getSchedulerName());
//                                           params.setHasAttach(false);
//                                           params.setIncludeURLMessage(false);
//                                           params.setIsHeaderLogoOn(isHeaderLogoOn);
//                                           params.setIsFooterLogoOn(isFooterLogoOn);
                                                        try {
                                                            mailer = new PbMail(params);
                                                            mailer.sendMail();
                                                        } catch (Exception e) {
                                                            logger.error("Exception:", e);
                                                        }
                                                    }
                                                }
                                            }//else
                                        }//
                                    }
//                               }
                                }//allV
                            }
                        }
                    }
                }//end of first for loop
            }
            //end of code By Dinanath

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }//END OF CODE BY DINANATH
    }
//Code added by Amar for automation of email
   public void generateEmailForReport(Container container) {                     //for exporting Reprot as EXCEL Format in Scheduler

        Label[] labels = null;
        //common code for all

        int columnCount;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        Number n = null;
        BigDecimal multiplier = new BigDecimal("100");
        int maxRows = 65500;//65500
        int countOfSheets = 1;
        Date date = new Date();
        int rowStart = 4;
        int rowStartTarget = 5;

        ProgenDataSet dataset = container.getRetObj();
        //dataset.resetViewSequence();
        ArrayList<String> cols = new ArrayList<String>();
        cols = container.getDisplayColumns();
        ArrayList<String> dTypes = new ArrayList<String>();
        dTypes = container.getDataTypes();
        ArrayList<String> rowView = new ArrayList<String>();

        String eleIdss = "";
        ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
        ArrayList<String> disCols = container.getDisplayLabels();
//int pos[]=new int[19];
//pos[0]=disCols.indexOf("Task Name");
//pos[1]=disCols.indexOf("Task Status");
//pos[2]=disCols.indexOf("Performer");
//pos[3]=disCols.indexOf("Barcode");
//pos[4]=disCols.indexOf("Client Name");
//pos[5]=disCols.indexOf("Client Full Name");
//pos[6]=disCols.indexOf("First Name");
//pos[7]=disCols.indexOf("Last Name");
//pos[8]=disCols.indexOf("Passport No");
//pos[9]=disCols.indexOf("Nationality");
//pos[10]=disCols.indexOf("Dateof Birth");
//pos[11]=disCols.indexOf("Email");
//pos[12]=disCols.indexOf("Status URL");
//pos[13]=disCols.indexOf("AST Email Address");
//pos[14]=disCols.indexOf("Barcode Id");
//pos[15]=disCols.indexOf("Client Unique id");
//pos[16]=disCols.indexOf("Client Ref No");
//pos[17]=disCols.indexOf("Task Start Date");
//pos[18]=disCols.indexOf("Case Start Date");

//         HashMap<String, String> crosstabMeasureId1 = ((PbReturnObject) container.getRetObj()).crosstabMeasureId;
//         if (container.isReportCrosstab() || crosstabMeasureId1!=null && !crosstabMeasureId1.isEmpty()) {
//                            for (Object hiddenCol : hiddenCols) {
//                 for(int i=0;i<cols.size();i++){
//                     eleIdss = crosstabMeasureId1.get(cols.get(i));
//                     if(eleIdss!=null && !eleIdss.equalsIgnoreCase("null") && !eleIdss.equalsIgnoreCase("")){
//                     if(hiddenCols.contains(eleIdss.replace("A_", ""))){
//                         cols.remove(i);
//                         disCols.remove(i);
//                         dTypes.remove(i);
//                     }}
//}
//             }
//         } else {
//          for (Object hiddenCol : hiddenCols) {
//                                int index = cols.indexOf("A_" + hiddenCol.toString());
//                                if (index != -1) {
//                                    cols.remove(index);
//                                    disCols.remove(index);
//                                    dTypes.remove(index);
//                                }
//           }}
//       for(int k=0;k<disCols.size();k++){
//           if(dTypes.size()<disCols.size()){
//               dTypes.add("N");
//           }
//       }
//       for(int k=0;k<disCols.size();k++){
//         if(dTypes.size()>disCols.size()){
//               dTypes.remove(dTypes.size()-1);
//           }
//       }
        ArrayList<String> paramNames = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        columnCount = disCols.size();
        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        String reportId = container.getReportCollect().reportId;
        String reportName = container.getReportCollect().reportName;

        try {
            ArrayList extraHeaderRows = new ArrayList();
            int counter = 2;
            int colCount = 0;
            int sheet = 0;
            int fromRow = 0;
            if (fromRow == 0 && colCount == 0) {
                colCount = dataset.getViewSequence().size();

            }
            ArrayList<Integer> viewsequence = dataset.getViewSequence();
            Label label1 = null;
            for (int k = 0; k <= sheet; k++) {
                rowStart = 4;
                counter = 2;
                String sheetName = "";

                int row = 0;

                String emailCol = "Email";
                String emailId = "";
                String Barcode = "";
                String applicant = "";
                ArrayList<String> values = new ArrayList();
                PbMailParams params = null;
                PbMail mailer = null;

//                  ArrayList<String> email = new ArrayList<String>();
//                       email.add("amar.pal@progenbusiness.com");
//                       email.add("mohit.jain@progenbusiness.com");
//                       email.add("monika.agrawal@progenbusiness.com");
//                       email.add("amit@progenbusiness.com");
//                       email.add("dinanath.parit@progenbusiness.com");
                //added By Dinanath for mail status
                List<String> Task_Name = new ArrayList<>();
                List<String> Task_Status = new ArrayList<>();
                List<String> Performer = new ArrayList<>();

                List<String> Barcode1 = new ArrayList<>();
                List<String> Client_Name = new ArrayList<>();
                List<String> Client_Full_Name = new ArrayList<>();
                List<String> First_Name = new ArrayList<>();
                List<String> Last_Name = new ArrayList<>();
                List<String> Passport_No = new ArrayList<>();
                List<String> Nationality = new ArrayList<>();
                List<String> DateofBirth = new ArrayList<>();
                List<String> Email = new ArrayList<>();
                List<String> Status_URL = new ArrayList<>();
                List<String> AST_Email_Address = new ArrayList<>();
                List<String> Clinet_Refno = new ArrayList<>();
                List<String> Task_Start_Date = new ArrayList<>();
                List<String> Case_Start_Date = new ArrayList<>();
                List<String> Barcode_id = new ArrayList<>();
                List<String> Client_Unique_id = new ArrayList<>();
                List<String> Report_Date = new ArrayList<>();
                List<String> Email_Sent_date = new ArrayList<>();
                List<String> Mail_Delivered_Status_Flag = new ArrayList<>();
                List<String> Mail_Failure_Success_Reason = new ArrayList<>();
                List<String> reportIdList = new ArrayList<>();
                List<String> reportNameList = new ArrayList<>();
                //Map<String, List<String>> map = new HashMap<>();

                int totalMailSent = viewsequence.size();
//for (int i=fromRow; i<5;  i++) {
                for (int i = fromRow; i < totalMailSent; i++) {

                    values.clear();
                    for (int j = 0; j < columnCount; j++) {
                        values.add(dataset.getFieldValueString(viewsequence.get(i), cols.get(j)));
                    }

                    try {
                        StringBuilder completeContent = new StringBuilder();
                        params = new PbMailParams();
                        boolean flag = false;
                        boolean vendorMsg=false;

                        String yesno = "No";
                        //By Ram 11May2016
                         if (reportName.equalsIgnoreCase("Pending Invoices: Vendor ")) {
                             DecimalFormat df = new DecimalFormat("###.##");
                             double pendingAmt = Double.parseDouble(values.get(4));
                            vendorMsg = true;
                            params.setSubject("Information on Pending Invoices");
                            completeContent.append("<html>\n" + "<body>\n" + " Dear  ").append(values.get(1)).append(",\n" + "<p>    We would like to inform you that your pending amount is Rs. "+df.format(pendingAmt) +". It will be processed as per due date. </p>").append("<br><br>\n" 
                                  //  + "In case of further queries, please visit our page <a href='http://www.dataflowgroup.com/contact-us'>http://www.dataflowgroup.com/contact-us</a>. You may enter your query in the space provided and we will reply to you in 48 hours.\n"
                                    + "<br><br>\n"
                                    + "Regards:<br>\n<br><br>"
                                    + "<img style='' src=\"cid:image\"><br>\n"
                                    + "<a href='http://www.progenbusiness.com/'>http://www.progenbusiness.com</a><br><br>"
                                    + "</p>\n"
                                    + "</body>\n"
                                    + "\n"
                                    + "</html>");
                        }else{
                        //added by Dinanath as on 05/08/2015
                        if (reportName.equalsIgnoreCase("Case WIP TAT equal 30 - MOMS")) {
                            yesno = "Yes";
                            params.setSubject("Automated Email - Delay in Case");
                            completeContent.append("<html>\n" + "<body>\n" + "  Dear ").append(values.get(1)).append(",\n" + "<p>We would like to inform you that your Primary Source Verification related to Singapore Workpass Application is currently under process and will be completed at the earliest :<br><br>\n" + "Applicant Name: ").append(values.get(1)).append(" ").append(values.get(2)).append("<br><br>\n" + "DataFlow Barcode: ").append(values.get(0)).append("<br><br>\n"
                                    + "In case of further queries, please visit our page <a href='http://www.dataflowgroup.com/contact-us'>http://www.dataflowgroup.com/contact-us</a>. You may enter your query in the space provided and we will reply to you in 48 hours.\n"
                                    + "<br><br>\n"
                                    + "Regards<br>\n"
                                    + "DataFlow Group \n"
                                    + "</p>\n"
                                    + "</body>\n"
                                    + "\n"
                                    + "</html>");
                        } else if (reportName.equalsIgnoreCase("Case WIP TAT equal 30 Insufficiency - MOMS")) {
                            yesno = "Yes";
                            params.setSubject("Automated Email - Insufficiency");
                            completeContent.append("<html>\n" + "<body>\n" + "  Dear ").append(values.get(1)).append(",\n" + "<p>We are contacting you with regard to the application submitted with us for Primary Source Verification regarding which additional document/information has been requested.<br><br>\n" + "Applicant Name : ").append(values.get(1)).append(" ").append(values.get(2)).append("<br><br>\n" + "DataFlow Barcode : ").append(values.get(0)).append("<br><br>\n"
                                    + "Accordingly, we have approached you for assistance via Email/Phone Call and requested the same.<br><br>\n"
                                    + "Please assist us in providing the required document/information at the earliest in order to expedite your application.\n"
                                    + "<br><br>\n"
                                    + "In case of further queries, please visit our page <a href='http://www.dataflowgroup.com/contact-us'>http://www.dataflowgroup.com/contact-us</a> . You may\n"
                                    + "enter your query in the space provided and we will reply to you in 48 hours.\n"
                                    + "<br><br>\n"
                                    + "Regards<br>\n"
                                    + "DataFlow Group \n"
                                    + "</p>\n"
                                    + "</body>\n"
                                    + "\n"
                                    + "</html>");
                        }
                        if (!yesno.equals("Yes")) {
                            if (reportName.equalsIgnoreCase("Case WIP TAT equal 30")) {
                                flag = true;
                                params.setSubject("Application Status");
                                completeContent.append("<html>\n" + "<body>\n" + "  Dear Sir/Madam,\n" + "<p>We would like to inform you that your Primary Source Verification related to SCH Qatar licensure is currently under process and will be completed at the earliest:<br><br>\n" + "Applicant Name - ").append(values.get(1)).append(" ").append(values.get(2)).append("<br><br>\n" + "DataFlow Barcode ").append(values.get(0)).append("<br><br>\n"
                                        + "To check your application status, please click here -  <br><a href=\"http://www.dataflowbpm.com/sbm/Applicant/MOH_Applicant.jsp\">http://www.dataflowbpm.com/sbm/Applicant/MOH_Applicant.jsp</a>\n"
                                        + "<br><br>\n"
                                        + "For any query please email us at schqatar@dataflowgroup.com  quoting above mentioned barcode.<br><br>\n"
                                        + "\n"
                                        + "Regards<br>\n"
                                        + "DataFlow Group \n"
                                        + "</p>\n"
                                        + "</body>\n"
                                        + "\n"
                                        + "</html>");
                            } else if (reportName.equalsIgnoreCase("Case WIP TAT equal 30 Insufficiency")) {
                                flag = true;
                                params.setSubject("Application Status");
                                String fileUrl = "http://www.dataflowbpm.com/sbm/Applicant/MOH_Applicant.jsp";
                                completeContent.append("<html>\n" + "<body>\n" + "  Dear Sir/Madam,\n" + "<p>We are contacting you with regard to the application submitted with us for Primary Source \n" + "Verification regarding which additional document/information has been requested.<br><br>\n" + "Applicant Name : ").append(values.get(1)).append(" ").append(values.get(2)).append("<br><br>\n" + "DataFlow Barcode : ").append(values.get(0)).append("<br><br>\n"
                                        + "Accordingly, we have approached you for assistance via Email/Phone Call and requested the same.<br><br>\n"
                                        + "Please assist us in providing the required document/information at the earliest in order to expedite your application.<br><br>\n"
                                        + "For any queries, please write to schqatar@dataflowgroup.com quoting the above mentioned DataFlow Barcode.<br><br>\n"
                                        + "Regards<br>\n"
                                        + "DataFlow Group \n"
                                        + "</p>\n"
                                        + "</body>\n"
                                        + "\n"
                                        + "</html>");
                            } else {
                                flag = false;
                                if (reportName.equalsIgnoreCase("Daily RTR Completed Cases List")) {//this changed and vrfdd
                                    params.setSubject("DataFlow Application Completed");
                                    //Added by Ram 22March2016 for different type of message according Report Status
                                    String clientName = values.get(4);
                                    String reportStatus = values.get(22);
                                    String dataflowURL = "http://www.dataflowgroup.com/faq.html";
                                    if (clientName.equalsIgnoreCase("HAAD") || clientName.equalsIgnoreCase("HAAD REN") || clientName.equalsIgnoreCase("GAHS") || clientName.equalsIgnoreCase("HAAD TANSEEQ")) {
                                        if (reportStatus.equalsIgnoreCase("Positive")) {
                                            completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Health Authority of Abu Dhabi (HAAD).<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "You may download a copy of your report  from the link <span style='font-family:Eurostile;font-size: 14px;'> ").append(values.get(12)).append("</span> by entering your Barcode and passport number.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                        if (reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                                   completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Health Authority of Abu Dhabi (HAAD).<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "To obtain a copy of your report, you can submit your request online by clicking on Contact Us on the link http://www.dataflowgroup.com/faq.html.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }

                                    }

                                    if (clientName.equalsIgnoreCase("SCFHS ONLINE") || clientName.equalsIgnoreCase("SCFHS OFFLINE")) {
                                        if (reportStatus.equalsIgnoreCase("Positive")) {
                            completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Saudi Commission for Health Specialities (SCFHS).<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "The report copy would be sent to you in another e-mail on your registered e-mail id.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                        if (reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                            completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Saudi Commission for Health Specialities (SCFHS).<br><br>" + "If you wish to obtain a copy, you must contact the SCFHS directly, quoting the following details in full:<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "<b>PLEASE NOTE</b>:DataFlow cannot provide a copy of your report.<br><br> " + "If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                    }
                                    if (clientName.equalsIgnoreCase("DHA")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                            completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Dubai Health Authority (DHA).<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "A copy of your report would be sent to you in another e-mail on your registered e-mail id.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }

                                    }
                                    if (clientName.equalsIgnoreCase("MOH UAE")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                                   completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Ministry of Health (UAE).<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "You may download a copy of your report  from the link <span style='font-family:Eurostile;font-size: 14px;'> ").append(values.get(12)).append("</span> by entering your Barcode and passport number.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }

                                    }

                                    if (clientName.equalsIgnoreCase("MOH")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify")) {
                                   completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the  Qatar Council for Health Practitioners (QCHP).<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "A copy of your report would be sent to you in another e-mail on your registered e-mail id.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + "You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                        if (reportStatus.equalsIgnoreCase("Negative")) {
                                     completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Qatar Council for Health Practitioners (QCHP).<br><br>" + "If you wish to obtain a copy, you must contact the QCHP directly, quoting the following details in full:<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "<b>PLEASE NOTE</b>: DataFlow cannot provide a copy of your report.<br><br>" + "If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                    }
                                    if (clientName.equalsIgnoreCase("SOCPA")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                                 completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Saudi Organization for Certified Public Accountants (SOCPA).<br><br>" + "If you wish to obtain a copy, you must contact the SOCPA directly, quoting the following details in full:<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "<b>PLEASE NOTE</b>: DataFlow cannot provide a copy of your report.<br></br>" + "If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                    }
                                    if (clientName.equalsIgnoreCase("OMSB")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                               completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Oman Medical Speciality Board (OMSB).<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "You would receive  another e-mail for the copy of your report.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                    }
                                    if (clientName.equalsIgnoreCase("CDA")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify")) {
                                   completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete.<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "To obtain a copy of your report, you can submit your request online by clicking on Contact Us on the link http://www.dataflowgroup.com/faq.html.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                        if (reportStatus.equalsIgnoreCase("Negative")) {
                                      completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with Community Development Authority (CDA).<br><br>" + "If you wish to obtain a copy, you must contact CDA directly, quoting the following details in full:<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "<b>PLEASE NOTE</b>: DataFlow cannot provide a copy of your report.<br><br>" + "If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                    }
                                    if (clientName.equalsIgnoreCase("MOHE KSA")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                                            completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with Ministry of Higher Education KSA (MOHE KSA).<br><br>" + "If you wish to obtain a copy, you must contact MOHE KSA directly, quoting the following details in full:<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "<b>PLEASE NOTE</b>:  DataFlow cannot provide a copy of your report." + "If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }

                                    }
                                    if (clientName.equalsIgnoreCase("DCAS")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                                            completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete and the report has been shared with the Dubai Corporation for Ambulance Services (DCAS).<br><br>" + "To obtain a copy of your report, you can submit your request online by clicking on Contact Us on the link http://www.dataflowgroup.com/faq.html.<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }
                                    }
                                    if (clientName.equalsIgnoreCase("MOMS PRIVATE") || clientName.equalsIgnoreCase("MOMS")) {
                                        if (reportStatus.equalsIgnoreCase("Positive") || reportStatus.equalsIgnoreCase("Unable to Verify") || reportStatus.equalsIgnoreCase("Negative")) {
                                            completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>The verification of your application is complete.<br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "To obtain a copy of your report, you can submit your request online by clicking on Contact Us on the link http://www.dataflowgroup.com/faq.html.<br><br>" + "<b>NOTE</b>: If you wish to submit your request for re-verification, you can contact us online at <span style='font-family:Eurostile;font-size: 14px;'> ").append(dataflowURL).append("</span>.<br>"
                                                    + " You need to provide any additional details and documents you might have, to help us conduct the re-verification. Our team will revert back shortly on your request."
                                                    + "Regards<br><br>"
                                                    + "<img src=\"cid:image\"><br>"
                                                    + "<a href='http://www.dataflowgroup.com'></a><br>"
                                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                                    + "</p></body></html>");
                                        }

                                    }
                                    //Endded By Ram

//                            completeContent.append(" <html>\n"
//                                    + "<body style='font-family:Eurostile;font-size: 14px;'>"
//                                    + "Dear " + values.get(6) + " " + values.get(7) + ",\n"
//                                    + "<p>We are pleased to inform you that the verification process of your application is completed.<br><br>"
//                                    + "<span style='color:#CC3333;'>Please check your status online at </span><span style='font-family:Eurostile;font-size: 14px;'> " + values.get(12)+"</span> .<br><br>"
//                                    + "<b>Applicant Name : </b>" + values.get(6) + " " + values.get(7) + "<br><br>"
//                                    + "<b>DataFlow Barcode: </b>" + values.get(3) + "<br><br>"
//                                    + "To obtain a copy of your report or in case of further queries, please visit our website <span style='font-family:Eurostile;font-size: 14px;'>" + values.get(13)+" </span>. You may enter your request in the space provided and we will reply to you in 48 hours.<br><br>"
//                                    + "Regards<br><br>"
//                                    + "<img src=\"cid:image\"><br><br>"
//                                    + "<a href='http://www.dataflowgroup.com'></a><br><br>"
//                                    + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
//                                    + "<b>Note</b>:It is the Applicant's responsibility to provide complete and accurate information to Dataflow. Failure to do so may delay the verification process."
//                                    + "In the event that additional information or documentation are required to complete the verification you will be contacted by email. Please reply in a timely manner to avoid delays in processing your application. Dataflow is not responsible for delays in verification due to incomplete or missing information."
//                                    + "</p></body></html>");
                                } else if (reportName.equalsIgnoreCase("Daily RTR Received Case List")) {//this changed vd
                                    params.setSubject("DataFlow Application Update");
                                    completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>Kindly note, your application is in the final report preparation stage. With exception to any clarification raised during this stage, we will complete and prepare the report at the earliest.<br><br>" + "The results will be shared with the respective regulating authority within next ten working days.<br><br>" + "<span style='color:#CC3333;'>Please check your status online at </span><span style='font-family:Eurostile;font-size: 14px;'> ").append(values.get(12)).append(" </span>.<br><br>" + "<b>Applicant Name :</b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode: </b>").append(values.get(3)).append("<br><br>" + "Incase of further queries, please visit our website <span style='font-family:Eurostile;font-size: 14px;'>").append(values.get(13)).append(" </span>. You may enter your query in the space provided and we will reply to you in 48 hour.<br><br>"
                                            + "Regards,<br><br>"
                                            + "<img src=\"cid:image\"><br><br>"
                                            + "<a href='http://www.dataflowgroup.com'></a><br><br>"
                                            + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                            + "<b>Note</b>:It is the Applicant's responsibility to provide complete and accurate information to Dataflow. Failure to do so may delay the verification process."
                                            + "In the event that additional information or documentation are required to complete the verification you will be contacted by email. Please reply in a timely manner to avoid delays in processing your application. Dataflow is not responsible for delays in verification due to incomplete or missing information."
                                            + "</p></body></html>");
                                } else if (reportName.equalsIgnoreCase("Daily Validation Completed List")) {
                                    String fdate = values.get(20);
                                    String dateStr = "";
                                    try {
                                        if (fdate != null && !fdate.isEmpty()) {
                                            String dyear = fdate.substring(0, 4);
                                            String month = fdate.substring(5, 7);
                                            String day = fdate.substring(8, 10);
                                            dateStr = day + "-" + month + "-" + dyear;
                                        }
                                    } catch (Exception e) {
                                    }
//                           Date utilDate2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(values.get(18).toString());
//                           java.sql.Date workingDate = new java.sql.Date(utilDate2.getTime());
                                    params.setSubject("DataFlow Application Initiated");//this has changed vd
                                    completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>We would like to inform that the verification of your submitted documents has been initiated. Your application is under process. The final report is expected to be ready by ").append(dateStr).append(".<br><br>" + "<span style='color:#CC3333;'>Please check your status online at </span><span style='font-family:Eurostile;font-size: 14px;'> ").append(values.get(12)).append(" </span> <br><br>" + "<b>Applicant Name : </b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode : </b>").append(values.get(3)).append("<br><br>" + "Incase of further queries, please visit our website <span style='font-family:Eurostile;font-size: 14px;'> ").append(values.get(13)).append(" </span>. You may enter your query in the space provided and we will reply to you in 48 hours.<br><br>"
                                            + "Regards,<br><br>"
                                            + "<img src=\"cid:image\"><br><br>"
                                            + "<a href='http://www.dataflowgroup.com'></a><br><br>"
                                            + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                            + "<b>Note</b>:It is the Applicant's responsibility to provide complete and accurate information to Dataflow. Failure to do so may delay the verification process."
                                            + "In the event that additional information or documentation are required to complete the verification you will be contacted by email. Please reply in a timely manner to avoid delays in processing your application. Dataflow is not responsible for delays in verification due to incomplete or missing information."
                                            + "</p></body></html>");
                                } else if (reportName.equalsIgnoreCase("Daily RTR Received Case List - DHA")) {
                                    params.setSubject("DataFlow Application Update");//this has changed vd
                                    completeContent.append(" <html>\n" + "<body style='font-family:Eurostile;font-size: 14px;'>" + "Dear ").append(values.get(6)).append(" ").append(values.get(7)).append(",\n" + "<p>Kindly note, your application is in the final report preparation stage. With exception to any clarification raised during this stage, we will complete and prepare the report at the earliest.<br><br>" + "The results will be shared with the respective regulating authority within next ten working days.<br><br>" + "<span style='color:#CC3333;'>Please check your status online at </span> <span style='font-family:Eurostile;font-size: 14px;'> ").append(values.get(12)).append(" </span>.<br><br>" + "<b>Applicant Name :</b>").append(values.get(6)).append(" ").append(values.get(7)).append("<br><br>" + "<b>DataFlow Barcode : </b>").append(values.get(3)).append("<br><br>" + "On completion we will share the Primary Source Verification Report with you.<br><br>" + "Incase of further queries, please visit our website <span style='font-family:Eurostile;font-size: 14px;'> ").append(values.get(13)).append(" </span>. You may enter your query in the space provided and we will reply to you in 48 hours.<br><br>"
                                            + "Regards,<br><br>"
                                            + "<img src=\"cid:image\"><br><br>"
                                            + "<a href='http://www.dataflowgroup.com'></a><br><br>"
                                            + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                            + "<b>Note</b>:It is the Applicant's responsibility to provide complete and accurate information to Dataflow. Failure to do so may delay the verification process."
                                            + "In the event that additional information or documentation are required to complete the verification you will be contacted by email. Please reply in a timely manner to avoid delays in processing your application. Dataflow is not responsible for delays in verification due to incomplete or missing information."
                                            + "</p></body></html>");
                                } else if (reportName.equalsIgnoreCase("Daily Validation Completed List - DHA")) {
                                    String fdate = values.get(20).toString();
                                    String dateStr = "";
                                    try {
                                        if (fdate != null && !fdate.isEmpty()) {
                                            String dyear = fdate.substring(0, 4);
                                            String month = fdate.substring(5, 7);
                                            String day = fdate.substring(8, 10);
                                            dateStr = day + "-" + month + "-" + dyear;
                                        }
                                    } catch (Exception e) {

                                    }
                                    params.setSubject("DataFlow Application Initiated");//this has changed v
                                    completeContent.append(" <html>\n"
                                            + "<body style='font-family:Eurostile;font-size: 14px;'>"
                                            + "Dear " + values.get(6) + " " + values.get(7) + ",\n"
                                            + "<p>We would like to inform that the verification of your submitted documents has been initiated. Your application is under process. The final report is expected to be ready by " + dateStr + ".<br><br>"
                                            + "<span style='color:#CC3333;'>Please check your status online at </span><span style='font-family:Eurostile'> " + values.get(12) + " </span> <br><br>"
                                            + "<b>Applicant Name: </b>" + values.get(6) + " " + values.get(7) + "<br><br>"
                                            + "<b>DataFlow Barcode: </b>" + values.get(3) + "<br><br>"
                                            + "Incase of further queries, please visit our website <span style='font-family:Eurostile;font-size: 14px;'> " + values.get(13) + " </span>. You may enter your query in the space provided and we will reply to you in 48 hours.<br><br>"
                                            + "Regards,<br><br>"
                                            + "<img src=\"cid:image\"><br><br>"
                                            + "<a href='http://www.dataflowgroup.com'></a><br><br>"
                                            + "<b>PLEASE DO NOT REPLY TO THIS MESSAGE</b><br><br>"
                                            + "<b>Note</b>:It is the Applicant's responsibility to provide complete and accurate information to Dataflow. Failure to do so may delay the verification process."
                                            + "In the event that additional information or documentation are required to complete the verification you will be contacted by email. Please reply in a timely manner to avoid delays in processing your application. Dataflow is not responsible for delays in verification due to incomplete or missing information."
                                            + "</p></body></html>");
                                }
                            }
                        }
                         }
                        params.setBodyText(completeContent.toString());
                        params.setHasAttach(false);
                        params.setAutoMail(true);
                        boolean status = false;
                        try {
                            if (yesno.equalsIgnoreCase("Yes")) {
                                params.setToAddr(values.get(3));
                            } else if (flag) {
                                params.setToAddr(values.get(3));
//                            params.setToAddr("dinanath.parit@progenbusiness.com");
                            } else if (vendorMsg) {
                            params.setToAddr(values.get(0));
//                            params.setToAddr("ram.newas@progenbusiness.com");
                            }else {
                                params.setToAddr(values.get(11));

//                            if((values.get(11)).equalsIgnoreCase("NA")){
//                                params.setToAddr(values.get(11));
//                            }else{
//                            params.setToAddr("ram.newas@progenbusiness.com");
//                            }
                            }
                        } catch (Exception ep) {
                            ep.printStackTrace();
                        }
                        mailer = new PbMail(params);
                        try {
                            if (yesno.equalsIgnoreCase("Yes")) {
                                status = mailer.sendMailToUserFromMOMS();//commented now
                            } else if (flag) {
                                status = mailer.sendMailToUser();//commented now
                            } else if (vendorMsg) {
                                status = mailer.sendMailToVendor();//commented now
                            } else {
                                status = mailer.sendMailToUserWithLogs(values, totalMailSent, i, Task_Name, Task_Status, Performer, Barcode1, Client_Name, Client_Full_Name, First_Name, Last_Name, Passport_No, Nationality, DateofBirth, Email, Status_URL, AST_Email_Address, Clinet_Refno, Task_Start_Date, Case_Start_Date, Barcode_id, Client_Unique_id, Report_Date, Report_Date, Email_Sent_date, Mail_Delivered_Status_Flag, Mail_Failure_Success_Reason, reportIdList, reportNameList, reportId, reportName);
                            }
                        } catch (Exception em) {
                            em.printStackTrace();
                        }
                        if (status) {
                            ReportTemplateDAO dao = new ReportTemplateDAO();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    row++;
                }
                colCount = colCount + 65000;
                fromRow = fromRow + 65000;
            }

        } catch (Exception exp) {
            //code Added By Amar
            if (container.getReportCollect().getLogReadWriterObject() != null) {
                StringWriter str = new StringWriter();
                PrintWriter writer = new PrintWriter(str);
                exp.printStackTrace(writer);
                container.getReportCollect().setLogBoolean(true);
                try {
                    container.getReportCollect().getLogReadWriterObject().fileWriter(str.getBuffer().toString());
                } catch (IOException eo) {
                }
            }//end of code
            exp.printStackTrace();
        }
    }
//end of code

    //added by sruthi to display overallavg,overallmin,overallmax
    public BigDecimal getOthersData(BigDecimal othersValue, int rowStart, int row, int i, WritableCellFormat cf2, int col, Container container, WritableCell cell, WritableCellFormat writableCellFormat, WritableSheet writableSheet) throws WriteException {
        String nbrSymbol = "";
        String formattedData = "";
        String element = container.getDisplayColumns().get(col);
        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
        int precision = container.getRoundPrecisionForMeasure(element);
        //   formattedData = NumberFormatter.getModifiedNumber(othersValue, nbrSymbol, precision);
        if (othersValue != null) {
            cell = new Number(col, row + rowStart + 1, Double.parseDouble(String.valueOf(othersValue)), cf2);
        } else {
            cell = new Number(col, row + rowStart + 1, Double.parseDouble("0"), cf2);
        }
        cell.setCellFormat(writableCellFormat);
        writableSheet.addCell(cell);
        return null;
    }
//ended by sruthi
    //added by sruthi to display overallavg,overallmin,overallmax

    public BigDecimal getOthersDataX(BigDecimal othersValue, int rowStart, int row, int i, XSSFRow hRow, int col, Container container) throws WriteException {
        String nbrSymbol = "";
        XSSFCell hCell = null;
        String formattedData = "";
        String element = container.getDisplayColumns().get(col);
        String snbrSymbol = container.symbol.get(container.getDisplayColumns().get(col));
        int precision = container.getRoundPrecisionForMeasure(element);
        //   formattedData = NumberFormatter.getModifiedNumber(othersValue, nbrSymbol, precision);
        if (othersValue != null) {
            hCell = hRow.createCell(col);
            hCell.setCellValue(Double.parseDouble(String.valueOf(othersValue)));
        } else {
            hCell = hRow.createCell(col);
            hCell.setCellValue(Double.parseDouble(String.valueOf("0")));
            //cell.setCellFormat(writableCellFormat);
            //  writableSheet.addCell(cell);
        }
        return null;
    }
//ended by sruthi
}
