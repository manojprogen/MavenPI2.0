/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;

/**
 *
 * @author Saurabh
 */
public class saveStandardCalenderCustom extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(saveStandardCalenderCustom.class);
    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            // Class.forName("oracle.jdbc.driver.OracleDriver");
            String connId = request.getParameter("connId");
            Connection con = new BusinessGroupDAO().getConnectionIdConnection(connId);
            ////////////////////////////////////////////////////////////////////////////////////////////.println("conn Object--in create--"+con);
            ////////////////////////////////////////////////////////////////////////////////////////////.println("con---------------------->"+con);
            Statement batchSt = con.createStatement();
            PbDb pbdb = new PbDb();
            TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
            String finalQuery = "";
            String minlevel = request.getParameter("minlevel");
            ////////////////////////////////////////////////////////////////////////////////////////////////.println("minlevel------------->"+minlevel);
            int rowCount = Integer.parseInt(request.getParameter("rowCount"));
            ////////////////////////////////////////////////////////////////////////////////////////////////.println("rowcount---------------->"+rowCount);
            String valCheck[] = request.getParameter("valCheck").split("~");
            String rowids[] = request.getParameter("rowids").split("~------~");
            if (minlevel.equals("1")) {
                String updatePrYear = resBundle.getString("updatePrYear");
                String custname = "";
                //  String rowid="";
                for (int i = 0; i < rowCount; i++) {
                    custname = request.getParameter("YCUST_YEAR" + i);
                    // rowid=request.getParameter("Yrowid"+i);
                    Object obj[] = new Object[2];

                    if (!valCheck[i].equals(custname)) {
                        obj[0] = custname;
                        obj[1] = rowids[i];
                        finalQuery = pbdb.buildQuery(updatePrYear, obj);
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("final query------------>"+i+"----->"+finalQuery);
                        batchSt.addBatch(finalQuery);
                    }
                }
            } else if (minlevel.equals("2")) {
                String updatePrQuarter = resBundle.getString("updatePrQuarter");
                String custname = "";
                //String rowid="";
                for (int i = 0; i < rowCount; i++) {
                    custname = request.getParameter("QCUST_NAME" + i);
                    //  rowid=request.getParameter("Qrowid"+i);
                    Object obj[] = new Object[2];

                    if (!valCheck[i].equals(custname)) {
                        obj[0] = custname;
                        obj[1] = rowids[i];
                        finalQuery = pbdb.buildQuery(updatePrQuarter, obj);
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("final query------------>"+i+"----->"+finalQuery);
                        batchSt.addBatch(finalQuery);
                    }
                }
            } else if (minlevel.equals("3")) {
                String updatePrMonth = resBundle.getString("updatePrMonth");
                String custname = "";
                //String rowid="";
                for (int i = 0; i < rowCount; i++) {
                    custname = request.getParameter("MCUST_NAME" + i);
                    // rowid=request.getParameter("Mrowid"+i);
                    Object obj[] = new Object[2];

                    if (!valCheck[i].equals(custname)) {
                        obj[0] = custname;
                        obj[1] = rowids[i];
                        finalQuery = pbdb.buildQuery(updatePrMonth, obj);
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("final query------------>"+i+"----->"+finalQuery);
                        batchSt.addBatch(finalQuery);
                    }
                }
            } else if (minlevel.equals("4")) {
                String updatePrWeek = resBundle.getString("updatePrWeek");
                String custname = "";
                //String rowid="";
                for (int i = 0; i < rowCount; i++) {
                    custname = request.getParameter("WCUST_NAME" + i);
                    // rowid=request.getParameter("Wrowid"+i);
                    Object obj[] = new Object[2];

                    if (!valCheck[i].equals(custname)) {
                        obj[0] = custname;
                        obj[1] = rowids[i];
                        finalQuery = pbdb.buildQuery(updatePrWeek, obj);
                        ////////////////////////////////////////////////////////////////////////////////////////////////.println("final query------------>"+i+"----->"+finalQuery);
                        batchSt.addBatch(finalQuery);
                    }
                }
            }

            // pbdb.execModifySQL(finalQuery);
            int c[] = batchSt.executeBatch();
            ////////////////////////////////////////////////////////////////////////////////////////////////.println("--------------length=="+c.length);
            batchSt.close();
            con.close();
            request.setAttribute("forward", "success");
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return mapping.findForward(SUCCESS);
    }
}
