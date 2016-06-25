package com.progen.querylayer;

/**
 *
 * @author Praveen Kumar.M
 */
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.*;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;
import utils.db.ProgenConnection;

public class DatabaseforExcel extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(DatabaseforExcel.class);

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("chechkExcelConnectionkey", "chechkExcelConnection");
        map.put("saveExcelConnectionkey", "saveExcelConnection");
        return map;
    }

    public ActionForward chechkExcelConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PrintWriter out = response.getWriter();

        String dsnname = request.getParameter("dsnname");
        ////////////////////.println("dsnname" + dsnname);

        Connection con = null;
        int i = 0;
        String status = "";
        try {
            ////////////////////.println("dsn" + dsnname);
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            con = DriverManager.getConnection("jdbc:odbc:" + dsnname + "");
        } catch (Exception e) {
            i = 1;
            logger.error("Exception:", e);
        }
        if (i == 0) {
            status = "Connection Successful";
        } else {
            status = "Connection Failed";
        }

        out.print(status);

        return null;
    }

    public ActionForward saveExcelConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        UploadExcelFormBean excelform = (UploadExcelFormBean) form;
        FileInputStream fs = null;

        FormFile file = excelform.getFile();
        ////////////////////.println("file" + file);
        String connname = excelform.getConnectionname();
        ////////////////////.println("connname\t" + connname);
        String dsnname = excelform.getDatasourcename();
        ////////////////////.println("dsnname\t" + dsnname);

        Connection con1 = ProgenConnection.getInstance().getConnection();
        Statement st = con1.createStatement();
        String query = "insert into prg_user_connections (CONNECTION_ID,CONNECTION_NAME,USER_NAME,PASSWORD,SERVER,SERVICE_ID,SERVICE_NAME,PORT,CREATED_BY,UPDATED_BY,DSN_NAME,DB_CONNECTION_TYPE,EXL_FILE_PATH) values(PRG_USER_CONNECTIONS_SEQ.nextval,'" + connname + "','','','','','','','','','" + dsnname + "','EXCEL','" + getServlet().getServletContext().getRealPath("/") + "" + file + "')";
        ////////////////////////////////////////////////////////////////////////////////////.println("Query is "+query);
        st.executeUpdate(query);
        st.close();
        con1.close();

        FileOutputStream fos = null;
        String fileName = file.toString();
        ////////////////////.println("filename\t" + fileName);
        byte[] fileData = file.getFileData();
        File tempFile = new File(getServlet().getServletContext().getRealPath("/") + fileName);// + ".xls");
        ////////////////////.println("tempfile\t" + tempFile);
        if (!tempFile.exists()) {
            tempFile.createNewFile();
            fos = new FileOutputStream(tempFile);
            fos.write(fileData);
//            replaceColumnNames(fs);
        } else {
            fos = new FileOutputStream(tempFile);
            fos.write(fileData);
//            replaceColumnNames(fs);
        }

        Exceldata xldata = new Exceldata();
        xldata.holdExcelSheet(tempFile.toString());

        return null;
    }

    public void replaceColumnNames(FileInputStream fs) throws WriteException {

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
            ////////////////////.println("fs"+fs);
            workbook = Workbook.getWorkbook(fs, ws);

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
            ////////////////////////////////////////////.println("Total Rows inside Sheet:" + s.getRows());
            rowCount = s.getRows();

            //Total Total No Of Columns in Sheet
            ////////////////////////////////////////////.println("Total Column inside Sheet:" + s.getColumns());
            columnCount = s.getColumns();
            // columnCount=columnCount-1;
            // ////////////////////////////////////////////.println(columnCount);

            //Reading Individual Row Content
          /*
             * for (int i = 0; i < rowCount; i++) { //Get Individual Row rowData
             * = s.getRow(i); if (rowData[0].getContents().length() != 0) { //
             * the first date column must not null for (int j = 0; j <
             * columnCount; j++) {
             * ////////////////////////////////////////////.println(s.getCell(j,0).getContents());
             *
             * // rowData[k].getContents()+""+rowData[0].getContents(); //
             * ////////////////////////////////////////////.println(rowData[i].getContents()+""+rowData[0].getContents());
             * //
             * ////////////////////////////////////////////.println(j+"\t"+s.getCell(j,
             * 4).getContents()+","+rowData[0].getContents());
             *
             *
             *
             * ////////////////////////////////////////////.println(hskey+"-./
             * "+hsval);
             *
             *
             * //
             * //////////////////////////////////////////.print(j+"\t"+rowData[j].getContents()
             * + "\t"); //
             * ////////////////////////////////////////////.println(String.valueOf(excelvalues.get(hskeylist.get(j-1))));
             * } // //////////////////////////////////////////.print(""+i); //
             * ////////////////////////////////////////////.println(""); }
            }
             */
            ////////////////////////////////////////////.println(" excelvalues after making "+excelvalues);

            workbook.close();
        } catch (IOException e) {
            logger.error("Exception:", e);
        } catch (BiffException e) {
            logger.error("Exception:", e);
        }


    }

    public void getHeadingFromXlsFile(Sheet sheet) throws WriteException {
        int columnCount = sheet.getColumns();
        for (int i = 0; i < columnCount; i++) {
            String clmnametoreplace = sheet.getCell(i, 0).getContents();//.replaceAll(" ", "_");
            if (clmnametoreplace.contains("-")) {
                clmnametoreplace = clmnametoreplace.replace("-", "_");
            } else if (clmnametoreplace.contains(" ")) {
                clmnametoreplace = clmnametoreplace.replace(" ", "_");
            }
            WritableFont times9pt = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat times = new WritableCellFormat(times9pt);
            ////////////////////.println("clmnametoreplace"+clmnametoreplace);
            Label label = new Label(i, 0, clmnametoreplace, times);
            WritableSheet wsheet = (WritableSheet) sheet;
            wsheet.addCell(label);

            ////////////////////////////////////////////.println("columnnames:- \t" + sheet.getCell(i, 0).getContents());
        }
    }
    /*
     * public void addLabel(WritableSheet sheet, int column, int row, String s)
     * throws WriteException, RowsExceededException { WritableFont times9pt =
     * new WritableFont(WritableFont.ARIAL, 10); WritableCellFormat times = new
     * WritableCellFormat(times9pt); Label label; label = new Label(column, row,
     * s, times); sheet.addCell((WritableCell) label);
    }
     */
}
