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
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class RepCepViewVals extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(RepCepViewVals.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        ArrayList a = new ArrayList();
        PbDb pbdb = new PbDb();
        try {
            String eleId = request.getParameter("eleId");
            if (eleId.equalsIgnoreCase("Time")) {


                a.add("All");

            } else {
                eleId = eleId.trim().replaceAll("A_", "");
                eleId = eleId.trim().replaceAll("_G", "");
                eleId = eleId.trim().replaceAll("_B", "");
                String sql = "SELECT  BUSS_COL_NAME,BUSS_TABLE_NAME, CONNECTION_ID FROM PRG_USER_ALL_INFO_DETAILS where element_id=" + eleId;
                PbReturnObject pbro = pbdb.execSelectSQL(sql);

                String connectionId = String.valueOf(pbro.getFieldValueInt(0, 2));
                if (connectionId != null) {

                    String tableName = pbro.getFieldValueString(0, 1);
                    String colName = pbro.getFieldValueString(0, 0);
                    //////////////////////////////////////////////////////////////////////////////////////////.println.println("colName" + colName);
                    String parentGrpValQuery = "select distinct " + colName + "  from " + tableName;
                    //////////////////////////////////////////////////////////////////////////////////////////.println.println("parentGrpValQuery===" + parentGrpValQuery);
                    Connection con = new BusinessGroupDAO().getConnectionIdConnection(connectionId);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(parentGrpValQuery);
                    PbReturnObject memdetPbro = new PbReturnObject(rs);
                    a.add("All");

                    for (int i = 0; i < memdetPbro.getRowCount(); i++) {
                        a.add(memdetPbro.getFieldValueString(i, 0));

                    }

                    rs.close();
                    st.close();
                    con.close();
                }


            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        String name = request.getParameter("q");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Parameter is "+name);



        if (name.equals("@") != true) {
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("size of arraylist is "+a.size());
            String s[] = new String[a.size()];
            a.toArray(s);

            //Comparison logic

            name = name.substring(name.lastIndexOf(",") + 1);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Parameter is "+name);
            String value = "";
            if (name.equals("@") != true) {
                for (int i = 0; i < s.length; i++) {
                    if (name.isEmpty()) {
                        break;
                    }
                    if ((s[i].toUpperCase()).startsWith((name.toUpperCase()))) {    //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in if");
                        value = value + s[i] + "\n";

                    }

                }

            } else {
                String s12[] = new String[a.size()];
                a.toArray(s12);

                for (int i = 0; i < s.length; i++) {
                    value = value + s[i] + "\n";

                }

            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("values are "+value);
            out.print(value);



            out.close();

        } else {
            String s[] = new String[a.size()];
            a.toArray(s);
            String value = "";
            for (int i = 0; i < s.length; i++) {
                value = value + s[i] + "\n";

            }

            out.print(value);

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
