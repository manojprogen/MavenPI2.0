/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uploadfile.excel;

/**
 *
 * @author Dinanath Parit
 */
import com.mysql.jdbc.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.CallableStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import prg.util.PbMail;
import prg.util.PbMailParams;

public class FTPReadCSVFile {

    public static String sDir = "";
    public static FTPClient ftpClient = new FTPClient();
    public static int replyCode;
    public static boolean success = false;
    public static String server = "ftp.mydrreddys.com";//server ip
//    public static String server = "localhost";//server ip
    //int port = ;
    public static String user = "bfrow2";//drl
//    public static String user = "progen";//localhost
    public static String pass = "12345";//drl
//    public static String pass = "password";//localhost
    public static Connection connection = null;
    public static CallableStatement proc = null;
    public static int failedRowsGRIRDATA = 0;
    public static int successRowsGRIRDATA = 0;
    public static int totalRowsGRIRDATA = 0;
    public static int failedRowsPOHEADER = 0;
    public static int successRowsPOHEADER = 0;
    public static int totalRowsPOHEADER = 0;
    public static int failedRowsFBL3NDATA = 0;
    public static int successRowsFBL3NDATA = 0;
    public static int totalRowsFBL3NDATA = 0;
    public static List exceptionGRIRDATA = new ArrayList();
    public static List exceptionPOHEADER = new ArrayList();
    public static List exceptionFBL3NDATA = new ArrayList();
    long start_time = 0;
    long end_time = 0;
    Date date1 = null;
    Date date2 = null;

    public static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public static void main(String[] agrs) throws IOException, ClassNotFoundException, SQLException {
        FTPReadCSVFile csv = new FTPReadCSVFile();
        csv.csvfile();
    }

