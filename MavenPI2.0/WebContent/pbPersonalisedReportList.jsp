
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,utils.db.*"%>
<% String contextPath=request.getContextPath(); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery-latest.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=contextPath%>/javascript/jquery.columnfilters.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/dragTable.js"></script>


        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />

       

    </head>
    <body>
        <%
        String userId = String.valueOf(session.getAttribute("USERID"));
            Connection con =  ProgenConnection.getInstance().getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT prg_report_cust_name,prg_report_id,prg_personalized_id FROM prg_ar_personalized_reports WHERE prg_user_id = prg_created_by AND prg_user_id="+userId);
            Statement st1 = con.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT prg_report_cust_name,prg_report_id,prg_personalized_id FROM prg_ar_personalized_reports WHERE prg_user_id <> prg_created_by AND prg_user_id="+userId);


        %>
<form name="myForm1">
    <input type="hidden" name="userId" value="<%=userId%>">
<center><b>My Personalised Reports</b></center>
        <table id="tablesorter" border="0" width="50%" cellspacing="1" class="tablesorter">
                    <thead>
                        <tr>
                            <th></th>

                            <th>Report Name</th>

                            <th>View</th>

                        </tr>
                    </thead>

                    <tbody>
                        <%while(rs.next()) {//////////////////////////////////////////////////////////////////////////////////////////////.println.println("In While Loop");%>
                        <tr>
                            <td><input type="checkbox" name="chk1" value="<%=rs.getString(3)%>"></td>

                            <td NOWRAP ALIGN="left"  class="text_classstyle1"><%=rs.getString(1)%></td>

                            <td><a href="javascript:void(0)"><%="view"+rs.getString(2)%></a></td> 

                        </tr>
                        <%}%>

                    </tbody>

                </table><br></form>
               <center> <input type="button" class="navtitle-hover" value="Delete" onclick="deletePersonalisedReports()">  <input type="button" class="navtitle-hover" value="Cancel" onclick="parent.cancelMessage()"></center>

                <br><br>
                <hr>
                    <br><br>
 <center><b>My Shared Reports</b></center>
 
 <form name="myForm2"><table id="tablesorter" border="0" width="50%" cellspacing="1" class="tablesorter">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Report Name</th>

                            <th>View</th>

                        </tr>
                    </thead>

                    <tbody>
                        <%while(rs1.next()) {%>
                        <tr>
                            <td><input type="checkbox" name="chk1" value="<%=rs1.getString(3)%>"></td>

                            <td NOWRAP ALIGN="left"  class="text_classstyle1"><%=rs1.getString(1)%></td>

                            <td><a href="javascript:void(0)"><%="view"+rs1.getString(2)%></a></td> 

                        </tr>
                        <%}%>

                    </tbody>

                </table><br></form>
               <center> <input type="button" class="navtitle-hover" value="Delete" onclick="deleteSharedReports()">  <input type="button" class="navtitle-hover" value="Cancel" onclick="parent.cancelMessage()"></center>

 <script>
            $(document).ready(function()
            {

                $("#tablesorter").tablesorter();
            }
        );

            $(function() {
                $("table")
                .tablesorter({widthFixed: true, widgets: ['zebra']})
                .tablesorterPager({container: $("#pager")});
            });

            function deletePersonalisedReports()
            {
                document.forms.myForm1.action="deletePersonalisedReports.do";
                document.forms.myForm1.submit();
                parent.cancelMessage();
            }

            function deleteSharedReports()
            {
                document.forms.myForm2.action="deletePersonalisedReports.do";
                document.forms.myForm2.submit();
                parent.cancelMessage();
            }


    </script>
    </body>
</html>
