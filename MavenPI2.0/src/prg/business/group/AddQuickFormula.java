/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

/**
 *
 * @author ramesh
 */
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.db.PbDb;
import prg.db.PbReturnObject;

public class AddQuickFormula extends Action {

    public static Logger logger = Logger.getLogger(AddQuickFormula.class);
//    private ResourceBundle getResourceBundle() {
//        ResourceBundle resourceBundle = null;
//
//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            resourceBundle = new PbBussGrpResBundleSqlServer();
//        } else {
//            resourceBundle = new PbBussGrpResourceBundle();
//        }
//        return resourceBundle;
//    }
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PbReturnObject returnObject = null;
        PbDb pbDb = new PbDb();
        BusinessGroupListDAO bgld = new BusinessGroupListDAO();
        String buss_table_id = null;
        String colId = request.getParameter("colid");
        String temp = request.getParameter("testVar");

        try {
            //save code
            String[] defaultname = request.getParameterValues("defaultAgr");
            String[] sum = request.getParameterValues("sum");
            String[] avg = request.getParameterValues("avg");
            String[] min = request.getParameterValues("min");
            String[] max = request.getParameterValues("max");
            String[] count = request.getParameterValues("count");
            String[] countdistinct = request.getParameterValues("COUNTDISTINCT");
            HashMap formulaAllDetails = new HashMap();
            String calc = null;
            String insertQuery = null;
            if (sum != null) {
                temp = "success";
                for (String sumkey : sum) {
                    String sumStr1 = sumkey.replace("sum", "displayname");
                    String sumStr2 = sumkey.replace("sum", "colsids");
                    String dispName = request.getParameter(sumStr1.trim()).toString();
                    String dispId = request.getParameter(sumStr2.trim()).toString();
                    String sumStr3 = sumkey.replace("sum", "Sum");
                    String sum_str = sumStr3.substring(0, 3);
                    String sum_con = sum_str.concat(" " + dispName);
                    String sum_replace = sum_con.replace(" ", "_");
                    formulaAllDetails.put(dispId, sum_replace);
                    if (sum_str.equals("Sum")) {
                        calc = "calculated";
                    }
                    String columns = bgld.getAllColumns(dispId, calc, sum_str, sum_con);
                }
            }
            if (avg != null) {
                temp = "success";
                for (String avgkey : avg) {
                    String avgstr1 = avgkey.replace("avg", "displayname");
                    String avgstr2 = avgkey.replace("avg", "colsids");
                    String displayname = request.getParameter(avgstr1.trim().toString());
                    String displayId = request.getParameter(avgstr2.trim().toString());
                    String avgstr3 = avgkey.replace("avg", "Avg");
                    String avg_str3 = avgstr3.substring(0, 3);
                    String avg_con = avg_str3.concat(" " + displayname);
                    String avg_replace = avg_con.replace(" ", "_");
                    formulaAllDetails.put(displayname, displayId);
                    if (avg_str3.equals("Avg")) {
                        calc = "calculated";
                    }
                    String columns = bgld.getAllColumns(displayId, calc, avg_str3, avg_con);
                }
            }
            if (min != null) {
                temp = "success";
                for (String minkey : min) {
                    String minstr1 = minkey.replace("min", "displayname");
                    String minstr2 = minkey.replace("min", "colsids");
                    String displayname1 = request.getParameter(minstr1.trim().toString());
                    String displayid1 = request.getParameter(minstr2.trim().toString());
                    String minstr = minkey.replace("min", "Min");
                    String min_str = minstr.substring(0, 3);
                    String min_con = min_str.concat(" " + displayname1);
                    String min_replace = min_con.replace(" ", "_");
                    formulaAllDetails.put(displayname1, displayid1);
                    if (min_str.equals("Min")) {
                        calc = "calculated";
                    }
                    String columns = bgld.getAllColumns(displayid1, calc, min_str, min_con);
                }
            }
            if (max != null) {
                temp = "success";
                for (String maxkey : max) {
                    String maxstr1 = maxkey.replace("max", "displayname");
                    String maxstr2 = maxkey.replace("max", "colsids");
                    String maxdisplayname = request.getParameter(maxstr1.trim().toString());
                    String maxdisplayid = request.getParameter(maxstr2.trim().toString());
                    String maxstr = maxkey.replace("max", "Max");
                    String max_str = maxstr.substring(0, 3);
                    String max_con = max_str.concat(" " + maxdisplayname);
                    String max_replace = max_con.replace(" ", "_");
                    formulaAllDetails.put(maxdisplayname, maxdisplayid);
                    if (max_str.equals("Max")) {
                        calc = "calculated";
                    }
                    String columns = bgld.getAllColumns(maxdisplayid, calc, max_str, max_con);
                }
            }
            if (count != null) {
                temp = "success";
                for (String countkey : count) {
                    String countstr1 = countkey.replace("count", "displayname");
                    String countstr2 = countkey.replace("count", "colsids");
                    String countdisplayname = request.getParameter(countstr1.trim().toString());
                    String countdisplayid = request.getParameter(countstr2.trim().toString());
                    String countstr = countkey.replace("count", "Count");
                    String count_str = countstr.substring(0, 5);
                    String count_con = countdisplayname.concat("(" + count_str + ")");

                    String count_replace = count_con.replace(" ", "_");
                    formulaAllDetails.put(countdisplayname, countdisplayid);
                    if (count_str.equals("Count")) {
                        calc = "calculated";
                    }
                    String columns = bgld.getAllColumns(countdisplayid, calc, count_str, count_con);

                }
            }
            if (countdistinct != null) {
                temp = "success";
                for (String countdistinctkey : countdistinct) {
                    String countdistinctstr1 = countdistinctkey.replace("COUNTDISTINCT", "displayname");
                    String countdistinctstr2 = countdistinctkey.replace("COUNTDISTINCT", "colsids");
                    String countdistinctdisplayname = request.getParameter(countdistinctstr1.trim().toString());
                    String countdistinctid = request.getParameter(countdistinctstr2.trim().toString());
                    String countdistinctstr = countdistinctkey.replace("COUNTDISTINCT", "COUNTDISTINCT");
                    String countdistinct_str = countdistinctstr.substring(0, 13);
                    String countdistinct_con = countdistinctdisplayname.concat("(" + countdistinct_str + ")");
                    String countdistinct_replace = countdistinct_con.replace(" ", "_");
                    formulaAllDetails.put(countdistinctdisplayname, countdistinctid);
                    if (countdistinct_str.equals("COUNTDISTINCT")) {
                        calc = "calculated";
                    }
                    String columns = bgld.getAllColumns(countdistinctid, calc, countdistinct_str, countdistinct_con);
                }
            }
            // edit code
            if (colId != null) {
                int tabId = Integer.parseInt(colId);
                BusinessGroupListDAO listdao = new BusinessGroupListDAO();
                String formula = listdao.getAllFields(tabId);
                response.getWriter().print(formula);
            }
        } catch (NullPointerException npe) {
            logger.error("Exception: ", npe);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        //changed by Nazneen
        if (temp != null) {
           //return mapping.findForward(temp);
return null;
        } else {
            return null;
        }

    }
}
