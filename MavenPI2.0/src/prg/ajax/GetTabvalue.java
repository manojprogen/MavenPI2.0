/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class GetTabvalue extends HttpServlet {

    public static Logger logger = Logger.getLogger(GetTabvalue.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con;
        PreparedStatement st;
        ResultSet rs;
        String tablename = request.getParameter("tableName");
//        PbDb pbdb = new PbDb();
        try {

            con = ProgenConnection.getInstance().getConnection();
            String Query = "select column_name from user_tab_cols where table_name=upper('?') order by column_name";
            st = con.prepareStatement(Query);
            st.setString(1, tablename);
            //"select DISTINCT " + selectedvalue + " FROM PRG_KNP_COMP_DIM1 order by "+selectedvalue;
// "select mon_name from prg_acn_mon_denom where cm_st_date > (select cm_st_date from prg_acn_mon_denom where mon_name='" + histEndMonth + "')";
            rs = (ResultSet) st.executeQuery(Query);
            while (rs.next()) {
                out.println(rs.getString(1));
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
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
