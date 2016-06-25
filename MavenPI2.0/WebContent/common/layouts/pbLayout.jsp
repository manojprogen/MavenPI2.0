<%-- 
    Document   : pbLayout
    Created on : Sep 12, 2009, 3:50:00 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><tiles:getAsString name="title" ignore="true"/></title>
    </head>
    <body>
        <table>
            <table border="0" cellpadding="0" cellspacing="0" width="100%"   bgcolor="#E7FDFE">
                <tr>
                    <td width="100%" colspan="2" valign="top"><tiles:insert attribute="header"/></td>
                </tr>
                <tr>
                    <td width="23%" valign="top"><tiles:insert attribute="menu"/></td>
                    <td width="77%" valign="top" valign="top"><tiles:insert attribute="body"/></td>
                </tr>
                <tr>
                    <td width="100%" colspan="2" valign="top"><tiles:insert attribute="footer"/></td>
                </tr>
            </table>
        </table>
    </body>
</html>
