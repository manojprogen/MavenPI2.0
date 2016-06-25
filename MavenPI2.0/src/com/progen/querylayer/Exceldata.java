package com.progen.querylayer;

/**
 * @created Feb 15 2010
 *
 * @author Praveen Kumar .M
 */
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

public class Exceldata {

    public static Logger logger = Logger.getLogger(Exceldata.class);
    HashMap excelvalues = new HashMap();
    ArrayList hskeylist = new ArrayList();
    String hskey = "";
    String hsval = "";
    int k = 0;

    public HashMap holdExcelSheet(String filename) {

        HashMap excelmap = new HashMap();

        try {
//      String filename = "input.xls";
            String tablename = filename;
            ////////////////////.println("tablename\t" + tablename);

            WorkbookSettings ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));

            FileInputStream fs = null;

            fs = new FileInputStream(new File(filename));

            Workbook workbook = Workbook.getWorkbook(new File(filename), ws);

            workbook = Workbook.getWorkbook(fs, ws);

            Sheet s = workbook.getSheet(0);
            getHeadingFromXlsFile(s);
            //  Sheet s1  = workbook.getSheet(1);
//            readDataSheet(s);
            workbook.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
            ////////////////////.println("Exception caught in holdexcelsheet data method of excel data");
        }
        return excelmap;
    }

    private static void readDataSheet(Sheet s) {
        // Find the labeled cell from sheet
        LabelCell lc = s.findLabelCell("Date");
        ////////////////////.println(lc.getString());

        //gets the value of cell at specified column and row
        DateCell dc = (DateCell) s.getCell(0, 1);
        ////////////////////.println(dc.getDate());

        lc = s.findLabelCell("Add 2 cells");
        ////////////////////.println(lc.getString());

        NumberCell c = (NumberCell) s.getCell(4, 1);
        ////////////////////.println(c.getValue());

        c = (NumberCell) s.getCell(4, 2);
        ////////////////////.println(c.getValue());

        NumberFormulaCell nc = (NumberFormulaCell) s.getCell(4, 3);
        ////////////////////.println(nc.getValue());

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
                ////////////////////////////////////////////.println("Total Sheet Found:" + totalSheet);
                for (int j = 0; j < totalSheet; j++) {
                    ////////////////////////////////////////////.println("Sheet Name:" + workbook.getSheet(j).getName());
                }
            }

            //Getting Default Sheet i.e. 0
            s = workbook.getSheet(0);

            //Reading Individual Cell
            getHeadingFromXlsFile(s);

            //Total Total No Of Rows in Sheet, will return you no of rows that are occupied with some data
            rowCount = s.getRows();

            //Total Total No Of Columns in Sheet
            columnCount = s.getColumns();

            //Reading Individual Row Content
            for (int i = 0; i < rowCount; i++) {
                //Get Individual Row
                rowData = s.getRow(i);
                if (rowData[0].getContents().length() != 0) { // the first date column must not null
                    for (int j = 1; j < columnCount; j++) {
                        hskey = rowData[0].getContents() + ":" + s.getCell(j, 0).getContents();
                        hskeylist.add(hskey);
                        hsval = rowData[j].getContents();
                        excelvalues.put(hskey, hsval);
                    }
                }
            }

            workbook.close();
        } catch (IOException e) {
            logger.error("Exception:", e);
        } catch (BiffException e) {
            logger.error("Exception:", e);
        }
    }

    public void getHeadingFromXlsFile(Sheet sheet) {
        int columnCount = sheet.getColumns();
        for (int i = 0; i < columnCount; i++) {
            ////////////////////.println("columnnames:- \t" + sheet.getCell(i, 0).getContents());
        }
        ////////////////////.println("columncount\t"+columnCount);
    }
}
