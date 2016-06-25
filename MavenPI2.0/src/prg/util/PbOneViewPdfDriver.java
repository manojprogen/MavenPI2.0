/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.itextpdf.awt.geom.Point;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.db.OneViewLetDetails;

/**
 *
 * @author srikanth.p@progenbusiness.com
 */
public class PbOneViewPdfDriver {

    public static Logger logger = Logger.getLogger(PbOneViewPdfDriver.class);
    Document document = null;
    ByteArrayOutputStream bos = null;
    ByteArrayInputStream bis = null;
    PdfWriter writer = null;
    private String[] filePaths = null;
    private HttpServletRequest request = null;
    public String logoPath = null;
    private String headerTitle;
    private List<String> timeDetailsArray;
    private HashMap ColorCodeMap = null;
    private int fromRow;
    private int toRow;
    private ArrayList<PdfPTable> oneTable;
    private String fileName = null;
    private HttpServletResponse response = null;
    Date date = new Date();
    String DATE_FORMAT = "MM-dd-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    private String title;
    public String measureFmtType;
    public String viewletName;
    public String measCurrVal;
    public String measPriorVal;
    public String formatVal;
    public String viewletNum;
    public int rowSpan = 1;
    public int colSpan = 1;
    public int viewletHeight;
    public int viewletWidth;
    public String prefixVal = "";
    private BigDecimal calCurrVal;
    private BigDecimal calPriorVal;
    private BigDecimal calChPer;
    private String measureType;
    private float currWidth;
    private float priorWidth;
    private String filePath;
    private int radius1;
    private int radius2;
    private java.awt.Image pdfimage;
    private int i = 0;
    public float oneViewWidth;
    public float oneViewHeight;
    private BaseColor measFontColor = new BaseColor(100, 178, 255);
    private int fontSize = 9;
    private int trendDays;
    private Rectangle pagesize;
    private boolean isOnePage = true;
    private String fitTo;
    private String pageType;
    private int templateBarSize = 40;
    private int circleRadius1 = 15;
    private int circleRadius2 = 12;
    private int squreSide1 = 30;
    private int squreSide2 = 24;
    public String fromDate;
    public String toDate;
    private boolean resizeImage;
    BaseColor currColor = new BaseColor(0, 149, 182);
    BaseColor priorColor = new BaseColor(154, 205, 50);
    private HashMap<String, ArrayList> noteMap = null;
    public int logoWidth;
    public int logoHeight;
    public int arrowHeight;
    public int arrowWidth;

    public void getPdfImage(java.awt.Image image) {
        pdfimage = image;

    }

