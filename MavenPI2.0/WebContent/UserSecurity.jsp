<%--
    Document   : userSecurity
    Created on : Jan 19, 2013, 11:48:49 AM
    Author     : Nazneen Khan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.SourceConn,java.util.*,utils.db.ProgenConnection,prg.db.PbReturnObject,prg.db.PbDb,prg.business.group.BusinessGroupDAO,java.sql.Connection,java.sql.ResultSet,java.sql.PreparedStatement"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String innerHtml=request.getParameter("dataHtml");
        PbDb pbdb = new PbDb();
        SourceConn sc = new SourceConn();
//        Connection conSource = sc.getConnection("oracle1", "", "", "","","","","","");
//        Connection conTarget = sc.getConnection("cccDw", "", "", "","","","","","");
        Connection conSource = ProgenConnection.getInstance().getConnection();
        PreparedStatement psGetComp;
        PreparedStatement psSetComp;

        String getusers = "select PU_ID, PU_LOGIN_ID from PRG_AR_USERS";
        psGetComp = conSource.prepareStatement(getusers);
        ResultSet pbrouseres = psGetComp.executeQuery();
        PbReturnObject pbrousers=new PbReturnObject(pbrouseres);
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
        conSource.close();
        String contextPath1=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Assignment</title>
          <script src="<%=contextPath1%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="<%=contextPath1%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath1%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath1%>/stylesheets/metadataButton.css" rel="stylesheet" />


        <script src="<%=contextPath1%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath1%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=contextPath1%>/stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath1%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath1%>/javascript/effects.explode.js"></script>
        <link type="text/css" href="<%=contextPath1%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath1%>/querydesigner/JS/jquery.columnfilters.js"></script>

    </head>
    <body>
        <table id="measureDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0">
            <tr><td align="left" class="migrate"><label>Select Connection : &nbsp;</label></td>
                <td align="left"><select name="connections" id="connections" onchange="assignUsers()"></select></td>
            <td align="left" class="migrate"><label>&nbsp;&nbsp;&nbsp;&nbsp;Select Company : &nbsp;</label></td>
            <td align="left"><select name="companyList" id="companyList" onchange="getUserList()"><%=innerHtml%></select></td>
            </tr>
        </table>
<!--               <label>Select Company :</label> <select id="companyList" name="companyList" onchange="getUserList()">
        
        </select>-->
        <br>
        <center><b style="color:#369;font-size:11px;font-family:verdana;">Users List (Drag Users to Right and click Save)</b></center>
        <br>
        <form name="myFormUser">
            <table align="center" border="0" width="100%" style="height:auto" >
               <tr><td align="left">
              </td> <td align="left">
               <a href="javascript:moveAll('<%=allUsers%>','<%=allUserNames%>')" style="text-decoration:none;font-family:verdana;"> Move All</a>
                         &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;">Available Users</b>
                </td> <td align="left">
                         <a href="javascript:deleteAll()" style="text-decoration:none;font-family:verdana;"> Delete All</a>
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

                         <div id="dropDiv1" style="height:250Px;width:100%;overflow-y:auto">

                             <ul id="sortable" >
                            <% // for (int i = 0; i < pbrousers.getRowCount(); i++) {
//            for (int j = 0; j < pbrousers.getRowCount(); j++) {
//                if (pbro1.getFieldValueInt(j, 0) == pbrousers.getFieldValueInt(i, 0)) {%>
<!--                            <li class="navtitle-hover" style="width:180px;color:white" id="">
                                <table id="u," >
                                    <tr><td style="backgroundColor:#e6e6e6">
                                            <a class="ui-icon ui-icon-close" href="javascript:deleteColumn('u,')"></a>
                                        </td><td style="backgroundColor:#e6e6e6">
                                            <font style="color:black"></font>
                                        </td>
                                    </tr>
                                </table>
                            </li>-->
                            <% // }
