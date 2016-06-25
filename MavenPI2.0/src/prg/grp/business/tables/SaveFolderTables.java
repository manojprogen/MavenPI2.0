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
public class SaveFolderTables extends org.apache.struts.action.Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    public static Logger logger = Logger.getLogger(SaveFolderTables.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int table_id = Integer.parseInt(request.getParameter("tableid"));
            String desc = request.getParameter("tabledesc");
            String tabDispName = request.getParameter("tabDispName");
            String tabTooltip = request.getParameter("tabTooltip");
            Connection con = ProgenConnection.getInstance().getConnection();
            Statement st = con.createStatement();
            String query = "update PRG_USER_SUB_FOLDER_TABLES set disp_name='" + desc + "',table_disp_name='" + tabDispName + "',table_tooltip_name='" + tabTooltip + "' where sub_folder_tab_id=" + table_id;
            st.executeUpdate(query);

            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return mapping.findForward(SUCCESS);
    }
}
