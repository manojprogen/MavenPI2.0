package com.progen.xlupdateAction;

//import com.progen.updateFromExl.StaticConnection;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.Cell;
import jxl.Sheet;
import jxl.read.biff.BiffException;
import jxl.DateCell;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import utils.db.ProgenConnection;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.log4j.*;

public class XlupdateDAO extends PbDb {

    public static Logger logger = Logger.getLogger(XlupdateDAO.class);
    HashMap excelvalues = new HashMap();
    ArrayList hskeylist = new ArrayList();
    String hskey = "";
    String hsval = "";
    int k = 0;
    private WritableCellFormat timesBoldUnderline;
    //private WritableCellFormat times;
    private String inputFile = "c:/temp/Test1.xls";
    String tablename = "";
    String collName1 = "";
    String collName2 = "";
    PbReturnObject prgr;
    CellView cv;
    ConnectionResourceBundle resBundle = new ConnectionResourceBundle();

    public HashMap init(String filePath) {
        FileInputStream fs = null;
        HashMap all = new HashMap();
        try {
            fs = new FileInputStream(new File(filePath));
            contentReading(fs);
        } catch (IOException e) {
            logger.error("Exception: ", e);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                logger.error("Exception: ", e);
            }
        }
        all.put("excelvalues", excelvalues);
        all.put("hskeylist", hskeylist);
        return all;
    }

    //Returns the Headings used inside the excel sheet
    public void getHeadingFromXlsFile(Sheet sheet) {
        int columnCount = sheet.getColumns();

    }

    public void contentReading(InputStream fileInputStream) throws Exception {
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


            //Getting Default Sheet i.e. 0
            s = workbook.getSheet(0);

            //Reading Individual Cell
            getHeadingFromXlsFile(s);

            //Total Total No Of Rows in Sheet, will return you no of rows that are occupied with some data
            ////////////////////////////////////////////////////.println.println("Total Rows inside Sheet:" + s.getRows());
            rowCount = s.getRows();

            //Total Total No Of Columns in Sheet
            ////////////////////////////////////////////////////.println.println("Total Column inside Sheet:" + s.getColumns());
            columnCount = s.getColumns();
//           ProgenConnection pgcon = new ProgenConnection();
            Connection con = ProgenConnection.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rsltset = null;
            PbDb pbdb = new PbDb();
            PbReturnObject pbretobj = new PbReturnObject();
            String finalQuery = "select COLLECTION_NAME,PRODUCT_NAME from PRG_KNP_COMP_DIM1";
            pbretobj = pbdb.execSelectSQL(finalQuery, con);
        } catch (IOException e) {
            logger.error("Exception: ", e);
        } catch (BiffException e) {
            logger.error("Exception: ", e);
        }
        //  printHashmap();
    }

    public void Downloadexl(String[] userList) throws IOException, WriteException, Exception {


        File file = new File(inputFile);
        tablename = userList[2];
        collName1 = userList[0];
        collName2 = userList[1];
        String getDetailsQuery = resBundle.getString("Downloadexl");
        String finalQuery = buildQuery(getDetailsQuery, userList);
        PbDb pbdb = new PbDb();
        Connection con = ProgenConnection.getInstance().getCustomerConn();
        prgr = pbdb.execSelectSQL(finalQuery, con);

        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));


        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createLabel(excelSheet);
        createContent(excelSheet);

        workbook.write();
        workbook.close();
    }

    public void createLabel(WritableSheet sheet)
            throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        WritableCellFormat times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);

        // Create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setSize(250 * 40);
        //Autosize(Boolean.TRUE);


        // Write a few headers
        // tablename ="ALERT_DETAils";

        addCaption(sheet, 0, 0, tablename);
        addCaption(sheet, 0, 1, collName1);
        addCaption(sheet, 1, 1, collName2);
        addCaption(sheet, 3, 1, "New_COLLECTION_NAME(ModifyArea)");
        addCaption(sheet, 4, 1, "New_PRODUCT_NAME(ModifyArea)");

    }

    public void createContent(WritableSheet sheet) throws WriteException,
            RowsExceededException {
        // Write a few number
        WritableFont times9pt = new WritableFont(WritableFont.ARIAL, 10);
        WritableCellFormat times = new WritableCellFormat(times9pt);
        times.setWrap(true);
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        for (int i = 0; i < prgr.getRowCount(); i++) {
            // First column
            sheet.setColumnView(i, cv);
            String temp1 = (String) prgr.getFieldValue(i, "COLLECTION_NAME");
            String temp2 = (String) prgr.getFieldValue(i, "PRODUCT_NAME");
            addCaption(sheet, 0, 3 + i, temp1);
            // Second column
            addCaption(sheet, 1, 3 + i, temp2);
            addCaption(sheet, 3, 3 + i, temp1);
            addCaption(sheet, 4, 3 + i, temp2);


        }
        // Lets calculate the sum of it
		/*
         * StringBuffer buf = new StringBuffer(); buf.append("SUM(A2:A10)");
         * Formula f = new Formula(0, 10, buf.toString()); sheet.addCell(f); buf
         * = new StringBuffer(); buf.append("SUM(B2:B10)"); f = new Formula(1,
         * 10, buf.toString());
        sheet.addCell(f);
         */

        // Now a bit of text
		/*
         * for (int i = 12; i < 20; i++) { // First column addLabel(sheet, 0, i,
         * "Boring text " + i); // Second column addLabel(sheet, 1, i, "Another
         * text");
        }
         */
    }

    public void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    public void addNumber(WritableSheet sheet, int column, int row,
            Integer integer) throws WriteException, RowsExceededException {
        WritableFont times9pt = new WritableFont(WritableFont.ARIAL, 10);
        WritableCellFormat times = new WritableCellFormat(times9pt);
        Number number;
        number = new Number(column, row, integer, times);
        sheet.addCell(number);
    }

    public void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        WritableFont times9pt = new WritableFont(WritableFont.ARIAL, 10);
        WritableCellFormat times = new WritableCellFormat(times9pt);
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

