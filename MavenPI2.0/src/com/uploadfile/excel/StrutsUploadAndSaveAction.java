/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uploadfile.excel;

/**
 *
 * @author Administrator
 */
import com.progen.target.ExcelSheetValidator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * @author Amit Gupta @Web http://www.roseindia.net @Email struts@roseindia.net
 */
/**
 * Struts File Upload Action Form.
 *
 */
public class StrutsUploadAndSaveAction extends Action {

    public static Logger logger = Logger.getLogger(StrutsUploadAndSaveAction.class);

    public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StrutsUploadAndSaveForm myForm = (StrutsUploadAndSaveForm) form;
        HttpSession session = request.getSession(false);
        String UserId = "";
        String targetId = "";
        if (session.getAttribute("USERID") != null) {
            UserId = String.valueOf(session.getAttribute("USERID"));
        }
        if (session.getAttribute("targetId") != null) {
            targetId = String.valueOf(session.getAttribute("targetId"));
        }

        // Process the FormFile
        FormFile myFile = myForm.getTheFile();
        FileOutputStream fos = null;
        //////////////////////////////////////////////////////////////////////////.println.println(targetId+" targetId myfile " + myFile.toString());
        ExcelSheetValidator exVal = new ExcelSheetValidator();
        String status = exVal.checkSheetForTarget(targetId, myFile.toString());
        //////////////////////////////////////////////////////////////////////////.println.println(" status-0-0-= "+status);
        //String contentType = myFile.getContentType();
        //Get the file name
        String oldfileName = myFile.getFileName();
        byte[] fileData = myFile.getFileData();

        String newfileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        //////////////////////////////////////////////////////////////////////////.println.println("File path is " + getServlet().getServletContext().getRealPath("/") + newfileName + ".xls");
        File tempFile = new File(getServlet().getServletContext().getRealPath("/") + newfileName + ".xls");
        if (!tempFile.exists()) {
            tempFile.createNewFile();
            fos = new FileOutputStream(tempFile);
            fos.write(fileData);
        } else {
            fos = new FileOutputStream(tempFile);
            fos.write(fileData);
        }
        //////////////////////////////////////////////////////////////////////////.println.println("tempFile created");
        request.setAttribute("tempFile", tempFile);
        request.setAttribute("targetId", targetId);
        request.setAttribute("UserId", UserId);
        request.setAttribute("myFile", myFile.toString());




        /*
         * ReadXLSheet rxl = new ReadXLSheet(); HashMap excelvalues=new
         * HashMap();
         * //////////////////////////////////////////////////////////////////////////.println.println("
         * before sjkdsjkdj "+newfileName); HashMap all = (HashMap)
         * rxl.init(tempFile.toString()); excelvalues = (HashMap)
         * all.get("excelvalues");
         * //////////////////////////////////////////////////////////////////////////.println.println("
         * excelvalues after -------- "+excelvalues);
         */
        //request.setAttribute("fileName", oldfileName);

        // ExcelScheduler es = new ExcelScheduler(targetId,UserId,myFile.toString(),tempFile.toString());
        // es.readExcel(targetId,UserId,myFile.toString(),tempFile.toString());

        String uploadStatus = "";
        if (status.equalsIgnoreCase("Available")) {
            uploadStatus = exVal.checkSheetUploadStatus(myFile.toString());
        }
        request.setAttribute("uploadStatus", uploadStatus);
        request.setAttribute("status", status);

        //////////////////////////////////////////////////////////////////////////.println.println(" uploadStatus--- "+uploadStatus);
        if (status.equalsIgnoreCase("")) {
            status = "The Excel Sheet Selected is not for this target";
        }
        if (status.equalsIgnoreCase("Available")) {
            status = "The Excel Sheet upload request has been.Please verify data in target UI";
        }
        PrintWriter out = response.getWriter();
        out.println(status);

        if (status.equalsIgnoreCase("")) {
            //////////////////////////////////////////////////////////////////////////.println.println(" in idhfjdh ");
            //  return mapping.findForward(null);
        }

