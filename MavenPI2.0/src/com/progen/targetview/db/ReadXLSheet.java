package com.progen.targetview.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import jxl.*;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

public class ReadXLSheet {

    public static Logger logger = Logger.getLogger(ReadXLSheet.class);
    HashMap excelvalues = new HashMap();
    ArrayList hskeylist = new ArrayList();
    String hskey = "";
    String hsval = "";
    int k = 0;

    public HashMap init(String filePath) {
        FileInputStream fs = null;
        HashMap all = new HashMap();
        try {
            fs = new FileInputStream(new File(filePath));
            contentReading(fs);
        } catch (IOException e) {
            logger.error("Exception:", e);
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                logger.error("Exception:", e);
            }
        }
        all.put("excelvalues", excelvalues);
        all.put("hskeylist", hskeylist);
        return all;
    }

    //Returns the Headings used inside the excel sheet
    public void getHeadingFromXlsFile(Sheet sheet) {
        int columnCount = sheet.getColumns();
        for (int i = 0; i < columnCount; i++) {
            ////////////////////////////////////////////////////////////////////////.println("columnnames:- \t" + sheet.getCell(i, 0).getContents());
        }
    }

    public void contentReading(InputStream fileInputStream) {
        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        Cell rowData[] = null;
        int rowCount = '0';
        int columnCount = '0';
        DateCell dc = null;
        int totalSheet = 0;

        try {
            ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = Workbook.getWorkbook(fileInputStream, ws);

            totalSheet = workbook.getNumberOfSheets();
            if (totalSheet > 0) {
                ////////////////////////////////////////////////////////////////////////.println("Total Sheet Found:" + totalSheet);
                for (int j = 0; j < totalSheet; j++) {
                    ////////////////////////////////////////////////////////////////////////.println("Sheet Name:" + workbook.getSheet(j).getName());
                }
            }

            //Getting Default Sheet i.e. 0
            s = workbook.getSheet(0);

            //Reading Individual Cell
            getHeadingFromXlsFile(s);

            //Total Total No Of Rows in Sheet, will return you no of rows that are occupied with some data
            ////////////////////////////////////////////////////////////////////////.println("Total Rows inside Sheet:" + s.getRows());
            rowCount = s.getRows();

            //Total Total No Of Columns in Sheet
            ////////////////////////////////////////////////////////////////////////.println("Total Column inside Sheet:" + s.getColumns());
            columnCount = s.getColumns();
            // columnCount=columnCount-1;
            // ////////////////////////////////////////////////////////////////////////.println(columnCount);

            //Reading Individual Row Content
            for (int i = 5; i < rowCount; i++) {
                //Get Individual Row
                rowData = s.getRow(i);
                if (rowData[0].getContents().length() != 0) { // the first date column must not null
                    for (int j = 1; j < columnCount; j++) {
                        ////////////////////////////////////////////////////////////////////////.println(s.getCell(j,0).getContents());
                        hskey = rowData[0].getContents() + ":" + s.getCell(j, 5).getContents();
                        // rowData[k].getContents()+""+rowData[0].getContents();
                        // ////////////////////////////////////////////////////////////////////////.println(rowData[i].getContents()+""+rowData[0].getContents());
                        // ////////////////////////////////////////////////////////////////////////.println(j+"\t"+s.getCell(j, 4).getContents()+","+rowData[0].getContents());
                        hskeylist.add(hskey);
                        hsval = rowData[j].getContents();

                        ////////////////////////////////////////////////////////////////////////.println(hskey+"-./ "+hsval);

                        excelvalues.put(hskey, hsval);
                        // //////////////////////////////////////////////////////////////////////.print(j+"\t"+rowData[j].getContents() + "\t");
                        // ////////////////////////////////////////////////////////////////////////.println(String.valueOf(excelvalues.get(hskeylist.get(j-1))));
                    }
                    // //////////////////////////////////////////////////////////////////////.print(""+i);
                    // ////////////////////////////////////////////////////////////////////////.println("");
                }
            }
            ////////////////////////////////////////////////////////////////////////.println(" excelvalues after making "+excelvalues);

            workbook.close();
        } catch (IOException e) {
            logger.error("Exception:", e);
        } catch (BiffException e) {
            logger.error("Exception:", e);
        }
        printHashmap();
    }

    public void printHashmap() {
//        ////////////////////////////////////////////////////////////////////////.println(excelvalues);
        ////////////////////////////////////////////////////////////////////////.println(" excelvalues... "+excelvalues);
        for (int i = 0; i < excelvalues.size(); i++) {
            ////////////////////////////////////////////////////////////////////////.println(hskeylist.get(0));
            ////////////////////////////////////////////////////////////////////////.println(excelvalues.get(i));
            for (int j = 0; j < hskeylist.size(); j++) {
                //////////////////////////////////////////////////////////////////////////.println(String.valueOf(excelvalues.get(hskeylist.get(j))));
            }
        }
    }

    public static void main(String[] args) {
        try {
            ReadXLSheet xlReader = new ReadXLSheet(); //Documents and Settings"+"\\"+"Saurabh"+"\\"+"Desktop\\
            xlReader.init("C:/Documents and Settings/Saurabh/Desktop/Target-22(2).xls");
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
}
