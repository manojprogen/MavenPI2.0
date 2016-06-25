/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.account;

import com.progen.account.db.PbOrganisationDAO;
import java.io.PrintWriter;
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
 * @author Saurabh
 */
public class OrganisationDetails extends LookupDispatchAction {

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
        map.put("dispUsers", "dispUsers");
        map.put("button.delete", "delete");
        map.put("saveOrg", "saveOrg");
        map.put("getAccountTypes", "getAccountTypes");
        map.put("expireOrg", "expireOrg");
        map.put("saveOrgDimMemberVals", "saveOrgDimMemberVals");
        map.put("deleteDimMemberValuesForAcc", "deleteDimMemberValuesForAcc");
        map.put("expireUser", "expireUser");
        map.put("accountAssignReports", "accountAssignReports");
        map.put("checkUserName", "checkUserName");

        map.put("saveNewOrg", "saveNewOrg");
        map.put("assignRoleToAcc", "assignRoleToAcc");
        map.put("checkRoleAssignment", "checkRoleAssignment");

        map.put("checkAccountRoleUser", "checkAccountRoleUser");
        map.put("addUserReportForAccount", "addUserReportForAccount");
        map.put("deleteCompany", "deleteCompany");
        map.put("verifyExpireOrgKey", "verifyExpireOrg");

        map.put("checkOrganisationName", "checkOrganisationName");

        //for modify company details of user
        map.put("modifyUserAccountDetails", "modifyUserAccountDetails");

        //for resetting expiry date of user/company
        map.put("validateExpiryDate", "validateExpiryDate");
        map.put("checkActiveKey", "checkActiveKey");
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
    public ActionForward dispUsers(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PbOrganisationDAO dispUser = new PbOrganisationDAO();
        dispUser.dispUserDetails();

        return null;
    }

    /**
     * Action called on Delete button click
     */
    public ActionForward saveOrg(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        // TODO:implement delete method
        String param = request.getParameter("param");
        if (param.equalsIgnoreCase("saveOrg")) {
            String orgName = request.getParameter("orgName");
            String orgDesc = request.getParameter("orgDesc");
            String startDate = request.getParameter("strtDate");
            String endDate = request.getParameter("endDate");

            String accountFolderId = request.getParameter("accountFolderId");
            ////////////////////////////////////////////////.println.println(" accountFolderId action "+accountFolderId);

            PbOrganisationDAO orgdao = new PbOrganisationDAO();
            orgdao.addOrganisation(orgName, orgDesc, endDate, accountFolderId);
        }

        return mapping.findForward(SUCCESS);
    }

