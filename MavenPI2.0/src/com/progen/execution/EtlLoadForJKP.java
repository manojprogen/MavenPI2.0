package com.progen.execution;

import com.progen.scheduler.ReportSchedule;
import com.progen.studio.StudioAction;
import com.progen.userlayer.db.LogReadWriter;
import com.progen.xml.UploadingXmlIntoDatabase;
import java.io.IOException;
import java.sql.*;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.SourceConn;
import prg.reportscheduler.LoadSchedulerJob;
import utils.db.ProgenConnection;

public class EtlLoadForJKP {

    public static Logger logger = Logger.getLogger(EtlLoadForJKP.class);
    public SourceConn sc = new SourceConn();
    public String load = "success";
    static int Totalloc = 0;
    LogReadWriter logrw = new LogReadWriter();
    //added by Dinanath for Dataflow main auto etl
    int curr_step = 0;
    int next_step = 1;
    boolean fromCatch = false;
    String proclastid = null;
    public static int lastCopyTablePId = 0;
    //added by Dinanath for Dataflow Philippines auto etl
    LogReadWriter logrw2 = new LogReadWriter();
    int curr_step2 = 0;
    int next_step2 = 1;
    boolean fromCatch2 = false;
    String proclastid2 = null;
    public static int lastCopyTablePId2 = 0;

    public void runLoadForJKP(String tableNames, String startDate, String endDate, String todaysDate) {
        logger.info("Table Name:::::::::::::" + tableNames);
        logger.info("start Date:::::::::::::" + startDate);
        logger.info("End Date:::::::::::::" + endDate);
        Connection ScConn = null;
        Connection TargetConn = null;
        String targetCon = "";
        String conname = "jkdwh";
        ScConn = sc.getConnection("PRODDTA", "", "", "", "", "", "", "", "");
        TargetConn = sc.getConnection(conname, "", "", "", "", "", "", "", "");

        try {
            TargetConn.setAutoCommit(false);
            if (ScConn != null) {
                String SelectQry = "";
                if (tableNames.equals("f0005")) {
                    tableNames = "f0005";
                    ScConn = null;
                    ScConn = sc.getConnection("PRODCTL", "", "", "", "", "", "", "", "");
                    TargetConn = sc.getConnection("jkdwh", "", "", "", "", "", "", "", "");
                    SelectQry = "SELECT * from " + tableNames + "  ";
                } else if (tableNames.equals("f03b11")) {
                    SelectQry = "SELECT * from " + tableNames + " where   rpAAP <>0   ";
                } else if (tableNames.equals("f41021")) {
                    SelectQry = "SELECT * from " + tableNames + " where lipqoh <>0 or LIQTTR<>0";
                } else if (tableNames.equals("f4211") || tableNames.equals("f42119") || tableNames.equals("f4201") || tableNames.equals("f42019") || tableNames.equals("f4074")
                        || tableNames.equals("f550001t") || tableNames.equals("f0116") || tableNames.equals("f0101") || tableNames.equals("f0006") || tableNames.equals("f4101")) {
                    if (tableNames.equals("f4211")) {
                        targetCon = "SDUPMj";
                    } else if (tableNames.equals("f42119")) {
                        targetCon = "SDUPMj";
                    } else if (tableNames.equals("f4201")) {
                        targetCon = "SHUPMJ";
                    } else if (tableNames.equals("f42019")) {
                        targetCon = "SHUPMJ";
                    } else if (tableNames.equals("f4074")) {
                        targetCon = "AlUPMJ";
                    } else if (tableNames.equals("f550001t")) {
                        targetCon = "Q1UPMJ";
                    } else if (tableNames.equals("f0116")) {
                        targetCon = "ALUPMJ";
                    } else if (tableNames.equals("f0101")) {
                        targetCon = "ABUPMJ";
                    } else if (tableNames.equals("f0006")) {
                        targetCon = "MCUPMJ";
                    } else if (tableNames.equals("f4101")) {
                        targetCon = "IMUPMJ";
                    }

                    SelectQry = "SELECT * from " + tableNames + " where " + targetCon + " between " + startDate + " and " + endDate;

//                if(targetCon!=null && !targetCon.equalsIgnoreCase("") && !targetCon.equalsIgnoreCase("null")){
//                    String qry = "select max("+targetCon+") FROM "+tableNames;
//                    ScConn = sc.getConnection("PRODDTA", "", "", "", "", "", "", "", "");
//                    PbDb pbdb = new PbDb();
//                    PbReturnObject retObj = null;
//                    retObj = pbdb.execSelectSQL(qry,ScConn);
//                    endDate = retObj.getFieldValueString(0, 0);
//                    ScConn = sc.getConnection("PRODDTA", "", "", "", "", "", "", "", "");
//                    SelectQry = "SELECT * from "+tableNames+" where "+targetCon+" between "+startDate+" and "+endDate;
//                } else {
//                    SelectQry = "SELECT * from "+tableNames+" ";
//                }
                } else if (tableNames.equals("f03012")) {
//                    SelectQry = "SELECT * from " + tableNames + "  ";
                    SelectQry = "SELECT * from  ?";
                }
                logger.info("Select Query:::::::::::::" + SelectQry);
                PreparedStatement stmt = ScConn.prepareStatement(SelectQry);
                stmt.setString(1, tableNames);
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                String[] cols = new String[colCount];
                String[] columnTypes = new String[colCount];
                String names = "insert into mysql_incr_" + tableNames + "(";
                String values11 = " values (";
                for (int i = 0; i < colCount - 1; i++) {
                    cols[i] = meta.getColumnName(i + 1);
                    columnTypes[i] = meta.getColumnTypeName(i + 1);
                    names += meta.getColumnName(i + 1).toString().replace("#", "");
                    names += ",";
                    values11 += "?";
                    values11 += ",";
                }
                String finalQuery = "";
                finalQuery += names.substring(0, names.length() - 1);
                finalQuery += ")";
                finalQuery += values11.substring(0, values11.length() - 1);
                finalQuery += ")";
                logger.info("Insert Query:::::::::::::" + finalQuery);
                PreparedStatement stmt1 = TargetConn.prepareStatement(finalQuery);
                while (rs.next()) {
                    for (int i = 0; i < colCount - 1; i++) {
                        if (meta.getColumnType(i + 1) == Types.BIGINT || meta.getColumnType(i + 1) == Types.DECIMAL || meta.getColumnType(i + 1) == Types.DOUBLE || meta.getColumnType(i + 1) == Types.FLOAT || meta.getColumnType(i + 1) == Types.INTEGER || meta.getColumnType(i + 1) == Types.NUMERIC || meta.getColumnType(i + 1) == Types.REAL || meta.getColumnType(i + 1) == Types.SMALLINT || meta.getColumnType(i + 1) == Types.TINYINT) {
                            if (meta.getColumnType(i + 1) == Types.FLOAT) {
                                stmt1.setFloat(i + 1, rs.getFloat(i + 1));
                            } else if (meta.getColumnType(i + 1) == Types.DECIMAL) {
                                stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                            } else if (meta.getColumnType(i + 1) == Types.DOUBLE) {
                                stmt1.setDouble(i + 1, rs.getDouble(i + 1));
                            } else {
                                stmt1.setInt(i + 1, rs.getInt(i + 1));
                            }
                        } else if (meta.getColumnType(i + 1) == Types.DATE) {
                            stmt1.setDate(i + 1, rs.getDate(i + 1));
                        } else if (rs.getString(i + 1) != null && !rs.getString(i + 1).equalsIgnoreCase("")) {
                            stmt1.setString(i + 1, (String) rs.getString(i + 1).replace("'", "").replace("&", " and "));
                        } else {
                            stmt1.setString(i + 1, rs.getString(i + 1));
                        }
                    }
                    stmt1.addBatch();
                }
                stmt1.executeBatch();
                if (tableNames.equals("f0005")) {
                } else {
                    TargetConn.commit();
                }
                stmt1.close();
                load = "success";
                if (load.equalsIgnoreCase("success")) {
                    updateTrackerMaster(tableNames, todaysDate, conname);
                }
            }
        } catch (Exception e) {
            load = "failure";
            logger.error(":::::ERROOOOOORRRRRRRRRRRRR::::", e);
        } finally {
            if (ScConn != null) {
                try {
                    ScConn.close();
                } catch (SQLException ex) {
                    load = "failure";
                    logger.error(":::::ERROOOOOORRRRRRRRRRRRR:::::", ex);
                }

            }
            if (TargetConn != null) {
                try {
                    TargetConn.close();
                } catch (SQLException ex) {
                    load = "failure";
                    logger.error(":::::ERROOOOOORRRRRRRRRRRRR:::::", ex);
                }

            }
        }

    }