//            }
//        }%>
                        </ul>
                        </div>
                    </td>
                </tr>

        </table>

        </form>
        <br><center>
            <input type="button" class="navtitle-hover" style="width:auto"  value="Save" onclick="saveAssignedUsers()">

            <input type="button"  value="Cancel" class="navtitle-hover" style="width:auto" onclick="closeDilog('userAssignmentDiv')">
        </center>
        <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 50px;'/>
        </div>
         <script type="text/javascript" >
           var companyId=null;
           var AssignedmemberID=new Array();
                       var y='';
            var msrArray= new Array();
            var xmlHttp;
            $(document).ready(function() {
                getConnDetails();
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

            function createColumn(elmntId,elementName){

                var parentUL=document.getElementById("sortable");
                var exists=true;
                var elementID=elmntId.split('~')[1];
                for(var i=0;i<AssignedmemberID.length;i++){

                    if(elementID==AssignedmemberID[i])
                        exists=false;
                }
                var x=msrArray.toString();
                if(exists){
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
                    alert('This user is already added')
                }
                }else{
                    alert('This user has Assigned before')
                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }



            function checkExist(chk){
                var chkarr=chk.split("*");
                for(var i=0;i<chkarr.length;i++){

                    msrArray.push(chkarr[i]);
                }
            }

            function moveAll(allUsers,allNames){

                msrArray.splice(0,msrArray.length);
                AssignedmemberID.splice(0,AssignedmemberID.length);
                var parentUL=document.getElementById("sortable");
                parentUL.innerHTML='';

                var allList=allUsers.split("*");
                var allNamesList=allNames.split("*");
                for(var i=0;i<allList.length;i++){
                    createColumn(allList[i],allNamesList[i]);
                }


            }
            function deleteAll(){
                msrArray.splice(0,msrArray.length);
                AssignedmemberID.splice(0,AssignedmemberID.length);
                var parentUL=document.getElementById("sortable");
                parentUL.innerHTML='';
            }

            function assignUsers(){
                $("#sortable").html("")
             $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllCompanyDetails",function(data){
                            companyId="0000";
                            $("#companyList").html(data)
                        });
                $("#userAssignmentDiv").dialog('open')
            }
            function deleteColumn(index){
                var index1=index.split(",")[1];
                var val=index1.split("~")[1];

                var ind=AssignedmemberID.indexOf(val)
                 if(ind>=0){
                 AssignedmemberID.splice(ind,1)
                 }
                var LiObj=document.getElementById(index1);
                var parentUL=document.getElementById("sortable");
                parentUL.removeChild(LiObj);
                var l=msrArray.indexOf(index);
                msrArray.splice(l,1);
            }
            function getUserList(){
                companyId = $("#companyList").val();
                msrArray.splice(0,msrArray.length);
                document.getElementById('sortable').innerHTML = '';

                $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getCompAssignedUsers&companyId="+companyId,function(data){
                    var jsonVar=eval('('+data+')')
                    var compId=jsonVar.compId
                    var compName=jsonVar.compName
                    AssignedmemberID=compName;
                    var count=jsonVar.rsLength
                    var htmlVar="";
                 for(var i=0;i<count;i++){
                 htmlVar+="<li class=\"navtitle-hover\" style=\"width:180px;color:white\" id=\""+compId[i]+"~"+compName[i]+"\">"+
                                "<table id=\"u,"+compId[i]+"~"+compName[i]+"\">"+
                                    "<tr><td style=\"backgroundColor:#e6e6e6\">"+
                                            "<a class=\"ui-icon ui-icon-close\" href=\"javascript:deleteColumn('u,"+compId[i]+"~"+compName[i]+"')\"></a>"+
                                        "</td><td style=\"backgroundColor:#e6e6e6\">"+
                                            "<font style=\"color:black\">"+compName[i]+"</font>"+
                                        "</td>"+
                                    "</tr>"+
                                "</table>"+
                            "</li>";
                 }
                       $("#sortable").html(htmlVar)
                });
               }
               function saveAssignedUsers(){
                    var connectionID= $("#connections").val()
                    var users=new Array;
                    var userNames=new Array
                    var usersUl=document.getElementById("sortable");
                    var usersIds=usersUl.getElementsByTagName("li");
                    
               for(var i=0;i<usersIds.length;i++){
                    users.push(usersIds[i].id.split("~")[0]);
                    userNames.push(usersIds[i].id.split("~")[1])
                }

             var urlVar=""
             if(companyId!=null){
                 $("#loadingmetadata").show();
                       if(companyId !='0000'){
                           urlVar='userLayerAction.do?userParam=assignuserToComp&companyId='+companyId+'&users='+users.toString()+'&userNames='+userNames+'&connectionID='+connectionID
                    $.ajax({
                        url:urlVar,
                        success: function(data) {
                                   if(data==1){
                                       alert("User Assigned Successfully")
                                   }
                                else{
                                     alert("Error in assignment !")
                                }
                                $("#loadingmetadata").hide();
                                parent.$("#userAssignmentDiv").dialog('close');
                        }
                    });
                       }
                       else{
                             alert("Select a Company");
                       }
             }else{
              alert("Refresh Connection");
             }
        }
         function getConnDetails(){
               $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                      $("#connections").html(data)
          });
            }
 </script>
    </body>
</html>