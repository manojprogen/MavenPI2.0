package prg.target.sheetschceduler;

import com.progen.targetview.db.PbTargetDAO;
import com.progen.targetview.db.ReadXLSheet;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.targetparam.qdparams.PbTargetParamTable;
import prg.targetparam.qdparams.PbTargetValuesParam;
import utils.db.ProgenConnection;

public class ExcelSheetJob implements Job {

    public static Logger logger = Logger.getLogger(ExcelSheetJob.class);

    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            String targetId = jec.getJobDetail().getJobDataMap().getString("targetId");
            String excelSheetName = jec.getJobDetail().getJobDataMap().getString("excelSheetName");
            String UserId = jec.getJobDetail().getJobDataMap().getString("userId");
            String FileName = (String) jec.getJobDetail().getJobDataMap().get("FileName");
            // Process the FormFile
            ////////////////////////////////////////////////////////////////////.println(" FileName in schedular for exc--------------------- "+FileName);
            File tempFile = new File(FileName);

            ReadXLSheet rxl = new ReadXLSheet();
            HashMap excelvalues = new HashMap();
            //////////////////////////////////////////////////////////////////////.println(" before sjkdsjkdj "+newfileName);
            HashMap all = (HashMap) rxl.init(tempFile.toString());
            excelvalues = (HashMap) all.get("excelvalues");
            ////////////////////////////////////////////////////////////////////.println(" excelvalues after ---in excell Sheet Job----- "+excelvalues);
            // request.setAttribute("fileName", oldfileName);


            ////////////////


