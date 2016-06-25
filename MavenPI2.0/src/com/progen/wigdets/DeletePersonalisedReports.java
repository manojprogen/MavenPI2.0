package com.progen.wigdets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DeletePersonalisedReports extends org.apache.struts.action.Action {

    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String[] reports = request.getParameterValues("chk1");
        String csvReports = "";
        for (int i = 0; i < reports.length; i++) {
            csvReports = csvReports + "," + reports[i];
        }
        csvReports = csvReports.substring(1);
        new ProgenWidgetsDAO().deletePersonalisedReports(userId, csvReports);

        return mapping.findForward(SUCCESS);
    }
}
