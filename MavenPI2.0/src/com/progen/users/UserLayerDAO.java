/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.progen.action.UserStatusHelper;
import com.progen.superadmin.SuperAdminBd;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEncrypter;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class UserLayerDAO extends PbDb {

    public static Logger logger = Logger.getLogger(UserLayerDAO.class);
    public String[] paramList = {"Date Selection", "Report Save", "Reset", "Toggle", "Hide Date Section", "Go Button", "Param_Reset", "Edit View By", "Add Parameters", "Remove Parameters",
        "Parameter Save", "Add Inner View By"};
    public String[] graphList = {"Add Graph", "Graph Save", "Save as Image", "Graph Types", "Table Columns", "Zoom", "Columns", "Properties", "Delete", "Rename"};
    public String[] tableList = {"Refresh Table", "Display All Records", "Time Display", "Table Properties", "Edit Table", "What-if Analysis", "Group Measures", "Export to Excel",
        "Export to PDF", "Export as CSV", "Export as HTML", "Define Custom Measure", "Define Custom Sequence", "Table Save", "Parameter Rename",
        "Param_Sort", "Operations(Parameter)", "Custom Sequence", "Show Duplicates", "Table Search", "Sort", "Quick Operations", "Custom KPI",
        "Measure Rename", "Statistical Options", "Quick Formulas", "Goal Seek", "Other Operations", "Import Excel"};
    public String[] actionList = {"Favorite Links", "Save as New Report", "Overwrite Report", "Assign Report to Users", "E-Mail Report", "Report Comparison", "Store Snapshot",
        "Store Static Headline", "Store Dynamic Headline", "Scheduler", "View Scheduled Reports", "Display Report Query", "View Fact Formula",
        "Parameter Reset", "Measure Drill Down", "Dimension Drill Down", "Advanced Parameters", "Parameter Edit (View/Filter)", "Parameter Sequence",
        "Initialize Report", "Scheduled Reports", "Snapshots", "Top/Bottom", "Report Info"};
    public HashMap featureHashMap = new HashMap();
//    PbUserLayerResourceBundle resBundle = new PbUserLayerResourceBundle();
    private boolean isCompanyValid = false;
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbUserLayerResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbUserLayerResourceBundleMySql();
            } else {
                resourceBundle = new PbUserLayerResourceBundle();
            }

        }

        return resourceBundle;
    }

    public int addUserDetails(String[] userList) {
        ArrayList alist = new ArrayList();
        String addUserDetailsQuery = "";
        int userId = -1;
        if (isCompanyValid) {
            addUserDetailsQuery = getResourceBundle().getString("getUserInsertDetailsindicus");
        } else {
            addUserDetailsQuery = getResourceBundle().getString("getUserInsertDetails");
        }



        //INSERT INTO PRG_AR_USERS VALUES (PRG_AR_USERS_ID_SEQ.nextval , '&','&','&', '&','&', '&' , '&', '&',   '&', '&',   '&', '&','&',)

        String finalQuery = "";
        boolean result = false;

        try {
            finalQuery = buildQuery(addUserDetailsQuery, userList);
//            String userType = "";
//            if (isCompanyValid) {
//                userType = userList[14];
//            } else {
//                userType = userList[12];
//            }
//
//            String getUserPrivQ = "select * from user_type_priveleges where user_type=" + userType;


//            PbReturnObject privRetObj = execSelectSQL(getUserPrivQ);
//
//            ArrayList allPrivileges = new ArrayList();
//            for (int g = 0; g < privRetObj.getRowCount(); g++) {
//                allPrivileges.add(privRetObj.getFieldValueString(g, "PRIVILEGE"));
//            }
//            ArrayList insertListPriv = new ArrayList();
//            insertListPriv.add(finalQuery);

//
//            String insertUserPrivileges = "insert into PRG_AR_USER_PRIVELEGES(PUR_ID,USER_ID,PRIVELEGE_ID) values(PRG_AR_USER_PRIVI_SEQ.nextval,PRG_AR_USERS_SEQ.currval,'&')";
//            String userRepPRivQ = "insert into PRG_AR_REPORT_PREVILAGES(PRP_ID,USER_ID,PREVILAGE_NAME) values(PRG_AR_REPORT_PREVILAGES_SEQ.nextval,PRG_AR_USERS_SEQ.currval,'&')";
//            String tableRepPriv = "insert into PRG_AR_REPTAB_PREVILAGES(PRTP_ID,USER_ID,PREVILAGE_NAME) values(PRG_AR_REPTAB_PREVILAGES_SEQ.nextval,PRG_AR_USERS_SEQ.currval,'&')";
//            String regGraphPrev = "insert into prg_ar_repgrp_previlages(PRGP_ID,USER_ID,PREVILAGE_NAME) values(PRG_AR_REPGRP_PREVILAGES_SEQ.nextval,PRG_AR_USERS_SEQ.currval,'&')";

//            for (int m = 0; m < privRetObj.getRowCount(); m++) {
//                Object privObj[] = new Object[1];
//                privObj[0] = privRetObj.getFieldValueString(m, "PRIVILEGE");
//                if (privRetObj.getFieldValueString(m, "PRIVILEGE_TYPE").equalsIgnoreCase("H")) {
//                    String insertUserPrivilegesQry =getResourceBundle().getString("insertUserLevelPrivileges");
//                    String finInsert = buildQuery(insertUserPrivilegesQry, privObj);
//                    insertListPriv.add(finInsert);
//                }
//                if (privRetObj.getFieldValueString(m, "PRIVILEGE_TYPE").equalsIgnoreCase("R")) {
//                    String userRepPRivQry =getResourceBundle().getString("userRepPRivQ");
//                    String finInsert = buildQuery(userRepPRivQry, privObj);
//                    insertListPriv.add(finInsert);
//                }
//                if (privRetObj.getFieldValueString(m, "PRIVILEGE_TYPE").equalsIgnoreCase("T")) {
//                    String tableRepPrivQry =getResourceBundle().getString("tableRepPriv");
//                    String finInsert = buildQuery(tableRepPrivQry, privObj);
//                    insertListPriv.add(finInsert);
//                }
//                if (privRetObj.getFieldValueString(m, "PRIVILEGE_TYPE").equalsIgnoreCase("RG")) {
//                    String regGraphPrevQry =getResourceBundle().getString("regGraphPrev");
//                    String finInsert = buildQuery(regGraphPrevQry, privObj);
//                    insertListPriv.add(finInsert);
//                }
//            }
            // for(int m=0;m<insertListPriv.size();m++)
            // ////.println(" insertListPriv=- "+insertListPriv.get(m));
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                execUpdateSQL(finalQuery);
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                userId = insertAndGetSequenceInMySql(finalQuery, "PRG_AR_USERS", "PU_ID");
            } else {
                userId = insertAndGetSequenceInSQLSERVER(finalQuery, "PRG_AR_USERS");
            }
            //added by susheela over
            //for (int i = 0; i < alist.size(); i++) {
            //////////////////////////////////////////////////////////////////////////////.println(alist.get(i));
            //}
            // result = executeMultiple(alist);
            result = true;
//            return result;
        } catch (Exception exception) {
            logger.error("Exception: ", exception);
//            return result;
        }
        return userId;
    }

    public void deleteUser(String[] userId) throws Exception {
        ArrayList al = new ArrayList();
        for (int i = 0; i < userId.length; i++) {
            String deleteQ = "delete from prg_ar_users  where pu_id in(" + userId[i] + ")";
            String deletePriv = "delete from PRG_USER_TYPE_PRIVILEGE where USER_ID in(" + userId[i] + ")";
            al.add(deleteQ);
            al.add(deletePriv);
        }
        executeMultiple(al);
    }

    public void addUserReports(String userId, String repIds) throws Exception {
        String alreadyRepForUserQ = "select pu.REPORT_ID, ar.REPORT_NAME from prg_ar_user_reports pu,prg_ar_report_master ar where user_id =" + userId + " and pu.report_id= ar.report_id";
        PbReturnObject pbro = execSelectSQL(alreadyRepForUserQ);
        ArrayList al = new ArrayList();
        ArrayList delRep = new ArrayList();
        ArrayList insertList = new ArrayList();
        ArrayList newRepL = new ArrayList();
        for (int u = 0; u < pbro.getRowCount(); u++) {
            al.add(pbro.getFieldValueString(u, "REPORT_ID"));
        }
        // //////.println(" al --.."+al);
        if (repIds.length() > 0) {
            repIds = repIds.substring(1);
        }
        String allRep[] = repIds.split(",");
        //String insertQ = "insert into prg_ar_user_reports(USER_REP_ID,USER_ID,REPORT_ID,PUR_REPORT_SEQUENCE,PUR_FAV_REPORT,PUR_CUST_REPORT_NAME) values(PRG_AR_USER_REPORTS_SEQ.nextval,'&','&','','N','')";
        String insertQ = getResourceBundle().getString("insertQry");
        String deleteQ = getResourceBundle().getString("deleteQry");
        for (int y = 0; y < allRep.length; y++) {
            String rep = allRep[y];
            Object insertObj[] = new Object[2];
            insertObj[0] = userId;
            insertObj[1] = rep;
            if (!al.contains(rep)) {

                String fininsertQ = buildQuery(insertQ, insertObj);
                // //////.println(" fininsertQ "+fininsertQ);
                insertList.add(fininsertQ);
            }

            newRepL.add(rep);
        }
        for (int y = 0; y < al.size(); y++) {
            Object insertObj[] = new Object[2];
            insertObj[0] = userId;
            insertObj[1] = al.get(y).toString();
            if (!newRepL.contains(al.get(y).toString())) {
                String findeleteQ = buildQuery(deleteQ, insertObj);
                // //////.println(" findeleteQ "+findeleteQ);
                insertList.add(findeleteQ);
            }
        }
        try {
            executeMultiple(insertList);
        } catch (Exception d) {
        }
    }

    public String addUserRoleReports(String orgId, String puLoginId1, String AssignReports) {
        String finalQuery = "";
        String Status = "";
        String sql = "";
        try {
            String checkRoleAssignment = getResourceBundle().getString("checkRoleAssignment");
            Object obj[] = new Object[1];
            obj[0] = orgId;
            finalQuery = buildQuery(checkRoleAssignment, obj);
            ////.println("finalQuery---" + finalQuery);
            PbReturnObject pbro = execSelectSQL(finalQuery);
            if (pbro.getRowCount() > 0 && pbro != null) {
                if (!pbro.getFieldValueString(0, 0).equalsIgnoreCase("")) {
                    String checkReportExistance = getResourceBundle().getString("checkReportExistance");
                    obj = new Object[1];
                    obj[0] = orgId;
                    finalQuery = buildQuery(checkReportExistance, obj);
                    PbReturnObject pbro1 = execSelectSQL(finalQuery);
                    ////.println("finalQuery---" + finalQuery);

                    // if (pbro1.getRowCount() > 0 && pbro1 != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        sql = "select ident_current('PRG_AR_USERS')";
                    } else {
                        sql = "select PRG_AR_USERS_SEQ.currval from dual";
                    }
                    PbReturnObject pbro2 = execSelectSQL(sql);
                    String sql1 = "select PU_ID from prg_ar_users where PU_ID=" + pbro2.getFieldValueInt(0, 0) + " and PU_LOGIN_ID='" + puLoginId1 + "'";
                    PbReturnObject pbro3 = execSelectSQL(sql1);
                    String userId = "";
                    ////.println("sql1===" + sql1);
                    if (pbro3.getRowCount() > 0 && pbro3 != null) {
                        userId = String.valueOf(pbro3.getFieldValueInt(0, 0));
                        ArrayList list = new ArrayList();
                        if (AssignReports.equalsIgnoreCase("Y")) {
                            if (pbro1.getRowCount() > 0 && pbro1 != null) {
                                for (int j = 0; j < pbro1.getRowCount(); j++) {
                                    String addReportstoUser = getResourceBundle().getString("addReportstoUser");
                                    obj = new Object[5];
                                    obj[0] = userId;
                                    obj[1] = pbro1.getFieldValueInt(j, 0);
                                    obj[2] = "0";
                                    obj[3] = "N";
                                    obj[4] = "";
                                    finalQuery = buildQuery(addReportstoUser, obj);
                                    list.add(finalQuery);
                                }
                            }
                        }

                        //code to assign role
                        String roleListstr = pbro.getFieldValueString(0, 0);
                        String roleList[] = roleListstr.split(",");

                        String addReportstoUser = getResourceBundle().getString("addUserRole");
                        obj = new Object[3];
                        for (int j = 0; j < roleList.length; j++) {
                            obj[0] = roleList[j];
                            obj[1] = userId;
                            obj[2] = roleList[j];;
                            finalQuery = buildQuery(addReportstoUser, obj);
                            ////.println("finalQuery-insert--" + finalQuery);
                            list.add(finalQuery);
                        }
                        boolean c = executeMultiple(list);

                        if (c == true) {
                            Status = "1";
                        } else {
                            Status = "";
                        }
                    }

                    //} else {
                    //    Status = "";
                    //}
                }
            } else {
                Status = "";
            }
            return Status;
        } catch (Exception j) {
            logger.error("Exception: ", j);
            return Status;
        }

    }

    public static void main(String args[]) {
        UserLayerDAO obj1 = new UserLayerDAO();
        String[] userList = {"k.chendradhar", "srikanth", " ", "k", "xxxx", "xxxx", "k.sreekanth@progenbusiness.com ", "999999999", "# 9-86/3, Temple Alwal", "Secunderabad", "Andhra Pradesh", "INDIA", "500010",};
        obj1.addUserDetails(userList);
        //////////////////////////////////////////////////////////////////////////.println("enter list is "+userList);
    }

    public PbReturnObject sortUserlist(String selval, String seloption) {

        String sortUserlistQuery = getResourceBundle().getString("sortUserlist");
        Object[] objects = new Object[2];
        objects[0] = selval;
        objects[1] = seloption;
        String finalQuery = "";
        PbReturnObject prgr = null;
        try {
            finalQuery = buildQuery(sortUserlistQuery, objects);
            ////.println("finalQuery:::" + finalQuery);
            prgr = execSelectSQL(finalQuery);
        } catch (Exception j) {
            logger.error("Exception: ", j);

        }
        return prgr;
    }

    PbReturnObject useracclistSel(String selval, String seloption) {
        String sortUserlistQuery = getResourceBundle().getString("useracclistSel");
        Object[] objects = new Object[2];
        objects[0] = selval;
        ////.println(" objects[0]" + objects[0]);
        objects[1] = seloption;
        String finalQuery = "";
        PbReturnObject prgr = null;
        try {
            finalQuery = buildQuery(sortUserlistQuery, objects);
            ////.println("finalQuery:::" + finalQuery);
            prgr = execSelectSQL(finalQuery);
        } catch (Exception j) {
            logger.error("Exception: ", j);

        }
        return prgr;
    }

    PbReturnObject useTypelistSel(String selecTypeLValist, String selectedoptyList) {
        String sortUserlistQuery = getResourceBundle().getString("useTypelistSel");
        Object[] objects = new Object[2];
        objects[0] = selecTypeLValist;
        ////.println(" objects[0]" + objects[0]);
        objects[1] = selectedoptyList;
        String finalQuery = "";
        PbReturnObject prgr = null;
        try {
            finalQuery = buildQuery(sortUserlistQuery, objects);
            ////.println("finalQuery:::" + finalQuery);
            prgr = execSelectSQL(finalQuery);
        } catch (Exception j) {
            logger.error("Exception: ", j);

        }

        return prgr;
    }

    public String deleteUserType(String userTypeId) {
        String getUsersbyUserTypeIdQuery = getResourceBundle().getString("getUsersbyUserTypeId");
        String deleteUserTypebyUserTypeIdQuery = getResourceBundle().getString("deleteUserTypebyUserTypeId");
        SuperAdminBd adminBd = new SuperAdminBd();
        PbReturnObject retObj = null;
        PbReturnObject delRetObj = null;
        Object obj[] = new Object[1];
        obj[0] = userTypeId;
        String finalQuery = "";
        String delFinalQuery = "";
        String flag = "";
        try {
            finalQuery = buildQuery(getUsersbyUserTypeIdQuery, obj);

            retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() > 0) {
                flag = "This UserType has assigned Users, you Cannot Delete ";
            } else {
                delFinalQuery = buildQuery(deleteUserTypebyUserTypeIdQuery, obj);
                adminBd.deleteAllPrivilegesToUser(Integer.parseInt(userTypeId));

                execUpdateSQL(delFinalQuery);
                flag = "UserType deleted";
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return flag;
    }

    public void insertUserPrivileges(String puLoginId1, String account, String rowcount) throws Exception {
        PbEncrypter enc = new PbEncrypter();
        String insertUserPrivilegesQry = getResourceBundle().getString("insertUserPrivileges");
        Object Objnew[] = new Object[3];
        Objnew[0] = enc.encrypt(puLoginId1);
        Objnew[1] = enc.encrypt(account);
        Objnew[2] = enc.encrypt(String.valueOf(rowcount));
//               ////.println("rowcount==="+String.valueOf(rowcount));
//        insertUserPrivileges = "insert into prg_role_rows(count_id,USER_ID,company_id,max_count) values(prg_role_rows_seq.nextval,'&','&','&')";
        String finInsert = buildQuery(insertUserPrivilegesQry, Objnew);
        execModifySQL(finInsert);
    }

    public boolean checkLoginId(String loginId) {

        String sqlQuery = getResourceBundle().getString("getUserId");
        PbReturnObject retObj = new PbReturnObject();
        boolean flag = false;
        try {
            retObj = execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (loginId.toUpperCase().trim().equalsIgnoreCase(retObj.getFieldValueString(i, "PU_LOGIN_ID").trim())) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return flag;
    }

    public String getAllUserDetails(String userId) {

        HashMap<String, String> modulesList = new HashMap<String, String>();
        String sqlQuery = getResourceBundle().getString("getModuleNames");
        String[] colNames = null;
        String jsonString = null;
        try {
            PbReturnObject pbro = execSelectSQL(sqlQuery);
            colNames = pbro.getColumnNames();
            for (int i = 0; i < pbro.getRowCount(); i++) {
                modulesList.put(pbro.getFieldValueString(i, colNames[0]), pbro.getFieldValueString(i, colNames[1]));
                Gson gson = new Gson();
                jsonString = gson.toJson(modulesList);
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return jsonString;

    }

    public String getPrivilageDetails(String previlageName, String previlageid) {

        ArrayList privilagelist = new ArrayList();
        HashMap<String, List> hlist = new HashMap<String, List>();
        String privilagestr = "";
        String privilageDetailsQuery = "";
        String jsonString = null;
        privilageDetailsQuery = getResourceBundle().getString("getPrivilageData");
        Object[] obj = new Object[2];
        obj[0] = previlageid;
        String finalBuildQuery = "";
        finalBuildQuery = buildQuery(privilageDetailsQuery, obj);
        String[] colNames = null;
        try {
            PbReturnObject pbretobj = execSelectSQL(finalBuildQuery);
            colNames = pbretobj.getColumnNames();
            for (int i = 0; i < pbretobj.getRowCount(); i++) {
                List<String> wordList = Arrays.asList(pbretobj.getFieldValueString(i, colNames[3]).split(","));
                hlist.put(pbretobj.getFieldValueString(i, colNames[2]), wordList);
            }
//                
            Gson gson = new Gson();
            jsonString = gson.toJson(hlist);

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        return jsonString;
    }

    public HashMap getUserIdsAndNames(String moduleName) {
        HashMap userIdsandNames = new HashMap();
        String query = null;
        List<String> userids = new ArrayList<String>();
        List<String> usernames = new ArrayList<String>();
        PbReturnObject returnObject = null;
        if (moduleName.equalsIgnoreCase("Portal Assignment")) {
            query = getResourceBundle().getString("getPortalUserData");
        }
        if (moduleName.equalsIgnoreCase("headlineAssignment")) {
            query = getResourceBundle().getString("getHeadlineUserData");
        }
        if (moduleName.equalsIgnoreCase("whatifassignment")) {
            query = getResourceBundle().getString("getWhatifUserData");
        }
        if (moduleName.equalsIgnoreCase("QueryStudio Assignment")) {
            query = getResourceBundle().getString("getQueryStudioUserData");
        }
        if (moduleName.equalsIgnoreCase("PowerAnalyzer Assignment")) {
            query = getResourceBundle().getString("getPowerAnalyzerUserData");
        }
        if (moduleName.equalsIgnoreCase("One View Assignment")) {
            query = getResourceBundle().getString("getOneViewAssignmentUserData");
        }
        if (moduleName.equalsIgnoreCase("Scorecards Assignment")) {
            query = getResourceBundle().getString("getScorecardsUserData");
        }
        if (moduleName.equalsIgnoreCase("CreateGroup")) {
            query = getResourceBundle().getString("getAllUsers");
        }
        if (moduleName.equalsIgnoreCase("editGroup")) {
            query = getResourceBundle().getString("getAllUsers");
        }
        if (moduleName.equalsIgnoreCase("Groups")) {
            query = getResourceBundle().getString("getAllGroups");
        }
        if (moduleName.equalsIgnoreCase("SuperAdmin Assignment")) {
            query = getResourceBundle().getString("getSuperAdminUserData");
        }
        try {
            returnObject = super.execSelectSQL(query);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                userids.add(returnObject.getFieldValueString(i, 0));
                usernames.add(returnObject.getFieldValueString(i, 1));
            }
            userIdsandNames.put("userids", userids);
            userIdsandNames.put("usernames", usernames);


        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }


        return userIdsandNames;

    }

    public HashMap getAssignedUsers(String moduleName) {
        HashMap assignedUserdets = new HashMap();
        ArrayList<String> memAssignedIds = new ArrayList<String>();
        ArrayList<String> memAssignedNames = new ArrayList<String>();
        String query = null;
        String finalquery = null;
        PbReturnObject retObj = null;
        if (moduleName.equalsIgnoreCase("Portal Assignment")) {
            query = getResourceBundle().getString("getUsersAssignedforportalviewer");
        }
        if (moduleName.equalsIgnoreCase("headlineAssignment")) {
            query = getResourceBundle().getString("getUsersAssignedforheadline");
        }
        if (moduleName.equalsIgnoreCase("whatifassignment")) {
            query = getResourceBundle().getString("getUsersAssignedforwhatif");
        }
        if (moduleName.equalsIgnoreCase("QueryStudio Assignment")) {
            query = getResourceBundle().getString("getUsersAssignedforQueryStudio");
        }
        if (moduleName.equalsIgnoreCase("PowerAnalyzer Assignment")) {
            query = getResourceBundle().getString("getUsersAssignedforPowerAnalyzer");
        }
        if (moduleName.equalsIgnoreCase("One View Assignment")) {
            query = getResourceBundle().getString("getUsersAssignedforOneView");
        }
        if (moduleName.equalsIgnoreCase("Scorecards Assignment")) {
            query = getResourceBundle().getString("getUsersAssignedforScorecards");
        }
        if (moduleName.equalsIgnoreCase("SuperAdmin Assignment")) {
            query = getResourceBundle().getString("getSuperAdmin");
        }

//        Object[] obj = new Object[1];
//        obj[0] = moduleName;
        try {
            retObj = super.execSelectSQL(query);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                memAssignedIds.add(retObj.getFieldValueString(i, 0));
                memAssignedNames.add(retObj.getFieldValueString(i, 1));
            }
            assignedUserdets.put("memAssignedIds", memAssignedIds);
            assignedUserdets.put("memAssignedNames", memAssignedNames);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }

        return assignedUserdets;
    }

    public void updateUserModuleAssignment(String[] userIds, String userNames, String modulename) {

        Object[] obj = new Object[1];
        String query = null;
        String query1 = null;
        if (modulename != null && modulename.equalsIgnoreCase("Portal Assignment")) {
            query1 = getResourceBundle().getString("resetPortalAssignment");
            query = getResourceBundle().getString("updatePortalAssignment");
        }
        if (modulename != null && modulename.equalsIgnoreCase("headlineAssignment")) {
            query = getResourceBundle().getString("updateHeadlinesAssignment");
        }
        if (modulename != null && modulename.equalsIgnoreCase("whatifassignment")) {
            query = getResourceBundle().getString("updatewhatifAssignment");
        }
        if (modulename != null && modulename.equalsIgnoreCase("QueryStudio Assignment")) {
            query1 = getResourceBundle().getString("resetQueryStudioAssignment");
            query = getResourceBundle().getString("updateQueryStudioAssignment");
        }
        if (modulename != null && modulename.equalsIgnoreCase("PowerAnalyzer Assignment")) {
            query1 = getResourceBundle().getString("resetPowerAnalyzerAssignment");
            query = getResourceBundle().getString("updatePowerAnalyzerAssignment");
        }
        if (modulename != null && modulename.equalsIgnoreCase("One View Assignment")) {
            query1 = getResourceBundle().getString("resetOneViewAssignment");
            query = getResourceBundle().getString("updateOneViewAssignment");
        }
        if (modulename != null && modulename.equalsIgnoreCase("Scorecards Assignment")) {
            query1 = getResourceBundle().getString("resetScorecardsAssignment");
            query = getResourceBundle().getString("updateScorecardsAssignment");
        }
        if (modulename != null && modulename.equalsIgnoreCase("SuperAdmin Assignment")) {
            query1 = getResourceBundle().getString("resetSuperAdminAssignment");
            query = getResourceBundle().getString("updateSuperAdminAssignment");
        }
        String[] userNamesarr = userNames.split(",");
        ArrayList<String> users = new ArrayList<String>();
        for (int i = 0; i < userNamesarr.length; i++) {
            users.add(userNamesarr[i]);
        }
        String tempusernames = users.toString().trim();
        String usernames = tempusernames.replace("[", "'");
        String user = usernames.replace("]", "'");
        user = user.trim();

        obj[0] = Joiner.on("','").join(user.split(",")).toString().replaceAll("\\s+", "");;
        String finalQry = super.buildQuery(query, obj);
        try {
            int i = execUpdateSQL(query1);
            super.execModifySQL(finalQry);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public ArrayList getUsersList(String status) {
        ArrayList<String> activeUsers = new ArrayList<String>();
        ArrayList<String> inactiveUsers = null;
        String activeusers = null;
        String inactiveusers = null;
        activeusers = getResourceBundle().getString("getActiveUsers");
        inactiveusers = getResourceBundle().getString("getInActiveUsers");
        PbReturnObject retObj = null;
        if (status.equalsIgnoreCase("activateUser")) {

            try {
                retObj = super.execSelectSQL(activeusers);
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    activeUsers.add(retObj.getFieldValueString(i, 0));
                }
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
        }
        if (status.equalsIgnoreCase("inactivateUser")) {
            try {
                retObj = super.execSelectSQL(inactiveusers);
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    activeUsers.add(retObj.getFieldValueString(i, 0));
                }
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
        }

        return activeUsers;
    }

    public HashMap getUsersMap(String status) {
        HashMap UserIdsNnames = new HashMap();
        String inactiveusersqry = null;
        String activeusersqry = null;
        ArrayList userIds = new ArrayList();
        ArrayList usernames = new ArrayList();
        inactiveusersqry = getResourceBundle().getString("getInActiveUserIdsNnames");
        activeusersqry = getResourceBundle().getString("getActiveUserIdsNnames");
        PbReturnObject retObj = null;
        if (status.equalsIgnoreCase("activateUser")) {
            try {
                retObj = super.execSelectSQL(inactiveusersqry);
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    userIds.add(retObj.getFieldValueString(i, 0));
                    usernames.add(retObj.getFieldValueString(i, 1));
                }
                UserIdsNnames.put("userIds", userIds);
                UserIdsNnames.put("usernames", usernames);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
        }
        if (status.equalsIgnoreCase("inactivateUser")) {
            try {
                retObj = super.execSelectSQL(activeusersqry);
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    userIds.add(retObj.getFieldValueString(i, 0));
                    usernames.add(retObj.getFieldValueString(i, 1));
                }
                UserIdsNnames.put("userIds", userIds);
                UserIdsNnames.put("usernames", usernames);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
        }
        return UserIdsNnames;
    }

    public void updateUserStatus(String status, String[] userIds, String userNames) {
        String updateActiveUsers = null;
        String updateInActiveUsers = null;
        String finalQry = null;
        Object[] obj = new Object[1];
        updateActiveUsers = getResourceBundle().getString("updateActiveUsers");
        updateInActiveUsers = getResourceBundle().getString("updateInActiveUsers");
        String[] userNamesarr = userNames.split(",");
        ArrayList<String> users = new ArrayList<String>();
        for (int i = 0; i < userNamesarr.length; i++) {
            users.add(userNamesarr[i]);
        }
        String tempusernames = users.toString().trim();
        String usernames = tempusernames.replace("[", "'");
        String user = usernames.replace("]", "'");
        user = user.trim();

        obj[0] = Joiner.on("','").join(user.split(",")).toString().replaceAll("\\s+", "");;
        if (status.equalsIgnoreCase("activateUser")) {
            finalQry = super.buildQuery(updateActiveUsers, obj);
        }
        if (status.equalsIgnoreCase("inactivateUser")) {
            finalQry = super.buildQuery(updateInActiveUsers, obj);
        }
        try {
            super.execModifySQL(finalQry);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }


    }

    public boolean validateUserActivation(String userNames) {
        boolean validate = false;
        PbReturnObject retObj = null;
        String[] usernames = userNames.split(",");
        int newUsers = usernames.length;
        String query = "SELECT ANALYZERS from PRG_TEST_DATA";
        try {
            retObj = super.execSelectSQL(query);
            int existingUsers = Integer.parseInt(retObj.getFieldValueString(0, 0));
            if (newUsers <= existingUsers) {
                validate = true;
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
            validate = false;
        }


        return validate;

    }

    public boolean validateUserAssignment(String userNames, String modulename, HttpServletRequest request) {
        boolean validate = false;
        PbReturnObject retObj = null;
        String query = "";
        String[] usernames = userNames.split(",");
        int newUsers = usernames.length;
        int existingAssignment = 0;
//      if(modulename.equalsIgnoreCase("portalAssignment"))
//       query = "SELECT PORTALS_VIEWER from PRG_TEST_DATA";
//      if(modulename.equalsIgnoreCase("headlineAssignment"))
//       query = "SELECT HEADLINES from PRG_TEST_DATA";
//      if(modulename.equalsIgnoreCase("whatifassignment"))
//        query = "SELECT What_if_Analysis from PRG_TEST_DATA";
        UserStatusHelper helper = UserStatusHelper.getUserStatusHelper();
        if (helper == null) {
            Properties helperProps = new Properties();
            InputStream servletStream = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/GenerateAssignModule.xml");
            if (servletStream != null) {
                try {
                    helperProps.loadFromXML(servletStream);
                    helper.createUserStatusHelper(helperProps);
                    helper = UserStatusHelper.getUserStatusHelper();
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                }
            }
        }

        if (modulename.equalsIgnoreCase("Portal Assignment")) {
            existingAssignment = helper.getPortalsTotalCnt();
        }
        if (modulename.equalsIgnoreCase("QueryStudio Assignment")) {
            existingAssignment = helper.getQueryStudionTotalCnt();
        }
        if (modulename.equalsIgnoreCase("PowerAnalyzer Assignment")) {
            existingAssignment = helper.getPowerAnalyzerTotalCnt();
        }
        if (modulename.equalsIgnoreCase("One View Assignment")) {
            existingAssignment = helper.getOneViewTotalCnt();
        }
        if (modulename.equalsIgnoreCase("Scorecards Assignment")) {
            existingAssignment = helper.getScorecardsTotalCnt();
        }
        if (modulename.equalsIgnoreCase("SuperAdmin Assignment")) {
            existingAssignment = helper.getSuperAdmin();
        }
        if (newUsers <= existingAssignment) {
            validate = true;
        } else {
            validate = false;
        }
//        try {
//            retObj = super.execSelectSQL(query);
//            int existingAssignment = Integer.parseInt(retObj.getFieldValueString(0, 0));
//            if(newUsers<=existingAssignment){
//               validate = true;
//            }
//
//        } catch (SQLException ex) {
//               validate = false;
//            logger.error("Exception: ",ex);
//        }
//

        return validate;

    }
    //Modify By Ashutosh

    public boolean checkusertype(String usertype, String Userid, String oldusertype) throws Exception {
        PbReturnObject retObj = null;
        boolean testfor = false;
        ArrayList list = new ArrayList();
//           int Usertype=(Integer.parseInt(usertype));
        String query = "";
        String query1 = null;
        // query="select USER_TYPE from prg_ar_users where USER_TYPE="+Usertype+"";
        try {
            if (!usertype.equalsIgnoreCase(oldusertype)) {
                if (oldusertype.equalsIgnoreCase("Admin")) {
                    query = "update PRG_USER_ASSIGNMENTS set QUERY_STUDIO='N' where USER_ID='" + Userid + "'";
                } else if (oldusertype.equalsIgnoreCase("Power Analyzer")) {
                    query = "update PRG_USER_ASSIGNMENTS set POWER_ANALYZER='N' where USER_ID='" + Userid + "'";
                } else {
                    query = "update PRG_USER_ASSIGNMENTS set POWER_ANALYZER='N',QUERY_STUDIO='N' where USER_ID='" + Userid + "'";
                }
                execUpdateSQL(query);
            }
            if (usertype.equalsIgnoreCase("Admin")) {
//                  query1="update PRG_USER_ASSIGNMENTS set QUERY_STUDIO='Y' where USER_ID='"+Userid+"'";
                query1 = "update PRG_USER_ASSIGNMENTS set ANALYZER='N',ADMIN='Y',REPORT_STUDIO='Y',DASHBOARD_STUDIO='Y',USER_TYPE='9999',POWER_ANALYZER='Y',QUERY_STUDIO='Y',ONE_VIEW='Y',RESTRICTED_POWER_ANALYZER='N' where USER_ID="+Userid;
            } else if (usertype.equalsIgnoreCase("Power Analyzer")) {
//                     query1="update PRG_USER_ASSIGNMENTS set POWER_ANALYZER='Y' where USER_ID='"+Userid+"'";
                query1 = "update PRG_USER_ASSIGNMENTS set ANALYZER='N',ADMIN='N',REPORT_STUDIO='Y',DASHBOARD_STUDIO='Y',USER_TYPE='10000',POWER_ANALYZER='Y',QUERY_STUDIO='N',ONE_VIEW='Y',RESTRICTED_POWER_ANALYZER='N' where USER_ID="+Userid;
            } else if (usertype.equalsIgnoreCase("Restricted Power Analyzer")) {
                query1 = "update PRG_USER_ASSIGNMENTS set ANALYZER='N',ADMIN='N',REPORT_STUDIO='Y',DASHBOARD_STUDIO='Y',USER_TYPE='10001',POWER_ANALYZER='Y',QUERY_STUDIO='N',ONE_VIEW='Y',RESTRICTED_POWER_ANALYZER='Y' where USER_ID="+Userid;
            } else if (usertype.equalsIgnoreCase("Analyzer")) {
                query1 = "update PRG_USER_ASSIGNMENTS set ANALYZER='Y',ADMIN='N',REPORT_STUDIO='N',DASHBOARD_STUDIO='N',USER_TYPE='10002',POWER_ANALYZER='N',QUERY_STUDIO='N',ONE_VIEW='N',RESTRICTED_POWER_ANALYZER='N' where USER_ID="+Userid;
            }

            if (query1 != null) {
                execUpdateSQL(query1);
            }
            if (!usertype.equalsIgnoreCase(oldusertype)) {
                testfor = true;
            } else {
                testfor = false;
            }
//                retObj=super.execSelectSQL(query);
//
//                if(retObj!=null){
//                for(int i=0;i<retObj.getRowCount();i++)
//                {
//                    list.add(Integer.parseInt(retObj.getFieldValueString(i, 0)));
//                }
//                }
//                if(list.contains(Integer.parseInt("9999"))){
//                    testfor=false;
//                }
//                else{
//                    query1="update PRG_AR_USERS set user_type="+usertype+" where PU_LOGIN_ID='"+Userid+"'";
//                    execUpdateSQL(query1);
//                    testfor=true;
//                }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return testfor;
    }

    public PbReturnObject getRoleRelatedReports(String folderId) {
        PbReturnObject retObj = new PbReturnObject();
        try {
            if (!folderId.equalsIgnoreCase("select")) {
                String qry = "select rd.REPORT_ID, rm.REPORT_NAME from prg_ar_report_details rd,prg_ar_report_master rm where folder_id in(" + folderId + ") and rd.report_id= rm.report_id";
                retObj = super.execSelectSQL(qry);
            }

        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return retObj;
    }
//by gopesh

    public void saveNewGroup(String userIds, String userNames, String groupName, String[] userId, String[] userName) throws Exception {
        PbReturnObject retObj = new PbReturnObject();
        String query = null;
        String query1 = null;
        int sequence = 0;

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            String selectQuer = "select PRG_AR_LOGICGROUP_MASTER_SEQ.nextval from dual";
            retObj = super.executeSelectSQL(selectQuer);
            sequence = Integer.parseInt(retObj.getFieldValueString(0, 0));
            query = "Insert into PRG_AR_LOGICGROUP_MASTER(logic_group_id,group_Name) VALUES(" + sequence + ",'" + groupName + "')";
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            String selectQuer = "select LAST_INSERT_ID(LOGIC_GROUP_ID) from PRG_AR_LOGICGROUP_MASTER order by 1 desc limit 1";
            retObj = super.executeSelectSQL(selectQuer);
            sequence = Integer.parseInt(retObj.getFieldValueString(0, 0)) + 1;
            query = "Insert into PRG_AR_LOGICGROUP_MASTER(group_Name) VALUES('" + groupName + "')";
        } else {
            String selectQuer = "select IDENT_CURRENT('PRG_AR_LOGICGROUP_MASTER')";
            retObj = super.executeSelectSQL(selectQuer);
            sequence = Integer.parseInt(retObj.getFieldValueString(0, 0));
            query = "Insert into PRG_AR_LOGICGROUP_MASTER(group_Name) VALUES('" + groupName + "')";
        }

        try {
            super.execInsert(query);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        for (int i = 0; i < userId.length; i++) {
            query1 = "Insert into PRG_AR_LOGICGROUP_DETAILS(logic_group_id,user_IdS,user_Names) VALUES(" + sequence + ",'" + userId[i] + "','" + userName[i] + "')";
            try {
                super.execInsert(query1);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
        }
    }
    //by gopesh

    public HashMap getAssignedUsersForGroup(String groupId) {
        HashMap assignedUserdets = new HashMap();
        ArrayList<String> memAssignedIds = new ArrayList<String>();
        ArrayList<String> memAssignedNames = new ArrayList<String>();
        String query = null;
        PbReturnObject retObj = null;

        query = "SELECT * from PRG_AR_LOGICGROUP_DETAILS where LOGIC_GROUP_ID='" + groupId + "'";
        try {
            retObj = super.execSelectSQL(query);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                memAssignedIds.add(retObj.getFieldValueString(i, 2));
                memAssignedNames.add(retObj.getFieldValueString(i, 1));
            }
            assignedUserdets.put("memAssignedIds", memAssignedIds);
            assignedUserdets.put("memAssignedNames", memAssignedNames);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
        return assignedUserdets;
    }
    //by gopesh

    public void editGroupDetails(String userIds, String userNames, String groupId, String[] userId, String[] userName, String groupName) throws Exception {
        String query = null;
        String query1 = null;
        String query2 = null;
        query = "delete from PRG_AR_LOGICGROUP_DETAILS where LOGIC_GROUP_ID='" + groupId + "'";
        query2 = "update PRG_AR_LOGICGROUP_MASTER set GROUP_NAME='" + groupName + "' where LOGIC_GROUP_ID=" + groupId;
        super.execModifySQL(query);
        super.execModifySQL(query2);
        for (int i = 0; i < userId.length; i++) {
            query1 = "Insert into PRG_AR_LOGICGROUP_DETAILS(logic_group_id,user_IdS,user_Names) VALUES(" + groupId + ",'" + userId[i] + "','" + userName[i] + "')";
            super.execInsert(query1);

        }
    }
    //by gopesh

    public HashMap getParamSelectedList(String userType) throws Exception {
        String query = null;
        PbReturnObject retObj = null;
        HashMap paramhashmap = new HashMap();

        ArrayList paramSelectedListPA = new ArrayList();
        ArrayList paramSelectedListPPA = new ArrayList();
        ArrayList paramSelectedListCCA = new ArrayList();
        ArrayList paramSelectedListCCPA = new ArrayList();
        query = "select * from PRG_AR_FEATURE_ASSIGNMENT ORDER BY SEQUENCE";
        PbDb pbdb = new PbDb();
        retObj = pbdb.execSelectSQL(query);
        int rowCount = retObj.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            paramSelectedListPA.add(Boolean.parseBoolean(retObj.getFieldValueString(i, 5).trim()));
            paramSelectedListPPA.add(Boolean.parseBoolean(retObj.getFieldValueString(i, 6).trim()));
            if (userType.equalsIgnoreCase("ADMIN")) {
                paramSelectedListCCA.add(Boolean.parseBoolean(retObj.getFieldValueString(i, 7).trim()));
                paramSelectedListCCPA.add(Boolean.parseBoolean(retObj.getFieldValueString(i, 8).trim()));
            }
        }
        paramhashmap.put("paramSelectedListPA", paramSelectedListPA);
        paramhashmap.put("paramSelectedListPPA", paramSelectedListPPA);
        paramhashmap.put("paramSelectedListCCA", paramSelectedListCCA);
        paramhashmap.put("paramSelectedListCCPA", paramSelectedListCCPA);

        return paramhashmap;
    }
    //by gopesh

    public HashMap getFeatureListAnaLyzer(String userType, int USERID) throws Exception {
        String query = null;
        String query1 = null;
        PbReturnObject retObj = null;
        String typeArrr = null;
        query = "select * from PRG_AR_FEATURE_ASSIGNMENT";
        PbDb pbdb = new PbDb();
        int returncolumn = 0;
        if (userType.equalsIgnoreCase("GROUPUSER")) {
            query1 = "select * from PRG_AR_LOGICGROUP_DETAILS where USER_IDS=" + USERID;
            PbReturnObject retObj1 = null;
            retObj1 = pbdb.execSelectSQL(query1);
            int groupId = retObj1.getFieldValueInt(0, 0);
            String query2 = "select * from PRG_AR_GROUP_FEATURE_ASSIGN where GROUP_ID=" + groupId;
            retObj = pbdb.execSelectSQL(query2);
            int rowCount = retObj.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                featureHashMap.put(retObj.getFieldValueString(i, 5), Boolean.parseBoolean(retObj.getFieldValueString(i, 6).trim()));
            }
        } else {
            if (userType.equalsIgnoreCase("SUPERADMIN")) {
                returncolumn = 6;
            } else if (userType.equalsIgnoreCase("ADMIN")) {
                returncolumn = 6;
            } else if (userType.equalsIgnoreCase("POWERANALYZER")) {
                returncolumn = 10;
            } else if (userType.equalsIgnoreCase("ANALYZER")) {
                returncolumn = 9;
            }

            retObj = pbdb.execSelectSQL(query);
            int rowCount = retObj.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                featureHashMap.put(retObj.getFieldValueString(i, 4).trim(), Boolean.parseBoolean(retObj.getFieldValueString(i, returncolumn).trim()));
            }
        }
//    else{
//    query1 = "select * from PRG_AR_LOGICGROUP_DETAILS where USER_IDS="+USERID;
//    PbReturnObject retObj1=null;
//    retObj1=pbdb.execSelectSQL(query1);
//    int groupId =  retObj1.getFieldValueInt(0, 0);
//    String query2 =  "select * from PRG_AR_GROUP_FEATURE_ASSIGN where GROUP_ID="+groupId;
//    retObj = pbdb.execSelectSQL(query2);
//    int rowCount = retObj.getRowCount();
//    for(int i=0;i<rowCount;i++){
//    featureHashMap.put(retObj.getFieldValueString(i, 5), Boolean.parseBoolean(retObj.getFieldValueString(i, 6).trim()));
//        }
//         }
        return featureHashMap;
    }

    public boolean getFeatureEnable(String feature) throws Exception {
//         if(featureHashMap.isEmpty()){
//         return false;
//         }else{
//         boolean checkcon =(Boolean)featureHashMap.get(feature);
//         return checkcon;
//         }
        //modified by Nazneen
        if (featureHashMap.isEmpty()) {
            return false;
        } else {
            if (featureHashMap.containsKey(feature)) {
                boolean checkcon = (Boolean) featureHashMap.get(feature);
                return checkcon;
            } else {
                return false;
            }
        }
    }

    public boolean getFeatureEnableHashMap(String feature, HashMap paramHashMap) throws Exception {
        if (paramHashMap.isEmpty()) {
            return false;
        } else {
            boolean checkcon = (Boolean) paramHashMap.get(feature);
            return checkcon;
        }
    }
    //by gopesh

    public String getUserTypeForFeatures(int userId) throws Exception {
        String query = null;
        String query1 = null;
        String query2 = null;
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        PbReturnObject retObj2 = null;
        PbDb pbdb = new PbDb();
        String groupUser = null;
        query = "select * from PRG_USER_ASSIGNMENTS where USER_ID=" + userId;
        retObj = pbdb.execSelectSQL(query);
        query1 = "select * from PRG_AR_LOGICGROUP_DETAILS where USER_IDS=" + userId;
        retObj1 = pbdb.execSelectSQL(query1);
        int groupId =0;
        if(retObj1!=null && retObj1.rowCount>0){
            groupId = retObj1.getFieldValueInt(0, 0);
        }
        if (groupId > 0) {
            query2 = "select * from PRG_AR_GROUP_FEATURE_ASSIGN where GROUP_ID=" + groupId;
            retObj2 = pbdb.execSelectSQL(query2);
            //ResultSet rs = (ResultSet) pbdb.execSelectSQL(query2);
            if (retObj2 != null) {
                groupUser = retObj2.getFieldValueString(0, 0);
            }
        }
        if (retObj.getFieldValueString(0, 1).equalsIgnoreCase("PROGEN")) {
            return "SUPERADMIN";
        } else if (retObj.getFieldValueString(0, 12).equalsIgnoreCase("Y")) {
            return "ADMIN";
        } else if (retObj2 != null) {
            return "GROUPUSER";
        } else if (retObj.getFieldValueString(0, 17).equalsIgnoreCase("Y")) {
            return "POWERANALYZER";
        } else if (retObj.getFieldValueString(0, 2).equalsIgnoreCase("Y")) {
            return "ANALYZER";
        } else if (retObj.getFieldValueString(0, 20).equalsIgnoreCase("Y")) {
            return "RESTRICTED POWER ANALYZER";   //Added by Ashutosh for Restricted PowerAnalyzer 10-12-2015
        } else {
            return null;
        }
    }
}
