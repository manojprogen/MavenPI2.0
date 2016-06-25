/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.ajax;

import com.progen.report.display.DisplayParameters;
import com.progen.report.query.PbTimeRanges;
import com.progen.scenarion.PbScenarioCollection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.business.group.BusinessGroupDAO;

/**
 *
 * @author Saurabh
 */
public class GetScenarioParameters extends HttpServlet {

    public static Logger logger = Logger.getLogger(GetScenarioParameters.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String scenarioId = "";
        String allParamIds = "";
        String parArrVals = "";
        String startValue = "";
        String elementId = "";
        try {
            scenarioId = request.getParameter("scenarioId");
            allParamIds = request.getParameter("allParamIds");
            parArrVals = request.getParameter("parArrVals");
            startValue = request.getParameter("startValue");
            elementId = request.getParameter("query");

     
            ArrayList elementIds = new ArrayList();
            ArrayList values = new ArrayList();
            LinkedHashMap valuesMap = new LinkedHashMap();

            if (allParamIds != null && parArrVals != null) {
                if (!(allParamIds.equalsIgnoreCase("")) && !(parArrVals.equalsIgnoreCase(""))) {
                    String[] paramId = allParamIds.split(",");
                    String[] paramVal = parArrVals.split(";");
                    for (int y = 0; y < paramId.length; y++) {
                        elementIds.add(paramId[y]);
                    }
                    for (int n = 0; n < paramVal.length; n++) {
                        values.add(paramVal[n]);
                        valuesMap.put(paramId[n].substring(3), paramVal[n]);
                    }
                }
            }
           
            String userId = (String) request.getSession().getAttribute("USERID");

            String endValue = "";
            int exceptionFlag = 0;
            int rowStart = 0;
            if (startValue == null || startValue.equalsIgnoreCase("")) {
                startValue = "1";
                endValue = "20";
            } else {
                rowStart = Integer.parseInt(startValue);
//                startValue = "" + rowStart;
//                endValue = "" + (rowStart - 1 + 20);
                startValue = String.valueOf( rowStart);
                endValue =  String.valueOf( (rowStart - 1 + 20));
            }


            ArrayList a = new ArrayList();
            DisplayParameters dispParam = new DisplayParameters();

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String parentName = "";
                PbTimeRanges pb = new PbTimeRanges();
                con = pb.getConnection(elementId);

                st = con.createStatement();

                //added by susheela start 03-12-09
                String filterClause = "";

                String Query = dispParam.getParameterQuery(elementId);
                PbScenarioCollection psc = new PbScenarioCollection();
                HashMap det = psc.getParentElementId(request.getParameter("query"), scenarioId);
                ArrayList parentId = (ArrayList) det.get("ParentList");
                ArrayList parentBussTab = (ArrayList) det.get("ParentBussTables");
                HashMap ParentBussCol = (HashMap) det.get("ParentBussCol");
                HashMap lMap = new HashMap();
                ArrayList lBusCols = new ArrayList();

                for (int p = 0; p < parentId.size(); p++) {
                    String parId = parentId.get(p).toString();
                    if (valuesMap.containsKey(parId)) {
                        String paramValues = (String) valuesMap.get(parId);
                        if (!paramValues.equalsIgnoreCase("All")) {
                            lMap.put(parId, paramValues);
                            if (ParentBussCol.containsKey(parId)) {
                                String pBussCol = (String) ParentBussCol.get(parId);
                                lBusCols.add(pBussCol);
                            }
                        }
                    }
                }
                String mainCaluse = "";
                mainCaluse = dispParam.getParameterQuery(elementId);
                String whereClause = "";//prc.getWhereClause(lMap, lBusCols);
                String fullQuery = "";
                // fullQuery = mainCaluse+whereClause;

                //modified by susheela start 03-12-09
                fullQuery = mainCaluse;
                //modified by susheela over 03-12-
                BusinessGroupDAO bgDao = new BusinessGroupDAO();
                String q = bgDao.viewBussDataWithouCol(parentBussTab);
                String[] searchWord = request.getParameter("q").split(",");
                String searchString = searchWord[searchWord.length - 1];
                StringBuffer changedQuery = new StringBuffer();
                changedQuery.append("select A1,A2 from (select rownum AS num1,A1,A2 from ( ");
                changedQuery.append(fullQuery);
                changedQuery.append(" ) ");
                if (searchString != null && !(searchString.equalsIgnoreCase("@")) && !searchString.equalsIgnoreCase("All")) {
                    changedQuery.append(" WHERE upper(A1) LIKE upper('" + searchString + "%') ");
                }
                changedQuery.append(") where num1 between " + startValue + " and " + endValue + "");
                rs = st.executeQuery(changedQuery.toString());
                if (startValue.equalsIgnoreCase("1")) {
                    a.add("All");
                }
                while (rs.next()) {
                    a.add(rs.getString(1));
                }

                rs.close();
                st.close();
                con.close();

            } catch (ClassNotFoundException  ex) {
                logger.error("Exception: ", ex);
            }catch (SQLException  ex) {
                logger.error("Exception: ", ex);
            }finally {
                if (exceptionFlag == 0) {
                    String name = request.getParameter("q");
                    if (name.equals("@") != true) {
                        String s[] = new String[a.size()];
                        a.toArray(s);
                        //Comparison logic
                        name = name.substring(name.lastIndexOf(",") + 1);
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
                        ////////////////////////////////////////.println.println("value is:: "+value);
                        out.close();
                    }
                } else {
                    out.print("Search Exception");
                    out.close();
                }
                try {
                    rs.close();
                    st.close();
                    con.close();
                } catch (SQLException e) {
                    logger.error("Exception: ", e);
                }
            }
        } catch (NumberFormatException ex) {
            logger.error("Exception: ", ex);
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
