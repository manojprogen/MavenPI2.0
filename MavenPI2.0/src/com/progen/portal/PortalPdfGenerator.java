/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.progen.portal.portlet.PortletProcessor;
import java.awt.Color;
import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import utils.db.ProgenParam;

/**
 *
 * @author progen
 */
public class PortalPdfGenerator {

    public static Logger logger = Logger.getLogger(PortalPdfGenerator.class);
    Date date = new Date();
    String DATE_FORMAT = "MM/dd/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    private HttpServletResponse response = null;
    Document document = null;
    ByteArrayOutputStream bos = null;
    private PdfWriter writer;
    private String headerTitle = "";
    private String fileName;
    private String reportName = "";
    private HttpServletRequest request = null;
    private PbReturnObject pbReturnObject = null;
    private String[] displayColumns = null;
    Font fontHead = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14);
    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
    private int viewByCount;
    private int count;
    private String[] types = null;
    private ArrayList displayLabelsPDf;
    private String contentType;

    public void portalPDF() {
        String portalTabId = String.valueOf(getRequest().getParameter("portalTabId"));
        List<Portal> portals = (List<Portal>) getRequest().getSession(false).getAttribute("PORTALS");
        Portal tempPortal = Iterables.find(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalTabId)));

        document = new Document(PageSize.A0, 50, 50, 50, 50);
        try {
            bos = new ByteArrayOutputStream();
            writer = PdfWriter.getInstance(document, bos);
            Date todaysDate = Calendar.getInstance().getTime();
            fileName = tempPortal.getPortalName().replace(" ", "") + ".pdf";
            if ("PortalPDF".equalsIgnoreCase(getContentType())) {
                fileName = tempPortal.getPortalName().replace(" ", "") + ".pdf";
                writer = PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + fileName));
            }
            document.open();
            PdfPTable logoTable = new PdfPTable(2);
            logoTable.setHorizontalAlignment(Element.ALIGN_TOP);//Code 3
            logoTable.setSpacingAfter(10f);
            File file = new File("Logo.png");
            InputStream servletStream = request.getSession().getServletContext().getResourceAsStream("images/pi_logo.png");
            OutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = servletStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            servletStream.close();
            Image logoimage = Image.getInstance(file.getPath());
            logoimage.setAlignment(Image.LEFT);
            PdfPCell img1 = new PdfPCell(logoimage);
            img1.setHorizontalAlignment(Element.ALIGN_LEFT);
            img1.setMinimumHeight(10f);
            img1.setBorder(Rectangle.NO_BORDER);
            file = new File("Logo2.gif");
            servletStream = request.getSession().getServletContext().getResourceAsStream("/images/prgLogo.gif");
            out = new FileOutputStream(file);

            while ((len = servletStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            servletStream.close();
//            file=new File((request.getSession().getServletContext().getResource("/images/prgLogo.gif")).toString());
            logoimage = Image.getInstance(file.getPath());
            logoimage.setAlignment(Image.RIGHT);
            PdfPCell img2 = new PdfPCell(logoimage);
            img2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            img2.setMinimumHeight(10f);
            img2.setBorder(Rectangle.NO_BORDER);
            logoTable.addCell(img1);
            logoTable.addCell(img2);
            document.add(logoTable);

//            document.add(new Paragraph(getHeaderTitle()));
//            document.add(new Paragraph("Report Title : " + getReportName().replace("_", " ")));
//            document.add(new Paragraph("Created on  :" + sdf.format(date)));
            PortletProcessor portalProcessor = new PortletProcessor();
            PdfPTable table = new PdfPTable(3);
            ArrayListMultimap<String, PortLet> coloumListMultimap = ArrayListMultimap.create();
            TreeSet<Integer> sequenceSet = new TreeSet<Integer>();
            for (PortLet portlet : tempPortal.getPortlets()) {

                coloumListMultimap.put(Integer.toString(portlet.getColumnNumber()), portlet);
                sequenceSet.add(portlet.getSeqnumber());
            }
            Set<String> coloumnSet = coloumListMultimap.keySet();
            List<PortLet> coloumnOnePortlets = (List<PortLet>) coloumListMultimap.get("1");
            List<PortLet> coloumnTwoPortlets = (List<PortLet>) coloumListMultimap.get("2");
            List<PortLet> coloumnThreePortlets = (List<PortLet>) coloumListMultimap.get("3");
            for (int count = 0; count < sequenceSet.last(); count++) {
                PortLet portLet = null;
                for (int i = 0; i < 3; i++) {
                    if (i == 0) {
                        if (!coloumnOnePortlets.isEmpty()) {
                            portLet = coloumnOnePortlets.get(0);
                            coloumnOnePortlets.remove(portLet);
                        } else {
                            portLet = null;
                        }
                    } else if (i == 1) {
                        if (!coloumnTwoPortlets.isEmpty()) {
                            portLet = coloumnTwoPortlets.get(0);
                            coloumnTwoPortlets.remove(portLet);
                        } else {
                            portLet = null;
                        }
                    } else if (i == 2) {
                        if (!coloumnThreePortlets.isEmpty()) {
                            portLet = coloumnThreePortlets.get(0);
                            coloumnThreePortlets.remove(portLet);
                        } else {
                            portLet = null;
                        }
                    } else {
                        portLet = null;
                    }
                    if (portLet != null) {
                        portalProcessor.setIfFxCharts(false);
                        ProgenParam pparam = new ProgenParam();
                        if (portLet.getWhereClause() != null) {
                            portalProcessor.setWhereClause(portLet.getWhereClause());
                        }
                        if (portLet.getRuleOn() != null) {
                            portalProcessor.setRuleOn(portLet.getRuleOn());
                        }
                        if (portLet.getReportParams() != null) {
                            portalProcessor.setReportParams(portLet.getReportParams());
                        }
                        portalProcessor.setMeasureIDs(portLet.getMeasureID());
                        portalProcessor.setAggreGationType(portLet.getAggreGationType());
                        portalProcessor.setMeasureNames(portLet.getMeasureNames());
                        if (portLet.getGraphProperty() != null) {
                            portalProcessor.setPortletProperty(portLet.getGraphProperty());
                        }
                        if (portLet.getXMLDocument() != null && portLet.getPortletType().equalsIgnoreCase("I")) {
                            portalProcessor.setXmlDocument(portLet.getXMLDocument());
                            portalProcessor.setPortLet(portLet);
                        }

                        if (portLet.getPortletXMLHelper().getMetaInfo().get("DisplayType").toString().equalsIgnoreCase("Table")) {
                            portalProcessor.processDataSet("");
                            pbReturnObject = portalProcessor.getReturnObject();
                            displayLabelsPDf = portalProcessor.getDisplayLabelsForPdf();
                            PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
                            PdfPTable tempTable = new PdfPTable(1);
                            tempTable.setWidthPercentage(100);
                            tempTable.setSpacingBefore(10.0f);
                            tempTable.setSpacingAfter(10f);
                            tempTable.getDefaultCell().setBorder(1);
                            PdfPCell nameCell = new PdfPCell(new Phrase(portLet.getPortLetName(), FontFactory.getFont(FontFactory.TIMES_BOLD, 15)));
                            nameCell.setBackgroundColor(new Color(66, 151, 215));
                            nameCell.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                            nameCell.setNoWrap(false);
                            tempTable.addCell(nameCell);
                            cell1.addElement(tempTable);
                            cell1.addElement(buildTable(portLet.getPortLetName()));

                            table.addCell(cell1);
                        } else if (portLet.getPortletXMLHelper().getMetaInfo().get("DisplayType").toString().equalsIgnoreCase("Graph") || portLet.getPortletXMLHelper().getMetaInfo().get("DisplayType").toString().equalsIgnoreCase("KPI Graph")) {
//                        PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
                            PdfPTable tempTable = new PdfPTable(1);
                            tempTable.setWidthPercentage(100);
                            tempTable.setSpacingBefore(10.0f);
                            tempTable.setSpacingAfter(10f);
                            tempTable.getDefaultCell().setBorder(1);
                            PdfPCell nameCell = new PdfPCell(new Phrase(portLet.getPortLetName(), FontFactory.getFont(FontFactory.TIMES_BOLD, 15)));
                            nameCell.setBackgroundColor(new Color(66, 151, 215));
                            nameCell.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                            nameCell.setNoWrap(false);
                            tempTable.addCell(nameCell);
//                        cell1.addElement(tempTable);
                            Image image = null;
                            try {
                                image = Image.getInstance(System.getProperty("java.io.tmpdir") + "/" + portLet.getImgName());
                            } catch (IOException iOException) {
                                image = null;
                            }
                            PdfPCell cell1 = null;
                            if (image != null) {
                                cell1 = new PdfPCell(image, true);
                            } else {
                                cell1 = new PdfPCell(Image.getInstance(file.getPath()), true);
                            }
                            tempTable.addCell(cell1);
                            PdfPCell cell12 = new PdfPCell(new Paragraph("Cell 1"));
                            cell12.addElement(tempTable);
                            table.addCell(cell12);
                        } else {
                            PdfPCell cell1 = new PdfPCell(new Paragraph("kpi"));
                            table.addCell(cell1);

                        }
                    } else {
                        PdfPCell cell1 = new PdfPCell(new Paragraph(""));
                        cell1.setBorder(PdfPCell.NO_BORDER);
                        table.addCell(cell1);
                    }

                }

            }
            document.add(table);
            document.close();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(bos.size());
            ServletOutputStream outputstream = response.getOutputStream();
            bos.writeTo(outputstream);
            outputstream.flush();

        } catch (DocumentException de) {
            logger.error("Exception:", de);
        } catch (IOException ioe) {
            logger.error("Exception:", ioe);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }


    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public PdfPTable buildTable(String portletName) {
        PdfPTable table = null;
        PdfPCell cell = null;
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);
        try {
            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            int columnCount = pbReturnObject.getColumnCount();
            displayColumns = pbReturnObject.getColumnNames();
            count = pbReturnObject.getRowCount();
            types = pbReturnObject.getColumnTypes();

            table = new PdfPTable(columnCount);
            table.setWidthPercentage(100);

            table.setSpacingBefore(10.0f);
            table.setSpacingAfter(10f);

            table.getDefaultCell().setBorder(1);

            for (int i = 0; i < displayLabelsPDf.size(); i++) {
                cell = new PdfPCell(new Phrase(displayLabelsPDf.get(i).toString(), font));
                cell.setBackgroundColor(Color.lightGray);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setNoWrap(false);
                table.addCell(cell);
                table.setSpacingBefore(10.0f);
            }
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < columnCount; j++) {
                    String colName = displayColumns[j];
                    // String discolName = displayLabels[j];
                    if (types[j].equalsIgnoreCase("VARCHAR2")) {
                        cell = new PdfPCell(new Phrase(pbReturnObject.getFieldValueString(i, colName), font));
                    } else if (types[j].equalsIgnoreCase("NUMBER")) {
                        cell = new PdfPCell(new Phrase(nFormat.format(pbReturnObject.getFieldValueBigDecimal(i, colName)), font));
                    }
                    cell.setNoWrap(false);
                    table.addCell(cell);

                    colName = null;
                }


            }





            // document.add(table);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }


        return table;

    }

    public PbReturnObject getPbReturnObject() {
        return pbReturnObject;
    }

    public void setPbReturnObject(PbReturnObject pbReturnObject) {
        this.pbReturnObject = pbReturnObject;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}