            String filName = excelSheetName;
            ////////////////////////////////////////////////////////////////////.println(" excelvalues in mm " + excelvalues);
            PbDb pbdb = new PbDb();
            ArrayList al = new ArrayList();
            String insertStatusQ = "insert into excelSheet_uplaod(UPLOADNUMBER,EXCEL_SHEET_NAME,UPLOAD_TIME,STATUS,USERID) values(excelSheet_uplaod_ID_SEQ.nextval,'" + filName + "',sysdate,'Uploaded'," + UserId + ")";
            ////////////////////////////////////////////////////////////////////.println(" insertStatusQ " + insertStatusQ);
            al.add(insertStatusQ);
            try {
                pbdb.executeMultiple(al);
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
            //ArrayList hskeylist=(ArrayList) all.get("hskeylist");
            // ////////////////////////////////////////////////////////////////////.println(excelvalues+" in excel read "+hskeylist);


            String minTimeLevel = "";

            PbTargetDAO tDao = new PbTargetDAO();
            String targetBasis = "Absolute";
            String parentDataQ = "";
            String parentDataQ2 = "";
            String dataQuery = "";
            String dataQuery2 = "";

            PbReturnObject fileDet = tDao.getExcelFileDetail(filName);
            String colEdVals = "";
            String rowEdVals = "";
            String primA = "";
            String secA = "";
            String sRange = "";
            String nonAll = "";
            String colViewByElement = "";
            HashMap originalResult = new HashMap();
            ArrayList columnEdgeValuesA = new ArrayList();
            ArrayList rowEdgeValuesA = new ArrayList();
            String secAnalyze = secA;
            String primaryAnalyze = "";



            if (fileDet.getRowCount() > 0) {
                targetId = fileDet.getFieldValueString(0, "TARGET_ID");
                minTimeLevel = fileDet.getFieldValueString(0, "PERIODTYPE");
                colEdVals = fileDet.getFieldValueClobString(0, "COLUMNVIEWBYVALUES");
                rowEdVals = fileDet.getFieldValueClobString(0, "ROWVIEWBYVALUES");
                primA = fileDet.getFieldValueString(0, "PRIMARYVIEWBY");
                secA = fileDet.getFieldValueString(0, "SECVIEWBY");
                sRange = fileDet.getFieldValueString(0, "TIMEVAL");
                nonAll = fileDet.getFieldValueClobString(0, "NONALLVALUE");
                targetBasis = fileDet.getFieldValueString(0, "TARGET_BASIS");
                parentDataQ2 = fileDet.getFieldValueClobString(0, "PARENTDATAQUERY2");
                parentDataQ = fileDet.getFieldValueClobString(0, "PARENTDATAQUERY");
                dataQuery2 = fileDet.getFieldValueClobString(0, "DATAQUERY2");
                dataQuery = fileDet.getFieldValueClobString(0, "DATAQUERY");
                if (dataQuery2 == null) {
                    dataQuery2 = "No";
                }
                if (dataQuery == null) {
                    dataQuery = "No";
                }
                if (dataQuery.equalsIgnoreCase("No")) {
                    dataQuery = "";
                }
                if (dataQuery2.equalsIgnoreCase("No")) {
                    dataQuery2 = "";
                }
                primaryAnalyze = primA;
            }
            String colEdValsArr[] = colEdVals.split("~");
            String rowEdValsArr[] = rowEdVals.split("~");
            for (int y = 0; y < colEdValsArr.length; y++) {
                columnEdgeValuesA.add(colEdValsArr[y]);
            }

            for (int y = 0; y < rowEdValsArr.length; y++) {
                rowEdgeValuesA.add(rowEdValsArr[y]);
            }

            if (parentDataQ2.equalsIgnoreCase("No")) {
                parentDataQ2 = "";
            }
            if (parentDataQ.equalsIgnoreCase("No")) {
                parentDataQ = "";
            }

            ResultSet rsParent1 = null;
            ResultSet rsParent2 = null;
            PreparedStatement psParent = null;
            PreparedStatement psParent2 = null;
            Connection con = ProgenConnection.getInstance().getConnection();
            if (parentDataQ != null) {
                if (!parentDataQ.equalsIgnoreCase("") || !parentDataQ.equalsIgnoreCase("No")) {
                    psParent = con.prepareStatement(parentDataQ);
                    rsParent1 = psParent.executeQuery();
                }
            }
            if (parentDataQ2 != null || !parentDataQ.equalsIgnoreCase("No")) {
                if (!parentDataQ2.equalsIgnoreCase("")) {
                    psParent2 = con.prepareStatement(parentDataQ2);
                    rsParent2 = psParent2.executeQuery();
                }
            }
            PbReturnObject parentDataObj = new PbReturnObject();
            PbReturnObject parentDataObj2 = new PbReturnObject();
            if (rsParent1 != null) {
                parentDataObj = new PbReturnObject(rsParent1);
            }

            if (psParent2 != null) {
                parentDataObj2 = new PbReturnObject(rsParent2);
            }

            HashMap primH2 = new HashMap();
            String oldPrimV = "";
            String newPrimV = "";
            String parentEle = "-1";
            GetTargetDetails gDetails = new GetTargetDetails();
            //if(!primA.equalsIgnoreCase("0"))
            //    primA=gDetails.getParentEleId(primA,targetId);

            HashMap RestrictingTotal = new HashMap();
            HashMap allV2 = new HashMap();
            HashMap parentTotals = new HashMap();

            if (secA.equalsIgnoreCase("Time")) {
                if (!parentEle.equalsIgnoreCase("-1")) {
                    for (int i = 0; i < parentDataObj.getRowCount(); i++) {
                        oldPrimV = parentDataObj.getFieldValueString(i, 0);
                        if (newPrimV.equalsIgnoreCase("")) {
                            newPrimV = oldPrimV;
                        }
                        oldPrimV = parentDataObj.getFieldValueString(i, 0);
                        String dat = parentDataObj.getFieldValueString(i, 2);
                        long mValue = parentDataObj.getFieldValueInt(i, 1);
                        long old = 0;
                        if (primH2.containsKey(dat)) {
                            old = ((Long) primH2.get(dat)).longValue();
                        }
                        mValue = mValue + old;
                        primH2.put(dat, new Long(mValue));
                    }
                } else {
                    for (int i = 0; i < parentDataObj.getRowCount(); i++) {
                        String dat = parentDataObj.getFieldValueString(i, 1);
                        long mValue = Long.parseLong(parentDataObj.getFieldValueString(i, 0));
                        long old = 0;
                        if (primH2.containsKey(dat)) {
                            old = ((Long) primH2.get(dat)).longValue();
                        }
                        mValue = mValue + old;
                        primH2.put(dat, new Long(mValue));
                    }
                }
                // to build the restricting total hashmap

                for (int k = 0; k < columnEdgeValuesA.size(); k++) {
                    long rTotal = 0;
                    if (primH2.containsKey(columnEdgeValuesA.get(k).toString())) {
                        rTotal = ((Long) primH2.get(columnEdgeValuesA.get(k).toString())).longValue();
                    }
                    RestrictingTotal.put(columnEdgeValuesA.get(k).toString(), new Long(rTotal));
                }
                ////////////////////////.println(RestrictingTotal + " RestrictingTotal allV2,.,.,.in sec view time,. " + allV2);
            } else if (!secA.equalsIgnoreCase("Time")) {
                PreparedStatement psNew = null;//con.prepareStatement(dataQuery);
                ResultSet rsNew = null;

                if (parentDataQ != null) {
                    if (!parentDataQ.equalsIgnoreCase("") || !parentDataQ.equalsIgnoreCase("No")) {
                        psNew = con.prepareStatement(parentDataQ);
                        rsNew = psNew.executeQuery();
                    }
                }
                PbReturnObject allD = new PbReturnObject(rsNew);
                rsNew = null;
                if (!parentDataQ2.equalsIgnoreCase("") || !parentDataQ2.equalsIgnoreCase("No")) {
                    psNew = con.prepareStatement(parentDataQ2);
                    rsNew = psNew.executeQuery();
                }
                PbReturnObject allD2 = new PbReturnObject(rsNew);

                // loop throuth data and make hashMap for row and column headers
                String oldPv = "";
                String newPv = "";
                HashMap primH = new HashMap();
                for (int n = 0; n < allD.getRowCount(); n++) {
                    oldPv = allD.getFieldValueString(n, 0);
                    if (newPv.equalsIgnoreCase("")) {
                        newPv = oldPv;
                    } else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
                        allV2.put(newPv, primH);
                        primH = new HashMap();
                        newPv = oldPv;
                    }
                    String dat = allD.getFieldValueString(n, 2);
                    Long mValue = Long.parseLong(allD.getFieldValueString(n, 1));
                    primH.put(dat, new Long(mValue));
                    if (n == (allD.getRowCount() - 1)) {
                        allV2.put(oldPv, primH);
                    }
                }



                for (int t = 0; t < allD.getRowCount(); t++) {
                    parentTotals.put(allD.getFieldValueString(t, 0), new Long(Long.parseLong(allD.getFieldValueString(t, 1))));
                }
                for (int t = 0; t < allD2.getRowCount(); t++) {
                    parentTotals.put(allD2.getFieldValueString(t, 0), new Long(Long.parseLong(allD2.getFieldValueString(t, 1))));
                }
            }
            String startRange = sRange;
            colViewByElement = secA;
            String viewByElement = primA;
            String periodType = minTimeLevel;
            String overallT = "";
            String targetQ = "select target_table_id,pgbt.buss_table_name,ptm.measure_name from prg_target_master ptm,"
                    + " prg_grp_buss_table pgbt where prg_measure_id in (select measure_id from target_master WHERE "
                    + " target_master.target_id=" + targetId + ") and pgbt.buss_table_id = ptm.target_table_id";
            PbReturnObject pbro = pbdb.execSelectSQL(targetQ);
            String measureName = pbro.getFieldValueString(0, "MEASURE_NAME");
            String targetTable = pbro.getFieldValueString(0, "BUSS_TABLE_NAME");
            String viewName = "";
            if (primA.equalsIgnoreCase("0")) {
                viewName = "OverAll";
            }
            String allParametrsFilterClause = "";
            HashMap elementCols = new HashMap();
            PbReturnObject pbro2 = new PbReturnObject();
            String getBussColNames = "select DISTINCT element_id, buss_col_name from prg_user_all_info_details where folder_id "
                    + " in(select bus_folder_id from target_measure_folder where bus_group_id=(select business_group from prg_target_master"
                    + " where prg_measure_id in(select measure_id from target_master WHERE target_master.target_id=&)))";

