package prg.business.group;

import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class AddColumnFormula extends Action {

    public static Logger logger = Logger.getLogger(AddColumnFormula.class);

    private ResourceBundle getResourceBundle() {
        ResourceBundle resourceBundle = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            resourceBundle = new PbBussGrpResBundleSqlServer();
        } else {
            resourceBundle = new PbBussGrpResourceBundle();
        }
        return resourceBundle;
    }
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        PbDb pbdb = new PbDb();

        try {
            //int table_id = Integer.parseInt(request.getParameter("tableId"));
            String column_name = request.getParameter("colName");
            String column_formula = request.getParameter("txt2");
            column_formula = column_formula.replace("@", "+");
            String agregationtype = request.getParameter("agretype");
            String grpId = request.getParameter("groupId");
            String connId = request.getParameter("connId");
            String tArea = request.getParameter("tArea");
            String tArea1 = request.getParameter("tArea1");

            ////.println("tArea---" + tArea);
            ////.println("tArea1---" + tArea1);
            ////.println("column_formula===" + column_formula);
            String colFol = column_formula.replace("'", "''");
            column_formula = column_formula.replace("'", "''");
            String DisplayFormula = column_formula;
            String calc = "calculated";
            String[] verify = {"nvl(", "sum(", "avg", "count(", "Count(Distinct", "NVL(", "SUM(", "AVG", "COUNT(", "COUNT(DISTINCT"};
            String summarisationList[] = {"SUM", "AVG", "COUNT", "MAX", "MIN", "ROUND"};
            boolean checking;
            for (int i = 0; i < verify.length; i++) {
                checking = column_formula.contains(verify[i]);
                if (checking == true) {
                    //    ////////////////////////////////////////////////////////////////////////////////.println.println("------");
                    calc = "summarised";
                }
            }

            String a = tArea1.trim().substring(1);
            String eleList2[] = a.split("~");
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
//                        eleList1 += "," + eleList2[j + 1];
                        eleList1.append(",").append(eleList2[j + 1]);
                    }

                }

//                if (!eleList1.equalsIgnoreCase("")) {
//                    eleList1 = eleList1.substring(1);
//                }
                if (eleList1.length() > 0) {
//                    eleList1 = eleList1.substring(1);
                    eleList1 = new StringBuilder(eleList1.substring(1));
                }
            } else {
                eleList1 = new StringBuilder(eleList2[0]);
            }
            String eleList3[] = eleList1.toString().split(",");

            String dependenteleids = "";
            for (int p = 0; p < eleList3.length; p++) {
                boolean check = tArea.contains(eleList3[p]);
                if (check == true) {
                    dependenteleids += "," + eleList3[p];
                }
                if (p == eleList3.length - 1) {

                    dependenteleids = dependenteleids.substring(1);
                }
            }
            String updatedDependentIds = "";
            String updatedIds = " SELECT Distinct d.BUSS_TABLE_ID,d.buss_column_id FROM PRG_GRP_BUSS_TABLE_DETAILS d ,PRG_GRP_BUSS_TABLE m where d.buss_column_id in(" + dependenteleids + ") and m.BUSS_TABLE_NAME!='Calculated Facts' and d.buss_table_id=m.buss_table_id";
            PbReturnObject updatedIdspbro = pbdb.execSelectSQL(updatedIds);
            for (int i = 0; i < updatedIdspbro.getRowCount(); i++) {
                updatedDependentIds += "," + updatedIdspbro.getFieldValueInt(i, 0) + "-" + updatedIdspbro.getFieldValueInt(i, 1);

            }
            if (!updatedDependentIds.equalsIgnoreCase("")) {
                updatedDependentIds = updatedDependentIds.substring(1);
            }
            String dependenteleidslist[] = updatedDependentIds.split(",");
