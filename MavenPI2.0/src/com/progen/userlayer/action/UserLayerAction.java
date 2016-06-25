/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.action;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.progen.datasnapshots.DataSnapshotGenerator;
import com.progen.metadata.Cube;
import com.progen.metadata.CubeInterface;
import com.progen.metadata.MetadataDAO;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.report.whatIf.WhatIfScenario;
import com.progen.report.whatIf.WhatIfSensitivity;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.userlayer.db.PbUserLayerEditDAO;
import com.progen.userlayer.db.PbUserLayerResourceBundle;
import com.progen.userlayer.db.SavePublishUserFolder;
import com.progen.userlayer.db.UserLayerDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import javax.servlet.http.HttpSession;
import prg.business.group.BusinessGroupDAO;
import prg.business.group.PbBussGrpResourceBundle;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import com.progen.userlayer.db.PbUserLayerEditDAO;
import com.progen.userlayer.db.PbUserLayerResourceBundle;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.sql.SQLException;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import prg.db.Container;
import utils.db.ProgenConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @filename UserLayerAction
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 5, 2009, 4:05:40 PM
 */
public class UserLayerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(UserLayerAction.class);

    public ActionForward addUserFolder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String folderName = request.getParameter("fldName");
        String folderDesc = request.getParameter("fldDesc");
        String grpId = request.getParameter("grpId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.truncateDimValues();
        userLayerDAO.insertNewDimValues();
        boolean result = userLayerDAO.addUserFolder(folderName, folderDesc, grpId);
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        out.print(result);
        return mapping.findForward("userLayer");
    }

    public ActionForward copyUserFolder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String folderName = request.getParameter("fldName");
        String folderDesc = request.getParameter("fldDesc");
        String oldFolderId = request.getParameter("oldFolderId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = userLayerDAO.copyUserFolder(folderName, folderDesc, oldFolderId);
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        out.print(result);

        return mapping.findForward("userLayer");
    }

    public ActionForward getUserFolderList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
      HttpSession session;
        String connId = request.getParameter("connId");

        if (request.getSession() == null) {
            session = request.getSession(true);

        } else {
            session = request.getSession();
        }
        if (connId == null || connId.equalsIgnoreCase("")) {
            connId = String.valueOf(session.getAttribute("connId"));
            session.setAttribute("connId", connId);
            MetadataDAO metadataDAO = new MetadataDAO();
            ArrayList<Cube> cubes = (ArrayList<Cube>) metadataDAO.getUserFolderList(connId);
            session.setAttribute("CUBES", cubes);
        } else {
            session.setAttribute("connId", connId);
            MetadataDAO metadataDAO = new MetadataDAO();
            ArrayList<Cube> cubes = (ArrayList<Cube>) metadataDAO.getUserFolderList(connId);
            session.setAttribute("CUBES", cubes);
        }
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String UserFolderList = userLayerDAO.getUserFolderList(connId);//bussGroupListDAO.getBusinessGroups();
        request.setAttribute("UserFolderList", UserFolderList);

        return mapping.findForward("userLayer");
    }
//by sunita

    public ActionForward getUserFolderList2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session;
        String connId = request.getParameter("connId");
        String folderId = request.getParameter("folderId");
        if (request.getSession() == null) {
            session = request.getSession(true);

        } else {
            session = request.getSession();
        }
        if (connId == null || connId.equalsIgnoreCase("")) {
            connId = String.valueOf(session.getAttribute("connId"));
            folderId = String.valueOf(session.getAttribute("folderId"));
            session.setAttribute("connId", connId);
            session.setAttribute("folderId", folderId);
            MetadataDAO metadataDAO = new MetadataDAO();
            ArrayList<Cube> cubes = (ArrayList<Cube>) metadataDAO.getUserFolderList2(connId, folderId);
            session.setAttribute("CUBES", cubes);
        } else {
            session.setAttribute("connId", connId);
            session.setAttribute("folderId", folderId);
            MetadataDAO metadataDAO = new MetadataDAO();
            ArrayList<Cube> cubes = (ArrayList<Cube>) metadataDAO.getUserFolderList2(connId, folderId);
            session.setAttribute("CUBES", cubes);
        }
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String UserFolderList = userLayerDAO.getUserFolderList2(connId, folderId);//bussGroupListDAO.getBusinessGroups();
        request.setAttribute("UserFolderList", UserFolderList);

        return mapping.findForward("userLayer");
    }

    public ActionForward getUserFolderList1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session;
        String connId = request.getParameter("connId");
        if (request.getSession() == null) {
            session = request.getSession(true);
        } else {
            session = request.getSession();
        }
        try {
            if (connId == null || connId.equalsIgnoreCase("")) {
                connId = String.valueOf(session.getAttribute("connId"));
                session.setAttribute("connId", connId);
                MetadataDAO metadataDAO = new MetadataDAO();
                response.getWriter().print(metadataDAO.getUserFolderList1(connId));
            } else {
                session.setAttribute("connId", connId);
                MetadataDAO metadataDAO = new MetadataDAO();
                response.getWriter().print(metadataDAO.getUserFolderList1(connId));
            }
        } catch (IOException e) {
            logger.error("Exception : ",e);
        }
        return null;
    }

    public ActionForward deleteUserFolder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String folderId = request.getParameter("folderId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        //boolean result = userLayerDAO.deleteUserFolder(folderId);
       PrintWriter out = response.getWriter();
        //added by susheela 28-11-09 start
        String status = userLayerDAO.checkRoleForDelete(folderId);
//        boolean result = false;
        if (status.equalsIgnoreCase("false")) {
             userLayerDAO.deleteUserFolder(folderId);
            out.println("2");
        } else {
            out.println("1");
         }
        //added by susheela over

        // response.setContentType("text/xml");
        //out.print(result);
        // return mapping.findForward("userLayer");
        return mapping.findForward(null);
    }

    //added by Mohit for user Registration
    public ActionForward getPUid(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pmid = request.getParameter("pmid");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = null;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.getPUid(pmid);
        out.print(result);
        return null;

    }

    public ActionForward getPendingUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String select = request.getParameter("select");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = null;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.getPendingUsers(select, request);
        out.print(result);
        return null;

    }
    //added by Mohit for user Registration

    public ActionForward dActUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//     String id = request.getParameter("id");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = null;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.dActUsers(request);
        out.print(result);
        return null;

    }

    public ActionForward rejectUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//     String id = request.getParameter("id");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = null;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.rejectUsers(request);
        out.print(result);
        return null;

    }

//added by Mohit for user Registration
    public ActionForward userRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String title = request.getParameter("title");
        String fn = request.getParameter("fn");
        String ln = request.getParameter("ln");
        String email = request.getParameter("email");
        String mob = request.getParameter("mob");
        String nationality = request.getParameter("nationality");
        String country = request.getParameter("country");
        String add = request.getParameter("add");
        String prof = request.getParameter("prof");
        String purpose = request.getParameter("purpose");
//        String ut = request.getParameter("prof");
//        String state = request.getParameter("purpose");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = null;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.userRegistration(title, fn, ln, email, mob, nationality, country, add, prof, purpose, request);
//        if (result==true){
//        response.getWriter().print(result);
//         } else {
//        response.getWriter().print(result);
//        }
//           
        out.print(result);
        return null;
        //added by Mohit for Custom setting
    }

    public ActionForward SubmitSetting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String repTitle = request.getParameter("repTitle");
        String hideDate = request.getParameter("hideDate");
        String fColor = request.getParameter("fColor");
        String dateF = request.getParameter("dateF");
        String sytm = request.getParameter("sytm");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = null;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.SubmitSetting(repTitle, hideDate, fColor, dateF, sytm);
//        if (result==true){
//        response.getWriter().print(result);
//         } else {
//        response.getWriter().print(result);
//        }
//           
        out.print(result);
        return null;
        //added by Mohit for Custom setting
    }

    //<!--Added by mohit for drl-->
    public ActionForward getBuyer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String querytype = request.getParameter("querytype");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = null;
        PrintWriter out = response.getWriter();
        if (querytype.equalsIgnoreCase("select")) {
            String temp = request.getParameter("temp");
            String flag = request.getParameter("flag");
            result = userLayerDAO.getBuyer(temp, flag, querytype);
        } else if (querytype.equalsIgnoreCase("insert") || querytype.equalsIgnoreCase("update")) {
            String bName = request.getParameter("bName");
            String bGroup = request.getParameter("bGroup");
            String bUnit = request.getParameter("bUnit");
            String team = request.getParameter("team");
            String sddatepicker = request.getParameter("sddatepicker");
            String eddatepicker = request.getParameter("eddatepicker");
            String bid = request.getParameter("bid");
            result = userLayerDAO.setBuyer(bName, bGroup, bUnit, team, sddatepicker, eddatepicker, bid, querytype);
        }
        //addede by sruthi
        if (querytype.equalsIgnoreCase("Delete")) {
            String dName = request.getParameter("dname");
            result = userLayerDAO.deleteBuyer(dName, querytype);
        }
        //ended by sruthi
        out.print(result);
        return null;
        //added by Mohit for Custom setting
    }

    public ActionForward publishUserFolder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String folderId = request.getParameter("folderId");
       SavePublishUserFolder publish = new SavePublishUserFolder();
        boolean result = publish.publishUserFolder(folderId);
         PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        out.print(result);
        return mapping.findForward("userLayer");
    }

    //added by susheela start
    public ActionForward updateElemetIdFolder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String elementId = request.getParameter("elementId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.updateElementId(elementId);
        return null;
    }

    public ActionForward updateFacts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String factTabId = request.getParameter("factTabId");
        String checked = request.getParameter("checked");
 UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.updateSelectedFactTable(factTabId, checked);
        return null;
    }

    public ActionForward deleteRoleDimension(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subFolderId = request.getParameter("subFolderId");
        String dimId = request.getParameter("dimId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.deleteRoleDimension(dimId, subFolderId);
        return mapping.findForward(null);
    }

    public ActionForward deleteRoleDimensionMember(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dimTabId = request.getParameter("dimTabId");
        String memId = request.getParameter("memId");
        String subFolderTabId = request.getParameter("sFolder");
UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.deleteRoleDimensionMembers(memId, subFolderTabId, dimTabId);
        return mapping.findForward(null);
    }

    public ActionForward editCustomeRoleDrill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String totalUrl = request.getParameter("totalUrl");
        String totalUrlVal[] = totalUrl.split("-");
        String subFolderId = "";
        HashMap vals = new HashMap();
        for (int g = 0; g < totalUrlVal.length; g++) {
            String indParam = totalUrlVal[g];
            String indVal[] = indParam.split("=");
            if (g == 0) {
                subFolderId = indVal[1];
            } else {
                vals.put(indVal[0], indVal[1]);
            }
        }
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.updateCustomDrill(subFolderId, vals);
        return mapping.findForward(null);
    }

    //added on 16Nov
    public ActionForward updateDimensionStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dimSubFolder = request.getParameter("dimSubFolder");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.updateDimensionStatus(dimSubFolder);
        return mapping.findForward(null);
    }

    public ActionForward updateDimensionMemberStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subMbrDimTab = request.getParameter("subMbrDimTab");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.updateDimensionMemberStatus(subMbrDimTab);
        return mapping.findForward(null);
    }
    // added by susheela 28-11-09

    public ActionForward checkBussRoleForTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subTargetFolder = request.getParameter("subTargetFolder");
        PrintWriter out = response.getWriter();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        HashMap details = userLayerDAO.checkBussRoleForTarget(subTargetFolder);
//        String busGroup = "";
//        String roleName = "";
        String status = "";
        String grpId = "";
        status = (String) details.get("status");
        grpId = (String) details.get("grpId");
        if (status.equalsIgnoreCase("true")) {
             out.println("1");
        } else if (status.equalsIgnoreCase("false")) {
            userLayerDAO.makeRoleForTarget(subTargetFolder, grpId);
            out.println("2");
        }
        return mapping.findForward(null);
    }

    public ActionForward getBussRoleForTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subTargetFolder = request.getParameter("subTargetFolder");
        PrintWriter out = response.getWriter();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        HashMap details = userLayerDAO.checkBussRoleForTarget(subTargetFolder);
        String busGroup = "";
        String roleName = "";
//        String status = "";
        String msg = "";
        roleName = (String) details.get("roleName");
        busGroup = (String) details.get("busGroup");
        msg = "The role '" + roleName + "' is already available for business group '" + busGroup + "'";
         out.println(msg);
        return mapping.findForward(null);
    }

