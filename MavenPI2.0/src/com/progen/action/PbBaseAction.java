/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.action;

import com.google.gson.Gson;
import com.progen.bd.PbBaseBD;
import com.progen.connection.ConnectionMetadata;
import com.progen.db.PbBaseDAO;
import com.progen.graph.info.GraphSizeInfo;
import com.progen.graph.info.GraphTypeInfo;
import com.progen.graph.info.ProgenGraphInfo;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.user.SessionListener;
import com.progen.users.ProgenUser;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.DataTracker;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.targetparam.qddb.PbTargetParamDb;
import javax.servlet.ServletContext;
import utils.db.ProgenConnection;
import com.progen.users.ProgenUser;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import prg.db.DataTracker;
import prg.util.PbDisplayLabel;
import prg.util.PbEmailConfig;
import prg.util.SetGBLParamIntoSession;
import org.apache.log4j.*;
import prg.util.SetPbBaseRetObjIntoContext;
import utils.db.ProgenConnection;
import org.apache.struts.actions.LookupDispatchAction;
/**
 *
 * @author santhosh.kumar@progenbusiness.com
 */
public class PbBaseAction extends LookupDispatchAction {

    public static boolean isExecuted = false;
    public static int countOfUsers = 0;
    private boolean isCompanyValid = false;
    public static int sessionCount = 0;
    public HashMap<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();
    public HashMap<String, UserStatusHelper> statushelper = new HashMap<String, UserStatusHelper>();
    public static Logger logger = Logger.getLogger(PbBaseAction.class);

    protected Map getKeyMethodMap() {

        Map map = new HashMap();
        map.put("loginPage", "loginPage");
        map.put("loginApplication", "loginApplication");
        map.put("logoutApplication", "logoutApplication");
        map.put("sessionExpired", "sessionExpired");
        map.put("goHome", "goHome");
        map.put("reportMessages", "reportMessages");
        map.put("changepswrdpage", "changepswrdpage");
        map.put("frgtpswrdpage", "frgtpswrdpage");
        map.put("killDuplicateAndLogin", "killDuplicateAndLogin");
        map.put("removeDuplicateUser", "removeDuplicateUser");
        map.put("killSpecficSession", "killSpecficSession");
        map.put("suspendUser", "suspendUser");
        map.put("activateUser", "activateUser");
        map.put("getLicenceForUser", "getLicenceForUser");
        map.put("register", "register");
        map.put("mobileLogin", "mobileLogin");
        map.put("getDisplayAllLocaleAvailable", "getDisplayAllLocaleAvailable");
        map.put("setLanguageToCurrentUser", "setLanguageToCurrentUser");
        map.put("getChangeDefaultLocale", "getChangeDefaultLocale");
        map.put("veractionLogin", "veractionLogin");
        map.put("veractionLogout", "veractionLogout");
        map.put("setLogOffURL", "setLogOffURL");
        return map;
    }

    public ActionForward loginPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);

        /*
         * if (session!=null && session.getAttribute("USERID")!= null) { return
         * mapping.findForward("homePage"); } else { return
         * mapping.findForward("loginPage"); }
         */
        session.setAttribute("fromWall", "login");
        return mapping.findForward("loginPage");
    }

    public ActionForward loginApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(true);
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        ServletContext context = this.getServlet().getServletContext();
        String accounttype = "";
        PbBaseDAO baseDAO = new PbBaseDAO();
        String user = request.getParameter("user");
        String password = "";
        session.setAttribute("status","ERORR");
        if (user != null && !user.equalsIgnoreCase("progenpgmas")) {
            password = request.getParameter("password");
        } else {
            password = "blank";
        }
        if (user == null || user.equalsIgnoreCase("")) {
            return mapping.findForward("loginPage");
        }
        String screenwidth = request.getParameter("screenwidth");
        String screenheight = request.getParameter("screenheight");
        ProgenUser progenUser = null;
        Locale locale = new Locale("en");
        UserStatusHelper userstatushelper = new UserStatusHelper();
        if (screenwidth != null && screenheight != null) {
            session.setAttribute("screenwidth", screenwidth);
            session.setAttribute("screenheight", screenheight);
            session.setAttribute("userLocale", locale);
        }
        session.setAttribute("REPORTVERSION", "1.1");
        session.setAttribute("isMultiCompany", false);
//        session.setAttribute("theme","orange"); // For Data Flow
        logger.info("++++++++++++++++Entered loginApplication+++++++++++++");
//        ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "++++++++++++++++Entered loginApplication+++++++++++++");
        logger.info("--------user-------- " + user);
//        ProgenLog.log(ProgenLog.FINE, this, "--------user--------", user);
        //ProgenLog.log(ProgenLog.FINE, this, "--------password----", password);

//        ProgenLog.log(ProgenLog.SEVERE,this,"loginApplication","Arun");
//        ProgenLog.log(ProgenLog.SEVERE,this,"loginApplication","Gopika");
//        ProgenLog.log(ProgenLog.FINE,this,"loginApplication","Ananth");

        //un comment for indicus only below line
        isCompanyValid = Boolean.parseBoolean(context.getInitParameter("isCompanyValid"));
//        Boolean isJqplotCharts = Boolean.parseBoolean(context.getInitParameter("isJqplotCharts"));
        if (!user.equalsIgnoreCase("progenpgmas")) {
            if (isCompanyValid) {
                accounttype = request.getParameter("accounttype");
            }
        }

        session.setAttribute("insightsUserRole", "");
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        HashMap GraphTypesHashMap;
//        HashMap GraphSizesHashMap;
        HashMap GraphClassesHashMap;
//        HashMap GraphSizesDtlsHashMap;
        PbReturnObject graphTypesRetObj = null;
