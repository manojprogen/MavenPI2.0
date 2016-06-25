/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import com.progen.connection.ConnectionDAO;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author arun
 */
public class SaveBucketsBD {

    public static Logger logger = Logger.getLogger(SaveBucketsBD.class);
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

    public boolean insertBucket(HttpServletRequest request, HttpServletResponse response) {

        Connection dataConnection = null;
        //Connection metaConnection;
        Statement st, stGetIdent;
        ResultSet rs;
        boolean checkStatus = false;
        Object[] obj;
        String finalQuery;
        BusinessGroupDAO pg = new BusinessGroupDAO();
        String dbType = "";
        String actualClauseForSummarized = "";
        String actualClauseForSummarizedRlt = "";
        String tempFormula = "";
        ArrayList querylList = new ArrayList();
        try {
            prg.db.PbDb pbdb = new prg.db.PbDb();
            String measureName = request.getParameter("measure");
            String bucketName1 = request.getParameter("bucket").replace("_", " ");
            String bucketDesc1 = request.getParameter("bdesc").replace("_", " ");
            String bucketName = request.getParameter("bucket").toUpperCase();
            bucketName = bucketName.trim().replace(" ", "_");
            String bucketDesc = request.getParameter("bdesc").toUpperCase();
            String tabId = request.getParameter("tabId");
            String colId = request.getParameter("colId");
            String colType = request.getParameter("colType");
            String colName = request.getParameter("colName");
            String tabName = request.getParameter("tabName");
            String grpId = request.getParameter("grpId");
            String bussTableId = request.getParameter("bussTableId");
            String bussColId = request.getParameter("bussColId");
            String connId = request.getParameter("connid");
            String elementId = request.getParameter("elementId");
            String buckMaxVal = request.getParameter("buckMaxVal");
            String buckMinVal = request.getParameter("buckMinVal");
            String buckAvgVal = request.getParameter("buckAvgVal");
            String actualColFormula = "";
            String buckAvgMinMaxDetails = buckAvgVal + "," + buckMinVal + "," + buckMaxVal;
            dbType = pg.getBussTableConnectionDbType(bussTableId);
            String userColType = "";
            String IsSummarized = "";
            String bussColName = "";

            //Haven't got Table Id fetch it from Business Column Id and ELement Id
            if (tabId == null && elementId == null) {
                return false;
            } else {
//            } else if (tabId == null || tabId.equalsIgnoreCase("")) {
                String busTabColQry = this.getResourceBundle().getString("getBussTableColumns");
                obj = new Object[1];
                obj[0] = elementId;

                finalQuery = pbdb.buildQuery(busTabColQry, obj);
                PbReturnObject busTabRetObj = pbdb.execSelectSQL(finalQuery);
                if (busTabRetObj.getRowCount() > 0) {
                    bussTableId = busTabRetObj.getFieldValueString(0, "BUSS_TABLE_ID");
                    bussColId = busTabRetObj.getFieldValueString(0, "BUSS_COL_ID");
                    actualColFormula = busTabRetObj.getFieldValueString(0, "ACTUAL_COL_FORMULA");
                    userColType = busTabRetObj.getFieldValueString(0, "USER_COL_TYPE");
                    bussColName = busTabRetObj.getFieldValueString(0, "BUSS_COL_NAME");
                }
                actualColFormula = actualColFormula.replace("'", "''");


                String masTabQry = this.getResourceBundle().getString("getMasterTableDtls");

                obj = new Object[1];
                obj[0] = bussTableId;

                finalQuery = pbdb.buildQuery(masTabQry, obj);
                PbReturnObject masTabRetObj = pbdb.execSelectSQL(finalQuery);
                if (masTabRetObj.getRowCount() > 0) {
                    tabId = masTabRetObj.getFieldValueString(0, "DB_TABLE_ID");
                    tabName = masTabRetObj.getFieldValueString(0, "DB_TABLE_NAME");
                    connId = masTabRetObj.getFieldValueString(0, "CONNECTION_ID");
                    grpId = masTabRetObj.getFieldValueString(0, "GRP_ID");
                }


                String masTabColQry = this.getResourceBundle().getString("getMasterTableColDtls");

                obj = new Object[2];
                obj[0] = bussTableId;
                obj[1] = bussColId;

                finalQuery = pbdb.buildQuery(masTabColQry, obj);
                PbReturnObject masTabColRetObj = pbdb.execSelectSQL(finalQuery);
                if (masTabColRetObj.getRowCount() > 0) {
                    colId = masTabColRetObj.getFieldValueString(0, "DB_COLUMN_ID");
                    colType = masTabColRetObj.getFieldValueString(0, "COLUMN_TYPE");
                    colName = masTabColRetObj.getFieldValueString(0, "TABLE_COL_NAME");
                }
                if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                    colId = "0";
                    colType = "SUMMARIZED";
                    colName = bussColName;
                    bussColId = "0";
                    IsSummarized = "Y";
                }
            }

            // commented By Nazneen on 28Jan14
//            int number = Integer.parseInt(request.getParameter("number"));
            // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("measureName " + measureName + " bucketDesc " + bucketDesc);
            //con = ProgenConnection.getInstance().getConnection();



            // st2 = con.createStatement();
//            dataConnection = ProgenConnection.getInstance().getConnectionByTable(tabId);
            dataConnection = ProgenConnection.getInstance().getConnectionForElement(elementId);
//            /
            ConnectionDAO conDAO = new ConnectionDAO();
            int connection = conDAO.getConnectionByTable(tabId).getConnId();
            String dataConnectionType = conDAO.getConnectionByTable(tabId).getDbType();
            connId = String.valueOf(connection);

//            String conne = "select connection_id from prg_db_master_table where table_id=" + tabId;
//            //////////////////////////////////////////////////////////////////////////////.println.println("conne------------------"+conne);
//            PbReturnObject rs2 = pbdb.execSelectSQL(conne);
//            int connection = rs2.getFieldValueInt(0, "CONNECTION_ID");
//            connId = String.valueOf(connection);
//            //////////////////////////////////////////////////////////////////////////////.println.println("connId---"+connId);
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connection---" + connection);
//            BusinessGroupDAO b = new BusinessGroupDAO();
//            con = b.getBussTableConnection(bussTableId);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("con"+con);
            ArrayList bucketList = new ArrayList();
            st = dataConnection.createStatement();
//            st1 = dataConnection.createStatement();
            Statement stdup = dataConnection.createStatement();

            ResultSet rsdup = stdup.executeQuery("select bucket_name from prg_grp_bucket_master where upper(bucket_name) = upper('" + bucketName + "')");
            PbReturnObject pbrodup = new PbReturnObject(rsdup);

            int dup = 0;
            if (rsdup.next()) {
                dup = 1;
            }

            rsdup.close();
            rsdup = null;
            stdup.close();
            stdup = null;

            if (dup == 0) {
                rs = null;
                String Q1 = "";
                String Q2 = "";
                int nextval = 0;
                if (dbType.equalsIgnoreCase("SqlServer")) {
//                    rs = st.executeQuery("select prg_grp_bucket_master_seq.nextval from dual");
                    Q1 = "insert into prg_grp_bucket_master (bucket_name,bucket_desc,grp_id,BUSS_COL_ID,element_id,AVG_MIN_MAX_DESC,IS_SUMMARIZED) values ('" + bucketName + "','" + bucketDesc1.replaceAll("_", " ") + "'," + grpId + "," + bussColId + "," + elementId + ",'" + buckAvgMinMaxDetails + "','" + IsSummarized + "')";

                } else if (dbType.equalsIgnoreCase("ORACLE")) {
                    rs = st.executeQuery("select prg_grp_bucket_master_seq.nextval from dual");
                    rs.next();
                    nextval = rs.getInt(1);
                    Q1 = "insert into prg_grp_bucket_master (bucket_id,bucket_name,bucket_desc,grp_id,BUSS_COL_ID,element_id,AVG_MIN_MAX_DESC,IS_SUMMARIZED) values (" + nextval + ",'" + bucketName + "','" + bucketDesc1.replaceAll("_", " ") + "'," + grpId + "," + bussColId + "," + elementId + ",'" + buckAvgMinMaxDetails + "','" + IsSummarized + "')";

                } else if (dbType.equalsIgnoreCase("PostgreSQL")) {
                    rs = st.executeQuery("select nextval('prg_grp_bucket_master_seq')");
                    rs.next();
                    nextval = rs.getInt(1);
                    Q1 = "insert into prg_grp_bucket_master (bucket_id,bucket_name,bucket_desc,grp_id,BUSS_COL_ID,element_id,AVG_MIN_MAX_DESC,IS_SUMMARIZED) values (" + nextval + ",'" + bucketName + "','" + bucketDesc1.replaceAll("_", " ") + "'," + grpId + "," + bussColId + "," + elementId + ",'" + buckAvgMinMaxDetails + "','" + IsSummarized + "')";

                } else if (dbType.equalsIgnoreCase("Mysql")) {
//                    rs = st.executeQuery("select prg_grp_bucket_master_seq.nextval from dual");
                    Q1 = "insert into prg_grp_bucket_master (bucket_name,bucket_desc,grp_id,BUSS_COL_ID,element_id,AVG_MIN_MAX_DESC,IS_SUMMARIZED) values ('" + bucketName + "','" + bucketDesc1.replaceAll("_", " ") + "'," + grpId + "," + bussColId + "," + elementId + ",'" + buckAvgMinMaxDetails + "','" + IsSummarized + "')";

                }



//                ////.println("Q1 is " + Q1);
                querylList.add(Q1);
                String actualClause = "";
                // added By Nazneen on 28Jan14
                int number = Integer.parseInt(request.getParameter("number"));
                int truncNo = number;
                int trun = truncNo;
                if (trun != 0) {
                }


                for (int i = 1; i <= number; i++) {
                    tempFormula = "";
                    //      //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("REQUEST--->" + request.getParameter("sl" + i).equals(""));
                    if ((request.getParameter("dv" + i) != null) && (request.getParameter("sl" + i) != null) && (request.getParameter("el" + i) != null) && (request.getParameter("dv" + i).equals("") != true) && (request.getParameter("sl" + i).equals("") != true) && (request.getParameter("el" + i).equals("") != true)) {
                        String displayValue = request.getParameter("dv" + i).trim();
                        // modified By Nazneen on 28Jan14
//                        for(int j=0;j<no;j++){
//                            displayValue = " "+displayValue;
//                        }
//                        no--;
                        if (truncNo != 0) {
                            for (int j = 0; j < truncNo; j++) {
                                displayValue = " " + displayValue;
                            }
                        }
                        truncNo--;

                        double startLimit = Double.parseDouble(request.getParameter("sl" + i));

                        double endLimit = Double.parseDouble(request.getParameter("el" + i));
                        // actualClause+=tabName+"."+measureName+" between "+startLimit+" AND "+endLimit;
                        // if(i+1<=number){
                        //      actualClause+=" OR " ;
                        //  }
                        if (dbType.equalsIgnoreCase("SqlServer")) {
                            Q2 = "insert into prg_grp_bucket_details (bucket_disp_value,bucket_disp_cust_value,order_seq,start_limit,end_limit,bucket_id) values ('" + displayValue + "','" + displayValue + "'," + i + "," + startLimit + "," + endLimit + ",ident_current('prg_grp_bucket_master') )";
                        } else if (dbType.equalsIgnoreCase("ORACLE")) {
                            Q2 = "insert into prg_grp_bucket_details (bucket_col_id,bucket_disp_value,bucket_disp_cust_value,order_seq,start_limit,end_limit,bucket_id) values (prg_grp_bucket_details_seq.nextval,'" + displayValue + "','" + displayValue + "'," + i + "," + startLimit + "," + endLimit + "," + nextval + ")";
                        } else if (dbType.equalsIgnoreCase("PostgreSQL")) {
                            Q2 = "insert into prg_grp_bucket_details (bucket_col_id,bucket_disp_value,bucket_disp_cust_value,order_seq,start_limit,end_limit,bucket_id) values (nextval('prg_grp_bucket_details_seq'),'" + displayValue + "','" + displayValue + "'," + i + "," + startLimit + "," + endLimit + "," + nextval + ")";
                        } else if (dbType.equalsIgnoreCase("Mysql")) {
                            Q2 = "insert into prg_grp_bucket_details (bucket_disp_value,bucket_disp_cust_value,order_seq,start_limit,end_limit,bucket_id) values ('" + displayValue + "','" + displayValue + "'," + i + "," + startLimit + "," + endLimit + ",(select Last_Insert_Id(BUCKET_ID) from prg_grp_bucket_master order by 1 desc limit 1) )";
                        }

                        tempFormula = " CASE WHEN (" + actualColFormula + ") BETWEEN " + startLimit + " AND " + endLimit + " THEN '" + displayValue + "' ELSE ";
                        actualClauseForSummarized += tempFormula;
//                        ////.println("Q2 is " + Q2);
                        querylList.add(Q2);
                    }
                }
                actualClauseForSummarized += "'OTHERS'";
                for (int i = 1; i <= number; i++) {
                    actualClauseForSummarized += " END ";
                }


                boolean check = pbdb.executeMultiple(querylList, dataConnection);
                dataConnection = null;
                dataConnection = ProgenConnection.getInstance().getConnectionByTable(tabId);
                if (dataConnectionType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    stGetIdent = dataConnection.createStatement();
                    rs = stGetIdent.executeQuery("select ident_current('prg_grp_bucket_master')");
                    rs.next();
                    nextval = rs.getInt(1);
                } //added by nazneen
                else if (dataConnectionType.equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    stGetIdent = dataConnection.createStatement();
                    rs = stGetIdent.executeQuery("select LAST_INSERT_ID(BUCKET_ID) from prg_grp_bucket_master order by 1 desc limit 1");
                    rs.next();
                    nextval = rs.getInt(1);
                }
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------------"+c.length);
                if (check) {
                    ArrayList bucketDetList = new ArrayList();
                    //con = ProgenConnection.getInstance().getConnection();
                    // st3 = con.createStatement();
                    //adding to buss master table
//                    PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();

                    String Query = "";
                    Query = "SELECT m.BUCKET_ID, m.BUCKET_NAME, m.BUCKET_DESC,d.BUCKET_COL_ID,d.BUCKET_DISP_VALUE, d.BUCKET_DISP_CUST_VALUE,d.ORDER_SEQ,d.START_LIMIT,d.END_LIMIT FROM PRG_GRP_BUCKET_MASTER m,PRG_GRP_BUCKET_DETAILS d where m.bucket_id=d.bucket_id and m.bucket_id=" + nextval;


                    String bucketColnames[] = new String[9];
                    String bucketColTypes[] = new String[9];
                    String bucketColLength[] = new String[9];
                    bucketColnames[0] = "BUCKET_ID";
                    bucketColTypes[0] = "NUMBER";
                    bucketColLength[0] = "22";
                    bucketColnames[1] = "BUCKET_NAME";
                    bucketColTypes[1] = "VARCHAR2";
                    bucketColLength[1] = "100";
                    bucketColnames[2] = "BUCKET_DESC";
                    bucketColTypes[2] = "VARCHAR2";
                    bucketColLength[2] = "255";
                    // bucketColnames[3]="TABLE_ID";
                    //   bucketColTypes[3]="NUMBER";
                    //  bucketColnames[4]="COLUMN_ID";
                    //    bucketColTypes[4]="NUMBER";
                    bucketColnames[3] = "BUCKET_COL_ID";
                    bucketColTypes[3] = "NUMBER";
                    bucketColLength[3] = "22";
                    bucketColnames[4] = "BUCKET_DISP_VALUE";
                    bucketColTypes[4] = "VARCHAR2";
                    bucketColLength[4] = "255";
                    bucketColnames[5] = "BUCKET_DISP_CUST_VALUE";
                    bucketColTypes[5] = "VARCHAR2";
                    bucketColLength[5] = "255";
                    bucketColnames[6] = "ORDER_SEQ";
                    bucketColTypes[6] = "NUMBER";
                    bucketColLength[6] = "22";
                    bucketColnames[7] = "START_LIMIT";
                    bucketColTypes[7] = "NUMBER";
                    bucketColLength[7] = "22";
                    bucketColnames[8] = "END_LIMIT";
                    bucketColTypes[8] = "NUMBER";
                    bucketColLength[8] = "22";
                    String addBucketdbMaster = getResourceBundle().getString("addBucketdbMaster");
                    finalQuery = "";
                    Object obj4[];
                    obj4 = new Object[5];
                    obj4[0] = bucketName;
                    obj4[1] = connection;
                    obj4[2] = bucketName;
                    obj4[3] = bucketName;
                    obj4[4] = Query;

                    finalQuery = pbdb.buildQuery(addBucketdbMaster, obj4);
                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addBucketdbMater------>" + finalQuery);
                    bucketDetList.add(finalQuery);
                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
                    String addBucketdbDetails = getResourceBundle().getString("addBucketdbDetails");
                    finalQuery = "";
                    int dbDetIds[] = new int[bucketColnames.length];
                    for (int i = 0; i < bucketColnames.length; i++) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                            PbReturnObject dbseqval = pbdb.execSelectSQL("select PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval from dual");

//                            int dbseqnextval = dbseqval.getFieldValueInt(0, "NEXTVAL");
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---dbseqnextval-----"+dbseqnextval);
//                            dbDetIds[i] = dbseqnextval;
                            Object obj5[];
                            obj5 = new Object[5];
//                            obj5[0] = dbseqnextval;
                            obj5[0] = bucketColnames[i];
                            obj5[1] = bucketColnames[i];
                            obj5[2] = bucketColTypes[i];
                            obj5[3] = bucketColLength[i];
                            obj5[4] = "N";
                            finalQuery = pbdb.buildQuery(addBucketdbDetails, obj5);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                            PbReturnObject dbseqval = pbdb.execSelectSQL("select PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval from dual");

//                            int dbseqnextval = dbseqval.getFieldValueInt(0, "NEXTVAL");
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---dbseqnextval-----"+dbseqnextval);
//                            dbDetIds[i] = dbseqnextval;
                            Object obj5[];
                            obj5 = new Object[5];
//                            obj5[0] = dbseqnextval;
                            obj5[0] = bucketColnames[i];
                            obj5[1] = bucketColnames[i];
                            obj5[2] = bucketColTypes[i];
                            obj5[3] = bucketColLength[i];
                            obj5[4] = "N";
                            finalQuery = pbdb.buildQuery(addBucketdbDetails, obj5);
                        } else {
                            PbReturnObject dbseqval = pbdb.execSelectSQL("select PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval from dual");

                            int dbseqnextval = dbseqval.getFieldValueInt(0, "NEXTVAL");
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---dbseqnextval-----"+dbseqnextval);
                            dbDetIds[i] = dbseqnextval;
                            Object obj5[];
                            obj5 = new Object[6];
                            obj5[0] = dbseqnextval;
                            obj5[1] = bucketColnames[i];
                            obj5[2] = bucketColnames[i];
                            obj5[3] = bucketColTypes[i];
                            obj5[4] = bucketColLength[i];
                            obj5[5] = "N";
                            finalQuery = pbdb.buildQuery(addBucketdbDetails, obj5);
                        }

                        //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addBucketBussMater db--details---->" + finalQuery);
                        bucketDetList.add(finalQuery);

                    }
                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
                    String addBucketBussMater = getResourceBundle().getString("addBucketBussMater");
                    obj = new Object[6];
                    obj[0] = bucketName;
                    obj[1] = bucketDesc.replace("_", " ");
                    obj[2] = "Query";
                    obj[3] = 1;
                    //  obj[4]=tabId;
                    // obj[4]=Query;
                    obj[4] = grpId;
                    obj[5] = Query;

                    finalQuery = pbdb.buildQuery(addBucketBussMater, obj);
                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addBucketBussMater------>" + finalQuery);
                    bucketDetList.add(finalQuery);
                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
                    //adding to src mater table
                    String addBucketSrc = getResourceBundle().getString("addBucketSrc");
                    finalQuery = "";
                    Object obj1[];
                    obj1 = new Object[4];
                    // obj1[0]=tabId;
                    obj1[0] = "Query";
                    obj1[1] = bucketName;
                    obj1[2] = connection;
                    obj1[3] = Query;


                    finalQuery = pbdb.buildQuery(addBucketSrc, obj1);
                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addBucketsrc master------>" + finalQuery);
                    bucketDetList.add(finalQuery);
                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
                    //adding to src details
                    String addBucketsrcDetails = getResourceBundle().getString("addBucketsrcDetails");
                    int srcDetIds[] = new int[bucketColnames.length];
                    for (int i = 0; i < bucketColnames.length; i++) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                            PbReturnObject srcseqval = pbdb.execSelectSQL("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");

//                            int nextval1 = srcseqval.getFieldValueInt(0, "NEXTVAL");
//                            srcDetIds[i] = nextval1;
                            finalQuery = "";
                            Object obj2[];
                            obj2 = new Object[3];

//                            obj2[0] = nextval1;
                            //obj2[1]=bussColId;

                            obj2[0] = bucketColnames[i];
                            obj2[1] = bucketColTypes[i];
                            obj2[2] = dbDetIds[i];//db dolumn id
                            finalQuery = pbdb.buildQuery(addBucketsrcDetails, obj2);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            finalQuery = "";
                            Object obj2[];
                            obj2 = new Object[3];
                            obj2[0] = bucketColnames[i];
                            obj2[1] = bucketColTypes[i];
                            obj2[2] = dbDetIds[i];//db dolumn id
                            finalQuery = pbdb.buildQuery(addBucketsrcDetails, obj2);
                        } else {
                            PbReturnObject srcseqval = pbdb.execSelectSQL("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");

                            int nextval1 = srcseqval.getFieldValueInt(0, "NEXTVAL");
                            srcDetIds[i] = nextval1;
                            finalQuery = "";
                            Object obj2[];
                            obj2 = new Object[4];

                            obj2[0] = nextval1;
                            //obj2[1]=bussColId;

                            obj2[1] = bucketColnames[i];
                            obj2[2] = bucketColTypes[i];
                            obj2[3] = dbDetIds[i];//db dolumn id
                            finalQuery = pbdb.buildQuery(addBucketsrcDetails, obj2);
                        }

