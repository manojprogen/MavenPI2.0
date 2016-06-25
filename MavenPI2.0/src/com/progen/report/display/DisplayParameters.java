package com.progen.report.display;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.i18n.TranslaterHelper;
import com.progen.report.PbReportCollection;
import com.progen.report.data.DataFacade;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.users.UserLayerDAO;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import org.apache.log4j.Logger;
import prg.business.group.BusinessGroupDAO;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenParam;

public class DisplayParameters {

    ProgenParam f1 = new ProgenParam();
    public int totalParam = 0;
//    String result = "";
    StringBuilder result = new StringBuilder();
    PbDb pbDb = new PbDb();
    DisplayParametersResourceBundle resundle = new DisplayParametersResourceBundle();
    BusinessGroupDAO bgdao = new BusinessGroupDAO();
    String tempPass = "";
    public String completeUrl1 = "";
    public boolean isReport = true;
    public String checked = "checked";
    public boolean whatIfFlag = false;
    private int userId;
    private boolean editDashboard = false;
    public boolean haveProgenTime = true;
    public String filePath = "";
    public static Logger logger = Logger.getLogger(DisplayParameters.class);

    public String getParameterQuery(String elementID) {
        PbReturnObject retObj = null;
        String[] colNames;
        String temp;
        String sqlstr = "";
        String finalQuery = "";
        Object[] Obj = null;
        try {
            sqlstr = resundle.getString("getParameterQuery2");
            Obj = new Object[1];
            Obj[0] = elementID;

            finalQuery = pbDb.buildQuery(sqlstr, Obj);

            retObj = pbDb.execSelectSQL(finalQuery);

            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();


            if (psize > 0) {

//                Start of code by Nazneen in March2014 for Dimension Segmentation
                String userColType = "";
                String formulaVal = "";
                userColType = retObj.getFieldValueString(0, colNames[4]);
                if (userColType != null && userColType.equalsIgnoreCase("CALCULATED")) {
                    PbReportViewerDAO bd = new PbReportViewerDAO();
                    formulaVal = bd.readFormulaFromFile(elementID, this.filePath);
                    sqlstr = "";
                    sqlstr += "select distinct " + formulaVal;
                    sqlstr += " A1 ,  " + formulaVal;
                    sqlstr += " A2 FROM " + retObj.getFieldValueString(0, colNames[5]);
                    sqlstr += " WHERE " + retObj.getFieldValueString(0, colNames[5]) + "." + retObj.getFieldValueString(0, colNames[6]) + " is not null ";
//                    sqlstr += " where " + retObj.getFieldValueString(0, colNames[5])+"."+ retObj.getFieldValue(0, colNames[0]) + " is not null ";
                } //              End of code by Nazneen in March2014 for Dimension Segmentation
                else if (!retObj.getFieldValueString(0, colNames[3]).equalsIgnoreCase("")) {
                    sqlstr = "";
                    sqlstr += "select distinct  " + retObj.getFieldValue(0, colNames[0]);
                    sqlstr += " A1 ,  " + retObj.getFieldValue(0, colNames[0]);
                    sqlstr += "  A2   FROM (" + retObj.getFieldUnknown(0, 3);
                    sqlstr += " ) O1 where " + retObj.getFieldValue(0, colNames[0]) + " is not null ";
                    tempPass = retObj.getFieldValue(0, colNames[2]).toString();
                } else {
                    sqlstr = "";
                    sqlstr += "select distinct  " + retObj.getFieldValue(0, colNames[0]);
                    sqlstr += " A1 ,  " + retObj.getFieldValue(0, colNames[0]);
                    sqlstr += "  A2   " + bgdao.viewBussDataWithColSingle(retObj.getFieldValue(0, colNames[1]).toString());
                    sqlstr += " where " + retObj.getFieldValue(0, colNames[0]) + " is not null ";
                    tempPass = retObj.getFieldValue(0, colNames[2]).toString();
                }


            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return (sqlstr);
    }

    public String getParameterQueryNew(String elementID) {
        PbReturnObject retObj = null;
        String[] colNames;
        String temp;
        String sqlstr = "";
        String finalQuery = "";


        Object[] Obj = null;
        try {
            sqlstr = resundle.getString("getParameterQuery1");
            Obj = new Object[1];
            Obj[0] = elementID;

            finalQuery = pbDb.buildQuery(sqlstr, Obj);

            retObj = pbDb.execSelectSQL(finalQuery);

            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();
            if (psize > 0) {
                sqlstr = "";
                sqlstr += "select distinct  " + retObj.getFieldValue(0, colNames[0]);
                sqlstr += " A1 ,  " + retObj.getFieldValue(0, colNames[0]);
                sqlstr += "  A2   " + bgdao.viewBussDataWithColSingle(retObj.getFieldValue(0, colNames[1]).toString());
                sqlstr += " where " + retObj.getFieldValue(0, colNames[0]) + " is not null ";

                tempPass = retObj.getFieldValue(0, colNames[2]).toString();
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return (sqlstr);
    }

    public String getParameterInfo(String elementID) {
        PbReturnObject retObj = null;
        String[] colNames;
        String temp;
        String sqlstr = "";
        String finalQuery = "";


        Object[] Obj = null;
        try {
            sqlstr = resundle.getString("getParameterQuery1");
            Obj = new Object[1];
            Obj[0] = elementID;

            finalQuery = pbDb.buildQuery(sqlstr, Obj);

            retObj = pbDb.execSelectSQL(finalQuery);

            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();
            if (psize > 0) {

                tempPass = retObj.getFieldValue(0, colNames[2]).toString();
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return ("");
    }

    public String displayParams(HashMap hm) throws SQLException, Exception {


        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

        this.totalParam = a1.length;
        for (int i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) hm.get(a1[i]);

            if (i == 0) {
                result.append("<Tr style=\"width:100%\">");
            } else if (i % 4 == 0) {
                result.append("</Tr><Tr style=\"width:100%\">");
            }
            if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
                result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(9).toString(), (String) a.get(1), (String) a.get(8))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH")) {

                result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb(a.get(9).toString(), (String) a.get(1), getParameterQuery(a1[i]), (String) a.get(8))).append(" </Td> ");

            } else if (a.get(5) == null) {

                result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb(a.get(9).toString(), (String) a.get(1), getParameterQuery(a1[i]), (String) a.get(8))).append(" </Td> ");

            } else {
                if (a.get(5).toString().equalsIgnoreCase("Combo(Without All)")) {
                    result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb(a.get(9).toString(), (String) a.get(1), getParameterQuery(a1[i]), (String) a.get(8))).append(" </Td> ");
                } else {
                    //result += " <Td align=\"right\"> " + f1.getMultiTextBoxNew (a.get(9).toString(),(String)a.get(1),factQry.setQuery(a1[i],hm), (String)a.get(8)) + " </Td> ";
                    result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getMultiTextBoxNew(a.get(9).toString(), (String) a.get(1), (String) a.get(8), a1[i], (String) a.get(10))).append(" </Td> ");

                }
            }
        }


