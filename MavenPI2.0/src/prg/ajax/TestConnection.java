package prg.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Saurabh
 */
public class TestConnection extends HttpServlet {

    public static Logger logger = Logger.getLogger(TestConnection.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con = null;
        String status = "";
        int i = 0;
        try {

            String username = request.getParameter("un");
            String password = request.getParameter("pwd");
            String server = request.getParameter("s");
            String sid = request.getParameter("sid");
            String port = request.getParameter("p");
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(username+password+server+sid+port);
            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection("jdbc:oracle:thin:@" + server + ":" + port + ":" + sid + "", username, password);




        } catch (Exception e) {
            i = 1;
            logger.error("Exception: ", e);
        }






        if (i == 0) {
            status = "Connection Successful";
        } else {
            status = "Connection Failed";
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("status is "+status);
        out.println(status);
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