    public void csvfile() throws IOException, ClassNotFoundException, SQLException {
        try {

//            try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.print("mysql class is loaded ");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataspend1", "root", "Pr0gen");//drl
//                connection = DriverManager.getConnection("jdbc:mysql://152.63.1.195:3306/dataspend1", "root", "Pr0gen");//from localhost
//                 connection = DriverManager.getConnection("jdbc:mysql://183.82.3.61:3306/test", "root", "root");//
//                 connection = DriverManager.getConnection("jdbc:mysql://192.168.0.251:3306/test", "root", "root");//
            System.out.println("Connection has been established");
//            } catch (SQLException ex) {
//                Logger.getLogger(FTPReadCSVFile.class.getName()).log(Level.SEVERE, null, ex);
//            }

            InetAddress addr = InetAddress.getLocalHost();
            InetAddress drladdr1 = InetAddress.getByName(server);

            String ipAddress = addr.getHostAddress();
            System.out.println("IP address of localhost from Java Program is: " + ipAddress);

            String drlIpAddress2 = drladdr1.getHostAddress();
            System.out.println("IP address of ftp.mydrreddys.com from Java Program is: " + drlIpAddress2);

//            String hostname = addr.getHostName();
//            String hostname2 = drladdr1.getHostName();

            ftpClient.connect(drlIpAddress2, 21);//drl
//            ftpClient.connect(ipAddress, 21);//local
            showServerReply(ftpClient);
            replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                //return "false";
            }
            success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            start_time = System.currentTimeMillis();
            date1 = new Date();
            if (!success) {
                System.out.println("Could not login to the ftp server");
                // return "false";
            } else {
                System.out.println("User successfully logged in.");
                ftpClient.changeWorkingDirectory("ftp_file_directory");//directory Changed
                FTPFile[] files = ftpClient.listFiles();//get list of files and folders
                
                for (int ii = 0; ii < files.length; ii++) {
                    InputStream fstream = null;
                    BufferedReader br = null;
                    DataInputStream in = null;
                    if (files[ii].isDirectory()) {//check for file or directory
                        System.out.println("Directory name  " + files[ii].getName());//print directory name
                    } else {
                        System.out.println("file name  " + files[ii].getName());//print file name

                        if (files[ii].getName().equalsIgnoreCase("GRIRDATA.csv")) {
                            String sqlQueryTruncateData = "truncate table pogrdata";
                            PreparedStatement pstrunc = (PreparedStatement) connection.prepareStatement(sqlQueryTruncateData.toString());
                            pstrunc.execute();
                            ftpClient.connect(drlIpAddress2);//drl
//                            ftpClient.connect(ipAddress);//local
                            showServerReply(ftpClient);
                            replyCode = ftpClient.getReplyCode();
                            if (!FTPReply.isPositiveCompletion(replyCode)) {
                                System.out.println("Operation failed. Server reply code: " + replyCode);
                                //return "false";
                            }
                            success = ftpClient.login(user, pass);
                            showServerReply(ftpClient);
                            ftpClient.changeWorkingDirectory("path_ to_file_directory");
                            fstream = ftpClient.retrieveFileStream(files[ii].getName());
                            in = new DataInputStream(fstream);
                            br = new BufferedReader(new InputStreamReader(in));
                            String strLine = null;
                            int count = 1;
                            while ((strLine = br.readLine()) != null) {
                                String dataline = strLine;
                                if (count > 1) {
                                    exeQuery(dataline);
                                }
                                count++;
                                totalRowsGRIRDATA++;
                                if (strLine.trim().length() == 0) {
                                    continue;  // Skip blank lines
                                }
                            }
                        }//else ennded

                        if (files[ii].getName().contains("Progen-1csv") || files[ii].getName().contains("Progen-2csv")|| files[ii].getName().contains("Progen-3csv")) {
                            String sqlQueryTruncateData2 = "truncate table poitemdata";
                            PreparedStatement pstrunc2 = (PreparedStatement) connection.prepareStatement(sqlQueryTruncateData2.toString());
                            pstrunc2.execute();
                            ftpClient.connect(drlIpAddress2);//drl
//                            ftpClient.connect(ipAddress);//local
                            showServerReply(ftpClient);
                            replyCode = ftpClient.getReplyCode();
                            if (!FTPReply.isPositiveCompletion(replyCode)) {
                                System.out.println("Operation failed. Server reply code: " + replyCode);
                                //return "false";
                            }
                            success = ftpClient.login(user, pass);
                            showServerReply(ftpClient);
                            ftpClient.changeWorkingDirectory("path_ to_file_directory");
                            fstream = ftpClient.retrieveFileStream(files[ii].getName());
                            in = new DataInputStream(fstream);
                            br = new BufferedReader(new InputStreamReader(in));
                            String strLine = null;
                            int count = 1;
                            while ((strLine = br.readLine()) != null) {
                                String dataline = strLine;
                                if (count > 1) {
                                    exeQueryPoitemTest(dataline);
                                }
                                count++;
                                totalRowsPOHEADER++;
                                if (strLine.trim().length() == 0) {
                                    continue;  // Skip blank lines
                                }
                            }
                        }
                        if (files[ii].getName().equalsIgnoreCase("POHEADER.csv")) {
                            String sqlQueryTruncateData2 = "truncate table poitemdata";
                            PreparedStatement pstrunc2 = (PreparedStatement) connection.prepareStatement(sqlQueryTruncateData2.toString());
                            pstrunc2.execute();
                            ftpClient.connect(drlIpAddress2);//drl
//                            ftpClient.connect(ipAddress);//local
                            showServerReply(ftpClient);
                            replyCode = ftpClient.getReplyCode();
                            if (!FTPReply.isPositiveCompletion(replyCode)) {
                                System.out.println("Operation failed. Server reply code: " + replyCode);
                                //return "false";
                            }
                            success = ftpClient.login(user, pass);
                            showServerReply(ftpClient);
                            ftpClient.changeWorkingDirectory("path_ to_file_directory");
                            fstream = ftpClient.retrieveFileStream(files[ii].getName());
                            in = new DataInputStream(fstream);
                            br = new BufferedReader(new InputStreamReader(in));
                            String strLine = null;
                            int count = 1;
                            while ((strLine = br.readLine()) != null) {
                                String dataline = strLine;
                                if (count > 1) {
                                    exeQuery2(dataline);
                                }
                                count++;
                                totalRowsPOHEADER++;
                                if (strLine.trim().length() == 0) {
                                    continue;  // Skip blank lines
                                }
                            }
                        }

                        if (files[ii].getName().equalsIgnoreCase("FBL3NDATA.csv")) {
                            String sqlQueryTruncateData2 = "truncate table fbl3n_data";
                            PreparedStatement pstrunc2 = (PreparedStatement) connection.prepareStatement(sqlQueryTruncateData2.toString());
                            pstrunc2.execute();
                            ftpClient.connect(drlIpAddress2);//drl
//                            ftpClient.connect(ipAddress);//local
                            showServerReply(ftpClient);
                            replyCode = ftpClient.getReplyCode();
                            if (!FTPReply.isPositiveCompletion(replyCode)) {
                                System.out.println("Operation failed. Server reply code: " + replyCode);
                                //return "false";
                            }
                            success = ftpClient.login(user, pass);
                            showServerReply(ftpClient);
                            ftpClient.changeWorkingDirectory("path_ to_file_directory");
                            fstream = ftpClient.retrieveFileStream(files[ii].getName());
                            in = new DataInputStream(fstream);
                            br = new BufferedReader(new InputStreamReader(in));
                            String strLine = null;
                            int count = 1;
                            while ((strLine = br.readLine()) != null) {
                                String dataline = strLine;
                                if (count == 1) {
                                    System.out.println("coloumn :" + dataline);
                                    //System.out.println(dataline.split("\\|"));
                                }
                                if (count > 1) {
//                                    String[] seperateData11 = dataline.split("\\|");
//                                    for(int j=0;j<seperateData11.length;j++)
//                                    System.out.print(seperateData11[j]+" | ");
//                                    System.out.println(dataline.toString());

                                    exeQuery3(dataline);
                                }
                                count++;
                                totalRowsFBL3NDATA++;
                                if (strLine.trim().length() == 0) {
                                    continue;  // Skip blank lines
                                }
                            }
                        }

                        if (files[ii].getName().equalsIgnoreCase("GRIRFILEAPRL_CSVnnnnnnnnn.csv")) {
                            String sqlQueryTruncateData = "truncate table pogrdata";
                            PreparedStatement pstrunc = (PreparedStatement) connection.prepareStatement(sqlQueryTruncateData.toString());
                            pstrunc.execute();
                            ftpClient.connect(drlIpAddress2);//drl
//                            ftpClient.connect(ipAddress);//local
                            showServerReply(ftpClient);
                            replyCode = ftpClient.getReplyCode();
                            if (!FTPReply.isPositiveCompletion(replyCode)) {
                                System.out.println("Operation failed. Server reply code: " + replyCode);
                                //return "false";
                            }
                            success = ftpClient.login(user, pass);
                            showServerReply(ftpClient);
                            ftpClient.changeWorkingDirectory("path_ to_file_directory");
                            fstream = ftpClient.retrieveFileStream(files[ii].getName());
                            in = new DataInputStream(fstream);
                            br = new BufferedReader(new InputStreamReader(in));
                            String strLine = null;
                            int count = 1;
                            while ((strLine = br.readLine()) != null) {
                                String dataline = strLine;
                                if (count > 1) {
                                    exeQuery4(dataline);
                                }
                                count++;
                                if (strLine.trim().length() == 0) {
                                    continue;  // Skip blank lines
                                }
                            }
                        }//else ennded
                    }//else endded
                }
            proc = connection.prepareCall("{ call DRL_FTP_INSERTS() }");
            proc.execute();
            }
            //calling procedure for inserting data into two tables after two csv file has uploadded to database
            
            end_time = System.currentTimeMillis();
            date2 = new Date();
//            PbMailParams params = null;
//            PbMail mailer = null;
//            String dir = System.getProperty("java.io.tmpdir") + "\\";
//            ArrayList<String> attch = new ArrayList<String>();
//            Date date = new Date();
//            String fileName = date + "ExceptionsStatus1.text";
//
//            File file = new File(dir + fileName);
//
//            // if file doesnt exists, then create it
//            if (file.exists()) {
//                if (file.isFile()) {
//                    file.delete();
//                }
//            }
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//
//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write("*************GRIRDATA.CSV FILE EXCEPTION****************\n");
//            for (int i = 0; i < exceptionGRIRDATA.size(); i++) {
//                bw.write(exceptionGRIRDATA.get(i).toString());
//                bw.write("\n-------------------------------\n");
//            }
//            bw.write("*************POHEADER.CSV FILE EXCEPTION****************\n");
//            for (int i = 0; i < exceptionPOHEADER.size(); i++) {
//                bw.write(exceptionPOHEADER.get(i).toString());
//                bw.write("\n-------------------------------\n");
//            }
//            bw.write("*************FBL3NDATA.CSV FILE EXCEPTION****************\n");
//            for (int i = 0; i < exceptionFBL3NDATA.size(); i++) {
//                bw.write(exceptionFBL3NDATA.get(i).toString());
//                bw.write("\n-------------------------------\n");
//            }
//
//            bw.close();
//            String str = new String(dir.concat(fileName));
//            attch.add(str);
//
//            String bodyText = "<div style='background-color:lightgrey;'>"
//                    + "*********File Name: GRIRDATA.CSV**************<br>"
//                    + "TOTAL NO. OF ROWS : " + totalRowsGRIRDATA + "<br>"
//                    + "<span style='color:green'>TOTAL NO. OF ROWS SUCCESSS : " + successRowsGRIRDATA + "</span><br>"
//                    + "<span style='color:red'>TOTAL NO. OF ROWS FAILED : " + failedRowsGRIRDATA + "</span><br>"
//                    + "*********File Name: POHEADER.CSV**************<br>"
//                    + "TOTAL NO. OF ROWS : " + totalRowsPOHEADER + "<br>"
//                    + "<span style='color:green'>TOTAL NO. OF ROWS SUCCESSS : " + successRowsPOHEADER + "</span><br>"
//                    + "<span style='color:red'>TOTAL NO. OF ROWS FAILED : " + failedRowsPOHEADER + "</span><br>"
//                    + "*********File Name: FBL3NDATA.CSV**************<br>"
//                    + "TOTAL NO. OF ROWS : " + totalRowsFBL3NDATA + "<br>"
//                    + "<span style='color:green'>TOTAL NO. OF ROWS SUCCESSS : " + successRowsFBL3NDATA + "</span><br>"
//                    + "<span style='color:red'>TOTAL NO. OF ROWS FAILED : " + failedRowsFBL3NDATA + "</span><br>"
//                    + "</div>";

//            long s=System.currentTimeMillis();
//System.out.println(s);
//for(int i=0;i<10000000;i++){
//	
//}
//
//long e=System.currentTimeMillis();
//System.out.println(e);
            String bodyText = ""
                    + "*********File Name: GRIRDATA.CSV**************"
                    + "TOTAL NO. OF ROWS : " + totalRowsGRIRDATA
                    + "TOTAL NO. OF ROWS SUCCESSS : " + successRowsGRIRDATA
                    + "TOTAL NO. OF ROWS FAILED : " + failedRowsGRIRDATA
                    + "*********File Name: POHEADER.CSV**************"
                    + "TOTAL NO. OF ROWS : " + totalRowsPOHEADER
                    + "TOTAL NO. OF ROWS SUCCESSS : " + successRowsPOHEADER
                    + "TOTAL NO. OF ROWS FAILED : " + failedRowsPOHEADER
                    + "*********File Name: FBL3NDATA.CSV**************"
                    + "TOTAL NO. OF ROWS : " + totalRowsFBL3NDATA
                    + "TOTAL NO. OF ROWS SUCCESSS : " + successRowsFBL3NDATA
                    + "TOTAL NO. OF ROWS FAILED : " + failedRowsFBL3NDATA;
            long millis = end_time - start_time;
            System.out.println(millis);
            long second = (millis / 1000) % 60;
            long minute = (millis / (1000 * 60)) % 60;
            long hour = (millis / (1000 * 60 * 60)) % 24;
            String duration = hour + ":" + minute + ":" + second ;
//System.out.println(hour+":"+minute+":"+second+"."+millis);
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
            String formateddate1=null;
            String formateddate2=null;
            if(date1!=null)
            formateddate1 = formatter.format(date1);
            if(date2!=null)
            formateddate2 = formatter.format(date2);
            String prg_schedule_load_status_qry = "INSERT INTO prg_schedule_load_status (Start_Time,End_Time,Duration,Result,Error_Message)values(?,?,?,?,?)";
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(prg_schedule_load_status_qry.toString());
//           long duration=start_time-end_time;
            ps.setString(1, formateddate1);
            ps.setString(2, formateddate2);
            ps.setString(3, duration);
            if (!success) {
            ps.setString(4, "Failed !Could not login to the FTP server at ftp.mydrreddys.com");
            }else{
            ps.setString(4, "success");
            }
            ps.setString(5, bodyText);

            ps.addBatch();
            ps.execute();
//            String toAddress = "dinanath.parit@progenbusiness.com, sandesh.kumar@progenbusiness.com";
//            params = new PbMailParams();
//            params.setBodyText(bodyText);
//            params.setToAddr(toAddress);
//            params.setSubject("Uploading Status of CSV File Data from DRL FTP Server");
//            params.setHasAttach(true);
//            params.setAttachFile(attch);


//                mailer = new PbMail(params);
//                mailer.sendMail();
//            if (file.exists()) {
//                file.delete();
//            }



        } catch (Exception e) {
            end_time = System.currentTimeMillis();
            date2 = new Date();
            //OTHER OUT OF MAIN EXCEPTIONS
//            PbMailParams params = null;
//            PbMail mailer = null;
//            String dir = System.getProperty("java.io.tmpdir") + "\\";
//            ArrayList<String> attch = new ArrayList<String>();
//            String fileName = "ExceptionsStatus2.text";
//
//            File file = new File(dir + fileName);
            try {
//                // if file doesnt exists, then create it
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//
//                FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write("*************MAIN EXCEPTION :********************\n" + e + "");
//                bw.write("\n*************GRIRDATA.CSV FILE EXCEPTION****************\n");
//                for (int i = 0; i < exceptionGRIRDATA.size(); i++) {
//                    bw.write(exceptionGRIRDATA.get(i).toString());
//                    bw.write("\n-------------------------------\n");
//                }
//                bw.write("*************POHEADER.CSV FILE EXCEPTION****************\n");
//                for (int i = 0; i < exceptionPOHEADER.size(); i++) {
//                    bw.write(exceptionPOHEADER.get(i).toString());
//                    bw.write("\n-------------------------------\n");
//                }
//                bw.write("*************FBL3NDATA.CSV FILE EXCEPTION****************\n");
//                for (int i = 0; i < exceptionFBL3NDATA.size(); i++) {
//                    bw.write(exceptionFBL3NDATA.get(i).toString());
//                    bw.write("\n-------------------------------\n");
//                }
//                bw.close();
//                String str = new String(dir.concat(fileName));
//                attch.add(str);

                String bodyText = ""
                        + "*********Main Exception**************"
                        + "" + e.toString() + ""
                        + " *********File Name: GRIRDATA.CSV**************"
                        + " TOTAL NO. OF ROWS : " + totalRowsGRIRDATA
                        + " TOTAL NO. OF ROWS SUCCESSS : " + successRowsGRIRDATA
                        + " TOTAL NO. OF ROWS FAILED : " + failedRowsGRIRDATA
                        + " *********File Name: POHEADER.CSV**************"
                        + " TOTAL NO. OF ROWS : " + totalRowsPOHEADER
                        + " TOTAL NO. OF ROWS SUCCESSS : " + successRowsPOHEADER
                        + " TOTAL NO. OF ROWS FAILED : " + failedRowsPOHEADER
                        + " *********File Name: FBL3NDATA.CSV**************"
                        + " TOTAL NO. OF ROWS : " + totalRowsFBL3NDATA
                        + " TOTAL NO. OF ROWS SUCCESSS : " + successRowsFBL3NDATA
                        + " TOTAL NO. OF ROWS FAILED : " + failedRowsFBL3NDATA;
                if(bodyText.length()>3998){
                    bodyText=bodyText.substring(0, 3998);
                 }
                long millis = end_time - start_time;
                System.out.println(millis);
                long second = (millis / 1000) % 60;
                long minute = (millis / (1000 * 60)) % 60;
                long hour = (millis / (1000 * 60 * 60)) % 24;
                String duration = hour + ":" + minute + ":" + second ;
//System.out.println(hour+":"+minute+":"+second+"."+millis);
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
                String formateddate1=null;
                String formateddate2=null;
                if(date1!=null)
                formateddate1 = formatter.format(date1);
                if(date2!=null)
                formateddate2 = formatter.format(date2);
                String prg_schedule_load_status_qry = "INSERT INTO prg_schedule_load_status (Start_Time,End_Time,Duration,Result,Error_Message)values(?,?,?,?,?)";
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(prg_schedule_load_status_qry.toString());
//           long duration=start_time-end_time;
                ps.setString(1, formateddate1);
                ps.setString(2, formateddate2);
                ps.setString(3, duration);
                ps.setString(4, "failed");
                ps.setString(5, bodyText);

                ps.addBatch();
                ps.execute();
//                String toAddress = "dinanath.parit@progenbusiness.com, sandesh.kumar@progenbusiness.com";
//                params = new PbMailParams();
//                params.setBodyText(bodyText);
//                params.setToAddr(toAddress);
//                params.setSubject("Uploading Status of CSV File Data from DRL FTP Server");
//                params.setHasAttach(true);
//                params.setAttachFile(attch);


//                mailer = new PbMail(params);
//                mailer.sendMail();
//                if (file.exists()) {
//                    file.delete();
//                }

            } catch (Exception eX) {
                eX.printStackTrace();
            }
        } finally {
            if (proc != null) {
                proc.close();
                proc = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }

    }

