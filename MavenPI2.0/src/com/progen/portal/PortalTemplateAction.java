/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import com.google.common.collect.Iterables;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
 * @author Santhosh.k
 */
public class PortalTemplateAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(PortalTemplateAction.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    PortalDAO PortalDAO = new PortalDAO();

    ///old code to delete start
//    public ActionForward insertTabMasterDet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//
//        HttpSession session = request.getSession(false);
//        String PORTAL_ID = String.valueOf(session.getAttribute("PORTALID"));
//        String PORTAL_TAB_NAME = request.getParameter("name");
//        String PORTAL_TAB_DESC = request.getParameter("desc");
//        String ORDER = request.getParameter("ORDER");
//        PbDb pbdb = new PbDb();
//        String seqnumQuery = "SELECT max(PORTAL_TAB_ORDER) FROM PRG_PORTAL_TAB_MASTER where portal_id=" + PORTAL_ID;
//        PbReturnObject pbroseq = pbdb.execSelectSQL(seqnumQuery);
//        String[] userList = new String[4];
//        userList[0] = PORTAL_ID;
//        userList[1] = PORTAL_TAB_NAME;
//        userList[2] = String.valueOf(pbroseq.getFieldValueInt(0, 0) + 1);
//        userList[3] = PORTAL_TAB_NAME;
//
//
//        boolean result = PortalDAO.insertTabMasterDet(userList);
//
//        return null;
//    }
    //old code to delete end   select max(portal_order) from PRG_PORTAL_TAB_MASTER
    public ActionForward insertTabMasterDet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String PORTAL_TAB_NAME = request.getParameter("name");
        String PORTAL_TAB_DESC = request.getParameter("desc");
        String USERID = String.valueOf(session.getAttribute("USERID"));
        PbDb pbdb = new PbDb();
        String orderQuery = "select max(portal_order) from PRG_PORTAL_TAB_MASTER";
        PbReturnObject pbroProTalOrderObject = pbdb.execSelectSQL(orderQuery);
        String[] userList = new String[5];
        userList[0] = "";
        userList[1] = PORTAL_TAB_NAME;
        userList[2] = PORTAL_TAB_DESC;
        userList[3] = String.valueOf(pbroProTalOrderObject.getFieldValueInt(0, 0) + 1);
        userList[4] = USERID;


        int result = PortalDAO.insertTabMasterDet(userList);
        if (result != 0) {
            List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
            Portal portal = new Portal(result, PORTAL_TAB_NAME);
            portals.add(portal);
            session.setAttribute("PORTALS", portals);
        }

        return null;
    }

    //used to create portals
    public ActionForward insertPortalMaster(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        // TODO: implement add method
        String path = "";
        HttpSession session = request.getSession(false);
        String USERID = String.valueOf(session.getAttribute("USERID"));
        PbDb pbdb = new PbDb();
        String seqnumQuery = "";
        PbReturnObject pbroseq = null;
        String seqnum = "";
        String PORTAL_NAME = request.getParameter("portalName");
        String PORTAL_DES = request.getParameter("portalDesc");
        String CREATED_BY = USERID;
        String UPDATE_BY = USERID;
        String CREATED_ON = "";
        String UPDATE_ON = "";
        String[] insretList = null;
        String[] userList = null;

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            CREATED_ON = "GETDATE()";
            UPDATE_ON = "GETDATE()";
            insretList = new String[5];
            insretList[0] = USERID;
            insretList[1] = PORTAL_NAME;
            insretList[2] = PORTAL_DES;
            insretList[3] = CREATED_BY;
            insretList[4] = UPDATE_BY;

            userList = new String[3];
            userList[0] = PORTAL_NAME;
            userList[1] = "1";
            userList[2] = PORTAL_DES;
        } else {
            CREATED_ON = "sysdate";
            UPDATE_ON = "sysdate";
            insretList = new String[6];
            seqnumQuery = "select PRG_PORTAL_MASTER_SEQ.nextval from dual";
            pbroseq = pbdb.execSelectSQL(seqnumQuery);
            seqnum = String.valueOf(pbroseq.getFieldValueInt(0, 0));
            insretList[0] = seqnum;
            insretList[1] = USERID;
            insretList[2] = PORTAL_NAME;
            insretList[3] = PORTAL_DES;
            insretList[4] = CREATED_BY;
            insretList[5] = UPDATE_BY;

            userList = new String[4];
            userList[0] = seqnum;
            userList[1] = PORTAL_NAME;
            userList[2] = "1";
            userList[3] = PORTAL_DES;

            session.setAttribute("PORTALID", seqnum);
        }

        boolean result = PortalDAO.insertPortalMaster(insretList);

        int result1 = PortalDAO.insertTabMasterDet(userList);

        String status = PORTAL_NAME + "~" + seqnum;

        response.getWriter().print(status);
        return null;


    }
    //old Code to delete start
    //used for inserting portlet detaisl