            Object tarOb[] = new Object[1];
            tarOb[0] = targetId;
            String fingetBussColNames = pbdb.buildQuery(getBussColNames, tarOb);
            ////////////////////////////////////////////////////////////////////.println(" fingetBussColNames " + fingetBussColNames);
            pbro2 = pbdb.execSelectSQL(fingetBussColNames);
            for (int i = 0; i < pbro2.getRowCount(); i++) {
                elementCols.put(pbro2.getFieldValueString(i, "ELEMENT_ID"), pbro2.getFieldValueString(i, "BUSS_COL_NAME"));
                // viewByNames.put(pbro.getFieldValueString(i,"ELEMENT_ID"),pbro.getFieldValueString(i,"PARAM_DISP_NAME"));
            }
            ////////////////////////////////////////////////////////////////////.println(" elementCols " + elementCols);
            HashMap viewByNames = new HashMap();
            String getDispNames = "select distinct element_id,param_disp_name from prg_target_param_details where target_id=&";
            String fingetDispNames = pbdb.buildQuery(getDispNames, tarOb);
            ////////////////////////////////////////////////////////////////////.println(" fingetDispNames " + fingetDispNames);
            PbReturnObject pbroDispNames = pbdb.execSelectSQL(fingetDispNames);
            for (int m = 0; m < pbroDispNames.getRowCount(); m++) {
                viewByNames.put(pbroDispNames.getFieldValueString(m, "ELEMENT_ID"), pbroDispNames.getFieldValueString(m, "PARAM_DISP_NAME"));
            }
            ////////////////////////////////////////////////////////////////////.println(" viewByNames.//./ " + viewByNames + " nonAll " + nonAll);
            HashMap nonAllIds = new HashMap();
            String viewByName = "";
            String nonAllArr[] = nonAll.split("~");
            for (int m = 0; m < nonAllArr.length; m++) {
                String val[] = nonAllArr[m].split("=");
                nonAllIds.put(val[0], val[1]);
            }
            ////////////////////////////////////////////////////////////////////.println(" nonAllIds " + nonAllIds);
            for (int l = 0; l < nonAllArr.length; l++) {
                String val[] = nonAllArr[l].split("=");
                String a = (String) nonAllIds.get(val[0]);
                ////////////////////////////////////////////////////////////////////.println(nonAllArr[l] + " a " + a);
                if (!a.equalsIgnoreCase("-1")) {
                    if (!a.equalsIgnoreCase("All")) {
                        allParametrsFilterClause = allParametrsFilterClause + " and " + elementCols.get(val[0]) + "='" + a + "'";
                    }
                }
            }
            ////////////////////////////////////////////////////////////////////.println(" allParametrsFilterClause " + allParametrsFilterClause + " secA " + secA);
            if (viewByNames.containsKey(viewByElement)) {
                overallT = (String) elementCols.get(viewByElement);
                viewName = (String) viewByNames.get(viewByElement);
            }

