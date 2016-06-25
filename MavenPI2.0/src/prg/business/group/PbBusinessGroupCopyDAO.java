package prg.business.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class PbBusinessGroupCopyDAO extends PbDb {

    public static Logger logger = Logger.getLogger(PbBusinessGroupCopyDAO.class);
    PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
    private java.sql.Statement st1;
    private java.sql.Statement st;

    public String getConnectionId(String srcGrpId) throws Exception {
        String connectionId = "";
        String conQ = "Select * from prg_grp_master where grp_id=" + srcGrpId;
        PbReturnObject conObj = execSelectSQL(conQ);
        connectionId = conObj.getFieldValueString(0, "CONNECTION_ID");
        return connectionId;
    }

    public int addBusinessGroup(String grpName, String grpDesc, String connId) {
        int bussGrpId = 0;
        String[] tableColNames = null;
        String getBussGrpId = resourceBundle.getString("getBussGrpId");
        String createBussGrp = resourceBundle.getString("createBussGrp");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(getBussGrpId);
            tableColNames = retObj.getColumnNames();
            bussGrpId = retObj.getFieldValueInt(0, tableColNames[0]);
            Object[] obj = new Object[4];
            obj[0] = bussGrpId;
            obj[1] = grpDesc;
            obj[2] = grpName;
            obj[3] = connId;
            createBussGrp = buildQuery(createBussGrp, obj);
            execModifySQL(createBussGrp);

        } catch (Exception e) {
            bussGrpId = 0;
            logger.error("Exception: ", e);
        }
        return bussGrpId;
    }

    public void copyBucket(String srcGrpId, String destGrpId, String bucketDims, String bucNames) throws Exception {
        String groupId = destGrpId;
//    String mName[]=null;
//    String bucketV[]=null;
//    String bucketDes[]=null;
//    String tId[]=null;
//    String colIdV[]=null;
//    String colTypeV[]=null;
//    String colNameV[]=null;
//    String tabNameV[]=null;
//    String bussTabIdV[]=null;
//    String bussColIdV[]=null;
        String coni = "";
//    String numberV[]=null;
        String tabId = "";
        String colId = "";
        String colType = "";
        String colName = "";
        String tabName = "";
        String bussTableId = "";
        String bussColId = "";
        String conQ = "select * from prg_grp_master where grp_id=" + srcGrpId;
        PbReturnObject conObj = execSelectSQL(conQ);
        coni = conObj.getFieldValueString(0, "CONNECTION_ID");
        String grpBussTabs = "select * from prg_grp_buss_table where grp_id=" + srcGrpId + " and buss_type='Query' and db_table_id is not null";// and buss_table_name in('"+bucNames+"')";
        PbReturnObject tabObj = execSelectSQL(grpBussTabs);
        HashMap tabDet = new HashMap();
        String bussT = "";
        for (int m = 0; m < tabObj.getRowCount(); m++) {
            tabDet.put(tabObj.getFieldValueString(m, "BUSS_TABLE_ID"), tabObj.getFieldValueString(m, "BUSS_TABLE_NAME"));
            bussT = tabObj.getFieldValueString(m, "BUSS_TABLE_ID");
        }
        String tabN = "";
        // String bucketQ="select * from prg_grp_bucket_master where grp_id="+srcGrpId;
        BusinessGroupDAO b2 = new BusinessGroupDAO();
        Connection con = b2.getBussTableConnection(bussT);
        try {
            prg.db.PbDb pbdb = new prg.db.PbDb();

            st = con.createStatement();
            st1 = con.createStatement();
            String qry="select * from prg_grp_bucket_master where grp_id=? and bucket_name in(" + bucNames + ")";
            PreparedStatement stdup2 = con.prepareStatement(qry);
            stdup2.setString(1, srcGrpId);
            PbReturnObject pbrodup2 = new PbReturnObject(stdup2.executeQuery());
            for (int m = 0; m < pbrodup2.getRowCount(); m++) {
                String bucketId = pbrodup2.getFieldValueString(m, "BUCKET_ID");
                String bucketName = pbrodup2.getFieldValueString(m, "BUCKET_NAME");
                String bucketDesc1 = pbrodup2.getFieldValueString(m, "BUCKET_DESC");
                grpBussTabs = "select * from prg_grp_buss_table where grp_id=" + srcGrpId + " and buss_type='Query' and db_table_id is not null and buss_table_name in('" + bucketName + "')";
                PbReturnObject tabs = execSelectSQL(grpBussTabs);
                String grpBuckets = "select * from prg_grp_buss_table_rlt_details where s_buss_table_id in(select buss_table_id from "
                        + " prg_grp_buss_table where grp_id=" + srcGrpId + " and buss_type='Query' and db_table_id is not null) order by p_buss_table_id";
                PbReturnObject pbro = execSelectSQL(grpBuckets);


                for (int l = 0; l < tabs.getRowCount(); l++) {
                    String bTab2 = "";
                    bTab2 = tabs.getFieldValueString(0, "BUSS_TABLE_ID");
                    String otherTabQ = "select * from prg_grp_buss_table_rlt_master where buss_table_id2=" + bTab2;
                    //////////////////////////////////////////////////////////////////////////////.println.println(" otherTabQ "+otherTabQ);
                    PbReturnObject otherTObj = execSelectSQL(otherTabQ);
                    String releOtherTable = "";
                    String buss2 = "";
                    releOtherTable = otherTObj.getFieldValueString(0, "BUSS_TABLE_ID");
                    for (int j = 0; j < otherTObj.getRowCount(); j++) {
                        if (releOtherTable.equalsIgnoreCase(otherTObj.getFieldValueString(j, "BUSS_TABLE_ID"))) {
                            buss2 = otherTObj.getFieldValueString(j, "BUSS_TABLE_ID2");
                        }
                    }
                    //////////////////////////////////////////////////////////////////////////////.println.println(" buss2- "+buss2);


                    for (int p = 0; p < pbro.getRowCount(); p++) {
                        if (tabDet.containsKey(pbro.getFieldValueString(p, "P_BUSS_TABLE_ID"))) {
                            tabN = (String) tabDet.get(pbro.getFieldValueString(p, "P_BUSS_TABLE_ID"));
                        }
                        bussTableId = pbro.getFieldValueString(p, "P_BUSS_TABLE_ID");
                        colId = pbro.getFieldValueString(p, "P_BUSS_COL_ID1");
                        if (bTab2.equalsIgnoreCase(pbro.getFieldValueString(p, "P_BUSS_TABLE_ID"))) {
                            colId = pbro.getFieldValueString(p, "P_BUSS_COL_ID1");
                        }
                    }
                    String colDetQ = "select * from prg_grp_buss_table_details where buss_column_id =" + colId;
                    //////////////////////////////////////////////////////////////////////////////.println.println(" colDetQ "+colDetQ);
                    PbReturnObject colObj = execSelectSQL(colDetQ);
                    colType = colObj.getFieldValueString(0, "COLUMN_TYPE");
                    colName = colObj.getFieldValueString(0, "COLUMN_NAME");

                    bucketName = bucketName.replace(" ", "_");
                    String bucketDesc = bucketDesc1;
                    tabId = buss2;
                    if (tabDet.containsKey(buss2)) {
                        tabName = (String) tabDet.get(buss2);
                    }
                    String grpId = groupId;
                    bussTableId = buss2;
                    bussColId = colId;
                    String connId = coni;

                    String bucketDetQ = "select * from prg_grp_bucket_details where bucket_id=?" ;
                    PreparedStatement   bucDet = con.prepareStatement(bucketDetQ);
                    bucDet.setString(1,  bucketId);
                    ResultSet bucDetRes = bucDet.executeQuery(bucketDetQ);
                    PbReturnObject bucDetObj = new PbReturnObject(bucDetRes);
                    int number = bucDetObj.getRowCount();
                    con = ProgenConnection.getInstance().getConnection();
                    // st2 = con.createStatement();
                    String conne = "select * from prg_db_master_table where table_id in(select db_table_id from prg_grp_buss_table where buss_table_id=" + tabId + ")";
                    //"select connection_id from prg_db_master_table where table_id=" + tabId;
                    PbReturnObject rs2 = pbdb.execSelectSQL(conne);
                    int connection = rs2.getFieldValueInt(0, "CONNECTION_ID");
                    connId = String.valueOf(connection);
                    BusinessGroupDAO b = new BusinessGroupDAO();
                    con = b.getBussTableConnection(bussTableId);
                    ArrayList bucketList = new ArrayList();
                    st = con.createStatement();
                    st1 = con.createStatement();
                    java.sql.Statement stdup = con.createStatement();
                    ResultSet rsdup = stdup.executeQuery("select bucket_name from prg_grp_bucket_master");
                    PbReturnObject pbrodup = new PbReturnObject(rsdup);
                    int dup = 0;
                    /*
                     * for(int i=0;i<pbrodup.getRowCount();i++){
                     * if(pbrodup.getFieldValueString(i,0).equalsIgnoreCase(bucketName)){
                     * dup=1; } }
                     */
                    if (dup == 0) {
                        ResultSet rs = st.executeQuery("select prg_grp_bucket_master_seq.nextval from dual");
                        rs.next();
                        int nextval = rs.getInt(1);
                        String Q1 = "insert into prg_grp_bucket_master (bucket_id,bucket_name,bucket_desc,grp_id) values (" + nextval + ",'" + bucketName + "','" + bucketDesc1 + "'," + grpId + ")";
                        String Q2 = "";
                        st1.addBatch(Q1);
                        String actualClause = "";
                        for (int i = 0; i < number; i++) {

                            double startLimit = Double.parseDouble(bucDetObj.getFieldValueString(i, "START_LIMIT"));
                            double endLimit = Double.parseDouble(bucDetObj.getFieldValueString(i, "END_LIMIT"));
                            String displayValue = "";
                            displayValue = bucDetObj.getFieldValueString(i, "BUCKET_DISP_VALUE");
                            int x = i + 1;
                            Q2 = "insert into prg_grp_bucket_details (bucket_col_id,bucket_disp_value,bucket_disp_cust_value,order_seq,start_limit,end_limit,bucket_id) values (prg_grp_bucket_details_seq.nextval,'" + displayValue + "','" + displayValue + "'," + x + "," + startLimit + "," + endLimit + "," + nextval + ")";
                            st1.addBatch(Q2);
                        }

                        int c[] = st1.executeBatch();
                        ArrayList bucketDetList = new ArrayList();
                        if (c.length >= 2) {
                            con = ProgenConnection.getInstance().getConnection();
                            // st3 = con.createStatement();
                            //adding to buss master table
                            PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
                            String finalQuery = "";
                            String Query = "SELECT m.BUCKET_ID, m.BUCKET_NAME, m.BUCKET_DESC,d.BUCKET_COL_ID,d.BUCKET_DISP_VALUE, d.BUCKET_DISP_CUST_VALUE,d.ORDER_SEQ,d.START_LIMIT,d.END_LIMIT FROM PRG_GRP_BUCKET_MASTER m,PRG_GRP_BUCKET_DETAILS d where m.bucket_id=d.bucket_id and m.bucket_id=" + nextval;

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
                            String addBucketdbMaster = resourceBundle.getString("addBucketdbMaster");
                            finalQuery = "";
                            Object obj4[];
                            obj4 = new Object[5];
                            obj4[0] = bucketName;
                            obj4[1] = connection;
                            obj4[2] = bucketName;
                            obj4[3] = bucketName;
                            obj4[4] = Query;

                            finalQuery = pbdb.buildQuery(addBucketdbMaster, obj4);
                            bucketDetList.add(finalQuery);
                            String addBucketdbDetails = resourceBundle.getString("addBucketdbDetails");
                            finalQuery = "";
                            int dbDetIds[] = new int[bucketColnames.length];
                            for (int i = 0; i < bucketColnames.length; i++) {
                                PbReturnObject dbseqval = pbdb.execSelectSQL("select PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval from dual");
                                int dbseqnextval = dbseqval.getFieldValueInt(0, "NEXTVAL");
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
                                bucketDetList.add(finalQuery);

                            }
                            String addBucketBussMater = resourceBundle.getString("addBucketBussMater");
                            Object obj[];
                            obj = new Object[6];
                            obj[0] = bucketName;
                            obj[1] = bucketDesc;
                            obj[2] = "Query";
                            obj[3] = 1;
                            //  obj[4]=tabId;
                            // obj[4]=Query;
                            obj[4] = grpId;
                            obj[5] = Query;

                            finalQuery = pbdb.buildQuery(addBucketBussMater, obj);
                            bucketDetList.add(finalQuery);
                            //adding to src mater table
                            String addBucketSrc = resourceBundle.getString("addBucketSrc");
                            finalQuery = "";
                            Object obj1[];
                            obj1 = new Object[4];
                            // obj1[0]=tabId;
                            obj1[0] = "Query";
                            obj1[1] = bucketName;
                            obj1[2] = connection;
                            obj1[3] = Query;


                            finalQuery = pbdb.buildQuery(addBucketSrc, obj1);
                            bucketDetList.add(finalQuery);
                            //adding to src details
                            String addBucketsrcDetails = resourceBundle.getString("addBucketsrcDetails");
                            int srcDetIds[] = new int[bucketColnames.length];
                            for (int i = 0; i < bucketColnames.length; i++) {
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
                                bucketDetList.add(finalQuery);
                            }
                            //adding to buss details
                            String addBucketBussDetails = resourceBundle.getString("addBucketBussDetails");
                            finalQuery = "";
//              int bussDetIds[] = new int[bucketColnames.length];
                            String rltstartlimit = "";
                            String rltendlimit = "";
                            ArrayList nextvalue = new ArrayList();
                            for (int i = 0; i < bucketColnames.length; i++) {
                                PbReturnObject bussdetseqval = pbdb.execSelectSQL("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                                int nextval1 = bussdetseqval.getFieldValueInt(0, "NEXTVAL");
                                // bussDetIds[i] = nextval1;
                                nextvalue.add(nextval1);
                                Object obj3[];
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
                                finalQuery = pbdb.buildQuery(addBucketBussDetails, obj3);
                                bucketDetList.add(finalQuery);
                            }
                            String addBucketBussRltMaster = resourceBundle.getString("addBucketBussRltMaster");
                            finalQuery = "";
                            Object obj6[];
                            obj6 = new Object[4];
                            obj6[0] = bussTableId;
                            obj6[1] = "1";
                            actualClause = tabName + "." + colName + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                            obj6[2] = actualClause;
                            obj6[3] = "inner";


                            finalQuery = pbdb.buildQuery(addBucketBussRltMaster, obj6);
                            bucketDetList.add(finalQuery);
                            String addBucketBussRltMasterDtl = resourceBundle.getString("addBucketBussRltMasterDtl");
                            finalQuery = "";
                            Object obj7[];
                            obj7 = new Object[7];
                            obj7[0] = bussTableId;
                            obj7[1] = bussColId;
                            obj7[2] = rltstartlimit;
                            obj7[3] = "inner";
                            obj7[4] = "BETWEEN";
                            obj7[5] = tabName + "." + colName + " BETWEEN " + bucketName + ".START_LIMIT  AND " + bucketName + ".END_LIMIT";
                            obj7[6] = rltendlimit;

                            finalQuery = pbdb.buildQuery(addBucketBussRltMasterDtl, obj7);
                            bucketDetList.add(finalQuery);
                            pbdb.executeMultiple(bucketDetList);

                            //addded by praveen to insert buckets data in some of the database tables

//                    ArrayList coldimquery = new ArrayList();
                            ArrayList multyqueries = new ArrayList();
                            Object[] bcktcolms = new Object[3];
                            bcktcolms[0] = "initcap('" + bucketName.toLowerCase().replace("_", " ") + "')";
                            bcktcolms[1] = "initcap('" + bucketName.toLowerCase().replace("_", " ") + "')";
                            bcktcolms[2] = grpId;
                            String insertbckcolms = resourceBundle.getString("insertbktgrpdims");
                            String buildinsertgrpdims = pbdb.buildQuery(insertbckcolms, bcktcolms);
                            multyqueries.add(buildinsertgrpdims);

//                    Object[] bcktabcolms = new Object[1];
//                    bcktabcolms[0] = bussTableId;
                            String insrtdimtables = resourceBundle.getString("insrtbktdimtables");
//                    String buildinsrtdimtables = pbdb.buildQuery(insrtdimtables, bcktabcolms);
                            multyqueries.add(insrtdimtables);
                            Object[] bcktabdetailcolms = new Object[1];

                            String insrtgrptabdetls = resourceBundle.getString("insrtbkttabdetails");
                            String buildgrptabdetls = "";
                            for (int i = 0; i < nextvalue.size(); i++) {
                                bcktabdetailcolms[0] = nextvalue.get(i);
                                buildgrptabdetls = pbdb.buildQuery(insrtgrptabdetls, bcktabdetailcolms);
                                multyqueries.add(buildgrptabdetls);
                            }


                            Object[] dimmemobj = new Object[2];
                            dimmemobj[0] = "initcap('" + bucketName.toLowerCase().replace("_", " ") + "')";
                            dimmemobj[1] = "initcap('" + bucketName.toLowerCase().replace("_", " ") + "')";

                            String insrtmem = resourceBundle.getString("insrtprgdimmember");
                            String buildinsertmem = pbdb.buildQuery(insrtmem, dimmemobj);
                            multyqueries.add(buildinsertmem);
                            Object[] dimmemdetails1 = new Object[1];
                            dimmemdetails1[0] = nextvalue.get(5);

                            String dimmemquery1 = resourceBundle.getString("inserdimmemdetails1");
                            String dimmemquery2 = resourceBundle.getString("inserdimmemdetails2");
                            String builddimmemquery1 = pbdb.buildQuery(dimmemquery1, dimmemdetails1);
                            String builddimmemquery2 = pbdb.buildQuery(dimmemquery2, dimmemdetails1);
                            multyqueries.add(builddimmemquery1);
                            multyqueries.add(builddimmemquery2);

                            Object[] dimrelobj = new Object[2];
                            dimrelobj[0] = "initcap('" + bucketName.toLowerCase().replace("_", " ") + "')";
                            dimrelobj[1] = "initcap('" + bucketName.toLowerCase().replace("_", " ") + "')";

                            String dimrelquery = resourceBundle.getString("insrtdimrel");
                            String builddimrelquery = pbdb.buildQuery(dimrelquery, dimrelobj);
                           
                            multyqueries.add(builddimrelquery);
                            Object[] dimreldetailsobj = new Object[1];
                            dimreldetailsobj[0] = 1;
                            String inserdimtabdetails = resourceBundle.getString("insrtreldetails");
                            String buildinserdimtabdetails = pbdb.buildQuery(inserdimtabdetails, dimreldetailsobj);
                            multyqueries.add(buildinserdimtabdetails);
                            String DBMasterQuery = "insert into PRG_DB_MASTER_TABLE (CONNECTION_ID,USER_SCHEMA,TABLE_ID,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE) values (" + connId + ",'" + "PRG_BUCKET_" + colName + "',PRG_DATABASE_MASTER_SEQ.nextval,'" + "PRG_BUCKET_" + colName + "','" + "PRG_BUCKET_" + colName + "','Query')";
                            String DbDetailsQuery = "insert into PRG_DB_MASTER_TABLE_DETAILS (COLUMN_ID,TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE) values (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,PRG_DATABASE_MASTER_SEQ.currval,'" + colName + "','" + colName + "','" + colType + "')";
                            String query1 = "select Distinct " + colName + " from " + tabName;
                            String GDBMasterQuery = "insert into PRG_GRP_BUSS_TABLE (BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_DESC,BUSS_TYPE,NO_OF_NODES,IS_PURE_QUERY,DB_TABLE_ID,GRP_ID,DB_QUERY) values (PRG_GRP_BUSS_TABLE_SEQ.nextval,'" + "PRG_BUCKET_" + colName + "','" + "PRG_BUCKET_" + colName + "','Query',1,'Y',PRG_DATABASE_MASTER_SEQ.currval," + grpId + ",'" + query1 + "')";
                            String GDSRCQuery = "insert into PRG_GRP_BUSS_TABLE_SRC (BUSS_SOURCE_ID,BUSS_TABLE_ID,CONNECTION_ID,DB_TABLE_ID,SOURCE_TYPE) values (PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval,PRG_GRP_BUSS_TABLE_SEQ.currval," + connId + ",PRG_DATABASE_MASTER_SEQ.currval,'Query')";
                            String GDSRCDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_TABLE_DTL_ID,BUSS_SRC_ID,DB_TABLE_ID,BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE) values (PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval,PRG_GRP_BUSS_TABLE_SRC_SEQ.currval,PRG_DATABASE_MASTER_SEQ.currval,PRG_GRP_BUSS_TABLE_SEQ.currval,PRG_DB_MASTER_TABLE_DTLS_SEQ.currval,'" + colName + "','" + colType + "')";
                            String GDDetailsQuery = "insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_COLUMN_ID,BUSS_TABLE_ID,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION,COLUMN_TYPE,COLUMN_DISP_NAME) values (PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval,PRG_GRP_BUSS_TABLE_SEQ.currval,'" + colName + "',PRG_DATABASE_MASTER_SEQ.currval,PRG_DB_MASTER_TABLE_DTLS_SEQ.currval,PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.currval,'N','" + colType + "','" + colName + "')";
                            String grpDimTableQuery = "insert into PRG_GRP_DIM_TABLES (DIM_TAB_ID,DIM_ID,TAB_ID) values (PRG_GRP_DIM_TABLES_SEQ.nextval,PRG_GRP_DIMENSIONS_SEQ.currval,PRG_GRP_BUSS_TABLE_SEQ.currval)";
                            String grpDimTableDetailsQuery = "insert into PRG_GRP_DIM_TAB_DETAILS (DIM_TAB_COL_ID,DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) values (PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval,PRG_GRP_DIM_TABLES_SEQ.currval,PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval,'Y','N')";
                            multyqueries.add(DBMasterQuery);
                            multyqueries.add(DbDetailsQuery);
                            multyqueries.add(GDBMasterQuery);
                            multyqueries.add(GDSRCQuery);


                            String addBucketBussRltMaster1 = resourceBundle.getString("addBucketBussRltMaster");
                            finalQuery = "";
                            Object obj61[];
                            obj61 = new Object[4];
                            obj61[0] = bussTableId;
                            obj61[1] = "1";
                            actualClause = tabName + "." + colName + " = PRG_BUCKET_" + colName + ". " + colName;
                            obj61[2] = actualClause;
                            obj61[3] = "inner";


                            finalQuery = pbdb.buildQuery(addBucketBussRltMaster1, obj61);
                            multyqueries.add(finalQuery);
                            String addBucketBussRltMasterDtl1 = resourceBundle.getString("addBucketBussRltMasterDtl");
                            finalQuery = "";
                            Object obj71[];
                            obj71 = new Object[7];
                            obj71[0] = bussTableId;
                            obj71[1] = bussColId;
                            obj71[2] = "PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval";
                            obj71[3] = "inner";
                            obj71[4] = "BETWEEN";
                            obj71[5] = tabName + "." + colName + " = PRG_BUCKET_" + colName + ". " + colName;
                            obj71[6] = "0";

                            finalQuery = pbdb.buildQuery(addBucketBussRltMasterDtl1, obj71);
                            multyqueries.add(finalQuery);
                            multyqueries.add(GDSRCDetailsQuery);
                            multyqueries.add(GDDetailsQuery);
                            dimmemobj[0] = "initcap('" + colName.toLowerCase().replace("_", " ") + "')";
                            dimmemobj[1] = "initcap('" + colName.toLowerCase().replace("_", " ") + "')";

                            buildinsertmem = pbdb.buildQuery(insrtmem, dimmemobj);
                            multyqueries.add(grpDimTableQuery);
                            multyqueries.add(grpDimTableDetailsQuery);
                            multyqueries.add(buildinsertmem);
                            //multyqueries.add(buildinserdimtabdetails);
//                    Object[] dimmemdetails2 = new Object[1];
                            //dimmemdetails2[0] =

                            //  String builddimmemquery3 = pbdb.buildQuery(dimmemquery2, dimmemdetails2);
                            //  String builddimmemquery4 = pbdb.buildQuery(dimmemquery1, dimmemdetails2);
                            String dimmemquery3 = resourceBundle.getString("inserdimmemdetails3");
                            String dimmemquery4 = resourceBundle.getString("inserdimmemdetails4");
                            String builddimmemquery3 = dimmemquery3;
                            String builddimmemquery4 = dimmemquery4;
                            multyqueries.add(builddimmemquery3);
                            multyqueries.add(builddimmemquery4);
                            dimreldetailsobj[0] = 2;
                            buildinserdimtabdetails = pbdb.buildQuery(inserdimtabdetails, dimreldetailsobj);
                            multyqueries.add(buildinserdimtabdetails);
                            pbdb.executeMultiple(multyqueries);
//                    pbdb.executeMultiple(coldimquery);
                        }
                    }
                }
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public boolean insertGrpDimForBucket(String[] dimIds, String grpId, String[] factIds) {
        boolean status = true;
        String copyBucketDim = resourceBundle.getString("copyBucketDim");
        ArrayList queries = new ArrayList();
        HashMap dimTabMap = new HashMap();
//     HashMap memIdMap = new HashMap();
        try {
            for (int i = 0; i < dimIds.length; i++) {
                Object dimObj[] = new Object[2];
                dimObj[0] = grpId;
                dimObj[1] = dimIds[i];
                String copyBucketDimQ = buildQuery(copyBucketDim, dimObj);
                queries.add(copyBucketDimQ);
                queries = insertGrpDimTables(dimIds[i], queries, dimTabMap, grpId);
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return status;
    }

    public boolean insertGrpDim(String[] dimIds, String grpId, String[] factIds) {
        //boolean result = checkAndInsertBussTables(dimIds, grpId);
//        boolean result = checkAndInsertBussTables(dimIds, grpId, factIds);
        checkAndInsertBussTables(dimIds, grpId, factIds);
        boolean flag = false;
        String insertGrpDimQuery = resourceBundle.getString("insertGrpDim");
        String finalQuery = "";
        ArrayList queries = new ArrayList();
        Object[] Obj = null;
        HashMap dimTabMap = new HashMap();
        HashMap memIdMap = new HashMap();
        try {
            Obj = new Object[3];
            for (int i = 0; i < dimIds.length; i++) {
                Obj[0] = grpId;
                Obj[1] = dimIds[i];
                Obj[2] = dimIds[i];
                finalQuery = buildQuery(insertGrpDimQuery, Obj);
                queries.add(finalQuery);
                queries = insertGrpDimTables(dimIds[i], queries, dimTabMap, grpId);
                // queries = insertGrpDimMembers(dimIds[i], queries, dimTabMap, memIdMap);
                //modified by susheela start
                queries = insertGrpDimMembers(dimIds[i], queries, dimTabMap, memIdMap, grpId);
                //modified by susheela over
                queries = insertGrpDimRel(dimIds[i], queries, memIdMap);
            }

            flag = executeMultiple(queries);
//            queries = new ArrayList();

        } catch (Exception e) {
            logger.error("Exception: ", e);
            flag = false;
        }
        return flag;
    }

    private ArrayList insertGrpDimTables(String dimId, ArrayList queries, HashMap dimTabMap, String grpId) {
        String getQryDimTablesQuery = resourceBundle.getString("getQryDimTables");
        String insertGrpDimTablesQuery = resourceBundle.getString("insertGrpDimTables");
        String finalQuery = "";
        Object[] Obj = null;
        PbReturnObject retObj = null;
        String[] tabColNames = null;
        try {
            Obj = new Object[2];
            Obj[0] = dimId;
            Obj[1] = grpId;
            finalQuery = buildQuery(getQryDimTablesQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tabColNames = retObj.getColumnNames();
            for (int index = 0; index < retObj.getRowCount(); index++) {
                int dimTabId = getSequenceNumber("select PRG_GRP_DIM_TAB_DETAILS_SEQ.NEXTVAL from dual");
                int oldDimTabId = retObj.getFieldValueInt(index, tabColNames[1]);
                Obj = new Object[3];
                Obj[0] = dimTabId;
                Obj[1] = dimId;
                Obj[2] = retObj.getFieldValueString(index, tabColNames[0]);
                //PRG_GRP_DIM_TAB_DETAILS_SEQ.NEXTVAL
                dimTabMap.put(String.valueOf(oldDimTabId), dimTabId);
                finalQuery = buildQuery(insertGrpDimTablesQuery, Obj);
                queries.add(finalQuery);
                queries = insertGrpDimTablesDetails(queries, dimTabId, oldDimTabId, grpId);
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        //return errorFlag;
        return queries;
    }

    private ArrayList insertGrpDimMembers(String dimId, ArrayList queries, HashMap dimTabMap, HashMap memIdMap, String grpId) {
        String insertGrpDimMembersQuery = resourceBundle.getString("insertGrpDimMembers");
        String getQryDimMbrsInfoQuery = resourceBundle.getString("getQryDimMbrsInfo");
        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] tabColNames = null;
        Object[] Obj = null;

        String member_name = "";
        String use_denom_table = "";
        String denom_tab_id = "";
        String denom_query = "";
        String dim_tab_id = "";
        String mem_id = "";

        try {
            Obj = new Object[1];
            Obj[0] = dimId;
            finalQuery = buildQuery(getQryDimMbrsInfoQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tabColNames = retObj.getColumnNames();
            int newMemId;
            for (int index = 0; index < retObj.getRowCount(); index++) {

                member_name = retObj.getFieldValueString(index, tabColNames[0]);
                use_denom_table = retObj.getFieldValueString(index, tabColNames[1]);
                denom_tab_id = retObj.getFieldValueString(index, tabColNames[2]);
                denom_query = retObj.getFieldValueString(index, tabColNames[3]);
                dim_tab_id = retObj.getFieldValueString(index, tabColNames[4]);
                mem_id = retObj.getFieldValueString(index, tabColNames[5]);


                newMemId = getSequenceNumber("select PRG_GRP_DIM_MEMBER_seq.nextval from dual");

                //modified by susheela start
                Obj = new Object[8];
                Obj[0] = newMemId;
                Obj[1] = member_name;
                Obj[2] = String.valueOf(dimTabMap.get(dim_tab_id));
                Obj[3] = use_denom_table;
                Obj[4] = denom_tab_id;
                Obj[5] = grpId;
                Obj[6] = denom_query;
                Obj[7] = member_name;
                //modified by susheela over

                memIdMap.put(String.valueOf(mem_id), newMemId);
                finalQuery = buildQuery(insertGrpDimMembersQuery, Obj);
                queries.add(finalQuery);
                queries = insertGrpDimMembersDtls(mem_id, queries, newMemId);

            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimRel(String dimId, ArrayList queries, HashMap memIdMap) {
        String getQryDimRelQuery = resourceBundle.getString("getQryDimRel");
        String insertGrpDimRelQuery = resourceBundle.getString("insertGrpDimRel");
        String finalQuery = "";
        Object[] Obj = null;
        PbReturnObject retObj = null;
        String[] tableColNames = null;
        String[] relIds = null;

        try {
            Obj = new Object[1];
            Obj[0] = dimId;
            finalQuery = buildQuery(getQryDimRelQuery, Obj);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery parent main is "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();

            relIds = new String[retObj.getRowCount()];
            for (int index = 0; index < retObj.getRowCount(); index++) {
                relIds[index] = retObj.getFieldValueString(index, tableColNames[3]);
                Obj = new Object[2];
                Obj[0] = dimId;
                Obj[1] = relIds[index];

                finalQuery = buildQuery(insertGrpDimRelQuery, Obj);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery parent minor is "+finalQuery);
                queries.add(finalQuery);
                queries = insertGrpDimRelDtls(dimId, queries, relIds[index], memIdMap);
            }

        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }        return queries;
        }

    private ArrayList insertGrpDimTablesDetails(ArrayList queries, int dimTabId, int oldDimTabId, String grpId) {
        String insertGrpDimTablesDetailsQuery = resourceBundle.getString("insertGrpDimTablesDetails");
        String finalQuery = "";
        Object[] Obj = null;

        try {
            Obj = new Object[3];
            Obj[0] = dimTabId;
            Obj[1] = oldDimTabId;
            Obj[2] = grpId;
            finalQuery = buildQuery(insertGrpDimTablesDetailsQuery, Obj);
            queries.add(finalQuery);

            //queries = insertGrpDimMembers(dimId, queries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimMembersDtls(String mem_id, ArrayList queries, int newMemId) {
        String insertGrpDimMembersDtlsQuery = "insert into PRG_GRP_DIM_MEMBER_DETAILS(MEM_DETAIL_ID, MEM_ID, COL_ID,"
                + " COL_TYPE) select (PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval) as mem_detail_id,& as mem_id,btdv.buss_column_id,"
                + " dmd.col_type FROM PRG_QRY_DIM_MEMBER_DETAILS dmd, prg_grp_buss_table_dtls_view btdv where dmd.col_id= btdv.db_column_id and "
                + " dmd.mem_id=&";

        //resourceBundle.getString("insertGrpDimMembersDtls");
        String finalQuery = "";
        Object[] Obj = null;

        try {
            Obj = new Object[2];
            Obj[0] = newMemId;
            Obj[1] = mem_id;
            finalQuery = buildQuery(insertGrpDimMembersDtlsQuery, Obj);
            queries.add(finalQuery);

            //queries = insertGrpDimRel(dimId, queries, mem_id);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return queries;
    }

    private ArrayList insertGrpDimRelDtls(String dimId, ArrayList queries, String relId, HashMap memIdMap) {
        String getQryDimRelDtlsQuery = resourceBundle.getString("getQryDimRelDtls");
        String finalQuery = "";
        String insertGrpDimRelDtlsQuery = resourceBundle.getString("insertGrpDimRelDtls");
        Object[] Obj = null;
        PbReturnObject retObj = null;
        String[] tableColNames = null;

        try {
            Obj = new Object[2];
            Obj[0] = dimId;
            Obj[1] = relId;
            finalQuery = buildQuery(getQryDimRelDtlsQuery, Obj);
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery child main is "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            tableColNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {

                Obj = new Object[3];
                Obj[0] = memIdMap.get(retObj.getFieldValueString(i, tableColNames[1]));
                Obj[1] = relId;
                Obj[2] = retObj.getFieldValueString(i, tableColNames[1]);
                // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery minor is "+finalQuery);
                finalQuery = buildQuery(insertGrpDimRelDtlsQuery, Obj);

                queries.add(finalQuery);
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }        return queries;
        }

    public boolean checkAndInsertBussTables(String[] dimIds, String grpId, String[] factIds) {
        boolean flag = false;
        String getBussTabIdsByDimQuery = resourceBundle.getString("getBussTabIdsByDim");
        String getBussTabIdsByDBTabIdsQuery = resourceBundle.getString("getBussTabIdsByDBTabIds");
        String finalQuery = "";
        PbReturnObject retObj = null;
        Object[] Obj = null;
        Vector bussTabVector = new Vector();
        String[] tabcolNames = null;

        String[] tableId = null;
        String[] noOfNodes = null;
        try {
            Obj = new Object[2];
            for (int i = 0; i < dimIds.length; i++) {
                Obj[0] = dimIds[i];
                Obj[1] = grpId;
                finalQuery = buildQuery(getBussTabIdsByDimQuery, Obj);
                ////////////////////////////////////////////////////////////////////////////////////.println.println(" finalQuery "+finalQuery);
                retObj = execSelectSQL(finalQuery);
                tabcolNames = retObj.getColumnNames();
                for (int j = 0; j < retObj.getRowCount(); j++) {
                    if ((retObj.getFieldValueString(j, tabcolNames[0]) == null) || ("".equalsIgnoreCase(retObj.getFieldValueString(j, tabcolNames[0])))) {
                        if ((!bussTabVector.contains(retObj.getFieldValueString(j, tabcolNames[1])))) {
                            bussTabVector.add(retObj.getFieldValueString(j, tabcolNames[1]));
                        }
                    }
                }
            }

            for (int i = 0; i < factIds.length; i++) {
                Obj[0] = factIds[i];
                Obj[1] = grpId;
                finalQuery = buildQuery(getBussTabIdsByDBTabIdsQuery, Obj);
                retObj = execSelectSQL(finalQuery);
                tabcolNames = retObj.getColumnNames();

                for (int j = 0; j < retObj.getRowCount(); j++) {
                    if ((retObj.getFieldValueString(j, tabcolNames[0]) == null) || ("".equalsIgnoreCase(retObj.getFieldValueString(j, tabcolNames[0])))) {
                        if ((!bussTabVector.contains(retObj.getFieldValueString(j, tabcolNames[1])))) {
                            bussTabVector.add(retObj.getFieldValueString(j, tabcolNames[1]));
                        }
                    }
                }
            }

            tableId = new String[bussTabVector.size()];
            noOfNodes = new String[bussTabVector.size()];

            for (int i = 0; i < bussTabVector.size(); i++) {
                tableId[i] = String.valueOf(bussTabVector.get(i));
                noOfNodes[i] = "1";
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(tableId[i]+" ----------------- ");
            }
            StringBuffer tempBuffer = new StringBuffer();
            flag = insertBusinessTable(tableId, noOfNodes, grpId, tempBuffer);

        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
            flag = false;
        } catch (Exception e) {
            logger.error("Exception: ", e);
            flag = false;
        }       return flag;
        }

    public boolean insertBusinessTable(String[] tableId, String[] noOfNodes, String grpId, StringBuffer newAddedBussTabIds) {
        boolean flag = false;
        ArrayList updateList = new ArrayList();
        //insert into PRG_GRP_BUSS_TABLE(BUSS_TABLE_ID, BUSS_TABLE_NAME, BUSS_DESC, BUSS_TYPE, NO_OF_NODES, DB_TABLE_ID, GRP_ID)
        //          SELECT (PRG_GRP_BUSS_TABLE_SiEQ.nextval), TABLE_NAME,TABLE_NAME,TABLE_TYPE,& as \"NO_OF_NODES\",TABLE_ID,& as \"GRP_ID\" FROM PRG_DB_MASTER_TABLE where TABLE_ID=&
        String insertBussTableQuery = resourceBundle.getString("insertBussTable");
        String finalQuery = "";
        ArrayList queries = new ArrayList();
        ArrayList queries1 = new ArrayList();
        Object[] Obj = null;
        String groupedtabIds = "";
        try {
            Obj = new Object[3];
            for (int i = 0; i < tableId.length; i++) {

                Obj[0] = noOfNodes[i];
                Obj[1] = grpId;
                Obj[2] = tableId[i];
                finalQuery = buildQuery(insertBussTableQuery, Obj);
                queries.add(finalQuery);
                queries = insertGrpBussTableSrc(tableId[i], queries, grpId);
            }
//	            for (int index = 0; index < queries.size(); index++) {
//	                ////////////////////////////////////////////////////////////////////////////////////.println.println(queries.get(index));
//	            }
            flag = executeMultiple(queries);
            ////////////////////////////////////////////////////////////////////////////////////.println.println("flag after inserting insertBusinessTable is " + flag);
            for (int i = 0; i < tableId.length; i++) {
                groupedtabIds = groupedtabIds + "," + tableId[i];
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("groupedtabIds is "+groupedtabIds);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("(groupedtabIds.equalsIgnoreCase() is "+(groupedtabIds.equalsIgnoreCase("")));
            if (!(groupedtabIds.equalsIgnoreCase(""))) {
                groupedtabIds = groupedtabIds.substring(1);
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" after groupedtabIds is "+groupedtabIds);
            queries1 = insertBusinessRelations(groupedtabIds, queries1, grpId);
            flag = executeMultiple(queries1);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("flag after inserting relationships is " + flag);
            PbReturnObject retObj = new PbReturnObject();
            String getCurrentBussTabId = resourceBundle.getString("getCurrentBussTabId");
            int currentBussTabId = 0;
            retObj = execSelectSQL(getCurrentBussTabId);
            currentBussTabId = retObj.getFieldValueInt(0, 0);
            currentBussTabId -= tableId.length - 1;
            for (int j = 0; j < tableId.length; j++) {
                newAddedBussTabIds.append("" + (currentBussTabId + j));
                if (j + 1 != tableId.length) {
                    newAddedBussTabIds.append("-");
                }
            }
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
            flag = false;
        }catch (Exception e) {
            logger.error("Exception: ", e);
            flag = false;

        }
        //added by susheela start 15-12-09
        //added by susheela start 15-12-09
        String updateBussFlagQ = "update prg_grp_buss_table set TAB_USE_NEXT_LEVEL='Y' where grp_id=&";
        Object grpObj[] = new Object[1];
        grpObj[0] = grpId;
        String finupdateBussFlagQ = buildQuery(updateBussFlagQ, grpObj);
        updateList.add(finupdateBussFlagQ);
        //added by susheela over 15-12-09
        try {
            executeMultiple(updateList);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        //added by susheela over 15-12-09
        return flag;
    }

    public ArrayList insertGrpBussTableSrc(String tableId, ArrayList queries, String grpId) {
        // INSERT INTO PRG_GRP_BUSS_TABLE_SRC  (BUSS_SOURCE_ID,BUSS_TABLE_ID ,CONNECTION_ID ,DB_TABLE_ID,SOURCE_TYPE)
        // SELECT (PRG_GRP_BUSS_TABLE_SRC_seq.nextval),(PRG_GRP_BUSS_TABLE_SEQ.currval),connection_id,table_id ,table_type  FROM prg_db_master_table WHERE table_id= &
        String insertBussTableQuery = resourceBundle.getString("insertBussSrcTable");
        String finalQuery = "";
        Object[] Obj = null;

        try {
            Obj = new Object[1];
            Obj[0] = tableId;
            finalQuery = buildQuery(insertBussTableQuery, Obj);
            queries.add(finalQuery);
            queries = insertGrpBussTableSrcDetails(tableId, queries, grpId);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        //return errorFlag;
        return queries;
    }

    public ArrayList insertGrpBussTableSrcDetails(String tableId, ArrayList queries, String grpId) {
        //insert into PRG_GRP_BUSS_TABLE_SRC_DETAILS (BUSS_SRC_TABLE_DTL_ID,BUSS_SRC_ID,DB_TABLE_ID, BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE)
        //SELECT (PRG_GRP_BUSS_TBL_SRC_DTLS_SEQ.NEXTVAL),(PRG_GRP_BUSS_TABLE_SRC_seq.currval),table_id,(PRG_GRP_BUSS_TABLE_SEQ.currval),column_id, table_col_name ,
        //col_type FROM prg_db_master_table_details WHERE table_id=&
        String insertBussSrcTableDetailsQuery = resourceBundle.getString("insertBussSrcTableDetails");
        String finalQuery = "";
        Object[] Obj = null;
        try {
            Obj = new Object[1];
            Obj[0] = tableId;
            // Obj[1] = tableId;
            finalQuery = buildQuery(insertBussSrcTableDetailsQuery, Obj);
            queries.add(finalQuery);

            queries = insertGrpBussTableDetails(tableId, queries, grpId);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return queries;
    }

    public ArrayList insertGrpBussTableDetails(String tableId, ArrayList queries, String grpId) {

        // insert into PRG_GRP_BUSS_TABLE_DETAILS (BUSS_COLUMN_ID, BUSS_TABLE_ID, COLUMN_NAME, DB_TABLE_ID, DB_COLUMN_ID, BUSS_SRC_TABLE_DTL_ID,
        // IS_UNION, COLUMN_TYPE, ACTUAL_COL_FORMULA,ACTUAL_COL_FORMULA1, DEFAULT_AGGREGATION, BUCKET_ATTACHED, COLUMN_DISP_NAME)
        // SELECT (PRG_GRP_BUSS_TABLE_DETAILS_seq.nextval),(PRG_GRP_BUSS_TABLE_SEQ.currval),TABLE_COL_NAME,DB_TABLE_ID,COLUMN_ID,
        // (PRG_GRP_BUSS_TABLE_DETAILS_seq.currval) , 'N',COL_TYPE,'','','','',TABLE_COL_NAME FROM PRG_DB_MASTER_TABLE_DETAILS where DB_TABLE_ID=&
        String insertBussTableDetailsQuery = resourceBundle.getString("insertBussTableDetails");
        String finalQuery = "";
        Object[] Obj = null;
        try {
            Obj = new Object[2];
            Obj[0] = tableId;
            Obj[1] = grpId;
            finalQuery = buildQuery(insertBussTableDetailsQuery, Obj);
            queries.add(finalQuery);
            //queries = insertBusinessRelations(tableId, queries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return queries;
    }

    public ArrayList insertBusinessRelations(String dbTableIds, ArrayList queries, String grpId) {
        //boolean flag = false;
        PbReturnObject retObj = null;
        //ArrayList queriesList = new ArrayList();
        String getDbMasterRelationsQuery = resourceBundle.getString("getDbMasterRelations");
        String insertBussMasterRelationsQuery = resourceBundle.getString("insertBussMasterRelations");
        String insertBussDetailsRelationsQuery = resourceBundle.getString("insertBussDetailsRelations");
        String RELATIONSHIP_ID = "";
        String finalQuery = "";
        Object[] Obj = null;
        String[] tableColumns = null;
        if (dbTableIds == null || "".equalsIgnoreCase(dbTableIds)) {
            dbTableIds = "''";
        }
        try {
            Obj = new Object[2];
            Obj[0] = dbTableIds;
            Obj[1] = dbTableIds;
            finalQuery = buildQuery(getDbMasterRelationsQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            tableColumns = retObj.getColumnNames();

            for (int outerIndex = 0; outerIndex < retObj.getRowCount(); outerIndex++) {
                RELATIONSHIP_ID = retObj.getFieldValueString(outerIndex, tableColumns[0]);
                Obj = new Object[1];
                Obj[0] = RELATIONSHIP_ID;
                finalQuery = buildQuery(insertBussMasterRelationsQuery, Obj);
                queries.add(finalQuery);

                Obj = new Object[5];
                Obj[0] = RELATIONSHIP_ID;
                Obj[1] = grpId;
                Obj[2] = grpId;
                Obj[3] = grpId;
                Obj[4] = grpId;
                finalQuery = buildQuery(insertBussDetailsRelationsQuery, Obj);
                queries.add(finalQuery);
            }
            //flag = executeMultiple(queriesList);
            return queries;
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
            return queries;
        }catch (Exception exp) {
            logger.error("Exception: ", exp);
            return queries;
        }    }

    public boolean checkAndInsertBussTablesForFacts(String[] tableIds, String grpId, String[] noOfNodes) {
        boolean flag = false;
        String getBussTabIdsByDBTabIdsQuery = resourceBundle.getString("getBussTabIdsByDBTabIds");
        String finalQuery = "";
        PbReturnObject retObj = null;
        Object[] Obj = null;
        Vector bussTabVector = new Vector();
        String[] tabcolNames = null;
        String[] tableId = null;
        try {
            for (int i = 0; i < tableIds.length; i++) {
                bussTabVector.add(tableIds[i]);
            }
            Obj = new Object[2];
            for (int i = 0; i < tableIds.length; i++) {
                Obj[0] = tableIds[i];
                Obj[1] = grpId;
                finalQuery = buildQuery(getBussTabIdsByDBTabIdsQuery, Obj);
                retObj = execSelectSQL(finalQuery);
                tabcolNames = retObj.getColumnNames();

                for (int j = 0; j < retObj.getRowCount(); j++) {
                    if ((retObj.getFieldValueString(j, tabcolNames[0]) == null) || ("".equalsIgnoreCase(retObj.getFieldValueString(j, tabcolNames[0])))) {
                        if ((!bussTabVector.contains(retObj.getFieldValueString(j, tabcolNames[1])))) {
                            bussTabVector.add(retObj.getFieldValueString(j, tabcolNames[1]));
                        }
                    }
                }
            }
            tableId = new String[bussTabVector.size()];
            noOfNodes = new String[bussTabVector.size()];
            for (int i = 0; i < bussTabVector.size(); i++) {
                tableId[i] = String.valueOf(bussTabVector.get(i));
                noOfNodes[i] = "1";
            }
            StringBuffer tempBuffer = new StringBuffer();
            flag = insertBusinessTable(tableId, noOfNodes, grpId, tempBuffer);

        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
            flag = false;
        }catch (Exception e) {
            logger.error("Exception: ", e);
            flag = false;
        }        return flag;
        }

    public void updateBussTableDetails(String srcGrpId, String newGrpId) throws Exception {
        ArrayList updateList = new ArrayList();
        String sourceBussQ = "select * from prg_grp_buss_table where grp_id=" + srcGrpId;
        String destBussQ = "select * from prg_grp_buss_table where grp_id=" + newGrpId;
        PbReturnObject sourceObj = execSelectSQL(sourceBussQ);
        PbReturnObject destBussObj = execSelectSQL(destBussQ);
//       String actualColFormula="";
//       String actualColFormula1="";
//       String bucket="";
//       String colDispDesc="";
        String dbColId = "";
        HashMap srcDetails = new HashMap();
        String bussTabId = "";
        HashMap colDetails = new HashMap();

        for (int m = 0; m < sourceObj.getRowCount(); m++) {
            bussTabId = sourceObj.getFieldValueString(m, "BUSS_TABLE_ID");
            String dbTab = sourceObj.getFieldValueString(m, "DB_TABLE_ID");
            if (srcDetails.containsKey(dbTab)) {
                if (colDetails.containsKey(dbTab)) {
                    colDetails = (HashMap) srcDetails.get(dbTab);
                }

            } else {
                colDetails = new HashMap();
            }
            String colDetQ = "select * from prg_grp_buss_table_details where buss_table_id=" + bussTabId;
            PbReturnObject colObj = execSelectSQL(colDetQ);
            for (int y = 0; y < colObj.getRowCount(); y++) {
                dbColId = colObj.getFieldValueString(y, "DB_COLUMN_ID");
                ArrayList colDet = new ArrayList();
                colDet.add(0, colObj.getFieldValueString(y, "COLUMN_DISPLAY_DESC"));
                colDet.add(1, colObj.getFieldValueString(y, "ACTUAL_COL_FORMULA"));
                colDet.add(2, colObj.getFieldValueString(y, "ACTUAL_COL_FORMULA1"));
                colDet.add(3, colObj.getFieldValueString(y, "BUCKET_ATTACHED"));
                colDet.add(4, colObj.getFieldValueString(y, "DEFAULT_AGGREGATION"));
                colDetails.put(dbColId, colDet);
            }

            srcDetails.put(dbTab, colDetails);
        }

        String destDbColId = "";
        String updatQ = "update PRG_GRP_BUSS_TABLE_DETAILS set bucket_attached='&', actual_col_formula='&', "
                + " actual_col_formula1='&',column_display_desc='&' where buss_table_id=& and db_column_id=&";
        String addBMaster = "insert into PRG_GRP_BUCKET_MASTER(BUCKET_ID,BUCKET_NAME,BUCKET_DESC,DB_TABLE_ID,DB_COLUMN_ID,"
                + " CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON,GRP_ID) select PRG_GRP_BUCKET_MASTER_SEQ.nextval as BUCKET_ID,"
                + " BUCKET_NAME,BUCKET_DESC,DB_TABLE_ID,DB_COLUMN_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON,& as grp_id "
                + " from PRG_GRP_BUCKET_MASTER where db_column_id=& and db_table_id=& and grp_id=&)";
//       String addBucketDet="insert into PRG_GRP_BUCKET_DETAILS(BUCKET_COL_ID,BUCKET_DISP_VALUE,BUCKET_DISP_CUST_VALUE," +
//               " ORDER_SEQ,START_LIMIT,END_LIMIT,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON,BUCKET_ID) select PRG_GRP_BUCKET_DETAILS_SEQ.nextval," +
//               " BUCKET_DISP_VALUE,BUCKET_DISP_CUST_VALUE,ORDER_SEQ,START_LIMIT,END_LIMIT,CREATED_BY,CREATED_ON,UPDATED_BY," +
//               " UPDATED_ON,PRG_GRP_BUCKET_MASTER_SEQ.currval from PRG_GRP_BUCKET_DETAILS where bucket_id=&";

        String colDisplay = "";
        String actFormula = "";
        String actFormula1 = "";
        String bucketV = "";
        String def_aggr = "";
        ArrayList bucketInsert = new ArrayList();
        for (int m = 0; m < destBussObj.getRowCount(); m++) {
            String dbTabId = destBussObj.getFieldValueString(m, "DB_TABLE_ID");
            String bussT = destBussObj.getFieldValueString(m, "BUSS_TABLE_ID");
            String colDetQ = "select * from prg_grp_buss_table_details where buss_table_id=" + bussT;
            PbReturnObject colObj = execSelectSQL(colDetQ);
            HashMap srcDet = new HashMap();
            if (srcDetails.containsKey(dbTabId)) {
                srcDet = (HashMap) srcDetails.get(dbTabId);

            }
            for (int n = 0; n < colObj.getRowCount(); n++) {
                destDbColId = colObj.getFieldValueString(n, "DB_COLUMN_ID");
                String destDbTab = colObj.getFieldValueString(n, "DB_TABLE_ID");
                ArrayList det = new ArrayList();
                if (srcDet.containsKey(destDbColId)) {
                    det = (ArrayList) srcDet.get(destDbColId);
                    colDisplay = det.get(0).toString();
                    actFormula = det.get(1).toString();
                    actFormula1 = det.get(2).toString();
                    bucketV = det.get(3).toString();
                    def_aggr = det.get(4).toString();
                }
                Object val[] = new Object[6];
                val[0] = bucketV;
                val[1] = actFormula;
                val[2] = actFormula1;
                val[3] = colDisplay;
                val[4] = bussT;
                val[5] = destDbColId;
                String finupdatQ = buildQuery(updatQ, val);
                updateList.add(finupdatQ);
                /*
                 * if(!bucketV.equalsIgnoreCase("")) { Object inserB[]=new
                 * Object[4]; inserB[0]=newGrpId; inserB[1]=destDbColId;
                 * inserB[2]=destDbTab; inserB[3]=srcGrpId; String
                 * finaddBMaster=buildQuery(addBMaster,inserB);
                 * bucketInsert.add(finaddBMaster); Object bbucketV[]=new
                 * Object[1]; bbucketV[0]=bucketV; String
                 * finaddBucketDet=buildQuery(addBucketDet,bbucketV);
                 * bucketInsert.add(finaddBucketDet); }
                 */
            }

        }
        try {
            executeMultiple(updateList);
            //executeMultiple(bucketInsert);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void copyFormula(String srcGrpId, String destGrpId) throws Exception {
        ArrayList list = new ArrayList();
        String connId = "";
        String conQ = "select connection_id from prg_grp_master where grp_id=" + destGrpId;
        PbReturnObject conObj = execSelectSQL(conQ);
        connId = conObj.getFieldValueString(0, "CONNECTION_ID");

        String bussTabQ = "select * from prg_grp_buss_table where buss_table_id in(select buss_table_id from "
                + " prg_grp_buss_table where grp_id=" + srcGrpId + " and buss_type not in('Query') minus select tab_id from "
                + " prg_grp_dim_tables where dim_id in( select distinct dim_id from prg_grp_dimensions where grp_id=" + srcGrpId + "))";

        String destGrpQ = "select * from prg_grp_buss_table where buss_table_id in(select buss_table_id from "
                + " prg_grp_buss_table where grp_id=" + destGrpId + " and buss_type not in('Query') minus select tab_id from "
                + " prg_grp_dim_tables where dim_id in( select distinct dim_id from prg_grp_dimensions where grp_id=" + destGrpId + "))";

        HashMap srcTab = new HashMap();
        HashMap destTab = new HashMap();
        PbReturnObject tabObj = execSelectSQL(bussTabQ);
        PbReturnObject destObj = execSelectSQL(destGrpQ);
        ArrayList al = new ArrayList();
        for (int m = 0; m < tabObj.getRowCount(); m++) {
            al = new ArrayList();
            al.add(0, tabObj.getFieldValueString(m, "BUSS_TABLE_ID"));
            al.add(1, tabObj.getFieldValueString(m, "BUSS_TABLE_NAME"));
            srcTab.put(tabObj.getFieldValueString(m, "DB_TABLE_ID"), al);
        }

        for (int m = 0; m < destObj.getRowCount(); m++) {
            al = new ArrayList();
            al.add(0, destObj.getFieldValueString(m, "BUSS_TABLE_ID"));
            al.add(1, destObj.getFieldValueString(m, "BUSS_TABLE_NAME"));
            destTab.put(destObj.getFieldValueString(m, "DB_TABLE_ID"), al);
        }
        String bussDetColQ = "";
        String bussTabId = "";
        String colId = "";
        String dbTabId = "";
        String addFormulasrcDetails = resourceBundle.getString("addFormulasrcDetails");
        String colType = "";
        String colName = "";
        String destTabId = "";
        String column_formula = "";
        String agregationtype = "";
        String finalQuery = "";
        String dependenteleids = "";
        String dbColId = "";


        for (int m = 0; m < tabObj.getRowCount(); m++) {
            bussTabId = tabObj.getFieldValueString(m, "BUSS_TABLE_ID");
            dbTabId = tabObj.getFieldValueString(m, "DB_TABLE_ID");
            bussDetColQ = "select * from prg_grp_buss_table_details where buss_table_id=" + bussTabId + " and column_type in('calculated','summarised')";
            //////////////////////////////////////////////////////////////////////////////.println.println(" bussDetColQ - "+bussDetColQ);
            PbReturnObject colObj = execSelectSQL(bussDetColQ);
            for (int n = 0; n < colObj.getRowCount(); n++) {
                colType = colObj.getFieldValueString(n, "COLUMN_TYPE");
                colName = colObj.getFieldValueString(n, "COLUMN_NAME");
                String refElements = colObj.getFieldValueString(n, "REFFERED_ELEMENTS");
                //////////////////////////////////////////////////////////////////////////////.println.println(" refElements "+refElements);
                ArrayList al2 = new ArrayList();
                //////////////////////////////////////////////////////////////////////////////.println.println(" destTab "+destTab);
                agregationtype = colObj.getFieldValueString(n, "DEFAULT_AGGREGATION");
                column_formula = colObj.getFieldValueString(n, "ACTUAL_COL_FORMULA");
                dbColId = "";//colObj.getFieldValueString(n,"DB_COLUMN_ID");
                String refEle[] = refElements.split(",");

                String ref = refElements;//"'"+refElements+"'";
                // ref=ref.replace(",","','");
                if (destTab.containsKey(dbTabId)) {
                    al2 = (ArrayList) destTab.get(dbTabId);
                    destTabId = al2.get(0).toString();
                }

                dependenteleids = refElements;
                String getOriginalDbCol = "select * from prg_grp_buss_table_details where buss_column_id in(" + ref + ")";
                PbReturnObject oriObj = execSelectSQL(getOriginalDbCol);
                for (int s = 0; s < oriObj.getRowCount(); s++) {
                    dbColId = dbColId + "," + oriObj.getFieldValueString(s, "DB_COLUMN_ID");
                }

                if (dbColId.length() > 0) {
                    dbColId = dbColId.substring(1);
                }

                String destRefElementsQ = "select * from prg_grp_buss_table_details btd,prg_grp_buss_table gbt where db_column_id "
                        + " in(" + dbColId + ") and gbt.buss_table_id=btd.buss_table_id and gbt.grp_id=" + destGrpId;
                //////////////////////////////////////////////////////////////////////////////.println.println(" destRefElementsQ - "+destRefElementsQ);
                PbReturnObject destRefObj = execSelectSQL(destRefElementsQ);

                String destRefElements = "";
                for (int p = 0; p < destRefObj.getRowCount(); p++) {
                    destRefElements = destRefElements + "," + destRefObj.getFieldValueString(p, "BUSS_COLUMN_ID");
                }
                if (destRefElements.length() > 0) {
                    destRefElements = destRefElements.substring(1);
                }

                if (colType.equalsIgnoreCase("calculated")) {
                    String doubleExist = " SELECT Distinct d.BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE_DETAILS d ,PRG_GRP_BUSS_TABLE m where d.buss_column_id in(" + destRefElements + ") and m.BUSS_TABLE_NAME!='Calculated Facts' and d.buss_table_id=m.buss_table_id";
                    // String doubleExist=" SELECT  BUSS_TABLE_ID  FROM PRG_GRP_BUSS_TABLE_DETAILS where buss_column_id in("+dependenteleids+")";
                    PbReturnObject pbrodoubleExist = execSelectSQL(doubleExist);
                    String sql = "select buss_source_id, buss_table_id  from prg_grp_buss_table_src  where buss_table_id in (" + pbrodoubleExist.getFieldValueInt(0, 0) + ")";
                    //////////////////////////////////////////////////////////////////////////////.println.println("sql---. "+sql);

                    PbReturnObject pbroexist = execSelectSQL(sql);
                    //////////////////////////////////////////////////////////////////////////////.println.println(" pbroexist- "+pbroexist.getRowCount());
                    int srcdetnextval = getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                    Object obj2[] = new Object[7];
                    obj2[0] = srcdetnextval;
                    obj2[1] = pbroexist.getFieldValueInt(0, 0);
                    obj2[2] = "0";
                    obj2[3] = pbroexist.getFieldValueInt(0, 1);
                    obj2[4] = colName;
                    obj2[5] = "calculated";
                    obj2[6] = "0";

                    finalQuery = buildQuery(addFormulasrcDetails, obj2);
                    //////////////////////////////////////////////////////////////////////////////.println.println(" finalQuery srcDet  "+finalQuery);
                    list.add(finalQuery);
                    //////////////////////////////////////////////////////////////////////////////.println.println("finalQuery---" + finalQuery);
                    String addFormulaBussDetails = resourceBundle.getString("addFormulaBussDetails");
                    int seqaddFormulaBussDetails = getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                    Object obj3[] = new Object[15];
                    obj3[0] = seqaddFormulaBussDetails;
                    obj3[1] = pbroexist.getFieldValueInt(0, 1);
                    obj3[2] = colName;
                    obj3[3] = "0";
                    obj3[4] = srcdetnextval;
                    obj3[5] = "calculated";
                    obj3[6] = "0";
                    obj3[7] = colName;
                    obj3[8] = "0";
                    obj3[9] = "N";
                    obj3[10] = column_formula;
                    obj3[11] = agregationtype;
                    obj3[12] = colName;
                    obj3[13] = destRefElements;
                    obj3[14] = "Y";
                    finalQuery = buildQuery(addFormulaBussDetails, obj3);
                    //////////////////////////////////////////////////////////////////////////////.println.println(" in if finalQuery- "+finalQuery);
                    list.add(finalQuery);

                } else if (colType.equalsIgnoreCase("summarised")) {

                    String addFormulaBussMater = resourceBundle.getString("addFormulaBussMater");
                    int seqaddFormulaBussMater = getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                    Object obj[];
                    obj = new Object[8];
                    obj[0] = seqaddFormulaBussMater;
                    obj[1] = "Calculated Facts";
                    obj[2] = "Calculated Facts";
                    obj[3] = "Table";
                    obj[4] = "1";
                    obj[5] = "";
                    obj[6] = "";
                    obj[7] = destGrpId;
                    finalQuery = buildQuery(addFormulaBussMater, obj);
                    list.add(finalQuery);
                    //////////////////////////////////////////////////////////////////////////////.println.println("finalQuery---1 " + finalQuery);
                    String addFormulaSrc = resourceBundle.getString("addFormulaSrc");
                    int seqaddFormulaSrc = getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                    Object obj1[];
                    obj1 = new Object[7];
                    obj1[0] = seqaddFormulaSrc;
                    obj1[1] = seqaddFormulaBussMater;
                    obj1[2] = "0";
                    obj1[3] = "Table";
                    obj1[4] = "";
                    obj1[5] = connId;
                    obj1[6] = "";
                    finalQuery = buildQuery(addFormulaSrc, obj1);
                    list.add(finalQuery);
                    //////////////////////////////////////////////////////////////////////////////.println.println("finalQuery--- 2 " + finalQuery);

                    //add grp buss src details
                    int srcdetnextval = getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                    addFormulasrcDetails = resourceBundle.getString("addFormulasrcDetails");
                    Object obj2[] = new Object[7];
                    obj2[0] = srcdetnextval;
                    obj2[1] = seqaddFormulaSrc;
                    obj2[2] = "0";
                    obj2[3] = seqaddFormulaBussMater;
                    obj2[4] = colName;
                    obj2[5] = "summarised";
                    obj2[6] = "0";

                    finalQuery = buildQuery(addFormulasrcDetails, obj2);
                    //////////////////////////////////////////////////////////////////////////////.println.println(" finalQuery for src det "+ finalQuery);
                    list.add(finalQuery);
                    //////////////////////////////////////////////////////////////////////////////.println.println("finalQuery---3 " + finalQuery);
                    String addFormulaBussDetails = resourceBundle.getString("addFormulaBussDetails");
                    int seqaddFormulaBussDetails = getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                    Object obj3[] = new Object[15];
                    obj3[0] = seqaddFormulaBussDetails;
                    obj3[1] = seqaddFormulaBussMater;
                    obj3[2] = colName;
                    obj3[3] = "0";
                    obj3[4] = srcdetnextval;
                    obj3[5] = "summarised";
                    obj3[6] = "0";
                    obj3[7] = colName;
                    obj3[8] = "0";
                    obj3[9] = "N";
                    obj3[10] = column_formula;
                    obj3[11] = agregationtype;
                    obj3[12] = colName;
                    obj3[13] = destRefElements;
                    obj3[14] = "Y";
                    finalQuery = buildQuery(addFormulaBussDetails, obj3);
                    //////////////////////////////////////////////////////////////////////////////.println.println(" in else finalQuery "+finalQuery);
                    list.add(finalQuery);

                }

            }
        }

        try {
            executeMultiple(list);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public String checkRelationForTables(String tabID) {
        String queryS = "select * from PRG_DB_TABLE_RLT_MASTER where table_id=" + tabID + " union select * from PRG_DB_TABLE_RLT_MASTER where table_id2=" + tabID;
        PbReturnObject pbReturnObject = new PbReturnObject();
        StringBuilder buffer = new StringBuilder();
        buffer.append("");
        try {

            pbReturnObject = super.execSelectSQL(queryS);
            for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
                buffer.append("<table width='100%'><tr><td>").append(pbReturnObject.getFieldValueString(i, "ACTUAL_CLAUSE")).append("</td></tr></table>");

            }
        } 
        catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception e) {
            logger.error("Exception: ", e);
        }        return buffer.toString();
        }
    }
