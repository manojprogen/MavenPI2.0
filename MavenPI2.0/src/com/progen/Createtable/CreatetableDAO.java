package com.progen.Createtable;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.progen.contypes.GetConnectionType;
import com.progen.report.DashletDetail;
import com.progen.report.KPIElement;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.entities.KPI;
import com.progen.report.kpi.KPISingleGroupHelper;
import com.progen.report.pbDashboardCollection;
import com.progen.report.query.PbTimeRanges;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import com.progen.userlayer.db.UserLayerDAO;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import jxl.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.*;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.xml.sax.SAXException;
import prg.business.group.BusinessGroupDAO;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class CreatetableDAO extends PbDb {

    private static HttpSession session = null;
    public static Logger logger = Logger.getLogger(CreatetableDAO.class);

    public static HttpSession getSession() {
        return session;
    }

    public static void setSession(HttpSession aSession) {
        session = aSession;
    }
    private String inputFile = " ";
    CreateTableResourceBundle resBundle = new CreateTableResourceBundle();

    public ArrayList readExcel(File tempFile, int sheetNum) throws SQLException {
        CreatetableDAO test = new CreatetableDAO();
        ArrayList alist = test.contentReading(tempFile, sheetNum);
        return alist;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFile() {
        return this.inputFile;
    }

    public ArrayList contentReading(File tempFile, int sheetNum) throws SQLException {
        FileInputStream fileInputStream = null;
        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        int columnCount = '0';
        int totalSheet = -1;
        ArrayList Alist = null;
        String sheetName = null;
        ArrayList colNamesList = new ArrayList();
        ArrayList colTypesList = new ArrayList();
        String[] columnNames = null;
        String[] columntypes = null;
        String query = "";
        String SelectQusery = "";
        Object[] obj = new Object[1];
        PbReturnObject prgr = null;
        Connection con = null;

        try {
            fileInputStream = new FileInputStream(tempFile);
            ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = Workbook.getWorkbook(fileInputStream, ws);
            totalSheet = workbook.getNumberOfSheets();
            if (totalSheet > 0 && sheetNum != -1 && (sheetNum <= (totalSheet - 1))) {
                Alist = new ArrayList();
                s = workbook.getSheet(sheetNum);
                columnCount = s.getColumns();
                sheetName = workbook.getSheet(sheetNum).getName();
                sheetName.toUpperCase();
                obj[0] = sheetName.toUpperCase();
                prgr = new PbReturnObject();
                con = ProgenConnection.getInstance().getCustomerConn();
                query = "select column_name, data_type, data_length from user_tab_columns where table_name = '&'";
                SelectQusery = buildQuery(query, obj);
                prgr = execSelectSQL(SelectQusery, con);
                for (int i = 0; i < columnCount; i++) {
                    Cell cell1 = s.getCell(i, 1);
                    CellType type = cell1.getType();
                    String contents = null;
                    contents = s.getCell(i, 0).getContents();
                    contents = contents.trim();
                    contents = contents.replaceAll(" ", "");

                    if (!contents.equalsIgnoreCase(" ")) {
                        colNamesList.add(s.getCell(i, 0).getContents().replace(" ", "_").replace("-", "_"));
                        colTypesList.add(type.toString());
                    }
                    cell1 = null;
                    type = null;
                }
                columnNames = (String[]) colNamesList.toArray(new String[0]);
                columntypes = (String[]) colTypesList.toArray(new String[0]);
                Alist.add(columnNames);
                Alist.add(columntypes);
                Alist.add(sheetName);
                if (sheetNum == (totalSheet - 1)) {
                    Alist.add(sheetNum + 1);
                } else {
                    Alist.add(-1);
                    if (tempFile.exists()) {
                        // boolean fileDeleted = tempFile.delete();
                    }
                }
                Alist.add(prgr);
                Alist.add(totalSheet);
            } else {
                Alist = null;
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        con.close();
        return Alist;
    }

    public ArrayList createTable(String sheetName, String[] columnNames, String[] columnTypes, String filepath) throws Exception {

        return null;
    }

    public void insertData(String[] cNames, String sheetName) throws IOException, Exception {

        ArrayList qryLsit = new ArrayList();

        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        int columnCount = '0';
        int totalSheet = 0;
        boolean fileDeleted = false;
        File inputWorkbook = new File(getInputFile());
        inputWorkbook.createNewFile();
        int qryCount = 0;
        try {
            Connection con = ProgenConnection.getInstance().getCustomerConn();
            ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = Workbook.getWorkbook(inputWorkbook);
            totalSheet = workbook.getNumberOfSheets();
            s = workbook.getSheet(0);
            columnCount = s.getColumns();

            for (int j = 1; j < s.getRows(); j++) {
                StringBuffer queryBuffer = new StringBuffer("");
                queryBuffer.append("INSERT  INTO ");
                queryBuffer.append(sheetName.replace(" ", "_"));
                queryBuffer.append("(");
                StringBuffer innerqueryBuffer = new StringBuffer("");
                for (int i = 0; i < cNames.length; i++) {
                    if (!cNames[i].equalsIgnoreCase(" ")) {
                        innerqueryBuffer.append("," + cNames[i]);
                    }
                    if (i == (cNames.length - 1)) {
                        queryBuffer.append(innerqueryBuffer.substring(1));
                    }
                }
                queryBuffer.append(")");
                queryBuffer.append("VALUES");
                queryBuffer.append("(");
                for (int i = 0; i < s.getColumns(); i++) {

                    if (!cNames[i].equalsIgnoreCase(" ")) {
                        Cell cell = s.getCell(i, j);
                        CellType type = cell.getType();
                        if (type.equals(CellType.NUMBER)) {
                            queryBuffer.append(s.getCell(i, j).getContents());
                        } else if (type.equals(CellType.DATE)) {
                            queryBuffer.append("(to_Date('" + s.getCell(i, j).getContents() + "','dd mm yyyy hh24:MI:ss'))");

                        } else {
                            queryBuffer.append("'" + s.getCell(i, j).getContents().replace("'", "'||chr(39)||'").replace("&", "'||chr(38)||'") + "'");
                        }
                        if (i < (s.getColumns() - 1)) {
                            queryBuffer.append(",");

                        }
                    }

                }

                queryBuffer.append(")");
                qryLsit.add(queryBuffer.toString());

                qryCount++;
                if (qryCount == 500) {
                    executeMultiple(qryLsit, con);
                    qryCount = 0;
                }

            }
            if (qryCount > 0) {
                executeMultiple(qryLsit, con);
            }
            con.close();

        } catch (IOException e) {
            logger.error("Exception:", e);

        } catch (BiffException e) {
            logger.error("Exception:", e);

        }
    }

    void cancelcreatetable(String filepath) {
        boolean fileDeleted = false;
        File tempFile = new File(filepath);
        if (tempFile.exists()) {
            // fileDeleted = tempFile.delete();
        }
    }

    public ArrayList alterTableinDB(ArrayList paramsAlterTableinDB) throws Exception {
        String filePath = (String) paramsAlterTableinDB.get(0);
        String[] alterColmNames = (String[]) paramsAlterTableinDB.get(1);
        String[] renameColnames = (String[]) paramsAlterTableinDB.get(2);
        int sheetNum = Integer.parseInt(paramsAlterTableinDB.get(3).toString());
        String sheetName = (String) paramsAlterTableinDB.get(4);
//        HashMap dispColnames=(HashMap) paramsAlterTableinDB.get(5);
        //String filePath, String[] alterColmNames, String[] renameColnames, int sheetNum, String sheetName
        FileInputStream fileInputStream = null;
        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        ArrayList Alist = new ArrayList();
        boolean chekck1 = false;
        ArrayList checkArrayList = new ArrayList();

        String query = "";
        String SelectQusery = "";
        Object[] obj = new Object[1];
        PbReturnObject prgr = null;
        ArrayList qryLsit1 = new ArrayList();
        Connection con = ProgenConnection.getInstance().getCustomerConn();
        obj[0] = sheetName.toUpperCase();
        prgr = new PbReturnObject();
        con = ProgenConnection.getInstance().getCustomerConn();
        query = "select * from &";
        SelectQusery = buildQuery(query, obj);
        // ////////
        qryLsit1.add(SelectQusery);
        //  boolean chekck = executeMultiple(qryLsit1, con);
        // //////
        String AcTableName = sheetName.trim();
        checkArrayList.add(AcTableName);
        String afterAddSeqTabName = "";
        PbDb pbdb = new PbDb();

        try {
            int seqtableName = pbdb.getSequenceNumber("select TABLE_NAME_SEQ.nextval from dual");
            sheetName = "P_" + seqtableName + sheetName.trim();
            afterAddSeqTabName = sheetName;
            checkArrayList.add(afterAddSeqTabName);
            if (sheetName.length() > 20) {

                sheetName = sheetName.substring(0, 19);

            }
            checkArrayList.add(sheetName);
            //
            fileInputStream = new FileInputStream(filePath);
            ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = Workbook.getWorkbook(fileInputStream, ws);
            //  //////
            s = workbook.getSheet(sheetNum - 1);
            StringBuffer queryBuffer = new StringBuffer("");
            queryBuffer.append("create table ");
            queryBuffer.append("" + sheetName.replace(" ", "_").replace("-", "_") + " ");
            queryBuffer.append("(");
            for (int i = 0; i < renameColnames.length; i++) {
                for (int k = 0; k < s.getColumns(); k++) {
                    if (alterColmNames[i].trim().equalsIgnoreCase(s.getCell(k, 0).getContents().toString().trim().replace(" ", "_").replace("-", "_"))) {
                        Cell cell1 = s.getCell(k, 1);
                        CellType type = cell1.getType();
                        queryBuffer.append("  " + renameColnames[i].trim().replace(" ", "_").replace("-", "_") + "");
                        if (type.toString().equalsIgnoreCase("Number")) {
                            queryBuffer.append(" NUMBER  (30,0) ");
                        } else if (type.toString().equalsIgnoreCase("Label")) {
                            queryBuffer.append(" VARCHAR2(4000 BYTE) ");
                        } else if (type.toString().equalsIgnoreCase("Date")) {
                            queryBuffer.append(" DATE ");
                        } else {
                            queryBuffer.append(" VARCHAR2(4000 BYTE) ");
                        }
                    }
                }
                if (i < alterColmNames.length - 1) {
                    queryBuffer.append(" ,");
                }
                String contents = null;
                contents = s.getCell(i, 0).getContents();
                contents = contents.trim();
                contents = contents.replaceAll(" ", "");
            }
            queryBuffer.append(" )");
            Alist.add(queryBuffer.toString());
            chekck1 = executeMultiple(Alist, con);
            //
            checkArrayList.add(chekck1);
            con.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        CreatetableDAO test = new CreatetableDAO();
        test.setInputFile(filePath);
        test.alertinsertData(alterColmNames, sheetName, sheetNum - 1);
        return checkArrayList;
    }

    public void alertinsertData(String[] alterColmNames, String sheetName, int sheetNum) throws IOException, BiffException, Exception {
        //
        int qryCount = 0;
        ArrayList qryLsit = new ArrayList();
        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        Connection con = ProgenConnection.getInstance().getCustomerConn();
        ws = new WorkbookSettings();
        ws.setLocale(new Locale("en", "EN"));
        File inputWorkbook = new File(getInputFile());
        inputWorkbook.createNewFile();
        workbook = Workbook.getWorkbook(inputWorkbook);
        int totalSheet = workbook.getNumberOfSheets();
        s = workbook.getSheet(sheetNum);
        for (int row = 1; row < s.getRows(); row++) {
            StringBuffer queryBuffer = new StringBuffer("");
            queryBuffer.append("INSERT INTO ");
            queryBuffer.append(" " + sheetName + " ");
            queryBuffer.append("(");
            for (int colcheck = 0; colcheck < alterColmNames.length; colcheck++) {
                queryBuffer.append(alterColmNames[colcheck].trim().replace(" ", "_").toUpperCase());
                if (colcheck < alterColmNames.length - 1) {
                    queryBuffer.append(",");
                }
            }
            queryBuffer.append(")");
            queryBuffer.append(" VALUES ");
            queryBuffer.append("(");
            for (int i = 0; i < alterColmNames.length; i++) {
                for (int shcol = 0; shcol < s.getColumns(); shcol++) {
                    if (alterColmNames[i].trim().equalsIgnoreCase(s.getCell(shcol, 0).getContents().toString().trim().replace(" ", "_").replace("-", "_"))) {
                        Cell cell1 = s.getCell(shcol, 1);
                        CellType type = cell1.getType();

                        if (type.toString().equalsIgnoreCase("Number")) {

                            queryBuffer.append(s.getCell(shcol, row).getContents().toString());

                        } else if (type.toString().equalsIgnoreCase("Date")) {
                            queryBuffer.append("(to_Date('" + s.getCell(shcol, row).getContents() + "','dd MM yyyy hh24:MI:ss'))");
                            // queryBuffer.append("'" + s.getCell(shcol, row).getContents() + "'");

                        } else {
                            queryBuffer.append("'" + s.getCell(shcol, row).getContents().replace("'", "'||chr(39)||'").replace("&", "'||chr(38)||'") + "'");
                        }

                    }

                }
                if (i < alterColmNames.length - 1) {
                    queryBuffer.append(",");


                }

            }
            queryBuffer.append(")");
            qryLsit.add(queryBuffer.toString());
            qryCount++;
            if (qryCount == 500) {
                executeMultiple(qryLsit, con);
                //   //////
                qryCount = 0;
                qryLsit = new ArrayList();
            }
        }
        if (qryCount > 0) {
            executeMultiple(qryLsit, con);
            //   //////
        }
        con.close();
    }

    public String saveTableDetails(ArrayList paramArrlist) throws Exception {
        //String[] dimTableNames, String[] dimColumnnames, String sheetName, String valradio, String fileName
        ArrayList insertparms = new ArrayList();
        String[] dimTableNames = (String[]) paramArrlist.get(0);
        String[] dimColumnnames = (String[]) paramArrlist.get(1);
        String sheetName = (String) paramArrlist.get(2);
        String valradio = (String) paramArrlist.get(3);
        String fileName = (String) paramArrlist.get(4);
        String tableNAme = (String) paramArrlist.get(5);
        String[] tabAllnames = tableNAme.split(",");
        ArrayList userGvenNames = new ArrayList();
        sheetName = tabAllnames[2];
        userGvenNames.add(tabAllnames[0].trim());
        //  //////
        ArrayList query1 = new ArrayList();
        PbReturnObject tabList = new PbReturnObject();
        String query = "select column_name, data_type from user_tab_cols where table_name='" + sheetName.toString().trim().toUpperCase() + "'";
        //////
        Connection con = ProgenConnection.getInstance().getCustomerConn();
        Connection connection = null;
        tabList = execSelectSQL(query, con);
        con.close();
        ArrayList coNames = new ArrayList();
        ArrayList coType = new ArrayList();
        String[] tabCol = null;
        String qrystat = "";
        for (int i = 0; i < tabList.getRowCount(); i++) {
            coNames.add(tabList.getFieldValue(i, "COLUMN_NAME"));
            coType.add(tabList.getFieldValue(i, "DATA_TYPE"));

        }
        //////
        String queryForSEQ = "";
        PbDb pbdb = new PbDb();
        String[] actDimName = new String[dimTableNames.length];
        try {

            for (int tacount = 0; tacount < dimTableNames.length; tacount++) {
                userGvenNames.add(dimTableNames[tacount].trim().replace(" ", "_"));
                int tabSequence = pbdb.getSequenceNumber("select TABLE_NAME_SEQ.nextval from dual");
                String tempdimName = "P_" + tabSequence + dimTableNames[tacount].trim().replace(" ", "_");
                actDimName[tacount] = tempdimName;
                if (tempdimName.length() > 20) {
                    tempdimName = tempdimName.substring(0, 19);
                }
                dimTableNames[tacount] = tempdimName;
                StringBuffer queryBuffer = queryBuffer = new StringBuffer("");
                queryBuffer.append("create table ");
                queryBuffer.append(dimTableNames[tacount].trim().toUpperCase());
                queryBuffer.append("( ");
                queryBuffer.append(dimTableNames[tacount].trim().replace(" ", "_") + "_ID  NUMBER(30,0) NOT NULL ENABLE,");
                PbReturnObject selcol = null;
                if (dimColumnnames[tacount].contains("~")) {
                    tabCol = dimColumnnames[tacount].split("~");
                } else {
                    tabCol = dimColumnnames[tacount].split("~");
                }


                for (int j = 0; j < tabCol.length; j++) {
                    logger.info("tabCol: \t" + tabCol[j].trim().toUpperCase());
                    if (coNames.contains(tabCol[j].trim().toUpperCase().replace(" ", "_"))) {
                        int index = coNames.indexOf(tabCol[j].trim().toUpperCase().replace(" ", "_"));
                        if (coType.get(index).toString().equalsIgnoreCase("VARCHAR2")) {
                            queryBuffer.append(tabCol[j].toString().trim().replace(" ", "_") + " VARCHAR2(4000 BYTE)");
                        } else if (coType.get(index).toString().equalsIgnoreCase("NUMBER")) {

                            queryBuffer.append(tabCol[j].toString().trim().replace(" ", "_") + " NUMBER(30,0)");
                        } else {

                            queryBuffer.append(tabCol[j].trim().replace(" ", "_") + " DATE");
                        }
                    }
                    if (j < tabCol.length - 1) {
                        queryBuffer.append(",");
                    }
                }
                queryBuffer.append(")");
                //  //////
                queryForSEQ = "CREATE SEQUENCE  " + dimTableNames[tacount].trim() + "_SEQ MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE ";
                //   //////

                query1.add(queryBuffer);
                query1.add(queryForSEQ);

            }
            connection = ProgenConnection.getInstance().getCustomerConn();
            //  //////

            boolean qrystatus = executeMultiple(query1, connection);
            logger.info("query1::\t" + query1);
            qrystat = String.valueOf(qrystatus);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        CreatetableDAO createdao = new CreatetableDAO();
        insertparms.add(dimTableNames);
        insertparms.add(dimColumnnames);
        insertparms.add(sheetName);
        insertparms.add(valradio);
        insertparms.add(fileName);
        insertparms.add(tabAllnames[1]);
        createdao.insertDimData(insertparms, userGvenNames);
        connection.close();
        return qrystat;
    }

    public boolean CheckDSNname(String dsnName) throws SQLException {

        Connection connection = null;
        boolean check = false;
        try {
            // //////
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connection = DriverManager.getConnection("jdbc:odbc:" + dsnName + "");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("Select * from [orders$]");
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            check = true;
        } catch (Exception e) {
            logger.error("Exception:", e);
            check = false;
        }
        connection.close();
        return check;
    }

    private void insertDimData(ArrayList insertparms, ArrayList userGvenNames) throws Exception {

        String[] dimTableNames = (String[]) insertparms.get(0);
        String[] dimColumnnames = (String[]) insertparms.get(1);
        String sheetName = (String) insertparms.get(2);
        String valradio = (String) insertparms.get(3);
        String fileName = (String) insertparms.get(4);
        String actTabname = (String) insertparms.get(5);
        //String[] dimTableNames, String[] dimColumnnames, String sheetName, String valradio, String fileName
        //
        // //////
        ArrayList query1 = new ArrayList();
        ArrayList queryForMD = new ArrayList();
        PbReturnObject pbretSelcol = null;
        String query = "";
        Connection connection = null;
        HashMap hmDimDetails = new HashMap();
        StringBuffer sbinsert = null;
        String inseDbMaster = "";
        int conn_id = 0;
        String selctTabDet = "";
        PbReturnObject tabDetails = null;
        String[] shNameAndDim = new String[dimTableNames.length + 1];
        shNameAndDim[0] = sheetName.toUpperCase();
        String[] strdimColName = null;
        String queryForrelatiMas = "";
        HashMap tabIDshm = null;
        String finalUploQuery = "";
        String queryForSeq = "";
        PbReturnObject forSeq = null;
        String insertIntoTabmast = resBundle.getString("insertIntoTabmast");
        int NumberOfCol = 0;
        Object[] measTabdet = new Object[6];
        String uploadRelationMaster = resBundle.getString("uploadRelationMaster");
        for (int sname = 1; sname < shNameAndDim.length; sname++) {
            shNameAndDim[sname] = dimTableNames[sname - 1];

        }
        String uploadQuery = resBundle.getString("uploadQuery");
        PbDb pbdb = new PbDb();
        String SelMetDetQuery = resBundle.getString("SelMetDetQuery");
        String insertMetDetails = resBundle.getString("insertMetDetails");

        try {
            for (int dimCount = 0; dimCount < dimTableNames.length; dimCount++) {
                hmDimDetails.put(dimTableNames[dimCount].trim(), dimColumnnames[dimCount].replace("~", ",").replace(" ", "_").replace("~", ","));
                query = "select DISTINCT " + hmDimDetails.get(dimTableNames[dimCount].trim()) + " FROM " + sheetName;
                //////
                //pbretSelcol = new PbReturnObject();
                connection = ProgenConnection.getInstance().getCustomerConn();
                //////
                // pbretSelcol = execSelectSQL(query, connection);

                // connection = null;
                sbinsert = new StringBuffer("");
                sbinsert.append("INSERT INTO  ");
                sbinsert.append(dimTableNames[dimCount].trim());
                sbinsert.append(" (");
                sbinsert.append(dimTableNames[dimCount].trim() + "_id ," + hmDimDetails.get(dimTableNames[dimCount].trim()));
                sbinsert.append(" )");
                sbinsert.append(" SELECT " + dimTableNames[dimCount].trim() + "_seq.nextval ," + hmDimDetails.get(dimTableNames[dimCount].trim()));
                sbinsert.append(" FROM");
                sbinsert.append("( SELECT DISTINCT " + hmDimDetails.get(dimTableNames[dimCount].trim()));
                sbinsert.append(" FROM " + sheetName + ")");
                query1.add(sbinsert.toString());
                logger.info("query for insert in dim: \t" + query1);

            }
            executeMultiple(query1, connection);
            tabIDshm = new HashMap();
            //connection = ProgenConnection.getCustomerConn();
//            if (query1.size() < 200) {
//                executeMultiple(query1, connection);
            String SQLQuery = "SELECT PRG_USER_CONNECTIONS_SEQ.nextval from dual";
            PbReturnObject userconnec = execSelectSQL(SQLQuery);
            conn_id = userconnec.getFieldValueInt(0, 0);
            connection.close();

            //  String queryForconn = "insert into PRG_USER_CONNECTIONS values(" + conn_id + ",'PRG_" + sheetName.toUpperCase().trim() + "','prprogen','prprogen','192.168.0.104','ORCL','',1521,'','','','ORACLE','')";
            String queryForconn = "insert into PRG_USER_CONNECTIONS values(" + conn_id + ",'PRG_" + sheetName.toUpperCase().trim() + "','dataus','dataus','localhost','XE','',1521,'','','','ORACLE','')";

            queryForMD.add(queryForconn);
            //uploadQuery
            int seqUPMaMaster = pbdb.getSequenceNumber("select PRG_UPLOAD_MASTER_SEQ.nextval from dual");
            Object[] uploObject = new Object[5];
            uploObject[0] = seqUPMaMaster;
            uploObject[1] = null;//userid
            uploObject[2] = sheetName.trim();
            uploObject[3] = conn_id;
            uploObject[4] = sheetName.trim();
            finalUploQuery = buildQuery(uploadQuery, uploObject);
            queryForMD.add(finalUploQuery);
            queryForSeq = " select PRG_DATABASE_MASTER_SEQ.nextval from dual ";
            forSeq = execSelectSQL(queryForSeq);
            tabIDshm.put(sheetName.toUpperCase().trim(), forSeq.getFieldValue(0, 0));
            inseDbMaster = "insert into PRG_DB_MASTER_TABLE values (" + conn_id + ","
                    + "'" + sheetName.toUpperCase().trim() + "'," + tabIDshm.get(sheetName.toUpperCase().trim()) + ",'" + sheetName.toUpperCase().trim() + "','" + sheetName.toUpperCase().trim() + "','Table','','','',sysdate,'' )";
            //
            queryForMD.add(inseDbMaster);
            for (int mc = 0; mc < dimTableNames.length; mc++) {

                forSeq = execSelectSQL(queryForSeq);
                tabIDshm.put(dimTableNames[mc].trim().toUpperCase(), forSeq.getFieldValue(0, 0));
                inseDbMaster = "insert into PRG_DB_MASTER_TABLE values (" + conn_id + ","
                        + "'" + dimTableNames[mc].toUpperCase().trim() + "'," + tabIDshm.get(dimTableNames[mc].toUpperCase().trim()) + ",'" + dimTableNames[mc].toUpperCase().trim() + "','" + dimTableNames[mc].toUpperCase().trim() + "','Table','','','',sysdate,'')";
                //
                queryForMD.add(inseDbMaster);
            }
            for (int taDet = 0; taDet < shNameAndDim.length; taDet++) {
                selctTabDet = "SELECT DISTINCT COLUMN_NAME, data_type, data_length FROM ALL_TAB_COLUMNS  WHERE table_name='" + shNameAndDim[taDet].toUpperCase().trim() + "'";
                tabDetails = execSelectSQL(selctTabDet);
                for (int reO = 0; reO < tabDetails.getRowCount(); reO++)//loop for insert values into PRG_DB_MASTER_TABLE_DETAILSin metadata
                {
                    StringBuffer SBquery = new StringBuffer();
                    SBquery.append("insert into PRG_DB_MASTER_TABLE_DETAILS values(PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,(select table_id FROM PRG_DB_MASTER_TABLE WHERE TABLE_ALIAS = '" + shNameAndDim[taDet].toUpperCase().trim() + "'),'" + tabDetails.getFieldValueString(reO, "COLUMN_NAME") + "','" + tabDetails.getFieldValueString(reO, "COLUMN_NAME") + "','" + tabDetails.getFieldValueString(reO, "DATA_TYPE") + "'," + tabDetails.getFieldValueString(reO, "DATA_LENGTH") + " ,'N','Y')");
                    queryForMD.add(SBquery);
                }
            }
            StringBuffer innerSBquery = new StringBuffer();
            queryForrelatiMas = resBundle.getString("queryForrelatiMas");
            Object[] obj1 = new Object[3];
            Object[] obj2 = new Object[4];
            int k = 0;

            for (String dim : dimColumnnames) {
                int seqUPRelMaster = pbdb.getSequenceNumber("select PRG_UPLOAD_REL_MASTER_SEQ.nextval from dual");
                obj1[0] = shNameAndDim[0].toUpperCase().trim();
                obj1[1] = dimTableNames[k].toUpperCase().trim();
                obj2[0] = seqUPRelMaster;
                obj2[1] = obj1[0];
                obj2[2] = obj1[1];
                strdimColName = dim.split("~");

                for (int stri = 0; stri < strdimColName.length; stri++) {
                    String dimeName = dimTableNames[k].trim().toUpperCase();
                    innerSBquery.append(shNameAndDim[0].toUpperCase().trim() + "." + strdimColName[stri].replace(" ", "_").trim() + "=" + dimTableNames[k].toUpperCase().trim() + "." + strdimColName[stri].replace(" ", "_").trim());
                    if (stri < strdimColName.length - 1) {
                        innerSBquery.append(" AND ");
                    }
                }
                //

                obj1[2] = innerSBquery.toString();
                obj2[3] = obj1[2];
                String finalQuery = buildQuery(queryForrelatiMas, obj1);
                String UploaRelMasQurery = buildQuery(uploadRelationMaster, obj2);
                //
                innerSBquery = new StringBuffer();
                queryForMD.add(finalQuery);
                queryForMD.add(UploaRelMasQurery);
                k++;
            }


            executeMultiple(queryForMD);
            //

            queryForMD = new ArrayList();
            int countDimCol = 0;
            Object[] scolumnObj = new Object[2];
            Object[] inseInMASDetObj = new Object[7];
            for (String namesStr : shNameAndDim) {
                int seqUPRelMaster = pbdb.getSequenceNumber("select PRG_TAB_MAPPING_MASTER_SEQ.nextval from dual");
                measTabdet[0] = seqUPRelMaster;
                measTabdet[1] = "";
                measTabdet[2] = sheetName.trim();
                measTabdet[3] = sheetName.trim();

                if (namesStr.trim().equalsIgnoreCase(sheetName.trim())) {
                    String queryForColcount = " SELECT COUNT(c.COLUMN_NAME) FROM user_tab_columns c  WHERE table_name='" + sheetName.toUpperCase().trim() + "'";
                    connection = ProgenConnection.getInstance().getCustomerConn();
                    PbReturnObject countReturn = execSelectSQL(queryForColcount, connection);
                    connection.close();
                    measTabdet[4] = countReturn.getFieldValue(0, 0);
                    measTabdet[5] = 'Y';
                } else {
                    //
                    String[] colnames = dimColumnnames[countDimCol].split("~");
                    measTabdet[4] = colnames.length;
                    measTabdet[5] = 'N';
                    countDimCol++;
                }
                finalUploQuery = buildQuery(insertIntoTabmast, measTabdet);
                queryForMD.add(finalUploQuery);
                scolumnObj[0] = conn_id;
                scolumnObj[1] = namesStr.toUpperCase().trim();
                String finalSelQuery = buildQuery(SelMetDetQuery, scolumnObj);
                PbReturnObject prgrDet = execSelectSQL(finalSelQuery);
                inseInMASDetObj[6] = tabIDshm.get(namesStr.toUpperCase().trim());
                //
                String selectQuery = "select DISTINCT WORKBOOK_NAME from PRG_TAB_MAP_DETAILS where WORKBOOK_NAME='" + fileName + "'";
                //
                PbReturnObject resultSql = execSelectSQL(selectQuery);
                if (resultSql.getRowCount() != 0) {
                    String deleteQuery = "delete PRG_TAB_MAP_DETAILS WHERE  WORKBOOK_NAME='" + fileName + "'";
                    execModifySQL(deleteQuery);

                }
                for (int prgcount = 0; prgcount < prgrDet.getRowCount(); prgcount++) {
                    int seqMasterDetailSqe = pbdb.getSequenceNumber("select PRG_TAB_MAP_DETAILS_SEQ.nextval from dual");
                    inseInMASDetObj[0] = seqMasterDetailSqe;
                    inseInMASDetObj[1] = seqUPRelMaster;
                    inseInMASDetObj[2] = prgrDet.getFieldValue(prgcount, "TABLE_COL_NAME");
                    inseInMASDetObj[3] = prgrDet.getFieldValue(prgcount, "COL_TYPE");
                    inseInMASDetObj[4] = "";
                    inseInMASDetObj[5] = fileName;

                    finalUploQuery = buildQuery(insertMetDetails, inseInMASDetObj);
                    queryForMD.add(finalUploQuery);
                }



            }

            executeMultiple(queryForMD);
            //



            CreatetableDAO createdao = new CreatetableDAO();
            ArrayList inserRelparams = new ArrayList();
            inserRelparams.add(tabIDshm);
            inserRelparams.add(sheetName);
            inserRelparams.add(dimTableNames);
            inserRelparams.add(hmDimDetails);
            inserRelparams.add(valradio);
            inserRelparams.add(userGvenNames);
            //userGvenNames
            createdao.insertRltDetailsMD(inserRelparams, conn_id);


        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    private void insertRltDetailsMD(ArrayList inserRelparams, int conn_id) throws Exception {

        // HashMap tabIDshm, String sheetName, String[] dimTableNames, int conn_id, HashMap hmDimDetails, String valradio
        HashMap tabIDshm = (HashMap) inserRelparams.get(0);
        String sheetName = (String) inserRelparams.get(1);
        String[] dimTableNames = (String[]) inserRelparams.get(2);
        HashMap hmDimDetails = (HashMap) inserRelparams.get(3);
        String valradio = (String) inserRelparams.get(4);
        ArrayList userGvenNames = (ArrayList) inserRelparams.get(5);
        ArrayList queryList = new ArrayList();
        String kesSEtvals = tabIDshm.keySet().toString();
        String[] kesSEArray = kesSEtvals.split(",");

        ArrayList temp = null;
        HashMap colDetails = new HashMap();
        HashMap factHM = new HashMap();
        Set set = tabIDshm.entrySet();
        PbReturnObject prgr = null;
        Iterator i = set.iterator();
        Map.Entry me = null;
        Object[] obj = new Object[7];
        Object[] objUploadRel = new Object[8];
        ArrayList colOfFactTab = null;
        String insInRlDet = resBundle.getString("insertRltDetailsMD");
        String uploadRelDetails = resBundle.getString("uploadRelDetails");
        String finalQuery = "";
        try {
            while (i.hasNext()) {
                me = (Map.Entry) i.next();
                //System.out.print(me.getKey() + ": ");
                //
                String query = "select COLUMN_ID,TABLE_COL_NAME from PRG_DB_MASTER_TABLE_DETAILS where table_id =" + me.getValue() + "";
                prgr = execSelectSQL(query);
                temp = new ArrayList();
                for (int j = 0; j < prgr.getRowCount(); j++) {
                    temp.add(prgr.getFieldValue(j, "COLUMN_ID") + "~" + prgr.getFieldValue(j, "TABLE_COL_NAME"));

                }
                colDetails.put(me.getKey(), temp);
            }
            colOfFactTab = new ArrayList();
            colOfFactTab = (ArrayList) colDetails.get(sheetName.toUpperCase().trim());
            for (int faccount = 0; faccount < colOfFactTab.size(); faccount++) {
                String[] tempstrAr = colOfFactTab.get(faccount).toString().split("~");
                factHM.put(tempstrAr[1], tempstrAr[0]);

            }
            for (int k = 0; k < dimTableNames.length; k++) {

                obj[0] = tabIDshm.get(sheetName.toString().toUpperCase().trim());
                objUploadRel[1] = obj[0];
                obj[1] = tabIDshm.get(dimTableNames[k].toString().toUpperCase().trim());
                objUploadRel[2] = obj[1];
                obj[2] = tabIDshm.get(sheetName.toString().toUpperCase().trim());
                objUploadRel[3] = obj[2];
                ArrayList templistcol = new ArrayList();
                templistcol = (ArrayList) colDetails.get(dimTableNames[k].toString().toUpperCase().trim());
                for (int liscon = 0; liscon < templistcol.size(); liscon++) {
                    String[] tempstAr = templistcol.get(liscon).toString().split("~");
                    if (factHM.containsKey(tempstAr[1])) {
                        int seqUPRelDetails = getSequenceNumber("select PRG_UPLOAD_REL_DETAILS_SEQ.nextval from dual");
                        objUploadRel[0] = seqUPRelDetails;
                        obj[3] = factHM.get(tempstAr[1]);
                        objUploadRel[4] = obj[3];
                        obj[4] = tabIDshm.get(dimTableNames[k].trim().toString().toUpperCase());
                        objUploadRel[5] = obj[4];
                        obj[5] = tempstAr[0];
                        objUploadRel[6] = obj[5];
                        obj[6] = sheetName.toUpperCase() + "." + tempstAr[1].toUpperCase().trim() + "=" + dimTableNames[k].toUpperCase().trim() + "." + tempstAr[1].toUpperCase().trim();
                        objUploadRel[7] = obj[6];
                        finalQuery = buildQuery(insInRlDet, obj);
                        String uploadRelDetQuery = buildQuery(uploadRelDetails, objUploadRel);
                        //
                        queryList.add(finalQuery);
                        queryList.add(uploadRelDetQuery);

                    }
                }
            }
            //
            executeMultiple(queryList);
            CreatetableDAO createdao = new CreatetableDAO();
            //userGvenNames
            ArrayList queryLayerParams = new ArrayList();
            queryLayerParams.add(dimTableNames);
            queryLayerParams.add(sheetName);
            queryLayerParams.add(tabIDshm);
            queryLayerParams.add(colDetails);
            queryLayerParams.add(factHM);
            queryLayerParams.add(hmDimDetails);
            queryLayerParams.add(valradio);
            queryLayerParams.add(userGvenNames);
            //dimTableNames ,sheetName, tabIDshm, colDetails, factHM, conn_id, hmDimDetails, valradio
            createdao.queryLayer(queryLayerParams, conn_id);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    private void queryLayer(ArrayList queryLayerParams, int conn_id) throws Exception {
//String[] dimTableNames, String sheetName, HashMap tabIDshm, HashMap colDetails, HashMap factHM, int conn_id, HashMap hmDimDetails, String valradio
        String[] dimTableNames = (String[]) queryLayerParams.get(0);
        String sheetName = (String) queryLayerParams.get(1);
        HashMap tabIDshm = (HashMap) queryLayerParams.get(2);
        HashMap colDetails = (HashMap) queryLayerParams.get(3);
        HashMap factHM = (HashMap) queryLayerParams.get(4);
        HashMap hmDimDetails = (HashMap) queryLayerParams.get(5);
        String valradio = (String) queryLayerParams.get(6);
        ArrayList userGvenNames = (ArrayList) queryLayerParams.get(7);
        ArrayList dimQueryList = new ArrayList();
        ArrayList memberList = new ArrayList();
        String queryFordim = resBundle.getString("queryLayer");
        String finalQuery = "";
        Object[] diObj = new Object[4];
        PbReturnObject pbret = new PbReturnObject();
        String selectseqQuery = "";
        String dimTableQuery = "";
        Object[] dimObjeTab = new Object[2];
        dimTableQuery = resBundle.getString("insertDimTable");
        String dimTabDetailsQuery = "";
        String inInDimMember = resBundle.getString("insertDimMember");
        String memberDetailQuery = resBundle.getString("memberDetailQuery");
        Object[] dimTabDetObj = null;
        String dimTabDetQuery = "";
        Object[] memberObject = null;
        Object[] memberDetails = new Object[4];
        String finalQueryformember = "";
        String insertIntoRelMas = resBundle.getString("insertIntoRelMas");
        String selectMemAndRelDetails = resBundle.getString("selectMemAndRelDetails");
        Object[] relObj = new Object[3];
        Object[] relDetaiObj = new Object[3];
        String insertIntoRElD = resBundle.getString("insertIntoRElD");
        Object[] selObj = new Object[1];
        String dimids = "";
        String[] dimentionsIDS = new String[dimTableNames.length];
        try {
            for (int dicount = 0; dicount < dimTableNames.length; dicount++) {
                selectseqQuery = "select PRG_QRY_DIMENSIONS_SEQ.nextval from dual";
                pbret = execSelectSQL(selectseqQuery);
                dimentionsIDS[dicount] = pbret.getFieldValue(0, 0).toString();
                diObj[0] = pbret.getFieldValue(0, 0);
                diObj[1] = userGvenNames.get(1 + dicount).toString().toUpperCase().trim();
                diObj[2] = dimTableNames[dicount].toUpperCase().trim();
                diObj[3] = conn_id;
                finalQuery = buildQuery(queryFordim, diObj);
                //
                dimQueryList.add(finalQuery);
                //dimObjeTab = new Object[2];
                dimObjeTab[0] = pbret.getFieldValue(0, 0);
                dimObjeTab[1] = tabIDshm.get(dimTableNames[dicount].toUpperCase().trim());
                // String dimtable = "insert into PRG_QRY_DIM_TABLES VALUES(PRG_QRY_DIM_TABLES_SEQ.nextval," + pbret.getFieldValue(0, 0) + "," + tabIDshm.get(dimTableNames[dicount].trim().toUpperCase()) + ")";
                String finaDimQuery = buildQuery(dimTableQuery, dimObjeTab);
                //
                dimQueryList.add(finaDimQuery);
                dimTabDetObj = new Object[2];

                ArrayList templistcol = new ArrayList();
                templistcol = (ArrayList) colDetails.get(dimTableNames[dicount].toString().toUpperCase().trim());
                dimTabDetailsQuery = resBundle.getString("inseDimTABDetails");
                //
                for (int liscon = 0; liscon < templistcol.size(); liscon++) {

                    String[] tempstAr = templistcol.get(liscon).toString().split("~");
                    dimTabDetObj[0] = tabIDshm.get(dimTableNames[dicount].toUpperCase().trim());
                    dimTabDetObj[1] = tempstAr[0].trim();

                    finalQuery = buildQuery(dimTabDetailsQuery, dimTabDetObj);
                    //
                    dimQueryList.add(finalQuery);

                }
                //
                executeMultiple(dimQueryList);

                dimQueryList = new ArrayList();
                String dimcolStr = (String) hmDimDetails.get(dimTableNames[dicount].trim());
                String[] dimColumns = dimcolStr.split(",");
                dimTabDetQuery = resBundle.getString("dimTabDetQuery");
                finalQuery = buildQuery(dimTabDetQuery, dimObjeTab);
                PbReturnObject prdimdetails = execSelectSQL(finalQuery);
                //
                //
                memberObject = new Object[6];
                for (int dimdet = 0; dimdet < dimColumns.length; dimdet++) {
                    memberObject[0] = dimColumns[dimdet];
                    memberObject[1] = prdimdetails.getFieldValue(0, "DIM_ID");
                    memberObject[2] = prdimdetails.getFieldValue(0, "DIM_TAB_ID");
                    memberObject[3] = prdimdetails.getFieldValue(0, "DIM_TAB_ID");
                    memberObject[4] = "select DISTINCT " + dimcolStr.toString().trim() + " from " + dimTableNames[dicount].toUpperCase().trim();
                    memberObject[5] = dimColumns[dimdet];
                    finalQueryformember = buildQuery(inInDimMember, memberObject);
                    //
                    execModifySQL(finalQueryformember);
                    //
                    dimQueryList = new ArrayList();
                    for (int c = 0; c < 2; c++) {
                        memberDetails[0] = dimColumns[dimdet].trim();
                        memberDetails[1] = prdimdetails.getFieldValue(0, "DIM_ID");
                        for (int menlist = 0; menlist < templistcol.size(); menlist++) {
                            String[] tempmemAr = templistcol.get(menlist).toString().split("~");
                            //
                            if (tempmemAr[1].trim().equalsIgnoreCase(dimColumns[dimdet].toUpperCase().trim().replace(" ", "_"))) {
                                memberDetails[2] = tempmemAr[0];
                            }
                            if (c == 0) {
                                memberDetails[3] = "KEY";
                            } else {
                                memberDetails[3] = "VALUE";
                            }
                        }
                        finalQuery = buildQuery(memberDetailQuery, memberDetails);
                        dimQueryList.add(finalQuery);
                        //


                    }
                    //
                    executeMultiple(dimQueryList);


                }

                relObj[0] = pbret.getFieldValue(0, 0);
                relObj[1] = dimTableNames[dicount].toUpperCase().trim();
                relObj[2] = dimTableNames[dicount].toUpperCase().trim();
                String relFinalQuery = buildQuery(insertIntoRelMas, relObj);
                //
                execModifySQL(relFinalQuery);
                selObj[0] = relObj[0];
                finalQuery = buildQuery(selectMemAndRelDetails, selObj);
                //
                PbReturnObject seldetails = execSelectSQL(finalQuery);
                for (int retList = 0; retList < seldetails.getRowCount(); retList++) {
                    relDetaiObj[0] = seldetails.getFieldValue(retList, "REL_ID");
                    relDetaiObj[1] = seldetails.getFieldValue(retList, "MEMBER_ID");
                    relDetaiObj[2] = retList + 1;
                    String finalQreldat = buildQuery(insertIntoRElD, relDetaiObj);
                    memberList.add(finalQreldat);
                }
                //
            }

            executeMultiple(memberList);
            CreatetableDAO createdao = new CreatetableDAO();
            ArrayList businesParaams = new ArrayList();
            businesParaams.add(sheetName);
            businesParaams.add(tabIDshm);
            businesParaams.add(dimentionsIDS);
            businesParaams.add(valradio);
            businesParaams.add(userGvenNames);

            createdao.businessGroupLayer(businesParaams, conn_id);
//sheetName, tabIDshm, conn_id, dimentionsIDS, valradio
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    private void businessGroupLayer(ArrayList businesParaams, int conn_id) throws Exception {
//String sheetName, HashMap tabIDshm, int conn_id, String[] dimentionsIDS, String valradio
        String assignuserTogrop = resBundle.getString("assignuserTogrop");
        String sheetName = (String) businesParaams.get(0);
        HashMap tabIDshm = (HashMap) businesParaams.get(1);
        String[] dimentionsIDS = (String[]) businesParaams.get(2);
        String valradio = (String) businesParaams.get(3);
        ArrayList userGvenNames = (ArrayList) businesParaams.get(4);
        ArrayList bussgropids = new ArrayList();
        BusinessGroupDAO businessGroupDAO = new BusinessGroupDAO();
        int busgrpID = 0;
        String[] facttalID = new String[1];
        String[] noOfnodes = null;
        String gropName = userGvenNames.get(0).toString().toUpperCase().trim();
        String roleName = userGvenNames.get(0).toString().toUpperCase().trim();
        String insertTimedimntion = resBundle.getString("insertTimedimntion");
        String dateColname = valradio.replace("[", "").replace("]", "").trim();
        String finalQuery = "";
        String finalQuery1 = "";
        PbReturnObject prgr = new PbReturnObject();
        Object[] obj = new Object[3];
        Object[] assignObje = new Object[3];
        HashMap dispColnames = null;
        HashMap defaultAgeHm = null;
        String updateColumnDes = resBundle.getString("updateColumnDes");
        Object[] uploadObject = new Object[4];
        String[] hasKeySet = null;
        ArrayList busGroupArList = new ArrayList();
        try {
            busgrpID = businessGroupDAO.addBusinessGroup(gropName, gropName, Integer.toString(conn_id));
            bussgropids.add(busgrpID);
            String userid = "";
            dispColnames = new HashMap();
            defaultAgeHm = new HashMap();
            if (session != null) {//related code copy at pbBaseAction
                userid = String.valueOf(session.getAttribute("USERID"));
                dispColnames = (HashMap) (session.getAttribute("dispColnames"));
                defaultAgeHm = (HashMap) (session.getAttribute("deAgrKesetHM"));
            }
            //
            hasKeySet = (String[]) dispColnames.keySet().toArray(new String[0]);
            assignObje[0] = userid;
            assignObje[1] = busgrpID;
            assignObje[2] = userid;
            finalQuery1 = buildQuery(assignuserTogrop, assignObje);
            busGroupArList.add(finalQuery1);
            logger.info("assign grop \t" + finalQuery1);
            //  execModifySQL(finalQuery1);
            for (int asscount = 0; asscount < hasKeySet.length; asscount++) {
                uploadObject[0] = dispColnames.get(hasKeySet[asscount]);
                uploadObject[1] = defaultAgeHm.get(hasKeySet[asscount]);
                uploadObject[2] = hasKeySet[asscount].toUpperCase().trim();
                uploadObject[3] = tabIDshm.get(sheetName.toUpperCase().trim()).toString().trim();


                finalQuery1 = buildQuery(updateColumnDes, uploadObject);
                busGroupArList.add(finalQuery1);
            }
            logger.info("busGroupArList\t" + busGroupArList);

            facttalID[0] = tabIDshm.get(sheetName.toUpperCase().trim()).toString().trim();
            noOfnodes = new String[facttalID.length];
            for (int len = 0; len < facttalID.length; len++) {
                noOfnodes[len] = "1";

            }
            businessGroupDAO.insertGrpDim(dimentionsIDS, Integer.toString(busgrpID), facttalID);
            boolean updateCheck = executeMultiple(busGroupArList);

            logger.info("updateCheck:\t" + updateCheck);
            SaveExcelTimeDimention saveExcelTimeDimention = new SaveExcelTimeDimention();

            obj[0] = busgrpID;
            obj[1] = sheetName.toUpperCase().trim();
            obj[2] = dateColname.toUpperCase().trim();
            finalQuery = buildQuery(insertTimedimntion, obj);
            prgr = execSelectSQL(finalQuery);
            //
            boolean check = saveExcelTimeDimention.inserttimeDimen(Integer.toString(busgrpID), sheetName, prgr);
            //
            UserLayerDAO userLayerDAO = new UserLayerDAO();
            boolean result = userLayerDAO.addUserFolder(roleName, roleName, Integer.toString(busgrpID));


        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public PbReturnObject checkInDB(String selectedcolname, String filename) {
        String[] seArraydcolname = selectedcolname.split(",");
        String checkInDBQuery = resBundle.getString("checkInDBQuery");
        String finalQuery = "";
        String strCol = "";

        PbReturnObject checkBo = null;
        Object[] obj = new Object[2];
        try {
            for (String colNames : seArraydcolname) {
                strCol = strCol + ",'" + colNames.replace(" ", "_").toUpperCase().trim() + "'";
                //
            }
            strCol = strCol.substring(1);
            obj[0] = strCol;
            obj[1] = filename.trim();
            //
            finalQuery = buildQuery(checkInDBQuery, obj);
            //
            checkBo = execSelectSQL(finalQuery);

            //  checkBo
        } catch (Exception e) {
            logger.error("Exception:", e);
        }


        return checkBo;
    }

    public boolean saveTableDetailsfromExcelSheet(File tempFile, String uploadTableName, String connId, String chkid) {
//         Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
//
//    put("^\\[a-z]{3}\\s[a-z]{3}\\s\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\s[a-z]{3}\\s\\d{4}$", "EEE MMM dd HH:mm:ss ZZZ yyyy");
//    put("^\\d{8}$", "yyyyMMdd");
//    put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
//    put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
//    put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
//    put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
//    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
//    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
//    put("^\\d{12}$", "yyyyMMddHHmm");
//    put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
//    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
//    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
//    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
//    put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
//    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
//    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
//    put("^\\d{14}$", "yyyyMMddHHmmss");
//    put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
//    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
//    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
//    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
//    put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
//    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
//    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
//}};




//        FileInputStream fileInputStream = null;
//         WorkbookSettings ws = null;
//        Workbook workbook = null;
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        WSBoolRecord wsb=new WSBoolRecord();
//        HSSFCell cell =null;
//         Cell cell =null;
        XSSFCell cell = null;
//        Sheet s = null;
//         HSSFSheet s =null;
        XSSFSheet s = null;
        int columnCount = '0';
        boolean check = true;
        String dateFormat = "";
        int totalSheet = -1;
        ArrayList Alist = null;
        String sheetName = null;
        String query = "";
        String SelectQusery = "";
        Object[] obj = new Object[1];
        PbReturnObject prgr = null;
        try {
//          Connection con =  ProgenConnection.getInstance().getConnectionByConId(connId);
////          Connection con =  ProgenConnection.getInstance().getConnection();
//             PbReturnObject pbReturnObject=new PbReturnObject();
//             if(con!=null)
//             pbReturnObject=super.execSelectSQL("select * from  "+uploadTableName+" limit 1 ",con);
//             else
//             pbReturnObject=super.execSelectSQL("select * from  "+uploadTableName+" limit 1");
//           fileInputStream = new FileInputStream(tempFile);
//            ws = new WorkbookSettings();
//            ws.setLocale(new Locale("en", "EN"));
//            BufferedReader br = null;
//            br = new BufferedReader(new FileReader(tempFile));
//            br.;
//            int count1=0;
//            while(br.readLine()!=null)
//            {
//                count1++;
//            }
//              
//            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tempFile));
//            bis.readLine();
//            String name = tempFile.getName();

//            NPOIFSFileSytem fs = new NPOIFSFileSystem(new File("file.xls"));
//            workbook = Workbook.getWorkbook(fileInputStream);
//            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
//            XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
//           File f1=new File(tempFile.getAbsolutePath());
//            PackageAccess READ;

            OPCPackage pkg = OPCPackage.open(tempFile, PackageAccess.READ);
            UploadXSLX xlsx2csv = new UploadXSLX(pkg, System.out, -1, uploadTableName, connId, chkid);
            xlsx2csv.process();
            boolean delete = tempFile.delete();
//            
//            org.apache.poi.ss.usermodel.Workbook workbook  = new SXSSFWorkbook(100);
//             XSSFWorkbook workbook=new XSSFWorkbook(pkg);
//            workbook= WorkbookFactory.create(pkg);
//             XSSFWorkbook workbook=(XSSFWorkbook) WorkbookFactory.create(pkg);
//             XSSFWorkbook workbook=new XSSFWorkbook(pkg);
//            totalSheet = workbook.getNumberOfSheets();
//            totalSheet = workbook.getNumberOfSheets();
//               s = workbook.getSheet(0) ;
//             s =   workbook.getSheetAt(0) ;
//               StringBuilder sqlQuery = new StringBuilder();
//               sqlQuery.append("insert into ").append(uploadTableName).append( " values(");
//               StringBuilder innersqlquery = new StringBuilder("");
//               for(int count=0;count<pbReturnObject.getColumnCount();count++){
//               innersqlquery.append(","+"?");
//               }
//               sqlQuery.append( innersqlquery.substring(1));
//               sqlQuery.append(")");
//              Connection con1 =  ProgenConnection.getInstance().getConnectionByConId(connId);
//               PreparedStatement ps=con1.prepareStatement(sqlQuery.toString());
//               PreparedStatement ps1=con1.prepareStatement("truncate table "+uploadTableName);
//               String[] dbcolType=pbReturnObject.getColumnTypes();
//                String str="";
//                           Date dnew=null;
//                           String mydatestring="";
////                 for (int i = 1; i <s.getRows(); i++) {
//                    for (int i = 1; i <s.getPhysicalNumberOfRows(); i++) {
//                        int flag=0;
//                     for(int j=0;j<pbReturnObject.getColumnCount();j++){
////                      cell = s.getCell(j, i);
//                         cell = s.getRow(i).getCell(j);
//                        int k=cell.getCellType();
//                        Object ovalue=null;
////                       CellType type = cell.getType();
////                       if(s.getCell(j, i).getContents()!=null && !s.getCell(j, i).getContents().equalsIgnoreCase("")){
//
//                            if(k==0)
//                            {
//                               ovalue= cell.getNumericCellValue();
//                            }
//                           else if(k==1)
//                            {
//                                ovalue= cell.getStringCellValue();
//
//                            }
//                        if(ovalue!=null){
////                         if(cell.getStringCellValue()!=null && !cell.getStringCellValue().equalsIgnoreCase("")){
//                           flag=1;
//                       if(dbcolType[j].equalsIgnoreCase("number")||dbcolType[j].equalsIgnoreCase("numeric")){
//                            if(k==0)
//                            {
//                               ps.setInt(j+1,(new Double(cell.getNumericCellValue())).intValue());
//                            }
//                           else if(k==1)
//                            {
//                              ps.setInt(j+1,Integer.parseInt(cell.getStringCellValue()));
//
//                            }
////                          ps.setInt(j+1,(new Double(cell.getNumericCellValue())).intValue());
////                           ps.setInt(j+1,Integer.parseInt(cell.getContents().toString()));
//                       } else if(dbcolType[j].equalsIgnoreCase("date")){
//
//
////                           if(cell.getContents().toString()!=null){
////                                  str = cell.getStringCellValue();
//                                   if(k==0)
//                                   {
//                                        if (DateUtil.isCellDateFormatted((org.apache.poi.ss.usermodel.Cell) cell)) {
////                                       
//                                     dnew=cell.getDateCellValue();
//                                     mydatestring=dnew.toString();
//                                     for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
//                                        if (mydatestring.toLowerCase().matches(regexp)) {
//                                                dateFormat= DATE_FORMAT_REGEXPS.get(regexp);
//                                             }
//                                      }
//                                     DataFormatter formatter = new DataFormatter();
////                             String formattedCellValue = formatter.formatCellValue(cell);
//                                                 java.text.Format dateFormat1=formatter.getDefaultFormat(cell);
////                                         CellFormat str1 = cell.getCellFormat();
////                                          Format dateFormat1=formattedCellValue.getFormat();
//                                           dateFormat=dateFormat1.toString();
//            //                               
//                                 if(dateFormat.equalsIgnoreCase("general")||dateFormat.equalsIgnoreCase("M/D/YYYY")||dateFormat.equalsIgnoreCase("DD/MM/YYYY"))
//                                   dateFormat="dd MM yyyy hh:mm:ss";
//                                 else if(dateFormat.equalsIgnoreCase("YYYY\\-MM\\-DD"))
//                                   dateFormat="dd/MM/yyyy";
//                                 else
//                                     dateFormat="dd-MMM-yyyy";
//                              SimpleDateFormat sdf1=new SimpleDateFormat(dateFormat);
//                              Date d=sdf1.parse(mydatestring);
////                              SimpleDateFormat sdf2=new SimpleDateFormat("yy-MM-dd");
//                            ps.setString(j+1,sdf1.format(d));
//                               }else
//                               {
//                                            String s1="";
//                                           ps.setString(j+1,s1+((new Double(cell.getNumericCellValue())).intValue()));
//                                }
//
//                          }
//                          else if(k==1)
//                          {
//                                 ps.setString(j+1,cell.getStringCellValue());
////                              ps.setInt(j+1,Integer.parseInt(cell.getStringCellValue()));
//
//                           }
////                               str =cell.getContents().toString();
//
//                       }else{
//
//                               if(k==0)
//                                   {
//                                        if (DateUtil.isCellDateFormatted((org.apache.poi.ss.usermodel.Cell) cell)) {
////                                       
//                                     dnew=cell.getDateCellValue();
//                                     mydatestring=dnew.toString();
//                                     for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
//                                        if (mydatestring.toLowerCase().matches(regexp)) {
//                                                dateFormat= DATE_FORMAT_REGEXPS.get(regexp);
//                                             }
//                                      }
//
//                                     DataFormatter formatter = new DataFormatter();
//                             String formattedCellValue = formatter.formatCellValue(cell);
////                                                 java.text.Format dateFormat1=formatter.getDefaultFormat(cell);
////                                         CellFormat str1 = cell.getCellFormat();
////                                          Format dateFormat1=formattedCellValue.getFormat();
////                                           dateFormat=dateFormat1.toString();
//
//            //                             CellFormat str1 = cell.getCellFormat();
//            //                              Format dateFormat1=str1.getFormat();
//            //                               dateFormat=dateFormat1.getFormatString();
//            //                               
//                                 if(dateFormat.equalsIgnoreCase("general")||dateFormat.equalsIgnoreCase("M/D/YYYY")||dateFormat.equalsIgnoreCase("DD/MM/YYYY"))
//                                   dateFormat="dd MM yyyy hh:mm:ss";
//                                 else if(dateFormat.equalsIgnoreCase("YYYY\\-MM\\-DD"))
//                                   dateFormat="dd/MM/yyyy";
//                                 else
//                                     dateFormat="dd-MMM-yyyy";
//                              SimpleDateFormat sdf1=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
//                              Date d=sdf1.parse(mydatestring);
//                              SimpleDateFormat sdf2=new SimpleDateFormat("dd-MMM-yyyy");
//                            ps.setString(j+1,sdf2.format(d));
//                               }else
//                               {
//                                            String s1="";
//                                           ps.setString(j+1,s1+((new Double(cell.getNumericCellValue())).intValue()));
////                                            ps.setString(j+1,cell.getDateCellValue().toString());
//                                }
//
//                          }
//                          else if(k==1)
//                          {
//                                 ps.setString(j+1,cell.getStringCellValue());
////                              ps.setInt(j+1,Integer.parseInt(cell.getStringCellValue()));
//
//                           }
//
////                               ps.setString(j+1,(cell.getStringCellValue()));
//                       }
////                        if (type.toString().equalsIgnoreCase("NUMBER")) {
////                            
////                            ps.setInt(j+1,Integer.parseInt(s.getCell(j, i).getContents().toString()));
////                        } else if (type.equals("DATE")) {
////                            
////                            ps.setString(j+1,"(to_Date('" +s.getCell(j, i).getContents() + "','dd mm yyyy hh24:MI:ss'))");
////
////                        } else {
//
////                        }
//                       }else{
//                           ps.setString(j+1,null);
//                       }
//                     }
//                     if(flag==1)
//                      ps.addBatch();
//                 }
//
//               if ("truncate".equals(chkid)) {
//                ps1.addBatch();
//                ps1.executeBatch();
//               }
//                ps.executeBatch();
//                ps.close();
//                check = true;
//                 pkg.close();
        } catch (Exception e) {
            logger.error("Exception:", e);
            check = false;
        }

        return check;
    }

    public String getFileName(String downloadTableName) {
        return ServletUtilities.prefix + downloadTableName + "_DS";
    }

    public String saveExcelDetails(String downloadTableName, String connId) {

        ServletOutputStream outputstream = null;
        Label[] labels = null;
        //common code for all
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 0;
        String[] columnNames;

        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;

        try {

            swt = ServletUtilities.createBufferedWriter(downloadTableName, "xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);

            // writableSheet = writableWorkbook.createSheet(downloadTableName, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(false);

//            Connection con = ProgenConnection.getInstance().getConnection();
            Connection con = ProgenConnection.getInstance().getConnectionByConId(connId);
            PbReturnObject pbReturnObject = new PbReturnObject();

            if (con != null) {
                pbReturnObject = super.execSelectSQL("select * from  " + downloadTableName, con);
            } else {
                pbReturnObject = super.execSelectSQL("select * from  " + downloadTableName);
            }
            columnNames = pbReturnObject.getColumnNames();

            int colCount = 0;
            int sheet = 0;
            int fromRow = 0;
            if (fromRow == 0 && colCount == 0) {
                colCount = pbReturnObject.getRowCount();

            }

            if (colCount > 65000) {
                sheet = colCount / 65000;
                colCount = 65000;
            }
            for (int k = 0; k <= sheet; k++) {
                writableSheet = writableWorkbook.createSheet(downloadTableName + (k + 1), k);
                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setBackground(Colour.GRAY_25);
                writableCellFormat.setWrap(true);

                WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.THOUSANDS_INTEGER);

                for (int i = 0; i < columnNames.length; i++) {
                    label = new Label(i, rowStart, columnNames[i]);
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);

                    HeadingCellView = new CellView();
                    HeadingCellView.setSize(256 * 20);
                    writableSheet.setColumnView(i, HeadingCellView);
                }

                writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setWrap(true);

                if (pbReturnObject != null) {
                    int row = 0;
                    for (int i = fromRow; i < colCount && i < pbReturnObject.getRowCount(); i++) {
                        for (int j = 0; j < columnNames.length; j++) {
                            cell = new Label(j, row + rowStart + 1, pbReturnObject.getFieldValueString(i, j), cf2);
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                        row++;
                    }
                }
                colCount = colCount + 65000;
                fromRow = fromRow + 65000;
            }

            writableWorkbook.write();
            writableWorkbook.close();

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return swt.fileName;

    }

    public String getExcelColumns(File tempFile, String uploadTableName) {
        FileInputStream fileInputStream = null;
        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        int totalSheet = -1;
        String colname = null;
        LinkedHashMap<String, String> excelColsTypes = new LinkedHashMap<String, String>();
        String jsonString = null;
        try {
            fileInputStream = new FileInputStream(tempFile);
            ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = Workbook.getWorkbook(fileInputStream);
            totalSheet = workbook.getNumberOfSheets();
            s = workbook.getSheet(0);
            for (int i = 0; i < s.getColumns(); i++) {
                for (int j = 0; j < 1; j++) {
                    if (s.getCell(i, j).getContents() != null && !s.getCell(i, j).getContents().equalsIgnoreCase("")) {
                        Cell cell = s.getCell(i, j);
                        CellType type = cell.getType();
                        if (s.getCell(i, 0).getContents() != null) {
                            colname = s.getCell(i, 0).getContents();
                        }
                        excelColsTypes.put(colname, type.toString());
                    }
                }
            }
            Gson gson = new Gson();
            jsonString = gson.toJson(excelColsTypes);
        } catch (Exception ex) {
            logger.error("Exception:", ex);

        }
        return jsonString;
    }

    public String getCustomerTableNames(String conid) {

        String tabnamesJson = "";
        HashMap map = new HashMap();
        List<String> tablelist = new ArrayList<String>();
        GetConnectionType getConnectionType = new GetConnectionType();
        String connectionType = getConnectionType.getConTypeByConnID(conid);
        Connection connection = ProgenConnection.getInstance().getConnectionByConId(conid);
        //
        if (!connectionType.equalsIgnoreCase("Mysql") && !connectionType.equalsIgnoreCase("SqlServer")) {
            String Query = "select tname from tab";
            PbReturnObject returnObject = new PbReturnObject();
            try {
                returnObject = super.execSelectSQL(Query, connection);
                if (returnObject != null) {
                    for (int i = 0; i < returnObject.getRowCount(); i++) {
                        tablelist.add(returnObject.getFieldValueString(i, 0));
                    }
                }
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        } else if (connectionType.equalsIgnoreCase("SqlServer")) {
            String Query = "select TABLE_NAME from PRG_DB_MASTER_TABLE where CONNECTION_ID = " + conid;

            PbReturnObject returnObject = new PbReturnObject();
            try {
                returnObject = super.execSelectSQL(Query);
                if (returnObject != null) {
                    for (int i = 0; i < returnObject.getRowCount(); i++) {
                        tablelist.add(returnObject.getFieldValueString(i, 0));
                    }
                }
            } catch (Exception ex) {
                logger.error("Exception: ", ex);
            }
        } else {
            try {
                DatabaseMetaData dbm = connection.getMetaData();
                String[] types = {"TABLE"};
                ResultSet rs = dbm.getTables(null, null, "%", types);
                while (rs.next()) {
                    tablelist.add(rs.getString("TABLE_NAME"));
                }
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
        }
        map.put("tablelist", tablelist);
        Gson gson = new Gson();
        tabnamesJson = gson.toJson(map);
        return tabnamesJson;
    }

    public String downloadexcel(String dahletid, String dashboardId, pbDashboardCollection collect, PbReturnObject pbretObj, Container container, String date) {

        DashletDetail dashletdetail = collect.getDashletDetail(dahletid);
        String kpiName = dashletdetail.getkpiName();
        StringBuilder kpiElementsname = new StringBuilder();
        if (kpiName.contains(" ")) {
            kpiName = kpiName.replaceAll(" ", "_");
        }
        String typeKPI = "MultiPeriodCurrentPrior";

        //Added By Ram
        List<ArrayList<String>> calculativeVal = container.getCalculativeVal();
        List<ArrayList<String>> gtVal = container.getGtVal();
        String fileName = dashletdetail.getDashletName();
        fileName = fileName.substring(0, fileName.length() - 4);
        fileName = fileName.replaceAll(" ", "_");
        String fName = fileName + "_" + date + ".xls";
        ArrayList ViewBy = container.getViewBy();
        //Ended Ram Code


        StringBuilder kpiElementIdsString = new StringBuilder();
        ServletOutputStream outputstream = null;
        Label[] labels = null;
        //common code for all
        WritableWorkbook writableWorkbook = null;
        WritableSheet writableSheet = null;
        WritableFont writableFont = null;
        WritableCellFormat writableCellFormat = null;
        Label label = null;
        CellView HeadingCellView = null;
        NumberFormat nFormat = null;
        WritableCell cell = null;
        int rowStart = 0;
        String[] columnNames;
        String elementname = null;
        List<String> group = null;

        nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        ServletWriterTransferObject swt = null;
        KPI kpiDetail = (KPI) dashletdetail.getReportDetails();
        String dashletId = dashletdetail.getDashBoardDetailId();
        List<String> a1 = kpiDetail.getElementIds();
        ArrayListMultimap<String, KPIElement> kpiElementMap = kpiDetail.getKPIElementsMap();
        for (String elementids : a1) {
            kpiElementIdsString.append(",").append(elementids);
            // 
            List<KPIElement> kpiElems = kpiElementMap.get(elementids);

            if (kpiElems != null) {
                for (KPIElement elem : kpiElems) {
                    if (elem.getElementName() != null) {
                        kpiElementsname.append(elem.getElementName()).append(",");
                    }

                }
            }

        }



        try {

            //  swt = ServletUtilities.createBufferedWriter(kpiName,"xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            //     writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + swt.fileName), wbSettings);
            writableWorkbook = Workbook.createWorkbook(new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + fName), wbSettings);
            writableSheet = writableWorkbook.createSheet(kpiName, 0);
            writableFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setWrap(false);

            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            writableCellFormat = new WritableCellFormat(writableFont);
            writableCellFormat.setBackground(Colour.GRAY_25);
            writableCellFormat.setWrap(true);
            if (dashletdetail.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                StringBuilder kpiheads = new StringBuilder();
                ArrayList<String> labelList = new ArrayList<String>();
                if (dashletdetail.getKpiheads().isEmpty() || dashletdetail.getKpiheads().get(0).equalsIgnoreCase("")) {
                    if (dashletdetail.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior")) {

                        kpiheads.append(",").append("KPI");
                        kpiheads.append(",").append("MTD Current");
                        kpiheads.append(",").append("Target");
                        kpiheads.append(",").append("MTD Prior");
                        kpiheads.append(",").append("Deviation");
                        kpiheads.append(",").append("Deviation%");
                        kpiheads.append(",").append("MTD Change");
                        kpiheads.append(",").append("MTD Change%");
                        kpiheads.append(",").append("QTD Current");
                        kpiheads.append(",").append("Target");
                        kpiheads.append(",").append("QTD Prior");
                        kpiheads.append(",").append("Deviation");
                        kpiheads.append(",").append("Deviation%");
                        kpiheads.append(",").append("QTD Change");
                        kpiheads.append(",").append("QTD Change%");
                        kpiheads.append(",").append("YTD Current");
                        kpiheads.append(",").append("Target");
                        kpiheads.append(",").append("YTD Prior");
                        kpiheads.append(",").append("Deviation");
                        kpiheads.append(",").append("Deviation%");
                        kpiheads.append(",").append("YTD Change");
                        kpiheads.append(",").append("YTD Change%");

                    }
                } else {
                    for (int i = 0; i < dashletdetail.getKpiheads().size(); i++) {
                        kpiheads.append(",").append(dashletdetail.getKpiheads().get(i));

                    }
                }
                kpiheads.replace(0, 1, "");
                String customkpiheads = kpiheads.toString();
                String[] Kpiheadarray = customkpiheads.split(",");
                labelList.add(Kpiheadarray[0]);
                if (kpiDetail.isMTDChecked()) {
                    labelList.add(Kpiheadarray[1]);
                    labelList.add(Kpiheadarray[2]);
                    labelList.add(Kpiheadarray[3]);
                    labelList.add(Kpiheadarray[4]);
                    labelList.add(Kpiheadarray[5]);
                    labelList.add(Kpiheadarray[6]);
                    labelList.add(Kpiheadarray[7]);
                }
                if (kpiDetail.isQTDChecked()) {
                    labelList.add(Kpiheadarray[8]);
                    labelList.add(Kpiheadarray[9]);
                    labelList.add(Kpiheadarray[10]);
                    labelList.add(Kpiheadarray[11]);
                    labelList.add(Kpiheadarray[12]);
                    labelList.add(Kpiheadarray[13]);
                    labelList.add(Kpiheadarray[14]);
                }
                if (kpiDetail.isYTDChecked()) {
                    labelList.add(Kpiheadarray[15]);
                    labelList.add(Kpiheadarray[16]);
                    labelList.add(Kpiheadarray[17]);
                    labelList.add(Kpiheadarray[18]);
                    labelList.add(Kpiheadarray[19]);
                    labelList.add(Kpiheadarray[20]);
                    labelList.add(Kpiheadarray[21]);
                }
                for (int i = 0; i < labelList.size(); i++) {
                    label = new Label(i, rowStart, labelList.get(i));
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);

                    HeadingCellView = new CellView();
                    HeadingCellView.setSize(256 * 30);
                    writableSheet.setColumnView(i, HeadingCellView);
                }
                writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setWrap(true);




                for (int k = 0; k < a1.size(); k++) {
                    String ElementId = a1.get(k);
                    List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);
                    String elementId = a1.get(k);
                    String elementName = "";
                    String value = "";
                    String kpiMasterId = dashletdetail.getKpiMasterId();
                    String targetValueStr = "--";
                    Double monthTargetVal = kpiDetail.getTargetValue(ElementId, "Month");
                    Double DayTargetVal = kpiDetail.getTargetValue(ElementId, "Day");
                    Double QtrTargetVal = kpiDetail.getTargetValue(ElementId, "Qtr");
                    Double yearTarget = kpiDetail.getTargetValue(ElementId, "Year");
                    String targetElementId = null;

                    if (kpiDetail.getKpiTragetMap(ElementId) != null && !kpiDetail.getKpiTragetMap(ElementId).equalsIgnoreCase("")) {
                        targetElementId = kpiDetail.getKpiTragetMap(ElementId);
                    }
                    boolean isGroupElement = dashletdetail.isGroupElement(elementId);
                    List<KPIElement> elements = kpiElementMap.get(elementId);
                    if (!isGroupElement) {
                        for (KPIElement elem : elements) {
                            if (elem != null && elem.getElementId() != null && !elem.getElementId().equalsIgnoreCase("")) {

                                elementName = elem.getElementName();
                                isGroupElement = elem.isIsGroupElement();
                                break;
                            }
                        }
                    }
                    if (isGroupElement) {
                        if (dashletdetail.getSingleGroupHelpers() != null && !dashletdetail.getSingleGroupHelpers().isEmpty()) {
                            List<KPISingleGroupHelper> kPISingleGroupHelpers = dashletdetail.getSingleGroupHelpers();
                            for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                                BigDecimal sumBd = new BigDecimal(0);
                                if (groupingHelper.getGroupName() != null) {
                                    if (groupingHelper.getGroupName().equalsIgnoreCase(elementId)) {
                                        if (groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()) != null) {
                                            kpiDetail.addKPIDrill(groupingHelper.getGroupName(), groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()));
                                        }
                                        group = getGroupVal(groupingHelper, pbretObj, 1, kpiMasterId, dashletdetail, dashboardId, collect);
                                    }
                                }
                            }
                        }

                    }
                    Double currValMul = 0.0;
                    int decimalplaces = 2;
                    double priorVal = 0.0;
                    Double changeVal = 0.0;
                    HashMap TargetInfo = new HashMap();
                    HashMap multiKpiDetailsMap = new HashMap();
                    TargetInfo.put("ElementId", ElementId);// oth element
                    TargetInfo.put("kpiMasterid", kpiMasterId);//1st element
                    TargetInfo.put("ReportId", dashboardId);
                    TargetInfo.put("dashletId", dashletdetail.getDashBoardDetailId());
                    TargetInfo.put("MonthTarget", monthTargetVal);
                    TargetInfo.put("DayTargetVal", DayTargetVal);
                    TargetInfo.put("QtrTargetVal", QtrTargetVal);
                    TargetInfo.put("yearTarget", yearTarget);
                    TargetInfo.put("targetElementId", targetElementId);
                    for (int i = 0; i < kpiElements.size(); i++) {
                        String temp = "A_" + kpiElements.get(i).getElementId();
                        String type = kpiElements.get(i).getRefElementType();
                        if (!isGroupElement) {
                            if (container.getMultiPeriodKPI().getMonthObject() != null && (container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)))) {
                                if (type.equalsIgnoreCase("1")) {
                                    currValMul = Double.parseDouble((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)));
                                    multiKpiDetailsMap.put("AbsMntCurrVal", currValMul);

                                    BigDecimal bd3 = new BigDecimal(currValMul);
                                    bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("MonthCurrentVal", formatter.format(bd3));



                                } else if (type.equalsIgnoreCase("2")) {
                                    String priorValStr = container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp);
                                    if (priorValStr != null && !("".equals(priorValStr))) {
                                        priorVal = Double.parseDouble(priorValStr);
                                    }
                                    BigDecimal bd3 = new BigDecimal(priorVal);
                                    bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("MonthPriorVal", formatter.format(bd3));


                                } else if (type.equalsIgnoreCase("3")) {
                                    String changeValStr = container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp);
                                    if (changeValStr != null && !("".equals(changeValStr))) {
                                        changeVal = Double.parseDouble(changeValStr);
                                    }
                                    BigDecimal bd3 = new BigDecimal(changeVal);
                                    bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("MonthChangeVal", formatter.format(bd3));


                                } else if (type.equalsIgnoreCase("4")) {
                                    double changePercVal = Double.parseDouble((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)));
                                    BigDecimal bd3 = new BigDecimal(changePercVal);
                                    bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("MonthChangePercenVal", formatter.format(bd3));
                                }
                            } else {
                            }
                            if (container.getMultiPeriodKPI().getQuarterObject() != null && (container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)))) {
                                if (type.equalsIgnoreCase("1")) {
                                    currValMul = Double.parseDouble((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)));
                                    multiKpiDetailsMap.put("AbsQtrCurrVal", currValMul);
                                    BigDecimal bd2 = new BigDecimal(currValMul);
                                    bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));

                                    multiKpiDetailsMap.put("QtrCurrentVal", formatter.format(bd2));


                                } else if (type.equalsIgnoreCase("2")) {
                                    String priorValStr = container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp);
                                    if (priorValStr != null && !("".equals(priorValStr))) {
                                        priorVal = Double.parseDouble(priorValStr);
                                    }
                                    BigDecimal bd2 = new BigDecimal(priorVal);
                                    bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("QtrPriorVal", formatter.format(bd2));


                                } else if (type.equalsIgnoreCase("3")) {
                                    String changeValStr = container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp);
                                    if (changeValStr != null && !("".equals(changeValStr))) {
                                        changeVal = Double.parseDouble(changeValStr);
                                    }

                                    BigDecimal bd2 = new BigDecimal(changeVal);
                                    bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("QtrChangeVal", formatter.format(bd2));

                                } else if (type.equalsIgnoreCase("4")) {
                                    double changePercVal = Double.parseDouble((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)));
                                    BigDecimal bd2 = new BigDecimal(changePercVal);
                                    bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("QtrChangePercenVal", formatter.format(bd2));
                                }
                            }
                            if (container.getMultiPeriodKPI().getYearObject() != null && (container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)))) {
                                if (type.equalsIgnoreCase("1")) {
                                    currValMul = Double.parseDouble((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)));
                                    multiKpiDetailsMap.put("AbsYrCurrVal", currValMul);
                                    BigDecimal bd1 = new BigDecimal(currValMul);
                                    bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("YearCurrentVal", formatter.format(bd1));


                                } else if (type.equalsIgnoreCase("2")) {
                                    String priorValStr = container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp);
                                    if (priorValStr != null && !("".equals(priorValStr))) {
                                        priorVal = Double.parseDouble(priorValStr);
                                    }
                                    BigDecimal bd1 = new BigDecimal(priorVal);
                                    bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("YearPriorVal", formatter.format(bd1));


                                } else if (type.equalsIgnoreCase("3")) {
                                    String changeValStr = container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp);
                                    if (changeValStr != null && !("".equals(changeValStr))) {
                                        changeVal = Double.parseDouble(changeValStr);
                                    }
                                    BigDecimal bd1 = new BigDecimal(changeVal);
                                    bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("YearchangeVal", formatter.format(bd1));

                                } else if (type.equalsIgnoreCase("4")) {
                                    double changePercVal = Double.parseDouble((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)));
                                    BigDecimal bd1 = new BigDecimal(changePercVal);
                                    bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                    NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                    multiKpiDetailsMap.put("YearChangePercenVal", formatter.format(bd1));

                                }
                            }

                        }


                    }

                    //getting the target element details...
                    if (targetElementId != null && !targetElementId.equalsIgnoreCase("")) {
                        List<KPIElement> targetkpielements = kpiDetail.getTargetKPIElements(targetElementId);
                        for (int i = 0; i < targetkpielements.size(); i++) {
                            String temp = "A_" + targetkpielements.get(i).getElementId();
                            String type = targetkpielements.get(i).getRefElementType();
                            if (!isGroupElement) {
                                if (container.getMultiPeriodKPI().getMonthObject() != null && (container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currValMul = Double.parseDouble((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)));
                                        BigDecimal bd3 = new BigDecimal(currValMul);
                                        bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                        multiKpiDetailsMap.put("MonthCustomTargetVal", bd3.toString());
                                    }
                                }
                                if (container.getMultiPeriodKPI().getQuarterObject() != null && (container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currValMul = Double.parseDouble((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)));
                                        BigDecimal bd3 = new BigDecimal(currValMul);
                                        bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                        multiKpiDetailsMap.put("QtrCustomTargetVal", bd3.toString());
                                    }
                                }
                                if (container.getMultiPeriodKPI().getYearObject() != null && (container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currValMul = Double.parseDouble((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)));
                                        BigDecimal bd3 = new BigDecimal(currValMul);
                                        bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                        multiKpiDetailsMap.put("YearCustomTargetVal", bd3.toString());
                                    }
                                }

                            }
                        }
                    }
                    String targetValStr = "--";

                    String kpiMasterid = TargetInfo.get("kpiMasterid").toString();
                    String ReportId = TargetInfo.get("ReportId").toString();

                    String monthDevPer = "0.0";
                    String dayDevPer = "0.0";
                    String qtrDevPer = "0.0";
                    String yearDevPer = "0.0";
                    String monthTarget = "--";
                    String dayTarget = "--";
                    String qtrTarget = "--";
                    String yearTarget1 = "--";
                    String monthDevVal = "--";
                    String dayDevVal = "--";
                    String qtrDevVal = "--";
                    String yearDevVal = "--";
                    int decimalplaces1 = 0;
                    DecimalFormat oneDForm = new DecimalFormat("#.0");
                    BigDecimal bd;
                    NumberFormatter nf = new NumberFormatter();
                    int index;
                    String AbsMntcurrVal;
                    String QtrTargetval;
                    String AbsQtrCurrVal;
                    String AbsYrCurrVal;
                    if (TargetInfo.get("targetElementId") != null) {
                        targetElementId = TargetInfo.get("targetElementId").toString();
                    }
                    if (multiKpiDetailsMap.get("AbsMntCurrVal") != null) {
                        AbsMntcurrVal = multiKpiDetailsMap.get("AbsMntCurrVal").toString();
                    } else {
                        AbsMntcurrVal = "0.0";
                    }
                    if (multiKpiDetailsMap.get("AbsQtrCurrVal") != null) {
                        AbsQtrCurrVal = multiKpiDetailsMap.get("AbsQtrCurrVal").toString();
                    } else {
                        AbsQtrCurrVal = "0.0";
                    }
                    if (multiKpiDetailsMap.get("AbsYrCurrVal") != null) {
                        AbsYrCurrVal = multiKpiDetailsMap.get("AbsYrCurrVal").toString();
                    } else {
                        AbsYrCurrVal = "0.0";
                    }
                    if (TargetInfo.get("QtrTargetVal") != null) {
                        QtrTargetval = TargetInfo.get("QtrTargetVal").toString();
                    } else {
                        QtrTargetval = "0.0";
                    }

                    if (multiKpiDetailsMap.get("MonthCustomTargetVal") != null) {
                        bd = new BigDecimal(Double.parseDouble(multiKpiDetailsMap.get("MonthCustomTargetVal").toString()));
                        bd = bd.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);

                        monthTarget = bd.toString();

