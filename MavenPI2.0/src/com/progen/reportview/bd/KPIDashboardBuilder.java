/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.bd;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.report.PbReportCollection;
import com.progen.report.data.*;
import com.progen.userlayer.db.UserLayerDAO;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
class KPIDashboardBuilder {

    public static Logger logger = Logger.getLogger(KPIDashboardBuilder.class);
    Gson gson = new Gson();
    String reportid;
    String contxtpath;
    String Ref_element;
    String fontsizego = "8";
    String textaligngo = "center";
    String fontweightgo = "normal";
    ArrayList<String> DisplayColumnskpi = new ArrayList<String>();
    ArrayList<Integer> ViewSequence = new ArrayList<Integer>();
    HashMap<Integer, BigDecimal> subTotalForkpidb = new HashMap<Integer, BigDecimal>();
    HashMap<Integer, String> subTotalForkpidb1 = new HashMap<Integer, String>();
    HashMap<Integer, ArrayList<String>> MeasureDataMap = new HashMap<Integer, ArrayList<String>>();
    //HashMap<Integer, ArrayList<BigDecimal>> RowSubTotalValuesMap = new HashMap<Integer, ArrayList<BigDecimal>>();
    HashMap<Integer, ArrayList<String>> RowSubTotalValuesMap = new HashMap<Integer, ArrayList<String>>();
    HashMap<Integer, ArrayList<String>> RowSequenceMap = new HashMap<Integer, ArrayList<String>>();
//bhargavi
    static final String KPI_ROUNDING = "parent.kpirounding";
    static final String NUMBER_FORMAT = "parent.numberformat";
    static final String ROWADD_ABOVE_BELOW = "parent.rowaddkpi";
    //static final String UPDATEGT_TYPE = "parent.updategttype";
    static final String UPDATEGT_TYPE1 = "parent.updategttype1";
    static final String SORT_FUNCTION_KPI = "parent.sortkpi";
    static final String COMPARISON = "parent.enableComparision";
    static final String EDIT_FORMAT = "parent.editformat";
    static final String ROW_COLOR = "parent.rowcoloring";
    static final String RESET_ROWCOLOR = "parent.resetrowcolor";
    public DataFacade facade;
    public Container container;
    ArrayList<BigDecimal> subTotalForSTList = new ArrayList<BigDecimal>();
    BigDecimal subTotalForST = BigDecimal.ZERO;
    BigDecimal rowSubTotalForGT = BigDecimal.ZERO;

    public StringBuilder DashboardHTML(Container container) throws Exception {
        StringBuilder subMenuHtml = new StringBuilder();
        StringBuilder kpiheads = new StringBuilder();
        int CloumnCount = 10;
        String str = "";
        TableBuilder tableBldr;
        TableDataRow rowkpi = new TableDataRow();
        this.container = container;

        PbReturnObject DataObject = (PbReturnObject) container.getRetObj();
        Menu[] tableMenu = null;
        this.facade = new DataFacade(container);

        ArrayList<String> DisplayColumns = container.getDisplayColumns();
        String[] DisplayColumnsfortimeDB = container.DisplayColsfortimeDB;
        ArrayList<String> DisplayLabels = container.getDisplayLabels();
        String[] columnTypes = null;


        int rowSpan;
        int colSpan = 0;

        columnTypes = DataObject.getColumnTypes();
        reportid = container.getReportCollect().reportId;
        contxtpath = container.getContextPath();
//PbReportCollection reportCollect = container.getReportCollect();
//    ArrayList<String> timeDetailsArray = reportCollect.timeDetailsArray;
//        String date = timeDetailsArray.get(2);
//           String query=" SELECT cm_cust_name,pm_cust_name,pym_cust_name,cq_cust_name,lq_cust_name,lyq_cust_name,cy_cust_name,ly_cust_name from pr_day_denom  WHERE ddate = str_to_date('"+date+" ','%m/%d/%Y')";
// Connection connection = null;
//        try {
//            connection = ProgenConnection.getInstance().getConnectionForElement(reportCollect.reportQryElementIds.get(0));
//        } catch (Exception e) {
//            logger.error("Exception: ", e);
//        }
//          PbReturnObject pbro = new PbDb().execSelectSQL(query,connection);
//          if(pbro!=null)
//          {
//
//          }

        PbReturnObject pbro = new ProgenReportViewerBD().getAllHeadersName(container.getReportCollect());
        container.cystdate = pbro.getFieldValueDate(0, "CY_ST_DATE");
        int ViewByCount = container.getViewByCount();
        HashMap<String, BigDecimal> allMesChange = container.getallMesChange();
        HashMap<String, String> datetimes = new HashMap<>();
        ArrayList changeColumns = container.getChangeColumns();
        ArrayList<String> allcolumns = container.getAllColumns();
        subMenuHtml.append("<div align=\"left\" style=\"width:99%;height:600px;overflow-y:auto;overflow-x:auto;padding:4px\" id=\"TimeDashDiv\"><table width=\"99%;height:auto\" cellspacing=\"1\" cellpadding=\"1\" class=\"tablesorter\" id=\"tablesorter\"><thead align=\"center\">");
        subMenuHtml.append("<tr><th width=\"10px\"></th>");
        subMenuHtml.append("<th width=\"300px\"><strong>KPI</strong></th>");
//             "MTD,PMTD,CPMTD,C%PMTD,PYMTD,CPYMTD,C%PYMTD,QTD,PQTD,CPQTD,C%PQTD,PYQTD,CPYQTD,C%PYQTD,YTD,PYTD,CPYTD,C%PYTD";
        for (int i = 0; i < allcolumns.size(); i++) {
            String s = allcolumns.get(i);
            switch (s) {
                case "MTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>" + pbro.getFieldValueString(0, "CM_CUST_NAME") + "</strong></th>");
                    String date = pbro.getFieldValueString(0, "CM_END_DATE");
                    date = date.substring(0, 10);
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
//String $monthly = date("Y-m-d", $monthlyDate);
//echo $monthly;
                    datetimes.put(s, date);
                    break;
                }
                case "PMTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>" + pbro.getFieldValueString(0, "PM_CUST_NAME") + "</strong></th>");
                    String date = pbro.getFieldValueString(0, "PM_DAY");
                    date = date.substring(0, 10);
//                               date=getexactdate(container.getReportCollect(),"month");

                    datetimes.put(s, date);
                    break;
                }
                case "CPMTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change MTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "CM_END_DATE");
                    date = date.substring(0, 10);
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
                    datetimes.put(s, date);
                    break;
                }
                case "C%PMTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change% MTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "CM_END_DATE");
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
                    date = date.substring(0, 10);
                    datetimes.put(s, date);
                    break;
                }
                case "PYMTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>" + pbro.getFieldValueString(0, "PYM_CUST_NAME") + "</strong></th>");
                    String date = pbro.getFieldValueString(0, "PYM_DAY");
                    date = date.substring(0, 10);
//                              date=getexactdate(container.getReportCollect(),"year");
                    datetimes.put(s, date);
                    break;
                }
                case "CPYMTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change PYMTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "PYM_DAY");
                    date = date.substring(0, 10);
//                                date=getexactdate(container.getReportCollect(),"year");
                    datetimes.put(s, date);
                    break;
                }
                case "C%PYMTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change% PYMTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "PYM_DAY");
                    date = date.substring(0, 10);
//                               date=getexactdate(container.getReportCollect(),"year");
                    datetimes.put(s, date);
                    break;
                }
                case "QTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>" + pbro.getFieldValueString(0, "CQ_CUST_NAME") + "</strong></th>");
                    String date = pbro.getFieldValueString(0, "CQ_END_DATE");
                    date = date.substring(0, 10);
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
                    datetimes.put(s, date);
                    break;
                }
                case "PQTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>" + pbro.getFieldValueString(0, "LQ_CUST_NAME") + "</strong></th>");
                    String date = pbro.getFieldValueString(0, "LQ_DAY");
                    date = date.substring(0, 10);
                    datetimes.put(s, date);
                    break;
                }
                case "CPQTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change QTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "CQ_END_DATE");
                    date = date.substring(0, 10);
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
                    datetimes.put(s, date);
                    break;
                }
                case "C%PQTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change% QTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "CQ_END_DATE");
                    date = date.substring(0, 10);
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
                    datetimes.put(s, date);
                    break;
                }
                case "PYQTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>" + pbro.getFieldValueString(0, "LYQ_CUST_NAME") + "</strong></th>");
                    String date = pbro.getFieldValueString(0, "LYQ_DAY");
                    date = date.substring(0, 10);
                    datetimes.put(s, date);
                    break;
                }
                case "CPYQTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change PYQTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "LYQ_DAY");
                    date = date.substring(0, 10);
                    datetimes.put(s, date);
                    break;
                }
                case "C%PYQTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change% PYQTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "LYQ_DAY");
                    date = date.substring(0, 10);
                    datetimes.put(s, date);
                    break;
                }
                case "YTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>" + pbro.getFieldValueString(0, "CY_CUST_NAME") + "</strong></th>");
                    String date = pbro.getFieldValueString(0, "CY_END_DATE");
                    date = date.substring(0, 10);
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
                    datetimes.put(s, date);
                    break;
                }
                case "PYTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>" + pbro.getFieldValueString(0, "LY_CUST_NAME") + "</strong></th>");
                    String date = pbro.getFieldValueString(0, "LY_DAY");
                    date = date.substring(0, 10);
