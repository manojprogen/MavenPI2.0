/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userType;

import com.progen.users.UserLayerDAO;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class SaveUserTypePrivileges extends LookupDispatchAction {

    /*
     * forward name="success" path=""
     */
    private static final String SUCCESS = "success";

    public ActionForward saveUserTypePrevileges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        PbDb pbdb = new PbDb();

        ArrayList queries = new ArrayList();

        //String query="insert into user_type_priveleges(user_type,privilege,user_type_privelage_id,privilege_type) values('&','&',USER_TYPE_PRIV_SEQ.nextval,'&')";
        if (session != null && session.getAttribute("USERID") != null) {
            String[] homePrevileges = request.getParameterValues("homePrevileges");
            String[] reportPrevileges = request.getParameterValues("reportPrevileges");
            String[] tablePrevileges = request.getParameterValues("tablePrevileges");
            String[] graphPrevileges = request.getParameterValues("graphPrevileges");
            String userType = request.getParameter("userType");

            // //.println("homePrevileges is " + homePrevileges);
            // //.println("reportPrevileges is " + reportPrevileges);
            ////.println("tablePrevileges is " + tablePrevileges);
            ArrayList delList = new ArrayList();
            String deleteQ = "delete from user_type_priveleges where user_type=" + userType;
            delList.add(deleteQ);
            String userListsql = "select pu_id from prg_ar_users where user_type=" + userType;
            PbReturnObject userListpbro = pbdb.execSelectSQL(userListsql);
            for (int i = 0; i < userListpbro.getRowCount(); i++) {
                //if(homePrevileges.length>0){
                delList.add("delete from PRG_AR_USER_PRIVELEGES where USER_ID=" + userListpbro.getFieldValueInt(i, 0));
                //  }
                //  if(reportPrevileges.length>0){
                delList.add("delete from PRG_AR_REPORT_PREVILAGES where USER_ID=" + userListpbro.getFieldValueInt(i, 0));
                // }
                //  if(tablePrevileges.length>0){
                delList.add("delete from PRG_AR_REPTAB_PREVILAGES where USER_ID=" + userListpbro.getFieldValueInt(i, 0));
                //  }
                //  if(graphPrevileges.length>0){
                delList.add("delete from PRG_AR_REPGRP_PREVILAGES where USER_ID=" + userListpbro.getFieldValueInt(i, 0));
                //  }

            }
            if (homePrevileges != null) {
                for (String str : homePrevileges) {
                    // //.println("str is " + str);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        queries.add("insert into user_type_priveleges(user_type,privilege,PRIVILEGE_TYPE) values('" + userType + "','" + str + "','H')");
                    } else {
                        queries.add("insert into user_type_priveleges(user_type,privilege,user_type_privelage_id,PRIVILEGE_TYPE) values('" + userType + "','" + str + "',USER_TYPE_PRIV_SEQ.nextval,'H')");
                    }


                    for (int i = 0; i < userListpbro.getRowCount(); i++) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            queries.add("insert into PRG_AR_USER_PRIVELEGES(USER_ID, PRIVELEGE_ID) values(" + userListpbro.getFieldValueInt(i, 0) + ",'" + str + "')");
                        } else {
                            queries.add("insert into PRG_AR_USER_PRIVELEGES(PUR_ID, USER_ID, PRIVELEGE_ID) values(PRG_AR_USER_PRIVI_SEQ.nextval  ," + userListpbro.getFieldValueInt(i, 0) + ",'" + str + "')");
                        }

                    }
                }
            }
            if (reportPrevileges != null) {
                for (String str : reportPrevileges) {
                    // //.println("str is " + str);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        queries.add("insert into user_type_priveleges(user_type,privilege,PRIVILEGE_TYPE) values('" + userType + "','" + str + "','R')");
                    } else {
                        queries.add("insert into user_type_priveleges(user_type,privilege,user_type_privelage_id,PRIVILEGE_TYPE) values('" + userType + "','" + str + "',USER_TYPE_PRIV_SEQ.nextval,'R')");
                    }
                    for (int i = 0; i < userListpbro.getRowCount(); i++) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            queries.add("insert into PRG_AR_REPORT_PREVILAGES(USER_ID, PREVILAGE_NAME) values(" + userListpbro.getFieldValueInt(i, 0) + ",'" + str + "')");
                        } else {
                            queries.add("insert into PRG_AR_REPORT_PREVILAGES(PRP_ID, USER_ID, PREVILAGE_NAME) values(PRG_AR_REPORT_PREVILAGES_SEQ.nextval  ," + userListpbro.getFieldValueInt(i, 0) + ",'" + str + "')");
                        }

                    }
                }
            }
            if (tablePrevileges != null) {
                for (String str : tablePrevileges) {
                    //  //.println("str is " + str);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        queries.add("insert into user_type_priveleges(user_type,privilege,PRIVILEGE_TYPE) values('" + userType + "','" + str + "','T')");
                    } else {
                        queries.add("insert into user_type_priveleges(user_type,privilege,user_type_privelage_id,PRIVILEGE_TYPE) values('" + userType + "','" + str + "',USER_TYPE_PRIV_SEQ.nextval,'T')");
                    }
                    for (int i = 0; i < userListpbro.getRowCount(); i++) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            queries.add("insert into PRG_AR_REPTAB_PREVILAGES(USER_ID, PREVILAGE_NAME) values(" + userListpbro.getFieldValueInt(i, 0) + ",'" + str + "')");
                        } else {
                            queries.add("insert into PRG_AR_REPTAB_PREVILAGES(PRTP_ID, USER_ID, PREVILAGE_NAME) values(PRG_AR_REPTAB_PREVILAGES_SEQ.nextval ," + userListpbro.getFieldValueInt(i, 0) + ",'" + str + "')");
                        }

                    }
                }
            }
            if (graphPrevileges != null) {
                for (String str : graphPrevileges) {
                    // //.println("str is " + str);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        queries.add("insert into user_type_priveleges(user_type,privilege,PRIVILEGE_TYPE) values('" + userType + "','" + str + "','RG')");
                    } else {
                        queries.add("insert into user_type_priveleges(user_type,privilege,user_type_privelage_id,PRIVILEGE_TYPE) values('" + userType + "','" + str + "',USER_TYPE_PRIV_SEQ.nextval,'RG')");
                    }
                    for (int i = 0; i < userListpbro.getRowCount(); i++) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            queries.add("insert into PRG_AR_REPGRP_PREVILAGES(USER_ID, PREVILAGE_NAME) values(" + userListpbro.getFieldValueInt(i, 0) + ",'" + str + "')");
                        } else {
                            queries.add("insert into PRG_AR_REPGRP_PREVILAGES(PRGP_ID, USER_ID, PREVILAGE_NAME) values(PRG_AR_REPGRP_PREVILAGES_SEQ.nextval ," + userListpbro.getFieldValueInt(i, 0) + ",'" + str + "')");
                        }

                    }
                }
            }

            //.println("queries is " + queries);
            UserTypeprivDAO usertypedao = new UserTypeprivDAO();
            usertypedao.saveUserTypePrevileges(queries, delList);
            return null;
        } else {
            return mapping.findForward("loginPage");//if session expires
        }
    }

    public ActionForward shwAdminChild(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        PbReturnObject shwChildpbro = new PbReturnObject();
        PbDb pbdb = new PbDb();
        PrintWriter out = response.getWriter();
        StringBuffer dispChildBuffer = new StringBuffer();
        if (session != null && session.getAttribute("USERID") != null) {
            String privType = request.getParameter("privType");
            String heading = request.getParameter("heading");
            String prop = request.getParameter("prop");
            //.println("----privType----" + privType);

            String shwChildQry = "select SUPER_PRIV_NAME,IS_SELECTED from super_prg_admin_privilege where super_priv_type='" + privType + "'";
            //.println("----shwChildQry----" + shwChildQry);
            shwChildpbro = pbdb.execSelectSQL(shwChildQry);

            //.println("shwChildpbro.getRowCount()----" + shwChildpbro.getRowCount());
            dispChildBuffer.append("<div align=\"center\" class='navtitle' style='width:99%'>");
            dispChildBuffer.append("<span id=\"Heading\" style=\"font-size:11px;font-weight:bold;color:#EAF5F7\">" + heading + "</span></div>");
            dispChildBuffer.append("<table><tr><td colspan=\"2\" align=\"center\"><br/></td></tr>");
            for (int i = 0; i < shwChildpbro.getRowCount(); i++) {
                dispChildBuffer.append("<tr>");
                if (shwChildpbro.getFieldValueString(i, "IS_SELECTED").equalsIgnoreCase("Y")) {
                    dispChildBuffer.append("<td class=\"navtitle-hover\" colspan=\"2\" align=\"center\" style=\"text-align:left\"><input type=\"checkbox\" checked id=\"RepSelect\" name=\"RepSelect\" value=\"" + shwChildpbro.getFieldValueString(i, "SUPER_PRIV_NAME") + "\"><a href=\"javascript:void(0)\" onclick=\"renameChild()\" title=\"Click Here To Rename\">" + shwChildpbro.getFieldValueString(i, "SUPER_PRIV_NAME") + "</a></td>");
                } else {
                    dispChildBuffer.append("<td class=\"navtitle-hover\" colspan=\"2\" align=\"center\" style=\"text-align:left\"><input type=\"checkbox\" id=\"RepSelect\" name=\"RepSelect\" value=\"" + shwChildpbro.getFieldValueString(i, "SUPER_PRIV_NAME") + "\"><a href=\"javascript:void(0)\" onclick=\"renameChild()\" title=\"Click Here To Rename\">" + shwChildpbro.getFieldValueString(i, "SUPER_PRIV_NAME") + "</a></td>");
                }
                dispChildBuffer.append("</tr>");
            }
            dispChildBuffer.append("</table>");
            dispChildBuffer.append("<input type=\"hidden\" id=\"prop\" name=\"prop\" value=\"" + prop + "\">");
            out.print(dispChildBuffer.toString());
            return null;
        } else {
            return mapping.findForward("loginPage");//if session expires
        }
    }

    public ActionForward saveAdminChild(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String saveAdminPrivQry = "";
        if (session != null && session.getAttribute("USERID") != null) {
            String Heading = request.getParameter("Heading");
            String savePropArr = request.getParameter("savePropArr");
            String savPropUnchk = request.getParameter("savPropUnchk");
            String savePrivArr = request.getParameter("savePrivArr");
            String prop = request.getParameter("prop");
            String[] savePropList = savePropArr.split(",");
            ArrayList mulQueries = new ArrayList();
            //.println("prop-----" + prop);
            //.println("Heading---" + Heading);
            //.println("savePropArr----" + savePropArr);
            //.println("savPropUnchk---" + savPropUnchk);
            //.println("savePrivArr---" + savePrivArr);
            for (int j = 0; j < savePropList.length; j++) {
                saveAdminPrivQry = "insert  into PRG_ADIM_PRIVILEGE (PRIV_ID,PRIV_TYPE,PRIV_NAME) values (PRG_ADIM_PRIVILEGE_SEQ.nextval,'" + prop + "','" + Heading + "','" + savePropList[j] + "')";
                //.println("saveAdminPrivQry-----" + saveAdminPrivQry);
                mulQueries.add(saveAdminPrivQry);
            }
            return null;
        } else {
            return mapping.findForward("loginPage");//if session expires
        }
    }

    public ActionForward shwSuperPriv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        UserTypeprivDAO dao = new UserTypeprivDAO();
        HashMap childHM = new HashMap();
        if (session != null && session.getAttribute("USERID") != null) {
//          UserTypeprivDAO dao = new UserTypeprivDAO();
            HashMap returnMap = dao.shwSuperPriv();
            //.println("returnMap--Action---" + returnMap.toString());
            HashMap retObjMap = (HashMap) returnMap.get("hashmap");
            HashMap childObjMap = (HashMap) returnMap.get("childObjMap");
            HashMap InnerchildObjMap = (HashMap) returnMap.get("InnerchildMap");
            String[] Innerkeys = (String[]) InnerchildObjMap.keySet().toArray(new String[0]);
            for (int k = 0; k < Innerkeys.length; k++) {
                PbReturnObject childTemp = (PbReturnObject) InnerchildObjMap.get(Innerkeys[k]);
                session.setAttribute(Innerkeys[k], childTemp);

            }
            String[] Childkeys = (String[]) childObjMap.keySet().toArray(new String[0]);
            for (int c = 0; c < Childkeys.length; c++) {
                PbReturnObject tempObj = (PbReturnObject) childObjMap.get(Childkeys[c]);
                //.println("tempObj---" + tempObj);
                //.println("Childkeys----" + Childkeys[c]);
                session.setAttribute(Childkeys[c], tempObj);
            }
            //.println("childObjMap-----" + childObjMap);
            //.println("retObjMap---" + retObjMap);
            //.println("InnerchildObjMap---" + InnerchildObjMap);

            session.setAttribute("retObjMap", retObjMap);
            return mapping.findForward("superPrivPage");
        } else {
            return mapping.findForward("loginPage");//if session expires
        }
    }

    public ActionForward insertReportViewerPriv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //code related to report viewer
        UserTypeprivDAO dao = new UserTypeprivDAO();
        String checkedValues = request.getParameter("finalStr").replace('?', '%');
        PrintWriter out = response.getWriter();
        if (checkedValues != "") {
            String[] parent = checkedValues.split("£");
            String[] childStr;
            String[] childStr1 = null;
            String[] onlyChildNames = null;
            ArrayList child = new ArrayList();
            ArrayList parentNamesAL = new ArrayList();
            ArrayList childNamesAL = new ArrayList();
            ArrayList onlyChildNamesAL = new ArrayList();
            String[] innerChildStr = null;
            ArrayList innerChildNamesAL = new ArrayList();
            HashMap childHM = new HashMap();
            HashMap innerChildHM = new HashMap();
            int i = 0, j = 0, k = 0, l = 0;
            for (i = 0; i < parent.length; i++) {
                childStr = parent[i].split("©");
                child.add(String.valueOf(childStr));
                for (j = 0; j < childStr.length; j++) {
                    if (j == 0) {
                        parentNamesAL.add(childStr[0]);
                    }
                    childStr1 = childStr[1].split("®");
                    for (k = 0; k < childStr1.length; k++) {
                        childNamesAL.add(String.valueOf(childStr1[k]));
                        onlyChildNames = childStr1[k].split("¥");
                        for (l = 0; l < onlyChildNames.length; l++) {
                            if (l == 0) {
                                onlyChildNamesAL.add(String.valueOf(onlyChildNames[l]));
                            }
                            if (l == 1) {
                                innerChildStr = onlyChildNames[l].split(",");
                                for (int m = 0; m < innerChildStr.length; m++) {
                                    innerChildNamesAL.add(String.valueOf(innerChildStr[m]));
                                }

                            }
                            innerChildHM.put(onlyChildNames[0], innerChildNamesAL);
                            innerChildNamesAL = new ArrayList();
                        }
                    }
                    childHM.put(childStr[0], innerChildHM);
                    innerChildHM = new HashMap();
                }
            }
            dao.saveData(childHM);
        }
        // //.println("innerChildHM----" + innerChildHM.keySet());
        // //.println("innerChildHM-Exports--" + innerChildHM.get("Exports"));
        // //.println("innerChildHM-Size--" + innerChildHM.size());


        //from here code related to report designer
        String checkedValuesRD = request.getParameter("finalStrRD").replace('?', '%');
        //.println("checkedValuesRD----" + checkedValuesRD);
        if (checkedValuesRD != "") {
            String[] parentRD = checkedValuesRD.split("£");
            String[] childStrRD;
            String[] childStr1RD = null;
            String[] onlyChildNamesRD = null;
            ArrayList childRD = new ArrayList();
            ArrayList parentNamesALRD = new ArrayList();
            ArrayList childNamesALRD = new ArrayList();
            ArrayList onlyChildNamesALRD = new ArrayList();
            String[] innerChildStrRD = null;
            ArrayList innerChildNamesALRD = new ArrayList();
            HashMap childHMRD = new HashMap();
            HashMap innerChildHMRD = new HashMap();
            int i = 0, j = 0, k = 0, l = 0;
            for (i = 0; i < parentRD.length; i++) {
                childStrRD = parentRD[i].split("©");
                childRD.add(String.valueOf(childStrRD));
                for (j = 0; j < childStrRD.length; j++) {
                    if (j == 0) {
                        parentNamesALRD.add(childStrRD[0]);
                    }
                    childStr1RD = childStrRD[1].split("®");
                    for (k = 0; k < childStr1RD.length; k++) {
                        childNamesALRD.add(String.valueOf(childStr1RD[k]));
                        onlyChildNamesRD = childStr1RD[k].split("¥");
                        for (l = 0; l < onlyChildNamesRD.length; l++) {
                            if (l == 0) {
                                onlyChildNamesALRD.add(String.valueOf(onlyChildNamesRD[l]));
                            }
                            if (l == 1) {
                                innerChildStrRD = onlyChildNamesRD[l].split(",");
                                for (int m = 0; m < innerChildStrRD.length; m++) {
                                    innerChildNamesALRD.add(String.valueOf(innerChildStrRD[m]));
                                }

                            }
                            innerChildHMRD.put(onlyChildNamesRD[0], innerChildNamesALRD);
                            innerChildNamesALRD = new ArrayList();
                        }
                    }
                    childHMRD.put(childStrRD[0], innerChildHMRD);
                    innerChildHMRD = new HashMap();
                }
            }
            dao.saveDataReportDesigner(childHMRD);
            // //.println("innerChildHM----" + innerChildHM.keySet());
            // //.println("innerChildHM-Exports--" + innerChildHM.get("Exports"));
            // //.println("innerChildHM-Size--" + innerChildHM.size());
        }
        out.println(1);




        return null;
    }

    public ActionForward delUserType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String userTypeId = request.getParameter("userTypeId");
        String delflag = "";
        UserLayerDAO userDAO = new UserLayerDAO();
        String[] userTypeIdStr = userTypeId.split(",");
        for (int i = 0; i < userTypeIdStr.length; i++) {
            delflag = userDAO.deleteUserType(userTypeIdStr[i]);
        }
        PrintWriter out = response.getWriter();
        out.print(delflag);

        return null;
    }

    @Override
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("saveUserTypePrevileges", "saveUserTypePrevileges");
        map.put("shwAdminChild", "shwAdminChild");
        map.put("saveAdminChild", "saveAdminChild");
        map.put("shwSuperPriv", "shwSuperPriv");
        map.put("getChildTree", "getChildTree");
        map.put("getInnerChildTree", "getInnerChildTree");
        map.put("insertReportViewerPriv", "insertReportViewerPriv");

        map.put("delUserType", "delUserType");
        return map;

    }
}