                        //   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addBucketsrc details------>" + i + "--------------" + finalQuery);
                        bucketDetList.add(finalQuery);
                    }
                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
                    //adding to buss details
                    String addBucketBussDetails = getResourceBundle().getString("addBucketBussDetails");
                    finalQuery = "";
                    int bussDetIds[] = new int[bucketColnames.length];
                    String rltstartlimit = "";
                    String rltendlimit = "";
                    ArrayList nextvalue = new ArrayList();
                    int nextval1 = 0;
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        PbReturnObject bussdetseqval = pbdb.execSelectSQL("select ident_current('PRG_GRP_BUSS_TABLE_DETAILS')");
                        nextval1 = bussdetseqval.getFieldValueInt(0, 0) + 1;

                    }
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        PbReturnObject bussdetseqval = pbdb.execSelectSQL("select Last_Insert_Id(BUSS_COLUMN_ID) from PRG_GRP_BUSS_TABLE_DETAILS order by 1 desc limit 1");
                        nextval1 = bussdetseqval.getFieldValueInt(0, 0) + 1;

                    }
                    for (int i = 0; i < bucketColnames.length; i++) {

                        Object obj3[] = null;
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            nextvalue.add(nextval1 + i);
                            obj3 = new Object[6];
                            obj3[0] = bucketColnames[i];
                            obj3[1] = srcDetIds[i];
                            obj3[2] = bucketColTypes[i];
                            obj3[3] = nextval;
                            obj3[4] = bucketColnames[i];
                            obj3[5] = dbDetIds[i];//db dolumn id

                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            nextvalue.add(nextval1 + i);
                            obj3 = new Object[6];
                            obj3[0] = bucketColnames[i];
                            obj3[1] = srcDetIds[i];
                            obj3[2] = bucketColTypes[i];
                            obj3[3] = nextval;
                            obj3[4] = bucketColnames[i];
                            obj3[5] = dbDetIds[i];//db dolumn id

                        } else {
                            PbReturnObject bussdetseqval = pbdb.execSelectSQL("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                            nextval1 = bussdetseqval.getFieldValueInt(0, 0);
                            // bussDetIds[i] = nextval1;
                            nextvalue.add(nextval1);
                            obj3 = new Object[7];
                            obj3[0] = nextval1;
                            obj3[1] = bucketColnames[i];
                            if (bucketColnames[i].equals("START_LIMIT")) {
                                rltstartlimit = String.valueOf(nextval1);
                            } else if (bucketColnames[i].equals("END_LIMIT")) {
                                rltendlimit = String.valueOf(nextval1);
                            }
                            obj3[2] = srcDetIds[i];
                            obj3[3] = bucketColTypes[i];
                            obj3[4] = nextval;
                            obj3[5] = bucketColnames[i];
                            obj3[6] = dbDetIds[i];//db dolumn id

                        }

                        finalQuery = pbdb.buildQuery(addBucketBussDetails, obj3);
                        bucketDetList.add(finalQuery);
                    }
                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
                    String addBucketBussRltMaster = getResourceBundle().getString("addBucketBussRltMaster");
                    finalQuery = "";
                    Object obj6[];
                    obj6 = new Object[4];
                    obj6[0] = bussTableId;
                    obj6[1] = "1";
