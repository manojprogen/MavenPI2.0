
<%@page contentType="text/html" pageEncoding="UTF-8" import="utils.db.ProgenConnection,prg.db.PbReturnObject,prg.db.PbDb,prg.business.group.BusinessGroupDAO,java.sql.Connection,java.sql.ResultSet,java.sql.PreparedStatement,java.util.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            PbDb pbdb = new PbDb();
            String reportId = String.valueOf(request.getAttribute("REPORTID"));
            String reportType = String.valueOf(request.getAttribute("ReportType"));
            String userId = String.valueOf(session.getAttribute("USERID"));
            String userFolderId = String.valueOf(request.getAttribute("UserFolderIds"));
              String sourcePage=String.valueOf(request.getAttribute("SourcePage"));
            String oldReportId="";
            if(sourcePage.equalsIgnoreCase("viewer")){
               oldReportId=String.valueOf(request.getAttribute("OLDREPORTID"));
            sourcePage="Viewer";
            }else{

            sourcePage="Designer";
            }
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Indicus Analytics</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>

<!--    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
       
<!--    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
<!--    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
       
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->


        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
         <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
         <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
         <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
         <script type="text/javascript">
               $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
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
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            *{font:11px verdana #000}
            a:hover {text-decoration:underline}
        </style>
    </head>

    <%      String allUsers = "";
            String allUserNames = "";

        String accountRoleQ = "select * from prg_org_master  order by  org_end_date desc";
        //////////////////////////////////////////////////////////////////////////////////////////.println.println(" userRoleQ " + userRoleQ);
        PbReturnObject userAccount = pbdb.execSelectSQL(accountRoleQ);
        ArrayList orgIdarr = new ArrayList();
        ArrayList orgNamearr = new ArrayList();
        String roleIdsList[] = userFolderId.split(",");
        for (int i = 0; i < userAccount.getRowCount(); i++) {
            String busRoles = userAccount.getFieldValueString(i, 5);
            String comproleIdsList[] = busRoles.split(",");
            int count = 0;
            for (int j = 0; j < roleIdsList.length; j++) {
                for (int j1 = 0; j1 < comproleIdsList.length; j1++) {
                    if (roleIdsList[j].equalsIgnoreCase(comproleIdsList[j1])) {
                        count++;
                    }
                }
            }
            if (count >= roleIdsList.length) {
                orgIdarr.add(userAccount.getFieldValueString(i, "ORG_ID"));
                orgNamearr.add(userAccount.getFieldValueString(i, "ORGANISATION_NAME"));
                 allUsers += ",u~" + userAccount.getFieldValueString(i, "ORG_ID");
                allUserNames += "," + userAccount.getFieldValueString(i, "ORGANISATION_NAME");
            }
        }

            String getUserList ="SELECT ORG_ID, ORGANISATION_NAME, BUSS_ROLE FROM PRG_ORG_MASTER where org_end_date>=sysdate";//where BUSS_ROLE in("+userFolderId+") and org_end_date>=sysdate";

            PbReturnObject pbrousers = pbdb.execSelectSQL(getUserList);

         /*    String roleIdsList[]=userFolderId.split(",");
            for (int i = 0; i < pbrousers.getRowCount(); i++) {
                String busRoles=pbrousers.getFieldValueString(i, 2);
                String comproleIdsList[]=busRoles.split(",");
                int count=0;
                for(int j=0;j<roleIdsList.length;j++){
                  for(int j1=0;j1<comproleIdsList.length;j1++){
                        if(roleIdsList[j].equalsIgnoreCase(comproleIdsList[j1])){
                         count++;
                        }
                  }
               }
                  if(count>=roleIdsList.length){
                allUsers += ",u~" + pbrousers.getFieldValueInt(i, 0);
                allUserNames += "," + pbrousers.getFieldValueString(i, 1);
            }
            }
            if (pbrousers.getRowCount() > 0) {
                allUsers = allUsers.substring(1);
                allUserNames = allUserNames.substring(1);
            }*/
    %>


    <body>
        <table style="width:100%">
                <tr>
                    <td valign="top" style="width:100%;">
                        <jsp:include page="Headerfolder/headerPage.jsp"/>
                    </td>
                </tr>
            </table>
        <form name="myFormDetails" method="post" action="">
            
            <table style="width:100%">
                <tr>
                    <td  style="height:10px;width:20%" align="right">
<!--                        <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |-->
<!--                        <a href="#" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |-->
<!--                        <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>-->
                    </td>
                </tr>
            </table>
            <div class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="width:99%;min-height:500px;max-height:100%">
                <table style="width:100%">
                    <tr>
                        <td>
                            <div style="height:20px" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
                                Comapny List (Drag Companies to Right and click Save)
                            </div>
                        </td>
                    </tr>
                </table>
                <br/>
                <table align="center" border="0" width="50%"  valign="top" style="background-color:#b4d9ee">
                    <tr>
                        <td width="50%" align="center">
                            <a href="javascript:moveAll1('<%=allUsers%>','<%=allUserNames%>')" style="font-family:verdana;font-size:10px;text-decoration:none;color:black"> MoveAll</a>
                        </td>
                        <td width="50%" align="center">
                            <a href="javascript:deleteAll1()" style="font-family:verdana;font-size:10px;text-decoration:none;color:black"> DeleteAll</a>
                        </td>
                    </tr>
                </table>
                <center>
                    <div style="height:280px;width:50%;overflow-y:auto">
                        <table align="center" border="1" width="100%" style="height:280px" valign="top">
                            <tr>
                                <td valign="top">
                                    <div class="draggedTable1" style="height:270px;overflow:auto">
                                        <table  border="0" style="line-height:18px">
                                            <%

                   // if (pbrousers.getRowCount() > 0) {
                      for (int p = 0; p < orgIdarr.size(); p++) {
                       String orgId = String.valueOf(orgIdarr.get(p));
                       String orgName = String.valueOf(orgNamearr.get(p));
                                            %>
                                            <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                                            <tr>
                                                <td class="myDragTabs1" class="navtitle-hover" style="width:200px" id="u~<%=orgId%>"><font style="font-family:verdana;font-size:12px;color:#369;"> <%=orgName%></font></td></tr>
                                                <%
                        }
                 //   }
                                                %>
                                        </table>
                                    </div>
                                </td>
                                <td id="dropTabs1" style="width:50%" valign="top">
                                    <ul id="sortable1">

                                    </ul>
                                </td>
                            </tr>

                        </table>
                    </div>
                </center>
                <br/>
                <center>
                    <input type="button"  class="navtitle-hover" style="width:auto" value="Save" onclick="savereportDetails('<%=reportId%>','<%=reportType%>','<%=userId%>')">
                    <input type="button"  class="navtitle-hover" style="width:auto" value="Back" onclick="cancelreportDetails('<%=sourcePage%>')">
                </center>
            </div>

        </form>
        <br/>
        <table width="100%" class="fontsty">
            <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                <td style="height:10px;width:100%;background-color:#bdbdbd" >
                    <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                </td>
            </tr>
        </table>

  <script>
            var y='';
            var msrArray=new Array();
            var msrArray1=new Array();
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

                $(".myDragTabs1").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                $("#dropTabs1").droppable(
                {
                    activeClass:"blueBorder",
                    accept:'.myDragTabs1',

                    drop: function(ev, ui) {


                        createColumn1(ui.draggable.attr('id'),ui.draggable.html());

                    }

                }
            );


            });
            function savereportDetails(reportId,reportType,userId,userFolderId,grpIds){
                dispReports(reportId,reportType,userId,userFolderId,grpIds);
            }
            function createColumn(elmntId,elementName){

                var parentUL=document.getElementById("sortable");
                // alert(elmntId+'   '+elementName);
                var x=msrArray.toString();
                if(x.match(elmntId)==null){
                    msrArray.push(elmntId)

                    var childLI=document.createElement("li");
                    var uid=elmntId.split("~");
                    childLI.id=uid[1];
                    childLI.style.width='180px';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id=elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    // cell1.style.backgroundColor="#e6e6e6";

                    var a=document.createElement("a");
                    a.href="javascript:deleteColumn('"+uid[1]+"')";
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
                    //alert('This Dashboard is already added')
                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }

            function createColumn1(elmntId,elementName){

                var parentUL=document.getElementById("sortable1");
                // alert(elmntId+'   '+elementName);
                var x=msrArray1.toString();
                if(x.match(elmntId)==null){
                    msrArray1.push(elmntId)

                    var childLI=document.createElement("li");
                    var uid=elmntId.split("~");
                    childLI.id=uid[1];
                    childLI.style.width='180px';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id=elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    // cell1.style.backgroundColor="#e6e6e6";

                    var a=document.createElement("a");
                    a.href="javascript:deleteColumn1('"+uid[1]+"')";
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
                    // alert('This User is already added')
                }
                $("#sortable1").sortable();
                $("#sortable1").disableSelection();
            }

            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                //alert('LiOBJ '+LiObj);
                var parentUL=document.getElementById("sortable");
                //alert('parentUL '+index)

                parentUL.removeChild(LiObj);
                index='r~'+index;
                var l=msrArray.indexOf(index);
                msrArray.splice(l,1);


            }
            function deleteColumn1(index){
                var LiObj=document.getElementById(index);
                //alert('LiOBJ '+LiObj);
                var parentUL=document.getElementById("sortable1");
                //alert('parentUL '+index)

                parentUL.removeChild(LiObj);
                index='u~'+index;
                var l=msrArray1.indexOf(index);
                msrArray1.splice(l,1);

            }

            function cancelreportDetails(sourcepage){
                //parent.cancelreportDetails();
                if(sourcepage=="Designer"){
               document.forms.myFormDetails.action="reportViewer.do?reportBy=backToReportDesigner&REPORTID=<%=reportId%>";

                document.forms.myFormDetails.submit();
                 }
                else{
                    document.forms.myFormDetails.action="reportViewer.do?reportBy=viewReport&REPORTID=<%=oldReportId%>";

                    document.forms.myFormDetails.submit();


                }
            }
          
                

            function dispReports(reportId,reportType,userId){
                var reports="";
                var useraccs="";
                var reportsUl=document.getElementById("sortable");
                var usersUl=document.getElementById("sortable1");

                if(usersUl!=null && usersUl!=undefined){
                    var usersIds=usersUl.getElementsByTagName("li");
                    for(var i=0;i<usersIds.length;i++){
                        useraccs=useraccs+","+usersIds[i].id;
                    }
                   if(useraccs!=""){
                       if(useraccs.charAt(0)==','){
                    useraccs=useraccs.substr(1);
                       }
                   }
                }
               // alert(reportId+"==userId="+userId+"companies=="+useraccs)
                document.forms.myFormDetails.action='reportTemplateAction.do?templateParam=addReportForCompany&reportId='+reportId+'&companies='+useraccs+'&userId='+userId+'&reportType='+reportType+"&oldReportId='"+<%=oldReportId%>+"'";;
                document.forms.myFormDetails.submit();
            }

            function moveAll(allReports,allNames){

                var allList=allReports.split(",");
                var allNamesList=allNames.split(",");
                for(var i=0;i<allList.length;i++){

                    createColumn(allList[i],allNamesList[i]);
                }


            }
            function deleteAll1(){
                msrArray1.splice(0,msrArray1.length);
                var parentUL=document.getElementById("sortable1");
                parentUL.innerHTML='';
                 }
            function deleteAll(){
                msrArray.splice(0,msrArray.length);
                var parentUL=document.getElementById("sortable");
                parentUL.innerHTML='';
            }
            function moveAll1(allUsers,allNames){
                var allList=allUsers.split(",");
                var allNamesList=allNames.split(",");
                for(var i=0;i<allList.length;i++){
                createColumn1(allList[i],allNamesList[i]);
                }

            }
//            function logout(){
//                document.forms.myFormDetails.action="baseAction.do?param=logoutApplication";
//                document.forms.myFormDetails.submit();
//            }
//            function gohome(){
//                document.forms.myFormDetails.action="baseAction.do?param=goHome";
//                document.forms.myFormDetails.submit();
//            }


        </script>
    </body>
</html>
