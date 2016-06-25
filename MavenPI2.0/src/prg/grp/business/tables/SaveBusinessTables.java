package prg.grp.business.tables;

import java.sql.Connection;
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
public class SaveBusinessTables extends org.apache.struts.action.Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    public static Logger logger = Logger.getLogger(SaveBusinessTables.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            int table_id = Integer.parseInt(request.getParameter("tableid"));
            String desc = request.getParameter("tabledesc");
            String tableDispName = request.getParameter("tableDispName");
            String tableTooltipName = request.getParameter("tableTooltipName");

            Connection con = ProgenConnection.getInstance().getConnection();
            Statement st = con.createStatement();
            String query = "update prg_grp_buss_table set buss_desc='" + desc + "',table_disp_name='" + tableDispName + "',table_tooltip_name='" + tableTooltipName + "' where buss_table_id=" + table_id + "";
            st.executeUpdate(query);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        //return mapping.findForward(SUCCESS);
        return null;
    }
}
