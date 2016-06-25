package org.querydesigner;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class DimensionTablesListAction extends org.apache.struts.action.Action {

//    Connection con = null;
//    Statement st = null;
//    ResultSet rs = null;
    PbReturnObject pbReturnObject = new PbReturnObject();
    /*
     * forward name="success" path=""
     */
    PbDb pbDb = new PbDb();
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("request.getParameter(dimId) is "+request.getParameter("dimId"));




        String connectionId = request.getParameter("connId");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connectionId--------->"+connectionId);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dimId--------->"+request.getParameter("dimId"));
        //int connectionId = 21;
        String Query = "select table_id,table_name from prg_db_master_table where connection_id=" + connectionId + "  and table_type!='Query'";
        // String Query = "select table_id,table_name from prg_db_master_table where table_type!='Query'";
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("query---------->"+Query);
        pbReturnObject = pbDb.execSelectSQL(Query);
        ArrayList a = new ArrayList();
        for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
            a.add(pbReturnObject.getFieldValueString(i, 0) + "-" + pbReturnObject.getFieldValueString(i, 1));
        }
        request.setAttribute("list", a);


        //return mapping.findForward(SUCCESS);
        request.setAttribute("dimId", request.getParameter("dimId"));
        request.getSession().setAttribute("dimId", request.getParameter("dimId"));
        //  return mapping.getInputForward();
        return mapping.findForward(SUCCESS);

    }
}
