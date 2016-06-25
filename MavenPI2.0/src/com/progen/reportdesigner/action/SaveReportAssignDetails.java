package com.progen.reportdesigner.action;

import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class SaveReportAssignDetails extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveReportAssignDetails.class);
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ReportAssignmentsResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new ReportAssignmentsResBundleMySql();
            } else {
                resourceBundle = new ReportAssignmentsResourceBundle();
            }
        }
        return resourceBundle;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        try {
            String reportId = null;
            String reports = request.getParameter("reports");
            String users = request.getParameter("users");

            if (request.getParameter("showGraphTable") == null) {
                reportId = request.getParameter("reportId");
            } else {
                reportId = (String) request.getAttribute("REPORTID");
            }
            String reportType = request.getParameter("reportType");
            String userFolderId = request.getParameter("userFolderId");
            String grpIds = request.getParameter("grpIds");
            String oldReportId = request.getParameter("oldReportId");
            String FavQuery = request.getParameter("FavQuery");
            String useradd = request.getParameter("useradd");
            if (reportType != null && !reportType.equalsIgnoreCase("I")) {
                HashMap map = new HashMap();
                Container container = null;
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                session.setAttribute("PROGENTABLES", map);
            }

            //////.println("reports in execute are:: "+reports);

            //added by uday on 20-mar-2010
            String isWhatIfReport = "";
            if (request.getParameter("isWhatIfReport") != null) {
                isWhatIfReport = request.getParameter("isWhatIfReport");
            }
            //////.println("isWhatIfReport is:: 2 "+isWhatIfReport);
            request.setAttribute("REPORTID", reportId);
//            HashMap map = new HashMap();
//            Container container = null;
//            map = (HashMap) session.getAttribute("PROGENTABLES");
//            container = (Container) map.get(reportId);
//            session.setAttribute("PROGENTABLES", map);
            PbDb pbdb = new PbDb();
            ArrayList list = new ArrayList();
//            ReportAssignmentsResourceBundle resBundle = new ReportAssignmentsResourceBundle();
            String addReportParents = getResourceBundle().getString("addReportParents");
            String finalQuery = "";
            //added by sanhtosh.k on 09-03-2010
            if (reportType != null && !reportType.equalsIgnoreCase("I")) {
                HashMap PROGENTABLES = (HashMap) session.getAttribute("PROGENTABLES");
                PROGENTABLES.remove(reportId);
                if (!oldReportId.equalsIgnoreCase("") && !oldReportId.equalsIgnoreCase(null) && oldReportId != null) {
                    //////.println("oldReportId===="+oldReportId);
                    PROGENTABLES.remove(oldReportId);
                }
            }
            PbReturnObject pbro = null;
            Object obj[] = null;
            //end of code added by sanhtosh.k on 09-03-2010
            if (reportType.equalsIgnoreCase("R")) {
                if (!reports.equalsIgnoreCase("")) {
                    String repIds[] = reports.split(",");
                    for (int i = 0; i < repIds.length; i++) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            obj = new Object[1];
                            obj[0] = repIds[i];
                        } else {
                            pbro = pbdb.execSelectSQL("select PRG_AR_REPORT_PARENTS_SEQ.nextval from dual");
                            String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
                            obj = new Object[3];
                            obj[0] = seqNum;
                            obj[1] = reportId;
                            obj[2] = repIds[i];
                        }
                        finalQuery = pbdb.buildQuery(addReportParents, obj);
                        list.add(finalQuery);
                    }
                } else {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        obj = new Object[1];
                        obj[0] = "999999";
                        finalQuery = pbdb.buildQuery(addReportParents, obj);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        obj = new Object[2];
                        pbro = pbdb.execSelectSQL("select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1");
                        reportId = String.valueOf(pbro.getFieldValueInt(0, 0));
                        obj[0] = reportId;
                        obj[1] = "999999";
                        finalQuery = pbdb.buildQuery(addReportParents, obj);
                    } else {
                        obj = new Object[3];
                        pbro = pbdb.execSelectSQL("select PRG_AR_REPORT_PARENTS_SEQ.nextval from dual");
                        String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
                        obj[0] = seqNum;
                        obj[1] = reportId;
                        obj[2] = "999999";
                        finalQuery = pbdb.buildQuery(addReportParents, obj);
                    }
                    list.add(finalQuery);
                }
            } else {
                if (!reports.equalsIgnoreCase("")) {
                    String repIds[] = reports.split(",");
                    for (int i = 0; i < repIds.length; i++) {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            obj = new Object[1];
                            obj[0] = repIds[i];
                        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            obj = new Object[2];
                            pbro = pbdb.execSelectSQL("select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1");
                            reportId = String.valueOf(pbro.getFieldValueInt(0, 0) + 1);
                            obj[0] = reportId;
                            obj[1] = "999999";
                            finalQuery = pbdb.buildQuery(addReportParents, obj);
                        } else {
                            pbro = pbdb.execSelectSQL("select PRG_AR_REPORT_PARENTS_SEQ.nextval from dual");
                            String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
                            obj = new Object[3];
                            obj[0] = seqNum;
                            obj[1] = repIds[i];
                            obj[2] = reportId;
                        }
                        finalQuery = pbdb.buildQuery(addReportParents, obj);
                        list.add(finalQuery);
                    }
                }
            }
            //code to Assign users to reports
         /*
             * String finalUsers = users; String userFolderList[] =
             * userFolderId.split(","); String grpIdList[] = grpIds.split(",");
             * for (int k = 0; k < userFolderList.length; k++) { String
             * getUserFolderExistance =
             * getResourceBundle().getString("getUserFolderExistance"); Object
             * obj1[] = new Object[1]; obj1[0] = userFolderList[k]; finalQuery =
             * pbdb.buildQuery(getUserFolderExistance, obj1); PbReturnObject
             * pbro1 = pbdb.execSelectSQL(finalQuery); if (pbro1.getRowCount() >
             * 0) { String getUserFolderusers =
             * getResourceBundle().getString("getUserFolderusers"); Object
             * obj2[] = new Object[2]; obj2[0] = users; obj2[1] =
             * userFolderList[k]; finalQuery =
             * pbdb.buildQuery(getUserFolderusers, obj2);
             *
             * PbReturnObject pbro2 = pbdb.execSelectSQL(finalQuery); for (int i
             * = 0; i < pbro2.getRowCount(); i++) { finalUsers += "," +
             * pbro2.getFieldValueInt(i, 0); } } else { String getBusGrpUsers =
             * getResourceBundle().getString("getBusGrpUsers"); Object obj3[] =
             * new Object[2]; obj3[0] = users; obj3[1] = grpIdList[k];
             * finalQuery = pbdb.buildQuery(getBusGrpUsers, obj3);
             * PbReturnObject pbro3 = pbdb.execSelectSQL(finalQuery); for (int i
             * = 0; i < pbro3.getRowCount(); i++) { finalUsers += "," +
             * pbro3.getFieldValueInt(i, 0); } } } if (!(finalUsers == null) &&
             * !(finalUsers.equalsIgnoreCase(""))) { finalUsers = "," +
             * String.valueOf(session.getAttribute("USERID")); } else {
             * finalUsers = String.valueOf(session.getAttribute("USERID")); } if
             * (finalUsers.startsWith(",")) { finalUsers =
             * finalUsers.substring(1); } String finalUserIds[] =
             * finalUsers.split(","); String finalUsers1 = ""; for (int j = 0; j
             * < finalUserIds.length - 1; j++) { int count = 0; for (int j1 = j
             * + 1; j1 < finalUserIds.length; j1++) { if
             * (finalUserIds[j].equalsIgnoreCase(finalUserIds[j1])) { count = 1;
             * break; } } if (count == 0) { finalUsers1 += "," +
             * finalUserIds[j]; } if (j == finalUserIds.length - 2) {
             * finalUsers1 += "," + finalUserIds[j + 1]; } } if
             * (!finalUsers1.equalsIgnoreCase("")) { finalUsers1 =
             * finalUsers1.substring(1);
            }
             */
            String finalUsers1 = users;//above code is not needed
            if (finalUsers1.equalsIgnoreCase("")) {
                finalUsers1 = (String) session.getAttribute("USERID");
            }
