/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.saveusertype;

import com.progen.superadmin.SuperAdminBd;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 *
 * @author Saurabh
 */
public class SaveUserTypeAction extends LookupDispatchAction {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("saveUsertype", "saveUsertype");
        map.put("modifyUsertype", "modifyUsertype");

        return map;
    }

    public ActionForward saveUsertype(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String userdec = request.getParameter("userDec");
        String userTypeName = request.getParameter("userTypeName");
        String[] userlslist = new String[2];
        PrintWriter out = response.getWriter();
        userlslist[0] = userTypeName;
        userlslist[1] = userdec;
        SaveUsertypeDAO budao = new SaveUsertypeDAO();
        boolean isAvailable = budao.isUserTypeAvailable(userTypeName);
        if (!isAvailable) {
            budao.saveUsertype(userlslist);
            out.print("");
        } else {
            out.print("available");
        }

        return null;
    }

    public ActionForward modifyUsertype(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String userType = request.getParameter("userType");
        String userId = request.getParameter("userId");
        ////////////////////////////////////////////.println.println(userType+" in mod userId "+userId);
        SaveUsertypeDAO budao = new SaveUsertypeDAO();
        InputStream inputstream = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/superAdmin.xml");
        String xmlData = "";
        SuperAdminBd adminBd = new SuperAdminBd();
        xmlData = adminBd.convertStreamToString(inputstream);
        adminBd.deleteModulesForUserType(Integer.parseInt(userId));
        adminBd.checkUserPrivileges(Integer.parseInt(userType), xmlData, request.getSession(false));
        adminBd.saveModulesForUserType(request.getSession(false), Integer.parseInt(userId), Integer.parseInt(userType));
        budao.updateUserTypeForUser(userId, userType);
        PrintWriter out = response.getWriter();
        out.println("1");

        return null;
    }
}