        return result.toString();
    }

    public String displayTimeParams(HashMap hm) throws SQLException, Exception {
        // String result1 = "<Table width=\"80%\">";
        StringBuilder result1 = new StringBuilder();
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

        this.totalParam += a1.length;
        int i = 0;
        for (i = 0; i < a1.length; i++) {
            if ((i + 1) % 4 == 1) {
                result1.append("<Tr style=\"width:100%\">");
            }
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1")).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringDuration((String) a.get(0), null)).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                ArrayList b = (ArrayList) hm.get("PRG_PERIOD_TYPE");
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), null)).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

            }
            if ((i + 1) % 4 == 0) {
                result1.append("</Tr>");
            }
        }
        if ((i) % 4 != 0) {
            result1.append("</Tr>");
        }
        result1.append("");
        return result1.toString();
    }

    public String displayViewBys(HashMap hm, HashMap ParameterMap) {
        // String result1 = "<Table width=\"80%\">";
//        String result1 = "";
        StringBuilder result1 = new StringBuilder();
        ArrayList empty = new ArrayList();
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

        this.totalParam += a1.length;
        int i = 0;
        for (i = 0; i < a1.length; i++) {
            if ((i + 1) % 4 == 1) {
                result1.append("<Tr style=\"width:100%\">");
            }
            ArrayList a = (ArrayList) hm.get(a1[i]);

            ArrayList comboValues = new ArrayList();
            ArrayList comboIds = new ArrayList();
            String currVal = null;
            if (a.get(2) != null) {
                currVal = a.get(2).toString();
            } else {
                currVal = a.get(1).toString();
            }
            if (!currVal.equalsIgnoreCase("Time")) {
                currVal = currVal;
            }
            for (int j = 3; j < a.size(); j++) {
                if (!a.get(j).toString().equalsIgnoreCase("Time")) {
                    comboIds.add(a.get(j));
                    comboValues.add(getElementName(a.get(j).toString(), ParameterMap));
                } else {
                    if (haveProgenTime) {
                        comboIds.add("TIME");
                        comboValues.add("Time");

                    }

                }


            }


            //getCombotbUsingArray
            result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getCombotbUsingArray("CBOVIEW_BY" + a1[i], "View By", comboIds, comboValues, currVal, empty)).append(" </Td> ");
            result1.append("<input name=\"Submit\" type=\"Button\" class=\"navtitle-hover\" value=\"    Go    \"  onclick=\"submitform();\">");



            if ((i + 1) % 4 == 0) {
                result1.append("</Tr>");
            }
        }
        if ((i) % 4 != 0) {
            result1.append("</Tr>");
        }

        result1.append("");
        return result1.toString();

    }

    public String displayAllParams(HashMap repParameters, HashMap timehm, HashMap viewhm, LinkedHashMap viewOrder, String elementId, String restPath, String contextPath, String selectedParams, HashMap parameterHashMap, String userType) throws SQLException, Exception {

        int j = 1;
        String swapUrl = "";
        String firstParam = "";
        String lastParamValue = "";
        String[] a1;
        int i = 0;
        ArrayList sPramList = new ArrayList();
        String[] selectedParamsArr = null;
        UserLayerDAO userdao = new UserLayerDAO();

        result.append("<Table width=\"100%\"> <tr><td width=\"90%\"> <table width=\"100%\">");
        f1.elementId = elementId;
        this.totalParam = 0;
        if (selectedParams != null && !selectedParams.isEmpty()) {
            selectedParamsArr = selectedParams.split(",");
            if (selectedParamsArr != null) {
                for (int k = 0; k < selectedParamsArr.length; k++) {
                    sPramList.add(selectedParamsArr[k]);
                }
            }
        }
        if (timehm != null && timehm.size() > 0) {
            a1 = (String[]) (timehm.keySet()).toArray(new String[0]);

            if (a1.length > 2) {
                if (a1[0].contentEquals("AS_OF_DATE") || a1[1].contentEquals("AS_OF_DATE") || a1[2].contentEquals("AS_OF_DATE")) {
                    ArrayList<String> arrli = new ArrayList<String>();
                    arrli.addAll(Arrays.asList(a1));
                    arrli.set(0, "AS_OF_DATE");
                    arrli.set(1, "PRG_PERIOD_TYPE");
                    arrli.set(2, "PRG_COMPARE");
                    arrli.toArray(a1);
                }
            } else {
                if (a1[1].contentEquals("PRG_DAY_ROLLING")) {
                    ArrayList<String> arrli = new ArrayList<String>();
                    arrli.addAll(Arrays.asList(a1));
                    arrli.set(0, "AS_OF_DATE");
                    arrli.set(1, "PRG_DAY_ROLLING");
                    arrli.toArray(a1);
                } else {
                    if (a1[0].contentEquals("AS_OF_DATE") || a1[1].contentEquals("AS_OF_DATE")) {
                        ArrayList<String> arrli = new ArrayList<String>();
                        arrli.addAll(Arrays.asList(a1));
                        arrli.set(0, "AS_OF_DATE");
                        arrli.set(1, "PRG_PERIOD_TYPE");
                        arrli.set(2, "PRG_COMPARE");
                        arrli.toArray(a1);
                    }
                }

            }

            this.totalParam += a1.length;
            i = 0;
            for (i = 0; i < a1.length; i++) {
                if ((j) % 4 == 1) {
                    result.append("<Tr style=\"width:100%\">");
                }
                ArrayList a = (ArrayList) timehm.get(a1[i]);
                if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker")).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DATE1")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1")).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DATE2")) {
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker2")).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker3")).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker4")).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DMONTH1")) {
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0))).append(" </Td> ");
                    //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_DMONTH2")) {
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DMONTH1")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DQUARTER1")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0))).append(" </Td> ");
                    //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_DQUARTER2")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DQUARTER1")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CQ_CUST_NAME, CQ_CUST_NAME, CQTR, cyear from pr_day_denom order by  cyear desc, CQTR desc ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CQ_CUST_NAME, CQ_CUST_NAME, CQTR, cyear from pr_day_denom order by  cyear desc, CQTR desc ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DYEAR1")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");
                    //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_DYEAR2")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringDuration((String) a.get(0), null)).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getRollingPeriodLOV((String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                    ArrayList b = (ArrayList) timehm.get("PRG_PERIOD_TYPE");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), null)).append(" </Td> ");

                    // result += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStringCompare((String) a.get(0)) + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH")) {

                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH1")) {

                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0))).append(" </Td> ");

                }
                if ((j) % 4 == 0) {
                    result.append("</Tr>");
                }
                j++;
            }

            result.append("");
            haveProgenTime = true;
        } else {
            haveProgenTime = false;

        }
        j = 1;

        int oldParam = this.totalParam;
        if (repParameters != null && repParameters.size() > 0) {
            a1 = (String[]) (repParameters.keySet()).toArray(new String[0]);



            this.totalParam = a1.length;

            for (i = 0; i < a1.length; i++) {
                ArrayList paramInfo = (ArrayList) repParameters.get(a1[i]);

                if (j % 4 == 1) {
                    result.append("<Tr style=\"width:100%\">");
                }
                if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
                    result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(paramInfo.get(9).toString(), (String) paramInfo.get(1), (String) paramInfo.get(8))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH")) {

                    result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb(paramInfo.get(9).toString(), (String) paramInfo.get(1), getParameterQuery(a1[i]), (String) paramInfo.get(8))).append(" </Td> ");

                } else if (paramInfo.get(5) == null) {

                    result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb(paramInfo.get(9).toString(), (String) paramInfo.get(1), getParameterQuery(a1[i]), (String) paramInfo.get(8))).append(" </Td> ");

                } else {
                    if (paramInfo.get(5).toString().equalsIgnoreCase("Combo(Without All)")) {
                        result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb(paramInfo.get(9).toString(), (String) paramInfo.get(1), getParameterQuery(a1[i]), (String) paramInfo.get(8))).append(" </Td> ");
                    } else {
                        //result += " <Td align=\"right\"> " + f1.getMultiTextBoxNew (a.get(9).toString(),(String)a.get(1),factQry.setQuery(a1[i],hm), (String)a.get(8)) + " </Td> ";
                        result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getMultiTextBoxNew(paramInfo.get(9).toString(), paramInfo.get(1).toString(), paramInfo.get(8).toString(), a1[i], paramInfo.get(10).toString())).append(" </Td> ");

                    }
                }
                if (j % 4 == 0) {
                    result.append("</Tr>");
                }
                j++;
            }

        }

        //if ((i+oldParam) % 4 != 0) {
        //    result += "</Tr>";
        // }
        result.append(" ");



        a1 = (String[]) (viewOrder.keySet()).toArray(new String[0]);

        oldParam = this.totalParam;
        this.totalParam += a1.length;
        i = 0;
        for (i = 0; i < a1.length; i++) {
            if (j % 4 == 1) {
                result.append("<Tr style=\"width:100%\">");
            }
            ArrayList a = (ArrayList) viewhm.get(a1[i]);
            ArrayList a2 = (ArrayList) viewOrder.get(a1[i]);
            ArrayList comboValues = new ArrayList();
            ArrayList comboIds = new ArrayList();
            String currVal = null;
            if (a.get(2) != null) {
                currVal = a.get(2).toString();
            } else {
                currVal = a.get(1).toString();
            }
            if (!currVal.equalsIgnoreCase("Time")) {
                currVal = currVal;
            }
            if (a1.length > 1) {
                if (i == 0) {
                    swapUrl = "";
                    firstParam = "CBOVIEW_BY" + a1[i];
                    lastParamValue = currVal;
                } else {
                    swapUrl += "&CBOVIEW_BY" + a1[i] + "=" + lastParamValue;
                    //firstParam ="CBOVIEW_BY" + a1[i];
                    lastParamValue = currVal;

                }

            }

            ArrayList vals = new ArrayList();
            Set setvals = repParameters.keySet();
            Iterator val = setvals.iterator();
            while (val.hasNext()) {
                vals.add(val.next());
            }
            vals.add("Time");

            for (int k = 0; k < vals.size(); k++) {
                if (!vals.get(k).toString().equalsIgnoreCase("Time")) {
                    comboIds.add(vals.get(k));
                    comboValues.add(getElementName(vals.get(k).toString(), repParameters));
                } else {
                    if (haveProgenTime) {
                        comboIds.add("TIME");
                        comboValues.add("Time");

                    }

                }


            }
            for (int i1 = 0; i1 < sPramList.size(); i1++) {
                comboIds.remove(sPramList.get(i1));
                comboValues.remove(getElementName(sPramList.get(i1).toString(), repParameters));
            }

            //getCombotbUsingArray
            result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getCombotbUsingArray("CBOVIEW_BY" + a1[i], a2.get(0).toString() + a2.get(1).toString(), comboIds, comboValues, currVal, sPramList)).append(" </Td> ");
            result.append(" <input name=\"txtcompleteurl\" id=\"txtcompleteurl\" type=hidden value=\"").append(completeUrl1).append("\">");



            if (j % 4 == 0) {
                result.append("</Tr>");
            }
            j++;
        }


        if ((j) % 4 != 0) {
            result.append("</Tr>");
        }


        result.append("</table>");
        result.append(" </td><td width=\"10%\"> <table><tr>");
        if (whatIfFlag) {
            if (isReport) {
                result.append("<td><input name=\"Submit\" type=\"Button\" class=\"navtitle-hover\" value=\"    Go    \"  onclick=\"submitform2();\"> </td>");
                if (a1.length > 1) {
                    swapUrl += "&" + firstParam + "=" + lastParamValue;

                    //  result += "</TR><TR><td><input name=\"SwapViewBy\" type=\"Button\" class=\"navtitle-hover\" value=\"Swap ViewBy\"  onclick=\"submiturls22('" + swapUrl + "');\"> </td>";
                }
            }
        } else {
            if (isReport) {
                if (userdao.getFeatureEnableHashMap("Add Inner View By", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-pencil\" title=\"Add Inner Viewbys\" onclick=\"AddInnerViewbys('").append(contextPath).append("')\"></a></td></tr><tr>");
                }
                if (userdao.getFeatureEnableHashMap("Parameter Save", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-disk\" title=\"Save Parameters\" onclick=\"saveParamSection('").append(contextPath).append("')\"></a></td></tr><tr>");
                }
                if (userdao.getFeatureEnableHashMap("Remove Parameters", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-minusthick\" title=\"Remove Parameters\" onclick=\"RemoveMoreDims('").append(contextPath).append("')\"></a></td></tr><tr>");
                }
                if (userdao.getFeatureEnableHashMap("Add Parameters", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-plusthick\" title=\"Add More Parameters\" onclick=\"AddMoreDims('").append(contextPath).append("')\"></a></td></tr><tr>");
                }
                result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-arrowthick-2-n-s\" title=\"Sequence Parameters\" onclick=\"parent.sequenceParams('").append(contextPath).append("')\"></a></td></tr><tr>");

                result.append("<td><input name=\"Submit\" type=\"Button\" class=\"navtitle-hover\" value=\"    Go    \"  onclick=\"submitform();\"> </td>");
                if (a1.length > 1) {
                    swapUrl += "&" + firstParam + "=" + lastParamValue;

                    //  result += "</TR><TR><td><input name=\"SwapViewBy\" type=\"Button\" class=\"navtitle-hover\" value=\"Swap ViewBy\"  onclick=\"submiturls2('" + swapUrl + "');\"> </td>";
                }
            } else {
                result.append("<td><input name=\"Submit\" type=\"Button\" class=\"navtitle-hover\" value=\"    Go    \"  onclick=\"submitdashboard();\"> </td>");

            }
        }

        //add Edit View By Button if it's a report
        if (isReport) {//userdao.getFeatureEnableHashMap("Edit View By",parameterHashMap)
            if (userdao.getFeatureEnableHashMap("Edit View By", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
//            if(PrivilegeManager.isComponentEnabledForUser("REPORT", "EDITVIEWBY", userId)){
                result.append("</tr><tr><td><input name=\"Edit\" type=\"Button\" class=\"navtitle-hover\" value=\"Edit ViewBy\" style=\"width:auto\" onclick=\"editViewBy();\"> </td></tr>");
            }
        }

        boolean editDbrd = this.editDashboard;
        if (!editDbrd == true) {
            if (isReport) {
                if (userdao.getFeatureEnableHashMap("Reset", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                    result.append("</tr><tr><td><A href=\"").append(restPath).append("\" onclick=\"openImgDiv()\"> Reset </A></td></tr>");
                }
            } else {
                restPath += "&resetFlag=" + true;
                result.append("</tr><tr><td><A href=\"").append(restPath).append("\"> Reset </A></td></tr>");
            }
            //result += "</tr><tr><td><a onclick=\"resetDashboard('"+restPath+"')\"> Reset </a></td></tr>";
        }
        result.append("</table>");
        result.append("</table>");

        return result.toString();
    }

    public String displayParamwithTime(ArrayList elementList, HashMap timehm) {
        StringBuilder result1 = new StringBuilder();
//        String result1 = "<Table width=\"80%\">";
        result1.append("<Table width=\"80%\">");
//        String sqlstr = "";
        StringBuilder sqlstr = new StringBuilder();
        int j = 1;
//        result1 = "<Table width=\"100%\"> <tr><td width=\"80%\"> <table width=\"100%\">";
        result1.append("<Table width=\"100%\"> <tr><td width=\"80%\"> <table width=\"100%\">");
        if (!elementList.isEmpty()) {
            f1.elementId = elementList.get(0).toString();
        }

        this.totalParam = 0;
        String[] a1 = (String[]) (timehm.keySet()).toArray(new String[0]);

        this.totalParam += a1.length;
        int i = 0;
        for (i = 0; i < a1.length; i++) {
            if ((j) % 4 == 1) {
//                result1 += "<Tr style=\"width:100%\">";
                result1.append("<Tr style=\"width:100%\">");
            }
            ArrayList a = (ArrayList) timehm.get(a1[i]);


            if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker") + " </Td> ";
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker")).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_DATE1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker") + " </Td> ";
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker")).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_DATE2")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1")).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1") + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker2")).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker2") + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker3")).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker3") + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("AS_OF_DMONTH1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("AS_OF_DMONTH2")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DMONTH1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("AS_OF_DQUARTER1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("AS_OF_DQUARTER2")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DQUARTER1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {

                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_DYEAR1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");
                //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("AS_OF_DYEAR2")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringDuration((String) a.get(0), null)).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_DAY_ROLLING")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getRollingPeriodLOV((String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                ArrayList b = (ArrayList) timehm.get("PRG_PERIOD_TYPE");
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), null)).append(" </Td> ");

                //result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStringCompare((String) a.get(0)) + " </Td> ";

            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH")) {

                result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH1")) {

                result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR ", (String) a.get(0))).append(" </Td> ");

            }
            if ((j) % 4 == 0) {
                result1.append("</Tr>");
            }
            j++;
        }

        result1.append("");
        if (!elementList.isEmpty()) {
            for (i = 0; i < elementList.size(); i++) {
                try {
                    //String sqlstr = this.getParameterQuery((String)elementList.get(i));
                    if (j % 4 == 1) {
                        result1.append("<Tr style=\"width:100%\">");
                    }
                    //getParameterInfo(elementList.get(i).toString());
                    sqlstr.append(" select A1 , A2 from ( select RANK() over(order by A1,A2) AS num1 , A1 , A2 from ( ").append(getParameterQuery(elementList.get(i).toString())).append(" ) O3 )O4 where num1 between 1 and 100 ");
//                       sqlstr = " select A1 , A2 from ( select RANK() over(order by A1,A2) AS num1 , A1 , A2 from ( " + getParameterQuery(elementList.get(i).toString()) + " ) O3 )O4 where num1 between 1 and 100 ";
                    //////.println("sqlstr "+sqlstr);
                    f1.elementId = elementList.get(i).toString();
                    //result1 += "<Td>" + f1.getMultiTextBoxNew("CBOAR" + elementList.get(i).toString(), this.tempPass, "All", elementList.get(i).toString()) + "</Td>";
                    result1.append(" <Td align=\"right\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryAllCombotb("CBOAR" + elementList.get(i).toString(), this.tempPass, sqlstr.toString(), elementList.get(i).toString())).append(" </Td> ");
                    if (j % 4 == 0) {
                        result1.append("</Tr>");
                    }
                    j++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
        if ((j) % 4 != 0) {
            result1.append("</Tr>");
        }


        result1.append("</table>");


        return result1.toString();

    }

    //added by susheela start to preview the secured parameter LOV in RD
    public String displayParamwithTime(ArrayList elementList, HashMap timehm, String reportId) {
        //added by susheela start
        String getsecuredValuesForReportQ = "select * from prg_ar_parameter_security where report_id=" + reportId;
        HashMap secMap = new HashMap();
        HashMap eleColMap = new HashMap();
        PbReturnObject pbro2 = new PbReturnObject();
        HashMap lMap = new HashMap();
        ArrayList dimElements = new ArrayList();
        HashMap valuesMap = new HashMap();
        HashMap ParentBussCol = new HashMap();
        ArrayList lBusCols = new ArrayList();
        HashMap membersEle = new HashMap();
        HashMap memberFilters = new HashMap();
        ArrayList filterMemIds = new ArrayList();
        PbReportCollection prc = new PbReportCollection();
        try {


            ArrayList Parameters = elementList;
            String allMEmberSecForRep = "select * from PRG_AR_PARAMETER_SECURITY where report_id=" + reportId;
            PbDb pbdb = new PbDb();
            PbReturnObject filterObj = pbdb.execSelectSQL(allMEmberSecForRep);

            String otherMembers = "";
            StringBuilder allFilteredEle = new StringBuilder(500);
//            String allFilteredEle = "";
            for (int m = 0; m < filterObj.getRowCount(); m++) {
                String memId2 = (filterObj.getFieldValueString(m, "MEMBER_ID"));
                String prevClause = "";
                String newClause = "";
                String totalClause = "";
                if (memberFilters.containsKey(memId2)) {
                    prevClause = (String) memberFilters.get(memId2);
                    newClause = filterObj.getFieldValueClobString(m, "MEMBER_VALUE");
                    totalClause = prevClause + "," + newClause;
                } else {
                    memberFilters.put(filterObj.getFieldValueString(m, "MEMBER_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                    filterMemIds.add(filterObj.getFieldValueString(m, "MEMBER_ID"));
                }
                valuesMap.put(filterObj.getFieldValueString(m, "ELEMENT_ID"), filterObj.getFieldValueClobString(m, "MEMBER_VALUE"));
                // allFilteredEle=allFilteredEle+","+filterObj.getFieldValueString(m,"ELEMENT_ID");
            }
            for (int g = 0; g < Parameters.size(); g++) {
//                allFilteredEle = allFilteredEle + "," + Parameters.get(g).toString();
                allFilteredEle.append(",").append(Parameters.get(g).toString());
            }
            if (allFilteredEle.length() > 0) {
//                allFilteredEle = allFilteredEle.substring(1);
                allFilteredEle = new StringBuilder(allFilteredEle.substring(1));
            }
            String allEleInfo = "select * from prg_user_all_info_details where element_id in(" + allFilteredEle + ")";

            pbro2 = pbdb.execSelectSQL(allEleInfo);


        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        //added by susheela over
        StringBuilder result1 = new StringBuilder();
        result1.append("<Table width=\"80%\">");
        String sqlstr = "";

        int j = 1;
        result1.append("<Table width=\"100%\"> <tr><td width=\"80%\"> <table width=\"100%\">");

        f1.elementId = elementList.get(0).toString();

        this.totalParam = 0;
        int i = 0;
        if (timehm != null && timehm.size() > 0) {
            String[] a1 = (String[]) (timehm.keySet()).toArray(new String[0]);

            this.totalParam += a1.length;

            for (i = 0; i < a1.length; i++) {
                if ((j) % 4 == 1) {
                    result1.append("<Tr style=\"width:100%\">");
                }
                ArrayList a = (ArrayList) timehm.get(a1[i]);


                if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker") + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker")).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_DATE1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1") + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1")).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_DATE2")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker2") + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker2")).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DATE1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker3") + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker3")).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DATE2")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker4") + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker4")).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_DMONTH1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                    //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_DMONTH2")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DMONTH1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_DQUARTER1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_DQUARTER2")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DQUARTER1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_DYEAR1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0)) + " </Td> ";
                    //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_DYEAR2")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
