
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.PreparedStatement,java.sql.ResultSet,java.sql.Connection,prg.business.group.BusinessGroupDAO,prg.db.PbDb,prg.db.PbReturnObject,java.util.*,utils.db.ProgenConnection" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

        PbDb pbdb = new PbDb();
        String grpId = request.getParameter("grpId");
        String roleId=request.getParameter("roleId");
        String flage="";
        if(request.getParameter("")!=null){
         flage=request.getParameter("flage");
        }
        String reportIds="";
        if(request.getParameter("reportIds")!=null)
            reportIds=request.getParameter("reportIds");
       // String getUserList = "select distinct puc.connection_id,puc.user_name FROM PRG_USER_CONNECTIONS puc,";
       // getUserList += " prg_db_master_table pdmt, prg_grp_buss_table pgbt, prg_grp_buss_table_src pgbts";
      //  getUserList += " where puc.connection_id= pdmt.connection_id and pgbt.buss_table_id= pgbts.buss_table_id ";
       // getUserList += " and pgbts.db_table_id=pdmt.table_id and pgbt.buss_table_id ";
       // getUserList += " in( select BUSS_TABLE_ID from PRG_GRP_BUSS_TABLE where grp_Id=" + grpId + ")";


      //  PbReturnObject pbro = pbdb.execSelectSQL(getUserList);
     ////   String connectionId = String.valueOf(pbro.getFieldValueInt(0, 0));
      //  String userName = pbro.getFieldValueString(0, 1);
     //   BusinessGroupDAO bgdao = new BusinessGroupDAO();
     //   Connection con = bgdao.getConnectionIdConnection(connectionId);
       // String getuserssql = "select PU_ID, PU_LOGIN_ID from PRG_USERS";
        String getuserssql = "select PU_ID, PU_LOGIN_ID from PRG_AR_USERS";
       // PreparedStatement ps = con.prepareStatement(getuserssql);
        // ResultSet rs = ps.executeQuery();
       // PbReturnObject pbrousers = new PbReturnObject(rs);
       // con.close();
        PbReturnObject pbrousers = pbdb.execSelectSQL(getuserssql);

       // pbrousers.writeString();
        String getExistUserList = "SELECT  USER_ID,USER_NAME FROM PRG_GRP_USER_ASSIGNMENT where grp_id = "+ grpId + " and FOLDER_ID ="+roleId;
        PbReturnObject pbro1 = pbdb.execSelectSQL(getExistUserList);
        pbro1.writeString();
        String chks = "";
        for (int i = 0; i < pbrousers.getRowCount(); i++) {
            for (int j = 0; j < pbrousers.getRowCount(); j++) {

                if (pbro1.getFieldValueInt(j, 0) == pbrousers.getFieldValueInt(i, 0)) {
                    chks += "*u," + pbrousers.getFieldValueInt(i, 0) + "~" + pbrousers.getFieldValueString(i, 1);
                }
            }
        }
        if (pbro1.getRowCount() > 0) {
            chks = chks.substring(1);
        }

        String allUsers = "";
        String allUserNames = "";
        for (int i = 0; i < pbrousers.getRowCount(); i++) {
            allUsers += "*u," + pbrousers.getFieldValueInt(i, 0) + "~" + pbrousers.getFieldValueString(i, 1);
            allUserNames += "*" + pbrousers.getFieldValueString(i, 1);
        }
        if (pbrousers.getRowCount() > 0) {
            allUsers = allUsers.substring(1);
            allUserNames = allUserNames.substring(1);
        }
        String conytextPath=request.getContextPath();
    %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
          <script src="<%=conytextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="<%=conytextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=conytextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=conytextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />

      
        <script src="<%=conytextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=conytextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=conytextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=conytextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=conytextPath%>/javascript/effects.explode.js"></script>
        <link type="text/css" href="<%=conytextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=conytextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <script type="text/javascript" >
         $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });
            });
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


                    .loading_image{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width:900%;
                height: 82%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                z-index:1001;
                overflow:auto;
            }
        </style>
    </head>

    
    <body onload="checkExist('<%=chks%>')">

        <br>
        <center><b style="color:#369;font-size:11px;font-family:verdana;">Users List (Drag Users to Right and click Save)</b></center>
        <br>
        <form name="myFormUser">
            <table align="center" border="0" width="100%" style="height:auto" >
               <tr><td align="left">
                    <a href="javascript:moveAll('<%=allUsers%>','<%=allUserNames%>')" style="text-decoration:none;font-family:verdana;"> MoveAll</a>
                         &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;">Available Users</b>
                </td> <td align="left">
                         <a href="javascript:deleteAll()" style="text-decoration:none;font-family:verdana;"> DeleteAll</a>
                         &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;"> Assigned Users </b>
                    </td>
                </tr>

               
                
            </table>
           
            <table align="center" border="1" width="100%" style="height:200px" valign="top">
                 
                <tr>

                    <td  class="draggedTable1" valign="top">
                        <div style="height:250Px;width:100%;overflow-y:auto">
                        <table  border="0">
                            <%

        if (pbrousers.getRowCount() > 0) {
            for (int i = 0; i < pbrousers.getRowCount(); i++) {

                            %>
                            <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                            <tr><td class="myDragTabs" class="ui-state-default3" style="width:200px" id="u,<%=pbrousers.getFieldValueInt(i, 0)%>~<%=pbrousers.getFieldValueString(i, 1)%>"><font style="color:black;font-family:verdana"><%=pbrousers.getFieldValueString(i, 1)%></font></td></tr>
                            <%
            }
        }
                            %>
                        </table >
                               </div>
                    </td>
                    <td id="dropTabs" style="width:50%;border:1px solid silver" class="myhead" valign="top">

                         <div style="height:250Px;width:100%;overflow-y:auto">
                        <ul id="sortable" >
                            <% for (int i = 0; i < pbrousers.getRowCount(); i++) {
            for (int j = 0; j < pbrousers.getRowCount(); j++) {
                if (pbro1.getFieldValueInt(j, 0) == pbrousers.getFieldValueInt(i, 0)) {%>
                            <li class="navtitle-hover" style="width:180px;color:white" id="<%=pbrousers.getFieldValueInt(i, 0)%>~<%=pbrousers.getFieldValueString(i, 1)%>">
                                <table id="u,<%=pbrousers.getFieldValueInt(i, 0)%>~<%=pbrousers.getFieldValueString(i, 1)%>" >
                                    <tr><td style="backgroundColor:#e6e6e6">
                                            <a class="ui-icon ui-icon-close" href="javascript:deleteColumn('u,<%=pbrousers.getFieldValueInt(i, 0)%>~<%=pbrousers.getFieldValueString(i, 1)%>')"></a>
                                        </td><td style="backgroundColor:#e6e6e6">
                                            <font style="color:black"> <%=pbrousers.getFieldValueString(i, 1)%></font>
                                        </td>
                                    </tr>
                                </table>
                            </li>
                            <% }
            }
        }%>
                        </ul>
                        </div>
                    </td>
                </tr>

        </table>
 
        </form>
        <br><center>
            <input type="button" class="navtitle-hover" style="width:auto"  value="Save" onclick="saveUsers('<%=grpId%>','<%=roleId%>')">

            <input type="button"  value="Cancel" class="navtitle-hover" style="width:auto" onclick="cancelUsers()">
        </center>

            <!--added by krishan-->
 <div id="loading" class="loading_image">
            <img id="imgId" src="images/help-loading.gif"  border="0px" style="position:absolute;left:150px;top:0px">
 </div>
              <script type="text/javascript" >
            var y='';
            var msrArray=new Array();
            var xmlHttp;
           

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

            function saveUsers(grpId,rolID)
            {

                dispUsers(grpId,rolID);
               
            }



            function deleteColumn(index){
                var index1=index.split(",")[1];
                // alert(index1)
                var LiObj=document.getElementById(index1);
                // alert('ined '+index);
                var parentUL=document.getElementById("sortable");

                parentUL.removeChild(LiObj);
                var l=msrArray.indexOf(index);
                //alert(l)
                msrArray.splice(l,1);
                // alert('mrrarr  '+msrArray)
            }

            function createColumn(elmntId,elementName){

                var parentUL=document.getElementById("sortable");

                var x=msrArray.toString();
                // alert(elmntId+'   '+x.match(elmntId));
                if(x.match(elmntId)==null){
                    msrArray.push(elmntId)

                    var childLI=document.createElement("li");
                    var uid=elmntId.split(",");
                    childLI.id=uid[1];
                    childLI.style.width='180px';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id=elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                  //cell1.style.backgroundColor="#e6e6e6";

                    var a=document.createElement("a");
                    a.href="javascript:deleteColumn('"+elmntId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    //cell2.style.backgroundColor="#e6e6e6";
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }else
                {
                    // alert('This user is already added')
                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }

            function cancelUsers(){

              parent.$("#userAssigned").dialog('close');
              parent.$("#assingHtmlReps").dialog('close');
            }

            function dispUsers(grpId,roleId){
                $("#loading").show();
                var users=new Array;
                var userNames=new Array

                var usersUl=document.getElementById("sortable");
                var usersIds=usersUl.getElementsByTagName("li");
                // alert('userIds----'+usersIds)
                for(var i=0;i<usersIds.length;i++){
                    //  alert(usersIds[i].id);
                    users.push(usersIds[i].id.split("~")[0]);
                    userNames.push(usersIds[i].id.split("~")[1])
                }
               
                if(usersIds.length!=0){
             //alert("if")
             var urlVar=""
                       if(grpId !='0')
                           urlVar='userLayerAction.do?userParam=assignRoleToUser&grpId='+grpId+'&users='+users.toString()+'&roleId='+roleId+'&userNames='+userNames
                           else
                             urlVar='userLayerAction.do?userParam=assignRoleToUser&grpId='+grpId+'&users='+users.toString()+'&roleId='+roleId+'&userNames='+userNames+'&reportIDs=<%= reportIds %>'
                    $.ajax({
                        url:urlVar,
//                        url: 'saveAssignUser.do?grpId='+grpId+'&users='+users,
                        success: function(data) {
                            //alert("data\t"+data)
                             if(grpId !='0'){
                                 $("#loading").hide();
                                 alert("User Assigned Successfully")
                                cancelUsers();
                             }else{
                                // alert("2")
                                   if(data=="true"){
                                    //  alert("3")
                                         $("#loading").hide();
                                          parent. $("#assingHtmlReps").dialog('close');
                                   }
                           
                                else{
                                  //  alert("else")
                                    $("#loading").hide();
                                     alert("Error in assign")
                        }

                              }



                        }
                    });

               }
             else{
             //alert("else")
                  $.ajax({
                        url:'userLayerAction.do?userParam=assignRoleToUser&grpId='+grpId+'&roleId='+roleId,
//                        url: 'saveAssignUser.do?grpId='+grpId+'&users='+users,
                        success: function(data) {
                            $("#loading").hide();
                                cancelUsers();

                        }
                    });
                      $("#loading").hide();
                cancelUsers();
             }
            }
            function checkExist(chk){
                var chkarr=chk.split("*");
                for(var i=0;i<chkarr.length;i++){

                    //alert(chkarr[i])
                    msrArray.push(chkarr[i]);
                }
            }

            function moveAll(allUsers,allNames){

                /// alert('moveall'+allReports)
                // alert('names--'+allNames)
                var allList=allUsers.split("*");
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