    /*
     * public String createOneViewPdf(OnceViewContainer oneContainer, int
     * TableNum){ document = new Document(PageSize.B2, 50, 50, 50, 50); Image
     * image = null; File file = null; FileInputStream fis = null; byte[] bytes
     * = null; PdfPTable table = null; PdfPCell cell = null; File tempFile=null;
     * //int i=0;
     *
     * try{ bos = new ByteArrayOutputStream(); writer =
     * PdfWriter.getInstance(document, bos); document.open();
     * List<OneViewLetDetails> oneViewlets=oneContainer.onviewLetdetails;
     * for(OneViewLetDetails viewlet:oneViewlets){ //if(i==0){
     * if(viewlet.getReptype().equalsIgnoreCase("repGraph")){
     * document=createOneVIewGraph(viewlet.getRepName(),viewlet.getNoOfViewLets(),TableNum);
     *
     * }
     * //} if(viewlet.getReptype().equalsIgnoreCase("template")){ //
     * document=createMesureType1(viewlet,TableNum); }
     * if(viewlet.getReptype().equalsIgnoreCase("measures")){ //
     * document=createMesureType2(oneContainer,TableNum); } } document.close();
     *
     * ServletUtilities.createTempDir(); tempFile =
     * File.createTempFile(getFileName(),".pdf"); FileOutputStream fos=new
     * FileOutputStream(tempFile); bos.writeTo(fos); fos.close(); bos.close();
     *
     *
     * /*response.setContentType("application/pdf");
     * response.setHeader("Content-Disposition", "attachment;filename=" +
     * fileName); response.setContentLength(bos.size());
     *
     * ServletOutputStream outputstream = response.getOutputStream();
     * bos.writeTo(outputstream);
            outputstream.flush();
     */
    /*
     * }catch(Exception e){ logger.error("Exception: ",e); } return
     * tempFile.getName(); }
     */
    /*
     * public Document createOneVIewGraph(String viewletName,String
     * viewletId,int tableNum){ Image image = null; File file = null;
     * FileInputStream fis = null; byte[] bytes = null; PdfPTable table = null;
     * PdfPCell cell = null; try{ table=oneTable.get(tableNum); file = new
     * File(System.getProperty("java.io.tmpdir")+viewletId+"img_.png");
     *
     * fis = new FileInputStream(file); bytes = new byte[fis.available()];
     *
     * while (-1 != fis.read(bytes)) { } //if(pdfimage.get(i)!=null)
     * //image=com.itextpdf.text.Image.getInstance(pdfimage,null); image =
     * Image.getInstance(bytes); if (image != null) { //document.add(image);
     * PdfPTable innerTable=new PdfPTable(1); PdfPCell innerCell=new PdfPCell();
     * innerCell.setBorder(Rectangle.NO_BORDER);
     * innerTable.addCell(drowHeader()); innerCell = new PdfPCell(image);
     * innerCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
     * innerCell.setNoWrap(false); innerCell.setBorder(0);
     * innerTable.addCell(innerCell); PdfPCell outerCell=new
     * PdfPCell(innerTable); // setouterCellProp(outerCell); PdfPTable
     * outerTable=oneTable.get(tableNum); outerCell.setBorderWidthLeft(0f);
     * outerTable.addCell(outerCell); } document.add(table); }catch(Exception
     * e){ logger.error("Exception: ",e); }
     *
     *
     * return document; }
     */
    public void createOneVIewGraph1(int table) {
        Image image = null;
        File file = null;
        FileInputStream fis = null;
        byte[] bytes = null;
        PdfPTable table1 = null;
        PdfPCell cell = null;
        int vAlignment = PdfPCell.ALIGN_CENTER;
        int hAlignment = PdfPCell.ALIGN_CENTER;
        try {
            table1 = oneTable.get(table);
            /*
             * file = new
             * File(System.getProperty("java.io.tmpdir")+File.separator+viewletNum+"_img.png");
             * if(resizeImage){ // BufferedImage convertImage =
             * ImageIO.read(file); // int type = convertImage.getType() == 0?
             * BufferedImage.TYPE_INT_ARGB : convertImage.getType(); //
             * BufferedImage resizeImagePng=resizeImage(convertImage,
             * type,(int)(getRelativeWidthForSinglePage(table1)-10),(int)(getRelativeHeightForSinglePage()-30));
             * // ImageIO.write(resizeImagePng, "png", file); }
             *
             * fis = new FileInputStream(file); bytes = new
             * byte[fis.available()];
             *
             * while (-1 != fis.read(bytes)) { }
             *
             * image = Image.getInstance(bytes);
            image.setDpi(200,200);
             */

            java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("java.io.tmpdir") + File.separator + viewletNum + "_img.png");
            image = Image.getInstance(writer, awtImage, 0.8F);



            if (resizeImage) {
                float relWidth = getRelativeWidthForSinglePage(table1) * (float) 0.70;
                float relHeight = getRelativeHeightForSinglePage() * (float) 0.70;
//                image.scaleAbsolute((float)relWidth*(float)0.48, (float)relHeight*(float)0.48);
                image.scaleAbsolute(relWidth, relHeight);
                vAlignment = PdfPCell.ALIGN_BOTTOM;
                hAlignment = PdfPCell.ALIGN_CENTER;
            } else {
//                float relHeight=getRelativeHeightForSinglePage()*(float)0.70;
//                image.scaleAbsolute(viewletWidth, relHeight);
                image.scaleAbsolute(viewletWidth, viewletHeight);
            }
            if (image != null) {
                //document.add(image);
                PdfPTable innerTable = new PdfPTable(1);
                PdfPTable innerTable1 = new PdfPTable(2);
                PdfPCell innerCell = new PdfPCell();
                innerCell.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
                innerTable.addCell(drowHeader());
                innerCell = new PdfPCell(image);
                innerCell.setHorizontalAlignment(hAlignment);
                innerCell.setVerticalAlignment(vAlignment);
                innerCell.setNoWrap(false);
                innerCell.setBorder(Rectangle.NO_BORDER);

                innerTable.addCell(innerCell);
                PdfPCell outerCell = new PdfPCell(innerTable);
                setouterCellProp(outerCell, table);
                table1.addCell(outerCell);
            }
        } catch (BadElementException | IOException e) {
            logger.error("Exception: ", e);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    private static BufferedImage resizeImage(BufferedImage convertImage, int type, int width, int height) {

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(convertImage, 0, 0, width, height, null);
        g.dispose();
        Kernel kernel = new Kernel(3, 3, new float[]{-1, -1, -1, -1, 9, -1, -1, -1, -1});
        ConvolveOp cop = new ConvolveOp(kernel);
        BufferedImage img = cop.filter(resizedImage, null);

        return img;
//        return resizedImage;
    }

    public BaseColor getMeasFontColor() {
        return measFontColor;
    }

    public void setMeasFontColor(BaseColor measFontColor) {
        this.measFontColor = measFontColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /*
     * This Inner class is created to build header ,Footer obased on Events .It
     * can be used fuether for automatic event handling
     */
    /*
     * public class TableHeader extends PdfPageEventHelper{ @Override public
     * void onStartPage(PdfWriter pWriter,Document document){
     *
     * }
     * @Override public void onEndPage(PdfWriter pWriter,Document document){
     *
     * }
     * @Override public void onOpenDocument(PdfWriter pWriter,Document
     * document){
     *
     * }
     * }
     */
    public void openOneViewPdf() {
        document = new Document(pagesize, 0, 0, 5, 3);
        try {
            bos = new ByteArrayOutputStream();
            writer = PdfWriter.getInstance(document, bos);

            /*
             * document.setMargins(8,8,8,8); TableHeader event=new
             * TableHeader(); writer.setPageEvent(event);
             */
//            document.setPageSize(PageSize.A4_LANDSCAPE);
            document.open();
            pagesize = document.getPageSize();

            oneTable = new ArrayList<PdfPTable>();
        }
        catch (DocumentException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception: ", ex);
        }    }

    public void setPaperColumns(int columns) {

        float width = pagesize.getWidth();
        float height = pagesize.getHeight();
        float innerWidth = width;

        PdfPTable table = new PdfPTable(columns);
        table.setTotalWidth(innerWidth);
        if (columns >= 6) {
            fontSize = 9;
            templateBarSize = 30;
        }

//        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        oneTable.add(table);
    }

    public void createMesureType1(OneViewLetDetails viewlet, int table) {
        PdfPCell cell = new PdfPCell(new Phrase("Template"));

        cell.setFixedHeight((float) viewlet.getHeight());
        cell.setColspan(viewlet.getColSpan());
        cell.setRowspan(viewlet.getRowSpan());
        PdfPTable localTable = oneTable.get(table);
        localTable.addCell(cell);

    }

    public void addTableToDoc() {
        try {
            for (int i = 0; i < oneTable.size(); i++) {
                document.add(oneTable.get(i));
            }

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public String getCreatedOneVIewPdf() {
        File tempFile = null;
        try {
            addTableToDoc();
            document.close();
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }
//            tempFile = File.createTempFile(getFileName(),".pdf");
            tempFile = new File(tempDir, title + "_" + sdf.format(new Date()) + ".pdf");
            FileOutputStream fos = new FileOutputStream(tempFile);
            bos.writeTo(fos);
            fos.close();
            bos.close();
        }
        catch (IOException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return tempFile.getName();
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

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void createDocumentHeader() {

        PdfPTable headerTable = new PdfPTable(3);
        try {
            headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            headerTable.getDefaultCell().setHorizontalAlignment(Rectangle.ALIGN_LEFT);
            headerTable.getDefaultCell().setBorderWidthBottom(0.1f);
            headerTable.setWidths(new int[]{50, 15, 35});
//                headerTable.setTotalWidth(document.getPageSize().getWidth());


            PdfPCell innerCell = new PdfPCell();

            PdfPTable innerTable = new PdfPTable(2);
            innerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            innerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            innerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.addElement(new Phrase(getHeaderTitle(), new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, new BaseColor(100, 178, 255))));
            innerCell.setColspan(2);
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerTable.addCell(innerCell);

            innerCell = new PdfPCell(new Phrase("OneView Title ", new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, new BaseColor(0, 0, 0))));
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);
            innerTable.addCell(innerCell);
            innerCell = new PdfPCell(new Phrase(":" + title, new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD, new BaseColor(100, 178, 255))));
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);
            innerTable.addCell(innerCell);
//                Phrase p=new Phrase();
//                p.add(left);
//                p.add(right);
//                paraGraph.add(p);
//                paraGraph.add(Chunk.NEWLINE);
            innerCell = new PdfPCell(new Phrase("Created On", new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);
            innerTable.addCell(innerCell);
            innerCell = new PdfPCell(new Phrase(":" + sdf.format(date), new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);

            innerTable.addCell(innerCell);
//                p=new Phrase();
//                p.add(left);
//                p.add(right);
//                paraGraph.add(p);
//                paraGraph.setAlignment(Element.ALIGN_BOTTOM);
//
//
//                innerCell.addElement(paraGraph);
//                innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                innerCell.setBorder(Rectangle.NO_BORDER);
//                innerCell.setBorderWidthBottom(0.1f);
            PdfPCell headercell = new PdfPCell(innerTable);
            headercell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headercell.setVerticalAlignment(Element.ALIGN_LEFT);
            headercell.setBorder(Rectangle.NO_BORDER);
            headercell.setBorderWidthBottom(0.1f);

            headerTable.addCell(headercell);
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setRowspan(3);
            emptyCell.setBorder(Rectangle.NO_BORDER);
            emptyCell.setBorderWidthBottom(0.1f);
            headerTable.addCell(emptyCell);

            innerTable = new PdfPTable(2);
            innerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            innerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                paraGraph=new Paragraph();
            innerTable.addCell(new Phrase("Date", new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerTable.addCell(new Phrase(":" + timeDetailsArray.get(2).toString(), new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0))));
//                p=new Phrase();
//                p.add(left);
//                p.add(right);
//                paraGraph.add(p);
//                paraGraph.add(Chunk.NEWLINE);

            innerTable.addCell(new Phrase("Duration", new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerTable.addCell(new Phrase(":" + timeDetailsArray.get(3).toString(), new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0))));
//                p=new Phrase();
//                p.add(left);
//                p.add(right);
//                paraGraph.add(p);
//                paraGraph.add(Chunk.NEWLINE);

            innerTable.addCell(new Phrase("Compare With", new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerTable.addCell(new Phrase(":" + timeDetailsArray.get(4).toString(), new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD, new BaseColor(0, 0, 0))));
//                p=new Phrase();
//                p.add(left);
//                p.add(right);
//                paraGraph.add(p);
//                paraGraph.add(Chunk.NEWLINE);
//                paraGraph.setAlignment(Element.ALIGN_CENTER);
            innerCell = new PdfPCell();
            innerCell.addElement(innerTable);
            innerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setBorderWidthBottom(0.1f);

            headerTable.addCell(innerCell);
            document.add(headerTable);

//
        }
        catch (DocumentException ex) {
             logger.error("Exception: ", ex);
        } catch (Exception ex) {
             logger.error("Exception: ", ex);
        }    }

    public void createDocuementHeader1() {
        PdfPTable headerTable = new PdfPTable(3);
        PdfPCell headerCell = new PdfPCell();
        try {
            headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            headerTable.getDefaultCell().setHorizontalAlignment(Rectangle.ALIGN_LEFT);
            headerTable.getDefaultCell().setBorderWidthBottom(1.0f);
            headerTable.setWidths(new int[]{50, 15, 35});
            PdfPTable innerTable = new PdfPTable(2);
            PdfPCell innerCell = new PdfPCell();
            innerCell.addElement(new Phrase(title, FontFactory.getFont("calibri", 9, Font.BOLD, new BaseColor(0, 149, 182)))); //new BaseColor(100, 178, 255)
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setColspan(2);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);
            innerTable.addCell(innerCell);
            innerCell = new PdfPCell(new Phrase("From Date", FontFactory.getFont("calibri", 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);
            innerTable.addCell(innerCell);
            innerCell = new PdfPCell(new Phrase(":" + fromDate, FontFactory.getFont("calibri", 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);
            innerTable.addCell(innerCell);
            innerCell = new PdfPCell(new Phrase("To Date", FontFactory.getFont("calibri", 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);
            innerTable.addCell(innerCell);
            innerCell = new PdfPCell(new Phrase(":" + toDate, FontFactory.getFont("calibri", 9, Font.BOLD, new BaseColor(0, 0, 0))));
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
            innerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            innerCell.setNoWrap(true);
            innerTable.addCell(innerCell);
            headerCell.addElement(innerTable);
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setBorderWidthBottom(1.0f);
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerCell.setVerticalAlignment(Element.ALIGN_LEFT);
//                headerCell.setNoWrap(true);
            headerTable.addCell(headerCell);
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setRowspan(3);
            emptyCell.setBorder(Rectangle.NO_BORDER);
            emptyCell.setBorderWidthBottom(1.0f);
            headerTable.addCell(emptyCell);
            Image logoImg = getImageFromPath(logoPath);
            logoImg.scaleAbsolute(logoWidth, logoHeight);
            emptyCell = new PdfPCell(logoImg);
            emptyCell.setRowspan(3);
            emptyCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            emptyCell.setVerticalAlignment(Element.ALIGN_BASELINE);
            emptyCell.setBorder(Rectangle.NO_BORDER);
            emptyCell.setBorderWidthBottom(1.0f);
            headerTable.addCell(emptyCell);
            document.add(headerTable);
        }
        catch (DocumentException ex) {
             logger.error("Exception: ", ex);
        }catch (Exception ex) {
             logger.error("Exception: ", ex);
        }    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List getTimeDetailsArray() {
        return timeDetailsArray;
    }

    public void setTimeDetailsArray(List timeDetailsArray) {
        this.timeDetailsArray = timeDetailsArray;
    }

    public void createMeasureType1(int tableNum) {
        PdfPTable innerTable = new PdfPTable(1);
        PdfPCell innerCell = new PdfPCell();
        innerTable.addCell(drowHeader());
        Phrase value = formatMeasure(measCurrVal, fontSize, Font.BOLD, measFontColor);
        innerCell = new PdfPCell(value);
        innerCell.setVerticalAlignment(Element.ALIGN_CENTER);
        innerCell.setBorder(Rectangle.NO_BORDER);
        innerCell.setPaddingRight(1f);
        innerCell.setPaddingLeft(1f);
        innerCell.setBorder(0);
        innerTable.addCell(innerCell);
        PdfPCell outerCell = new PdfPCell(innerTable);
        setouterCellProp(outerCell, tableNum);
        PdfPTable outerTable = oneTable.get(tableNum);
        outerTable.addCell(outerCell);
    }

    public PdfPCell drowHeader() {
        Paragraph innerTitle = new Paragraph(viewletName, FontFactory.getFont("calibri", fontSize + 1, Font.BOLD));
        PdfPCell innerCell = new PdfPCell();
        innerCell.addElement(innerTitle);
//        innerCell.addElement(drowHeader(tableNum));
        innerCell.setBorder(Rectangle.NO_BORDER);
        innerCell.setBorderWidthBottom(0.2f);
        innerCell.setBorderColorBottom(BaseColor.GRAY);
//        innerCell.setPaddingRight(10f);
        innerCell.setVerticalAlignment(Element.ALIGN_LEFT);
        return innerCell;

    }

    public void setouterCellProp(PdfPCell outerCell, int tabId) {
        outerCell.setBorder(Rectangle.NO_BORDER);

        if (pageType.equalsIgnoreCase("A4_Landascpe")) {
            if (fitTo.equalsIgnoreCase("SinglePage")) {
                outerCell.setFixedHeight(getRelativeHeightForSinglePage());
            }
            if (fitTo.equalsIgnoreCase("MultiPage")) {
                outerCell.setFixedHeight((float) viewletHeight);
            }
        } else {
            if (fitTo.equalsIgnoreCase("SinglePage") && oneViewHeight >= pagesize.getHeight()) {
                outerCell.setFixedHeight(getRelativeHeightForSinglePage());
            }
            if (fitTo.equalsIgnoreCase("MultiPage")) {
                outerCell.setFixedHeight((float) viewletHeight);
            }
        }
//        outerCell.setFixedHeight((getRelativeHeight(tabId)));

        outerCell.setColspan(colSpan);
        outerCell.setRowspan(rowSpan);
        outerCell.setPaddingRight(10f);

    }

    public void createMeasureType2(int tableNum) {
        PdfPTable innerTable = new PdfPTable(1);
        PdfPTable outerTable = oneTable.get(tableNum);
        float outerWidth = outerTable.getTotalWidth();
        float innerWidth = ((outerWidth / outerTable.getNumberOfColumns()) / 6);
        innerTable.setTotalWidth(innerWidth);
        innerTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell innerCell = new PdfPCell();
        innerTable.addCell(drowHeader()); //builds Header
        PdfPTable contentTable = new PdfPTable(1);
        PdfPCell contentCell = new PdfPCell();
        Chunk measType = new Chunk(measureType, new Font(FontFamily.TIMES_ROMAN, (fontSize / 2) + 2, Font.NORMAL));

        Chunk currImag = buildCurrentImage(currWidth, true);

        Phrase currVal = formatMeasure(measCurrVal, (fontSize / 2) + 2, Font.NORMAL, BaseColor.BLACK);
        Phrase finalVal = new Phrase();
        finalVal.add(measType);
        finalVal.add(currImag);
        finalVal.add(currVal);
        finalVal.add(Chunk.NEWLINE);


        Chunk priorType = new Chunk("Prior", new Font(FontFamily.TIMES_ROMAN, (fontSize / 2) + 2, Font.NORMAL));

        Chunk priorImg = buildCurrentImage(priorWidth, false);

        Phrase priorVal = formatMeasure(measPriorVal, (fontSize / 2) + 2, Font.NORMAL, BaseColor.BLACK);
//        Phrase finalPrior=new Phrase();
        finalVal.add(priorType);
        finalVal.add(priorImg);
        finalVal.add(priorVal);
        contentCell.addElement(finalVal);
//        contentCell=new PdfPCell(priorVal);
//        contentCell.addElement(priorVal);
        contentCell.setBorder(Rectangle.NO_BORDER);
        contentCell.setNoWrap(true);
        contentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        contentCell.setVerticalAlignment(Element.ALIGN_TOP);
        contentTable.addCell(contentCell); //(2,3)
        innerCell = new PdfPCell(contentTable);
        innerCell.setBorder(Rectangle.NO_BORDER);
        innerTable.addCell(innerCell);
        PdfPCell outerCell = new PdfPCell(innerTable);
        setouterCellProp(outerCell, tableNum);
        outerTable.addCell(outerCell);//adding content cell to main table
    }

    public Chunk buildCurrentImage(float width, boolean isCurrent) {
        Image img = null;
        Chunk imgChunk = null;
        PdfContentByte canvas = writer.getDirectContent();
        PdfTemplate template = null;
//        if(isCurrent){
//            template=canvas.createTemplate((templateBarSize*calCurrVal.intValue())/Math.max(calCurrVal.intValue(),calPriorVal.intValue()),8);
        template = canvas.createTemplate(width, 8);
//        }else{
//            template=canvas.createTemplate((templateBarSize*calPriorVal.intValue())/Math.max(calCurrVal.intValue(),calPriorVal.intValue()),8);
//        }


        template.setLineWidth(0.1f);
        template.rectangle(5, 2, width, 10);
        if (isCurrent) {
            template.setColorFill(new BaseColor(0, 149, 182));
        } else {
            template.setColorFill(new BaseColor(154, 205, 50));
        }
        template.fill();
        template.stroke();
        try {
            img = Image.getInstance(template);
            imgChunk = new Chunk(img, -2f, -2f);
        }
        catch (BadElementException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }       return imgChunk;

    }

    public void createMeasureType3(int tableNum) {
        try {
            PdfPTable contentTable = new PdfPTable(2);
            contentTable.setWidths(new int[]{55, 45});
            contentTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Phrase currVal = formatMeasure(measCurrVal, fontSize, Font.NORMAL, measFontColor);
            PdfPCell contentCell = new PdfPCell(currVal);
            contentCell.setBorder(Rectangle.NO_BORDER);
            contentTable.addCell(contentCell);

            PdfPTable imageTable = new PdfPTable(2);
            Image arrowImg = getImageFromPath(filePath);
            arrowImg.scaleAbsoluteHeight(arrowHeight);
            arrowImg.scaleAbsoluteWidth(arrowWidth);
            contentCell = new PdfPCell(arrowImg);
            contentCell.setBorder(Rectangle.NO_BORDER);
            contentCell.setHorizontalAlignment(Rectangle.ALIGN_RIGHT);
            contentCell.setVerticalAlignment(Element.ALIGN_TOP);
            contentCell.setNoWrap(true);
            imageTable.addCell(contentCell);
            String chPerString = "(" + calChPer.toString() + "%)";
            Phrase chPer = new Phrase(chPerString, new Font(FontFamily.TIMES_ROMAN, (float) fontSize - 4, Font.NORMAL, measFontColor));
            contentCell = new PdfPCell(chPer);
            contentCell.setBorder(Rectangle.NO_BORDER);
            contentCell.setVerticalAlignment(Element.ALIGN_CENTER);
            contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            contentCell.setNoWrap(true);
            imageTable.addCell(contentCell);

            contentTable.addCell(imageTable);

            PdfPTable innerTable = new PdfPTable(1);
            PdfPCell innerCell = new PdfPCell();
            innerTable.addCell(drowHeader());
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerCell = new PdfPCell(contentTable);
            innerCell.setPaddingTop(1f);
            innerCell.setBorder(Rectangle.NO_BORDER);
            innerTable.addCell(innerCell);
            PdfPCell outerCell = new PdfPCell(innerTable);
            setouterCellProp(outerCell, tableNum);
            PdfPTable outerTable = oneTable.get(tableNum);
            outerTable.addCell(outerCell);
        }
        catch (DocumentException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }    }

    public void createMeasureType4(int tableNum) {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.addCell(drowHeader());
        PdfPTable contentTable = new PdfPTable(1);
        PdfPCell contentCell = new PdfPCell();
        if (calCurrVal.intValue() < calPriorVal.intValue()) {
            circleRadius1 = circleRadius1 + circleRadius2;
            circleRadius2 = circleRadius1 - circleRadius2;
            circleRadius1 = circleRadius1 - circleRadius2;
        }
        Chunk circle1 = createCircleWithText(circleRadius1 + 4, circleRadius1, circleRadius1, currColor);
        Chunk circle2 = createCircleWithText(circleRadius2 + 4, circleRadius2, circleRadius2, priorColor);
        Phrase combCircle = new Phrase();
        combCircle.add(circle1);
        combCircle.add(circle2);
        contentCell.addElement(combCircle);
        contentCell.setBorder(Rectangle.NO_BORDER);


        Paragraph measureVal = new Paragraph();
        contentCell.addElement(measureVal);

        measureVal = new Paragraph();
//        measureVal.add(Chunk.NEWLINE);
        measureVal.add("\u00a0");
        measureVal.add("\u00a0");
        measureVal.add(formatMeasure(measCurrVal, (fontSize - 3), Font.BOLD, currColor));
        measureVal.add("\u00a0");
        measureVal.add("\u00a0");
        measureVal.add("\u00a0");
        measureVal.add("\u00a0");
//        if(calCurrVal.intValue() < calPriorVal.intValue() ){
//            measureVal.add(formatMeasure(measCurrVal,(fontSize/2)+2,Font.NORMAL, new BaseColor(0, 149, 182)));
//        }else{
        measureVal.add(formatMeasure(measPriorVal, (fontSize - 3), Font.BOLD, priorColor));
//        }
        contentCell.addElement(measureVal);
        contentTable.addCell(contentCell);
        contentCell = new PdfPCell(contentTable);
        contentCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(contentCell);



        PdfPCell outerCell = new PdfPCell(headerTable);
        setouterCellProp(outerCell, tableNum);
        PdfPTable outerTable = oneTable.get(tableNum);
        outerTable.addCell(outerCell);
    }

    public Chunk createCircleWithText(int cCenterX, int cCenterY, int radius, BaseColor color) {
        Image img = null;
        Chunk imgChunk = null;
        PdfContentByte canvas = writer.getDirectContent();
        PdfTemplate template = canvas.createTemplate(cCenterX + radius, cCenterY + radius);
        template.setLineWidth(0.1f);
        template.circle(cCenterX, cCenterY, radius);
        template.setColorFill(color);
//        template.saveState();
//        template.beginText();
//        template.setColorStroke(BaseColor.WHITE);
//        float sinus=(float)Math.sin(Math.PI/60);
//        float cosin=(float)Math.cos(Math.PI/60);


        try {

//            template.setFontAndSize(BaseFont.createFont(),6);
//            template.setRGBColorStroke(0, 0, 0);
//            template.setTextMatrix(sinus,cosin,-sinus,cosin,(float)cCenterX,(float)cCenterY);
//            template.showText("llo");
//            template.endText();
//            template.restoreState();
//
            template.fill();
            template.stroke();
            img = Image.getInstance(template);
//            imgChunk=new Chunk(img, 0f, ~(Math.max(circleRadius1,circleRadius2)));
            imgChunk = new Chunk(img, 0f, ~((radius == Math.min(circleRadius1, circleRadius2)) ? Math.min(circleRadius1, circleRadius2) - 2 : Math.max(circleRadius1, circleRadius2) - 2));
        }
        catch (BadElementException ex) {
             logger.error("Exception: ", ex);
        }catch (Exception ex) {
             logger.error("Exception: ", ex);
        }        return imgChunk;
    }

    public void createMeasureType5(int tableNum) {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.addCell(drowHeader());
        PdfPTable contentTable = new PdfPTable(1);
        PdfPCell contentCell = new PdfPCell();
        if (calCurrVal.intValue() < calPriorVal.intValue()) {
            squreSide1 = squreSide1 + squreSide2;
            squreSide2 = squreSide1 - squreSide2;
            squreSide1 = squreSide1 - squreSide2;
        }
        Chunk currRect = createRoundedRectangle((squreSide1 / 2), (squreSide1 / 2), squreSide1, squreSide1, squreSide1, currColor);
        Chunk priorRect = createRoundedRectangle(squreSide2 / 2, (squreSide2 / 2), squreSide2, squreSide2, squreSide2, priorColor);
        Phrase combCircle = new Phrase();
        combCircle.add(currRect);
        combCircle.add(priorRect);
//        combCircle.add(Chunk.NEWLINE);
        contentCell.addElement(combCircle);
//        contentCell.setColspan(2);
        contentCell.setBorder(Rectangle.NO_BORDER);


        Paragraph measureVal = new Paragraph();
        measureVal.add("\u00a0");
        measureVal.add(formatMeasure(measCurrVal, (fontSize / 2) + 2, Font.NORMAL, currColor));

        measureVal.add("\u00a0");
        measureVal.add("\u00a0");
        measureVal.add("\u00a0");
        measureVal.add("\u00a0");

        measureVal.add(formatMeasure(measPriorVal, (fontSize / 2) + 2, Font.NORMAL, priorColor));
        contentCell.addElement(measureVal);

        contentTable.addCell(contentCell);
        contentCell = new PdfPCell(contentTable);
        contentCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(contentCell);



        PdfPCell outerCell = new PdfPCell(headerTable);
        setouterCellProp(outerCell, tableNum);
        PdfPTable outerTable = oneTable.get(tableNum);
        outerTable.addCell(outerCell);
    }

    public Chunk createRoundedRectangle(float cCenterX, float cCenterY, float width, float height, int radious, BaseColor color) {
        Chunk rectChunk = new Chunk();
        Image img = null;
        try {
            PdfContentByte canvas = writer.getDirectContent();
            PdfTemplate template = canvas.createTemplate(width, height);
            template.rectangle(5, 5, width, height);
//            template.roundRectangle(5,5, width,height,2);
            template.setLineWidth(0.1f);
            template.setColorFill(color);
            template.fillStroke();
            template.fill();
            template.stroke();
            img = Image.getInstance(template);
            rectChunk = new Chunk(img, 0f, ~((height == Math.max(squreSide1, squreSide2)) ? Math.min(squreSide1 / 2, squreSide2 / 2) + 3 : Math.max(squreSide1 / 2, squreSide2 / 2)));
        }
        catch (BadElementException ex) {
             logger.error("Exception: ", ex);
        }catch (Exception ex) {
             logger.error("Exception: ", ex);
        }        return rectChunk;
    }

    public Point CalculatePointOnCircle(Point center, double radius, double rotationAngle) {
        double angleRadions = rotationAngle * Math.PI / 180;
        double x = center.x + radius * Math.cos(angleRadions);
        double y = center.y + radius * Math.cos(angleRadions);
        return new Point((int) x, (int) y);

    }

    public Image getImageFromPath(String fPath) {
        Image image = null;
        File file = null;
        FileInputStream fis = null;
        byte[] bytes = null;
        PdfPTable table = null;
        PdfPCell cell = null;
        try {
            String path = request.getRealPath("/");

            file = new File(path + fPath);
            fis = new FileInputStream(file);
            bytes = new byte[fis.available()];
            while (-1 != fis.read(bytes)) {
            }
            image = Image.getInstance(bytes);
        } catch (IOException | BadElementException e) {
            logger.error("Exception: ", e);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return image;

    }

    public BigDecimal getCalCurrVal() {
        return calCurrVal;
    }

    public void setCalCurrVal(BigDecimal calCurrVal) {
        this.calCurrVal = calCurrVal;
    }

    public BigDecimal getCalPriorVal() {
        return calPriorVal;
    }

    public void setCalPriorVal(BigDecimal calPriorVal) {
        this.calPriorVal = calPriorVal;
    }

    public BigDecimal getCalChPer() {
        return calChPer;
    }

    public void setCalChPer(BigDecimal calChPer) {
        this.calChPer = calChPer;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public float getCurrWidth() {
        return currWidth;
    }

    public void setCurrWidth(float currWidth) {
        this.currWidth = currWidth;
    }

    public float getPriorWidth() {
        return priorWidth;
    }

    public void setPriorWidth(float priorWidth) {
        this.priorWidth = priorWidth;
    }

    public Phrase formatMeasure(String val, int fontsize, int fontStyle, BaseColor baseColor) {
//        Font font=new Font(FontFamily.TIMES_ROMAN, fontsize,fontStyle, baseColor);
        Phrase finalMeasure = new Phrase();
        if (val.contains(measureFmtType)) {
            val = val.replace(measureFmtType, "");
        }
        Chunk measChunk = new Chunk(prefixVal + " " + val, new Font(FontFamily.TIMES_ROMAN, fontsize, fontStyle, baseColor));
        Chunk formatChunk = new Chunk(formatVal, new Font(FontFamily.TIMES_ROMAN, fontsize - 2, fontStyle, baseColor));
        /*
         * formatChunk.setTextRise(-3);
         */ //to align text to downwards

        finalMeasure.add(measChunk);
        finalMeasure.add(formatChunk);
        return finalMeasure;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getRadius1() {
        return radius1;
    }

    public void setRadius1(int radius1) {
        this.radius1 = radius1;
    }

    public int getRadius2() {
        return radius2;
    }

    public void setRadius2(int radius2) {
        this.radius2 = radius2;
    }

    public void createTemplate(int table) {
        PdfPTable innerTable = new PdfPTable(1);
        PdfPCell innerCell = new PdfPCell(drowHeader());
        innerTable.addCell(innerCell);
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, fontSize, Font.NORMAL, BaseColor.GRAY.brighter());
        PdfPCell cell = new PdfPCell(new Phrase("Template", font));
        cell.setBorder(Rectangle.NO_BORDER);

        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        innerTable.addCell(cell);
        PdfPCell outerCell = new PdfPCell(innerTable);
        setouterCellProp(outerCell, table);
        PdfPTable localTable = oneTable.get(table);
        localTable.addCell(outerCell);
    }

    public void setInnerCellProperties(PdfPCell innerCell) {
        innerCell.setBorder(Rectangle.NO_BORDER);
        innerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        innerCell.setVerticalAlignment(Element.ALIGN_CENTER);
    }

    public float getRalativeWidth(int tabId) {
        float relativeWidth = 0f;
        float tableWidth = oneTable.get(tabId).getTotalWidth();
        int cols = oneTable.get(tabId).getNumberOfColumns();
        relativeWidth = (tableWidth * colSpan) / cols;
        return relativeWidth;
    }

    public float getRelativeHeight(int tabId) {
        float relativeHeight = 1f;
        float tableHeight = oneTable.get(tabId).getTotalHeight();
        relativeHeight = (tableHeight * viewletHeight * rowSpan) / oneViewHeight;

        return relativeHeight;
    }

    public void createOneVIewTrendGraph1(int table) {
        Image image = null;
        File file = null;
        FileInputStream fis = null;
        byte[] bytes = null;
        PdfPTable table1 = null;
        PdfPCell cell = null;
        String days = String.valueOf(trendDays);
        days = days + " d";
        try {
            table1 = oneTable.get(table);
            /*
             * file = new
             * File(System.getProperty("java.io.tmpdir")+"/"+viewletNum+"_img.png");
             * // // File file2 = new
             * File("/home/progen/Desktop/"+viewletNum+"img_.png"); //
             * BufferedImage convertImage = ImageIO.read(file); // int type =
             * convertImage.getType() == 0? BufferedImage.TYPE_INT_ARGB :
             * convertImage.getType(); // BufferedImage
             * resizeImagePng=resizeImage1(convertImage, type); //
             * ImageIO.write(resizeImagePng, "png", file); //
             * ImageIO.write(resizeImagePng, "png", file2); // File file1 = new
             * File(System.getProperty("java.io.tmpdir")+"/"+viewletNum+"_.txt");
             * /* fis = new FileInputStream(file); bytes = new
             * byte[fis.available()];
             *
             *
             * while (-1 != fis.read(bytes)) { }
             *
             * image = Image.getInstance(bytes);
             */

            java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("java.io.tmpdir") + File.separator + viewletNum + "_img.png");
            image = Image.getInstance(writer, awtImage, 0.8F);
            if (image != null) {

                PdfPTable innerTable = new PdfPTable(1);
//                    innerTable1.setWidths(new int[]{96,4});

                innerTable.addCell(drowHeader());

                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.NORMAL, BaseColor.BLACK);
                PdfPCell innerCell1 = new PdfPCell(new Phrase(days, font));
                innerCell1.setVerticalAlignment(Element.ALIGN_TOP);
                innerCell1.setHorizontalAlignment(Rectangle.ALIGN_RIGHT);
                innerCell1.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(innerCell1);

                PdfPCell innerCell = new PdfPCell(image);
                innerCell.setBorder(Rectangle.NO_BORDER);
                innerCell.setNoWrap(false);
                //innerTable.addCell(innerCell2);
                innerTable.addCell(innerCell);

                PdfPCell outerCell = new PdfPCell(innerTable);
                setouterCellProp(outerCell, table);

                PdfPTable outerTable = oneTable.get(table);
                outerTable.addCell(outerCell);
//        file1.delete();
            }
            //document.add(table);
        } catch (BadElementException | IOException e) {
            logger.error("Exception: ", e);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    private static BufferedImage resizeImage1(BufferedImage convertImage, int type) {
        BufferedImage resizedImage = new BufferedImage(100, 100, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(convertImage, 0, 0, 100, 100, null);
        g.dispose();

        return resizedImage;
    }

    public int getTrendDays() {
        return trendDays;
    }

    public void setTrendDays(int trendDays) {
        this.trendDays = trendDays;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public void addTableToDoc(PdfPTable ptable, PdfPCell pCell) {
        int lastCompletRow = ptable.getLastCompletedRowIndex();
//        ptable.
    }

    public float getPaperHeight() {
        return pagesize.getHeight();

    }

    public float getPaperWidth() {
        return pagesize.getWidth();
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
        if (pageType != null && pageType.equalsIgnoreCase("A4")) {
            pagesize = PageSize.A4;

        }
        if (pageType != null && pageType.equalsIgnoreCase("A4_Landascpe")) {
            pagesize = PageSize.A4.rotate();
            if (oneViewHeight > 800 && fitTo.equalsIgnoreCase("SinglePage")) {
                resizeImage = true;
            }
        }
    }

    public String getFitTo() {
        return fitTo;
    }

    public void setFitTo(String fitTo) {
        this.fitTo = fitTo;
    }

    public float getRelativeHeightForSinglePage() {
        float cellHeight = ((pagesize.getHeight() - 140) * viewletHeight) / oneViewHeight;
        return cellHeight;
    }

    public float getRelativeWidthForSinglePage(PdfPTable table) {
        float cellWidth = 0;
        int totalCols = table.getNumberOfColumns();
        cellWidth = ((pagesize.getWidth() / totalCols) * colSpan);
        return cellWidth;
    }

    public void createNotes(int table) {
        Set noteKeySet = getNoteMap().keySet();
        Iterator iter = noteKeySet.iterator();
        String noteWriter = "";
        ArrayList note = new ArrayList();

        PdfPTable innerTable = new PdfPTable(1);
        Font font;
        PdfPCell cell = new PdfPCell();
//        cell.setBorder(Rectangle.NO_BORDER);
        innerTable.addCell(drowHeader());
        StringBuffer content = new StringBuffer();
        if (getNoteMap().isEmpty()) {

            font = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.NORMAL, BaseColor.GRAY.brighter());
            cell = new PdfPCell(new Phrase("Notes", font));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        } else {
            while (iter.hasNext()) {
                noteWriter = iter.next().toString();
                note = (ArrayList) getNoteMap().get(noteWriter);
                content.append(note.get(i).toString()).append("(").append(noteWriter).append(")\n");
            }

            font = new Font(Font.FontFamily.TIMES_ROMAN, fontSize, Font.NORMAL, BaseColor.BLACK);
            cell = new PdfPCell(new Phrase(content.toString(), font));

        }
        cell.setFixedHeight(viewletHeight);
        cell.setBorder(Rectangle.NO_BORDER);
        PdfPTable localTable = oneTable.get(table);
        innerTable.addCell(cell);
//        cell.setBorder(Rectangle.NO_BORDER);
        PdfPCell outerCell = new PdfPCell(innerTable);
        setouterCellProp(outerCell, table);
        localTable.addCell(outerCell);
    }

    public HashMap<String, ArrayList> getNoteMap() {
        return noteMap;
    }

    public void setNoteMap(HashMap<String, ArrayList> noteMap) {
        this.noteMap = noteMap;
    }
}
