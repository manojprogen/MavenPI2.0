/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import com.progen.userlayer.db.DeEncrypter;
import java.util.Properties;
import prg.db.PbReturnObject;

/**
 *
 * @author arun
 */
public class PbEmailConfig {

    private String smtpHostName;
    private String smtpPortNo;
    private String fromAddr;
    private String debug;
    private String authUser;
    private String authPwd;
    private String sslStatus;
    private String serverIdentity;
    private static PbEmailConfig emailConfig;

    public static PbEmailConfig getPbEmailConfig() {
        return emailConfig;
    }

    public PbEmailConfig() {
    }

    public static PbEmailConfig createEmailConfig(Properties props) {
        if (emailConfig == null) {
            emailConfig = new PbEmailConfig();
        }
        emailConfig.setSmtpHostName(props.getProperty("smtpHostName"));
        emailConfig.setSmtpPortNo(props.getProperty("smtpPortNo"));
        emailConfig.setDebug(props.getProperty("debug"));
        emailConfig.setFromAddr(props.getProperty("fromAddr"));
        emailConfig.setAuthUser(props.getProperty("authUser"));
        emailConfig.setAuthPwd(props.getProperty("authPwd"));
        return emailConfig;
    }

    public static PbEmailConfig createEmailConfigfrmDB(PbReturnObject returnObj) {
        if (emailConfig == null) {
            emailConfig = new PbEmailConfig();
        }
        emailConfig.setSmtpHostName(returnObj.getFieldValueString(0, 0));
        emailConfig.setSmtpPortNo(returnObj.getFieldValueString(0, 1));
        emailConfig.setDebug(returnObj.getFieldValueString(0, 3));
        emailConfig.setFromAddr(returnObj.getFieldValueString(0, 2));
        emailConfig.setAuthUser(returnObj.getFieldValueString(0, 4));
        String pswd = returnObj.getFieldValueString(0, 5);
        String decryptPwd = DeEncrypter.getInstance().decrypt(pswd);
        emailConfig.setAuthPwd(decryptPwd);
        String sslstatus = returnObj.getFieldValueString(0, 6);
        emailConfig.setSslStatus(returnObj.getFieldValueString(0, 6));
        emailConfig.setServerIdentity(returnObj.getFieldValueString(0, 7));
        return emailConfig;
    }

    public String getAuthPwd() {
        return authPwd;
    }

    public void setAuthPwd(String authPwd) {
        this.authPwd = authPwd;
    }

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getSmtpHostName() {
        return smtpHostName;
    }

    public void setSmtpHostName(String smtpHostName) {
        this.smtpHostName = smtpHostName;
    }

    public String getSmtpPortNo() {
        return smtpPortNo;
    }

    public void setSmtpPortNo(String smtpPortNo) {
        this.smtpPortNo = smtpPortNo;
    }

    public String getSslStatus() {
        return sslStatus;
    }

    public void setSslStatus(String sslStatus) {
        this.sslStatus = sslStatus;
    }

    public String getServerIdentity() {
        return serverIdentity;
    }

    public void setServerIdentity(String serverIdentity) {
        this.serverIdentity = serverIdentity;
    }
}
