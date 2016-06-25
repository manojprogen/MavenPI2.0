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
import java.util.ArrayList;
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
public class CheckPublishBusRole extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(CheckPublishBusRole.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection con;
        Statement st;
        ResultSet rs;
        ProgenConnection pg = null;
        ArrayList a = new ArrayList();
        String folderId = request.getParameter("folderId");

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("folderId "+folderId);

        try {
            // pg=new ProgenConnection();
            // con=pg.getConnection();
            //  st=con.createStatement();
            PbDb pbdb = new PbDb();
            // String Query="SELECT GRP_ID, FOLDER_ID, FOLDER_NAME FROM PRG_USER_ALL_INFO_DETAILS where FOLDER_ID="+folderId;
            String Query = "SELECT ispublished FROM PRG_USER_Folder where FOLDER_ID=" + folderId;
            //rs=st.executeQuery(Query);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("query====="+Query);
            PbReturnObject pbro = pbdb.execSelectSQL(Query);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("pbro==========================="+pbro.getRowCount());
            int i = 0;
            if (pbro.getRowCount() > 0) {
                if (pbro.getFieldValueString(0, 0).equalsIgnoreCase("Y")) {
                    i = pbro.getRowCount();
                } else {
                    i = 0;
                }
                out.println(i);
            } else {
                out.println(i);
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
