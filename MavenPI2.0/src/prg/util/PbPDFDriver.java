/*
 * PbPDFDriver.java
 *
 * Created on June 29, 2009, 10:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package prg.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.progen.db.ProgenDataSet;
import com.progen.report.PbReportCollection;
import com.progen.report.ReportParameter;
import com.progen.report.colorgroup.ColorGroup;
import com.progen.report.data.DataFacade;
import com.progen.report.data.RowViewTableBuilder;
import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.PdfTableBodyDisplay;
import com.progen.report.display.PdfTableSubTotalDisplay;
import com.progen.report.display.table.TableDisplay;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import java.awt.Color;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.ContainerConstants;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class PbPDFDriver {

    public static Logger logger = Logger.getLogger(PbPDFDriver.class);
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
    String DATE_FORMAT1 = "dd/MM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT1);
    public Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
    Font fontHead = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
    //Document document = new Document(PageSize.A4);
    Document document = null;
    ByteArrayOutputStream bos = null;
    PdfWriter writer = null;
    private String[] filePaths = null;
    private HttpServletRequest request = null;
    private String logoPath = null;
    private String paramType = null;
    private String pdfTypeSelect = null;
    private String headerTitle = "";
    private ArrayList timeDetailsArray;
    private ArrayList filterValues;
    BigDecimal multiplier = new BigDecimal("100");
    private HashMap ColorCodeMap = null;
    private int fromRow;
    private int toRow;
    private float cellHeight;
    Container container = null;
    private ColorGroup colorGroup;
    private ReportParameter repParameter;

    /**
     * Creates a new instance of PbPDFDriver
     */
    public PbPDFDriver() {
    }

    public void setContainer(Container container) {
        this.container = container;

    }

    public void createPDF(PbReturnObject ret, String fileName, HttpServletResponse response, String[] types) {
        this.createPDF();
    }

    public void createPDF() {
        if (displayColumns.length <= 5) {
            //document = new Document(PageSize.A4, 50, 50, 50, 50);
        } else if (displayColumns.length > 5 && displayColumns.length <= 15) {
            //document = new Document(PageSize.B2, 50, 50, 50, 50);
        } else {
            //document = new Document(PageSize.B0, 50, 50, 50, 50);
        }
        if (this.getPdfTypeSelect() != null && this.getPdfTypeSelect().equalsIgnoreCase("A4")) {
            document = new Document(PageSize.A4, 50, 50, 50, 50);
        } else if (this.getPdfTypeSelect() != null && this.getPdfTypeSelect().equalsIgnoreCase("A4_Landascpe")) {
            document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
        } else if (this.getPdfTypeSelect() != null && this.getPdfTypeSelect().equalsIgnoreCase("B3")) {
            document = new Document(PageSize.B3, 50, 50, 50, 50);
        } else if (this.getPdfTypeSelect() != null && this.getPdfTypeSelect().equalsIgnoreCase("B3_Landascpe")) {
            document = new Document(PageSize.B3.rotate(), 50, 50, 50, 50);
        } else {
            document = new Document(PageSize.B2, 50, 50, 50, 50);
        }
        try {
            bos = new ByteArrayOutputStream();
            writer = PdfWriter.getInstance(document, bos);
            document.open();

            //F:\QD-26-10-2009\QueryDesigner\build\web\images\pi_logo.gif
            HeaderFooter header = new HeaderFooter(new Phrase(getHeaderTitle()), true);
            HeaderFooter footer = new HeaderFooter(new Phrase(getHeaderTitle()), new Phrase("............"));


            header.setLeft(document.getPageSize().getWidth() - 100.0f);
            footer.setLeft(document.getPageSize().getWidth() - 100.0f);
            document.setHeader(header);
            document.setFooter(footer);

            //File file = new File(getLogoPath());
            //FileInputStream fis = new FileInputStream(file);
            //byte[] bytes = new byte[fis.available()];
            //while (-1 != fis.read(bytes)) {
            //}
            //Image image = Image.getInstance(bytes);
            //image.setUrl(new URL("http://www.progenbusiness.com"));

            PbReportCollection collect = container.getReportCollect();
            ArrayList<String> sortCols = null;
            char[] sortTypes = null;//ArrayList sortTypes = null;
            char[] sortDataTypes = null;
            //Start of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
            Date FromDate = null;
            Date ToDate = null;
            Date CmpFromDate = null;
            Date CmpToDate = null;
            Date CreatedDate = null;
//            FromDate = sdf.parse(timeDetailsArray.get(2).toString());
//            ToDate = sdf.parse(timeDetailsArray.get(3).toString());
//            CmpFromDate = sdf.parse(timeDetailsArray.get(4).toString());
//            CmpToDate = sdf.parse(timeDetailsArray.get(5).toString());
//            CreatedDate = sdf.parse(timeDetailsArray.get(6).toString());
            //end of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY

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
            //document.add(image);
            if (this.getParamType() == null || this.getParamType().equalsIgnoreCase("withParameters")) {
                document.add(new Paragraph(getHeaderTitle()));
                document.add(new Paragraph("Report Title : " + getReportName().replace("_", " ")));
                document.add(new Paragraph("Created on  :" + sdf.format(date)));
            } else {
                document.add(new Paragraph("Report Title : " + getReportName().replace("_", " ")));
                document.add(new Paragraph("Created on  :" + sdf.format(date)));
            }
            //document.add(new Paragraph(""));

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
//                     extraHeaderRows.add("Todate : " + timeDetailsArray.get(3));
//                     extraHeaderRows.add("CompareFromDate : " + timeDetailsArray.get(4));
//                     extraHeaderRows.add("CompareTodate : " + timeDetailsArray.get(5));
                        FromDate = sdf.parse(timeDetailsArray.get(2).toString());
                        ToDate = sdf.parse(timeDetailsArray.get(3).toString());
                        CmpFromDate = sdf.parse(timeDetailsArray.get(4).toString());
                        CmpToDate = sdf.parse(timeDetailsArray.get(5).toString());
                        extraHeaderRows.add("FromDate : " + sdf1.format(FromDate));
                        extraHeaderRows.add("Todate : " + sdf1.format(ToDate));
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
                for (int i = 0; i < extraHeaderRows.size(); i++) {
                    document.add(new Paragraph("" + extraHeaderRows.get(i)));
                }
            }


            if (displayType.equalsIgnoreCase("Report")) {
                document = buildTable();
            } else if (displayType.equalsIgnoreCase("Graph")) {
                document = buildGraph();
            } else if (displayType.equalsIgnoreCase("ReportAndGraph")) {
                document = buildGraph();
                document = buildTable();
            } else {
                document = buildTable();
            }

            document.close();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(bos.size());

            ServletOutputStream outputstream = response.getOutputStream();
            bos.writeTo(outputstream);
            outputstream.flush();

        } catch (DocumentException de) {
            logger.error("Exception: ", de);
        } catch (IOException ioe) {
            logger.error("Exception: ", ioe);
        } catch (Exception e) {
            logger.error("Exception: ", e);
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

    public Document buildTable() {
        PdfPTable table = null;
        PdfPCell cell = null;
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);
        //added by sruthi for hidden measurs
        ArrayList<String> cols = new ArrayList<String>();
        ArrayList<String> dTypes = new ArrayList<String>();
        dTypes = container.getDataTypes();
        cols = container.getDisplayColumns();
        //ended by sruthi
        try {
            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            int columnCount = displayColumns.length;
            float colWidth = width / columnCount;
            float[] columnDefinitionSize = new float[columnCount];
            //added by sruthi for hidden measurs
            ArrayList<String> hiddenCols = container.getReportCollect().getHideMeasures();
            ArrayList<String> disCols = container.getDisplayLabels();
            for (Object hiddenCol : hiddenCols) {
                int index = cols.indexOf("A_" + hiddenCol.toString());
                if (index != -1) {
                    cols.remove(index);
                    disCols.remove(index);
                    dTypes.remove(index);
                    columnCount = disCols.size();
                }
            }
            displayLabels = new String[dTypes.size()];
            for (int i = 0; i < dTypes.size(); i++) {
                displayLabels[i] = String.valueOf(disCols.get(i));
            }
            //ended by sruthi
            for (int i = 0; i < columnCount; i++) {
                //columnDefinitionSize[i] = colWidth;
                columnDefinitionSize[i] = colWidth;
            }
            table = new PdfPTable(columnDefinitionSize);
            table.setSpacingBefore(10.0f);
            table.getDefaultCell().setBorder(1);
            if (this.getPdfTypeSelect() != null && this.getPdfTypeSelect().equalsIgnoreCase("A4")) {
                table.setTotalWidth(493);
                table.setLockedWidth(true);
            } else if (this.getPdfTypeSelect() != null && this.getPdfTypeSelect().equalsIgnoreCase("A4_Landascpe")) {
                table.setTotalWidth(745);
                table.setLockedWidth(true);
            } else if (this.getPdfTypeSelect() != null && this.getPdfTypeSelect().equalsIgnoreCase("B3")) {
                table.setTotalWidth(897);
                table.setLockedWidth(true);
            } else if (this.getPdfTypeSelect() != null && this.getPdfTypeSelect().equalsIgnoreCase("B3_Landascpe")) {
                table.setTotalWidth(1320);
                table.setLockedWidth(true);
            } else {
                table.setTotalWidth(colWidth * displayColumns.length);
                table.setLockedWidth(false);
            }
            table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);

            DataFacade facade = new DataFacade(container);
            TableBuilder tableBldr = new RowViewTableBuilder(facade);
            if (fromRow == 0 && toRow == 0) {
                tableBldr.setFromAndToRow(0, container.getRetObj().getViewSequence().size());
            } else {
                tableBldr.setFromAndToRow(fromRow, toRow);
            }
            if (container.isReportCrosstab()) {
                TableHeaderRow[] headerRows;
                headerRows = tableBldr.getHeaderRowData();
                int colCount = tableBldr.getColumnCount();
                int rowSpan;
                int colSpan;
                String heading;
                String compare = "";
                for (TableHeaderRow headerRow : headerRows) {
                    for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                        heading = headerRow.getRowData(i);

                        if (headerRow.getLayer() == 0 && !heading.equalsIgnoreCase(compare)) {
                            colSpan = headerRow.getColumnSpan(i);
                            cell = new PdfPCell(new Phrase(heading, fontHead));
                            cell.setColspan(colSpan);
                            cell.setNoWrap(true);
                            cell.setBackgroundColor(Color.lightGray);
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                            cell.setNoWrap(false);
                            table.addCell(cell);
                            compare = heading;
                        } else if (headerRow.getLayer() == 1) {
                            colSpan = headerRow.getColumnSpan(i);
                            cell = new PdfPCell(new Phrase(heading, fontHead));
                            cell.setColspan(colSpan);
                            cell.setNoWrap(true);
                            cell.setBackgroundColor(Color.lightGray);
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                            cell.setNoWrap(false);
                            table.addCell(cell);
                            compare = heading;
                        }
                    }
                }
            } else {
                for (int i = 0; i < columnCount; i++) {
                    cell = new PdfPCell(new Phrase(displayLabels[i], fontHead));
                    cell.setBackgroundColor(Color.lightGray);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setNoWrap(false);
                    table.addCell(cell);
                }
            }
            // added by krishan 
            table.setHeaderRows(1);
            TableDisplay bodyHelper = null;
            TableDisplay subTotalHelper = null;
            bodyHelper = new PdfTableBodyDisplay(tableBldr);
            bodyHelper.setHeadlineflag("Export");
            bodyHelper.setPdfTable(table);
            bodyHelper.setCellHeight(cellHeight);
            subTotalHelper = new PdfTableSubTotalDisplay(tableBldr);
            subTotalHelper.setPdfTable(table);
            subTotalHelper.setCellHeight(cellHeight);
            bodyHelper.setNext(subTotalHelper);
            StringBuilder tableHtml = new StringBuilder();
            tableHtml.append(bodyHelper.generateOutputHTML());

            /*
             * PbReportTableBD reportTableBD = new PbReportTableBD();
             * reportTableBD.searchDataSet(this.container); ArrayList<String>
             * sortCols = null; char[] sortTypes = null;//ArrayList sortTypes =
             * null; char[] sortDataTypes = null; ArrayList<Integer>
             * sortSequence =null; if(sortTypes!=null&&sortDataTypes!=null){
             * sortSequence =ret.sortDataSet(sortCols, sortTypes,
             * sortDataTypes); } else{ sortSequence = ret.getViewSequence(); }
             *
             * for (int i = fromRow; i < toRow; i++) { for (int j = 0; j <
             * columnCount; j++) { String colName = displayColumns[j]; String
             * discolName = displayLabels[j]; Color BGColor = null; int
             * PerCentColumnIndex = colName.lastIndexOf("_percentwise"); if
             * ("D".equals(types[j])) { cell = new PdfPCell(new
             * Phrase(ret.getFieldValueDateString(sortSequence.get(i), colName),
             * font)); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
             * if(this.getCellHeight()!=0.0f){
             * cell.setFixedHeight(this.getCellHeight()); } } else if
             * ("N".equals(types[j])) { String formattedData = ""; if
             * (ret.getFieldValueBigDecimal(sortSequence.get(i), colName) !=
             * null) { String element = container.getDisplayColumns().get(j);
             * String nbrSymbol=""; HashMap NFMap=new HashMap(); NFMap =
             * (HashMap)container.getTableHashMap().get("NFMap");
             * HashMap<String,String>
             * crosstabMeasureId=((PbReturnObject)container.getRetObj()).crosstabMeasureId;
             * if(NFMap != null){ if(container.isReportCrosstab() &&
             * crosstabMeasureId!=null && !crosstabMeasureId.isEmpty()){
             * if(NFMap.get(crosstabMeasureId.get(element))!=null){ nbrSymbol=
             * String.valueOf(NFMap.get(crosstabMeasureId.get(element))); } }
             * else if(NFMap.get(element)!=null) nbrSymbol=
             * String.valueOf(NFMap.get(element)); } int precision =
             * container.getRoundPrecisionForMeasure(element); String snbrSymbol
             * = container.symbol.get(element); if(container.isReportCrosstab()
             * && crosstabMeasureId!=null && !crosstabMeasureId.isEmpty()){
             * precision =
             * container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
             * } if(nbrSymbol!=null && nbrSymbol.equalsIgnoreCase("nf") ){
             * formattedData=ret.getFieldValueBigDecimal(sortSequence.get(i),
             * colName).toString(); } else
             * if(container.gettimeConversion(colName)!=null &&
             * container.gettimeConversion(colName).equalsIgnoreCase("Y")){
             * formattedData = this.convertDataToTime(new
             * BigDecimal(ret.getFieldValueBigDecimal(sortSequence.get(i),
             * colName).toString())); }else if(snbrSymbol!=null &&
             * !snbrSymbol.equalsIgnoreCase("")){ formattedData =
             * NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i),
             * colName), nbrSymbol, precision); formattedData =
             * snbrSymbol+""+formattedData; } else { formattedData =
             * NumberFormatter.getModifiedNumber(ret.getFieldValueBigDecimal(sortSequence.get(i),
             * colName), nbrSymbol, precision); HashMap colProps =
             * container.getColumnProperties(); if (colProps != null &&
             * colProps.containsKey(element)) { ArrayList propList = (ArrayList)
             * colProps.get(element); String colSymbol = (String)
             * propList.get(7); if (!("N".equalsIgnoreCase(colSymbol) ||
             * "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) { if
             * (colSymbol.equalsIgnoreCase("%")) { formattedData = formattedData
             * + colSymbol; } else { formattedData = colSymbol + formattedData;
             * } } } } } if (PerCentColumnIndex == -1) { cell = new PdfPCell(new
             * Phrase(formattedData, font)); } else { colName =
             * colName.replace("_percentwise", ""); cell = new PdfPCell(new
             * Phrase(nFormat.format(ret.getFieldValueBigDecimal(sortSequence.get(i),
             * colName).divide(ret.getColumnGrandTotalValue(colName),
             * MathContext.DECIMAL64).multiply(multiplier)), font)); }
             * cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
             * if(this.getCellHeight()!=0.0f){
             * cell.setFixedHeight(this.getCellHeight()); }
             *
             * BGColor = getColor(colName,
             * ret.getFieldValueString(sortSequence.get(i), colName)); if
             * (BGColor != null) { cell.setBackgroundColor(BGColor); } } else {
             * cell = new PdfPCell(new
             * Phrase(ret.getFieldValueString(sortSequence.get(i), colName),
             * font)); cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
             * if(this.getCellHeight()!=0.0f){
             * cell.setFixedHeight(this.getCellHeight()); } }
             *
             * cell.setNoWrap(false); table.addCell(cell);
             *
             * colName = null; } } ArrayList displayValues = new ArrayList();
             * displayValues = container.getDisplayColumns();
             * if(sortSequence.size()==toRow){ for (int i = 0; i < 2; i++) { for
             * (int col = 0; col < displayValues.size(); col++) {
             * if(types[col].toString().equalsIgnoreCase("N")){ String
             * formattedData = ""; String element =
             * container.getDisplayColumns().get(col); String nbrSymbol="";
             * HashMap NFMap=new HashMap(); NFMap =
             * (HashMap)container.getTableHashMap().get("NFMap");
             * HashMap<String,String>
             * crosstabMeasureId=((PbReturnObject)container.getRetObj()).crosstabMeasureId;
             * if(NFMap != null){ if(container.isReportCrosstab() &&
             * crosstabMeasureId!=null && !crosstabMeasureId.isEmpty()){
             * if(NFMap.get(crosstabMeasureId.get(element))!=null){ nbrSymbol=
             * String.valueOf(NFMap.get(crosstabMeasureId.get(element))); } }
             * else if(NFMap.get(element)!=null) nbrSymbol=
             * String.valueOf(NFMap.get(element)); } int precision =
             * container.getRoundPrecisionForMeasure(element); String snbrSymbol
             * = container.symbol.get(element); if(container.isReportCrosstab()
             * && crosstabMeasureId!=null && !crosstabMeasureId.isEmpty()){
             * precision =
             * container.getRoundPrecisionForMeasure(crosstabMeasureId.get(element));
             * } if(nbrSymbol!=null && nbrSymbol.equalsIgnoreCase("nf") ){
             * formattedData=ret.getColumnGrandTotalValue(displayValues.get(col).toString()).toString();
             * }else if(snbrSymbol!=null && !snbrSymbol.equalsIgnoreCase("")){
             * formattedData =
             * NumberFormatter.getModifiedNumber(ret.getColumnGrandTotalValue(displayValues.get(col).toString()),
             * nbrSymbol, precision); formattedData =
             * snbrSymbol+""+formattedData; } else { formattedData =
             * NumberFormatter.getModifiedNumber(ret.getColumnGrandTotalValue(displayValues.get(col).toString()),
             * nbrSymbol, precision); HashMap colProps =
             * container.getColumnProperties(); if (colProps != null &&
             * colProps.containsKey(element)) { ArrayList propList = (ArrayList)
             * colProps.get(element); String colSymbol = (String)
             * propList.get(7); if (!("N".equalsIgnoreCase(colSymbol) ||
             * "".equals(colSymbol) || "Y".equalsIgnoreCase(colSymbol))) { if
             * (colSymbol.equalsIgnoreCase("%")) { formattedData = formattedData
             * + colSymbol; } else { formattedData = colSymbol + formattedData;
             * } } } } cell = new PdfPCell(new Phrase(formattedData, font));
             * cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
             * if(this.getCellHeight()!=0.0f){
             * cell.setFixedHeight(this.getCellHeight()); } }else{ if(i==0){
             * cell = new PdfPCell(new Phrase("Grand Total", font));
             * cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
             * if(this.getCellHeight()!=0.0f){
             * cell.setFixedHeight(this.getCellHeight()); } } else{ cell = new
             * PdfPCell(new Phrase("Category Sub Total", font));
             * cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
             * if(this.getCellHeight()!=0.0f){
             * cell.setFixedHeight(this.getCellHeight()); } } }
             * cell.setNoWrap(false); table.addCell(cell); } } }
             */
            document.add(table);
        }
        catch (DocumentException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return document;
    }

    public Document buildGraph() {
        Image image = null;
        File file = null;
        FileInputStream fis = null;
        byte[] bytes = null;
        PdfPTable table = null;
        PdfPCell cell = null;

        /*
         * URL url = null; URLConnection urlConnection = null; DataInputStream
         * dis = null; String strURL = null;
         */
        try {

            table = new PdfPTable(getFilePaths().length);
            table.setSpacingBefore(10.0f);

            table.setTotalWidth(1000.0f * getFilePaths().length);
            table.setLockedWidth(false);
            table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
            for (int fileCount = 0; fileCount < getFilePaths().length; fileCount++) {
                int index = getFilePaths()[fileCount].indexOf("jfreechart");
                file = new File(System.getProperty("java.io.tmpdir") + "/" + getFilePaths()[fileCount].substring(index));
                fis = new FileInputStream(file);
                bytes = new byte[fis.available()];
                while (-1 != fis.read(bytes)) {
                }
                image = Image.getInstance(bytes);
                if (image != null) {
                    //document.add(image);
                    cell = new PdfPCell(image);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setNoWrap(false);
                    cell.setBorder(0);
                    table.addCell(cell);
                }
                /*
                 * file = new File(getFilePaths()[fileCount]); strURL =
                 * "http://" + request.getServerName() + ":" +
                 * request.getServerPort() + request.getContextPath() + "/" +
                 * getFilePaths()[fileCount]; url = new URL(strURL); image =
                 * Image.getInstance(url); dis = new
                 * DataInputStream(url.openStream()); bytes = new
                 * byte[url.openStream().available()]; while (-1 !=
                 * url.openStream().read(bytes)) { }
                 *
                 */
            }
            document.add(table);

        } catch (IOException | DocumentException exp) {
            logger.error("Exception: ", exp);
        }catch (Exception exp) {
            logger.error("Exception: ", exp);
        }

        return document;
    }

    public static void main(String[] a) {
        /*
         * String fileName="
         * http://localhost:8082/QueryDesigner/servlet/DisplayChart?filename=jfreechart-6848526738329517197.png";
         * int index=fileName.lastIndexOf("jfreechart");
         * fileName=fileName.substring(index);
         * //////////////////////////////////////////////////////////////////////////////////////////.println.println("fileName
         * is "+fileName);
         */
        //StringBuffer fileName = new StringBuffer(" http://localhost:8082/QueryDesigner/servlet/DisplayChart?filename=jfreechart-6848526738329517197.png");
        //int index = fileName.indexOf("jfreechart");
        //fileName.substring(index);
        //////////////////////////////////////////////////////////////////////////////////////////.println.println("File Name is " + fileName.substring(index));
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

//    public Color getColor(String disColumnName, String currentValue) {
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
//            return Color.RED;
//        } else if (bgColor.equalsIgnoreCase("Orange")) {
//            return Color.ORANGE;
//        } else if (bgColor.equalsIgnoreCase("Green")) {
//            return Color.GREEN;
//        } else {
//            return null;
//        }
//    }
    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getPdfTypeSelect() {
        return pdfTypeSelect;
    }

    public void setPdfTypeSelect(String pdfTypeSelect) {
        this.pdfTypeSelect = pdfTypeSelect;
    }

    public float getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(float cellHeight) {
        this.cellHeight = cellHeight;
    }

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

    public Color getColor(String columnName, String endvalu) {
        BigDecimal maxVal = ret.getColumnMaximumValue(columnName);
        BigDecimal minVal = ret.getColumnMinimumValue(columnName);
        BigDecimal diff = maxVal.subtract(minVal);
        Color aColor = null;
        int originalMeasIndex;
        String measure = "";

        PbReportViewerBD repBD = new PbReportViewerBD();
        if (container.isReportCrosstab()) {
            originalMeasIndex = repBD.findMeasureIndexInCT(container, columnName);
            originalMeasIndex = originalMeasIndex - (container.getViewByCount());
            ArrayList measureList = container.getTableDisplayMeasures();
            measure = (String) measureList.get(originalMeasIndex);
        }
        if (colorGroup.isColorCodePresent(columnName, repParameter, measure)) {
            String color = colorGroup.getColor(columnName, new Double(endvalu), repParameter, measure, diff);
            if (!color.equalsIgnoreCase("")) {
                int intValue = Integer.parseInt(color.replace("#", ""), 16);
                aColor = new Color(intValue);
            }
        }
        return aColor;
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

    public String queryPdfDownload(PbReturnObject retObj, String connId, HttpServletResponse response) throws DocumentException, IOException {
        ServletWriterTransferObject swt = null;
        swt = ServletUtilities.createBufferedWriter("test", "pdf");
        document = new Document(PageSize.A4, 50, 50, 50, 50);
        writer = PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName));
//         writer = PdfWriter.getInstance(document, bos);
        document.open();

        PdfPTable table = null;
        PdfPCell cell = null;

        float width = document.getPageSize().getWidth();
        int columnCount = retObj.getColumnCount();
        float colWidth = width / columnCount;
        float[] columnDefinitionSize = new float[columnCount];

        for (int i = 0; i < columnCount; i++) {
            columnDefinitionSize[i] = colWidth;
        }
        table = new PdfPTable(columnDefinitionSize);

        table.setTotalWidth(colWidth * columnCount);
        table.setLockedWidth(false);
        table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);

        for (int i = 0; i < columnCount; i++) {
            cell = new PdfPCell(new Phrase(retObj.getColumnNames()[i], fontHead));
            cell.setBackgroundColor(Color.lightGray);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setNoWrap(false);
            table.addCell(cell);
        }
        for (int i = 0; i < retObj.rowCount; i++) {
            for (int col = 0; col < columnCount; col++) {
                cell = new PdfPCell(new Phrase(retObj.getFieldValueString(i, col), font));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                cell.setNoWrap(false);
                table.addCell(cell);
            }
        }
        document.add(table);
        document.close();
        return swt.fileName;
    }
}