//                    actualClause = tabName + "." + colName + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                    if (colType.equalsIgnoreCase("calculated")) {
                        actualClause = "(" + actualColFormula + ")" + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                    } else if (colType.equalsIgnoreCase("SUMMARIZED")) {
//                        actualClauseForSummarized = "(" + actualColFormulaTemp + ")" + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                        actualClause = "";
                    } else {
                        actualClause = tabName + "." + colName + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                    }
                    obj6[2] = actualClause;
                    obj6[3] = "inner";


                    finalQuery = pbdb.buildQuery(addBucketBussRltMaster, obj6);
                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addBucket buss rlt master------>" + finalQuery);
                    bucketDetList.add(finalQuery);
                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
                    String addBucketBussRltMasterDtl = getResourceBundle().getString("addBucketBussRltMasterDtl");
                    finalQuery = "";
                    Object obj7[] = null;
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        obj7 = new Object[7];
                        obj7[0] = bussTableId;
                        obj7[1] = bussColId;
                        obj7[2] = "ident_current('PRG_GRP_BUSS_TABLE_DETAILS')";//PRG_GRP_BUSS_TABLE_DETAILS
                        obj7[3] = "inner";
                        obj7[4] = "BETWEEN";
                        if (colType.equalsIgnoreCase("calculated")) {
                            obj7[5] = "(" + actualColFormula + ")" + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                        } else if (colType.equalsIgnoreCase("SUMMARIZED")) {
//                            actualClauseForSummarized = "(" + actualColFormulaTemp + ")" + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                            actualClause = "";
                        } else {
                            obj7[5] = tabName + "." + colName + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                        }
                        obj7[6] = "ident_current('PRG_GRP_BUSS_TABLE_DETAILS')";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        obj7 = new Object[7];
                        obj7[0] = bussTableId;
                        obj7[1] = bussColId;
                        obj7[2] = "(select Last_Insert_Id(BUSS_COLUMN_ID) from PRG_GRP_BUSS_TABLE_DETAILS order by 1 desc limit 1)";//PRG_GRP_BUSS_TABLE_DETAILS
                        obj7[3] = "inner";
                        obj7[4] = "BETWEEN";
                        if (colType.equalsIgnoreCase("calculated")) {
                            obj7[5] = "(" + actualColFormula + ")" + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                        } else if (colType.equalsIgnoreCase("SUMMARIZED")) {
//                            actualClauseForSummarized = "(" + actualColFormulaTemp + ")" + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                            actualClause = "";
                        } else {
                            obj7[5] = tabName + "." + colName + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                        }
                        obj7[6] = "(select Last_Insert_Id(BUSS_COLUMN_ID) from PRG_GRP_BUSS_TABLE_DETAILS order by 1 desc limit 1)";
                    } else {
                        obj7 = new Object[7];
                        obj7[0] = bussTableId;
                        obj7[1] = bussColId;
                        obj7[2] = rltstartlimit;
                        obj7[3] = "inner";
                        obj7[4] = "BETWEEN";
                        if (colType.equalsIgnoreCase("calculated")) {
                            obj7[5] = "(" + actualColFormula + ")" + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                        } else if (colType.equalsIgnoreCase("SUMMARIZED")) {
//                            actualClauseForSummarized = "(" + actualColFormulaTemp + ")" + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                            actualClause = "";
                        } else {
                            obj7[5] = tabName + "." + colName + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                        }
                        obj7[6] = rltendlimit;
                    }


                    finalQuery = pbdb.buildQuery(addBucketBussRltMasterDtl, obj7);
                    bucketDetList.add(finalQuery);
