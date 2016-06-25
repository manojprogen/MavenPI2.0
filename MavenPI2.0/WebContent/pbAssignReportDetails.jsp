

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.util.screenDimensions,java.sql.PreparedStatement,java.sql.ResultSet,java.sql.Connection,prg.business.group.BusinessGroupDAO,prg.db.PbDb,prg.db.PbReturnObject,java.util.*,utils.db.ProgenConnection" %>
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
            PbDb pbdb = new PbDb();
            String reportId = String.valueOf(request.getAttribute("REPORTID"));
            String reportType = String.valueOf(request.getAttribute("ReportType"));
            String userId = String.valueOf(session.getAttribute("USERID"));
            String userFolderId = String.valueOf(request.getAttribute("UserFolderIds"));
            String sourcePage=String.valueOf(request.getAttribute("SourcePage"));
            String reportName=String.valueOf(request.getAttribute("ReportName"));
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
            String ContxtPath=request.getContextPath();
            
            ////.println("isWhatIfReport in pbAssignReportDetails is:: " + isWhatIfReport);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=ContxtPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ContxtPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="<%=ContxtPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=ContxtPath%>/stylesheets/treeviewstyle/screen.css" />
          <link type="text/css" href="<%=ContxtPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
       <link type="text/css" href="<%=ContxtPath%>/stylesheets/demos.css" rel="stylesheet" />
           <link type="text/css" href="<%=ContxtPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        
        
        <script src="<%=ContxtPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=ContxtPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ContxtPath%>/javascript/treeview/demo.js"></script>
     <!--   <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
    
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
 
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->


<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
      
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

            String getExistUserList = "SELECT distinct PU_ID,PU_LOGIN_ID FROM PRG_AR_USERS where PU_ID in (SELECT distinct USER_ID FROM PRG_AR_USER_REPORTS where REPORT_ID=" + reportId + ") and PU_ID!=" + userId;

            String chks = "";
            PbReturnObject pbroExistusers = pbdb.execSelectSQL(getExistUserList);
            for (int i = 0; i < pbroExistusers.getRowCount(); i++) {
                chks += "*u~" + pbroExistusers.getFieldValueInt(i, 0);
            }
            ////.println("chks=="+chks);
    %>


    <body onload="checkExist('<%=chks%>')">
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
                     <td style="height:10px;width:10%" >
                         <span id="reportName"  style="color: #4F4F4F;font-family:verdana;font-size:12px;font-weight:bold;text-decoration:none"   title="<%=reportName%>"><%=reportName%></span>
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
                                User List (Drag Users to Right and click Save)
                            </div>
                        </td>
                    </tr>
                </table>
                <br/>
                <table align="center" border="0" width="50%"  valign="top" style="background-color:#b4d9ee;margin-left: auto;margin-right: auto;">
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
                    <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                    <div style="height:280px;width:50%;overflow-y:auto">
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
                                                <td class="myDragTabs1" class="navtitle-hover" style="width:200px" id="u~<%=pbrousers.getFieldValueString(i, 0)%>"><font style="font-family:verdana;color:#369;"> <%=pbrousers.getFieldValueString(i, 1)%></font></td></tr>
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
                                                        <font style="font-family: verdana; font-size: 12px; color: rgb(51, 102, 153);"> <%=pbroExistusers.getFieldValueString(i, 1)%></font>
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
                    <input type="button"  class="navtitle-hover" style="width:auto" value="Save" onclick="savereportDetails('<%=reportId%>','<%=reportType%>','<%=userId%>','<%=folderarr%>','<%=grparr%>')">