//                                date=getexactdate(container.getReportCollect(),"year");
                    datetimes.put(s, date);
                    break;
                }
                case "CPYTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change YTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "CY_END_DATE");
                    date = date.substring(0, 10);
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
                    datetimes.put(s, date);
                    break;
                }
                case "C%PYTD": {
                    subMenuHtml.append("<th width=\"250px\"><strong>Change% YTD</strong></th>");
                    String date = pbro.getFieldValueString(0, "CY_END_DATE");
                    date = date.substring(0, 10);
                    String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
                    date = curentdate.substring(6, 10) + "-" + curentdate.substring(0, 2) + "-" + curentdate.substring(3, 5);
                    datetimes.put(s, date);
                    break;
                }



            }
        }
        subMenuHtml.append("</tr></thead><tfoot></tfoot><tbody>");



        int count = 1;
        int index;
        int changecol = 0;
        int rtcount = 1;
        String ViewByelement = null;
        ArrayList<String> DisplayColumns1 = new ArrayList<>();
        ArrayList<String> DisplayLabels1 = new ArrayList<>();
        for (int MsrsLngth = 0; MsrsLngth < DisplayColumns.size(); MsrsLngth++) {
            if (!columnTypes[MsrsLngth].equalsIgnoreCase("VARCHAR") && !columnTypes[MsrsLngth].equalsIgnoreCase("CHAR")) {
                DisplayColumns1.add(DisplayColumns.get(MsrsLngth));
                DisplayLabels1.add(DisplayLabels.get(MsrsLngth));
            }
        }

        for (int MsrsLngth = 0; MsrsLngth < DisplayColumns1.size(); MsrsLngth++) {


            changecol = 0;
            String fontsize = "8";
            String textalign = "center";
            String textalign1 = "left";
            String fontweight = "normal";
            String color = "";
            String menuName;
            String menuFunction;
            //added by sruthi for column properties
            ViewByelement = DisplayColumns1.get(MsrsLngth);
            tableMenu = getMenu(DisplayColumns1.get(MsrsLngth), DisplayLabels1.get(MsrsLngth), ViewByelement);
            //ended by sruthi
            subMenuHtml.append("<Tr id=\"rowId").append(MsrsLngth).append("\"><td>");
            // subMenuHtml.append("<td class=\"dropDownMenu\" width=\"5px\"><a class=\"ui-icon ui-icon-plusthick\"></a></td>");
            subMenuHtml.append("<ul class=\"dropDownMenu\" style=\"width:5px;\">");
            subMenuHtml.append("<li style=\"width:100%;margin-left:-18px;\"><a href=\"#\" class=\"ui-icon ui-icon-plusthick\" style=\"padding:0px;margin-top:-5px;margin-left: 15px;\"></a>");
            subMenuHtml.append("<ul style=\"width:150px;padding:5px 62px 0 0px;margin-top:-15px;margin-left: 15px;\">");
            //added by sruthi for column properties
            for (Menu menu : tableMenu) {
                menuName = menu.getMenuEntry();
                menuFunction = menu.getMenuFunction();
                subMenuHtml.append("<li><a href=\"javascript:" + menuFunction + "\">" + menuName + "</a>");
                subMenuHtml.append(this.getSubMenu(menu.getSubMenu()));
                subMenuHtml.append("</li>");

            }
            //ended by sruthi
            subMenuHtml.append("</ul>");
            subMenuHtml.append("</li>");
            subMenuHtml.append("</td>");

            subMenuHtml.append("<td height=\"25\"  align=\"left\" style=\"cursor:pointer;background-color:").append(color).append(";font-weight:").append(fontweight).append(";text-align:").append(textalign1).append(";font-size:").append(fontsize).append("pt;  font-weight: bold;\"  class=\"dimensionCell\">").append(DisplayLabels1.get(MsrsLngth)).append("</td>");


//              Double Msrdata=0.0;
            BigDecimal b1;
            String value = "";
            int rounding;
            List cellDataList = new ArrayList();

//                                         if(round.get(MsrsLngth)==""){
//                                              rounding=0;
//                                         }
//                                         else{
//                                             rounding=Integer.parseInt(round.get(MsrsLngth));
//                                         }
            for (int l = 0; l < allcolumns.size(); l++) {
                if (container.getrowcolorForkpiHashMap().containsKey(DisplayColumns1.get(MsrsLngth))) {
                    color = container.getrowcolorForkpi(DisplayColumns1.get(MsrsLngth));
                }
//                       String reportDrillUrl="";
                StringBuilder reportDrillUrl = new StringBuilder();
                Gson gson = new Gson();
                Type tarType = new TypeToken<List<String>>() {
                }.getType();
                String finalrepDrillUrl = facade.container.getContextPath() + "/reportViewer.do?reportBy=viewReport";
                finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(DisplayColumns1.get(MsrsLngth));
                Object cellData = facade.getDimensionData(rtcount, DisplayColumns.get(0));
                Set paramEleIds = container.getReportCollect().reportParameters.keySet();
                Iterator paramEleIter = paramEleIds.iterator();
                String paramElement;
                List<String> parameterValue = new ArrayList();
                ArrayList paramInfo;
                container.getReportCollect().paramValueList.clear();
//        Gson gson=new Gson();
//        Type tarType=new TypeToken<List<String>>() {}.getType();
                String filters = "";
                while (paramEleIter.hasNext()) {
//            excludedParam = false;
                    paramElement = (String) paramEleIter.next();
                    paramInfo = (ArrayList) container.getReportCollect().reportParameters.get(paramElement);
//            parameterValue = (List<String>)container.getReportCollect().reportIncomingParameters.get("CBOARP" + paramElement);
//            if(parameterValue==null){
                    if (paramInfo.get(8) != null && !paramInfo.get(8).equals("")) {
                        parameterValue = (List<String>) paramInfo.get(8);
                    }
                    if (!parameterValue.contains("All")) {
                        reportDrillUrl.append("&CBOARP").append(paramElement.replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(parameterValue, tarType)));
//       reportDrillUrl+="&CBOARP"+paramElement.replace("A_", "") + "="+java.net.URLEncoder.encode(gson.toJson(parameterValue,tarType));
                    }
//            }
                }
                cellDataList.add(cellData);
                String p1value = "";
                if (allcolumns.get(l).contains("%")) {
                    p1value = allcolumns.get(l).replace("%", "p1");
                } else {
                    p1value = allcolumns.get(l);
                }
//reportDrillUrl+="&CBOARP"+DisplayColumns.get(0).replace("A_", "") + "="+java.net.URLEncoder.encode(gson.toJson(cellDataList,tarType));
                finalrepDrillUrl += reportDrillUrl.toString() + "&reportDrill=Y&istimedash=Y&isKpiDashboard=true&timetype=" + p1value;//reportDrill=Y means assigned report is not there in session in that it is useful calling updatecollection  for passing date from assigning report to assigned report


                if (allcolumns.get(l).equalsIgnoreCase("CPMTD") || allcolumns.get(l).equalsIgnoreCase("C%PMTD")
                        || allcolumns.get(l).equalsIgnoreCase("CPYMTD") || allcolumns.get(l).equalsIgnoreCase("C%PYMTD") || allcolumns.get(l).equalsIgnoreCase("CPQTD")
                        || allcolumns.get(l).equalsIgnoreCase("C%PQTD") || allcolumns.get(l).equalsIgnoreCase("CPYQTD") || allcolumns.get(l).equalsIgnoreCase("C%PYQTD")
                        || allcolumns.get(l).equalsIgnoreCase("CPYTD") || allcolumns.get(l).equalsIgnoreCase("C%PYTD")) {
                    b1 = allMesChange.get(DisplayColumns1.get(MsrsLngth) + "_" + changeColumns.get(changecol));
//                    String formatMeasureData = facade.formatMeasureData(b1, MsrsLngth);
                    if (!facade.container.getReportCollect().reportDrillMap.containsKey(DisplayColumns1.get(MsrsLngth))) {
                        subMenuHtml.append("<td id=").append(count).append(" align=\"right\" style=\"text-align:center;background-color:").append(color).append(";font-weight:").append(fontweight).append(";text-align:").append(textalign).append(";font-size:").append(fontsize).append("pt;\" >");
                        subMenuHtml.append("<font color=\"\"><a style=\"text-decoration:none;\"  style=\"color: #CC0000\">").append(b1).append("</a></font></td>");
                    } else {
                        if (!facade.container.getReportDrillMap(DisplayColumns1.get(MsrsLngth)).equalsIgnoreCase("0")) {
                            subMenuHtml.append("<td id=").append(count).append(" align=\"right\" style=\"text-align:center;background-color:").append(color).append(";font-weight:").append(fontweight).append(";text-align:").append(textalign).append(";font-size:").append(fontsize).append("pt;\" >");
                            subMenuHtml.append("<font color=\"\"><a href=\"javascript:submiturlsinNewTab('" + finalrepDrillUrl + "')\" style=\"text-decoration:none;\"  style=\"color: #CC0000\">").append(b1).append("</a></font></td>");
                        } else {
                            subMenuHtml.append("<td id=").append(count).append(" align=\"right\" style=\"text-align:center;background-color:").append(color).append(";font-weight:").append(fontweight).append(";text-align:").append(textalign).append(";font-size:").append(fontsize).append("pt;\" >");
                            subMenuHtml.append("<font color=\"\"><a style=\"text-decoration:none;\"  style=\"color: #CC0000\">").append(b1).append("</a></font></td>");

                        }

                    }
                    changecol++;
                } else {
//                             b1=retObj.getFieldValueBigDecimal(0, rtcount);
//                        b1=b1.setScale(2, RoundingMode.CEILING);
                    String Msrdata = facade.getFormattedMeasureDataForTimeDB(0, rtcount);
                    if (!facade.container.getReportCollect().reportDrillMap.containsKey(DisplayColumns1.get(MsrsLngth))) {


                        subMenuHtml.append("<td  align=\"right\" style=\"text-align:center;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" >");
                        subMenuHtml.append("<font color=\"\"><a  style=\"text-decoration:none;\"  style=\"color: #CC0000\">" + Msrdata + "</a></font></td>");
                    } else {
                        if (!facade.container.getReportDrillMap(DisplayColumns1.get(MsrsLngth)).equalsIgnoreCase("0")) {
                            subMenuHtml.append("<td  align=\"right\" style=\"text-align:center;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" >");
                            subMenuHtml.append("<font color=\"\"><a href=\"javascript:submiturlsinNewTab('" + finalrepDrillUrl + "')\"  style=\"text-decoration:none;\"  style=\"color: #CC0000\">" + Msrdata + "</a></font></td>");
                        } else {
                            subMenuHtml.append("<td  align=\"right\" style=\"text-align:center;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" >");
                            subMenuHtml.append("<font color=\"\"><a  style=\"text-decoration:none;\"  style=\"color: #CC0000\">" + Msrdata + "</a></font></td>");

                        }

                    }
                    //     subMenuHtml.append("<font color=\"\"><a style=\"text-decoration:none;\"  style=\"color: #CC0000\">   </a></font></td>");

                    rtcount++;
                }
                count++;
                String type = allcolumns.get(l);
                String PRG_PERIOD_TYPE = "PRG_PERIOD_TYPE" + type;
                String PRG_COMPARE = "PRG_COMPARE" + type;
                String peroidtype = "";
                String prgcompare = "";
                if (type.equalsIgnoreCase("MTD") || type.equalsIgnoreCase("CPMTD") || type.equalsIgnoreCase("C%PMTD") || type.equalsIgnoreCase("PMTD")) {
                    peroidtype = "Month";
                    prgcompare = "Last Month";
                }
                if (type.equalsIgnoreCase("PYMTD") || type.equalsIgnoreCase("CPYMTD") || type.equalsIgnoreCase("C%PYMTD")) {
                    peroidtype = "Month";
                    prgcompare = "Same Month Last Year";
                }
                if (type.equalsIgnoreCase("QTD") || type.equalsIgnoreCase("CPQTD") || type.equalsIgnoreCase("C%PQTD") || type.equalsIgnoreCase("PQTD")) {
                    peroidtype = "Qtr";
                    prgcompare = "Last Qtr";
                }
                if (type.equalsIgnoreCase("PYQTD") || type.equalsIgnoreCase("CPYQTD") || type.equalsIgnoreCase("C%PYQTD")) {
                    peroidtype = "Qtr";
                    prgcompare = "Same Qtr Last Year";
                }
                if (type.equalsIgnoreCase("YTD") || type.equalsIgnoreCase("CPYTD") || type.equalsIgnoreCase("C%PYTD") || type.equalsIgnoreCase("PYTD")) {
                    peroidtype = "Year";
                    prgcompare = "Last Year";
                }

                subMenuHtml.append("<input name=\"" + PRG_PERIOD_TYPE + "\" id=\"" + PRG_PERIOD_TYPE + "\" type=hidden value=\"" + peroidtype + "\">");
                subMenuHtml.append("<input name=\"" + PRG_COMPARE + "\" id=\"" + PRG_COMPARE + "\" type=hidden value=\"" + prgcompare + "\">");

                subMenuHtml.append("<input name=\"" + p1value + "\" id=\"" + p1value + "\" type=hidden value=\"" + datetimes.get(allcolumns.get(l)).replace("-", "/") + "\">");
            }
            subMenuHtml.append("</Tr>");
        }


        subMenuHtml.append("</tbody></table><div>");

        return subMenuHtml;
    }

    public StringBuilder getKpiDashboardHTML(Container container) throws Exception {
//String HTMLString="";
        StringBuilder subMenuHtml = new StringBuilder();
        int CloumnCount = 10;
        String str = "";
        TableBuilder tableBldr;
        TableDataRow rowkpi = new TableDataRow();
        BigDecimal CurrentValue = null;
        BigDecimal PriorValue = null;
        this.container = container;
        PbReturnObject DataObject = (PbReturnObject) container.getRetObj();
        Menu[] tableMenu = null;
        this.facade = new DataFacade(container);
        tableBldr = new RowViewTableBuilder(facade);
        int toRow = tableBldr.getToRow();
        int fromrow = tableBldr.getFromRow();
        int viewcount = tableBldr.getViewByCount();
        int viewbyCount = 0;
        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int stCount = 0;
        int subTotalRowcount = 1;
//          TableSubtotalRow[] subtotalRows = tableBldr.getSubtotalRowData();
        TableSubtotalRow[] subTotalRow;
        subTotalRow = tableBldr.getGrandtotalRowData();
        ArrayList<String> DisplayColumns = container.getDisplayColumns();
        ArrayList<String> DisplayLabels = container.getDisplayLabels();
        ArrayList<String> DisplayColumns1 = new ArrayList<String>();
        ArrayList<String> DisplayLabelskpi = container.getDisplayLabelskpi();
        ArrayList<String> CmprEnabledMsrs = null;
        ArrayList<String> CmprEnabledMsrs1 = null;
        ArrayList<String> rowaddingarr = null;
        ArrayList<String> PriorMsrs = null;
        ArrayList<String> rowDatakpi = new ArrayList<String>();
        ArrayList<String> measureIds = new ArrayList<String>();
        HashMap<String, String> CmpareMap = null;
        HashMap<String, String> priorcoloumn = null;
        HashMap<String, String> rowaddingmap = null;
// HashMap<String,String> mainrowaddingmap=null;
        HashMap<String, HashMap<String, String>> mainrowaddingmap = new HashMap<String, HashMap<String, String>>();

        ArrayList<String> droppableList = new ArrayList<String>();
        droppableList = container.getReportCollect().getHideMeasures();
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;
        HashMap<String, String> finalCrossTabReportDrillMap = ((PbReturnObject) facade.container.getRetObj()).finalCrossTabReportDrillMap;

        for (int i = 0; i < DisplayColumns.size(); i++) {
            if (!DisplayColumns.get(i).contains("A_")) {
                DisplayColumns1.add(DisplayColumns.get(i));
                if (measureIds == null || !measureIds.contains(crosstabMeasureId.get(DisplayColumns.get(i)))) {
                    measureIds.add(crosstabMeasureId.get(DisplayColumns.get(i)));
                }
            }
        }

        if (container.isComparisionEnabled()) {
            PriorMsrs = new ArrayList<String>();
            CmpareMap = container.getComparisionEnabledmsr();
            CmprEnabledMsrs = new ArrayList(CmpareMap.keySet());
            for (int p = 0; p < CmprEnabledMsrs.size(); p++) {
                PriorMsrs.add(CmpareMap.get(CmprEnabledMsrs.get(p)));
            }
            priorcoloumn = container.getispriorcoloumn();
            CmprEnabledMsrs1 = new ArrayList(priorcoloumn.keySet());
            for (int p = 0; p < CmprEnabledMsrs1.size(); p++) {
                String ispriorcoloumn = priorcoloumn.get(CmprEnabledMsrs1.get(p));

            }
        }
        mainrowaddingmap = container.getrowAddingids();
        if (mainrowaddingmap != null) {

            rowaddingarr = new ArrayList(mainrowaddingmap.keySet());
        }
        int rowSpan;
        int colSpan = 0;
        String dispStyle = null;
        String dispStylecross = null;
        String dispStylecross1 = null;
        String datecompare = null;
        String datecompareto = null;
        String grvalue = null;
        reportid = container.getReportCollect().reportId;
        contxtpath = container.getContextPath();
        ArrayList tempTimeDetails = new ArrayList();
        tempTimeDetails = container.getReportCollect().timeDetailsArray;
        if (tempTimeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
            datecompare = tempTimeDetails.get(2).toString();
            datecompare = datecompare.substring(6, 10);
            int val = Integer.parseInt(datecompare) - 1;
            datecompareto = Integer.toString(val);

        } else {
            datecompare = tempTimeDetails.get(2).toString();
            datecompare = datecompare.substring(6, 10);
            String datecompare1 = tempTimeDetails.get(3).toString();
            datecompare1 = datecompare1.substring(6, 10);
            datecompare = datecompare + "-" + datecompare1;
            datecompareto = tempTimeDetails.get(4).toString();
            datecompareto = datecompareto.substring(6, 10);
            String datecompareto1 = tempTimeDetails.get(5).toString();
            datecompareto1 = datecompareto1.substring(6, 10);
            datecompareto = datecompareto + "-" + datecompareto1;
        }
        int ViewByCount = container.getViewByCount();
        String ViewByelement = DisplayColumns.get(0);
        String ViewByName = DisplayLabels.get(0);
//String Ref_element="";
//int topcount=tableProperties.topBtmCount;
        int count = container.getkpiTopBottomCount();
        if (count > 0) {
            CloumnCount = count;
        }
//HTMLString=HTMLString+"<div id=\"innerDivKpiId\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\" height=\"20px\"><table width=\"100%\">";
//HTMLString=HTMLString+"<tbody><tr><td id=\"kpiregion\" align=\"left\"></td><td align=\"right\"></td>";
//HTMLString=HTMLString+"<td align='right' width='5px;'><a title='Edit Table' class='ui-icon ui-icon-calculator' onclick=\"dispChangeTableColumns('"+container.getContextPath()+"','"+container.getReportCollect().reportBizRoles[0]+"')\" href='javascript:void(0)'></a> </td>";
//HTMLString=HTMLString+"<td align='right' width='5px;'><a title='Report Drill' class='ui-icon ui-icon-triangle-2-n-s' onclick=\"reportDrillAssignment('"+container.getContextPath()+"','"+container.getFolderIdsForFact()+"','"+container.getMsrDrillReportSelection()+"')\" href='javascript:void(0)'></a> </td>";
//HTMLString=HTMLString+"<td align='right' width='5px;'><a href='javascript:void(0)'  onclick=\"showkpiTableProperties('"+container.getContextPath()+"','"+container.getFolderIdsForFact()+"','"+container.getReportId()+"')\" class='ui-icon ui-icon-pencil'  title='Table Properties'></a> </td>";
//
//      HTMLString=HTMLString+"</tr></tbody> </table></div>";
        subMenuHtml.append("<div align=\"left\" style=\"width:99%;height:100%;overflow-y:auto;overflow-x:auto;padding:4px\" id=\"KpiDashDiv\"><table width=\"99%;height:auto\" cellspacing=\"1\" cellpadding=\"1\" class=\"tablesorter\" id=\"tablesorter\"><thead align=\"center\">");

        int RowCount = DataObject.getRowCount();
        if (RowCount < CloumnCount) {
            CloumnCount = RowCount;
        }
// int actualRow = facade.getActualRow(Integer.parseInt(ViewByelement));
        for (int vcnt = 0; vcnt < viewcount; vcnt++) {
            subMenuHtml.append("<tr><th rowspan='2' width=\"5px\"><strong></strong></th>");
            subMenuHtml.append("<th rowspan='2' width=\"250px\"><strong>KPI</strong></th>");
            if (facade.isReportCrosstab()) {
                for (TableHeaderRow headerRow : headerRows) {
                    String crosstabval = headerRow.getRowData(0);
                    String valelement1 = headerRow.getID(0);
                    subMenuHtml.append("<th rowspan='2' width=\"170\"><strong>" + crosstabval + "</strong></th>");
                    break;
                }
            }
            CloumnCount = CloumnCount + 2;
//subMenuHtml.append("<th rowspan='2' width=\"250px\"><strong>KPI</strong></th>");
            subMenuHtml.append("<th colspan='" + CloumnCount + "' width=\"250px\"><strong>" + DisplayLabels.get(vcnt) + "</strong></th></tr>");
            CloumnCount = CloumnCount - 2;
            subMenuHtml.append("<tr>");
            for (int i = 0; i < CloumnCount; i++) {
                int actualRow = facade.getActualRow(i);
//     String displaystyle=getDisplayStyle(i);
//      facade.setActualrow(actualRow);
                String ViewBYHeader = facade.getDimensionData(actualRow, DisplayColumns.get(vcnt));
                subMenuHtml.append("<th width=\"170\"><strong>" + ViewBYHeader + "</strong></th>");
            }
            if (vcnt == 0) {
                if (container.getGrandTotalReqForkpi()) {//bhargavi
                    for (int i = 0; i < subTotalRow.length; i++) {
                        TableSubtotalRow row = null;

                        row = subTotalRow[i];
                        grvalue = row.getRowData(0);
//            id2= row.getID(col1);
//            cssClass1 = row.getCssClass(col1);
//String displaystyle=row.getDisplayStyle(col1);
                        break;
                    }

                    subMenuHtml.append("<th width=\"170\" rowspan=" + viewcount + "><strong>" + grvalue + "</strong></th>");
                }
                if (container.getNetTotalReqForkpi()) {
                    subMenuHtml.append("<th width=\"170\" rowspan=" + viewcount + "><strong>Sub Total</strong></th>");
                }
            }
            subMenuHtml.append("</tr>");
        }
        subMenuHtml.append("</thead>");
        subMenuHtml.append("<tfoot></tfoot><tbody>");


        String measureval2 = "";
        for (int MsrsLngth = viewcount; MsrsLngth < DisplayLabels.size(); MsrsLngth++) {
            String fontsize = "8";
            String textalign = "center";
            String textalign1 = "left";
            String fontweight = "normal";
            String color = "";
            int n = 0;
            ArrayList<BigDecimal> subTotalCol = new ArrayList<BigDecimal>();
            if (!ViewSequence.isEmpty() && MsrsLngth < ViewSequence.size()) {
                if (container.getrowcolorForkpiHashMap().containsKey(DisplayColumns.get(ViewSequence.get(MsrsLngth)))) {
                    color = container.getrowcolorForkpi(DisplayColumns.get(ViewSequence.get(MsrsLngth)));
                }
            } else {
                if (container.getrowcolorForkpiHashMap().containsKey(DisplayColumns.get(MsrsLngth))) {
                    color = container.getrowcolorForkpi(DisplayColumns.get(MsrsLngth));
                }
            }
            if (!ViewSequence.isEmpty() && MsrsLngth < ViewSequence.size()) {
                if (container.getFontFormatsHashMap().containsKey(DisplayColumns.get(ViewSequence.get(MsrsLngth)))) {
                    HashMap fontsize1 = container.getFontFormatsType(DisplayColumns.get(ViewSequence.get(MsrsLngth)));
                    fontsize = fontsize1.get("fontsize").toString();
                    fontsizego = fontsize;
                    textalign = fontsize1.get("textalign").toString();
                    textaligngo = textalign;
                    fontweight = fontsize1.get("fontstyle").toString();
                    fontweightgo = fontweight;
                    if (fontsize.equalsIgnoreCase("Small")) {
                        fontsize = "6";
                    } else if (fontsize.equalsIgnoreCase("Large")) {
                        fontsize = "11";
                    }
                }
            } else {
                if (container.getFontFormatsHashMap().containsKey(DisplayColumns.get(MsrsLngth))) {
                    HashMap fontsize1 = container.getFontFormatsType(DisplayColumns.get(MsrsLngth));
                    fontsize = fontsize1.get("fontsize").toString();
                    fontsizego = fontsize;
                    textalign = fontsize1.get("textalign").toString();
                    textaligngo = textalign;
                    fontweight = fontsize1.get("fontstyle").toString();
                    fontweightgo = fontweight;
                    if (fontsize.equalsIgnoreCase("Small")) {
                        fontsize = "6";
                    } else if (fontsize.equalsIgnoreCase("Large")) {
                        fontsize = "11";
                    }
                }
            }
            String ViewByelement1 = "";
            String cmprmsr1 = "";
            String rowaddtrue = "";
            String rowaddtrue1 = "";
            PbDb pbdb = new PbDb();
            ArrayList<String> crosstabvales = new ArrayList<String>();
            if (facade.isReportCrosstab()) {
                ViewByelement1 = DisplayColumns.get(MsrsLngth);
                ViewByelement1 = crosstabMeasureId.get(ViewByelement1);
                ViewByelement1 = ViewByelement1.replace("A_", "");
            } else {
                ViewByelement1 = DisplayColumns.get(MsrsLngth).replace("A_", "");
            }
            String eleId = "select REF_ELEMENT_ID from prg_user_sub_folder_elements where ELEMENT_ID=" + ViewByelement1;
            PbReturnObject pbro = pbdb.execSelectSQL(eleId);
            for (int i = 0; i < pbro.getRowCount(); i++) {
                Ref_element = pbro.getFieldValueString(i, 0);

            }
            String enablevalue = "";
            if (priorcoloumn != null) {
                enablevalue = priorcoloumn.get(ViewByelement1);
            }
            if (container.isReportCrosstab()) {
                cmprmsr1 = crosstabMeasureId.get(DisplayColumns.get(MsrsLngth)).replace("A_", "");
            } else {
                cmprmsr1 = DisplayColumns.get(MsrsLngth).replace("A_", "");
            }
            if (!container.isComparisionEnabled() || (!PriorMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) || (enablevalue != null && enablevalue.equalsIgnoreCase("priorcoloumn")))) {
//HTMLString=HTMLString+"<tr><td align=\"center\" style=\"cursor:pointer;background-color:\"><a class=\"ui-icon ui-icon-plus\" onclick=\"javascript:enableComparision('"+ DisplayColumns.get(MsrsLngth).toString().replace("A_","")+"')\" target=\"_parent\" style=\"text-decoration:none\">Enable Comparision</a></td>";
//HTMLString=HTMLString+"<tr><td height=\"25\" align=\"center\" style=\"cursor:pointer;background-color:\"><a class=\"ui-icon ui-icon-plus\" onclick=\"javascript:enableComparision('"+ DisplayColumns.get(MsrsLngth).toString().replace("A_","")+"')\" target=\"_parent\" style=\"text-decoration:none\" title=\"enable\\disable comparision\">Enable Comparision</a></td>";
//"<div id="modules" onmouseover="modlistDisp1()"  style="display:none;width:125px;height:auto;background-color:white;overflow: visible;position:absolute;text-align:left;z-index: 9999999;margin-left: 0px;margin-top: 0px;">
//                                             <table border='0' align='left' >
//       <tr><td></td></tr></table></div>
                String displaylable = "";
                if (facade.isReportCrosstab()) {

// for(int ij=0; ij<DisplayLabels.size(); ij++){
                    Object paramObject = null;
                    paramObject = DisplayLabelskpi.get(MsrsLngth);
                    Type listOfTestObject = new TypeToken<ArrayList>() {
                    }.getType();
                    //Gson gson = new Gson();
                    String paramObject1 = gson.toJson(paramObject, listOfTestObject);
//                 paramObject1=(String) paramObject;
                    rowDatakpi.add(paramObject1.replace("\"", "").replace("[", "").replace("]", ""));
                    String[] val = paramObject1.split(",");
                    for (int i = 0; i < val.length; i++) {
                        String v1 = val[i];
                        v1 = v1.replace("[", "");
                        crosstabvales.add(v1);
                    }
//     }
                } else {
                    displaylable = DisplayLabels.get(MsrsLngth);
                }


                if (!ViewSequence.isEmpty() && MsrsLngth < ViewSequence.size()) {
                    ViewByelement = DisplayColumns.get(ViewSequence.get(MsrsLngth));
                    tableMenu = getMenu(DisplayColumns.get(ViewSequence.get(MsrsLngth)), displaylable, ViewByelement);
                } else {
                    ViewByelement = DisplayColumns.get(MsrsLngth);
                    tableMenu = getMenu(DisplayColumns.get(MsrsLngth), displaylable, ViewByelement);
                }

                String menuName;
                String menuFunction;
                for (TableHeaderRow headerRow : headerRows) {
                    if (facade.isReportCrosstab()) {
//                 for (int ij=1; ij<headerRows.length;ij++ ){
//                     TableHeaderRow headerRow1 =headerRows[ij];
//                     //code added by bhargavi to aviod row grand total on 20th Dec
                        if (DisplayColumns.get(MsrsLngth).contains("A_")) {
                            dispStylecross1 = "none";
                        } else {
                            dispStylecross1 = "";
                        }
                    } else {
                        dispStyle = headerRow.getDisplayStyle(MsrsLngth);
                    }
                    rowSpan = headerRow.getRowSpan(MsrsLngth);
                    colSpan = headerRow.getColumnSpan(MsrsLngth);
//                 dispStyle = headerRow.getDisplayStyle(MsrsLngth);
                    if (rowaddingarr != null && rowaddingarr.size() > 0) {
                        // if(container.isNewRowAdded!=null && container.isNewRowAdded.equalsIgnoreCase("true")){
                        String ViewByelementrowadd = DisplayColumns.get(MsrsLngth);

                        for (int r = 0; r < rowaddingarr.size(); r++) {
                            String rowaddingarrid = rowaddingarr.get(r);
                            if (ViewByelementrowadd == null ? rowaddingarrid == null : ViewByelementrowadd.equals(rowaddingarrid)) {
                                rowaddingmap = mainrowaddingmap.get(DisplayColumns.get(MsrsLngth));
                                if (rowaddingmap != null) {
                                    rowaddtrue = rowaddingmap.get("position");
                                    rowaddtrue1 = rowaddingmap.get("type");
                                }
                                Menu[] tableMenu1 = null;
                                if (rowaddtrue != null && rowaddtrue.equalsIgnoreCase("above")) {
                                    tableMenu1 = getaddMenurow(DisplayColumns.get(MsrsLngth), displaylable, ViewByelement);
                                    subMenuHtml.append(this.getaddingrow(container, ViewByelement, MsrsLngth, dispStyle, tableMenu1, CloumnCount, rowaddtrue1));
                                }
                            }
                        }
                    }


                    if (facade.isReportCrosstab()) {
                        viewbyCount = DisplayColumns1.size() / measureIds.size();
                        if (stCount == viewbyCount) {
//                            Menu[] tableMenuForst = null;
//                    tableMenuForst = getaddMenurow(DisplayColumns.get(MsrsLngth), displaylable, ViewByelement);
                            subMenuHtml.append(this.setRowSubTotalHtml(CloumnCount, MsrsLngth, subTotalRowcount, measureIds, stCount));
                            subTotalRowcount++;
                            stCount = 0;
                        }
                        subMenuHtml.append("<Tr id=\"rowId" + MsrsLngth + "\"><td width=\"5px;\" style=\"display:" + dispStylecross1 + ";\">");

                    } else {
                        subMenuHtml.append("<Tr id=\"rowId" + MsrsLngth + "\"><td width=\"5px;\" style=\"display:" + dispStyle + ";\">");
                    }
                    subMenuHtml.append("<ul class=\"dropDownMenu\" style=\"width:5px;\">");
                    subMenuHtml.append("<li style=\"width:100%;margin-left:-18px;\"><a href=\"#\" class=\"ui-icon ui-icon-star\" style=\"padding:0px;margin-top:-5px;margin-left: 15px;\"></a>");
                    subMenuHtml.append("<ul style=\"width:150px;padding:5px 62px 0 0px;margin-top:-15px;margin-left: 15px;margin-left: 15px;\">");
                    break;
                }
                for (Menu menu : tableMenu) {
                    menuName = menu.getMenuEntry();
                    menuFunction = menu.getMenuFunction();
                    subMenuHtml.append("<li><a href=\"javascript:" + menuFunction + "\">" + menuName + "</a>");
                    subMenuHtml.append(this.getSubMenu(menu.getSubMenu()));
                    subMenuHtml.append("</li>");

                }

                subMenuHtml.append("</ul>");
                subMenuHtml.append("</li>");

//          subMenuHtml.append("/ul>");
                subMenuHtml.append("</td>");
                String measureval = "";
                String measureval1 = "";

                if (facade.isReportCrosstab()) {

                    measureval = crosstabvales.get(1).replace("[", "").replace("\"", "").replace("]", "");
                    measureval1 = crosstabvales.get(0).replace("[", "").replace("\"", "").replace("]", "");

                    if (measureval2.equalsIgnoreCase(measureval1) || DisplayColumns.get(MsrsLngth).contains("A_")) {
                        dispStylecross = "none";
                    } else {
                        dispStylecross = "";
                    }
//subMenuHtml.append("<td height=\"25\"  align=\"left\" rowspan="+colSpan+" style=\"cursor:pointer; vertical-align:middle;background-color:"+color+";font-weight:"+fontweight+";text-align:"+textalign1+";font-size:"+fontsize+"pt; display:"+dispStylecross+"; font-weight: bold;font-color:block;\"  class=\"dimensionCell\">"+measureval1+"</td>");
                    subMenuHtml.append("<td height=\"25\"  align=\"left\" rowspan=" + viewbyCount + " style=\"cursor:pointer; vertical-align:middle;font-weight:" + fontweight + ";text-align:" + textalign1 + ";font-size:" + fontsize + "pt; display:" + dispStylecross + "; font-weight: bold;\"  class=\"dimensionCell\">" + measureval1 + "</td>");
                    subMenuHtml.append("<td height=\"25\"  align=\"left\" style=\"cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign1 + ";font-size:" + fontsize + "pt; display:" + dispStylecross1 + "; font-weight: bold;\"  class=\"dimensionCell\">" + measureval + "</td>");
                    measureval2 = measureval1;
                } else {
                    subMenuHtml.append("<td height=\"25\"  align=\"left\" style=\"cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign1 + ";font-size:" + fontsize + "pt; display:" + dispStyle + "; font-weight: bold;\"  class=\"dimensionCell\">" + DisplayLabels.get(MsrsLngth) + "</td>");
                }

                if (!facade.isReportCrosstab()) {
                    for (int l = 0; l < CloumnCount; l++) {
                        int actualRow = facade.getActualRow(l);
//      facade.setActualrow(actualRow);
                        String element = "";
//                    if (facade.isReportCrosstab()) {
//                        dispStyle = dispStylecross1;
                        element = DisplayColumns.get(MsrsLngth);
//                        element = crosstabMeasureId.get(element);
//                    } else {
//                        element = DisplayColumns.get(MsrsLngth);
//                    }
                        List cellDataList = new ArrayList();
                        //Gson gson = new Gson();
                        Type tarType = new TypeToken<List<String>>() {
                        }.getType();
//String Msrdata=facade.getFormattedMeasureData(l, MsrsLngth);
                        String Msrdata = facade.getFormattedMeasureData(actualRow, MsrsLngth);
                        BigDecimal Msrdata1 = facade.getFormattedMeasureDataForkpi(actualRow, MsrsLngth);

                        subTotalCol.add(Msrdata1);
                        if (container.isComparisionEnabled() && CmprEnabledMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) && CmpareMap != null) {
                            String Msr3 = CmpareMap.get(DisplayColumns.get(MsrsLngth).replace("A_", ""));
                            Msr3 = "A_" + Msr3;
                            int index = DisplayColumns.indexOf(Msr3);
                            String PriorData = facade.getFormattedMeasureData(actualRow, index);
                            CurrentValue = new BigDecimal(Msrdata.replaceAll(",", "").replaceAll("K", "").replaceAll("M", "").replaceAll("L", "").replaceAll("Cr", ""));
                            PriorValue = new BigDecimal(PriorData.replaceAll(",", "").replaceAll("K", "").replaceAll("M", "").replaceAll("L", "").replaceAll("Cr", ""));
                        }
                        if (!facade.container.getReportCollect().reportDrillMap.containsKey(element)) {
                            if (container.isComparisionEnabled() && CmprEnabledMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) && CmpareMap != null) {
                                int res = CurrentValue.compareTo(PriorValue);
                                if (res == 0 || res == 1) {
                                    subMenuHtml.append("<td height=\"25\"  align=\"right\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellPositive\" style=\"text-align:center;cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + dispStyle + ";\" title='prior value is: " + PriorValue.toString() + "' ><font color=\"green\">" + Msrdata + "</font></td>");
                                } else {
                                    subMenuHtml.append("<td height=\"25\"  align=\"right\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellNegative\" style=\"text-align:center;cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + dispStyle + ";\" title='prior value is: " + PriorValue.toString() + "' ><font color=\"red\">" + Msrdata + "</font></td>");
                                }
                            } else {
                                subMenuHtml.append("<td height=\"25\" style=\" text-align:center;background-color:" + color + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;font-weight:" + fontweight + ";display:" + dispStyle + ";\" align=\"right\">" + Msrdata + "</td>");
                            }
                        } else if (facade.container.getReportDrillMap(element) != null && facade.container.getReportDrillMap(element).equalsIgnoreCase("0")) {
                            if (container.isComparisionEnabled() && CmprEnabledMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) && CmpareMap != null) {
                                int res = CurrentValue.compareTo(PriorValue);
                                if (res == 0 || res == 1) {
                                    subMenuHtml.append("<td height=\"25\"  align=\"right\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellPositive\" style=\"text-align:center;cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + dispStyle + ";\" title='prior value is: " + PriorValue.toString() + "' ><font color=\"green\">" + Msrdata + "</font></td>");
                                } else {
                                    subMenuHtml.append("<td height=\"25\"  align=\"right\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellNegative\" style=\" text-align:center;cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + dispStyle + ";\" title='prior value is: " + PriorValue.toString() + "' ><font color=\"red\">" + Msrdata + "</font></td>");
                                }
                            } else {
                                subMenuHtml.append("<td height=\"25\" style=\"text-align:center; background-color:" + color + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;font-weight:" + fontweight + ";display:" + dispStyle + ";\" align=\"right\">" + Msrdata + "</td>");
                            }
                        } else {
//                            String reportDrillUrl = "";
                            StringBuilder reportDrillUrl = new StringBuilder();
                            String finalrepDrillUrl = facade.container.getContextPath() + "/reportViewer.do?reportBy=viewReport";
                            finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(element);
                            for (int vcnt = 0; vcnt < viewcount; vcnt++) {
                                Object cellData = facade.getDimensionData(actualRow, DisplayColumns.get(vcnt));
// if(!facade.isReportCrosstab()){
                                cellDataList.clear();
//    }
                                cellDataList.add(cellData);
                                if (facade.isReportCrosstab()) {
                                    Object cellData1 = measureval1;
                                    cellDataList.add(cellData1);
                                }
                                reportDrillUrl.append("&CBOARP").append(DisplayColumns.get(vcnt).replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType)));
//                                reportDrillUrl += "&CBOARP" + DisplayColumns.get(vcnt).replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));

                            }
                            finalrepDrillUrl += reportDrillUrl.toString() + "&fromreport=report&istimedash=N&reportDrill=Y";//reportDrill=Y means assigned report is not there in session in that it is useful calling updatecollection  for passing date from assigning report to assigned report

                            if (container.isComparisionEnabled() && CmprEnabledMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) && CmpareMap != null) {
                                int res = CurrentValue.compareTo(PriorValue);
                                if (res == 0 || res == 1) {
                                    subMenuHtml.append("<td height=\"25\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellPositive\" style=\"text-align:center;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" align=\"right\"  >");
                                    subMenuHtml.append("<font><a href=\"javascript:submiturlsinNewTab1('" + finalrepDrillUrl + "')\"  style=\"text-decoration:none;color:green;display:" + dispStyle + ";\" title= 'prior value is: " + PriorValue.toString() + "'  >" + Msrdata + "</a></font></td>");
                                } else {
                                    subMenuHtml.append("<td height=\"25\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" style=\"text-align:center;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" class=\"kpimeasureNumericCellNegative\" align=\"right\">");
                                    subMenuHtml.append("<font><a href=\"javascript:submiturlsinNewTab1('" + finalrepDrillUrl + "')\"  style=\"text-decoration:none;color:red;display:" + dispStyle + ";\" title= 'prior value is: " + PriorValue.toString() + "'>" + Msrdata + "</a></font></td>");
                                }
                            } else {
                                subMenuHtml.append("<td  align=\"right\" style=\"text-align:center;display:" + dispStyle + ";background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" >");
                                subMenuHtml.append("<font color=\"\"><a href=\"javascript:submiturlsinNewTab1('" + finalrepDrillUrl + "')\"  style=\"text-decoration:none;display:" + dispStyle + ";\"  style=\"color: #CC0000\">" + Msrdata + "</a></font></td>");
                            }
                            //<font color=""><a href="javascript:parent.submiturlsinNewTab('%2FpiEE%2FreportViewer.do%3FreportBy%3DviewReport%26REPORTID%3D21859%26CBOARP111667%3D%255B%2522AHMEDABAD%2522%255D%26reportDrill%3DY')" target="_parent" style="text-decoration:none">657,537</a></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        }
                    }
                } else {
                    setDisplayColumnskpi(container, measureIds, CloumnCount);
                    //getRowDataForKpi(MsrsLngth);
                    for (int l = 0; l < CloumnCount; l++) {

                        int actualRow = facade.getActualRow(l);
                        String element = "";
                        dispStyle = dispStylecross1;
                        if (MsrsLngth <= DisplayColumnskpi.size()) {
                            element = crosstabMeasureId.get(DisplayColumnskpi.get(MsrsLngth));
                        } else {
                            element = crosstabMeasureId.get(DisplayColumns.get(MsrsLngth));
                        }
                        List cellDataList = new ArrayList();
                        //Gson gson = new Gson();
                        Type tarType = new TypeToken<List<String>>() {
                        }.getType();

                        String Msrdata;
                        if (MsrsLngth <= MeasureDataMap.size()) {
                            Msrdata = MeasureDataMap.get(MsrsLngth).get(l).toString();
                        } else {
                            Msrdata = facade.getFormattedMeasureData(actualRow, MsrsLngth);
                        }
                        rowDatakpi.add(Msrdata);
                        //BigDecimal Msrdata1 = facade.getFormattedMeasureDataForkpi(actualRow, MsrsLngth);

                        //subTotalCol.add(Msrdata1);
                        if (container.isComparisionEnabled() && CmprEnabledMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) && CmpareMap != null) {
                            String Msr3 = CmpareMap.get(DisplayColumns.get(MsrsLngth).replace("A_", ""));
                            Msr3 = "A_" + Msr3;
                            int index = DisplayColumns.indexOf(Msr3);
                            String PriorData = facade.getFormattedMeasureData(actualRow, index);
                            CurrentValue = new BigDecimal(Msrdata.replaceAll(",", "").replaceAll("K", "").replaceAll("M", "").replaceAll("L", "").replaceAll("Cr", ""));
                            PriorValue = new BigDecimal(PriorData.replaceAll(",", "").replaceAll("K", "").replaceAll("M", "").replaceAll("L", "").replaceAll("Cr", ""));
                        }
                        if (!facade.container.getReportCollect().reportDrillMap.containsKey(element)) {
                            if (container.isComparisionEnabled() && CmprEnabledMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) && CmpareMap != null) {
                                int res = CurrentValue.compareTo(PriorValue);
                                if (res == 0 || res == 1) {
                                    subMenuHtml.append("<td height=\"25\"  align=\"right\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellPositive\" style=\"text-align:center;cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + dispStyle + ";\" title='prior value is: " + PriorValue.toString() + "' ><font color=\"green\">" + Msrdata + "</font></td>");
                                } else {
                                    subMenuHtml.append("<td height=\"25\"  align=\"right\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellNegative\" style=\"text-align:center;cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + dispStyle + ";\" title='prior value is: " + PriorValue.toString() + "' ><font color=\"red\">" + Msrdata + "</font></td>");
                                }
                            } else {
                                subMenuHtml.append("<td height=\"25\" style=\" text-align:center;background-color:" + color + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;font-weight:" + fontweight + ";display:" + dispStyle + ";\" align=\"right\">" + Msrdata + "</td>");
                            }
                        } else if (facade.container.getReportDrillMap(element) != null && facade.container.getReportDrillMap(element).equalsIgnoreCase("0")) {
                            if (container.isComparisionEnabled() && CmprEnabledMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) && CmpareMap != null) {
                                int res = CurrentValue.compareTo(PriorValue);
                                if (res == 0 || res == 1) {
                                    subMenuHtml.append("<td height=\"25\"  align=\"right\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellPositive\" style=\"text-align:center;cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + dispStyle + ";\" title='prior value is: " + PriorValue.toString() + "' ><font color=\"green\">" + Msrdata + "</font></td>");
                                } else {
                                    subMenuHtml.append("<td height=\"25\"  align=\"right\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellNegative\" style=\" text-align:center;cursor:pointer;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + dispStyle + ";\" title='prior value is: " + PriorValue.toString() + "' ><font color=\"red\">" + Msrdata + "</font></td>");
                                }
                            } else {
                                subMenuHtml.append("<td height=\"25\" style=\"text-align:center; background-color:" + color + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;font-weight:" + fontweight + ";display:" + dispStyle + ";\" align=\"right\">" + Msrdata + "</td>");
                            }
                        } else {
//                            String reportDrillUrl = "";
                            StringBuilder reportDrillUrl = new StringBuilder(250);
                            String finalrepDrillUrl = facade.container.getContextPath() + "/reportViewer.do?reportBy=viewReport";
                            if (finalCrossTabReportDrillMap.containsKey(element)) {
                                finalrepDrillUrl += "&REPORTID=" + finalCrossTabReportDrillMap.get(element);
                            }
                            finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(element);

                            for (int vcnt = 0; vcnt < viewcount; vcnt++) {
                                Object cellData = facade.getDimensionData(actualRow, DisplayColumns.get(vcnt));

                                cellDataList.clear();

                                cellDataList.add(cellData);
                                if (facade.isReportCrosstab()) {
                                    Object cellData1 = measureval;
                                    cellDataList.add(cellData1);
                                }
                                reportDrillUrl.append("&CBOARP").append(DisplayColumns.get(vcnt).replace("A_", "")).append("=").append(java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType)));
