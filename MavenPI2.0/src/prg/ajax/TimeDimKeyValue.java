/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
public class TimeDimKeyValue extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(TimeDimKeyValue.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con;
        ProgenConnection pg = null;
        PbDb pbdb = new PbDb();
        String bussTableId = request.getParameter("bussTableId");
        String minTimeLevel = request.getParameter("minTimeLevel");
        String Query = "";
        try {
            if (minTimeLevel.equals("1")) {
                Query = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS where upper(column_type)='DATE' and  BUSS_TABLE_ID=" + bussTableId;
            } else {
                Query = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS where BUSS_TABLE_ID=" + bussTableId;
            }
            PbReturnObject pbro = pbdb.execSelectSQL(Query);
            for (int i = 0; i < pbro.getRowCount(); i++) {
                out.println(pbro.getFieldValueInt(i, 0) + "," + pbro.getFieldValueString(i, 1));
            }

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
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
