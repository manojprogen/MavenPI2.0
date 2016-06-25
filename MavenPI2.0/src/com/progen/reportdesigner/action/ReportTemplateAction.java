package com.progen.reportdesigner.action;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.bd.ProgenJqplotGraphBD;
import com.progen.charts.JqplotGraphProperty;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.datasnapshots.DataSnapshotBD;
import com.progen.db.ProgenDataSet;
import com.progen.i18n.TranslaterHelper;
import com.progen.jqplot.ProGenJqPlotChartTypes;
import com.progen.report.*;
import static com.progen.report.query.QueryTimeConstants.*;
import com.progen.report.query.QueryTimeHelper;
import com.progen.reportdesigner.bd.ReportTemplateBD;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.action.ReportViewerAction;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.scheduler.ReportDetails;
import com.progen.ui.messages.PrgMessage;
import com.progen.ui.messages.PrgUIContainer;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import prg.db.*;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class ReportTemplateAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(ReportTemplateAction.class);
    private final static String SUCCESS = "success";

    //when user clicks on done button in create report dialogue in Report Studio
    public ActionForward goToReportDesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {



        HttpSession session = request.getSession(false);
        HashMap map = null;
        String reportId = null;
        Container container = null;
        String reportName = null;
        String reportDesc = null;
        String editReportName = null;
        String PbUserId = "";
        String repId = null;
        Boolean flag = false;
        String url = request.getRequestURL().toString();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();

        if (session != null) {
            try {

                reportName = request.getParameter("reportName");
                reportName = reportName.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
                reportName = reportName.replace("'", "''");
                reportDesc = request.getParameter("reportDesc");
                reportDesc = reportDesc.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
                reportDesc = reportDesc.replace("'", "''");
                if (request.getParameter("editRepName") != null) {

                    repId = request.getParameter("repId");
                    editReportName = request.getParameter("editRepName");
                }
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                url = url + "?templateParam=goToReportDesigner&reportName=" + reportName + "&reportDesc=" + reportDesc;
                request.setAttribute("reportdesignerurl", url);
                reportId = reportViewerDAO.getCustomReportId();

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(reportId) != null) {
                        container = (Container) map.get(reportId);
                        container = new Container();
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }

                PbReportCollection collect = container.getReportCollect();
                if (collect == null) {
                    collect = new PbReportCollection();
                }
                container.setReportCollect(collect);
                ////.println(container.getReportId());
                container.setReportId(reportId);
                container.setTableId(reportId);
                container.setNetTotalReq(true);
                container.setGrandTotalReq(true);


                String result = reportTemplateDAO.getUserFoldersByUserId(PbUserId);


                request.setAttribute("ReportId", reportId);
                request.setAttribute("UserFlds", result);

                container.setReportName(reportName);
                container.setReportDesc(reportDesc);

                //PbReportCollection collect = new PbReportCollection();
                //collect.reportId = reportId;
                //container.setReportCollect(collect);
                container.setReportId(reportId);

                container.setSessionContext(session, container);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            if (editReportName == null) {
                return mapping.findForward(SUCCESS);
            } else {

                reportTemplateDAO.editReportName(repId, reportName, reportDesc);
                return null;
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
// when user clicks on reset button in Report Designer

    public ActionForward resetCurrReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        HashMap map = new HashMap();
        String reportId = "";
        Container container = null;
        if (session != null) {
            reportId = request.getParameter("reportId");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportId) != null) {
                map.remove(reportId);
            }
            out.println("1");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward enableMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        boolean isMapEnabled = Boolean.valueOf(request.getParameter("mapFlag"));
        HashMap map = new HashMap();
        String reportId = "";

        Container container = null;
        if (session != null) {
            reportId = request.getParameter("reportId");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(reportId);

            container.setMapEnabled(isMapEnabled);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //Used to get User specific business roles and it is used in all designer pages
    public ActionForward getUserFolders(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String PbUserId = "";
        String result = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            result = reportTemplateDAO.getUserFoldersByUserId(PbUserId);
            request.setAttribute("UserFlds", result);
            return mapping.findForward(SUCCESS);
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
//used to retrieve Dimensions based on the user folders selected

    public ActionForward getUserDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String PbUserId = "";
        String result = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            foldersIds = request.getParameter("foldersIds");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);


            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", foldersIds);
            result = reportTemplateDAO.getUserDims(foldersIds, PbUserId);
            PrintWriter out = response.getWriter();
            out.print(result);
            container.setParametersHashMap(ParametersHashMap);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getUserFavouriteDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("REPORTID");
        String userId = String.valueOf(session.getAttribute("USERID"));
        if (session != null) {
            String foldersIds = request.getParameter("foldersIds");
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            String result = reportTemplateDAO.getFavParams(foldersIds, userId);
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward editUserFavouriteDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("REPORTID");
        String userId = String.valueOf(session.getAttribute("USERID"));
        String favName = request.getParameter("favName");
        if (session != null) {
            String foldersIds = request.getParameter("foldersIds");
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            String result = reportTemplateDAO.editFavoriteParameters(foldersIds, userId, favName);
            response.getWriter().print(result);
        }
        return null;
    }

    public ActionForward updateUserFavouriteDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("REPORTID");
        String userId = String.valueOf(session.getAttribute("USERID"));
        String favName = request.getParameter("favName");
        String elementIds = request.getParameter("elementIds");
        String favParamDesc = request.getParameter("favParamDesc");
        // String[] elements=elementIds.split(",");
        if (session != null) {
            String folderIds = request.getParameter("foldersIds");
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            reportTemplateDAO.updateFavouriteParams(reportId, userId, favName, favParamDesc, folderIds, elementIds);
        }
        return null;
    }
    //for building facts and its corressponding measures when user clickc on measures in report designing page

    public ActionForward getMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String foldersIds = "";
        String result = "";
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";

        ArrayList<String> Parameters = null;
        ArrayList<String> Parameters1 = null;

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);
            String tableList = request.getParameter("tableList");


            Parameters = new ArrayList<String>();
            Parameters1 = new ArrayList<String>();

            for (String parameter : container.getParameterElements()) {
                Parameters.add(parameter);
            }

            //////.println("dimFactRel is : "+dimFactRel);

            foldersIds = request.getParameter("foldersIds");
            result = "";
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            if (container.getTableList() != null && !container.getTableList().isEmpty()) {
                ArrayList alist = new ArrayList();
                result = repdao.getMeasuresForReport(foldersIds, Parameters, request.getContextPath(), container.getTableList());
                container.setTableList(alist);
            } else if (tableList != null && tableList.equalsIgnoreCase("true")) {
                result = reportTemplateDAO.getMeasures(foldersIds, Parameters, request.getContextPath());
            }

            request.setAttribute("Measures", result);
            return mapping.findForward("measures");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getMeasures1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String foldersIds = "";
        String result = "";
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";

        String minTimeLevel = null;
        String dimId = null;
        String timeparams = null;
        ArrayList<String> Parameters = null;

        String[] timeparameterIds = null;

        ArrayList timeParameters = new ArrayList();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        Date date = new Date();
        String DATE_FORMAT = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        HashMap<String, ArrayList<String>> timeDimMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeDetails = new ArrayList<String>();
        QueryTimeHelper timeHelper = new QueryTimeHelper();

        ArrayList<String> timeColTypes = new ArrayList<String>();

        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);
            PbReportCollection collect = container.getReportCollect();
            String tableList = request.getParameter("tableList");
            String isTimeDashboard = request.getParameter("isTimeDashboard");
            dimId = request.getParameter("dimId");

            timeparams = request.getParameter("timeparams");
            Parameters = new ArrayList<String>();

            if (timeparams != null) {
                timeparameterIds = timeparams.split(",");
                for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                    timeParameters.add(timeparameterIds[timeparamCount]);
                }
            }
            for (String parameter : container.getParameterElements()) {
                Parameters.add(parameter);
            }

            foldersIds = request.getParameter("foldersIds");
            if (foldersIds != null && !"".equals(foldersIds)) {
                collect.reportBizRoles = foldersIds.split(",");
            }

            minTimeLevel = reportTemplateDAO.getUserFolderMinTimeLevel(foldersIds);
            result = "";
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            if (container.getTableList() != null && !container.getTableList().isEmpty()) {
                ArrayList alist = new ArrayList();
                result = repdao.getMeasuresForReport(foldersIds, Parameters, request.getContextPath(), container.getTableList());
                container.setTableList(alist);
            } else if (tableList != null && tableList.equalsIgnoreCase("true")) {
                result = reportTemplateDAO.getMeasures(foldersIds, Parameters, request.getContextPath());
            }
            for (Object timeParameter : timeParameters) {
                timeColTypes.add((String) timeParameter);
            }
            if (dimId != null) {
                if (minTimeLevel.equals("5")) {

                    if (dimId.equalsIgnoreCase("Time-Period Basis")) {
                        timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_DAY_LEVEL, timeColTypes);
                        timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_DAY_LEVEL);
                    }

                } else if (minTimeLevel.equals("4")) {

                    timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_WEEK_LEVEL, timeColTypes);
                    timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_WEEK_LEVEL);

                } else if (minTimeLevel.equals("3")) {
                    timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_MONTH_LEVEL, timeColTypes);
                    timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_MONTH_LEVEL);
                } else if (minTimeLevel.equals("2")) {
                    timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_QUARTER_LEVEL, timeColTypes);
                    timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_QUARTER_LEVEL);
                } else if (minTimeLevel.equals("1")) {
                    timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_YEAR_LEVEL, timeColTypes);
                    timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_YEAR_LEVEL);
                }



                collect.timeDetailsArray = timeDetails;
                collect.timeDetailsMap = timeDimMap;
                container.setReportCollect(collect);
            }
            request.setAttribute("Measures", result);
            request.setAttribute("isTimeDashboard", isTimeDashboard);
            return mapping.findForward("measures");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
//    public ActionForward getTabColumns(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session = request.getSession(false);
//        String Measrs = "";
//        String MeasrsNames = "";
//        String REPORTID = "";
//        String result = "";
//        PrintWriter out = response.getWriter();
//        if (session != null) {
//            Measrs = request.getParameter("Msrs");
//            MeasrsNames = request.getParameter("MsrsNames");
//            REPORTID = request.getParameter("REPORTID");
//            if (Measrs != null) {
//                String[] Msrs = Measrs.split(",");
//                String[] MsrsNames = MeasrsNames.split(",");
//                ArrayList measureArray = new ArrayList();
//                ArrayList measureNamesArray = new ArrayList();
//                for (int i = 0; i < Msrs.length; i++) {
//                    if (!(Msrs[i].equalsIgnoreCase(""))) {
//                        measureArray.add(Msrs[i]);
//                        measureNamesArray.add(MsrsNames[i]);
//                    }
//                }
//                //////.println("measureArray is : " + measureArray);
//                //////.println("measureNamesArray is : " + measureNamesArray);
//                result = reportTemplateDAO.getTabColumns(measureArray, measureNamesArray);
//                request.setAttribute("Tabcols", result);
//                out.print(result);
//            }
//            return null;
//        } else {
//            return mapping.findForward("sessionExpired");
//        }
//    }
//For building table i.e, selecting REP,CEP and Measures table is builded here

    public ActionForward buildTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);

        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";

        String buildTableChange = null;
        String Measures = null;
        String selectedList = null;
        String rowEdgeParams = null;
        String colEdgeParams = null;


        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);
            buildTableChange = request.getParameter("buildTableChange");
            Measures = request.getParameter("Msrs");
            String Fulllist = request.getParameter("timeDetails");
            selectedList = "MTD,PMTD,PYMTD,QTD,PQTD,PYQTD,YTD,PYTD";
            rowEdgeParams = request.getParameter("rowEdgeParams");
            colEdgeParams = request.getParameter("colEdgeParams");
            String[] splitselectedList = selectedList.split(",");
            String Allchange = "CPMTD,C%PMTD,CPYMTD,C%PYMTD,CPQTD,C%PQTD,CPYQTD,C%PYQTD,CPYTD,C%PYTD";
            String[] splitAllchange = Allchange.split(",");
            ArrayList<String> currentTimeperiods = new ArrayList<>();
            ArrayList<String> CurrentAllchanges = new ArrayList<>();
            ArrayList<String> allcolumns = new ArrayList<>();
            boolean b = true;


            PbReportCollection collect = container.getReportCollect();


            try {


                if (buildTableChange != null && buildTableChange.equalsIgnoreCase("REP")) {
                    String[] REP = rowEdgeParams.split(",");
                    ArrayList<String> rowViewByValues = new ArrayList<String>();

                    for (int i = 0; i < REP.length; i++) {
                        if (!(REP[i].equalsIgnoreCase(""))) {
                            rowViewByValues.add(REP[i]);
                        }
                    }

                    collect.setRowViewBys(rowViewByValues);
                    //GraphHashMap = reportBD.changeViewBys(GraphHashMap, rowEdgeArray, rowEdgeNameArray, Parameters, ParametersNames);


                } else if (buildTableChange != null && buildTableChange.equalsIgnoreCase("CEP")) {
                    String[] CEP = colEdgeParams.split(",");
                    ArrayList<String> colViewByValues = new ArrayList<String>();

                    for (int i = 0; i < CEP.length; i++) {
                        if (!(CEP[i].equalsIgnoreCase(""))) {
                            colViewByValues.add(CEP[i]);
                        }
                    }

                    collect.setColumnViewBys(colViewByValues);

                } else if (buildTableChange != null && buildTableChange.equalsIgnoreCase("Measures") && Measures != null) {
                    String[] Msrs = Measures.split(",");


                    if (Fulllist != null && !Fulllist.equalsIgnoreCase("")) {

                        String[] splitFulllist = Fulllist.split(",");

                        outerloop:
                        for (int i = 0; i < splitFulllist.length; i++) {

                            allcolumns.add(splitFulllist[i]);
                            for (int j = 0; j < splitselectedList.length; j++) {

                                if (splitFulllist[i].equalsIgnoreCase(splitselectedList[j])) {
                                    currentTimeperiods.add(splitFulllist[i]);
                                    continue outerloop;
                                }


                            }
                            for (int j = 0; j < splitAllchange.length; j++) {
                                if (splitFulllist[i].equalsIgnoreCase(splitAllchange[j])) {
                                    CurrentAllchanges.add(splitFulllist[i]);
                                    continue outerloop;
                                }


                            }

                        }
                        ArrayList<String> timePeriodsList = new ArrayList<String>();
                        List<String> l = Arrays.<String>asList(Msrs);
                        ArrayList<String> al = new ArrayList<String>(l);
                        for (int j = 0; j < Msrs.length; j++) {
                            for (int i = 0; i < currentTimeperiods.size(); i++) {
                                timePeriodsList.add(currentTimeperiods.get(i));
                            }
                        }
                        container.setDistinctTimePeriodsList(currentTimeperiods);
                        container.setTimePeriodsList(timePeriodsList);
                        container.setAllColumns(allcolumns);
                        container.setChangeColumns(CurrentAllchanges);
                    }


                    ArrayList measureArray = new ArrayList();
                    ReportTemplateDAO dao = new ReportTemplateDAO();
                    ArrayList<String> reportQryElements = new ArrayList<String>();
                    for (int i = 0; i < Msrs.length; i++) {
                        if (!(Msrs[i].equalsIgnoreCase(""))) {
                            measureArray.add("A_" + Msrs[i]);
                            //MeasuresNames.add(MsrsNames[i]);//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
                            reportQryElements.add(Msrs[i]);
                        }
                    }
                    ArrayList<String> reportQryAggs;
                    ArrayList<String> reportQryColNames;
                    ArrayList<String> reportQryColTypes;
                    ArrayList<String> originalColTypes;

                    if (!reportQryElements.isEmpty()) {
                        reportQryAggs = dao.getReportQryAggregations(reportQryElements);
                        reportQryColNames = dao.getReportQryColNames();
                        reportQryColTypes = dao.getReportQryColTypes();
                        originalColTypes = dao.getOriginalReportQryColTypes();
                        HashMap nfmap = new HashMap();
                        PbDb pbdb = new PbDb();

                        for (int j = 0; j < reportQryElements.size(); j++) {
                            String numberFormatQuery = "select no_format from prg_user_sub_folder_elements where element_id=" + reportQryElements.get(j);
                            PbReturnObject returnObject = new PbReturnObject();
                            returnObject = pbdb.execSelectSQL(numberFormatQuery);
                            if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                                nfmap.put("A_" + reportQryElements.get(j), returnObject.getFieldValueString(0, 0));
                            }
                        }
                        for (int j = 0; j < reportQryElements.size(); j++) {
                            String numberFormatQuery = "select symbols from prg_user_sub_folder_elements where element_id=" + reportQryElements.get(j);
                            PbReturnObject returnObject = new PbReturnObject();
                            returnObject = pbdb.execSelectSQL(numberFormatQuery);
                            if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                                container.symbol.put("A_" + reportQryElements.get(j), returnObject.getFieldValueString(0, 0));
                            }
                        }
                        for (int j = 0; j < reportQryElements.size(); j++) {
                            String numberFormatQuery = "select round from prg_user_sub_folder_elements where element_id=" + reportQryElements.get(j);
                            PbReturnObject returnObject = new PbReturnObject();
                            returnObject = pbdb.execSelectSQL(numberFormatQuery);
                            if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                                if (returnObject.getFieldValueString(0, 0).equalsIgnoreCase("")) {
                                    container.measRoundingPrecisions.put("A_" + reportQryElements.get(j), 0);
                                } else {
                                    container.measRoundingPrecisions.put("A_" + reportQryElements.get(j), Integer.parseInt(returnObject.getFieldValueString(0, 0)));
                                }
                            } else {
                                container.measRoundingPrecisions.put("A_" + reportQryElements.get(j), 0);
                            }
                        }
                        //added by Nazneen
                        for (int j = 0; j < reportQryElements.size(); j++) {
                            String numberFormatQuery = "select date_format from prg_user_all_info_details where element_id=" + reportQryElements.get(j);
                            PbReturnObject returnObject = new PbReturnObject();
                            returnObject = pbdb.execSelectSQL(numberFormatQuery);
                            if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                                if (!returnObject.getFieldValueString(0, 0).equalsIgnoreCase("")) {
                                    container.setDateFormatt("A_" + reportQryElements.get(j), returnObject.getFieldValueString(0, 0));
                                }
                            }
                        }


                        container.getTableHashMap().put("NFMap", nfmap);
                        for (int i = 0; i < reportQryElements.size(); i++) {
                            collect.setReportQueryColumns(reportQryElements.get(i), reportQryColNames.get(i), reportQryAggs.get(i), reportQryColTypes.get(i), originalColTypes.get(i));
                        }
                    }


                    collect.initializeReportTableCols();

                } else if (buildTableChange != null && buildTableChange.equalsIgnoreCase("TableProperties")) {
                }

                return null;
            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //For building pARAMS i.e, WHEN USER DRAGS PARAMETERS AN drops in parameter section
    public ActionForward buildParams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ProgenParam pParam = new ProgenParam();
        HttpSession session = request.getSession(false);
        String foldersIds = null;
        String minTimeLevel = null;

        String paramIds = null;
        String ReportLayout = null;
        String paramNames = null;
        String dimId = null;
        String timeparams = null;

        String[] parameterIds = null;
        String[] parameterNames = null;//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
        String[] timeparameterIds = null;

        ArrayList Parameters = new ArrayList();
        ArrayList ParametersNames = new ArrayList();//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
        ArrayList timeParameters = new ArrayList();

        String customReportId = null;
        HashMap map = null;
        Container container = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        if (session != null) {
            ReportLayout = request.getParameter("ReportLayout");
            customReportId = request.getParameter("REPORTID");
            String dispDesignerDims = request.getParameter("dispDesignerDims");
            String fromDesigner = request.getParameter("fromDesigner");
            session.setAttribute("ReportLayout", ReportLayout);//added by Ram for kpi and none
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);
            HashMap AOMasterMap = (HashMap) session.getAttribute("AOMasterMap");
            PbReportCollection collect = container.getReportCollect();
            foldersIds = request.getParameter("foldersIds");
            String aoid = (String) session.getAttribute("aoId");
            if ((foldersIds.equalsIgnoreCase("null") || foldersIds == null) && aoid != null && !aoid.equalsIgnoreCase("")) {
                foldersIds = ((HashMap) AOMasterMap.get(aoid)).get("FOLDER_ID").toString();
            }
            dimId = request.getParameter("dimId");
            if (foldersIds != null && !"".equals(foldersIds)) {
                collect.reportBizRoles = foldersIds.split(",");
            }
            minTimeLevel = reportTemplateDAO.getUserFolderMinTimeLevel(foldersIds);
//minTimeLevel = reportTemplateDAO.getUserFolderMinTimeLevel("6");
            paramIds = request.getParameter("params");
            paramIds = paramIds.replaceAll("elmnt-", "").trim();
            paramNames = request.getParameter("paramNames");//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
            timeparams = request.getParameter("timeparams");
            //update Param hashmap in session
            if (paramIds != null && !"".equalsIgnoreCase(paramIds)) {
                parameterIds = paramIds.split(",");
                parameterNames = paramNames.split(",");
                collect.resetAllParameters();
                ArrayList<String> defaultValues = new ArrayList<String>();
                for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                    Parameters.add(parameterIds[paramCount]);
                    ParametersNames.add(parameterNames[paramCount]);//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
                    collect.setParameters(parameterIds[paramCount], parameterNames[paramCount], defaultValues);
                    container.removeParameterSecurity(parameterIds[paramCount]);
                }
            }

            if (timeparams != null) {
                timeparameterIds = timeparams.split(",");
                for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                    timeParameters.add(timeparameterIds[timeparamCount]);
                }
            }


            Date date = new Date();
            String DATE_FORMAT = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

            HashMap<String, ArrayList<String>> timeDimMap = new HashMap<String, ArrayList<String>>();
            ArrayList<String> timeDetails = new ArrayList<String>();
            QueryTimeHelper timeHelper = new QueryTimeHelper();
            ArrayList<String> timeColTypes = new ArrayList<String>();
            for (Object timeParameter : timeParameters) {
                timeColTypes.add((String) timeParameter);
            }
            if (dimId.equalsIgnoreCase("Time-Period Basis") || dimId.equalsIgnoreCase("Time-Range Basis") || dimId.equalsIgnoreCase("Time-Rolling Period") || dimId.equalsIgnoreCase("Time-Month Range Basis")
                    || dimId.equalsIgnoreCase("Time-Quarter Range Basis") || dimId.equalsIgnoreCase("Time-Year Range Basis") || dimId.equalsIgnoreCase("Time-Cohort Basis") || minTimeLevel.equals("4")
                    || (minTimeLevel.equals("3")) || (minTimeLevel.equals("2")) || (minTimeLevel.equals("1"))) {
                if (timeParameters != null) {
                    for (int time = 0; time < timeParameters.size(); time++) {
                        if (minTimeLevel.equals("5")) {
                            if (dimId.equalsIgnoreCase("Time-Period Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Range Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_RANGE_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_RANGE_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Rolling Period")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_ROLLING_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_ROLLING_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Month Range Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_MONTH_RANGE_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_MONTH_RANGE_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Quarter Range Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_QUARTER_RANGE_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_QUARTER_RANGE_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Year Range Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_YEAR_RANGE_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_YEAR_RANGE_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Cohort Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_COHORT_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_COHORT_BASIS, TIME_DAY_LEVEL);
                            }
                        } else if (minTimeLevel.equals("4")) {

                            timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_WEEK_LEVEL, timeColTypes);
                            timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_WEEK_LEVEL);

                        } else if (minTimeLevel.equals("3")) {
                            timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_MONTH_LEVEL, timeColTypes);
                            timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_MONTH_LEVEL);
                        } else if (minTimeLevel.equals("2")) {
                            timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_QUARTER_LEVEL, timeColTypes);
                            timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_QUARTER_LEVEL);
                        } else if (minTimeLevel.equals("1")) {
                            timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_YEAR_LEVEL, timeColTypes);
                            timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_YEAR_LEVEL);
                        }
                    }
                }

                collect.timeDetailsArray = timeDetails;
                collect.timeDetailsMap = timeDimMap;
                container.setReportCollect(collect);
                if (dispDesignerDims != null && !dispDesignerDims.isEmpty() && dispDesignerDims.equalsIgnoreCase("dispDesignerDims")) {
                    HashMap ParametersHashMap = container.getParametersHashMap();
                    ParametersHashMap.put("Parameters", Parameters);
                    ParametersHashMap.put("ParametersNames", ParametersNames);
                    collect.reportParamIds = Parameters;
                    collect.reportParamNames = ParametersNames;
                }
                if (fromDesigner != null && !fromDesigner.isEmpty() && fromDesigner.equalsIgnoreCase("insightDesigner")) {
                    ArrayList<String> colViewByLst = new ArrayList<String>();
                    ArrayList<String> colViewNamesLst = new ArrayList<String>();
                    ArrayList<String> rowViewByLst = new ArrayList<String>();
                    ArrayList<String> rowViewNamesLst = new ArrayList<String>();
                    HashMap TableHashMap = null;
                    session.removeAttribute("colViewByLst");
                    session.removeAttribute("rowViewByLst");
                    rowViewByLst.add(String.valueOf(Parameters.get(0)));
                    collect.setRowViewBys(rowViewByLst);
                    rowViewNamesLst.add(String.valueOf(ParametersNames.get(0)));
                    session.setAttribute("colViewByLst", colViewByLst);
                    session.setAttribute("rowViewByLst", rowViewByLst);
                    session.setAttribute("rowViewIdList", rowViewByLst);
                    session.setAttribute("colViewIdList", colViewByLst);
                    session.setAttribute("rowNamesLst", rowViewNamesLst);
                    session.setAttribute("colNamesLst", colViewNamesLst);
                    session.setAttribute("ReportLayout", ReportLayout);//added by mohit for kpi and none
                    container.setColumnViewList(colViewByLst);
                    container.setRowViewList(rowViewByLst);
                    TableHashMap = container.getTableHashMap();
                    TableHashMap.put("REP", rowViewByLst);
                    TableHashMap.put("CEP", colViewByLst);
                    container.setTableHashMap(TableHashMap);
                }

            }

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
//Used to build graphs in report designing

    public ActionForward buildGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ServletContext context = this.getServlet().getServletContext();
        PrintWriter out = response.getWriter();
        ReportTemplateBD reportBD = new ReportTemplateBD();
        boolean isFxCharts = Boolean.parseBoolean(context.getInitParameter("isFxCharts"));
        reportBD.setIsFxCharts(isFxCharts);
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String result = "";
        HashMap GraphHashMap = null;
        String graphChange = null;
        String grpId = null;
        String grpType = null;
        String grpSize = null;
        String grpColumns = null;
        String grpViewBys = null;
        String grpIds = null;
        //graph details code starts from here
        String grpTitle = null;
        String grpLegend = null;
        String grpLegendLoc = null;
        String sym1 = "#";
        String sym2 = "#";
        String grpshowX = null;
        String grpshowY = null;
        String grplyaxislabel = null;
        String grpryaxislabel = null;
        String grpdrill = null;
        String grpbcolor = null;
        String grpfcolor = null;
        String grpdata = null;
        String GraphDisplayRows = null;
        String startindex = null;
        String endindex = null;
        //graph details related code end here
        String grpColumnsNames = null;
        ArrayList allGraphColumns = null;
        ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        HashMap ReportHashMap = null;

        HashMap GraphClassesHashMap = null;
        HashMap GraphSizesDtlsHashMap = null;

        String leftgrpColumns = null;
        String rightgrpColumns = null;

        String rowValues = null;
