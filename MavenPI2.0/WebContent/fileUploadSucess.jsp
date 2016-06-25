<%-- 
    Document   : fileUploadSucess
    Created on : 10 Jan, 2013, 1:07:36 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
String publisingTypedata = "";
String importExcelFrmReport="";
String reportId="";
  if(request.getAttribute("PublishingTypes")!=null){
    publisingTypedata = request.getAttribute("PublishingTypes").toString();
   
 }

 if(request.getAttribute("importExcelFrmReport")!=null){
     importExcelFrmReport=request.getAttribute("importExcelFrmReport").toString();
     reportId=request.getAttribute("reportId").toString();
 }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript">
            function closeUploadFileDiv(){
                parent.$("#uploadExcel").dialog('close');
            }
            function closeUploadFile(){
                parent.$("#importExcelFileDiv").dialog('close');
                alert("File has been Uploaded succesfully to the Report")
                var path='<%=request.getContextPath()%>'+"/reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+'<%=reportId%>';
                parent.submiturls1(path);
            }
        </script>
    </head>
    <body>
        <% if(importExcelFrmReport.equalsIgnoreCase("true")){%>
           <script type="text/javascript">
            this.closeUploadFile();
        </script>
        <%}else{%>
        <%
        if(publisingTypedata.equalsIgnoreCase("")){
        %>
        <table>
            <tr><td><h5>File Uploaded Successfully</h5></td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td align='center'><input type="button" value="Ok" class="navtitle1-hover" onclick="closeUploadFileDiv()"></td></tr>
        </table>
        <%}
        else{
        %>
        <table>
            <tr><td width='50%'><h5>Target Values</h5></td><td width='50%'><h5>Publish Type</h5></td></tr>
            <%=publisingTypedata%>
            <tr><td width="72%"><h5>The above Fields are not changed</h5></td></tr>
            <tr><td align='center'> <input type="button" value="Close" class="navtitle1-hover" onclick="closeUploadFileDiv()"></td></tr>
            <tr><td align='center'> <input type="button" value="Back" class="navtitle1-hover" onclick="BacktoUpload()"></td></tr>
        </table>
         <%}
        %>
        <%}%>
    </body>
</html>
