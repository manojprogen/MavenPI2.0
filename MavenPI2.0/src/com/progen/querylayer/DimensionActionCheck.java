/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.querylayer;

import com.progen.dimensions.DimensionEditDAO;
import com.progen.dimensions.DimensionsDAO;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import com.progen.userlayer.db.UserLayerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class DimensionActionCheck extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(DimensionActionCheck.class);
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
        map.put("button.delete", "delete");//
        map.put("checkDimensionForDelete", "checkDimensionForDelete");
        map.put("deleteDimension", "deleteDimension");
        map.put("checkDimensionForRename", "checkDimensionForRename");
        map.put("renameDimension", "renameDimension");
        map.put("checkReNamememExist", "checkReNamememExist");
        map.put("checkExistmemName", "checkExistmemName");
        map.put("saveMemRename", "saveMemRename");

        map.put("checkQueryDimTableDelete", "checkQueryDimTableDelete");
        map.put("editHierarachy", "editHierarachy");
        map.put("deleteHierarachyLevel", "deleteHierarachyLevel");
        map.put("saveHierarachyLevel", "saveHierarachyLevel");
        map.put("getTimeDimensionMembers", "getTimeDimensionMembers");
        map.put("saveTimeDimensionMembers", "saveTimeDimensionMembers");
        map.put("getDimensionMembers", "getDimensionMembers");
        map.put("saveAccessEnabledDimMembers", "saveAccessEnabledDimMembers");
        map.put("saveDimensionMembers", "saveDimensionMembers");
        map.put("getDimensionGrp", "getDimensionGrp");
        map.put("migrateDimensionGrp", "migrateDimensionGrp");
        return map;
    }

    public ActionForward checkDimensionForDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dimensionId = request.getParameter("dimensionId");
        String connectionId = request.getParameter("connectionId");
        //////////////////////////////////////////////////////////////////////////////////////.println.println(connectionId+" dimensionId in checekdel  "+dimensionId);

        PrintWriter out = response.getWriter();
        DimensionEditDAO dimDao = new DimensionEditDAO();
        String status = dimDao.checkQueryDimension(dimensionId);
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" status "+status);
        if (status.equalsIgnoreCase("Deleteable")) {
            //////////////////////////////////////////////////////////////////////////////////////.println.println(" in if ");
            out.println("1");
            dimDao.deleteQueryDimension(dimensionId);
        } else {
            //////////////////////////////////////////////////////////////////////////////////////.println.println(" in else ");
            out.println("2");
        }
        // out.print(result);
        return null;
    }

    public ActionForward checkDimensionForRename(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dimensionId = request.getParameter("dimensionId");
        String connectionId = request.getParameter("connectionId");
        String newDimName = request.getParameter("newDimName");

        //////////////////////////////////////////////////////////////////////////////////////.println.println(connectionId+" dimensionId in checek rename  "+dimensionId);

        PrintWriter out = response.getWriter();
        DimensionEditDAO dimDao = new DimensionEditDAO();
        HashMap details = dimDao.checkQueryDimensionForRename(dimensionId, connectionId, newDimName);
        String status = (String) details.get("status");
        String alreadyExists = (String) details.get("alreadyExists");
        //////////////////////////////////////////////////////////////////////////////////////.println.println(alreadyExists+" alreadyExists status "+status);
        if (status.equalsIgnoreCase("Yes") && alreadyExists.equalsIgnoreCase("false")) {
            //////////////////////////////////////////////////////////////////////////////////////.println.println(" in if ");
            out.println("1");
        } else if (status.equalsIgnoreCase("No") && alreadyExists.equalsIgnoreCase("false")) {
            //////////////////////////////////////////////////////////////////////////////////////.println.println(" in else ");
            out.println("2");
        } else if (alreadyExists.equalsIgnoreCase("true") && status.equalsIgnoreCase("No")) {
            out.println("3");
        } else if (alreadyExists.equalsIgnoreCase("false")) {
            out.println("2");
        } else if (status.equalsIgnoreCase("Yes") && alreadyExists.equalsIgnoreCase("true")) {
            out.println("3");
        }
        // out.print(result);

        return null;
    }

    public ActionForward renameDimension(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dimensionId = request.getParameter("dimensionId");
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" in rename");
        String connectionId = request.getParameter("connectionId");
        String newDimName = request.getParameter("newDimName");
        String dimdesc = request.getParameter("dimdesc");
        //////////////////////////////////////////////////////////////////////////////////////.println.println(connectionId+" dimensionId in check rename "+dimensionId+" newDimName "+newDimName);

        PrintWriter out = response.getWriter();
        DimensionEditDAO dimDao = new DimensionEditDAO();
        boolean st = dimDao.renameDimension(dimensionId, newDimName, dimdesc);
        String status = "";
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" status -. "+status);
        //////////////////////////////////////////////////////////////////////////////////////.println.println(" in if ");
        out.println("1");


        // out.print(result);

        return null;
    }

    public ActionForward checkReNamememExist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbDb pbdb = new PbDb();
        String memId = request.getParameter("memId");
        String memName = request.getParameter("memName");
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("memName--" + memName);
        // //////////////////////////////////////////////////////////////////////////////////////.println.println("memId---" + memId);
        String checkQuery = "select MEMBER_NAME from PRG_GRP_DIM_MEMBER where DIM_ID=";
        checkQuery += "(select DIM_ID from PRG_QRY_DIM_MEMBER where MEMBER_ID=" + memId + ")";
        PbReturnObject pbro = pbdb.execSelectSQL(checkQuery);
        String status = "";
        for (int i = 0; i < pbro.getRowCount(); i++) {
            if (memName.equalsIgnoreCase(pbro.getFieldValueString(i, 0))) {
                status = "Exist";
                break;
            }
        }
        response.getWriter().print(status);
        return null;
    }

    public ActionForward checkExistmemName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbDb pbdb = new PbDb();
        String connectionId = request.getParameter("connectionId");
        String memName = request.getParameter("newMemName");
        String oldmemName = request.getParameter("oldmemName");
        String checkQuery = "SELECT MEMBER_NAME FROM PRG_QRY_DIM_MEMBER where dim_id in (SELECT DIMENSION_ID FROM PRG_QRY_DIMENSIONS where connection_id=" + connectionId + ") and MEMBER_NAME!='" + oldmemName + "'";
        // //////////////////////////////////////////////////////////////////////////////////////.println.println("checkQuery"+checkQuery);
        PbReturnObject pbro = pbdb.execSelectSQL(checkQuery);
        String status = "";
        for (int i = 0; i < pbro.getRowCount(); i++) {
            if (memName.equalsIgnoreCase(pbro.getFieldValueString(i, 0))) {
                status = "Exist";
                break;
            }
        }
        response.getWriter().print(status);
        return null;
    }

    public ActionForward saveMemRename(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbDb pbdb = new PbDb();
        String connectionId = request.getParameter("connectionId");
        String memName = request.getParameter("newMemName");
        String memDesc = request.getParameter("memDesc");
        String memId = request.getParameter("memId");
        try {
            String checkQuery = "UPDATE PRG_QRY_DIM_MEMBER set MEMBER_NAME='" + memName + "' ,MEMBER_DESC='" + memDesc + "' where member_id=" + memId;
            //////////////////////////////////////////////////////////////////////////////////////.println.println("checkQuery--"+checkQuery);
            int c = pbdb.execUpdateSQL(checkQuery);
            response.getWriter().print(c);
        } catch (Exception e) {
            logger.error("Exception:", e);
            response.getWriter().print("0");
        }

        return null;
    }

    public ActionForward checkQueryDimTableDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String connectionId = request.getParameter("connectionId");
        String dimId = request.getParameter("dimId");
        //////////////////////////////////////////////////////////////////////////////////.println.println(" dimId "+dimId+" connectionId "+connectionId);
        try {
            DimensionEditDAO dimDao = new DimensionEditDAO();
            PrintWriter out = response.getWriter();
            String status = dimDao.checkQueryDimTableDelete(connectionId, dimId);
            if (status.equalsIgnoreCase("true")) {
                dimDao.deleteDimTable(connectionId, dimId);
                //////////////////////////////////////////////////////////////////////////////////.println.println(" in delete");
                out.println("1");

            } else if (status.equalsIgnoreCase("false")) {
                // ////////////////////////////////////////////////////////////////////////////////.println.println(" the dim cant be deleted");
                out.println("2");
            }
        } catch (Exception e) {
            logger.error("Exception:", e);

        }
        return null;
    }

    public ActionForward editHierarachy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String dimid = request.getParameter("dimid");
        String relID = request.getParameter("relID");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            DimensionEditDAO dimensionEditDAO = new DimensionEditDAO();
            String resultStr = dimensionEditDAO.editHierarachy(dimid, relID);
            out.print(resultStr);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return null;
    }

    public ActionForward deleteHierarachyLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String memId = request.getParameter("memId");
        String relId = request.getParameter("relId");
        String dimID = request.getParameter("dimID");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            DimensionEditDAO dimensionEditDAO = new DimensionEditDAO();
            String resultStr = dimensionEditDAO.deleteHierarachyLevel(memId, relId, dimID);
