package utils.db;

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
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println("Cell
         * value is " + cell.getStringCellValue());
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("it.next
         * is "+it.next()); }
         *
         * int noOfSheets = wb.getNumberOfSheets();
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("num
         * of sheets "+noOfSheets); for(int i=0;i<noOfSheets;i++) {
         *
         * ArrayList columnsList = new ArrayList(); HSSFSheet sheet =
         * wb.getSheetAt(i); sheetsList.add(wb.getSheetName(i)); HSSFRow row =
         * sheet.getRow(0); HSSFRow row1 = sheet.getRow(0); Iterator it =
         * row.cellIterator(); int j=0; while(it.hasNext()) {
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("loop-->"+j);
         * it.next();
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("i-->"+row.getCell(j)+"<><><>
         * "); columnsList.add(row.getCell(j)); j++; }
         * totalList.put(wb.getSheetName(i), columnsList); columnsList=null; //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("wb."+wb.getSheetAt(i).getRow(0));
         * }
         *
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("Tables
         * is--->"+sheetsList);
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println("Columns
         * is--->"+columnsList);
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("TotalList
         * is "+totalList);
         */

        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("TotalList is "+totalList);

    }

    public HashMap getDetails() throws Exception {
        ArrayList sheetsList = new ArrayList();

        HashMap totalList = new HashMap();
        //InputStream myxls = new FileInputStream("C:\\Documents and Settings\\Saurabh\\PRG_KPN_BASE_DATA.xls");
        InputStream myxls = new FileInputStream("C:\\Sample.xls");
        HSSFWorkbook wb = new HSSFWorkbook(myxls);

        //HSSFSheet sheet = wb.getSheetAt(0);       // first sheet
        //HSSFRow row     = sheet.getRow(0);        // third row
        // HSSFCell cell   = row.getCell(3);  // fourth cell
        //Iterator it = row.iterator();
       /*
         * while(it.hasNext()) { it.next();
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println("Cell
         * value is " + cell.getStringCellValue());
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("it.next
         * is "+it.next());
        }
         */

        int noOfSheets = wb.getNumberOfSheets();
        ////////////////////////////////////////////////////////////////////////////////////////////////.println("num of sheets "+noOfSheets);
        for (int i = 0; i < noOfSheets; i++) {

            ArrayList columnsList = new ArrayList();
            HSSFSheet sheet = wb.getSheetAt(i);
            sheetsList.add(wb.getSheetName(i));
            HSSFRow row = sheet.getRow(0);
            HSSFRow row1 = sheet.getRow(0);
            Iterator it = row.cellIterator();
            int j = 0;
            while (it.hasNext()) {
                ////////////////////////////////////////////////////////////////////////////////////////////////.println("loop-->"+j);
                it.next();
                ////////////////////////////////////////////////////////////////////////////////////////////////.println("i-->"+row.getCell(j)+"<><><> ");
                columnsList.add(row.getCell(j));
                j++;
            }
            totalList.put(wb.getSheetName(i), columnsList);
            columnsList = null;
            ////////////////////////////////////////////////////////////////////////////////////////////////.println("wb."+wb.getSheetAt(i).getRow(0));
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////.println("Tables is--->"+sheetsList);
        //////////////////////////////////////////////////////////////////////////////////////////////////.println("Columns is--->"+columnsList);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("TotalList is "+totalList);


        return totalList;
    }
}
