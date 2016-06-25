<%-- 
    Document   : pbAssignHeadlines.jsp
    Created on : 4 May, 2012, 10:58:35 AM
    Author     : progen
--%>

<%@page  contentType="text/html" pageEncoding="UTF-8" import="java.util.*,utils.db.ProgenConnection,prg.db.PbReturnObject,prg.db.PbDb,prg.business.group.BusinessGroupDAO,java.sql.Connection,java.sql.ResultSet,java.sql.PreparedStatement" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

        PbDb pbdb = new PbDb();
        String grpId = request.getParameter("grpId");
        String roleId=request.getParameter("roleId");
        String reportId=request.getParameter("reportId");
        String userId=request.getParameter("userId");
        String headlineId=request.getParameter("headlineId");
        String getusers="SELECT a.user_id,b.user_name FROM PRG_GRP_USER_FOLDER_ASSIGNMENT a,prg_user_assignments b WHERE a.user_id=b.user_id AND a.user_folder_id IN("+roleId+") AND b.headlines='Y' AND a.user_id!="+userId;
        PbReturnObject pbrousers=pbdb.execSelectSQL(getusers);
       
     //   String getExistUserList = "SELECT distinct PU_ID,PU_LOGIN_ID FROM PRG_AR_USERS where PU_ID in (SELECT distinct USER_ID FROM PRG_AR_USER_REPORTS where REPORT_ID=" + reportId + ") and PU_ID!=" + userId;
       //  PbReturnObject pbro1 = pbdb.execSelectSQL(getExistUserList);
       //  String chks = "";
       //  for (int i = 0; i < pbro1.getRowCount(); i++) {
        //         chks += "*u," + pbro1.getFieldValueInt(i, 0) + "~" + pbro1.getFieldValueString(i, 1);
         //   }
                  
      //  if (pbro1.getRowCount() > 0) {
       //    chks = chks.substring(1);
       // }

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
        String contextPath=request.getContextPath();
    %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
          <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />


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
        </style>
    </head>


    <body>

      
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
                        <ul id="sortableheadline" >
                            
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
                var parentUL=document.getElementById("sortableheadline");

                parentUL.removeChild(LiObj);
                var l=msrArray.indexOf(index);
                //alert(l)
                msrArray.splice(l,1);
                // alert('mrrarr  '+msrArray)
            }

            function createColumn(elmntId,elementName){

                var parentUL=document.getElementById("sortableheadline");

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
                $("#sortableheadline").sortable();
                $("#sortableheadline").disableSelection();
            }

            function cancelUsers(){

              parent.$("#userAssignHeadline").dialog('close');
              parent.$("#assignheadlineReps").dialog('close');
            }

            function dispUsers(grpId,roleId){
                var users=new Array;
                var userNames=new Array

                var usersUl=document.getElementById("sortableheadline");
                var usersIds=usersUl.getElementsByTagName("li");
                // alert('userIds----'+usersIds)
                for(var i=0;i<usersIds.length;i++){
                    //  alert(usersIds[i].id);
                    users.push(usersIds[i].id.split("~")[0]);
                    userNames.push(usersIds[i].id.split("~")[1])
                }

                if(usersIds.length!=0){
              $.ajax({
                        url:'dataSnapshot.do?doAction=shareHeadlinetoUser&users='+users.toString()+'&roleId='+roleId+'&userNames='+userNames+'&reportId='+<%= reportId %>+'&headlineId='+<%=headlineId%>,
                        success:function(data){
                             $("#loading").hide();
                             cancelUsers();
                             if(data=='success')
                            alert("HeadLine shared  sucessfully to the usres")
                            else
                                 alert("Error in assign");
                        }
                    });

               }
             else{
                     $.ajax({
                        url:'dataSnapshot.do?doAction=shareHeadlinetoUser&userId='+<%=userId%>+'&roleId='+roleId,
                        success: function(data) {
                                cancelUsers();

                        }
                    });
                cancelUsers();
             }
            }
         
            function moveAll(allUsers,allNames){
            //alert("moveall")
                var allList=allUsers.split("*");
                var allNamesList=allNames.split("*");
                for(var i=0;i<allList.length;i++){
                    // alert(allList[i]+'------------'+allNamesList[i])
                    createColumn(allList[i],allNamesList[i]);
                }
            }
            function deleteAll(){
               
                msrArray.splice(0,msrArray.length);
                var parentUL=document.getElementById("sortableheadline");
                parentUL.innerHTML='';
                // alert(msrArray)
            }
        </script>
    </body>
</html>
