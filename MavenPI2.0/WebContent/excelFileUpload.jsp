<%-- 
    Document   : excelFileUpload
    Created on : Jan 28, 2014, 10:48:31 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!DOCTYPE html>
 <% 
    String reportId=request.getParameter("reportid");
    String themeColor="blue";
  if(session.getAttribute("theme")==null)
                      session.setAttribute("theme",themeColor);
                  else
                      themeColor=String.valueOf(session.getAttribute("theme"));
            String contextPath=request.getContextPath();
%>
<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
       
        <script type="text/javascript">
         var ctxPath='<%=request.getContextPath()%>';
            function Checkfiles()
            {
                var fup = document.getElementById('filename');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                if( fileName!=null && ext == "xls")
                {
//                    $.ajax({
//                        url: ctxPath+'/fileUploadAction.do?fileUploadParam=excelFileUpload',
//                        success: function(data){
//                            parent.$("#importExcelFileDiv").dialog('close'); 
////                            parent.importExcelFile(fileName);
//                        }
//                    });
//                    parent.$("#importExcelFileDiv").dialog('close');
                    return true;
                }
                else
                {
                    alert("Upload .xls files only");                  
                    return false;
                }
             
            }
           
        </script>
         <style type="text/css">
/*              .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#b4d9ee;
                border:0px;
            }*/
            *{font:10pt verdana;}
        </style>
   <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
          <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>-->
<link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
 <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/pbReportViewerCSS.css" rel="stylesheet" />
      <!--  <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" />-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
    </head>
    <body>
<!--        <table  align="center">
             <h1 align="center">Upload Excel Sheet Data </h1>
        </table>-->
        <div align="center" style=" width: 100%" > 
        <html:form  action="fileUploadAction.do?fileUploadParam=excelFileUpload" method="post" enctype="multipart/form-data">

            <table width="100%" align="center">                
                <tr>
                    <td align="right">File Name</td>
                    <td align="left"><html:file property="filename" styleId="filename" style="background-color:lightgoldenrodyellow; color:black;" size="50" /></td>
                </tr>
                <tr>
                    <td align="center" colspan="2"><html:submit onclick ="return Checkfiles()" styleClass="navtitle-hover"  style="width:auto">Upload File</html:submit>
                    </td>
                </tr>                 
            </table>
                    <input type="hidden" value='<%=reportId%>' name="reportId" id="reportId"/> 
        </html:form>
        </div>
    </body>
</html:html>
