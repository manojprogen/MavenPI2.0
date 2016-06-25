<%-- 
    Document   : uploadExcelData
    Created on : 15 Dec, 2012, 7:38:28 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="prg.db.PbReturnObject,java.util.ArrayList" %>
<%@page import="prg.db.PbDb" %>
<%@page import="utils.db.ProgenConnection" %>
<%@page import="java.sql.Connection" %>
<%
   String qry = "";
    qry = "select * from PRG_USER_CONNECTIONS";
    PbDb pbdb = new PbDb();
    PbReturnObject list = null;
    list = pbdb.execSelectSQL(qry);
    int vals = 0;
    vals = list.getRowCount();
    String contextPath = request.getContextPath().toString();
    String elemntId = request.getParameter("elementID");
    String busRole = request.getParameter("bussId");
    String periodType = request.getParameter("periodtype");
    String startValue = request.getParameter("startValue");
    String endValue = request.getParameter("endValue");
    String elemtName = request.getParameter("elemtName");
    String monthName = request.getParameter("monthName");
    String regName = request.getParameter("regName");
    String fromExcelUpload = request.getParameter("fromExcelUpload");
    String dayandWeekLevel = request.getParameter("dayandWeekLevelDistribution");
    String themeColor = "blue";
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
%>

<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Upload File</title>

        <html:base/>
        <script type="text/javascript">
     
            function Checkfiles()
            {
                var fup = document.getElementById('filename');
               
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);

                if( fileName!=null && (ext == "xls" || ext == "xlsx"))
                { 
                   // parent.$("#uploadExcel").dialog('close');
                     return true; 
                     
                }
                else
                {                 
                    alert("Upload .xls files only","Uplode");
                   
                    return false;
                }
                
            }
          
        </script>
        <style type="text/css">
            *{font:11px verdana;}
        </style>
    </head>
    <body>
        <table  align="center" id="headtableId">
            <h3 align="center">Excel Data Uploading</h3>
        </table>  
        <div align="center" style=" width: 100%" id="formId"> 
             <html:form  action="createtableAction.do?param=uploadExcelData1" method="post" enctype="multipart/form-data">
            <table  align="center" cellpadding="1" cellspacing="1" >
             <tr><td> <br><br>
            <table align="center">
                <tr style="width:100%">
                    <td align="left" colspan="2" class="migrate"><font color="red"><html:errors/></font>
                </tr>
                <tr style="width:100%;display: none;">
                    <td align="left" colspan="1" >Table Name : </td>
                    <td align="left" colspan="1" ><html:text property="elementId" styleId="elementId" value="<%=elemntId%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="bussRoleId" styleId="bussRoleId" value="<%=busRole%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="periodType" styleId="periodType" value="<%=periodType%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="startValue" styleId="startValue" value="<%=startValue%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="endValue" styleId="endValue" value="<%=endValue%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="elemtName" styleId="elemtName" value="<%=elemtName%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="fromExcelUpload" styleId="fromExcelUpload" value="<%=fromExcelUpload%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="dayandWeekLevel" styleId="dayandWeekLevel" value="<%=dayandWeekLevel%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="monthName" styleId="monthName" value="<%=monthName%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                    <td align="left" colspan="1" ><html:text property="regName" styleId="regName" value="<%=regName%>" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                <tr style="width:100%">
                    <td align="left" colspan="1" >File Name : <label style="color:Red">(.xls files only)</label> </td>
                    <td align="left" colspan="1" ><html:file property="filename" styleId="filename" style="background-color:lightgoldenrodyellow; color:black;"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr style="width:100%">
                    <td align="center" colspan="2" ><html:submit onclick ="return Checkfiles()" styleClass="navtitle-hover" >Upload File</html:submit>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
            </table></td></tr>
            </table>
        </html:form>
        </div>
    </body>
</html:html>
