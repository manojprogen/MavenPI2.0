<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html"%>
<%@page import="java.awt.Font" %>
<%@page import="utils.db.*" %>
<%@ page language="java" import="java.sql.*, java.lang.*, java.util.*" %>
<%@ page  import="java.awt.*" %>
<%@ page  import="java.io.*" %>
<%@page import="com.progen.charts.*"%>
<%@page import="java.util.HashMap" %>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="com.progen.metadata.*"%>
<%@page import="prg.db.*"%>
<%@page import="prg.util.TableUtils"%>
<%@ page import="java.util.*" %>
<%@page import="com.progen.report.params.PrgReportParams" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

        <SCRIPT LANGUAGE="JavaScript">

            function submittracker()
            {
                alert("reached");
                document.frmtracker.action = "trackerCheck.jsp?"+document.frmtracker.passurl1.value;
                // document.frmalert.target ="_blank";
                document.frmtracker.submit();
            }

        </SCRIPT>
        <%
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PbReturnObject pbretObj = null;
        %>
    </head>
    <body onload="submittracker()">
        <%
        //getting values form dadabase
        //String REPORTID =session.getAttribute("REPORTID").toString();
        String REPORTID ="56";
        String UserId = "41";
        String passurl = "";
        session.setAttribute("REPORTID", REPORTID);
        session.setAttribute("userid", UserId);
        session.setAttribute("oldReportId", REPORTID);

        try {
            //con = DriverManager.getConnection (URL, username, password);
            String sqlstr1 = "select PRIMARY_PARAMETERS from tracker_master where REPORTID="+REPORTID;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "prprogen", "prprogen");
            //con =ProgenConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(sqlstr1);
            pbretObj = new PbReturnObject(rs);
            passurl = pbretObj.getFieldValueString(0, "PRIMARY_PARAMETERS");
            //passurl = rs.getString(0);
            ////////////////////////////////////////////////////////////////////////.println.println("first=="+rs.);
            //////////////////////////////////////////////////////////////////////.println.println("passurl===" + passurl);

            //Building HashMap
           HashMap hm = new HashMap();
            String[] urlValues  = passurl.split("&");
            for(int i=0;i<urlValues.length;i++)
                {
                  //////////////////////////////////////////////////////////////////////.println.println(urlValues[i].split("=")[0]);
                  hm.put(urlValues[i].split("=")[0],urlValues[i].split("=")[1]);
                }
            //////////////////////////////////////////////////////////////////////.println.println("HASHMAP IN RUNNNER IS "+hm);
              //String REPORTID =session.getAttribute("REPORTID").toString();


            ReportData rd = new ReportData(REPORTID);
            rd.ReportType="Progen Standard Report";
            rd.ReportName="Shades Sales Analysis";
            //////////////////////////////////////////////////////////////////////.println.println("rd===" + rd);
            PrgReportParams prp = new PrgReportParams(rd);



            prp.storeParams(hm);

            //////////////////////////////////////////////////////////////////////.println.println("IN JSPPPP-----> "+prp.hm.get("VIEW_BY_NAME")+"-------->"+prp.hmViewBy);
            //////////////////////////////////////////////////////////////////////.println.println("prp==" + prp);
            prp.setUserId(UserId);
            TableData td = new TableData(rd);
            //////////////////////////////////////////////////////////////////////.println.println("td===" + td);
            String TableId = td.getTableIds(REPORTID);
            //////////////////////////////////////////////////////////////////////.println.println("TABLEID ====>"+ TableId);
            String measure=session.getAttribute("measure").toString();
            String sqlstr = td.getTableQueryId(TableId, prp);
            sqlstr= td.setTableData(sqlstr);
            //////////////////////////////////////////////////////////////////////.println.println("sqlstr====" + sqlstr);
            //outer wrapper Query starts
            rs=st.executeQuery("select column_name  from prg_report_table_details where (is_view_by_column='Y' or upper(column_disp_name)=upper('Sales Value')) and table_id="+TableId+" order by is_view_by_column desc ");
            PbReturnObject pbro=new PbReturnObject(rs);
           //////////////////////////////////////////////////////////////////////.println.println("prbo==="+pbro);
            String wrapperQuery="select ";
            String arr[]={"VIEW_BY",measure};
            for(int i=0;i<pbro.getRowCount();i++){
              wrapperQuery+=pbro.getFieldValueString(i,"COLUMN_NAME")+" as \""+arr[i]+"\",";
               }
            wrapperQuery=wrapperQuery.substring(0,(wrapperQuery.length()-1));
            wrapperQuery=wrapperQuery+" from("+sqlstr+")";
            //////////////////////////////////////////////////////////////////////.println.println("main wraper query=="+wrapperQuery);
            out.println("main wraper query=="+wrapperQuery);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }




        %>
        <form name="frmtracker" method=post action="trackerCheck.jsp"><input type="hidden" name="passurl1" value="<%=passurl%>"></form>

        <%-- <%
         ReportData rd = new ReportData(REPORTID);
         PrgReportParams prp = new PrgReportParams(rd);
         prp.setUserId(UserId);
         TableData td= new TableData(rd);
         String TableId =td.getTableIds(REPORTID);
         String sqlstr=td.getTableQueryId(TableId,prp);
         //////////////////////////////////////////////////////////////////////.println.println("sqlstr===="+sqlstr);

        %>
        --%>

    </body>
</html>
