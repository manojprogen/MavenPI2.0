package com.progen.wigdets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Saurabh
 */
public class SaveFavReport extends Action {

    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        String reportIds = "";
        String userId = null;
        String[] values = request.getParameterValues("chk1");

        if (session != null) {
            for (int i = 0; i < values.length; i++) {
                reportIds = reportIds + "," + values[i];
            }
            reportIds = reportIds.substring(1);
            userId = String.valueOf(session.getAttribute("USERID"));
            session.setAttribute("chk1", request.getParameterValues("chk1"));
            //  new ProgenWidgetsDAO().saveFavReport(reportIds, userId);

            return mapping.findForward("success");
        } else {
            return mapping.findForward("sessionExpired");
        }



    }
}
