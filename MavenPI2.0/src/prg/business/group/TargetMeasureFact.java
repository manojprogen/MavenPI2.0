/*
 added by susheela 01-oct-09
 */
package prg.business.group;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Saurabh
 */
public class TargetMeasureFact extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(TargetMeasureFact.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ArrayList newList = new ArrayList();
        String grpId = request.getParameter("grpId");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" grpId............................ "+grpId);
        BusinessGroupListDAO bussGroupListDAO = new BusinessGroupListDAO();
        BusinessGroupDAO businessGroupDAO = new BusinessGroupDAO();
        TargetMeasureParametersDAO tmpDao = new TargetMeasureParametersDAO();

        //newList = bussGroupListDAO.getTables(grpId);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" new List.... "+newList);
        if (newList.size() > 0) {
            ArrayList tableList = businessGroupDAO.getTableNames();
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" tableList "+tableList);
            request.setAttribute("allTablesList", newList);
            request.setAttribute("list", tableList);
        }

        return mapping.findForward(SUCCESS);
    }
}
