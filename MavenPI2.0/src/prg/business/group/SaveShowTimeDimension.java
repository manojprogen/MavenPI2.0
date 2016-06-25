/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class SaveShowTimeDimension extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveShowTimeDimension.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbBussGrpResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbBussGrpResBundleMysql();
            } else {
                resourceBundle = new PbBussGrpResourceBundle();//PbBussGrpResourceBundle()
            }
        }

        return resourceBundle;
    }

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {


        try {
            PbDb pbdb = new prg.db.PbDb();

            String grpId = request.getParameter("grpId");
            String bussTableId = request.getParameter("bussTableId");
            String bussColId = request.getParameter("bussColId");
            String tabName = request.getParameter("tabName");
            String colName = request.getParameter("colName");
            String colType = request.getParameter("colType");
            String minTimeLevel = request.getParameter("preminlevel");
            String timebussTableId = request.getParameter("timebussTableId");

            String mainfactbussColId[] = request.getParameter("mainfactbussColId").split(",");
            String mainfactbussTableId[] = request.getParameter("mainfactbussTableId").split(",");
            String seqaddTimeDimInfoarr[] = request.getParameter("timeDimInfoId").split(",");
            int seqaddTimeDimInfo = 0;
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---first------seqaddTimeDimInfo---------"+bussColId);
            String alrdExists = "N";
            for (int k = 0; k < mainfactbussTableId.length; k++) {
                // //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--mainfactbussColId-"+mainfactbussColId[k]+"------"+bussColId+" && ---"+mainfactbussTableId[k]+"---"+bussTableId);
//                if (mainfactbussColId[k].equals(bussColId) && mainfactbussTableId[k].equals(bussTableId)) {
                if (mainfactbussTableId[k].equals(bussTableId)) {
                    seqaddTimeDimInfo = Integer.parseInt(seqaddTimeDimInfoarr[k]);
                    alrdExists = "Y";
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("equal---------"+alrdExists);
                    break;
                }
            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in save time dimension ");
            String finalQuery = "";
//            PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
            String getTimeDimColIds = getResourceBundle().getString("getTimeDimColIds");
            ArrayList batchSt = new ArrayList();
            String colnamestr = "";

            if (minTimeLevel.equals("5")) {
                colnamestr = " 'DDATE'";
            } else if (minTimeLevel.equals("4")) {
                colnamestr = "'CWEEK','CWYEAR'";
            } else if (minTimeLevel.equals("3")) {
                colnamestr = "'CMONTH','CM_YEAR','CM_ST_DATE','CM_END_DATE','CM_CUST_NAME'";
            } else if (minTimeLevel.equals("2")) {
                colnamestr = "'CQTR','CQ_YEAR'";
            } else if (minTimeLevel.equals("1")) {
                colnamestr = "'CYEAR'";
            }
            Object obj[] = new Object[2];
            obj[0] = timebussTableId;
            obj[1] = colnamestr;
            finalQuery = pbdb.buildQuery(getTimeDimColIds, obj);
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------finalQuery------colIds-----"+finalQuery);
            PbReturnObject pbroIds = pbdb.execSelectSQL(finalQuery);
            String timecolIds[] = pbroIds.getColumnNames();
            String rltdateId = "";
            String rltweeknoId = "";
            String rltweekyearId = "";
            String rltmonthnoId = "";
            String rltmonthcustnameId = "";
            String rltmonthyearId = "";
            String rltmonthstrId = "";
            String rltmonthendId = "";
            String rltqtrnoId = "";
            String rltqtryearId = "";
            String rltyearId = "";
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--------------");
            if (minTimeLevel.equals("5")) {
                rltdateId = String.valueOf(pbroIds.getFieldValueInt(0, timecolIds[0]));
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltdateId-------" + rltdateId);
            } else if (minTimeLevel.equals("4")) {
                for (int j = 0; j < pbroIds.getRowCount(); j++) {
                    if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CWEEK")) {
                        rltweeknoId = String.valueOf(pbroIds.getFieldValueInt(j, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltweeknoId-------" + rltweeknoId);
                    } else if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CWYEAR")) {
                        rltweekyearId = String.valueOf(pbroIds.getFieldValueInt(j, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltweekyearId-------" + rltweekyearId);
                    }
                }
            }
            if (minTimeLevel.equals("3")) {
                for (int j = 0; j < pbroIds.getRowCount(); j++) {
                    if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CMONTH")) {
                        rltmonthnoId = String.valueOf(pbroIds.getFieldValueInt(0, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthnoId-------" + rltmonthnoId);
                    } else if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CM_YEAR")) {
                        rltmonthyearId = String.valueOf(pbroIds.getFieldValueInt(1, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthyearId-------" + rltmonthyearId);
                    } else if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CM_ST_DATE")) {
                        rltmonthstrId = String.valueOf(pbroIds.getFieldValueInt(2, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthstrId-------" + rltmonthstrId);
                    } else if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CM_END_DATE")) {
                        rltmonthendId = String.valueOf(pbroIds.getFieldValueInt(3, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthendId-------" + rltmonthendId);rltmonthcustnameId
                    } else if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CM_CUST_NAME")) {
                        rltmonthcustnameId = String.valueOf(pbroIds.getFieldValueInt(3, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltmonthcustnameId-------" + rltmonthcustnameId);
                    }
                }
            }
            if (minTimeLevel.equals("2")) {
                for (int j = 0; j < pbroIds.getRowCount(); j++) {
                    if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CQTR")) {
                        rltqtrnoId = String.valueOf(pbroIds.getFieldValueInt(0, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltqtrnoId-------" + rltqtrnoId);
                    } else if (pbroIds.getFieldValueString(j, timecolIds[2]).equalsIgnoreCase("CQ_YEAR")) {
                        rltqtryearId = String.valueOf(pbroIds.getFieldValueInt(1, timecolIds[0]));
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltqtryearId-------" + rltqtryearId);
                    }
                }
            }
            if (minTimeLevel.equals("1")) {
                rltyearId = String.valueOf(pbroIds.getFieldValueInt(0, timecolIds[0]));
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("-------rltyearId-------" + rltyearId);
            }




            //for adding into grp_buss_table
            //add Time Dim info
            if (alrdExists.equalsIgnoreCase("N")) {
                String addTimeDimInfo = resourceBundle.getString("addTimeDimInfo1");
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    PbReturnObject returnObject = pbdb.execSelectSQL("select ident_current('PRG_TIME_DIM_INFO')");
                    seqaddTimeDimInfo = returnObject.getFieldValueInt(0, 0) + 1;
                    if (minTimeLevel.equals("5")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        obj12[2] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = rltdateId;
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                        // //////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);

                    } else if (minTimeLevel.equals("3")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        if (request.getParameter("monthJoinType").equals("F")) {
                            obj12[2] = request.getParameter("timeDimKeyValuemonth").split("~")[0];
                        } else if (request.getParameter("monthJoinType").equals("M")) {
                            obj12[2] = bussColId;
                        } else {
                            obj12[2] = request.getParameter("timeDimKeyValuesingleMonthno").split("~")[0];
                        }
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = rltmonthstrId;
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        /////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);

                    } else if (minTimeLevel.equals("4")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        if (request.getParameter("weekJoinType").equals("S")) {
                            obj12[2] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                        } else {
                            obj12[2] = bussColId;
                        }
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    } else if (minTimeLevel.equals("2")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        if (request.getParameter("quarterJoinType").equals("S")) {
                            obj12[2] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                        } else {
                            obj12[2] = bussColId;
                        }
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        // //////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    } else if (minTimeLevel.equals("1")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        obj12[2] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    }

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    PbReturnObject returnObject = pbdb.execSelectSQL("select Last_Insert_Id(time_dim_id) from PRG_TIME_DIM_INFO order by 1 desc limit 1");
                    seqaddTimeDimInfo = returnObject.getFieldValueInt(0, 0) + 1;
                    if (minTimeLevel.equals("5")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        obj12[2] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = rltdateId;
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                        // //////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);

                    } else if (minTimeLevel.equals("3")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        if (request.getParameter("monthJoinType").equals("F")) {
                            obj12[2] = request.getParameter("timeDimKeyValuemonth").split("~")[0];
                        } else if (request.getParameter("monthJoinType").equals("M")) {
                            obj12[2] = bussColId;
                        } else {
                            obj12[2] = request.getParameter("timeDimKeyValuesingleMonthno").split("~")[0];
                        }
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = rltmonthstrId;
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        /////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);

                    } else if (minTimeLevel.equals("4")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        if (request.getParameter("weekJoinType").equals("S")) {
                            obj12[2] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                        } else {
                            obj12[2] = bussColId;
                        }
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    } else if (minTimeLevel.equals("2")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        if (request.getParameter("quarterJoinType").equals("S")) {
                            obj12[2] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                        } else {
                            obj12[2] = bussColId;
                        }
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        // //////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    } else if (minTimeLevel.equals("1")) {
                        Object obj12[] = new Object[7];
//                        obj12[0] = seqaddTimeDimInfo;
                        obj12[0] = timebussTableId;
                        obj12[1] = bussTableId;
                        obj12[2] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                        obj12[3] = minTimeLevel;
                        obj12[4] = "Y";
                        obj12[5] = "N";
                        obj12[6] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    }

                } else {
                    seqaddTimeDimInfo = pbdb.getSequenceNumber("select PRG_TIME_DIM_INFO_SEQ.nextval from dual");//PRG_TIME_DIM_INFO

                    if (minTimeLevel.equals("5")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = timebussTableId;
                        obj12[2] = bussTableId;
                        obj12[3] = request.getParameter("timeDimKeyValueDay").split("~")[0];
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = rltdateId;
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);
                        // //////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);

                    } else if (minTimeLevel.equals("3")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = timebussTableId;
                        obj12[2] = bussTableId;
                        if (request.getParameter("monthJoinType").equals("F")) {
                            obj12[3] = request.getParameter("timeDimKeyValuemonth").split("~")[0];
                        } else if (request.getParameter("monthJoinType").equals("M")) {
                            obj12[3] = bussColId;
                        } else {
                            obj12[3] = request.getParameter("timeDimKeyValuesingleMonthno").split("~")[0];
                        }
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = rltmonthstrId;
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        /////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);

                    } else if (minTimeLevel.equals("4")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = timebussTableId;
                        obj12[2] = bussTableId;
                        if (request.getParameter("weekJoinType").equals("S")) {
                            obj12[3] = request.getParameter("timeDimKeyValuesingleWeekno").split("~")[0];
                        } else {
                            obj12[3] = bussColId;
                        }
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    } else if (minTimeLevel.equals("2")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = timebussTableId;
                        obj12[2] = bussTableId;
                        if (request.getParameter("quarterJoinType").equals("S")) {
                            obj12[3] = request.getParameter("timeDimKeyValuesingleQuaterno").split("~")[0];
                        } else {
                            obj12[3] = bussColId;
                        }
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        // //////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    } else if (minTimeLevel.equals("1")) {
                        Object obj12[] = new Object[8];
                        obj12[0] = seqaddTimeDimInfo;
                        obj12[1] = timebussTableId;
                        obj12[2] = bussTableId;
                        obj12[3] = request.getParameter("timeDimKeyValueYear").split("~")[0];
                        obj12[4] = minTimeLevel;
                        obj12[5] = "Y";
                        obj12[6] = "N";
                        obj12[7] = "0";
                        finalQuery = pbdb.buildQuery(addTimeDimInfo, obj12);

                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimInfo ---->" + finalQuery);
                        batchSt.add(finalQuery);
                    }
                }




            }



            //add to rlt master

            if (alrdExists.equalsIgnoreCase("Y")) {
                String deleteAlreadyExistedTimeDimColDetails = resourceBundle.getString("deleteAlreadyExistedTimeDimColDetails");
                Object delObj[] = new Object[1];
                delObj[0] = seqaddTimeDimInfo;
                finalQuery = pbdb.buildQuery(deleteAlreadyExistedTimeDimColDetails, delObj);
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--final delete Query----"+finalQuery);
                pbdb.execModifySQL(finalQuery);
            }
            String addTimeDimBussRltDetails = resourceBundle.getString("addTimeDimBussRltDetails1");
            if (minTimeLevel.equals("5")) {
                String timeDimKeyValueDay = request.getParameter("timeDimKeyValueDay");
                int seqaddTimeDimBussRltDetails = 0;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    PbReturnObject pbReturnObject = pbdb.execSelectSQL("select ident_current('PRG_TIME_DIM_INFO_RLT_DETAILS')");
                    seqaddTimeDimBussRltDetails = pbReturnObject.getFieldValueInt(0, 0);
                    Object obj13[] = new Object[10];
                    obj13[0] = seqaddTimeDimInfo;
//                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[1] = bussTableId;
                    obj13[2] = timeDimKeyValueDay.split("~")[0];
                    obj13[3] = timebussTableId;
                    obj13[4] = rltdateId;
                    obj13[5] = "inner";
                    obj13[6] = "=";
                    obj13[7] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                    obj13[8] = "DDATE";

                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    PbReturnObject pbReturnObject = pbdb.execSelectSQL("select ident_current('PRG_TIME_DIM_INFO_RLT_DETAILS')");
//                    seqaddTimeDimBussRltDetails = pbReturnObject.getFieldValueInt(0, 0);
                    Object obj13[] = new Object[10];
                    obj13[0] = seqaddTimeDimInfo;
//                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[1] = bussTableId;
                    obj13[2] = timeDimKeyValueDay.split("~")[0];
                    obj13[3] = timebussTableId;
                    obj13[4] = rltdateId;
                    obj13[5] = "inner";
                    obj13[6] = "=";
                    obj13[7] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                    obj13[8] = "DDATE";

                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                } else {
                    seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueDay.split("~")[0];
                    obj13[4] = timebussTableId;
                    obj13[5] = rltdateId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = "trunc(" + tabName + "." + timeDimKeyValueDay.split("~")[1] + ")= PROGEN_TIME.DDATE";
                    obj13[9] = "DDATE";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                }

                // ////.println("finalQuery\t"+finalQuery);

                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                batchSt.add(finalQuery);



            } else if (minTimeLevel.equals("4")) {


                if (request.getParameter("weekJoinType").equals("S")) {

                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValuesingleWeekno");
                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                    obj13[4] = timebussTableId;
                    obj13[5] = rltweeknoId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                    obj13[9] = "CWEEK";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);




                } else {
                    String timeDimKeyValueWeekno = request.getParameter("timeDimKeyValueWeekno");
                    String timeDimKeyValueWeekyr = request.getParameter("timeDimKeyValueWeekyr");
                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueWeekno.split("~")[0];
                    obj13[4] = timebussTableId;
                    obj13[5] = rltweeknoId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = tabName + "." + timeDimKeyValueWeekno.split("~")[1] + "= PROGEN_TIME.CWEEK";
                    obj13[9] = "CWEEK";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);



                    int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj14[] = new Object[11];
                    obj14[0] = seqaddTimeDimInfo;
                    obj14[1] = seqaddTimeDimBussRltDetails1;
                    obj14[2] = bussTableId;
                    obj14[3] = timeDimKeyValueWeekyr.split("~")[0];
                    obj14[4] = timebussTableId;
                    obj14[5] = rltweekyearId;
                    obj14[6] = "inner";
                    obj14[7] = "=";
                    obj14[8] = tabName + "." + timeDimKeyValueWeekyr.split("~")[1] + "= PROGEN_TIME.CWYEAR";
                    obj14[9] = "CW_YEAR";
                    obj14[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);


                }
            } else if (minTimeLevel.equals("3")) {

                String monthFormatyn = request.getParameter("monthJoinType");

                if (monthFormatyn.equals("F")) {
                    String monthPre = request.getParameter("monthPre");
                    String monthFormat = request.getParameter("monthFormat");
                    String monthPost = request.getParameter("monthPost");
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("month pre---------->" + monthPre);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("month post---------->" + monthPost);
                    String totalMonthFormat = "";
                    if (monthPre != null && monthPost != null) {
                        totalMonthFormat = monthPre + "&" + monthPost + "&" + monthFormat;
                    } else if (monthPre != null) {
                        totalMonthFormat = monthPre + "&" + monthFormat;
                    } else if (monthPost != null) {
                        totalMonthFormat = monthPost + "&" + monthFormat;
                    }

                    String timeDimKeyValuemonth = request.getParameter("timeDimKeyValuemonth");
                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValuemonth.split("~")[0];
                    obj13[4] = timebussTableId;
                    obj13[5] = rltmonthstrId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = tabName + "." + timeDimKeyValuemonth.split("~")[1] + "= TO_CHAR(PROGEN_TIME.CM_ST_DATE," + monthFormat + ")";
                    obj13[9] = "CM_ST_DATE";
                    obj13[10] = totalMonthFormat;
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);


                } else if (monthFormatyn.equals("M")) {
                    String timeDimKeyValueMonthyr = request.getParameter("timeDimKeyValueMonthyr");
                    String timeDimKeyValueMonthno = request.getParameter("timeDimKeyValueMonthno");
                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueMonthno.split("~")[0];
                    obj13[4] = timebussTableId;
                    obj13[5] = rltmonthcustnameId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                    obj13[9] = "CM_CUST_NAME";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);



                    int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj14[] = new Object[11];
                    obj14[0] = seqaddTimeDimInfo;
                    obj14[1] = seqaddTimeDimBussRltDetails1;
                    obj14[2] = bussTableId;
                    obj14[3] = timeDimKeyValueMonthyr.split("~")[0];
                    obj14[4] = timebussTableId;
                    obj14[5] = rltmonthyearId;
                    obj14[6] = "inner";
                    obj14[7] = "=";
                    obj14[8] = tabName + "." + timeDimKeyValueMonthyr.split("~")[1] + "= PROGEN_TIME.CM_YEAR";
                    obj14[9] = "CM_YEAR";
                    obj14[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);

                } else {

                    String timeDimKeyValueMonthno = request.getParameter("timeDimKeyValuesingleMonthno");
                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueMonthno.split("~")[0];
                    obj13[4] = timebussTableId;
                    obj13[5] = rltmonthcustnameId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = tabName + "." + timeDimKeyValueMonthno.split("~")[1] + "= PROGEN_TIME.CM_CUST_NAME";
                    obj13[9] = "CM_CUST_NAME";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);


                }
            } else if (minTimeLevel.equals("2")) {
                //for mintime level Quarter
                if (request.getParameter("quarterJoinType").equals("S")) {
                    String timeDimKeyValueQuaterno = request.getParameter("timeDimKeyValuesingleQuaterno");
                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueQuaterno.split("~")[0];
                    obj13[4] = timebussTableId;
                    obj13[5] = rltqtrnoId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                    obj13[9] = "CQTR";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);


                } else {
                    String timeDimKeyValueQuaterno = request.getParameter("timeDimKeyValueQuaterno");

                    String timeDimKeyValueQuateryr = request.getParameter("timeDimKeyValueQuateryr");


                    int seqaddTimeDimBussRltDetails = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                    Object obj13[] = new Object[11];
                    obj13[0] = seqaddTimeDimInfo;
                    obj13[1] = seqaddTimeDimBussRltDetails;
                    obj13[2] = bussTableId;
                    obj13[3] = timeDimKeyValueQuaterno.split("~")[0];
                    obj13[4] = timebussTableId;
                    obj13[5] = rltqtrnoId;
                    obj13[6] = "inner";
                    obj13[7] = "=";
                    obj13[8] = tabName + "." + timeDimKeyValueQuaterno.split("~")[1] + "= PROGEN_TIME.CQTR";
                    obj13[9] = "CQTR";
                    obj13[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj13);

                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);



                    int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");

                    Object obj14[] = new Object[11];
                    obj14[0] = seqaddTimeDimInfo;
                    obj14[1] = seqaddTimeDimBussRltDetails1;
                    obj14[2] = bussTableId;
                    obj14[3] = timeDimKeyValueQuateryr.split("~")[0];
                    obj14[4] = timebussTableId;
                    obj14[5] = rltqtryearId;
                    obj14[6] = "inner";
                    obj14[7] = "=";
                    obj14[8] = tabName + "." + timeDimKeyValueQuateryr.split("~")[1] + "= PROGEN_TIME.CQ_YEAR";
                    obj14[9] = "CQ_YEAR";
                    obj14[10] = "";
                    finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);

                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                    batchSt.add(finalQuery);


                }
            } else if (minTimeLevel.equals("1")) {

                String timeDimKeyValueYear = request.getParameter("timeDimKeyValueYear");
                int seqaddTimeDimBussRltDetails1 = pbdb.getSequenceNumber("select PRG_TIME_INFO_RLT_DETAILS_SEQ.nextval from dual");
                Object obj14[] = new Object[11];
                obj14[0] = seqaddTimeDimInfo;
                obj14[1] = seqaddTimeDimBussRltDetails1;
                obj14[2] = bussTableId;
                obj14[3] = timeDimKeyValueYear.split("~")[0];
                obj14[4] = timebussTableId;
                obj14[5] = rltyearId;
                obj14[6] = "inner";
                obj14[7] = "=";
                obj14[8] = tabName + "." + timeDimKeyValueYear.split("~")[1] + "= PROGEN_TIME.CYEAR";
                obj14[9] = "CYEAR";
                obj14[10] = "";
                finalQuery = pbdb.buildQuery(addTimeDimBussRltDetails, obj14);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("final addTimeDimRltdetails ---->" + finalQuery);
                batchSt.add(finalQuery);

            }

            // for(int i=0;i<batchSt.size();i++){
            //     //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--start-  --"+batchSt.get(i));
            //  }

            pbdb.executeMultiple(batchSt);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--start---");
            // new Object().wait(1);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("--end--");

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        request.setAttribute("forward", "success");



        return mapping.findForward(SUCCESS);
    }
}
