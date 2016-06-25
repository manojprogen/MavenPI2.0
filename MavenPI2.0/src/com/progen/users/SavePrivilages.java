/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Mahesh.S
 */
public class SavePrivilages extends org.apache.struts.action.Action {

    PbUserLayerResourceBundle resBundle = new PbUserLayerResourceBundle();
    private static String SUCCESS = "success";
    PbDb pbdb = new PbDb();
    boolean showExtraTabs = true;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("USERID") != null) {
            String method = request.getParameter("method");
            if (method.equalsIgnoreCase("save")) {
                String dscheck = "";
                String msgcheck = "";
                String alrtcheck = "";
                String prtlCheck = "";
                String adminCheckcopy = "";
                String dataCorrectCheck = "";
                String targetCheck = "";
                String scenarioCheck = "";
                String buscheck = "";
                String allcheck = "";
                String admincheck = "";
                buscheck = request.getParameter("buscheck");
                allcheck = request.getParameter("allcheck");
                admincheck = request.getParameter("admincheck");
                ////.println(""+buscheck+"=="+allcheck+"==="+admincheck);
                if (showExtraTabs) {
                    dscheck = request.getParameter("dscheck");
                }
                String rscheck = request.getParameter("rscheck");
                String qscheck = request.getParameter("qscheck");
                if (showExtraTabs) {
                    msgcheck = request.getParameter("msgcheck");
                    alrtcheck = request.getParameter("alrtcheck");
                    prtlCheck = request.getParameter("prtlcheck");
                    adminCheckcopy = request.getParameter("adminCheckcopy");
                    dataCorrectCheck = request.getParameter("dataCorrectCheck");
                    targetCheck = request.getParameter("targetCheck");
                    scenarioCheck = request.getParameter("scenarioCheck");
                }

                String userId = request.getParameter("userId");

                ArrayList UserPrevileges = new ArrayList();
                if (buscheck != null && !buscheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(buscheck);
                }
                if (allcheck != null && !allcheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(allcheck);
                }
                if (admincheck != null && !admincheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(admincheck);
                }
                if (dscheck != null && !dscheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(dscheck);
                }
                if (rscheck != null && !rscheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(rscheck);
                }
                if (qscheck != null && !qscheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(qscheck);
                }
                if (msgcheck != null && !msgcheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(msgcheck);
                }
                if (alrtcheck != null && !alrtcheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(alrtcheck);
                }
                if (prtlCheck != null && !prtlCheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(prtlCheck);
                }
                if (adminCheckcopy != null && !adminCheckcopy.equalsIgnoreCase("")) {
                    UserPrevileges.add(adminCheckcopy);
                }
                if (dataCorrectCheck != null && !dataCorrectCheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(dataCorrectCheck);
                }

                if (targetCheck != null && !targetCheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(targetCheck);
                }
                if (scenarioCheck != null && !scenarioCheck.equalsIgnoreCase("")) {
                    UserPrevileges.add(scenarioCheck);
                }

                session.setAttribute("UserPrevileges", UserPrevileges);
                String addPrevilagesQuery = resBundle.getString("addUserPrivilages");
                String sql = "delete from PRG_AR_USER_PRIVELEGES where USER_ID in(" + userId + ")";
                Object obj[] = null;
                String finalQuery = "";
                ArrayList queries = new ArrayList();
                queries.add(sql);
                ////.println("UserPrevileges=="+UserPrevileges);
                for (int i = 0; i < UserPrevileges.size(); i++) {
                    obj = new Object[2];
                    obj[0] = userId;
                    obj[1] = UserPrevileges.get(i).toString();
                    finalQuery = pbdb.buildQuery(addPrevilagesQuery, obj);
                    queries.add(finalQuery);
                }
                pbdb.executeMultiple(queries);
                //////.println("UserPrevileges is " + UserPrevileges);
            } else if (method.equalsIgnoreCase("getPrivilages")) {
                PbReturnObject pbretobj = new PbReturnObject();
                String userid = request.getParameter("userId");
                String query = "";
                // query="select privilege from user_type_priveleges where user_type in( select user_type from prg_ar_users where pu_id="+userid+") and privilege_type ='H'";
                // pbretobj = pbdb.execSelectSQL(query);
                //  if(!(pbretobj.getRowCount()>0)){
                query = "select PRIVELEGE_ID from prg_ar_user_priveleges where user_id =" + userid;
                pbretobj = pbdb.execSelectSQL(query);
                // }
                ArrayList UserPrevileges = new ArrayList();
                if (pbretobj.getRowCount() != 0 && pbretobj != null) {
                    for (int i = 0; i < pbretobj.getRowCount(); i++) {
                        UserPrevileges.add(pbretobj.getFieldValueString(i, 0));
                    }
                }
                //request.setAttribute("savedprivilages", savedprivilages);
                session.setAttribute("UserPrevileges", UserPrevileges);
                SUCCESS = "showprivilages";

            } else if (method.equalsIgnoreCase("getReportPrevilages")) {
                PbReturnObject pbretobj = new PbReturnObject();
                String userid = request.getParameter("userId");
                String repQuery = "";
                // repQuery="select privilege from user_type_priveleges where user_type in( select user_type from prg_ar_users where pu_id="+userid+") and privilege_type ='R'";
                // pbretobj = pbdb.execSelectSQL(repQuery);
                /// ////.println("repQuery==="+repQuery);
                // if(!(pbretobj.getRowCount()>0)){
                ////.println("in if===");
                repQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPORT_PREVILAGES WHERE USER_ID =" + userid;
                pbretobj = pbdb.execSelectSQL(repQuery);
                ////.println("repQuery=="+repQuery);
                // }

                ArrayList UserReportPrevileges = new ArrayList();
                if (pbretobj.getRowCount() > 0 && pbretobj != null) {
                    for (int i = 0; i < pbretobj.getRowCount(); i++) {
                        UserReportPrevileges.add(pbretobj.getFieldValueString(i, 0));
                    }
                }
                //////.println("UserReportPrevileges is " + UserReportPrevileges);
                session.setAttribute("UserReportPrevileges", UserReportPrevileges);
                SUCCESS = "showRepPrivilages";
            } else if (method.equalsIgnoreCase("saveRepPrevilages")) {
                String addReportPrevilagesQuery = resBundle.getString("addReportPrevilages");

                String favLink = request.getParameter("flink");
                String compMsg = request.getParameter("cmsg");
                String tBottom = request.getParameter("topbot");
                String customRep = request.getParameter("custrep");
                String snapShot = request.getParameter("snap");
                String Schedule = request.getParameter("sch");
                String msgs = request.getParameter("msgtab");
                String tracker = request.getParameter("track");
                String bizrole = request.getParameter("bRole");
                String sticky = request.getParameter("sticky");
                String edit = request.getParameter("edit");
                String custDrill = request.getParameter("custDrill");
                String overwriteRep = request.getParameter("overwriteRep");
                String userId = request.getParameter("userId");

                ArrayList UserReportPrevileges = new ArrayList();
                if (favLink != null) {
                    UserReportPrevileges.add(favLink);
                }
                if (compMsg != null) {
                    UserReportPrevileges.add(compMsg);
                }
                if (tBottom != null) {
                    UserReportPrevileges.add(tBottom);
                }
                if (customRep != null) {
                    UserReportPrevileges.add(customRep);
                }
                if (snapShot != null) {
                    UserReportPrevileges.add(snapShot);
                }
                if (Schedule != null) {
                    UserReportPrevileges.add(Schedule);
                }
                if (msgs != null) {
                    UserReportPrevileges.add(msgs);
                }
                if (tracker != null) {
                    UserReportPrevileges.add(tracker);
                }
                if (bizrole != null) {
                    UserReportPrevileges.add(bizrole);
                }
                if (sticky != null) {
                    UserReportPrevileges.add(sticky);
                }
                if (edit != null) {
                    UserReportPrevileges.add(edit);
                }
                if (custDrill != null) {
                    UserReportPrevileges.add(custDrill);
                }
                if (overwriteRep != null) {
                    UserReportPrevileges.add(overwriteRep);
                }

                session.setAttribute("UserReportPrevileges", UserReportPrevileges);
                String sql = "delete from PRG_AR_REPORT_PREVILAGES where USER_ID=" + userId;
                Object obj[] = null;
                String finalQuery = "";
                ArrayList queries = new ArrayList();
                queries.add(sql);

                for (int i = 0; i < UserReportPrevileges.size(); i++) {
                    obj = new Object[2];
                    obj[0] = userId;
                    obj[1] = UserReportPrevileges.get(i).toString();
                    finalQuery = pbdb.buildQuery(addReportPrevilagesQuery, obj);
                    queries.add(finalQuery);
                }
                pbdb.executeMultiple(queries);
                //////.println("UserReportPrevileges is " + UserReportPrevileges);

            } else if (method.equalsIgnoreCase("getReportGraphPrevilages")) {
                PbReturnObject pbrepGrpretobj = new PbReturnObject();
                String userid = request.getParameter("userId");
                String repGrpQuery = "";
                // repGrpQuery="select privilege from user_type_priveleges where user_type in( select user_type from prg_ar_users where pu_id="+userid+") and privilege_type ='RG'";
                // pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
                // if(!(pbrepGrpretobj.getRowCount()>0)){
                repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPGRP_PREVILAGES WHERE USER_ID =" + userid;
                pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
                // }
                ArrayList UserGraphPrevileges = new ArrayList();
                if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
                    for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
                        UserGraphPrevileges.add(pbrepGrpretobj.getFieldValueString(i, 0));
                    }
                }
                //////.println("UserGraphPrevileges is " + UserGraphPrevileges);
                session.setAttribute("UserGraphPrevileges", UserGraphPrevileges);
                SUCCESS = "showRepGraphPrivilages";
            } else if (method.equalsIgnoreCase("saveRepGraphPrevilages")) {

                ArrayList UserGraphPrevileges = new ArrayList();
                String grpTypes = request.getParameter("grpTypes");
                String grpCols = request.getParameter("grpCols");
                String rowsCheck = request.getParameter("rowsCheck");
                String grpProperties = request.getParameter("grpProperties");
                String addGraphs = request.getParameter("addGraphs");
                String editGrpName = request.getParameter("editGrpName");
                String deleteGraph = request.getParameter("deleteGraph");
                String userId = request.getParameter("userId");
                if (grpTypes != null) {
                    UserGraphPrevileges.add(grpTypes);
                }
                if (grpCols != null) {
                    UserGraphPrevileges.add(grpCols);
                }
                if (rowsCheck != null) {
                    UserGraphPrevileges.add(rowsCheck);
                }
                if (grpProperties != null) {
                    UserGraphPrevileges.add(grpProperties);
                }
                if (addGraphs != null) {
                    UserGraphPrevileges.add(addGraphs);
                }
                if (editGrpName != null) {
                    UserGraphPrevileges.add(editGrpName);
                }
                if (deleteGraph != null) {
                    UserGraphPrevileges.add(deleteGraph);
                }

                session.setAttribute("UserGraphPrevileges", UserGraphPrevileges);

                String sql = "delete from PRG_AR_REPGRP_PREVILAGES where USER_ID=" + userId;
                String addReportGraphPrevilagesQuery = resBundle.getString("addReportGraphPrevilages");

                Object obj[] = null;
                String finalQuery = "";
                ArrayList queries = new ArrayList();
                queries.add(sql);
                for (int i = 0; i < UserGraphPrevileges.size(); i++) {
                    obj = new Object[2];
                    obj[0] = userId;
                    obj[1] = UserGraphPrevileges.get(i).toString();
                    finalQuery = pbdb.buildQuery(addReportGraphPrevilagesQuery, obj);
                    queries.add(finalQuery);
                }
                pbdb.executeMultiple(queries);
                //////.println("UserGraphPrevileges is " + UserGraphPrevileges);

            } else if (method.equalsIgnoreCase("getReportTablePrevilages")) {
                PbReturnObject pbretobj = new PbReturnObject();
                String userid = request.getParameter("userId");
                String repQuery = "";
                // repQuery="select privilege from user_type_priveleges where user_type in( select user_type from prg_ar_users where pu_id="+userid+") and privilege_type ='T'";
                // pbretobj = pbdb.execSelectSQL(repQuery);
                // if(!(pbretobj.getRowCount()>0)){
                repQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPTAB_PREVILAGES WHERE USER_ID =" + userid;
                pbretobj = pbdb.execSelectSQL(repQuery);
                //  }
                ArrayList UserTablePrevileges = new ArrayList();

                if (pbretobj.getRowCount() != 0 && pbretobj != null) {
                    for (int i = 0; i < pbretobj.getRowCount(); i++) {
                        UserTablePrevileges.add(pbretobj.getFieldValueString(i, 0));
                    }
                }
                //////.println("UserTablePrevileges is " + UserTablePrevileges);
                session.setAttribute("UserTablePrevileges", UserTablePrevileges);
                SUCCESS = "showRepTablePrivilages";
            } else if (method.equalsIgnoreCase("saveRepTablePrevilages")) {

                String expCheck = request.getParameter("expCheck");
                String hideShowColsCheck = request.getParameter("hideShowColsCheck");
                String TablePropertiesCheck = request.getParameter("TablePropertiesCheck");
                String hideTableCheck = request.getParameter("hideTableCheck");
                String transTableCheck = request.getParameter("transTableCheck");
                String userId = request.getParameter("userId");

                ArrayList UserTablePrevileges = new ArrayList();
                if (expCheck != null) {
                    UserTablePrevileges.add(expCheck);
                }
                if (hideShowColsCheck != null) {
                    UserTablePrevileges.add(hideShowColsCheck);
                }
                if (TablePropertiesCheck != null) {
                    UserTablePrevileges.add(TablePropertiesCheck);
                }

                if (hideTableCheck != null) {
                    UserTablePrevileges.add(hideTableCheck);
                }
                if (transTableCheck != null) {
                    UserTablePrevileges.add(transTableCheck);
                }

                session.setAttribute("UserTablePrevileges", UserTablePrevileges);
                String sql = "delete from PRG_AR_REPTAB_PREVILAGES where USER_ID=" + userId;
                String addReportTablePrevilagesQuery = resBundle.getString("addReportTablePrevilages");
                Object obj[] = null;
                String finalQuery = "";
                ArrayList queries = new ArrayList();
                queries.add(sql);
                for (int i = 0; i < UserTablePrevileges.size(); i++) {
                    obj = new Object[2];
                    obj[0] = userId;
                    obj[1] = UserTablePrevileges.get(i).toString();
                    finalQuery = pbdb.buildQuery(addReportTablePrevilagesQuery, obj);
                    queries.add(finalQuery);
                }
                pbdb.executeMultiple(queries);
                //////.println("UserTablePrevileges is " + UserTablePrevileges);
            }
            return mapping.findForward(SUCCESS);
            //return null;
        } else {
            return mapping.findForward("loginPage");//if session expires
        }
    }
}
