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

public class UpdateTableColumns extends org.apache.struts.action.Action {

    Connection con;
    Statement st, st1, st2;
    ResultSet rs, rs1, rs2;
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int table_id = Integer.parseInt(request.getParameter("tableid"));
        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = ProgenConnection.getInstance().getConnection();
        st = con.createStatement();
        String Q1 = "select element_id from PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID=" + table_id;
        rs = st.executeQuery(Q1);
        st1 = con.createStatement();
        st2 = con.createStatement();
        while (rs.next()) {
            String column_id = rs.getString(1);
            String aggr = request.getParameter(column_id);
            //modified by susheela start
            String Q2 = "update PRG_USER_SUB_FOLDER_ELEMENTS set user_col_desc='" + aggr + "' where ELEMENT_ID=" + column_id;
            //modified by susheela over
            String Q3 = "update PRG_USER_all_info_details set user_col_desc='" + aggr + "' where ELEMENT_ID=" + column_id;
            st1.addBatch(Q2);
            st2.addBatch(Q3);
        }
//        int[] i=st1.executeBatch();
//        int[] j =st2.executeBatch();
        st1.executeBatch();
        st2.executeBatch();
        //return mapping.findForward(SUCCESS);
        return null;
    }
}
