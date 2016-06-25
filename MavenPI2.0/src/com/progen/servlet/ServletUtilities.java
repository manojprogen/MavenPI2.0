/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.servlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author arun
 */
public class ServletUtilities {

    public static String prefix = "PRG_";

    public static void createTempDir() {
        String tempDirName = System.getProperty("java.io.tmpdir");
        if (tempDirName == null) {
            throw new RuntimeException("Temporary directory system property "
                    + "(java.io.tmpdir) is null.");
        }

        // create the temporary directory if it doesn't exist
        File tempDir = new File(tempDirName);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
    }

    public static ServletWriterTransferObject createBufferedWriter(String fileName, String suffix) throws IOException {
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }

        ServletUtilities.createTempDir();
        File tempFile = File.createTempFile(fileName, suffix);
        FileOutputStream fos = new FileOutputStream(tempFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        ServletWriterTransferObject servletWriter = new ServletWriterTransferObject(tempFile.getName(), writer);
        return servletWriter;
    }

    public static ServletWriterTransferObject getBufferedWriterForAppend(String fileName) throws IOException {
        fileName = System.getProperty("java.io.tmpdir") + "/" + fileName;
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file, true);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        ServletWriterTransferObject servletWriter = new ServletWriterTransferObject(file.getName(), writer);
        return servletWriter;
    }

    public static ServletWriterTransferObject createFileOutputStream(String fileName, String suffix) throws IOException {
        if (!suffix.startsWith(".")) {
            suffix = "." + suffix;
        }

        ServletUtilities.createTempDir();
        File tempFile = File.createTempFile(fileName, suffix);
        FileOutputStream fos = new FileOutputStream(tempFile);
        ServletWriterTransferObject servletWriter = new ServletWriterTransferObject(tempFile.getName(), fos);
        return servletWriter;
    }

    public static Reader createBufferedReader(String fileName) throws IOException {
        fileName = System.getProperty("java.io.tmpdir") + "/" + fileName;
        FileReader fileReader = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        return reader;
    }

    public static void markFilesForDeletion(Iterable<String> fileNames, HttpSession session) {
        ServletFileDeleter fileDeleter = (ServletFileDeleter) session.getAttribute("Prg_Temp_File_Deleter");
        if (fileDeleter == null) {
            fileDeleter = new ServletFileDeleter();
        }
        session.setAttribute("Prg_Temp_File_Deleter", fileDeleter);

        for (String file : fileNames) {
            fileDeleter.addFile(file);
        }
    }

    public static void markFileForDeletion(String fileName, HttpSession session) {
        ArrayList<String> fileNameAsLst = new ArrayList<String>();
        fileNameAsLst.add(fileName);
        ServletUtilities.markFilesForDeletion(fileNameAsLst, session);

    }

    public static String makeAndStoreZip(Iterable<String> fileNames) throws IOException {
        String dir = System.getProperty("java.io.tmpdir") + "/";
        String filePath;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        FileReader fileReader;
        BufferedReader reader;
        char[] cbuf;
        byte[] bbuf;
        int toRead;
        int offset = 0;
        int len = 5196;
        int size = 0;

        for (String file : fileNames) {
            size = 0;
            filePath = dir + file;
            fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);
            zos.putNextEntry(new ZipEntry(file));
            do {
                cbuf = new char[len];
                toRead = reader.read(cbuf, offset, len);
                if (toRead == -1) {
                    zos.closeEntry();
                    break;
                } else {
                    bbuf = new String(cbuf).getBytes("UTF-8");
                    zos.write(bbuf, offset, bbuf.length);
                }
            } while (true);
        }
        zos.close();

        File tempFile = File.createTempFile("PRG_OFFLINE_REPORTS", ".zip");

        FileOutputStream zfos = new FileOutputStream(tempFile);
        zfos.write(bos.toByteArray());
        zfos.close();
        return tempFile.getName();

    }

    public static void downloadFile(String fileName, HttpServletResponse response, String mimeType) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);

        if (file.exists()) {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));

            //  Set HTTP headers

//            response.setHeader("Content-Length", String.valueOf(file.length()));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            response.setHeader("Last-Modified", sdf.format(new Date(file.lastModified())));
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            byte[] input = new byte[1024];
            boolean eof = false;
            while (!eof) {
                int length = bis.read(input);
                if (length == -1) {
                    eof = true;
                } else {
                    bos.write(input, 0, length);
                }
            }
            if (mimeType != null) {
                if (mimeType.equalsIgnoreCase("application/html")) {
                    response.setContentType("application/html");
                } else if (mimeType.equalsIgnoreCase("application/pdf")) {
                    response.setContentType("application/pdf");
                } else {
                    response.setContentType("application/xls");
                }
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                bos.flush();
                bis.close();
                bos.close();


//                response.setHeader("Content-Type", mimeType);
            }

        } else {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        return;
    }
}


/*
 *
 MIME TYPES for Office 2007
.docm,application/vnd.ms-word.document.macroEnabled.12
.docx,application/vnd.openxmlformats-officedocument.wordprocessingml.document
.dotm,application/vnd.ms-word.template.macroEnabled.12
.dotx,application/vnd.openxmlformats-officedocument.wordprocessingml.template
.potm,application/vnd.ms-powerpoint.template.macroEnabled.12
.potx,application/vnd.openxmlformats-officedocument.presentationml.template
.ppam,application/vnd.ms-powerpoint.addin.macroEnabled.12
.ppsm,application/vnd.ms-powerpoint.slideshow.macroEnabled.12
.ppsx,application/vnd.openxmlformats-officedocument.presentationml.slideshow
.pptm,application/vnd.ms-powerpoint.presentation.macroEnabled.12
.pptx,application/vnd.openxmlformats-officedocument.presentationml.presentation
.xlam,application/vnd.ms-excel.addin.macroEnabled.12
.xlsb,application/vnd.ms-excel.sheet.binary.macroEnabled.12
.xlsm,application/vnd.ms-excel.sheet.macroEnabled.12
.xlsx,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
.xltm,application/vnd.ms-excel.template.macroEnabled.12
.xltx,application/vnd.openxmlformats-officedocument.spreadsheetml.template
*/