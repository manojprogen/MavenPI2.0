/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class SaveTimeDimension extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(SaveTimeDimension.class);
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbBussGrpResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbBussGrpResBundleMysql();
            } else {
                resourceBundle = new PbBussGrpResourceBundle();
            }
        }

        return resourceBundle;
    }
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("button.add", "add");
        map.put("button.edit", "edit");
        map.put("button.delete", "delete");
        map.put("postgreSQLkey", "postgreSQL");
        return map;
    }

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {


        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {

            prg.db.PbDb pbdb = new prg.db.PbDb();

            String grpId = request.getParameter("grpId");
            String bussTableId = request.getParameter("bussTableId");
            String bussColId = request.getParameter("bussColId");
            String tabName = request.getParameter("tabName");
            String colName = request.getParameter("colName");
            String colType = request.getParameter("colType");
            String minTimeLevel = request.getParameter("minTimeLevel");
            String calender = request.getParameter("calender");
            String calenderId = calender.split("~")[0];
            String calenderTable = calender.split("~")[1];
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------------------------------------------------");
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in save time dimension--nmm-- "+calenderId+"---"+calenderTable+"-------->"+calender);
       /*
             * //for mintime level day if (minTimeLevel.equals("5")) { String
             * timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");
             * //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------timeDimKeyValueDay------------"+timeDimKeyValueDay);
             * } else if (minTimeLevel.equals("4")) { //for mintime level week
             *
             * String timeDimKeyValueWeekno =
             * request.getParameter("timeDimKeyValueWeekno"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------timeDimKeyValueWeekno------------"+timeDimKeyValueWeekno);
             * String timeDimKeyValueWeekyr =
             * request.getParameter("timeDimKeyValueWeekyr"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------timeDimKeyValueWeekyr------------"+timeDimKeyValueWeekyr);
             *
             * } else if (minTimeLevel.equals("3")) { //for mintime level month
             * String monthFormatyn = request.getParameter("monthcheck");
             * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------monthFormatyn------------"+monthFormatyn);
             * if (monthFormatyn.equals("Y")) { String monthPre =
             * request.getParameter("monthPre"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------monthPre------------"+monthPre);
             * String monthFormat = request.getParameter("monthFormat"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------monthFormat------------"+monthFormat);
             * String monthPost = request.getParameter("monthPost"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------monthPost------------"+monthPost);
             * String timeDimKeyValuemonth =
             * request.getParameter("timeDimKeyValuemonth"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------timeDimKeyValuemonth------------"+timeDimKeyValuemonth);
             * } else { String timeDimKeyValueMonthyr =
             * request.getParameter("timeDimKeyValueMonthyr"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------timeDimKeyValueMonthyr------------"+timeDimKeyValueMonthyr);
             * String timeDimKeyValueMonthno =
             * request.getParameter("timeDimKeyValueMonthno"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------timeDimKeyValueMonthno------------"+timeDimKeyValueMonthno);
             * } } else if (minTimeLevel.equals("2")) { //for mintime level
             * Quarter String timeDimKeyValueQuaterno =
             * request.getParameter("timeDimKeyValueQuaterno"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------timeDimKeyValueQuaterno------------"+timeDimKeyValueQuaterno);
             * String timeDimKeyValueQuateryr =
             * request.getParameter("timeDimKeyValueQuateryr"); //
             * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------timeDimKeyValueQuateryr------------"+timeDimKeyValueQuateryr);
             * } else if (minTimeLevel.equals("1")) { //for mintime level Year
             * String timeDimKeyValueYear =
             * request.getParameter("timeDimKeyValueYear"); }
             */
            String colNames[] = null;
            String colTypes[] = null;
            String colLength[] = null;
            String TimeDimMemName[] = {"Year", "Quarter", "Month", "Week", "Day"};
            String TimeDimMemDesc[] = {"Year", "Quarter", "Month", "Week", "Day"};
            String TimeDimMemDenomQuery[] = new String[5];

            TimeDimMemDenomQuery[0] = "SELECT DISTINCT CY_CUST_NAME,CY_CUST_NAME FROM " + calenderTable;
            TimeDimMemDenomQuery[1] = "SELECT DISTINCT CQ_CUST_NAME,CQ_CUST_NAME FROM " + calenderTable;
            TimeDimMemDenomQuery[2] = "SELECT DISTINCT CM_CUST_NAME,CM_CUST_NAME FROM " + calenderTable;
            TimeDimMemDenomQuery[3] = "SELECT DISTINCT CW_CUST_NAME,CW_CUST_NAME FROM " + calenderTable;
            TimeDimMemDenomQuery[4] = "SELECT DISTINCT DDATE,DDATE FROM " + calenderTable;


            ArrayList timeDimList = new ArrayList();
            String finalQuery = "";
//            PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
            //for adding into grp_buss_table
            String getBusinessTableConnectionId = getResourceBundle().getString("getBusinessTableConnectionId");
            Object objc[] = new Object[1];
            objc[0] = bussTableId;
            finalQuery = pbdb.buildQuery(getBusinessTableConnectionId, objc);
            ////.println("getBusinessTableConnectionId\t" + finalQuery);
            PbReturnObject pbroConId = pbdb.execSelectSQL(finalQuery);
            String ConnectionId = String.valueOf(pbroConId.getFieldValueInt(0, 0));
            String username = pbroConId.getFieldValueString(0, 1).toUpperCase();
            String dbName = pbroConId.getFieldValueString(0, 2);
            //getting details columns from pr_day_denom

            BusinessGroupDAO b = new BusinessGroupDAO();
            PbDb pbDb = new PbDb();
            Connection con = b.getBussTableConnection(bussTableId);
            //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("----------con-------"+con);
            String conntype = b.getBussTableConnectionDbType(bussTableId);
            // Statement st = con.createStatement();
            PbReturnObject denom = null;


            if (!(conntype.equalsIgnoreCase("PostgreSQL"))) {
                String allColsSql;

                if (conntype.equalsIgnoreCase("SqlServer")) {
                    allColsSql = "select column_name, data_type, character_maximum_length data_length from information_schema.columns  where table_name='" + calenderTable.toUpperCase() + "' ";
                } else if (conntype.equalsIgnoreCase("mysql")) {
                    allColsSql = "SELECT COLUMN_NAME,DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE  TABLE_SCHEMA='" + dbName + "' AND TABLE_NAME ='" + calenderTable + "'";
                } else {
                    allColsSql = "select column_name, data_type, data_length from all_tab_cols  where owner='" + username + "' and table_name='" + calenderTable + "' ";
                }
                if (minTimeLevel.equals("5")) {
//                    if(con == null){
                    denom = pbDb.execSelectSQL(allColsSql);
//                    } else {
//                        denom = pbDb.execSelectSQL(allColsSql,con);
//                    }
                } else if (minTimeLevel.equals("4")) {
                    denom = pbDb.execSelectSQL(allColsSql + "and (column_name like 'CW%' or column_name like 'PW%' or column_name like 'LYW%' or column_name like 'CM%' or column_name like 'PM%' or column_name like 'PYM%' or column_name like 'CQ%' or column_name like 'LQ%' or column_name like 'LYQ%' or column_name like 'CY%' or column_name like 'LY%')", con);

                } else if (minTimeLevel.equals("3")) {
                    denom = pbDb.execSelectSQL(allColsSql + "and column_name in ('CM_CUST_NAME','LY_END_DATE','CQ_YEAR','LYQ_ST_DATE','CY_END_DATE','CM_ST_DATE','PYM_ST_DATE','PYM_END_DATE','CQ_ST_DATE','CQ_END_DATE',"
                            + "'CY_ST_DATE','LY_CUST_NAME','CMONTH','CM_END_DATE','PYM_CUST_NAME','LQ_CUST_NAME','PM_CUST_NAME','LQ_ST_DATE','LQ_END_DATE','LY_ST_DATE','CM_YEAR','LYQ_END_DATE',"
                            + "'LYQ_CUST_NAME','CY_CUST_NAME','PM_ST_DATE','CQTR','CQ_CUST_NAME','CYEAR','PM_END_DATE')", con);
                } else if (minTimeLevel.equals("2")) {
                    denom = pbDb.execSelectSQL(allColsSql + "and (  column_name like 'CQ%' or column_name like 'LQ%' or column_name like 'LYQ%' or column_name like 'CY%' ) or column_name in ('LY_ST_DATE','LY_END_DATE','LY_CUST_NAME')", con);
                } else if (minTimeLevel.equals("1")) {
                    denom = pbDb.execSelectSQL(allColsSql + "and (column_name like 'CY%' ) or column_name in ('LY_ST_DATE','LY_END_DATE','LY_CUST_NAME')", con);
                }
                colNames = new String[denom.getRowCount()];
                colTypes = new String[denom.getRowCount()];
                String getColNames[] = denom.getColumnNames();
                String querystring = "";
                for (int i = 0; i < denom.getRowCount(); i++) {
                    colNames[i] = denom.getFieldValueString(i, getColNames[0]);
                    colTypes[i] = denom.getFieldValueString(i, getColNames[1]);
                    querystring += "," + denom.getFieldValueString(i, getColNames[0]);
                }
                if (denom.getRowCount() > 0) {
                    querystring = querystring.substring(1);
                }
                // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("pr denom-----------"+denom.getRowCount()+"user name---"+username);
//                st.close();
                con = null;
                //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("con------------"+con);
//               Connection con1 =ProgenConnection.getInstance().getConnection();
                // Statement batchSt = ProgenConnection.getInstance().getConnection().createStatement();
                ArrayList queryList = new ArrayList();
                if (denom.getRowCount() > 0) {
                    String addTimeDimBussMater = getResourceBundle().getString("addTimeDimBussMater");
                    int seqaddTimeDimBussMater = 0;
                    Object obj[];
                    if (conntype.equalsIgnoreCase("SqlServer")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            PbReturnObject seqReturnObj = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE')");
                            seqaddTimeDimBussMater = seqReturnObj.getFieldValueInt(0, 0) + 1;
                            obj = new Object[7];
//                        obj[0] = seqaddTimeDimBussMater;
                            obj[0] = "PROGEN_TIME";
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "Query";
                            obj[3] = 1;
                            obj[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[5] = grpId;
                            obj[6] = calenderTable;
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            PbReturnObject seqReturnObj = pbdb.execSelectSQL("select Last_Insert_ID(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1");
                            seqaddTimeDimBussMater = seqReturnObj.getFieldValueInt(0, 0) + 1;
                            obj = new Object[7];
//                        obj[0] = seqaddTimeDimBussMater;
                            obj[0] = "PROGEN_TIME";
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "Query";
                            obj[3] = 1;
                            obj[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[5] = grpId;
                            obj[6] = calenderTable;
                        } else {
                            seqaddTimeDimBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                            obj = new Object[8];
                            obj[0] = seqaddTimeDimBussMater;
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "PROGEN_TIME";
                            obj[3] = "Query";
                            obj[4] = 1;
                            obj[5] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[6] = grpId;
                            obj[7] = calenderTable;
                        }
                    } else if (conntype.equalsIgnoreCase("Mysql")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            PbReturnObject seqReturnObj = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE')");
                            seqaddTimeDimBussMater = seqReturnObj.getFieldValueInt(0, 0) + 1;
                            obj = new Object[7];
//                        obj[0] = seqaddTimeDimBussMater;
                            obj[0] = "PROGEN_TIME";
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "Query";
                            obj[3] = 1;
                            obj[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[5] = grpId;
                            obj[6] = calenderTable;
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            PbReturnObject seqReturnObj = pbdb.execSelectSQL("select Last_Insert_ID(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1");
                            seqaddTimeDimBussMater = seqReturnObj.getFieldValueInt(0, 0) + 1;
                            obj = new Object[7];
//                        obj[0] = seqaddTimeDimBussMater;
                            obj[0] = "PROGEN_TIME";
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "Query";
                            obj[3] = 1;
                            obj[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[5] = grpId;
                            obj[6] = calenderTable;
                        } else {
                            seqaddTimeDimBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                            obj = new Object[8];
                            obj[0] = seqaddTimeDimBussMater;
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "PROGEN_TIME";
                            obj[3] = "Query";
                            obj[4] = 1;
                            obj[5] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[6] = grpId;
                            obj[7] = calenderTable;
                        }
                    } else {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            PbReturnObject seqReturnObj = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE')");
                            seqaddTimeDimBussMater = seqReturnObj.getFieldValueInt(0, 0) + 1;
                            obj = new Object[7];
//                        obj[0] = seqaddTimeDimBussMater;
                            obj[0] = "PROGEN_TIME";
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "Query";
                            obj[3] = 1;
                            obj[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[5] = grpId;
                            obj[6] = calenderTable;
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            PbReturnObject seqReturnObj = pbdb.execSelectSQL("select Last_Insert_ID(BUSS_TABLE_ID) from PRG_GRP_BUSS_TABLE order by 1 desc limit 1");
                            seqaddTimeDimBussMater = seqReturnObj.getFieldValueInt(0, 0) + 1;
                            obj = new Object[7];
//                        obj[0] = seqaddTimeDimBussMater;
                            obj[0] = "PROGEN_TIME";
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "Query";
                            obj[3] = 1;
                            obj[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[5] = grpId;
                            obj[6] = calenderTable;
                        } else {
                            seqaddTimeDimBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                            obj = new Object[8];
                            obj[0] = seqaddTimeDimBussMater;
                            obj[1] = "PROGEN_TIME";
                            obj[2] = "PROGEN_TIME";
                            obj[3] = "Query";
                            obj[4] = 1;
                            obj[5] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                            obj[6] = grpId;
                            obj[7] = calenderTable;
                        }
                    }



                    finalQuery = pbdb.buildQuery(addTimeDimBussMater, obj);
                    ////.println("PROGEN_TIME final addTimeDimBussMater ---->" + finalQuery);
                    queryList.add(finalQuery);
                    // timeDimList.add(finalQuery);

                    String addTimeDimSrc = getResourceBundle().getString("addTimeDimSrc");

                    int seqaddTimeDimSrc = 0;
                    Object obj1[];
                    if (conntype.equalsIgnoreCase("SqlServer")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            PbReturnObject seqReturnObj1 = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE_SRC')");
                            seqaddTimeDimSrc = seqReturnObj1.getFieldValueInt(0, 0);
                            obj1 = new Object[3];
//                        obj1[0] = seqaddTimeDimSrc;
//                        obj1[0] = seqaddTimeDimBussMater;
                            obj1[0] = "Query";
                            obj1[1] = ConnectionId;
                            obj1[2] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            PbReturnObject seqReturnObj1 = pbdb.execSelectSQL("select Last_Insert_ID(BUSS_SOURCE_ID) from PRG_GRP_BUSS_TABLE_SRC order by 1 desc limit 1");
                            seqaddTimeDimSrc = seqReturnObj1.getFieldValueInt(0, 0);
                            obj1 = new Object[3];
//                        obj1[0] = seqaddTimeDimSrc;
//                        obj1[0] = seqaddTimeDimBussMater;
                            obj1[0] = "Query";
                            obj1[1] = ConnectionId;
                            obj1[2] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        } else {
                            seqaddTimeDimSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                            obj1 = new Object[5];
                            obj1[0] = seqaddTimeDimSrc;
                            obj1[1] = seqaddTimeDimBussMater;
                            obj1[2] = "Query";
                            obj1[3] = ConnectionId;
                            obj1[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        }
                    } else if (conntype.equalsIgnoreCase("Mysql")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            PbReturnObject seqReturnObj1 = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE_SRC')");
                            seqaddTimeDimSrc = seqReturnObj1.getFieldValueInt(0, 0);
                            obj1 = new Object[3];
//                        obj1[0] = seqaddTimeDimSrc;
//                        obj1[0] = seqaddTimeDimBussMater;
                            obj1[0] = "Query";
                            obj1[1] = ConnectionId;
                            obj1[2] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            PbReturnObject seqReturnObj1 = pbdb.execSelectSQL("select Last_Insert_ID(BUSS_SOURCE_ID) from PRG_GRP_BUSS_TABLE_SRC order by 1 desc limit 1");
                            seqaddTimeDimSrc = seqReturnObj1.getFieldValueInt(0, 0);
                            obj1 = new Object[3];
//                        obj1[0] = seqaddTimeDimSrc;
//                        obj1[0] = seqaddTimeDimBussMater;
                            obj1[0] = "Query";
                            obj1[1] = ConnectionId;
                            obj1[2] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        } else {
                            seqaddTimeDimSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                            obj1 = new Object[5];
                            obj1[0] = seqaddTimeDimSrc;
                            obj1[1] = seqaddTimeDimBussMater;
                            obj1[2] = "Query";
                            obj1[3] = ConnectionId;
                            obj1[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        }
                    } else {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            PbReturnObject seqReturnObj1 = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE_SRC')");
                            seqaddTimeDimSrc = seqReturnObj1.getFieldValueInt(0, 0);
                            obj1 = new Object[3];
//                        obj1[0] = seqaddTimeDimSrc;
//                        obj1[0] = seqaddTimeDimBussMater;
                            obj1[0] = "Query";
                            obj1[1] = ConnectionId;
                            obj1[2] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            PbReturnObject seqReturnObj1 = pbdb.execSelectSQL("select Last_Insert_ID(BUSS_SOURCE_ID) from PRG_GRP_BUSS_TABLE_SRC order by 1 desc limit 1");
                            seqaddTimeDimSrc = seqReturnObj1.getFieldValueInt(0, 0);
                            obj1 = new Object[3];
//                        obj1[0] = seqaddTimeDimSrc;
//                        obj1[0] = seqaddTimeDimBussMater;
                            obj1[0] = "Query";
                            obj1[1] = ConnectionId;
                            obj1[2] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        } else {
                            seqaddTimeDimSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                            obj1 = new Object[5];
                            obj1[0] = seqaddTimeDimSrc;
                            obj1[1] = seqaddTimeDimBussMater;
                            obj1[2] = "Query";
                            obj1[3] = ConnectionId;
                            obj1[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                            finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                        }
                    }



                    ////.println("final addTimeDimSrc ---->" + finalQuery);
                    // timeDimList.add(finalQuery);


                    queryList.add(finalQuery);



                    //add grp buss src details
                    String srcdetnextvalarr[] = new String[denom.getRowCount()];
                    for (int i = 0; i < denom.getRowCount(); i++) {
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            PbReturnObject returnObjectSrcDet = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TAB_SRC_DTLS')");

                            srcdetnextvalarr[i] = String.valueOf(returnObjectSrcDet.getFieldValueInt(0, 0) + i);
                            String addTimeDimDetails = getResourceBundle().getString("addTimeDimSrcDetails");
                            Object obj2[] = new Object[3];
//                        obj2[0] = srcdetnextval;
                            obj2[0] = seqaddTimeDimSrc + i;
//                            obj2[1] = seqaddTimeDimBussMater;
                            obj2[1] = colNames[i];
                            obj2[2] = colTypes[i];
                            finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                PbReturnObject returnObjectSrcDet = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TAB_SRC_DTLS')");

                                srcdetnextvalarr[i] = String.valueOf(returnObjectSrcDet.getFieldValueInt(0, 0) + i);
                                String addTimeDimDetails = getResourceBundle().getString("addTimeDimSrcDetails");
                                Object obj2[] = new Object[3];
//                        obj2[0] = srcdetnextval;
                                obj2[0] = seqaddTimeDimSrc + i;
//                            obj2[1] = seqaddTimeDimBussMater;
                                obj2[1] = colNames[i];
                                obj2[2] = colTypes[i];
                                finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                PbReturnObject returnObjectSrcDet = pbdb.execSelectSQL("select Last_Insert_ID(BUSS_SRC_TABLE_DTL_ID) from prg_grp_buss_table_src_details order by 1 desc limit 1");

                                srcdetnextvalarr[i] = String.valueOf(returnObjectSrcDet.getFieldValueInt(0, 0) + i);
                                String addTimeDimDetails = getResourceBundle().getString("addTimeDimSrcDetails");
                                Object obj2[] = new Object[3];
//                        obj2[0] = srcdetnextval;
                                obj2[0] = seqaddTimeDimSrc + i;
//                            obj2[1] = seqaddTimeDimBussMater;
                                obj2[1] = colNames[i];
                                obj2[2] = colTypes[i];
                                finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                            } else {
                                PbReturnObject srcseqval = pbdb.execSelectSQL("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                                int srcdetnextval = srcseqval.getFieldValueInt(0, "NEXTVAL");
                                srcdetnextvalarr[i] = String.valueOf(srcdetnextval);
                                String addTimeDimDetails = getResourceBundle().getString("addTimeDimSrcDetails");
                                Object obj2[] = new Object[5];
                                obj2[0] = srcdetnextval;
                                obj2[1] = seqaddTimeDimSrc;
                                obj2[2] = seqaddTimeDimBussMater;
                                obj2[3] = colNames[i];
                                obj2[4] = colTypes[i];
                                finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                PbReturnObject returnObjectSrcDet = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TAB_SRC_DTLS')");

                                srcdetnextvalarr[i] = String.valueOf(returnObjectSrcDet.getFieldValueInt(0, 0) + i);
                                String addTimeDimDetails = getResourceBundle().getString("addTimeDimSrcDetails");
                                Object obj2[] = new Object[3];
//                        obj2[0] = srcdetnextval;
                                obj2[0] = seqaddTimeDimSrc + i;
//                            obj2[1] = seqaddTimeDimBussMater;
                                obj2[1] = colNames[i];
                                obj2[2] = colTypes[i];
                                finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                PbReturnObject returnObjectSrcDet = pbdb.execSelectSQL("select Last_Insert_ID(BUSS_SRC_TABLE_DTL_ID) from prg_grp_buss_table_src_details order by 1 desc limit 1");

                                srcdetnextvalarr[i] = String.valueOf(returnObjectSrcDet.getFieldValueInt(0, 0) + i);
                                String addTimeDimDetails = getResourceBundle().getString("addTimeDimSrcDetails");
                                Object obj2[] = new Object[3];
//                        obj2[0] = srcdetnextval;
                                obj2[0] = seqaddTimeDimSrc + i;
//                            obj2[1] = seqaddTimeDimBussMater;
                                obj2[1] = colNames[i];
                                obj2[2] = colTypes[i];
                                finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                            } else {
                                PbReturnObject srcseqval = pbdb.execSelectSQL("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                                int srcdetnextval = srcseqval.getFieldValueInt(0, "NEXTVAL");
                                srcdetnextvalarr[i] = String.valueOf(srcdetnextval);
                                String addTimeDimDetails = getResourceBundle().getString("addTimeDimSrcDetails");
                                Object obj2[] = new Object[5];
                                obj2[0] = srcdetnextval;
                                obj2[1] = seqaddTimeDimSrc;
                                obj2[2] = seqaddTimeDimBussMater;
                                obj2[3] = colNames[i];
                                obj2[4] = colTypes[i];
                                finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                            }
                        }

                        ////.println("final addTimeDimDetails ---->" + finalQuery);
                        queryList.add(finalQuery);

                    }
                    //add grp buss details

                    String seqaddTimeDimBussDetailsarr[] = new String[denom.getRowCount()];
                    //String memBussNames[]=new String[5];
                    //  String memBussIds[]=new String[5];
                    HashMap h = new HashMap();
                    String rltdateId = "";
                    String rltweeknoId = "";
                    String rltweekyearId = "";
                    String rltmonthnoId = "";
                    String rltmonthyearId = "";
                    String rltmonthstrId = "";
                    String rltmonthendId = "";
                    String rltqtrnoId = "";
                    String rltqtryearId = "";
                    String rltyearId = "";
                    int seqaddTimeDimBussDetails = 0;
                    for (int i = 0; i < denom.getRowCount(); i++) {
                        String addTimeDimBussDetails = getResourceBundle().getString("addTimeDimBussDetails");

                        Object[] obj3 = null;
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            obj3 = new Object[4];
                            PbReturnObject retDetSeq = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE_DETAILS')");
//                                    pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                            seqaddTimeDimBussDetails = retDetSeq.getFieldValueInt(0, 0) + i + 1;
                            seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                obj3 = new Object[4];
                                PbReturnObject retDetSeq = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE_DETAILS')");
//                                    pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                                seqaddTimeDimBussDetails = retDetSeq.getFieldValueInt(0, 0) + i + 1;
                                seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                obj3 = new Object[4];
                                PbReturnObject retDetSeq = pbdb.execSelectSQL("select Last_Insert_Id(BUSS_COLUMN_ID) from PRG_GRP_BUSS_TABLE_DETAILS Order by 1 Desc Limit 1");
//                                    pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                                seqaddTimeDimBussDetails = retDetSeq.getFieldValueInt(0, 0) + i + 1;
                                seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                            } else {
                                obj3 = new Object[6];
                                seqaddTimeDimBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                                seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                obj3 = new Object[4];
                                PbReturnObject retDetSeq = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE_DETAILS')");
//                                    pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                                seqaddTimeDimBussDetails = retDetSeq.getFieldValueInt(0, 0) + i + 1;
                                seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                obj3 = new Object[4];
                                PbReturnObject retDetSeq = pbdb.execSelectSQL("select Last_Insert_Id(BUSS_COLUMN_ID) from PRG_GRP_BUSS_TABLE_DETAILS Order by 1 Desc Limit 1");
//                                    pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                                seqaddTimeDimBussDetails = retDetSeq.getFieldValueInt(0, 0) + i + 1;
                                seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                            } else {
                                obj3 = new Object[6];
                                seqaddTimeDimBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                                seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                            }

                        }

                        //// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---------------------"+i+"----------------------");
                        if (colNames[i].equalsIgnoreCase("DDATE")) {

                            h.put("Day", String.valueOf(seqaddTimeDimBussDetails));
                            //// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------Day------"+h.get("Day"));
                        } else if (colNames[i].equalsIgnoreCase("CW_CUST_NAME")) {

                            h.put("Week", String.valueOf(seqaddTimeDimBussDetails));
                            // // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------week------"+h.get("Week"));
                        } else if (colNames[i].equalsIgnoreCase("CM_CUST_NAME")) {

                            h.put("Month", String.valueOf(seqaddTimeDimBussDetails));
                            //// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------Month------"+h.get("Month"));
                        } else if (colNames[i].equalsIgnoreCase("CQ_CUST_NAME")) {

                            h.put("Quarter", String.valueOf(seqaddTimeDimBussDetails));
                            // // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------Quarter------"+h.get("Quarter"));

                        } else if (colNames[i].equalsIgnoreCase("CY_CUST_NAME")) {

                            h.put("Year", String.valueOf(seqaddTimeDimBussDetails));
                            //// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------Year------"+h.get("Year"));
                        }
                        if (colNames[i].equalsIgnoreCase("DDATE")) {
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------------");
                            rltdateId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltdateId-------"+rltdateId);
                        } else if (colNames[i].equalsIgnoreCase("CWEEK")) {
                            rltweeknoId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltweeknoId-------"+rltweeknoId);
                        } else if (colNames[i].equalsIgnoreCase("CWYEAR")) {
                            rltweekyearId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltweekyearId-------"+rltweekyearId);
                        } else if (colNames[i].equalsIgnoreCase("CMONTH")) {
                            rltmonthnoId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthnoId-------"+rltmonthnoId);
                        } else if (colNames[i].equalsIgnoreCase("CM_YEAR")) {
                            rltmonthyearId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthyearId-------"+rltmonthyearId);
                        } else if (colNames[i].equalsIgnoreCase("CM_ST_DATE")) {
                            rltmonthstrId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthstrId-------"+rltmonthstrId);
                        } else if (colNames[i].equalsIgnoreCase("CM_END_DATE")) {
                            rltmonthendId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthendId-------"+rltmonthendId);
                        } else if (colNames[i].equalsIgnoreCase("CQTR")) {
                            rltqtrnoId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltqtrnoId-------"+rltqtrnoId);
                        } else if (colNames[i].equalsIgnoreCase("CQ_YEAR")) {
                            rltqtryearId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltqtryearId-------"+rltqtryearId);
                        } else if (colNames[i].equalsIgnoreCase("CYEAR")) {
                            rltyearId = String.valueOf(seqaddTimeDimBussDetails);
                            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltyearId-------"+rltyearId);
                        }


                        if (conntype.equalsIgnoreCase("SqlServer")) {
//                              obj3[0] = seqaddTimeDimBussDetails;
//                            obj3[0] = seqaddTimeDimBussMater;
                            obj3[0] = colNames[i];
                            obj3[1] = srcdetnextvalarr[i];
                            obj3[2] = colTypes[i];
                            obj3[3] = colNames[i];
                            finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                            ////.println("final addTimeDim bussDetails ---->" + finalQuery);
                            queryList.add(finalQuery);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                obj3[0] = colNames[i];
                                obj3[1] = srcdetnextvalarr[i];
                                obj3[2] = colTypes[i];
                                obj3[3] = colNames[i];
                                finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                obj3[0] = colNames[i];
                                obj3[1] = srcdetnextvalarr[i];
                                obj3[2] = colTypes[i];
                                obj3[3] = colNames[i];
                                finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                                ////.println("final addTimeDim bussDetails ---->" + finalQuery);
                                queryList.add(finalQuery);
                            } else {
                                obj3[0] = seqaddTimeDimBussDetails;
                                obj3[1] = seqaddTimeDimBussMater;
                                obj3[2] = colNames[i];
                                obj3[3] = srcdetnextvalarr[i];
                                obj3[4] = colTypes[i];
                                obj3[5] = colNames[i];
                                finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                                ////.println("final addTimeDim bussDetails ---->" + finalQuery);
                                queryList.add(finalQuery);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                obj3[0] = colNames[i];
                                obj3[1] = srcdetnextvalarr[i];
                                obj3[2] = colTypes[i];
                                obj3[3] = colNames[i];
                                finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                obj3[0] = colNames[i];
                                obj3[1] = srcdetnextvalarr[i];
                                obj3[2] = colTypes[i];
                                obj3[3] = colNames[i];
                                finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                                ////.println("final addTimeDim bussDetails ---->" + finalQuery);
                                queryList.add(finalQuery);
                            } else {
                                obj3[0] = seqaddTimeDimBussDetails;
                                obj3[1] = seqaddTimeDimBussMater;
                                obj3[2] = colNames[i];
                                obj3[3] = srcdetnextvalarr[i];
                                obj3[4] = colTypes[i];
                                obj3[5] = colNames[i];
                                finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                                ////.println("final addTimeDim bussDetails ---->" + finalQuery);
                                queryList.add(finalQuery);
                            }
                        }


                    }
                    //add grp rlt master
                    //add grp rlt details
//              Connection connection =ProgenConnection.getInstance().getConnection();
                    // Statement batchSt1 = ProgenConnection.getInstance().getConnection().createStatement();
                    ArrayList queryList1 = new ArrayList();
                    //add dimensions
                    String addTimeDimDimension = getResourceBundle().getString("addTimeDimDimension");
                    int seqaddTimeDimDimension = 0;
                    if (conntype.equalsIgnoreCase("SqlServer")) {
                        Object obj4[] = new Object[4];
//                    obj4[0] = seqaddTimeDimDimension;
                        obj4[0] = "Time";
                        obj4[1] = "Time";
                        obj4[2] = "Y";
                        obj4[3] = grpId;
                        finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                        ////.println("final addTimeDimDimension ---->" + finalQuery);
                        queryList1.add(finalQuery);

                    } else if (conntype.equalsIgnoreCase("Mysql")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            Object obj4[] = new Object[4];
//                    obj4[0] = seqaddTimeDimDimension;
                            obj4[0] = "Time";
                            obj4[1] = "Time";
                            obj4[2] = "Y";
                            obj4[3] = grpId;
                            finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                            ////.println("final addTimeDimDimension ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            Object obj4[] = new Object[4];
//                    obj4[0] = seqaddTimeDimDimension;
                            obj4[0] = "Time";
                            obj4[1] = "Time";
                            obj4[2] = "Y";
                            obj4[3] = grpId;
                            finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                            ////.println("final addTimeDimDimension ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else {
                            seqaddTimeDimDimension = pbdb.getSequenceNumber("select  PRG_GRP_DIMENSIONS_SEQ.nextval from dual");
                            Object obj4[] = new Object[5];
                            obj4[0] = seqaddTimeDimDimension;
                            obj4[1] = "Time";
                            obj4[2] = "Time";
                            obj4[3] = "Y";
                            obj4[4] = grpId;
                            finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                            ////.println("final addTimeDimDimension ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        }

                    } else {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            Object obj4[] = new Object[4];
//                    obj4[0] = seqaddTimeDimDimension;
                            obj4[0] = "Time";
                            obj4[1] = "Time";
                            obj4[2] = "Y";
                            obj4[3] = grpId;
                            finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                            ////.println("final addTimeDimDimension ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            Object obj4[] = new Object[4];
//                    obj4[0] = seqaddTimeDimDimension;
                            obj4[0] = "Time";
                            obj4[1] = "Time";
                            obj4[2] = "Y";
                            obj4[3] = grpId;
                            finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                            ////.println("final addTimeDimDimension ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else {
                            seqaddTimeDimDimension = pbdb.getSequenceNumber("select  PRG_GRP_DIMENSIONS_SEQ.nextval from dual");
                            Object obj4[] = new Object[5];
                            obj4[0] = seqaddTimeDimDimension;
                            obj4[1] = "Time";
                            obj4[2] = "Time";
                            obj4[3] = "Y";
                            obj4[4] = grpId;
                            finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                            ////.println("final addTimeDimDimension ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        }
                    }



                    //add grp dimension table
                    String addTimeDimTables = getResourceBundle().getString("addTimeDimTables");
                    int seqaddTimeDimTables = 0;
                    if (conntype.equalsIgnoreCase("SqlServer")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            Object obj5[] = new Object[1];
//                    obj5[0] = seqaddTimeDimTables;
                            obj5[0] = seqaddTimeDimBussMater;
//                    obj5[1] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            Object obj5[] = new Object[1];
//                    obj5[0] = seqaddTimeDimTables;
                            obj5[0] = seqaddTimeDimBussMater;
//                    obj5[1] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else {
                            seqaddTimeDimTables = pbdb.getSequenceNumber("select  PRG_GRP_DIM_TABLES_SEQ.nextval from dual");
                            Object obj5[] = new Object[3];
                            obj5[0] = seqaddTimeDimTables;
                            obj5[1] = seqaddTimeDimDimension;
                            obj5[2] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        }
                    } else if (conntype.equalsIgnoreCase("Mysql")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            Object obj5[] = new Object[1];
//                    obj5[0] = seqaddTimeDimTables;
                            obj5[0] = seqaddTimeDimBussMater;
//                    obj5[1] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            Object obj5[] = new Object[1];
//                    obj5[0] = seqaddTimeDimTables;
                            obj5[0] = seqaddTimeDimBussMater;
//                    obj5[1] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else {
                            seqaddTimeDimTables = pbdb.getSequenceNumber("select  PRG_GRP_DIM_TABLES_SEQ.nextval from dual");
                            Object obj5[] = new Object[3];
                            obj5[0] = seqaddTimeDimTables;
                            obj5[1] = seqaddTimeDimDimension;
                            obj5[2] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        }
                    } else {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            Object obj5[] = new Object[1];
//                    obj5[0] = seqaddTimeDimTables;
                            obj5[0] = seqaddTimeDimBussMater;
//                    obj5[1] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            Object obj5[] = new Object[1];
//                    obj5[0] = seqaddTimeDimTables;
                            obj5[0] = seqaddTimeDimBussMater;
//                    obj5[1] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else {
                            seqaddTimeDimTables = pbdb.getSequenceNumber("select  PRG_GRP_DIM_TABLES_SEQ.nextval from dual");
                            Object obj5[] = new Object[3];
                            obj5[0] = seqaddTimeDimTables;
                            obj5[1] = seqaddTimeDimDimension;
                            obj5[2] = seqaddTimeDimBussMater;
                            finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                            ////.println("final addTimeDimTables ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        }
                    }



                    //add grp dimension table details
                    for (int i = 0; i < denom.getRowCount(); i++) {
                        String addTimeDimTableDetails = getResourceBundle().getString("addTimeDimTableDetails");
                        if (conntype.equalsIgnoreCase("SqlServer")) {
//                             int seqaddTimeDimTableDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval from dual");
                            Object obj6[] = new Object[3];
//                        obj6[0] = seqaddTimeDimTableDetails;
//                        obj6[0] = seqaddTimeDimTables;
                            obj6[0] = seqaddTimeDimBussDetailsarr[i];
                            obj6[1] = "Y";
                            obj6[2] = "N";
                            finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);

                        } else if (conntype.equalsIgnoreCase("Mysql")) {
//                             int seqaddTimeDimTableDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval from dual");
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj6[] = new Object[3];
//                        obj6[0] = seqaddTimeDimTableDetails;
//                        obj6[0] = seqaddTimeDimTables;
                                obj6[0] = seqaddTimeDimBussDetailsarr[i];
                                obj6[1] = "Y";
                                obj6[2] = "N";
                                finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj6[] = new Object[3];
//                        obj6[0] = seqaddTimeDimTableDetails;
//                        obj6[0] = seqaddTimeDimTables;
                                obj6[0] = seqaddTimeDimBussDetailsarr[i];
                                obj6[1] = "Y";
                                obj6[2] = "N";
                                finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);
                            } else {
                                int seqaddTimeDimTableDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval from dual");
                                Object obj6[] = new Object[5];
                                obj6[0] = seqaddTimeDimTableDetails;
                                obj6[1] = seqaddTimeDimTables;
                                obj6[2] = seqaddTimeDimBussDetailsarr[i];
                                obj6[3] = "Y";
                                obj6[4] = "N";
                                finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);
                            }

                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj6[] = new Object[3];
