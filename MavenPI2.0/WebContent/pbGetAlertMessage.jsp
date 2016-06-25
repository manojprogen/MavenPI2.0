
<%@page contentType="text/html" pageEncoding="windows-1252" import="java.sql.*,utils.db.ProgenConnection"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
            <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
           *{
               font:11px verdana;
           }
        </style>
    </head>
    <body>
<%
ResultSet rs = null;
try
        {
String alertId = request.getParameter("alertId");
//Class.forName("oracle.jdbc.driver.OracleDriver");
Connection con = ProgenConnection.getInstance().getCustomerConn();
//Connection con = ProgenConnection.getConnection();

Statement st = con.createStatement();
String Query = "select message_status from alert_master where alert_id="+alertId;
rs = st.executeQuery(Query);
rs.next();
//////////////////////////////////////////////////////////////////////////////////////////////////////.println.println(" ----- "+rs.getString(1).substring(6).replace("\\",""));
//out.println(rs.getString(1));

}
catch(Exception e)
        {
           e.printStackTrace();
         }

%>

<%="<br><br><center>"+rs.getString(1).substring(6).replace("\\","")+"</center></br></br>"%>

<br>
        <center>
            <input type="button" class="navtitle-hover" value="close" onclick="window.close()">
        </center>

    </body>
</html>