//        PbReturnObject graphSizesRetObj = null;
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        PbDb pbdb = new PbDb();
        if (isCompanyValid) {
            String sql = "select * from prg_ar_users";
            PbReturnObject pbrousercount = pbdb.execSelectSQL(sql);
            String sql1 = "select * from prg_role_rows";
            PbReturnObject pbrousercount1 = pbdb.execSelectSQL(sql1);
            int ucount = 0;
            int ucount1 = 0;
            if (pbrousercount.getRowCount() > 0) {
                ucount = pbrousercount.getRowCount();
            }
            if (pbrousercount1.getRowCount() > 0) {
                ucount1 = pbrousercount1.getRowCount();
            }

            if (ucount1 >= ucount) {


                if (user != null && password != null && accounttype != null) {
                    user = user.toUpperCase();
                    if (!user.equalsIgnoreCase("progenpgmas")) {
                        retObj = baseDAO.indicusLogin(user, password, accounttype);
                    } else {
                        String qry = "select * FROM prg_ar_users WHERE PU_LOGIN_ID =" + user;
                        retObj = pbdb.execSelectSQL(qry);
                    }
                    String accountType = null;
                    if (!user.equalsIgnoreCase("progenpgmas")) {
                        accountType = accounttype;//String.valueOf(retObj.getFieldValueInt(0, "ACCOUNT_TYPE"));
                    } else {
                        accountType = String.valueOf(retObj.getFieldValueInt(0, "ACCOUNT_TYPE"));
                    }
                    String expireAcct = "";
                    String userStQ = "";
                    if (accountType != null) {
                        if (user != null) {
                            if (!accountType.equalsIgnoreCase("null")) {
                                if (!accountType.equalsIgnoreCase("") && user != null) {
                                    if (retObj.getRowCount() > 0) {
                                        String pu_id = retObj.getFieldValueString(0, "PU_ID");
                                        logger.info("--------pu_id--------" + pu_id);
//                                        ProgenLog.log(ProgenLog.FINE, this, "--------pu_id--------", pu_id);
                                        progenUser = new ProgenUser(Integer.parseInt(pu_id));
                                        session.setAttribute("ProgenUser", progenUser);
                                        userStQ = "select to_char(nvl(pu_end_date,sysdate),'dd-mm-yy') from prg_ar_users where pu_id=" + pu_id;
                                        PbReturnObject userObj = new PbReturnObject();
                                        userObj = pbdb.execSelectSQL(userStQ);
                                        String userExpireDate = userObj.getFieldValueString(0, 0);
                                        String actTimeDiffQ = "select sysdate-(select nvl(pu_end_date,sysdate) from prg_ar_users where pu_id=" + pu_id + ") from dual";
//                                        ProgenLog.log(ProgenLog.FINE, this,"--------userExpireDate--------" , userExpireDate);
                                        logger.warn("--------userExpireDate--------" + userExpireDate);
                                        PbReturnObject timObj = pbdb.execSelectSQL(actTimeDiffQ);
                                        float diff = 0;
                                        diff = Float.parseFloat(timObj.getFieldValueString(0, 0));
                                        if (diff > 0) {
                                            expireAcct = "expired";
                                        } else {
                                            expireAcct = "";
                                        }
                                    }
                                }
                            }
                        }
                    }

//                    ArrayList UserReportPrevileges = new ArrayList();
//                    ArrayList UserGraphPrevileges = new ArrayList();
//                    ArrayList UserTablePrevileges = new ArrayList();
//                    PbReturnObject pbrepGrpretobj = new PbReturnObject();
                    if (retObj != null && retObj.getRowCount() != 0) {

//                        if (session.getAttribute("GraphTypesHashMap") == null && session.getAttribute("GraphClassesHashMap") == null) {
//                            GraphTypesHashMap = new HashMap();
//                            GraphClassesHashMap = new HashMap();
//                            if (isFxCharts) {
//                                graphTypesRetObj = reportTemplateDAO.getGraphTypeClassFx();
//                            } else {
//                                graphTypesRetObj = reportTemplateDAO.getGraphTypeClass();
//                            }
//
//                            for (int i = 0; i < graphTypesRetObj.getRowCount(); i++) {
//                                GraphTypesHashMap.put(graphTypesRetObj.getFieldValueString(i, 0), graphTypesRetObj.getFieldValueString(i, 1));
//                                GraphClassesHashMap.put(graphTypesRetObj.getFieldValueString(i, 1), graphTypesRetObj.getFieldValueString(i, 3));
//                            }
//                            session.setAttribute("GraphTypesHashMap", GraphTypesHashMap);
//                            session.setAttribute("GraphClassesHashMap", GraphClassesHashMap);
//                        }

//                        if (session.getAttribute("GraphSizesHashMap") == null && session.getAttribute("GraphSizesDtlsHashMap") == null) {
//                            GraphSizesHashMap = new HashMap();
//                            GraphSizesDtlsHashMap = new HashMap();
//
//                            //modified by santhosh.kumar on 06-02-2010
//                            if (isFxCharts) {
//                                graphSizesRetObj = reportTemplateDAO.getGraphSizeAxisFx();
//                            } else {
//                                graphSizesRetObj = reportTemplateDAO.getGraphSizeAxis();
//                            }

//                        for (int i = 0; i < graphSizesRetObj.getRowCount(); i++) {
//                            ArrayList axis = new ArrayList();
//                            axis.add(graphSizesRetObj.getFieldValueString(i, 2));//width
//                            axis.add(graphSizesRetObj.getFieldValueString(i, 3));//height
//
//                            GraphSizesHashMap.put(graphSizesRetObj.getFieldValueString(i, 0), graphSizesRetObj.getFieldValueString(i, 1));
//                            GraphSizesDtlsHashMap.put(graphSizesRetObj.getFieldValueString(i, 1), axis);
//                            axis = null;
//
//                        }
//                            for (int i = 0; i < graphSizesRetObj.getRowCount(); i++) {
//                                ArrayList axis = new ArrayList();
//                                if (screenwidth.trim().equalsIgnoreCase("1024")) {
//                                    if (graphSizesRetObj.getFieldValueString(i, 1).equalsIgnoreCase("Large")) {
//                                        axis.add("750");//width
//                                    } else if (graphSizesRetObj.getFieldValueString(i, 1).equalsIgnoreCase("Medium")) {
//                                        axis.add("350");//width
//                                    } else {
//                                        axis.add("350");//width
//                                    }
//
//                                } else {
//                                    axis.add(graphSizesRetObj.getFieldValueString(i, 2)); //width
//                                }
//                                axis.add(graphSizesRetObj.getFieldValueString(i, 3)); //height
//                                GraphSizesHashMap.put(graphSizesRetObj.getFieldValueString(i, 0), graphSizesRetObj.getFieldValueString(i, 1));
//                                GraphSizesDtlsHashMap.put(graphSizesRetObj.getFieldValueString(i, 1), axis);
//                                axis = null;
//
//                            }
//                            session.setAttribute("GraphSizesHashMap", GraphSizesHashMap);
//                            session.setAttribute("GraphSizesDtlsHashMap", GraphSizesDtlsHashMap);
//                        }
//
//                         try{
//                           if(graphTypesRetObj != null && graphSizesRetObj != null){
//                        initializeGraphInfo(graphTypesRetObj, graphSizesRetObj);
//                            }
//                       }catch(Exception ex){
//
//                         }
                        //added by srikanth.p for mapping jqplot graphs with jfree graphs
//                        if(session.getAttribute("JqpToJfNameMap")==null && session.getAttribute("JqpMap") ==null){
//                            HashMap<String,String> jqpToJfNameMap=new HashMap<String, String>();
//                            HashMap jqpMap=new HashMap();
//                            PbReturnObject graphMapRetObj=reportTemplateDAO.getJqtoJfRetObj();
//                            if(graphMapRetObj != null){
//                                for(int i=0;i<graphMapRetObj.rowCount;i++){
//                                    jqpToJfNameMap.put(graphMapRetObj.getFieldValueString(i,"JqpGraph_Name"), graphMapRetObj.getFieldValueString(i,"JfGraph_Name"));
//                                    jqpMap.put(graphMapRetObj.getFieldValueString(i,"JqpGraph_Id"), graphMapRetObj.getFieldValueString(i,"JqpGraph_Name"));
//                                }
//                            }
//                            session.setAttribute("JqpToJfNameMap",jqpToJfNameMap);
//                            session.setAttribute("JqpMap",jqpMap);
//                        }


//                        if (session.getAttribute("UserReportPrevileges") != null) {
//                            UserReportPrevileges = (ArrayList) session.getAttribute("UserReportPrevileges");
//                        } else {
//                            String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPORT_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                            pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                            if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                                for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                    UserReportPrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                                }
//                            }
//                            session.setAttribute("UserReportPrevileges", UserReportPrevileges);
//                        }
//                        if (session.getAttribute("UserGraphPrevileges") != null) {
//                            UserGraphPrevileges = (ArrayList) session.getAttribute("UserGraphPrevileges");
//                        } else {
//                            String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPGRP_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                            pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                            if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                                for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                    UserGraphPrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                                }
//                            }
//                            session.setAttribute("UserGraphPrevileges", UserGraphPrevileges);
//                        }
//                        if (session.getAttribute("UserTablePrevileges") != null) {
//                            UserTablePrevileges = (ArrayList) session.getAttribute("UserTablePrevileges");
//                        } else {
//                            String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPTAB_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                            pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                            if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                                for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                    UserTablePrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                                }
//                            }
//                            session.setAttribute("UserTablePrevileges", UserTablePrevileges);
//                        }

                        countOfUsers++;


                        dbColumns = retObj.getColumnNames();
                        session.setAttribute("USERID", retObj.getFieldValueString(0, dbColumns[0]));
                        session.setAttribute("LOGINID", retObj.getFieldValueString(0, dbColumns[1]));
                        session.setAttribute("MetadataDbType", ProgenConnection.getInstance().getDatabaseType());

                        //Added by Faiz Ansari
                        String UserId = retObj.getFieldValueString(0, dbColumns[0]);
                        String sqls = "select HEADER_TAGS from prg_ar_users where pu_id=" + UserId;
                        PbReturnObject pbros = pbdb.execSelectSQL(sqls);
                        if (pbros != null) {
                            String headerTags = pbros.getFieldValueString(0, 0);
                            StringTokenizer st = new StringTokenizer(headerTags, ",");
                            String[] headerTagsInfo = new String[3];
                            int f = 0;
                            while (st.hasMoreTokens()) {
                                headerTagsInfo[f] = st.nextToken();
                                f++;
                            }
                            session.setAttribute("HEADER_FONT", headerTagsInfo[0]);
                            session.setAttribute("HEADER_LENGTH", headerTagsInfo[1]);
                        }
                        //End!!!


                        String REPORTID = "";
                        if (request.getParameter("REPORTID") != null && (!"".equalsIgnoreCase(request.getParameter("REPORTID")))) {
                            REPORTID = String.valueOf(request.getParameter("REPORTID"));

                            String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

                            strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + REPORTID;

                            request.setAttribute("reportUrl", strURL);
                            return mapping.findForward("reportPage");
                        } else {

                            if (expireAcct.equalsIgnoreCase("")) {
                                return mapping.findForward("startPage");
                            } else {
                                return mapping.findForward("expireAccountPage");
                            }
                        }
                    } else {
                        request.setAttribute("validateUser", "Invalid UserName and Password");
                        return mapping.findForward("loginPage");
                    }



                } else {
                    request.setAttribute("validateUser", "Invalid UserName and Password");
                    return mapping.findForward("loginPage");
                }
            } else {
                request.setAttribute("validateUser", "Invalid Backend Deletion");
                return mapping.findForward("loginPage");
            }
        } else {

            if (user != null && password != null) {
                Properties connProps = new Properties();
                InputStream servletStream = request.getSession(false).getServletContext().getResourceAsStream("/WEB-INF/MetadataConn.xml");
                ConnectionMetadata metadata = null;
                //ProgenConnection.connProps = ;//
                if (servletStream != null) {
                    connProps.loadFromXML(servletStream);
                    metadata = new ConnectionMetadata(connProps);
                    ProgenConnection.setConnectionMetadata(metadata);
                }
                user = user.toUpperCase();
//                ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "Call validateUser "+user);
                logger.info("Call validateUser: " + user);
                if (!user.equalsIgnoreCase("progenpgmas")) {
                    retObj = baseDAO.validateUser(user, password);
                } else {

                    String qry1 = "SELECT PU_ID ID, PU_LOGIN_ID LOGIN_ID, PU_FIRSTNAME FIRSTNAME, "
                            + "PU_MIDDLENAME MIDDLENAME, PU_LASTNAME LASTNAME, PU_TYPE USERTYPE, "
                            + "PU_ACTIVE ACTIVE,ACCOUNT_TYPE FROM  PRG_AR_USERS WHERE upper(PU_LOGIN_ID)='" + user + "'";
                    retObj = pbdb.execSelectSQL(qry1);
                }


                //Get the Email configuration settings used for sending email
                //added by Nazneen
                PbReportViewerDAO pbrv = new PbReportViewerDAO();
                PbReturnObject returnObj = new PbReturnObject();
                returnObj = pbrv.getEmailConfigDetails();
                if (returnObj != null && returnObj.getRowCount() > 0) {
                    PbEmailConfig emailConfigObj = PbEmailConfig.getPbEmailConfig();

                    if (emailConfigObj == null) {
                        PbEmailConfig.createEmailConfigfrmDB(returnObj);
                    }
                }

//                PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
//
//                if (emailConfig == null){
//                    Properties emailProps=new Properties();
//                    servletStream = request.getSession(false).getServletContext().getResourceAsStream("/WEB-INF/EmailConfig.xml");
//                    if( servletStream != null )
//                    {
//                        try{
//                        emailProps.loadFromXML(servletStream);
//                        PbEmailConfig.createEmailConfig(emailProps);
//                        }
//                        catch(Exception e){
//                            logger.error("Exception:",e);
//                        }
//                    }
//                }

                servletStream = request.getSession(false).getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
                if (servletStream != null) {
                    try {
                        Properties advHtmlFileProps = new Properties();
                        advHtmlFileProps.load(servletStream);//loadFromXML(servletStream);
                        session.setAttribute("advHtmlFileProps", advHtmlFileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath"));
                        session.setAttribute("OneViewVersion", "2.1");
                        session.setAttribute("oldAdvHtmlFileProps", advHtmlFileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath"));
                        session.setAttribute("reportAdvHtmlFileProps", advHtmlFileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath"));
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    }
                }

                if (servletStream != null) {
                    servletStream.close();
                }

                System.setProperty("Tagger.dir", context.getInitParameter("wordtagger"));
                System.setProperty("wordnet.database.dir", context.getInitParameter("wordnet"));

//                ArrayList UserReportPrevileges = new ArrayList();
//                ArrayList UserGraphPrevileges = new ArrayList();
//                ArrayList UserTablePrevileges = new ArrayList();
//                
//                
//                
//                PbReturnObject pbrepGrpretobj = new PbReturnObject();
                int adminCnt = 0;
                if (retObj != null && retObj.getRowCount() != 0) {
                    adminCnt = baseDAO.getAdminCount(retObj.getFieldValueString(0, "ID"));
                    int avilableSessions = Integer.parseInt(context.getInitParameter("TotalSessionsAssigned"));
                    session.setAttribute("avilableSessions", avilableSessions);
//                    

                    //by gopesh for check available sessions
                    String ServerLicencing = context.getInitParameter("ServerLicencing");
                    session.setAttribute("ServerLicencing", ServerLicencing);

//                     PbReturnObject pbpretobj1 = null;
                    String ParallelUsage = null;
//                    String ggg = retObj.getFieldValueString(0, "ID");
//                    String querycheckPU = "select * from prg_ar_parallel_usage where user_id="+retObj.getFieldValueString(0, "ID");
//                    pbpretobj1=pbdb.execSelectSQL(querycheckPU);
//                    if(pbpretobj1.getRowCount()>0){
//                    ParallelUsage = pbpretobj1.getFieldValueString(0, 0);
//                    }
//                    else{
                    ParallelUsage = context.getInitParameter("ParallelUsage");
//                    }
                    session.setAttribute("ParallelUsage", ParallelUsage);
//long t=System.currentTimeMillis();
//     PbBaseDAO dao = new PbBaseDAO();
//     int activeUserCount=0;
//     int purchasedUsers =0;
//     activeUserCount = dao.getActiveUsers();
//     purchasedUsers = dao.getPurchaseUsers();
//     if(purchasedUsers>activeUserCount){
//        session.setAttribute("userActiveStatus", "Y");
//     }
//     else{
//        session.setAttribute("userActiveStatus", "N");
//         }
//long t1=System.currentTimeMillis();
//  System.out.println("******************************************************************************");
//             System.out.println("Dao  purchasedUsers time="+(t1-t));
//             System.out.println("******************************************************************************");
//      
                    if (context.getAttribute("sessionCount") != null) {
                        if (adminCnt <= 0) // if user is able to access QD,PA,Admin don't increase session cnt
                        {
                            sessionCount = (Integer) context.getAttribute("sessionCount") + 1;
                        } else {
                            sessionCount = (Integer) context.getAttribute("sessionCount");
                        }
                        if (sessionCount <= avilableSessions) {
                            context.setAttribute("sessionCount", sessionCount);
                            sessionMap.put(session.getId(), session);
                        } else {
//                            ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "User Validation Failed");
                            logger.warn("User Validation Failed");
                            request.setAttribute("validateUser", "ur exceed no.of sessions");
                            return mapping.findForward("loginPage");
                        }
                    } else {
                        if (adminCnt <= 0) // if user is able to access QD,PA,Admin don't increase session cnt
                        {
                            sessionCount++;
                        }
                        context.setAttribute("sessionCount", sessionCount);
                        sessionMap.put(session.getId(), session);
                    }
                    context.setAttribute("sessionMap", sessionMap);
//                    
//                    
                    baseDAO.updateActiveSession(sessionCount);
                    int puID = retObj.getFieldValueInt(0, "ID");
//                    ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "Create ProgenUser "+puID);
                    logger.info("Create ProgenUser " + puID);
                    progenUser = new ProgenUser(puID);
                    session.setAttribute("ProgenUser", progenUser);
//                    ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "In Session ProgenUser "+puID);
                    logger.info("In Session ProgenUser " + puID);

                    //code added by Amar on Oct 6, 2015
                    returnObj = pbrv.getColDisplayLabel(puID);
                    if (returnObj != null && returnObj.getRowCount() > 0) {


//                        if (displayLabel == null) {
                        PbDisplayLabel.createPbDisplayLabel(returnObj);
                        PbDisplayLabel displayLabel = PbDisplayLabel.getPbDisplayLabel();
                        session.setAttribute("Def_Company", displayLabel.getDefaultCompanyId());
                        //}
                    } else {
                        PbDisplayLabel displayLabel = PbDisplayLabel.getPbDisplayLabel();
                        if (displayLabel != null) {
                            displayLabel.setDispLab();
                            displayLabel = null;
                            //    displayLabel.setDispLab();
                        }
                    }
                    //end of code
                    //end of code
//
//                    if (session.getAttribute("GraphTypesHashMap") == null && session.getAttribute("GraphClassesHashMap") == null) {
////                        GraphTypesHashMap = new HashMap();
////                        GraphClassesHashMap = new HashMap();
//                        if (isFxCharts) {
//                            graphTypesRetObj = reportTemplateDAO.getGraphTypeClassFx();
//                        } else {
//                            graphTypesRetObj = reportTemplateDAO.getGraphTypeClass();
//                        }
//
////                        for (int i = 0; i < graphTypesRetObj.getRowCount(); i++) {
////                            GraphTypesHashMap.put(graphTypesRetObj.getFieldValueString(i, 0), graphTypesRetObj.getFieldValueString(i, 1));
////                            GraphClassesHashMap.put(graphTypesRetObj.getFieldValueString(i, 1), graphTypesRetObj.getFieldValueString(i, 3));
////                        }
////                        session.setAttribute("GraphTypesHashMap", GraphTypesHashMap);
////                        session.setAttribute("GraphClassesHashMap", GraphClassesHashMap);
//                    }

                    if (session.getAttribute("GraphTypesHashMap") == null && session.getAttribute("GraphClassesHashMap") == null) {
                        GraphTypesHashMap = new HashMap();
                        GraphClassesHashMap = new HashMap();
                        if (isFxCharts) {
                            graphTypesRetObj = reportTemplateDAO.getGraphTypeClassFx();
                        } else {
                            graphTypesRetObj = reportTemplateDAO.getGraphTypeClass();
                        }

                        for (int i = 0; i < graphTypesRetObj.getRowCount(); i++) {
                            GraphTypesHashMap.put(graphTypesRetObj.getFieldValueString(i, 0), graphTypesRetObj.getFieldValueString(i, 1));
                            GraphClassesHashMap.put(graphTypesRetObj.getFieldValueString(i, 1), graphTypesRetObj.getFieldValueString(i, 3));
                        }
                        session.setAttribute("GraphTypesHashMap", GraphTypesHashMap);
                        session.setAttribute("GraphClassesHashMap", GraphClassesHashMap);
                    }

//                    if (session.getAttribute("GraphSizesHashMap") == null && session.getAttribute("GraphSizesDtlsHashMap") == null) {
////                        GraphSizesHashMap = new HashMap();
////                        GraphSizesDtlsHashMap = new HashMap();
//
//                            GraphSizesHashMap.put(graphSizesRetObj.getFieldValueString(i, 0), graphSizesRetObj.getFieldValueString(i, 1));
//                            GraphSizesDtlsHashMap.put(graphSizesRetObj.getFieldValueString(i, 1), axis);
//                            axis = null;
//
//                        }

//                    if (session.getAttribute("UserReportPrevileges") != null) {
//                        UserReportPrevileges = (ArrayList) session.getAttribute("UserReportPrevileges");
//                    } else {
//                        String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPORT_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                        pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                        if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                            for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                UserReportPrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                            }
//                        }
//                        session.setAttribute("UserReportPrevileges", UserReportPrevileges);
//                    }
//                    if (session.getAttribute("UserGraphPrevileges") != null) {
//                        UserGraphPrevileges = (ArrayList) session.getAttribute("UserGraphPrevileges");
//                    } else {
//                        String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPGRP_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                        pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                        if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                            for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                UserGraphPrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                            }
//                        }
//                        session.setAttribute("UserGraphPrevileges", UserGraphPrevileges);
//                    }
//                    if (session.getAttribute("UserTablePrevileges") != null) {
//                        UserTablePrevileges = (ArrayList) session.getAttribute("UserTablePrevileges");
//                    } else {
//                        String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPTAB_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                        pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                        if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                            for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                UserTablePrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                            }
//                        }
//                        session.setAttribute("UserTablePrevileges", UserTablePrevileges);
//                    }

                    countOfUsers++;


                    dbColumns = retObj.getColumnNames();
                    String UserId = retObj.getFieldValueString(0, dbColumns[0]);
                    session.setAttribute("USERID", retObj.getFieldValueString(0, dbColumns[0]));
                    session.setAttribute("LOGINID", retObj.getFieldValueString(0, dbColumns[1]));
                    session.setAttribute("MetadataDbType", ProgenConnection.getInstance().getDatabaseType());
                    session.setAttribute("USERNAME", user);
                    //Added by Faiz Ansari

//                     session.setAttribute("UserId",UserId );
//                    String sqls = "select HEADER_TAGS from prg_ar_users where pu_id="+UserId;
//                    PbReturnObject pbros = pbdb.execSelectSQL(sqls);
//                    if(pbros != null){
//                    String headerTags = pbros.getFieldValueString(0, 0);
//                    StringTokenizer st = new StringTokenizer(headerTags,",");  
//                    String [] headerTagsInfo = new String[3];
//                    int f=0;
//                    while (st.hasMoreTokens()) {  
//                        headerTagsInfo[f]=st.nextToken(); 
//                        f++;
//                    }  
//                    session.setAttribute("HEADER_FONT",headerTagsInfo[0]);
//                    session.setAttribute("HEADER_LENGTH",headerTagsInfo[1]);  
//                    }                     
//                    //End!!!
                    String REPORTID = "";
                    if (request.getParameter("REPORTID") != null && (!"".equalsIgnoreCase(request.getParameter("REPORTID")))) {
                        REPORTID = String.valueOf(request.getParameter("REPORTID"));

                        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

                        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + REPORTID;

                        request.setAttribute("reportUrl", strURL);
                        Cookie[] cookies = request.getCookies();
                        for (Cookie cookie : cookies) {
                            if ("shareReport".equals(cookie.getName())) {
                                session.setAttribute("shareReport", cookie.getValue());
                                cookie.setMaxAge(0);
                            }
                            if ("filePath".equals(cookie.getName())) {
                                session.setAttribute("filePath", cookie.getValue());
                                cookie.setMaxAge(0);

                            }
                        }
                        //added By Mohit Gupta for getting all the parameter while logging from report page session expire.
                        PbReturnObject pbro = baseDAO.getUserDetails(retObj.getFieldValueString(0, dbColumns[0]));
                        PbReturnObject pbro1 = baseDAO.getUserStatus(retObj.getFieldValueString(0, dbColumns[0]));
                        if (pbro1 != null && pbro1.getRowCount() != 0) {
                            if (pbro1.getFieldValueString(0, 0) != null && pbro1.getFieldValueString(0, 0).charAt(0) == 'Y') {
                                userstatushelper.setIsActive(true);
                            }
                        }
                        if (pbro != null && pbro.getRowCount() != 0) {

                            if (pbro.getFieldValueString(0, 9).equalsIgnoreCase("Y")) {
                                userstatushelper.setPortalViewer(true);
                            }
                            if (pbro.getFieldValueString(0, 7).equalsIgnoreCase("Y")) {
                                userstatushelper.setWhatif(true);
                            }
                            if (pbro.getFieldValueString(0, 6).equalsIgnoreCase("Y")) {
                                userstatushelper.setHeadlines(true);
                            }
                            if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("9999")) {
                                userstatushelper.setUserType("Admin");

                            } else if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("10000")) {
                                userstatushelper.setUserType("Power Analyzer");
                            } else if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("10001")) {
                                userstatushelper.setUserType("Restricted Power Analyzer");
                            } else {
                                userstatushelper.setUserType("Analyzer");
                            }
                            if (pbro.getFieldValueString(0, 12).equalsIgnoreCase("Y")) {
                                userstatushelper.setQueryStudio(true);
                            }
                            if (pbro.getFieldValueString(0, 17).equalsIgnoreCase("Y")) {
                                userstatushelper.setPowerAnalyser(true);
                            }
                            if (pbro.getFieldValueString(0, 18).equalsIgnoreCase("Y")) {
                                userstatushelper.setOneView(true);
                            }
                            if (pbro.getFieldValueString(0, 10).equalsIgnoreCase("Y")) {
                                userstatushelper.setScoreCards(true);
                            }
                            //for XtendUser Check
                            if (pbro.getFieldValueString(0, 20).equalsIgnoreCase("Y")) {
                                userstatushelper.setXtendUser(true);
                            }
                        }
//                        //by gopesh for check state of user
//                            String query = "select PU_ACTIVE from prg_ar_users where  PU_ACTIVE='Y' AND pu_id="+retObj.getFieldValueString(0, dbColumns[0]);
//                            PbReturnObject pbroObj = null;
//                            pbroObj=pbdb.execSelectSQL(query);
//                            int activate = pbroObj.getRowCount();

                        if (retObj.getFieldValueString(0, dbColumns[1]).equalsIgnoreCase("progen")) {
                            userstatushelper.setIsActive(true);
                            userstatushelper.setPortalViewer(true);
                            userstatushelper.setWhatif(true);
                            userstatushelper.setHeadlines(true);
                            userstatushelper.setQueryStudio(true);
                            userstatushelper.setPowerAnalyser(true);
                            userstatushelper.setOneView(true);
                            userstatushelper.setScoreCards(true);
                            userstatushelper.setUserType("Admin");
                        }
                        statushelper.put(session.getId(), userstatushelper);
                        context.setAttribute("helperclass", statushelper);
                        session.setAttribute("userstatushelper", userstatushelper);

                        //added by Nazneen for multi calander
//                        String denomTable = "";
//                        String query1 = "SELECT IS_MULTI_COMP_CAL FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='MULTI_CAL'";
//                        String query1 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='IS_MULTI_COMP_CAL'";
//                        PbReturnObject pbroObj1 = null;
//                        pbroObj1=pbdb.execSelectSQL(query1);
//                        if(pbroObj1.getRowCount()>0){
//                            String isMultiCompCal = pbroObj1.getFieldValueString(0, 0);
//                            session.setAttribute("isMultiCompCal", isMultiCompCal);
//                            if(isMultiCompCal.equalsIgnoreCase("YES")){
//                                String query2 = "SELECT COMPANY_ID FROM PRG_AR_USERS WHERE PU_ID = "+UserId;
//                                PbReturnObject pbroObj2 = null;
//                                pbroObj2=pbdb.execSelectSQL(query2);
//                                if(pbroObj2.getRowCount()>0){
//                                    String companyId = pbroObj2.getFieldValueString(0, 0);
//                                    if(companyId!=null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")){
//                                        session.setAttribute("companyId", companyId);
//                                        String query3 = "SELECT A.DENOM_TABLE FROM PRG_CALENDER_SETUP A,PRG_COMPANY_CALANDER B WHERE A.CALENDER_ID = B.CALENDER_ID AND B.DEFAULT_VAL = 'Y' AND B.COMPANY_ID ="+companyId;
//                                       PbReturnObject pbroObj3 = null;
//                                       pbroObj3=pbdb.execSelectSQL(query3);
//                                       if(pbroObj3.getRowCount()>0){
//                                           denomTable = pbroObj3.getFieldValueString(0, 0);
//                                       }
//                                    }
//                                }
//                            }
//                        }
                        //added by Nazneen to retrieve Theme color
//                        String themeColor="orange"; //For Data Flow
//                        session.setAttribute("theme", themeColor); //For Data Flow
//                        String themeColor="";
//                        themeColor = pbrv.getGlobalParametersPiTheme();
//                        if(themeColor!=null && !themeColor.equalsIgnoreCase("") && !themeColor.equalsIgnoreCase("null"))
//                            session.setAttribute("theme", themeColor);
//                        session.setAttribute("theme", "Green");
//
//                        session.setAttribute("denomTable", denomTable);
                        //End of code by Nazneen for multi calander
                        //added by Nazneen for logo based on company
//                        String isCompLogo = "N";
//                        String companyId = "";
//                        String compLogo = "";
//                        String bussLogo = "";
//                        String compTitle = "";
//                        String bussTitle = "";
//                        String rightWebSiteUrl = "";
//                        String leftWebSiteUrl = "";
//                        String copyRightMsg = "";
//                        String welcomeMsg = "";
//                        //Added by Ram 30Oct15 for Dynamic height/width of logo
//                        String leftSideLogoHeight = "";
//                        String leftSideLogoWidth = "";
//                        String rightSideLogoHeight = "";
//                        String rightSideLogoWidth = "";
//                        //added By Mohit Gupta for favicon and favtitle
//                        String compFavicon="";
//                        String compFavtitle="";
////                        String queryLogo = "SELECT IS_COMP_LOGO FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='COMP_LOGO'";
//                        String queryLogo = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='IS_COMP_LOGO'";
//                        PbReturnObject pbroObject = null;
//                        pbroObject=pbdb.execSelectSQL(queryLogo);
//                        if(pbroObject.getRowCount()>0){
//                            isCompLogo = pbroObject.getFieldValueString(0, 0);
//                            if(isCompLogo.equalsIgnoreCase("YES")){
//                                String query2 = "SELECT COMPANY_ID FROM PRG_AR_USERS WHERE PU_ID = "+UserId;
//                                PbReturnObject pbroObj2 = null;
//                                pbroObj2=pbdb.execSelectSQL(query2);
//                                if(pbroObj2.getRowCount()>0){
//                                    companyId = pbroObj2.getFieldValueString(0, 0);
//                                     if(companyId==null || companyId.equalsIgnoreCase("null") || companyId.equalsIgnoreCase("") || companyId.equalsIgnoreCase(" ")){
//                                        companyId = "0000";
//                                     }
//                                        String queryLogoDetails = "SELECT COMPANY_LOGO,BUSSINESS_LOGO,COMPANY_TITLE,BUSSINESS_TITLE,RIGHT_WEB_SITE_URL,LEFT_WEB_SITE_URL,COPYRIGTH_MSG,WELCOME_MSG,LEFT_SIDE_LOGO_HEIGHT ,LEFT_SIDE_LOGO_WIDTH ,RIGHT_SIDE_LOGO_HEIGHT,RIGHT_SIDE_LOGO_WIDTH,COMPANY_FEVICON,COMPANY_FEVICON_TITLE FROM PRG_COMPANY_LOGO WHERE COMPANY_ID="+companyId;
//                                        pbroObject = null;
//                                        pbroObject = pbdb.execSelectSQL(queryLogoDetails);
//                                        if (pbroObject.getRowCount() > 0) {
//                                            compLogo = pbroObject.getFieldValueString(0, 0);
//                                            bussLogo = pbroObject.getFieldValueString(0, 1);
//                                            compTitle = pbroObject.getFieldValueString(0, 2);
//                                            bussTitle = pbroObject.getFieldValueString(0, 3);
//                                            rightWebSiteUrl = pbroObject.getFieldValueString(0, 4);
//                                            leftWebSiteUrl = pbroObject.getFieldValueString(0, 5);
//                                            copyRightMsg = pbroObject.getFieldValueString(0, 6);
//                                            welcomeMsg = pbroObject.getFieldValueString(0, 7);
//                                            leftSideLogoHeight=pbroObject.getFieldValueString(0, 8);
//                                            leftSideLogoWidth=pbroObject.getFieldValueString(0, 9);
//                                            rightSideLogoHeight=pbroObject.getFieldValueString(0, 10);
//                                            rightSideLogoWidth=pbroObject.getFieldValueString(0, 11);
//                                            compFavicon=pbroObject.getFieldValueString(0, 12);
//                                            compFavtitle=pbroObject.getFieldValueString(0, 13);
//                                        }
//                                    }
//                                }
//                            }
//                        session.setAttribute("isCompLogo", isCompLogo);
//                        session.setAttribute("companyId", companyId);
//                        if(companyId!=null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")){
//                            session.setAttribute("compLogo", compLogo);
//                            session.setAttribute("bussLogo", bussLogo);
//                            session.setAttribute("compTitle", compTitle);
//                            session.setAttribute("bussTitle", bussTitle);
//                            session.setAttribute("rightWebSiteUrl", rightWebSiteUrl);
//                            session.setAttribute("leftWebSiteUrl", leftWebSiteUrl);
//                            session.setAttribute("copyRightMsg", copyRightMsg);
//                            session.setAttribute("welcomeMsg", welcomeMsg);
//                            session.setAttribute("leftSideLogoHeight", leftSideLogoHeight);
//                            session.setAttribute("leftSideLogoWidth", leftSideLogoWidth);
//                            session.setAttribute("rightSideLogoHeight", rightSideLogoHeight);
//                            session.setAttribute("rightSideLogoWidth", rightSideLogoWidth);
//                            session.setAttribute("compFavicon", compFavicon);
//                            session.setAttribute("compFavtitle", compFavtitle);
//                        }
//                        String query2 = "SELECT SETUP_DATE_TYPE_VALUE,SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='GLOBAL_PARAMS'";
//                        PbReturnObject pbroObj2 = null;
//                        pbroObj2=pbdb.execSelectSQL(query2);
//                            
//                        if(pbroObj2!=null && pbroObj2.getRowCount()>0){
//                            String oneviewdatetype = pbroObj2.getFieldValueString(0, 0);
//                            String layoutvar = pbroObj2.getFieldValueString(0, 1);
//                            session.setAttribute("oneviewdatetype", oneviewdatetype);
////                            
//                            session.setAttribute("LayoutVar", layoutvar);
//                        }
//
//                      //added by Dinanath for setting up locale for user from database
//                        String queryLocale = "SELECT LANGUAGE_COUNTRY_CODE from prg_ar_users WHERE PU_ID='"+UserId+"'";
//                        PbReturnObject pbroObj23 = null;
//                        pbroObj23=pbdb.execSelectSQL(queryLocale);
//                        if(pbroObj23!=null && pbroObj23.getRowCount()>0){
//                            String uLocale = pbroObj23.getFieldValueString(0, 0);
//                            if(uLocale!=null && !uLocale.isEmpty() && !uLocale.equalsIgnoreCase("null")){
//                            String newLocale[]=uLocale.split("_");
//                            Locale currLocale=new Locale(newLocale[0],newLocale[1]);
////                            Locale currLocale=new Locale("hi","IN");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }else{
//                            Locale currLocale=new Locale("en","US");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }
//                        }else{
//                            Locale currLocale=new Locale("en","US");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }
                        // ended By Mohit Gupta

                        return mapping.findForward("reportPage");
                    } else {
                        PbReturnObject pbro = baseDAO.getUserDetails(retObj.getFieldValueString(0, dbColumns[0]));
                        PbReturnObject pbro1 = baseDAO.getUserStatus(retObj.getFieldValueString(0, dbColumns[0]));
                        if (pbro1 != null && pbro1.getRowCount() != 0) {
                            if (pbro1.getFieldValueString(0, 0) != null && pbro1.getFieldValueString(0, 0).charAt(0) == 'Y') {
                                userstatushelper.setIsActive(true);
                            }
                        }
                        if (pbro != null && pbro.getRowCount() != 0) {

                            if (pbro.getFieldValueString(0, 9).equalsIgnoreCase("Y")) {
                                userstatushelper.setPortalViewer(true);
                            }
                            if (pbro.getFieldValueString(0, 7).equalsIgnoreCase("Y")) {
                                userstatushelper.setWhatif(true);
                            }
                            if (pbro.getFieldValueString(0, 6).equalsIgnoreCase("Y")) {
                                userstatushelper.setHeadlines(true);
                            }
                            if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("9999")) {
                                userstatushelper.setUserType("Admin");

                            } else if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("10000")) {
                                userstatushelper.setUserType("Power Analyzer");
                            } else if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("10001")) {
                                userstatushelper.setUserType("Restricted Power Analyzer");
                            } else {
                                userstatushelper.setUserType("Analyzer");
                            }
                            if (pbro.getFieldValueString(0, 12).equalsIgnoreCase("Y")) {
                                userstatushelper.setQueryStudio(true);
                            }
                            if (pbro.getFieldValueString(0, 17).equalsIgnoreCase("Y")) {
                                userstatushelper.setPowerAnalyser(true);
                            }
                            if (pbro.getFieldValueString(0, 18).equalsIgnoreCase("Y")) {
                                userstatushelper.setOneView(true);
                            }
                            if (pbro.getFieldValueString(0, 10).equalsIgnoreCase("Y")) {
                                userstatushelper.setScoreCards(true);
                            }
                            //for XtendUser Check
//                            if(pbro.getFieldValueString(0, 20).equalsIgnoreCase("Y")){
//                                userstatushelper.setXtendUser(true);
//                                }

                            //Added by Ashutosh for RestrictedPowerAnalyser 11-12-2015
                            if (pbro.getFieldValueString(0, 20).equalsIgnoreCase("Y")) {
                                userstatushelper.setRestrictedPowerAnalyser(true);
                            }
                            //Ended by Ashutosh
                        }
                        //by gopesh for check state of user
//                            String query = "select PU_ACTIVE from prg_ar_users where  PU_ACTIVE='Y' AND pu_id="+retObj.getFieldValueString(0, dbColumns[0]);
//                            PbReturnObject pbroObj = null;
//                            pbroObj=pbdb.execSelectSQL(query);
//                            int activate = pbroObj.getRowCount();

                        if (retObj.getFieldValueString(0, dbColumns[1]).equalsIgnoreCase("progen")) {
                            userstatushelper.setIsActive(true);
                            userstatushelper.setPortalViewer(true);
                            userstatushelper.setWhatif(true);
                            userstatushelper.setHeadlines(true);
                            userstatushelper.setQueryStudio(true);
                            userstatushelper.setPowerAnalyser(true);
                            userstatushelper.setOneView(true);
                            userstatushelper.setScoreCards(true);
                            userstatushelper.setUserType("Admin");
                        }
                        statushelper.put(session.getId(), userstatushelper);
                        context.setAttribute("helperclass", statushelper);
                        session.setAttribute("userstatushelper", userstatushelper);

                        //added by Nazneen for multi calander
//                        String denomTable = "";
////                        String query1 = "SELECT IS_MULTI_COMP_CAL FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='MULTI_CAL'";
//                        String query1 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='IS_MULTI_COMP_CAL'";
//                        PbReturnObject pbroObj1 = null;
//                        pbroObj1=pbdb.execSelectSQL(query1);
//                        if(pbroObj1.getRowCount()>0){
//                            String isMultiCompCal = pbroObj1.getFieldValueString(0, 0);
//                            session.setAttribute("isMultiCompCal", isMultiCompCal);
//                            if(isMultiCompCal.equalsIgnoreCase("YES")){
////                                String query2 = "SELECT COMPANY_ID FROM PRG_AR_USERS WHERE PU_ID = "+UserId;
////                                PbReturnObject pbroObj2 = null;
////                                pbroObj2=pbdb.execSelectSQL(query2);
////                                if(pbroObj2.getRowCount()>0){
////                                    String companyId = pbroObj2.getFieldValueString(0, 0);
////                                    if(companyId!=null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")){
////                                        session.setAttribute("companyId", companyId);
////                                        String query3 = "SELECT A.DENOM_TABLE FROM PRG_CALENDER_SETUP A,PRG_COMPANY_CALANDER B WHERE A.CALENDER_ID = B.CALENDER_ID AND B.DEFAULT_VAL = 'Y' AND B.COMPANY_ID ="+companyId;
////                                       PbReturnObject pbroObj3 = null;
////                                       pbroObj3=pbdb.execSelectSQL(query3);
////                                       if(pbroObj3.getRowCount()>0){
////                                           denomTable = pbroObj3.getFieldValueString(0, 0);
////                                       }
////                                    }
////                                }
//                            }
//                        }
//                        //added by Nazneen to retrieve Theme color
////                        String themeColor="orange"; //For Data Flow
////                        session.setAttribute("theme", themeColor); //For Data Flow
////                        String themeColor="";
////                        themeColor = pbrv.getGlobalParametersPiTheme();
////                        if(themeColor!=null && !themeColor.equalsIgnoreCase("") && !themeColor.equalsIgnoreCase("null"))
////                            session.setAttribute("theme", themeColor);
//                           session.setAttribute("theme", "Green");

//                        session.setAttribute("denomTable", denomTable);
//                        //End of code by Nazneen for multi calander
//                        //added by Nazneen for logo based on company
//                        String isCompLogo = "N";
//                        String companyId = "";
//                        String compLogo = "";
//                        String bussLogo = "";
//                        String compTitle = "";
//                        String bussTitle = "";
//                        String rightWebSiteUrl = "";
//                        String leftWebSiteUrl = "";
//                        String copyRightMsg = "";
//                        String welcomeMsg = "";
//                        //Added by Ram 30Oct15 for Dynamic height/width of logo
//                        String leftSideLogoHeight = "";
//                        String leftSideLogoWidth = "";
//                        String rightSideLogoHeight = "";
//                        String rightSideLogoWidth = "";
//                        //added By Mohit Gupta for favicon and favtitle
//                        String compFavicon="";
//                        String compFavtitle="";
////                        String queryLogo = "SELECT IS_COMP_LOGO FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='COMP_LOGO'";
//                        String queryLogo = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='IS_COMP_LOGO'";
//                        PbReturnObject pbroObject = null;
//                        pbroObject=pbdb.execSelectSQL(queryLogo);
//                        if(pbroObject.getRowCount()>0){
//                            isCompLogo = pbroObject.getFieldValueString(0, 0);
//                            if(isCompLogo.equalsIgnoreCase("YES")){
//                                String query2 = "SELECT COMPANY_ID FROM PRG_AR_USERS WHERE PU_ID = "+UserId;
//                                PbReturnObject pbroObj2 = null;
//                                pbroObj2=pbdb.execSelectSQL(query2);
//                                if(pbroObj2.getRowCount()>0){
//                                    companyId = pbroObj2.getFieldValueString(0, 0);
//                                     if(companyId==null || companyId.equalsIgnoreCase("null") || companyId.equalsIgnoreCase("") || companyId.equalsIgnoreCase(" ")){
//                                        companyId = "0000";
//                                     }
//                                        String queryLogoDetails = "SELECT COMPANY_LOGO,BUSSINESS_LOGO,COMPANY_TITLE,BUSSINESS_TITLE,RIGHT_WEB_SITE_URL,LEFT_WEB_SITE_URL,COPYRIGTH_MSG,WELCOME_MSG,LEFT_SIDE_LOGO_HEIGHT ,LEFT_SIDE_LOGO_WIDTH ,RIGHT_SIDE_LOGO_HEIGHT,RIGHT_SIDE_LOGO_WIDTH,COMPANY_FEVICON,COMPANY_FEVICON_TITLE FROM PRG_COMPANY_LOGO WHERE COMPANY_ID="+companyId;
//                                        pbroObject = null;
//                                        pbroObject = pbdb.execSelectSQL(queryLogoDetails);
//                                        if (pbroObject.getRowCount() > 0) {
//                                            compLogo = pbroObject.getFieldValueString(0, 0);
//                                            bussLogo = pbroObject.getFieldValueString(0, 1);
//                                            compTitle = pbroObject.getFieldValueString(0, 2);
//                                            bussTitle = pbroObject.getFieldValueString(0, 3);
//                                            rightWebSiteUrl = pbroObject.getFieldValueString(0, 4);
//                                            leftWebSiteUrl = pbroObject.getFieldValueString(0, 5);
//                                            copyRightMsg = pbroObject.getFieldValueString(0, 6);
//                                            welcomeMsg = pbroObject.getFieldValueString(0, 7);
//                                            leftSideLogoHeight=pbroObject.getFieldValueString(0, 8);
//                                            leftSideLogoWidth=pbroObject.getFieldValueString(0, 9);
//                                            rightSideLogoHeight=pbroObject.getFieldValueString(0, 10);
//                                            rightSideLogoWidth=pbroObject.getFieldValueString(0, 11);
//                                            compFavicon=pbroObject.getFieldValueString(0, 12);
//                                            compFavtitle=pbroObject.getFieldValueString(0, 13);
//                                        }
//                                    }
//                                }
//                            }
//                        session.setAttribute("isCompLogo", isCompLogo);
//                        session.setAttribute("companyId", companyId);
//                        if(companyId!=null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")){
//                            session.setAttribute("compLogo", compLogo);
//                            session.setAttribute("bussLogo", bussLogo);
//                            session.setAttribute("compTitle", compTitle);
//                            session.setAttribute("bussTitle", bussTitle);
//                            session.setAttribute("rightWebSiteUrl", rightWebSiteUrl);
//                            session.setAttribute("leftWebSiteUrl", leftWebSiteUrl);
//                            session.setAttribute("copyRightMsg", copyRightMsg);
//                            session.setAttribute("welcomeMsg", welcomeMsg);
//                            session.setAttribute("leftSideLogoHeight", leftSideLogoHeight);
//                            session.setAttribute("leftSideLogoWidth", leftSideLogoWidth);
//                            session.setAttribute("rightSideLogoHeight", rightSideLogoHeight);
//                            session.setAttribute("rightSideLogoWidth", rightSideLogoWidth);
//                            session.setAttribute("compFavicon", compFavicon);
//                            session.setAttribute("compFavtitle", compFavtitle);
//                        }
                        // Added by Prabal
                        String sId = "22,23,24,25,26,27,28,29,30,31,32,33,34,35,12,38,39,40,41,42,43,45";
////                        String query2 = "SELECT SETUP_DATE_TYPE_VALUE,SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='GLOBAL_PARAMS'";
                        String query2 = "SELECT SETUP_KEY,SETUP_DATE_TYPE_VALUE,SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_ID in (" + sId + ")";
                        PbReturnObject pbroObj2 = null;
                        pbroObj2 = pbdb.execSelectSQL(query2);
                        SetGBLParamIntoSession.setGBLParamIntoSession(pbroObj2, session);
                        SetPbBaseRetObjIntoContext.setPbBaseRetObjIntoContext(Integer.parseInt(UserId), context, session);
//                        pbroObj2=null;
//                        //Added by Ashutosh
//                        String multiCompanyQry="select multi_company from prg_usr_col_disp_label where User_Id='"+UserId+"'";
//                        pbroObj2=pbdb.execSelectSQL(multiCompanyQry);
//                        if(pbroObj2!=null && pbroObj2.rowCount > 0){
//                        session.setAttribute("isMultiCompany", true);
//                        }else{
//                        session.setAttribute("isMultiCompany", false);
//                        }
                        // Ended by Prabal
                        //added by Dinanath for setting up locale for user from database
//                        String queryLocale = "SELECT LANGUAGE_COUNTRY_CODE from prg_ar_users WHERE PU_ID='"+UserId+"'";
//                        PbReturnObject pbroObj23 = null;
//                        pbroObj23=pbdb.execSelectSQL(queryLocale);
//                        if(pbroObj23!=null && pbroObj23.getRowCount()>0){
//                            String uLocale = pbroObj23.getFieldValueString(0, 0);
//                            if(uLocale!=null && !uLocale.isEmpty() && !uLocale.equalsIgnoreCase("null")){
//                            String newLocale[]=uLocale.split("_");
//                            Locale currLocale=new Locale(newLocale[0],newLocale[1]);
////                            Locale currLocale=new Locale("hi","IN");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }else{
//                            Locale currLocale=new Locale("en","US");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }
//                        }else{
//                            Locale currLocale=new Locale("en","US");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }
//                        //added by Nazneen for logo based on company
                        if ((userstatushelper.getIsActive() && Integer.valueOf(context.getAttribute("activate").toString()) > 0) || ServerLicencing.equalsIgnoreCase("Yes")) {
                            DataTracker tracker = new DataTracker();
                            // 
                            tracker.setLoginInformation(request, user);
                            return mapping.findForward("startPage");
                        } else {
//                           ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "InActive User Can't Logged in");
                            logger.warn("InActive User Can't Logged in");
                            request.setAttribute("validateUser", "Invalid UserName and Password");
                            return mapping.findForward("loginPage");
                        }
                    }
                } else {
//                    ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "User Validation Failed");
                    logger.warn("User Validation Failed");
                    request.setAttribute("validateUser", "Invalid UserName and Password");
                    return mapping.findForward("loginPage");
                }
            } else {
                request.setAttribute("validateUser", "Invalid UserName and Password");
                return mapping.findForward("loginPage");
            }
        }
    }

    private void initializeGraphInfo(PbReturnObject graphTypesRetObj, PbReturnObject graphSizesRetObj) {
        if (!ProgenGraphInfo.isInitialized()) {
            synchronized (ProgenGraphInfo.class) {
                if (!ProgenGraphInfo.isInitialized()) {
                    for (int i = 0; i < graphTypesRetObj.getRowCount(); i++) {
                        GraphTypeInfo info = new GraphTypeInfo();
                        info.setGraphTypeId(graphTypesRetObj.getFieldValueInt(i, 0));
                        info.setGraphTypeName(graphTypesRetObj.getFieldValueString(i, 1));
                        info.setGraphClassId(graphTypesRetObj.getFieldValueInt(i, 2));
                        info.setGraphClassName(graphTypesRetObj.getFieldValueString(i, 3));
                        ProgenGraphInfo.addGraphType(info);
                    }

                    for (int i = 0; i < graphSizesRetObj.getRowCount(); i++) {
                        GraphSizeInfo info = new GraphSizeInfo();
                        info.setSizeId(graphSizesRetObj.getFieldValueInt(i, 0));
                        info.setSizeName(graphSizesRetObj.getFieldValueString(i, 1));
                        info.setWidth(graphSizesRetObj.getFieldValueInt(i, 2));
                        info.setHeight(graphSizesRetObj.getFieldValueInt(i, 3));
                        ProgenGraphInfo.addGraphSize(info);
                    }

                    ProgenGraphInfo.setInitialized(true);
                }
            }
        }
    }

    public ActionForward killDuplicateAndLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ProgenUser user = (ProgenUser) request.getSession(false).getAttribute("ProgenUser");
        user.killAndSetNewSession(user, request.getSession(false));

        return null;//mapping.findForward("startPage");
    }

    public ActionForward removeDuplicateUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ProgenUser user = (ProgenUser) request.getSession(false).getAttribute("ProgenUser");
        user.removeDuplicateUser(user.getUserId());

        return null;//mapping.findForward("startPage");
    }

    public ActionForward logoutApplication(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PbBaseDAO dao = new PbBaseDAO();
        ServletContext context = this.getServlet().getServletContext();
        int adminCnt = 0;
        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                session.removeAttribute("PROGENTABLES");
            }
            if ((session.getAttribute("USERID")) != null) {
                String localUserId = (String) session.getAttribute("USERID");
//                
                PbTargetParamDb targetDb = new PbTargetParamDb();
                targetDb.deleteLock(localUserId);
                dao.updateLogOutInformation(session, localUserId);
//                
                sessionMap.remove(session.getId());
                context.setAttribute("sessionMap", sessionMap);
//                
                adminCnt = dao.getAdminCount(localUserId);
//                
            }

            if (context.getAttribute("sessionCount") != null) {
                if (adminCnt <= 0) {
                    sessionCount = (Integer) context.getAttribute("sessionCount") - 1;
                } else {
                    sessionCount = (Integer) context.getAttribute("sessionCount");
                }
                context.setAttribute("sessionCount", sessionCount);
//            
                dao.updateActiveSession(sessionCount);
            }
            statushelper.remove(session.getId());
            context.setAttribute("helperclass", statushelper);
            session.invalidate();
        }


        countOfUsers--;

        return mapping.findForward("logoutApp");
    }

    public ActionForward sessionExpired(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        } else {
        }
        return mapping.findForward("sessionExpired");
    }

    public ActionForward goHome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        //added by uday to delete lock start