    public int runLoadForDataflow(String tableNames, String startDate, String todaysDate, Boolean PHILIPPINES, String Filename) throws Exception {
        if (PHILIPPINES == false) {
            Totalloc++;
        }
        int totalrows = 0;
        LogReadWriter logrw1 = new LogReadWriter();
        logger.info("Table Name:::::::::::::" + tableNames);
        logger.info("start Date:::::::::::::" + startDate);
        logger.info("todaysDate:::::::::::::" + todaysDate);
//logger.info("End Date:::::::::::::"+endDate);
        Connection con = null;
        Connection con1 = null;
        String conname = "Df_mySql";
        con = sc.getConnection("Df_oracle", "", "", "", "", "", "", "", "");
        con1 = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");

        try {
            int value = 0;
            int initalVal = 0;
            int lastVla = 0;
            int finalValue = 0;
            int range = 0;
            int totalloop = 0;
            con1.setAutoCommit(false);
            if (con != null) {
                String table = "";
                if (tableNames.equals("dvs") || tableNames.equals("dvs_personal") || tableNames.equals("dvs_modeofverification")) {
                    int[] noofre = {(450000000)};
                    value = noofre[0];
                    initalVal = 1;
                    range = 50000;
                    lastVla = range;
                    finalValue = (value / lastVla) + 1;
                } else if (tableNames.equals("documentverficationsystem") || tableNames.equals("TICKET_CRMUSER_LOCATION") || tableNames.equals("Ticket_Data")) {
                    int[] noofre = {(850000)};
                    value = noofre[0];
                    initalVal = 1;
                    range = 50000;
                    lastVla = range;
                    finalValue = (value / lastVla) + 1;
                } else if (tableNames.equals("DOCUMENTVERFICATIONSYSTEM_1") ) {
                    int[] noofre = {(850000)};
                    value = noofre[0];
                    initalVal = 1;
                    range = 50000;
                    lastVla = range;
                    finalValue = (value / lastVla) + 1;
                } else if (tableNames.equals("dvs_case") || tableNames.equals("dvs_user")) {
                    int[] noofre = {(100000)};
                    value = noofre[0];
                    initalVal = 1;
                    range = 3000;
                    lastVla = range;
                    finalValue = (value / lastVla) + 1;
                } else if (tableNames.equals("dvs_education") || tableNames.equals("dvs_employment") || tableNames.equals("dvs_health_license")) {
                    int[] noofre = {(45000)};
                    value = noofre[0];
                    initalVal = 1;
                    range = 250;
                    lastVla = range;
                    finalValue = (value / lastVla) + 1;
                } else if (tableNames.equals("dvs_issuing_authority") || tableNames.equals("workitem")) {
                    int[] noofre = {(450000)};
                    value = noofre[0];
                    initalVal = 1;
                    range = 50000;
                    lastVla = range;
                    finalValue = (value / lastVla) + 1;
                } else if (tableNames.equals("processinstance") || tableNames.equals("dvs_deleted_barcodes") || tableNames.equals("end_date_missing_log")) {
                    int[] noofre = {(4500000)};
                    value = noofre[0];
                    initalVal = 1;
                    range = 25000;
                    lastVla = range;
                    finalValue = (value / lastVla) + 1;
                }
                for (int n = 0; n < finalValue; n++) {
                    if (tableNames.equals("dvs")) {
//                        table = "SELECT PROCESS_TEMPLATE_ID,PROCESS_INSTANCE_ID,SUPERVISORESCALATION,FOLLOWUPDATETIME,BARCODE,DOC1,LASTUPDATEDDATE,DESIGNATION,INSUFFICIENCYSTATUS,COMPONENTBIZSOLO,INSUFFICIENCYCOMMENT,QC2STATUS,ISINSUFFICIENCYAPPROVED,ISIAKNOWN,APPLICANTNAME,DOC3,PID,DOC6_LABEL,FIRSTNAME,CREATEDDATE,PROCESSDUEDURATION,DEPARTMENT,PISTARTTIME,QC3PHRASEOLOGY,ISACCEPTPOSITIVECASE,LASTTIMEUPDATED,DATEOFBIRTH,STAGE,GENDER,STATE,QC3COMMENT,POCVERIFIER,DOC5,DOC5_LABEL,VERIFICATIONPROOFDOC,DOC2_LABEL,PHRASEOLOGY,CRMREMARKS,CASERECEIVEDDATE,ISBYPASSINSUFFICIENCY,ESCALATIONFROM,ISQC3REJECT,COUNTRY,CHECKID,DOC2,MIDDLENAME,STATUS,QC2COMMENT,DOC4_LABEL,ACTION,QC2PHRASEOLOGY,ESCALATETO,ESCALATIONDATE,ISPOCBASEDVERIFICATION,INTERNALCOMMENTS,COMPONENT,ISSUINGAUTHORITY,NATIONALITY,\"MODE\",CLIENTNAME,PASSPORTNO,SUBBARCODE,FINALSTATUS,DOC4,CLIENTDISCREPANCY,SPOKETO,DOC6,VERIFICATIONCOMMENT,PERFORMER,RESULT,QC3STATUS,EXPECTEDDOC,ISQC2REJECT,DOC3_LABEL,DOC1_LABEL,AKANAME,ISERROR,QCSTATUS,ALLDOCS,ESCALATIONTYPE,HISTORYREMARKS,MODEOFINITIATION,LASTNAME,VERIFICATIONLANGUAGE,VTSTATUS,QCEXTERNALCOMMENT,CLIENTREFNO,ECTREQUESTUSER,ISBYPASSINSUFFICIENCYNOTIFY,COMMENTSFROMECT,INSUFFICIENCYNOTIFYSTATUS,CRMACTION,ECTREQUESTSRC,COMMENTSFORECT,ISECTFLAG,INSUFFICIENCYQCSTATUS,NOTIFICATION,PERSONALDOCS,ISSUINGAUTHORITYID,INSUFFICIENCYREJECTREMARKS,MODEOFVERIFICATION,CURRENCY,VERIFICATIONFEE,USDAMOUNT,AMOUNT,VISATYPE,PAYMENTAPPROVALDATE,VERAMOUNT,CURRENCYEXCHANGERATEINR,PAYMENTDATE,TRANSACTIONID,INSUFFPHRASEOLOGY,TOTALAMOUNTINR,PAYMENTINFAVOUROF,ISDUPLICATES from (SELECT p.*,row_number() over(ORDER by p.process_instance_id ) as rn from ebms."+tableNames+"  p inner join (select distinct  process_instance_id from ebms.WORKITEM where START_TIME >= '"+startDate+"' or END_TIME >= '"+startDate+"' or END_TIME is null) B on (p.process_instance_id = b.process_instance_id) )  col where rn between "+initalVal+" and "+lastVla+"";
                        table = "SELECT 0 AS PROCESS_TEMPLATE_ID,\n"
                                + "  PROCESS_INSTANCE_ID,\n"
                                + "  NULL AS SUPERVISORESCALATION,\n"
                                + "  FOLLOWUPDATETIME,\n"
                                + "  BARCODE,\n"
                                + "  NULL AS DOC1,\n"
                                + "  LASTUPDATEDDATE,\n"
                                + "  DESIGNATION,\n"
                                + "  INSUFFICIENCYSTATUS,\n"
                                + "  COMPONENTBIZSOLO,\n"
                                + "  NULL AS INSUFFICIENCYCOMMENT,\n"
                                + "  QC2STATUS,\n"
                                + "  0 AS ISINSUFFICIENCYAPPROVED,\n"
                                + "  0 AS ISIAKNOWN,\n"
                                + "  APPLICANTNAME,\n"
                                + "  NULL AS DOC3,\n"
                                + "  PID,\n"
                                + "  NULL AS DOC6_LABEL,\n"
                                + "  FIRSTNAME,\n"
                                + "  CREATEDDATE,\n"
                                + "  0 AS PROCESSDUEDURATION,\n"
                                + "  DEPARTMENT,\n"
                                + "  PISTARTTIME,\n"
                                + "  QC3PHRASEOLOGY,\n"
                                + "  0 AS ISACCEPTPOSITIVECASE,\n"
                                + "  LASTTIMEUPDATED,\n"
                                + "  DATEOFBIRTH,\n"
                                + "  STAGE,\n"
                                + "  GENDER,\n"
                                + "  STATE,\n"
                                + "  QC3COMMENT,\n"
                                + "  NULL AS POCVERIFIER,\n"
                                + "  NULL AS DOC5,\n"
                                + "  NULL AS DOC5_LABEL,\n"
                                + "  NULL AS VERIFICATIONPROOFDOC,\n"
                                + "  NULL AS DOC2_LABEL,\n"
                                + "  PHRASEOLOGY,\n"
                                + "  NULL AS CRMREMARKS,\n"
                                + "  CASERECEIVEDDATE,\n"
                                + "  0 AS ISBYPASSINSUFFICIENCY,\n"
                                + "  NULL AS ESCALATIONFROM,\n"
                                + "  ISQC3REJECT,\n"
                                + "  COUNTRY,\n"
                                + "  CHECKID,\n"
                                + "  NULL AS DOC2,\n"
                                + "  MIDDLENAME,\n"
                                + "  STATUS,\n"
                                + "  QC2COMMENT,\n"
                                + "  DOC4_LABEL,\n"
                                + "  ACTION,\n"
                                + "  QC2PHRASEOLOGY,\n"
                                + "  NULL AS ESCALATETO,\n"
                                + "  ESCALATIONDATE,\n"
                                + "  ISPOCBASEDVERIFICATION,\n"
                                + "  NULL AS INTERNALCOMMENTS,\n"
                                + "  COMPONENT,\n"
                                + "  ISSUINGAUTHORITY,\n"
                                + "  NATIONALITY,\n"
                                + "  \"MODE\",\n"
                                + "  CLIENTNAME,\n"
                                + "  PASSPORTNO,\n"
                                + "  SUBBARCODE,\n"
                                + "  FINALSTATUS,\n"
                                + "  NULL AS DOC4,\n"
                                + "  NULL AS CLIENTDISCREPANCY,\n"
                                + "  SPOKETO,\n"
                                + "  NULL AS DOC6,\n"
                                + "  VERIFICATIONCOMMENT,\n"
                                + "  PERFORMER,\n"
                                + "  RESULT,\n"
                                + "  QC3STATUS,\n"
                                + "  NULL AS EXPECTEDDOC,\n"
                                + "  ISQC2REJECT,\n"
                                + "  NULL AS DOC3_LABEL,\n"
                                + "  NULL AS DOC1_LABEL,\n"
                                + "  AKANAME,\n"
                                + "  ISERROR,\n"
                                + "  QCSTATUS,\n"
                                + "  NULL AS ALLDOCS,\n"
                                + "  ESCALATIONTYPE,\n"
                                + "  NULL AS HISTORYREMARKS,\n"
                                + "  MODEOFINITIATION,\n"
                                + "  LASTNAME,\n"
                                + "  VERIFICATIONLANGUAGE,\n"
                                + "  VTSTATUS,\n"
                                + "  QCEXTERNALCOMMENT,\n"
                                + "  CLIENTREFNO,\n"
                                + "  ECTREQUESTUSER,\n"
                                + "  ISBYPASSINSUFFICIENCYNOTIFY,\n"
                                + "  COMMENTSFROMECT,\n"
                                + "  INSUFFICIENCYNOTIFYSTATUS,\n"
                                + "  CRMACTION,\n"
                                + "  NULL AS ECTREQUESTSRC,\n"
                                + "  COMMENTSFORECT,\n"
                                + "  ISECTFLAG,\n"
                                + "  INSUFFICIENCYQCSTATUS,\n"
                                + "  NULL AS NOTIFICATION,\n"
                                + "  NULL AS PERSONALDOCS,\n"
                                + "  ISSUINGAUTHORITYID,\n"
                                + "  INSUFFICIENCYREJECTREMARKS,\n"
                                + "  MODEOFVERIFICATION,\n"
                                + "  CURRENCY,\n"
                                + "  VERIFICATIONFEE,\n"
                                + "  USDAMOUNT,\n"
                                + "  AMOUNT,\n"
                                + "  VISATYPE,\n"
                                + "  PAYMENTAPPROVALDATE,\n"
                                + "  VERAMOUNT,\n"
                                + "  CURRENCYEXCHANGERATEINR,\n"
                                + "  PAYMENTDATE,\n"
                                + "  TRANSACTIONID,\n"
                                + "  INSUFFPHRASEOLOGY,\n"
                                + "  TOTALAMOUNTINR,\n"
                                + "  PAYMENTINFAVOUROF,\n"
                                + "  0 AS ISDUPLICATES\n"
                                + "from (SELECT p.*,row_number() over(ORDER by p.process_instance_id ) as rn from ebms." + tableNames + "  p inner join (select distinct  process_instance_id from ebms.WORKITEM where START_TIME >= '" + startDate + "' or END_TIME >= '" + startDate + "' or END_TIME is null) B on (p.process_instance_id = b.process_instance_id) )  col where rn between " + initalVal + " and " + lastVla + "";

                    } else if (tableNames.equals("dvs_education")) {
                        table = "SELECT  attendancefromdate, attendancefromdate_ver, attendancetodate, attendancetodate_ver, barcode, checkid, checkstatus, collegename, creationdate, degreecertificate, degreetype, degreetype_ver, isgraduate, isgraduate_ver, issuingauthorityid, lastyearcomp, lastyearcomp_ver, majorsubject, majorsubject_ver, marksheet, minorsubject, name, name_ver, pid, qualification, qualificationconferreddate, qualificationconferreddate_ver, qualification_ver, studentidentityno, studentregistrationno, universityaddress, universityarea, universitycity, universitycountry, universityname, universitystate, universitytel, universitywebaddress, yearofcompletion, timing, timing_ver, insufficiencycomments, insufficiencyrejectremarks, historyremarks, crmremarks, qc3comment, qc2comment, internalcomments, verificationcomment, qcexternalcomment, commentsfromect, commentsforect from (SELECT p.*,row_number() over(ORDER by creationdate,barcode,checkid) as rn from ebms." + tableNames + "  p where creationdate > '" + startDate + "')  col where rn between " + initalVal + " and " + lastVla + "";
                    } else if (tableNames.equals("dvs_employment")) {
                        table = "SELECT agencyname, agency_address, agency_address_ver, agency_phno, agency_phno_ver, barcode, checkid, checkstatus, contactno, contactno_ver, creationdate, department, designation, designation_ver, employeecode, employeraddress, employerarea, employercity, employercountry, employername, employerstate, employertel, employerwebsite, employmentfromdate, employmentfromdate_ver, employmenttodate, employmenttodate_ver, familyowned, isempbyfamily, isempbyfamily_ver, issuingauthorityid, jobtype, lastsalarydrawn, lastsalarydrawn_ver, name, name_ver, pid, position, position_ver, salary, salary_ver, supervisorname, supervisortel, tenure, tenure_ver, insufficiencycomments, insufficiencyrejectremarks, historyremarks, crmremarks, qc3comment, qc2comment, internalcomments, verificationcomment, qcexternalcomment, commentsfromect, commentsforect from (SELECT p.*,row_number() over(ORDER by creationdate,barcode,checkid) as rn from ebms." + tableNames + "  p where creationdate > '" + startDate + "')  col where rn between " + initalVal + " and " + lastVla + "";
                    } else if (tableNames.equals("dvs_health_license")) {
                        table = "SELECT barcode, checkid, checkstatus, creationdate, iaaddress, iaarea, iacity, iacountry, ianame, iastate, iatel, iawebaddress, islicenseissued, islicensevalid, islicenseissued_ver, islicensevalid_ver, issuingauthorityid, licenseattained, licenseattained_ver, licenseattendancefrom, licenseattendanceto, licenseattendancefrom_ver, licenseattendanceto_ver, licenseconferreddate, licenseconferreddate_ver, licenseno, licenseno_ver, licensetype, licensetype_ver, name, name_ver, pid, insufficiencycomments, insufficiencyrejectremarks, historyremarks, crmremarks, qc3comment, qc2comment, internalcomments, verificationcomment, qcexternalcomment, commentsfromect, commentsforect from (SELECT p.*,row_number() over(ORDER by creationdate,barcode,checkid) as rn from ebms." + tableNames + "  p where creationdate > '" + startDate + "')  col where rn between " + initalVal + " and " + lastVla + "";
//              table = "SELECT  qc3comment, qc2comment ,creationdate,barcode from  ebms."+tableNames+"  where creationdate > '"+startDate+"'";

                    } else if (tableNames.equals("dvs_case")) {
                        table = "SELECT * from (SELECT p.*,row_number() over(ORDER by  barcode,caserecieveddate,pid) as rn from ebms." + tableNames + "  p where caserecieveddate >= '" + startDate + "')  col where rn between " + initalVal + " and " + lastVla + "";
                    } else if (tableNames.equals("dvs_issuing_authority")) {
                        table = "SELECT * from (SELECT p.*,row_number() over(ORDER by ISSUINGAUTHORITYID ) as rn from ebms." + tableNames + "  p where ISSUINGAUTHORITYID is not  null and ISSUINGAUTHORITYID > 277545 )  col where rn between " + initalVal + " and " + lastVla + "";
                    } else if (tableNames.equals("processinstance")) {
                        table = "SELECT * from (SELECT p.*,row_number() over(ORDER by process_instance_id ) as rn from ebms." + tableNames + "  p where START_TIME >= '" + startDate + "' or END_TIME >= '" + startDate + "' or END_TIME is null)  col where rn between " + initalVal + " and " + lastVla + "";
                    } else if (tableNames.equals("workitem")) {
                        table = "SELECT * from (SELECT p.*,row_number() over(ORDER by workitem_id ) as rn from ebms." + tableNames + "  p where START_TIME >= '" + startDate + "' or END_TIME >= '" + startDate + "' or END_TIME is null)  col where rn between " + initalVal + " and " + lastVla + "";
                    } else if (tableNames.equals("dvs_user") || tableNames.equals("TICKET_CRMUSER_LOCATION")) {
                        table = "SELECT * from (SELECT p.*,row_number() over(ORDER by username) as rn from ebms." + tableNames + "  p )  col where rn between " + initalVal + " and " + lastVla + "";

                    } else if (tableNames.equals("DOCUMENTVERFICATIONSYSTEM_1") ) {
                          table = "SELECT process_template_id, process_instance_id, appstatus, barcode, isdocsrequiredforde, employment_insufficiency_3, totalcheckcount, lastupdateddate, clientid, drvlicense_insufficiency_2, count_employment, isredefinecase, applicantname, drvlicense_insufficiency_1, employment_insufficiency_6, pid, firstname, employment_insufficiency_1, processdueduration, employment_insufficiency_10, personal_others, count_hltlicense, education_insufficiency_4, dateofbirth, gender, employment_insufficiency_8, employment_insufficiency_7, applicationform, personaldocs, education_insufficiency_3, isprintrequired, country, validationremarks, middlename, count_education, picompleteflag, employment_insufficiency_4, employment_insufficiency_9, healthlicense_insufficiency_1, education_insufficiency_1, internalcomments, healthlicense_insufficiency_2, nationality, \"MODE\", clientname, maidenname, passportno, isdataentered, namechangedoc, personaldoc2, education_insufficiency_2, passport, personaldoc1, count_drvlicense, isdocsattached, isdocsavailable, akaname, loa, alldocs, employment_insufficiency_5, employment_insufficiency_2, lastname, finalreport, pancard_insufficiency_1, nadra_insufficiency_1, criminal_insufficiency_1, count_criminal, count_address, birthcert_insufficiency_1, clientrefno, ispersonaldocsmapped, address_insufficiency_1, count_birthcertificate, count_marriagecertificate, count_pancard, count_nadra, marriagecert_insufficiency_1, count_goc, goc_insufficiency_1, bankstmt_insufficiency_1, count_bankstatement, count_propertypapers, proppapers_insufficiency_1, notification, temp, visatype, count_directorship, count_reference, count_database, isduplicates, count_identity, database_insufficiency_1, directorship_insufficiency_1, reference_insufficiency_1, identity_insufficiency_1, count_credit from (SELECT p.*,row_number() over(ORDER by process_instance_id,barcode,clientid) as rn from ebms.documentverficationsystem  p )  col where rn between " + initalVal + " and " + lastVla + "";
                          
                    } else if (tableNames.equals("documentverficationsystem")) {
//                         table = "SELECT process_template_id, process_instance_id, appstatus, barcode, isdocsrequiredforde, employment_insufficiency_3, totalcheckcount, lastupdateddate, clientid, drvlicense_insufficiency_2, count_employment, isredefinecase, applicantname, drvlicense_insufficiency_1, employment_insufficiency_6, pid, firstname, employment_insufficiency_1, processdueduration, employment_insufficiency_10, personal_others, count_hltlicense, education_insufficiency_4, dateofbirth, gender, employment_insufficiency_8, employment_insufficiency_7, applicationform, personaldocs, education_insufficiency_3, isprintrequired, country, validationremarks, middlename, count_education, picompleteflag, employment_insufficiency_4, employment_insufficiency_9, healthlicense_insufficiency_1, education_insufficiency_1, internalcomments, healthlicense_insufficiency_2, nationality, \"MODE\", clientname, maidenname, passportno, isdataentered, namechangedoc, personaldoc2, education_insufficiency_2, passport, personaldoc1, count_drvlicense, isdocsattached, isdocsavailable, akaname, loa, alldocs, employment_insufficiency_5, employment_insufficiency_2, lastname, finalreport, pancard_insufficiency_1, nadra_insufficiency_1, criminal_insufficiency_1, count_criminal, count_address, birthcert_insufficiency_1, clientrefno, ispersonaldocsmapped, address_insufficiency_1, count_birthcertificate, count_marriagecertificate, count_pancard, count_nadra, marriagecert_insufficiency_1, count_goc, goc_insufficiency_1, bankstmt_insufficiency_1, count_bankstatement, count_propertypapers, proppapers_insufficiency_1, notification, temp, visatype, count_directorship, count_reference, count_database, isduplicates, count_identity, database_insufficiency_1, directorship_insufficiency_1, reference_insufficiency_1, identity_insufficiency_1, count_credit from (SELECT p.*,row_number() over(ORDER by process_instance_id,barcode,clientid) as rn from ebms." + tableNames + "  p )  col where rn between " + initalVal + " and " + lastVla + "";
                        table = "SELECT process_template_id,\n"
                                + "  process_instance_id,\n"
                                + "  appstatus,\n"
                                + "  barcode,\n"
                                + "  isdocsrequiredforde,\n"
                                + "  employment_insufficiency_3,\n"
                                + "  totalcheckcount,\n"
                                + "  lastupdateddate,\n"
                                + "  clientid,\n"
                                + "  drvlicense_insufficiency_2,\n"
                                + "  count_employment,\n"
                                + "  isredefinecase,\n"
                                + "  applicantname,\n"
                                + "  drvlicense_insufficiency_1,\n"
                                + "  employment_insufficiency_6,\n"
                                + "  pid,\n"
                                + "  firstname,\n"
                                + "  employment_insufficiency_1,\n"
                                + "  processdueduration,\n"
                                + "  employment_insufficiency_10,\n"
                                + "  personal_others,\n"
                                + "  count_hltlicense,\n"
                                + "  education_insufficiency_4,\n"
                                + "  dateofbirth,\n"
                                + "  gender,\n"
                                + "  employment_insufficiency_8,\n"
                                + "  employment_insufficiency_7,\n"
                                + "  applicationform,\n"
                                + "  personaldocs,\n"
                                + "  education_insufficiency_3,\n"
                                + "  isprintrequired,\n"
                                + "  country,\n"
                                + "  validationremarks,\n"
                                + "  middlename,\n"
                                + "  count_education,\n"
                                + "  picompleteflag,\n"
                                + "  employment_insufficiency_4,\n"
                                + "  employment_insufficiency_9,\n"
                                + "  healthlicense_insufficiency_1,\n"
                                + "  education_insufficiency_1,\n"
                                + "  internalcomments,\n"
                                + "  healthlicense_insufficiency_2,\n"
                                + "  nationality,\n"
                                + "  \"MODE\",\n"
                                + "  clientname,\n"
                                + "  maidenname,\n"
                                + "  passportno,\n"
                                + "  isdataentered,\n"
                                + "  namechangedoc,\n"
                                + "  personaldoc2,\n"
                                + "  education_insufficiency_2,\n"
                                + "  passport,\n"
                                + "  personaldoc1,\n"
                                + "  count_drvlicense,\n"
                                + "  isdocsattached,\n"
                                + "  isdocsavailable,\n"
                                + "  akaname,\n"
                                + "  loa,\n"
                                + "  alldocs,\n"
                                + "  employment_insufficiency_5,\n"
                                + "  employment_insufficiency_2,\n"
                                + "  lastname,\n"
                                + "  finalreport,\n"
                                + "  pancard_insufficiency_1,\n"
                                + "  nadra_insufficiency_1,\n"
                                + "  criminal_insufficiency_1,\n"
                                + "  count_criminal,\n"
                                + "  count_address,\n"
                                + "  birthcert_insufficiency_1,\n"
                                + "  clientrefno,\n"
                                + "  ispersonaldocsmapped,\n"
                                + "  address_insufficiency_1,\n"
                                + "  count_birthcertificate,\n"
                                + "  count_marriagecertificate,\n"
                                + "  count_pancard,\n"
                                + "  count_nadra,\n"
                                + "  marriagecert_insufficiency_1,\n"
                                + "  count_goc,\n"
                                + "  goc_insufficiency_1,\n"
                                + "  bankstmt_insufficiency_1,\n"
                                + "  count_bankstatement,\n"
                                + "  count_propertypapers,\n"
                                + "  proppapers_insufficiency_1,\n"
                                + "  notification,\n"
                                + "  temp,\n"
                                + "  visatype,\n"
                                + "  count_directorship,\n"
                                + "  count_reference,\n"
                                + "  count_database,\n"
                                + "  isduplicates,\n"
                                + "  count_identity,\n"
                                + "  database_insufficiency_1,\n"
                                + "  directorship_insufficiency_1,\n"
                                + "  reference_insufficiency_1,\n"
                                + "  identity_insufficiency_1,\n"
                                + "  count_credit\n"
                                + "FROM\n"
                                + "  (SELECT p.*,\n"
                                + "    row_number() over(ORDER by process_instance_id,barcode,clientid) AS rn\n"
                                + "  FROM ebms." + tableNames + " p\n"
                                + "  where ( (to_date( (\n"
                                + "  CASE\n"
                                + "    WHEN instr ((SUBSTR (LASTUPDATEDDATE,1,12)),' ',1) = 3\n"
                                + "    THEN SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),1,2)\n"
                                + "      ||'-'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),4,3)\n"
                                + "      ||'-'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),9,4)\n"
                                + "    WHEN instr ((SUBSTR (LASTUPDATEDDATE,1,12)),' ',1) = 2\n"
                                + "    THEN '0'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),1,1)\n"
                                + "      ||'-'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),3,3)\n"
                                + "      ||'-'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),8,4)\n"
                                + "    WHEN instr ((SUBSTR (LASTUPDATEDDATE,1,12)),' ',1) =4\n"
                                + "    AND LENGTH(trim(SUBSTR (LASTUPDATEDDATE,1,12)))    =12\n"
                                + "    THEN SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),5,2)\n"
                                + "      ||'-'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),1,3)\n"
                                + "      ||'-'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),9,4)\n"
                                + "    WHEN instr ((SUBSTR (LASTUPDATEDDATE,1,12)),' ',1) =4\n"
                                + "    AND LENGTH(trim(SUBSTR (LASTUPDATEDDATE,1,12)))    =11\n"
                                + "    THEN '0'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),5,1)\n"
                                + "      ||'-'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),1,3)\n"
                                + "      ||'-'\n"
                                + "      ||SUBSTR((SUBSTR (LASTUPDATEDDATE,1,12)),8,4)\n"
                                + "  END),'DD-MON-YYYY' )) >= TRUNC(sysdate-2)  or LASTUPDATEDDATE is null or LASTUPDATEDDATE ='' or LASTUPDATEDDATE =' '    ) ) col\n"
                                + "where rn between " + initalVal + " and " + lastVla + "";

                    } else if (tableNames.equals("Ticket_Data")) {
                        table = "SELECT * from (SELECT p.*,row_number() over(ORDER by  caseid) as rn from ebms." + tableNames + "  p )  col where rn between " + initalVal + " and " + lastVla + "";
                    } else if (tableNames.equals("dvs_personal")) {
                        table = "select BARCODE,PID,"
                                + "FIRSTNAME,"
                                + "LASTNAME,"
                                + "MIDDLENAME,"
                                + "MAIDENNAME,"
                                + "DATEOFBIRTH,"
                                + "GENDER,"
                                + "NATIONALITY,"
                                + "PASSPORTNO,"
                                + "VISATYPE,"
                                + "NATIONALIDENTITYCARDNO,"
                                + "MAILINGADDRESS,"
                                + "MAILADDRESSCITY,"
                                + "MAILADDRESSPOSTCODE,"
                                + "MAILADDRESSCOUNTRY ,"
                                + "MAILADDRESSAREA ,"
                                + "TELPHONE ,"
                                + "CHECKSTATUS   ,"
                                + "PLACEOFBIRTH   ,"
                                + "PASSPORTCOUNTRY ,"
                                + "PANCARDNO    ,"
                                + "VISAAPPLIED    ,"
                                + "VISACOUNTRY     ,"
                                + "VISAAPPLIEDDATE   ,"
                                + "LOADOCATTACHED    ,"
                                + "PASSPORTDOCATTACHED ,"
                                + "APPFORMATTACHED    ,"
                                + "NAMECHANGEDOCATTACHED ,"
                                + "CREATIONDATE       ,"
                                + "REFERENCENO        ,"
                                + "EMAIL            ,"
                                + "STATE          ,"
                                + "CELLNO       ,"
                                + "FAXNO       ,"
                                + "ARABICNAME   ,"
                                + "COMMUNICATIONMODE1 ,"
                                + "COMMUNICATIONMODE2  ,"
                                + "COMMUNICATIONMODE3  ,"
                                + "PERMANENTADDRESS    ,"
                                + "PERMANENTCITY      ,"
                                + "PERMANENTSTATE      ,"
                                + "PERMANENTCOUNTRY     ,"
                                + "PERMANENTPOSTALCODE  ,"
                                + "EMPLOYERNAME         ,"
                                + "EMPLOYERADDRESS       ,"
                                + "EMPLOYERPHONENO      ,"
                                + "EMPLOYERFROMDATE    ,"
                                + "EMPLOYERTODATE      ,"
                                + "EMPLOYERTITLE      ,"
                                + "EMPLOYERDEPT  from ebms.dvs_personal where creationdate >= sysdate-5";
                    } else if (tableNames.equals("dvs_modeofverification")) {
                        table = "SELECT PROCESS_INSTANCE_ID,MODEOFVERIFICATION , row_number() over(Partition by MODEOFVERIFICATION ORDER by process_instance_id ) as rn FROM ebms.dvs p";
                    } else if (tableNames.equals("dvs_deleted_barcodes")) {
                        table = "SELECT * FROM ebms.dvs_deleted_barcodes";
                    } else if (tableNames.equals("end_date_missing_log")) {
                        table = "SELECT * FROM ebms.end_date_missing_log";
                    }
                    logger.info(table);
                    PreparedStatement stmt = con.prepareStatement(table);
                    ResultSet rs = null;
                    Date date = null;
                    if (PHILIPPINES) {
                        date = new Date();
                        logrw.fileWriterWithFileName("---------------------SELECT Query Started for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
                        copyTimeLog(lastCopyTablePId2, tableNames + " SELECT", "PHILIPPINES");//insert start time
                        rs = stmt.executeQuery();
                        date = new Date();
                        logrw.fileWriterWithFileName("---------------------SELECT Query Completed for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
                        setCompletedTime(0);//update end time
                    } else {
                        date = new Date();
                        logrw.fileWriterWithFileName("---------------------SELECT Query Started for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
                        copyTimeLog(lastCopyTablePId, tableNames + " SELECT", "Dataflow_Auto_Etl");//insert start time
                        rs = stmt.executeQuery();
                        date = new Date();
                        logrw.fileWriterWithFileName("---------------------SELECT Query Completed for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
                        setCompletedTime(0);//update end time   
                    }
                    if (PHILIPPINES) {
                        date = new Date();
                        logrw.fileWriterWithFileName("---------------------INSERTION  Started for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
                        copyTimeLog(lastCopyTablePId2, tableNames + " INSERTION", "PHILIPPINES");//insert start time
                    } else {
                        date = new Date();
                        logrw.fileWriterWithFileName("---------------------INSERTION  Started for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
                        copyTimeLog(lastCopyTablePId, tableNames + " INSERTION", "Dataflow_Auto_Etl");//insert start time 
                    }

                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();
                    String[] cols = new String[colCount];
                    String[] columnTypes = new String[colCount];
                    String names = "insert into " + tableNames + "_stg(";
                    String values11 = " values (";
                    if (tableNames.equals("dvs_personal") || tableNames.equals("dvs_deleted_barcodes") || tableNames.equals("end_date_missing_log")) {
                        totalloop = colCount;
                    } else {
                        totalloop = colCount - 1;
                    }
                    for (int i = 0; i < totalloop; i++) {
                        cols[i] = meta.getColumnClassName(i + 1);
                        columnTypes[i] = meta.getColumnTypeName(i + 1);
                        names += meta.getColumnName(i + 1).toString().replace("#", "");
                        names += ",";
                        values11 += "?";
                        values11 += ",";
                    }
                    String finalQuery = "";
                    finalQuery += names.substring(0, names.length() - 1);
                    finalQuery += ")";
                    finalQuery += values11.substring(0, values11.length() - 1);
                    finalQuery += ")";
                    PreparedStatement stmt1 = con1.prepareStatement(finalQuery);
                    int j = 0;
                    int Typei = 0;
                    int count = 0;
                    int countTot = 0;
                    while (rs.next()) {
                        count++;
                        countTot++;
                        totalrows++;
                        try {
                            for (int i = 0; i < totalloop; i++) {
                                Typei = meta.getColumnType(i + 1);
                                if (meta.getColumnType(i + 1) == Types.BIGINT || meta.getColumnType(i + 1) == Types.DECIMAL || meta.getColumnType(i + 1) == Types.DOUBLE || meta.getColumnType(i + 1) == Types.FLOAT || meta.getColumnType(i + 1) == Types.INTEGER || meta.getColumnType(i + 1) == Types.NUMERIC || meta.getColumnType(i + 1) == Types.REAL || meta.getColumnType(i + 1) == Types.SMALLINT || meta.getColumnType(i + 1) == Types.TINYINT) {
                                    if (meta.getColumnType(i + 1) == Types.FLOAT) {
                                        stmt1.setFloat(i + 1, rs.getFloat(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.DECIMAL) {
                                        stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.DOUBLE) {
                                        stmt1.setDouble(i + 1, rs.getDouble(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.BIGINT) {
                                        stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.TINYINT
                                            || meta.getColumnType(i + 1) == Types.SMALLINT
                                            || meta.getColumnType(i + 1) == Types.INTEGER) {
                                        stmt1.setInt(i + 1, rs.getInt(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.NUMERIC
                                            || meta.getColumnType(i + 1) == Types.REAL) {
                                        stmt1.setFloat(i + 1, rs.getFloat(i + 1));
                                    } else {
                                        stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                                    }
                                } else if (meta.getColumnType(i + 1) == Types.DATE) {
                                    stmt1.setDate(i + 1, rs.getDate(i + 1));
                                } else if (meta.getColumnType(i + 1) == Types.TIMESTAMP) {
                                    stmt1.setTimestamp(i + 1, rs.getTimestamp(i + 1));
                                } else if (meta.getColumnTypeName(i + 1).equalsIgnoreCase("CLOB")) {
                                    stmt1.setClob(i + 1, rs.getClob(i + 1));
                                } else if (rs.getString(i + 1) != null && !rs.getString(i + 1).equalsIgnoreCase("")) {
                                    stmt1.setString(i + 1, (String) rs.getString(i + 1).replace("'", "").replace("&", " and "));
                                } else {
                                    stmt1.setString(i + 1, rs.getString(i + 1));
                                }
                                j = i;
                            }
                        } // } catch (Exception e) {
                        finally {
                        }
                        stmt1.addBatch();
                        if (count == 5000) {
                            stmt1.executeBatch();
                            logger.info("Bach of 5 thousands executed");
                            count = 0;
                        }
                    }
                    //added by Dinanath
                    if (PHILIPPINES) {
                        if (count > 0) {
                            stmt1.executeBatch();
                        }
                        date = new Date();
                        logrw.fileWriterWithFileName("---------------------INSERTION Completed for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
                        setCompletedTime(totalrows);//update end time
                    } else {
                        if (count > 0) {
                            stmt1.executeBatch();
                        }
                        date = new Date();
                        logrw.fileWriterWithFileName("---------------------INSERTION Completed for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
                        setCompletedTime(totalrows);//update end time
                    }
                    con1.commit();
                    stmt1.close();
                    initalVal = initalVal + range;
                    lastVla = lastVla + range;
                    load = "success";

                    logger.info("count...." + countTot);
                    if (countTot < range || tableNames.equals("dvs_personal") || tableNames.equals("dvs_modeofverification")
                            || tableNames.equals("dvs_deleted_barcodes") || tableNames.equals("end_date_missing_log")) {
                        if (load.equalsIgnoreCase("success")) {
                            if (!PHILIPPINES) {
                                updateTrackerMaster(tableNames, todaysDate, conname);
                            }
                            logger.info("(" + Totalloc + ") Total " + totalrows + " Rows Inserted for " + tableNames + "_stg");
                            if (!Filename.equalsIgnoreCase("")) {
                                logrw1.fileWriterWithFileName("(" + Totalloc + ") Total " + totalrows + " Rows Inserted for " + tableNames + "_stg\n", Filename);
                            }
                        }
                        logger.info("less than range.. " + countTot);

                        return 1;
                    }
                }
            }
//         logger.info("Successfully loaded the data for"+tableNames);
            Date date = new Date();
            logrw.fileWriterWithFileName("---------------------Truncation and Insertion Completed for table " + tableNames + "-----------------------@" + date.toString() + "\n", Filename);
            logger.info("(" + Totalloc + ") Total " + totalrows + " Rows Inserted for " + tableNames + "_stg");
            if (!Filename.equalsIgnoreCase("")) {
                logrw1.fileWriterWithFileName("(" + Totalloc + ") Total " + totalrows + " Rows Inserted for " + tableNames + "_stg\n", Filename);
            }
            if (load.equalsIgnoreCase("success")) {
                if (!PHILIPPINES) {
                    updateTrackerMaster(tableNames, todaysDate, conname);
                    logger.info("updateTrackerMaster Function called");
                }
            }

        } //    catch  (Exception e) {
        //        load = "failure";
        //        logger.error(":::::ERROOOOOORRRRRRRRRRRRR::::"+e);
        //    } 
        finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    load = "failure";
                    logger.error(":::::ERROOOOOORRRRRRRRRRRRR:::::", ex);
                }

            }
            if (con1 != null) {
                try {
                    con1.close();
                } catch (SQLException ex) {
                    load = "failure";
                    logger.error(":::::ERROOOOOORRRRRRRRRRRRR:::::", ex);
                }

            }
        }
        return 1;
    }

    public int Live_Case_Update(String tableNames, String Last_date) {
//logger.info("Table Name:::::::::::::"+tableNames);
//logger.info("start Date:::::::::::::"+startDate);
//logger.info("todaysDate:::::::::::::"+todaysDate);
//logger.info("End Date:::::::::::::"+endDate);
        Connection con = null;
        Connection con1 = null;
        String conname = "Df_mySql";
        con = sc.getConnection("Df_oracle", "", "", "", "", "", "", "", "");
        con1 = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
        String ldate = "";
        try {
            int value = 0;
            int initalVal = 0;
            int lastVla = 0;
            int finalValue = 0;
            int range = 0;
            int totalloop = 0;
            con1.setAutoCommit(false);
            if (con != null) {
                String table = "";
                if (tableNames.equalsIgnoreCase("DVS_LIVE_CASE_COUNT")) {
                    int[] noofre = {(450000000)};
                    value = noofre[0];
                    initalVal = 1;
                    range = 5000;
                    lastVla = range;
                    finalValue = (value / lastVla) + 1;
                }
                for (int n = 0; n < finalValue; n++) {
                    if (tableNames.equalsIgnoreCase("DVS_LIVE_CASE_COUNT")) {
                        table = "SELECT * from (SELECT p.*,row_number() over(ORDER by CURR_TIME,TYPE ) as rn from ebms." + tableNames + "  p "
                                + " where CURR_TIME > '" + Last_date + "') "
                                + "  col where rn between " + initalVal + " and " + lastVla + " ";
                    }
                    logger.info(table);
                    PreparedStatement stmt = con.prepareStatement(table);
                    //stmt.setFetchSize(250);

                    ResultSet rs = stmt.executeQuery();
                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();
//                int rowcount=rs.getRow();
//                logger.info("rowcount"+rowcount);
                    String[] cols = new String[colCount];
                    String[] columnTypes = new String[colCount];
                    String names = "insert into " + tableNames + "_stg(";
                    String values11 = " values (";
                    if (tableNames.equals("dvs_personal")) {
                        totalloop = colCount;
                    } else {
                        totalloop = colCount - 1;
                    }
                    for (int i = 0; i < totalloop; i++) {
                        cols[i] = meta.getColumnClassName(i + 1);
                        columnTypes[i] = meta.getColumnTypeName(i + 1);
                        // out.println(meta.getColumnTypeName(i + 1));
                        names += meta.getColumnName(i + 1).toString().replace("#", "");
                        names += ",";
                        values11 += "?";
                        values11 += ",";
                    }
                    String finalQuery = "";
                    finalQuery += names.substring(0, names.length() - 1);
                    finalQuery += ")";
                    finalQuery += values11.substring(0, values11.length() - 1);
                    finalQuery += ")";
                    //out.println("<BR>"+finalQuery);
                    PreparedStatement stmt1 = con1.prepareStatement(finalQuery);
                    int j = 0;
                    int Typei = 0;
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        try {
                            for (int i = 0; i < totalloop; i++) {
                                Typei = meta.getColumnType(i + 1);
                                if (meta.getColumnType(i + 1) == Types.BIGINT || meta.getColumnType(i + 1) == Types.DECIMAL || meta.getColumnType(i + 1) == Types.DOUBLE || meta.getColumnType(i + 1) == Types.FLOAT || meta.getColumnType(i + 1) == Types.INTEGER || meta.getColumnType(i + 1) == Types.NUMERIC || meta.getColumnType(i + 1) == Types.REAL || meta.getColumnType(i + 1) == Types.SMALLINT || meta.getColumnType(i + 1) == Types.TINYINT) {
                                    if (meta.getColumnType(i + 1) == Types.FLOAT) {
                                        // out.println("<br>getFloat---"+rs.getFloat(i + 1));
                                        stmt1.setFloat(i + 1, rs.getFloat(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.DECIMAL) {
                                        //  out.println("<br>getBigDecimal---"+rs.getBigDecimal(i + 1));
                                        stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.DOUBLE) {
                                        //  out.println("<br>getDouble---"+rs.getDouble(i + 1));
                                        stmt1.setDouble(i + 1, rs.getDouble(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.BIGINT) {

                                        stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.TINYINT
                                            || meta.getColumnType(i + 1) == Types.SMALLINT
                                            || meta.getColumnType(i + 1) == Types.INTEGER) {
                                        stmt1.setInt(i + 1, rs.getInt(i + 1));
                                    } else if (meta.getColumnType(i + 1) == Types.NUMERIC
                                            || meta.getColumnType(i + 1) == Types.REAL) {
                                        stmt1.setFloat(i + 1, rs.getFloat(i + 1));
                                    } else {
                                        stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                                    }
                                } else if (meta.getColumnType(i + 1) == Types.DATE) {
                                    //  out.println("<br>getDate---"+rs.getDate(i + 1));
                                    stmt1.setDate(i + 1, rs.getDate(i + 1));

                                } else if (meta.getColumnType(i + 1) == Types.TIMESTAMP) {
                                    // out.println("<br>timestamp---"+rs.getTimestamp(i + 1));

                                    stmt1.setTimestamp(i + 1, rs.getTimestamp(i + 1));
                                    ldate = (rs.getTimestamp(i + 1)).toString();

                                } else if (rs.getString(i + 1) != null && !rs.getString(i + 1).equalsIgnoreCase("")) {
                                    //   out.println("<br>getString1---"+rs.getString(i + 1));
                                    stmt1.setString(i + 1, (String) rs.getString(i + 1).replace("'", "").replace("&", " and "));
                                } else {
                                    //   out.println("<br>getString2---"+rs.getString(i + 1));
                                    stmt1.setString(i + 1, rs.getString(i + 1));
                                }
                                j = i;
                            }
                        } catch (Exception e) {
                            logger.info("+++++++" + j + "Data Type " + Typei);
                            //out.print(e);
                        }
                        //out.println("stmt1------"+stmt1.toString());
                        stmt1.addBatch();
//		logger.info("++++ETL STARTED Query+++"+stmt1.toString());
                    }

//                                out.println("iiiiiii--"+j);
//                out.println("<br>Statement--------->"+stmt1.toString());
                    stmt1.executeBatch();
                    con1.commit();
                    stmt1.close();
                    initalVal = initalVal + range;
                    lastVla = lastVla + range;
                    load = "success";

                    logger.info("count...." + count);
                    if (count < range) {
                        if (load.equalsIgnoreCase("success")) {
                            updateLiveCase(ldate);
                            logger.info("updateTrackerMaster Function called");
                        }
                        logger.info("less than range.. " + count);
                        return 1;
                    }
                }
            }
            logger.info("Successfully loaded the data for" + tableNames);
            if (load.equalsIgnoreCase("success")) {
                updateLiveCase(ldate);
                logger.info("updateTrackerMaster Function called");
            }

        } catch (Exception e) {
            load = "failure";
            logger.error(":::::ERROOOOOORRRRRRRRRRRRR::::", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    load = "failure";
                    logger.error(":::::ERROOOOOORRRRRRRRRRRRR:::::", ex);
                }

            }
            if (con1 != null) {
                try {
                    con1.close();
                } catch (SQLException ex) {
                    load = "failure";
                    logger.error(":::::ERROOOOOORRRRRRRRRRRRR:::::", ex);
                }

            }
        }
        return 1;
    }

    public void updateLiveCase(String ldate) {

        Connection TargetConn = null;
        TargetConn = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
        PbReturnObject retObj = null;
        PbDb pbdb = new PbDb();

        if (ldate.equals("")) {
            logger.info("No need to Update PRG_LOAD_TRACKER_MASTER");
        } else {
            try {

                Date ludate = null;

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                ludate = dateFormat.parse(ldate);
                DateFormat dateFormat1 = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSS a");
//                            Date date = new Date();H

                String todaysDate = dateFormat1.format(ludate);
                logger.info("todaysDate " + todaysDate);
                TargetConn = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                if (TargetConn != null) {
                    String qry = "UPDATE PRG_LOAD_TRACKER_MASTER set last_live_case_update = '" + todaysDate + "' WHERE id=100";
                    logger.info("UPDATE TRACKER MASTER:::::::::::::" + qry);
//        PbDb pbdb = new PbDb();
//        PbReturnObject retObj = null;
                    int c = 0;

                    c = pbdb.execUpdateSQL(qry, TargetConn);

                } else {
                    logger.error("Connection ERRRORRRRRRR");
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void updateTrackerMaster(String tableNames, String todaysDate, String conname) throws Exception {
//        logger.info("In Update Tracker Master..................");
        //String targetCon = "";
//        Connection ScConn = null;
        Connection TargetConn = null;
        String qry = "";
//        ScConn = sc.getConnection("PRODDTA", "", "", "", "", "", "", "", "");
        TargetConn = sc.getConnection(conname, "", "", "", "", "", "", "", "");
        if (TargetConn != null) {
            if (todaysDate.equalsIgnoreCase("No Need")) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                Date date = new Date();
                todaysDate = dateFormat.format(date);
                qry = "UPDATE PRG_LOAD_TRACKER_MASTER set last_dvs_user_update = '" + todaysDate + "' WHERE id=100";
            } else {
                qry = "UPDATE PRG_LOAD_TRACKER_MASTER set LAST_UPDATE_DATE = '" + todaysDate + "' WHERE TAB_NAME='" + tableNames + "'";
            }
            logger.info("UPDATE TRACKER MASTER:::::::::::::" + qry);
            PbDb pbdb = new PbDb();
            PbReturnObject retObj = null;
            int c = 0;
            try {
                c = pbdb.execUpdateSQL(qry, TargetConn);
            } //            catch (Exception ex) {
            //                logger.error("Exception:",ex);
            //            }
            finally {
            }
        } else {
            logger.error("Connection ERRRORRRRRRR");
        }
    }

    public void updateTrackerMasterForAll(String tableNames, String LOCATION_NAME, String filename) {
        String query = "";
        PbDb db = new PbDb();
        switch (ProgenConnection.getInstance().getDatabaseType()) {
            case ProgenConnection.SQL_SERVER:
                query = "UPDATE prg_load_tracker_master SET LAST_UPDATE_DATE=getDate() WHERE TAB_NAME='&' and Location_name='&'";
                break;
            case ProgenConnection.MYSQL:
                query = "UPDATE prg_load_tracker_master SET LAST_UPDATE_DATE=curdate() WHERE TAB_NAME='&' and Location_name='&'";
                break;
            default:
                query = "UPDATE prg_load_tracker_master SET LAST_UPDATE_DATE=sysdate WHERE TAB_NAME='&' and Location_name='&'";
                break;
        }
        Object[] objArr = new Object[2];
        objArr[0] = tableNames;
        objArr[1] = LOCATION_NAME;
        String finalQuery = db.buildQuery(query, objArr);
        PbDb pbdb = new PbDb();
        PbReturnObject retObj = null;
        int c = 0;
        try {
            c = pbdb.execUpdateSQL(finalQuery);
            logger.info("TRACKER MASTER UPDATED for Table " + tableNames);
            logrw.fileWriterWithFileName("TRACKER MASTER UPDATED for Table " + tableNames, filename);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }
    //added by Dinanath

    public void setEtlCompletedDate() {
        String query = "update staging.prg_load_tracker_master set etl_completed_date=curdate() WHERE id=100";
        int c = 0;
        try {
            PbDb pbdb = new PbDb();
            c = pbdb.execUpdateSQL(query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
    //added by Dinanath

    public Date getEtlCompletedDate() {
        String query = "select etl_completed_date from staging.prg_load_tracker_master WHERE id=100";
        int c = 0;
        try {
            PbDb pbdb = new PbDb();
            PbReturnObject retObj = pbdb.execSelectSQL(query);
            Date etl_completed_date = retObj.getFieldValueDate(0, 0);
            return etl_completed_date;
        } catch (Exception ex) {
            logger.error("Exception:", ex);

        }
        return null;
    }
    //added by Dinanath

    public void setEtlFailedDate() {
        String query = "update staging.prg_load_tracker_master set last_etl_failed_date=curdate() WHERE id=100";
        int c = 0;
        try {
            PbDb pbdb = new PbDb();
            c = pbdb.execUpdateSQL(query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
    //added by Dinanath

    public Date getEtlFailedDate() {
        String query = "select last_etl_failed_date from staging.prg_load_tracker_master WHERE id=100";
        int c = 0;
        try {
            PbDb pbdb = new PbDb();
            PbReturnObject retObj = pbdb.execSelectSQL(query);
            Date etl_completed_date = retObj.getFieldValueDate(0, 0);
            return etl_completed_date;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }
    //added by Dinanath

    public Date setEtlStartedDate() {
        String query = "update staging.prg_load_tracker_master set last_etl_start_date=curdate() WHERE id=100";
        int c = 0;
        try {
            PbDb pbdb = new PbDb();
            c = pbdb.execUpdateSQL(query);

        } catch (Exception ex) {
            logger.error("Exception:", ex);

        }
        return null;
    }
    //added by Dinanath

    public Date getEtlStartedDate() {
        String query = "select last_etl_start_date from staging.prg_load_tracker_master WHERE id=100";
        int c = 0;
        try {
            PbDb pbdb = new PbDb();
            PbReturnObject retObj = pbdb.execSelectSQL(query);
            Date etl_completed_date = retObj.getFieldValueDate(0, 0);
            return etl_completed_date;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }
//added by Dinanath

    public String runDailyPhilippines_Auto_ETL(ReportSchedule s1) throws Exception {
        try {
            String queryInsertHeaderOnlyOnce = "insert into metaflow.prg_procedure_header(procedure_name,start_time,end_time,status_flag)values('Philippines_Auto_Etl_Table',sysdate(),null,null)";
            String LastIdOfQueryInsert = "select last_insert_id() as last_id from metaflow.prg_procedure_header";
            PbDb pbdb = new PbDb();
            PbReturnObject retObj = null;
            int status1 = pbdb.execUpdateSQL(queryInsertHeaderOnlyOnce);
            String lastid = null;
            if (status1 != 0) {
                retObj = pbdb.execSelectSQL(LastIdOfQueryInsert);
                if (retObj != null && retObj.getRowCount() > 0) {
                    lastid = retObj.getFieldValueString(0, 0);
                }
            }
            this.runDailyPhilippines_Auto_ETL_Main(s1, 0, 10, lastid);

            String queryUpdate2procDetails2 = "update metaflow.prg_procedure_header set end_time=sysdate(),status_flag='SUCCESS' where id=" + lastid + " and procedure_name='Philippines_Auto_Etl_Table'";
            int status2insert = pbdb.execUpdateSQL(queryUpdate2procDetails2);
            curr_step2 = 0;
            next_step2 = 1;
            fromCatch2 = false;
            proclastid2 = null;
            lastCopyTablePId2 = 0;
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return "success";
    }

    //added by Dinanath for running Philippines auto etl at 12:00 'o' clock
    public String runDailyPhilippines_Auto_ETL_Main(ReportSchedule s1, int retryCount, int limit, String lastidInsert) throws Exception {
        LoadSchedulerJob callmethod = new LoadSchedulerJob();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        Date date = new Date();
        String todaysDate = dateFormat.format(date);
        final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
        final SourceConn sc = new SourceConn();
        Connection con = null;
        CallableStatement proc = null;
        PbReturnObject retObj = null;
        String tableName = "", startDate = "";
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        date = new Date();
        String s = formatter.format(date);
        String Filename = "Philippines_Load" + s;

        try {
            PbDb pbdb = new PbDb();
            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from staging.prg_load_tracker_master where  tab_name not in ('TICKET_CRMUSER_LOCATION','Ticket_Data','dvs_personal','dvs_modeofverification')";
            if (fromCatch2 == false) {
                logrw2.fileWriterWithFileName("----------------------------------Philippins ETL Scheduler " + s + " ---------------------------------------", Filename);
                logger.info("----------------------------------Philippins Scheduler " + s + " ---------------------------------------" + Filename);
            }
            retObj = pbdb.execSelectSQL(query);
            if (retObj != null && retObj.getRowCount() > 0) {

                for (int i = curr_step2; i < retObj.getRowCount(); i++) {
                    tableName = retObj.getFieldValueString(i, 0);
                    startDate = retObj.getFieldValueString(i, 1);

                    String queryInsert2procDetails = "insert into metaflow.prg_procedure_details(header_id,procedure_name,process_name,start_time,end_time,status_flag,step_no,next_step_no)values('&','&','&',&,'&','&','&','&')";
                    Object object[] = new Object[8];
                    object[0] = lastidInsert;
                    object[1] = tableName + "_truncat_insert_philip";
                    object[2] = tableName + "_truncat_insert_philip";
                    object[3] = "sysdate()";
                    object[4] = null;
                    object[5] = null;
                    curr_step2 = i;
                    next_step2 = i + 1;
                    object[6] = curr_step2;
                    object[7] = next_step2;
                    String finalQuery = pbdb.buildQuery(queryInsert2procDetails, object);
                    int status2 = pbdb.execUpdateSQL(finalQuery);
                    lastCopyTablePId2 = Integer.parseInt(lastidInsert);

                    date = new Date();
                    logrw2.fileWriterWithFileName("---------------------Truncation Started for table " + tableName + "-----------------------@" + date.toString() + "\n", Filename);
                    copyTimeLog(lastCopyTablePId2, tableName + " TRUNCATION", "PHILIPPINES");//insert start time
                    int j = pbdb.execUpdateSQL("TRUNCATE TABLE staging." + tableName + "_stg");
                    date = new Date();
                    logrw2.fileWriterWithFileName("---------------------Truncation Completed for table " + tableName + "-----------------------@" + date.toString() + "\n", Filename);
                    setCompletedTime(0);//update end time

                    logger.info("Executed successfully:::::::::::::TRUNCATE TABLE " + tableName + "_stg");

                    date = new Date();
                    logger.info("eltJkp.runLoadForDataflow..philip.start.." + tableName + "...." + date);

                    eltJkp.runLoadForDataflow(tableName, startDate, todaysDate, true, Filename);//table copy

                    logger.info("Insertion completed successfully:::::::::::::insertion in TABLE " + tableName + "_stg");

                    String queryUpdate2procDetails2 = "update metaflow.prg_procedure_details set end_time=sysdate(),status_flag='SUCCESS' where header_id=" + lastidInsert + " and procedure_name='" + tableName + "_truncat_insert_philip'";
                    int status2insert = pbdb.execUpdateSQL(queryUpdate2procDetails2);

                }//for end
                curr_step2++;
                if (proclastid2 == null) {
                    date = new Date();
                    logrw2.fileWriterWithFileName("---------------------Procedure calling started-----------------------@" + date.toString() + "\n", Filename);
                    retryCount = 0;
                    String queryInsertHeaderOnlyOnce = "insert into metaflow.prg_procedure_header(procedure_name,start_time,end_time,status_flag)values('Philippines_Etl_Proc',sysdate(),null,null)";
                    String LastIdOfQueryInsert = "select last_insert_id() as last_id from metaflow.prg_procedure_header where procedure_name='Philippines_Etl_Proc'";
                    int status1 = pbdb.execUpdateSQL(queryInsertHeaderOnlyOnce);
                    if (status1 != 0) {
                        retObj = pbdb.execSelectSQL(LastIdOfQueryInsert);
                        if (retObj != null && retObj.getRowCount() > 0) {
                            proclastid2 = retObj.getFieldValueString(0, 0);
                        }
                    }
                    String queryInsert2procDetails = "insert into metaflow.prg_procedure_details(header_id,procedure_name,process_name,start_time,end_time,status_flag,step_no,next_step_no)values('&','&','&',&,'&','&','&','&')";
                    Object object[] = new Object[8];
                    object[0] = proclastid2;
                    object[1] = "first_philip_proc_initial";
                    object[2] = "first_philip_proc_initial";
                    object[3] = "sysdate()";
                    object[4] = null;
                    object[5] = null;
                    object[6] = 0;
                    object[7] = 1;
                    String finalQuery = pbdb.buildQuery(queryInsert2procDetails, object);
                    int status2 = pbdb.execUpdateSQL(finalQuery);
                }
                String detailq = "select max(next_step_no) from metaflow.prg_procedure_details where header_id=" + proclastid2;
                int step_no = 0;
                retObj = pbdb.execSelectSQL(detailq);
                if (retObj != null && retObj.getRowCount() > 0) {
                    step_no = retObj.getFieldValueInt(0, 0);
                }
                logger.info("calling dataflow.philippines_proc_auto_etl( ?,?)...start......" + new Date().toString());

                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
                proc = con.prepareCall("{ call dataflow.philippines_proc_auto_etl(?,?) }");//Now this one is main auto etl
                proc.setInt(1, Integer.parseInt(proclastid2));
                proc.setInt(2, step_no);
                proc.execute();

                logger.info("call dataflow.philippines_proc_auto_etl(?,?)...finish......" + new Date().toString());

                String queryUpdate2procDetails4 = "update metaflow.prg_procedure_details set end_time=sysdate(),status_flag='SUCCESS' where header_id=" + proclastid2 + " and procedure_name='first_philip_proc_initial'";
                int status2insert4 = pbdb.execUpdateSQL(queryUpdate2procDetails4);

                String queryUpdate2procDetails2 = "update metaflow.prg_procedure_header set end_time=sysdate(),status_flag='SUCCESS' where id=" + proclastid2 + " and procedure_name='Philippines_Etl_Proc'";
                int status2insert = pbdb.execUpdateSQL(queryUpdate2procDetails2);
                date = new Date();
                logrw2.fileWriterWithFileName("---------------------Procedure calling completed-----------------------@" + date.toString() + "\n", Filename);
                if (con != null) {
                    con.close();
                }
                if (proc != null) {
                    proc.close();
                }
            }//if end
            date = new Date();
            logrw2.fileWriterWithFileName(":::::Philippines ETL LOAD COMPLETED SUCCESSFULLY::::@" + date.toString() + "\n", Filename);
            logger.info(":::::Philippins ETL LOAD COMPLETED SUCCESSFULLY::::@" + date.toString());
            callmethod.sendSchedulerFinishMail(s1, Filename);//
        } catch (Exception ex) {

            String exception = "Exception" + ex;
            logger.error("::::::::::::::::Philippines_AUTO_ETL Main Exception*****************", ex);
            if (exception.contains("Communications link failure")) {
                fromCatch2 = true;
                if (retryCount > 1) {
                    callmethod.commuLinkFailureOrDeadLockMail(5, exception, "Communications link failure");
                    TimeUnit.MINUTES.sleep(5);
                } else {
                    callmethod.commuLinkFailureOrDeadLockMail(2, exception, "Communications link failure");
                    TimeUnit.MINUTES.sleep(2);
                }
                retryCount++;
                if (retryCount > limit) {
                    logger.info("Tryied to execute when connection error occured and 3 times limit exceeded at " + new Date());
                    try {
                        logrw2.fileWriterWithFileName("Philippines LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                    } catch (IOException ex1) {
                        logger.error("Exception:", ex1);
                    }
                    callmethod.sendSchedulerEtlFailedMail(s1, "Philippines LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                    logger.error("Exception:", ex);
                } else {
                    runDailyPhilippines_Auto_ETL_Main(s1, retryCount, limit, lastidInsert);
                }
            } else if (exception.contains("Deadlock found when trying to get lock")) {
                fromCatch2 = true;
                if (retryCount > 1) {
                    callmethod.commuLinkFailureOrDeadLockMail(15, exception, "Deadlock found when trying to get lock");
                    TimeUnit.MINUTES.sleep(15);
                } else {
                    callmethod.commuLinkFailureOrDeadLockMail(5, exception, "Deadlock found when trying to get lock");
                    TimeUnit.MINUTES.sleep(5);
                }
                retryCount++;
                if (retryCount > limit) {
                    logger.info("Tryied to execute when connection error occured and 3 times limit exceeded at " + new Date());
                    try {
                        logrw2.fileWriterWithFileName("Philippines LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                    } catch (IOException ex1) {
                        logger.error("Exception:", ex1);
                    }
                    callmethod.sendSchedulerEtlFailedMail(s1, "Philippines LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                    logger.error("Exception:", ex);
                } else {
                    runDailyPhilippines_Auto_ETL_Main(s1, retryCount, limit, lastidInsert);
                }
            } else {
                try {
                    logrw2.fileWriterWithFileName("Philippines LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                } catch (IOException ex1) {
                    logger.error("Exception:", ex1);
                }
                callmethod.sendSchedulerEtlFailedMail(s1, "Philippines LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                logger.error("Exception:", ex);
            }
        }

        return "success";
    }
    //end of code by Dinanath   

//added by Dinanath for main running auto etl at 03:00 o clock
    public String runDailyDFLoadAutoETL(ReportSchedule s1) throws Exception {
        try {
            String queryInsertHeaderOnlyOnce = "insert into metaflow.prg_procedure_header(procedure_name,start_time,end_time,status_flag)values('DF_Auto_Etl_Table',sysdate(),null,null)";
            String LastIdOfQueryInsert = "select last_insert_id() as last_id from metaflow.prg_procedure_header";
            PbDb pbdb = new PbDb();
            PbReturnObject retObj = null;
            int status1 = pbdb.execUpdateSQL(queryInsertHeaderOnlyOnce);
            String lastid = null;
            if (status1 != 0) {
                retObj = pbdb.execSelectSQL(LastIdOfQueryInsert);
                if (retObj != null && retObj.getRowCount() > 0) {
                    lastid = retObj.getFieldValueString(0, 0);
                }
            }
            this.runDailyDFLoadAutoETLMain(s1, 0, 10, lastid);

            String queryUpdate2procDetails2 = "update metaflow.prg_procedure_header set end_time=sysdate(),status_flag='SUCCESS' where id=" + lastid;
            int status2insert = pbdb.execUpdateSQL(queryUpdate2procDetails2);
            curr_step = 0;
            next_step = 1;
            fromCatch = false;
            proclastid = null;
            lastCopyTablePId = 0;
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return "success";
    }
//added by Dinanath for Dataflow auto etl

    public String runDailyDFLoadAutoETLMain(ReportSchedule s1, int retryCount, int limit, String lastidInsert) throws Exception {
        LoadSchedulerJob callmethod = new LoadSchedulerJob();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        Date date = new Date();
        String todaysDate = dateFormat.format(date);
        final EtlLoadForJKP eltJkp = new EtlLoadForJKP();
        final SourceConn sc = new SourceConn();
        Connection con = null;
        CallableStatement proc = null;
        PbReturnObject retObj = null;
        String tableName = "", startDate = "";
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        date = new Date();
        String s = formatter.format(date);
        String Filename = "DataFlow_Load" + s;

        try {
            PbDb pbdb = new PbDb();
            String query = "SELECT TAB_NAME,LAST_UPDATE_DATE from staging.prg_load_tracker_master where  tab_name not in ('TICKET_CRMUSER_LOCATION','Ticket_Data','dvs_personal','dvs_modeofverification')";
            eltJkp.setEtlStartedDate();//added by Dinanath
            if (fromCatch == false) {
                logrw.fileWriterWithFileName("----------------------------------Data Flow ETL Scheduler " + s + " ---------------------------------------", Filename);
                logger.info("----------------------------------Data Flow ETL Scheduler " + s + " ---------------------------------------" + Filename);
            }
            retObj = pbdb.execSelectSQL(query);
            if (retObj != null && retObj.getRowCount() > 0) {

                for (int i = curr_step; i < retObj.getRowCount(); i++) {
                    tableName = retObj.getFieldValueString(i, 0);
                    startDate = retObj.getFieldValueString(i, 1);

                    String queryInsert2procDetails = "insert into metaflow.prg_procedure_details(header_id,procedure_name,process_name,start_time,end_time,status_flag,step_no,next_step_no)values('&','&','&',&,'&','&','&','&')";
                    Object object[] = new Object[8];
                    object[0] = lastidInsert;
                    object[1] = tableName + "_truncat_insert";
                    object[2] = tableName + "_truncat_insert";
                    object[3] = "sysdate()";
                    object[4] = null;
                    object[5] = null;
                    curr_step = i;
                    next_step = i + 1;
                    object[6] = curr_step;
                    object[7] = next_step;
                    String finalQuery = pbdb.buildQuery(queryInsert2procDetails, object);
                    int status2 = pbdb.execUpdateSQL(finalQuery);
                    lastCopyTablePId = Integer.parseInt(lastidInsert);

                    date = new Date();
                    logrw.fileWriterWithFileName("---------------------Truncation Started for table " + tableName + "-----------------------@" + date.toString() + "\n", Filename);
                    copyTimeLog(lastCopyTablePId, tableName + " TRUNCATION", "Dataflow_Auto_Etl");//insert start time
                    int j = pbdb.execUpdateSQL("TRUNCATE TABLE staging." + tableName + "_stg");
                    date = new Date();
                    logrw.fileWriterWithFileName("---------------------Truncation Completed for table " + tableName + "-----------------------@" + date.toString() + "\n", Filename);
                    setCompletedTime(0);//update end time

                    logger.info("Executed successfully:::::::::::::TRUNCATE TABLE " + tableName + "_stg");

                    date = new Date();
                    logger.info("eltJkp.runLoadForDataflow...start.." + tableName + "...." + date);
                    eltJkp.runLoadForDataflow(tableName, startDate, todaysDate, false, Filename);
                    logger.info("Insertion completed successfully:::::::::::::insertion in TABLE " + tableName + "_stg");

                    String queryUpdate2procDetails2 = "update metaflow.prg_procedure_details set end_time=curdate(),status_flag='SUCCESS' where header_id=" + lastidInsert + " and procedure_name='" + tableName + "_truncat_insert" + "'";
                    int status2insert = pbdb.execUpdateSQL(queryUpdate2procDetails2);

                }//for end
                curr_step++;
                if (proclastid == null) {
                    date = new Date();
                    logrw.fileWriterWithFileName("---------------------Procedure calling started-----------------------@" + date.toString() + "\n", Filename);
                    retryCount = 0;
                    String queryInsertHeaderOnlyOnce = "insert into metaflow.prg_procedure_header(procedure_name,start_time,end_time,status_flag)values('DF_Auto_Etl_Proc',sysdate(),null,null)";
                    String LastIdOfQueryInsert = "select last_insert_id() as last_id from metaflow.prg_procedure_header";
                    int status1 = pbdb.execUpdateSQL(queryInsertHeaderOnlyOnce);
                    if (status1 != 0) {
                        retObj = pbdb.execSelectSQL(LastIdOfQueryInsert);
                        if (retObj != null && retObj.getRowCount() > 0) {
                            proclastid = retObj.getFieldValueString(0, 0);
                        }
                    }
                    String queryInsert2procDetails = "insert into metaflow.prg_procedure_details(header_id,procedure_name,process_name,start_time,end_time,status_flag,step_no,next_step_no)values('&','&','&',&,'&','&','&','&')";
                    Object object[] = new Object[8];
                    object[0] = proclastid;
                    object[1] = "first_proc_initial";
                    object[2] = "first_proc_initial";
                    object[3] = "sysdate()";
                    object[4] = null;
                    object[5] = null;
                    object[6] = 0;
                    object[7] = 1;
                    String finalQuery = pbdb.buildQuery(queryInsert2procDetails, object);
                    int status2 = pbdb.execUpdateSQL(finalQuery);
                }
                String detailq = "select max(next_step_no) from metaflow.prg_procedure_details where header_id=" + proclastid;
                int step_no = 0;
                retObj = pbdb.execSelectSQL(detailq);
                if (retObj != null && retObj.getRowCount() > 0) {
                    step_no = retObj.getFieldValueInt(0, 0);
                }
                logger.info("calling dataflow.DF_AUTO_ETL( ?,?)...start......" + new Date().toString());
                con = sc.getConnection("Df_mySql", "", "", "", "", "", "", "", "");
//                //proc = con.prepareCall("{ call dataflow.neeraj_all(?,?) }");//this one also main without auto etl
//               // proc = con.prepareCall("{ call dataflow.neeraj_all_28jan(?,?) }");//this one for test

                proc = con.prepareCall("{ call dataflow.DF_AUTO_ETL(?,?) }");//Now this one is main auto etl
                proc.setInt(1, Integer.parseInt(proclastid));
                proc.setInt(2, step_no);
                proc.execute();
                logger.info("call dataflow.DF_AUTO_ETL(?,?)...finish......" + new Date().toString());

                String queryUpdate2procDetails4 = "update metaflow.prg_procedure_details set end_time=sysdate(),status_flag='SUCCESS' where header_id=" + proclastid + " and procedure_name='first_proc_initial'";
                int status2insert4 = pbdb.execUpdateSQL(queryUpdate2procDetails4);

                String queryUpdate2procDetails2 = "update metaflow.prg_procedure_header set end_time=sysdate(),status_flag='SUCCESS' where id=" + proclastid;
                int status2insert = pbdb.execUpdateSQL(queryUpdate2procDetails2);
                date = new Date();
                logrw.fileWriterWithFileName("---------------------Procedure calling completed-----------------------@" + date.toString() + "\n", Filename);
                if (con != null) {
                    con.close();
                }
                if (proc != null) {
                    proc.close();
                }
            }//if end
            date = new Date();
            logrw.fileWriterWithFileName(":::::DATAFLOW ETL LOAD COMPLETED SUCCESSFULLY::::@" + date.toString() + "\n", Filename);
            logger.info(":::::DATAFLOW ETL LOAD COMPLETED SUCCESSFULLY::::@" + date.toString());
            eltJkp.setEtlCompletedDate();//added by Dinanath
            callmethod.sendSchedulerFinishMail(s1, Filename);//
            callmethod.scheduleJobAfterEtlCompleted(); //Added by DINANATH
        } catch (Exception ex) {

            String exception = "Exception" + ex;
            logger.info("::::::::::::::::DATAFLOW_AUTO_ETL Main Exception*****************" + exception);
            if (exception.contains("Communications link failure")) {
                fromCatch = true;
                if (retryCount > 1) {
                    callmethod.commuLinkFailureOrDeadLockMail(5, exception, "Communications link failure");
                    TimeUnit.MINUTES.sleep(5);
                } else {
                    callmethod.commuLinkFailureOrDeadLockMail(2, exception, "Communications link failure");
                    TimeUnit.MINUTES.sleep(2);
                }
                retryCount++;
                if (retryCount > limit) {
                    logger.info("Tryied to execute when connection error occured and 3 times limit exceeded at " + new Date());
                    try {
                        logrw.fileWriterWithFileName("DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                    } catch (IOException ex1) {
                        logger.error("Exception:", ex1);
                    }
                    callmethod.sendSchedulerEtlFailedMail(s1, "DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                    callmethod.delayReportSchedulerMailIfEtlFailed();
                    eltJkp.setEtlFailedDate();//added by Dinanath
                    logger.error("Exception:", ex);
                } else {
                    runDailyDFLoadAutoETLMain(s1, retryCount, limit, lastidInsert);
                }
            } else if (exception.contains("Deadlock found when trying to get lock")) {
                fromCatch = true;
                if (retryCount > 1) {
                    callmethod.commuLinkFailureOrDeadLockMail(15, exception, "Deadlock found when trying to get lock");
                    TimeUnit.MINUTES.sleep(15);
                } else {
                    callmethod.commuLinkFailureOrDeadLockMail(5, exception, "Deadlock found when trying to get lock");
                    TimeUnit.MINUTES.sleep(5);
                }
                retryCount++;
                if (retryCount > limit) {
                    logger.info("Tryied to execute when connection error occured and 3 times limit exceeded at " + new Date());
                    try {
                        logrw.fileWriterWithFileName("DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                    } catch (IOException ex1) {
                        logger.error("Exception:", ex1);
                    }
                    callmethod.sendSchedulerEtlFailedMail(s1, "DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                    callmethod.delayReportSchedulerMailIfEtlFailed();
                    eltJkp.setEtlFailedDate();//added by Dinanath
                    logger.error(":::::ETL ERROOOOOORRRRRRRRRRRRR::::", ex);
                } else {
                    runDailyDFLoadAutoETLMain(s1, retryCount, limit, lastidInsert);
                }
            } else {
                try {
                    logrw.fileWriterWithFileName("DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                } catch (IOException ex1) {
                    logger.error("Exception:", ex1);
                }
                callmethod.sendSchedulerEtlFailedMail(s1, "DATA FLOW LOAD ERROR FOR TABLE " + tableName + " And Exception is ****" + ex + "****", Filename);
                callmethod.delayReportSchedulerMailIfEtlFailed();
                eltJkp.setEtlFailedDate();//added by Dinanath
                logger.error(":::::ETL ERROOOOOORRRRRRRRRRRRR::::", ex);
            }
        }

        return "success";
    }

    public void copyTimeLog(int pid, String tableName, String etlType) {
        try {
            PbDb pbdb = new PbDb();
            String queryUpdate2procDetails4 = "insert into metaflow.prg_copy_time_log(p_id,tablename,start_time,etl_type)values(" + pid + ",'" + tableName + "',sysdate(),'" + etlType + "')";
            int status2insert4 = pbdb.execUpdateSQL(queryUpdate2procDetails4);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void setCompletedTime(int noofrowsComp) {
        int seq_no = 0;
        try {
            PbDb pbdb = new PbDb();
            String detailq = "select max(seq_no) from metaflow.prg_copy_time_log ";
            PbReturnObject retObj = pbdb.execSelectSQL(detailq);
            if (retObj != null && retObj.getRowCount() > 0) {
                seq_no = retObj.getFieldValueInt(0, 0);
            }
            String queryUpdate2procDetails4 = null;
            if (noofrowsComp > 0) {
                queryUpdate2procDetails4 = "update metaflow.prg_copy_time_log set end_time=sysdate(),rows_count=" + noofrowsComp + " where seq_no=" + seq_no;
            } else {
                queryUpdate2procDetails4 = "update metaflow.prg_copy_time_log set end_time=sysdate() , rows_count=null where seq_no=" + seq_no;
            }
            int status2insert4 = pbdb.execUpdateSQL(queryUpdate2procDetails4);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
    //end of code by Dinanath

    public int LoadForCampBell(String tableNames, String LOCATION_NAME, String filename,String columnNames) throws SQLException, IOException {
        Totalloc++;
        Connection Des = null;
        Connection Source = null;
        String conname = "Df_mySql";
        UploadingXmlIntoDatabase UpXml = new UploadingXmlIntoDatabase();
        Source = sc.getConnection(LOCATION_NAME, "", "", "", "", "", "", "", "");
        try {
            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
            Des = DriverManager.getConnection("jdbc:mysql://localhost:3306/META_CS?useServerPrepStmts=false&rewriteBatchedStatements=true","root","CampBell2015");
//            Des = ProgenConnection.getInstance().getConnection();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
     String ldate="";
      int totalrows=0;
        try {
                  int value =0;
                int initalVal =0;
            int lastVla = 0;
                int finalValue =0;
                int range=0;
                int totalloop=0;
                 String names="";

            Source.setAutoCommit(false);
            if (Source != null) {
            String table = "select "+columnNames+" from "+tableNames;
//logrw.fileWriterWithFileName(table,filename);
//String table = "select * from "+tableNames;
                System.out.println(table);
                PreparedStatement stmt = Source.prepareStatement(table);
                //stmt.setFetchSize(250);

                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

//              int rowcount=rs.getRow();
//                System.out.println("rowcount"+rowcount);
                String[] cols = new String[colCount];
                String[] columnTypes = new String[colCount];
                if (LOCATION_NAME.equalsIgnoreCase("ACCPAC_CAMPBELL")) {
                    names = "insert into ERP_CS_COMPANY_DATA_DBO." + tableNames + "(";
                } else if (LOCATION_NAME.equalsIgnoreCase("NS5")) {
                    names = "insert into SAFENET." + tableNames + "(";
                }
                String values11 = " values (";
                for (int i = 0; i < colCount; i++) {
                    cols[i] = meta.getColumnClassName(i + 1);
                    columnTypes[i] = meta.getColumnTypeName(i + 1);
                    names += "`" + meta.getColumnName(i + 1).toString().replace("#", "") + "`";
                    names += ",";
                    values11 += "?";
                    values11 += ",";
                }
                String finalQuery = "";
                finalQuery += names.substring(0, names.length() - 1);
                finalQuery += ")";
                finalQuery += values11.substring(0, values11.length() - 1);
                finalQuery += ")";
                //out.println("<BR>"+finalQuery);
                PreparedStatement stmt1 = Des.prepareStatement(finalQuery);
//                        stmt1.execute("use safenet");
//                          stmt1.execute("SET FOREIGN_KEY_CHECKS=0");
                int j = 0;
                int Typei = 0;
                int count = 0;
                while (rs.next()) {
                    count++;
                    totalrows++;
//				try{
                    for (int i = 0; i < colCount; i++) {
                        Typei = meta.getColumnType(i + 1);
                        if (meta.getColumnType(i + 1) == Types.BIGINT || meta.getColumnType(i + 1) == Types.DECIMAL || meta.getColumnType(i + 1) == Types.DOUBLE || meta.getColumnType(i + 1) == Types.FLOAT || meta.getColumnType(i + 1) == Types.INTEGER || meta.getColumnType(i + 1) == Types.NUMERIC || meta.getColumnType(i + 1) == Types.REAL || meta.getColumnType(i + 1) == Types.SMALLINT || meta.getColumnType(i + 1) == Types.TINYINT) {
                            if (meta.getColumnType(i + 1) == Types.FLOAT) {
                                // out.println("<br>getFloat---"+rs.getFloat(i + 1));
                                stmt1.setFloat(i + 1, rs.getFloat(i + 1));
                            } else if (meta.getColumnType(i + 1) == Types.DECIMAL) {
                                //  out.println("<br>getBigDecimal---"+rs.getBigDecimal(i + 1));
                                stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                            } else if (meta.getColumnType(i + 1) == Types.DOUBLE) {
                                //  out.println("<br>getDouble---"+rs.getDouble(i + 1));
                                stmt1.setDouble(i + 1, rs.getDouble(i + 1));
                            } else if (meta.getColumnType(i + 1) == Types.BIGINT) {

                                stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                            } else if (meta.getColumnType(i + 1) == Types.TINYINT
                                    || meta.getColumnType(i + 1) == Types.SMALLINT
                                    || meta.getColumnType(i + 1) == Types.INTEGER) {
                                stmt1.setInt(i + 1, rs.getInt(i + 1));
                            } else if (meta.getColumnType(i + 1) == Types.NUMERIC
                                    || meta.getColumnType(i + 1) == Types.REAL) {
                                stmt1.setFloat(i + 1, rs.getFloat(i + 1));
                            } else {
                                stmt1.setBigDecimal(i + 1, rs.getBigDecimal(i + 1));
                            }
                        } else if (meta.getColumnType(i + 1) == Types.DATE) {
                            //  out.println("<br>getDate---"+rs.getDate(i + 1));
                            stmt1.setDate(i + 1, rs.getDate(i + 1));

                        } else if (meta.getColumnType(i + 1) == Types.TIMESTAMP) {
                            // out.println("<br>timestamp---"+rs.getTimestamp(i + 1));
                            try {
                                stmt1.setTimestamp(i + 1, rs.getTimestamp(i + 1));
                            } catch (Exception e) {
                                stmt1.setTimestamp(i + 1, null);

                            }
//                          ldate=(rs.getTimestamp(i+1)).toString();

                        } else if (rs.getString(i + 1) != null && !rs.getString(i + 1).equalsIgnoreCase("")) {
                            //   out.println("<br>getString1---"+rs.getString(i + 1));
                            stmt1.setString(i + 1, (String) rs.getString(i + 1).replace("'", "").replace("&", " and "));
                        } else {
                            //   out.println("<br>getString2---"+rs.getString(i + 1));
                            stmt1.setString(i + 1, rs.getString(i + 1));
                        }
                        j = i;
                    }
// }
//               catch (Exception e) {
//                        
//                        //out.print(e);
//                        }

                    stmt1.addBatch();
                    if (count == 50000) {
                        stmt1.executeBatch();
                        stmt1.close();
                        stmt1 = Des.prepareStatement(finalQuery);
                        // Des.commit();

                        count = 0;
                    }
//		
                }
//                                out.println("iiiiiii--"+j);
//                out.println("<br>Statement--------->"+stmt1.toString());
                stmt1.executeBatch();
//                stmt1.execute("SET FOREIGN_KEY_CHECKS=1");
                // Des.commit();
                stmt1.close();
                initalVal = initalVal + range;
                lastVla = lastVla + range;
                load = "success";
                logrw.fileWriterWithFileName("(" + Totalloc + ") Total " + totalrows + " Rows Inserted for " + tableNames, filename);
            }

            if (load.equalsIgnoreCase("success")) {
                UpXml.InsertIntoTrackerMaster(tableNames, "Scheduler", totalrows + "", "Succeed");
                updateTrackerMasterForAll(tableNames, LOCATION_NAME, filename);

            }

        } //    catch  (Exception ex) {
        //        load = "failure";
        //        
        //         UpXml.InsertIntoTrackerMaster(tableNames+"_stg", "Scheduler", totalrows+" and Exception is "+ex, "Failed");
        //
        //    }
        finally {
            if (Source != null) {
                try {
                    Source.close();
                } catch (SQLException ex) {
                    load = "failure";

                    logger.error("Exception:", ex);
                }

            }
            if (Des != null) {
                try {
                    Des.close();
                } catch (SQLException ex) {
                    load = "failure";

                    logger.error("Exception:", ex);
                }

            }
        }
        return totalrows;
    }
}