        return mapping.findForward("showTargetSheetStatus");

    }
}
////////////////
/*
 * String filName = myFile.toString();
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * excelvalues in mm " + excelvalues); PbDb pbdb = new PbDb(); ArrayList al =
 * new ArrayList(); String insertStatusQ = "insert into
 * excelSheet_uplaod(UPLOADNUMBER,EXCEL_SHEET_NAME,UPLOAD_TIME,STATUS)
 * values(excelSheet_uplaod_ID_SEQ.nextval,'" + filName +
 * "',sysdate,'Uploaded')";
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * insertStatusQ " + insertStatusQ); al.add(insertStatusQ); try {
 * pbdb.executeMultiple(al); } catch (Exception e) { logger.error("Exception:
 * ",e); } //ArrayList hskeylist=(ArrayList) all.get("hskeylist"); //
 * //////////////////////////////////////////////////////////////////////////.println.println(excelvalues+"
 * in excel read "+hskeylist);
 *
 * String UserId = ""; String minTimeLevel = "";
 *
 * PbTargetDAO tDao = new PbTargetDAO(); String targetId = ""; PbReturnObject
 * fileDet = tDao.getExcelFileDetail(filName); String colEdVals = ""; String
 * rowEdVals = ""; String primA = ""; String secA = ""; String sRange = "";
 * String nonAll = ""; String colViewByElement = "";
 *
 * if (fileDet.getRowCount() > 0) { targetId = fileDet.getFieldValueString(0,
 * "TARGET_ID"); minTimeLevel = fileDet.getFieldValueString(0, "PERIODTYPE");
 * colEdVals = fileDet.getFieldValueClobString(0, "COLUMNVIEWBYVALUES");
 * rowEdVals = fileDet.getFieldValueClobString(0, "ROWVIEWBYVALUES"); primA =
 * fileDet.getFieldValueString(0, "PRIMARYVIEWBY"); secA =
 * fileDet.getFieldValueString(0, "SECVIEWBY"); sRange =
 * fileDet.getFieldValueString(0, "TIMEVAL"); nonAll =
 * fileDet.getFieldValueClobString(0, "NONALLVALUE");
 *
 * }
 * String startRange = sRange; colViewByElement = secA; HttpSession session =
 * request.getSession(false);
 * //////////////////////////////////////////////////////////////////////////.println.println("targetId
 * is:: in save " + targetId); if (session.getAttribute("USERID") != null) {
 * UserId = String.valueOf(session.getAttribute("USERID")); } HashMap
 * originalResult = new HashMap(); ArrayList columnEdgeValuesA = new
 * ArrayList(); ArrayList rowEdgeValuesA = new ArrayList(); String secAnalyze =
 * secA; String primaryAnalyze = "";
 *
 * String colEdValsArr[] = colEdVals.split("~"); String rowEdValsArr[] =
 * rowEdVals.split("~"); for (int y = 0; y < colEdValsArr.length; y++) {
 * columnEdgeValuesA.add(colEdValsArr[y]); }
 *
 * for (int y = 0; y < rowEdValsArr.length; y++) {
 * rowEdgeValuesA.add(rowEdValsArr[y]); }
 *
 * String dataQuery = ""; String viewByElement = primA; String periodType =
 * minTimeLevel; String overallT = ""; String targetQ = "select
 * target_table_id,pgbt.buss_table_name,ptm.measure_name from prg_target_master
 * ptm," + " prg_grp_buss_table pgbt where prg_measure_id in (select measure_id
 * from target_master WHERE " + " target_master.target_id=" + targetId + ") and
 * pgbt.buss_table_id = ptm.target_table_id";
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * targetQ " + targetQ);
 *
 * PbReturnObject pbro = pbdb.execSelectSQL(targetQ); String measureName =
 * pbro.getFieldValueString(0, "MEASURE_NAME"); String targetTable =
 * pbro.getFieldValueString(0, "BUSS_TABLE_NAME"); String viewName = "";
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * primA " + primA); if (primA.equalsIgnoreCase("0")) { viewName = "OverAll"; }
 * String allParametrsFilterClause = "";
 * //////////////////////////////////////////////////////////////////////////.println.println(periodType
 * + "-=-=-=-=-=--=- viewByElement " + viewByElement + " colViewByElement " +
 * colViewByElement); HashMap elementCols = new HashMap(); PbReturnObject pbro2
 * = new PbReturnObject(); String getBussColNames = "select DISTINCT element_id,
 * buss_col_name from prg_user_all_info_details where folder_id " + " in(select
 * bus_folder_id from target_measure_folder where bus_group_id=(select
 * business_group from prg_target_master" + " where prg_measure_id in(select
 * measure_id from target_master WHERE target_master.target_id=&)))";
 *
 * Object tarOb[] = new Object[1]; tarOb[0] = targetId; String
 * fingetBussColNames = pbdb.buildQuery(getBussColNames, tarOb);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * fingetBussColNames " + fingetBussColNames); pbro2 =
 * pbdb.execSelectSQL(fingetBussColNames); for (int i = 0; i <
 * pbro2.getRowCount(); i++) { elementCols.put(pbro2.getFieldValueString(i,
 * "ELEMENT_ID"), pbro2.getFieldValueString(i, "BUSS_COL_NAME")); //
 * viewByNames.put(pbro.getFieldValueString(i,"ELEMENT_ID"),pbro.getFieldValueString(i,"PARAM_DISP_NAME"));
 * }
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * elementCols " + elementCols); HashMap viewByNames = new HashMap(); String
 * getDispNames = "select distinct element_id,param_disp_name from
 * prg_target_param_details where target_id=&"; String fingetDispNames =
 * pbdb.buildQuery(getDispNames, tarOb);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * fingetDispNames " + fingetDispNames); PbReturnObject pbroDispNames =
 * pbdb.execSelectSQL(fingetDispNames); for (int m = 0; m <
 * pbroDispNames.getRowCount(); m++) {
 * viewByNames.put(pbroDispNames.getFieldValueString(m, "ELEMENT_ID"),
 * pbroDispNames.getFieldValueString(m, "PARAM_DISP_NAME")); }
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * viewByNames.//./ " + viewByNames + " nonAll " + nonAll); HashMap nonAllIds =
 * new HashMap(); String viewByName = ""; String nonAllArr[] =
 * nonAll.split("~"); for (int m = 0; m < nonAllArr.length; m++) { String val[]
 * = nonAllArr[m].split("="); nonAllIds.put(val[0], val[1]); }
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * nonAllIds " + nonAllIds); for (int l = 0; l < nonAllArr.length; l++) { String
 * val[] = nonAllArr[l].split("="); String a = (String) nonAllIds.get(val[0]);
 * //////////////////////////////////////////////////////////////////////////.println.println(nonAllArr[l]
 * + " a " + a); if (!a.equalsIgnoreCase("-1")) { if
 * (!a.equalsIgnoreCase("All")) { allParametrsFilterClause =
 * allParametrsFilterClause + " and " + elementCols.get(val[0]) + "='" + a +
 * "'"; } } }
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * allParametrsFilterClause " + allParametrsFilterClause + " secA " + secA); if
 * (viewByNames.containsKey(viewByElement)) { overallT = (String)
 * elementCols.get(viewByElement); viewName = (String)
 * viewByNames.get(viewByElement); }
 *
 * String secCol = ""; String secName = ""; if (viewByNames.containsKey(secA)) {
 * secCol = (String) elementCols.get(secA); secName = (String)
 * viewByNames.get(secA); } String dataQuery2 = "";
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * viewByElement in excel " + viewByElement + " secAnalyze " + secAnalyze); if
 * (!viewByElement.equalsIgnoreCase("0")) { if
 * (secAnalyze.equalsIgnoreCase("Time")) {
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * in if 1 --"); if (periodType.equalsIgnoreCase("Day")) { dataQuery = "select "
 * + overallT + ",sum(" + measureName.trim() + "),to_char(t_date,'dd-mm-yy')
 * from " + targetTable.trim() + " where target_id='" + targetId + "' and
 * viewby='" + viewName + "' " + allParametrsFilterClause + " and t_date is not
 * null group by " + overallT + ",to_char(t_date,'dd-mm-yy') order by " +
 * overallT + ",to_char(t_date,'dd-mm-yy')"; } else if
 * (periodType.equalsIgnoreCase("Month")) { dataQuery = "select " + overallT +
 * ",sum(" + measureName.trim() + "),t_month from " + targetTable.trim() + "
 * where target_id='" + targetId + "' and viewby='" + viewName + "' " +
 * allParametrsFilterClause + " and t_month is not null group by " + overallT +
 * ",t_month order by " + overallT + ",t_month"; } //dataQuery ="select
 * "+elementCols.get(viewByElement.trim()).toString()+",sum("+measureName.trim()+"),t_month
 * from "+targetTable.trim()+" where target_id='"+targetId+"' and
 * viewby='"+viewByNames.get(viewByElement.trim()).toString()+"'"+allParametrsFilterClause+"
 * group by "+elementCols.get(viewByElement.trim()).toString()+",t_month order
 * by "+elementCols.get(viewByElement.trim()).toString()+",t_month"; else if
 * (periodType.equalsIgnoreCase("Quarter")) { dataQuery = "select " + overallT +
 * ",sum(" + measureName.trim() + "),t_qtr from " + targetTable.trim() + " where
 * target_id='" + targetId + "' and t_qtr is not null " +
 * allParametrsFilterClause + " " + " and viewby='" + viewName + "' group by " +
 * overallT + ", t_qtr order by " + overallT + ",t_qtr"; } else if
 * (periodType.equalsIgnoreCase("Year")) { dataQuery = "select " + overallT +
 * ",sum(" + measureName.trim() + "),t_year from " + targetTable.trim() + "
 * where target_id='" + targetId + "' and viewby='" + viewName + "' " +
 * allParametrsFilterClause + " and t_year is not null group by " + overallT +
 * ",t_year order by " + overallT + ",t_year"; } } else { if
 * (periodType.equalsIgnoreCase("Year")) {
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * in if year ");
 * //////////////////////////////////////////////////////////////////////////.println.println(elementCols
 * + " viewByElement " + viewByElement + " colViewByElement " + colViewByElement
 * + " startRange " + startRange); //dataQuery ="select
 * "+overallT+",sum("+measureName.trim()+"),t_year from "+targetTable.trim()+"
 * where target_id='"+targetId+"' and viewby='"+viewName+"' and secviewby='' "+
 * allParametrsFilterClause+" and t_year is not null and t_year='"+startRange+"'
 * group by "+overallT+",t_year order by "+overallT+",t_year"; dataQuery =
 * "select " + elementCols.get(viewByElement.trim()).toString() + ",sum(" +
 * measureName.trim() + ")," +
 * elementCols.get(colViewByElement.trim()).toString() + " from " +
 * targetTable.trim() + " where target_id='" + targetId + "' and viewby='" +
 * viewByNames.get(viewByElement.trim()).toString() + "' and secviewby='" +
 * viewByNames.get(colViewByElement.trim()).toString() + "' and t_year='" +
 * startRange + "' " + allParametrsFilterClause + " group by " +
 * elementCols.get(viewByElement.trim()).toString() + "," +
 * elementCols.get(colViewByElement.trim()).toString() + " order by " +
 * elementCols.get(viewByElement.trim()).toString() + "," +
 * elementCols.get(colViewByElement.trim()).toString(); dataQuery2 = "select " +
 * elementCols.get(viewByElement.trim()).toString() + ",sum(" +
 * measureName.trim() + ")," +
 * elementCols.get(colViewByElement.trim()).toString() + " from " +
 * targetTable.trim() + " where target_id='" + targetId + "' and viewby='" +
 * viewByNames.get(colViewByElement.trim()).toString() + "' and secviewby='" +
 * viewByNames.get(viewByElement.trim()).toString() + "' and t_year='" +
 * startRange + "' " + allParametrsFilterClause + " group by " +
 * elementCols.get(viewByElement.trim()).toString() + "," +
 * elementCols.get(colViewByElement.trim()).toString() + " order by " +
 * elementCols.get(viewByElement.trim()).toString() + "," +
 * elementCols.get(colViewByElement.trim()).toString(); }
 *
 * }
 * } else { if (viewByElement.equalsIgnoreCase("0")) {
 * //////////////////////////////////////////////////////////////////////////.println.println(periodType
 * + " in if overall "); ArrayList paramVals = new ArrayList();
 * paramVals.add("OverAll Target"); paramVals.add("OverAll"); }
 *
 * if (periodType.equalsIgnoreCase("Day")) { dataQuery = "select sum(" +
 * measureName.trim() + "),to_char(t_date,'dd-mm-yy') from " +
 * targetTable.trim() + " where target_id='" + targetId + "' and
 * viewby='OverAll' and t_date is not null group by to_char(t_date,'dd-mm-yy')
 * order by to_char(t_date,'dd-mm-yy')"; } else if
 * (periodType.equalsIgnoreCase("Month")) { dataQuery = "select sum(" +
 * measureName.trim() + "),t_month from " + targetTable.trim() + " where
 * target_id='" + targetId + "' and viewby='OverAll' and t_month is not null
 * group by t_month order by t_month"; } else if
 * (periodType.equalsIgnoreCase("Quarter") ||
 * periodType.equalsIgnoreCase("QTR")) { dataQuery = "select sum(" +
 * measureName.trim() + "),t_qtr from " + targetTable.trim() + " where
 * target_id='" + targetId + "' and viewby='OverAll' and t_qtr is not null group
 * by t_qtr order by t_qtr"; } else if (periodType.equalsIgnoreCase("Year")) {
 * dataQuery = "select sum(" + measureName.trim() + "),t_year from " +
 * targetTable.trim() + " where target_id='" + targetId + "' and
 * viewby='OverAll' and t_year is not null group by t_year order by t_year"; }
 *
 * }
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * dataQuery in excelupload " + dataQuery);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * dataQuery2 in excelupload " + dataQuery2); Connection con =
 * ProgenConnection.getCustomerConn(); //
 * if(elementCols.containsKey(viewByElement.trim())) // { PreparedStatement ps =
 * con.prepareStatement(dataQuery); ResultSet rs = ps.executeQuery(); // }
 * PbReturnObject allD = new PbReturnObject(); if (rs != null) { allD = new
 * PbReturnObject(rs); } HashMap primH = new HashMap(); String oldPv = "";
 * String newPv = ""; HashMap allV = new HashMap(); if
 * (!viewByElement.equalsIgnoreCase("0")) { for (int n = 0; n <
 * allD.getRowCount(); n++) { oldPv = allD.getFieldValueString(n, 0); //
 * //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("
 * oldPv "+oldPv); if (newPv.equalsIgnoreCase("")) { newPv = oldPv; } else if
 * (!newPv.trim().equalsIgnoreCase(oldPv.trim()) && newPv.length() > 0) {
 * allV.put(newPv, primH); primH = new HashMap(); newPv = oldPv; } String dat =
 * allD.getFieldValueString(n, 2); long mValue =
 * Long.parseLong(allD.getFieldValueString(n, 1)); primH.put(dat, new
 * Long(mValue)); if (n == (allD.getRowCount() - 1)) { allV.put(oldPv, primH); }
 * //
 * //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("
 * primH "+primH); } } else { oldPv = "OverAll";
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * in overall hm ");
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * count " + allD.getRowCount()); primH = new HashMap(); for (int n = 0; n <
 * allD.getRowCount(); n++) {
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * in allD"); if (newPv.equalsIgnoreCase("")) { newPv = oldPv; }
 *
 * String dat = allD.getFieldValueString(n, 1); long mValue =
 * Long.parseLong(allD.getFieldValueString(n, 0)); primH.put(dat, new
 * Long(mValue)); allV.put(oldPv, primH);
 *
 * }
 * }
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * allV in upload " + allV);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * rowEdgeValuesA " + rowEdgeValuesA);
 *
 * for (int x = 1; x < rowEdgeValuesA.size(); x++) { String pVal =
 * rowEdgeValuesA.get(x).toString(); HashMap dt = new HashMap(); if
 * (allV.containsKey(pVal)) { dt = (HashMap) allV.get(pVal); } for (int y = 0; y
 * < columnEdgeValuesA.size(); y++) { String timeV =
 * columnEdgeValuesA.get(y).toString(); long mV = 0; if (dt.containsKey(timeV))
 * { mV = ((Long) dt.get(timeV)).longValue(); } String value = ""; if (mV != 0)
 * { value = "" + mV + ""; } originalResult.put(rowEdgeValuesA.get(x) + ":" +
 * timeV, new Long(mV)); } }
 *
 *
 *
 * //////////////////////////////////////////////////////////////////////////.println.println("originalResult-.
 * is:: " + originalResult);
 *
 *
 *
 * primaryAnalyze = primA; secAnalyze = secA; startRange = sRange;
 *
 *
 *
 *
 * String primParamEleId = ""; if (session.getAttribute("primParamEleId") !=
 * null) { primParamEleId = (String) session.getAttribute("primParamEleId"); }
 *
 *
 *
 * HashMap boxNames = new HashMap(); Enumeration ee =
 * request.getParameterNames(); while (ee.hasMoreElements()) { String reqKey =
 * (String) ee.nextElement(); String val = request.getParameter(reqKey); if
 * (reqKey.startsWith("CBOARP")) { String elementId = reqKey.substring(6); if
 * (val.equalsIgnoreCase("All")) { nonAllIds.put(elementId, "-1"); } else {
 * nonAllIds.put(elementId, val); } } if (reqKey.startsWith("CBOVIEW_BY")) {
 * String k = reqKey.substring(10); viewByName = request.getParameter(reqKey); }
 * long mValue = 0;
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * originalResult " + originalResult); if (originalResult.containsKey(reqKey)) {
 * if (!val.equalsIgnoreCase("")) { mValue = Long.parseLong(val); }
 * boxNames.put(reqKey, new Long(mValue)); } } // boxNames=excelvalues; String
 * allBox[] = (String[]) excelvalues.keySet().toArray(new String[0]); String k =
 * ""; String v = ""; long value = 0; for (int m = 0; m < allBox.length; m++) {
 * k = allBox[m]; value = 0; v = (String) excelvalues.get(k); if
 * (!v.equalsIgnoreCase("")) { value = Long.parseLong(v); }
 * boxNames.put(k.trim(), new Long(value)); }
 * //////////////////////////////////////////////////////////////////////////.println.println(nonAllIds
 * + " - boxNames " + boxNames);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * startRange... " + startRange);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * rowEdgeValuesA ." + rowEdgeValuesA + " columnEdgeValuesA " +
 * columnEdgeValuesA + " viewByName.. " + viewByName); if
 * (secAnalyze.equalsIgnoreCase("Time")) { PbTargetParamTable pTable = new
 * PbTargetParamTable(); PbTargetValuesParam targetParam = new
 * PbTargetValuesParam(); Session sess = new Session(); PbTargetDAO pbDao = new
 * PbTargetDAO();
 *
 * targetParam.setPeriodType(minTimeLevel);
 * targetParam.setRatioComb(originalResult);
 * targetParam.setMinimumTimeLevel(minTimeLevel);
 * targetParam.setTargetId(targetId); targetParam.setNonAllIds(nonAllIds); //
 * targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * primaryAnalyze in action " + primaryAnalyze);
 * targetParam.setViewByParameter(primaryAnalyze.trim());
 *
 * for (int i = 0; i < rowEdgeValuesA.size(); i++) { for (int j = 0; j <
 * columnEdgeValuesA.size(); j++) { String key =
 * rowEdgeValuesA.get(i).toString().trim() + ":" +
 * columnEdgeValuesA.get(j).toString().trim(); long newVal = 0; long oldVal = 0;
 * if (boxNames.containsKey(key)) { newVal = ((Long)
 * boxNames.get(key)).longValue(); } if (originalResult.containsKey(key)) {
 * oldVal = ((Long) originalResult.get(key)).longValue(); }
 * //////////////////////////////////////////////////////////////////////////.println.println(newVal
 * + " oldVal " + oldVal); // when new is not!=0 and original update if (newVal
 * != 0 && (newVal != oldVal) && oldVal != 0) { PbTargetValuesParam targetParam2
 * = new PbTargetValuesParam(); targetParam2.setMeasureVal(newVal);
 * targetParam2.setDateVal(columnEdgeValuesA.get(j).toString().trim());
 * targetParam2.setUpdateFlag("Y");
 * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString().trim());
 * //////////////////////////////////////////////////////////////////////////.println.println(key
 * + " update " + newVal + " oldVal " + oldVal); pTable.addRow(targetParam2); }
 * // when new is not!=0 and original delete if (newVal == 0 && oldVal != 0) {
 * PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
 * targetParam2.setMeasureVal(newVal);
 * targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
 * targetParam2.setUpdateFlag("D"); targetParam2.setDeleteFlag("D");
 * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
 * //////////////////////////////////////////////////////////////////////////.println.println(key
 * + " delete " + newVal + " oldVal " + oldVal); pTable.addRow(targetParam2); }
 * // when new is not!=0 and original 0 if (newVal != 0 && oldVal == 0) {
 * PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
 * targetParam2.setMeasureVal(newVal);
 * targetParam2.setDateVal(columnEdgeValuesA.get(j).toString());
 * targetParam2.setUpdateFlag("N");
 * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
 * //////////////////////////////////////////////////////////////////////////.println.println(key
 * + " insert " + newVal + " oldVal " + oldVal); pTable.addRow(targetParam2); }
 * } } sess.setObject(targetParam); sess.setObject(pTable);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * pTable " + pTable.getRowCount() + " -- " + primaryAnalyze); if
 * (pTable.getRowCount() > 0) { // pbDao.saveTargetValues(sess); if
 * (primaryAnalyze.trim().equalsIgnoreCase("OverAll")) {
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * in overall save-. "); pbDao.saveTargetValuesNewForOverAll(sess);
 *
 * } else {
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * in other save-. "); pbDao.saveTargetValuesNew(sess);
 *
 * }
 * if (primParamEleId.equalsIgnoreCase(primaryAnalyze)) { //
 * pbDao.saveForPrimaryParameter(sess); } } } else { PbTargetParamTable pTable =
 * new PbTargetParamTable(); PbTargetValuesParam targetParam = new
 * PbTargetValuesParam(); Session sess = new Session(); PbTargetDAO pbDao = new
 * PbTargetDAO();
 *
 * targetParam.setPeriodType(minTimeLevel);
 * targetParam.setMinimumTimeLevel(minTimeLevel);
 * targetParam.setTargetId(targetId); targetParam.setNonAllIds(nonAllIds);
 * targetParam.setRatioComb(originalResult); //
 * targetParam.setViewByParameter(viewByName.trim());primaryAnalyze
 * targetParam.setPrimaryParameter(primaryAnalyze);
 * targetParam.setSecViewByParameter(colViewByElement);
 * targetParam.setViewByParameter(viewByElement);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * originalResult " + originalResult);
 *
 * for (int i = 0; i < rowEdgeValuesA.size(); i++) { for (int j = 0; j <
 * columnEdgeValuesA.size(); j++) { String key =
 * rowEdgeValuesA.get(i).toString().trim() + ":" +
 * columnEdgeValuesA.get(j).toString().trim(); long newVal = 0; long oldVal = 0;
 * if (boxNames.containsKey(key)) { newVal = ((Long)
 * boxNames.get(key)).longValue(); } if (originalResult.containsKey(key)) {
 * oldVal = ((Long) originalResult.get(key)).longValue(); }
 * //////////////////////////////////////////////////////////////////////////.println.println(newVal
 * + " oldVal-- " + oldVal + " ;;; " + key); // when new is not!=0 and original
 * update if (newVal != 0 && (newVal != oldVal) && oldVal != 0) {
 * PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
 * targetParam2.setMeasureVal(newVal); targetParam2.setDateVal(startRange);
 * targetParam2.setUpdateFlag("Y"); //
 * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
 * targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
 * targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim());
 * //////////////////////////////////////////////////////////////////////////.println.println(key
 * + " update " + newVal + " oldVal " + oldVal); pTable.addRow(targetParam2); }
 * // when new is not!=0 and original delete if (newVal == 0 && oldVal != 0) {
 * PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
 * targetParam2.setMeasureVal(newVal); targetParam2.setDateVal(startRange);
 * targetParam2.setUpdateFlag("D");
 * targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim());
 * targetParam2.setDeleteFlag("D"); //
 * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
 * targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
 * //////////////////////////////////////////////////////////////////////////.println.println(key
 * + " delete " + newVal + " oldVal " + oldVal); pTable.addRow(targetParam2); }
 * // when new is not!=0 and original 0 if (newVal != 0 && oldVal == 0) {
 * PbTargetValuesParam targetParam2 = new PbTargetValuesParam();
 * targetParam2.setMeasureVal(newVal); targetParam2.setDateVal(startRange);
 * targetParam2.setUpdateFlag("N");
 * targetParam2.setPrimAnalyzeVal(rowEdgeValuesA.get(i).toString().trim()); //
 * targetParam2.setPrimViewByValue(rowEdgeValuesA.get(i).toString());
 * targetParam2.setPrimViewByValue(columnEdgeValuesA.get(j).toString().trim());
 * //////////////////////////////////////////////////////////////////////////.println.println(key
 * + " insert-- " + newVal + " oldVal " + oldVal); pTable.addRow(targetParam2);
 * } } } sess.setObject(targetParam); sess.setObject(pTable);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * pTable,.,.,;; " + pTable.getRowCount()); if (pTable.getRowCount() > 0) {
 * pbDao.savePrimSecNew(sess); } }
 *
 * /////////////////
 *
 * if (tempFile.exists()) {
 * //////////////////////////////////////////////////////////////////////////.println.println("File
 * exits or newly created "+tempFile);
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * File deleted is " + tempFile.delete());
 *
 * }
 */
