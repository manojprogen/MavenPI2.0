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
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class DuplicateGroup extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(DuplicateGroup.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        PbReturnObject pbro = null;
        Connection con = null;
        PbDb pbdb = new PbDb();
        ////.println("    in  processRequest");
        try {
            String groupName = request.getParameter("grpName").replace(" ", "_");
            String connectionId = request.getParameter("connectionId");
            String isNewTable = request.getParameter("isNewTable");
            String tableName = request.getParameter("tableName").toUpperCase();
            groupName = groupName.toUpperCase();
            con = new BusinessGroupDAO().getConnectionIdConnection(connectionId);
            String Query = "";
            if (isNewTable.equalsIgnoreCase("Y")) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    Query = "select name from sys.tables where  name='PR_" + groupName + "'";
                } else {
                    Query = "select tname from tab where  tname='PR_" + groupName + "'";
                }

                ////.println("Query\t"+Query);
            } else {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    Query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS  WHERE TABLE_NAME ='" + tableName + "' and COLUMN_NAME='" + groupName + "'";
                    ////.println("Query\t"+Query);
                } else {
                    Query = "SELECT column_name from all_tab_cols where table_name='" + tableName + "' and column_name='" + groupName + "'";
                    ////.println("Query\t"+Query);

                }


            }
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(Query);
            // //////////////////////////////////////////////////////////////////////////////////////////////.println.println("Query---"+Query);
            pbro = new PbReturnObject(rs);
            pbro.writeString();
            int i = 0;
            if (pbro.getRowCount() > 0) {
                i = pbro.getRowCount();
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