//                        double val = new Double(bd.toString());
                        double val = Double.parseDouble(bd.toString());
                        BigDecimal DEVAL = kpiDetail.getDeviationVal(new BigDecimal(AbsMntcurrVal), new BigDecimal(multiKpiDetailsMap.get("MonthCustomTargetVal").toString()));
                        DEVAL = DEVAL.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);

                        monthDevVal = DEVAL.toString();


                        BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsMntcurrVal.toString()), Double.parseDouble(multiKpiDetailsMap.get("MonthCustomTargetVal").toString()));
                        monthDevPer = String.valueOf(devPer);
                        monthDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
                        if (monthDevPer.contains("M")) {
                            monthDevPer = (devPer) + "%";
                        }

                    } else if (TargetInfo.get("MonthTarget") != null) {
                        bd = new BigDecimal(Double.parseDouble(TargetInfo.get("MonthTarget").toString()));
                        bd = bd.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);

                        monthTarget = bd.toString();

                        double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(Double.parseDouble(AbsMntcurrVal))), Double.parseDouble(TargetInfo.get("MonthTarget").toString()));

                        monthDevVal = Double.toString(DEVAL);

                        BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsMntcurrVal), Double.parseDouble(TargetInfo.get("MonthTarget").toString()));
                        monthDevPer = String.valueOf(devPer);
                        monthDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
                        if (monthDevPer.contains("M")) {
                            monthDevPer = (devPer) + "%";
                        }

                    } else {
                        // you can add some more options for getting the target
                    }

                    // for Day level
                    if (TargetInfo.get("DayTargetVal") != null) {
                        bd = new BigDecimal(Double.parseDouble(TargetInfo.get("DayTargetVal").toString()));
                        bd = bd.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);
