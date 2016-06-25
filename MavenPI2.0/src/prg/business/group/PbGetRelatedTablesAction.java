/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 *
 * @author Saurabh
 */
public class PbGetRelatedTablesAction extends LookupDispatchAction {

    public ActionForward getRelatedTables(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableId = request.getParameter("tableId");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tableId is:: "+tableId);

        BusinessGroupDAO relatedTablesDAO = new BusinessGroupDAO();
        String relatedTablesList = relatedTablesDAO.getRelatedTablesList(tableId);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("relatedTablesList is::------------ "+relatedTablesList);

        request.setAttribute("relatedTablesList", relatedTablesList);
        PrintWriter out = response.getWriter();
        out.print(relatedTablesList);

        //return mapping.findForward("relatedTablesList");
        return null;
    }

    public ActionForward getEditRelationSection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableId = request.getParameter("tableId");
        String grpId = request.getParameter("grpId");
        //////////////////////////////////////////////////////////////////////////////.println.println(" grpId "+grpId);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tableId in edit is:: "+tableId);

        BusinessGroupDAO relatedTablesDAO = new BusinessGroupDAO();
        String editRelationshipSection = relatedTablesDAO.getEditRelationSection(tableId, grpId);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("editRelationshipSection is::------------ "+editRelationshipSection);

        request.setAttribute("editRelationshipSection", editRelationshipSection);
        PrintWriter out = response.getWriter();
        out.print(editRelationshipSection);

        //return mapping.findForward("relatedTablesList");
        return null;
    }

    public ActionForward updateRelations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String relationIds = request.getParameter("relationIds");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("relationIds in edit are:: "+relationIds);
        String relationDetailsIds = request.getParameter("relationDetailsIds");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("relationDetailsIds in edit are:: "+relationDetailsIds);
        String commitRelationTxt = request.getParameter("commitRelationTxt");
        commitRelationTxt = commitRelationTxt.replaceAll("@", "+");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("commitRelationTxt in edit are:: "+commitRelationTxt);
        String joinTypeStr = request.getParameter("joinTypeStr");
        String allSelectTypes = request.getParameter("allSelectTypes");
        //////////////////////////////////////////////////////////////////////////////.println.println("allSelectTypes is:: "+allSelectTypes);

        BusinessGroupDAO relatedTablesDAO = new BusinessGroupDAO();
        relatedTablesDAO.updateRelatedTablesRelations(relationIds, relationDetailsIds, commitRelationTxt, joinTypeStr, allSelectTypes);
        PrintWriter out = response.getWriter();
        out.println("1");

        return null;
    }

    /**
     * Provides the mapping from resource key to method name.
     *
     * @return Resource key / method name map.
     */
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getRelatedTables", "getRelatedTables");
        map.put("getEditRelationSection", "getEditRelationSection");
        map.put("updateRelations", "updateRelations");
        return map;
    }
}