/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.action;

/**
 *
 * @author DINANATH
 */
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.report.*;
import com.progen.report.data.DataFacade;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.report.query.QueryExecutor;
import com.progen.reportdesigner.action.ReportAssignmentsResBundleMySql;
import com.progen.reportdesigner.action.ReportAssignmentsResBundleSqlServer;
import com.progen.reportdesigner.action.ReportAssignmentsResourceBundle;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.ProgenReportViewerBD;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.reportview.db.ProgenReportViewerDAO;
import com.progen.scheduler.AOSchedule;
import com.progen.scheduler.KPIAlertSchedule;
import com.progen.scheduler.SchedulerBD;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
//import org.jfree.util.SortOrder;
//import org.jfree.util.SortOrder;
import prg.db.Container;
import prg.db.ContainerConstants;
import prg.db.ContainerConstants.SortOrder;
import prg.db.OnceViewContainer;
import prg.db.OneViewLetDetails;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.ReportTablePropertyBuilder;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class ProgenReportViewerAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ProgenReportViewerAction.class);

    private ResourceBundle getResourceBundle() {
        ResourceBundle resourceBundle;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            resourceBundle = new CustomMeasureResourceBundleSqlServer();
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            resourceBundle = new CustomMeasureResourceBundleMySql();
        } else {
            resourceBundle = new CustomMeasureResourceBundle();
        }
        return resourceBundle;
    }

    private ResourceBundle getResourceBundle1() {
        ResourceBundle resourceBundle1;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            resourceBundle1 = new ReportAssignmentsResBundleSqlServer();
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            resourceBundle1 = new ReportAssignmentsResBundleMySql();
        } else {

            resourceBundle1 = new ReportAssignmentsResourceBundle();
        }
        return resourceBundle1;
    }
//    PbReportViewerBD reportViewBD = new PbReportViewerBD();
//    ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//    ReportTemplateBD reportTemplateBD = new ReportTemplateBD();
//    PbReportQuery reportQry = new PbReportQuery();

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("loadDialogs", "loadDialogs");
        map.put("getYearData", "getYearData");
        map.put("getQtrData", "getQtrData");
        map.put("getinformmeasure", "getinformmeasure");
        map.put("getAllViewByIdandName", "getAllViewByIdandName");
        map.put("setTableListToContainerAO", "setTableListToContainerAO");
        map.put("getStartPage", "getStartPage");
        map.put("getLocalObjectMap", "getLocalObjectMap");
        map.put("getReportDefinitionDate", "getReportDefinitionDate");
        map.put("GroupedMeasureCheck", "GroupedMeasureCheck");
        map.put("saveCollectObjectInLocalFile", "saveCollectObjectInLocalFile");
        map.put("getAllCollectObjectCreatedFile", "getAllCollectObjectCreatedFile");
        map.put("setDefaultCollectSavepoint", "setDefaultCollectSavepoint");
        map.put("addMeasureAsViewBy", "addMeasureAsViewBy");
        //Added by Ashutosh
        map.put("setSavePoint", "setSavePoint");
        map.put("createCustomMeasureForGraphs", "createCustomMeasureForGraphs");
        map.put("getComboGraphData", "getComboGraphData");
        map.put("getAOBasedOnReport", "getAOBasedOnReport");
        map.put("createAOForGraph", "createAOForGraph");
        map.put("invalidateCache", "invalidateCache");
        map.put("rebuildCache", "rebuildCache");
        map.put("currentSelectedTag", "currentSelectedTag");
        //added by Dinanath
        map.put("showExistingDimForEditAO", "showExistingDimForEditAO");
        map.put("getcurrentYearDate", "getcurrentYearDate");
        map.put("getGraphData","getGraphData");
        map.put("addNewPage", "addNewPage");
        map.put("createFirstPage", "createFirstPage");
        map.put("changeReportPageMapping", "changeReportPageMapping");
        map.put("updatePageSequence", "updatePageSequence");
        map.put("renamePage", "renamePage");
         map.put("defineKpiAlertSchedule", "defineKpiAlertSchedule");
          map.put("getViwByIconMapping", "getViwByIconMapping");
        map.put("ParentGroupedMeasuresCheck", "ParentGroupedMeasuresCheck");
        map.put("getPeriodDataFlag", "getPeriodDataFlag");
         map.put("sendKPIChartSchedulerNow", "sendKPIChartSchedulerNow");
         map.put("saveGlobalDate","saveGlobalDate");
         map.put("removePage", "removePage");
         //added by bhargavi
         map.put("defineAOSchedule", "defineAOSchedule");
         map.put("validateScheduleAO", "validateScheduleAO");
         map.put("getComparisionWithRTMesaure","getComparisionWithRTMesaure");
         map.put("setMgtmlTemplateFlag","setMgtmlTemplateFlag");
         map.put("getComparedWithRT","getComparedWithRT");
           map.put("getapplyedfilters","getapplyedfilters");
             map.put("getAdvanceFilters","getAdvanceFilters");
         map.put("getAdvanceLogicalFilters","getAdvanceLogicalFilters");
         return map;
    }
    ArrayList<String> mes = new ArrayList<String>();

    public ActionForward loadDialogs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);


        return null;
    }

    public ActionForward getYearData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        PrintWriter out = null;
        Connection con = null;
        ResultSet rs2 = null;
        Statement st = null;
        ArrayList<String> measures = new ArrayList<String>();
        String reportid = request.getParameter("reportid");
        String gComp = request.getParameter("gComp");
        HttpSession session = request.getSession(false);
        String year = request.getParameter("year");
        PbDb pbdb = new PbDb();
        // String qtryear=request.getParameter("selectedqtryear");
        if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportid) != null) {
                Container container = (Container) map.get(reportid);
                if (container != null) {
                    measures = (ArrayList) container.getTableMeasure();
                    container.setNewUIyr(year);
                }

            }
        }
        String query = "";

        query = "select CY_END_DATE from PR_DAY_DENOM where CYEAR='" + year + "' ";

        PbTimeRanges pbTime = new PbTimeRanges();
        String qrtdate = "";
        try {


            con = pbTime.getConnection(measures.get(0));
            st = con.createStatement();
            rs2 = st.executeQuery(query);
//             PbReturnObject pbreturn = pbdb.execSelectSQL(query);
//
//     if(pbreturn.getRowCount()>0){
//         for(int k=0;k<pbreturn.getRowCount();k++){
//             qrtdate=pbreturn.getFieldValueString(k, 0);
//             break;
//         }
//     }
            if (rs2 != null) {
                if (rs2.next()) {
                    qrtdate = rs2.getString(1);

                }
            }

            rs2.close();
            st.close();
            con.close();

            rs2 = null;
            st = null;
            con = null;
        } catch (Exception e) {
            logger.error("Exception", e);
        }


//     con = pbtime.getConnection(measures.get(0));
//     if(pbreturn.getRowCount()>0){
//         for(int k=0;k<pbreturn.getRowCount();k++){
//             qrtdate=pbreturn.getFieldValueString(k, 0);
//             break;
//         }
//     }
        try {
            out = response.getWriter();
            out.print(qrtdate);
        } catch (IOException e) {
        }
        return null;
    }

    public ActionForward getQtrData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        PrintWriter out = null;
        Connection con = null;
        ResultSet rs2 = null;
        Statement st = null;
        ArrayList<String> measures = new ArrayList<String>();
        String reportid = request.getParameter("reportid");
        String gComp = request.getParameter("gComp");
        HttpSession session = request.getSession(false);

        String qtr = request.getParameter("selectedqtr");
        qtr = qtr.trim();
        PbDb pbdb = new PbDb();
        String qtryear = request.getParameter("selectedqtryear");
        if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportid) != null) {
                Container container = (Container) map.get(reportid);
                if (container != null) {
                    measures = (ArrayList) container.getTableMeasure();
                }
                PbReportCollection collect = new PbReportCollection();

                collect = container.getReportCollect();
                collect.Qtrtype = qtr;
                container.setNewUiqr(qtryear);
            }
        }
        String query = "";
//               if(gComp!=null && gComp.equalsIgnoreCase("MOM")){
//                    query="select PM_END_DATE from PR_DAY_DENOM where CM_END_DATE='"+qtr+"-"+qtryear+"'";
//               }
//               if(gComp!=null && gComp.equalsIgnoreCase("MOY")){
//                    query="select PYM_END_DATE from PR_DAY_DENOM where CM_END_DATE='"+qtr+"-"+qtryear+"'";
//               }
//               if(gComp!=null && gComp.equalsIgnoreCase("QOQ")){
//                    query="select PQ_END_DATE from PR_DAY_DENOM where CQ_END_DATE='"+qtr+"-"+qtryear+"'";
//               }
//               if(gComp!=null && gComp.equalsIgnoreCase("QOY")){
//                    query="select PYQ_END_DATE from PR_DAY_DENOM where CQ_END_DATE='"+qtr+"-"+qtryear+"'";
//               }
//               if(gComp!=null && gComp.equalsIgnoreCase("YOY")){
//                    query="select LY_END_DATE from PR_DAY_DENOM where CY_END_DATE='"+qtr+"-"+qtryear+"'";
//               }
        query = "select CQ_END_DATE from PR_DAY_DENOM where CQ_CUST_NAME='" + qtr + "-" + qtryear + "'";
        String qrtdate = "";
        try {
            con = ProgenConnection.getInstance().getConnectionForElement(measures.get(0));
            st = con.createStatement();
            rs2 = st.executeQuery(query);

            if (rs2 != null) {
                if (rs2.next()) {
                    qrtdate = rs2.getString(1);

                }
            }

            rs2.close();
            st.close();
            con.close();

            rs2 = null;
            st = null;
            con = null;
        } catch (Exception e) {
            logger.error("Exception", e);
        }


