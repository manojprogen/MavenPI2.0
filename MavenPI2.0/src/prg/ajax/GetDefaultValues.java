/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.ajax;

import com.progen.datadisplay.db.PbDataDisplayBeanDb;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class GetDefaultValues extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(GetDefaultValues.class);
    PbDb pbDb = new PbDb();
    Connection con;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ArrayList alist = new ArrayList();
        HashMap hmap = null;

//        Statement st;
        PbReturnObject rsPbReturnObject = new PbReturnObject();
        ProgenConnection pg = null;
        ArrayList a = new ArrayList();
        String tabName = request.getParameter("tabName");
        String pkvaluename = request.getParameter("pkvaluename");
        String tableId = request.getParameter("tableId");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tabName "+tabName+"pkvaluename=="+pkvaluename+"tableId="+tableId);

        try {

            PbDataDisplayBeanDb pg1 = new PbDataDisplayBeanDb();
            con = ProgenConnection.getInstance().getConnectionByTable(tableId);

            // String Query="select m.column_id,m.table_col_name,d.is_pk_key,d.is_available from prg_db_master_table_details m, prg_qry_dim_tab_details d where m.column_id= d.col_id and d.dim_tab_id="+dimtabId+" and d.col_id not in("+key+") and d.is_available='Y' order by d.is_pk_key desc";

            String Query = "select distinct " + pkvaluename + " from " + tabName;

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Query==="+Query);
            rsPbReturnObject = pbDb.execSelectSQL(Query, con);
            con = null;
            for (int i = 0; i < rsPbReturnObject.getRowCount(); i++) {
                out.println(rsPbReturnObject.getFieldValueString(i, 0));
            }

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        } finally {
            out.close();
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exception: ", ex);
                }
            }
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