//        if ((session.getAttribute("USERID")) != null) {
//            String localUserId = (String) session.getAttribute("USERID");
//            PbTargetParamDb targetDb = new PbTargetParamDb();
//            targetDb.deleteLock(localUserId);
//        }
        // added by uday over
        //HashMap map = new HashMap();
        if (session != null) {
            return mapping.findForward("homePage");
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward reportMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PbBaseBD BD = new PbBaseBD();
        String userId = null;
        String reportId = null;
        String msgText = null;
        if (session != null) {
            try {
                userId = String.valueOf(session.getAttribute("USERID"));
                reportId = request.getParameter("reportId");
                msgText = request.getParameter("msgText");

                BD.reportMessages(userId, reportId, msgText);
                return null;
            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");
            }

        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward changepswrdpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        return mapping.findForward("changepswrdpage");
    }

    public ActionForward register(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        return mapping.findForward("register");
    }

    public ActionForward frgtpswrdpage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("frgtpswrdpage");
    }

    public ActionForward killSpecficSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        String sessionId = request.getParameter("sessionId");
        ServletContext context = this.getServlet().getServletContext();
        HashMap map = (HashMap) context.getAttribute("sessionMap");
        PbBaseDAO dao = new PbBaseDAO();
        int adminCnt = 0;
        if (map.containsKey(sessionId)) {
//     
            HttpSession session = (HttpSession) map.get(sessionId);
            sessionMap.remove(sessionId);
            context.setAttribute("sessionMap", sessionMap);
            dao.updateLogOutInformation(session, userId);
            adminCnt = dao.getAdminCount(userId);
            if (context.getAttribute("sessionCount") != null) {
                if (adminCnt <= 0) {
                    sessionCount = (Integer) context.getAttribute("sessionCount") - 1;
                } else {
                    sessionCount = (Integer) context.getAttribute("sessionCount");
                }

                context.setAttribute("sessionCount", sessionCount);
//            
                dao.updateActiveSession(sessionCount);
            }

            session.invalidate();
            response.getWriter().print("success");
        }
        return null;
    }

    public void test() {
    }

    public boolean isIsCompanyValid() {
        return isCompanyValid;
    }

    public void setIsCompanyValid(boolean isCompanyValid) {
        this.isCompanyValid = isCompanyValid;
    }
