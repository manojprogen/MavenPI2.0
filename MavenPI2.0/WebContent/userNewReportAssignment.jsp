
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
<%
boolean isCompanyValid=false;
String themeColor="blue";
if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
    PbReturnObject rolesObj = new PbReturnObject();
    PbDb pbdb = new PbDb();
    String userId = request.getParameter("userId");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->

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
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>


<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <script type="text/javascript">
            var y='';
            var msrArray=new Array();
            var xmlHttp;
            $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });
                 
            });
            
            $(function() {
                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',

                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html());
                    }
                }
            );
            });
            function initDialogue(){
           
             $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',

                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html());
                    }
                }
            );
            }

            function saveUsersFolders(userId) {
                dispUsers(userId);
            }
            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById("sortable");
                parentUL.removeChild(LiObj);
                index='u~'+index;
                var l=msrArray.indexOf(index);
                msrArray.splice(l,1);
            }
            function createColumn(elmntId,elementName){
                var parentUL=document.getElementById("sortable");
                var x=msrArray.toString();
                if(x.match(elmntId)==null){
                    msrArray.push(elmntId)
                    var childLI=document.createElement("li");
                    var uid=elmntId.split(",");
                    childLI.id=uid[1];
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id=elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);

                    var a=document.createElement("a");
                    a.href="javascript:deleteColumn('"+uid[1]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }else{
                    // alert('This userFolder is already added')
                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }

            function cancelUsersFolders(){
                parent.cancelUsersFolders();
            }
            function checkExist(chk){
               // alert(' in chd;; ')
                var chkarr=chk.split("*");
                for(var i=0;i<chkarr.length;i++){
                    msrArray.push(chkarr[i]);
                }
            }
            function dispUsers(userId){
                var usersFolders="";
                var repIds="";
                var usersFoldersUl=document.getElementById("sortable");
                var userFoldersIds=usersFoldersUl.getElementsByTagName("li");
                if(userFoldersIds.length!=0){
                    for(var i=0;i<userFoldersIds.length;i++){
                        var v="";
                        var fVal=userFoldersIds[i].id;
                        var f= fVal.split("~");
                        for(var t=0;t<f.length-1;t++){
                            if(t==0){
                               v=f[0];
                            }
                        }
                        repIds=repIds+","+v;
                    }
                }
                //start of code modified by Nazneen On 19 May 2014
               repIds = repIds.toString();
               $("#repIds").val(repIds);
//                $.ajax({
//                     url: '<%=request.getContextPath()%>'+'/UserRepAssign.do?userId='+userId+'&repIds='+repIds,
//                    success: function(data) {
//                        if(data==1){
//                            cancelUsersReports();
//                             alert("Reports Assigned to User Successfully")
//                        }
//                    }
//                });

                  $.post("<%=request.getContextPath()%>/UserRepAssign.do?userId="+userId,$("#myFormUser").serialize(),
                                function(data){
                        if(data==1){
                            cancelUsersReports();
                             alert("Reports Assigned to User Successfully")
                        }
                });
                 //end of code modified by Nazneen On 19 May 2014
            }
            function cancelUsersReports(){
                parent.cancelUsersReportAssign();
            }

            function checkExist1(chk){
                var chkarr=chk.split("*");
                $.ajax({
                    url: 'saveUserFolderAssignmentAll.do?grpId='+grpIdsall+'&userId='+userId+'&userFolderIds='+folderIdsall,
                    success: function(data) {
                    }
                });
                for(var i=0;i<chkarr.length;i++){
                    msrArray.push(chkarr[i]);
                }
            }

            function moveAll(allFolders,allNames){
                var allList=allFolders.split("*");
                var allNamesList=allNames.split("*");
                for(var i=0;i<allList.length;i++){
                    createColumn(allList[i],"<font style='color:black;font-family:verdana'>"+allNamesList[i]+"</font>");
                }
            }
            function deleteAll(){
                msrArray.splice(0,msrArray.length);
                var parentUL=document.getElementById("sortable");
                parentUL.innerHTML='';
            }
            function dispRoleRelatedReports(id){               
                 var folderId=$("#"+id).val();
//                 alert(folderId)
//                 document.getElementById('quicksearch').style.display = 'none';
                 $('#quicksearch').remove();
//                 $('#quicksearch'+id).hide();
//$('#ReportTable1').hide();
            
                $.post("<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=getRoleRelatedReports&folderId="+folderId+"&userId="+'<%=userId%>',
                function(data){
                    var htmlVar=""
                    
                  var jsonVar=eval('('+data+')')
                   var keys =new Array();
                    for (var key in jsonVar) {
                      if (jsonVar.hasOwnProperty(key)) {
                          keys.push(key);
                           }
                        }
                       
                       for(var i=0;i<keys.length;i++)
                        {
                          var jsonvalues=jsonVar[keys[i]]
                htmlVar+="<tr valign='top'><td valign='top' class='myDragTabs' class='ui-state-default3' style='width:200px' id='u,"+keys[i]+'~'+jsonvalues+"'><font style='color:black;font-family:verdana'>"+jsonvalues+"</font></td></tr>";
                                
                         }
                         $("#ReportTable1").html(htmlVar)
                       
                    
 
                          $('div#ReportTable1 tr').quicksearch({
                    position: 'before',
                    attached: 'div#ReportTable1',
                    loaderText: '',
                    delay: 100
            });
             
                         initDialogue();

              });
                
            }
             $(document).ready(function() {
             $('table#ReportTable tbody tr ').quicksearch({
                    position: 'before',
                    attached: 'table#ReportTable',
                    loaderText: '',
                    delay: 100
            });
            
             });
        </script>
        <style type="text/css">
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
            *{font:11px verdana}
        </style>
    </head>

    <%
            
           
            String getRolesbasedonUserId="";
        getRolesbasedonUserId="SELECT folder_Id,FOLDER_NAME,grp_id FROM PRG_USER_FOLDER WHERE folder_id IN(SELECT user_folder_id FROM prg_grp_user_folder_assignment WHERE user_id in("+userId+") ) order by FOLDER_NAME asc";
       
        rolesObj=pbdb.execSelectSQL(getRolesbasedonUserId);
        String getrepssql = "";
        PbReturnObject pbroreports = new PbReturnObject();
        if (isCompanyValid) {
                getrepssql = "select REPORT_ID,REPORT_NAME from prg_ar_report_master where report_id in(select report_id from account_report  where report_id in(select  report_id from  prg_ar_report_details " +
                        " where folder_id in(select " +
                        " user_folder_id from prg_grp_user_folder_assignment where user_id in("+userId+")) " +
                        "  ) and org_id in(select account_type from prg_ar_users where pu_id in("+userId+"))) order by report_name";
            pbroreports = pbdb.execSelectSQL(getrepssql);
               ////.println("reportss==="+getrepssql);
        } else {
            //check role assignment
        getrepssql = "select rd.REPORT_ID, rm.REPORT_NAME from prg_ar_report_details rd, " +
                    " prg_ar_report_master rm where folder_id in( select folder_id from prg_user_folder " +
                    " where grp_id in ( select grp_id from prg_grp_user_assignment where user_id=" + userId + " ) )" +
                    " and rd.report_id= rm.report_id order by report_name";
            pbroreports = pbdb.execSelectSQL(getrepssql);
            if (pbroreports.getRowCount() == 0) {
                getrepssql = "select rd.REPORT_ID, rm.REPORT_NAME from prg_ar_report_details rd, prg_ar_report_master rm " +
                        "where folder_id in(select folder_id from prg_user_folder where grp_id in( select " +
                        " grp_id from prg_grp_user_assignment where user_id=" + userId + ")) and rd.report_id= rm.report_id";
                pbroreports = pbdb.execSelectSQL(getrepssql);
            }
        //String grpAssignQ="select * from prg_grp_user_assignment where user_id="+userId;

        }
        //PbReturnObject pbroreports = pbdb.execSelectSQL(getrepssql);
            String getExistUserRepList = "select pu.REPORT_ID, ar.REPORT_NAME from prg_ar_user_reports pu,prg_ar_report_master ar where user_id =" + userId + " and pu.report_id= ar.report_id order by ar.report_name";
            ////////////////////////////.println.println(" getrepssql "+getrepssql);

            PbReturnObject pbro1 = pbdb.execSelectSQL(getExistUserRepList);
            String chks = "";
           /* for (int i = 0; i < pbroreports.getRowCount(); i++) {
                for (int j = 0; j < pbroreports.getRowCount(); j++) {

                    if (pbro1.getFieldValueInt(j, 0) == pbroreports.getFieldValueInt(i, 0)) {
                       // chks += "*u," + pbroreports.getFieldValueInt(i, 0) + "~" + pbroreports.getFieldValueString(i, 1);
                    }
                }
            }*/

            String allReports = "";
            String allRepNames = "";
            for (int i = 0; i < pbroreports.getRowCount(); i++) {
                allReports += "*u," + pbroreports.getFieldValueInt(i, 0) + "~" + pbroreports.getFieldValueString(i, 1);
                allRepNames += "*" + pbroreports.getFieldValueString(i, 1);
            }
            if (pbroreports.getRowCount() > 0) {
                allReports = allReports.substring(1);
                allRepNames = allRepNames.substring(1);
            }
            ArrayList allRe = new ArrayList();
            for (int m = 0; m < pbro1.getRowCount(); m++) {
                allRe.add(pbro1.getFieldValueString(m, "REPORT_ID"));
                 chks += "*u," + pbro1.getFieldValueInt(m, 0) + "~" + pbro1.getFieldValueString(m, 1);

            }
            ////////////////////////////.println.println(" chks-- "+chks);

    %>

    <body onload="checkExist('<%=chks%>')">
        <br>
        <center>
            <b style="color:#369;font-size:11px;font-family:verdana;">Reports List (Drag reports to Right and click Save)</b>
        </center>
        <br>
        <form name="myFormUser" action="" method="post" id="myFormUser">
            <input type="hidden" id="h" value="<%=request.getContextPath()%>">
            <input type="hidden" name="repIds" id="repIds" value="">

            <table align="center" border="0" width="100%" style="height:auto" >
                <tr valign="top">
                    <td align="center" colspan="2">
                        <select id="SelectRole" name="SelectRole" onchange="dispRoleRelatedReports(this.id)">
                <%if(rolesObj.getRowCount()>0){%>   
                    <option value="select">SELECT</option>
                   <%for(int i=0;i<rolesObj.getRowCount();i++){%>
                   <option value='<%=rolesObj.getFieldValueString(i,0)%>'><%=rolesObj.getFieldValueString(i,1)%></option>
                <%}
                 }%>
                     </select>
                    </td>
                </tr>
                <tr valign="top">
                    <td align="left">
                        <a href="javascript:void(0)" onclick="moveAll('<%=allReports%>','<%=allRepNames%>')" style="text-decoration:none;font-family:verdana;"> MoveAll</a>
                        &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;">Available Reports</b>
                    </td>
                    <td align="left">
                        <a href="javascript:void(0)" onclick="deleteAll()" style="text-decoration:none;font-family:verdana;"> DeleteAll</a>
                        &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;"> Assigned Reports</b>
                    </td>
                </tr>
            </table>
             <table align="center" border="1" width="100%" style="height:200px" id="1">
                <tr valign="top">
                    <td  class="draggedTable1" valign="top">
                        <div style="height:250Px;width:100%;overflow-y:auto" id="ReportTable1">
                            
                            <table  border="0" id="ReportTable">

                                <%if (pbroreports.getRowCount() > 0) {
                for (int i = 0; i < pbroreports.getRowCount(); i++) {
                  //  if (!allRe.contains(pbroreports.getFieldValueString(i, 0))) {
                                %>

                                <tr valign="top">
                                    <td valign="top" class="myDragTabs" class="ui-state-default3" style="width:200px" id="u,<%=pbroreports.getFieldValueInt(i, 0)%>~<%=pbroreports.getFieldValueString(i, 1)%>"><font style="color:black;font-family:verdana"><%=pbroreports.getFieldValueString(i, 1)%></font>
                                    </td>
                                </tr>
                                <%//}
                }
            }%>
                            </table >
                        </div>
                    </td>
                    <td id="dropTabs" style="width:50%;border:1px solid silver" class="myhead" valign="top">
                        <div style="height:250Px;width:100%;overflow-y:auto">
                            <ul id="sortable">
                                <%for (int i = 0; i < pbro1.getRowCount(); i++) {%>
                                <li class="navtitle-hover" style="width:auto;height:auto;color:white" id="<%=pbro1.getFieldValueInt(i, 0)%>~<%=pbro1.getFieldValueString(i, 1)%>">
                                    <table id="u,<%=pbro1.getFieldValueInt(i, 0)%>~<%=pbro1.getFieldValueString(i, 1)%>" >
                                        <tr><td style="backgroundColor:#e6e6e6">
                                                <a class="ui-icon ui-icon-close" href="javascript:void(0)" onclick="deleteColumn('<%=pbro1.getFieldValueInt(i, 0)%>~<%=pbro1.getFieldValueString(i, 1)%>')"></a>
                                            </td><td style="backgroundColor:#e6e6e6">
                                                <font style="color:black"> <%=pbro1.getFieldValueString(i, 1)%></font>
                                            </td>
                                        </tr>
                                    </table>
                                    <%}%>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <br><center>
            <input type="button" class="navtitle-hover" style="width:auto"  value="Save" onclick="dispUsers('<%=userId%>')">

        </center>
    </body>
</html>
