/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.databaseralation;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Administrator
 */
public class SaveRelationAction extends org.apache.struts.action.Action {

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
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in ajax called class");
        PrintWriter out = response.getWriter();
        String actionType = request.getParameter("actionType");
        SaveRelationDAO saveRelationDAO = new SaveRelationDAO();

        if (actionType.equalsIgnoreCase("save")) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("actionType is "+actionType);
            String relWithId = request.getParameter("relWithId");
            String relationArray = request.getParameter("relationArray");
            boolean b = false;
            String[] relId = null;
            String[] relation = null;
            if (relWithId.length() != 0) {
                relId = relWithId.split(",");
            }
            if (relationArray.length() != 0) {
                relation = relationArray.split(",");
            }
            if (relWithId != null && relationArray != null) {
                b = saveRelationDAO.insertRelations(relId, relation);
            }

            out.print(b);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("first relWithId"+relWithId);
        } else {
            //  response.setContentType("text/xml");
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("i Should get Saved Rels");
            String tableIDs = request.getParameter("tableIDs");
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tableIDs "+tableIDs);
            String relations = saveRelationDAO.getSavedRelations(tableIDs);
            out.print(relations);
        }

        return mapping.findForward(null);
    }
}