//    public static void main(String[] args) throws WriteException, IOException, Exception {
//        XlupdateDAO test = new XlupdateDAO();
//        String tablename = "PRG_KPN_MASTER_DATA";
//        String Col_name1 = "PRG_KPN_MASTER_DATA";
//        String Col_name2 = "COLLECTION_NAME";
//        String Col_name3 = "PRODUCT_NAME";
//        String[] userList = new String[3];
//
//        userList[0] = Col_name2;
//        userList[1] = Col_name3;
//        userList[2] = Col_name1;
//        // userList[3]= Col_name1;
//        test.Downloadexl(userList);
//        //test.putFile("c:/temp/sample1.xls");
//        //test.write();
//    }
    public PbReturnObject getColumns() throws Exception {

        String tablename1 = "PRG_KNP_COMP_DIM1";
        String getColumnsquery = resBundle.getString("getColumns");
        Object[] obj = new Object[1];
        obj[0] = tablename1;
        String query = buildQuery(getColumnsquery, obj);
        PbDb pbdb = new PbDb();
        Connection con = ProgenConnection.getInstance().getCustomerConn();
        prgr = pbdb.execSelectSQL(query, con);

        return prgr;
    }

    public PbReturnObject getColumnsvalues(String selectval) throws Exception {
        String getColumnsvaluesquery = resBundle.getString("getColumnsvalues");


        Object[] obj = new Object[1];
        obj[0] = selectval;

        String query = buildQuery(getColumnsvaluesquery, obj);
        PbDb pbdb = new PbDb();
        Connection con = ProgenConnection.getInstance().getCustomerConn();
        prgr = pbdb.execSelectSQL(query, con);

        return prgr;

    }

    String upDateColumns(String tableName, String colName, String[] userlist1, String[] userlist2) throws Exception {
        ArrayList querylist = new ArrayList();
        String upDateColumnsquery = resBundle.getString("upDateColumns");
        PbDb pbdb = new PbDb();
        Connection con = ProgenConnection.getInstance().getCustomerConn();
        Object[] obj = new Object[5];
        obj[0] = tableName;
        obj[1] = colName;
        String query = "";
        String result = "";
        try {
            for (int i = 0; i < userlist1.length; i++) {

                obj[2] = userlist2[i];
                obj[3] = colName;
                obj[4] = userlist1[i];
                query = buildQuery(upDateColumnsquery, obj);
                querylist.add(query);

            }
            pbdb.executeMultiple(querylist, con);
            result = "";
            return result;
        } catch (Exception ex) {

            logger.error("Exception: ", ex);
            result = "Error";
            return result;
        }


    }
}
