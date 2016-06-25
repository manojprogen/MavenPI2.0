package com.progen.wigdets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbReturnObject;

public class GetAllReports extends org.apache.struts.action.Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReturnObject pbro = new ProgenWidgetsDAO().getAllReports(Integer.parseInt(userId));
        request.getSession().setAttribute("links", pbro);
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("links-->" + pbro.getRowCount());




        return mapping.findForward(SUCCESS);
    }
}
