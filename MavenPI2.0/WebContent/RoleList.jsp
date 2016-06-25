<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.Connection,java.sql.ResultSet,java.sql.PreparedStatement,java.util.*,utils.db.ProgenConnection,prg.db.PbReturnObject,prg.db.PbDb,prg.business.group.BusinessGroupDAO"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

                    String themeColor="blue";
        if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
                    String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%--<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>
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


        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>
          <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />--%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
<!--           <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
      


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
        String orgId=request.getParameter("orgId");
             PbReturnObject pbro1=null;

            // String loginUserId = session.getAttribute("USERID").toString();
          //  String loginUserId = request.getParameter("userId");
            String getfolderList = "select folder_id,folder_name from prg_user_folder order by folder_name";
            PbReturnObject pbro = pbdb.execSelectSQL(getfolderList);
            ////.println("getfolderList=="+getfolderList);
            String roleId="";
            String roleidSql="select BUSS_ROLE from PRG_ORG_MASTER where ORG_ID="+orgId;
            PbReturnObject pbroroleId = pbdb.execSelectSQL(roleidSql);
            if(pbroroleId.getRowCount()>0){
             roleId=pbroroleId.getFieldValueString(0,0);
            }
            ////.println("roleId=="+roleId);
            if(!roleId.equalsIgnoreCase("") && roleId!=null && !roleId.equalsIgnoreCase("null")){
            String ExistFolders = "select folder_id,folder_name from prg_user_folder where folder_id in("+roleId+") order by folder_name";
             ////.println("ExistFolders"+ExistFolders);
             pbro1 = pbdb.execSelectSQL(ExistFolders);

            }

            String chks = "";
            if(pbro1!=null){
                ////.println("in if");
            if (pbro1.getRowCount() > 0 && pbro.getRowCount()>0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    for (int j = 0; j < pbro.getRowCount(); j++) {

                        if (pbro1.getFieldValueInt(j, 0) == pbro.getFieldValueInt(i, 0)) {
                            chks += "*u~" + pbro.getFieldValueInt(i, 0) + "," + pbro.getFieldValueInt(i, 1);
                        }
                    }
                }

                chks = chks.substring(1);
            }
            }
            String allFolders = "";
            String allFolderNames = "";
            for (int i = 0; i < pbro.getRowCount(); i++) {
                allFolders += "*u~" + pbro.getFieldValueInt(i, 0) + "," + pbro.getFieldValueString(i, 1);
                allFolderNames += "*" + pbro.getFieldValueString(i, 1);
            }
            if (pbro.getRowCount() > 0) {
                allFolders = allFolders.substring(1);
                allFolderNames = allFolderNames.substring(1);
            }
            // if(pbro1.getRowCount()>0){
        %>
    <body onload="checkExist('<%=chks%>')">


        <br><br>
        <center><b style="font-size:12px;font-weight:bold;color:#369">Business Roles List (Drag Business Roles to Right and click Save)</b></center>
        <form name="myFormUser" action="" method="post">
            <input type="hidden" id="h" value="<%=request.getContextPath()%>">
            <table align="center" border="0" width="100%" >
                <tr><td align="left">
                        <a href="javascript:moveAll('<%=allFolders%>','<%=allFolderNames%>')"> MoveAll</a>
                        &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b>Available BusinessRoles</b>
                    </td> <td align="left">
                        <a href="javascript:deleteAll()"> DeleteAll</a>
                        &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b> Assigned BusinessRoles </b>
                    </td>
                </tr>
                  

            </table>

            <table align="center" border="1" width="100%" style="height:200px" valign="top">
                <tr valign="top">
                    <td  class="draggedTable1" valign="top">
                        <div style="height:200Px;width:100%;overflow-y:auto">
                            <table  border="0">
                                <%if (pbro.getRowCount() > 0 && pbro!=null) {
                      for (int i = 0; i < pbro.getRowCount(); i++) {%>
                                <tr valign="top">

                                <td valign="top" class="myDragTabs" class="ui-state-default3" style="width:200px" id="u~<%=pbro.getFieldValueString(i, 0)%>,<%=pbro.getFieldValueString(i, 1)%>"><%=pbro.getFieldValueString(i, 1)%></td></tr>
                                <%}
                                }

            %>

                            </table >
                        </div>
                                        </td>
                    <td id="dropTabs" style="width:50%" class="myhead" valign="top">
                        <div style="height:200Px;width:100%;overflow-y:auto">
                            <ul id="sortable">

                                <%if ( pbro1!=null){
                            if (pbro1.getRowCount()>0){
                    for (int i = 0; i < pbro1.getRowCount(); i++) {
                        %>
                                <li class="navtitle-hover" style="width:180px;height:auto;color:white" id="<%=pbro1.getFieldValueString(i, 0)%>,<%=pbro1.getFieldValueString(i, 1)%>">
                                    <table id="u~<%=pbro1.getFieldValueString(0, 0)%>,<%=pbro1.getFieldValueString(i, 1)%>">
                                        <tr><td >
                                                <a class="ui-icon ui-icon-close" href="javascript:deleteColumn('<%=pbro1.getFieldValueString(i, 0)%>,<%=pbro1.getFieldValueString(i, 1)%>')"></a>
                                            </td><td >
                                                <font style="color:black"> <%=pbro1.getFieldValueString(i, 1)%></font>
                                            </td>
                                        </tr>
                                    </table>
                                </li>
                                <%
                             }
                             }
                             } %>

                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <br>
        <center>
            <input type="button"  class="navtitle-hover" value="Save" onclick="saveUsersFolders('<%=orgId%>')">
        </center>
          <script>
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

            function saveUsersFolders(orgId)
        {

                dispUsers(orgId);
                /*
                var cols = dispUsers();
                alert('cols '+cols)
                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }
                ctxPath=document.getElementById("h").value;
                y = y.substring(0,(y.length-1));
                alert("y is "+y);
                var url=ctxPath+"/saveAssignUser.do?grpId="+grpId;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);
                parent.cancelCols();
                 */
            }



            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                //alert('LiOBJ '+index);
                var parentUL=document.getElementById("sortable");
                //alert('parentUL '+index)

                parentUL.removeChild(LiObj);
                index='u~'+index;
                //alert(index);
                var l=msrArray.indexOf(index);
                msrArray.splice(l,1);

            }


            function createColumn(elmntId,elementName){
                var parentUL=document.getElementById("sortable");
               // alert(msrArray.length);
                var x=msrArray.toString();
                if(x.match(elmntId)==null){
                     //if(msrArray.length<1){
                    msrArray.push(elmntId)

                    var childLI=document.createElement("li");
                    var uid=elmntId.split("~");
                    childLI.id=uid[1];
                    childLI.style.width='180px';
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
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                    // }

                }else
                {
                    // alert('This userFolder is already added')
                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }

            function cancelUsersFolders(){

                parent.cancelUsersFolders();
            }
            function checkExist(chk){

                var chkarr=chk.split("*");
                for(var i=0;i<chkarr.length;i++){
                    msrArray.push(chkarr[i]);
                }
            }

            function dispUsers(orgId){
                var usersFolders="";
                var grpIds="";
                var usersFoldersUl=document.getElementById("sortable");
                var userFoldersIds=usersFoldersUl.getElementsByTagName("li");
                // alert('userIds----'+usersIds)
                for(var i=0;i<userFoldersIds.length;i++){
                    //  alert(usersIds[i].id);
                    usersFolders=usersFolders+","+userFoldersIds[i].id.split(",")[0];

                }
                if(userFoldersIds.length!=0){

                    usersFolders=usersFolders.substr(1);
                    grpIds=grpIds.substr(1);
                    //  alert('organisationDetails.do?param=assignRoleToAcc&orgId='+orgId+"&accountFolderId="+usersFolders)
                    $.ajax({
                        url: 'organisationDetails.do?param=assignRoleToAcc&orgId='+orgId+"&accountFolderId="+usersFolders,
                        success: function(data) {
                           // alert(data)
            parent.refAccRole();
        }
                    });



                }
                else{/*
                var grpIdsall=document.getElementById("grpIdsall").value;
                 var folderIdsall=document.getElementById("folderIdsall").value;
                 alert(grpIdsall+'-----'+folderIdsall)
                   $.ajax({
                        url: 'saveUserFolderAssignmentAll.do?grpId='+grpIdsall+'&userId='+userId+'&userFolderIds='+folderIdsall,
                        success: function(data) {
                           //alert(data)
                            if(data!=""){
                                cancelUsersFolders();
                            }
                        }
                    });
                 */
                    parent.refAccRole();
                }
            }




            function moveAll(allFolders,allNames){

                /// alert('moveall'+allReports)
                // alert('names--'+allNames)
                var allList=allFolders.split("*");
                var allNamesList=allNames.split("*");
                for(var i=0;i<allList.length;i++){
                    // alert(allList[i]+'------------'+allNamesList[i])
                    createColumn(allList[i],allNamesList[i]);
                }


            }


            function deleteAll(){
                // alert(msrArray)
                msrArray.splice(0,msrArray.length);
                var parentUL=document.getElementById("sortable");
                parentUL.innerHTML='';
                // alert(msrArray)
            }
          </script>
    </body>
</html>
