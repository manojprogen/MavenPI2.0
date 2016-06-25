package com.progen.account.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class PbOrganisationDAO extends PbDb {

    public static Logger logger = Logger.getLogger(PbOrganisationDAO.class);
//    PbOrgResourceBundle resbundle = new PbOrgResourceBundle();
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbOrgResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbOrgResBundleMySql();
            } else {
                resourceBundle = new PbOrgResourceBundle();
            }

        }

        return resourceBundle;
    }

    public void addOrganisation(String orgname, String orgdesc, String enddate, String accountFolderId) throws Exception {
        String insertOrgMaster = getResourceBundle().getString("insertOrgMaster");
        String sequenceQuery = getResourceBundle().getString("getSequenceNumber");
        String compInsertRepQry = getResourceBundle().getString("compInsertRep");
        ArrayList al = new ArrayList();
        String insertQuery = "";
        int seq_id = 0;
        String timeQ = "";
        String fincompInsertRep = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            timeQ = "select convert(varchar,convert(datetime,'" + enddate + "',101),106)";
            PbReturnObject pbro = execSelectSQL(timeQ);
            String insertEndDate = pbro.getFieldValueString(0, 0);

            Object insertObj[] = new Object[3];
            insertObj[0] = orgname;
            insertObj[1] = orgdesc;
            insertObj[2] = insertEndDate;
//            insertObj[3] = Integer.parseInt(accountFolderId);
            insertQuery = buildQuery(insertOrgMaster, insertObj);
        } else {
            Object seqObj[] = new Object[1];
            seqObj[0] = "ORG_MASTER_SEQ";
            String seqQuery = buildQuery(sequenceQuery, seqObj);
            seq_id = getSequenceNumber(seqQuery);

            timeQ = "select to_char(to_date('" + enddate + "','mm-dd-yy'),'dd-MON-yyyy') from dual";
            PbReturnObject pbro = execSelectSQL(timeQ);
            String insertEndDate = pbro.getFieldValueString(0, 0);

            Object insertObj[] = new Object[5];
            insertObj[0] = Integer.valueOf(seq_id);
            insertObj[1] = orgname;
            insertObj[2] = orgdesc;
            insertObj[3] = insertEndDate;
            insertObj[4] = accountFolderId;
            insertQuery = buildQuery(insertOrgMaster, insertObj);
        }

        String repForRole = "select * from prg_ar_report_details where folder_id=" + accountFolderId;
        if (accountFolderId != null && !accountFolderId.equalsIgnoreCase("")) {
            PbReturnObject allRepObj = execSelectSQL(repForRole);
//            String compInsertRep = "insert into account_report(ORG_ID, REPORT_ID) values('&','&')";
            if (allRepObj.getRowCount() > 0) {
                String repId = "";
                for (int m = 0; m < allRepObj.getRowCount(); m++) {
                    repId = allRepObj.getFieldValueString(m, "REPORT_ID");
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        Object inObj[] = new Object[1];
                        inObj[0] = repId;
                        fincompInsertRep = buildQuery(compInsertRepQry, inObj);
                    } else {
                        Object inObj[] = new Object[2];
                        inObj[0] = seq_id;
                        inObj[1] = repId;
                        fincompInsertRep = buildQuery(compInsertRepQry, inObj);
                    }
                    al.add(fincompInsertRep);
                }
            }
        }

        //.println("insertquery is : " + insertQuery);

        al.add(insertQuery);
        try {
            executeMultiple(al);
        } catch (Exception s) {
            logger.error("Exception:", s);
        }
    }

    public PbReturnObject dispUserDetails() throws Exception {
        String dispUserQuery = getResourceBundle().getString("dispUserQuery");
        PbReturnObject dispUserpbro = new PbReturnObject();
        dispUserpbro = execSelectSQL(dispUserQuery);
        return dispUserpbro;

    }

    public PbReturnObject alreadyAssignedUser(String accountId) throws Exception {
        String dispUserQuery = getResourceBundle().getString("getAssignedUser");
        PbReturnObject dispUserpbro = new PbReturnObject();
        Object s[] = new Object[1];
        s[0] = accountId;
        String findispUserQuery = buildQuery(dispUserQuery, s);
        dispUserpbro = execSelectSQL(findispUserQuery);
        return dispUserpbro;

    }

    public String getAccountTypes() {
        String id = "";
        String name = "";
        String str = "";
        try {
            PbReturnObject retObj = new PbReturnObject();
            String getAccountTypes = getResourceBundle().getString("getAccountTypes");
            retObj = execSelectSQL(getAccountTypes);
            int num = retObj.getRowCount();
            for (int i = 0; i < num; i++) {
                if (str.equalsIgnoreCase("") || "".equalsIgnoreCase(str)) {
                    str = String.valueOf(retObj.getFieldValueInt(i, 0)) + "~" + retObj.getFieldValueString(i, 1);
                } else {
                    str = str + ";" + String.valueOf(retObj.getFieldValueInt(i, 0)) + "~" + retObj.getFieldValueString(i, 1);
                }
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return str;

    }

    public PbReturnObject getAccountNames() {
        PbReturnObject retObj = new PbReturnObject();
        String id = "";
        String name = "";
        String str = "";
        String getAccountTypes1 = getResourceBundle().getString("getAccountTypes");
        try {
            // String getAccountTypes = getResourceBundle().getString("getAccountTypes");
            retObj = execSelectSQL(getAccountTypes1);
            int num = retObj.getRowCount();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return retObj;

    }

    public void expireOrg(String orgId) {
        String expireAccount = getResourceBundle().getString("expireAccount");
        String expireUserQ = "update prg_ar_users set pu_end_date=sysdate where account_type=" + orgId;
        Object org[] = new Object[1];
        org[0] = orgId;
        String finexpireAccount = buildQuery(expireAccount, org);
        ArrayList al = new ArrayList();
        //ArrayList al2=new ArrayList();
        al.add(finexpireAccount);
        al.add(expireUserQ);
        try {
            executeMultiple(al);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public String checkExpireOrg(String orgId) {
        String status = null;
        try {
            // String checkexpiredacnt = resbundle.getString("chkexpireuser");
            String chkexpireorg = getResourceBundle().getString("chkexpireorg");
            Object org[] = new Object[1];
            org[0] = orgId;
            String finexpireAccount = buildQuery(chkexpireorg, org);
            PbDb pbdb = new PbDb();
            PbReturnObject pbretobj = new PbReturnObject();
            pbretobj = pbdb.execSelectSQL(finexpireAccount);
            String enddate = pbretobj.getFieldValueString(0, 0);
            if (enddate.contains(".")) {
                enddate = enddate.substring(0, enddate.indexOf('.'));
            }
            if (Integer.parseInt(enddate) > 0) {
                status = enddate;
            } else {
                status = null;
            }
            /*
             * status = enddate; enddatearr = enddate.split("-"); if
             * (Integer.parseInt(enddatearr[0]) == year &&
             * Integer.parseInt(enddatearr[1]) == month &&
             * Integer.parseInt(enddatearr[2]) == day) { status = enddate; //
             * finexpireAccount = buildQuery(checkexpiredacnt,org); // pbretobj
             * = pbdb.execSelectSQL(finexpireAccount); // enddate =
             * pbretobj.getFieldValueString(0, 0); // if(enddate!=null){ //
             * //.println("enddate innner"+enddate); // }else{ //
             * //.println("There is no system end date"); // } /* else { status
             * = null;
            }
             */
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return status;
    }

    public HashMap checkMemberValueStatusForAccount(String orgId, String memberId) throws Exception {
        String status = "";
        HashMap details = new HashMap();
        String addedDataQ = "select * from prg_account_member_filter where member_id=" + memberId + " and account_no=" + orgId;
        PbReturnObject pbro = execSelectSQL(addedDataQ);
        String filterId = "";
        if (pbro.getRowCount() > 0) {
            status = "true";
            filterId = pbro.getFieldValueString(0, "ACC_FILTER_ID");
        } else {
            status = "false";
        }
        details.put("status", status);
        details.put("filterId", filterId);
        return details;
    }

    public void addUserMemberFilterForAcc(String orgId, String memberValue, String memberId) throws Exception {

        String addAccMemberFilter = "insert into prg_account_member_filter(ACC_FILTER_ID,MEMBER_ID,MEMBER_VALUE,ACCOUNT_NO) values(ACCOUNT_FILTER_ID_SEQ.nextval,?,?,?)";
        //resBundle.getString("addUserMemberFilter");
        OraclePreparedStatement opstmt = null;
        Connection connection = ProgenConnection.getInstance().getConnection();

        opstmt = (OraclePreparedStatement) connection.prepareStatement(addAccMemberFilter);
        opstmt.setString(1, memberId);
        opstmt.setStringForClob(2, memberValue);
        opstmt.setString(3, orgId);
        int rows = opstmt.executeUpdate();
        if (connection != null) {
            connection.close();
        }
    }

    public String updateMemberValueStatusForAcc(String orgId, String memberId, String memberValue, String filterId) throws Exception {
        String status = "";

        String folderId = "";
        String deleteQ = "delete from prg_account_member_filter  where ACC_FILTER_ID=" + filterId;
        OraclePreparedStatement opstmt = null;
        Connection connection = ProgenConnection.getInstance().getConnection();
        /*
         * opstmt = (OraclePreparedStatement)
         * connection.prepareStatement(deleteQ);
         * opstmt.setStringForClob(1,memberValue); opstmt.setString(2,userId);
         * opstmt.setString(3,memberId); opstmt.setString(4,folderId);
         */
        String addUserMemberFilter = "insert into prg_account_member_filter(ACC_FILTER_ID,MEMBER_ID,MEMBER_VALUE,ACCOUNT_NO) values(ACCOUNT_FILTER_ID_SEQ.nextval,?,?,?)";
        ////.println.println(" here - " + addUserMemberFilter);
        opstmt = (OraclePreparedStatement) connection.prepareStatement(addUserMemberFilter);
        opstmt.setString(1, memberId);
        opstmt.setStringForClob(2, memberValue);
        opstmt.setString(3, orgId);
        int rows = opstmt.executeUpdate();
        if (connection != null) {
            connection.close();
        }
        try {
            ArrayList del = new ArrayList();
            del.add(deleteQ);
            executeMultiple(del);
        } catch (Exception x) {
            logger.error("Exception:", x);
        }

        return status;
    }

    public void deleteDimMemberValuesForAcc(String orgId, String memId) {
        String delQ = "delete from prg_account_member_filter where member_ID=" + memId + " and ACCOUNT_NO='" + orgId + "'";
        ////.println.println(" delQ Acc "+delQ);
        ArrayList delList = new ArrayList();
        delList.add(delQ);
        try {
            executeMultiple(delList);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    //added on 13-jan-10
    public void expireUser(String userId) throws SQLException {
        String expireUserQ = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            expireUserQ = "update prg_ar_users set pu_end_date=getdate() where pu_id=" + userId;
        } else {
            expireUserQ = "update prg_ar_users set pu_end_date=sysdate where pu_id=" + userId;
        }

        ArrayList al = new ArrayList();
        al.add(expireUserQ);
        try {
            executeMultiple(al);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public void addAccountReports(String accountId, String repIds) throws Exception {
        ArrayList delRep = new ArrayList();
        ArrayList newRepL = new ArrayList();
        if (repIds.length() > 0) {
            repIds = repIds.substring(1);
        }

        ArrayList inList = new ArrayList();
        ArrayList delList = new ArrayList();
        String alreadyAccountRepQ = "select * from account_report where org_id=" + accountId;
        PbReturnObject alObj = execSelectSQL(alreadyAccountRepQ);
        ArrayList alRep = new ArrayList();
        for (int g = 0; g < alObj.getRowCount(); g++) {
            alRep.add(alObj.getFieldValueString(g, "REPORT_ID"));
        }

        String accountInsertQ = "insert into account_report(org_id,report_id) values('&','&')";
        String insertQ = "insert into prg_ar_user_reports(USER_REP_ID,USER_ID,REPORT_ID,PUR_REPORT_SEQUENCE,PUR_FAV_REPORT,PUR_CUST_REPORT_NAME) values(PRG_AR_USER_REPORTS_SEQ.nextval,'&','&','','N','')";

        String allUsersQ = "select * from prg_ar_users where account_type=" + accountId;
        PbReturnObject userObj = execSelectSQL(allUsersQ);
        ArrayList userList = new ArrayList();
        String allU = "";
        for (int o = 0; o < userObj.getRowCount(); o++) {
            userList.add(userObj.getFieldValueString(o, "PU_ID"));
            allU = allU + "," + userObj.getFieldValueString(o, "PU_ID");
        }
        if (allU.length() > 0) {
            allU = allU.substring(1);
        }
        if (repIds.length() > 0) {
            String report[] = repIds.split(",");
            for (int m = 0; m < report.length; m++) {
                String rep = report[m];
                newRepL.add(rep);
                String repUserQ = "select * from prg_ar_user_reports where user_id in(" + allU + ") and report_id=" + rep;
                PbReturnObject allRepObj = new PbReturnObject();
                if (allU.length() > 0) {
                    allRepObj = execSelectSQL(repUserQ);
                }
                ArrayList userRep = new ArrayList();
                for (int y = 0; y < allRepObj.getRowCount(); y++) {
                    userRep.add(allRepObj.getFieldValueString(y, "USER_ID"));
                }
                if (!alRep.contains(rep)) {
                    Object orObj[] = new Object[2];
                    orObj[0] = accountId;
                    orObj[1] = rep;
                    String finaccountInsertQ = buildQuery(accountInsertQ, orObj);
                    inList.add(finaccountInsertQ);
                    for (int y = 0; y < userList.size(); y++) {
                        Object inObj[] = new Object[2];
                        inObj[0] = userList.get(y).toString();
                        inObj[1] = rep;
                        String fininsertQ = buildQuery(insertQ, inObj);
                        if (!userRep.contains(userList.get(y).toString())) {
                            inList.add(fininsertQ);
                        }
                    }
                } else {
                }
            }
        }
        String deleteQ = "delete from prg_ar_user_reports where report_id='&' and user_id='&'";
        for (int y = 0; y < alRep.size(); y++) {
            for (int n = 0; n < userList.size(); n++) {
                String userId = userList.get(n).toString();
                Object insertObj[] = new Object[2];
                insertObj[0] = alRep.get(y).toString();
                insertObj[1] = userId;
                if (!newRepL.contains(alRep.get(y).toString())) {
                    String findeleteQ = buildQuery(deleteQ, insertObj);
                    delList.add(findeleteQ);
                }
            }
            if (!newRepL.contains(alRep.get(y).toString())) {
                String delOrgRep = "delete from account_report where org_id='" + accountId + "' and report_id='" + alRep.get(y).toString() + "'";
                delList.add(delOrgRep);
            }
        }
        try {
            executeMultiple(delList);
            executeMultiple(inList);
        } catch (Exception d) {
        }
    }

    public void setRoleForAccount(String orgId, String roleId) throws Exception {
        String updateQ = "update prg_org_master set buss_role='" + roleId + "' where  org_id=" + orgId;
        String userRoleAssign = "INSERT INTO PRG_GRP_USER_FOLDER_ASSIGNMENT(USER_ASSI_ID, USER_FOLDER_ID, USER_ID, GRP_ID,"
                + " START_DATE, END_DATE) values(PRG_GRP_USER_FOLDER_ASSI_SEQ.nextval,&,&,(select grp_id from prg_user_folder where folder_id=&),sysdate,sysdate)";
        String insertQ = "insert into prg_ar_user_reports(USER_REP_ID,USER_ID,REPORT_ID,PUR_REPORT_SEQUENCE,PUR_FAV_REPORT,PUR_CUST_REPORT_NAME)"
                + " values(PRG_AR_USER_REPORTS_SEQ.nextval,'&','&','','N','')";

        ArrayList al = new ArrayList();
        String roleIdslist[] = null;
        if (roleId.contains(",")) {
            roleIdslist = roleId.split(",");
        } else {
            roleIdslist = new String[1];
            roleIdslist[0] = roleId;
        }

        String accUsers = "select * from prg_ar_users where account_type=" + orgId;
        PbReturnObject userObj = execSelectSQL(accUsers);
        for (int m = 0; m < userObj.getRowCount(); m++) {
            String delsql = " delete from PRG_GRP_USER_FOLDER_ASSIGNMENT where USER_ID=" + userObj.getFieldValueString(m, "PU_ID");
            al.add(delsql);

            for (int j = 0; j < roleIdslist.length; j++) {
                Object inObj[] = new Object[3];
                inObj[0] = roleIdslist[j];
                inObj[1] = userObj.getFieldValueString(m, "PU_ID");
                inObj[2] = roleIdslist[j];
                String finuserRoleAssign = buildQuery(userRoleAssign, inObj);
                al.add(finuserRoleAssign);
            }

        }

        al.add(updateQ);

        String delsql = " delete from account_report where ORG_ID=" + orgId;
        al.add(delsql);
        for (int j = 0; j < roleIdslist.length; j++) {
            String repForRole = "select * from prg_ar_report_details where folder_id=" + roleIdslist[j];
            PbReturnObject allRepObj = null;
            if (roleId != null && !roleId.equalsIgnoreCase("")) {
                allRepObj = execSelectSQL(repForRole);
            }
            if (roleId != null && !roleId.equalsIgnoreCase("")) {
                if (allRepObj != null) {
                    if (allRepObj.getRowCount() > 0) {
                        String compInsertRep = "insert into account_report(ORG_ID, REPORT_ID) values('&','&')";
                        if (allRepObj.getRowCount() > 0) {
                            String repId = "";
                            for (int m = 0; m < allRepObj.getRowCount(); m++) {
                                repId = allRepObj.getFieldValueString(m, "REPORT_ID");
                                Object inObj[] = new Object[2];
                                inObj[0] = orgId;
                                inObj[1] = repId;
                                String fincompInsertRep = buildQuery(compInsertRep, inObj);
                                al.add(fincompInsertRep);
                            }
                        }
                    }
                }
            }
            String repId = "";
            for (int n = 0; n < userObj.getRowCount(); n++) {
                String userId = userObj.getFieldValueString(n, "PU_ID");
                String delsql1 = " delete from prg_ar_user_reports where USER_ID=" + userId;
                al.add(delsql1);
                Object userInserOb[] = new Object[2];
                for (int m = 0; m < allRepObj.getRowCount(); m++) {
                    repId = allRepObj.getFieldValueString(m, "REPORT_ID");
                    userInserOb[0] = userId;
                    userInserOb[1] = repId;
                    String fininsertQ = buildQuery(insertQ, userInserOb);
                    al.add(fininsertQ);
                }
            }

        }

        try {
            executeMultiple(al);
        } catch (Exception j) {
            logger.error("Exception:", j);
        }
    }

    public String checkRoleAssignment(String orgId) {
        String finalQuery = "";
        String Status = "";
        try {
            String checkRoleAssignment = getResourceBundle().getString("checkRoleAssignment");
            Object obj[] = new Object[1];
            obj[0] = orgId;
            finalQuery = buildQuery(checkRoleAssignment, obj);
            PbReturnObject pbro = execSelectSQL(finalQuery);
            if (pbro.getRowCount() > 0 && pbro != null) {
                String checkReportExistance = getResourceBundle().getString("checkReportExistance");
                obj = new Object[1];
                obj[0] = orgId;
                finalQuery = buildQuery(checkReportExistance, obj);
                PbReturnObject pbro1 = execSelectSQL(finalQuery);
                if (pbro1.getRowCount() > 0 && pbro1 != null) {
                    Status = "1";
                } else {
                    Status = "";
                }

            } else {
                Status = "";
            }
            return Status;
        } catch (Exception j) {
            logger.error("Exception:", j);
            return Status;
        }

    }

    //added on 18jan to check user name in org
    public String checkUserName(String orgId, String userName) throws Exception {
        String status = "";
        String query = "select * from prg_ar_users where account_type=" + orgId;
        PbReturnObject pbro = execSelectSQL(query);
        for (int i = 0; i < pbro.getRowCount(); i++) {
            String localUName = pbro.getFieldValueString(i, "PU_LOGIN_ID");
            if (localUName.equalsIgnoreCase(userName)) {
                status = "exists";
            }
            if (status.length() > 0) {
                break;
            }
        }
        return status;
    }

    public HashMap getRoleStatusForAcc(String orgId, String bussRole) throws Exception {
        String userStatus = "";
        String reportSt = "";
        HashMap all = new HashMap();
        String accountUserQ = "select * from prg_ar_users where account_type in('" + orgId + "')";
        PbReturnObject userObj = execSelectSQL(accountUserQ);
        if (userObj.getRowCount() > 0) {
            userStatus = "User Exists";
        }
        String roleReportsQ = "select * from prg_ar_report_details where folder_id in('" + bussRole + "')";
        PbReturnObject reportObj = execSelectSQL(roleReportsQ);
        if (reportObj.getRowCount() > 0) {
            reportSt = "Report Exists";
        }
        all.put("reportSt", reportSt);
        all.put("userStatus", userStatus);
        return all;
    }

    public void addAccountUserReports(String userIds, String repIds, String orgId, String roleId) {
        String userRoleAssign = "INSERT INTO PRG_GRP_USER_FOLDER_ASSIGNMENT(USER_ASSI_ID, USER_FOLDER_ID, USER_ID, GRP_ID,"
                + " START_DATE, END_DATE) values(PRG_GRP_USER_FOLDER_ASSI_SEQ.nextval,&,&,(select grp_id from prg_user_folder where folder_id=&),sysdate,sysdate)";
        if (userIds.length() > 0) {
            userIds = userIds.substring(1);
        }
        if (repIds.length() > 0) {
            repIds = repIds.substring(1);
        }
        String updateQ = "update prg_org_master set buss_role=" + roleId + " where  org_id=" + orgId;
        ArrayList al = new ArrayList();
        al.add(updateQ);

        String users[] = userIds.split(",");
        String reports[] = repIds.split(",");
        String insertQ = "insert into prg_ar_user_reports(USER_REP_ID,USER_ID,REPORT_ID,PUR_REPORT_SEQUENCE,PUR_FAV_REPORT,PUR_CUST_REPORT_NAME) values(PRG_AR_USER_REPORTS_SEQ.nextval,'&','&','','N','')";
        String accReports = "insert into account_report(org_id,report_id) values('&','&')";
        Object orgObj[] = new Object[2];
        for (int n = 0; n < reports.length; n++) {
            orgObj[0] = orgId;
            orgObj[1] = reports[n];
            String finaccReports = buildQuery(accReports, orgObj);
            al.add(finaccReports);
        }


        for (int m = 0; m < users.length; m++) {
            String uId = users[m];
            Object insertObj[] = new Object[3];
            insertObj[0] = roleId;
            insertObj[1] = uId;
            insertObj[2] = roleId;

            String finuserRoleAssign = buildQuery(userRoleAssign, insertObj);
            al.add(finuserRoleAssign);

            for (int n = 0; n < reports.length; n++) {
                String rep = reports[n];
                Object inObj[] = new Object[2];
                inObj[0] = uId;
                inObj[1] = rep;
                String insertQfin = buildQuery(insertQ, inObj);
                al.add(insertQfin);
            }
        }

        try {
            executeMultiple(al);
        } catch (Exception j) {
            logger.error("Exception:", j);
        }
    }

    public void deleteCompany(String orgId) {
        ArrayList delList = new ArrayList();
        if (orgId.length() > 0) {
            orgId = orgId.substring(1);
        }
        String deleteComQ = "delete from prg_org_master where org_id in(" + orgId + ")";

        String accounts[] = orgId.split(",");

        //delete user report assign
        String userReportAssignQ = "delete from prg_ar_user_reports where user_id in(select pu_id from prg_ar_users where account_type in(" + orgId + "))";
        //delete account report assign
        String accountRep = "delete from account_report where org_id in(" + orgId + ")";

        //users delete
        String userDeleteQ = "delete from prg_ar_users where account_type in(" + orgId + ")";

        // user role assign
        String userGrAssign = "delete from prg_grp_user_folder_assignment where user_id in(select pu_id from prg_ar_users "
                + "where account_type in(" + orgId + "))";
        //user grp assignment
        String delUserRoleAssign = "delete from prg_grp_user_assignment where user_id in(select pu_id from prg_ar_users where account_type in(" + orgId + "))";
        String accFilter = "delete from prg_account_member_filter where account_no in(" + orgId + ")";

        delList.add(accFilter);
        delList.add(delUserRoleAssign);
        delList.add(userGrAssign);
        delList.add(userReportAssignQ);
        delList.add(userDeleteQ);
        delList.add(deleteComQ);
        delList.add(deleteComQ);

        try {
            executeMultiple(delList);
        } catch (Exception d) {
            logger.error("Exception:", d);
        }
    }

    public void modifyUserCompanyDetails(String orgId, String userId) throws Exception {
        String getBusRoleQry = "select buss_role from prg_org_master where org_id=" + orgId;
        String updateUserAccQry = "update prg_ar_users set account_type=" + orgId + " where pu_id=" + userId;
        String userRoleAssign = "INSERT INTO PRG_GRP_USER_FOLDER_ASSIGNMENT(USER_ASSI_ID, USER_FOLDER_ID, USER_ID, GRP_ID,"
                + " START_DATE, END_DATE) values(PRG_GRP_USER_FOLDER_ASSI_SEQ.nextval,&,&,(select grp_id from prg_user_folder where folder_id=&),sysdate,sysdate)";
        String insertQ = "insert into prg_ar_user_reports(USER_REP_ID,USER_ID,REPORT_ID,PUR_REPORT_SEQUENCE,PUR_FAV_REPORT,PUR_CUST_REPORT_NAME)"
                + " values(PRG_AR_USER_REPORTS_SEQ.nextval,'&','&','','N','')";

        PbReturnObject BusRoleObj = execSelectSQL(getBusRoleQry);
        ArrayList al = new ArrayList();
        String roleId = "";
        String delsql = "";
        String roleIdslist[] = null;
        for (int brole = 0; brole < BusRoleObj.getRowCount(); brole++) {
            roleId = BusRoleObj.getFieldValueString(brole, "BUSS_ROLE");
        }
        if (roleId.contains(",")) {
            roleIdslist = roleId.split(",");
        } else {
            roleIdslist = new String[1];
            roleIdslist[0] = roleId;
        }
        al.add(updateUserAccQry);
        delsql = " delete from PRG_GRP_USER_FOLDER_ASSIGNMENT where USER_ID=" + userId;
        al.add(delsql);
        for (int j = 0; j < roleIdslist.length; j++) {
            Object inObj[] = new Object[3];
            inObj[0] = roleIdslist[j];
            inObj[1] = userId;
            inObj[2] = roleIdslist[j];
            String finuserRoleAssign = buildQuery(userRoleAssign, inObj);
            al.add(finuserRoleAssign);
        }

        delsql = " delete from account_report where ORG_ID=" + orgId;
        al.add(delsql);
        for (int j = 0; j < roleIdslist.length; j++) {
            String repForRole = "select * from prg_ar_report_details where folder_id=" + roleIdslist[j];
            PbReturnObject allRepObj = null;
            if (roleId != null && !roleId.equalsIgnoreCase("")) {
                allRepObj = execSelectSQL(repForRole);
            }

            if (roleId != null && !roleId.equalsIgnoreCase("")) {
                if (allRepObj != null) {
                    if (allRepObj.getRowCount() > 0) {
                        String compInsertRep = "insert into account_report(ORG_ID, REPORT_ID) values('&','&')";
                        if (allRepObj.getRowCount() > 0) {
                            String repId = "";
                            for (int m = 0; m < allRepObj.getRowCount(); m++) {
                                repId = allRepObj.getFieldValueString(m, "REPORT_ID");
                                Object inObj[] = new Object[2];
                                inObj[0] = orgId;
                                inObj[1] = repId;
                                String fincompInsertRep = buildQuery(compInsertRep, inObj);
                                al.add(fincompInsertRep);
                            }
                        }
                    }
                }
            }
            String repId = "";
            String delsql1 = " delete from prg_ar_user_reports where USER_ID=" + userId;
            al.add(delsql1);
            Object userInserOb[] = new Object[2];
            for (int m = 0; m < allRepObj.getRowCount(); m++) {
                repId = allRepObj.getFieldValueString(m, "REPORT_ID");
                userInserOb[0] = userId;
                userInserOb[1] = repId;
                String fininsertQ = buildQuery(insertQ, userInserOb);
                al.add(fininsertQ);
            }
        }
        try {
            executeMultiple(al);
        } catch (Exception j) {
            logger.error("Exception:", j);
        }
    }

    public PbReturnObject checkOrganisationName() throws Exception {
        PbReturnObject retObj = new PbReturnObject();
        try {
            String getOrganisationNameQuery = getResourceBundle().getString("getOrganisationName");
            retObj = execSelectSQL(getOrganisationNameQuery);
            int num = retObj.getRowCount();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public void validateExpiryDate(String userId, String newExpDate, String userFlag) throws Exception {
        String newExpDateQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            newExpDateQry = "select convert(varchar,convert(datetime,'" + newExpDate + "',110),106)";
        } else {
            newExpDateQry = "select to_char(to_date('" + newExpDate + "','mm-dd-yy'),'dd-MON-yy') from dual";
        }
        PbReturnObject newExpDateObj = execSelectSQL(newExpDateQry);
        String insertExpDate = newExpDateObj.getFieldValueString(0, 0);
        String updateExpdateQry = "";
        if (userFlag.equalsIgnoreCase("Y")) {
            updateExpdateQry = "update prg_ar_users set pu_end_date='" + insertExpDate + "' where pu_id=" + userId;
        } else {
            updateExpdateQry = "update prg_org_master set org_end_date='" + insertExpDate + "' where org_id=" + userId;
        }
        ArrayList al = new ArrayList();
        al.add(updateExpdateQry);
        try {
            executeMultiple(al);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }

    public boolean checkActiveKey() {
        boolean checkActive = false;
        String getActiveQuery = getResourceBundle().getString("getActiveQuery");
        try {

            PbReturnObject pbReturnObject = execSelectSQL(getActiveQuery);
            PbReturnObject checkUserEntries = execSelectSQL("select * from PRG_AR_USERS");
            if (checkUserEntries.getRowCount() < pbReturnObject.getFieldValueInt(0, 0) || pbReturnObject.getRowCount() == 0) {
                checkActive = true;

            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return checkActive;
    }
}