// to find graphtable method
        String graphTableMethod = null;
        String isdashbaord = null;
        String dashletId = null;
        String measureid = null;
        String measurename = null;
        int height = 0;
        int width = 0;
        boolean isShowLabels = false;


        if (session != null) {
            try {
                customReportId = request.getParameter("REPORTID");

                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(customReportId);

                graphChange = request.getParameter("graphChange");
                grpId = request.getParameter("gid");
                grpType = request.getParameter("grptypid");
                grpSize = request.getParameter("grpsizeid");
                grpColumns = request.getParameter("grpColumns");
                grpViewBys = request.getParameter("grpViewBys");
                grpIds = request.getParameter("grpIds");

                //graph details code starts from here
                grpTitle = request.getParameter("grpTitle");
                grpLegend = request.getParameter("grpLegend");
                grpLegendLoc = request.getParameter("grpLegendLoc");

                grpshowX = request.getParameter("showX");
                grpshowY = request.getParameter("showY");
                grplyaxislabel = request.getParameter("lyaxisLabel");
                grpryaxislabel = request.getParameter("ryaxisLabel");
                grpdrill = request.getParameter("Drill");
                grpbcolor = request.getParameter("bcolor");
                grpbcolor = sym1 + grpbcolor;
                grpfcolor = request.getParameter("fcolor");
                grpfcolor = sym2 + grpfcolor;
                grpdata = request.getParameter("Data");
                graphTableMethod = request.getParameter("graphTableMethod");
                isdashbaord = request.getParameter("isdashboard");//sandeep
                dashletId = request.getParameter("dashletId");//sandeep
                measureid = request.getParameter("measureid");//sandeep
                measurename = request.getParameter("measurename");//sandeep
                //added by sruthi for jqplotgraph
                String targetElementId = request.getParameter("targetElementId");
                String targetElemName = request.getParameter("targetElemName");
                String priorid = request.getParameter("priorid");
                //ended by sruthi
                if (height == 0 || width == 0) {
                    if (request.getParameter("height") != null) {
                        height = Integer.parseInt(request.getParameter("height").toString());
                    }
                    if (request.getParameter("width") != null) {
                        width = Integer.parseInt(request.getParameter("width").toString());
                    }
                }

                rowValues = request.getParameter("rowValues");
                GraphDisplayRows = request.getParameter("GraphDisplayRows");
                startindex = request.getParameter("startindex");
                endindex = GraphDisplayRows;
                if (request.getParameter("showLabels") != null) {
                    isShowLabels = Boolean.valueOf(request.getParameter("showLabels"));
                }

                //graph details related code end here

                grpColumnsNames = request.getParameter("grpColumnsNames");//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
                leftgrpColumns = request.getParameter("leftColumns");
                rightgrpColumns = request.getParameter("rightColumns");
                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                ReportHashMap = container.getReportHashMap();
                GraphHashMap = container.getGraphHashMap();

                GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
                // 
                GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
                // 

                //added by bugger santhosh.kumar@progenbusiness.com on 03/12/2009 fro setting userId and request object
                reportBD.setUserId(String.valueOf(session.getAttribute("USERID")));
                reportBD.setBizRoles(String.valueOf(ParametersHashMap.get("UserFolderIds")));

                //added  on 11feb10

                reportBD.setReportId(customReportId);

                if (GraphHashMap != null) {
                    if (GraphHashMap.get("AllGraphColumns") != null) {
                        allGraphColumns = (ArrayList) GraphHashMap.get("AllGraphColumns");
                    }
                    if (allGraphColumns == null) {
                        allGraphColumns = new ArrayList();
                    }
                    if (graphChange != null) {
                        if (graphChange.equalsIgnoreCase("default")) {
                            if (grpId != null && grpType != null) {
                                if (!("".equalsIgnoreCase(grpId)) && !("".equalsIgnoreCase(grpType))) {
                                    GraphHashMap = reportBD.setDefaults(grpId, grpType, container, GraphSizesDtlsHashMap, GraphClassesHashMap, isdashbaord, measureid, measurename);

                                    String[] graphIds = GraphHashMap.get("graphIds").toString().split(",");
                                    ArrayList sizeDtls = (ArrayList) GraphSizesDtlsHashMap.get("Medium");
                                    if (graphIds.length > 1) {
                                        for (int i = 0; i < graphIds.length; i++) {
                                            HashMap singleGraphdetails = (HashMap) GraphHashMap.get(graphIds[i]);
                                            singleGraphdetails.put("graphSize", "Medium");
                                            singleGraphdetails.put("graphWidth", String.valueOf(sizeDtls.get(0)));
                                            singleGraphdetails.put("graphHeight", String.valueOf(sizeDtls.get(1)));
                                        }
                                    }
                                    //
                                }
                            }
                        } else if (graphChange.equalsIgnoreCase("GrpSize")) {
                            if (grpId != null && grpSize != null) {
                                if (!("".equalsIgnoreCase(grpId)) && !("".equalsIgnoreCase(grpSize))) {
                                    GraphHashMap = reportBD.changeGraphSize(GraphHashMap, grpSize, grpId, ParametersHashMap, TableHashMap, GraphSizesDtlsHashMap);
                                }
                            }
                        } else if (graphChange.equalsIgnoreCase("GrpColumns")) {

                            ReportViewerAction viewerAction = new ReportViewerAction();
                            ActionForward graphColumnChanges = viewerAction.graphColumnChanges(mapping, form, request, response);

//                            if (grpId != null && grpColumns != null) {
//                                if (!("".equalsIgnoreCase(grpId)) && !("".equalsIgnoreCase(grpColumns))) {
//
//                                    GraphHashMap = reportBD.changeGraphColumns(GraphHashMap, grpColumns, grpId, ParametersHashMap, TableHashMap, grpColumns);
//                                    String[] graphCols = grpColumns.split(",");
//                                    for (int i = 0; i < graphCols.length; i++) {
//                                        if (!allGraphColumns.contains(graphCols[i])) {
//                                            allGraphColumns.add(graphCols[i]);
//                                        }
//                                    }
//                                    GraphHashMap.put("AllGraphColumns", allGraphColumns);
//
//                                }
//                            }
                        } else if (graphChange.equalsIgnoreCase("GrpType")) {
                            if (grpId != null && grpType != null) {
                                if (!("".equalsIgnoreCase(grpId)) && !("".equalsIgnoreCase(grpSize))) {
                                    GraphHashMap = reportBD.changeGraphType(GraphHashMap, grpType, grpId, ParametersHashMap, TableHashMap, GraphClassesHashMap);
                                }
                            }
                        } /*
                         * else if (graphChange.equalsIgnoreCase("GrpViewBys"))
                         * { if (grpId != null && grpColumns != null) { if
                         * (!("".equalsIgnoreCase(grpIds)) &&
                         * !("".equalsIgnoreCase(grpViewBys))) { GraphHashMap =
                         * reportBD.changeViewBys(GraphHashMap, grpViewBys,
                         * grpIds);
                         *
                         * }
                         * }
                         * }
                         */ else if (graphChange.equalsIgnoreCase("DeleteGraph")) {
                            if (grpId != null && grpIds != null) {
                                if (!("".equalsIgnoreCase(grpId) && ("".equalsIgnoreCase(grpIds)))) {

                                    String[] tempGraphIds = grpIds.split(",");
//                                    String temp = "";
                                    StringBuilder temp = new StringBuilder(100);
                                    if (tempGraphIds.length <= 1) {
                                        GraphHashMap.remove(grpId);
                                        grpIds = null;
                                        GraphHashMap.put("graphIds", grpIds);
                                    } else {
                                        for (int i = 0; i < tempGraphIds.length; i++) {
                                            if (tempGraphIds[i].equalsIgnoreCase(grpId)) {
                                            } else {
                                                temp.append(",").append(tempGraphIds[i]);
//                                                temp += "," + tempGraphIds[i];
                                            }
                                        }
//                                        if (!(temp.equalsIgnoreCase(""))) {
//                                            temp = temp.substring(1);
//                                        }
                                        if (temp.length() > 0) {
                                            temp = new StringBuilder(temp.substring(1));
                                        }
                                        grpIds = temp.toString();

                                        GraphHashMap.remove(grpId);
                                        GraphHashMap.put("graphIds", temp);
                                    }
                                }
                            }
                        } else if (graphChange.equalsIgnoreCase("GraphDetails")) {
                            if (grpId != null && grpTitle != null) {
//                                ////.println("before chagegrpah method display rows are : "+GraphDisplayRows);
                                GraphHashMap = reportBD.changeGraphDetails(grpId, grpTitle, grpLegend, grpLegendLoc, grpshowX, grpshowY, grplyaxislabel, grpryaxislabel, grpdrill, grpbcolor, grpfcolor, grpdata, GraphHashMap, GraphDisplayRows, startindex, endindex, isShowLabels);
                            }
                        } else if (graphChange.equalsIgnoreCase("changeDualAxisColumns")) {
                            ReportViewerAction viewerAction = new ReportViewerAction();
                            ActionForward graphColumnChanges = viewerAction.graphColumnChanges(mapping, form, request, response);

//                            if (grpId != null && grpColumns != null && leftgrpColumns != null && rightgrpColumns != null) {
//                                if (!("".equalsIgnoreCase(grpId)) && !("".equalsIgnoreCase(grpColumns))) {
//                                    GraphHashMap = reportBD.changeDualAxisColumns(GraphHashMap, grpId, ParametersHashMap, TableHashMap, leftgrpColumns, rightgrpColumns);
//                                    String[] graphCols = grpColumns.split(",");
//                                    for (int i = 0; i < graphCols.length; i++) {
//                                        if (!allGraphColumns.contains(graphCols[i])) {
//                                            allGraphColumns.add(graphCols[i]);
//                                        }
//                                    }
//                                    GraphHashMap.put("AllGraphColumns", allGraphColumns);
//                                }
//                            }
                        } else if (graphChange.equalsIgnoreCase("RowValues")) {
                            GraphHashMap = reportBD.changeRowValuesList(grpId, rowValues, GraphHashMap);
                        } else if (graphChange.equalsIgnoreCase("All")) {
                        }
                    }
                    container.setGraphHashMap(GraphHashMap);
                    if (isdashbaord != null && isdashbaord.equalsIgnoreCase("true")) {
                        ArrayList<String> targetid = new ArrayList<String>();//added by sruthi for target jqplotgraph
                        ArrayList<String> targetnames = new ArrayList<String>();//added by sruthi for terget jqplotgraph
                        ArrayList<String> priorId = new ArrayList<String>();//added by sruthi for prior jqplotgraph
                        ArrayList<String> priornames = new ArrayList<String>();//added by sruthi for prior jqplotgraph

                        HashMap singleGraphDetails = null;
                        singleGraphDetails = (HashMap) GraphHashMap.get(grpId);

                        singleGraphDetails.put("graphDisplayRows", "");
                        singleGraphDetails.put("nbrFormat", "");
                        GraphHashMap.put(grpId, singleGraphDetails);
                        container.setGraphHashMap(GraphHashMap);
                        ProGenJqPlotChartTypes jqplotcontainer = new ProGenJqPlotChartTypes();
                        StringBuilder graph = new StringBuilder();
                        String selectedgraph = "jq";
                        JqplotGraphProperty graphproperty = new JqplotGraphProperty();
                        String[] rowValues1 = null;
                        String[] tablecols1 = null;
                        rowValues1 = graphproperty.getRowValues();

                        tablecols1 = graphproperty.getTableColumns();
                        //added by sruthi for jqplotgraph
                        if (targetElementId != null && !targetElementId.equalsIgnoreCase("null")) {
                            ArrayList<String> mesid = new ArrayList<String>();
                            ArrayList<String> measurenames = new ArrayList<String>();
                            mesid = container.getDisplayColumns();
                            measurenames = container.getDisplayLabels();
                            targetnames.add(measurename);
                            targetnames.add(targetElemName);
                            targetid.add("A_" + measureid);
                            targetid.add("A_" + targetElementId);
                            for (String data1 : targetnames) {
                                if (!measurenames.contains(data1)) {
                                    measurenames.add(data1);
                                }
                            }
                            for (int i = 0; i < measurenames.size(); i++) {
                                if (measurenames.get(i).equalsIgnoreCase(targetElemName)) {
                                    mesid.add(i, "A_" + targetElementId);
                                }
                            }
                            container.setDisplayColumns(mesid);
                            container.setDisplayLabels(measurenames);
                            tablecols1 = (String[]) targetid.toArray(new String[targetid.size()]);
                        }
                        //ended by sruthi

                        //added by sruthi for prior jqplotgraph
                        if (priorid != null && !priorid.equalsIgnoreCase("null")) {
                            priorid = priorid.replace("A_", "");
                            ArrayList<String> mesid = new ArrayList<String>();
                            ArrayList<String> measurenames = new ArrayList<String>();
                            PbReturnObject allRepNameObj = null;
                            String priorname = null;
                            String query = "select user_col_desc,REF_ELEMENT_TYPE from  prg_user_all_info_details where sub_folder_type = 'Facts'and ref_element_id = " + measureid + " and REF_ELEMENT_TYPE=2";
                            PbDb pbdb = new PbDb();
                            allRepNameObj = pbdb.execSelectSQL(query);
                            mesid = container.getDisplayColumns();
                            measurenames = container.getDisplayLabels();
                            priorId.add("A_" + measureid);
                            priorId.add("A_" + priorid);
                            if (targetid != null && !targetid.isEmpty()) {
                                for (String data2 : priorId) {
                                    if (!targetid.contains(data2)) {
                                        targetid.add(data2);
                                    }
                                }
                            } else {
                                for (String data2 : priorId) {
                                    targetid.add(data2);
                                }

                            }
                            priornames.add(measurename);
                            if (allRepNameObj != null && allRepNameObj.rowCount > 0) {
                                priornames.add(allRepNameObj.getFieldValueString(0, 0));
                                priorname = allRepNameObj.getFieldValueString(0, 0);
                            }
                            for (String data1 : priornames) {
                                if (data1 != null) {
                                    if (!measurenames.contains(data1)) {
                                        measurenames.add(data1);
                                    }
                                }
                            }
                            for (int i = 0; i < measurenames.size(); i++) {
                                if (measurenames.get(i).equalsIgnoreCase(priorname)) {
                                    int index = mesid.indexOf("A_" + priorid);
                                    mesid.remove(index);
                                    mesid.add(i, "A_" + priorid);
                                }

                            }
                            container.setDisplayColumns(mesid);
                            container.setDisplayLabels(measurenames);
                            tablecols1 = (String[]) targetid.toArray(new String[targetid.size()]);

                        }
                        //ended by sruthi
                        jqplotgraphbd.chartId = "chart5515";
                        jqplotgraphbd.JqplotGraphProperty = graphproperty;
                        jqplotgraphbd.rowValues = rowValues1;
                        jqplotgraphbd.tablecols = tablecols1;
                        int screenheight = 440;
                        graph.append("<div id='chart5515'  class='cla' style='width:" + width + "px; height:" + height + "px;margin: auto'></div>");
                        ProgenDataSet retObj = container.getRetObj();
                        ProgenDataSet retObj1 = container.getdashretobj();
                        container.setRetObj(container.getdashretobj());
                        ArrayList viewSequence = new ArrayList();
                        ArrayList sortCols = new ArrayList();
                        char[] sortTypes = null;//ArrayList sortTypes = null;
                        char[] sortDataTypes = null;
                        String valumsid = "A_" + measureid;
                        sortCols.add(valumsid);
                        String val = "1";
                        String val1 = "N";
//                                    container.setsorttype(val);
                        sortTypes = container.getColumnDataTypes(sortCols);
                        sortTypes = new char[sortCols.size()];
                        sortDataTypes = container.getColumnDataTypes(sortCols);
                        ArrayList<Integer> rowSequence = retObj1.sortDataSet(sortCols, sortTypes, sortDataTypes);//dataTypes, container.getOriginalColumns());
                        retObj1.setViewSequence(rowSequence);

                        container.setRetObj(retObj1);
                        jqplotgraphbd.tickId = "";
                        graph.append("<script>");
                        graph.append(jqplotgraphbd.prepareJqplotGraph(customReportId, grpId, grpType, jqplotcontainer, request, null, selectedgraph, grpId));
                        graph.append("</script>");
                        graph.append("</div>");
                        retObj = (ProgenDataSet) session.getAttribute("originalretobj");
//                                 int cnt=retObj.rowCount;
                        container.setRetObj(retObj);
                        out.print(graph.toString());
                    } else {
                        if (graphTableMethod != null && graphTableMethod.equalsIgnoreCase("GTM")) {
                            //////.println("graphTableMethod----" + graphTableMethod);
                            result = reportBD.buildGraphTable(container, request, response, grpIds);
                        } else {
                            result = reportBD.buildGraph(container, request, response, grpIds);
                        }
                        out.print(result);
                    }
                }
                return null;
            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //wnen user clicks on preview(Parameters) in report designing
    public ActionForward dispParams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String params = null;
        HashMap ParametersHashMap = null;
        String result = null;
        PrintWriter out = null;
        String[] parameterIds = null;
        ArrayList Parameters = new ArrayList();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            try {
                customReportId = request.getParameter("REPORTID");
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(customReportId);
                params = request.getParameter("params");
                //ParametersHashMap = container.getParametersHashMap();
                //update Param hashmap in session

                if (params != null) {
                    parameterIds = params.split(",");
                    for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                        Parameters.add(parameterIds[paramCount]);//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
                    }
                }

                ParametersHashMap = new HashMap();
                ParametersHashMap.put("Parameters", Parameters);
                //container.setParametersHashMap(ParametersHashMap);

                params = Joiner.on(",").join(container.getParameterElements());

                ParametersHashMap.put("TimeDimHashMap", container.getReportCollect().timeDetailsMap);
                ParametersHashMap.put("timeParameters", container.getReportCollect().timeDetailsArray);

                //  result = reportTemplateDAO.dispParameters(params, ParametersHashMap);
                //added by susheela start

                result = reportTemplateDAO.dispParameters(params, ParametersHashMap, customReportId);
                //added by susheela start
                out = response.getWriter();
                out.print(result);
                return null;
            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //when user clicks on preview(Table) in report designing
    public ActionForward dispTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String filesPath = "";
//        String result = "";
//
//        HashMap ParametersHashMap = null;
//        HashMap TableHashMap = null;
//        HashMap GraphHashMap = null;
        PrintWriter out = response.getWriter();
        String userId;
        String KpiDashboard;
        boolean aoflag;
        String action;
        ReportTemplateBD reportBD = new ReportTemplateBD();

        if (session != null) {
            try {
                customReportId = request.getParameter("REPORTID");
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(customReportId);
                userId = (String) session.getAttribute("USERID");
                action = request.getParameter("action");
                KpiDashboard = request.getParameter("isKpiDashboard");
                aoflag = Boolean.valueOf(request.getParameter("aoflag"));
                container.isAOReport = aoflag;
                PbReportViewerDAO pb = new PbReportViewerDAO();
                filesPath = pb.getFilePath(session);
                container.setFilesPath(filesPath);
//                ParametersHashMap = container.getParametersHashMap();
//                TableHashMap = container.getTableHashMap();
//                GraphHashMap = container.getGraphHashMap();
//                reportBD.setUserId(String.valueOf(session.getAttribute("USERID")));
//                reportBD.setBizRoles(String.valueOf(ParametersHashMap.get("UserFolderIds")));
//                reportBD.setReportId(customReportId);

                //result = reportBD.buildTable(container, request.getContextPath(), session);
                HashMap timehash = new HashMap();
                String paramChange1 = request.getParameter("paramChange1");
                if (request.getParameter("action") != null && paramChange1 != null && paramChange1.equalsIgnoreCase("paramChange1")) {
                    timehash.put("CBO_PRG_COMPARE", request.getParameter("CBO_PRG_COMPARE"));
                    timehash.put("perioddate", request.getParameter("perioddate"));
                    timehash.put("CBO_AS_COF_DATE", request.getParameter("CBO_AS_COF_DATE"));
                    timehash.put("CBO_AS_COF_DATE1", request.getParameter("CBO_AS_COF_DATE1"));
                    timehash.put("CBO_PRG_PERIOD_TYPE", request.getParameter("CBO_PRG_PERIOD_TYPE"));
                    timehash.put("datetext", request.getParameter("datetext"));
                    timehash.put("fromdate", request.getParameter("fromdate"));
                    timehash.put("todate", request.getParameter("todate"));
                    timehash.put("comparefrom", request.getParameter("comparefrom"));
                    timehash.put("compareto", request.getParameter("compareto"));
                    timehash.put("CBO_AS_OF_DATE1", request.getParameter("CBO_AS_OF_DATE1"));
                    timehash.put("CBO_AS_OF_DATE11", request.getParameter("CBO_AS_OF_DATE11"));
                    timehash.put("CBO_AS_OF_DATE2", request.getParameter("CBO_AS_OF_DATE2"));
                    timehash.put("CBO_AS_OF_DATE21", request.getParameter("CBO_AS_OF_DATE21"));
                    timehash.put("CBO_CMP__AS_OF_DATE1", request.getParameter("CBO_CMP_AS_OF_DATE1"));
                    timehash.put("CBO_CMP_AS_OF_DATE11", request.getParameter("CBO_CMP_AS_OF_DATE11"));
                    timehash.put("CBO_CMP_AS_OF_DATE2", request.getParameter("CBO_CMP_AS_OF_DATE2"));
                    timehash.put("CBO_CMP_AS_OF_DATE21", request.getParameter("CBO_CMP_AS_OF_DATE21"));
                }
                String result = reportBD.buildTable(action, container, userId, timehash);
//                String tabStr = result.split("ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¥")[0];
//                String sqlStr = result.split("ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¥")[1];
//                //////.println("in action sqlstr is : " + sqlStr);
//                session.setAttribute("sqlStr", sqlStr);
//                //////.println("in action result is : " + result);
//                out.print(tabStr + "@!" + sqlStr);
                out.print(result);
                return null;

            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    ////used to preview graphs
    public ActionForward refreshGraphs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String result = "";
        String grpIds = "";
        HashMap GraphHashMap = null;
        HashMap ParametersHashMap = null;
        PrintWriter out = response.getWriter();
        String graphTableMethod = null;
        ReportTemplateBD reportBD = new ReportTemplateBD();
        if (session != null) {
            try {
                customReportId = request.getParameter("REPORTID");
                map = (HashMap) session.getAttribute("PROGENTABLES");
                graphTableMethod = request.getParameter("graphTableMethod");
                container = (Container) map.get(customReportId);
                GraphHashMap = container.getGraphHashMap();
                ParametersHashMap = container.getParametersHashMap();
                if (GraphHashMap.get("graphIds") != null) {
                    if (GraphHashMap.get("graphIds") != null) {
                        grpIds = String.valueOf(GraphHashMap.get("graphIds"));
                    } else {
                        grpIds = null;
                    }
                }
                reportBD.setUserId(String.valueOf(session.getAttribute("USERID")));
                reportBD.setBizRoles(String.valueOf(ParametersHashMap.get("UserFolderIds")));
                reportBD.setReportId(customReportId);
                if (graphTableMethod != null && graphTableMethod.equalsIgnoreCase("GTM")) {
                    result = reportBD.buildGraphTable(container, request, response, grpIds);
                } else {
                    result = reportBD.buildGraph(container, request, response, grpIds);
                }
                out.print(result);
                return null;

            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }

        } else {
            return mapping.findForward("sessionExpired");
        }
    }
//checks for duplicate name in create report stage

    public ActionForward checkReportName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String reportName = request.getParameter("reportName");
        reportName = reportName.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
        reportName = reportName.replace("'", "''");
        String roleId = request.getParameter("roleid");
        String status = "";


        HttpSession session = request.getSession(false);
        if (session != null) {
            String PbUserId = String.valueOf(session.getAttribute("USERID"));
            ReportTemplateDAO tempDAO = new ReportTemplateDAO();
            boolean exists = tempDAO.checkReportNameExists(reportName, PbUserId, roleId);
            if (exists) {
                status = "Report Name Exists";
            }
            response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
//once designing is done and if user clicks on next button in report designing then it inserts into database and goes to report adssignment page

    public ActionForward saveReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        ServletContext context = this.getServlet().getServletContext();
        boolean isCompanyValid = Boolean.parseBoolean(context.getInitParameter("isCompanyValid"));

        HashMap parametersMap = null;
        HashMap TableHashMap = null;
        HashMap reportMap = null;
        HashMap GraphHashMap = null;

        String reportName = null;
        String reportDesc = null;
        String UserFolderIds = null;
        String buildGraph = null;
        String buildTable = null;
        String graphTableHidden = null;
        String aoId = null;
        String cacheAO = "false";
        ReportTemplateBD reportBD = new ReportTemplateBD();

        String reportId = "";
        if (session != null) {
            reportId = request.getParameter("REPORTID");
            aoId = request.getParameter("aoId");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(reportId);
            graphTableHidden = request.getParameter("graphTableHidden");
            String isKpiDashboard = request.getParameter("isKpiDashboard");
            String isTimeDashboard = request.getParameter("isTimeDashboard");
//            container.isAOEnable = false;
            //code modified by Bhargavi
//            if(isKpiDashboard!=null && !isKpiDashboard.equalsIgnoreCase("true")){
            cacheAO = request.getParameter("cacheAo");
            if (cacheAO != null && cacheAO.equalsIgnoreCase("true")) {     //added by mohit for kpi and none
                container.isAOEnable = true;
            } else {
                container.isAOEnable = false;
            }
//            }
            //end of code
            double currReportVersion = Double.parseDouble(session.getAttribute("REPORTVERSION").toString());
            container.getReportCollect().setCurrentRepVersion(currReportVersion);


            parametersMap = container.getParametersHashMap();
            TableHashMap = container.getTableHashMap();
            reportMap = container.getReportHashMap();
            GraphHashMap = container.getGraphHashMap();
            if (TableHashMap == null || TableHashMap.size() == 0) {
                if (parametersMap.containsKey("Parameters")) {
                    String params = parametersMap.get("Parameters").toString();
                    if (params.contains("[")) {
                        params = params.replace("[", "");
                    }
                    if (params.contains("]")) {
                        params = params.replace("]", "");
                    }
                    if (params.contains(",")) {
                        params = params.substring(0, params.indexOf(","));
                    }
//                    //////.println("params" + params);
                    ArrayList reps = new ArrayList();
                    if (params != null && params.length() != 0) {
                        reps.add(params);
                        TableHashMap.put("REP", reps);
                    }
                }
                if (parametersMap.containsKey("ParametersNames")) {
                    String paramNms = parametersMap.get("ParametersNames").toString();

                    paramNms = paramNms.substring(0, paramNms.indexOf(","));
                    ArrayList repNms = new ArrayList();
                    if (paramNms.contains("[")) {
                        paramNms = paramNms.replace("[", "");
                    }
                    if (paramNms.contains("]")) {
                        paramNms = paramNms.replace("]", "");
                    }
                    if (paramNms.contains(",")) {
                        paramNms = paramNms.substring(0, paramNms.indexOf(","));
                    }
//                    //////.println("paranms" + paramNms);
                    if (paramNms != null && paramNms.length() != 0) {
                        repNms.add(paramNms);
                        TableHashMap.put("REPNames", repNms);
                    }
                }
                container.setTableHashMap(TableHashMap);
            }




//            //////.println("reportMap\t" + reportMap);
//            //////.println("GraphHashMap\t" + GraphHashMap);
//            //////.println("paramDefaultValuesHashMap\t" + paramDefaultValuesHashMap);
            if (GraphHashMap.containsKey("AllGraphColumns")) {
//                //////.println(GraphHashMap.get("AllGraphColumns"));
                buildGraph = "yes";
            } else {
                buildGraph = "no";
            }
            if (TableHashMap == null) {
                buildTable = "no";
            } else if (TableHashMap != null && TableHashMap.size() != 0) {
                buildTable = "yes";
            }
//            //////.println("buildgraph is" + buildGraph);
//            //////.println("buildtable is" + buildTable);
            session.setAttribute("buildgraph", buildGraph);
            session.setAttribute("buildtable", buildTable);
            reportName = container.getReportName();//String.valueOf(reportMap.get("ReportName"));
            reportDesc = container.getReportDesc();//String.valueOf(reportMap.get("ReportDesc"));
            UserFolderIds = String.valueOf(parametersMap.get("UserFolderIds"));
            reportBD.setGetGraphIdFromSequence(false);//added by bugger santhosh.k on 09-03-2010

//            reportId = reportBD.saveReport(parametersMap, reportMap, TableHashMap, GraphHashMap, reportName, reportDesc, UserFolderIds, Integer.parseInt(reportId), paramDefaultValuesHashMap);
            if (graphTableHidden != null && !graphTableHidden.equalsIgnoreCase("")) {
//                //////.println("::::graphTableHidden:::"+graphTableHidden);
                reportBD.setGraphTableHidden(graphTableHidden);
            }
            String oldReportId = reportId;
            String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String fromdesigner = request.getParameter("Designer");
            if (fromdesigner != null && !fromdesigner.isEmpty() && fromdesigner.equalsIgnoreCase("fromInsightDesigner")) {
                request.setAttribute("ReportType", "I");
                container.setReportType("I");
            } else {
                request.setAttribute("ReportType", "R");
                container.setReportType("R");
            }
            reportId = reportBD.saveReport(container, reportName, reportDesc, Integer.parseInt(reportId), userId);

            if (!oldReportId.equals(reportId)) {
                container.setReportId(reportId);
                container.setTableId(reportId);
                map.remove(oldReportId);
                container.setSessionContext(session, container);

            }
            //map.remove(reportId); commented by bugger santhosh.k on 08-03-2010 for going back to report designer on clicking of back/cancel button
            ////.println("Report_id for user" +reportId);

            request.setAttribute("REPORTID", reportId);
            request.setAttribute("UserFolderIds", String.valueOf(parametersMap.get("UserFolderIds")));
            request.setAttribute("sourcePage", "Designer");
            request.setAttribute("ReportName", reportName);
            if (fromdesigner == null) {
                fromdesigner = "";
            }
            if (fromdesigner != null && !fromdesigner.isEmpty() && fromdesigner.equalsIgnoreCase("fromInsightDesigner")) {
                String users = (String) session.getAttribute("USERID");
                String reportId1 = reportId;
                String finalUsers1 = users;
                if (finalUsers1.equalsIgnoreCase("")) {
                    finalUsers1 = (String) session.getAttribute("USERID");
                }
                String finalUserIds1[] = finalUsers1.split(",");
                ReportTemplateDAO templateDAO = new ReportTemplateDAO();
                templateDAO.assignReportToUsers(reportId1, finalUserIds1);
                PbReportViewerBD viewerBd = new PbReportViewerBD();
                String action = request.getParameter("action");
                viewerBd.prepareReport(action, container, container.getReportCollect().reportId, userId, new HashMap());
                // viewerBd.prepareReport(action, container, reportId, userId, new HashMap());
                response.getWriter().print("saveInsight");
                return null;
            } else if (fromdesigner != null && !fromdesigner.isEmpty() && fromdesigner.equalsIgnoreCase("fromdesigner")) {
                String users = (String) session.getAttribute("USERID");

                String reportId1 = reportId;
                String finalUsers1 = users;
                if (finalUsers1.equalsIgnoreCase("")) {
                    finalUsers1 = (String) session.getAttribute("USERID");
                }
                String finalUserIds1[] = finalUsers1.split(",");
                ReportTemplateDAO templateDAO = new ReportTemplateDAO();
                templateDAO.assignReportToUsers(reportId1, finalUserIds1);
                if (isKpiDashboard != null && isKpiDashboard.equalsIgnoreCase("true")) {
                    request.setAttribute("isKPIDashboard", "true");
                    request.setAttribute("isTimeDashboard", container.IsTimedasboard());
                    return mapping.findForward("reportView");
                } else {
                    return mapping.findForward("reportView");
                }
            } else {
                if (isCompanyValid) {
                    return mapping.findForward("reportAssignmentWithCompany");
                } else {
                    return mapping.findForward("reportAssignment");
                }
            }

        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward checkRportNGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        Container container = null;
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;
        HashMap parametersMap = null;
        HashMap reportMap = null;
        HashMap map = new HashMap();
        String reportId = "";
        PrintWriter out = response.getWriter();
        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            reportId = request.getParameter("REPORTID");
            //////.println("reportId is" + reportId);
            container = (Container) map.get(reportId);
            //////.println("reportId is" + reportId);
            //////.println("container object" + container);
            TableHashMap = container.getTableHashMap();
            GraphHashMap = container.getGraphHashMap();
            parametersMap = container.getParametersHashMap();
            reportMap = container.getReportHashMap();
            //////.println("reportMap" + reportMap.keySet());
            //////.println("parametersMap" + parametersMap.keySet());


            /*
             * if (parametersMap.containsKey("TimeParametersNames")) {
             * //////.println(parametersMap.get("TimeParametersNames")); } if
             * (parametersMap.containsKey("TimeParameters")) {
             * //////.println(parametersMap.get("TimeParameters")); } if
             * (parametersMap.containsKey("Parameters")) {
             * //////.println(parametersMap.get("Parameters")); } if
             * (parametersMap.containsKey("ParametersNames")) {
             * //////.println(parametersMap.get("ParametersNames")); } if
             * (parametersMap.containsKey("TimeDetailstList")) {
             * //////.println(parametersMap.get("TimeDetailstList")); } if
             * (parametersMap.containsKey("TimeDimHashMap")) {
             * //////.println(parametersMap.get("TimeDimHashMap")); } if
             * (parametersMap.containsKey("timeParameters")) {
             * //////.println(parametersMap.get("timeParameters")); } if
             * (parametersMap.containsKey("UserFolderIds")) {
             * //////.println(parametersMap.get("UserFolderIds")); } if
             * (GraphHashMap.containsKey("AllGraphColumns")) {
             * //////.println(GraphHashMap.get("AllGraphColumns"));
            }
             */


            //////.println("TableHashMap.keySet()" + TableHashMap.keySet());
            //////.println("GraphHashMap.keySet()" + GraphHashMap.keySet());
            //////.println("paramdefaultvalues hashmap" + paramDefaultValuesHashMap.keySet());

            /*
             * if (GraphHashMap.keySet() == null || GraphHashMap.size() == 0) {
             * if (TableHashMap.keySet() == null || TableHashMap.size() == 0) {
             * out.print("Please Select Table"); //////.println("Please Select
             * Table"); } else if (GraphHashMap == null || GraphHashMap.size()
             * == 0) { out.print("Please Select Graph"); } else if (TableHashMap
             * == null && GraphHashMap == null && GraphHashMap.size() == 0 &&
             * TableHashMap.size() == 0) { out.print("Please Select Table and
             * Graph"); //////.println("Please Select Table and Graph"); } else
             * { out.print("ok"); //////.println("ok"); } } else if
             * (GraphHashMap.keySet() == null || GraphHashMap.size() == 0) { if
             * (GraphHashMap.containsKey("AllGraphColumns")) { ArrayList chklist
             * = (ArrayList) GraphHashMap.get("AllGraphColumns"); if
             * (GraphHashMap.get("AllGraphColumns") == null || chklist.size() ==
             * 0) { out.print("Please Select Graph Columns To Add Graph");
             * //////.println("Please Select Graph Columns To Add Graph"); } } }
             */
            if (TableHashMap.size() == 0 && GraphHashMap.size() == 0) {
                out.print("3");
                //////.println("Please Select Table and Graph");
            } else if (TableHashMap.size() == 0 || TableHashMap.keySet().size() == 0) {
                out.print("4");
                //////.println("please Select table");
            } else if (TableHashMap != null && TableHashMap.keySet() != null) {
                if (!TableHashMap.containsKey("Measures")) {
                    out.print("5");
                    //////.println("please select measures");
                } else {
                    out.print("ok");
                }
            } else {
                out.print("Error");
            }
            /*
             * else if(parametersMap.keySet()!=null || parametersMap.size()!=0){
             * out.print("Please Select Graph Columns To Add Graph");
             * //////.println("Please Select Graph Columns To Add Graph");
            }
             */
            /*
             * else { if (GraphHashMap == null || GraphHashMap.size() == 0) {
             * out.print("Please Select Graph"); if (parametersMap == null ||
             * parametersMap.size() == 0) { out.print("Please Select Graph
             * Columns To Add Graph"); } } else if (TableHashMap == null &&
             * GraphHashMap == null && GraphHashMap.size() == 0 &&
             * TableHashMap.size() == 0) { out.print("Please Select Table and
             * Graph"); } else { out.print("ok"); }
            }
             */
        }
        return null;
    }

    public ActionForward getGraphDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PbReturnObject pbro = null;
        HttpSession session = request.getSession(false);
        if (session != null) {

            try {
                PrintWriter pw = response.getWriter();
                String opCode = request.getParameter("opCode");

                if (opCode.equals("1")) {
                    pbro = new ReportTemplateDAO().getGraphTypes();
                    StringBuilder value = new StringBuilder(100);
//                    String value = "";
                    for (int i = 0; i < pbro.getRowCount(); i++) {
//                        value = value + pbro.getFieldValueString(i, 0) + "~" + pbro.getFieldValueString(i, 1) + "\n";
                        value.append(pbro.getFieldValueString(i, 0)).append("~").append(pbro.getFieldValueString(i, 1)).append("\n");
                    }

                    pw.print(value);
                } else if (opCode.equals("2")) {
                    pbro = new ReportTemplateDAO().getGraphSizes();
                    StringBuilder value = new StringBuilder(100);
//                    String value = "";
                    for (int i = 0; i < pbro.getRowCount(); i++) {
//                        value = value + pbro.getFieldValueString(i, 0) + "~" + pbro.getFieldValueString(i, 1) + "\n";
                        value.append(pbro.getFieldValueString(i, 0)).append("~").append(pbro.getFieldValueString(i, 1)).append("\n");//+ "~" + pbro.getFieldValueString(i, 1) + "\n";
                    }
                    pw.print(value);
                }
                return null;
            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getAllReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();
            }
            PbReturnObject pbro = repdao.getUserReportsAndDashboards(userid);
            request.setAttribute("reportList", pbro);
            return mapping.findForward("reportList");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getAllReportshome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String userid = String.valueOf(session.getAttribute("USERID"));
            PbReturnObject pbro = new ReportTemplateDAO().getAllReportshome(String.valueOf(session.getAttribute("USERID")));

            if (!(pbro.getRowCount() > 0)) {

                String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
                AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userid;
                AvailableFiolers += " union ";
                AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
                AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userid;
                AvailableFiolers += "and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userid + ")))";

// PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
                PbReturnObject folderpbro = new PbDb().execSelectSQL(AvailableFiolers);

                if ((folderpbro.getRowCount() > 0)) {
//                    String folderList = "";
                    StringBuilder folderList = new StringBuilder(100);
                    for (int i = 0; i < folderpbro.getRowCount(); i++) {
//                        folderList += "," + folderpbro.getFieldValueInt(i, 0);
                        folderList.append(",").append(folderpbro.getFieldValueInt(i, 0));
                    }
//                    if (!folderList.equalsIgnoreCase("")) {
//                        folderList = folderList.substring(1);
                    if (folderList.length() > 0) {
                        folderList = new StringBuilder(folderList.substring(1));
                        //String rolereps = "SELECT distinct report_id,report_name, report_desc, report_type FROM PRG_AR_REPORT_MASTER where REPORT_ID in(select distinct report_id from PRG_AR_REPORT_DETAILS where folder_id in (" + folderList + "))  order by report_id DESC";
                        String rolereps = "select a.report_id,a.report_name, a.report_desc, a.report_type, b.folder_name,b.folder_created_on  from prg_ar_report_master a,prg_user_folder b where report_id in (select distinct c.report_id from PRG_AR_REPORT_DETAILS c,prg_ar_user_reports d where c.folder_id in (" + folderList + ") and c.report_id= d.report_id and d.user_id=" + userid + " and b.folder_id= c.folder_id) and b.folder_id in (" + folderList + ") order by upper(a.report_name) asc";
                        PbReturnObject rolereppbro = new PbDb().execSelectSQL(rolereps);
                        pbro = rolereppbro;
                    }
                }
            }
            request.setAttribute("allrepList", pbro);
            return mapping.findForward("allrepList");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getAllDashs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }
            PbReturnObject pbro = repdao.getUserDashboards(userid);

            if (!(pbro.getRowCount() > 0)) {

                String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
                AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userid;
                AvailableFiolers += " union ";
                AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
                AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userid;
                AvailableFiolers += "and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userid + ")))";

// PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
                PbReturnObject folderpbro = new PbDb().execSelectSQL(AvailableFiolers);

                if ((folderpbro.getRowCount() > 0)) {
//                      String folderList = "";

                    StringBuilder folderList = new StringBuilder(100);
                    for (int i = 0; i < folderpbro.getRowCount(); i++) {
//                        folderList += "," + folderpbro.getFieldValueInt(i, 0);
                        folderList.append(",").append(folderpbro.getFieldValueInt(i, 0));
                    }
//                    if (!folderList.equalsIgnoreCase("")) {
//                        folderList = folderList.substring(1);
                    if (folderList.length() > 0) {
                        folderList = new StringBuilder(folderList.substring(1));

                        // String rolereps = "SELECT distinct REPORT_ID, REPORT_NAME,REPORT_TYPE FROM PRG_AR_REPORT_MASTER where REPORT_ID in(select distinct report_id from PRG_AR_REPORT_DETAILS where folder_id in (" + folderList + ")) and REPORT_TYPE='D' order by upper(report_name) asc";
                        String rolereps = "";
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            rolereps = "select  rm.report_id,rm.report_name,rm.report_type,uf.folder_name,uf.folder_created_on from prg_ar_report_master rm,prg_user_folder uf "
                                    + "where rm.report_id in (select distinct a.report_id from PRG_AR_REPORT_DETAILS a,prg_ar_user_reports b "
                                    + "where a.folder_id in (" + folderList + ") and a.report_id= b.report_id and b.user_id=" + userid + " and uf.folder_id= a.folder_id) and rm.report_type='D' and uf.folder_id in (" + folderList + ") order by upper(rm.report_name) asc";
                        } else {
                            rolereps = "select distinct rm.report_id,rm.report_name,rm.report_type,uf.folder_name,uf.folder_created_on from prg_ar_report_master rm,prg_user_folder uf "
                                    + "where rm.report_id in (select distinct a.report_id from PRG_AR_REPORT_DETAILS a,prg_ar_user_reports b "
                                    + "where a.folder_id in (" + folderList + ") and a.report_id= b.report_id and b.user_id=" + userid + " and uf.folder_id= a.folder_id) and rm.report_type='D' and uf.folder_id in (" + folderList + ") order by upper(rm.report_name) asc";



                        }

                        PbReturnObject rolereppbro = new PbDb().execSelectSQL(rolereps);
                        pbro = rolereppbro;
                    }
                }
            }


            request.setAttribute("dashList", pbro);
            return mapping.findForward("dashList");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getAllUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            PbReturnObject pbro = new ReportTemplateDAO().getAllUsers();
            session.setAttribute("userList", pbro);
            return mapping.findForward("userList");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getAllreps(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }

            PbReturnObject pbro = repdao.getUserReports(userid);
            if (!(pbro.getRowCount() > 0)) {

                String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
                AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userid;
                AvailableFiolers += " union ";
                AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
                AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userid;
                AvailableFiolers += "and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userid + ")))";

// PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
                PbReturnObject folderpbro = new PbDb().execSelectSQL(AvailableFiolers);

                if ((folderpbro.getRowCount() > 0)) {
//                    String folderList = "";
                    StringBuilder folderList = new StringBuilder(100);
                    for (int i = 0; i < folderpbro.getRowCount(); i++) {
//                        folderList += "," + folderpbro.getFieldValueInt(i, 0);
                        folderList.append(",").append(folderpbro.getFieldValueInt(i, 0));
                    }
//                    if (!folderList.equalsIgnoreCase("")) {
//                        folderList = folderList.substring(1);
                    if (folderList.length() > 0) {
                        folderList = new StringBuilder(folderList.substring(1));
                        //    String rolereps = "SELECT distinct REPORT_ID, REPORT_NAME,REPORT_TYPE FROM PRG_AR_REPORT_MASTER where REPORT_ID in(select distinct report_id from PRG_AR_REPORT_DETAILS where folder_id in (" + folderList + ")) and REPORT_TYPE='R' order by upper(report_name) asc";
                        String rolereps = "select rm.report_id,rm.report_name,rm.report_type,uf.folder_name,uf.folder_created_on from prg_ar_report_master rm,prg_user_folder uf "
                                + "where rm.report_id in (select a.report_id from PRG_AR_REPORT_DETAILS a,prg_ar_user_reports b "
                                + "where a.folder_id in (" + folderList + ") and a.report_id= b.report_id and b.user_id=" + userid + " and uf.folder_id= a.folder_id) and rm.report_type='R' and uf.folder_id in (" + folderList + ") order by upper(rm.report_name) asc";
                        PbReturnObject rolereppbro = new PbDb().execSelectSQL(rolereps);
                        pbro = rolereppbro;
                    }
                }
            }

            request.setAttribute("repList", pbro);
            return mapping.findForward("repList");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getPurgeReps(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }

            PbReturnObject pbro = repdao.getUserReports(userid);
            if (!(pbro.getRowCount() > 0)) {

                String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
                AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userid;
                AvailableFiolers += " union ";
                AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
                AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userid;
                AvailableFiolers += "and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userid + ")))";

// PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
                //////.println("----AvailableFiolers----::" + AvailableFiolers);
                PbReturnObject folderpbro = new PbDb().execSelectSQL(AvailableFiolers);

                if ((folderpbro.getRowCount() > 0)) {
//                    String folderList = "";
                    StringBuilder folderList = new StringBuilder(100);
                    for (int i = 0; i < folderpbro.getRowCount(); i++) {
//                        folderList += "," + folderpbro.getFieldValueInt(i, 0);
                        folderList.append(",").append(folderpbro.getFieldValueInt(i, 0));
                    }
//                    if (!folderList.equalsIgnoreCase("")) {
//                        folderList = folderList.substring(1);
                    if (folderList.length() > 0) {
                        folderList = new StringBuilder(folderList.substring(1));
                        //    String rolereps = "SELECT distinct REPORT_ID, REPORT_NAME,REPORT_TYPE FROM PRG_AR_REPORT_MASTER where REPORT_ID in(select distinct report_id from PRG_AR_REPORT_DETAILS where folder_id in (" + folderList + ")) and REPORT_TYPE='R' order by upper(report_name) asc";
                        String rolereps = "select distinct rm.report_id,rm.report_name,rm.report_type,uf.folder_name,uf.folder_created_on from prg_ar_report_master rm,prg_user_folder uf "
                                + "where rm.report_id in (select distinct a.report_id from PRG_AR_REPORT_DETAILS a,prg_ar_user_reports b "
                                + "where a.folder_id in (" + folderList + ") and a.report_id= b.report_id and b.user_id=" + userid + " and uf.folder_id= a.folder_id) and rm.report_type='R' and uf.folder_id in (" + folderList + ") order by upper(rm.report_name) asc";
                        //////.println("-----rolereps------:" + rolereps);
                        PbReturnObject rolereppbro = new PbDb().execSelectSQL(rolereps);
                        pbro = rolereppbro;
                    }
                }
            }

            request.setAttribute("repPurgeList", pbro);
            return mapping.findForward("repPurgeList");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward repStudioSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        // HashMap repStudioSortmap = new HashMap();
        if (session != null) {
            String selectValue = request.getParameter("selectValue");
            // //////.println("-------selectValue------"+selectValue);
            String seloption = request.getParameter("seloption");
            // //////.println("---seloption-----"+seloption);
            session.setAttribute("selectValue", selectValue);
            session.setAttribute("seloption", seloption);
            //  //////.println("after---selectValue--"+session.getAttribute("selectValue"));
            //   //////.println("after---seloption--"+session.getAttribute("seloption"));
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }

            PbReturnObject pbro = repdao.getrepStudioSort(userid, seloption, selectValue);
            //  //////.println("----pbro----"+pbro);
            session.setAttribute("repStudioSortList", pbro);
            //    //////.println("llllll-------"+session.getAttribute("repStudioSortList"));

            // return mapping.findForward("repStudioSortList");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward repPurgeSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        // HashMap repStudioSortmap = new HashMap();
        if (session != null) {
            String selectValue = request.getParameter("selectValuePurge");
            // //////.println("-------selectValue------"+selectValue);
            String seloption = request.getParameter("seloptionPurge");
            // //////.println("---seloption-----"+seloption);
            session.setAttribute("selectValuePurge", selectValue);
            session.setAttribute("seloptionPurge", seloption);
            //  //////.println("after---selectValue--"+session.getAttribute("selectValue"));
            //   //////.println("after---seloption--"+session.getAttribute("seloption"));
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }

            PbReturnObject pbro = repdao.getrepPurgeSort(userid, seloption, selectValue);
            //  //////.println("----pbro----"+pbro);
            session.setAttribute("repPurgeSortList", pbro);
            //    //////.println("llllll-------"+session.getAttribute("repStudioSortList"));

            // return mapping.findForward("repStudioSortList");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward allReportSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        if (session != null) {
            String selectValue = request.getParameter("selectValue");
            //   //////.println("-------selectValue------"+selectValue);
            String seloption = request.getParameter("seloption");
            //   //////.println("---seloption-----"+seloption);
            session.setAttribute("selectValueAllRep", selectValue);
            session.setAttribute("seloptionAllRep", seloption);
            //    //////.println("after---selectValue--"+session.getAttribute("selectValue"));
            //    //////.println("after---seloption--"+session.getAttribute("seloption"));
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }

            PbReturnObject pbro = repdao.getallReportSort(userid, seloption, selectValue);
            //    //////.println("----pbro----"+pbro);
            session.setAttribute("allRepSortList", pbro);
            //    //////.println("llllll-------"+session.getAttribute("repStudioSortList"));

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward dashStudioSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session = request.getSession(true);
        HttpSession session = request.getSession(false);
        if (session != null) {
            String selectValue = request.getParameter("selectValue");
            //   //////.println("-------selectValue------"+selectValue);
            String seloption = request.getParameter("seloption");
            //   //////.println("---seloption-----"+seloption);
            session.setAttribute("selectValueDash", selectValue);
            session.setAttribute("seloptionDash", seloption);
            //    //////.println("after---selectValue--"+session.getAttribute("selectValue"));
            //     //////.println("after---seloption--"+session.getAttribute("seloption"));
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }

            PbReturnObject pbro = repdao.getDashSort(userid, seloption, selectValue);
            //   //////.println("----pbro----"+pbro);
            session.setAttribute("dashSortList", pbro);
            //  //////.println("llllll-------" + session.getAttribute("repStudioSortList"));

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //added by bugger santhosh.kumar@progenbusiness.com on 03/11/2009 for retreiving User Buckets based on the business roles selected in report designing
    public ActionForward getBuckets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        HashMap ParametersHashMap = null;
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String result = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            foldersIds = request.getParameter("foldersIds");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);


            ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", foldersIds);
            result = reportTemplateDAO.getBuckets(foldersIds);

            out.print(result);
            container.setParametersHashMap(ParametersHashMap);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward Deletedashboard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            String deletingids = request.getParameter("dashboardid");

            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }
            repdao.DeleteUserdashboards(userid, deletingids);