//     con = pbtime.getConnection(measures.get(0));
//     if(pbreturn.getRowCount()>0){
//         for(int k=0;k<pbreturn.getRowCount();k++){
//             qrtdate=pbreturn.getFieldValueString(k, 0);
//             break;
//         }
//     }
        try {
            out = response.getWriter();
            out.print(qrtdate);
        } catch (IOException e) {
        }
        return null;
    }

    public ActionForward getinformmeasure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = null;

        String reportid = request.getParameter("reportid");
        String measureid = request.getParameter("measureid");
        boolean isCustMeasure;
        ArrayList tableMeasures = new ArrayList();
        ArrayList qryelements = new ArrayList();
        ArrayList srchColumn1 = new ArrayList();
        // start of code by anitha
        ArrayList<String> rowviewlist = new ArrayList<String>();
        // end of code by anitha


        ArrayList srchValue1 = new ArrayList();
        ArrayList<String> headeralign = new ArrayList<String>();
        ArrayList<String> dataalign = new ArrayList<String>();
        ArrayList<Integer> rounding = new ArrayList<Integer>();
        ArrayList<String> priorsymbols = new ArrayList<String>();
        String sortValue1 = "";
        String sortElemnt1 = "";
        ArrayList srchConditions1 = new ArrayList();
        HttpSession session = request.getSession(false);
        HashMap<String, List> map1 = new HashMap<String, List>();
        ArrayList<String> customids = new ArrayList<String>();
        ArrayList<String> sortValue = new ArrayList<String>();
        ArrayList<String> sortElemnt = new ArrayList<String>();
        ArrayList<String> AoReport = new ArrayList<String>();
         ArrayList<String> refreshRep = new ArrayList<String>();
         ArrayList<String> DisplayColumns = new ArrayList<String>();
         ArrayList<String> DisplayLabels = new ArrayList<String>();
          String refreshReport = "";
          String[] timeDetails=new String[3];
        // String qtryear=request.getParameter("selectedqtryear");
        if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportid) != null) {
                Container container = (Container) map.get(reportid);
                PbReportCollection collect = new PbReportCollection();



                if (container != null) {
                    if(container.AOId.isEmpty()) {
                    } else {
                        AoReport.add(container.AOId);
                    }
                    sortValue1 = container.getSubtotalsort();
                    sortElemnt1 = container.getIdsubtotal();
                    refreshReport=container.getRefreshGr();
                    collect = container.getReportCollect();
                    ArrayList<String> timeinfo = collect.timeDetailsArray;
                    ArrayList timeList = new ArrayList();
                    timeList.add(timeinfo.get(3));
                    isCustMeasure = container.isFormulaMeasure("A_" + measureid);
                    String iscustom = String.valueOf(isCustMeasure);
                    tableMeasures = container.getTableMeasureNames();
                    qryelements = collect.reportQryElementIds;
                    // start of code by anitha
                    rowviewlist = container.getViewByElementIds();
                    // end of code by anitha
                    customids.add(iscustom);
                    sortValue.add(sortValue1);
                    refreshRep.add(refreshReport);
                    sortElemnt.add(sortElemnt1);
                    srchConditions1 = container.getSearchConditions();
                    srchColumn1 = container.getSearchColumns();
                    srchValue1 = container.getSearchValues();
                    DisplayColumns = container.getDisplayColumns();
                    DisplayLabels = container.getDisplayLabels();
                    headeralign.add(container.getMeasureAlign("A_" + measureid));
                    dataalign.add(container.getTextAlign("A_" + measureid));
                    rounding.add(container.measRoundingPrecisions.get("A_" + measureid));
                    priorsymbols.add(Boolean.toString(container.isSignSetForMeasure("A_" + measureid)));
                    map1.put("isCustMeasure", customids);
                    map1.put("tableMeasures", tableMeasures);
                    map1.put("qryelements", qryelements);
                   map1.put("refreshRep",refreshRep);
                    map1.put("srchConditions1", srchConditions1);
                    map1.put("srchColumn1", srchColumn1);
                    map1.put("srchValue1", srchValue1);
// start of code by anitha
                    map1.put("rowviewlist", rowviewlist);
                    map1.put("headeralign", headeralign);
                    map1.put("dataalign", dataalign);
                    map1.put("rounding", rounding);
                    map1.put("priorsymbols", priorsymbols);
// end of code by anitha
                    map1.put("sortValue", sortValue);
                    map1.put("sortElemnt", sortElemnt);
//code added by Bhargavi                    
                    map1.put("AoReport", AoReport);
                    map1.put("timeinfo", timeList);
                    map1.put("DisplayColumns", DisplayColumns);
                    map1.put("DisplayLabels", DisplayLabels);

                }

            }
        }
        Gson json = new Gson();
        String jsonString = json.toJson(map1);

        out = response.getWriter();
        out.print(jsonString);
        return null;
    }

    public ActionForward getAllViewByIdandName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        PrintWriter out = null;

        String reportid = request.getParameter("REPORTID");
        String measureid = request.getParameter("measureid");
        boolean isCustMeasure;
        ArrayList tableMeasures = new ArrayList();
        HttpSession session = request.getSession(false);
        HashMap<String, List> map1 = new HashMap<String, List>();
        ArrayList<String> allViewIdsNew = null;
        ArrayList<String> allViewNamesNew = null;
        // String qtryear=request.getParameter("selectedqtryear");
        if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            allViewIdsNew = (ArrayList<String>) session.getAttribute("allViewIds");
            allViewNamesNew = (ArrayList<String>) session.getAttribute("allViewNames");
            map1.put("allViewNamesNew", allViewNamesNew);
            map1.put("allViewIdsNew", allViewIdsNew);

        }
        Gson json = new Gson();
        String jsonString = json.toJson(map1);

        out = response.getWriter();
        out.print(jsonString);
        return null;
    }

    public ActionForward getLocalObjectMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PbReportViewerDAO pbDao = new PbReportViewerDAO();
        XtendReportMeta reportMeta = new XtendReportMeta();
        HashMap map = null;
        HashMap allViewBys = new HashMap();
        List allViewIds = new ArrayList<String>();
        List allViewNames = new ArrayList<String>();
        List rowViewIdList = new ArrayList<String>();
        List temprowViewIdList = new ArrayList<String>();
        List colViewIdList = new ArrayList<String>();
        List rowNamesLst = new ArrayList<String>();
        List temprowNamesLst = new ArrayList<String>();
        List colNamesLst = new ArrayList<String>();
        List runtimeMeasure = new ArrayList<String>();
        Gson gson = new Gson();
        String reportId = request.getParameter("REPORTID");
        String fromdesigner = request.getParameter("fromdesigner");
        String chartId = request.getParameter("chartId");
        Container container = null;
        container = Container.getContainerFromSession(request, reportId);
//        String fileLocation ="";
//        String runtimeMeasure = request.getParameter("runtimeMeasure");
//     if(chartId !=null){
        Type tarType1 = new TypeToken<Map<String, DashboardChartData>>() {
        }.getType();
        Map<String, DashboardChartData> chartData = gson.fromJson(request.getParameter("chartData"), tarType1);
        reportMeta = container.getReportMeta();
        DashboardChartData chartDetails = reportMeta.getChartData().get(chartId);
        if (chartData != null) {
            runtimeMeasure = chartDetails.getRuntimeMeasure();
        }
//     }
//             if(session != null){
//            fileLocation  = pbDao.getFilePath(session);
//        }else {
//            fileLocation = "/usr/local/cache";
//        }
//        if (fromdesigner == null) {
//            fromdesigner = "";
//        }

        List aggregationType = new ArrayList();
//

//          ReportObjectMeta reportMeta = new ReportObjectMeta();
//FileReadWrite readWrite = new FileReadWrite();

//String metaString = readWrite.loadJSON(fileLocation+"/analyticalobject/R_GO_"+reportId+".json");
//     Type tarType = new TypeToken<ReportObjectMeta>() {
//            }.getType();

//     reportMeta = gson.fromJson(metaString, tarType);

        ArrayList<String> allViewIdsAfter = new ArrayList<String>();
        ArrayList<String> allViewNamesAfter = new ArrayList<String>();
        rowViewIdList = reportMeta.getViewbysIds();
        rowNamesLst = reportMeta.getViewbys();
        for (int k = 0; k < rowViewIdList.size(); k++) {
            temprowViewIdList.add(rowViewIdList.get(k));
            temprowNamesLst.add(rowNamesLst.get(k));
        }

        allViewIds = temprowViewIdList;
        allViewNames = temprowNamesLst;
        List nameListName = reportMeta.getMeasures();
        List nameListIds = reportMeta.getMeasuresIds();

        for (int i = 0; i < nameListIds.size(); i++) {
            allViewNames.add(nameListName.get(i));
            allViewIds.add(nameListIds.get(i));
        }
//        allViewIdsAfter.addAll(0,reportMeta.getViewbysIds());
//        allViewIdsAfter.addAll(allViewIdsAfter.size(),reportMeta.getMeasuresIds());
        aggregationType = reportMeta.getAggregations();


        session.setAttribute("allViewIds", allViewIds);
        session.setAttribute("allViewNames", allViewNames);
        session.setAttribute("aggType", aggregationType);
        if (fromdesigner == null || fromdesigner.equalsIgnoreCase("")) {
            session.setAttribute("rowViewIdList", temprowViewIdList);
            session.setAttribute("rowMeasIdList", nameListIds);
            session.setAttribute("colViewIdList", colViewIdList);
            session.setAttribute("rowNamesLst", temprowNamesLst);
            session.setAttribute("rowMeasNamesLst", nameListName);
            if (chartId != null) {

                session.setAttribute("runtimeMeasNamesLst", runtimeMeasure);
            }
            session.setAttribute("colNamesLst", colNamesLst);
        }
        if (fromdesigner != null && !fromdesigner.equalsIgnoreCase("")) {
            if (allViewIds == null || allViewIds.isEmpty() || allViewBys.size() == 0) {
                PrintWriter out = null;
                out = response.getWriter();
                out.print("NoViewBys");

            }

        }
//            }

        return null;
    }

    public ActionForward getReportDefinitionDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        try {
            String reportId = (String) request.getParameter("reportId");
            String userId = (String) request.getParameter("userId");
            ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
            PrintWriter out;
            out = response.getWriter();
            out.flush();
            out.print(dao.getReportDefinitionDate(reportId));
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }

    public ActionForward GroupedMeasureCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String meaasureId = request.getParameter("measureId");
        ReportManagementDAO dao = new ReportManagementDAO();
        String childMeasure = dao.GroupedMeasureCheck(meaasureId);

        try {
            response.getWriter().print(childMeasure);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        return null;

    }
  public ActionForward ParentGroupedMeasuresCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
   //     String meaasureId = request.getParameter("measureId");
        ReportManagementDAO dao = new ReportManagementDAO();
        String groupMeasures = dao.ParentGroupedMeasuresCheck();

        try {
            response.getWriter().print(groupMeasures);
        } catch (IOException ex) {
           logger.error("Exception: ", ex);
        }
        return null;

    }
    public ActionForward getStartPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("user_Id");
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        String url = dao.getStartPAgeUrl(userId);
        try {
            response.getWriter().print(url);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
        return null;
    }
    //added by Dinanath for saving local  PbReportCollection object

    public ActionForward saveCollectObjectInLocalFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");
        String reportId = request.getParameter("reportId");
        String fileName = request.getParameter("fileName");
        String status = request.getParameter("status");
        String usrSavepointName = request.getParameter("usrSavepointName");
        HashMap map = new HashMap();
        Container container = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(reportId);
        PbReportCollection collection2 = null;
        //for serializing object into file
        ProgenReportViewerDAO dao = null;
        PbReportCollection collect = container.getReportCollect();
        ObjectOutputStream out = null;
        String filePath = null;
        FileOutputStream fileOut = null;
        String newFlag = "false";
        String overriteFlag = "false";
        String currenCollectSavepointPath = null;
        String collectfilename = null;
        String status1[] = null;
        String container_filename = null;
        String savepoint_id = null;
        String owriteSavepointName = null;
        boolean exceptionFlag = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        SimpleDateFormat formatter2db = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String createdDate = formatter.format(date);
        createdDate = createdDate.replace(" ", "_");
        String fileLocation = null;
        PbReportViewerDAO pbDao = new PbReportViewerDAO();
        if (session != null) {
            fileLocation = pbDao.getFilePath(session);
        } else {
            fileLocation = "/usr/local/cache";
        }
        try {
            File tempFile = null;
            filePath = fileLocation + File.separator + userId + File.separator + reportId;
            tempFile = new File(filePath);
            if (tempFile.exists()) {
            } else {
                tempFile.mkdirs();
            }
            status1 = status.split("_");
            if (fileName.equalsIgnoreCase("createNewSavePoint") && status1[0].equalsIgnoreCase("New")) {
                collectfilename = userId + "_" + reportId + "_" + createdDate + "_collect.ser";
                currenCollectSavepointPath = filePath + File.separator + collectfilename;
                fileOut = new FileOutputStream(currenCollectSavepointPath);
                out = new ObjectOutputStream(fileOut);
                out.writeObject(collect);
                newFlag = "true";
            } else if (status1[0].equalsIgnoreCase("Overrite")) {
                String bothfile[] = fileName.split("::");
                collectfilename = bothfile[0];
                container_filename = bothfile[1];
                savepoint_id = bothfile[2];
                owriteSavepointName = bothfile[3];
                currenCollectSavepointPath = filePath + File.separator + collectfilename;
                fileOut = new FileOutputStream(currenCollectSavepointPath);
                out = new ObjectOutputStream(fileOut);
                out.writeObject(collect);
                overriteFlag = "true";
            }
           // session.setAttribute("COLLECT_OBJPATH_" + userId + "_" + reportId, currenCollectSavepointPath);

        } catch (IOException i) {
            logger.error("Exception: ", i);
            exceptionFlag = true;
        } finally {
            if (out != null && fileOut != null) {
                try {
                    out.reset();
                    out.close();
                    fileOut.flush();
                    fileOut.close();
                } catch (IOException ex) {
                    logger.error("Exception: ", ex);
                }
            }
        }
        String currentContainerObjectPath = null;
        try {
            FileOutputStream fileOut2 = null;
            ObjectOutputStream out2 = null;
            filePath = fileLocation + File.separator + userId + File.separator + reportId;
            status1 = status.split("_");
            if (fileName.equalsIgnoreCase("createNewSavePoint") && status1[0].equalsIgnoreCase("New")) {
                container_filename = userId + "_" + reportId + "_" + createdDate + "_container.ser";
                currentContainerObjectPath = filePath + File.separator + container_filename;
                fileOut2 = new FileOutputStream(currentContainerObjectPath);
                out2 = new ObjectOutputStream(fileOut2);
                out2.writeObject(container);
            } else if (status1[0].equalsIgnoreCase("Overrite")) {
                currentContainerObjectPath = filePath + File.separator + container_filename;
                fileOut2 = new FileOutputStream(currentContainerObjectPath);
                out2 = new ObjectOutputStream(fileOut2);
                out2.writeObject(container);
            }
            out2.reset();
            out2.close();
            fileOut2.flush();
            fileOut2.close();
           // session.setAttribute("CONTAINER_OBJPATH_" + userId + "_" + reportId, currentContainerObjectPath);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
            exceptionFlag = true;
        }
        createdDate = formatter2db.format(date);
        String savepntname = null;

        if (newFlag.equalsIgnoreCase("true")) {
            dao = new ProgenReportViewerDAO();
            if (usrSavepointName != null && !usrSavepointName.isEmpty()) {
                savepntname = usrSavepointName;
            } else {
                savepntname = "SavePoint" + status1[1];
            }
            dao.saveCurrentLocalSavePointPath(userId, reportId, savepntname, currenCollectSavepointPath, collectfilename, currentContainerObjectPath, container_filename, "true", createdDate, savepoint_id);
        }
        if (overriteFlag.equalsIgnoreCase("true")) {
            dao = new ProgenReportViewerDAO();
            if (usrSavepointName != null && !usrSavepointName.isEmpty()) {
                savepntname = usrSavepointName;
            } else {
                savepntname = owriteSavepointName;
            }
            dao.updateCurrentLocalSavePointPath(userId, reportId, savepntname, currenCollectSavepointPath, collectfilename, currentContainerObjectPath, container_filename, "true", createdDate, savepoint_id);
        }
        if (exceptionFlag) {
            response.getWriter().print("Your Report Savepoint has not saved. Please try Again!!!");
        } else {
            response.getWriter().print("Your Report Savepoint has saved successfully");
        }
        return null;
    }
