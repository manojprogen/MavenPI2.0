/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

import com.progen.cache.MetadataCacheManager;
import com.progen.superadmin.SuperAdminBd;
import com.progen.superadmin.UserAssignment;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class PrivilegeManager {

    public static Logger logger = Logger.getLogger(PrivilegeManager.class);

    public static UserAssignment loadUserPrivileges(int userId) {
        UserAssignment userAssignment;
        userAssignment = MetadataCacheManager.MANAGER.retrieveUserAssignment(userId);
        if (userAssignment == null) {
            SuperAdminBd adminBd = new SuperAdminBd();
            userAssignment = adminBd.createUserAssigmentObject(userId, "user");
            MetadataCacheManager.MANAGER.storeUserAssignment(userId, userAssignment);
        }
        return userAssignment;
    }

    public static String getUserName(int userId) {
        String userName = "";
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        userName = MetadataCacheManager.MANAGER.retrieveUserName(userId);

        if (userName == null) {
            String query = "select PU_LOGIN_ID from PRG_AR_USERS where PU_ID=" + userId;
            try {
                pbro = pbdb.execSelectSQL(query);
            } catch (SQLException ex) {
                logger.error("Exception: ", ex);
            }
            if (pbro != null && pbro.getRowCount() > 0) {
                userName = pbro.getFieldValueString(0, 0);
            }
            MetadataCacheManager.MANAGER.storeUserName(userId, userName);
        }

        return userName;

    }

    public static boolean isModuleEnabledForUser(String moduleCode, int userId) {
        if ("HTMLREPORTS".equalsIgnoreCase(moduleCode)) //                 "REPVIEWER".equalsIgnoreCase(moduleCode)||
        //                 "MAP".equalsIgnoreCase(moduleCode))
        {
            return true;
        }
        if ("PROGEN".equalsIgnoreCase(PrivilegeManager.getUserName(userId))
                || "NESMA".equalsIgnoreCase(PrivilegeManager.getUserName(userId))) {
            return true;
        } else {
            UserAssignment userAssignment = PrivilegeManager.loadUserPrivileges(userId);
            return userAssignment.isModuleAssigned(moduleCode);
        }
    }

    public static boolean isModuleComponentEnabledForUser(String moduleCode, String compCode, int userId) {
        if ("PROGEN".equalsIgnoreCase(PrivilegeManager.getUserName(userId)) || "NESMA".equalsIgnoreCase(PrivilegeManager.getUserName(userId))) {
            return true;
        } else {
            UserAssignment userAssignment = PrivilegeManager.loadUserPrivileges(userId);
//             if("DASHBOARDDESIGNER".equalsIgnoreCase(compCode))
//                 return userAssignment.isModuleComponentAssigned(moduleCode, compCode);
//             else
            return userAssignment.isModuleComponentAssigned(moduleCode, compCode);
        }
    }

    public static boolean isComponentEnabledForUser(String functionalArea, String compCode, int userId) {

        if ("REPORT".equals(functionalArea)) {
            if (PrivilegeManager.isModuleComponentEnabledForUser("REPVIEWER", compCode, userId)
                    || PrivilegeManager.isModuleComponentEnabledForUser("VIEWERPLUS", compCode, userId)
                    || PrivilegeManager.isModuleComponentEnabledForUser("ANALYSER", compCode, userId)) {
                return true;
            }

        }
        return false;
    }
}
