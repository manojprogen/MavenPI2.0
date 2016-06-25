<%-- 
    Document   : pbEditRepName
    Created on : Jan 26, 2010, 1:35:35 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,java.util.*,prg.db.Session,prg.db.PbReturnObject,java.sql.*,utils.db.*,prg.db.Session,prg.db.PbReturnObject"%>

<%      String reportId = request.getParameter("ReportId");
            ////////////////////////////////////////////////.println.println("reportid is: " + reportId);
            String repName = null;
            String repDesc = null;
            PbDb pbdb = new PbDb();
            String repNameQry = "select REPORT_NAME,REPORT_DESC from PRG_AR_REPORT_MASTER where REPORT_ID=" + reportId;
            PbReturnObject repNameObj = pbdb.execSelectSQL(repNameQry);
            if (repNameObj.getRowCount() > 0) {
                repName = repNameObj.getFieldValueString(0, 0);
                repDesc = repNameObj.getFieldValueString(0, 1);
                if(repName.contains("^")){
                    repName = repName.replace("^", "&") ;
                }else if(repName.contains("~")){
                    repName = repName.replace("~", "+") ;
                }else if(repName.contains("`")){
                    repName = repName.replace("`", "#") ;
                }else if(repName.contains("_")){
                    repName = repName.replace("_", "%") ;
            }
            }
            String folderId = "select folder_id from prg_ar_report_details where report_id=" + reportId;
            PbReturnObject roleobj = pbdb.execSelectSQL(folderId);
            int roleid = roleobj.getFieldValueInt(0, 0);
           //  //.println("roleid--" + roleid);
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi EE</title>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/global.css"/>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=contextPath%>/javascript/jquery.columnfilters.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/dragTable.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            *{font:11px verdana;}
        </style>

       

    </head>
    <body id="mainBody">

        <form name="editRepNameForm"  method="post">
            <%--<div id="editRepMeta" class="white_content"  align="justify" style="height:150px;width:400px">
                <center>
                    <br><br>--%>
                    <table style="width:100%" >
                        <tr>
                            <td valign="top" class="myHead" style="width:30%">Old Report Name</td>
                            <td valign="top" style="width:80%">
                                <input type="text" maxlength="35" name="reportName" style="width:80%" id="reportName" readonly value="<%=repName%>">
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">New Report Name</td>
                            <td valign="top" style="width:70%">
                                <input type="text" name="newreportName" style="width:80%" id="newreportName" onkeyup="tabmsg2()" onfocus="document.getElementById('save').disabled = false;"><br><span id="duplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" class="myHead" style="width:30%">Old Description</td>
                            <td valign="top" style="width:80%">
                                <input type="text"  name="description" style="width:80%" id="description" readonly value="<%=repDesc%>">
                            </td>
                        </tr>
                        
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">New Description</td>
                            <td valign="top" style="width:70%">
                                <input type="text" maxlength="35" name="newdescription" style="width:80%" id="newdescription" >
                            </td>
                        </tr>
                    </table>
                            <center>
                    <table>
                        <tr>
                            <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Save" id="save" onclick="saveRepName()"></td>
                            <%--<td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelRepName()"></td>--%>
                        </tr>
                    </table>
                </center>
                <%--</center>
            </div>--%>
            <input type="hidden" id="repId" name="repId" value="<%=reportId%>">
            <input type="hidden" id="roleid" name="" value="<%=roleid%>">
        </form>
        <div id="fade" class="black_overlay"></div>
         <script>
             function saveRepName(){
                 parent.closeEditRep();
		 var roleid=document.getElementById("roleid").value;
                 var repId=document.getElementById("repId").value;
                 var oldrepName=document.getElementById("reportName").value;
                 var oldrepDesc=document.getElementById("description").value;
                 var newrepName=document.getElementById("newreportName").value;
                 newrepName=newrepName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                 var newrepDesc=document.getElementById("newdescription").value;
                 newrepDesc=newrepDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_')
                 //alert('oldrepname: '+oldrepName+' - newrepname: '+newrepName+' -- reportid is: '+repId)
                 //alert('oldrepdesc: '+oldrepDesc+' - newrepdesc: '+newrepDesc)

                 if(newrepName==''){
                    alert("Please enter New Report Name");
                }
                else  if(newrepDesc==''){
                    alert("Please enter New Report Description")
                }
                else{
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=checkReportName&reportName='+newrepName+"&roleid="+roleid,
                        success: function(data){
                            if(data!=""){
                                document.getElementById('duplicate').innerHTML = "Report Name already exists";
                                document.getElementById('save').disabled = true;
                            }
                            else if(data==''){
                                $.ajax({
                        url: 'reportTemplateAction.do?templateParam=goToReportDesigner&reportName='+newrepName+"&reportDesc="+newrepDesc+"&editRepName=Y&repId="+repId,
                        success: function(data){                           
                                parent.document.forms.myFormH.action = "home.jsp#Report_Studio";
                                parent.document.forms.myFormH.submit();
                        }
                                });
                                //$("#editRepNameForm").attr("action", "reportTemplateAction.do?templateParam=goToReportDesigner&reportName="+newrepName+"&reportDesc="+newrepDesc+"&editRepName='Y'&repId="+repId);
                                //document.forms.editRepNameForm.action = "reportTemplateAction.do?templateParam=goToReportDesigner&reportName="+newrepName+"&reportDesc="+newrepDesc+"&editRepName=Y&repId="+repId;                                
//                                document.forms.editRepNameForm.method = "POST";
//                                document.forms.editRepNameForm.submit();
                                <%--document.getElementById('editRepMeta').style.display='none';
                                parent.document.getElementById("editRepName").style.display='none';
                                parent.document.getElementById('fade').style.display='none';--%>
//                                parent.closeEditRep();
//                                parent.document.forms.myFormH.action = "home.jsp#Report_Studio";
//                                parent.document.forms.myFormH.submit();
                            }
                        }
                    });
                }
                //window.location.reload(true);
            }
            function tabmsg2(){
                document.getElementById('newdescription').value = document.getElementById('newreportName').value;
            }
            function cancelRepName(){
                <%--document.getElementById('editRepMeta').style.display='none';
                parent.document.getElementById("editRepName").style.display='none';
                parent.document.getElementById('fade').style.display='none';--%>
               parent.closeEditRep();
            }
            
        </script>
    </body>
</html>