//added by Dinanath for get all available collect object in system for report

    public ActionForward getAllCollectObjectCreatedFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");
        String reportId = request.getParameter("reportId");
        String action = request.getParameter("action");
        ProgenReportViewerDAO dao = null;
        try {
            dao = new ProgenReportViewerDAO();
            String htmlContent = dao.getLocalCollectSavePointPath(userId, reportId, action, session);
            response.getWriter().print(htmlContent);
        } catch (Exception e) {
        }
        return null;
    }
//added by Dinanath for get all available collect object in system for report

    public ActionForward setDefaultCollectSavepoint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        HttpSession session = request.getSession(false);
        String userId = request.getParameter("userId");
        String reportId = request.getParameter("reportId");
        String savepointName = null;
        String savepointId = null;
        String fileLocation = null;
        PbReportViewerDAO pbDao = new PbReportViewerDAO();
        if (session != null) {
            fileLocation = pbDao.getFilePath(session);
        } else {
            fileLocation = "/usr/local/cache";
        }
        if (request.getParameter("savepointName").equalsIgnoreCase("Global Report")) {
            savepointName = request.getParameter("savepointName");
            session.setAttribute("CURRENT_COLLECT_OBJPATH", null);
            session.setAttribute("CURRENT_CONTAINER_OBJPATH", null);
        } else {
            String savepointNameandFileName[] = request.getParameter("savepointName").split("::");
            savepointName = savepointNameandFileName[0];
            String collectFile = savepointNameandFileName[1];
            String ContainerFile = savepointNameandFileName[2];
            savepointId = savepointNameandFileName[3];
            String filePath = fileLocation + File.separator + userId + File.separator + reportId;
            String currenCollectSavepointPath = filePath + File.separator + collectFile;
            String currentContainerObjectPath = filePath + File.separator + ContainerFile;
            session.setAttribute("CURRENT_COLLECT_OBJPATH", currenCollectSavepointPath);
            session.setAttribute("CURRENT_CONTAINER_OBJPATH", currentContainerObjectPath);
        }
        ProgenReportViewerDAO dao = null;
        try {
            dao = new ProgenReportViewerDAO();
            String result = dao.setDefaultCollectSavepoint(userId, reportId, savepointName, savepointId);
            response.getWriter().print(result);
        } catch (Exception e) {
        }
        return null;
    }
    //Added by Ram 18Desc2015 for adding measures as viewbys

    public ActionForward addMeasureAsViewBy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        int dispSeqNo = 0;
        boolean result = false;

        String userId = "";
        String rowViewByArray = request.getParameter("RowViewByArray");
        String[] rowIdArr = rowViewByArray.split(",");
        String colViewByArray = request.getParameter("ColViewByArray");
        String[] colIdArr = colViewByArray.split(",");
        String colViewNamesArr = request.getParameter("colViewNamesArr");
        String[] colNamesArr = colViewNamesArr.split(",");
        String rowViewNamesArr = request.getParameter("rowViewNamesArr");
        String[] rowNamesArr = rowViewNamesArr.split(",");
        String reportId = request.getParameter("reportId");
        ArrayList<String> rowIdArrayList = new ArrayList<String>();
        ArrayList<String> rowNameArrayList = new ArrayList<String>();
        Boolean flag = false;
        ArrayList<String> colViewByLst;
        if (colViewByArray != null && !"".equals(colViewByArray)) {
            colViewByLst = new ArrayList<String>(Arrays.asList(colIdArr));
        } else {
            colViewByLst = new ArrayList<String>();
        }
        ArrayList<String> colViewNamesLst;
        if (colViewNamesArr != null && !"".equals(colViewNamesArr)) {
            colViewNamesLst = new ArrayList<String>(Arrays.asList(colNamesArr));
        } else {
            colViewNamesLst = new ArrayList<String>();
        }

//By Ram
        ArrayList<String> rowViewIdList = new ArrayList<String>();
        PbReportCollection collect = new PbReportCollection();
        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
            }
        }
        collect = container.getReportCollect();
        rowViewIdList = collect.reportRowViewbyValues;
        int size = rowIdArr.length - rowViewIdList.size();
        String[] rowIdArray = new String[size];
        String[] rowNameArray = new String[size];
        int j = 0;
        for (int i = rowViewIdList.size(); i < rowIdArr.length; i++) {

            rowIdArray[j] = rowIdArr[i];
            rowNameArray[j] = rowNamesArr[i];
            j++;
        }
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        dispSeqNo = rowViewIdList.size() + 1;
        result = dao.insertMeasuresAsViewBys(reportId, rowNameArray, rowIdArray, dispSeqNo);
        if (result) {
            response.getWriter().println(result);
        }
        //Ended by Ram
        return null;
    }

    public ActionForward setTableListToContainerAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String repId = request.getParameter("repId");
        String tabLst = request.getParameter("tabLst");
        String[] tabListArray = null;
        ArrayList alsit = new ArrayList();
        Container container = null;
        if (tabLst != null && !tabLst.equalsIgnoreCase("")) {
            tabListArray = tabLst.split(",");
            for (int i = 0; i < tabListArray.length; i++) {
                alsit.add(tabListArray[i]);
            }
        }
        container = Container.getContainerFromSession(request, repId);
        container.setTableList(alsit);
        return null;
    }

    /**
     * *Added by Ashutosh for graphs save points**
     */
    public ActionForward setSavePoint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean savePoint = Boolean.parseBoolean(request.getParameter("savePoint"));
        XtendAdapter.savePoint = savePoint;
        return null;
    }

    /**
     * * Ended by Ashutosh**
     */
    public ActionForward createCustomMeasureForGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
         HashMap map = null;
        Container container = null;
        String elementId = request.getParameter("elementId");
        String reportId = request.getParameter("reportId");
        ProgenReportViewerBD bd = new ProgenReportViewerBD();
        HashMap<String, String> measureInfo = bd.createCustomMeasureForGraphs(elementId, reportId);
         if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
            }
        }
         //added by sruthi for complex and hybrid mes in graph 4/2/2016      
          XtendReportMeta reportMeta = new XtendReportMeta();
         reportMeta = container.getReportMeta();
    List nameListName = reportMeta.getMeasures();
    List nameListIds = reportMeta.getMeasuresIds();
    List aggregationType = reportMeta.getAggregations();
            nameListName.add(measureInfo.get("measureName"));
            nameListIds.add(measureInfo.get("elementId"));
            aggregationType.add(measureInfo.get("aggType"));
            reportMeta.setAggregations(aggregationType);
            reportMeta.setMeasuresIds(nameListIds);
            reportMeta.setMeasures(nameListName);
            //ended by sruthi
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(measureInfo));
        return null;
    }

    public ActionForward getComboGraphData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        GraphManagementDao dao = new GraphManagementDao();
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        String fileLocation = "";
        HttpSession session = request.getSession(false);
        if (session != null) {
            fileLocation = pbDAO.getFilePath(session);
        } else {
            fileLocation = "/usr/local/cache";
        }
        String dataInfo = dao.getComboGraphData(request, fileLocation);
        PrintWriter out = response.getWriter();
        out.print(dataInfo);
        return null;
    }
     public ActionForward getViwByIconMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PbReturnObject retobj = null;
        PbDb pbdb = new PbDb();
        Gson gson=new Gson();
        String[] viewByIds;
        viewByIds = request.getParameter("viewByIds").split(",");
        
        String query = "select ELEMENT_ID,VIEWBY_DATA,ICON_PATH from PRG_VIEWBY_IMAGE_MAPPING where ELEMENT_ID='" + viewByIds[0] + "'";
        Map<String,String> viewByIconMap=new HashMap<String,String>();
        try {
            retobj = pbdb.executeSelectSQL(query);
            if (retobj != null && retobj.getRowCount() > 0) {
                for (int i = 0; i < retobj.getRowCount(); i++) {
                    viewByIconMap.put(retobj.getFieldValueString(i, "VIEWBY_DATA"), retobj.getFieldValueString(i, "ICON_PATH"));
                }
            }
        } catch (SQLException ex) {
//            logger.error("Exception:", ex);
        }
        try {
            response.getWriter().print(gson.toJson(viewByIconMap));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ProgenReportViewerAction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return null;
    }
    //added by mohit for ao

    public ActionForward getAOBasedOnReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportBizRoles = request.getParameter("reportBizRoles");
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        HttpSession session = request.getSession(false);
        String result;

        try {
            if (session != null) {
                String fileLocation = new PbReportViewerDAO().getFilePath(session);
                result = dao.getAOBasedOnReport(reportBizRoles, fileLocation);
                response.getWriter().print(result);
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return null;
    }

    public ActionForward createAOForGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String aoId = request.getParameter("aoId");
        String REPORTID = request.getParameter("REPORTID");
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();

        String result;
        try {
            result = dao.createAOForGraph(aoId, REPORTID, request);
            if (!result.equalsIgnoreCase("Failed")) {
                dao.updateMasterForGraph(aoId, REPORTID);
            }
            response.getWriter().print(result);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return null;
    }
    //added by Dinanath for showing existing dim and measure of AO

    public ActionForward showExistingDimForEditAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String aoId = request.getParameter("aoId");
        String REPORTID = request.getParameter("REPORTID");
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        String result;
        try {
            result = dao.getExistingDimOrFactForAO(aoId, REPORTID, request);
            response.getWriter().print(result);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return null;
    }

    public ActionForward invalidateCache(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
            String userid = "";
            String fileLocation = "";
            String invalidateCache="";
            fileLocation = new PbReportViewerDAO().getFilePath(session);
            String deleterepids = request.getParameter("deleterepids");
            String usertype = request.getParameter("usertype");
             String forallusers = request.getParameter("forallusers");
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }
            if(usertype !=null && usertype.equalsIgnoreCase("ADMIN") && forallusers !=null && forallusers.equalsIgnoreCase("true") )
            {
                 File tempFile = new File(fileLocation + "/AO_Data");
                            File[] allusers = tempFile.listFiles();
                            if (allusers != null) {
                                for (File f1 : allusers) {
                                    invalidateCache = dao.invalidateCache(f1.getName(), deleterepids, fileLocation);
                                    }
                                    }
                
                
                
            }
            else
            {
                invalidateCache = dao.invalidateCache(userid, deleterepids, fileLocation);
            }
          
            try {
                response.getWriter().print(invalidateCache);
            } catch (IOException ex) {
                logger.error("Exception", ex);
            }
//         return mapping.findForward("repList");
            // return mapping.findForward("ReportStudio");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward rebuildCache(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
            String userid = "";
            String fileLocation = "";
             String invalidateCache="";
            fileLocation = new PbReportViewerDAO().getFilePath(session);
            String deleterepids = request.getParameter("deleterepids");
            String usertype = request.getParameter("usertype");
             String forallusers = request.getParameter("forallusers");
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }
            if(usertype !=null && usertype.equalsIgnoreCase("ADMIN") && forallusers !=null && forallusers.equalsIgnoreCase("true") )
            {
                 File tempFile = new File(fileLocation + "/AO_Data");
                            File[] allusers = tempFile.listFiles();
                            if (allusers != null) {
                                for (File f1 : allusers) {
                                    invalidateCache = dao.rebuildCache(f1.getName(), deleterepids, fileLocation, false, "");
                                    }
                                    }
                
                
                
            }
            else
            {
                  invalidateCache = dao.rebuildCache(userid, deleterepids, fileLocation, false, "");
            }
          
          
            try {
                response.getWriter().print(invalidateCache);
            } catch (IOException ex) {
                logger.error("Exception", ex);
            }
