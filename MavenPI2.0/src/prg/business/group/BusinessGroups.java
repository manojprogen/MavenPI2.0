/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class BusinessGroups extends DispatchAction {

    public static Logger logger = Logger.getLogger(BusinessGroups.class);
    private final static String SUCCESS = "success";

    public ActionForward deletebusinessrole(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            PrintWriter out = response.getWriter();
//        HttpSession session = request.getSession();
            String userid = request.getParameter("userid");
            String deletingid = request.getParameter("deletingid");
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("userid is -------" + userid);
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("deleting id is===-"+deletingid);

            BusinessGroupDAO bgrpdao = new BusinessGroupDAO();

            String status = bgrpdao.getuserFolders(userid, deletingid);

            if (status.equalsIgnoreCase("Failure")) {
                ////////////////////////////////////////////////////////////////////////////////////////.println.println("Ther are no folders for this user id");
                out.println("1");
            } else if (status.equalsIgnoreCase("Success")) {
                ////////////////////////////////////////////////////////////////////////////////////////.println.println("userfolderids size is" + status);
                out.println("2");
            }

//        return mapping.findForward(SUCCESS);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return null;
    }

    public ActionForward Deleteallreports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String busroleid = request.getParameter("busid");
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("going to delete busrole id is"+busroleid);
            BusinessGroupDAO bgrpdao = new BusinessGroupDAO();
            String status = bgrpdao.DeletBusinessRoles(busroleid);
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("status is"+status);
            PrintWriter out = response.getWriter();
            out.println("1");
            return null;
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return null;
    }
}
