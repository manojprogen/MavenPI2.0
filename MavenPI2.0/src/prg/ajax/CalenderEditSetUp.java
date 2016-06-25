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
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class CalenderEditSetUp extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(CalenderEditSetUp.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();


        ProgenConnection pg = null;

        String calId = request.getParameter("calId");

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calId "+calId);

        try {
            PbDb pbdb = new PbDb();
            String Query = " SELECT CALENDER_ID, CALENDER_TYPE, CALENDER_NAME, DENOM_TABLE FROM PRG_CALENDER_SETUP where CALENDER_ID=" + calId;

            PbReturnObject pbro = pbdb.execSelectSQL(Query);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("pbro==========================="+pbro.getRowCount()+"=========="+Query);
            String colNames[] = pbro.getColumnNames();
            out.println(pbro.getFieldValueInt(0, colNames[0]) + "," + pbro.getFieldValueString(0, colNames[1]) + "," + pbro.getFieldValueString(0, colNames[2]) + "," + pbro.getFieldValueString(0, colNames[3]));


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
