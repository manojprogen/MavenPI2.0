package prg.grp.business.tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utils.db.ProgenConnection;

public class GetFolderTables extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(GetFolderTables.class);
    Connection con;
    Statement st, st1, st2;
    ResultSet rs, rs1, rs2;
    String table_name;
    String table_description;
    String table_disp_name;
    String table_tooltip_name;
    ArrayList columns;
    HashMap sourceMap = new HashMap();
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //Get table_id from Right Click Menu Of List Of Tables
            //int table_id = 139;
            ArrayList sources = new ArrayList();
            int table_id = Integer.parseInt(request.getParameter("foldertabId"));
            // Class.forName("oracle.jdbc.driver.OracleDriver");
            con = ProgenConnection.getInstance().getConnection();

            //Get The master table
            //String Q1 = "SELECT DISTINCT disp_name FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_tab_id=" + table_id + " order by disp_name";
            String Q1 = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                Q1 = "SELECT DISTINCT disp_name,isnull(table_disp_name,disp_name) table_disp_name,isnull(table_tooltip_name,disp_name) table_tooltip_name FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_tab_id=" + table_id + " order by disp_name";

            } else {
                Q1 = "SELECT DISTINCT disp_name,nvl(table_disp_name,disp_name) table_disp_name,nvl(table_tooltip_name,disp_name) table_tooltip_name FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_tab_id=" + table_id + " order by disp_name";

            }
            ////.println("Q1 is-->"+Q1);
            st = con.createStatement();
            rs = st.executeQuery(Q1);
            rs.next();
            table_name = rs.getString(1);
            table_description = rs.getString(1);
            table_disp_name = rs.getString(2);
            table_tooltip_name = rs.getString(3);

            request.setAttribute("table_name", table_name);
            request.setAttribute("table_id", Integer.valueOf(table_id));
            request.setAttribute("table_description", table_description);
            request.setAttribute("table_disp_name", table_disp_name);
            request.setAttribute("table_tooltip_name", table_tooltip_name);
            request.getSession().setAttribute("sources", sources);
            String Q3 = "SELECT ELEMENT_ID, BUSS_COL_NAME, USER_COL_NAME, USER_COL_TYPE,USER_COL_DESC,ref_element_type FROM PRG_USER_SUB_FOLDER_ELEMENTS where sub_folder_tab_id=" + table_id + "  and USER_COL_TYPE not in('TIMECALCULATED','TIMECALUCULATED','TIMESUMMARISED','TIMESUMMARIZED')order by BUSS_COL_NAME, ref_element_type";

            st2 = con.createStatement();
            rs2 = st2.executeQuery(Q3);
            request.setAttribute("columndetails", rs2);
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return mapping.findForward(SUCCESS);
    }
}
