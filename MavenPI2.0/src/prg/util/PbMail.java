/*
 * PbMail.java
 *
 * Created on April 25, 2009, 3:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package prg.util;

/**
 *
 * @author Administrator
 */
//import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.progen.userlayer.db.LogReadWriter;
import com.progen.xml.UploadingXmlIntoDatabase;
import com.sun.mail.dsn.MultipartReport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Flags.Flag;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.FlagTerm;
import org.apache.log4j.Logger;

public class PbMail {

    public static Logger logger = Logger.getLogger(PbMail.class);
    private PbMailParams mailParams = null;
    LogReadWriter logrw = new LogReadWriter();
    int Totalloc = 0;
    //    public String fromAddr = "";
//    public String authUser = "";
//    public String authPwd = "";

    /**
     * Creates a new instance of PbMail
     */
    public PbMail() {
    }

    public PbMail(PbMailParams params) {
        this.mailParams = params;
    }

    public boolean sendMail() throws AddressException, MessagingException {
        Session session = null;
        Properties properties = null;
        MimeMessage mimemessage = null;
        PbMailAuthenticator authenticator = null;

        File attFile = null;
        FileDataSource filedatasource = null;

        //Get the Email configuration settings used for sending email
        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
        if (emailConfig == null) {
            return false;
        }

        String hostName = emailConfig.getSmtpHostName();
        //modified by Nazneen on 17/03/15 for setting default mail id
        String authUser = emailConfig.getAuthUser();
        String authPwd = emailConfig.getAuthPwd();
        String fromAddr = emailConfig.getFromAddr();
//        if(authUser.equalsIgnoreCase("")){
//            authUser = emailConfig.getFromAddr();
//        }
//        if(authPwd.equalsIgnoreCase("")){
//            authPwd = emailConfig.getFromAddr();
//        }
//        if(fromAddr.equalsIgnoreCase("")){
//            fromAddr = emailConfig.getFromAddr();
//        }
        String debug = emailConfig.getDebug();
        String smtpPortNo = emailConfig.getSmtpPortNo();
        String sslStatus = emailConfig.getSslStatus();

        //String hostName = "74.125.155.109";//"209.85.147.109"; //209.85.199.109 //"72.14.235.121";
        String toAddr = mailParams.getToAddr(); //"venkat@progenbusiness.com";
        String subject = mailParams.getSubject(); //"Test Subject";
        String bodyText = mailParams.getBodyText(); //"Sample mail subject to test";
        String fileUrl = mailParams.getFileUrl();  // text file url which has to be send
        boolean hasAttachment = mailParams.getHasAttach();
        //String attachFile = mailParams.getAttachFile();
        ArrayList<String> attachFile = mailParams.getAttachFile();
        //String usrMessage = mailParams.getBodyMessage();
        String urlMessage = "To view the report online,please click  <a href=" + fileUrl + " >Here</a> .\n\n";
        //String urlMessage="";//changed by sruthi for microsoftoutlook
        //urlMessage+=fileUrl;
        try {
            authenticator = new PbMailAuthenticator();
            InternetAddress toAddrs[] = InternetAddress.parse(toAddr);

            authenticator.myUserId = authUser;
            authenticator.myPassword = authPwd;

            properties = new Properties();
            properties.put("mail.smtp.host", hostName);
            properties.put("mail.smtp.port", smtpPortNo);
            properties.put("mail.debug", debug);
            //by g
            if (!sslStatus.equalsIgnoreCase("false")) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.starttls.enable", "false");
                properties.put("mail.smtp.ssl.checkserveridentity", "false");
                properties.put("mail.smtp.ssl.trust", "*");
            }
            properties.put("mail.smtp.auth", "true");

            session = Session.getInstance(properties, authenticator);
            session.setDebug(false);

            mimemessage = new MimeMessage(session);
            mimemessage.setFrom(new InternetAddress(fromAddr));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, toAddrs);
            //mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, ccAddrs);
            //mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, bccAddrs);
            mimemessage.setContent(bodyText, "text/html");
            mimemessage.setSubject(subject);
            mimemessage.setSentDate(new Date());
            if (hasAttachment) {
                MimeBodyPart mimebodypart = new MimeBodyPart();
                //mimebodypart.setContent("HTML");
                //mimebodypart.setText(bodyText);
                mimebodypart.setContent(bodyText, "text/html");
                if (attachFile != null && !"".equals(attachFile)) {
                    MimeMultipart mimemultipart = new MimeMultipart();
                    for (int i = 0; i < attachFile.size(); i++) {
                        mimemultipart.addBodyPart(mimebodypart);
                        MimeBodyPart mimebodypart1 = new MimeBodyPart();
                        //attFile = new File(attachFile);
                        attFile = new File(attachFile.get(i));
                        if (attFile.exists()) {
                            filedatasource = new FileDataSource(attFile);
                            mimebodypart1.setDataHandler(new DataHandler(filedatasource));
                            mimebodypart1.setFileName(attFile.getName());
                            mimemultipart.addBodyPart(mimebodypart1);
                        }
                    }
                    MimeBodyPart mimebodyparturl = new MimeBodyPart();
                    mimebodyparturl.setContent(urlMessage, "text/html");
                    // MimeMultipart mimemultipart = new MimeMultipart();
                    mimemultipart.addBodyPart(mimebodyparturl);
                    mimemessage.setContent(mimemultipart);
                    //mimemessage.setContent(mimemultipart);
                }
            } else {
                MimeBodyPart mimebodypart = new MimeBodyPart();
                mimebodypart.setContent(bodyText, "text/html");
                MimeMultipart mimemultipart = new MimeMultipart();
                mimemultipart.addBodyPart(mimebodypart);

                //added by amar
                if (mailParams.getIncludeURLMessage()) {
                    MimeBodyPart mimebodypart1 = new MimeBodyPart();
                    mimebodypart1.setContent(urlMessage, "text/html");
                    mimemultipart.addBodyPart(mimebodypart1);
                    mimemessage.setContent(mimemultipart);
                } else {
                }
                //code added by Dinanath for embedded image displaying in mail
                MimeMultipart multipart = new MimeMultipart("related");
                BodyPart messageBodyPart = new MimeBodyPart();
                String htmlText = bodyText;
                //String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
                messageBodyPart.setContent(htmlText, "text/html");
//for first image
                multipart.addBodyPart(messageBodyPart);
//        DataSource fds = new FileDataSource("C:\\images\\dataflow.jpg");
                File tempFile = null;
                //filePath = "c://usr/local/cache";
                String filePath = mailParams.getFilePath();
                if (filePath == null) {
                    filePath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
                }
                tempFile = new File(filePath);
                if (tempFile.exists()) {
                } else {
                    tempFile.mkdirs();
                }
                if (mailParams.getIsHeaderLogoOn() != null && mailParams.getIsHeaderLogoOn().equalsIgnoreCase("on")) {
                    try {
                        tempFile = new File(filePath + File.separator + "HeaderImageOFHtmlEmail.jpg");
                        if (tempFile.exists()) {
                            messageBodyPart = new MimeBodyPart();

                            DataSource fds = new FileDataSource(tempFile);
                            messageBodyPart.setDataHandler(new DataHandler(fds));
                            messageBodyPart.addHeader("Content-ID", "<image>");
                            multipart.addBodyPart(messageBodyPart);
                            mimemessage.setContent(multipart);
                        }
                    } catch (MessagingException e) {
                        logger.error("Exception :", e);
                    }
                }
                if (mailParams.getIsFooterLogoOn() != null && mailParams.getIsFooterLogoOn().equalsIgnoreCase("on")) {
                    try {
                        //for second image
                        tempFile = new File(filePath + File.separator + "leftLogoImageOfScheduler.jpg");
                        if (tempFile.exists()) {
                            messageBodyPart = new MimeBodyPart();
                            DataSource fds2 = new FileDataSource(tempFile);
                            messageBodyPart.setDataHandler(new DataHandler(fds2));
                            messageBodyPart.addHeader("Content-ID", "<pidina_image>");
                            multipart.addBodyPart(messageBodyPart);
                            mimemessage.setContent(multipart);
                        }
                        //for third image
                        tempFile = new File(filePath + File.separator + "rightLogoImageOfScheduler.jpg");
                        if (tempFile.exists()) {
                            messageBodyPart = new MimeBodyPart();
                            DataSource fds3 = new FileDataSource(tempFile);
                            messageBodyPart.setDataHandler(new DataHandler(fds3));
                            messageBodyPart.addHeader("Content-ID", "<progendina_image>");
                            multipart.addBodyPart(messageBodyPart);
                            mimemessage.setContent(multipart);
                        }
                    } catch (MessagingException e) {
                        logger.error("Exception :", e);
                    }
                }
//code endded by Dinanath
            }
            Transport.send(mimemessage);
            return true;
        } catch (MessagingException e) {
            logger.error("Exception: ", e);
            return false;
        }
    }
    //added By Amar for Multiple Excel Export

    public boolean sendExportReportMail() throws AddressException, MessagingException {
        Session session = null;
        Properties properties = null;
        MimeMessage mimemessage = null;
        PbMailAuthenticator authenticator = null;
        File attFile = null;
        FileDataSource filedatasource = null;
        //Get the Email configuration settings used for sending email
        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
        if (emailConfig == null) {
            return false;
        }

//        String hostName = emailConfig.getSmtpHostName();
//        String authUser = emailConfig.getAuthUser();
//        String authPwd = emailConfig.getAuthPwd();
//        String fromAddr = emailConfig.getFromAddr();
//        String debug = emailConfig.getDebug();
//        String smtpPortNo = emailConfig.getSmtpPortNo();
//        String sslStatus = emailConfig.getSslStatus();
        String hostName = emailConfig.getSmtpHostName();
        String authUser = emailConfig.getAuthUser();
        String authPwd = emailConfig.getAuthPwd();
        String fromAddr = emailConfig.getFromAddr();
        String debug = emailConfig.getDebug();
        String smtpPortNo = emailConfig.getSmtpPortNo();
        String sslStatus = emailConfig.getSslStatus();

        //String hostName = "74.125.155.109";//"209.85.147.109"; //209.85.199.109 //"72.14.235.121";
        String toAddr = mailParams.getToAddr(); //"venkat@progenbusiness.com";
        String subject = mailParams.getSubject(); //"Test Subject";
        String bodyText = mailParams.getBodyText(); //"Sample mail subject to test";
        String fileUrl = mailParams.getFileUrl();  // text file url which has to be send
        boolean hasAttachment = mailParams.getHasAttach();
        //String attachFile = mailParams.getAttachFile();
        ArrayList<String> attachFile = mailParams.getAttachFile();
        //String urlMessage="To view the report online,please click  <a href="+fileUrl+" >Here</a> .\n\n";
        //urlMessage+=fileUrl;
        try {
            authenticator = new PbMailAuthenticator();
            InternetAddress toAddrs[] = InternetAddress.parse(toAddr);
            authenticator.myUserId = authUser;
            authenticator.myPassword = authPwd;
            properties = new Properties();
            properties.put("mail.smtp.host", hostName);
            properties.put("mail.smtp.port", smtpPortNo);
            properties.put("mail.debug", debug);
            //by g
            if (!sslStatus.equalsIgnoreCase("false")) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.starttls.enable", "false");
                properties.put("mail.smtp.ssl.checkserveridentity", "false");
                properties.put("mail.smtp.ssl.trust", "*");
            }
            properties.put("mail.smtp.auth", "true");
            session = Session.getInstance(properties, authenticator);
            session.setDebug(false);
            mimemessage = new MimeMessage(session);
            mimemessage.setFrom(new InternetAddress(fromAddr));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, toAddrs);
            //mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, ccAddrs);
            //mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, bccAddrs);