//           return mapping.findForward("dashList");
            //    return mapping.findForward("DashboardStudio");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward purgeDashboard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            DashboardTemplateDAO dashdao = new DashboardTemplateDAO();
            String userid = "";
            String dashboardids = request.getParameter("dashboardid");

            String[] reportIds = dashboardids.split(",");
            for (int i = 0; i < reportIds.length; i++) {
                dashdao.purgeDashboard(reportIds[i]);
            }

//           return mapping.findForward("dashList");
            //    return mapping.findForward("DashboardStudio");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward DeleteUserReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            String deleterepids = request.getParameter("deleterepids");
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }
            repdao.DeleteUserReports(userid, deleterepids);
//         return mapping.findForward("repList");
            // return mapping.findForward("ReportStudio");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward PurgeReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            String purgeRepids = request.getParameter("purgeRepids");
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }
            repdao.PurgeReportsDAO(userid, purgeRepids);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward clearCache(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            String reportIdsParam = request.getParameter("clearCacheids");
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();
            }
            String[] reportIds = reportIdsParam.split(",");

//            for ( String reportId : reportIds )
//                ReportCacheManager.MANAGER.clearReportQueryCache(reportId);
            repdao.clearCache(reportIds);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getCustomMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        HashMap ParametersHashMap = null;
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String result = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            foldersIds = request.getParameter("foldersIds");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);


            ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", foldersIds);
            result = reportTemplateDAO.getCustomMembers(foldersIds);
            out.print(result);
            container.setParametersHashMap(ParametersHashMap);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //added by Mahesh for Favourite Parameters on 29/12/2009
    public ActionForward checkParamName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String favName = request.getParameter("favName");
        String status = "";
        PbReturnObject pbro = new ReportTemplateDAO().checkParamName();

        HttpSession session = request.getSession(false);
        if (session != null) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                if (pbro.getFieldValueString(i, 0).equalsIgnoreCase(favName)) {
                    status = "Favourite Param Name Already Exists";
                    break;
                }
            }
            response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveFavParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String reportId = null;
        String favName = null;
        String favDesc = null;
        String foldersIds = null;
        String PbUserId = "";
        String paramswithTime = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();

        if (session != null) {
            try {
                favName = request.getParameter("favName");
                favDesc = request.getParameter("favDesc");
                foldersIds = request.getParameter("foldersIds");
                paramswithTime = request.getParameter("paramswithTime");
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                reportId = reportViewerDAO.getCustomReportId();
                String busRoles[] = foldersIds.split(",");
                String paramsTime[] = paramswithTime.split(",");

                reportTemplateDAO.insertFavParams(favName, favDesc, foldersIds, reportId, PbUserId, paramswithTime);

            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getFavParams(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String result = "";
        String userid = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            foldersIds = request.getParameter("foldersIds");
            userid = String.valueOf(session.getAttribute("USERID"));
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);


            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", foldersIds);
            result = reportTemplateDAO.getFavParams(foldersIds, userid);
            PrintWriter out = response.getWriter();
            out.print(result);
            container.setParametersHashMap(ParametersHashMap);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //addded by bharathi reddy
    public ActionForward saveParamSecurity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HashMap map = null;
        HashMap paramDefaultValuesHashMap = null;
        Container container = null;
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String elementId = request.getParameter("elementId");
        String paramValues = request.getParameter("paramValues");
        String REPORTID = request.getParameter("REPORTID");
        String memberId = request.getParameter("memberId");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        ReportTemplateBD repTempBD = new ReportTemplateBD();

        //reportTemplateDAO.saveParameterSecurity(elementId, paramValues, REPORTID, memberId);

        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(REPORTID) != null) {
                    container = (Container) map.get(REPORTID);
                } else {
                    container = new Container();
                }
            } else {
                container = new Container();
            }

            PbReportCollection collect = container.getReportCollect();

            Iterable<String> defaultValues = Splitter.on(",").split(paramValues);
            collect.updateParameterDefaults(elementId, defaultValues, false);

            container.setParameterSecurity(elementId, memberId, defaultValues);
//
//
//            String existparamvalues = "";
//            if (container.getParamDefaultValuesHashMap() != null) {
//
//                paramDefaultValuesHashMap = container.getParamDefaultValuesHashMap();
//                if (paramDefaultValuesHashMap.get(elementId) != null) {
//                    existparamvalues = String.valueOf(paramDefaultValuesHashMap.get(elementId));
//                }
//
//            }
//            if (!existparamvalues.equalsIgnoreCase("")) {
//                String finalParamDefaultValsList = "";
//                String paramValuesarr[] = paramValues.split(",");
//                String existparamvaluesarr[] = existparamvalues.split(",");
//                for (int j = 0; j < paramValuesarr.length; j++) {
//                    for (int k = 0; k < existparamvaluesarr.length; k++) {
//                        if (paramValuesarr[j].equalsIgnoreCase(existparamvaluesarr[k])) {
//                            finalParamDefaultValsList += "," + paramValuesarr[j];
//
//                        }
//
//                    }
//
//                }
//                if (!finalParamDefaultValsList.equalsIgnoreCase("")) {
//                }
//                {
//                    finalParamDefaultValsList = finalParamDefaultValsList.substring(1);
//
//                }
//                paramDefaultValuesHashMap.put(elementId, finalParamDefaultValsList);
////                container.setParamDefaultValuesHashMap(paramDefaultValuesHashMap);
//            }

            container.setSessionContext(session, container);



            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveParamDefaultValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HashMap map = null;
        Container container = null;
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String elementId = request.getParameter("elementId");
        String paramValues = request.getParameter("paramValues");
        String REPORTID = request.getParameter("REPORTID");

        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(REPORTID) != null) {
                    container = (Container) map.get(REPORTID);
                } else {
                    container = new Container();
                }
            } else {
                container = new Container();
            }


            PbReportCollection collect = container.getReportCollect();

            Iterable<String> defaultValues = Splitter.on(",").split(paramValues);
            collect.updateParameterDefaults(elementId, defaultValues, false);

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward checkavgTwoTables(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String dependenteleids = request.getParameter("dependenteleids");
//        String status = "";
        StringBuilder status = new StringBuilder(200);
        HttpSession session = request.getSession(false);

        String doubleExistQuery = " SELECT distinct BUSS_TABLE_ID,SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS  where element_id in(" + dependenteleids + ")";

        PbReturnObject doubleExistQuerypbro = new PbDb().execSelectSQL(doubleExistQuery);
        if (session != null) {

            if ((doubleExistQuerypbro.getRowCount() > 1)) {
//                status = "";
                status.append(" ");
            } else {
                String doubleExistQuery1 = " SELECT distinct element_id,AGGREGATION_TYPE FROM PRG_USER_ALL_INFO_DETAILS  where element_id in(" + dependenteleids + ")";

                PbReturnObject doubleExistQuerypbro1 = new PbDb().execSelectSQL(doubleExistQuery1);
                for (int i = 0; i < doubleExistQuerypbro1.getRowCount(); i++) {
//                     status += ";" + doubleExistQuerypbro1.getFieldValueInt(i, 0) + "-" + doubleExistQuerypbro1.getFieldValueString(i, 1).toUpperCase();
                    status.append(";").append(doubleExistQuerypbro1.getFieldValueInt(i, 0)).append("-").append(doubleExistQuerypbro1.getFieldValueString(i, 1).toUpperCase());
                }
                if (status.length() > 0) {
                    status = new StringBuilder(status.substring(1));
                }
//                if (!status.equalsIgnoreCase("")) {
//                    status = status.substring(1);
//                }
            }

            response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getElementDataType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String dependenteleids = request.getParameter("dependenteleids");
        String status = "";
        HttpSession session = request.getSession(false);

        String doubleExistQuery = " SELECT distinct BUSS_TABLE_ID,SUB_FOLDER_TAB_ID,USER_COL_TYPE,DEFAULT_AGGREGATION FROM PRG_USER_SUB_FOLDER_ELEMENTS  where element_id in(" + dependenteleids + ")";

        PbReturnObject doubleExistQuerypbro = new PbDb().execSelectSQL(doubleExistQuery);
        if (session != null) {
            if (doubleExistQuerypbro.getRowCount() > 0) {
                String userColType = doubleExistQuerypbro.getFieldValueString(0, 2);
                String AGGREGATION_TYPE = doubleExistQuerypbro.getFieldValueString(0, 3);
                status = userColType + "-" + AGGREGATION_TYPE;



            }
            response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward checkReportNameBeforeUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HashMap map = new HashMap();
//        Container container = null;
        String reportId = request.getParameter("REPORTID");
        String newRepName = request.getParameter("newRepName");
        //newRepName = newRepName.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
        String newRepDesc = request.getParameter("newRepDesc");
        //newRepDesc = newRepDesc.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');

        PbDb pbdb = new PbDb();
        String checkRepDuplicateNameQ = "select * from prg_ar_report_master where report_id not in('" + reportId + "') order by report_id desc";
        PbReturnObject allRepNameObj = pbdb.execSelectSQL(checkRepDuplicateNameQ);
        String status = "1";
        for (int m = 0; m < allRepNameObj.getRowCount(); m++) {
            if (newRepName.equalsIgnoreCase(allRepNameObj.getFieldValueString(m, "REPORT_NAME"))) {
                status = "Report Name Already Exists";
            }
            if (!status.equalsIgnoreCase("1")) {
                break;
            }
        }
        PrintWriter out = response.getWriter();
        out.println(status);
        return mapping.findForward(null);
    }

    public ActionForward changeReportName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String reportId = request.getParameter("reportID");
        String reportName = request.getParameter("reportName");
        String newRepDesc = request.getParameter("newRepDesc");
        HashMap map = new HashMap();
        Container container = null;
//        reportName = reportName.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
//        newRepDesc = newRepDesc.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');


        HttpSession session = request.getSession(false);
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(reportId);
        HashMap ReportHashMap = container.getReportHashMap();
        ReportHashMap.put("ReportName", reportName);

        container.setReportName(reportName);
        container.setReportDesc(newRepDesc);


        container.getReportCollect().reportName = reportName;
        container.getReportCollect().reportDesc = newRepDesc;


        return mapping.findForward(null);

    }

    public ActionForward saveSticky(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String parameterXml = null;
        if (session != null) {
            try {
                XMLOutputter serializer = null;
                Document document = null;
                HashMap map = new HashMap();
                Container container = null;
                HashMap paramMap = new HashMap();
                HashMap paramTime = new HashMap();
//                String parameters = "";
                DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
                ReportTemplateDAO repDAO = new ReportTemplateDAO();
                String PbReportId = request.getParameter("REPORTID");
                String completeURL = request.getParameter("completeURL");
                String timeLevelStr = "";

                HashMap ParamDefValsHashMap = new HashMap();

                if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                }
                if (map.get(PbReportId) != null) {
                    container = (prg.db.Container) map.get(PbReportId);

                } else {
                    container = new prg.db.Container();
                }
                String userFolders = repDAO.getReportUserFolders(PbReportId);
                String minTimeLevel = dbrdDAO.getUserFolderMinTimeLevel(userFolders);

                if (minTimeLevel.equalsIgnoreCase("5")) {
                    timeLevelStr = "Day";
                } else if (minTimeLevel.equalsIgnoreCase("4")) {
                    timeLevelStr = "Week";
                } else if (minTimeLevel.equalsIgnoreCase("3")) {
                    timeLevelStr = "Month";
                } else if (minTimeLevel.equalsIgnoreCase("2")) {
                    timeLevelStr = "Quarter";
                } else if (minTimeLevel.equalsIgnoreCase("1")) {
                    timeLevelStr = "Year";
                }
                request.setAttribute("timelevel", timeLevelStr);

                paramMap = container.getReportParameterHashMap();
                paramTime = container.getParametersHashMap();
                if (paramMap.size() == 0) {
                    paramMap = paramTime;
                }
                ArrayList timeArray = (ArrayList) paramMap.get("TimeDetailstList");
                HashMap timeDetsMap = (HashMap) paramMap.get("TimeDimHashMap");

                PrintWriter out = response.getWriter();

                Element root = new Element("StickyNote");
                root.setAttribute("version", "1.00001");
                root.setText("New Root");
                Element TimeDimensions = new Element("TimeDimensions");
                ArrayList dateArray = new ArrayList();
                ArrayList periodArray = new ArrayList();
                ArrayList compareArray = new ArrayList();

                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DATE");
                    periodArray = (ArrayList) timeDetsMap.get("PRG_PERIOD_TYPE");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");

                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DATE1");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_DATE2");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DATE");
                    periodArray = (ArrayList) timeDetsMap.get("PRG_DAY_ROLLING");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DMONTH1");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_DMONTH2");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DYEAR1");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_DYEAR2");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_RANGE")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DQUARTER1");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_DQUARTER2");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("WEEK") && timeArray.get(1).toString().equalsIgnoreCase("PRG_WEEK_CMP")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_WEEK");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_WEEK1");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("QUARTER") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_CMP")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_QUARTER");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_QUARTER1");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Month") && timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_MONTH");
                    periodArray = (ArrayList) timeDetsMap.get("PRG_PERIOD_TYPE");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("YEAR") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_YEAR");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_YEAR1");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }

                String date = dateArray.get(0).toString();
                String periodType = periodArray.get(0).toString();
//              String compareType = compareArray.get(0).toString

                Element asOfDateValue = new Element("AsOfDate");
                asOfDateValue.setText(date);
                TimeDimensions.addContent(asOfDateValue);

                Element periodTypeValue = new Element("PeriodType");
                periodTypeValue.setText(periodType);
                TimeDimensions.addContent(periodTypeValue);

//                Element compareWithValue = new Element("CompareWith");
//                compareWithValue.setText(compareType);
//                TimeDimensions.addContent(compareWithValue);


                Element ParaValues = new Element("ParamSection");
                String urldets[] = completeURL.split(";");
                for (int j = 0; j < urldets.length; j++) {
                    if (urldets[j].startsWith("CBOARP")) {
                        String defkey = urldets[j].split("=")[0];
                        defkey = defkey.substring(6);
                        String defvalue = urldets[j].split("=")[1];
                        Element elementId = new Element("ElementId");
                        elementId.setText(defkey);
                        ParaValues.addContent(elementId);
                        Element elementValue = new Element("ElementValue");
                        elementValue.setText(defvalue);
                        ParaValues.addContent(elementValue);
                    }
                    if (urldets[j].startsWith("CBOVIEW_BY")) {
                        String defkey = urldets[j].split("=")[0];
                        defkey = defkey.substring(10);
                        String defvalue = urldets[j].split("=")[1];
                        Element viewBy = new Element("ViewById");
                        viewBy.setText(defkey);
                        ParaValues.addContent(viewBy);
                        Element viewByValue = new Element("ViewByValue");
                        viewByValue.setText(defvalue);
                        ParaValues.addContent(viewByValue);
                    }
                }
                root.addContent(TimeDimensions);
                root.addContent(ParaValues);
                document = new Document(root);
                serializer = new XMLOutputter();

                parameterXml = serializer.outputString(document);

                out.print(parameterXml);


            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        return null;
    }

    public ActionForward deleteSticky(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            userid = request.getParameter("userId");
            String deleteStickId = request.getParameter("snote_id");

            repdao.DeleteSticky(userid, deleteStickId);
            //   return mapping.findForward("stickyList");
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward forshowStickText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String status = "";
        HashMap stickListHashMap = new HashMap();
        HashMap stickValueMap = new HashMap();
        if (session != null) {
//            //////.println("********forshowStickText***********");
            String stickListId = request.getParameter("stickListId");
//            //////.println("stickListId---::" + stickListId);
            String dispStick = String.valueOf(request.getParameter("disp"));
//            //////.println("dispStick---::" + dispStick);
            if (dispStick.equalsIgnoreCase("none")) {
                stickListHashMap = (HashMap) session.getAttribute("stickListHashMap");
                status = (String) stickListHashMap.get(stickListId);
                //    //////.println("Status------"+status);
                //    status = status.trim();
                stickListHashMap.put(stickListId, "block");
//                //////.println("---stickListHashMap---" + session.getAttribute("stickListHashMap"));
                session.setAttribute("stickListHashMap", stickListHashMap);
                status = "block";
                stickValueMap = (HashMap) session.getAttribute("stickValueMap");
                stickValueMap.remove(stickListId);
                session.setAttribute("stickValueMap", stickValueMap);
                response.getWriter().print(status);
            }
            // response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward forhideStickText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        int count = Integer.parseInt(request.getParameter("count"));
        HashMap stickListHashMap = new HashMap();
        if (session != null) {
//            //////.println("*********forhideStickText************");
            String stickListId = request.getParameter("stickListId");
            String dispStick = String.valueOf(request.getParameter("disp"));
            if (dispStick.equalsIgnoreCase("block")) {
                stickListHashMap.put(stickListId, "none");
                session.setAttribute("stickListHashMap", stickListHashMap);
            }

            // response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getitext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String dependenteleids = request.getParameter("elementId");
        String status = "";
        HttpSession session = request.getSession(false);

        String doubleExistQuery = "select * from  PRG_MEMBER_DETAILS where member_id in(select buss_col_id from prg_user_all_info_details "
                + "where element_id=" + dependenteleids + " ) and grp_id=(select grp_id from prg_user_all_info_details where element_id=" + dependenteleids + ")";

        PbReturnObject doubleExistQuerypbro = new PbDb().execSelectSQL(doubleExistQuery);
        if (session != null) {

            if ((doubleExistQuerypbro.getRowCount() > 1)) {
                status = doubleExistQuerypbro.getFieldValueClobString(0, "MEMBER_DESC");
            } else {
                status = "";
            }

            response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward excludeREP(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HashMap map = null;
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        Container container = null;
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String elementId = request.getParameter("elementId");
        String REPORTID = request.getParameter("REPORTID");
        String IncludeREP = request.getParameter("IncludeREP");
        ArrayList repExclude1 = new ArrayList();
        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(REPORTID) != null) {
                    container = (Container) map.get(REPORTID);
                } else {
                    container = new Container();
                }
            } else {
                container = new Container();
            }
            String elelist = "";
            ParametersHashMap = container.getParametersHashMap();
            TableHashMap = container.getTableHashMap();
            if (ParametersHashMap == null) {
                ParametersHashMap = new HashMap();
            } else {
                ArrayList repExclude = (ArrayList) ParametersHashMap.get("repExclude");
                if (IncludeREP.equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(IncludeREP) || IncludeREP == "Y") {
                    repExclude.remove(elementId);
                } else {
                    if (repExclude == null || !(repExclude.size() > 0)) {
                        repExclude = new ArrayList();
                        repExclude.add(elementId);
                    } else {
                        repExclude.add(elementId);
                    }
                }
                repExclude1 = repExclude;
            }

            ParametersHashMap.put("repExclude", repExclude1);

            container.setParametersHashMap(ParametersHashMap);


            if (TableHashMap != null) {
                if (TableHashMap.get("REP") != null) {

                    ArrayList rep = new ArrayList();
                    ArrayList repNames = new ArrayList();
                    ArrayList REP_Elements = (ArrayList) TableHashMap.get("REP");
                    ArrayList REP_ElementsNames = (ArrayList) TableHashMap.get("REPNames");
//                    ////.println("REP_Elements is : "+REP_Elements);
                    for (int j = 0; j < REP_Elements.size(); j++) {
                        if (repExclude1.contains(String.valueOf(REP_Elements.get(j)))) {
                            if (IncludeREP.equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(IncludeREP) || IncludeREP == "Y") {
//                                ////.println("REP_Elements includerep is y");
                                rep.add(REP_Elements.get(j));
                                repNames.add(REP_ElementsNames.get(j));
                            }
                        } else {
                            rep.add(REP_Elements.get(j));
                            repNames.add(REP_ElementsNames.get(j));
                        }

                    }
                    TableHashMap.put("REP", rep);
                    TableHashMap.put("REPNames", repNames);
                    container.setTableHashMap(TableHashMap);

                }
            }
//            if(IncludeREP.equalsIgnoreCase("N") || "N".equalsIgnoreCase(IncludeREP) || IncludeREP=="N"){
//            String repexlist = "";
            StringBuilder repexlist = new StringBuilder(100);
            for (int i = 0; i < repExclude1.size(); i++) {
//                repexlist += "," + repExclude1.get(i);
                repexlist.append(",").append(repExclude1.get(i));
            }
//            if (!repexlist.equalsIgnoreCase("")) {
//                repexlist = repexlist.substring(1);
            if (repexlist.length() > 0) {
                repexlist = new StringBuilder(repexlist.substring(1));

                String finalUserIds[] = repexlist.toString().split(",");
//                String dependenteleids1 = "";
                StringBuilder dependenteleids1 = new StringBuilder();
                for (int j = 0; j < finalUserIds.length - 1; j++) {
                    int count = 0;
                    for (int j1 = j + 1; j1 < finalUserIds.length; j1++) {
                        //
                        if (finalUserIds[j].equalsIgnoreCase(finalUserIds[j1])) {

                            count = 1;
                            break;
                        }
                    }
                    if (count == 0) {
//                          dependenteleids1 += "," + finalUserIds[j];
                        dependenteleids1.append(",").append(finalUserIds[j]);
                    }
                    if (j == finalUserIds.length - 2) {
//                        dependenteleids1 += "," + finalUserIds[j + 1];
                        dependenteleids1.append(",").append(finalUserIds[j + 1]);
                    }

                }
                if (dependenteleids1.length() > 0) {
//                    dependenteleids1 = dependenteleids1.substring(1);
//                    repexlist =new StringBuilder( dependenteleids1);
                    repexlist = new StringBuilder(dependenteleids1.substring(1));
                }
//                if (!dependenteleids1.equalsIgnoreCase("")) {
//                    dependenteleids1 = dependenteleids1.substring(1);
//                    repexlist =new StringBuilder( dependenteleids1);
//                }
            }
            response.getWriter().print(repexlist);
//            }
            if (IncludeREP.equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(IncludeREP) || IncludeREP == "Y") {
                response.getWriter().print(repExclude1 + "~" + repexlist);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward excludeCEP(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HashMap map = null;
        HashMap ParametersHashMap = null;
        HashMap TableHashMap = null;
        Container container = null;
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String elementId = request.getParameter("elementId");
        String REPORTID = request.getParameter("REPORTID");
        String IncludeCEP = request.getParameter("IncludeCEP");
        ArrayList cepExclude1 = new ArrayList();
        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(REPORTID) != null) {
                    container = (Container) map.get(REPORTID);
                } else {
                    container = new Container();
                }
            } else {
                container = new Container();
            }
//            String elelist = "";
            ParametersHashMap = container.getParametersHashMap();
            TableHashMap = container.getTableHashMap();

            if (ParametersHashMap == null) {
                ParametersHashMap = new HashMap();
            } else {
                ArrayList cepExclude = (ArrayList) ParametersHashMap.get("cepExclude");

                if (IncludeCEP.equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(IncludeCEP) || IncludeCEP == "Y") {
//                    ////.println("in includecep is y");
                    cepExclude.remove(elementId);
                } else {
                    if (cepExclude == null || !(cepExclude.size() > 0)) {
                        cepExclude = new ArrayList();
                        cepExclude.add(elementId);
                    } else {
                        cepExclude.add(elementId);
                    }
                }
                cepExclude1 = cepExclude;
            }

            ParametersHashMap.put("cepExclude", cepExclude1);

            container.setParametersHashMap(ParametersHashMap);




            if (TableHashMap != null) {
                if (TableHashMap.get("CEP") != null) {

                    ArrayList rep = new ArrayList();
                    ArrayList repNames = new ArrayList();
                    ArrayList REP_Elements = (ArrayList) TableHashMap.get("CEP");
                    ArrayList REP_ElementsNames = (ArrayList) TableHashMap.get("CEPNames");
                    for (int j = 0; j < REP_Elements.size(); j++) {
                        if (cepExclude1.contains(String.valueOf(REP_Elements.get(j)))) {
                            if (IncludeCEP.equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(IncludeCEP) || IncludeCEP == "Y") {
//                                ////.println("REP_Elements includecep is y");
                                rep.add(REP_Elements.get(j));
                                repNames.add(REP_ElementsNames.get(j));
                            }
                        } else {
                            rep.add(REP_Elements.get(j));
                            repNames.add(REP_ElementsNames.get(j));
                        }

                    }

                    TableHashMap.put("CEP", rep);
                    TableHashMap.put("CEPNames", repNames);
                    container.setTableHashMap(TableHashMap);

                }
            }

            StringBuilder repexlist = new StringBuilder(100);
            for (int i = 0; i < cepExclude1.size(); i++) {
//                repexlist += "," + repExclude1.get(i);
                repexlist.append(",").append(cepExclude1.get(i));
            }
//            if (!repexlist.equalsIgnoreCase("")) {
//                repexlist = repexlist.substring(1);
            if (repexlist.length() > 0) {
                repexlist = new StringBuilder(repexlist.substring(1));

//            if (!repexlist.equalsIgnoreCase("")) {
//                repexlist = repexlist.substring(1);


                String finalUserIds[] = repexlist.toString().split(",");
//                String dependenteleids1 = "";
                StringBuilder dependenteleids1 = new StringBuilder(400);
                for (int j = 0; j < finalUserIds.length - 1; j++) {
                    int count = 0;
                    for (int j1 = j + 1; j1 < finalUserIds.length; j1++) {
                        //
                        if (finalUserIds[j].equalsIgnoreCase(finalUserIds[j1])) {
                            count = 1;
                            break;
                        }
                    }
                    if (count == 0) {
                        dependenteleids1.append(",").append(finalUserIds[j]);
//                        dependenteleids1 += "," + finalUserIds[j];
                    }
                    if (j == finalUserIds.length - 2) {
                        dependenteleids1.append(",").append(finalUserIds[j + 1]);
//                        dependenteleids1 += "," + finalUserIds[j + 1];
                    }

                }

//                if (!dependenteleids1.equalsIgnoreCase("")) {
//                    dependenteleids1 = dependenteleids1.substring(1);
//                    repexlist = new StringBuilder(dependenteleids1);
//                }
                if (dependenteleids1.length() > 0) {
//                    dependenteleids1 = dependenteleids1.substring(1);
//                    repexlist = new StringBuilder(dependenteleids1);
                    repexlist = new StringBuilder(dependenteleids1.substring(1));
                }
            }

            response.getWriter().print(repexlist);
            if (IncludeCEP.equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(IncludeCEP) || IncludeCEP == "Y") {
                response.getWriter().print(cepExclude1 + "~" + repexlist);
            }
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward addReportForCompany(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String status = "";
        String userId = request.getParameter("userId");
        String companies = request.getParameter("companies");
        String reportId = request.getParameter("reportId");
        String reportType = request.getParameter("reportType");
        String oldReportId = request.getParameter("oldReportId");
        request.setAttribute("REPORTID", reportId);
        HttpSession session = request.getSession(false);
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        if (session != null) {

            HashMap PROGENTABLES = (HashMap) session.getAttribute("PROGENTABLES");
            PROGENTABLES.remove(reportId);
            if (!oldReportId.equalsIgnoreCase("") && !oldReportId.equalsIgnoreCase(null) && oldReportId != null) {
                PROGENTABLES.remove(oldReportId);
            }
            reportTemplateDAO.addReportForCompany(reportId, companies, userId);
            if (reportType != null && reportType.equalsIgnoreCase("R")) {
                return mapping.findForward("reportView");
            } else {
                return mapping.findForward("dashboardView");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward shwparamAssis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String viewBY = request.getParameter("viewBY");
            StringBuffer viewBYValuesDisp = new StringBuffer();
            HashMap reportParamDrillAssisMap = (HashMap) session.getAttribute("reportParamDrillAssisMap");
            reportParamDrillAssisMap.get(viewBY);
            String viewbyValues[] = reportParamDrillAssisMap.get(viewBY).toString().split(",");
            viewBYValuesDisp.append("<table>");
            for (int j = 0; j < viewbyValues.length; j++) {
                viewBYValuesDisp.append("<tr>");
                viewBYValuesDisp.append("<td style='color:#369;font-weight:bold;'>" + viewbyValues[j]);
                viewBYValuesDisp.append("</td>");
                viewBYValuesDisp.append("</tr>");
            }
            viewBYValuesDisp.append("</table>");
            response.getWriter().print(viewBYValuesDisp.toString());
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward checkReportNameatRoleLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String reportId = request.getParameter("REPORTID");
        String repName = request.getParameter("repName");
        repName = repName.replace("'", "''");
        // repName = repName.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
        String folderIds = request.getParameter("folderIds");
        HashMap map = null;
        Container container = null;
        HashMap ParametersMap;
        String paramIds = null;
        String[] parameterIds = null;
        ArrayList Parameters = new ArrayList();
        String fromDesigner = request.getParameter("fromRepDesigner");

        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(reportId);

            ParametersMap = container.getParametersHashMap();
            if (ParametersMap == null) {
                ParametersMap = new HashMap();
            }
            paramIds = request.getParameter("params");
            //update Param hashmap in session
            if (paramIds != null) {
                parameterIds = paramIds.split(",");
                for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                    Parameters.add(parameterIds[paramCount]);//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
                }
            }
            if (fromDesigner != null && fromDesigner.equalsIgnoreCase("fromRepDesigner") && (Parameters.size() == 0)) {
                Parameters = (ArrayList) container.getParametersHashMap().get("Parameters");
            }
            ParametersMap.put("Parameters", Parameters);
            container.setParametersHashMap(ParametersMap);
        }
//        //////.println("repName in action is : "+repName);
//        //////.println("folderIds in action is : "+request.getParameter("folderIds"));
        PbDb pbdb = new PbDb();
        String checkRoleRepNameQry = "";
        if (container.getAOId() != null && !container.getAOId().isEmpty()) {
            checkRoleRepNameQry = "select * from prg_ar_report_master where report_id in(select report_id from prg_ar_report_details where AO_ID in(" + container.getAOId() + ")) order by report_id desc";
        } else {
            checkRoleRepNameQry = "select * from prg_ar_report_master where report_id in(select report_id from prg_ar_report_details where folder_id in(" + folderIds + ")) order by report_id desc";
        }
//        //////.println("checkRoleRepNameQry is : "+checkRoleRepNameQry);
        PbReturnObject allRoleRepNameObj = pbdb.execSelectSQL(checkRoleRepNameQry);
        String status = "1";
        for (int m = 0; m < allRoleRepNameObj.getRowCount(); m++) {
            if (repName.equalsIgnoreCase(allRoleRepNameObj.getFieldValueString(m, 1))) {
                status = "Report Name Already Exists";
            }
            if (!status.equalsIgnoreCase("1")) {
                break;
            }
        }
        PrintWriter out = response.getWriter();
        out.println(status);
        session.setAttribute("isGraphThere", "noGraphThere");
        return mapping.findForward(null);
    }



    public ActionForward checkReportNameatAOLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
  HttpSession session = request.getSession(false);

        String repName = request.getParameter("repName");
        repName = repName.replace("'", "''");

        PbDb pbdb = new PbDb();
        String checkRoleRepNameQry = "";
        if (session != null) {
            checkRoleRepNameQry = "select * from prg_ar_report_master ";
        } else {
            checkRoleRepNameQry = "select * from prg_ar_report_master ";
        }
//        //////.println("checkRoleRepNameQry is : "+checkRoleRepNameQry);
        PbReturnObject allRoleRepNameObj = pbdb.execSelectSQL(checkRoleRepNameQry);
        String status = "1";
        for (int m = 0; m < allRoleRepNameObj.getRowCount(); m++) {
            if (repName.equalsIgnoreCase(allRoleRepNameObj.getFieldValueString(m, 1))) {
                status = "Report Name Already Exists";
            }
            if (!status.equalsIgnoreCase("1")) {
                break;
            }
        }
        PrintWriter out = response.getWriter();
        out.println(status);
        session.setAttribute("isGraphThere", "noGraphThere");
        return mapping.findForward(null);
    }

  public ActionForward checkReportNameatAOCreateLevel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
  HttpSession session = request.getSession(false);

        String repName = request.getParameter("repName");
        repName = repName.replace("'", "''");

        PbDb pbdb = new PbDb();
        String checkRoleRepNameQry = "";
        if (session != null) {
            checkRoleRepNameQry = "select * from  prg_ar_ao_master where ao_name='"+repName +"'";
        } else {
            checkRoleRepNameQry = "select * from  prg_ar_ao_master where ao_name='"+repName +"'";
        }
//        //////.println("checkRoleRepNameQry is : "+checkRoleRepNameQry);
        PbReturnObject allRoleRepNameObj = pbdb.execSelectSQL(checkRoleRepNameQry);
        String status = "1";
//        for (int m = 0; m < allRoleRepNameObj.getRowCount(); m++) {
            if (allRoleRepNameObj !=null && allRoleRepNameObj.getRowCount() > 0 ) {
                status = "AO Name Already Exists";
            }
//            if (!status.equalsIgnoreCase("1")) {
//                break;
//            }
//        }
        PrintWriter out = response.getWriter();
        out.println(status);
        session.setAttribute("isGraphThere", "noGraphThere");
        return mapping.findForward(null);
    }

    public ActionForward setCustomDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        String customdate = request.getParameter("date");
        ////////.println("customdate----"+customdate);
        HttpSession session = request.getSession(false);

        if (session != null) {
            Container container = null;
            String customReportId = request.getParameter("reportid");
            String dateType = request.getParameter("dateType");
            String dateformat = request.getParameter("dateFormat");

            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");

            container = (Container) map.get(customReportId);
            String ddformT = null;
            if (session.getAttribute("dateFormat") != null) {
                ddformT = session.getAttribute("dateFormat").toString();
            }

            String value = "";
            String valu = "";
            String mont = "";
            String CurrValue = "";
            if (ddformT == null || ddformT.equalsIgnoreCase("dd/mm/yy")) {
                value = customdate;
                int slashval = value.indexOf("/");
                int slashLast = value.lastIndexOf("/");
                valu = value.substring(0, slashval);
                mont = value.substring(slashval + 1, slashLast + 1);
                CurrValue = mont.concat(valu).concat(value.substring(slashLast));
                customdate = CurrValue;
            }

            PbReportCollection collect = container.getReportCollect();
            if (dateType.equalsIgnoreCase("AS_OF_DATE2")) {
                String fromDatestr = collect.getTimeDefault("AS_OF_DATE1");
                DateFormat dateFormat = new SimpleDateFormat(dateformat);
                Date fromDate = dateFormat.parse(fromDatestr);
                Date toDate = dateFormat.parse(customdate);
                Calendar fromCalendar = Calendar.getInstance();
                fromCalendar.setTime(fromDate);
                Calendar toCalendar = Calendar.getInstance();
                toCalendar.setTime(toDate);
                int diffOfCal = toCalendar.compareTo(fromCalendar);
                if (diffOfCal > 0) {
                    collect.updateTimeDefault(dateType, customdate);
                    out.println("1");
                } else {
                    out.println("2");
                }


            } else {
                collect.updateTimeDefault(dateType, customdate);
                out.println("1");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
        return null;
    }

    public ActionForward setAggregation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        Container container = null;
        String customReportId = request.getParameter("reportid");
        //////.println("customReportIdinsetAggregation--->" + customReportId);
        HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
        //////.println("map--" + map);
        container = (Container) map.get(customReportId);
        //////.println("container--" + container);
        HashMap mapdate = container.getParametersHashMap();
        //////.println("mapdate----->" + mapdate);
        //        HashMap timeDimHM= (HashMap)mapdate.get("TimeDimHashMap");
        //         //////.println("timeDimHM----->in setaggregation"+timeDimHM);
        ArrayList timeDimArr = (ArrayList) mapdate.get("TimeDetailstList");
        //////.println("timeDimArr---->in set aggre" + timeDimArr);
        //////.println("name for 0 array---->" + timeDimArr.get(0));
        String dvalue = (String) timeDimArr.get(0);
        //timeDimArr.set(5, customdate);
        ////////.println("timeDSim--"+timeDimArr);
        //timeDimHM.remove("AS_OF_DATE");
        //timeDimHM.put("AS_OF_DATE",timeDimArr);
        //mapdate.remove("TimeDimHashMap");
        //mapdate.put("TimeDimHashMap", timeDimHM);
        //container.setParametersHashMap(mapdate);
        ////////.println("mapdate--->"+mapdate);
        out.println(dvalue);
        return null;
    }

    public ActionForward delParamSecurity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HashMap map = null;
        HashMap paramDefaultValuesHashMap = null;
        Container container = null;
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String elementId = request.getParameter("elementId");
        String REPORTID = request.getParameter("REPORTID");
        String memberId = request.getParameter("memberId");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//        reportTemplateDAO.delParameterSecurity(elementId, REPORTID);

        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(REPORTID) != null) {
                    container = (Container) map.get(REPORTID);
                } else {
                    container = new Container();
                }
            } else {
                container = new Container();
            }

            PbReportCollection collect = container.getReportCollect();
            collect.resetParameterDefaults(elementId);

            container.removeParameterSecurity(elementId);