//             NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//             dayTarget = formatter.format(bd);
                        double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(Double.parseDouble(AbsQtrCurrVal))), Double.parseDouble(QtrTargetval));
                        dayDevVal = Double.toString(DEVAL);
                        dayTarget = bd.toString();
                        BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsQtrCurrVal), Double.parseDouble(QtrTargetval));
                        dayDevPer = String.valueOf(devPer);
                        dayDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
                        if (dayDevPer.contains("M")) {
                            dayDevPer = (devPer) + "%";
                        }

                    }

                    if (multiKpiDetailsMap.get("QtrCustomTargetVal") != null) {
                        bd = new BigDecimal(Double.parseDouble(multiKpiDetailsMap.get("QtrCustomTargetVal").toString()));
                        bd = bd.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);
                        double qtrTargetVal = Double.parseDouble(multiKpiDetailsMap.get("QtrCustomTargetVal").toString());
                        qtrTarget = bd.toString();

//             NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//             qtrTarget = formatter.format(bd);
                        BigDecimal DEVAL = kpiDetail.getDeviationVal(new BigDecimal(AbsQtrCurrVal), new BigDecimal(multiKpiDetailsMap.get("QtrCustomTargetVal").toString()));
                        DEVAL = DEVAL.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);
                        qtrDevVal = DEVAL.toString();


                        BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsQtrCurrVal), Double.parseDouble(multiKpiDetailsMap.get("QtrCustomTargetVal").toString()));
                        qtrDevPer = String.valueOf(devPer);
                        qtrDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
                        if (qtrDevPer.contains("M")) {
                            qtrDevPer = (devPer) + "%";
                        }
                    } else if (TargetInfo.get("QtrTargetVal") != null) {
                        bd = new BigDecimal(Double.parseDouble(TargetInfo.get("QtrTargetVal").toString()));
                        bd = bd.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);

                        qtrTarget = bd.toString();
                        double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(Double.parseDouble(AbsQtrCurrVal))), Double.parseDouble(TargetInfo.get("QtrTargetVal").toString()));

                        qtrDevVal = Double.toString(DEVAL);

                        BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsQtrCurrVal), Double.parseDouble(TargetInfo.get("QtrTargetVal").toString()));
                        qtrDevPer = String.valueOf(devPer);
                        qtrDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
                        if (qtrDevPer.contains("M")) {
                            qtrDevPer = (devPer) + "%";
                        }
                    } else {
                    }

                    if (multiKpiDetailsMap.get("YearCustomTargetVal") != null) {
                        bd = new BigDecimal(Double.parseDouble(multiKpiDetailsMap.get("YearCustomTargetVal").toString()));
                        bd = bd.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);
                        double yearTargetVal = Double.parseDouble(multiKpiDetailsMap.get("YearCustomTargetVal").toString());

                        yearTarget1 = bd.toString();
                        BigDecimal DEVAL = kpiDetail.getDeviationVal(new BigDecimal(AbsYrCurrVal), new BigDecimal(multiKpiDetailsMap.get("YearCustomTargetVal").toString()));
                        DEVAL = DEVAL.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);
                        yearDevVal = DEVAL.toString();



                        BigDecimal devPer = kpiDetail.getDeviationPer(Math.abs(Double.parseDouble(AbsYrCurrVal)), Math.abs(Double.parseDouble(multiKpiDetailsMap.get("YearCustomTargetVal").toString())));
                        yearDevPer = String.valueOf(devPer);
                        yearDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
                        if (yearDevPer.contains("M")) {
                            yearDevPer = String.valueOf(devPer) + "%";
                        }
                    } else if (TargetInfo.get("yearTarget") != null) {
                        bd = new BigDecimal(Double.parseDouble(TargetInfo.get("yearTarget").toString()));
                        bd = bd.setScale(decimalplaces1, BigDecimal.ROUND_HALF_UP);
                        double yrTargetVal = Double.parseDouble(TargetInfo.get("yearTarget").toString());

                        yearTarget1 = bd.toString();
                        double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(Double.parseDouble(AbsYrCurrVal))), Double.parseDouble(TargetInfo.get("yearTarget").toString()));

                        yearDevVal = Double.toString(DEVAL);

                        BigDecimal devPer = kpiDetail.getDeviationPer(Math.abs(Double.parseDouble(AbsYrCurrVal)), Math.abs(Double.parseDouble(TargetInfo.get("yearTarget").toString())));
                        yearDevPer = String.valueOf(devPer);
                        yearDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
                        if (yearDevPer.contains("M")) {
                            yearDevPer = devPer + "%";
                        }
                    } else {
                    }


                    if (typeKPI.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                        ArrayList<String> tempList = new ArrayList<String>();
                        String groupname = "";
                        List<KPISingleGroupHelper> kPISingleGroupHelpers = dashletdetail.getSingleGroupHelpers();
                        String curval = "0";
                        cell = new Label(0, k + rowStart + 1, elementName);
                        cell.setCellFormat(writableCellFormat);
                        writableSheet.addCell(cell);
                        List<KPISingleGroupHelper> kPISingleGroupHelpers1 = dashletdetail.getSingleGroupHelpers();
                        for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers1) {
                            if (groupingHelper.getGroupName() != null) {
                                groupname = groupingHelper.getGroupName();

                            }
                        }
                        if (isGroupElement && group != null && !group.isEmpty()) {
                            cell = new Label(0, k + rowStart + 1, elementId);
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                        if (kpiDetail.isMTDChecked() && !isGroupElement) {
                            if (multiKpiDetailsMap.get("MonthCurrentVal") != null) {
                                curval = multiKpiDetailsMap.get("MonthCurrentVal").toString();
                            }
                            tempList.add(curval);
                            tempList.add(monthTarget);
                            if (multiKpiDetailsMap.get("MonthPriorVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("MonthPriorVal").toString());
                            } else {
                                tempList.add("0");
                            }
                            tempList.add(monthDevVal);
                            tempList.add(monthDevPer);
                            if (multiKpiDetailsMap.get("MonthChangeVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("MonthChangeVal").toString());
                            } else {
                                tempList.add("0");
                            }
                            if (multiKpiDetailsMap.get("MonthChangePercenVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("MonthChangePercenVal").toString());
                            } else {
                                tempList.add("0");
                            }

                        }
                        if (kpiDetail.isQTDChecked() && !isGroupElement) {
                            if (multiKpiDetailsMap.get("QtrCurrentVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("QtrCurrentVal").toString());
                            } else {
                                tempList.add("0");
                            }
                            tempList.add(qtrTarget);
                            if (multiKpiDetailsMap.get("QtrPriorVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("QtrPriorVal").toString());
                            } else {
                                tempList.add("0");
                            }
                            tempList.add(qtrDevVal);
                            tempList.add(qtrDevPer);
                            if (multiKpiDetailsMap.get("QtrChangeVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("QtrChangeVal").toString());
                            } else {
                                tempList.add("0");
                            }
                            if (multiKpiDetailsMap.get("QtrChangePercenVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("QtrChangePercenVal").toString());
                            } else {
                                tempList.add("0");
                            }
                        }
                        if (kpiDetail.isYTDChecked() && !isGroupElement) {
                            if (multiKpiDetailsMap.get("YearCurrentVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("YearCurrentVal").toString());
                            } else {
                                tempList.add("0");
                            }
                            tempList.add(yearTarget1);
                            if (multiKpiDetailsMap.get("YearPriorVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("YearPriorVal").toString());
                            } else {
                                tempList.add("0");
                            }
                            tempList.add(yearDevVal);
                            tempList.add(yearDevPer);
                            if (multiKpiDetailsMap.get("YearchangeVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("YearchangeVal").toString());
                            } else {
                                tempList.add("0");
                            }
                            if (multiKpiDetailsMap.get("YearChangePercenVal") != null) {
                                tempList.add(multiKpiDetailsMap.get("YearChangePercenVal").toString());
                            } else {
                                tempList.add("0");
                            }


                        }

                        if (isGroupElement && group != null && !group.isEmpty()) {
                            for (int j = 0; j < group.size(); j++) {
                                cell = new Label(j + 1, k + rowStart + 1, group.get(j));
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        } else {
                            for (int j = 0; j < tempList.size(); j++) {
                                cell = new Label(j + 1, k + rowStart + 1, tempList.get(j));
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        }

                    }

                }
            }
            if (dashletdetail.getKpiType().equalsIgnoreCase("MultiPeriod")) {
                if (container.getMultiPeriodKPI() != null) {
                    StringBuilder kpiheads = new StringBuilder();
                    ArrayList<String> labelList = new ArrayList<String>();
                    if (dashletdetail.getKpiheads().isEmpty() || dashletdetail.getKpiheads().get(0).equalsIgnoreCase("")) {
                        kpiheads.append(",").append("KPI");
                        kpiheads.append(",").append("Current Day");
                        kpiheads.append(",").append("MTD");
                        kpiheads.append(",").append("QTD");
                        kpiheads.append(",").append("YTD");
                    } else {
                        for (int i = 0; i < dashletdetail.getKpiheads().size(); i++) {
                            kpiheads.append(",").append(dashletdetail.getKpiheads().get(i));

                        }
                    }
                    kpiheads.replace(0, 1, "");
                    String customkpiheads = kpiheads.toString();
                    String[] Kpiheadarray = customkpiheads.split(",");
                    labelList.add(Kpiheadarray[0]);
                    if (kpiDetail.isCurrentChecked()) {
                        labelList.add(Kpiheadarray[1]);
                    }
                    if (kpiDetail.isMTDChecked()) {
                        labelList.add(Kpiheadarray[2]);
                    }
                    if (kpiDetail.isQTDChecked()) {
                        labelList.add(Kpiheadarray[3]);
                    }
                    if (kpiDetail.isYTDChecked()) {
                        labelList.add(Kpiheadarray[4]);
                    }
                    for (int i = 0; i < labelList.size(); i++) {
                        label = new Label(i, rowStart, labelList.get(i));
                        label.setCellFormat(writableCellFormat);
                        writableSheet.addCell(label);

                        HeadingCellView = new CellView();
                        HeadingCellView.setSize(256 * 30);
                        writableSheet.setColumnView(i, HeadingCellView);
                    }
                    writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                    writableCellFormat = new WritableCellFormat(writableFont);
                    writableCellFormat.setWrap(true);

                    for (int k = 0; k < a1.size(); k++) {
                        String ElementId = a1.get(k);
                        List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);
                        String elementId = a1.get(k);
                        String elementName = "";
                        String value = "";
                        String kpiMasterId = dashletdetail.getKpiMasterId();
                        NumberFormatter nf = new NumberFormatter();
                        String drillReportId = kpiDetail.getKPIDrill(ElementId);
                        ArrayList<String> tempList = new ArrayList<String>();
                        int i = 0;
                        Double currValMul = 0.0;
                        int decimalplaces = 2;
                        boolean isGroupElement = dashletdetail.isGroupElement(elementId);
                        List<KPIElement> elements = kpiElementMap.get(elementId);
                        if (!isGroupElement) {
                            for (KPIElement elem : elements) {
                                if (elem != null && elem.getElementId() != null && !elem.getElementId().equalsIgnoreCase("")) {
                                    elementName = elem.getElementName();
                                    isGroupElement = elem.isIsGroupElement();
                                    break;
                                }
                            }
                        }
                        if (isGroupElement) {
                            if (dashletdetail.getSingleGroupHelpers() != null && !dashletdetail.getSingleGroupHelpers().isEmpty()) {
                                List<KPISingleGroupHelper> kPISingleGroupHelpers = dashletdetail.getSingleGroupHelpers();
                                for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                                    BigDecimal sumBd = new BigDecimal(0);
                                    if (groupingHelper.getGroupName() != null) {
                                        if (groupingHelper.getGroupName().equalsIgnoreCase(elementId)) {
                                            if (groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()) != null) {
                                                kpiDetail.addKPIDrill(groupingHelper.getGroupName(), groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()));
                                            }
                                            group = getGroupVal(groupingHelper, pbretObj, 1, kpiMasterId, dashletdetail, dashboardId, collect);
                                        }
                                    }
                                }
                            }

                        }
                        if (!isGroupElement) {
                            for (i = 0; i < kpiElements.size(); i++) {

                                //if(kpiElements.size() > 1){
                                String temp = "A_" + kpiElements.get(i).getElementId();
                                String type = kpiElements.get(i).getRefElementType();
                                if (container.getMultiPeriodKPI().getDayObject() != null && (container.getMultiPeriodKPI().getDayObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getDayObject().getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currValMul = Double.parseDouble((container.getMultiPeriodKPI().getDayObject().getFieldValueString(0, temp)));
                                        BigDecimal bd4 = new BigDecimal(currValMul);
                                        bd4 = bd4.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                        if (kpiDetail.isCurrentChecked()) {
                                            tempList.add(formatter.format(bd4));
                                        }
                                    }
                                }
                                if (container.getMultiPeriodKPI().getMonthObject() != null && (container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currValMul = Double.parseDouble((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)));
                                        BigDecimal bd3 = new BigDecimal(currValMul);
                                        bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                        if (kpiDetail.isMTDChecked()) {
                                            tempList.add(formatter.format(bd3));
                                        }
                                    }
                                }
                                if (container.getMultiPeriodKPI().getQuarterObject() != null && (container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currValMul = Double.parseDouble((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)));
                                        if (kpiDetail.isQTDChecked()) {
                                            BigDecimal bd2 = new BigDecimal(currValMul);
                                            bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                            if (kpiDetail.isQTDChecked()) {
                                                tempList.add(formatter.format(bd2));
                                            }
                                        }
                                    }
                                }
                                if (container.getMultiPeriodKPI().getYearObject() != null && (container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currValMul = Double.parseDouble((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)));
                                        BigDecimal bd1 = new BigDecimal(currValMul);
                                        bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                        if (kpiDetail.isYTDChecked()) {
                                            tempList.add(formatter.format(bd1));
                                        }
                                    }
                                }
                            }
                        }
                        if (isGroupElement && group != null && !group.isEmpty()) {
                            cell = new Label(0, k + rowStart + 1, elementId);
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        } else {
                            cell = new Label(0, k + rowStart + 1, elementName);
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                        if (isGroupElement && group != null && !group.isEmpty()) {
                            for (int j = 0; j < group.size(); j++) {
                                cell = new Label(j + 1, k + rowStart + 1, group.get(j));
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        } else {
                            for (int j = 0; j < tempList.size(); j++) {
                                cell = new Label(j + 1, k + rowStart + 1, tempList.get(j));
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        }
                    }
                }

            }
            if (dashletdetail.getKpiType().equalsIgnoreCase("BasicTarget")) {
                StringBuilder kpiheads = new StringBuilder();
                ArrayList<String> labelList = new ArrayList<String>();
                if (dashletdetail.getKpiheads().isEmpty() || dashletdetail.getKpiheads().get(0).equalsIgnoreCase("")) {
                    kpiheads.append(",").append("KPI");
                    kpiheads.append(",").append("Value");
                    kpiheads.append(",").append("Target");
                    kpiheads.append(",").append("Deviation");
                    kpiheads.append(",").append("Deviation%");
                } else {
                    for (int i = 0; i < dashletdetail.getKpiheads().size(); i++) {
                        kpiheads.append(",").append(dashletdetail.getKpiheads().get(i));

                    }
                }
                kpiheads.replace(0, 1, "");
                String customkpiheads = kpiheads.toString();
                String[] Kpiheadarray = customkpiheads.split(",");
                labelList.add(Kpiheadarray[0]);
                labelList.add(Kpiheadarray[1]);
                labelList.add(Kpiheadarray[2]);
                labelList.add(Kpiheadarray[3]);
                labelList.add(Kpiheadarray[4]);
                for (int i = 0; i < labelList.size(); i++) {
                    label = new Label(i, rowStart, labelList.get(i));
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);

                    HeadingCellView = new CellView();
                    HeadingCellView.setSize(256 * 30);
                    writableSheet.setColumnView(i, HeadingCellView);
                }
                writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setWrap(true);
                for (int k = 0; k < a1.size(); k++) {
                    String ElementId = a1.get(k);
                    List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);
                    String elementId = a1.get(k);
                    String elementName = "";
                    String value = "";
                    String kpiMasterId = dashletdetail.getKpiMasterId();
                    NumberFormatter nf = new NumberFormatter();
                    ArrayList<String> tempList = new ArrayList<String>();
                    Double currValMul = 0.0;
                    int decimalplaces = 2;
                    DecimalFormat oneDForm = new DecimalFormat("#.0");
                    int sum = 0;
                    double currVal = 0.0;
                    double priorVal = 0.0;
                    String devVal = "--";
                    String basicDevVal = "--";
                    String basicDevPer = "0.0";
                    String targetValueStr = "--";
                    String BasicTvalue = "0";
                    HashMap basicTargetDetails = new HashMap();
                    boolean isGroupElement = dashletdetail.isGroupElement(elementId);
                    List<KPIElement> elements = kpiElementMap.get(elementId);
                    if (!isGroupElement) {
                        for (KPIElement elem : elements) {
                            if (elem != null && elem.getElementId() != null && !elem.getElementId().equalsIgnoreCase("")) {
                                elementName = elem.getElementName();
                                isGroupElement = elem.isIsGroupElement();
                                break;
                            }
                        }
                    }
                    if (isGroupElement) {
                        if (dashletdetail.getSingleGroupHelpers() != null && !dashletdetail.getSingleGroupHelpers().isEmpty()) {
                            List<KPISingleGroupHelper> kPISingleGroupHelpers = dashletdetail.getSingleGroupHelpers();
                            for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                                BigDecimal sumBd = new BigDecimal(0);
                                if (groupingHelper.getGroupName() != null) {
                                    if (groupingHelper.getGroupName().equalsIgnoreCase(elementId)) {
                                        if (groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()) != null) {
                                            kpiDetail.addKPIDrill(groupingHelper.getGroupName(), groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()));
                                        }
                                        group = getGroupVal(groupingHelper, pbretObj, 1, kpiMasterId, dashletdetail, dashboardId, collect);
                                    }
                                }
                            }
                        }

                    }

                    for (int i = 0; i < kpiElements.size(); i++) {

                        //For Each element id try to value from retObjQry  and build a table row  here
                        if (pbretObj.getRowCount() > 0) {
                            String temp = "A_" + kpiElements.get(i).getElementId();
                            String type = kpiElements.get(i).getRefElementType();
                            if (kpiElements.size() > 1) {
                                if ((pbretObj.getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(pbretObj.getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currVal = Double.parseDouble((pbretObj.getFieldValueString(0, temp)));
                                        BigDecimal bd = new BigDecimal(currVal);
                                        bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                        tempList.add(bd.toString());

                                    } else if (type.equalsIgnoreCase("2")) {
                                        String priorValStr = pbretObj.getFieldValueString(0, temp);
                                        if (priorValStr != null && !("".equals(priorValStr))) {
                                            priorVal = Double.parseDouble(priorValStr);
                                        }
                                        double[] valArray = {currVal, priorVal};
                                        for (int j = 0; j < valArray.length; j++) {
                                            sum += (valArray[j]);
                                        }
                                        for (int kpi = 0; kpi < valArray.length; kpi++) {
                                            BasicTvalue = pbretObj.getModifiedNumber(new BigDecimal(valArray[kpi]));
                                            if (priorVal == 0.0) {
                                                if (kpi == 1) {
                                                    continue;
                                                }
                                            }
                                        }
                                    } else {
                                        String dev = "--";
                                        int decimalPlaces = 1;
                                        BigDecimal bd = new BigDecimal(currVal);
                                        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                        basicTargetDetails.put("currentValue", formatter.format(bd));
                                        ArrayList timeDim = collect.timeDetailsArray;
                                        String timeLevel = timeDim.get(3).toString();
                                        Double targetValue = kpiDetail.getTargetValue(ElementId, timeLevel);
                                        if (targetValue == null) {
                                            PbTimeRanges timeRanges = new PbTimeRanges();
                                            timeRanges.setElementID(ElementId);
                                            String currPeriodName = timeRanges.getCurrentPeriodName(timeDim.get(2).toString(), timeLevel);
                                            BigDecimal targetData = kpiDetail.getTargetData(ElementId, timeLevel, currPeriodName);
                                            if (targetData != null) {
                                                targetValue = targetData.doubleValue();
                                            }

                                        }

                                        if (targetValue != null) {
                                            double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(currVal)), targetValue);
                                            basicDevVal = String.valueOf(DEVAL);
                                            basicDevVal = NumberFormatter.getModifiedNumber(new BigDecimal(basicDevVal));
                                            //double DEVAL1 = Double.valueOf(basicDevVal);
                                            BigDecimal DEVPER = kpiDetail.getDeviationPer(currVal, targetValue);
                                            basicDevPer = String.valueOf(DEVPER);
                                            basicDevPer = NumberFormatter.getModifiedNumber(DEVPER) + "%";
                                            if (basicDevPer.contains("M")) {
                                                basicDevPer = (DEVPER) + "%";
                                            }
                                            if (basicDevVal.equalsIgnoreCase("-0.0")) {
                                                basicDevVal = "0.0";
                                                basicDevPer = "0.0";
                                            }


                                        }

                                        double perDev = 0;
                                        String timeDimVal = collect.timeDetailsArray.get(1).toString();
                                        double tempDouble = 0.0;
                                        if (targetValue != null) {
                                            if (targetValue != 0) {
                                                devVal = String.valueOf(((currVal - targetValue) / targetValue) * 100);
                                            } else {
                                                devVal = String.valueOf((currVal) * 100);
                                            }
                                            perDev = new Double(devVal);
                                            devVal = NumberFormatter.getModifiedNumber(new BigDecimal(devVal)) + "%";
                                            String currentStr = targetValue.toString();
                                            String pattern = "###,###";
                                            double value1 = Double.parseDouble(currentStr);
                                            DecimalFormat myFormatter = new DecimalFormat(pattern);
                                            String output = myFormatter.format(value1);
                                            targetValueStr = output;

                                            if (targetValueStr.charAt(targetValueStr.length() - 1) == ',') {
                                                targetValueStr = targetValueStr.substring(0, targetValueStr.length() - 1);
                                            }

                                        }


                                    }
                                }
                            }
                        }

                    }
                    tempList.add(targetValueStr);
                    tempList.add(basicDevVal);
                    tempList.add(basicDevPer);
                    if (isGroupElement && group != null && !group.isEmpty()) {
                        cell = new Label(0, k + rowStart + 1, elementId);
                        cell.setCellFormat(writableCellFormat);
                        writableSheet.addCell(cell);
                    } else {
                        cell = new Label(0, k + rowStart + 1, elementName);
                        cell.setCellFormat(writableCellFormat);
                        writableSheet.addCell(cell);
                    }
                    if (isGroupElement && group != null && !group.isEmpty()) {
                        for (int j = 0; j < group.size(); j++) {
                            cell = new Label(j + 1, k + rowStart + 1, group.get(j));
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                    } else {
                        for (int j = 0; j < tempList.size(); j++) {
                            cell = new Label(j + 1, k + rowStart + 1, tempList.get(j));
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                    }

                }
            }

            if (dashletdetail.getKpiType().equalsIgnoreCase("Standard")) {
                StringBuilder kpiheads = new StringBuilder();
                ArrayList<String> labelList = new ArrayList<String>();
                if (dashletdetail.getKpiheads().isEmpty() || dashletdetail.getKpiheads().get(0).equalsIgnoreCase("")) {
                    kpiheads.append(",").append("KPI");
                    kpiheads.append(",").append("Current");
                    kpiheads.append(",").append("Prior");
                    kpiheads.append(",").append("Change%");
                } else {
                    for (int i = 0; i < 3; i++) {
                        kpiheads.append(",").append(dashletdetail.getKpiheads().get(i));

                    }
                }
                kpiheads.replace(0, 1, "");
                String customkpiheads = kpiheads.toString();
                String[] Kpiheadarray = customkpiheads.split(",");

                labelList.add(Kpiheadarray[0]);
                labelList.add(Kpiheadarray[1]);
                if (Kpiheadarray.length == 3) {
                    labelList.add(" ");
                } else {
                    labelList.add(Kpiheadarray[2]);
                }

                if (Kpiheadarray.length == 3) {
                    labelList.add(Kpiheadarray[2]);
                } else {
                    labelList.add(Kpiheadarray[3]);
                }
                for (int i = 0; i < labelList.size(); i++) {
                    label = new Label(i, rowStart, labelList.get(i));
                    int heightInPoints = 26 * 15;
                    writableSheet.setRowView(0, heightInPoints);
                    WritableCellFormat newFormat = new WritableCellFormat();
                    newFormat.setFont(writableFont);
                    newFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                    newFormat.setBackground(Colour.GRAY_50);
                    label.setCellFormat(newFormat);
                    writableSheet.addCell(label);
                    if (i == 0) {
                        HeadingCellView = new CellView();
                        HeadingCellView.setSize(256 * 30);

                        writableSheet.setColumnView(i, HeadingCellView);
                    } else {
                        HeadingCellView = new CellView();
                        HeadingCellView.setSize(256 * 20);
                        writableSheet.setColumnView(i, HeadingCellView);
                    }

                }
                writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setWrap(true);

                for (int k = 0; k < a1.size(); k++) {
                    String ElementId = a1.get(k);
                    List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);
                    String elementId = a1.get(k);
                    String elementName = "";
                    String value = "";
                    String kpiMasterId = dashletdetail.getKpiMasterId();
                    NumberFormatter nf = new NumberFormatter();
                    ArrayList<String> tempList = new ArrayList<String>();
                    int decimalplaces = 2;
                    int sum = 0;
                    double currVal = 0.0;
                    double priorVal = 0.0;
                    String BasicTvalue = "0";
                    StringBuffer val = new StringBuffer();
                    boolean isGroupElement = dashletdetail.isGroupElement(elementId);
                    List<KPIElement> elements = kpiElementMap.get(elementId);
                    if (!isGroupElement) {
                        for (KPIElement elem : elements) {
                            if (elem != null && elem.getElementId() != null && !elem.getElementId().equalsIgnoreCase("")) {

                                elementName = elem.getElementName();
                                isGroupElement = elem.isIsGroupElement();
                                break;
                            }
                        }
                    }
                    if (isGroupElement) {
                        if (dashletdetail.getSingleGroupHelpers() != null && !dashletdetail.getSingleGroupHelpers().isEmpty()) {
                            List<KPISingleGroupHelper> kPISingleGroupHelpers = dashletdetail.getSingleGroupHelpers();
                            for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                                BigDecimal sumBd = new BigDecimal(0);
                                if (groupingHelper.getGroupName() != null) {
                                    if (groupingHelper.getGroupName().equalsIgnoreCase(elementId)) {
                                        if (groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()) != null) {
                                            kpiDetail.addKPIDrill(groupingHelper.getGroupName(), groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()));
                                        }
                                        group = getGroupVal(groupingHelper, pbretObj, 1, kpiMasterId, dashletdetail, dashboardId, collect);
                                    }
                                }
                            }
                        }

                    }

                    for (int i = 0; i < kpiElements.size(); i++) {

                        //For Each element id try to value from retObjQry  and build a table row  here
                        if (pbretObj.getRowCount() > 0) {
                            String temp = "A_" + kpiElements.get(i).getElementId();
                            String type = kpiElements.get(i).getRefElementType();
                            if (kpiElements.size() > 1) {
                                if ((pbretObj.getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(pbretObj.getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currVal = Double.parseDouble((pbretObj.getFieldValueString(0, temp)));

                                    } else if (type.equalsIgnoreCase("2")) {
                                        String priorValStr = pbretObj.getFieldValueString(0, temp);
                                        if (priorValStr != null && !("".equals(priorValStr))) {
                                            priorVal = Double.parseDouble(priorValStr);
                                        }
                                        double[] valArray = {currVal, priorVal};
                                        double[] celWidth = new double[valArray.length];
                                        for (int j = 0; j < valArray.length; j++) {
                                            sum += (valArray[j]);
                                        }
                                        for (int kpi = 0; kpi < valArray.length; kpi++) {
                                            BigDecimal bd = new BigDecimal(valArray[kpi]);
                                            int decimalPlaces = 0;
                                            BasicTvalue = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).toString();
                                            if (priorVal == 0.0) {
                                                if (kpi == 1) {
                                                    continue;
                                                }
                                            }
                                            tempList.add(BasicTvalue);

                                        }
                                        //tempList.add(val.substring(1));
                                    } else if (type.equalsIgnoreCase("3")) {
                                    } else {
                                        double r = Double.parseDouble((pbretObj.getFieldValueString(0, temp)));
                                        int decimalPlaces = 2;
                                        BigDecimal bd = new BigDecimal(r);
                                        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                                        tempList.add(bd.toString());
                                    }
                                }
                            }
                        }
                    }

                    if (isGroupElement && group != null && !group.isEmpty()) {
                        int heightInPoints = 26 * 15;
                        writableSheet.setRowView(k + rowStart + 1, heightInPoints);
                        cell = new Label(0, k + rowStart + 1, elementId);
                        WritableCellFormat newFormat = new WritableCellFormat();
                        newFormat.setFont(writableFont);
                        newFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                        newFormat.setAlignment(Alignment.LEFT);
                        cell.setCellFormat(newFormat);
                        writableSheet.addCell(cell);
                    } else {
                        cell = new Label(0, k + rowStart + 1, elementName);
                        int heightInPoints = 26 * 15;
                        writableSheet.setRowView(k + rowStart + 1, heightInPoints);
                        writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                        WritableCellFormat newFormat = new WritableCellFormat();
                        newFormat.setFont(writableFont);
                        newFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                        newFormat.setAlignment(Alignment.LEFT);
                        cell.setCellFormat(newFormat);
                        //cell.setCellFormat(writableCellFormat);
                        writableSheet.addCell(cell);
                    }
                    if (isGroupElement && group != null && !group.isEmpty()) {
                        for (int j = 0; j < group.size(); j++) {
                            cell = new Label(j + 1, k + rowStart + 1, group.get(j));
                            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                            WritableCellFormat newFormat = new WritableCellFormat();
                            newFormat.setFont(writableFont);
                            newFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                            newFormat.setAlignment(Alignment.LEFT);
                            cell.setCellFormat(newFormat);

                            writableSheet.addCell(cell);
                        }
                    } else {
                        for (int j = 0; j < tempList.size(); j++) {
                            cell = new Label(j + 1, k + rowStart + 1, tempList.get(j));
                            if (j == 0) {
                                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, j == 0, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
                                WritableCellFormat newFormat = new WritableCellFormat();
                                newFormat.setBackground(Colour.LIGHT_BLUE);
                                newFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                                newFormat.setFont(writableFont);
                                newFormat.setAlignment(Alignment.CENTRE);
                                cell.setCellFormat(newFormat);
                            } else if (j == 1) {
                                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, j == 1, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
                                WritableCellFormat newFormat = new WritableCellFormat();
                                newFormat.setBackground(Colour.GREEN);
                                newFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                                newFormat.setFont(writableFont);
                                newFormat.setAlignment(Alignment.CENTRE);
                                cell.setCellFormat(newFormat);
                            } else {

                                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                                WritableCellFormat newFormat = new WritableCellFormat();
                                newFormat.setFont(writableFont);
                                newFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                                newFormat.setAlignment(Alignment.CENTRE);
                                cell.setCellFormat(newFormat);;
                            }
                            writableSheet.addCell(cell);
                        }
                    }

                }
            }
            if (dashletdetail.getKpiType().equalsIgnoreCase("Basic")) {
                StringBuilder kpiheads = new StringBuilder();
                ArrayList<String> labelList = new ArrayList<String>();
                if (dashletdetail.getKpiheads().isEmpty() || dashletdetail.getKpiheads().get(0).equalsIgnoreCase("")) {
                    kpiheads.append(",").append("KPI");
                    kpiheads.append(",").append("Value");
                } else {
                    for (int i = 0; i < dashletdetail.getKpiheads().size(); i++) {
                        kpiheads.append(",").append(dashletdetail.getKpiheads().get(i));

                    }
                }
                kpiheads.replace(0, 1, "");
                String customkpiheads = kpiheads.toString();
                String[] Kpiheadarray = customkpiheads.split(",");
                labelList.add(Kpiheadarray[0]);
                labelList.add(Kpiheadarray[1]);
                for (int i = 0; i < labelList.size(); i++) {
                    label = new Label(i, rowStart, labelList.get(i));
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);

                    HeadingCellView = new CellView();
                    HeadingCellView.setSize(256 * 30);
                    writableSheet.setColumnView(i, HeadingCellView);

                }
                writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setWrap(true);
                for (int k = 0; k < a1.size(); k++) {
                    String ElementId = a1.get(k);
                    List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);
                    String elementId = a1.get(k);
                    String elementName = "";
                    String value = "";
                    String kpiMasterId = dashletdetail.getKpiMasterId();
                    NumberFormatter nf = new NumberFormatter();
                    ArrayList<String> tempList = new ArrayList<String>();
                    int decimalplaces = 2;
                    int sum = 0;
                    double currVal = 0.0;
                    double priorVal = 0.0;
                    String BasicTvalue = "0";
                    StringBuffer val = new StringBuffer();
                    boolean isGroupElement = dashletdetail.isGroupElement(elementId);
                    List<KPIElement> elements = kpiElementMap.get(elementId);
                    if (!isGroupElement) {
                        for (KPIElement elem : elements) {
                            if (elem != null && elem.getElementId() != null && !elem.getElementId().equalsIgnoreCase("")) {

                                elementName = elem.getElementName();
                                isGroupElement = elem.isIsGroupElement();
                                break;
                            }
                        }
                    }
                    if (isGroupElement) {
                        if (dashletdetail.getSingleGroupHelpers() != null && !dashletdetail.getSingleGroupHelpers().isEmpty()) {
                            List<KPISingleGroupHelper> kPISingleGroupHelpers = dashletdetail.getSingleGroupHelpers();
                            for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                                BigDecimal sumBd = new BigDecimal(0);
                                if (groupingHelper.getGroupName() != null) {
                                    if (groupingHelper.getGroupName().equalsIgnoreCase(elementId)) {
                                        if (groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()) != null) {
                                            kpiDetail.addKPIDrill(groupingHelper.getGroupName(), groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()));
                                        }
                                        group = getGroupVal(groupingHelper, pbretObj, 1, kpiMasterId, dashletdetail, dashboardId, collect);
                                    }
                                }
                            }
                        }

                    }

                    for (int i = 0; i < kpiElements.size(); i++) {

                        //For Each element id try to value from retObjQry  and build a table row  here
                        if (pbretObj.getRowCount() > 0) {
                            String temp = "A_" + kpiElements.get(i).getElementId();
                            String type = kpiElements.get(i).getRefElementType();
                            if (kpiElements.size() > 1) {
                                if ((pbretObj.getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(pbretObj.getFieldValueString(0, temp)))) {
                                    if (type.equalsIgnoreCase("1")) {
                                        currVal = Double.parseDouble((pbretObj.getFieldValueString(0, temp)));
                                        BigDecimal bd = new BigDecimal(currVal);
                                        bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                        tempList.add(bd.toString());

                                    } else if (type.equalsIgnoreCase("2")) {
                                    } else if (type.equalsIgnoreCase("3")) {
                                    }
                                }
                            }
                        }
                    }

                    if (isGroupElement && group != null && !group.isEmpty()) {
                        cell = new Label(0, k + rowStart + 1, elementId);
                        cell.setCellFormat(writableCellFormat);
                        writableSheet.addCell(cell);
                    } else {
                        cell = new Label(0, k + rowStart + 1, elementName);
                        cell.setCellFormat(writableCellFormat);
                        writableSheet.addCell(cell);
                    }
                    if (isGroupElement && group != null && !group.isEmpty()) {
                        for (int j = 0; j < group.size(); j++) {
                            cell = new Label(j + 1, k + rowStart + 1, group.get(j));
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                    } else {
                        for (int j = 0; j < tempList.size(); j++) {
                            cell = new Label(j + 1, k + rowStart + 1, tempList.get(j));
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                    }

                }

            }
            if (dashletdetail.getKpiType().equalsIgnoreCase("Target")) {
                StringBuilder kpiheads = new StringBuilder();
                ArrayList<String> labelList = new ArrayList<String>();
                //Added By Ram
                if (!ViewBy.isEmpty()) {
                    for (int i = 0; i < ViewBy.size(); i++) {
                        if (ViewBy.get(i) != "") {
                            kpiheads.append(",").append(collect.reportParameters.get(ViewBy.get(i)).get(1));
                        }
                    }
                }
                //End Ram code
                if (dashletdetail.getKpiheads().isEmpty() || dashletdetail.getKpiheads().get(0).equalsIgnoreCase("")) {


//                    kpiheads.append(",").append("KPI");
//                    kpiheads.append(",").append("Current/Prior");
//                    kpiheads.append(",").append("Change%");
//                    kpiheads.append(",").append("Target Value");
//                    kpiheads.append(",").append("Deviation%");
                    kpiheads.append(",").append("KPI");
                    kpiheads.append(",").append("LY");
                    kpiheads.append(",").append("BUD");
                    kpiheads.append(",").append("TY");
                    kpiheads.append(",").append("Var-Bud");
                    kpiheads.append(",").append("Var-Bud%");
                    kpiheads.append(",").append("Var-Ly");
                    kpiheads.append(",").append("Var-Ly%");
                } else {
                    if (dashletdetail.getKpiheads().size() > 10) {
                        for (int i = dashletdetail.getKpiheads().size() - 10; i < dashletdetail.getKpiheads().size() - 2; i++) {
                            kpiheads.append(",").append(dashletdetail.getKpiheads().get(i));   // added by mohit

                        }
                    } else {
                        for (int i = 0; i < dashletdetail.getKpiheads().size() - 2; i++) {
                            kpiheads.append(",").append(dashletdetail.getKpiheads().get(i));   // added by mohit

                        }
                    }
                }
                kpiheads.replace(0, 1, "");
                String customkpiheads = kpiheads.toString();
                String[] Kpiheadarray = customkpiheads.split(",");
//                labelList.add(Kpiheadarray[0]);
//                labelList.add(Kpiheadarray[1]);
//                labelList.add(Kpiheadarray[2]);
//                labelList.add(Kpiheadarray[3]);
//                labelList.add(Kpiheadarray[4]);
//                labelList.add(Kpiheadarray[5]);
//                labelList.add(Kpiheadarray[6]);
//                labelList.add(Kpiheadarray[7]);
                //  Modified by Ram view bys headers
                for (int ii = 0; ii < Kpiheadarray.length; ii++) {
                    labelList.add(Kpiheadarray[ii]);
                }
                for (int i = 0; i < labelList.size(); i++) {
                    label = new Label(i, rowStart, labelList.get(i));
                    label.setCellFormat(writableCellFormat);
                    writableSheet.addCell(label);

                    HeadingCellView = new CellView();
                    HeadingCellView.setSize(256 * 30);
                    writableSheet.setColumnView(i, HeadingCellView);
                }
                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
                writableCellFormat = new WritableCellFormat(writableFont);
                writableCellFormat.setWrap(true);

                // Added by Ram For ViewBys Dashboard
                if (ViewBy.size() > 0) {
                    int totalSize = calculativeVal.size() + gtVal.size();
                    int count = 0;
                    for (int jj = 0; jj < totalSize; jj++) {
                        boolean bool = false;
                        boolean secondView = false;
                        ArrayList<String> tempList = new ArrayList<String>();
                        List list = new ArrayList();
                        if (jj < calculativeVal.size()) {
                            list = calculativeVal.get(jj);
                        }
                        if (jj >= calculativeVal.size() && count < gtVal.size()) {
                            list = gtVal.get(count);
                            count++;
                            if (jj > 0 && (String) list.get(0) != "") {
                                bool = true;
                            }
                        }
                        for (int kk = 0; kk < list.size(); kk++) {
                            String devValue = list.get(kk).toString();
                            tempList.add(devValue);
                            if (jj > 0 && (String) list.get(0) != "") {
                                bool = true;
                            }
                            if (jj > 0 && (String) list.get(1) != "" && ViewBy.size() == 2) {
                                secondView = true;
                            }
                        }

                        for (int j = 0; j < tempList.size(); j++) {
                            writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                            writableCellFormat = new WritableCellFormat(writableFont);
                            if (j > 0 && secondView == true) {
                                writableCellFormat.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.GRAY_50);
                            }
                            writableCellFormat.setWrap(true);
                            if (count > 0 && ViewBy.size() == 2) {
                                cell = new Label(j + 1, jj + rowStart + 1, tempList.get(j));
                            } else {
                                cell = new Label(j, jj + rowStart + 1, tempList.get(j));
                            }
                            if (bool == true) {
                                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                                writableCellFormat = new WritableCellFormat(writableFont);
                                writableCellFormat.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.GRAY_50);
                                writableCellFormat.setWrap(true);

                            }
                            if (count > 0) {
                                writableFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                                writableCellFormat = new WritableCellFormat(writableFont);
                                if (bool == true) {
                                    writableCellFormat.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.GRAY_50);
                                }
                                if (jj == (totalSize - 1)) {
                                    writableCellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.GRAY_50);
                                }
                                writableCellFormat.setWrap(true);
                                cell.setCellFormat(writableCellFormat);

                            } else {
                                cell.setCellFormat(writableCellFormat);
                            }

                            writableSheet.addCell(cell);
                        }
                    }
                } //Endded By Ram
                else {
                    for (int k = 0; k < a1.size(); k++) {
                        String ElementId = a1.get(k);
                        List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);
                        String elementId = a1.get(k);
                        String elementName = "";
                        String value = "";
                        String kpiMasterId = dashletdetail.getKpiMasterId();
                        NumberFormatter nf = new NumberFormatter();
                        ArrayList<String> tempList = new ArrayList<String>();
                        Double currValMul = 0.0;
                        int decimalplaces = 2;
                        DecimalFormat oneDForm = new DecimalFormat("#.0");
                        int sum = 0;
                        double currVal = 0.0;
                        double priorVal = 0.0;
                        String devVal = "--";
                        String basicDevVal = "--";
                        String basicDevPer = "--";
                        String targetValueStr = "--";
                        String BasicTvalue = "0";
                        HashMap basicTargetDetails = new HashMap();
                        boolean isGroupElement = dashletdetail.isGroupElement(elementId);
                        List<KPIElement> elements = kpiElementMap.get(elementId);
                        if (!isGroupElement) {
                            for (KPIElement elem : elements) {
                                if (elem != null && elem.getElementId() != null && !elem.getElementId().equalsIgnoreCase("")) {

                                    elementName = elem.getElementName();
                                    isGroupElement = elem.isIsGroupElement();
                                    break;
                                }
                            }
                        }
                        if (isGroupElement) {
                            if (dashletdetail.getSingleGroupHelpers() != null && !dashletdetail.getSingleGroupHelpers().isEmpty()) {
                                List<KPISingleGroupHelper> kPISingleGroupHelpers = dashletdetail.getSingleGroupHelpers();
                                for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                                    BigDecimal sumBd = new BigDecimal(0);
                                    if (groupingHelper.getGroupName() != null) {
                                        if (groupingHelper.getGroupName().equalsIgnoreCase(elementId)) {
                                            if (groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()) != null) {
                                                kpiDetail.addKPIDrill(groupingHelper.getGroupName(), groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()));
                                            }
                                            group = getGroupVal(groupingHelper, pbretObj, 1, kpiMasterId, dashletdetail, dashboardId, collect);
                                        }
                                    }
                                }
                            }

                        }

                        /*
                         * for (int i = 0; i < kpiElements.size(); i++) {
                         *
                         * //For Each element id try to value from retObjQry and
                         * build a table row here if (pbretObj.getRowCount() >
                         * 0) { String temp = "A_" +
                         * kpiElements.get(i).getElementId(); String type =
                         * kpiElements.get(i).getRefElementType(); if
                         * (kpiElements.size() > 1) { if
                         * ((pbretObj.getFieldValueString(0, temp)) != null &&
                         * !("".equalsIgnoreCase(pbretObj.getFieldValueString(0,
                         * temp)))) { if (type.equalsIgnoreCase("1")) { currVal
                         * = Double.parseDouble((pbretObj.getFieldValueString(0,
                         * temp)));
                         *
                         * } else if (type.equalsIgnoreCase("2")) { StringBuffer
                         * val = new StringBuffer(); String priorValStr =
                         * pbretObj.getFieldValueString(0, temp); if
                         * (priorValStr != null && !("".equals(priorValStr))) {
                         * priorVal = Double.parseDouble(priorValStr); }
                         * double[] valArray = {currVal, priorVal}; double[]
                         * celWidth = new double[valArray.length]; for (int j =
                         * 0; j < valArray.length; j++) { sum += (valArray[j]);
                         * } for (int kpi = 0; kpi < valArray.length; kpi++) {
                         * BigDecimal bd = new BigDecimal(valArray[kpi]); int
                         * decimalPlaces = 0; BasicTvalue =
                         * bd.setScale(decimalPlaces,
                         * BigDecimal.ROUND_HALF_UP).toString(); if (priorVal ==
                         * 0.0) { if (kpi == 1) { continue; } }
                         *
                         * val.append("/").append(BasicTvalue); }
                         * tempList.add(val.substring(1)); } else if
                         * (type.equalsIgnoreCase("3")) { } else { double r =
                         * Double.parseDouble((pbretObj.getFieldValueString(0,
                         * temp))); int decimalPlaces = 2; BigDecimal bd = new
                         * BigDecimal(r); bd = bd.setScale(decimalPlaces,
                         * BigDecimal.ROUND_HALF_UP);
                         * tempList.add(bd.toString()); String dev = "--";
                         *
                         * ArrayList timeDim = collect.timeDetailsArray; String
                         * timeLevel = timeDim.get(3).toString(); Double
                         * targetValue = kpiDetail.getTargetValue(ElementId,
                         * timeLevel); if (targetValue == null) { PbTimeRanges
                         * timeRanges = new PbTimeRanges();
                         * timeRanges.setElementID(ElementId); String
                         * currPeriodName =
                         * timeRanges.getCurrentPeriodName(timeDim.get(2).toString(),
                         * timeLevel); BigDecimal targetData =
                         * kpiDetail.getTargetData(ElementId, timeLevel,
                         * currPeriodName); if (targetData != null) {
                         * targetValue = targetData.doubleValue(); }
                         *
                         * }
                         *
                         * if (targetValue != null) { double DEVAL =
                         * kpiDetail.getDeviationVal(new
                         * Double(oneDForm.format(currVal)), targetValue);
                         * basicDevVal = String.valueOf(DEVAL); basicDevVal =
                         * NumberFormatter.getModifiedNumber(new
                         * BigDecimal(basicDevVal)); //double DEVAL1 =
                         * Double.valueOf(basicDevVal); BigDecimal DEVPER =
                         * kpiDetail.getDeviationPer(currVal, targetValue);
                         * basicDevPer = String.valueOf(DEVPER); basicDevPer =
                         * NumberFormatter.getModifiedNumber(DEVPER) + "%"; if
                         * (basicDevPer.contains("M")) { basicDevPer = (DEVPER)
                         * + "%"; } if (basicDevVal.equalsIgnoreCase("-0.0")) {
                         * basicDevVal = "0.0"; basicDevPer = "0.0"; } String
                         * basicKpiType = "Standard"; String color =
                         * kpiDetail.getBasicKpiColor(DEVAL, ElementId);
                         *
                         * }
                         *
                         * double perDev = 0; String timeDimVal =
                         * collect.timeDetailsArray.get(1).toString(); double
                         * tempDouble = 0.0; if (targetValue != null) { if
                         * (targetValue != 0) { devVal =
                         * String.valueOf(((currVal - targetValue) /
                         * targetValue) * 100); } else { devVal =
                         * String.valueOf((currVal) * 100); } perDev = new
                         * Double(devVal); devVal =
                         * NumberFormatter.getModifiedNumber(new
                         * BigDecimal(devVal)) + "%"; String currentStr =
                         * targetValue.toString(); String pattern = "###,###";
                         * double value1 = Double.parseDouble(currentStr);
                         * DecimalFormat myFormatter = new
                         * DecimalFormat(pattern); String output =
                         * myFormatter.format(value1); targetValueStr = output;
                         *
                         * if (targetValueStr.charAt(targetValueStr.length() -
                         * 1) == ',') { targetValueStr =
                         * targetValueStr.substring(0, targetValueStr.length() -
                         * 1); }
                         *
                         * }
                         *
                         *
                         * }
                         * }
                         * }
                         * }
                         *
                         * }
                         *
                         * tempList.add(targetValueStr);
                         * tempList.add(basicDevPer);
                         */

                        //  if(ViewBy.size()==1 || ViewBy.size()==2)
                        //        do{
                        List list = calculativeVal.get(k);
                        for (int ii = 0; ii < list.size(); ii++) {
                            String devValue = (String) list.get(ii);
                            tempList.add(devValue);
                        }


                        //Ended By Ram
                        if (isGroupElement && group != null && !group.isEmpty()) {
                            cell = new Label(0, k + rowStart + 1, elementId);
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        } else {
                            cell = new Label(0, k + rowStart + 1, elementName);
                            cell.setCellFormat(writableCellFormat);
                            writableSheet.addCell(cell);
                        }
                        if (isGroupElement && group != null && !group.isEmpty()) {
                            for (int j = 0; j < group.size(); j++) {
                                cell = new Label(j + 1, k + rowStart + 1, group.get(j));
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        } else {
                            for (int j = 0; j < tempList.size(); j++) {
                                cell = new Label(j + 1, k + rowStart + 1, tempList.get(j));
                                cell.setCellFormat(writableCellFormat);
                                writableSheet.addCell(cell);
                            }
                        }

                    }
                }
            }
            writableWorkbook.write();
            writableWorkbook.close();


        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        // return swt.fileName;
        return fName;


    }

    private List getGroupVal(KPISingleGroupHelper groupingHelper, PbReturnObject pbretObj, int decimalPlaces, String kpiMasterId, DashletDetail dashletDetail, String dashBoardId, pbDashboardCollection collect) {
        BigDecimal sumBd = new BigDecimal(0);
        KPI kpiDetail = (KPI) dashletDetail.getReportDetails();
        String targetValue = " ";
        String dev = "--";
        String devper = "--";
        String TargetdevPer = "--";
        double currVal = 0.0;
        double priorVal = 0.0;
        BigDecimal grpcurrVal = new BigDecimal(0);
        BigDecimal grppriorVal = new BigDecimal(0);
        int sum = 0;
        String drillReportId = "";
        String drillRepType = "";
        String value = "";
        if (groupingHelper.getGroupName() != null) {
            drillReportId = groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName());
            drillRepType = groupingHelper.getGroupKPIDrillType(groupingHelper.getGroupName());
        }
        ArrayList<String> tempList = new ArrayList<String>();
        String kpiType = dashletDetail.getKpiType();
        String icon = "";
        double changePercVal = 0.0;
        BigDecimal changeperVal = new BigDecimal(0);
        int decimalplaces = 2;
        String None = " ";
        double Count = 0.0;
        BigDecimal average = new BigDecimal(0);
        for (String string : groupingHelper.getElementIds()) {
            Count = Count + 1;
            if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                sumBd = sumBd.add(new BigDecimal(pbretObj.getFieldValueString(0, "A_" + string.trim())));
            }
            if (kpiType.equalsIgnoreCase("Target") || kpiType.equalsIgnoreCase("Standard")) {
                List<KPIElement> kpiElements = kpiDetail.getKPIElements(string);
                int i = 0;
                for (i = 0; i < kpiElements.size(); i++) {
                    if (pbretObj.getRowCount() > 0) {
                        String temp = "A_" + kpiElements.get(i).getElementId();
                        String type = kpiElements.get(i).getRefElementType();
                        if (kpiElements.size() > 1) {
                            if ((pbretObj.getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(pbretObj.getFieldValueString(0, temp)))) {
                                if (type.equalsIgnoreCase("1")) {
                                    currVal = Double.parseDouble(pbretObj.getFieldValueString(0, temp));
                                    grpcurrVal = grpcurrVal.add(new BigDecimal(currVal));
                                } else if (type.equalsIgnoreCase("2")) {
                                    String grppriorValStr = pbretObj.getFieldValueString(0, temp);
                                    priorVal = Double.parseDouble(grppriorValStr);
                                    if (grppriorValStr != null && !("".equals(grppriorValStr))) {
                                        grppriorVal = grppriorVal.add(new BigDecimal(grppriorValStr));
                                    }

                                } else if (type.equalsIgnoreCase("3")) {
                                } else if (type.equalsIgnoreCase("4")) {
                                    changePercVal = Double.parseDouble((pbretObj.getFieldValueString(0, temp)));
                                    changeperVal = changeperVal.add(new BigDecimal(pbretObj.getFieldValueString(0, temp)));
                                    String elemId = kpiElements.get(i).getRefElementId();


                                }
                            }
                        }
                    }
                }
            }
            if (groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                changeperVal = changeperVal.divide(new BigDecimal(Count));
                grpcurrVal = grpcurrVal.divide(new BigDecimal(Count));
                grppriorVal = grppriorVal.divide(new BigDecimal(Count));
            }
            changeperVal = changeperVal.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            BigDecimal[] grpValArray = {grpcurrVal, grppriorVal};
            double[] valArray = {currVal, priorVal};
            for (int j = 0; j < valArray.length; j++) {
                sum += (valArray[j]);
            }
            for (int kpi = 0; kpi < grpValArray.length; kpi++) {
                value = pbretObj.getModifiedNumber(grpValArray[kpi]);
                if (priorVal == 0.0) {
                    if (kpi == 1) {
                        continue;
                    }

                }

            }



            if (groupingHelper.getGroupName() != null) {



                groupingHelper.getGroupName();
                if (kpiType.equalsIgnoreCase("Standard") || kpiType.equalsIgnoreCase("Target")) {
                    if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                        tempList.add(value);
                        tempList.add(changeperVal.toString());
                    } else {
                        tempList.add(None);
                    }
                } else if (kpiType.equalsIgnoreCase("MultiPeriod")) {
                    if (groupingHelper.getCalcType().equalsIgnoreCase("sum")) {

                        if (kpiDetail.isCurrentChecked()) {
                            tempList.add(sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).toString());
                        }

                    } else if (groupingHelper.getCalcType().equalsIgnoreCase("None")) {

                        if (kpiDetail.isCurrentChecked()) {
                            tempList.add(None);
                        }
                    } else {
                        average = sumBd.divide(new BigDecimal(Count));

                        if (kpiDetail.isCurrentChecked()) {
                            tempList.add(None);
                        }



                    }
                } else if (kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
//           if(groupingHelper.getCalcType().equalsIgnoreCase("sum")) {
//                if(kpiDetail.isCurrentChecked())
//               if(drillReportId != null && !("0".equals(drillReportId))&& !drillReportId.equalsIgnoreCase("")){
//                 tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"')\">"+sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)+"</a></td>");
//              }else{
//                   if(kpiDetail.isCurrentChecked())
//                  tableBuffer.append("<td align=\"center\" padding=\"0px\">"+sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)+"</td>");
//              }
//            }
//            else if(groupingHelper.getCalcType().equalsIgnoreCase("None")){
//            if(drillReportId != null && !("0".equals(drillReportId))&& !drillReportId.equalsIgnoreCase("")){
//                if(kpiDetail.isCurrentChecked())
//                tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"')\">"+None+"</a></td>");
//            }else{
//                if(kpiDetail.isCurrentChecked())
//                tableBuffer.append("<td align=\"center\" padding=\"0px\">"+None+"</td>");
//            }
//          } else
//          {
//              average=sumBd.divide(new BigDecimal(Count));
//              if(drillReportId != null && !("0".equals(drillReportId))&& !drillReportId.equalsIgnoreCase("")){
//                  if(kpiDetail.isCurrentChecked())
//                  tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"')\">"+average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)+"</a></td>");
//              }else{
//                  if(kpiDetail.isCurrentChecked())
//                  tableBuffer.append("<td align=\"center\" padding=\"0px\">"+average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)+"</td>");
//              }
//
//          }
                } else {
                    if (groupingHelper.getCalcType().equalsIgnoreCase("sum")) {
                        tempList.add(sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).toString());

                    } else if (groupingHelper.getCalcType().equalsIgnoreCase("None")) {
                        tempList.add(None);
                    } else {
                        average = sumBd.divide(new BigDecimal(Count));
                        tempList.add(average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).toString());


                    }
                }
            }
            if (groupingHelper.getKpiType() != null) {
                if (groupingHelper.getKpiType().equalsIgnoreCase("BasicTarget") || groupingHelper.getKpiType().equalsIgnoreCase("Target")) {
                    if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                        tempList.add(targetValue);
                    } else {
                        tempList.add(None);
                    }
                    if (groupingHelper.getKpiType().equalsIgnoreCase("BasicTarget")) {
                        if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                            tempList.add(dev);
                            tempList.add(devper);
                        } else {
                            tempList.add(None);
                            tempList.add(None);
                        }
                    }
                    if (groupingHelper.getKpiType().equalsIgnoreCase("Target")) {
                        if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                            tempList.add(TargetdevPer);
                        } else {
                            tempList.add(None);
                        }
                    }
                } else if (groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriod")) {

                    if (kpiDetail.isMTDChecked()) {
                        tempList.add(None);
                    }
                    if (kpiDetail.isQTDChecked()) {
                        tempList.add(None);
                    };
                    if (kpiDetail.isYTDChecked()) {
                        tempList.add(None);
                    }
                } else if (groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                    if (kpiDetail.isMTDChecked()) {
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                    }
                    if (kpiDetail.isQTDChecked()) {
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                    }
                    if (kpiDetail.isYTDChecked()) {
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                        tempList.add(None);
                    }
                } else {
                }

            }
        }
        return tempList;
    }

    public boolean saveSSISTableDetailsfromExcelSheet(File tempFile, String uploadTableName, String connId, String dateFormat) {
        FileInputStream fileInputStream = null;
        WorkbookSettings ws = null;
        Workbook workbook = null;
        Sheet s = null;
        int columnCount = '0';
        boolean check = false;
        int totalSheet = -1;
        ArrayList Alist = null;
        String sheetName = null;
        String query = "";
        String SelectQusery = "";
        Object[] obj = new Object[1];
        PbReturnObject prgr = null;
        PbReturnObject trunc = null;
        try {

            Connection connection = null;

            String dbUrl = "jdbc:jtds:sqlserver://182.71.255.85:1433/Meritrac";
            connection = DriverManager.getConnection(dbUrl, "sa", "Merit123");
//          Connection con =  ProgenConnection.getInstance().getConnectionByConId(connId);
//          Connection con =  ProgenConnection.getInstance().getConnection();
            PbReturnObject pbReturnObject = new PbReturnObject();
            ResultSet resultSet = null;
//             if(con!=null)
//             pbReturnObject=super.execSelectSQL("select * from  "+uploadTableName,con);
//             else
//             pbReturnObject=super.execSelectSQL("select * from  "+uploadTableName);
            String s2 = "use Meritrac";
            PreparedStatement prepSt1 = connection.prepareStatement(s2);
            prepSt1.execute();
//             super.execUpdateSQL(s2);
            String s1 = "select * from   ?" ;
            PreparedStatement prepSt3 = connection.prepareStatement(s1);
            prepSt3.setString(1, uploadTableName);
            resultSet = prepSt3.executeQuery();
            pbReturnObject = new PbReturnObject(resultSet);
//             pbReturnObject = super.execSelectSQL(s1);
            fileInputStream = new FileInputStream(tempFile);
            ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = Workbook.getWorkbook(fileInputStream);
            totalSheet = workbook.getNumberOfSheets();
            s = workbook.getSheet(0);
            StringBuilder sqlQuery = new StringBuilder();
//               sqlQuery.append( "insert into "+uploadTableName+" values(");
            //newly added
//               StringBuffer innersqlquery1 = new StringBuffer("");
            sqlQuery.append("insert into dbo." + uploadTableName + " (Event_Name,Client,College_Name,Test_City,State,Zone,Login_Name,First_Name,Last_Name,Candidate_Name,Gender,Date_of_Birth,E_Mail_ID,Mobile_Number,Test_Name,Batch,Test_Taken_On,Tenth_Baord,Tenth_Percent");
            sqlQuery.append(",Twelth_Board,Twelth_Percent,Highest_Degree,Specialization,University,Year_of_Passing,Percentage_3,Section_1,Section1_Percent,Section_2,Section2_Percent,Section_3,Section3_Percent,Section_4,Section4_Percent,Section_5,Section5_Percent");
            sqlQuery.append(",Section_6,Section6_Percent,Total_Score,Precentage,Test_Status,Number_Of_Correct_Resposnes,Number_Of_Incorrect_Responses,Scaled_Scores,HR_Interview_Status,Tech_Interview_Status,Manager_Interview_Status,Offer) values (");
            //               for(int count=0;count<pbReturnObject.getColumnCount();count++){
//                innersqlquery1.append(","+s.getCell(count, 0).getContents());
//               }
//               sqlQuery.append(innersqlquery1.substring(1));
//               sqlQuery.append(") values(");
            //close added

            //newly added
            String sqlQry = "";
            ArrayList insertList = new ArrayList();
            Object inOb[] = new Object[s.getColumns()];
            String inQuery = "";
            sqlQry = "insert into " + uploadTableName + " (Event_Name,Client,College_Name,Test_City,State,Zone,Login_Name,First_Name,Last_Name,Candidate_Name,Gender,Date_of_Birth,E_Mail_ID,Mobile_Number,Test_Name,Batch,Test_Taken_On,Tenth_Baord,Tenth_Percent"
                    + ",Twelth_Board,Twelth_Percent,Highest_Degree,Specialization,University,Year_of_Passing,Percentage_3,Section_1,Section1_Percent,Section_2,Section2_Percent,Section_3,Section3_Percent,Section_4,Section4_Percent,Section_5,Section5_Percent"
                    + ",Section_6,Section6_Percent,Total_Score,Precentage,Test_Status,Number_Of_Correct_Resposnes,Number_Of_Incorrect_Responses,Scaled_Scores,HR_Interview_Status,Tech_Interview_Status,Manager_Interview_Status,Offer) values ("
                    + "'&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&',&,'&',&,'&','&','&',&,&,&,&,&,&,&,&,&,&,&,&,&,&,&,&,'&','&','&','&','&','&','&','&')";



            StringBuffer innersqlquery = new StringBuffer("");
            for (int count = 0; count < pbReturnObject.getColumnCount(); count++) {
                innersqlquery.append("," + "?");
            }
            sqlQuery.append(innersqlquery.substring(1));
            sqlQuery.append(")");
            String truncTab = "truncate table  ?" ;
            PreparedStatement prepSt2 = connection.prepareStatement(truncTab);
            prepSt2.setString(1, uploadTableName);
            prepSt2.execute();
//               super.execUpdateSQL(truncTab);
//              Connection con1 =  ProgenConnection.getInstance().getConnectionByConId(connId);
            logger.info("s.getRows(): " + s.getRows());
            logger.info("s.getColumns()" + s.getColumns());
//               PreparedStatement ps=con1.prepareStatement(sqlQuery.toString());
            PreparedStatement ps = connection.prepareStatement(sqlQuery.toString());
            String[] dbcolType = pbReturnObject.getColumnTypes();
            for (int i = 1; i < s.getRows(); i++) {
                for (int j = 0; j < s.getColumns(); j++) {
                    Cell cell = s.getCell(j, i);
                    CellType type = cell.getType();
                    String colType = type.toString();
//                             
                    if (s.getCell(j, i).getContents() != null && !s.getCell(j, i).getContents().equalsIgnoreCase("")) {
//                           if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                               if(dbcolType[j].equalsIgnoreCase("numeric")){
//                               if(colType.equalsIgnoreCase("Number")){
                        //for number type cols
                        if (j == 18 || j == 20 || j == 24 || j == 25 || j == 26 || j == 27 || j == 28 || j == 29 || j == 30 || j == 31 || j == 32 || j == 33 || j == 34 || j == 35 || j == 36 || j == 37 || j == 38 || j == 39) {
//                                   
                            String val = s.getCell(j, i).getContents().toString();
                            val = val.replace("<", "").replace(">", "").replace("NA", "").replace("%", "");
                            try {
                                Double intObj1 = Double.parseDouble(val);
                            } catch (NumberFormatException e) {
                                val = "null";
                            }
//                                    
//                                   ps.setString(j+1,val);
//                                   int n = Integer.parseInt(val);
                            inOb[j] = val;

//                                     ps.setInt(j+1,Integer.parseInt(s.getCell(j, i).getContents().toString()));
//                                 }else if(dbcolType[j].equalsIgnoreCase("date") || dbcolType[j].equalsIgnoreCase("nvarchar")){
//                                   }else if(colType.equalsIgnoreCase("Date")){
                            //for date type cols
                        } else if (j == 11 || j == 16) {
//                                       
                            String str = "";
                            if (s.getCell(j, i).getContents() != null) {
                                str = s.getCell(j, i).getContents();
                                String date1 = s.getCell(j, i).getContents().toString();
                                SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat);
                                try {
                                    Date d = sdf1.parse(str);
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yy");
                                    inOb[j] = sdf2.format(d);
//                                       ps.setString(j+1,sdf2.format(d));
                                } catch (ParseException e) {
                                    inOb[j] = "";
                                }

                            }
                        } else {
//                                       
//                                    ps.setString(j+1,(cell.getContents()).toString());
                            inOb[j] = cell.getContents().toString().replace("'", "''");
                        }
//                           }
//                           else {
//                                if(dbcolType[j].equalsIgnoreCase("number")){
//                                     ps.setInt(j+1,Integer.parseInt(s.getCell(j, i).getContents().toString()));
//                                 }else if(dbcolType[j].equalsIgnoreCase("date")){
//                                     String str="";
//                                     if(s.getCell(j, i).getContents()!=null){
//                                       str = s.getCell(j, i).getContents();
//                                       SimpleDateFormat sdf1=new SimpleDateFormat(dateFormat);
//                                       Date d=sdf1.parse(str);
//                                       SimpleDateFormat sdf2=new SimpleDateFormat("dd-MMM-yy");
//                                       ps.setString(j+1,sdf2.format(d));
//                                     }
//                                 }else{
//                                ps.setString(j+1,(cell.getContents()).toString());
//                                }
//                            }
                    } else {
//                           
                        if (j == 18 || j == 20 || j == 24 || j == 25 || j == 26 || j == 27 || j == 28 || j == 29 || j == 30 || j == 31 || j == 32 || j == 33 || j == 34 || j == 35 || j == 36 || j == 37 || j == 38 || j == 39) {
//                               
//                               ps.setString(j+1,null);
                            inOb[j] = "null";
                        } else {
//                               
//                               ps.setString(j+1,null);
                            inOb[j] = null;
                        }
                    }
                }
                inQuery = buildQuery(sqlQry, inOb);
                logger.info("inQuery-------" + inQuery);
                PreparedStatement prepSt = connection.prepareStatement(inQuery);
                prepSt.execute();
                insertList.add(inQuery);
//                      ps.addBatch();
                prepSt.close();

            }
            //
//                 executeMultiple(insertList);
//                ps.executeBatch();
//                ps.close();
            check = true;
        } catch (IOException e) {
            logger.error("Exception:", e);
            check = false;
        } catch (IndexOutOfBoundsException e) {
            logger.error("Exception:", e);
            check = false;
        } catch (SQLException e) {
            logger.error("Exception:", e);
            check = false;
        } catch (BiffException e) {
            logger.error("Exception:", e);
            check = false;
        }
        return check;
    }

    public String getAllTables(String conid) {
//        String[] seArraydcolname = selectedcolname.split(",");

        String Query = "";
        if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL))) {
            Query = "show tables";

        } else if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER))) {
            Query = "SELECT table_name FROM information_schema.tables";

        } else {
            Query = "SELECT table_name FROM user_tables";

        }
        Connection con = ProgenConnection.getInstance().getConnectionByConId(conid);