//                 pbdb.execUpdateSQL("delete from PRG_AR_USER_REPORTS where REPORT_ID = "+ reportId +" and USER_ID not in (select PU_ID from PRG_AR_USERS where PU_LOGIN_ID ='PROGEN')");
            if (reportType.equalsIgnoreCase("I")) {
                pbdb.execUpdateSQL("delete from PRG_INSIGHT_ASSIGNMENT where INSIGHT_ID=" + reportId);
                String finalUserIds1[] = finalUsers1.split(",");
                PbReportViewerDAO dao = new PbReportViewerDAO();
                dao.assignInsightToUsers(reportId, finalUserIds1);
            } else {
                pbdb.execUpdateSQL("delete from PRG_AR_USER_REPORTS where REPORT_ID = " + reportId);
                String finalUserIds1[] = finalUsers1.split(",");
                pbro = null;
                ReportTemplateDAO templateDAO = new ReportTemplateDAO();
                templateDAO.assignReportToUsers(reportId, finalUserIds1);
            }
            /*
             * String addUserReports =
             * getResourceBundle().getString("addUserReports"); Object obj4[] =
             * null; for (int i = 0; i < finalUserIds1.length; i++) { if
             * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
             * { obj4 = new Object[3]; obj4[0] = finalUserIds1[i];
             * obj4[1]=reportId; if (isWhatIfReport.equalsIgnoreCase("Y")) {
             * obj4[2] = isWhatIfReport; } else { obj4[2] = ""; }
             *
             *
             *
             * } else { pbro = pbdb.execSelectSQL("select
             * PRG_AR_USER_REPORTS_SEQ.nextval from dual"); obj4 = new
             * Object[4]; String seqNum =
             * String.valueOf(pbro.getFieldValueInt(0, 0)); obj4[0] = seqNum;
             * obj4[1] = finalUserIds1[i]; obj4[2] = reportId; if
             * (isWhatIfReport.equalsIgnoreCase("Y")) { obj4[3] =
             * isWhatIfReport; } else { obj4[3] = ""; } }
             *
             * finalQuery = pbdb.buildQuery(addUserReports, obj4);
             * ////.println("PRG_AR_USER_REPORTS="+finalQuery);
             * list.add(finalQuery);
            }
             */