//                        obj6[0] = seqaddTimeDimTableDetails;
//                        obj6[0] = seqaddTimeDimTables;
                                obj6[0] = seqaddTimeDimBussDetailsarr[i];
                                obj6[1] = "Y";
                                obj6[2] = "N";
                                finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj6[] = new Object[3];
//                        obj6[0] = seqaddTimeDimTableDetails;
//                        obj6[0] = seqaddTimeDimTables;
                                obj6[0] = seqaddTimeDimBussDetailsarr[i];
                                obj6[1] = "Y";
                                obj6[2] = "N";
                                finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);
                            } else {
                                int seqaddTimeDimTableDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval from dual");
                                Object obj6[] = new Object[5];
                                obj6[0] = seqaddTimeDimTableDetails;
                                obj6[1] = seqaddTimeDimTables;
                                obj6[2] = seqaddTimeDimBussDetailsarr[i];
                                obj6[3] = "Y";
                                obj6[4] = "N";
                                finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);
                            }
                        }


                        ////.println("final addTimeDimTables details ---->" + finalQuery);
                        queryList1.add(finalQuery);
                    }
                    //add grp dimension members
                    String level[] = new String[Integer.parseInt(minTimeLevel)];
                    String memberIds[] = new String[Integer.parseInt(minTimeLevel)];

                    for (int i = 0; i < Integer.parseInt(minTimeLevel); i++) {

                        String addTimeDimMembers = getResourceBundle().getString("addTimeDimMembers");
                        int seqaddTimeDimMembers = 0;
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            PbReturnObject retTiDimMemSeqObj = pbdb.execSelectSQL("select ident_current('PRG_GRP_DIM_MEMBER')");
                            seqaddTimeDimMembers = retTiDimMemSeqObj.getFieldValueInt(0, 0) + i + 1; // Added 1 for member id for details 
                            memberIds[i] = String.valueOf(seqaddTimeDimMembers);
//                        level[i] = String.valueOf(i + 1);
                            level[i] = String.valueOf(i + 1);
                            Object obj7[] = new Object[5];
//                        obj7[0] = seqaddTimeDimMembers;
                            obj7[0] = TimeDimMemName[i];
//                        obj7[1] = seqaddTimeDimDimension;//PRG_GRP_DIMENSIONS
//                        obj7[2] = seqaddTimeDimTables;//PRG_GRP_DIM_TABLES
                            obj7[1] = "Y";
//                            obj7[2] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                            obj7[2] = TimeDimMemDenomQuery[i];
                            obj7[3] = TimeDimMemDesc[i];
                            finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                PbReturnObject retTiDimMemSeqObj = pbdb.execSelectSQL("select ident_current('PRG_GRP_DIM_MEMBER')");
                                seqaddTimeDimMembers = retTiDimMemSeqObj.getFieldValueInt(0, 0) + i + 1; // Added 1 for member id for details
                                memberIds[i] = String.valueOf(seqaddTimeDimMembers);
//                        level[i] = String.valueOf(i + 1);
                                level[i] = String.valueOf(i + 1);
                                Object obj7[] = new Object[5];
//                        obj7[0] = seqaddTimeDimMembers;
                                obj7[0] = TimeDimMemName[i];
//                        obj7[1] = seqaddTimeDimDimension;//PRG_GRP_DIMENSIONS
//                        obj7[2] = seqaddTimeDimTables;//PRG_GRP_DIM_TABLES
                                obj7[1] = "Y";
//                            obj7[2] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj7[2] = TimeDimMemDenomQuery[i];
                                obj7[3] = TimeDimMemDesc[i];
                                finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                PbReturnObject retTiDimMemSeqObj = pbdb.execSelectSQL("select Last_Insert_Id(member_id) from PRG_GRP_DIM_MEMBER order by 1 desc limit 1");
                                seqaddTimeDimMembers = retTiDimMemSeqObj.getFieldValueInt(0, 0) + i + 1; // Added 1 for member id for details
                                memberIds[i] = String.valueOf(seqaddTimeDimMembers);
//                        level[i] = String.valueOf(i + 1);
                                level[i] = String.valueOf(i + 1);
                                Object obj7[] = new Object[5];
//                        obj7[0] = seqaddTimeDimMembers;
                                obj7[0] = TimeDimMemName[i];
//                        obj7[1] = seqaddTimeDimDimension;//PRG_GRP_DIMENSIONS
//                        obj7[2] = seqaddTimeDimTables;//PRG_GRP_DIM_TABLES
                                obj7[1] = "Y";
//                            obj7[2] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj7[2] = TimeDimMemDenomQuery[i];
                                obj7[3] = TimeDimMemDesc[i];
                                finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);
                            } else {
                                seqaddTimeDimMembers = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_SEQ.nextval from dual");
                                memberIds[i] = String.valueOf(seqaddTimeDimMembers);
                                level[i] = String.valueOf(i + 1);
                                Object obj7[] = new Object[8];
                                obj7[0] = seqaddTimeDimMembers;
                                obj7[1] = TimeDimMemName[i];
                                obj7[2] = seqaddTimeDimDimension;
                                obj7[3] = seqaddTimeDimTables;
                                obj7[4] = "Y";
                                obj7[5] = seqaddTimeDimBussMater;
                                obj7[6] = TimeDimMemDenomQuery[i];
                                obj7[7] = TimeDimMemDesc[i];
                                finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                PbReturnObject retTiDimMemSeqObj = pbdb.execSelectSQL("select ident_current('PRG_GRP_DIM_MEMBER')");
                                seqaddTimeDimMembers = retTiDimMemSeqObj.getFieldValueInt(0, 0) + i + 1; // Added 1 for member id for details
                                memberIds[i] = String.valueOf(seqaddTimeDimMembers);
//                        level[i] = String.valueOf(i + 1);
                                level[i] = String.valueOf(i + 1);
                                Object obj7[] = new Object[5];
//                        obj7[0] = seqaddTimeDimMembers;
                                obj7[0] = TimeDimMemName[i];
//                        obj7[1] = seqaddTimeDimDimension;//PRG_GRP_DIMENSIONS
//                        obj7[2] = seqaddTimeDimTables;//PRG_GRP_DIM_TABLES
                                obj7[1] = "Y";
//                            obj7[2] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj7[2] = TimeDimMemDenomQuery[i];
                                obj7[3] = TimeDimMemDesc[i];
                                finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                PbReturnObject retTiDimMemSeqObj = pbdb.execSelectSQL("select Last_Insert_Id(member_id) from PRG_GRP_DIM_MEMBER order by 1 desc limit 1");
                                seqaddTimeDimMembers = retTiDimMemSeqObj.getFieldValueInt(0, 0) + i + 1; // Added 1 for member id for details
                                memberIds[i] = String.valueOf(seqaddTimeDimMembers);
//                        level[i] = String.valueOf(i + 1);
                                level[i] = String.valueOf(i + 1);
                                Object obj7[] = new Object[5];
//                        obj7[0] = seqaddTimeDimMembers;
                                obj7[0] = TimeDimMemName[i];
//                        obj7[1] = seqaddTimeDimDimension;//PRG_GRP_DIMENSIONS
//                        obj7[2] = seqaddTimeDimTables;//PRG_GRP_DIM_TABLES
                                obj7[1] = "Y";
//                            obj7[2] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj7[2] = TimeDimMemDenomQuery[i];
                                obj7[3] = TimeDimMemDesc[i];
                                finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);
                            } else {
                                seqaddTimeDimMembers = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_SEQ.nextval from dual");
                                memberIds[i] = String.valueOf(seqaddTimeDimMembers);
                                level[i] = String.valueOf(i + 1);
                                Object obj7[] = new Object[8];
                                obj7[0] = seqaddTimeDimMembers;
                                obj7[1] = TimeDimMemName[i];
                                obj7[2] = seqaddTimeDimDimension;
                                obj7[3] = seqaddTimeDimTables;
                                obj7[4] = "Y";
                                obj7[5] = seqaddTimeDimBussMater;
                                obj7[6] = TimeDimMemDenomQuery[i];
                                obj7[7] = TimeDimMemDesc[i];
                                finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);
                            }
                        }


                        ////.println("final addTimeDimMembers ---->" + finalQuery);
                        queryList1.add(finalQuery);

                        //add member details
                        String bussdltId = "";
                        if (TimeDimMemName[i].equalsIgnoreCase("Year")) {
                            bussdltId = h.get("Year").toString();
                        } else if (TimeDimMemName[i].equalsIgnoreCase("Quarter")) {
                            bussdltId = h.get("Quarter").toString();
                        } else if (TimeDimMemName[i].equalsIgnoreCase("Month")) {
                            bussdltId = h.get("Month").toString();
                        } else if (TimeDimMemName[i].equalsIgnoreCase("Week")) {
                            bussdltId = h.get("Week").toString();
                        } else if (TimeDimMemName[i].equalsIgnoreCase("Day")) {
                            bussdltId = h.get("Day").toString();
                        }
                        String addTimeDimMemberDetails = getResourceBundle().getString("addTimeDimMemberDetails");
                        if (conntype.equalsIgnoreCase("SqlServer")) {
//                            int seqaddTimeDimMemberDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                            Object obj8[] = new Object[4];
//                            obj8[0] = seqaddTimeDimMemberDetails;
                            obj8[0] = seqaddTimeDimMembers;//PRG_GRP_DIM_MEMBER
                            obj8[1] = bussdltId;
                            obj8[2] = "KEY";
                            obj8[3] = "null";

                            finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
//                            int seqaddTimeDimMemberDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj8[] = new Object[4];
//                            obj8[0] = seqaddTimeDimMemberDetails;
                                obj8[0] = seqaddTimeDimMembers;//PRG_GRP_DIM_MEMBER
                                obj8[1] = bussdltId;
                                obj8[2] = "KEY";
                                obj8[3] = "null";

                                finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj8[] = new Object[4];
//                            obj8[0] = seqaddTimeDimMemberDetails;
                                obj8[0] = seqaddTimeDimMembers;//PRG_GRP_DIM_MEMBER
                                obj8[1] = bussdltId;
                                obj8[2] = "KEY";
                                obj8[3] = "null";

                                finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);
                            } else {
                                int seqaddTimeDimMemberDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                                Object obj8[] = new Object[5];
                                obj8[0] = seqaddTimeDimMemberDetails;
                                obj8[1] = seqaddTimeDimMembers;
                                obj8[2] = bussdltId;
                                obj8[3] = "KEY";
                                obj8[4] = "";

                                finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj8[] = new Object[4];
//                            obj8[0] = seqaddTimeDimMemberDetails;
                                obj8[0] = seqaddTimeDimMembers;//PRG_GRP_DIM_MEMBER
                                obj8[1] = bussdltId;
                                obj8[2] = "KEY";
                                obj8[3] = "null";

                                finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj8[] = new Object[4];
//                            obj8[0] = seqaddTimeDimMemberDetails;
                                obj8[0] = seqaddTimeDimMembers;//PRG_GRP_DIM_MEMBER
                                obj8[1] = bussdltId;
                                obj8[2] = "KEY";
                                obj8[3] = "null";

                                finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);
                            } else {
                                int seqaddTimeDimMemberDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                                Object obj8[] = new Object[5];
                                obj8[0] = seqaddTimeDimMemberDetails;
                                obj8[1] = seqaddTimeDimMembers;
                                obj8[2] = bussdltId;
                                obj8[3] = "KEY";
                                obj8[4] = "";

                                finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);
                            }
                        }


                        ////.println("final addTimeDimMemberkey Details ---->" + finalQuery);
                        queryList.add(finalQuery);


                        String addTimeDimMemberDetails1 = getResourceBundle().getString("addTimeDimMemberDetails");
                        if (conntype.equalsIgnoreCase("SqlServer")) {
//                            int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                            Object obj9[] = new Object[4];
//                        obj9[0] = seqaddTimeDimMemberDetails1;
                            obj9[0] = seqaddTimeDimMembers;
                            obj9[1] = bussdltId;
                            obj9[2] = "VALUE";
                            obj9[3] = "null";

                            finalQuery = pbdb.buildQuery(addTimeDimMemberDetails1, obj9);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                //                            int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                                Object obj9[] = new Object[4];
//                        obj9[0] = seqaddTimeDimMemberDetails1;
                                obj9[0] = seqaddTimeDimMembers;
                                obj9[1] = bussdltId;
                                obj9[2] = "VALUE";
                                obj9[3] = "null";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                //                            int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                                Object obj9[] = new Object[4];
//                        obj9[0] = seqaddTimeDimMemberDetails1;
                                obj9[0] = seqaddTimeDimMembers;
                                obj9[1] = bussdltId;
                                obj9[2] = "VALUE";
                                obj9[3] = "null";
                            } else {
                                int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                                Object obj9[] = new Object[5];
                                obj9[0] = seqaddTimeDimMemberDetails1;
                                obj9[1] = seqaddTimeDimMembers;
                                obj9[2] = bussdltId;
                                obj9[3] = "VALUE";
                                obj9[4] = "";

                                finalQuery = pbdb.buildQuery(addTimeDimMemberDetails1, obj9);
                            }


                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                //                            int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                                Object obj9[] = new Object[4];