//            mimemessage.setContent(bodyText, "text/html");cc
            mimemessage.setSubject(subject);
            mimemessage.setSentDate(new Date());
            if (hasAttachment) {
                BodyPart mimebodypart = new MimeBodyPart();//cc
                //mimebodypart.setContent("HTML");
                //mimebodypart.setText(bodyText);
                mimebodypart.setContent(bodyText, "text/html");//cc
                if (attachFile != null && !"".equals(attachFile)) {
                    Multipart mimemultipart = new MimeMultipart();
                    for (int i = 0; i < attachFile.size(); i++) {
                        BodyPart mimebodypart1 = new MimeBodyPart();
                        //attFile = new File(attachFile);
                        attFile = new File(attachFile.get(i));
                        if (attFile.exists()) {
                            filedatasource = new FileDataSource(attFile);
                            mimebodypart1.setDataHandler(new DataHandler(filedatasource));
                            mimebodypart1.setFileName(attFile.getName().replace("Temp", ""));
                            mimemultipart.addBodyPart(mimebodypart1);
                        }
                        mimemultipart.addBodyPart(mimebodypart);
                    }
//                     MimeBodyPart mimebodyparturl = new MimeBodyPart();
//                     mimebodyparturl.setContent(urlMessage,"text/html");
//                    // MimeMultipart mimemultipart = new MimeMultipart();
//                     mimemultipart.addBodyPart(mimebodyparturl);
                    mimemessage.setContent(mimemultipart);
                    //mimemessage.setContent(mimemultipart);
                }
            } else {
                MimeBodyPart mimebodypart = new MimeBodyPart();
                mimebodypart.setContent(bodyText, "text/html");
                MimeMultipart mimemultipart = new MimeMultipart();
                mimemultipart.addBodyPart(mimebodypart);
                //added by amar
//                MimeBodyPart mimebodypart1 = new MimeBodyPart();
//                mimebodypart1.setContent(urlMessage,"text/html");
//                mimemultipart.addBodyPart(mimebodypart1);
                mimemessage.setContent(mimemultipart);
            }

            Transport.send(mimemessage);
            return true;
        } catch (MessagingException e) {
            logger.error("Exception: ", e);
            return false;
        }

    }
//Added by Amar to send log file

    public boolean sendLogFileMail() throws AddressException, MessagingException {
        Session session = null;
        Properties properties = null;
        MimeMessage mimemessage = null;
        PbMailAuthenticator authenticator = null;
        File attFile = null;
        FileDataSource filedatasource = null;
        //Get the Email configuration settings used for sending email
        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
        if (emailConfig == null) {
            return false;
        }
        String hostName = emailConfig.getSmtpHostName();
        String authUser = emailConfig.getAuthUser();
        String authPwd = emailConfig.getAuthPwd();
        String fromAddr = emailConfig.getFromAddr();
        String debug = emailConfig.getDebug();
        String smtpPortNo = emailConfig.getSmtpPortNo();
        String sslStatus = emailConfig.getSslStatus();

        //String hostName = "74.125.155.109";//"209.85.147.109"; //209.85.199.109 //"72.14.235.121";
        String toAddr = mailParams.getToAddr(); //"venkat@progenbusiness.com";
        String subject = mailParams.getSubject(); //"Test Subject";
        String bodyText = mailParams.getBodyText(); //"Sample mail subject to test";
        String fileUrl = mailParams.getFileUrl();  // text file url which has to be send
        boolean hasAttachment = mailParams.getHasAttach();
        //String attachFile = mailParams.getAttachFile();
        ArrayList<String> attachFile = mailParams.getAttachFile();
        String urlMessage = "Scheduler Failed To Run Properly. please open below attached text file to see All Exceptions.";
        //urlMessage+=fileUrl;
        try {
            authenticator = new PbMailAuthenticator();
            InternetAddress toAddrs[] = InternetAddress.parse(toAddr);

            authenticator.myUserId = authUser;
            authenticator.myPassword = authPwd;

            properties = new Properties();
            properties.put("mail.smtp.host", hostName);
            properties.put("mail.smtp.port", smtpPortNo);
            properties.put("mail.debug", debug);
            //by g
            if (!sslStatus.equalsIgnoreCase("false")) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.starttls.enable", "false");
                properties.put("mail.smtp.ssl.checkserveridentity", "false");
                properties.put("mail.smtp.ssl.trust", "*");
            }
            properties.put("mail.smtp.auth", "true");

            session = Session.getInstance(properties, authenticator);
            session.setDebug(false);

            mimemessage = new MimeMessage(session);
            mimemessage.setFrom(new InternetAddress(fromAddr));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, toAddrs);
            //mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, ccAddrs);
            //mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, bccAddrs);
            mimemessage.setContent(bodyText, "text/html");
            mimemessage.setSubject(subject);
            mimemessage.setSentDate(new Date());
            if (hasAttachment) {
                MimeBodyPart mimebodypart = new MimeBodyPart();
                //mimebodypart.setContent("HTML");

                //mimebodypart.setText(bodyText);
                mimebodypart.setContent(bodyText, "text/html");

                if (attachFile != null && !"".equals(attachFile)) {
                    MimeMultipart mimemultipart = new MimeMultipart();

                    for (int i = 0; i < attachFile.size(); i++) {
                        mimemultipart.addBodyPart(mimebodypart);

                        MimeBodyPart mimebodypart1 = new MimeBodyPart();
                        //attFile = new File(attachFile);
                        attFile = new File(attachFile.get(i));
                        if (attFile.exists()) {
                            filedatasource = new FileDataSource(attFile);
                            mimebodypart1.setDataHandler(new DataHandler(filedatasource));
                            mimebodypart1.setFileName(attFile.getName());
                            mimemultipart.addBodyPart(mimebodypart1);
                        }

                    }
                    MimeBodyPart mimebodyparturl = new MimeBodyPart();
                    mimebodyparturl.setContent(urlMessage, "text/html");
                    // MimeMultipart mimemultipart = new MimeMultipart();
                    mimemultipart.addBodyPart(mimebodyparturl);
                    mimemessage.setContent(mimemultipart);
                    //mimemessage.setContent(mimemultipart);
                }
            } else {
                MimeBodyPart mimebodypart = new MimeBodyPart();
                mimebodypart.setContent(bodyText, "text/html");
                MimeMultipart mimemultipart = new MimeMultipart();
                mimemultipart.addBodyPart(mimebodypart);

                //added by amar
                MimeBodyPart mimebodypart1 = new MimeBodyPart();
                mimebodypart1.setContent(urlMessage, "text/html");
                mimemultipart.addBodyPart(mimebodypart1);

                mimemessage.setContent(mimemultipart);
            }

            Transport.send(mimemessage);
            return true;
        } catch (MessagingException e) {
            logger.error("Exception: ", e);
            return false;
        }

    }
    // end of code
    //Code added By Amar
    //public boolean sendMailToUser(ArrayList<String> values, Map<String, List<String>> map, List<String> successEmailCountList, List<String> unsuccessEmailCountList, List<String> successEmailName, List<String> unsuccessEmailName) throws AddressException, MessagingException {

    public boolean sendMailToUser() {
        Session session = null;
        Properties properties = null;
        MimeMessage mimemessage = null;
        PbMailAuthenticator authenticator = null;

        File attFile = null;
        FileDataSource filedatasource = null;

        //Get the Email configuration settings used for sending email
        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
        if (emailConfig == null) {
            return false;
        }

        String hostName = emailConfig.getSmtpHostName();
        String authUser = emailConfig.getAuthUser();
        String authPwd = emailConfig.getAuthPwd();
        String fromAddr = emailConfig.getFromAddr();
        String debug = emailConfig.getDebug();
        String smtpPortNo = emailConfig.getSmtpPortNo();
        String sslStatus = emailConfig.getSslStatus();

        //String hostName = "74.125.155.109";//"209.85.147.109"; //209.85.199.109 //"72.14.235.121";
        String toAddr = mailParams.getToAddr(); //"venkat@progenbusiness.com";
        String subject = mailParams.getSubject(); //"Test Subject";
        String bodyText = mailParams.getBodyText(); //"Sample mail subject to test";
        String fileUrl = mailParams.getFileUrl();  // text file url which has to be send
        boolean hasAttachment = mailParams.getHasAttach();
        //String attachFile = mailParams.getAttachFile();
        ArrayList<String> attachFile = mailParams.getAttachFile();
        //urlMessage+=fileUrl;
        try {
            authenticator = new PbMailAuthenticator();
            InternetAddress toAddrs[] = InternetAddress.parse(toAddr);
            InternetAddress bccAddrs[] = InternetAddress.parse("monika.agrawal@progenbusiness.com, amar.pal@progenbusiness.com", true);
            authenticator.myUserId = authUser;
            authenticator.myPassword = authPwd;

            properties = new Properties();
            properties.put("mail.smtp.host", hostName);
            properties.put("mail.smtp.port", smtpPortNo);
            properties.put("mail.debug", debug);
            //by g
            if (!sslStatus.equalsIgnoreCase("false")) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.starttls.enable", "false");
                properties.put("mail.smtp.ssl.checkserveridentity", "false");
                properties.put("mail.smtp.ssl.trust", "*");
            }
            properties.put("mail.smtp.auth", "true");

            session = Session.getInstance(properties, authenticator);
            session.setDebug(false);

            mimemessage = new MimeMessage(session);
//            if(mailParams.getTAT())
            mimemessage.setFrom(new InternetAddress("schqatar@dataflowgroup.com"));
//            else
//                mimemessage.setFrom(new InternetAddress("noreply@dataflowgroup.com"));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, toAddrs);
            if (mailParams.getAutoMail()) {
                // mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse("schqatar@dataflowgroup.com"));
                mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse("schqatar@dataflowgroup.com"));
                mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, bccAddrs);
            }
            mimemessage.setContent(bodyText, "text/html");
            mimemessage.setSubject(subject);
            mimemessage.setSentDate(new Date());