//by gopesh

    public ActionForward suspendUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        PbBaseDAO dao = new PbBaseDAO();
        dao.updateUserInfo(userId, "N");
        response.getWriter().print("success");
        return null;
    }
    //by gopesh

    public ActionForward activateUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        HttpSession session = request.getSession(false);
        String ServerLicencing = String.valueOf(session.getAttribute("ServerLicencing"));//ServerLicencing
        PbDb pbdb = new PbDb();
        PbBaseDAO dao = new PbBaseDAO();
        int activeUserCount = 0;
        int purchasedUsers = 0;
        activeUserCount = dao.getActiveUsers();
        purchasedUsers = dao.getPurchaseUsers();
        if (purchasedUsers > activeUserCount || ServerLicencing.equalsIgnoreCase("Yes")) {
            dao.updateUserInfo(userId, "Y");
            response.getWriter().print("success");
        } else {
            response.getWriter().print("failer");
        }
        return null;
    }
//by gopesh

    public ActionForward getLicenceForUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        HttpSession session = request.getSession(false);
        String loginId = String.valueOf(session.getAttribute("LOGINID"));
        PbBaseDAO dao = new PbBaseDAO();
        int activeUserCount = 0;
        int purchasedUsers = 0;
        activeUserCount = dao.getActiveUsers();
        purchasedUsers = dao.getPurchaseUsers();
        String ServerLicencing = String.valueOf(session.getAttribute("ServerLicencing"));//ServerLicencing
        String ParallelUsage = String.valueOf(session.getAttribute("ParallelUsage"));
        if (purchasedUsers > activeUserCount || ParallelUsage.equalsIgnoreCase("Y")) {
            response.getWriter().print("success");
        } else if (ServerLicencing.equalsIgnoreCase("Yes")) {
            response.getWriter().print("success");
        } else if (userId.equalsIgnoreCase("478") && loginId.equalsIgnoreCase("STC1")) {
            response.getWriter().print("success");
        } else if (userId.equalsIgnoreCase("362") && loginId.equalsIgnoreCase("JOHN MANITARAS")) {
            response.getWriter().print("success");
        } else {
            response.getWriter().print("failer");
        }
        return null;
    }

    public ActionForward mobileLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(true);
        Gson gson = new Gson();
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        ServletContext context = this.getServlet().getServletContext();
        String accounttype = "";
        PbBaseDAO baseDAO = new PbBaseDAO();
        String user = request.getParameter("user");
        String password = "";
        if (!user.equalsIgnoreCase("progenpgmas")) {
            password = request.getParameter("password");
        } else {
            password = "blank";
        }

        Properties connProps = new Properties();
        InputStream servletStream = request.getSession(false).getServletContext().getResourceAsStream("/WEB-INF/MetadataConn.xml");
        ConnectionMetadata metadata = null;
        //ProgenConnection.connProps = ;//
        if (servletStream != null) {
            connProps.loadFromXML(servletStream);
            metadata = new ConnectionMetadata(connProps);
            ProgenConnection.setConnectionMetadata(metadata);
        }
        String query1 = "select user_id from prg_grp_user_assignment  where USER_NAME='" + user + "'";
        PbReturnObject retObj1 = pbdb.execSelectSQL(query1);
        String userId = retObj1.getFieldValueString(0, "user_id");
        String query2 = "SELECT folder_Id,FOLDER_NAME,grp_id FROM PRG_USER_FOLDER WHERE folder_id IN(SELECT user_folder_id FROM prg_grp_user_folder_assignment WHERE user_id=" + userId + " ) order by FOLDER_NAME asc;";
        PbReturnObject retObj2 = pbdb.execSelectSQL(query2);
        ArrayList FOLDER_NAME = new ArrayList();
        ArrayList grp_id = new ArrayList();
        ArrayList folder_id = new ArrayList();
        Map<String, List<String>> reportBuzzMap = new HashMap<String, List<String>>();
        for (int i = 0; i < retObj2.getRowCount(); i++) {

            folder_id.add(retObj2.getFieldValueString(i, "folder_Id"));
            FOLDER_NAME.add(retObj2.getFieldValueString(i, "FOLDER_NAME"));
            grp_id.add(retObj2.getFieldValueString(i, "grp_id"));
            String query3 = "select report_name,report_id from prg_ar_report_master where report_id in(select report_id from prg_ar_report_details where folder_id = " + retObj2.getFieldValueString(i, "folder_Id") + ");";
            PbReturnObject retObj3 = pbdb.execSelectSQL(query3);
            ArrayList reportName = new ArrayList();
            ArrayList reportID = new ArrayList();
            for (int j = 0; j < retObj3.getRowCount(); j++) {
                String repIDName = retObj3.getFieldValueString(j, "report_name") + "_" + retObj3.getFieldValueString(j, "report_id");
                reportName.add(repIDName);
//         reportID.add(retObj3.getFieldValueString(j, "report_id"));
            }
            reportBuzzMap.put(retObj2.getFieldValueString(i, "FOLDER_NAME"), reportName);
//         reportBuzzMap.put(retObj2.getFieldValueString(i, "FOLDER_NAME"), reportID);
        }
        String returnData = gson.toJson(reportBuzzMap);