//                    ////.println("bucketDetList\t" + bucketDetList);
//                    for ( int i=0; i<bucketDetList.size(); i++ )
//                        ////.println("Qry++ "+bucketDetList.get(i));
                    pbdb.executeMultiple(bucketDetList);

                    //addded by praveen to insert buckets data in some of the database tables

//                    ArrayList coldimquery = new ArrayList();
                    ArrayList multyqueries = new ArrayList();

                    Object[] bcktcolms = new Object[3];
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        bcktcolms[0] = bucketName.toLowerCase().replaceAll("_", " ");
                        ////.println("bcktcolms[0]\t" + bcktcolms[0]);
                        bcktcolms[1] = bucketName.toLowerCase().replaceAll("_", " ");
                        bcktcolms[2] = grpId;
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        bcktcolms[0] = bucketName.toLowerCase().replaceAll("_", " ");
                        ////.println("bcktcolms[0]\t" + bcktcolms[0]);
                        bcktcolms[1] = bucketName.toLowerCase().replaceAll("_", " ");
                        bcktcolms[2] = grpId;
                    } else {
                        bcktcolms[0] = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
                        bcktcolms[1] = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
                        bcktcolms[2] = grpId;
                    }

                    String insertbckcolms = getResourceBundle().getString("insertbktgrpdims");

                    String buildinsertgrpdims = pbdb.buildQuery(insertbckcolms, bcktcolms);
                    //////////////////////////////////////////////////////////////////////////////.println.println("buildinsertgrpdims||||||||||||" + buildinsertgrpdims);

                    multyqueries.add(buildinsertgrpdims);

