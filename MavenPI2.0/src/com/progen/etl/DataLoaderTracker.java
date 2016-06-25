/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.etl;

import com.progen.reportview.db.PbReportViewerDAO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.*;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEmailConfig;
import prg.util.PbMail;
import prg.util.PbMailParams;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class DataLoaderTracker implements Job {

    public static Logger logger = Logger.getLogger(DataLoaderTracker.class);

    public void execute(JobExecutionContext jec) throws JobExecutionException {

        Properties loadTableProps = new Properties();
        PbDb pbDb = new PbDb();
        EtlUploadBd uploadBd = new EtlUploadBd();
        PbReturnObject returnObj = new PbReturnObject();
        ArrayList<String> tableLst = new ArrayList<String>();
        PbMailParams mailParams = new PbMailParams();
        StringBuilder buffer = new StringBuilder("");
        Iterator propKeyIter;
        HashMap<String, String> loadTableDetailsMap = new HashMap<String, String>();

        ServletContext context = null;
        Set tableKeys = null;
        String tableNames = "";
        String tabName;
        int exCount = 0;
        int axCount = 0;
        try {
            context = (ServletContext) jec.getJobDetail().getJobDataMap().get("context");
            InputStream servletStream = context.getResourceAsStream("WEB-INF/EtlLoadTables.xml");
            loadTableProps.loadFromXML(servletStream);
            tableKeys = loadTableProps.keySet();
            propKeyIter = tableKeys.iterator();
            while (propKeyIter.hasNext()) {
                tableLst.add(propKeyIter.next().toString());
            }

            for (int i = 0; i < tableLst.size(); i++) {
                if (tableLst.get(i).toString().startsWith("ExcelTable")) {
                    exCount++;
                }
                if (tableLst.get(i).toString().startsWith("AccessTable")) {
                    axCount++;
                }
            }
            for (int i = 1; i <= exCount; i++) {
                tabName = loadTableProps.getProperty("ExcelTable" + i);
                tableNames = tableNames + "'" + tabName + "'";
                if (i != exCount) {
                    tableNames = tableNames + ",";
                }
            }

            for (int i = 1; i <= axCount; i++) {
                tabName = loadTableProps.getProperty("AccessTable" + i);
                tableNames = tableNames + "'" + tabName + "'";
                if (i != axCount) {
                    tableNames = tableNames + ",";
                }
            }


            //  
        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        String query = "select DB_TABLE from prg_etl_setup where DB_TABLE in( " + tableNames + " )";
        String tableName;
        try {
            returnObj = pbDb.execSelectSQL(query);
            if (returnObj.getRowCount() > 0) {
                for (int i = 0; i < returnObj.getRowCount(); i++) {
                    tableName = returnObj.getFieldValueString(i, "DB_TABLE");
                    String status = uploadBd.loadData(tableName, "", "");
                    loadTableDetailsMap.put(tableName, status);
                    //        //Cannot connect to the database, please try again.
                }
            }

            if (!loadTableDetailsMap.isEmpty()) {
                String connId;
                String procedureName;
                connId = loadTableProps.getProperty("DataConnectionId");
                procedureName = loadTableProps.getProperty("DataLoadProcedure");
                Connection dataCon = ProgenConnection.getInstance().getConnectionByConId(connId);

                CallableStatement cstmt = dataCon.prepareCall("{ call " + procedureName + " }");
                cstmt.execute();

            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        //Code to Send Mail

        PbReportViewerDAO pbrv = new PbReportViewerDAO();
        PbReturnObject returnObj1 = new PbReturnObject();
        returnObj1 = pbrv.getEmailConfigDetails();
        if (returnObj1.getRowCount() > 0) {
            PbEmailConfig emailConfigObj = PbEmailConfig.getPbEmailConfig();

            if (emailConfigObj == null) {
                PbEmailConfig.createEmailConfigfrmDB(returnObj1);
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
//        InputStream servletStream = null;
//        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
//        if (emailConfig == null) {
//            Properties emailProps = new Properties();
//            servletStream = context.getResourceAsStream("WEB-INF/EmailConfig.xml");
//            if (servletStream != null) {
//                try {
//                    emailProps.loadFromXML(servletStream);
//                    PbEmailConfig.createEmailConfig(emailProps);
//                } catch (Exception e) {
//                   logger.error("Exception:",e);
//                }
//            }
//        }
        int sentMailCt = 0;
        PbMail mail = null;
        ArrayList<String> sentMailsLst = new ArrayList<String>();
        for (int i = 0; i < tableLst.size(); i++) {
            if (tableLst.get(i).startsWith("mailto")) {
                sentMailCt++;
                sentMailsLst.add(loadTableProps.getProperty("mailto" + sentMailCt));
            }
        }

        if (sentMailsLst.size() > 0) {
            String keys[] = loadTableDetailsMap.keySet().toArray(new String[0]);
            if (keys.length > 0) {
                buffer.append("<table border='1'><thead><tr><td bgcolor='#D7FAFF'>Table Name</td><td bgcolor='#D7FAFF'>Statuts</td><tbody>");
                for (int i = 0; i < loadTableDetailsMap.size(); i++) {
                    buffer.append("<tr><td>");
                    buffer.append(keys[i] + "</td><td>");
                    buffer.append(loadTableDetailsMap.get(keys[i]) + "</td>");
                    buffer.append("</tr>");
                }
                buffer.append("</tbody></table>");
            } else {
                buffer.append("No Table Loaded");
            }

            //  
            for (int i = 0; i < sentMailsLst.size(); i++) {
                mailParams.setToAddr(sentMailsLst.get(i));
                mailParams.setSubject("Information About Load Tables");
                mailParams.setBodyText(buffer.toString());
                mail = new PbMail(mailParams);
                try {
                    mail.sendMail();
                } catch (AddressException ex) {
                    logger.error("Exception:", ex);
                } catch (MessagingException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
    }
}
