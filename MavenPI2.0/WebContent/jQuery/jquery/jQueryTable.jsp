<%-- 
    Document   : jQueryTable
    Created on : Aug 17, 2009, 2:10:03 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery-latest.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>
        <script>
            $(document).ready(function()
            {
                $("#myTable").tablesorter();
            }
        );
            $(document).ready(function()
            {
                $("#myTable").tablesorter( {sortList: [[0,0], [1,0]]} );
            }
        );

            
        </script>
    </head>
    <body>
        <table id="myTable">
            <thead>
                <tr>
                    <th>Last Name</th>
                    <th>First Name</th>
                    <th>Email</th>
                    <th>Due</th>
                    <th>Web Site</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Smith</td>
                    <td>John</td>
                    <td>jsmith@gmail.com</td>
                    <td>$50.00</td>
                    <td>http://www.jsmith.com</td>
                </tr>
                <tr>
                    <td>Bach</td>
                    <td>Frank</td>
                    <td>fbach@yahoo.com</td>
                    <td>$50.00</td>
                    <td>http://www.frank.com</td>
                </tr>
                <tr>
                    <td>Doe</td>
                    <td>Jason</td>
                    <td>jdoe@hotmail.com</td>
                    <td>$100.00</td>
                    <td>http://www.jdoe.com</td>
                </tr>
                <tr>
                    <td>Conway</td>
                    <td>Tim</td>
                    <td>tconway@earthlink.net</td>
                    <td>$50.00</td>
                    <td>http://www.timconway.com</td>
                </tr>
            </tbody>
        </table>
    </body>
</html>
