package com.progen.wigdets;

import com.progen.cacheLayer.CacheLayer;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utils.db.ProgenConnection;

public class SavePrioritizeLinks extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SavePrioritizeLinks.class);
    private final static String SUCCESS = "success";
    private String userId;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArrayList queries = new ArrayList();
        String query = "";
        String reportIds = "";
        userId = request.getParameter("userId");

        try {
            String[] links = request.getParameterValues("favouriteSelect");

            if (links != null) {
                for (int i = 0; i < links.length; i++) {
                    String[] val = links[i].split("~");
                    String custname = request.getParameter("custname" + val[1]);
                    query = "update prg_ar_user_reports set pur_report_sequence = " + val[0] + ",PUR_CUST_REPORT_NAME='" + custname + "' where report_id = " + val[1].replaceAll("select", "").replaceAll("SELECT", "").replaceAll("fav_", "");
//                ////.println("query"+(i+1)+" "+query);
                    reportIds = reportIds + "," + val[1];
                    queries.add(query);
                    val = null;
                }
            }
            if (!reportIds.equalsIgnoreCase("")) {
                reportIds = reportIds.substring(1).replaceAll("select", "").replaceAll("SELECT", "").replaceAll("fav_", "");
            }
            String nullQuery = "";
            new ProgenWidgetsDAO().saveFavReport(reportIds, userId);
            if (!reportIds.equalsIgnoreCase("")) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    nullQuery = "update prg_ar_user_reports set pur_report_sequence =null  where report_id not in (" + reportIds + ") AND user_id=" + userId;
                } else {
                    nullQuery = "update prg_ar_user_reports set pur_report_sequence = '' where report_id not in (" + reportIds + ") AND user_id=" + userId;
                }
            } else {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    nullQuery = "update prg_ar_user_reports set pur_fav_report = 'N' where user_id=" + userId;
                } else {
                    nullQuery = "update prg_ar_user_reports set pur_fav_report = 'N' where user_id=" + userId;
                }
            }
            queries.add(nullQuery);
            boolean status = new ProgenWidgetsDAO().savePriorityLinks(queries);
            response.getWriter().print(status);
        } catch (IOException e) {
            logger.error("Exception: ", e);
        }

        CacheLayer cacheLayer = CacheLayer.getCacheInstance();// ByPrabal  
        cacheLayer.setdata("GetAllfavReportsLandingPage" + userId, null);
        //return mapping.findForward(SUCCESS);
        return null;
    }
}