//            MimeBodyPart mimebodypart = new MimeBodyPart();
//            mimebodypart.setContent(bodyText,"text/html");
//            MimeMultipart mimemultipart = new MimeMultipart();
//            mimemultipart.addBodyPart(mimebodypart);
            //added by amar
//            MimeBodyPart mimebodypart1 = new MimeBodyPart();
//            mimebodypart1.setContent(urlMessage,"text/html");
//            mimemultipart.addBodyPart(mimebodypart1);
            // mimemessage.setContent(mimemultipart);
            Transport.send(mimemessage);
            return true;
        } catch (MessagingException e) {
            logger.error("Exception: ", e);
            return false;
        }

    }
    //Added By Ram 11May20116 For Sending Mail to Vendor
    public boolean sendMailToVendor() {
        Session session = null;
        Properties properties = null;
        MimeMessage mimemessage = null;
        PbMailAuthenticator authenticator = null;

        File attFile = null;
        FileDataSource filedatasource = null;

        //Get the Email configuration settings used for sending email
        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
        if (emailConfig == null) {
            return false;
        }

        String hostName = emailConfig.getSmtpHostName();
        String authUser = emailConfig.getAuthUser();
        String authPwd = emailConfig.getAuthPwd();
        String fromAddr = emailConfig.getFromAddr();
        String debug = emailConfig.getDebug();
        String smtpPortNo = emailConfig.getSmtpPortNo();
        String sslStatus = emailConfig.getSslStatus();

        //String hostName = "74.125.155.109";//"209.85.147.109"; //209.85.199.109 //"72.14.235.121";
        String toAddr = mailParams.getToAddr(); //"venkat@progenbusiness.com";
        String subject = mailParams.getSubject(); //"Test Subject";
        String bodyText = mailParams.getBodyText(); //"Sample mail subject to test";
        String fileUrl = mailParams.getFileUrl();  // text file url which has to be send
        boolean hasAttachment = mailParams.getHasAttach();
        //String attachFile = mailParams.getAttachFile();
        ArrayList<String> attachFile = mailParams.getAttachFile();
        //urlMessage+=fileUrl;
        try {
            authenticator = new PbMailAuthenticator();
            InternetAddress toAddrs[] = InternetAddress.parse(toAddr);
            InternetAddress bccAddrs[] = InternetAddress.parse("ram.newas@progenbusiness.com", true);
            authenticator.myUserId = authUser;
            authenticator.myPassword = authPwd;

            properties = new Properties();
            properties.put("mail.smtp.host", hostName);
            properties.put("mail.smtp.port", smtpPortNo);
            properties.put("mail.debug", debug);
            //by g
            if (!sslStatus.equalsIgnoreCase("false")) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.starttls.enable", "false");
                properties.put("mail.smtp.ssl.checkserveridentity", "false");
                properties.put("mail.smtp.ssl.trust", "*");
            }
            properties.put("mail.smtp.auth", "true");

            session = Session.getInstance(properties, authenticator);
            session.setDebug(false);

            mimemessage = new MimeMessage(session);
//            if(mailParams.getTAT())
            mimemessage.setFrom(new InternetAddress("noreply@progenbusiness.com"));
//            else
//                mimemessage.setFrom(new InternetAddress("noreply@dataflowgroup.com"));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, toAddrs);
            if (mailParams.getAutoMail()) {
                // mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse("schqatar@dataflowgroup.com"));
//                mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse("schqatar@dataflowgroup.com"));
//                mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, bccAddrs);
            }
            mimemessage.setContent(bodyText, "text/html");
            mimemessage.setSubject(subject);
            mimemessage.setSentDate(new Date());
            
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = bodyText;
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            File tempFile = null;
            String filePath = null;
            filePath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
            tempFile = new File(filePath);
            if (tempFile.exists()) {
            } else {
                tempFile.mkdirs();
            }
            tempFile = new File(filePath + "/progen.jpg");
            DataSource fds = new FileDataSource(tempFile);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);
            mimemessage.setContent(multipart);
//            MimeBodyPart mimebodypart = new MimeBodyPart();
//            mimebodypart.setContent(bodyText,"text/html");
//            MimeMultipart mimemultipart = new MimeMultipart();
//            mimemultipart.addBodyPart(mimebodypart);
            //added by amar
//            MimeBodyPart mimebodypart1 = new MimeBodyPart();
//            mimebodypart1.setContent(urlMessage,"text/html");
//            mimemultipart.addBodyPart(mimebodypart1);
            // mimemessage.setContent(mimemultipart);
            Transport.send(mimemessage);
            return true;
        } catch (MessagingException e) {
            logger.error("Exception: ", e);
            return false;
        }

    }
    public static Connection connection = null;

//Code added By Amar
    public boolean sendMailToUserWithLogs(ArrayList<String> values, int totalMailSent, int m, List<String> Task_Name, List<String> Task_Status, List<String> Performer, List<String> Barcode1, List<String> Client_Name, List<String> Client_Full_Name, List<String> First_Name, List<String> Last_Name, List<String> Passport_No, List<String> Nationality, List<String> DateofBirth, List<String> Email, List<String> Status_URL, List<String> AST_Email_Address, List<String> Clinet_Refno, List<String> Task_Start_Date, List<String> Case_Start_Date, List<String> Barcode_id, List<String> Client_Unique_id, List<String> Report_Date, List<String> Report_Date0, List<String> Email_Sent_date, List<String> Mail_Delivered_Status_Flag, List<String> Mail_Failure_Success_Reason, List<String> reportIdList, List<String> reportNameList, String reportId, String reportName) throws ClassNotFoundException, ParseException {
        //public boolean sendMailToUserWithLogs(ArrayList<String> values,int totalMailSent,int m) throws ClassNotFoundException, ParseException{
        Session session = null;
        Properties properties = null;
        MimeMessage mimemessage = null;
        PbMailAuthenticator authenticator = null;

        File attFile = null;
        FileDataSource filedatasource = null;

        //Get the Email configuration settings used for sending email
        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
        if (emailConfig == null) {
            return false;
        }

        String hostName = emailConfig.getSmtpHostName();
        String authUser = emailConfig.getAuthUser();
        String authPwd = emailConfig.getAuthPwd();
        String fromAddr = emailConfig.getFromAddr();
        String debug = emailConfig.getDebug();
        String smtpPortNo = emailConfig.getSmtpPortNo();
        String sslStatus = emailConfig.getSslStatus();

        //String hostName = "74.125.155.109";//"209.85.147.109"; //209.85.199.109 //"72.14.235.121";
        String toAddr = mailParams.getToAddr(); //"venkat@progenbusiness.com";
        String subject = mailParams.getSubject(); //"Test Subject";
        String bodyText = mailParams.getBodyText(); //"Sample mail subject to test";

        try {
            authenticator = new PbMailAuthenticator();
            InternetAddress toAddrs[] = InternetAddress.parse(toAddr);
//            InternetAddress bccAddrs[] = InternetAddress.parse("monika.agrawal@progenbusiness.com, amar.pal@progenbusiness.com, nasim.ahmed@dataflowgroup.com, dinanath.parit@progenbusiness.com,sonia.nagpal@dataflowgroup.com",true);
            InternetAddress bccAddrs[] = InternetAddress.parse("monika.agrawal@progenbusiness.com, sonia.nagpal@dataflowgroup.com", true);
            authenticator.myUserId = authUser;
            authenticator.myPassword = authPwd;

            properties = new Properties();
            properties.put("mail.smtp.host", hostName);
            properties.put("mail.smtp.port", smtpPortNo);
            properties.put("mail.debug", debug);
            //by g
            if (!sslStatus.equalsIgnoreCase("false")) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.starttls.enable", "false");
                properties.put("mail.smtp.ssl.checkserveridentity", "false");
                properties.put("mail.smtp.ssl.trust", "*");
            }
            properties.put("mail.smtp.auth", "true");

            session = Session.getInstance(properties, authenticator);
            session.setDebug(false);

            mimemessage = new MimeMessage(session);
////            if(mailParams.getTAT())
//                mimemessage.setFrom(new InternetAddress("schqatar@dataflowgroup.com"));
//            else
            mimemessage.setFrom(new InternetAddress("noreply@dataflowgroup.com"));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, toAddrs);
            if (mailParams.getAutoMail()) {
                // mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse("schqatar@dataflowgroup.com"));
                //mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse("schqatar@dataflowgroup.com"));
                mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, bccAddrs);
            }
            mimemessage.setContent(bodyText, "text/html");
            mimemessage.setSubject(subject);
            mimemessage.setSentDate(new Date());
            //code added by Dinanath for embedded image displaying in mail
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = bodyText;
            //String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