    public ActionForward getAccountTypes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PrintWriter out = response.getWriter();
        String name = orgdao.getAccountTypes();
        out.print(name);
        return null;
    }

    public ActionForward expireOrg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PrintWriter out = response.getWriter();
        String orgId = request.getParameter("orgId");
        orgdao.expireOrg(orgId);
        out.println("1");
        return null;

    }

    public ActionForward verifyExpireOrg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PrintWriter out = response.getWriter();
        String orgId = request.getParameter("orgId");
        // //.println("orgId is" + orgId);
        String status = orgdao.checkExpireOrg(orgId);
        //.println("status is" + status);
        if (status == null) {
            //   out.print("ok not in java");
            out.print("ok");
        } else {
            out.print("These company is already Expired");
        }
        return null;
    }

    public ActionForward saveOrgDimMemberVals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PrintWriter out = response.getWriter();
        String orgId = "";
        String totalUrl = request.getParameter("totalUrl");
        ////////////////////////////////////////////////////.println.println(" totalUrl-.. "+totalUrl);
        String totalUrlVal[] = totalUrl.split("~");
        String memId = "";
        String allVal = "";

        HashMap vals = new HashMap();
        for (int g = 0; g < totalUrlVal.length; g++) {
            String indParam = totalUrlVal[g];
            String indVal[] = indParam.split("=");
            ////////////////////////////////////////////////////.println.println(" indParam "+indParam);
            if (g == 1) {
                memId = indVal[1];
            } else if (g == 0) {
                orgId = indVal[1];
            } else if (g == 2) {
                allVal = indVal[1];
            }

        }
        if (allVal.length() > 1) {
            allVal = allVal.substring(1);
            allVal = allVal.replaceAll(";", "'||chr(38)||'");
        }
        //addUserMemberFilter
        String status = "";
        HashMap details = orgdao.checkMemberValueStatusForAccount(orgId, memId);
        String filterId = (String) details.get("filterId");
        status = (String) details.get("status");
        ////////////////////////////////////////////////////.println.println(" status "+status);
        if (status.equalsIgnoreCase("false")) {
            orgdao.addUserMemberFilterForAcc(orgId, allVal, memId);
        } else if (status.equalsIgnoreCase("true")) {
            orgdao.updateMemberValueStatusForAcc(orgId, memId, allVal, filterId);
        }

        out.println("2");
        return mapping.findForward(null);
    }

    public ActionForward deleteDimMemberValuesForAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //////////////////////////////////////////////////////////////////////.println.println(" in edit");
        String totalUrl = request.getParameter("totalUrl");
        ////////////////////////////////////////////////.println.println(" totalUrl-.. "+totalUrl);
        String totalUrlVal[] = totalUrl.split("~");
        String subFolderIdUser = "";
        String orgId = "";
        String allVal = "";
        String memId = "";
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        HashMap vals = new HashMap();
        for (int g = 0; g < totalUrlVal.length; g++) {
            String indParam = totalUrlVal[g];
            String indVal[] = indParam.split("=");
            if (g == 1) {
                memId = indVal[1];
            } else if (g == 0) {
                orgId = indVal[1];
            }
        }

        orgdao.deleteDimMemberValuesForAcc(orgId, memId);
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }

    public ActionForward expireUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PbDb pbdb = new PbDb();
        String expiresql = "";
        PrintWriter out = response.getWriter();
        String userId = request.getParameter("userId");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            expiresql = "select case when getdate()-pu_end_date>0 then 'Expired' else 'Active' end  as STATUS  from prg_ar_users where pu_id=" + userId;
        } else {
            expiresql = "select case when sysdate-pu_end_date>0 then 'Expired' else 'Active' end  as STATUS  from prg_ar_users where pu_id=" + userId;
        }

        PbReturnObject pbro = pbdb.execSelectSQL(expiresql);
        String status = "";
        if (pbro.getRowCount() > 0) {
            status = pbro.getFieldValueString(0, 0);
            if (status.equalsIgnoreCase("expired")) {
                out.println("2");
            } else {
                orgdao.expireUser(userId);
                out.println("1");
            }
        }

        return null;
    }

    public ActionForward accountAssignReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////.println.println(" in edit");
        String orgId = request.getParameter("orgId");
        String repIds = request.getParameter("repIds");
        //////////////////////////.println.println(orgId+" in hsdjh "+repIds);
        PbOrganisationDAO dao = new PbOrganisationDAO();
        dao.addAccountReports(orgId, repIds);
        PrintWriter out = response.getWriter();
        out.println("1");
        return mapping.findForward(null);
    }

    public ActionForward saveNewOrg(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        // TODO:implement delete method
        String param = "saveOrg";
        if (param.equalsIgnoreCase("saveOrg")) {
            String orgName = request.getParameter("orgName");
            String orgDesc = request.getParameter("orgDesc");
            String startDate = request.getParameter("strtDate");
            String endDate = request.getParameter("endDate");

            String accountFolderId = "";
            if (request.getParameter("accountFolderId") != null) {
                accountFolderId = request.getParameter("accountFolderId");
            }
            //.println(" accountFolderId action " + accountFolderId);

            PbOrganisationDAO orgdao = new PbOrganisationDAO();
            orgdao.addOrganisation(orgName, orgDesc, endDate, accountFolderId);
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward assignRoleToAcc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String orgId = request.getParameter("orgId");
        String accountFolderId = "";
        if (request.getParameter("accountFolderId") != null) {
            accountFolderId = request.getParameter("accountFolderId");
        }
        ////.println.println(orgId+" accountFolderId action..--=-=- "+accountFolderId);
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        orgdao.setRoleForAccount(orgId, accountFolderId);
        return null;
    }

    public ActionForward checkUserName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PrintWriter out = response.getWriter();
        String userName = request.getParameter("userName");
        String orgId = request.getParameter("orgId");
        String status = orgdao.checkUserName(orgId, userName);
        if (status.equalsIgnoreCase("exists")) {
            out.println("1");
        } else {
            out.println("2");
        }
        return null;
    }
    //added by bharathi reddy for account report assignment check

    public ActionForward checkRoleAssignment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PrintWriter out = response.getWriter();
        String orgId = request.getParameter("orgId");
        String status = orgdao.checkRoleAssignment(orgId);
        //////////////////////////.println.println("status--"+status);
        out.println(status);
        return null;
    }