//                                 reportDrillUrl += "&CBOARP" + DisplayColumns.get(vcnt).replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));

                            }
                            finalrepDrillUrl += reportDrillUrl + "&fromreport=report&reportDrill=Y";//reportDrill=Y means assigned report is not there in session in that it is useful calling updatecollection  for passing date from assigning report to assigned report

                            if (container.isComparisionEnabled() && CmprEnabledMsrs.contains(DisplayColumns.get(MsrsLngth).replace("A_", "")) && CmpareMap != null) {
                                int res = CurrentValue.compareTo(PriorValue);
                                if (res == 0 || res == 1) {
                                    subMenuHtml.append("<td height=\"25\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" class=\"kpimeasureNumericCellPositive\" style=\"text-align:center;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" align=\"right\"  >");
                                    subMenuHtml.append("<font><a href=\"javascript:submiturlsinNewTab1('" + finalrepDrillUrl + "')\"  style=\"text-decoration:none;color:green;display:" + dispStyle + ";\" title= 'prior value is: " + PriorValue.toString() + "'  >" + Msrdata + "</a></font></td>");
                                } else {
                                    subMenuHtml.append("<td height=\"25\" onclick=\"getenablevalues('" + Msrdata + "','" + PriorValue + "','" + datecompare + "','" + datecompareto + "')\" style=\"text-align:center;background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" class=\"kpimeasureNumericCellNegative\" align=\"right\">");
                                    subMenuHtml.append("<font><a href=\"javascript:submiturlsinNewTab1('" + finalrepDrillUrl + "')\"  style=\"text-decoration:none;color:red;display:" + dispStyle + ";\" title= 'prior value is: " + PriorValue.toString() + "'>" + Msrdata + "</a></font></td>");
                                }
                            } else {
                                subMenuHtml.append("<td  align=\"right\" style=\"text-align:center;display:" + dispStyle + ";background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;\" >");
                                subMenuHtml.append("<font color=\"\"><a href=\"javascript:submiturlsinNewTab1('" + finalrepDrillUrl + "')\"  style=\"text-decoration:none;display:" + dispStyle + ";\"  style=\"color: #CC0000\">" + Msrdata + "</a></font></td>");
                            }

                        }
                    }
                }

                //createSubTotalRow(subTotalCol, MsrsLngth, container);
                String displaystyle = "";
                if (container.getGrandTotalReqForkpi()) {//bhargavi

                    for (int i = 0; i < subTotalRow.length; i++) {
                        TableSubtotalRow row = null;

                        row = subTotalRow[i];
//               for(int MsrsLngth1=1;MsrsLngth1<DisplayLabels.size();MsrsLngth1++){
                        if (facade.isReportCrosstab()) {
                            if (MsrsLngth <= ViewSequence.size()) {
                                grvalue = row.getRowData(ViewSequence.get(MsrsLngth));
                                rowDatakpi.add(grvalue);
                                if (MsrsLngth < DisplayLabels.size() - measureIds.size()) {
                                    rowSubTotalForGT = rowSubTotalForGT.add(new BigDecimal(grvalue.replace(",", "").replace("K", "").replace("L", "").replace("M", "").replace("Cr", "")));
                                }
                            } else {
                                grvalue = row.getRowData(MsrsLngth);

                            }
                        } else {
                            grvalue = row.getRowData(MsrsLngth);
                        }
                        //code added by bhargavi to aviod row grand total on 20th Dec
                        if (facade.isReportCrosstab()) {
                            if (DisplayColumns.get(MsrsLngth).contains("A_")) {
                                displaystyle = "none";
                            } else {
                                displaystyle = row.getDisplayStyle(MsrsLngth);
                            }
                        } else {
                            displaystyle = row.getDisplayStyle(MsrsLngth);
                        }
                        break;
//    }
//            id2= row.getID(col1);
//            cssClass1 = row.getCssClass(col1);
//String displaystyle=row.getDisplayStyle(col1);

                    }

                    subMenuHtml.append("<td height=\"25\" style=\"text-align:center; background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + displaystyle + " ;\" align=\"right\">" + grvalue + "</td>");
                }
