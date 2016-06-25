package com.progen.scenario.display;

//import com.progen.report.query.PbTimeRanges;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import com.progen.target.display.DisplayTargetParameters;

public class GetParameters extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String startValue = request.getParameter("startValue");
        String allParamIds = request.getParameter("allParamIds").replaceAll("CBO", "");
        String parArrVals = request.getParameter("parArrVals");
        String scenarioId = request.getParameter("SCENARIOID");

        ////////////////////////////////////////.println.println("scenarioId in GetParameters is:: " + scenarioId);
        ////////////////////////////////////////.println.println("allParamIds are:: "+allParamIds);
        ////////////////////////////////////////.println.println("startValue is:: "+startValue);
        ////////////////////////////////////////.println.println("parArrVals are:: "+parArrVals);



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