//    public ActionForward makeRoleForTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String subTargetFolder = request.getParameter("subTargetFolder");
//        UserLayerDAO userLayerDAO = new UserLayerDAO();
//        return mapping.findForward(null);
//    }

    //added by susheela start 02-12-09
    public ActionForward editUserCustomRoleDrill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String totalUrl = request.getParameter("totalUrl");
        String totalUrlVal[] = totalUrl.split("-");
        String subFolderId = "";
        String userId = "";
        HashMap vals = new HashMap();
        for (int g = 0; g < totalUrlVal.length; g++) {
            String indParam = totalUrlVal[g];
            String indVal[] = indParam.split("=");
            if (g == 0) {
                subFolderId = indVal[1];
            } else if (g == 1) {
                userId = indVal[1];
            } else {
                vals.put(indVal[0], indVal[1]);
            }
        }
       UserLayerDAO userLayerDAO = new UserLayerDAO();
        HashMap details = userLayerDAO.getUserDrilStatus(subFolderId, userId);
        String userDrillId = (String) details.get("userDrillId");
        String status = (String) details.get("status");
         if (status.equalsIgnoreCase("insert")) {
            userLayerDAO.insertUserCustomDrill(vals, userId, subFolderId);
        } else if (status.equalsIgnoreCase("update")) {
            userLayerDAO.updateUserCustomDrill(vals, userId, subFolderId, userDrillId);
        }
        return mapping.findForward(null);
    }

    public ActionForward addUserDimMemberValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subFolderIdUser = request.getParameter("subFolderIdUser");
        String userId = request.getParameter("userId");
        String allVal = request.getParameter("meemberValues");
        String userMemId = request.getParameter("userMemId");
        String elementId = request.getParameter("elementId");
        String dimId = request.getParameter("dimId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String[] meemberValues = null;
        String memberValuesXml = "";
        HttpSession session = request.getSession();
//        if (allVal.length() > 1) {
//
//            allVal = allVal.replaceAll(";", "'||chr(38)||'");
//        }
//        if (!allVal.equalsIgnoreCase("")) {
//            meemberValues = allVal.split(",");
//        }

        if (!allVal.equalsIgnoreCase("")) {
            allVal = allVal.replace("%20", " ").replace("%26", "&").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%40", "@");
            allVal = allVal.replace("%23", "#").replace("%24", "$").replace("%25", "%").replace("%5E", "^").replace("%60", "`").replace("%3C", "<").replace("%3E", ">");
            meemberValues = allVal.split(",");
        }

        if (meemberValues != null) {
            memberValuesXml = userLayerDAO.toXMLSec(meemberValues);
            int len = memberValuesXml.length();
            memberValuesXml = memberValuesXml.substring(0, len - 1);
        }
        String status = "";
        HashMap details = userLayerDAO.checkMemberValueStatus(userId, subFolderIdUser, userMemId);
        String filterId = (String) details.get("filterId");
        status = (String) details.get("status");
        if (status.equalsIgnoreCase("false")) {
            userLayerDAO.addUserMemberFilter(userId, subFolderIdUser, memberValuesXml, userMemId, elementId, dimId);
        } else if (status.equalsIgnoreCase("true")) {
            userLayerDAO.updateMemberValueStatus(userId, subFolderIdUser, userMemId, memberValuesXml, filterId, elementId, dimId);
        }
        String connectionId = (String) session.getAttribute("connId");
        MetadataDAO metadataDAO = new MetadataDAO();
        ArrayList<Cube> cubes = (ArrayList<Cube>) metadataDAO.getUserFolderList(connectionId);
        session.setAttribute("CUBES", cubes);
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }

    //added by susheela over 02-12-09
    //added on  04-12-09
    public ActionForward addUserLayerFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subFolderTabId = request.getParameter("table_id");
        String buss_table_id = request.getParameter("buss_table_id");
        String filter = request.getParameter("txt2");
        String subFolderId = request.getParameter("subFolderId");
        Connection con;
        Statement st;
        ResultSet rs;
        con = ProgenConnection.getInstance().getConnection();
        st = con.createStatement();
        PrintWriter out = response.getWriter();
        String Query = "insert into PRG_ROLE_FILTER_SIM (filter_id,buss_table_id,sub_folder_id,sub_folder_tab_id,filter,created_by,created_on,updated_by,updated_on) values "
                + " (PRG_ROLE_FILTER_SIM_SEQ.nextval," + buss_table_id + ",'" + subFolderId + "','" + subFolderTabId + "','" + filter + "','','','','')";
        ArrayList al = new ArrayList();
        al.add(Query);
        PbDb pbdb = new PbDb();
        //st.executeUpdate(Query);
        try {
            pbdb.executeMultiple(al);
            out.println(2);
        } catch (Exception ex) {
            out.println(1);
            logger.error("Exception:", ex);
        }
        if (con != null) {
            con.close();
        }
        return mapping.findForward(null);
    }
    //addUserDimMemberValues

    public ActionForward deleteDimMemberValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String totalUrl = request.getParameter("totalUrl");
        String totalUrlVal[] = totalUrl.split("~");
        String subFolderIdUser = "";
        String userId = "";
        String allVal = "";
        String userMemId = "";
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        HashMap vals = new HashMap();
        for (int g = 0; g < totalUrlVal.length; g++) {
            String indParam = totalUrlVal[g];
            String indVal[] = indParam.split("=");
            if (g == 1) {
                subFolderIdUser = indVal[1];
            } else if (g == 0) {
                userId = indVal[1];
            } else if (g == 2) {
                userMemId = indVal[1];
            }
        }
        userLayerDAO.deleteDimMemberValues(userId, subFolderIdUser, userMemId);
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }
// added by susheela over

    public ActionForward checkRoleFormulaQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultSet rs = null;
        PbDb pbdb = new PbDb();
        int i = 0;
        try {
            /*
             * String Query = request.getParameter("query"); Query =
             * Query.replace("@", "+"); String tableName =
             * request.getParameter("tableList"); String ConnectionId =
             * request.getParameter("ConnectionId");
             *
             * String finalQuery = "select " + Query + " from " + tableName;
             * ////////////////////////////////////////////////////////////////////////////////.println.println("finalQuery---"
             * + finalQuery); BusinessGroupDAO bd = new BusinessGroupDAO();
             * Connection con = bd.getConnectionIdConnection(ConnectionId);
             * Statement st = con.createStatement(); rs = st.executeQuery(finalQuery);
             */
            String Query = request.getParameter("query");
            Query = Query.replace("@", "+");
            String ConnectionId = request.getParameter("ConnectionId");
            String tArea1 = request.getParameter("tArea1");
            String tArea = request.getParameter("tArea");
            tArea = tArea.replace("@", "+");
            if (tArea1.length() > 0) {
                String a = tArea1.trim().substring(1);
                String eleList2[] = a.split("~");
//                String eleList1 = "";
                StringBuilder eleList1 = new StringBuilder();
                if (eleList2.length > 1) {
                    for (int j = 0; j < eleList2.length - 1; j++) {
                        int count = 0;
                        for (int j1 = j + 1; j1 < eleList2.length; j1++) {
                            if (eleList2[j].equalsIgnoreCase(eleList2[j1])) {
                                count = 1;
                                break;
                            }
                        }
                        if (count == 0) {
//                            eleList1 += "," + eleList2[j];
                            eleList1.append(",").append( eleList2[j]);
                        }
                        if (j == eleList2.length - 2) {
//                            eleList1 += "," + eleList2[j + 1];
                            eleList1.append(",").append( eleList2[j + 1]);
                        }
                    }
//                    if (!eleList1.equalsIgnoreCase("")) {
                    if (eleList1!=null && eleList1.length()>0) {
                        eleList1 = new StringBuilder(eleList1.substring(1));
                    }
                } else {
                    eleList1 = new StringBuilder(eleList2[0]);
                }
                String eleList3[] = eleList1.toString().split(",");
                String dependenteleids = "";
                for (int p = 0; p < eleList3.length; p++) {
                    boolean check = tArea.contains(eleList3[p]);
                    if (check == true) {
                        dependenteleids += "," + eleList3[p];
                    }
                    if (p == eleList3.length - 1) {
                        dependenteleids = dependenteleids.substring(1);
                    }
                }
                String tableList = "";
                String dependenteleidsList[] = dependenteleids.split(",");
                for (int n = 0; n < dependenteleidsList.length; n++) {
                    String nameQuery = "select distinct DISP_NAME,BUSS_COL_NAME from PRG_USER_ALL_INFO_DETAILS where element_id in(" + dependenteleidsList[n] + ")";
                    PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
                    tableList += "," + pbro.getFieldValueString(0, 0);
                    String querypart = pbro.getFieldValueString(0, 0) + "." + pbro.getFieldValueString(0, 1);
                    String val = "~" + dependenteleidsList[n];
                    tArea = tArea.replaceAll(val, querypart);
                    if (n == dependenteleidsList.length - 1) {
                        tableList = tableList.substring(1);
                    }
                }
//                String tList = "";
                StringBuilder tList = new StringBuilder();
                if (tableList.equalsIgnoreCase("")) {
                } else {
                    String tabList[] = tableList.split(",");
                    if (tabList.length > 1) {
                        for (int m = 0; m < tabList.length - 1; m++) {
                            int c = 0;
                            for (int m1 = m + 1; m1 < tabList.length; m1++) {
                                if (tabList[m].equalsIgnoreCase(tabList[m1])) {
                                    c = 1;
                                    break;
                                }
                            }
                            if (c == 0) {
//                                tList += "," + tabList[m];
                                tList.append(",").append( tabList[m]);
                            }
                            if (m == tabList.length - 2) {
//                                tList += "," + tabList[m + 1];
                                tList.append( ",").append( tabList[m + 1]);
                            }
                        }
                        if (tList!=null && tList.length()>0) {
                            tList = new StringBuilder(tList.substring(1));
                        }
                    } else {
//                        tList = tabList[0];
                        tList =new StringBuilder( tabList[0]);
                    }
                }
                String finalQuery = "select " + tArea + " from " + tList;
                BusinessGroupDAO bd = new BusinessGroupDAO();
                Connection con = bd.getConnectionIdConnection(ConnectionId);
                Statement st = con.createStatement();
                rs = st.executeQuery(finalQuery);
                if (con != null) {
                    con.close();
                }
            }
        } catch (SQLException e) {
            if (rs == null) {
                i = 1;
            }

            logger.error("Exception:", e);
        }
        if (i == 1) {
            response.getWriter().print("Formula you entered is incorrect");
        } else {
            response.getWriter().print("Correct");
        }

        return null;
    }

    public ActionForward saveRoleFormula(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PbUserLayerResourceBundle resBundle = new PbUserLayerResourceBundle();
        PbDb pbdb = new PbDb();
        String finalQuery = "";

        try {
            String columnFormula = request.getParameter("txt2");
            columnFormula = columnFormula.replace("@", "+");
//            String folderIds = request.getParameter("folderTabId");
            String columnName = request.getParameter("columnName");
            String iscalculate = request.getParameter("iscalculate");
            String tArea = request.getParameter("tArea").replace("@", "+");
            String tArea1 = request.getParameter("tArea1");
            String grpId = request.getParameter("grpId");
            String connectionId = request.getParameter("connectionId");

            columnName = columnName.trim();
            columnName = columnName.replace("#", "_");
            columnName = columnName.replace("&", "_");
            columnName = columnName.replace("!", "_");
            columnName = columnName.replace("@", "_");
            columnName = columnName.replace("(", "_");
            columnName = columnName.replace(")", "_");
            columnName = columnName.replace("[", "_");
            columnName = columnName.replace("]", "_");
            columnName = columnName.replace("{", "_");
            columnName = columnName.replace("}", "_");
            columnName = columnName.replace(" ", "_");
            String columnNamesList[] = new String[4];
            columnNamesList[0] = columnName;
            columnNamesList[1] = "Prior_" + columnName;
            columnNamesList[2] = "Change_" + columnName;
            columnNamesList[3] = "Change%_" + columnName;

            String columnDescList[] = new String[4];
            columnDescList[0] = columnName.replace("_", " ");
            columnDescList[1] = "Prior " + columnName.replace("_", " ");
            columnDescList[2] = "Change " + columnName.replace("_", " ");
            columnDescList[3] = "Change% " + columnName.replace("_", " ");
            String refEleTypes[] = new String[4];
            refEleTypes[0] = "1";
            refEleTypes[1] = "2";
            refEleTypes[2] = "3";
            refEleTypes[3] = "4";
            int len = 1;
            if (iscalculate.equalsIgnoreCase("Y")) {
                len = 4;
            }

            String tableName = request.getParameter("tableName");

            String aggType = "";
            String colType = "calculated";
            if (columnFormula.indexOf("SUM(") >= 0) {
                aggType = "sum";
                colType = "summarized";

            } else if (columnFormula.indexOf("COUNT(") >= 0) {
                aggType = "count";
                colType = "summarized";

            } else if (columnFormula.indexOf("COUNT(*") >= 0) {
                aggType = "count(*)";
                colType = "summarized";

            } else if (columnFormula.indexOf("AVG(") >= 0) {
                aggType = "avg";
                colType = "summarized";

            } else if (columnFormula.indexOf("COUNT(DISTINCT") >= 0) {
                aggType = "count";
                colType = "summarized";
            } else {
                aggType = "sum";
                colType = "summarized";
            }

            //  String eleList1[]=eleList.split(",");
            String a = tArea1.trim().substring(1);

            String eleList2[] = a.split("~");

//            String eleList1 = "";
            StringBuilder eleList1 = new StringBuilder();
            if (eleList2.length > 1) {
                for (int j = 0; j < eleList2.length - 1; j++) {
                    int count = 0;
                    for (int j1 = j + 1; j1 < eleList2.length; j1++) {
                        //
                        if (eleList2[j].equalsIgnoreCase(eleList2[j1])) {
                            count = 1;
                            break;
                        }
                    }
                    if (count == 0) {
//                        eleList1 += "," + eleList2[j];
                        eleList1.append("," ).append( eleList2[j]);
                    }
                    if (j == eleList2.length - 2) {
//                        eleList1 += "," + eleList2[j + 1];
                        eleList1.append(",").append( eleList2[j+1]);
                    }

                }

//                if (!eleList1.equalsIgnoreCase("")) {
                if (eleList1!=null && eleList1.length()>0) {
                    eleList1 = new StringBuilder(eleList1.substring(1));
                }
            } else {
                eleList1 = new StringBuilder(eleList2[0]);
            }

            String eleList3[] = eleList1.toString().split(",");

//            String dependenteleids = "";
            StringBuilder dependenteleids = new StringBuilder();
            for (int p = 0; p < eleList3.length; p++) {
                boolean check = tArea.contains(eleList3[p]);
                if (check == true) {
//                    dependenteleids += "," + eleList3[p];
                    dependenteleids.append(",").append( eleList3[p]);
                }
                if (p == eleList3.length - 1) {
                    dependenteleids =new StringBuilder( dependenteleids.substring(1));
                }
            }
            String doubleExistQuery = " SELECT distinct BUSS_TABLE_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS  where element_id in(" + dependenteleids + ")";
            PbReturnObject doubleExistQuerypbro = pbdb.execSelectSQL(doubleExistQuery);
            if (doubleExistQuerypbro.getRowCount() > 1) {
                // colType = "sum";
                String foldersQuery = "select folder_id,folder_name from prg_user_folder where grp_id=" + grpId;
                PbReturnObject folderpbro = pbdb.execSelectSQL(foldersQuery);
//                String countQuery = "";
                StringBuilder countQuery = new StringBuilder();
                String folderId = "";
                String folderName = "";
                String subFolderId = "";
                String bussTableId = "";
                String subFoldertabId = "";
                String count = "";
//                String detQuery = "";
                StringBuilder detQuery = new StringBuilder();
                PbReturnObject countpbro = null;
                PbReturnObject detpbro = null;
                ArrayList list = new ArrayList();
                for (int i = 0; i < folderpbro.getRowCount(); i++) {
                    folderId = String.valueOf(folderpbro.getFieldValueInt(i, 0));
                    folderName = folderpbro.getFieldValueString(i, 0);

                    String nameQuery = " SELECT t.DISP_NAME,NVL(e.BUSS_COL_NAME,e.user_col_name)   FROM PRG_USER_SUB_FOLDER_ELEMENTS e, "
                            + " PRG_USER_SUB_FOLDER_TABLES t   WHERE e.element_id in(" + dependenteleids + ") AND t.sub_folder_tab_id = e.sub_folder_tab_id";
                    PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
                    String bussNamecolName = "";
                    String onlyColName = "";
                    for (int n = 0; n < pbro.getRowCount(); n++) {
                        bussNamecolName = pbro.getFieldValueString(n, 0) + "." + pbro.getFieldValueString(n, 1);
                        onlyColName = pbro.getFieldValueString(n, 1);
                        columnFormula = columnFormula.replace(bussNamecolName, onlyColName);
                    }
                    String bussTableIds[] = new String[doubleExistQuerypbro.getRowCount()];
                    String bussTablecolCount[] = new String[doubleExistQuerypbro.getRowCount()];
                    String colListarr[] = new String[doubleExistQuerypbro.getRowCount()];
                    for (int j = 0; j < doubleExistQuerypbro.getRowCount(); j++) {
//                        String colNamesList = "";
                        StringBuilder colNamesList = new StringBuilder();
                        String sql = "SELECT ELEMENT_ID, BUSS_TABLE_ID,NVL(BUSS_COL_NAME,user_col_name) FROM PRG_USER_SUB_FOLDER_ELEMENTS where BUSS_TABLE_ID in(" + doubleExistQuerypbro.getFieldValueInt(j, 0) + ") and element_id in(" + dependenteleids + ") ";
                        PbReturnObject pbrocount = pbdb.execSelectSQL(sql);
                        bussTableIds[j] = String.valueOf(doubleExistQuerypbro.getFieldValueInt(j, 0));
                        bussTablecolCount[j] = String.valueOf(pbrocount.getRowCount());
                        for (int k = 0; k < pbrocount.getRowCount(); k++) {
//                            colNamesList += ",'" + pbrocount.getFieldValueString(k, 2) + "'";
                            colNamesList.append( ",'" ).append( pbrocount.getFieldValueString(k, 2)).append( "'");
                            if (k == pbrocount.getRowCount() - 1) {
                                colNamesList = new StringBuilder(colNamesList.substring(1));
                            }
                        }
                        colListarr[j] = colNamesList.toString();
                    }
                    int result = 0;
                    for (int j = 0; j < bussTableIds.length; j++) {
//                        countQuery = " SELECT COUNT(e.BUSS_COL_NAME)   FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id";
                        countQuery.append(" SELECT COUNT(e.BUSS_COL_NAME)   FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id");
//                        countQuery += " FROM prg_user_folder_detail    WHERE folder_id IN(" + folderId + ")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(";
                        countQuery.append( " FROM prg_user_folder_detail    WHERE folder_id IN(" ).append(folderId).append(")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(");
//                        countQuery += colListarr[j] + ") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=" + bussTableIds[j];
                        countQuery.append( colListarr[j]).append(") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=" ).append( bussTableIds[j]);
                        countpbro = pbdb.execSelectSQL(countQuery.toString());
                        count = String.valueOf(countpbro.getFieldValueInt(0, 0));
                        if (count.equalsIgnoreCase(bussTablecolCount[j])) {
                            result = result + 1;
                        }
                    }
                    if (bussTableIds.length == result) {
//                        detQuery = "select distinct e.SUB_FOLDER_ID, e.BUSS_TABLE_ID, e.SUB_FOLDER_TAB_ID  FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id";
                        detQuery.append( "select distinct e.SUB_FOLDER_ID, e.BUSS_TABLE_ID, e.SUB_FOLDER_TAB_ID  FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id");
//                        detQuery += " FROM prg_user_folder_detail    WHERE folder_id IN(" + folderId + ")  AND sub_folder_type='Facts'  ) AND t.DISP_NAME='Calculated Facts' and t.sub_folder_tab_id= e.sub_folder_tab_id";
                        detQuery.append(" FROM prg_user_folder_detail    WHERE folder_id IN(").append( folderId ).append( ")  AND sub_folder_type='Facts'  ) AND t.DISP_NAME='Calculated Facts' and t.sub_folder_tab_id= e.sub_folder_tab_id");
                        detpbro = pbdb.execSelectSQL(detQuery.toString());
                        //adding to subfolder element types
                        if (detpbro.getRowCount() > 0) {
                            subFolderId = String.valueOf(detpbro.getFieldValueInt(0, 0));
                            bussTableId = String.valueOf(detpbro.getFieldValueInt(0, 1));
                            subFoldertabId = String.valueOf(detpbro.getFieldValueInt(0, 2));
                            String ref_elementId = "";
                            for (int k = 0; k < len; k++) {
                                String addSubFolderElements = resBundle.getString("addSubFolderElements");
                                String elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                                PbReturnObject pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                                String elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));
                                if (k == 0) {
                                    ref_elementId = elementId;
                                }
                                Object obj1[] = new Object[16];
                                obj1[0] = elementId;
                                obj1[1] = subFolderId;
                                obj1[2] = bussTableId;
                                obj1[3] = "0";
                                obj1[4] = columnName;
                                obj1[5] = columnNamesList[k];
                                obj1[6] = columnDescList[k];
                                obj1[7] = colType;//"calculated";
                                obj1[8] = subFoldertabId;
                                obj1[9] = ref_elementId;
                                obj1[10] = refEleTypes[k];
                                obj1[11] = aggType;
                                obj1[12] = "null";
                                obj1[13] = "Y";
                                obj1[14] = dependenteleids;
                                obj1[15] = columnFormula;
                                finalQuery = pbdb.buildQuery(addSubFolderElements, obj1);
                                list.add(finalQuery);
                                //adding to user all Info
                                String addUserAllInfoDets = resBundle.getString("addUserAllInfoDets");

                                Object obj2[] = new Object[29];
                                obj2[0] = grpId;
                                obj2[1] = folderId;
                                obj2[2] = folderName;
                                obj2[3] = subFolderId;
                                obj2[4] = "Facts";
                                obj2[5] = "Facts";
                                obj2[6] = subFoldertabId;
                                obj2[7] = "N";
                                obj2[8] = "Y";
                                obj2[9] = "N";
                                obj2[10] = tableName;

                                obj2[11] = " ";
                                obj2[12] = elementId;
                                obj2[13] = bussTableId;
                                obj2[14] = "0";
                                obj2[15] = columnName;
                                obj2[16] = columnNamesList[k];
                                obj2[17] = columnDescList[k];
                                obj2[18] = colType;// "calculated";
                                obj2[19] = ref_elementId;
                                obj2[20] = refEleTypes[k];
                                obj2[21] = "0";
                                obj2[22] = "null";
                                obj2[23] = "null";
                                obj2[24] = "null";
                                obj2[25] = connectionId;
                                obj2[26] = aggType;
                                obj2[27] = columnFormula;
                                obj2[28] = dependenteleids;
                                finalQuery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                                list.add(finalQuery);
                            }
                        } else {

                            String subFolderidQuery = "SELECT  SUB_FOLDER_ID FROM PRG_USER_FOLDER_DETAIL where FOLDER_ID=" + folderId + " and  SUB_FOLDER_TYPE='Facts'";
                            PbReturnObject pbroSubFolderId = pbdb.execSelectSQL(subFolderidQuery);
                            subFolderId = String.valueOf(pbroSubFolderId.getFieldValueInt(0, 0));
                            PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
                            String addFormulaBussMater = resourceBundle.getString("addFormulaBussMater");
                            int seqaddFormulaBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                            Object obj[];
                            obj = new Object[8];
                            obj[0] = seqaddFormulaBussMater;
                            obj[1] = "Calculated Facts";
                            obj[2] = "Calculated Facts";
                            obj[3] = "Table";
                            obj[4] = "1";
                            obj[5] = "null";
                            obj[6] = "null";
                            obj[7] = grpId;
                            finalQuery = pbdb.buildQuery(addFormulaBussMater, obj);
                            list.add(finalQuery);
                            String addFormulaSrc = resourceBundle.getString("addFormulaSrc");
                            int seqaddFormulaSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                            Object obj4[];
                            obj4 = new Object[7];
                            obj4[0] = seqaddFormulaSrc;
                            obj4[1] = seqaddFormulaBussMater;
                            obj4[2] = "0";
                            obj4[3] = "Table";
                            obj4[4] = "null";
                            obj4[5] = connectionId;
                            obj4[6] = "null";
                            finalQuery = pbdb.buildQuery(addFormulaSrc, obj4);
                            list.add(finalQuery);
                            //add grp buss src details
                            int srcdetnextval = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                            String addFormulasrcDetails = resourceBundle.getString("addFormulasrcDetails");
                            Object obj5[] = new Object[7];
                            obj5[0] = srcdetnextval;
                            obj5[1] = seqaddFormulaSrc;
                            obj5[2] = "0";
                            obj5[3] = seqaddFormulaBussMater;
                            obj5[4] = columnName;
                            obj5[5] = colType;// "summarised"; for two tables
                            obj5[6] = "0";

                            finalQuery = pbdb.buildQuery(addFormulasrcDetails, obj5);
                            //list.add(finalQuery);
                            String addFormulaBussDetails = resourceBundle.getString("addFormulaBussDetails");
                            int seqaddFormulaBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                            Object obj3[] = new Object[13];
                            obj3[0] = seqaddFormulaBussDetails;
                            obj3[1] = seqaddFormulaBussMater;
                            obj3[2] = columnName;
                            obj3[3] = "0";
                            obj3[4] = srcdetnextval;
                            obj3[5] = colType;// "summarised"; for two tables
                            obj3[6] = "0";
                            obj3[7] = columnName;
                            obj3[8] = "0";
                            obj3[9] = "N";
                            obj3[10] = columnFormula;
                            obj3[11] = aggType;
                            obj3[12] = columnName;
                            finalQuery = pbdb.buildQuery(addFormulaBussDetails, obj3);
                            // list.add(finalQuery);
                            bussTableId = String.valueOf(seqaddFormulaBussMater);
                            //add subfolder Table Details
                            String addSubFolderTables = resBundle.getString("addSubFolderTables");
                            String addSubFolderTablesIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                            PbReturnObject pbrofoldertable = pbdb.execSelectSQL(addSubFolderTablesIdQuery);
                            subFoldertabId = String.valueOf(pbrofoldertable.getFieldValueInt(0, 0));
                            Object obj6[] = new Object[4];
                            obj6[0] = subFolderId;
                            obj6[1] = bussTableId;
                            obj6[2] = "Calculated Facts";
                            obj6[3] = subFoldertabId;
                            finalQuery = pbdb.buildQuery(addSubFolderTables, obj6);
                            list.add(finalQuery);

                            //subFoldertabId = String.valueOf(detpbro.getFieldValueInt(0, 2));
                            String ref_elementId = "";
                            for (int k = 0; k < len; k++) {

                                String addSubFolderElements = resBundle.getString("addSubFolderElements");
                                String elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                                PbReturnObject pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                                String elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));
                                if (k == 0) {
                                    ref_elementId = elementId;
                                }
                                Object obj1[] = new Object[16];
                                obj1[0] = elementId;
                                obj1[1] = subFolderId;
                                obj1[2] = bussTableId;
                                obj1[3] = "0";
                                obj1[4] = columnName;
                                obj1[5] = columnNamesList[k];
                                obj1[6] = columnDescList[k];
                                obj1[7] = colType;// "summarised"; for two tables
                                obj1[8] = subFoldertabId;
                                obj1[9] = ref_elementId;
                                obj1[10] = refEleTypes[k];
                                obj1[11] = aggType;
                                obj1[12] = "null";
                                obj1[13] = "Y";
                                obj1[14] = dependenteleids;
                                obj1[15] = columnFormula;
                                finalQuery = pbdb.buildQuery(addSubFolderElements, obj1);
                                list.add(finalQuery);
                                //adding to user all Info
                                String addUserAllInfoDets = resBundle.getString("addUserAllInfoDets");
                                Object obj2[] = new Object[29];
                                obj2[0] = grpId;
                                obj2[1] = folderId;
                                obj2[2] = folderName;
                                obj2[3] = subFolderId;
                                obj2[4] = "Facts";
                                obj2[5] = "Facts";
                                obj2[6] = subFoldertabId;
                                obj2[7] = "N";
                                obj2[8] = "Y";
                                obj2[9] = "N";
                                obj2[10] = tableName;

                                obj2[11] = "null";
                                obj2[12] = elementId;
                                obj2[13] = bussTableId;
                                obj2[14] = "0";
                                obj2[15] = columnName;
                                obj2[16] = columnNamesList[k];
                                obj2[17] = columnDescList[k];
                                obj2[18] = colType;// "summarised"; for two tables
                                obj2[19] = ref_elementId;
                                obj2[20] = refEleTypes[k];
                                obj2[21] = "0";
                                obj2[22] = "null ";
                                obj2[23] = "null";
                                obj2[24] = "null";
                                obj2[25] = connectionId;
                                obj2[26] = aggType;
                                obj2[27] = columnFormula;
                                obj2[28] = dependenteleids;
                                finalQuery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                                list.add(finalQuery);
                            }
                        }
                    }
                }
                pbdb.executeMultiple(list);
            } else {
                //String dependenteleidsList[] = dependenteleids.split(",");
//                String colNamesList = "";
                StringBuilder colNamesList = new StringBuilder();
                String nameQuery = " SELECT e.ELEMENT_ID,  t.disp_name,   NVL(BUSS_COL_NAME,user_col_name)   FROM PRG_USER_SUB_FOLDER_ELEMENTS e, "
                        + " PRG_USER_SUB_FOLDER_TABLES t   WHERE e.element_id in(" + dependenteleids + ") AND t.sub_folder_tab_id = e.sub_folder_tab_id";
                PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
//                String bussNamecolName = "";
//                String onlyColName = "";
                for (int n = 0; n < pbro.getRowCount(); n++) {
                    // bussNamecolName = pbro.getFieldValueString(n, 1) + "." + pbro.getFieldValueString(n, 2);
                    // onlyColName = pbro.getFieldValueString(n, 2);
                    //  columnFormula = columnFormula.replace(bussNamecolName, onlyColName);
//                    colNamesList += ",'" + pbro.getFieldValueString(n, 2) + "'";
                    colNamesList.append(",'" ).append( pbro.getFieldValueString(n, 2) ).append( "'");
                }
//                if (!colNamesList.equalsIgnoreCase("")) {
                if (colNamesList.length()>0) {
                    colNamesList = new StringBuilder(colNamesList.substring(1));
                }
                // colType = "sum";

                String foldersQuery = "select folder_id,folder_name from prg_user_folder where grp_id=" + grpId;
                PbReturnObject folderpbro = pbdb.execSelectSQL(foldersQuery);
//                String countQuery = "";
                StringBuilder countQuery = new StringBuilder();
                String folderId = "";
                String folderName = "";
                String subFolderId = "";
                String bussTableId = "";
                String subFoldertabId = "";
                int count = 0;
//                String detQuery = "";
                StringBuilder detQuery = new StringBuilder();
                PbReturnObject countpbro = null;
                PbReturnObject detpbro = null;
                ArrayList list = new ArrayList();
                for (int i = 0; i < folderpbro.getRowCount(); i++) {
                    folderId = String.valueOf(folderpbro.getFieldValueInt(i, 0));
                    folderName = folderpbro.getFieldValueString(i, 0);

//                    countQuery = " SELECT COUNT(e.BUSS_COL_NAME)   FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id";
                    countQuery.append(" SELECT COUNT(e.BUSS_COL_NAME)   FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id");
//                    countQuery += " FROM prg_user_folder_detail    WHERE folder_id IN(" + folderId + ")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(";
                    countQuery.append(" FROM prg_user_folder_detail    WHERE folder_id IN(").append(folderId ).append( ")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(");
//                    countQuery += colNamesList + ") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=" + doubleExistQuerypbro.getFieldValueInt(0, 0);
                    countQuery.append( colNamesList).append(") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=" ).append( doubleExistQuerypbro.getFieldValueInt(0, 0));
                    countpbro = pbdb.execSelectSQL(countQuery.toString());
                    count = countpbro.getFieldValueInt(0, 0);
                    if (count == pbro.getRowCount()) {
//                        detQuery = "select distinct e.SUB_FOLDER_ID, e.BUSS_TABLE_ID, e.SUB_FOLDER_TAB_ID  FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id";
                        detQuery.append("select distinct e.SUB_FOLDER_ID, e.BUSS_TABLE_ID, e.SUB_FOLDER_TAB_ID  FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id");
//                        detQuery += " FROM prg_user_folder_detail    WHERE folder_id IN(" + folderId + ")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(";
                        detQuery.append( " FROM prg_user_folder_detail    WHERE folder_id IN(").append( folderId ).append( ")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(");
//                        detQuery += colNamesList + ") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=" + doubleExistQuerypbro.getFieldValueInt(0, 0);
                        detQuery.append(colNamesList ).append( ") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=").append(doubleExistQuerypbro.getFieldValueInt(0, 0));
                        detpbro = pbdb.execSelectSQL(detQuery.toString());
                        //adding to subfolder element types
                        subFolderId = String.valueOf(detpbro.getFieldValueInt(0, 0));
                        bussTableId = String.valueOf(detpbro.getFieldValueInt(0, 1));
                        subFoldertabId = String.valueOf(detpbro.getFieldValueInt(0, 2));
                        String ref_elementId = "";
                        for (int k = 0; k < len; k++) {
                            String addSubFolderElements = resBundle.getString("addSubFolderElements");
                            String elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                            PbReturnObject pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                            String elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));
                            if (k == 0) {
                                ref_elementId = elementId;
                            }
                            Object obj1[] = new Object[16];
                            obj1[0] = elementId;
                            obj1[1] = subFolderId;
                            obj1[2] = bussTableId;
                            obj1[3] = "0";
                            obj1[4] = columnName;
                            obj1[5] = columnNamesList[k];
                            obj1[6] = columnDescList[k];
                            obj1[7] = colType;//"calculated";
                            obj1[8] = subFoldertabId;
                            obj1[9] = ref_elementId;
                            obj1[10] = refEleTypes[k];
                            obj1[11] = aggType;
                            obj1[12] = "null";
                            obj1[13] = "Y";
                            obj1[14] = dependenteleids;
                            obj1[15] = columnFormula;
                            finalQuery = pbdb.buildQuery(addSubFolderElements, obj1);
                            list.add(finalQuery);
                            //adding to user all Info
                            String addUserAllInfoDets = resBundle.getString("addUserAllInfoDets");
                            Object obj2[] = new Object[29];
                            obj2[0] = grpId;
                            obj2[1] = folderId;
                            obj2[2] = folderName;
                            obj2[3] = subFolderId;
                            obj2[4] = "Facts";
                            obj2[5] = "Facts";
                            obj2[6] = subFoldertabId;
                            obj2[7] = "N";
                            obj2[8] = "Y";
                            obj2[9] = "N";
                            obj2[10] = tableName;

                            obj2[11] = "null";
                            obj2[12] = elementId;
                            obj2[13] = bussTableId;
                            obj2[14] = "0";
                            obj2[15] = columnName;
                            obj2[16] = columnNamesList[k];
                            obj2[17] = columnDescList[k];
                            obj2[18] = colType;//"calculated";
                            obj2[19] = ref_elementId;
                            obj2[20] = refEleTypes[k];
                            obj2[21] = "0";
                            obj2[22] = "null";
                            obj2[23] = "null";
                            obj2[24] = "null";
                            obj2[25] = connectionId;
                            obj2[26] = aggType;
                            obj2[27] = columnFormula;
                            obj2[28] = dependenteleids;
                            finalQuery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                            list.add(finalQuery);
                        }
                    }
                }
                pbdb.executeMultiple(list);
            }
            response.getWriter().print("Correct");
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            response.getWriter().print("not correct");
        }
        return null;
    }
    // susheela start 10-12-09 addUserDimMemberValues

    public ActionForward addUserDimMemberValuesForRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String subFolderIdUser = request.getParameter("subFolderIdUser");
        String allVal = request.getParameter("meemberValues");
        String userMemId = request.getParameter("userMemId");
        String elementId = request.getParameter("elementId");
        String subFolderId = request.getParameter("subFolderId");
        String dimId = request.getParameter("dimId");
        HttpSession session = request.getSession();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String memberValuesXml = "";
        String[] meemberValues = null;