//        DataSource fds = new FileDataSource("C:\\images\\dataflow.jpg");
            File tempFile = null;
            String filePath = null;
            //filePath = "c://usr/local/cache";
            filePath = File.separator + "usr" + File.separator + "local" + File.separator + "cache";
            tempFile = new File(filePath);
            if (tempFile.exists()) {
            } else {
                tempFile.mkdirs();
            }
            tempFile = new File(filePath + "/dataflow.jpg");
            DataSource fds = new FileDataSource(tempFile);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);
            mimemessage.setContent(multipart);
//code endded by Dinanath
//            MimeBodyPart mimebodypart = new MimeBodyPart();
//            mimebodypart.setContent(bodyText,"text/html");
//            MimeMultipart mimemultipart = new MimeMultipart();
//            mimemultipart.addBodyPart(mimebodypart);
            //added by amar
//            MimeBodyPart mimebodypart1 = new MimeBodyPart();
//            mimebodypart1.setContent(urlMessage,"text/html");
//            mimemultipart.addBodyPart(mimebodypart1);
            // mimemessage.setContent(mimemultipart);
//Container container1 = new Container();

//     if((values.get(11)).equalsIgnoreCase("NA")){
            Transport.send(mimemessage);
//                                params.setToAddr(values.get(11));
//                            }else{
//                            params.setToAddr("dinanath.parit@progenbusiness.com");
//                            }

//code added by Dinanath
            Task_Name.add(values.get(0));
            Task_Status.add(values.get(1));
            Performer.add(values.get(2));
            Barcode1.add(values.get(3));
            Client_Name.add(values.get(4));
            Client_Full_Name.add(values.get(5));
            First_Name.add(values.get(6));
            Last_Name.add(values.get(7));
            Passport_No.add(values.get(8));
            Nationality.add(values.get(9));
            DateofBirth.add(values.get(10));
            Email.add(values.get(11));
            Status_URL.add(values.get(12));
            AST_Email_Address.add(values.get(13));
            Barcode_id.add(values.get(14));
            Client_Unique_id.add(values.get(15));
            Clinet_Refno.add(values.get(16));
            Task_Start_Date.add(values.get(17));
            Case_Start_Date.add(values.get(18));

// for prevous date and time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String prevousDate = dateFormat.format(cal.getTime());
            Report_Date.add(prevousDate);
