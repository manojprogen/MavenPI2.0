package prg.business.group;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class AddFilter extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(AddFilter.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    Connection con;
    Statement st, st1, st2;
    ResultSet rs, rs1, rs2;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            int tableId = Integer.parseInt(request.getParameter("tableId"));
            String filter = request.getParameter("txt2");
            con = ProgenConnection.getInstance().getConnection();
            st = con.createStatement();
            String Query = "insert into PRG_BUSS_FILTER_SIM (filter_id,buss_table_id,filter,created_by,created_on,updated_by,updated_on) values (PRG_BUSS_FILTER_SIM_SEQ.nextval," + tableId + ",'" + filter + "','','','','')";
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Inser Que "+Query);
            st.executeUpdate(Query);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        if (con != null) {
            con.close();
        }
        if (st != null) {
            st.close();
        }
        return mapping.findForward(SUCCESS);
    }
}
