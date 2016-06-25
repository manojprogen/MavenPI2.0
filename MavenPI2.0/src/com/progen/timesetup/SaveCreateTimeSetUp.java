/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.timesetup;

import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import prg.business.group.BusinessGroupDAO;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Saurabh
 */
public class SaveCreateTimeSetUp extends org.apache.struts.action.Action {

    /*
     * forward name="success" path=""
     */
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        PbDb pbdb = new PbDb();
        TimeSetUpResourceBundle resBundle = new TimeSetUpResourceBundle();
        String finalQuery = "";
        ////////////////////////////////////////////////////////////////////////////////////.println("----------------------request.getParameter(custom)------------------"+request.getParameter("custom"));
        String calName = request.getParameter("ccalName");
        String calType = request.getParameter("ccalType");
        String isCustom = request.getParameter("custom");
        String connId = request.getParameter("connId");
        ////////////////////////////////////////////////////////////////////////////////.println("----------------------request.getParameter(custom)------------------"+request.getParameter("custom")+"--com-Id-"+connId);
        String strYear = "";
        String endYear = "";
        if (isCustom != null) {
            strYear = request.getParameter("strYear");
            endYear = request.getParameter("endYear");
        }

        String denomtabs = "PR_DAY_DENOM";
        // String denomtabs=request.getParameter("denomtabs");
        String addCalender = resBundle.getString("addCalender");
        int seqaddCalender = pbdb.getSequenceNumber("select PRG_CALENDER_SETUP_SEQ.nextval from dual");
        Object obj[] = new Object[7];
        if (isCustom != null) {

            obj[0] = seqaddCalender;
            obj[1] = calType;
            obj[2] = calName;
            obj[3] = denomtabs + "_" + seqaddCalender;
            obj[4] = strYear;
            obj[5] = endYear;
            obj[6] = connId;
        } else {

            obj[0] = seqaddCalender;
            obj[1] = calType;
            obj[2] = calName;
            obj[3] = denomtabs + "_" + seqaddCalender;
            obj[4] = "";
            obj[5] = "";
            obj[6] = connId;
        }
        finalQuery = pbdb.buildQuery(addCalender, obj);
        ////////////////////////////////////////////////////////////////////////////////.println("final-------------"+finalQuery);
        pbdb.execModifySQL(finalQuery);


        //This code is for Creating new Denom tables----------Start----------
        if (isCustom != null) {
            String createDenomTables = resBundle.getString("createDenomTables");
            Object obj1[] = new Object[2];
            obj1[0] = "PR_YEAR_" + seqaddCalender;
            obj1[1] = "PYEAR NUMBER, 	START_DATE DATE, 	END_DATE DATE, 	CUST_YEAR VARCHAR2(255 BYTE)";
            finalQuery = pbdb.buildQuery(createDenomTables, obj1);
            ////////////////////////////////////////////////////////////////////////////////////.println("final-------createDenomTables------"+finalQuery);
            Statement st, cst, st1 = null;
            //ResultSet rs = null;
            String sql = "";
            Connection con = null;
            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = new BusinessGroupDAO().getConnectionIdConnection(connId);
            ////////////////////////////////////////////////////////////////////////////////.println("conn Object--in create--"+con);
            // ////////////////////////////////////////////////////////////////////////////////.println("con-----------------" + con);
            st1 = con.createStatement();
            String createPrDenomTable = "create table " + denomtabs + "_" + seqaddCalender + " as select * from PR_DAY_DENOM ";
            st1.executeUpdate(createPrDenomTable);
            cst = con.createStatement();
            cst.executeUpdate(finalQuery);
            st = con.createStatement();

            sql = "SELECT PYEAR, START_DATE, END_DATE, CUST_YEAR FROM PR_YEAR order by START_DATE desc";
            PbReturnObject pbro = new PbReturnObject(st.executeQuery(sql));
            // pbro.writeString();
            for (int i = 0; i < pbro.getRowCount(); i++) {
                String startyear = pbro.getFieldValueDate(i, 1).toString();
                ////////////////////////////////////////////////////////////////////////////////////.println("start year--"+startyear);
                String arr[] = startyear.split("-");
                String year = arr[0];
                String month = arr[1];
                String date = arr[2];
                ////////////////////////////////////////////////////////////////////////////////////.println("str year-------"+strYear);
                String orgarr[] = strYear.split("/");
                String orgyear = orgarr[2];
                String orgmonth = orgarr[0];
                String orgdate = orgarr[1];
                ////////////////////////////////////////////////////////////////////////////////////.println("year---"+orgyear+"--"+orgmonth+"---"+orgdate);

                String insertyear = year;
                String insertstartdate = orgmonth + "/" + date + "/" + year;
                //for adding days to end year
     /*
                 * String adddaysend=""; if(Integer.parseInt(year)%4==0){
                 * ////////////////////////////////////////////////////////////////////////////////////.println("leap---");
                 * if(Integer.parseInt(month)>2){
                 * ////////////////////////////////////////////////////////////////////////////////////.println("month
                 * after 365 leap month"); adddaysend="363"; }else{
                 * if(Integer.parseInt(date)==29){
                 * ////////////////////////////////////////////////////////////////////////////////////.println("29---365----");
                 * adddaysend="363"; }else{
                 * ////////////////////////////////////////////////////////////////////////////////////.println("not
                 * 29-----366-----"); adddaysend="365"; } } }else
                 * if((Integer.parseInt(year)+1)%4==0){
                 * ////////////////////////////////////////////////////////////////////////////////////.println("end
                 * leap---");
                 *
                 * if(Integer.parseInt(month)+12>14){
                 * ////////////////////////////////////////////////////////////////////////////////////.println("366---");
                 * adddaysend="366"; } else if(Integer.parseInt(month)+12==14){
                 * if(Integer.parseInt(date)<28){
                 * ////////////////////////////////////////////////////////////////////////////////////.println("end--365----");
                 * adddaysend="365"; }else{
                 * ////////////////////////////////////////////////////////////////////////////////////.println("not
                 * 29---366-------"); adddaysend="366"; }
                 *
                 * }else { adddaysend="364"; }
                 *
                 *
                 * }else{
                 *
                 * adddaysend="364";
       }
                 */
//ends code for adding days to end year
                String insertcustname = year + "-" + (Integer.parseInt(year) + 1);
                //////////////////////////////////////////////////////////////////////////////////////.println("---------------"+i+"---------"+insertyear+"-----"+insertstartdate+"---------"+adddaysend+"-----------"+insertcustname);
                st.executeUpdate("INSERT INTO  PR_YEAR_" + seqaddCalender + "(PYEAR, START_DATE, END_DATE, CUST_YEAR) values(" + insertyear + "," + "TO_DATE('" + insertstartdate + "','mm-dd-yyyy'),add_months(TO_DATE('" + insertstartdate + "','mm-dd-yyyy'), 12)-1,'" + insertcustname + "')");

                // ////////////////////////////////////////////////////////////////////////////////////.println("INSERT INTO  PR_YEAR(PYEAR, START_DATE, END_DATE, CUST_YEAR) values("+insertyear+","+"TO_DATE('"+insertstartdate+"','mm-dd-yyyy'),TO_DATE('"+insertstartdate+"','mm-dd-yyyy')+"+adddaysend+",'"+insertcustname+"')");
                // Ends here -------------------
            }
            st.close();
            cst.close();
            con.close();
        }

        return mapping.findForward(SUCCESS);
    }
}
