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
import prg.business.group.BusinessGroupDAO;
import utils.db.ProgenConnection;

public class CheckFilter extends HttpServlet {

    Connection con, con1;
    Statement st, st1;
    ResultSet rs, rs1;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        int i = 0;
        try {

            String tableId = request.getParameter("tableId");
            String Query = request.getParameter("query");
            String table_name = "";
            con1 = ProgenConnection.getInstance().getConnection();
            st1 = con1.createStatement();
            rs1 = st1.executeQuery("select buss_table_name from prg_grp_buss_table where buss_table_id=" + tableId);
            while (rs1.next()) {
                table_name = rs1.getString(1);
            }
            BusinessGroupDAO bgd = new BusinessGroupDAO();
            con = bgd.getBussTableConnection(tableId);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("con is-->"+con);
            st = con.createStatement();
            String Query1 = "select * from " + table_name + " where " + Query;
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Query is--->"+Query1);
            rs = st.executeQuery(Query1);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("RS is-->"+rs);
            if (con1 != null) {
                con1.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            if (rs == null) {
                i = 1;
            }
        }
        if (i == 1) {
            out.print("Filter you entered is incorrect");
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
