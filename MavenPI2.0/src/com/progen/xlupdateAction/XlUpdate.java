/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.xlupdateAction;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class XlUpdate extends LookupDispatchAction {

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
        map.put("button.delete", "delete");
        map.put("update", "update");
        map.put("DownloadExl", "DownloadExl");
        map.put("getColumns", "getColumns");
        map.put("getColumnsvalues", "getColumnsvalues");
        map.put("upDateColumns", "upDateColumns");
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

    /**
     * Action called on Edit button click
     */
    public ActionForward edit(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        // TODO: implement edit method
        return mapping.findForward(SUCCESS);
    }

    /**
     * Action called on Delete button click
     */
    public ActionForward delete(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        // TODO:implement delete method
        return mapping.findForward(SUCCESS);
    }

    public ActionForward update(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {

        String filepath = request.getParameter("filepath");

        String filepath1 = "F:/Sreekanth/11-jan-10/KNP Prisma Pro History File.xls";
//                  filepath=filepath1.concat(filepath);
//                    ////////////////////////////////////////////////////.println.println("file fath-----------------------------------------"+filepath);
        XlupdateDAO xlupdate = new XlupdateDAO();
        xlupdate.init(filepath1);

        return null;
    }

    public ActionForward DownloadExl(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {

        String tablename = request.getParameter("TABLE_NAME");
        String coll_names = request.getParameter("coll_names");

        String collist = coll_names.toString();
        String[] userList1 = collist.split(",");
        String[] userList = new String[12];
        XlupdateDAO xlupdao = new XlupdateDAO();
        userList[0] = tablename;
        userList[1] = userList1[0];
        userList[2] = userList1[1];
        userList[3] = userList1[2];
        xlupdao.Downloadexl(userList);

        // TODO:implement delete method
        return mapping.findForward(SUCCESS);
    }

    public ActionForward getColumns(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {

        String tablename = request.getParameter("TABLE_NAME");

        XlupdateDAO xlupdao = new XlupdateDAO();

        //     xlupdao.getColumns(tablename);


        // TODO:implement delete method
        return null;
    }

    public ActionForward getColumnsvalues(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        PrintWriter Out = response.getWriter();
        PbReturnObject prgr = new PbReturnObject();
        String selectval = request.getParameter("selectedval");

        XlupdateDAO getColval = new XlupdateDAO();

        prgr = getColval.getColumnsvalues(selectval);
        Out.print(prgr);

        return null;
    }
    //upDateColumns

    public ActionForward upDateColumns(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        PrintWriter Out = response.getWriter();
        PbReturnObject prgr = new PbReturnObject();
        String oldValues = request.getParameter("oldvlaArr");
        String newValues = request.getParameter("newValArr");
        String tableName = request.getParameter("tableName");
        String colName = request.getParameter("colname");
        XlupdateDAO getColval = new XlupdateDAO();
        String[] oldvalues = oldValues.split(",");
        String[] newvalues = newValues.split(",");

        int count = 2 * oldvalues.length + 2;
        ////////////////////////////////////////////////////.println.println("count================= "+count);
        String[] userlist1 = new String[oldvalues.length];
        String[] userlist2 = new String[oldvalues.length];
        for (int i = 0; i < userlist1.length; i++) {

            userlist1[i] = oldvalues[i];

        }

        for (int j = 0; j < userlist2.length; j++) {

            userlist2[j] = newvalues[j];
        }
        XlupdateDAO updCol = new XlupdateDAO();
        String result = updCol.upDateColumns(tableName, colName, userlist1, userlist2);
        Connection con = ProgenConnection.getInstance().getConnection();
        String sql = "select column_name from user_tab_cols where table_name=upper('PRG_KNP_SHADES_TRANS')";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        PbReturnObject pbro1 = new PbReturnObject(rs);
        ArrayList a = new ArrayList();
        for (int i = 0; i < pbro1.getRowCount(); i++) {
            a.add(pbro1.getFieldValueString(i, 0));
        }
        String result1 = "";
        if (a.contains(colName)) {
            tableName = "PRG_KNP_SHADES_TRANS";
            result1 = updCol.upDateColumns(tableName, colName, userlist1, userlist2);
        }
        // prgr=getColval.upDateColumns(selectval);
        if (result.equalsIgnoreCase("") && result1.equalsIgnoreCase("")) {
            result = "";
        } else {
            result = "Error";
        }
        Out.print(result);
        if (con != null) {
            con.close();
        }
        return null;

    }
}