// for current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currnetDate = sdf.format(new Date());
            Email_Sent_date.add(currnetDate);
            Mail_Delivered_Status_Flag.add("1");
            Mail_Failure_Success_Reason.add("Email sent successfully");
            reportIdList.add(reportId);
            reportNameList.add(reportName);

            if (m == (totalMailSent - 1)) {
                String query = "insert into dataflow.prg_ar_mail_delivered_status_report(Task_Name,Task_Status,Performer,Barcode,Client_Name,Client_Full_Name,"
                        + "First_Name,Last_Name,Passport_No,Nationality,DateofBirth,Email,Status_URL,AST_Email_Address,Clinet_Refno,Task_Start_Date,"
                        + "Case_Start_Date,Barcode_id,Client_Unique_id,Report_Date,Email_Sent_date,Mail_Delivered_Status_Flag,Mail_Failure_Success_Reason,Report_id,Report_Name,custom_field_2)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    System.out.print("mysql class is loaded");

                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metaflow", "root", "India@123");
                    System.out.println("Connection has been established");

                    PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query.toString());
                    for (int i = 0; i < Task_Name.size(); i++) {
                        try {
                            ps.setString(1, Task_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(1, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(2, Task_Status.get(i));
                        } catch (SQLException excp) {
                            ps.setString(2, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(3, Performer.get(i));
                        } catch (SQLException excp) {
                            ps.setString(3, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(4, Barcode1.get(i));
                        } catch (SQLException excp) {
                            ps.setString(4, "");
                        }
                        try {
                            ps.setString(5, Client_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(5, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(6, Client_Full_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(6, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(7, First_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(7, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(8, Last_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(8, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(9, Passport_No.get(i));
                        } catch (SQLException excp) {
                            ps.setString(9, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(10, Nationality.get(i));
                        } catch (SQLException excp) {
                            ps.setString(10, Nationality.get(i));
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(DateofBirth.get(i));
                            java.sql.Date newDateofBirth = new java.sql.Date(utilDate2.getTime());
                            ps.setDate(11, newDateofBirth);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newDateofBirth = new java.sql.Date(d.getTime());
                            ps.setDate(11, newDateofBirth);
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(12, Email.get(i));
                        } catch (SQLException excp) {
                            ps.setString(12, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(13, Status_URL.get(i));
                        } catch (SQLException excp) {
                            ps.setString(13, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(14, AST_Email_Address.get(i));
                        } catch (SQLException excp) {
                            ps.setString(14, AST_Email_Address.get(i));
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(15, Clinet_Refno.get(i));
                        } catch (SQLException excp) {
                            ps.setString(15, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Task_Start_Date.get(i));
                            java.sql.Date newTask_Start_Date = new java.sql.Date(utilDate4.getTime());
                            ps.setDate(16, newTask_Start_Date);
                        } catch (ParseException | SQLException excp) {
                            Date d = new Date();
                            java.sql.Date newTask_Start_Date = new java.sql.Date(d.getTime());
                            ps.setDate(16, newTask_Start_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Case_Start_Date.get(i));
                            java.sql.Date newCase_Start_Date = new java.sql.Date(utilDate5.getTime());
                            ps.setDate(17, newCase_Start_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newCase_Start_Date = new java.sql.Date(d.getTime());
                            ps.setDate(17, newCase_Start_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            int barcodeid = Double.valueOf(Barcode_id.get(i).toString()).intValue();
                            ps.setInt(18, barcodeid);
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(18, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            int Client_Unique_id1 = Double.valueOf(Client_Unique_id.get(i).toString()).intValue();
                            ps.setInt(19, Client_Unique_id1);
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(19, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Report_Date.get(i));
                            java.sql.Date newReport_Date = new java.sql.Date(utilDate3.getTime());
                            ps.setDate(20, newReport_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newReport_Date = new java.sql.Date(d.getTime());
                            ps.setDate(20, newReport_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Email_Sent_date.get(i));
                            java.sql.Date newEmail_Sent_date = new java.sql.Date(utilDate1.getTime());
                            ps.setDate(21, newEmail_Sent_date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newEmail_Sent_date = new java.sql.Date(d.getTime());
                            ps.setDate(16, newEmail_Sent_date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setInt(22, Integer.parseInt(Mail_Delivered_Status_Flag.get(i)));
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(22, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(23, Mail_Failure_Success_Reason.get(i));
                        } catch (SQLException excp) {
                            ps.setString(23, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(24, reportIdList.get(i));
                        } catch (SQLException excp) {
                            ps.setString(24, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(25, reportNameList.get(i));
                        } catch (SQLException excp) {
                            ps.setString(25, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            java.util.Date date1 = new Date();
                            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                            String day = dayFormat.format(date1.getTime());
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                            String month = monthFormat.format(date1.getTime());
                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                            String year = yearFormat.format(date1.getTime());
                            String currentfullDate = year + month + day;
                            ps.setString(26, currentfullDate);
                        } catch (SQLException el) {
                            ps.setString(26, "");
                            logger.error("Exception :", el);
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                } catch (SQLException ex) {
                    logger.error("Exception: ", ex);
                }
            }//if end
            return true;
        } catch (SendFailedException sfex) {
            //  logger.error("Exception: ",sfex);
//             StackTraceElement[] stack = sfex.getStackTrace();
//    String exception = "";
//    for (StackTraceElement s : stack) {
//        exception = exception + s.toString() + "\n\t\t";
//    }
//    System.out.println(exception);

            Task_Name.add(values.get(0));
            Task_Status.add(values.get(1));
            Performer.add(values.get(2));
            Barcode1.add(values.get(3));
            Client_Name.add(values.get(4));
            Client_Full_Name.add(values.get(5));
            First_Name.add(values.get(6));
            Last_Name.add(values.get(7));
            Passport_No.add(values.get(8));
            Nationality.add(values.get(9));
            DateofBirth.add(values.get(10));
            Email.add(values.get(11));
            Status_URL.add(values.get(12));
            AST_Email_Address.add(values.get(13));
            Barcode_id.add(values.get(14));
            Client_Unique_id.add(values.get(15));
            Clinet_Refno.add(values.get(16));
            Task_Start_Date.add(values.get(17));
            Case_Start_Date.add(values.get(18));
// for prevous date and time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String prevousDate = dateFormat.format(cal.getTime());
            Report_Date.add(prevousDate);
// for current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currnetDate = sdf.format(new Date());
            Email_Sent_date.add(currnetDate);
            if (values.get(11).equalsIgnoreCase("NA")) {
                Mail_Delivered_Status_Flag.add("2");
                Mail_Failure_Success_Reason.add("Failed because email id was not applicable");
            } else {
                Mail_Delivered_Status_Flag.add("3");
                Mail_Failure_Success_Reason.add("Failed because email id was wrong or invalid");
            }
            reportIdList.add(reportId);
            reportNameList.add(reportName);

            if (m == (totalMailSent - 1)) {
                String query = "insert into dataflow.prg_ar_mail_delivered_status_report(Task_Name,Task_Status,Performer,Barcode,Client_Name,Client_Full_Name,"
                        + "First_Name,Last_Name,Passport_No,Nationality,DateofBirth,Email,Status_URL,AST_Email_Address,Clinet_Refno,Task_Start_Date,"
                        + "Case_Start_Date,Barcode_id,Client_Unique_id,Report_Date,Email_Sent_date,Mail_Delivered_Status_Flag,Mail_Failure_Success_Reason,Report_id,Report_Name,custom_field_2)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    System.out.print("mysql class is loaded");

                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metaflow", "root", "India@123");
                    System.out.println("Connection has been established");

                    PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query.toString());
                    for (int i = 0; i < Task_Name.size(); i++) {
                        try {
                            ps.setString(1, Task_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(1, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(2, Task_Status.get(i));
                        } catch (SQLException excp) {
                            ps.setString(2, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(3, Performer.get(i));
                        } catch (SQLException excp) {
                            ps.setString(3, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(4, Barcode1.get(i));
                        } catch (SQLException excp) {
                            ps.setString(4, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(5, Client_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(5, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(6, Client_Full_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(6, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(7, First_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(7, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(8, Last_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(8, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(9, Passport_No.get(i));
                        } catch (SQLException excp) {
                            ps.setString(9, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(10, Nationality.get(i));
                        } catch (SQLException excp) {
                            ps.setString(10, Nationality.get(i));
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(DateofBirth.get(i));
                            java.sql.Date newDateofBirth = new java.sql.Date(utilDate2.getTime());
                            ps.setDate(11, newDateofBirth);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newDateofBirth = new java.sql.Date(d.getTime());
                            ps.setDate(11, newDateofBirth);
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(12, Email.get(i));
                        } catch (SQLException excp) {
                            ps.setString(12, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(13, Status_URL.get(i));
                        } catch (SQLException excp) {
                            ps.setString(13, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(14, AST_Email_Address.get(i));
                        } catch (SQLException excp) {
                            ps.setString(14, AST_Email_Address.get(i));
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(15, Clinet_Refno.get(i));
                        } catch (SQLException excp) {
                            ps.setString(15, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Task_Start_Date.get(i));
                            java.sql.Date newTask_Start_Date = new java.sql.Date(utilDate4.getTime());
                            ps.setDate(16, newTask_Start_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newTask_Start_Date = new java.sql.Date(d.getTime());
                            ps.setDate(16, newTask_Start_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Case_Start_Date.get(i));
                            java.sql.Date newCase_Start_Date = new java.sql.Date(utilDate5.getTime());
                            ps.setDate(17, newCase_Start_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newCase_Start_Date = new java.sql.Date(d.getTime());
                            ps.setDate(17, newCase_Start_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            int barcodeid = Double.valueOf(Barcode_id.get(i).toString()).intValue();
                            ps.setInt(18, barcodeid);
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(18, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            int Client_Unique_id1 = Double.valueOf(Client_Unique_id.get(i).toString()).intValue();
                            ps.setInt(19, Client_Unique_id1);
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(19, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Report_Date.get(i));
                            java.sql.Date newReport_Date = new java.sql.Date(utilDate3.getTime());
                            ps.setDate(20, newReport_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newReport_Date = new java.sql.Date(d.getTime());
                            ps.setDate(20, newReport_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Email_Sent_date.get(i));
                            java.sql.Date newEmail_Sent_date = new java.sql.Date(utilDate1.getTime());
                            ps.setDate(21, newEmail_Sent_date);
                        } catch (ParseException | SQLException excp) {
                            Date d = new Date();
                            java.sql.Date newEmail_Sent_date = new java.sql.Date(d.getTime());
                            ps.setDate(21, newEmail_Sent_date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setInt(22, Integer.parseInt(Mail_Delivered_Status_Flag.get(i)));
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(22, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(23, Mail_Failure_Success_Reason.get(i));
                        } catch (SQLException excp) {
                            ps.setString(23, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(24, reportIdList.get(i));
                        } catch (SQLException excp) {
                            ps.setString(24, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(25, reportNameList.get(i));
                        } catch (SQLException excp) {
                            ps.setString(25, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            java.util.Date date1 = new Date();
                            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                            String day = dayFormat.format(date1.getTime());
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                            String month = monthFormat.format(date1.getTime());
                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                            String year = yearFormat.format(date1.getTime());
                            String currentfullDate = year + month + day;
                            ps.setString(26, currentfullDate);
                        } catch (SQLException el) {
                            ps.setString(26, "");
                            logger.error("Exception :", el);
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                } catch (SQLException ex) {
                    logger.error("Exception: ", ex);
                }
            }//if end
            return false;
        } catch (MessagingException mex) {
            // logger.error("Exception: ",mex);
//            StackTraceElement[] stack = mex.getStackTrace();
//            String exception = "";
//            for (StackTraceElement s : stack) {
//                exception = exception + s.toString() + "\n\t\t";
//            }
//            System.out.println(exception);
            Task_Name.add(values.get(0));
            Task_Status.add(values.get(1));
            Performer.add(values.get(2));
            Barcode1.add(values.get(3));
            Client_Name.add(values.get(4));
            Client_Full_Name.add(values.get(5));
            First_Name.add(values.get(6));
            Last_Name.add(values.get(7));
            Passport_No.add(values.get(8));
            Nationality.add(values.get(9));
            DateofBirth.add(values.get(10));
            Email.add(values.get(11));
            Status_URL.add(values.get(12));
            AST_Email_Address.add(values.get(13));
            Barcode_id.add(values.get(14));
            Client_Unique_id.add(values.get(15));
            Clinet_Refno.add(values.get(16));
            Task_Start_Date.add(values.get(17));
            Case_Start_Date.add(values.get(18));

// for prevous date and time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String prevousDate = dateFormat.format(cal.getTime());
            Report_Date.add(prevousDate);
// for current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currnetDate = sdf.format(new Date());
            Email_Sent_date.add(currnetDate);
            Mail_Delivered_Status_Flag.add("4");
            Mail_Failure_Success_Reason.add("Failed because of Authentication problem or Connection timed out");
            reportIdList.add(reportId);
            reportNameList.add(reportName);

            if (m == (totalMailSent - 1)) {
                String query = "insert into dataflow.prg_ar_mail_delivered_status_report(Task_Name,Task_Status,Performer,Barcode,Client_Name,Client_Full_Name,"
                        + "First_Name,Last_Name,Passport_No,Nationality,DateofBirth,Email,Status_URL,AST_Email_Address,Clinet_Refno,Task_Start_Date,"
                        + "Case_Start_Date,Barcode_id,Client_Unique_id,Report_Date,Email_Sent_date,Mail_Delivered_Status_Flag,Mail_Failure_Success_Reason,Report_id,Report_Name,custom_field_2)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    System.out.print("mysql class is loaded");

                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metaflow", "root", "India@123");
                    System.out.println("Connection has been established");

                    PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query.toString());
                    for (int i = 0; i < Task_Name.size(); i++) {
                        try {
                            ps.setString(1, Task_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(1, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(2, Task_Status.get(i));
                        } catch (SQLException excp) {
                            ps.setString(2, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(3, Performer.get(i));
                        } catch (SQLException excp) {
                            ps.setString(3, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(4, Barcode1.get(i));
                        } catch (SQLException excp) {
                            ps.setString(4, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(5, Client_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(5, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(6, Client_Full_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(6, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(7, First_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(7, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(8, Last_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(8, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(9, Passport_No.get(i));
                        } catch (SQLException excp) {
                            ps.setString(9, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(10, Nationality.get(i));
                        } catch (SQLException excp) {
                            ps.setString(10, Nationality.get(i));
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(DateofBirth.get(i));
                            java.sql.Date newDateofBirth = new java.sql.Date(utilDate2.getTime());
                            ps.setDate(11, newDateofBirth);
                        } catch (ParseException | SQLException excp) {
                            Date d = new Date();
                            java.sql.Date newDateofBirth = new java.sql.Date(d.getTime());
                            ps.setDate(11, newDateofBirth);
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(12, Email.get(i));

                        } catch (SQLException excp) {
                            ps.setString(12, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(13, Status_URL.get(i));
                        } catch (SQLException excp) {
                            ps.setString(13, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(14, AST_Email_Address.get(i));
                        } catch (SQLException excp) {
                            ps.setString(14, AST_Email_Address.get(i));
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(15, Clinet_Refno.get(i));
                        } catch (SQLException excp) {
                            ps.setString(15, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Task_Start_Date.get(i));
                            java.sql.Date newTask_Start_Date = new java.sql.Date(utilDate4.getTime());
                            ps.setDate(16, newTask_Start_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newTask_Start_Date = new java.sql.Date(d.getTime());
                            ps.setDate(16, newTask_Start_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Case_Start_Date.get(i));
                            java.sql.Date newCase_Start_Date = new java.sql.Date(utilDate5.getTime());
                            ps.setDate(17, newCase_Start_Date);

                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newCase_Start_Date = new java.sql.Date(d.getTime());
                            ps.setDate(17, newCase_Start_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            int barcodeid = Double.valueOf(Barcode_id.get(i).toString()).intValue();
                            ps.setInt(18, barcodeid);
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(18, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            int Client_Unique_id1 = Double.valueOf(Client_Unique_id.get(i).toString()).intValue();
                            ps.setInt(19, Client_Unique_id1);

                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(19, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Report_Date.get(i));
                            java.sql.Date newReport_Date = new java.sql.Date(utilDate3.getTime());
                            ps.setDate(20, newReport_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newReport_Date = new java.sql.Date(d.getTime());
                            ps.setDate(20, newReport_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Email_Sent_date.get(i));
                            java.sql.Date newEmail_Sent_date = new java.sql.Date(utilDate1.getTime());
                            ps.setDate(21, newEmail_Sent_date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newEmail_Sent_date = new java.sql.Date(d.getTime());
                            ps.setDate(21, newEmail_Sent_date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setInt(22, Integer.parseInt(Mail_Delivered_Status_Flag.get(i)));
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(22, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(23, Mail_Failure_Success_Reason.get(i));
                        } catch (SQLException excp) {
                            ps.setString(23, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(24, reportIdList.get(i));
                        } catch (SQLException excp) {
                            ps.setString(24, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(25, reportNameList.get(i));
                        } catch (SQLException excp) {
                            ps.setString(25, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            java.util.Date date1 = new Date();
                            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                            String day = dayFormat.format(date1.getTime());
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                            String month = monthFormat.format(date1.getTime());
                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                            String year = yearFormat.format(date1.getTime());
                            String currentfullDate = year + month + day;
                            ps.setString(26, currentfullDate);
                        } catch (SQLException el) {
                            ps.setString(26, "");
                            logger.error("Exception :", el);
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                } catch (SQLException ex) {
                    logger.error("Exception: ", ex);
                }
            }//if end
            return false;
        } catch (ClassNotFoundException | NumberFormatException e) {
            //  logger.error("Exception: ",e);
            //added by Dinanath for mail status
            Task_Name.add(values.get(0));
            Task_Status.add(values.get(1));
            Performer.add(values.get(2));
            Barcode1.add(values.get(3));
            Client_Name.add(values.get(4));
            Client_Full_Name.add(values.get(5));
            First_Name.add(values.get(6));
            Last_Name.add(values.get(7));
            Passport_No.add(values.get(8));
            Nationality.add(values.get(9));
            DateofBirth.add(values.get(10));
            Email.add(values.get(11));
            Status_URL.add(values.get(12));
            AST_Email_Address.add(values.get(13));
            Barcode_id.add(values.get(14));
            Client_Unique_id.add(values.get(15));
            Clinet_Refno.add(values.get(16));
            Task_Start_Date.add(values.get(17));
            Case_Start_Date.add(values.get(18));
// for prevous date and time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String prevousDate = dateFormat.format(cal.getTime());
            Report_Date.add(prevousDate);
// for current date and time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currnetDate = sdf.format(new Date());
            Email_Sent_date.add(currnetDate);
            Mail_Delivered_Status_Flag.add("5");
            Mail_Failure_Success_Reason.add("Failed because Unknown error");
            reportIdList.add(reportId);
            reportNameList.add(reportName);

            if (m == (totalMailSent - 1)) {
                String query = "insert into dataflow.prg_ar_mail_delivered_status_report(Task_Name,Task_Status,Performer,Barcode,Client_Name,Client_Full_Name,"
                        + "First_Name,Last_Name,Passport_No,Nationality,DateofBirth,Email,Status_URL,AST_Email_Address,Clinet_Refno,Task_Start_Date,"
                        + "Case_Start_Date,Barcode_id,Client_Unique_id,Report_Date,Email_Sent_date,Mail_Delivered_Status_Flag,Mail_Failure_Success_Reason,Report_id,Report_Name,custom_field_2)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    System.out.print("mysql class is loaded");

                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metaflow", "root", "India@123");
                    System.out.println("Connection has been established");

                    PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query.toString());
                    for (int i = 0; i < Task_Name.size(); i++) {
                        try {
                            ps.setString(1, Task_Name.get(i));
                        } catch (Exception excp) {
                            ps.setString(1, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(2, Task_Status.get(i));
                        } catch (SQLException excp) {
                            ps.setString(2, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(3, Performer.get(i));
                        } catch (SQLException excp) {
                            ps.setString(3, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(4, Barcode1.get(i));
                        } catch (SQLException excp) {
                            ps.setString(4, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(5, Client_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(5, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(6, Client_Full_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(6, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(7, First_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(7, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(8, Last_Name.get(i));
                        } catch (SQLException excp) {
                            ps.setString(8, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(9, Passport_No.get(i));
                        } catch (SQLException excp) {
                            ps.setString(9, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(10, Nationality.get(i));
                        } catch (SQLException excp) {
                            ps.setString(10, Nationality.get(i));
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(DateofBirth.get(i));
                            java.sql.Date newDateofBirth = new java.sql.Date(utilDate2.getTime());
                            ps.setDate(11, newDateofBirth);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newDateofBirth = new java.sql.Date(d.getTime());
                            ps.setDate(11, newDateofBirth);
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(12, Email.get(i));
                        } catch (SQLException excp) {
                            ps.setString(12, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(13, Status_URL.get(i));
                        } catch (SQLException excp) {
                            ps.setString(13, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(14, AST_Email_Address.get(i));
                        } catch (SQLException excp) {
                            ps.setString(14, AST_Email_Address.get(i));
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(15, Clinet_Refno.get(i));
                        } catch (SQLException excp) {
                            ps.setString(15, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Task_Start_Date.get(i));
                            java.sql.Date newTask_Start_Date = new java.sql.Date(utilDate4.getTime());
                            ps.setDate(16, newTask_Start_Date);
                        } catch (ParseException | SQLException excp) {
                            Date d = new Date();
                            java.sql.Date newTask_Start_Date = new java.sql.Date(d.getTime());
                            ps.setDate(16, newTask_Start_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Case_Start_Date.get(i));
                            java.sql.Date newCase_Start_Date = new java.sql.Date(utilDate5.getTime());
                            ps.setDate(17, newCase_Start_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newCase_Start_Date = new java.sql.Date(d.getTime());
                            ps.setDate(17, newCase_Start_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            int barcodeid = Double.valueOf(Barcode_id.get(i).toString()).intValue();
                            ps.setInt(18, barcodeid);
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(18, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            int Client_Unique_id1 = Double.valueOf(Client_Unique_id.get(i).toString()).intValue();
                            ps.setInt(19, Client_Unique_id1);
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(19, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Report_Date.get(i));
                            java.sql.Date newReport_Date = new java.sql.Date(utilDate3.getTime());
                            ps.setDate(20, newReport_Date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newReport_Date = new java.sql.Date(d.getTime());
                            ps.setDate(20, newReport_Date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            Date utilDate1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(Email_Sent_date.get(i));
                            java.sql.Date newEmail_Sent_date = new java.sql.Date(utilDate1.getTime());
                            ps.setDate(21, newEmail_Sent_date);
                        } catch (SQLException | ParseException excp) {
                            Date d = new Date();
                            java.sql.Date newEmail_Sent_date = new java.sql.Date(d.getTime());
                            ps.setDate(21, newEmail_Sent_date);
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setInt(22, Integer.parseInt(Mail_Delivered_Status_Flag.get(i)));
                        } catch (NumberFormatException | SQLException excp) {
                            ps.setInt(22, Integer.parseInt("0"));
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(23, Mail_Failure_Success_Reason.get(i));
                        } catch (SQLException excp) {
                            ps.setString(23, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(24, reportIdList.get(i));
                        } catch (SQLException excp) {
                            ps.setString(24, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            ps.setString(25, reportNameList.get(i));
                        } catch (SQLException excp) {
                            ps.setString(25, "");
                            logger.error("Exception :", excp);
                        }
                        try {
                            java.util.Date date1 = new Date();
                            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                            String day = dayFormat.format(date1.getTime());
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                            String month = monthFormat.format(date1.getTime());
                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                            String year = yearFormat.format(date1.getTime());
                            String currentfullDate = year + month + day;
                            ps.setString(26, currentfullDate);
                        } catch (SQLException el) {
                            ps.setString(26, "");
                            logger.error("Exception :", el);
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                } catch (SQLException ex) {
                    logger.error("Exception: ", ex);
                }
            }//if end
            return false;
        }
    }

//Added by mohit for Leela
    public void downloadEmailAttachments(String emailId) throws Exception {
        UploadingXmlIntoDatabase UpXml = new UploadingXmlIntoDatabase();
        Properties props = System.getProperties();
//        Properties properties = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        // server setting
//        properties.put("mail.pop3.host", "pop.gmail.com");
//        properties.put("mail.pop3.port", "995");
        // SSL setting
//        properties.setProperty("mail.pop3.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
//        properties.setProperty("mail.pop3.socketFactory.port",
//                String.valueOf("995"));
//        Session session = Session.getDefaultInstance(properties);
        Session session = Session.getDefaultInstance(props, null);

        String from = "";
        String subject = "";
        String sentDate = "";
        String contentType = "";
        String messageContent = "";
//        String DownloadLoc = "";
         StringBuilder DownloadLoc = new StringBuilder();
        String fileName = "";

//        try{
        // connects to the message store
//            Store store = session.getStore("pop3");
        Store store = session.getStore("imaps");
//             store.connect("vivek.ravi@progenbusiness.com", "qwertqwert123");
//                  store.connect("imap.gmail.com","vivek.ravi@progenbusiness.com", "qwer@1234");
        store.connect("imap.gmail.com", "noreply1@progenbusiness.com", "noreply1");
        // opens the inbox folder

        Folder folderInbox = store.getFolder("INBOX");
        folderInbox.open(Folder.READ_WRITE);
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
        Message messages[] = folderInbox.search(unseenFlagTerm);
        // fetches new messages from server
//            Message[] arrayMessages = folderInbox.getMessages();
        for (int i = messages.length - 1; i >= 0; i--) {
            String cont = "";
//                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//                            Date date = new Date();
//                         String todaysDate = dateFormat.format(date);
            Message message = messages[i];
            Address[] fromAddress = message.getFrom();
            Flags flags = message.getFlags();
            flags.getUserFlags();
            from = fromAddress[0].toString();
            subject = message.getSubject();
            sentDate = message.getSentDate().toString();
            contentType = message.getContentType();
            messageContent = "";
            fileName = "";
//                 DownloadLoc="D://Leela_Xmls";
//            DownloadLoc = "c://usr/local/cache/Leela_Xmls";
            DownloadLoc.append( "c://usr/local/cache/Leela_Xmls");
            Format formatter = new SimpleDateFormat("dd-MMM-yy");
            Date date = new Date();
            String s = formatter.format(date);
//            DownloadLoc = DownloadLoc + "/" + s;
            DownloadLoc.append("/").append( s);
            File tempFile = null;
//            String filePath = null;
//            filePath = "c://usr/local/cache/UploadedExcels";
            tempFile = new File(DownloadLoc.toString());
            if (tempFile.exists()) {
            } else {
                tempFile.mkdirs();
            }
            // store attachment file name, separated by comma
//            String attachFiles = "";
            StringBuilder attachFiles = new StringBuilder();
            if (contentType.contains("multipart")) {
                // content may contain attachments
                Multipart multiPart = (Multipart) message.getContent();
                int numberOfParts = multiPart.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                    fileName = part.getFileName();
                    if (fileName != null) {
//                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        if (fileName.contains(".xml") || fileName.contains(".XML")) {
                            // this part is attachment
//                            String fileName = part.getFileName();
//                            DownloadLoc=DownloadLoc+ File.separator + fileName;
//                            attachFiles += fileName + ", ";
                            attachFiles.append(fileName ).append( ", ");
//                            || from.contains("jey.balaji@theleela.com") || from.contains("reservations.newdelhi@theleela.com")
//                            from.contains("ocis.reports@theleela.com") && && subject.contains("profile2")
                            if (from.contains("ocis.reports@theleela.com") || from.contains("reservations.newdelhi@theleela.com")
                                    || from.contains("jey.balaji@theleela.com") || from.contains("it.kovalam@theleela.com")
                                    || from.contains("it.goa@theleela.com") || from.contains("reservations.gurgaon-delhi@theleela.com")
                                    || from.contains("rakesh.mehta@theleela.com") || from.contains("daniel.dsuza@theleela.com")
                                    || from.contains("mum.gibyroom@theleela.com") || from.contains("udp.gibyroom@theleela.com")
                                    || from.contains("del.gibyroom@theleela.com") || from.contains("goa.gibyroom@theleela.com")
                                    || from.contains("is.bangalore@theleela.com") || from.contains("kov.gibyroom@theleela.com")
                                    || from.contains("ggn.gibyroom@theleela.com") || from.contains("chn.gibyroom@theleela.com") || from.contains("mohit.jain@progenbusiness.com")
                                    || from.contains("chn.financepg@theleela.com") || from.contains("udp.financepg@theleela.com") || from.contains("mum.financepg@theleela.com")
                                    || from.contains("kov.financepg@theleela.com") || from.contains("del.financepg@theleela.com") || from.contains("goa.financepg@theleela.com")
                                    || from.contains("ggn.financepg@theleela.com") || from.contains("udp.financepg@theleela.com") || from.contains("bg.financepg@theleela.com")
                                    || from.contains("gaurav.sharma@theleela.com") || from.contains("ranjana.nikam@theleela.com")) {
                                cont = UpXml.GetLocationName(from, subject);
                                if (!cont.equalsIgnoreCase("")) {

//                                 DownloadLoc=DownloadLoc+ File.separator + fileName;
//                                    DownloadLoc = DownloadLoc + File.separator + cont + "_" + s + ".xml";
                                    DownloadLoc.append(File.separator).append(cont).append("_").append(s).append(".xml");
//                                    DownloadLoc=DownloadLoc+ File.separator + from.split("@")[0]+"_"+subject+"_"+todaysDate+".xml";
                                    part.saveFile(DownloadLoc.toString());
                                    try {
                                        if (cont.equalsIgnoreCase("Mumbai_profile2") || cont.equalsIgnoreCase("Mumbai_profile2_Arrival") || cont.equalsIgnoreCase("Mumbai_Guest_Arrival")) {
                                            FileInputStream fileInputStream = new FileInputStream(DownloadLoc.toString());
                                            FileChannel fc = fileInputStream.getChannel();
                                            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                                            /* Instead of using default, pass in a decoder. */
                                            String newfile = Charset.defaultCharset().decode(bb).toString();
                                            //       newfile=(newfile.replaceAll("[\\Ã­\\Â¿\\Ãƒ\\Â¼\\Ã¯\\Â½\\ï¿½\\Ã¼\\Å“\\Ã¶\\Ã¼\\Â¶\\ÃƒÂ¸\\Ã³\\Â³\\Å¸\\ÃŸ\\ÃƒÂ¡\\Æ’Ã‚]", "")).replaceAll("Â­a Gar", "a Gar").replaceAll("MartÂ­n", "Martin");
                                            newfile = (newfile.replaceAll("[^a-zA-Z0-9_ -~!@#$%^&*/]", ""));  //Added by Ram 26Oct15 for removing special character

//                                            DownloadLoc = DownloadLoc.replace(cont, cont + "_new");
                                            DownloadLoc.append(DownloadLoc.toString().replace(cont, cont + "_new"));
                                            File tempFilenew = new File(DownloadLoc.toString());
                                            byte[] bytes = newfile.getBytes();
                                            FileOutputStream fos = null;
                                            tempFilenew.createNewFile();
                                            fos = new FileOutputStream(tempFilenew);
                                            fos.write(bytes);
                                            if (fos != null) {
                                                fos.close();
                                            }
                                        }
                                        String targettablename = (UpXml.TruncateLoadTracker(cont)).trim();
                                        String status = UpXml.UploadXML(from, subject, DownloadLoc.toString(), targettablename, cont);
                                        if (status.contains("success")) {
                                            sendXmlLoadingMail(from, subject, emailId, sentDate, status.split("::")[1]);
                                        }
                                        message.setFlag(Flag.SEEN, true);
                                    } catch (IOException | ClassNotFoundException | SQLException | MessagingException ex) {
                                        logger.error("Exception: ", ex);
                                        sendXmlExceptionMail(from, subject, emailId, sentDate, ex);
                                        message.setFlag(Flag.SEEN, true);
                                        message.setFlag(Flag.FLAGGED, true);
                                        logger.error("Exception: ", ex);
                                    }
                                } else {
                                    message.setFlag(Flag.SEEN, true);

                                }

                            }

                        } else {
                            // this part may be the message content
                            messageContent = part.getContent().toString();
                        }
                    }
                }

                if (attachFiles.length() > 1) {
                    attachFiles = new StringBuilder(attachFiles.substring(0, attachFiles.length() - 2));
                }
            } else if (contentType.contains("text/plain")
                    || contentType.contains("text/html")) {
                Object content = message.getContent();
                if (content != null) {
                    messageContent = content.toString();
                }
            }
            // print out details of each message
//                 message.setFlag(Flag.SEEN, true);
        }

        // disconnect
        folderInbox.close(false);
        store.close();
//        }
//        catch (Exception ex) {
//            sendXmlExceptionMail(from,subject,emailId,sentDate,ex);
//            logger.error("Exception: ",ex);
//        }
//                         catch (IOException ex) {
//            logger.error("Exception: ",ex);
//        }
    }
    //added by Dinanath for testing failed email
    ArrayList<Integer> Fldsno = new ArrayList<Integer>();
    ArrayList<String> FldfromAddress = new ArrayList<String>();
    ArrayList<String> FldSubject = new ArrayList<String>();
    ArrayList<String> FldSentDate = new ArrayList<String>();
    ArrayList<String> FldMessageBody = new ArrayList<String>();

    public void downloadFailedEmailInbox(String emailId) throws Exception {

        System.out.println("Undelivered Mail Status has been called");
        Properties props = System.getProperties();
        props.setProperty("mail.host", "mail.dataflowgroup.com");
        props.setProperty("mail.port", "995");
        props.setProperty("mail.transport.protocol", "imap");
        Session session = Session.getDefaultInstance(props, null);
//        Store store = session.getStore("imaps");
        Store store = session.getStore("imap");
        System.out.println("Trying to connect with noreply@dataflowgroup.com Please Wait...........................");
        store.connect("10.20.1.102", "noreply@dataflowgroup.com", "@Br0Zn90l");
//        store.connect("mail.dataflowgroup.com", "noreply@dataflowgroup.com", "@Br0Zn90l");
        System.out.println("Successfully connected  and it may take several minutes while reading inbox");
        System.out.println("Please wait! Reading inbox mail is continue................");

        String from = "";
        String subject = "";
        String sentDate = "";
        String contentType = "";
        String messageContent = "";

        Folder folderInbox = store.getFolder("INBOX");
        folderInbox.open(Folder.READ_WRITE);
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);//false for reading only unread message and true vice versa
        Message messages[] = folderInbox.search(unseenFlagTerm);
        // fetches new messages from server
        for (int i = messages.length - 1; i >= 0; i--) {
            Message message = messages[i];
            try {

                Address[] fromAddress = message.getFrom();
                Flags flags = message.getFlags();
                flags.getUserFlags();
                from = fromAddress[0].toString();
                Address[] replyAddress = message.getReplyTo();
                String replto = replyAddress[0].toString();
                subject = message.getSubject();
                sentDate = message.getSentDate().toString();
                contentType = message.getContentType();
                messageContent = "";

                if (contentType.contains("multipartaaaaaaaaa")) {
                    // content may contain attachments
                } else if (contentType.toString().contains("TEXT/PLAIN") || contentType.toString().contains("text/html")) {
                    try {
                        Object content = message.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    } catch (IOException | MessagingException e) {
//                        logger.error("Exception: ",e);
                    }
                } else if (contentType.toString().contains("multipart/REPORT") || contentType.toString().contains("multipart/REPORT")) {
                    try {
                        MultipartReport mr = (MultipartReport) message.getContent();
                        messageContent = mr.getText();
                    } catch (IOException | MessagingException e) {
//                        logger.error("Exception: ",e);
                    }

                } else if (contentType.toString().contains("multipart/RELATED") || contentType.toString().contains("multipart/related")) {
                    try {
                        Multipart mp = (Multipart) message.getContent();
                        String text = null;
                        for (int k = 0; k < mp.getCount(); k++) {
                            Part bp = mp.getBodyPart(k);
                            if (bp.isMimeType("text/plain")) {
                                if (text == null) //                        text = getText(bp);
                                {
                                    if (bp.isMimeType("text/*")) {
                                        messageContent = (String) bp.getContent();
                                    }
                                }
                                continue;
                            }

                        }
                    } catch (IOException | MessagingException e) {
//                        logger.error("Exception: ",e);
                    }
                } else if (contentType.toString().contains("multipart/Mixed") || contentType.toString().contains("multipart/MIXED")) {
                    try {
                        Multipart mp = (Multipart) message.getContent();
                        String text = null;
                        for (int k = 0; k < mp.getCount(); k++) {
                            Part bp = mp.getBodyPart(k);
                            if (bp.isMimeType("text/plain")) {
                                if (text == null) //                        text = getText(bp);
                                {
                                    if (bp.isMimeType("text/*")) {
                                        messageContent = (String) bp.getContent();
                                    }
                                }
                                continue;
                            }

                        }
                    } catch (IOException | MessagingException e) {
                        logger.error("Exception: ", e);
                    }
                } else if (contentType.toString().contains("multipart/ALTERNATIVE") || contentType.toString().contains("multipart/ALTERNATIVE")) {
                    // prefer html text over plain text
                    try {
                        Multipart mp = (Multipart) message.getContent();
                        String text = null;
                        for (int k = 0; k < mp.getCount(); k++) {
                            Part bp = mp.getBodyPart(k);
                            if (bp.isMimeType("text/plain")) {
                                if (text == null) //                        text = getText(bp);
                                {
                                    if (bp.isMimeType("text/*")) {
                                        messageContent = (String) bp.getContent();
                                    }
                                }
                                continue;
                            }
                        }
                    } catch (IOException | MessagingException e) {
                        logger.error("Exception: ", e);
                    }
                }

                Fldsno.add(i + 1);
                FldfromAddress.add(from);
                FldSubject.add(subject);
                FldSentDate.add(sentDate);
                FldMessageBody.add(messageContent);

                if (i == 0) {
                    System.out.println("No of Messages are :" + FldMessageBody.size());
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        System.out.println("mysql driver class is loaded");

                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metaflow", "root", "India@123");
                        System.out.println("Connection has been established");

                        String selectSQL = "SELECT Email, Status_id, Report_id FROM dataflow.prg_ar_mail_delivered_status_report where custom_field_2=?";
                        PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(selectSQL);
                        java.util.Date date1 = new Date();
                        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                        String day = dayFormat.format(date1.getTime());
                        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                        String month = monthFormat.format(date1.getTime());
                        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                        String year = yearFormat.format(date1.getTime());
                        String currentfullDate = year + month + day;
//                    String currentfullDate="20150916";

                        preparedStatement.setString(1, currentfullDate);
                        ResultSet rs = preparedStatement.executeQuery();
                        while (rs.next()) {
                            String emailIds = rs.getString("Email");
                            String StatusIds = rs.getString("Status_id");

                            if (!emailIds.equals("NA")) {
                                if (FldMessageBody.toString().contains(emailIds)) {
                                    try {
                                        String deleteQuery = "update dataflow.prg_ar_mail_delivered_status_report set Mail_Delivered_Status_Flag='3', Mail_Failure_Success_Reason='Failed because email id was wrong or invalid' where Email=? and custom_field_2=?";
                                        PreparedStatement preparedStatement2 = (PreparedStatement) connection.prepareStatement(deleteQuery);
                                        preparedStatement2.setString(1, emailIds.toString());
                                        preparedStatement2.setString(2, currentfullDate);
                                        int b = preparedStatement2.executeUpdate();
                                    } catch (SQLException e) {
                                        logger.error("Exception: ", e);
                                    }
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        logger.error("Exception: ", ex);
                    }
                }//if
            } catch (ClassNotFoundException | MessagingException ff) {
                Object content;
                try {
                    content = message.getContent();
                } catch (MessagingException e) {
                    if (message instanceof MimeMessage && "Unable to load BODYSTRUCTURE".equalsIgnoreCase(e.getMessage())) {
                        content = new MimeMessage((MimeMessage) message).getContent();
                    } else {
                        throw e;
                    }
                }
            }
        }
        System.out.println("Undeliverd mail Reading/Updating to database has completed");
        folderInbox.close(false);
        store.close();
    }

//code Added by mohit
    public void sendXmlLoadingMail(String from, String subject, String emailId, String sentDate, String totalrows) throws IOException {
        Totalloc++;
        String[] mailids = emailId.split(",");
        UploadingXmlIntoDatabase UpXml = new UploadingXmlIntoDatabase();
        PbMailParams params = null;
        PbMail mailer = null;
        String cont = UpXml.GetLocationName(from, subject);

        UpXml.InsertIntoTrackerMaster(cont, "Scheduler", totalrows, "Succeed");
        UpXml.UpdateLoadTracker(cont);
        logrw.fileWriter("(" + Totalloc + ") Total " + totalrows + " Rows Inserted for " + cont + " Location  Mail_Recieved_Date--" + sentDate + "\n");

//                for (int i = 0; i < mailids.length; i++) {
//                    params = new PbMailParams();
//                    params.setBodyText("Xml has been Loaded into database table for  "+cont+"  Mail_Recieved_Date--"+sentDate+"..<html><body><br>--Total "+totalrows+" Rows Inserted-- </body></html>");
//
//
//                    params.setToAddr(mailids[i]);
//                    params.setSubject("XML Loaded for "+cont);
//                    params.setHasAttach(false);
////                     mailer = new PbMail(params);
////                    mailer.sendMail();.
//                    try {
//                        mailer = new PbMail(params);
//                        Boolean b=mailer.sendMail();
////                       
//                        int count=0;
//
//                        while(!(b) && count<5)
//                        {
//                             b=mailer.sendMail();
//                              count++;
////                              
//                        }
//
//                    } catch (Exception e) {
//                        logger.error("Exception: ",e);
//
//                    }
//                }
    }
//code Added by mohit

    public void sendXmlExceptionMail(String from, String subject, String emailId, String sentDate, Exception ex) throws IOException {
        Totalloc++;
        String[] mailids = emailId.split(",");
        UploadingXmlIntoDatabase UpXml = new UploadingXmlIntoDatabase();
        PbMailParams params = null;
        PbMail mailer = null;
        String cont = UpXml.GetLocationName(from, subject);

        UpXml.InsertIntoTrackerMaster(cont, "Scheduler", "0 and Exception is " + ex, "Failed");
        logrw.fileWriter("(" + Totalloc + ") ********************Xml Loading Exception for  " + cont + " ___Mail_Recieved_Date--" + sentDate + "******************** \n "
                + "*******************Exception is  '" + ex + "' ****************\n");
        System.out.println("(" + Totalloc + ") ********************Xml Loading Exception for  " + cont + " ___Mail_Recieved_Date--" + sentDate + "******************** \n "
                + "*******************Exception is  '" + ex + "' ****************\n");
        for (int i = 0; i < mailids.length; i++) {
            params = new PbMailParams();
            params.setBodyText("Xml Loading Exception for  " + cont + " ___Mail_Recieved_Date--" + sentDate + "..<html><body><br>Exception is  ~~" + ex + " ~~</body></html>");

            params.setToAddr(mailids[i]);
            params.setSubject("XML Loading Exception for " + cont);
            params.setHasAttach(false);
            try {
                mailer = new PbMail(params);
                Boolean b = mailer.sendMail();
//                       
                int count = 0;

                while (!(b) && count < 5) {
                    b = mailer.sendMail();
                    count++;
//                              
                }

            } catch (MessagingException e) {
                logger.error("Exception: ", e);

            }
        }
    }

    public boolean sendMailToUserFromMOMS() {
        Session session = null;
        Properties properties = null;
        MimeMessage mimemessage = null;
        PbMailAuthenticator authenticator = null;

        File attFile = null;
        FileDataSource filedatasource = null;

        //Get the Email configuration settings used for sending email
        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
        if (emailConfig == null) {
            return false;
        }

        String hostName = emailConfig.getSmtpHostName();
        String authUser = emailConfig.getAuthUser();
        String authPwd = emailConfig.getAuthPwd();
        String fromAddr = emailConfig.getFromAddr();
        String debug = emailConfig.getDebug();
        String smtpPortNo = emailConfig.getSmtpPortNo();
        String sslStatus = emailConfig.getSslStatus();

        //String hostName = "74.125.155.109";//"209.85.147.109"; //209.85.199.109 //"72.14.235.121";
        String toAddr = mailParams.getToAddr(); //"venkat@progenbusiness.com";
        String subject = mailParams.getSubject(); //"Test Subject";
        String bodyText = mailParams.getBodyText(); //"Sample mail subject to test";
        String fileUrl = mailParams.getFileUrl();  // text file url which has to be send
        boolean hasAttachment = mailParams.getHasAttach();
        //String attachFile = mailParams.getAttachFile();
        ArrayList<String> attachFile = mailParams.getAttachFile();
        //urlMessage+=fileUrl;
        try {
            authenticator = new PbMailAuthenticator();
            InternetAddress toAddrs[] = InternetAddress.parse(toAddr);
            InternetAddress bccAddrs[] = InternetAddress.parse("monika.agrawal@progenbusiness.com, amar.pal@progenbusiness.com, dinanath.parit@progenbusiness.com, amit@progenbusiness.com", true);
            authenticator.myUserId = authUser;
            authenticator.myPassword = authPwd;

            properties = new Properties();
            properties.put("mail.smtp.host", hostName);
            properties.put("mail.smtp.port", smtpPortNo);
            properties.put("mail.debug", debug);
            //by g
            if (!sslStatus.equalsIgnoreCase("false")) {
                properties.put("mail.smtp.starttls.enable", "true");
            } else {
                properties.put("mail.smtp.starttls.enable", "false");
                properties.put("mail.smtp.ssl.checkserveridentity", "false");
                properties.put("mail.smtp.ssl.trust", "*");
            }
            properties.put("mail.smtp.auth", "true");

            session = Session.getInstance(properties, authenticator);
            session.setDebug(false);

            mimemessage = new MimeMessage(session);
//            if(mailParams.getTAT())
            mimemessage.setFrom(new InternetAddress("moms@dataflowgroup.com"));
//            else
//                mimemessage.setFrom(new InternetAddress("noreply@dataflowgroup.com"));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, toAddrs);
            if (mailParams.getAutoMail()) {
                // mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse("schqatar@dataflowgroup.com"));
                mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse("moms@dataflowgroup.com"));
                mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, bccAddrs);
            }
            mimemessage.setContent(bodyText, "text/html");
            mimemessage.setSubject(subject);
            mimemessage.setSentDate(new Date());

//            MimeBodyPart mimebodypart = new MimeBodyPart();
//            mimebodypart.setContent(bodyText,"text/html");
//            MimeMultipart mimemultipart = new MimeMultipart();
//            mimemultipart.addBodyPart(mimebodypart);
            //added by amar
//            MimeBodyPart mimebodypart1 = new MimeBodyPart();
//            mimebodypart1.setContent(urlMessage,"text/html");
//            mimemultipart.addBodyPart(mimebodypart1);
            // mimemessage.setContent(mimemultipart);
            Transport.send(mimemessage);
            return true;
        } catch (MessagingException e) {
            logger.error("Exception: ", e);
            return false;
        }

    }

    // end of code
    public static void main(String[] args) {
        prg.util.PbMail mail = new prg.util.PbMail();

        try {
            if (mail.sendMail()) {
            } else {
            }
        } catch (MessagingException e) {
        }
    }
}
