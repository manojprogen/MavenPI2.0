/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.dashboard;

import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.reportdesigner.action.GroupMeassureParams;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 *
 * @author srikanth.p This class is created to create and view the group
 * Meassure hirarchy at the front end
 */
public class GroupMeassureCreation extends LookupDispatchAction {

    /*
     * forward name="success" path=""
     */
    private static final String SUCCESS = "success";

    protected Map getKeyMethodMap() // <editor-fold defaultstate="collapsed" desc="comment">
    {// </editor-fold>
        Map map = new HashMap();
        map.put("saveGroup", "saveGroup");
        map.put("viewGroup", "viewGroup");
        map.put("insertElements", "insertElements");
        map.put("showParents", "showParents");
        map.put("showChilds", "showChilds");
        map.put("drillToReport", "drillToReport");
        map.put("saveReportAssignment", "saveReportAssignment");
        return map;
    }

    public ActionForward saveGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String folderId = request.getParameter("folderId");
        String groupName = request.getParameter("groupName");
        String UserId = "";
        if (session != null) {
            DashboardTemplateDAO dao = new DashboardTemplateDAO();
            String result = dao.saveGroup(folderId, groupName);
            out.write(result);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward viewGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String outPut = "";

        if (session != null) {
            PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
            outPut = viewerBd.ViewGroup();
            out.write(outPut);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward insertElements(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String groupId = request.getParameter("groupId");
        String folderId = request.getParameter("folderId");
        String elements = request.getParameter("elements");
        String groupName = request.getParameter("groupName");
        String parentElement = request.getParameter("parentElement");
        LinkedHashMap seqMap = new LinkedHashMap();
        GroupMeassureParams grpParams = new GroupMeassureParams();
        grpParams.setFolderId(folderId);
        grpParams.setGroupId(groupId);
        grpParams.setGroupName(groupName);
        grpParams.setMeassureId(parentElement);
//       HashMap groupMap=grpParams.getGroupMap();
//       groupMap.put(groupId, groupName);
//       session.setAttribute("groupMeassureParams",grpParams);
        ArrayList elementList = new ArrayList();
        String[] elementIds = elements.split(",");
        String result = "";
        PrintWriter out = response.getWriter();
        for (int i = 0; i < elementIds.length; i++) {
            elementList.add(elementIds[i]);
            seqMap.put(elementIds[i], i);
        }
        if (session != null) {
            DashboardTemplateDAO dao = new DashboardTemplateDAO();
            if (parentElement == null || parentElement.equalsIgnoreCase("")) {
                result = dao.insertParents(grpParams, seqMap);
            } else if (parentElement != null) {
                result = dao.insertChilds(grpParams, seqMap);

            }
            out.write(result);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }


    }

    public ActionForward showParents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String groupId = request.getParameter("groupId");
        PrintWriter out = response.getWriter();
        String outPut = "";

        if (session != null) {
            PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
            outPut = viewerBd.GroupMeassureBuildParents(groupId);
            out.write(outPut);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward showChilds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String groupId = request.getParameter("groupId");
        String parentId = request.getParameter("parentId");
        String folderId = request.getParameter("folderId");
        PrintWriter out = response.getWriter();
        String outPut = "";

        if (session != null) {
            PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
            outPut = viewerBd.GroupMeassureBuildChilds(groupId, parentId, folderId);
            out.write(outPut);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward drillToReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String groupId = request.getParameter("groupId");
        String dbrdId = request.getParameter("dbrdId");
        String dashletId = request.getParameter("dashletId");
        String rootElements = request.getParameter("rootElements");
        String folderId = request.getParameter("folderId");
        String outPut = "";
        PrintWriter out = response.getWriter();
        if (session != null) {
            PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
            String temp = rootElements.substring(rootElements.indexOf('[') + 1, rootElements.lastIndexOf(']'));
            String[] elems = temp.split(",");
            ArrayList rootElementsList = new ArrayList();
            ArrayList elementList = new ArrayList();
            for (int i = 0; i < elems.length; i++) {
                rootElementsList.add(elems[i].trim());
            }
            outPut = viewerBd.drillToReport(groupId, dbrdId, dashletId, folderId);
            out.write(outPut);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveReportAssignment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String groupId = request.getParameter("groupId");
        String dbrdId = request.getParameter("dbrdId");
        String dashletId = request.getParameter("dashletId");
        String elemNameList = request.getParameter("elemNameList");
        boolean status = false;
        String outPut = "";
        PrintWriter out = response.getWriter();
        HashMap reportMap = new HashMap();
        if (session != null) {
            String temp = elemNameList.substring(elemNameList.indexOf('[') + 1, elemNameList.lastIndexOf(']'));
            String[] elems = temp.split(",");
            ArrayList allElementNames = new ArrayList();
            ArrayList elementList = new ArrayList();
            for (int i = 0; i < elems.length; i++) {
                allElementNames.add(elems[i].trim());
            }
            for (int j = 0; j < allElementNames.size(); j++) {
                reportMap.put(allElementNames.get(j), request.getParameter(String.valueOf(allElementNames.get(j))));
            }
            status = dao.saveReportAssignment(reportMap, dbrdId, dashletId);
            if (status == true) {
                outPut = "Reports Successfully Assigned";
            } else {
                outPut = "Reports are Not Assigned Some Exception Occured...!!";

            }
            out.write(outPut);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
}
