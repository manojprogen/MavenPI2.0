/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;

/**
 *
 * @author Saurabh
 */
public class SaveTimeSetUp extends org.apache.struts.action.Action {

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
        PbDb pbdb = new PbDb();
        TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
        String finalQuery = "";
        String calName = request.getParameter("calName");
        String calId = request.getParameter("calId");
        String updateTimeSetUp = resBundle.getString("updateTimeSetUp");
        Object obj[] = new Object[2];
        obj[0] = calName;
        obj[1] = calId;
        finalQuery = pbdb.buildQuery(updateTimeSetUp, obj);
        pbdb.execModifySQL(finalQuery);

        return mapping.findForward(SUCCESS);
    }
}
