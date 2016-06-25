/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.kpi;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.progen.charts.JqplotGraphProperty;
import com.progen.charts.ProgenChartDatasets;
import com.progen.charts.ProgenChartDisplay;
import com.progen.contypes.GetConnectionType;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.dashboardView.db.DashboardViewerDAO;
import com.progen.report.DashletDetail;
import com.progen.report.KPIElement;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.entities.KPI;
import com.progen.report.entities.KPIComment;
import com.progen.report.pbDashboardCollection;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportview.db.CreateKPIFromReport;
import com.progen.scheduler.db.SchedulerDAO;
import com.progen.users.PrivilegeManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.OneViewLetDetails;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class KPIBuilder {

    public static Logger logger = Logger.getLogger(KPIBuilder.class);
    public HashMap kpiQuery = new LinkedHashMap();
    public HashMap kpiMasterQuery = new LinkedHashMap();
    public ArrayListMultimap<String, KPIElement> kpiElementList = ArrayListMultimap.create();
    public HashMap kpiCommentHashMap = new HashMap();
    String targetColor = "Black";
    private boolean SchedulerFlag = false;
    private String elemntIdForMail = "";
    private String viewbyvalue = "";
    private String display = "";
    private int colspan = 0;
    private Map<String, KPIGroupingHelper> KPIGroupHashMap = new HashMap<String, KPIGroupingHelper>();
    private HashMap<String, BigDecimal> grpMapValues = new HashMap<String, BigDecimal>();
    private List<String> atrelementIds = new ArrayList<String>();
    private List<String> symbols = new ArrayList<String>();
    private List<String> symbols1 = new ArrayList<String>();
    private List<String> alignment = new ArrayList<String>();
    private List<String> font = new ArrayList<String>();
    private List<String> numberFormat = new ArrayList<String>();
    private List<String> numberFormat1 = new ArrayList<String>();
    private List<String> round = new ArrayList<String>();
    private List<String> selectrepids = new ArrayList<String>();
    private List<String> background = new ArrayList<String>();
    private List<String> negativevalues = new ArrayList<String>();
    private List<ArrayList<String>> calculativeVal = new ArrayList<ArrayList<String>>();//Added by Ram
    private List<ArrayList<String>> gtVal = new ArrayList<ArrayList<String>>();//Added by Ram
    String isViewby = "";
    String isViewbySecond = "";
    private boolean checkformat;
    private boolean checkhead;
    StringBuilder DisplayGT = new StringBuilder();
    //added by sruthi for grand total
    public HashMap<String, ArrayList<String>> dataMap = new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> currlist = new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> budgetlist = new HashMap<String, ArrayList<String>>();
    public ArrayList<String> currbudgetlist = new ArrayList<String>();
    public ArrayList<String> currbudgetlistper = new ArrayList<String>();
    public HashMap<String, ArrayList<String>> lastcurrlist = new HashMap<String, ArrayList<String>>();
    public HashMap<String, ArrayList<String>> lastcurrlistper = new HashMap<String, ArrayList<String>>();
    public HashMap<String, BigDecimal> SummarizedMeasureTargetGTVals = new HashMap<String, BigDecimal>();
    //added by sruthi for hide columns
    public ArrayList<String> hidecolumns = new ArrayList<String>();
    int flag1 = 0;
    int flag2 = 0;
    int flag3 = 0;
    int flag4 = 0;
    int flag5 = 0;
    int flag6 = 0;
    int flag7 = 0;
    BigDecimal priorvalue = null;
    BigDecimal currentvalue = null;
    BigDecimal targetvalue = null;
    public int days = 0;
    public ArrayList<String> elementids = new ArrayList<String>();
    public HashMap<String, Double> manuvaltargetval = new HashMap<String, Double>();
//ended by sruthi

    public void getKpiForDesigner(String elementIds) throws Exception {

        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] dbColNames = null;
        String[] kpiCols = new String[elementIds.length()];
//        String kpiOrder = " Case ";
        StringBuilder kpiOrder = new StringBuilder(400);
        kpiOrder.append(" Case ");

        PbDb db = new PbDb();
        kpiCols = elementIds.split(",");
//        for (int i = kpiCols.length - 1; i >= 0; i--) {
//            kpiOrder = kpiOrder + "," + kpiCols[i] + "," + (i + 1);
//        }
//        kpiOrder = kpiOrder.substring(1);
        for (int i = kpiCols.length - 1; i >= 0; i--) {
//            kpiOrder += " when ELEMENT_ID = " + kpiCols[i] + " then " + (i + 1);
            kpiOrder.append(" when ELEMENT_ID = ").append(kpiCols[i]).append(" then ").append((i + 1));
        }
//        kpiOrder += " else 100000 end ";
        kpiOrder.append(" else 100000 end ");
        String sqlstr = " SELECT ELEMENT_ID,AGGREGATION_TYPE,case when USER_COL_DESC is null then USER_COL_NAME else USER_COL_DESC end USER_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS  where ELEMENT_ID in ( " + elementIds + " ) order by " + kpiOrder + " ";
        retObj = db.execSelectSQL(sqlstr);

//        finalQuery = sqlstr;
//        retObj = db.execSelectSQL(finalQuery);
        dbColNames = retObj.getColumnNames();

        for (int i = 0; i < retObj.getRowCount(); i++) {
            ArrayList kpiDetails = new ArrayList();

            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[0]));
            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[1]));
            kpiDetails.add(i);
            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[2]));
            kpiDetails.add("KPI");
            kpiDetails.add(1);
            kpiDetails.add("0");
            kpiQuery.put(kpiDetails.get(0).toString(), kpiDetails);

            //reportQryElementIds.add(retObj.getFieldValueString(i, dbColNames[0]));
        }
        kpiMasterQuery.put("1", kpiQuery);
        //processKpi("1");
    }

    public void getKpiForDesigner(String elementIds, HashMap kpiCommentMap) throws Exception {

        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] dbColNames = null;
        String[] kpiCols = new String[elementIds.length()];
        String sqlstr = "";
//        String kpiOrder = "";
        StringBuilder kpiOrder = new StringBuilder(300);
        PbDb db = new PbDb();
        kpiCols = elementIds.split(",");
//        String kpiCase = " case ";
        StringBuilder kpiCase = new StringBuilder(300);
        kpiCase.append(" case ");
        for (int i = kpiCols.length - 1; i >= 0; i--) {
//            kpiOrder = kpiOrder + "," + kpiCols[i] + "," + (i + 1);
            kpiOrder.append(",").append(kpiCols[i]).append(",").append((i + 1));
//            kpiCase += " when element_id =" + kpiCols[i] + " then " + (i + 1) + " ";
            kpiCase.append(" when element_id =").append(kpiCols[i]).append(" then ").append((i + 1)).append(" ");
        }
        kpiCase.append(" else 10000 end ");
//         kpiCase += " else 10000 end ";
//        kpiOrder = kpiOrder.substring(1);
        kpiOrder = new StringBuilder(kpiOrder.substring(1));

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            sqlstr += " SELECT ELEMENT_ID,AGGREGATION_TYPE,ISNULL(USER_COL_DESC,USER_COL_NAME) USER_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS ";
            sqlstr += " where ELEMENT_ID in ( " + elementIds + " ) ";
            sqlstr += " order by " + kpiCase;

        } else {
            sqlstr += " SELECT ELEMENT_ID,AGGREGATION_TYPE,nvl(USER_COL_DESC,USER_COL_NAME) USER_COL_NAME FROM PRG_USER_ALL_INFO_DETAILS ";
            sqlstr += " where ELEMENT_ID in ( " + elementIds + " ) ";
            sqlstr += " order by decode ( element_id," + kpiOrder + " )";
        }
        finalQuery = sqlstr;
        retObj = db.execSelectSQL(finalQuery);
        dbColNames = retObj.getColumnNames();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            ArrayList kpiDetails = new ArrayList();
            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[0]));
            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[1]));
            kpiDetails.add(i);
            kpiDetails.add(retObj.getFieldValueString(i, dbColNames[2]));
            kpiDetails.add("KPI");
            kpiDetails.add(1);
            kpiDetails.add("0");
            kpiQuery.put(kpiDetails.get(0).toString(), kpiDetails);
            //reportQryElementIds.add(retObj.getFieldValueString(i, dbColNames[0]));
        }
        kpiMasterQuery.put("1", kpiQuery);
        if (kpiCommentMap != null || !"".equals(kpiCommentMap)) {
            kpiCommentHashMap.put("kpiCommentMap", kpiCommentMap);
        }
        //processKpi("1");
    }

    public String processKpi(String kpiMasterId, String kpiType, pbDashboardCollection dashboardCollect, String userId) throws Exception {
        //////.println("in designer getCheckDashboardType()="+getCheckDashboardType());

        PbReportQuery repQuery = new PbReportQuery();
        PbReturnObject pbretObj = new PbReturnObject();
        PbReturnObject pbretObj1 = new PbReturnObject();
        Container container = null;

        kpiQuery = (LinkedHashMap) kpiMasterQuery.get(kpiMasterId);
        String[] a1 = (String[]) (kpiQuery.keySet()).toArray(new String[0]);

        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        ArrayList kpiElements = new ArrayList();
        String[] elements = new String[a1.length];
        PbReturnObject retObj = null;
        PbDb db = new PbDb();

//        String urlElements = "";
        StringBuilder urlElements = new StringBuilder(300);
        StringBuilder str = new StringBuilder(300);
//        String sqlstr = "";
        String finalQuery = "";
        String dispKpi = "";
        String[] ColNames = null;

        for (int j = 0; j < a1.length; j++) {
            ArrayList kpi = (ArrayList) kpiQuery.get(a1[j]);
            kpiElements.add(kpi.get(0).toString());
            elements[j] = kpi.get(0).toString();
            urlElements.append(urlElements).append(",").append(kpi.get(0).toString());
//            urlElements = urlElements + "," + kpi.get(0).toString();
        }
//        if (!urlElements.equalsIgnoreCase("")) {
//            urlElements = urlElements.substring(1);
//        }
        if (urlElements.length() > 0) {

            urlElements = new StringBuilder(urlElements.substring(1));
        }
        if (kpiType.equalsIgnoreCase("Target")) {
            if (dashboardCollect.getCheckDashboardType().equalsIgnoreCase("y")) {
                if (!dashboardCollect.isOneviewCheckForKpis()) {
                    dispKpi = "<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"Viewer_KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>";
                }
            } else {
                dispKpi = "<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>";
            }
            // dispKpi = "<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"KpiDrilldown('" + urlElements + "')\">KPI Drill</a></td></tr></table>";
            dispKpi += "<Table width=\"99%\"  cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\" class=\"tablesorter\" >";
            dispKpi += "<thead><tr><th width=\"170px\"><strong>KPI</strong></th><th width=\"150px\"><strong>Current/Prior</strong></th><th width=\"50px\"><strong>Change%</strong></th>";
            dispKpi += "<th width=\"20px\">&nbsp;</th><th width=\"100px\"><strong>Target Value</strong></th>";
            dispKpi += "<th width=\"70px\"><strong>Deviation%</strong></th><th width=\"180px\"><strong>Comments</strong></th>";
            dispKpi += "<th width=\"200px\"><strong>Graph</strong></th><th width=\"70px\"><strong>Alerts</strong></th></tr></thead><tfoot></tfoot><tbody>";
        } else {
            if (kpiType.equalsIgnoreCase("PortalKPI")) {
            } else {
                if (dashboardCollect.getCheckDashboardType().equalsIgnoreCase("y")) {
                    if (!dashboardCollect.isOneviewCheckForKpis()) {
                        dispKpi = "<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"Viewer_KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>";
                    } else {
                        dispKpi = "<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"Viewer_KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>";
                    }
                } else {
                    dispKpi = "<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>";
                }
            }

            //dispKpi = "<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"KpiDrilldown('" + urlElements + "')\">KPI Drill</a></td></tr></table>";
            if (kpiType.equalsIgnoreCase("PortalKPI")) {
                dispKpi += "<Table width=\"99%\"  cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\" class=\"tablesorter\" border=\"1\" >";
            } else {
                dispKpi += "<Table width=\"99%\"  cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\" class=\"tablesorter\" >";
            }
            dispKpi += "<thead><tr>";
            dispKpi += "<th ><strong>KPI</strong></th>";
            if (kpiType.equalsIgnoreCase("PortalKPI")) {

                dispKpi += "<th ><strong>Value</strong></th>";
            } else if (!(kpiType.equalsIgnoreCase("Kpi"))) {
                dispKpi += "<th ><strong>Current/Prior</strong></th><th ><strong>Change%</strong></th><th >&nbsp;</th><th ><strong>Comments</strong></th><th ><strong>Alerts</strong></th><th ></th>";
                if (!(kpiType.equalsIgnoreCase("Basic")) || !(kpiType.equalsIgnoreCase("BasicTarget"))) {
                    dispKpi += "<th><strong>Graphs</strong></th>";
                }
            } else {
                dispKpi += "<th ><strong>Current</strong></th><th ><strong>Prior</strong></th>";
            }
            dispKpi += "</tr></thead><tfoot></tfoot><tbody>";
        }
        for (int i = 0; i < a1.length; i++) {
            ArrayList kpi = (ArrayList) kpiQuery.get(a1[i]);
            QueryCols = new ArrayList();
            QueryAggs = new ArrayList();
            retObj = null;
//            sqlstr = "";
            {
//                sqlstr += "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE  from  PRG_USER_ALL_INFO_DETAILS  where ELEMENT_ID=" + kpi.get(0).toString() + " OR REF_ELEMENT_ID = " + kpi.get(0).toString() + " order by REF_ELEMENT_TYPE asc ";
//                finalQuery = sqlstr;
                finalQuery = "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE  from  PRG_USER_ALL_INFO_DETAILS  where ELEMENT_ID=" + kpi.get(0).toString() + " OR REF_ELEMENT_ID = " + kpi.get(0).toString() + " order by REF_ELEMENT_TYPE asc ";
                retObj = db.execSelectSQL(finalQuery);
                if (retObj != null && retObj.getRowCount() > 0) {
                    ColNames = retObj.getColumnNames();
                    for (int looper = 0; looper < retObj.getRowCount(); looper++) {
                        KPIElement kpiElem = new KPIElement();
                        String refElementId = retObj.getFieldValueString(looper, ColNames[1]);
                        kpiElem.setElementId(retObj.getFieldValueString(looper, ColNames[0]));
                        kpiElem.setRefElementId(refElementId);
                        kpiElem.setRefElementType(retObj.getFieldValueString(looper, ColNames[2]));
                        kpiElem.setAggregationType(retObj.getFieldValueString(looper, ColNames[3]));
                        kpiElementList.put(refElementId, kpiElem);

                        QueryCols.add(retObj.getFieldValueString(looper, ColNames[0]));//Query columns
                        QueryAggs.add(retObj.getFieldValueString(looper, ColNames[3])); //query Aggration

                    }
                } else {
                    QueryCols.add(kpi.get(0).toString());//Query columns
                    QueryAggs.add(kpi.get(1).toString()); //query Aggration
                }
            }
            repQuery = new PbReportQuery();
            repQuery.setRowViewbyCols(dashboardCollect.reportRowViewbyValues);
            //repQuery.setParamValue(reportParametersValues);//Added by k
            repQuery.setColViewbyCols(new ArrayList());
            repQuery.setQryColumns(QueryCols);
            repQuery.setColAggration(QueryAggs);
            repQuery.setTimeDetails(dashboardCollect.timeDetailsArray);
            repQuery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
            repQuery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
            repQuery.isKpi = true;
            pbretObj = repQuery.getPbReturnObject(String.valueOf(QueryCols.get(0)));//report kpi query which returns current,prior ,change and change%
//            str += displayKpi(container, pbretObj, kpi.get(0).toString(), kpi.get(3).toString(), kpi.get(6).toString(), kpiMasterId, "Y", kpiType, pbretObj1, null, false, dashboardCollect, userId);
            str.append(displayKpi(container, pbretObj, kpi.get(0).toString(), kpi.get(3).toString(), kpi.get(6).toString(), kpiMasterId, "Y", kpiType, pbretObj1, null, false, dashboardCollect, userId));
        }
        dispKpi += str;
        dispKpi += "</tbody></Table>";
        repQuery.isKpi = false;
        return dispKpi;
    }

    public String processSingleKpi(Container container, String kpiMasterId, HashMap kpiQuery, String kpiDrill, String kpidashid, String dashBoardId, boolean forDesigner, pbDashboardCollection dashboardcollect, String userId, String editDbrd) throws Exception {
        dashboardcollect.setCheckDashboardType("Y");
        ArrayList<String> hidecolumnsdata = new ArrayList<String>();
        ArrayList ViewBy = container.getViewBy();
        boolean isViewByAdd = false;
        boolean isGtShow = false;
        DashletDetail detail = dashboardcollect.getDashletDetail(kpidashid);
        hidecolumns = detail.getHidecolumns();
        Gson gson = new Gson();
        String stockArr = gson.toJson(hidecolumns);
        if (!kpiMasterId.equalsIgnoreCase("0")) {
            kpiMasterId = detail.getKpiMasterId();
        }
        KPI kpiDetails = (KPI) detail.getReportDetails();
        ArrayListMultimap<String, KPIElement> kpiElementMap = kpiDetails.getKPIElementsMap();
        List<String> a1 = kpiDetails.getElementIds();
        String BizRoles = getbusinessroles(dashBoardId);
        String dashId = detail.getDashBoardDetailId();
        List<DashletDetail> dashletDetails = dashboardcollect.dashletDetails;
        if (!forDesigner || (kpiDrill.equalsIgnoreCase("Y"))) {
            DashletDetail dashlettemp = null;
            for (DashletDetail dashletDetail : dashletDetails) {
                if (dashletDetail.getKpiMasterId() != null && dashletDetail.getKpiMasterId().equalsIgnoreCase(kpiMasterId)) {
                    dashlettemp = dashletDetail;
                    break;
                }
            }
            if (dashlettemp == null) {
                dashlettemp = new DashletDetail();
            }
            List<KPISingleGroupHelper> singleGroupHelpers = dashlettemp.getSingleGroupHelpers();
            HashMap<String, HashMap> map = new HashMap();
            for (int k = 0; k < a1.size(); k++) {
                DashboardTemplateDAO dao = new DashboardTemplateDAO();
                String attrchange = a1.get(k);
                HashMap modifyMeasureAttr = new HashMap();
                if (!detail.isGroupElement(attrchange)) {
                    modifyMeasureAttr = dao.getModifyMeasureattr(attrchange);
                }
                map.put(attrchange, modifyMeasureAttr);
            }
            detail.setmodifymeasureAttrChnge(map);

            if (!singleGroupHelpers.isEmpty()) {
                atrelementIds = singleGroupHelpers.get(0).getAtrelementIds();
                for (int i = 0; i < atrelementIds.size(); i++) {
                    HashMap<String, String> map2 = map.get(atrelementIds.get(i));
                    if (!singleGroupHelpers.get(0).getSymbols().get(i).equalsIgnoreCase("")) {
                        symbols.add(singleGroupHelpers.get(0).getSymbols().get(i));
                        symbols1.add(singleGroupHelpers.get(0).getSymbols().get(i));
                    } else {
                        if (map2 != null) {
                            symbols.add(map2.get("symbol"));
                            symbols1.add("");
                        } else {
                            symbols.add("");
                        }
                    }
                    if (!singleGroupHelpers.get(0).getNumberFormat().get(i).equalsIgnoreCase("")) {
                        numberFormat.add(singleGroupHelpers.get(0).getNumberFormat().get(i));
                    } else {
                        if (map2 != null) {
                            numberFormat.add(map2.get("no_format"));
                        } else {
                            numberFormat.add("");
                        }
                    }
                    if (!singleGroupHelpers.get(0).getRound().get(i).equalsIgnoreCase("")) {
                        round.add(singleGroupHelpers.get(0).getRound().get(i));
                    } else {
                        if (map2 != null) {
                            round.add(map2.get("round"));
                        } else {
                            round.add("");
                        }
                    }

                }
//                symbols=singleGroupHelpers.get(0).getSymbols();
                alignment = singleGroupHelpers.get(0).getAlignment();
                font = singleGroupHelpers.get(0).getFont();
//                numberFormat=singleGroupHelpers.get(0).getNumberFormat();
//                round=singleGroupHelpers.get(0).getRound();
                checkformat = singleGroupHelpers.get(0).getcheckformat();
                checkhead = singleGroupHelpers.get(0).getcheckhead();
                numberFormat1 = singleGroupHelpers.get(0).getgblFormatList();
                selectrepids = singleGroupHelpers.get(0).getselectrepIds();
                background = singleGroupHelpers.get(0).getBackGround();
                negativevalues = singleGroupHelpers.get(0).getNegativevalue();
                DashletDetail dashlet = Iterables.find(dashletDetails, DashletDetail.getDashletDetailPredicate(kpiMasterId));
                for (int j = 0; j < a1.size(); j++) {
                    if (!atrelementIds.contains(a1.get(j))) {

                        HashMap<String, String> map2 = map.get(a1.get(j));
                        atrelementIds.add(a1.get(j));
                        numberFormat.add(map2.get("no_format"));
                        symbols1.add("");
                        symbols.add(map2.get("symbols"));
                        round.add(map2.get("round"));
                        alignment.add("");
                        font.add("");
                        numberFormat1.add("");
                        selectrepids.add("");
                        background.add("");
                        negativevalues.add("");
                        KPISingleGroupHelper kpiGrouphelper = null;
                        if (!singleGroupHelpers.isEmpty()) {
                            kpiGrouphelper = singleGroupHelpers.get(0);
                        } else {
                            kpiGrouphelper = new KPISingleGroupHelper();
                        }
                        kpiGrouphelper.setAtrelementIds(atrelementIds);
                        kpiGrouphelper.setSymbols(symbols1);
                        kpiGrouphelper.setAlignment(alignment);
                        kpiGrouphelper.setFont(font);
                        kpiGrouphelper.setNumberFormat(numberFormat);
                        kpiGrouphelper.setRound(round);
                        kpiGrouphelper.setBackGround(background);
                        kpiGrouphelper.setcheckformat(checkformat);
                        kpiGrouphelper.setcheckhead(checkhead);
                        kpiGrouphelper.setgblFormatList(numberFormat1);
                        kpiGrouphelper.setselectrepIds(selectrepids);
                        kpiGrouphelper.setNegativevalue(negativevalues);
                        singleGroupHelpers.set(0, kpiGrouphelper);
                        dashlet.setSingleGroupHelpers(singleGroupHelpers);

                    }
//               KPISingleGroupHelper kpiGrouphelper= null;
//             kpiGrouphelper.setSymbols(symbols);  //bhargavi

                }

            } else if (map != null && map.size() != 0) {
                for (int i = 0; i < a1.size(); i++) {
                    HashMap<String, String> map2 = map.get(a1.get(i));
                    atrelementIds.add(a1.get(i));
                    numberFormat.add(map2.get("no_format"));
                    symbols.add(map2.get("symbols"));
                    round.add(map2.get("round"));
                    alignment.add("");
                    font.add("");
                    numberFormat1.add("");
                    selectrepids.add("");
                    background.add("");
                    negativevalues.add("");

                }
            } else {
                atrelementIds.clear();
                symbols.clear();
                alignment.clear();
                font.clear();
                numberFormat.clear();
                round.clear();
                selectrepids.clear();
                background.clear();
                negativevalues.clear();
            }
        }
        ArrayListMultimap<Integer, Integer> rowinfo = ArrayListMultimap.create();
        for (int i = 0; i < dashletDetails.size(); i++) {
            DashletDetail dashlet = dashletDetails.get(i);
            rowinfo.put(dashlet.getRow(), dashlet.getCol());
        }
        List<Integer> dashlets = rowinfo.get(Integer.parseInt(dashId));
        int numOfDashlets = dashlets.size();

        int row = detail.getRow();
        int col = detail.getCol();
        int rowSpan = detail.getRowSpan();
        int colSpan = detail.getColSpan();
        String kpiName = detail.getkpiName();
        Boolean editFlag = detail.getEditFlag();
//        String kpiSymbol=detail.getKpiSymbol();
        int width = 500;
        int height = 330;
        width = width * colSpan;
        height = height * rowSpan;
        if (checkformat == true) {
            String value = "";
            if (numberFormat1 != null) {
                value = numberFormat1.toString();
                if (value.contains("M")) {
                    value = "( Millions )";
                } else if (value.contains("L")) {
                    value = "( Lacs )";
                } else if (value.contains("C")) {
                    value = "( Crores )";
                } else if (value.contains("K")) {
                    value = "(Thousands )";
                } else if (value.contains("")) {
                    value = "";
                }

            }
            kpiName = kpiName + value;
        }

        StringBuilder bizRolesString = new StringBuilder();
        StringBuilder kpiheads = new StringBuilder();
        ArrayList<String> kpiheadsdets = new ArrayList<String>();
        String formatvalue = "";
        if (checkhead == true) {
            if (numberFormat1 != null) {
                formatvalue = numberFormat1.toString();
                if (formatvalue.contains("M")) {
                    formatvalue = "( Millions )";
                } else if (formatvalue.contains("L")) {
                    formatvalue = "( Lacs )";
                } else if (formatvalue.contains("C")) {
                    formatvalue = "( Crores )";
                } else if (formatvalue.contains("K")) {
                    formatvalue = "( Thousands )";
                } else if (formatvalue.contains("")) {
                    formatvalue = "";
                }

            }

        }
        if (detail.getKpiheads().isEmpty() || detail.getKpiheads().get(0).equalsIgnoreCase("")) {
            if (detail.getKpiType().equalsIgnoreCase("Standard")) {
                kpiheads.append(",").append("KPI");
                if (checkhead == true) {
                    kpiheads.append(",").append("Current/Prior").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Current/Prior");
                }
                kpiheads.append(",").append("Change%");
                if (!dashboardcollect.isOneviewCheckForKpis()) {
                    kpiheads.append(",").append("Insights");
                    kpiheads.append(",").append("Comments");
                    kpiheads.append(",").append("Alerts");
                }

            } else if (detail.getKpiType().equalsIgnoreCase("MultiPeriod")) {
                kpiheads.append(",").append("KPI");
                kpiheads.append(",").append("Current Day");
                if (checkhead == true) {
                    kpiheads.append(",").append("MTD").append(formatvalue);
                    kpiheads.append(",").append("QTD").append(formatvalue);
                    kpiheads.append(",").append("YTD").append(formatvalue);
                } else {
                    kpiheads.append(",").append("MTD");
                    kpiheads.append(",").append("QTD");
                    kpiheads.append(",").append("YTD");
                }
                kpiheads.append(",").append("Alerts");
            } else if (detail.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                kpiheads.append(",").append("KPI");
                if (checkhead == true) {
                    kpiheads.append(",").append("MTD Current").append(formatvalue);
                } else {
                    kpiheads.append(",").append("MTD Current");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("Target").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Target");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("MTD Prior").append(formatvalue);
                } else {
                    kpiheads.append(",").append("MTD Prior");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("Deviation").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Deviation");
                }
                kpiheads.append(",").append("Deviation%");
                if (checkhead == true) {
                    kpiheads.append(",").append("MTD Change").append(formatvalue);
                } else {
                    kpiheads.append(",").append("MTD Change");
                }
                kpiheads.append(",").append("MTD Change%");
                if (checkhead == true) {
                    kpiheads.append(",").append("QTD Current").append(formatvalue);
                } else {
                    kpiheads.append(",").append("QTD Current");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("Target").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Target");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("QTD Prior").append(formatvalue);
                } else {
                    kpiheads.append(",").append("QTD Prior");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("Deviation").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Deviation");
                }
                kpiheads.append(",").append("Deviation%");
                if (checkhead == true) {
                    kpiheads.append(",").append("QTD Change").append(formatvalue);
                } else {
                    kpiheads.append(",").append("QTD Change");
                }
                kpiheads.append(",").append("QTD Change%");
                if (checkhead == true) {
                    kpiheads.append(",").append("YTD Current").append(formatvalue);
                } else {
                    kpiheads.append(",").append("YTD Current");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("Target").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Target");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("YTD Prior").append(formatvalue);
                } else {
                    kpiheads.append(",").append("YTD Prior");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("Deviation").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Deviation");
                }
                kpiheads.append(",").append("Deviation%");
                if (checkhead == true) {
                    kpiheads.append(",").append("YTD Change").append(formatvalue);
                } else {
                    kpiheads.append(",").append("YTD Change");
                }
                kpiheads.append(",").append("YTD Change%");

            } else if (detail.getKpiType().equalsIgnoreCase("Target")) {
                if (!ViewBy.isEmpty()) {
                    for (int i = 0; i < ViewBy.size(); i++) {
                        if (ViewBy.get(i) != "") {
                            kpiheads.append(",").append(dashboardcollect.reportParameters.get(ViewBy.get(i)).get(1));
                        }
                    }
                }
                kpiheads.append(",").append("KPI");
                if (checkhead == true) {
//                    kpiheads.append(",").append("Current/Prior").append(formatvalue);
                    kpiheads.append(",").append("LY");
                } else {
                    kpiheads.append(",").append("LY");
                }
                kpiheads.append(",").append("BUD");
//                         if(checkhead==true){
//                    kpiheads.append(",").append("Target Value").append(formatvalue);
//                    }else
                kpiheads.append(",").append("TY");
                kpiheads.append(",").append("Var-Bud");
                kpiheads.append(",").append("Var-Bud%");//changed by sruthi fro hide columns
                kpiheads.append(",").append("Var-Ly");
                kpiheads.append(",").append("Var-Ly%");//changed by sruthi fro hide columns
//                    if(!dashboardcollect.isOneviewCheckForKpis()){
//                    kpiheads.append(",").append("Insights");
//                    kpiheads.append(",").append("Comments");
//                    kpiheads.append(",").append("Graph");
                kpiheads.append(",").append("Alerts");
                kpiheads.append(",").append("Graph");
//                    }

            } else if (detail.getKpiType().equalsIgnoreCase("Basic")) {
                kpiheads.append(",").append("KPI");
                if (checkhead == true) {
                    kpiheads.append(",").append("Value").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Value");
                }
                if (!dashboardcollect.isOneviewCheckForKpis()) {
                    kpiheads.append(",").append("Insights");
                    kpiheads.append(",").append("Comments");
                    kpiheads.append(",").append("Alerts");
                }
            } else {
                kpiheads.append(",").append("KPI");
                if (checkhead == true) {
                    kpiheads.append(",").append("Value").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Value");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("Target").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Target");
                }
                if (checkhead == true) {
                    kpiheads.append(",").append("Deviation").append(formatvalue);
                } else {
                    kpiheads.append(",").append("Deviation");
                }
                kpiheads.append(",").append("Deviation%");
                if (!dashboardcollect.isOneviewCheckForKpis()) {
                    kpiheads.append(",").append("Insights");
                    kpiheads.append(",").append("Comments");
                    kpiheads.append(",").append("Alerts");
                }
            }
        } else {
            if (detail.getKpiType().equalsIgnoreCase("Target")) {
                if (!ViewBy.isEmpty()) {
                    for (int i = 0; i < ViewBy.size(); i++) {
                        if (ViewBy.get(i) != "") {
                            kpiheads.append(",").append(dashboardcollect.reportParameters.get(ViewBy.get(i)).get(1));
                        }
                    }
                }
                if (detail.getKpiheads().size() > 10) {
                    for (int i = detail.getKpiheads().size() - 10; i < detail.getKpiheads().size(); i++) {
                        kpiheads.append(",").append(detail.getKpiheads().get(i));   // added by mohit

                    }
                } else {
                    for (int i = 0; i < detail.getKpiheads().size(); i++) {
                        kpiheads.append(",").append(detail.getKpiheads().get(i));   // added by mohit

                    }
                }

            } else {
                for (int i = 0; i < detail.getKpiheads().size(); i++) {
                    if (checkhead && !detail.getKpiheads().get(i).equalsIgnoreCase("KPI") && !detail.getKpiheads().get(i).contains("%")) {
                        kpiheads.append(",").append(detail.getKpiheads().get(i)).append(formatvalue);
                    } else {
                        kpiheads.append(",").append(detail.getKpiheads().get(i));
                    }
                }
            }
//                for(int i=0;i<detail.getKpiheads().size();i++){
//                    //sandeep
//                    if(detail.getKpiType().equalsIgnoreCase("Target")){
//                    if(ViewBy.isEmpty()){
//                 if(i==0){   if(!detail.getKpiheads().get(i).equalsIgnoreCase("KPI")){ i++;}
//                  if(i==1 && !detail.getKpiheads().get(i).equalsIgnoreCase("KPI")){  i++;}}
//             }else{
//                          if(i==0){
//                        for(int i1=0;i1<ViewBy.size();i1++){
//                             if(ViewBy.get(i1)!=""){
//                    kpiheads.append(",").append(dashboardcollect.reportParameters.get(ViewBy.get(i1)).get(1));
//                             }
//             }
//                          if(i==0){   if(!detail.getKpiheads().get(i).equalsIgnoreCase("KPI")){ i++;}
//                  if(i==1 && !detail.getKpiheads().get(i).equalsIgnoreCase("KPI")){  i++;}}
//                        }                                                                        //commented by mohit
//                        }
//
//                                if(checkhead && !detail.getKpiheads().get(i).equalsIgnoreCase("KPI") &&!detail.getKpiheads().get(i).contains("%") )
//                       {
//                    kpiheads.append(",").append(detail.getKpiheads().get(i)).append(formatvalue);
//                           }
//                       else
//                        kpiheads.append(",").append(detail.getKpiheads().get(i));
//                    // end of sandeep code for headerkpi name changing.
//                    }else{
//                                if(checkhead && !detail.getKpiheads().get(i).equalsIgnoreCase("KPI") &&!detail.getKpiheads().get(i).contains("%") )
//                       {
//                    kpiheads.append(",").append(detail.getKpiheads().get(i)).append(formatvalue);
//                }
//                       else
//                        kpiheads.append(",").append(detail.getKpiheads().get(i));
//                }
//
//                }

        }
        kpiheads.replace(0, 1, "");
        String customkpiheads = kpiheads.toString();
        String[] Kpiheadarray = customkpiheads.split(",");
        String folderDetails = dashboardcollect.reportBizRoles[0];
        if (dashboardcollect.reportBizRoles != null) {
            for (String role : dashboardcollect.reportBizRoles) {
                bizRolesString.append(",").append(role);
            }
        }
        ArrayList<String> customkpiheaderdata = new ArrayList<String>();
        for (int i = ViewBy.size() + 1; i < ViewBy.size() + 8; i++) {
            customkpiheaderdata.add(Kpiheadarray[i]);
        }
        if (bizRolesString.length() > 0) {
            bizRolesString.replace(0, 1, "");
        }
        String kpiType = detail.getKpiType();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        HashMap<String, String> aggid = new HashMap<String, String>();//added by sruthi for gt
        StringBuilder kpiElementIdsString = new StringBuilder();
        StringBuilder kpiElementsname = new StringBuilder();
        for (String elementids : a1) {
            kpiElementIdsString.append(",").append(elementids);
            // 
            List<KPIElement> kpiElems = kpiElementMap.get(elementids);
            HashMap<String, HashMap> msrChange = detail.getmodifymeasureAttrChng();
            if (msrChange != null && msrChange.size() != 0 && msrChange.get(elementids) != null) {
                HashMap<String, String> elementChange = msrChange.get(elementids);
                if (elementChange != null && elementChange.size() != 0) {
                    String elementname = elementChange.get("elementname");
                    String DefaultAggr = elementChange.get("aggregation");
                    kpiElementsname.append(elementname).append(",");
                    QueryCols.add(elementids);
                    QueryAggs.add(DefaultAggr);
                    aggid.put(elementids, DefaultAggr);//added by  sruthi for gt
                }
            } else {
                if (kpiElems != null) {
                    for (KPIElement elem : kpiElems) {
                        if (elem.getElementName() != null) {
                            kpiElementsname.append(elem.getElementName()).append(",");
                        }
                        // 
                        QueryCols.add(elem.getElementId());
                        QueryAggs.add(elem.getAggregationType());
                    }
                }
            }

        }
        //added by Dinanath for setting element id and name of measure
        container.setKPIElementIds(kpiElementIdsString.toString());
        container.setKPIElementMesureName(kpiElementsname.toString());
        container.setCustomKPIHeaderNames(customkpiheads.toString());
        //end of code by dinanath
        if (kpiElementIdsString.length() > 0) {
            kpiElementIdsString.replace(0, 1, "");
        }

        PbReturnObject pbretObj = new PbReturnObject();
        PbReturnObject pbretObjForTime = null;

        StringBuilder urlElements = new StringBuilder();
        StringBuilder ElementStatus = new StringBuilder();
        StringBuilder tableBuffer = new StringBuilder();
        for (int j = 0; j < a1.size(); j++) {
            urlElements.append(",").append(a1.get(j));
            ElementStatus.append(",").append(detail.isGroupElement(a1.get(j)));
        }
//        

// For Designer And Viewer
        String InsightChecked = " ";
        String CommentChecked = "";
        String GraphChecked = "";
        String MTDChecked = "";
        String QTDChecked = "";
        String YTDChecked = "";
        String CurrentChecked = "";
        boolean isPowerAnalyserEnableforUser = container.isPowerAnalyserEnableforUser;

        if (kpiDetails.isShowInsights()) {
            InsightChecked = "checked";

        }
        if (kpiDetails.isShowComments()) {
            CommentChecked = "checked";
        }
        if (kpiDetails.isShowGraphs()) {
            GraphChecked = "checked";
        }
        if (kpiDetails.isMTDChecked()) {
            MTDChecked = "checked";
        }
        if (kpiDetails.isQTDChecked()) {
            QTDChecked = "checked";
        }
        if (kpiDetails.isYTDChecked()) {
            YTDChecked = "checked";
        }
        if (kpiDetails.isCurrentChecked()) {
            CurrentChecked = "checked";
        }
        if (!dashboardcollect.isOneviewCheckForKpis()) {
            //commented by veena
//        tableBuffer.append("<div class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:100%; height:100% \">");
            tableBuffer.append("<div  style=\"width:100%; height:800px \">");
            tableBuffer.append("<div id=\"innerDivKpiId\"  class=\"portlet-header1 navtitle portletHeader ui-corner-all\">");
            tableBuffer.append("<table width=\"100%\"><tr>");
//        if(kpiName.equalsIgnoreCase("") || kpiName == null)
//        tableBuffer.append("<td align='left'><a  title=\"Dashlet Rename\" onclick=\"DashletRename("+kpiMasterId+",'"+kpiName+"')\"><strong>KPI Region</strong></a></td>");
//         else

            if (isPowerAnalyserEnableforUser) {
                tableBuffer.append("<td id=\"kpiregion\" align='left'><a title=\"Dashlet Rename\" onclick=\"DashletRename(" + kpiMasterId + ",'" + kpiName + "','" + dashBoardId + "','" + kpidashid + "','" + forDesigner + "')\"><strong>" + kpiName + "</strong></a></td>");
            }

            if (kpiType.equalsIgnoreCase("Standard") || kpiType.equalsIgnoreCase("Target") || kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("MultiPeriod") || kpiType.equalsIgnoreCase("Kpi") || kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                if (!SchedulerFlag) {
                    tableBuffer.append("<td align='right'>");
                    tableBuffer.append("<table><tr>");
//            if(!(kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior"))){

                    // added for local save(save now) purpose by anitha :start
                    if (isPowerAnalyserEnableforUser) {
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon  ui-icon-disk\" title=\"Save Now\" href=\"javascript:void(0)\" frameborder=\"0\" onclick=\"localSaveInDbRegions(" + dashBoardId + "," + kpidashid + "," + kpiMasterId + ",'" + kpiType + "','" + kpiElementIdsString + "','" + kpiElementsname + "')\" ></a> </td>");
                    }
                    //by anitha :End

                    tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon  ui-icon-bookmark\" title=\"Export to Excel\" href=\"javascript:void(0)\" SRC=\"TableDisplay/pbDownload.jsp\" frameborder=\"0\" onclick=\"exportToExcel(" + dashBoardId + "," + kpidashid + "," + kpiMasterId + ",'" + kpiElementIdsString + "','" + kpiElementsname + "')\" ></a> </td>");
                    // added by ramesh janakuttu
                    tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon ui-icon-arrowthick-2-n-s\" title=\"ReOrder KPIs\" href=\"javascript:void(0)\" onclick=\"addAllKpis(" + dashBoardId + "," + kpidashid + "," + kpiMasterId + ",'" + kpiElementIdsString + "','" + kpiElementsname + "','" + kpiType + "')\" ></a> </td>");
                    // ended by ramesh janakuttu
                    if (isPowerAnalyserEnableforUser) // tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;<td><td align='left'><a  class=\" ui-icon ui-icon-plusthick\" title=\"Group KPIs\"  onclick=\"GroupKPI("+dashBoardId+","+kpidashid+","+kpiMasterId+ ","+folderDetails+ ",'"+kpiType+ "')\"></a></td>");
                    // tableBuffer.append("<td align='left'><a  title=\"Add Attributes\" onclick=\"addAttributes("+dashBoardId+","+kpidashid+","+kpiMasterId+")\"><strong>Attributes</strong></a></td>");
                    //                }
                    {
                        if (isPowerAnalyserEnableforUser) {
                            tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td align='left'><a href=\"javascript:void(0)\" onclick=\"RenameKpiHeading('" + kpidashid + "','" + dashBoardId + "','" + kpiMasterId + "','" + kpiheads + "')\"><strong>KPIHeadsRename</strong></a></td>");
                        }
                    }
                    //tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td align='left'><a href=\"javascript:void(0)\" onclick=\"Hidemeasures('"+kpidashid+"','"+dashBoardId+"','"+kpiMasterId+"','"+kpiheads+"')\"><strong>Show/HideMeasure</strong></a></td>");
                    tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td id=\"attributes\" align='left'><a  title=\"Add Attributes\" onclick=\"addAttributes('").append(dashBoardId).append("','").append(kpidashid).append("','").append(kpiMasterId).append("','").append(kpiElementIdsString).append("','").append(kpiElementsname).append("','" + kpiType + "')\"><strong>Attributes</strong></a></td>");
                    tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td align='left'><a  class =\" ui-icon ui-icon-clock\" title=\"Time Info\" onclick=\"getDbrdTimeDisplay('" + dashBoardId + "')\"></a></td>");

                    if (isPowerAnalyserEnableforUser) {
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon ui-icon-pencil\" title=\"Edit KPIs\" href=\"javascript:void(0)\" onclick=\"addMoreKpis(" + dashBoardId + "," + kpidashid + "," + kpiMasterId + "," + folderDetails + ",'" + kpiType + "')\" ></a> </td>");
                    }
                    if (isPowerAnalyserEnableforUser) {
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a  class=\"ui-icon ui-icon-triangle-2-n-s\" title=\"KPI Drill\" onclick=\"KpiDrilldown('").append(urlElements.toString()).append("','").append(bizRolesString.toString()).append("','").append(dashboardcollect.reportId).append("','").append(detail.getDashBoardDetailId()).append("','").append(detail.getKpiMasterId()).append("','").append(forDesigner).append("','" + ElementStatus.toString() + "','" + kpiElementsname + "')\"></a></td>");
                    }
                    // tableBuffer.append("<td ><a  title=\"Attributes\" href=\"javascript:void(0)\" onclick=\"addAttributes("+dashBoardId+","+kpidashid+","+kpiMasterId+ ","+folderDetails+ ",'"+kpiType+ "')\" >  Attributes</a> </td>");
                    tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a  href=\"javascript:void(0)\" title=\"Edit Viewby\" class=\"ui-icon ui-icon-newwin\" frameborder=\"0\" onclick=\"editViewBydash()\"  ></a> </td>");
                    tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td align='left'><a href=\"javascript:void(0)\" title=\"	Show/HideColumns\"  class=\"ui-icon ui-icon-refresh\" onclick=\"HideColumns('" + kpidashid + "','" + dashBoardId + "','" + hidecolumns + "','" + customkpiheaderdata + "')\"></a></td>");//added  by sruthi for hide columns

                    if (!SchedulerFlag) {
                        if (isPowerAnalyserEnableforUser) {
                            tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a  title=\"KPI Rename\" onclick=\"kpiRename(" + kpiMasterId + "," + dashBoardId + "," + kpidashid + ")\">Rename</a></td>");
                        }
                    }
                    if ((kpiType.equalsIgnoreCase("BasicTarget"))) {
                        if (!SchedulerFlag) {
                            tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a title=\"KPI Type\" onclick=\"KpiType(" + kpiMasterId + "," + kpidashid + "," + dashboardcollect.reportId + ")\">Type</a></td>");
                        }
                    }
                    if (forDesigner && (editFlag == null || !editFlag)) {
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td> <img style=\"width:20px;\"  src='images/clear-icon.png' onclick=\"clearDashlet('" + dashId + "','" + numOfDashlets + "','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "')\" /></td>");
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + kpidashid + "','" + kpidashid + "')\" ></a></td>");
                    } else if ("true".equals(editDbrd)) {
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td> <img style=\"width:20px;\"  src='images/clear-icon.png' onclick=\"clearDashlet('" + dashId + "','" + numOfDashlets + "','" + row + "','" + col + "','" + rowSpan + "','" + colSpan + "')\" /></td>");
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + kpidashid + "','" + kpidashid + "')\" ></a></td>");
                    }
                    tableBuffer.append("</tr></table></td>");
                }
            } else {
                if (!SchedulerFlag) {
                    if (forDesigner) {
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td width=\"10%\" align=\"right\"><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + kpidashid + "','" + kpidashid + "')\" ></a></td>");
                        tableBuffer.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td width=\"10%\" > <a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + kpidashid + "','" + kpidashid + "')\" ></a></td>");
                    }
                }
            }

            tableBuffer.append("</tr></table>");
            tableBuffer.append("</div>");
        }
        if (!SchedulerFlag) {
            tableBuffer.append("<div><table width=\"100%\"><tr>");

            tableBuffer.append("<td align=\"left\" width=\"50%\" >");
//        tableBuffer.append("<input type=\"checkbox\" id=\"insights_").append(kpidashid).append("\" name = \"insights\" value=\"insights\" onclick=\"changeInsightStatus(\'").append(kpidashid).append("\')\"").append(InsightChecked).append(">Insights").append("&nbsp");
//        tableBuffer.append("<input type=\"checkbox\"  id=\"comments_").append(kpidashid).append( "\"name = \"comments\" value=\"comments\" onclick=\"changeCommentStatus(\'").append(kpidashid).append("\')\"").append(CommentChecked).append(">Comments").append("</td>");

//            if (!forDesigner){
//            tableBuffer.append("<div id=\"innerDivKpiId\"  class=\"portlet-header1 navtitle portletHeader ui-corner-all\">");
//            tableBuffer.append("<table width=\"100%\"><tr>");
//            tableBuffer.append("<td width=\"74%\"><strong>" + detail.getDashletName() + "</strong></td>");
//            if(kpiType.equalsIgnoreCase("Standard")||kpiType.equalsIgnoreCase("Target")){
//                tableBuffer.append("<td width=\"30%\" valign=\"top\" align=\"left\"> <a  href=\"javascript:void(0)\"  onclick=\"addMoreKpis("+dashBoardId+",'"+kpidashid+"',"+kpiMasterId+ ")\" ><strong>Edit More KPIs </strong> </a> </td>");
//                tableBuffer.append("<td width=\"10%\" > <a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-" + kpidashid + "','"+kpidashid+"')\" ></a></td>");
//            }
//            else
//            {
//                tableBuffer.append("<td width=\"10%\" align=\"right\"><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  onclick=\"closeOldPortlet('Dashlets-"+kpidashid+"','"+kpidashid+"')\" ></a></td>");
//            }
//
//            tableBuffer.append("</tr></table>");
//            tableBuffer.append("</div>");
//        }
//        tableBuffer.append("<div><table width=\"100%\"><tr><td align=\"left\" width=\"10%\"><a onclick=\"KpiDrilldown('").append(kpiElementIdsString.toString()).append("','").append(bizRolesString.toString()).append("','").append(dashboardcollect.reportId).append("','").append(detail.getDashBoardDetailId()).append("','").append(detail.getKpiMasterId()).append("')\">KPI Drill</a></td>");
            if (forDesigner) {
                if (!(kpiType.equalsIgnoreCase("MultiPeriod") || kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior"))) {
                    if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget"))) {
                        tableBuffer.append("<td align=\"left\" width=\"50%\" >");
                        if (editFlag == null || !editFlag) {
                            tableBuffer.append("<input type=\"checkbox\" id=\"insights_").append(kpidashid).append("\" name = \"insights\" value=\"insights\" onclick=\"changeInsightStatus(\'").append(kpidashid).append("\')\"").append(InsightChecked).append(">Insights").append("&nbsp");
                            tableBuffer.append("<input type=\"checkbox\"  id=\"comments_").append(kpidashid).append("\"name = \"comments\" value=\"comments\" onclick=\"changeCommentStatus(\'").append(kpidashid).append("\')\"").append(CommentChecked).append(">Comments").append("&nbsp");
                            if (kpiType.equalsIgnoreCase("Target")) {
                                tableBuffer.append("<input type=\"checkbox\"  id=\"graphs_").append(kpidashid).append("\"name = \"graphs\" value=\"graphs\" onclick=\"changeGraphStatus(\'").append(kpidashid).append("\')\"").append(GraphChecked).append(">Graphs").append("</td>");
                            }
                        }
                    }
                }
                if (kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior") || kpiType.equalsIgnoreCase("MultiPeriod")) {
                    if (editFlag == null || !editFlag) {
                        tableBuffer.append("<td align=\"left\" width=\"50%\" >");
//                  if(kpiType.equalsIgnoreCase("MultiPeriod")){
                        tableBuffer.append("<input type=\"checkbox\" id=\"CURRENT_").append(kpidashid).append("\" name = \"CURRENT\" value=\"CURRENT\" onclick=\"changeCurrentStatus(\'").append(kpidashid).append("\')\"").append(CurrentChecked).append(">Current").append("&nbsp");
//                  }
//                  if(kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")){
//                   tableBuffer.append("<input type=\"hidden\" id=\"CURRENT_").append(kpidashid).append("\" name = \"CURRENT\" onclick=\"changeCurrentStatus(\'").append(kpidashid).append("\')\"").append(CurrentChecked).append("&nbsp");
//                  }
                        tableBuffer.append("<input type=\"checkbox\" id=\"MTD_").append(kpidashid).append("\" name = \"MTD\" value=\"MTD\" onclick=\"changeMTDStatus(\'").append(kpidashid).append("\')\"").append(MTDChecked).append(">MTD").append("&nbsp");
                        tableBuffer.append("<input type=\"checkbox\" id=\"QTD_").append(kpidashid).append("\" name = \"QTD\" value=\"QTD\" onclick=\"changeQTDStatus(\'").append(kpidashid).append("\')\"").append(QTDChecked).append(">QTD").append("&nbsp");
                        tableBuffer.append("<input type=\"checkbox\" id=\"YTD_").append(kpidashid).append("\" name = \"YTD\" value=\"YTD\" onclick=\"changeYTDStatus(\'").append(kpidashid).append("\')\"").append(YTDChecked).append(">YTD").append("&nbsp");
                        tableBuffer.append("</td>");
                    }
                }
            }
            tableBuffer.append("</tr>").append("</table>").append("</div>");

        }
        if (kpiType.equalsIgnoreCase("Target")) {
            hidecolumns = detail.getHidecolumns();//added by sruthi for hide columns
            if (dashboardcollect.isOneviewCheckForKpis()) {
                tableBuffer.append("<center>").append("<div id=" + kpiMasterId + " style=\"width:" + dashboardcollect.getOneViewWidth() + "px;padding:4px\"  align=\"right\">");
            } else {
                tableBuffer.append("<div id=" + kpiMasterId + " style=\"width:99%;height:95%;overflow-y:auto;overflow-x:auto;padding:4px\"  align=\"left\">");
            }
            if (!kpiDrill.equalsIgnoreCase("Y")) {
                if (dashboardcollect.getCheckDashboardType().equalsIgnoreCase("y")) {
                    if (!dashboardcollect.isOneviewCheckForKpis()) {
                        tableBuffer.append("<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"Viewer_KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>");
                    }
                } else {
                    tableBuffer.append("<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>");
                }
            }
            if (dashboardcollect.isOneviewCheckForKpis()) {
                tableBuffer.append("<Table style=\"width:" + dashboardcollect.getOneViewWidth() + "px;\"   cellpadding=\"0\" cellspacing=\"1\" id='" + kpiMasterId + "'  class=\"tablesorter\" >");
            } else {
                tableBuffer.append("<Table style=\"width:99%;height:auto\"   cellpadding=\"0\" cellspacing=\"1\" id=\"tablesorter\"  class=\"tablesorter\" >");
            }
            tableBuffer.append("<thead align=\"center\">");
            tableBuffer.append("<tr>");
            if (!dashboardcollect.isOneviewCheckForKpis()) {
                if (isPowerAnalyserEnableforUser) {
                    tableBuffer.append("<th><strong></strong></th>");
                    tableBuffer.append("<th><strong></strong></th>");
                }
            }
            if (!ViewBy.isEmpty()) {
                ArrayList<String> kpiheaderdata = new ArrayList<String>();
                if (hidecolumns != null && !hidecolumns.isEmpty()) {//added by sruthi for hide columns
                    for (int i = ViewBy.size() + 1; i < ViewBy.size() + 8; i++) {
                        kpiheaderdata.add(Kpiheadarray[i]);
                    }
                    for (String hidedat : hidecolumns) {
                        if (hidedat.trim().equalsIgnoreCase(kpiheaderdata.get(0))) {
                            flag1 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(kpiheaderdata.get(1))) {
                            flag2 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(kpiheaderdata.get(2))) {
                            flag3 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(kpiheaderdata.get(3))) {
                            flag4 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(kpiheaderdata.get(4))) {
                            flag5 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(kpiheaderdata.get(5))) {
                            flag6 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(kpiheaderdata.get(6))) {
                            flag7 = 1;
                        }
                    }
//
                    for (int j = 0; j < ViewBy.size() + 1; j++) {
                        tableBuffer.append("<th width=\"15%\"><strong>" + Kpiheadarray[j] + "</strong></th>");
                    }
                    // tableBuffer.append("<th><strong>"+Kpiheadarray[1]+"</strong></th>");

                    if (flag1 == 1) {
                        tableBuffer.append("<th width=\"10%\" style=\"display:none\"><strong>" + kpiheaderdata.get(0) + "</strong></th>");
                    } else {
                        tableBuffer.append("<th width=\"10%\" ><strong>" + kpiheaderdata.get(0) + "</strong></th>");
                    }
                    if (flag2 == 1) {
                        tableBuffer.append("<th width=\"10%\" style=\"display:none\"><strong>" + kpiheaderdata.get(1) + "</strong></th>");
                    } else {
                        tableBuffer.append("<th width=\"10%\" ><strong>" + kpiheaderdata.get(1) + "</strong></th>");
                    }
                    if (flag3 == 1) {
                        tableBuffer.append("<th width=\"10%\" style=\"display:none\"><strong>" + kpiheaderdata.get(2) + "</strong></th>");
                    } else {
                        tableBuffer.append("<th width=\"10%\" ><strong>" + kpiheaderdata.get(2) + "</strong></th>");
                    }
                    if (flag4 == 1) {
                        tableBuffer.append("<th width=\"10%\" style=\"display:none\"><strong>" + kpiheaderdata.get(3) + "</strong></th>");
                    } else {
                        tableBuffer.append("<th width=\"10%\" ><strong>" + kpiheaderdata.get(3) + "</strong></th>");
                    }
                    if (flag5 == 1) {
                        tableBuffer.append("<th style=\"display:none\"><strong>" + kpiheaderdata.get(4) + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + kpiheaderdata.get(4) + "</strong></th>");
                    }
                    if (flag6 == 1) {
                        tableBuffer.append("<th style=\"display:none\"><strong>" + kpiheaderdata.get(5) + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + kpiheaderdata.get(5) + "</strong></th>");
                    }
                    if (flag7 == 1) {
                        tableBuffer.append("<th style=\"display:none\"><strong>" + kpiheaderdata.get(6) + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + kpiheaderdata.get(6) + "</strong></th>");
                    }
                    if (ViewBy.size() > 1) {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[10] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[9] + "</strong></th>");
                    }
                    if (ViewBy.size() > 1) {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[11] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[10] + "</strong></th>");
                    }
                    //   tableBuffer.append("<th width=\"10%\"><strong>"+Kpiheadarray[11]+"</strong></th>");

                } else {//endedd by sruthi
                    tableBuffer.append("<th width=\"15%\"><strong>" + Kpiheadarray[0] + "</strong></th>");
                    tableBuffer.append("<th width=\"15%\" ><strong>" + Kpiheadarray[1] + "</strong></th>");
                    tableBuffer.append("<th width=\"15%\" ><strong>" + Kpiheadarray[2] + "</strong></th>");
                    tableBuffer.append("<th width=\"10%\"><strong>" + Kpiheadarray[3] + "</strong></th>");
//            tableBuffer.append("<th width=\"20px\">&nbsp;</th>");
                    tableBuffer.append("<th width=\"10%\"><strong>" + Kpiheadarray[4] + "</strong></th>");
                    tableBuffer.append("<th width=\"10%\" ><strong>" + Kpiheadarray[5] + "</strong></th>");
//            if(kpiDetails.isShowInsights()||forDesigner){
//            if(PrivilegeManager.isModuleEnabledForUser("INSIGHT", Integer.parseInt(userId)))
//                if(!dashboardcollect.isOneviewCheckForKpis())
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[6] + "</strong></th>");
//            }
//            if(kpiDetails.isShowComments()||forDesigner){
//                if(!dashboardcollect.isOneviewCheckForKpis())
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[7] + "</strong></th>");
//            }
//            if(kpiDetails.isShowGraphs()||forDesigner){
//                if(!dashboardcollect.isOneviewCheckForKpis())

                    tableBuffer.append("<th><strong>" + Kpiheadarray[8] + "</strong></th>");
//            }
//           if(!dashboardcollect.isOneviewCheckForKpis())
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[9] + "</strong></th>");
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[10] + "</strong></th>");
                    if (ViewBy.size() > 1) {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[11] + "</strong></th>");
                    }
                }
            } else {  //added by sruthi for hide columns
                if (hidecolumns != null && !hidecolumns.isEmpty()) {
                    for (String hidedat : hidecolumns) {
                        if (hidedat.trim().equalsIgnoreCase(Kpiheadarray[1])) {
                            flag1 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(Kpiheadarray[2])) {
                            flag2 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(Kpiheadarray[3])) {
                            flag3 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(Kpiheadarray[4])) {
                            flag4 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(Kpiheadarray[5])) {
                            flag5 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(Kpiheadarray[6])) {
                            flag6 = 1;
                        }
                        if (hidedat.trim().equalsIgnoreCase(Kpiheadarray[7])) {
                            flag7 = 1;
                        }
                    }

                    tableBuffer.append("<th width=\"15%\"><strong>" + Kpiheadarray[0] + "</strong></th>");

                    if (flag1 == 1) {
                        tableBuffer.append("<th width=\"10%\" style=\"display:none\"><strong>" + Kpiheadarray[1] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th width=\"10%\" ><strong>" + Kpiheadarray[1] + "</strong></th>");
                    }
                    if (flag2 == 1) {
                        tableBuffer.append("<th width=\"10%\" style=\"display:none\"><strong>" + Kpiheadarray[2] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th width=\"10%\" ><strong>" + Kpiheadarray[2] + "</strong></th>");
                    }
                    if (flag3 == 1) {
                        tableBuffer.append("<th width=\"10%\" style=\"display:none\"><strong>" + Kpiheadarray[3] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th width=\"10%\" ><strong>" + Kpiheadarray[3] + "</strong></th>");
                    }
                    if (flag4 == 1) {
                        tableBuffer.append("<th style=\"display:none\"><strong>" + Kpiheadarray[4] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[4] + "</strong></th>");
                    }
                    if (flag5 == 1) {
                        tableBuffer.append("<th style=\"display:none\"><strong>" + Kpiheadarray[5] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[5] + "</strong></th>");
                    }
                    if (flag6 == 1) {
                        tableBuffer.append("<th style=\"display:none\"><strong>" + Kpiheadarray[6] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[6] + "</strong></th>");
                    }
                    if (flag7 == 1) {
                        tableBuffer.append("<th style=\"display:none\"><strong>" + Kpiheadarray[7] + "</strong></th>");
                    } else {
                        tableBuffer.append("<th ><strong>" + Kpiheadarray[7] + "</strong></th>");
                    }
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[8] + "</strong></th>");
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[9] + "</strong></th>");
                    // tableBuffer.append("<th width=\"10%\"><strong>"+Kpiheadarray[10]+"</strong></th>");

                } else {//ended by sruthi
                    tableBuffer.append("<th width=\"15%\"><strong>" + Kpiheadarray[0] + "</strong></th>");
                    tableBuffer.append("<th width=\"10%\"><strong>" + Kpiheadarray[1] + "</strong></th>");
                    tableBuffer.append("<th width=\"10%\" ><strong>" + Kpiheadarray[2] + "</strong></th>");
                    tableBuffer.append("<th width=\"10%\"><strong>" + Kpiheadarray[3] + "</strong></th>");
//            tableBuffer.append("<th width=\"20px\">&nbsp;</th>");
                    tableBuffer.append("<th width=\"10%\" ><strong>" + Kpiheadarray[4] + "</strong></th>");
                    tableBuffer.append("<th width=\"10%\"><strong>" + Kpiheadarray[5] + "</strong></th>");
//            if(kpiDetails.isShowInsights()||forDesigner){
//            if(PrivilegeManaInteger.parseInt(userId)))
//                if(!dashboardcollect.isOneviewCheckForKpis())ger.isModuleEnabledForUser("INSIGHT",
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[6] + "</strong></th>");
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[7] + "</strong></th>");
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[8] + "</strong></th>");
                    tableBuffer.append("<th ><strong>" + Kpiheadarray[9] + "</strong></th>");
                }
            }
            tableBuffer.append("</tr>");
            tableBuffer.append("</thead>");
            tableBuffer.append("<tfoot></tfoot>");
            tableBuffer.append("<tbody>");
        } else {
            if (dashboardcollect.isOneviewCheckForKpis()) {
                tableBuffer.append("<center>").append("<div id=" + kpiMasterId + " style=\"width:" + dashboardcollect.getOneViewWidth() + "px; padding:4px\"  align=\"right\">");
            } else {
                tableBuffer.append("<div id=" + kpiMasterId + " style=\"width:99%;height:" + height + "px;overflow-y:auto;overflow-x:auto;padding:4px\"  align=\"left\">");
            }
            if (kpiDrill != null) {
                if (!kpiDrill.equalsIgnoreCase("Y")) {
                    if (dashboardcollect.getCheckDashboardType().equalsIgnoreCase("y")) {
                        if (!dashboardcollect.isOneviewCheckForKpis()) {
                            tableBuffer.append("<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"Viewer_KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>");
                        }
                    } else {
                        tableBuffer.append("<table><tr><td><a style='decoration:none' href=\"javascript:void(0)\" onclick=\"KpiDrilldown('" + urlElements.toString().substring(1) + "')\">KPI Drill</a></td></tr></table>");
                    }
                }
            }
            if (!SchedulerFlag) {
                if (dashboardcollect.isOneviewCheckForKpis()) {
                    tableBuffer.append("<Table style=\"width:" + dashboardcollect.getOneViewWidth() + "px;\"   cellpadding=\"1\" cellspacing=\"1\" id='" + kpiMasterId + "'  class=\"tablesorter\" >");
                } else {
                    tableBuffer.append("<Table width=\"99%;height:auto\" cellpadding=\"1\" cellspacing=\"1\" id=\"tablesorter\"  class=\"tablesorter\" >");
                }
            } else {
                tableBuffer.append("<Table width=\"99%;height:auto\" cellpadding=\"1\" cellspacing=\"1\" id=\"tablesorter\"  border=\"1\" class=\"tablesorter\" >");
            }

            tableBuffer.append("<thead align=\"center\">");
            tableBuffer.append("<tr>");
            if (!dashboardcollect.isOneviewCheckForKpis()) {
                if (!SchedulerFlag) {
                    tableBuffer.append("<th width=\"30px\"><strong></strong></th>");
                    if (kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                        tableBuffer.append("<th width=\"30px\"><strong></strong></th>");
                    }
                }
            }
            if (detail.getKpiType().equalsIgnoreCase("Basic") || detail.getKpiType().equalsIgnoreCase("BasicTarget") || detail.getKpiType().equalsIgnoreCase("Standard") || kpiType.equalsIgnoreCase("MultiPeriod")) {
                tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[0] + "</strong></th>");
            }
            if (kpiType.equalsIgnoreCase("MultiPeriod")) {
                if (kpiDetails.isCurrentChecked() || forDesigner) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[1] + "</strong></th>");
                }
                if (kpiDetails.isMTDChecked() || forDesigner) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[2] + "</strong></th>");
                }
                if (kpiDetails.isQTDChecked() || forDesigner) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[3] + "</strong></th>");
                }
                if (kpiDetails.isYTDChecked() || forDesigner) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[4] + "</strong></th>");
                }

                tableBuffer.append("<th width=\"50px\"><strong>" + Kpiheadarray[5] + "</strong></th>");

            } else if (kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                tableBuffer.append("<th width=\"250px\"><strong>" + Kpiheadarray[0] + "</strong></th>");
                if (kpiDetails.isMTDChecked() || forDesigner) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[1] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[2] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[3] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[4] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[5] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[6] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[7] + "</strong></th>");
                }
                if (kpiDetails.isQTDChecked() || forDesigner) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[8] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[9] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[10] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[11] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[12] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[13] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[14] + "</strong></th>");
                }
                if (kpiDetails.isYTDChecked() || forDesigner) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[15] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[16] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[17] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[18] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[19] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[20] + "</strong></th>");
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[21] + "</strong></th>");
                }
            } else if (kpiType.equalsIgnoreCase("Standard")) {
                tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[1] + "</strong></th>");
                tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[2] + "</strong></th>");
                tableBuffer.append("<th width=\"170px\"><strong>&nbsp;</strong></th>");
                if (kpiDetails.isShowInsights() || forDesigner) {
                    if (PrivilegeManager.isModuleEnabledForUser("INSIGHT", Integer.parseInt(userId))) {
                        if (!SchedulerFlag) {
                            if (!dashboardcollect.isOneviewCheckForKpis()) {
                                tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[3] + "</strong></th>");
                            }
                        }
                    }
                }
                if (kpiDetails.isShowComments() || forDesigner) {
                    if (!SchedulerFlag) {
                        if (!dashboardcollect.isOneviewCheckForKpis()) {
                            tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[4] + "</strong></th>");
                        }
                    }
                }
                if (!dashboardcollect.isOneviewCheckForKpis()) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[5] + "</strong></th>");
                }
            } else {
//           if(kpiType.equalsIgnoreCase("Basic") ||kpiType.equalsIgnoreCase("BasicTarget")){
//            tableBuffer.append("<th width=\"150px\"><strong>Value</strong></th>");
//            }
//           else{
//            tableBuffer.append("<th width=\"150px\"><strong>Current/Prior</strong></th>");
//            }
                if (kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget")) {
                    tableBuffer.append("<th width=\"170px\"><strong>" + Kpiheadarray[1] + "</strong></th>");
                }
                if (kpiType.equalsIgnoreCase("BasicTarget")) {
                    tableBuffer.append("<th width=\"150px\"><strong>" + Kpiheadarray[2] + "</strong></th>");
                    tableBuffer.append("<th width=\"150px\"><strong>" + Kpiheadarray[3] + "</strong></th>");
                    tableBuffer.append("<th width=\"150px\"><strong>" + Kpiheadarray[4] + "</strong></th>");
                }
                if (kpiDetails.isShowInsights() || forDesigner) {
                    if (PrivilegeManager.isModuleEnabledForUser("INSIGHT", Integer.parseInt(userId))) {
                        if (!SchedulerFlag) {
                            if (!dashboardcollect.isOneviewCheckForKpis()) {
                                if (detail.getKpiType().equalsIgnoreCase("Basic")) {
                                    tableBuffer.append("<th width=\"140px\"><strong>" + Kpiheadarray[2] + "</strong></th>");
                                } else if (kpiType.equalsIgnoreCase("BasicTarget")) {
                                    tableBuffer.append("<th width=\"140px\"><strong>" + Kpiheadarray[5] + "</strong></th>");
                                }
                            }
                        }
                    }
                }
                if (kpiDetails.isShowComments() || forDesigner) {
                    if (!SchedulerFlag) {
                        if (!dashboardcollect.isOneviewCheckForKpis()) {
                            if (detail.getKpiType().equalsIgnoreCase("Basic")) {
                                tableBuffer.append("<th width=\"140px\"><strong>" + Kpiheadarray[3] + "</strong></th>");
                                tableBuffer.append("<th width=\"100px\"><strong>" + Kpiheadarray[4] + "</strong></th>");
                            } else if (kpiType.equalsIgnoreCase("BasicTarget")) {
                                tableBuffer.append("<th width=\"140px\"><strong>" + Kpiheadarray[6] + "</strong></th>");
                                tableBuffer.append("<th width=\"100px\"><strong>" + Kpiheadarray[7] + "</strong></th>");
                            }
                        }
                    }
                }
                if (kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Basic")) {
//               if(!SchedulerFlag)
//              tableBuffer.append("<th width=\"70px\"></th>");
                }
            }
            tableBuffer.append("</tr>");
            tableBuffer.append("</thead>");
            tableBuffer.append("<tfoot></tfoot>");
            tableBuffer.append("<tbody>");
        }

        if (kpiType.equalsIgnoreCase("Target") && kpiDetails.isShowGraphs()) {
            ArrayList rowview = new ArrayList();
            rowview.add("TIME");
            PbReportQuery timequery = new PbReportQuery();

            timequery.setRowViewbyCols(rowview);
            timequery.setColViewbyCols(new ArrayList());

            timequery.setQryColumns(QueryCols);
            timequery.setColAggration(QueryAggs);
            timequery.setParamValue(dashboardcollect.reportParametersValues);
            timequery.setParamValue(dashboardcollect.reportParametersValues);//Added by k
            ArrayList arl = new ArrayList();
            arl.add(dashboardcollect.timeDetailsArray.get(0));
            arl.add(dashboardcollect.timeDetailsArray.get(1));
            arl.add(dashboardcollect.timeDetailsArray.get(2));
            arl.add(dashboardcollect.timeDetailsArray.get(3));
            arl.add(dashboardcollect.timeDetailsArray.get(4));

            timequery.setTimeDetails(arl);
            timequery.isTimeSeries = true;
            timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
            timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
            timequery.setUserId(userId);
            timequery.setBizRoles(BizRoles);
            timequery.setReportId(dashBoardId);
            timequery.setParameterGroupAnalysisHashMap(new HashMap());
            timequery.setBucketAnalysisHashMap(new HashMap());
            pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(0)));
            timequery.isTimeSeries = false;
        }
//container.setKpiRetObj();
        pbretObj = container.getKpiRetObj();
        ArrayList<String> tempList = new ArrayList<String>();
        tempList.add(elemntIdForMail);
        if (SchedulerFlag) {
            a1 = new ArrayList<String>();
            a1 = tempList;
        }
        //sandeep
        int localcolspan = 0;
        if (ViewBy.size() > 1) {

            for (int totalrow = 0; totalrow < pbretObj.getRowCount(); totalrow++) {
                if (!viewbyvalue.equalsIgnoreCase("")) {
                    if (viewbyvalue.equalsIgnoreCase(pbretObj.getFieldValueString(totalrow, 0))) {
                        localcolspan = totalrow + 1;
                    } else {
                        break;
                    }
                } else {
                    viewbyvalue = pbretObj.getFieldValueString(totalrow, 0);

                    localcolspan = totalrow + 1;
                }
            }
            colspan = localcolspan * a1.size();
        } else {
            colspan = a1.size();
        }
        //end of sandeep code for 2row viewby
        //Ram 07Oct15 for Two Viewbys rows
        ArrayList localcol = new ArrayList();
        String viewbyval = "";
        if (ViewBy.size() > 1) {
            int lcolspan = 0;
            for (int totalrow = 0; totalrow < pbretObj.getRowCount(); totalrow++) {
                if (!viewbyval.equalsIgnoreCase("")) {
                    if (viewbyval.equalsIgnoreCase(pbretObj.getFieldValueString(totalrow, 0))) {
                        lcolspan = totalrow + 1;
                        if (totalrow == pbretObj.getRowCount() - 1) {
                            localcol.add(lcolspan);
                        }
                    } else {
                        localcol.add(lcolspan);
                        viewbyval = pbretObj.getFieldValueString(totalrow, 0);
                        lcolspan = totalrow + 1;
                    }
                } else {
                    viewbyval = pbretObj.getFieldValueString(totalrow, 0);
                    lcolspan = totalrow + 1;

                }
            }
        }

        // Ended Ram code
        int count = 0;
        int counter = 0;
        for (int totalrow = 0; totalrow < pbretObj.getRowCount(); totalrow++) {

            if (ViewBy.size() > 1) {
                if (count == totalrow) {
                    display = "";
                    // count=count+localcolspan;
                    colspan = (Integer.parseInt(localcol.get(counter).toString()) - count) * a1.size();
                    count = Integer.parseInt(localcol.get(counter).toString());
                    counter++;
                }
            }
            for (int i = 0; i < a1.size(); i++) {
                if (totalrow == pbretObj.getRowCount() - 1 && i == 0) {
                    isGtShow = true;
                } else {
                    isGtShow = false;
                }

                if (i == 0) {
                    isViewByAdd = true;
                } else {
                    isViewByAdd = false;
                }

                String elementId = a1.get(i);
                String elementName = "";
                String value = "";
                boolean isGroupElement = detail.isGroupElement(elementId);
                List<KPIElement> elements = kpiElementMap.get(elementId);
                //added by sruthi for manuval bud based on dates
                GetConnectionType gettypeofconn = new GetConnectionType();
                String connType = gettypeofconn.getConTypeByElementId(a1.get(0));
                ArrayList arl1 = new ArrayList();
                PbTimeRanges timeRangesdays = new PbTimeRanges();
                arl1.add(dashboardcollect.timeDetailsArray.get(2));
                arl1.add(dashboardcollect.timeDetailsArray.get(3));
                timeRangesdays.st_d = (String) dashboardcollect.timeDetailsArray.get(2);
                timeRangesdays.ed_d = (String) dashboardcollect.timeDetailsArray.get(3);
                timeRangesdays.elementID = a1.get(0);
                timeRangesdays.setDateDiff(connType);
                days = timeRangesdays.daysDiff + 1;
                for (int id = 0; id < a1.size(); id++) {
                    elementids.add(a1.get(id));
                }
                //endedby sruthi
                if (!isGroupElement) {
                    for (KPIElement elem : elements) {
                        if (elem != null && elem.getElementId() != null && !elem.getElementId().equalsIgnoreCase("")) {
                            if (elementId.equalsIgnoreCase(elem.getElementId())) {
//                             if(!atrelementIds.isEmpty()&& atrelementIds.contains(elementId)){
//                                 int index=atrelementIds.indexOf(elementId);
//                                  if(selectrepids.contains(elementId))
//                                      {
//                    String formattedvalue=numberFormat.get(index);
//                   if(formattedvalue.contains("M"))
//                       value="( Millions )";
//                  else if(formattedvalue.contains("L"))
//                       value="( Lakhs )";
//                   else if(formattedvalue.contains("C"))
//                       value="( Crores )";
//                   else if(formattedvalue.contains("K"))
//                       value="( Thousands )";
//                   else if(formattedvalue.contains(""))
//                       value="";
//                                  }
//                            }

                                elementName = elem.getElementName();
                                HashMap<String, HashMap> msrChange = detail.getmodifymeasureAttrChng();
                                if (elementName != null && !elementName.equalsIgnoreCase("")) {
                                    elementName = elem.getElementName();
                                } else {

                                    if (msrChange != null && msrChange.size() != 0 && msrChange.get(elementId) != null) {
                                        HashMap<String, String> elementChange = msrChange.get(elementId);
                                        if (elementChange != null && elementChange.size() != 0) {
                                            elementName = elementChange.get("elementname");
                                        }
                                    }
                                }
                                isGroupElement = elem.isIsGroupElement();
                                break;
                            }
                        }
                    }
                }
                if (isGroupElement) {
                    if (detail.getSingleGroupHelpers() != null && !detail.getSingleGroupHelpers().isEmpty()) {
                        List<KPISingleGroupHelper> kPISingleGroupHelpers = detail.getSingleGroupHelpers();
                        for (KPISingleGroupHelper groupingHelper : kPISingleGroupHelpers) {
                            BigDecimal sumBd = new BigDecimal(0);
                            if (groupingHelper.getGroupName() != null) {
                                if (groupingHelper.getGroupName().equalsIgnoreCase(elementId)) {
                                    if (groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()) != null) {
                                        kpiDetails.addKPIDrill(groupingHelper.getGroupName(), groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName()));
                                    }
                                    tableBuffer.append(getGroupVal(groupingHelper, pbretObj, 1, kpiMasterId, detail, dashBoardId, dashboardcollect));
                                }
                            }
                        }
                    }

                } else {
                    if (kpiType.equalsIgnoreCase("MultiPeriod")) {
                        tableBuffer.append(displayMultiPeriodKpi(container, elementId, elementName, dashBoardId, kpiMasterId, "N", kpiType, pbretObjForTime, detail, forDesigner, dashboardcollect, userId));
                    } else if (kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                        tableBuffer.append(genrateMultiPeriodKPICurrentPrior(container, elementId, elementName, dashBoardId, kpiMasterId, "N", kpiType, pbretObjForTime, detail, forDesigner, dashboardcollect, userId, kpiElementIdsString.toString(), kpiElementsname.toString()));
                    } else {
                        if (ViewBy.isEmpty()) {
                            tableBuffer.append(displayKpi(container, pbretObj, elementId, elementName, dashBoardId, kpiMasterId, "N", kpiType, pbretObjForTime, detail, forDesigner, dashboardcollect, userId));
                        } else {
                            tableBuffer.append(displayKpiForTarget(container, pbretObj, totalrow, a1.size(), isViewByAdd, isGtShow, elementId, elementName, dashBoardId, kpiMasterId, "N", kpiType, pbretObjForTime, detail, forDesigner, dashboardcollect, userId, i, aggid, ViewBy));//changed by sruthi for gt
                        }
                        container.setCalculativeVal(calculativeVal);
                        if (!gtVal.isEmpty()) {
                            container.setGtVal(gtVal);
                        }
                    }
                }

            }
        }

        tableBuffer.append("</tbody>");
        tableBuffer.append("</Table>");
        if (dashboardcollect.isOneviewCheckForKpis()) {
            tableBuffer.append("</div>").append("</center>");
        } else {
            tableBuffer.append("</div>");
        }
        if (!dashboardcollect.isOneviewCheckForKpis()) {
            tableBuffer.append("</div>");
        }
        //added by sruthi for hide columns
//         tableBuffer.append("<div id=\"HideColumns\" title=\"HideColumns\" style=\"display:none\">");
//           tableBuffer.append("<div id=\"HideColumnsdata\">");                             //commented by mohit

//              tableBuffer.append("</div>");
//          tableBuffer.append("</div>");
        //ended by sruthi
        return tableBuffer.toString();
    }

    public String displayKpi(Container container, PbReturnObject retObjQry, String ElementId, String ElementName, String ReportId, String kpiMasterid,
            String kpiDrill, String kpiType, PbReturnObject pbretObjForTime, DashletDetail dashletDetail, boolean fromDesigner,
            pbDashboardCollection collect, String userId) throws Exception {
        String attribute = "";
        String imgdata = "";
//        String datavaluesstr = "";
        StringBuilder datavaluesstr = new StringBuilder();
        StringBuilder keyvaluesstr = new StringBuilder();
        StringBuilder barchartcolumntitlesstr = new StringBuilder();
//        String barchartcolumntitlesstr = "";
        StringBuilder viewbycolumnsstr = new StringBuilder();
        String targetElementId = null;
        String targetElemName = null;
        ArrayList arr = new ArrayList();
        boolean istarget = false;
        List<KPIElement> targetkpielem = new ArrayList<KPIElement>();
        ArrayList<String> viewbyvales = new ArrayList<String>();
        String kpiSymbol = dashletDetail.getKpiSymbol();
        HashMap basicTargetDetails = new HashMap();
        HashMap simpleKpi = new HashMap();
        DecimalFormat oneDForm = new DecimalFormat("#.0");
        String folderDetails = collect.reportBizRoles[0];
        if ((pbretObjForTime != null && pbretObjForTime.rowCount > 0) && !kpiType.equalsIgnoreCase("BasicTarget")) {
            ProgenChartDatasets graph = new ProgenChartDatasets();
            String[] barchartcolumnnames = new String[2];
            String[] barchartcolumntitles = new String[2];
            String[] viewbycolumns = new String[1];

            barchartcolumnnames[0] = "Time";
            barchartcolumnnames[1] = "A_" + ElementId;
            barchartcolumntitles[0] = "Time";
            barchartcolumntitles[1] = ElementName;
            viewbycolumns[0] = "TIME";

            for (int index = 0; index < pbretObjForTime.getRowCount(); index++) {
//                barchartcolumntitlesstr = barchartcolumntitlesstr + ElementName + ",";
                barchartcolumntitlesstr.append(ElementName).append(",");
//                datavaluesstr = datavaluesstr + pbretObjForTime.getFieldValueString(index, "A_" + ElementId) + ",";
                datavaluesstr.append(pbretObjForTime.getFieldValueString(index, "A_" + ElementId)).append(",");
                String str = pbretObjForTime.getFieldValueString(index, viewbycolumns[0]);
                str = str.replaceAll("-", "/");
//                keyvaluesstr = keyvaluesstr + str + ",";
                keyvaluesstr.append(str).append(",");
//                viewbycolumnsstr = viewbycolumnsstr + "Time" + ",";
                viewbycolumnsstr.append("Time").append(",");
            }
            graph.setBarChartColumnNames(barchartcolumnnames);
            graph.setViewByColumns(viewbycolumns);
            graph.setBarChartColumnTitles(barchartcolumntitles);
            graph.setPieChartColumns(barchartcolumnnames);
            graph.setTimeLevel("DAY");
            graph.setDisplayGraphRows("All");

            ProgenChartDisplay pchart = new ProgenChartDisplay(140, 35);
            pchart.setCtxPath(collect.ctxPath);
            pchart.setGraph(graph);
            pchart.setSession(collect.getSession());
            pchart.setResponse(collect.getServletResponse());
            if (collect.getServletResponse() != null) {
                pchart.setOut(collect.getServletResponse().getWriter());
                pchart.setRetObj(pbretObjForTime);
                pchart.GetKPITimeSeriesChart();
            }
            imgdata = pchart.chartDisplay;
        }

//        String kpiComment = null;
        String[] COLORS = {"#0095B6", "#9ACD32", "#007BA7", "#0095B6", "#78866B", "#00FFFF", "#0067A5", "#9ACD32", "#FEFE33", "#4F7942", "#CFB53B", "#006600", "#003300", "#ecddoo",};
        int tableWidth = 200;
        int sum = 0;
        double currVal = 0.0;
        double priorVal = 0.0;
        String viewbyname = null;
        String viewbyid = null;
        String temp = null;
        String varBudget = "--";
        String devVal = "--";
        String priorid = null;
        String varLastyear = "--";
        StringBuilder kpiBarChart = new StringBuilder();

        KPI kpiDetail = (KPI) dashletDetail.getReportDetails();
        String dashletId = dashletDetail.getDashBoardDetailId();
        List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);

        StringBuilder dispKpi = new StringBuilder();
        String drillReportId = kpiDetail.getKPIDrill(ElementId);
        String drillRepType = kpiDetail.getKPIDrillRepType(ElementId);
        if (kpiDetail.getKpiTragetMap(ElementId) != null && !kpiDetail.getKpiTragetMap(ElementId).equalsIgnoreCase("")) {
            targetElementId = kpiDetail.getKpiTragetMap(ElementId);
        }
        if (targetElementId != null && !targetElementId.equalsIgnoreCase("")) {

            targetkpielem = kpiDetail.getTargetKPIElements(targetElementId);
        }
        if (targetkpielem != null) {
            for (KPIElement elem : targetkpielem) {
                targetElemName = elem.getElementName();
            }
        }
        dispKpi.append("<Tr>");
        if (!collect.isOneviewCheckForKpis()) {
            if (container.isPowerAnalyserEnableforUser) {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                    if (background != null && !background.isEmpty()) {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Assign Target Measure\" onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Assign Target Measure\" onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
                    }
                } else {
                    dispKpi.append("<Td align=\"center\" padding=\"0px\" width=\"250px\"><a title=\"Assign Target Measure\" onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
                }
                if (!SchedulerFlag) {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null && background.get(atrelementIds.indexOf(ElementId)) != null) {
                        dispKpi.append("<td align='center' style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "','" + kpiMasterid + "')\"></a></td>");
                    } else {
                        dispKpi.append("<td align='center' style=\"cursor:pointer\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "','" + kpiMasterid + "')\"></a></td>");
                    }
                }
            }
        }
        if ((drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) && (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId))) {
            if (!background.isEmpty() && background != null) {
                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><a style='decoration:none'  href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
            } else {
                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + "\"><a style='decoration:none' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
            }
        } else if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
            if (drillRepType != null && !("0".equals(drillRepType)) && !drillRepType.equalsIgnoreCase("") && drillRepType.equalsIgnoreCase("D")) {
                dispKpi.append("<Td><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
            } else {
                dispKpi.append("<Td><a style='decoration:none' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                //function added by mohit
            }

        } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
            if (!background.isEmpty() && background != null) {
                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
            } else if (!alignment.isEmpty() && alignment != null) {
                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
            } else {
                dispKpi.append("<Td style=\"cursor:pointer\"><strong>" + ElementName + "</strong></Td>");
            }
        } else {
            dispKpi.append("<Td style=\"cursor:pointer\"><strong>" + ElementName + "</strong></Td>");
        }

        double[] celWidth = null;
        int i = 0;
        for (i = 0; i < kpiElements.size(); i++) {

            //For Each element id try to value from retObjQry  and build a table row  here
            if (retObjQry.getRowCount() > 0) {
                temp = "A_" + kpiElements.get(i).getElementId();
                String type = kpiElements.get(i).getRefElementType();
                if (kpiElements.size() > 1) {

                    if ((retObjQry.getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(retObjQry.getFieldValueString(0, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currVal = Double.parseDouble((retObjQry.getFieldValueString(0, temp)));
                        } else if (type.equalsIgnoreCase("2")) {
                            String priorValStr = retObjQry.getFieldValueString(0, temp);
                            priorid = kpiElements.get(i).getElementId();
                            if (priorValStr != null && !("".equals(priorValStr))) {
                                priorVal = Double.parseDouble(priorValStr);
                            }

                            double[] valArray = {currVal, priorVal};

                            celWidth = new double[valArray.length];
                            for (int j = 0; j < valArray.length; j++) {
                                sum += (valArray[j]);
                            }
                            for (int k = 0; k < valArray.length; k++) {
                                celWidth[k] = ((tableWidth * (valArray[k])) / sum);
                                celWidth[k] = Math.round(celWidth[k]);
                            }
                            kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                            for (int kpi = 0; kpi < valArray.length; kpi++) {
                                if (kpiType.equalsIgnoreCase("Target")) {
                                    kpi++;
                                }
                                String value = retObjQry.getModifiedNumber(new BigDecimal(valArray[kpi]));
                                if (priorVal == 0.0) {
                                    if (kpi == 1) {
                                        if (kpi == valArray.length - 1) {
                                            arr.add("");
                                        }
                                        continue;

                                    }
//                                    else
//                                        
//                                        COLORS[kpi] = "";
                                }
                                if (celWidth[kpi] == 0.0) {
                                    COLORS[kpi] = "#FFFFFF";
                                }
                                if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget"))) {
                                    if (COLORS[kpi] == "") {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                            int index = atrelementIds.indexOf(ElementId);
                                            NumberFormatter nf = new NumberFormatter();
                                            int rounding;
                                            if (round.get(index) == "") {
                                                rounding = 0;
                                            } else {
                                                rounding = Integer.parseInt(round.get(index));
                                            }
                                            String formattedvalue = nf.getModifiedNumber(valArray[kpi], numberFormat.get(index), rounding);
                                            String currVal1 = nf.getModifiedNumber(currVal, numberFormat.get(index), rounding);
                                            currVal = Double.parseDouble(currVal1);
                                            attribute = numberFormat.get(index).toString();
                                            if (!(kpiType.equalsIgnoreCase("Target"))) {      ///added by mohit
                                                if (!attribute.equalsIgnoreCase("")) {
                                                    if (attribute.contains("M")) {
                                                        formattedvalue = formattedvalue.replace("M", "");
                                                    }
                                                    if (attribute.contains("K")) {
                                                        formattedvalue = formattedvalue.replace("K", "");
                                                    }
                                                    if (attribute.contains("Cr")) {
                                                        formattedvalue = formattedvalue.replace("Cr", "");
                                                    }
                                                    if (attribute.contains("L")) {
                                                        formattedvalue = formattedvalue.replace("L", "");
                                                    }
                                                }
                                            }
                                            StringBuffer sb = new StringBuffer();
                                            for (int k = 0; k < formattedvalue.length(); k++) {
//                                             if(formattedvalue.charAt(k)=='M' || formattedvalue.charAt(k)=='C' || formattedvalue.charAt(k)=='L' || formattedvalue.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                   if(selectrepids.contains(ElementId)){
                                                sb.append(formattedvalue.charAt(k));
//                                                   }
//                                             }else{
//                                             sb.append(formattedvalue.charAt(k));
//                                             }
                                            }

                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");

                                            arr.add(symbols.get(index) + sb.toString());
                                        } else {

                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                            arr.add(value);
                                        }
                                    } else {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                            int index = atrelementIds.indexOf(ElementId);
                                            NumberFormatter nf = new NumberFormatter();
                                            int rounding;
                                            if (round.get(index) == "") {
                                                rounding = 0;
                                            } else {
                                                rounding = Integer.parseInt(round.get(index));
                                            }
                                            String formattedvalue = nf.getModifiedNumberDashboard(valArray[kpi], numberFormat.get(index), rounding);//changed by sruthi for indian rs
                                            attribute = numberFormat.get(index).toString();
                                            if (!(kpiType.equalsIgnoreCase("Target"))) {    ///added by mohit
                                                if (!attribute.equalsIgnoreCase("")) {
                                                    if (attribute.contains("M")) {
                                                        formattedvalue = formattedvalue.replace("M", "");
                                                    }
                                                    if (attribute.contains("K")) {
                                                        formattedvalue = formattedvalue.replace("K", "");
                                                    }
                                                    if (attribute.contains("Cr")) {
                                                        formattedvalue = formattedvalue.replace("Cr", "");
                                                    }
                                                    if (attribute.contains("L")) {
                                                        formattedvalue = formattedvalue.replace("L", "");
                                                    }
                                                }
                                            }
                                            StringBuffer sb = new StringBuffer();
                                            for (int k = 0; k < formattedvalue.length(); k++) {//
                                                sb.append(formattedvalue.charAt(k));
//
                                            }
                                            if (COLORS[kpi] == "#FFFFFF") {
                                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                                    if (symbols.get(index) != null) {
                                                        if (symbols.get(index).equalsIgnoreCase("%")) {

                                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + symbols.get(index) + "</Td>");
                                                            arr.add(sb.toString() + symbols.get(index));
                                                        } else {

                                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");

                                                            arr.add(symbols.get(index) + sb.toString());
                                                        }
                                                    } else {

                                                        kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                        arr.add(sb.toString());
                                                    }
                                                } else {

                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                    arr.add(symbols.get(index) + sb.toString());
                                                }
                                            } else {
                                                if (symbols.get(index) != null) {
                                                    if (symbols.get(index).equalsIgnoreCase("%") && !formattedvalue.equalsIgnoreCase("--")) {
                                                        //added by sruthi for negative color
                                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                                            if (formattedvalue.contains("-")) {
                                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {

                                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + "(" + sb.toString() + symbols.get(index) + ")" + "</Td>");
                                                                    arr.add("(" + sb.toString() + symbols.get(index) + ")");
                                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                                    String negativecolor = "#f24040";

                                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + symbols.get(index) + "</Td>");

                                                                    arr.add(sb.toString() + symbols.get(index));
                                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                                    String negativecolor = "#f24040";

                                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + "(" + sb.toString() + symbols.get(index) + ")" + "</Td>");
                                                                    arr.add("(" + sb.toString() + symbols.get(index) + ")");
                                                                } else {
                                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + symbols.get(index) + "</Td>");
                                                                    arr.add(sb.toString() + symbols.get(index));
                                                                }
                                                            } else {
                                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + symbols.get(index) + "</Td>");
                                                                arr.add(sb.toString() + symbols.get(index));
                                                            }
                                                        } else //ended by sruthi
                                                        {

                                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                            arr.add(sb.toString());
                                                        }
                                                    } else {
                                                        if (negativevalues != null && !negativevalues.isEmpty() && !formattedvalue.equalsIgnoreCase("--")) {
                                                            if (formattedvalue.contains("-")) {
                                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {

                                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + "(" + symbols.get(index) + sb.toString() + ")" + "</Td>");
                                                                    arr.add("(" + symbols.get(index) + sb.toString() + ")");
                                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                                    String negativecolor = "#f24040";

                                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                                    arr.add(symbols.get(index) + sb.toString());
                                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                                    String negativecolor = "#f24040";

                                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + "(" + symbols.get(index) + sb.toString() + ")" + "</Td>");
                                                                    arr.add("(" + symbols.get(index) + sb.toString() + ")");
                                                                } else {

                                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                                    arr.add(symbols.get(index) + sb.toString());
                                                                }
                                                            } else {

                                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                                arr.add(symbols.get(index) + sb.toString());
                                                            }
                                                        } else //ended by sruthi
                                                        {

                                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                            arr.add(sb.toString());
                                                        }
                                                    }
                                                } else {
                                                    //added by sruthi for negative color
                                                    if (negativevalues != null && !negativevalues.isEmpty() && !formattedvalue.equalsIgnoreCase("--")) {
                                                        if (formattedvalue.contains("-")) {
                                                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {

                                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + "(" + sb.toString() + ")" + "</Td>");
                                                                arr.add("(" + sb.toString() + ")");
                                                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                                String negativecolor = "#f24040";

                                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                                arr.add(sb.toString());
                                                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                                String negativecolor = "#f24040";

                                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + "(" + sb.toString() + ")" + "</Td>");
                                                                arr.add("(" + sb.toString() + ")");
                                                            } else {

                                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                                arr.add(sb.toString());
                                                            }
                                                        } else {

                                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                            arr.add(sb.toString());
                                                        }
                                                    } else //ended by sruthi
                                                    {
                                                        kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                        arr.add(sb.toString());
                                                    }
                                                }
                                            }
                                        } else if (COLORS[kpi] == "#FFFFFF") {
                                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {

                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                                arr.add(value);
                                            } else {

                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                                arr.add(value);
                                            }
                                        } else {

                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                            arr.add(value);
                                        }
                                    }
                                }
                            }

                            kpiBarChart.append("</Tr></Table>");
                            if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Kpi"))) {
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
//                               dispKpi.append("<Td style=\"cursor:pointer;color: " +font.get(atrelementIds.indexOf(ElementId)) + ";background-color:"+background.get(atrelementIds.indexOf(ElementId))+"\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"','"+ElementId+"','"+drillRepType+"')\">" +kpiBarChart.toString()+ "</a></Td>");

                                        if (flag1 == 1) {
                                            dispKpi.append("<Td style=\"display:none;cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
                                        }

                                    } else {
                                        if (flag1 == 1) {
                                            dispKpi.append("<Td align=\"display:none;center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                                        }
                                    }
                                } else {
                                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                        if (flag1 == 1)//added by sruthi for hide columns
                                        {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
                                        }

                                    } else {
                                        if (flag1 == 1) {
                                            dispKpi.append("<Td style=\"display:none;\"align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                                        }

                                    }
                                }//ended by sruthi
                            }
                        } else if (type.equalsIgnoreCase("3") && (!kpiType.equalsIgnoreCase("Target"))) {
                        } else if ((kpiType.equalsIgnoreCase("Standard"))) {
                            double r = Double.parseDouble((retObjQry.getFieldValueString(0, temp)));
                            int decimalPlaces = 2;
                            BigDecimal bd = new BigDecimal(r);
                            bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                if (negativevalues != null && !negativevalues.isEmpty()) {
                                    String negativeValue = bd.toString();
                                    if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                        negativeValue = negativeValue.replace("-", " ");
                                        if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                            arr.add("(" + negativeValue + ")");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                            String negativecolor = "#f24040";
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                            arr.add(negativeValue);
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                            String negativecolor = "#f24040";
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                            arr.add("(" + negativeValue + ")");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + bd + "</Td>");
                                            arr.add(bd);
                                        }
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                        arr.add(negativeValue);
                                    }
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + bd + "</Td>");
                                    arr.add(bd);
                                }
                            } else {
                                dispKpi.append("<Td align=\"center\" >" + bd + "</Td>");
                                arr.add(bd);
                            }
                        }
                        if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Target") || kpiType.equalsIgnoreCase("Kpi"))) {
                            if (type.equalsIgnoreCase("4") && !kpiType.equalsIgnoreCase("Target")) {
                                double changePercVal = Double.parseDouble((retObjQry.getFieldValueString(0, temp)));
                                String elemId = kpiElements.get(i).getRefElementId();
                                String color = kpiDetail.getKPIColor(elemId, changePercVal);
                                String icon = "";
                                if ("Green".equalsIgnoreCase(color)) {
                                    icon = "icons pinvoke/status.png";
                                } else if ("Red".equalsIgnoreCase(color)) {
                                    icon = "icons pinvoke/status-busy.png";
                                } else if ("Yellow".equalsIgnoreCase(color)) {
                                    icon = "icons pinvoke/brightness-small.png";
                                } else {
                                    icon = "icons pinvoke/status-offline.png";
                                }
                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                    dispKpi.append("<Td  style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"right\" ><a onclick=\" editTarget('").append(ElementId).append("','").append(kpiMasterid).append("','").append(ElementName).append("','").append(userId).append("','").append(retObjQry.getFieldValueFormatInt(0, temp)).append("','").append(collect.reportId).append("','").append(dashletId).append("','").append(fromDesigner).append("')\" " + "title=\"Click to Define Custom Range\"><img src='").append(icon).append("'></img></a></Td>");
                                } else {
                                    dispKpi.append("<Td align=\"right\" ><a onclick=\" editTarget('").append(ElementId).append("','").append(kpiMasterid).append("','").append(ElementName).append("','").append(userId).append("','").append(retObjQry.getFieldValueFormatInt(0, temp)).append("','").append(collect.reportId).append("','").append(dashletId).append("','").append(fromDesigner).append("')\" " + "title=\"Click to Define Custom Range\"><img src='").append(icon).append("'></img></a></Td>");
                                }
                            } else {
                            }
                        }
                    } else {
                        if (type.equalsIgnoreCase("2")) {
//                           if ((retObjQry.getFieldValueString(0, temp)) == null ) {
                            double[] valArray = {currVal, priorVal};
                            celWidth = new double[valArray.length];
                            for (int j = 0; j < valArray.length; j++) {
                                sum += (valArray[j]);
                            }

                            for (int k = 0; k < valArray.length; k++) {
                                celWidth[k] = ((tableWidth * (valArray[k])) / sum);
                                celWidth[k] = Math.round(celWidth[k]);
                            }
                            kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                            for (int kpi = 0; kpi < valArray.length; kpi++) {
                                String value = retObjQry.getModifiedNumber(new BigDecimal(valArray[kpi]));
                                if (priorVal == 0.0) {
                                    if (kpi == 1) {
                                        continue;
                                    }
                                }
                                if (celWidth[kpi] == 0.0) {
                                    COLORS[kpi] = "#FFFFFF";
                                }
                                if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget"))) {
                                    if (COLORS[kpi] == "") {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                            int index = atrelementIds.indexOf(ElementId);
                                            NumberFormatter nf = new NumberFormatter();
                                            int rounding;
                                            if (round.get(index) == "") {
                                                rounding = 0;
                                            } else {
                                                rounding = Integer.parseInt(round.get(index));
                                            }
                                            String formattedvalue = nf.getModifiedNumber(valArray[kpi], numberFormat.get(index), rounding);

                                            attribute = numberFormat.get(index).toString();
                                            if (!attribute.equalsIgnoreCase("")) {
                                                if (attribute.contains("M")) {
                                                    formattedvalue = formattedvalue.replace("M", "");
                                                }
                                                if (attribute.contains("K")) {
                                                    formattedvalue = formattedvalue.replace("K", "");
                                                }
                                                if (attribute.contains("Cr")) {
                                                    formattedvalue = formattedvalue.replace("Cr", "");
                                                }
                                                if (attribute.contains("L")) {
                                                    formattedvalue = formattedvalue.replace("L", "");
                                                }
                                            }
                                            StringBuffer sb = new StringBuffer();
                                            for (int k = 0; k < formattedvalue.length(); k++) {
                                                sb.append(formattedvalue.charAt(k));
                                            }
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                            arr.add(symbols.get(index) + sb.toString());
                                        } else {
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                            arr.add(value);
                                        }
                                    } else {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                            int index = atrelementIds.indexOf(ElementId);
                                            NumberFormatter nf = new NumberFormatter();
                                            int rounding;
                                            if (round.get(index) == "") {
                                                rounding = 0;
                                            } else {
                                                rounding = Integer.parseInt(round.get(index));
                                            }
                                            String formattedvalue = nf.getModifiedNumber(valArray[kpi], numberFormat.get(index), rounding);
                                            attribute = numberFormat.get(index).toString();
                                            if (!attribute.equalsIgnoreCase("")) {
                                                if (attribute.contains("M")) {
                                                    formattedvalue = formattedvalue.replace("M", "");
                                                }
                                                if (attribute.contains("K")) {
                                                    formattedvalue = formattedvalue.replace("K", "");
                                                }
                                                if (attribute.contains("Cr")) {
                                                    formattedvalue = formattedvalue.replace("Cr", "");
                                                }
                                                if (attribute.contains("L")) {
                                                    formattedvalue = formattedvalue.replace("L", "");
                                                }
                                            }

                                            StringBuffer sb = new StringBuffer();
                                            for (int k = 0; k < formattedvalue.length(); k++) {
                                                sb.append(formattedvalue.charAt(k));
                                            }
                                            if (COLORS[kpi] == "#FFFFFF") {
                                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                    arr.add(symbols.get(index) + sb.toString());
                                                } else {
                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                    arr.add(symbols.get(index) + sb.toString());
                                                }
                                            } else {
                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                arr.add(symbols.get(index) + sb.toString());
                                            }
                                        } else if (COLORS[kpi] == "#FFFFFF") {
                                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                                arr.add(value);
                                            } else {
                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                                arr.add(value);
                                            }
                                        } else {
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                            arr.add(value);
                                        }
                                    }
                                }
                            }
                            kpiBarChart.append("</Tr></Table>");
                            if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Kpi"))) {
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
//                               dispKpi.append("<Td style=\"cursor:pointer;color: " +font.get(atrelementIds.indexOf(ElementId)) + ";background-color:"+background.get(atrelementIds.indexOf(ElementId))+"\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"','"+ElementId+"','"+drillRepType+"')\">" +kpiBarChart.toString()+ "</a></Td>");
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
//                                        arr.add(kpiBarChart.toString());
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
//                                        arr.add(kpiBarChart.toString());
                                    }
                                } else {
                                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
//                                dispKpi.append("<Td style=\"cursor:pointer;color: " +font.get(atrelementIds.indexOf(ElementId)) + ";background-color:"+background.get(atrelementIds.indexOf(ElementId))+"\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"','"+ElementId+"','"+drillRepType+"')\">" +kpiBarChart.toString()+ "</a></Td>");
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
//                                        arr.add(kpiBarChart.toString());
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
//                                        arr.add(kpiBarChart.toString());
                                    }
                                }
                            }
//                          }
                        }
                    }
                } else {

                    if (kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Basic")) {
                        currVal = Double.parseDouble((retObjQry.getFieldValueString(0, temp)));
                        int decimalPlaces = 1;
                        BigDecimal bd = new BigDecimal(currVal);
                        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
                        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                        basicTargetDetails.put("currentValue", formatter.format(bd));
                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                            int index = atrelementIds.indexOf(ElementId);
                            NumberFormatter nf = new NumberFormatter();
                            int rounding;
                            if (round.get(index) == "") {
                                rounding = 0;
                            } else {
                                rounding = Integer.parseInt(round.get(index));
                            }
                            String value = nf.getModifiedNumber(currVal, numberFormat.get(index), rounding);
                            attribute = numberFormat.get(index).toString();
                            if (!attribute.equalsIgnoreCase("")) {
                                if (attribute.contains("M")) {
                                    value = value.replace("M", "");
                                }
                                if (attribute.contains("K")) {
                                    value = value.replace("K", "");
                                }
                                if (attribute.contains("Cr")) {
                                    value = value.replace("Cr", "");
                                }
                                if (attribute.contains("L")) {
                                    value = value.replace("L", "");
                                }
                            }

                            StringBuffer sb = new StringBuffer();
                            for (int k = 0; k < value.length(); k++) {
                                sb.append(value.charAt(k));
                            }
                            basicTargetDetails.put("currVal", sb);
                            if (kpiType.equalsIgnoreCase("Basic")) {
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
//                    dispKpi.append("<Td align=\"center\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"','"+ElementId+"','"+drillRepType+"')\">"+sb+"</a></Td>");
                                    dispKpi.append("<Td align=\"center\">" + sb + "</Td>");
                                    arr.add(sb);
                                }
//                                          else{
//                            dispKpi.append("<Td align=\"center\">"+sb+"</Td>");
//                        }
                            }
                        }
                        grpMapValues.put(ElementId, bd);
                        priorVal = Double.parseDouble((retObjQry.getFieldValueString(0, temp)));
                        simpleKpi.put("currentValue", currVal);
                        simpleKpi.put("priorVal", priorVal);
                        if (kpiType.equalsIgnoreCase("Basic")) {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                dispKpi.append("<Td align=\"center\">" + formatter.format(bd) + "</Td>");
                                arr.add(formatter.format(bd));
                            } else {
                                dispKpi.append("<Td align=\"center\">" + formatter.format(bd) + "</Td>");
                                arr.add(formatter.format(bd));
                            }
                        }
                    } else {
                        if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                int index = atrelementIds.indexOf(ElementId);
//                     dispKpi.append("<Td style=\"cursor:pointer;color: " +font.get(index) + ";background-color:"+background.get(index)+"\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"','"+ElementId+"','"+drillRepType+"')\">--</a></Td>");
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">--</Td>");

                            } else {
                                dispKpi.append("<Td align=\"center\">--</Td>");
                            }
                        } else {
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                int index = atrelementIds.indexOf(ElementId);
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">--</Td>");
                            } else {
                                dispKpi.append("<Td align=\"center\">--</Td>");
                            }
                        }
                    }
                }

            } else {
                temp = "A_" + kpiElements.get(i).getElementId();
                String type = kpiElements.get(i).getRefElementType();
                if ((retObjQry.getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(retObjQry.getFieldValueString(0, temp)))) {
                    if (type.equalsIgnoreCase("1")) {
                        if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Kpi"))) {
                            currVal = Double.parseDouble((retObjQry.getFieldValueString(0, temp)));

                            priorVal = 0;
                            double[] valArray = {currVal, priorVal};
                            celWidth = new double[valArray.length];
                            for (int j = 0; j < valArray.length; j++) {
                                sum += (valArray[j]);
                            }

                            double widthFl = 0;
                            for (int k = 0; k < valArray.length; k++) {
                                widthFl = ((tableWidth * (valArray[k])) / sum);
                                // celWidth[k]=
                                celWidth[k] = ((tableWidth * (valArray[k])) / sum);
                                celWidth[k] = Math.round(celWidth[k]);
                            }
                            kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                            for (int kpi = 0; kpi < valArray.length; kpi++) {
                                String value = retObjQry.getModifiedNumber(new BigDecimal(valArray[kpi]));
                                if (celWidth[kpi] == 0.0) {
                                    COLORS[kpi] = "#FFFFFF";
                                }
                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:9px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                arr.add(value);
                                //}
                            }
                            kpiBarChart.append("</Tr></Table>");
                            if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
//                           dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"','"+ElementId+"','"+drillRepType+"')\">" + kpiBarChart.toString() + "</a></Td>");
                                dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
//                                arr.add(kpiBarChart.toString());
                            } else {
                                dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
//                                arr.add(kpiBarChart.toString());
                            }
                        }

                    } else {
                        dispKpi.append("<Td align=\"right\" >" + retObjQry.getFieldValueFormatInt(0, temp) + "</Td>");
                        arr.add(retObjQry.getFieldValueFormatInt(0, temp));
                    }

                    if (type.equalsIgnoreCase("4")) {
                        if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("Kpi") || kpiType.equalsIgnoreCase("Target"))) {
                            double changePercVal = Double.parseDouble((retObjQry.getFieldValueString(0, temp)));
                            String elemId = kpiElements.get(i).getElementId();
                            String color = kpiDetail.getKPIColor(elemId, changePercVal);
                            String icon = "";
                            if ("Green".equalsIgnoreCase(color)) {
                                icon = "icons pinvoke/status.png";
                            } else if ("Red".equalsIgnoreCase(color)) {
                                icon = "icons pinvoke/status-busy.png";
                            } else if ("Yellow".equalsIgnoreCase(color)) {
                                icon = "icons pinvoke/brightness-small.png";
                            } else {
                                icon = "icons pinvoke/status-offline.png";
                            }

                            dispKpi.append("<Td align=\"right\" ><a onclick=\" editTarget('").append(ElementId).append("','").append(kpiMasterid).append("','").append(ElementName).append("','").append(userId).append("','").append(retObjQry.getFieldValueFormatInt(0, temp)).append("','").append(collect.reportId).append("','").append(dashletId).append("','").append(fromDesigner).append("')\" " + "title=\"Click to Define Custom Range\"><img src='").append(icon).append("'></img></a></Td>");
                        }
                    }
                } else {
                    if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Kpi"))) {
                        if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                int index = atrelementIds.indexOf(ElementId);
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">--</Td>");
                            } else {
                                dispKpi.append("<Td align=\"center\" >--</Td>");
                            }
                        } else {
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty()) {
                                int index = atrelementIds.indexOf(ElementId);
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">--</Td>");
                            } else {
                                dispKpi.append("<Td align=\"center\" >--</Td>");
                            }
                        }
                    }
                }
            }
        }

        for (int j = i; j < 3; j++) {
            if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Kpi"))) {
                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                        int index = atrelementIds.indexOf(ElementId);
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">--</Td>");
                    } else {
                        dispKpi.append("<Td align=\"center\">--</Td>");
                    }
                } else {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                        int index = atrelementIds.indexOf(ElementId);
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">--</Td>");
                    } else {
                        dispKpi.append("<Td align=\"center\">--</Td>");
                    }
                }
            }
        }

        //kpitarget
        if (kpiType.equalsIgnoreCase("Target") || kpiType.equalsIgnoreCase("BasicTarget")) {

            ArrayList timeDim = collect.timeDetailsArray;
            String timeLevel = timeDim.get(3).toString();
            Double targetValue = null;
            int index1 = atrelementIds.indexOf(ElementId);
            int rounding1;
            if (round.get(index1) == "") {
                rounding1 = 0;
            } else {
                rounding1 = Integer.parseInt(round.get(index1));
            }
            if (targetElementId != null && !targetElementId.equalsIgnoreCase("") && !targetElementId.equalsIgnoreCase("null")) {
                if (retObjQry.getFieldValueString(0, "A_" + targetElementId) == null || retObjQry.getFieldValueString(0, "A_" + targetElementId).equalsIgnoreCase("")
                        || retObjQry.getFieldValueString(0, "A_" + targetElementId).equalsIgnoreCase(" ")) {
                    targetValue = Double.parseDouble("0");
                } else {
                    targetValue = Double.parseDouble((retObjQry.getFieldValueString(0, "A_" + targetElementId)));
                }
                istarget = true;
            } else {
                targetValue = kpiDetail.getTargetValue(ElementId, timeLevel);
                //added by sruthi for manuval bud based on dates
                String nodays = dashletDetail.getNumberOfDays();
                String query = "";
                ArrayList queries = new ArrayList();
                String string1 = Integer.toString(days);
                if (nodays != null && !nodays.equalsIgnoreCase("") && !nodays.equalsIgnoreCase("null")) {
                    int latestdays = Integer.parseInt(nodays);
                    HashMap<String, Double> targetdata = dashletDetail.getTargetMauval();
                    Double targetval = null;
                    targetval = targetdata.get(ElementId);
                    if (targetval != null) {
                        if (string1.trim().equalsIgnoreCase(nodays)) {
                            targetValue = targetdata.get(ElementId);
                            Double actuvaltarget = null;
                            actuvaltarget = kpiDetail.getTargetValue(ElementId, timeLevel);
                            if (actuvaltarget != null) {
                                if (actuvaltarget < targetValue || actuvaltarget > targetValue) {
                                    targetValue = kpiDetail.getTargetValue(ElementId, timeLevel);
                                }
                            }
                        } else {
                            targetValue = (targetval / latestdays) * days;
                        }
                    }
                }
                int elementsize = elementids.size();
                if (ElementId == elementids.get(elementsize - 1)) {
                    dashletDetail.setNumberOfDays(string1);
                }
                manuvaltargetval.put(ElementId, targetValue);
                //  List<KPITarget> kpiTargets = kpiDetail.getKPITargets(ElementId);
                if (ElementId == elementids.get(elementsize - 1)) {
                    dashletDetail.setTargetMauval(manuvaltargetval);
                }
                //ended by sruthi
            }
            if (targetValue == null) {
                PbTimeRanges timeRanges = new PbTimeRanges();
                timeRanges.setElementID(ElementId);
                String currPeriodName = timeRanges.getCurrentPeriodName(timeDim.get(2).toString(), timeLevel);
                BigDecimal targetData = kpiDetail.getTargetData(ElementId, timeLevel, currPeriodName);
                if (targetData != null) {
                    targetValue = targetData.doubleValue();
                }

            }
            String basicTargetColor = "#000000";
            String basicDevVal = "--";
            String basicDevPer = "0.0";
            if (targetValue != null) {
                double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(currVal)), targetValue);
                basicDevVal = String.valueOf(DEVAL);
                basicDevVal = NumberFormatter.getModifiedNumber(new BigDecimal(basicDevVal));
                //double DEVAL1 = Double.valueOf(basicDevVal);
                BigDecimal DEVPER = kpiDetail.getDeviationPer(currVal, targetValue);
                basicDevPer = String.valueOf(DEVPER);
                basicDevPer = NumberFormatter.getModifiedNumber(DEVPER) + "%";
                if (basicDevPer.contains("M")) {
                    basicDevPer = (DEVPER) + "%";
                }
                if (basicDevVal.equalsIgnoreCase("-0.0")) {
                    basicDevVal = "0.0";
                    basicDevPer = "0.0";
                }
                String basicKpiType = "Standard";
                String color = kpiDetail.getBasicKpiColor(DEVAL, ElementId);

                basicTargetDetails.put("color", color);

            }
            double perDev = 0;
            String timeDimVal = collect.timeDetailsArray.get(1).toString();
            String targetValueStr = "--";
            double tempDouble = 0.0;
            varLastyear = String.valueOf(currVal - priorVal);//code by Bhargavi
            if (targetValue != null) {
                if (targetValue != 0) {
                    devVal = String.valueOf(((currVal - targetValue) / targetValue) * 100);
                    varBudget = String.valueOf(currVal - targetValue);//code by Bhargavi

                } else {
                    devVal = String.valueOf((currVal) * 100);
                    varBudget = String.valueOf(currVal);//code by Bhargavi
                }
                perDev = new Double(devVal);

//                devVal = NumberFormatter.getModifiedNumber(new BigDecimal(devVal)) + "%";  //comment by mohit
                devVal = NumberFormatter.getModifiedNumberDashboard(new BigDecimal(devVal), "", rounding1);//changed by sruthi for indian rs

                varBudget = NumberFormatter.getModifiedNumberDashboard(new BigDecimal(varBudget), numberFormat.get(index1), rounding1);//changed by sruthi for indian rs

                String nVal = "";

                String currentStr = targetValue.toString();
                String pattern = "###,###";
                double value = Double.parseDouble(currentStr);
                DecimalFormat myFormatter = new DecimalFormat(pattern);
                String output = myFormatter.format(value);
                targetValueStr = output;

                if (targetValueStr.charAt(targetValueStr.length() - 1) == ',') {
                    targetValueStr = targetValueStr.substring(0, targetValueStr.length() - 1);
                }
            }
            varLastyear = NumberFormatter.getModifiedNumberDashboard(new BigDecimal(varLastyear), numberFormat.get(index1), rounding1);//changed by sruthi for indian rs

//            if (!devVal.equalsIgnoreCase("") && !devVal.equalsIgnoreCase("--")) {
//                perDev = Double.parseDouble(devVal.substring(0, devVal.length() - 2));
//            }
            if (perDev < 0.0) {
                targetColor = "Red";
            } else if (perDev > 0.0) {
                targetColor = "Blue";
            }

            if ((kpiType.equalsIgnoreCase("BasicTarget"))) {
                ArrayList tempList = new ArrayList();
                tempList.add(ElementId);
                tempList.add(kpiMasterid);
                tempList.add(timeLevel);
                tempList.add(targetValue);
                tempList.add(collect.reportId);
                tempList.add(dashletId);
                tempList.add(targetValueStr);
                basicTargetDetails.put("targetDetails", tempList);
            } else {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                    int index = atrelementIds.indexOf(ElementId);
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        //added by sruthi for rounding n numberformate
                        String negativeValue = null;
                        if (targetValueStr.equalsIgnoreCase("--")) {
                            negativeValue = (String) targetValueStr;
                        } else {
                            BigDecimal db = new BigDecimal(targetValueStr.replaceAll(",", ""));
                            negativeValue = NumberFormatter.getModifiedNumberDashboard(db, numberFormat.get(index1), rounding1);//changed by sruthi for indian rs
                        }
                        if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                            negativeValue = negativeValue.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                if (istarget) {
                                    if (flag2 == 1)//changed  by sruthi for hide columns
                                    {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + ")" + "</Td>");
                                    }
                                    arr.add("(" + negativeValue + ")");
                                } else {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                    }
                                    arr.add("(" + negativeValue + ")");
                                }
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                if (istarget) {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + negativecolor + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                    }
                                    arr.add(negativeValue);
                                } else {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";display:none;background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"color: " + negativecolor + ";\"onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                    }
                                    arr.add(negativeValue);
                                }
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                if (istarget) {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\"  style=\"cursor:pointer;display:none;color: " + negativecolor + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\"  style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + ")" + "</Td>");
                                    }
                                    arr.add("(" + negativeValue + ")");
                                } else {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"color: " + negativecolor + ";\"onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                    }
                                    arr.add("(" + negativeValue + ")");
                                }
                            } else {
                                if (istarget) {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\"  style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetValueStr + "</Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\"  style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetValueStr + "</Td>");
                                    }
                                    arr.add(targetValueStr);
                                } else {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                                    }
                                    arr.add(targetValueStr);
                                }

                            }//ended by sruthi
                        } //                                     kpiBarChart.append("<Td style=\"width:" + celWidth[1] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" +value  + "</Td>");
                        else {
                            if (istarget) {
                                //added by sruthi for symbol
                                if (symbols.get(index) != null) {
                                    //added by sruthi for negative color
                                    if (symbols.get(index).equalsIgnoreCase("%")) {
                                        if (negativevalues != null && !negativevalues.isEmpty() && !negativeValue.equalsIgnoreCase("--")) {
                                            if (negativeValue.contains("-")) {

                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    if (flag2 == 1)//changed  by sruthi for hide columns
                                                    {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + symbols.get(index) + ")" + "</Td>");
                                                    } else {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + symbols.get(index) + ")" + "</Td>");
                                                    }
                                                    arr.add("(" + negativeValue + symbols.get(index) + ")");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + symbols.get(index) + "</Td>");
                                                    } else {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + symbols.get(index) + "</Td>");
                                                    }
                                                    arr.add(negativeValue + symbols.get(index));
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";display:none;background-color:" + background.get(index) + "\">" + "(" + negativeValue + symbols.get(index) + ")" + "</Td>");
                                                    } else {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + symbols.get(index) + ")" + "</Td>");
                                                    }
                                                    arr.add("(" + negativeValue + symbols.get(index) + ")");
                                                } else {
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + symbols.get(index) + "</Td>");
                                                    } else {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + symbols.get(index) + "</Td>");
                                                    }
                                                    arr.add(negativeValue + symbols.get(index));
                                                }
                                            } else {
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + symbols.get(index) + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + symbols.get(index) + "</Td>");
                                                }
                                                arr.add(negativeValue + symbols.get(index));
                                            }
                                        } else //ended by sruthi
                                        {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                            } else {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                            }
                                        }
                                        ;
                                    } else {
                                        if (negativevalues != null && !negativevalues.isEmpty() && !negativeValue.equalsIgnoreCase("--")) {
                                            if (negativeValue.contains("-")) {
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + symbols.get(index) + negativeValue + ")" + "</Td>");
                                                    } else {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + symbols.get(index) + negativeValue + ")" + "</Td>");
                                                    }
                                                    arr.add("(" + symbols.get(index) + negativeValue + ")");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";display:none;background-color:" + background.get(index) + "\">" + symbols.get(index) + negativeValue + "</Td>");
                                                    } else {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + negativeValue + "</Td>");
                                                    }
                                                    arr.add(symbols.get(index) + negativeValue);
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + symbols.get(index) + negativeValue + ")" + "</Td>");
                                                    } else {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + symbols.get(index) + negativeValue + ")" + "</Td>");
                                                    }
                                                    arr.add("(" + symbols.get(index) + negativeValue + ")");
                                                } else {
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + negativeValue + "</Td>");
                                                    } else {
                                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + negativeValue + "</Td>");
                                                    }
                                                    arr.add(symbols.get(index) + negativeValue);
                                                }
                                            } else {
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + negativeValue + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + negativeValue + "</Td>");
                                                }
                                                arr.add(symbols.get(index) + negativeValue);
                                            }
                                        } else //ended by sruthi
                                        {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                            } else {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                            }
                                            arr.add(negativeValue);
                                        }
                                    }
                                } else {
                                    if (negativevalues != null && !negativevalues.isEmpty() && !negativeValue.equalsIgnoreCase("--")) {
                                        if (negativeValue.contains("-")) {
                                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + ")" + "</Td>");
                                                }

                                                arr.add("(" + negativeValue + ")");
                                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                String negativecolor = "#f24040";
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                                }
                                                arr.add(negativeValue);
                                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                String negativecolor = "#f24040";
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + "(" + negativeValue + ")" + "</Td>");
                                                }
                                                arr.add("(" + negativeValue + ")");

                                            } else {
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                                }
                                                arr.add(negativeValue);
                                            }
                                        } else {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                            } else {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                            }
                                            arr.add(negativeValue);
                                        }
                                    } else //ended by sruthi
                                    {
                                        if (flag2 == 1) {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                        }
                                        arr.add(negativeValue);
                                    }
                                } //ended by sruthi
                            } else {
                                if (symbols.get(index) != null) {
                                    if (symbols.get(index).equalsIgnoreCase("%")) {
                                        //added by sruthi for negative color
                                        if (negativevalues != null && !negativevalues.isEmpty() && !negativeValue.equalsIgnoreCase("--")) {
                                            if (negativeValue.contains("-")) {
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td style=\"height:6px;display:none;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + symbols.get(index) + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + symbols.get(index) + ")" + "</a></Td>");
                                                    }
                                                    arr.add("(" + negativeValue + symbols.get(index) + ")");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;display:none;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + symbols.get(index) + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + symbols.get(index) + "</a></Td>");
                                                    }
                                                    arr.add(negativeValue + symbols.get(index));
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;display:none;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + symbols.get(index) + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + symbols.get(index) + ")" + "</a></Td>");
                                                    }
                                                    arr.add("(" + negativeValue + symbols.get(index) + ")");
                                                } else {
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;display:none;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                                    }
                                                    arr.add(symbols.get(index) + negativeValue);
                                                }
                                            } else {
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;display:none;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + symbols.get(index) + "</a></Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + symbols.get(index) + "</a></Td>");
                                                }
                                                arr.add(negativeValue + symbols.get(index));
                                            }
                                        } else //ended by sruthi
                                        {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                            }
                                            arr.add(negativeValue);
                                        }
                                    } else {
                                        if (negativevalues != null && !negativevalues.isEmpty() && !negativeValue.equalsIgnoreCase("--")) {
                                            if (negativeValue.contains("-")) {
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + symbols.get(index) + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"  onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + symbols.get(index) + negativeValue + ")" + "</a></Td>");
                                                    }
                                                    arr.add("(" + symbols.get(index) + negativeValue + ")");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;display:none;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                                    }
                                                    arr.add(symbols.get(index) + negativeValue);
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + symbols.get(index) + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + symbols.get(index) + negativeValue + ")" + "</a></Td>");
                                                    }
                                                    arr.add("(" + symbols.get(index) + negativeValue + ")");
                                                } else {
                                                    if (flag2 == 1) {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\" style=\"color: " + font.get(index) + ";\"onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                                    }
                                                    arr.add(symbols.get(index) + negativeValue);
                                                }
                                            } else {
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                                }
                                                arr.add(symbols.get(index) + negativeValue);
                                            }
                                        } else //ended by sruthi
                                        {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                            }
                                            arr.add(negativeValue);
                                        }
                                    }
                                } else {
                                    //added by sruthi for negative color
                                    if (negativevalues != null && !negativevalues.isEmpty() && !negativeValue.equalsIgnoreCase("--")) {
                                        if (negativeValue.contains("-")) {
                                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                }
                                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                String negativecolor = "#f24040";
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                String negativecolor = "#f24040";
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + negativecolor + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                }
                                            } else {
                                                if (flag2 == 1) {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            }
                                        } else {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";display:none;color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                            }
                                            arr.add(negativeValue);
                                        }
                                    } else //ended by sruthi
                                    {
                                        if (flag2 == 1) {
                                            dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\" style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\"onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"height:6px;font-size:10px;font-family:VERDANA;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";color:#fff;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\"style=\"color: " + font.get(atrelementIds.indexOf(ElementId)) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                        }
                                        arr.add(negativeValue);
                                    }
                                }

                            }
                        }
                    } else {
                        if (istarget) {
                            if (flag2 == 1) {
                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetValueStr + "</Td>");
                            } else {
                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetValueStr + "</Td>");
                            }
                            arr.add(targetValueStr);
                        } else {
                            if (flag2 == 1) {
                                dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(index) + ";background-color:" + background.get(index) + "\"><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\"><a href=\"javascript:void(0)\"style=\"color: " + font.get(index) + ";\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                            }
                            arr.add(targetValueStr);
                        }

                    }
                } else {
                    if (istarget) {
                        if (flag2 == 1) {
                            dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\"  style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + targetValueStr + "</Td>");
                        } else {
                            dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\"  style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + targetValueStr + "</Td>");
                        }
                        arr.add(targetValueStr);
                    } else {
                        if (flag2 == 1) {
                            dispKpi.append("<Td align=\"center\" style=\"display:none;\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                        } else {
                            dispKpi.append("<Td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                        }
                        arr.add(targetValueStr);
                    }

                }//ended by sruthi
            }
            if (!(kpiType.equalsIgnoreCase("BasicTarget"))) {
                if (kpiType.equalsIgnoreCase("Target")) {
                    kpiBarChart = new StringBuilder();
                    int index = atrelementIds.indexOf(ElementId);
                    NumberFormatter nf = new NumberFormatter();
                    int rounding;
                    if (round.get(index) == "") {
                        rounding = 0;
                    } else {
                        rounding = Integer.parseInt(round.get(index));
                    }
                    String value = nf.getModifiedNumberDashboard(currVal, numberFormat.get(index), rounding);//changed by sruthi for indian rs
                    kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                    //added by sruthi for symbol
                    if (symbols.get(index) != null) {
                        if (symbols.get(index).equalsIgnoreCase("%")) {
                            kpiBarChart.append("<Td style=\"width:" + celWidth[1] + "px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + symbols.get(index) + "</Td>");
                            arr.add(value + symbols.get(index));
                        } else {
                            kpiBarChart.append("<Td style=\"width:" + celWidth[1] + "px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + value + "</Td>");
                            arr.add(symbols.get(index) + value);
                        }
                    } else {
                        kpiBarChart.append("<Td style=\"width:" + celWidth[1] + "px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                        arr.add(value);
                    } //ended by sruthi
                    kpiBarChart.append("</Tr></Table>");
                    if (flag3 == 1) {
                        dispKpi.append("<Td align=\"center\" padding=\"0px\" style=\"background-color:" + background.get(index) + ";display:none;\">" + kpiBarChart.toString() + "</Td>");
                    } else {
                        dispKpi.append("<Td align=\"center\" padding=\"0px\" style=\"background-color:" + background.get(index) + ";\">" + kpiBarChart.toString() + "</Td>");
                    }
                } else if (devVal.equalsIgnoreCase("--")) {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                        int index = atrelementIds.indexOf(ElementId);
                        if (negativevalues != null && !negativevalues.isEmpty()) {
                            String negativeValue = (String) devVal;
                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                negativeValue = negativeValue.replace("-", " ");
                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                    arr.add("(" + negativeValue + ")");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                    String negativecolor = "#f24040";
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                    arr.add(negativeValue);
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                    String negativecolor = "#f24040";
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                    arr.add("(" + negativeValue + ")");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + devVal + "</Td>");
                                    arr.add(devVal);
                                }
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                arr.add(negativeValue);
                            }
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\">" + devVal + "</Td>");
                            arr.add(devVal);
                        }
                    } else {
                        dispKpi.append("<Td align=\"center\">" + devVal + "</Td>");
                    }
                } else {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                        int index = atrelementIds.indexOf(ElementId);
                        if (negativevalues != null && !negativevalues.isEmpty()) {
                            String negativeValue = (String) devVal;
                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                negativeValue = negativeValue.replace("-", " ");
                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                    arr.add("(" + negativeValue + ")");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                    String negativecolor = "#f24040";
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                    arr.add(negativeValue);
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                    String negativecolor = "#f24040";
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                    arr.add("(" + negativeValue + ")");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + devVal + "</Td>");
                                    arr.add(devVal);
                                }
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                arr.add(negativeValue);
                            }
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\">" + devVal + "</Td>");
                            arr.add(devVal);
                        }
                    } else {
                        dispKpi.append("<Td align=\"center\" style=\"color:" + targetColor + "\">" + devVal + "</Td>");
                        arr.add(devVal);
                    }
                }
            } else if (kpiType.equalsIgnoreCase("BasicTarget")) {
                basicTargetDetails.put("deviationvalue", basicDevVal);
                basicTargetDetails.put("deviationPercent", basicDevPer);
                basicTargetDetails.put("targetValue", targetValue);
                String tempStr = buildPartialBasicTargetKpi(basicTargetDetails);
                dispKpi.append(tempStr);

            }
        } else if (kpiType.equalsIgnoreCase("Kpi")) {
            String temStrKpi = buildPartialKpi(simpleKpi);
            dispKpi.append(temStrKpi);
        }

        //To add the insight icon
        if (!kpiType.equalsIgnoreCase("Kpi")) {
            if (!SchedulerFlag) {
                if (!collect.isOneviewCheckForKpis()) {
                    if (kpiType.equalsIgnoreCase("Target")) {
                        //added by sruthi for symbol
                        int index = atrelementIds.indexOf(ElementId);
                        if (symbols.get(index) != null) {
                            if (symbols.get(index).equalsIgnoreCase("%")) {
                                //added by sruthi for negative color
                                if (negativevalues != null && !negativevalues.isEmpty() && !varBudget.equalsIgnoreCase("--")) {
                                    if (varBudget.contains("-")) {
                                        if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                            if (flag4 == 1)//changed by sruthi for hide columns
                                            {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + symbols.get(index) + ")" + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + symbols.get(index) + ")" + "</Td>");
                                            }
                                            arr.add("(" + varBudget + symbols.get(index) + ")");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                            }
                                            arr.add(varBudget + symbols.get(index));
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + symbols.get(index) + ")" + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + symbols.get(index) + ")" + "</Td>");
                                            }
                                            arr.add("(" + varBudget + symbols.get(index) + ")");
                                        } else {
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                            }
                                            arr.add(varBudget + symbols.get(index));
                                        }
                                    } else {
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                        }
                                        arr.add(varBudget + symbols.get(index));
                                    }
                                } else //ended by sruthi
                                {
                                    if (flag4 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    }
                                    arr.add(varBudget);
                                }
                            } else {
                                if (negativevalues != null && !negativevalues.isEmpty() && !varBudget.equalsIgnoreCase("--")) {
                                    if (varBudget.contains("-")) {
                                        if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varBudget + ")" + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varBudget + ")" + "</Td>");
                                            }
                                            arr.add("(" + symbols.get(index) + varBudget + ")");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                            }
                                            arr.add(symbols.get(index) + varBudget);
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varBudget + ")" + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varBudget + ")" + "</Td>");
                                            }
                                            arr.add("(" + symbols.get(index) + varBudget + ")");
                                        } else {
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                            }
                                            arr.add(symbols.get(index) + varBudget);
                                        }
                                    } else {
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                        }
                                        arr.add(symbols.get(index) + varBudget);
                                    }
                                } else //ended by sruthi
                                {
                                    if (flag4 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    }
                                    arr.add(varBudget);
                                }
                            }

                        } else {
                            //added by sruthi for negative color
                            if (negativevalues != null && !negativevalues.isEmpty() && !varBudget.equalsIgnoreCase("--")) {
                                if (varBudget.contains("-")) {
                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + ")" + "</Td>");
                                        }
                                        arr.add("(" + varBudget + ")");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                        }
                                        arr.add(varBudget);
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + ")" + "</Td>");
                                        }
                                        arr.add("(" + varBudget + ")");   //changed by mohit
                                    } else {
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                        }
                                        arr.add(varBudget);
                                    }
                                } else {
                                    if (flag4 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    }
                                    arr.add(varBudget);
                                }
                            } else //ended by sruthi
                            {
                                if (flag4 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                }

                                arr.add(varBudget);
                            }
                        }//ended by sruthi

                    } else {
                        if (kpiDetail.isShowInsights() || fromDesigner) {
                            if (PrivilegeManager.isModuleEnabledForUser("INSIGHT", Integer.parseInt(userId))) {
                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                    dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a onclick=\"gotoInsight('" + ElementId + "'," + kpiMasterid + ");\"><span class=\"ui-icon ui-icon-search\"></span></a></td>");

                                } else {
                                    dispKpi.append("<td align=\"center\"><a onclick=\"gotoInsight('" + ElementId + "'," + kpiMasterid + ");\"><span class=\"ui-icon ui-icon-search\"></span></a></td>");
                                }
                            }
                        }
                    }
                }
                List<KPIComment> kpiComments = kpiDetail.getKPIComments(ElementId);
                if (!collect.isOneviewCheckForKpis()) {
                    if (kpiType.equalsIgnoreCase("Target")) {
                        //added by sruthi for symbol
                        int index = atrelementIds.indexOf(ElementId);
                        //added by sruthi for negative color
                        if (negativevalues != null && !negativevalues.isEmpty() && !devVal.equalsIgnoreCase("--")) {
                            if (devVal.contains("-")) {
                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                    if (flag5 == 1)//changed by sruthi for hide columns
                                    {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + devVal + "%" + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + devVal + "%" + ")" + "</Td>");
                                    }
                                    arr.add("(" + devVal + "%" + ")");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag5 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + " </Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + " </Td>");
                                    }
                                    arr.add(devVal + "%");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag5 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + devVal + "%" + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + devVal + "%" + ")" + "</Td>");
                                    }
                                    arr.add("(" + devVal + "%" + ")");
                                } else {
                                    if (flag5 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                    } //         }else     dispKpi.append("<Td style=\"cursor:pointer;text-align:"+alignment.get(atrelementIds.indexOf(ElementId))+";background-color:"+background.get(atrelementIds.indexOf(ElementId))+"\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " +font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index)+devVal + "</Td>");
                                    else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                    }
                                    arr.add(devVal + "%");
                                }
                            } else {
                                if (flag5 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                }
                                arr.add(devVal + "%");
                            }
                        } else //ended by sruthi
                        {
                            if (flag5 == 1) {
                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "</Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "</Td>");
                            }

                            arr.add(devVal);
                        }
                        //ended by sruthi

                    }
                }
                if (kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Basic")) {
                    Double targetValue = (Double) basicTargetDetails.get("targetValue");
                    String deviationPercent = (String) basicTargetDetails.get("deviationPercent");
                }
                if ((!kpiType.equalsIgnoreCase("Standard")) && !(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget"))) {
                    if (!collect.isOneviewCheckForKpis()) {
                        if (!kpiType.equalsIgnoreCase("Target") && kpiDetail.isShowGraphs()) {
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {

                                dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\" onclick=\"ZoomTargetGraph('" + viewbycolumnsstr + "','" + barchartcolumntitlesstr + "','" + keyvaluesstr + "','" + datavaluesstr + "')\" title=\" \">" + imgdata + "</Td>");

                            } else {
                                dispKpi.append("<Td align=\"center\" ><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\" onclick=\"ZoomTargetGraph('" + viewbycolumnsstr + "','" + barchartcolumntitlesstr + "','" + keyvaluesstr + "','" + datavaluesstr + "')\" title=\" \">" + imgdata + "</Td>");
                            }
                        }
                    }
                }
                if (kpiType.equalsIgnoreCase("Target")) {
                    //added by sruthi for symbol
                    int index = atrelementIds.indexOf(ElementId);
                    if (symbols.get(index) != null) {
                        if (symbols.get(index).equalsIgnoreCase("%")) {
                            //added by sruthi for negative color
                            if (negativevalues != null && !negativevalues.isEmpty() && !varLastyear.equalsIgnoreCase("--")) {
                                if (varLastyear.contains("-")) {
                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                        if (flag6 == 1)//changed by sruthi for hide columns
                                        {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + symbols.get(index) + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + symbols.get(index) + ")" + "</Td>");
                                        }
                                        arr.add("(" + varLastyear + symbols.get(index) + ")");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                        }
                                        arr.add(varLastyear + symbols.get(index));
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + symbols.get(index) + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + symbols.get(index) + ")" + "</Td>");
                                        }
                                        arr.add("(" + varLastyear + symbols.get(index) + ")");
                                    } else {
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:nonetext-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                        }
                                        arr.add(varLastyear + symbols.get(index));
                                    }
                                } else {
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                    }
                                    arr.add(varLastyear + symbols.get(index));
                                }
                            } else //ended by sruthi
                            {
                                if (flag6 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                }
                                arr.add(varLastyear);
                            }
                        } else {
                            if (negativevalues != null && !negativevalues.isEmpty() && !varLastyear.equalsIgnoreCase("--")) {
                                if (varLastyear.contains("-")) {
                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varLastyear + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varLastyear + ")" + "</Td>");
                                        }
                                        arr.add("(" + symbols.get(index) + varLastyear + ")");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                        }
                                        arr.add(symbols.get(index) + varLastyear);
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varLastyear + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varLastyear + ")" + "</Td>");
                                        }
                                        arr.add("(" + symbols.get(index) + varLastyear + ")");
                                    } else {
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                        }
                                        arr.add(symbols.get(index) + varLastyear);
                                    }
                                } else {
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                    }
                                    arr.add(symbols.get(index) + varLastyear);
                                }
                            } else //ended by sruthi
                            {
                                if (flag6 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                }
                                arr.add(varLastyear);
                            }
                        }
                    } else {
                        //added by sruthi for negative color
                        if (negativevalues != null && !negativevalues.isEmpty()) {
                            if (varLastyear.contains("-")) {
                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + ")" + "</Td>");
                                    }
                                    arr.add("(" + varLastyear + ")");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                    }
                                    arr.add(varLastyear);
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + ")" + "</Td>");
                                    }
                                    arr.add("(" + varLastyear + ")");
                                } else {
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                    }
                                    arr.add(varLastyear);
                                }
                            } else {
                                if (flag6 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                }
                                arr.add(varLastyear);
                            }
                        } else //ended by sruthi
                        {
                            if (flag6 == 1) {
                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                            }
                            arr.add(varLastyear);
                        }
                    }
//ended by sruthi
                    //bud
                }

                // dispKpi.append("<Td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"javascript:dashboardKpiAlerts('"+ElementId+"',"+kpiMasterid+",'" + dashletId + "');\" style=\"text-decoration:none;cursor:pointer;\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
                if (!collect.isOneviewCheckForKpis()) {
                    int index = atrelementIds.indexOf(ElementId);
                    Double targetValue = (Double) basicTargetDetails.get("targetValue");
                    String deviationPercent = (String) basicTargetDetails.get("deviationPercent");
                    NumberFormatter nf = new NumberFormatter();
                    int rounding;
                    if (round.get(index) == "") {
                        rounding = 0;
                    } else {
                        rounding = Integer.parseInt(round.get(index));
                    }
                    if (kpiType.equalsIgnoreCase("Target")) {
                        BigDecimal changePercVal = (retObjQry.getFieldValueBigDecimal(0, temp));
                        changePercVal = changePercVal.setScale(2, RoundingMode.CEILING);
                        //added by sruthi for symbol
                        String changePercdata = nf.getModifiedNumberDashboard(changePercVal, "", rounding);
                        //added by sruthi for negative color
                        if (negativevalues != null && !negativevalues.isEmpty()) {
                            if (changePercdata.contains("-")) {
                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                    if (flag7 == 1)//changed by sruthi for hide columns
                                    {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + changePercdata + "%" + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + changePercdata + "%" + ")" + "</Td>");
                                    }
                                    arr.add("(" + changePercdata + "%" + ")");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag7 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;color:" + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color:" + negativecolor + ";cursor:pointer;\"  title=\" \">" + changePercdata + "%" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;color:" + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color:" + negativecolor + ";cursor:pointer;\"  title=\" \">" + changePercdata + "%" + "</Td>");
                                    }
                                    arr.add(changePercdata + "%");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag7 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;color:" + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color:" + negativecolor + ";color:" + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + changePercdata + "%" + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;color:" + negativecolor + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color:" + negativecolor + ";color:" + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + changePercdata + "%" + ")" + "</Td>");
                                    }
                                    arr.add("(" + changePercdata + "%" + ")");
                                } else //  dispKpi.append("<Td style=\"cursor:pointer;text-align:"+alignment.get(atrelementIds.indexOf(ElementId))+";background-color:"+background.get(atrelementIds.indexOf(ElementId))+"\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\"  title=\" \">" +symbols.get(index)+ changePercdata + "</Td>");
                                {
                                    if (flag7 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercdata + "%" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercdata + "%" + "</Td>");
                                    }
                                    arr.add(changePercdata + "%");
                                }
                            } else {
                                if (flag7 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercdata + "%" + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercdata + "%" + "</Td>");
                                }
                                arr.add(changePercdata + "%");
                            }
                        } else //ended by sruthi
                        {
                            if (flag7 == 1) {
                                dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercdata + "</Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercdata + "</Td>");
                            }
                            arr.add(changePercdata);
                        }

                        //ended by sruthi
                    }
                    //comment by mohit
                    if (kpiType.equalsIgnoreCase("Target")) {
                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                            //dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(index) + ";\" align=\"center\" padding=\"0px\"><a onclick= \"viewSchedulerAndTracker('" + new Double(oneDForm.format(currVal)) + "','" + ElementId + "','" + kpiMasterid + "','" + dashletId + "','" + collect.reportId + "','" + targetValue + "','" + deviationPercent + "','" + kpiType + "','" + ElementName + "')\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
                            dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(index) + ";\" align=\"center\" padding=\"0px\"><a onclick= \"viewAlertCondition('" + new Double(oneDForm.format(currVal)) + "','" + ElementId + "','" + kpiMasterid + "','" + dashletId + "','" + collect.reportId + "','" + targetValue + "','" + deviationPercent + "','" + kpiType + "','" + ElementName + "','" + container.getCustomKPIHeaderNames() + "','" + isViewby + "','" + isViewbySecond + "')\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
                            //comment by mohit
                            dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\" >");
                            dispKpi.append("<img width='15' height='15' style='float:middle;cursor:pointer;' onclick=\"dashgenerateQuickTrend('" + ReportId + "','" + dashletId + "','" + ElementId + "','" + ElementName + "','" + targetElementId + "','" + targetElemName + "','" + priorid + "','" + viewbyname + "','" + viewbyid + "')\" 'title=\"Click to Zoom \" src='images/chart1.png'/></td>");

                        }
                    }
                    //dispKpi.append("</Td>");
                    dispKpi.append("</Tr>");
                }
            }
        }
        calculativeVal.add(arr);
        return dispKpi.toString();
    }
    //Added by Mohit for  Viewby Visualization of Dashboard table

    public String displayKpiForTarget(Container container, PbReturnObject retObjQry, int totalrow, int totalelements, boolean isViewByAdd, boolean isGtShow, String ElementId, String ElementName, String ReportId, String kpiMasterid,
            String kpiDrill, String kpiType, PbReturnObject pbretObjForTime, DashletDetail dashletDetail, boolean fromDesigner,
            pbDashboardCollection collect, String userId, int m, HashMap<String, String> aggid, ArrayList ViewBy) throws Exception {
        ArrayList arr = new ArrayList();
        ArrayList grTotal = new ArrayList();
        String attribute = "";
        retObjQry.setProcessGT(true);
        retObjQry.prepareObject(retObjQry);
        String targetElementId = null;
        String targetElemName = null;
        PbReturnObject retobj = null;
        boolean istarget = false;
        List<KPIElement> targetkpielem = new ArrayList<KPIElement>();
        ArrayList<String> viewbyvalues = new ArrayList<String>();
        viewbyvalues.add(retObjQry.getFieldValueString(totalrow, 0));
        viewbyvalues.add(retObjQry.getFieldValueString(totalrow, 1));
        String kpiSymbol = dashletDetail.getKpiSymbol();
        HashMap basicTargetDetails = new HashMap();
        HashMap simpleKpi = new HashMap();
        String folderDetails = collect.reportBizRoles[0];
        DecimalFormat oneDForm = new DecimalFormat("#.0");
        String kpiComment = null;
        String[] COLORS = {"#0095B6", "#9ACD32", "#007BA7", "#0095B6", "#78866B", "#00FFFF", "#0067A5", "#9ACD32", "#FEFE33", "#4F7942", "#CFB53B", "#006600", "#003300", "#ecddoo",};
        int tableWidth = 200;
        int sum = 0;
        double currVal = 0.0;
        double priorVal = 0.0;
        String temp = null;
        String varBudget = "--";
        String devVal = "--";
        String targetid = null;
        String userColType = "";
        String priorid = null;
        BigDecimal currentval = null;
        BigDecimal priorval = null;
        BigDecimal targetval = null;
        String viewbyname = null;
//String priorid=null;
        PbDb pbdb = new PbDb();
        String varLastyear = "--";
        StringBuilder kpiBarChart = new StringBuilder();
        StringBuilder kpiBarChart1 = new StringBuilder();
        KPI kpiDetail = (KPI) dashletDetail.getReportDetails();
        String dashletId = dashletDetail.getDashBoardDetailId();
        List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);

        StringBuilder dispKpi = new StringBuilder();
        String drillReportId = kpiDetail.getKPIDrill(ElementId);
        String drillRepType = kpiDetail.getKPIDrillRepType(ElementId);
        if (kpiDetail.getKpiTragetMap(ElementId) != null && !kpiDetail.getKpiTragetMap(ElementId).equalsIgnoreCase("")) {
            targetElementId = kpiDetail.getKpiTragetMap(ElementId);
        }
        if (targetElementId != null && !targetElementId.equalsIgnoreCase("")) {

            targetkpielem = kpiDetail.getTargetKPIElements(targetElementId);
        }
        if (targetkpielem != null) {
            for (KPIElement elem : targetkpielem) {
                targetElemName = elem.getElementName();
            }
        }
        dispKpi.append("<Tr>");
        //added by sruthi for grand total
        if (totalrow == retObjQry.getRowCount() - 1) {
            DisplayGT.append("<Tr>");
        }
        //ended by sruthi
        if (!collect.isOneviewCheckForKpis()) {
            if (container.isPowerAnalyserEnableforUser) {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                    if (background != null && !background.isEmpty()) {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Assign Target Measure\" onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Assign Target Measure\" onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
                    }
                } else {
                    dispKpi.append("<Td align=\"center\" padding=\"0px\" width=\"250px\"><a title=\"Assign Target Measure\" onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
                }

                if (!SchedulerFlag) {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null && background.get(atrelementIds.indexOf(ElementId)) != null) {
                        dispKpi.append("<td align='center' style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "','" + kpiMasterid + "')\"></a></td>");
                    } else {
                        dispKpi.append("<td align='center' style=\"cursor:pointer\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "','" + kpiMasterid + "')\"></a></td>");
                    }
                }
            }
        }
        if ((drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) && (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId))) {
            if (!background.isEmpty() && background != null) {
                if (isViewByAdd) {
                    dispKpi.append("<Td rowspan=" + colspan + " class=\"viewbyname\" style=\"cursor:pointer;display:" + display + ";vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 0) + "</strong></Td>");
                    if (isViewby.equalsIgnoreCase(retObjQry.getFieldValueString(totalrow, 0))) {
                        arr.add("");
                    } else {
                        arr.add(retObjQry.getFieldValueString(totalrow, 0));
                    }
                    isViewby = retObjQry.getFieldValueString(totalrow, 0);

                    if (ViewBy.size() > 1) {
                        dispKpi.append("<Td rowspan=" + totalelements + " class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 1) + "</strong></Td>");
                        arr.add(retObjQry.getFieldValueString(totalrow, 1));
                    }
                    viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                }
                if (totalrow == retObjQry.getRowCount() - 1) {
                    if (isGtShow) {
                        if (container.isPowerAnalyserEnableforUser) {
                            if (ViewBy.size() > 1) {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=4 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
//  grTotal.add( "Grand Total");
                            } else {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=3 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                            }
                        } else {
                            if (ViewBy.size() > 1) //added by mohit
                            {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=2 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                            } else {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=1 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                            }

                        }
                        grTotal.add("Grand Total");
                    }
                    if (!isGtShow) {
                        grTotal.add("");
                    }
                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                        if (drillRepType != null && !("0".equals(drillRepType)) && !drillRepType.equalsIgnoreCase("") && drillRepType.equalsIgnoreCase("D")) {
                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><a style='decoration:none;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        } else {
                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><a style='decoration:none;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        }
                    } else {
                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + ElementName + "</strong></Td>");
                    }
                    grTotal.add(ElementName);

                }//ended by  sruthi
                //DisplayGT.append("<Td rowspan="+totalelements+" colspan=3 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:"+alignment.get(atrelementIds.indexOf(ElementId))+";color:"+font.get(atrelementIds.indexOf(ElementId))+";background-color:"+background.get(atrelementIds.indexOf(ElementId))+";\"><strong>Grand Total</strong></Td>");
                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><a style='decoration:none' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                arr.add(ElementName);
            } else {
                if (isViewByAdd) {
                    dispKpi.append("<Td rowspan=" + colspan + " class=\"viewbyname\" style=\"cursor:pointer;display:" + display + ";vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 0) + "</strong></Td>");
                    if (isViewby.equalsIgnoreCase(retObjQry.getFieldValueString(totalrow, 0))) {
                        arr.add("");
                    } else {
                        arr.add(retObjQry.getFieldValueString(totalrow, 0));
                    }
                    isViewby = retObjQry.getFieldValueString(totalrow, 0);

                    if (ViewBy.size() > 1) {
                        dispKpi.append("<Td rowspan=" + totalelements + " class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 1) + "</strong></Td>");
                        arr.add(retObjQry.getFieldValueString(totalrow, 0));
                    }
                    viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                }
                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + "\"><a style='decoration:none' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                if (totalrow == retObjQry.getRowCount() - 1) {
                    if (isGtShow) {
                        if (container.isPowerAnalyserEnableforUser) {
                            if (ViewBy.size() > 1) {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=4 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");

                            } else {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=3 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                            }
                        } else {    //added by mohit
                            if (ViewBy.size() > 1) {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=2 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                            } else {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=1 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                            }

                        }
                        grTotal.add("Grand Total");
                    }
                    if (!isGtShow) {
                        grTotal.add("");
                    }
                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                        if (drillRepType != null && !("0".equals(drillRepType)) && !drillRepType.equalsIgnoreCase("") && drillRepType.equalsIgnoreCase("D")) {
                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><a style='decoration:none;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        } else {
                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><a style='decoration:none;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        }
                    } else {
                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + ElementName + "</strong></Td>");
                    }
                    grTotal.add(ElementName);
                }//ended by  sruthi
            }
        } else if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
            if (drillRepType != null && !("0".equals(drillRepType)) && !drillRepType.equalsIgnoreCase("") && drillRepType.equalsIgnoreCase("D")) {
                if (!isViewByAdd) {           //Adding by Ram for stroring data of one and two viewbys dashboard
                    if (ViewBy.size() > 1) {
                        arr.add("");
                        arr.add("");
                    } else {
                        arr.add("");
                    }
                }
                if (isViewby.equalsIgnoreCase(retObjQry.getFieldValueString(totalrow, 0))) {
                    arr.add("");
                } else {
                    arr.add(retObjQry.getFieldValueString(totalrow, 0));
                }

                if (isViewByAdd) {
                    dispKpi.append("<Td rowspan=" + colspan + " class=\"viewbyname\" style=\"cursor:pointer;display:" + display + ";vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 0) + "</strong></Td>");
                    if (ViewBy.size() > 1) {
                        dispKpi.append("<Td rowspan=" + totalelements + " class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 1) + "</strong></Td>");
                        arr.add(retObjQry.getFieldValueString(totalrow, 1));
                    }
                    viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                }
                dispKpi.append("<Td><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                //function added by mohit for DrillWithFilters
            } else {
                if (isViewByAdd) {
                    dispKpi.append("<Td rowspan=" + colspan + " class=\"viewbyname\" style=\"cursor:pointer;display:" + display + ";vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 0) + "</strong></Td>");
                    if (ViewBy.size() > 1) {
                        dispKpi.append("<Td rowspan=" + totalelements + " class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 1) + "</strong></Td>");

                    }
                    viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                }
                dispKpi.append("<Td><a style='decoration:none' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                viewbyname = retObjQry.getFieldValueString(totalrow, 0);
            }

        } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
            if (!background.isEmpty() && background != null) {
                if (!isViewByAdd) {           //Adding by Ram for stroring data of one and two viewbys dashboard
                    if (ViewBy.size() > 1) {
                        arr.add("");
                        arr.add("");
                    } else {
                        arr.add("");
                    }
                }
                //end Ram Code
                if (isViewByAdd) {
                    dispKpi.append("<Td rowspan=" + colspan + " class=\"viewbyname\" style=\"cursor:pointer;display:" + display + ";vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 0) + "</strong></Td>");

                    if (isViewby.equalsIgnoreCase(retObjQry.getFieldValueString(totalrow, 0))) {
                        arr.add("");
                    } else {
                        arr.add(retObjQry.getFieldValueString(totalrow, 0));
                    }
                    isViewby = retObjQry.getFieldValueString(totalrow, 0);
                    if (ViewBy.size() > 1) {
                        dispKpi.append("<Td rowspan=" + totalelements + " class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 1) + "</strong></Td>");
                        arr.add(retObjQry.getFieldValueString(totalrow, 1));
                        isViewbySecond = retObjQry.getFieldValueString(totalrow, 1);//added by Dinanath
                    }
                    viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                }
                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
                arr.add(ElementName);
                viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                //added by sruthi for grand total
                if (totalrow == retObjQry.getRowCount() - 1) {
                    if (isGtShow) {
                        if (container.isPowerAnalyserEnableforUser) {
                            if (ViewBy.size() > 1) {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=4 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                                grTotal.add("Grand Total");
                            } else {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=3 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                                grTotal.add("Grand Total");
                            }
                        } else {
                            if (ViewBy.size() > 1) {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=2 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                            } else {
                                DisplayGT.append("<Td rowspan=" + totalelements + " colspan=1 class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:center;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>Grand Total</strong></Td>");
                            }
                            grTotal.add("Grand Total");
                        }
                    }
                    if (!isGtShow) {
                        grTotal.add("");
                    }
                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                        if (drillRepType != null && !("0".equals(drillRepType)) && !drillRepType.equalsIgnoreCase("") && drillRepType.equalsIgnoreCase("D")) {
                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><a style='decoration:none;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        } else {
                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><a style='decoration:none;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";' href=\"javascript:DrillWithFilters('reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        }
                    } else {
                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + ElementName + "</strong></Td>");
                    }
                    grTotal.add(ElementName);
                }//ended by  sruthi
            } else if (!alignment.isEmpty() && alignment != null) {
                if (isViewByAdd) {
                    dispKpi.append("<Td rowspan=" + colspan + " class=\"viewbyname\" style=\"cursor:pointer;display:" + display + ";vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 0) + "</strong></Td>");
                    if (ViewBy.size() > 1) {
                        dispKpi.append("<Td rowspan=" + totalelements + " class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 1) + "</strong></Td>");

                    }
                    viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                }
                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
                viewbyname = retObjQry.getFieldValueString(totalrow, 0);
            } else {
                if (isViewByAdd) {
                    dispKpi.append("<Td rowspan=" + colspan + " class=\"viewbyname\" style=\"cursor:pointer;display:" + display + ";vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 0) + "</strong></Td>");
                    if (ViewBy.size() > 1) {
                        dispKpi.append("<Td rowspan=" + totalelements + " class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 1) + "</strong></Td>");

                    }
                    viewbyname = retObjQry.getFieldValueString(totalrow, 0);
                }
                dispKpi.append("<Td style=\"cursor:pointer\"><strong>" + ElementName + "</strong></Td>");
                viewbyname = retObjQry.getFieldValueString(totalrow, 0);
            }
            viewbyname = retObjQry.getFieldValueString(totalrow, 0);
        } else {
            if (isViewByAdd) {
                dispKpi.append("<Td rowspan=" + colspan + " class=\"viewbyname\" style=\"cursor:pointer;display:" + display + ";vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 0) + "</strong></Td>");
                if (ViewBy.size() > 1) {
                    dispKpi.append("<Td rowspan=" + totalelements + " class=\"viewbyname\" style=\"cursor:pointer;vertical-align:middle;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + ";\"><strong>" + retObjQry.getFieldValueString(totalrow, 1) + "</strong></Td>");

                }
                viewbyname = retObjQry.getFieldValueString(totalrow, 0);
            }
            dispKpi.append("<Td style=\"cursor:pointer\"><strong>" + ElementName + "</strong></Td>");
            viewbyname = retObjQry.getFieldValueString(totalrow, 0);
        }
        double[] celWidth = null;

        int i = 0;
        int flag = 0;
        for (i = 0; i < kpiElements.size(); i++) {

            //For Each element id try to value from retObjQry  and build a table row  here
            //added by sruthi for  gt
            if (kpiElements.get(i).getRefElementType().equalsIgnoreCase("1")) {
                String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + kpiElements.get(i).getElementId();
                try {
                    retobj = pbdb.execSelectSQL(qry);
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
                if (retobj != null && retobj.getRowCount() > 0) {
                    userColType = retobj.getFieldValueString(0, 2);
                }
            }
            //ended by sruthi
            if (retObjQry.getRowCount() > 0) {
                temp = "A_" + kpiElements.get(i).getElementId();
                String type = kpiElements.get(i).getRefElementType();
                if (kpiElements.size() > 1) {

                    if ((retObjQry.getFieldValueString(totalrow, temp)) != null && !("".equalsIgnoreCase(retObjQry.getFieldValueString(totalrow, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currVal = Double.parseDouble((retObjQry.getFieldValueString(totalrow, temp)));
                            targetid = "A_" + kpiElements.get(i).getElementId();
                        } else if (type.equalsIgnoreCase("2")) {
                            String priorValStr = retObjQry.getFieldValueString(totalrow, temp);
                            priorid = "A_" + kpiElements.get(i).getElementId();
                            if (priorValStr != null && !("".equals(priorValStr))) {
                                priorVal = Double.parseDouble(priorValStr);
                            }

                            double[] valArray = {currVal, priorVal};

                            celWidth = new double[valArray.length];
                            for (int j = 0; j < valArray.length; j++) {
                                sum += (valArray[j]);
                            }
                            for (int k = 0; k < valArray.length; k++) {
                                celWidth[k] = ((tableWidth * (valArray[k])) / sum);
                                celWidth[k] = Math.round(celWidth[k]);
                            }
                            kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                            if (totalrow == retObjQry.getRowCount() - 1) {
                                kpiBarChart1.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                            }
                            for (int kpi = 0; kpi < valArray.length; kpi++) {
                                if (kpiType.equalsIgnoreCase("Target")) {
                                    kpi++;
                                }
                                String value = retObjQry.getModifiedNumber(new BigDecimal(valArray[kpi]));
                                if (priorVal == 0.0) {
                                    flag = 1;
                                    if (kpi == 1) {
                                        if (kpi == valArray.length - 1) {
                                            arr.add("");
                                        }

                                        continue;
                                    }
//                                    else
//                                        
//                                        COLORS[kpi] = "";
                                }
                                if (celWidth[kpi] == 0.0) {
                                    COLORS[kpi] = "#FFFFFF";
                                }
                                if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget"))) {
                                    if (COLORS[kpi] == "") {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                            int index = atrelementIds.indexOf(ElementId);
                                            NumberFormatter nf = new NumberFormatter();
                                            int rounding;
                                            if (round.get(index) == "") {
                                                rounding = 0;
                                            } else {
                                                rounding = Integer.parseInt(round.get(index));
                                            }
                                            String formattedvalue = nf.getModifiedNumber(valArray[kpi], numberFormat.get(index), rounding);
                                            String currVal1 = nf.getModifiedNumber(currVal, numberFormat.get(index), rounding);
                                            currVal = Double.parseDouble(currVal1);
                                            attribute = numberFormat.get(index).toString();
                                            if (!(kpiType.equalsIgnoreCase("Target"))) {
                                                if (!attribute.equalsIgnoreCase("")) {
                                                    if (attribute.contains("M")) {
                                                        formattedvalue = formattedvalue.replace("M", "");
                                                    }
                                                    if (attribute.contains("K")) {
                                                        formattedvalue = formattedvalue.replace("K", "");
                                                    }
                                                    if (attribute.contains("Cr")) {
                                                        formattedvalue = formattedvalue.replace("Cr", "");
                                                    }
                                                    if (attribute.contains("L")) {
                                                        formattedvalue = formattedvalue.replace("L", "");
                                                    }
                                                }
                                            }
                                            StringBuffer sb = new StringBuffer();
                                            for (int k = 0; k < formattedvalue.length(); k++) {
//                                             if(formattedvalue.charAt(k)=='M' || formattedvalue.charAt(k)=='C' || formattedvalue.charAt(k)=='L' || formattedvalue.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                   if(selectrepids.contains(ElementId)){
                                                sb.append(formattedvalue.charAt(k));
//                                                   }
//                                             }else{
//                                             sb.append(formattedvalue.charAt(k));
//                                             }
                                            }
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                            arr.add(symbols.get(index) + sb.toString());
                                        } else {
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                            arr.add(value);
                                        }
                                    } else {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                            ArrayList<String> listdata = new ArrayList<String>();
                                            int index = atrelementIds.indexOf(ElementId);
                                            NumberFormatter nf = new NumberFormatter();
                                            int rounding;
                                            if (round.get(index) == "") {
                                                rounding = 0;
                                            } else {
                                                rounding = Integer.parseInt(round.get(index));
                                            }
                                            String formattedvalue = nf.getModifiedNumberDashboard(valArray[kpi], numberFormat.get(index), rounding);//changed by sruthi for indian rs
//                                       String currVal1=nf.getModifiedNumber(currVal,numberFormat.get(index) ,rounding);
//                                       currVal= Double.parseDouble(currVal1);
                                            if (dataMap.get(targetid) != null) {
                                                listdata = (ArrayList) dataMap.get(targetid);
                                            }
                                            listdata.add(formattedvalue);
                                            dataMap.put(targetid, listdata);
                                            attribute = numberFormat.get(index).toString();
                                            if (!(kpiType.equalsIgnoreCase("Target"))) {
                                                if (!attribute.equalsIgnoreCase("")) {
                                                    if (attribute.contains("M")) {
                                                        formattedvalue = formattedvalue.replace("M", "");
                                                    }
                                                    if (attribute.contains("K")) {
                                                        formattedvalue = formattedvalue.replace("K", "");
                                                    }
                                                    if (attribute.contains("Cr")) {
                                                        formattedvalue = formattedvalue.replace("Cr", "");
                                                    }
                                                    if (attribute.contains("L")) {
                                                        formattedvalue = formattedvalue.replace("L", "");
                                                    }
                                                }
                                            }

                                            StringBuffer sb = new StringBuffer();
                                            for (int k = 0; k < formattedvalue.length(); k++) {
                                                sb.append(formattedvalue.charAt(k));

                                            }
                                            if (COLORS[kpi] == "#FFFFFF") {
                                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                                    if (symbols.get(index) != null) {
                                                        if (symbols.get(index).equalsIgnoreCase("%")) {
                                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + symbols.get(index) + "</Td>");
                                                            arr.add(sb.toString() + symbols.get(index));
                                                        } else {
                                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                            arr.add(symbols.get(index) + sb.toString());
                                                        }
                                                    } else {
                                                        kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                        arr.add(sb.toString());
                                                    }
                                                } else {
                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                    arr.add(symbols.get(index) + sb.toString());
                                                }
                                            } else {
                                                if (symbols.get(index) != null) {
                                                    if (symbols.get(index).equalsIgnoreCase("%")) {
                                                        kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + symbols.get(index) + "</Td>");
                                                        arr.add(sb.toString() + symbols.get(index));
                                                        // }
                                                    } else {
                                                        kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                        // }
                                                        arr.add(symbols.get(index) + sb.toString());
                                                    }
                                                    //added by sruthi for grand total
                                                    if (totalrow == retObjQry.getRowCount() - 1) {
                                                        String columntype = "Ly";
                                                        String gtformattedvalue = getGrandTotalValue(temp, index, retObjQry, targetid, aggid, userColType, columntype);
                                                        StringBuffer gtsb = new StringBuffer();
                                                        for (int j = 0; j < gtformattedvalue.length(); j++) {
                                                            gtsb.append(gtformattedvalue.charAt(j));
                                                        }
                                                        //  }
                                                        if (symbols.get(index).equalsIgnoreCase("%")) {
                                                            kpiBarChart1.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + gtsb.toString() + symbols.get(index) + "</Td>");
                                                            grTotal.add(gtsb.toString() + symbols.get(index));
//}
                                                        } else {
                                                            kpiBarChart1.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + gtsb.toString() + "</Td>");
                                                            grTotal.add(symbols.get(index) + gtsb.toString());
                                                            // }
                                                        }
                                                    }
                                                    // }
                                                    //ended by sruhti
                                                } else {
                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + sb.toString() + "</Td>");
                                                    arr.add(sb.toString());
                                                    // }
                                                    //added by sruthi for grand total
                                                    if (totalrow == retObjQry.getRowCount() - 1) {
                                                        String columntype = "Ly";
                                                        String gtformattedvalue = getGrandTotalValue(temp, index, retObjQry, targetid, aggid, userColType, columntype);
                                                        StringBuffer gtsb = new StringBuffer();
                                                        for (int j = 0; j < gtformattedvalue.length(); j++) {
                                                            gtsb.append(gtformattedvalue.charAt(j));
                                                        }
                                                        kpiBarChart1.append("<Td style=\"width:" + celWidth[kpi] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + gtsb.toString() + "</Td>");
                                                        grTotal.add(gtsb.toString());
                                                        // }
                                                    }
                                                    //ended by sruhti
                                                }
                                            }
                                        } else if (COLORS[kpi] == "#FFFFFF") {
                                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                                arr.add(value);
                                            } else {
                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                                arr.add(value);
                                            }
                                        } else {
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                            arr.add(value);
                                        }
                                    }
                                }
                            }

                            kpiBarChart.append("</Tr></Table>");
                            //added by sruthi for grand total
                            if (totalrow == retObjQry.getRowCount() - 1) {
                                if (flag == 1) {
                                    int index = atrelementIds.indexOf(ElementId);
                                    ArrayList<String> list1 = new ArrayList<String>();
                                    NumberFormatter gtnf = new NumberFormatter();
                                    BigDecimal gtpriorVal = null;
                                    BigDecimal dividedval = null;
                                    StringBuffer gtsb = new StringBuffer();
                                    int gtrounding;
                                    if (round.get(index) == "") {
                                        gtrounding = 0;
                                    } else {
                                        gtrounding = Integer.parseInt(round.get(index));
                                    }
                                    int count = 0;
                                    gtpriorVal = retObjQry.getColumnGrandTotalValue(temp);
                                    priorval = gtpriorVal;
                                    String gtformattedvalue = gtnf.getModifiedNumberDashboard(gtpriorVal, numberFormat.get(index), gtrounding);//changed by sruthi for indian rs
                                    for (int j = 0; j < gtformattedvalue.length(); j++) {
                                        gtsb.append(gtformattedvalue.charAt(j));
                                    }
                                    if (symbols.get(index) != null) {
                                        if (symbols.get(index).equalsIgnoreCase("%")) {
                                            kpiBarChart1.append("<Td style=\"width:" + celWidth[flag] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[flag] + " ;padding:6px;valign=middle\" align=\"center\" >" + gtsb.toString() + symbols.get(index) + "</Td>");
                                            grTotal.add(gtsb.toString() + symbols.get(index));
                                        } else {
                                            kpiBarChart1.append("<Td style=\"width:" + celWidth[flag] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[flag] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + gtsb.toString() + "</Td>");
                                            grTotal.add(symbols.get(index) + gtsb.toString());
                                        }
                                    } else {
                                        kpiBarChart1.append("<Td style=\"width:" + celWidth[flag] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[flag] + " ;padding:6px;valign=middle\" align=\"center\" >" + gtsb.toString() + "</Td>");
                                        grTotal.add(gtsb.toString());
                                    }
                                }
                                kpiBarChart1.append("</Tr></Table>");
                            }
                            //ended by sruthi
                            if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Kpi"))) {
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                        if (flag1 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
                                        }

                                        //added by sruthi for grand total
                                        if (totalrow == retObjQry.getRowCount() - 1) {
                                            if (flag1 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart1.toString() + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart1.toString() + "</Td>");
                                            }

                                        }//ended by sruthi
                                    } else {
                                        if (flag1 == 1) {
                                            dispKpi.append("<Td align=\"center\"style=\"display:none;\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                                        }

                                    }
                                } else {
                                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                        if (flag1 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");
                                        }
                                        //added by sruthi for grand total
                                        if (totalrow == retObjQry.getRowCount() - 1) {
                                            if (flag1 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart1.toString() + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart1.toString() + "</Td>");
                                            }
                                        }//ended by sruthi
                                    } else {
                                        if (flag1 == 1) {
                                            dispKpi.append("<Td align=\"center\"style=\"display:none;\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                                        }

                                    }
                                }
                                // }
                            }
//
                        } else if (type.equalsIgnoreCase("3") && (!kpiType.equalsIgnoreCase("Target"))) {
                        } else if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Target") || kpiType.equalsIgnoreCase("Kpi"))) {
//
                        }
                    } else {
                        if (type.equalsIgnoreCase("2")) {
//                           if ((retObjQry.getFieldValueString(totalrow, temp)) == null ) {
                            double[] valArray = {currVal, priorVal};
                            celWidth = new double[valArray.length];
                            for (int j = 0; j < valArray.length; j++) {
                                sum += (valArray[j]);
                            }

                            for (int k = 0; k < valArray.length; k++) {
                                celWidth[k] = ((tableWidth * (valArray[k])) / sum);
                                celWidth[k] = Math.round(celWidth[k]);
                            }
                            kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                            for (int kpi = 0; kpi < valArray.length; kpi++) {
                                String value = retObjQry.getModifiedNumber(new BigDecimal(valArray[kpi]));
                                if (priorVal == 0.0) {
                                    if (kpi == 1) {
                                        continue;
                                    }
//                                    else
//                                        
//                                        COLORS[kpi] = "";
                                }
                                if (celWidth[kpi] == 0.0) {
                                    COLORS[kpi] = "#FFFFFF";
                                }
                                if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget"))) {
                                    if (COLORS[kpi] == "") {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                            int index = atrelementIds.indexOf(ElementId);
                                            NumberFormatter nf = new NumberFormatter();
                                            int rounding;
                                            if (round.get(index) == "") {
                                                rounding = 0;
                                            } else {
                                                rounding = Integer.parseInt(round.get(index));
                                            }
                                            String formattedvalue = nf.getModifiedNumber(valArray[kpi], numberFormat.get(index), rounding);

                                            attribute = numberFormat.get(index).toString();
                                            if (!attribute.equalsIgnoreCase("")) {
                                                if (attribute.contains("M")) {
                                                    formattedvalue = formattedvalue.replace("M", "");
                                                }
                                                if (attribute.contains("K")) {
                                                    formattedvalue = formattedvalue.replace("K", "");
                                                }
                                                if (attribute.contains("Cr")) {
                                                    formattedvalue = formattedvalue.replace("Cr", "");
                                                }
                                                if (attribute.contains("L")) {
                                                    formattedvalue = formattedvalue.replace("L", "");
                                                }
                                            }
                                            StringBuffer sb = new StringBuffer();
                                            for (int k = 0; k < formattedvalue.length(); k++) {
//                                             if(formattedvalue.charAt(k)=='M' || formattedvalue.charAt(k)=='C' || formattedvalue.charAt(k)=='L' || formattedvalue.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                   if(selectrepids.contains(ElementId)){
                                                sb.append(formattedvalue.charAt(k));
//                                                   }
//                                             }else{
//                                             sb.append(formattedvalue.charAt(k));
//                                             }
                                            }
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                            arr.add(symbols.get(index) + sb.toString());
                                        } else {
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                            arr.add(value);
                                        }
                                    } else {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                            int index = atrelementIds.indexOf(ElementId);
                                            NumberFormatter nf = new NumberFormatter();
                                            int rounding;
                                            if (round.get(index) == "") {
                                                rounding = 0;
                                            } else {
                                                rounding = Integer.parseInt(round.get(index));
                                            }
                                            String formattedvalue = nf.getModifiedNumberDashboard(valArray[kpi], numberFormat.get(index), rounding);//changed by sruthi for indian rs
                                            attribute = numberFormat.get(index).toString();
                                            if (!attribute.equalsIgnoreCase("")) {
                                                if (attribute.contains("M")) {
                                                    formattedvalue = formattedvalue.replace("M", "");
                                                }
                                                if (attribute.contains("K")) {
                                                    formattedvalue = formattedvalue.replace("K", "");
                                                }
                                                if (attribute.contains("Cr")) {
                                                    formattedvalue = formattedvalue.replace("Cr", "");
                                                }
                                                if (attribute.contains("L")) {
                                                    formattedvalue = formattedvalue.replace("L", "");
                                                }
                                            }

                                            StringBuffer sb = new StringBuffer();
                                            for (int k = 0; k < formattedvalue.length(); k++) {
                                                sb.append(formattedvalue.charAt(k));
                                            }
                                            if (COLORS[kpi] == "#FFFFFF") {
                                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                    arr.add(symbols.get(index) + sb.toString());
                                                } else {
                                                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                    arr.add(symbols.get(index) + sb.toString());
                                                }
                                            } else {
                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + sb.toString() + "</Td>");
                                                arr.add(symbols.get(index) + sb.toString());
                                            }
                                        } else if (COLORS[kpi] == "#FFFFFF") {
                                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:" + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "  ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                                arr.add(value);
                                            } else {
                                                kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                                arr.add(value);
                                            }
                                        } else {
                                            kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                                        }
                                        arr.add(value);
                                    }
                                }
                            }
                            kpiBarChart.append("</Tr></Table>");
                            if (!(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget") || kpiType.equalsIgnoreCase("Kpi"))) {
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");

                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");

                                    }
                                } else {
                                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + kpiBarChart.toString() + "</Td>");

                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");

                                    }
                                }
                            }
//                          }
                        }
                    }
                }
            }
        }

        if (kpiType.equalsIgnoreCase("Target") || kpiType.equalsIgnoreCase("BasicTarget")) {
            ArrayList timeDim = collect.timeDetailsArray;
            String timeLevel = timeDim.get(3).toString();
            Double targetValue = null;
            if (targetElementId != null && !targetElementId.equalsIgnoreCase("") && !targetElementId.equalsIgnoreCase("null")) {
                if (retObjQry.getFieldValueString(totalrow, "A_" + targetElementId) == null || retObjQry.getFieldValueString(totalrow, "A_" + targetElementId).equalsIgnoreCase("")
                        || retObjQry.getFieldValueString(totalrow, "A_" + targetElementId).equalsIgnoreCase(" ")) {
                    targetValue = Double.parseDouble("0");
                } else {
                    targetValue = Double.parseDouble((retObjQry.getFieldValueString(totalrow, "A_" + targetElementId)));
                }
                istarget = true;
            } else {
                targetValue = kpiDetail.getTargetValue(ElementId, timeLevel);
                //added by sruthi for manuval bud based on dates
                String nodays = dashletDetail.getNumberOfDays();
                String string1 = Integer.toString(days);
                if (nodays != null && !nodays.equalsIgnoreCase("") && !nodays.equalsIgnoreCase("null")) {
                    int latestdays = Integer.parseInt(nodays);
                    HashMap<String, Double> targetdata = dashletDetail.getTargetMauval();
                    Double targetval1 = null;
                    targetval1 = targetdata.get(ElementId);
                    if (targetval1 != null) {
                        if (string1.trim().equalsIgnoreCase(nodays)) {
                            targetValue = targetdata.get(ElementId);
                            Double actuvaltarget = null;
                            actuvaltarget = kpiDetail.getTargetValue(ElementId, timeLevel);
                            if (actuvaltarget != null) {
                                if (actuvaltarget < targetValue || actuvaltarget > targetValue) {
                                    targetValue = kpiDetail.getTargetValue(ElementId, timeLevel);
                                }
                            }
                        } else {
                            targetValue = (targetval1 / latestdays) * days;
                        }
                    }
                }
                int elementsize = ViewBy.size();
                if (ViewBy == ViewBy.get(elementsize - 1)) {
                    dashletDetail.setNumberOfDays(string1);
                }
                manuvaltargetval.put(ElementId, targetValue);
                //    List<KPITarget> kpiTargets = kpiDetail.getKPITargets(ElementId);
                if (ViewBy == ViewBy.get(elementsize - 1)) {
                    dashletDetail.setTargetMauval(manuvaltargetval);
                }
                //ended by sruthi
            }

            if (targetValue == null) {
                PbTimeRanges timeRanges = new PbTimeRanges();
                timeRanges.setElementID(ElementId);
                String currPeriodName = timeRanges.getCurrentPeriodName(timeDim.get(2).toString(), timeLevel);
                BigDecimal targetData = kpiDetail.getTargetData(ElementId, timeLevel, currPeriodName);
                if (targetData != null) {
                    targetValue = targetData.doubleValue();
                }

            }
            String basicTargetColor = "#000000";
            String basicDevVal = "--";

            String basicDevPer = "0.0";
            if (targetValue != null) {
                double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(currVal)), targetValue);
                basicDevVal = String.valueOf(DEVAL);
                // basicDevVal = NumberFormatter.getModifiedNumber(new BigDecimal(basicDevVal));
                //added by latha var-bud
                int index4 = atrelementIds.indexOf(ElementId);
                NumberFormatter nf2 = new NumberFormatter();
                int rounding4;
                if (round.get(index4) == "") {
                    rounding4 = 0;
                } else {
                    rounding4 = Integer.parseInt(round.get(index4));
                }
                BigDecimal bd = new BigDecimal(basicDevVal);
                basicDevVal = NumberFormatter.getModifiedNumberDashboard(bd, numberFormat.get(index4), rounding4);//changed by sruthi for indian rs
                //ended by latha
                //double DEVAL1 = Double.valueOf(basicDevVal);
                BigDecimal DEVPER = kpiDetail.getDeviationPer(currVal, targetValue);
                basicDevPer = String.valueOf(DEVPER);
                basicDevPer = NumberFormatter.getModifiedNumber(DEVPER) + "%";
                if (basicDevPer.contains("M")) {
                    basicDevPer = (DEVPER) + "%";
                }
                if (basicDevVal.equalsIgnoreCase("-0.0")) {
                    basicDevVal = "0.0";
                    basicDevPer = "0.0";
                }
                String basicKpiType = "Standard";
                String color = kpiDetail.getBasicKpiColor(DEVAL, ElementId);

                basicTargetDetails.put("color", color);

            }
            double perDev = 0;

            String timeDimVal = collect.timeDetailsArray.get(1).toString();
            String targetValueStr = "--";
            double tempDouble = 0.0;
            varLastyear = String.valueOf(currVal - priorVal);//code by Bhargavi
            if (targetValue != null) {
                ArrayList<String> list = new ArrayList<String>();
                if (targetValue != 0) {
                    devVal = String.valueOf(((currVal - targetValue) / targetValue) * 100);
                    varBudget = String.valueOf(currVal - targetValue);//code by Bhargavi

                } else {
                    devVal = String.valueOf((currVal) * 100);
                    varBudget = String.valueOf(currVal);//code by Bhargavi
                }
                perDev = new Double(devVal);
                int index4 = atrelementIds.indexOf(ElementId);
                NumberFormatter nf2 = new NumberFormatter();
                int rounding4;
                if (round.get(index4) == "") {
                    rounding4 = 0;
                } else {
                    rounding4 = Integer.parseInt(round.get(index4));
                }
                //devVal = NumberFormatter.getModifiedNumber(new BigDecimal(devVal)) + "%";  //comment by mohit
                devVal = NumberFormatter.getModifiedNumberDashboard(new BigDecimal(devVal), "", rounding4);
                //added by latha varBUD and var%
                // varBudget = NumberFormatter.getModifiedNumber(new BigDecimal(varBudget));
                int index1 = atrelementIds.indexOf(ElementId);
                NumberFormatter nf = new NumberFormatter();
                int rounding5;
                if (round.get(index1) == null ? "" == null : round.get(index1).equals("")) {
                    rounding5 = 0;
                } else {
                    rounding5 = Integer.parseInt(round.get(index1));
                }
                BigDecimal bd = new BigDecimal(varBudget);
                varBudget = NumberFormatter.getModifiedNumberDashboard(bd, numberFormat.get(index1), rounding5);//changed by sruthi for indian rs
                //ended by latha varBUD and var%
                String nVal = "";

                String currentStr = targetValue.toString();
                if (budgetlist.get(targetid) != null) {
                    list.add(currentStr);
                }
                list.add(currentStr);
                budgetlist.put(targetid, list);

                String pattern = "###,###";
                double value = Double.parseDouble(currentStr);
                DecimalFormat myFormatter = new DecimalFormat(pattern);
                String output = myFormatter.format(value);
                targetValueStr = output;

                if (targetValueStr.charAt(targetValueStr.length() - 1) == ',') {
                    targetValueStr = targetValueStr.substring(0, targetValueStr.length() - 1);
                }
            }
            //added by latha  var-ly rounding
            int index2 = atrelementIds.indexOf(ElementId);
            NumberFormatter nf1 = new NumberFormatter();
            int rounding1;
            if (round.get(index2) == "") {
                rounding1 = 0;
            } else {
                rounding1 = Integer.parseInt(round.get(index2));
            }
            BigDecimal bd = new BigDecimal(varLastyear);
            varLastyear = NumberFormatter.getModifiedNumberDashboard(bd, numberFormat.get(index2), rounding1);//changed by sruthi for indian rs

            //ended by latha var-ly rounding
            //varLastyear = NumberFormatter.getModifiedNumber(new BigDecimal(varLastyear));
            ArrayList<String> list = new ArrayList<String>();
            if (lastcurrlist.get(targetid) != null) {
                list = (ArrayList) lastcurrlist.get(targetid);
            }
            list.add(varLastyear);
            lastcurrlist.put(targetid, list);
            if (perDev < 0.0) {
                targetColor = "Red";
            } else if (perDev > 0.0) {
                targetColor = "Blue";
            }

            if ((kpiType.equalsIgnoreCase("BasicTarget"))) {
            } else {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                    int index = atrelementIds.indexOf(ElementId);
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String negativeValue = null;
                        if (targetValueStr.equalsIgnoreCase("--")) {
                            negativeValue = (String) targetValueStr;
                        } else {
                            BigDecimal db = new BigDecimal(targetValueStr.replaceAll(",", ""));
                            negativeValue = NumberFormatter.getModifiedNumberDashboard(db, numberFormat.get(index2), rounding1);//changed by sruthi for indian rs
                        }
                        if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                            negativeValue = negativeValue.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                            } else {
                            }
                        } //                                     kpiBarChart.append("<Td style=\"width:" + celWidth[1] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" +value  + "</Td>");
                        else {
                            if (istarget) {
                                if (!negativeValue.equalsIgnoreCase("--")) {
                                    if (symbols.get(index) != null) {
                                        if (symbols.get(index).equalsIgnoreCase("%")) {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\"style=\"display:none;\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + symbols.get(index) + "</Td>");
                                            } else {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + symbols.get(index) + "</Td>");
                                            }
                                            arr.add(negativeValue + symbols.get(index));
                                        } else {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td align=\"center\" style=\"display:none;\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + negativeValue + "</Td>");
                                            } else {
                                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + negativeValue + "</Td>");
                                            }
                                            arr.add(symbols.get(index) + negativeValue);
                                        }
                                    } else {
                                        if (flag2 == 1) {
                                            dispKpi.append("<Td align=\"center\"style=\"display:none;\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                        }
                                        arr.add(negativeValue);
                                    }
                                } else {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td align=\"center\"style=\"display:none;\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + negativeValue + "</Td>");
                                    }
                                    arr.add(negativeValue);
                                }
                                //added  by sruthi for grand total
                                if (totalrow == retObjQry.getRowCount() - 1) {
                                    if (targetElementId != null && !targetElementId.equalsIgnoreCase("") && !targetElementId.equalsIgnoreCase("null")) {
                                        NumberFormatter gtnf = new NumberFormatter();
                                        String columntype = "BUD";
                                        String gttargetvalue = getGrandTotalValue(targetElementId, index2, retObjQry, targetid, aggid, userColType, columntype);
                                        if (symbols.get(index) != null) {
                                            if (symbols.get(index).equalsIgnoreCase("%")) {
                                                if (flag2 == 1) {
                                                    DisplayGT.append("<Td align=\"center\"style=\"display:none;\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + symbols.get(index) + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + symbols.get(index) + "</Td>");
                                                }
                                                grTotal.add(gttargetvalue + symbols.get(index));
                                            } else {
                                                if (flag2 == 1) {
                                                    DisplayGT.append("<Td align=\"center\"style=\"display:none;\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + gttargetvalue + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + gttargetvalue + "</Td>");
                                                }
                                                grTotal.add(symbols.get(index) + gttargetvalue);
                                            }
                                        } else {
                                            if (flag2 == 1) {
                                                DisplayGT.append("<Td align=\"center\"style=\"display:none;\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + "</Td>");
                                            }
                                            grTotal.add(gttargetvalue);
                                        }
                                    } else {
                                        String targetvalue = "--";
                                        if (flag2 == 1) {
                                            DisplayGT.append("<Td align=\"center\"style=\"display:none;\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetvalue + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetvalue + "</Td>");
                                        }
                                        grTotal.add(targetvalue);
                                    }
                                }//ended by sruthi
                            } else {
                                if (!negativeValue.equalsIgnoreCase("--")) {
                                    if (symbols.get(index) != null) {
                                        if (symbols.get(index).equalsIgnoreCase("%")) {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td style=\"height:6px;display:none;font-size:10px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";font-family:VERDANA;color:#fff;background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\">" + negativeValue + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"height:6px;font-size:10px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";font-family:VERDANA;color:#fff;background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\">" + negativeValue + symbols.get(index) + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + symbols.get(index) + "</a></Td>");
                                            }
                                            arr.add(negativeValue + symbols.get(index));
                                        } else {
                                            if (flag2 == 1) {
                                                dispKpi.append("<Td style=\"height:6px;display:none;font-size:10px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";font-family:VERDANA;color:#fff;background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\">" + negativeValue + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"height:6px;font-size:10px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";font-family:VERDANA;color:#fff;background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\">" + symbols.get(index) + negativeValue + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + symbols.get(index) + negativeValue + "</a></Td>");
                                            }
                                            arr.add(symbols.get(index) + negativeValue);
                                        }
                                    } else {
                                        if (flag2 == 1) {
                                            dispKpi.append("<Td style=\"height:6px;display:none;font-size:10px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";font-family:VERDANA;color:#fff;background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\">" + negativeValue + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"height:6px;font-size:10px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";font-family:VERDANA;color:#fff;background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\">" + negativeValue + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                        }
                                        arr.add(negativeValue);
                                    }
                                } else {
                                    if (flag2 == 1) {
                                        dispKpi.append("<Td style=\"height:6px;display:none;font-size:10px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";font-family:VERDANA;color:#fff;background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\">" + negativeValue + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"height:6px;font-size:10px;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";font-family:VERDANA;color:#fff;background-color:" + background.get(index) + " ;padding:6px;valign=middle\" align=\"center\" ><a href=\"javascript:void(0)\">" + negativeValue + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                                    }
                                    arr.add(negativeValue);
                                }
                                if (totalrow == retObjQry.getRowCount() - 1) {
                                    if (targetElementId != null && !targetElementId.equalsIgnoreCase("") && !targetElementId.equalsIgnoreCase("null")) {
                                        NumberFormatter gtnf = new NumberFormatter();
                                        String columntype = "BUD";
                                        String gttargetvalue = getGrandTotalValue(targetElementId, index2, retObjQry, targetid, aggid, userColType, columntype);
                                        if (symbols.get(index) != null) {
                                            if (symbols.get(index).equalsIgnoreCase("%")) {
                                                if (flag2 == 1) {
                                                    DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + symbols.get(index) + "</Td>");
                                                }
                                                grTotal.add(gttargetvalue + symbols.get(index));
                                            } else {
                                                if (flag2 == 1) {
                                                    DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + symbols.get(index) + gttargetvalue + "</Td>");
                                                }
                                                grTotal.add(symbols.get(index) + gttargetvalue);
                                            }
                                        } else {
                                            if (flag2 == 1) {
                                                DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + gttargetvalue + "</Td>");
                                            }
                                            grTotal.add(gttargetvalue);
                                        }
                                    } else {
                                        String targetvalue = "--";
                                        if (flag2 == 1) {
                                            DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetvalue + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetvalue + "</Td>");
                                        }
                                        grTotal.add(targetvalue);
                                    }
                                }//ended by sruthi
                            }
                        }
                    } else {
                        if (istarget) {
                            if (flag2 == 1) {
                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetValueStr + "</Td>");
                            } else {
                                dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\" style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(index) + "\">" + targetValueStr + "</Td>");
                            }
                            arr.add(targetValueStr);
                        } else {
                            if (flag2 == 1) {
                                dispKpi.append("<Td style=\"cursor:pointer;display:none;color: " + font.get(index) + ";background-color:" + background.get(index) + "\"><a href=\"javascript:void(0)\">" + targetValueStr + "</a></Td>"); //onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\"><a href=\"javascript:void(0)\">" + targetValueStr + "</a></Td>");// onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                            }
                            arr.add(targetValueStr);
                        }
                    }
                } else {
                    if (istarget) {
                        if (flag2 == 1) {
                            dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\"  style=\"cursor:pointer;display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + targetValueStr + "</Td>");
                        } else {
                            dispKpi.append("<Td align=\"center\" padding=\"0px\" title=\"Data from Target Measure\"  style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + targetValueStr + "</Td>");
                        }
                        arr.add(targetValueStr);
                    } else {
                        if (flag2 == 1) {
                            dispKpi.append("<Td align=\"center\" style=\"display:none\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                        } else {
                            dispKpi.append("<Td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + ElementId + ":" + kpiMasterid + ":" + timeLevel + ":" + targetValueStr + ":" + collect.reportId + ":" + dashletId + "','" + kpiType + "')\">" + targetValueStr + "</a></Td>");
                        }
                        arr.add(targetValueStr);
                    }

                }
            }
            if (!(kpiType.equalsIgnoreCase("BasicTarget"))) {
                if (kpiType.equalsIgnoreCase("Target")) {
                    ArrayList<String> list1 = new ArrayList<String>();
                    kpiBarChart = new StringBuilder();
                    int index = atrelementIds.indexOf(ElementId);
                    NumberFormatter nf = new NumberFormatter();
                    int rounding;
                    if (round.get(index) == "") {
                        rounding = 0;
                    } else {
                        rounding = Integer.parseInt(round.get(index));
                    }
                    String value = nf.getModifiedNumberDashboard(currVal, numberFormat.get(index), rounding);//changed by sruthi for indian rs
                    if (currlist.get(targetid) != null) {
                        list1 = (ArrayList) currlist.get(targetid);
                    }
                    list1.add(value);
                    currlist.put(targetid, list1);
                    kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                    if (symbols.get(index) != null) {
                        if (symbols.get(index).equalsIgnoreCase("%")) {
//
                            kpiBarChart.append("<Td style=\"width:" + celWidth[1] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + symbols.get(index) + "</Td>");
                            arr.add(value + symbols.get(index));
                            //}
                        } else {
                            kpiBarChart.append("<Td style=\"width:" + celWidth[1] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + value + "</Td>");
                            arr.add(symbols.get(index) + value);
                            //}
                        }
                    } else {
                        kpiBarChart.append("<Td style=\"width:" + celWidth[1] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                        arr.add(value);
                        // }
                    }
                    kpiBarChart.append("</Tr></Table>");
                    //added by sruthi for grand total
                    if (totalrow == retObjQry.getRowCount() - 1) {
                        StringBuilder kpibarchart = new StringBuilder();
                        NumberFormatter gtnf = new NumberFormatter();
                        String columntype = "TY";
                        String gtformattedvalue = getGrandTotalValue(targetid, index, retObjQry, targetid, aggid, userColType, columntype);
                        kpibarchart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
                        if (symbols.get(index) != null) {
                            if (symbols.get(index).equalsIgnoreCase("%")) {
                                kpibarchart.append("<Td style=\"width:" + celWidth[1] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + gtformattedvalue + symbols.get(index) + "</Td>");
                                grTotal.add(gtformattedvalue + symbols.get(index));
                            } else {
                                kpibarchart.append("<Td style=\"width:" + celWidth[1] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + gtformattedvalue + "</Td>");
                                grTotal.add(symbols.get(index) + gtformattedvalue);
                            }
                        } else {
                            kpibarchart.append("<Td style=\"width:" + celWidth[1] + "px;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[0] + " ;padding:6px;valign=middle\" align=\"center\" >" + gtformattedvalue + "</Td>");
                            grTotal.add(gtformattedvalue);
                        }
                        kpibarchart.append("</Tr></Table>");
                        if (flag3 == 1) {
                            DisplayGT.append("<Td style=\"text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + kpibarchart.toString() + "</Td>");
                        } else {
                            DisplayGT.append("<Td style=\"text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + kpibarchart.toString() + "</Td>");
                        }
                    }//ended  by sruthi
                    if (flag3 == 1) {
                        dispKpi.append("<Td style=\"text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";display:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                    } else {
                        dispKpi.append("<Td style=\"text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                    }
                } else if (devVal.equalsIgnoreCase("--")) {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                        int index = atrelementIds.indexOf(ElementId);
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\">" + devVal + "</Td>");
                        arr.add(devVal);
                        // }
                    } else {
                        dispKpi.append("<Td align=\"center\">" + devVal + "</Td>");
                        arr.add(devVal);
                    }
                } else {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                        int index = atrelementIds.indexOf(ElementId);
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\">" + devVal + "</Td>");
                        arr.add(devVal);
                        //      }
                    } else {
                        dispKpi.append("<Td align=\"center\" style=\"color:" + targetColor + "\">" + devVal + "</Td>");
                        arr.add(devVal);
                    }
                }
            }
        }
        if (!kpiType.equalsIgnoreCase("Kpi")) {
            int index = atrelementIds.indexOf(ElementId);
            if (!SchedulerFlag) {
                if (!collect.isOneviewCheckForKpis()) {
                    if (kpiType.equalsIgnoreCase("Target")) {
                        if (symbols.get(index) != null) {
                            if (symbols.get(index).equalsIgnoreCase("%")) { //added by sruthi for negative color
                                if (negativevalues != null && !negativevalues.isEmpty() && !varBudget.equalsIgnoreCase("--")) {
                                    if (varBudget.contains("-")) {
                                        if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + symbols.get(index) + ")" + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + symbols.get(index) + ")" + "</Td>");
                                            }
                                            arr.add("(" + varBudget + symbols.get(index) + ")");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                            }
                                            arr.add(varBudget + symbols.get(index));
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + symbols.get(index) + ")" + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + symbols.get(index) + ")" + "</Td>");
                                            }
                                            arr.add("(" + varBudget + symbols.get(index) + ")");
                                        } else {
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                            }
                                            arr.add(varBudget + symbols.get(index));
                                        }
                                    } else {
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + symbols.get(index) + "</Td>");
                                        }
                                        arr.add(varBudget + symbols.get(index));
                                    }
                                } else //ended by sruthi
                                {
                                    if (flag4 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    }
                                    arr.add(varBudget);
                                }
                            } else {
                                if (negativevalues != null && !negativevalues.isEmpty() && !varBudget.equalsIgnoreCase("--")) {
                                    if (varBudget.contains("-")) {
                                        if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varBudget + ")" + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varBudget + ")" + "</Td>");
                                            }
                                            arr.add("(" + symbols.get(index) + varBudget + ")");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                            }
                                            arr.add(symbols.get(index) + varBudget);
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varBudget + ")" + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varBudget + ")" + "</Td>");
                                            }
                                            arr.add("(" + symbols.get(index) + varBudget + ")");
                                        } else {
                                            if (flag4 == 1) {
                                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                            }
                                            arr.add("(" + symbols.get(index) + varBudget + ")");
                                        }
                                    } else {
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varBudget + "</Td>");
                                        }
                                        arr.add(symbols.get(index) + varBudget);
                                    }
                                } else //ended by sruthi
                                {
                                    if (flag4 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    }
                                    arr.add(varBudget);
                                }
                            }
                        } else { //added by sruthi for negative color
                            if (negativevalues != null && !negativevalues.isEmpty() && !varBudget.equalsIgnoreCase("--")) {
                                if (varBudget.contains("-")) {
                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + ")" + "</Td>");
                                        }
                                        arr.add("(" + varBudget + ")");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                        }
                                        arr.add(varBudget);
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varBudget + ")" + "</Td>");
                                        }
                                        arr.add("(" + varBudget + ")");
                                    } else {
                                        if (flag4 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                        }
                                        arr.add(varBudget);
                                    }
                                } else //ended  by sruthi
                                {
                                    if (flag4 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                    }
                                    arr.add(varBudget);
                                }
                            } else {
                                if (flag4 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varBudget + "</Td>");
                                }
                                arr.add(varBudget);
                            }
                        }  //added by sruthi for grand total
                        if (totalrow == retObjQry.getRowCount() - 1) {
                            if (targetElementId != null && !targetElementId.equalsIgnoreCase("") && !targetElementId.equalsIgnoreCase("null")) {
                                NumberFormatter gtnf = new NumberFormatter();
                                String columntype = "Var-Bud";
                                String gtvarBudgevalue = getGrandTotalData(index, columntype);
                                if (symbols.get(index) != null) {
                                    if (symbols.get(index).equalsIgnoreCase("%")) {
                                        //added by sruthi for negative color
                                        if (negativevalues != null && !negativevalues.isEmpty() && !gtvarBudgevalue.equalsIgnoreCase("--")) {
                                            if (gtvarBudgevalue.contains("-")) {
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    if (flag4 == 1) {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + symbols.get(index) + ")" + "</Td>");
                                                    } else {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + symbols.get(index) + ")" + "</Td>");
                                                    }
                                                    grTotal.add("(" + gtvarBudgevalue + symbols.get(index) + ")");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag4 == 1) {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + symbols.get(index) + "</Td>");
                                                    } else {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + symbols.get(index) + "</Td>");
                                                    }
                                                    grTotal.add(gtvarBudgevalue + symbols.get(index));
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag4 == 1) {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + symbols.get(index) + ")" + "</Td>");
                                                    } else {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + symbols.get(index) + ")" + "</Td>");
                                                    }
                                                    grTotal.add("(" + gtvarBudgevalue + symbols.get(index) + ")");
                                                } else {
                                                    if (flag4 == 1) {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + symbols.get(index) + "</Td>");
                                                    } else {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + symbols.get(index) + "</Td>");
                                                    }
                                                    grTotal.add(gtvarBudgevalue + symbols.get(index));
                                                }
                                            } else {
                                                if (flag4 == 1) {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + symbols.get(index) + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + symbols.get(index) + "</Td>");
                                                }
                                                grTotal.add(gtvarBudgevalue + symbols.get(index));
                                            }
                                        } else //ended by sruthi
                                        {
                                            if (flag4 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                            }

                                            grTotal.add(gtvarBudgevalue);
                                        }
                                    } else {
                                        if (negativevalues != null && !negativevalues.isEmpty() && !gtvarBudgevalue.equalsIgnoreCase("--")) {
                                            if (gtvarBudgevalue.contains("-")) {
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    if (flag4 == 1) {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + gtvarBudgevalue + ")" + "</Td>");
                                                    } else {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + gtvarBudgevalue + ")" + "</Td>");
                                                    }
                                                    arr.add("(" + symbols.get(index) + gtvarBudgevalue + ")");
                                                    grTotal.add("(" + symbols.get(index) + gtvarBudgevalue + ")");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag4 == 1) {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtvarBudgevalue + "</Td>");
                                                    } else {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtvarBudgevalue + "</Td>");
                                                    }
                                                    grTotal.add(symbols.get(index) + gtvarBudgevalue);
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    if (flag4 == 1) {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + gtvarBudgevalue + ")" + "</Td>");
                                                    } else {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + gtvarBudgevalue + ")" + "</Td>");
                                                    }
                                                    grTotal.add("(" + symbols.get(index) + gtvarBudgevalue + ")");
                                                } else {
                                                    if (flag4 == 1) {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtvarBudgevalue + "</Td>");
                                                    } else {
                                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtvarBudgevalue + "</Td>");
                                                    }
                                                    grTotal.add(symbols.get(index) + gtvarBudgevalue);
                                                }
                                            } else {
                                                if (flag4 == 1) {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtvarBudgevalue + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtvarBudgevalue + "</Td>");
                                                }
                                                grTotal.add(symbols.get(index) + gtvarBudgevalue);
                                            }
                                        } else //ended by sruthi
                                        {
                                            if (flag4 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                            }
                                            grTotal.add(gtvarBudgevalue);
                                        }
                                    }
                                } else {
                                    //added by sruthi for negative color
                                    if (negativevalues != null && !negativevalues.isEmpty() && !gtvarBudgevalue.equalsIgnoreCase("--")) {
                                        if (gtvarBudgevalue.contains("-")) {
                                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                if (flag4 == 1) {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;dispaly:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + ")" + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + ")" + "</Td>");
                                                }
                                                grTotal.add("(" + gtvarBudgevalue + ")");
                                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                String negativecolor = "#f24040";
                                                if (flag4 == 1) {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                                }
                                                grTotal.add(gtvarBudgevalue);
                                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                String negativecolor = "#f24040";
                                                if (flag4 == 1) {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + ")" + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + ")" + "</Td>");
                                                }
                                                grTotal.add("(" + gtvarBudgevalue + ")");
                                            } else {
                                                if (flag4 == 1) {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                                } else {
                                                    DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                                }
                                                grTotal.add("(" + gtvarBudgevalue + ")");
                                            }
                                        } else {
                                            if (flag4 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                            }
                                            grTotal.add(gtvarBudgevalue);
                                        }
                                    } else {//ended by sruthi
                                        if (flag4 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                        }
                                        grTotal.add(gtvarBudgevalue);
                                    }

                                }
                            } else {
                                String gtvarBudgevalue = "--";
                                if (flag4 == 1) {
                                    DisplayGT.append("<Td style=\"cursor:pointer;display:none;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                } else {
                                    DisplayGT.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                }
                                grTotal.add(gtvarBudgevalue);
                            }
                        }//ended by sruthi
                    } else {
                        if (kpiDetail.isShowInsights() || fromDesigner) {
                            if (PrivilegeManager.isModuleEnabledForUser("INSIGHT", Integer.parseInt(userId))) {
                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                                    dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a onclick=\"gotoInsight('" + ElementId + "'," + kpiMasterid + ");\"><span class=\"ui-icon ui-icon-search\"></span></a></td>");

                                } else {
                                    dispKpi.append("<td align=\"center\"><a onclick=\"gotoInsight('" + ElementId + "'," + kpiMasterid + ");\"><span class=\"ui-icon ui-icon-search\"></span></a></td>");
                                }
                            }
                        }
                    }
                }
                List<KPIComment> kpiComments = kpiDetail.getKPIComments(ElementId);
                if (!collect.isOneviewCheckForKpis()) {
                    if (kpiType.equalsIgnoreCase("Target")) {
                        //added by sruthi for negative color
                        if (negativevalues != null && !negativevalues.isEmpty() && !devVal.equalsIgnoreCase("--")) {
                            if (devVal.contains("-")) {
                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                    if (flag5 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + devVal + "%" + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + devVal + "%" + ")" + "</Td>");
                                    }
                                    arr.add("(" + devVal + "%" + ")");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag5 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                    }
                                    arr.add(devVal + "%");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag5 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + devVal + "%" + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + devVal + "%" + ")" + "</Td>");
                                    }
                                    arr.add("(" + devVal + "%" + ")");
                                } else {
                                    if (flag5 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                    }
                                    arr.add(devVal + "%");
                                }
                            } else {
                                if (flag5 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "%" + "</Td>");
                                }
                                arr.add(devVal + "%");
                            }
                        } else //ended by sruthi
                        {
                            if (flag5 == 1) {
                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "</Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + devVal + "</Td>");
                            }
                            arr.add(devVal);
                        }
                        //added by sruthi for grand total
                        if (totalrow == retObjQry.getRowCount() - 1) {
                            if (targetElementId != null && !targetElementId.equalsIgnoreCase("") && !targetElementId.equalsIgnoreCase("null")) {
                                NumberFormatter gtnf = new NumberFormatter();
                                String columntype = "Var-Bud%";
                                String gtvarBudgevalue = getGrandTotalData(index, columntype);
                                if (negativevalues != null && !negativevalues.isEmpty() && !devVal.equalsIgnoreCase("--")) {
                                    if (devVal.contains("-")) {
                                        if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                            if (flag5 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + "%" + ")" + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + "%" + ")" + "</Td>");
                                            }
                                            grTotal.add("(" + gtvarBudgevalue + "%" + ")");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag5 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "%" + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "%" + "</Td>");
                                            }
                                            grTotal.add(gtvarBudgevalue + "%");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag5 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + "%" + ")" + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtvarBudgevalue + "%" + ")" + "</Td>");
                                            }
                                            grTotal.add("(" + gtvarBudgevalue + "%" + ")");
                                        } else {
                                            if (flag5 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "%" + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "%" + "</Td>");
                                            }
                                            grTotal.add(gtvarBudgevalue + "%");
                                        }
                                    } else {
                                        if (flag5 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "%" + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "%" + "</Td>");
                                        }
                                        grTotal.add(gtvarBudgevalue + "%");
                                    }
                                } else //ended by sruthi
                                {
                                    if (flag5 == 1) {
                                        DisplayGT.append("<Td style=\"cursor:pointer;disply:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                    } else {
                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                    }
                                    grTotal.add(gtvarBudgevalue);
                                }
                            } else {
                                String gtvarBudgevalue = "--";
                                if (flag5 == 1) {
                                    DisplayGT.append("<Td style=\"cursor:pointer;display:none;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                } else {
                                    DisplayGT.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtvarBudgevalue + "</Td>");
                                }
                                grTotal.add(gtvarBudgevalue);
                            }
                        }//ended by sruthi
                    } else {
                    }
                }
                if ((!kpiType.equalsIgnoreCase("Standard")) && !(kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget"))) {
                    if (!collect.isOneviewCheckForKpis()) {
                    }
                }
                if (kpiType.equalsIgnoreCase("Target")) {
                    if (symbols.get(index) != null) {
                        //added by sruthi for negative color
                        if (symbols.get(index).equalsIgnoreCase("%")) {
                            if (negativevalues != null && !negativevalues.isEmpty() && !varLastyear.equalsIgnoreCase("--")) {
                                if (varLastyear.contains("-")) {
                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + symbols.get(index) + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + symbols.get(index) + ")" + "</Td>");
                                        }
                                        arr.add("(" + varLastyear + symbols.get(index) + ")");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                        }
                                        arr.add(varLastyear + symbols.get(index));
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + symbols.get(index) + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + symbols.get(index) + ")" + "</Td>");
                                        }
                                        arr.add("(" + varLastyear + symbols.get(index) + ")");
                                    } else {
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                        }
                                        arr.add(varLastyear + symbols.get(index));
                                    }
                                } else {
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + symbols.get(index) + "</Td>");
                                    }
                                    arr.add(varLastyear + symbols.get(index));
                                }
                            } else //ended by sruthi
                            {
                                if (flag6 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                }
                                arr.add(varLastyear);
                            }
                        } else {
                            if (negativevalues != null && !negativevalues.isEmpty() && !varLastyear.equalsIgnoreCase("--")) {
                                if (varLastyear.contains("-")) {
                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varLastyear + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varLastyear + ")" + "</Td>");
                                        }
                                        arr.add("(" + symbols.get(index) + varLastyear + ")");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                        }
                                        arr.add(symbols.get(index) + varLastyear);
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varLastyear + ")" + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + varLastyear + ")" + "</Td>");
                                        }
                                        arr.add("(" + symbols.get(index) + varLastyear + ")");
                                    } else {
                                        if (flag6 == 1) {
                                            dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                        }
                                        arr.add(symbols.get(index) + varLastyear);
                                    }
                                } else {
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + varLastyear + "</Td>");
                                    }
                                    arr.add(symbols.get(index) + varLastyear);
                                }
                            } else //ended by sruthi
                            {
                                if (flag6 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                }
                                arr.add(varLastyear);
                            }
                        }
                    } else {
                        //added by sruthi for negative color
                        if (negativevalues != null && !negativevalues.isEmpty()) {
                            if (varLastyear.contains("-")) {
                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + ")" + "</Td>");
                                    }
                                    arr.add("(" + varLastyear + ")");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                    }
                                    arr.add(varLastyear);
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + varLastyear + ")" + "</Td>");
                                    }
                                    arr.add("(" + varLastyear + ")");
                                } else {
                                    if (flag6 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                    }
                                    arr.add(varLastyear);
                                }
                            } else {
                                if (flag6 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                                }
                                arr.add(varLastyear);
                            }
                        } else //ended by sruthi
                        {
                            if (flag6 == 1) {
                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + varLastyear + "</Td>");
                            }
                            arr.add(varLastyear);
                        }
                    }//added by sruhti for grand total
                    if (totalrow == retObjQry.getRowCount() - 1) {
                        int index1 = atrelementIds.indexOf(targetid.replace("A_", ""));
                        String columntype = "Var-Ly";
                        String gtformatvarLastyear = getGrandTotalData(index1, columntype);
                        if (symbols.get(index) != null) {
                            if (symbols.get(index).equalsIgnoreCase("%")) {
                                //added by sruthi for negative color
                                if (negativevalues != null && !negativevalues.isEmpty() && !gtformatvarLastyear.equalsIgnoreCase("--")) {
                                    if (gtformatvarLastyear.contains("-")) {
                                        if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                            if (flag6 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatvarLastyear + symbols.get(index) + ")" + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatvarLastyear + symbols.get(index) + ")" + "</Td>");
                                            }
                                            grTotal.add("(" + gtformatvarLastyear + symbols.get(index) + ")");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag6 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + symbols.get(index) + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + symbols.get(index) + "</Td>");
                                            }
                                            grTotal.add(gtformatvarLastyear + symbols.get(index));
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag6 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatvarLastyear + symbols.get(index) + ")" + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatvarLastyear + symbols.get(index) + ")" + "</Td>");
                                            }
                                            grTotal.add("(" + gtformatvarLastyear + symbols.get(index) + ")");
                                        } else {
                                            if (flag6 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + symbols.get(index) + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + symbols.get(index) + "</Td>");
                                            }
                                            grTotal.add(gtformatvarLastyear + symbols.get(index));
                                        }
                                    } else {
                                        if (flag6 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + symbols.get(index) + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + symbols.get(index) + "</Td>");
                                        }
                                        grTotal.add(gtformatvarLastyear + symbols.get(index));
                                    }
                                } else //ended by sruthi
                                {
                                    if (flag6 == 1) {
                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                    } else {
                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                    }
                                    grTotal.add(gtformatvarLastyear + symbols.get(index));
                                }
                            } else {
                                if (negativevalues != null && !negativevalues.isEmpty() && !gtformatvarLastyear.equalsIgnoreCase("--")) {
                                    if (gtformatvarLastyear.contains("-")) {
                                        if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                            if (flag6 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + gtformatvarLastyear + ")" + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + gtformatvarLastyear + ")" + "</Td>");
                                            }
                                            grTotal.add("(" + symbols.get(index) + gtformatvarLastyear + ")");
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag6 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtformatvarLastyear + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtformatvarLastyear + "</Td>");
                                            }
                                            grTotal.add(symbols.get(index) + gtformatvarLastyear);
                                        } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                            String negativecolor = "#f24040";
                                            if (flag6 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + gtformatvarLastyear + ")" + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + symbols.get(index) + gtformatvarLastyear + ")" + "</Td>");
                                            }
                                            grTotal.add("(" + symbols.get(index) + gtformatvarLastyear + ")");
                                        } else {
                                            if (flag6 == 1) {
                                                DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtformatvarLastyear + "</Td>");
                                            } else {
                                                DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtformatvarLastyear + "</Td>");
                                            }
                                            grTotal.add(symbols.get(index) + gtformatvarLastyear);
                                        }
                                    } else {
                                        if (flag6 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtformatvarLastyear + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + symbols.get(index) + gtformatvarLastyear + "</Td>");
                                        }
                                        grTotal.add(symbols.get(index) + gtformatvarLastyear);
                                    }
                                } else //ended by sruthi
                                {
                                    if (flag6 == 1) {
                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                    } else {
                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                    }
                                    grTotal.add(gtformatvarLastyear);
                                }
                            }
                        } else {
                            //added by sruthi for negative color
                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                if (gtformatvarLastyear.contains("-")) {
                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
//                                                 if(flag6==1)
//                                              DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:"+alignment.get(atrelementIds.indexOf(ElementId))+";background-color:"+background.get(atrelementIds.indexOf(ElementId))+"\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: "+font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" +"("+ gtformatvarLastyear+")" + "</Td>");
//                                                       else
                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatvarLastyear + ")" + "</Td>");
                                        grTotal.add("(" + gtformatvarLastyear + ")");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                        }
                                        grTotal.add(gtformatvarLastyear);
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag6 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatvarLastyear + ")" + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatvarLastyear + ")" + "</Td>");
                                        }
                                        grTotal.add("(" + gtformatvarLastyear + ")");
                                    } else {
                                        if (flag6 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                        }
                                        grTotal.add(gtformatvarLastyear);
                                    }
                                } else {
                                    if (flag6 == 1) {
                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                    } else {
                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                    }
                                    grTotal.add(gtformatvarLastyear);
                                }
                            } else //ended by sruthi
                            {
                                if (flag6 == 1) {
                                    DisplayGT.append("<Td style=\"cursor:pointerdisplay:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                } else {
                                    DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatvarLastyear + "</Td>");
                                }
                                grTotal.add(gtformatvarLastyear);
                            }
                        }
                    }//ended by sruthi
                }
                // dispKpi.append("<Td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"javascript:dashboardKpiAlerts('"+ElementId+"',"+kpiMasterid+",'" + dashletId + "');\" style=\"text-decoration:none;cursor:pointer;\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
                if (!collect.isOneviewCheckForKpis()) {
                    Double targetValue = (Double) basicTargetDetails.get("targetValue");
                    String deviationPercent = (String) basicTargetDetails.get("deviationPercent");
                    if (kpiType.equalsIgnoreCase("Target")) {
                        BigDecimal changePercVal1 = (retObjQry.getFieldValueBigDecimal(totalrow, temp));
                        //added by latha var%
                        int index3 = atrelementIds.indexOf(targetid.replace("A_", ""));
                        //int index3=atrelementIds.indexOf(ElementId);
                        NumberFormatter nf2 = new NumberFormatter();
                        int rounding2;
                        if (round.get(index3) == "") {
                            rounding2 = 0;
                        } else {
                            rounding2 = Integer.parseInt(round.get(index3));
                        }
                        String changePercVal = NumberFormatter.getModifiedNumberDashboard(changePercVal1, "", rounding2);
                        //ended by latha
                        ArrayList<String> list4 = new ArrayList<String>();
                        //  changePercVal=changePercVal.setScale(2, RoundingMode.CEILING);
                        if (lastcurrlistper.get(targetid) != null) {
                            list4 = (ArrayList) lastcurrlistper.get(targetid);
                        }
                        list4.add(changePercVal.toString());
                        lastcurrlistper.put(targetid, list4);
                        //added by sruthi for negative color
                        if (negativevalues != null && !negativevalues.isEmpty() && !changePercVal.equalsIgnoreCase("--")) {
                            if (changePercVal.contains("-")) {
                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                    if (flag7 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + changePercVal + "%" + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + changePercVal + "%" + ")" + "</Td>");
                                    }
                                    arr.add("(" + changePercVal + "%" + ")");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag7 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + changePercVal + "%" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + changePercVal + "%" + "</Td>");
                                    }
                                    arr.add(changePercVal + "%");
                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                    String negativecolor = "#f24040";
                                    if (flag7 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + changePercVal + "%" + ")" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + changePercVal + "%" + ")" + "</Td>");
                                    }
                                    arr.add("(" + changePercVal + "%" + ")");
                                } else {
                                    if (flag7 == 1) {
                                        dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercVal + "%" + "</Td>");
                                    } else {
                                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercVal + "%" + "</Td>");
                                    }
                                    arr.add(changePercVal + "%");
                                }
                            } else {
                                if (flag7 == 1) {
                                    dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercVal + "%" + "</Td>");
                                } else {
                                    dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercVal + "%" + "</Td>");
                                }
                                arr.add(changePercVal + "%");
                            }
                        } else //ended by sruthi
                        {
                            if (flag7 == 1) {
                                dispKpi.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercVal + "</Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + changePercVal + "</Td>");
                            }
                            arr.add(changePercVal);
                        }
                        //aded by sruthi for grand total
                        if (totalrow == retObjQry.getRowCount() - 1) {
                            int index1 = atrelementIds.indexOf(targetid.replace("A_", ""));
                            String columntype = "Var-Ly%";
                            String gtformatchangePercVal = getGrandTotalData(index1, columntype);
                            if (negativevalues != null && !negativevalues.isEmpty() && !gtformatchangePercVal.equalsIgnoreCase("--")) {
                                if (gtformatchangePercVal.contains("-")) {
                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                        if (flag7 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatchangePercVal + "%" + ")" + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatchangePercVal + "%" + ")" + "</Td>");
                                        }
                                        grTotal.add("(" + gtformatchangePercVal + "%" + ")");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag7 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtformatchangePercVal + "%" + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + gtformatchangePercVal + "%" + "</Td>");
                                        }
                                        grTotal.add(gtformatchangePercVal + "%");
                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                        String negativecolor = "#f24040";
                                        if (flag7 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatchangePercVal + "%" + ")" + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + negativecolor + ";cursor:pointer;\"  title=\" \">" + "(" + gtformatchangePercVal + "%" + ")" + "</Td>");
                                        }
                                        grTotal.add("(" + gtformatchangePercVal + "%" + ")");
                                    } else {
                                        if (flag7 == 1) {
                                            DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatchangePercVal + "%" + "</Td>");
                                        } else {
                                            DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatchangePercVal + "%" + "</Td>");
                                        }
                                        grTotal.add(gtformatchangePercVal + "%");
                                    }
                                } else {
                                    if (flag7 == 1) {
                                        DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatchangePercVal + "%" + "</Td>");
                                    } else {
                                        DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatchangePercVal + "%" + "</Td>");
                                    }
                                    grTotal.add(gtformatchangePercVal + "%");
                                }
                            } else //ended by sruthi
                            {
                                if (flag7 == 1) {
                                    DisplayGT.append("<Td style=\"cursor:pointer;display:none;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatchangePercVal + "</Td>");
                                } else {
                                    DisplayGT.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";cursor:pointer;\"  title=\" \">" + gtformatchangePercVal + "</Td>");
                                }
                                grTotal.add(gtformatchangePercVal);
                            }

                        }//ended by sruthi
                    }
                    if (kpiType.equalsIgnoreCase("Target")) {
                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && !background.isEmpty() && background != null) {
                            // dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a onclick= \"viewSchedulerAndTracker('" + new Double(oneDForm.format(currVal)) + "','" + ElementId + "','" + kpiMasterid + "','" + dashletId + "','" + collect.reportId + "','" + targetValue + "','" + deviationPercent + "','" + kpiType + "','" + ElementName + "')\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
                            dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a onclick= \"viewAlertCondition('" + new Double(oneDForm.format(currVal)) + "','" + ElementId + "','" + kpiMasterid + "','" + dashletId + "','" + collect.reportId + "','" + targetValue + "','" + deviationPercent + "','" + kpiType + "','" + ElementName + "','" + container.getCustomKPIHeaderNames() + "','" + isViewby + "','" + isViewbySecond + "')\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
                            dispKpi.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\" >");
                            dispKpi.append("<img width='15' height='15' style='float:middle;cursor:pointer;' onclick=\"dashgenerateQuickTrend('" + ReportId + "','" + dashletId + "','" + ElementId + "','" + ElementName + "','" + targetElementId + "','" + targetElemName + "','" + priorid + "','" + viewbyvalues + "','" + ViewBy + "')\" 'title=\" \" src='images/chart1.png'/></td>");
                            //added by sruthi for grand total
                            if (totalrow == retObjQry.getRowCount() - 1) {

                                DisplayGT.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a onclick= \"viewAlertCondition('" + new Double(oneDForm.format(currVal)) + "','" + ElementId + "','" + kpiMasterid + "','" + dashletId + "','" + collect.reportId + "','" + targetValue + "','" + deviationPercent + "','" + kpiType + "','" + ElementName + "','" + container.getCustomKPIHeaderNames() + "','" + "Grand Total" + "','" + "Grand Total" + "')\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
                                DisplayGT.append("<Td style=\"cursor:pointer;background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" style=\"text-decoration:none;cursor:pointer;\" >");
                                DisplayGT.append("<img width='15' height='15' style='float:middle;cursor:pointer;' onclick=\"dashgenerateQuickTrend('" + ReportId + "','" + dashletId + "','" + ElementId + "','" + ElementName + "','" + targetElementId + "','" + targetElemName + "','" + priorid + "','" + "" + "','" + "" + "')\" 'title=\" \" src='images/chart1.png'/></td>");
                            }//ended by sruthi
                        }
                    }
                    dispKpi.append("</Tr>");

                }
            }
        }
        if (totalrow == retObjQry.getRowCount() - 1 && m == totalelements - 1) {
            dispKpi.append(DisplayGT);
            dispKpi.append("</Tr>");
        }
        if (ViewBy.size() > 1) {
            display = "none";
        }
        calculativeVal.add(arr);
        if (!grTotal.isEmpty()) {
            gtVal.add(grTotal);
        }
        return dispKpi.toString();
    }

    public String getbusinessroles(String dashBoardId) {
        String folderdetailsqquery = " select distinct FOLDER_ID from PRG_AR_REPORT_DETAILS  where REPORT_ID =" + dashBoardId;
        StringBuffer foldernames = new StringBuffer();
        String folderDetails = "";
        PbDb reportDetail = new PbDb();
        ///ArrayList arlist111 = new ArrayList();

        PbReturnObject retObj = new PbReturnObject();

        try {
            retObj = reportDetail.execSelectSQL(folderdetailsqquery);
            for (int j = 0; j < retObj.getRowCount(); j++) {
                foldernames = foldernames.append(retObj.getFieldValueString(j, 0));

            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        folderDetails = foldernames.toString();

        return folderDetails;

    }

    public String buildPartialBasicTargetKpi(HashMap basicTargetDetails) {
        StringBuilder dispKpi = new StringBuilder();
        String kpiType = "BasicTarget";
        ArrayList arrayList = (ArrayList) basicTargetDetails.get("targetDetails");
        if (basicTargetDetails != null) {
            if (!atrelementIds.isEmpty() && atrelementIds.contains(arrayList.get(0)) && background != null && !background.isEmpty()) {
                int index = atrelementIds.indexOf(arrayList.get(0));
                if (!SchedulerFlag) {
                    // to check the negative value and display in red or bracket or red&&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String negativeValue = (String) symbols.get(index) + basicTargetDetails.get("currVal");
                        if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                            negativeValue = negativeValue.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket")) {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(arrayList.get(0))) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + symbols.get(index) + basicTargetDetails.get("currVal") + "</a></Td>");
                            }
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                        }
                    } //
                    else {
                        dispKpi.append("<Td style='background-color:" + basicTargetDetails.get("color") + "' align=\"center\" padding=\"0px\">" + symbols.get(index) + basicTargetDetails.get("currVal") + "</Td>");//value
                    }
                } else {
                    dispKpi.append("<Td align=\"center\" style=\"color: " + basicTargetDetails.get("color") + "\" padding=\"0px\">" + symbols.get(index) + basicTargetDetails.get("currVal") + "</Td>");//value
                }
            } else {
                if (!SchedulerFlag) {
                    dispKpi.append("<Td style='background-color:" + basicTargetDetails.get("color") + "' align=\"center\" padding=\"0px\">" + basicTargetDetails.get("currentValue") + "</Td>");//value
                } else {
                    dispKpi.append("<Td style=\"color: " + basicTargetDetails.get("color") + "\" align=\"center\" padding=\"0px\">" + basicTargetDetails.get("currentValue") + "</Td>");//value
                }
            }
            if (arrayList.get(3) == null) {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(arrayList.get(0)) && background != null && !background.isEmpty()) {
                    int index = atrelementIds.indexOf(arrayList.get(0));
                    // to check the negative value and display in red or bracket or red&&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String negativeValue = (String) arrayList.get(6);
                        if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                            negativeValue = negativeValue.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket")) {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(arrayList.get(0))) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + arrayList.get(3) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + arrayList.get(3) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + arrayList.get(3) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:black align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + arrayList.get(3) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + arrayList.get(6) + "</a></Td>");
                            }
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + arrayList.get(3) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + negativeValue + "</Td>");
                        }
                    } //
                    else {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + arrayList.get(3) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + arrayList.get(6) + "</a></Td>");
                    }
                } else {
                    dispKpi.append("<Td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + arrayList.get(3) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + arrayList.get(6) + "</a></Td>");
                }
            } else {
                Double value = (Double) arrayList.get(3);
                BigDecimal bd = new BigDecimal(value);
                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                if (!atrelementIds.isEmpty() && atrelementIds.contains(arrayList.get(0)) && background != null && !background.isEmpty()) {
                    int index = atrelementIds.indexOf(arrayList.get(0));
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String negativeValue = (String) formatter.format(bd);
                        if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                            negativeValue = negativeValue.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket")) {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(arrayList.get(0))) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + formatter.format(bd) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + formatter.format(bd) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + negativeValue + "</a></Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + formatter.format(bd) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + formatter.format(bd) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + formatter.format(bd) + "</a></Td>");
                            }
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + formatter.format(bd) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + negativeValue + "</Td>");
                        }
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + formatter.format(bd) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\"><font color=\"" + font.get(index) + "\">" + formatter.format(bd) + "</font></a></Td>");
                    }
                } else {
                    dispKpi.append("<Td align=\"center\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + arrayList.get(0) + ":" + arrayList.get(1) + ":" + arrayList.get(2) + ":" + formatter.format(bd) + ":" + arrayList.get(4) + ":" + arrayList.get(5) + "','" + kpiType + "')\">" + formatter.format(bd) + "</a></Td>");
                }
            }
            if (!atrelementIds.isEmpty() && atrelementIds.contains(arrayList.get(0)) && background != null && !background.isEmpty()) {
                int index = atrelementIds.indexOf(arrayList.get(0));
                if (negativevalues != null && !negativevalues.isEmpty()) {
                    String negativeValue = (String) basicTargetDetails.get("deviationvalue");
                    if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                        negativeValue = negativeValue.replace("-", " ");
                        if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket")) {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(arrayList.get(0))) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                        } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Red")) {
                            String negativecolor = "#f24040";
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                        } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket&Red")) {
                            String negativecolor = "#f24040";
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + basicTargetDetails.get("deviationvalue") + "</Td>");
                        }
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                    }
                } else {
                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\">" + basicTargetDetails.get("deviationvalue") + "</Td>");
                }
            } else {
                dispKpi.append("<Td align=\"center\" style='\"color:" + basicTargetDetails.get("basicTargetColor") + "'\">" + basicTargetDetails.get("deviationvalue") + "</Td>");
            }

            if (!atrelementIds.isEmpty() && atrelementIds.contains(arrayList.get(0)) && background != null && !background.isEmpty()) {
                int index = atrelementIds.indexOf(arrayList.get(0));
                if (negativevalues != null && !negativevalues.isEmpty()) {
                    String negativeValue = (String) basicTargetDetails.get("deviationPercent");
                    if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                        negativeValue = negativeValue.replace("-", " ");
                        if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket")) {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(arrayList.get(0))) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                        } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Red")) {
                            String negativecolor = "#f24040";
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                        } else if (negativevalues.get(atrelementIds.indexOf(arrayList.get(0))).equalsIgnoreCase("Bracket&Red")) {
                            String negativecolor = "#f24040";
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + basicTargetDetails.get("deviationPercent") + "</Td>");
                        }
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                    }
                } else {
                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(index) + ";background-color:" + background.get(index) + "\">" + basicTargetDetails.get("deviationPercent") + "</Td>");
                }
            } else {
                dispKpi.append("<Td align=\"center\" style=\"color: " + basicTargetDetails.get("color") + "\">" + basicTargetDetails.get("deviationPercent") + "</Td>");
            }
        }

//  basicTargetDetails.put("deviationvalue", basicDevVal);
//                basicTargetDetails.put("deviationPercent", basicDevPer);
//
// dispKpi.append("<Td align=\"center\" style=\"color:\">" + basicDevVal + "</Td>");
//             dispKpi.append("<Td align=\"center\" style=\"color: \">" + basicDevPer + "</Td>");
        return dispKpi.toString();
    }

    public String buildPartialKpi(HashMap simpleKpi) {
        StringBuilder dispKpi = new StringBuilder();
        if (simpleKpi != null) {
            dispKpi.append("<Td align=\"center\">" + simpleKpi.get("currentValue") + "</Td>");
            dispKpi.append("<Td align=\"center\">" + simpleKpi.get("priorVal") + "</Td>");
        }
        return dispKpi.toString();
    }

    public String displayMultiPeriodKpi(Container container, String ElementId, String ElementName, String ReportId, String kpiMasterid,
            String kpiDrill, String kpiType, PbReturnObject pbretObjForTime, DashletDetail dashletDetail, boolean fromDesigner,
            pbDashboardCollection collect, String userId) throws Exception {
        String attribute = "";
        if (container.getMultiPeriodKPI() != null) {
            KPI kpiDetail = (KPI) dashletDetail.getReportDetails();
            String dashletId = dashletDetail.getDashBoardDetailId();
            int index = 0;
            List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);
            NumberFormatter nf = new NumberFormatter();
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
            }

            StringBuilder dispKpi = new StringBuilder();
            String drillReportId = kpiDetail.getKPIDrill(ElementId);
            String drillreptype = kpiDetail.getKPIDrillRepType(ElementId);
            String[] drilldets = new String[2];
            dispKpi.append("<Tr>");
            if (!collect.isOneviewCheckForKpis()) {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                    if (background != null && !background.isEmpty()) {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "')\"></a></Td>");
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "')\"></a></Td>");
                    }
                } else {
                    dispKpi.append("<td align='center' style=\"cursor:pointer\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "')\"></a></td>");
                }
            }
            if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                if (!drillReportId.equalsIgnoreCase("")) {
                    if (drillreptype != null && !("0".equals(drillreptype)) && !drillreptype.equalsIgnoreCase("") && drillreptype.equalsIgnoreCase("D")) {
                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                            if (background != null && !background.isEmpty()) {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + "\"><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                            }
                        } else {
                            dispKpi.append("<Td><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        }

                    } else {
                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                            if (background != null && !background.isEmpty()) {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "\"><strong>" + ElementName + "</strong></A></Td>");
                            } else {
                                dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + "\"><a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "\"><strong>" + ElementName + "</strong></A></Td>");
                            }
                        } else {
                            dispKpi.append("<Td><a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "\"><strong>" + ElementName + "</strong></A></Td>");
                        }
                    }
                } else {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                        if (background != null && !background.isEmpty()) {
                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
                        }
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer\"><strong>" + ElementName + "</strong></Td>");
                    }
                }
            } else {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                    if (background != null && !background.isEmpty()) {
                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
                    }
                } else {
                    dispKpi.append("<Td style=\"cursor:pointer\"><strong>" + ElementName + "</strong></Td>");
                }
            }

            int i = 0;
            Double currValMul = 0.0;
            int decimalplaces = 2;
            for (i = 0; i < kpiElements.size(); i++) {
                //if(kpiElements.size() > 1){
                String temp = "A_" + kpiElements.get(i).getElementId();
                String type = kpiElements.get(i).getRefElementType();

                // day object strts
                if (kpiDetail.isCurrentChecked()) {
                    if ((container.getMultiPeriodKPI().getDayObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getDayObject().getFieldValueString(0, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currValMul = Double.parseDouble((container.getMultiPeriodKPI().getDayObject().getFieldValueString(0, temp)));
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(currValMul, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                  if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                if (kpiDetail.isCurrentChecked()) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                                String negativeValue = (String) sb.toString();
                                                if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                    negativeValue = negativeValue.replace("-", " ");
                                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</a></Td>");
                                                    }
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</Td>");
                                        }
                                    } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                            String negativeValue = (String) sb.toString();
                                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                negativeValue = negativeValue.replace("-", " ");
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + sb.toString() + "</Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + sb.toString() + " </Td>");
                                        }
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + sb.toString() + "</Td>");
                                    }
                                }
                            } else {
                                BigDecimal bd4 = new BigDecimal(currValMul);
                                bd4 = bd4.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                if (kpiDetail.isCurrentChecked()) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                                String negativeValue = (String) formatter.format(bd4);
                                                if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                    negativeValue = negativeValue.replace("-", " ");
                                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd4) + "</a></Td>");
                                                    }
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd4) + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd4) + "</Td>");
                                        }
                                    } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                            String negativeValue = (String) formatter.format(bd4);
                                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                negativeValue = negativeValue.replace("-", " ");
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + formatter.format(bd4) + "</Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + formatter.format(bd4) + " </Td>");
                                        }
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + formatter.format(bd4) + "</Td>");
                                    }
                                }
                            }
                        }
                    } else {
                        if (kpiDetail.isCurrentChecked()) {
                            if (type.equalsIgnoreCase("1")) {
                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        dispKpi.append("<Td align=\"center\" style=\"cursor:pointer;color:" + font.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + currValMul + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" style=\"cursor:pointer;color:" + font.get(atrelementIds.indexOf(ElementId)) + "\">" + currValMul + "</Td>");
                                    }
                                } else {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + currValMul + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + currValMul + "</Td>");
                                    }
                                }
                            }
                        }
                    }
                }
                //day object ends

                // month object starts
                if (kpiDetail.isMTDChecked()) {
                    if ((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currValMul = Double.parseDouble((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)));
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(currValMul, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                  if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                if (kpiDetail.isMTDChecked()) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                                String negativeValue = (String) sb.toString();
                                                if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                    negativeValue = negativeValue.replace("-", " ");
                                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</a></Td>");
                                                    }
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + " </Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</Td>");
                                        }
                                    } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                            String negativeValue = (String) sb.toString();
                                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                negativeValue = negativeValue.replace("-", " ");
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + sb.toString() + "</Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + sb.toString() + " </Td>");
                                        }
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + sb.toString() + "</Td>");
                                    }
                                }
                            } else {
                                BigDecimal bd3 = new BigDecimal(currValMul);
                                bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                if (kpiDetail.isMTDChecked()) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                                String negativeValue = (String) formatter.format(bd3);
                                                if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                    negativeValue = negativeValue.replace("-", " ");
                                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd3) + "</a></Td>");
                                                    }
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd3) + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd3) + "</Td>");
                                        }
                                    } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                            String negativeValue = (String) formatter.format(bd3);
                                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                negativeValue = negativeValue.replace("-", " ");
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + formatter.format(bd3) + "</Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + formatter.format(bd3) + " </Td>");
                                        }
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + formatter.format(bd3) + "</Td>");
                                    }
                                }
                            }
                        }
                    } else {
                        if (kpiDetail.isMTDChecked()) {
                            if (type.equalsIgnoreCase("1")) {
                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        dispKpi.append("<Td align=\"center\" style=\"cursor:pointer;color:" + font.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + currValMul + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" style=\"cursor:pointer;color:" + font.get(atrelementIds.indexOf(ElementId)) + "\">" + currValMul + "</Td>");
                                    }
                                } else {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + currValMul + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + currValMul + "</Td>");
                                    }
                                }
                            }
                        }
                    }
                }
                // month object ends

                //quarter object starts
                if (kpiDetail.isQTDChecked()) {
                    if ((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currValMul = Double.parseDouble((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)));
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(currValMul, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                if (kpiDetail.isQTDChecked()) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                                String negativeValue = (String) sb.toString();
                                                if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                    negativeValue = negativeValue.replace("-", " ");
                                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</a></Td>");
                                                    }
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</Td>");
                                        }
                                    } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                            String negativeValue = (String) sb.toString();
                                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                negativeValue = negativeValue.replace("-", " ");
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + sb.toString() + "</Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + sb.toString() + " </Td>");
                                        }
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + sb.toString() + "</Td>");
                                    }
                                }
                            } else {
                                BigDecimal bd2 = new BigDecimal(currValMul);
                                bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                if (kpiDetail.isQTDChecked()) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                                String negativeValue = (String) formatter.format(bd2);
                                                if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                    negativeValue = negativeValue.replace("-", " ");
                                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd2) + "</a></Td>");
                                                    }
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd2) + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd2) + "</Td>");
                                        }
                                    } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                            String negativeValue = (String) formatter.format(bd2);
                                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                negativeValue = negativeValue.replace("-", " ");
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + formatter.format(bd2) + "</Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + formatter.format(bd2) + " </Td>");
                                        }
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + formatter.format(bd2) + "</Td>");
                                    }
                                }
                            }
                        }
                    } else {
                        if (kpiDetail.isQTDChecked()) {
                            if (type.equalsIgnoreCase("1")) {
                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        dispKpi.append("<Td align=\"center\" style=\"cursor:pointer;color:" + font.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + currValMul + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" style=\"cursor:pointer;color:" + font.get(atrelementIds.indexOf(ElementId)) + "\">" + currValMul + "</Td>");
                                    }
                                } else {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + currValMul + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + currValMul + "</Td>");
                                    }
                                }
                            }
                        }
                    }
                }
                // quarter object ends

                // year object starts
                if (kpiDetail.isYTDChecked()) {
                    if ((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currValMul = Double.parseDouble((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)));
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(currValMul, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                   if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                if (kpiDetail.isYTDChecked()) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                                String negativeValue = (String) sb.toString();
                                                if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                    negativeValue = negativeValue.replace("-", " ");
                                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</a></Td>");
                                                    }
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + sb.toString() + "</Td>");
                                        }
                                    } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                            String negativeValue = (String) sb.toString();
                                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                negativeValue = negativeValue.replace("-", " ");
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + sb.toString() + "</Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + sb.toString() + " </Td>");
                                        }
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + sb.toString() + "</Td>");
                                    }
                                }
                            } else {
                                BigDecimal bd1 = new BigDecimal(currValMul);
                                bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                if (kpiDetail.isYTDChecked()) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                            if (negativevalues != null && !negativevalues.isEmpty()) {
                                                String negativeValue = (String) formatter.format(bd1);
                                                if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                    negativeValue = negativeValue.replace("-", " ");
                                                    if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                    } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                        String negativecolor = "#f24040";
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + "(" + negativeValue + ")" + "</a></Td>");
                                                    } else {
                                                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd1) + "</a></Td>");
                                                    }
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + negativeValue + "</a></Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd1) + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + formatter.format(bd1) + "</Td>");
                                        }
                                    } else if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                                        if (negativevalues != null && !negativevalues.isEmpty()) {
                                            String negativeValue = (String) formatter.format(bd1);
                                            if (negativeValue != null && negativeValue.contains("-") && (negativeValue.contains("1") || negativeValue.contains("2") || negativeValue.contains("3") || negativeValue.contains("4") || negativeValue.contains("5") || negativeValue.contains("6") || negativeValue.contains("7") || negativeValue.contains("8") || negativeValue.contains("9") || negativeValue.contains("0"))) {
                                                negativeValue = negativeValue.replace("-", " ");
                                                if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                                    String negativecolor = "#f24040";
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + negativeValue + ")" + "</Td>");
                                                } else {
                                                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + negativeValue + "</Td>");
                                                }
                                            } else {
                                                dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + formatter.format(bd1) + "</Td>");
                                            }
                                        } else {
                                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\">" + formatter.format(bd1) + " </Td>");
                                        }
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + formatter.format(bd1) + "</Td>");
                                    }
                                }
                            }
                        }
                    } else {
                        if (kpiDetail.isYTDChecked()) {
                            if (type.equalsIgnoreCase("1")) {
                                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        dispKpi.append("<Td align=\"center\" style=\"cursor:pointer;color:" + font.get(atrelementIds.indexOf(ElementId)) + "\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + currValMul + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" style=\"cursor:pointer;color:" + font.get(atrelementIds.indexOf(ElementId)) + "\">" + currValMul + "</Td>");
                                    }
                                } else {
                                    if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + drillreptype + "')\">" + currValMul + "</a></Td>");
                                    } else {
                                        dispKpi.append("<Td align=\"center\" padding=\"0px\">" + currValMul + "</Td>");
                                    }
                                }
                            }
                        }
                    }
                }
                // year object ends

//                     if(type.equalsIgnoreCase("1")){
//                          dispKpi.append("<Td align=\"center\" padding=\"0px\">");
//                       dispKpi.append("<img src=\"icons pinvoke/mail.png\" style=\"text-decoration:none;cursor:pointer;\" onclick= \"viewSchedulerAndTracker()\" />");
//                        dispKpi.append("</Td>");
//                    }
                //}
            }
            //dispKpi.append("<Td style=\"cursor:pointer;\" align=\"center\" padding=\"0px\"><a onclick= \"viewSchedulerAndTracker('','" + ElementId + "','" + kpiMasterid + "','" + dashletId + "','" + collect.reportId + "','','','" + kpiType + "','" + ElementName + "')\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
            dispKpi.append("<Td style=\"cursor:pointer;\" align=\"center\" padding=\"0px\"><a onclick= \"viewAlertCondition('','" + ElementId + "','" + kpiMasterid + "','" + dashletId + "','" + collect.reportId + "','','','" + kpiType + "','" + ElementName + "','" + container.getCustomKPIHeaderNames() + "','" + isViewby + "','" + isViewbySecond + "')\"><span class=\"ui-icon ui-icon-alert\"></span></a></td>");
            dispKpi.append("</tr>");
            return dispKpi.toString();

        }
        return null;
    }

    public void setSchedulerFlag(boolean SchedulerFlag) {
        this.SchedulerFlag = SchedulerFlag;
    }

    public void setElemntIdForMail(String elemntIdForMail) {
        this.elemntIdForMail = elemntIdForMail;
    }

    public String genrateMultiPeriodKPICurrentPrior(Container container, String ElementId, String ElementName, String ReportId, String kpiMasterid,
            String kpiDrill, String kpiType, PbReturnObject pbretObjForTime, DashletDetail dashletDetail, boolean fromDesigner,
            pbDashboardCollection collect, String userId, String kpielementIds, String kpielemNames) throws Exception {
        String typeKPI = "MultiPeriodCurrentPrior";
        String attribute = "";
        if (container.getMultiPeriodKPI() != null) {
            KPI kpiDetail = (KPI) dashletDetail.getReportDetails();
            String dashletId = dashletDetail.getDashBoardDetailId();
            List<KPIElement> kpiElements = kpiDetail.getKPIElements(ElementId);
            String targetElementId = null;
            String targetElemName = null;
            List<KPIElement> targetkpielem = new ArrayList<KPIElement>();
            HashMap multiKpiDetailsMap = new HashMap();
            String targetValueStr = "--";
            Double monthTargetVal = kpiDetail.getTargetValue(ElementId, "Month");
            Double DayTargetVal = kpiDetail.getTargetValue(ElementId, "Day");
            Double QtrTargetVal = kpiDetail.getTargetValue(ElementId, "Qtr");
            Double yearTarget = kpiDetail.getTargetValue(ElementId, "Year");
            StringBuilder dispKpi = new StringBuilder();
            NumberFormatter nf = new NumberFormatter();
            int index;
            StringBuilder bizRolesString = new StringBuilder();
            String folderDetails = collect.reportBizRoles[0];
            if (collect.reportBizRoles != null) {
                for (String role : collect.reportBizRoles) {
                    bizRolesString.append(",").append(role);
                }
            }

            if (bizRolesString.length() > 0) {
                bizRolesString.replace(0, 1, "");
            }

            if (kpiDetail.getKpiTragetMap(ElementId) != null && !kpiDetail.getKpiTragetMap(ElementId).equalsIgnoreCase("")) {
                targetElementId = kpiDetail.getKpiTragetMap(ElementId);
            }
            HashMap TargetInfo = new HashMap();
            TargetInfo.put("ElementId", ElementId);// oth element
            TargetInfo.put("kpiMasterid", kpiMasterid);//1st element
            TargetInfo.put("ReportId", ReportId);
            TargetInfo.put("dashletId", dashletDetail.getDashBoardDetailId());
            TargetInfo.put("MonthTarget", monthTargetVal);
            TargetInfo.put("DayTargetVal", DayTargetVal);
            TargetInfo.put("QtrTargetVal", QtrTargetVal);
            TargetInfo.put("yearTarget", yearTarget);
            TargetInfo.put("targetElementId", targetElementId);
            if (targetElementId != null && !targetElementId.equalsIgnoreCase("")) {

                targetkpielem = kpiDetail.getTargetKPIElements(targetElementId);
            }
            if (targetkpielem != null) {
                for (KPIElement elem : targetkpielem) {
                    targetElemName = elem.getElementName();
                }
            }

            String drillReportId = kpiDetail.getKPIDrill(ElementId);
            String repType = kpiDetail.getKPIDrillRepType(ElementId);
//     innerTdHtml.append("<Td align=\"center\" padding=\"0px\"><a onclick=\"getTargetMapElements('"+elementId+"','"+folderDetails+"','"+ReportId+"','"+dashletId+"')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
            dispKpi.append("<Tr>");
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                if (background != null && !background.isEmpty()) {
                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
                } else {
                    dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
                }
            } else {
                dispKpi.append("<Td align=\"center\" padding=\"0px\" width=\"250px\"><a onclick=\"getTargetMapElements('" + ElementId + "','" + folderDetails + "','" + ReportId + "','" + dashletId + "','" + kpiMasterid + "','" + targetElementId + "','" + targetElemName + "')\" class=\"ui-icon ui-icon-extlink\"></a></Td>");
            }
            if (!collect.isOneviewCheckForKpis()) {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                    if (background != null && !background.isEmpty()) {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "','" + kpiMasterid + "')\"></a></Td>");
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "','" + kpiMasterid + "')\"></a></Td>");
                    }
                } else {
                    dispKpi.append("<td align='center' style=\"cursor:pointer\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + ElementId + "','" + ElementName + "','" + kpiMasterid + "')\"></a></td>");
                }
            }
            if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                if (repType != null && !("0".equals(repType)) && !repType.equalsIgnoreCase("") && repType.equalsIgnoreCase("D")) {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                        if (background != null && !background.isEmpty()) {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                        }
                    } else {
                        dispKpi.append("<Td><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + ElementName + "</strong></A></Td>");
                    }

                } else {
                    if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                        if (background != null && !background.isEmpty()) {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "\"><strong>" + ElementName + "</strong></A></Td>");
                        } else {
                            dispKpi.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "\"><strong>" + ElementName + "</strong></A></Td>");
                        }
                    } else {
                        dispKpi.append("<Td><a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "\"><strong>" + ElementName + "</strong></A></Td>");
                    }

                }
            } else {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                    if (background != null && !background.isEmpty()) {
                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
                    } else {
                        dispKpi.append("<Td style=\"cursor:pointer;text-align:" + alignment.get(atrelementIds.indexOf(ElementId)) + ";color: " + font.get(atrelementIds.indexOf(ElementId)) + "\"><strong>" + ElementName + "</strong></Td>");
                    }
                } else {
                    dispKpi.append("<Td style=\"cursor:pointer\"><strong>" + ElementName + "</strong></Td>");
                }
            }

            int i = 0;
            Double currValMul = 0.0;
            int decimalplaces = 2;
            double priorVal = 0.0;
            Double changeVal = 0.0;
            for (i = 0; i < kpiElements.size(); i++) {
                String temp = "A_" + kpiElements.get(i).getElementId();
                String type = kpiElements.get(i).getRefElementType();
                //month object starts

                if (kpiDetail.isMTDChecked()) {
                    if ((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currValMul = Double.parseDouble((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)));
                            multiKpiDetailsMap.put("AbsMntCurrVal", currValMul);
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(currValMul, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner && !drillReportId.equalsIgnoreCase("")) {
                                    multiKpiDetailsMap.put("MonthCurrentVal", "<a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + repType + "')\">" + sb.toString());
                                } else {
                                    multiKpiDetailsMap.put("MonthCurrentVal", sb.toString());
                                }
                            } else {
                                BigDecimal bd3 = new BigDecimal(currValMul);
                                bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                                    multiKpiDetailsMap.put("MonthCurrentVal", "<a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + repType + "')\">" + formatter.format(bd3));
                                } else {
                                    multiKpiDetailsMap.put("MonthCurrentVal", formatter.format(bd3));
                                }
                            }

                        } else if (type.equalsIgnoreCase("2")) {
                            String priorValStr = container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp);
                            if (priorValStr != null && !("".equals(priorValStr))) {
                                priorVal = Double.parseDouble(priorValStr);
                            }
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(priorVal, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                multiKpiDetailsMap.put("MonthPriorVal", sb.toString());
                            } else {
                                BigDecimal bd3 = new BigDecimal(priorVal);
                                bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("MonthPriorVal", formatter.format(bd3));
                            }

                        } else if (type.equalsIgnoreCase("3")) {
                            String changeValStr = container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp);
                            if (changeValStr != null && !("".equals(changeValStr))) {
                                changeVal = Double.parseDouble(changeValStr);
                            }
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(changeVal, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                multiKpiDetailsMap.put("MonthChangeVal", sb.toString());
                            } else {
                                BigDecimal bd3 = new BigDecimal(changeVal);
                                bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("MonthChangeVal", formatter.format(bd3));
                            }

                        } else if (type.equalsIgnoreCase("4")) {
                            double changePercVal = Double.parseDouble((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)));
                            BigDecimal bd3 = new BigDecimal(changePercVal);
                            bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                            multiKpiDetailsMap.put("MonthChangePercenVal", formatter.format(bd3));
                        }
                    } else {
                    }

                    // month object ends
                }
                // quarter object starts
                if (kpiDetail.isQTDChecked()) {
                    if ((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currValMul = Double.parseDouble((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)));
                            multiKpiDetailsMap.put("AbsQtrCurrVal", currValMul);
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(currValMul, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                               if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                                    multiKpiDetailsMap.put("QtrCurrentVal", "<a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + repType + "')\">" + sb.toString());
                                } else {
                                    multiKpiDetailsMap.put("QtrCurrentVal", sb.toString());
                                }
                            } else {
                                BigDecimal bd2 = new BigDecimal(currValMul);
                                bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                                    multiKpiDetailsMap.put("QtrCurrentVal", "<a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + repType + "')\">" + formatter.format(bd2));
                                } else {
                                    multiKpiDetailsMap.put("QtrCurrentVal", formatter.format(bd2));
                                }
                            }

                        } else if (type.equalsIgnoreCase("2")) {
                            String priorValStr = container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp);
                            if (priorValStr != null && !("".equals(priorValStr))) {
                                priorVal = Double.parseDouble(priorValStr);
                            }
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(priorVal, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                multiKpiDetailsMap.put("QtrPriorVal", sb.toString());
                            } else {
                                BigDecimal bd2 = new BigDecimal(priorVal);
                                bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("QtrPriorVal", formatter.format(bd2));
                            }

                        } else if (type.equalsIgnoreCase("3")) {
                            String changeValStr = container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp);
                            if (changeValStr != null && !("".equals(changeValStr))) {
                                changeVal = Double.parseDouble(changeValStr);
                            }
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(changeVal, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                multiKpiDetailsMap.put("QtrChangeVal", sb.toString());
                            } else {
                                BigDecimal bd2 = new BigDecimal(changeVal);
                                bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("QtrChangeVal", formatter.format(bd2));
                            }
                        } else if (type.equalsIgnoreCase("4")) {
                            double changePercVal = Double.parseDouble((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)));
                            BigDecimal bd2 = new BigDecimal(changePercVal);
                            bd2 = bd2.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                            multiKpiDetailsMap.put("QtrChangePercenVal", formatter.format(bd2));
                        }
                    }
                }
                // ends quarter object
                if (kpiDetail.isYTDChecked()) {
                    // year object starts
                    if ((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)))) {
                        if (type.equalsIgnoreCase("1")) {
                            currValMul = Double.parseDouble((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)));
                            multiKpiDetailsMap.put("AbsYrCurrVal", currValMul);
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(currValMul, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                              if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                                    multiKpiDetailsMap.put("YearCurrentVal", "<a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + repType + "')\">" + sb.toString());
                                } else {
                                    multiKpiDetailsMap.put("YearCurrentVal", sb.toString());
                                }
                            } else {
                                BigDecimal bd1 = new BigDecimal(currValMul);
                                bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                                    multiKpiDetailsMap.put("YearCurrentVal", "<a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + repType + "')\">" + formatter.format(bd1));
                                } else {
                                    multiKpiDetailsMap.put("YearCurrentVal", formatter.format(bd1));
                                }
                            }

                        } else if (type.equalsIgnoreCase("2")) {
                            String priorValStr = container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp);
                            if (priorValStr != null && !("".equals(priorValStr))) {
                                priorVal = Double.parseDouble(priorValStr);
                            }
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(priorVal, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                multiKpiDetailsMap.put("YearPriorVal", sb.toString());
                            } else {
                                BigDecimal bd1 = new BigDecimal(priorVal);
                                bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("YearPriorVal", formatter.format(bd1));
                            }

                        } else if (type.equalsIgnoreCase("3")) {
                            String changeValStr = container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp);
                            if (changeValStr != null && !("".equals(changeValStr))) {
                                changeVal = Double.parseDouble(changeValStr);
                            }
                            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                                index = atrelementIds.indexOf(ElementId);
                                int rounding;
                                if (round.get(index) == "") {
                                    rounding = 0;
                                } else {
                                    rounding = Integer.parseInt(round.get(index));
                                }
                                String value = nf.getModifiedNumber(changeVal, numberFormat.get(index), rounding);
                                attribute = numberFormat.get(index).toString();
                                if (!attribute.equalsIgnoreCase("")) {
                                    if (attribute.contains("M")) {
                                        value = value.replace("M", "");
                                    }
                                    if (attribute.contains("K")) {
                                        value = value.replace("K", "");
                                    }
                                    if (attribute.contains("Cr")) {
                                        value = value.replace("Cr", "");
                                    }
                                    if (attribute.contains("L")) {
                                        value = value.replace("L", "");
                                    }
                                }
                                StringBuffer sb = new StringBuffer();
                                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                                }
                                multiKpiDetailsMap.put("YearchangeVal", sb.toString());
                            } else {
                                BigDecimal bd1 = new BigDecimal(changeVal);
                                bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("YearchangeVal", formatter.format(bd1));
                            }

                        } else if (type.equalsIgnoreCase("4")) {
                            double changePercVal = Double.parseDouble((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)));
                            BigDecimal bd1 = new BigDecimal(changePercVal);
                            bd1 = bd1.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                            NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                            multiKpiDetailsMap.put("YearChangePercenVal", formatter.format(bd1));

                        }
                    }
                }
                //year object ends

            }

            //getting the target element details...
            if (targetElementId != null && !targetElementId.equalsIgnoreCase("")) {
                List<KPIElement> targetkpielements = kpiDetail.getTargetKPIElements(targetElementId);
                for (i = 0; i < targetkpielements.size(); i++) {
                    String temp = "A_" + targetkpielements.get(i).getElementId();
                    String type = targetkpielements.get(i).getRefElementType();
                    //month object starts
                    if (kpiDetail.isMTDChecked()) {
                        if ((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)))) {
                            if (type.equalsIgnoreCase("1")) {
                                currValMul = Double.parseDouble((container.getMultiPeriodKPI().getMonthObject().getFieldValueString(0, temp)));
                                BigDecimal bd3 = new BigDecimal(currValMul);
                                bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("MonthCustomTargetVal", bd3.toString());
                            }
                        }
                    }
                    // month object ends
                    // quarter object starts
                    if (kpiDetail.isQTDChecked()) {
                        if ((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)))) {
                            if (type.equalsIgnoreCase("1")) {
                                currValMul = Double.parseDouble((container.getMultiPeriodKPI().getQuarterObject().getFieldValueString(0, temp)));
                                BigDecimal bd3 = new BigDecimal(currValMul);
                                bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("QtrCustomTargetVal", bd3.toString());
                            }
                        }
                    }
                    //quarter object ends
                    //year object starts
                    if (kpiDetail.isYTDChecked()) {
                        if ((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)))) {
                            if (type.equalsIgnoreCase("1")) {
                                currValMul = Double.parseDouble((container.getMultiPeriodKPI().getYearObject().getFieldValueString(0, temp)));
                                BigDecimal bd3 = new BigDecimal(currValMul);
                                bd3 = bd3.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
                                NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
                                multiKpiDetailsMap.put("YearCustomTargetVal", bd3.toString());
                            }
                        }
                    }
                    //year object ends
                }
            }
            dispKpi.append(buildMultiPeriodKPI(multiKpiDetailsMap, typeKPI, kpiDetail, fromDesigner, TargetInfo, collect, container, dashletDetail, ElementId, drillReportId));
            dispKpi.append("</tr>");
            return dispKpi.toString();

        }
        return null;

    }

    public String buildMultiPeriodKPI(HashMap multiKpiDetailsMap, String typeKPI, KPI kpiDetail, boolean fromDesigner, HashMap targetInfo, pbDashboardCollection collect, Container container, DashletDetail dashletDetail, String ElementId, String drillReportId) {
        StringBuilder innerTdHtml = new StringBuilder();
        String targetValStr = "--";
        String elementId = targetInfo.get("ElementId").toString();
        String kpiMasterid = targetInfo.get("kpiMasterid").toString();
        String ReportId = targetInfo.get("ReportId").toString();
        String dashletId = targetInfo.get("dashletId").toString();
        String monthDevPer = "0.0";
        String dayDevPer = "0.0";
        String qtrDevPer = "0.0";
        String yearDevPer = "0.0";
        String monthTarget = "--";
        String dayTarget = "--";
        String qtrTarget = "--";
        String yearTarget = "--";
        String monthDevVal = "--";
        String dayDevVal = "--";
        String qtrDevVal = "--";
        String yearDevVal = "--";
        int decimalplaces = 0;
        DecimalFormat oneDForm = new DecimalFormat("#.0");
        BigDecimal bd;
        String targetElementId = null;
        NumberFormatter nf = new NumberFormatter();
        int index;
        String AbsMntcurrVal;
        String QtrTargetval;
        String AbsQtrCurrVal;
        String AbsYrCurrVal;
        if (targetInfo.get("targetElementId") != null) {
            targetElementId = targetInfo.get("targetElementId").toString();
        }
        if (multiKpiDetailsMap.get("AbsMntCurrVal") != null) {
            AbsMntcurrVal = multiKpiDetailsMap.get("AbsMntCurrVal").toString();
        } else {
            AbsMntcurrVal = "0.0";
        }
        if (multiKpiDetailsMap.get("AbsQtrCurrVal") != null) {
            AbsQtrCurrVal = multiKpiDetailsMap.get("AbsQtrCurrVal").toString();
        } else {
            AbsQtrCurrVal = "0.0";
        }
        if (multiKpiDetailsMap.get("AbsYrCurrVal") != null) {
            AbsYrCurrVal = multiKpiDetailsMap.get("AbsYrCurrVal").toString();
        } else {
            AbsYrCurrVal = "0.0";
        }
        if (targetInfo.get("QtrTargetVal") != null) {
            QtrTargetval = targetInfo.get("QtrTargetVal").toString();
        } else {
            QtrTargetval = "0.0";
        }

        if (multiKpiDetailsMap.get("MonthCustomTargetVal") != null) {
            double monthTargetVal = Double.parseDouble(multiKpiDetailsMap.get("MonthCustomTargetVal").toString());
            bd = new BigDecimal(Double.parseDouble(multiKpiDetailsMap.get("MonthCustomTargetVal").toString()));
            bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(monthTargetVal, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                monthTarget = sb.toString();

            } else {
                monthTarget = bd.toString();
            }
            double val = Double.parseDouble(bd.toString());
            BigDecimal DEVAL = kpiDetail.getDeviationVal(new BigDecimal(AbsMntcurrVal), new BigDecimal(multiKpiDetailsMap.get("MonthCustomTargetVal").toString()));
            DEVAL = DEVAL.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(DEVAL, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                    if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                        sb.append(" ");
//                     if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                    }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                        sb.append(value.charAt(k));
//                    }
//                 }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                monthDevVal = sb.toString();
            } else {
                monthDevVal = DEVAL.toString();
            }

            BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsMntcurrVal.toString()), Double.parseDouble(multiKpiDetailsMap.get("MonthCustomTargetVal").toString()));
            monthDevPer = String.valueOf(devPer);
            monthDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
            if (monthDevPer.contains("M") || monthDevPer.contains("K")) {
                devPer = devPer.setScale(2, RoundingMode.HALF_EVEN);
                monthDevPer = (devPer) + "%";
            }

        } else if (targetInfo.get("MonthTarget") != null) {
            double monthTargetVal = Double.parseDouble(targetInfo.get("MonthTarget").toString());
            bd = new BigDecimal(Double.parseDouble(targetInfo.get("MonthTarget").toString()));
            bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(monthTargetVal, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                monthTarget = sb.toString();

            } else {
                monthTarget = bd.toString();
            }
//             NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));

            double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(Double.parseDouble(AbsMntcurrVal))), Double.parseDouble(targetInfo.get("MonthTarget").toString()));
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(DEVAL, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                monthDevVal = sb.toString();
            } else {
                monthDevVal = Double.toString(DEVAL);
            }
            BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsMntcurrVal), Double.parseDouble(targetInfo.get("MonthTarget").toString()));
            monthDevPer = String.valueOf(devPer);
            monthDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
            if (monthDevPer.contains("M")) {
                monthDevPer = (devPer) + "%";
            }

        } else {
            // you can add some more options for getting the target
        }

        // for Day level
        if (targetInfo.get("DayTargetVal") != null) {
            bd = new BigDecimal(Double.parseDouble(targetInfo.get("DayTargetVal").toString()));
            bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
//             NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//             dayTarget = formatter.format(bd);
            double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(Double.parseDouble(AbsQtrCurrVal))), Double.parseDouble(QtrTargetval));
            dayDevVal = Double.toString(DEVAL);
            dayTarget = bd.toString();
            BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsQtrCurrVal), Double.parseDouble(QtrTargetval));
            dayDevPer = String.valueOf(devPer);
            dayDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
            if (dayDevPer.contains("M")) {
                dayDevPer = (devPer) + "%";
            }

        }

        if (multiKpiDetailsMap.get("QtrCustomTargetVal") != null) {
            bd = new BigDecimal(Double.parseDouble(multiKpiDetailsMap.get("QtrCustomTargetVal").toString()));
            bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            double qtrTargetVal = Double.parseDouble(multiKpiDetailsMap.get("QtrCustomTargetVal").toString());
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(qtrTargetVal, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                qtrTarget = sb.toString();

            } else {
                qtrTarget = bd.toString();
            }
//             NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//             qtrTarget = formatter.format(bd);
            BigDecimal DEVAL = kpiDetail.getDeviationVal(new BigDecimal(AbsQtrCurrVal), new BigDecimal(multiKpiDetailsMap.get("QtrCustomTargetVal").toString()));
            DEVAL = DEVAL.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(DEVAL, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                               if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                qtrDevVal = sb.toString();
            } else {
                qtrDevVal = DEVAL.toString();
            }

            BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsQtrCurrVal), Double.parseDouble(multiKpiDetailsMap.get("QtrCustomTargetVal").toString()));
            qtrDevPer = String.valueOf(devPer);
            qtrDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
            if (qtrDevPer.contains("M")) {
                qtrDevPer = (devPer) + "%";
            }
        } else if (targetInfo.get("QtrTargetVal") != null) {
            bd = new BigDecimal(Double.parseDouble(targetInfo.get("QtrTargetVal").toString()));
            bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            double QtrTargetVal = Double.parseDouble(targetInfo.get("QtrTargetVal").toString());
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(QtrTargetVal, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                qtrTarget = sb.toString();

            } else {
                qtrTarget = bd.toString();
            }
//             NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//             qtrTarget = formatter.format(bd);
            double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(Double.parseDouble(AbsQtrCurrVal))), Double.parseDouble(targetInfo.get("QtrTargetVal").toString()));
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(DEVAL, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                qtrDevVal = sb.toString();
            } else {
                qtrDevVal = Double.toString(DEVAL);
            }
            BigDecimal devPer = kpiDetail.getDeviationPer(Double.parseDouble(AbsQtrCurrVal), Double.parseDouble(targetInfo.get("QtrTargetVal").toString()));
            qtrDevPer = String.valueOf(devPer);
            qtrDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
            if (qtrDevPer.contains("M")) {
                qtrDevPer = (devPer) + "%";
            }
        } else {
        }

        if (multiKpiDetailsMap.get("YearCustomTargetVal") != null) {
            bd = new BigDecimal(Double.parseDouble(multiKpiDetailsMap.get("YearCustomTargetVal").toString()));
            bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            double yearTargetVal = Double.parseDouble(multiKpiDetailsMap.get("YearCustomTargetVal").toString());
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(yearTargetVal, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                 if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                yearTarget = sb.toString();

            } else {
                yearTarget = bd.toString();
            }
//             NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//             yearTarget = formatter.format(bd);
            BigDecimal DEVAL = kpiDetail.getDeviationVal(new BigDecimal(AbsYrCurrVal), new BigDecimal(multiKpiDetailsMap.get("YearCustomTargetVal").toString()));
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(DEVAL, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                yearDevVal = sb.toString();
            } else {
                yearDevVal = DEVAL.toString();
            }

            BigDecimal devPer = kpiDetail.getDeviationPer(Math.abs(Double.parseDouble(AbsYrCurrVal)), Math.abs(Double.parseDouble(multiKpiDetailsMap.get("YearCustomTargetVal").toString())));
            yearDevPer = String.valueOf(devPer);
            yearDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
            if (yearDevPer.contains("M")) {
                yearDevPer = String.valueOf(devPer) + "%";
            }
        } else if (targetInfo.get("yearTarget") != null) {
            bd = new BigDecimal(Double.parseDouble(targetInfo.get("yearTarget").toString()));
            bd = bd.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
            double yrTargetVal = Double.parseDouble(targetInfo.get("yearTarget").toString());
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(yrTargetVal, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                               if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                yearTarget = sb.toString();

            } else {
                yearTarget = bd.toString();
            }
//             NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
//             yearTarget = formatter.format(bd);
            double DEVAL = kpiDetail.getDeviationVal(new Double(oneDForm.format(Double.parseDouble(AbsYrCurrVal))), Double.parseDouble(targetInfo.get("yearTarget").toString()));
            if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                index = atrelementIds.indexOf(ElementId);
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                String value = nf.getModifiedNumber(DEVAL, numberFormat.get(index), rounding);
                StringBuffer sb = new StringBuffer();
                for (int k = 0; k < value.length(); k++) {
//                                             if(value.charAt(k)=='M' || value.charAt(k)=='C' || value.charAt(k)=='L' || value.charAt(k)=='K'){
//                                                 sb.append(" ");
//                                                if(selectrepids.contains(ElementId)){
                    sb.append(value.charAt(k));
//                                                   }
//                                             }else{
//                                                 if(!selectrepids.contains(ElementId)){
//                                                      if(value.charAt(k)!='r')
//                                                      {
//                                             sb.append(value.charAt(k));
//                                             }
//                                         }
//                                                 else{
//                                             sb.append(value.charAt(k));
//                                                 }
//                                             }
                }
                yearDevVal = sb.toString();
            } else {
                yearDevVal = Double.toString(DEVAL);
            }
            BigDecimal devPer = kpiDetail.getDeviationPer(Math.abs(Double.parseDouble(AbsYrCurrVal)), Math.abs(Double.parseDouble(targetInfo.get("yearTarget").toString())));
            yearDevPer = String.valueOf(devPer);
            yearDevPer = NumberFormatter.getModifiedNumber(devPer) + "%";
            if (yearDevPer.contains("M")) {
                yearDevPer = devPer + "%";
            }
        } else {
        }

        if (typeKPI.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
            String curval = "0";
            if (kpiDetail.isMTDChecked() || fromDesigner) {
                if (multiKpiDetailsMap.get("MonthCurrentVal") != null) {
                    curval = multiKpiDetailsMap.get("MonthCurrentVal").toString();
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("MonthCurrentVal");
                        if (value == null) {
                            value = "--";
                        }
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthCurrentVal") + "</Td>");
                            }
                        } else {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + kpiDetail.getKPIDrillRepType(ElementId) + "')\">" + value + "</a></Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            }
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthCurrentVal") + "</Td>");
                    }
                    //ends
                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthCurrentVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) monthTarget;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + monthTarget + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Month:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + value + "</a></Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Month:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + monthTarget + "</a></Td>");
                    }
                    //ends
                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Month:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + monthTarget + "</a></Td>");
                }
                String priorval = "0";
                if (multiKpiDetailsMap.get("MonthPriorVal") != null) {
                    priorval = multiKpiDetailsMap.get("MonthPriorVal").toString();
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("MonthPriorVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthPriorVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthPriorVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthPriorVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty() && background != null && !background.isEmpty()) {
                        String value = (String) monthDevVal;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + monthDevVal + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + monthDevVal + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + monthDevVal + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) monthDevPer;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + monthDevPer + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + monthDevPer + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + monthDevPer + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("MonthChangeVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthChangeVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthChangeVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthChangeVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("MonthChangePercenVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthChangePercenVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthChangePercenVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("MonthChangePercenVal") + "</Td>");
                }
            }
            if (kpiDetail.isQTDChecked() || fromDesigner) {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("QtrCurrentVal");
                        if (value == null) {
                            value = "--";
                        }
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrCurrentVal") + "</Td>");
                            }
                        } else {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + kpiDetail.getKPIDrillRepType(ElementId) + "')\">" + value + "</a></Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            }
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrCurrentVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrCurrentVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) qtrTarget;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Qtr:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + "(" + value + ")" + "</a></Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Qtr:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + value + "</a></Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Qtr:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + "(" + value + ")" + "</a></Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Qtr:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + qtrTarget + "</a></Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Qtr:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + value + "</a></Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Qtr:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + qtrTarget + "</a></Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Qtr:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + qtrTarget + "</a></Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("QtrPriorVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrPriorVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrPriorVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrPriorVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) qtrDevVal;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (value != null && negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + qtrDevVal + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + qtrDevVal + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + qtrDevVal + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) qtrDevPer;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + qtrDevPer + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + qtrDevPer + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + qtrDevPer + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId)) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("QtrChangeVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrChangeVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrChangeVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrChangeVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("QtrChangePercenVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrChangePercenVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrChangePercenVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("QtrChangePercenVal") + "</Td>");
                }
            }
            if (kpiDetail.isYTDChecked() || fromDesigner) {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("YearCurrentVal");
                        if (value == null) {
                            value = "--";
                        }
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearCurrentVal") + "</Td>");
                            }
                        } else {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !fromDesigner) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + ElementId + "','" + kpiDetail.getKPIDrillRepType(ElementId) + "')\">" + value + "</a></Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            }
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearCurrentVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearCurrentVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) yearTarget;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Year:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + "(" + value + ")" + "</a></Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Year:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + value + "</a></Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Year:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + "(" + value + ")" + "</a></Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Year:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + yearTarget + "</a></Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Year:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + value + "</a></Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Year:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + yearTarget + "</a></Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\"><a href=\"javascript:void(0)\" onclick=\"changeTargetValue('" + elementId + ":" + kpiMasterid + ":Year:" + targetValStr + ":" + ReportId + ":" + dashletId + "','" + typeKPI + "')\">" + yearTarget + "</a></Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("YearPriorVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearPriorVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearPriorVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearPriorVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) yearDevVal;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + yearDevVal + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + yearDevVal + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + yearDevVal + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) yearDevPer;
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + yearDevPer + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + yearDevPer + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + yearDevPer + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("YearchangeVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearchangeVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearchangeVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearchangeVal") + "</Td>");
                }
                if (!atrelementIds.isEmpty() && atrelementIds.contains(ElementId) && background != null && !background.isEmpty()) {
                    // to check whether value is negative and display with red, bracket or red&bracket
                    if (negativevalues != null && !negativevalues.isEmpty()) {
                        String value = (String) multiKpiDetailsMap.get("YearChangePercenVal");
                        if (value != null && value.contains("-") && (value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0"))) {
                            value = value.replace("-", " ");
                            if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket")) {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                            } else if (negativevalues.get(atrelementIds.indexOf(ElementId)).equalsIgnoreCase("Bracket&Red")) {
                                String negativecolor = "#f24040";
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + negativecolor + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + "(" + value + ")" + "</Td>");
                            } else {
                                innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearChangePercenVal") + "</Td>");
                            }
                        } else {
                            innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + value + "</Td>");
                        }
                    } else {
                        innerTdHtml.append("<Td style=\"cursor:pointer;color: " + font.get(atrelementIds.indexOf(ElementId)) + ";background-color:" + background.get(atrelementIds.indexOf(ElementId)) + "\" align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearChangePercenVal") + "</Td>");
                    }

                } else {
                    innerTdHtml.append("<Td align=\"center\" padding=\"0px\">" + multiKpiDetailsMap.get("YearChangePercenVal") + "</Td>");
                }
            }
        } else if (typeKPI.equalsIgnoreCase("")) {
            //write code for any other multiperiodKPI Enhancement
        }

        return innerTdHtml.toString();
    }

    private String getGroupVal(KPISingleGroupHelper groupingHelper, PbReturnObject pbretObj, int decimalPlaces, String kpiMasterId, DashletDetail dashletDetail, String dashBoardId, pbDashboardCollection collect) {
        BigDecimal sumBd = new BigDecimal(0);
        StringBuilder tableBuffer = new StringBuilder();
        KPI kpiDetail = (KPI) dashletDetail.getReportDetails();
        String attribute = "";
        String targetValue = " ";
        String dev = "--";
        String devper = "--";
        String TargetdevPer = "--";
        double currVal = 0.0;
        double priorVal = 0.0;
        BigDecimal grpcurrVal = new BigDecimal(0);
        BigDecimal grppriorVal = new BigDecimal(0);
        int sum = 0;
        int tableWidth = 200;
        String drillReportId = "";
        String drillRepType = "";
        if (groupingHelper.getGroupName() != null) {
            drillReportId = groupingHelper.getGroupKPIDrill(groupingHelper.getGroupName());
            drillRepType = groupingHelper.getGroupKPIDrillType(groupingHelper.getGroupName());
        }
        StringBuilder kpiBarChart = new StringBuilder();
        String[] COLORS = {
            "#0095B6",
            "#9ACD32",
            "#007BA7",
            "#0095B6",
            "#78866B",
            "#00FFFF",
            "#0067A5",
            "#9ACD32",
            "#FEFE33",
            "#4F7942",
            "#CFB53B",
            "#006600",
            "#003300",
            //"#cc9700", //
            "#ecddoo",};
        String kpiType = dashletDetail.getKpiType();
        String icon = "";
        double changePercVal = 0.0;
        BigDecimal changeperVal = new BigDecimal(0);
        int decimalplaces = 2;
        String None = " ";
        double Count = 0.0;
        BigDecimal average = new BigDecimal(0);
        String ReportId = dashBoardId;
        for (String string : groupingHelper.getElementIds()) {
            Count = Count + 1;
            if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                sumBd = sumBd.add(new BigDecimal(pbretObj.getFieldValueString(0, "A_" + string.trim())));
            }
            if (kpiType.equalsIgnoreCase("Target") || kpiType.equalsIgnoreCase("Standard")) {
                List<KPIElement> kpiElements = kpiDetail.getKPIElements(string);
                int i = 0;
                for (i = 0; i < kpiElements.size(); i++) {
                    if (pbretObj.getRowCount() > 0) {
                        String temp = "A_" + kpiElements.get(i).getElementId();
                        String type = kpiElements.get(i).getRefElementType();
                        if (kpiElements.size() > 1) {
                            if ((pbretObj.getFieldValueString(0, temp)) != null && !("".equalsIgnoreCase(pbretObj.getFieldValueString(0, temp)))) {
                                if (type.equalsIgnoreCase("1")) {
                                    currVal = Double.parseDouble(pbretObj.getFieldValueString(0, temp));
                                    grpcurrVal = grpcurrVal.add(new BigDecimal(currVal));
                                } else if (type.equalsIgnoreCase("2")) {
                                    String grppriorValStr = pbretObj.getFieldValueString(0, temp);
                                    priorVal = Double.parseDouble(grppriorValStr);
                                    if (grppriorValStr != null && !("".equals(grppriorValStr))) {
                                        grppriorVal = grppriorVal.add(new BigDecimal(grppriorValStr));
                                    }

                                } else if (type.equalsIgnoreCase("3")) {
                                } else if (type.equalsIgnoreCase("4")) {
                                    changePercVal = Double.parseDouble((pbretObj.getFieldValueString(0, temp)));
                                    changeperVal = changeperVal.add(new BigDecimal(pbretObj.getFieldValueString(0, temp)));
                                    String elemId = kpiElements.get(i).getRefElementId();
                                    String color = kpiDetail.getKPIColor(elemId, changePercVal);

                                    if ("Green".equalsIgnoreCase(color)) {
                                        icon = "icons pinvoke/status.png";
                                    } else if ("Red".equalsIgnoreCase(color)) {
                                        icon = "icons pinvoke/status-busy.png";
                                    } else if ("Yellow".equalsIgnoreCase(color)) {
                                        icon = "icons pinvoke/brightness-small.png";
                                    } else {
                                        icon = "icons pinvoke/status-offline.png";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
            changeperVal = changeperVal.divide(new BigDecimal(Count));
            grpcurrVal = grpcurrVal.divide(new BigDecimal(Count));
            grppriorVal = grppriorVal.divide(new BigDecimal(Count));
        }
        changeperVal = changeperVal.setScale(decimalplaces, BigDecimal.ROUND_HALF_UP);
        BigDecimal[] grpValArray = {grpcurrVal, grppriorVal};
        double[] valArray = {currVal, priorVal};
        double[] celWidth = new double[valArray.length];
        for (int j = 0; j < valArray.length; j++) {
            sum += (valArray[j]);
        }
        for (int k = 0; k < valArray.length; k++) {
            celWidth[k] = ((tableWidth * (valArray[k])) / sum);
            celWidth[k] = Math.round(celWidth[k]);
        }
        kpiBarChart.append("<Table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\" ><Tr>");
        for (int kpi = 0; kpi < grpValArray.length; kpi++) {
            String value = pbretObj.getModifiedNumber(grpValArray[kpi]);
            if (priorVal == 0.0) {
                if (kpi == 1) {
                    continue;
                } else {
                    COLORS[kpi] = "";
                }
            }
            if (celWidth[kpi] == 0.0) {
                COLORS[kpi] = "#FFFFFF";
            }
            if (COLORS[kpi] == "") {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(groupingHelper.getGroupName())) {
                    int index = atrelementIds.indexOf(groupingHelper.getGroupName());
                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + value + "</Td>");
                } else {
                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                }
            } else {
                if (!atrelementIds.isEmpty() && atrelementIds.contains(groupingHelper.getGroupName())) {
                    int index = atrelementIds.indexOf(groupingHelper.getGroupName());
                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;padding:6px;valign=middle\" align=\"center\" >" + symbols.get(index) + value + "</Td>");
                } else {
                    kpiBarChart.append("<Td style=\"width:" + celWidth[kpi] + "px;height:6px;font-size:10px;font-family:VERDANA;color:#fff;background-color:" + COLORS[kpi] + " ;padding:6px;valign=middle\" align=\"center\" >" + value + "</Td>");
                }
            }
        }
        kpiBarChart.append("</Tr></Table>");
        tableBuffer.append("<tr>");
//           for(String elementID:groupingHelper.getElementIds()){
//               BigDecimal measureval=grpMapValues.get(elementID);
//               if(measureval!=null)
//              sumBd=sumBd.add(measureval);
//               else
//                   sumBd=sumBd.add(new BigDecimal(pbretObj.getFieldValueString(0,"A_"+elementID.trim())));
//
//           }
        if (kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
            tableBuffer.append("<td ><a class=\"ui-icon ui-icon-extlink\"></a></td>");
        }
        if (!collect.isOneviewCheckForKpis()) {
            tableBuffer.append("<td align='center' style=\"cursor:pointer\"><a  class=\" ui-icon ui-icon-gear\" title=\"Drill To Report\" onclick=\"kpiDrillToReport('" + Joiner.on(",").join(groupingHelper.getElementIds()) + "','" + groupingHelper.getGroupName() + "','" + kpiMasterId + "')\"></a></td>");
        }
        if (groupingHelper.getGroupName() != null) {
            if (!atrelementIds.isEmpty() && atrelementIds.contains(groupingHelper.getGroupName())) {
                int index = atrelementIds.indexOf(groupingHelper.getGroupName());
                NumberFormatter nf = new NumberFormatter();
                String value = "";
                int rounding;
                if (round.get(index) == "") {
                    rounding = 0;
                } else {
                    rounding = Integer.parseInt(round.get(index));
                }
                if (groupingHelper.getCalcType().equalsIgnoreCase("sum")) {
                    value = nf.getModifiedNumber(sumBd, numberFormat.get(index), rounding);
                }
                attribute = numberFormat.get(index).toString();
                if (!attribute.equalsIgnoreCase("")) {
                    if (attribute.contains("M")) {
                        value = value.replace("M", "");
                    }
                    if (attribute.contains("K")) {
                        value = value.replace("K", "");
                    }
                    if (attribute.contains("Cr")) {
                        value = value.replace("Cr", "");
                    }
                    if (attribute.contains("L")) {
                        value = value.replace("L", "");
                    }
                } else if (groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                    average = sumBd.divide(new BigDecimal(Count));
                    value = nf.getModifiedNumber(average, numberFormat.get(index), rounding);
                    attribute = numberFormat.get(index).toString();
                    if (!attribute.equalsIgnoreCase("")) {
                        if (attribute.contains("M")) {
                            value = value.replace("M", "");
                        }
                        if (attribute.contains("K")) {
                            value = value.replace("K", "");
                        }
                        if (attribute.contains("Cr")) {
                            value = value.replace("Cr", "");
                        }
                        if (attribute.contains("L")) {
                            value = value.replace("L", "");
                        }
                    }
                }
                if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                    if (drillRepType != null && !("0".equals(drillRepType)) && !drillRepType.equalsIgnoreCase("") && drillRepType.equalsIgnoreCase("D")) {
                        tableBuffer.append("<td style=\"cursor:pointer;text-align:" + alignment.get(index) + ";font-size:" + font.get(index) + "px;\"><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + groupingHelper.getGroupName() + "</strong></A></td>");
                    } else {
                        tableBuffer.append("<td style=\"cursor:pointer;text-align:" + alignment.get(index) + ";font-size:" + font.get(index) + "px;\"><a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "\"><strong>" + groupingHelper.getGroupName() + "</strong></A></td>");
                    }
                } else {
                    tableBuffer.append("<td style=\"cursor:pointer;text-align:" + alignment.get(index) + ";font-size:" + font.get(index) + "px;\"><strong>" + groupingHelper.getGroupName() + "</strong></td>");
                }
                if (groupingHelper.getKpiType() != null) {
                    if (!(groupingHelper.getKpiType().equalsIgnoreCase("Target") || groupingHelper.getKpiType().equalsIgnoreCase("Standard"))) {
                        if (groupingHelper.getCalcType().equalsIgnoreCase("sum")) {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + symbols.get(index) + value + "</a></td>");
                            } else {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\">" + symbols.get(index) + value + "</td>");
                            }
                        } else if (groupingHelper.getCalcType().equalsIgnoreCase("None")) {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                                if (kpiDetail.isCurrentChecked()) {
                                    tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + None + "</a></td>");
                                }
                            } else {
                                if (kpiDetail.isCurrentChecked()) {
                                    tableBuffer.append("<td align=\"center\" padding=\"0px\">" + None + "</td>");
                                }
                            }
                        } else {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + symbols.get(index) + value + "</a></td>");
                            } else {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\">" + symbols.get(index) + value + "</td>");
                            }

                        }
                    } else {
                        if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                                tableBuffer.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + kpiBarChart.toString() + "</a></Td>");
                            } else {
                                tableBuffer.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                            }

                            tableBuffer.append("<Td align=\"center\" padding=\"0px\">" + changeperVal + "</Td>");
                            tableBuffer.append("<Td align=\"right\" ><img src='").append(icon).append("'></img></a></Td>");
                        } else {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                                tableBuffer.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + None + "</a></Td>");
                            } else {
                                tableBuffer.append("<Td align=\"center\" padding=\"0px\">" + None + "</Td>");
                            }

                            tableBuffer.append("<Td align=\"center\" padding=\"0px\">" + None + "</Td>");
                            icon = "icons pinvoke/status-offline.png";
                            tableBuffer.append("<Td align=\"right\" ><img src='").append(icon).append("'></img></a></Td>");
                        }
                    }
                }
            }//code chanege start here.
            else {
                if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {

                    if (drillRepType != null && !("0".equals(drillRepType)) && !drillRepType.equalsIgnoreCase("") && drillRepType.equalsIgnoreCase("D")) {
                        tableBuffer.append("<td style=\"cursor: pointer;\"><a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&drillfromrepId=" + ReportId + "')\"><strong>" + groupingHelper.getGroupName() + "</strong></A></td>");

                    } else {
                        tableBuffer.append("<td style=\"cursor: pointer;\"><a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + drillReportId + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + ReportId + "\"><strong>" + groupingHelper.getGroupName() + "</strong></A></td>");
                    }
                } else {
                    tableBuffer.append("<td style=\"cursor: pointer;\"><strong>" + groupingHelper.getGroupName() + "</strong></td>");
                }
                if (kpiType.equalsIgnoreCase("Standard") || kpiType.equalsIgnoreCase("Target")) {
                    if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                        if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                            tableBuffer.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + kpiBarChart.toString() + "</a></Td>");
                        } else {
                            tableBuffer.append("<Td align=\"center\" padding=\"0px\">" + kpiBarChart.toString() + "</Td>");
                        }

                        tableBuffer.append("<Td align=\"center\" padding=\"0px\">" + changeperVal + "</Td>");
                        tableBuffer.append("<Td align=\"right\" ><img src='").append(icon).append("'></img></a></Td>");
                    } else {
                        if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                            tableBuffer.append("<Td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + None + "</a></Td>");
                        } else {
                            tableBuffer.append("<Td align=\"center\" padding=\"0px\">" + None + "</Td>");
                        }
                        tableBuffer.append("<Td align=\"center\" padding=\"0px\">" + None + "</Td>");
                        tableBuffer.append("<Td align=\"right\" ></a></Td>");
                    }
                } else if (kpiType.equalsIgnoreCase("MultiPeriod")) {
                    if (groupingHelper.getCalcType().equalsIgnoreCase("sum")) {
                        if (kpiDetail.isCurrentChecked()) {
                            if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) + "</a></td>");
                            } else {
                                if (kpiDetail.isCurrentChecked()) {
                                    tableBuffer.append("<td align=\"center\" padding=\"0px\">" + sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) + "</td>");
                                }
                            }
                        }
                    } else if (groupingHelper.getCalcType().equalsIgnoreCase("None")) {
                        if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                            if (kpiDetail.isCurrentChecked()) {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + None + "</a></td>");
                            }
                        } else {
                            if (kpiDetail.isCurrentChecked()) {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\">" + None + "</td>");
                            }
                        }
                    } else {
                        average = sumBd.divide(new BigDecimal(Count));
                        if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                            if (kpiDetail.isCurrentChecked()) {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) + "</a></td>");
                            }
                        } else {
                            if (kpiDetail.isCurrentChecked()) {
                                tableBuffer.append("<td align=\"center\" padding=\"0px\">" + average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) + "</td>");
                            }
                        }

                    }
                } else if (kpiType.equalsIgnoreCase("MultiPeriodCurrentPrior")) {
//           if(groupingHelper.getCalcType().equalsIgnoreCase("sum")) {
//                if(kpiDetail.isCurrentChecked())
//               if(drillReportId != null && !("0".equals(drillReportId))&& !drillReportId.equalsIgnoreCase("")){
//                 tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"')\">"+sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)+"</a></td>");
//              }else{
//                   if(kpiDetail.isCurrentChecked())
//                  tableBuffer.append("<td align=\"center\" padding=\"0px\">"+sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)+"</td>");
//              }
//            }
//            else if(groupingHelper.getCalcType().equalsIgnoreCase("None")){
//            if(drillReportId != null && !("0".equals(drillReportId))&& !drillReportId.equalsIgnoreCase("")){
//                if(kpiDetail.isCurrentChecked())
//                tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"')\">"+None+"</a></td>");
//            }else{
//                if(kpiDetail.isCurrentChecked())
//                tableBuffer.append("<td align=\"center\" padding=\"0px\">"+None+"</td>");
//            }
//          } else
//          {
//              average=sumBd.divide(new BigDecimal(Count));
//              if(drillReportId != null && !("0".equals(drillReportId))&& !drillReportId.equalsIgnoreCase("")){
//                  if(kpiDetail.isCurrentChecked())
//                  tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('"+drillReportId+"')\">"+average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)+"</a></td>");
//              }else{
//                  if(kpiDetail.isCurrentChecked())
//                  tableBuffer.append("<td align=\"center\" padding=\"0px\">"+average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP)+"</td>");
//              }
//
//          }
                } else {
                    if (groupingHelper.getCalcType().equalsIgnoreCase("sum")) {
                        if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                            tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) + "</a></td>");
                        } else {
                            tableBuffer.append("<td align=\"center\" padding=\"0px\">" + sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) + "</td>");
                        }
                    } else if (groupingHelper.getCalcType().equalsIgnoreCase("None")) {
                        if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                            tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + None + "</a></td>");
                        } else {
                            tableBuffer.append("<td align=\"center\" padding=\"0px\">" + None + "</td>");
                        }
                    } else {
                        average = sumBd.divide(new BigDecimal(Count));
                        if (drillReportId != null && !("0".equals(drillReportId)) && !drillReportId.equalsIgnoreCase("")) {
                            tableBuffer.append("<td align=\"center\" padding=\"0px\"><a title=\"Drill To Rep with ViewBy\" onclick=\"getDrillReportViewBys('" + drillReportId + "','" + groupingHelper.getGroupName() + "','" + drillRepType + "')\">" + average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) + "</a></td>");
                        } else {
                            tableBuffer.append("<td align=\"center\" padding=\"0px\">" + average.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) + "</td>");
                        }

                    }
                }
            }               //end of else
//          if(groupingHelper.getKpiType()!=null){
//              if(groupingHelper.getKpiType().equalsIgnoreCase("Target") || groupingHelper.getKpiType().equalsIgnoreCase("Standard")){
//
//              }
//          }
            if (groupingHelper.getKpiType() != null) {
                if (groupingHelper.getKpiType().equalsIgnoreCase("BasicTarget") || groupingHelper.getKpiType().equalsIgnoreCase("Target")) {
                    if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                        tableBuffer.append("<td align=\"center\" style=\"cursor: pointer;\">" + targetValue + "</td>");
                    } else {
                        tableBuffer.append("<td align=\"center\" style=\"cursor: pointer;\">" + None + "</td>");
                    }
                    if (groupingHelper.getKpiType().equalsIgnoreCase("BasicTarget")) {
                        if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                            tableBuffer.append("<td align=\"center\">" + dev + "</td>");
                            tableBuffer.append("<td align=\"center\">" + devper + "</td>");
                        } else {
                            tableBuffer.append("<td align=\"center\">" + None + "</td>");
                            tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        }
                    }
                    if (groupingHelper.getKpiType().equalsIgnoreCase("Target")) {
                        if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                            tableBuffer.append("<td align=\"center\">" + TargetdevPer + "</td>");
                        } else {
                            tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        }
                    }
                } else if (groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriod")) {
//              if(groupingHelper.getCalcType().equalsIgnoreCase("None"))
//              tableBuffer.append("<td align=\"center\">"+None+"</td>");

                    if (kpiDetail.isMTDChecked()) {
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                    }
                    if (kpiDetail.isQTDChecked()) {
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                    }
                    if (kpiDetail.isYTDChecked()) {
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                    }
                } else if (groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior")) {
                    if (kpiDetail.isMTDChecked()) {
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                    }
                    if (kpiDetail.isQTDChecked()) {
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                    }
                    if (kpiDetail.isYTDChecked()) {
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                        tableBuffer.append("<td align=\"center\">" + None + "</td>");
                    }
                } else {
                }
            }
        }
        if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
            if (kpiDetail.isShowInsights() && !(groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriod") || groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior"))) {
                tableBuffer.append("<td align=\"center\"><span class=\"ui-icon ui-icon-search\"></span></td>");
            }
            if (kpiDetail.isShowComments() && !(groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriod") || groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior"))) {
                tableBuffer.append("<td align=\"center\"><a title=\"Click to Add Comment\" onclick=\"javascript:showCommentDialog()\" style=\"text-decoration: none; cursor: pointer;\" href=\"javascript:void(0)\"><font color=\"black\">Add Comment</font></a></td>");
            }
        } else {
            if (kpiDetail.isShowInsights() && !(groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriod") || groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior"))) {
                tableBuffer.append("<td align=\"center\"></td>");
            }
            if (kpiDetail.isShowComments() && !(groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriod") || groupingHelper.getKpiType().equalsIgnoreCase("MultiPeriodCurrentPrior"))) {
                tableBuffer.append("<td align=\"center\"></td>");
            }
        }
        if (kpiType.equalsIgnoreCase("Basic") || kpiType.equalsIgnoreCase("BasicTarget")) {
            if (groupingHelper.getCalcType().equalsIgnoreCase("sum") || groupingHelper.getCalcType().equalsIgnoreCase("avg")) {
                tableBuffer.append("<td align=\"center\" padding=\"0px\"><img onclick=\"viewSchedulerAndTracker()\" style=\"text-decoration: none; cursor: pointer;\" src=\"icons pinvoke/mail.png\"></td></tr>");
            } else {
                tableBuffer.append("<td align=\"center\" padding=\"0px\"></td></tr>");
            }
        }
        if (kpiType.equalsIgnoreCase("Target")) {
            if (kpiDetail.isShowGraphs()) {
                tableBuffer.append("<td align=\"center\"></td>");
            }
        }
        tableBuffer.append("</tr>");
//       return sumBd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        return tableBuffer.toString();
    }

    public String processComplexKpi(String[] kpiName, String[] kpiValue, String[] kpisArray) {
        StringBuilder tableBuilder = new StringBuilder();
        //div header building
        tableBuilder.append("<div class=\"portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all\" style=\"width:100%; height:100% \">");
//        if(!test){
        tableBuilder.append("<div id=\"innerDivKpiId\"  class=\"portlet-header1 navtitle portletHeader ui-corner-all\">");
        tableBuilder.append("<table width=\"100%\"><tr>");
        tableBuilder.append("<td align='left' width=\"70%\"><a title=\"Dashlet Rename\">KPI REGION</td>");
        tableBuilder.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td align='left'><a  class=\" ui-icon ui-icon-plusthick\" title=\"Group KPIs\"></a></td>");
        tableBuilder.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td align='left'><a  title=\"Add Attributes\"><strong>Attributes</strong></a></td>");
        tableBuilder.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon ui-icon-pencil\" title=\"Edit KPIs\" href=\"javascript:void(0)\" ></a> </td>");
        tableBuilder.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a  class=\"ui-icon ui-icon-triangle-2-n-s\" title=\"KPI Drill\"></a></td>");
        tableBuilder.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a  title=\"KPI Rename\">Rename</a></td>");
        // tableBuilder.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><img style=\"width:20px;\"  src='images/clear-icon.png' /></td>");
        // tableBuilder.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a class=\"ui-icon ui-icon-trash\" href=\"javascript:void(0)\"  ></a></td>");
        tableBuilder.append("</tr></table>");
        tableBuilder.append("</div>");
//            }
        //table data buliding
//         if(!test)
        tableBuilder.append("<table id=\"createKPITable\" class=\"tablesorter\"><thead><tr><th><strong></strong></th><th><strong>Complex KPI</strong></th><th><strong>Value</strong></th><th><strong>Comments</strong></th><th><strong></strong></th></tr></thead><tbody>");
//         else
//        tableBuilder.append("<table id=\"createKPITable\" class=\"tablesorter\"><thead><tr><th><strong></strong></th><th><strong>Complex KPI</strong></th><th><strong>Value</strong></th></tr></thead><tbody>");
        for (int i = 0; i < kpisArray.length; i++) {
//            if(!test)
            tableBuilder.append("<tr><td align=\"center\" style=\"cursor: pointer;\">").append("<a title=\"Drill To Report\" class=\" ui-icon ui-icon-gear\"></a>").append("</td><td>").append(kpiName[i]).append("</td><td>").append(kpiValue[i]).append("</td><td>").append("<a><font color=\"black\">Add Comment</font></a>").append("</td><td align=\"center\" padding=\"0px\">").append("<img style=\"text-decoration: none; cursor: pointer;\" src=\"icons pinvoke/mail.png\">").append("</td></tr>");
//            else
//            tableBuilder.append("<tr><td align=\"center\" style=\"cursor: pointer;\">").append("<a title=\"Drill To Report\" class=\" ui-icon ui-icon-gear\"></a>").append("</td><td>").append(kpiName[i]).append("</td><td>").append(kpiValue[i]).append("</td></tr>");
        }
        tableBuilder.append("</tbody></table>");
        tableBuilder.append("</div>");
        return tableBuilder.toString();
    }

    /**
     * @author srikanth.p Builds KpiRegion of KPIWIth Graph
     */
    /**
     * @author srikanth.p Builds KpiRegion of KPIWIth Graph
     */
    public String buildKPIRegion(Container container, String kpiMasterId, String kpidashid, String dashBoardId, pbDashboardCollection dashboardcollect) {
        StringBuilder kpiregion = new StringBuilder();
        DashletDetail detail = dashboardcollect.getDashletDetail(kpidashid);
        if (!kpiMasterId.equalsIgnoreCase("0")) {
            kpiMasterId = detail.getKpiMasterId();
        }
        KPI kpiDetails = (KPI) detail.getReportDetails();
        ArrayListMultimap<String, KPIElement> kpiElementMap = kpiDetails.getKPIElementsMap();
        List<String> elements = kpiDetails.getElementIds();
        List<KPIElement> KpiElements = kpiDetails.getKPIElements();
//    String BizRoles=getbusinessroles(dashBoardId);
        String folderDetails = dashboardcollect.reportBizRoles[0];
        String dashId = detail.getDashBoardDetailId();
        List<DashletDetail> dashletDetails = dashboardcollect.dashletDetails;
        StringBuilder kpiElementIdsString = new StringBuilder();
        StringBuilder kpiElementsname = new StringBuilder();
        for (String elementid : elements) {
            kpiElementIdsString.append(",").append(elementid);
            List<KPIElement> kpiElems = kpiElementMap.get(elementid);

            if (kpiElems != null) {
                for (KPIElement elem : kpiElems) {
                    if (elem.getElementName() != null) {
                        if (elem.getElementId().equalsIgnoreCase(elementid)) {
                            kpiElementsname.append(elem.getElementName()).append(",");
                        }
                    }
                }
            }

        }
        if (kpiElementIdsString.length() > 0) {
            kpiElementIdsString.replace(0, 1, "");
        }
        PbReturnObject kpiRetObj = null;
        kpiRetObj = container.getKpiRetObj();
        kpiregion.append("<div id=\"Dashlets-" + kpidashid + "-kpi\" style=\"width:100%; overflow:no\"");
        kpiregion.append("<div class='portlet-header1 navtitle portletHeader ui-corner-all' align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='left'>KPI Region</font>");
        kpiregion.append("<table align='right' valign='top'><tr>");
        kpiregion.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon ui-icon-arrowthick-2-n-s\" title=\"ReOrder KPIs\" href=\"javascript:void(0)\" onclick=\"addAllKpis(" + dashBoardId + "," + kpidashid + "," + kpiMasterId + ",'" + kpiElementIdsString + "','" + kpiElementsname + "','" + detail.getKpiType() + "')\" ></a> </td>");
        kpiregion.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td id=\"attributes\" align='left'><a  title=\"Add Attributes\" onclick=\"addAttributes('").append(dashBoardId).append("','").append(kpidashid).append("','").append(kpiMasterId).append("','").append(kpiElementIdsString).append("','").append(kpiElementsname).append("','" + detail.getDisplayType() + "')\"><strong>Attributes</strong></a></td>");
        kpiregion.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon ui-icon-pencil\" title=\"Edit KPIs\" href=\"javascript:void(0)\" onclick=\"addMoreKpis(" + dashBoardId + "," + kpidashid + "," + kpiMasterId + "," + folderDetails + ",'" + detail.getKpiType() + "')\" ></a> </td>");
        kpiregion.append("</tr></table></div>");

        if (kpiRetObj != null) {
            kpiregion.append("<table class='tablesorter'>");
            kpiregion.append("<thead><th align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='center'>KPI</font></th>");
            kpiregion.append("<th align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='center'>Current Value</font></th>");
            kpiregion.append("<th align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='center'>View Graph</font></th>");
            kpiregion.append("<tbody>");
            String currVal = "";
            String id = "";
            for (KPIElement kpielem : KpiElements) {
                if (kpielem.getRefElementType().equalsIgnoreCase("1") && elements.contains(kpielem.getElementId())) {
                    id = kpielem.getElementId();
                    kpiregion.append("<tr><td align='center'><a href='javascript:buildGraphOnKpi(\"" + id + "\",\"" + dashId + "\")' title='View Graph On This Element'><strong>").append(kpielem.getElementName()).append("</strong></a></td>");
                    currVal = NumberFormatter.getModifiedNumber(Double.parseDouble(kpiRetObj.getFieldValueString(0, "A_" + id)), "", -1);
                    kpiregion.append("<td align='center'><strong>").append(currVal).append("</strong></td>");
                    kpiregion.append("<td align='center'><a class='ui-icon ui-icon-image'href='javascript:buildGraphOnKpi(\"" + id + "\",\"" + kpidashid + "\",\"" + dashBoardId + "\",\"" + detail.getGraphId() + "\",\"" + kpiMasterId + "\",\"" + detail.getDisplayType() + "\",\"6\")' title='View Graph On This Element'></a></td></tr>");

                }
            }
            kpiregion.append("</tbody></table>");
            kpiregion.append("</div>");
        }
        return kpiregion.toString();
    }

    /**
     *
     * @param kpiRegion
     * @param graphRegion
     * @param dashletId
     * @return totalRegion Built by combining the kpiRegion And GraphRegion
     */
    public String buildKpiWithGraph(String kpiRegion, String graphRegion, String dashletId, DashletDetail dashlet, int height, String dashBoardId) {

        StringBuilder totalRegion = new StringBuilder();
        totalRegion.append("<table width='100%' border='0'>");
        totalRegion.append("<tr valign='top'>");
        totalRegion.append("<td width='40%'>");
        //totalRegion.append("<table width='100%' align='top'><tr>");

        totalRegion.append(kpiRegion);
        totalRegion.append("</td>");
        totalRegion.append("<td width='60%'>");
        if (dashlet.getJqplotgrapprop() != null) {
            JqplotGraphProperty prop = dashlet.getJqplotgrapprop();
            totalRegion.append("<div id=\"Dashlets-" + dashletId + "-graph\"><iframe name=\"graphdisplay-" + dashletId + "\" id=\"graphdisplay-" + dashletId + "\" width=\"100%\" height=\"'" + height + "'px\" frameborder=\"0\" style=\"overflow-x: hidden;overflow-y: hidden;\" src=\"PbDbrdJQplot.jsp?graphType=" + prop.getGraphTypename() + "&graphId=" + prop.getGraphTypeId() + "&dashboardId=" + dashBoardId + "&dashletId=" + dashletId + "&height=" + height + "px\" ></frame>");
//       totalRegion.append("buildDBJqplot('" + + "','" +  + "','" +  + "','" +  + "','"++"','KpiWithGraph');");
            totalRegion.append(" </div>");
        } else {
            totalRegion.append(graphRegion);
        }

//       totalRegion.append("<div class='ui-accordion-header ui-helper-reset ui-state-default ui-corner-all' align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='right'>Graph Region</font></div></td>");
//       totalRegion.append("</tr><tr><td colspan='2'><table width='100%' class='tablesorter' align='top'><tr>");
//
//       totalRegion.append("<td width='40%'><div id='dashlets-").append(dashletId).append("-kpi' style='overflow: auto;'>");
//       totalRegion.append(kpiRegion).append("</div></td>");
//       totalRegion.append("<td width='60%'><div id='dashlets-").append(dashletId).append("-graph' style='overflow: no;'>");
//       totalRegion.append(graphRegion).append("</div></td></tr></table>");
        totalRegion.append("</td>");
        totalRegion.append("</tr></table>");
        return totalRegion.toString();
    }

    public String buildKpiRegionForViewer(Container container, String dashletId, String userId, String dashbordId) {
        StringBuilder result = new StringBuilder();
        String kpiRegion = "";
//       String sqlSqery="SELECT KPI_ID,GRAPH_ID,GRAPH_TYPE,VIEWBY,ELEMENT_IDS,GRAPH_MEASURE,SEQUENCE_NUM FROM PRG_AR_KPIWITHGRAPH WHERE DASHLET_ID="+dashletId+" ORDER BY SEQUENCE_NUM";//
//       PbDb pbdb=new PbDb();
//       PbReturnObject elementsRetobj=null;
        List<String> elementList = new ArrayList<String>();
//       String elements="";

        DashboardViewerDAO dao = new DashboardViewerDAO();
        PbDashboardViewerBD viewerBd = new PbDashboardViewerBD();
        try {

//            elementsRetobj=pbdb.execSelectSQL(sqlSqery);
//            if(elementsRetobj !=null && elementsRetobj.rowCount>0){
//                String kpiMasterId=elementsRetobj.getFieldValueString(0,0);
//                String graphId=elementsRetobj.getFieldValueString(0,1);
//                String dispType=elementsRetobj.getFieldValueString(0, 0);
//                String grpahType=elementsRetobj.getFieldValueString(0,2);
//                String viewBy=elementsRetobj.getFieldValueString(0,3);
//                String graphMeasure=elementsRetobj.getFieldValueString(0,5);
//                for(int i=0;i<elementsRetobj.rowCount;i++)
//                {
//                    elementList.add(elementsRetobj.getFieldValueString(i, 4));
//                }
//                String[] elemArray=elements.split(",");
//                for(int j=0;j<elemArray.length;j++)
//                {
//                    elementList.add(elemArray[j]);
//                }
            Container kpiContainer = new Container();
//                
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            pbDashboardCollection kpiCollect = (pbDashboardCollection) container.getReportCollect().clone();
            DashletDetail dashlet = collect.getDashletDetail(dashletId);
            List<DashletDetail> dashlets = new ArrayList<DashletDetail>();
            DashletDetail kpiDashlet = new DashletDetail();
            String folderDetails = collect.reportBizRoles[0];
            //kpiContainer.setReportCollect(kpiCollect);
            KPI kpiDetail = dashlet.getKpiDetails();
            elementList = kpiDetail.getElementIds();
            String kpiMasterId = dashlet.getKpiMasterId();
            List<KPIElement> kpiElements = kpiDetail.getKPIElements();
            String dispType = dashlet.getDisplayType();
            String graphId = dashlet.getAssignedGraphId();
            String graphType = dashlet.getAssignedGraphType();
//                List<KPIElement> kpiElements = dao.getKPIElements(elementList,new HashMap<String, String>());
//                kpiDetail.setElementIds(elementList);
//                for(KPIElement kpiElem:kpiElements)
//                {
//                    kpiDetail.addKPIElement(kpiElem.getRefElementId(), kpiElem);
//                }
//                kpiDashlet.setReportDetails(kpiDetail);
//                dashlet.setKpiDetails(kpiDetail, dashlet.getDisplayType());
            StringBuilder kpiElementIdsString = new StringBuilder();
            StringBuilder kpiElementsname = new StringBuilder();
            ArrayListMultimap<String, KPIElement> kpiElementMap = kpiDetail.getKPIElementsMap();
            for (String elementid : elementList) {
                kpiElementIdsString.append(",").append(elementid);
                List<KPIElement> kpiElems = kpiElementMap.get(elementid);

                if (kpiElems != null) {
                    for (KPIElement elem : kpiElems) {
                        if (elem.getElementName() != null) {
                            if (elem.getElementId().equalsIgnoreCase(elementid)) {
                                kpiElementsname.append(elem.getElementName()).append(",");
                            }
                        }
                    }
                }

            }

            if (kpiElementIdsString.length() > 0) {
                kpiElementIdsString.replace(0, 1, "");
            }
//        if(kpiElementsname.length()>0){
//            kpiElementsname.replace(0, 1, "");
//        }

            dashlets.add(kpiDashlet);
            kpiCollect.dashletDetails = dashlets;
            PbReturnObject kpiRetObj = container.getKpiRetObj();
            if (kpiRetObj == null) {
                kpiRetObj = viewerBd.getDashboardKPIData(container, kpiCollect, userId);
                container.setKpiRetObj(kpiRetObj);
            }
            List<KPISingleGroupHelper> singleGroupHelpers = dashlet.getSingleGroupHelpers();

            if (!singleGroupHelpers.isEmpty()) {
                atrelementIds = singleGroupHelpers.get(0).getAtrelementIds();
                symbols = singleGroupHelpers.get(0).getSymbols();
                alignment = singleGroupHelpers.get(0).getAlignment();
                font = singleGroupHelpers.get(0).getFont();
                numberFormat = singleGroupHelpers.get(0).getNumberFormat();
                round = singleGroupHelpers.get(0).getRound();
                checkformat = singleGroupHelpers.get(0).getcheckformat();
                checkhead = singleGroupHelpers.get(0).getcheckhead();
                numberFormat1 = singleGroupHelpers.get(0).getgblFormatList();
                selectrepids = singleGroupHelpers.get(0).getselectrepIds();
                background = singleGroupHelpers.get(0).getBackGround();

            } else {
                atrelementIds.clear();
                symbols.clear();
                alignment.clear();
                font.clear();
                numberFormat.clear();
                round.clear();
                selectrepids.clear();
                background.clear();
            }

            String measureColor = "black";
            result.append("<div id=\"Dashlets-" + dashletId + "-kpi\" style=\"width:100%; overflow:no\">");
            result.append("<div class='portlet-header1 navtitle portletHeader ui-corner-all' align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='left'>KPI Region</font>");
            result.append("<table align='right' valign='top'><tr>");
            result.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon ui-icon-arrowthick-2-n-s\" title=\"ReOrder KPIs\" href=\"javascript:void(0)\" onclick=\"addAllKpis(" + dashbordId + "," + dashletId + "," + kpiMasterId + ",'" + kpiElementIdsString + "','" + kpiElementsname + "','" + dashlet.getDisplayType() + "')\" ></a> </td>");
            result.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td id=\"attributes\" align='left'><a  title=\"Add Attributes\" onclick=\"addAttributes('").append(dashbordId).append("','").append(dashletId).append("','").append(kpiMasterId).append("','").append(kpiElementIdsString).append("','").append(kpiElementsname).append("','" + dashlet.getDisplayType() + "')\"><strong>Attributes</strong></a></td>");
            result.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a  class=\"ui-icon ui-icon-triangle-2-n-s\" title=\"KPI Drill\" onclick=\"kpiWithGraphDrilldown('" + dashbordId + "','" + dashletId + "','" + kpiMasterId + "','" + folderDetails + "')\"></a></td>");//DrillToReportDialoge
            result.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign=\"top\"><a class=\"ui-icon ui-icon-pencil\" title=\"Edit KPIs\" href=\"javascript:void(0)\" onclick=\"addMoreKpis(" + dashbordId + "," + dashletId + "," + kpiMasterId + "," + folderDetails + ",'" + dashlet.getDisplayType() + "')\" ></a> </td>");
            result.append("</tr></table></div>");
            if (kpiRetObj != null) {
//                   result.append("<div align='left'>");
                result.append("<table class='tablesorter'>");
                result.append("<thead><th align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='center'>KPI</font></th>");
                result.append("<th align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='center'>Current Value</font></th>");
                result.append("<th align='center'><font size='1px' face='verdana' style='font-weight: bold;' align='center'>View Graph</font></th>");
                result.append("<tbody>");
                BigDecimal currVal = null;
//                   String params="'" + dashletId + "','" + dashBoardId + "','" + refReportId + "','" + graphId + "','" + kpiMasterId + "','" + dispSequence + "','" + dispType + "','" + detail.getDashletName() + "','" + contextPath + "','" + fromDesigner + "'";
                String id = "";
                NumberFormatter nf = new NumberFormatter();
                String formattedvalue = "";
                String align = "left";
                String numFormat = "";
                int precision = -1;
                for (String elem : elementList) {
                    for (KPIElement kpielem : kpiElements) {
                        if (kpielem.getRefElementType().equalsIgnoreCase("1") && elem.equalsIgnoreCase(kpielem.getElementId())) {
                            id = kpielem.getElementId();
//                           measureColor="black";
                            int index = atrelementIds.indexOf(id);
                            if (index == -1) {
                                align = "left";

                            } else {
                                align = alignment.get(index);
                                numFormat = numberFormat.get(index);
                                precision = Integer.parseInt(round.get(index));

                            }

                            result.append("<tr><td align=\"" + align + "\">");
                            if (kpiDetail.getKPIDrillRepType(elem) != null && kpiDetail.getKPIDrillRepType(elem).equalsIgnoreCase("D")) {
                                result.append("<a style='decoration:none' href=\"javascript:submitDbrdUrl('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + kpiDetail.getKPIDrill(elem) + "')\">");
                            } else if (kpiDetail.getKPIDrillRepType(elem) != null && kpiDetail.getKPIDrillRepType(elem).equalsIgnoreCase("R")) {
                                result.append("<a style='decoration:none' href=\"reportViewer.do?reportBy=viewReport&REPORTID=" + kpiDetail.getKPIDrill(elem) + "&drillViewCheck=true" + "&action=reset" + "&drillfromrepId=" + dashbordId + "\">");
                            }
                            result.append("<strong>").append(kpielem.getElementName()).append("</strong>");
                            if (kpiDetail.getKPIDrillRepType(elem) != null) {
                                result.append("</a></td>");
                            } else {
                                result.append("</td>");
                            }

                            currVal = BigDecimal.valueOf(Double.valueOf(kpiRetObj.getFieldValueString(0, "A_" + id)));
                            formattedvalue = nf.getModifiedNumber(currVal, numFormat, precision);
                            result.append("<td align='center'><strong >").append(formattedvalue).append("</strong></td>");
                            result.append("<td align='center'><a class='ui-icon ui-icon-image'href='javascript:buildGraphOnKpi(\"" + id + "\",\"" + dashletId + "\",\"" + dashbordId + "\",\"" + graphId + "\",\"" + kpiMasterId + "\",\"" + dispType + "\",\"" + graphType + "\")' title='View Graph On This Element'></a></td></tr>");
                        }
                    }
                }
                result.append("</tbody></table></div>");
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return result.toString();
    }

    public String processComplexKpiForOneview(String[] kpiName, String[] kpiValue, String[] kpisArray, OneViewLetDetails oneviewlet, String valu, String val1, CreateKPIFromReport kPIFromReport, String viewLetId) {
        StringBuilder tableBuilder = new StringBuilder();

        tableBuilder.append("<table id=\"createKPITable\"><tbody>");

        for (int i = 0; i < kpisArray.length; i++) {
            tableBuilder.append("<tr><td><span style='color:#369;font-size:18pt;'>").append("<a onclick=\"getComplexKPIGraphinoneview('" + oneviewlet.getRepId() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "','" + viewLetId + "')\" href='javascript:void(0)' >").append(kpiValue[i]).append("</span></td></tr>");
            // tableBuilder.append("<tr><td><span style='color:#369;font-size:18pt;'>").append(kpiValue[i]).append("</span></td></tr>");
        }
        tableBuilder.append("</tbody></table>");
        return tableBuilder.toString();
    }

    public String processComplexKpiForOneviewData(String[] kpiName, String[] kpiValue, String[] kpisArray, OneViewLetDetails oneviewlet, String valu, String val1, CreateKPIFromReport kPIFromReport, String viewLetId, List tiemdetails) {
        StringBuilder finalStringVal = new StringBuilder();
        ArrayList assignedGraphIds = oneviewlet.getAssignedGraphIds();
        ArrayList assignedGraphNames = oneviewlet.getAssignedGraphNames();
        String measurecolor = "#369";
        String formatType = "";
        String formatValue = "";
        if (oneviewlet.getMeasureColor() != null && !oneviewlet.getMeasureColor().isEmpty()) {
            measurecolor = oneviewlet.getMeasureColor();
        }
        // finalStringVal.append("<td id='" + oneviewlet.getNoOfViewLets() + "' width='" + oneviewlet.getWidth() + "px' style='height:" + oneviewlet.getHeight() + "px;' rowspan='" + oneviewlet.getRowSpan() + "' colspan='" + oneviewlet.getColSpan() + "'>");
        if (val1 == null) {
            finalStringVal.append(new SchedulerDAO().getOneviewComplexKpiHeader(oneviewlet, valu, kpiValue[0]));
        }
        //
//            if(!kpiValue[0].contains("--")){
//            BigDecimal value = new BigDecimal(kpiValue[0].replace(",", "").replace("--", ""));
//            if(oneviewlet.getFormatVal() != null && oneviewlet.getRoundVal() != null){
//                kpiValue[0] = NumberFormatter.getModifiedNumber(value, oneviewlet.getFormatVal(),Integer.parseInt(oneviewlet.getRoundVal()));
//            }else{
//                kpiValue[0] = NumberFormatter.getModifiedNumber(value, "",0);
//            }
//            }
        String prefixValue = "";
        String suffixValue = "";
        if (oneviewlet.getPrefixValue() != null) {
            prefixValue = oneviewlet.getPrefixValue();
        }
        if (oneviewlet.getSuffixValue() != null) {
            suffixValue = oneviewlet.getSuffixValue();
        }
        if (oneviewlet.getSuffixValue() != null) {
            if (oneviewlet.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("K")) {
            formatType = "K";
            formatValue = "K";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("M")) {
            formatType = "M";
            formatValue = "Mn";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("L")) {
            formatType = "L";
            formatValue = "Lkh";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
            formatType = "Cr";
            formatValue = "Crs";
        }
        finalStringVal.append("<div id='Dashlets-" + oneviewlet.getNoOfViewLets() + "' class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='border-bottom: medium hidden LightGrey ; border-left: medium hidden LightGrey ; border-right: medium hidden LightGrey ; border-color: LightGrey ; width: 100%; height: 100%; margin-left: 10px; margin-right: 10px;overflow:auto;'>");
        //StringBuilder tableBuilder=new StringBuilder();

        finalStringVal.append("<table id=\"createKPITable\"><tbody>");

        for (int i = 0; i < kpisArray.length; i++) {
            finalStringVal.append("<tr><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><span style='color:" + measurecolor + ";font-size:18pt;'>").append("<a onclick=\"getComplexKPIGraphinoneview('" + oneviewlet.getRepId() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "','" + viewLetId + "','" + oneviewlet.getRepName() + "')\" href='javascript:void(0)' >").append(prefixValue + kpiValue[i].replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span>").append("</span></td><td colspan='1' align='right'></td></tr>");
            //finalStringVal.append("<tr><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><span style='color:"+measurecolor+";font-size:18pt;'>").append(kpiValue[i].replace(formatType, "")+ " <sub>" + formatValue + "</sub>").append("</span></td><td colspan='1' align='right'></td></tr>");
        }
        finalStringVal.append("<tr height='15'><td><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
        if (assignedGraphIds != null) {
            String tdWidth = "0";
            for (int i = 0; i < assignedGraphIds.size(); i++) {
                if (i > 0) {
                    tdWidth = "1%";
                }
                finalStringVal.append("<td align='right' width='1%' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
            }
        }
        //finalStringVal.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','','','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() + "px' width='" + oneviewlet.getWidth() + "px' align='left'></td></tr>");
        finalStringVal.append("</tr></td></tr></tbody></table>");
        return finalStringVal.toString();
    }

    public BigDecimal getComputeFormulaTargetVal(String temp, String tempFormula, String mysqlString) {
        PbDb pbdb = new PbDb();
        String formula = "";
        BigDecimal gtvalue = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            tempFormula = "SELECT " + tempFormula;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            mysqlString = mysqlString.substring(1);
            tempFormula = "SELECT " + tempFormula + " FROM (SELECT " + mysqlString + ") A";
        } else {
            tempFormula = "SELECT " + tempFormula + " FROM DUAL";
        }
        PbReturnObject retobj2 = null;
        try {
            if (tempFormula.contains("CURRENT") || tempFormula.contains("PRIOR")) {
                gtvalue = retobj2.getColumnGrandTotalValue(temp);
                return gtvalue;
            }
            retobj2 = pbdb.execSelectSQL(tempFormula);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retobj2 != null && retobj2.getRowCount() > 0) {
            formula = retobj2.getFieldValueString(0, 0);
            if (formula.equalsIgnoreCase("")) {
                formula = "0";
            }
            BigDecimal gTotalVal = new BigDecimal(formula);
            gTotalVal = gTotalVal.setScale(2, RoundingMode.CEILING);
            return gTotalVal;
        } else {
            gtvalue = retobj2.getColumnGrandTotalValue(temp);
            return gtvalue;
        }

    }
//added by sruthi for grand total

    public String getGrandTotalValue(String temp, int index, PbReturnObject retObjQry, String targetid, HashMap<String, String> aggid, String userColType, String columntype) {
        ArrayList<String> list1 = new ArrayList<String>();
        PbDb pbdb = new PbDb();
        NumberFormatter gtnf = new NumberFormatter();
        BigDecimal gtpriorVal = null;
        String aggtype = null;
        boolean isRunTime = false;
        if (temp.contains("_percentwise") || temp.contains("_rank") || temp.contains("_wf") || temp.contains("_wtrg") || temp.contains("_rt") || temp.contains("_pwst") || temp.contains("_excel") || temp.contains("_excel_target") || temp.contains("_deviation_mean") || temp.contains("_gl") || temp.contains("_userGl") || temp.contains("_timeBased") || temp.contains("_changedPer") || temp.contains("_glPer")) {
            temp = temp.replace("_percentwise", "").replace("_rank", "").replace("_wf", "").replace("_wtrg", "").replace("_rt", "").replace("_pwst", "").replace("_excel", "").replace("_excel_target", "").replace("_deviation_mean", "").replace("_gl", "").replace("_userGl", "").replace("_timeBased", "").replace("_changedPer", "").replace("_glPer", "");
            isRunTime = true;
        }

        BigDecimal dividedval = null;
        int gtrounding;
        if (round.get(index) == "") {
            gtrounding = 0;
        } else {
            gtrounding = Integer.parseInt(round.get(index));
        }
        int count = 0;
        if (columntype.equalsIgnoreCase("Ly")) {
            list1 = (ArrayList) dataMap.get(targetid);
            gtpriorVal = retObjQry.getColumnGrandTotalValue(temp);
// gtnf.getModifidNumber(gtpriorVal);
            aggtype = aggid.get(targetid.replace("A_", ""));
        }
        if (columntype.equalsIgnoreCase("BUD")) {
            String query = "Select aggregation_type from prg_user_all_info_details where element_id=" + temp;
            try {
                PbReturnObject ro = pbdb.executeSelectSQL(query);
                if (ro.rowCount > 0) {
                    aggtype = ro.getFieldValueString(0, 0);
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            list1 = (ArrayList) budgetlist.get(targetid);
            gtpriorVal = retObjQry.getColumnGrandTotalValue("A_" + temp);
        }
        if (columntype.equalsIgnoreCase("TY")) {
            list1 = (ArrayList) currlist.get(targetid);
            gtpriorVal = retObjQry.getColumnGrandTotalValue(targetid);
            aggtype = aggid.get(targetid.replace("A_", ""));
        }
        int valuesize = list1.size();
        for (int g = 0; g < list1.size(); g++) {
            if (list1.get(g).equalsIgnoreCase("0") || list1.get(g).equalsIgnoreCase("0.0") || list1.get(g).equalsIgnoreCase("0.00")) {
                count++;
            }
        }
        if (count > 0) {
            valuesize = list1.size() - count;
        }
        if (aggtype != null) {
            if (aggtype.equalsIgnoreCase("avg")) {
                if (userColType.equalsIgnoreCase("SUMMARIZED") || userColType.equalsIgnoreCase("TIMECALUCULATED") || isRunTime) {
                    gtpriorVal = retObjQry.getColumnGrandTotalValue(temp);
                    if (valuesize != 0) {
                        gtpriorVal = gtpriorVal.divide(new BigDecimal(valuesize), 2, RoundingMode.CEILING);
                    }
                }
            }
        }
        if (columntype.equalsIgnoreCase("Ly")) {
            priorvalue = gtpriorVal;
        }
        if (columntype.equalsIgnoreCase("BUD")) {
            targetvalue = gtpriorVal;
        }
        if (columntype.equalsIgnoreCase("TY")) {
            currentvalue = gtpriorVal;
        }

        String gtformattedvalue = gtnf.getModifiedNumberDashboard(gtpriorVal, numberFormat.get(index), gtrounding);
        return gtformattedvalue;
    }

    public String getGrandTotalData(int index, String columntype) {
        int gtrounding;
        String gtvarBudgevalue = null;
        NumberFormatter gtnf = new NumberFormatter();
        if (round.get(index) == "") {
            gtrounding = 0;
        } else {
            gtrounding = Integer.parseInt(round.get(index));
        }
        BigDecimal gttarget = null;
        BigDecimal zeroval = new BigDecimal("0.0");
        BigDecimal varBudge1 = null;
        if (columntype.equalsIgnoreCase("Var-Bud")) {
            if (targetvalue == null) {
                targetvalue = zeroval;
            }
            if (currentvalue == null) {
                currentvalue = zeroval;
            }
//if(!targetvalue.equals(zeroval)){
            varBudge1 = currentvalue.subtract(targetvalue);
            gtvarBudgevalue = gtnf.getModifiedNumberDashboard(varBudge1, numberFormat.get(index), gtrounding);//changed by sruthi for indian rs
            // }else{
            // gtvarBudgevalue=gtnf.getModifiedNumberDashboard(currentvalue,numberFormat.get(index) ,gtrounding);
            //}
        }
        if (columntype.equalsIgnoreCase("Var-Bud%")) {
            BigDecimal multy = new BigDecimal("100");
            BigDecimal gtcurr = null;
            BigDecimal divisionvar = null;
            BigDecimal gttotaltarget = null;
            if (targetvalue == null) {
                targetvalue = zeroval;
            }
            if (currentvalue == null) {
                currentvalue = zeroval;
            }
            varBudge1 = currentvalue.subtract(targetvalue);
            if (!targetvalue.equals(new BigDecimal("0")) && !targetvalue.equals(new BigDecimal("0.0")) && !targetvalue.equals(new BigDecimal("0.00"))) {
                divisionvar = varBudge1.divide(targetvalue, 2, RoundingMode.CEILING);
                gttotaltarget = divisionvar.multiply(multy);
                gtvarBudgevalue = gtnf.getModifiedNumberDashboard(gttotaltarget, "", gtrounding);
            } else {
                gttotaltarget = varBudge1.multiply(multy);
                gtvarBudgevalue = gtnf.getModifiedNumberDashboard(gttotaltarget, "", gtrounding);
            }
        }
        if (columntype.equalsIgnoreCase("Var-Ly")) {
            BigDecimal gtvarLastyear = null;
            if (priorvalue == null) {
                priorvalue = zeroval;
            }
            if (currentvalue == null) {
                currentvalue = zeroval;
            }
            gtvarLastyear = currentvalue.subtract(priorvalue);
            gtvarBudgevalue = gtnf.getModifiedNumberDashboard(gtvarLastyear, numberFormat.get(index), gtrounding);

        }
        if (columntype.equalsIgnoreCase("Var-Ly%")) {
            BigDecimal gtpriorVal = null;
            BigDecimal gtcurrVal = null;
            BigDecimal gtvarLastyear = null;
            BigDecimal gtchangePercVal = null;
            BigDecimal gtactualval = null;
            BigDecimal multiyVal = new BigDecimal("100");
            if (priorvalue == null) {
                priorvalue = zeroval;
            }
            if (currentvalue == null) {
                currentvalue = zeroval;
            }
            gtvarLastyear = currentvalue.subtract(priorvalue);
            if (!priorvalue.equals(new BigDecimal("0")) && !priorvalue.equals(new BigDecimal("0.0")) && !priorvalue.equals(new BigDecimal("0.00"))) {
                gtchangePercVal = gtvarLastyear.divide(priorvalue, 2, RoundingMode.CEILING);
                gtactualval = gtchangePercVal.multiply(multiyVal);
            } else {
                gtactualval = gtvarLastyear.multiply(multiyVal);

            }

            gtvarBudgevalue = gtnf.getModifiedNumberDashboard(gtactualval, "", gtrounding);
        }
        return gtvarBudgevalue;
    }
//ended by sruthi
}
