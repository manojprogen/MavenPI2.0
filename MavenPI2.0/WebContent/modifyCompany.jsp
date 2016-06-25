<%-- 
    Document   : modifyCompany
    Created on : Mar 26, 2010, 4:51:24 PM
    Author     : Administrator
--%>


<%@page import="java.util.*" %>
<%@page import="utils.db.ProgenConnection" %>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.db.PbDb" %>
<%@page import="prg.business.group.BusinessGroupDAO" %>
<%@page import="java.sql.Connection" %>
<%@page import="java.sql.ResultSet" %>
<%@page import="java.sql.PreparedStatement" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script>
            var y='';
            var msrArray=new Array();
            var xmlHttp;
            $(document).ready(function() {
                $("#companyList").treeview({
                    animated:"slow",
                    persist: "cookie"
                });
            });
            $(function() {
                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                $("#dropTabs").droppable(
                {
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html());
                    }
                }
            );

            });
            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById("sortable");
                parentUL.removeChild(LiObj);
                for(var i=0;i<msrArray.length;i++){
                    if(msrArray[i]==index.split("-")[1])
                        msrArray.splice(i,1);
                }
            }
            function createColumn(elmntId,elementName){
                var existLI="";
                var parentUL=document.getElementById("sortable");
                var availLI=parentUL.getElementsByTagName("li");                
                for(var k=0;k<availLI.length;k++){
                    var LiObj=document.getElementById(availLI[k].id);
                    parentUL.removeChild(LiObj);
                }
                var x=msrArray.toString();
                msrArray.splice(0,1);
                if(x.match(elmntId)==null){
                    msrArray.push(elmntId.split("-")[1])
                    var childLI=document.createElement("li");
                    var uid=elmntId.split("-");
                    childLI.id="comp-"+uid[1];
                    childLI.style.width='180px';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id="compTab-"+uid[1];
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    var a=document.createElement("a");
                    a.href="javascript:deleteColumn('"+elmntId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }else
                {
                    // alert('This userFolder is already added')
                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();               
            }
            function checkExist(chk){
                msrArray=new Array();
                msrArray.push(chk);
            }
            function saveUserCompany(userId){
                var userFolders="";
                var orgId="";
                var companyUl=document.getElementById("sortable");
                var orgIdLI=companyUl.getElementsByTagName("li");
                for(var i=0;i<orgIdLI.length;i++){
                    orgId=orgIdLI[i].id.split("-")[1];
                }
                if(orgId.length!=0){
                    $.ajax({
                        url: 'organisationDetails.do?param=modifyUserAccountDetails&orgId='+orgId+'&userId='+userId,
                        success: function(data) {
                            parent.document.forms.myForm.action = "<%=request.getContextPath()%>/AdminTab.jsp";
                            parent.document.forms.myForm.submit();
                        }
                    });
                }
            }
        </script>
        <style>
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:white;
                border:1px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
            *{font:11px verdana;}
            a{color:#369;text-decoration:none}
            a:hover{text-decoration:underline}
        </style>
    </head>

    <%
                PbDb pbdb = new PbDb();
                PbReturnObject availCompNamesObj = new PbReturnObject();
                String chks = "";
                String companyId = "";
                String companyNames = "";
                String getCompanyNamesQry ="";
                String getAvailCompanyNamesQry ="";
                String userId = request.getParameter("userId");
                String dbType = "";
                if (session.getAttribute("MetadataDbType") != null) {
                    dbType = (String) session.getAttribute("MetadataDbType");
                }
                if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    getCompanyNamesQry = "SELECT ORG_ID,ORGANISATION_NAME FROM PRG_ORG_MASTER WHERE getdate()-ORG_END_DATE<0";
                } else {
                    getCompanyNamesQry = "SELECT ORG_ID,ORGANISATION_NAME FROM PRG_ORG_MASTER WHERE SYSDATE-ORG_END_DATE<0";
                }
                PbReturnObject compNamesObj = pbdb.execSelectSQL(getCompanyNamesQry);
                ////.println("compNamesObj rowcount is==" + compNamesObj.getRowCount());
                String getuserDetsQry = "select ACCOUNT_TYPE from prg_ar_users where pu_id=" + userId;
                PbReturnObject userDetsObj = pbdb.execSelectSQL(getuserDetsQry);
                if (userDetsObj.getRowCount() > 0) {
                    getAvailCompanyNamesQry = "select distinct org.ORG_ID,org.ORGANISATION_NAME from prg_org_master org, prg_ar_users users where org.org_id= users.account_type and users.account_type=" + userDetsObj.getFieldValueString(0, 0);
                    availCompNamesObj = pbdb.execSelectSQL(getAvailCompanyNamesQry);
                }
                if (availCompNamesObj.getRowCount() > 0) {
                    chks = availCompNamesObj.getFieldValueString(0, 0);
                }
                for (int i = 0; i < compNamesObj.getRowCount(); i++) {
                    companyId += "comp-" + compNamesObj.getFieldValueInt(i, 0);
                    companyNames += compNamesObj.getFieldValueString(i, 2);
                }
                if (compNamesObj.getRowCount() > 0) {
                    companyId = companyId.substring(1);
                    companyNames = companyNames.substring(1);
                }
    %>
    <body onload="checkExist('<%=chks%>')">
        <br><br>
        <center><b style="font-size:12px;font-weight:bold;color:#369">Company List (Drag Only One Company to Right and click Save)</b></center>
        <form name="myFormComp" action="" method="post">
            <input type="hidden" id="h" value="<%=request.getContextPath()%>">
            <%--<table align="center" border="0" width="100%" >
                <tr><td align="left">
                        <a href="javascript:moveAll('<%=companyId%>','<%=companyNames%>')"> MoveAll</a>
                        &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b>Available Companies</b>
                    </td> <td align="left">
                        <a href="javascript:deleteAll()"> DeleteAll</a>
                        &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b>Assigned Companies</b>
                    </td>
                </tr>
            </table>--%>
            <table align="center" border="1" width="100%" style="height:200px" valign="top">
                <tr valign="top">
                    <td  class="draggedTable1" valign="top">
                        <div style="height:200Px;width:100%;overflow-y:auto">
                            <table  border="0">
                                <%if (compNamesObj.getRowCount() > 0) {
                                                for (int i = 0; i < compNamesObj.getRowCount(); i++) {%>
                                <tr valign="top">
                                    <td valign="top" class="myDragTabs" class="ui-state-default3" style="width:200px" id="company-<%=compNamesObj.getFieldValueString(i, 0)%>"><%=compNamesObj.getFieldValueString(i, 1)%></td></tr>
                                    <%}
                                                }
                                    %>
                            </table >
                        </div>
                    </td>
                    <td id="dropTabs" style="width:50%" class="myhead" valign="top">
                        <div style="height:200Px;width:100%;overflow-y:auto">
                            <ul id="sortable">
                                <%if (availCompNamesObj.getRowCount() > 0) {%>
                                <li class="navtitle-hover" style="width:180px;height:auto;color:white" id="comp-<%=availCompNamesObj.getFieldValueString(0, 0)%>">
                                    <table id="compTab-<%=availCompNamesObj.getFieldValueString(0, 0)%>">
                                        <tr><td>
                                                <a class="ui-icon ui-icon-close" href="javascript:deleteColumn('comp-<%=availCompNamesObj.getFieldValueString(0, 0)%>')"></a>
                                            </td><td>
                                                <font style="color:black"> <%=availCompNamesObj.getFieldValueString(0, 1)%></font>
                                            </td>
                                        </tr>
                                    </table>
                                </li>
                                <%
                                            }%>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <br>
        <center>
            <input type="button"  class="navtitle-hover" value="Save" onclick="saveUserCompany('<%=userId%>')">
        </center>
    </body>
</html>