//        if (allVal.length() > 1) {
//            allVal = allVal.replaceAll(";", "'||chr(38)||'");
//        }

        if (!allVal.equalsIgnoreCase("")) {
            allVal = allVal.replace("%20", " ").replace("%26", "&").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%40", "@");
            allVal = allVal.replace("%23", "#").replace("%24", "$").replace("%25", "%").replace("%5E", "^").replace("%60", "`").replace("%3C", "<").replace("%3E", ">");
            meemberValues = allVal.split(",");
        }
        if (meemberValues != null) {
            memberValuesXml = userLayerDAO.toXMLSec(meemberValues);
            int len = memberValuesXml.length();
            memberValuesXml = memberValuesXml.substring(0, len - 1);
        }
        String folderId = userLayerDAO.getFolderID(Integer.parseInt(subFolderIdUser));

        String status = "";
        HashMap details = userLayerDAO.checkMemberValueStatusForRole(folderId, userMemId);
        String filterId = (String) details.get("filterId");
        status = (String) details.get("status");
        if (status.equalsIgnoreCase("false")) {
            userLayerDAO.addUserMemberFilterForRole(folderId, memberValuesXml, userMemId, elementId, subFolderId, dimId);
        } else if (status.equalsIgnoreCase("true")) {
            userLayerDAO.updateMemberValueStatusForRole(folderId, userMemId, memberValuesXml, filterId, elementId, subFolderId, dimId);
        }

        String connectionId = (String) session.getAttribute("connId");
        MetadataDAO metadataDAO = new MetadataDAO();
        ArrayList<Cube> cubes = (ArrayList<Cube>) metadataDAO.getUserFolderList(connectionId);
        session.setAttribute("CUBES", cubes);
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);

    }

    public ActionForward deleteDimMemberValuesForRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String totalUrl = request.getParameter("totalUrl");
        String totalUrlVal[] = totalUrl.split("~");
        String folderId = "";
        String memId = "";
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        HashMap vals = new HashMap();
        for (int g = 0; g < totalUrlVal.length; g++) {
            String indParam = totalUrlVal[g];
            String indVal[] = indParam.split("=");
            if (g == 0) {
                memId = indVal[1];
            } else if (g == 1) {
                folderId = indVal[1];
            }
        }
        userLayerDAO.deleteDimMemberValuesForRole(folderId, memId);
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }

    public ActionForward migrateChangesToRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String grp = request.getParameter("grpId");
        String roles = request.getParameter("roles");
        String roleIds[] = roles.split(",");
        String roleIdValues[] = new String[roleIds.length];
        for (int m = 0; m < roleIds.length; m++) {
            String val[] = roleIds[m].split("~");
            roleIdValues[m] = val[0];
        }
        PbUserLayerEditDAO userLayerDAO = new PbUserLayerEditDAO();
        userLayerDAO.migrateToBussRole(grp, roleIdValues);
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }

    public ActionForward migrateChangesToRolePublish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String grp = request.getParameter("grpId");
        String roles = request.getParameter("roles");
        String roleIds[] = roles.split(",");
        String roleIdValues[] = new String[roleIds.length];
        for (int m = 0; m < roleIds.length; m++) {
            String val[] = roleIds[m].split("~");
            roleIdValues[m] = val[0];
        }
        PbUserLayerEditDAO userLayerDAO = new PbUserLayerEditDAO();
        userLayerDAO.migrateToBussRole(grp, roleIdValues);
        SavePublishUserFolder publish = new SavePublishUserFolder();
        for (int m = 0; m < roleIdValues.length; m++) {
            String roleId = roleIdValues[m];
            publish.publishUserFolder(roleId);
        }
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }

    public ActionForward migrateChangesToRoleNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String grp = request.getParameter("grpId");
        String totalUrl = request.getParameter("totalUrl");
        String totalurlFactCols = request.getParameter("totalurlFactCols");
        String factsVal = request.getParameter("factsVal");
        String cols = request.getParameter("cols");
        totalurlFactCols = cols;
        if (totalurlFactCols.length() > 0) {
            totalurlFactCols = totalurlFactCols.substring(1);
        }
        if (totalUrl.length() > 0) {
            totalUrl = totalUrl.substring(1);
        }
        String roleIds[] = totalUrl.split("~");
        String dimVals[] = new String[roleIds.length];
        if (totalUrl.length() == 0) {
            roleIds = null;
        }
        String roleIdValues[] = null;
        if (roleIds != null) {
            roleIdValues = new String[roleIds.length];
        }

        if (roleIds != null) {
            for (int m = 0; m < roleIds.length; m++) {
                String val[] = roleIds[m].split("=");
                String dim = val[1];
                if (dim.length() > 0) {
                    dim = dim.substring(1);
                }
                dimVals[m] = dim;
                if (val[1].length() > 0) {
                    roleIdValues[m] = val[0];
                }
            }
        }
        PbUserLayerEditDAO userLayerDAO = new PbUserLayerEditDAO();
        UserLayerDAO dao = new UserLayerDAO();
        dao.truncateDimValues();
        dao.insertNewDimValues();
        if (roleIdValues != null) {
            for (int m = 0; m < roleIdValues.length; m++) {
                userLayerDAO.migrateToBussRoleNew(grp, roleIdValues, dimVals);
            }
        }

        String roleForFacts[] = totalurlFactCols.split(",");
        ArrayList ColumnIds = new ArrayList();
        for (int m = 0; m < roleForFacts.length; m++) {
            String rolId = "";
            String val = roleForFacts[m];
            String ndVal[] = val.split(":");
            String colId = "";
            String tabId = "";
            String tabCol = "";
            rolId = ndVal[0];
            if (ndVal.length == 2) {
                tabCol = ndVal[1];
            }
            String v[] = tabCol.split("-");
            if (v.length == 2) {
                colId = v[1];
                tabId = v[0];
            }
            if (!colId.equalsIgnoreCase("")) {
                if (!ColumnIds.contains(colId)) {
                    ColumnIds.add(colId);
                    userLayerDAO.migrateExtraColumnToRole(rolId, tabId, colId);
                }
            }
        }
        if (factsVal.length() > 0) {
            factsVal = factsVal.substring(1);
        }
        String Extrafacts[] = factsVal.split(",");
       if (factsVal.length() > 0) {
            for (int i = 0; i < Extrafacts.length; i++) {
                String a = Extrafacts[i];
                String roleId = "";
                String factId = "";
                String bb[] = a.split(":");
                roleId = bb[0];
                factId = bb[1];
                userLayerDAO.addExtraFact(factId, "", roleId);
            }
        }
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }

    public ActionForward migrateChangesToRoleNewPublish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String grp = request.getParameter("grpId");
        String totalUrl = request.getParameter("totalUrl");
        String totalurlFactCols = request.getParameter("totalurlFactCols");
        if (totalurlFactCols.length() > 0) {
            totalurlFactCols = totalurlFactCols.substring(1);
        }
        if (totalUrl.length() > 0) {
            totalUrl = totalUrl.substring(1);
        }
       String roleIds[] = totalUrl.split("~");

        String dimVals[] = new String[roleIds.length];
        if (totalUrl.length() == 0) {
            roleIds = null;
        }
        String roleIdValues[] = null;
        if (roleIds != null) {
            roleIdValues = new String[roleIds.length];
        }

        if (roleIds != null) {
            for (int m = 0; m < roleIds.length; m++) {
                 String val[] = roleIds[m].split("=");
                String dim = val[1];

                if (dim.length() > 0) {
                    dim = dim.substring(1);
                }
                dimVals[m] = dim;
                if (val[1].length() > 0) {
                    roleIdValues[m] = val[0];
                }
            }
        }
        PbUserLayerEditDAO userLayerDAO = new PbUserLayerEditDAO();
        if (roleIdValues != null) {
            for (int m = 0; m < roleIdValues.length; m++) {
                userLayerDAO.migrateToBussRoleNew(grp, roleIdValues, dimVals);
            }
        }

        String roleForFacts[] = totalurlFactCols.split(",");
        ArrayList ColumnIds = new ArrayList();
        for (int m = 0; m < roleForFacts.length; m++) {
            String rolId = "";
            String val = roleForFacts[m];
            String ndVal[] = val.split(":");
            String colId = "";
            String tabId = "";
            String tabCol = "";
            rolId = ndVal[0];
            if (ndVal.length == 2) {
                tabCol = ndVal[1];
            }

            String v[] = tabCol.split("-");
            if (v.length == 2) {
                colId = v[1];
                tabId = v[0];
            }
            if (!colId.equalsIgnoreCase("")) {
                if (!ColumnIds.contains(colId)) {
                    ColumnIds.add(colId);
                    userLayerDAO.migrateExtraColumnToRole(rolId, tabId, colId);
                }
            }
        }
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }

    public ActionForward saveRoleParamFormula(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PbUserLayerResourceBundle resBundle = new PbUserLayerResourceBundle();
        PbDb pbdb = new PbDb();
        String finalQuery = "";
        ArrayList list = new ArrayList();
        try {
            String columnFormula = request.getParameter("txt2");
            columnFormula = columnFormula.replace("@", "+");
//            String folderIds = request.getParameter("folderTabId");
            String columnName = request.getParameter("columnName");
            String iscalculate = request.getParameter("iscalculate");
            String tArea = request.getParameter("tArea").replace("@", "+");
            String tArea1 = request.getParameter("tArea1");
//            String factSubFoldertabId = request.getParameter("factSubFoldertabId");
            // String grpId = request.getParameter("grpId");

            String UpdatedFormula = tArea;
            String dimSubFolderId = request.getParameter("dimSubFolderId");
//            String factSubFolderId = request.getParameter("factSubFolderId");
            String folderId = request.getParameter("folderId");
            String factBussTableId = request.getParameter("factBussTableId");

            String tableName = request.getParameter("tableName");

            columnName = columnName.trim();
            columnName = columnName.replace("#", "_");
            columnName = columnName.replace("&", "_");
            columnName = columnName.replace("!", "_");
            columnName = columnName.replace("@", "_");
            columnName = columnName.replace("(", "_");
            columnName = columnName.replace(")", "_");
            columnName = columnName.replace("[", "_");
            columnName = columnName.replace("]", "_");
            columnName = columnName.replace("{", "_");
            columnName = columnName.replace("}", "_");
            columnName = columnName.replace(" ", "_");
            String columnNamesList[] = new String[4];
            columnNamesList[0] = columnName;
            columnNamesList[1] = "Prior_" + columnName;
            columnNamesList[2] = "Change_" + columnName;
            columnNamesList[3] = "Change%_" + columnName;

            String columnDescList[] = new String[4];
            columnDescList[0] = columnName.replace("_", " ");
            columnDescList[1] = "Prior " + columnName.replace("_", " ");
            columnDescList[2] = "Change " + columnName.replace("_", " ");
            columnDescList[3] = "Change% " + columnName.replace("_", " ");
            String refEleTypes[] = new String[4];
            refEleTypes[0] = "1";
            refEleTypes[1] = "2";
            refEleTypes[2] = "3";
            refEleTypes[3] = "4";
            int len = 1;
            if (iscalculate.equalsIgnoreCase("Y")) {
                len = 4;
            }

            String aggType = "";
            String colType = "Calculated";
            if (columnFormula.indexOf("SUM(") >= 0) {
                aggType = "sum";
                colType = "summarized";

            } else if (columnFormula.indexOf("COUNT(") >= 0) {
                aggType = "count";
                colType = "summarized";

            } else if (columnFormula.indexOf("COUNT(*") >= 0) {
                aggType = "count(*)";
                colType = "summarized";

            } else if (columnFormula.indexOf("AVG(") >= 0) {
                aggType = "avg";
                colType = "summarized";

            } else if (columnFormula.indexOf("COUNT(DISTINCT") >= 0) {
                aggType = "count";
                colType = "summarized";
            } else {
                aggType = "sum";
                colType = "summarized";
            }
            //  String eleList1[]=eleList.split(",");
            String a = tArea1.trim().substring(1);
            String eleList2[] = a.split("~");
//            String eleList1 = "";
            StringBuilder eleList1 = new StringBuilder();
            if (eleList2.length > 1) {
                for (int j = 0; j < eleList2.length - 1; j++) {
                    int count = 0;
                    for (int j1 = j + 1; j1 < eleList2.length; j1++) {
                        if (eleList2[j].equalsIgnoreCase(eleList2[j1])) {
                            count = 1;
                            break;
                        }
                    }
                    if (count == 0) {
//                        eleList1 += "," + eleList2[j];
                        eleList1.append(",").append(eleList2[j]);
                    }
                    if (j == eleList2.length - 2) {
//                        eleList1 += "," + eleList2[j + 1];
                        eleList1.append(",").append(eleList2[j + 1]);
                    }

                }
//                if (!eleList1.equalsIgnoreCase("")) {
                if (eleList1!=null && eleList1.length()>0) {
                    eleList1 = new StringBuilder(eleList1.substring(1));
                }
            } else {
                eleList1 =new StringBuilder(eleList2[0]);
            }

            String eleList3[] = eleList1.toString().split(",");

//            String dependenteleids = "";
            StringBuilder dependenteleids = new StringBuilder();
            for (int p = 0; p < eleList3.length; p++) {
                boolean check = tArea.contains(eleList3[p]);

                if (check == true) {
//                    dependenteleids += "," + eleList3[p];
                    dependenteleids.append("," ).append( eleList3[p]);
                }
                if (p == eleList3.length - 1) {

                    dependenteleids =new StringBuilder( dependenteleids.substring(1));
                }
            }
            String eleIdsList[] = dependenteleids.toString().split(",");
            String memesubTabId = "";
            String memColId = "";
//            String memeIdsList = "";
            StringBuilder memeIdsList = new StringBuilder();
            String eleQuery = "";
            String eleMemstr = "";
            String orielestr = "";
            String str = "";
            String updateddependenteleids = dependenteleids.toString();
            PbReturnObject pbroeleList = null;
            String dimmemtabIds = "";
            String colNamesList = "";
            String elementIdexist = "";
            int elementsCount = 0;
            for (int i = 0; i < eleIdsList.length; i++) {

                if (eleIdsList[i].startsWith("M-")) {

                    String ele[] = eleIdsList[i].substring(2).split("-");
                    memesubTabId = ele[0];
                    memColId = ele[1];
                    eleQuery = "SELECT ELEMENT_ID, SUB_FOLDER_ID,BUSS_COL_ID, SUB_FOLDER_TAB_ID FROM  PRG_USER_SUB_FOLDER_ELEMENTS WHERE SUB_FOLDER_ID=" + dimSubFolderId + " and buss_col_id=" + memColId + " and SUB_FOLDER_TAB_ID=" + memesubTabId;
                    pbroeleList = pbdb.execSelectSQL(eleQuery);
                    orielestr = "~" + eleIdsList[i];
                    eleMemstr = "<!" + pbroeleList.getFieldValueInt(0, 0) + ">";
                    UpdatedFormula = UpdatedFormula.replace(orielestr, eleMemstr);
                    str = eleIdsList[i];
                    updateddependenteleids = updateddependenteleids.replace(str, String.valueOf(pbroeleList.getFieldValueInt(0, 0)));
                    dimmemtabIds += "," + memesubTabId;
//                    memeIdsList += "," + ele[2];
                    memeIdsList.append(",").append(ele[2]);
                } else {
                    elementIdexist = "YES";
                    elementsCount = elementsCount + 1;
                    eleQuery = "SELECT nvl(BUSS_COL_NAME,user_col_name) FROM"
                            + " PRG_USER_SUB_FOLDER_ELEMENTS WHERE  ELEMENT_ID=" + eleIdsList[i];
                    pbroeleList = pbdb.execSelectSQL(eleQuery);
                    orielestr = "~" + eleIdsList[i];
                    eleMemstr = pbroeleList.getFieldValueString(0, 0);
                    UpdatedFormula = UpdatedFormula.replace(orielestr, eleMemstr);
                    colNamesList += ",'" + pbroeleList.getFieldValueString(0, 0) + "'";

                }
            }

            if (!dimmemtabIds.equalsIgnoreCase("")) {
                dimmemtabIds = dimmemtabIds.substring(1);
//                memeIdsList = memeIdsList.substring(1);
                memeIdsList= new StringBuilder(memeIdsList.substring(1));
            }
            if (!colNamesList.equalsIgnoreCase("")) {

                colNamesList = colNamesList.substring(1);
            }

            String folDets = "select f.FOLDER_ID,f.FOLDER_NAME,f.GRP_ID,g.connection_id from PRG_USER_FOLDER  f,"
                    + " prg_grp_master g where folder_id in(" + folderId + ") and f.grp_id= g.grp_id";
            PbReturnObject pbroFolDets = pbdb.execSelectSQL(folDets);
            String grpId = String.valueOf(pbroFolDets.getFieldValueInt(0, 2));
            String connectionId = String.valueOf(pbroFolDets.getFieldValueInt(0, 3));
            String folderName = String.valueOf(pbroFolDets.getFieldValueInt(0, 1));
            //tocheck for other folders get member id of dimension by -member_id through from jsp in column argment  and write query based on that to get exist memder of thst dimension and check fact and that column

            String foldersQuery = "select folder_id,folder_name from prg_user_folder where grp_id=" + grpId;
            String dimQuery = "";
            String dimIdsCount = "";
            PbReturnObject folderpbro = pbdb.execSelectSQL(foldersQuery);
//            String dimIdsList = "";
            StringBuilder dimIdsList =new StringBuilder();
//            if (!memeIdsList.equalsIgnoreCase("")) {
            if (memeIdsList.length()>0) {
                String dimIdsQuery = "select distinct  DIM_ID from PRG_USER_SUB_FOLDER_TABLES  where sub_folder_tab_id in(" + dimmemtabIds + ")";
                PbReturnObject dimIdspbro = pbdb.execSelectSQL(dimIdsQuery);
                dimIdsCount = String.valueOf(dimIdspbro.getRowCount());
                for (int i = 0; i < dimIdspbro.getRowCount(); i++) {
//                    dimIdsList += "," + dimIdspbro.getFieldValueInt(i, 0);
                    dimIdsList.append( ",").append( dimIdspbro.getFieldValueInt(i, 0));
                }
//                if (!dimIdsList.equalsIgnoreCase("")) {
                if (dimIdsList.length()>0) {
                    dimIdsList =new StringBuilder( dimIdsList.substring(1));
                }

            }
//            String countQuery = "";
            StringBuilder countQuery = new StringBuilder();
            PbReturnObject countpbro = null;
            int count = 0;
            for (int i = 0; i < folderpbro.getRowCount(); i++) {
                folderId = String.valueOf(folderpbro.getFieldValueInt(i, 0));
                folderName = folderpbro.getFieldValueString(i, 0);
                String factSubFolderId1 = "";
                String factSubFoldertabId1 = "";
                String factBussTableId1 = "";

                if (dimIdsList.length()>0) {

                    dimQuery = "SELECT distinct  DIM_ID from PRG_USER_SUB_FOLDER_TABLES where USE_REPORT_MEMBER='Y' and  USE_REPORT_DIM_MEMBER='Y' and  SUB_FOLDER_ID in(select sub_folder_id from prg_user_folder_detail where folder_id=" + folderId + " and sub_folder_type='Dimensions')  and dim_id in(" + dimIdsList + ") and MEMBER_ID in(" + memeIdsList + ")";
                   PbReturnObject folderdimIdspbro = pbdb.execSelectSQL(dimQuery);
                    String folderDimCount = String.valueOf(folderdimIdspbro.getRowCount());
                    if (dimIdsCount.equalsIgnoreCase(folderDimCount)) {
                        //  dimQuery="SELECT distinct  MEM_ID from PRG_USER_SUB_FOLDER_TABLES where  USE_REPORT_DIM_MEMBER='Y' and"+
                        // " SUB_FOLDER_ID in(select sub_folder_id from prg_user_folder_detail where folder_id="+folderId+" and sub_folder_type='Dimensions')  and dim_id in("+dimIdsList+")";
                        // PbReturnObject folderdimIdspbro1 = pbdb.execSelectSQL(dimQuery);
                        // String folderDimmemCount1=String.valueOf(folderdimIdspbro1.getRowCount());
                        if (elementIdexist.equalsIgnoreCase("YES")) {
//                            countQuery = " SELECT COUNT(nvl(e.BUSS_COL_NAME,e.user_col_name))   FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id";
                            countQuery.append(" SELECT COUNT(nvl(e.BUSS_COL_NAME,e.user_col_name))   FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id");
//                            countQuery += " FROM prg_user_folder_detail    WHERE folder_id IN(" + folderId + ")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(";
                            countQuery.append(" FROM prg_user_folder_detail    WHERE folder_id IN(" ).append( folderId ).append( ")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(");
//                            countQuery += colNamesList + ") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=" + factBussTableId;
                            countQuery.append( colNamesList).append(") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=").append( factBussTableId);
                            countpbro = pbdb.execSelectSQL(countQuery.toString());
                            count = countpbro.getFieldValueInt(0, 0);
                            if (count == elementsCount) {
//add
//adding to subfolder element types
                                String folderSubDetails = "select SUB_FOLDER_ID,SUB_FOLDER_TAB_ID,BUSS_TABLE_ID from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id="
                                        + "(SELECT Distinct sub_folder_id  FROM prg_user_folder_detail WHERE folder_id IN (" + folderId + ") and sub_folder_type='Facts')"
                                        + " and DISP_NAME='" + tableName + "' and  BUSS_TABLE_ID=" + factBussTableId;
                                PbReturnObject pbrodets = pbdb.execSelectSQL(folderSubDetails);
                                factSubFolderId1 = String.valueOf(pbrodets.getFieldValueInt(0, 0));
                                factSubFoldertabId1 = String.valueOf(pbrodets.getFieldValueInt(0, 1));
                                factBussTableId1 = String.valueOf(pbrodets.getFieldValueInt(0, 2));
                                String ref_elementId = "";
                                for (int k = 0; k < len; k++) {

                                    String addSubFolderElements = resBundle.getString("addSubFolderElements");
                                    String elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                                    PbReturnObject pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                                    String elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));
                                    if (k == 0) {
                                        ref_elementId = elementId;

                                    }
                                    Object obj1[] = new Object[16];
                                    obj1[0] = elementId;
                                    obj1[1] = factSubFolderId1;
                                    obj1[2] = factBussTableId1;
                                    obj1[3] = "0";
                                    obj1[4] = columnName;
                                    obj1[5] = columnNamesList[k];
                                    obj1[6] = columnDescList[k];
                                    obj1[7] = colType;//"Calculated";
                                    obj1[8] = factSubFoldertabId1;
                                    obj1[9] = ref_elementId;
                                    obj1[10] = "1";
                                    obj1[11] = aggType;
                                    obj1[12] = "null";
                                    obj1[13] = "Y";
                                    obj1[14] = updateddependenteleids;
                                    obj1[15] = UpdatedFormula;
                                    finalQuery = pbdb.buildQuery(addSubFolderElements, obj1);
                                    list.add(finalQuery);
                                    //adding to user all Info
                                    String addUserAllInfoDets = resBundle.getString("addUserAllInfoDets");

                                    Object obj2[] = new Object[29];
                                    obj2[0] = grpId;
                                    obj2[1] = folderId;
                                    obj2[2] = folderName;
                                    obj2[3] = factSubFolderId1;
                                    obj2[4] = "Facts";
                                    obj2[5] = "Facts";
                                    obj2[6] = factSubFoldertabId1;
                                    obj2[7] = "N";
                                    obj2[8] = "Y";
                                    obj2[9] = "N";
                                    obj2[10] = tableName;
                                    obj2[11] = "null";
                                    obj2[12] = elementId;
                                    obj2[13] = factBussTableId1;
                                    obj2[14] = "0";
                                    obj2[15] = columnName;
                                    obj2[16] = columnNamesList[k];
                                    obj2[17] = columnDescList[k];
                                    obj2[18] = colType;//"calculated";
                                    obj2[19] = ref_elementId;
                                    obj2[20] = refEleTypes[k];
                                    obj2[21] = "0";
                                    obj2[22] = "null";
                                    obj2[23] = "null";
                                    obj2[24] = "null";
                                    obj2[25] = connectionId;
                                    obj2[26] = aggType;
                                    obj2[27] = UpdatedFormula;
                                    obj2[28] = updateddependenteleids;
                                    finalQuery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                                    list.add(finalQuery);

                                }
                                //end
                            }
                        } else {

                            //adding to subfolder element types
                            String folderSubDetails = "select SUB_FOLDER_ID,SUB_FOLDER_TAB_ID,BUSS_TABLE_ID from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id="
                                    + "(SELECT Distinct sub_folder_id  FROM prg_user_folder_detail WHERE folder_id IN (" + folderId + ") and sub_folder_type='Facts')"
                                    + " and DISP_NAME='" + tableName + "' and  BUSS_TABLE_ID=" + factBussTableId;
                            PbReturnObject pbrodets = pbdb.execSelectSQL(folderSubDetails);
                            factSubFolderId1 = String.valueOf(pbrodets.getFieldValueInt(0, 0));
                            factSubFoldertabId1 = String.valueOf(pbrodets.getFieldValueInt(0, 1));
                            factBussTableId1 = String.valueOf(pbrodets.getFieldValueInt(0, 2));
                            String ref_elementId = "";
                            for (int k = 0; k < len; k++) {

                                String addSubFolderElements = resBundle.getString("addSubFolderElements");
                                String elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                                PbReturnObject pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                                String elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));
                                if (k == 0) {
                                    ref_elementId = elementId;
                                }
                                Object obj1[] = new Object[16];
                                obj1[0] = elementId;
                                obj1[1] = factSubFolderId1;
                                obj1[2] = factBussTableId1;
                                obj1[3] = "0";
                                obj1[4] = columnName;
                                obj1[5] = columnNamesList[k];
                                obj1[6] = columnDescList[k];
                                obj1[7] = colType;// "Calculated";
                                obj1[8] = factSubFoldertabId1;
                                obj1[9] = ref_elementId;
                                obj1[10] = "1";
                                obj1[11] = aggType;
                                obj1[12] = "null";
                                obj1[13] = "Y";
                                obj1[14] = updateddependenteleids;
                                obj1[15] = UpdatedFormula;
                                finalQuery = pbdb.buildQuery(addSubFolderElements, obj1);
                                list.add(finalQuery);

                                //adding to user all Info
                                String addUserAllInfoDets = resBundle.getString("addUserAllInfoDets");

                                Object obj2[] = new Object[29];
                                obj2[0] = grpId;
                                obj2[1] = folderId;
                                obj2[2] = folderName;
                                obj2[3] = factSubFolderId1;
                                obj2[4] = "Facts";
                                obj2[5] = "Facts";
                                obj2[6] = factSubFoldertabId1;
                                obj2[7] = "N";
                                obj2[8] = "Y";
                                obj2[9] = "N";
                                obj2[10] = tableName;
                                obj2[11] = "null";
                                obj2[12] = elementId;
                                obj2[13] = factBussTableId1;
                                obj2[14] = "0";
                                obj2[15] = columnName;
                                obj2[16] = columnNamesList[k];
                                obj2[17] = columnDescList[k];
                                obj2[18] = colType;//"calculated";
                                obj2[19] = ref_elementId;
                                obj2[20] = refEleTypes[k];
                                obj2[21] = "0";
                                obj2[22] = "null";
                                obj2[23] = "null";
                                obj2[24] = "null";
                                obj2[25] = connectionId;
                                obj2[26] = aggType;
                                obj2[27] = UpdatedFormula;
                                obj2[28] = updateddependenteleids;
                                finalQuery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                                list.add(finalQuery);

                            }

                        }

                    }
                } else {

                    if (elementIdexist.equalsIgnoreCase("YES")) {
//                        countQuery = " SELECT COUNT(nvl(e.BUSS_COL_NAME,e.user_col_name))   FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id";
                        countQuery.append(" SELECT COUNT(nvl(e.BUSS_COL_NAME,e.user_col_name))   FROM PRG_USER_SUB_FOLDER_ELEMENTS e,PRG_USER_SUB_FOLDER_TABLES t  WHERE e.sub_folder_id IN  (SELECT sub_folder_id");
//                        countQuery += " FROM prg_user_folder_detail    WHERE folder_id IN(" + folderId + ")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(";
                        countQuery.append(" FROM prg_user_folder_detail    WHERE folder_id IN(" ).append(folderId ).append(")  AND sub_folder_type='Facts'  )AND e.buss_col_name IN(");
//                        countQuery += colNamesList + ") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=" + factBussTableId;
                        countQuery.append( colNamesList ).append( ") AND e.element_id     = e.ref_element_id And e.use_report_flag='Y' and t.sub_folder_tab_id= e.sub_folder_tab_id and e.buss_table_id=").append( factBussTableId);
                      countpbro = pbdb.execSelectSQL(countQuery.toString());
                        count = countpbro.getFieldValueInt(0, 0);
                        if (count == elementsCount) {
//add
//adding to subfolder element types
                            String folderSubDetails = "select SUB_FOLDER_ID,SUB_FOLDER_TAB_ID,BUSS_TABLE_ID from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id="
                                    + "(SELECT Distinct sub_folder_id  FROM prg_user_folder_detail WHERE folder_id IN (" + folderId + ") and sub_folder_type='Facts')"
                                    + " and DISP_NAME='" + tableName + "' and  BUSS_TABLE_ID=" + factBussTableId;
                            PbReturnObject pbrodets = pbdb.execSelectSQL(folderSubDetails);
                            factSubFolderId1 = String.valueOf(pbrodets.getFieldValueInt(0, 0));
                            factSubFoldertabId1 = String.valueOf(pbrodets.getFieldValueInt(0, 1));
                            factBussTableId1 = String.valueOf(pbrodets.getFieldValueInt(0, 2));
                            String ref_elementId = "";
                            for (int k = 0; k < len; k++) {

                                String addSubFolderElements = resBundle.getString("addSubFolderElements");
                                String elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                                PbReturnObject pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                                String elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));
                                if (k == 0) {
                                    ref_elementId = elementId;

                                }
                                Object obj1[] = new Object[16];
                                obj1[0] = elementId;
                                obj1[1] = factSubFolderId1;
                                obj1[2] = factBussTableId1;
                                obj1[3] = "0";
                                obj1[4] = columnName;
                                obj1[5] = columnNamesList[k];
                                obj1[6] = columnDescList[k];
                                obj1[7] = colType;//"Calculated";
                                obj1[8] = factSubFoldertabId1;
                                obj1[9] = ref_elementId;
                                obj1[10] = "1";
                                obj1[11] = aggType;
                                obj1[12] = "";
                                obj1[13] = "Y";
                                obj1[14] = updateddependenteleids;
                                obj1[15] = UpdatedFormula;
                                finalQuery = pbdb.buildQuery(addSubFolderElements, obj1);
                                list.add(finalQuery);
                                //adding to user all Info
                                String addUserAllInfoDets = resBundle.getString("addUserAllInfoDets");

                                Object obj2[] = new Object[29];
                                obj2[0] = grpId;
                                obj2[1] = folderId;
                                obj2[2] = folderName;
                                obj2[3] = factSubFolderId1;
                                obj2[4] = "Facts";
                                obj2[5] = "Facts";
                                obj2[6] = factSubFoldertabId1;
                                obj2[7] = "N";
                                obj2[8] = "Y";
                                obj2[9] = "N";
                                obj2[10] = tableName;
                                obj2[11] = "null";
                                obj2[12] = elementId;
                                obj2[13] = factBussTableId1;
                                obj2[14] = "0";
                                obj2[15] = columnName;
                                obj2[16] = columnNamesList[k];
                                obj2[17] = columnDescList[k];
                                obj2[18] = colType;//"calculated";
                                obj2[19] = ref_elementId;
                                obj2[20] = refEleTypes[k];
                                obj2[21] = "0";
                                obj2[22] = "null";
                                obj2[23] = "null";
                                obj2[24] = "null";
                                obj2[25] = connectionId;
                                obj2[26] = aggType;
                                obj2[27] = UpdatedFormula;
                                obj2[28] = updateddependenteleids;
                                finalQuery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                                list.add(finalQuery);

                            }
                            //end
                        }
                    } else {

                        //adding to subfolder element types
                        String folderSubDetails = "select SUB_FOLDER_ID,SUB_FOLDER_TAB_ID,BUSS_TABLE_ID from PRG_USER_SUB_FOLDER_TABLES where sub_folder_id="
                                + "(SELECT Distinct sub_folder_id  FROM prg_user_folder_detail WHERE folder_id IN (" + folderId + ") and sub_folder_type='Facts')"
                                + " and DISP_NAME='" + tableName + "' and  BUSS_TABLE_ID=" + factBussTableId;
                        PbReturnObject pbrodets = pbdb.execSelectSQL(folderSubDetails);
                        factSubFolderId1 = String.valueOf(pbrodets.getFieldValueInt(0, 0));
                        factSubFoldertabId1 = String.valueOf(pbrodets.getFieldValueInt(0, 1));
                        factBussTableId1 = String.valueOf(pbrodets.getFieldValueInt(0, 2));
                        String ref_elementId = "";
                        for (int k = 0; k < len; k++) {

                            String addSubFolderElements = resBundle.getString("addSubFolderElements");
                            String elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                            PbReturnObject pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                            String elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));
                            if (k == 0) {
                                ref_elementId = elementId;
                            }
                            Object obj1[] = new Object[16];
                            obj1[0] = elementId;
                            obj1[1] = factSubFolderId1;
                            obj1[2] = factBussTableId1;
                            obj1[3] = "0";
                            obj1[4] = columnName;
                            obj1[5] = columnNamesList[k];
                            obj1[6] = columnDescList[k];
                            obj1[7] = colType;//"Calculated";
                            obj1[8] = factSubFoldertabId1;
                            obj1[9] = ref_elementId;
                            obj1[10] = "1";
                            obj1[11] = aggType;
                            obj1[12] = "null";
                            obj1[13] = "Y";
                            obj1[14] = updateddependenteleids;
                            obj1[15] = UpdatedFormula;
                            finalQuery = pbdb.buildQuery(addSubFolderElements, obj1);
                            list.add(finalQuery);
                            //adding to user all Info
                            String addUserAllInfoDets = resBundle.getString("addUserAllInfoDets");

                            Object obj2[] = new Object[29];
                            obj2[0] = grpId;
                            obj2[1] = folderId;
                            obj2[2] = folderName;
                            obj2[3] = factSubFolderId1;
                            obj2[4] = "Facts";
                            obj2[5] = "Facts";
                            obj2[6] = factSubFoldertabId1;
                            obj2[7] = "N";
                            obj2[8] = "Y";
                            obj2[9] = "N";
                            obj2[10] = tableName;
                            obj2[11] = "null";
                            obj2[12] = elementId;
                            obj2[13] = factBussTableId1;
                            obj2[14] = "0";
                            obj2[15] = columnName;
                            obj2[16] = columnNamesList[k];
                            obj2[17] = columnDescList[k];
                            obj2[18] = colType;//"calculated";
                            obj2[19] = ref_elementId;
                            obj2[20] = refEleTypes[k];
                            obj2[21] = "0";
                            obj2[22] = "null";
                            obj2[23] = "null";
                            obj2[24] = "null";
                            obj2[25] = connectionId;
                            obj2[26] = aggType;
                            obj2[27] = UpdatedFormula;
                            obj2[28] = updateddependenteleids;
                            finalQuery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                            list.add(finalQuery);

                        }

                    }

                }
            }
            pbdb.executeMultiple(list);
            response.getWriter().print("Correct");

        } catch (IOException ex) {
            logger.error("Exception:", ex);
            response.getWriter().print("not correct");
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
            response.getWriter().print("not correct");
        }

        return null;
    }