//                        obj9[0] = seqaddTimeDimMemberDetails1;
                                obj9[0] = seqaddTimeDimMembers;
                                obj9[1] = bussdltId;
                                obj9[2] = "VALUE";
                                obj9[3] = "null";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                //                            int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                                Object obj9[] = new Object[4];
//                        obj9[0] = seqaddTimeDimMemberDetails1;
                                obj9[0] = seqaddTimeDimMembers;
                                obj9[1] = bussdltId;
                                obj9[2] = "VALUE";
                                obj9[3] = "null";
                            } else {
                                int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                                Object obj9[] = new Object[5];
                                obj9[0] = seqaddTimeDimMemberDetails1;
                                obj9[1] = seqaddTimeDimMembers;
                                obj9[2] = bussdltId;
                                obj9[3] = "VALUE";
                                obj9[4] = "";

                                finalQuery = pbdb.buildQuery(addTimeDimMemberDetails1, obj9);
                            }
                        }


                        ////.println("final addTimeDimMemberkey value Details ---->" + finalQuery);
                        queryList1.add(finalQuery);

                    }

                    //add Time Dim Hierachies(relations)
                    String addTimeDimRlt = getResourceBundle().getString("addTimeDimRlt");
                    int seqaddTimeDimRlt = 0;
                    if (conntype.equalsIgnoreCase("SqlServer")) {
//                        int seqaddTimeDimRlt = pbdb.getSequenceNumber("select PRG_GRP_DIM_REL_SEQ.nextval from dual");
                        Object obj10[] = new Object[4];
//                        obj10[0] = seqaddTimeDimRlt;
//                        obj10[0] = seqaddTimeDimDimension;//
                        obj10[0] = "Time";
                        obj10[1] = "N";
                        obj10[2] = "Time";

                        finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);

                    } else if (conntype.equalsIgnoreCase("Mysql")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            Object obj10[] = new Object[4];
//                        obj10[0] = seqaddTimeDimRlt;
//                        obj10[0] = seqaddTimeDimDimension;//
                            obj10[0] = "Time";
                            obj10[1] = "N";
                            obj10[2] = "Time";

                            finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            Object obj10[] = new Object[4];
//                        obj10[0] = seqaddTimeDimRlt;
//                        obj10[0] = seqaddTimeDimDimension;//
                            obj10[0] = "Time";
                            obj10[1] = "N";
                            obj10[2] = "Time";

                            finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);
                        } else {
                            seqaddTimeDimRlt = pbdb.getSequenceNumber("select PRG_GRP_DIM_REL_SEQ.nextval from dual");

                            Object obj10[] = new Object[5];
                            obj10[0] = seqaddTimeDimRlt;
                            obj10[1] = seqaddTimeDimDimension;
                            obj10[2] = "Time";
                            obj10[3] = "N";
                            obj10[4] = "Time";

                            finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);
                        }

                    } else {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            Object obj10[] = new Object[4];
//                        obj10[0] = seqaddTimeDimRlt;
//                        obj10[0] = seqaddTimeDimDimension;//
                            obj10[0] = "Time";
                            obj10[1] = "N";
                            obj10[2] = "Time";

                            finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            Object obj10[] = new Object[4];
//                        obj10[0] = seqaddTimeDimRlt;
//                        obj10[0] = seqaddTimeDimDimension;//
                            obj10[0] = "Time";
                            obj10[1] = "N";
                            obj10[2] = "Time";

                            finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);
                        } else {
                            seqaddTimeDimRlt = pbdb.getSequenceNumber("select PRG_GRP_DIM_REL_SEQ.nextval from dual");

                            Object obj10[] = new Object[5];
                            obj10[0] = seqaddTimeDimRlt;
                            obj10[1] = seqaddTimeDimDimension;
                            obj10[2] = "Time";
                            obj10[3] = "N";
                            obj10[4] = "Time";

                            finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);
                        }

                    }

                    ////.println("final addTimeDimRlt ---->" + finalQuery);
                    queryList.add(finalQuery);
                    //add Time Dim Relation Details
                    for (int i = 0; i < Integer.parseInt(minTimeLevel); i++) {
                        String addTimeDimRltDetails = getResourceBundle().getString("addTimeDimRltDetails");
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            Object obj11[] = new Object[2];
//                            obj11[0] = seqaddTimeDimRlt;//PRG_GRP_DIM_REL
                            obj11[0] = memberIds[i];
                            obj11[1] = level[i];
                            finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj11[] = new Object[2];
//                            obj11[0] = seqaddTimeDimRlt;//PRG_GRP_DIM_REL
                                obj11[0] = memberIds[i];
                                obj11[1] = level[i];
                                finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj11[] = new Object[2];
//                            obj11[0] = seqaddTimeDimRlt;//PRG_GRP_DIM_REL
                                obj11[0] = memberIds[i];
                                obj11[1] = level[i];
                                finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                            } else {
                                Object obj11[] = new Object[3];
                                obj11[0] = seqaddTimeDimRlt;
                                obj11[1] = memberIds[i];
                                obj11[2] = level[i];
                                finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj11[] = new Object[2];
//                            obj11[0] = seqaddTimeDimRlt;//PRG_GRP_DIM_REL
                                obj11[0] = memberIds[i];
                                obj11[1] = level[i];
                                finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj11[] = new Object[2];
//                            obj11[0] = seqaddTimeDimRlt;//PRG_GRP_DIM_REL
                                obj11[0] = memberIds[i];
                                obj11[1] = level[i];
                                finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                            } else {
                                Object obj11[] = new Object[3];
                                obj11[0] = seqaddTimeDimRlt;
                                obj11[1] = memberIds[i];
                                obj11[2] = level[i];
                                finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                            }
                        }

                        ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                        queryList.add(finalQuery);

                    }
                    //add Time Dim info
                    String addTimeDimInfo = getResourceBundle().getString("addTimeDimInfo");
                    int seqaddTimeDimInfo = 0;
                    if (conntype.equalsIgnoreCase("SqlServer") || conntype.equalsIgnoreCase("Mysql")) {
//                           int seqaddTimeDimInfo = pbdb.getSequenceNumber("select PRG_TIME_DIM_INFO_SEQ.nextval from dual");
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        } else {
                            seqaddTimeDimInfo = pbdb.getSequenceNumber("select PRG_TIME_DIM_INFO_SEQ.nextval from dual");
                        }
                    } else {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        } else {
                            seqaddTimeDimInfo = pbdb.getSequenceNumber("select PRG_TIME_DIM_INFO_SEQ.nextval from dual");
                        }
                    }

                    if (minTimeLevel.equals("5")) {
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            Object obj12[] = new Object[6];
//                        obj12[0] = seqaddTimeDimInfo;
//                        obj12[0] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                            obj12[0] = bussTableId;
                            //obj12[3] = bussColId;request.getParameter("timeDimKeyValueDay")
                            obj12[1] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                            obj12[2] = minTimeLevel;
                            obj12[3] = "Y";
                            obj12[4] = "N";
                            obj12[5] = h.get("Day").toString();
                            finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            ////.println("finalQuery\t" + finalQuery);
                            queryList.add(finalQuery);//Moved from batchSt1 to batchSt
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj12[] = new Object[6];
//                        obj12[0] = seqaddTimeDimInfo;
//                        obj12[0] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj12[0] = bussTableId;
                                //obj12[3] = bussColId;request.getParameter("timeDimKeyValueDay")
                                obj12[1] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = h.get("Day").toString();
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList.add(finalQuery);//Moved from batchSt1 to batchSt
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj12[] = new Object[6];
//                        obj12[0] = seqaddTimeDimInfo;
//                        obj12[0] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj12[0] = bussTableId;
                                //obj12[3] = bussColId;request.getParameter("timeDimKeyValueDay")
                                obj12[1] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = h.get("Day").toString();
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList.add(finalQuery);//Moved from batchSt1 to batchSt
                            } else {
                                Object obj12[] = new Object[8];
                                obj12[0] = seqaddTimeDimInfo;
                                obj12[1] = seqaddTimeDimBussMater;
                                obj12[2] = bussTableId;
                                //obj12[3] = bussColId;request.getParameter("timeDimKeyValueDay")
                                obj12[3] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                                obj12[4] = minTimeLevel;
                                obj12[5] = "Y";
                                obj12[6] = "N";
                                obj12[7] = h.get("Day").toString();
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                queryList1.add(finalQuery);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj12[] = new Object[6];
//                        obj12[0] = seqaddTimeDimInfo;
//                        obj12[0] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj12[0] = bussTableId;
                                //obj12[3] = bussColId;request.getParameter("timeDimKeyValueDay")
                                obj12[1] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = h.get("Day").toString();
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList.add(finalQuery);//Moved from batchSt1 to batchSt
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj12[] = new Object[6];
//                        obj12[0] = seqaddTimeDimInfo;
//                        obj12[0] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj12[0] = bussTableId;
                                //obj12[3] = bussColId;request.getParameter("timeDimKeyValueDay")
                                obj12[1] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = h.get("Day").toString();
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList.add(finalQuery);//Moved from batchSt1 to batchSt
                            } else {
                                Object obj12[] = new Object[8];
                                obj12[0] = seqaddTimeDimInfo;
                                obj12[1] = seqaddTimeDimBussMater;
                                obj12[2] = bussTableId;
                                //obj12[3] = bussColId;request.getParameter("timeDimKeyValueDay")
                                obj12[3] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                                obj12[4] = minTimeLevel;
                                obj12[5] = "Y";
                                obj12[6] = "N";
                                obj12[7] = h.get("Day").toString();
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                queryList1.add(finalQuery);
                            }
                        }

                    } else if (minTimeLevel.equals("3")) {
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                            obj12[0] = bussTableId;
                            if (request.getParameter("monthJoinType").equals("F")) {
                                obj12[1] = request.getParameter("timeDimKeyValuemonth").split("~")[0];
                            } else if (request.getParameter("monthJoinType").equals("M")) {
                                obj12[1] = bussColId;
                            } else {
                                obj12[1] = request.getParameter("timeDimKeyValuesingleMonthno").split("~")[0];
                            }

                            obj12[2] = minTimeLevel;
                            obj12[3] = "Y";
                            obj12[4] = "N";
                            obj12[5] = rltmonthstrId;
                            finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            ////.println("finalQuery\t" + finalQuery);
                            queryList1.add(finalQuery);
                        } else {
                            Object obj12[] = new Object[8];
                            obj12[0] = seqaddTimeDimInfo;
                            obj12[1] = seqaddTimeDimBussMater;
                            obj12[2] = bussTableId;
                            if (request.getParameter("monthJoinType").equals("F")) {
                                obj12[3] = request.getParameter("timeDimKeyValuemonth").split("~")[0];
                            } else if (request.getParameter("monthJoinType").equals("M")) {
                                obj12[3] = bussColId;
                            } else {
                                obj12[3] = request.getParameter("timeDimKeyValuesingleMonthno").split("~")[0];
                            }

                            obj12[4] = minTimeLevel;
                            obj12[5] = "Y";
                            obj12[6] = "N";
                            obj12[7] = rltmonthstrId;
                            finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            queryList1.add(finalQuery);
                        }

                    } else if (minTimeLevel.equals("4")) {
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                            obj12[0] = bussTableId;
                            if (request.getParameter("weekJoinType").equals("S")) {
                                obj12[1] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                            } else {
                                obj12[1] = bussColId;
                            }
                            obj12[2] = minTimeLevel;
                            obj12[3] = "Y";
                            obj12[4] = "N";
                            obj12[5] = "0";
                            finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            ////.println("finalQuery\t" + finalQuery);
                            queryList1.add(finalQuery);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                            obj12[0] = bussTableId;
                            if (request.getParameter("weekJoinType").equals("S")) {
                                obj12[1] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                            } else {
                                obj12[1] = bussColId;
                            }
                            obj12[2] = minTimeLevel;
                            obj12[3] = "Y";
                            obj12[4] = "N";
                            obj12[5] = "0";
                            finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            ////.println("finalQuery\t" + finalQuery);
                            queryList1.add(finalQuery);
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                                obj12[0] = bussTableId;
                                if (request.getParameter("weekJoinType").equals("S")) {
                                    obj12[1] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                                } else {
                                    obj12[1] = bussColId;
                                }
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                                obj12[0] = bussTableId;
                                if (request.getParameter("weekJoinType").equals("S")) {
                                    obj12[1] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                                } else {
                                    obj12[1] = bussColId;
                                }
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList1.add(finalQuery);
                            } else {
                                Object obj12[] = new Object[8];
                                obj12[0] = seqaddTimeDimInfo;
                                obj12[1] = seqaddTimeDimBussMater;
                                obj12[2] = bussTableId;
                                if (request.getParameter("weekJoinType").equals("S")) {
                                    obj12[3] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                                } else {
                                    obj12[3] = bussColId;
                                }
                                obj12[4] = minTimeLevel;
                                obj12[5] = "Y";
                                obj12[6] = "N";
                                obj12[7] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList1.add(finalQuery);
                            }
                        }

                    } else if (minTimeLevel.equals("2")) {
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                            obj12[0] = bussTableId;
                            if (request.getParameter("quarterJoinType").equals("S")) {
                                obj12[1] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                            } else {
                                obj12[1] = bussColId;
                            }
                            obj12[2] = minTimeLevel;
                            obj12[3] = "Y";
                            obj12[4] = "N";
                            obj12[5] = "0";
                            finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            ////.println("finalQuery");
                            queryList1.add(finalQuery);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                                obj12[0] = bussTableId;
                                if (request.getParameter("quarterJoinType").equals("S")) {
                                    obj12[1] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                                } else {
                                    obj12[1] = bussColId;
                                }
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery");
                                queryList1.add(finalQuery);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                                obj12[0] = bussTableId;
                                if (request.getParameter("quarterJoinType").equals("S")) {
                                    obj12[1] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                                } else {
                                    obj12[1] = bussColId;
                                }
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery");
                                queryList1.add(finalQuery);
                            } else {
                                Object obj12[] = new Object[8];
                                obj12[0] = seqaddTimeDimInfo;
                                obj12[1] = seqaddTimeDimBussMater;
                                obj12[2] = bussTableId;
                                if (request.getParameter("quarterJoinType").equals("S")) {
                                    obj12[3] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                                } else {
                                    obj12[3] = bussColId;
                                }
                                obj12[4] = minTimeLevel;
                                obj12[5] = "Y";
                                obj12[6] = "N";
                                obj12[7] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList1.add(finalQuery);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                                obj12[0] = bussTableId;
                                if (request.getParameter("quarterJoinType").equals("S")) {
                                    obj12[1] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                                } else {
                                    obj12[1] = bussColId;
                                }
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery");
                                queryList1.add(finalQuery);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj12[] = new Object[6];
//                            obj12[0] = seqaddTimeDimInfo;
//                            obj12[1] = seqaddTimeDimBussMater;
                                obj12[0] = bussTableId;
                                if (request.getParameter("quarterJoinType").equals("S")) {
                                    obj12[1] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                                } else {
                                    obj12[1] = bussColId;
                                }
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery");
                                queryList1.add(finalQuery);
                            } else {
                                Object obj12[] = new Object[8];
                                obj12[0] = seqaddTimeDimInfo;
                                obj12[1] = seqaddTimeDimBussMater;
                                obj12[2] = bussTableId;
                                if (request.getParameter("quarterJoinType").equals("S")) {
                                    obj12[3] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                                } else {
                                    obj12[3] = bussColId;
                                }
                                obj12[4] = minTimeLevel;
                                obj12[5] = "Y";
                                obj12[6] = "N";
                                obj12[7] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList1.add(finalQuery);
                            }
                        }

                    } else if (minTimeLevel.equals("1")) {
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            Object obj12[] = new Object[6];
//                        ifobj12[0] = seqaddTimeDimInfo;
//                        obj12[1] = seqaddTimeDimBussMater;
                            obj12[0] = bussTableId;
                            obj12[1] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                            obj12[2] = minTimeLevel;
                            obj12[3] = "Y";
                            obj12[4] = "N";
                            obj12[5] = "0";
                            finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            ////.println("finalQuery\t" + finalQuery);
                            queryList.add(finalQuery);//Moved from batchSt1 to batchSt
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            Object obj12[] = new Object[6];
//                        ifobj12[0] = seqaddTimeDimInfo;
//                        obj12[1] = seqaddTimeDimBussMater;
                            obj12[0] = bussTableId;
                            obj12[1] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                            obj12[2] = minTimeLevel;
                            obj12[3] = "Y";
                            obj12[4] = "N";
                            obj12[5] = "0";
                            finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            ////.println("finalQuery\t" + finalQuery);
                            queryList.add(finalQuery);//Moved from batchSt1 to batchSt
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj12[] = new Object[6];
//                        ifobj12[0] = seqaddTimeDimInfo;
//                        obj12[1] = seqaddTimeDimBussMater;
                                obj12[0] = bussTableId;
                                obj12[1] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                ////.println("finalQuery\t" + finalQuery);
                                queryList.add(finalQuery);//Moved from batchSt1 to batchSt
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj12[] = new Object[6];
//                        ifobj12[0] = seqaddTimeDimInfo;
//                        obj12[1] = seqaddTimeDimBussMater;
                                obj12[0] = bussTableId;
                                obj12[1] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                                obj12[2] = minTimeLevel;
                                obj12[3] = "Y";
                                obj12[4] = "N";
                                obj12[5] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                            } else {
                                Object obj12[] = new Object[8];
                                obj12[0] = seqaddTimeDimInfo;
                                obj12[1] = seqaddTimeDimBussMater;
                                obj12[2] = bussTableId;
                                obj12[3] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                                obj12[4] = minTimeLevel;
                                obj12[5] = "Y";
                                obj12[6] = "N";
                                obj12[7] = "0";
                                finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                                queryList1.add(finalQuery);
                            }
                        }

                    }
                    // //////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);

                    //add to rlt master
                    String addTimeDimBussRltDetails = getResourceBundle().getString("addTimeDimBussRltDetails");
                    if (minTimeLevel.equals("5")) {
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            Object obj12[] = new Object[6];
                            String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");

//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                            Object obj13[] = new Object[8];
//                            obj13[0] = seqaddTimeDimInfo;//PRG_TIME_DIM_INFO
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                            obj13[0] = bussTableId;
                            obj13[1] = timeDimKeyValueDay.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                            obj13[2] = rltdateId;//PRG_GRP_BUSS_TABLE_DETAILS
                            obj13[3] = "inner";
                            obj13[4] = "=";
                            obj13[5] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                            obj13[6] = "DDATE";
                            obj13[7] = "null";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                            ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                            queryList.add(finalQuery);

                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj12[] = new Object[6];
                                String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");

//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                                Object obj13[] = new Object[8];
//                            obj13[0] = seqaddTimeDimInfo;//PRG_TIME_DIM_INFO
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueDay.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj13[2] = rltdateId;//PRG_GRP_BUSS_TABLE_DETAILS
                                obj13[3] = "inner";
                                obj13[4] = "=";
                                obj13[5] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                                obj13[6] = "DDATE";
                                obj13[7] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList.add(finalQuery);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj12[] = new Object[6];
                                String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");

//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                                Object obj13[] = new Object[8];
//                            obj13[0] = seqaddTimeDimInfo;//PRG_TIME_DIM_INFO
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueDay.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj13[2] = rltdateId;//PRG_GRP_BUSS_TABLE_DETAILS
                                obj13[3] = "inner";
                                obj13[4] = "=";
                                obj13[5] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                                obj13[6] = "DDATE";
                                obj13[7] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList.add(finalQuery);
                            } else {
                                Object obj12[] = new Object[6];
                                String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");

                                int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                                Object obj13[] = new Object[11];
                                obj13[0] = seqaddTimeDimInfo;
                                obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[2] = bussTableId;
                                obj13[3] = timeDimKeyValueDay.split("~")[0];
                                obj13[4] = seqaddTimeDimBussMater;
                                obj13[5] = rltdateId;
                                obj13[6] = "inner";
                                obj13[7] = "=";
                                obj13[8] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                                obj13[9] = "DDATE";
                                obj13[10] = "";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            }

                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj12[] = new Object[6];
                                String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");