//subMenuHtml.append("</tr>");
                if (container.getNetTotalReqForkpi()) {
                    if (facade.isReportCrosstab()) {
                        String element;
                        String aggType;
                        String formattedData;
                        BigDecimal subTotalValue = BigDecimal.ZERO;


                        if (MsrsLngth <= subTotalForkpidb.size()) {
                            element = crosstabMeasureId.get(DisplayColumnskpi.get(MsrsLngth));
                            aggType = (String) container.getAggregationType(element);
                            if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                                subTotalValue = subTotalForkpidb.get(MsrsLngth).divide(new BigDecimal(CloumnCount), RoundingMode.HALF_UP);
                            } else {
                                subTotalValue = subTotalForkpidb.get(MsrsLngth);
                            }

                            formattedData = facade.formatMeasureData(subTotalValue, ViewSequence.get(MsrsLngth));
                        } else {
                            formattedData = "";
                        }
                        rowDatakpi.add(formattedData);
                        subMenuHtml.append("<td height=\"25\" style=\"text-align:center; background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + displaystyle + " ;\" align=\"right\">" + formattedData + "</td>");
                    } else {
                        createSubTotalRow(subTotalCol, MsrsLngth, container);
                        subMenuHtml.append("<td height=\"25\" style=\"text-align:center; background-color:" + color + ";font-weight:" + fontweight + ";text-align:" + textalign + ";font-size:" + fontsize + "pt;display:" + displaystyle + " ;\" align=\"right\">" + subTotalForkpidb1.get(MsrsLngth) + "</td>");

                    }
                }

                if (rowaddtrue != null && rowaddtrue.equalsIgnoreCase("below")) {
                    Menu[] tableMenu1 = null;
                    tableMenu1 = getaddMenurow(DisplayColumns.get(MsrsLngth), displaylable, ViewByelement);
                    subMenuHtml.append(this.getaddingrow(container, ViewByelement, MsrsLngth, dispStyle, tableMenu1, CloumnCount, rowaddtrue1));
                }
            }
            stCount++;

            RowSequenceMap.put(MsrsLngth, rowDatakpi);
            rowDatakpi.clear();
        }

