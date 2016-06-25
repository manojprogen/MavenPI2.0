/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.action;

import com.google.gson.Gson;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class SaveCustMemberRD extends org.apache.struts.action.Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

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
        CustomMeasureResourceBundle resBundle = new CustomMeasureResourceBundle();
        PbDb pbdb = new PbDb();
        Gson gson = new Gson();
        String finalQuery = "";
        ArrayList list = new ArrayList();
        try {
            String columnFormula = request.getParameter("txt2");
            columnFormula = columnFormula.replace("@", "+");
            String folderIds = request.getParameter("folderIds");
            String columnName = request.getParameter("columnName");
            String iscalculate = request.getParameter("iscalculate");
            String tArea = request.getParameter("tArea");
            String tArea1 = request.getParameter("tArea1");
//            String eleList = request.getParameter("eleList");
            columnFormula = columnFormula.replace("'", "''");
            String displayFormula = columnFormula;
            // String eleList1[]=eleList.split(",");
            String a = tArea1.trim();
            if (a.startsWith(",")) {
                a = a.substring(1);
            }
            String eleList2[] = a.split(",");
            StringBuilder eleList1 = new StringBuilder(100);
//            String eleList1 = "";
            if (eleList2.length > 1) {
                for (int j = 0; j < eleList2.length - 1; j++) {
                    int count = 0;
                    for (int j1 = j + 1; j1 < eleList2.length; j1++) {
                        //
                        if (eleList2[j].equalsIgnoreCase(eleList2[j1])) {

                            count = 1;
                            break;
                        }
                    }
                    if (count == 0) {
                        eleList1.append(",").append(eleList2[j]);
//                        eleList1 += "," + eleList2[j];
                    }
                    if (j == eleList2.length - 2) {
                        eleList1.append(",").append(eleList2[j + 1]);
//                        eleList1 += "," + eleList2[j+1];
                    }

                }

//                if (!eleList1.equalsIgnoreCase("")) {
                if (eleList1.length() > 0) {
                    if (eleList1.toString().startsWith(",")) {
                        eleList1 = new StringBuilder(eleList1.substring(1));
                    }
                }
            } else {
                eleList1 = new StringBuilder(eleList2[0]);
//                eleList1 = eleList2[0];
            }

            String eleList3[] = eleList1.toString().split(",");

            String dependenteleids = "";
            for (int p = 0; p < eleList3.length; p++) {
                boolean check = tArea.contains(eleList3[p]);

                if (check == true) {

                    dependenteleids += "," + eleList3[p];
                }
                if (p == eleList3.length - 1) {

                    if (dependenteleids.startsWith(",")) {

                        dependenteleids = dependenteleids.substring(1);
                    }
                }
            }

            String aggType = "";
            String colType = "";
            // columnFormula=tArea.toUpperCase();
            columnFormula = columnFormula.toUpperCase();
            if (columnFormula.indexOf("SUM(") >= 0 || columnFormula.indexOf("COUNT(") >= 0 || columnFormula.indexOf("COUNT(*") >= 0 || columnFormula.indexOf("AVG(") >= 0 || columnFormula.indexOf("MIN(") >= 0 || columnFormula.indexOf("MAX(") >= 0 || columnFormula.indexOf("POWER(") >= 0 || columnFormula.indexOf("SQRT(") >= 0 || columnFormula.indexOf("ROUND(") > 0 || columnFormula.indexOf("ABS(") >= 0) {
                colType = "summarised";
            } else {
                colType = "calculated";
            }
            if (columnFormula.indexOf("SUM(") >= 0) {
                aggType = "sum";

            } else if (columnFormula.indexOf("COUNT(") >= 0) {
                aggType = "count";

            } else if (columnFormula.indexOf("COUNT(*") >= 0) {
                aggType = "count(*)";

            } else if (columnFormula.indexOf("COUNT(DISTINCT") >= 0) {
                aggType = "count";
            } else if (columnFormula.indexOf("AVG(") >= 0) {
                aggType = "avg";

            } else if (columnFormula.indexOf("MIN(") >= 0) {
                aggType = "min";
            } else if (columnFormula.indexOf("MAX(") >= 0) {
                aggType = "max";

            } else if (columnFormula.indexOf("POWER(") >= 0) {
                aggType = "avg";

            } else if (columnFormula.indexOf("SQRT(") >= 0) {
                aggType = "min";
            } else if (columnFormula.indexOf("ABS(") >= 0) {
                aggType = "max";

            } else if (columnFormula.indexOf("ROUND(") >= 0) {
                aggType = "max";

            }
            if (columnFormula.indexOf("SUM(") >= 0 && columnFormula.indexOf("COUNT(") >= 0) {
                aggType = "avg";

            } else {
                aggType = "sum";
            }

            String summarisationList[] = {"SUM", "AVG", "COUNT", "MAX", "MIN", "ROUND"};

            int bussTableId = 0;
            int subFolderTabId = 0;
            String bussTableName = "";

            String doubleExistQuery = " SELECT distinct BUSS_TABLE_ID,SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS  where element_id in(" + dependenteleids + ")";
            PbReturnObject doubleExistQuerypbro = pbdb.execSelectSQL(doubleExistQuery);
            if (!(doubleExistQuerypbro.getRowCount() > 1)) {
                String doubleExistQuery1 = " SELECT distinct BUSS_TABLE_ID,SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS  where element_id in(" + dependenteleids + ") AND BUSS_TABLE_ID!=0";
                PbReturnObject doubleExistQuerypbro1 = pbdb.execSelectSQL(doubleExistQuery1);
                if (doubleExistQuerypbro1.getRowCount() > 0) {
                    String nameQuery = "SELECT BUSS_TABLE_NAME,NVL(BUSS_COL_NAME,USER_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,length(nvl(USER_COL_DESC, user_col_name))  FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependenteleids + ") order by length(nvl(USER_COL_DESC, user_col_name)) desc";
                    PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
                    String nameQuery1 = "SELECT BUSS_TABLE_NAME,NVL(BUSS_COL_NAME,USER_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,length(nvl(USER_COL_DESC, user_col_name))   FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependenteleids + ") and user_col_type in('calculated','summarized','summarised') order by length(nvl(USER_COL_DESC, user_col_name)) desc";
                    PbReturnObject pbro1 = pbdb.execSelectSQL(nameQuery1);
                    String bussNamecolName = "";
                    String onlyColName = "";
                    String formulaold = "";
                    int count = 0;
                    int count1 = 0;
                    for (int n = 0; n < pbro.getRowCount(); n++) {
                        bussNamecolName = pbro.getFieldValueString(n, 0) + "." + pbro.getFieldValueString(n, 1);
                        //  onlyColName = String.valueOf(pbro.getFieldValueInt(n, 2));
                        onlyColName = pbro.getFieldValueString(n, 3);
                        bussNamecolName = bussNamecolName.toUpperCase();
                        onlyColName = onlyColName.toUpperCase();
                        if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("calculated") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                            if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                                colType = "summarized";
                            }
                            if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("calculated") && (pbro.getFieldValueString(n, 7).equalsIgnoreCase("AVG"))) {
                                if (pbro.getFieldValueString(n, 5).toUpperCase().indexOf("AVG") > 0) {
                                    formulaold = "(" + pbro.getFieldValueString(n, 5) + ")";
                                } else {
                                    String a1 = pbro.getFieldValueString(n, 7).toUpperCase() + "(" + onlyColName + ")";
                                    String a2[] = new String[summarisationList.length];
                                    for (int m = 0; m < summarisationList.length; m++) {
                                        a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                    }

                                    if (columnFormula.contains(a1)) {
                                        formulaold = pbro.getFieldValueString(n, 5);
                                    } else {
                                        int testcount = 0;
                                        for (int m = 0; m < a2.length; m++) {
                                            if (columnFormula.contains(a2[m])) {
                                                testcount = 1;
                                            }
                                        }
                                        if (testcount == 0) {
                                            formulaold = pbro.getFieldValueString(n, 7) + "(" + pbro.getFieldValueString(n, 5) + ")";
                                        } else {
                                            formulaold = pbro.getFieldValueString(n, 5);
                                        }
                                    }
                                }

                            } else if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("calculated") && (pbro.getFieldValueString(n, 7).equalsIgnoreCase("COUNT"))) {
                                if (pbro.getFieldValueString(n, 5).toUpperCase().indexOf("COUNT") > 0) {
                                    formulaold = "(" + pbro.getFieldValueString(n, 5) + ")";
                                } else {
                                    String a1 = pbro.getFieldValueString(n, 7).toUpperCase() + "(" + onlyColName + ")";
                                    String a2[] = new String[summarisationList.length];
                                    for (int m = 0; m < summarisationList.length; m++) {
                                        a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                    }

                                    if (columnFormula.contains(a1)) {
                                        formulaold = pbro.getFieldValueString(n, 5);
                                    } else {
                                        int testcount = 0;
                                        for (int m = 0; m < a2.length; m++) {
                                            if (columnFormula.contains(a2[m])) {
                                                testcount = 1;
                                            }
                                        }
                                        if (testcount == 0) {
                                            formulaold = pbro.getFieldValueString(n, 7) + "(" + pbro.getFieldValueString(n, 5) + ")";
                                        } else {
                                            formulaold = pbro.getFieldValueString(n, 5);
                                        }
                                    }
                                }

                            } else if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("calculated") && (!(pbro.getFieldValueString(n, 7).equalsIgnoreCase("COUNT") || (pbro.getFieldValueString(n, 7).equalsIgnoreCase("AVG"))))) {
                                colType = "summarized";
                                String a1 = pbro.getFieldValueString(n, 7).toUpperCase() + "(" + onlyColName + ")";
                                String a2[] = new String[summarisationList.length];
                                for (int m = 0; m < summarisationList.length; m++) {
                                    a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                }
                                if (columnFormula.contains(a1)) {
                                    formulaold = pbro.getFieldValueString(n, 5);
                                } else {
                                    int testcount = 0;
                                    for (int m = 0; m < a2.length; m++) {
                                        if (columnFormula.contains(a2[m])) {
                                            testcount = 1;
                                        }
                                    }
                                    if (testcount == 0) {
                                        formulaold = pbro.getFieldValueString(n, 7) + "(" + pbro.getFieldValueString(n, 5) + ")";
                                    } else {
                                        formulaold = pbro.getFieldValueString(n, 5);
                                    }
                                }

                            } else if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                                formulaold = "(" + pbro.getFieldValueString(n, 5) + ")";  //don,t delete imp for avg and count
                            }
                            columnFormula = columnFormula.replace(onlyColName, formulaold);
                            if (!(pbro.getFieldValueString(n, 6).equalsIgnoreCase(""))) {
                                dependenteleids = dependenteleids + "," + pbro.getFieldValueString(n, 6);

                            }
                            count++;
                            //   if(count1>0){
                            //   colType="calculated";
                            //  }
                        } else {
                            if (pbro1.getRowCount() == pbro.getRowCount()) {
                                columnFormula = columnFormula.replace(onlyColName, bussNamecolName);
                            } else {
                                String a1 = pbro.getFieldValueString(n, 7).toUpperCase() + "(" + onlyColName + ")";
                                String a2[] = new String[summarisationList.length];
                                for (int m = 0; m < summarisationList.length; m++) {
                                    a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                }
                                if (columnFormula.contains(a1)) {
                                    //.println("==");
                                    bussNamecolName = pbro.getFieldValueString(n, 0) + "." + pbro.getFieldValueString(n, 1);
                                } else {
                                    int testcount = 0;
                                    for (int m = 0; m < a2.length; m++) {
                                        if (columnFormula.contains(a2[m])) {
                                            testcount = 1;
                                        }
                                    }
                                    if (testcount == 0) {
                                        bussNamecolName = pbro.getFieldValueString(n, 7) + "(" + pbro.getFieldValueString(n, 0) + "." + pbro.getFieldValueString(n, 1) + ")";
                                    } else {
                                        bussNamecolName = pbro.getFieldValueString(n, 0) + "." + pbro.getFieldValueString(n, 1);
                                    }


                                }
                                columnFormula = columnFormula.replace(onlyColName, bussNamecolName);
                                colType = "summarized";
                            }
                            // if(count>0){
                            // colType="calculated";
                            // }
                            count1++;
                        }

                    }

                    bussTableId = doubleExistQuerypbro.getFieldValueInt(0, 0);
                    subFolderTabId = doubleExistQuerypbro.getFieldValueInt(0, 1);
                    bussTableName = pbro.getFieldValueString(0, 0);
                } else {

//                    String formulaold = "";

                    // columnFormula = tArea;
                    //  String nameQuery = " SELECT t.DISP_NAME,NVL(e.BUSS_COL_NAME,e.user_col_name),e.element_id   FROM PRG_USER_SUB_FOLDER_ELEMENTS e, " +
                    //              " PRG_USER_SUB_FOLDER_TABLES t   WHERE e.element_id in(" + dependenteleids + ") AND t.sub_folder_tab_id = e.sub_folder_tab_id";
                    String nameQuery = "SELECT BUSS_TABLE_NAME,NVL(BUSS_COL_NAME,USER_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,length(nvl(USER_COL_DESC, user_col_name))   FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependenteleids + ") order by  length(nvl(USER_COL_DESC, user_col_name)) desc";
                    PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
                    String bussNamecolName = "";
                    String onlyColName = "";
                    for (int n = 0; n < pbro.getRowCount(); n++) {
                        if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                            colType = "summarized";
                        }
                        bussNamecolName = pbro.getFieldValueString(n, 1);
                        //  onlyColName = String.valueOf(pbro.getFieldValueInt(n, 2));
                        onlyColName = String.valueOf(pbro.getFieldValueString(n, 3));
                        bussNamecolName = bussNamecolName.toUpperCase();
                        onlyColName = onlyColName.toUpperCase();
                        if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("calculated") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                            String nameQuery2 = "SELECT BUSS_TABLE_NAME,NVL(BUSS_COL_NAME,USER_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE   FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + pbro.getFieldValueInt(n, 2) + ")";
                            PbReturnObject pbro2 = pbdb.execSelectSQL(nameQuery2);
                            if (!(pbro2.getFieldValueString(0, 6).equalsIgnoreCase(""))) {
                                dependenteleids = dependenteleids + "," + pbro2.getFieldValueString(0, 6);
                            }
                            bussNamecolName = "(" + String.valueOf(pbro2.getFieldValueString(0, 5)) + ")";
                        }
                        //else{
                        columnFormula = columnFormula.replace(onlyColName, bussNamecolName);
                        //  }
                    }


                }
            } else {
                String formulaold = "";
                String doubleExistQuery1 = " SELECT distinct BUSS_TABLE_ID,SUB_FOLDER_TAB_ID FROM PRG_USER_SUB_FOLDER_ELEMENTS  where element_id in(" + dependenteleids + ") AND BUSS_TABLE_ID!=0";
                PbReturnObject doubleExistQuerypbro1 = pbdb.execSelectSQL(doubleExistQuery1);
                if (doubleExistQuerypbro1.getRowCount() == doubleExistQuerypbro.getRowCount()) {
                    String nameQuery = "SELECT BUSS_TABLE_NAME,NVL(BUSS_COL_NAME,USER_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,length(nvl(USER_COL_DESC, user_col_name))  FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependenteleids + ") order by length(nvl(USER_COL_DESC, user_col_name)) desc";
                    PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
                    String nameQuery1 = "SELECT BUSS_TABLE_NAME,NVL(BUSS_COL_NAME,USER_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,length(nvl(USER_COL_DESC, user_col_name))  FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependenteleids + ") and user_col_type in('calculated','summarized','summarised') order by length(nvl(USER_COL_DESC, user_col_name)) desc";
                    PbReturnObject pbro1 = pbdb.execSelectSQL(nameQuery1);
                    String bussNamecolName = "";
                    String onlyColName = "";
                    for (int n = 0; n < pbro.getRowCount(); n++) {
                        if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                            colType = "summarized";
                        }
                        bussNamecolName = pbro.getFieldValueString(n, 1);
                        //  onlyColName = String.valueOf(pbro.getFieldValueInt(n, 2));
                        onlyColName = String.valueOf(pbro.getFieldValueString(n, 3));
                        bussNamecolName = bussNamecolName.toUpperCase();
                        onlyColName = onlyColName.toUpperCase();
                        /*
                         * if(pbro.getFieldValueString(n,
                         * 4).equalsIgnoreCase("calculated") ||
                         * pbro.getFieldValueString(n,
                         * 4).equalsIgnoreCase("summarized")||
                         * pbro.getFieldValueString(n,
                         * 4).equalsIgnoreCase("summarised")){
                         * formulaold="("+pbro.getFieldValueString(n, 5)+")";
                         * columnFormula = columnFormula.replace(onlyColName,
                         * formulaold);
                         * dependenteleids=dependenteleids+","+pbro.getFieldValueString(n,6);
                     }else{
                         */
                        if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("calculated") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                            if (!(pbro.getFieldValueString(n, 6).equalsIgnoreCase(""))) {
                                dependenteleids = dependenteleids + "," + pbro.getFieldValueString(n, 6);
                            }
                            // if(pbro.getRowCount()!=pbro1.getRowCount()){
                            // if(pbro.getFieldValueString(n, 6).equalsIgnoreCase("COUNT")){
                            //.println("==summarized333 ==");
                            String a1 = "SUM(" + onlyColName + ")";
                            String a2[] = new String[summarisationList.length];
                            for (int m = 0; m < summarisationList.length; m++) {
                                a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                            }
                            if (columnFormula.contains(a1)) {
                                bussNamecolName = pbro.getFieldValueString(n, 1);
                                //}else{
                            } else {
                                int testcount = 0;
                                for (int m = 0; m < a2.length; m++) {
                                    if (columnFormula.contains(a2[m])) {
                                        testcount = 1;
                                    }
                                }
                                if (testcount == 0) {
                                    bussNamecolName = "SUM(" + pbro.getFieldValueString(n, 1) + ")";
                                } else {
                                    bussNamecolName = pbro.getFieldValueString(n, 1);
                                }

                                //}else{
                            }

                            // bussNamecolName=pbro.getFieldValueString(n, 6)+"("+ pbro.getFieldValueString(n, 1)+")";
                            //  }
                            //  }
                        } else {
                            // if(pbro.getRowCount()!=pbro1.getRowCount()){
                            //   colType="summarized";
                            //   bussNamecolName=pbro.getFieldValueString(n, 1);
                            //  }else{
                            // if(pbro.getRowCount()==pbro1.getRowCount()){zz
                            colType = "summarized";
                            String a1 = pbro.getFieldValueString(n, 7).toUpperCase() + "(" + onlyColName + ")";
                            String a2[] = new String[summarisationList.length];
                            for (int m = 0; m < summarisationList.length; m++) {
                                a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                            }
                            if (columnFormula.contains(a1)) {
                                bussNamecolName = pbro.getFieldValueString(n, 1);
                            } else {


                                int testcount = 0;
                                for (int m = 0; m < a2.length; m++) {
                                    if (columnFormula.contains(a2[m])) {
                                        testcount = 1;
                                    }
                                }
                                if (testcount == 0) {
                                    bussNamecolName = pbro.getFieldValueString(n, 7) + "(" + pbro.getFieldValueString(n, 1) + ")";
                                } else {
                                    bussNamecolName = pbro.getFieldValueString(n, 1);
                                }


                            }
                            // }
                            // else{

                            //  bussNamecolName=pbro.getFieldValueString(n, 1);
                            // }

                            // }
                        }
                        columnFormula = columnFormula.replace(onlyColName, bussNamecolName);

                        // columnFormula = columnFormula;
                        //  }
                    }
                } else {

                    String nameQuery = "SELECT BUSS_TABLE_NAME,NVL(BUSS_COL_NAME,USER_COL_NAME),element_id,nvl(USER_COL_DESC, user_col_name),user_col_type,ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,AGGREGATION_TYPE,length(nvl(USER_COL_DESC, user_col_name))   FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id IN(" + dependenteleids + ") order by length(nvl(USER_COL_DESC, user_col_name)) desc";
                    PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
                    String bussNamecolName = "";
                    String onlyColName = "";
                    for (int n = 0; n < pbro.getRowCount(); n++) {
                        if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                            colType = "summarized";
                        }
                        bussNamecolName = pbro.getFieldValueString(n, 1);
                        //  onlyColName = String.valueOf(pbro.getFieldValueInt(n, 2));
                        onlyColName = String.valueOf(pbro.getFieldValueString(n, 3));
                        bussNamecolName = bussNamecolName.toUpperCase();
                        onlyColName = onlyColName.toUpperCase();
                        if (pbro.getFieldValueString(n, 4).equalsIgnoreCase("calculated") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarized") || pbro.getFieldValueString(n, 4).equalsIgnoreCase("summarised")) {
                            formulaold = "(" + pbro.getFieldValueString(n, 5) + ")";
                            columnFormula = columnFormula.replace(onlyColName, formulaold);
                            if (!(pbro.getFieldValueString(n, 6).equalsIgnoreCase(""))) {
                                dependenteleids = dependenteleids + "," + pbro.getFieldValueString(n, 6);
                            }
                        } else {
                            String a1 = pbro.getFieldValueString(n, 7).toUpperCase() + "(" + onlyColName + ")";
                            String a2[] = new String[summarisationList.length];
                            for (int m = 0; m < summarisationList.length; m++) {
                                a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                            }
                            if (columnFormula.contains(a1)) {
                                bussNamecolName = pbro.getFieldValueString(n, 1);
                            } else {

                                int testcount = 0;
                                for (int m = 0; m < a2.length; m++) {
                                    if (columnFormula.contains(a2[m])) {
                                        testcount = 1;
                                    }
                                }
                                if (testcount == 0) {
                                    bussNamecolName = pbro.getFieldValueString(n, 7) + "(" + pbro.getFieldValueString(n, 1) + ")";
                                } else {
                                    bussNamecolName = pbro.getFieldValueString(n, 1);
                                }

                            }
                            columnFormula = columnFormula.replace(onlyColName, bussNamecolName);
                        }
                    }

                }

            }


            //   string folderIdQuery="SELECT  FOLDER_ID, FOLDER_NAME, ELEMENT_ID FROM PRG_USER_ALL_INFO_DETAILS where element_id in("++")";
            String folderId = folderIds.split(",")[0];
            String folderdets = "select distinct GRP_ID, FOLDER_ID, FOLDER_NAME,CONNECTION_ID from PRG_USER_ALL_INFO_DETAILS where FOLDER_ID=" + folderId;
            // String folderdets = "select distinct GRP_ID, FOLDER_ID, FOLDER_NAME,CONNECTION_ID from PRG_USER_ALL_INFO_DETAILS where FOLDER_ID in( select distinct folder_id from prg_user_all_info_details where element_id in ("+dependenteleids+")";
            PbReturnObject pbrofolderdet = pbdb.execSelectSQL(folderdets);
            String folderName = pbrofolderdet.getFieldValueString(0, 2);
            String connectionId = pbrofolderdet.getFieldValueString(0, 3);
            String grpId = String.valueOf(pbrofolderdet.getFieldValueInt(0, 0));
            columnFormula = columnFormula.toUpperCase();







            //  aggType = "sum";

            /*
             * String addUserFolderDetail =
             * resBundle.getString("addUserFolderDetail");
             *
             * String existFolderQuery = "SELECT Distinct SUB_FOLDER_ID FROM
             * PRG_USER_FOLDER_DETAIL where folder_id =" + folderId + " and
             * SUB_FOLDER_TYPE='Formula'"; PbReturnObject pbroext =
             * pbdb.execSelectSQL(existFolderQuery); String subfolderId = ""; if
             * (pbroext.getRowCount() > 0) { subfolderId =
             * String.valueOf(pbroext.getFieldValueInt(0, 0)); } else { String
             * subFolderIdQuery = "select PRG_USER_FOLDER_DETAIL_SEQ.nextval
             * from dual"; PbReturnObject pbrofolderdetseq =
             * pbdb.execSelectSQL(subFolderIdQuery); subfolderId =
             * String.valueOf(pbrofolderdetseq.getFieldValueInt(0, 0)); Object
             * obj[] = new Object[4]; obj[0] = folderId; obj[1] = subfolderId;
             * obj[2] = "Formula"; obj[3] = "Formula"; finalQuery =
             * pbdb.buildQuery(addUserFolderDetail, obj); list.add(finalQuery);
             *
             * }
             */

            //only for single group

            if (columnFormula.indexOf("SUM(") >= 0 || columnFormula.indexOf("COUNT(*") >= 0 || columnFormula.indexOf("AVG(") >= 0 || columnFormula.indexOf("MIN(") >= 0 || columnFormula.indexOf("MAX(") >= 0 || columnFormula.indexOf("POWER(") >= 0 || columnFormula.indexOf("SQRT(") >= 0 || columnFormula.indexOf("ROUND(") > 0 || columnFormula.indexOf("ABS(") >= 0) {
                colType = "summarised";
            } else {
                colType = "calculated";
            }

            String existFolderQuery = "SELECT Distinct SUB_FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE FROM PRG_USER_FOLDER_DETAIL where folder_id =" + folderId + " and SUB_FOLDER_TYPE='Facts'";
            PbReturnObject pbroext = pbdb.execSelectSQL(existFolderQuery);
            int subfolderId = 0;
            String subFolderName = "";
            String subFolderType = "";
            if (pbroext.getRowCount() > 0) {
                subfolderId = pbroext.getFieldValueInt(0, 0);
                subFolderName = pbroext.getFieldValueString(0, 1);
                subFolderType = pbroext.getFieldValueString(0, 2);
            }

            String finalUserIds[] = dependenteleids.split(",");