//        String finalQuery = "";
//        String strCol = "";
        PbReturnObject pbro = new PbReturnObject();
//        int flag = 0;
        PbDb pbdb = new PbDb();
        String jsonString = "";
        ResultSet resultSet = null;
//        PbReturnObject retObj = null;
        StringBuilder sb = new StringBuilder();
        try {

            pbro = pbdb.execSelectSQL(Query, con);

            sb.append("<option> --SELECT-- </option>");
            for (int i = 0; i < pbro.getRowCount(); i++) {
                sb.append("<option>");
                sb.append(pbro.getFieldValueString(i, 0));
                sb.append("</option>");
            }
//            resultSet = pstmtForTransfer.executeQuery();

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

//

        jsonString = sb.toString();


        return jsonString;
    }

    public String CallEtls(HttpServletRequest request) throws SQLException {
        String etl = request.getParameter("etl");
        CallableStatement proc = null;
        Connection con = ProgenConnection.getInstance().getConnection();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date varDate = null;
        java.sql.Date sqlDate = null;
        String status = "Fail";

        try {
            if (etl.equalsIgnoreCase("Finance_DATA_proc")) {
                proc = con.prepareCall("{call dataflow.Finance_DATA_proc(?,?)}");
                String sd = request.getParameter("sd");
                String ed = request.getParameter("ed");
                varDate = dateFormat.parse(sd);
                sqlDate = new java.sql.Date(varDate.getTime());
                proc.setDate(1, sqlDate);
                varDate = dateFormat.parse(ed);
                sqlDate = new java.sql.Date(varDate.getTime());
                proc.setDate(2, sqlDate);
                proc.execute();
                status = "success";
            } else if (etl.equalsIgnoreCase("KPI_HR_Proc")) {

                proc = con.prepareCall("{call dataflow.KPI_HR_Proc(?,?)}");
                String sd = request.getParameter("sd");
                String ed = request.getParameter("ed");
                varDate = dateFormat.parse(sd);
                sqlDate = new java.sql.Date(varDate.getTime());
                proc.setDate(1, sqlDate);
                varDate = dateFormat.parse(ed);
                sqlDate = new java.sql.Date(varDate.getTime());
                proc.setDate(2, sqlDate);
                // proc.execute();
                status = "success";

            } else if (etl.equalsIgnoreCase("CleanUp_Data_proc")) {
                proc = con.prepareCall("{call dataflow.CleanUp_Data_proc()}");
                // proc.execute();
                status = "success";
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
//            check = false;
        } catch (ParseException e) {
            logger.error("Exception:", e);
//            check = false;
        } finally {
            if (proc != null) {
                proc.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return status;
    }

    public void SaveAndUploadXslx(HttpServletRequest request, HttpServletResponse response) throws SQLException, InvalidFormatException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String wbo = request.getParameter("wbo");
        String fn = request.getParameter("fn");
        String loc = request.getParameter("loc");
        String fullpath = null;
        if (loc.equalsIgnoreCase("Fixed")) {
            fullpath = request.getParameter("fullpath");
        }
        String wbtype = request.getParameter("wbtype");
        String insertdata = request.getParameter("insertdata");
        String connid = request.getParameter("connid");
        String sheets = request.getParameter("sheets");
        String alltables = request.getParameter("alltables");
        String alltruncate = request.getParameter("alltruncate");
        String table = request.getParameter("bbg");
        String Truncate = request.getParameter("Truncate");
        String totalsheets[] = sheets.split("::");
        String selectedSheet = request.getParameter("selectedSheet");//added by Dinanath
        String allSelectedSheet[] = selectedSheet.split("::");
        Map<String, String> AllInfo = new HashMap<>();
        UploadXSLX upxs = new UploadXSLX();
        File tempFile = null;
        String totalinfo = "";
        String filePath = "";
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        if (session != null) {
            filePath = pbDAO.getFilePath(session);
        } else {
            filePath = "/usr/local/cache";
        }
        filePath=filePath+File.separator+"UploadedExcels";
        tempFile = new File(filePath + File.separator + fn);
        OPCPackage pkg = OPCPackage.open(tempFile, PackageAccess.READ);
        String query = "";
        String query1 = "";
        String query2 = "";
        String updateQueryForLoadedDate = "";
        int WB_ID = 0;
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                query = "INSERT INTO PRG_AR_WORKBOOK_MASTER_DETAILS(WB_OBJECT,FILE_NAME,LOCATION_TYPE,FULL_PATH,WB_TYPE,INSERT_DATA,CONNECTION_ID,TOTAL_SHEETS)"
                        + " VALUES('&','&','&','&','&','&','&','&')";
                updateQueryForLoadedDate = "update PRG_AR_WORKBOOK_MASTER_DETAILS set LAST_LOADED=? where WB_ID=?";
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                query = "INSERT INTO PRG_AR_WORKBOOK_MASTER_DETAILS(WB_OBJECT,FILE_NAME,LOCATION_TYPE,FULL_PATH,WB_TYPE,INSERT_DATA,CONNECTION_ID,TOTAL_SHEETS)"
                        + " VALUES('&','&','&','&','&','&','&','&')";
                updateQueryForLoadedDate = "update PRG_AR_WORKBOOK_MASTER_DETAILS set LAST_LOADED=? where WB_ID=?";
            } else {
                query = "INSERT INTO PRG_AR_WORKBOOK_MASTER_DETAILS(WB_ID,WB_OBJECT,FILE_NAME,LOCATION_TYPE,FULL_PATH,WB_TYPE,INSERT_DATA,CONNECTION_ID,TOTAL_SHEETS)"
                        + " VALUES(PRG_WB_ID_SEQ.nextval,'&','&','&','&','&','&','&','&')";
                updateQueryForLoadedDate = "update PRG_AR_WORKBOOK_MASTER_DETAILS set LAST_LOADED=? where WB_ID=?";
            }

            Object obj[] = new Object[8];
            obj[0] = wbo;
            obj[1] = fn;
            obj[2] = loc;
            obj[3] = fullpath;
            obj[4] = wbtype;
            obj[5] = insertdata;
            obj[6] = connid;
            obj[7] = totalsheets.length;
            query = super.buildQuery(query, obj);

            int i1 = pbdb.execUpdateSQL(query);

            query = "select WB_ID from PRG_AR_WORKBOOK_MASTER_DETAILS where WB_OBJECT='&' and FILE_NAME='&' order by WB_ID desc";
            Object obj1[] = new Object[2];
            obj1[0] = wbo;
            obj1[1] = fn;
            query = super.buildQuery(query, obj1);
            pbro = pbdb.execSelectSQL(query);
            if (pbro != null) {
                WB_ID = pbro.getFieldValueInt(0, 0);
            }


            if (insertdata.equalsIgnoreCase("st")) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    query2 = "INSERT INTO PRG_AR_SHEET_MASTER_DETAILS(WB_ID,SHEET_NAME,TABLE_NAME,IS_TRUNCATE,isSelectedSheet)"
                            + " VALUES('&','&','&','&','&')";
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    query2 = "INSERT INTO PRG_AR_SHEET_MASTER_DETAILS(WB_ID,SHEET_NAME,TABLE_NAME,IS_TRUNCATE,isSelectedSheet)"
                            + " VALUES('&','&','&','&','&')";
                } else {

                    query2 = "INSERT INTO PRG_AR_SHEET_MASTER_DETAILS(SHEET_ID,WB_ID,SHEET_NAME,TABLE_NAME,IS_TRUNCATE,isSelectedSheet)"
                            + " VALUES(PRG_SHEET_ID_SEQ.nextval,'&','&','&','&','&')";
                }
                for (int j = 0; j < (totalsheets.length); j++) {
                    Object obj2[] = new Object[5];
                    obj2[0] = WB_ID;
                    obj2[1] = totalsheets[j];
                    obj2[2] = table;
                    obj2[3] = Truncate;
                    obj2[4] = allSelectedSheet[j];
                    AllInfo.put(totalsheets[j], table + "::" + Truncate + "::" + allSelectedSheet[j]);
                    query = super.buildQuery(query2, obj2);
                    int i2 = pbdb.execUpdateSQL(query);
                }
                upxs.UploadIntoDatabase(pkg, System.out, -1, connid, insertdata, AllInfo, response);//commented by Dinanath
                //added by Dinanath for update query for loaded date
                java.util.Date today = new java.util.Date();
                java.sql.Date date = new java.sql.Date(today.getTime());
                Connection con = getConnection();
                int flag = 0;
                try {
                    PreparedStatement ps = con.prepareStatement(updateQueryForLoadedDate);
                    ps.setDate(1, date);
                    ps.setFloat(2, WB_ID);
                    flag = ps.executeUpdate();
                    ps.close();
                    ps = null;
                    if (!con.getAutoCommit()) {
                        con.commit();
                    }
                    con.close();
                    con = null;
                } catch (SQLException ex) {
                    flag = 0;
                    logger.error("Exception:", ex);
                } finally {
                    if (con != null) {
                        con.close();
                    }
                }
                //ended by Dinanath
            } else if (insertdata.equalsIgnoreCase("mt")) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    query1 = "INSERT INTO PRG_AR_SHEET_MASTER_DETAILS(WB_ID,SHEET_NAME,TABLE_NAME,IS_TRUNCATE,isSelectedSheet)VALUES('&','&','&','&','&')";
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    query1 = "INSERT INTO PRG_AR_SHEET_MASTER_DETAILS(WB_ID,SHEET_NAME,TABLE_NAME,IS_TRUNCATE,isSelectedSheet)VALUES('&','&','&','&','&')";
                } else {
                    query1 = "INSERT INTO PRG_AR_SHEET_MASTER_DETAILS(SHEET_ID,WB_ID,SHEET_NAME,TABLE_NAME,IS_TRUNCATE,isSelectedSheet)VALUES(PRG_SHEET_ID_SEQ.nextval,'&','&','&','&','&')";
                }
                String alltablesarr[] = alltables.split("::");
                String alltruncatearr[] = alltruncate.split("::");
                for (int j = 0; j < (totalsheets.length); j++) {
                    Object obj3[] = new Object[5];
                    obj3[0] = WB_ID;
                    obj3[1] = totalsheets[j];
                    obj3[2] = alltablesarr[j];
                    obj3[3] = alltruncatearr[j];
                    obj3[4] = allSelectedSheet[j];
                    AllInfo.put(totalsheets[j], alltablesarr[j] + "::" + alltruncatearr[j] + "::" + allSelectedSheet[j]);
                    query = super.buildQuery(query1, obj3);
                    int i3 = pbdb.execUpdateSQL(query);
                }
                upxs.UploadIntoDatabase(pkg, System.out, -1, connid, insertdata, AllInfo, response);//commented by Dinanath
                java.util.Date today = new java.util.Date();
                java.sql.Date date = new java.sql.Date(today.getTime());
                Connection con = getConnection();
                int flag = 0;
                try {
                    PreparedStatement ps = con.prepareStatement(updateQueryForLoadedDate);
                    ps.setDate(1, date);
                    ps.setFloat(2, WB_ID);
                    flag = ps.executeUpdate();
                    ps.close();
                    ps = null;
                    if (!con.getAutoCommit()) {
                        con.commit();
                    }
                    con.close();
                    con = null;
                } catch (Exception ex) {
                    flag = 0;
                    logger.error("Exception:", ex);
                } finally {
                    if (con != null) {
                        con.close();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
            out.print(e.getMessage());
        }
    }
    //added by Dinanath

    public String getLoadDataFromAlreadyInsertedWorkbook(String select, HttpServletRequest request) throws SQLException {
        String jsonString = "";
        PbReturnObject pbro = new PbReturnObject();
        Connection conn = ProgenConnection.getInstance().getConnection();
        String query = "";
        StringBuilder sb = new StringBuilder();
        query = "select wb_id,wb_object,file_name,location_type,full_path,wb_type,insert_data,connection_id,last_loaded,total_sheets from PRG_AR_WORKBOOK_MASTER_DETAILS";
        PbDb pbdb = new PbDb();
        //pbro = pbdb.execSelectSQL(query);
        try {

            pbro = pbdb.execSelectSQL(query);
            for (int i = 0; i < pbro.getRowCount(); i++) {
                sb.append("<tr>");
                sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap id='").append(pbro.getFieldValueString(i, 0)).append("'>").append(pbro.getFieldValueString(i, 0)).append("</td>");
                sb.append("<td style='width: 12.0em;padding: 0.6em; border: 1px solid #CCC;' nowrap >").append(pbro.getFieldValueString(i, 1)).append("</td>");
                sb.append("<td style='width: 12.0em;padding: 0.6em; border: 1px solid #CCC;' nowrap >").append(pbro.getFieldValueString(i, 2)).append("</td>");
                sb.append("<td style='width: 6.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap >").append(pbro.getFieldValueString(i, 3)).append("</td>");
                sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append(pbro.getFieldValueString(i, 4)).append("</td>");
                if (pbro.getFieldValueString(i, 5).equalsIgnoreCase("ms")) {
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append("Multiple Sheets").append("</td>");
                }
                if (pbro.getFieldValueString(i, 5).equalsIgnoreCase("ss")) {
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append("Single Sheets").append("</td>");
                }
                if (pbro.getFieldValueString(i, 6).equalsIgnoreCase("mt")) {
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append("Multiple Table").append("</td>");
                }
                if (pbro.getFieldValueString(i, 6).equalsIgnoreCase("st")) {
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append("Single Table").append("</td>");
                }

                sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append(pbro.getFieldValueString(i, 7)).append("</td>");
                sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append(pbro.getFieldValueString(i, 8)).append("</td>");
                sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append(pbro.getFieldValueString(i, 9)).append("</td>");
                sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append("<input id='loadDataXslInfo'type='button' class='navtitle-hover' value='Load Data' onclick=\"loadDataXls('").append(pbro.getFieldValueString(i, 0)).append("','").append(pbro.getFieldValueString(i, 1)).append("','").append(pbro.getFieldValueString(i, 2)).append("','").append(pbro.getFieldValueString(i, 3)).append("','").append(pbro.getFieldValueString(i, 4)).append("','").append(pbro.getFieldValueString(i, 5)).append("','").append(pbro.getFieldValueString(i, 6)).append("','").append(pbro.getFieldValueString(i, 7)).append("','").append(pbro.getFieldValueString(i, 8)).append("','").append(pbro.getFieldValueString(i, 9)).append("')").append("\">").append("</td></tr>");
                // sb.append("<td style='width: 4.0em; padding: 0.6em; border: 1px solid #CCC;' nowrap>").append("<input id='loadDataXslInfo'type='button' class='navtitle-hover' value='Delete' onclick=\"deleteDataXls('").append(pbro.getFieldValueString(i, 0)).append("')").append("\">").append("</td></tr>");
            }

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        jsonString = sb.toString();

        return jsonString;
    }
//endded by Dinanath
    //added by Dinanath for fixed path

    public void getFixedFullpathFilename(HttpServletRequest request, HttpServletResponse response) throws SQLException, InvalidFormatException, IOException {
        String fullpath = request.getParameter("fullpath");
        String filename = request.getParameter("fn");
        String connId = request.getParameter("connid");
        String insertIntoTableType = request.getParameter("tabletype");
        String wbid = request.getParameter("wbid");
        String wbtype = request.getParameter("wbtype");

        String singleTName = request.getParameter("singleTName");
        String singleTruncate = request.getParameter("singleTruncate");

        String totalsheets = request.getParameter("totalsheets");
        Integer totalsheetsLen = Integer.parseInt(totalsheets);
        String allSheetName = request.getParameter("allSheetName");
        String alltables = request.getParameter("alltables");
        String alltruncate = request.getParameter("alltruncate");
        String selectedSheet = request.getParameter("selectedSheet");
        String filePath = null;
        String fileName = null;
        PrintWriter out = response.getWriter();
        FileOutputStream fos = null;
        byte[] fileData = null;
        File tempFile = null;
        String totalinfo = "";
        Map<String, String> AllInfo = new HashMap<>();

        String query = "select sheet_id,wb_id,sheet_name,table_name,is_truncate from prg_ar_sheet_master_details where wb_id=" + wbid;
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        try {
            //pbro = pbdb.execSelectSQL(query);
            if (insertIntoTableType.equalsIgnoreCase("mt")) {
                String allSheetname2[] = allSheetName.split("::");
                String alltablesarr2[] = alltables.split("::");
                String alltruncatearr2[] = alltruncate.split("::");
                String selectedSheet2[] = selectedSheet.split("::");
                for (int j = 0; j < totalsheetsLen; j++) {
                    AllInfo.put(allSheetname2[j], alltablesarr2[j] + "::" + alltruncatearr2[j] + "::" + selectedSheet2[j]);
                }
            }
            if (insertIntoTableType.equalsIgnoreCase("st")) {
                String allSheetname2[] = allSheetName.split("::");
                String alltablesarr2 = singleTName;
                String alltruncatearr2 = singleTruncate;
                String selectedSheet2[] = selectedSheet.split("::");
                for (int j = 0; j < totalsheetsLen; j++) {
                    AllInfo.put(allSheetname2[j], alltablesarr2 + "::" + alltruncatearr2 + "::" + selectedSheet2[j]);
                }
            }

            //filePath = "c://usr/local/cache/UploadedExcels";
            //filePath = fullpath;
            String filep[] = fullpath.split(":");
            filePath = filep[0] + ":\\" + filep[1];
            tempFile = new File(filePath + File.separator + filename);
            OPCPackage pkg = OPCPackage.open(tempFile, PackageAccess.READ);
            UploadXSLX upxs = new UploadXSLX();
            upxs.UploadIntoDatabase(pkg, System.out, -1, connId, insertIntoTableType, AllInfo, response);//commented by Dinanath
            String updateQueryForLoadedDate = "update PRG_AR_WORKBOOK_MASTER_DETAILS set LAST_LOADED=? where WB_ID=?";
            java.util.Date today = new java.util.Date();
            java.sql.Date date = new java.sql.Date(today.getTime());
            Connection con = getConnection();
            int wbid1 = Integer.parseInt(wbid);
            int flag = 0;
            try {
                PreparedStatement ps = con.prepareStatement(updateQueryForLoadedDate);
                ps.setDate(1, date);
                ps.setFloat(2, wbid1);
                flag = ps.executeUpdate();
                ps.close();
                ps = null;
                if (!con.getAutoCommit()) {
                    con.commit();
                }
                con.close();
                con = null;
            } catch (SQLException ex) {
                flag = 0;
                out.print(ex.getMessage());
            } finally {
                if (con != null) {
                    con.close();
                }
            }

        } catch (IOException e) {
            out.print(e.getMessage());
        } catch (NumberFormatException e) {
            out.print(e.getMessage());
        } catch (SQLException e) {
            out.print(e.getMessage());
        } catch (ParserConfigurationException e) {
            out.print(e.getMessage());
        } catch (OpenXML4JException e) {
            out.print(e.getMessage());
        } catch (SAXException e) {
            out.print(e.getMessage());
        }
    }
    //added by Dinanath for Variable file location path

    public void addVariableExcelData(HttpServletRequest request, OPCPackage pkg, String wbid, String filename, String connectionid, String insertIntoTableType, HttpServletResponse response) throws SQLException, InvalidFormatException {
        Map<String, String> AllInfo = new HashMap<>();
        String singleTName = request.getParameter("singleTName");
        String singleTruncate = request.getParameter("singleTruncate");

        String totalsheets = request.getParameter("globaltotalsheets");
        Integer totalsheetsLen = Integer.parseInt(totalsheets);
        String allSheetName = request.getParameter("allSheetName");//
        String alltables = request.getParameter("alltables");
        String alltruncate = request.getParameter("alltruncate");
        String selectedSheet = request.getParameter("selectedSheet");//
        try {
            if (insertIntoTableType.equalsIgnoreCase("mt")) {
                String allSheetname2[] = allSheetName.split("::");
                String alltablesarr2[] = alltables.split("::");
                String alltruncatearr2[] = alltruncate.split("::");
                String selectedSheet2[] = selectedSheet.split("::");
                for (int j = 0; j < totalsheetsLen; j++) {
                    AllInfo.put(allSheetname2[j], alltablesarr2[j] + "::" + alltruncatearr2[j] + "::" + selectedSheet2[j]);
                }
                UploadXSLX upxs = new UploadXSLX();
                upxs.UploadIntoDatabase(pkg, System.out, -1, connectionid, insertIntoTableType, AllInfo, response);
            }
            if (insertIntoTableType.equalsIgnoreCase("st")) {
                String allSheetname2[] = allSheetName.split("::");
                String alltablesarr2 = singleTName;
                String alltruncatearr2 = singleTruncate;
                String selectedSheet2[] = selectedSheet.split("::");
                for (int j = 0; j < totalsheetsLen; j++) {
                    AllInfo.put(allSheetname2[j], alltablesarr2 + "::" + alltruncatearr2 + "::" + selectedSheet2[j]);
                }
                UploadXSLX upxs = new UploadXSLX();
                upxs.UploadIntoDatabase(pkg, System.out, -1, connectionid, insertIntoTableType, AllInfo, response);
            }


            String updateQueryForLoadedDate = "update PRG_AR_WORKBOOK_MASTER_DETAILS set LAST_LOADED=? where WB_ID=?";
            java.util.Date today = new java.util.Date();
            java.sql.Date date = new java.sql.Date(today.getTime());
            Connection con = getConnection();
            int wbid1 = Integer.parseInt(wbid);
            int flag = 0;
            try {
                PreparedStatement ps = con.prepareStatement(updateQueryForLoadedDate);
                ps.setDate(1, date);
                ps.setFloat(2, wbid1);
                flag = ps.executeUpdate();
                ps.close();
                ps = null;
                if (!con.getAutoCommit()) {
                    con.commit();
                }
                con.close();
                con = null;
            } catch (Exception ex) {
                flag = 0;
                logger.error("Exception:", ex);
            } finally {
                if (con != null) {
                    con.close();
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
//added by Dinanath for sheet info

    public String getAllSheetsFromDatabase(HttpServletRequest request) throws SQLException, InvalidFormatException {
//        Map<String, String> AllInfo = new HashMap<>();
        String workbookId = request.getParameter("workbookId");
        String tabletype = request.getParameter("tabletype");
        String query = "select sheet_id,wb_id,sheet_name,table_name,is_truncate,isSelectedSheet from prg_ar_sheet_master_details where wb_id=" + workbookId;
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String html = "";
        StringBuilder sb = new StringBuilder();
        //pbro = pbdb.execSelectSQL(query);
        try {

            pbro = pbdb.execSelectSQL(query);
            for (int i = 0; i < pbro.getRowCount(); i++) {

                //  AllInfo.put(pbro.getFieldValueString(i, 2), pbro.getFieldValueString(i, 3) + "::" + pbro.getFieldValueString(i, 4));
                if (tabletype.equalsIgnoreCase("st")) {
                    if (i == 0) {
                        sb.append("<tr><td align='left'  style='font-size:larger; background-color: #B4D9EE; width: 5%;  padding: 0.6em; border: 1px solid #CCC; '><label>Table Name:</label></td>");
                        sb.append("<td  style='padding: 0.6em;'><select name='bbgSTime' id='bbgSTime' style='width:150px;' onchange=''><option> --SELECT-- </option>");
                        sb.append("<option selected> ").append(pbro.getFieldValueString(i, 3)).append("</option>");
                        sb.append("</select></td></tr>");
                        sb.append("<tr>");
                        sb.append("<td align=\"left\" style=\"font-size:larger; background-color: #B4D9EE; width: 5%;  padding: 0.6em; border: 1px solid #CCC; \"><label>Truncate Table:&nbsp;&nbsp;&nbsp;&nbsp;</label></td>");
                        if (pbro.getFieldValueString(i, 4).equalsIgnoreCase("Yes")) {
                            sb.append("<td style=\"width:10%\"><center><input type=\"radio\" name=\"TruncateSTime\" checked style=\"width:auto;color:black\" id=\"YesSTime\" value =\"Yes\" />Yes&nbsp;&nbsp;</center></td>");
                            sb.append("<td style=\"width:10%\" ><input type=\"radio\" checked name=\"TruncateSTime\"  style=\"width:auto;color:black\" id=\"NoSTime\" value=\"No\" />No&nbsp;&nbsp;&nbsp;&nbsp;</td>");
                        } else if (pbro.getFieldValueString(i, 4).equalsIgnoreCase("No")) {
                            sb.append("<td style=\"width:10%\"><center><input type=\"radio\" name=\"TruncateSTime\" style=\"width:auto;color:black\" id=\"YesSTime\" value =\"Yes\" />Yes&nbsp;&nbsp;</center></td>");
                            sb.append("<td style=\"width:10%\" ><input type=\"radio\" checked name=\"TruncateSTime\" checked style=\"width:auto;color:black\" id=\"NoSTime\" value=\"No\" />No&nbsp;&nbsp;&nbsp;&nbsp;</td>");
                        }
                        sb.append("</tr>");

                        sb.append("<tr><td style=\"background-color: #B4D9EE; width: 1%;  padding: 0.6em; border: 1px solid #CCC; \">Sheet No.</td>");
                        sb.append("<td  style=\"background-color: #B4D9EE; width: 4%;  padding: 0.6em; border: 1px solid #CCC; \" >Sheet Name </td>");
                        sb.append("<td colspan=\"2\" style=\"background-color: #B4D9EE; width: 4%;  padding: 0.6em; border: 1px solid #CCC; \" >Select Sheet</td>");
                        sb.append("</tr>");
                    }
                    sb.append("<tr>");
                    sb.append("<tr><td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(i + 1).append("</td>");
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;' id='allSheetName").append(i + 1).append("'>").append(pbro.getFieldValueString(i, 2)).append("</td>");//sheet
                    if (pbro.getFieldValueString(i, 5).equalsIgnoreCase("true")) {
                        sb.append("<td style='width:10%' ><input type='checkbox' checked id='SelectedSheetSTime").append(i + 1).append("' style='width:auto;color:black'  value='' /></td>");
                    } else if (pbro.getFieldValueString(i, 5).equalsIgnoreCase("false")) {
                        sb.append("<td style='width:10%' ><input type='checkbox'  id='SelectedSheetSTime").append(i + 1).append("'style='width:auto;color:black'  value='' /></td>");
                    }
                    sb.append(" </tr>");

                } else if (tabletype.equalsIgnoreCase("mt")) {
                    sb.append("<tr><td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;'>").append(i + 1).append("</td>");
                    sb.append("<td style='width: 8.0em; padding: 0.6em; border: 1px solid #CCC;' id='allSheetName").append(i + 1).append("'>").append(pbro.getFieldValueString(i, 2)).append("</td>");//sheet
                    sb.append("<td  style='padding: 0.6em;'><select name='bbgSTime' id='bbgSTime").append((i + 1)).append("' style='width:150px;'><option>").append(pbro.getFieldValueString(i, 3)).append("</option>");
                    sb.append("</select></td>");
                    if (pbro.getFieldValueString(i, 4).equalsIgnoreCase("Yes")) {
                        sb.append("<td style='width:10%'><center><input type='radio' checked name='TruncateSTime").append(i + 1).append("' style='width:auto;color:black'  value ='Yes' />");
                        sb.append("   Yes&nbsp;&nbsp;</center></td>");
                        sb.append("<td style='width:10%' ><input type='radio'  name='TruncateSTime").append(i + 1).append("' style='width:auto;color:black'  value='No' />");
                        sb.append("  No&nbsp;&nbsp;&nbsp;&nbsp;</td>");
                    } else if (pbro.getFieldValueString(i, 4).equalsIgnoreCase("No")) {
                        sb.append("<td style='width:10%'><center><input type='radio' name='TruncateSTime").append(i + 1).append("' style='width:auto;color:black'  value ='Yes' />");
                        sb.append("   Yes&nbsp;&nbsp;</center></td>");
                        sb.append("<td style='width:10%' ><input type='radio' checked name='TruncateSTime").append(i + 1).append("' style='width:auto;color:black'  value='No' />");
                        sb.append("  No&nbsp;&nbsp;&nbsp;&nbsp;</td>");
                    }
                    if (pbro.getFieldValueString(i, 5).equalsIgnoreCase("true")) {
                        sb.append("<td style='width:10%' ><input type='checkbox' checked id='SelectedSheetSTime").append(i + 1).append("' style='width:auto;color:black'  value='' /></td>");
                    } else if (pbro.getFieldValueString(i, 5).equalsIgnoreCase("false")) {
                        sb.append("<td style='width:10%' ><input type='checkbox'  id='SelectedSheetSTime").append(i + 1).append("'style='width:auto;color:black'  value='' /></td>");
                    }
                    sb.append(" </tr>");
                }

            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return html = sb.toString();
    }
    //added by Dinanath for customer help facility

    public String getCustomerHelpPersonNameAndEmailId() throws Exception {
        PbReturnObject tabList = new PbReturnObject();
        String query = "select SERIAL_NUM, HELP_PERSON_NAME, HELP_PERSON_EMAIL from METAMMT.[dbo].[CUSTOMER_HELP_INFO]";
//         String query = "select SERIAL_NUM, HELP_PERSON_NAME, HELP_PERSON_EMAIL from Meta_Demo1.[dbo].[CUSTOMER_HELP_INFO]";
      
        Connection con;
        ArrayList colData = new ArrayList();
        try {
            con = ProgenConnection.getInstance().getConnection();
            tabList = execSelectSQL(query, con);
            con.close();


            String[] tabCol = null;
            String qrystat = "";
            for (int i = 0; i < tabList.getRowCount(); i++) {
                colData.add(tabList.getFieldValue(i, "SERIAL_NUM").toString());
                colData.add(tabList.getFieldValue(i, "HELP_PERSON_NAME").toString());
                colData.add(tabList.getFieldValue(i, "HELP_PERSON_EMAIL").toString());
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return colData.toString();
    }
}

