<%-- 
    Document   : uplaodExcel
    Created on : Dec 31, 2009, 5:03:45 PM
    Author     : Administrator
--%>

<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UploadingExcel Page</title>
        <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" />

        <script type="text/javascript">
            function uploadexcel()
            {                
                var excelname = document.getElementById("excelname").value;               
                if(excelname==null || excelname=="")
                    {
                        alert("Please Select Excel File");
                        return false;
                    }
                    alert("going to ajax");
                    $.ajax({
                     url: '<%=request.getContextPath()%>/targetView.do?targetParams=getExcelSheet&excelname='+excelname,
                    success: function(data) {
                        alert("hai");
                        alert("date"+data);
                          }
                    });
                     canceuploading();
                
                
            }
            function canceuploading()
            {
                parent.document.getElementById("uploadframe").style.display='none';
                parent.document.getElementById("uploadexcel").style.display='none';
                parent.document.getElementById("fade").style.display='none';
               // parent.window.location.reload(true);// href=parent.window.location.href;
            }
        </script>
    </head>
    <body>
        <br/>
        <center>
            <form action="" name="excelform" method="post">
                <table>
                    <tr>
                        <td>
                            <input type="file" name="excelname" id="excelname">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Upload" onclick="uploadexcel()">
                            &nbsp;&nbsp;&nbsp;
                            <input type="button" value="Cancel" onclick="canceuploading()">
                        </td>
                    </tr>
                </table>
            </form>
        </center>
    </body>
</html>
--%>
<%--
    Document   : FileUploadAndSave
    Created on : Jan 7, 2010, 11:39:18 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html>
<head>
<title>Struts File Upload and Save Example</title>
<script type="text/javascript">
    function checkfun()
    {
       
      //  parent.cancelExcelClose();
    }
    </script>
<html:base/>
</head>
<body>
<html:form action="FileUploadAndSave" method="post"  enctype="multipart/form-data" onsubmit="checkfun()">
<table style="width:100%" align="center">

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