//        subMenuHtml.append("</Tr>");
//       rowkpi.setRowDatakpi(rowData);

        subMenuHtml.append("</tbody></table><div>");
//String  cellData = DataObject.getFieldValueString(row, columnName);

        return subMenuHtml;
    }

    public Menu[] getaddMenurow(String column, String displaylable, String ViewByelement) throws Exception {


        return buildMenuForaddrow(column, displaylable, ViewByelement);


    }

    public Menu[] getMenu(String column, String displaylable, String ViewByelement) throws Exception {


        return buildMenuForMeasure(column, displaylable, ViewByelement);


    }

    private Menu[] buildMenuForaddrow(String column, String displaylable, String ViewByelement) throws Exception {
        ArrayList<Menu> menuLst = new ArrayList<Menu>();

        Menu menu = null;
        String menuFunction;
        menu = new Menu("Delete Row");
        menuFunction = this.getMenuFunctionForRowadd(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "delete", "delete");
        menu.setMenuFunction(menuFunction);
        menuLst.add(menu);

        menu = new Menu("Edit Format");
        menuFunction = this.getMenuFunctionForFormatting(ViewByelement, displaylable, reportid, contxtpath, Ref_element);
        menu.setMenuFunction(menuFunction);
        menuLst.add(menu);

        menu = new Menu("Row Coloring");
        menuFunction = this.getMenuFunctionForRowColoring(ViewByelement, displaylable, reportid, contxtpath, Ref_element);
        menu.setMenuFunction(menuFunction);
        menuLst.add(menu);

        return menuLst.toArray(new Menu[]{});
    }
    //sandeep

    private Menu[] buildMenuForMeasure(String column, String displaylable, String ViewByelement) throws Exception {
        ArrayList<Menu> menuLst = new ArrayList<Menu>();
        UserLayerDAO uDAO = new UserLayerDAO();
        Menu menu = null;
        Menu[] subMenu = new Menu[6];
        Menu[] subSortMenu = new Menu[2];
        String menuFunction;
//       if(menuPrivileges.contains("SORT")){
//        if(uDAO.getFeatureEnableHashMap("Sort",paramhashmapPA) || userTypeAdmin.equalsIgnoreCase("SUPERADMIN")) {
        menu = new Menu("Rounding");
        menu.setMenuFunction("#");
        subMenu[0] = new Menu("No Decimal");
        menuFunction = this.getMenuFunctionForRounding(ViewByelement, displaylable, reportid, 0);
        subMenu[0].setMenuFunction(menuFunction);
        subMenu[1] = new Menu("One Decimal");
        menuFunction = this.getMenuFunctionForRounding(ViewByelement, displaylable, reportid, 1);
        subMenu[1].setMenuFunction(menuFunction);
        subMenu[2] = new Menu("Two Decimal");
        menuFunction = this.getMenuFunctionForRounding(ViewByelement, displaylable, reportid, 2);
        subMenu[2].setMenuFunction(menuFunction);
        subMenu[3] = new Menu("Three Decimal");
        menuFunction = this.getMenuFunctionForRounding(ViewByelement, displaylable, reportid, 3);
        subMenu[3].setMenuFunction(menuFunction);
        subMenu[4] = new Menu("Four Decimal");
        menuFunction = this.getMenuFunctionForRounding(ViewByelement, displaylable, reportid, 4);
        subMenu[4].setMenuFunction(menuFunction);
        subMenu[5] = new Menu("Five Decimal");
        menuFunction = this.getMenuFunctionForRounding(ViewByelement, displaylable, reportid, 5);
        subMenu[5].setMenuFunction(menuFunction);
        menu.setSubMenu(subMenu);
        menuLst.add(menu);


//                subMenu = new Menu[6];
//        subSortMenu = new Menu[2];
        menu = new Menu("Number Format");
        menu.setMenuFunction("#");
        subMenu = new Menu[5];
        subMenu[0] = new Menu("Absolute");
        menuFunction = this.getMenuFunctionForNumberFormat(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "A");
        subMenu[0].setMenuFunction(menuFunction);
        subMenu[1] = new Menu("Thousands(K)");
        menuFunction = this.getMenuFunctionForNumberFormat(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "K");
        subMenu[1].setMenuFunction(menuFunction);
        subMenu[2] = new Menu("Millions(M)");
        menuFunction = this.getMenuFunctionForNumberFormat(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "M");
        subMenu[2].setMenuFunction(menuFunction);

        subMenu[3] = new Menu("Lakhs(L)");
        menuFunction = this.getMenuFunctionForNumberFormat(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "L");
        subMenu[3].setMenuFunction(menuFunction);

        subMenu[4] = new Menu("Crores(Cr)");
        menuFunction = this.getMenuFunctionForNumberFormat(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "Cr");
        subMenu[4].setMenuFunction(menuFunction);
        menu.setSubMenu(subMenu);
        menuLst.add(menu);


//                 menu = new Menu("GrandTotal Aggregation");
//             menu.setMenuFunction("#");
//                subMenu = new Menu[7];
//                subMenu[0] = new Menu("SUM");
//                menuFunction = this.getMenuFunctionForGtupdate(ViewByelement,displaylable, reportid, contxtpath,Ref_element, "SUM");
//                subMenu[0].setMenuFunction(menuFunction);
//                subMenu[1] = new Menu("MIN");
//                menuFunction = this.getMenuFunctionForGtupdate(ViewByelement,displaylable, reportid, contxtpath,Ref_element, "MIN");
//                subMenu[1].setMenuFunction(menuFunction);
//                subMenu[2] = new Menu("MAX");
//                menuFunction = this.getMenuFunctionForGtupdate(ViewByelement,displaylable, reportid, contxtpath,Ref_element, "MAX");
//                subMenu[2].setMenuFunction(menuFunction);
//
//                subMenu[3] = new Menu("AVG");
//                menuFunction = this.getMenuFunctionForGtupdate(ViewByelement,displaylable, reportid, contxtpath,Ref_element, "AVG");
//                subMenu[3].setMenuFunction(menuFunction);
//
//                subMenu[4] = new Menu("COUNT");
//                menuFunction = this.getMenuFunctionForGtupdate(ViewByelement,displaylable, reportid, contxtpath,Ref_element, "COUNT");
//                subMenu[4].setMenuFunction(menuFunction);
//
//                subMenu[5] = new Menu("COUNTDISTINCT");
//                menuFunction = this.getMenuFunctionForGtupdate(ViewByelement,displaylable, reportid, contxtpath,Ref_element, "COUNTDISTINCT");
//                subMenu[5].setMenuFunction(menuFunction);
//
//                subMenu[6] = new Menu("NONE");
//                menuFunction = this.getMenuFunctionForGtupdate(ViewByelement,displaylable, reportid, contxtpath,Ref_element, "NONE");
//                subMenu[6].setMenuFunction(menuFunction);
//                menu.setSubMenu(subMenu);
//                menuLst.add(menu);
//bhargavi
//        if (container.isReportCrosstab()) {
//            menu = new Menu("Row Sorting");
//            menuFunction = this.getMenuFunctionForSorting(ViewByelement, displaylable, reportid, contxtpath, Ref_element);
////                    
//            menu.setMenuFunction(menuFunction);
//            menuLst.add(menu);
//        }

        menu = new Menu("GrandTotal Aggregation");
        menuFunction = this.getMenuFunctionForgt(ViewByelement, displaylable, reportid, contxtpath, Ref_element);
//                    
        menu.setMenuFunction(menuFunction);
        menuLst.add(menu);

        menu = new Menu("Comparison");
        menu.setMenuFunction("#");
        subMenu = new Menu[2];
        subMenu[0] = new Menu("Enable");
        menuFunction = this.getMenuFunctionForcomapre(ViewByelement.toString().replace("A_", ""), false);
        subMenu[0].setMenuFunction(menuFunction);
        subMenu[1] = new Menu("Disable");
        menuFunction = this.getMenuFunctionForcomapre(ViewByelement.toString().replace("A_", ""), true);
        subMenu[1].setMenuFunction(menuFunction);
        menu.setSubMenu(subMenu);
        menuLst.add(menu);


        menu = new Menu("Row Adding");
        menu.setMenuFunction("#");
        subMenu = new Menu[2];
        subMenu[0] = this.createtextkrow(ViewByelement, displaylable);

        subMenu[1] = this.createblabkrow(ViewByelement, displaylable);

//
//                subMenu[0] = new Menu("Above");
//                menuFunction = this.getMenuFunctionForRowadd(ViewByelement,displaylable, reportid, contxtpath,Ref_element,"above");
//                subMenu[0].setMenuFunction(menuFunction);
//                subMenu[1] = new Menu("Below");
//                menuFunction = this.getMenuFunctionForRowadd(ViewByelement,displaylable, reportid, contxtpath,Ref_element,"below");
//                subMenu[1].setMenuFunction(menuFunction);
        menu.setSubMenu(subMenu);
        menuLst.add(menu);

        menu = new Menu("Row Coloring");
        menu.setMenuFunction("#");
        subMenu = new Menu[2];
        subMenu[0] = new Menu("Set");
        menuFunction = this.getMenuFunctionForRowColoring(ViewByelement, displaylable, reportid, contxtpath, Ref_element);
        subMenu[0].setMenuFunction(menuFunction);
        subMenu[1] = new Menu("Reset");
        menuFunction = this.getMenuFunctionForREsetRowColor(ViewByelement, displaylable, reportid, contxtpath, Ref_element);
        subMenu[1].setMenuFunction(menuFunction);
        menu.setSubMenu(subMenu);
        menuLst.add(menu);

        menu = new Menu("Edit Format");
        menuFunction = this.getMenuFunctionForFormatting(ViewByelement, displaylable, reportid, contxtpath, Ref_element);
        menu.setMenuFunction(menuFunction);
        menuLst.add(menu);
        return menuLst.toArray(new Menu[]{});
    }

    private Menu createtextkrow(String ViewByelement, String displaylable) {
        Menu menu = new Menu("Text Row");
        menu.setMenuFunction("#");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();

        subMenu = new Menu[2];

        subMenu[0] = new Menu("Above");
        menuFunction = this.getMenuFunctionForRowadd(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "above", "Text");
        subMenu[0].setMenuFunction(menuFunction);
        subMenu[1] = new Menu("Below");
        menuFunction = this.getMenuFunctionForRowadd(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "below", "Text");
        subMenu[1].setMenuFunction(menuFunction);
        menu.setSubMenu(subMenu);
        return menu;
    }

    private Menu createblabkrow(String ViewByelement, String displaylable) {
        Menu menu = new Menu("Blank Row");
        menu.setMenuFunction("#");
        Menu[] subMenu = null;
        String menuFunction;
        ArrayList<Menu> menuLst = new ArrayList<Menu>();

        subMenu = new Menu[2];

        subMenu[0] = new Menu("Above");
        menuFunction = this.getMenuFunctionForRowadd(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "above", "Blank");
        subMenu[0].setMenuFunction(menuFunction);
        subMenu[1] = new Menu("Below");
        menuFunction = this.getMenuFunctionForRowadd(ViewByelement, displaylable, reportid, contxtpath, Ref_element, "below", "Blank");
        subMenu[1].setMenuFunction(menuFunction);
        menu.setSubMenu(subMenu);
        return menu;
    }

    private String getMenuFunctionForRounding(String elementId, String displaylable, String reportId, int precision) {
        String menuFunction = KPI_ROUNDING;
        menuFunction = menuFunction + "('" + elementId + "','" + displaylable + "','" + reportId + "','" + precision + "')";

        return menuFunction;
    }

    private String getMenuFunctionForcomapre(String elementId, boolean edflaag) {
        String menuFunction = COMPARISON;

        menuFunction = menuFunction + "('" + elementId + "','" + edflaag + "')";

        return menuFunction;
    }

    private String getMenuFunctionForNumberFormat(String elementId, String displayLabel, String reportId, String ctxpath, String Ref_element, String numFrmt) {
        String menuFunction = NUMBER_FORMAT;

        menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + Ref_element + "','" + numFrmt + "')";

        return menuFunction;
    }

    private String getMenuFunctionForRowadd(String elementId, String displayLabel, String reportId, String ctxpath, String Ref_element, String rposition, String type) {
        String menuFunction = ROWADD_ABOVE_BELOW;

        menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + Ref_element + "','" + rposition + "','" + type + "')";

        return menuFunction;
    }

    private String getMenuFunctionForSorting(String elementId, String displayLabel, String reportId, String ctxpath, String Ref_element) {
        String menuFunction = SORT_FUNCTION_KPI;

        menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + Ref_element + "')";

        return menuFunction;
    }

