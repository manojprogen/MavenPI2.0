/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.user;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 *
 * @author Ashutosh
 */
public class InvalidateSession extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(InvalidateSession.class);

    @Override
    protected Map getKeyMethodMap() {
        HashMap methodKeyMap = new HashMap();
        methodKeyMap.put("invalidate", "invalidate");
        return methodKeyMap;
    }

    public ActionForward invalidate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String ssoToken = request.getParameter("ssoToken");
        String appToken = (String) request.getSession(false).getAttribute("appToken");

        try {

            SessionListener.getSSOSession(ssoToken).invalidate();

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return null;
    }
}