//                    Object[] bcktabcolms = new Object[1];
//                    bcktabcolms[0] = bussTableId;
                    String insrtdimtables = getResourceBundle().getString("insrtbktdimtables");
//                    String buildinsrtdimtables = pbdb.buildQuery(insrtdimtables, bcktabcolms);
                    //////////////////////////////////////////////////////////////////////////////.println.println("insrtdimtables----------" + insrtdimtables);
                    multyqueries.add(insrtdimtables);
                    Object[] bcktabdetailcolms = new Object[1];
                    String insrtgrptabdetls = getResourceBundle().getString("insrtbkttabdetails");
                    String buildgrptabdetls = "";
                    for (int i = 0; i < nextvalue.size(); i++) {
                        bcktabdetailcolms[0] = nextvalue.get(i);
                        buildgrptabdetls = pbdb.buildQuery(insrtgrptabdetls, bcktabdetailcolms);
                        multyqueries.add(buildgrptabdetls);
                        //////////////////////////////////////////////////////////////////////////////.println.println("insrtgrptabdetls============" + buildgrptabdetls);
                    }
                    Object[] dimmemobj = new Object[3];
                    String insrtmem = getResourceBundle().getString("insrtprgdimmember");
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        dimmemobj[0] = bucketName.toLowerCase().replaceAll("_", " ");
                        dimmemobj[1] = bucketName.toLowerCase().replaceAll("_", " ");
                        dimmemobj[2] = "ORDER_SEQ";

                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        dimmemobj[0] = bucketName.toLowerCase().replaceAll("_", " ");
                        dimmemobj[1] = bucketName.toLowerCase().replaceAll("_", " ");
                        dimmemobj[2] = "ORDER_SEQ";

                    } else {
                        dimmemobj[0] = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
                        dimmemobj[1] = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
                        dimmemobj[2] = "ORDER_SEQ";
                    }



                    String buildinsertmem = pbdb.buildQuery(insrtmem, dimmemobj);

                    //////////////////////////////////////////////////////////////////////////////.println.println("buildinsertmem query is" + buildinsertmem);

                    multyqueries.add(buildinsertmem);
                    Object[] dimmemdetails1 = new Object[1];
                    dimmemdetails1[0] = nextvalue.get(5);
                    String dimmemquery1 = getResourceBundle().getString("inserdimmemdetails1");
                    String dimmemquery2 = getResourceBundle().getString("inserdimmemdetails2");
                    String builddimmemquery1 = pbdb.buildQuery(dimmemquery1, dimmemdetails1);
                    String builddimmemquery2 = pbdb.buildQuery(dimmemquery2, dimmemdetails1);
                    multyqueries.add(builddimmemquery1);
                    multyqueries.add(builddimmemquery2);
                    Object[] dimrelobj = new Object[2];

                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        dimrelobj[0] = bucketName.toLowerCase().replaceAll("_", " ");
                        dimrelobj[1] = bucketName.toLowerCase().replaceAll("_", " ");
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        dimrelobj[0] = bucketName.toLowerCase().replaceAll("_", " ");
                        dimrelobj[1] = bucketName.toLowerCase().replaceAll("_", " ");
                    } else {
                        dimrelobj[0] = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
                        dimrelobj[1] = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
                    }
