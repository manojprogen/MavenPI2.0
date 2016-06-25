/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.servlet;

import com.lowagie.text.pdf.PdfWriter;
import java.io.OutputStream;
import java.io.Writer;

/**
 *
 * @author arun
 */
public class ServletWriterTransferObject {

    public Writer writer;
    public String fileName;
    public String reportName;
    public OutputStream outputStream;
    public PdfWriter pdfWriter;

    public ServletWriterTransferObject(String fileName, Writer writer) {
        this.writer = writer;
        this.fileName = fileName;
    }

    public ServletWriterTransferObject(String fileName, OutputStream outputStream) {
        this.outputStream = outputStream;
        this.fileName = fileName;
    }

    public ServletWriterTransferObject(String fileName, PdfWriter pdfWriter) {
        this.fileName = fileName;
        this.pdfWriter = pdfWriter;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
