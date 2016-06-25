/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.grp.business.tables;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;

/**
 *
 * @author Saurabh
 */
public class DeleteUserFolderDets extends org.apache.struts.action.Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String elementId = request.getParameter("elementId");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("ele----------"+elementId);
        String deleteQuery = "delete from PRG_USER_SUB_FOLDER_ELEMENTS where ELEMENT_ID=" + elementId;
        PbDb pbdb = new PbDb();
        pbdb.execUpdateSQL(deleteQuery);
        return mapping.findForward(SUCCESS);
    }
}
