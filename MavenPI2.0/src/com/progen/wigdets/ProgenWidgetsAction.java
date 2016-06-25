package com.progen.wigdets;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class ProgenWidgetsAction extends LookupDispatchAction {

    private final static String SUCCESS = "success";

    public ActionForward getFavReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        PbReturnObject pbro = new ProgenWidgetsDAO().getFavReports(41);
        request.getSession().setAttribute("links", pbro);
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("links-->" + pbro);
        return mapping.findForward(SUCCESS);
    }

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getFavReports", "getFavReports");

        return map;
    }
}