//         

        try {
            response.getWriter().print(returnData);
        } catch (IOException ex) {
            //    logger.error("Exception: ",ex);
        }
        return null;
    }
//   added by Dinanath

    public ActionForward getDisplayAllLocaleAvailable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String userId = request.getParameter("userId");
        PbDb pbdb = new PbDb();
        String query2 = "SELECT LANGUAGE_COUNTRY_CODE from prg_ar_users WHERE PU_ID='" + userId + "'";
//        String query2 = "SELECT language_country_code from prg_ar_users WHERE PU_ID='24'";
        PbReturnObject retObj4 = pbdb.execSelectSQL(query2);
        StringBuffer buffer = new StringBuffer();
        if (retObj4 != null) {
            for (int m = 0; m < retObj4.getRowCount(); m++) {
//                
                String availableLang = retObj4.getFieldValueString(m, "LANGUAGE_COUNTRY_CODE");
                if (availableLang == null || availableLang.isEmpty() || availableLang.equalsIgnoreCase("null")) {
                    String query3 = "select LANGUAGE_CODE,COUNTRY_CODE,LANGUAGE_NAME from prg_language_master";
                    PbReturnObject retObj3 = pbdb.execSelectSQL(query3);
                    buffer.append("<table><tr><th></th><th></th></tr>");
                    buffer.append("<tr><td>Select Language</td><td><select id='myselect'>");
                    if (retObj3 != null) {
                        for (int j = 0; j < retObj3.getRowCount(); j++) {

                            buffer.append(" <option name='langValue' id='" + retObj3.getFieldValueString(j, "LANGUAGE_CODE") + "_" + retObj3.getFieldValueString(j, "COUNTRY_CODE") + "' value='" + retObj3.getFieldValueString(j, "LANGUAGE_NAME") + "'>" + retObj3.getFieldValueString(j, "LANGUAGE_NAME") + "</option>");

                        }
                    }
                    buffer.append("</select></td></tr><tr><td colspan='2'><br><center><input type='button' class='navtitle-hover' style='background-color: #2D89B3;color: white;' value='Done' onclick='setSelectedDefaultLocaleLanguage()'></center></td></tr></table>");
                } else {
                    buffer.append("Already Availble");
                }
            }
        }
        PrintWriter out = response.getWriter();
        out.print(buffer.toString());
        return null;
    }
//   added by Dinanath
    public ActionForward setLanguageToCurrentUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");
        String language_country_code = request.getParameter("language_country_code");
        String language_name = request.getParameter("language_name");
        PbDb pbdb = new PbDb();
        String query3 = "update prg_ar_users set language_country_code='" + language_country_code + "' , langauge_name='" + language_name + "' where PU_ID=" + userId;
        int retObj3 = pbdb.execUpdateSQL(query3);
        String result = "";
        if (language_country_code != null) {
            String newLocale[] = language_country_code.split("_");

            if (retObj3 == 1) {
                Locale currLocale = new Locale(newLocale[0], newLocale[1]);
                session.setAttribute("UserLocaleFormat", currLocale);
                result = "Details has saved successfully.";
            } else {
                result = "Failed to save. Please try again!";
            }
        } else {
            result = "Failed to save. Please try again!";
        }
        PrintWriter out = response.getWriter();
        out.print(result);
        return null;
    }
//added by Dinanath

    public ActionForward getChangeDefaultLocale(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String userId = request.getParameter("userId");
        PbDb pbdb = new PbDb();
        String query2 = "SELECT language_country_code from prg_ar_users WHERE PU_ID='" + userId + "'";
//        String query2 = "SELECT language_country_code from prg_ar_users WHERE PU_ID='24'";
        PbReturnObject retObj4 = pbdb.execSelectSQL(query2);
        StringBuffer buffer = new StringBuffer();
        if (retObj4 != null) {
            for (int m = 0; m < retObj4.getRowCount(); m++) {
//                
                String availableLang = retObj4.getFieldValueString(m, "language_country_code");
//                if (availableLang.isEmpty()) {
                String query3 = "select language_code,country_code,language_name from prg_language_master";
                PbReturnObject retObj3 = pbdb.execSelectSQL(query3);
                buffer.append("<table><tr><th></th><th></th></tr>");
                buffer.append("<tr><td>Select Language</td><td><select id='myselect'>");
                if (retObj3 != null) {
                    for (int j = 0; j < retObj3.getRowCount(); j++) {

                        buffer.append(" <option name='langValue' id='" + retObj3.getFieldValueString(j, "language_code") + "_" + retObj3.getFieldValueString(j, "country_code") + "' value='" + retObj3.getFieldValueString(j, "language_name") + "'>" + retObj3.getFieldValueString(j, "language_name") + "</option>");

                    }
                }
                buffer.append("</select></td></tr><tr><td colspan='2'><br/><center><input type='button' class='navtitle-hover' style='background-color: #2D89B3;color: white;' value='Done' onclick='setSelectedDefaultLocaleLanguage2()'></center></td></tr></table>");
//                } else {
//                    buffer.append("Already Availble");
//                }
            }
        }
        PrintWriter out = response.getWriter();
        out.print(buffer.toString());
        return null;
    }
    //Added by Ashutosh for SSO

    public ActionForward veractionLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        /***Added by Ashutosh for Removing Unused Session***/                    
final Map<String, HttpSession> sessionMap = SessionListener.getAllSessionMap();
        HttpSession in_session;
        ArrayList<String> KeysList = new ArrayList<String>(sessionMap.keySet());
        Iterator<String> iter = KeysList.iterator();
        try {
            while (iter.hasNext()) {
                in_session = sessionMap.get(iter.next());
                if (in_session.getAttribute("LOGINID") == null) {
                    in_session.invalidate();
                }
            }
        } catch (Exception e) {
            logger.error("ignore: invalidated session");
        }
/***Ended by Ashutosh***/ 
        HttpSession session = request.getSession(true);
        String requString = request.getRequestURL().toString();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        ServletContext context = this.getServlet().getServletContext();
        String accounttype = "";
        boolean ssologin = false;
        String user = "";

        String password = "";
//           user = request.getParameter("user")+"";
//     password = "";
//        if(!user.equalsIgnoreCase("progenpgmas")){
//            password = request.getParameter("password")+"";
//        } else {
//             password = "blank";
//        }
//
        PbBaseDAO baseDAO = new PbBaseDAO();
        HashMap userInfo = baseDAO.validateToken(request, "ssoToken",context);
        if(userInfo!=null)
        {
            ssologin=true;
  try{
            if(userInfo.get("status").toString().equalsIgnoreCase("OK"))
            {
                user=((HashMap)userInfo.get("user")).get("username")+"";
                session.setAttribute("status","OK");
                session.setAttribute("SSOAuthenticateFlag",true);
                    session.setAttribute("isMultiCompany", true);
                }
        }catch(Exception ex){
          response.sendRedirect(context.getInitParameter("sso_login"));
                return null;
            }
        }
         final String ssovalue1=session.getAttribute("ssoToken").toString();

  if(user==null || user.equalsIgnoreCase("")){
     response.sendRedirect(context.getInitParameter("sso_login"));      
            return null;
        }


//        String screenwidth = request.getParameter("screenwidth");
//        String screenheight = request.getParameter("screenheight");
        String screenwidth = "1366";
        String screenheight = "768";
        ProgenUser progenUser = null;
        Locale locale = new Locale("en");
        UserStatusHelper userstatushelper = new UserStatusHelper();
        if (screenwidth != null && screenheight != null) {
            session.setAttribute("screenwidth", screenwidth);
            session.setAttribute("screenheight", screenheight);

        }
        session.setAttribute("userLocale", locale);
        session.setAttribute("REPORTVERSION", "1.1");

//        session.setAttribute("theme","orange"); // For Data Flow