//                result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0)) + " </Td> ";
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getRollingPeriodLOV((String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringDuration((String) a.get(0), null)).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                    ArrayList b = (ArrayList) timehm.get("PRG_PERIOD_TYPE");
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), null)).append(" </Td> ");

                    //result1 += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStringCompare((String) a.get(0)) + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                    result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH")) {

                    result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR  ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH1")) {

                    result.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, CMONTH, CM_YEAR from pr_day_denom order by  CMONTH, CM_YEAR ", (String) a.get(0))).append(" </Td> ");

                }
                if ((j) % 4 == 0) {

                    result1.append("</Tr>");
                }
                j++;
            }
            haveProgenTime = true;
        } else {
            haveProgenTime = false;
        }
        result1.append("");
        for (i = 0; i < elementList.size(); i++) {
            try {
                //String sqlstr = this.getParameterQuery((String)elementList.get(i));
                if (j % 4 == 1) {
                    result1.append("<Tr style=\"width:100%\">");
                }
                //getParameterInfo(elementList.get(i).toString());
                lBusCols = new ArrayList();
                lMap = new HashMap();

//                String filterClause = "";
                String eleId = elementList.get(i).toString();
                String eleIdQuery = "";
                //added by susheela start 01-02-10
                String selectedEleDim = "";
                dimElements = new ArrayList();
                for (int m = 0; m < pbro2.getRowCount(); m++) {
                    String localDim = pbro2.getFieldValueString(m, "DIM_ID");
                    String localEleId = pbro2.getFieldValueString(m, "ELEMENT_ID");
                    if (localEleId.equalsIgnoreCase(eleId)) {
                        selectedEleDim = localDim;
                    }
                    if (!selectedEleDim.equalsIgnoreCase("")) {
                        break;
                    }
                }
                for (int m = 0; m < pbro2.getRowCount(); m++) {
                    String localDim = pbro2.getFieldValueString(m, "DIM_ID");
                    String localEleId = pbro2.getFieldValueString(m, "ELEMENT_ID");
                    if (selectedEleDim.equalsIgnoreCase(localDim)) {
                        dimElements.add(localEleId);
                    }
                }


                for (int p = 0; p < dimElements.size(); p++) {
                    String parId = dimElements.get(p).toString();

                    if (valuesMap.containsKey(parId)) {
                        String paramValues = (String) valuesMap.get(parId);

                        if (!paramValues.equalsIgnoreCase("All")) {
                            lMap.put(parId, paramValues);
                            if (ParentBussCol.containsKey(parId)) {
                                String pBussCol = (String) ParentBussCol.get(parId);
                                lBusCols.add(pBussCol);
                            }
                        } else if (membersEle.containsKey(parId)) {
                            String pBussCol = (String) ParentBussCol.get(parId);
                            lBusCols.add(pBussCol);
                            String newSecuredVal = "";
                            String mId = "";
                            mId = membersEle.get(parId).toString();

                            newSecuredVal = memberFilters.get(mId).toString();
                            lMap.put(parId, newSecuredVal);
                        }
                    }
                }
                DisplayParameters dispParam = new DisplayParameters();
                String mainCaluse = "";

                String whereClause = prc.getWhereClause(lMap, lBusCols);




                /*
                 * if(secMap.containsKey(eleId)) { String values=(String)
                 * secMap.get(eleId); values = values.replace("'", "''"); values
                 * = "'" + values + "'"; values = values.replace(",", "','");
                 * values = values.replace("'||chr(38)||'", "||chr(38)||");
                 * if(eleColMap.containsKey(eleId)) { String colName=(String)
                 * eleColMap.get(eleId); filterClause=" and "+colName +"
                 * in("+values+")"; } }
                 *
                 */
                eleIdQuery = getParameterQuery(eleId) + whereClause;



                //   over
                //sqlstr = "select A1 , A2 from ( " + eleIdQuery + " ) where rownum between 1 and 100 ";
                sqlstr = " select A1 , A2 from ( select RANK() over(order by A1,A2) AS num1 , A1 , A2 from ( " + eleIdQuery + " ) O3 ) O4 where num1 between 1 and 100 ";
                //////.println("sqlstr "+sqlstr);

                f1.elementId = elementList.get(i).toString();

                //result1 += "<Td>" + f1.getMultiTextBoxNew("CBOAR" + elementList.get(i).toString(), this.tempPass, "All", elementList.get(i).toString()) + "</Td>";
                result1.append(" <Td align=\"right\" id=column" + i + " align=\"right\"> " + f1.getQueryAllCombotb("CBOAR" + elementList.get(i).toString(), this.tempPass, sqlstr, elementList.get(i).toString()) + " </Td> ");
                if (j % 4 == 0) {
//                    result1 += "</Tr>";
                    result1.append("</Tr>");
                }
                j++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
        if ((j) % 4 != 0) {
//            result1 += "</Tr>";
            result1.append("</Tr>");
        }
        result1.append("</table>");


        return (result1).toString();

    }
    //added by susheela over

    public String displayParam(ArrayList elementList) {
        StringBuilder result1 = new StringBuilder();
        result1.append("<Table width=\"80%\">");
        String sqlstr = "";
        int i;

        for (i = 0; i < elementList.size(); i++) {
            try {
                //String sqlstr = this.getParameterQuery((String)elementList.get(i));
                if ((i + 1) % 4 == 1) {
                    result1.append("<Tr style=\"width:100%\">");
                }
                //getParameterInfo(elementList.get(i).toString());
                sqlstr = getParameterQuery(elementList.get(i).toString());

                f1.elementId = elementList.get(i).toString();
                //result1 += "<Td>" + f1.getMultiTextBoxNew("CBOAR" + elementList.get(i).toString(), this.tempPass, "All", elementList.get(i).toString()) + "</Td>";
                result1.append(" <Td align=\"right\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryAllCombotb("CBOAR" + elementList.get(i).toString(), this.tempPass, sqlstr, elementList.get(i).toString())).append(" </Td> ");
                if ((i + 1) % 4 == 0) {
                    result1.append("</Tr>");
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        if ((i) % 4 != 0) {
            result1.append("</Tr>");
        }
        result1.append("</Table>");

        return (result1).toString();
    }

    public String getElementName(String elementId, HashMap reportParameters) {
        ArrayList paraInfo = new ArrayList();
        String NextElementId = null;
        if (elementId != null && elementId.equalsIgnoreCase("Time")) {
            NextElementId = "TIME";
        } else {
            paraInfo = (ArrayList) reportParameters.get(elementId);
            if (paraInfo != null) {
                if (paraInfo.get(1) != null) {
                    NextElementId = paraInfo.get(1).toString();
                }
            }

        }

        return (NextElementId);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String displayTimeParamsForScorecardTracker(HashMap hm) {
        StringBuilder result1 = new StringBuilder();
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

        this.totalParam += a1.length;
        int i = 0;
        for (i = 0; i < a1.length; i++) {
            if ((i + 1) % 4 == 1) {
                result1.append("<Tr style=''>");
            }
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
                result1.append(" <Td class='myhead' style='width:33%' id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1")).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                result1.append(" <Td class='myhead' style='width:24%' id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringDuration((String) a.get(0), null)).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                ArrayList b = (ArrayList) hm.get("PRG_PERIOD_TYPE");
                // result += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStringCompare((String) a.get(0),(String) b.get(0)) + " </Td> ";

                result1.append(" <Td class='myhead' style='width:36%'  id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), null)).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result1.append(" <Td class='myhead' style='width:24%' id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

            }
            if ((i + 1) % 4 == 0) {
                result1.append("</Tr>");
            }
        }
        if ((i) % 4 != 0) {
            result1.append("</Tr>");
        }
        result1.append("");
        return result1.toString();
    }

    /**
     * @return the editDashboard
     */
    public boolean isEditDashboard() {
        return editDashboard;
    }

    /**
     * @param editDashboard the editDashboard to set
     */
    public void setEditDashboard(boolean editDashboard) {
        this.editDashboard = editDashboard;
    }

    public String displayTimeParamsForDashboard(HashMap hm, String resetPath) throws SQLException, Exception {
        StringBuilder result1 = new StringBuilder();
        result1.append("<Table width=\"100%\"> <tr><td width=\"90%\"> <table width=\"100%\">");
        //String result1 = "";
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

        this.totalParam += a1.length;
        int i = 0;
        for (i = 0; i < a1.length; i++) {
            if ((i + 1) % 4 == 1) {
                result1.append("<Tr style=\"width:25%\">");
            }
            ArrayList a = (ArrayList) hm.get(a1[i]);
            if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
                result1.append(" <Td width=\"5%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker")).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                result1.append(" <Td width=\"5%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringDuration((String) a.get(0), null)).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                ArrayList b = (ArrayList) hm.get("PRG_PERIOD_TYPE");
                // result += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStringCompare((String) a.get(0),(String) b.get(0)) + " </Td> ";

                result1.append(" <Td width=\"5%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), null)).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result1.append(" <Td width=\"5%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

            }
            if ((i + 1) % 4 == 0) {
                result1.append("</Tr>");
            }
        }

        if ((i) % 4 != 0) {
            result1.append("</Tr>");
        }
        result1.append("</Table></td>");
        result1.append("<Td><Table><Tr></Tr><Tr>");
        result1.append("<td><input name=\"Submit\" type=\"Button\" class=\"navtitle-hover\" value=\"    Go    \"  onclick=\"submitdashboard();\"> </td>");
        result1.append("</Tr>");
        boolean editDbrd = this.editDashboard;
        if (!editDbrd == true) {
            resetPath += "&resetFlag=" + true;
            result1.append("<tr><td><A href=\"").append(resetPath).append("\"> Reset </A></td></tr></table></Td></Tr>");
        }
        result1.append("</Table>");

        result1.append("");
        return result1.toString();
    }

    public String displayTime(HashMap hm) throws SQLException, Exception {
        // String result1 = "<Table width=\"80%\">";
//        String result1 = "";
        StringBuilder result1 = new StringBuilder();
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

        this.totalParam += a1.length;
        int i = 0;
        for (i = 0; i < a1.length; i++) {
            ArrayList a = (ArrayList) hm.get(a1[i]);

            if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\" style=\" padding-left: 1.5em\"> ").append(f1.getStdStringDuration((String) a.get(0), "fixdate")).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                ArrayList b = (ArrayList) hm.get("PRG_PERIOD_TYPE");
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\" style=\" padding-left: 1.5em\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), "fixdate")).append(" </Td> ");
            } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                result1.append(" <Td width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

            } else if (a1[i].equalsIgnoreCase("PRG_DAY_ROLLING")) {
                result1.append(" <Td  style=\" padding-left: 1.5em;white-space: nowrap\" width=\"25%\" id=column").append(i).append(" align=\"right\"> ").append(f1.getRollingPeriodLOV((String) a.get(0))).append(" </Td> ");

            }
        }

        result1.append("");
        return result1.toString();
    }
    //started by Nazneen

    public String getParamFilter(String userId, String reportId, String eleId) throws SQLException {

        String tabQ = "select BUSS_TABLE_ID from prg_user_all_info_details  where ELEMENT_ID =" + eleId;
        PbReturnObject tabObj = pbDb.execSelectSQL(tabQ);
        String tableId = tabObj.getFieldValueString(0, 0);

        String tabFilter = "";
        String filterTypeDate = "";
//        ProgenLog.log(ProgenLog.FINE, this, "getSecFactFilter", "Enter ");
        logger.info("Enter ");
        PbReturnObject retObj;
        String[] colNames = null;
        String aList = "";

        String sqlstr = "";
        sqlstr += "select sec_clause_1 ,sec_clause_2,sec_clause_3,sec_clause_4,sec_clause_5, buss_tab_col_name ,sec_key_name ";
        sqlstr += " ";
        sqlstr += " from PRG_SEC_GRP_ROLE_USER_VAR  ";
        sqlstr += "  M ";
        sqlstr += " where buss_table_id = ( " + tableId + ") and folder_id = -1 "; //Add folder sepcific code too

        logger.info("PRG_SEC_GRP_ROLE_USER_VAR " + sqlstr);

        retObj = pbDb.execSelectSQL(sqlstr);

        int psize = retObj.getRowCount();

        if (psize > 0) {
            colNames = retObj.getColumnNames();
//            String filterFromTable = null;
            StringBuilder filterFromTable = new StringBuilder();
            //Looping twice
            //Loop 1 find the fact and current and prior cols
            //loop 2 build query
            for (int looper = 0; looper < psize; looper++) {
                if (retObj.getFieldValue(looper, 0) != null && !retObj.getFieldValueString(looper, 0).trim().equals("")) {
                    if (looper == 0) {
                        filterFromTable.append(" and ( 1= 1  and ");
                        filterFromTable.append(retObj.getFieldValueString(looper, colNames[5]));
                        filterFromTable.append(" in ( " + retObj.getFieldValueString(looper, colNames[0]));
                        filterFromTable.append(" ) ");

                    } else {

//                     filterFromTable =  " and " + filterFromTable + retObj.getFieldValueString(looper, colNames[5]) ;
                        filterFromTable.append( " and " + retObj.getFieldValueString(looper, colNames[5]));
                        filterFromTable.append( " in ( " + retObj.getFieldValueString(looper, colNames[0]));
                        filterFromTable.append( " ) ");
                    }
                }
                //    
            }
            if (filterFromTable != null && !filterFromTable.toString().trim().equals("")) {
//                filterFromTable.append(" and ) ");//comment by ram
                 filterFromTable.append("  ) ");
                tabFilter = filterFromTable.toString();
                tabFilter = tabFilter.replace("@@PROGEN_GBL_VAR@@USER_ID", userId);
                logger.info("filterFromTable: " + filterFromTable);
                logger.info("tabFilter: " + tabFilter);
                aList = (tabFilter);
            } else {
                aList = aList;
            }
        } else {
            aList = aList + (tabFilter);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getSecFactFilter", "Exit " + tabFilter);
        logger.info("Exit " + tabFilter);
        logger.info("tabFilter: " + tabFilter);
        return (aList);
//        return (tabFilter);
    }
//by Nazneen for Cross Dim Security

    public String getCrossDimFilter(String userId, String reportId, String eleId, HashMap valuesMap) throws SQLException {
        ArrayList secElementId = new ArrayList();
        ArrayList bussTabColName = new ArrayList();
        HashMap dimDetails = new HashMap();
        PbReturnObject pbro = new PbReturnObject();
        PbDb pbDb = new PbDb();
        PbDb pbDb1 = new PbDb();
        PbReportCollection prc = new PbReportCollection();
        StringBuilder filterFromTable = new StringBuilder();
        String sqlstr = "";

//        HashMap det = prc.getCrossDimElementId(eleId, reportId,tableId);

        String tabQ = "SELECT BUSS_TABLE_ID FROM prg_user_all_info_details WHERE ELEMENT_ID =" + eleId;
        PbReturnObject tabObj = pbDb.execSelectSQL(tabQ);
        String tableId = tabObj.getFieldValueString(0, 0);
        tabObj = null;
        String tempInVals = "";
        sqlstr = "SELECT SEC_ELEMENT_ID,BUSS_TAB_COL_NAME FROM PRG_CROSS_DIM_SEC_TABLE  WHERE BUSS_TABLE_ID = " + tableId;

        try {
            PbReturnObject retObj = pbDb1.execSelectSQL(sqlstr);
            String colNames[] = retObj.getColumnNames();
            int psize = 0;
            psize = retObj.getRowCount();
            if (psize > 0) {
                colNames = retObj.getColumnNames();

                for (int looper = 0; looper < psize; looper++) {
                    if (retObj.getFieldValue(looper, 0) != null && !retObj.getFieldValueString(looper, 0).trim().equals("")) {
                        tempInVals = prc.getCrossDimEleDetails(retObj.getFieldValueString(looper, colNames[0]), valuesMap);
                        if (tempInVals.length() > 0) {
                            if (looper == 0) {
                                filterFromTable.append("and ( 1=1 and ");
                                filterFromTable.append(retObj.getFieldValueString(looper, colNames[1]));
                                filterFromTable.append( " in ( " + tempInVals);
                                filterFromTable.append(" ) ");

                            } else {
//                        filterFromTable =  " and " + filterFromTable + retObj.getFieldValueString(looper, colNames[5]) ;
//                        filterFromTable =  " and " + filterFromTable + retObj.getFieldValueString(looper, colNames[1]) ;
                                filterFromTable.append( " and " + retObj.getFieldValueString(looper, colNames[1]));
                                filterFromTable.append(" in ( " + tempInVals);
                                filterFromTable.append(" ) ");
                            }
                        } else {
                            if (looper == 0) {
                                filterFromTable.append("( 1=1 ");
                            }
                        }
                    }
                }
            }
            if (filterFromTable != null && !filterFromTable.toString().trim().equals("")) {
//                filterFromTable.append(" and  ) ");
                  filterFromTable.append(" ) ");
            } else {
                filterFromTable.append("");
            } 
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


//        ArrayList secElementId = (ArrayList) det.get("secElementId");
//        ArrayList bussTabColName = (ArrayList) det.get("bussTabColName");
//        HashMap lMap = new HashMap();
//        ArrayList lBusCols = new ArrayList();
//        String val = "";
//        String crossDimFilterClause = "";
//        if(secElementId!=null){
//            String strAry = secElementId.get(0).toString();
//            String secElementIds[] =strAry.split(",");
//
//           if(secElementIds.length > 0){
//            for (int p = 0; p < secElementIds.length ; p++) {
//                String parId = secElementIds[p];
//                if (valuesMap.containsKey(parId)) {
//                 String paramValues = (String) valuesMap.get(parId);
//                   if (!paramValues.equalsIgnoreCase("All")) {
//                             val = val + ",'" + paramValues +"'";
//                   }
//                }
//            }
//            int len = val.length();
//            if(len>0){
//                val = val.substring(1,len);
//                crossDimFilterClause = " and " + bussTabColName.get(0) + " in (" + val + ")";
//            }
//           }
//        }
        return (filterFromTable).toString();

    }

    //ended by Nazneen
    /*
     * @author srikanth.p
     */
    public String getglobalparams(HashMap repParameters, HashMap timehm, HashMap viewhm, LinkedHashMap viewOrder, String elementId, String restPath, String contextPath, String selectedParams, HashMap parameterHashMap, String userType, String reportId, boolean GTAverageBoolean, PbReportCollection collect, String kpidashboard, Container container) throws SQLException, Exception {

        String ParamSectionDisplay = "";
        collect.setisglobalparamkpi(true);
        try {
            ParamSectionDisplay = newDisplayAllParams(collect.reportParameters, collect.timeDetailsMap, collect.reportViewByMain, collect.reportViewByOrder, collect.reportQryElementIds.get(0).toString(), collect.resetPath, contextPath, selectedParams, parameterHashMap, userType, reportId, GTAverageBoolean, collect, kpidashboard, container);
        } catch (SQLException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "getParameterRegionDisplay", "Exception " + ex.getMessage());
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "getParameterRegionDisplay", "Exception " + ex.getMessage());
            logger.error("Exception: ", ex);
        }
        return ParamSectionDisplay;
    }

    public String newDisplayAllParams(HashMap repParameters, HashMap timehm, HashMap viewhm, LinkedHashMap viewOrder, String elementId, String restPath, String contextPath, String selectedParams, HashMap parameterHashMap, String userType, String reportId, boolean GTAverageBoolean, PbReportCollection collect, String kpidashboard, Container container) throws SQLException, Exception {
        Locale locale = container.getLocale();
        int j = 1;
//        String swapUrl = "";
        StringBuilder swapUrl = new StringBuilder();
        String firstParam = "";
        String lastParamValue = "";
        String[] a1;
        String[] showFilter;   // added by Mayank
        int i = 0;
        HashMap inMap;
        HashMap notInMap;
        HashMap notLikeMap;
        HashMap likeMap;
        ArrayList sPramList = new ArrayList();
        String[] selectedParamsArr = null;
        StringBuilder result = new StringBuilder();
        UserLayerDAO userdao = new UserLayerDAO();
        result.append("<Table>");
        result.append("<tr>");
        result.append("<td id='headerTableData' style='height:15%;width:'100%'>");
        result.append("<div id='headerDiv' style='overflow:auto;'>");
        result.append("<Table width='100%' height='100%'> ");
        inMap = (HashMap) collect.operatorFilters.get("IN");
        //added by Bhargavi for showing text box in diff color if not in filters are applied on 15 nov 14
        notInMap = collect.operatorFilters.get("NOTIN");
        notLikeMap = collect.operatorFilters.get("NOTLIKE");
        likeMap = collect.operatorFilters.get("LIKE");
        //ended by Bhargavi for showing text box in diff color if not in filters are applied on 15 nov 14
        f1.elementId = elementId;
        this.totalParam = 0;
        if (selectedParams != null && !selectedParams.isEmpty()) {
            selectedParamsArr = selectedParams.split(",");
            if (selectedParamsArr != null) {
                for (int k = 0; k < selectedParamsArr.length; k++) {
                    sPramList.add(selectedParamsArr[k]);
                }
            }
        }
        a1 = (String[]) (viewOrder.keySet()).toArray(new String[0]);
        result.append("<tr style=\"width:100%\">");
//            result.append("<td width=\"10%\" style=\"background-color: #E5E5E5;height:25px\">"); //style=\"border-bottom:1px dashed;\"
//        eddited by manik    result.append(" <table style=\"width:100%\"><tr>");
        result.append(" <table align=\"right\" style=\"width:100%\">");

//            end of code by manik

        // startof code by mayank for show/hide filters
        if (repParameters != null && repParameters.size() > 0) {
            showFilter = (String[]) (repParameters.keySet()).toArray(new String[0]);
            List<String> currValList1;
          String str = "";
            boolean flg = false;
          for(int filter=0; filter < showFilter.length; filter++) {

           currValList1=(List<String>)inMap.get(showFilter[filter]);
          if(currValList1 != null && !currValList1.isEmpty() && !currValList1.contains("All") && !currValList1.contains("ALL") ){
              flg=true;
              if(str==""){
                  str = currValList1.toString();
              }else{
                   str = str +","+currValList1.toString();
                    }

                }
            }
          if(flg){
                result.append("<tr>");
           result.append(" <td width=\"100%\" style=\"color:black;height:25px;padding-left:1%\"><font style=\"font-family:elephant\">"+TranslaterHelper.getTranslatedInLocale("Applied_Filter", locale)+": </font>"+str+"</td></tr>");
            }

        }

        //end of code

//           result.append("<tr> <td width=\"100%\" style=\"background-color: #E5E5E5;height:25px\"><table align=\"right\" style=\"width:auto;margin-right:2%\"><tr>");
        result.append("<tr> <td width=\"100%\" class='themeColor' style=\"height:15px\"><table align=\"right\" style=\"width:auto;margin-right:2%\"><tr>");
        if (whatIfFlag) {
            if (isReport) {
//          edited by manik          result.append("<td ><input name=\"Submit\" type=\"Button\" class=\"navtitle-hover\" value=\"    Go    \"  onclick=\"submitform2();\"> </td>");
                    result.append("<td ><input name=\"Submit\" type=\"Button\" class=\"navtitle-hover\" value=\""+   TranslaterHelper.getTranslatedInLocale("go", locale)     +"  onclick=\"submitform2();\"> </td>");
                if (a1.length > 1) {
                    swapUrl.append("&" + firstParam + "=" + lastParamValue);

                    //  result += "</TR><TR><td><input name=\"SwapViewBy\" type=\"Button\" class=\"navtitle-hover\" value=\"Swap ViewBy\"  onclick=\"submiturls22('" + swapUrl + "');\"> </td>";
                }
            }
        } else {
            //start by mayank for dropdown option
            if (isReport) {
                if (kpidashboard != null && kpidashboard.equalsIgnoreCase("true")) {
                    //result.append("<td   align=\"left\" style=\" display:none; width:90%\"> Global Filter </td>");
                    result.append("<td align=\"center\"><a href=\"javascript:submitformkpi()\" style=\"font-size:8pt;font-style:bold;padding-left:20px\"><font style=\"color:#eee; font-family:elephant\">&nbsp" + TranslaterHelper.getTranslatedInLocale("go", locale) + "</font></a></td>");
                    result.append("<td style=\"text-align: right;\" ><img src=\"images/Close.png\" onclick=\"closeHideParamkpi()\" style=\"margin: -14px; padding-left: 25px;\" alt=\"Close\" align=\"right\"></td>");
                } else {
                    result.append("<td width=\"100%\"><div style=\"height:30px;width:145px\"><ul class=\"navbar color2\" style=\"position:absolute; width:13%\">");
                    result.append("<li class=\"drpdown menu-item\" style=\"width:100%\">");
                    result.append("<a href=\"#\" class=\"\"><span><font size=\"3\" face=\"verdana\" style=\"font-size: 15px; color: white\">" + TranslaterHelper.getTranslatedInLocale("Actions", locale) + "</font></span></a>");
                    result.append("<ul class=\"drpcontent\" style=\"top:30px\">");

                    if (userdao.getFeatureEnableHashMap("Add Inner View By", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
//        result.append("<li><a data-color=\"color1\" style=\"padding:10px 0px 0px 24px;\">Inner Viewbys</a></li>");
                        result.append("<li><a data-color=\"color1\" style=\"padding:0px 0px 0px 24px;\" onclick=\"AddInnerViewbys('" + contextPath + "')\">" + TranslaterHelper.getTranslatedInLocale("Inner_ViewBys", locale) + "</a></li>");
                    }
                    if (userdao.getFeatureEnableHashMap("Add Parameters", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                        result.append("<li><a data-color=\"color1\" style=\"padding:0px 0px 0px 24px;\" onclick=\"AddMoreDims('" + contextPath + "')\">" + TranslaterHelper.getTranslatedInLocale("Add_More_Parameters", locale) + "</a></li>");
                    }
                    if (userdao.getFeatureEnableHashMap("Remove Parameters", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                        result.append("<li><a data-color=\"color1\" style=\"padding:0px 0px 0px 24px;\" onclick=\"RemoveMoreDims('" + contextPath + "')\">" + TranslaterHelper.getTranslatedInLocale("Remove_Parameters", locale) + "</a></li>");
                    }
                    if (userdao.getFeatureEnableHashMap("Parameter Sequence", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                        result.append("<li><a data-color=\"color1\" style=\"padding:0px 0px 0px 24px;\" onclick=\"parent.sequenceParams('" + contextPath + "')\">" + TranslaterHelper.getTranslatedInLocale("Sequence_Parameters", locale) + "</a></li>");
                    }
                    if (userdao.getFeatureEnableHashMap("Parameter Save", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
                        result.append("<li><a data-color=\"color1\" style=\"padding:0px 0px 0px 24px;\" onclick=\"saveParamSection('" + contextPath + "')\">Save Parameters</a></li>");
                    }

                    result.append("</ul></li></ul></div></td>");
                    result.append("<td align=\"center\"><a href=\"javascript:submitform()\" style=\"font:verdana;font-size:8pt;font-style:bold;padding-left:20px\"><font style=\"color:#eee; font-family:elephant\">&nbsp " + TranslaterHelper.getTranslatedInLocale("go", locale) + "</font></a></td>");

                    result.append("<td style=\"text-align: right;\" ><img src=\"images/Close.png\" onclick=\"closeHideParam()\" style=\"margin: -10px; padding-left: 25px;\" alt=\"Close\" align=\"right\"></td>");
                    //        result.append("<td><a href=\"javascript:submitform()\" ><input type=\"submit\" class=\"submitButton\" value=\"SUBMIT\"></a></td>");
                }
            } // end by mayank
            // commented by mayank
            //                if (isReport) {
            //                    if(userdao.getFeatureEnableHashMap("Add Inner View By",parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN"))  {
            ////                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-pencil\" title=\"Add Inner Viewbys\" onclick=\"AddInnerViewbys('" + contextPath + "')\"></a></td>");
            //                    result.append("<td align=\"center\"><a  class=\"\" title=\"Add Inner Viewbys\" onclick=\"AddInnerViewbys('" + contextPath + "')\"><font style=\"color:#eee; font-family:elephant\"> Add Viewbys | </font></a></td>");
            //                    }if(userdao.getFeatureEnableHashMap("Parameter Sequence",parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN"))  {
            ////                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon ui-icon-arrowthick-2-n-s\" title=\"Sequence Parameters\" onclick=\"sequenceParams('" + contextPath + "')\"></a></td>");
            //                    result.append("<td align=\"center\"><a  class=\" \" title=\"Sequence Parameters\" onclick=\"sequenceParams('" + contextPath + "')\"><font style=\"color:#eee; font-family:elephant\">&nbsp Change Sequence | </font></a></td>");
            //                    }if(userdao.getFeatureEnableHashMap("Remove Parameters",parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN"))  {
            ////                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-minusthick\" title=\"Remove Parameters\" onclick=\"RemoveMoreDims('" + contextPath + "')\"></a></td>");
            //                    result.append("<td align=\"center\"><a  class=\"\" title=\"Remove Parameters\" onclick=\"RemoveMoreDims('" + contextPath + "')\"><font style=\"color:#eee; font-family:elephant\">&nbsp Remove Params | </font></a></td>");
            //                    } if (userdao.getFeatureEnableHashMap("Add Parameters", parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN")) {
            ////                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-plusthick\" title=\"Add More Parameters\" onclick=\"AddMoreDims('" + contextPath + "')\"></a></td>");
            //                    result.append("<td align=\"center\"><a  class=\"\" title=\"Add More Parameters\" onclick=\"AddMoreDims('" + contextPath + "')\"><font style=\"color:#eee; font-family:elephant\">&nbsp Add More Params | </font></a></td>");
            //
            //                    }
            //                    //commented by mayank
            ////                if(userdao.getFeatureEnableHashMap("Edit View By",parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN"))  {
            ////                result.append("<td align=\"center\"><a href=\"javascript:editViewBy()\" style=\"font:verdana;font-size:8pt;font-style:bold;\" class=\"ui-icon ui-icon-copy\" title=\"Edit ViewBy\"></a></td>");
            ////                result.append("<td><input name=\"Edit\" type=\"Button\" class=\"navtitle-hover\" value=\"Edit ViewBy\" style=\"width:auto\" onclick=\"editViewBy();\"> </td>");
            ////                 }
            //                if(userdao.getFeatureEnableHashMap("Reset",parameterHashMap) || userType.equalsIgnoreCase("SUPERADMIN"))  {
            //                    result.append("<td align=\"center\"><A href=\"" + restPath + "\" onclick=\"openImgDiv()\" style=\"font:verdana;font-size:8pt;font-style:bold;\"><font style=\"color:#eee; font-family:elephant\">&nbsp Reset | </font></A></td>");
            //                    }
            //                    if(userdao.getFeatureEnableHashMap("Parameter Save",parameterHashMap)|| userType.equalsIgnoreCase("SUPERADMIN"))  {
            ////                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-disk\" title=\"Save Parameters\" onclick=\"saveParamSection('" + contextPath + "')\"></a></td>");
            //                    result.append("<td align=\"center\"><a  class=\"\" title=\"Save Parameters\" onclick=\"saveParamSection('" + contextPath + "')\"><font style=\"color:#eee; font-family:elephant\">&nbsp Save Params | </font></a></td>");
            //                    }
            //
            //                    result.append("<td align=\"center\"><a href=\"javascript:submitform()\" style=\"font:verdana;font-size:8pt;font-style:bold;\"><font style=\"color:#eee; font-family:elephant\">&nbsp GO</font></a></td>");
            //                    if (a1.length > 1) {
            //                        swapUrl += "&" + firstParam + "=" + lastParamValue;
            //
            //                        //  result += "</TR><TR><td><input name=\"SwapViewBy\" type=\"Button\" class=\"navtitle-hover\" value=\"Swap ViewBy\"  onclick=\"submiturls2('" + swapUrl + "');\"> </td>";
            //                    }
            //                }
            else {
                result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-plusthick\" title=\"Add More Parameters\" style=\"font:verdana;font-size:8pt;font-style:bold;color:#eee\" onclick=\"AddMoreDims('" + contextPath + "')\"></a></td>");
                result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-minusthick\" title=\"Remove Parameters\" style=\"font:verdana;font-size:8pt;font-style:bold;color:#eee\" onclick=\"RemoveMoreDims('" + contextPath + "')\"></a></td>");
                result.append("<td align=\"center\"><a href=\"javascript:submitdashboard()\" style=\"font:verdana;font-size:8pt;font-style:bold;color:#eee\">" + TranslaterHelper.getTranslatedInLocale("go", locale) + "</a></td>");
            }
        }

        //add Edit View By Button if it's a report

        boolean editDbrd = this.editDashboard;
        if (!editDbrd == true) {
            if (!isReport) {
                restPath += "&resetFlag=" + true;
                result.append("<td><A href=\"" + restPath + "\" style=\"font:verdana;font-size:8pt;font-style:bold;color:#eee\"> Reset </A></td>");
            }
            //result += "</tr><tr><td><a onclick=\"resetDashboard('"+restPath+"')\"> Reset </a></td></tr>";
        }
//            result.append("<td><a href=\"javascript:void(0)\" class='ui-icon ui-icon-triangle-1-w' onclick=\"closeparamsTab()\"></a></td></tr>");
// edited by manik            result.append("<td align=\"center\" style=\"display:none;\"><input id='numbuerOfViewbys' type='text' type='hidden' value='"+viewOrder.size()+"'></td>");
        result.append("<td align=\"center\" style=\"display:none;\"><input id='numbuerOfViewbys' type='text' type='hidden' value='" + viewOrder.size() + "'></td>");
        result.append("</tr>");
        result.append("</table>");
//            result.append("<hr style=\"color:LightGrey;\"/>");
        result.append("</td>");
        result.append("</tr>");
        if (isReport) {

            if (GTAverageBoolean) {
                checked = "checked";
            } else {
                checked = "";
            }
            //modified by mayank to hide checkbox
//           result.append(" <input type=\"hidden\" name=\"GTAverage\" id=\"GTAverage\" value=\"true\">");
//            result.append("<tr><td><input title=\" GT Average\" type=\"checkbox\" "+checked+" name=\"GTAverage1\" id=\"GTAverage1\" value=\"true\" onclick=\"averageValue()\" </td></tr>");
        }


        //header region Ends


        //view by Region
        String[] newArray = (String[]) (viewOrder.keySet()).toArray(new String[0]);
//            

        int oldParam = this.totalParam;
        this.totalParam += newArray.length;
        i = 0;
        for (i = 0; i < newArray.length; i++) {
            /*
             * if (j % 4 == 1) { result.append( "<Tr style=\"width:100%\">");
            }
             */
            ArrayList a = (ArrayList) viewhm.get(newArray[i]);
            ArrayList a2 = (ArrayList) viewOrder.get(newArray[i]);
            ArrayList comboValues = new ArrayList();
            ArrayList comboIds = new ArrayList();
            String currVal = null;
            if (a.get(2) != null) {
                currVal = a.get(2).toString();
            } else {
                currVal = a.get(1).toString();
            }
            if (!currVal.equalsIgnoreCase("Time")) {
                currVal = currVal;
            }
            if (newArray.length > 1) {
                if (i == 0) {
                    swapUrl.append("");
                    firstParam = "CBOVIEW_BY" + newArray[i];
                    lastParamValue = currVal;
                } else {
                    swapUrl.append("&CBOVIEW_BY" + newArray[i] + "=" + lastParamValue);
                    //firstParam ="CBOVIEW_BY" + a1[i];
                    lastParamValue = currVal;

                }

            }

            ArrayList vals = new ArrayList();
            Set setvals = repParameters.keySet();
            Iterator val = setvals.iterator();
            while (val.hasNext()) {
                vals.add(val.next());
            }
            vals.add("Time");

            for (int k = 0; k < vals.size(); k++) {
                if (!vals.get(k).toString().equalsIgnoreCase("Time")) {
                    comboIds.add(vals.get(k));
                    comboValues.add(getElementName(vals.get(k).toString(), repParameters));
                } else {
                    if (haveProgenTime) {
                        comboIds.add("TIME");
                        comboValues.add("Time");

                    }

                }


            }
            for (int i1 = 0; i1 < sPramList.size(); i1++) {
                comboIds.remove(sPramList.get(i1));
                comboValues.remove(getElementName(sPramList.get(i1).toString(), repParameters));
            }

            //getCombotbUsingArray
//            result.append("<tr style=\"width:100%\">");
//            result.append(" <Td width=\"25%\" id=column" + i + " align=\"left\"> " + f1.getCombotbUsingArray("CBOVIEW_BY" + newArray[i], a2.get(0).toString() + a2.get(1).toString(), comboIds, comboValues, currVal, sPramList) + "");
//            result.append("<input name='txtcompleteurl' id='txtcompleteurl' type=hidden value='" + completeUrl1 + "'></td>" );
//
//
//
//            /*if (j % 4 == 0) {
//            result.append( "</Tr>");
//            }*/
//            result.append("</Tr>");
            j++;
        }
        result.append("</table>");
        result.append("</div>");
        result.append("</td>");
        result.append("</tr>");
        //view by region Ends

        result.append("<tr>");
        result.append("<td style='width:100%;height:85%'>");
        result.append("<div id='paramRegion'style='margin-left:0%;margin-top:1%' class='dynamicClass'>"); //modified by mayank for spacing
        result.append("<table width=\"100%\">");

        //Param Region
        result.append("<tr><td width=\"100%\"> ");
        result.append("<table width=\"100%\">");


        if (timehm != null && timehm.size() > 0) {
            a1 = (String[]) (timehm.keySet()).toArray(new String[0]);

            if (a1.length > 2) {
                if (a1[0].contentEquals("AS_OF_DATE") || a1[1].contentEquals("AS_OF_DATE") || a1[2].contentEquals("AS_OF_DATE")) {
                    ArrayList<String> arrli = new ArrayList<String>();
                    arrli.addAll(Arrays.asList(a1));
                    arrli.set(0, "AS_OF_DATE");
                    arrli.set(1, "PRG_PERIOD_TYPE");
                    arrli.set(2, "PRG_COMPARE");
                    arrli.toArray(a1);
                }
            } else {
                if (a1[1].contentEquals("PRG_DAY_ROLLING")) {
                    ArrayList<String> arrli = new ArrayList<String>();
                    arrli.addAll(Arrays.asList(a1));
                    arrli.set(0, "AS_OF_DATE");
                    arrli.set(1, "PRG_DAY_ROLLING");
                    arrli.toArray(a1);
                } else {
                    if (a1[0].contentEquals("AS_OF_DATE") || a1[1].contentEquals("AS_OF_DATE")) {
                        ArrayList<String> arrli = new ArrayList<String>();
                        arrli.addAll(Arrays.asList(a1));
                        arrli.set(0, "AS_OF_DATE");
                        arrli.set(1, "PRG_PERIOD_TYPE");
                        arrli.set(2, "PRG_COMPARE");
                        arrli.toArray(a1);
                    }
                }

            }





            this.totalParam += a1.length;
            i = 0;
            for (i = 0; i < a1.length; i++) {
                /*
                 * if ((j) % 4 == 1) { result.append( "<Tr
                 * style=\"width:100%\">");
                }
                 */
//start of code by manik for parameter Top region
                if ((i) % 3 == 0) {
                    if (i == 0) {
                        result.append("<Tr style=\"width:100%\">");
                    } else {
                        result.append("</tr><tr style=\"width:100%\">");
                    }
                } else if (i == a1.length - 1) {
                    result.append("</tr>");
                }
//end of code by manik for parameter Top region
                ArrayList a = (ArrayList) timehm.get(a1[i]);
                if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
//                    result.append("<Tr style=\"width:100%\">"); by manik
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker") + " </Td> ");
//                    result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DATE1")) {
//                    result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker1") + " </Td> ");
//                    result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DATE2")) {
//                    result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker2") + " </Td> ");
//                    result.append("</tr>");
//
                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DATE1")) {
//                    result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker3") + " </Td> ");
//                    result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DATE2")) {
//                    result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0), "datepicker4") + " </Td> ");
//                    result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DMONTH1")) {
//                    result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");
                    //   result.append("</tr>");
                    //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_DMONTH2")) {
                    //              result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\"  width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");
                    //               result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DMONTH1")) {
                    //            result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");
                    //            result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DMONTH2")) {
                    //           result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");
                    //           result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DQUARTER1")) {
                    //         result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");
                    //         result.append("</tr>");
                    //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_DQUARTER2")) {
                    //         result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");
                    //        result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DQUARTER1")) {
                    //       result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CQ_CUST_NAME, CQ_CUST_NAME, CQTR, cyear from pr_day_denom order by  cyear desc, CQTR desc ", (String) a.get(0)) + " </Td> ");
                    //      result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    //        result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CQ_CUST_NAME, CQ_CUST_NAME, CQTR, cyear from pr_day_denom order by  cyear desc, CQTR desc ", (String) a.get(0)) + " </Td> ");
                    //      result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("AS_OF_DYEAR1")) {
                    //     result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0)) + " </Td> ");
                    //         result.append("</tr>");
                    //f1.getStdStratDate(a.get(1).toString(), (String) a.get(2), (String) a.get(0),"datepicker1") + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_DYEAR2")) {
                    //          result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0)) + " </Td> ");
                    //          result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DYEAR1")) {
                    //        result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0)) + " </Td> ");
                    //       result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("CMP_AS_OF_DYEAR2")) {
                    //         result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0)) + " </Td> ");
                    //         result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    //        result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStringDuration((String) a.get(0), null) + " </Td> ");
                    //        result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    //         result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getRollingPeriodLOV((String) a.get(0)) + " </Td> ");
                    //        result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                    ArrayList b = (ArrayList) timehm.get("PRG_PERIOD_TYPE");
                    //        result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStringCompare((String) a.get(0), (String) b.get(0), null) + " </Td> ");
                    //         result.append("</tr>");

                    // result += " <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStringCompare((String) a.get(0)) + " </Td> ";

                } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                    //      result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0)) + " </Td> ");
                    //         result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH")) {
                    //       result.append("<Tr style=\"width:100%\">");

                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");
                    //       result.append("</tr>");

                } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH1")) {
                    //        result.append("<Tr style=\"width:100%\">");

                    result.append(" <Td style=\"display:none\" width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct CM_CUST_NAME, CM_CUST_NAME, cmonth, cyear from pr_day_denom order by  cyear desc, cmonth desc ", (String) a.get(0)) + " </Td> ");
                    //        result.append("</tr>");

                }
                /*
                 * if ((j) % 4 == 0) { result.append( "</Tr>");
                }
                 */
                j++;
            }

//            result.append("");
            haveProgenTime = true;
        } else {
            haveProgenTime = false;

        }
        j = 1;

        oldParam = this.totalParam;
        if (repParameters != null && repParameters.size() > 0) {
            a1 = (String[]) (repParameters.keySet()).toArray(new String[0]);



            this.totalParam = a1.length;

            for (i = 0; i < a1.length; i++) {
                ArrayList paramInfo = (ArrayList) repParameters.get(a1[i]);

                /*
                 * if (j % 4 == 1) { result.append( "<Tr
                 * style=\"width:100%\">");
                }
                 */
//start of code by manik for parameter Top region
                if ((i) % 3 == 0) {
                    if (i == 0) {
                        result.append("<Tr style=\"width:100%\">");
                    } else {
                        result.append("</tr><tr style=\"width:100%\">");
                    }
                } else if (i == a1.length - 1) {
                    result.append("</tr>");
                }
//             end of code by manik for parameter Top region
                if (a1[i].equalsIgnoreCase("AS_OF_DATE")) {
                    //      result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getStdStratDate(paramInfo.get(9).toString(), (String) paramInfo.get(1), (String) paramInfo.get(8)) + " </Td> ");
                    //        result.append("</Tr>");

                } else if (a1[i].equalsIgnoreCase("AS_OF_MONTH")) {
                    //       result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb(paramInfo.get(9).toString(), (String) paramInfo.get(1), getParameterQuery(a1[i]), (String) paramInfo.get(8)) + " </Td> ");
                    //      result.append("</Tr>");

                } else if (paramInfo.get(5) == null) {
                    //       result.append("<Tr style=\"width:100%\">");
                    result.append(" <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb(paramInfo.get(9).toString(), (String) paramInfo.get(1), getParameterQuery(a1[i]), (String) paramInfo.get(8)) + " </Td> ");
                    //       result.append("</Tr>");

                } else {
                    if (paramInfo.get(5).toString().equalsIgnoreCase("Combo(Without All)")) {
                        //          result.append("<Tr style=\"width:100%\">");
                        result.append(" <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getQueryCombotb(paramInfo.get(9).toString(), (String) paramInfo.get(1), getParameterQuery(a1[i]), (String) paramInfo.get(8)) + " </Td> ");
                        //         result.append("</Tr>");
                    } else {
                        // result.append("<Tr style=\"width:100%\">");
//                        List<String> includedVals = (List<String>)paramInfo.get(8);
//                        if(inMap!=null && !inMap.isEmpty()){
//                            if(inMap.get(elementId)!=null)
//                            includedVals.add(inMap.get(elementId).toString());
//                        }
                        //result += " <Td align=\"right\"> " + f1.getMultiTextBoxNew (a.get(9).toString(),(String)a.get(1),factQry.setQuery(a1[i],hm), (String)a.get(8)) + " </Td> ";
//                      //started by Bhargavi for showing text box in diff color if not in filters are applied on 15 nov 14
//                        result.append(" <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getMultiTextAreaNew(paramInfo.get(9).toString(), (String) paramInfo.get(1),(List<String>)inMap.get(a1[i]) , a1[i], (String) paramInfo.get(10),reportId,a1[i]) + " </Td> ");
                        result.append(" <Td width=\"25%\" id=column" + i + " align=\"right\"> " + f1.getMultiTextAreaNew(paramInfo.get(9).toString(), (String) paramInfo.get(1), (List<String>) inMap.get(a1[i]), a1[i], (String) paramInfo.get(10), reportId, a1[i], (List<String>) notInMap.get(a1[i]), (List<String>) notLikeMap.get(a1[i]), (List<String>) likeMap.get(a1[i]), container) + " </Td> ");
                        // result.append("</Tr>");

                    }
                }
                /*
                 * if (j % 4 == 0) { result.append( "</Tr>");
                }
                 */
                j++;
            }

        }

        //if ((i+oldParam) % 4 != 0) {
        //    result += "</Tr>";
        // }
//        result.append("");






        /*
         * if ((j) % 4 != 0) { result.append( "</Tr>");
        }
         */


        result.append("</table>");
        result.append("</td>");

        result.append("</tr></table>");
        result.append("</div>");
        result.append("</td></tr></table>");

        return result.toString();

    }
    //sandeep

    public String upadatedrilldates(PbReportCollection collect) throws ParseException {
        String value = "";
        String valu = "";
        String mont = "";
        String CurrValue = "";
        if (collect.reportIncomingParameters.get("fromdate") != null && collect.reportIncomingParameters.get("datetext") != "") {
            value = (String) collect.reportIncomingParameters.get("fromdate");
            if (!value.contains("/") && !value.isEmpty()) {
                String formatdate = collect.parseDate(value);
                valu = formatdate.substring(0, 2);
                mont = formatdate.substring(3, 5);
                CurrValue = valu.concat("/").concat(mont).concat(formatdate.substring(5));
            }
            collect.reportIncomingParameters.put("CBO_AS_OF_DATE1", CurrValue);
        }
        if (collect.reportIncomingParameters.get("todate") != null && collect.reportIncomingParameters.get("datetext") != "") {
            value = (String) collect.reportIncomingParameters.get("todate");
            if (!value.contains("/") && !value.isEmpty()) {
                String formatdate = collect.parseDate(value);
                valu = formatdate.substring(0, 2);
                mont = formatdate.substring(3, 5);
                CurrValue = valu.concat("/").concat(mont).concat(formatdate.substring(5));
            }
            collect.reportIncomingParameters.put("CBO_AS_OF_DATE2", CurrValue);
        }
        if (collect.reportIncomingParameters.get("comparefrom") != null && collect.reportIncomingParameters.get("datetext") != "") {
            value = (String) collect.reportIncomingParameters.get("comparefrom");
            if (!value.contains("/") && !value.isEmpty()) {
                String formatdate = collect.parseDate(value);
                valu = formatdate.substring(0, 2);
                mont = formatdate.substring(3, 5);
                CurrValue = valu.concat("/").concat(mont).concat(formatdate.substring(5));
            }
            collect.reportIncomingParameters.put("CBO_CMP_AS_OF_DATE1", CurrValue);
        }
        if (collect.reportIncomingParameters.get("compareto") != null && collect.reportIncomingParameters.get("datetext") != "") {
            value = (String) collect.reportIncomingParameters.get("compareto");
            if (!value.contains("/") && !value.isEmpty()) {
                String formatdate = collect.parseDate(value);
                valu = formatdate.substring(0, 2);
                mont = formatdate.substring(3, 5);
                CurrValue = valu.concat("/").concat(mont).concat(formatdate.substring(5));
            }
            collect.reportIncomingParameters.put("CBO_CMP_AS_OF_DATE2", CurrValue);
        }
        return null;

    }
    //sandeep

    public String buildfilreregion(PbReportCollection collect, Container container, String action) throws SQLException {

        HashMap viewhm = null;
        HashMap repParameters = collect.reportParameters;
        LinkedHashMap viewOrder = collect.reportViewByOrder;
        if (collect.kpireportViewByMain.size() > 0) {
            viewhm = collect.kpireportViewByMain;
        } else {
//    viewhm=collect.reportViewByMain;
//     collect.kpireportViewByMain=collect.reportViewByMain;
        }
        String rowviewbys;
        if (action != null && (action.equalsIgnoreCase("open") || action.equalsIgnoreCase("reset"))) {
            PbReturnObject retObj = null;
            PbDb db = new PbDb();
            String sqlqry = "select GLOBAL_KPIPARAM, ELEMENT_ID from  PRG_AR_REPORT_PARAM_DETAILS where REPORT_ID=" + collect.reportId + "";
            retObj = db.execSelectSQL(sqlqry);
            int psize = retObj.getRowCount();
            psize = retObj.getRowCount();
            if (psize > 0) {

                for (int looper = 0; looper < psize; looper++) {
                    String globaliletr = retObj.getFieldValueString(looper, "GLOBAL_KPIPARAM");
                    if (globaliletr != null && globaliletr.equalsIgnoreCase("true")) {
                        ArrayList<String> viewBysList2 = collect.kpireportViewByMain.get("global");
                        String elementid = retObj.getFieldValueString(looper, "ELEMENT_ID");

                        viewBysList2.set(1, elementid);
                        collect.kpireportViewByMain.put("global", viewBysList2);
                    }
                }
            }
            viewhm = collect.kpireportViewByMain;
        } else if (collect.reportIncomingParameters.get("CBOVIEW_BY_1") != null) {

            rowviewbys = (String) collect.reportIncomingParameters.get("CBOVIEW_BY_1");
//                   requestParamValues.put("CBOVIEW_BY" + viewById,rowviewbys);
            ArrayList<String> viewBysList1 = collect.kpireportViewByMain.get("global");
            viewBysList1.set(1, rowviewbys);
            collect.kpireportViewByMain.put("global", viewBysList1);
        }
        String boxNamekpi = "";
        String boxNamekpidrill = "";
        ArrayList paramInfo = null;
        String textBoxName = "";
        HashMap inMap;
        inMap = (HashMap) collect.operatorFilters.get("IN");
        int i = 0;
        String[] a1 = null;
// String cbo = "";
        StringBuilder cbo = new StringBuilder(1000);
        String[] newArray = (String[]) (viewOrder.keySet()).toArray(new String[0]);
        for (i = 0; i < newArray.length; i++) {

            ArrayList comboValues = new ArrayList();
            ArrayList comboIds = new ArrayList();
            String currVal = null;
            ArrayList a = (ArrayList) viewhm.get("global");
//            if (a.get(2) != null) {
//                currVal = a.get(2).toString();
//            } else {
            currVal = a.get(1).toString();
//            }
            ArrayList a2 = (ArrayList) viewOrder.get(newArray[i]);
            ArrayList vals = new ArrayList();
            Set setvals = repParameters.keySet();
            Iterator val = setvals.iterator();
            while (val.hasNext()) {
                vals.add(val.next());
            }
            for (int k = 0; k < vals.size(); k++) {
                if (!vals.get(k).toString().equalsIgnoreCase("Time")) {
                    comboIds.add(vals.get(k));
                    comboValues.add(getElementName(vals.get(k).toString(), repParameters));
                }
            }
            int CloumnCount = 10;
            PbReturnObject DataObject = (PbReturnObject) container.getRetObj();
            int RowCount = DataObject.getRowCount();
            if (RowCount < CloumnCount) {
                CloumnCount = RowCount;

            }
            String str1 = "";
            String temp = "";
            String temp1 = "";
            String Label = a2.get(0).toString() + a2.get(1).toString();
            String Name = "CBOVIEW_BY_1";
            String Name2 = "CBOVIEW_BY_12" + newArray[i];
            /// start of query
            cbo.append("<Table  border='0' cellspacing='0' cellpadding='0' style='width:100%'><Tr style='width:100%'> <Td style='width:20%; font-style:bold;' align='right'>Global Filter:</Td> ");
//        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
//        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:20%; font-style:bold;\" align=\"right\">Global Filter:</Td> ";//Label
            cbo.append("<Td style='width:10%'> <select id='selectparam' style='height:20px;' name='").append(Name).append(" onclick='kpireplaceinclude('selectparam','").append(Name).append("')  onchange='getparamvales('selectparam','").append(Name).append("') ' class='myTextbox3' >");
//        cbo = cbo + "<Td style=\"width:10%\"> <select id='selectparam' style=\"height:20px;\" name=\"" + Name + "\" onclick=\"kpireplaceinclude('selectparam','" + Name + "')\" onchange=\"getparamvales('selectparam','" + Name + "')\" class=\"myTextbox3\" >";
            {


                for (int i1 = 0; i1 < comboIds.size(); i1++) {
                    temp = (String) comboIds.get(i1);
                    temp1 = (String) comboValues.get(i1);

                    if (currVal.equalsIgnoreCase(temp)) {
                        currVal = temp;
                        cbo.append("<option selected value=").append(temp).append(">").append(temp1).append("</option>");
//                     cbo = cbo + ("<option selected value=\"" + temp + "\">" + temp1 + "</option>");
                    } else {
                        cbo.append("<option value='").append(temp).append("'>").append(temp1).append("</option>");
//                    cbo = cbo + ("<option value=\"" + temp + "\">" + temp1 + "</option>");
                    }
                }

            }
            cbo.append("</select></Td>");
//        cbo = cbo + "</select></Td>";

//          cbo = cbo + "<Td style=\"width:15%\"><input id=\"" + Name2 + "\" name=\"" + Name2 + "\"  style='width: 200px;'/>";
//        {
            ArrayList parameterValue = new ArrayList();

            for (int i1 = 0; i1 < comboIds.size(); i1++) {
                temp = (String) comboIds.get(i1);
                temp1 = (String) comboValues.get(i1);

                if (currVal.equalsIgnoreCase(temp)) {
                    if (repParameters != null && repParameters.size() > 0) {
                        a1 = (String[]) (repParameters.keySet()).toArray(new String[0]);
                        this.totalParam = a1.length;

                        paramInfo = (ArrayList) repParameters.get(a1[i1]);
                        boxNamekpi = "kpi" + paramInfo.get(9).toString() + "";
                        boxNamekpidrill = "kpidrill" + paramInfo.get(9).toString() + "";
                        textBoxName = "selFilter_" + paramInfo.get(9).toString() + "";
                    }
                    ArrayList<String> DisplayColumns = container.getDisplayColumns();
// int CloumnCount=10;
//  cbo = cbo + ("<option onclick=\"setCountrykpi('All','"+boxNamekpi+"','" + textBoxName + "','"+Name2+"')\" selected value=\"All \"> All</option>");
                    for (int l = 0; l < CloumnCount; l++) {

                        DataFacade facade = new DataFacade(container);
                        int actualRow = facade.getActualRow(l);
                        String ViewBYHeader = facade.getDimensionData(actualRow, DisplayColumns.get(0));

//String Msrdata=facade.getFormattedMeasureData(actualRow, MsrsLngth);
                        parameterValue.add(ViewBYHeader);
// cbo = cbo + ("<input type='checkbox' name=\"" + ViewBYHeader + "\" value=\"" + ViewBYHeader + "\">");
//  cbo = cbo + ("<input type='checkbox' name=\"" + ViewBYHeader + "\" value=\"" + ViewBYHeader + "\"> <label>" + ViewBYHeader + "</label> <br />");
//          cbo = cbo + ("<input type='checkbox' onclick=\"setCountrykpi('" + ViewBYHeader + "','"+boxNamekpi+"','" + textBoxName + "','"+Name2+"')\" value=\"" + ViewBYHeader + "\" ><label>" + ViewBYHeader + "</label>");

                    }

                }
            }
            String mKey = "";

            String divName = "'" + Name + "SuggestList" + "'";
            String boxName = "'" + Name + "'";
            if (!currVal.toString().equalsIgnoreCase("Time")) {
                mKey = "'" + a1[i] + "'";
            }
//         cbo = cbo + "<Td style=\"width:15%\"  onclick=\"setCountrykpigetcheckbox(this,'"+boxNamekpi+"','" + textBoxName + "','"+Name2+"','" + textBoxName + "','" + textBoxName + "'," + divName + "," + mKey + ","+boxName+",'checkboxdiv','selectparam')\"><input id=\"" + Name2 + "\" name=\"" + Name2 + "\"  style='width: 150px;'/>";
////         cbo = cbo + "<Td style=\"width:15%\"  onclick=\"setCountrykpigetcheckbox('" + parameterValue + "','"+boxNamekpi+"','" + textBoxName + "','"+Name2+"','checkboxdiv')\"><input id=\"" + Name2 + "\" name=\"" + Name2 + "\"  style='width: 150px;'/>";
////        }
//          cbo = cbo + "</Td>";
//          cbo = cbo + "<div id='checkboxdiv' name='checkboxdiv'  style='width: 160px; height: 200px;  z-index:99999; overflow: scroll; overflow-x: hidden; display:none; position: absolute; background-color:white; margin-left: 60%; margin-top: 15px; '/></div>";
//
// cbo = cbo + "<Table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"width:100%\">";
//       cbo = cbo + "<tr style=\"width:100%\">";
            cbo.append("<td><table><tr><td colspan='2'  style='width:1%;'>");
//      cbo = cbo + "<td>";
//       cbo = cbo + "<table>";
//      cbo = cbo + "<tr>";
//    cbo = cbo + "<td colspan=\"2\" style=\"width:1%;\">";
            if (repParameters != null && repParameters.size() > 0) {
                a1 = (String[]) (repParameters.keySet()).toArray(new String[0]);
//              for (i = 0; i < a1.length; i++) {
                for (int i1 = 0; i1 < comboIds.size(); i1++) {
                    temp = (String) comboIds.get(i1);
                    temp1 = (String) comboValues.get(i1);

                    if (currVal.equalsIgnoreCase(temp)) {
                        paramInfo = (ArrayList) repParameters.get(a1[i1]);
                        cbo.append("<Td width='40%' id=column").append(temp).append(" align='right'> ").append(f1.getMultiTextAreakpi(paramInfo.get(9).toString(), (String) paramInfo.get(1), (List<String>) inMap.get(a1[i1]), a1[i1], (String) paramInfo.get(10), collect.reportId, a1[i1], comboIds, comboValues, currVal)).append("</td>");
//        cbo = cbo + " <Td width=\"40%\" id=column" +temp + " align=\"right\"> " + f1.getMultiTextAreakpi(paramInfo.get(9).toString(), (String) paramInfo.get(1),(List<String>)inMap.get(a1[i1]) , a1[i1], (String) paramInfo.get(10),collect.reportId,a1[i1],comboIds,comboValues,currVal) + " </Td>";
                    } else {
                        paramInfo = (ArrayList) repParameters.get(a1[i1]);
                        cbo.append("<Td width=\"40%\"  style=\"display:none;\" id=column").append(temp).append(" align=\"right\"> ").append(f1.getMultiTextAreakpi(paramInfo.get(9).toString(), (String) paramInfo.get(1), (List<String>) inMap.get(a1[i1]), a1[i1], (String) paramInfo.get(10), collect.reportId, a1[i1], comboIds, comboValues, currVal)).append("</td>");
//          cbo = cbo + " <Td width=\"40%\"  style=\"display:none;\" id=column" + temp + " align=\"right\"> " + f1.getMultiTextAreakpi(paramInfo.get(9).toString(), (String) paramInfo.get(1),(List<String>)inMap.get(a1[i1]) , a1[i1], (String) paramInfo.get(10),collect.reportId,a1[i1],comboIds,comboValues,currVal) + " </Td>";
                    }
                }
//       }
            }
//     cbo = cbo + "</td>";
//       cbo = cbo + "</tr>";
//       cbo = cbo + "</table>";
//       cbo = cbo + "</td>";
            cbo.append("</td>").append("</tr>").append("</table>").append("</td>");
            //          cbo = cbo + "</Td>";
//         cbo = cbo + "<Td style=\"width:15%\"> <select name=\"" + Name2 + "\"    class=\"myTextbox3\" >";
//        {
//
//
//            for (int i1 = 0; i1< comboIds.size(); i1++) {
//                temp = (String) comboIds.get(i1);
//                temp1 = (String) comboValues.get(i1);
//
//                 if (currVal.equalsIgnoreCase(temp)) {
//                       if (repParameters != null && repParameters.size() > 0) {
//            a1 = (String[]) (repParameters.keySet()).toArray(new String[0]);
//            this.totalParam = a1.length;
//
//                paramInfo = (ArrayList) repParameters.get(a1[i1]);
//              boxNamekpi ="kpi" + paramInfo.get(9).toString() + "";
//              boxNamekpidrill ="kpidrill" + paramInfo.get(9).toString() + "";
//                textBoxName = "selFilter_" + paramInfo.get(9).toString() + "";
//     }
// ArrayList<String> DisplayColumns=container.getDisplayColumns();
//  cbo = cbo + ("<option onclick=\"setCountrykpi('All','"+boxNamekpi+"','" + textBoxName + "','"+Name2+"')\" selected value=\"All \"> All</option>");
// for(int l=0;l<RowCount;l++){
//
//            DataFacade facade = new DataFacade(container);
//String ViewBYHeader=facade.getDimensionData(l, DisplayColumns.get(0));
// List<String> parameterValue=new LinkedList<String>();
// parameterValue.add(ViewBYHeader);
//// cbo = cbo + ("<input type='checkbox' name=\"" + ViewBYHeader + "\" value=\"" + ViewBYHeader + "\">");
////  cbo = cbo + ("<input type='checkbox' name=\"" + ViewBYHeader + "\" value=\"" + ViewBYHeader + "\"> <label>" + ViewBYHeader + "</label> <br />");
//          cbo = cbo + ("<option onclick=\"setCountrykpi('" + ViewBYHeader + "','"+boxNamekpi+"','" + textBoxName + "','"+Name2+"')\" value=\"" + ViewBYHeader + "\" >" + ViewBYHeader + "</option>");
//
//            }
//
//        }
//            }
//        }
//        cbo = cbo + "</select></Td>";
            if (repParameters != null && repParameters.size() > 0) {
                a1 = (String[]) (repParameters.keySet()).toArray(new String[0]);

                ArrayList parameterValueinclude = new ArrayList();
                String imageName = "includeparamval";
                this.totalParam = a1.length;

                for (i = 0; i < a1.length; i++) {
                    ArrayList paramInfo1 = (ArrayList) repParameters.get(a1[i]);

                    LinkedHashMap viewOrder1 = new LinkedHashMap<String, ArrayList>();
                    String idval = a1[i];
                    viewOrder1.put(idval, paramInfo1);

                    boxNamekpi = "kpi" + paramInfo1.get(9).toString() + "";
                    boxNamekpidrill = "kpidrill" + paramInfo1.get(9).toString() + "";
                    textBoxName = "selFilter_" + paramInfo1.get(9).toString() + "";
                    Gson gson = new Gson();
                    List<String> currValList = (List<String>) inMap.get(a1[i]);
                    Type listOfTestObject = new TypeToken<List<String>>() {
                    }.getType();
                    String filterStr = gson.toJson(currValList, listOfTestObject);
                    parameterValueinclude.add(paramInfo1.get(9).toString());
                    cbo.append("<input  type='text' style='border:1px solid #B2B2B2;background:transparent;float:left;display:none;' name='").append(boxNamekpi).append("' id='").append(boxNamekpi).append("' class='' value='").append(filterStr).append("'>");
//   cbo = cbo + ("<input  type='text' style='border:1px solid #B2B2B2;background:transparent;float:left;display:none;' name='" + boxNamekpi + "' id='" + boxNamekpi + "' class='' value='" + filterStr + "'>");
                    cbo.append("<input  type='text' style='border:1px solid #B2B2B2;background:transparent;float:left;display:none;' name='").append(boxNamekpidrill).append("' id='").append(boxNamekpidrill).append("' class='' value='").append(paramInfo1).append("'>");
//   cbo = cbo + ("<input  type='text' style='border:1px solid #B2B2B2;background:transparent;float:left;display:none;' name='" + boxNamekpidrill + "' id='" + boxNamekpidrill + "' class='' value='" + paramInfo1 + "'>");
                    cbo.append("<input type=\"hidden\" id='orderVal1_").append(paramInfo1.get(9).toString()).append("' name='orderVal1_").append(paramInfo1.get(9).toString()).append("' value=\"orderAsc_").append(paramInfo1.get(9).toString()).append("\"></td>");
//   cbo = cbo + ("<input type=\"hidden\" id='orderVal1_"+paramInfo1.get(9).toString()+"' name='orderVal1_"+paramInfo1.get(9).toString()+"' value=\"orderAsc_"+paramInfo1.get(9).toString()+"\"></td>");


                }
                cbo.append("<input type=\"hidden\" id='").append(imageName).append("' name='").append(imageName).append("' value=\"").append(parameterValueinclude).append("\"></td>");
//    cbo = cbo + ("<input type=\"hidden\" id='"+imageName+"' name='"+imageName+"' value=\""+parameterValueinclude+"\"></td>");
            }
//cbo = cbo + "<td style=\"width:43%\"><a  class=\"navtitle-hover\" onclick=\"submitform1()\" style=\"font:verdana;font-size:8pt;height: 20px; width: 20px;font-style:bold; \">GO</a></td>";
            cbo.append("<td style=\"width:43%\"><a  class=\"navtitle-hover\" onclick=\"submitform1()\" style=\"font:verdana;font-size:8pt;height: 20px; width: 20px;font-style:bold; \">GO</a></td></Tr></Table>");

//           cbo = cbo + "</Tr></Table>";
        }
        /// end of query
        container.setReportCollect(collect);
        return cbo.toString();
    }

    public String designerParams(String contextPath) {
        StringBuilder result = new StringBuilder();

        result.append("<div id='headerDiv' style='overflow:auto;'>");
        result.append("<Table width='100%' height='100%'> ");
        result.append("<tr style=\"width:100%\">");
        result.append("<td width=\"100%\" style=\"background-color: #E5E5E5;height:25px\">"); //style=\"border-bottom:1px dashed;\"
        result.append(" <table style=\"width:100%\"><tr>");
        if (isReport) {
//                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-pencil\" title=\"Add Inner Viewbys\" onclick=\"AddInnerViewbys('" + contextPath + "')\"></a></td>");
//                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-disk\" title=\"Save Parameters\" onclick=\"saveParamSection('" + contextPath + "')\"></a></td>");
//                    result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-minusthick\" title=\"Remove Parameters\" onclick=\"RemoveMoreDims('" + contextPath + "')\"></a></td>");
            result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon-plusthick\" title=\"Add More Parameters\" onclick=\"AddMoreDims('" + contextPath + "')\"></a></td>");
            result.append("<td align=\"center\"><a  class=\" ui-icon ui-icon ui-icon-arrowthick-2-n-s\" title=\"Edit View By\" onclick=\"designerViewBy()\"></a></td>");
//                    result.append("<td align=\"center\"><a href=\"javascript:submitform()\" style=\"font:verdana;font-size:8pt;font-style:bold;\">GO</a></td>");
            result.append("<td align=\"center\"><a  class=\"ui-icon ui-icon-triangle-1-w\" href=\"javascript:void(0)\" onclick=\"closeDiv()\" title=\"Close\" ></a></td>");

        }
        result.append("</table>");
        result.append("</td>");
        result.append("</tr>");
        result.append("</Table>");
        result.append("</div>");

        return result.toString();
    }

    public String getGrandTotalRegion(ArrayList MeasureNames, ArrayList measureIdsList, ArrayList MeasureVals) {

        StringBuilder result = new StringBuilder();
        result.append("<table width='auto' height='80px' style='border-spacing:10px;margin-left:auto;margin-right:auto;' align='left'><tr>");
        for (int i = 0; i < MeasureNames.size(); i++) {
            if (i == 7) {
                break;
            }
            if (MeasureVals.get(i) != null && !MeasureVals.get(i).toString().equalsIgnoreCase("")) {
                result.append("<td width='auto' style='white-space: nowrap;align:center;' class='TopButtonsNames'>" + MeasureNames.get(i) + "</td>");
            }
        }
        result.append("</tr><tr>");

        for (int j = 0; j < MeasureNames.size(); j++) {
            int numId = 0;
            if (j != 0) {
                numId = j % 2;
            }
            if (j == 7) {
                break;
            }
            if (MeasureVals.get(j) != null && !MeasureVals.get(j).toString().equalsIgnoreCase("")) {
                result.append("<td align='center' width='auto' style='white-space: nowrap' class='TopButtons" + numId + "' id='subVal" + j + "'>" + MeasureVals.get(j) + "</td>");
            }
        }
        result.append("</tr></table>");
        return result.toString();
    }

// added by krishan Pratap
    public String displayTpggleTime(HashMap hm, String comp, String COMPARISON_DATE) throws SQLException, Exception {
        // String result1 = "<Table width=\"80%\">";
//        String result1 = "";
        StringBuilder result1 = new StringBuilder();
        String[] a1 = (String[]) (hm.keySet()).toArray(new String[0]);

        this.totalParam += a1.length;
        int i = 0;
        if (comp.equals("Yes") || COMPARISON_DATE.equals("Yes")) {
            for (i = 0; i < a1.length; i++) {
                ArrayList a = (ArrayList) hm.get(a1[i]);

                if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    result1.append(" <Td width=\"25%\" id=column1  align=\"right\" style=\" padding-left: 1.5em\"> ").append(f1.getStdStringDuration((String) a.get(0), "fixdate")).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                    ArrayList b = (ArrayList) hm.get("PRG_PERIOD_TYPE");
                    result1.append(" <Td width=\"25%\" id=column2  align=\"right\" style=\" padding-left: 1.5em ;display:none\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), "fixdate")).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                    result1.append(" <Td width=\"25%\" id=column3 align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    result1.append(" <Td  style=\" padding-left: 1.5em;white-space: nowrap\" width=\"25%\" id=column4 align=\"right\"> ").append(f1.getRollingPeriodLOV((String) a.get(0))).append(" </Td> ");

                }
            }
        } else {
            for (i = 0; i < a1.length; i++) {
                ArrayList a = (ArrayList) hm.get(a1[i]);

                if (a1[i].equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                    result1.append(" <Td width=\"25%\" id=column1 align=\"right\" style=\" padding-left: 1.5em\"> ").append(f1.getStdStringDuration((String) a.get(0), "fixdate")).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_COMPARE")) {
                    ArrayList b = (ArrayList) hm.get("PRG_PERIOD_TYPE");
                    result1.append(" <Td width=\"25%\" id=column2 align=\"right\" style=\" padding-left: 1.5em\"> ").append(f1.getStdStringCompare((String) a.get(0), (String) b.get(0), "fixdate")).append(" </Td> ");
                } else if (a1[i].equalsIgnoreCase("AS_OF_YEAR") || a1[i].equalsIgnoreCase("AS_OF_YEAR1")) {
                    result1.append(" <Td width=\"25%\" id=column3 align=\"right\"> ").append(f1.getQueryCombotb((String) a.get(1), (String) a.get(2), "select distinct cyear, cyear from pr_day_denom order by 1 ", (String) a.get(0))).append(" </Td> ");

                } else if (a1[i].equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    result1.append(" <Td  style=\" padding-left: 1.5em;white-space: nowrap\" width=\"25%\" id=column4 align=\"right\"> ").append(f1.getRollingPeriodLOV((String) a.get(0))).append(" </Td> ");

                }
            }
        }

        result1.append("");
        return result1.toString();
    }
}
