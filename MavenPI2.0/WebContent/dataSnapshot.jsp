<%-- 
    Document   : dataSnapshot
    Created on : 23 Nov, 2010, 7:58:40 PM
    Author     : arun
--%>

<%@page import="java.io.Reader"%>
<%@page import="java.sql.Clob"%>
<%@page import="com.progen.datasnapshots.DataSnapshot"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbDb" %>
<%@page import="prg.db.PbReturnObject" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%
                String themeColor = "blue";
                String snapShotName = "";
                DataSnapshot snapShot = (DataSnapshot) session.getAttribute("snapShot");
                snapShotName = snapShot.getSnapShotName();

        %>
        <script type="text/javascript">
            function goPaths(path){
                parent.closeStart();
                document.forms.snapShotForm.action=path;
                document.forms.snapShotForm.submit();
            }
            function viewReportG(path){
                document.forms.snapShotForm.action=path;
                document.forms.snapShotForm.submit();
            }
            function viewDashboardG(path){
                document.forms.snapShotForm.action=path;
                document.forms.snapShotForm.submit();
            }
            function gotoHtmlReportHome(){
                 document.forms.snapShotForm.action = "<%=request.getContextPath()%>/home.jsp#Html_Reports";
                document.forms.snapShotForm.submit();
            }
        </script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>piEE</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />

    </head>
    <body>
        <table style="width:100%">
            <tr>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <form name="snapShotForm" method="post" action="">
            <div class="navtitle1" style=" max-width: 100%; cursor: auto; height: 20px;">
                <span> <font size="2">HTML Report - <%=snapShotName%> </font><b> </b></span>
            </div>
            <table align="right"><tr><td>
            <input type="button" value="Back" onclick="javascript:gotoHtmlReportHome()" class="navtitle-hover" style="width:50px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia">
                    </td></tr></table>
            
                    <%
                                Clob htmlTbl = snapShot.getHtmlView();

                                Reader clobReader = htmlTbl.getCharacterStream();
                                char[] cbuf;
                                int toRead = 0;
                                do {
                                    cbuf = new char[5196];
                                    toRead = clobReader.read(cbuf, 0, 5196);
                                    if (toRead == -1) {
                                        break;
                                    }
                                    out.print(cbuf);
                                } while (true);
                                clobReader.close();
                                session.removeAttribute("snapShot");
                    %>

        </form>
    </body>
</html>