<!--                    <input type="button"  class="navtitle-hover" style="width:auto" value="Back" onclick="cancelreportDetails('<%=sourcePage%>')">-->
                </center>
            </div>
            <%--<table style="height:600px;width:100%;max-height:100%"  cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="3" valign="top">
                        <center>
                            <div class="ui-corner-all"  style="height:auto;width:99%;border:1px solid silver" align="center">
                                <center>

                                                                <% if (reportType.equalsIgnoreCase("R")) {%>
                                                                <b>Dashboard List (Drag Dashboards to Right and click Save)</b>
                                                                <%} else {%>
                                                                <b>Report List (Drag Reportss to Right and click Save)</b>
                                                                <%}%>
                                                                <table align="center" border="0" width="50%"  valign="top">
                                                                    <tr>
                                                                        <td>
                                                                            <center>
                                                                                <a href="javascript:moveAll('<%=allReports%>','<%=allReportNames%>')" style="font-family:verdana;font-size:10px;text-decoration:none"> MoveAll</a>
                                                                                &nbsp;&nbsp;
                                                                            <a href="javascript:deleteAll()" style="font-family:verdana;font-size:10px;text-decoration:none"> DeleteAll</a></center>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                                <div style="height:200Px;width:50%;overflow-y:auto">
                                                                    <table align="center" border="1" width="100%" style="height:200px;" valign="top">
                                                                        <tr>
                                                                            <td  class="draggedTable1" valign="top" style="">
                                                                                <table  border="0">
                                                                                    <%

        if (pbroreportnames.getRowCount() > 0) {
            for (int i = 0; i < pbroreportnames.getRowCount(); i++) {

                                                %>
                                                <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                                                <tr><td class="myDragTabs" class="navtitle-hover" style="width:200px" id="r~<%=pbroreportnames.getFieldValueString(i, 0)%>"><font style="font-family:verdana;font-size:12px;color:#369;"> <%=pbroreportnames.getFieldValueString(i, 1)%></font></td></tr>
                                                <%
            }
        }
                                                %>
                                            </table >

                                        </td>
                                        <td id="dropTabs" style="width:50%;" class="myhead" valign="top">
                                            <ul id="sortable">
                                            </ul>
                                        </td>
                                    </tr>
                                </table>
                            </div>

                                    <b>User List (Drag Users to Right and click Save)</b>
                                    <table align="center" border="0" width="50%"  valign="top"><tr><td>
                                                <center>   <a href="javascript:moveAll1('<%=allUsers%>','<%=allUserNames%>')" style="font-family:verdana;font-size:10px;text-decoration:none"> MoveAll</a>
                                                    &nbsp;&nbsp;
                                                    <a href="javascript:deleteAll1()" style="font-family:verdana;font-size:10px;text-decoration:none"> DeleteAll</a></center>
                                            </td></tr>
                                    </table>
                                    <div style="height:200Px;width:50%;overflow-y:auto">
                                        <table align="center" border="1" width="100%" style="height:200px" valign="top">

                                            <tr>

                                                <td  class="draggedTable1" valign="top">
                                                    <table  border="0">
                                                        <%

            if (pbrousers.getRowCount() > 0) {
                for (int i = 0; i < pbrousers.getRowCount(); i++) {

                                                        %>
                                                        <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                                                        <tr>
                                                            <td class="myDragTabs1" class="navtitle-hover" style="width:200px" id="u~<%=pbrousers.getFieldValueString(i, 0)%>"><font style="font-family:verdana;font-size:12px;color:#369;"> <%=pbrousers.getFieldValueString(i, 1)%></font></td></tr>
                                                            <%
                }
            }
                                                            %>
                                                    </table>
                                                </td>
                                                <td id="dropTabs1" style="width:50%" class="myhead" valign="top">
                                                    <ul id="sortable1">

                                                    </ul>
                                                </td>
                                            </tr>

                                        </table>
                                    </div>
                                </center>
                                <br>
                                <center>
                                    <input type="button"  class="navtitle-hover" style="width:auto" value="Save" onclick="savereportDetails('<%=reportId%>','<%=reportType%>','<%=userId%>','<%=folderarr%>','<%=grparr%>')">
                                    <input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelreportDetails()">
                                </center>
                                <br>
                            </div>
                        </center>
                    </td>
                </tr>
            </table>--%>
        </form>
        <br/>
        <table style="width:100%">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>

<script type="text/javascript">
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

            function dispReports(reportId,reportType,userId,userFolderId,grpIds){
//                alert("in dispReports in pbAssignReportDetails.jsp")
                var reports="";
                var users="";
                var reportsUl=document.getElementById("sortable");
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
                    document.forms.myFormDetails.action='saveReportAssignDetails.do?reportId='+reportId+'&users='+users+'&reports='+reports+'&reportType='+reportType+'&userFolderId='+userFolderId+'&grpIds='+grpIds+"&isWhatIfReport="+'<%=isWhatIfReport%>'+"&oldReportId="+'<%=oldReportId%>'+"&FavQuery=<%=FavQuery%>";
                    document.forms.myFormDetails.submit();        
            <%}else{%>
                        document.forms.myFormDetails.action='saveReportAssignDetails.do?reportId='+reportId+'&users='+users+'&reports='+reports+'&reportType='+reportType+'&userFolderId='+userFolderId+'&grpIds='+grpIds+"&isWhatIfReport="+'<%=isWhatIfReport%>'+"&oldReportId="+'<%=oldReportId%>'+"&FavQuery=null";
                        document.forms.myFormDetails.submit();    
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