//addded by bharathireddy for add formula

    public ActionForward updateFactStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subFolderfactTabId = request.getParameter("subFolderfactTabId");
        try {
            UserLayerDAO userLayerDAO = new UserLayerDAO();
            userLayerDAO.updateFactStatus(subFolderfactTabId);

        } catch (NullPointerException e) {
            logger.error("Exception:", e);
        }

        return mapping.findForward(null);
    }

    //added by uday
    public ActionForward copyUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String selectedUserId = request.getParameter("selectedUserId");
        String newUserName = request.getParameter("newUserName");
        String newUserPassword = request.getParameter("newUserPassword");
        String newUserId = "";

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        newUserId = userLayerDAO.copyUser(selectedUserId, newUserName, newUserPassword);
        userLayerDAO.copyUserPrivileges(selectedUserId, newUserId);
        userLayerDAO.copyReportPrivileges(selectedUserId, newUserId);
        userLayerDAO.copyTablePrivileges(selectedUserId, newUserId);
        userLayerDAO.copyGraphPrivileges(selectedUserId, newUserId);
        userLayerDAO.copyUserReports(selectedUserId, newUserId);
        userLayerDAO.copyUserAssignment(selectedUserId, newUserId);
        userLayerDAO.copyUserFolderAssignment(selectedUserId, newUserId);

        userLayerDAO.copyUserRoleDrill(selectedUserId, newUserId);
        userLayerDAO.copyUserRoleMemberFilter(selectedUserId, newUserId);

        userLayerDAO.copyUserStickyNote(selectedUserId, newUserId);
        userLayerDAO.copyUserBussGroups(selectedUserId, newUserId);

        //return mapping.findForward("success");
        return null;
    }

    //added by susheela on 18 feb-2010
    public ActionForward updateTargetMeasureForRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleId = request.getParameter("roleId");
        String colId = request.getParameter("colId");
        String tabId = request.getParameter("tabId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.updateTargetMeasureForRole(roleId, colId, tabId);
        return mapping.findForward(null);
    }

    public ActionForward publishFact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        boolean resultBo = false;
        String subFolderTabId = request.getParameter("subFolderTabId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        resultBo = userLayerDAO.publishFact(subFolderTabId);
        out.print(resultBo);
        return mapping.findForward(null);
    }

    public ActionForward assignRoleToUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String groupId = request.getParameter("grpId");
        PrintWriter printWriter = response.getWriter();

        String[] users = null;
        boolean htmlUpdationResult = false;
        String reportIDs = "";
        String refUserId = request.getSession(false).getAttribute("USERID").toString();
        if (request.getParameter("users") != null) {
            users = request.getParameter("users").split(",");
        }
        String rolId = request.getParameter("roleId");

        String userName[] = null;
        if (request.getParameter("userNames") != null) {
            userName = request.getParameter("userNames").split(",");
        }
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        if (request.getParameter("reportIDs") != null) {
            reportIDs = request.getParameter("reportIDs");
        }
        if (!reportIDs.equalsIgnoreCase("")) {
            htmlUpdationResult = userLayerDAO.assignRoleToUser(users, reportIDs, refUserId);
            printWriter.print(htmlUpdationResult);

        } else {
            userLayerDAO.assignRoleToUser(groupId, users, rolId, userName);
        }
        return null;
    }

    public ActionForward assignRolesToUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] grpIds = null;
        String[] roleIds = null;
        if (request.getParameter("grpId") != null) {
            grpIds = request.getParameter("grpId").split(",");
        }
        if (request.getParameter("roleId") != null) {
            roleIds = request.getParameter("roleId").split(",");
        }
        String userId = request.getParameter("users");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.assignFolderToUsers(grpIds, roleIds, userId);
        return null;
    }

    public ActionForward getSenSitivityFactors(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Container container = null;
        PrintWriter out = null;
        String reportId = request.getParameter("reportId");
        String grpCol = request.getParameter("grpColArray");
        String[] grpColArray = grpCol.split(",");
        WhatIfScenario whatIfScenario = null;
        WhatIfSensitivity whatIfSensitivity = null;
        ArrayList<String> sentivityFactors = new ArrayList<String>();
        try {
            out = response.getWriter();
            container = Container.getContainerFromSession(request, reportId);
            if (container != null) {
                whatIfScenario = container.getWhatIfScenario();
                if (whatIfScenario != null) {
                    whatIfSensitivity = whatIfScenario.getWhatIfSensitivity();
                    for (String string : grpColArray) {
                        boolean check = whatIfSensitivity.isApplicable(string);
                        if (check) {
                            sentivityFactors.add(string);
                        }
                    }
                }
            }
            out.print("{\"sensitivityFactiors\":[\"" + Joiner.on("\",\"").join(sentivityFactors) + "\"]}");
        } catch (IOException e) {
             logger.error("Exception:", e);
        }

        return null;
    }

    public ActionForward getDimentionMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userMemId = null;
        String subFolderIdUser = null;
        String userId = null;
        String MemberName = null;
        UserLayerDAO uDao = new UserLayerDAO();
        String accessLevel = "";
        int roleId = 0;
        ArrayList<Cube> cubes = null;
        boolean isMemberUseInOtherLevel = false;
        String type = "null";
        String[] memValues = null;

        type = request.getParameter("type");
        String startVal = request.getParameter("startVal");
        String mbrs = request.getParameter("mbrs");
        String searchText = request.getParameter("searchText");

        if (!(mbrs == null || mbrs.equalsIgnoreCase(""))) {
            mbrs = mbrs.replace("%20", " ").replace("%26", "&").replace("'''", ",").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%40", "@");
            mbrs = mbrs.replace("%23", "#").replace("%24", "$").replace("%25", "%").replace("%5E", "^").replace("%60", "`").replace("%3C", "<").replace("%3E", ">").replace("%2C", ",");
        }
        if (!(mbrs == null || mbrs.equalsIgnoreCase(""))) {
            memValues = mbrs.split(",");
        }

        if (type != null && !type.equalsIgnoreCase("null") && type.equalsIgnoreCase("restrictAccess")) {
            if (request.getParameter("accessLevel") != null) {
                accessLevel = request.getParameter("accessLevel");
            }
            HttpSession session = request.getSession(false);
            if (request.getParameter("elementId") != null) {
                String elementId = request.getParameter("elementId");
                PbReturnObject object = uDao.getElementIDDetails(elementId);
                userMemId = object.getFieldValueString(0, "MEMBER_ID");
                subFolderIdUser = object.getFieldValueString(0, "SUB_FOLDER_ID");
                userId = (String) session.getAttribute("USERID");
                MemberName = object.getFieldValueString(0, "MEMBER_NAME");
                roleId = object.getFieldValueInt(0, "FOLDER_ID");

            } else {
                userMemId = request.getParameter("userMemId");
                subFolderIdUser = request.getParameter("subFolderIdUser");
                userId = request.getParameter("userId");
                MemberName = request.getParameter("MemberName");

            }
            if (!accessLevel.equalsIgnoreCase("")) {
                uDao.setAccessLevel(accessLevel);
            }
            if (session.getAttribute("CUBES") != null) {
                cubes = (ArrayList<Cube>) session.getAttribute("CUBES");
            }

            if (cubes == null) {
                cubes = new ArrayList<Cube>();
                CubeInterface cubeInterface = new CubeInterface();
                cubes.add(cubeInterface.getCube(roleId));
            }

            ArrayList insertedMemberValues = new ArrayList();
            if (!(mbrs == null || mbrs.equalsIgnoreCase(""))) {
                for (int i = 0; i < memValues.length; i++) {
                    insertedMemberValues.add(memValues[i].replace("'''", ","));
                }
            }
            PrintWriter out = null;
            try {
                out = response.getWriter();
//            HashMap details = uDao.getMemberValuesForDim(userMemId, subFolderIdUser, request.getParameter("elementId"));
                HashMap details = uDao.getMemberValuesForDim(userMemId, subFolderIdUser, cubes, userId);

                if (!accessLevel.equalsIgnoreCase("")) {
                    insertedMemberValues = uDao.getAddedMemberValues(subFolderIdUser, userMemId, accessLevel, userId);
                    isMemberUseInOtherLevel = uDao.isIsMemberUseInOtherLevel();
                }
                Set<String> detailsKey = details.keySet();
                ArrayList<String> memBerDet = new ArrayList();
                for (String key : detailsKey) {
                    memBerDet.addAll((Collection<? extends String>) details.get(key));
                }
                memBerDet.removeAll(insertedMemberValues);
//             
                GenerateDragAndDrophtml dragAndDrophtml = new GenerateDragAndDrophtml("", "", insertedMemberValues, memBerDet, request.getContextPath());
                dragAndDrophtml.setIsdragavleRestrict(isMemberUseInOtherLevel);
                out.print(dragAndDrophtml.getDragAndDropDivParam());
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        } else if (!(type == null || type.equalsIgnoreCase("") || type.equalsIgnoreCase("facts"))) {
            if (request.getParameter("accessLevel") != null) {
                accessLevel = request.getParameter("accessLevel");
            }
            HttpSession session = request.getSession(false);
            if (request.getParameter("elementId") != null) {
                String elementId = request.getParameter("elementId");
                PbReturnObject object = uDao.getElementIDDetails(elementId);
                userMemId = object.getFieldValueString(0, "MEMBER_ID");
                subFolderIdUser = object.getFieldValueString(0, "SUB_FOLDER_ID");
                userId = (String) session.getAttribute("USERID");
                MemberName = object.getFieldValueString(0, "MEMBER_NAME");
                roleId = object.getFieldValueInt(0, "FOLDER_ID");

            } else {
                userMemId = request.getParameter("userMemId");
                subFolderIdUser = request.getParameter("subFolderIdUser");
                userId = request.getParameter("userId");
                MemberName = request.getParameter("MemberName");

            }
            if (!accessLevel.equalsIgnoreCase("")) {
                uDao.setAccessLevel(accessLevel);
            }
            if (session.getAttribute("CUBES") != null) {
                cubes = (ArrayList<Cube>) session.getAttribute("CUBES");
            }

//        if(cubes==null){
//            cubes=new ArrayList<Cube>();
//            CubeInterface cubeInterface=new CubeInterface();
//            cubes.add( cubeInterface.getCube(roleId));
//        }
            ArrayList insertedMemberValues = new ArrayList();
            if (!(mbrs == null || mbrs.equalsIgnoreCase(""))) {
                for (int i = 0; i < memValues.length; i++) {
                    insertedMemberValues.add(memValues[i].replace("'''", ","));
                }
            }
            PrintWriter out = null;
            try {
                out = response.getWriter();
                HashMap details = uDao.getMemberValuesForDim(userMemId, subFolderIdUser, request.getParameter("elementId"), startVal, searchText);
//            HashMap details = uDao.getMemberValuesForDim(userMemId, subFolderIdUser, cubes, userId);

                if (!accessLevel.equalsIgnoreCase("")) {
                    insertedMemberValues = uDao.getAddedMemberValues(subFolderIdUser, userMemId, accessLevel, userId);
                    isMemberUseInOtherLevel = uDao.isIsMemberUseInOtherLevel();
                }
                Set<String> detailsKey = details.keySet();
                ArrayList<String> memBerDet = new ArrayList();
                for (String key : detailsKey) {
                    memBerDet.addAll((Collection<? extends String>) details.get(key));
                }
                if (insertedMemberValues != null) {
                    memBerDet.removeAll(insertedMemberValues);
                }
//             
                GenerateDragAndDrophtml dragAndDrophtml = new GenerateDragAndDrophtml("", "", insertedMemberValues, memBerDet, request.getContextPath());
                dragAndDrophtml.setIsdragavleRestrict(isMemberUseInOtherLevel);
                out.print(dragAndDrophtml.getDragAndDropDivParam());
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        } else {
            // String elementId = request.getParameter("measureId");
            String elementId = request.getParameter("elementId");
            String factName = request.getParameter("factName");
            PbReportViewerDAO dao = new PbReportViewerDAO();
            ArrayList draggableList = new ArrayList();
            PbReturnObject measureValues = null;
            try {
                measureValues = dao.getMeasureValues(elementId, factName);
                if (measureValues != null) {
                    for (int i = 0; i < measureValues.getRowCount(); i++) {
                        //
                        draggableList.add(measureValues.getFieldValueString(i, 0));
                    }
                    GenerateDragAndDrophtml draganddrophtml = new GenerateDragAndDrophtml("", "", null, draggableList, request.getContextPath());
                    //
                    response.getWriter().print(draganddrophtml.getDragAndDropDivParam());
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

        }

        return null;
    }

    public ActionForward getAllMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String connectionID = request.getParameter("connectionID");
        String foldersSelected = request.getParameter("foldersSelected");
        String tablesSelected = request.getParameter("tablesSelected");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getAllMeasures(userId, connectionID, foldersSelected, tablesSelected));
        } catch (Exception e) {
        }
        return null;
    }

    //addded by Nazneen
    public ActionForward getDimDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String connectionID = request.getParameter("connectionID");
        String folderID = request.getParameter("folderID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getDimDetails(userId, connectionID, folderID));
        } catch (IOException e) {
             logger.error("Exception:", e);
        }
        return null;
    }

    //ended by Nazneen
    //added by sunita
    public ActionForward getAllColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String tablesSelected = request.getParameter("tablesSelected");
        String conId = request.getParameter("connectionID");
        String grpId = request.getParameter("grpId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getAllColumns(tablesSelected, conId, grpId));
        } catch (IOException e) { logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward deleteRow1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String tableCol = request.getParameter("tableCol");
        String conId = request.getParameter("connectionID");
        String rowID = request.getParameter("rowID");
        String tablesSelected = request.getParameter("tablesSelected");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            userLayerDAO.deleteRow1(tableCol, conId, rowID, tablesSelected);
        } catch (Exception e) {
             logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getFolderDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String connectionID = request.getParameter("connectionID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getFolderDetails(userId, connectionID));
        } catch (IOException e) {
               logger.error("Exception:", e);
        }catch (NullPointerException e) {
               logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getGroupDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String connectionID = request.getParameter("connectionID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getGroupDetails(userId, connectionID));
        } catch (IOException e) {  logger.error("Exception:", e);
        }catch (NullPointerException e) {
               logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getTabDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String connectionID = request.getParameter("connectionID");
        String folderID = request.getParameter("folderID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getTabDetails(userId, connectionID, folderID));
        } catch (IOException e) {
              logger.error("Exception:", e);
        }catch (NullPointerException e) {
               logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getTableDetails1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String groupID = request.getParameter("groupID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getTableDetails1(groupID));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
               logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward deleteRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String refID = request.getParameter("refID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            userLayerDAO.deleteRow(refID);
        } catch (Exception e) {
             logger.error("Exception:", e);
        }
        return null;
    }
    //ended by sunita
    //code for get AllConnection

    public ActionForward getAllConnectionForModifyMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getAllConnectionForModifyMeasures());
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
               logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getUpdateMeasureDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayListMultimap changeInDependentMultimap = ArrayListMultimap.create();
        ArrayListMultimap changeInGroupMultimap = ArrayListMultimap.create();
        ArrayListMultimap changeInReportMultimap = ArrayListMultimap.create();
        String changedependentmeasure = (String) request.getParameter("changedependentmeasure");
        String originaldependentmeasure = (String) request.getParameter("originaldependentmeasure");

        if (changedependentmeasure != null) {
            String[] changeinDependMeasure = changedependentmeasure.split(",");
            for (String elementID : changeinDependMeasure) {
                boolean testFlag = false;
                if (request.getParameterValues(elementID) != null) {
                    String[] tempStr = request.getParameterValues(elementID);
                    //changed by nazneen
                    String[] tempStr1 = request.getParameterValues("temp_" + elementID);
//            for (String chengDependent : tempStr) {
//                changeInDependentMultimap.put(elementID, chengDependent);
//            }
                    for (int i = 0; i < tempStr.length; i++) {
                        if (!tempStr[i].equalsIgnoreCase(tempStr1[i])) {
                            testFlag = true;
                            break;
                        }
                    }
                    if (testFlag) {
                        for (String chengDependent : tempStr) {
                            changeInDependentMultimap.put(elementID, chengDependent);
                        }
                    }
                }
            }
        }
        String changeingroup = request.getParameter("changeingroup");
        if (changeingroup != null) {
            String[] changeinGroup = changeingroup.split(",");
            for (String elementID : changeinGroup) {
                if (request.getParameterValues(elementID) != null) {
                    String[] tempStr = request.getParameterValues(elementID);
                    for (String chengeingroup : tempStr) {
                        changeInGroupMultimap.put(elementID, chengeingroup);
                    }
                }
            }
        }
        String changeinreport = request.getParameter("changeinreport");
        if (changeinreport != null) {
            String[] changeinReport = changeinreport.split(",");
            for (String elementID : changeinReport) {
                if (request.getParameterValues(elementID) != null) {
                    String[] tempStr = request.getParameterValues(elementID);
                    for (String changeinrep : tempStr) {
                        changeInReportMultimap.put(elementID, changeinrep);
                    }
                }
            }
        }
        boolean statusinDependentMe = false;
        boolean statusinGroup = false;
        boolean statusinReport = false;
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        statusinDependentMe = userLayerDAO.updateDependentMeasure(changeInDependentMultimap);
        if (changeInDependentMultimap.isEmpty()) {
            statusinDependentMe = true;
        }
        statusinGroup = userLayerDAO.updateInGroup(changeInGroupMultimap);
        if (changeInGroupMultimap.isEmpty()) {
            statusinGroup = true;
        }
        statusinReport = userLayerDAO.updateInReport(changeInReportMultimap);
        if (changeInReportMultimap.isEmpty()) {
            statusinReport = true;
        }
        if (statusinDependentMe && statusinGroup && statusinReport) {
            response.getWriter().print(true);
        } else {
            response.getWriter().print(false);
        }
        return null;
    }
    //added by Nazneen

    public ActionForward getAllConnectionForModifyMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getAllConnectionForModifyMembers());
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //added by Nazneen

    public ActionForward getAllMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connectionID = request.getParameter("connectionID");
        String folderId = request.getParameter("folderID");
        String dimId = request.getParameter("dimId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String jsonstring = userLayerDAO.getAllMembers(connectionID, folderId, dimId);
        response.getWriter().print(jsonstring);

        return null;
    }
    //added by Nazneen

    public ActionForward getModifyMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conId = request.getParameter("conid");
        String[] chkrpt = request.getParameterValues("chkrpt");
        String[] dimnamelist = request.getParameterValues("dimnamelist");
        String[] oldmembernamelist = request.getParameterValues("oldmemname");
        String[] newmembernamelist = request.getParameterValues("newmemname");
        String[] elementidlist = request.getParameterValues("elementid");
        String[] memberidlist = request.getParameterValues("memberid");
        String[] folderidlist = request.getParameterValues("folderid");
        String[] subfolderidlist = request.getParameterValues("subfolderid");
        boolean jsonstring1 = false;
        boolean jsonstring2 = false;
        boolean jsonstring = false;

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        int val = 0;
        if (chkrpt != null) {
            for (int i = 0; i < chkrpt.length; i++) {
                String checkd = chkrpt[i];
                if (checkd.length() == 7) {
                    val = Integer.parseInt(checkd.substring(6, 7));
                } else {
                    val = Integer.parseInt(checkd.substring(6, 8));
                }

                jsonstring1 = userLayerDAO.getModifyMembersRep(elementidlist[val], oldmembernamelist[val], newmembernamelist[val], folderidlist[val]);
            }
        }

        for (int i = 0; i < dimnamelist.length; i++) {
            if (!oldmembernamelist[i].equals(newmembernamelist[i])) {
                jsonstring2 = userLayerDAO.getModifyMembers(elementidlist[i], dimnamelist[i], oldmembernamelist[i], newmembernamelist[i], folderidlist[i], subfolderidlist[i], memberidlist[i]);
            }
            if (chkrpt != null) {
                if (jsonstring1 == true) {
                    if (jsonstring2 == true) {
                        jsonstring = true;
                    }
                }
            } else {
                if (jsonstring2 == true) {
                    jsonstring = true;
                }
            }
        }
        response.getWriter().print(jsonstring);
        return null;
    }

    // add by ramesh on 26-jun-12
    public ActionForward partialPublishDimentions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session;
        if (request.getSession() == null) {
            session = request.getSession(true);

        } else {
            session = request.getSession();
        }
        String dimid = request.getParameter("dimid");
        String subfolderid = request.getParameter("subfolderid");
        String folderid = "";
        // String folderid=request.getParameter("folderid");
        folderid = String.valueOf(session.getAttribute("folderId"));
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = userLayerDAO.partialPublishDimentions(dimid, subfolderid, folderid, "");
        PrintWriter out = response.getWriter();
        out.print(result);

