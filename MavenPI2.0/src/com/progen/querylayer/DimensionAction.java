package com.progen.querylayer;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DimensionAction extends Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String method = request.getParameter("method");

        HttpSession session;
        if (method == null || method.equalsIgnoreCase("")) {
            ArrayList newList = new ArrayList();

            String connId = request.getParameter("connId");
            if (request.getSession(false) == null) {

                session = request.getSession(true);
            } else {

                session = request.getSession();
            }
            if (connId == null || connId.equalsIgnoreCase("")) {
                connId = String.valueOf(session.getAttribute("connId"));
                session.setAttribute("connId", connId);

            } else {
                session.setAttribute("connId", connId);
            }
            MydimensionDynamicDao dynadimdao = new MydimensionDynamicDao();
//            ArrayList dynadimlist = dynadimdao.getDimensions(connId);
            String dynadimlist = dynadimdao.getDimensions(connId);
            request.setAttribute("dimensionslist", dynadimlist);
            request.setAttribute("IsConnection", "YES");
//            return mapping.findForward("dynadimensions");
            return mapping.findForward(SUCCESS);

            // newList = MyDimensionDAO.getDimensions(connId);


            // if(connId==null || connId.equalsIgnoreCase("")){
//            return mapping.findForward(SUCCESS);
            //  }else{
            // return null;
            // }
        }/*
         * else if (method.equalsIgnoreCase("dimdynamic")) { String connId =
         * request.getParameter("connId");
         *
         * if (request.getSession() == null) { session =
         * request.getSession(true); } else { session = request.getSession(); }
         * if (connId == null || connId.equalsIgnoreCase("")) { connId =
         * String.valueOf(session.getAttribute("connId"));
         * session.setAttribute("connId", connId);
         *
         * } else { session.setAttribute("connId", connId); }
         * MydimensionDynamicDao dynadimdao = new MydimensionDynamicDao(); //
         * ArrayList dynadimlist = dynadimdao.getDimensions(connId); String
         * dynadimlist = dynadimdao.getDimensions(connId);
         * request.setAttribute("dimensionslist", dynadimlist);
         * request.setAttribute("IsConnection", "YES"); // return
         * mapping.findForward("dynadimensions"); return
         * mapping.findForward("successdims"); }/* else if
         * (method.equalsIgnoreCase("showdimlist")) { String dimid =
         * request.getParameter("dimids"); String dimname =
         * dimid.substring(dimid.indexOf(",") + 1, dimid.length()); dimid =
         * dimid.substring(0,dimid.indexOf(","));
         *
         *
         *
         * PrintWriter out = response.getWriter(); // String connId =
         * request.getParameter("connId");
         *
         * MydimensionDynamicDao dynadimdao = new MydimensionDynamicDao();
         * String dynadimstr = dynadimdao.getTableList(dimid);
         * out.println(dynadimstr); return null; } /*else if
         * (method.equalsIgnoreCase("showdimlist")) { PrintWriter out =
         * response.getWriter();
         *
         * String dimids = request.getParameter("dimids");
         *
         * MydimensionDynamicDao dynadimdao = new MydimensionDynamicDao();
         *
         * String tablestr = dynadimdao.getTableList(dimids);
         * out.print(tablestr); return null; } /*else if
         * (method.equalsIgnoreCase("dynamicdims")) { PrintWriter out =
         * response.getWriter(); MydimensionDynamicDao dynadimdao = new
         * MydimensionDynamicDao(); String connId =
         * request.getParameter("connId"); if (request.getSession() == null) {
         *
         * session = request.getSession(true);
         *
         * } else {
         *
         * session = request.getSession(); } if (connId == null ||
         * connId.equalsIgnoreCase("")) { connId =
         * String.valueOf(session.getAttribute("connId"));
         * session.setAttribute("connId", connId);
         *
         * } else { session.setAttribute("connId", connId); }
         *
         *
         * //String dimstr = dynadimdao.getDimensions(connId);
         * //out.print(dimstr); // request.setAttribute("dimstr", dimstr);
         *
         * return mapping.findForward("dynadimensions"); } /* else
         * if(method.equalsIgnoreCase("dynamicdimsagain")){ PrintWriter out =
         * response.getWriter(); MydimensionDynamicDao dynadimdao = new
         * MydimensionDynamicDao(); String connId =
         * request.getParameter("connId"); if (request.getSession() == null) {
         *
         * session = request.getSession(true);
         *
         * } else {
         *
         * session = request.getSession(); } if (connId == null ||
         * connId.equalsIgnoreCase("")) { connId =
         * String.valueOf(session.getAttribute("connId"));
         * session.setAttribute("connId", connId);
         *
         * } else { session.setAttribute("connId", connId); }
         *
         *
         * String dimstr = dynadimdao.getDimensions(connId); out.print(dimstr);
         * return null; } else if(method.equalsIgnoreCase("dimdyanmic")) {
         * return mapping.findForward("dynadimensions"); } else
         * if(method.equalsIgnoreCase("showdimensions")){
         *
         * }
         */ else {
        }
        return null;
    }
}