//return mapping.findForward("success");
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 *
 * package com.uploadfile.excel;
 *
 *
 *
 * @author Administrator
 *
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse;
 *
 * import org.apache.struts.action.Action; import
 * org.apache.struts.action.ActionForm; import
 * org.apache.struts.action.ActionForward; import
 * org.apache.struts.action.ActionMapping; import
 * org.apache.struts.upload.FormFile; import java.io.*; import
 * java.util.Calendar;
 *
 * /**
 * @author Amit Gupta @Web http://www.roseindia.net @Email struts@roseindia.net
 *
 * Struts File Upload Action Form.
 *
 *
 * public class StrutsUploadAndSaveAction extends Action {
 *
 * public ActionForward execute( ActionMapping mapping, ActionForm form,
 * HttpServletRequest request, HttpServletResponse response) throws Exception {
 *
 * StrutsUploadAndSaveForm myForm = (StrutsUploadAndSaveForm) form;
 *
 * // Process the FormFile FormFile myFile = myForm.getTheFile();
 * //////////////////////////////////////////////////////////////////////////.println.println("myfile
 * -.." + myFile); String contentType = myFile.getContentType(); //Get the file
 * name String oldfileName = myFile.getFileName(); //int fileSize =
 * myFile.getFileSize(); byte[] fileData = myFile.getFileData(); String
 * newfileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
 * //////////////////////////////////////////////////////////////////////////.println.println("File
 * path is " + getServlet().getServletContext().getRealPath("/") + newfileName +
 * ".xls"); File tempFile = new
 * File(getServlet().getServletContext().getRealPath("/") + newfileName +
 * ".xls"); if(!tempFile.exists()){ tempFile.createNewFile(); }
 * //////////////////////////////////////////////////////////////////////////.println.println("tempFile
 * created");
 *
 * if (tempFile.exists()) {
 * //////////////////////////////////////////////////////////////////////////.println.println("File
 * exits or newly created"); //
 * //////////////////////////////////////////////////////////////////////////.println.println("
 * File deleted is " + tempFile.delete());
 *
 * }
 *
 * //////////////////////////////////////////////////////////////////////////.println.println("tempFile
 * is" + tempFile);
 *
 *
 * response.setContentType("application/x-download");
 * response.setHeader("Content-Disposition",
 * "attachment;filename=downnload.xls");
 *
 * response.getOutputStream().write(fileData);
 *
 * request.setAttribute("fileName", oldfileName); return null;
 *
 * //return mapping.findForward("success"); } }
 */
