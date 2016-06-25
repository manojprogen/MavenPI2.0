/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.bugDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import oracle.jdbc.OraclePreparedStatement;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class BugDetailsDAO extends PbDb {

    BugDetailsResourceBundle resBundle = new BugDetailsResourceBundle();
    OraclePreparedStatement opstmt;

    public PbReturnObject bugPriority() throws Exception {


        PbReturnObject prgr = new PbReturnObject();
        String bugPriorityquery = resBundle.getString("bugPriority");

        //PbDb pbdb = new PbDb();
        Connection con = (Connection) ProgenConnection.getInstance();
        prgr = execSelectSQL(bugPriorityquery, con);
        //////////////////////.println("f==============" + bugPriorityquery);
        return prgr;
    }

    public PbReturnObject bugstatus() throws Exception {


        PbReturnObject prgr = new PbReturnObject();
        String getbugstatusquery = resBundle.getString("getbugstatus");

        //PbDb pbdb = new PbDb();

        Connection con = (Connection) ProgenConnection.getInstance();
        prgr = execSelectSQL(getbugstatusquery, con);
        //////////////////////.println("f==============" + getbugstatusquery);
        /*
         * String[] strstatus =new String[prgr.getRowCount()]; for(int i
         * =0;i<prgr.getRowCount();i++){ strstatus[i]=(String)
         * prgr.getFieldValue(i, "STATUS_NAME");
         *
         * }
         */

        return prgr;
    }

    void savebugdetails(String[] bugDetailslist, String[] bugDetailslist1) throws SQLException {
        PbReturnObject prgr = new PbReturnObject();
        ArrayList query = new ArrayList();
        String savebugdetailsquery = resBundle.getString("savebugdetails");
        // String savebugdetailsquery1 = resBundle.getString("bugDetailslist1");
        String finalyquery = "";
        String finalyquery1 = "";
        Connection con = (Connection) ProgenConnection.getInstance();
        //Object[] obj1 = new Object[2];
        // obj1[0] = bugDetailslist1[0];
        // obj1[1] = bugDetailslist1[1];

        try {

            finalyquery = buildQuery(savebugdetailsquery, bugDetailslist);
            // = buildQuery(savebugdetailsquery1, obj1);
            // //////////////////////.println("finalyquery1--------===" + finalyquery1);
            //////////////////////.println("finalyquery--------===" + finalyquery);
            query.add(finalyquery);
            //query.add(finalyquery1);

            String savequery = "INSERT INTO BUG_DETAILS (bug_det_id,BUG_DESC,bug_id,created_date,created_time) VALUES(BUG_DETAILS_SEQ.nextval,?,?,sysdate,to_char(sysdate,'dd-mon-yyyy hh24:mi:ss'))";
            //////////////////////.println("savequery is::: 1 "+savequery);
            opstmt = (OraclePreparedStatement) con.prepareStatement(savequery);
            opstmt.setStringForClob(1, bugDetailslist1[0]);
            opstmt.setInt(2, Integer.parseInt(bugDetailslist1[1]));
            //////////////////////.println("savequery is:: 2"+savequery);
            int f = opstmt.executeUpdate();
            //////////////////////.println("f is::: "+f);
            executeMultiple(query, con);

        } catch (Exception ex) {

            logger.error("Exception:", ex);

        }


    }

    public PbReturnObject getDetailsList() throws Exception {

        String getDetailsListquery = resBundle.getString("getDetailsList");
        Connection con = (Connection) ProgenConnection.getInstance();
        PbReturnObject prgr = new PbReturnObject();
        try {

            prgr = execSelectSQL(getDetailsListquery, con);
            //////////////////////.println("query is====" + getDetailsListquery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);

        }

        con.close();
        return prgr;
    }

    public PbReturnObject bugEditdetails(String bugId) throws Exception {

        String getDetailsListquery = resBundle.getString("bugeditdetails");
        Connection con = (Connection) ProgenConnection.getInstance();
        PbReturnObject prgr = new PbReturnObject();
        Object[] obj1 = new Object[1];
        obj1[0] = bugId;
        String finalyquery = "";
        try {
            finalyquery = buildQuery(getDetailsListquery, obj1);
            prgr = execSelectSQL(finalyquery, con);
            //////////////////////.println("query is====" + finalyquery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);

        }

        con.close();
        return prgr;
    }

    void saveEditBugdetails(String[] bugDetailslist, String[] bugDetailslist1) throws SQLException {

        ArrayList query = new ArrayList();
        String savebugdetailsquery = resBundle.getString("saveEditBugdetails");
        // String savebugdetailsquery1 = resBundle.getString("saveEditBugdetails1");
        String finalyquery = "";
        String finalyquery1 = "";
        Connection con = (Connection) ProgenConnection.getInstance();
        Object[] obj1 = new Object[2];
        obj1[0] = bugDetailslist1[0];
        obj1[1] = bugDetailslist1[1];

        try {

            finalyquery = buildQuery(savebugdetailsquery, bugDetailslist);
            //  finalyquery1 = buildQuery(savebugdetailsquery1, obj1);
            //  //////////////////////.println("finalyquery1--------===" + finalyquery1);
            //////////////////////.println("obj1[0] is==" +obj1[0]);
            //////////////////////.println("obj1[1] is==" +obj1[1]);

            String savequery = "INSERT INTO BUG_DETAILS (bug_det_id,BUG_DESC,bug_id,created_date,created_time) VALUES(BUG_DETAILS_SEQ.nextval,?,?,sysdate,to_char(sysdate,'dd-mon-yyyy hh24:mi:ss'))";
            //////////////////////.println("savequery is::: 1 "+savequery);
            opstmt = (OraclePreparedStatement) con.prepareStatement(savequery);
            opstmt.setStringForClob(1, bugDetailslist1[0]);
            opstmt.setInt(2, Integer.parseInt(bugDetailslist1[1]));
            //////////////////////.println("savequery is:: 2"+savequery);
            int f = opstmt.executeUpdate();
            //////////////////////.println("f is::: "+f);


            query.add(finalyquery);
            executeMultiple(query, con);


        } catch (Exception ex) {

            logger.error("Exception:", ex);

        }

        con.close();
    }

    void deleteBugdetails(String bugid) throws SQLException {
        ArrayList query = new ArrayList();
        String deleteBugdetailsquery = resBundle.getString("deleteBugdetails");
        String finalyquery = "";
        Object[] obj1 = new Object[1];
        obj1[0] = bugid;
        Connection con = (Connection) ProgenConnection.getInstance();

        try {

            finalyquery = buildQuery(deleteBugdetailsquery, obj1);

            //////////////////////.println("finalyquery--------===" + finalyquery);
            query.add(finalyquery);
            executeMultiple(query, con);


        } catch (Exception ex) {

            logger.error("Exception:", ex);

        }

    }

    void saveUserDetails(String[] userlist) throws SQLException {

        ArrayList query = new ArrayList();
        String saveUserDetailssquery = resBundle.getString("saveUserDetails");
        String finalyquery = "";

        Connection con = (Connection) ProgenConnection.getInstance();

        try {

            finalyquery = buildQuery(saveUserDetailssquery, userlist);

            //////////////////////.println("finalyquery--------===" + finalyquery);
            query.add(finalyquery);
            executeMultiple(query, con);


        } catch (Exception ex) {

            logger.error("Exception:", ex);

        }

    }

    public boolean saveAssignDetails(String[] userlist) throws SQLException {
        ArrayList query = new ArrayList();
        String saveUserDetailssquery = resBundle.getString("saveAssignDetails");
        String saveUserDetailssquery1 = resBundle.getString("saveAssignDetails1");
        String finalyquery = "";
        String finalyquery1 = "";
        boolean result;

        Connection con = (Connection) ProgenConnection.getInstance();

        try {

            finalyquery = buildQuery(saveUserDetailssquery, userlist);
            finalyquery1 = buildQuery(saveUserDetailssquery1, userlist);

            //////////////////////.println("finalyquery--------===" + finalyquery);
            query.add(finalyquery);
            result = query.add(finalyquery1);

            executeMultiple(query, con);
            return result;

        } catch (Exception ex) {

            logger.error("Exception:", ex);
            result = false;
            return result;

        }

    }
}
