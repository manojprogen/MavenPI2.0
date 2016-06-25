package prg.grp.business.tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utils.db.ProgenConnection;

public class UpdateTableColumnsAction extends org.apache.struts.action.Action {

    Connection con;
    //st3,st4 added by susheela
    Statement st, st1, st2, st3, st4;
    ResultSet rs, rs1, rs2;
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int table_id = Integer.parseInt(request.getParameter("tableid"));
        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = ProgenConnection.getInstance().getConnection();
        st = con.createStatement();
        String Q1 = "select buss_column_id from prg_grp_buss_table_details where buss_table_id=" + table_id;
        rs = st.executeQuery(Q1);
        st1 = con.createStatement();
        //st3 added by susheela
        st3 = con.createStatement();
        st4 = con.createStatement();

        while (rs.next()) {
            String column_id = rs.getString(1);
            String aggr = request.getParameter(column_id);


            //addded by susheela start
            String newDesc = request.getParameter(column_id + ":desc");
            String updateDesc = "update prg_grp_buss_table_details set column_display_desc='" + newDesc + "' where buss_column_id=" + column_id;
            st3.addBatch(updateDesc);

            String newRole = request.getParameter(column_id + ":roleFl");
            String roleVal = "";
            if (newRole != null && newRole.equalsIgnoreCase("on")) {
                roleVal = "Y";
            } else {
                roleVal = "N";
            }
            String updateRole = "update prg_grp_buss_table_details set role_flag='" + roleVal + "' where buss_column_id=" + column_id;
            st4.addBatch(updateRole);
            //added by susheela over
            //changed by ramesh
            String Q2 = "update prg_grp_buss_table_details set default_aggregation='" + aggr + "' where buss_column_id=" + column_id;
            st1.addBatch(Q2);
        }
        int[] i = st1.executeBatch();
        //added by susheela start
        int[] j = st3.executeBatch();
        int[] k = st4.executeBatch();
        //added by susheela over
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("NUMBER OF COLUMNS ADDED ARE--->"+i);
        //return mapping.findForward(SUCCESS);
        if (con != null) {
            con.close();
        }
        return null;
    }
}