//                    dimrelobj[0] = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
//                    dimrelobj[1] = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
                    String dimrelquery = getResourceBundle().getString("insrtdimrel");
                    String builddimrelquery = pbdb.buildQuery(dimrelquery, dimrelobj);
                    ////////////////////////////////////////////////////////////////////////////////.println.println("builddimrelquery" + builddimrelquery);
                    multyqueries.add(builddimrelquery);
                    Object[] dimreldetailsobj = new Object[1];
                    dimreldetailsobj[0] = 1;
                    String inserdimtabdetails = getResourceBundle().getString("insrtreldetails");
                    String buildinserdimtabdetails = pbdb.buildQuery(inserdimtabdetails, dimreldetailsobj);
                    ////////////////////////////////////////////////////////////////////////////////.println.println("buildinserdimtabdetails" + buildinserdimtabdetails);
                    multyqueries.add(buildinserdimtabdetails);
                    String DBMasterQuery = "";
                    String DbDetailsQuery = "";
                    String query1 = "";
                    String GDBMasterQuery = "";
                    String GDSRCQuery = "";
                    String GDSRCDetailsQuery = "";
                    String GDDetailsQuery = "";
                    String grpDimTableQuery = "";
                    String grpDimTableDetailsQuery = "";

                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        DBMasterQuery = "insert into PRG_DB_MASTER_TABLE (CONNECTION_ID,USER_SCHEMA,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE) values (" + connId + ",'" + "PRG_BUCKET_" + colName + "','" + "PRG_BUCKET_" + colName + "','" + "PRG_BUCKET_" + colName + "','Query')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("DBMasterQuery-->" + DBMasterQuery);
                        DbDetailsQuery = "insert into PRG_DB_MASTER_TABLE_DETAILS (TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE) values (ident_current('PRG_DB_MASTER_TABLE'),'" + colName + "','" + colName + "','" + colType + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("DbDetailsQuery-->" + DbDetailsQuery);
                        query1 = "select Distinct " + colName + " from " + tabName;
                        GDBMasterQuery = "insert into PRG_GRP_BUSS_TABLE (BUSS_TABLE_NAME,BUSS_DESC,BUSS_TYPE,NO_OF_NODES,IS_PURE_QUERY,DB_TABLE_ID,GRP_ID,DB_QUERY) values ('" + "PRG_BUCKET_" + colName + "','" + "PRG BUCKET " + colName + "','Query',1,'Y',ident_current('PRG_DB_MASTER_TABLE')," + grpId + ",'" + query1 + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("");
                        GDSRCQuery = "insert into PRG_GRP_BUSS_TABLE_SRC (BUSS_TABLE_ID,CONNECTION_ID,DB_TABLE_ID,SOURCE_TYPE) values (ident_current('PRG_GRP_BUSS_TABLE')," + connId + ",ident_current('PRG_DB_MASTER_TABLE'),'Query')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + GDSRCQuery);
                        GDSRCDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_ID,DB_TABLE_ID,BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE) values (ident_current('PRG_GRP_BUSS_TABLE_SRC'),ident_current('PRG_DB_MASTER_TABLE'),ident_current('PRG_GRP_BUSS_TABLE'),ident_current('PRG_DB_MASTER_TABLE_DETAILS'),'" + colName + "','" + colType + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCDetailsQuery" + GDSRCDetailsQuery);
                        GDDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_TABLE_ID,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION,COLUMN_TYPE,COLUMN_DISP_NAME) values (ident_current('PRG_GRP_BUSS_TABLE'),'" + colName + "',ident_current('PRG_DB_MASTER_TABLE'),ident_current('PRG_DB_MASTER_TABLE_DETAILS'),ident_current('PRG_GRP_BUSS_TABLE_DETAILS'),'N','" + colType + "','" + colName + "')";
                        //  //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + GDDetailsQuery);
                        grpDimTableQuery = "insert into PRG_GRP_DIM_TABLES (DIM_ID,TAB_ID) values (ident_current('PRG_GRP_DIMENSIONS'),ident_current('PRG_GRP_BUSS_TABLE'))";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + grpDimTableQuery);
                        grpDimTableDetailsQuery = "insert into PRG_GRP_DIM_TAB_DETAILS (DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) values (ident_current('PRG_GRP_DIM_TABLES'),ident_current('PRG_GRP_BUSS_TABLE_DETAILS'),'Y','N')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("grpDimTableDetailsQuery" + grpDimTableDetailsQuery);

                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        DBMasterQuery = "insert into PRG_DB_MASTER_TABLE (CONNECTION_ID,USER_SCHEMA,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE) values (" + connId + ",'" + "PRG_BUCKET_" + colName + "','" + "PRG_BUCKET_" + colName + "','" + "PRG_BUCKET_" + colName + "','Query')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("DBMasterQuery-->" + DBMasterQuery);
                        DbDetailsQuery = "insert into PRG_DB_MASTER_TABLE_DETAILS (TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE) values ((select LAST_INSERT_ID(TABLE_ID) FROM PRG_DB_MASTER_TABLE ORDER BY 1 DESC LIMIT 1),'" + colName + "','" + colName + "','" + colType + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("DbDetailsQuery-->" + DbDetailsQuery);
                        query1 = "select Distinct " + colName + " from " + tabName;
                        GDBMasterQuery = "insert into PRG_GRP_BUSS_TABLE (BUSS_TABLE_NAME,BUSS_DESC,BUSS_TYPE,NO_OF_NODES,IS_PURE_QUERY,DB_TABLE_ID,GRP_ID,DB_QUERY) values ('" + "PRG_BUCKET_" + colName + "','" + "PRG BUCKET " + colName + "','Query',1,'Y',(select LAST_INSERT_ID(TABLE_ID) FROM PRG_DB_MASTER_TABLE ORDER BY 1 DESC LIMIT 1)," + grpId + ",'" + query1 + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("");
                        GDSRCQuery = "insert into PRG_GRP_BUSS_TABLE_SRC (BUSS_TABLE_ID,CONNECTION_ID,DB_TABLE_ID,SOURCE_TYPE) values ((select LAST_INSERT_ID(BUSS_TABLE_ID) FROM PRG_GRP_BUSS_TABLE ORDER BY 1 DESC LIMIT 1)," + connId + ",(select LAST_INSERT_ID(TABLE_ID) FROM PRG_DB_MASTER_TABLE ORDER BY 1 DESC LIMIT 1),'Query')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + GDSRCQuery);
                        GDSRCDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_ID,DB_TABLE_ID,BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE) values ((select LAST_INSERT_ID(BUSS_SOURCE_ID) FROM PRG_GRP_BUSS_TABLE_SRC ORDER BY 1 DESC LIMIT 1),(select LAST_INSERT_ID(TABLE_ID) FROM PRG_DB_MASTER_TABLE ORDER BY 1 DESC LIMIT 1),(select LAST_INSERT_ID(BUSS_TABLE_ID) FROM PRG_GRP_BUSS_TABLE ORDER BY 1 DESC LIMIT 1),(select LAST_INSERT_ID(column_id) FROM prg_db_master_table_details ORDER BY 1 DESC LIMIT 1),'" + colName + "','" + colType + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCDetailsQuery" + GDSRCDetailsQuery);
                        int SEQ = pbdb.getSequenceNumber("select LAST_INSERT_ID(BUSS_COLUMN_ID) FROM PRG_GRP_BUSS_TABLE_DETAILS ORDER BY 1 DESC LIMIT 1");
                        GDDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_TABLE_ID,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION,COLUMN_TYPE,COLUMN_DISP_NAME) values ((select LAST_INSERT_ID(BUSS_TABLE_ID) FROM PRG_GRP_BUSS_TABLE ORDER BY 1 DESC LIMIT 1),'" + colName + "',(select LAST_INSERT_ID(TABLE_ID) FROM PRG_DB_MASTER_TABLE ORDER BY 1 DESC LIMIT 1),(select LAST_INSERT_ID(column_id) FROM PRG_DB_MASTER_TABLE_DETAILS ORDER BY 1 DESC LIMIT 1)," + String.valueOf(SEQ) + ",'N','" + colType + "','" + colName + "')";
                        //  //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + GDDetailsQuery);
                        grpDimTableQuery = "insert into PRG_GRP_DIM_TABLES (DIM_ID,TAB_ID) values ((select LAST_INSERT_ID(DIM_ID) FROM PRG_GRP_DIMENSIONS ORDER BY 1 DESC LIMIT 1),(select LAST_INSERT_ID(BUSS_TABLE_ID) FROM PRG_GRP_BUSS_TABLE ORDER BY 1 DESC LIMIT 1))";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + grpDimTableQuery);
                        grpDimTableDetailsQuery = "insert into PRG_GRP_DIM_TAB_DETAILS (DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) values ((select LAST_INSERT_ID(DIM_TAB_ID) FROM PRG_GRP_DIM_TABLES ORDER BY 1 DESC LIMIT 1),(select LAST_INSERT_ID(BUSS_COLUMN_ID) FROM PRG_GRP_BUSS_TABLE_DETAILS ORDER BY 1 DESC LIMIT 1),'Y','N')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("grpDimTableDetailsQuery" + grpDimTableDetailsQuery);

                    } else {
                        DBMasterQuery = "insert into PRG_DB_MASTER_TABLE (CONNECTION_ID,USER_SCHEMA,TABLE_ID,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE) values (" + connId + ",'" + "PRG_BUCKET_" + colName + "',PRG_DATABASE_MASTER_SEQ.nextval,'" + "PRG_BUCKET_" + colName + "','" + "PRG_BUCKET_" + colName + "','Query')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("DBMasterQuery-->" + DBMasterQuery);
                        DbDetailsQuery = "insert into PRG_DB_MASTER_TABLE_DETAILS (COLUMN_ID,TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE) values (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,PRG_DATABASE_MASTER_SEQ.currval,'" + colName + "','" + colName + "','" + colType + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("DbDetailsQuery-->" + DbDetailsQuery);
                        query1 = "select Distinct " + colName + " from " + tabName;
                        GDBMasterQuery = "insert into PRG_GRP_BUSS_TABLE (BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_DESC,BUSS_TYPE,NO_OF_NODES,IS_PURE_QUERY,DB_TABLE_ID,GRP_ID,DB_QUERY) values (PRG_GRP_BUSS_TABLE_SEQ.nextval,'" + "PRG_BUCKET_" + colName + "','" + "PRG BUCKET " + colName + "','Query',1,'Y',PRG_DATABASE_MASTER_SEQ.currval," + grpId + ",'" + query1 + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("");
                        GDSRCQuery = "insert into PRG_GRP_BUSS_TABLE_SRC (BUSS_SOURCE_ID,BUSS_TABLE_ID,CONNECTION_ID,DB_TABLE_ID,SOURCE_TYPE) values (PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval,PRG_GRP_BUSS_TABLE_SEQ.currval," + connId + ",PRG_DATABASE_MASTER_SEQ.currval,'Query')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + GDSRCQuery);
                        GDSRCDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_TABLE_DTL_ID,BUSS_SRC_ID,DB_TABLE_ID,BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE) values (PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval,PRG_GRP_BUSS_TABLE_SRC_SEQ.currval,PRG_DATABASE_MASTER_SEQ.currval,PRG_GRP_BUSS_TABLE_SEQ.currval,PRG_DB_MASTER_TABLE_DTLS_SEQ.currval,'" + colName + "','" + colType + "')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCDetailsQuery" + GDSRCDetailsQuery);
                        GDDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_COLUMN_ID,BUSS_TABLE_ID,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION,COLUMN_TYPE,COLUMN_DISP_NAME) values (PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval,PRG_GRP_BUSS_TABLE_SEQ.currval,'" + colName + "',PRG_DATABASE_MASTER_SEQ.currval,PRG_DB_MASTER_TABLE_DTLS_SEQ.currval,PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.currval,'N','" + colType + "','" + colName + "')";
                        //  //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + GDDetailsQuery);
                        grpDimTableQuery = "insert into PRG_GRP_DIM_TABLES (DIM_TAB_ID,DIM_ID,TAB_ID) values (PRG_GRP_DIM_TABLES_SEQ.nextval,PRG_GRP_DIMENSIONS_SEQ.currval,PRG_GRP_BUSS_TABLE_SEQ.currval)";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery" + grpDimTableQuery);
                        grpDimTableDetailsQuery = "insert into PRG_GRP_DIM_TAB_DETAILS (DIM_TAB_COL_ID,DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) values (PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval,PRG_GRP_DIM_TABLES_SEQ.currval,PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval,'Y','N')";
                        // //////////////////////////////////////////////////////////////////////////////////.println.println("grpDimTableDetailsQuery" + grpDimTableDetailsQuery);


                    }
                    multyqueries.add(DBMasterQuery);
                    multyqueries.add(DbDetailsQuery);
                    multyqueries.add(GDBMasterQuery);
                    multyqueries.add(GDSRCQuery);
                    String addBucketBussRltMaster1 = getResourceBundle().getString("addBucketBussRltMaster");
                    finalQuery = "";
                    Object obj61[];
                    obj61 = new Object[4];
                    obj61[0] = bussTableId;
                    obj61[1] = "1";
                    if (colType.equalsIgnoreCase("calculated")) {
                        actualClause = "(" + actualColFormula + ")" + " = PRG_BUCKET_" + colName + ". " + colName;
                    } else if (colType.equalsIgnoreCase("SUMMARIZED")) {
//                        actualClauseForSummarizedRlt = "(" + actualColFormulaTemp + ")" + " = PRG_BUCKET_" + colName + ". " + colName;
//                        actualClause = "(" + actualColFormula + ")" + " = PRG_BUCKET_" + colName + ". " + colName;
                        actualClause = "";
                    } else {
                        actualClause = tabName + "." + colName + " = PRG_BUCKET_" + colName + ". " + colName;
                    }
                    obj61[2] = actualClause;
                    obj61[3] = "inner";
                    finalQuery = pbdb.buildQuery(addBucketBussRltMaster1, obj61);
                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addBucket buss rlt master------>" + finalQuery);
                    multyqueries.add(finalQuery);
                    //  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
                    String addBucketBussRltMasterDtl1 = getResourceBundle().getString("addBucketBussRltMasterDtl");
                    finalQuery = "";
                    Object obj71[];
                    obj71 = new Object[7];
                    obj71[0] = bussTableId;
                    obj71[1] = bussColId;
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        obj71[2] = "ident_current('PRG_GRP_BUSS_TABLE_DETAILS')";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        obj71[2] = "(select LAST_INSERT_ID(BUSS_COLUMN_ID) FROM PRG_GRP_BUSS_TABLE_DETAILS ORDER BY 1 DESC LIMIT 1)";
                    } else {
                        obj71[2] = "PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval";
                    }

                    obj71[3] = "inner";
                    obj71[4] = "BETWEEN";
                    if (colType.equalsIgnoreCase("calculated")) {
                        obj71[5] = "(" + actualColFormula + ")" + " = PRG_BUCKET_" + colName + ". " + colName;
                    } else if (colType.equalsIgnoreCase("SUMMARIZED")) {
//                        actualClauseForSummarizedRlt = "(" + actualColFormulaTemp + ")" + " = PRG_BUCKET_" + colName + ". " + colName;
//                        actualClause = "(" + actualColFormula + ")" + " = PRG_BUCKET_" + colName + ". " + colName;
                        actualClause = "";
                    } else {
                        obj71[5] = tabName + "." + colName + " = PRG_BUCKET_" + colName + ". " + colName;
                    }
                    obj71[6] = "0";

                    finalQuery = pbdb.buildQuery(addBucketBussRltMasterDtl1, obj71);
                    multyqueries.add(finalQuery);
                    multyqueries.add(GDSRCDetailsQuery);
                    multyqueries.add(GDDetailsQuery);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        dimmemobj[0] = colName.toLowerCase().replaceAll("_", " ");
                        dimmemobj[1] = colName.toLowerCase().replaceAll("_", " ");

                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        dimmemobj[0] = colName.toLowerCase().replaceAll("_", " ");
                        dimmemobj[1] = colName.toLowerCase().replaceAll("_", " ");

                    } else {
                        dimmemobj[0] = "initcap('" + colName.toLowerCase().replaceAll("_", " ") + "')";
                        dimmemobj[1] = "initcap('" + colName.toLowerCase().replaceAll("_", " ") + "')";

                    }

                    buildinsertmem = pbdb.buildQuery(insrtmem, dimmemobj);

                    ////////////////////////////////////////////////////////////////////////////////.println.println("buildinsertmem query is" + buildinsertmem);
                    multyqueries.add(grpDimTableQuery);
                    multyqueries.add(grpDimTableDetailsQuery);
                    multyqueries.add(buildinsertmem);
                    //multyqueries.add(buildinserdimtabdetails);
                    Object[] dimmemdetails2 = new Object[1];
                    //dimmemdetails2[0] =

                    //  String builddimmemquery3 = pbdb.buildQuery(dimmemquery2, dimmemdetails2);
                    //  String builddimmemquery4 = pbdb.buildQuery(dimmemquery1, dimmemdetails2);
                    String dimmemquery3 = getResourceBundle().getString("inserdimmemdetails3");
                    String dimmemquery4 = getResourceBundle().getString("inserdimmemdetails4");
                    String builddimmemquery3 = dimmemquery3;
                    String builddimmemquery4 = dimmemquery4;
                    multyqueries.add(builddimmemquery3);
                    multyqueries.add(builddimmemquery4);


                    dimreldetailsobj[0] = 2;

                    buildinserdimtabdetails = pbdb.buildQuery(inserdimtabdetails, dimreldetailsobj);

                    ////////////////////////////////////////////////////////////////////////////////.println.println("buildinserdimtabdetails" + buildinserdimtabdetails);
                    multyqueries.add(buildinserdimtabdetails);