//         return mapping.findForward("repList");
            // return mapping.findForward("ReportStudio");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward currentSelectedTag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String tagId = request.getParameter("tagId");
        if (tagId != null && !tagId.equalsIgnoreCase("")) {
            session.setAttribute("currentSelectedTag", tagId);
        }
        return null;
    }
    //Start of code by bhargavi

    public ActionForward getcurrentYearDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        PrintWriter out;
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        String createdDate;

        createdDate = dao.getDateDetails(request);

        out = response.getWriter();
        out.print(createdDate);

        return null;
    }
    //Start of code by bhargavi
//added by mayank
    
    public ActionForward getGraphData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
          String reportId = request.getParameter("reportId");
//        String aoAsGoId = request.getParameter("aoAsGoId");
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        String fileLocation = "";
        HttpSession session = request.getSession(false);
        if (session != null) {
            fileLocation = pbDAO.getFilePath(session);
        } else {
            fileLocation = "/usr/local/cache";
}
        String reportName = request.getParameter("reportName");
        String fromoneview = request.getParameter("fromoneview");
//        String busrolename = request.getParameter("busrolename");
        String oneviewid = request.getParameter("oneviewID");
        String regid = request.getParameter("regid");
        String drillregid = request.getParameter("drillregid");
//       String reportName = request.getParameter("reportName");
        Container container = Container.getContainerFromSession(request, reportId);
//        ReportManagementDAO report = new ReportManagementDAO();
//       PbReportCollection collect = container.getReportCollect();
//     String bizzRoleId = container.getReportCollect().reportBizRoles[0];
//        String bizzRoleName = collect.getReportBizRoleName(bizzRoleId);
//        String bizzRoleName = "";

        String data = null;
        OnceViewContainer onecontainer1 = null;
        GraphManagementDao dao = new GraphManagementDao();
        if (fromoneview != null && fromoneview.equalsIgnoreCase("true")) {
//            String report1 = "";
//            String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
//            String advHtmlFileProps = (String) request.getSession(false).getAttribute("advHtmlFileProps");
//            String isseurity = (String) request.getSession(false).getAttribute("isseurity");
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//            PbReturnObject securityfilters = null;
//            securityfilters = (PbReturnObject) request.getSession(false).getAttribute("securityfilters");
            String fileName = reportTemplateDAO.getOneviewFileName(oneviewid);
            File file = null;

            ArrayList viewbys = new ArrayList();
            ArrayList viewbygblname = new ArrayList();
//            String viewvlaue = null;
            if (request.getParameter("viewbygblids") != null) {
                String[] viewbys1 = request.getParameter("viewbygblids").split(",");
                String[] viewbygblname1 = request.getParameter("viewbygblname").split(",");
                for (int i = 0; i < viewbys1.length; i++) {
//                    viewvlaue = viewbys1[0].replace("[", "").replace("]", "").replace("\"", "'").replace("'", "");
                    viewbys.add(viewbys1[i].replace("[", "").replace("]", "").replace("\"", "'").replace("'", ""));
                    viewbygblname.add(viewbygblname1[i].replace("[", "").replace("]", "").replace("\"", "'"));
                }
            }
//        request.getSession(false).setAttribute("selectglobalval", viewbygblname);
//            String bizzRoleName="";
//            PbDb pbdb = new PbDb();
            file = new File(oldAdvHtmlFileProps + "/" + fileName);
            if (file.exists()) {
                FileInputStream fis = null;
                {
                    ObjectInputStream ois = null;
                    try {
                        fis = new FileInputStream(oldAdvHtmlFileProps + "/" + fileName);
                        ois = new ObjectInputStream(fis);
                        try {
                            onecontainer1 = (OnceViewContainer) ois.readObject();
                        } catch (ClassNotFoundException ex) {
                            logger.error("Exception:", ex);
                        }
                        ois.close();
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    } finally {
                        try {
                            fis.close();
                        } catch (IOException ex) {
                            logger.error("Exception:", ex);
                        }
                        try {
                            ois.close();
                        } catch (IOException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                }
            }
            List<OneViewLetDetails> oneviewletDetails = onecontainer1.onviewLetdetails;

            for (int i = 0; i < oneviewletDetails.size(); i++) {
                OneViewLetDetails detail = oneviewletDetails.get(i);
                String regionid = detail.getNoOfViewLets();
                if (regionid != null && regionid.equalsIgnoreCase(regid)) {
//if( !detail.isOneviewReportTimeDetails()){
//try {
//    if( viewvlaue == null ? "" != null : !viewvlaue.equals("")){
//            data = dao.getChartsDataDrills(request);
//    }else{
//         PbReportViewerDAO dao1=new PbReportViewerDAO();
//       data = dao1.geroneviewcharts(reportId,reportName,container,detail.getOneviewId(),detail.getNoOfViewLets(),"false");
//  Map map = new HashMap();
//     Gson gson = new Gson();
//      Type tarType1 = new TypeToken<Map<String, String>>() {
//        }.getType();
//    map = gson.fromJson(data, tarType1);
//  data=  (String) map.get("data");
//    }
//        } catch (FileNotFoundException ex) {
//            logger.error("Exception:",ex);
//        }
//
//}else{
//    if(isseurity!=null && isseurity.equalsIgnoreCase("true")){
//                            try {
//                                request.setAttribute("isseurity", isseurity);
//                                dao.setgblfilter(request);
//                                data = dao.getChartsDataDrills(request);
//                            } catch (FileNotFoundException ex) {
//                                logger.error("Exception:",ex);
//                            }
//
//           }else{
                    PbReportViewerDAO dao1 = new PbReportViewerDAO();
                    try {
//                              Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
                        Type tarType1 = new TypeToken<Map<String, List<String>>>() {
                        }.getType();
                        Gson gson = new Gson();
                        if (request.getParameter("filterMap") != null) {
                            Map<String, List<String>> map = gson.fromJson(request.getParameter("filterMap"), tarType1);
                            request.setAttribute("filterMap", map);
                        }
//        if( viewvlaue == null ? "" != null : !viewvlaue.equals("")){
                        if (drillregid != null && drillregid.equalsIgnoreCase(regid)) {
                            data = "false";
                        } else {
                            data = dao.getChartsData(request, fileLocation);
                        }
                        if (data == null ? "false" == null : data.equals("false")) {
                            data = dao1.geroneviewcharts(reportId, reportName, container, detail.getOneviewId(), detail.getNoOfViewLets());
//                                    Map map = new HashMap();
////     Gson gson = new Gson();
//      Type tarType11 = new TypeToken<Map<String, String>>() {
//        }.getType();
//    map = gson.fromJson(data, tarType11);
//  data=  (String) map.get("data");
                        }
//                                }
                    } catch (Exception ex) {
                        logger.error("Exception:", ex);
                    }
//     if(data==null){
// data = dao1.getReports(reportId,reportName,container,fromoneview,busrolename);
//  Map map = new HashMap();
//     Gson gson = new Gson();
//      Type tarType1 = new TypeToken<Map<String, String>>() {
//        }.getType();
//    map = gson.fromJson(data, tarType1);
//  data=  (String) map.get("data");
//        }
//    }
//}
                }
            }

        } else {
//        String reportData = report.chartRequestHandlerDrills(request,bizzRoleName);

            try {
                data = dao.getChartsData(request, fileLocation);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.print(data);
//       PrintWriter out =null;
//        try {
//            response.getWriter().print(reportData);
//        } catch (IOException ex) {
//            logger.error("Exception:",ex);
//        }
        return null;
    }
    public ActionForward addNewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        String pageId = dao.addNewPage(request);
        response.getWriter().print(pageId);
        return null;
    }

    public ActionForward createFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        String pageId = dao.createFirstPage(request);
        response.getWriter().print(pageId);
        return null;
    }

    public ActionForward updatePageSequence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        HttpSession session = request.getSession(false);
        String pageId = request.getParameter("pageId");
        String reportId = request.getParameter("reportId");
        String userId = request.getParameter("usersId");
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        dao.updatePageSequence(reportId, pageId, userId);
        return null;
    }

    public ActionForward renamePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        
        String currentPage = request.getParameter("currentPage");
        if(currentPage ==null){
            currentPage = "default";
}
        if(currentPage!=null && currentPage.equalsIgnoreCase("")){
            currentPage = "default";
        }
        String reportId = request.getParameter("reportId");
        String pageLabel = request.getParameter("pageLabel");
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        dao.updatePageLabels(request,reportId,pageLabel,currentPage);
        return null;
    }
     //added by Dinanath as on 22 march 2016 for defining kpi alert scheduelrs
    public ActionForward defineKpiAlertSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);
            String chartMeta = request.getParameter("chartMeta");
            String chartData = request.getParameter("chartData");
            String operators = request.getParameter("operators");
    
            String reportName=request.getParameter("reportName");
            String kpialertname = request.getParameter("kpialertname");
            String measureType = request.getParameter("measureType");
            String operatorList1[] = request.getParameterValues("operators");
            String operatorList2 = request.getParameter("operatorsList");
            String operatorList[] = request.getParameter("operatorsList").split(",");
            String sValuesList[] = request.getParameter("sValuesList").split(",");
            String eValuesList[] = request.getParameter("eValuesList").split(",");
            String mailIdsList[] = request.getParameter("usermailtextareaList").split(",");
            String startDate = request.getParameter("startdate");
            String endDate = request.getParameter("enddate");
            String hours = request.getParameter("hours");
            String minutes = request.getParameter("minutes");
            String frequency = request.getParameter("frequency");
            String chartName = request.getParameter("chartName");
            String reportId = request.getParameter("reportId");
            String userId = request.getParameter("userId");
            //chart information

            String viewBys = request.getParameter("viewBys");
            String viewIds = request.getParameter("viewIds");
            String dimensions = request.getParameter("dimensions");
            String measName = request.getParameter("measName");
            String measId = request.getParameter("measId");
            String aggType = request.getParameter("aggType");
            String defaultMeasures = request.getParameter("defaultMeasures");
            String defaultMeasureIds = request.getParameter("defaultMeasureIds");
            String chartType = request.getParameter("chartType");
            String KPIName = request.getParameter("KPIName");
            Map<String, String> mapData = new HashMap<String, String>();
            Map<String, Map<String, String>> mainMapData = new HashMap<String, Map<String, String>>();
            mapData.put("viewBys", viewBys);
            mapData.put("viewIds", viewIds);
            mapData.put("dimensions", dimensions);
            mapData.put("measName", measName);
            mapData.put("measId", measId);
            mapData.put("aggType", aggType);
            mapData.put("defaultMeasures", defaultMeasures);
            mapData.put("defaultMeasureIds", defaultMeasureIds);
            mapData.put("chartType", chartType);
            mapData.put("KPIName", KPIName);
            mainMapData.put(chartName, mapData);
            //date format
            String weeklyparticularDay = request.getParameter("weeklyparticularDay");
            String monthlyParticularDay = request.getParameter("monthlyParticularDay");
            String hourlyParticularDay = request.getParameter("hourlyParticularDay");
            if (weeklyparticularDay == null) {
                weeklyparticularDay = "1";
}
            if (monthlyParticularDay == null) {
                monthlyParticularDay = "1";
            }
            if (hourlyParticularDay == null) {
                hourlyParticularDay = "1";
            }
            PbDb pbDB = new PbDb();
            PbReturnObject PbRetObj;
            PbReturnObject PbRetObj1;
            String sql1 = "select * from prg_ar_personalized_kpi where PRG_REPORT_CUST_NAME='" + kpialertname + "'";
            PbRetObj1 = pbDB.execSelectSQL(sql1);
            if (PbRetObj1.rowCount > 0) {
                logger.info("in already Exists");
                out.println("Scheduler with this name already exists! Please enter another name.");
            } else {

                Date sDate, eDate;
                DateFormat formatter;
                String value = "";
                String valu = "";
                String mont = "";
                String CurrValue = "";
                String value1 = "";
                String valu1 = "";
                String mont1 = "";
                String CurrValue1 = "";
                value = startDate;
                int slashval = value.indexOf("/");
                int slashLast = value.lastIndexOf("/");
                valu = value.substring(0, slashval);
                mont = value.substring(slashval + 1, slashLast + 1);
                CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                startDate = CurrValue;
                value1 = endDate;
                int slashval1 = value1.indexOf("/");
                int slashLast1 = value1.lastIndexOf("/");
                valu1 = value1.substring(0, slashval1);
                mont1 = value1.substring(slashval1 + 1, slashLast1 + 1);
                CurrValue1 = mont1.concat(valu1).concat(value1.substring(slashLast1));
                endDate = CurrValue1;

                formatter = new SimpleDateFormat("dd/MM/yyyy");//modified by Dinanath
                sDate = formatter.parse(startDate);
                eDate = formatter.parse(endDate);

                //end of date format
                ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
                String schedulerId = "";
                String ctxpath = request.getContextPath();
                String createdby = (String) session.getAttribute("USERNAME");
                schedulerId = dao.getKPISchedulerID(Integer.parseInt(reportId), kpialertname, userId, ctxpath, createdby, sDate, eDate);

                KPIAlertSchedule kpiSchedule = new KPIAlertSchedule();
                kpiSchedule.setKpiAlertSchedName(kpialertname);
                kpiSchedule.setKpiAlertSchedId(schedulerId);
                kpiSchedule.setReportName(reportName);
                kpiSchedule.setMeasureType(measureType);
                kpiSchedule.setOperatorValuesArr(operatorList);
                kpiSchedule.setStartValuesArr(sValuesList);
                kpiSchedule.setEndValuesArr(eValuesList);
                kpiSchedule.setMailIdsArr(mailIdsList);
                kpiSchedule.setStartDate(sDate);
                kpiSchedule.setEndDate(eDate);
                kpiSchedule.setScheduledTime(hours + ":" + minutes);
                kpiSchedule.setFrequency(frequency);
                kpiSchedule.setChartName(chartName);
                kpiSchedule.setReportId(reportId);
                kpiSchedule.setUserId(userId);
                kpiSchedule.setIsFromKPIChart(true);
                kpiSchedule.setChartMapData(mainMapData);
                if (frequency.equalsIgnoreCase("Weekly")) {
                    kpiSchedule.setParticularDay(weeklyparticularDay);
                } else if (frequency.equalsIgnoreCase("Monthly")) {
                    kpiSchedule.setParticularDay(monthlyParticularDay);
                } else if (frequency.equalsIgnoreCase("Hourly")) {
                    kpiSchedule.setParticularDay(hourlyParticularDay);
                }
                String filePath = null;
                PbReportViewerDAO pbDAO = new PbReportViewerDAO();
                if (session != null) {
                    filePath = pbDAO.getFilePath(session);
                } else {
                    filePath = "/usr/local/cache";
                }
                kpiSchedule.setGlobalFilePath(filePath);
                kpiSchedule.setContextPath(ctxpath);
                //updating scheduler details                 
                dao.updateKPISchedulerDetails(schedulerId, kpiSchedule);

                SchedulerBD bd = new SchedulerBD();
                bd.scheduleKPIChart(kpiSchedule, false);

                out.println(kpialertname + " has scheduled successfully. ");
            }
        } catch (Exception e) {
            logger.error("Exception is :", e);
            out.println("Something went wrong! Please try again " + e);
        }
        return null;
    }
    
