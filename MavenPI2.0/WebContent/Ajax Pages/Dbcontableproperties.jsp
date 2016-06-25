<%-- 
    Document   : Dbcontableproperties
    Created on : Nov 24, 2009, 6:28:57 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ArrayList,com.progen.querylayer.Tablepropertiesbean"%>
<%@page import="prg.db.PbReturnObject"%>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

%>
<html>
    <head>
        <title>Dbcontableproperties Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>


        <style>
            .myHead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#b4d9ee;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
        </style>


    </head>
    <body >
        <%
            ArrayList tabledata = (ArrayList) request.getAttribute("tabledetails");
            int i = 0;

            String tablename = "";
            if (request.getAttribute("tablename") != null) {
                tablename = request.getAttribute("tablename").toString();
            }
        %>
        <!-- <a href="#" onclick="loading()">Click</a>
        -->
        <div id="tabs" class="tabs">
            <ul>
                <li><a href="#tables1" >Tables</a></li>
                <li><a href="#columns1" >Columns</a></li>
            </ul>
            <div id="tables1" style="height:200px;overflow-y:auto">
                <form name="myForm" action="javascript:void(0)">
                    <table border="0" style="border-width:thin;width:100%">
                        <tr>
                            <td width="150px" class="myHead" align="center">
                                Table Name
                            </td>
                            <td class="myHead" align="center">
                                <%=tablename%>
                            </td>
                        </tr>
                    </table>
                    <br/>
                    <table border="1px solid" style="border-width:thin" width="100%">
                        <tr>
                            <td class="myHead" width="100px" align="center">
                                Related Table 1</td>
                        </tr>
                        <tr>
                            <td>

                                <% if (tabledata == null || tabledata.size() == 0) {
                out.println("<font color=black><strong>No Related Tables</strong></font>");
            }
                                %>      </td>
                        </tr>
                        <%if (tabledata != null && tabledata.size() != 0) {
                for (i = 0; i < tabledata.size(); i++) {
                    Tablepropertiesbean tbean = (Tablepropertiesbean) tabledata.get(i);
                        %>
                        <tr>
                            <td height="5px">
                                <%=tbean.getTablename()%>
                            </td>
                        </tr>
                        <%}%>
                    </table>
                    <br/>
                    <table border="0" style="border-width:thin" width="100%">
                        <tr>
                            <td class="myHead" width="100px" align="center">Description</td>
                        </tr>
                        <%
            }
            for (i = 0; i < tabledata.size(); i++) {
                Tablepropertiesbean tbean = (Tablepropertiesbean) tabledata.get(i);
                        %>
                        <tr>
                            <td>
                                <%=tbean.getTabledesc()%>
                            </td>
                        </tr>
                        <%}%>
                    </table>
                    <br/>
                </form>
            </div>
            <%--From Here The Code Is For Showing Columns Of That Related Table--%>
            <%
            if (request.getAttribute("tabledescription") == null) {
                out.println("There Are No Columns");
            }
            if (request.getAttribute("tabledescription") != null) {
                PbReturnObject pbretobj = (PbReturnObject) request.getAttribute("tabledescription");
            %>
            <div id="columns1"  style="height:200px;overflow-y:auto">
                <form name="columnForm" action="javascript:void(0)">
                    <table style="border-width:thin" border="0">
                        <tr>
                            <th class="myHead">Column Name</th>
                            <th class="myHead">Column Type</th>
                            <th class="myHead">Column Length</th>
                        </tr>
                        <%
                for (i = 0; i < pbretobj.getColumnCount(); i++) {
                        %>
                        <tr>
                            <td>
                                &nbsp;<%=pbretobj.getFieldValueString(i, "TABLE_COL_NAME")%>
                            </td>
                            <td>
                                &nbsp;<%=pbretobj.getFieldValueString(i, "COL_TYPE")%>
                            </td>
                            <td>
                                &nbsp; <%=pbretobj.getFieldValueString(i, "COL_LENGTH")%>
                            </td>
                        </tr>
                        <%}
            }%>
                    </table>
                </form>
            </div>
            <%--From Here The Code Is For Showing Columns Of That Related Table--%>
        </div>
        <br/>
        <center>
            <input type="button" class="navtitle-hover" style="width:auto" onclick="closetabproperties()" value="Cancel">
        </center>
    </body>
</html>
