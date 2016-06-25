/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.user;

//import com.progen.db.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEncrypter;

/**
 *
 * @author Saurabh
 */
public class Login extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        PbReturnObject retObj = null;
        //out.print("Hello");
        try {
            String user = request.getParameter("user");
            String password = request.getParameter("password");

            if (user != null) {
                user = user.toUpperCase();
            }

            if (user != null && password != null) {
                retObj = validateUser(user, password);
            }
            if (user == null || password == null || retObj == null || retObj.getRowCount() == 0) {

                RequestDispatcher rd =
                        getServletContext().getRequestDispatcher("/login.jsp?userId=" + user);
                if (rd != null) {
                    rd.forward(request, response);
                }
            } else {

                if ((retObj.getFieldValueString(0, "ACTIVE")).equals("Y")) {

                    HttpSession userSession = request.getSession(false);
                    userSession.setAttribute("USER_DATA", retObj);

                    user = String.valueOf(retObj.getFieldValueInt(0, 0));
                    response.sendRedirect("home.jsp?userId=" + user);

                    // }
                } else {
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp?userId=" + user + "&active=N");
                    if (rd != null) {
                        rd.forward(request, response);
                    }
                }
            }

        } catch (Exception e) {
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
        return "Login";
    }

    //  without DB testing tester
   /*
     * private String validateUser(String userName, String password) { String
     * user = ""; if (userName.equals("saurabh")) { user = "saurabh"; } return
     * user; }
     */
    private PbReturnObject validateUser(String userName, String password) {
        PbReturnObject retObj = null;
        PbDb db = new PbDb();
        Object[] Obj = new Object[2];


        String finalQuery = "";

        try {
            PbEncrypter enc = new PbEncrypter();
            String pwd = enc.encrypt(password);
            Obj[0] = userName;
            Obj[1] = pwd;

            String query = "SELECT PU_ID ID, PU_LOGIN_ID LOGIN_ID, PU_FIRSTNAME FIRSTNAME, "
                    + "PU_MIDDLENAME MIDDLENAME, PU_LASTNAME LASTNAME, PU_TYPE USERTYPE, "
                    + "PU_ACTIVE ACTIVE FROM  PRG_AR_USERS WHERE PU_LOGIN_ID='&' AND PU_PASSWORD='&'";

            finalQuery = db.buildQuery(query, Obj);

            retObj = db.execSelectSQL(finalQuery);
        } catch (Exception s) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(s);
        }
        return retObj;
    }
    // </editor-fold>
}
