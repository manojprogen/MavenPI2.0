/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class AddColumnParamFormula extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(AddColumnParamFormula.class);
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



        PbBussGrpResourceBundle resourceBundle = new PbBussGrpResourceBundle();
        PbDb pbdb = new PbDb();

        try {
            //int table_id = Integer.parseInt(request.getParameter("tableId"));
            String column_name = request.getParameter("colName");
            String column_formula = request.getParameter("txt2");
            column_formula = column_formula.replace("@", "+");
            String agregationtype = request.getParameter("agretype");
//            String grpId = request.getParameter("groupId");
            String connId = request.getParameter("connId");
            String tArea = request.getParameter("tArea");
            String tArea1 = request.getParameter("tArea1");
            String bussTabId = request.getParameter("bussTabId");
            tArea = tArea.replace("@", "+");
//            String colFol = column_formula.replace("'", "''");
            String calc = "calculated";
            String[] verify = {"nvl(", "sum(", "avg", "count(", "Count(Distinct"};
            boolean checking;
            for (int i = 0; i < verify.length; i++) {
                checking = column_formula.contains(verify[i]);
                if (checking == true) {
                    calc = "summarized";
                }
            }

            String a = tArea1.trim().substring(1);
            String eleList2[] = a.split("~");

//            String eleList1 = "";
            StringBuilder eleList1 = new StringBuilder(100);
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
//                         eleList1 += "," + eleList2[j];
                        eleList1.append(",").append(eleList2[j]);
                    }
                    if (j == eleList2.length - 2) {
//                         eleList1 += "," + eleList2[j + 1];
                        eleList1.append(",").append(eleList2[j + 1]);
                    }

                }

//                if (!eleList1.equalsIgnoreCase("")) {
//                    eleList1 = eleList1.substring(1);
//                }
                if (eleList1.length() > 0) {
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
            String sql = "select buss_source_id, buss_table_id  from prg_grp_buss_table_src  where buss_table_id in (" + bussTabId + ")";

            PbReturnObject pbroexist = pbdb.execSelectSQL(sql);
            String eleIdsList[] = dependenteleids.split(",");
            String memTabId = "";
            String memColId = "";
            String memId = "";
            String UpdatedFormula = tArea;
            String eleQuery = "";
            String eleMemstr = "";
            String orielestr = "";
//            String str = "";
            String updateddependenteleids = dependenteleids;
            PbReturnObject pbroeleList = null;
            String updaterefids1 = "";
            String updaterefids = "";
//            int elementsCount = 0;
            for (int i = 0; i < eleIdsList.length; i++) {

                if (eleIdsList[i].startsWith("M-")) {

                    String ele[] = eleIdsList[i].substring(2).split("-");
                    memTabId = ele[0];
                    memColId = ele[1];
                    memId = ele[2];
                    eleQuery = "SELECT TAB_ID, dim_tab_id  FROM PRG_GRP_DIM_TABLES where dim_tab_id=" + memTabId;
                    pbroeleList = pbdb.execSelectSQL(eleQuery);
                    orielestr = "~M-" + memTabId + "-" + memColId + "-" + memId;

                    eleMemstr = "<!M-" + pbroeleList.getFieldValueInt(0, 0) + "-" + memColId + "-" + memId + ">";
                    UpdatedFormula = UpdatedFormula.replace(orielestr, eleMemstr);

                    updaterefids = "M-" + pbroeleList.getFieldValueInt(0, 0) + "-" + memColId + "-" + memId;
                    updaterefids1 = "M-" + memTabId + "-" + memColId + "-" + memId;
                    updateddependenteleids = updateddependenteleids.replace(updaterefids1, updaterefids);


                } else {

                    memColId = eleIdsList[i];
                    eleQuery = "SELECT COLUMN_NAME,BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE_DETAILS where BUSS_COLUMN_ID=" + memColId;
                    pbroeleList = pbdb.execSelectSQL(eleQuery);
                    orielestr = "~" + eleIdsList[i];
                    eleMemstr = pbroeleList.getFieldValueString(0, 0);
                    UpdatedFormula = UpdatedFormula.replace(orielestr, eleMemstr);
                    updaterefids = eleIdsList[i];
                    updaterefids1 = pbroeleList.getFieldValueString(0, 1) + "-" + memColId;
                    updateddependenteleids = updateddependenteleids.replace(updaterefids, updaterefids1);
                }
            }


            String colNamenew = column_name.trim();
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
            String finalQuery = "";


            ArrayList list = new ArrayList();
//            int seqaddFormulaBussMater = 0;
//            int seqaddFormulaSrc = 0;

            //add grp buss src details
            int srcdetnextval = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TAB_SRC_DTLS_SEQ.nextval from dual");
            String addFormulasrcDetails = resourceBundle.getString("addFormulasrcDetails");
            Object obj2[] = new Object[7];
            obj2[0] = srcdetnextval;
            obj2[1] = pbroexist.getFieldValueInt(0, 0);
            obj2[2] = "0";
            obj2[3] = pbroexist.getFieldValueInt(0, 1);
            obj2[4] = colNamenew.replaceAll("_", "");
            obj2[5] = calc;// "calculated";
            obj2[6] = "0";

            finalQuery = pbdb.buildQuery(addFormulasrcDetails, obj2);
            list.add(finalQuery);
            String addFormulaBussDetails = resourceBundle.getString("addParamFormulaBussDetails");
            int seqaddFormulaBussDetails = pbdb.getSequenceNumber("select PRG_GRP_BUSS_TABLE_DETAILS_SEQ.nextval from dual");
            Object obj3[] = new Object[15];
            obj3[0] = seqaddFormulaBussDetails;
            obj3[1] = pbroexist.getFieldValueInt(0, 1);
            obj3[2] = colNamenew;
            obj3[3] = "0";
            obj3[4] = srcdetnextval;
            obj3[5] = calc;//"calculated";
            obj3[6] = "0";
            obj3[7] = colNamenew.replaceAll("_", "");
            obj3[8] = "0";
            obj3[9] = "N";
            obj3[10] = UpdatedFormula;
            obj3[11] = agregationtype;
            obj3[12] = colNamenew.replaceAll("_", "");
            obj3[13] = updateddependenteleids;
            obj3[14] = "Y";
            finalQuery = pbdb.buildQuery(addFormulaBussDetails, obj3);
            list.add(finalQuery);
            pbdb.executeMultiple(list);


        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

        return mapping.findForward(SUCCESS);
    }
}