//            if (finalUsers1.equalsIgnoreCase("")) {
//                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    String delQuery = "delete from PRG_AR_USER_REPORTS where REPORT_ID=ident_current('PRG_AR_REPORT_MASTER')";
//                    pbdb.execUpdateSQL(delQuery);
//                    obj4 = new Object[2];
//                    obj4[0] = session.getAttribute("USERID");
//                    obj4[1] = reportId;
//                } else {
//                    String delQuery = "delete from PRG_AR_USER_REPORTS where REPORT_ID=" + reportId;
//                    pbdb.execUpdateSQL(delQuery);
//                    pbro = pbdb.execSelectSQL("select PRG_AR_USER_REPORTS_SEQ.nextval from dual");
//                    String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
//                    obj4 = new Object[3];
//                    obj4[0] = seqNum;
//                    obj4[1] = session.getAttribute("USERID");
//                    obj4[2] = reportId;
//                }
//
//                finalQuery = pbdb.buildQuery(addUserReports, obj4);
//                 ////.println("PRG_AR_USER_REPORTS="+finalQuery);
//                pbdb.execModifySQL(finalQuery);
//
//            } else {
//                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    String delQuery = "delete from PRG_AR_USER_REPORTS where REPORT_ID=ident_current('PRG_AR_REPORT_MASTER')";
//                    pbdb.execUpdateSQL(delQuery);
//                } else {
//                    String delQuery = "delete from PRG_AR_USER_REPORTS where REPORT_ID=" + reportId;
//                    pbdb.execUpdateSQL(delQuery);
//                }

//                ////.println("list queries are : " + list);
            pbdb.execMultiple(list);
//            }

            if (FavQuery != null && !FavQuery.equalsIgnoreCase("null")) {
                pbdb.execUpdateSQL(FavQuery);
            }
            if (reportType != null && reportType.equalsIgnoreCase("I")) {
                return mapping.findForward("workBench");
            } else if (reportType != null && reportType.equalsIgnoreCase("R")) {
                if (isWhatIfReport.equalsIgnoreCase("Y")) {
                    return mapping.findForward("whatIfScenarioView");
                } else {
                    return mapping.findForward("forwardToReport");
                }
            } else {
                return mapping.findForward("forwardToDashBoard");
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
            return mapping.findForward("exceptionPage");
        }


    }
}