//       response.getWriter().print(jsonstring);
        return null;
    }
    //added by sunita

    public ActionForward partialPublishDimentionsCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session;
        if (request.getSession() == null) {
            session = request.getSession(true);

        } else {
            session = request.getSession();
        }
        String dimid = request.getParameter("dimid");
        String subfolderid = request.getParameter("subfolderid");
        //String folderid=request.getParameter("folderid");
        String folderid = "";
        folderid = String.valueOf(session.getAttribute("folderId"));
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = userLayerDAO.partialPublishDimentionsCheck(dimid, subfolderid, folderid);
        PrintWriter out = response.getWriter();
        out.print(result);

//       response.getWriter().print(jsonstring);
        return null;
    }

    public ActionForward SaveExtraMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String tableId = request.getParameter("tableId");
        String dbtabName = request.getParameter("dbtabName");
        String[] extramembers = request.getParameterValues("extracolumn");
        for (int i = 0; i < extramembers.length; i++) {
            UserLayerDAO userLayerDAO = new UserLayerDAO();
            userLayerDAO.SaveExtraMembers(tableId, dbtabName, extramembers[i]);

        }

        return null;
    }
    //added by anil

    public ActionForward getBusinessRolesMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String bsrSelectedID = request.getParameter("bsrSelectedID");
        String contextPath = request.getParameter("contextPath");
        String userID = (String) request.getSession(false).getAttribute("USERID");
        boolean fromBPM = Boolean.parseBoolean(request.getParameter("fromBPM"));
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getAllTargetMeasures(userID, bsrSelectedID, contextPath, fromBPM));
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //added by anil

    public ActionForward mappedElements(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        int z = 0;
        PbReturnObject elementID = new PbReturnObject();
        ArrayList rtMeasures1 = new ArrayList();
        String eleID = request.getParameter("eleID");
        String mappedEleID = request.getParameter("mapID");
        // String[] mapele=mappedEleID.split(",");
        //  for(int i=0;i<mapele.length;i++){

        //   }
        // rtMeasures.add(mappedEleID.split(","));
        // String query="update PRG_USER_ALL_INFO_DETAILS set DEPENDENT_MEASURE="+mappedEleID+" where ELEMENT_ID="+eleID;
        String contextPath = request.getParameter("contextPath");
        String userID = (String) request.getSession(false).getAttribute("USERID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            String result = userLayerDAO.getMappedElements(eleID, mappedEleID, contextPath);
            out.print(result);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //added by Nazneen

    public ActionForward getEmailConfigDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String jsonstring = userLayerDAO.getEmailConfigDetails();
        response.getWriter().print(jsonstring);
        return null;
    }
//added by Nazneen

    public ActionForward saveEmailConfigDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String hostName = request.getParameter("hostName");
        String portNo = request.getParameter("portNo");
        String fromAdd = request.getParameter("fromAdd");
        String checkDebug = request.getParameter("checkDebug");
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String sslStatus = request.getParameter("sslStatus");

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        int result = userLayerDAO.saveEmailConfigDetails(hostName, portNo, fromAdd, checkDebug, userId, password, sslStatus);
        response.getWriter().print(result);
        return null;
    }

    public ActionForward getDefineMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String bsrSelectedID = request.getParameter("bsrSelectedID");
        String contextPath = request.getParameter("contextPath");
        String userID = (String) request.getSession(false).getAttribute("USERID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getAllDefineMeasures(userID, bsrSelectedID, contextPath));
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward mappedRelatedMeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String eleID = request.getParameter("eleID");
        String mappedEleID = request.getParameter("mapID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();

        String query = "update PRG_USER_ALL_INFO_DETAILS set DEPENDENT_MEASURE='" + mappedEleID + "' where ELEMENT_ID=" + eleID;
        String result = userLayerDAO.getRelatedMappedElements(eleID, mappedEleID, query);
        try {
            response.getWriter().print(result);
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward usersForRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String groupID = request.getParameter("groupID");
        String folderID = request.getParameter("folderID");

        //String userID = (String) request.getSession(false).getAttribute("USERID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getUsersForRoles(groupID, folderID));
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward reportsBasedOnRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String groupID = request.getParameter("groupID");
        String folderID = request.getParameter("folderID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        HashMap reports = userLayerDAO.getRepAndDbrdsForRole(groupID);
        GenerateDragAndDrophtmlForExcel generateDragAndDrophtml = null;
        ArrayList<String> Mohit = null;
        generateDragAndDrophtml = new GenerateDragAndDrophtmlForExcel("select columns from below", "drop columns here", (ArrayList<String>) Mohit, (ArrayList<String>) reports.get("reportIds"), request.getContextPath());
        generateDragAndDrophtml.setDragableListNames((ArrayList<String>) reports.get("reportNames"));
        generateDragAndDrophtml.setDropedmesNames((ArrayList<String>) reports.get("reportNames"));
        String dragndrop = generateDragAndDrophtml.getDragAndDropDiv(request);
        try {

            response.getWriter().print(dragndrop);
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward repAnddbrdsForUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String grpID = request.getParameter("folderId");
        String userIds = request.getParameter("userIds");
        GenerateDragAndDrophtml generateDragAndDrophtml = null;
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        HashMap repDbrds = userLayerDAO.getRepAndDbrdsForUser(grpID);
        if (userIds.contains(",")) {
            //changed by Nazneen
//               generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here",null,(ArrayList<String>)repDbrds.get("reportIds") , request.getContextPath());
//               generateDragAndDrophtml.setDragableListNames((ArrayList<String>)repDbrds.get("reportNames"));
//               generateDragAndDrophtml.setDropedmesNames(null);
            HashMap favrepDbrds = userLayerDAO.getFavRepsForAll(userIds);
            generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", (ArrayList<String>) favrepDbrds.get("reportIds"), (ArrayList<String>) repDbrds.get("reportIds"), request.getContextPath());
            generateDragAndDrophtml.setDragableListNames((ArrayList<String>) repDbrds.get("reportNames"));
            generateDragAndDrophtml.setDropedmesNames((ArrayList<String>) favrepDbrds.get("reportNames"));

        } else {
            HashMap favrepDbrds = userLayerDAO.getFavReps(userIds);
            generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", (ArrayList<String>) favrepDbrds.get("reportIds"), (ArrayList<String>) repDbrds.get("reportIds"), request.getContextPath());
            generateDragAndDrophtml.setDragableListNames((ArrayList<String>) repDbrds.get("reportNames"));
            generateDragAndDrophtml.setDropedmesNames((ArrayList<String>) favrepDbrds.get("reportNames"));
        }
        String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();
        try {
            response.getWriter().print(dragndrop);
        } catch (Exception e) {
        }
        return null;
    }

    public ActionForward saveUsersForReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userIds = request.getParameter("userIds");
        String reportIds = request.getParameter("reportIds");
        String[] userId = userIds.split(",");
        ArrayList queries = new ArrayList();
        String nullQuery = "";
        UserLayerDAO userLayerDAO = null;
        if (userIds.contains(",")) {
            new UserLayerDAO().saveAllUsersForReports(userIds, reportIds);
        } else {
            new UserLayerDAO().saveUsersForReports(userIds, reportIds);
        }
        // boolean status= userLayerDAO.saveUsersForReports(userIds,reportIds);
        if (!reportIds.equalsIgnoreCase("")) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                for (int i = 0; i < userId.length; i++) {
                    nullQuery = "update prg_ar_user_reports set pur_report_sequence =null  where report_id not in (" + reportIds + ") AND user_id=" + userId[i];
                    queries.add(nullQuery);
                }
            } else {
                for (int i = 0; i < userId.length; i++) {
                    nullQuery = "update prg_ar_user_reports set pur_report_sequence = '' where report_id not in (" + reportIds + ") AND user_id=" + userId[i];
                    queries.add(nullQuery);
                }
            }
        } else {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                for (int i = 0; i < userId.length; i++) {
                    nullQuery = "update prg_ar_user_reports set pur_fav_report = 'N' where user_id=" + userId[i];
                    queries.add(nullQuery);
                }
            } else {
                for (int i = 0; i < userId.length; i++) {
                    nullQuery = "update prg_ar_user_reports set pur_fav_report = 'N' where user_id=" + userId[i];
                    queries.add(nullQuery);
                }
            }
        }
        // queries.add(nullQuery);
        boolean status = new UserLayerDAO().saveFavLinks(queries);
        try {
            response.getWriter().print(status);
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward insertTargetMeasureDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String bussRoleId = request.getParameter("bussRoleId");
        String periodTypeId = request.getParameter("periodTypeId");
        String elementID = request.getParameter("elementID");
        String elementName = request.getParameter("elementName");
        String startValue = request.getParameter("startValue");
        String endValue = request.getParameter("endValue");
        String userId = String.valueOf(session.getAttribute("USERID"));

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.insertTargetMeasures(bussRoleId, userId, periodTypeId, elementID, elementName, startValue, endValue);
        return null;
    }

    public ActionForward getTargetMeasuresDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String bussRoleId = request.getParameter("bussRoleId");
        String userId = String.valueOf(session.getAttribute("USERID"));
        List<String> elementIds = new ArrayList<String>();
        List<String> elementNames = new ArrayList<String>();
        HashMap<String, List<String>> targetMeasure = new HashMap<String, List<String>>();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        targetMeasure = userLayerDAO.getTargetMeasuresDetails(bussRoleId, userId);
        elementIds = targetMeasure.get("ELEMENTIDS");
        elementNames = targetMeasure.get("ELEMENTNAMES");
        StringBuilder buildTargets = new StringBuilder();

        if (targetMeasure != null && !targetMeasure.isEmpty()) {
            buildTargets.append("<ul id=\"kpiTree1\" >");
            buildTargets.append(" <span class=\"openmenu1\">");
            buildTargets.append(" <li class='closed' id=\"2_tagetTable1\">");
            //buildTargets.append("<img src='"+request.getContextPath()+"/icons pinvoke/table.png' ></img>");
            buildTargets.append("<span style='font-family:verdana;' id='2~tagetTables ' class=\"2_Targets\" title=\"targetTable\">TARGET_MEASURES</span>");
            buildTargets.append("<ul id=\"2_TagetTable\">");
            buildTargets.append(userLayerDAO.getUiData(elementIds, elementNames, request));
            buildTargets.append("</ul>");
            buildTargets.append("</li>");
            buildTargets.append("</span>  ");
            buildTargets.append("</ul> ");
            response.getWriter().print(buildTargets.toString());
        } else {
            response.getWriter().print("No Measures");
        }
        return null;
    }

    public ActionForward testTargetsExistorNot(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String bussRoleId = request.getParameter("bussRoleId");
        String elementID = request.getParameter("elementID");
        String selectYear = request.getParameter("selectYear");
        //String rollingtype=request.getParameter("rollingtype");
        String userId = String.valueOf(session.getAttribute("USERID"));
        List<String> targetPeriodDetails = new ArrayList<String>();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        targetPeriodDetails = userLayerDAO.testTargetsExistorNot(bussRoleId, userId, elementID, selectYear);
        StringBuilder targetPeriodDe = new StringBuilder();
        targetPeriodDe.append("{ TargetPeriodDetails :[");
        if (targetPeriodDetails != null && !targetPeriodDetails.isEmpty()) {
            for (int i = 0; i < targetPeriodDetails.size(); i++) {
                targetPeriodDe.append("\"").append(targetPeriodDetails.get(i)).append("\"");
                if (i != targetPeriodDetails.size() - 1) {
                    targetPeriodDe.append(",");
                }
            }
            targetPeriodDe.append("]}");
            response.getWriter().print(targetPeriodDe.toString());
        } else {
            response.getWriter().print("No PeriodDetails");
        }
        return null;
    }

    public ActionForward defineTargetPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String bussRoleId = request.getParameter("bussRoleId");
        String elementID = request.getParameter("elementID");
        String userId = String.valueOf(session.getAttribute("USERID"));
        List<String> targetPeriodDetails = new ArrayList<String>();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        targetPeriodDetails = userLayerDAO.testTargetsExistorNot(bussRoleId, userId, elementID, "");
        StringBuilder targetPeriodDe = new StringBuilder();
        targetPeriodDe.append("{ TargetPeriodDetails :[");
        if (targetPeriodDetails != null) {
            for (int i = 0; i < targetPeriodDetails.size(); i++) {
                targetPeriodDe.append("\"").append(targetPeriodDetails.get(i)).append("\"");
                if (i != targetPeriodDetails.size() - 1) {
                    targetPeriodDe.append(",");
                }
            }
            targetPeriodDe.append("]}");
            response.getWriter().print(targetPeriodDe.toString());
        } else {
            response.getWriter().print("No PeriodDetails");
        }
        return null;
    }

    public ActionForward getTargetMeasureTimeDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String startValue = request.getParameter("startValue");
        String elementID = request.getParameter("elementID");
        String endValue = request.getParameter("endValue");
        String periodtype = request.getParameter("periodtype");
        int selectYear = 0;
        String rollingtype = "";
        if (request.getParameter("selectYear") != null && !request.getParameter("selectYear").equalsIgnoreCase("undefined")) {
            selectYear = Integer.parseInt(request.getParameter("selectYear"));
            rollingtype = request.getParameter("rollingtype");
        }
        String eleName = request.getParameter("eleName");
        String bussId = request.getParameter("bussId");
        String userId = String.valueOf(session.getAttribute("USERID"));
        boolean fromdownLoad = Boolean.parseBoolean(request.getParameter("fromdownLoadOption"));
        List<String> targetPeriodDetails = new ArrayList<String>();
        List<String> targetValues = new ArrayList<String>();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        targetPeriodDetails = userLayerDAO.getTargetPeriodDetails(startValue, endValue, elementID, periodtype);
        targetValues = userLayerDAO.getTargetValues(startValue, endValue, elementID, periodtype);
        String fileNameinCache = "";
        if (selectYear == 0) {
            selectYear = userLayerDAO.getPrevieousYears(elementID);
            rollingtype = userLayerDAO.getPrevieousRollingType(elementID);
        }
        HashMap test = new HashMap();
        if (selectYear != 0) {
            for (int i = 0; i <= selectYear; i++) {
                SimpleDateFormat datefrmt = new SimpleDateFormat();
                datefrmt.applyPattern("dd-MMM-yy");
                int endDate = Integer.parseInt(endValue.substring(endValue.length() - 2, endValue.length())) - i;

                Date date = datefrmt.parse(endValue.replace(endValue.substring(endValue.length() - 2, endValue.length()), Integer.toString(endDate)));
                SimpleDateFormat datefrmt1 = new SimpleDateFormat("MM/dd/yyyy");
                ArrayList timeDetailsArray = new ArrayList();
                ArrayList rowViewbyCols = new ArrayList();
                ArrayList queryElements = new ArrayList();
                ArrayList aggrigation = new ArrayList();
                ArrayList tiemDetailsData = new ArrayList();
                aggrigation = userLayerDAO.getMeasureAggration(elementID);
//                      arl.add("Day");
//        arl.add("PRG_DAY_ROLLING");
//        arl.add(dateFormat);
//        arl.add("Last 30 Days");
                timeDetailsArray.add("Day");
                timeDetailsArray.add("PRG_STD");
                timeDetailsArray.add(datefrmt1.format(date));
                timeDetailsArray.add(periodtype);
                timeDetailsArray.add("Last Period");
//                     timeDetailsArray.add("Day");
//                     timeDetailsArray.add("PRG_DAY_ROLLING");
//                     timeDetailsArray.add(datefrmt1.format(date));
//                    // timeDetailsArray.add(periodtype);
//                     timeDetailsArray.add("Last 365 Period");
                rowViewbyCols.add("Time");
                queryElements.add(elementID);
                PbReportQuery repQuery = new PbReportQuery();
                PbReturnObject pbretObj = null;
                repQuery.setRowViewbyCols(rowViewbyCols);
                repQuery.setColViewbyCols(new ArrayList());
                repQuery.setQryColumns(queryElements);
                repQuery.setColAggration(aggrigation);
                repQuery.setTimeDetails(timeDetailsArray);
                pbretObj = repQuery.getPbReturnObject(String.valueOf(elementID));
                if (pbretObj.rowCount >= 0) {
                    for (int j = 0; j < pbretObj.rowCount; j++) {
                        tiemDetailsData.add(pbretObj.getFieldValueString(j, 1));
                    }
                    test.put(i, tiemDetailsData);
                }
            }
        }
        DataSnapshotGenerator reportTemplate = new DataSnapshotGenerator();
        fileNameinCache = reportTemplate.targetMeasureExcel(elementID, eleName, targetPeriodDetails, test, selectYear, targetValues, fromdownLoad);
        session.setAttribute("targetMeasureExcelFile", fileNameinCache);
        userLayerDAO.insertExcelFile(bussId, userId, elementID, fileNameinCache);
        response.getWriter().print(fileNameinCache);
        return null;
    }

    public ActionForward insertTargetMeasureValueDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String elementID = request.getParameter("elementId");
        String startValue = request.getParameter("startvalue");
        String endValue = request.getParameter("endvalue");
        String periodtype = request.getParameter("periodType");
        String elemtName = request.getParameter("elemtName");
        String userId = String.valueOf(session.getAttribute("USERID"));
        String bussRoleId = request.getParameter("bussroleId");
        String[] targetValues = request.getParameterValues("targetValues");
        String[] cusomNames = request.getParameterValues("cusomNames");
        List<String> targetValuesList = new ArrayList<String>();
        List<String> customNames = new ArrayList<String>();
        targetValuesList.addAll(Arrays.asList(targetValues));
        customNames.addAll(Arrays.asList(cusomNames));
        UserLayerDAO usrlrDao = new UserLayerDAO();
        usrlrDao.insertTargetMeasureValues(elementID, elemtName, userId, bussRoleId, startValue, endValue, periodtype, targetValuesList, customNames);
        return null;
    }

    public ActionForward getTargetValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String elementID = request.getParameter("elementID");
        String periodtype = request.getParameter("periodtype");
        String startValue = request.getParameter("startValue");
        String endValue = request.getParameter("endValue");

        UserLayerDAO userLayerdao = new UserLayerDAO();
        List<String> targetPeriodDetails = new ArrayList<String>();
        List<String> custNames = new ArrayList<String>();
        HashMap<String, List<String>> targetMeasure = new HashMap<String, List<String>>();
        targetMeasure = userLayerdao.testForTargetValuesExits(elementID, periodtype, startValue, endValue);
        StringBuilder targetValues = new StringBuilder();
        StringBuilder customNames = new StringBuilder();

        targetValues.append("{ TargetValues :[");
        customNames.append(" CustomNames :[");
        if (!targetMeasure.isEmpty()) {
            targetPeriodDetails = targetMeasure.get("TARGETVALUES");
            custNames = targetMeasure.get("CUSTOMNAMES");
            for (int i = 0; i < targetPeriodDetails.size(); i++) {
                targetValues.append("\"").append(targetPeriodDetails.get(i)).append("\"");
                customNames.append("\"").append(custNames.get(i)).append("\"");
                if (i != targetPeriodDetails.size() - 1) {
                    targetValues.append(",");
                    customNames.append(",");
                }
            }
            targetValues.append("],").append(customNames).append("]}");
            response.getWriter().print(targetValues.toString());
        } else {
            response.getWriter().print("No TargetValues");
        }

        return null;
    }

    public ActionForward getallDimViewBys(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String bussRoleId = request.getParameter("bussRoleId");
        String elementID = request.getParameter("elementID");
        String fromDescrete = request.getParameter("fromDescrete");

        UserLayerDAO userLayerdao = new UserLayerDAO();
        ArrayList<String> viewByValueList = new ArrayList();
        ArrayList dropedList = new ArrayList();
        if (fromDescrete == null) {
            viewByValueList = userLayerdao.getAllMeasuresViewbys(bussRoleId);
            dropedList = userLayerdao.getViewbyValues(bussRoleId, elementID);
        } else {
            viewByValueList = userLayerdao.getTargetPeriodDetails(elementID);
        }

        GenerateDragAndDrophtml dragAndDrophtml = new GenerateDragAndDrophtml("Drag values from here", "Drop values here", dropedList, viewByValueList, request.getContextPath());
        String htmlStr = dragAndDrophtml.getDragAndDropDiv();
        response.getWriter().print(htmlStr);

        return null;
    }

    public ActionForward testWhetherExcelFileExist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String bussId = request.getParameter("bussId");
        String elementID = request.getParameter("elementID");
        boolean testforFile = false;
        UserLayerDAO userLayerdao = new UserLayerDAO();
        testforFile = userLayerdao.getFileExistingOrNot(bussId, elementID);
        if (testforFile) {
            response.getWriter().print("File Exist");
        } else {
            response.getWriter().print("File Not Exist");
        }
        return null;
    }

    public ActionForward insertTargetParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String bussId = request.getParameter("bussRoleId");
        String elementID = request.getParameter("elementID");
        String viewByVals = request.getParameter("viewByVals");
        String[] viewByvalues = viewByVals.split(",");
        List<String> viewbys = new ArrayList<String>();
        viewbys.addAll(Arrays.asList(viewByvalues));
        UserLayerDAO userLayerdao = new UserLayerDAO();
        userLayerdao.updateTargetParameters(bussId, elementID, viewbys);
        return null;
    }

    public ActionForward downLoadTargetValuesDuration(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String startValue = request.getParameter("startValue");
        String elementID = request.getParameter("elementID");
        String endValue = request.getParameter("endValue");
        String periodtype = request.getParameter("periodtype");
        String eleName = request.getParameter("eleName");
        List<String> targetPeriodDetails = new ArrayList<String>();
        List<String> targetValues = new ArrayList<String>();
        List<String> publishType = new ArrayList<String>();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        targetPeriodDetails = userLayerDAO.getTargetPeriodDetails(startValue, endValue, elementID, periodtype);
        targetValues = userLayerDAO.getTargetValues(startValue, endValue, elementID, periodtype);
        publishType = userLayerDAO.getPublishType(startValue, endValue, elementID, periodtype);
        DataSnapshotGenerator reportTemplate = new DataSnapshotGenerator();
        String fileNameinCache = "";
        fileNameinCache = reportTemplate.targetMeasureExcel(elementID, eleName, targetPeriodDetails, targetValues, publishType);
        session.setAttribute("targetMeasureExcelFile", fileNameinCache);
        return null;
    }

    public ActionForward downLoadMonthWiseData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String rollType = request.getParameter("rollType");
        String elementID = request.getParameter("elementID");
        String bussId = request.getParameter("bussId");
        String elemntName = request.getParameter("monthVal");
        String regionName = request.getParameter("regionName");
        String monthName = request.getParameter("monthName");
        String fileNameinCache = "";
        List<String> targetPeriodDetails = new ArrayList<String>();
        ArrayList dateAndEleId = new ArrayList();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        dateAndEleId = userLayerDAO.getEndDateValue(elementID, monthName, bussId, regionName);
        ArrayList regionNames = new ArrayList();
        ArrayList monthNames = new ArrayList();
        HashMap<Integer, HashMap<Integer, ArrayList>> multiMap = new HashMap<Integer, HashMap<Integer, ArrayList>>();
        PbReturnObject pbretObj = null;
        int noofRolling = 0;
        if (Integer.parseInt(rollType) != 0) {
            noofRolling = Integer.parseInt(rollType);
//                for(int i=0; i<Integer.parseInt(rollType)+1; i++){
            SimpleDateFormat datefrmt = new SimpleDateFormat();
            datefrmt.applyPattern("yyyy-MM-dd hh:mm:ss");
            Date date = datefrmt.parse(dateAndEleId.get(1).toString());
            SimpleDateFormat datefrmt1 = new SimpleDateFormat("MM/dd/yyyy");
            String datevalue = datefrmt1.format(date);

//                     int endDate =Integer.parseInt(datevalue.substring(datevalue.length()-4, datevalue.length()))-i;
//                     String fianlDateValue = datevalue.replace(datevalue.substring(datevalue.length()-4, datevalue.length()), Integer.toString(endDate));
            ArrayList timeDetailsArray = new ArrayList();
            ArrayList rowViewbyCols = new ArrayList();
            ArrayList colViewbyCols = new ArrayList();
            ArrayList queryElements = new ArrayList();
            ArrayList aggrigation = new ArrayList();

            aggrigation = userLayerDAO.getMeasureAggration(elementID);
//
//                     timeDetailsArray.add("Day");
//                     timeDetailsArray.add("PRG_STD");
//                     timeDetailsArray.add(fianlDateValue);
//                     timeDetailsArray.add("Month");
//                     timeDetailsArray.add("Last Period");
            timeDetailsArray.add("Day");
            timeDetailsArray.add("PRG_MONTH_ROLLING");
            timeDetailsArray.add(datevalue);
            // timeDetailsArray.add("Month");
            timeDetailsArray.add("Last " + (Integer.parseInt(rollType) + 1) + " Months");
//                     timeDetailsArray.add("Day");
//                     timeDetailsArray.add("PRG_DAY_ROLLING");
//                     timeDetailsArray.add(datefrmt1.format(date));
//                    // timeDetailsArray.add(periodtype);
//                     timeDetailsArray.add("Last 365 Period");
            colViewbyCols.add("Time");
            rowViewbyCols.add(dateAndEleId.get(0));
            queryElements.add(elementID);
            PbReportQuery repQuery = new PbReportQuery();

            repQuery.setRowViewbyCols(rowViewbyCols);
            repQuery.setColViewbyCols(colViewbyCols);
            repQuery.setQryColumns(queryElements);
            repQuery.setColAggration(aggrigation);
            repQuery.setTimeDetails(timeDetailsArray);
            //PbTimeRanges ranges = new PbTimeRanges();
            //ranges.setDateJava(timeDetailsArray);
            //ranges.setJavaTrendRange("Month","Last Period",fianlDateValue);
            pbretObj = repQuery.getPbReturnObject(String.valueOf(elementID));
            if (pbretObj.rowCount >= 0) {
                for (int j = 0; j < pbretObj.rowCount; j++) {
                    regionNames.add(pbretObj.getFieldValueString(j, 0));
                    monthNames.add(pbretObj.getFieldValueString(j, 1));
                }

            }

//                }
        }
        DataSnapshotGenerator reportTemplate = new DataSnapshotGenerator();
        fileNameinCache = reportTemplate.downLoadRegionViewExcelSheet(elementID, regionName, pbretObj, regionNames, monthNames, noofRolling + 1, elemntName);
        session.setAttribute("targetMeasureExcelFile", fileNameinCache);

        return null;
    }

    public ActionForward downLoadYoyandMomOfTarge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String elementID = request.getParameter("elementID");
        String bussId = request.getParameter("bussRoleId");

        String rolingYearId = request.getParameter("rolingYearId");
        String noofRollingyears = "";

        String typeofRollingmonthId = request.getParameter("typeofRollingmonthId");
        String noofrolingMonths = "";

        String noDescrMonths = "";
        ArrayList descriMonthsviewBys = new ArrayList();
        ArrayList supportiveMeas = new ArrayList();
        int selectYear = 0;
        int noofRolling = 0;
        String rollingYearsNo = "";
        String rollingandDescNo = "";
        HashMap rollingYearMap = new HashMap();
        HashMap discreMonthMap = new HashMap();
        if (rolingYearId.equalsIgnoreCase("yes")) {
            noofRollingyears = request.getParameter("noofRollingyears");
            UserLayerDAO layerDao = new UserLayerDAO();

            supportiveMeas = layerDao.getRollingDate(elementID);
            String endValue = supportiveMeas.get(1).toString();
            selectYear = Integer.parseInt(noofRollingyears);
            if (selectYear != 0) {
                for (int i = 0; i < selectYear; i++) {
                    SimpleDateFormat datefrmt = new SimpleDateFormat();
                    datefrmt.applyPattern("dd-MMM-yy");
                    int endDate = Integer.parseInt(endValue.substring(endValue.length() - 2, endValue.length())) - i;

                    Date date = datefrmt.parse(endValue.replace(endValue.substring(endValue.length() - 2, endValue.length()), Integer.toString(endDate)));
                    SimpleDateFormat datefrmt1 = new SimpleDateFormat("MM/dd/yyyy");
                    ArrayList timeDetailsArray = new ArrayList();
                    ArrayList rowViewbyCols = new ArrayList();
                    ArrayList queryElements = new ArrayList();
                    ArrayList aggrigation = new ArrayList();
                    ArrayList tiemDetailsData = new ArrayList();
                    aggrigation = layerDao.getMeasureAggration(elementID);
                    timeDetailsArray.add("Day");
                    timeDetailsArray.add("PRG_STD");
                    timeDetailsArray.add(datefrmt1.format(date));
                    timeDetailsArray.add(supportiveMeas.get(0));
                    timeDetailsArray.add("Last Period");
                    rowViewbyCols.add("Time");
                    queryElements.add(elementID);
                    PbReportQuery repQuery = new PbReportQuery();
                    PbReturnObject pbretObj = null;
                    repQuery.setRowViewbyCols(rowViewbyCols);
                    repQuery.setColViewbyCols(new ArrayList());
                    repQuery.setQryColumns(queryElements);
                    repQuery.setColAggration(aggrigation);
                    repQuery.setTimeDetails(timeDetailsArray);
                    pbretObj = repQuery.getPbReturnObject(String.valueOf(elementID));
                    if (pbretObj.rowCount >= 0) {
                        for (int j = 0; j < pbretObj.rowCount; j++) {
                            tiemDetailsData.add(pbretObj.getFieldValueString(j, 1));
                        }
                        rollingYearMap.put(i, tiemDetailsData);
                    }
                }
            }
        } else {
            rollingYearsNo = "0";
        }
        if (typeofRollingmonthId.equalsIgnoreCase("descrMonths")) {
            noDescrMonths = request.getParameter("rolingDescr");
            String viewByValmonts = request.getParameter("viewByValmonts");
            String[] viewByMonths = viewByValmonts.split(",");

            descriMonthsviewBys.addAll(Arrays.asList(viewByMonths));
            UserLayerDAO layerDao = new UserLayerDAO();
            ArrayList descreteMonths = new ArrayList();
            descreteMonths = layerDao.getDiscreteMonths(elementID, descriMonthsviewBys);
            if (!descreteMonths.isEmpty()) {
                for (int i = 0; i < descreteMonths.size(); i++) {
                    SimpleDateFormat datefrmt = new SimpleDateFormat();
                    datefrmt.applyPattern("dd-MMM-yy");
                    //int endDate =Integer.parseInt(descreteMonths.get(i).toString().substring(descreteMonths.get(i).toString().length()-2, descreteMonths.get(i).toString().length()))-i;

                    Date date = datefrmt.parse(descreteMonths.get(i).toString());
                    SimpleDateFormat datefrmt1 = new SimpleDateFormat("MM/dd/yyyy");
                    ArrayList timeDetailsArray = new ArrayList();
                    ArrayList rowViewbyCols = new ArrayList();
                    ArrayList colViewbyCols = new ArrayList();
                    ArrayList queryElements = new ArrayList();
                    ArrayList aggrigation = new ArrayList();
                    ArrayList tiemDetailsData = new ArrayList();
                    aggrigation = layerDao.getMeasureAggration(elementID);
                    timeDetailsArray.add("Day");
                    timeDetailsArray.add("PRG_STD");
                    timeDetailsArray.add(datefrmt1.format(date));
                    timeDetailsArray.add("Month");
                    timeDetailsArray.add("Last Period");
                    rowViewbyCols.add("Time");
                    queryElements.add(elementID);
                    PbReportQuery repQuery = new PbReportQuery();
                    PbReturnObject pbretObj = null;
                    repQuery.setRowViewbyCols(rowViewbyCols);
                    repQuery.setColViewbyCols(new ArrayList());
                    repQuery.setQryColumns(queryElements);
                    repQuery.setColAggration(aggrigation);
                    repQuery.setTimeDetails(timeDetailsArray);
                    pbretObj = repQuery.getPbReturnObject(String.valueOf(elementID));
                    if (pbretObj.rowCount >= 0) {
                        for (int j = 0; j < pbretObj.rowCount; j++) {
                            tiemDetailsData.add(pbretObj.getFieldValueString(j, 1));
                        }
                        discreMonthMap.put(i, tiemDetailsData);
                    }
                }
            }
        } else if (typeofRollingmonthId.equalsIgnoreCase("rolingMonths")) {
            noofrolingMonths = request.getParameter("rolingMonths");
            noofRolling = Integer.parseInt(noofrolingMonths);
//              if(noofRolling!=0){
////                for(int i=0; i<Integer.parseInt(rollType)+1; i++){
//                     SimpleDateFormat datefrmt = new SimpleDateFormat();
//                     datefrmt.applyPattern("yyyy-MM-dd hh:mm:ss");
//                     Date date = datefrmt.parse(dateAndEleId.get(1).toString());
//                     SimpleDateFormat datefrmt1 = new SimpleDateFormat("MM/dd/yyyy");
//                     String datevalue = datefrmt1.format(date);
//                     
////                     int endDate =Integer.parseInt(datevalue.substring(datevalue.length()-4, datevalue.length()))-i;
////                     String fianlDateValue = datevalue.replace(datevalue.substring(datevalue.length()-4, datevalue.length()), Integer.toString(endDate));
//                     ArrayList timeDetailsArray = new ArrayList();
//                     ArrayList rowViewbyCols = new ArrayList();
//                     ArrayList colViewbyCols = new ArrayList();
//                     ArrayList queryElements = new ArrayList();
//                     ArrayList aggrigation = new ArrayList();
//
//                     aggrigation=userLayerDAO.getMeasureAggration(elementID);
////
////                     timeDetailsArray.add("Day");
////                     timeDetailsArray.add("PRG_STD");
////                     timeDetailsArray.add(fianlDateValue);
////                     timeDetailsArray.add("Month");
////                     timeDetailsArray.add("Last Period");
//                     timeDetailsArray.add("Day");
//                     timeDetailsArray.add("PRG_MONTH_ROLLING");
//                     timeDetailsArray.add(datevalue);
//                    // timeDetailsArray.add("Month");
//                     timeDetailsArray.add("Last "+(Integer.parseInt(rollType)+1)+" Months");
////                     timeDetailsArray.add("Day");
////                     timeDetailsArray.add("PRG_DAY_ROLLING");
////                     timeDetailsArray.add(datefrmt1.format(date));
////                    // timeDetailsArray.add(periodtype);
////                     timeDetailsArray.add("Last 365 Period");
//                     colViewbyCols.add("Time");
//                     rowViewbyCols.add(dateAndEleId.get(0));
//                     queryElements.add(elementID);
//                     PbReportQuery repQuery = new PbReportQuery();
//
//                     repQuery.setRowViewbyCols(rowViewbyCols);
//                     repQuery.setColViewbyCols(colViewbyCols);
//                     repQuery.setQryColumns(queryElements);
//                     repQuery.setColAggration(aggrigation);
//                     repQuery.setTimeDetails(timeDetailsArray);
//                     //PbTimeRanges ranges = new PbTimeRanges();
//                     //ranges.setDateJava(timeDetailsArray);
//                     //ranges.setJavaTrendRange("Month","Last Period",fianlDateValue);
//                     pbretObj = repQuery.getPbReturnObject(String.valueOf(elementID));
//                     if(pbretObj.rowCount>=0){
//                       for(int j=0;j<pbretObj.rowCount;j++){
//                        regionNames.add(pbretObj.getFieldValueString(j, 0));
//                        monthNames.add(pbretObj.getFieldValueString(j, 1));
//                     }
//                  }
//
////                }
//              }
        } else {
            rollingandDescNo = "0";
        }
        String fileNameinCache = "";
        DataSnapshotGenerator reportTemplate = new DataSnapshotGenerator();
        fileNameinCache = reportTemplate.momYoyExcelDownLoad(elementID, discreMonthMap, rollingYearMap, descriMonthsviewBys, selectYear);
        session.setAttribute("targetMeasureExcelFile", fileNameinCache);
        return null;
    }

    public ActionForward supportMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String elementID = request.getParameter("elementID");
        String bussId = request.getParameter("bussRoleId");
        String elementName = request.getParameter("elementName");
        List<String> targetValues = new ArrayList<String>();
        List<String> publishType = new ArrayList<String>();
        PbReturnObject reportObj = new PbReturnObject();
        ReportTemplateDAO templatedao = new ReportTemplateDAO();
        reportObj = templatedao.getMeasureValues(elementID);
        for (int i = 0; i < reportObj.rowCount; i++) {
            if (!elementID.equalsIgnoreCase(reportObj.getFieldValueString(i, "ELEMENT_ID"))) {
                targetValues.add(reportObj.getFieldValueString(i, "USER_COL_DESC"));
                publishType.add(reportObj.getFieldValueString(i, "ELEMENT_ID"));
            }
        }
        StringBuilder targetValue = new StringBuilder();
        StringBuilder customNames = new StringBuilder();

        targetValue.append("{ MeasureNames :[");
        customNames.append(" MeasureIds :[");
        if (!targetValues.isEmpty()) {
            for (int i = 0; i < targetValues.size(); i++) {
                targetValue.append("\"").append(targetValues.get(i)).append("\"");
                customNames.append("\"").append(publishType.get(i)).append("\"");
                if (i != publishType.size() - 1) {
                    targetValue.append(",");
                    customNames.append(",");
                }
            }
            targetValue.append("],").append(customNames).append("]}");
            response.getWriter().print(targetValue.toString());
        } else {
            response.getWriter().print("No TargetValues");
        }

        return null;
    }

    public ActionForward getSupportiveMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String elementID = request.getParameter("elementID");
        UserLayerDAO templatedao = new UserLayerDAO();
        String supportiveMeas = "";
        supportiveMeas = templatedao.getSupportiveMeasures(elementID);
        if (!supportiveMeas.equalsIgnoreCase("")) {
            response.getWriter().print(supportiveMeas.split(",")[0]);
        } else {
            response.getWriter().print("no supportiveMeas");
        }
        return null;
    }

    public ActionForward updateSupportiveMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String elementID = request.getParameter("elementID");
        String supportMeasure = request.getParameter("supportMeasure");
        UserLayerDAO templatedao = new UserLayerDAO();
        templatedao.updateSupportiveMeasure(elementID, supportMeasure);
        return null;
    }

    //over by ramesh on  26-jun-12
    //added by anitha
    public ActionForward getOldRelatedMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String busRoleId = request.getParameter("busRoleId");
        UserLayerDAO dao = new UserLayerDAO();
        String result = dao.getOldRelatedMeasures(busRoleId, request);
        try {
            response.getWriter().print(result);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward deleteRelateMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String idsNew = request.getParameter("idsNew");
        String names1New = request.getParameter("names1New");
        //
        UserLayerDAO dao = new UserLayerDAO();
        boolean result = dao.deleteRelateMeasures(idsNew);
        try {
            response.getWriter().print(result);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward getdayLevelData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String elementID = request.getParameter("elementID");
        String monthName = request.getParameter("monthName");
        boolean manualAlloChecked = Boolean.parseBoolean(request.getParameter("manualAlloChecked"));
        boolean autoAlloChecked = Boolean.parseBoolean(request.getParameter("autoAlloChecked"));

        String[] targetVals = request.getParameterValues("targetValues");
        String[] percentVals = request.getParameterValues("targetPerValue");
        String[] nameofWeek = request.getParameterValues("nameofWeek");

        UserLayerDAO usrLayerDao = new UserLayerDAO();
        ArrayList monthDates = new ArrayList();
        ArrayList<String> dayDetails = new ArrayList<String>();
        ArrayList<String> weekLevelData = new ArrayList<String>();

        monthDates = usrLayerDao.getDayLevelDataForAllocation(elementID, monthName);
        dayDetails = usrLayerDao.getMonthWeeksValues(elementID, monthName);
        SimpleDateFormat datefrmt = new SimpleDateFormat();
        datefrmt.applyPattern("dd-MMM-yy");
        String nof = monthDates.get(1).toString().substring(0, 2);
        ArrayList days = new ArrayList();
        ArrayList dayvalues = new ArrayList();
        StringBuilder monthdays = new StringBuilder();
        StringBuilder dayNames = new StringBuilder();
        StringBuilder weekNo = new StringBuilder();
        StringBuilder weekLevelDistru = new StringBuilder();

        for (int i = 1; i <= Integer.parseInt(nof); i++) {
            Date date = datefrmt.parse(monthDates.get(0).toString().replace(monthDates.get(0).toString().substring(0, 2), Integer.toString(i)));
            days.add(i);
            if (date.toString().substring(0, 3).toString().equalsIgnoreCase("Mon")) {
                dayvalues.add("Monday");
            }
            if (date.toString().substring(0, 3).toString().equalsIgnoreCase("Tue")) {
                dayvalues.add("Tuesday");
            }
            if (date.toString().substring(0, 3).toString().equalsIgnoreCase("Wed")) {
                dayvalues.add("Wednesday");
            }
            if (date.toString().substring(0, 3).toString().equalsIgnoreCase("Thu")) {
                dayvalues.add("Thursday");
            }
            if (date.toString().substring(0, 3).toString().equalsIgnoreCase("Fri")) {
                dayvalues.add("Friday");
            }
            if (date.toString().substring(0, 3).toString().equalsIgnoreCase("Sat")) {
                dayvalues.add("Saturday");
            }
            if (date.toString().substring(0, 3).toString().equalsIgnoreCase("Sun")) {
                dayvalues.add("Sunday");
            }
        }
        if (nameofWeek != null) {
            String targetValue = monthDates.get(2).toString();
            weekLevelData = usrLayerDao.getCalculatedWeekData(targetVals, percentVals, nameofWeek, dayDetails, targetValue);
        }

        monthdays.append("{ MonthDays:[");
        dayNames.append(" MonthNames:[");
        weekNo.append(" WeekNumbers:[");
        weekLevelDistru.append(" WeekLevelDistru:[");
        for (int i = 0; i < dayvalues.size(); i++) {
            monthdays.append("\"").append(days.get(i)).append("\"");
            dayNames.append("\"").append(dayvalues.get(i)).append("\"");
            weekNo.append("\"").append(dayDetails.get(i)).append("\"");
            if (!weekLevelData.isEmpty()) {
                weekLevelDistru.append("\"").append(weekLevelData.get(i)).append("\"");
            }
            if (i != dayvalues.size() - 1) {
                monthdays.append(",");
                dayNames.append(",");
                weekNo.append(",");
                if (!weekLevelData.isEmpty()) {
                    weekLevelDistru.append(",");
                }
            }
        }
        if (!weekLevelData.isEmpty()) {
            monthdays.append("],").append(dayNames).append("],").append(weekNo).append("],").append(weekLevelDistru).append("]}");
        } else {
            monthdays.append("],").append(dayNames).append("],").append(weekNo).append("],").append("TargetValue:[").append("\"").append(monthDates.get(2).toString()).append("\"").append("]}");
        }
        response.getWriter().print(monthdays.toString());
        return null;
    }

    public ActionForward insertDeayLevelDataFrom(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String elementID = request.getParameter("elementID");
        String monthName = request.getParameter("monthName");
        String dayLevelallocation = request.getParameter("dayLevelallocation");
        boolean autoallocnManul = Boolean.parseBoolean(request.getParameter("autoMan"));
        boolean autoallocnPer = Boolean.parseBoolean(request.getParameter("autoPer"));
        boolean manualAllocation = Boolean.parseBoolean(request.getParameter("manual"));

//            String autoallocnManul =request.getParameter("autoMan");
//            String autoallocnPer =request.getParameter("autoPer");
//            String manualAllocation = request.getParameter("manual");
        String allocationtype = "";
        String[] weekNames = request.getParameterValues("weekNames");
        String[] monthDates = request.getParameterValues("monthDates");
        String[] monthDayNames = request.getParameterValues("monthDayNames");
        String[] targetValues = request.getParameterValues("daylevelTargetValues");
        ArrayList weekNamesArray = new ArrayList();
        ArrayList monthdatesArray = new ArrayList();
        ArrayList monthDaysArray = new ArrayList();
        ArrayList targetValuesArray = new ArrayList();
        weekNamesArray.addAll(Arrays.asList(weekNames));
        monthdatesArray.addAll(Arrays.asList(monthDates));
        monthDaysArray.addAll(Arrays.asList(monthDayNames));
        targetValuesArray.addAll(Arrays.asList(targetValues));

        if (autoallocnManul) {
            allocationtype = "autoallocationManual";
        } else if (autoallocnPer) {
            allocationtype = "autoallocationPercent";
        } else if (manualAllocation) {
            allocationtype = "manualallocation";
        } else {
            allocationtype = "weekLevelManual";
        }
        UserLayerDAO userLayer = new UserLayerDAO();
        userLayer.insertTargetDayLevelDetails(elementID, monthName, weekNamesArray, monthdatesArray, monthDaysArray, targetValuesArray, allocationtype, dayLevelallocation);

        return null;
    }

    public ActionForward getWeekLevelData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String elementID = request.getParameter("elementID");
        String monthName = request.getParameter("monthName");
        UserLayerDAO usrLayer = new UserLayerDAO();
        ArrayList<String> dayDetails = new ArrayList<String>();
        PbReturnObject retObj = null;
        int noofWeeks = 0;
        noofWeeks = usrLayer.getMonthWeeks(elementID, monthName);
        String TargetValue = usrLayer.getDayLevelDataForAllocation(elementID, monthName).get(2).toString();
        response.getWriter().print(TargetValue + "," + String.valueOf(noofWeeks));

        return null;
    }

    public ActionForward insertTargetWeeklevelData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String elementID = request.getParameter("elementID");
        String monthName = request.getParameter("monthName");
        String targetVal = request.getParameter("targetVal");
        int noofWeeks = Integer.parseInt(request.getParameter("noofWeeks"));
        String[] targetVals = request.getParameterValues("targetValues");
        String[] percentVals = request.getParameterValues("targetPerValue");
        String[] nameofWeek = request.getParameterValues("nameofWeek");
        UserLayerDAO usrLayer = new UserLayerDAO();
        ArrayList<String> dayDetails = new ArrayList<String>();

        ArrayList targetValsArray = new ArrayList();
        ArrayList percentValsArray = new ArrayList();
        ArrayList nameofWeekArray = new ArrayList();
        targetValsArray.addAll(Arrays.asList(targetVals));
        percentValsArray.addAll(Arrays.asList(percentVals));
        nameofWeekArray.addAll(Arrays.asList(nameofWeek));
        UserLayerDAO usrDao = new UserLayerDAO();
        usrDao.insertWeekLevelData(elementID, monthName, noofWeeks, targetValsArray, percentValsArray, nameofWeekArray);

        return null;
    }

    public ActionForward downLoadMonthDayLevelData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        HttpSession session = request.getSession(false);
        String elementID = request.getParameter("elementID");
        String bussId = request.getParameter("bussId");
        String elemntName = request.getParameter("monthVal");
        String regionName = request.getParameter("regionName");
        String monthName = request.getParameter("monthName");
        String levelTypeData = request.getParameter("levelTypeData");
        String fileNameinCache = "";
        String monthTargetVal = "";
        ArrayList dateAndEleId = new ArrayList();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        dateAndEleId = userLayerDAO.getEndDateValue(elementID, monthName, bussId, regionName);
        monthTargetVal = userLayerDAO.getMonthTargetValue(monthName, elementID);

        ArrayList regionNames = new ArrayList();
        ArrayList testFordayandAllocation = new ArrayList();
        PbReturnObject pbretObj = null;

        SimpleDateFormat datefrmt = new SimpleDateFormat();
        datefrmt.applyPattern("yyyy-MM-dd hh:mm:ss");
        Date date = datefrmt.parse(dateAndEleId.get(1).toString());
        SimpleDateFormat datefrmt1 = new SimpleDateFormat("MM/dd/yyyy");
        String datevalue = datefrmt1.format(date);
        ArrayList timeDetailsArray = new ArrayList();
        ArrayList rowViewbyCols = new ArrayList();
        ArrayList colViewbyCols = new ArrayList();
        ArrayList queryElements = new ArrayList();
        ArrayList aggrigation = new ArrayList();

        aggrigation = userLayerDAO.getMeasureAggration(elementID);
        timeDetailsArray.add("Day");
        timeDetailsArray.add("PRG_MONTH_ROLLING");
        timeDetailsArray.add(datevalue);
        timeDetailsArray.add("Last 1 Months");
        colViewbyCols.add("Time");
        rowViewbyCols.add(dateAndEleId.get(0));
        queryElements.add(elementID);
        PbReportQuery repQuery = new PbReportQuery();

        repQuery.setRowViewbyCols(rowViewbyCols);
        repQuery.setColViewbyCols(colViewbyCols);
        repQuery.setQryColumns(queryElements);
        repQuery.setColAggration(aggrigation);
        repQuery.setTimeDetails(timeDetailsArray);
        pbretObj = repQuery.getPbReturnObject(String.valueOf(elementID));
        if (pbretObj.rowCount >= 0) {
            for (int j = 0; j < pbretObj.rowCount; j++) {
                regionNames.add(pbretObj.getFieldValueString(j, 0));
            }
        }
        if (levelTypeData.equalsIgnoreCase("weekLevelAlloction")) {
            pbretObj = userLayerDAO.getWeekLevelDetalsData(elementID, monthName);
        } else {
            pbretObj = userLayerDAO.getDayLevelDetalsData(elementID, monthName);
        }

        if (pbretObj.rowCount > 0) {
            testFordayandAllocation = userLayerDAO.getDayOrWeekandAllocationType(elementID, monthName, levelTypeData);
            DataSnapshotGenerator reportTemplate = new DataSnapshotGenerator();
            if (levelTypeData.equalsIgnoreCase("weekLevelAlloction")) {
                fileNameinCache = reportTemplate.downLoadMonthWeekLevleData(elementID, regionName, regionNames, elemntName, pbretObj, monthTargetVal, testFordayandAllocation);
            } else {
                fileNameinCache = reportTemplate.downLoadMonthDayLevleData(elementID, regionName, regionNames, elemntName, pbretObj, monthTargetVal, testFordayandAllocation);
            }
            session.setAttribute("targetMeasureExcelFile", fileNameinCache);
        } else {
            response.getWriter().print("No Data");
        }
        return null;
    }

    //Started by Nazneen For CCC
    public ActionForward saveCompanyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String companyName = request.getParameter("companyName");
        String companyDesc = request.getParameter("companyDesc");
        int status = new UserLayerDAO().saveCompanyDetails(companyName, companyDesc);
        try {
            response.getWriter().print(status);
        } catch (IOException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getCompanyConnDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String companyId = request.getParameter("companyId");
        try {
            response.getWriter().print(userLayerDAO.getCompanyConnDetails(companyId));
        } catch (IOException e) {
            logger.error("Exception:", e);
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getAllCompanyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getAllCompanyDetails());
        } catch (IOException e) {
            logger.error("Exception:", e);
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getloadtypeDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String companyId = request.getParameter("companyId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getloadtypeDetails(companyId));
        } catch (IOException e) {logger.error("Exception:", e);
        } catch (SQLException e) {logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getCompanyName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String companyId = request.getParameter("companyId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getCompanyName(companyId));
        } catch (IOException e) {logger.error("Exception:", e);
        } catch (SQLException e) {logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward saveConnectionsDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String companyId = request.getParameter("companyId");
        String loadType = request.getParameter("loadType");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String server = request.getParameter("server");
        String serviceId = request.getParameter("serviceId");
        String serviceName = request.getParameter("serviceName");
        String port = request.getParameter("port");
        String SourceTimeZone = request.getParameter("SourceTimeZone");
        String TargetTimeZone = request.getParameter("TargetTimeZone");
        String dsnName = request.getParameter("dsnName");
        String dbConnType = request.getParameter("dbConnType");
        String dbName = request.getParameter("dbName");
        String runIndep = request.getParameter("runIndep");

//        UserLayerDAO userLayerDAO = new UserLayerDAO();
        int status = new UserLayerDAO().saveConnectionsDetails(companyId, loadType, userName, password, server, serviceId, serviceName, port, SourceTimeZone, TargetTimeZone, dsnName, dbConnType, dbName, runIndep);
        try {
            response.getWriter().print(status);
        } catch (IOException e) {logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward deleteConnectionsDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String tabId = request.getParameter("tabId");
//        UserLayerDAO userLayerDAO = new UserLayerDAO();
        int status = new UserLayerDAO().deleteConnectionsDetails(tabId);
        try {
            response.getWriter().print(status);
        } catch (IOException e) {logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward editCompanyConnDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String tabId = request.getParameter("tabId");
        String compId = request.getParameter("compId");
        try {
            response.getWriter().print(userLayerDAO.editCompanyConnDetails(tabId, compId));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward updateConnectionsDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String tabId = request.getParameter("tabId");
        String loadType = request.getParameter("loadType");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String server = request.getParameter("server");
        String serviceId = request.getParameter("serviceId");
        String serviceName = request.getParameter("serviceName");
        String port = request.getParameter("port");
        String SourceTimeZone = request.getParameter("SourceTimeZone");
        String TargetTimeZone = request.getParameter("TargetTimeZone");
        String dsnName = request.getParameter("dsnName");
        String dbConnType = request.getParameter("dbConnType");
        String dbName = request.getParameter("dbName");
        String runIndep = request.getParameter("runIndep");

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        int status = new UserLayerDAO().updateConnectionsDetails(tabId, loadType, userName, password, server, serviceId, serviceName, port, SourceTimeZone, TargetTimeZone, dsnName, dbConnType, dbName, runIndep);
        try {
            response.getWriter().print(status);
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getCompanyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getCompanyDetails());
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward editCompanyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String compId = request.getParameter("compId");
        try {
            response.getWriter().print(userLayerDAO.editCompanyDetails(compId));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward updateCompanyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String companyName = request.getParameter("companyName");
        String companyDesc = request.getParameter("companyDesc");
        String compId = request.getParameter("compId");

//        UserLayerDAO userLayerDAO = new UserLayerDAO();
        int status = new UserLayerDAO().updateCompanyDetails(compId, companyName, companyDesc);
        try {
            response.getWriter().print(status);
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //end of code by Nazneen for CCC
//Start user assignment for company..by Nazneen

    public ActionForward getCompAssignedUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String companyId = request.getParameter("companyId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getCompAssignedUsers(companyId));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward assignuserToComp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String companyId = request.getParameter("companyId");
        String[] users = request.getParameter("users").split(",");
        String[] userName = request.getParameter("userNames").split(",");
        String connectionID = request.getParameter("connectionID");
        PrintWriter printWriter = response.getWriter();
        int htmlUpdationResult = 0;
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        htmlUpdationResult = userLayerDAO.assignuserToComp(users, userName, companyId, connectionID);
        printWriter.print(htmlUpdationResult);

        return null;
    }

    public ActionForward getCompAvailableUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getCompAvailableUsers());
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //End user assignment for company..by Nazneen
    //Start DIm and Fact Sec..by Nazneen

    public ActionForward getDimMemberDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connectionID = request.getParameter("connectionID");
        String folderId = request.getParameter("folderID");
        String dimId = request.getParameter("dimId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String jsonstring = userLayerDAO.getDimMemberDetails(connectionID, folderId, dimId);
        response.getWriter().print(jsonstring);

        return null;
    }

    public ActionForward getFactTabDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String connectionID = request.getParameter("connectionID");
        String folderID = request.getParameter("folderID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getFactTabDetails(userId, connectionID, folderID));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getFactMeasureDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String connectionID = request.getParameter("connectionID");
        String foldersSelected = request.getParameter("foldersSelected");
        String tablesSelected = request.getParameter("tablesSelected");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getFactMeasureDetails(userId, connectionID, foldersSelected, tablesSelected));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward applySecurity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String elementId = request.getParameter("elementId");

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        int status = new UserLayerDAO().applySecurity(elementId);
        try {
            response.getWriter().print(status);
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //End DIm and Fact Sec..by Nazneen
    //started by nazneen for Killing a running process

    public ActionForward getProcessListDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getProcessListDetails());
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward killProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String processId = request.getParameter("processId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        int status = new UserLayerDAO().killProcess(processId);
        try {
            response.getWriter().print(status);
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getUsersDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getUsersDetails());
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getMemDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//        String connectionID=request.getParameter("connectionID");
//        String folderId=request.getParameter("folderID");
//        String dimId=request.getParameter("dimId");
//        String memId=request.getParameter("memId");
        String elementId = request.getParameter("elementId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getMemDetails(elementId));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getDimentionMembersDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String roleID = request.getParameter("roleId");
        String memberId = request.getParameter("memId");
        String contextPath = request.getParameter("contextPath");
        String userId = request.getParameter("userId");
        String subFolderIdUser = request.getParameter("subFolderIdUser");
        String finalQuery = "";

        PrintWriter out = null;
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String html = userLayerDAO.getMembersForDimensionDragAndDrop(roleID, memberId, contextPath, userId, subFolderIdUser);
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        out.print(html.toString());
        return null;

    }

    public ActionForward addUserDimensionMemberValuesForRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String subFolderIdUser = request.getParameter("subFolderIdUser");
        String allVal = request.getParameter("meemberValues");
        String userMemId = request.getParameter("userMemId");
        String elementId = request.getParameter("elementId");
        String subFolderId = request.getParameter("subFolderId");
        String dimId = request.getParameter("dimId");
//        HttpSession session = request.getSession();
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String memberValuesXml = "";
        String[] meemberValues = null;

//        if (allVal.length() > 1) {
//            allVal = allVal.replaceAll(";", "'||chr(38)||'");
//        }
        if (!allVal.equalsIgnoreCase("")) {
            allVal = allVal.replace("%20", " ").replace("%26", "&").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%40", "@");
            allVal = allVal.replace("%23", "#").replace("%24", "$").replace("%25", "%").replace("%5E", "^").replace("%60", "`").replace("%3C", "<").replace("%3E", ">");
            meemberValues = allVal.split(",");
        }
        if (meemberValues != null) {
            memberValuesXml = userLayerDAO.toXMLSec(meemberValues);
            int len = memberValuesXml.length();
            memberValuesXml = memberValuesXml.substring(0, len - 1);
        }
        String folderId = userLayerDAO.getFolderID(Integer.parseInt(subFolderIdUser));

        String status = "";
        HashMap details = userLayerDAO.checkMemberValueStatusForRole(folderId, userMemId);
        String filterId = (String) details.get("filterId");
        status = (String) details.get("status");
        if (status.equalsIgnoreCase("false")) {
            userLayerDAO.addUserMemberFilterForRole(folderId, memberValuesXml, userMemId, elementId, subFolderId, dimId);
        } else if (status.equalsIgnoreCase("true")) {
            userLayerDAO.updateMemberValueStatusForRole(folderId, userMemId, memberValuesXml, filterId, elementId, subFolderId, dimId);
        }

//        String connectionId = (String) session.getAttribute("connId");
//        MetadataDAO metadataDAO = new MetadataDAO();
//        ArrayList<Cube> cubes = (ArrayList<Cube>) metadataDAO.getUserFolderList(connectionId);
//        session.setAttribute("CUBES", cubes);
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);

    }

    public ActionForward addUserDimensionMemberValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String subFolderIdUser = request.getParameter("subFolderIdUser");
        String userId = request.getParameter("userId");
        String allVal = request.getParameter("meemberValues");
        String userMemId = request.getParameter("userMemId");
        String elementId = request.getParameter("elementId");
        String dimId = request.getParameter("dimId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String[] meemberValues = null;
        String memberValuesXml = "";
//        HttpSession session = request.getSession();
//        if (allVal.length() > 1) {
//
//            allVal = allVal.replaceAll(";", "'||chr(38)||'");
//        }
//        if (!allVal.equalsIgnoreCase("")) {
//            meemberValues = allVal.split(",");
//        }

        if (!allVal.equalsIgnoreCase("")) {
            allVal = allVal.replace("%20", " ").replace("%26", "&").replace("%3A", ":").replace("%2F", "/").replace("%3F", "?").replace("%40", "@");
            allVal = allVal.replace("%23", "#").replace("%24", "$").replace("%25", "%").replace("%5E", "^").replace("%60", "`").replace("%3C", "<").replace("%3E", ">");
            meemberValues = allVal.split(",");
        }

        if (meemberValues != null) {
            memberValuesXml = userLayerDAO.toXMLSec(meemberValues);
            int len = memberValuesXml.length();
            memberValuesXml = memberValuesXml.substring(0, len - 1);
        }
        String status = "";
        HashMap details = userLayerDAO.checkMemberValueStatus(userId, subFolderIdUser, userMemId);
        String filterId = (String) details.get("filterId");
        status = (String) details.get("status");
        if (status.equalsIgnoreCase("false")) {
            userLayerDAO.addUserMemberFilter(userId, subFolderIdUser, memberValuesXml, userMemId, elementId, dimId);
        } else if (status.equalsIgnoreCase("true")) {
            userLayerDAO.updateMemberValueStatus(userId, subFolderIdUser, userMemId, memberValuesXml, filterId, elementId, dimId);
        }
//        String connectionId = (String) session.getAttribute("connId");
//        MetadataDAO metadataDAO = new MetadataDAO();
//        ArrayList<Cube> cubes = (ArrayList<Cube>) metadataDAO.getUserFolderList(connectionId);
//        session.setAttribute("CUBES", cubes);
        PrintWriter out = response.getWriter();
        out.println("2");
        return mapping.findForward(null);
    }
    //added for creating empty role

    public ActionForward addUserFolderEmpty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String folderName = request.getParameter("fldName");
        String folderDesc = request.getParameter("fldDesc");
        String grpId = request.getParameter("grpId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        userLayerDAO.truncateDimValues();
        userLayerDAO.insertNewDimValues();
//       boolean result = false;
        boolean result = userLayerDAO.addUserFolderEmpty(folderName, folderDesc, grpId);
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        out.print(result);
        return mapping.findForward("userLayer");
    }

    public ActionForward getGroupsDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionID = request.getParameter("connectionID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getGroupsDetails(connectionID));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getRolesDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String groupId = request.getParameter("groupId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getRolesDetails(groupId));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward deleteRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String folderId = request.getParameter("folderId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        //boolean result = userLayerDAO.deleteUserFolder(folderId);
        PrintWriter out = response.getWriter();
        boolean result = false;
//        if (status.equalsIgnoreCase("false")) {
//            result = userLayerDAO.deleteUserFolder(folderId);
//            out.println("2");
//        } else {
//            out.println("1");
        result = userLayerDAO.deleteUserFolder(folderId);
        if (result == false) {
            out.println("1");
        } else {
            out.println("2");
        }
//        }
        //added by susheela over

        // response.setContentType("text/xml");
        //out.print(result);
        // return mapping.findForward("userLayer");
        return mapping.findForward(null);
    }

    public ActionForward publishBussinessRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String folderId = request.getParameter("folderId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = false;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.publishBussinessRole(folderId);
        if (result == false) {
            out.println("2");
        } else {
            out.println("1");
        }

        return mapping.findForward(null);
    }

    public ActionForward rePublishBussinessRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String folderId = request.getParameter("folderId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = false;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.rePublishBussinessRole(folderId);
        if (result == false) {
            out.println("2");
        } else {
            out.println("1");
        }

        return mapping.findForward(null);
    }

    public ActionForward updateRoleDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String roleName = request.getParameter("roleName");
        String roleDesc = request.getParameter("roleDesc");
        String folderId = request.getParameter("folderId");

//     UserLayerDAO userLayerDAO = new UserLayerDAO();
        int status = new UserLayerDAO().updateRoleDetails(roleName, roleDesc, folderId);
        try {
            response.getWriter().print(status);
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward collectPlanData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connId");
        String procName = request.getParameter("procName");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = "false";
        PrintWriter out = response.getWriter();
        result = userLayerDAO.collectPlanData(connId, procName);
        if (result.equalsIgnoreCase("true")) {
            out.println("1");
        } else if (result.equalsIgnoreCase("false")) {
            out.println("2");
        } else {
            out.println("3");
        }

        return mapping.findForward(null);
    }

    public ActionForward getPlantCodeDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionID = request.getParameter("connectionID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getPlantCodeDetails(connectionID));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getPlantPeriodDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionID = request.getParameter("connectionID");
        String plantCode = request.getParameter("plantCode");
        String planScenario = request.getParameter("planScenario");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getPlantPeriodDetails(connectionID, plantCode, planScenario));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getPlanVersionDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionID = request.getParameter("connectionID");
        String plantCode = request.getParameter("plantCode");
        String planScenario = request.getParameter("planScenario");
        String plantPeriod = request.getParameter("plantPeriod");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getPlanVersionDetails(connectionID, plantCode, plantPeriod, planScenario));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getcheckIsPublished(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connId = request.getParameter("connectionID");
        String plantCode = request.getParameter("plantCode");
        String plantPeriod = request.getParameter("plantPeriod");
        String planVersion = request.getParameter("planVersion");
        String planScenario = request.getParameter("planScenario");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getcheckIsPublished(connId, plantCode, plantPeriod, planVersion, planScenario));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward publishPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connId = request.getParameter("connectionID");
        String plantCode = request.getParameter("plantCode");
        String plantPeriod = request.getParameter("plantPeriod");
        String planVersion = request.getParameter("planVersion");
        String planScenario = request.getParameter("planScenario");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = false;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.publishPlan(connId, plantCode, plantPeriod, planVersion, planScenario);
        if (result == true) {
            out.println("1");
        } else {
            out.println("2");
        }

        return mapping.findForward(null);
    }
    //added by Nazneen for Segmentation

    public ActionForward getElementIdDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String columnName = request.getParameter("columnName");
        String elementId = columnName.substring(2);
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getElementIdDetails(elementId));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getElementColType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String columnName = request.getParameter("columnName");
        String elementId = columnName.substring(2);
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getElementColType(elementId));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getPlanScenarioDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionID = request.getParameter("connectionID");
        String plantCode = request.getParameter("plantCode");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getPlanScenarioDetails(connectionID, plantCode));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getElementBussTabName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String elmntId = request.getParameter("elmntId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getElementBussTabName(elmntId));
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward saveMgmtCalender(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String calenderName = request.getParameter("calenderName");
        String conid = request.getParameter("conid");
        String denomTable = request.getParameter("denomTable");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = false;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.saveMgmtCalender(calenderName, conid, denomTable);
        if (result == true) {
            out.println("1");
        } else {
            out.println("2");
        }
        return mapping.findForward(null);
    }

    public ActionForward getCompanyDetailsForConn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getCompanyDetailsForConn());
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getCompanyCalDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String conid = request.getParameter("conid");
        String companyId = request.getParameter("companyId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getCompanyCalDetails(conid, companyId));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward SaveCalenderToComp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String companyId = request.getParameter("companyId");
        String calenderId = request.getParameter("calenderId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = false;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.SaveCalenderToComp(companyId, calenderId);
        if (result == true) {
            out.println("1");
        } else {
            out.println("2");
        }
        return mapping.findForward(null);
    }

    public ActionForward getCompanyCalanders(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String conid = request.getParameter("conid");
        String companyId = request.getParameter("companyId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getCompanyCalanders(conid, companyId));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward saveMarkCalender(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String companyId = request.getParameter("companyId");
        String calenderId = request.getParameter("calenderId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = false;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.saveMarkCalender(companyId, calenderId);
        if (result == true) {
            out.println("1");
        } else {
            out.println("2");
        }
        return mapping.findForward(null);
    }

    public ActionForward getPlanDataDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connectionID = request.getParameter("connectionID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String jsonstring = userLayerDAO.getPlanDataDetails(connectionID);
        response.getWriter().print(jsonstring);

        return null;
    }

    public ActionForward getSecurityVal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String elementId = request.getParameter("elementId");

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String jsonstring = userLayerDAO.getSecurityVal(elementId);
        response.getWriter().print(jsonstring);

        return null;
    }

    public ActionForward deleteSecurityVal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, Exception {
        String filterId = request.getParameter("filterId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String status = userLayerDAO.deleteSecurityVal(filterId);
        response.getWriter().print(status);
        return null;
    }

    public ActionForward getMultiSecurityVal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String elementId = request.getParameter("elementId");
        String userId = request.getParameter("userId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String jsonstring = userLayerDAO.getMultiSecurityVal(elementId, userId);
        response.getWriter().print(jsonstring);

        return null;
    }

    public ActionForward deleteMultiSecurityVal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, Exception {
        String filterId = request.getParameter("filterId");
        String userId = request.getParameter("userId");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String status = userLayerDAO.deleteMultiSecurityVal(filterId, userId);
        response.getWriter().print(status);
        return null;
    }
//end of code by Nazneen

    public ActionForward getcheckIsCollected(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String connId = request.getParameter("connectionID");
        String plantCode = request.getParameter("plantCode");
        String planScenario = request.getParameter("planScenario");
        String planRunDate = request.getParameter("planRunDate");
        String procName = "drl_master_load";
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String result = "false";
        PrintWriter out = response.getWriter();
        //
        result = userLayerDAO.getcheckIsCollected(connId, plantCode, planScenario, planRunDate, procName);
        if (result.equalsIgnoreCase("true")) {
            out.println("Plan Collected successfully");
        } else if (result.equalsIgnoreCase("false")) {
            out.println("Plan not Collected");
        } else {
            out.println("3");
        }

        return mapping.findForward(null);
    }

    public ActionForward getPlanScenarioDetails11(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionID = request.getParameter("connectionID");
        String plantCode = request.getParameter("plantCode");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getPlanScenarioDetails11(connectionID, plantCode));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getPlantCodeDetails11(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionID = request.getParameter("connectionID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getPlantCodeDetails11(connectionID));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getPlantRunDateDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String connectionID = request.getParameter("connectionID");
        String plantCode = request.getParameter("plantCode");
        String planScenario = request.getParameter("planScenario");
        //
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {

            response.getWriter().print(userLayerDAO.getPlantPeriodDetails11(connectionID, plantCode, planScenario));
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;

    }

    public ActionForward getUseridDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getUseridDetails());
        } catch (IOException e) {
             logger.error("Exception:", e);
        } catch (SQLException e) {
            logger.error("Exception:", e);
        } catch (ParseException e) {
            logger.error("Exception:", e);
        }
        return null;

    }

//added by Nazneen for re publishing fact and dimensions members
    public ActionForward rePublishDimension(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connID = request.getParameter("connectionID");
        String folderId = request.getParameter("foldersSelect");
        String dimId = request.getParameter("dimSelect");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = false;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.rePublishDimension(connID, folderId, dimId);
        if (result == false) {
            out.println("2");
        } else {
            out.println("1");
        }

        return mapping.findForward(null);
    }

    public ActionForward rePublishFact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connID = request.getParameter("connectionID");
        String folderId = request.getParameter("foldersSelect");
        String bussTableId = request.getParameter("factSelect");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        boolean result = false;
        PrintWriter out = response.getWriter();
        result = userLayerDAO.rePublishFact(connID, folderId, bussTableId);
        if (result == false) {
            out.println("2");
        } else {
            out.println("1");
        }

        return mapping.findForward(null);
    }

    public ActionForward getAllFactsDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connectionID = request.getParameter("connectionID");
        String folderId = request.getParameter("folderID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String jsonstring = userLayerDAO.getAllFactsDetails(connectionID, folderId);
        response.getWriter().print(jsonstring);

        return null;
    }

    public ActionForward modifyTableDispName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conId = request.getParameter("conid");
        String folderID = request.getParameter("folderID");
        String[] bussTableName = request.getParameterValues("bussTableName");
        String[] oldTableDispName = request.getParameterValues("oldTableDispName");
        String[] newTableDispName = request.getParameterValues("newTableDispName");
        String[] bussTableId = request.getParameterValues("bussTableId");
        int jsonstring = 1;

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        for (int i = 0; i < bussTableName.length; i++) {
            if (!oldTableDispName[i].equals(newTableDispName[i])) {
                jsonstring = userLayerDAO.modifyTableDispName(conId, folderID, newTableDispName[i], bussTableId[i]);
            }
        }
        response.getWriter().print(jsonstring);
        return null;
    }
    //end Of nazneen code
    //start of code  by Nazneen for Modify Custom Measures

    public ActionForward getAllCustomMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getSession(false).getAttribute("USERID");
        String connectionID = request.getParameter("connectionID");
        String foldersSelected = request.getParameter("foldersSelected");
        String tablesSelected = request.getParameter("tablesSelected");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            response.getWriter().print(userLayerDAO.getAllCustomMeasures(userId, connectionID, foldersSelected, tablesSelected));
        } catch (IOException e) {
             logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    //added by krishan pratap
    public ActionForward getallTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("reportId");
        GenerateDragAndDrophtmlForExcel generateDragAndDrophtml = new GenerateDragAndDrophtmlForExcel();
        String dragndrop = generateDragAndDrophtml.getAllTemplate(request, reportId);
        try {
            response.getWriter().print(dragndrop);
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //addede by krishan pratap

    public ActionForward getAllDimsDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connectionID = request.getParameter("connectionID");
        String folderId = request.getParameter("folderID");
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        String jsonstring = userLayerDAO.getAllDimsDetails(connectionID, folderId);
        response.getWriter().print(jsonstring);

        return null;
    }
    //addede by krishan pratap

    public ActionForward modifyTableDispDimName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String conId = request.getParameter("conid");
        String folderID = request.getParameter("folderID");
        //String[] bussTableName = request.getParameterValues("bussTableName");
        String[] oldTableDispName = request.getParameterValues("oldTableDispName");
        String[] newTableDispName = request.getParameterValues("newTableDispName");
        String[] bussTableId = request.getParameterValues("bussTableId");
        //String[]dimid=request.getParameterValues("sNO");
        int jsonstring = 1;

        UserLayerDAO userLayerDAO = new UserLayerDAO();
        for (int i = 0; i < oldTableDispName.length; i++) {
            if (!oldTableDispName[i].equals(newTableDispName[i])) {
                jsonstring = userLayerDAO.getTableDispDimName(folderID, newTableDispName[i], bussTableId[i]);
            }
        }
        response.getWriter().print(jsonstring);
        return null;
    }

    //Added by Ashutosh for Restricted Power Analyzer 11-12-2015
    public ActionForward isReportCreator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("REPORTID");
        String userId = (String) request.getSession(false).getAttribute("USERID");

        UserLayerDAO userLayerDAO = new UserLayerDAO();

        try {
            String data = userLayerDAO.isReportCreator(userId, reportId);
            System.out.print("ashu" + data);
            response.getWriter().print(data);
        } catch (IOException e) { logger.error("Exception:", e);
        } catch (SQLException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //Added by Ashutosh for Veraction Navigation Menu

    public ActionForward getSSOUserLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String ssoValue = (String) session.getAttribute("ssoToken");
        HashMap userInfo = new HashMap<>();
        String https_url="";
        ServletContext context = this.getServlet().getServletContext();
        try {
            https_url = context.getInitParameter("sso_links").toString();
            https_url = https_url.replace("$", ssoValue);
//            String https_url = "https://service.veraction.com/sso/session/" + ssoValue + "/links?appToken=710f4b14-6064-4166-9297-cca19b40d503";
//            String https_url = "https://localhost:7701/sso/session/" + ssoValue + "/links?appToken=710f4b14-6064-4166-9297-cca19b40d503";

            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] certs = new TrustManager[]{
                new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String t) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String t) {
                    }
                }
            };
            ssl_ctx.init(null, // key manager
                    certs, // trust manager
                    new SecureRandom()); // random number generator
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//           String https_url = "https://localhost:7700/sso/session/"+ssoValue+"/validate?appToken=veraction";
            URL url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            // Guard against "bad hostname" errors during handshake.
            con.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String host, SSLSession sess) {
                    if (host.equals("app-ft-30.mem-tw.veraction.net")) {
//                    if (host.equals("localhost")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            if (con != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input = "";
                String str = "";
                PrintWriter out = response.getWriter();
                while ((input = br.readLine()) != null) {
                    str = input;
}
                out.print(str);
            }
        } catch (IOException e) {
            logger.error("Exception:", e);
        } catch (KeyManagementException e) {
            logger.error("Exception:", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }

    public ActionForward getUserRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        UserLayerDAO userLayerDAO = new UserLayerDAO();
        try {
            String data = userLayerDAO.getUserRole(session);
            response.getWriter().print(data);
        } catch (IOException e) { logger.error("Exception:", e);
        }catch (NullPointerException e) {
            logger.error("Exception:", e);
        }
        return null;
    }
    //Code Ended by Ashutosh

    @Override
    protected Map getKeyMethodMap() {
        HashMap methodKeyMap = new HashMap();
        methodKeyMap.put("addUserFolder", "addUserFolder");
        methodKeyMap.put("copyUserFolder", "copyUserFolder");
        methodKeyMap.put("getUserFolderList", "getUserFolderList");
        methodKeyMap.put("getUserFolderList1", "getUserFolderList1");
        methodKeyMap.put("getUserFolderList2", "getUserFolderList2");
        methodKeyMap.put("deleteUserFolder", "deleteUserFolder");
        methodKeyMap.put("publishUserFolder", "publishUserFolder");
        methodKeyMap.put("updateElemetIdFolder", "updateElemetIdFolder");//
        methodKeyMap.put("deleteRoleDimension", "deleteRoleDimension"); //
        methodKeyMap.put("deleteRoleDimensionMember", "deleteRoleDimensionMember");
        methodKeyMap.put("editCustomeRoleDrill", "editCustomeRoleDrill");
        methodKeyMap.put("updateDimensionStatus", "updateDimensionStatus");
        methodKeyMap.put("updateDimensionMemberStatus", "updateDimensionMemberStatus");
//        methodKeyMap.put("makeRoleForTarget", "makeRoleForTarget");
        methodKeyMap.put("checkBussRoleForTarget", "checkBussRoleForTarget");
        methodKeyMap.put("getBussRoleForTarget", "getBussRoleForTarget");
        methodKeyMap.put("editUserCustomRoleDrill", "editUserCustomRoleDrill"); //
        methodKeyMap.put("addUserDimMemberValues", "addUserDimMemberValues");
        methodKeyMap.put("addUserLayerFilter", "addUserLayerFilter");//
        methodKeyMap.put("deleteDimMemberValues", "deleteDimMemberValues");
        methodKeyMap.put("checkRoleFormulaQuery", "checkRoleFormulaQuery");
        methodKeyMap.put("saveRoleFormula", "saveRoleFormula");
        methodKeyMap.put("addUserDimMemberValuesForRole", "addUserDimMemberValuesForRole");
        methodKeyMap.put("deleteDimMemberValuesForRole", "deleteDimMemberValuesForRole"); //
        methodKeyMap.put("migrateChangesToRole", "migrateChangesToRole");//
        methodKeyMap.put("migrateChangesToRolePublish", "migrateChangesToRolePublish");//migrateChangesToRoleNew
        methodKeyMap.put("migrateChangesToRoleNew", "migrateChangesToRoleNew");
        methodKeyMap.put("migrateChangesToRoleNewPublish", "migrateChangesToRoleNewPublish");
        methodKeyMap.put("saveRoleParamFormula", "saveRoleParamFormula");
        methodKeyMap.put("updateFactStatus", "updateFactStatus");
        //added by uday
        methodKeyMap.put("copyUser", "copyUser");
        methodKeyMap.put("updateTargetMeasureForRole", "updateTargetMeasureForRole");
        methodKeyMap.put("updateFactskey", "updateFacts");
        methodKeyMap.put("publishFact", "publishFact");
        methodKeyMap.put("assignRoleToUser", "assignRoleToUser");
        methodKeyMap.put("assignRolesToUser", "assignRolesToUser");
        methodKeyMap.put("getDimentionMembers", "getDimentionMembers");
        methodKeyMap.put("getSenSitivityFactors", "getSenSitivityFactors");
        //added by malli
        methodKeyMap.put("getAllMeasures", "getAllMeasures");
        methodKeyMap.put("getAllConnectionForModifyMeasure", "getAllConnectionForModifyMeasure");
        methodKeyMap.put("getFolderDetails", "getFolderDetails");
        methodKeyMap.put("getUpdateMeasureDetails", "getUpdateMeasureDetails");
        // added ramesh janakuttu
        methodKeyMap.put("getAllConnectionForModifyMembers", "getAllConnectionForModifyMembers");
        methodKeyMap.put("getAllMembers", "getAllMembers");
        methodKeyMap.put("getModifyMembers", "getModifyMembers");
        methodKeyMap.put("getAllConnectionForModifyDimensions", "getAllConnectionForModifyDimensions");
        methodKeyMap.put("getAllDimensions", "getAllDimensions");
        methodKeyMap.put("partialPublishDimentions", "partialPublishDimentions");
        methodKeyMap.put("SaveExtraMembers", "SaveExtraMembers");
        methodKeyMap.put("getDimDetails", "getDimDetails");
        methodKeyMap.put("getTabDetails", "getTabDetails");
        //by sunita
        methodKeyMap.put("getTableDetails1", "getTableDetails1");
        methodKeyMap.put("deleteRow", "deleteRow");
        methodKeyMap.put("deleteRow1", "deleteRow1");
        methodKeyMap.put("getAllColumns", "getAllColumns");
        methodKeyMap.put("getGroupDetails", "getGroupDetails");
        methodKeyMap.put("partialPublishDimentionsCheck", "partialPublishDimentionsCheck");
        /// added by priyanka
        methodKeyMap.put("getcheckIsCollected", "getcheckIsCollected");
        methodKeyMap.put("getPlantCodeDetails11", "getPlantCodeDetails11");
        methodKeyMap.put("getPlanScenarioDetails11", "getPlanScenarioDetails11");
        methodKeyMap.put("getPlantRunDateDetails", "getPlantRunDateDetails");
        methodKeyMap.put("getUseridDetails", "getUseridDetails");
//added by anil
        methodKeyMap.put("getBusinessRolesMethod", "getBusinessRolesMethod");
        methodKeyMap.put("mappedElements", "mappedElements");
        //added by Nazneen
        methodKeyMap.put("getEmailConfigDetails", "getEmailConfigDetails");
        methodKeyMap.put("saveEmailConfigDetails", "saveEmailConfigDetails");
//added by anil
        methodKeyMap.put("getDefineMeasures", "getDefineMeasures");
        methodKeyMap.put("mappedRelatedMeasure", "mappedRelatedMeasure");
        methodKeyMap.put("usersForRoles", "usersForRoles");
        methodKeyMap.put("saveUsersForReports", "saveUsersForReports");
        methodKeyMap.put("repAnddbrdsForUser", "repAnddbrdsForUser");
        methodKeyMap.put("getOldRelatedMeasures", "getOldRelatedMeasures");
        methodKeyMap.put("deleteRelateMeasures", "deleteRelateMeasures");
        methodKeyMap.put("insertTargetMeasureDetails", "insertTargetMeasureDetails");
        methodKeyMap.put("getTargetMeasuresDetails", "getTargetMeasuresDetails");
        methodKeyMap.put("testTargetsExistorNot", "testTargetsExistorNot");
        methodKeyMap.put("defineTargetPeriod", "defineTargetPeriod");
        methodKeyMap.put("getTargetMeasureTimeDetails", "getTargetMeasureTimeDetails");
        methodKeyMap.put("insertTargetMeasureValueDetails", "insertTargetMeasureValueDetails");
        methodKeyMap.put("getTargetValues", "getTargetValues");
        methodKeyMap.put("getallDimViewBys", "getallDimViewBys");
        methodKeyMap.put("testWhetherExcelFileExist", "testWhetherExcelFileExist");
        methodKeyMap.put("insertTargetParameters", "insertTargetParameters");
        methodKeyMap.put("downLoadTargetValuesDuration", "downLoadTargetValuesDuration");
        methodKeyMap.put("downLoadMonthWiseData", "downLoadMonthWiseData");
        methodKeyMap.put("downLoadYoyandMomOfTarge", "downLoadYoyandMomOfTarge");
        methodKeyMap.put("supportMeasures", "supportMeasures");
        methodKeyMap.put("updateSupportiveMeasures", "updateSupportiveMeasures");
        methodKeyMap.put("getSupportiveMeasures", "getSupportiveMeasures");
        methodKeyMap.put("getdayLevelData", "getdayLevelData");
        methodKeyMap.put("insertDeayLevelDataFrom", "insertDeayLevelDataFrom");
        methodKeyMap.put("getWeekLevelData", "getWeekLevelData");
        methodKeyMap.put("insertTargetWeeklevelData", "insertTargetWeeklevelData");
        methodKeyMap.put("downLoadMonthDayLevelData", "downLoadMonthDayLevelData");
        //added by Nazneen For CCC
        methodKeyMap.put("saveCompanyDetails", "saveCompanyDetails");
        methodKeyMap.put("getAllCompanyDetails", "getAllCompanyDetails");
        methodKeyMap.put("getCompanyConnDetails", "getCompanyConnDetails");
        methodKeyMap.put("getloadtypeDetails", "getloadtypeDetails");
        methodKeyMap.put("getCompanyName", "getCompanyName");
        methodKeyMap.put("saveConnectionsDetails", "saveConnectionsDetails");
        methodKeyMap.put("deleteConnectionsDetails", "deleteConnectionsDetails");
        methodKeyMap.put("editCompanyConnDetails", "editCompanyConnDetails");
        methodKeyMap.put("updateConnectionsDetails", "updateConnectionsDetails");
        methodKeyMap.put("getCompanyDetails", "getCompanyDetails");
        methodKeyMap.put("editCompanyDetails", "editCompanyDetails");
        methodKeyMap.put("updateCompanyDetails", "updateCompanyDetails");
        //user assignment for company
        methodKeyMap.put("getCompAssignedUsers", "getCompAssignedUsers");
        methodKeyMap.put("assignuserToComp", "assignuserToComp");
        methodKeyMap.put("getCompAvailableUsers", "getCompAvailableUsers");
        methodKeyMap.put("getDimMemberDetails", "getDimMemberDetails");
        methodKeyMap.put("getFactTabDetails", "getFactTabDetails");
        methodKeyMap.put("getFactMeasureDetails", "getFactMeasureDetails");
        methodKeyMap.put("applySecurity", "applySecurity");
        methodKeyMap.put("getProcessListDetails", "getProcessListDetails");
        methodKeyMap.put("killProcess", "killProcess");
        methodKeyMap.put("getUsersDetails", "getUsersDetails");
        methodKeyMap.put("getMemDetails", "getMemDetails");
        methodKeyMap.put("getDimentionMembersDetails", "getDimentionMembersDetails");
        methodKeyMap.put("addUserDimensionMemberValuesForRole", "addUserDimensionMemberValuesForRole");
        methodKeyMap.put("addUserDimensionMemberValues", "addUserDimensionMemberValues");
        methodKeyMap.put("addUserFolderEmpty", "addUserFolderEmpty");
        methodKeyMap.put("getGroupsDetails", "getGroupsDetails");
        methodKeyMap.put("getRolesDetails", "getRolesDetails");
        methodKeyMap.put("deleteRole", "deleteRole");
        methodKeyMap.put("publishBussinessRole", "publishBussinessRole");
        methodKeyMap.put("updateRoleDetails", "updateRoleDetails");
        methodKeyMap.put("rePublishBussinessRole", "rePublishBussinessRole");
        methodKeyMap.put("collectPlanData", "collectPlanData");
        methodKeyMap.put("getPlantCodeDetails", "getPlantCodeDetails");
        methodKeyMap.put("getPlantPeriodDetails", "getPlantPeriodDetails");
        methodKeyMap.put("getPlanVersionDetails", "getPlanVersionDetails");
        methodKeyMap.put("getcheckIsPublished", "getcheckIsPublished");
        methodKeyMap.put("publishPlan", "publishPlan");
        methodKeyMap.put("getElementIdDetails", "getElementIdDetails");
        methodKeyMap.put("getElementColType", "getElementColType");
        methodKeyMap.put("getPlanScenarioDetails", "getPlanScenarioDetails");
        methodKeyMap.put("getElementBussTabName", "getElementBussTabName");
        methodKeyMap.put("saveMgmtCalender", "saveMgmtCalender");
        methodKeyMap.put("getCompanyDetailsForConn", "getCompanyDetailsForConn");
        methodKeyMap.put("getCompanyCalDetails", "getCompanyCalDetails");
        methodKeyMap.put("SaveCalenderToComp", "SaveCalenderToComp");
        methodKeyMap.put("getCompanyCalanders", "getCompanyCalanders");
        methodKeyMap.put("saveMarkCalender", "saveMarkCalender");
        methodKeyMap.put("getPlanDataDetails", "getPlanDataDetails");
        methodKeyMap.put("getSecurityVal", "getSecurityVal");
        methodKeyMap.put("deleteSecurityVal", "deleteSecurityVal");
        methodKeyMap.put("getMultiSecurityVal", "getMultiSecurityVal");
        methodKeyMap.put("deleteMultiSecurityVal", "deleteMultiSecurityVal");
        methodKeyMap.put("rePublishDimension", "rePublishDimension");
        methodKeyMap.put("rePublishFact", "rePublishFact");
        methodKeyMap.put("getAllFactsDetails", "getAllFactsDetails");
        methodKeyMap.put("modifyTableDispName", "modifyTableDispName");
        methodKeyMap.put("getAllCustomMeasures", "getAllCustomMeasures");
        methodKeyMap.put("SubmitSetting", "SubmitSetting");
        methodKeyMap.put("getBuyer", "getBuyer");
        methodKeyMap.put("userRegistration", "userRegistration");
        methodKeyMap.put("getPendingUsers", "getPendingUsers");
        methodKeyMap.put("dActUsers", "dActUsers");
        methodKeyMap.put("getPUid", "getPUid");
        methodKeyMap.put("rejectUsers", "rejectUsers");
        methodKeyMap.put("reportsBasedOnRoles", "reportsBasedOnRoles");
        //addded by krishan
        methodKeyMap.put("getallTemplate", "getallTemplate");
        methodKeyMap.put("getAllDimsDetails", "getAllDimsDetails");
        methodKeyMap.put("modifyTableDispDimName", "modifyTableDispDimName");
        //Added by Ashutosh for Restricted Power Analyzer
        methodKeyMap.put("isReportCreator", "isReportCreator");
        methodKeyMap.put("getSSOUserLinks", "getSSOUserLinks");
        methodKeyMap.put("getUserRole", "getUserRole");

        return methodKeyMap;
    }
}
