/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;

/**
 *
 * @author Administrator
 */
public class SaveStartPage extends org.apache.struts.action.Action {

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
        PbUserLayerResourceBundle resBundle = new PbUserLayerResourceBundle();
        PbDb pbdb = new PbDb();

        String userId = request.getParameter("userId");
        //   ////.println("userid--"+userId);
        String startpage = request.getParameter("startlogin");
        //   ////.println("startpage-----"+startpage);
        String checkId = request.getParameter("checkId");
        //    ////.println("checkId----"+checkId);
        String sql = "update PRG_AR_USERS  set start_page='" + startpage + "'where PU_ID in(" + checkId + ")";
        //     ////.println("sql--------::"+sql);
        String from = request.getParameter("from");

        pbdb.execModifySQL(sql);

        if (from.equalsIgnoreCase("startpage")) {
            return null;
        }

        return mapping.findForward(SUCCESS);
    }
}
