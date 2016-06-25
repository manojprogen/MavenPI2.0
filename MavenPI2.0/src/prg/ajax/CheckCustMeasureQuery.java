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
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class CheckCustMeasureQuery extends HttpServlet {

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
    public static Logger logger = Logger.getLogger(CheckCustMeasureQuery.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ResultSet rs = null;
        PbDb pbdb = new PbDb();
        int i = 0;
        try {

            String Query = request.getParameter("query");
            Query = Query.replace("@", "+");
            String ConnectionId = request.getParameter("ConnectionId");
            String tArea1 = request.getParameter("tArea1");
            String tArea = request.getParameter("tArea");
            tArea = tArea.replace("@", "+");
            //////////////////////////////////////////////////////////////////////////////////////////.println.println(request.getParameter("query"));
            // ////////////////////////////////////////////////////////////////////////////////////////.println.println(request.getParameter("tArea1"));
            // ////////////////////////////////////////////////////////////////////////////////////////.println.println(request.getParameter("tArea"));
            if (tArea1.length() > 0) {
                String a = tArea1.trim().substring(1);
                // ////////////////////////////////////////////////////////////////////////////////////////.println.println("a====" + a);
                String eleList2[] = a.split("~");
                String eleList1 = "";
                if (eleList2.length > 1) {
                    for (int j = 0; j < eleList2.length - 1; j++) {
                        int count = 0;
                        for (int j1 = j + 1; j1 < eleList2.length; j1++) {
                            //
                            if (eleList2[j].equalsIgnoreCase(eleList2[j1])) {
                                ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("equal");
                                count = 1;
                                break;
                            }
                        }
                        if (count == 0) {

                            eleList1 += "," + eleList2[j];
                        }
                        if (j == eleList2.length - 2) {

                            eleList1 += "," + eleList2[j + 1];
                        }

                    }

                    if (!eleList1.equalsIgnoreCase("")) {
                        eleList1 = eleList1.substring(1);
                    }
                } else {

                    eleList1 = eleList2[0];
                }
                //////////////////////////////////////////////////////////////////////////////////////.println.println("eleList1--" + eleList1);
                String eleList3[] = eleList1.split(",");

                String dependenteleids = "";
                for (int p = 0; p < eleList3.length; p++) {
                    boolean check = tArea.contains(eleList3[p]);
                    //  ////////////////////////////////////////////////////////////////////////////////////////.println.println("check==" + check);
                    if (check == true) {
                        // ////////////////////////////////////////////////////////////////////////////////////////.println.println("in if");
                        dependenteleids += "," + eleList3[p];
                    }
                    if (p == eleList3.length - 1) {

                        dependenteleids = dependenteleids.substring(1);
                    }
                }


                // ////////////////////////////////////////////////////////////////////////////////////////.println.println("dependenteleids===" + dependenteleids);
                String tableList = "";
                String dependenteleidsList[] = dependenteleids.split(",");
                for (int n = 0; n < dependenteleidsList.length; n++) {
                    String nameQuery = "select distinct DISP_NAME,BUSS_COL_NAME from PRG_USER_ALL_INFO_DETAILS where element_id in(" + dependenteleidsList[n] + ")";
                    // ////////////////////////////////////////////////////////////////////////////////////////.println.println("nameQuery--" + nameQuery);
                    PbReturnObject pbro = pbdb.execSelectSQL(nameQuery);
                    tableList += "," + pbro.getFieldValueString(0, 0);
                    String querypart = pbro.getFieldValueString(0, 0) + "." + pbro.getFieldValueString(0, 1);
                    String val = "~" + dependenteleidsList[n];

                    tArea = tArea.replaceAll(val, querypart);
                    //////////////////////////////////////////////////////////////////////////////////////////.println.println("querypart==" + querypart+"val=="+val);
                    // ////////////////////////////////////////////////////////////////////////////////////////.println.println("tArea===" + tArea);
                    if (n == dependenteleidsList.length - 1) {
                        tableList = tableList.substring(1);
                    }
                }
                String tList = "";
                if (tableList.equalsIgnoreCase("")) {
                    //  ////////////////////////////////////////////////////////////////////////////////////////.println.println("in if");
                } else {
                    // ////////////////////////////////////////////////////////////////////////////////////////.println.println("in else");
                    String tabList[] = tableList.split(",");
                    if (tabList.length > 1) {
                        for (int m = 0; m < tabList.length - 1; m++) {
                            int c = 0;
                            for (int m1 = m + 1; m1 < tabList.length; m1++) {
                                if (tabList[m].equalsIgnoreCase(tabList[m1])) {
                                    ////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("equal");
                                    c = 1;
                                    break;
                                }

                            }

                            if (c == 0) {

                                tList += "," + tabList[m];
                            }
                            if (m == tabList.length - 2) {

                                tList += "," + tabList[m + 1];
                            }
                        }

                        if (!tList.equalsIgnoreCase("")) {
                            tList = tList.substring(1);
                        }
                    } else {
                        tList = tabList[0];
                    }

                }
                String finalQuery = "select  ?  from  ?";
                BusinessGroupDAO bd = new BusinessGroupDAO();
                Connection con = bd.getConnectionIdConnection(ConnectionId);
                PreparedStatement st = con.prepareStatement(finalQuery);
                st.setString(1, tArea);
                st.setString(2, tList);
                rs = st.executeQuery(finalQuery);
                 if (st != null) {
              st.close();
            }
            }
           

        } catch (SQLException e) {
            if (rs == null) {
                i = 1;
            }

            logger.error("Exception: ", e);
        }
         if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.error("Exception", ex);
                }
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
