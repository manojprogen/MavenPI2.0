
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.util.screenDimensions,java.sql.PreparedStatement,java.sql.ResultSet,java.sql.Connection,prg.business.group.BusinessGroupDAO,prg.db.PbDb,prg.db.PbReturnObject,utils.db.ProgenConnection,java.util.*,com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);

            screenDimensions dims =new screenDimensions();
                int pageFont,anchorFont;
                HashMap screenMap  =dims.getFontSize(session,request,response);
                //.println("screenMap --"+screenMap .size());
                if(!String.valueOf(screenMap .get("pageFont")).equalsIgnoreCase("NULL")){
                pageFont=Integer.parseInt(String.valueOf(screenMap .get("pageFont")));
                anchorFont = Integer.parseInt(String.valueOf(screenMap .get("pageFont")))+1;
                //.println("pageFont--"+pageFont+"---anchorFont--"+anchorFont);
                }else{
                pageFont = 11;
                anchorFont = 12;
                //.println("pageFont--"+pageFont+"---anchorFont--"+anchorFont);
                }

                if(session.getAttribute("USERID")==null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")){
response.sendRedirect(request.getContextPath()+"/newpbLogin.jsp");
   }else{
            //added by Mohit Gupta for default locale
            Locale cL=null;
            cL=(Locale)session.getAttribute("UserLocaleFormat");
            //ended By Mohit Gupta
            PbDb pbdb = new PbDb();
           String reportId = request.getParameter("REPORTID");
            String reportType = request.getParameter("ReportType");
            String userId = String.valueOf(session.getAttribute("USERID"));
            String userFolderId = request.getParameter("UserFolderIds");
            String sourcePage=request.getParameter("SourcePage");
            String reportName=request.getParameter("ReportName");
            reportName = reportName.replace("(((", "'");
            
            String oldReportId="";
            if(sourcePage.equalsIgnoreCase("viewer")){
               oldReportId=String.valueOf(request.getAttribute("OLDREPORTID"));
            sourcePage="Viewer";
            }else{

            sourcePage="Designer";
            }

            //added by uday
            String isWhatIfReport = "";
            String FavQuery = null;
            if (request.getAttribute("isWhatIfReport") != null) {
                isWhatIfReport = (String) request.getAttribute("isWhatIfReport");
            }
            if(request.getAttribute("FavQuery")!=null){
                FavQuery =(String)request.getAttribute("FavQuery");
            }
String contextPath=request.getContextPath();
            ////.println("isWhatIfReport in pbAssignReportDetails is:: " + isWhatIfReport);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
          <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
       <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
           <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
           <link type="text/css" href="<%=contextPath%>/css/global.css" rel="stylesheet" />

        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>

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
            a {font-family:Verdana;cursor:pointer;font-size:<%=anchorFont%>px;}
            *{font:<%=pageFont%>px verdana}
        </style>
    </head>

    <%
            String busgrps = "SELECT distinct USER_ID, GRP_ID FROM PRG_GRP_USER_ASSIGNMENT where USER_ID=" + userId;
            PbReturnObject pbrobusgrps = pbdb.execSelectSQL(busgrps);
            //pbdb.execonnCommit();
            String busgrpListstr = "";
            for (int i = 0; i < pbrobusgrps.getRowCount(); i++) {
                busgrpListstr += "," + pbrobusgrps.getFieldValueInt(i, 1);
            }
            if (pbrobusgrps.getRowCount() > 0) {
                busgrpListstr = busgrpListstr.substring(1);
            }

            String reportNames = "";
            if (reportType.equalsIgnoreCase("R")) {
                reportNames = "select  REPORT_ID, REPORT_NAME from PRG_AR_REPORT_MASTER where REPORT_ID in (select distinct REPORT_ID from PRG_AR_REPORT_DETAILS where FOLDER_ID in (" + userFolderId + ") and REPORT_TYPE='D')";
            } else {
                reportNames = "select  REPORT_ID, REPORT_NAME from PRG_AR_REPORT_MASTER where REPORT_ID in (select distinct REPORT_ID from PRG_AR_REPORT_DETAILS where FOLDER_ID in (" + userFolderId + ") and REPORT_TYPE='R')";
            }

            //add this when using for query
            //                              " and REPORT_TYPE='D'";
            PbReturnObject pbroreportnames = pbdb.execSelectSQL(reportNames);
            pbroreportnames.writeString();
            String allReports = "";
            String allReportNames = "";
            for (int i = 0; i < pbroreportnames.getRowCount(); i++) {
                allReports += ",r~" + pbroreportnames.getFieldValueInt(i, 0);
                allReportNames += "," + pbroreportnames.getFieldValueString(i, 1);
            }
            if (pbroreportnames.getRowCount() > 0) {
                allReports = allReports.substring(1);
                allReportNames = allReportNames.substring(1);
            }
            String getUserList1 = "SELECT GRP_ID,folder_id FROM PRG_USER_FOLDER where folder_id in(" + userFolderId + ")";

            PbReturnObject pbrousers1 = pbdb.execSelectSQL(getUserList1);
            String folderarr = "";
            String grparr = "";
            for (int i = 0; i < pbrousers1.getRowCount(); i++) {
                folderarr += "," + pbrousers1.getFieldValueInt(i, 1);
                grparr += "," + pbrousers1.getFieldValueInt(i, 0);
            }
            if (pbrousers1.getRowCount() > 0) {
                folderarr = folderarr.substring(1);
                grparr = grparr.substring(1);
            }
            //String getUserList = "SELECT distinct USER_ID,USER_NAME FROM PRG_GRP_USER_ASSIGNMENT where grp_id in (SELECT GRP_ID FROM PRG_USER_FOLDER where folder_id in(" + userFolderId + ")) and USER_ID!=" + userId;
            String getUserList = "SELECT USER_ID ,b.pu_login_id USER_NAME FROM PRG_GRP_USER_FOLDER_ASSIGNMENT a,PRG_AR_USERS  b where a.user_id = b.PU_ID and USER_FOLDER_ID in(" + userFolderId + ") and USER_ID!=" + userId;

            PbReturnObject pbrousers = pbdb.execSelectSQL(getUserList);
            String allUsers = "";
            String allUserNames = "";
            for (int i = 0; i < pbrousers.getRowCount(); i++) {
                allUsers += ",u~" + pbrousers.getFieldValueInt(i, 0);
                allUserNames += "," + pbrousers.getFieldValueString(i, 1);
            }
            if (pbrousers.getRowCount() > 0) {
                allUsers = allUsers.substring(1);
                allUserNames = allUserNames.substring(1);
            }
             String chks = "";
             String getExistUserList="";
             if (reportType.equalsIgnoreCase("I")) {
               getExistUserList = "SELECT distinct PU_ID,PU_LOGIN_ID FROM PRG_AR_USERS where PU_ID in (SELECT distinct USER_ID FROM PRG_INSIGHT_ASSIGNMENT where INSIGHT_ID="+reportId+") and PU_ID!=" + userId;
           }else{
            getExistUserList = "SELECT distinct PU_ID,PU_LOGIN_ID FROM PRG_AR_USERS where PU_ID in (SELECT distinct USER_ID FROM PRG_AR_USER_REPORTS where REPORT_ID=" + reportId + ") and PU_ID!=" + userId;
              }

            PbReturnObject pbroExistusers = pbdb.execSelectSQL(getExistUserList);
            for (int i = 0; i < pbroExistusers.getRowCount(); i++) {
                chks += "*u~" + pbroExistusers.getFieldValueInt(i, 0);
            }
            ////.println("chks=="+chks);
    %>


    <body onload="checkExist('<%=chks%>')">
        <form name="adduser" id="adduser" method="post" action="">

            <table style="width:100%">
                <tr>
                    <td  style="height:10px;width:20%" align="right">
                    </td>
                </tr>
            </table>

                <table align="center" border="0" width="90%"  valign="top" style="background-color:#b4d9ee;margin-left: auto;margin-right: auto;">
                    <tr>
                        <td width="50%" align="center">
                            <a href="javascript:moveAll1('<%=allUsers%>','<%=allUserNames%>')" class="gFontFamily gFontSize12" style="text-decoration:none;color:black"><%=TranslaterHelper.getTranslatedInLocale("MoveAll", cL)%> </a>
                        </td>
                        <td width="50%" align="center">
                            <a href="javascript:deleteAll1()" class="gFontFamily gFontSize12" style="text-decoration:none;color:black"><%=TranslaterHelper.getTranslatedInLocale("DeleteAll", cL)%> </a>
                        </td>
                    </tr>
                </table>
                <center>
                    <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                    <div style="height:280px;width:90%;overflow-y:auto">
                        <table  border="1" width="100%" style="height:280px" valign="top" align="left">
                            <tr>
                                <td valign="top" align="left">
                                    <div class="draggedTable1" style="height:270px;overflow:auto">
                                        <table  border="0" style="line-height:18px">
                                            <%

            if (pbrousers.getRowCount() > 0) {
                for (int i = 0; i < pbrousers.getRowCount(); i++) {

                                            %>
                                            <tr>
                                                <td class="myDragTabs1" class="navtitle-hover" style="width:200px" id="u~<%=pbrousers.getFieldValueString(i, 0)%>"><font class="gFontFamily gFontSize12" style="color:#369;"> <%=pbrousers.getFieldValueString(i, 1)%></font></td></tr>
                                                <%
                }
            }
                                                %>
                                        </table>
                                    </div>
                                </td>
                                <td id="dropTabs1" style="width:50%"  valign="top">
                                    <ul id="sortable1">
                                        <%if (pbroExistusers.getRowCount() > 0 && pbroExistusers.getRowCount() > 0) {
                for (int i = 0; i < pbroExistusers.getRowCount(); i++) {

                                        %>
                                        <li class="navtitle-hover" style="width:180px;color:white" id="<%=pbroExistusers.getFieldValueString(i, 0)%>">
                                            <table id="u~<%=pbroExistusers.getFieldValueString(i, 0)%>">
                                                <tr><td >
                                                        <a class="ui-icon ui-icon-close" href="javascript:deleteColumn1('<%=pbroExistusers.getFieldValueString(i, 0)%>')"></a>
                                                    </td><td >
                                                        <font class="gFontFamily gFontSize12" style="color: rgb(51, 102, 153);"> <%=pbroExistusers.getFieldValueString(i, 1)%></font>
                                                    </td>
                                                </tr>
                                            </table>
                                        </li>
                                        <%}

            }
                                        %>
                                    </ul>
                                </td>
                            </tr>

                        </table>
                    </div>
                </center>
                <br/>
                <center>
                    <input type="button"  class="gFontFamily gFontSize12 navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("save", cL)%>" onclick="savereportDetailsUser('<%=reportId%>','<%=reportType%>','<%=userId%>','<%=folderarr%>','<%=grparr%>')">
                </center>
</form>
        <br/>

 <script type="text/javascript">
            var pageid= parent.isDashboardOrReport;
            var y='';
            var msrArray=new Array();
            var msrArray1=new Array();
            var xmlHttp;
          

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
                });

                $(".myDragTabs1").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#dropTabs1").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs1',
                    drop: function(ev, ui) {
                        createColumn1(ui.draggable.attr('id'),ui.draggable.html());
                    }
                });
            });
            function savereportDetails(reportId,reportType,userId,userFolderId,grpIds){
//           alert("in savereportDetails in pbAssignReportDetails.jsp")
           // alert(reportId);
           // alert(userId);
           // alert(userFolderId);
           // alert(grpIds);
                dispReports(reportId,reportType,userId,userFolderId,grpIds);
            }
            function createColumn(elmntId,elementName){

                var parentUL=document.getElementById("sortable");

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

                }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }

            function createColumn1(elmntId,elementName){

                var parentUL=document.getElementById("sortable1");

                var x=msrArray1.toString();
                if(msrArray1.indexOf(elmntId)<0){
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
                  alert("Already Assigned")
                }
                $("#sortable1").sortable();
                $("#sortable1").disableSelection();
            }

            function deleteColumn(index){
                var LiObj=document.getElementById(index);

                var parentUL=document.getElementById("sortable");


                parentUL.removeChild(LiObj);
                index='r~'+index;
                var l=msrArray.indexOf(index);
                msrArray.splice(l,1);


            }
            function deleteColumn1(index){
                var LiObj=document.getElementById(index);

                var parentUL=document.getElementById("sortable1");


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
                }else{
                    document.forms.myFormDetails.action="reportViewer.do?reportBy=viewReport&REPORTID=<%=oldReportId%>";

                    document.forms.myFormDetails.submit();


                }
            }
 function savereportDetailsUser(reportId,reportType,userId,userFolderId,grpIds){

                dispReportsUser(reportId,reportType,userId,userFolderId,grpIds);
            }
            function dispReportsUser(reportId,reportType,userId,userFolderId,grpIds){
                var reports="";
                var users="";

                    var useradd="aaaa";
                var reportsUl=document.getElementById("sortable");
               // alert(reportsUl)
                var usersUl=document.getElementById("sortable1");
                if(reportsUl!=null && reportsUl!=undefined){
                    var reportsIds=reportsUl.getElementsByTagName("li");
                    for(var i=0;i<reportsIds.length;i++){
                        reports=reports+","+reportsIds[i].id;
                    }
                    reports=reports.substr(1);
                }
                if(usersUl!=null && usersUl!=undefined){
                    var usersIds=usersUl.getElementsByTagName("li");
                    for(var i=0;i<usersIds.length;i++){
                        users=users+","+usersIds[i].id;
                    }
                    users=users+","+userId;
                    users=users.substr(1);
                }
            <%if(request.getAttribute("FavQuery")!=null){%>
                     $.post(
                   'saveReportAssignDetails.do?reportId='+reportId+'&users='+users+'&useradd='+useradd+'&reports='+reports+'&reportType='+reportType+'&userFolderId='+userFolderId+'&grpIds='+grpIds+"&isWhatIfReport="+'<%=isWhatIfReport%>'+"&oldReportId="+'<%=oldReportId%>'+"&FavQuery=<%=FavQuery%>",$("#adduser").serialize(),
             function(data){parent.$('#divassign').dialog('close');
         
                 alert("Report Assigned to users successfully done");
                 
        
             });
            <%}else{%>
                       $.post(
                'saveReportAssignDetails.do?reportId='+reportId+'&users='+users+'&useradd='+useradd+'&reports='+reports+'&reportType='+reportType+'&userFolderId='+userFolderId+'&grpIds='+grpIds+"&isWhatIfReport="+'<%=isWhatIfReport%>'+"&oldReportId="+'<%=oldReportId%>'+"&FavQuery=null",$("#adduser").serialize(),
                  function(data){parent.$('#divassign').dialog('close');
              var pageid= parent.isDashboardOrReport;
              //alert(pageid)
              if(pageid=='report'){
                  alert("Report Assigned to users successfully done");
              }else if(pageid=='dashboard'){
                alert("Dashboard Assigned to users successfully done");   
              }
               });
          <% } %>
            }

            function moveAll(allReports,allNames){

//                alert("allReports"+allReports)
//                alert("allNames"+allNames)
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

            function checkExist(chk){

                var chkarr=chk.split("*");


                for(var i=0;i<chkarr.length;i++){

                    msrArray1.push(chkarr[i]);
                }
            }


        </script>
    </body>
</html>
<%}%>