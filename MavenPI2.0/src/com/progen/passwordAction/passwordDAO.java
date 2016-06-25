/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.passwordAction;

import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEncrypter;

/**
 *
 * @author Administrator
 */
public class passwordDAO {

    public static Logger logger = Logger.getLogger(passwordDAO.class);
    passwordResourceBundle pswrdresbundle = new passwordResourceBundle();
    PbReturnObject pswrdpbobj = new PbReturnObject();
    PbDb pbdb = new PbDb();
    PbEncrypter pswrdencrypter = new PbEncrypter();

    public String checkOldpswrd(String oldpassword, String username, String newpswrd) {
        String status = "failure";
        try {

            String password = null;
            Object[] pswrdobj = new Object[1];
            pswrdobj[0] = username;

            String chkpswrdquery = pswrdresbundle.getString("checkoldpassword");
            String updtepswrdqry = pbdb.buildQuery(chkpswrdquery, pswrdobj);
            ////////////////////////////////////////////////////.println("builded query is" + updtepswrdqry);
            pswrdpbobj = pbdb.execSelectSQL(updtepswrdqry);
            if (pswrdpbobj.getRowCount() != 0) {
                password = pswrdpbobj.getFieldValueString(0, "PU_PASSWORD");
                ////////////////////////////////////////////////////.println("database password" + password);
                // password = pswrdencrypter.decrypt(password);
                oldpassword = pswrdencrypter.encrypt(oldpassword);
                ////////////////////////////////////////////////////.println("afterdecrypt " + password);
                if (password.equals(oldpassword)) {
                    status = "Success";
                } else {
                    status = "failure";
                }
            } else {
                status = "failure";
            }
        } catch (Exception e) {
            ////////////////////////////////////////////////////.println("Exception Caught in checkoldpassword method of password dao class");
            logger.error("Exception:", e);
        }
        return status;
    }

    public String updatePassword(String username, String newpswrd) {
        String status = "success";

        try {
            String updtedpswrd = null;
            updtedpswrd = pswrdencrypter.encrypt(newpswrd);
            ////////////////////////////////////////////////////.println("encrypted passwrd" + updtedpswrd);
            Object[] updtobj = new Object[2];
            updtobj[0] = updtedpswrd;
            updtobj[1] = username;
            ////////////////////////////////////////////////////.println("new password" + newpswrd);
            String updtepswrdqry = pswrdresbundle.getString("updatenewpassword");
            String bildupdtpswrdqry = pbdb.buildQuery(updtepswrdqry, updtobj);
            ////////////////////////////////////////////////////.println("buildedqueyr" + bildupdtpswrdqry);

            pbdb.execModifySQL(bildupdtpswrdqry);

        } catch (Exception e) {
            ////////////////////////////////////////////////////.println("Exception caught in updatepassword method of passworddao class");
            logger.error("Exception:", e);
            status = "failure";
        }
        return status;
    }

    public String getPassword(String username, String emailid) {
        String status = "failure";
        try {
            Object[] gtpswrdobj = new Object[2];
            gtpswrdobj[0] = username;
            gtpswrdobj[1] = emailid;
            String getpswrdqry = pswrdresbundle.getString("getpassword");
            String buildgetpswrdqry = pbdb.buildQuery(getpswrdqry, gtpswrdobj);
            ////////////////////////////////////////////////////.println("builquery is"+buildgetpswrdqry);
            String password = null;
            pswrdpbobj = pbdb.execSelectSQL(buildgetpswrdqry);
            if (pswrdpbobj.getRowCount() != 0) {
                password = pswrdpbobj.getFieldValueString(0, "PU_PASSWORD");
                ////////////////////////////////////////////////////.println("password"+password);
                status = pswrdencrypter.decrypt(password);
                ////////////////////////////////////////////////////.println("afterdecrypting"+status);
            }

        } catch (Exception e) {
            ////////////////////////////////////////////////////.println("Exception caught in getpassword method of passworddao class");
            logger.error("Exception:", e);
            status = "failure";
        }
        return status;
    }

    public String resetPassword(String newPassword, String userid) {
        String status = "failure";
//        //////.println("newPassword\t"+newPassword);
//        //////.println("userid\t"+userid);
        newPassword = pswrdencrypter.encrypt(newPassword);
//        //////.println("newpasswrod is"+newPassword);
        try {
            Object[] resetobj = new Object[2];
            resetobj[0] = newPassword;
            resetobj[1] = userid;

            String rstpswrdqry = pswrdresbundle.getString("resetpassword");
//        //////.println("query beforebuild rstpswrdqry"+rstpswrdqry);
            String buildedrstqry = pbdb.buildQuery(rstpswrdqry, resetobj);
//        //////.println("after building buildedrstqry"+buildedrstqry);
            pbdb.execModifySQL(buildedrstqry);
            status = "success";
        } catch (Exception e) {
            //////.println("Exception caught in resetpassword method of password dao class");
            logger.error("Exception:", e);
        }
        return status;
    }

    public String verifyResetPassword(String userid) {
        String status = "failure";
        try {
            String vrfyqry = pswrdresbundle.getString("verifyrstpswrd");
            Object[] vrfyobj = new Object[1];
            vrfyobj[0] = userid;

            String vrfybuildqry = pbdb.buildQuery(vrfyqry, vrfyobj);
            pswrdpbobj = pbdb.execSelectSQL(vrfybuildqry);
            String isAdminuser = pswrdpbobj.getFieldValueString(0, 0);
            //////.println("isadminuser is"+isAdminuser);
            if (isAdminuser.equalsIgnoreCase("Admin User")) {
                status = "success";
            } else {
                status = "failure";
            }
        } catch (Exception e) {
            //////.println("Exception caugth in verifyresetpswrd method of passworddao");
            logger.error("Exception:", e);
        }
        return status;
    }
//by gopesh

    public String checkOldName(String userId, String oldName, String newName) {
        String status = "failure";
        try {

            String nameIn = null;
            Object[] nameObj = new Object[1];
            nameObj[0] = userId;

            String chknamequery = pswrdresbundle.getString("checkoldName");
            String updtepswrdqry = pbdb.buildQuery(chknamequery, nameObj);
            pswrdpbobj = pbdb.execSelectSQL(updtepswrdqry);
            if (pswrdpbobj.getRowCount() != 0) {
                nameIn = pswrdpbobj.getFieldValueString(0, "PU_LOGIN_ID");
                if (nameIn.equals(oldName)) {
                    status = "Success";
                } else {
                    status = "failure";
                }
            } else {
                status = "failure";
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return status;
    }
    //by gopesh

    public String updateName(String oldname, String userId, String newName) {
        String status = "success";
        try {
            Object[] updtobj = new Object[3];
            updtobj[0] = newName;
            updtobj[1] = oldname;
            updtobj[2] = userId;
            String updtenameqry = pswrdresbundle.getString("updatenewname");
            String bildupdtpswrdqry = pbdb.buildQuery(updtenameqry, updtobj);

            pbdb.execModifySQL(bildupdtpswrdqry);

        } catch (Exception e) {
            logger.error("Exception:", e);
            status = "failure";
        }
        return status;
    }
}
