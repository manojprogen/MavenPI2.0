/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbReturnObject;

/**
 *
 * @author Administrator
 */
public class SortTabdetails extends LookupDispatchAction {

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
        map.put("button.add", "add");
        map.put("button.edit", "edit");
        map.put("sortUserlist", "sortUserlist");
        map.put("useracclistSel", "useracclistSel");
        map.put("useTypelistSel", "useTypelistSel");
        return map;
    }

    /**
     * Action called on Add button click
     */
    public ActionForward add(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        // TODO: implement add method
        return mapping.findForward(SUCCESS);
    }

    public ActionForward sortUserlist(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        String selvalUList = request.getParameter("selval");
        String seloptionuList = request.getParameter("seloption");
        UserLayerDAO userLayerdao = new UserLayerDAO();
        PbReturnObject prgr = userLayerdao.sortUserlist(selvalUList, seloptionuList);
        session.setAttribute("prgr", prgr);
        session.setAttribute("seloptionuList", seloptionuList);
        session.setAttribute("selvalUList", selvalUList);
        ////.println("prgr len::::::" + prgr.getRowCount());
        return null;
    }

    public ActionForward useracclistSel(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        String selectedVal = request.getParameter("selval1");
        ////.println("");
        String selectedoption = request.getParameter("seloption1");
        UserLayerDAO userLayerdao = new UserLayerDAO();
        PbReturnObject userlistaccprgr = userLayerdao.useracclistSel(selectedVal, selectedoption);
        session.setAttribute("userlistaccprgr", userlistaccprgr);
        session.setAttribute("selectedoption", selectedoption);
        session.setAttribute("selectedVal", selectedVal);
        ////.println("prgr len::::::" + userlistaccprgr.getRowCount());
        return null;
    }

    public ActionForward useTypelistSel(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        String selecTypeLValist = request.getParameter("selval12");
        ////.println("");
        String selectedoptyList = request.getParameter("seloption12");
        UserLayerDAO userLayerdao = new UserLayerDAO();
        PbReturnObject userTypelistprgr = userLayerdao.useTypelistSel(selecTypeLValist, selectedoptyList);
        session.setAttribute("userTypelistprgr", userTypelistprgr);
        session.setAttribute("selectedoptyList", selectedoptyList);
        session.setAttribute("selecTypeLValist", selecTypeLValist);
        ////.println("prgr len::::::" + userTypelistprgr.getRowCount());
        return null;
    }
    //useTypelistSel
}
