/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Saurabh
 */
public class TimeSetUpAction extends org.apache.struts.action.Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ////////////////////////////////////////////////////////////////////////////////.println("time action");
        ArrayList newList = new ArrayList();
        HttpSession session;
        String connId = null;
        connId = request.getParameter("connId");
        if (request.getSession(false) == null) {
            ////////////////////////////////////////////////////////////////////////////////.println("in if ");
            session = request.getSession(true);

        } else {
            ////////////////////////////////////////////////////////////////////////////////.println("in else");
            session = request.getSession(false);
        }
        if (connId == null || connId.equalsIgnoreCase("")) {
            connId = String.valueOf(session.getAttribute("connId"));
            session.setAttribute("connId", connId);
            ////////////////////////////////////////////////////////////////////////////////.println("conId in if action--->"+connId);
        } else {
            session.setAttribute("connId", connId);
        }

        ////////////////////////////////////////////////////////////////////////////////.println("conId in action--->"+connId);
        newList = MyTimeSetUpListDAO.getTimeSetUps(connId);
        request.setAttribute("calList", newList);
        return mapping.findForward(SUCCESS);
    }
}