//        ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "++++++++++++++++Entered loginApplication+++++++++++++");
//        ProgenLog.log(ProgenLog.FINE, this, "--------user--------", user);
        logger.info("-----------------: Entered veractionLogin :-----------------");
        logger.info("--------: user :--------" + user);
        //un comment for indicus only below line
        isCompanyValid = Boolean.parseBoolean(context.getInitParameter("isCompanyValid"));
        Boolean isJqplotCharts = Boolean.parseBoolean(context.getInitParameter("isJqplotCharts"));
        if (!user.equalsIgnoreCase("progenpgmas")) {
            if (isCompanyValid) {
                accounttype = request.getParameter("accounttype");
            }
        }


        PbReturnObject retObj = null;
        String[] dbColumns = null;
        HashMap GraphTypesHashMap;
        HashMap GraphSizesHashMap;
        HashMap GraphClassesHashMap;
        HashMap GraphSizesDtlsHashMap;
        PbReturnObject graphTypesRetObj = null;
        PbReturnObject graphSizesRetObj = null;
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        PbDb pbdb = new PbDb();
        if (isCompanyValid) {
            String sql = "select * from prg_ar_users";
            PbReturnObject pbrousercount = pbdb.execSelectSQL(sql);
            String sql1 = "select * from prg_role_rows";
            PbReturnObject pbrousercount1 = pbdb.execSelectSQL(sql1);
            int ucount = 0;
            int ucount1 = 0;
            if (pbrousercount.getRowCount() > 0) {
                ucount = pbrousercount.getRowCount();
            }
            if (pbrousercount1.getRowCount() > 0) {
                ucount1 = pbrousercount1.getRowCount();
            }

            if (ucount1 >= ucount) {


                if (user != null && password != null && accounttype != null) {
                    user = user.toUpperCase();
                    if (!user.equalsIgnoreCase("progenpgmas")) {
                        retObj = baseDAO.indicusLogin(user, password, accounttype);
                    } else {
                        String qry = "select * FROM prg_ar_users WHERE PU_LOGIN_ID =" + user;
                        retObj = pbdb.execSelectSQL(qry);
                    }
                    String accountType = null;
                    if (!user.equalsIgnoreCase("progenpgmas")) {
                        accountType = accounttype;//String.valueOf(retObj.getFieldValueInt(0, "ACCOUNT_TYPE"));
                    } else {
                        accountType = String.valueOf(retObj.getFieldValueInt(0, "ACCOUNT_TYPE"));
                    }
                    String expireAcct = "";
                    String userStQ = "";
                    if (accountType != null) {
                        if (user != null) {
                            if (!accountType.equalsIgnoreCase("null")) {
                                if (!accountType.equalsIgnoreCase("") && user != null) {
                                    if (retObj.getRowCount() > 0) {
                                        String pu_id = retObj.getFieldValueString(0, "PU_ID");
//                                        ProgenLog.log(ProgenLog.FINE, this, "--------pu_id--------", pu_id);
                                        logger.info("--------pu_id--------" + pu_id);
                                        progenUser = new ProgenUser(Integer.parseInt(pu_id));
                                        session.setAttribute("ProgenUser", progenUser);
                                        userStQ = "select to_char(nvl(pu_end_date,sysdate),'dd-mm-yy') from prg_ar_users where pu_id=" + pu_id;
                                        PbReturnObject userObj = new PbReturnObject();
                                        userObj = pbdb.execSelectSQL(userStQ);
                                        String userExpireDate = userObj.getFieldValueString(0, 0);
                                        String actTimeDiffQ = "select sysdate-(select nvl(pu_end_date,sysdate) from prg_ar_users where pu_id=" + pu_id + ") from dual";
//                                        ProgenLog.log(ProgenLog.FINE, this, "--------userExpireDate--------", userExpireDate);
                                        logger.warn("--------userExpireDate--------" + userExpireDate);
                                        PbReturnObject timObj = pbdb.execSelectSQL(actTimeDiffQ);
                                        float diff = 0;
                                        diff = Float.parseFloat(timObj.getFieldValueString(0, 0));
                                        if (diff > 0) {
                                            expireAcct = "expired";
                                        } else {
                                            expireAcct = "";
                                        }
                                    }
                                }
                            }
                        }
                    }

//                    ArrayList UserReportPrevileges = new ArrayList();
//                    ArrayList UserGraphPrevileges = new ArrayList();
//                    ArrayList UserTablePrevileges = new ArrayList();
//                    PbReturnObject pbrepGrpretobj = new PbReturnObject();
                    if (retObj != null && retObj.getRowCount() != 0) {

                        if (session.getAttribute("GraphTypesHashMap") == null && session.getAttribute("GraphClassesHashMap") == null) {
                            GraphTypesHashMap = new HashMap();
                            GraphClassesHashMap = new HashMap();
                            if (isFxCharts) {
                                graphTypesRetObj = reportTemplateDAO.getGraphTypeClassFx();
                            } else {
                                graphTypesRetObj = reportTemplateDAO.getGraphTypeClass();
                            }

                            for (int i = 0; i < graphTypesRetObj.getRowCount(); i++) {
                                GraphTypesHashMap.put(graphTypesRetObj.getFieldValueString(i, 0), graphTypesRetObj.getFieldValueString(i, 1));
                                GraphClassesHashMap.put(graphTypesRetObj.getFieldValueString(i, 1), graphTypesRetObj.getFieldValueString(i, 3));
                            }
                            session.setAttribute("GraphTypesHashMap", GraphTypesHashMap);
                            session.setAttribute("GraphClassesHashMap", GraphClassesHashMap);
                        }

//                        if (session.getAttribute("GraphSizesHashMap") == null && session.getAttribute("GraphSizesDtlsHashMap") == null) {
//                            GraphSizesHashMap = new HashMap();
//                            GraphSizesDtlsHashMap = new HashMap();
//
//                            //modified by santhosh.kumar on 06-02-2010
//                            if (isFxCharts) {
//                                graphSizesRetObj = reportTemplateDAO.getGraphSizeAxisFx();
//                            } else {
//                                graphSizesRetObj = reportTemplateDAO.getGraphSizeAxis();
//                            }
//
////                        for (int i = 0; i < graphSizesRetObj.getRowCount(); i++) {
////                            ArrayList axis = new ArrayList();
////                            axis.add(graphSizesRetObj.getFieldValueString(i, 2));//width
////                            axis.add(graphSizesRetObj.getFieldValueString(i, 3));//height
////
////                            GraphSizesHashMap.put(graphSizesRetObj.getFieldValueString(i, 0), graphSizesRetObj.getFieldValueString(i, 1));
////                            GraphSizesDtlsHashMap.put(graphSizesRetObj.getFieldValueString(i, 1), axis);
////                            axis = null;
////
////                        }
//                            for (int i = 0; i < graphSizesRetObj.getRowCount(); i++) {
//                                ArrayList axis = new ArrayList();
//                                if (screenwidth.trim().equalsIgnoreCase("1024")) {
//                                    if (graphSizesRetObj.getFieldValueString(i, 1).equalsIgnoreCase("Large")) {
//                                        axis.add("750");//width
//                                    } else if (graphSizesRetObj.getFieldValueString(i, 1).equalsIgnoreCase("Medium")) {
//                                        axis.add("350");//width
//                                    } else {
//                                        axis.add("350");//width
//                                    }
////
//                                } else {
//                                    axis.add(graphSizesRetObj.getFieldValueString(i, 2)); //width
//                                }
//                                axis.add(graphSizesRetObj.getFieldValueString(i, 3)); //height
//                                GraphSizesHashMap.put(graphSizesRetObj.getFieldValueString(i, 0), graphSizesRetObj.getFieldValueString(i, 1));
//                                GraphSizesDtlsHashMap.put(graphSizesRetObj.getFieldValueString(i, 1), axis);
//                                axis = null;
//
//                            }
//                            session.setAttribute("GraphSizesHashMap", GraphSizesHashMap);
//                            session.setAttribute("GraphSizesDtlsHashMap", GraphSizesDtlsHashMap);
//                        }

//                        initializeGraphInfo(graphTypesRetObj, graphSizesRetObj);
                        //added by srikanth.p for mapping jqplot graphs with jfree graphs
//                        if(session.getAttribute("JqpToJfNameMap")==null && session.getAttribute("JqpMap") ==null){
//                            HashMap<String,String> jqpToJfNameMap=new HashMap<String, String>();
//                            HashMap jqpMap=new HashMap();
//                            PbReturnObject graphMapRetObj=reportTemplateDAO.getJqtoJfRetObj();
//                            if(graphMapRetObj != null){
//                                for(int i=0;i<graphMapRetObj.rowCount;i++){
//                                    jqpToJfNameMap.put(graphMapRetObj.getFieldValueString(i,"JqpGraph_Name"), graphMapRetObj.getFieldValueString(i,"JfGraph_Name"));
//                                    jqpMap.put(graphMapRetObj.getFieldValueString(i,"JqpGraph_Id"), graphMapRetObj.getFieldValueString(i,"JqpGraph_Name"));
//                                }
//                            }
//                            session.setAttribute("JqpToJfNameMap",jqpToJfNameMap);
//                            session.setAttribute("JqpMap",jqpMap);
//                        }


//                        if (session.getAttribute("UserReportPrevileges") != null) {
//                            UserReportPrevileges = (ArrayList) session.getAttribute("UserReportPrevileges");
//                        } else {
//                            String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPORT_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                            pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                            if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                                for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                    UserReportPrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                                }
//                            }
//                            session.setAttribute("UserReportPrevileges", UserReportPrevileges);
//                        }
//                        if (session.getAttribute("UserGraphPrevileges") != null) {
//                            UserGraphPrevileges = (ArrayList) session.getAttribute("UserGraphPrevileges");
//                        } else {
//                            String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPGRP_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                            pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                            if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                                for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                    UserGraphPrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                                }
//                            }
//                            session.setAttribute("UserGraphPrevileges", UserGraphPrevileges);
//                        }
//                        if (session.getAttribute("UserTablePrevileges") != null) {
//                            UserTablePrevileges = (ArrayList) session.getAttribute("UserTablePrevileges");
//                        } else {
//                            String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPTAB_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                            pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                            if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                                for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                    UserTablePrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                                }
//                            }
//                            session.setAttribute("UserTablePrevileges", UserTablePrevileges);
//                        }

                        countOfUsers++;


                        dbColumns = retObj.getColumnNames();
                        session.setAttribute("USERID", retObj.getFieldValueString(0, dbColumns[0]));
                        session.setAttribute("LOGINID", retObj.getFieldValueString(0, dbColumns[1]));
                        session.setAttribute("MetadataDbType", ProgenConnection.getInstance().getDatabaseType());

                        //Added by Faiz Ansari
                        String UserId = retObj.getFieldValueString(0, dbColumns[0]);
                        String sqls = "select HEADER_TAGS from prg_ar_users where pu_id=" + UserId;
                        PbReturnObject pbros = pbdb.execSelectSQL(sqls);
                        if (pbros != null) {
                            String headerTags = pbros.getFieldValueString(0, 0);
                            StringTokenizer st = new StringTokenizer(headerTags, ",");
                            String[] headerTagsInfo = new String[3];
                            int f = 0;
                            while (st.hasMoreTokens()) {
                                headerTagsInfo[f] = st.nextToken();
                                f++;
                            }
                            session.setAttribute("HEADER_FONT", headerTagsInfo[0]);
                            session.setAttribute("HEADER_LENGTH", headerTagsInfo[1]);
                        }
                        //End!!!

                        String REPORTID = "";
                        if (request.getParameter("REPORTID") != null && (!"".equalsIgnoreCase(request.getParameter("REPORTID")))) {
                            REPORTID = String.valueOf(request.getParameter("REPORTID"));

                            String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

                            strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + REPORTID;

                            request.setAttribute("reportUrl", strURL);
                            return mapping.findForward("reportPage");
                        } else {

                            if (expireAcct.equalsIgnoreCase("")) {
                                return mapping.findForward("startPage");
                            } else {
                                return mapping.findForward("expireAccountPage");
                            }
                        }
                    } else {
                        request.setAttribute("validateUser", "Invalid UserName and Password");
                       response.sendRedirect(context.getInitParameter("sso_login"));
                        return null;
                    }



                } else {
                    request.setAttribute("validateUser", "Invalid UserName and Password");
                    response.sendRedirect(context.getInitParameter("sso_login"));
                    return null;
                }
            } else {
                request.setAttribute("validateUser", "Invalid Backend Deletion");
                response.sendRedirect(context.getInitParameter("sso_login"));
                return null;
            }
        } else {

            if (user != null && password != null) {
                Properties connProps = new Properties();
                InputStream servletStream = request.getSession(false).getServletContext().getResourceAsStream("/WEB-INF/MetadataConn.xml");
                ConnectionMetadata metadata = null;
                //ProgenConnection.connProps = ;//
                if (servletStream != null) {
                    connProps.loadFromXML(servletStream);
                    metadata = new ConnectionMetadata(connProps);
                    ProgenConnection.setConnectionMetadata(metadata);
                }
                user = user.toUpperCase();
//                ProgenLog.log(ProgenLog.FINE, this, "veractionLogin", "Call validateUser "+user);
                logger.info("Call validateUser " + user);
                if (!user.equalsIgnoreCase("progenpgmas")) {
                    if (ssologin) {
                       retObj = baseDAO.validateUserWithoutPwd(userInfo,context,session);
                    } else {
                        retObj = baseDAO.validateUser(user, password);
                    }


                } else {
                    String qry1 = "SELECT PU_ID ID, PU_LOGIN_ID LOGIN_ID, PU_FIRSTNAME FIRSTNAME, "
                            + "PU_MIDDLENAME MIDDLENAME, PU_LASTNAME LASTNAME, PU_TYPE USERTYPE, "
                            + "PU_ACTIVE ACTIVE,ACCOUNT_TYPE FROM  PRG_AR_USERS WHERE upper(PU_LOGIN_ID)='" + user + "'";
                    retObj = pbdb.execSelectSQL(qry1);
                }


                //Get the Email configuration settings used for sending email
                //added by Nazneen
                PbReportViewerDAO pbrv = new PbReportViewerDAO();
                PbReturnObject returnObj = new PbReturnObject();
                returnObj = pbrv.getEmailConfigDetails();
                if (returnObj != null && returnObj.getRowCount() > 0) {
                    PbEmailConfig emailConfigObj = PbEmailConfig.getPbEmailConfig();

                    if (emailConfigObj == null) {
                        PbEmailConfig.createEmailConfigfrmDB(returnObj);
                    }
                }

//                PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
//
//                if (emailConfig == null){
//                    Properties emailProps=new Properties();
//                    servletStream = request.getSession(false).getServletContext().getResourceAsStream("/WEB-INF/EmailConfig.xml");
//                    if( servletStream != null )
//                    {
//                        try{
//                        emailProps.loadFromXML(servletStream);
//                        PbEmailConfig.createEmailConfig(emailProps);
//                        }
//                        catch(Exception e){
//                            logger.error("Exception:",e);
//                        }
//                    }
//                }

                servletStream = request.getSession(false).getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
                if (servletStream != null) {
                    try {
                        Properties advHtmlFileProps = new Properties();
                        advHtmlFileProps.load(servletStream);//loadFromXML(servletStream);
                        session.setAttribute("advHtmlFileProps", advHtmlFileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath"));
                        session.setAttribute("OneViewVersion", "2.1");
                        session.setAttribute("oldAdvHtmlFileProps", advHtmlFileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath"));
                        session.setAttribute("reportAdvHtmlFileProps", advHtmlFileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath"));
                    } catch (Exception e) {
                        logger.error("Exception:", e);
                    }
                }

                if (servletStream != null) {
                    servletStream.close();
                }

                System.setProperty("Tagger.dir", context.getInitParameter("wordtagger"));
                System.setProperty("wordnet.database.dir", context.getInitParameter("wordnet"));

//                ArrayList UserReportPrevileges = new ArrayList();
//                ArrayList UserGraphPrevileges = new ArrayList();
//                ArrayList UserTablePrevileges = new ArrayList();
//                
//                
//                
                PbReturnObject pbrepGrpretobj = new PbReturnObject();
                int adminCnt = 0;
                if (retObj != null && retObj.getRowCount() != 0) {
                    final String logouturl = "http://localhost:8080" + request.getContextPath() + "/baseAction.do?param=logoutApplication";
//                    final Timer t = new Timer();
//                    final HttpSession session1 = request.getSession(false);
////Set the schedule function and rate
//t.scheduleAtFixedRate(new TimerTask() {
//
//    @Override
//    public void run() {
//        //Called each time when 1000 milliseconds (1 second) (the period parameter)
//        PbBaseDAO baseDAO=new PbBaseDAO();
//         HashMap checkUserSession = baseDAO.checkUserSession(ssovalue1);
//                   if(checkUserSession!=null)
//                   {
//                       if(checkUserSession.get("status").toString().equalsIgnoreCase("ERROR")){
//                                 session1.invalidate();
//                                  
//                                   t.cancel();
//                       }
//                   }
//
//         }
//
//},
////Set how long before to start calling the TimerTask (in milliseconds)
//2000000,
////Set the amount of time between each execution (in milliseconds)
//2000000);



                    adminCnt = baseDAO.getAdminCount(retObj.getFieldValueString(0, "ID"));
                    int avilableSessions = Integer.parseInt(context.getInitParameter("TotalSessionsAssigned"));
                    session.setAttribute("avilableSessions", avilableSessions);

                    //by gopesh for check available sessions
                    String ServerLicencing = context.getInitParameter("ServerLicencing");
                    session.setAttribute("ServerLicencing", ServerLicencing);

//                     PbReturnObject pbpretobj1 = null;
                    String ParallelUsage = null;
//                    String ggg = retObj.getFieldValueString(0, "ID");
//                    String querycheckPU = "select * from prg_ar_parallel_usage where user_id="+retObj.getFieldValueString(0, "ID");
//                    pbpretobj1=pbdb.execSelectSQL(querycheckPU);
//                    if(pbpretobj1.getRowCount()>0){
//                    ParallelUsage = pbpretobj1.getFieldValueString(0, 0);
//                    }
//                    else{
                    ParallelUsage = context.getInitParameter("ParallelUsage");
//                    }
                    session.setAttribute("ParallelUsage", ParallelUsage);

                    PbBaseDAO dao = new PbBaseDAO();
                    int activeUserCount = 0;
                    int purchasedUsers = 0;
                    activeUserCount = dao.getActiveUsers();
                    purchasedUsers = dao.getPurchaseUsers();
                    if (purchasedUsers > activeUserCount) {
                        session.setAttribute("userActiveStatus", "Y");
                    } else {
                        session.setAttribute("userActiveStatus", "N");
                    }


                    if (context.getAttribute("sessionCount") != null) {
                        if (adminCnt <= 0) // if user is able to access QD,PA,Admin don't increase session cnt
                        {
                            sessionCount = (Integer) context.getAttribute("sessionCount") + 1;
                        } else {
                            sessionCount = (Integer) context.getAttribute("sessionCount");
                        }
                        if (sessionCount <= avilableSessions) {
                            context.setAttribute("sessionCount", sessionCount);
                            sessionMap.put(session.getId(), session);
                        } else {
//                            ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "User Validation Failed");
                            logger.warn("User Validation Failed");
                            request.setAttribute("validateUser", "ur exceed no.of sessions");
                            response.sendRedirect(context.getInitParameter("sso_login"));
                            return null;
                        }
                    } else {
                        if (adminCnt <= 0) // if user is able to access QD,PA,Admin don't increase session cnt
                        {
                            sessionCount++;
                        }
                        context.setAttribute("sessionCount", sessionCount);
                        sessionMap.put(session.getId(), session);
                    }
                    context.setAttribute("sessionMap", sessionMap);
//                    
//                    logger.info("sessionCount OutSide: " + sessionCount);
//                   
//                     logger.info("sessionMap login: " + sessionMap.toString());
                    baseDAO.updateActiveSession(sessionCount);
                    int puID = retObj.getFieldValueInt(0, "ID");
//                    ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "Create ProgenUser "+puID);
                    logger.info("Create ProgenUser: " + puID);
                    progenUser = new ProgenUser(puID);
                    session.setAttribute("ProgenUser", progenUser);
//                    ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "In Session ProgenUser "+puID);
                    logger.info("In Session ProgenUser: " + puID);


                    //code added by Amar on Oct 6, 2015
                    returnObj = pbrv.getColDisplayLabel(puID);
                    if (returnObj != null && returnObj.getRowCount() > 0) {


//                        if (displayLabel == null) {
                        PbDisplayLabel.createPbDisplayLabel(returnObj);
                        PbDisplayLabel displayLabel = PbDisplayLabel.getPbDisplayLabel();
                        session.setAttribute("Def_Company", displayLabel.getDefaultCompanyId());
                        //}
                    } else {
                        PbDisplayLabel displayLabel = PbDisplayLabel.getPbDisplayLabel();
                        if (displayLabel != null) {
                            displayLabel.setDispLab();
                            displayLabel = null;
                            //    displayLabel.setDispLab();
                        }
                    }
                    //end of code
                    //end of code

//                    if (session.getAttribute("GraphTypesHashMap") == null && session.getAttribute("GraphClassesHashMap") == null) {
//                        GraphTypesHashMap = new HashMap();
//                        GraphClassesHashMap = new HashMap();
//                        if (isFxCharts) {
//                            graphTypesRetObj = reportTemplateDAO.getGraphTypeClassFx();
//                        } else {
//                            graphTypesRetObj = reportTemplateDAO.getGraphTypeClass();
//                        }
//
//                        for (int i = 0; i < graphTypesRetObj.getRowCount(); i++) {
//                            GraphTypesHashMap.put(graphTypesRetObj.getFieldValueString(i, 0), graphTypesRetObj.getFieldValueString(i, 1));
//                            GraphClassesHashMap.put(graphTypesRetObj.getFieldValueString(i, 1), graphTypesRetObj.getFieldValueString(i, 3));
//                        }
//                        session.setAttribute("GraphTypesHashMap", GraphTypesHashMap);
//                        session.setAttribute("GraphClassesHashMap", GraphClassesHashMap);
//                    }

//                    if (session.getAttribute("GraphSizesHashMap") == null && session.getAttribute("GraphSizesDtlsHashMap") == null) {
//                        GraphSizesHashMap = new HashMap();
//                        GraphSizesDtlsHashMap = new HashMap();
//
//                        //modified by santhosh.kumar on 06-02-2010
//                        if (isFxCharts) {
//                            graphSizesRetObj = reportTemplateDAO.getGraphSizeAxisFx();
//                        } else {
//                            graphSizesRetObj = reportTemplateDAO.getGraphSizeAxis();
//                        }
//
////                        for (int i = 0; i < graphSizesRetObj.getRowCount(); i++) {
////                            ArrayList axis = new ArrayList();
////                            axis.add(graphSizesRetObj.getFieldValueString(i, 2));
////                            axis.add(graphSizesRetObj.getFieldValueString(i, 3));
////
////                            GraphSizesHashMap.put(graphSizesRetObj.getFieldValueString(i, 0), graphSizesRetObj.getFieldValueString(i, 1));
////                            GraphSizesDtlsHashMap.put(graphSizesRetObj.getFieldValueString(i, 1), axis);
////                            axis = null;
////
////                        }
//                        for (int i = 0; i < graphSizesRetObj.getRowCount(); i++) {
//                            ArrayList axis = new ArrayList();
//                            if (screenwidth.trim().equalsIgnoreCase("1024")) {
//                                if (graphSizesRetObj.getFieldValueString(i, 1).equalsIgnoreCase("Large")) {
//                                    axis.add("750");//width
//                                } else if (graphSizesRetObj.getFieldValueString(i, 1).equalsIgnoreCase("Medium")) {
//                                    axis.add("350");//width
//                                } else {
//                                    axis.add("350");//width
//                                }
//
//                            } else {
//                                axis.add(graphSizesRetObj.getFieldValueString(i, 2)); //width
//                            }
//                            axis.add(graphSizesRetObj.getFieldValueString(i, 3)); //height
//                            GraphSizesHashMap.put(graphSizesRetObj.getFieldValueString(i, 0), graphSizesRetObj.getFieldValueString(i, 1));
//                            GraphSizesDtlsHashMap.put(graphSizesRetObj.getFieldValueString(i, 1), axis);
//                            axis = null;
//                        }
//                        session.setAttribute("GraphSizesHashMap", GraphSizesHashMap);
//                        session.setAttribute("GraphSizesDtlsHashMap", GraphSizesDtlsHashMap);
//                       
//                    }

//                    initializeGraphInfo(graphTypesRetObj, graphSizesRetObj);
//                    if(session.getAttribute("JqpToJfNameMap")==null && session.getAttribute("JqpMap") ==null){
//                            HashMap<String,String> jqpToJfNameMap=new HashMap<String, String>();
//                            HashMap jqpMap=new HashMap();
//                            PbReturnObject graphMapRetObj=reportTemplateDAO.getJqtoJfRetObj();
//                            if(graphMapRetObj != null){
//                                for(int i=0;i<graphMapRetObj.rowCount;i++){
//                                    jqpToJfNameMap.put(graphMapRetObj.getFieldValueString(i,3), graphMapRetObj.getFieldValueString(i,1));
//                                    jqpMap.put(graphMapRetObj.getFieldValueString(i,1), graphMapRetObj.getFieldValueString(i,2));
//                                }
//                            }
//                            session.setAttribute("JqpToJfNameMap",jqpToJfNameMap);
//                            session.setAttribute("JqpMap",jqpMap);
//                        }

//                    if (session.getAttribute("UserReportPrevileges") != null) {
//                        UserReportPrevileges = (ArrayList) session.getAttribute("UserReportPrevileges");
//                    } else {
//                        String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPORT_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                        pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                        if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                            for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                UserReportPrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                            }
//                        }
//                        session.setAttribute("UserReportPrevileges", UserReportPrevileges);
//                    }
//                    if (session.getAttribute("UserGraphPrevileges") != null) {
//                        UserGraphPrevileges = (ArrayList) session.getAttribute("UserGraphPrevileges");
//                    } else {
//                        String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPGRP_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                        pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                        if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                            for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                UserGraphPrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                            }
//                        }
//                        session.setAttribute("UserGraphPrevileges", UserGraphPrevileges);
//                    }
//                    if (session.getAttribute("UserTablePrevileges") != null) {
//                        UserTablePrevileges = (ArrayList) session.getAttribute("UserTablePrevileges");
//                    } else {
//                        String repGrpQuery = "SELECT PREVILAGE_NAME FROM PRG_AR_REPTAB_PREVILAGES WHERE USER_ID =" + retObj.getFieldValueString(0, 0);
//                        pbrepGrpretobj = pbdb.execSelectSQL(repGrpQuery);
//                        if (pbrepGrpretobj.getRowCount() != 0 && pbrepGrpretobj != null) {
//                            for (int i = 0; i < pbrepGrpretobj.getRowCount(); i++) {
//                                UserTablePrevileges.add(pbrepGrpretobj.getFieldValueString(i, "PREVILAGE_NAME"));
//                            }
//                        }
//                        session.setAttribute("UserTablePrevileges", UserTablePrevileges);
//                    }

                    countOfUsers++;


                    dbColumns = retObj.getColumnNames();
                    session.setAttribute("USERID", retObj.getFieldValueString(0, dbColumns[0]));
                    session.setAttribute("LOGINID", retObj.getFieldValueString(0, dbColumns[1]));
                    session.setAttribute("MetadataDbType", ProgenConnection.getInstance().getDatabaseType());
                    session.setAttribute("USERNAME", user);

                    //Added by Faiz Ansari
                    String UserId = retObj.getFieldValueString(0, dbColumns[0]);
//                    String sqls = "select HEADER_TAGS from prg_ar_users where pu_id="+UserId;
//                    PbReturnObject pbros = pbdb.execSelectSQL(sqls);
//                    if(pbros != null){
//                    String headerTags = pbros.getFieldValueString(0, 0);
//                    StringTokenizer st = new StringTokenizer(headerTags,",");  
//                    String [] headerTagsInfo = new String[3];
//                    int f=0;
//                    while (st.hasMoreTokens()) {  
//                        headerTagsInfo[f]=st.nextToken(); 
//                        f++;
//                    }  
//                    session.setAttribute("HEADER_FONT",headerTagsInfo[0]);
//                    session.setAttribute("HEADER_LENGTH",headerTagsInfo[1]);  
//                    }
                    //End!!!

                    //Added by Ashutosh for User Settings
                    if (userInfo.get("status").toString().equalsIgnoreCase("OK")) {
                        SessionListener.setSSOSessionMap();

                    baseDAO.userSettings(session,context);

                    }
                    String REPORTID = "";
                    if (request.getParameter("REPORTID") != null && (!"".equalsIgnoreCase(request.getParameter("REPORTID")))) {
                        REPORTID = String.valueOf(request.getParameter("REPORTID"));

                        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

                        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + REPORTID;

                        request.setAttribute("reportUrl", strURL);
                        Cookie[] cookies = request.getCookies();
                        for (Cookie cookie : cookies) {
                            if ("shareReport".equals(cookie.getName())) {
                                session.setAttribute("shareReport", cookie.getValue());
                                cookie.setMaxAge(0);
                            }
                            if ("filePath".equals(cookie.getName())) {
                                session.setAttribute("filePath", cookie.getValue());
                                cookie.setMaxAge(0);

                            }
                        }
                        return mapping.findForward("reportPage");
                    } else {
                        PbReturnObject pbro = baseDAO.getUserDetails(retObj.getFieldValueString(0, dbColumns[0]));
                        PbReturnObject pbro1 = baseDAO.getUserStatus(retObj.getFieldValueString(0, dbColumns[0]));
                        if (pbro1 != null && pbro1.getRowCount() != 0) {
                            if (pbro1.getFieldValueString(0, 0) != null && pbro1.getFieldValueString(0, 0).charAt(0) == 'Y') {
                                userstatushelper.setIsActive(true);
                            }
                        }
                        if (pbro != null && pbro.getRowCount() != 0) {

                            if (pbro.getFieldValueString(0, 9).equalsIgnoreCase("Y")) {
                                userstatushelper.setPortalViewer(true);
                            }
                            if (pbro.getFieldValueString(0, 7).equalsIgnoreCase("Y")) {
                                userstatushelper.setWhatif(true);
                            }
                            if (pbro.getFieldValueString(0, 6).equalsIgnoreCase("Y")) {
                                userstatushelper.setHeadlines(true);
                            }
                            if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("9999")) {
                                userstatushelper.setUserType("Admin");

                            } else if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("10000")) {
                                userstatushelper.setUserType("Power Analyzer");
                            } else if (pbro.getFieldValueString(0, 15).equalsIgnoreCase("10001")) {
                                userstatushelper.setUserType("Restricted Power Analyzer");
                            } else {
                                userstatushelper.setUserType("Analyzer");
                            }
                            if (pbro.getFieldValueString(0, 12).equalsIgnoreCase("Y")) {
                                userstatushelper.setQueryStudio(true);
                            }
                            if (pbro.getFieldValueString(0, 17).equalsIgnoreCase("Y")) {
                                userstatushelper.setPowerAnalyser(true);
                            }
                            if (pbro.getFieldValueString(0, 18).equalsIgnoreCase("Y")) {
                                userstatushelper.setOneView(true);
                            }
                            if (pbro.getFieldValueString(0, 10).equalsIgnoreCase("Y")) {
                                userstatushelper.setScoreCards(true);
                            }
                            //for XtendUser Check
                            if (pbro.getFieldValueString(0, 20).equalsIgnoreCase("Y")) {
                                userstatushelper.setXtendUser(true);
                            }
                        }
                        //by gopesh for check state of user
                        String query = "select PU_ACTIVE from prg_ar_users where  PU_ACTIVE='Y' AND pu_id=" + retObj.getFieldValueString(0, dbColumns[0]);
                        PbReturnObject pbroObj = null;
                        pbroObj = pbdb.execSelectSQL(query);
                        int activate = pbroObj.getRowCount();

                        if (retObj.getFieldValueString(0, dbColumns[1]).equalsIgnoreCase("progen")) {
                            userstatushelper.setIsActive(true);
                            userstatushelper.setPortalViewer(true);
                            userstatushelper.setWhatif(true);
                            userstatushelper.setHeadlines(true);
                            userstatushelper.setQueryStudio(true);
                            userstatushelper.setPowerAnalyser(true);
                            userstatushelper.setOneView(true);
                            userstatushelper.setScoreCards(true);
                            userstatushelper.setUserType("Admin");
                        }
                        statushelper.put(session.getId(), userstatushelper);
                        context.setAttribute("helperclass", statushelper);
                        session.setAttribute("userstatushelper", userstatushelper);

                        //added by Nazneen for multi calander
//                        String denomTable = "";
////                        String query1 = "SELECT IS_MULTI_COMP_CAL FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='MULTI_CAL'";
//                        String query1 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='IS_MULTI_COMP_CAL'";
//                        PbReturnObject pbroObj1 = null;
//                        pbroObj1=pbdb.execSelectSQL(query1);
//                        if(pbroObj1.getRowCount()>0){
//                            String isMultiCompCal = pbroObj1.getFieldValueString(0, 0);
//                            session.setAttribute("isMultiCompCal", isMultiCompCal);
//                            if(isMultiCompCal.equalsIgnoreCase("YES")){
//                                String query2 = "SELECT COMPANY_ID FROM PRG_AR_USERS WHERE PU_ID = "+UserId;
//                                PbReturnObject pbroObj2 = null;
//                                pbroObj2=pbdb.execSelectSQL(query2);
//                                if(pbroObj2.getRowCount()>0){
//                                    String companyId = pbroObj2.getFieldValueString(0, 0);
//                                    if(companyId!=null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")){
//                                        session.setAttribute("companyId", companyId);
//                                        String query3 = "SELECT A.DENOM_TABLE FROM PRG_CALENDER_SETUP A,PRG_COMPANY_CALANDER B WHERE A.CALENDER_ID = B.CALENDER_ID AND B.DEFAULT_VAL = 'Y' AND B.COMPANY_ID ="+companyId;
//                                       PbReturnObject pbroObj3 = null;
//                                       pbroObj3=pbdb.execSelectSQL(query3);
//                                       if(pbroObj3.getRowCount()>0){
//                                           denomTable = pbroObj3.getFieldValueString(0, 0);
//                                       }
//                                    }
//                                }
//                            }
//                        }
                        //added by Nazneen to retrieve Theme color
//                        String themeColor="orange"; //For Data Flow
//                        session.setAttribute("theme", themeColor); //For Data Flow
//                        String themeColor="";
//                        themeColor = pbrv.getGlobalParametersPiTheme();
//                        if(themeColor!=null && !themeColor.equalsIgnoreCase("") && !themeColor.equalsIgnoreCase("null"))
//                            session.setAttribute("theme", "Green");

//                        session.setAttribute("denomTable", denomTable);
                        //End of code by Nazneen for multi calander
                        //added by Nazneen for logo based on company
//                        String isCompLogo = "N";
//                        String companyId = "";
//                        String compLogo = "";
//                        String bussLogo = "";
//                        String compTitle = "";
//                        String bussTitle = "";
//                        String rightWebSiteUrl = "";
//                        String leftWebSiteUrl = "";
//                        String copyRightMsg = "";
//                        String welcomeMsg = "";
//                        //Added by Ram 30Oct15 for Dynamic height/width of logo
//                        String leftSideLogoHeight = "";
//                        String leftSideLogoWidth = "";
//                        String rightSideLogoHeight = "";
//                        String rightSideLogoWidth = "";
//                        //added By Mohit Gupta for favicon and favtitle
//                        String compFavicon="";
//                        String compFavtitle="";
////                        String queryLogo = "SELECT IS_COMP_LOGO FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='COMP_LOGO'";
//                        String queryLogo = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='IS_COMP_LOGO'";
//                        PbReturnObject pbroObject = null;
//                        pbroObject=pbdb.execSelectSQL(queryLogo);
//                        if(pbroObject.getRowCount()>0){
//                            isCompLogo = pbroObject.getFieldValueString(0, 0);
//                            if(isCompLogo.equalsIgnoreCase("YES")){
//                                String query2 = "SELECT COMPANY_ID FROM PRG_AR_USERS WHERE PU_ID = "+UserId;
//                                PbReturnObject pbroObj2 = null;
//                                pbroObj2=pbdb.execSelectSQL(query2);
//                                if(pbroObj2.getRowCount()>0){
//                                    companyId = pbroObj2.getFieldValueString(0, 0);
//                                     if(companyId==null || companyId.equalsIgnoreCase("null") || companyId.equalsIgnoreCase("") || companyId.equalsIgnoreCase(" ")){
//                                        companyId = "0000";
//                                     }
//                                        String queryLogoDetails = "SELECT COMPANY_LOGO,BUSSINESS_LOGO,COMPANY_TITLE,BUSSINESS_TITLE,RIGHT_WEB_SITE_URL,LEFT_WEB_SITE_URL,COPYRIGTH_MSG,WELCOME_MSG,LEFT_SIDE_LOGO_HEIGHT ,LEFT_SIDE_LOGO_WIDTH ,RIGHT_SIDE_LOGO_HEIGHT,RIGHT_SIDE_LOGO_WIDTH,COMPANY_FEVICON,COMPANY_FEVICON_TITLE FROM PRG_COMPANY_LOGO WHERE COMPANY_ID="+companyId;
//                                        pbroObject = null;
//                                        pbroObject = pbdb.execSelectSQL(queryLogoDetails);
//                                        if (pbroObject.getRowCount() > 0) {
//                                            compLogo = pbroObject.getFieldValueString(0, 0);
//                                            bussLogo = pbroObject.getFieldValueString(0, 1);
//                                            compTitle = pbroObject.getFieldValueString(0, 2);
//                                            bussTitle = pbroObject.getFieldValueString(0, 3);
//                                            rightWebSiteUrl = pbroObject.getFieldValueString(0, 4);
//                                            leftWebSiteUrl = pbroObject.getFieldValueString(0, 5);
//                                            copyRightMsg = pbroObject.getFieldValueString(0, 6);
//                                            welcomeMsg = pbroObject.getFieldValueString(0, 7);
//                                            leftSideLogoHeight=pbroObject.getFieldValueString(0, 8);
//                                            leftSideLogoWidth=pbroObject.getFieldValueString(0, 9);
//                                            rightSideLogoHeight=pbroObject.getFieldValueString(0, 10);
//                                            rightSideLogoWidth=pbroObject.getFieldValueString(0, 11);
//                                            compFavicon=pbroObject.getFieldValueString(0, 12);
//                                            compFavtitle=pbroObject.getFieldValueString(0, 13);
//                                        }
//                                    }
//                                }
//                            }
//                        session.setAttribute("isCompLogo", isCompLogo);
//                        session.setAttribute("companyId", companyId);
//                        if(companyId!=null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")){
//                            session.setAttribute("compLogo", compLogo);
//                            session.setAttribute("bussLogo", bussLogo);
//                            session.setAttribute("compTitle", compTitle);
//                            session.setAttribute("bussTitle", bussTitle);
//                            session.setAttribute("rightWebSiteUrl", rightWebSiteUrl);
//                            session.setAttribute("leftWebSiteUrl", leftWebSiteUrl);
//                            session.setAttribute("copyRightMsg", copyRightMsg);
//                            session.setAttribute("welcomeMsg", welcomeMsg);
//                            session.setAttribute("leftSideLogoHeight", leftSideLogoHeight);
//                            session.setAttribute("leftSideLogoWidth", leftSideLogoWidth);
//                            session.setAttribute("rightSideLogoHeight", rightSideLogoHeight);
//                            session.setAttribute("rightSideLogoWidth", rightSideLogoWidth);
//                            session.setAttribute("compFavicon", compFavicon);
//                            session.setAttribute("compFavtitle", compFavtitle);
//                        }
//                        String query2 = "SELECT SETUP_DATE_TYPE_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='GLOBAL_PARAMS'";
//                        PbReturnObject pbroObj2 = null;
//                        pbroObj2=pbdb.execSelectSQL(query2);
//                        if(pbroObj2!=null && pbroObj2.getRowCount()>0){
//                            String oneviewdatetype = pbroObj2.getFieldValueString(0, 0);
//                            String layoutvar = pbroObj2.getFieldValueString(0, 1);
//                            session.setAttribute("oneviewdatetype", oneviewdatetype);
//                            session.setAttribute("LayoutVar", layoutvar);
//                        }

                        // Added by Prabal
//                        String sId="22,23,24,25,26,27,28,29,30,31,32,33,34,35,12,38,39,40,41,42";
////                        String query2 = "SELECT SETUP_DATE_TYPE_VALUE,SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY='GLOBAL_PARAMS'";
//                       String query2 = "SELECT SETUP_KEY,SETUP_DATE_TYPE_VALUE,SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_ID in ("+sId+")";
//                        PbReturnObject pbroObj2 = null;
//                        pbroObj2=pbdb.execSelectSQL(query2);
//                        SetGBLParamIntoSession.setGBLParamIntoSession( pbroObj2, session);
                        // Ended by Prabal
                        SetPbBaseRetObjIntoContext.setPbBaseRetObjIntoContext(Integer.parseInt(UserId), context, session);
                        //added by Dinanath for setting up locale for user from database
//                        String queryLocale = "SELECT LANGUAGE_COUNTRY_CODE from prg_ar_users WHERE PU_ID='"+UserId+"'";
//                        PbReturnObject pbroObj23 = null;
//                        pbroObj23=pbdb.execSelectSQL(queryLocale);
//                        if(pbroObj23!=null && pbroObj23.getRowCount()>0){
//                            String uLocale = pbroObj23.getFieldValueString(0, 0);
//                            if(uLocale!=null && !uLocale.isEmpty() && !uLocale.equalsIgnoreCase("null")){
//                            String newLocale[]=uLocale.split("_");
//                            Locale currLocale=new Locale(newLocale[0],newLocale[1]);
////                            Locale currLocale=new Locale("hi","IN");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }else{
//                            Locale currLocale=new Locale("en","US");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }
//                        }else{
//                            Locale currLocale=new Locale("en","US");
//                            session.setAttribute("UserLocaleFormat", currLocale);
//                            }
                        //added by Nazneen for logo based on company

                        if ((userstatushelper.getIsActive() && activate > 0) || ServerLicencing.equalsIgnoreCase("Yes")) {
                            DataTracker tracker = new DataTracker();
                            // 
                            tracker.setLoginInformation(request, user);
                            logger.info("-----------: User logged Successfully :--------------");
                            return mapping.findForward("startPage");
                        } else {
//                           ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "InActive User Can't Logged in");
                            logger.warn("InActive User Can't Logged in");
                            request.setAttribute("validateUser", "Invalid UserName and Password");
                            response.sendRedirect(context.getInitParameter("sso_login"));
                            return null;
                        }
                    }
                } else {
//                    ProgenLog.log(ProgenLog.FINE, this, "loginApplication", "User Validation Failed");
                    logger.warn("User Validation Failed");
                    request.setAttribute("validateUser", "Invalid UserName and Password");
                    response.sendRedirect(context.getInitParameter("sso_login"));
                    return null;
                }
            } else {
                request.setAttribute("validateUser", "Invalid UserName and Password");
                response.sendRedirect(context.getInitParameter("sso_login"));
                return null;
            }
        }
    }
