/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import com.progen.userlayer.db.PbUserLayerEditDAO;
import com.progen.userlayer.db.UserLayerDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class SaveBucketAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(SaveBucketAction.class);
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbBussGrpResBundleSqlServer();
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
        return map;
    }

    /**
     * Action called on Add button click
     */
    public ActionForward add(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        // TODO: implement add method
        return mapping.findForward(SUCCESS);
    }

    /**
     * Action called on Edit button click
     */
    public ActionForward edit(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        // TODO: implement edit method
        return mapping.findForward(SUCCESS);
    }

    /**
     * Action called on Delete button click
     */
    public ActionForward delete(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        // TODO:implement delete method
        return mapping.findForward(SUCCESS);
    }

    /*
     * And your JSP would have the following format for submit buttons:
     *
     * <html:form action="/test"> <html:submit property="method"> <bean:message
     * key="button.add"/> </html:submit> <html:submit property="method">
     * <bean:message key="button.edit"/> </html:submit> <html:submit
     * property="method"> <bean:message key="button.delete"/> </html:submit>
     * </html:form>
     */
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {

        Connection dataConnection = null;
        //Connection metaConnection;
        Statement st, st1, st2, st3, stGetIdent;
        ResultSet rs, rs2;
        boolean checkStatus = false;

        SaveBucketsBD bucketBd = new SaveBucketsBD();
        checkStatus = bucketBd.insertBucket(request, response);
        String tempBuck = request.getParameter("tempBuck");
        String BucketType = request.getParameter("BucketType");

        //for Dim Migration By Nazneen
        if (checkStatus == true && tempBuck.equalsIgnoreCase("yes")) {
            String lastDimId = (String) request.getAttribute("lastDimId");
            String folderId = request.getParameter("folderId");
            String bussTableId = request.getParameter("bussTableId");
            String grpId = request.getParameter("grpId");

            String dimVals[] = new String[1];
            String roleIdValues[] = new String[1];
            dimVals[0] = lastDimId;
            roleIdValues[0] = folderId;
            //Dimensiom Migration
            PbUserLayerEditDAO userLayerDAO = new PbUserLayerEditDAO();
            try {
                UserLayerDAO dao = new UserLayerDAO();
                dao.truncateDimValues();
                dao.insertNewDimValues();
                userLayerDAO.migrateToBussRoleNew(grpId, roleIdValues, dimVals);
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
            //Dimension Publish
            UserLayerDAO userLayerDAO1 = new UserLayerDAO();
            boolean result = userLayerDAO1.partialPublishDimentions(lastDimId, bussTableId, folderId, BucketType);

        }
        //end of code by Nazneen


        /*
         * prg.db.PbDb pbdb = new prg.db.PbDb(); String measureName =
         * request.getParameter("measure"); String bucketName1 =
         * request.getParameter("bucket").replace("_", " "); String bucketDesc1
         * = request.getParameter("bdesc").replace("_", " "); String bucketName
         * = request.getParameter("bucket").toUpperCase(); bucketName =
         * bucketName.trim().replace(" ", "_"); String bucketDesc =
         * request.getParameter("bdesc").toUpperCase(); String tabId =
         * request.getParameter("tabId"); String colId =
         * request.getParameter("colId"); String colType =
         * request.getParameter("colType"); String colName =
         * request.getParameter("colName"); String tabName =
         * request.getParameter("tabName"); String grpId =
         * request.getParameter("grpId"); String bussTableId =
         * request.getParameter("bussTableId"); String bussColId =
         * request.getParameter("bussColId"); String connId =
         * request.getParameter("connid");
         *
         *
         *
         *
         * int number = Integer.parseInt(request.getParameter("number")); //
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("measureName
         * " + measureName + " bucketDesc " + bucketDesc); //con =
         * ProgenConnection.getInstance().getConnection();
         *
         *
         *
         * // st2 = con.createStatement(); dataConnection =
         * ProgenConnection.getInstance().getConnectionByTable(tabId);
         *
         * ConnectionDAO conDAO = new ConnectionDAO(); int connection =
         * conDAO.getConnectionByTable(tabId).getConnId(); String
         * dataConnectionType = conDAO.getConnectionByTable(tabId).getDbType();
         * connId = String.valueOf(connection);
         *
         * // String conne = "select connection_id from prg_db_master_table
         * where table_id=" + tabId; //
         * //////////////////////////////////////////////////////////////////////////////.println.println("conne------------------"+conne);
         * // PbReturnObject rs2 = pbdb.execSelectSQL(conne); // int connection
         * = rs2.getFieldValueInt(0, "CONNECTION_ID"); // connId =
         * String.valueOf(connection); //
         * //////////////////////////////////////////////////////////////////////////////.println.println("connId---"+connId);
         * //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connection---"
         * + connection); // BusinessGroupDAO b = new BusinessGroupDAO(); // con
         * = b.getBussTableConnection(bussTableId);
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("con"+con);
         * ArrayList bucketList = new ArrayList(); st =
         * dataConnection.createStatement(); st1 =
         * dataConnection.createStatement(); Statement stdup =
         * dataConnection.createStatement();
         *
         * ResultSet rsdup = stdup.executeQuery("select bucket_name from
         * prg_grp_bucket_master where upper(bucket_name) = upper('" +
         * bucketName + "')"); PbReturnObject pbrodup = new
         * PbReturnObject(rsdup);
         *
         * int dup = 0; if (rsdup.next()) { dup = 1; }
         *
         * rsdup.close(); rsdup = null; stdup.close(); stdup = null;
         *
         * if (dup == 0) { rs = null; String Q1 = ""; String Q2 = ""; int
         * nextval = 0; if
         * (dataConnectionType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
         * // rs = st.executeQuery("select prg_grp_bucket_master_seq.nextval
         * from dual"); Q1 = "insert into prg_grp_bucket_master
         * (bucket_name,bucket_desc,grp_id) values ('" + bucketName + "','" +
         * bucketDesc1.replaceAll("_", " ") + "'," + grpId + ")";
         *
         * } else { rs = st.executeQuery("select
         * prg_grp_bucket_master_seq.nextval from dual"); rs.next(); nextval =
         * rs.getInt(1); Q1 = "insert into prg_grp_bucket_master
         * (bucket_id,bucket_name,bucket_desc,grp_id) values (" + nextval + ",'"
         * + bucketName + "','" + bucketDesc1.replaceAll("_", " ") + "'," +
         * grpId + ")";
         *
         * }
         *
         *
         * ////.println("Q1 is " + Q1); st1.addBatch(Q1); String actualClause =
         * ""; for (int i = 1; i <= number; i++) { //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("REQUEST--->"
         * + request.getParameter("sl" + i).equals("")); if
         * ((request.getParameter("dv" + i) != null) &&
         * (request.getParameter("sl" + i) != null) &&
         * (request.getParameter("el" + i) != null) &&
         * (request.getParameter("dv" + i).equals("") != true) &&
         * (request.getParameter("sl" + i).equals("") != true) &&
         * (request.getParameter("el" + i).equals("") != true)) { String
         * displayValue = request.getParameter("dv" + i);
         *
         * double startLimit = Double.parseDouble(request.getParameter("sl" +
         * i));
         *
         * double endLimit = Double.parseDouble(request.getParameter("el" + i));
         * // actualClause+=tabName+"."+measureName+" between "+startLimit+" AND
         * "+endLimit; // if(i+1<=number){ // actualClause+=" OR " ; // } if
         * (dataConnectionType.equals(ProgenConnection.SQL_SERVER)) { Q2 =
         * "insert into prg_grp_bucket_details
         * (bucket_disp_value,bucket_disp_cust_value,order_seq,start_limit,end_limit,bucket_id)
         * values ('" + displayValue + "','" + displayValue + "'," + i + "," +
         * startLimit + "," + endLimit +
         * ",ident_current('prg_grp_bucket_master') )";
         *
         * } else { Q2 = "insert into prg_grp_bucket_details
         * (bucket_col_id,bucket_disp_value,bucket_disp_cust_value,order_seq,start_limit,end_limit,bucket_id)
         * values (prg_grp_bucket_details_seq.nextval,'" + displayValue + "','"
         * + displayValue + "'," + i + "," + startLimit + "," + endLimit + "," +
         * nextval + ")";
         *
         * }
         * ////.println("Q2 is " + Q2); st1.addBatch(Q2); } }
         *
         *
         * int c[] = st1.executeBatch();
         *
         * if (dataConnectionType.equalsIgnoreCase(ProgenConnection.SQL_SERVER))
         * { stGetIdent = dataConnection.createStatement(); rs =
         * stGetIdent.executeQuery("select
         * ident_current('prg_grp_bucket_master')"); rs.next(); nextval =
         * rs.getInt(1); }
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------------------"+c.length);
         * if (c.length >= 2) { ArrayList bucketDetList = new ArrayList(); //con
         * = ProgenConnection.getInstance().getConnection(); // st3 =
         * con.createStatement(); //adding to buss master table //
         * PbBussGrpResourceBundle resourceBundle = new
         * PbBussGrpResourceBundle();
         *
         * String finalQuery = ""; String Query = ""; Query = "SELECT
         * m.BUCKET_ID, m.BUCKET_NAME,
         * m.BUCKET_DESC,d.BUCKET_COL_ID,d.BUCKET_DISP_VALUE,
         * d.BUCKET_DISP_CUST_VALUE,d.ORDER_SEQ,d.START_LIMIT,d.END_LIMIT FROM
         * PRG_GRP_BUCKET_MASTER m,PRG_GRP_BUCKET_DETAILS d where
         * m.bucket_id=d.bucket_id and m.bucket_id=" + nextval;
         *
         *
         * String bucketColnames[] = new String[9]; String bucketColTypes[] =
         * new String[9]; String bucketColLength[] = new String[9];
         * bucketColnames[0] = "BUCKET_ID"; bucketColTypes[0] = "NUMBER";
         * bucketColLength[0] = "22"; bucketColnames[1] = "BUCKET_NAME";
         * bucketColTypes[1] = "VARCHAR2"; bucketColLength[1] = "100";
         * bucketColnames[2] = "BUCKET_DESC"; bucketColTypes[2] = "VARCHAR2";
         * bucketColLength[2] = "255"; // bucketColnames[3]="TABLE_ID"; //
         * bucketColTypes[3]="NUMBER"; // bucketColnames[4]="COLUMN_ID"; //
         * bucketColTypes[4]="NUMBER"; bucketColnames[3] = "BUCKET_COL_ID";
         * bucketColTypes[3] = "NUMBER"; bucketColLength[3] = "22";
         * bucketColnames[4] = "BUCKET_DISP_VALUE"; bucketColTypes[4] =
         * "VARCHAR2"; bucketColLength[4] = "255"; bucketColnames[5] =
         * "BUCKET_DISP_CUST_VALUE"; bucketColTypes[5] = "VARCHAR2";
         * bucketColLength[5] = "255"; bucketColnames[6] = "ORDER_SEQ";
         * bucketColTypes[6] = "NUMBER"; bucketColLength[6] = "22";
         * bucketColnames[7] = "START_LIMIT"; bucketColTypes[7] = "NUMBER";
         * bucketColLength[7] = "22"; bucketColnames[8] = "END_LIMIT";
         * bucketColTypes[8] = "NUMBER"; bucketColLength[8] = "22"; String
         * addBucketdbMaster =
         * getResourceBundle().getString("addBucketdbMaster"); finalQuery = "";
         * Object obj4[]; obj4 = new Object[5]; obj4[0] = bucketName; obj4[1] =
         * connection; obj4[2] = bucketName; obj4[3] = bucketName; obj4[4] =
         * Query;
         *
         * finalQuery = pbdb.buildQuery(addBucketdbMaster, obj4); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final
         * addBucketdbMater------>" + finalQuery);
         * bucketDetList.add(finalQuery); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
         * String addBucketdbDetails =
         * getResourceBundle().getString("addBucketdbDetails"); finalQuery = "";
         * int dbDetIds[] = new int[bucketColnames.length]; for (int i = 0; i <
         * bucketColnames.length; i++) { if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { // PbReturnObject dbseqval = pbdb.execSelectSQL("select
         * PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval from dual");
         *
         * // int dbseqnextval = dbseqval.getFieldValueInt(0, "NEXTVAL");
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---dbseqnextval-----"+dbseqnextval);
         * // dbDetIds[i] = dbseqnextval; Object obj5[]; obj5 = new Object[5];
         * // obj5[0] = dbseqnextval; obj5[0] = bucketColnames[i]; obj5[1] =
         * bucketColnames[i]; obj5[2] = bucketColTypes[i]; obj5[3] =
         * bucketColLength[i]; obj5[4] = "N"; finalQuery =
         * pbdb.buildQuery(addBucketdbDetails, obj5); } else { PbReturnObject
         * dbseqval = pbdb.execSelectSQL("select
         * PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval from dual");
         *
         * int dbseqnextval = dbseqval.getFieldValueInt(0, "NEXTVAL");
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---dbseqnextval-----"+dbseqnextval);
         * dbDetIds[i] = dbseqnextval; Object obj5[]; obj5 = new Object[6];
         * obj5[0] = dbseqnextval; obj5[1] = bucketColnames[i]; obj5[2] =
         * bucketColnames[i]; obj5[3] = bucketColTypes[i]; obj5[4] =
         * bucketColLength[i]; obj5[5] = "N"; finalQuery =
         * pbdb.buildQuery(addBucketdbDetails, obj5); }
         *
         * //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final
         * addBucketBussMater db--details---->" + finalQuery);
         * bucketDetList.add(finalQuery);
         *
         * }
         * //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
         * String addBucketBussMater =
         * getResourceBundle().getString("addBucketBussMater"); Object obj[];
         * obj = new Object[6]; obj[0] = bucketName; obj[1] =
         * bucketDesc.replace("_", " "); obj[2] = "Query"; obj[3] = 1; //
         * obj[4]=tabId; // obj[4]=Query; obj[4] = grpId; obj[5] = Query;
         *
         * finalQuery = pbdb.buildQuery(addBucketBussMater, obj); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final
         * addBucketBussMater------>" + finalQuery);
         * bucketDetList.add(finalQuery); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
         * //adding to src mater table String addBucketSrc =
         * getResourceBundle().getString("addBucketSrc"); finalQuery = "";
         * Object obj1[]; obj1 = new Object[4]; // obj1[0]=tabId; obj1[0] =
         * "Query"; obj1[1] = bucketName; obj1[2] = connection; obj1[3] = Query;
         *
         *
         * finalQuery = pbdb.buildQuery(addBucketSrc, obj1); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final
         * addBucketsrc master------>" + finalQuery);
         * bucketDetList.add(finalQuery); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
         * //adding to src details String addBucketsrcDetails =
         * getResourceBundle().getString("addBucketsrcDetails"); int srcDetIds[]
         * = new int[bucketColnames.length]; for (int i = 0; i <
         * bucketColnames.length; i++) { if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { // PbReturnObject srcseqval = pbdb.execSelectSQL("select
         * PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
         *
         * // int nextval1 = srcseqval.getFieldValueInt(0, "NEXTVAL"); //
         * srcDetIds[i] = nextval1; finalQuery = ""; Object obj2[]; obj2 = new
         * Object[3];
         *
         * // obj2[0] = nextval1; //obj2[1]=bussColId;
         *
         * obj2[0] = bucketColnames[i]; obj2[1] = bucketColTypes[i]; obj2[2] =
         * dbDetIds[i];//db dolumn id finalQuery =
         * pbdb.buildQuery(addBucketsrcDetails, obj2); } else { PbReturnObject
         * srcseqval = pbdb.execSelectSQL("select
         * PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
         *
         * int nextval1 = srcseqval.getFieldValueInt(0, "NEXTVAL"); srcDetIds[i]
         * = nextval1; finalQuery = ""; Object obj2[]; obj2 = new Object[4];
         *
         * obj2[0] = nextval1; //obj2[1]=bussColId;
         *
         * obj2[1] = bucketColnames[i]; obj2[2] = bucketColTypes[i]; obj2[3] =
         * dbDetIds[i];//db dolumn id finalQuery =
         * pbdb.buildQuery(addBucketsrcDetails, obj2); }
         *
         * //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final
         * addBucketsrc details------>" + i + "--------------" + finalQuery);
         * bucketDetList.add(finalQuery); } //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
         * //adding to buss details String addBucketBussDetails =
         * getResourceBundle().getString("addBucketBussDetails"); finalQuery =
         * ""; int bussDetIds[] = new int[bucketColnames.length]; String
         * rltstartlimit = ""; String rltendlimit = ""; ArrayList nextvalue =
         * new ArrayList(); int nextval1 = 0; if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { PbReturnObject bussdetseqval = pbdb.execSelectSQL("select
         * ident_current('PRG_GRP_BUSS_TABLE_DETAILS')"); nextval1 =
         * bussdetseqval.getFieldValueInt(0, "NEXTVAL") + 1;
         *
         * }
         * for (int i = 0; i < bucketColnames.length; i++) {
         * nextvalue.add(nextval1 + i); Object obj3[] = null; if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { obj3 = new Object[6]; obj3[0] = bucketColnames[i]; obj3[1] =
         * srcDetIds[i]; obj3[2] = bucketColTypes[i]; obj3[3] = nextval; obj3[4]
         * = bucketColnames[i]; obj3[5] = dbDetIds[i];//db dolumn id
         *
         * } else { PbReturnObject bussdetseqval = pbdb.execSelectSQL("select
         * PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual"); nextval1 =
         * bussdetseqval.getFieldValueInt(0, "NEXTVAL"); // bussDetIds[i] =
         * nextval1; nextvalue.add(nextval1); obj3 = new Object[7]; obj3[0] =
         * nextval1; obj3[1] = bucketColnames[i]; //error and discrepancy if
         * (bucketColnames[i].equals("START_LIMIT")) { rltstartlimit =
         * String.valueOf(nextval1); } else if
         * (bucketColnames[i].equals("END_LIMIT")) { rltendlimit =
         * String.valueOf(nextval1); obj3[2] = srcDetIds[i]; obj3[3] =
         * bucketColTypes[i]; obj3[4] = nextval; obj3[5] = bucketColnames[i];
         * obj3[6] = dbDetIds[i];//db dolumn id } }
         *
         * finalQuery = pbdb.buildQuery(addBucketBussDetails, obj3);
         * bucketDetList.add(finalQuery); } //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
         * String addBucketBussRltMaster =
         * getResourceBundle().getString("addBucketBussRltMaster"); finalQuery =
         * ""; Object obj6[]; obj6 = new Object[4]; obj6[0] = bussTableId;
         * obj6[1] = "1"; actualClause = tabName + "." + colName + " BETWEEN " +
         * bucketName + ".START_LIMIT AND " + bucketName + ".END_LIMIT"; obj6[2]
         * = actualClause; obj6[3] = "inner";
         *
         *
         * finalQuery = pbdb.buildQuery(addBucketBussRltMaster, obj6); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final
         * addBucket buss rlt master------>" + finalQuery);
         * bucketDetList.add(finalQuery); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
         * String addBucketBussRltMasterDtl =
         * getResourceBundle().getString("addBucketBussRltMasterDtl");
         * finalQuery = ""; Object obj7[] = null; if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { obj7 = new Object[7]; obj7[0] = bussTableId; obj7[1] = bussColId;
         * obj7[2] =
         * "ident_current('PRG_GRP_BUSS_TABLE_DETAILS')";//PRG_GRP_BUSS_TABLE_DETAILS
         * obj7[3] = "inner"; obj7[4] = "BETWEEN"; obj7[5] = tabName + "." +
         * colName + " BETWEEN " + bucketName + ".START_LIMIT AND " + bucketName
         * + ".END_LIMIT"; obj7[6] =
         * "ident_current('PRG_GRP_BUSS_TABLE_DETAILS')"; } else { obj7 = new
         * Object[7]; obj7[0] = bussTableId; obj7[1] = bussColId; obj7[2] =
         * rltstartlimit; obj7[3] = "inner"; obj7[4] = "BETWEEN"; obj7[5] =
         * tabName + "." + colName + " BETWEEN " + bucketName + ".START_LIMIT
         * AND " + bucketName + ".END_LIMIT"; obj7[6] = rltendlimit; }
         *
         *
         * finalQuery = pbdb.buildQuery(addBucketBussRltMasterDtl, obj7);
         * bucketDetList.add(finalQuery); //////.println("bucketDetList\t" +
         * bucketDetList); for ( int i=0; i<bucketDetList.size(); i++ )
         * ////.println("Qry++ "+bucketDetList.get(i));
         * pbdb.executeMultiple(bucketDetList);
         *
         * //addded by praveen to insert buckets data in some of the database
         * tables
         *
         * // ArrayList coldimquery = new ArrayList(); ArrayList multyqueries =
         * new ArrayList();
         *
         * Object[] bcktcolms = new Object[3]; if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { bcktcolms[0] = bucketName.toLowerCase().replaceAll("_", " ");
         * ////.println("bcktcolms[0]\t" + bcktcolms[0]); bcktcolms[1] =
         * bucketName.toLowerCase().replaceAll("_", " "); bcktcolms[2] = grpId;
         * } else { bcktcolms[0] = "initcap('" +
         * bucketName.toLowerCase().replaceAll("_", " ") + "')"; bcktcolms[1] =
         * "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
         * bcktcolms[2] = grpId; }
         *
         * String insertbckcolms =
         * getResourceBundle().getString("insertbktgrpdims");
         *
         * String buildinsertgrpdims = pbdb.buildQuery(insertbckcolms,
         * bcktcolms);
         * //////////////////////////////////////////////////////////////////////////////.println.println("buildinsertgrpdims||||||||||||"
         * + buildinsertgrpdims);
         *
         * multyqueries.add(buildinsertgrpdims);
         *
         * // Object[] bcktabcolms = new Object[1]; // bcktabcolms[0] =
         * bussTableId; String insrtdimtables =
         * getResourceBundle().getString("insrtbktdimtables"); // String
         * buildinsrtdimtables = pbdb.buildQuery(insrtdimtables, bcktabcolms);
         * //////////////////////////////////////////////////////////////////////////////.println.println("insrtdimtables----------"
         * + insrtdimtables); multyqueries.add(insrtdimtables); Object[]
         * bcktabdetailcolms = new Object[1]; String insrtgrptabdetls =
         * getResourceBundle().getString("insrtbkttabdetails"); String
         * buildgrptabdetls = ""; for (int i = 0; i < nextvalue.size(); i++) {
         * bcktabdetailcolms[0] = nextvalue.get(i); buildgrptabdetls =
         * pbdb.buildQuery(insrtgrptabdetls, bcktabdetailcolms);
         * multyqueries.add(buildgrptabdetls);
         * //////////////////////////////////////////////////////////////////////////////.println.println("insrtgrptabdetls============"
         * + buildgrptabdetls); } Object[] dimmemobj = new Object[2]; String
         * insrtmem = getResourceBundle().getString("insrtprgdimmember"); if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { dimmemobj[0] = bucketName.toLowerCase().replaceAll("_", " ");
         * dimmemobj[1] = bucketName.toLowerCase().replaceAll("_", " "); } else
         * { dimmemobj[0] = "initcap('" +
         * bucketName.toLowerCase().replaceAll("_", " ") + "')"; dimmemobj[1] =
         * "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')"; }
         *
         *
         *
         * String buildinsertmem = pbdb.buildQuery(insrtmem, dimmemobj);
         *
         * //////////////////////////////////////////////////////////////////////////////.println.println("buildinsertmem
         * query is" + buildinsertmem);
         *
         * multyqueries.add(buildinsertmem); Object[] dimmemdetails1 = new
         * Object[1]; dimmemdetails1[0] = nextvalue.get(5); String dimmemquery1
         * = getResourceBundle().getString("inserdimmemdetails1"); String
         * dimmemquery2 = getResourceBundle().getString("inserdimmemdetails2");
         * String builddimmemquery1 = pbdb.buildQuery(dimmemquery1,
         * dimmemdetails1); String builddimmemquery2 =
         * pbdb.buildQuery(dimmemquery2, dimmemdetails1);
         * multyqueries.add(builddimmemquery1);
         * multyqueries.add(builddimmemquery2); Object[] dimrelobj = new
         * Object[2];
         *
         * if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { dimrelobj[0] = bucketName.toLowerCase().replaceAll("_", " ");
         * dimrelobj[1] = bucketName.toLowerCase().replaceAll("_", " "); } else
         * { dimmemobj[0] = "initcap('" +
         * bucketName.toLowerCase().replaceAll("_", " ") + "')"; dimmemobj[1] =
         * "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')"; }
         * // dimrelobj[0] = "initcap('" +
         * bucketName.toLowerCase().replaceAll("_", " ") + "')"; // dimrelobj[1]
         * = "initcap('" + bucketName.toLowerCase().replaceAll("_", " ") + "')";
         * String dimrelquery = getResourceBundle().getString("insrtdimrel");
         * String builddimrelquery = pbdb.buildQuery(dimrelquery, dimrelobj);
         * ////////////////////////////////////////////////////////////////////////////////.println.println("builddimrelquery"
         * + builddimrelquery); multyqueries.add(builddimrelquery); Object[]
         * dimreldetailsobj = new Object[1]; dimreldetailsobj[0] = 1; String
         * inserdimtabdetails =
         * getResourceBundle().getString("insrtreldetails"); String
         * buildinserdimtabdetails = pbdb.buildQuery(inserdimtabdetails,
         * dimreldetailsobj);
         * ////////////////////////////////////////////////////////////////////////////////.println.println("buildinserdimtabdetails"
         * + buildinserdimtabdetails);
         * multyqueries.add(buildinserdimtabdetails); String DBMasterQuery = "";
         * String DbDetailsQuery = ""; String query1 = ""; String GDBMasterQuery
         * = ""; String GDSRCQuery = ""; String GDSRCDetailsQuery = ""; String
         * GDDetailsQuery = ""; String grpDimTableQuery = ""; String
         * grpDimTableDetailsQuery = "";
         *
         * if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { DBMasterQuery = "insert into PRG_DB_MASTER_TABLE
         * (CONNECTION_ID,USER_SCHEMA,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE) values
         * (" + connId + ",'" + "PRG_BUCKET_" + colName + "','" + "PRG_BUCKET_"
         * + colName + "','" + "PRG_BUCKET_" + colName + "','Query')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("DBMasterQuery-->"
         * + DBMasterQuery); DbDetailsQuery = "insert into
         * PRG_DB_MASTER_TABLE_DETAILS
         * (TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE) values
         * (ident_current('PRG_DB_MASTER_TABLE'),'" + colName + "','" + colName
         * + "','" + colType + "')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("DbDetailsQuery-->"
         * + DbDetailsQuery); query1 = "select Distinct " + colName + " from " +
         * tabName; GDBMasterQuery = "insert into PRG_GRP_BUSS_TABLE
         * (BUSS_TABLE_NAME,BUSS_DESC,BUSS_TYPE,NO_OF_NODES,IS_PURE_QUERY,DB_TABLE_ID,GRP_ID,DB_QUERY)
         * values ('" + "PRG_BUCKET_" + colName + "','" + "PRG BUCKET " +
         * colName + "','Query',1,'Y',ident_current('PRG_DB_MASTER_TABLE')," +
         * grpId + ",'" + query1 + "')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("");
         * GDSRCQuery = "insert into PRG_GRP_BUSS_TABLE_SRC
         * (BUSS_TABLE_ID,CONNECTION_ID,DB_TABLE_ID,SOURCE_TYPE) values
         * (ident_current('PRG_GRP_BUSS_TABLE')," + connId +
         * ",ident_current('PRG_DB_MASTER_TABLE'),'Query')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery"
         * + GDSRCQuery); GDSRCDetailsQuery = "insert into
         * PRG_GRP_BUSS_TABLE_SRC_DETAILS
         * (BUSS_SRC_ID,DB_TABLE_ID,BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE)
         * values
         * (ident_current('PRG_GRP_BUSS_TABLE_SRC'),ident_current('PRG_DB_MASTER_TABLE'),ident_current('PRG_GRP_BUSS_TABLE'),ident_current('PRG_DB_MASTER_TABLE_DETAILS'),'"
         * + colName + "','" + colType + "')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCDetailsQuery"
         * + GDSRCDetailsQuery); GDDetailsQuery = "insert into
         * PRG_GRP_BUSS_TABLE_DETAILS
         * (BUSS_TABLE_ID,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION,COLUMN_TYPE,COLUMN_DISP_NAME)
         * values (ident_current('PRG_GRP_BUSS_TABLE'),'" + colName +
         * "',ident_current('PRG_DB_MASTER_TABLE'),ident_current('PRG_DB_MASTER_TABLE_DETAILS'),ident_current('PRG_GRP_BUSS_TABLE_DETAILS'),'N','"
         * + colType + "','" + colName + "')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery"
         * + GDDetailsQuery); grpDimTableQuery = "insert into PRG_GRP_DIM_TABLES
         * (DIM_ID,TAB_ID) values
         * (ident_current('PRG_GRP_DIMENSIONS'),ident_current('PRG_GRP_BUSS_TABLE'))";
         * //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery"
         * + grpDimTableQuery); grpDimTableDetailsQuery = "insert into
         * PRG_GRP_DIM_TAB_DETAILS (DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY)
         * values
         * (ident_current('PRG_GRP_DIM_TABLES'),ident_current('PRG_GRP_BUSS_TABLE_DETAILS'),'Y','N')";
         * //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("grpDimTableDetailsQuery"
         * + grpDimTableDetailsQuery);
         *
         * } else { DBMasterQuery = "insert into PRG_DB_MASTER_TABLE
         * (CONNECTION_ID,USER_SCHEMA,TABLE_ID,TABLE_NAME,TABLE_ALIAS,TABLE_TYPE)
         * values (" + connId + ",'" + "PRG_BUCKET_" + colName +
         * "',PRG_DATABASE_MASTER_SEQ.nextval,'" + "PRG_BUCKET_" + colName +
         * "','" + "PRG_BUCKET_" + colName + "','Query')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("DBMasterQuery-->"
         * + DBMasterQuery); DbDetailsQuery = "insert into
         * PRG_DB_MASTER_TABLE_DETAILS
         * (COLUMN_ID,TABLE_ID,TABLE_COL_NAME,TABLE_COL_CUST_NAME,COL_TYPE)
         * values
         * (PRG_DB_MASTER_TABLE_DTLS_SEQ.nextval,PRG_DATABASE_MASTER_SEQ.currval,'"
         * + colName + "','" + colName + "','" + colType + "')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("DbDetailsQuery-->"
         * + DbDetailsQuery); query1 = "select Distinct " + colName + " from " +
         * tabName; GDBMasterQuery = "insert into PRG_GRP_BUSS_TABLE
         * (BUSS_TABLE_ID,BUSS_TABLE_NAME,BUSS_DESC,BUSS_TYPE,NO_OF_NODES,IS_PURE_QUERY,DB_TABLE_ID,GRP_ID,DB_QUERY)
         * values (PRG_GRP_BUSS_TABLE_SEQ.nextval,'" + "PRG_BUCKET_" + colName +
         * "','" + "PRG BUCKET " + colName +
         * "','Query',1,'Y',PRG_DATABASE_MASTER_SEQ.currval," + grpId + ",'" +
         * query1 + "')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("");
         * GDSRCQuery = "insert into PRG_GRP_BUSS_TABLE_SRC
         * (BUSS_SOURCE_ID,BUSS_TABLE_ID,CONNECTION_ID,DB_TABLE_ID,SOURCE_TYPE)
         * values
         * (PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval,PRG_GRP_BUSS_TABLE_SEQ.currval,"
         * + connId + ",PRG_DATABASE_MASTER_SEQ.currval,'Query')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery"
         * + GDSRCQuery); GDSRCDetailsQuery = "insert into
         * PRG_GRP_BUSS_TABLE_SRC_DETAILS
         * (BUSS_SRC_TABLE_DTL_ID,BUSS_SRC_ID,DB_TABLE_ID,BUSS_TABLE_ID,DB_COLUMN_ID,COLUMN_ALIAS,COLUMN_TYPE)
         * values
         * (PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval,PRG_GRP_BUSS_TABLE_SRC_SEQ.currval,PRG_DATABASE_MASTER_SEQ.currval,PRG_GRP_BUSS_TABLE_SEQ.currval,PRG_DB_MASTER_TABLE_DTLS_SEQ.currval,'"
         * + colName + "','" + colType + "')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCDetailsQuery"
         * + GDSRCDetailsQuery); GDDetailsQuery = "insert into
         * PRG_GRP_BUSS_TABLE_DETAILS
         * (BUSS_COLUMN_ID,BUSS_TABLE_ID,COLUMN_NAME,DB_TABLE_ID,DB_COLUMN_ID,BUSS_SRC_TABLE_DTL_ID,IS_UNION,COLUMN_TYPE,COLUMN_DISP_NAME)
         * values
         * (PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval,PRG_GRP_BUSS_TABLE_SEQ.currval,'"
         * + colName +
         * "',PRG_DATABASE_MASTER_SEQ.currval,PRG_DB_MASTER_TABLE_DTLS_SEQ.currval,PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.currval,'N','"
         * + colType + "','" + colName + "')"; //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery"
         * + GDDetailsQuery); grpDimTableQuery = "insert into PRG_GRP_DIM_TABLES
         * (DIM_TAB_ID,DIM_ID,TAB_ID) values
         * (PRG_GRP_DIM_TABLES_SEQ.nextval,PRG_GRP_DIMENSIONS_SEQ.currval,PRG_GRP_BUSS_TABLE_SEQ.currval)";
         * //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("GDSRCQuery"
         * + grpDimTableQuery); grpDimTableDetailsQuery = "insert into
         * PRG_GRP_DIM_TAB_DETAILS
         * (DIM_TAB_COL_ID,DIM_TAB_ID,COL_ID,IS_AVAILABLE,IS_PK_KEY) values
         * (PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval,PRG_GRP_DIM_TABLES_SEQ.currval,PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval,'Y','N')";
         * //
         * //////////////////////////////////////////////////////////////////////////////////.println.println("grpDimTableDetailsQuery"
         * + grpDimTableDetailsQuery);
         *
         *
         * }
         * multyqueries.add(DBMasterQuery); multyqueries.add(DbDetailsQuery);
         * multyqueries.add(GDBMasterQuery); multyqueries.add(GDSRCQuery);
         * String addBucketBussRltMaster1 =
         * getResourceBundle().getString("addBucketBussRltMaster"); finalQuery =
         * ""; Object obj61[]; obj61 = new Object[4]; obj61[0] = bussTableId;
         * obj61[1] = "1"; actualClause = tabName + "." + colName + " =
         * PRG_BUCKET_" + colName + ". " + colName; obj61[2] = actualClause;
         * obj61[3] = "inner"; finalQuery =
         * pbdb.buildQuery(addBucketBussRltMaster1, obj61); //
         * //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final
         * addBucket buss rlt master------>" + finalQuery);
         * multyqueries.add(finalQuery); //
         * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------------------------------------------");
         * String addBucketBussRltMasterDtl1 =
         * getResourceBundle().getString("addBucketBussRltMasterDtl");
         * finalQuery = ""; Object obj71[]; obj71 = new Object[7]; obj71[0] =
         * bussTableId; obj71[1] = bussColId; if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { obj71[2] = "ident_current('PRG_GRP_BUSS_TABLE_DETAILS')"; } else {
         * obj71[2] = "PRG_GRP_BUSS_TABLE_DETAILS_SEQ.currval"; }
         *
         * obj71[3] = "inner"; obj71[4] = "BETWEEN"; obj71[5] = tabName + "." +
         * colName + " = PRG_BUCKET_" + colName + ". " + colName; obj71[6] =
         * "0";
         *
         * finalQuery = pbdb.buildQuery(addBucketBussRltMasterDtl1, obj71);
         * multyqueries.add(finalQuery); multyqueries.add(GDSRCDetailsQuery);
         * multyqueries.add(GDDetailsQuery); if
         * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
         * { dimmemobj[0] = colName.toLowerCase().replaceAll("_", " ");
         * dimmemobj[1] = colName.toLowerCase().replaceAll("_", " ");
         *
         * } else { dimmemobj[0] = "initcap('" +
         * colName.toLowerCase().replaceAll("_", " ") + "')"; dimmemobj[1] =
         * "initcap('" + colName.toLowerCase().replaceAll("_", " ") + "')";
         *
         * }
         *
         * buildinsertmem = pbdb.buildQuery(insrtmem, dimmemobj);
         *
         * ////////////////////////////////////////////////////////////////////////////////.println.println("buildinsertmem
         * query is" + buildinsertmem); multyqueries.add(grpDimTableQuery);
         * multyqueries.add(grpDimTableDetailsQuery);
         * multyqueries.add(buildinsertmem);
         * //multyqueries.add(buildinserdimtabdetails); Object[] dimmemdetails2
         * = new Object[1]; //dimmemdetails2[0] =
         *
         * // String builddimmemquery3 = pbdb.buildQuery(dimmemquery2,
         * dimmemdetails2); // String builddimmemquery4 =
         * pbdb.buildQuery(dimmemquery1, dimmemdetails2); String dimmemquery3 =
         * getResourceBundle().getString("inserdimmemdetails3"); String
         * dimmemquery4 = getResourceBundle().getString("inserdimmemdetails4");
         * String builddimmemquery3 = dimmemquery3; String builddimmemquery4 =
         * dimmemquery4; multyqueries.add(builddimmemquery3);
         * multyqueries.add(builddimmemquery4);
         *
         *
         * dimreldetailsobj[0] = 2;
         *
         * buildinserdimtabdetails = pbdb.buildQuery(inserdimtabdetails,
         * dimreldetailsobj);
         *
         * ////////////////////////////////////////////////////////////////////////////////.println.println("buildinserdimtabdetails"
         * + buildinserdimtabdetails);
         * multyqueries.add(buildinserdimtabdetails);
         * ////.println("multyqueries\t" + multyqueries);
         * pbdb.executeMultiple(multyqueries); //
         * pbdb.executeMultiple(coldimquery);
         *
         * dataConnection.close(); dataConnection = null; }
            }
         */
        request.setAttribute("statusOfBucket", checkStatus);
        return mapping.findForward(SUCCESS);
    }
}
