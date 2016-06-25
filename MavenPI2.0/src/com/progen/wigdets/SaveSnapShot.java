/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.wigdets;

import com.progen.reportview.action.SnapshotDesigner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class SaveSnapShot extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        PbDb pbDB = new PbDb();
        if (session != null) {
            String userId = String.valueOf(session.getAttribute("USERID"));
            String reportId = request.getParameter("ReportId");
            String repcustname = request.getParameter("repcustname");
            String emails = request.getParameter("toAddress");//UrlVal
            String snapDate = request.getParameter("snapDate");
            String fromOption = request.getParameter("fromOption");
            if (fromOption == null) {
                fromOption = "fromSnapshot";
            }
            String[] email = null;
            String emailList = "";
            String sql = "";

            if (emails != null) {
                email = emails.split(",");
                for (int i = 0; i < email.length; i++) {
                    emailList = emailList + ",'" + email[i] + "'";
                }
                if (email.length > 0) {
                    emailList = emailList.substring(1);
                }
            }
            String completeurl = request.getParameter("UrlVal");
            SnapshotDesigner SSDesigner = new SnapshotDesigner();
            if (fromOption.equalsIgnoreCase("headline")) {
                sql = "select PRG_REPORT_HEADLINE from PRG_AR_PERSONALIZED_REPORTS where PRG_REPORT_ID=" + reportId + " and PRG_REPORT_HEADLINE='" + repcustname + "'";
            } else {
                sql = "select PRG_REPORT_CUST_NAME from PRG_AR_PERSONALIZED_REPORTS where PRG_REPORT_ID=" + reportId + " and PRG_REPORT_CUST_NAME='" + repcustname + "'";
            }

            PbReturnObject PbRetObj = pbDB.execSelectSQL(sql);
            if (!(PbRetObj.getRowCount() > 0 && PbRetObj != null)) {
                SSDesigner.createDocument(reportId, completeurl, request, snapDate, emailList, userId, repcustname, fromOption);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
}
