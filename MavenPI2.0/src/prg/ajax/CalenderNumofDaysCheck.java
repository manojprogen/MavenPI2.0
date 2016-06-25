/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class CalenderNumofDaysCheck extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(CalenderNumofDaysCheck.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String strdate = request.getParameter("strdate");
        String enddate = request.getParameter("enddate");
        String Query = "";
        PbDb pbdb = new PbDb();
        try {
            Query = "SELECT TO_DATE('" + enddate + "','mm-dd-yyyy')- TO_DATE('" + strdate + "','mm-dd-yyyy'),sysdate-TO_DATE('" + strdate + "','mm-dd-yyyy') FROM dual";
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("query==="+Query);
            PbReturnObject pbro = pbdb.execSelectSQL(Query);

            out.println(pbro.getFieldValueInt(0, 0) + "," + pbro.getFieldValue(0, 1));


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
