/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.querydesigner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utils.db.ProgenConnection;

public class SaveDimensionsAction extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveDimensionsAction.class);
    Connection con = null;
    Statement st = null;
    Statement st1 = null;
    ResultSet rs = null;
    String dimensionName = null;
    String dimensionDescription = null;
    String connId = null;

    public SaveDimensionsAction() {
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("In SaveDim Action Cons");
    }
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {

            con = ProgenConnection.getInstance().getConnection();
            //String connectionId=request.getParameter("connectionId");
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("connectionId---"+connectionId);
            SaveDimensionForm dForm = (SaveDimensionForm) form;
            dimensionName = dForm.getDimensionName();
            dimensionDescription = dForm.getDimensionDescription();
            connId = dForm.getConnId();
            st1 = con.createStatement();
            rs = st1.executeQuery("select dimension_name from prg_qry_dimensions where connection_id=" + connId);
            ArrayList a = new ArrayList();
            while (rs.next()) {
                a.add(rs.getString(1));
            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connection==" + con);
            st = con.createStatement();

            if (!a.contains(dimensionName)) {
                String query = "";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    query = "insert into PRG_QRY_DIMENSIONS (DIMENSION_NAME,DIMENSION_DESC,PRG_DIM_ACTIVE,DEFAULT_HIERARCHY_ID,connection_id) values('" + dimensionName + "','" + dimensionDescription + "','Y',null," + connId + ")";

                } else {
                    query = "insert into PRG_QRY_DIMENSIONS (DIMENSION_ID,DIMENSION_NAME,DIMENSION_DESC,PRG_DIM_ACTIVE,DEFAULT_HIERARCHY_ID,connection_id) values(PRG_QRY_DIMENSIONS_SEQ.nextval,'" + dimensionName + "','" + dimensionDescription + "','Y',''," + connId + ")";

                }
                // String query = "insert into PRG_QRY_DIMENSIONS (DIMENSION_ID,DIMENSION_NAME,DIMENSION_DESC,PRG_DIM_ACTIVE,DEFAULT_HIERARCHY_ID,connection_id) values(PRG_QRY_DIMENSIONS_SEQ.nextval,'" + dimensionName + "','" + dimensionDescription + "','Y','',"+connId+")";
                ////.println("Query is " + query);
                st.executeUpdate(query);
                request.setAttribute("DimensionInsertStatus", "Dimension Created Successfully");
            } else {
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("In Else Part");
                request.setAttribute("DimensionInsertStatus", "This Dimension Already Exists...");
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            st.close();
            con.close();
        }
        return mapping.findForward(SUCCESS);
    }
}