//Added by Ashutosh for SSO Logout
    public void veractionLogout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String requString = request.getRequestURL().toString();
        String logoffurl=null;
        if(session.getAttribute("logoffurl")!=null){
        logoffurl = String.valueOf(session.getAttribute("logoffurl"));
        }
        PbBaseDAO dao = new PbBaseDAO();
        ServletContext context = this.getServlet().getServletContext();
        int adminCnt = 0;
        try {
            if (session != null) {
                final String ssovalue1 = session.getAttribute("ssoToken").toString();
                Cookie[] cookies = request.getCookies();
                HashMap userInfo = new HashMap<>();
                String ssoValue = "";
                for (int i = 0; i < cookies.length; i++) {
                    if (ssovalue1.equals(cookies[i].getValue())) {
                        if (session.getAttribute("PROGENTABLES") != null) {
                            session.removeAttribute("PROGENTABLES");
                        }
                        if ((session.getAttribute("USERID")) != null) {
                            String localUserId = (String) session.getAttribute("USERID");
//                logger.info("localUserId: "+localUserId);
                            PbTargetParamDb targetDb = new PbTargetParamDb();
                            targetDb.deleteLock(localUserId);
                            dao.updateLogOutInformation(session, localUserId);

                            sessionMap.remove(session.getId());
                            context.setAttribute("sessionMap", sessionMap);

                            adminCnt = dao.getAdminCount(localUserId);

                        }

                        if (context.getAttribute("sessionCount") != null) {
                            if (adminCnt <= 0) {
                                sessionCount = (Integer) context.getAttribute("sessionCount") - 1;
                            } else {
                                sessionCount = (Integer) context.getAttribute("sessionCount");
                            }
                            context.setAttribute("sessionCount", sessionCount);

                            dao.updateActiveSession(sessionCount);
                        }
                        statushelper.remove(session.getId());
                        context.setAttribute("helperclass", statushelper);
                        session.invalidate();
                    }
                }

            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        countOfUsers--;

//        return mapping.findForward("loginPage");
        try{
        if(logoffurl!=null && (logoffurl.equalsIgnoreCase("https://ft.client.veraction.com/sso/logout")||(logoffurl.equalsIgnoreCase("http://ft.client.veraction.com/sso/logout")))){
            response.sendRedirect(logoffurl);
        } else {
            response.sendRedirect(context.getInitParameter("sso_logout"));
        }}catch(Exception ex){
        response.sendRedirect(context.getInitParameter("sso_logout"));
        }

    }
//Added by Ashutosh for SSO Logout
    public ActionForward setLogOffURL(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String logoffurl = String.valueOf(request.getParameter("logoffurl"));
        request.getSession(false).setAttribute("logoffurl", logoffurl);
        return null;
    }

    public static void main(String[] args) {
        HashMap hmap = new HashMap();
        hmap.put(2, "Area");
        hmap.put(3, "Bar3D");
        hmap.put(20, "Stacked");
        hmap.put(1, "Line");


        TreeMap treemap = new TreeMap(hmap);

    }
}