public ActionForward getPeriodDataFlag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
        String reportId = request.getParameter("reportId");
        String periodType = request.getParameter("periodType");
          ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
          String flag = "";
          if(periodType !=null && (periodType.equalsIgnoreCase("qtr") || periodType.equalsIgnoreCase("year"))){
              
          flag = dao.getDataFlag(reportId);
}
         response.getWriter().print(flag.toLowerCase());
        return null;
    }
    //added by Dinanath for send now kpi alerts scheduler
     public ActionForward sendKPIChartSchedulerNow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        KPIAlertSchedule schedule=null;
        try{
        HttpSession session = request.getSession(false);
        String schedulerId = request.getParameter("schedulerId");
        String reportId = request.getParameter("reportId");
        PbReportViewerDAO dao = new PbReportViewerDAO();
        
        schedule = dao.getKPIChartSchedulerDetails(schedulerId);
  
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String startDate = dateFormat.format(cal.getTime()).toString().substring(0, 10);
        String endDate = startDate;
        String fullDate = dateFormat.format(cal.getTime()).toString();
        String timeArrTmp[] = fullDate.split(" ");
        String timeArr[] = timeArrTmp[1].toString().split(":");
        String requestUrl1 = request.getRequestURL().toString();//added by sruhti for schedular
        String hrs = timeArr[0];
        String mins = timeArr[1];
        int hrsT = Integer.parseInt(hrs);
        int minsT = Integer.parseInt(mins);
        minsT = minsT + 1;
        String mailIds[] = request.getParameter("emailIds").split(",");
        String usrMesg = request.getParameter("usrMesg");
        String scheduledTime = Integer.toString(hrsT).concat(":").concat(Integer.toString(minsT));
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        Date sDate, eDate;
        DateFormat formatter;
        String value = "";
        String valu = "";
        String mont = "";
        String CurrValue = "";
        String value1 = "";
        String valu1 = "";
        String mont1 = "";
        String CurrValue1 = "";
//        value = startDate;
//        int slashval = value.indexOf("/");
//        int slashLast = value.lastIndexOf("/");
//        valu = value.substring(0, slashval);
//        mont = value.substring(slashval + 1, slashLast + 1);
//        CurrValue = mont.concat(valu).concat(value.substring(slashLast));
//        startDate = CurrValue;
//
//        value1 = endDate;
//        int slashval1 = value1.indexOf("/");
//        int slashLast1 = value1.lastIndexOf("/")+1;
//        valu1 = value1.substring(0, slashval1);
//        mont1 = value1.substring(slashval1 + 1, slashLast1 + 1);
//        int day = Integer.parseInt(valu1);
//        value1 = Integer.toString(day+1);
//        CurrValue1 = mont1.concat(valu1).concat(value1.substring(slashLast1));
//        endDate = CurrValue1;

        String endDateArr[] = endDate.split("/");
        String months = endDateArr[0].toString();
        String days = endDateArr[1].toString();
        String years = endDateArr[2].toString();
        int daysVal = Integer.parseInt(days);
        daysVal++;
        days = Integer.toString(daysVal);
        if (days.length() == 1) {
            days = "0" + days;
        }
        endDate = months + "/" + days + "/" + years;
        formatter = new SimpleDateFormat("MM/dd/yyyy");
        sDate = formatter.parse(startDate);
        eDate = formatter.parse(endDate);
        schedule.setMailIdsArr(mailIds);
//        schedule.setUserMessage(usrMesg);
        schedule.setScheduledTime(scheduledTime);
        schedule.setKpiAlertSchedId(schedulerId);
        schedule.setStartDate(sDate);
        schedule.setEndDate(eDate);
        schedule.setReportId(reportId);
        schedule.setIsFromKPIChart(true);
        String filePath = null;
        PbReportViewerDAO pbDAO = new PbReportViewerDAO();
        if (session != null) {
            filePath = pbDAO.getFilePath(session);
        } else {
            filePath = "/usr/local/cache";
        }
        schedule.setGlobalFilePath(filePath);
        schedule.setUserId((request.getSession(false).getAttribute("USERID")).toString());
        String frequency = schedule.getFrequency();
//        reportDAO.updateSchedulerDetails(schedulerId, schedule);
        SchedulerBD bd = new SchedulerBD();
        //bd.scheduleReport(schedule, false);
        bd.scheduleKPIChart(schedule, false);
        }catch(Exception e){
            
        }
        return null;
    }
     public ActionForward saveGlobalDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
{
    GraphManagementDao dao = new GraphManagementDao();
    dao.saveGlobalDateGraph(request);
    return null;
}
     public ActionForward removePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
        
        String currentPage = request.getParameter("currentPage");
        String reportId = request.getParameter("reportId");
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
        String result=dao.removePage(request,reportId,currentPage);
        PrintWriter out = response.getWriter();
        out.print(result);
        return null;
}
      public ActionForward defineAOSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession(false);

            String aoname = request.getParameter("AOname");
            String schedulerId = request.getParameter("schedulerId");

            String loadType = request.getParameter("loadType");
            String mailIds = request.getParameter("usermailtextarea");

            String selectedDateType = request.getParameter("dateType");
            String loadDate = request.getParameter("SelectedDate");
            String loadStartDate = request.getParameter("sDate");
            String loadEndDate = request.getParameter("eDate");

            String startDate = request.getParameter("startdate");
            String endDate = request.getParameter("enddate");

            String hours = request.getParameter("hrs");
            String minutes = request.getParameter("mins");
            String frequency = request.getParameter("frequency");

            //date format
            String weeklyparticularDay = request.getParameter("particularDay");
            String monthlyParticularDay = request.getParameter("monthParticularDay");
            //  String hourlyParticularDay = request.getParameter("hourlyParticularDay");
            if (weeklyparticularDay == null) {
                weeklyparticularDay = "1";
            }
            if (monthlyParticularDay == null) {
                monthlyParticularDay = "1";
            }
           
            
            if (selectedDateType.equalsIgnoreCase("Normal_Date")) {
            loadStartDate=loadDate;
            loadEndDate=loadDate;
            }

            Date sDate, eDate;
            String value = "";
            String valu = "";
            String mont = "";
            String CurrValue = "";
            String value1 = "";
            String valu1 = "";
            String mont1 = "";
            String CurrValue1 = "";
            value = startDate;
            int slashval = value.indexOf("/");
            int slashLast = value.lastIndexOf("/");
            valu = value.substring(0, slashval);
            mont = value.substring(slashval + 1, slashLast + 1);
            CurrValue = mont.concat(valu).concat(value.substring(slashLast));
            startDate = CurrValue;
            value1 = endDate;
            int slashval1 = value1.indexOf("/");
            int slashLast1 = value1.lastIndexOf("/");
            valu1 = value1.substring(0, slashval1);
            mont1 = value1.substring(slashval1 + 1, slashLast1 + 1);
            CurrValue1 = mont1.concat(valu1).concat(value1.substring(slashLast1));
            endDate = CurrValue1;

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            sDate = formatter1.parse(startDate);
            eDate = formatter1.parse(endDate);


            AOSchedule aoSchedule = new AOSchedule();

            aoSchedule.setAOSchedName(aoname);
            aoSchedule.setAOSchedId(schedulerId);

            aoSchedule.setMailIds(mailIds);
            aoSchedule.setLoadType(loadType);
            aoSchedule.setStartDate(sDate);
            aoSchedule.setEndDate(eDate);
            aoSchedule.setStartDateString(startDate);
            aoSchedule.setEndDateString(endDate);
            aoSchedule.setDateType(selectedDateType);
            aoSchedule.setloadStartDate(loadStartDate);
            aoSchedule.setloadEndDate(loadEndDate);
            aoSchedule.setScheduledTime(hours + ":" + minutes);
            aoSchedule.setFrequency(frequency);
            aoSchedule.setIsAO(true);
            if (frequency.equalsIgnoreCase("Weekly")) {
                aoSchedule.setParticularDay(weeklyparticularDay);
            } else if (frequency.equalsIgnoreCase("Monthly")) {
                aoSchedule.setParticularDay(monthlyParticularDay);
            }
            
            //updating scheduler details   
            ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
             dao.updateAOSchedulerDetails(schedulerId, aoSchedule);
            
            SchedulerBD bd = new SchedulerBD();
            bd.scheduleAO(aoSchedule, false);

            out.println(aoname + " has scheduled successfully. ");

        } catch (Exception e) {
            logger.error("Exception is :", e);
            out.println("Something went wrong! Please try again " + e);
        }
        return null;
    }

    public ActionForward validateScheduleAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PrintWriter out = response.getWriter();

        String AOid = request.getParameter("scheduleAOid");
        String validateString="";
        PbDb pbDB = new PbDb();
        PbReturnObject PbRetObj1;
        String sql1 = "select SCHEDULE_LOAD_TYPE, AO_NAME from PRG_AR_AO_MASTER where AO_ID='" + AOid + "'";
        PbRetObj1 = pbDB.execSelectSQL(sql1);
        
           validateString = PbRetObj1.getFieldValue(0, 0) + "," + PbRetObj1.getFieldValue(0, 1);
       out.print(validateString);

        return null;
    }
    public ActionForward getComparisionWithRTMesaure(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
         String reportId = request.getParameter("reportId");
         String elementid = request.getParameter("elementid");
         String actualRow = request.getParameter("baselemenetid");
         HttpSession session = request.getSession(false);
         if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportId) != null) {
                Container container = (Container) map.get(reportId);
                if (container != null) {
                    DataFacade datafacade = new DataFacade(container); 
                    //datafacade.isSignApplicable(actualRow);
                    if(elementid==null||elementid.equalsIgnoreCase("")){
                        container.resetSignsForMeasure();
                        container.setrtMeasureCompareWith("A_"+actualRow, "reset");
                    }else{
                    container.setMeasureType(elementid,"Standard");
                    ColorHelper signHelper = new ColorHelper();
                    signHelper.setDependentMeasure(elementid);
                    signHelper.setEvaluationMethod("RTCompare");
                    container.setSignForMeasure("A_"+actualRow, signHelper);
                    container.setrtMeasureCompareWith("A_"+actualRow, elementid);
                }
            }
        }
        }
         return null;
     }
    public ActionForward setMgtmlTemplateFlag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
        ProgenReportViewerDAO dao = new ProgenReportViewerDAO();
         String reportId=request.getParameter("reportId");
         String flag=request.getParameter("flag");
        dao.setMgtmlTemplateFlag(reportId,flag);
        
        return null;
}
    public ActionForward getComparedWithRT(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
        String element_id = request.getParameter("colName");
        PrintWriter out = response.getWriter();
        String reportId = request.getParameter("reportid");
        String compWith = "false";
        String eleId = element_id.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                                .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_PYtdrank", "").replace("_PQtdrank", "").replace("_PMtdrank", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PYMTD", "").replace("_PYQTD", "");
        HttpSession session = request.getSession(false);
        ColorHelper signHelper = new ColorHelper();
         if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportId) != null) {
                Container container = (Container) map.get(reportId);
                if (container != null) {
                    signHelper = container.getSignForMeasure(eleId);
                    if(signHelper!=null){
                    String dependentMeasure = signHelper.getDependentMeasure();
                    if(dependentMeasure!=null&& !dependentMeasure.isEmpty()&& element_id.equalsIgnoreCase(dependentMeasure)){
                      compWith = "true";
                    }else{
                      compWith = "false";  
}
                    }
                }
            }
          }
         out.print(compWith);
        return null;
    }
    
      //sandeep
    public ActionForward getapplyedfilters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
         String reportId = request.getParameter("reportId");
         String elementid = request.getParameter("elementid");
         String actualRow = request.getParameter("baselemenetid");
         HttpSession session = request.getSession(false);
     ArrayList<String> elmentids=(ArrayList<String>) session.getAttribute("parameterlistold");
        ArrayList<String> elmentidnmaes=(ArrayList<String>) session.getAttribute("viewbynamesold");
          Gson gson = new Gson();
     ArrayList<String> elmentids1= new ArrayList();
        ArrayList<String> elmentidnmaes1= new ArrayList();
        ArrayList<String> completeContent1= new ArrayList();
        
        Type tarType1 = new TypeToken<Map<String, List<String>>>() {
                }.getType();
                 Map<String, List<String>>  inmap = gson.fromJson(request.getParameter("infilters"), tarType1);
                  Map<String, List<String>> notinmap = gson.fromJson(request.getParameter("notinfilters"), tarType1);
        
 if(inmap!=null){
   for (String key : inmap.keySet()) {
        List<String> selectedvlaues;
       
        if(inmap.get(key).size()>0){
       selectedvlaues=(List<String>) inmap.get(key);
       if(selectedvlaues!=null && !selectedvlaues.isEmpty() && selectedvlaues.size()>0){
           elmentids1.add(key);
           int index=elmentids.indexOf(key);
           String elementnae=elmentidnmaes.get(index);
           elmentidnmaes1.add(elementnae);
}
   }
   }
    }
 if(notinmap!=null){
   for (String key : notinmap.keySet()) {
        List<String> selectedvlaues;
           if(notinmap.get(key).size()>0){
       selectedvlaues=(List<String>) notinmap.get(key);
       if(selectedvlaues!=null && !selectedvlaues.isEmpty() && selectedvlaues.size()>0){
        if(!elmentids1.contains(key)){
           elmentids1.add(key);
           int index=elmentids.indexOf(key);
           String elementnae=elmentidnmaes.get(index);
           elmentidnmaes1.add(elementnae);
       }
       }
       }
   }
    }
 for(int i=0;i<elmentids.size();i++){
      if(!elmentids1.contains(elmentids.get(i))){
           elmentids1.add(elmentids.get(i));
        
           elmentidnmaes1.add(elmentidnmaes.get(i));
       }
     
 }
 if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportId) != null) {
                Container container = (Container) map.get(reportId);
                if (container != null) {
                    
//                    container.setviewbynamesnew(elmentidnmaes1);
//                    container.setparameterlistnew(elmentids1);
                }
                
            }
 }
  StringBuilder completeContent = new StringBuilder();
  Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
      
        List<String> parameterlist = new ArrayList<String>();
        List<String> viewbynames = new ArrayList<String>();
   Map<String, List<String>> allFiltersnames = new HashMap<String, List<String>>();
 
  allFiltersnames = (Map<String, List<String>>) session.getAttribute("allFiltersnames");
        allFilters = (Map<String, List<String>>) session.getAttribute("allFilters");
        parameterlist = elmentids1;
        viewbynames = elmentidnmaes1;
  ArrayList  viewbynamesold1=new ArrayList<String>();ArrayList parameterlistold1=new ArrayList<String>();
             
                            String   graphselecteddata=(String)session.getAttribute("graphselectdata");
                                  if (viewbynames != null && !viewbynames.isEmpty() && allFiltersnames != null && !allFiltersnames.isEmpty()) {
                                        StringBuilder stringbuilder = new StringBuilder();
                                      
                                        Set keySet1 = allFiltersnames.keySet();
                                        List<String> filterids = new ArrayList<String>();

    int size = 6;
                          if(graphselecteddata!=null && !graphselecteddata.isEmpty()){
                              size=Integer.parseInt(graphselecteddata);
                          }else{
                                 if (viewbynames != null && !viewbynames.isEmpty() && viewbynames.size() >= 6) {
                                
				 size =6;
                                } else {
                                    size = viewbynames.size();
                                   
                                }}
                                        String key1;
                                        int i1 = 0;
                                        int setgblflag = 0;String selectidgraph;
                                        Set keySet = allFilters.keySet();
                                        Iterator itr = keySet.iterator();
                                        String key;
                                        if(size>0){
                                        for (int j1 = 0; j1 < size; j1++) {
                                            List<String> parameterlistNames = new ArrayList<String>();
                                            Iterator itr1 = keySet1.iterator();
                                            if (setgblflag == j1) {
                                                String idgbl = "globalfilterrow" + setgblflag;
                              
                                 
                             completeContent.append("<div  id="+idgbl+" class='item' style='vertical-align: top;margin: 0px;padding: 0px 5px;'>");
                                   setgblflag = setgblflag + 6;
                                        }
                                          
                                           // alert('<%=viewbynamesold%>')
                                           
                                       
                                            boolean runtimeflag=false;
                                            key1 =viewbynames.get(j1).toString();
 // for (var i in keys){
                       // var elemntid = keys[i];
                       
  
                                          
                                          
                                          
                                                key1 = key1.toString().replace(" ", "1q1");
selectidgraph=parameterlist.get(i1)+"__"+key1;
                                  
                                                // alert('<%=viewbynamesold%>')
                                     completeContent.append("<select name="+parameterlist.get(i1)+" id="+selectidgraph+" multiple style=''>");
 completeContent.append("</select>");
                                       runtimeflag=false;
                                                
                                        
                                        
                                        i1++;
                            if ((setgblflag - 1) == j1) {

                                  completeContent.append("</div>");
                                 }
                                        }}

                                 
                                        
}
 completeContent1.add(completeContent.toString());
 
  HashMap<String, List> map1 = new HashMap<String, List>();
   map1.put("viewbynamesnew", elmentidnmaes1);
                    map1.put("parameterlistnew", elmentids1);
                    map1.put("completeContent", completeContent1);
 session.setAttribute("viewbynamesnew", elmentidnmaes1);
 session.setAttribute("parameterlistnew", elmentids1);
 session.setAttribute("viewbynamesold", elmentidnmaes1);
 session.setAttribute("parameterlistold", elmentids1);
  PrintWriter out = response.getWriter();