//  private String getMenuFunctionForGtupdate(String elementId, String displayLabel, String reportId, String ctxpath,String Ref_element, String gttype) {
//        String menuFunction = UPDATEGT_TYPE;
//
//            menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + Ref_element + "','" + gttype + "')";
//
//        return menuFunction;
//    }
    //bhargavi
    private String getMenuFunctionForgt(String elementId, String displayLabel, String reportId, String ctxpath, String Ref_element) {
        String menuFunction = UPDATEGT_TYPE1;

        menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + Ref_element + "')";

        return menuFunction;
    }

    private String getMenuFunctionForRowColoring(String elementId, String displayLabel, String reportId, String ctxpath, String Ref_element) {
        String menuFunction = ROW_COLOR;
        menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + Ref_element + "')";

        return menuFunction;
    }

    private String getMenuFunctionForREsetRowColor(String elementId, String displayLabel, String reportId, String ctxpath, String Ref_element) {
        String menuFunction = RESET_ROWCOLOR;
        menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + Ref_element + "')";

        return menuFunction;
    }

    private String getMenuFunctionForFormatting(String elementId, String displayLabel, String reportId, String ctxpath, String Ref_element) {

        String menuFunction = EDIT_FORMAT;
        menuFunction = menuFunction + "('" + elementId + "','" + displayLabel + "','" + reportId + "','" + ctxpath + "','" + Ref_element + "','" + fontsizego + "','" + textaligngo + "','" + fontweightgo + "')";

        return menuFunction;
    }

    private StringBuilder getSubMenu(Menu[] subMenu) {
        StringBuilder HTMLString = new StringBuilder();

        HTMLString.append("<ul style=\"width:150px;padding:5px 62px 0 0px;margin-top:-5px;\">");

        String menuName;
        String str = "";
        String menuFunction;
        for (int i = 0; i < subMenu.length; i++) {
            menuName = subMenu[i].getMenuEntry();
            menuFunction = subMenu[i].getMenuFunction();
            //subMenuHtml.append("<li><a href=\"#\" onClick=\"parent.doCallOperation(document.getElementById('HMENU_").append(j).append("'),'").append(columnName).append("',0,'").append(dataType).append("','").append(container.getTableId()).append("')\">Sort Ascend</a></li>");
            HTMLString.append("<li><a href=\"javascript:" + menuFunction + "\">" + menuName + "</a>");
            HTMLString.append(this.getSubMenu(subMenu[i].getSubMenu()));

            HTMLString.append("</li>");
        }
        HTMLString.append("</ul>");
        return HTMLString;
    }

    private StringBuilder getaddingrow(Container container, String ViewByelement, int MsrsLngth, String dispStyle, Menu[] tableMenu, int CloumnCount, String rowaddtrue1) {
        int CloumnCount1 = CloumnCount + 2;
        if (container.isReportCrosstab()) {
            CloumnCount1 = CloumnCount + 3;
        }
        StringBuilder subMenuHtml = new StringBuilder();
        subMenuHtml.append("<Tr id=\"rowId" + MsrsLngth + "\"><td width=\"5px;\" style=\"display:" + dispStyle + ";\">");
        subMenuHtml.append("<ul class=\"dropDownMenu\" style=\"width:5px;\">");
        subMenuHtml.append("<li style=\"width:100%;margin-left:-18px;\"><a href=\"#\" class=\"ui-icon ui-icon-star\" style=\"padding:0px;margin-top:-5px;\"></a>");
        subMenuHtml.append("<ul style=\"width:150px;padding:5px 62px 0 0px;margin-top:-15px;\">");
        String menuName;
        String menuFunction;
        for (Menu menu : tableMenu) {
            menuName = menu.getMenuEntry();
            menuFunction = menu.getMenuFunction();
            subMenuHtml.append("<li><a href=\"javascript:" + menuFunction + "\">" + menuName + "</a>");
            subMenuHtml.append(this.getSubMenu(menu.getSubMenu()));
            subMenuHtml.append("</li>");

        }

        subMenuHtml.append("</ul>");
        subMenuHtml.append("</li>");

//          subMenuHtml.append("/ul>");
        subMenuHtml.append("</td>");
        if (rowaddtrue1 != null && rowaddtrue1.equalsIgnoreCase("text")) {
            subMenuHtml.append("<td colSpan=" + CloumnCount1 + " ><input type='text' style='width:100%;' value='" + container.getRowText().get(ViewByelement) + "' onKeyPress=\"requiredValue(this.value,'" + ViewByelement + "')\" id=text_" + ViewByelement + " ></td>");
        } else {
            subMenuHtml.append("<td colSpan=" + CloumnCount1 + " ></td>");

        }
        subMenuHtml.append("</tr>");
        return subMenuHtml;
    }

    public void createSubTotalRow(ArrayList subTotalCol, int MsrsLngth, Container container) {
        BigDecimal subTotalValue = BigDecimal.ZERO;
        String formattedData = "";
        String element = "";
        String aggType = "";
        int counter = 0;

        DataFacade facade = new DataFacade(container);
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId;

        if (facade.isReportCrosstab()) {
            element = container.getDisplayColumns().get(MsrsLngth);
            element = crosstabMeasureId.get(element);
        } else {
            element = container.getDisplayColumns().get(MsrsLngth);
        }

        for (int i = 0; i < subTotalCol.size(); i++) {
            if ((BigDecimal) subTotalCol.get(i) == null) {
                counter++;
            }
            subTotalValue = subTotalValue.add((BigDecimal) subTotalCol.get(i));
        }
        aggType = (String) container.getAggregationType(element);

        if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
            if (new BigDecimal(subTotalCol.size()).equals(new BigDecimal("0")) || new BigDecimal(subTotalCol.size()).equals(new BigDecimal("0.0"))) {
                subTotalValue = subTotalValue.divide(new BigDecimal("0.0"), 2, RoundingMode.CEILING);
            } else {
                subTotalValue = subTotalValue.divide(new BigDecimal(subTotalCol.size()), 2, RoundingMode.CEILING);
            }
            //subTotalValue = subTotalValue.divide(new BigDecimal(subTotalCol.size()), RoundingMode.HALF_UP);
        }

        formattedData = facade.formatMeasureData(subTotalValue, MsrsLngth);
        subTotalForkpidb1.put(MsrsLngth, formattedData);
    }

    public void setDisplayColumnskpi(Container container, ArrayList mesureIds, int CloumnCount) {

        int count = 1;

        DataFacade facade = new DataFacade(container);
        ArrayList<String> DisplayColumns = container.getDisplayColumns();
        ArrayList<String> DisplayLabelskpi = container.getDisplayLabelskpi();
        DisplayColumnskpi.add(DisplayColumns.get(0));
        ViewSequence.add(0);
        for (int i = 1; i <= mesureIds.size(); i++) {
            for (int MsrsLngth = i; MsrsLngth < DisplayLabelskpi.size() - mesureIds.size(); MsrsLngth = MsrsLngth + mesureIds.size()) {

                ArrayList<String> MeasureData = new ArrayList<String>();
                ArrayList<BigDecimal> subTotalCol = new ArrayList<BigDecimal>();
                DisplayColumnskpi.add(DisplayColumns.get(MsrsLngth));
                ViewSequence.add(MsrsLngth);
                BigDecimal subTotalValue = BigDecimal.ZERO;

                for (int j = 0; j < CloumnCount; j++) {

                    int actualRow = facade.getActualRow(j);
                    String FormattedMsrdata = facade.getFormattedMeasureData(actualRow, MsrsLngth);
                    BigDecimal Msrdata = facade.getFormattedMeasureDataForkpi(actualRow, MsrsLngth);
                    MeasureData.add(FormattedMsrdata);
                    subTotalCol.add(Msrdata);
                }
                MeasureDataMap.put(count, MeasureData);

                for (int n = 0; n < subTotalCol.size(); n++) {

                    subTotalValue = subTotalValue.add((BigDecimal) subTotalCol.get(n));

                }
                subTotalForkpidb.put(count, subTotalValue);
                count++;

            }
        }
    }

    private StringBuilder setRowSubTotalHtml(int CloumnCount, int MsrsLngth, int subTotalRowcount, ArrayList mesureIds, int stCount) {

        StringBuilder subMenuHtml = new StringBuilder();
        ArrayList<String> DisplayLabelskpi = container.getDisplayLabelskpi();

        for (int i = 1; i <= mesureIds.size(); i++) {

            subTotalForST = BigDecimal.ZERO;
            ArrayList<String> FormatedRowSubTotal = new ArrayList<String>();
            ArrayList<BigDecimal> RowSubTotalValues = new ArrayList<BigDecimal>();
            for (int MsrsLngth1 = i; MsrsLngth1 < DisplayLabelskpi.size() - mesureIds.size(); MsrsLngth1 = MsrsLngth1 + mesureIds.size()) {


                ArrayList<BigDecimal> subTotalCol = new ArrayList<BigDecimal>();

                for (int j = 0; j < CloumnCount; j++) {

                    int actualRow = facade.getActualRow(j);
                    BigDecimal Msrdata = facade.getFormattedMeasureDataForkpi(actualRow, MsrsLngth1);

                    subTotalCol.add(Msrdata);
                }

                if (RowSubTotalValues.isEmpty()) {
                    RowSubTotalValues.addAll(subTotalCol);
                    for (int n = 0; n < subTotalCol.size(); n++) {
                        FormatedRowSubTotal.add(facade.formatMeasureData(RowSubTotalValues.get(n), MsrsLngth1));
                    }
                } else {
                    for (int n = 0; n < subTotalCol.size(); n++) {
                        RowSubTotalValues.set(n, RowSubTotalValues.get(n).add((BigDecimal) subTotalCol.get(n)));
                        //  FormatedRowSubTotal.set(n, facade.formatMeasureData(RowSubTotalValues.get(n), MsrsLngth1));
                    }
                }


            }
            String aggType = (String) container.getAggregationType(mesureIds.get(i - 1).toString());

            for (int j = 0; j < RowSubTotalValues.size(); j++) {
                if (aggType.equalsIgnoreCase("avg") || aggType.toUpperCase().contains("avg")) {
                    RowSubTotalValues.set(j, RowSubTotalValues.get(j).divide(new BigDecimal(stCount), RoundingMode.HALF_UP));
                }

                subTotalForST = subTotalForST.add((BigDecimal) RowSubTotalValues.get(j));
                FormatedRowSubTotal.set(j, facade.formatMeasureDataForkpi(RowSubTotalValues.get(j), mesureIds.get(i - 1).toString()));
            }

            subTotalForSTList.add(subTotalForST);
            RowSubTotalValuesMap.put(i, FormatedRowSubTotal);
        }

        if (subTotalRowcount <= RowSubTotalValuesMap.size()) {
            subMenuHtml.append("<tr id=\"rowId1_" + MsrsLngth + "\"><td width=\"5px;\"></td>");
            subMenuHtml.append("<td colSpan=2 style=\"font-weight:bold;\">Sub Total</td>");
            for (int i = 0; i < CloumnCount; i++) {
                subMenuHtml.append("<td height=\"25\" style=\"font-weight:bold; text-align:center;\">" + RowSubTotalValuesMap.get(subTotalRowcount).get(i) + "</td>");
            }
            if (container.getGrandTotalReqForkpi()) {
                subMenuHtml.append("<td height=\"25\" style=\"font-weight:bold; text-align:center;\">" + facade.formatMeasureDataForkpi(rowSubTotalForGT, mesureIds.get(subTotalRowcount - 1).toString()) + "</td>");
            }
            if (container.getNetTotalReqForkpi()) {
                subMenuHtml.append("<td height=\"25\" style=\"font-weight:bold; text-align:center;\">" + facade.formatMeasureDataForkpi(subTotalForSTList.get(subTotalRowcount - 1), mesureIds.get(subTotalRowcount - 1).toString()) + "</td>");
            }
            subMenuHtml.append("</tr>");
            rowSubTotalForGT = BigDecimal.ZERO;

        }
        return subMenuHtml;
    }