//            String existparamvalues = "";
//            if (container.getParamDefaultValuesHashMap() != null) {
//
//                paramDefaultValuesHashMap = container.getParamDefaultValuesHashMap();
//                if (paramDefaultValuesHashMap.get(elementId) != null) {
//                    existparamvalues = String.valueOf(paramDefaultValuesHashMap.get(elementId));
//                    paramDefaultValuesHashMap.put(elementId, "");
//                    container.setParamDefaultValuesHashMap(paramDefaultValuesHashMap);
//                }
//
//            }
//            if (!existparamvalues.equalsIgnoreCase("")) {
//                String finalParamDefaultValsList = "";
//                String paramValuesarr[] = paramValues.split(",");
//                String existparamvaluesarr[] = existparamvalues.split(",");
//                for (int j = 0; j < paramValuesarr.length; j++) {
//                    for (int k = 0; k < existparamvaluesarr.length; k++) {
//                        if (paramValuesarr[j].equalsIgnoreCase(existparamvaluesarr[k])) {
//                            finalParamDefaultValsList += "," + paramValuesarr[j];
//
//                        }
//
//                    }
//
//                }
//                if (!finalParamDefaultValsList.equalsIgnoreCase("")) {
//                }
//                {
//                    finalParamDefaultValsList = finalParamDefaultValsList.substring(1);
//
//                }
//                paramDefaultValuesHashMap.put(elementId, finalParamDefaultValsList);
//                container.setParamDefaultValuesHashMap(paramDefaultValuesHashMap);
//            }


            container.setSessionContext(session, container);



            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward updateParamSecurity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HashMap map = null;
        HashMap paramDefaultValuesHashMap = null;
        Container container = null;
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String elementId = request.getParameter("elementId");
        String paramValues = request.getParameter("paramValues");
        String REPORTID = request.getParameter("REPORTID");
        String memberId = request.getParameter("memberId");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        reportTemplateDAO.updateParameterSecurity(elementId, paramValues, REPORTID);

        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(REPORTID) != null) {
                    container = (Container) map.get(REPORTID);
                } else {
                    container = new Container();
                }
            } else {
                container = new Container();
            }

            PbReportCollection collect = container.getReportCollect();

            Iterable<String> defaultValues = Splitter.on(",").split(paramValues);
            collect.updateParameterDefaults(elementId, defaultValues, false);

            container.setParameterSecurity(elementId, memberId, defaultValues);

//            String existparamvalues = "";
////            if (container.getParamDefaultValuesHashMap() != null) {
////
////                paramDefaultValuesHashMap = container.getParamDefaultValuesHashMap();
////                if (paramDefaultValuesHashMap.get(elementId) != null) {
////                    existparamvalues = String.valueOf(paramDefaultValuesHashMap.get(elementId));
////                }
////
////            }
//            if (!existparamvalues.equalsIgnoreCase("")) {
//                String finalParamDefaultValsList = "";
//                String paramValuesarr[] = paramValues.split(",");
//                String existparamvaluesarr[] = existparamvalues.split(",");
//                for (int j = 0; j < paramValuesarr.length; j++) {
//                    for (int k = 0; k < existparamvaluesarr.length; k++) {
//                        if (paramValuesarr[j].equalsIgnoreCase(existparamvaluesarr[k])) {
//                            finalParamDefaultValsList += "," + paramValuesarr[j];
//
//                        }
//
//                    }
//
//                }
//                if (!finalParamDefaultValsList.equalsIgnoreCase("")) {
//                }
//                {
//                    finalParamDefaultValsList = finalParamDefaultValsList.substring(1);
//
//                }
//                paramDefaultValuesHashMap.put(elementId, finalParamDefaultValsList);
////                container.setParamDefaultValuesHashMap(paramDefaultValuesHashMap);
//            }


            container.setSessionContext(session, container);



            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward loadUIComponents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        ArrayList<String> components = new ArrayList<String>();
        ArrayList<String> translatedComponent = new ArrayList<String>();
        PrgUIContainer uiContainer = new PrgUIContainer("DROP_DOWN");
        String userId = String.valueOf(session.getAttribute("USERID"));
        Locale locale = (Locale) session.getAttribute("userLocale");
        components.add("CREATE_REPORT");
        components.add("CREATE_REPORT_AO");
        components.add("EDIT_REPORT_NAME");
        components.add("DELETE_REPORT");
        components.add("CLEAR_CACHE");
        components.add("DOWNLOAD");
        components.add("SAVE_AS_HTML");
        components.add("COPY_REPORT");
        components.add("SHOW_METADATA");
//        components.add("COMPARE_REPORTS");
        components.add("SAVE_AS_ADVANCED_HTML");

//        if( PrivilegeManager.isModuleComponentEnabledForUser("REPDESIGNER", "PURGEREPORT", Integer.parseInt(userId)))
        components.add("PURGE_REPORT");
        components.add("RETRIEVE_FROM_BKP");
        components.add("INVALIDATE_QUERY_CACHE");
        components.add("REBUILD_QUERY_CACHE");    //added by Mohit
        //components.add("ADHOC_REPORT");           // Commented by mayank..

        for (String Componentvalues : components) {
            translatedComponent.add(TranslaterHelper.getTranslatedString(Componentvalues, locale));
        }

        for (int i = 0; i < components.size(); i++) {
            uiContainer.addComponent(components.get(i), translatedComponent.get(i));
        }

        String uiComponentJson = uiContainer.generateJSON();
        out.println(uiComponentJson);
        return null;
    }

    public ActionForward getRoleIdByReportID(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        String reportID = request.getParameter("reportId");
        String roleID = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        roleID = reportTemplateDAO.getRoleIdByReportId(reportID);
        out.println(roleID);
        return null;

    }

    public ActionForward showMetaData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("reportId");
        //ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        ReportTemplateBD reportTemplateBD = new ReportTemplateBD();
        String buildTable = reportTemplateBD.showMetaData(reportId);
        try {
            PrintWriter out = response.getWriter();
            out.print(buildTable);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward copyReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String reportName = request.getParameter("reportName");
        String reportDesc = request.getParameter("reportDesc");
        String oldReportId = request.getParameter("reportId");
        boolean status;

        //reportName = reportName.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
        //reportDesc = reportDesc.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');

        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO tempDAO = new ReportTemplateDAO();
            status = tempDAO.copyReport(reportName, reportDesc, oldReportId);
            String copyMessage = status ? "REPORT_COPY_SUCCESS" : "REPORT_COPY_FAILED";
            Locale locale = (Locale) session.getAttribute("userLocale");
            String copyMsgTxt = TranslaterHelper.getTranslatedString(copyMessage, locale);
            PrgMessage message = new PrgMessage("REPORT_COPY_STATUS", copyMessage);
            message.setMesgTxt(copyMsgTxt);
            response.getWriter().print(message.getMessageJSON());
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

//    excludeParamViewbys
    public ActionForward getRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        ReportTemplateDAO dao = new ReportTemplateDAO();
        HashMap<String, ArrayList<String>> roleMap = dao.getRoleIds(userId);
        Gson gson = new Gson();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(gson.toJson(roleMap));
        return null;
    }

    public ActionForward getReportBasedOnRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String roleId = request.getParameter("roleId");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        List<ReportDetails> reportDetails = dao.getReportDetails(userId, roleId);
        Gson gson = new Gson();
        String reportJson = gson.toJson(reportDetails);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(reportJson);
        return null;
    }

    public ActionForward getReportsForDrillAcross(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String reportId = request.getParameter("reportId");
        Container container = Container.getContainerFromSession(request, reportId);
        String[] bizRoles = container.getReportCollect().reportBizRoles;
        ArrayList<String> rowViewBys = container.getReportCollect().reportRowViewbyValues;
        ReportTemplateDAO dao = new ReportTemplateDAO();
        List<ReportDetails> reportDetails = dao.getReportsForDrillAcross(userId, bizRoles[0], rowViewBys);
        Gson gson = new Gson();
        String reportJson = gson.toJson(reportDetails);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(reportJson);
        return null;
    }

    public ActionForward drillAcross(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String reportId = request.getParameter("reportId");
        String targetRptId = request.getParameter("targetReportId");
        String drillAcrossParams = request.getParameter("params");

        String[] paramArr = drillAcrossParams.split(",");
        ArrayListMultimap<String, String> map = ArrayListMultimap.create();

        for (int i = 0; i < paramArr.length; i++) {
            String param = paramArr[i];
            String[] keyVal = param.split(":");
            String key = keyVal[0].replace("A_", "CBOARP");
            map.put(key, keyVal[1]);
        }
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();

        while (iter.hasNext()) {
            String key = iter.next();
            List<String> valList = map.get(key);
            sb.append("&" + key + "=");
            StringBuilder valSB = new StringBuilder();
            for (String val : valList) {
                valSB.append("," + val);
            }
            sb.append(valSB.substring(1));
        }

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(sb.toString());
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward drill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String reportId = request.getParameter("reportId");
        String drillAcrossParams = request.getParameter("params");
        String elementId = request.getParameter("elementId");

        if (elementId != null && elementId.startsWith("A_")) {
            elementId = elementId.substring(2);
        }

        String[] paramArr = drillAcrossParams.split(",");
        ArrayListMultimap<String, String> map = ArrayListMultimap.create();

        for (int i = 0; i < paramArr.length; i++) {
            String param = paramArr[i];
            String[] keyVal = param.split(":");
            String key = keyVal[0].replace("A_", "CBOARP");
            map.put(key, keyVal[1]);
        }
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();

        while (iter.hasNext()) {
            String key = iter.next();
            List<String> valList = map.get(key);
            sb.append("&" + key + "=");
            StringBuilder valSB = new StringBuilder();
            for (String val : valList) {
                valSB.append("," + val);
            }
            sb.append(valSB.substring(1));
        }

        Container container = Container.getContainerFromSession(request, reportId);
        PbReportCollection collect = container.getReportCollect();

        String viewby = "";
        LinkedHashMap<String, ArrayList<String>> viewbys = collect.reportViewByOrder;
        keySet = viewbys.keySet();
        iter = keySet.iterator();

        if (iter.hasNext()) {
            viewby = iter.next();
        }

        if (!"".equals(viewby)) {
            HashMap<String, String> drillMap = collect.getDrillMap();
            if (drillMap != null && drillMap.containsKey(elementId)) {
                String val = drillMap.get(elementId);
                sb.append("&CBOVIEW_BY").append(viewby).append("=").append(val);
            }
        }

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(sb.toString());
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward getFavReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        ReportTemplateDAO dao = new ReportTemplateDAO();
        List<ReportDetails> favReportDetails = dao.getFavouriteRept(userId);
        Gson gson = new Gson();
        String reportJson = gson.toJson(favReportDetails);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(reportJson);
        return null;
    }

    public ActionForward prepareReportsToCompare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        boolean flag = false;
        Container firstContainer = null;
        Container secondContainer = null;
        String firstRepId = request.getParameter("firstRepId");
        String secondRepId = request.getParameter("secondRepId");

        ReportTemplateDAO reportTemplateDao = new ReportTemplateDAO();
        ArrayList firstReportViewBy = reportTemplateDao.getReportViewBy(firstRepId);
        ArrayList secondReportViewBy = reportTemplateDao.getReportViewBy(secondRepId);

//        PbReportViewerBD repBD = new PbReportViewerBD();
//        String userid = "";
//        if (session.getAttribute("USERID") != null) {
//            userid = session.getAttribute("USERID").toString();
//        }
//        repBD.prepareReport(firstRepId, userid, request, response);
//        repBD.prepareReport(secondRepId, userid, request, response);


//        HashMap map = new HashMap();
//        map = (HashMap) session.getAttribute("PROGENTABLES");
//        firstContainer = (Container) map.get(firstRepId);
//        secondContainer = (Container) map.get(secondRepId);

//        ArrayList<String> firstRowViewBy=firstContainer.getReportCollect().reportRowViewbyValues;
//        ArrayList<String> secondRowViewBy=secondContainer.getReportCollect().reportRowViewbyValues;
//        ArrayList<String> firstColViewBy=firstContainer.getReportCollect().reportColViewbyValues;
//        ArrayList<String> secondColViewBy=secondContainer.getReportCollect().reportColViewbyValues;

        if (firstReportViewBy.size() == secondReportViewBy.size()) {
            for (int i = 0; i < firstReportViewBy.size(); i++) {
                if (((String) firstReportViewBy.get(i)).equals((String) secondReportViewBy.get(i))) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
        } else {
            flag = false;
        }

        PrintWriter out = response.getWriter();
        if (flag == true) {
            out.println("success");
        }

        return null;
    }

//    public ActionForward getReportsToCompare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session=request.getSession();
//        String firstRepId = request.getParameter("firstRepId");
//        String secondRepId = request.getParameter("secondRepId");
////        Container firstContainer=Container.getContainerFromSession(request, firstRepId);
////        Container secondContainer=Container.getContainerFromSession(request, secondRepId);
//        Container firstContainer = null;
//        Container secondContainer = null;
//
//        PbReportViewerBD repBD = new PbReportViewerBD();
//        String userid = "";
//        if (session.getAttribute("USERID") != null) {
//            userid = session.getAttribute("USERID").toString();
//        }
//        repBD.prepareReport(firstRepId, userid, request, response);
//        repBD.prepareReport(secondRepId, userid, request, response);
//        HashMap map = new HashMap();
//        map = (HashMap) session.getAttribute("PROGENTABLES");
//        firstContainer = (Container) map.get(firstRepId);
//        secondContainer = (Container) map.get(secondRepId);
//
//        ArrayList<String> firstRowViewBy=firstContainer.getReportCollect().reportRowViewbyValues;
//        ArrayList<String> secondRowViewBy=secondContainer.getReportCollect().reportRowViewbyValues;
//        ArrayList<String> firstColViewBy=firstContainer.getReportCollect().reportColViewbyValues;
//        ArrayList<String> secondColViewBy=secondContainer.getReportCollect().reportColViewbyValues;
//        ArrayList timeDetailsArray1=firstContainer.getReportCollect().timeDetailsArray;
//        ArrayList timeDetailsArray2=secondContainer.getReportCollect().timeDetailsArray;
//
//        String reportTime="<table><tr><td class='myhead'>"+firstContainer.getReportName()+":</td><td>"+timeDetailsArray1.get(2)+"</td></tr>";
//        reportTime+="<tr><td class='myhead' >"+secondContainer.getReportName()+":</td><td>"+timeDetailsArray2.get(2)+"</td></tr></table>";
//        String paramHtml="";
//        ArrayList<String> firstParamNames = new ArrayList<String>();
//        ArrayList<String> secParamNames = new ArrayList<String>();
//        ArrayList<String> firstParamIds = new ArrayList<String>();
//        ArrayList<String> secParamIds = new ArrayList<String>();
//        ArrayList<String> paramIds = new ArrayList<String>();
//        ArrayList<String> paramNames = new ArrayList<String>();
//        firstParamNames = (ArrayList) firstContainer.getParametersHashMap().get("ParametersNames");
//        firstParamIds = (ArrayList) firstContainer.getParametersHashMap().get("Parameters");
//        secParamNames = (ArrayList) secondContainer.getParametersHashMap().get("ParametersNames");
//        secParamIds = (ArrayList) secondContainer.getParametersHashMap().get("Parameters");
//        for(int i=0;i<firstParamIds.size();i++){
//            for(int j=0;j<secParamIds.size();j++){
//                if(firstParamIds.get(i).equals(secParamIds.get(j))){
//                    paramIds.add(firstParamIds.get(i));
//                    paramNames.add(firstParamNames.get(i));
//                }
//            }
//        }
//        for(int k=0;k<paramIds.size();k++){
//            paramHtml+="<option value=\""+paramIds.get(k)+"\">"+paramNames.get(k)+"</option>";
//        }
//        ReportTemplateDAO repDao=new ReportTemplateDAO();
//        String userId = session.getAttribute("USERID").toString();
//        String html=repDao.getNewPbReturnObject(firstContainer,secondContainer,firstRepId,secondRepId,userId);
//        request.setAttribute("html", html);
//        request.setAttribute("frstRepId", firstRepId);
//        request.setAttribute("secndRepId", secondRepId);
//        request.setAttribute("paramHtml", paramHtml);
//        request.setAttribute("firstColViewBy", firstColViewBy);
//        request.setAttribute("secondColViewBy", secondColViewBy);
//        request.setAttribute("reportTime", reportTime);
//        return mapping.findForward("compare");
//    }
    public ActionForward getReportsToCompare1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession();
        String firstRepId = request.getParameter("firstRepId");
        String reportIdList = request.getParameter("reports");
        String[] reportIds = reportIdList.split(",");
        DataSnapshotBD snapshotBD = new DataSnapshotBD();
//        PrintWriter out=null;
        List<Container> containerList = new ArrayList<Container>();

        HashMap map = new HashMap();
//         Container firstContainer=null;
        PbReportViewerBD repBD = new PbReportViewerBD();

        String[] repIdList = new String[reportIds.length + 1];
        repIdList[0] = firstRepId;
        for (int i = 1; i < repIdList.length; i++) {

            repIdList[i] = reportIds[i - 1];
        }

        String userid = "";
        if (session.getAttribute("USERID") != null) {
            userid = session.getAttribute("USERID").toString();
        }
//        ReportComparisionHelper reportComparision=null;
//        int reportId=0;
//          repBD.prepareReport(firstRepId, userid, request, response);

        Container firstContainer = Container.getContainerFromSession(request, repIdList[0]);

//           Container firstContainer=snapshotBD.generateContainer(firstRepId, userid, null, null)
        ArrayList<String> rowViewBys = new ArrayList<String>();
        rowViewBys.add(firstContainer.getReportCollect().reportRowViewbyValues.get(0));
        Container container1 = snapshotBD.generateContainer(firstRepId, userid, rowViewBys, null);
        containerList.add(container1);

        for (int j = 0; j < reportIds.length; j++) {
            Container container = snapshotBD.generateContainer(reportIds[j], userid, rowViewBys, null);
            if (container != null) {
                containerList.add(container);
            }
            // repBD.prepareReport(repIdList[j], userid, request,response);
        }

        map = (HashMap) session.getAttribute("PROGENTABLES");
//        firstContainer=(Container) map.get(firstRepId);
//        containerList.add(firstContainer);
//        for(String repId:repIdList)
//        {
//            Container container=(Container) map.get(repId);
//            containerList.add(container);;
//        }
        StringBuilder reportTime = new StringBuilder();
        reportTime.append("<table>");
        ArrayList timeDetailsArray = null;
        for (int l = 0; l < containerList.size(); l++) {
            timeDetailsArray = new ArrayList();
            timeDetailsArray = containerList.get(l).getReportCollect().timeDetailsArray;
            reportTime = reportTime.append("<tr><td class='myhead' width='200px'>").append(containerList.get(l).getReportName()).append(":</td><td>").append(timeDetailsArray.get(2)).append("</td></tr>");
        }
        reportTime.append("</table>");
//        String paramHtml="";
        StringBuilder paramHtml = new StringBuilder(200);
        ArrayList<String> firstParamNames = new ArrayList<String>();
        ArrayList<String> firstParamIds = new ArrayList<String>();

        ArrayList<String> paramIds = new ArrayList<String>();
        ArrayList<String> paramNames = new ArrayList<String>();
        firstParamNames = (ArrayList) containerList.get(0).getParametersHashMap().get("ParametersNames");
        firstParamIds = (ArrayList) containerList.get(0).getParametersHashMap().get("Parameters");
        for (int i = 0; i < firstParamIds.size(); i++) {
            paramIds.add(firstParamIds.get(i));
            paramNames.add(firstParamNames.get(i));
        }

        for (int k = 0; k < paramIds.size(); k++) {
//            paramHtml+="<option value=\""+paramIds.get(k)+"\">"+paramNames.get(k)+"</option>";
            paramHtml.append("<option value=\"").append(paramIds.get(k)).append("\">").append(paramNames.get(k)).append("</option>");
        }
        ReportTemplateDAO repDao = new ReportTemplateDAO();
        String html = repDao.buildPbReturnObject(containerList);

        request.setAttribute("html", html);
        request.setAttribute("paramHtml", paramHtml);
        request.setAttribute("reportTime", reportTime.toString());
        session.setAttribute("containerList", containerList);
        session.setAttribute("repIdList", repIdList);
        request.setAttribute("viewBy", rowViewBys.get(0));
//        out=response.getWriter();
//        out.print(html);

        return mapping.findForward("compare");
    }

    public ActionForward changeViewByCompare1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String viewBy = request.getParameter("viewBy");
        session.setAttribute("viewBy", viewBy);
//        PrintWriter out=null;

        List<Container> containerList = (List<Container>) session.getAttribute("containerList");
        String[] repIdList = (String[]) session.getAttribute("repIdList");
        HashMap map = new HashMap();
        ArrayList<String> changeViewBy = new ArrayList<String>();
        PbReportViewerBD repBD = new PbReportViewerBD();

        changeViewBy.add(viewBy);

        String userid = "";
        if (session.getAttribute("USERID") != null) {
            userid = session.getAttribute("USERID").toString();
        }
        for (int j = 0; j < repIdList.length; j++) {
            repBD.setRowViewIds(changeViewBy);
            repBD.setColViewIds(new ArrayList<String>());
            repBD.prepareReport("ChangeViewBy", containerList.get(j), repIdList[j], userid, new HashMap<String, String>());
        }
        map = (HashMap) session.getAttribute("PROGENTABLES");
//        String paramHtml="";
        StringBuilder paramHtml = new StringBuilder(200);
        ArrayList<String> firstParamNames = new ArrayList<String>();
        ArrayList<String> firstParamIds = new ArrayList<String>();

        ArrayList<String> paramIds = new ArrayList<String>();
        ArrayList<String> paramNames = new ArrayList<String>();
        firstParamNames = (ArrayList) containerList.get(0).getParametersHashMap().get("ParametersNames");
        firstParamIds = (ArrayList) containerList.get(0).getParametersHashMap().get("Parameters");
        for (int i = 0; i < firstParamIds.size(); i++) {
            paramIds.add(firstParamIds.get(i));
            paramNames.add(firstParamNames.get(i));
        }
        for (int k = 0; k < paramIds.size(); k++) {
//            paramHtml+="<option value=\""+paramIds.get(k)+"\">"+paramNames.get(k)+"</option>";
            paramHtml.append("<option value=\"").append(paramIds.get(k)).append("\">").append(paramNames.get(k)).append("</option>");
        }
        ReportTemplateDAO repDao = new ReportTemplateDAO();
        String html = repDao.buildPbReturnObject(containerList);
        request.setAttribute("html", html);
        request.setAttribute("paramHtml", paramHtml);
        request.setAttribute("containerList", containerList);
        request.setAttribute("repIdList", repIdList);
        request.setAttribute("viewBy", viewBy);
//        try {
//            out = response.getWriter();
//            out.print(html);
//        } catch (IOException ex) {
//            logger.error("Exception:",ex);
//        }

        return mapping.findForward("compare");
    }

//    public ActionForward changeViewByCompare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        HttpSession session=request.getSession();
//        Container firstContainer = null;
//        Container secondContainer = null;
//        String userid = "";
//        if (session.getAttribute("USERID") != null) {
//            userid = session.getAttribute("USERID").toString();
//        }
//        String firstRepId = request.getParameter("firstRepId");
//        String secondRepId = request.getParameter("secondRepId");
//        String firstColViewBy = request.getParameter("firstColViewBy");
//        String secondColViewBy = request.getParameter("secondColViewBy");
//        String viewBy = request.getParameter("viewBy");
//        ArrayList<String> changeViewBy=new ArrayList<String>();
//        ArrayList<String> changeFirstColViewBy=new ArrayList<String>();
//        ArrayList<String> changeSecondColViewBy=new ArrayList<String>();
//        changeViewBy.add(viewBy);
//        changeFirstColViewBy.add(firstColViewBy);
//        changeSecondColViewBy.add(secondColViewBy);
//
//        HashMap map = new HashMap();
//        map = (HashMap) session.getAttribute("PROGENTABLES");
//        firstContainer = (Container) map.get(firstRepId);
//        secondContainer = (Container) map.get(secondRepId);
//          ArrayList timeDetailsArray1=firstContainer.getReportCollect().timeDetailsArray;
//        ArrayList timeDetailsArray2=secondContainer.getReportCollect().timeDetailsArray;
//         String reportTime="<table><tr><td class='myhead'>"+firstContainer.getReportName()+":</td><td>"+timeDetailsArray1.get(2)+"</td></tr>";
//        reportTime+="<tr><td class='myhead' >"+secondContainer.getReportName()+":</td><td>"+timeDetailsArray2.get(2)+"</td></tr></table>";
//
//        String paramHtml="";
//        ArrayList<String> firstParamNames = new ArrayList<String>();
//        ArrayList<String> secParamNames = new ArrayList<String>();
//        ArrayList<String> firstParamIds = new ArrayList<String>();
//        ArrayList<String> secParamIds = new ArrayList<String>();
//        ArrayList<String> paramIds = new ArrayList<String>();
//        ArrayList<String> paramNames = new ArrayList<String>();
//        firstParamNames = (ArrayList) firstContainer.getParametersHashMap().get("ParametersNames");
//        firstParamIds = (ArrayList) firstContainer.getParametersHashMap().get("Parameters");
//        secParamNames = (ArrayList) secondContainer.getParametersHashMap().get("ParametersNames");
//        secParamIds = (ArrayList) secondContainer.getParametersHashMap().get("Parameters");
//        for(int i=0;i<firstParamIds.size();i++){
//            for(int j=0;j<secParamIds.size();j++){
//                if(firstParamIds.get(i).equals(secParamIds.get(j))){
//                    paramIds.add(firstParamIds.get(i));
//                    paramNames.add(firstParamNames.get(i));
//                }
//            }
//        }
//        for(int k=0;k<paramIds.size();k++){
//            if(viewBy.equals(paramIds.get(k)))
//                paramHtml+="<option value=\""+paramIds.get(k)+"\" selected=\"\">"+paramNames.get(k)+"</option>";
//            else
//                paramHtml+="<option value=\""+paramIds.get(k)+"\">"+paramNames.get(k)+"</option>";
//        }
//        PbReportViewerBD repBD = new PbReportViewerBD();
//        PbReportRequestParameter reportReqParams = new PbReportRequestParameter(request);
//        reportReqParams.setParametersHashMap();
//        repBD.setRowViewIds(changeViewBy);
//        if(!firstColViewBy.isEmpty())
//            repBD.setColViewIds(changeFirstColViewBy);
//        else
//            repBD.setColViewIds(new ArrayList<String>());
//        repBD.prepareReport("ChangeViewBy", firstContainer, firstRepId, userid, new HashMap<String, String>());
//        if(!secondColViewBy.isEmpty())
//            repBD.setColViewIds(changeSecondColViewBy);
//        else
//            repBD.setColViewIds(new ArrayList<String>());
//        repBD.prepareReport("ChangeViewBy", secondContainer, secondRepId, userid, new HashMap<String, String>());
//        ReportTemplateDAO repDao=new ReportTemplateDAO();
//        String html=repDao.getNewPbReturnObject(firstContainer,secondContainer,firstRepId,secondRepId,userid);
//        request.setAttribute("html", html);
//        request.setAttribute("frstRepId", firstRepId);
//        request.setAttribute("secndRepId", secondRepId);
//        request.setAttribute("paramHtml", paramHtml);
//        request.setAttribute("firstColViewBy", changeFirstColViewBy);
//        request.setAttribute("secondColViewBy", changeSecondColViewBy);
//        request.setAttribute("reportTime", reportTime);
////                prepareReport(action,container,pbReportId,pbUserId,reportReqParams.requestParamValues);
//        return mapping.findForward("compare");
//    }
    public ActionForward getComparableReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String reportId = request.getParameter("reportId");
        ArrayList<String> droppableList = new ArrayList<String>();
        ArrayList<String> draggableList = new ArrayList<String>();
//        ArrayList<String> droppableListnames=new ArrayList<String>();
        ArrayList<String> draggableListNames = new ArrayList<String>();
        GenerateDragAndDrophtml dragAndDrophtml = null;
//        boolean flag=false;
        Container container = Container.getContainerFromSession(request, reportId);
        String[] bizRoles = container.getReportCollect().reportBizRoles;
        ArrayList<String> rowViewBys = container.getReportCollect().reportRowViewbyValues;
        ReportTemplateDAO dao = new ReportTemplateDAO();
        List<ReportDetails> reportDetails = dao.getReportsForComparison(userId, bizRoles[0], rowViewBys);
//        ArrayList<String> elementIdList=new ArrayList<String>();
//        elementIdList=dao.getElementIdListForReport(reportId);

        for (ReportDetails reportDetail : reportDetails) {
            if (!reportId.equalsIgnoreCase(reportDetail.getReportId())) //            {
            //                flag=dao.IsComparableReport(elementIdList,reportDetail.getReportId());
            //                if(flag==true)
            //                {
            {
                draggableList.add(reportDetail.getReportId());
                draggableListNames.add(reportDetail.getReportName());
            }
//                }
//            }
        }
        dragAndDrophtml = new GenerateDragAndDrophtml("Select columns from below", "Drag columns to here", droppableList, draggableList, request.getContextPath());
        dragAndDrophtml.setDragableListNames(draggableListNames);
        String htmlData = dragAndDrophtml.getDragAndDropDiv();
//        Gson gson=new Gson();
//        String reportJson=gson.toJson(reportDetails);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(htmlData);
        return null;
    }

    public ActionForward getReportDetailsList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reportId = request.getParameter("reportId");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        PbReturnObject retobj = dao.getReportDetailsList(reportId);
        Map<String, String> map = new HashMap<String, String>();
        if (retobj.rowCount != 0) {
            map.put("ReportName", retobj.getFieldValueString(0, 0));
            map.put("ReportDesc", retobj.getFieldValueString(0, 1));
            map.put("BusinessRole", retobj.getFieldValueString(0, 2));
            map.put("CreatedOn", retobj.getFieldValueDateString(0, 3));
            map.put("CreatedBy", retobj.getFieldValueString(0, 4));
            map.put("LastUpdateOn", retobj.getFieldValueDateString(0, 5));
            map.put("LastUpdateBy", retobj.getFieldValueString(0, 6));
        }
        Gson gson = new Gson();
        String reportdetails = gson.toJson(map);
        //System.out.println("reportdeatils"+reportdetails.toString());
        response.getWriter().print(reportdetails);
        return null;
    }

    public ActionForward pbParamDefaultValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String elementId = request.getParameter("elementId");
        List<String> excvallist = null;
        ArrayList existvalues = new ArrayList();
        String REPORTID = request.getParameter("REPORTID");
        PbReportViewerDAO reportviewerdao = new PbReportViewerDAO();
        HashMap hmap = reportviewerdao.pbParamDefaultValues(elementId, REPORTID, request);
        ArrayList defaultvaleslist = (ArrayList) hmap.get("list");
        excvallist = (List<String>) hmap.get("existparamvalues");


        GenerateDragAndDrophtml generateDragAndDrophtml = null;
        if (excvallist.isEmpty()) {
            generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", null, defaultvaleslist, request.getContextPath());
        } else {
            for (int i = 0; i < excvallist.size(); i++) {
                existvalues.add(excvallist.get(i));
                generateDragAndDrophtml = new GenerateDragAndDrophtml("select columns from below", "drop columns here", existvalues, defaultvaleslist, request.getContextPath());
            }

        }
        String dragndrop = generateDragAndDrophtml.getDragAndDropDiv();
        response.getWriter().print(dragndrop);
        return null;
    }
    //Surender

    public ActionForward viewByReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String buzRoles = request.getParameter("busroleID");
        String busrolename = request.getParameter("busrolename");
        String PbUserId = String.valueOf(session.getAttribute("USERID"));
        String result = "";
        ReportTemplateDAO dao = new ReportTemplateDAO();
        request.setAttribute("busrolename", busrolename);
        result = dao.getAllReportsWithGrap(buzRoles, busrolename, PbUserId);

        response.getWriter().print(result);
        return null;
    }

//     public ActionForward getAddMoreDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//
//        HttpSession session = request.getSession(false);
//        HashMap map = new HashMap();
//        Container container = null;
//        String customReportId = "";
//        String foldersIds = "";
//        String PbUserId = "";
//        String result = "";
//        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
//        if (session != null) {
////            customReportId = request.getParameter("REPORTID");
//            foldersIds = request.getParameter("foldersIds");
//            PbUserId = String.valueOf(session.getAttribute("USERID"));
//            result = reportTemplateDAO.getUserDimDetails(foldersIds, PbUserId);
//            PrintWriter out = response.getWriter();
//            out.print(result);
//
//            return null;
//        } else {
//            return mapping.findForward("sessionExpired");
//        }
//    }
    public ActionForward addMoreDimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String PbUserId = "";
        String result = "";
          String aoIds = "";
        String IsrepAdhoc1;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            ArrayList<String> reportParamIds = new ArrayList<String>();
//            String reportParamIdsVal = "0";
            StringBuilder reportParamIdsVal = new StringBuilder();
            reportParamIdsVal.append("0");
            String reportId = request.getParameter("REPORTID");
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                if (container != null) {
                    reportParamIds = (ArrayList) container.getParametersHashMap().get("Parameters");
                    if (reportParamIds != null && reportParamIds.size() > 0) {
                        for (int i = 0; i < reportParamIds.size(); i++) {
//                        reportParamIdsVal = reportParamIdsVal +","+reportParamIds.get(i);
                            reportParamIdsVal.append(",").append(reportParamIds.get(i));
                        }
                    }
                }
            }

            IsrepAdhoc1 = (request.getParameter("IsrepAdhoc"));
            request.setAttribute("IsrepAdhoc1", IsrepAdhoc1);
            customReportId = request.getParameter("REPORTID");
            foldersIds = request.getParameter("foldersIds");
            aoIds = request.getParameter("aoid");
//            PbUserId = String.valueOf(session.getAttribute("USERID"));
            
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            //changed by Nazneen for getting dims which r not added to rep
//            result = reportTemplateDAO.getUserDimDetails(foldersIds, PbUserId, reportParamIdsVal);

