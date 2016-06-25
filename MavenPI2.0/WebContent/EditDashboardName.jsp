<%-- 
    Document   : EditDashboardName
    Created on : Jul 15, 2010, 3:42:18 PM
    Author     : Manisha 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,java.sql.*,java.util.*,prg.db.PbDb"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<html>
    <head>
        <% String contextPath=request.getContextPath(); %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi EE</title>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            *{font:11px verdana;}
        </style>

        
    </head>
    <body id="mainBody">

        <%
            String dashbdid = request.getParameter("dashbdId");
            ////.println("dashbdid is in editdashboardname: " + dashbdid);
            String DashbdName = null;
            String Dashbddesc = null;
            PbDb pbdb = new PbDb();

            String dashNameQry = "select REPORT_NAME,REPORT_DESC from PRG_AR_REPORT_MASTER where REPORT_ID=" + dashbdid;
           // //.println("dashnameqry-in ---" + dashNameQry);
            PbReturnObject dashNameObj = pbdb.execSelectSQL(dashNameQry);

            if (dashNameObj.getRowCount() > 0) {
                DashbdName = dashNameObj.getFieldValueString(0, 0);
                Dashbddesc = dashNameObj.getFieldValueString(0, 1);
            }
        %>
        <form name="EditDashbdNameForm"  method="post">
            <table style="width:100%" >
                <tr>
                    <td valign="top" class="myHead" style="width:30%">Old Dashboard Name</td>
                    <td valign="top" style="width:80%">
                        <input type="text" maxlength="35" name="DashbdName" style="width:80%" id="DashbdName" readonly value="<%=DashbdName%>">
                    </td>
                </tr>
                <tr>
                    <td valign="top" class="myHead" style="width:40%">New Dashboard Name</td>
                    <td valign="top" style="width:70%">
                        <input type="text" maxlength="35" name="NewDashbdName" style="width:80%" id="NewDashbdName" onkeyup="tabdashmsg()" onfocus="document.getElementById('save').disabled = false;"><br><span id="DuplicateDash" style="color:red"></span>
                    </td>
                </tr>
                <tr>
                    <td valign="top" class="myHead" style="width:30%">Old Description</td>
                    <td valign="top" style="width:80%">
                        <input type="text" maxlength="35" name="DashbdDesc" style="width:80%" id="DashbdDesc" readonly value="<%=Dashbddesc%>">
                    </td>
                </tr>
                <tr>
                    <td valign="top" class="myHead" style="width:40%">New Description</td>
                    <td valign="top" style="width:70%">
                        <input type="text" maxlength="35" name="NewDashbddesc" style="width:80%" id="NewDashbddesc" >
                    </td>
                </tr>
            </table>
            <center>
                <table>
                    <tr>
                        <td align="center"><input type="button" class="navtitle-hover" style="width:auto" value="Save" id="SaveDashbd" onclick="saveDashbdName()"></td>
                    </tr>
                </table>
            </center>
            <input type="hidden" id="dashId" name="dashId" value="<%=dashbdid%>">

        </form>
        <div id="fade" class="black_overlay"></div>
        <script type="text/javascript">
            function saveDashbdName(){
                parent.closeDashbdEdit();
                var dashId=document.getElementById("dashId").value;
                var oldDashbdName=document.getElementById("DashbdName").value;
                var olddashbdDesc=document.getElementById("DashbdDesc").value;
                var newdashbdName=document.getElementById("NewDashbdName").value;
                newdashbdName=newdashbdName.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                var newDashbdDesc=document.getElementById("NewDashbddesc").value;
                newDashbdDesc=newDashbdDesc.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                // alert('oldDashbdName '+oldDashbdName+' - newdashbdName '+newdashbdName+' -- dashId is: '+dashId)
                // alert('olddashbdDesc '+olddashbdDesc+' - newDashbdDesc '+newDashbdDesc)

                if(newdashbdName==''){
                    alert("Please enter New Report Name");
                }
                else  if(newDashbdDesc==''){
                    alert("Please enter New Report Description")
                }
                else{
                    $.ajax({
                        url: 'dashboardTemplateAction.do?templateParam2=checkDashboardName&dashboardName='+newdashbdName+'&dashboardDesc='+newDashbdDesc,
                        success: function(data){
                            if(data!=""){
                                document.getElementById('DuplicateDash').innerHTML = "Dashboard Name already exists";
                                document.getElementById('save').disabled = true;
                            }
                            else if(data==''){
//                                document.forms.EditDashbdNameForm.action = "dashboardTemplateAction.do?templateParam2=EditDashBdName&dashboardName="+newdashbdName+"&dashboardDesc="+newDashbdDesc+"&editDashbdName='Y'&dashId="+dashId;
//                                document.forms.EditDashbdNameForm.method="POST";
//                                document.forms.EditDashbdNameForm.submit();
//                                parent.closeDashbdEdit();
//                                parent.document.forms.myFormH.action = "home.jsp#Dashboard_Studio";
//                                parent.document.forms.myFormH.submit();
                                $.ajax({
                        url: 'dashboardTemplateAction.do?templateParam2=EditDashBdName&dashboardName='+newdashbdName+'&dashboardDesc='+newDashbdDesc+'&dashId='+dashId+'&editDashbdName=Y',
                        success: function(data){                          
                                parent.document.forms.myFormH.action = "home.jsp#Dashboard_Studio";
                                parent.document.forms.myFormH.submit();
                        }
                                });
                            }
                        }
                    });
                }
                //window.location.reload(true);
            }
            function tabdashmsg(){
                document.getElementById('NewDashbddesc').value = document.getElementById('NewDashbdName').value;
            }
            function cancelRepName(){
                parent.closeDashbdEdit();
            }

        </script>

    </body>
</html>

