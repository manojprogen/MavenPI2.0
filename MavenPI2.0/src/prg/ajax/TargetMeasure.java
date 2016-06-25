package prg.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class TargetMeasure extends HttpServlet {

    public static Logger logger = Logger.getLogger(TargetMeasure.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con;
        Statement st;
        ResultSet rs;
//        ProgenConnection pg=null;
        String Query = null;

        String businessAreaName = request.getParameter("q");
        ArrayList al = new ArrayList();
        ArrayList tabIds = new ArrayList();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println("businessAreaName is "+businessAreaName);


        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver");
//           pg=new ProgenConnection();
            con = ProgenConnection.getInstance().getConnection();
            st = con.createStatement();
            if (businessAreaName.equalsIgnoreCase("ALL")) {
                Query = "select distinct tm.measure_id,tm.measure_name from target_measure_master tm,prg_business_area ba where tm.business_area "
                        + "in(select pba_area_id from prg_business_area) order by tm.measure_id";
            } else {

                String[] businessAreas = businessAreaName.split(",");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("businessAreas are: "+businessAreas);
                String businessAreasWithQuotes = "";
                for (int i = 0; i < businessAreas.length; i++) {
                    if (i != businessAreas.length - 1) {
                        businessAreasWithQuotes = businessAreasWithQuotes + "'" + businessAreas[i] + "',";
                    } else {
                        businessAreasWithQuotes = businessAreasWithQuotes + "'" + businessAreas[i] + "'";
                    }

                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("businessAreasWithQuotes is: "+businessAreasWithQuotes);
               /*
                 * Query="select distinct tm.measure_id,tm.measure_name from
                 * target_measure_master tm,prg_business_area ba where
                 * tm.business_area " + "in(select pba_area_id from
                 * prg_business_area where pba_area_name
                 * in("+businessAreasWithQuotes+")) order by tm.measure_id";
                 */

                // Query=" select distinct prg_measure_id,measure_name from prg_target_master where business_group " +
                //         "in(select distinct grp_id from prg_grp_master where grp_name in("+businessAreasWithQuotes+")) order by prg_measure_id";
                Query = "select distinct prg_measure_id,measure_name,target_colid,target_table_id from prg_target_master where business_group "
                        + " in(select distinct grp_id from prg_grp_master where grp_id in(Select grp_id from "
                        + " prg_user_folder where folder_name=" + businessAreasWithQuotes + ")) order by prg_measure_id";
                String checkedMeasureForR = "select * from PRG_FOLDER_TARGET_MEASURE where folder_id in (select folder_id from prg_user_folder where folder_name in(" + businessAreasWithQuotes + "))";
                ////////////////////////.println(" checkedMeasureForR "+checkedMeasureForR);
                PbDb pbdb = new PbDb();
                PbReturnObject pbro = pbdb.execSelectSQL(checkedMeasureForR);

                for (int m = 0; m < pbro.getRowCount(); m++) {
                    al.add(pbro.getFieldValueString(m, "MEASURE_ID"));
                    tabIds.add(pbro.getFieldValueString(m, "TABLE_ID"));
                }
            }
            // ////////////////////////.println(" Query./ "+Query);
            // ////////////////////////.println(" tabIds "+tabIds);
            rs = st.executeQuery(Query);

            while (rs.next()) {
                String co = rs.getString(4);
                if (tabIds.contains(co)) {
                    out.println(rs.getString(2) + "," + rs.getString(1));
                }

            }
            rs.close();
            st.close();
            con.close();

        } catch (Exception ex) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("In catch block");
            logger.error("Exception: ", ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