//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                                Object obj13[] = new Object[8];
//                            obj13[0] = seqaddTimeDimInfo;//PRG_TIME_DIM_INFO
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueDay.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj13[2] = rltdateId;//PRG_GRP_BUSS_TABLE_DETAILS
                                obj13[3] = "inner";
                                obj13[4] = "=";
                                obj13[5] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                                obj13[6] = "DDATE";
                                obj13[7] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList.add(finalQuery);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj12[] = new Object[6];
                                String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");

//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                                Object obj13[] = new Object[8];
//                            obj13[0] = seqaddTimeDimInfo;//PRG_TIME_DIM_INFO
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueDay.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;//PRG_GRP_BUSS_TABLE
                                obj13[2] = rltdateId;//PRG_GRP_BUSS_TABLE_DETAILS
                                obj13[3] = "inner";
                                obj13[4] = "=";
                                obj13[5] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                                obj13[6] = "DDATE";
                                obj13[7] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList.add(finalQuery);
                            } else {
                                Object obj12[] = new Object[6];
                                String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");

                                int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                                Object obj13[] = new Object[11];
                                obj13[0] = seqaddTimeDimInfo;
                                obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[2] = bussTableId;
                                obj13[3] = timeDimKeyValueDay.split("~")[0];
                                obj13[4] = seqaddTimeDimBussMater;
                                obj13[5] = rltdateId;
                                obj13[6] = "inner";
                                obj13[7] = "=";
                                obj13[8] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                                obj13[9] = "DDATE";
                                obj13[10] = "";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            }

                        }



                    } else if (minTimeLevel.equals("4")) {


                        if (request.getParameter("weekJoinType").equals("S")) {

                            String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValuesingleWeekno");
                            if (conntype.equalsIgnoreCase("SqlServer")) {
//                                int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltweeknoId;
                                obj13[2] = "inner";
                                obj13[3] = "=";
                                obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                obj13[5] = "CWEEK";
                                obj13[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);

                            } else if (conntype.equalsIgnoreCase("Mysql")) {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltweeknoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[5] = "CWEEK";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltweeknoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[5] = "CWEEK";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltweeknoId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[9] = "CWEEK";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }

                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltweeknoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[5] = "CWEEK";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltweeknoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[5] = "CWEEK";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltweeknoId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[9] = "CWEEK";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            }
                        } else {
                            String timeDimKeyValueWeekyr = "";
                            if (conntype.equalsIgnoreCase("SqlServer")) {
                                String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                                timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
//                            obj13[5] = rltweeknoId;
                                obj13[2] = "inner";
                                obj13[3] = "=";
                                obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                obj13[5] = "CWEEK";
                                obj13[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (conntype.equalsIgnoreCase("Mysql")) {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                                    timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
//                            obj13[5] = rltweeknoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[5] = "CWEEK";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                                    timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
//                            obj13[5] = rltweeknoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[5] = "CWEEK";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                                    timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
                                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltweeknoId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[9] = "CWEEK";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                                    timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
//                            obj13[5] = rltweeknoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[5] = "CWEEK";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                                    timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
//                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueWeekno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
//                            obj13[5] = rltweeknoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[5] = "CWEEK";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                                    timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
                                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltweeknoId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                                    obj13[9] = "CWEEK";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }

                            }



                            if (conntype.equalsIgnoreCase("SqlServer")) {
//                                int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[0] = bussTableId;
                                obj14[1] = timeDimKeyValueWeekyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltweekyearId;
                                obj14[2] = "inner";
                                obj14[3] = "=";
                                obj14[4] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                                obj14[5] = "CW_YEAR";
                                obj14[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);

                            } else if (conntype.equalsIgnoreCase("Mysql")) {
//                                int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueWeekyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltweekyearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                                    obj14[5] = "CW_YEAR";
                                    obj14[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueWeekyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltweekyearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                                    obj14[5] = "CW_YEAR";
                                    obj14[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj14[] = new Object[11];
                                    obj14[0] = seqaddTimeDimInfo;
                                    obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[2] = bussTableId;
                                    obj14[3] = timeDimKeyValueWeekyr.split("~")[0];
                                    obj14[4] = seqaddTimeDimBussMater;
                                    obj14[5] = rltweekyearId;
                                    obj14[6] = "inner";
                                    obj14[7] = "=";
                                    obj14[8] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                                    obj14[9] = "CW_YEAR";
                                    obj14[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueWeekyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltweekyearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                                    obj14[5] = "CW_YEAR";
                                    obj14[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueWeekyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltweekyearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                                    obj14[5] = "CW_YEAR";
                                    obj14[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj14[] = new Object[11];
                                    obj14[0] = seqaddTimeDimInfo;
                                    obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[2] = bussTableId;
                                    obj14[3] = timeDimKeyValueWeekyr.split("~")[0];
                                    obj14[4] = seqaddTimeDimBussMater;
                                    obj14[5] = rltweekyearId;
                                    obj14[6] = "inner";
                                    obj14[7] = "=";
                                    obj14[8] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                                    obj14[9] = "CW_YEAR";
                                    obj14[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }

                            }


                        }
                    } else if (minTimeLevel.equals("3")) {

                        String monthFormatyn = request.getParameter("monthJoinType");

                        if (monthFormatyn.equals("F")) {
                            String monthPre = request.getParameter("monthPre");
                            String monthFormat = request.getParameter("monthFormat");
                            String monthPost = request.getParameter("monthPost");
                            //   ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("month pre---------->"+monthPre);
                            //    ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("month post---------->"+monthPost);
                            String totalMonthFormat = "";
                            if (monthPre != null && monthPost != null) {
                                totalMonthFormat = monthPre + "&" + monthFormat + "~" + monthPost;
                            } else if (monthPre != null) {
                                totalMonthFormat = monthPre + "&" + monthFormat;
                            } else if (monthPost != null) {
                                totalMonthFormat = monthFormat + "~" + monthPost;
                            }

                            String timeDimKeyValuemonth = request.getParameter("timeDimKeyValuemonth");
                            if (conntype.equalsIgnoreCase("SqlServer")) {
//                                int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValuemonth.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltmonthstrId;
                                obj13[2] = "inner";
                                obj13[3] = "=";
                                obj13[4] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                                obj13[5] = "CM_ST_DATE";
                                obj13[6] = totalMonthFormat;
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (conntype.equalsIgnoreCase("Mysql")) {
//                                int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValuemonth.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltmonthstrId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                                    obj13[5] = "CM_ST_DATE";
                                    obj13[6] = totalMonthFormat;
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValuemonth.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltmonthstrId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                                    obj13[5] = "CM_ST_DATE";
                                    obj13[6] = totalMonthFormat;
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValuemonth.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltmonthstrId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                                    obj13[9] = "CM_ST_DATE";
                                    obj13[10] = totalMonthFormat;
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValuemonth.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltmonthstrId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                                    obj13[5] = "CM_ST_DATE";
                                    obj13[6] = totalMonthFormat;
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValuemonth.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltmonthstrId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                                    obj13[5] = "CM_ST_DATE";
                                    obj13[6] = totalMonthFormat;
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValuemonth.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltmonthstrId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                                    obj13[9] = "CM_ST_DATE";
                                    obj13[10] = totalMonthFormat;
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            }



                        } else if (monthFormatyn.equals("M")) {
                            String timeDimKeyValueMonthyr = request.getParameter("timeDimKeyValueMonthyr");
                            String timeDimKeyValueMonthno = request.getParameter("timeDimKeyValueMonthno");
                            if (conntype.equalsIgnoreCase("SqlServer")) {
//                                int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
                                //obj13[5] = rltmonthnoId;
//                                obj13[5] = h.get("Month").toString();
                                obj13[2] = "inner";
                                obj13[3] = "=";
                                //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
                                //obj13[9]="CMONTH";h.get("Month").toString();
                                obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                obj13[5] = "CM_CUST_NAME";
                                obj13[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (conntype.equalsIgnoreCase("Mysql")) {
//                                int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
                                    //obj13[5] = rltmonthnoId;
//                                obj13[5] = h.get("Month").toString();
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
                                    //obj13[9]="CMONTH";h.get("Month").toString();
                                    obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    obj13[5] = "CM_CUST_NAME";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
                                    //obj13[5] = rltmonthnoId;
//                                obj13[5] = h.get("Month").toString();
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
                                    //obj13[9]="CMONTH";h.get("Month").toString();
                                    obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    obj13[5] = "CM_CUST_NAME";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueMonthno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    //obj13[5] = rltmonthnoId;
                                    obj13[5] = h.get("Month").toString();
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
                                    //obj13[9]="CMONTH";h.get("Month").toString();
                                    obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    obj13[9] = "CM_CUST_NAME";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
                                    //obj13[5] = rltmonthnoId;
//                                obj13[5] = h.get("Month").toString();
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
                                    //obj13[9]="CMONTH";h.get("Month").toString();
                                    obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    obj13[5] = "CM_CUST_NAME";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
                                    //obj13[5] = rltmonthnoId;
//                                obj13[5] = h.get("Month").toString();
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
                                    //obj13[9]="CMONTH";h.get("Month").toString();
                                    obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    obj13[5] = "CM_CUST_NAME";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueMonthno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    //obj13[5] = rltmonthnoId;
                                    obj13[5] = h.get("Month").toString();
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
                                    //obj13[9]="CMONTH";h.get("Month").toString();
                                    obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    obj13[9] = "CM_CUST_NAME";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            }




                            int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            if (conntype.equalsIgnoreCase("SqlServer")) {
                                Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[0] = bussTableId;
                                obj14[1] = timeDimKeyValueMonthyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltmonthyearId;
                                obj14[2] = "inner";
                                obj14[3] = "=";
                                obj14[4] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                                obj14[5] = "CM_YEAR";
                                obj14[6] = "";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (conntype.equalsIgnoreCase("Mysql")) {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueMonthyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltmonthyearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                                    obj14[5] = "CM_YEAR";
                                    obj14[6] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueMonthyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltmonthyearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                                    obj14[5] = "CM_YEAR";
                                    obj14[6] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    Object obj14[] = new Object[11];
                                    obj14[0] = seqaddTimeDimInfo;
                                    obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[2] = bussTableId;
                                    obj14[3] = timeDimKeyValueMonthyr.split("~")[0];
                                    obj14[4] = seqaddTimeDimBussMater;
                                    obj14[5] = rltmonthyearId;
                                    obj14[6] = "inner";
                                    obj14[7] = "=";
                                    obj14[8] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                                    obj14[9] = "CM_YEAR";
                                    obj14[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueMonthyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltmonthyearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                                    obj14[5] = "CM_YEAR";
                                    obj14[6] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj14[] = new Object[7];
//                                obj14[0] = seqaddTimeDimInfo;
//                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueMonthyr.split("~")[0];
//                                obj14[4] = seqaddTimeDimBussMater;
//                                obj14[5] = rltmonthyearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                                    obj14[5] = "CM_YEAR";
                                    obj14[6] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    Object obj14[] = new Object[11];
                                    obj14[0] = seqaddTimeDimInfo;
                                    obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[2] = bussTableId;
                                    obj14[3] = timeDimKeyValueMonthyr.split("~")[0];
                                    obj14[4] = seqaddTimeDimBussMater;
                                    obj14[5] = rltmonthyearId;
                                    obj14[6] = "inner";
                                    obj14[7] = "=";
                                    obj14[8] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                                    obj14[9] = "CM_YEAR";
                                    obj14[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            }


                        } else {

                            String timeDimKeyValueMonthno = request.getParameter("timeDimKeyValuesingleMonthno");
                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            if (conntype.equalsIgnoreCase("SqlServer")) {
                                Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
                                // obj13[5] = rltmonthnoId;
//                            obj13[5] = h.get("Month").toString();
                                obj13[2] = "inner";
                                obj13[3] = "=";
                                obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                //obj13[9]="CMONTH";
                                obj13[5] = "CM_CUST_NAME";
                                obj13[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (conntype.equalsIgnoreCase("Mysql")) {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
                                    // obj13[5] = rltmonthnoId;
//                            obj13[5] = h.get("Month").toString();
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    //obj13[9]="CMONTH";
                                    obj13[5] = "CM_CUST_NAME";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
                                    // obj13[5] = rltmonthnoId;
//                            obj13[5] = h.get("Month").toString();
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    //obj13[9]="CMONTH";
                                    obj13[5] = "CM_CUST_NAME";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueMonthno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    // obj13[5] = rltmonthnoId;
                                    obj13[5] = h.get("Month").toString();
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    //obj13[9]="CMONTH";
                                    obj13[9] = "CM_CUST_NAME";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
                                    // obj13[5] = rltmonthnoId;
//                            obj13[5] = h.get("Month").toString();
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    //obj13[9]="CMONTH";
                                    obj13[5] = "CM_CUST_NAME";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                            obj13[0] = seqaddTimeDimInfo;
//                            obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueMonthno.split("~")[0];
//                            obj13[4] = seqaddTimeDimBussMater;
                                    // obj13[5] = rltmonthnoId;
//                            obj13[5] = h.get("Month").toString();
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    //obj13[9]="CMONTH";
                                    obj13[5] = "CM_CUST_NAME";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueMonthno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    // obj13[5] = rltmonthnoId;
                                    obj13[5] = h.get("Month").toString();
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                                    //obj13[9]="CMONTH";
                                    obj13[9] = "CM_CUST_NAME";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            }



                        }
                    } else if (minTimeLevel.equals("2")) {
                        //for mintime level Quarter
                        if (request.getParameter("quarterJoinType").equals("S")) {
                            String timeDimKeyValueQuaterno = request.getParameter("timeDimKeyValuesingleQuaterno");
                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            if (conntype.equalsIgnoreCase("SqlServer")) {
                                Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueQuaterno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltqtrnoId;
                                obj13[2] = "inner";
                                obj13[3] = "=";
                                obj13[4] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                obj13[5] = "CQTR";
                                obj13[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (conntype.equalsIgnoreCase("Mysql")) {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueQuaterno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltqtrnoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[5] = "CQTR";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                } else {
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueQuaterno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltqtrnoId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[9] = "CQTR";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueQuaterno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltqtrnoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[5] = "CQTR";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                } else {
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueQuaterno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltqtrnoId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[9] = "CQTR";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }

                            }





                        } else {
                            String timeDimKeyValueQuaterno = request.getParameter("timeDimKeyValueQuaterno");

                            String timeDimKeyValueQuateryr = request.getParameter("timeDimKeyValueQuateryr");


                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            if (conntype.equalsIgnoreCase("SqlServer")) {
                                Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                obj13[0] = bussTableId;
                                obj13[1] = timeDimKeyValueQuaterno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltqtrnoId;
                                obj13[2] = "inner";
                                obj13[3] = "=";
                                obj13[4] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                obj13[5] = "CQTR";
                                obj13[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);

                            } else if (conntype.equalsIgnoreCase("Mysql")) {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueQuaterno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltqtrnoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[5] = "CQTR";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueQuaterno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltqtrnoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[5] = "CQTR";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueQuaterno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltqtrnoId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[9] = "CQTR";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }

                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueQuaterno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltqtrnoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[5] = "CQTR";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj13[] = new Object[7];
//                                obj13[0] = seqaddTimeDimInfo;
//                                obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[0] = bussTableId;
                                    obj13[1] = timeDimKeyValueQuaterno.split("~")[0];
//                                obj13[4] = seqaddTimeDimBussMater;
//                                obj13[5] = rltqtrnoId;
                                    obj13[2] = "inner";
                                    obj13[3] = "=";
                                    obj13[4] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[5] = "CQTR";
                                    obj13[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    Object obj13[] = new Object[11];
                                    obj13[0] = seqaddTimeDimInfo;
                                    obj13[1] = seqaddTimeDimBussRltDetails;
                                    obj13[2] = bussTableId;
                                    obj13[3] = timeDimKeyValueQuaterno.split("~")[0];
                                    obj13[4] = seqaddTimeDimBussMater;
                                    obj13[5] = rltqtrnoId;
                                    obj13[6] = "inner";
                                    obj13[7] = "=";
                                    obj13[8] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                                    obj13[9] = "CQTR";
                                    obj13[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }

                            }




                            int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            if (conntype.equalsIgnoreCase("SqlServer")) {
                                Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[0] = bussTableId;
                                obj14[1] = timeDimKeyValueQuateryr.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltqtryearId;
                                obj14[2] = "inner";
                                obj14[3] = "=";
                                obj14[4] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                                obj14[5] = "CQ_YEAR";
                                obj14[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (conntype.equalsIgnoreCase("Mysql")) {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueQuateryr.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltqtryearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                                    obj14[5] = "CQ_YEAR";
                                    obj14[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueQuateryr.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltqtryearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                                    obj14[5] = "CQ_YEAR";
                                    obj14[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    Object obj14[] = new Object[11];
                                    obj14[0] = seqaddTimeDimInfo;
                                    obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[2] = bussTableId;
                                    obj14[3] = timeDimKeyValueQuateryr.split("~")[0];
                                    obj14[4] = seqaddTimeDimBussMater;
                                    obj14[5] = rltqtryearId;
                                    obj14[6] = "inner";
                                    obj14[7] = "=";
                                    obj14[8] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                                    obj14[9] = "CQ_YEAR";
                                    obj14[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            } else {
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                    Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueQuateryr.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltqtryearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                                    obj14[5] = "CQ_YEAR";
                                    obj14[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[0] = bussTableId;
                                    obj14[1] = timeDimKeyValueQuateryr.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltqtryearId;
                                    obj14[2] = "inner";
                                    obj14[3] = "=";
                                    obj14[4] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                                    obj14[5] = "CQ_YEAR";
                                    obj14[6] = "null";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                } else {
                                    Object obj14[] = new Object[11];
                                    obj14[0] = seqaddTimeDimInfo;
                                    obj14[1] = seqaddTimeDimBussRltDetails1;
                                    obj14[2] = bussTableId;
                                    obj14[3] = timeDimKeyValueQuateryr.split("~")[0];
                                    obj14[4] = seqaddTimeDimBussMater;
                                    obj14[5] = rltqtryearId;
                                    obj14[6] = "inner";
                                    obj14[7] = "=";
                                    obj14[8] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                                    obj14[9] = "CQ_YEAR";
                                    obj14[10] = "";
                                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                                    ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                    queryList1.add(finalQuery);
                                }
                            }



                        }
                    } else if (minTimeLevel.equals("1")) {

                        String timeDimKeyValueYear = request.getParameter("timeDimKeyValueYear");
                        int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                        if (conntype.equalsIgnoreCase("SqlServer")) {
                            Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                            obj14[0] = bussTableId;
                            obj14[1] = timeDimKeyValueYear.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltyearId;
                            obj14[2] = "inner";
                            obj14[3] = "=";
                            obj14[4] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                            obj14[5] = "CYEAR";
                            obj14[6] = "null";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                            ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                            queryList1.add(finalQuery);
                        } else if (conntype.equalsIgnoreCase("Mysql")) {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[0] = bussTableId;
                                obj14[1] = timeDimKeyValueYear.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltyearId;
                                obj14[2] = "inner";
                                obj14[3] = "=";
                                obj14[4] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                                obj14[5] = "CYEAR";
                                obj14[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[0] = bussTableId;
                                obj14[1] = timeDimKeyValueYear.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltyearId;
                                obj14[2] = "inner";
                                obj14[3] = "=";
                                obj14[4] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                                obj14[5] = "CYEAR";
                                obj14[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else {
                                Object obj14[] = new Object[11];
                                obj14[0] = seqaddTimeDimInfo;
                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[2] = bussTableId;
                                obj14[3] = timeDimKeyValueYear.split("~")[0];
                                obj14[4] = seqaddTimeDimBussMater;
                                obj14[5] = rltyearId;
                                obj14[6] = "inner";
                                obj14[7] = "=";
                                obj14[8] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                                obj14[9] = "CYEAR";
                                obj14[10] = "";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            }
                        } else {
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[0] = bussTableId;
                                obj14[1] = timeDimKeyValueYear.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltyearId;
                                obj14[2] = "inner";
                                obj14[3] = "=";
                                obj14[4] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                                obj14[5] = "CYEAR";
                                obj14[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                Object obj14[] = new Object[7];
//                            obj14[0] = seqaddTimeDimInfo;
//                            obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[0] = bussTableId;
                                obj14[1] = timeDimKeyValueYear.split("~")[0];
//                            obj14[4] = seqaddTimeDimBussMater;
//                            obj14[5] = rltyearId;
                                obj14[2] = "inner";
                                obj14[3] = "=";
                                obj14[4] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                                obj14[5] = "CYEAR";
                                obj14[6] = "null";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            } else {
                                Object obj14[] = new Object[11];
                                obj14[0] = seqaddTimeDimInfo;
                                obj14[1] = seqaddTimeDimBussRltDetails1;
                                obj14[2] = bussTableId;
                                obj14[3] = timeDimKeyValueYear.split("~")[0];
                                obj14[4] = seqaddTimeDimBussMater;
                                obj14[5] = rltyearId;
                                obj14[6] = "inner";
                                obj14[7] = "=";
                                obj14[8] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                                obj14[9] = "CYEAR";
                                obj14[10] = "";
                                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                                ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                                queryList1.add(finalQuery);
                            }
                        }


                    }

                    // pbdb.executeMultiple(timeDimList);
                    //////.println("batchSt1"+batchSt1);
                    pbDb.executeMultiple(queryList1);
                    pbDb.executeMultiple(queryList);
                    ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------------length=="+c1.length);
                    ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------------length=="+c.length);

                }
            } else if (conntype.equalsIgnoreCase("PostgreSQL")) {
                ResultSet rs = null;
                ResultSetMetaData rsmetadata = null;
                // //////.println("calendertable" + calenderTable);
                calenderTable = calenderTable.toLowerCase();
                if (minTimeLevel.equals("5")) {
                    String qry = "select attname from pg_attribute where attrelid in (select relid from pg_stat_user_tables WHERE relname='" + calenderTable + "') AND attname not in ('tableoid','cmax','xmax','cmin','xmin','ctid');";
                    //////.println("qry is\n===" +qry);

                    denom = pbDb.execSelectSQL(qry, con);
                    qry = "select * from " + calenderTable + ";";
                    //////.println("second query is\t" + qry);
//                    rs = st.executeQuery(qry);
//                    rsmetadata = rs.getMetaData();
                    con = null;
                    con = b.getBussTableConnection(bussTableId);
                    PbReturnObject pbdenom = pbDb.execSelectSQL(qry, con);
                    con = null;
                    colTypes = new String[pbdenom.getRowCount()];
                    colLength = new String[pbdenom.getRowCount()];
                    String[] tempColumnTypes = pbdenom.getColumnTypes();
                    int[] tempPrecision = pbdenom.getColumnSizes();

                    for (int clcount = 0; clcount < pbdenom.getColumnCount(); clcount++) {
                        String odatatype = "varchar2";
                        String otype = tempColumnTypes[clcount];

                        int len = 255;
                        if (tempPrecision[clcount] > 4000) {
                            len = 4000;
                        }
                        if (otype.contains("int") || otype.contains("oid") || otype.contains("float") || otype.contains("serial") || otype.contains("real")) {
                            odatatype = " number ";
                        } else if (otype.contains("time")) {

                            odatatype = "date";

                        } else if (otype.contains("date")) {
                            odatatype = "date";
                        } else {
                            odatatype = "varchar2";
                        }
                        colTypes[clcount] = odatatype;
                        colLength[clcount] = String.valueOf(len);
                    }
                }
                String querystring = ",";
                colNames = new String[denom.getRowCount()];
                for (int i = 0; i < denom.getRowCount(); i++) {
                    querystring += denom.getFieldValueString(i, 0) + ",";
                    colNames[i] = denom.getFieldValueString(i, 0);
                }


                if (denom.getColumnCount() > 0) {
                    querystring = querystring.substring(1, querystring.length() - 1);
                    ////////.println("query string is\t" + querystring);
                }
                //////.println("pr denom-----------"+denom.getRowCount()+"user name---"+username);
//                st.close();
                con = null;
//                Connection connection= ProgenConnection.getInstance().getConnection();
//                Statement batchSt = ProgenConnection.getInstance().getConnection().createStatement();
                ArrayList postGresArrayList = new ArrayList();
                //////.println("denom==="+denom.getRowCount());
                if (denom.getRowCount() > 0) {
                    String addTimeDimBussMater = getResourceBundle().getString("addTimeDimBussMater");
                    int seqaddTimeDimBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                    Object obj[];
                    obj = new Object[7];
                    obj[0] = seqaddTimeDimBussMater;
                    obj[1] = "PROGEN_TIME";
                    obj[2] = "PROGEN_TIME";
                    obj[3] = "Query";
                    obj[4] = 1;
                    obj[5] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                    // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                    obj[6] = grpId;

                    finalQuery = pbdb.buildQuery(addTimeDimBussMater, obj);
                    ////.println("final addTimeDimBussMater ---->" + finalQuery);
                    postGresArrayList.add(finalQuery);
                    // timeDimList.add(finalQuery);

                    String addTimeDimSrc = getResourceBundle().getString("addTimeDimSrc");
                    int seqaddTimeDimSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                    Object obj1[];
                    obj1 = new Object[5];
                    obj1[0] = seqaddTimeDimSrc;
                    obj1[1] = seqaddTimeDimBussMater;
                    obj1[2] = "Query";
                    obj1[3] = ConnectionId;
                    obj1[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                    finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                    ////.println("final addTimeDimSrc ---->" + finalQuery);
                    // timeDimList.add(finalQuery);
                    postGresArrayList.add(finalQuery);


                    String srcdetnextvalarr[] = new String[denom.getRowCount()];
                    for (int i = 0; i < denom.getRowCount(); i++) {
                        PbReturnObject srcseqval = pbdb.execSelectSQL("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                        int srcdetnextval = srcseqval.getFieldValueInt(0, "NEXTVAL");
                        srcdetnextvalarr[i] = String.valueOf(srcdetnextval);
                        String addTimeDimDetails = getResourceBundle().getString("addTimeDimSrcDetails");
                        Object obj2[] = new Object[5];
                        obj2[0] = srcdetnextval;
                        obj2[1] = seqaddTimeDimSrc;
                        obj2[2] = seqaddTimeDimBussMater;
                        obj2[3] = colNames[i];
                        obj2[4] = colTypes[i];
                        finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                        ////.println("final addTimeDimDetails ---->" + finalQuery);
                        postGresArrayList.add(finalQuery);

                    }
                    String seqaddTimeDimBussDetailsarr[] = new String[denom.getRowCount()];
                    //String memBussNames[]=new String[5];
                    //  String memBussIds[]=new String[5];
                    HashMap h = new HashMap();
                    String rltdateId = "";
                    String rltweeknoId = "";
                    String rltweekyearId = "";
                    String rltmonthnoId = "";
                    String rltmonthyearId = "";
                    String rltmonthstrId = "";
                    String rltmonthendId = "";
                    String rltqtrnoId = "";
                    String rltqtryearId = "";
                    String rltyearId = "";
                    //////.println("columncount\t " + denom.getColumnCount());
                    //////.println("rowcount\t " + denom.getRowCount());
                    for (int i = 0; i < denom.getRowCount(); i++) {
                        String addTimeDimBussDetails = getResourceBundle().getString("addTimeDimBussDetails");
                        int seqaddTimeDimBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                        seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                        //// ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---------------------"+i+"----------------------");
                        if (colNames[i].equalsIgnoreCase("DDATE")) {

                            h.put("Day", String.valueOf(seqaddTimeDimBussDetails));
                            //// ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------Day------"+h.get("Day"));
                        } else if (colNames[i].equalsIgnoreCase("CW_CUST_NAME")) {

                            h.put("Week", String.valueOf(seqaddTimeDimBussDetails));
                            // // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------week------"+h.get("Week"));
                        } else if (colNames[i].equalsIgnoreCase("CM_CUST_NAME")) {

                            h.put("Month", String.valueOf(seqaddTimeDimBussDetails));
                            //// ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------Month------"+h.get("Month"));
                        } else if (colNames[i].equalsIgnoreCase("CQ_CUST_NAME")) {

                            h.put("Quarter", String.valueOf(seqaddTimeDimBussDetails));
                            // // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------Quarter------"+h.get("Quarter"));

                        } else if (colNames[i].equalsIgnoreCase("CY_CUST_NAME")) {

                            h.put("Year", String.valueOf(seqaddTimeDimBussDetails));
                            //// ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------Year------"+h.get("Year"));
                        }
                        if (colNames[i].equalsIgnoreCase("DDATE")) {
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------------");
                            rltdateId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltdateId-------"+rltdateId);
                        } else if (colNames[i].equalsIgnoreCase("CWEEK")) {
                            rltweeknoId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltweeknoId-------"+rltweeknoId);
                        } else if (colNames[i].equalsIgnoreCase("CWYEAR")) {
                            rltweekyearId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltweekyearId-------"+rltweekyearId);
                        } else if (colNames[i].equalsIgnoreCase("CMONTH")) {
                            rltmonthnoId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthnoId-------"+rltmonthnoId);
                        } else if (colNames[i].equalsIgnoreCase("CM_YEAR")) {
                            rltmonthyearId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthyearId-------"+rltmonthyearId);
                        } else if (colNames[i].equalsIgnoreCase("CM_ST_DATE")) {
                            rltmonthstrId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthstrId-------"+rltmonthstrId);
                        } else if (colNames[i].equalsIgnoreCase("CM_END_DATE")) {
                            rltmonthendId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthendId-------"+rltmonthendId);
                        } else if (colNames[i].equalsIgnoreCase("CQTR")) {
                            rltqtrnoId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltqtrnoId-------"+rltqtrnoId);
                        } else if (colNames[i].equalsIgnoreCase("CQ_YEAR")) {
                            rltqtryearId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltqtryearId-------"+rltqtryearId);
                        } else if (colNames[i].equalsIgnoreCase("CYEAR")) {
                            rltyearId = String.valueOf(seqaddTimeDimBussDetails);
                            // ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltyearId-------"+rltyearId);
                        }


                        Object obj3[] = new Object[6];
                        obj3[0] = seqaddTimeDimBussDetails;
                        obj3[1] = seqaddTimeDimBussMater;
                        obj3[2] = colNames[i];
                        obj3[3] = srcdetnextvalarr[i];
                        obj3[4] = colTypes[i];
                        obj3[5] = colNames[i];
                        finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                        ////.println("final addTimeDim bussDetails ---->" + finalQuery);
                        postGresArrayList.add(finalQuery);

                    }
                    //add grp rlt master
                    //add grp rlt details

//                    Statement batchSt1 = ProgenConnection.getInstance().getConnection().createStatement();

                    //add dimensions
                    ArrayList arrayListofQuerys = new ArrayList();
                    String addTimeDimDimension = getResourceBundle().getString("addTimeDimDimension");
                    int seqaddTimeDimDimension = pbdb.getSequenceNumber("select  PRG_GRP_DIMENSIONS_SEQ.nextval from dual");
                    Object obj4[] = new Object[5];
                    obj4[0] = seqaddTimeDimDimension;
                    obj4[1] = "Time";
                    obj4[2] = "Time";
                    obj4[3] = "Y";
                    obj4[4] = grpId;
                    finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                    ////.println("final addTimeDimDimension ---->" + finalQuery);
                    arrayListofQuerys.add(finalQuery);

                    //add grp dimension table
                    String addTimeDimTables = getResourceBundle().getString("addTimeDimTables");
                    int seqaddTimeDimTables = pbdb.getSequenceNumber("select  PRG_GRP_DIM_TABLES_SEQ.nextval from dual");
                    Object obj5[] = new Object[3];
                    obj5[0] = seqaddTimeDimTables;
                    obj5[1] = seqaddTimeDimDimension;
                    obj5[2] = seqaddTimeDimBussMater;
                    finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);

                    ////.println("final addTimeDimTables ---->" + finalQuery);
                    arrayListofQuerys.add(finalQuery);

                    //add grp dimension table details
                    for (int i = 0; i < denom.getRowCount(); i++) {
                        String addTimeDimTableDetails = getResourceBundle().getString("addTimeDimTableDetails");
                        int seqaddTimeDimTableDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval from dual");
                        Object obj6[] = new Object[5];
                        obj6[0] = seqaddTimeDimTableDetails;
                        obj6[1] = seqaddTimeDimTables;
                        obj6[2] = seqaddTimeDimBussDetailsarr[i];
                        obj6[3] = "Y";
                        obj6[4] = "N";
                        finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);

                        ////.println("final addTimeDimTables details ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);
                    }
                    //add grp dimension members
                    String level[] = new String[Integer.parseInt(minTimeLevel)];
                    String memberIds[] = new String[Integer.parseInt(minTimeLevel)];

                    for (int i = 0; i < Integer.parseInt(minTimeLevel); i++) {

                        String addTimeDimMembers = getResourceBundle().getString("addTimeDimMembers");
                        int seqaddTimeDimMembers = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_SEQ.nextval from dual");
                        memberIds[i] = String.valueOf(seqaddTimeDimMembers);
                        level[i] = String.valueOf(i + 1);
                        Object obj7[] = new Object[8];
                        obj7[0] = seqaddTimeDimMembers;
                        obj7[1] = TimeDimMemName[i];
                        obj7[2] = seqaddTimeDimDimension;
                        obj7[3] = seqaddTimeDimTables;
                        obj7[4] = "Y";
                        obj7[5] = seqaddTimeDimBussMater;
                        obj7[6] = TimeDimMemDenomQuery[i];
                        obj7[7] = TimeDimMemDesc[i];
                        finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);

                        ////.println("final addTimeDimMembers ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);

                        //add member details
                        String bussdltId = "";
                        if (TimeDimMemName[i].equals("Year")) {
                            bussdltId = h.get("Year").toString();
                        } else if (TimeDimMemName[i].equals("Quarter")) {
                            bussdltId = h.get("Quarter").toString();
                        } else if (TimeDimMemName[i].equals("Month")) {
                            bussdltId = h.get("Month").toString();
                        } else if (TimeDimMemName[i].equals("Week")) {
                            bussdltId = h.get("Week").toString();
                        } else if (TimeDimMemName[i].equals("Day")) {
                            bussdltId = h.get("Day").toString();
                        }
                        String addTimeDimMemberDetails = getResourceBundle().getString("addTimeDimMemberDetails");
                        int seqaddTimeDimMemberDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                        Object obj8[] = new Object[5];
                        obj8[0] = seqaddTimeDimMemberDetails;
                        obj8[1] = seqaddTimeDimMembers;
                        obj8[2] = bussdltId;
                        obj8[3] = "KEY";
                        obj8[4] = "";

                        finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);

                        ////.println("final addTimeDimMemberkey Details ---->" + finalQuery);
                        postGresArrayList.add(finalQuery);


                        String addTimeDimMemberDetails1 = getResourceBundle().getString("addTimeDimMemberDetails");
                        int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                        Object obj9[] = new Object[5];
                        obj9[0] = seqaddTimeDimMemberDetails1;
                        obj9[1] = seqaddTimeDimMembers;
                        obj9[2] = bussdltId;
                        obj9[3] = "VALUE";
                        obj9[4] = "";

                        finalQuery = pbdb.buildQuery(addTimeDimMemberDetails1, obj9);

                        ////.println("final addTimeDimMemberkey value Details ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);

                    }

                    //add Time Dim Hierachies(relations)
                    String addTimeDimRlt = getResourceBundle().getString("addTimeDimRlt");
                    int seqaddTimeDimRlt = pbdb.getSequenceNumber("select PRG_GRP_DIM_REL_SEQ.nextval from dual");

                    Object obj10[] = new Object[5];
                    obj10[0] = seqaddTimeDimRlt;
                    obj10[1] = seqaddTimeDimDimension;
                    obj10[2] = "Time";
                    obj10[3] = "N";
                    obj10[4] = "Time";

                    finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);

                    ////.println("final addTimeDimRlt ---->" + finalQuery);
                    postGresArrayList.add(finalQuery);
                    //add Time Dim Relation Details
                    for (int i = 0; i < Integer.parseInt(minTimeLevel); i++) {
                        String addTimeDimRltDetails = getResourceBundle().getString("addTimeDimRltDetails");
                        Object obj11[] = new Object[3];
                        obj11[0] = seqaddTimeDimRlt;
                        obj11[1] = memberIds[i];
                        obj11[2] = level[i];
                        finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                        ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);

                    }
                    //add Time Dim info
                    String addTimeDimInfo = getResourceBundle().getString("addTimeDimInfo");
                    int seqaddTimeDimInfo = pbdb.getSequenceNumber("select PRG_TIME_DIM_INFO_SEQ.nextval from dual");
                    if (minTimeLevel.equals("5")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = seqaddTimeDimBussMater;
                        obj12[2] = bussTableId;
                        obj12[3] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = h.get("Day").toString();
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                        ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);
                    } else if (minTimeLevel.equals("3")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = seqaddTimeDimBussMater;
                        obj12[2] = bussTableId;
                        if (request.getParameter("monthJoinType").equals("F")) {
                            obj12[3] = request.getParameter("timeDimKeyValuemonth").split("~")[0];
                        } else if (request.getParameter("monthJoinType").equals("M")) {
                            obj12[3] = bussColId;
                        } else {
                            obj12[3] = request.getParameter("timeDimKeyValuesingleMonthno").split("~")[0];
                        }
                        // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);

                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = rltmonthstrId;
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                        ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);
                    } else if (minTimeLevel.equals("4")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = seqaddTimeDimBussMater;
                        obj12[2] = bussTableId;
                        if (request.getParameter("weekJoinType").equals("S")) {
                            obj12[3] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                        } else {
                            obj12[3] = bussColId;
                        }
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                        ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);
                    } else if (minTimeLevel.equals("2")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = seqaddTimeDimBussMater;
                        obj12[2] = bussTableId;
                        if (request.getParameter("quarterJoinType").equals("S")) {
                            obj12[3] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                        } else {
                            obj12[3] = bussColId;
                        }
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                        ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);
                    } else if (minTimeLevel.equals("1")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = seqaddTimeDimBussMater;
                        obj12[2] = bussTableId;
                        obj12[3] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                        ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);
                    }
                    // //////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);

                    //add to rlt master
                    String addTimeDimBussRltDetails = getResourceBundle().getString("addTimeDimBussRltDetails");
                    if (minTimeLevel.equals("5")) {
                        String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");

                        int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                        Object obj13[] = new Object[11];
                        obj13[0] = seqaddTimeDimInfo;
                        obj13[1] = seqaddTimeDimBussRltDetails;
                        obj13[2] = bussTableId;
                        obj13[3] = timeDimKeyValueDay.split("~")[0];
                        obj13[4] = seqaddTimeDimBussMater;
                        obj13[5] = rltdateId;
                        obj13[6] = "inner";
                        obj13[7] = "=";
                        obj13[8] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                        obj13[9] = "DDATE";
                        obj13[10] = "";
                        finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                        ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                        postGresArrayList.add(finalQuery);//Moved from batchSt1 to batchSt



                    } else if (minTimeLevel.equals("4")) {


                        if (request.getParameter("weekJoinType").equals("S")) {

                            String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValuesingleWeekno");
                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            Object obj13[] = new Object[11];
                            obj13[0] = seqaddTimeDimInfo;
                            obj13[1] = seqaddTimeDimBussRltDetails;
                            obj13[2] = bussTableId;
                            obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                            obj13[4] = seqaddTimeDimBussMater;
                            obj13[5] = rltweeknoId;
                            obj13[6] = "inner";
                            obj13[7] = "=";
                            obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                            obj13[9] = "CWEEK";
                            obj13[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);




                        } else {
                            String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                            String timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            Object obj13[] = new Object[11];
                            obj13[0] = seqaddTimeDimInfo;
                            obj13[1] = seqaddTimeDimBussRltDetails;
                            obj13[2] = bussTableId;
                            obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                            obj13[4] = seqaddTimeDimBussMater;
                            obj13[5] = rltweeknoId;
                            obj13[6] = "inner";
                            obj13[7] = "=";
                            obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                            obj13[9] = "CWEEK";
                            obj13[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);



                            int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            Object obj14[] = new Object[11];
                            obj14[0] = seqaddTimeDimInfo;
                            obj14[1] = seqaddTimeDimBussRltDetails1;
                            obj14[2] = bussTableId;
                            obj14[3] = timeDimKeyValueWeekyr.split("~")[0];
                            obj14[4] = seqaddTimeDimBussMater;
                            obj14[5] = rltweekyearId;
                            obj14[6] = "inner";
                            obj14[7] = "=";
                            obj14[8] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                            obj14[9] = "CW_YEAR";
                            obj14[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);


                        }
                    } else if (minTimeLevel.equals("3")) {

                        String monthFormatyn = request.getParameter("monthJoinType");

                        if (monthFormatyn.equals("F")) {
                            String monthPre = request.getParameter("monthPre");
                            String monthFormat = request.getParameter("monthFormat");
                            String monthPost = request.getParameter("monthPost");
                            //   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("month pre---------->"+monthPre);
                            //    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("month post---------->"+monthPost);
                            String totalMonthFormat = "";
                            if (monthPre != null && monthPost != null) {
                                totalMonthFormat = monthPre + "&" + monthFormat + "~" + monthPost;
                            } else if (monthPre != null) {
                                totalMonthFormat = monthPre + "&" + monthFormat;
                            } else if (monthPost != null) {
                                totalMonthFormat = monthFormat + "~" + monthPost;
                            }

                            String timeDimKeyValuemonth = request.getParameter("timeDimKeyValuemonth");
                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            Object obj13[] = new Object[11];
                            obj13[0] = seqaddTimeDimInfo;
                            obj13[1] = seqaddTimeDimBussRltDetails;
                            obj13[2] = bussTableId;
                            obj13[3] = timeDimKeyValuemonth.split("~")[0];
                            obj13[4] = seqaddTimeDimBussMater;
                            obj13[5] = rltmonthstrId;
                            obj13[6] = "inner";
                            obj13[7] = "=";
                            obj13[8] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                            obj13[9] = "CM_ST_DATE";
                            obj13[10] = totalMonthFormat;
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);


                        } else if (monthFormatyn.equals("M")) {
                            String timeDimKeyValueMonthyr = request.getParameter("timeDimKeyValueMonthyr");
                            String timeDimKeyValueMonthno = request.getParameter("timeDimKeyValueMonthno");
                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            Object obj13[] = new Object[11];
                            obj13[0] = seqaddTimeDimInfo;
                            obj13[1] = seqaddTimeDimBussRltDetails;
                            obj13[2] = bussTableId;
                            obj13[3] = timeDimKeyValueMonthno.split("~")[0];
                            obj13[4] = seqaddTimeDimBussMater;
                            //obj13[5] = rltmonthnoId;
                            obj13[5] = h.get("Month").toString();
                            obj13[6] = "inner";
                            obj13[7] = "=";
                            //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
                            //obj13[9]="CMONTH";h.get("Month").toString();
                            obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                            obj13[9] = "CM_CUST_NAME";
                            obj13[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);



                            int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            Object obj14[] = new Object[11];
                            obj14[0] = seqaddTimeDimInfo;
                            obj14[1] = seqaddTimeDimBussRltDetails1;
                            obj14[2] = bussTableId;
                            obj14[3] = timeDimKeyValueMonthyr.split("~")[0];
                            obj14[4] = seqaddTimeDimBussMater;
                            obj14[5] = rltmonthyearId;
                            obj14[6] = "inner";
                            obj14[7] = "=";
                            obj14[8] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                            obj14[9] = "CM_YEAR";
                            obj14[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);

                        } else {

                            String timeDimKeyValueMonthno = request.getParameter("timeDimKeyValuesingleMonthno");
                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            Object obj13[] = new Object[11];
                            obj13[0] = seqaddTimeDimInfo;
                            obj13[1] = seqaddTimeDimBussRltDetails;
                            obj13[2] = bussTableId;
                            obj13[3] = timeDimKeyValueMonthno.split("~")[0];
                            obj13[4] = seqaddTimeDimBussMater;
                            // obj13[5] = rltmonthnoId;
                            obj13[5] = h.get("Month").toString();
                            obj13[6] = "inner";
                            obj13[7] = "=";
                            obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                            //obj13[9]="CMONTH";
                            obj13[9] = "CM_CUST_NAME";
                            obj13[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);


                        }
                    } else if (minTimeLevel.equals("2")) {
                        //for mintime level Quarter
                        if (request.getParameter("quarterJoinType").equals("S")) {
                            String timeDimKeyValueQuaterno = request.getParameter("timeDimKeyValuesingleQuaterno");
                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                            Object obj13[] = new Object[11];
                            obj13[0] = seqaddTimeDimInfo;
                            obj13[1] = seqaddTimeDimBussRltDetails;
                            obj13[2] = bussTableId;
                            obj13[3] = timeDimKeyValueQuaterno.split("~")[0];
                            obj13[4] = seqaddTimeDimBussMater;
                            obj13[5] = rltqtrnoId;
                            obj13[6] = "inner";
                            obj13[7] = "=";
                            obj13[8] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                            obj13[9] = "CQTR";
                            obj13[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);


                        } else {
                            String timeDimKeyValueQuaterno = request.getParameter("timeDimKeyValueQuaterno");

                            String timeDimKeyValueQuateryr = request.getParameter("timeDimKeyValueQuateryr");


                            int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                            Object obj13[] = new Object[11];
                            obj13[0] = seqaddTimeDimInfo;
                            obj13[1] = seqaddTimeDimBussRltDetails;
                            obj13[2] = bussTableId;
                            obj13[3] = timeDimKeyValueQuaterno.split("~")[0];
                            obj13[4] = seqaddTimeDimBussMater;
                            obj13[5] = rltqtrnoId;
                            obj13[6] = "inner";
                            obj13[7] = "=";
                            obj13[8] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                            obj13[9] = "CQTR";
                            obj13[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);



                            int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                            Object obj14[] = new Object[11];
                            obj14[0] = seqaddTimeDimInfo;
                            obj14[1] = seqaddTimeDimBussRltDetails1;
                            obj14[2] = bussTableId;
                            obj14[3] = timeDimKeyValueQuateryr.split("~")[0];
                            obj14[4] = seqaddTimeDimBussMater;
                            obj14[5] = rltqtryearId;
                            obj14[6] = "inner";
                            obj14[7] = "=";
                            obj14[8] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                            obj14[9] = "CQ_YEAR";
                            obj14[10] = "";
                            finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                            ////.println("final addTimeDimRltDetails ---->" + finalQuery);
                            arrayListofQuerys.add(finalQuery);


                        }
                    } else if (minTimeLevel.equals("1")) {

                        String timeDimKeyValueYear = request.getParameter("timeDimKeyValueYear");
                        int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                        Object obj14[] = new Object[11];
                        obj14[0] = seqaddTimeDimInfo;
                        obj14[1] = seqaddTimeDimBussRltDetails1;
                        obj14[2] = bussTableId;
                        obj14[3] = timeDimKeyValueYear.split("~")[0];
                        obj14[4] = seqaddTimeDimBussMater;
                        obj14[5] = rltyearId;
                        obj14[6] = "inner";
                        obj14[7] = "=";
                        obj14[8] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                        obj14[9] = "CYEAR";
                        obj14[10] = "";
                        finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                        ////.println("final addTimeDimRltdetails ---->" + finalQuery);
                        arrayListofQuerys.add(finalQuery);

                    }

                    // pbdb.executeMultiple(timeDimList);

                    pbDb.executeMultiple(arrayListofQuerys);
                    pbDb.executeMultiple(postGresArrayList);
                    //////.println.println("--------------length=="+c1.length);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------------length=="+c.length);

                }
            } else {
                //////.println("The Type of connection is other then oracle and postgresql");
            }
            request.setAttribute("forward", "success");
            out.print("true");
        } catch (Exception e) {
            logger.error("Exception:", e);
            out.print("false");
        }

        return mapping.findForward(SUCCESS);
    }
}