//            String dependenteleids1 = "";
            StringBuilder dependenteleids1 = new StringBuilder(300);
            for (int j = 0; j < finalUserIds.length - 1; j++) {
                int count = 0;
                for (int j1 = j + 1; j1 < finalUserIds.length; j1++) {

                    if (finalUserIds[j].equalsIgnoreCase(finalUserIds[j1])) {
                        count = 1;
                        break;
                    }
                }
                if (count == 0) {
//                    dependenteleids1 += "," + finalUserIds[j];
                    dependenteleids1.append(",").append(finalUserIds[j]);
                }
                if (j == finalUserIds.length - 2) {
//                     dependenteleids1 += "," + finalUserIds[j + 1];
                    dependenteleids1.append(",").append(finalUserIds[j + 1]);
                }

            }

//            if (!dependenteleids1.equalsIgnoreCase("")) {
//                 dependenteleids1 = dependenteleids1.substring(1);
//                dependenteleids = dependenteleids1;
//            }
            if (dependenteleids1.length() > 0) {
                dependenteleids = dependenteleids1.substring(1);
            }
            columnName = columnName.trim();
            columnName = columnName.replace("#", "_");
            columnName = columnName.replace("&", "_");
            columnName = columnName.replace("!", "_");
            columnName = columnName.replace("@", "_");
            columnName = columnName.replace("(", "_");
            columnName = columnName.replace(")", "_");
            columnName = columnName.replace("[", "_");
            columnName = columnName.replace("]", "_");
            columnName = columnName.replace("{", "_");
            columnName = columnName.replace("}", "_");
            columnName = columnName.replace(" ", "_");

            String columnNamesList[] = new String[4];
            columnNamesList[0] = columnName;
            columnNamesList[1] = "Prior_" + columnName;
            columnNamesList[2] = "Change_" + columnName;
            columnNamesList[3] = "Change%_" + columnName;

            String columnDescList[] = new String[4];
            columnDescList[0] = columnName.replace("_", " ");
            columnDescList[1] = "Prior " + columnName.replace("_", " ");
            columnDescList[2] = "Change " + columnName.replace("_", " ");
            columnDescList[3] = "Change% " + columnName.replace("_", " ");
            int len = 1;
            if (iscalculate.equalsIgnoreCase("Y")) {
                len = 4;
            }

            //adding to subfolder element types
            String ref_elementId = "";
            for (int k = 0; k < len; k++) {

                String addSubFolderElements = resBundle.getString("addSubFolderElements");
                String elementIdQuery = "select PRG_USER_SUB_FLDR_ELEMENTS_SEQ.nextval from dual";
                PbReturnObject pbrofolderelement = pbdb.execSelectSQL(elementIdQuery);
                String elementId = String.valueOf(pbrofolderelement.getFieldValueInt(0, 0));
                if (k == 0) {
                    ref_elementId = elementId;
                }
                Object obj1[] = new Object[16];
                obj1[0] = elementId;
                obj1[1] = subfolderId;
                obj1[2] = bussTableId;
                obj1[3] = "0";
                obj1[4] = columnName;
                obj1[5] = columnNamesList[k];
                obj1[6] = columnDescList[k];
                obj1[7] = colType;
                obj1[8] = subFolderTabId;
                obj1[9] = elementId;
                obj1[10] = "1";
                obj1[11] = aggType;
                obj1[12] = "";
                obj1[13] = "";
                obj1[14] = dependenteleids;
                obj1[15] = displayFormula;
                finalQuery = pbdb.buildQuery(addSubFolderElements, obj1);
                list.add(finalQuery);


                //adding to user all Info
                String addUserAllInfoDets = resBundle.getString("addUserAllInfoDets");

                Object obj2[] = new Object[33];
                obj2[0] = grpId;
                obj2[1] = folderId;
                obj2[2] = folderName;
                obj2[3] = subfolderId;
                obj2[4] = subFolderName;//"Formula";
                obj2[5] = subFolderType;//"Formula";
                obj2[6] = subFolderTabId;
                obj2[7] = "N";
                obj2[8] = "Y";
                obj2[9] = "N";
                obj2[10] = "";
                obj2[11] = "0";
                obj2[12] = "0";
                obj2[13] = "0";
                obj2[14] = "";
                obj2[15] = elementId;
                obj2[16] = bussTableId;
                obj2[17] = "0";
                obj2[18] = columnName;
                obj2[19] = columnNamesList[k];
                obj2[20] = columnDescList[k];
                obj2[21] = colType;
                obj2[22] = ref_elementId;
                obj2[23] = "1";
                obj2[24] = "0";
                obj2[25] = "";
                obj2[26] = "";
                obj2[27] = bussTableName;
                obj2[28] = connectionId;
                obj2[29] = aggType;
                obj2[30] = columnFormula;
                obj2[31] = dependenteleids;
                obj2[32] = displayFormula;
                finalQuery = pbdb.buildQuery(addUserAllInfoDets, obj2);
                list.add(finalQuery);
            }

            pbdb.executeMultiple(list);
            // if(request.getParameter("sourcePage").equalsIgnoreCase("Viewer")){
            //    return mapping.findForward("viewerFormula");
            // }else{
            return mapping.findForward(SUCCESS);
            // }
        } catch (Exception ex) {
            return mapping.findForward("exceptionPage");
        }

    }
}
