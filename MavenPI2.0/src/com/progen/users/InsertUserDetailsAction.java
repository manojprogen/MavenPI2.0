/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

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
 * @author Administrator
 */
public class InsertUserDetailsAction extends LookupDispatchAction {

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
        map.put("insertUserRecord", "insertUserRecord");
        //map.put("button.add", "add");
        //map.put("button.edit", "edit");
        //map.put("button.delete", "delete");
        return map;
    }

    public ActionForward insertUserRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        // TODO: implement add method
        //////////////////////////////////////////////////////////////////////////.println("--insert user--");
        String puLoginId = request.getParameter("loginId");
        String puFirstName = request.getParameter("firstName");
        String puMiddleName = request.getParameter("middleName");
        String puLastName = request.getParameter("lastName");
        String puPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword ");
        String puEmail = request.getParameter("email");
        String puContactNo = request.getParameter("contactNo");
        String puAddress = request.getParameter("address");
        String puCity = request.getParameter("city");
        String puState = request.getParameter("state");
        String puCountry = request.getParameter("country");
        String puPin = request.getParameter("pin");
        // String START_DATE = request.getParameter("START_DATE");
        //String END_DATE = request.getParameter("END_DATE");
        UserLayerDAO userLayerDAO = new UserLayerDAO();

        String[] userList = new String[15];
        userList[0] = puLoginId;
        userList[1] = puFirstName;
        userList[2] = puMiddleName;
        userList[3] = puLastName;
        userList[4] = puPassword;
        userList[5] = confirmPassword;
        userList[6] = puEmail;
        userList[7] = puContactNo;
        userList[8] = puAddress;
        userList[9] = puCity;
        userList[10] = puState;
        userList[11] = puCountry;
        userList[12] = puPin;
        //    userList[13] = START_DATE;
        //   userList[14] = END_DATE;



        userLayerDAO.addUserDetails(userList);

        return mapping.findForward(SUCCESS);
    }
    /*
     * public ActionForward add(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * java.lang.Exception { // TODO: implement add method return
     * mapping.findForward(SUCCESS); }
     *
     * /
     * public ActionForward edit(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) { // TODO:
     * implement edit method return mapping.findForward(SUCCESS); }
     *
     * /
     * public ActionForward delete(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * java.lang.Exception { // TODO:implement delete method return
     * mapping.findForward(SUCCESS); }
     */
    /*
     * And your JSP would have the following format for submit buttons:
     *
     * <html:form action="/test"> <html:submit property="method"> <bean:message
     * key="button.add"/> </html:submit> <html:submit property="method">
     * <bean:message key="button.edit"/> </html:submit> <html:submit
     * property="method"> <bean:message key="button.delete"/> </html:submit>
     * </html:form>
     */
}