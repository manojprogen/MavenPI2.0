/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.querylayer;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbReturnObject;

public class Dbcontablepropertiesaction extends Action {

    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DbcontablepropertiesDAO tablepropdao = new DbcontablepropertiesDAO();

        String method = request.getParameter("method");
        ////////////////////////////////////////////////////////////////////////////////////.println.println("Method name in dbcontableproperties"+method);
        if (method.equalsIgnoreCase("tableproperties")) {
            String tableid = request.getParameter("tableid");
            ////////////////////////////////////////////////////////////////////////////////////.println.println("table id is"+tableid);

            String tablename = tablepropdao.gettablename(tableid);
            request.setAttribute("tablename", tablename);
            ArrayList tabledata = tablepropdao.gettabledata(tableid);
            ////////////////////////////////////////////////////////////////////////////////////.println.println("table data is"+tabledata);
            request.setAttribute("tabledetails", tabledata);

            PbReturnObject pbretobj = tablepropdao.gettabledescription(tableid);

            request.setAttribute("tabledescription", pbretobj);

            return mapping.findForward("showtabdetails");
        }


        return mapping.findForward(SUCCESS);
    }
}
