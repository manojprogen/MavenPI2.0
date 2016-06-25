/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.studio;

import com.google.gson.Gson;
import com.progen.execution.DisplayRequest;
import com.progen.execution.EtlLoadForJKP;
import com.progen.execution.ExecuteQuery;
import com.progen.execution.ProgramExecusion;
import com.progen.search.suggest.AutoSuggest;
import com.progen.userlayer.db.DeEncrypter;
import com.progen.userlayer.db.LogReadWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.SourceConn;

/**
 *
 * @author progen
 */
public class StudioAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(StudioAction.class);
    String idValue = null;
    String connName = null;
    String userName = null;
    String password = null;
    private boolean isDebugEnable = false;

    @Override
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getAllItems", "getAllItems");
        map.put("autoSuggest", "autoSuggest");
        map.put("displayList", "displayList");
        map.put("executeQuery", "executeQuery");
        map.put("getDetails", "getDetails");
        map.put("getMaster", "getMaster");
        map.put("cargoQuery", "cargoQuery");
        map.put("getAllAO", "getAllAO");
        return map;
    }

    public ActionForward getAllItems(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String fromTab = request.getParameter("fromTab");
        String toPage = "";
        StudioBd studioBd = new StudioBd();
        Studio studio = new Studio();
        Studio studio2 = new Studio();
        Gson gson = new Gson();
        Locale locale = null;
        if (session.getAttribute("ISDEBUGENABLE") != null) {
            isDebugEnable = Boolean.parseBoolean(session.getAttribute("ISDEBUGENABLE").toString());
        }
        if (session != null) {
            if (session.getAttribute("userLocale") != null) {
                locale = (Locale) session.getAttribute("userLocale");
            }

            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();
            }
            if (fromTab.equalsIgnoreCase("ReportStudio")) {
                studio = studioBd.getAllReports(userid, fromTab, locale);
                toPage = "repList";
                //added by krishan
            } else if (fromTab.equalsIgnoreCase("ReportBuilder")) {
                studio = studioBd.getAllReports(userid, fromTab, locale);
                toPage = "repList1";
            } else if (fromTab.equalsIgnoreCase("AOBuilder")) {
                studio = studioBd.getAllAO(userid, fromTab, locale);
                toPage = "aoList";
            } else if (fromTab.equalsIgnoreCase("DashboardStudio")) {
                studio = studioBd.getAllDashboards(userid, fromTab, locale);
                toPage = "dashList";
                studio2 = studioBd.getAllKPIDashboards(userid, fromTab, locale);
                if (studio2 != null) {
                    request.setAttribute("KPIDashboarditemList", gson.toJson(studio2));
                }

            } else if (fromTab.equalsIgnoreCase("Scorecard")) {
                studio = studioBd.getAllScorecards(userid, fromTab, locale);
                toPage = "scorecard";
            } else if (fromTab.equalsIgnoreCase("MyReports")) {
                studio = studioBd.getAllReportsHome(userid, locale);
                toPage = "allrepList";
            } else if (fromTab.equalsIgnoreCase("HtmlReports")) {
                studio = studioBd.getAllSnapshots(userid, locale);
                toPage = "snapshot";
            } else if (fromTab.equalsIgnoreCase("MOBuilder")) {
                studio = studioBd.getAllTemplateInfo(userid, fromTab, locale);
                toPage = "managementList";
            }
            session.setAttribute("studio", studio);
            request.setAttribute("itemList", gson.toJson(studio));
            return mapping.findForward(toPage);
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward autoSuggest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String fromTab = request.getParameter("tab");
        if (session.getAttribute("ISDEBUGENABLE") != null) {
            isDebugEnable = Boolean.parseBoolean(session.getAttribute("ISDEBUGENABLE").toString());
        }
        if (session != null) {
            String qryString = request.getParameter("query");

            AutoSuggest suggest = new AutoSuggest(qryString);
            Gson gson = new Gson();
            String userId = request.getParameter("userId");

            StudioBd studioBd = new StudioBd();

            suggest = studioBd.createAutoSuggestJson(qryString, userId, fromTab);
            try {
                response.getWriter().print(gson.toJson(suggest));
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward displayList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String portals = null;
        Studio studios = new Studio();
        Gson gson = new Gson();
        PrintWriter out = null;
        Locale locale = null;
        String searchText = request.getParameter("searchText");
        String userId = request.getParameter("userId");
        PbReturnObject ret = new PbReturnObject();
        String type = request.getParameter("type");
        if (session.getAttribute("userLocale") != null) {
            locale = (Locale) session.getAttribute("userLocale");
        }

        if (type != null && type.equals("Portals")) {
            try {
                Studio studio = null;
                StudioBd studioBd = new StudioBd();
                out = response.getWriter();
                ret = studioBd.getAllportals(userId, type, searchText);
                studio = studioBd.createStudio(ret, type, locale);
                Studio selectedStudio = studioBd.getSelectedStudioItem(studio, searchText);
                out.print(gson.toJson(selectedStudio));
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } else {
            AutoSuggest suggest = new AutoSuggest(searchText);
            StudioBd studioBd = new StudioBd();
            if (session.getAttribute("ISDEBUGENABLE") != null) {
                isDebugEnable = Boolean.parseBoolean(session.getAttribute("ISDEBUGENABLE").toString());
            }
            try {
                Studio studio = (Studio) session.getAttribute("studio");
                out = response.getWriter();
                Studio selectedStudio = studioBd.getSelectedStudioItem(studio, searchText);
                out.print(gson.toJson(selectedStudio));

            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        return null;
    }

    public ActionForward executeQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final ExecuteQuery exQry = new ExecuteQuery();
        final ProgramExecusion proExe = new ProgramExecusion();
        final LogReadWriter logrw = new LogReadWriter();
        final SourceConn sc = new SourceConn();
        PbReturnObject retObj = null;
        PbDb pbdb = new PbDb();
        String query = "";
        String loadVal = "";

//        final Connection connectionSC = sc.getConnection("seaLinkdw", "", "", "","","","","","");


        idValue = request.getParameter("ids");
        connName = request.getParameter("connName");
//      userName = request.getParameter("userName");
//      password = request.getParameter("password");


        if (idValue.equals("QuickInit")) {

            logrw.setLogFileName("QtInit");
            final String option = "true";

            try {
                logrw.fileWriter("truncate QT-->" + option);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            final String forceInit = "INIT";
            final String forceReq = "Quick Travel";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
//                            Connection con = sc.getConnection("oracle1", "", "", "","","","","","");
                            String loadVal = "";
                            String query = "";

                            proExe.truncateQuickTravelStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "quickTravel";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
                                    //changed for CCC
//                                proExe.loadQuickTravel(forceInit,connName,userName,password,option);
                                    proExe.loadQuickTravel(forceInit, compId, loadType, userName, password, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadQuickTravelInit();

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR QT INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception:", ex);
                            }
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("QuickIncr")) {
            logrw.setLogFileName("QtIncr");
            final String option = "true";

            try {
                logrw.fileWriter("truncate QT-->" + option);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            final String forceInit = "INCR";
            final String forceReq = "Quick Travel";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
//                            Connection con = sc.getConnection("oracle1", "", "", "","","","","","");
                            String loadVal = "";
                            String query = "";

                            proExe.truncateQuickTravelStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "quickTravel";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);

                                    //end for CCC

                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
                                    //changed for CCC
                                    //proExe.loadQuickTravel(forceInit,connName,userName,password,option);
                                    proExe.loadQuickTravel(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadQuickTravelIncr();
//                            
//                            try {
//                                logrw.fileWriter("Running ETL Procedure");
//                            } catch (IOException ex) {
//                                logger.error("Exception:",ex);
//                            }
//                            CallableStatement proc = null;
//                            try
//                            {
//                                try {
//                                    proc = connectionSC.prepareCall("{ call procForCCCETL() }");
//                                    proc.execute();
//                                } catch (SQLException ex) {
//                                    logger.error("Exception:",ex);
//                                }
//                            }
//                            finally
//                            {
//                                try {
//                                    proc.close();
////                                    connectionSC.close();
//                                } catch (SQLException ex) {
//                                    logger.error("Exception:",ex);
//                                }
//                            }

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR QT INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception:", ex);
                            }
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("CtInit")) {
            logrw.setLogFileName("CtInit");
            final String option = request.getParameter("option");

            try {
                logrw.fileWriter("truncate CT Server-->" + option);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            final String forceInit = "INIT";
            final String forceReq = "CT Server";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
//                            Connection con = sc.getConnection("oracle1", "", "", "","","","","","");
                            String loadVal = "";
                            String query = "";

                            proExe.truncateCtServerStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "ctServer";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";
                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC

                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
                                    //changed for CCC
                                    //proExe.loadCtServer(forceInit,connName,userName,password,option);
                                    proExe.loadCtServer(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadCTServerInit();

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR QT INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception:", ex);
                            }
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("AccInit")) {
            logrw.setLogFileName("AccInit");
            final String option = request.getParameter("option");

            try {
                logrw.fileWriter("truncate Accpac-->" + option);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            final String forceInit = "INIT";
            final String forceReq = "Accpac";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
//                            Connection con = sc.getConnection("oracle1", "", "", "","","","","","");
                            String loadVal = "";
                            String query = "";

                            proExe.truncateAccpacStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "accpac";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC

                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
                                    //changed for CCC
                                    //proExe.loadAccpac(forceInit,connName,userName,password,option);
                                    proExe.loadAccpac(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadAccpacInit();

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR ACCPAC INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception:", ex);
                            }
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
                        }
                    },
                    10000);

        } else if (idValue.equals("ComInit")) {
            logrw.setLogFileName("CombInit");
            final String optionAcc = request.getParameter("optionAcc");
            final String optionCt = request.getParameter("optionCt");
            final String forceInit = "INIT";
            final String option = "true";
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            String forceReq = "";
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
//                            Connection con = sc.getConnection("oracle1", "", "", "","","","","","");
                            String loadVal = "";
                            String query = "";
                            //QT INIT Load
                            proExe.truncateQuickTravelStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "quickTravel";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC
                                    forceReq = "Quick Travel";


                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                        logrw.fileWriter("truncate QT-->" + option);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    proExe.runLoad(forceInit, forceReq, compId);
                                    //changed for CCC
//                                    proExe.loadQuickTravel(forceInit, connName, userName, password, option);
                                    proExe.loadQuickTravel(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadQuickTravelInit();
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
                            //changed for CCC
                            //CT server INIT Load
                            proExe.truncateCtServerStg(option);
                            Connection con1 = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");

                            loadVal = "ctServer";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con1);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);

                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId----->" + compId);
                                        logrw.fileWriter("truncate CT Server-->" + optionCt);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    forceReq = "CT Server";
                                    proExe.runLoad(forceInit, forceReq, compId);
                                    //changed for CCC
//                                    proExe.loadCtServer(forceInit, connName, userName, password, optionCt);
                                    proExe.loadCtServer(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            if (con1 != null) {
                                try {
                                    con1.close();
                                    con1 = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
                            proExe.loadCTServerInit();

                            //changed for CCC
                            //Accpac INIT Load
                            proExe.truncateAccpacStg(option);
                            Connection con2 = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "accpac";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con2);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                        logrw.fileWriter("truncate Accpac-->" + optionAcc);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    forceReq = "Accpac";
                                    proExe.runLoad(forceInit, forceReq, compId);
                                    //changed for CCC
//                              proExe.loadAccpac(forceInit, connName, userName, password, optionAcc);
                                    proExe.loadAccpac(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadAccpacInit();

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR COMBINED INIT--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception:", ex);
                            }
                            if (con2 != null) {
                                try {
                                    con2.close();
                                    con2 = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("ComIncr")) {
            logrw.setLogFileName("CombIncr");
            final String optionAcc = request.getParameter("optionAcc");
            final String optionCt = request.getParameter("optionCt");
            final String option = "true";

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            String forceInit = "";
                            String forceReq = "";
                            //changed for CCC
                            PbReturnObject retObj = null;
                            PbDb pbdb = new PbDb();
                            String loadVal = "";
                            String query = "";

                            //QT INCR Load
                            proExe.truncateQuickTravelStg(option);
                            Connection con = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "quickTravel";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                        logrw.fileWriter("truncate QT-->" + option);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    forceInit = "INCR";
                                    forceReq = "Quick Travel";
                                    proExe.runLoad(forceInit, forceReq, compId);
                                    //changed for CCC
//                              proExe.loadQuickTravel(forceInit, connName, userName, password, option);
                                    proExe.loadQuickTravel(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadQuickTravelIncr();
                            if (con != null) {
                                try {
                                    con.close();
                                    con = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
//                            
//                            try {
//                                logrw.fileWriter("Running ETL Procedure");
//                            } catch (IOException ex) {
//                                logger.error("Exception:",ex);
//                            }
//                            CallableStatement proc = null;
//                            try
//                            {
//                                try {
//                                    proc = connectionSC.prepareCall("{ call procForCCCETL() }");
//                                    proc.execute();
//                                } catch (SQLException ex) {
//                                    logger.error("Exception:",ex);
//                                }
//                            }
//                            finally
//                            {
//                                try {
//                                    proc.close();
////                                    connectionSC.close();
//                                } catch (SQLException ex) {
//                                    logger.error("Exception:",ex);
//                                }
//                            }
                            //changed for CCC
                            //CT Server INIT Load
                            proExe.truncateCtServerStg(option);
                            Connection con1 = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "ctServer";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con1);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                        logrw.fileWriter("truncate CT Server-->" + optionCt);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    forceInit = "INIT";
                                    forceReq = "CT Server";
                                    proExe.runLoad(forceInit, forceReq, compId);
//                              changed for CCC
//                              proExe.loadCtServer(forceInit, connName, userName, password, optionCt);
                                    proExe.loadCtServer(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadCTServerInit();
                            if (con1 != null) {
                                try {
                                    con1.close();
                                    con1 = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }
                            //changed for CCC
                            //Accpac INIT Load
                            proExe.truncateAccpacStg(option);
                            Connection con2 = sc.getConnection("oracle1", "", "", "", "", "", "", "", "");
                            loadVal = "accpac";
                            query = "SELECT COMPANY_ID,LOAD_TYPE,USER_NAME,PASSWORD,SERVER,SERVICE_ID,PORT,DBNAME,SOURCE_TIMEZONE,TARGET_TIMEZONE FROM ETL_CONNECTIONS_DETAILS WHERE LOAD_TYPE ='" + loadVal + "'";

                            try {
                                retObj = pbdb.execSelectSQL(query, con2);
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retObj != null && retObj.getRowCount() != 0) {
                                for (int j = 0; j < retObj.getRowCount(); j++) {
                                    final String compId = retObj.getFieldValueString(j, "COMPANY_ID");
                                    final String loadType = retObj.getFieldValueString(j, "LOAD_TYPE");
                                    final String userName = retObj.getFieldValueString(j, "USER_NAME");
                                    final String password = retObj.getFieldValueString(j, "PASSWORD");
                                    final String server = retObj.getFieldValueString(j, "SERVER");
                                    final String serviceId = retObj.getFieldValueString(j, "SERVICE_ID");
                                    final String port = retObj.getFieldValueString(j, "PORT");
                                    final String databaseName = retObj.getFieldValueString(j, "DBNAME");
                                    final String sourceTimezone = retObj.getFieldValueString(j, "SOURCE_TIMEZONE");
                                    final String targetTimezone = retObj.getFieldValueString(j, "TARGET_TIMEZONE");
                                    String password1 = DeEncrypter.getInstance().decrypt(password);
                                    //end for CCC


                                    try {
                                        logrw.fileWriter("compId------>" + compId);
                                        logrw.fileWriter("truncate Accpac-->" + optionAcc);
                                    } catch (IOException ex) {
                                        logger.error("Exception:", ex);
                                    }
                                    forceReq = "Accpac";
                                    proExe.runLoad(forceInit, forceReq, compId);
//                              changed for CCC
//                              proExe.loadAccpac(forceInit, connName, userName, password, optionAcc);
                                    proExe.loadAccpac(forceInit, compId, loadType, userName, password1, server, serviceId, port, option, databaseName, sourceTimezone, targetTimezone);
                                }
                            }
                            proExe.loadAccpacInit();
                            if (con2 != null) {
                                try {
                                    con2.close();
                                    con2 = null;
                                } catch (SQLException ex) {
                                    logger.error("Exception:", ex);
                                }
                            }

                            try {
                                logrw.fileWriter("-----------------------LOAD COMPLETED FOR COMBINED INCR--------------------------");
                            } catch (IOException ex) {
                                logger.error("Exception:", ex);
                            }

                            try {
                                con.close();
                                con = null;
                            } catch (SQLException ex) {
                                logger.error("Exception:", ex);
                            }
                        }
                    },
                    10000);
        } else if (idValue.equals("DFLoad")) {


            new java.util.Timer().schedule(
                    new java.util.TimerTask() {

                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
//                            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-dd-MM");
                            Date date = new Date();
                            String todaysDate = dateFormat.format(date);

//                            Date d1 = null;
//                            Date d2 = null;
//                            Date d3 = null;
//                            Date d4 = null;
//                            int yearDiffd1 = 0;
//                            int yearDiffd2 = 0;
//                            String finalStartDate = "";
//                            String finalEndDate = "";
                            final SourceConn sc = new SourceConn();
                            final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
                            // CallableStatement proc = null;
                            //call of truncate procedure
                            Connection con = null;
                            con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                            //Connection con1 = null;
                            //con1 = sc.getConnection("Df_oracle", "", "", "", "", "", "", "", "");
                            CallableStatement proc = null;
                            PbReturnObject retObj = null;
                            PbReturnObject retObj2 = null;
                            PbDb pbdb = new PbDb();

                            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from PRG_LOAD_TRACKER_MASTER";
                            String truntable[] = {"dvs_stg", "workitem_stg", "dvs_employment_stg", "documentverficationsystem_stg", "dvs_health_license_stg", "dvs_education_stg", "dvs_case_stg", "dvs_user_stg", "dvs_issuing_authority_stg", "processinstance_stg"};


                            try {
                                retObj = pbdb.execSelectSQL(query, con);
                                if (retObj != null && retObj.getRowCount() > 0) {


                                    Connection con1 = null;
                                    con1 = sc.getConnection("Df_oracle", "", "", "", "", "", "", "", "");
                                    for (int i = 0; i < truntable.length; i++) {
                                        con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");

                                        retObj2 = pbdb.execSelectSQL("TRUNCATE TABLE " + truntable[i] + ";", con);


                                    }
                                    for (int i = 0; i < retObj.getRowCount(); i++) {
                                        String tableName = retObj.getFieldValueString(i, 0);
                                        String startDate = retObj.getFieldValueString(i, 1);

                                        eltJkp.runLoadForDataflow(tableName, startDate, todaysDate, false, "");
                                        //updates in fact


                                    }

                                    //calling ending procedures

                                    //proc = con.prepareCall("{ call Incremental_Update_Insert() }");
                                    //proc.execute();
                                    //proc = con.prepareCall("{ call Dvs_Customer_Doc_Stg() }");
                                    //proc.execute();
                                    con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                                    proc = con.prepareCall("{ call dataflow.test() }");
                                    proc.execute();
                                    //proc = con.prepareCall("{ call Dimension() }");
                                    // proc.execute();


//                                        eltJkp.UpdateforDataflow("DOCUMENTVERFICATIONSYSTEM");
//                                          eltJkp.insertforDataflow("DOCUMENTVERFICATIONSYSTEM");
//                                          eltJkp.UpdateforDataflow("dvs_case");
//                                          eltJkp.insertforDataflow("dvs_case");
//                                          eltJkp.UpdateforDataflow("WORKITEM");
//                                          eltJkp.insertforDataflow("WORKITEM");
//                                          eltJkp.UpdateforDataflow("DVS_HEALTH_LICENSE");
//                                          eltJkp.insertforDataflow("DVS_HEALTH_LICENSE");
//                                          eltJkp.UpdateforDataflow("DVS");
//                                          eltJkp.insertforDataflow("DVS");
//                                          eltJkp.UpdateforDataflow("DVS_EMPLOYMENT");
//                                          eltJkp.insertforDataflow("DVS_EMPLOYMENT");
//                                          eltJkp.UpdateforDataflow("dvs_education");
//                                          eltJkp.insertforDataflow("dvs_education");
//                                          eltJkp.UpdateforDataflow("processinstance");
//                                          eltJkp.insertforDataflow("processinstance");
//                                          eltJkp.UpdateforDataflow("dvs_user");
//                                          eltJkp.insertforDataflow("dvs_user");
//                                          eltJkp.UpdateforDataflow("dvs_issuing_authority");
//                                          eltJkp.insertforDataflow("dvs_issuing_authority");





                                }

//                                sendSchedulerFinishMail(s1);

                            } catch (Exception ex) {
                                //sendSchedulerConnError("DATA FLOW LOAD ERROR");

                                logger.error("Exception:", ex);
                            }
                        }
                    },
                    10000);
        }









        //String values=vale.executeQueries();
        //
//        String val=request.getParameter("QuickInit");
//
//        
        //  return null;

        return mapping.findForward("quicksuccesstest");
    }

    public ActionForward getMaster(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        DisplayRequest eq = new DisplayRequest();
        String details = eq.runUiMasterQuery();

        response.getWriter().print(details);
        return null;
    }

    public ActionForward getDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String masterId = request.getParameter("ids");

        DisplayRequest eq = new DisplayRequest();
        String details = eq.runUiDetailQuery(masterId);

        response.getWriter().print(details);

        return null;
    }

    public ActionForward cargoQuery(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        idValue = request.getParameter("ids");

        ProgramExecusion pe = new ProgramExecusion();
        pe.runCargo(idValue);

        return null;
    }
}
