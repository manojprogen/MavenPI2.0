package prg.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import utils.db.ProgenConnection;

public class GetTableNames extends HttpServlet {

    public static Logger logger = Logger.getLogger(GetTableNames.class);
    Connection con = null;
    Connection con1 = null;
    Statement st = null;
    ResultSet rs = null;
    Statement st1 = null;
    ResultSet rs1 = null;
    String connectionName = null;
    String userName = null;
    String password = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            connectionName = request.getParameter("connectionname");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = ProgenConnection.getInstance().getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select user_name,password from prg_user_connections where connection_name='" + connectionName + "'");
            rs.next();
            userName = rs.getString(1);
            password = rs.getString(2);
            con1 = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", userName, password);

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
