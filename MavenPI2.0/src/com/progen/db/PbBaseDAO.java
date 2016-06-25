/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.progen.superadmin.SuperAdminBd;
import com.progen.userlayer.db.UserLayerDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEncrypter;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class PbBaseDAO extends PbDb {

    public PbBaseDAO() {
    }
    PbBaseResourceBundle resbundle = (PbBaseResourceBundle) ResourceBundle.getBundle("com.progen.db.PbBaseResourceBundle");
    public static Logger logger = Logger.getLogger(PbBaseDAO.class);

    public PbReturnObject validateUser(String userName, String password) {
        logger.info("Enter validateUser");
//        ProgenLog.log(ProgenLog.FINE, this, "validateUser", "Enter validateUser");
        PbReturnObject retObj = null;
        Object[] Obj = new Object[2];
        String finalQuery = "";
        PbEncrypter enc = null;
        String pwd = null;
        String query = null;
        String checkExpDate = "select SETUP_DATE_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='ACTIVE_EXPIRE_DATE'";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        try {
            PbReturnObject dateObj = execSelectSQL(checkExpDate);
//            
//            
            int compareDate = 0;
            if (dateObj.getRowCount() != 0) {
                compareDate = dateObj.getFieldValueDate(0, 0).compareTo(date);
            }
//            
            if (compareDate == -1) {
//                ProgenLog.log(ProgenLog.FINE, this, "validateUser", "Subscription expired");
                logger.error("Subscription expired");
            } else {
//                ProgenLog.log(ProgenLog.FINE, this, "validateUser", "Subscription valid");
                logger.info("Subscription valid");
                enc = new PbEncrypter();
                pwd = enc.encrypt(password);
                Obj[0] = userName;
                Obj[1] = pwd;
                query = "SELECT PU_ID ID, PU_LOGIN_ID LOGIN_ID, PU_FIRSTNAME FIRSTNAME, "
                        + "PU_MIDDLENAME MIDDLENAME, PU_LASTNAME LASTNAME, PU_TYPE USERTYPE, "
                        + "PU_ACTIVE ACTIVE,ACCOUNT_TYPE FROM  PRG_AR_USERS WHERE upper(PU_LOGIN_ID)='&' AND PU_PASSWORD='&'";

                finalQuery = buildQuery(query, Obj);
                //  
                retObj = execSelectSQL(finalQuery);
//                ProgenLog.log(ProgenLog.FINE, this, "validateUser", "Exit validateUser");
                logger.info("Exit validateUser");
            }

            ////.println("query===="+finalQuery);
            return retObj;
        } catch (Exception s) {
            //////////////////////////////////////////////////////////////////////////.println(s);
            logger.error("Exception:", s);
            return retObj;

        } finally {
            enc = null;
            pwd = null;
            Obj = null;
            query = null;
            finalQuery = null;
        }
    }

    public void reportMessages(String userId, String reportId, String msgText) throws Exception {
        String checkReportMessagesQuery = resbundle.getString("checkReportMessages");
        String updateReportMessagesQuery = resbundle.getString("updateReportMessages");
        String insertReportMessagesQuery = resbundle.getString("insertReportMessages");

        Object[] dataValues = null;
        PbReturnObject retObj = null;
        String finalQuery = "";


        dataValues = new Object[2];
        dataValues[0] = reportId;
        dataValues[1] = userId;

        finalQuery = buildQuery(checkReportMessagesQuery, dataValues);
        retObj = execSelectSQL(finalQuery);
        //////////////////////////////////////////////////////////////////////.println("finalQuery is "+finalQuery);

        if (retObj != null && retObj.getRowCount() != 0) {
            dataValues = new Object[5];

            dataValues[0] = msgText;
            dataValues[1] = reportId;
            dataValues[2] = userId;

            finalQuery = buildQuery(updateReportMessagesQuery, dataValues);
            //////////////////////////////////////////////////////////////////////.println("finalQuery is "+finalQuery);
            execModifySQL(finalQuery);
        } else {
            dataValues = new Object[5];
            dataValues[0] = userId;
            dataValues[1] = reportId;
            dataValues[2] = msgText;
            dataValues[3] = userId;
            dataValues[4] = userId;

            finalQuery = buildQuery(insertReportMessagesQuery, dataValues);
            //////////////////////////////////////////////////////////////////////.println("finalQuery is "+finalQuery);
            execModifySQL(finalQuery);
        }

    }

    public String getReportUserMeassage(String userId, String reportId) throws Exception {

        String checkReportMessagesQuery = resbundle.getString("checkReportMessages");

        String pFinalMessage = "";
        String finalQuery = "";
        Object[] dataValues = null;
        PbReturnObject retObj = null;
        String[] colNames;

        dataValues = new Object[2];
        dataValues[0] = reportId;
        dataValues[1] = userId;

        finalQuery = buildQuery(checkReportMessagesQuery, dataValues);
        retObj = execSelectSQL(finalQuery);
        //////////////////////////////////////////////////////////////////////.println("finalQuery is "+finalQuery);

        colNames = retObj.getColumnNames();

        if (retObj != null && retObj.getRowCount() != 0) {
            pFinalMessage = retObj.getFieldValueString(0, colNames[0]);
        }


        return (pFinalMessage);
    }

    public PbReturnObject indicusLogin(String username, String password, String accounttype) {
        String status = "failuer";
        PbReturnObject pbretobj = new PbReturnObject();
        PbEncrypter enc = new PbEncrypter();
        PbDb pbdb = new PbDb();
        try {

            ////////.println("username" + username);
            ////////.println("password" + password);
            ////////.println("accounttype" + accounttype);

            password = enc.encrypt(password);
            ////////.println("after encrypting" + password);
            Object[] indicusobj = new Object[3];
            indicusobj[0] = username;
            indicusobj[1] = password;
            indicusobj[2] = accounttype.toUpperCase();
            String indquey = resbundle.getString("indicuslogin");
            ////.println("before building" + indquey);
            String buildindquery = pbdb.buildQuery(indquey, indicusobj);
            ////.println("builded query" + buildindquery);
            pbretobj = pbdb.execSelectSQL(buildindquery);
            if (pbretobj.getRowCount() != 0) {
                status = "succes";
            } else {
                status = "failuer";
            }

        } catch (Exception e) {
            ////////.println("Exception caught in pbbasedao class of forindicus");
            logger.error("Exception:", e);
        }
        return pbretobj;
    }

    public ArrayList<String> getWordNetTaggerPath() {
        String sqlQuery = resbundle.getString("getFilePath");
        PbReturnObject retObj = null;
        ArrayList<String> filePaths = new ArrayList<String>();
        try {
            retObj = execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
                filePaths.add(retObj.getFieldValueString(0, 0));
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        return filePaths;
    }

    public PbReturnObject getUserDetails(String userId) {
        PbReturnObject retObj = null;
        String userDetsQry = null;
        String finalQry = null;
        userDetsQry = resbundle.getString("getUserDetails");
        Object[] obj = new Object[1];
        obj[0] = userId;
        finalQry = buildQuery(userDetsQry, obj);
        try {
            retObj = execSelectSQL(finalQry);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return retObj;
    }

    public PbReturnObject getUserStatus(String userId) {
        PbReturnObject retObj = null;
        String userDetsQry = null;
        String finalQry = null;
        userDetsQry = resbundle.getString("getUserStatus");
        Object[] obj = new Object[1];
        obj[0] = userId;
        finalQry = buildQuery(userDetsQry, obj);
        try {
            retObj = execSelectSQL(finalQry);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return retObj;
    }

    public void updateLogOutInformation(HttpSession session, String userid) {
        try {
            //HttpSession session = null;
            String userId = null;
            String finalQry = null;
            String sql = null;
            //session = request.getSession(false);
            //userId = String.valueOf(session.getAttribute("USERID"));
            userId = userid;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                sql = "Update Hit_Calc Set Status='N',LOGOUT_DATE =getdate(),LOGOUT_TIME ='&' WHERE SESSION_ID='&' AND user_id=&";
            } else {
                sql = resbundle.getString("updateLogOutInformation");
            }

            Object[] obj = new Object[3];
            GregorianCalendar date = new GregorianCalendar();
            String loginTime = date.get(Calendar.HOUR) + " : " + date.get(Calendar.MINUTE) + " : " + date.get(Calendar.SECOND);
            obj[0] = loginTime;
            obj[1] = session.getId();
            obj[2] = userId;
            //
            finalQry = buildQuery(sql, obj);
            execUpdateSQL(finalQry);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateActiveSession(int sessionCnt) {
        try {
            String qry = "update Test_Fact set ACTIVE_SESSIONS=" + sessionCnt + " where ORG_ID=100";
            execUpdateSQL(qry);
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    public PbReturnObject getAllActiveSessions() {
        PbReturnObject retObj = null;
        try {
            String qry = "SELECT USER_ID,SESSION_ID,USER_NAME,LOGIN_TIME FROM HIT_CALC WHERE STATUS='Y'";
            retObj = execSelectSQL(qry);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return retObj;
    }

    public void killAllsessions() {
        String qry = "update Test_Fact set ACTIVE_SESSIONS=0 where ORG_ID=100";
        String qry2 = "Update Hit_Calc Set Status='N',LOGOUT_DATE =sysdate,LOGOUT_TIME ='&' WHERE Status='Y'";
        Object[] obj = new Object[1];
        GregorianCalendar date = new GregorianCalendar();
        String logoutTime = date.get(Calendar.HOUR) + " : " + date.get(Calendar.MINUTE) + " : " + date.get(Calendar.SECOND);
        obj[0] = logoutTime;
        String finalQry = buildQuery(qry2, obj);
        ArrayList al = new ArrayList();
        al.add(qry);
        al.add(finalQry);
        try {
            executeMultiple(al);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public int getAdminCount(String userId) {
        int cnt = 0;
        String finalQry = null;
        PbReturnObject retObj = null;
        String sql = resbundle.getString("getAdminCount");
        Object[] obj = new Object[1];
        obj[0] = userId;
        finalQry = buildQuery(sql, obj);
        logger.info("getAdminCount qry: " + finalQry);
        try {
            retObj = execSelectSQL(finalQry);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        if (retObj.getRowCount() > 0) {
            cnt = Integer.parseInt(retObj.getFieldValueString(0, 0));
            logger.info("getAdminCount: " + cnt);
        }
        return cnt;

    }

    // by gopesh for getTimeDuration
    public long getTimeDuration(String loginTime) throws ParseException {
        Date sysDate = new Date();
        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        String machitDateFormat = sdf.format(sysDate);
        Date machit = sdf.parse(machitDateFormat.replace(" ", ""));
        logger.info(machit);
        Date date = sdf.parse(loginTime.replace(" ", ""));
        Date diff = new Date(machit.getTime() - date.getTime());
        logger.info(diff.getTime() / 60000);
        return (diff.getTime() / 60000);
    }
    //by gopesh

    public PbReturnObject getUserActiveState(String userId) {
        PbReturnObject retObj = null;

        try {
            String qry = "SELECT PU_ACTIVE FROM prg_ar_users WHERE PU_ID=" + userId;
            retObj = execSelectSQL(qry);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return retObj;
    }//updateUserInfo by gopesh

    public void updateUserInfo(String userId, String state) throws Exception {
        PbReturnObject retObj = null;
        try {
            String qry = "update  PRG_AR_USERS set PU_ACTIVE='" + state + "' where pu_id=" + userId;
            execUpdateSQL(qry);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

    }
    //by gopesh

    public int getPurchaseUsers() {
        int purchasedUsers = 0;
        PbDb pbdb = new PbDb();
        String validationQry1 = null;
        PbReturnObject pbroOb = null;
        try {
            validationQry1 = "SELECT ANALYZERS from PRG_TEST_DATA";
            pbroOb = pbdb.execSelectSQL(validationQry1);
            purchasedUsers = Integer.parseInt(pbroOb.getFieldValueString(0, 0));
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return purchasedUsers;
    }
    //by gopesh

    public int getActiveUsers() {
        int activeUserCount = 0;
        PbDb pbdb = new PbDb();
        PbReturnObject pbroActive = null;
        try {
            String queryActive = "select * from prg_ar_users where PU_ACTIVE='Y' AND PU_ID not in (41)";
            pbroActive = pbdb.execSelectSQL(queryActive);
            activeUserCount = pbroActive.getRowCount();
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return activeUserCount;
    }
    //added by mohit for sso integration
    //modify by Ashutosh

    public HashMap validateToken(HttpServletRequest request, String tokenName,ServletContext context) throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException, IOException {
        HttpSession session = request.getSession(false);
        Cookie[] cookies = request.getCookies();
        HashMap userInfo = new HashMap<>();
        String https_url="";
        String ssoValue = "";
        for (int i = 0; i < cookies.length; i++) {
            if (tokenName.equals(cookies[i].getName())) {
                ssoValue = cookies[i].getValue();
            }
        }
        session.setAttribute("ssoToken", ssoValue);
        String requString = request.getRequestURL().toString();
        
        https_url = context.getInitParameter("sso_validate").toString();
        https_url = https_url.replace("$", ssoValue);
//        String https_url = "https://service.veraction.com/sso/session/"+ssoValue+"/validate?appToken=710f4b14-6064-4166-9297-cca19b40d503";
//        String https_url = "https://localhost:7701/sso/session/" + ssoValue + "/validate?appToken=710f4b14-6064-4166-9297-cca19b40d503";

        if (!ssoValue.equalsIgnoreCase("")) {
            userInfo = callWebService(https_url);
            return userInfo;
        } else {
            userInfo.put("Token", "false");
            return userInfo;
        }
//                    return userInfo;
    }

    public HashMap getUserInfo(String input) {
        JSONObject obj;
        HashMap userInfo = new HashMap<>();
        try {
            obj = (JSONObject) new JSONParser().parse(input);
            logger.info("Status: " + obj.get("status"));
            logger.info("User: " + obj.get("user"));
            logger.info("Gender: " + obj.get("gender"));
            logger.info("Latitude: " + obj.get("latitude"));
            logger.info("Longitude: " + obj.get("longitude"));
            userInfo.put("status", obj.get("status"));
            userInfo.put("user", obj.get("user"));

        } catch (org.json.simple.parser.ParseException ex) {
            logger.error("Exception:", ex);
        }
        return userInfo;
    }

    public PbReturnObject validateUserWithoutPwd(HashMap userInfo,ServletContext context,HttpSession session) {
     
        PbReturnObject retObj = null;
        Object[] Obj = new Object[1];
        String finalQuery = "";
        PbEncrypter enc = null;
        String pwd = null;
        String query = null;
        String checkExpDate = "select SETUP_DATE_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='ACTIVE_EXPIRE_DATE'";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String ssovalue=session.getAttribute("ssoToken").toString();
        try {
            PbReturnObject dateObj = execSelectSQL(checkExpDate);
//            
//            
            int compareDate = 0;
            if (dateObj.getRowCount() != 0) {
                compareDate = dateObj.getFieldValueDate(0, 0).compareTo(date);
            }
//            
            if (compareDate == -1) {
//                ProgenLog.log(ProgenLog.FINE, this, "validateUser", "Subscription expired");
                logger.warn("Subscription expired");
            } else {
//                ProgenLog.log(ProgenLog.FINE, this, "validateUser", "Subscription valid");
                logger.info("Subscription valid");
                enc = new PbEncrypter();
//                pwd = enc.encrypt(password);
                Obj[0] = ((HashMap) userInfo.get("user")).get("username") + "_" +((HashMap) userInfo.get("user")).get("clientCode");
//                Obj[1] = pwd;
                query = "SELECT PU_ID ID, PU_LOGIN_ID LOGIN_ID, PU_FIRSTNAME FIRSTNAME, "
                        + "PU_MIDDLENAME MIDDLENAME, PU_LASTNAME LASTNAME, PU_TYPE USERTYPE, "
                        + "PU_ACTIVE ACTIVE,ACCOUNT_TYPE FROM  PRG_AR_USERS WHERE upper(PU_LOGIN_ID)='&' ";

                finalQuery = buildQuery(query, Obj);
                //  
                retObj = execSelectSQL(finalQuery);
                if (retObj == null || retObj.getRowCount() == 0) {
                    boolean CreateSSOUser = CreateSSOUser(userInfo,context,session);
                    if (CreateSSOUser) {
                        retObj = execSelectSQL(finalQuery);
                    }
                }

//                ProgenLog.log(ProgenLog.FINE, this, "validateUser", "Exit validateUser");
                logger.info("Exit validateUser");
            }

            ////.println("query===="+finalQuery);
            return retObj;
        } catch (Exception s) {
            //////////////////////////////////////////////////////////////////////////.println(s);
            logger.error("Exception:", s);
            return retObj;

        } finally {
            enc = null;
            pwd = null;
            Obj = null;
            query = null;
            finalQuery = null;
        }
    }
//Added by mohit
//Modify by Ashutosh

    public boolean CreateSSOUser(HashMap userInfo,ServletContext context,HttpSession session) {
        String ssovalue=session.getAttribute("ssoToken").toString();
        String query2 = "";
        String role = "";
        String https_url="";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            query2 = "insert into PRG_AR_USERS(PU_LOGIN_ID,PU_FIRSTNAME,PU_LASTNAME,PU_EMAIL,PU_ACTIVE,PU_START_DATE,PU_END_DATE,USER_TYPE) VALUES('&','&','&','&','&','&','&',&) ";
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            query2 = "insert into PRG_AR_USERS(PU_LOGIN_ID,PU_FIRSTNAME,PU_LASTNAME,PU_EMAIL,PU_ACTIVE,PU_START_DATE,PU_END_DATE,USER_TYPE) VALUES('&','&','&','&','&','&''&',&) ";
        } else {
            query2 = "insert into PRG_AR_USERS(PU_ID,PU_LOGIN_ID,PU_FIRSTNAME,PU_LASTNAME,PU_EMAIL,PU_ACTIVE,PU_START_DATE,PU_END_DATE,USER_TYPE) VALUES(PRG_AR_USERS_SEQ.nextval,'&','&','&','&','&','&','&',&) ";
        }

        try {
            https_url = context.getInitParameter("sso_users").toString();
            https_url = https_url.replace("$", ssovalue);
//            String https_url = "https://service.veraction.com/sso/session/" + ssovalue + "/user?appToken=710f4b14-6064-4166-9297-cca19b40d503";
//            String https_url = "https://localhost:7701/sso/session/" + ssoValue + "/links?appToken=710f4b14-6064-4166-9297-cca19b40d503";

            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] certs = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String t) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String t) {
                    }
                }
            };
            ssl_ctx.init(null, // key manager
                    certs, // trust manager
                    new SecureRandom()); // random number generator
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//           String https_url = "https://localhost:7700/sso/session/"+ssoValue+"/validate?appToken=veraction";
            URL url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            // Guard against "bad hostname" errors during handshake.
            con.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String host, SSLSession sess) {
                    if (host.equals("app-ft-30.mem-tw.veraction.net")) {
//                    if (host.equals("localhost")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            if (con != null) {
//                
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input = "";
                String str = "";

                while ((input = br.readLine()) != null) {
                    str = input;
//                    

                }
                JsonElement jelement = new JsonParser().parse(str);
                JsonObject jobject = jelement.getAsJsonObject();
                jobject = jobject.getAsJsonObject("user");
                role = (jobject.get("insightsUserRole").toString()).replaceAll("^\"|\"$", "");
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }



//      String query2="insert into PRG_AR_USERS(PU_ID,PU_LOGIN_ID,PU_FIRSTNAME,PU_LASTNAME,PU_PASSWORD,PU_EMAIL,PU_CONTACTNO,PU_ADDRESS,PU_COUNTRY,PU_ACTIVE,PU_START_DATE,PU_END_DATE,USER_TYPE) VALUES(PRG_AR_USERS_SEQ.nextval,'&','&','&','&','&','&','&','&','&','&','&',&) ";
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
        String todaysDate = dateFormat.format(date);
        PbDb pbdb = new PbDb();
//

        Object obj[] = new Object[8];
        obj[0] = ((HashMap) userInfo.get("user")).get("username") +"_"+((HashMap) userInfo.get("user")).get("clientCode");
        obj[1] = ((HashMap) userInfo.get("user")).get("firstName") + "";
        obj[2] = ((HashMap) userInfo.get("user")).get("lastName") + "";
        obj[3] = ((HashMap) userInfo.get("user")).get("email") + "";
        obj[4] = "Y";
        obj[5] = todaysDate;
        obj[6] = todaysDate;
        if (role.equalsIgnoreCase("VPA")) {
            obj[7] = 9999;
        } else if (role.equalsIgnoreCase("PA")) {
            obj[7] = 10000;
        } else if (role.equalsIgnoreCase("RPA")) {
            obj[7] = 10001;
        } else if (role.equalsIgnoreCase("A")) {
            obj[7] = 10002;
        } else {
            obj[7] = 10002;
        }

        query2 = super.buildQuery(query2, obj);
        try {
            int i1 = pbdb.execUpdateSQL(query2);

            if (i1 == 1) {
                String query = "select PU_ID,PU_LOGIN_ID from PRG_AR_USERS where PU_LOGIN_ID='" + obj[0] + "' and"
                        + " PU_FIRSTNAME='" + obj[1] + "' and USER_TYPE=" + obj[7];
                PbReturnObject pbro1 = new PbReturnObject();
                pbro1 = pbdb.execSelectSQL(query);
                SuperAdminBd adminBd = new SuperAdminBd();
                adminBd.saveAssignedModules(pbro1.getFieldValueInt(0, 0), Integer.parseInt(obj[7].toString()), pbro1.getFieldValueString(0, 1), "Y");
            }

        } catch (SQLException ex) {
            return false;
//        
//            logger.error("Exception:",ex);
        } catch (Exception ex) {
            return false;
        }




        return true;

    }
//Added by mohit
//
//    public HashMap checkUserSession(String ssovalue) {
//        String https_url = "https://service.veraction.com/sso/session/"+ssovalue+"/keepAlive?appToken=710f4b14-6064-4166-9297-cca19b40d503";
//
////        String https_url = "https://localhost:7701/sso/session/" + ssovalue + "/keepAlive?appToken=710f4b14-6064-4166-9297-cca19b40d503";
//        HashMap callWebService = callWebService(https_url);
//        return callWebService;
//    }
//Added by mohit


    public HashMap checkUserSession(String ssovalue,String https_url) {
//        String https_url = context.getInitParameter("sso_keepAlive").toString();
        https_url = https_url.replace("$", ssovalue);
//        String https_url = "https://localhost:7701/sso/session/" + ssovalue + "/keepAlive?appToken=710f4b14-6064-4166-9297-cca19b40d503";
        HashMap callWebService = callWebService(https_url);
        return callWebService;
    }


    public HashMap callWebService(String urlstring) {
        //https://localhost:7700/sso/session/12345/validate?appToken=12345

//    String REST_URI = "https://localhost:7700/sso";
//    String INCH_TO_FEET = "/validate?appToken=veraction";
////     String FEET_TO_INCH = "/ConversionService/FeetToInch/";
//                     ClientConfig config = new DefaultClientConfig();
////                     SSLContext ctx = SSLContext.getInstance("SSL");
        HashMap userInfo = new HashMap<>();
        try {

            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] certs = new TrustManager[]{
                new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String t) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String t) {
                    }
                }
            };
            ssl_ctx.init(null, // key manager
                    certs, // trust manager
                    new SecureRandom()); // random number generator
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//           String https_url = "https://localhost:7700/sso/session/"+ssoValue+"/validate?appToken=veraction";
            URL url = new URL(urlstring);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();


            // Guard against "bad hostname" errors during handshake.
            con.setHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String host, SSLSession sess) {
                    if (host.equals("app-ft-30.mem-tw.veraction.net")) {
//                    if (host.equals("localhost")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });


            if (con != null) {
//     try {
//	
//	
//	
//
//	Certificate[] certs1 = con.getServerCertificates();
//	for(Certificate cert : certs1){
//	  
//	  
//	  
//	  
//	  
//	}
//     } catch (SSLPeerUnverifiedException e) {
//	  logger.error("Exception:",e);
//     } catch (IOException e){
//	  logger.error("Exception:",e);
//     }
            }

            if (con != null) {

//    try {
                logger.info("****** Content of the URL ********");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input;
                while ((input = br.readLine()) != null) {
                    logger.info(input);
                    userInfo = getUserInfo(input);

                }

                br.close();
//     } catch (IOException e) {
//	logger.error("Exception:",e);
//     }
//   }
                userInfo.put("Token", "true");
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return userInfo;
    }

//Added by Ashutosh for Veraction User Settings
    public void userSettings(HttpSession session,ServletContext context) {
        String userId=session.getAttribute("USERID").toString();
        String ssoValue=session.getAttribute("ssoToken").toString();
        String insightsUserRole="";
        String urlstring="";
        Map<String, ArrayList> elementCurrency;
        urlstring = context.getInitParameter("sso_users").toString(); 
        urlstring = urlstring.replace("$", ssoValue);
//        String urlstring = "https://service.veraction.com/sso/session/" + ssoValue + "/user?appToken=710f4b14-6064-4166-9297-cca19b40d503";
        //String urlstring = "https://localhost:7701/sso/session/" + ssoValue + "/user?appToken=710f4b14-6064-4166-9297-cca19b40d503";

        try {
            PbDb pbdb = new PbDb();
            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] certs = new TrustManager[]{
                new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String t) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String t) {
                    }
                }
            };
            ssl_ctx.init(null, // key manager
                    certs, // trust manager
                    new SecureRandom()); // random number generator
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
//           String https_url = "https://localhost:7700/sso/session/"+ssoValue+"/validate?appToken=veraction";
            URL url = new URL(urlstring);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            // Guard against "bad hostname" errors during handshake.
            con.setHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String host, SSLSession sess) {
                    if (host.equals("app-ft-30.mem-tw.veraction.net")) {
//                        if (host.equals("localhost")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            if (con != null) {
//    try {
                logger.info("****** User Settings ********");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input = "";
                String str = "";
                while ((input = br.readLine()) != null) {
                    str = input;

                }
                JsonElement jelement = new JsonParser().parse(str);
                JsonObject jobject = jelement.getAsJsonObject();
                jobject = jobject.getAsJsonObject("user");
                JsonArray jarray = jobject.getAsJsonArray("accessibleClientCodes");
                //String result = jarray.get(0).getAsString();
                //    jobject = jarray.get(0).getAsJsonObject();
//    String result = jobject.get("translatedText").toString();
                String query8 = "delete from PRG_SEC_USER_COMPANY where PROGEN_USER_ID='" + userId + "'";
//            int intStatus = pbdb.execUpdateSQL(query8);  
                pbdb.execUpdateSQL(query8);
                String query = "select ID from PRG_SEC_USER_COMPANY where PROGEN_USER_ID='" + userId + "'";
                PbReturnObject pbro1 = new PbReturnObject();
                pbro1 = pbdb.execSelectSQL(query);
                Map<String, Boolean> ClientCodesMap = new HashMap<String, Boolean>();
                if (pbro1.getFieldValue(0, 0) == null) {
//                    String subString = "";
                    StringBuilder subString = new StringBuilder(200);
                    for (int i = 0; i < jarray.size(); i++) {
                        if (i == 0) {
                            subString.append("'").append(jarray.get(i).getAsString()).append("'");
//                            subString += "'" + jarray.get(i).getAsString() + "'";
                            ClientCodesMap.put(jarray.get(i).getAsString(), Boolean.TRUE);
                        } else {
//                        subString += ",'" + jarray.get(i).getAsString() + "'";
                            subString.append(",'").append(jarray.get(i).getAsString()).append("'");
                            ClientCodesMap.put(jarray.get(i).getAsString(), Boolean.TRUE);
                        }
                    }

                    String app_id = jobject.get("id").toString();
                    String app_username = jobject.get("username").toString();
                    if (!ClientCodesMap.isEmpty()) {
                        session.setAttribute("ClientCodesMap", ClientCodesMap);
                    }
                    if ((jobject.get("insightsUserRole").toString()).replaceAll("^\"|\"$", "") != null) {
                        session.setAttribute("insightsUserRole", (jobject.get("insightsUserRole").toString()).replaceAll("^\"|\"$", ""));

                    }
                    if ((jobject.get("clientCode").toString()).replaceAll("^\"|\"$", "") != null) {
                        session.setAttribute("clientCode", (jobject.get("clientCode").toString()).replaceAll("^\"|\"$", ""));
                    }
                    if ((jobject.get("language").toString()).replaceAll("^\"|\"$", "") != null) {
                        session.setAttribute("UserLocaleFormat", (jobject.get("language").toString()).replaceAll("^\"|\"$", ""));
                    }

                    app_username = app_username.replaceAll("^\"|\"$", "");
                    String query1 = "insert into PRG_SEC_USER_COMPANY(ID,company_id,progen_user_id,app_user_id,progen_user_name,app_user_name,client_code)"
                            + "select comp_seq.nextval ID,company_id,progen_user_id,app_user_id,progen_user_name,app_user_name,company_name "
                            + "from(select company_id,'" + userId + "' progen_user_id,'" + app_id + "' app_user_id,'" + app_username + "' progen_user_name,'" + app_username + "' app_user_name,company_name from PRG_COMPANY_MASTER where company_name in(" + subString + ")) a";
                    logger.info("query1: " + query1);
                    int i2 = pbdb.execUpdateSQL(query1);
                    pbdb.execUpdateSQL(query1);
                    String query2 = "insert into PRG_GBL_USER_SETUP(setup_id,progen_user_id,app_user_id,app_user_name,client_code,currency,user_lang,measurementsystem)"
                            + "VALUES(PRG_GBL_USER_SETUP_SEQ.nextval,&,&,'&','&','&','&','&')";
                    Object obj[] = new Object[7];
                    obj[0] = userId;
                    obj[1] = (jobject.get("id").toString()).replaceAll("^\"|\"$", "");
                    obj[2] = (jobject.get("username").toString()).replaceAll("^\"|\"$", "");
                    obj[3] = (jobject.get("clientCode").toString()).replaceAll("^\"|\"$", "");
                    obj[4] = (jobject.get("currency").toString()).replaceAll("^\"|\"$", "");
                    obj[5] = (jobject.get("language").toString()).replaceAll("^\"|\"$", "");
                    obj[6] = (jobject.get("measurementSystem").toString()).replaceAll("^\"|\"$", "");

                    query2 = super.buildQuery(query2, obj);
                    logger.info("query2: " + query2);
                     int i4 = pbdb.execUpdateSQL(query2);
                    // logger.info("Row inserted in PRG_GBL_USER_SETUP: "+i4);
//                    pbdb.execUpdateSQL(query2);


//     } catch (IOException e) {
//	logger.error("Exception:",e);
//     }
                }
//                     if (PbBaseAction.isExecuted) {
//                            return;






//                       } else {






                //setting up user currency
                elementCurrency = new HashMap<String, ArrayList>();
                String symbolQuery = "select distinct(symbol) from CURRENCY where CURRENCY.CODE ='" + (jobject.get("currency").toString()).replaceAll("^\"|\"$", "") + "'";
                PbReturnObject sym_pbro = new PbReturnObject();
                sym_pbro = pbdb.execSelectSQL(symbolQuery);
                ArrayList currencyInfoList = new ArrayList();
                currencyInfoList.add((jobject.get("currency").toString()).replaceAll("^\"|\"$", ""));
                currencyInfoList.add(sym_pbro.getFieldValue(0, 0).toString());

                String elementIdQuery = "select distinct(ELEMENT_ID) from PRG_ALL_BASE_CURRENCY_ELEMENTS where flag='C'";
                PbReturnObject element_pbro = new PbReturnObject();
                element_pbro = pbdb.execSelectSQL(elementIdQuery);
                for (int i = 0; i < element_pbro.rowCount; i++) {
                    elementCurrency.put((element_pbro.getFieldValue(i, 0)).toString(), currencyInfoList);
                }

                session.setAttribute("elementCurrency", elementCurrency);
//                        HttpSession session = SessionListener.getSSOSession(ssoValue);
//                        Gson gson=new Gson();
//                         
//                         String st = (element_pbro.getFieldValue(1, 0)).toString();
//                            HashMap ec = (HashMap)session.getAttribute("elementCurrency");
//                         
//                         PbBaseAction.isExecuted = false;
//            }
                br.close();

            }
            //modified by Dinanath
            String sec_query = "select report_id from prg_ar_user_reports where user_id='" + userId + "'";
            String sec_query2 = "select setup_char_value from prg_gbl_setup_values where setup_key='TAGGED_USER_ID'";


            PbReturnObject pbro2 = new PbReturnObject();
            PbReturnObject pbro3 = new PbReturnObject();
            pbro2 = pbdb.execSelectSQL(sec_query);
            pbro3 = pbdb.execSelectSQL(sec_query2);
            String userIdFromGbl = pbro3.getFieldValueString(0, 0);


//            if (pbro2.getFieldValue(0, 0) == null) {
//
//                UserLayerDAO ud = new UserLayerDAO();
//                ud.addToUserFolderAssignment(userId, "2422", "2006");
                String ex_update_query = "update prg_ar_users set start_page =  'reportViewer.do?reportBy=viewReport&REPORTID=18953' where PU_ID=" + userId;
                int i3 = pbdb.execUpdateSQL(ex_update_query);
                //
//            }
            /*Added by Ashutosh for home tabs*/
            String home_qury ="SELECT report_id from prg_home_tabs where user_id="+userId;
            PbReturnObject pbro5 = new PbReturnObject();
            pbro5 = pbdb.execSelectSQL(home_qury);
            if (pbro5.getFieldValue(0, 0) == null) {
                String home_tabs = "Insert Into prg_home_tabs(USER_ID,REPORT_ID,REPORT_NAME,REPORT_TYPE,REPORT_DESC,DISPLAY_TYPE)"
                                +"select '"+userId+"',REPORT_ID,REPORT_NAME,REPORT_TYPE,REPORT_DESC,DISPLAY_TYPE from prg_home_tabs where user_id='" + userIdFromGbl + "'";
            int i4 = pbdb.execUpdateSQL(home_tabs);
                //
            }
            //added by Dinanath create new tag for users with copying all tags which is assigned for 41
            String insert_in_tag_master1 = resbundle.getString("direct_insert_prg_tag_master");
            Object objTag[] = new Object[3];
            objTag[0] = userId;
            objTag[1] = userIdFromGbl;
            objTag[2] = userId;
            insert_in_tag_master1 = buildQuery(insert_in_tag_master1, objTag);
            int i34 = pbdb.execUpdateSQL(insert_in_tag_master1);

            //added by Dinanath create new tag assignment for users with copying all tags which is assigned for 41
            String insert_tags_assignment2 = resbundle.getString("direct_insert_prg_tag_report_assignment");
            Object obj[] = new Object[7];
            obj[0] = userId;
            obj[1] = userIdFromGbl;
            obj[2] = userId;
            obj[3] = userIdFromGbl;
            obj[4] = userIdFromGbl;
            obj[5] = userId;
            obj[6] = userId;
            insert_tags_assignment2 = buildQuery(insert_tags_assignment2, obj);
            i3 = pbdb.execUpdateSQL(insert_tags_assignment2);
            //
                   //added by anitha
            String grpFolder = "insert into PRG_GRP_USER_FOLDER_ASSIGNMENT "
                                +" (USER_ASSI_ID,"
                                +" USER_FOLDER_ID,"
                                +" USER_ID,"
                                +" GRP_ID,"
                                +" START_DATE,"
                                +" END_DATE)"
                                +" SELECT"
                                +" PRG_GRP_USER_FOLDER_ASSI_SEQ.nextval,"
                                +" USER_FOLDER_ID,"
                                +" "+userId+","
                                +" GRP_ID,"
                                +" START_DATE,"
                                +" END_DATE"
                                +" from("
                                +" select * from PRG_GRP_USER_FOLDER_ASSIGNMENT"
                                +" where user_id = "+userIdFromGbl+""
                                +" and user_folder_id not in (select user_folder_id from PRG_GRP_USER_FOLDER_ASSIGNMENT"
                                +" where user_id = "+userId+"))";
            try{
            int loadedintogrpFolder = pbdb.execUpdateSQL(grpFolder);
            }catch(Exception e){
                System.out.println("insert into::::::::  "+grpFolder);
                System.out.println("Exception occured:::: while inserting into PRG_GRP_USER_FOLDER_ASSIGNMENT");
                e.printStackTrace();
            }


            String arUsers = "insert into PRG_AR_USER_REPORTS"
                                +" ("
                                +" USER_REP_ID,"
                                +" USER_ID,"
                                +" REPORT_ID,"
                                +" PUR_REPORT_SEQUENCE,"
                                +" PUR_FAV_REPORT,"
                                +" PUR_CUST_REPORT_NAME,"
                                +" IS_WHAT_IF_REPORT)"
                                +" SELECT"
                                +" PRG_AR_USER_REPORTS_SEQ.nextval,"
                                +" "+userId+","
                                +" REPORT_ID,"
                                +" PUR_REPORT_SEQUENCE,"
                                +" PUR_FAV_REPORT,"
                                +" PUR_CUST_REPORT_NAME,"
                                +" IS_WHAT_IF_REPORT"
                                +" from("
                                 +" select distinct  "+userId+" USER_ID                   ,      "
                                +" A.REPORT_ID                 ,      "
                                +" A.PUR_REPORT_SEQUENCE       ,      "
                                +" 'N'            ,"
                                +" A.PUR_CUST_REPORT_NAME      ,"
                                +" A.IS_WHAT_IF_REPORT       "
                                  +" from PRG_AR_USER_REPORTS A"
                                +" inner join PRG_AR_REPORT_DETAILS c"
                                +" on (a.report_id = c.report_id )"
                                +" inner join"
                                +" (select user_folder_id from PRG_GRP_USER_FOLDER_ASSIGNMENT A"
                                +" where A.user_id = "+userId+") B"
                                +" on (c.folder_id =b.user_folder_id)"
                                +" where A.user_id ="+userIdFromGbl+""
                                +" and A.report_id not in (select report_id from PRG_AR_USER_REPORTS where user_id = "+userId+"))";

            try{
               int loadedarUsers = pbdb.execUpdateSQL(arUsers);
            }catch(Exception e){
                System.out.println("insert into::::::::  "+arUsers);
                System.out.println("Exception occured:::: while inserting into PRG_GRP_USER_FOLDER_ASSIGNMENT");
                e.printStackTrace();
            }
            //end of code by anitha
            
           
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
}
