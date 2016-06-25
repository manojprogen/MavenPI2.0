package com.progen.querylayer;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class TableAction extends Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int i = 0;
        TableForm tableForm = (TableForm) form;
        ArrayList newList = new ArrayList();
        StringBuffer dropString = new StringBuffer();
        PrintWriter out = response.getWriter();
        //added by bharathi
        MyDAO mydao = new MyDAO();
//        newList = mydao.getConnections();
        /*
         * String method = request.getParameter("method");
         * ////////////////////.println("method" + method); if
         * (method.equalsIgnoreCase("databases")) { String connstr =
         * mydao.getConnections(); out.print(connstr);
         * //request.setAttribute("connectionsList", newList); // } else if
         * (method.equalsIgnoreCase("tablesandviews")) { String connid =
         * request.getParameter("connid"); ////////////////////.println("connid"
         * + connid); String tableviews = mydao.buildTablesAndViews(connid);
         * out.println(tableviews); } else if (method.equalsIgnoreCase("") ||
         * method == null) {
         */
//            String connstr = mydao.getConnections();
//            out.print(connstr);
        String connstr = mydao.getConnections();
//            out.print(connstr);
        request.setAttribute("buildedinjava", connstr);
        //request.setAttribute("connectionsList", newList);
        return mapping.findForward(SUCCESS);
    }
}
