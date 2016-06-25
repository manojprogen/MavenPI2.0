package com.progen.Createtable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import prg.business.group.BusinessGroupDAO;
import prg.business.group.PbBussGrpResourceBundle;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class SaveExcelTimeDimention extends PbDb {
// PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();

    public static Logger logger = Logger.getLogger(SaveExcelTimeDimention.class);
    PbBussGrpResourceBundle resBundle = new PbBussGrpResourceBundle();

    public boolean inserttimeDimen(String busgrpID, String sheetName, PbReturnObject prgr) throws Exception {
        boolean check = false;

        logger.info("sheetName:\t" + sheetName);
        try {
            String getCalenderId = resBundle.getString("getCalenderId");//check Query in ResourceBundle
            prg.db.PbDb pbdb = new prg.db.PbDb();
            String grpId = busgrpID;
            PbReturnObject selCalDetails = execSelectSQL(getCalenderId);

            String bussTableId = prgr.getFieldValue(0, "BUSS_TABLE_ID").toString();
            String bussColId = prgr.getFieldValue(0, "BUSS_COLUMN_ID").toString();
            String tabName = sheetName.toUpperCase();
            String colName = prgr.getFieldValue(0, "COLUMN_NAME").toString();
            String colType = prgr.getFieldValue(0, "COLUMN_TYPE").toString();
            String minTimeLevel = "5";
            String calenderId = selCalDetails.getFieldValueString(0, "CALENDER_ID");
            String calenderTable = selCalDetails.getFieldValueString(0, "DENOM_TABLE");


            String TimeDimMemName[] = {"Year", "Quarter", "Month", "Week", "Day"};
            String TimeDimMemDesc[] = {"Year", "Quarter", "Month", "Week", "Day"};
            String TimeDimMemDenomQuery[] = new String[5];
            TimeDimMemDenomQuery[0] = "SELECT DISTINCT CY_CUST_NAME,CY_CUST_NAME FROM " + calenderTable;
            TimeDimMemDenomQuery[1] = "SELECT DISTINCT CQ_CUST_NAME,CQ_CUST_NAME FROM " + calenderTable;
            TimeDimMemDenomQuery[2] = "SELECT DISTINCT CM_CUST_NAME,CM_CUST_NAME FROM " + calenderTable;
            TimeDimMemDenomQuery[3] = "SELECT DISTINCT CW_CUST_NAME,CW_CUST_NAME FROM " + calenderTable;
            TimeDimMemDenomQuery[4] = "SELECT DISTINCT DDATE,DDATE FROM " + calenderTable;
            ArrayList timeDimList = new ArrayList();
            String finalQuery = "";
            //for adding into grp_buss_table
            String getBusinessTableConnectionId = resBundle.getString("getBusinessTableConnectionId");
            Object objc[] = new Object[1];
            objc[0] = bussTableId;
            finalQuery = pbdb.buildQuery(getBusinessTableConnectionId, objc);
            PbReturnObject pbroConId = pbdb.execSelectSQL(finalQuery);
            String ConnectionId = String.valueOf(pbroConId.getFieldValueInt(0, 0));
            String username = pbroConId.getFieldValueString(0, 1).toUpperCase();
            //getting details columns from pr_day_denom
            BusinessGroupDAO b = new BusinessGroupDAO();
            Connection con = b.getBussTableConnection(bussTableId);
            PreparedStatement st = null;con.createStatement();
            String leveln="";
            PbReturnObject denom = null;
            if (minTimeLevel.equals("5")) {
                leveln="select column_name, data_type, data_length from all_tab_cols  where owner='?' and table_name='?'";
                st=con.prepareCall(leveln);
                st.setString(1, username);
                st.setString(2, calenderTable);
                denom = new PbReturnObject(st.executeQuery());
            } else if (minTimeLevel.equals("4")) {
                leveln="select distinct column_name, data_type, data_length from all_tab_cols  where owner='?' and table_name='?' and (column_name like 'CW%' or column_name like 'PW%' or column_name like 'LYW%' or column_name like 'CM%' or column_name like 'PM%' or column_name like 'PYM%' or column_name like 'CQ%' or column_name like 'LQ%' or column_name like 'LYQ%' or column_name like 'CY%' or column_name like 'LY%')";
                st=con.prepareCall(leveln);
                st.setString(1, username);
                st.setString(2, calenderTable);
                denom = new PbReturnObject(st.executeQuery());
            } else if (minTimeLevel.equals("3")) {
                leveln="select distinct column_name, data_type, data_length from all_tab_cols  where owner='?' and table_name='?' and column_name in ('CM_CUST_NAME','LY_END_DATE','CQ_YEAR','LYQ_ST_DATE','CY_END_DATE','CM_ST_DATE','PYM_ST_DATE','PYM_END_DATE','CQ_ST_DATE','CQ_END_DATE',"
                        + "'CY_ST_DATE','LY_CUST_NAME','CMONTH','CM_END_DATE','PYM_CUST_NAME','LQ_CUST_NAME','PM_CUST_NAME','LQ_ST_DATE','LQ_END_DATE','LY_ST_DATE','CM_YEAR','LYQ_END_DATE',"
                        + "'LYQ_CUST_NAME','CY_CUST_NAME','PM_ST_DATE','CQTR','CQ_CUST_NAME','CYEAR','PM_END_DATE')";
                st=con.prepareCall(leveln);
                st.setString(1, username);
                st.setString(2, calenderTable);
                denom = new PbReturnObject(st.executeQuery());
            } else if (minTimeLevel.equals("2")) {
                leveln="select distinct column_name, data_type, data_length from all_tab_cols  where owner='?' and table_name='?' and (  column_name like 'CQ%' or column_name like 'LQ%' or column_name like 'LYQ%' or column_name like 'CY%' ) or column_name in ('LY_ST_DATE','LY_END_DATE','LY_CUST_NAME')";
                st=con.prepareCall(leveln);
                st.setString(1, username);
                st.setString(2, calenderTable);
                denom = new PbReturnObject(st.executeQuery());
            } else if (minTimeLevel.equals("1")) {
                leveln="select distinct column_name, data_type, data_length from all_tab_cols  where owner='?' and table_name='?' and (column_name like 'CY%' ) or column_name in ('LY_ST_DATE','LY_END_DATE','LY_CUST_NAME')";
                st=con.prepareCall(leveln);
                st.setString(1, username);
                st.setString(2, calenderTable);
                denom = new PbReturnObject(st.executeQuery());
            }
            String colNames[] = new String[denom.getRowCount()];
            String colTypes[] = new String[denom.getRowCount()];
            String getColNames[] = denom.getColumnNames();
            String querystring = "";
            for (int i = 0;
                    i < denom.getRowCount();
                    i++) {
                colNames[i] = denom.getFieldValueString(i, getColNames[0]);
                colTypes[i] = denom.getFieldValueString(i, getColNames[1]);
                querystring += "," + denom.getFieldValueString(i, getColNames[0]);
            }
            if (denom.getRowCount() > 0) {
                querystring = querystring.substring(1);
            }
            st.close();
            con.close();
            Statement batchSt = ProgenConnection.getInstance().getCustomerConn().createStatement();
            if (denom.getRowCount() > 0) {
                String addTimeDimBussMater = resBundle.getString("addTimeDimBussMater");
                int seqaddTimeDimBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                Object obj[];
                obj = new Object[10];
                obj[0] = seqaddTimeDimBussMater;
                obj[1] = "PROGEN_TIME";
                obj[2] = "PROGEN_TIME";
                obj[3] = "Query";
                obj[4] = 1;
                obj[5] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                // obj[5] = "SELECT DISTINCT " + colName + " FROM " + tabName;
                obj[6] = grpId;
                obj[7] = calenderTable;
                obj[8] = sheetName.toUpperCase();
                obj[9] = sheetName.toUpperCase();
                finalQuery = pbdb.buildQuery(addTimeDimBussMater, obj);
                logger.info("final addTimeDimBussMater ---->" + finalQuery);
                batchSt.addBatch(finalQuery);
                // timeDimList.add(finalQuery);

                String addTimeDimSrc = resBundle.getString("addTimeDimSrc");
                int seqaddTimeDimSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                Object obj1[];
                obj1 = new Object[5];
                obj1[0] = seqaddTimeDimSrc;
                obj1[1] = seqaddTimeDimBussMater;
                obj1[2] = "Query";
                obj1[3] = ConnectionId;
                obj1[4] = "SELECT DISTINCT " + querystring + " FROM " + calenderTable;
                finalQuery = pbdb.buildQuery(addTimeDimSrc, obj1);
                // timeDimList.add(finalQuery);
          batchSt.addBatch(finalQuery);
                //add grp buss src details
                String srcdetnextvalarr[] = new String[denom.getRowCount()];
                for (int i = 0; i < denom.getRowCount(); i++) {
                    PbReturnObject srcseqval = pbdb.execSelectSQL("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                    int srcdetnextval = srcseqval.getFieldValueInt(0, "NEXTVAL");
                    srcdetnextvalarr[i] = String.valueOf(srcdetnextval);
                    String addTimeDimDetails = resBundle.getString("addTimeDimSrcDetails");
                    Object obj2[] = new Object[5];
                    obj2[0] = srcdetnextval;
                    obj2[1] = seqaddTimeDimSrc;
                    obj2[2] = seqaddTimeDimBussMater;
                    obj2[3] = colNames[i];
                    obj2[4] = colTypes[i];
                    finalQuery = pbdb.buildQuery(addTimeDimDetails, obj2);
                    batchSt.addBatch(finalQuery);

                }
                //add grp buss details

                String seqaddTimeDimBussDetailsarr[] = new String[denom.getRowCount()];
                //String memBussNames[]=new String[5];
                //  String memBussIds[]=new String[5];
                HashMap h = new HashMap();
                String rltdateId = "";
                String rltweeknoId = "";
                String rltweekyearId = "";
                String rltmonthnoId = "";
                String rltmonthyearId = "";
                String rltmonthstrId = "";
                String rltmonthendId = "";
                String rltqtrnoId = "";
                String rltqtryearId = "";
                String rltyearId = "";
                for (int i = 0; i < denom.getRowCount(); i++) {
                    String addTimeDimBussDetails = resBundle.getString("addTimeDimBussDetails");
                    int seqaddTimeDimBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                    seqaddTimeDimBussDetailsarr[i] = String.valueOf(seqaddTimeDimBussDetails);
                    if (colNames[i].equals("DDATE")) {
                        h.put("Day", String.valueOf(seqaddTimeDimBussDetails));
                    } else if (colNames[i].equals("CW_CUST_NAME")) {
                        h.put("Week", String.valueOf(seqaddTimeDimBussDetails));
                    } else if (colNames[i].equals("CM_CUST_NAME")) {
                        h.put("Month", String.valueOf(seqaddTimeDimBussDetails));
                    } else if (colNames[i].equals("CQ_CUST_NAME")) {
                        h.put("Quarter", String.valueOf(seqaddTimeDimBussDetails));
                    } else if (colNames[i].equals("CY_CUST_NAME")) {
                        h.put("Year", String.valueOf(seqaddTimeDimBussDetails));
                    }
                    if (colNames[i].equals("DDATE")) {
                        rltdateId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CWEEK")) {
                        rltweeknoId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CWYEAR")) {
                        rltweekyearId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CMONTH")) {
                        rltmonthnoId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CM_YEAR")) {
                        rltmonthyearId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CM_ST_DATE")) {
                        rltmonthstrId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CM_END_DATE")) {
                        rltmonthendId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CQTR")) {
                        rltqtrnoId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CQ_YEAR")) {
                        rltqtryearId = String.valueOf(seqaddTimeDimBussDetails);
                    } else if (colNames[i].equals("CYEAR")) {
                        rltyearId = String.valueOf(seqaddTimeDimBussDetails);
                    }
                    Object obj3[] = new Object[6];
                    obj3[0] = seqaddTimeDimBussDetails;
                    obj3[1] = seqaddTimeDimBussMater;
                    obj3[2] = colNames[i];
                    obj3[3] = srcdetnextvalarr[i];
                    obj3[4] = colTypes[i];
                    obj3[5] = colNames[i];
                    finalQuery = pbdb.buildQuery(addTimeDimBussDetails, obj3);
                    batchSt.addBatch(finalQuery);
                }
                //add grp rlt master
                //add grp rlt details

                Statement batchSt1 = ProgenConnection.getInstance().getCustomerConn().createStatement();

                //add dimensions
                String addTimeDimDimension = resBundle.getString("addTimeDimDimension");
                int seqaddTimeDimDimension = pbdb.getSequenceNumber("select  PRG_GRP_DIMENSIONS_SEQ.nextval from dual");
                Object obj4[] = new Object[5];
                obj4[0] = seqaddTimeDimDimension;
                obj4[1] = "Time";
                obj4[2] = "Time";
                obj4[3] = "Y";
                obj4[4] = grpId;
                finalQuery = pbdb.buildQuery(addTimeDimDimension, obj4);
                batchSt1.addBatch(finalQuery);
                //add grp dimension table
                String addTimeDimTables = resBundle.getString("addTimeDimTables");
                int seqaddTimeDimTables = pbdb.getSequenceNumber("select  PRG_GRP_DIM_TABLES_SEQ.nextval from dual");
                Object obj5[] = new Object[3];
                obj5[0] = seqaddTimeDimTables;
                obj5[1] = seqaddTimeDimDimension;
                obj5[2] = seqaddTimeDimBussMater;
                finalQuery = pbdb.buildQuery(addTimeDimTables, obj5);
                batchSt1.addBatch(finalQuery);
                //add grp dimension table details
                for (int i = 0; i < denom.getRowCount(); i++) {
                    String addTimeDimTableDetails = resBundle.getString("addTimeDimTableDetails");
                    int seqaddTimeDimTableDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_TAB_DETAILS_SEQ.nextval from dual");
                    Object obj6[] = new Object[5];
                    obj6[0] = seqaddTimeDimTableDetails;
                    obj6[1] = seqaddTimeDimTables;
                    obj6[2] = seqaddTimeDimBussDetailsarr[i];
                    obj6[3] = "Y";
                    obj6[4] = "N";
                    finalQuery = pbdb.buildQuery(addTimeDimTableDetails, obj6);
                     batchSt1.addBatch(finalQuery);
                }
                //add grp dimension members
                String level[] = new String[Integer.parseInt(minTimeLevel)];
                String memberIds[] = new String[Integer.parseInt(minTimeLevel)];
                for (int i = 0; i < Integer.parseInt(minTimeLevel); i++) {
                    String addTimeDimMembers = resBundle.getString("addTimeDimMembers");
                    int seqaddTimeDimMembers = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_SEQ.nextval from dual");
                    memberIds[i] = String.valueOf(seqaddTimeDimMembers);
                    level[i] = String.valueOf(i + 1);
                    Object obj7[] = new Object[8];
                    obj7[0] = seqaddTimeDimMembers;
                    obj7[1] = TimeDimMemName[i];
                    obj7[2] = seqaddTimeDimDimension;
                    obj7[3] = seqaddTimeDimTables;
                    obj7[4] = "Y";
                    obj7[5] = seqaddTimeDimBussMater;
                    obj7[6] = TimeDimMemDenomQuery[i];
                    obj7[7] = TimeDimMemDesc[i];
                    finalQuery = pbdb.buildQuery(addTimeDimMembers, obj7);
                    batchSt1.addBatch(finalQuery);
                     //add member details
                    String bussdltId = "";
                    if (TimeDimMemName[i].equals("Year")) {
                        bussdltId = h.get("Year").toString();
                    } else if (TimeDimMemName[i].equals("Quarter")) {
                        bussdltId = h.get("Quarter").toString();
                    } else if (TimeDimMemName[i].equals("Month")) {
                        bussdltId = h.get("Month").toString();
                    } else if (TimeDimMemName[i].equals("Week")) {
                        bussdltId = h.get("Week").toString();
                    } else if (TimeDimMemName[i].equals("Day")) {
                        bussdltId = h.get("Day").toString();
                    }
                    String addTimeDimMemberDetails = resBundle.getString("addTimeDimMemberDetails");
                    int seqaddTimeDimMemberDetails = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                    Object obj8[] = new Object[5];
                    obj8[0] = seqaddTimeDimMemberDetails;
                    obj8[1] = seqaddTimeDimMembers;
                    obj8[2] = bussdltId;
                    obj8[3] = "KEY";
                    obj8[4] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimMemberDetails, obj8);
                    batchSt.addBatch(finalQuery);
                    String addTimeDimMemberDetails1 = resBundle.getString("addTimeDimMemberDetails");
                    int seqaddTimeDimMemberDetails1 = pbdb.getSequenceNumber("select PRG_GRP_DIM_MEMBER_DETAILS_SEQ.nextval from dual");
                    Object obj9[] = new Object[5];
                    obj9[0] = seqaddTimeDimMemberDetails1;
                    obj9[1] = seqaddTimeDimMembers;
                    obj9[2] = bussdltId;
                    obj9[3] = "VALUE";
                    obj9[4] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimMemberDetails1, obj9);
                    batchSt1.addBatch(finalQuery);

                }

                //add Time Dim Hierachies(relations)
                String addTimeDimRlt = resBundle.getString("addTimeDimRlt");
                int seqaddTimeDimRlt = pbdb.getSequenceNumber("select PRG_GRP_DIM_REL_SEQ.nextval from dual");
                Object obj10[] = new Object[5];
                obj10[0] = seqaddTimeDimRlt;
                obj10[1] = seqaddTimeDimDimension;
                obj10[2] = "Time";
                obj10[3] = "N";
                obj10[4] = "Time";
                finalQuery = pbdb.buildQuery(addTimeDimRlt, obj10);
                batchSt.addBatch(finalQuery);
                //add Time Dim Relation Details
                for (int i = 0; i < Integer.parseInt(minTimeLevel); i++) {
                    String addTimeDimRltDetails = resBundle.getString("addTimeDimRltDetails");
                    Object obj11[] = new Object[3];
                    obj11[0] = seqaddTimeDimRlt;
                    obj11[1] = memberIds[i];
                    obj11[2] = level[i];
                    finalQuery = pbdb.buildQuery(addTimeDimRltDetails, obj11);
                   batchSt1.addBatch(finalQuery);
                }
                //add Time Dim info
                String addTimeDimInfo = resBundle.getString("addTimeDimInfo");
                int seqaddTimeDimInfo = pbdb.getSequenceNumber("select PRG_TIME_DIM_INFO_SEQ.nextval from dual");
                if (minTimeLevel.equals("5")) {
                    Object obj12[] = new Object[8];
                    obj12[0] = seqaddTimeDimInfo;
                    obj12[1] = seqaddTimeDimBussMater;
                    obj12[2] = bussTableId;
                    //obj12[3] = bussColId;request.getParameter("timeDimKeyValueDay")
                    obj12[3] = prgr.getFieldValue(0, "BUSS_COLUMN_ID");
                    //BUSS_COLUMN_ID
                    obj12[4] = minTimeLevel;
                    obj12[5] = "Y";
                    obj12[6] = "N";
                    obj12[7] = h.get("Day").toString();
                    finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                    batchSt1.addBatch(finalQuery);
                } else if (minTimeLevel.equals("3")) {
                    Object obj12[] = new Object[8];
                    obj12[0] = seqaddTimeDimInfo;
                    obj12[1] = seqaddTimeDimBussMater;
                    obj12[2] = bussTableId;
                    obj12[3] = prgr.getFieldValue(0, "BUSS_COLUMN_ID");
                    obj12[4] = minTimeLevel;
                    obj12[5] = "Y";
                    obj12[6] = "N";
                    obj12[7] = rltmonthstrId;
                    finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                    batchSt1.addBatch(finalQuery);
                } else if (minTimeLevel.equals("4")) {
                    Object obj12[] = new Object[8];
                    obj12[0] = seqaddTimeDimInfo;
                    obj12[1] = seqaddTimeDimBussMater;
                    obj12[2] = bussTableId;
                    obj12[3] = prgr.getFieldValue(0, "BUSS_COLUMN_ID");
                    obj12[4] = minTimeLevel;
                    obj12[5] = "Y";
                    obj12[6] = "N";
                    obj12[7] = "0";
                    finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                    batchSt1.addBatch(finalQuery);
                } else if (minTimeLevel.equals("2")) {
                    Object obj12[] = new Object[8];
                    obj12[0] = seqaddTimeDimInfo;
                    obj12[1] = seqaddTimeDimBussMater;
                    obj12[2] = bussTableId;
                    obj12[3] = prgr.getFieldValue(0, "BUSS_COLUMN_ID");
                    obj12[4] = minTimeLevel;
                    obj12[5] = "Y";
                    obj12[6] = "N";
                    obj12[7] = "0";
                    finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                    batchSt1.addBatch(finalQuery);
                } else if (minTimeLevel.equals("1")) {
                    Object obj12[] = new Object[8];
                    obj12[0] = seqaddTimeDimInfo;
                    obj12[1] = seqaddTimeDimBussMater;
                    obj12[2] = bussTableId;
                    obj12[3] = prgr.getFieldValue(0, "BUSS_COLUMN_ID");
                    obj12[4] = minTimeLevel;
                    obj12[5] = "Y";
                    obj12[6] = "N";
                    obj12[7] = "0";
                    finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                    batchSt1.addBatch(finalQuery);
                }
                //add to rlt master
                String addTimeDimBussRltDetails = resBundle.getString("addTimeDimBussRltDetails");
                if (minTimeLevel.equals("5")) {
                    String timeDimKeyValueDay = prgr.getFieldValue(0, "BUSS_COLUMN_ID").toString();
                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueDay.split("~")[0];
                    obj13[4] = seqaddTimeDimBussMater;
                    obj13[5] = rltdateId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = "trunc(" + tabName + "." + prgr.getFieldValue(0, "COLUMN_NAME").toString() + ")= PROGEN_TIME.DDATE";
                    obj13[9] = "DDATE";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                    batchSt1.addBatch(finalQuery);
                } else if (minTimeLevel.equals("4")) {

                    String timeDimKeyValueWeekno = prgr.getFieldValue(0, "BUSS_COLUMN_ID").toString();
                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                    obj13[4] = seqaddTimeDimBussMater;
                    obj13[5] = rltweeknoId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                    obj13[9] = "CWEEK";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                    batchSt1.addBatch(finalQuery);

//                } else {
//                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
//                    String timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
//                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
//                    Object obj13[] = new Object[11];
//                    obj13[0] = seqaddTimeDimInfo;
//                    obj13[1] = seqaddTimeDimBussRltDetails;
//                    obj13[2] = bussTableId;
//                    obj13[3] = timeDimKeyValueWeekno.split("~")[0];
//                    obj13[4] = seqaddTimeDimBussMater;
//                    obj13[5] = rltweeknoId;
//                    obj13[6] = "inner";
//                    obj13[7] = "=";
//                    obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
//                    obj13[9] = "CWEEK";
//                    obj13[10] = "";
//                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
//                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////System.out.println.println("final addTimeDimRltdetails ---->" + finalQuery);
//                    batchSt1.addBatch(finalQuery);
//
//
//
//                    int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
//                    Object obj14[] = new Object[11];
//                    obj14[0] = seqaddTimeDimInfo;
//                    obj14[1] = seqaddTimeDimBussRltDetails1;
//                    obj14[2] = bussTableId;
//                    obj14[3] = timeDimKeyValueWeekyr.split("~")[0];
//                    obj14[4] = seqaddTimeDimBussMater;
//                    obj14[5] = rltweekyearId;
//                    obj14[6] = "inner";
//                    obj14[7] = "=";
//                    obj14[8] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
//                    obj14[9] = "CW_YEAR";
//                    obj14[10] = "";
//                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
//                    //   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////System.out.println.println("final addTimeDimRltdetails ---->" + finalQuery);
//                    batchSt1.addBatch(finalQuery);
//
//
//                }
//            } else if (minTimeLevel.equals("3")) {
//
//                String monthFormatyn = "S";
//
//                if (monthFormatyn.equals("F")) {
//                    String monthPre = request.getParameter("monthPre");
//                    String monthFormat = request.getParameter("monthFormat");
//                    String monthPost = request.getParameter("monthPost");
//                    //   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////System.out.println.println("month pre---------->"+monthPre);
//                    //    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////System.out.println.println("month post---------->"+monthPost);
//                    String totalMonthFormat = "";
//                    if (monthPre != null && monthPost != null) {
//                        totalMonthFormat = monthPre + "&" + monthFormat + "~" + monthPost;
//                    } else if (monthPre != null) {
//                        totalMonthFormat = monthPre + "&" + monthFormat;
//                    } else if (monthPost != null) {
//                        totalMonthFormat = monthFormat + "~" + monthPost;
//                    }
//
//                    String timeDimKeyValuemonth = request.getParameter("timeDimKeyValuemonth");
//                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
//                    Object obj13[] = new Object[11];
//                    obj13[0] = seqaddTimeDimInfo;
//                    obj13[1] = seqaddTimeDimBussRltDetails;
//                    obj13[2] = bussTableId;
//                    obj13[3] = timeDimKeyValuemonth.split("~")[0];
//                    obj13[4] = seqaddTimeDimBussMater;
//                    obj13[5] = rltmonthstrId;
//                    obj13[6] = "inner";
//                    obj13[7] = "=";
//                    obj13[8] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
//                    obj13[9] = "CM_ST_DATE";
//                    obj13[10] = totalMonthFormat;
//                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
//                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////System.out.println.println("final addTimeDimRltdetails ---->" + finalQuery);
//                    batchSt1.addBatch(finalQuery);
//
//
//                } else if (monthFormatyn.equals("M")) {
//                    String timeDimKeyValueMonthyr = request.getParameter("timeDimKeyValueMonthyr");
//                    String timeDimKeyValueMonthno = request.getParameter("timeDimKeyValueMonthno");
//                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
//                    Object obj13[] = new Object[11];
//                    obj13[0] = seqaddTimeDimInfo;
//                    obj13[1] = seqaddTimeDimBussRltDetails;
//                    obj13[2] = bussTableId;
//                    obj13[3] = timeDimKeyValueMonthno.split("~")[0];
//                    obj13[4] = seqaddTimeDimBussMater;
//                    //obj13[5] = rltmonthnoId;
//                    obj13[5] = h.get("Month").toString();
//                    obj13[6] = "inner";
//                    obj13[7] = "=";
//                    //obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CMONTH";
//                    //obj13[9]="CMONTH";h.get("Month").toString();
//                    obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
//                    obj13[9] = "CM_CUST_NAME";
//                    obj13[10] = "";
//                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
//                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////System.out.println.println("final addTimeDimRltdetails ---->" + finalQuery);
//                    batchSt1.addBatch(finalQuery);
//
//
//
//                    int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
//                    Object obj14[] = new Object[11];
//                    obj14[0] = seqaddTimeDimInfo;
//                    obj14[1] = seqaddTimeDimBussRltDetails1;
//                    obj14[2] = bussTableId;
//                    obj14[3] = timeDimKeyValueMonthyr.split("~")[0];
//                    obj14[4] = seqaddTimeDimBussMater;
//                    obj14[5] = rltmonthyearId;
//                    obj14[6] = "inner";
//                    obj14[7] = "=";
//                    obj14[8] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
//                    obj14[9] = "CM_YEAR";
//                    obj14[10] = "";
//                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
//                    //  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////System.out.println.println("final addTimeDimRltdetails ---->" + finalQuery);
//                    batchSt1.addBatch(finalQuery);
//
//                } else {
//
//                    String timeDimKeyValueMonthno = request.getParameter("timeDimKeyValuesingleMonthno");
//                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
//                    Object obj13[] = new Object[11];
//                    obj13[0] = seqaddTimeDimInfo;
//                    obj13[1] = seqaddTimeDimBussRltDetails;
//                    obj13[2] = bussTableId;
//                    obj13[3] = timeDimKeyValueMonthno.split("~")[0];
//                    obj13[4] = seqaddTimeDimBussMater;
//                    // obj13[5] = rltmonthnoId;
//                    obj13[5] = h.get("Month").toString();
//                    obj13[6] = "inner";
//                    obj13[7] = "=";
//                    obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
//                    //obj13[9]="CMONTH";
//                    obj13[9] = "CM_CUST_NAME";
//                    obj13[10] = "";
//                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
//                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////System.out.println.println("final addTimeDimRltdetails ---->" + finalQuery);
//                    batchSt1.addBatch(finalQuery);
//
//
//                }
                } else if (minTimeLevel.equals("1")) {

                    String timeDimKeyValueYear = prgr.getFieldValue(0, "BUSS_COLUMN_ID").toString();
                    int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj14[] = new Object[11];
                    obj14[0] = seqaddTimeDimInfo;
                    obj14[1] = seqaddTimeDimBussRltDetails1;
                    obj14[2] = bussTableId;
                    obj14[3] = timeDimKeyValueYear.split("~")[0];
                    obj14[4] = seqaddTimeDimBussMater;
                    obj14[5] = rltyearId;
                    obj14[6] = "inner";
                    obj14[7] = "=";
                    obj14[8] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                    obj14[9] = "CYEAR";
                    obj14[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                    batchSt1.addBatch(finalQuery);
                }
                // pbdb.executeMultiple(timeDimList);
                int c1[] = batchSt1.executeBatch();
                int c[] = batchSt.executeBatch();
              
            }
            check = true;
            return check;
        } catch (Exception e) {
            logger.error("Exception:", e);
            check = false;
            return check;
        }

    }
}
