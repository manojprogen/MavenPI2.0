/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class DimensionDeleteColumns extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public static Logger logger = Logger.getLogger(DimensionDeleteColumns.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String collen[] = request.getParameterValues("chk2");
//String collen[]=request.getParameterValues("chk3");


        try {
            String colNames[] = request.getParameterValues("chk2");
            if (colNames != null) {
                String columnIds = "";
                String tabId = request.getParameter("dimtableId");
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tabb id in delete"+tabId);
                String tabids[] = tabId.split(",");
                tabId = tabids[0];
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("dim tabid =="+tabId);

                String dimtableId = "";
                for (int i = 0; i < colNames.length; i++) {
                    String vals[] = colNames[i].split(",");

                    dimtableId = vals[1];
                    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" tableid=="+tabId+"dim tableid=="+dimtableId+"columnId=="+vals[0]);
                    if (tabId.equals(dimtableId)) {
                        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in if tableid=="+tabId+"dim tableid=="+dimtableId+"columnId=="+vals[0]);
                        columnIds += "," + vals[0];
                    }


                }
                columnIds = columnIds.substring(1);
                Connection con = null;
                Statement st, st1 = null;
                ResultSet rs = null;
//Class.forName("oracle.jdbc.driver.OracleDriver");
                con = ProgenConnection.getInstance().getConnection();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("connection=="+con);
                st = con.createStatement();
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("statement ="+st);
//String sql="update prg_db_master_table_details set is_active='N' where column_id  not in ("+columnIds+") and table_id="+tabId;
                String sql = "update prg_qry_dim_tab_details  set is_available='N' where col_id  not in (" + columnIds + ") and dim_tab_id=" + tabId;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sql fror source id=="+sql);
                st.executeUpdate(sql);
                st1 = con.createStatement();
                String sql1 = "update prg_qry_dim_tab_details  set is_available='Y' where col_id  in (" + columnIds + ") and dim_tab_id=" + tabId;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("sql fror source id=1="+sql1);
                st.executeUpdate(sql1);
                st1.close();
                con.close();
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
