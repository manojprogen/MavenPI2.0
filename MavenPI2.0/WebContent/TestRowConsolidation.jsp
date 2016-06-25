<%-- 
    Document   : TestRowConsolidation
    Created on : Jan 8, 2010, 12:12:46 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <BODY>
        <TABLE BORDER="1">

            
            <TR>
                <TD  rowspan="4">Cell 1</TD>
                <TD> Cell 2 </TD>
                <TD> Cell 3 </TD>
            </TR> 
            <TR>
               
                <TD rowspan="2"> Cell 5 </TD>
                <TD> Cell 6 </TD>
            </TR>
            <TR>
               
                
                <TD> Cell 8 </TD>
            </TR>
             <TR>
                 
                <TD> Cell 9 </TD>
                <TD> Cell 10 </TD>
            </TR>
        </TABLE>
    </BODY>
</html>
