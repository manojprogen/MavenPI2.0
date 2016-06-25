<%--
    Document   : Uploadfile
    Created on : Jan 28, 2010, 9:30:19 PM
    Author     : Saurabh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%
String contextPath=request.getContextPath();
%>


<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        
        <title>Upload File</title>

        <html:base/>
        
        <style type="text/css">
            *{font:11px verdana;}
        </style>
    </head>
    <body>
           <html:form  action="fileUploadAction.do?fileUploadParam=onSuccess" method="post" enctype="multipart/form-data">

            <table width="" align="center">
                <tr style="">
                    <td align="left" colspan="2"><font color="red"><html:errors/></font>
                </tr>
                <tr style="">
                    <td align="right" colspan="1">File Name</td>
                    <td align="left" colspan="1" ><html:file property="filename" styleId="filename" /></td>
                </tr>
                <tr style="">
                    <td align="center" colspan="2"><html:submit onclick ="return Checkfiles()" styleClass="navtitle-hover" >Upload File</html:submit>
                    </td>
                </tr>
            </table>

        </html:form>
<script type="text/javascript">
           
            function Checkfiles()
            {
                var fup = document.getElementById('filename');
                var fileName = fup.value;
                var ctxPath = '<%=request.getContextPath()%>';
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                if(fileName!=null && ext == "xls" )
                {
                    $.ajax({
                        url: ctxPath+'fileUploadAction.do?fileUploadParam=fileUpload',
                        success: function(data){
                            parent.importExcelFile(fileName);
                        }
                    });
                    /*document.forms.FileUploadFormBean.action="<%=request.getContextPath()%>/fileUploadAction.do?fileUploadParam=fileUpload";
                    //document.forms.FileUploadFormBean.submit();
                  */
                  return true;
                }
                else
                {
                   alert("Please Upload .xls files only");
                   return false;
                }
            }
     </script>
    </body>
</html:html>