//added by Mohit for AO
               if(aoIds !=null && !aoIds.equalsIgnoreCase("null") && !aoIds.equalsIgnoreCase("") )
               {
                   result = reportTemplateDAO.getUserDimDetailsForRepAO(aoIds, PbUserId, reportParamIdsVal.toString());
               }else
               {
            result = reportTemplateDAO.getUserDimDetailsForRep(foldersIds, PbUserId, reportParamIdsVal.toString());
               }

            
            request.setAttribute("dims", result);
            request.setAttribute("roleId", foldersIds);
            request.setAttribute("USERID", PbUserId);
            request.setAttribute("reportId", customReportId);
            if (request.getParameter("isOneview") != null && request.getParameter("isOneview").equalsIgnoreCase("True")) {
                request.setAttribute("isOneview", "true");
            }
            return mapping.findForward("addDims");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward addMoreDimensionsfilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException, ClassNotFoundException {

        String oneviewId = request.getParameter("REPORTID");
        String oneviewname = request.getParameter("oneviewname");
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
//        PbDb pbdb = new PbDb();
        String[] colNames = null;
        ReportTemplateDAO DAO = new ReportTemplateDAO();
        OnceViewContainer onecontainer = null;
        map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
//        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
//            String advHtmlFileProps=(String) request.getSession(false).getAttribute("advHtmlFileProps");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = reportTemplateDAO.getOneviewFileName(oneviewId);
        File file = null;
//            String bizzRoleName="";
        file = new File(oldAdvHtmlFileProps + "/" + fileName);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(oldAdvHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer = (OnceViewContainer) ois.readObject();
            ois.close();
        }


        String busroleid = "";
        List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
        for (int i = 0; i < oneviewletDetails.size(); i++) {
            OneViewLetDetails detail = oneviewletDetails.get(i);
//                   if(detail.getReptype()!=null && detail.getReptype().equalsIgnoreCase("repGraph")){
            busroleid = detail.getRoleId();
//              }

        }
//       HttpSession session = request.getSession(false);
//        HashMap map = new HashMap();
//        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String PbUserId = "";
//        String result = "";
        String IsrepAdhoc1;
        Gson gson = new Gson();
        ArrayList<String> viewidslist = new ArrayList<String>();
        ArrayList<String> viewbyisnames = new ArrayList<String>();
        Map<String, List<String>> allFiltersjson = new HashMap<String, List<String>>();
//        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
//            ArrayList<String> reportParamIds = new ArrayList<String>();
//            String reportParamIdsVal = "0";

// Type type = new TypeToken<List<String>>() { }.getType();
//             ReportObjectMeta goMeta = new ReportObjectMeta();
            FileReadWrite fileReadWrite = new FileReadWrite();
            Type goType = new TypeToken<ReportObjectMeta>() {
            }.getType();
            String fileLocation = "/usr/local/cache";
            oneviewletDetails = onecontainer.onviewLetdetails;
            for (int n = 0; n < oneviewletDetails.size(); n++) {
                OneViewLetDetails detail = oneviewletDetails.get(n);
                String repid = detail.getRepId();
//             String repname=detail.getRepName();
                busroleid = detail.getRoleId();
//             String rolename=detail.getRolename();
                ReportObjectMeta reportMeta = new ReportObjectMeta();
                String goPath = fileLocation + "/analyticalobject/R_GO_" + repid + ".json";
                String goData = fileReadWrite.loadJSON(goPath);
                reportMeta = gson.fromJson(goData, goType);
                if (!reportMeta.getViewIds().isEmpty()) {
                    List<String> list = reportMeta.getViewIds();
                    List<String> list1 = reportMeta.getViewNames();
                    allFiltersjson.put(repid, list);
                    if (viewidslist.isEmpty()) {

                        viewidslist.addAll(list);
                        viewbyisnames.addAll(list1);
                        if (viewidslist.get(0) == null ? "" == null : viewidslist.get(0).equals("")) {
                            viewidslist.remove(0);
                        }
                        if (viewbyisnames.get(0) == null ? "" == null : viewbyisnames.get(0).equals("")) {
                            viewbyisnames.remove(0);
                        }
                    } else {
                        for (int i1 = 0; i1 < list.size(); i1++) {
                            if (viewidslist.contains(list.get(i1).toString())) {
                            } else {
                                viewidslist.add(list.get(i1));
                                viewbyisnames.add(list1.get(i1));
                            }
//if(viewbyisnames.contains(list1.get(i1).toString())){
//
//}else{
//    viewbyisnames.add(list1.get(i1));
//}
                        }
                    }
                }
// goMeta = gson.fromJson(goData, goType);
            }

            IsrepAdhoc1 = (request.getParameter("IsrepAdhoc"));
            request.setAttribute("IsrepAdhoc1", IsrepAdhoc1);
            customReportId = request.getParameter("REPORTID");
            foldersIds = busroleid;
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            //changed by Nazneen for getting dims which r not added to rep
//  result = reportTemplateDAO.getUserDimDetailsForRep(foldersIds, PbUserId, reportParamIdsVal);
//            result = reportTemplateDAO.getUserDimsMbrsForOneview(foldersIds, PbUserId, reportParamIdsVal,viewbyisnames,viewidslist);
//            request.setAttribute("dims",result);
            request.setAttribute("roleId", foldersIds);
            session.setAttribute("allViewIds", viewidslist);
            session.setAttribute("allViewNames", viewbyisnames);
            session.setAttribute("fromoneview", "true");
            request.setAttribute("USERID", PbUserId);
            request.setAttribute("reportId", customReportId);
            session.setAttribute("oneviewname", oneviewname);
            if (request.getParameter("isOneview") != null && request.getParameter("isOneview").equalsIgnoreCase("True")) {
                request.setAttribute("isOneview", "true");
            }
            return null;
//            return mapping.findForward("filterview");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveGlobalFilterOneview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException, ClassNotFoundException, Exception {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Gson gson = new Gson();
        ArrayList<String> parameterlist = new ArrayList<String>();
        ArrayList<String> parameterlistNames = new ArrayList<String>();
        ArrayList viewbygblname = new ArrayList();
        OnceViewContainer onecontainer = null;
        String oneviewId = request.getParameter("oneviewId");
        String oneviewopen = request.getParameter("oneviewopen");
        StringBuilder stringbuilder = new StringBuilder();
        Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
        Map<String, List<String>> allFiltersjson = new HashMap<String, List<String>>();
        Map<String, List<String>> allFiltersnames = new HashMap<String, List<String>>();
        map = (HashMap) session.getAttribute("ONEVIEWDETAILS");
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        String oldAdvHtmlFileProps = (String) request.getSession(false).getAttribute("oldAdvHtmlFileProps");
//            String advHtmlFileProps=(String) request.getSession(false).getAttribute("advHtmlFileProps");
//            String isseurity=(String) request.getSession(false).getAttribute("isseurity");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = reportTemplateDAO.getOneviewFileName(oneviewId);
        File file = null;
//            String bizzRoleName="";
        file = new File(oldAdvHtmlFileProps + "/" + fileName);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(oldAdvHtmlFileProps + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            onecontainer = (OnceViewContainer) ois.readObject();
            ois.close();
        }
        ArrayList viewbynames = new ArrayList();
        PbDb pbdb = new PbDb();
        String[] filterParametersArr;
        String[] filterParametersArrNames;
        String filterParamIds = request.getParameter("filterParameterIds");
        String filterParamNames = request.getParameter("filterParameterNames");

        if (oneviewopen != null && oneviewopen.equalsIgnoreCase("true")) {
            allFilters = onecontainer.getallFilters();
            viewbygblname = onecontainer.getviewbygblname();
            if (allFilters != null) {
                allFiltersnames = onecontainer.getallFiltersnames();
                Set keySet = allFilters.keySet();
                Iterator itr = keySet.iterator();
                String key;
                while (itr.hasNext()) {
                    key = itr.next().toString();
                    parameterlist.add(key);

                }
                Set keySet1 = allFiltersnames.keySet();
                Iterator itr1 = keySet1.iterator();
                String key1;
                while (itr1.hasNext()) {
                    key1 = itr1.next().toString();
                    parameterlistNames.add(key1);

                }
            }
//    parameterlist.add(allFilters);
        } else {
            filterParametersArr = filterParamIds.split(",");
            filterParametersArrNames = filterParamNames.split(",");
            parameterlist.addAll(Arrays.asList(filterParametersArr));
            parameterlistNames.addAll(Arrays.asList(filterParametersArrNames));
            viewbynames.addAll(Arrays.asList(filterParametersArrNames));
            onecontainer.setFilterParameters(parameterlist);
        }

        if (!parameterlist.isEmpty() && !parameterlistNames.isEmpty()) {
//            stringbuilder.append("<table><tr style=\"width:100px\">");
            for (int i = 0; i < parameterlist.size(); i++) {

                ArrayList<String> a = new ArrayList<String>();
//            String listString = "";
//            stringbuilder.append("<td>" + parameterlistNames.get(i) + ":</td>");

                //   stringbuilder.append("<div class='expandDiv"+parameterlist.get(i)+"' name='expand' width='13%' onclick='expandDiv("+parameterlist.get(i)+")' style='border-bottom: 1px solid lightgrey;padding-top:.5em;padding-bottom:.5em;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:1px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>");

//            stringbuilder.append("<td></td><td><div class=\"multiselect\">");
                // stringbuilder.append("<div  class=\"selectBox\" onclick=\"showCheckboxes("+parameterlist.get(i)+")\">");
//        stringbuilder.append("<div  class=\"selectBox\" onclick=\"showCheckboxes()\">");
                //    stringbuilder.append("<select><option selected>All</option></select>");
                if (oneviewopen != null && oneviewopen.equalsIgnoreCase("true") && viewbygblname != null) {
                    if (viewbygblname.size() > i) {
//                           stringbuilder.append("<select id='viewBy_"+i+"' multiple onchange='applyGlobalFilterinOneview(this.value,this.id)'><option  value="+viewbygblname.get(i).toString().replace("'", "")+">"+viewbygblname.get(i).toString().replace("'", "")+"</option>");
                    } else {
//                     stringbuilder.append("<select id='viewBy_"+i+"' multiple onchange='applyGlobalFilterinOneview(this.value,this.id)'><option  value=All>All</option>");
                    }
//                stringbuilder.append("<option  value='"+parameterlistNames.get(i)+"-00-"+parameterlist.get(i)+"'>All</option>");
                } else {
//           stringbuilder.append("<select id='viewBy_"+i+"'multiple value='"+parameterlistNames.get(i)+"-00-"+parameterlist.get(i)+"' onchange='applyGlobalFilterinOneview(this.value,this.id)'><option  value='All'>All</option>");
                }
//                stringbuilder.append("<option>All</option>");
//            stringbuilder.append("</select>");
//            stringbuilder.append("<div class=\"overSelect\"></div>");
//        stringbuilder.append("</div>");
//        stringbuilder.append("<div id=\"checkboxes\" style=\"background-color: white; position: absolute; z-index: 999; width: inherit;\">");

//            stringbuilder.append("<label ><input type='checkbox' id='three'/>Third checkbox</label>");
//        stringbuilder.append("</div></div>");

                //  stringbuilder.append("<td ></td><td ><select  id='my-select" + parameterlistNames.get(i) + "' onchange=\"getglobalparamvales('my-select" + parameterlistNames.get(i) + "','" + parameterlistNames.get(i) + "','" + parameterlist.get(i) + "')\" style='font-size:9pt'>");
//<select multiple=\"multiple\" id=\"my-select\" style='font-size:9pt'>
                if (oneviewopen != null && oneviewopen.equalsIgnoreCase("true")) {
                    a.addAll(allFilters.get(parameterlist.get(i)));
                } else {
//           try{
//               PbReturnObject retObj1 = null;
//               String query1="select buss_col_name,BUSS_TABLE_NAME from prg_user_all_info_details where ELEMENT_ID= "+parameterlist.get(i);
//               retObj1=pbdb.execSelectSQL(query1);
//               Object[] ObjArray = new Object[3];
//                    ObjArray[0] =retObj1.getFieldUnknown(0, 0);
//                    ObjArray[1] =retObj1.getFieldUnknown(0, 1);
//                    ObjArray[2] =retObj1.getFieldUnknown(0, 0);
//             String query="select distinct  &   from  &  where & is not null ";
//
//            Statement st = null;
//        ResultSet rs = null;
//            Connection conn = ProgenConnection.getInstance().getConnectionForElement(parameterlist.get(0));
//            st =  conn.createStatement();
//               String finalQuery = pbdb.buildQuery(query,ObjArray);
//               rs = st.executeQuery(finalQuery);
//              // a.add("All");
//                while (rs.next()) {
//                a.add(rs.getString(1).replace("'", ""));
//            }
//
//
////for (String s : a)
////{
////    listString += s + "\t";
////}
//
//            rs.close();
//            rs = null;
//            st.close();
//            st = null;
//            conn.close();
//            conn = null;
//
//        } catch (Exception ex) {
//
//            logger.error("Exception:",ex);
//
//
//        }
                }
//            Gson gson = new Gson();
                Type type = new TypeToken<List<String>>() {
                }.getType();
                ReportObjectMeta goMeta = new ReportObjectMeta();
                FileReadWrite fileReadWrite = new FileReadWrite();
//         Type goType = new TypeToken<ReportObjectMeta>() { }.getType();
                String fileLocation = "/usr/local/cache";
                List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
                for (int n = 0; n < oneviewletDetails.size(); n++) {
                    OneViewLetDetails detail = oneviewletDetails.get(n);
                    String repid = detail.getRepId();
//             String repname=detail.getRepName();
//             String busroleid=detail.getRoleId();
//             String rolename=detail.getRolename();
                    String goPath = fileLocation + "/analyticalobject/filters/R_GO_" + repid + "/" + parameterlistNames.get(i) + ".json";
                    String goData = fileReadWrite.loadJSON(goPath);
                    if (goData != "{}") {
                        List<String> list = gson.fromJson(goData, type);
                        allFiltersjson.put(repid, list);
                        if (a.isEmpty()) {

                            a.addAll(list);
                            if (a.get(0) == null ? "" == null : a.get(0).equals("")) {
                                a.remove(0);
                            }
                        } else {
                            for (int i1 = 0; i1 < list.size(); i1++) {
                                if (a.contains(list.get(i1).toString())) {
                                } else {
                                    a.add(list.get(i1));
                                }
                            }
                        }
                    }
// goMeta = gson.fromJson(goData, goType);
                }
//               for(int n=0; n<a.size(); n++){
//                   String namidvale=parameterlist.get(i)+"-"+parameterlistNames.get(i);
//            stringbuilder.append("<option id='"+parameterlistNames.get(i)+"-"+n+"-"+parameterlist.get(i)+"'  value='"+parameterlistNames.get(i)+"-"+n+"-"+parameterlist.get(i)+"' >"+ a.get(n)+"</option>");
                // stringbuilder.append("<label ><input type='checkbox' id='"+parameterlistNames.get(i)+"-"+n+"-"+parameterlist.get(i)+"' name='"+parameterlist+"' onclick='applyGlobalFilterinOneview(this.id,this.name)'/>"+ a.get(n)+"</label>");

//               }

                allFilters.put(parameterlist.get(i), a);
                allFiltersnames.put(parameterlistNames.get(i), a);
                onecontainer.setallFilters(allFilters);
                onecontainer.setallFiltersnames(allFiltersnames);
//        stringbuilder.append("</div></div>");
                //stringbuilder.append("</select></td>");
//            stringbuilder.append("</select>");
                stringbuilder.append("<input  type='hidden' id='paramList_" + parameterlist.get(i) + "' name='paramList' value='" + gson.toJson(allFilters) + "'/>");
            }

            ArrayList viewbysold = new ArrayList();
            viewbysold = (ArrayList) session.getAttribute("viewbys");

            PbReturnObject securityfilters;
            String securityqry = "select * from PRG_USER_ROLE_MEMBER_FILTER where user_id = " + userId;
            securityfilters = (PbReturnObject) session.getAttribute("securityfilters");
//if(securityfilters!=null){

//}else{
            securityfilters = pbdb.execSelectSQL(securityqry);
            String FOLDER_ID = "";
//       String  MEMEBER_VALUE ="";
            String ELEMENT_ID = "";
            String flagoldviewbys = "";
            ArrayList viewbys = new ArrayList();
            ArrayList viewbys1 = new ArrayList();
            OneViewLetDetails oneviewlet = null;
            ArrayList QueryAggs1 = new ArrayList();
            ArrayList QueryCols1 = new ArrayList();
//        if(isseurity!=null && isseurity.equalsIgnoreCase("true")){
            if (securityfilters.getRowCount() > 0) {
                for (int i = 0; i < securityfilters.getRowCount(); i++) {

                    FOLDER_ID = securityfilters.getFieldValueString(0, "USER_ID");
                    ELEMENT_ID = securityfilters.getFieldValueString(i, "ELEMENT_ID");
//                  if(parameterlist.get(i).equalsIgnoreCase(ELEMENT_ID)){
//
//                       }else{
                    viewbys.add(ELEMENT_ID);
//                             }
                }
            }
//    }

            viewbys1 = viewbys;
//                         Map<String, List<String>> allFilters = new HashMap<String, List<String>>();
//  Map<String, List<String>> allFiltersnames = new HashMap<String, List<String>>();
            allFilters = onecontainer.getallFilters();
            if (viewbys != null) {
                if (allFilters != null) {

                    Set keySet = allFilters.keySet();
                    Iterator itr = keySet.iterator();
                    String key;
                    while (itr.hasNext()) {
                        key = itr.next().toString();
                        if (viewbys.contains(key)) {
                        } else {
                            viewbys.add(key);
                        }

                    }
                }
            } else {

                for (int i = 0; i < parameterlist.size(); i++) {
//                         if(parameterlist.get(i).equalsIgnoreCase(ELEMENT_ID)){
//
//                       }else{
                    viewbys.add(parameterlist.get(i));
//                             }
                }
            }
            List<OneViewLetDetails> oneviewletDetails = onecontainer.onviewLetdetails;
            for (int i = 0; i < oneviewletDetails.size(); i++) {
                OneViewLetDetails detail = oneviewletDetails.get(i);

                if (detail.getReptype() != null && detail.getReptype().equalsIgnoreCase("measures")) {
                    oneviewlet = oneviewletDetails.get(i);
                    flagoldviewbys = "false";
//                    if(detail.getQryColumns()!=null){
//                        for(int i1=0; i1<detail.getQryColumns().size();i1++){
//                    QueryCols1.add(detail.getQryColumns().get(i1));
//                    QueryAggs1.add(detail.getQueryAggs().get(i1));
//
//                        }
//
//                          }
                }
            }
//                    String flagoldviewbys="false";
            request.setAttribute("issecurity", "true");
            session.setAttribute("viewbys", viewbys);
            request.setAttribute("viewbys", viewbys);
            session.setAttribute("allFilters", allFilters);
            session.setAttribute("parameterlist", parameterlist);
            session.setAttribute("allFiltersnames", allFiltersnames);
            session.setAttribute("viewbynames", viewbynames);
            request.setAttribute("action", "save");
            PbReturnObject pbretObjTime1 = new PbReturnObject();
            DashboardViewerDAO dao = new DashboardViewerDAO();
//                     if(viewbysold!=null){
//if(viewbysold.size()==viewbys.size()){
//    for(int i1=0;i1<viewbysold.size();i1++){
//        if(viewbysold.get(i1).toString().equalsIgnoreCase(viewbys.get(i1).toString())){
//flagoldviewbys="true";
//        }else{
//flagoldviewbys="false";
//break;
//        }
//    }
//}
//         }
            if (oneviewopen != null && oneviewopen.equalsIgnoreCase("true") || oneviewopen != null && oneviewopen.equalsIgnoreCase("save")) {
            } else {
                if (flagoldviewbys != null && flagoldviewbys.equalsIgnoreCase("false")) {
                    pbretObjTime1 = dao.getReturnObjectForOneView(QueryCols1, QueryAggs1, userId, oneviewlet.getRoleId(), request, onecontainer, oneviewlet);
                }
            }
//stringbuilder.append("<td id='goTabId1' style='padding-left: 1.5em;'>");
//     stringbuilder.append("<input id='gottabId1' class='navtitle-hover' type='button'  value='Go' onclick=\"gotForOneview1('Go')\" style='width:25px' name=''></input></td>");
//        stringbuilder.append("</tr></table>");
            PrintWriter out = response.getWriter();
            if (oneviewopen != null && oneviewopen.equalsIgnoreCase("true")) {
                if (onecontainer.getallFilters() != null) {
                    out.print(gson.toJson(allFilters));
                } else {
                    out.print("nofilter");
                }
            } else {
                out.print(gson.toJson(allFilters));
            }
        } else {
            stringbuilder.append("");
        }
        return null;
    }
//end of code by Bhargavi

    public ActionForward saveNewDimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("ReportId");
        String dimIds = request.getParameter("dimIds");
        String dimName = request.getParameter("dimName");
        String ReportLayout = request.getParameter("ReportLayout");
        Container container = Container.getContainerFromSession(request, reportId);
        PbReportCollection collect = (PbReportCollection) container.getReportCollect();
        container.ReportLayout = ReportLayout;
        String[] dimidarray;
        dimidarray = dimIds.split(",");
        String dimIdsstr = null;
        for (int i = 0; i < dimidarray.length; i++) {
            if (i == 0) {
                dimIdsstr = dimidarray[i].replace("elmnt-", "").trim();
            } else {
                dimIdsstr = dimIdsstr + "," + dimidarray[i].replace("elmnt-", "").trim();
            }
        }
        ReportTemplateDAO dao = new ReportTemplateDAO();
        dao.saveNewDimensions(reportId, dimIdsstr, container);

        return null;
    }

    public ActionForward getFavReports1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));

        ReportTemplateDAO dao = new ReportTemplateDAO();
        List<ReportDetails> favReportDetails = dao.getFavouriteRept1(userId);
        Gson gson = new Gson();
        String reportJson = gson.toJson(favReportDetails);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.println(reportJson);
        return null;
    }

    public ActionForward saveTabs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids = request.getParameter("tempArray");
        String userid = request.getParameter("userid");
        HashMap reportname = new HashMap();
        String[] id = ids.split(",");
        String name1 = null;
        String Id = null;
        ArrayList name = new ArrayList();
        ArrayList type = new ArrayList();
        ArrayList repid = new ArrayList();
        PbDb pbdb = new PbDb();
//             String query3="truncate  table PRG_HOME_TABS";
//             pbdb.execUpdateSQL(query3);
//           HttpSession session=request.getSession(true);
        HttpSession session = request.getSession(false);
        for (int i = 0; i < id.length; i++) {

            if (id[i].contains("_")) {
                Id = id[i].substring(4);
            } else {
                Id = id[i].substring(6);
            }
            ReportTemplateDAO dao = new ReportTemplateDAO();
            dao.getreportdetails(Id, userid);

        }

        return null;
    }
    //Surender

    public ActionForward getKpiDashboards(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String buzRoles = request.getParameter("busroleID");
        PbReturnObject retObj = null;
        String[] colNames = null;
        DashboardTemplateDAO dao = new DashboardTemplateDAO();
        String repName = "";
        String repId = "";

        retObj = dao.getKpiDashboardForOneview(buzRoles);
        StringBuilder repIds = new StringBuilder();
        StringBuilder repNames = new StringBuilder();
        StringBuilder kpiTypes = new StringBuilder();
//        repIds.append("{ DashboardIds: [");
//        repNames.append(" DashboardNames: [");
//        kpiTypes.append(" KpiTypes: [");
        StringBuilder kpisDetails = new StringBuilder();
        try {

            if (retObj != null) {
                colNames = retObj.getColumnNames();

                for (int i = 0; i < retObj.getRowCount(); i++) {
                    repId = retObj.getFieldValueString(i, colNames[0]);
                    repName = retObj.getFieldValueString(i, colNames[1]);
                    Object[] ObjArray = new Object[1];
                    ObjArray[0] = repId;
                    String buildDashBoardQuery = "";
                    String finalQuery = "";

                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        buildDashBoardQuery = "SELECT a.DASHBOARD_DETAILS_ID,a.KPI_TYPE,a.KPI_NAME ,a.KPI_MASTER_ID,A.DISPLAY_TYPE FROM PRG_AR_DASHBOARD_DETAILS as a inner join PRG_AR_REPORT_MASTER c on (a.dashboard_id =c.report_id) WHERE a.dashboard_id=& ";

                    } else {
                        buildDashBoardQuery = "SELECT a.DASHBOARD_DETAILS_ID,a.kpi_type,a.KPI_NAME,a.KPI_MASTER_ID,A.DISPLAY_TYPE FROM PRG_AR_DASHBOARD_DETAILS a ,PRG_AR_REPORT_MASTER c where a.dashboard_id=& and a.dashboard_id=c.report_id ";
                    }
                    PbDb pbdb = new PbDb();
                    finalQuery = pbdb.buildQuery(buildDashBoardQuery, ObjArray);
                    PbReturnObject retObj1 = null;

                    retObj1 = pbdb.execSelectSQL(finalQuery);
                    boolean test = false;
                    String kpityesval = null;

                    ArrayList<String> kpitypes = new ArrayList<String>();
                    ArrayList<String> kpiDashLetIds = new ArrayList<String>();
                    ArrayList<String> kpiMasterIds = new ArrayList<String>();
                    ArrayList<String> kpiMeterGraphs = new ArrayList<String>();
                    for (int j = 0; j < retObj1.getRowCount(); j++) {
//                    if(!kpitypes.isEmpty()){
//                       kpitypes.clear();
//                    }
                        String kpiType = retObj1.getFieldValueString(j, 1);
                        if (kpiType != null && !kpiType.equalsIgnoreCase("") && !kpiType.equalsIgnoreCase("Complexkpi")) {
                            kpitypes.add(kpiType);
                            kpiDashLetIds.add(retObj1.getFieldValueString(j, 0));
                            kpiMasterIds.add(retObj1.getFieldValueString(j, 3));

//                         repIds.append("\"").append(repId).append("\"");
//                         repNames.append("\"").append(repName).append("\"");
//                         kpiTypes.append("\"").append(kpiType).append("\"");
//
//                        if (i != retObj.getRowCount() - 1) {
//                            repIds.append(",");
//                            repNames.append(",");
//                            kpiTypes.append(",");
//                          }
                        }
                        String displayType = retObj1.getFieldValueString(j, 4);
                        if (displayType != null && displayType.equalsIgnoreCase("KPIGraph") && !displayType.equalsIgnoreCase("")) {
                            kpiMeterGraphs.add(displayType);
                            kpiDashLetIds.add(retObj1.getFieldValueString(j, 0));
                            kpiMasterIds.add(retObj1.getFieldValueString(j, 3));
                        }
                    }
//                if(!kpitypes.isEmpty()){

                    if (!kpitypes.isEmpty() || !kpiMeterGraphs.isEmpty()) {
                        kpisDetails.append("<ul id='kpi'>");
                        kpisDetails.append("<li id='" + repName + "' class='closed'>");
                        kpisDetails.append("<img src='icons pinvoke/report.png'/>");
                        kpisDetails.append("<span onclick=\"dragGraph(" + repId + ")\" style='font-family: verdana; font-size: 8pt;'>" + repName + "</span>");
                        kpisDetails.append("<ul id='repName-" + repId + "' style='display: none;'>");
                        if (!kpitypes.isEmpty()) {
                            for (int p = 0; p < kpitypes.size(); p++) {
                                kpisDetails.append("<li id='" + repId + "' class='closed'>");
                                kpisDetails.append("<img src='icons pinvoke/chart.png'/>");
                                kpisDetails.append("<span id='graph-" + repId + "' style='font-family: verdana; font-size: 8pt;'>");
                                kpisDetails.append("<a href=\"javascript:KpiBuilding(" + repId + ",'" + kpitypes.get(p) + "','" + repName + "','" + kpiDashLetIds.get(p) + "','" + kpiMasterIds.get(p) + "')\">" + kpitypes.get(p) + "</a>");
                                kpisDetails.append("</span>");
                                kpisDetails.append("</li>");
                            }
                            kpitypes.clear();
                        }
                        if (!kpiMeterGraphs.isEmpty()) {
                            for (int p = 0; p < kpiMeterGraphs.size(); p++) {
                                kpisDetails.append("<li id='" + repId + "' class='closed'>");
                                kpisDetails.append("<img src='icons pinvoke/chart.png'/>");
                                kpisDetails.append("<span id='graph-" + repId + "' style='font-family: verdana; font-size: 8pt;'>");
                                kpisDetails.append("<a href=\"javascript:KpiBuilding(" + repId + ",'" + kpiMeterGraphs.get(p) + "','" + repName + "','" + kpiDashLetIds.get(p) + "','" + kpiMasterIds.get(p) + "')\">" + repName + "</a>");
                                kpisDetails.append("</span>");
                                kpisDetails.append("</li>");
                            }
                            kpiMeterGraphs.clear();
                        }
                        kpisDetails.append("</ul>");
                        kpisDetails.append("</li>");
                        kpisDetails.append("</ul>");
                        kpitypes.clear();

                    }
//                if(!kpitypes.isEmpty()){
//                     if(!kpitypes.isEmpty()){
//                         kpisDetails.append("<li id='"+repName+"' class='closed'>");
//                             kpisDetails.append("<img src='icons pinvoke/report.png'/>");
//                   kpisDetails.append("<span onclick=\"dragGraph("+repId+")\" style='font-family: verdana; font-size: 8pt;'>"+repName+"</span>");
//                      kpisDetails.append("<ul id='repName-"+repId+"' style='display: none;'>");
//                      for(int p=0;p<kpitypes.size();p++){
//                      kpisDetails.append("<li id='"+repId+"' class='closed'>");
//                      kpisDetails.append("<img src='icons pinvoke/chart.png'/>");
//                      kpisDetails.append("<span id='graph-"+repId+"' style='font-family: verdana; font-size: 8pt;'>");
//                      kpisDetails.append("<a href=\"javascript:KpiBuilding("+repId+",'"+kpitypes.get(p)+"','"+repName+"','"+kpiDashLetIds.get(p)+"','"+kpiMasterIds.get(p)+"')\">"+kpitypes.get(p)+"</a>");
//                      kpisDetails.append("</span>");
//                        kpisDetails.append("</li>");
//                    }
//                      
//                  kpisDetails.append("</ul>");
//               kpisDetails.append("</li>");
//               kpitypes.clear();
//                }
//                }

//            if(test){
//                    repIds.append("\"").append(repId).append("\"");
//                    repNames.append("\"").append(repName).append("\"");
//                    kpiTypes.append("\"").append(kpityesval).append("\"");
//
//                     if (i != retObj.getRowCount() - 1) {
//                            repIds.append(",");
//                            repNames.append(",");
//                            kpiTypes.append(",");
//                     }
//                    }

//                }
//                repIds.append("],").append(repNames).append("],").append(kpiTypes).append("]}");
                }
            }
        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }

        response.getWriter().print(kpisDetails.toString());
        return null;
    }

    public ActionForward removeDimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("ReportId");
        String dimIds = request.getParameter("dimIds");
        String dimName = request.getParameter("dimName");
        //
//        Container container = Container.getContainerFromSession(request, reportId);
//        PbReportCollection collect = (PbReportCollection)container.getReportCollect();
        String[] dimidarray;
        dimidarray = dimIds.split(",");
        String dimIdsstr = null;
        for (int i = 0; i < dimidarray.length; i++) {
            if (i == 0) {
                dimIdsstr = dimidarray[i].replace("elmnt-", "").trim();
            } else {
                dimIdsstr = dimIdsstr + "," + dimidarray[i].replace("elmnt-", "").trim();
            }
        }
        ReportTemplateDAO dao = new ReportTemplateDAO();
        dao.deleteDimensions(reportId, dimIdsstr);
//        dao.saveNewDimensions(reportId , dimIdsstr , container);

        return null;
    }

    public ActionForward graphInReportViewer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ProgenJqplotGraphBD jqplotgraphbd = new ProgenJqplotGraphBD();
        ProGenJqPlotChartTypes jqploContainer = new ProGenJqPlotChartTypes();
        String ReportId = request.getParameter("REPORTID");
        String graphid = request.getParameter("graphid");
        String graphtype = request.getParameter("graphtype");
        String grpidfrmrep = request.getParameter("gid");
        String graphString = "";
        HashMap jqpToJfNameMap = (HashMap) request.getSession(false).getAttribute("JqpToJfNameMap");
        HashMap jqpMap = (HashMap) request.getSession(false).getAttribute("JqpMap");
        String jqGraphTypeName = (String) jqpToJfNameMap.get(graphtype);
        String jqgraphId = (String) jqpMap.get(jqGraphTypeName);
        graphString = jqplotgraphbd.prepareJqplotGraph(ReportId, jqgraphId, jqGraphTypeName, jqploContainer, request, "", "jq", grpidfrmrep);
        try {
            PrintWriter out = response.getWriter();
            out.print(graphString);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward sequenceDimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("ReportId");
        String dimIds = request.getParameter("dimIds");
        String dimName = request.getParameter("dimName");

//        Container container = Container.getContainerFromSession(request, reportId);
//        PbReportCollection collect = (PbReportCollection)container.getReportCollect();
        String[] dimidarray;
        dimidarray = dimIds.split(",");
        String dimIdsstr = null;
        for (int i = 0; i < dimidarray.length; i++) {
            if (i == 0) {
                dimIdsstr = dimidarray[i].replace("elmnt-", "").trim();
            } else {
                dimIdsstr = dimIdsstr + "," + dimidarray[i].replace("elmnt-", "").trim();
            }
        }
        ReportTemplateDAO dao = new ReportTemplateDAO();
        dao.updateDimensions(reportId, dimIdsstr);
//        dao.saveNewDimensions(reportId , dimIdsstr , container);

        return null;
    }

    public ActionForward selectRoleGoToDesin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String repId = request.getParameter("repId");
        String roleId = request.getParameter("roleId");
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;

        request.setAttribute("ReportId", repId);
        request.setAttribute("fromDesigner", "true");
        request.setAttribute("roleid", roleId);

        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(repId);


            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", roleId);
//            result = reportTemplateDAO.getUserDims(foldersIds, PbUserId);
//            PrintWriter out = response.getWriter();
//            out.print(result);
            container.setParametersHashMap(ParametersHashMap);
            String[] strArray = new String[]{roleId};
            PbReportCollection collection = container.getReportCollect();
            collection.setReportBizRole(Integer.parseInt(roleId));
            container.setFlagg(true);
            container.setDefault_tab("Key" + repId, "table");
            return mapping.findForward("reportDesigner");
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward goToCreateReportDesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = null;
        String reportId = null;
        Container container = null;
        String reportName = null;
        String reportDesc = null;
        String editReportName = null;
        String PbUserId = "";
        String repId = null;
        String result = "";
        String AOFlag = request.getParameter("aoFlag");
        if (AOFlag == null) {
            AOFlag = "false";
        }
        String url = request.getRequestURL().toString();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();

        if (session != null) {
            try {

                reportName = request.getParameter("reportName");
                reportName = reportName.replace('^', '&').replace('~', '+').replace('`', '#');
                reportName = reportName.replace("'", "''");
                reportDesc = request.getParameter("reportDesc");
                reportDesc = reportDesc.replace('^', '&').replace('~', '+').replace('`', '#');
                reportDesc = reportDesc.replace("'", "''");
                if (request.getParameter("editRepName") != null) {

                    repId = request.getParameter("repId");
                    editReportName = request.getParameter("editRepName");
                }
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                url = url + "?templateParam=goToReportDesigner&reportName=" + reportName + "&reportDesc=" + reportDesc;
                request.setAttribute("reportdesignerurl", url);
                reportId = reportViewerDAO.getCustomReportId();

                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(reportId) != null) {
                        container = (Container) map.get(reportId);
                        container = new Container();
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }

                PbReportCollection collect = container.getReportCollect();
                if (collect == null) {
                    collect = new PbReportCollection();
                }
                container.setReportCollect(collect);
                ////.println(container.getReportId());
                container.setReportId(reportId);
                container.setTableId(reportId);
                container.setNetTotalReq(false);
                container.setGrandTotalReq(true);
                if (!AOFlag.equalsIgnoreCase("true")) {
                    result = "<table><tr><td><ul>";
                    result += reportTemplateDAO.getUserFoldersByUserIdInDesigner(PbUserId);
                    result += "</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"creationOfReport(" + reportId + ")\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>";
                } else {
                    result = " ";
                    result = "<table><tr><td><ul>";
                    StringBuffer outerBuffer = new StringBuffer("");
                    String AOId = "";
                    String AOName = "";
                    File datafile = null;
                    String fileLocation = new PbReportViewerDAO().getFilePath(session);
                    String listOfAOQuery = "select AO_ID,AO_NAME from prg_ar_ao_master";
                    PbReturnObject returnObject = new PbReturnObject();
                    returnObject = new PbDb().execSelectSQL(listOfAOQuery);
                    if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                        for (int i = 0; i < returnObject.getRowCount(); i++) {
                            AOId = returnObject.getFieldValueString(i, 0);
                            AOName = returnObject.getFieldValueString(i, 1);
                            //added by mohit
                            datafile = new File(fileLocation + "/analyticalobject/M_AO_" + AOId + ".json");
                            if (datafile.exists()) {
                                outerBuffer.append("<li class='closed' id='" + AOId + "'>");
                                outerBuffer.append("<input type='radio' name='AOList' id='" + AOId + "'><span><font size='1px' face='verdana'><b>" + AOName + "</b></font></span>");
                                outerBuffer.append("</li>");
                            }

                        }
                    }
                    result += outerBuffer.toString();
                    result += "</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"creationOfReportAO(" + reportId + "," + AOFlag + ")\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>";
                }


                request.setAttribute("ReportId", reportId);
                request.setAttribute("UserFlds", result);

                container.setReportName(reportName);
                container.setReportDesc(reportDesc);

                //PbReportCollection collect = new PbReportCollection();
                //collect.reportId = reportId;
                //container.setReportCollect(collect);
                container.setReportId(reportId);

                container.setSessionContext(session, container);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            if (editReportName == null) {
                PrintWriter out = response.getWriter();
                out.print(result);
                return null;
//                return mapping.findForward(SUCCESS);
            } else {

                reportTemplateDAO.editReportName(repId, reportName, reportDesc);
                return null;
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getRoleIdforDesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String PbUserId = "";
        String result = "";
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            foldersIds = request.getParameter("foldersIds");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);


            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", foldersIds);
//            result = reportTemplateDAO.getUserDims(foldersIds, PbUserId);
//            PrintWriter out = response.getWriter();
//            out.print(result);
            container.setParametersHashMap(ParametersHashMap);
            String[] strArray = new String[]{foldersIds};
            PbReportCollection collection = container.getReportCollect();
            collection.setReportBizRole(Integer.parseInt(foldersIds));

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward savedesignerParamDimensions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = request.getParameter("ReportId");
        String dimIds = request.getParameter("dimIds");
        String dimName = request.getParameter("dimName");
        Container container = Container.getContainerFromSession(request, reportId);
        PbReportCollection collect = (PbReportCollection) container.getReportCollect();
        String[] dimidarray;
        String[] dimNamearay;
        dimidarray = dimIds.split(",");
        dimNamearay = dimName.split(",");
        String dimIdsstr = null;
        String dimNamestr = null;
        for (int i = 0; i < dimidarray.length; i++) {
            if (i == 0) {
                dimIdsstr = dimidarray[i].replace("elmnt-", "").trim();
            } else {
                dimIdsstr = dimIdsstr + "," + dimidarray[i].replace("elmnt-", "").trim();
            }
        }
        for (int i = 0; i < dimNamearay.length; i++) {
            if (i == 0) {
                dimNamestr = dimNamearay[i].toString().substring(0, dimNamearay[i].toString().lastIndexOf("^")).trim();
            } else {
                dimNamestr = dimNamestr + "," + dimNamearay[i].toString().substring(0, dimNamearay[i].toString().lastIndexOf("^")).trim();
            }
        }
        ReportTemplateDAO dao = new ReportTemplateDAO();
        dao.saveDesignerDimensions(reportId, dimIdsstr, dimNamestr, container);

        return null;
    }

    public ActionForward designerViewbys(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;

        String userId = "";
        String rowViewByArray = request.getParameter("RowViewByArray");
        String buildTableChange = request.getParameter("buildTableChange");
        String[] rowIdArr = rowViewByArray.split(",");
        String colViewByArray = request.getParameter("ColViewByArray");
        String[] colIdArr = colViewByArray.split(",");
        String colViewNamesArr = request.getParameter("colViewNamesArr");
        String[] colNamesArr = colViewNamesArr.split(",");
        String rowViewNamesArr = request.getParameter("rowViewNamesArr");
        String[] rowNamesArr = rowViewNamesArr.split(",");
        String reportId = request.getParameter("reportId");
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


        ArrayList<String> rowViewByLst = new ArrayList<String>(Arrays.asList(rowIdArr));
        ArrayList<String> rowViewNamesLst = new ArrayList<String>(Arrays.asList(rowNamesArr));
        HashMap TableHashMap = null;
        HashMap GraphHashMap = null;


        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
                session.removeAttribute("colViewByLst");
                session.removeAttribute("rowViewByLst");
                PbReportCollection collection = container.getReportCollect();
                collection.setRowViewBys(rowViewByLst);
                session.setAttribute("colViewByLst", colViewByLst);
                session.setAttribute("rowViewByLst", rowViewByLst);
                session.setAttribute("rowViewIdList", rowViewByLst);
                session.setAttribute("colViewIdList", colViewByLst);
                session.setAttribute("rowNamesLst", rowViewNamesLst);
                session.setAttribute("colNamesLst", colViewNamesLst);
                container.setColumnViewList(colViewByLst);
                container.setRowViewList(rowViewByLst);
                TableHashMap = container.getTableHashMap();
                GraphHashMap = container.getGraphHashMap();
                HashMap ParametersHashMap = container.getParametersHashMap();
//            ParametersHashMap.put("Parameters", ParamId);
//            ParametersHashMap.put("ParametersNames", ParamName);

                TableHashMap.put("REP", rowViewByLst);
                TableHashMap.put("CEP", colViewByLst);
//                session.removeAttribute("allViewIds");
//                session.removeAttribute("allViewNames");
//                session.removeAttribute("rowViewIdList");
//                session.removeAttribute("colViewIdList");
//                session.removeAttribute("rowNamesLst");
//                session.removeAttribute("colNamesLst");
            }

            PbReportViewerBD reportViewerBD = new PbReportViewerBD();
            HashMap ParametersHashMap = container.getParametersHashMap();

            ArrayList Parameters = null;
            ArrayList ParametersNames = null;

            if (ParametersHashMap.get("ParametersNames") != null) {
                Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
            }
            if (TableHashMap != null) {
                if (rowViewByArray != null && !rowViewByArray.equalsIgnoreCase("")) {
                    String[] REP = rowViewByArray.split(",");
                    String[] REPNames = rowViewNamesArr.split(",");//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com
                    ArrayList rowEdgeArray = new ArrayList();
                    ArrayList rowEdgeNameArray = new ArrayList();//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com

                    for (int i = 0; i < REP.length; i++) {
                        if (!(REP[i].equalsIgnoreCase(""))) {
                            rowEdgeArray.add(REP[i]);
                            rowEdgeNameArray.add(REPNames[i]);//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com
                        }
                    }

                    TableHashMap.put("REP", rowEdgeArray);
                    TableHashMap.put("REPNames", rowEdgeNameArray);//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com

                    GraphHashMap = reportViewerBD.changeViewBys(GraphHashMap, rowEdgeArray, rowEdgeNameArray, Parameters, ParametersNames);
                }
                if (colViewByArray != null && colViewByArray.equalsIgnoreCase("CEP")) {
                    String[] CEP = colViewByArray.split(",");
                    String[] CEPNames = colViewNamesArr.split(",");//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com
                    ArrayList colEdgeArray = new ArrayList();
                    ArrayList colEdgeNameArray = new ArrayList();//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com

                    for (int i = 0; i < CEP.length; i++) {
                        if (!(CEP[i].equalsIgnoreCase(""))) {
                            colEdgeArray.add(CEP[i]);
                            colEdgeNameArray.add(CEPNames[i]);//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com
                        }
                    }
                    TableHashMap.put("CEP", colEdgeArray);
                    TableHashMap.put("CEPNames", colEdgeNameArray);//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com


                }
            }
            //session.setAttribute("TableHashMap", TableHashMap);
            container.setTableHashMap(TableHashMap);
            container.setGraphHashMap(GraphHashMap);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward refreshLocalHomePage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        HttpSession session = request.getSession(false);
        String graphtype = "";
        String hotFilePath = (String) session.getAttribute("reportAdvHtmlFileProps");
        String reportId = request.getParameter("reportId");
        String graphid = request.getParameter("graphId");

        reportViewerBD.homePageRefresh(hotFilePath, reportId);
        PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();
        JqplotGraphProperty graphproperty = new JqplotGraphProperty();
        graphproperty = reportViewerdao.getJqGraphDetails(graphid);
        if (graphproperty != null) {
            graphtype = graphproperty.getGraphTypename();
        }
        String resultStr = "{\"graphtype\":\"" + graphtype + "\"}";
        response.getWriter().print(resultStr);
        return null;
