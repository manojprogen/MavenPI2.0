/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.saveusertype;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
class SaveUsertypeDAO extends PbDb {

    public static Logger logger = Logger.getLogger(SaveUsertypeDAO.class);
//    userlistTypeResourceBundle resBundle = new userlistTypeResourceBundle();
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new userlistTypeResBundleSqlServer();
            } else {
                resourceBundle = new userlistTypeResourceBundle();
            }

        }

        return resourceBundle;
    }

    void saveUsertype(String[] userlslist) throws Exception {
        String saveUsertypequery = getResourceBundle().getString("saveUsertype");
        ArrayList al = new ArrayList();
        String finalyquery = "";

        Object[] obj1 = new Object[2];
        obj1[0] = userlslist[0];
        obj1[1] = userlslist[1];
        finalyquery = buildQuery(saveUsertypequery, obj1);
        al.add(finalyquery);
        ////////////////////////////////////////////.println.println(" finalyquery "+finalyquery);

        try {
            executeMultiple(al);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

    }

    public boolean isUserTypeAvailable(String userTypeName) {
        String qry = "select USER_TYPE_NAME from PRG_USER_TYPE where Upper(USER_TYPE_NAME)='" + userTypeName.trim().toUpperCase() + "'";
        try {
            PbReturnObject pbro = execSelectSQL(qry);
            if (pbro.getRowCount() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return false;
    }

    ///
    public void updateUserTypeForUser(String userId, String userTypeId) throws Exception {
//        String insertUserPrivileges = "";
//        String userRepPRivQ = "";
//        String tableRepPriv = "";
//        String repGraphPriv = "";
//        ArrayList delList = new ArrayList();
        ArrayList update = new ArrayList();
//        ArrayList insertListPriv = new ArrayList();
//        String delRepGr = "delete from prg_ar_repgrp_previlages where user_id=" + userId;
//        String delUserARPriv = "delete from prg_ar_user_priveleges where user_id=" + userId;
//        String delUserRepTab = "delete from PRG_AR_REPTAB_PREVILAGES where user_id=" + userId;
//        String delArPriv = "delete from PRG_AR_REPORT_PREVILAGES where user_id=" + userId;
//        delList.add(delRepGr);
//        delList.add(delUserARPriv);
//        delList.add(delUserRepTab);
//        delList.add(delArPriv);

        String updateQ = "update prg_ar_users set user_type=" + userTypeId + " where pu_id=" + userId;
        update.add(updateQ);

//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            insertUserPrivileges = "insert into PRG_AR_USER_PRIVELEGES(USER_ID,PRIVELEGE_ID) values('&','&')";
//            userRepPRivQ = "insert into PRG_AR_REPORT_PREVILAGES(USER_ID,PREVILAGE_NAME) values('&','&')";
//            tableRepPriv = "insert into PRG_AR_REPTAB_PREVILAGES(USER_ID,PREVILAGE_NAME) values('&','&')";
//            repGraphPriv = "insert into PRG_AR_REPGRP_PREVILAGES(USER_ID,PREVILAGE_NAME) values('&','&')";
//        } else {
//            insertUserPrivileges = "insert into PRG_AR_USER_PRIVELEGES(PUR_ID,USER_ID,PRIVELEGE_ID) values(PRG_AR_USER_PRIVI_SEQ.nextval,'&','&')";
//            userRepPRivQ = "insert into PRG_AR_REPORT_PREVILAGES(PRP_ID,USER_ID,PREVILAGE_NAME) values(PRG_AR_REPORT_PREVILAGES_SEQ.nextval,'&','&')";
//            tableRepPriv = "insert into PRG_AR_REPTAB_PREVILAGES(PRTP_ID,USER_ID,PREVILAGE_NAME) values(PRG_AR_REPTAB_PREVILAGES_SEQ.nextval,'&','&')";
//            repGraphPriv = "insert into PRG_AR_REPGRP_PREVILAGES(PRGP_ID,USER_ID,PREVILAGE_NAME) values(PRG_AR_REPGRP_PREVILAGES_SEQ.nextval,'&','&')";
//        }
//
//
//        ///
//        String grphRepPriv = "";
//        String getUserPrivQ = "select * from user_type_priveleges where user_type=" + userTypeId;
//        PbReturnObject privRetObj = execSelectSQL(getUserPrivQ);
//
//        for (int m = 0; m < privRetObj.getRowCount(); m++) {
//            Object privObj[] = new Object[2];
//            privObj[0] = userId;
//            privObj[1] = privRetObj.getFieldValueString(m, "PRIVILEGE");
//            if (privRetObj.getFieldValueString(m, "PRIVILEGE_TYPE").equalsIgnoreCase("H")) {
//                String finInsert = buildQuery(insertUserPrivileges, privObj);
//                //////////////////////////////////////////////.println.println(" finInsert-== " + finInsert);
//                insertListPriv.add(finInsert);
//            }
//            if (privRetObj.getFieldValueString(m, "PRIVILEGE_TYPE").equalsIgnoreCase("R")) {
//                String finInsert = buildQuery(userRepPRivQ, privObj);
//                //////////////////////////////////////////////.println.println(" finInsert-== " + finInsert);
//                insertListPriv.add(finInsert);
//            }
//            if (privRetObj.getFieldValueString(m, "PRIVILEGE_TYPE").equalsIgnoreCase("T")) {
//                // privObj[0]=
//                String finInsert = buildQuery(tableRepPriv, privObj);
//                //////////////////////////////////////////////.println.println(" finInsert-== " + finInsert);
//                insertListPriv.add(finInsert);
//            }
//            if (privRetObj.getFieldValueString(m, "PRIVILEGE_TYPE").equalsIgnoreCase("RG")) {
//                String finInsert = buildQuery(repGraphPriv, privObj);
//                //////////////////////////////////////////////.println.println(" finInsert-== " + finInsert);
//                insertListPriv.add(finInsert);
//            }
//        }
        try {
            executeMultiple(update);
//            executeMultiple(delList);
//            executeMultiple(insertListPriv);
        } catch (Exception j) {
            logger.error("Exception:", j);
        }
    }
}
