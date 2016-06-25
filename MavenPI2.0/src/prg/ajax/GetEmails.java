/*
 * getEmails.java
 *
 * Created on June 8, 2009, 5:35 PM
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
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 * @version
 */
public class GetEmails extends HttpServlet {

    public static Logger logger = Logger.getLogger(GetEmails.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con;
        Statement st;
        ResultSet rs;
        ArrayList a = new ArrayList();
        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            con = ProgenConnection.getInstance().getConnection();
            st = con.createStatement();
            String Query = "select PU_EMAIL from PRG_AR_USERS";
            rs = st.executeQuery(Query);
            while (rs.next()) {
                a.add(rs.getString(1));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        String name = request.getParameter("q");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Parameter is "+name);



        if (name.equals("@") != true) {
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("size of arraylist is "+a.size());
            String s[] = new String[a.size()];
            a.toArray(s);

            //Comparison logic

            name = name.substring(name.lastIndexOf(",") + 1);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Parameter is "+name);
            String value = "";
            if (name.equals("@") != true) {
                for (int i = 0; i < s.length; i++) {
                    if (name.isEmpty()) {
                        break;
                    }
                    if ((s[i].toUpperCase()).startsWith((name.toUpperCase()))) {
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
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("values are "+value);
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
     */
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
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