//                 mapping.findForward("startPage");

    }

    public ActionForward workBenchPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        return mapping.findForward("workBench");
    }

    public ActionForward createInsightDesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        HttpSession session = request.getSession(false);
        String insightName = null;
        String roleId;
        String PbUserId = "";
        String reportId = null;
        HashMap map = null;
        Container container = null;
        //String url = request.getRequestURL().toString();
        PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();
        if (session != null) {
            try {
                insightName = request.getParameter("insightName");
                insightName = insightName.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
                insightName = insightName.replace("'", "''");
                roleId = request.getParameter("roleId");
                PbUserId = String.valueOf(session.getAttribute("USERID"));
//             url = url + "?templateParam=goToReportDesigner&reportName=" + reportName + "&reportDesc=" + reportDesc;
//             request.setAttribute("reportdesignerurl", url);
                // reportId = reportViewerDAO.getCustomReportId();
                reportId = reportViewerDAO.getCustomInsightId();
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(reportId) != null) {
                        container = (Container) map.get(reportId);
                        container = new Container();
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }
                PbReportCollection collect = container.getReportCollect();
                if (collect == null) {
                    collect = new PbReportCollection();
                }

                HashMap ParametersHashMap = container.getParametersHashMap();
                ParametersHashMap.put("UserFolderIds", roleId);
                container.setParametersHashMap(ParametersHashMap);
                collect.setReportBizRole(Integer.parseInt(roleId));
                container.setReportCollect(collect);
                container.setReportId(reportId);
                container.setTableId(reportId);
//                container.setNetTotalReq(true);
//                container.setGrandTotalReq(true);
                request.setAttribute("ReportId", reportId);
                request.setAttribute("fromDesigner", "true");
                request.setAttribute("roleid", roleId);
                container.setReportName(insightName);
                container.setReportDesc(insightName);
                container.setReportId(reportId);
                container.setTreeTableDisplay(true);
                container.setSessionContext(session, container);
                response.getWriter().print(reportId);
                return null;
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward generateInsightTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String reportId = request.getParameter("reportId");
        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        String action = request.getParameter("action");
        String action1 = request.getParameter("action1");
        // 
        ReportTemplateBD templatebd = new ReportTemplateBD();
        if (action != null && action.equalsIgnoreCase("insightOpen")) {
            templatebd.getContainerFromDb(reportId, userId, session);
        } else if ((action != null && action.equalsIgnoreCase("open")) || (action1 != null && action1.equalsIgnoreCase("InsightTimeChange"))) {
            request.setAttribute("REPORTID", reportId);
            PbReportViewerBD viewerBd = new PbReportViewerBD();
            viewerBd.prepareReport(reportId, userId, request, response,false);
        }
        if (session != null) {
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                container.setReportType("I");
//                DataFacade facade = new DataFacade(container);
//                facade.setUserId(userId);
//                facade.setCtxPath(request.getContextPath());
//                TableBuilder tableBldr= new RowViewTableBuilder(facade);
//                InsightTableHeaderDisplay displayHelper=new InsightTableHeaderDisplay(tableBldr);
//                StringBuilder str=displayHelper.generateOutputHTML();
                StringBuilder str = templatebd.generateInsightHtmlData(container, reportId, userId, action);
                String date = "";
                str.append("<input type='hidden' id='timeType' name='timeType' value='" + container.getReportCollect().timeDetailsArray.get(1) + "'>");
                if (String.valueOf(container.getReportCollect().timeDetailsArray.get(1)).equalsIgnoreCase("PRG_STD")) {

                    date = String.valueOf(container.getReportCollect().timeDetailsArray.get(2));
                    String pickerDate = "";
                    pickerDate = date.substring(3, 5) + "/" + date.substring(0, 2) + "/" + date.substring(6);
                    //
                    str.append("<input type='hidden' id='toDate' name='toDate' value='" + pickerDate + "'>");
                    str.append("<input type='hidden' id='Duration' name='Duration' value='" + container.getReportCollect().timeDetailsArray.get(3) + "'>");
                    str.append("<input type='hidden' id='compare' name='compare' value='" + container.getReportCollect().timeDetailsArray.get(4) + "'>");
                } else if (String.valueOf(container.getReportCollect().timeDetailsArray.get(1)).equalsIgnoreCase("PRG_DATE_RANGE")) {
                    String toDate, frmDate, cmpToDate, cmpFrmDate;
                    date = String.valueOf(container.getReportCollect().timeDetailsArray.get(2));
                    toDate = date.substring(3, 5) + "/" + date.substring(0, 2) + "/" + date.substring(6);
                    date = String.valueOf(container.getReportCollect().timeDetailsArray.get(3));
                    frmDate = date.substring(3, 5) + "/" + date.substring(0, 2) + "/" + date.substring(6);
                    date = String.valueOf(container.getReportCollect().timeDetailsArray.get(4));
                    cmpToDate = date.substring(3, 5) + "/" + date.substring(0, 2) + "/" + date.substring(6);
                    date = String.valueOf(container.getReportCollect().timeDetailsArray.get(5));
                    cmpFrmDate = date.substring(3, 5) + "/" + date.substring(0, 2) + "/" + date.substring(6);
                    str.append("<input type='hidden' id='toDate' name='toDate' value='" + toDate + "'>");
                    str.append("<input type='hidden' id='fromDate' name='fromDate' value='" + frmDate + "'>");
                    str.append("<input type='hidden' id='compareToDate' name='compareToDate' value='" + cmpToDate + "'>");
                    str.append("<input type='hidden' id='compareFrmDate' name='compareFrmDate' value='" + cmpFrmDate + "'>");
                }
                response.getWriter().print(str);
            }
            //  return mapping.findForward("insightworkBench");
        } else {
            return mapping.findForward("sessionExpired");
        }
        return null;

    }

    public ActionForward changeParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        HttpSession session = request.getSession(false);
        String paramIds = null;
        String paramNames = null;


        String[] parameterIds = null;
        String[] parameterNames = null;//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
        String[] timeparameterIds = null;

        ArrayList Parameters = new ArrayList();
        ArrayList ParametersNames = new ArrayList();//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
        ArrayList timeParameters = new ArrayList();

        String customReportId = null;
        HashMap map = null;
        Container container = null;
        if (session != null) {

            customReportId = request.getParameter("REPORTID");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);

            PbReportCollection collect = container.getReportCollect();

            paramIds = request.getParameter("params");
            paramNames = request.getParameter("paramNames");//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com

            //update Param hashmap in session
            if (paramIds != null && !"".equalsIgnoreCase(paramIds)) {
                parameterIds = paramIds.split(",");
                parameterNames = paramNames.split(",");
                collect.resetAllParameters();
                ArrayList<String> defaultValues = new ArrayList<String>();
                for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                    Parameters.add(parameterIds[paramCount]);
                    ParametersNames.add(parameterNames[paramCount]);//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
                    collect.setParameters(parameterIds[paramCount], parameterNames[paramCount], defaultValues);
                    container.removeParameterSecurity(parameterIds[paramCount]);
                }
            }
            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("Parameters", Parameters);
            ParametersHashMap.put("ParametersNames", ParametersNames);
            collect.reportParamIds = Parameters;
            collect.reportParamNames = ParametersNames;


            ArrayList<String> colViewByLst = new ArrayList<String>();
            ArrayList<String> colViewNamesLst = new ArrayList<String>();
            ArrayList<String> rowViewByLst = new ArrayList<String>();
            ArrayList<String> rowViewNamesLst = new ArrayList<String>();
            HashMap TableHashMap = null;
            session.removeAttribute("colViewByLst");
            session.removeAttribute("rowViewByLst");
            rowViewByLst.add(String.valueOf(Parameters.get(0)));
            collect.setRowViewBys(rowViewByLst);
            rowViewNamesLst.add(String.valueOf(ParametersNames.get(0)));
            session.setAttribute("colViewByLst", colViewByLst);
            session.setAttribute("rowViewByLst", rowViewByLst);
            session.setAttribute("rowViewIdList", rowViewByLst);
            session.setAttribute("colViewIdList", colViewByLst);
            session.setAttribute("rowNamesLst", rowViewNamesLst);
            session.setAttribute("colNamesLst", colViewNamesLst);
            container.setColumnViewList(colViewByLst);
            container.setRowViewList(rowViewByLst);
            TableHashMap = container.getTableHashMap();
            TableHashMap.put("REP", rowViewByLst);
            TableHashMap.put("REPNames", rowViewNamesLst);
            TableHashMap.put("CEP", colViewByLst);
            container.setTableHashMap(TableHashMap);




            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward saveInsightWorkBench(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String reportId = request.getParameter("REPORTID");
        String roleId = request.getParameter("roleId");
        HttpSession session = request.getSession(false);
        HashMap map = null;
        Container container = null;
        ReportTemplateBD bd = new ReportTemplateBD();
        PbReportViewerDAO dao = new PbReportViewerDAO();
        PbReportViewerBD viewerbd = new PbReportViewerBD();
        String userId = String.valueOf(session.getAttribute("USERID"));
        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(reportId);
            container.setReportType("I");
            String localPath = (String) session.getAttribute("reportAdvHtmlFileProps");
//             
            String filePath = localPath + "/InsightFile_" + reportId + ".txt";
            String newInsightId = dao.savingInsightDetails(reportId, container.getReportName(), filePath, roleId, userId);
            container.setTableId(newInsightId);
            container.setReportId(newInsightId);
            bd.writeBackUpFile(container, filePath);
            // viewerbd.prepareReport("open", container, reportId, userId,null);
            response.getWriter().print(newInsightId);
        } else {
            return mapping.findForward("sessionExpired");
        }
        return null;
    }

    public ActionForward deleteInsightWorkBench(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String insightId = request.getParameter("insightId");
        String userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
        PbReportViewerDAO dao = new PbReportViewerDAO();
        dao.deleteInsightWorkBench(insightId, userId);
        return null;
    }

    public ActionForward getParameterdrill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        HttpSession session = request.getSession(false);
        Container container = null;
        String reportId = request.getParameter("reportId");
        String UserId = String.valueOf(session.getAttribute("USERID"));
        HashMap map = null;
        map = (HashMap) session.getAttribute("PROGENTABLES");
        container = (Container) map.get(reportId);
        PrintWriter out = response.getWriter();
        String FinalHtml = "";
        PbReportViewerBD KPIDashboardBD = new PbReportViewerBD();
        FinalHtml = container.getParamSectionDisplay();
        out.print(FinalHtml);
        return null;
    }

    public ActionForward readExcelFileDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        String reportid = request.getParameter("reportid");
        Container container = null;
        String[] colNameArr = request.getParameterValues("excelcolName");
        String[] dispColNameArr = request.getParameterValues("dispColName");
        String[] typeArr = request.getParameterValues("Type");
        String[] mappingDim = request.getParameterValues("MappingDim");
        String[] aggregation = request.getParameterValues("Aggregation");
        String[] dataType = request.getParameterValues("DataType");
        container = Container.getContainerFromSession(request, reportid);
        ImportExcelDetail importdetail = null;
        if (container.importExcelDetails != null) {
            importdetail = container.importExcelDetails;
        } else {
            importdetail = new ImportExcelDetail();
            container.importExcelDetails = importdetail;
        }
        for (int i = 0; i < colNameArr.length; i++) {
            HashMap<String, String> rowmap = new HashMap<String, String>();
            rowmap.put("ColName", colNameArr[i]);
            rowmap.put("DispColName", dispColNameArr[i]);
            rowmap.put("Type", typeArr[i]);
            rowmap.put("Mapping", mappingDim[i]);
            if (typeArr[i].equalsIgnoreCase("D")) {
                importdetail.getExcelViewbys().add(colNameArr[i]);
                if (importdetail.getRepToExcelMapping() != null && !importdetail.getRepToExcelMapping().containsKey(mappingDim[i])) {
                    importdetail.getRepToExcelMapping().put(mappingDim[i], colNameArr[i]);
                }

            } else {
                importdetail.getExcelMeasures().add(colNameArr[i]);
                importdetail.getExcelMeasureLables().add(dispColNameArr[i]);
            }
            rowmap.put("Aggregation", aggregation[i]);
            rowmap.put("DataType", dataType[i]);
//         importdetail.setExcelColsMap(rowmap);
            importdetail.setFinalExcelColNameMap(colNameArr[i], rowmap);
            importdetail.getExcelColumnNames().put(colNameArr[i], dispColNameArr[i]);
            if (typeArr[i].equalsIgnoreCase("D")) {
                importdetail.getExcelDimMapping().put(colNameArr[i], mappingDim[i]);
            } else {
                importdetail.getExcelDimMapping().put(colNameArr[i], null);
            }
        }
        new ReportTemplateBD().generateExcelreturnObject(reportid, container, new File(container.getExcelFilePath()));
        return null;
    }
    //Added by Amar

    public ActionForward addMoreFilters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
//        String foldersIds = "";
        String PbUserId = "";
//        String result = "";
//       String IsrepAdhoc1;

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            ArrayList<String> reportParamIds = new ArrayList<String>();
//            String reportParamIdsVal = "0";
            StringBuilder reportParamIdsVal = new StringBuilder(300);
            reportParamIdsVal.append("0");

            String reportId = request.getParameter("REPORTID");
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                if (container != null) {
                    reportParamIds = (ArrayList) container.getParametersHashMap().get("Parameters");
                    if (reportParamIds != null && reportParamIds.size() > 0) {
                        for (int i = 0; i < reportParamIds.size(); i++) {
//                        reportParamIdsVal = reportParamIdsVal +","+reportParamIds.get(i);
                            reportParamIdsVal.append(",").append(reportParamIds.get(i));
                        }
                    }
                }
            }

//          IsrepAdhoc1=(request.getParameter("IsrepAdhoc"));
//          request.setAttribute("IsrepAdhoc1", IsrepAdhoc1);
            customReportId = request.getParameter("REPORTID");
//            foldersIds = request.getParameter("foldersIds");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            //changed by Nazneen for getting dims which r not added to rep
//            result = reportTemplateDAO.getUserDimDetails(foldersIds, PbUserId, reportParamIdsVal);
            // result = reportTemplateDAO.getUserDimDetailsForRep(foldersIds, PbUserId, reportParamIdsVal);
            //request.setAttribute("dims",result);
            //request.setAttribute("roleId",foldersIds);
            request.setAttribute("USERID", PbUserId);
            request.setAttribute("reportId", customReportId);
//            if(request.getParameter("isOneview")!=null && request.getParameter("isOneview").equalsIgnoreCase("True")){
//            request.setAttribute("isOneview","true");
//            }

            return mapping.findForward("addflters");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward trendAnalysisAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String PbUserId = "";
        String reportId = "";
//       String JsonList = "";
        Container container = null;
        HashMap map = new HashMap();
//         ArrayList rowviewBy = new ArrayList();
//         ArrayList rowviewId = new ArrayList();
        String[] rowviewBy = new String[1];
        String[] rowviewId = new String[1];
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        if (request.getParameter("reportId") != null) {
            reportId = request.getParameter("reportId");
        }
        if (session.getAttribute("PROGENTABLES") != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            container = (Container) map.get(reportId);
            if (container != null) {
                ArrayList nameListIds = (ArrayList) (container.getTableHashMap().get("Measures"));

                rowviewBy[0] = "TIME";
                rowviewId[0] = "TIME";

            }
            String JsonList = ReportTemplateDAO.trendAnalysisAction(reportId, container, PbUserId, rowviewBy, rowviewId);
            response.getWriter().print(JsonList);
        } else {
            return mapping.findForward("sessionExpired");
        }

        return null;
    }
//added by mohit
    // modified by krishan pratap

    public ActionForward SaveDefaultTab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        String reportid = request.getParameter("reportId");
        String defaulttab = request.getParameter("defaulttab");
        String showIcon = request.getParameter("show_id");
        int i = ReportTemplateDAO.SaveDefaultTab(reportid, defaulttab, showIcon);

        out.print(i);
        return null;
    }

    public ActionForward GetDefaultTab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        String reportid = request.getParameter("reportId");
//         String reportTabs=request.getParameter("reportTabs");
        String reportTabs = ReportTemplateDAO.GetDefaultTab(reportid);

// else{
//      return mapping.findForward("sessionExpired");
// }
        out.print(reportTabs);
        return null;
    }

// Added By prabal
    public ActionForward GetAllfavReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        PrintWriter out = response.getWriter();
        String reportTabs = null;
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        try {
            String favouriteVar = request.getParameter("favouriteVar");
            if (favouriteVar != null) {
                reportTabs = ReportTemplateDAO.GetAllfavReportsLandingPage(userId);
            } else {
                reportTabs = ReportTemplateDAO.GetAllfavReports(userId);
            }
        } catch (NullPointerException e) {
        }
        out.print(reportTabs);
        return null;
    }

    public ActionForward GetAllDB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        String userId = String.valueOf(session.getAttribute("USERID"));
        PrintWriter out = response.getWriter();
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        String reportTabs = ReportTemplateDAO.GetAllDB(userId);

        out.print(reportTabs);
        return null;
    }

    public ActionForward GetAllOneView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        String userId = String.valueOf(session.getAttribute("USERID"));
        PrintWriter out = response.getWriter();
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        String reportTabs = ReportTemplateDAO.GetAllOneView(userId);
        out.print(reportTabs);
        return null;
    }

    public ActionForward ParseNewdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        HttpSession session = request.getSession(false);

        String date = request.getParameter("newdate");
        PrintWriter out = response.getWriter();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date parse = dateFormat.parse(date);
        String format = dateFormat1.format(parse);
        out.print(format);
        return null;
    }

    public ActionForward saveResetGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        String reportid = request.getParameter("reportId");
        String graphGO = request.getParameter("graphGO");
        String graphRefresh = request.getParameter("graphRefresh");
        int i = ReportTemplateDAO.saveResetGraph(reportid, graphGO, graphRefresh);

        out.print(i);
        return null;
    }

    public ActionForward getResetGraph(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        String reportid = request.getParameter("reportId");
//         String reportTabs=request.getParameter("reportTabs");
        String list = ReportTemplateDAO.getResetGraph(reportid);

// else{
//      return mapping.findForward("sessionExpired");
// }
        out.print(list);
        return null;
    }
    //added by krishan 

    public ActionForward getMeasures2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String foldersIds = "";
        String result = "";
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";

        ArrayList<String> Parameters = null;
        ArrayList<String> Parameters1 = null;

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);
            String tableList = request.getParameter("tableList");
            String aoid = request.getParameter("aoid");
            String createAO = request.getParameter("createAO");
            Parameters = new ArrayList<String>();
            Parameters1 = new ArrayList<String>();

            for (String parameter : container.getParameterElements()) {
                Parameters.add(parameter);
            }

            //////.println("dimFactRel is : "+dimFactRel);

            foldersIds = request.getParameter("foldersIds");
            result = "";
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            if (aoid != null && !aoid.equalsIgnoreCase("")) {
                result = repdao.getMeasurescustomForA0(aoid, Parameters, request.getContextPath());
            } else {
                if (container.getTableList() != null && !container.getTableList().isEmpty()) {
                    ArrayList alist = new ArrayList();
                    result = repdao.getMeasuresForReportCustom(foldersIds, Parameters, request.getContextPath(), container.getTableList(), createAO);
                    container.setTableList(alist);
                } else if (tableList != null && tableList.equalsIgnoreCase("true")) {
                    result = reportTemplateDAO.getMeasurescustom(foldersIds, Parameters, request.getContextPath(), createAO);
                }
            }

            request.setAttribute("Measures", result);
            //  Gson gson = new Gson();
            //String reportdetails = gson.toJson(result);
            //     
            response.getWriter().print(result);
            return null;


            //    return mapping.findForward("measures");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward goToCreateReportDesigner1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = null;
        String reportId = null;
        Container container = null;
        String reportName = null;
        String reportDesc = null;
        String editReportName = null;
        String PbUserId = "";
        String repId = null;
        String result = "";
        String url = request.getRequestURL().toString();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();
        //   HashMap map = null;
        String dimId = null;
        String timeparams = null;
        String[] timeparameterIds = null;
        String customReportId = null;
        //  Container container = null;

        ArrayList timeParameters = new ArrayList();
        ArrayList<String> timeColTypes = new ArrayList<String>();
        HashMap<String, ArrayList<String>> timeDimMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeDetails = new ArrayList<String>();
        QueryTimeHelper timeHelper = new QueryTimeHelper();

        if (session != null) {
            try {
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(reportId) != null) {
                        container = (Container) map.get(reportId);
                        container = new Container();
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }

                customReportId = request.getParameter("REPORTID");
                dimId = request.getParameter("dimId");
                timeparams = request.getParameter("timeparams");
                //  map = (HashMap) session.getAttribute("PROGENTABLES");
                //  container = (Container) map.get(customReportId);
//              PbReportCollection collect = new PbReportCollection() ;

                // 
                // 
                if (timeparams != null) {
                    timeparameterIds = timeparams.split(",");
                    for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                        timeParameters.add(timeparameterIds[timeparamCount]);
                    }
                }
                for (Object timeParameter : timeParameters) {
                    timeColTypes.add((String) timeParameter);
                }
                for (int time = 0; time < timeParameters.size(); time++) {
                    timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_DAY_LEVEL, timeColTypes);
                    timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_DAY_LEVEL);
                }
                PbReportCollection collect = container.getReportCollect();
                if (collect == null) {
                    collect = new PbReportCollection();
                }
                collect.timeDetailsArray = timeDetails;
                collect.timeDetailsMap = timeDimMap;
                //

                reportName = request.getParameter("reportName");
                reportName = reportName.replace('^', '&').replace('~', '+').replace('`', '#');
                reportName = reportName.replace("'", "''");
                reportDesc = request.getParameter("reportDesc");
                reportDesc = reportDesc.replace('^', '&').replace('~', '+').replace('`', '#');
                reportDesc = reportDesc.replace("'", "''");
                if (request.getParameter("editRepName") != null) {

                    repId = request.getParameter("repId");
                    editReportName = request.getParameter("editRepName");
                }
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                url = url + "?templateParam=goToReportDesigner&reportName=" + reportName + "&reportDesc=" + reportDesc;
                request.setAttribute("reportdesignerurl", url);
                reportId = reportViewerDAO.getCustomReportId();



                // PbReportCollection collect = container.getReportCollect();

                container.setReportCollect(collect);
                ////.println(container.getReportId());
                container.setReportId(reportId);
                container.setTableId(reportId);
                container.setNetTotalReq(true);
                container.setGrandTotalReq(true);

                result = "<table><tr><td><ul>";
                result += reportTemplateDAO.getUserFoldersByUserIdInDesigner(PbUserId);
                result += "</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"creationOfReport1(" + reportId + ")\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>";


                request.setAttribute("ReportId", reportId);
                request.setAttribute("UserFlds", result);

                container.setReportName(reportName);
                container.setReportDesc(reportDesc);

                //PbReportCollection collect = new PbReportCollection();
                //collect.reportId = reportId;
                //container.setReportCollect(collect);
                container.setReportId(reportId);

                container.setSessionContext(session, container);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            if (editReportName == null) {
                PrintWriter out = response.getWriter();
                out.print(result);
                return null;
//                return mapping.findForward(SUCCESS);
            } else {

                reportTemplateDAO.editReportName(repId, reportName, reportDesc);
                return null;
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward goToCreateReportDesigner2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = null;
        String reportId = null;
        Container container = null;
        String reportName = null;
        String reportDesc = null;
        String editReportName = null;
        String PbUserId = "";
        String repId = null;
        String result = "";
        String url = request.getRequestURL().toString();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();
        //   HashMap map = null;
        String dimId = null;
        String timeparams = null;
        String[] timeparameterIds = null;
        String customReportId = null;
        //  Container container = null;

        ArrayList timeParameters = new ArrayList();
        ArrayList<String> timeColTypes = new ArrayList<String>();
        HashMap<String, ArrayList<String>> timeDimMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeDetails = new ArrayList<String>();
        QueryTimeHelper timeHelper = new QueryTimeHelper();

        if (session != null) {
            try {
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(reportId) != null) {
                        container = (Container) map.get(reportId);
                        container = new Container();
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }

                customReportId = request.getParameter("REPORTID");
                dimId = request.getParameter("dimId");
                timeparams = request.getParameter("timeparams");
                //  map = (HashMap) session.getAttribute("PROGENTABLES");
                //  container = (Container) map.get(customReportId);
//              PbReportCollection collect = new PbReportCollection() ;

                // 
                //  
                if (timeparams != null) {
                    timeparameterIds = timeparams.split(",");
                    for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                        timeParameters.add(timeparameterIds[timeparamCount]);
                    }
                }
                for (Object timeParameter : timeParameters) {
                    timeColTypes.add((String) timeParameter);
                }
                for (int time = 0; time < timeParameters.size(); time++) {
                    timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_DAY_LEVEL, timeColTypes);
                    timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_DAY_LEVEL);
                }
                PbReportCollection collect = container.getReportCollect();
                if (collect == null) {
                    collect = new PbReportCollection();
                }
                collect.timeDetailsArray = timeDetails;
                collect.timeDetailsMap = timeDimMap;

                reportName = request.getParameter("reportName");
//                reportName = reportName.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
                 reportName = reportName.replace('^', '&').replace('~', '+').replace('`', '#');
                reportName = reportName.replace("'", "''");
                reportDesc = request.getParameter("reportDesc");
//                reportDesc = reportDesc.replace('^', '&').replace('~', '+').replace('`', '#').replace('_', '%');
                 reportDesc = reportDesc.replace('^', '&').replace('~', '+').replace('`', '#');
                reportDesc = reportDesc.replace("'", "''");
                if (request.getParameter("editRepName") != null) {

                    repId = request.getParameter("repId");
                    editReportName = request.getParameter("editRepName");
                }
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                url = url + "?templateParam=goToReportDesigner&reportName=" + reportName + "&reportDesc=" + reportDesc;
                request.setAttribute("reportdesignerurl", url);
                reportId = reportViewerDAO.getCustomReportId();



                // PbReportCollection collect = container.getReportCollect();

                container.setReportCollect(collect);
                ////.println(container.getReportId());
                container.setReportId(reportId);
                container.setTableId(reportId);
                container.setNetTotalReq(true);
                container.setGrandTotalReq(true);

                result = "<table><tr><td><ul>";
                result += reportTemplateDAO.getUserFoldersByUserIdInDesigner(PbUserId);
                result += "</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"creationOfReport2(" + reportId + ")\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>";


                request.setAttribute("ReportId", reportId);
                request.setAttribute("UserFlds", result);

                container.setReportName(reportName);
                container.setReportDesc(reportDesc);

                //PbReportCollection collect = new PbReportCollection();
                //collect.reportId = reportId;
                //container.setReportCollect(collect);
                container.setReportId(reportId);

                container.setSessionContext(session, container);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            if (editReportName == null) {
                PrintWriter out = response.getWriter();
                out.print(result);
                return null;
//                return mapping.findForward(SUCCESS);
            } else {

                reportTemplateDAO.editReportName(repId, reportName, reportDesc);
                return null;
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward selectRoleGoToDesin1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String repId = request.getParameter("repId");
        String roleId = request.getParameter("roleId");
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;

        request.setAttribute("ReportId", repId);
        request.setAttribute("fromDesigner", "true");
        request.setAttribute("roleid", roleId);

        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(repId);


            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", roleId);
//            result = reportTemplateDAO.getUserDims(foldersIds, PbUserId);
//            PrintWriter out = response.getWriter();
//            out.print(result);
            container.setParametersHashMap(ParametersHashMap);
            String[] strArray = new String[]{roleId};
            PbReportCollection collection = container.getReportCollect();
            collection.setReportBizRole(Integer.parseInt(roleId));
            container.setFlagg(true);
            container.setDefault_tab("Key" + repId, "table");
            return mapping.findForward("reportDesigner1");
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward selectRoleGoToDesin2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String repId = request.getParameter("repId");
        String roleId = request.getParameter("roleId");
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;

        request.setAttribute("ReportId", repId);
        request.setAttribute("fromDesigner", "true");
        request.setAttribute("roleid", roleId);

        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(repId);


            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", roleId);
//            result = reportTemplateDAO.getUserDims(foldersIds, PbUserId);
//            PrintWriter out = response.getWriter();
//            out.print(result);
            container.setParametersHashMap(ParametersHashMap);
            String[] strArray = new String[]{roleId};
            PbReportCollection collection = container.getReportCollect();
            collection.setReportBizRole(Integer.parseInt(roleId));
            container.setFlagg(true);
            container.setDefault_tab("Key" + repId, "table");
            return mapping.findForward("reportDesigner2");
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward buildParams1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        // 
        HttpSession session = request.getSession(false);
        HashMap map = null;
        String dimId = null;
        String timeparams = null;
        String[] timeparameterIds = null;
        String customReportId = null;
        Container container = null;

        ArrayList timeParameters = new ArrayList();
        ArrayList<String> timeColTypes = new ArrayList<String>();
        HashMap<String, ArrayList<String>> timeDimMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeDetails = new ArrayList<String>();
        QueryTimeHelper timeHelper = new QueryTimeHelper();

        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            dimId = request.getParameter("dimId");
            timeparams = request.getParameter("timeparams");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);
//              PbReportCollection collect = new PbReportCollection() ;

            // 
            //  
            if (timeparams != null) {
                timeparameterIds = timeparams.split(",");
                for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                    timeParameters.add(timeparameterIds[timeparamCount]);
                }
            }
            for (Object timeParameter : timeParameters) {
                timeColTypes.add((String) timeParameter);
            }
            for (int time = 0; time < timeParameters.size(); time++) {
                timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_DAY_LEVEL, timeColTypes);
                timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_DAY_LEVEL);
            }
            PbReportCollection collect = container.getReportCollect();
            collect.timeDetailsArray = timeDetails;
            collect.timeDetailsMap = timeDimMap;
            //    
            container.setReportCollect(collect);

        } else {
            return null;
        }

        return mapping.findForward("sessionExpired");

    }

    public ActionForward addMoreDimensions1(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String PbUserId = "";
        String result = "";
        String IsrepAdhoc1;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            ArrayList<String> reportParamIds = new ArrayList<String>();
//            String reportParamIdsVal = "0";
            StringBuilder reportParamIdsVal = new StringBuilder(100);
            reportParamIdsVal.append("0");

            String reportId = request.getParameter("REPORTID");
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                if (container != null) {
                    reportParamIds = (ArrayList) container.getParametersHashMap().get("Parameters");
                    if (reportParamIds != null && reportParamIds.size() > 0) {
                        for (int i = 0; i < reportParamIds.size(); i++) {
//                        reportParamIdsVal = reportParamIdsVal +","+reportParamIds.get(i);
                            reportParamIdsVal.append(",").append(reportParamIds.get(i));
                        }
                    }
                }
            }

            IsrepAdhoc1 = (request.getParameter("IsrepAdhoc"));
            request.setAttribute("IsrepAdhoc1", IsrepAdhoc1);
            customReportId = request.getParameter("REPORTID");
            foldersIds = request.getParameter("foldersIds");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
//          changed by Nazneen for getting dims which r not added to rep
//          result = reportTemplateDAO.getUserDimDetails(foldersIds, PbUserId, reportParamIdsVal);
            result = reportTemplateDAO.getUserDimDetailsForRepCustom(foldersIds, PbUserId, reportParamIdsVal.toString());
            request.setAttribute("dims", result);
            request.setAttribute("roleId", foldersIds);
            request.setAttribute("USERID", PbUserId);
            request.setAttribute("reportId", customReportId);
            if (request.getParameter("isOneview") != null && request.getParameter("isOneview").equalsIgnoreCase("True")) {
                request.setAttribute("isOneview", "true");
            }

            return mapping.findForward("addDims1");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward goToCreateReportTime(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        //  
        HttpSession session = request.getSession(false);
        String dimId = null;
        String timeparams = null;
        ArrayList timeParameters = new ArrayList();
        ArrayList<String> timeColTypes = new ArrayList<String>();
        HashMap<String, ArrayList<String>> timeDimMap = new HashMap<String, ArrayList<String>>();
        String[] timeparameterIds = null;
        QueryTimeHelper timeHelper = new QueryTimeHelper();
        ArrayList<String> timeDetails = new ArrayList<String>();
        String reportId = null;
        Container container = null;
        String reportName = null;
        HashMap map = null;
        if (session != null) {
            try {
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(reportId) != null) {
                        container = (Container) map.get(reportId);
                        container = new Container();
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }
                // customReportId = request.getParameter("REPORTID");
                dimId = request.getParameter("dimId");
                timeparams = request.getParameter("timeparams");
                //  map = (HashMap) session.getAttribute("PROGENTABLES");
                //  container = (Container) map.get(customReportId);
//              PbReportCollection collect = new PbReportCollection() ;

                // 
                // 
                if (timeparams != null) {
                    timeparameterIds = timeparams.split(",");
                    for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                        timeParameters.add(timeparameterIds[timeparamCount]);
                    }
                }
                for (Object timeParameter : timeParameters) {
                    timeColTypes.add((String) timeParameter);
                }
                for (int time = 0; time < timeParameters.size(); time++) {
                    timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_DAY_LEVEL, timeColTypes);
                    timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_DAY_LEVEL);
                }
                PbReportCollection collect = container.getReportCollect();
                if (collect == null) {
                    collect = new PbReportCollection();
                    collect.timeDetailsArray = timeDetails;
                    collect.timeDetailsMap = timeDimMap;
                    //
                }
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
        return null;
    }

    public ActionForward getSelectedDims(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        PbReportViewerDAO pbDao = new PbReportViewerDAO();
        List allViewIds = new ArrayList<String>();
        List allViewNames = new ArrayList<String>();
        String filterDataString = request.getParameter("filterData");
        String reportId = request.getParameter("REPORTID");
        String fromdesigner = request.getParameter("fromdesigner");
        String fileLocation = "";
        if (session != null) {
            fileLocation = pbDao.getFilePath(session);
        } else {
            fileLocation = "/usr/local/cache";
        }
        if (fromdesigner == null) {
            fromdesigner = "";
        }


        ReportObjectMeta reportMeta = new ReportObjectMeta();
        FileReadWrite readWrite = new FileReadWrite();

        String metaString = readWrite.loadJSON(fileLocation + "/analyticalobject/R_GO_" + reportId + ".json");
        Gson gson = new Gson();
        Type tarType = new TypeToken<ReportObjectMeta>() {
        }.getType();

        reportMeta = gson.fromJson(metaString, tarType);

        Type goTypedash = new TypeToken<XtendReportMeta>() {
        }.getType();

        Type filterDataType = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        Map<String, List<String>> filterData = new HashMap<String, List<String>>();
        filterData = gson.fromJson(filterDataString, filterDataType);


        FileReadWrite fileReadWrite = new FileReadWrite();
        Container container = Container.getContainerFromSession(request, reportId);
        String bizzRoleId = container.getReportCollect().reportBizRoles[0];
        String bizzRoleName = container.getReportCollect().getReportBizRoleName(bizzRoleId);
        String reportName = container.getReportName();
//        String reportName=
        XtendReportMeta xtendsReportMeta = new XtendReportMeta();
        String visualName = request.getParameter("visualName");
        String metaFilePath = "/usr/local/cache/" + bizzRoleName.replaceAll("\\W", "").trim() + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "/" + reportName.replaceAll("\\W", "").trim() + "_" + reportId + "_" + visualName + ".json";
        String gimeta = "";
        gimeta = fileReadWrite.loadJSON(metaFilePath);
        xtendsReportMeta = gson.fromJson(gimeta, goTypedash);
        List<String> viewBys = xtendsReportMeta.getViewbys();
        List<String> viewByIds = xtendsReportMeta.getViewbysIds();
        List<String> selectedViewByIds = xtendsReportMeta.getSelectedViewBys();
        List<String> selectedViewBys = new LinkedList<String>();
        List<String> dimIds = xtendsReportMeta.getSelectedViewBys();
        String currentViewBy = request.getParameter("viewBy");
        Set<String> keySet = filterData.keySet();
        List<String> data = null;
        for (String key : keySet) {
            if (currentViewBy.equals(key)) {
                data = filterData.get(key);
                break;
            }
        }
        data.remove("Levi's");
        data.remove("Q&Q");
        data.remove("D-Link");
        data.remove("TP-Link");
        for (String viewById : selectedViewByIds) {
            selectedViewBys.add(viewBys.get(viewByIds.indexOf(viewById)));
        }
        PrintWriter out = response.getWriter();
        out.print(data);
        session.setAttribute("viewByFilters", data);
        session.setAttribute("currentViewBy", currentViewBy);
        session.setAttribute("selectedViewBys", selectedViewBys);

        return null;
    }
    //added by sruthi for tablecolumn pro
