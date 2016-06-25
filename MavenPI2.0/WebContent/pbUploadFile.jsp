<%-- 
    Document   : pbUploadFile
    Created on : Oct 12, 2009, 8:31:24 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html:html>
    <head>
        <title>Struts File Upload Example</title>
        <html:base/>
    </head>
    <body bgcolor="white">
        <html:form action="/uploadFileAction" method="post" enctype="multipart/form-data">
            <table>
                <tr>
                    <td align="center" colspan="2">
                    <font size="4">Please Enter the Following Details</font>
                </tr>
                <tr>
                    <td align="left" colspan="2">
                    <font color="red"><html:errors/></font>
                </tr>
                <tr>
                    <td align="right">
                        File Name
                    </td>
                    <td align="left">
                        <html:file property="theFile"/>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <html:submit>Upload File</html:submit>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>