            String secCol = "";
            String secName = "";
            if (viewByNames.containsKey(secA)) {
                secCol = (String) elementCols.get(secA);
                secName = (String) viewByNames.get(secA);
            }

            ////////////////////////////////////////////////////////////////////.println(" viewByElement in excel " + viewByElement + " secAnalyze " + secAnalyze);
            if (!viewByElement.equalsIgnoreCase("0")) {
                if (secAnalyze.equalsIgnoreCase("Time")) {
                    ////////////////////////////////////////////////////////////////////.println(" in if 1 --");
                    if (periodType.equalsIgnoreCase("Day")) {
                        // dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and t_date is not null group by " + overallT + ",to_char(t_date,'dd-mm-yy') order by " + overallT + ",to_char(t_date,'dd-mm-yy')";
                    } else if (periodType.equalsIgnoreCase("Month")) {
                        // dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and t_month is not null group by " + overallT + ",t_month order by " + overallT + ",t_month";
                    } //dataQuery ="select "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),t_month from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewByNames.get(viewByElement.trim()).toString()+"'"+allParametrsFilterClause+" group by "+elementCols.get(viewByElement.trim()).toString()+",t_month order by "+elementCols.get(viewByElement.trim()).toString()+",t_month";
                    else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("Qtr")) {
                        // dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id='" + targetId + "' and t_qtr is not null " + allParametrsFilterClause + " " +
                        //      " and viewby='" + viewName + "' group by " + overallT + ", t_qtr order by " + overallT + ",t_qtr";
                        // dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_year='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                    } else if (periodType.equalsIgnoreCase("Year")) {
                        dataQuery = "select " + overallT + ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewName + "' " + allParametrsFilterClause + " and t_year is not null group by " + overallT + ",t_year order by " + overallT + ",t_year";
                    }
                } else {
                    if (periodType.equalsIgnoreCase("Year")) {
                        //dataQuery ="select "+overallT+",sum("+measureName.trim()+"),t_year from "+targetTable.trim()+" where target_id='"+targetId+"' and viewby='"+viewName+"' and secviewby='' "+ allParametrsFilterClause+" and t_year is not null and t_year='"+startRange+"' group by "+overallT+",t_year order by "+overallT+",t_year";
                        //dataQuery = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(colViewByElement.trim()).toString() + "'  and t_year='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                        //dataQuery2 = "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" + measureName.trim() + ")," + elementCols.get(colViewByElement.trim()).toString() + " from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='" + viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" + viewByNames.get(viewByElement.trim()).toString() + "'  and t_year='" + startRange + "' " + allParametrsFilterClause + " group by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString() + " order by " + elementCols.get(viewByElement.trim()).toString() + "," + elementCols.get(colViewByElement.trim()).toString();
                    }

                }
            } else {
                if (viewByElement.equalsIgnoreCase("0")) {
                    ////////////////////////////////////////////////////////////////////.println(periodType + " in if  overall ");
                    ArrayList paramVals = new ArrayList();
                    paramVals.add("OverAll Target");
                    paramVals.add("OverAll");
                }

                if (periodType.equalsIgnoreCase("Day")) {
                    dataQuery = "select sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll' and t_date is not null  group by to_char(t_date,'dd-mm-yy') order by to_char(t_date,'dd-mm-yy')";
                } else if (periodType.equalsIgnoreCase("Month")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll' and t_month is not null  group by t_month order by t_month";
                } else if (periodType.equalsIgnoreCase("Quarter") || periodType.equalsIgnoreCase("QTR")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll' and t_qtr is not null group by t_qtr order by t_qtr";
                } else if (periodType.equalsIgnoreCase("Year")) {
                    dataQuery = "select sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + " where target_id='" + targetId + "' and viewby='OverAll' and t_year is not null group by t_year order by t_year";
                }

            }
            // Connection con = ProgenConnection.getCustomerConn();
            // if(elementCols.containsKey(viewByElement.trim()))
            // {
            PreparedStatement ps = con.prepareStatement(dataQuery);
            ResultSet rs = ps.executeQuery();
            //  }
            PbReturnObject allD = new PbReturnObject();
            if (rs != null) {
                allD = new PbReturnObject(rs);
            }
            HashMap primH = new HashMap();
            String oldPv = "";
            String newPv = "";
            HashMap allV = new HashMap();
            if (!viewByElement.equalsIgnoreCase("0")) {
                for (int n = 0; n < allD.getRowCount(); n++) {
                    oldPv = allD.getFieldValueString(n, 0);
                    //  ////////////////////////////////////////////////////////////////////////////////////////////////.println(" oldPv "+oldPv);
                    if (newPv.equalsIgnoreCase("")) {
                        newPv = oldPv;
                    } else if (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
                        allV.put(newPv, primH);
                        primH = new HashMap();
                        newPv = oldPv;
                    }
                    String dat = allD.getFieldValueString(n, 2);
                    long mValue = Long.parseLong(allD.getFieldValueString(n, 1));
                    primH.put(dat, new Long(mValue));
                    if (n == (allD.getRowCount() - 1)) {
                        allV.put(oldPv, primH);
                    }
                    ////////////////////////.println(" allV " + allV);
                }
            } else {
                oldPv = "OverAll";
                primH = new HashMap();
                for (int n = 0; n < allD.getRowCount(); n++) {
                    ////////////////////////////////////////////////////////////////////.println(" in allD");
                    if (newPv.equalsIgnoreCase("")) {
                        newPv = oldPv;
                    }

                    String dat = allD.getFieldValueString(n, 1);
                    long mValue = Long.parseLong(allD.getFieldValueString(n, 0));
                    primH.put(dat, new Long(mValue));
                    allV.put(oldPv, primH);

                }
            }

            for (int x = 1; x < rowEdgeValuesA.size(); x++) {
                String pVal = rowEdgeValuesA.get(x).toString();
                HashMap dt = new HashMap();
                if (allV.containsKey(pVal)) {
                    dt = (HashMap) allV.get(pVal);
                }
                for (int y = 0; y < columnEdgeValuesA.size(); y++) {
                    String timeV = columnEdgeValuesA.get(y).toString();
                    long mV = 0;
                    if (dt.containsKey(timeV)) {
                        mV = ((Long) dt.get(timeV)).longValue();
                    }
                    String value = "";
                    if (mV != 0) {
                        value = "" + mV + "";
                    }
                    originalResult.put(rowEdgeValuesA.get(x) + ":" + timeV, new Long(mV));
                }
            }

            primaryAnalyze = primA;
            secAnalyze = secA;
            startRange = sRange;




            String primParamEleId = "";
            /*
             * if (session.getAttribute("primParamEleId") != null) {
             * primParamEleId = (String) session.getAttribute("primParamEleId");
             * }
             */



            HashMap boxNames = new HashMap();
            /*
             * Enumeration ee = request.getParameterNames(); while
             * (ee.hasMoreElements()) { String reqKey = (String)
             * ee.nextElement(); String val = request.getParameter(reqKey); if
             * (reqKey.startsWith("CBOARP")) { String elementId =
             * reqKey.substring(6); if (val.equalsIgnoreCase("All")) {
             * nonAllIds.put(elementId, "-1"); } else { nonAllIds.put(elementId,
             * val); } } if (reqKey.startsWith("CBOVIEW_BY")) { String k =
             * reqKey.substring(10); viewByName = request.getParameter(reqKey);
             * } long mValue = 0;
             * ////////////////////////////////////////////////////////////////////.println("
             * originalResult " + originalResult); if
             * (originalResult.containsKey(reqKey)) { if
             * (!val.equalsIgnoreCase("")) { mValue = Long.parseLong(val); }
             * boxNames.put(reqKey, new Long(mValue)); } }
             */
            // boxNames=excelvalues;
            String allBox[] = (String[]) excelvalues.keySet().toArray(new String[0]);
            String k = "";
            String v = "";
            long value = 0;
            for (int m = 0; m < allBox.length; m++) {
                k = allBox[m];
                value = 0;
                v = (String) excelvalues.get(k);
                ////////////////////////////////////////////////////////////////////.println(" v--- "+v);
                if (originalResult.containsKey(k)) {
                    if (!v.equalsIgnoreCase("")) {
                        value = Long.parseLong(v);
                    }
                    boxNames.put(k.trim(), new Long(value));
                }
            }
            if (secAnalyze.equalsIgnoreCase("Time")) {

                PbTargetParamTable pTable = new PbTargetParamTable();
                PbTargetValuesParam targetParam = new PbTargetValuesParam();
                Session sess = new Session();
                PbTargetDAO pbDao = new PbTargetDAO();

                targetParam.setPeriodType(minTimeLevel);
                targetParam.setRatioComb(originalResult);
                targetParam.setMinimumTimeLevel(minTimeLevel);
                targetParam.setTargetId(targetId);
                targetParam.setNonAllIds(nonAllIds);
                // targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
                targetParam.setViewByParameter(primaryAnalyze.trim());


                for (int i = 0; i < rowEdgeValuesA.size(); i++) {
                    for (int j = 0; j < columnEdgeValuesA.size(); j++) {
                        String key = rowEdgeValuesA.get(i).toString().trim() + ":" + columnEdgeValuesA.get(j).toString().trim();
                        long newVal = 0;
                        long oldVal = 0;
                        if (boxNames.containsKey(key)) {
                            if (targetBasis.equalsIgnoreCase("Absolute")) {
                                newVal = ((Long) boxNames.get(key)).longValue();
                            } else if (targetBasis.equalsIgnoreCase("Percent")) {
                                long perVal = ((Long) boxNames.get(key)).longValue();
                                long totVal = 1;
                                if (RestrictingTotal.containsKey(columnEdgeValuesA.get(j).toString().trim())) {
                                    totVal = ((Long) RestrictingTotal.get(columnEdgeValuesA.get(j).toString().trim())).longValue();
                                    long v2 = perVal * totVal;
                                    newVal = v2 / 100;
                                }

                            }
                        }
                        if (originalResult.containsKey(key)) {
                            oldVal = ((Long) originalResult.get(key)).longValue();
                        }
                        // when new is not!=0 and original update
                        if (newVal != 0 && (newVal != oldVal) && oldVal != 0) {
                            PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                            targetParam2.setMeasureVal(newVal);
                            targetParam2.setDateVal(columnEdgeValuesA.get(j).toString().trim());
                            targetParam2.setUpdateFlag("Y");
                            targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString().trim());
                            ////////////////////////.println(key + " update " + newVal + " oldVal " + oldVal);
                            pTable.addRow(targetParam2);
                        }
                        // when new is not!=0 and original delete
                        if (newVal == 0 && oldVal != 0) {
                            PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                            targetParam2.setMeasureVal(newVal);
                            targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
                            targetParam2.setUpdateFlag("D");
                            targetParam2.setDeleteFlag("D");
                            targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                            ////////////////////////.println(key + " delete " + newVal + " oldVal " + oldVal);
                            pTable.addRow(targetParam2);
                        }
                        // when new is not!=0 and original 0
                        if (newVal != 0 && oldVal == 0) {
                            PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                            targetParam2.setMeasureVal(newVal);
                            targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
                            targetParam2.setUpdateFlag("N");
                            targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                            ////////////////////////.println(key + " insert-= " + newVal + " oldVal " + oldVal);
                            pTable.addRow(targetParam2);
                        }
                    }
                }
                sess.setObject(targetParam);
                sess.setObject(pTable);
                ////////////////////////.println(" pTable " + pTable.getRowCount() + " -- " + primaryAnalyze);
                if (pTable.getRowCount() > 0) {
                    // pbDao.saveTargetValues(sess);
                    if (primaryAnalyze.trim().equalsIgnoreCase("OverAll")) {
                        ////////////////////////////////////////////////////////////////////.println(" in overall save-. ");
                        pbDao.saveTargetValuesNewForOverAll(sess);

                    } else {
                        ////////////////////////.println(" in other save-. ");
                        pbDao.saveTargetValuesNew(sess);

                    }
                    if (primParamEleId.equalsIgnoreCase(primaryAnalyze)) {
                        // pbDao.saveForPrimaryParameter(sess);
                    }
                }
            } else {
                PbTargetParamTable pTable = new PbTargetParamTable();
                PbTargetValuesParam targetParam = new PbTargetValuesParam();
                Session sess = new Session();
                PbTargetDAO pbDao = new PbTargetDAO();

                targetParam.setPeriodType(minTimeLevel);
                targetParam.setMinimumTimeLevel(minTimeLevel);
                targetParam.setTargetId(targetId);
                targetParam.setNonAllIds(nonAllIds);
                targetParam.setRatioComb(originalResult);
                // targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
                targetParam.setPrimaryParameter(primaryAnalyze);
                targetParam.setSecViewByParameter(colViewByElement);
                targetParam.setViewByParameter(viewByElement);
                ////////////////////////////////////////////////////////////////////.println(" originalResult " + originalResult);

                for (int i = 0; i < rowEdgeValuesA.size(); i++) {
                    for (int j = 0; j < columnEdgeValuesA.size(); j++) {
                        String key = rowEdgeValuesA.get(i).toString().trim() + ":" + columnEdgeValuesA.get(j).toString().trim();
                        long newVal = 0;
                        long oldVal = 0;
                        if (boxNames.containsKey(key)) {
                            newVal = ((Long) boxNames.get(key)).longValue();
                        }
                        if (originalResult.containsKey(key)) {
                            oldVal = ((Long) originalResult.get(key)).longValue();
                        }
                        ////////////////////////.println(newVal + " oldVal-- " + oldVal + " ;;; " + key);
                        // when new is not!=0 and original update
                        if (newVal != 0 && (newVal != oldVal) && oldVal != 0) {
                            PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                            targetParam2.setMeasureVal(newVal);
                            targetParam2.setDateVal(startRange);
                            targetParam2.setUpdateFlag("Y");
                            // targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                            targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
                            targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim());
                            ////////////////////////.println(key + " update " + newVal + " oldVal " + oldVal);
                            pTable.addRow(targetParam2);
                        }
                        // when new is not!=0 and original delete
                        if (newVal == 0 && oldVal != 0) {
                            PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                            targetParam2.setMeasureVal(newVal);
                            targetParam2.setDateVal(startRange);
                            targetParam2.setUpdateFlag("D");
                            targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim());
                            targetParam2.setDeleteFlag("D");
                            // targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                            targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
                            ////////////////////////.println(key + " delete " + newVal + " oldVal " + oldVal);
                            pTable.addRow(targetParam2);
                        }
                        // when new is not!=0 and original 0
                        if (newVal != 0 && oldVal == 0) {
                            PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
                            targetParam2.setMeasureVal(newVal);
                            targetParam2.setDateVal(startRange);
                            targetParam2.setUpdateFlag("N");
                            targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim());
                            // targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
                            targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
                            ////////////////////////.println(key + " insert-- " + newVal + " oldVal " + oldVal);
                            pTable.addRow(targetParam2);
                        }
                    }
                }
                sess.setObject(targetParam);
                sess.setObject(pTable);
                ////////////////////////.println(" pTable,.,.,;; " + pTable.getRowCount());
                if (pTable.getRowCount() > 0) {
                    pbDao.savePrimSecNew(sess);
                }
            }



        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }
}