//            int depIscount = dependenteleidslist.length;
            String colNamenew = column_name.trim();
            String colNameforInsert = "";
            colNamenew = colNamenew.replace("#", "_");
            colNamenew = colNamenew.replace("&", "_");
            colNamenew = colNamenew.replace("!", "_");
            colNamenew = colNamenew.replace("@", "_");
            colNamenew = colNamenew.replace("(", "_");
            colNamenew = colNamenew.replace(")", "_");
            colNamenew = colNamenew.replace("[", "_");
            colNamenew = colNamenew.replace("]", "_");
            colNamenew = colNamenew.replace("{", "_");
            colNamenew = colNamenew.replace("}", "_");
            colNamenew = colNamenew.replace(" ", "_");
            colNameforInsert = colNamenew;
            if (colNamenew.length() > 30) {
                colNameforInsert = colNameforInsert.substring(0, 30);
            }
            String finalQuery = "";
            BussColumnFormulaHelper formulaHelper = new BussColumnFormulaHelper(getResourceBundle(),
                    ProgenConnection.getInstance().getDatabaseType(), grpId, connId);
            formulaHelper.initializeBusinessColumns(dependenteleids.split(","));
            BusinessColumn targetColumn = new BusinessColumn.BusinessColumnBuilder().bussColName(colNamenew.toUpperCase()).actualColFormula(column_formula).defaultAggregation(agregationtype).displayFormula(DisplayFormula).colDispDesc(colNamenew).colDispName(colNamenew).bussSrcId(0).refElements("").build();
            if (!formulaHelper.isSingleTableFormula()) {
                formulaHelper.checkAndCreateCalculatedFactFolder();
            }

            targetColumn = formulaHelper.evaluateFormula(targetColumn);
            formulaHelper.insertFormula(targetColumn);


            if (formulaHelper == null) {


                ArrayList list = new ArrayList();
                int seqaddFormulaBussMater = 0;
                int seqaddFormulaSrc = 0;
                column_formula = column_formula.toUpperCase();
                String bussTableName = "";
                String doubleExist = " SELECT Distinct d.BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE_DETAILS d ,PRG_GRP_BUSS_TABLE m where d.buss_column_id in(" + dependenteleids + ")  and d.buss_table_id=m.buss_table_id";

                // String doubleExist=" SELECT  BUSS_TABLE_ID  FROM PRG_GRP_BUSS_TABLE_DETAILS where buss_column_id in("+dependenteleids+")";
                PbReturnObject pbrodoubleExist = pbdb.execSelectSQL(doubleExist);
                if (pbrodoubleExist.getRowCount() > 1) {

                    //Facts from multiple tables

                    String sql = "select buss_source_id, buss_table_id  from prg_grp_buss_table_src  where buss_table_id in (select buss_table_id from prg_grp_buss_table where buss_table_name='Calculated Facts' and grp_id=" + grpId + ")";
                    // //////////////////////////////////////////////////////////////.println.println("sql---"+sql);

                    PbReturnObject pbroexist = pbdb.execSelectSQL(sql);
                    // //////////////////////////////////////////////////////////////.println.println("pbroexist.getRowCount()--"+pbroexist.getRowCount());
                    pbroexist.writeString();
                    if (pbroexist.getRowCount() > 0) {
                        seqaddFormulaBussMater = pbroexist.getFieldValueInt(0, 1);
                        seqaddFormulaSrc = pbroexist.getFieldValueInt(0, 0);
                    } else {
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            String addFormulaBussMater = getResourceBundle().getString("addFormulaBussMater");
//                        seqaddFormulaBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                            Object obj[];
                            obj = new Object[7];
//                        obj[0] = seqaddFormulaBussMater;
                            obj[0] = "Calculated Facts";
                            obj[1] = "Calculated Facts";
                            obj[2] = "Table";
                            obj[3] = "1";
                            obj[4] = "null";
                            obj[5] = "null";
                            obj[6] = grpId;
                            finalQuery = pbdb.buildQuery(addFormulaBussMater, obj);
                            list.add(finalQuery);
                            // //////////////////////////////////////////////////////////////.println.println("finalQuery---" + finalQuery);
                            String addFormulaSrc = getResourceBundle().getString("addFormulaSrc");
//                        seqaddFormulaSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                            Object obj1[];
                            obj1 = new Object[6];
//                        obj1[0] = seqaddFormulaSrc;
                            obj1[0] = "ident_current('PRG_GRP_BUSS_TABLE')";
                            obj1[1] = "0";
                            obj1[2] = "Table";
                            obj1[3] = "null";
                            obj1[4] = connId;
                            obj1[5] = "null";
                            finalQuery = pbdb.buildQuery(addFormulaSrc, obj1);
                            list.add(finalQuery);
                        } else {
                            String addFormulaBussMater = getResourceBundle().getString("addFormulaBussMater");
                            seqaddFormulaBussMater = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SEQ.nextval from dual");
                            Object obj[];
                            obj = new Object[8];
                            obj[0] = seqaddFormulaBussMater;
                            obj[1] = "Calculated Facts";
                            obj[2] = "Calculated Facts";
                            obj[3] = "Table";
                            obj[4] = "1";
                            obj[5] = "";
                            obj[6] = "";
                            obj[7] = grpId;
                            finalQuery = pbdb.buildQuery(addFormulaBussMater, obj);
                            list.add(finalQuery);
                            // //////////////////////////////////////////////////////////////.println.println("finalQuery---" + finalQuery);
                            String addFormulaSrc = getResourceBundle().getString("addFormulaSrc");
                            seqaddFormulaSrc = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_SRC_SEQ.nextval from dual");
                            Object obj1[];
                            obj1 = new Object[7];
                            obj1[0] = seqaddFormulaSrc;
                            obj1[1] = seqaddFormulaBussMater;
                            obj1[2] = "0";
                            obj1[3] = "Table";
                            obj1[4] = "";
                            obj1[5] = connId;
                            obj1[6] = "";
                            finalQuery = pbdb.buildQuery(addFormulaSrc, obj1);
                            list.add(finalQuery);
                        }

                        // //////////////////////////////////////////////////////////////.println.println("finalQuery---" + finalQuery);
                    }
                    /*
                     * for (int i = 0; i < verify.length; i++) { checking =
                     * column_formula.contains(verify[i]); if (checking == true)
                     * {
                     *
                     * calc = "summarised"; break; } else { calc = "calculated";
                     *
                     * }
                     * }
                     */
                    String formulaold = "";
                    String doubleExist1 = " SELECT Distinct d.BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE_DETAILS d ,PRG_GRP_BUSS_TABLE m where d.buss_column_id in(" + dependenteleids + ") and m.BUSS_TABLE_NAME!='Calculated Facts'  and d.buss_table_id=m.buss_table_id";
                    PbReturnObject pbrodoubleExist1 = pbdb.execSelectSQL(doubleExist1);

                    if (pbrodoubleExist.getRowCount() == pbrodoubleExist1.getRowCount()) {
                        ////.println("first if ");

                        String coldetssql = "select d.COLUMN_NAME,m.BUSS_TABLE_NAME,d.COLUMN_TYPE,d.ACTUAL_COL_FORMULA,d.REFFERED_ELEMENTS,d.DEFAULT_AGGREGATION from PRG_GRP_BUSS_TABLE_DETAILS d,PRG_GRP_BUSS_TABLE m where d.buss_column_id in (" + dependenteleids + ") and d.BUSS_TABLE_ID=m.BUSS_TABLE_ID";
                        PbReturnObject coldetssqlpbro = pbdb.execSelectSQL(coldetssql);
                        String coldetssql1 = "select d.COLUMN_NAME,m.BUSS_TABLE_NAME,d.COLUMN_TYPE,d.ACTUAL_COL_FORMULA,d.REFFERED_ELEMENTS,d.DEFAULT_AGGREGATION from PRG_GRP_BUSS_TABLE_DETAILS d,PRG_GRP_BUSS_TABLE m where d.buss_column_id in (" + dependenteleids + ") and d.BUSS_TABLE_ID=m.BUSS_TABLE_ID and d.COLUMN_TYPE in('calculated','summarized','summarised')";
                        PbReturnObject coldetssqlpbro1 = pbdb.execSelectSQL(coldetssql1);
                        String bussNamecolName = "";
                        String onlyColName = "";

                        int countforcaltest = 0;
                        for (int i = 0; i < coldetssqlpbro.getRowCount(); i++) {
                            if (coldetssqlpbro.getFieldValueString(i, 2).equalsIgnoreCase("summarized") || coldetssqlpbro.getFieldValueString(i, 2).equalsIgnoreCase("summarised") || coldetssqlpbro.getFieldValueString(i, 2).equalsIgnoreCase("summarized")) {
                                calc = "summarised";
                            }
                        }
                        for (int i = 0; i < coldetssqlpbro.getRowCount(); i++) {

                            bussNamecolName = coldetssqlpbro.getFieldValueString(i, 0);
                            onlyColName = coldetssqlpbro.getFieldValueString(i, 0);
                            bussNamecolName = bussNamecolName.toUpperCase();
                            onlyColName = onlyColName.toUpperCase();

                            if (coldetssqlpbro.getFieldValueString(i, 2).equalsIgnoreCase("calculated") || coldetssqlpbro.getFieldValueString(i, 2).equalsIgnoreCase("summarized") || coldetssqlpbro.getFieldValueString(i, 2).equalsIgnoreCase("summarised")) {
                                if (!(coldetssqlpbro.getFieldValueString(i, 4).equalsIgnoreCase(""))) {
                                    updatedDependentIds = updatedDependentIds + "," + coldetssqlpbro.getFieldValueString(i, 4);
                                }


                                String a1 = "SUM(" + onlyColName + ")";
                                String a2[] = new String[summarisationList.length];
                                for (int m = 0; m < summarisationList.length; m++) {
                                    a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                }
                                if (column_formula.contains(a1)) {
                                    bussNamecolName = coldetssqlpbro.getFieldValueString(i, 0);

                                } else {
                                    int testcount = 0;
                                    for (int m = 0; m < a2.length; m++) {
                                        if (column_formula.contains(a2[m])) {
                                            testcount = 1;
                                        }
                                    }
                                    if (testcount == 0) {
                                        bussNamecolName = "SUM(" + coldetssqlpbro.getFieldValueString(i, 0) + ")";
                                    } else {
                                        bussNamecolName = coldetssqlpbro.getFieldValueString(i, 0);
                                    }




                                }
                            } else {
                                /*
                                 * String testexist = "(" +
                                 * coldetssqlpbro.getFieldValueString(i, 0) +
                                 * ")"; ////.println("testexist=d=" +
                                 * testexist); if
                                 * (column_formula.toUpperCase().contains(testexist.toUpperCase()))
                                 * { ////.println("in if"); onlyColName =
                                 * coldetssqlpbro.getFieldValueString(i, 0); }
                                 * else {
                                 * if(calc.equalsIgnoreCase("calculated")){
                                 * onlyColName =
                                 * coldetssqlpbro.getFieldValueString(i,
                                 * 5)+"("+coldetssqlpbro.getFieldValueString(i,
                                 * 0)+")"; countforcaltest=1; }else{ onlyColName
                                 * = coldetssqlpbro.getFieldValueString(i, 0); }
                                 * } column_formula =
                                 * column_formula.toUpperCase(); column_formula
                                 * = column_formula.replace(bussNamecolName,
                                 * onlyColName);
                                 */

                                calc = "summarized";
                                String a1 = coldetssqlpbro.getFieldValueString(i, 5).toUpperCase() + "(" + onlyColName + ")";
                                String a2[] = new String[summarisationList.length];
                                for (int m = 0; m < summarisationList.length; m++) {
                                    a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                }

                                if (column_formula.contains(a1)) {
                                    bussNamecolName = coldetssqlpbro.getFieldValueString(i, 0);
                                } else {

                                    int testcount = 0;
                                    for (int m = 0; m < a2.length; m++) {
                                        if (column_formula.contains(a2[m])) {
                                            testcount = 1;
                                        }
                                    }
                                    if (testcount == 0) {
                                        bussNamecolName = coldetssqlpbro.getFieldValueString(i, 5) + "(" + coldetssqlpbro.getFieldValueString(i, 0) + ")";
                                    } else {
                                        bussNamecolName = coldetssqlpbro.getFieldValueString(i, 0);
                                    }




                                }

                            }
                            // column_formula = column_formula.toUpperCase();
                            column_formula = column_formula.replace(onlyColName, bussNamecolName);

                        }
                    } else {
                        ////.println("2ndif ");
                        String coldetssql = "select d.COLUMN_NAME,m.BUSS_TABLE_NAME,d.COLUMN_TYPE,d.ACTUAL_COL_FORMULA,d.REFFERED_ELEMENTS,d.DEFAULT_AGGREGATION from PRG_GRP_BUSS_TABLE_DETAILS d,PRG_GRP_BUSS_TABLE m where d.buss_column_id in (" + dependenteleids + ") and d.BUSS_TABLE_ID=m.BUSS_TABLE_ID";
                        PbReturnObject coldetssqlpbro = pbdb.execSelectSQL(coldetssql);
                        String bussNamecolName = "";
                        String onlyColName = "";
                        for (int n = 0; n < coldetssqlpbro.getRowCount(); n++) {

                            bussNamecolName = coldetssqlpbro.getFieldValueString(n, 0);
                            onlyColName = String.valueOf(coldetssqlpbro.getFieldValueString(n, 0));
                            bussNamecolName = bussNamecolName.toUpperCase();
                            onlyColName = onlyColName.toUpperCase();
                            if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("calculated") || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarized") || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarised")) {
                                formulaold = "(" + coldetssqlpbro.getFieldValueString(n, 3) + ")";
                                column_formula = column_formula.replace(onlyColName, formulaold);
                                if (!(coldetssqlpbro.getFieldValueString(n, 4).equalsIgnoreCase(""))) {
                                    updatedDependentIds = updatedDependentIds + "," + coldetssqlpbro.getFieldValueString(n, 4);
                                }
                                //////// //////.println("----in if---");
                            } else {
                                String a1 = coldetssqlpbro.getFieldValueString(n, 5).toUpperCase() + "(" + onlyColName + ")";
                                //   ////.println("columnFormula"+columnFormula);
                                //  ////.println("a1=="+a1);
                                String a2[] = new String[summarisationList.length];
                                for (int m = 0; m < summarisationList.length; m++) {
                                    a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                }
                                if (column_formula.contains(a1)) {
                                    bussNamecolName = coldetssqlpbro.getFieldValueString(n, 0);
                                } else {

                                    int testcount = 0;
                                    for (int m = 0; m < a2.length; m++) {
                                        if (column_formula.contains(a2[m])) {
                                            testcount = 1;
                                        }
                                    }
                                    if (testcount == 0) {
                                        bussNamecolName = coldetssqlpbro.getFieldValueString(n, 5) + "(" + coldetssqlpbro.getFieldValueString(n, 0) + ")";
                                    } else {
                                        bussNamecolName = coldetssqlpbro.getFieldValueString(n, 0);
                                    }

                                }
                                column_formula = column_formula.replace(onlyColName, bussNamecolName);
                            }
                        }

                    }

                    String finalUserIds[] = updatedDependentIds.split(",");
                    String updatedDependentIds1 = "";
                    for (int j = 0; j < finalUserIds.length - 1; j++) {
                        int count = 0;
                        for (int j1 = j + 1; j1 < finalUserIds.length; j1++) {
                            //
                            if (finalUserIds[j].equalsIgnoreCase(finalUserIds[j1])) {
                                //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("equal");
                                count = 1;
                                break;
                            }
                        }
                        if (count == 0) {

                            updatedDependentIds1 += "," + finalUserIds[j];
                        }
                        if (j == finalUserIds.length - 2) {

                            updatedDependentIds1 += "," + finalUserIds[j + 1];
                        }

                    }

                    if (!updatedDependentIds1.equalsIgnoreCase("")) {
                        updatedDependentIds1 = updatedDependentIds1.substring(1);
                        updatedDependentIds = updatedDependentIds1;
                    }
                    //add grp buss src details
                    int srcdetnextval = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                    String addFormulasrcDetails = getResourceBundle().getString("addFormulasrcDetails");
                    Object obj2[] = new Object[7];
                    obj2[0] = srcdetnextval;
                    obj2[1] = seqaddFormulaSrc;
                    obj2[2] = "0";
                    obj2[3] = seqaddFormulaBussMater;
                    obj2[4] = colNamenew.replaceAll("_", " ");
                    obj2[5] = calc;//"summarised";//for two tablwes
                    obj2[6] = "0";

                    finalQuery = pbdb.buildQuery(addFormulasrcDetails, obj2);
                    list.add(finalQuery);
                    // //////////////////////////////////////////////////////////////.println.println("finalQuery---" + finalQuery);
                    String addFormulaBussDetails = getResourceBundle().getString("addFormulaBussDetails");
                    int seqaddFormulaBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                    Object obj3[] = new Object[15];
                    obj3[0] = seqaddFormulaBussDetails;
                    obj3[1] = seqaddFormulaBussMater;
                    obj3[2] = colNameforInsert;
                    obj3[3] = "0";
                    obj3[4] = srcdetnextval;
                    obj3[5] = calc;//"summarised"; //for two tablwes
                    obj3[6] = "0";
                    obj3[7] = colNamenew.replaceAll("_", " ");
                    obj3[8] = "0";
                    obj3[9] = "N";
                    obj3[10] = column_formula;
                    obj3[11] = agregationtype;
                    obj3[12] = colNamenew.replaceAll("_", " ");
                    obj3[13] = updatedDependentIds;
                    obj3[14] = "Y";
                    finalQuery = pbdb.buildQuery(addFormulaBussDetails, obj3);
                    list.add(finalQuery);
                    System.out.println("finalQuery---" + finalQuery);

                    pbdb.executeMultiple(list);


                } else {
                    /*
                     * for (int i = 0; i < verify.length; i++) { checking =
                     * column_formula.contains(verify[i]); if (checking == true)
                     * { ////.println("in calc if"); calc = "summarised"; break;
                     * } else { ////.println("in calc else"); calc =
                     * "calculated"; } }
                     */
                    ////.println("3rddif ");

                    //Facts from Single Table

                    String doubleExist1 = " SELECT Distinct d.BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE_DETAILS d ,PRG_GRP_BUSS_TABLE m where d.buss_column_id in(" + dependenteleids + ") and m.BUSS_TABLE_NAME!='Calculated Facts'  and d.buss_table_id=m.buss_table_id";
                    PbReturnObject pbrodoubleExist1 = pbdb.execSelectSQL(doubleExist1);

                    if (pbrodoubleExist.getRowCount() == pbrodoubleExist1.getRowCount()) {
                        //No Calculated facts in the formula
                        String coldetssql = "select d.COLUMN_NAME,m.BUSS_TABLE_NAME,d.COLUMN_TYPE,d.ACTUAL_COL_FORMULA,d.REFFERED_ELEMENTS,d.DEFAULT_AGGREGATION from PRG_GRP_BUSS_TABLE_DETAILS d,PRG_GRP_BUSS_TABLE m where d.buss_column_id in (" + dependenteleids + ") and d.BUSS_TABLE_ID=m.BUSS_TABLE_ID";
                        PbReturnObject coldetssqlpbro = pbdb.execSelectSQL(coldetssql);
                        String coldetssql1 = "select d.COLUMN_NAME,m.BUSS_TABLE_NAME,d.COLUMN_TYPE,d.ACTUAL_COL_FORMULA,d.REFFERED_ELEMENTS,d.DEFAULT_AGGREGATION from PRG_GRP_BUSS_TABLE_DETAILS d,PRG_GRP_BUSS_TABLE m where d.buss_column_id in (" + dependenteleids + ") and d.BUSS_TABLE_ID=m.BUSS_TABLE_ID and d.COLUMN_TYPE in('calculated','summarized','summarised')";
                        PbReturnObject coldetssqlpbro1 = pbdb.execSelectSQL(coldetssql1);
                        String bussNamecolName = "";
                        String onlyColName = "";
                        String formulaold = "";
                        int count = 0;
                        int count1 = 0;
                        for (int n = 0; n < coldetssqlpbro.getRowCount(); n++) {
                            bussNamecolName = coldetssqlpbro.getFieldValueString(n, 1) + "." + coldetssqlpbro.getFieldValueString(n, 0);
                            onlyColName = coldetssqlpbro.getFieldValueString(n, 0);
                            bussNamecolName = bussNamecolName.toUpperCase();
                            onlyColName = onlyColName.toUpperCase();
                            if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("calculated")
                                    || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarized")
                                    || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarised")) {
                                if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarized")
                                        || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarised")) {
                                    calc = "summarized";
                                }

                                if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("calculated") && (coldetssqlpbro.getFieldValueString(n, 5).equalsIgnoreCase("AVG"))) {
                                    if (coldetssqlpbro.getFieldValueString(n, 3).toUpperCase().indexOf("AVG") > 0) {
                                        formulaold = "(" + coldetssqlpbro.getFieldValueString(n, 3) + ")";
                                    } else {
                                        String a1 = coldetssqlpbro.getFieldValueString(n, 5).toUpperCase() + "(" + onlyColName + ")";
                                        //   ////.println("columnFormula"+columnFormula);
                                        //  ////.println("a1=="+a1);
                                        if (column_formula.contains(a1)) {
                                            formulaold = coldetssqlpbro.getFieldValueString(n, 3);
                                        } else {
                                            formulaold = coldetssqlpbro.getFieldValueString(n, 5) + "(" + coldetssqlpbro.getFieldValueString(n, 3) + ")";
                                        }
                                    }
                                    calc = "summarized";
                                } else if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("calculated") && (coldetssqlpbro.getFieldValueString(n, 5).equalsIgnoreCase("COUNT"))) {
                                    if (coldetssqlpbro.getFieldValueString(n, 3).toUpperCase().indexOf("COUNT") > 0) {
                                        formulaold = "(" + coldetssqlpbro.getFieldValueString(n, 3) + ")";
                                    } else {
                                        String a1 = coldetssqlpbro.getFieldValueString(n, 5).toUpperCase() + "(" + onlyColName + ")";
                                        //   ////.println("columnFormula"+columnFormula);
                                        //  ////.println("a1=="+a1);
                                        String a2[] = new String[summarisationList.length];
                                        for (int m = 0; m < summarisationList.length; m++) {
                                            a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                        }
                                        if (column_formula.contains(a1)) {
                                            formulaold = coldetssqlpbro.getFieldValueString(n, 3);
                                        } else {
                                            int testcount = 0;
                                            for (int m = 0; m < a2.length; m++) {
                                                if (column_formula.contains(a2[m])) {
                                                    testcount = 1;
                                                }
                                            }
                                            if (testcount == 0) {
                                                formulaold = coldetssqlpbro.getFieldValueString(n, 5) + "(" + coldetssqlpbro.getFieldValueString(n, 3) + ")";
                                            } else {
                                                formulaold = coldetssqlpbro.getFieldValueString(n, 3);
                                            }

                                        }
                                    }
                                    calc = "summarized";
                                } else if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("calculated") && (!(coldetssqlpbro.getFieldValueString(n, 5).equalsIgnoreCase("COUNT") || (coldetssqlpbro.getFieldValueString(n, 5).equalsIgnoreCase("AVG"))))) {
                                    ////// //////.println("-------in if test");
                                    calc = "summarized";
                                    String a1 = coldetssqlpbro.getFieldValueString(n, 5).toUpperCase() + "(" + onlyColName + ")";
                                    //   ////.println("columnFormula"+columnFormula);
                                    //  ////.println("a1=="+a1);
                                    String a2[] = new String[summarisationList.length];
                                    for (int m = 0; m < summarisationList.length; m++) {
                                        a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                    }
                                    if (column_formula.contains(a1)) {
                                        formulaold = coldetssqlpbro.getFieldValueString(n, 3);
                                    } else {
                                        int testcount = 0;
                                        for (int m = 0; m < a2.length; m++) {
                                            if (column_formula.contains(a2[m])) {
                                                testcount = 1;
                                            }
                                        }
                                        if (testcount == 0) {
                                            formulaold = coldetssqlpbro.getFieldValueString(n, 5) + "(" + coldetssqlpbro.getFieldValueString(n, 3) + ")";
                                        } else {
                                            formulaold = coldetssqlpbro.getFieldValueString(n, 3);
                                        }

                                    }

                                    //////.println("formulaold--"+formulaold);
                                } else if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarized") || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarised")) {
                                    formulaold = "(" + coldetssqlpbro.getFieldValueString(n, 3) + ")";  //don,t delete imp for avg and count
                                    ////.println("formulaold=lll=" + formulaold);
                                }
                                ////// //////.println("formula old=="+formulaold);
                                //////.println("onlyColName=="+onlyColName);
                                //////.println("columnFormula=="+columnFormula);
                                ////.println("columnFormulavg" + column_formula);
                                column_formula = column_formula.replace(onlyColName, formulaold);
                                ////.println("columnFormula" + column_formula);
                                if (!(coldetssqlpbro.getFieldValueString(n, 4).equalsIgnoreCase(""))) {
                                    ////// //////.println("in if 1 cal");
                                    updatedDependentIds = updatedDependentIds + "," + coldetssqlpbro.getFieldValueString(n, 4);

                                }
                                count++;
                                //   if(count1>0){
                                //   colType="calculated";
                                //  }
                            } else {
                                //Column Type Number
                                ////.println("in if else ");
                                if (coldetssqlpbro1.getRowCount() == coldetssqlpbro.getRowCount()) {
                                    ////.println("in if=====");
                                    column_formula = column_formula.replace(onlyColName, bussNamecolName);
                                } else {
                                    //Number
                                    //all column types are Number
                                    String a1 = coldetssqlpbro.getFieldValueString(n, 5).toUpperCase() + "(" + onlyColName + ")";
                                    //   ////.println("columnFormula"+columnFormula);
                                    //  ////.println("a1=="+a1);
                                    String a2[] = new String[summarisationList.length];
                                    for (int m = 0; m < summarisationList.length; m++) {
                                        a2[m] = summarisationList[m].toUpperCase() + "(" + onlyColName + ")";
                                    }
                                    if (column_formula.contains(a1)) {
                                        ////.println("==");
                                        bussNamecolName = coldetssqlpbro.getFieldValueString(n, 1) + "." + coldetssqlpbro.getFieldValueString(n, 0);
                                    } else {
                                        int testcount = 0;
                                        for (int m = 0; m < a2.length; m++) {
                                            if (column_formula.contains(a2[m])) {
                                                testcount = 1;
                                            }
                                        }
                                        if (testcount == 0) {
                                            bussNamecolName = coldetssqlpbro.getFieldValueString(n, 5) + "(" + coldetssqlpbro.getFieldValueString(n, 1) + "." + coldetssqlpbro.getFieldValueString(n, 0) + ")";
                                        } else {
                                            bussNamecolName = coldetssqlpbro.getFieldValueString(n, 1) + "." + coldetssqlpbro.getFieldValueString(n, 0);
                                        }
//                                    ////.println("====y");

                                    }    //////.println("bussNamecolName=="+bussNamecolName);
                                    column_formula = column_formula.replace(onlyColName, bussNamecolName);
                                    calc = "summarized";
                                }
                                // if(count>0){
                                // colType="calculated";
                                // }
                                count1++;
                            }

                        }
                        bussTableName = coldetssqlpbro.getFieldValueString(0, 1);
                        updatedDependentIds = "";
                    } else {
//                    ////.println("e5555rddif ");

                        String formulaold = "";
                        String coldetssql = "select d.COLUMN_NAME,m.BUSS_TABLE_NAME,d.COLUMN_TYPE,d.ACTUAL_COL_FORMULA,d.REFFERED_ELEMENTS,d.DEFAULT_AGGREGATION,d.BUSS_COLUMN_ID from PRG_GRP_BUSS_TABLE_DETAILS d,PRG_GRP_BUSS_TABLE m where d.buss_column_id in (" + dependenteleids + ") and d.BUSS_TABLE_ID=m.BUSS_TABLE_ID";
                        PbReturnObject coldetssqlpbro = pbdb.execSelectSQL(coldetssql);
                        String bussNamecolName = "";
                        String onlyColName = "";
                        for (int n = 0; n < coldetssqlpbro.getRowCount(); n++) {
                            if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarized") || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarised")) {
                                calc = "summarized";
                            }
                            bussNamecolName = coldetssqlpbro.getFieldValueString(n, 0);
                            //  onlyColName = String.valueOf(pbro.getFieldValueInt(n, 2));
                            onlyColName = String.valueOf(coldetssqlpbro.getFieldValueString(n, 0));
                            bussNamecolName = bussNamecolName.toUpperCase();
                            onlyColName = onlyColName.toUpperCase();
                            if (coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("calculated") || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarized") || coldetssqlpbro.getFieldValueString(n, 2).equalsIgnoreCase("summarised")) {
                                String coldetssql1 = "select d.COLUMN_NAME,m.BUSS_TABLE_NAME,d.COLUMN_TYPE,d.ACTUAL_COL_FORMULA,d.REFFERED_ELEMENTS,d.DEFAULT_AGGREGATION from PRG_GRP_BUSS_TABLE_DETAILS d,PRG_GRP_BUSS_TABLE m where d.buss_column_id in (" + coldetssqlpbro.getFieldValueInt(n, 6) + ") and d.BUSS_TABLE_ID=m.BUSS_TABLE_ID";
                                PbReturnObject coldetssqlpbro1 = pbdb.execSelectSQL(coldetssql);
                                //////// //////.println("NAMEQUERY2==="+nameQuery2);
                                if (!(coldetssqlpbro1.getFieldValueString(0, 4).equalsIgnoreCase(""))) {
                                    updatedDependentIds = updatedDependentIds + "," + coldetssqlpbro1.getFieldValueString(0, 4);
                                }
                                //////// //////.println("dependenteleids---"+dependenteleids);
                                bussNamecolName = "(" + String.valueOf(coldetssqlpbro1.getFieldValueString(0, 3)) + ")";
                                //////// //////.println("----in if--2-");
                            }
                            //else{
                            //////// //////.println("----in else--2-");
                            column_formula = column_formula.replace(onlyColName, bussNamecolName);
                            //  }
                        }


                    }





                    ////.println("------" + column_formula);

                    String finalUserIds[] = updatedDependentIds.split(",");
                    String updatedDependentIds1 = "";
                    for (int j = 0; j < finalUserIds.length - 1; j++) {
                        int count = 0;
                        for (int j1 = j + 1; j1 < finalUserIds.length; j1++) {
                            //
                            if (finalUserIds[j].equalsIgnoreCase(finalUserIds[j1])) {
                                //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("equal");
                                count = 1;
                                break;
                            }
                        }
                        if (count == 0) {

                            updatedDependentIds1 += "," + finalUserIds[j];
                        }
                        if (j == finalUserIds.length - 2) {

                            updatedDependentIds1 += "," + finalUserIds[j + 1];
                        }

                    }

                    if (!updatedDependentIds1.equalsIgnoreCase("")) {
                        updatedDependentIds1 = updatedDependentIds1.substring(1);
                        updatedDependentIds = updatedDependentIds1;
                    }


                    String sql = "select buss_source_id, buss_table_id  from prg_grp_buss_table_src  where buss_table_id in (" + pbrodoubleExist.getFieldValueInt(0, 0) + ")";
                    // //////////////////////////////////////////////////////////////.println.println("sql---"+sql);

                    PbReturnObject pbroexist = pbdb.execSelectSQL(sql);
                    //add grp buss src details
                    int srcdetnextval = 0;
                    String addFormulasrcDetails = getResourceBundle().getString("addFormulasrcDetails");
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    srcdetnextval = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                        Object obj2[] = new Object[6];
//                    obj2[0] = srcdetnextval;
                        obj2[0] = pbroexist.getFieldValueInt(0, 0);
                        obj2[1] = "0";
                        obj2[2] = pbroexist.getFieldValueInt(0, 1);
                        obj2[3] = colNamenew.replaceAll("_", " ");
                        obj2[4] = calc;//"calculated";
                        obj2[5] = "0";

                        finalQuery = pbdb.buildQuery(addFormulasrcDetails, obj2);

                    } else {
                        srcdetnextval = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
                        Object obj2[] = new Object[7];
                        obj2[0] = srcdetnextval;
                        obj2[1] = pbroexist.getFieldValueInt(0, 0);
                        obj2[2] = "0";
                        obj2[3] = pbroexist.getFieldValueInt(0, 1);
                        obj2[4] = colNamenew.replaceAll("_", " ");
                        obj2[5] = calc;//"calculated";
                        obj2[6] = "0";

                        finalQuery = pbdb.buildQuery(addFormulasrcDetails, obj2);
                    }



                    list.add(finalQuery);
                    ////.println("finalQuery---" + finalQuery);
                    String addFormulaBussDetails = getResourceBundle().getString("addFormulaBussDetails");
                    int seqaddFormulaBussDetails = 0;
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                        seqaddFormulaBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                        Object obj3[] = new Object[15];
//                    obj3[0] = seqaddFormulaBussDetails;
                        obj3[0] = pbroexist.getFieldValueInt(0, 1);
                        obj3[1] = colNameforInsert.replaceAll("_", " ");
                        obj3[2] = "0";
                        obj3[3] = "IDENT_CURRENT('PRG_GRP_BUSS_TAB_SRC_DTLS')";
                        obj3[4] = calc;// "calculated";
                        obj3[5] = "0";
                        obj3[6] = colNamenew.replaceAll("_", " ");
                        obj3[7] = "0";
                        obj3[8] = "N";
                        obj3[9] = column_formula;
                        obj3[10] = agregationtype;
                        obj3[11] = colNamenew.replaceAll("_", " ");
                        obj3[12] = updatedDependentIds;
                        obj3[13] = "Y";
                        obj3[14] = DisplayFormula;
                        finalQuery = pbdb.buildQuery(addFormulaBussDetails, obj3);
                    } else {
                        seqaddFormulaBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
                        Object obj3[] = new Object[16];
                        obj3[0] = seqaddFormulaBussDetails;
                        obj3[1] = pbroexist.getFieldValueInt(0, 1);
                        obj3[2] = colNameforInsert.replaceAll("_", " ");
                        obj3[3] = "0";
                        obj3[4] = srcdetnextval;
                        obj3[5] = calc;// "calculated";
                        obj3[6] = "0";
                        obj3[7] = colNamenew.replaceAll("_", " ");
                        obj3[8] = "0";
                        obj3[9] = "N";
                        obj3[10] = column_formula;
                        obj3[11] = agregationtype;
                        obj3[12] = colNamenew.replaceAll("_", " ");
                        obj3[13] = updatedDependentIds;
                        obj3[14] = "Y";
                        obj3[15] = DisplayFormula;
                        finalQuery = pbdb.buildQuery(addFormulaBussDetails, obj3);
                    }
                    list.add(finalQuery);
                    pbdb.executeMultiple(list);
                }
            }

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return mapping.findForward(SUCCESS);
    }
}