//      String resultStr = dimensionEditDAO.editHierarachy(dimid);
            out.print(resultStr);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return null;
    }

    public ActionForward saveHierarachyLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String memId = request.getParameter("memId");
        String relId = request.getParameter("relId");
        String dimID = request.getParameter("dimID");
        PrintWriter out = null;
        String[] memIdArray = memId.split(",");
        try {
            out = response.getWriter();
            DimensionEditDAO dimensionEditDAO = new DimensionEditDAO();
            boolean resultStr = dimensionEditDAO.saveHierarachyLevel(memIdArray, relId, dimID);

            out.print(resultStr);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return null;
    }

    public ActionForward getTimeDimensionMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String contextPath = request.getContextPath();
        ArrayList<String> timeMembers = new ArrayList<String>();
        String fromUserDefinedTimeDim = request.getParameter("fromUserDefinedTimeDim");
        String fromAddMember = request.getParameter("fromAddMember");
        ArrayList<String> dragableListNames = new ArrayList<String>();
        GenerateDragAndDrophtml html = null;
        DimensionsDAO dimDao = new DimensionsDAO();
        String connId = request.getParameter("connId");
        PrintWriter out = null;
        boolean isUserDefTimeDimEnabled = dimDao.isUserDefinedTimeDimensionEnabled(connId);
        try {
            out = response.getWriter();
            if (fromUserDefinedTimeDim.equals("false")) {
                timeMembers.add("Year");
                timeMembers.add("Quarter");
                timeMembers.add("Month");
                timeMembers.add("week");
                timeMembers.add("Day");
                html = new GenerateDragAndDrophtml("Drag Members From Here", "Drop Menbers Here", new ArrayList(), timeMembers, contextPath);
                String divhtml = html.getDragAndDropDiv();
                out.println(divhtml.toString());
            } else if (!isUserDefTimeDimEnabled || fromAddMember.equals("true")) {


                timeMembers.add("DDATE");
                timeMembers.add("DAY_OF_WEEK");
                timeMembers.add("WEEK_DAY_NAME");
                timeMembers.add("MONTH_DAY");
                timeMembers.add("DAYSOFYEAR");
                timeMembers.add("WEEK_OF_YEAR");
                timeMembers.add("WEEK_OF_MONTH");
                timeMembers.add("MONTH_OF_YEAR");
                timeMembers.add("QTR_OF_YEAR");
                timeMembers.add("YEAR_NAME");

                dragableListNames.add("Day");
                dragableListNames.add("Day of week");
                dragableListNames.add("Week day name");
                dragableListNames.add("Day of month");
                dragableListNames.add("Days of year");
                dragableListNames.add("Week of year");
                dragableListNames.add("Week of month");
                dragableListNames.add("Month of Year");
                dragableListNames.add("Qtr of year");
                dragableListNames.add("Year name");
                html = new GenerateDragAndDrophtml("Drag Members From Here", "Drop Menbers Here", new ArrayList(), timeMembers, contextPath);
                html.setDragableListNames(dragableListNames);
                String divhtml = html.getDragAndDropDiv();


                out.print(divhtml.toString());

            } else {
                out.print("Dimension Enabled");
            }

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward saveTimeDimensionMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String memberIdsString = request.getParameter("memberIds");
        String dimension_Id = request.getParameter("dimension_Id");
        String fromUserDefTimeDim = request.getParameter("fromUserDefTimeDim");
        String connId = request.getParameter("connId");
        DimensionsDAO dimDAO = new DimensionsDAO();

        if (fromUserDefTimeDim.equals("true")) {
            dimension_Id = dimDAO.setUserTimeDimension(connId);
        }


        String memberIds[] = memberIdsString.split(",");
        try {
            dimDAO.saveTimeDimensionMembers(memberIds, dimension_Id);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return mapping.findForward(SUCCESS);
    }
    //by sunita

    public ActionForward saveDimensionMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String dimId = "";

        String dimtabId = "";
        String colName = "";
        String is_pk = "";
        String tableId = "";
        String tabName = "";
        String sql = "";
        String listarr[] = request.getParameter("colId").split(",");
        String flag = request.getParameter("flag");
        dimtabId = listarr[0];
        tableId = listarr[1];
        dimId = listarr[2];
        tabName = listarr[3];

        Connection con = null;

        Statement st2 = null;
        Statement st3 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String Q1, Q2, Q3;
        int f = 0;
        try {
            con = ProgenConnection.getInstance().getConnection();

            Q2 = "select column_id from prg_db_master_table_details  where table_id in (" + tableId + ")and column_id not in( select col_id from prg_qry_dim_tab_details where dim_tab_id=" + dimtabId + ")and is_active='Y'";
//
            st3 = con.createStatement();
            rs = st3.executeQuery(Q2);
            while (rs.next()) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    Q3 = "insert into prg_qry_dim_tab_details (dim_tab_id,col_id,is_available,is_pk_key) values (" + dimtabId + "," + Integer.parseInt(rs.getString(1)) + ",'Y','N')";

                } else {
                    Q3 = "insert into prg_qry_dim_tab_details (dim_tab_col_id,dim_tab_id,col_id,is_available,is_pk_key) values (prg_qry_dim_tab_details_seq.nextval," + dimtabId + "," + Integer.parseInt(rs.getString(1)) + ",'Y','N')";

                }
                st2 = con.createStatement();
                st2.executeUpdate(Q3);
            }
            if (st2 != null) {//by Prabal
                st2.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return null;

    }
    //by sunita  rai

    public ActionForward getDimensionGrp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dimId = "";

        String dimtabId = "";
        String colName = "";
        String is_pk = "";
        String tableId = "";
        String tabName = "";
        StringBuilder str = new StringBuilder(800);
        String listarr[] = request.getParameter("colId").split(",");

        dimtabId = listarr[0];
        tableId = listarr[1];
        dimId = listarr[2];
        tabName = listarr[3];

        Connection con = null;
        Statement st3 = null;
        ResultSet rs = null;
        String Q2;

        try {
            con = ProgenConnection.getInstance().getConnection();

            Q2 = "select grp_id,GRP_NAME from PRG_GRP_MASTER  where grp_id in (select GRP_ID from PRG_GRP_DIMENSIONS where QRY_DIM_ID=" + dimId + ")";
            //
            st3 = con.createStatement();
            rs = st3.executeQuery(Q2);
            while (rs.next()) {
                //changed by Nazneen
//                     str+="<tr><td  align='left'><input type='checkbox' name='sum' id='"+rs.getString(1)+"' value='"+rs.getString(1)+"' onclick=checktab("+rs.getString(1)+")>"+rs.getString(2)+"</td></tr>\n";
                str.append("<tr><td  align='left'><input type='checkbox' name='sum' id='temp" + rs.getString(1) + "' value='" + rs.getString(1) + "' onclick=checktab(" + rs.getString(1) + ")>" + rs.getString(2) + "</td></tr>\n");
            }
            if (st3 != null) {// By Prabal
                st3.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        PrintWriter out = response.getWriter();
        out.print(str.toString());
        return null;

    }

    public ActionForward migrateDimensionGrp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dimId = "";

        String dimtabId = "";
        String colName = "";
        String is_pk = "";
        String tableId = "";
        String tabName = "";
        String str = "<tr>";
        String listarr[] = request.getParameter("colId").split(",");
        String grparr[] = request.getParameter("grpnames").split(",");
        dimtabId = listarr[0];
        tableId = listarr[1];
        dimId = listarr[2];
        tabName = listarr[3];
        DimensionsDAO dimDAO = new DimensionsDAO();
        UserLayerDAO userLayerDAO = new UserLayerDAO();

        for (int i = 0; i < grparr.length; i++) {
            dimDAO.migrateDimensionGrp(tableId, dimtabId, dimId, grparr[i]);
            userLayerDAO.truncateDimValues();
            userLayerDAO.insertNewDimValues();
            dimDAO.migrateDimensionToRole(tableId, dimtabId, dimId, grparr[i]);
        }
        //userLayerDAO.truncateDimValues();
        //userLayerDAO.insertNewDimValues();

        return null;

    }
    //end by sunita rai

    public ActionForward getDimensionMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String contextPath = request.getContextPath();
        String dimensionName = request.getParameter("dimName");
        String tableName = request.getParameter("tableName");
        String connId = request.getParameter("connId");
        int dim_tab_id = Integer.parseInt(request.getParameter("dim_tab_id"));
        int col_id = Integer.parseInt(request.getParameter("col_id"));
        DimensionsDAO dimDAO = new DimensionsDAO();
        PrintWriter out = null;
        String html = dimDAO.getDimensionMembersDropDown(connId, dimensionName, tableName, contextPath, dim_tab_id, col_id);
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.print(html.toString());
        return null;
    }

    public ActionForward saveAccessEnabledDimMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        int dim_tab_id = Integer.parseInt(request.getParameter("dim_tab_id"));
        int col_id = Integer.parseInt(request.getParameter("col_id"));
        String dimMemString = request.getParameter("dimMembers");
        DimensionsDAO dimDAO = new DimensionsDAO();
        dimDAO.saveAccessEnabledDimMembers(dimMemString, dim_tab_id, col_id);
        return null;
    }
}
