/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.tracker;

import com.progen.connection.ConnectionMetadata;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.scheduler.SchedulerBD;
import com.progen.scheduler.db.SchedulerDAO;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEmailConfig;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class ScorecardTrackerStartupServlet extends HttpServlet {

    public static Logger logger = Logger.getLogger(ScorecardTrackerStartupServlet.class);

    public void init() throws ServletException {
        try {
            super.init();
            PbReturnObject pbretObj = null;
            PbDb pbdb = new PbDb();
            Properties connProps = new Properties();
            InputStream servletStream = getServletContext().getResourceAsStream("WEB-INF/MetadataConn.xml");
            ConnectionMetadata metadata = null;
            SchedulerBD bd = new SchedulerBD();
            if (servletStream != null) {
                try {
                    connProps.loadFromXML(servletStream);
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
                metadata = new ConnectionMetadata(connProps);
                ProgenConnection.setConnectionMetadata(metadata);
            }
            PbReportViewerDAO pbrv = new PbReportViewerDAO();
            PbReturnObject returnObj = new PbReturnObject();
            returnObj = pbrv.getEmailConfigDetails();
            if (returnObj.getRowCount() > 0) {
                PbEmailConfig emailConfigObj = PbEmailConfig.getPbEmailConfig();

                if (emailConfigObj == null) {
                    PbEmailConfig.createEmailConfigfrmDB(returnObj);
//                       PbEmailConfig emailConfig = new PbEmailConfig();
//                       emailConfig.setSmtpHostName(returnObj.getFieldValueString(0,0));
//                       emailConfig.setSmtpPortNo(returnObj.getFieldValueString(0,1));
//                       emailConfig.setFromAddr(returnObj.getFieldValueString(0,2));
//                       emailConfig.setDebug(returnObj.getFieldValueString(0,3));
//                       emailConfig.setAuthUser(returnObj.getFieldValueString(0,4));
//                       String pswd = returnObj.getFieldValueString(0,5);
//                       String decryptPwd = DeEncrypter.getInstance().decrypt(pswd); 
//                       emailConfig.setAuthPwd(decryptPwd);
                }
            }
//            PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
//            if (emailConfig == null) {
//                Properties emailProps = new Properties();
//                servletStream = getServletContext().getResourceAsStream("WEB-INF/EmailConfig.xml");
//                if (servletStream != null) {
//                    try {
//                        emailProps.loadFromXML(servletStream);
//                        PbEmailConfig.createEmailConfig(emailProps);
//                    } catch (Exception e) {
//                        logger.error("Exception:",e);
//                    }
//                }
//            }
            String qry = "select distinct(SCORECARD_SCHEDULE_ID) from PRG_AR_SCORECARD_TRACKER ";
            PbReturnObject pbro = pbdb.execSelectSQL(qry);
            SchedulerDAO dao = new SchedulerDAO();

            for (int i = 0; i < pbro.getRowCount(); i++) {
                ScorecardTracker tracker = dao.editScorecardTracker(pbro.getFieldValueInt(i, "SCORECARD_SCHEDULE_ID"));
                bd.scheduleScorecardTracker(tracker);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }
}