public ActionForward tableColumnProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
  HttpSession session = request.getSession(false);
  String reportId = request.getParameter("reportid");
  PbReportViewerBD repViewerBD = new PbReportViewerBD();
  HashMap map = null;
  boolean measureflag=true;
   Container container = null;
    HashMap NFMap = null;
      HashMap TableProperties = null;
       HashMap ColumnProperties = null;
      HashMap TableHashMap = null;
       HashMap<String,ArrayList<String>> fontHeaderMaps= new HashMap<String,ArrayList<String>>();
  ArrayList<String> measureid = new ArrayList<String>();
  HashMap<String,ArrayList<String>> columnproperties=new  HashMap<String,ArrayList<String>>();
  ArrayList<String> arrylistnumber = new ArrayList<String>();
  ArrayList<String> arrylistrounding = new ArrayList<String>();
  ArrayList<String> measurNames = new ArrayList<String>();
  ArrayList<String> alldetailslist=new ArrayList<String>();
  HashMap<String,String> customdata=null;
     if (session != null) {
           if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    container = (Container) map.get(reportId);
                   // measureid=(ArrayList) container.getTableMeasure();
                   // measurNames=(ArrayList) container.getTableMeasureNames();
                      TableHashMap = container.getTableHashMap();
                       TableProperties = (HashMap) TableHashMap.get("TableProperties");
                        measureid=(ArrayList) TableHashMap.get("Measures");
                         measurNames=(ArrayList) TableHashMap.get("MeasuresNames");
                          fontHeaderMaps = (container.getTableColumnProperties() == null) ? new HashMap() : container.getTableColumnProperties();
                           ColumnProperties = (container.getColumnProperties() == null) ? new HashMap() : container.getColumnProperties();
                           customdata=(container.getCustomHeader()==null)?new HashMap<String,String>():container.getCustomHeader();
                     NFMap = (TableHashMap.get("NFMap") == null) ? new HashMap() : (HashMap) TableHashMap.get("NFMap");
                        String numberdecimal=request.getParameter("elementsrounding");
                        String measuralias= request.getParameter("measuralias");
                        String headerfont=request.getParameter("headerfont");
                         String arrfontsize=request.getParameter("arrfontsize");
                         String numberformat=request.getParameter("numberformat");
                         String headeralign=request.getParameter("headeralign");
                         String dataalign=request.getParameter("dataalign");
                        String colorcode=request.getParameter("colorcode");
                         String number=request.getParameter("number");
                         String customarr= request.getParameter("customarr");
                         String textcolor=request.getParameter("textcolor");
                           String bgcolor=request.getParameter("bgcolor");
                         String alldetails=request.getParameter("alldetails").replace("[", "").replace("]", "");
                         String arrnumberformat[]=numberformat.split(",");
                         for(int j=0;j<arrnumberformat.length;j++){
                             //String answer=arrnumberformat[j];
                           //   if(arrnumberformat[j].equalsIgnoreCase("Ab"))
                               //   answer="";
                              arrylistnumber.add(arrnumberformat[j]);
                         }
                          for(int j=0;j<numberdecimal.length();j++){
                              char c = numberdecimal.charAt(j);
                              String answer = Character.toString(c);
                               if(!answer.equalsIgnoreCase(","))
                              arrylistrounding.add(answer);
                         }
                         String arrmeasuralias[]=null;//=measuralias.split(",");
                        if(measuralias!=null){
                            arrmeasuralias=measuralias.split(",");
                        }
                         String arrheaderfont[]=headerfont.split(",");
                         String fontsizeval[]=arrfontsize.split(",");
                         String arrrounding[]=numberdecimal.split(",");
                         String headerarr[]=headeralign.split(",");
                         String dataarr[]=dataalign.split(",");
                        String measurecolorarr[]=colorcode.split(",");
                        String textcolorarr[]=textcolor.split(",");
                         String numberarr[]=number.split(",");
                         String customheader[]=customarr.split(",");
                          String bgcolorarr[]=bgcolor.split(",");
                        String alldetailsarr[]=null;
                         if(alldetails!=null){
                           alldetailsarr=alldetails.split(",");
                         for(int al=0;al<alldetailsarr.length;al++){
                             alldetailslist.add(alldetailsarr[al]);
                         }}
                       for(int i=0;i<measureid.size();i++){
                             ArrayList singleColProp = new ArrayList();
                             ArrayList singleColProp1 = new ArrayList();
                           if(!arrylistnumber.get(i).equalsIgnoreCase("")){
                                NFMap.put(measureid.get(i), arrylistnumber.get(i));
                           }
                           //if(!arrylistrounding.get(i).equalsIgnoreCase("0")){
                               container.setRoundPrecisionForMeasure(measureid.get(i), Integer.parseInt(arrrounding[i]));
                           //}
                           if(arrmeasuralias!=null && !arrmeasuralias[i].equalsIgnoreCase(measurNames.get(i))){
                                container.setMeasurAlis(measureflag);
                              repViewerBD.updateGlobalTableMeasureName(container, measureid.get(i), arrmeasuralias[i]);
                              container.setisrenamed(measureid.get(i), arrmeasuralias[i]);
                           }
//                           if(!headerarr[i].equalsIgnoreCase("Center")){
                                repViewerBD.updateMeasureAlign(container, measureid.get(i), measurNames.get(i), headerarr[i]);
//                           }
//                               if(!dataarr[i].equalsIgnoreCase("Center")){
                                repViewerBD.updateScriptAlign(container, measureid.get(i), measurNames.get(i), dataarr[i]);
//                           }
                                 if(!bgcolorarr[i].equalsIgnoreCase("undefined")) {
                                 repViewerBD.updateMeasureBgColor(container,measureid.get(i), measurNames.get(i), bgcolorarr[i]);
                             }
                             if(!measurecolorarr[i].equalsIgnoreCase("undefined")) {
                                 repViewerBD.updateMeasureColor(container,measureid.get(i), measurNames.get(i), measurecolorarr[i]);
                             } 
                             if(!textcolorarr[i].equalsIgnoreCase("undefined")){
                                repViewerBD.updateScriptColor(container,measureid.get(i), measurNames.get(i), textcolorarr[i]); 
                             }
                             if(!customheader[i].equalsIgnoreCase("notdefined")){
                              customdata.put(measureid.get(i),customheader[i]);
                             }
                              singleColProp1=(ArrayList) ColumnProperties.get(measureid.get(i));
                              singleColProp1.set(9,numberarr[i]);
                           singleColProp.add(fontsizeval[i].trim());
                           singleColProp.add(arrheaderfont[i].trim());
                           fontHeaderMaps.put(measureid.get(i), singleColProp);
                           ColumnProperties.put(measureid.get(i),singleColProp1);
                           //container.setCustomHeader(customdata);
                           }
                          TableHashMap.put("NFMap", NFMap);
                          TableHashMap.put("TableProperties", TableProperties);
                      container.setTableColumnProperties(fontHeaderMaps);
                      container.setColumnProperties(ColumnProperties);
                      container.setCustomHeader(customdata);
           }
           if(!alldetailslist.isEmpty() && alldetailslist!=null){
           container.setAllDetails(alldetailslist);
           }
     }
    return null;
}
//ended by sruthi
//added by sruthi for tableviewbyproperties
public ActionForward tableviewbyColumnProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
  HttpSession session = request.getSession(false);
  String reportId = request.getParameter("reportid");
   HashMap map = null;
   Container container = null;
     HashMap TableProperties = null;
       HashMap ColumnProperties = null;
      HashMap TableHashMap = null;
      PbReportViewerBD repViewerBD = new PbReportViewerBD();
      ArrayList<String> viewbyNames=new ArrayList<String>();
       HashMap<String,ArrayList<String>> fontHeaderMaps= new HashMap<String,ArrayList<String>>();
 ArrayList<String> viewbyid=new ArrayList<String>();
     if (session != null) {
          if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                    container = (Container) map.get(reportId);
                     TableHashMap = container.getTableHashMap();
                       TableProperties = (HashMap) TableHashMap.get("TableProperties");
                        fontHeaderMaps = (container.getTableColumnProperties() == null) ? new HashMap() : container.getTableColumnProperties();
                       viewbyid=(ArrayList)TableHashMap.get("REP");
                       viewbyNames=(ArrayList)TableHashMap.get("REPNames");
                       String arralias=request.getParameter("arralias");
                        String arrfontsize= request.getParameter("arrfontsize");
                        String arrHeadersize=request.getParameter("arrHeadersize");
                         String arrHeaderAlign=request.getParameter("arrHeaderAlign");
                         String arrDataAlign=request.getParameter("arrDataAlign");
                         String colorcode=request.getParameter("colorcode");
                         String textcolor=request.getParameter("textcolor");
                         String alias[]=arralias.split(",");
                         String fontsize[]=arrfontsize.split(",");
                         String Headersize[]=arrHeadersize.split(",");
                         String HeaderAlign[]=arrHeaderAlign.split(",");
                         String DataAlign[]=arrDataAlign.split(",");
                         String arrcolorcode[]=colorcode.split(",");
                         String arrtextcolor[]=textcolor.split(",");
                         for(int i=0;i<viewbyid.size();i++){
                             ArrayList singleColProp = new ArrayList();
                             if(alias!=null && !alias[i].equalsIgnoreCase(viewbyNames.get(i))){
                              repViewerBD.updateTableMeasureName(container, "A_"+viewbyid.get(i), alias[i]);
                              container.setisrenamed(viewbyid.get(i), alias[i]);
                           }
                               repViewerBD.updateviewbyalign(container, "A_"+viewbyid.get(i), viewbyNames.get(i), HeaderAlign[i]);
                               repViewerBD.updateviewbydataalign(container,"A_"+viewbyid.get(i), viewbyNames.get(i), DataAlign[i]);
                            
                               if(!arrcolorcode[i].equalsIgnoreCase("undefined")) {
                                 repViewerBD.updateMeasureColor(container, "A_"+viewbyid.get(i), viewbyNames.get(i), arrcolorcode[i]);
                             } 
                             if(!arrtextcolor[i].equalsIgnoreCase("undefined")){
                                repViewerBD.updateScriptColor(container, "A_"+viewbyid.get(i), viewbyNames.get(i), arrtextcolor[i]); 
                             }
                             singleColProp.add(fontsize[i].trim());
                           singleColProp.add(Headersize[i].trim());
                           fontHeaderMaps.put("A_"+viewbyid.get(i), singleColProp);
                         }
                           container.setTableColumnProperties(fontHeaderMaps);
          }
     }
    
    return null; 
}
//ended by sruthi
// Added By Prabal

    public void saveCurrentTabAndReportId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        HttpSession session = request.getSession(false);
        reportTemplateDAO.saveCurrentTabAndReportIdDao(request, response, session);
    }
    // Ended by Prabal
    //added By Ram 28NOv15 for unable dimensions

    public ActionForward unableLookup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dimName = request.getParameter("getval");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        String result = dao.unableLookup(dimName);
        response.getWriter().println(result);
        return null;

    }

    public ActionForward enableLookup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dimName = request.getParameter("getval");
        ReportTemplateDAO dao = new ReportTemplateDAO();
        String result = dao.enableLookup(dimName);
        response.getWriter().println(result);
        return null;

    }
    //Ended By Ram
    //added by sruthi for multicalendar
    public ActionForward getReportMultiCalendar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, SQLException {
        String ReportId = null;
        String factsdata = null;
        HttpSession session = request.getSession();
        HashMap multidatecalendar = new HashMap();
        HashMap multicalendardata = new HashMap();
        if (request.getParameter("reportId") != null) {
            ReportId = String.valueOf(request.getParameter("reportId"));
            Container container = null;
            container = Container.getContainerFromSession(request, ReportId);
            String factshtml = "";
            HashMap TableHashMap = null;
            TableHashMap = container.getTableHashMap();
//	 String elemetids="";
            StringBuilder elemetids = new StringBuilder(300);
            ArrayList<String> measList = new ArrayList<String>();
            ArrayList<String> measureid = new ArrayList<String>();
            ArrayList<String> daydenomlist = new ArrayList<String>();
            measureid = (ArrayList) container.getTableMeasure();
            measList = (ArrayList) TableHashMap.get("Measures");
            String ctxpath = String.valueOf(request.getParameter("ctxPath"));
            if (measList != null && !measList.isEmpty()) {
                for (int i = 0; i < measList.size(); i++) {
//            elemetids=elemetids+","+measList.get(i).replace("A_","");
                    elemetids.append(",").append(measList.get(i).replace("A_", ""));
                }
            }
            if (measureid != null && !measureid.isEmpty()) {
                elemetids = new StringBuilder("");
                for (int j = 0; j < measureid.size(); j++) {
                    elemetids.append(",").append(measureid.get(j).replace("A_", ""));
//              elemetids=elemetids+","+measureid.get(j).replace("A_","");
                }
            }
//	if (!(elemetids.equalsIgnoreCase(""))) {
//            elemetids = elemetids.substring(1);
//        }
            if (elemetids.length() > 0) {
//            elemetids = elemetids.substring(1);
                elemetids = new StringBuilder(elemetids.substring(1));
            }
            PbDb pbdbfact = new PbDb();
            ArrayList<String> factdetails = new ArrayList<String>();
//	ArrayList<String> factdatedetails=new ArrayList<String>();
            ArrayList<String> tableid = new ArrayList<String>();
//        HashMap factshashmap=new HashMap();
            String query = "select distinct buss_table_id, DISP_NAME from prg_user_all_info_details where DISP_NAME !=' ' and DISP_NAME is not null and element_id in (" + elemetids + ")";
            PbReturnObject roleobj1 = pbdbfact.execSelectSQL(query);
            if (roleobj1.getRowCount() != 0) {
                for (int k = 0; k < roleobj1.getRowCount(); k++) {
                    String busstableid = roleobj1.getFieldValueString(k, 0);
                    String factNames = roleobj1.getFieldValueString(k, 1);
                    // factshashmap.put(elemetids)
                    if (factdetails == null || factdetails.isEmpty()) {
                        factdetails.add(factNames);
                        tableid.add(busstableid);
                    } else {
                        if (!factdetails.contains(factNames)) {
                            factdetails.add(factNames);
                            tableid.add(busstableid);
                        }
                    }
                }
            }
            HashMap<String, ArrayList<String>> factshashmaps = new HashMap<String, ArrayList<String>>();
            // ArrayList<String> factviews=new ArrayList<String>();
            // ArrayList<String> busscolumnid=new ArrayList<String>();
            ArrayList<String> busscoloname = new ArrayList<String>();
            ArrayList<String> usercolumndesc = new ArrayList<String>();
            ArrayList<String> usercolumntype = new ArrayList<String>();
            ArrayList<String> usercolumnname = new ArrayList<String>();
            ArrayList<String> calenderlist = new ArrayList<String>();
            for (int f = 0; f < factdetails.size(); f++) {
                String queryfact = "select BUSS_COL_ID,BUSS_COL_NAME,USER_COL_DESC,USER_COL_TYPE,USER_COL_NAME from prg_user_all_info_details where DISP_NAME ='" + factdetails.get(f) + "' and USER_COL_TYPE in('datetime','date','DATETIME','DATE')";
                PbReturnObject roleobfact = pbdbfact.execSelectSQL(queryfact);
                ArrayList<String> factviews = new ArrayList<String>();
                ArrayList<String> busscolumnid = new ArrayList<String>();
                if (roleobfact.getRowCount() != 0) {
                    for (int m = 0; m < roleobfact.getRowCount(); m++) {
                        String busscolid = roleobfact.getFieldValueString(m, 0);
                        String busscolname = roleobfact.getFieldValueString(m, 1);
                        String usercoldec = roleobfact.getFieldValueString(m, 2);
                        String usercoltype = roleobfact.getFieldValueString(m, 3);
                        String usercolname = roleobfact.getFieldValueString(m, 4);
                        factviews.add(usercoldec);
                        busscolumnid.add(busscolid);
                        busscoloname.add(busscolname);
                        usercolumndesc.add(usercoldec);
                        usercolumntype.add(usercoltype);
                        usercolumnname.add(usercolname);
                    }
                    factshashmaps.put(factdetails.get(f), factviews);
                }
//factshashmaps.put(factdetails.get(f),factviews);
            }
            try {
                String otherssql = "select CALENDER_ID,CALENDER_NAME from PRG_CALENDER_SETUP ";
                PbReturnObject daydenom = null;
                PbDb pbdb1 = new PbDb();
                daydenom = pbdb1.execSelectSQL(otherssql);
                for (int dq = 0; dq < daydenom.getRowCount(); dq++) {
                    String calenderid = daydenom.getFieldValueString(dq, 0);
                    String calnedername = daydenom.getFieldValueString(dq, 1);
                    daydenomlist.add(calnedername);
                    calenderlist.add(calenderid);
                }
            } catch (Exception ex) {
            }
            ArrayList<String> datetimedata = new ArrayList<String>();
            ArrayList<String> factlist = new ArrayList<String>();
            ArrayList<String> truncatelist = new ArrayList<String>();
            ArrayList<String> selectedtypelist = new ArrayList<String>();
            HashMap selectedColumns = new HashMap();
            String calendar = "";
//     HashMap  multicalendar = new HashMap();
            ArrayList<String> tableids = new ArrayList<String>();
            HashMap<String, ArrayList<String>> setmulticalendardata = new HashMap<String, ArrayList<String>>();
            setmulticalendardata = container.getMultiCalendarHashMap();
            if (setmulticalendardata != null && !setmulticalendardata.isEmpty()) {
                // setmulticalendardata=  (HashMap<String,ArrayList<String>>) multicalendar.get(ReportId);
                for (String key : setmulticalendardata.keySet()) {
                    tableids.add(key);
                }
                for (int j = 0; j < tableids.size(); j++) {
                    ArrayList<String> listofdata = new ArrayList<String>();
                    listofdata = setmulticalendardata.get(tableids.get(j));

                    String factname = listofdata.get(0);
                    String selectedtype = listofdata.get(2);
                    //added by Mohit for remove repetations
                    selectedColumns.put(factname, selectedtype);
                    String truncate = listofdata.get(6);
                    calendar = listofdata.get(5);
                    factlist.add(factname);
                    selectedtypelist.add(selectedtype);
                    truncatelist.add(truncate);
                }
            }
            factshtml += "<form>";
                   //added by Mohit for AO
            if((container.getReportCollect().AOId!=null && !container.getReportCollect().AOId.equalsIgnoreCase("")) )
                {
            
                    factshtml += "<table style=\"display:none;\">";
                }else
            {
                factshtml += "<table>";
            }
                        
                         factshtml +="<tr><th style=\"width:100px;\" align=\"left\">Fact Name</th><th style=\"width:100px;\" align=\"left\" >Custom Date</th><th style=\"width:100px;\" align=\"left\" >Truncate</th></tr>";
            for (int k = 0; k < factdetails.size(); k++) {
                if (factlist != null && !factlist.isEmpty()) {
                    if (factlist.contains(factdetails.get(k))) {
                        factshtml += "<tr><td ><input type=\"checkbox\" checked name=\"parameters\" id=\"factsdetails_" + factdetails.get(k) + "\" value='" + factdetails.get(k) + "' >" + factdetails.get(k) + "</td>";
                    } else {
                        factshtml += "<tr><td><input type=\"checkbox\" name=\"parameters\" id=\"factsdetails_" + factdetails.get(k) + "\" value='" + factdetails.get(k) + "' >" + factdetails.get(k) + "</td>";
                    }
                } else {
                  
                     if((container.getReportCollect().AOId!=null && !container.getReportCollect().AOId.equalsIgnoreCase("")) )
                   {
                      if(k==0)
                      {
                      factshtml += "<tr><td><input type=\"checkbox\" checked name=\"parameters\" id=\"factsdetails_" + factdetails.get(k) + "\" value='" + factdetails.get(k) + "' >" + factdetails.get(k) + "</td>";
                      }
                      else
                      {
                    factshtml += "<tr><td><input type=\"checkbox\" name=\"parameters\" id=\"factsdetails_" + factdetails.get(k) + "\" value='" + factdetails.get(k) + "' >" + factdetails.get(k) + "</td>";
           
                }
                   }else
                   {
                       factshtml += "<tr><td><input type=\"checkbox\" name=\"parameters\" id=\"factsdetails_" + factdetails.get(k) + "\" value='" + factdetails.get(k) + "' >" + factdetails.get(k) + "</td>";

                   }
              }
                // ArrayList<String> datetimedata=new ArrayList<String>();
                datetimedata = factshashmaps.get(factdetails.get(k));
                String get = selectedColumns.get(factdetails.get(k)) + "";
                int j = 0;
                factshtml += "<td><select name=factsdatatime id=factsdatatime_" + factdetails.get(k) + ">";
                if (selectedtypelist != null && !selectedtypelist.isEmpty()) {
                    for (int st = 0; st < datetimedata.size(); st++) {
                        if (!get.equalsIgnoreCase("") && get.equalsIgnoreCase(datetimedata.get(st))) {
                            factshtml += " <option selected value='" + datetimedata.get(st) + "'>" + datetimedata.get(st) + "</option>";
                        } else {
                            factshtml += " <option value='" + datetimedata.get(st) + "'>" + datetimedata.get(st) + "</option>";
                        }
                    }

                } else {
                    for (; j < datetimedata.size(); j++) {
                        factshtml += " <option value='" + datetimedata.get(j) + "'>" + datetimedata.get(j) + "</option>";
                    }
                }
                factshtml += "</select></td>&nbsp;&nbsp";
                if (truncatelist != null && !truncatelist.isEmpty()) {
                    if (factlist.contains(factdetails.get(k))) {
                        if (truncatelist.contains("Yes")) {
                            factshtml += "<td><lable><input type=\"radio\" name=\"radiono_" + factdetails.get(k) + "\" id='N_" + factdetails.get(k) + "' value=No >No</lable>&nbsp;&nbsp";
                            factshtml += "<lable><input type=\"radio\"name=\"radiono_" + factdetails.get(k) + "\" id='Y_" + factdetails.get(k) + "' value=Yes checked>Yes</lable></td>&nbsp;&nbsp";
                        } else {
                            factshtml += "<td><lable><input type=\"radio\" name=\"radiono_" + factdetails.get(k) + "\" id='N_" + factdetails.get(k) + "' value=No checked>No</lable>&nbsp;&nbsp";
                            factshtml += "<lable><input type=\"radio\"name=\"radiono_" + factdetails.get(k) + "\" id='Y_" + factdetails.get(k) + "' value=Yes>Yes</lable></td>&nbsp;&nbsp";
                        }
                    } else {
                        factshtml += "<td><lable><input type=\"radio\" name=\"radiono_" + factdetails.get(k) + "\" id='N_" + factdetails.get(k) + "' value=No checked>No</lable>&nbsp;&nbsp";
                        factshtml += "<lable><input type=\"radio\"name=\"radiono_" + factdetails.get(k) + "\" id='Y_" + factdetails.get(k) + "' value=Yes>Yes</lable></td>&nbsp;&nbsp";
                    }

                } else {
                    factshtml += "<td><lable><input type=\"radio\" name=\"radiono_" + factdetails.get(k) + "\" id='N_" + factdetails.get(k) + "' value=No checked>No</lable>&nbsp;&nbsp";
                    factshtml += "<lable><input type=\"radio\"name=\"radiono_" + factdetails.get(k) + "\" id='Y_" + factdetails.get(k) + "' value=Yes>Yes</lable></td>&nbsp;&nbsp";
                }
            }
            factshtml += "</tr></table>";
            
              
            
            factshtml += "<table><tr><br><th style=\"width:100px;\" align=\"left\">Choose Calendar</th><td colspan=\"3\"><select name=daydenom id=daydenom>";
            for (int l = 0; l < daydenomlist.size(); l++) {
                if (!calendar.equalsIgnoreCase("")) {
                    if (calendar.equalsIgnoreCase(daydenomlist.get(l))) {
                        factshtml += "<option selected value='" + daydenomlist.get(l) + "'>" + daydenomlist.get(l) + "</option>";
                    } else {
                        factshtml += "<option value='" + daydenomlist.get(l) + "'>" + daydenomlist.get(l) + "</option>";
                    }
                } else {
                    factshtml += "<option value='" + daydenomlist.get(l) + "'>" + daydenomlist.get(l) + "</option>";
                }
            }
            factshtml += "</select></td></tr></table></n></n></n></n>";

            factshtml += "<table><tr align=\"center\"><td colspan=\"3\"><input type=\"button\"name=\"Done\" value=\"Done\"onclick=\"MultiCalendar('" + ctxpath + "','" + ReportId + "','" + measureid + "','" + tableid + "','" + factdetails + "')\"></td></tr>";
            factshtml += "</table></form>";
            PrintWriter out = response.getWriter();
            out.print(factshtml.toString());
        }
        return null;
    }

    public ActionForward changeSelectedViewBy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportid = request.getParameter("reportId");
        String rowViewByArray = request.getParameter("RowViewByArray");
        String[] rowIdArr = rowViewByArray.split(",");
        ArrayList<String> listview = new ArrayList<String>();
        for (int k = 0; k < rowIdArr.length; k++) {
            listview.add(rowIdArr[k]);
        }
        ArrayList<String> viewlist = new ArrayList<String>();

        HashMap<String, List> viewdata = new HashMap<String, List>();
        ArrayList<String> viewbyname = new ArrayList<String>();
        HttpSession session = request.getSession(false);
        if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportid) != null) {
                Container container = (Container) map.get(reportid);
                if (container != null) {
                    container.setSelectedviewby(listview);
                    PbReportCollection collect = container.getReportCollect();
                    HashMap inMap1;
                    inMap1 = (HashMap) collect.reportParameters;
                    List al12 = null;
                    // al12 = (List<String>) inMap1.get(a1[j]);
                    viewlist = container.getSelectedviewby();

                    if (viewlist != null && !viewlist.isEmpty()) {
                        for (int k = 0; k < viewlist.size(); k++) {
                            al12 = (List<String>) inMap1.get(viewlist.get(k));
                            viewbyname.add(al12.get(1).toString());
                        }
                        viewdata.put("Viewbyid", viewlist);
                        viewdata.put("Viewbyname", viewbyname);

                    }
                    session.setAttribute("Viewbynamenew", viewbyname);
                    session.setAttribute("Viewbyidnew", viewlist);
                }

            }
        }
        return null;
    }

    public ActionForward changeEditViewBy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<String> viewlist = new ArrayList<String>();

        HashMap<String, List> viewdata = new HashMap<String, List>();
        ArrayList<String> viewbyname = new ArrayList<String>();
        String reportid = request.getParameter("reportId");
        HttpSession session = request.getSession(false);
        if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportid) != null) {
                Container container = (Container) map.get(reportid);
                if (container != null) {
                    PbReportCollection collect = container.getReportCollect();
                    HashMap inMap1;
                    inMap1 = (HashMap) collect.reportParameters;
                    List al12 = null;
                    // al12 = (List<String>) inMap1.get(a1[j]);
                    viewlist = container.getSelectedviewby();

                    if (viewlist != null && !viewlist.isEmpty()) {
                        for (int k = 0; k < viewlist.size(); k++) {
                            al12 = (List<String>) inMap1.get(viewlist.get(k));
                            viewbyname.add(al12.get(1).toString());
                        }
                        viewdata.put("Viewbyid", viewlist);
                        viewdata.put("Viewbyname", viewbyname);

                    }
                    session.setAttribute("Viewbynamenew", viewbyname);
                    session.setAttribute("Viewbyidnew", viewlist);
                    PrintWriter out = response.getWriter();
                    Gson gson = new Gson();
                    String jsonMap = gson.toJson(viewdata);
                    out.print(jsonMap);
                }
            }
        }



        return null;
    }
public ActionForward changeCompDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<String> viewlist = new ArrayList<String>();
   HashMap<String, List> viewdata = new HashMap<String, List>();
        ArrayList<String> viewbyname = new ArrayList<String>();
        String reportid = request.getParameter("reportId");
        Boolean flageValue = Boolean.parseBoolean(request.getParameter("flageValue"));
        HttpSession session = request.getSession(false);
        if (session.getAttribute("PROGENTABLES") != null) {
            HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
            if (map.get(reportid) != null) {
                Container container = (Container) map.get(reportid);
                container.setenableComp(flageValue);
            }
        }
 return null;
    }
    // Added by krishan pratap
    public ActionForward saveShowHeader(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        String reportid = request.getParameter("reportId");
        String radioValue = request.getParameter("radioValue");

        int i = ReportTemplateDAO.saveShowHeader(reportid, radioValue);

        out.print(i);
        return null;
    }
    // Added by Ashutosh
    public ActionForward isDraggable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        ReportTemplateDAO ReportTemplateDAO = new ReportTemplateDAO();
        String reportid = request.getParameter("reportId");
        String draggable = request.getParameter("draggable");

        int i = ReportTemplateDAO.isDraggable(reportid, draggable);

        out.print(i);
        return null;
    }
    
//ended by sruthi
    //added by sruthi for showfilters

    public ActionForward getShowFilters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String reportid = request.getParameter("rportid");
        String selectdata = request.getParameter("seleteddata");
        String selectedfield = request.getParameter("selectedfield");
        if (selectedfield != null && !selectedfield.isEmpty() && selectedfield.equalsIgnoreCase("graphfilters")) {
            session.setAttribute("graphselectdata", selectdata);
            String graphselecteddata = (String) session.getAttribute("graphselectdata");
        } else {
            if (session.getAttribute("PROGENTABLES") != null) {
                HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(reportid) != null) {
                    Container container = (Container) map.get(reportid);
                    if (container != null) {
                        container.setshowfilters(selectdata);
                    }
                }
            }
        }
        return null;
    }

    public ActionForward getRecentalyUsedUserReports(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        try {
            ReportTemplateDAO dao = new ReportTemplateDAO();
            response.getWriter().print(dao.getRecentalyUsedUserReports(request, response, session));
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }
    /*
     * Author: Amar Module: adding this to get all AO' UI component
     *
     */

    public ActionForward loadAOUIComponents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        ArrayList<String> components = new ArrayList<String>();
        String tabType = request.getParameter("tabType");
        ArrayList<String> translatedComponent = new ArrayList<String>();
        PrgUIContainer uiContainer = new PrgUIContainer("DROP_DOWN");
        String userId = String.valueOf(session.getAttribute("USERID"));
        Locale locale = (Locale) session.getAttribute("userLocale");
        if(tabType!=null && tabType.equalsIgnoreCase("MgmtTemplate")){
            components.add("CREATE_TEMPLATE");
        components.add("EDIT_TEMPLATE");
        components.add("DELETE_TEMPLATE");
//        components.add("SCHEDULE_AO");
//        components.add("EDIT_SCHEDULE_AO");
        }else{
        components.add("CREATE_AO");
        components.add("EDIT_AO");
        components.add("DELETE_AO");
        components.add("SCHEDULE_AO");
        components.add("EDIT_SCHEDULE_AO");
        }
        
//        components.add("CLEAR_CACHE");
//        components.add("DOWNLOAD");
//        components.add("SAVE_AS_HTML");
//        components.add("COPY_REPORT");
//        components.add("SHOW_METADATA");
//        components.add("COMPARE_REPORTS");
        //components.add("SAVE_AS_ADVANCED_HTML");

//        if( PrivilegeManager.isModuleComponentEnabledForUser("REPDESIGNER", "PURGEREPORT", Integer.parseInt(userId)))
        //  components.add("PURGE_REPORT");
        // components.add("RETRIEVE_FROM_BKP");
        //components.add("ADHOC_REPORT");           // Commented by mayank..

        for (String Componentvalues : components) {
            translatedComponent.add(TranslaterHelper.getTranslatedString(Componentvalues, locale));
        }

        for (int i = 0; i < components.size(); i++) {
            uiContainer.addComponent(components.get(i), translatedComponent.get(i));
        }

        String uiComponentJson = uiContainer.generateJSON();
        out.println(uiComponentJson);
        return null;
    }

    public ActionForward goToCreateAODesigner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        HashMap map = null;
        String aoId = null;
        HashMap AOId = new HashMap();
        HashMap AODetail = new HashMap();
        String isFromAOEdit = request.getParameter("fromAOEdit");
        String foldersIds = null;
        String aoName = null;
        String aoDesc = null;
        if (Boolean.parseBoolean(isFromAOEdit)) {//added by Dinanath
            aoId = request.getParameter("aoid");
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            reportTemplateDAO.getAOMasterMap(request, response, session);
            AODetail = (HashMap) session.getAttribute("AOMasterMap");
            foldersIds = ((HashMap) AODetail.get(aoId)).get("FOLDER_ID").toString();
            aoName = ((HashMap) AODetail.get(aoId)).get("AO_NAME").toString();
            aoDesc = ((HashMap) AODetail.get(aoId)).get("AO_DESC").toString();
        }
        Container container = null;
        String editAOName = null;
        String PbUserId = "";
        String repId = null;
        String result = "";
        String url = request.getRequestURL().toString();
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();
        String dimId = null;
        String timeparams = null;
        String[] timeparameterIds = null;
        String customReportId = null;
        ArrayList timeParameters = new ArrayList();
        ArrayList<String> timeColTypes = new ArrayList<String>();
        HashMap<String, ArrayList<String>> timeDimMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeDetails = new ArrayList<String>();
        QueryTimeHelper timeHelper = new QueryTimeHelper();

        if (session != null) {
            try {
                if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");
                    if (map.get(aoId) != null) {
                        container = (Container) map.get(aoId);
                        container = new Container();
                    } else {
                        container = new Container();
                    }
                } else {
                    container = new Container();
                }
                customReportId = request.getParameter("AOID");
                dimId = request.getParameter("dimId");
                timeparams = request.getParameter("timeparams");
                if (timeparams != null) {
                    timeparameterIds = timeparams.split(",");
                    for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                        timeParameters.add(timeparameterIds[timeparamCount]);
                    }
                }
                for (Object timeParameter : timeParameters) {
                    timeColTypes.add((String) timeParameter);
                }
                for (int time = 0; time < timeParameters.size(); time++) {
                    timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_DAY_LEVEL, timeColTypes);
                    timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_DAY_LEVEL);
                }
                PbReportCollection collect = container.getReportCollect();
                if (collect == null) {
                    collect = new PbReportCollection();
                }
                collect.timeDetailsArray = timeDetails;
                collect.timeDetailsMap = timeDimMap;
                if (!Boolean.parseBoolean(isFromAOEdit)) {//added by Dinanath
                    aoName = request.getParameter("aoName");
                    aoDesc = request.getParameter("aoDesc");
                    aoId = reportViewerDAO.getAOId();
                }
                aoName = aoName.replace('^', '&').replace('~', '+').replace('`', '#');
                aoName = aoName.replace("'", "''");
                aoDesc = aoDesc.replace('^', '&').replace('~', '+').replace('`', '#');
                aoDesc = aoDesc.replace("'", "''");
                if (request.getParameter("editRepName") != null) {
                    repId = request.getParameter("repId");
                    editAOName = request.getParameter("editRepName");
                }
                PbUserId = String.valueOf(session.getAttribute("USERID"));
                url = url + "?templateParam=goToAODesigner&aoName=" + aoName + "&aoDesc=" + aoDesc;
                request.setAttribute("reportdesignerurl", url);
                container.setReportCollect(collect);
                container.setReportId(aoId);
                container.setTableId(aoId);
                container.setNetTotalReq(true);
                container.setGrandTotalReq(true);
                if (!Boolean.parseBoolean(isFromAOEdit)) {//added by Dinanath
                    result = "<table><tr><td><ul>";
                    result += reportTemplateDAO.getUserFoldersByUserIdInDesigner(PbUserId);
                    result += "</ul></td></tr><tr><td><input type=\"button\" value=\"Done\" onclick=\"creationOfAO(" + aoId + ")\" class=\"navtitle-hover\" name=\"Done\"></td></tr></table>";
                }
                request.setAttribute("ReportId", aoId);
                request.setAttribute("UserFlds", result);
                container.setAOName(aoName);
                container.setReportDesc(aoDesc);
                container.setReportId(aoId);
                container.setSessionContext(session, container);
            } catch (Exception exception) {
                logger.error("Exception:", exception);
                return mapping.findForward("exceptionPage");
            }
            //added by Dinanath
            request.setAttribute("isFromAOEdit", isFromAOEdit);
            if (!Boolean.parseBoolean(isFromAOEdit)) {
                if (editAOName == null) {
                    PrintWriter out = response.getWriter();
                    out.print(result);
                    return null;
                } else {
                    reportTemplateDAO.editReportName(repId, aoName, aoDesc);
                    return null;
                }
            } else {
                foldersIds = foldersIds;
                response.getWriter().print(foldersIds);
                return null;//roleids 
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward selectRoleGoToDesinAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String repId = request.getParameter("repId");
        String roleId = request.getParameter("roleId");
        String isFromAOEdit = (String) request.getParameter("isFromAOEdit");
        if (Boolean.parseBoolean(isFromAOEdit)) {
            request.setAttribute("isFromAOEdit", isFromAOEdit);
        }
        HashMap map = new HashMap();
        Container container = null;
        System.out.println("roleId-----------" + roleId);
        request.setAttribute("ReportId", repId);
        request.setAttribute("fromDesigner", "true");
        request.setAttribute("roleid", roleId);
        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(repId);
            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserFolderIds", roleId);
            container.setParametersHashMap(ParametersHashMap);
            String[] strArray = new String[]{roleId};
            PbReportCollection collection = container.getReportCollect();
            collection.setReportBizRole(Integer.parseInt(roleId));
            container.setFlagg(true);
            container.setDefault_tab("Key" + repId, "table");
            return mapping.findForward("aoDesigner");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
    /*
     * Author: Amar Module: for retriving dimensions
     */

    public ActionForward addMoreDimensionsAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String foldersIds = "";
        String PbUserId = "";
        String result = "";
        String IsrepAdhoc1;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            ArrayList<String> reportParamIds = new ArrayList<String>();
//            String reportParamIdsVal = "0";
            StringBuilder reportParamIdsVal = new StringBuilder(300);
            reportParamIdsVal.append("0");
            String reportId = request.getParameter("REPORTID");
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                if (container != null) {
                    reportParamIds = (ArrayList) container.getParametersHashMap().get("Parameters");
                    if (reportParamIds != null && reportParamIds.size() > 0) {
                        for (int i = 0; i < reportParamIds.size(); i++) {
//                        reportParamIdsVal = reportParamIdsVal +","+reportParamIds.get(i);
                            reportParamIdsVal.append(",").append(reportParamIds.get(i));
                        }
                    }
                }
            }

            IsrepAdhoc1 = (request.getParameter("IsrepAdhoc"));
            request.setAttribute("IsrepAdhoc1", IsrepAdhoc1);
            customReportId = request.getParameter("REPORTID");
            foldersIds = request.getParameter("foldersIds");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