//    public ActionForward insertPortalDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session = request.getSession(false);
//        PrintWriter out = response.getWriter();
//        String USERID = String.valueOf(session.getAttribute("USERID"));
//        String PRG_PORTLET_TAB_ID = request.getParameter("portalTabId");
//        String COLUMN_NUM = request.getParameter("ColNum");
//        String SEQUENCE_NUM = request.getParameter("SeqNum");
//        String PORTAL_MAP_ID = String.valueOf(session.getAttribute("PORTALID"));
//        String portletName = request.getParameter("portletName");
//        String portletDesc = request.getParameter("portletDesc");
//        String sharable = request.getParameter("sharable");
//
//        String[] detailList =null;
//        String[] PortletMastrList = null;
//        int portletId = 0;
//        boolean result = false;
//
//        if (session != null) {
//            try {
//                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    detailList = new String[4];
//                    PortletMastrList = new String[9];
//                    detailList[0] = PRG_PORTLET_TAB_ID;
////                    detailList[1] = String.valueOf(portletId);
//                    detailList[1] = COLUMN_NUM;
//                    detailList[2] = SEQUENCE_NUM;
//                    detailList[3] = PORTAL_MAP_ID;
//
////                    PortletMastrList[0] = String.valueOf(portletId);
//                    PortletMastrList[0] = portletName;
//                    PortletMastrList[1] = portletDesc;
//                    PortletMastrList[2] = "I";
//                    PortletMastrList[3] = sharable;
//                    PortletMastrList[4] = USERID;
//                    PortletMastrList[5] = USERID;
//                    PortletMastrList[6] = "GETDATE()";
//                    PortletMastrList[7] = "GETDATE()";
//                    PortletMastrList[8] = "250";
//                }else{
//                    detailList = new String[5];
//                    PortletMastrList = new String[10];
//
//                    portletId = PortalDAO.getSequenceNumber("select PRG_PORTAL_TAB_DETAILS_SEQ.nextval from dual");
//                    detailList[0] = PRG_PORTLET_TAB_ID;
//                    detailList[1] = String.valueOf(portletId);
//                    detailList[2] = COLUMN_NUM;
//                    detailList[3] = SEQUENCE_NUM;
//                    detailList[4] = PORTAL_MAP_ID;
//
//                    PortletMastrList[0] = String.valueOf(portletId);
//                    PortletMastrList[1] = portletName;
//                    PortletMastrList[2] = portletDesc;
//                    PortletMastrList[3] = "I";
//                    PortletMastrList[4] = sharable;
//                    PortletMastrList[5] = USERID;
//                    PortletMastrList[6] = USERID;
//                    PortletMastrList[7] = "sysdate";
//                    PortletMastrList[8] = "sysdate";
//                    PortletMastrList[9] = "250";
//                }
//                //////////////////////////////////////////////////////////////////////.println("portletId is " + portletId);
//                result = PortalDAO.insertPortletMaster(PortletMastrList);
//                portletId = PortalDAO.getSequenceNumber("select max(portlet_id) NEXTVAL from PRG_PORTLETS_MASTER");
//                // //////////////////////////////////////////////////////////////////////.println("String.valueOf(portletId) is " + String.valueOf(portletId));
//                result = PortalDAO.insertPortalDetails(detailList);
//
//                out.print(portletId);
//
//                return null;
//            } catch (Exception exp) {
//                logger.error("Exception:",exp);
//                return mapping.findForward("exceptionPage");
//            }
//
//        } else {
//            return mapping.findForward("sessionExpired");
//        }
//
//    }
//old code to delete end

    public ActionForward insertPortalDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PrintWriter out = null;
        String USERID = String.valueOf(session.getAttribute("USERID"));
        String portalId = request.getParameter("portalTabId");
        String COLUMN_NUM = request.getParameter("ColNum");
        String SEQUENCE_NUM = request.getParameter("SeqNum");
        String portletName = request.getParameter("portletName");
        String portletDesc = request.getParameter("portletDesc");
        String sharable = request.getParameter("sharable");
        int portletId = 0;
        ArrayList<String> protletDetails = new ArrayList<String>();
        protletDetails.add(USERID);
        protletDetails.add(portalId);
        protletDetails.add(COLUMN_NUM);
        protletDetails.add(SEQUENCE_NUM);
        protletDetails.add(portletName);
        protletDetails.add(portletDesc);
        protletDetails.add(sharable);
        portletId = PortalDAO.insertPortalDetails(protletDetails);
        if (portletId != 0) {
            Portal portal = null;
            List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
            Iterator<Portal> moduleIter = Iterables.filter(portals, Portal.getAccessPortalPredicate(Integer.parseInt(portalId))).iterator();
            if (moduleIter.hasNext()) {
                portal = moduleIter.next();
            }
            PortLet portLet = new PortLet(portletId, portletName);
            portLet.setColumnNumber(Integer.parseInt(COLUMN_NUM));
            portLet.setSeqnumber(Integer.parseInt(SEQUENCE_NUM));
            portLet.setPortLetDes(portletDesc);
            portLet.setIsSharable(sharable);
            portal.addPortlet(portLet);
            out = response.getWriter();
            out.print(portletId);
        }
        return null;
    }
    //used to check for a portlet with same name and to retrict the user not to enter a portlet name which is already exists in database

    public ActionForward checkPortalName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ////////////////////////////////////////////////////////////////////////.println(" request.getParameter(PortalName);"+ request.getParameter("portalName"));
        String PortalName = request.getParameter("portalName");
        String status = "";

        PbReturnObject pbro = PortalDAO.checkPortalName();
        //pbro.writeString();
        HttpSession session = request.getSession(false);
        if (session != null) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                if (pbro.getFieldValueString(i, 0).equalsIgnoreCase(PortalName)) {
                    status = "This Portal Name Already Exists";
                    break;
                }
            }
            ////////////////////////////////////////////////////////////////////////.println("status---"+status);
            response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //is to check if the user has any portlas, if not then a dialogus is opened to create a portla other wise it is forwarded to portals page
    public ActionForward checkUserPortalExist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String path = "";
        String status = "";

        HttpSession session = request.getSession(false);

        String USERID = String.valueOf(session.getAttribute("USERID"));
        String fromModule = request.getParameter("fromModule");
        List<Portal> portals = PortalDAO.buildPortal(Integer.parseInt(USERID));
        // PbReturnObject pbro = PortalDAO.checkUserPortalExistance(USERID);
