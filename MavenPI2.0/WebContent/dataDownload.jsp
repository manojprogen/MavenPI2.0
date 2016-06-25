<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,prg.db.PbReturnObject,java.util.ArrayList,prg.db.PbDb,utils.db.ProgenConnection,java.sql.Connection"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%-- 
    Document   : dataDownload
    Created on : 26 Mar, 2012, 6:55:38 PM
    Author     : progen
--%>

<%
 //added by Dinanath
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
    String qry = "";
    qry = "select * from PRG_USER_CONNECTIONS";
    PbDb pbdb = new PbDb();
    PbReturnObject list = null;
    list = pbdb.execSelectSQL(qry);
        String contextPath=request.getContextPath();
%>

<%--<html:html>--%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <%--<script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>--%>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <%--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>--%>

        <link href="<%=contextPath%>/jQuery/jquery/themes/base/jquery.alerts.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.alerts.js"></script>

        <title>Download File</title>

     <%--   <html:base/> --%>
        
<!--        <style type="text/css">
            *{font:11px verdana;}
        </style>-->
    </head>
    <body>
        <script type="text/javascript">
            $(document).ready(function(){
                if ($.browser.msie == true){
                    $("#checkDiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                }else{
                    $("#checkDiv").dialog({
                        autoOpen: false,
                        height:200,
                        width: 200,
                        position: 'justify',
                        modal: true
                    });
                }
                });

            function Checkfiles()
            {
                var fup = document.getElementById('filename');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);

                if( fileName!=null && ext == "xls" || ext == "xlsx" )
                {
                    //document.forms.strutsuplaodfileform.action="<%=request.getContextPath()%>/createtableAction.do?param=uploadFile";
                    // document.forms.strutsuplaodfileform.submit();
                    return true;
                }
                else
                {
                  
                    jAlert("Upload .xls files only","Uplode");
                    // fup.focus();
                    // document.getElementById("checkDiv").style.display ='block'
                   
                    return false;
                }
                //                $.ajax({
                //                    url: '/createtableAction.do?param=uploadfile',
                //                    success: function(data){
                //                        if(data==true){
                //                            alert("uploaded successfully")                            
                //                        }else{
                //                            alert("error in uploading data")
                //                        }
                //                    }
                //                });               
            }
           
            function closeDiv(){
                parent.$(".uploadDialog").dialog('close');
            }
            function getTableNames()
            {
                var conid=$("#connid").val()
                 $.post('createtableAction.do?param=getCustomerTableNames&conid='+conid,$("#excelDownload").serialize(),
                    function(data)
                    {
                    var jsonVar=eval('('+data+')')
                    var tablenames=jsonVar.tablelist
                      var htmlVar=""
                        var jsonvaleuse=""
                        htmlVar+="<option  value=''>--SELECT--</option>"
                        for(var i=0;i<tablenames.length;i++)
                        {
                            jsonvaleuse=tablenames[i]
                            htmlVar+="<option  value='"+tablenames[i]+"'>"+jsonvaleuse+"</option>"
                        }
                        $("#tablelistid").html(htmlVar);
                    });
                
            }
//            function getDataToExcel(){
//                $.post("/createtableAction.do?param=getExcelDownload", $("#excelDownload").serialize(), function(data){
//                    var myval = data;
//                    if(myval=='true')
//                        alert("Excel file has been generated successfully");                   
//                    else
//                        alert("Excel file has not been generated");
//                                       
//                });
//            }
        </script>
        <%--   <html:form  action="createtableAction.do?param=uploadFile" method="post" enctype="multipart/form-data">

            <table width="30%" align="center">
                <tr style="width:100%">
                    <td align="left" colspan="2"><font color="red"><html:errors/></font>
                </tr>
                <tr style="width:100%">
                    <td align="right" colspan="1">Table Name</td>
                    <td align="left" colspan="1" ><html:text property="tablename" styleId="tablename" /></td>
                </tr>
                <tr>
                    <td align="right" colspan="1">Connection Name</td>                    
                    <td align="left" colspan="1" >
                        <select name="connid" id="connid" >
                            <% for (int i = 0; i < list.getRowCount(); i++) {
                            %>
                            <option value="<%=list.getFieldValueInt(i, 0)%>"><%=list.getFieldValueString(i, 1)%></option> 
                            <%}%>
                        </select>
                    </td>                   
                </tr> 
<!--                <tr style="width:100%">
                    <td align="right" colspan="1">File Name</td>
                    <td align="left" colspan="1" ><html:file property="filename" styleId="filename" /></td>
                </tr>
                <tr style="width:100%">
                    <td align="center" colspan="2"><html:submit onclick ="return Checkfiles()" styleClass="navtitle-hover" >Upload File</html:submit>
                    </td>
                </tr>                -->
            </table>

        </html:form> --%>
        <div align="center" style=" height: 50px; border-width: 2px; border-style: dotted;  margin-top: auto; margin-left: 200px; margin-right: 200px; margin-bottom: 200px; margin: 80px 10px 10px 10px; padding-bottom: 80px; padding-left: 150px; padding-top: 50px;"> &nbsp;
        <form id="excelDownload" name="excelDownload" method="post" action='<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp'>
            <table align="left">
                <tr>
                    
                 <%--   <td align="left" width="38%">
                        <input type="textbox" id="tabName" name="tabName">
                    </td>--%>
                    <td> <select id="aggType" name="dType" style="display: none">
                            <option value="exceldown">Sum</option></select></td>
                </tr>
                <tr>
                    <td align="right" width="100"><%=TranslaterHelper.getTranslatedInLocale("Connection_Name", cle)%></td>
                    <td align="left" colspan="1" >
                        <select name="connid" id="connid" onchange="getTableNames()">
                            <% for (int i = 0; i < list.getRowCount(); i++) {
                            %>
                            <option value="<%=list.getFieldValueInt(i, 0)%>"><%=list.getFieldValueString(i, 1)%></option> 
                            <%}%>
                        </select>
                    </td>                   
                </tr>
                <tr>
                      <td align="right" colspan="1"><%=TranslaterHelper.getTranslatedInLocale("table_name", cle)%></td>
                         <td><select id="tablelistid" name="tablelistid" style="width:auto">
                                </select>
                            </td>
                </tr>
                <tr>
                    <td align="center" width="38%">
                        <input type="submit" id="btn" value="<%=TranslaterHelper.getTranslatedInLocale("done", cle)%>" class="navtitle-hover" style="width:auto" onclick="getDataToExcel()">
                    </td>
                </tr>
            </table>
        </form>
        </div>    
    </body>
 <%--  </html:html> --%>
</html>