//          changed by Nazneen for getting dims which r not added to rep
//          result = reportTemplateDAO.getUserDimDetails(foldersIds, PbUserId, reportParamIdsVal);
            result = reportTemplateDAO.getUserDimDetailsForRepCustom(foldersIds, PbUserId, reportParamIdsVal.toString());
            request.setAttribute("dims", result);
            request.setAttribute("roleId", foldersIds);
            request.setAttribute("USERID", PbUserId);
            request.setAttribute("reportId", customReportId);
            request.setAttribute("isFromAOEdit", request.getParameter("isFromAOEdit"));
            if (request.getParameter("isOneview") != null && request.getParameter("isOneview").equalsIgnoreCase("True")) {
                request.setAttribute("isOneview", "true");
            }

            return mapping.findForward("addDimsAO");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
    /*
     * Author: Amar Module: added by Amar to create AO Object
     */

    public ActionForward buildTableAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);

        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";

        String buildTableChange = null;
        String Measures = null;
        String selectedList = null;
        String rowEdgeParams = null;
        String colEdgeParams = null;


        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);
            buildTableChange = request.getParameter("buildTableChange");
            Measures = request.getParameter("Msrs");
            String Fulllist = request.getParameter("timeDetails");
            selectedList = "MTD,PMTD,PYMTD,QTD,PQTD,PYQTD,YTD,PYTD";
            rowEdgeParams = request.getParameter("rowEdgeParams");
            colEdgeParams = request.getParameter("colEdgeParams");
            String[] splitselectedList = selectedList.split(",");
            String Allchange = "CPMTD,C%PMTD,CPYMTD,C%PYMTD,CPQTD,C%PQTD,CPYQTD,C%PYQTD,CPYTD,C%PYTD";
            String[] splitAllchange = Allchange.split(",");
            ArrayList<String> currentTimeperiods = new ArrayList<>();
            ArrayList<String> CurrentAllchanges = new ArrayList<>();
            ArrayList<String> allcolumns = new ArrayList<>();
            boolean b = true;

            String tduration = request.getParameter("timespan");
            String aoSdate = request.getParameter("startdate");
            String aoEdate = request.getParameter("enddate");
            String aoTimePeriod = request.getParameter("frequency");

            StringBuilder Parameters1 = new StringBuilder();
            Parameters1.append(",").append(tduration).append(",").append(aoSdate).append(",").append(aoEdate).append(",").append(aoTimePeriod);

            PbReportCollection collect = container.getReportCollect();

            try {
                String numberFormatQuery1 = "select TIME_Details from prg_ar_ao_master where Ao_id=" + customReportId;
                PbDb pbdb1 = new PbDb();
                PbReturnObject returnObject1 = new PbReturnObject();
                returnObject1 = pbdb1.execSelectSQL(numberFormatQuery1);
                if (returnObject1 != null && returnObject1.getRowCount()> 0 && returnObject1.getFieldValueString(0, 0) != null) {
                    if (!Parameters1.toString().equalsIgnoreCase(returnObject1.getFieldValueString(0, 0).toString())) {
                        container.aoEditSummFlag = true;
                    }
                }else{
                    container.aoEditSummFlag = true;
                }

                container.tduration = tduration;
                container.aoSdate = aoSdate;
                container.aoEdate = aoEdate;
                container.aoTimePeriod = aoTimePeriod;

                if (buildTableChange != null && buildTableChange.equalsIgnoreCase("REP")) {
                    String[] REP = rowEdgeParams.split(",");
                    ArrayList<String> rowViewByValues = new ArrayList<String>();

                    for (int i = 0; i < REP.length; i++) {
                        if (!(REP[i].equalsIgnoreCase(""))) {
                            rowViewByValues.add(REP[i]);
                        }
                    }

                    collect.setRowViewBys(rowViewByValues);
                    //GraphHashMap = reportBD.changeViewBys(GraphHashMap, rowEdgeArray, rowEdgeNameArray, Parameters, ParametersNames);


                } else if (buildTableChange != null && buildTableChange.equalsIgnoreCase("CEP")) {
                    String[] CEP = colEdgeParams.split(",");
                    ArrayList<String> colViewByValues = new ArrayList<String>();

                    for (int i = 0; i < CEP.length; i++) {
                        if (!(CEP[i].equalsIgnoreCase(""))) {
                            colViewByValues.add(CEP[i]);
                        }
                    }

                    collect.setColumnViewBys(colViewByValues);

                } else if (buildTableChange != null && buildTableChange.equalsIgnoreCase("Measures") && Measures != null) {
                    String[] Msrs = Measures.split(",");


                    if (Fulllist != null && !Fulllist.equalsIgnoreCase("")) {

                        String[] splitFulllist = Fulllist.split(",");

                        outerloop:
                        for (int i = 0; i < splitFulllist.length; i++) {

                            allcolumns.add(splitFulllist[i]);
                            for (int j = 0; j < splitselectedList.length; j++) {

                                if (splitFulllist[i].equalsIgnoreCase(splitselectedList[j])) {
                                    currentTimeperiods.add(splitFulllist[i]);
                                    continue outerloop;
                                }


                            }
                            for (int j = 0; j < splitAllchange.length; j++) {
                                if (splitFulllist[i].equalsIgnoreCase(splitAllchange[j])) {
                                    CurrentAllchanges.add(splitFulllist[i]);
                                    continue outerloop;
                                }


                            }

                        }
                        ArrayList<String> timePeriodsList = new ArrayList<String>();
                        List<String> l = Arrays.<String>asList(Msrs);
                        ArrayList<String> al = new ArrayList<String>(l);
                        for (int j = 0; j < Msrs.length; j++) {
                            for (int i = 0; i < currentTimeperiods.size(); i++) {
                                timePeriodsList.add(currentTimeperiods.get(i));
                            }
                        }
                        container.setDistinctTimePeriodsList(currentTimeperiods);
                        container.setTimePeriodsList(timePeriodsList);
                        container.setAllColumns(allcolumns);
                        container.setChangeColumns(CurrentAllchanges);
                    }


                    ArrayList measureArray = new ArrayList();
                    ReportTemplateDAO dao = new ReportTemplateDAO();
                    ArrayList<String> reportQryElements = new ArrayList<String>();
                    for (int i = 0; i < Msrs.length; i++) {
                        if (!(Msrs[i].equalsIgnoreCase(""))) {
                            measureArray.add("A_" + Msrs[i]);
                            //MeasuresNames.add(MsrsNames[i]);//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
                            reportQryElements.add(Msrs[i]);
                        }
                    }
                    ArrayList<String> reportQryAggs;
                    ArrayList<String> reportQryColNames;
                    ArrayList<String> reportQryColTypes;
                    ArrayList<String> originalColTypes;

                    if (!reportQryElements.isEmpty()) {
                        reportQryAggs = dao.getReportQryAggregations(reportQryElements);
                        reportQryColNames = dao.getReportQryColNames();
                        reportQryColTypes = dao.getReportQryColTypes();
                        originalColTypes = dao.getOriginalReportQryColTypes();
                        HashMap nfmap = new HashMap();
                        PbDb pbdb = new PbDb();

                        for (int j = 0; j < reportQryElements.size(); j++) {
                            String numberFormatQuery = "select no_format from prg_user_sub_folder_elements where element_id=" + reportQryElements.get(j);
                            PbReturnObject returnObject = new PbReturnObject();
                            returnObject = pbdb.execSelectSQL(numberFormatQuery);
                            if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                                nfmap.put("A_" + reportQryElements.get(j), returnObject.getFieldValueString(0, 0));
                            }
                        }
                        for (int j = 0; j < reportQryElements.size(); j++) {
                            String numberFormatQuery = "select symbols from prg_user_sub_folder_elements where element_id=" + reportQryElements.get(j);
                            PbReturnObject returnObject = new PbReturnObject();
                            returnObject = pbdb.execSelectSQL(numberFormatQuery);
                            if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                                container.symbol.put("A_" + reportQryElements.get(j), returnObject.getFieldValueString(0, 0));
                            }
                        }
                        for (int j = 0; j < reportQryElements.size(); j++) {
                            String numberFormatQuery = "select round from prg_user_sub_folder_elements where element_id=" + reportQryElements.get(j);
                            PbReturnObject returnObject = new PbReturnObject();
                            returnObject = pbdb.execSelectSQL(numberFormatQuery);
                            if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                                if (returnObject.getFieldValueString(0, 0).equalsIgnoreCase("")) {
                                    container.measRoundingPrecisions.put("A_" + reportQryElements.get(j), 0);
                                } else {
                                    container.measRoundingPrecisions.put("A_" + reportQryElements.get(j), Integer.parseInt(returnObject.getFieldValueString(0, 0)));
                                }
                            } else {
                                container.measRoundingPrecisions.put("A_" + reportQryElements.get(j), 0);
                            }
                        }
                        //added by Nazneen
                        for (int j = 0; j < reportQryElements.size(); j++) {
                            String numberFormatQuery = "select date_format from prg_user_all_info_details where element_id=" + reportQryElements.get(j);
                            PbReturnObject returnObject = new PbReturnObject();
                            returnObject = pbdb.execSelectSQL(numberFormatQuery);
                            if (returnObject != null && returnObject.getFieldValueString(0, 0) != null) {
                                if (!returnObject.getFieldValueString(0, 0).equalsIgnoreCase("")) {
                                    container.setDateFormatt("A_" + reportQryElements.get(j), returnObject.getFieldValueString(0, 0));
                                }
                            }
                        }


                        container.getTableHashMap().put("NFMap", nfmap);
                        for (int i = 0; i < reportQryElements.size(); i++) {
                            collect.setReportQueryColumns(reportQryElements.get(i), reportQryColNames.get(i), reportQryAggs.get(i), reportQryColTypes.get(i), originalColTypes.get(i));
                        }
                    }


                    collect.initializeReportTableCols();

                } else if (buildTableChange != null && buildTableChange.equalsIgnoreCase("TableProperties")) {
                }

                return null;
            } catch (Exception exp) {
                logger.error("Exception:", exp);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward dispAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        System.out.println("----------display Table\n");
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        String filesPath = "";
     
    PbReportViewerDAO dao = new PbReportViewerDAO();
  

   
//         Container container = Container.getContainerFromSession(request, REPORTID);
        if (session != null) {
            filesPath = dao.getFilePath(session);
        } else {
            filesPath = "/usr/local/cache";
        }
        //
       
        Container container = null;
        String customReportId = "";
        String isFromAOEdit = request.getParameter("isFromAOEdit");
//        String result = "";
//
//        HashMap ParametersHashMap = null;
//        HashMap TableHashMap = null;
//        HashMap GraphHashMap = null;
        PrintWriter out = response.getWriter();
        String userId;
        String KpiDashboard;
        String action;
        ReportTemplateBD reportBD = new ReportTemplateBD();

        if (session != null) {
            try {
                customReportId = request.getParameter("REPORTID");
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(customReportId);
                userId = (String) session.getAttribute("USERID");
                action = request.getParameter("action");
                KpiDashboard = request.getParameter("isKpiDashboard");
//                ParametersHashMap = container.getParametersHashMap();
//                TableHashMap = container.getTableHashMap();
//                GraphHashMap = container.getGraphHashMap();
//                reportBD.setUserId(String.valueOf(session.getAttribute("USERID")));
//                reportBD.setBizRoles(String.valueOf(ParametersHashMap.get("UserFolderIds")));
//                reportBD.setReportId(customReportId);

                //result = reportBD.buildTable(container, request.getContextPath(), session);
                HashMap timehash = new HashMap();
                String paramChange1 = request.getParameter("paramChange1");
                if (request.getParameter("action") != null && paramChange1 != null && paramChange1.equalsIgnoreCase("paramChange1")) {
                    timehash.put("CBO_PRG_COMPARE", request.getParameter("CBO_PRG_COMPARE"));
                    timehash.put("perioddate", request.getParameter("perioddate"));
                    timehash.put("CBO_AS_COF_DATE", request.getParameter("CBO_AS_COF_DATE"));
                    timehash.put("CBO_AS_COF_DATE1", request.getParameter("CBO_AS_COF_DATE1"));
                    timehash.put("CBO_PRG_PERIOD_TYPE", request.getParameter("CBO_PRG_PERIOD_TYPE"));
                    timehash.put("datetext", request.getParameter("datetext"));
                    timehash.put("fromdate", request.getParameter("fromdate"));
                    timehash.put("todate", request.getParameter("todate"));
                    timehash.put("comparefrom", request.getParameter("comparefrom"));
                    timehash.put("compareto", request.getParameter("compareto"));
                    timehash.put("CBO_AS_OF_DATE1", request.getParameter("CBO_AS_OF_DATE1"));
                    timehash.put("CBO_AS_OF_DATE11", request.getParameter("CBO_AS_OF_DATE11"));
                    timehash.put("CBO_AS_OF_DATE2", request.getParameter("CBO_AS_OF_DATE2"));
                    timehash.put("CBO_AS_OF_DATE21", request.getParameter("CBO_AS_OF_DATE21"));
                    timehash.put("CBO_CMP__AS_OF_DATE1", request.getParameter("CBO_CMP_AS_OF_DATE1"));
                    timehash.put("CBO_CMP_AS_OF_DATE11", request.getParameter("CBO_CMP_AS_OF_DATE11"));
                    timehash.put("CBO_CMP_AS_OF_DATE2", request.getParameter("CBO_CMP_AS_OF_DATE2"));
                    timehash.put("CBO_CMP_AS_OF_DATE21", request.getParameter("CBO_CMP_AS_OF_DATE21"));
                }
                container.setIsFromAOEdit(Boolean.parseBoolean(isFromAOEdit));
                 container.setFilesPath(filesPath);
                boolean result=reportBD.buildAO(action, container, userId, timehash);
//                String tabStr = result.split("ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¥")[0];
//                String sqlStr = result.split("ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¥")[1];
//                //////.println("in action sqlstr is : " + sqlStr);
//                session.setAttribute("sqlStr", sqlStr);
//                //////.println("in action result is : " + result);
//                out.print(tabStr + "@!" + sqlStr);
                out.print(result);
                return null;

            } catch (Exception e) {
                logger.error("Exception:", e);
                return mapping.findForward("exceptionPage");
            }
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward getMeasuresAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String foldersIds = "";
        String result = "";
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";

        ArrayList<String> Parameters = null;
        ArrayList<String> Parameters1 = null;

        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        if (session != null) {
            customReportId = request.getParameter("REPORTID");
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);
            String tableList = request.getParameter("tableList");


            Parameters = new ArrayList<String>();
            Parameters1 = new ArrayList<String>();

            for (String parameter : container.getParameterElements()) {
                Parameters.add(parameter);
            }

            //////.println("dimFactRel is : "+dimFactRel);

            foldersIds = request.getParameter("foldersIds");
            result = "";
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            if (container.getTableList() != null && !container.getTableList().isEmpty()) {
                ArrayList alist = new ArrayList();
                result = repdao.getMeasuresForReport(foldersIds, Parameters, request.getContextPath(), container.getTableList());
                container.setTableList(alist);
            } else if (tableList != null && tableList.equalsIgnoreCase("true")) {
                result = reportTemplateDAO.getMeasures(foldersIds, Parameters, request.getContextPath());
            }

            request.setAttribute("Measures", result);
            return mapping.findForward("measuresAO");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward buildParamsAO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        ProgenParam pParam = new ProgenParam();
        HttpSession session = request.getSession(false);
        String foldersIds = null;
        String minTimeLevel = null;

        String paramIds = null;
        String paramNames = null;
        String dimId = null;
        String timeparams = null;

        String[] parameterIds = null;
        String[] parameterNames = null;//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
        String[] timeparameterIds = null;

        ArrayList Parameters = new ArrayList();
        ArrayList ParametersNames = new ArrayList();//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
        ArrayList timeParameters = new ArrayList();

        String customReportId = null;
        HashMap map = null;
        Container container = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();

        if (session != null) {

            customReportId = request.getParameter("REPORTID");
            String dispDesignerDims = request.getParameter("dispDesignerDims");
            String fromDesigner = request.getParameter("fromDesigner");

            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(customReportId);

            PbReportCollection collect = container.getReportCollect();

            foldersIds = request.getParameter("foldersIds");
            dimId = request.getParameter("dimId");
            if (foldersIds != null && !"".equals(foldersIds)) {
                collect.reportBizRoles = foldersIds.split(",");
            }


            minTimeLevel = reportTemplateDAO.getUserFolderMinTimeLevel(foldersIds);

            paramIds = request.getParameter("params");
            paramIds = paramIds.replaceAll("elmnt-", "").trim();
            paramNames = request.getParameter("paramNames");//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
            timeparams = request.getParameter("timeparams");
            //update Param hashmap in session
            if (paramIds != null && !"".equalsIgnoreCase(paramIds)) {
                parameterIds = paramIds.split(",");
                parameterNames = paramNames.split(",");
                collect.resetAllParameters();
                ArrayList<String> defaultValues = new ArrayList<String>();
                for (int paramCount = 0; paramCount < parameterIds.length; paramCount++) {
                    Parameters.add(parameterIds[paramCount]);
                    ParametersNames.add(parameterNames[paramCount]);//newly added on 15-10-2009 by bugger santhosh.kumar@progenbusiness.com
                    collect.setParameters(parameterIds[paramCount], parameterNames[paramCount], defaultValues);
                    container.removeParameterSecurity(parameterIds[paramCount]);
                }
            }

            if (timeparams != null) {
                timeparameterIds = timeparams.split(",");
                for (int timeparamCount = 0; timeparamCount < timeparameterIds.length; timeparamCount++) {
                    timeParameters.add(timeparameterIds[timeparamCount]);
                }
            }


            Date date = new Date();
            String DATE_FORMAT = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

            HashMap<String, ArrayList<String>> timeDimMap = new HashMap<String, ArrayList<String>>();
            ArrayList<String> timeDetails = new ArrayList<String>();
            QueryTimeHelper timeHelper = new QueryTimeHelper();

            ArrayList<String> timeColTypes = new ArrayList<String>();

            for (Object timeParameter : timeParameters) {
                timeColTypes.add((String) timeParameter);
            }

            if (dimId.equalsIgnoreCase("Time-Period Basis") || dimId.equalsIgnoreCase("Time-Range Basis") || dimId.equalsIgnoreCase("Time-Rolling Period") || dimId.equalsIgnoreCase("Time-Month Range Basis")
                    || dimId.equalsIgnoreCase("Time-Quarter Range Basis") || dimId.equalsIgnoreCase("Time-Year Range Basis") || dimId.equalsIgnoreCase("Time-Cohort Basis") || minTimeLevel.equals("4")
                    || (minTimeLevel.equals("3")) || (minTimeLevel.equals("2")) || (minTimeLevel.equals("1"))) {
                if (timeParameters != null) {
                    for (int time = 0; time < timeParameters.size(); time++) {
                        if (minTimeLevel.equals("5")) {
                            if (dimId.equalsIgnoreCase("Time-Period Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Range Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_RANGE_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_RANGE_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Rolling Period")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_ROLLING_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_ROLLING_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Month Range Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_MONTH_RANGE_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_MONTH_RANGE_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Quarter Range Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_QUARTER_RANGE_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_QUARTER_RANGE_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Year Range Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_YEAR_RANGE_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_YEAR_RANGE_BASIS, TIME_DAY_LEVEL);
                            }
                            if (dimId.equalsIgnoreCase("Time-Cohort Basis")) {
                                timeDimMap = timeHelper.buildTimeMap(TIME_COHORT_BASIS, TIME_DAY_LEVEL, timeColTypes);
                                timeDetails = timeHelper.buildTimeDetails(TIME_COHORT_BASIS, TIME_DAY_LEVEL);
                            }
                        } else if (minTimeLevel.equals("4")) {

                            timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_WEEK_LEVEL, timeColTypes);
                            timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_WEEK_LEVEL);

                        } else if (minTimeLevel.equals("3")) {
                            timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_MONTH_LEVEL, timeColTypes);
                            timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_MONTH_LEVEL);
                        } else if (minTimeLevel.equals("2")) {
                            timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_QUARTER_LEVEL, timeColTypes);
                            timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_QUARTER_LEVEL);
                        } else if (minTimeLevel.equals("1")) {
                            timeDimMap = timeHelper.buildTimeMap(TIME_PERIOD_BASIS, TIME_YEAR_LEVEL, timeColTypes);
                            timeDetails = timeHelper.buildTimeDetails(TIME_PERIOD_BASIS, TIME_YEAR_LEVEL);
                        }
                    }
                }

                collect.timeDetailsArray = timeDetails;
                collect.timeDetailsMap = timeDimMap;
                container.setReportCollect(collect);
                if (dispDesignerDims != null && !dispDesignerDims.isEmpty() && dispDesignerDims.equalsIgnoreCase("dispDesignerDims")) {
                    HashMap ParametersHashMap = container.getParametersHashMap();
                    ParametersHashMap.put("Parameters", Parameters);
                    ParametersHashMap.put("ParametersNames", ParametersNames);
                    collect.reportParamIds = Parameters;
                    collect.reportParamNames = ParametersNames;
                }
                if (fromDesigner != null && !fromDesigner.isEmpty() && fromDesigner.equalsIgnoreCase("insightDesigner")) {
                    ArrayList<String> colViewByLst = new ArrayList<String>();
                    ArrayList<String> colViewNamesLst = new ArrayList<String>();
                    ArrayList<String> rowViewByLst = new ArrayList<String>();
                    ArrayList<String> rowViewNamesLst = new ArrayList<String>();
                    HashMap TableHashMap = null;
                    session.removeAttribute("colViewByLst");
                    session.removeAttribute("rowViewByLst");
                    //rowViewByLst.add(String.valueOf(Parameters.get(0)));
                    //rowViewByLst.addAll(Arrays.asList(Parameters));
                    Collections.addAll((List) rowViewByLst, Parameters);
                    collect.setRowViewBys(rowViewByLst);
                    //rowViewNamesLst.add(String.valueOf(ParametersNames.get(0)));
                    Collections.addAll((List) rowViewNamesLst, ParametersNames);
                    session.setAttribute("colViewByLst", colViewByLst);
                    session.setAttribute("rowViewByLst", rowViewByLst);
                    session.setAttribute("rowViewIdList", rowViewByLst);
                    session.setAttribute("colViewIdList", colViewByLst);
                    session.setAttribute("rowNamesLst", rowViewNamesLst);
                    session.setAttribute("colNamesLst", colViewNamesLst);
                    container.setColumnViewList(colViewByLst);
                    container.setRowViewList(rowViewByLst);
                    TableHashMap = container.getTableHashMap();
                    TableHashMap.put("REP", rowViewByLst);
                    TableHashMap.put("CEP", colViewByLst);
                    container.setTableHashMap(TableHashMap);
                }

            }

            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }
    //Start of code by bhargavi

    public ActionForward selectAoGoToDesin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String repId = request.getParameter("repId");
        String aoId = request.getParameter("aoId");
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        System.out.println("roleId-----------" + aoId);
        request.setAttribute("ReportId", repId);
        request.setAttribute("fromDesigner", "true");
        request.setAttribute("aoId", aoId);

        if (session != null) {
            map = (HashMap) session.getAttribute("PROGENTABLES");
            container = (Container) map.get(repId);
            HashMap ParametersHashMap = container.getParametersHashMap();
            ParametersHashMap.put("UserAoIds", aoId);
            container.setParametersHashMap(ParametersHashMap);
            String[] strArray = new String[]{aoId};
            PbReportCollection collection = container.getReportCollect();
            //collection.setReportAoId(Integer.parseInt(aoId));
            collection.setReportAoId(aoId);
            container.setAOId(aoId);
            container.setFlagg(true);
            return mapping.findForward("reportDesignerAO");
        } else {
            return mapping.findForward("sessionExpired");
        }

    }

    public ActionForward addMoreDimensionsAoReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        HashMap map = new HashMap();
        Container container = null;
        String customReportId = "";
        String aoIds = "";
        String PbUserId = "";
        String result = "";
        String IsrepAdhoc1;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        if (session != null) {
            ArrayList<String> reportParamIds = new ArrayList<String>();
//            String reportParamIdsVal = "0";
            StringBuilder reportParamIdsVal = new StringBuilder(300);
            reportParamIdsVal.append("0");
            String reportId = request.getParameter("REPORTID");
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (Container) map.get(reportId);
                if (container != null) {
                    reportParamIds = (ArrayList) container.getParametersHashMap().get("Parameters");
                    if (reportParamIds != null && reportParamIds.size() > 0) {
                        for (int i = 0; i < reportParamIds.size(); i++) {
//                        reportParamIdsVal = reportParamIdsVal +","+reportParamIds.get(i);
                            reportParamIdsVal.append(",").append(reportParamIds.get(i));
                        }
                    }
                }
            }

            IsrepAdhoc1 = (request.getParameter("IsrepAdhoc"));
            request.setAttribute("IsrepAdhoc1", IsrepAdhoc1);
            customReportId = request.getParameter("REPORTID");
            aoIds = request.getParameter("aoid");
            PbUserId = String.valueOf(session.getAttribute("USERID"));
            result = reportTemplateDAO.getUserDimDetailsForRepAO(aoIds, PbUserId, reportParamIdsVal.toString());
            request.setAttribute("dims", result);
            request.setAttribute("aoId", aoIds);
            request.setAttribute("USERID", PbUserId);
            request.setAttribute("reportId", customReportId);
            if (request.getParameter("isOneview") != null && request.getParameter("isOneview").equalsIgnoreCase("True")) {
                request.setAttribute("isOneview", "true");
            }

            return mapping.findForward("addAODimsRep");
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    //Added by Mohit for ao master
    public ActionForward getAOMasterMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//       String aoId = request.getParameter("aoId");
        HttpSession session = request.getSession(false);
        try {
            ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
            reportTemplateDAO.getAOMasterMap(request, response, session);
            HashMap map = new HashMap();
            Container container = null;
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (session != null) {
            return null;
        } else {
            return null;
        }
    }
    //added by Mohit for session management;

    public ActionForward removeSessionAttributes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//       String aoId = request.getParameter("aoId");
        HttpSession session = request.getSession(false);
        try {
            Enumeration keys = session.getAttributeNames();
            int i = 0;
            while (keys.hasMoreElements()) {
                i++;
                String key = (String) keys.nextElement();
//  System.out.println("::"+i+")"+key+"");
            }
            String sessionNames[] = {"rowNamesLst", "colViewIdList", "allViewIds", "colViewByLst", "rowViewIdList", "rowViewByLst", "allViewNames", "colNamesLst", "FiltersMap"};
            for (i = 0; i < sessionNames.length; i++) {
                session.removeAttribute(sessionNames[i]);
//      System.out.println("removeAttribute::"+sessionNames[i]);
            }

            keys = session.getAttributeNames();
            i = 0;
            while (keys.hasMoreElements()) {
                i++;
                String key = (String) keys.nextElement();
//  System.out.println("::"+i+")"+key+"");
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (session != null) {
            return null;
        } else {
            return null;
        }
    }
    //added by bhargavi

    public ActionForward DeleteUserAOs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            ReportTemplateDAO repdao = new ReportTemplateDAO();
            String userid = "";
            String deleteRepIds = request.getParameter("deleterepids");
            if (session.getAttribute("USERID") != null) {
                userid = session.getAttribute("USERID").toString();

            }
            repdao.DeleteUserAOs(userid, deleteRepIds);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("getUserBusGrps", "getUserBusGrps");
        map.put("getUserFolders", "getUserFolders");
        map.put("getUserDims", "getUserDims");
        map.put("buildGraphs", "buildGraphs");
        map.put("dispRowEdgeParams", "dispRowEdgeParams");
        map.put("dispColEdgeParams", "dispColEdgeParams");
        map.put("getMeasures", "getMeasures");
        map.put("dispParams", "dispParams");
        map.put("buildParams", "buildParams");
        map.put("buildTable", "buildTable");
        map.put("dispTable", "dispTable");
        map.put("refreshGraphs", "refreshGraphs");
        map.put("deleteSticky", "deleteSticky");
        map.put("saveSticky", "saveSticky");
        map.put("checkReportName", "checkReportName");
        map.put("saveReport", "saveReport");
        map.put("goToReportDesigner", "goToReportDesigner");
        map.put("getGraphDetails", "getGraphDetails");
        map.put("getAllReports", "getAllReports");
        map.put("getAllReportshome", "getAllReportshome");
        map.put("getAllDashs", "getAllDashs");
        map.put("getAllreps", "getAllreps");
        map.put("getAllUsers", "getAllUsers");
        map.put("getBuckets", "getBuckets");
        map.put("Deletedashboard", "Deletedashboard");
        map.put("DeleteUserReportslist", "DeleteUserReports");
        map.put("getCustomMeasures", "getCustomMeasures");
        //added for favparams
        map.put("checkParamName", "checkParamName");
        map.put("saveFavParameters", "saveFavParameters");
        map.put("getFavParams", "getFavParams");
        //map.put("getCustomMeasures", "getCustomMeasures");
        map.put("changeReportName", "changeReportName");
        map.put("checkReportNameBeforeUpdate", "checkReportNameBeforeUpdate");
        map.put("saveCurrentTabAndReportId", "saveCurrentTabAndReportId");//by prabal

        //added by bharathi reddy
        map.put("saveParamSecurity", "saveParamSecurity");
        map.put("saveParamDefaultValues", "saveParamDefaultValues");
        map.put("checkavgTwoTables", "checkavgTwoTables");
        map.put("getitext", "getitext");
        map.put("excludeREP", "excludeREP");
        map.put("excludeCEP", "excludeCEP");
        map.put("addReportForCompany", "addReportForCompany");
        map.put("shwparamAssis", "shwparamAssis");
        map.put("checkRportNGraphKey", "checkRportNGraph");

        // map.put("getTabColumns", "getTabColumns");
//for checking duplicate report names at role levelexcvallist
        map.put("checkReportNameatRoleLevel", "checkReportNameatRoleLevel");
         map.put("checkReportNameatAOLevel", "checkReportNameatAOLevel");

         map.put("checkReportNameatAOCreateLevel", "checkReportNameatAOCreateLevel");
        //buildGraphs
        //map.put("getUserDimsMbrs", "getUserDimsMbrs");

        map.put("repStudioSort", "repStudioSort");
        map.put("allReportSort", "allReportSort");
        map.put("dashStudioSort", "dashStudioSort");
        map.put("getPurgeReps", "getPurgeReps");
        map.put("repPurgeSort", "repPurgeSort");
        map.put("PurgeReports", "PurgeReports");
        map.put("getElementDataType", "getElementDataType");

        map.put("setCustomDate", "setCustomDate");
        map.put("setAggregation", "setAggregation");
        map.put("resetCurrReport", "resetCurrReport");
        map.put("updateParamSecurity", "updateParamSecurity");
        map.put("delParamSecurity", "delParamSecurity");
        map.put("getUserFavouriteDims", "getUserFavouriteDims");
        map.put("editUserFavouriteDims", "editUserFavouriteDims");
        map.put("updateUserFavouriteDims", "updateUserFavouriteDims");
        map.put("purgeDashboard", "purgeDashboard");
        map.put("loadUIComponents", "loadUIComponents");
        map.put("getRoleIdByReportID", "getRoleIdByReportID");
        map.put("copyReport", "copyReport");
        map.put("enableMap", "enableMap");
        map.put("showMetaData", "showMetaData");
        map.put("getRoles", "getRoles");
        map.put("getReportBasedOnRole", "getReportBasedOnRole");
        map.put("getReportsForDrillAcross", "getReportsForDrillAcross");
        map.put("drillAcross", "drillAcross");
        map.put("drill", "drill");
        map.put("getFavReports", "getFavReports");
        map.put("prepareReportsToCompare", "prepareReportsToCompare");
        map.put("getReportsToCompare", "getReportsToCompare");
        map.put("changeViewByCompare", "changeViewByCompare");
        map.put("getComparableReports", "getComparableReports");
        map.put("getReportsToCompare1", "getReportsToCompare1");
        map.put("changeViewByCompare1", "changeViewByCompare1");
        map.put("getReportDetailsList", "getReportDetailsList");
        map.put("clearCache", "clearCache");
        // added by ramesh
        map.put("pbParamDefaultValues", "pbParamDefaultValues");
        map.put("viewByReports", "viewByReports");
        map.put("getMeasuresForOneView", "getMeasuresForOneView");
        //added by veena
//        map.put("getAddMoreDims", "getAddMoreDims");
        map.put("addMoreDimensions", "addMoreDimensions");
        map.put("addMoreDimensionsfilter", "addMoreDimensionsfilter");
        map.put("saveNewDimensions", "saveNewDimensions");
        map.put("getFavReports1", "getFavReports1");
        map.put("saveTabs", "saveTabs");
        map.put("getKpiDashboards", "getKpiDashboards");
        map.put("removeDimensions", "removeDimensions");
        map.put("graphInReportViewer", "graphInReportViewer");
        map.put("sequenceDimensions", "sequenceDimensions");
        map.put("selectRoleGoToDesin", "selectRoleGoToDesin");
        map.put("goToCreateReportDesigner", "goToCreateReportDesigner");
        map.put("getRoleIdforDesigner", "getRoleIdforDesigner");
        map.put("savedesignerParamDimensions", "savedesignerParamDimensions");
        map.put("designerViewbys", "designerViewbys");
        map.put("refreshLocalHomePage", "refreshLocalHomePage");
        map.put("workBenchPage", "workBenchPage");
        map.put("createInsightDesigner", "createInsightDesigner");
        map.put("generateInsightTable", "generateInsightTable");
        map.put("changeParameters", "changeParameters");
        map.put("saveInsightWorkBench", "saveInsightWorkBench");
        map.put("deleteInsightWorkBench", "deleteInsightWorkBench");
        map.put("readExcelFileDetails", "readExcelFileDetails");
        map.put("addMoreFilters", "addMoreFilters");
        map.put("trendAnalysisAction", "trendAnalysisAction");
        map.put("trendAnalysisActionLocalEdit", "trendAnalysisActionLocalEdit");
        map.put("SaveDefaultTab", "SaveDefaultTab");
        map.put("GetDefaultTab", "GetDefaultTab");
        map.put("saveGlobalFilterOneview", "saveGlobalFilterOneview");
        map.put("getParameterdrill", "getParameterdrill");
        map.put("getMeasures1", "getMeasures1");
        map.put("GetAllfavReports", "GetAllfavReports");
        map.put("GetAllDB", "GetAllDB");
        map.put("GetAllOneView", "GetAllOneView");
        map.put("ParseNewdate", "ParseNewdate");
        map.put("saveResetGraph", "saveResetGraph");
        map.put("getResetGraph", "getResetGraph");
        map.put("goToCreateReportDesigner1", "goToCreateReportDesigner1");
        map.put("goToCreateReportDesigner2", "goToCreateReportDesigner2");
        map.put("selectRoleGoToDesin1", "selectRoleGoToDesin1");
        map.put("selectRoleGoToDesin2", "selectRoleGoToDesin2");
        map.put("buildParams1", "buildParams1");
        map.put("addMoreDimensions1", "addMoreDimensions1");
        map.put("getMeasures2", "getMeasures2");
        map.put("goToCreateReportTime", "goToCreateReportTime");
        map.put("getSelectedDims", "getSelectedDims");
        map.put("tableColumnProperties", "tableColumnProperties");
        //Added by Ram for language Lookup on 01Des2015
        map.put("unableLookup", "unableLookup");
        map.put("enableLookup", "enableLookup");
        map.put("getReportMultiCalendar", "getReportMultiCalendar");
        map.put("changeSelectedViewBy", "changeSelectedViewBy");
        map.put("changeEditViewBy", "changeEditViewBy");
        map.put("changeCompDate", "changeCompDate");
        map.put("saveShowHeader", "saveShowHeader");
        map.put("isDraggable", "isDraggable");
        map.put("getShowFilters", "getShowFilters");
        map.put("getRecentalyUsedUserReports", "getRecentalyUsedUserReports");//added by prabal for recently used reports to a user
        map.put("goToCreateAODesigner", "goToCreateAODesigner");
        map.put("loadAOUIComponents", "loadAOUIComponents");
        map.put("selectRoleGoToDesinAO", "selectRoleGoToDesinAO");
        map.put("addMoreDimensionsAO", "addMoreDimensionsAO");
        map.put("buildTableAO", "buildTableAO");
        map.put("dispAO", "dispAO");
        map.put("getMeasuresAO", "getMeasuresAO");
        map.put("buildParamsAO", "buildParamsAO");
        map.put("selectAoGoToDesin", "selectAoGoToDesin");
        map.put("addMoreDimensionsAoReport", "addMoreDimensionsAoReport");
        map.put("getAOMasterMap", "getAOMasterMap");
        map.put("removeSessionAttributes", "removeSessionAttributes");
        map.put("DeleteUserAOs", "DeleteUserAOs");
        map.put("tableviewbyColumnProperties","tableviewbyColumnProperties");
        map.put("loadAOMeasures","loadAOMeasures");
        map.put("loadPages","loadPages");
        return map;
    }
    public ActionForward loadAOMeasures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String html = reportTemplateDAO.loadAOMeasures(userId);
        session.setAttribute("templateName", request.getParameter("templateName"));
        session.setAttribute("templateDesc", request.getParameter("templateDesc"));
        request.setAttribute("measures", html);
        return mapping.findForward("selectManagementTemplateMeasures");
}
    public ActionForward loadPages(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String html = reportTemplateDAO.loadPages(userId);
        request.setAttribute("pageHtml", html);
        request.setAttribute("pages", "true");
        session.setAttribute("tempMeasures",request.getParameter("measures"));
        return mapping.findForward("selectManagementTemplateMeasures");
    }
}