    public static void exeQuery(String dataline) {
//        String[] seperateData1 = dataline.split(",");
//        int len = 24 - seperateData1.length;
//        for (int i = 0; i < len; i++) {
//            dataline = dataline + ",";
//        }
        String[] seperateData = dataline.split("\\|");
        try {
//String sqlQuery1 = "INSERT INTO pogrdata  (BASE_UOM,CALDAY, CALMONTH, CALYEAR,LOC_CURRCY,MATERIAL, MATL_GROUP, MATL_TYPE, PSTNG_DATE, BASE_UOM,TYPE, BICGMOVETYPE, BBP_ACC_NO,GRNNO,PONO,POITEM,GRNITEM,BICMAT_YEAR,BICVGABE,OI_MENGE,VALUE_LC,BICGRQTY_BUM)VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String sqlQuery1 = "INSERT INTO pogrdata  (VALUE_LC,BICGRQTY_BUM , OI_MENGE, PONO,POITEM,BBP_ACC_NO, GRNNO, GRNITEM, BICVGABE,  BICMAT_YEAR, BASE_UOM, BICGMOVETYPE, PSTNG_DATE, CALDAY, CALMONTH, MATL_GROUP, MATL_TYPE, TYPE, CALYEAR, MATERIAL,LOC_CURRCY,Load_time)VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,curdate())";
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sqlQuery1.toString());
            for (int i = 1; i <= 24; i++) {
                if (i == 1) {
                    try {
                        if (seperateData[0].contains("-")) {
                            String sdata = seperateData[0].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(1, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(1, Double.parseDouble(seperateData[0].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setDouble(1, Double.parseDouble("0"));
                    } catch (NumberFormatException nfe) {
                        ps.setDouble(1, Double.parseDouble("0"));
                    } catch (Exception nfe) {
                        ps.setDouble(1, Double.parseDouble("0"));
                    }

                }
                if (i == 3) {
                    try {
                        if (seperateData[2].contains("-")) {
                            String sdata = seperateData[2].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(2, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(2, Double.parseDouble(seperateData[2].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setDouble(2, Double.parseDouble("0"));
                    } catch (NumberFormatException nfe) {
                        ps.setDouble(2, Double.parseDouble("0"));
                    } catch (Exception nfe) {
                        ps.setDouble(1, Double.parseDouble("0"));
                    }
                }
                if (i == 5) {
                    try {
                        if (seperateData[4].contains("-")) {
                            String sdata = seperateData[4].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(3, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(3, Double.parseDouble(seperateData[4].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setDouble(3, Double.parseDouble("0"));
                    } catch (NumberFormatException nfe) {
                        ps.setDouble(3, Double.parseDouble("0"));
                    } catch (Exception nfe) {
                        ps.setDouble(1, Double.parseDouble("0"));
                    }
                }
                if (i == 7) {
                    try {
                        ps.setString(4, seperateData[6]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(4, "");
                    } catch (Exception nfe) {
                        ps.setString(4, "");
                    }
                }
                if (i == 8) {
                    try {
                        ps.setString(5, seperateData[7].replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(5, "");
                    } catch (Exception nfe) {
                        ps.setString(5, "");
                    }
                }
                if (i == 9) {
                    try {
                        ps.setString(6, seperateData[8].replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(6, "");
                    } catch (Exception nfe) {
                        ps.setString(6, "");
                    }
                }
                if (i == 10) {
                    try {
                        ps.setString(7, seperateData[9]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(7, "");
                    } catch (Exception nfe) {
                        ps.setString(7, "");
                    }
                }
                if (i == 11) {
                    try {
                        ps.setString(8, seperateData[10].replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(8, "");
                    } catch (Exception nfe) {
                        ps.setString(8, "");
                    }
                }
                if (i == 12) {
                    try {
                        ps.setString(9, seperateData[11]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(9, "");
                    } catch (Exception nfe) {
                        ps.setString(9, "");
                    }
                }
                if (i == 13) {
                    try {
                        ps.setString(10, seperateData[12]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(10, "");
                    } catch (Exception nfe) {
                        ps.setString(10, "");
                    }
                }
                if (i == 14) {
                    try {
                        ps.setString(11, seperateData[13]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(11, "");
                    } catch (Exception nfe) {
                        ps.setString(11, "");
                    }
                }
                if (i == 15) {
                    try {
                        ps.setString(12, seperateData[14]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(12, "");
                    } catch (Exception nfe) {
                        ps.setString(12, "");
                    }
                }
                if (i == 16) {
                    try {
                        String fdate = seperateData[15].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String day = fdate.substring(6);
                        String dateStr = dyear + "." + month + "." + day;
                        ps.setString(13, dateStr);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(13, "");
                    } catch (Exception nfe) {
                        ps.setString(13, "");
                    }
                }
                if (i == 17) {
                    try {
                        String fdate = seperateData[16].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String day = fdate.substring(6);
                        String dateStr = dyear + "." + month + "." + day;
                        ps.setString(14, dateStr);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(14, "");
                    } catch (Exception nfe) {
                        ps.setString(14, "");
                    }
                }
                if (i == 18) {
                    try {
                        String fdate = seperateData[17].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String dateStr = dyear + "." + month;
                        ps.setString(15, dateStr);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(15, "");
                    } catch (Exception nfe) {
                        ps.setString(15, "");
                    }
                }
                if (i == 19) {
                    try {
                        ps.setString(16, seperateData[18]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(16, "");
                    } catch (Exception nfe) {
                        ps.setString(16, "");
                    }
                }
                if (i == 20) {
                    try {
                        ps.setString(17, seperateData[19]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(17, "");
                    } catch (Exception nfe) {
                        ps.setString(17, "");
                    }
                }
                if (i == 21) {
                    try {
                        ps.setString(18, seperateData[20]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(18, "");
                    } catch (Exception nfe) {
                        ps.setString(18, "");
                    }
                }
                if (i == 22) {
                    try {
                        ps.setString(19, seperateData[21]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(19, "");
                    } catch (Exception nfe) {
                        ps.setString(19, "");
                    }
                }
                if (i == 23) {
                    try {
                        ps.setString(20, seperateData[22].toString().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(20, "");
                    } catch (Exception nfe) {
                        ps.setString(20, "");
                    }
                }
                if (i == 24) {
                    try {
                        ps.setString(21, seperateData[23]);
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(21, "");
                    } catch (Exception nfe) {
                        ps.setString(21, "");
                    }
                }
            }
            ps.addBatch();
            ps.execute();

            successRowsGRIRDATA++;
        } catch (Exception e) {
            failedRowsGRIRDATA++;
            exceptionGRIRDATA.add(e.toString());
            Logger.getLogger(FTPReadCSVFile.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void exeQuery2(String dataline) {

//        String[] seperateData1 = dataline.split("|");
//        int len = 30 - seperateData1.length;
//        for (int i = 0; i < len; i++) {
//            dataline = dataline + "|";
//        }
        String[] seperateData = dataline.split("\\|");
        try {
            String sqlQuery = "INSERT INTO POITEMDATA(EXCHANGE,PURNET_VALUE,NETPRICE ,ORDER_VAL, BICZPR_DATE, BICZPAYCRD,NET_PO_VAL,QUANTITY,PO_NO, PO_ITEM, Acct_Assignment_Cat, PR_NO,  PR_ITEM, PUR_GROUP, PODATE, PLANT, PURCH_ORG, VENDOR,  vendor_name, MATERIAL, material_text, MATL_GROUP, BICZPAYTRM,ORDER_CURR,BASE_UOM,Load_time)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,curdate())";
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sqlQuery.toString());
            for (int i = 1; i <= 30; i++) {

                if (i == 1) {
                    try {
                        if (seperateData[0].contains("-")) {
                            String sdata = seperateData[0].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(1, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(1, Double.parseDouble(seperateData[0].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setDouble(1, Double.parseDouble("0"));
                    } catch (Exception nfe) {
                        ps.setDouble(1, Double.parseDouble("0"));
                    }


                }
                if (i == 2) {
                    try {
                        if (seperateData[1].contains("-")) {
                            String sdata = seperateData[1].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(2, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(2, Double.parseDouble(seperateData[1].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setDouble(2, Double.parseDouble("0"));
                    } catch (Exception nfe) {
                        ps.setDouble(2, Double.parseDouble("0"));
                    }

                }
                if (i == 4) {
                    try {
                        if (seperateData[3].contains("-")) {
                            String sdata = seperateData[3].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(3, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(3, Double.parseDouble(seperateData[3].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setDouble(3, Double.parseDouble("0"));
                    } catch (Exception nfe) {
                        ps.setDouble(3, Double.parseDouble("0"));
                    }
                }
                if (i == 6) {
                    try {
                        if (seperateData[5].contains("-")) {
                            String sdata = seperateData[5].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(4, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(4, Double.parseDouble(seperateData[5].replace("-", "")));
                        }
                    } catch (Exception e) {
                        ps.setDouble(4, Double.parseDouble("0"));
                    }

                }
                if (i == 8) {
                    try {
                        String fdate = seperateData[7].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String day = fdate.substring(6);
                        String dateStr = dyear + "." + month + "." + day;
                        ps.setString(5, dateStr);
                    } catch (Exception e) {
                        ps.setString(5, "");
                    }
                }
                if (i == 9) {
                    try {
                        ps.setString(6, seperateData[8]);
                    } catch (Exception e) {
                        ps.setString(6, "");
                    }
                }
                if (i == 10) {
                    try {
                        if (seperateData[9].contains("-")) {
                            String sdata = seperateData[9].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(7, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(7, Double.parseDouble(seperateData[9].replace("-", "")));
                        }
                    } catch (Exception e) {
                        ps.setDouble(7, Double.parseDouble("0"));
                    }
                }
                if (i == 12) {
                    try {
                        if (seperateData[11].contains("-")) {
                            String sdata = seperateData[11].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(8, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(8, Double.parseDouble(seperateData[11].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setDouble(8, Double.parseDouble("0"));
                    } catch (Exception e) {
                        ps.setDouble(8, Double.parseDouble("0"));
                    }
                }
                if (i == 14) {
                    try {
                        ps.setString(9, seperateData[13]);
                    } catch (Exception e) {
                        ps.setString(9, "");
                    }
                }
                if (i == 15) {
                    try {
                        ps.setString(10, seperateData[14].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(10, "");
                    }
                }
                if (i == 16) {
                    try {
                        ps.setString(11, seperateData[15]);
                    } catch (Exception e) {
                        ps.setString(11, "");
                    }
                }
                if (i == 17) {
                    try {
                        ps.setString(12, seperateData[16]);
                    } catch (Exception e) {
                        ps.setString(12, "");
                    }
                }
                if (i == 18) {
                    try {
                        ps.setString(13, seperateData[17].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(13, "");
                    }
                }
                if (i == 19) {
                    try {
                        ps.setString(14, seperateData[18]);
                    } catch (Exception e) {
                        ps.setString(14, "");
                    }
                }
                if (i == 20) {
                    try {
                        String fdate = seperateData[19].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String day = fdate.substring(6);
                        String dateStr = dyear + "." + month + "." + day;
                        ps.setString(15, dateStr);
                    } catch (Exception e) {
                        ps.setString(15, "");
                    }
                }
                if (i == 21) {
                    try {
                        ps.setString(16, seperateData[20]);
                    } catch (Exception e) {
                        ps.setString(16, "");
                    }

                }
                if (i == 22) {
                    try {
                        ps.setString(17, seperateData[21]);
                    } catch (Exception e) {
                        ps.setString(17, "");
                    }

                }
                if (i == 23) {
                    try {
                        ps.setString(18, seperateData[22].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(18, "");
                    }

                }
                if (i == 24) {
                    try {
                        ps.setString(19, seperateData[23]);
                    } catch (Exception e) {
                        ps.setString(19, "");

                    }

                }
                if (i == 25) {
                    try {
                        ps.setString(20, seperateData[24].toString().replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(20, "");

                    }
                }
                if (i == 26) {
                    try {
                        ps.setString(21, seperateData[25]);
                    } catch (Exception e) {
                        ps.setString(21, "");

                    }
                }
                if (i == 27) {
                    try {
                        ps.setString(22, seperateData[26]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setString(22, "");
                    } catch (Exception e) {
                        ps.setString(22, "");
                    }
                }
                if (i == 28) {
                    try {
                        ps.setString(23, seperateData[27]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setString(23, "");
                    } catch (Exception e) {
                        ps.setString(23, "");
                    }
                }
                if (i == 29) {
                    try {
                        ps.setString(24, seperateData[28]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setString(24, "");
                    } catch (Exception e) {
                        ps.setString(24, "");
                    }
                }
                if (i == 30) {
                    try {
                        ps.setString(25, seperateData[29]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setString(25, "");

                    } catch (Exception e) {
                        ps.setString(25, "");

                    }

                }
                /*
                 else if(i==39||i==40){
                 String fdate=seperateData[i-1].toString();
                 String dyear=fdate.substring(0, 4);
                 String month=fdate.substring(4, 6);
                 String day=fdate.substring(6);
                 String dateStr = dyear+"-"+month+"-"+day;
                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                 java.sql.Date date = new java.sql.Date(sdf.parse(dateStr).getTime());
                 //              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                 //              System.out.println(sdf.format(new Date(dateStr)));
                 //               fulldate=sdf.format(new Date(dateStr));

                 ps.setDate(i, date);
                 }
                 else{
                 ps.setString(i,seperateData[i-1]);
                 }
                 *
                 */
            }

            ps.addBatch();
            ps.execute();
            successRowsPOHEADER++;
        } catch (Exception e) {
            failedRowsPOHEADER++;
            exceptionPOHEADER.add(e.toString());
            Logger.getLogger(FTPReadCSVFile.class.getName()).log(Level.SEVERE, null, e);

        }
    }
    public static void exeQueryPoitemTest(String dataline) {

//        String[] seperateData1 = dataline.split("|");
//        int len = 30 - seperateData1.length;
//        for (int i = 0; i < len; i++) {
//            dataline = dataline + "|";
//        }
        String[] seperateData = dataline.replaceAll("^\"", "").split("\"?(,|$)(?=(([^\"]*\"){2})*[^\"]*$) *\"?");
        try {
            String sqlQuery = "INSERT INTO poitemdata_sampl(\n" +
"PO_NO,\n" +
"PO_ITEM,\n" +
"Acct_Assignment_Cat,\n" +
"PR_NO,\n" +
"PR_ITEM,\n" +
"PUR_GROUP,\n" +
"PODATE,\n" +
"PLANT,\n" +
"PURCH_ORG,\n" +
"VENDOR,\n" +
"vendor_name,\n" +
"MATERIAL,\n" +
"material_text,\n" +
"MATL_GROUP,\n" +
"BICZPAYTRM,\n" +
"ORDER_CURR,\n" +
"BASE_UOM,\n" +
"BICZPR_DATE,\n" +
"NETPRICE,\n" +
"PURNET_VALUE,\n" +
"QUANTITY,\n" +
"NET_PO_VAL,\n" +
"ORDER_VAL,\n" +
"EXCHANGE,\n" +
"BICZPAYCRD,\n" +
"Load_time)\n" +
")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate())";
            
//            String str="INSERT INTO POITEMDATA(EXCHANGE1,PURNET_VALUE2,NETPRICE3 ,ORDER_VAL4, BICZPR_DATE5, BICZPAYCRD6,NET_PO_VAL7,\n" +
//"QUANTITY8,"
//+ "PO_NO9, PO_ITEM10, Acct_Assignment_Cat11, PR_NO12,  PR_ITEM13, PUR_GROUP14, PODATE15,\n" +
//" PLANT16, PURCH_ORG17, VENDOR18,  vendor_name19, MATERIAL20, material_text21, MATL_GROUP22, BICZPAYTRM23,ORDER_CURR24,BASE_UOM25,"
//                    + "Load_time26)"
//                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,curdate())";
//            String sqlQuery = "INSERT INTO POITEMDATA(EXCHANGE,PURNET_VALUE,NETPRICE ,ORDER_VAL, BICZPR_DATE, BICZPAYCRD,NET_PO_VAL,QUANTITY,PO_NO, PO_ITEM, Acct_Assignment_Cat, PR_NO,  PR_ITEM, PUR_GROUP, PODATE, PLANT, PURCH_ORG, VENDOR,  vendor_name, MATERIAL, material_text, MATL_GROUP, BICZPAYTRM,ORDER_CURR,BASE_UOM,Load_time)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,curdate())";
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sqlQuery.toString());
            for (int i = 0; i <= 30; i++) {
                if (i == 0) {
                    try {
                        ps.setString(1, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(1, "");
                    }
                }
                 if (i == 1) {
                    try {
                        ps.setString(2, seperateData[i].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(2, "");
                    }
                }
                if (i == 2) {
                    try {
                        ps.setString(3, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(3, "");
                    }
                }
               if (i == 3) {
                    try {
                        ps.setString(4, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(4, "");
                    }
                }
                if (i == 4) {
                    try {
                        ps.setString(5, seperateData[i].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(5, "");
                    }
                }
                if (i == 5) {
                    try {
                        ps.setString(6, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(6, "");
                    }
                }
                if (i == 6) {
                    try {
                        String fdate = seperateData[i].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String day = fdate.substring(6);
                        String dateStr = dyear + "." + month + "." + day;
                        ps.setString(7, dateStr);
                    } catch (Exception e) {
                        ps.setString(7, "");
                    }
                }
                if (i == 7) {
                    try {
                        ps.setString(8, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(8, "");
                    }

                }
                if (i == 8) {
                    try {
                        ps.setString(9, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(9, "");
                    }

                }
                if (i == 9) {
                    try {
                        ps.setString(10, seperateData[i].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(10, "");
                    }

                }
                if (i == 10) {
                    try {
                        ps.setString(11, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(11, "");

                    }

                }
                if (i == 11) {
                    try {
                        ps.setString(12, seperateData[i].toString().replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(12, "");

                    }
                }
                if (i == 12) {
                    try {
                        ps.setString(13, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(13, "");

                    }
                }
                if (i == 13) {
                    try {
                        ps.setString(14, seperateData[i]);                  
                    } catch (Exception e) {
                        ps.setString(14, "");
                    }
                }
                if (i == 14) {
                    try {
                        ps.setString(15, seperateData[i]);
                    }catch (Exception e) {
                        ps.setString(15, "");
                    }
                }
                if (i == 15) {
                    try {
                        ps.setString(16, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(16, "");
                    }
                }
                if (i == 16) {
                    try {
                        ps.setString(17, seperateData[i]);
                    }catch (Exception e) {
                        ps.setString(17, "");
                    }
                }
                if (i == 17) {
                    try {
                        String fdate = seperateData[i].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String day = fdate.substring(6);
                        String dateStr = dyear + "." + month + "." + day;
                        ps.setString(18, dateStr);
                    } catch (Exception e) {
                        ps.setString(18, "");
                    }
                }
                if (i == 18) {
                    try {
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(i+1, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(i+1, Double.parseDouble(seperateData[i].replace("-", "")));
                        }
                    } catch (Exception nfe) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    }
                }
                if (i == 19) {
                    try {
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(i+1, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(i+1, Double.parseDouble(seperateData[i].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    } catch (Exception nfe) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    }

                }
                                if (i == 20) {
                    try {
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(i+1, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(i+1, Double.parseDouble(seperateData[i].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    } catch (Exception e) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    }
                }
               if (i == 21) {
                    try {
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(i+1, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(i+1, Double.parseDouble(seperateData[i].replace("-", "")));
                        }
                    } catch (NumberFormatException | SQLException e) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    } catch (Exception e) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    }
                }   
                if (i == 22) {
                    try {
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(i+1, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(i+1, Double.parseDouble(seperateData[i].replace("-", "")));
                        }
                    } catch (Exception e) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    }

                }
               if (i == 23) {
                    try {
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(i+1, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(i+1, Double.parseDouble(seperateData[i].replace("-", "")));
                        }
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    } catch (Exception nfe) {
                        ps.setDouble(i+1, Double.parseDouble("0"));
                    }
                }
                if (i == 24) {
                    try {
                        ps.setString(i+1, seperateData[i]);
                    } catch (Exception e) {
                        ps.setString(i+1, "");
                    }
                }
                //end
               

               


               
               



                /*
                 else if(i==39||i==40){
                 String fdate=seperateData[i-1].toString();
                 String dyear=fdate.substring(0, 4);
                 String month=fdate.substring(4, 6);
                 String day=fdate.substring(6);
                 String dateStr = dyear+"-"+month+"-"+day;
                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                 java.sql.Date date = new java.sql.Date(sdf.parse(dateStr).getTime());
                 //              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                 //              System.out.println(sdf.format(new Date(dateStr)));
                 //               fulldate=sdf.format(new Date(dateStr));

                 ps.setDate(i, date);
                 }
                 else{
                 ps.setString(i,seperateData[i-1]);
                 }
                 *
                 */
            }

            ps.addBatch();
            ps.execute();
            successRowsPOHEADER++;
        } catch (Exception e) {
            failedRowsPOHEADER++;
            exceptionPOHEADER.add(e.toString());
            Logger.getLogger(FTPReadCSVFile.class.getName()).log(Level.SEVERE, null, e);

        }
    }
    //added by Dinanath

    public static void exeQuery3(String dataline) {
        String[] seperateData = dataline.split("\\|");
        try {
            String sqlQuery = "INSERT INTO FBL3N_DATA(AMOUNT_IN_DOCCURR,DOCUMENT_CURRENCY,GR_VALUE_LC ,OI_MENGE, CAL_MONTH, DOCUMENT_DATE,Posting_Date,PLANT,COST_CENTER,ACCOUNT , DOCUMENT_NUMBER , PO_NO, VENDOR , MATERIAL, BASE_UNIT_OF_MEASURE, PROFIT_CENTER,Load_time)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,curdate())";
//            String sqlQuery1 = "INSERT INTO FBL3N_DATA(AMOUNT_IN_DOCCURR(A),DOCUMENT_CURRENCY(B),GR_VALUE_LC(C),OI_MENGE(E),CAL_MONTH(G),DOCUMENT_DATE(H),PLANT(J),COST_CENTER(K),ACCOUNT (L),DOCUMENT_NUMBER(M) ,PO_NO(O),VENDOR(R) ,MATERIAL(S),BASE_UNIT_OF_MEASURE(T),PROFIT_CENTER(U))values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sqlQuery.toString());
            for (int i = 1; i <= 21; i++) {
//"""Amount in DC"""00O2TLYAQC0FW99Y23I7XM0FV:0DEB_CRE_DC|"""(Currency) Amount in DC"""00O2TLYAQC0FW99Y23I7XM0FV_CUKY:0CURRENCY|"""Amount in LC"""00O2TLYAQC0FW99Y23I7XM6RF:0DEB_CRE_LC|"""(Currency) Amount in LC"""00O2TLYAQC0FW99Y23I7XM6RF_CUKY:0CURRENCY|"""Quantity"""00O2TLYAQC0FW99Y23I7XLU4B:0OI_MENGE|"""(Unit) Quantity"""00O2TLYAQC0FW99Y23I7XLU4B_UNIT:0UNIT|"""Fiscal year/period"""ZFIPUR2___F3:0FISCPER|"""Document Date"""ZFIPUR2___F42:0DOC_DATE|"""Company code"""ZFIPUR2___F1:0COMP_CODE|"""Plant"""ZFIPUR2___F31:0PLANT|"""Cost Center"""ZFIPUR2___F24:0COSTCENTER|"""G/L Account"""ZFIPUR2___F15:0GL_ACCOUNT|"""Doc. No. (GL View)"""ZFIPUR2___F6:0AC_DOC_NR|"""Line Item (GL View)"""ZFIPUR2___F7:0AC_DOC_LN|"""Purchasing document"""ZFIPUR2___F70:0OI_EBELN|"""Item"""ZFIPUR2___F71:0OI_EBELP|"""Customer"""ZFIPUR2___F26:0CUSTOMER|"""Vendor"""ZFIPUR2___F76:0VENDOR|"""Material"""ZFIPUR2___F286:0MATERIAL|"""Base Unit"""ZFIPUR2___F270:0BASE_UOM|"""Profit Center"""ZFIPUR2___F27:0PROFIT_CTR
// 8740.00-0   						 |  blank-1   								|  8740.00-2   						| blank-3   								|  0.000-4   					| blank-5   						 |  2015012-6  				       |  20110412-7  				  |  1000-8  				    |  2005-9  			    |  0000100272-10  			       |  0051010009-11  			  |  4800186599-12  				  |  000005-13  				   |  7100017022-14  				    |  00010-15  		      | blank-16   			    |  0000512652-17  		      |  blank -18 			     | blank-19  			     |  0020050201-20
                if (i == 1) {//Amount_in_doccurr
                    try {
                        if (seperateData[0].contains("-")) {
                            String sdata = seperateData[11].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setString(1, minusAddData);
                        } else {
                            ps.setString(1, seperateData[0]);
                        }
                    } catch (Exception e) {
                        ps.setString(1, "");
                        // e.printStackTrace();
                    }
                }
                if (i == 2) {//,Document_currency
                    try {
                        if (seperateData[1].contains("-")) {
                            String sdata = seperateData[11].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setString(2, minusAddData);
                        } else {
                            ps.setString(2, seperateData[1]);
                        }

                    } catch (Exception e) {
                        ps.setString(2, "");
                        //  e.printStackTrace();
                    }
                }
                if (i == 3) {//,GR_Value_lc
                    try {
                        if (seperateData[2].contains("-")) {
                            String sdata = seperateData[11].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(3, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(3, Double.parseDouble(seperateData[2]));
                        }

                    } catch (Exception e) {
                        ps.setDouble(3, Double.parseDouble("0"));
                        // e.printStackTrace();
                    }
                }
                if (i == 5) {//OI_menge
                    try {
                        if (seperateData[4].contains("-")) {
                            String sdata = seperateData[11].replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(4, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(4, Double.parseDouble(seperateData[4]));
                        }
                    } catch (Exception e) {
                        ps.setDouble(4, Double.parseDouble("0"));
                        // e.printStackTrace();
                    }
                }
                if (i == 7) {//cal_month
                    try {
                        String fdate = seperateData[6].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(5, 7);
                        //String day = fdate.substring(6);
                        String dateStr = dyear + "/" + month;//+ "." + day;
                        ps.setString(5, dateStr);
                    } catch (Exception e) {
                        ps.setString(5, "");
                        // e.printStackTrace();
                    }
                }
                if (i == 8) {//Document_Date
                    try {
                        String fdate = seperateData[7].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String day = fdate.substring(6);
                        String dateStr = dyear + "." + month + "." + day;
                        ps.setString(6, dateStr);
                    } catch (Exception e) {
                        ps.setString(6, "");
                        //  e.printStackTrace();
                    }
                }
                if (i == 9) {//Posting_Date
                    try {
                        String fdate = seperateData[8].toString();
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.substring(4, 6);
                        String day = fdate.substring(6);
                        String dateStr = dyear + "." + month + "." + day;
                        ps.setString(7, dateStr);
                    } catch (Exception e) {
                        ps.setString(7, "");
                        //  e.printStackTrace();
                    }
                }
                if (i == 10) {//Plant
                    try {
                        ps.setString(8, seperateData[10].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(8, "");
                        // e.printStackTrace();
                    }
                }
                if (i == 11) {//,Cost_Center,
                    try {
                        ps.setString(9, seperateData[11].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(9, "");
                        //  e.printStackTrace();
                    }
                }
                if (i == 12) {//Account ,
                    try {
                        ps.setString(10, seperateData[12].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(10, "");
                        //   e.printStackTrace();
                    }
                }
                if (i == 13) {// Document_Number ,
                    try {
                        ps.setString(11, seperateData[13].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(11, "");
                        //   e.printStackTrace();
                    }
                }
                if (i == 14) {//PO_NO,
                    try {
                        ps.setString(12, seperateData[15].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(12, "");
                        //    e.printStackTrace();
                    }
                }
                if (i == 15) {//Vendor ,
                    try {
                        ps.setString(13, seperateData[18].replaceFirst("^0+(?!$)", ""));
                    } catch (Exception e) {
                        ps.setString(13, "");
                        //    e.printStackTrace();
                    }
                }
                if (i == 16) {//Material,
                    try {
                        ps.setString(14, seperateData[19].replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setString(14, "");
                        //  e.printStackTrace();
                    } catch (Exception e) {
                        ps.setString(14, "");
                        //  e.printStackTrace();
                    }
                }
                if (i == 18) {//Base_Unit_of_Measure
                    try {
                        ps.setString(15, seperateData[20].replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setString(15, "");
                        //  e.printStackTrace();
                    } catch (Exception e) {
                        ps.setString(15, "");
                        //  e.printStackTrace();
                    }
                }
                if (i == 21) {//, Profit_Center
                    try {
                        ps.setString(16, seperateData[21].replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ps.setString(16, "");
                    } catch (Exception e) {
                        ps.setString(15, "");
                    }
                }


            }//endded for loop
            ps.addBatch();
            ps.execute();
            successRowsFBL3NDATA++;
        } catch (Exception e) {
            failedRowsFBL3NDATA++;
            exceptionFBL3NDATA.add(e.toString());
//            System.out.print("execquery3 exception");
//           Logger.getLogger(FTPReadCSVFile.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void exeQuery4(String dataline) {
        String[] seperateData = dataline.split(",");
        try {
            /*
             GRIRFILEAPRL_CSV
             GRQTY_BUM,
             VALUE_LC
             OI_MENGE
             having nagetive values(PLZ handle these -'Ves)
             String sqlQuery1 = "INSERT INTO pogrdata  (BASE_UOM(A),CALDAY(B), CALMONTH(C), CALYEAR(D),LOC_CURRCY(E),MATERIAL(F), MATL_GROUP(G), MATL_TYPE(H), PSTNG_DATE(I),
             TYPE(K), BICGMOVETYPE(N), BBP_ACC_NO(O),GRNNO(P),PONO(Q),POITEM(R),GRNITEM(S),BICMAT_YEAR(T),BICVGABE(U),OI_MENGE(V),VALUE_LC(W),BICGRQTY_BUM(AB))

             */
            String sqlQuery1 = "INSERT INTO pogrdata(BASE_UOM,CALDAY, CALMONTH, CALYEAR,LOC_CURRCY,MATERIAL, MATL_GROUP, MATL_TYPE, PSTNG_DATE,TYPE, BICGMOVETYPE, BBP_ACC_NO,GRNNO,PONO,POITEM,GRNITEM,BICMAT_YEAR,BICVGABE,OI_MENGE,VALUE_LC,BICGRQTY_BUM)VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//            String sqlQuery1 = "INSERT INTO pogrdata  (VALUE_LC,BICGRQTY_BUM , OI_MENGE, PONO,POITEM,BBP_ACC_NO, GRNNO, GRNITEM, BICVGABE,  BICMAT_YEAR, BASE_UOM, BICGMOVETYPE, PSTNG_DATE, CALDAY, CALMONTH, MATL_GROUP, MATL_TYPE, TYPE, CALYEAR, MATERIAL,LOC_CURRCY)VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sqlQuery1.toString());
            for (int i = 0; i < 28; i++) {
                if (i == 0) {//BASE_UOM
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(1, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(1, "");
                    } catch (Exception nfe) {
                        ps.setString(1, "");
                    }
                }
                if (i == 1) {//CALDAY
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(2, seperateData[i].trim());
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(2, "");
                    } catch (Exception nfe) {
                        ps.setString(2, "");
                    }
                }
                if (i == 2) {//CALMONTH
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        String fdate = seperateData[i];
                        String dyear = fdate.substring(0, 4);
                        String month = fdate.trim().substring(4, 6);
                        //String day = fdate.substring(6);
                        String dateStr = dyear + "/" + month;//+ "." + day;
                        ps.setString(3, dateStr);
                    } catch (Exception e) {
                        ps.setString(3, "");
                        // e.printStackTrace();
                    }
                }
                if (i == 3) {//CALYEAR,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(4, seperateData[i].trim());
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(4, "");
                    } catch (Exception nfe) {
                        ps.setString(4, "");
                    }
                }
                if (i == 4) {//LOC_CURRCY,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(5, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(5, "");
                    } catch (Exception nfe) {
                        ps.setString(5, "");
                    }
                }
                if (i == 5) {//MATERIAL,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(6, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(6, "");
                    } catch (Exception nfe) {
                        ps.setString(6, "");
                    }
                }
                if (i == 6) {// MATL_GROUP,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(7, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(7, "");
                    } catch (Exception nfe) {
                        ps.setString(7, "");
                    }
                }
                if (i == 7) {//MATL_TYPE,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(8, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(8, "");
                    } catch (Exception nfe) {
                        ps.setString(8, "");
                    }
                }
                if (i == 8) {//PSTNG_DATE,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(9, seperateData[i].trim());
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(9, "");
                    } catch (Exception nfe) {
                        ps.setString(9, "");
                    }
                }

                if (i == 10) {//TYPE,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(10, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(10, "");
                    } catch (Exception nfe) {
                        ps.setString(10, "");
                    }
                }
                if (i == 13) {//BICGMOVETYPE,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(11, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(11, "");
                    } catch (Exception nfe) {
                        ps.setString(11, "");
                    }
                }
                if (i == 14) {//BBP_ACC_NO,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(12, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(12, "");
                    } catch (Exception nfe) {
                        ps.setString(12, "");
                    }
                }
                if (i == 15) {//GRNNO,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(13, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(13, "");
                    } catch (Exception nfe) {
                        ps.setString(13, "");
                    }
                }
                if (i == 16) {//PONO,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(14, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(14, "");
                    } catch (Exception nfe) {
                        ps.setString(14, "");
                    }
                }
                if (i == 17) {//POITEM,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(15, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(15, "");
                    } catch (Exception nfe) {
                        ps.setString(15, "");
                    }
                }
                if (i == 18) {//GRNITEM,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(16, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(16, "");
                    } catch (Exception nfe) {
                        ps.setString(16, "");
                    }
                }
                if (i == 19) {//BICMAT_YEAR,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(17, seperateData[i].trim());
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(17, "");
                    } catch (Exception nfe) {
                        ps.setString(17, "");
                    }
                }
                if (i == 20) {////BICVGABE,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        ps.setString(18, seperateData[i].trim().replaceFirst("^0+(?!$)", ""));
                    } catch (ArrayIndexOutOfBoundsException nfe) {
                        ps.setString(18, "");
                    } catch (Exception nfe) {
                        ps.setString(18, "");
                    }
                }
                if (i == 21) {//OI_MENGE,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].trim().replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(19, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(19, Double.parseDouble(seperateData[i].trim()));
                        }
                    } catch (Exception e) {
                        ps.setDouble(19, Double.parseDouble("0"));
                        e.printStackTrace();
                    }
                }
                if (i == 22) {//VALUE_LC,
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].trim().replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(20, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(20, Double.parseDouble(seperateData[i]));
                        }
                    } catch (Exception e) {

//                        seperateData[i].trim().replace("-", "");
                        ps.setDouble(20, Double.parseDouble("0"));
                        e.printStackTrace();
                    }
                }
                if (i == 27) {//BICGRQTY_BUM
                    try {
                        seperateData[i] = seperateData[i].replaceAll("\u00A0", "").trim();
                        if (seperateData[i].contains("-")) {
                            String sdata = seperateData[i].trim().replace("-", "");
                            String minusAddData = "-" + sdata;
                            ps.setDouble(21, Double.parseDouble(minusAddData));
                        } else {
                            ps.setDouble(21, Double.parseDouble(seperateData[i]));
                        }
                    } catch (Exception e) {
                        ps.setDouble(21, Double.parseDouble("0"));
                        // e.printStackTrace();
                    }
                }

            }
            ps.addBatch();
            ps.execute();
        } catch (Exception e) {
            Logger.getLogger(FTPReadCSVFile.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static String trim1(final String s) {
        final StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0); // delete from the beginning
        }
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1); // delete from the end
        }
        return sb.toString();
    }
}
