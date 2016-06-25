/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

/**
 *
 * @author Administrator
 */
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbReturnObject;

public class GetrolesAction extends Action {

    public void sop(String s) {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(s);
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PrintWriter out = response.getWriter();
        ReportsDAO repdao = new ReportsDAO();
        PbReturnObject rolereppbro = null;

        String param = request.getParameter("param");
        String rolevalue = "";
        StringBuffer reportLinks = new StringBuffer("");
        StringBuffer sbuffer = new StringBuffer("");
        int rolesid;
        String reportURL = "";
        if (param != null) {
            sop(param);
            if (param.equalsIgnoreCase("roles")) {
                String roleid = request.getParameter("roleId");
                sop(roleid);
                rolereppbro = repdao.roles(roleid);
                sbuffer.append("<table width='100%' align='center' style='height:50px;overflow:auto' >");
                for (int i = 0; i < rolereppbro.getRowCount(); i++) {
                    sbuffer.append("<tr>");
                    sbuffer.append("<td>");

                    rolevalue = rolereppbro.getFieldValueString(i, 1);
                    rolesid = rolereppbro.getFieldValueInt(i, 0);
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(rolevalue);
                    reportURL = "reportViewer.do?reportBy=viewReport&REPORTID=" + rolereppbro.getFieldValueInt(i, 0);
                    sbuffer.append("<a href='javascript:void(0)' onclick=javascript:submiturls1('" + reportURL + "') >" + rolevalue + "</a>");

                    sbuffer.append("</td>");
                    sbuffer.append("</tr>");
                }

                sbuffer.append("</table>");
            }
            out.println(sbuffer.toString());

            return null;
        }
        return mapping.findForward("");
    }
}
