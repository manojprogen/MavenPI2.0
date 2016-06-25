/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class BucketDBExistance extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(BucketDBExistance.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        PbDb pbDb = new PbDb();
        Connection con = null;
//        Statement st = null;
//        ResultSet rs = null;
        ArrayList a = new ArrayList();
        String bussTableId = request.getParameter("bussTableId");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------------bussTableId---------"+bussTableId);

        try {
            // pg=new ProgenConnection();
            BusinessGroupDAO pg = new BusinessGroupDAO();
            con = pg.getBussTableConnection(bussTableId);
            // st = con.createStatement();
            String dbType = pg.getBussTableConnectionDbType(bussTableId);
            String Query = "";
//             if ( ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) ){
//                 Query="Select NAME From sysobjects Where name IN ('PRG_GRP_BUCKET_MASTER','PRG_GRP_BUCKET_DETAILS')";
//             }else{
//                Query="select tname from tab where tname in ('PRG_GRP_BUCKET_MASTER','PRG_GRP_BUCKET_DETAILS')";
//             }

            if (dbType.equalsIgnoreCase("ORACLE")) {
                Query = "select tname from tab where tname in ('PRG_GRP_BUCKET_MASTER','PRG_GRP_BUCKET_DETAILS')";
            } else if (dbType.equalsIgnoreCase("SqlServer")) {
                Query = "select name from sys.Tables where name in('PRG_GRP_BUCKET_MASTER','PRG_GRP_BUCKET_DETAILS')";
            } else if (dbType.equalsIgnoreCase("PostgreSQL") || dbType.equalsIgnoreCase("mysql")) {
                Query = "SELECT DISTINCT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE upper (TABLE_NAME ) IN  ('PRG_GRP_BUCKET_MASTER','PRG_GRP_BUCKET_DETAILS')";
            }


            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("query====="+Query);
            PbReturnObject pbro = pbDb.execSelectSQL(Query, con);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("pbro==========================="+pbro.getRowCount());
            int i = 0;
            if (pbro.getRowCount() > 0) {
                i = pbro.getRowCount();
                out.println(i);
            } else {
                out.println(i);
            }

            con = null;

        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        } finally {
            out.close();
            try {

                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }

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
