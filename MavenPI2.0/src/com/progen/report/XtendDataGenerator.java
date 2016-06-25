package com.progen.report;

import com.progen.reportview.bd.PbReportViewerBD;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.Container;

public class XtendDataGenerator extends LookupDispatchAction {

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("generateData", "generateData");
        return map;
    }

    public ActionForward generateData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, Exception {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");
        String reportId = request.getParameter("reportId");
        session.setAttribute("REPORTVERSION", "1.1");
        session.setAttribute("USERID", userId);
        Container container = Container.getContainerFromSession(request, reportId);
        request.setAttribute("REPORTID", reportId);
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        reportViewerBD.prepareReportForXtend(reportId);
        return null;
    }
}