//        if (session != null) {
//            if (pbro.getRowCount() > 0) {
//                status = pbro.getFieldValueString(0, 0) + "~" + pbro.getFieldValueString(0, 1);
//            }
//            session.setAttribute("PORTALID", pbro.getFieldValueString(0, 1));
        session.setAttribute("PORTALS", portals);
        session.setAttribute("ONEVIEW", fromModule);
        response.getWriter().print(status);
        return null;
//        } else {
//            return mapping.findForward("sessionExpired");
//        }
    }

    public ActionForward getAllPortlets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String userId = (String) request.getSession().getAttribute("USERID");
        String portalID = request.getParameter("crtPortId");
        String portlets = PortalDAO.getAllPortlets(userId, portalID);
        response.getWriter().print(portlets);
        return null;
    }

    public ActionForward getAllPortletsForOneView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String userId = (String) request.getSession().getAttribute("USERID");
        String portlets = PortalDAO.getAllPortletsForOneView(userId);
        response.getWriter().print(portlets.toString());
        return null;
    }

    public ActionForward setModuleNameInSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String modulecode = request.getParameter("modulecode");
        session.setAttribute("setModuleNameInSession", modulecode);
        return null;
    }

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("insertTabMasterDet", "insertTabMasterDet");
        map.put("insertPortalMaster", "insertPortalMaster");
        map.put("insertPortalDetails", "insertPortalDetails");
        map.put("checkPortalName", "checkPortalName");
        map.put("checkUserPortalExist", "checkUserPortalExist");
        map.put("getAllPortlets", "getAllPortlets");
        map.put("getAllPortletsForOneView", "getAllPortletsForOneView");
        map.put("setModuleNameInSession", "setModuleNameInSession");
        return map;
    }
}