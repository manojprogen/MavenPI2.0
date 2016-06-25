package com.progen.querylayer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelTest {

    public static void main(String[] args) throws Exception {

        ArrayList sheetsList = new ArrayList();

        HashMap totalList = new HashMap();
        ExcelTest et = new ExcelTest();
        totalList = et.getDetails();
        //InputStream myxls = new FileInputStream("C:\\Documents and Settings\\Saurabh\\PRG_KPN_BASE_DATA.xls");
   /*
         * InputStream myxls = new FileInputStream("C:\\ExcelTables.xls");
         * HSSFWorkbook wb = new HSSFWorkbook(myxls);
         *
         * //HSSFSheet sheet = wb.getSheetAt(0); // first sheet //HSSFRow row =
         * sheet.getRow(0); // third row // HSSFCell cell = row.getCell(3); //
         * fourth cell //Iterator it = row.iterator(); /* while(it.hasNext()) {
         * it.next();
         *
         * }
         *
         * int noOfSheets = wb.getNumberOfSheets();
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("num
         * of sheets "+noOfSheets); for(int i=0;i<noOfSheets;i++) {
         *
         * ArrayList columnsList = new ArrayList(); HSSFSheet sheet =
         * wb.getSheetAt(i); sheetsList.add(wb.getSheetName(i)); HSSFRow row =
         * sheet.getRow(0); HSSFRow row1 = sheet.getRow(0); Iterator it =
         * row.cellIterator(); int j=0; while(it.hasNext()) {
         *
         * it.next();
         *
         * columnsList.add(row.getCell(j)); j++; }
         * totalList.put(wb.getSheetName(i), columnsList); columnsList=null;
         *
         * }
         *
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Tables
         * is--->"+sheetsList);
         *
         */



    }

    public HashMap getDetails() throws Exception {
        ArrayList sheetsList = new ArrayList();

        HashMap totalList = new HashMap();
        //InputStream myxls = new FileInputStream("C:\\Documents and Settings\\Saurabh\\PRG_KPN_BASE_DATA.xls");
        InputStream myxls = new FileInputStream("C:\\ExcelTables.xls");
        HSSFWorkbook wb = new HSSFWorkbook(myxls);

        //HSSFSheet sheet = wb.getSheetAt(0);       // first sheet
        //HSSFRow row     = sheet.getRow(0);        // third row
        // HSSFCell cell   = row.getCell(3);  // fourth cell
        //Iterator it = row.iterator();
       /*
         * while(it.hasNext()) { it.next();
         *
         * }
         */

        int noOfSheets = wb.getNumberOfSheets();

        for (int i = 0; i < noOfSheets; i++) {

            ArrayList columnsList = new ArrayList();
            HSSFSheet sheet = wb.getSheetAt(i);
            sheetsList.add(wb.getSheetName(i));
            HSSFRow row = sheet.getRow(0);
            HSSFRow row1 = sheet.getRow(0);
            Iterator it = row.cellIterator();
            int j = 0;
            while (it.hasNext()) {

                it.next();

                columnsList.add(row.getCell(j));
                j++;
            }
            totalList.put(wb.getSheetName(i), columnsList);
            columnsList = null;

        }

        return totalList;
    }
}
