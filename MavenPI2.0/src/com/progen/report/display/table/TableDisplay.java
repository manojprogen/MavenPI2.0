/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.table;

import com.lowagie.text.pdf.PdfPTable;
import com.progen.report.data.TableBuilder;
import java.io.Writer;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author progen
 */
public abstract class TableDisplay {

    protected TableDisplay next;
    protected TableBuilder tableBldr;
    protected Writer out;
    protected StringBuilder parentHtml;
    protected final static int BATCH_SIZE = 5000;
    protected XSSFSheet sheet;
    protected XSSFWorkbook workbook;
    protected HSSFWorkbook hWorkbook;
    protected HSSFSheet hSheet;
    public String delimiter;
    public String textIdentifier;
    public String headlineflag;
    public String fromOneviewflag;
    public WritableWorkbook writableWorkbook;
    public WritableSheet writableSheet;
    public PdfPTable pdfTable;
    public float cellHeight;
    protected int startRow = 0;

    public TableDisplay setNext(TableDisplay display) {
        this.next = display;
        this.next.setWriter(this.out);
        return display;
    }

    public StringBuilder generateOutputHTML() {
        StringBuilder html = new StringBuilder();
        html = this.generateHTML();
        if (this.next != null) {
            this.next.setParentHtml(html);
            html = this.next.generateOutputHTML();
        }
        return html;
    }

    protected abstract StringBuilder generateHTML();

    protected abstract void setParentHtml(StringBuilder parentHtml);

    public TableDisplay(TableBuilder builder) {
        this.tableBldr = builder;
        this.parentHtml = new StringBuilder();

    }

    public void setWriter(Writer out) {
        this.out = out;
    }

    public XSSFSheet getSheet() {
        return sheet;
    }

    public HSSFSheet getHSheet() {
        return hSheet;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public void setHSheet(HSSFSheet nSheet) {
        this.hSheet = nSheet;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public HSSFWorkbook getHWorkbook() {
        return hWorkbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public void setHWorkbook(HSSFWorkbook nWorkbook) {
        this.hWorkbook = nWorkbook;
    }

    //  abstract void setDelimiter(String delimiter);
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    // abstract void setTextIdentifier(String textIdentifier);
    public void setTextIdentifier(String textIdentifier) {
        this.textIdentifier = textIdentifier;
    }

    public void setHeadlineflag(String Headlineflag) {
        this.headlineflag = Headlineflag;
    }

    public void setFromOneviewflag(String fromOneviewflag) {
        this.fromOneviewflag = fromOneviewflag;
    }

    /**
     * @return the writableWorkbook
     */
    public WritableWorkbook getWritableWorkbook() {
        return writableWorkbook;
    }

    /**
     * @param writableWorkbook the writableWorkbook to set
     */
    public void setWritableWorkbook(WritableWorkbook writableWorkbook) {
        this.writableWorkbook = writableWorkbook;
    }

    /**
     * @return the writableSheet
     */
    public WritableSheet getWritableSheet() {
        return writableSheet;
    }

    /**
     * @param writableSheet the writableSheet to set
     */
    public void setWritableSheet(WritableSheet writableSheet) {
        this.writableSheet = writableSheet;
    }

    /**
     * @return the pdfTable
     */
    public PdfPTable getPdfTable() {
        return pdfTable;
    }

    /**
     * @param pdfTable the pdfTable to set
     */
    public void setPdfTable(PdfPTable pdfTable) {
        this.pdfTable = pdfTable;
    }

    /**
     * @return the cellHeight
     */
    public float getCellHeight() {
        return cellHeight;
    }

    /**
     * @param cellHeight the cellHeight to set
     */
    public void setCellHeight(float cellHeight) {
        this.cellHeight = cellHeight;
    }

    public void setStartRow(int startRowCount) {
        this.startRow = startRowCount;
    }

    public int getSatartRow() {
        return this.startRow;
    }
}