//              out.print(completeContent.toString());
                Gson json = new Gson();
        String jsonString = json.toJson(map1);

        out = response.getWriter();
        out.print(jsonString);
return null;
     }
     //added  by sruthi for advance filters 
    public ActionForward getAdvanceFilters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession(false);
          StringBuilder str=new StringBuilder();
         PrintWriter out = response.getWriter();
        String reportid=request.getParameter("reportid");
        String elementid=request.getParameter("elementid");
        String elementname=request.getParameter("elementname");
        String reportstatus="";
           if (session.getAttribute("PROGENTABLES") != null) {
                 HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                  if (map.get(reportid) != null) {
                      Container container = (Container) map.get(reportid);
                        PbReportCollection collect = container.getReportCollect();
                      if(container!=null){
                        DataFacade datafacade = new DataFacade(container);
                        ArrayList<String> measurenames=null;
                          HashMap TableHashMap = null;
                           ArrayList<String> MeasuresNames=new ArrayList<String>();
                            ArrayList<String> Measureid=new ArrayList<String>();
                            ArrayList<String> list=new ArrayList<String>();
                             HashMap<String,ArrayList<String>> datalist=new HashMap<String,ArrayList<String>>();
                             datalist=container.getadvdatalist();
                             if(datalist!=null && !datalist.isEmpty() && datalist.containsKey(elementid))
                                 list=datalist.get(elementid);
                              TableHashMap = container.getTableHashMap();
                            MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
                             Measureid=(ArrayList) TableHashMap.get("Measures");
                        str.append("<table style=\"height: 58%; width: 39%;\">");
                        str.append("<tr style=\"height: 30%;\"><td style=\"width: 50%;\">Measures</td>");
                        str.append("<td style=\"float: left; width: 58%; margin-left: 3em; margin-top: 1em;\"><select id='selectedmeasure'>");
                        for(int i=0;i<Measureid.size();i++){
                            if(list!=null && !list.isEmpty() && list.get(0).contains(Measureid.get(i)))
                            str.append(" <option id="+Measureid.get(i)+" value="+Measureid.get(i)+" selected>"+MeasuresNames.get(i)+"</option>");
                            else
                            str.append(" <option id="+Measureid.get(i)+" value="+Measureid.get(i)+">"+MeasuresNames.get(i)+"</option>");
                        }
                        str.append("</select></td>");
                        str.append("</tr>");
                        str.append("<tr style=\"height: 17%;\"><td>LogicalOperation</td>");
                        str.append("<td style=\"margin-top: 1em; width: 30%; float: left; margin-left: 3em;\"><select id='logicaloperation'>");
                         if(list!=null && !list.isEmpty()){
                             if(!list.get(2).equalsIgnoreCase("TOP"))
                             {
                                 str.append("<option id=TOP value=BTM>BTM</option>");
                                  str.append("<option id=BTM value=TOP>TOP</option>");
                             }else{
                                  str.append("<option id=TOP value=TOP>TOP</option>");
                                  str.append("<option id=BTM value=BTM>BTM</option>");
                             }
                             }else{
                        str.append("<option id=TOP value=TOP>TOP</option>");
                                str.append("<option id=BTM value=BTM>BTM</option>");
                         }
                        str.append("</select>");
                        str.append("<td style=\"width: 30%; float: left; margin-top: 1em;\">");
                         if(list!=null && !list.isEmpty())
                        str.append("<input id='selectedval'type=text value="+list.get(1)+">");
                         else
                        str.append("<input id='selectedval'type=text value=''>");
                        str.append("</td>");
                        str.append("</tr>");
                        String display="none";
                        if(collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")){
                            reportstatus="standard";
                            if(list!=null && !list.isEmpty() && list.get(3).equalsIgnoreCase("reportdate")){
                        str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='reportdate' name='reportdate1' value='reportdate' checked>Report Date</td></tr>");
                        str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='customdate' name='reportdate1'  onclick=parent.opencustomdates()>Custom Date</td></tr>");
                        
                            }else  if(list!=null && !list.isEmpty() && !list.get(3).equalsIgnoreCase("reportdate")){
                         str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='reportdate' name='reportdate1' value='reportdate' >Report Date</td></tr>");
                str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='customdate' name='reportdate1'  onclick=parent.opencustomdates() checked>Custom Date</td></tr>");
                             display="";
                            }else{
                            str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='reportdate' name='reportdate1' value='reportdate' checked>Report Date</td></tr>");
                        str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='customdate' name='reportdate1'  onclick=parent.opencustomdates()>Custom Date</td></tr>");
      
                                
                            }
                           
                            // else
                             //str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='customdate' name='reportdate'  onclick=parent.opencustomdates() >Custom Date</td></tr>");
                        str.append("<table id='customtable' style=\"height: 20%;display:"+display+"; border:1px solid lightgray; width: 90%;\">");
                               if(list!=null && !list.isEmpty() && list.get(3).equalsIgnoreCase("Yesterday"))             
                             str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='Yesterday' name='reportdate'  value='Yesterday' checked>Yesterday</td></tr>");
                            else
                            str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='Yesterday' name='reportdate'  value='Yesterday'>Yesterday</td></tr>");       
                              if(list!=null && !list.isEmpty() && list.get(3).equalsIgnoreCase("Today"))             
                               str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='Today' name='reportdate' value='Today' checked>Today</td></tr>");
                               else
                             str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='Today' name='reportdate' value='Today' >Today</td></tr>");      
                             if(list!=null && !list.isEmpty() && list.get(3).equalsIgnoreCase("Tomorrow"))              
                              str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='Tomorrow' name='reportdate' value='Tomorrow' checked>Tomorrow</td></tr>");
                             else
                              str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='Tomorrow' name='reportdate' value='Tomorrow' >Tomorrow</td></tr>");   
                             if(list!=null && !list.isEmpty() && list.get(3).equalsIgnoreCase("Last Month End Date"))              
                             str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='LMED' name='reportdate' value='Last Month End Date' checked>Last Month End Date</td></tr>");
                            else
                              str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='LMED' name='reportdate' value='Last Month End Date' >Last Month End Date</td></tr>");    
                              if(list!=null && !list.isEmpty() && list.get(3).equalsIgnoreCase("Last Qtr End Date"))              
                             str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='LQED' name='reportdate' value='Last Qtr End Date' checked>Last Qtr End Date</td></tr>");
                             else
                             str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='LQED' name='reportdate' value='Last Qtr End Date' >Last Qtr End Date</td></tr>");     
                             if(list!=null && !list.isEmpty() && list.get(3).equalsIgnoreCase("Last Year End Date"))              
                              str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='LYED' name='reportdate'  value='Last Year End Date' checked>Last Year End Date</td></tr>");
                              else
                               str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='LYED' name='reportdate'  value='Last Year End Date'>Last Year End Date</td></tr>");  
                                  String text="";
                                    if(list!=null && !list.isEmpty())
                                    text= list.get(3).toString();
                             String value="";
                             String sign="";
                             if(!text.isEmpty() && text!=null && text.contains("_")){
                                 String[] arrtext=list.get(3).split("_");
                                 sign=arrtext[0];
                                 value=arrtext[1];
                             }
                             str.append("<tr style=\"height: 17%;\"><td>System Date</td><td><select id='systemdate'>");
                            if(!sign.isEmpty() && sign!=null && sign.equalsIgnoreCase("plus")){
                             str.append("<option  value=plus selected>+</option>");
                             str.append("<option  value=minus>-</option>");
                            }else if(!sign.isEmpty() && sign!=null && sign.equalsIgnoreCase("minus")){
                                 str.append("<option  value=minus selected>-</option>");
                          str.append("<option  value=plus>+</option>");
                            }else{
                                str.append("<option  value=plus selected>+</option>");
                             str.append("<option  value=minus>-</option>");
                            }
                          str.append("</select></td>");
                          if(!value.isEmpty() && value!=null)
                          str.append("<td><input type='text'  id='textid' value="+value+">Days</td>");
                          else
                          str.append("<td><input type='text'  id='textid' value=''>Days</td>");   
                        
                          str.append("</tr>");
                        str.append("</table>");
                        }else{
                            reportstatus="range";
                       str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='reportdate' name='reportdate' value='reportdate' checked>Report Date</td></tr>");
                       str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='customdate' name='reportdate'  onclick=parent.opencustomdates() >Custom Date</td></tr>");
                       str.append("<table id='customtable' style=\"height: 20%;display:none; border:1px solid lightgray; width: 90%;\">");
                            
                        str.append("<tr ><td>For From Date</td></tr>");
                        str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='fromToday' name='fromdate' value='Yesterday' checked>Yesterday</td><td><input type='radio'  id='fromTomorrow' name='fromdate' value='Tomorrow' >Tomorrow</td></tr>");
                     //   str.append("<tr style=\"height: 17%;\"><td>System Date</td><td><select id='systemdate'>");
                      //  str.append("<option  value=plus>+</option>");
                      //  str.append("<option  value=minus>-</option></select></td><td><input type='text'  id='textid' value=''>Days</td></tr>");
                    
                         
                         str.append("<tr ><td>For To Date</td></tr>");
                       str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='forToday' name='todate' value='Yesterday' >Yesterday</td><td><input type='radio'  id='forTomorrow' name='todate' value='Tomorrow' checked>Tomorrow</td></tr>");
                       //  str.append("<tr style=\"height: 17%;\"><td>System Date</td><td><select id='systemdate'>");
                       //   str.append("<option  value=plus>+</option>");
                       //  str.append("<option  value=minus>-</option></select></td><td><input type='text'  id='textid' value=''>Days</td></tr>");
                    
                         
                         str.append("<tr ><td>For Compare From Date</td></tr>");
                       str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='cfromToday' name='cfromdate' value='Yesterday' checked>Yesterday</td><td><input type='radio'  id='cfromTomorrow' name='cfromdate' value='Tomorrow' >Tomorrow</td></tr>");
                        //str.append("<tr style=\"height: 17%;\"><td>System Date</td><td><select id='systemdate'>");
                        //  str.append("<option  value=plus>+</option>");
                       //  str.append("<option  value=minus>-</option></select></td><td><input type='text'  id='textid' value=''>Days</td></tr>");
                    
                         
                         str.append("<tr ><td>For Compare To Date</td></tr>");
                          str.append("<tr style=\"height: 17%;\"><td><input type='radio'  id='ctoToday' name='ctodate' value='Yesterday' checked>Yesterday</td><td><input type='radio'  id='ctoTomorrow' name='ctodate' value='Tomorrow' checked>Tomorrow</td></tr>");
                          str.append("<tr style=\"height: 17%;\"><td>System Date</td><td><select id='systemdate'>");
                          str.append("<option  value=plus>+</option>");
                         str.append("<option  value=minus>-</option></select></td><td><input type='text'  id='textid' value=''>Days</td></tr>");
                    
                    
                     str.append("</table>");
                        }
                        
                        str.append("<tr>");
                        str.append("<td><input type=button value=Done onclick=parent.applyadvancefilters('"+reportid+"','"+elementid+"','"+elementname+"','"+reportstatus+"')></td>");
                        str.append("</tr>");
                        str.append("</table>");
                      }
                  }
           }
        
         out.print(str.toString());
        
        return null;
    }
    public ActionForward getAdvanceLogicalFilters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
         String reportid=request.getParameter("reportid");
        String elementid=request.getParameter("elementid");
         String search = request.getParameter("selectedlogical");
        String srchCol = request.getParameter("selectedmeasure");
        String srchValue = request.getParameter("selectedval");
        String elementname=request.getParameter("elementname");
         String selectedmeasurename=request.getParameter("selectedmeasurename");
         String selecteddate=request.getParameter("selecteddate");
         String systemdate=request.getParameter("systemdate");
         String textid=request.getParameter("textid");
         String fromdate=request.getParameter("fromdate");
         String todate=request.getParameter("todate");
         String compdate=request.getParameter("compdate");
         String forcompdate=request.getParameter("forcompdate");
          PrintWriter out = response.getWriter();
            ArrayList<String> displayColumns;
            String topBtmMode = null;
            String topBtmType=null;
             String sortType = "0";
             int topBottomCount = 0;
              Object cellData = "";
                String PbUserId = "";
              ArrayList<String> srchColumns=new  ArrayList<String>();
              ArrayList<String> timedetails=new  ArrayList<String>();
               ArrayList<String> parameters=new  ArrayList<String>();
              ArrayList<SortColumn> sortColumns = new ArrayList<SortColumn>();
               QueryExecutor qryExec = new QueryExecutor();
              ArrayList originalColumns = new ArrayList();
              ArrayList<String> details=new ArrayList<String>();
              HashMap<String,ArrayList<String>> datalist=new HashMap<String,ArrayList<String>>();
             // srchColumns.add("A_"+elementid);
              details.add(srchCol);
              details.add(srchValue);
              details.add(search);
              if(!selecteddate.equalsIgnoreCase("undefined"))
              details.add(selecteddate);
              else
               details.add(systemdate+"_"+textid);    
               srchColumns.add(srchCol);
            char[] colDataTypes = null;
        HttpSession session = request.getSession(false);
         if (session.getAttribute("PROGENTABLES") != null) {
              HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
               PbUserId = String.valueOf(session.getAttribute("USERID"));
                if (map.get(reportid) != null) {
                     Container container = (Container) map.get(reportid);
                      ProgenParam pParam = new ProgenParam();
                     DataFacade facade = new DataFacade(container);
                      if(container!=null){ 
                           PbReportCollection collect = container.getReportCollect();
                           Container tempContainer = new Container();
                           PbReportCollection tempCollection = new PbReportCollection();
                           tempContainer.setReportCollect(tempCollection);
                            tempContainer.setReportId(reportid);
                             datalist=container.getadvdatalist();
                               datalist.put(elementid,details);
                            container.setadvdatalist(datalist);
                               try {
                                  tempCollection.reportId = reportid;  
                                 tempCollection.ctxPath = request.getContextPath();
                                 tempCollection.getParamMetaData(true);
                                  //tempCollection.timeDetailsArray = collect.timeDetailsArray;
                                    if(collect.timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")){
                                       timedetails.add((String) collect.timeDetailsArray.get(0));
                                       timedetails.add((String) collect.timeDetailsArray.get(1));
                                       timedetails.add((String) collect.timeDetailsArray.get(2));
                                       timedetails.add((String) collect.timeDetailsArray.get(3));
                                       timedetails.add((String) collect.timeDetailsArray.get(4));
                                       tempCollection.timeDetailsArray=timedetails;
                                 if(selecteddate.equalsIgnoreCase("reportdate")){
                               tempCollection.timeDetailsArray=timedetails;
                                 }else{
                                     String data="";
                                     if(selecteddate.equalsIgnoreCase("Yesterday")){
                                         data=pParam.getcurrentdateforpage(1);
                                         tempCollection.timeDetailsArray.set(2,data);
                                     } else if(selecteddate.equalsIgnoreCase("Today")) {
                                          DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                           Date date = new Date();
                                       data=(dateFormat.format(date.getTime()));
                                        tempCollection.timeDetailsArray.set(2, data);
                                     } else if(selecteddate.equalsIgnoreCase("Tomorrow")){
                                       data=pParam.getcurrentdateforpage(-1);
                                         tempCollection.timeDetailsArray.set(2,data);
                                     }else if(selecteddate.equalsIgnoreCase("Last Month End Date")){
                                         String value= tempCollection.timeDetailsArray.get(2).toString();
                                            String[] qtryear= value.split("/");
                                             Connection con = null;
                                                ResultSet rs2 = null;
                                                 Statement st = null;
                                                 String monthdate="";
                                                 String monthdate1="";
                                                 String qtr="";
                                                 qtr="select distinct pm_end_date from pr_day_denom where cmonth='"+qtryear[0]+"' and cm_year='"+qtryear[2]+"'";
                                                 try{
                                                     con = ProgenConnection.getInstance().getConnectionForElement(srchCol.replace("A_", "")); 
                                                       st = con.createStatement();
                                                        rs2 = st.executeQuery(qtr);
                                                          if (rs2 != null) {
                                                               if (rs2.next()) {
                                                                    monthdate = rs2.getString(1);
                                                           String[] splitdata=monthdate.split(" ");
                                                           String[] dateval=splitdata[0].split("-");
                                                            monthdate1=dateval[1]+"/"+dateval[2]+"/"+dateval[0];
                                                               }
                                                          }
                                                         rs2.close();
                                                         st.close();
                                                         con.close();
                                                          rs2 = null;
                                                            st = null;
                                                          con = null;
                                                 }catch (Exception e) {
                                         logger.error("Exception", e);
                                               }
                                           tempCollection.timeDetailsArray.set(2,monthdate1);
                                     } else if(selecteddate.equalsIgnoreCase("Last Qtr End Date")){
                                            String query = "";
                                             String value= tempCollection.timeDetailsArray.get(2).toString();
                                            String[] qtryear= value.split("/");
                                             Connection con = null;
                                                ResultSet rs2 = null;
                                                 Statement st = null;
                                                 String qtr="";
                                                 String subquery="select distinct CQTR  from pr_day_denom where cmonth = '"+qtryear[0]+"'";
                                          try{
                                               con = ProgenConnection.getInstance().getConnectionForElement(srchCol.replace("A_", ""));
                                                 st = con.createStatement();
                                                  rs2 = st.executeQuery(subquery);
                                                  if (rs2 != null) {
                                                  if (rs2.next()) {
                                                  qtr = rs2.getString(1);
                                                  if(qtr.equalsIgnoreCase("1")){
                                                     qtr="Q1";
                                                   }else if(qtr.equalsIgnoreCase("2")){
                                                      qtr="Q2";
                                                     }else if(qtr.equalsIgnoreCase("3")){
                                                      qtr="Q3";
                                                    }else if(qtr.equalsIgnoreCase("4")){
                                                      qtr="Q4";
                                                      }
                                                    }
                                                  }
                                            rs2.close();
                                            st.close();
                                           con.close();
                                            rs2 = null;
                                             st = null;
                                             con = null;
                                          }catch (Exception e) {
                                         logger.error("Exception", e);
                                               }
                                                 query = "select LQ_END_DATE from PR_DAY_DENOM where CQ_CUST_NAME='" + qtr + "-" + qtryear[2] + "'";
                                           String qrtdate = "";
                                           String qtrdate1="";
                                         try {
                                            con = ProgenConnection.getInstance().getConnectionForElement(srchCol.replace("A_", ""));
                                            st = con.createStatement();
                                            rs2 = st.executeQuery(query);
                                       if (rs2 != null) {
                                      if (rs2.next()) {
                                    qrtdate = rs2.getString(1);
                                     String[] splitdata=qrtdate.split(" ");
                                          String[] dateval=splitdata[0].split("-");
                                          qtrdate1=dateval[1]+"/"+dateval[2]+"/"+dateval[0];
                                          }
                                            }
                                            rs2.close();
                                            st.close();
                                           con.close();
                                            rs2 = null;
                                             st = null;
                                             con = null;
                                          } catch (Exception e) {
                                         logger.error("Exception", e);
                                               }
                                            tempCollection.timeDetailsArray.set(2,qtrdate1);
                                     }  else if(selecteddate.equalsIgnoreCase("Last Year End Date")){
                                        // DateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
                                         String value= tempCollection.timeDetailsArray.get(2).toString();
                                         String[] arrvalue= value.split("/");
                                            String query = "";
                                             query = "select LY_END_DATE from PR_DAY_DENOM where CYEAR='" + arrvalue[2] + "' ";
                                              PbTimeRanges pbTime = new PbTimeRanges();
                                              Connection con = null;
                                                ResultSet rs2 = null;
                                                 Statement st = null;
                                            String lastyear = "";
                                            String lastyear1="";
                                             try {
                                          con = pbTime.getConnection(srchCol.replace("A_", ""));
                                          st = con.createStatement();
                                          rs2 = st.executeQuery(query);
                                           if (rs2 != null) {
                                              if (rs2.next()) {
                                          lastyear = rs2.getString(1);
                                          String[] splitdata=lastyear.split(" ");
                                          String[] dateval=splitdata[0].split("-");
                                          lastyear1=dateval[1]+"/"+dateval[2]+"/"+dateval[0];
                                            }
                                          }
                                            rs2.close();
                                            st.close();
                                           con.close();
                                             rs2 = null;
                                             st = null;
                                           con = null;
                                         } catch (Exception e) {
                                       logger.error("Exception", e);
                                           }
                                          tempCollection.timeDetailsArray.set(2,lastyear1);
                                     }else if(systemdate.equalsIgnoreCase("plus") || systemdate.equalsIgnoreCase("minus")){
                                          DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                             Date date = new Date();
                                             Calendar cal = Calendar.getInstance();    
                                             String currentdate=dateFormat.format(date);
                                             cal.setTime( dateFormat.parse(currentdate));    
                                         if(systemdate.equalsIgnoreCase("plus")){
                                            cal.add( Calendar.DATE,Integer.parseInt(textid)); 
                                            String convertedDate=dateFormat.format(cal.getTime());  
                                              tempCollection.timeDetailsArray.set(2,convertedDate);
                                         }else if(systemdate.equalsIgnoreCase("minus")){
                                              cal.add( Calendar.DATE,-Integer.parseInt(textid)); 
                                            String convertedDate=dateFormat.format(cal.getTime());  
                                               tempCollection.timeDetailsArray.set(2,convertedDate);
                                         }
                                     }
                                   
                                 }
                                    }else{
                                         String data="";
                                          timedetails.add((String) collect.timeDetailsArray.get(0));
                                       timedetails.add((String) collect.timeDetailsArray.get(1));
                                       timedetails.add((String) collect.timeDetailsArray.get(2));
                                       timedetails.add((String) collect.timeDetailsArray.get(3));
                                       timedetails.add((String) collect.timeDetailsArray.get(4));
                                       timedetails.add((String) collect.timeDetailsArray.get(5));
                                   
                                       tempCollection.timeDetailsArray=timedetails;
                                          if(selecteddate.equalsIgnoreCase("reportdate")){
                                             tempCollection.timeDetailsArray=timedetails;   
                                          }else{
                                        if(fromdate.equalsIgnoreCase("Yesterday")){
                                            data=pParam.getcurrentdateforpage(1);
                                             tempCollection.timeDetailsArray.set(2,data);
                                        }else if(fromdate.equalsIgnoreCase("Tomorrow")){
                                             data=pParam.getcurrentdateforpage(-1);
                                               tempCollection.timeDetailsArray.set(2,data);
                                        }
                                        if(todate.equalsIgnoreCase("Yesterday")){
                                           data=pParam.getcurrentdateforpage(1);
                                             tempCollection.timeDetailsArray.set(3,data);
                                        }else if(todate.equalsIgnoreCase("Tomorrow")){
                                             data=pParam.getcurrentdateforpage(-1);
                                               tempCollection.timeDetailsArray.set(3,data);
                                        }
                                        if(compdate.equalsIgnoreCase("Yesterday")){
                                           data=pParam.getcurrentdateforpage(1);  
                                             tempCollection.timeDetailsArray.set(4,data);
                                        }else if(compdate.equalsIgnoreCase("Tomorrow")){
                                             data=pParam.getcurrentdateforpage(-1);
                                               tempCollection.timeDetailsArray.set(4,data);
                                        }
                                        if(forcompdate.equalsIgnoreCase("Yesterday")){
                                             data=pParam.getcurrentdateforpage(1); 
                                               tempCollection.timeDetailsArray.set(5,data);
                                        }else if(forcompdate.equalsIgnoreCase("Tomorrow")){
                                             data=pParam.getcurrentdateforpage(-1); 
                                               tempCollection.timeDetailsArray.set(5,data);
                                        }
                                          }
                                    }
                                  tempCollection.reportQryElementIds.clear();
                                  tempCollection.reportRowViewbyValues.clear();
                                  tempCollection.reportQryColNames.clear();
                                  tempCollection.reportRowViewbyValues.add(elementid);
                                  tempCollection.reportQryElementIds.add(srchCol.replace("A_",""));
                                  tempCollection.reportQryColNames.add(selectedmeasurename);
                                   tempContainer.setTableMeasure(tempCollection.reportQryElementIds);
                                     tempContainer.setTableMeasureNames(tempCollection.reportQryColNames);
                                    tempContainer.setViewByColNames(tempCollection.reportRowViewbyValues);
                                     tempContainer.setViewByElementIds(tempCollection.reportRowViewbyValues);
                                     tempContainer.setTimeDetailsArray(tempCollection.timeDetailsArray);
                                      for (int i = 0; i < tempCollection.reportRowViewbyValues.size(); i++) {
                                           originalColumns.add("A_" + tempCollection.reportRowViewbyValues.get(i).toString());
                                           }
                                       for (int j = 0; j < tempCollection.reportQryElementIds.size(); j++) {
                                             originalColumns.add("A_" + tempCollection.reportQryElementIds.get(j).toString());
                                        }
                                       tempCollection.tableElementIds.clear();
                                       tempCollection.tableColNames.clear();
                                       tempCollection.tableColTypes.clear();
                                       tempCollection.tableColDispTypes.clear();
                                       tempCollection.tableElementIds.add(elementid);
                                       tempCollection.tableElementIds.add(srchCol.replace("A_", ""));
                                       tempCollection.tableColNames.add(elementname);
                                       tempCollection.tableColNames.add(selectedmeasurename);
                                       //tempCollection.tableColTypes.add('C');
                                      // tempCollection.tableColTypes.add('N');
                                       //tempCollection.tableColDispTypes.add('H');
                                       // tempCollection.tableColDispTypes.add('T');
                                       tempContainer.setOriginalColumns(originalColumns);
                                       tempContainer.setDisplayColumns(tempCollection.tableElementIds);
                                        tempContainer.setDisplayLabels(tempCollection.tableColNames);
                                       //  tempContainer.setDataTypes(tempCollection.tableColTypes);
                                        // tempContainer.setDisplayTypes(tempCollection.tableColDispTypes);
                                             PbReportQuery depRepQry=new PbReportQuery();
                                           depRepQry = qryExec.formulateQuery(tempCollection, PbUserId);
                                               String query = depRepQry.generateViewByQry();
                                                PbReturnObject retObj1= (PbReturnObject) qryExec.executeQuery(collect, query, false);
                                                       tempContainer.setRetObj(retObj1);
                                                          try {
                        if (tempCollection.tablePropertiesXML != null) {
//                            ReportTablePropertyBuilder.TABLE_PROPERTY_BUILDER.updateTablePropertiesInContainer(container, tempCollection.tablePropertiesXML);
                            ReportTablePropertyBuilder.TABLE_PROPERTY_BUILDER.updateTablePropertiesInContainer(tempContainer, tempCollection.tablePropertiesXML);
                        }
                         } catch (Exception e) {
                        logger.error("Exception: ", e);
                    }
                                                ProgenDataSet retObj = tempContainer.getRetObj();
                                                 DataFacade facade1 = new DataFacade(tempContainer);
                          int rowCount = retObj.getRowCount();
                           displayColumns=container.getDisplayColumns();
                            if (srchCol != null && !srchCol.isEmpty()) {
                                   SortColumn sortColumn;
                                   if ("TOP".equals(search) || "BTM".equals(search)) {
                                         if ("TOP".equals(search)) {
                                            topBtmMode = ContainerConstants.TOP_BOTTOM_MODE_ABSOLUTE; 
                                            sortType = "1";
                                              sortColumn = new SortColumn(srchCol, SortOrder.DESCENDING);
                                              sortColumns.add(sortColumn);
                                              topBottomCount=Integer.parseInt(srchValue);
                                              ArrayList<Integer> viewSeq;
                                                viewSeq = retObj.findTopBottom(sortColumns, topBottomCount);
                                                for(int i=0;i<viewSeq.size();i++){
                                                    int actualRow = facade1.getActualRow(viewSeq.get(i));
                                                    cellData=facade1.getDimensionData(actualRow, "A_"+elementid);
                                                    parameters.add(cellData.toString());
                                                    
                                                }
                                         }else{
                                        topBtmType = ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS;
                                        sortType = "0";
                                          sortColumn = new SortColumn(srchCol, SortOrder.ASCENDING);
                                           sortColumns.add(sortColumn);
                                            topBottomCount=Integer.parseInt(srchValue);
                                              ArrayList<Integer> viewSeq;
                                                viewSeq = retObj.findTopBottom(sortColumns, topBottomCount);
                                                for(int i=0;i<viewSeq.size();i++){
                                                    int actualRow = facade1.getActualRow(viewSeq.get(i));
                                                    cellData=facade1.getDimensionData(actualRow, "A_"+elementid);
                                                    parameters.add(cellData.toString());
                                                }
                                         }
                                   }
                            }
                      }catch (Exception e) {
                        logger.error("Exception: ", e);
                    }
                }
         }
         }
            out.print(parameters);
        return null;

    }
    //ended by sruthi
}
