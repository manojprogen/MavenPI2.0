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
import prg.business.group.BusinessGroupDAO;

/**
 *
 * @author Saurabh
 */
public class CheckQuery extends HttpServlet {
//    Connection con;
//    Statement st,st1;

    ResultSet rs;
    public static Logger logger = Logger.getLogger(CheckQuery.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int i = 0;
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String tableId = request.getParameter("tableId");
            String Query = request.getParameter("query");
            String groupId = request.getParameter("groupId");
            //Query = "select "+Query+" from prg_grp_buss_table_details";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //Connection con = new utils.db.ProgenConnection().getConnection();//Add IMPORT//
            BusinessGroupDAO bd = new BusinessGroupDAO();
            String businessQuery = bd.viewBussData(tableId, groupId, 1);
            String checkQuery = "select " + Query + " from (" + businessQuery + ")";
            Connection con2 = bd.getBussTableConnection(tableId);
            Statement st2 = con2.createStatement();
            rs = st2.executeQuery(checkQuery);
            if (con2 != null) {
                con2.close();
            }
        } catch (Exception e) {
            if (rs == null) {
                i = 1;
            }

            logger.error("Exception: ", e);
        }
        if (i == 1) {
            out.print("Formula you entered is incorrect");
        } else {
            out.print("Correct");
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