//////////////////added on 28jan
    public ActionForward checkAccountRoleUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////.println.println(" in edit");
        String orgId = request.getParameter("orgId");
        String bRole = request.getParameter("bRole");
        //////////////////////////.println.println(bRole+" in action che -hsdjh "+orgId);
        PbOrganisationDAO dao = new PbOrganisationDAO();

        HashMap all = new HashMap();
        all = dao.getRoleStatusForAcc(orgId, bRole);
        String userStatus = (String) all.get("userStatus");
        String reportSt = (String) all.get("reportSt");
        //////////////////////////.println.println(" all "+all);

        PrintWriter out = response.getWriter();
        if (userStatus.equalsIgnoreCase("User Exists")) {
            if (reportSt.equalsIgnoreCase("Report Exists")) {
                out.println("1");
            } else {
                out.println("2");
            }
        } else {
            out.println("2");
        }

        return mapping.findForward(null);
    }

    public ActionForward addUserReportForAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////.println.println(" in edit");
        String orgId = request.getParameter("orgId");
        String bRole = request.getParameter("bRole");
        String repIds = request.getParameter("repIds");
        String userIds = request.getParameter("userIds");

        //////////////////////////.println.println(bRole+" in action che -hsdjh "+orgId);
        //////////////////////////.println.println(repIds+" userIds/./., "+userIds);
        PbOrganisationDAO dao = new PbOrganisationDAO();
        dao.addAccountUserReports(userIds, repIds, orgId, bRole);

        PrintWriter out = response.getWriter();
        out.println("1");

        return mapping.findForward(null);
    }

    public ActionForward deleteCompany(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ////////////////////////////////////////////////.println.println(" in edit");
        String orgId = request.getParameter("orgId");


        //////////////////////////.println.println(" in action delete -hsdjh "+orgId);

        PbOrganisationDAO dao = new PbOrganisationDAO();
        dao.deleteCompany(orgId);

        PrintWriter out = response.getWriter();
        out.println("1");

        return mapping.findForward(null);
    }

    public ActionForward checkOrganisationName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String orgName = request.getParameter("orgName");
        String status = "";
        //.println("in checkOrganisationName orgname is : " + orgName);
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PbReturnObject pbro = orgdao.checkOrganisationName();
        HttpSession session = request.getSession(false);
        if (session != null) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                if (pbro.getFieldValueString(i, 0).equalsIgnoreCase(orgName)) {
                    status = "This Company Name Already Exists";
                    break;
                }
            }
            response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward modifyUserAccountDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String orgId = request.getParameter("orgId");
        String userId = request.getParameter("userId");
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        orgdao.modifyUserCompanyDetails(orgId, userId);
        return null;
    }

    public ActionForward validateExpiryDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        //.println("in validateExpiryDate is : " + request.getParameter("userId"));
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        PbDb pbdb = new PbDb();
        PrintWriter out = response.getWriter();
        String userId = request.getParameter("userId");
        String newExpDate = request.getParameter("newExpDate");
        String userFlag = request.getParameter("userFlag");
        //.println("newExpDate is : " + newExpDate);
        //.println("userFlag is : " + userFlag);
//        String expiresql = "select case when sysdate-pu_end_date>0 then 'Expired' else 'Active' end  as status  from prg_ar_users where pu_id=" + userId;
//        PbReturnObject pbro = pbdb.execSelectSQL(expiresql);
//        String status = "";
//        if (pbro.getRowCount() > 0) {
//            status = pbro.getFieldValueString(0, 0);
//            if (status.equalsIgnoreCase("expired")) {
//                out.println("2");
//            } else {
//                orgdao.expireUser(userId);
//                out.println("1");
//            }
//        }
        orgdao.validateExpiryDate(userId, newExpDate, userFlag);
        return null;
    }

    public ActionForward checkActiveKey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        //.println("checkActiveKey ");
        PbOrganisationDAO orgdao = new PbOrganisationDAO();
        boolean checkActive = orgdao.checkActiveKey();
//        //.println("checkActive\t"+checkActive);
        out.print(checkActive);
        return null;
    }
}