//                    ////.println("multyqueries\t" + multyqueries);
//                    for ( int i=0; i<multyqueries.size(); i++ )
//                        ////.println("MultyQry++"+multyqueries.get(i));
                    pbdb.executeMultiple(multyqueries);
//                    pbdb.executeMultiple(coldimquery);

                    dataConnection.close();
                    dataConnection = null;
                }
            }
            checkStatus = true;
        } catch (Exception e) {
            checkStatus = false;
            logger.error("Exception:", e);
        } finally {
            try {
                if (dataConnection != null) {
                    dataConnection.close();
                }
            } catch (SQLException e) {
                logger.error("Exception:", e);
            }
        }
        if (checkStatus == true) {
            String lastDimIdQuery = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                lastDimIdQuery = "select ident_current('PRG_GRP_DIMENSIONS')";
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                lastDimIdQuery = "select LAST_INSERT_ID(DIM_ID) FROM PRG_GRP_DIMENSIONS ORDER BY 1 DESC LIMIT 1";
            } else {
                lastDimIdQuery = "select PRG_GRP_DIMENSIONS_SEQ.currval from dual ";
            }
            prg.db.PbDb pbdb1 = new prg.db.PbDb();
            PbReturnObject bussdetseqval = null;
            try {
                bussdetseqval = pbdb1.execSelectSQL(lastDimIdQuery);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
            String lastDimId = bussdetseqval.getFieldValueString(0, 0);

            request.setAttribute("lastDimId", lastDimId);
            request.setAttribute("checkStatus", checkStatus);
            HttpSession session = request.getSession(false);
            writeFormulaToFile(lastDimId, actualClauseForSummarized, session);
//             writeFormulaToFile(lastDimId+"_Rlt",actualClauseForSummarizedRlt,session);

        }
        return checkStatus;
    }

    public void writeFormulaToFile(String eleId, String formula, HttpSession session) {
        if (session != null) {
            InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
            String advHtmlFileProps = "";
            String fileName = "";
            if (servletStream != null) {
                try {
                    Properties fileProps = new Properties();
                    fileProps.load(servletStream);
                    advHtmlFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                    String folderName = "SummarizedBuckets";
                    String folderPath = advHtmlFileProps + File.separator + folderName;
                    File folderDir = new File(folderPath);
                    if (!folderDir.exists()) {
                        folderDir.mkdir();
                    }
                    fileName = folderDir + File.separator + "Formula_" + eleId + ".txt";
                    //        String fileName = "c:/usr/local/cache/DimensionFormula_" + eleId + ".txt";
                    FileWriter fileWriter = new FileWriter(fileName);
                    //wrap FileWriter in BufferedWriter.
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    // append a newline formula.
                    bufferedWriter.write(formula);
                    //close files.
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }

            }
        } else {
        }
    }

    public String readFormulaFromFile(String elementID, String advHtmlFileProps) {
        String fileName = "";
        String line = null;
        String formula = "";
        Properties fileProps = new Properties();
        fileName = advHtmlFileProps + File.separator + "SummarizedBuckets" + File.separator + "Formula_" + elementID + ".txt";
        FileReader fileReader;
        try {
            fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    formula = line;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                }
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        }


        return formula;
    }
}