//    public TableDataRow getRowDataForKpi(int rowNum) {
//        TableDataRow row = new TableDataRow();
//        ArrayList dataTypes = facade.getDataTypes();
//        ArrayList<String> displayCols = facade.getDisplayColumns();
//        ArrayList<Object> rowData = new ArrayList<Object>();
//        ArrayList<String> fontColorList = new ArrayList<String>();
//        ArrayList<String> commentList = new ArrayList<String>();
//        ArrayList<Boolean> editableList = new ArrayList<Boolean>();
//        ArrayList<String> textAlignList = new ArrayList<String>();
//        HashMap<String, ArrayList<Double>> rowdataMap = new HashMap();
//        HashMap<String, String> finalCrossTabReportDrillMap = ((PbReturnObject) facade.container.getRetObj()).finalCrossTabReportDrillMap;
//
//        int actualRow = facade.getActualRow(rowNum - 1);
//        facade.setActualrow(actualRow);
//        String colViewby = null;
//        Object cellData = "";
//        String bgColor = "";
//        String fontColor = "";
//        String measureType = "";
//        boolean isDimension;
//        String cellData2 = "";
//        String reportDrillUrl = "";
//        HashMap<String, ArrayList> nonViewByMapNew = null;
//        StringBuffer tempReportDrill = new StringBuffer();
//        ArrayList<String> reportDrillList = new ArrayList<String>();
//        ArrayList<String> colVallist = new ArrayList<String>();
//        List<String> summarizedlist = null;
//        nonViewByMapNew = ((PbReturnObject) facade.container.getRetObj()).nonViewByMapNew;
//        //Gson gson = new Gson();
//        Type tarType = new TypeToken<List<String>>() {
//        }.getType();
//        List cellDataList = new ArrayList();
//        List cellDataLst = new ArrayList();
//        ArrayList<String> Measures = new ArrayList<String>();
//
//        for (int i = 0; i < container.getDisplayColumns().size(); i++) {
//            if (container.isReportCrosstab()) {
//                if (i < ViewSequence.size()) {
//                    String element = displayCols.get(ViewSequence.get(i));
//                    if (!element.contains("_")) {
//                        String actualMsrs = ((PbReturnObject) facade.container.getRetObj()).crosstabMeasureId.get(element);
//                        if (!Measures.contains(actualMsrs)) {
//                            Measures.add(actualMsrs);
//                        }
//                        ArrayList<Double> RowDatavalues = new ArrayList();
//                        BigDecimal value = BigDecimal.ZERO;
//                        RowDatavalues = rowdataMap.get(actualMsrs);
//                        if (RowDatavalues == null) {
//                            RowDatavalues = new ArrayList();
//                        }
//                        double RwValue = facade.getValue(actualRow, element);
//                        facade.getFormattedMeasureData(rowNum, i);
////                    if(!CtAvgdata.isEmpty()){
////                          value=new BigDecimal(CtAvgdata.replaceAll(",", ""));
////                     }
//
//                        RowDatavalues.add(RwValue);
//                        Collections.sort(RowDatavalues);
//                        //Collections.sort(RowDatavalues);
//                        rowdataMap.put(actualMsrs, RowDatavalues);
//
////                    facade.rowdataMap=rowdataMap;
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < container.getDisplayColumns().size(); i++) {
//            if (i < ViewSequence.size()) {
//                String finalrepDrillUrl = facade.getContextPath() + "/reportViewer.do?reportBy=viewReport";
//                String element = displayCols.get(ViewSequence.get(i));
//
//                if (i < facade.getViewByCount()) {
//                    isDimension = true;
//                } else {
//                    isDimension = false;
//                }
//
//                if (isDimension) {
//                    if ("D".equals(dataTypes.get(ViewSequence.get(i)))) {
//                        cellData = facade.getDateData(actualRow, element);
//                        cellDataList.clear();
//                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");  //fetched format
//                        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); //new format..
//                        Date date = null;
//                        try {
//                            if (cellData != null && !cellData.toString().equalsIgnoreCase("null") && !cellData.toString().isEmpty() && !cellData.toString().equalsIgnoreCase("")) {
//                                date = format1.parse(cellData.toString().trim());
//                                cellData = format2.format(date);
//                                cellData2 = cellData.toString();
//                                String cellData1 = cellData.toString();
//                                cellData1 = cellData1.trim();
//                                String[] dateandtime = cellData1.split(" ");
//                                cellData = dateandtime[0];
//                            } else {
//                                cellData = "";
//                            }
//                        } catch (ParseException ex) {
//                            logger.error("Exception: ", ex);
//                        }
//
//                        cellDataList.add(cellData);
//                        if (element.equalsIgnoreCase("TIME")) {
//                            reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(facade.container.getReportCollect().timeDetailsArray) + cellData + "&DDrillAcross=Y";
//                        } else {
//                            reportDrillUrl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
//                        }
//
//                    } else {
//                        cellData = facade.getDimensionData(actualRow, element);
//                        cellDataList.clear();
//                        cellDataList.add(cellData);
//                        if (element.equalsIgnoreCase("TIME")) {
//                            reportDrillUrl += facade.container.getReportCollect().resolveTimeDrill(facade.container.getReportCollect().timeDetailsArray) + cellData + "&DDrillAcross=Y";
//                        } else {
//                            reportDrillUrl += "&CBOARP" + element.replace("A_", "") + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataList, tarType));
//                        }
//                    }
//
//                } else {
//                    if (facade.container.isReportCrosstab()) {
//                        colViewby = (String) facade.container.getReportCollect().reportColViewbyValues.get(0);
//                        if (finalCrossTabReportDrillMap.containsKey(element)) {
//                            finalrepDrillUrl += "&REPORTID=" + finalCrossTabReportDrillMap.get(element);
//                        }
//                    } else {
//                        finalrepDrillUrl += "&REPORTID=" + facade.container.getReportDrillMap(element);
//                    }
//
//                    if (nonViewByMapNew != null && nonViewByMapNew.size() > 0 && nonViewByMapNew.containsKey(element)) {
////                    cellDataList.clear();
////                    cellDataList.add(cellData);
//                        cellDataLst.clear();
//                        colVallist = (ArrayList<String>) nonViewByMapNew.get(element);
//                        cellDataLst.add((String) colVallist.get(0));
//                        if (colViewby.equalsIgnoreCase("TIME")) {
//                            tempReportDrill.append(facade.container.getReportCollect().resolveTimeDrill(facade.container.getReportCollect().timeDetailsArray) + (String) colVallist.get(0) + "&DDrillAcross=Y");
//                        } else {
//                            if (facade.container.getSummerizedTableHashMap() != null && !facade.container.getSummerizedTableHashMap().isEmpty()) {
//                                HashMap summarizedmMesMap = facade.container.getSummerizedTableHashMap();
//                                summarizedlist = (List<String>) summarizedmMesMap.get("summerizedQryeIds");
//                            }
//                            if (summarizedlist == null || !summarizedlist.contains(element.toString().replace("A_", ""))) {
//                                tempReportDrill.append("&CBOARP" + colViewby + "=" + java.net.URLEncoder.encode(gson.toJson(cellDataLst, tarType)));
//                            }
//                        }
//                    }
//                    if ("D".equals(dataTypes.get(i))) {
//                        cellData = facade.getDateData(actualRow, element);
//                        cellDataList.clear();
//                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");  //fetched format
//                        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); //new format..
//                        Date date = null;
//                        try {
//                            if (cellData != null && !cellData.toString().equalsIgnoreCase("null") && !cellData.toString().isEmpty() && !cellData.toString().equalsIgnoreCase("")) {
//                                date = format1.parse(cellData.toString().trim());
//                                cellData = format2.format(date);
//                                cellData2 = cellData.toString();
//                                String cellData1 = cellData.toString();
//                                cellData1 = cellData1.trim();
//                                String[] dateandtime = cellData1.split(" ");      //added by mayank on 12/05/14(Split date and time.)
//                                cellData = dateandtime[0];
//                            } else {
//                                cellData = "";
//                            }
//                            cellDataList.add(cellData);
//
//                        } catch (ParseException ex) {
//                            logger.error("Exception: ", ex);
//                        }
//                    } else {
//                        cellData = facade.getFormattedMeasureData(actualRow, i);
//                        cellDataList.clear();
//                        cellDataList.add(cellData);
//                    }
//                }
//                cellData = facade.getFormattedMeasureData(actualRow, i);
//
//                rowData.add(cellData);
//                finalrepDrillUrl += reportDrillUrl + tempReportDrill.toString() + "&reportDrill=Y";
//
//                if (facade.container.isReportCrosstab()) {
//
//                    if (!finalCrossTabReportDrillMap.containsKey(element)) {
//                        reportDrillList.add("#");
//                    } else if (finalCrossTabReportDrillMap.get(element) != null && finalCrossTabReportDrillMap.get(element).equalsIgnoreCase("0")) {
//                        reportDrillList.add("#");
//                    } else {
//                        reportDrillList.add(finalrepDrillUrl);
//                    }
//                } else {
//                    if (!facade.container.getReportCollect().reportDrillMap.containsKey(element)) {
//                        reportDrillList.add("#");
//                    } else if (facade.container.getReportDrillMap(element) != null && facade.container.getReportDrillMap(element).equalsIgnoreCase("0")) {
//                        reportDrillList.add("#");
//                    } else {
//                        reportDrillList.add(finalrepDrillUrl);
//                    }
//                }
//                tempReportDrill = new StringBuffer();
//            }
//        }
//        row.setRowData(rowData);
//        return row;
//    }

    public static String getDate(Calendar cal) {
        return "" + cal.get(Calendar.DATE) + "/"
                + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    }

    public String getexactdate(PbReportCollection reportCollect, String type) throws ParseException {
        String curentdate = ((ArrayList<String>) container.getReportCollect().timeDetailsArray).get(2);
        Calendar cal = Calendar.getInstance();
        int month = Integer.parseInt(curentdate.substring(0, 2));
        int datev = Integer.parseInt(curentdate.substring(3, 5));
        int year = Integer.parseInt(curentdate.substring(6, 10));
        String datezone = datev + "-" + month + "-" + year;

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = df.parse(datezone);
        cal.setTime(date1);
        if (type != null && type.equalsIgnoreCase("month")) {
            cal.add(Calendar.MONTH, -1);
        } else if (type != null && type.equalsIgnoreCase("year")) {
            cal.add(Calendar.YEAR, -1);
        } else if (type != null && type.equalsIgnoreCase("qtr")) {
        }
//cal.add(datev,0);
//cal.add(year,0);
        String date = getDate(cal);
// date=df1.parse(date).toString();
        date1 = df1.parse(date);
        Date date111 = new Date();
        String DATE_FORMAT = "dd-MM-yyyy";
//     SimpleDateFormat sdf = new SimpleDateFormat("EEE,dd,MMM,yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
//Date date1q=null;
//      date= sdf.format(date.toString().trim());
        date = format2.format(date1);
        date = date.substring(6, 10) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2);
        return date;
    }
}
