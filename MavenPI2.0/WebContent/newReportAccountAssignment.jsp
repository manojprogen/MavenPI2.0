
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
        String themeColor="blue";
        if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
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
        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>


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


                $("#dropTabs").droppable(
                {
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',

                    drop: function(ev, ui) {

                        //alert(ui.draggable.attr('id')+'=-==-=-')
                        createColumn(ui.draggable.attr('id'),ui.draggable.html());

                    }

                }
            );

            });

            function saveUsersFolders(userId)
            {

                dispUsers(userId);
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
                var reportLI=parentUL.getElementsByTagName("li");
                msrArray=new Array();
                for(var i=0;i<reportLI.length;i++){
                    var reportId=reportLI[i].id.split("~")[0];
                    msrArray.push(reportId);
                }
                var x=msrArray.toString();
                var elmntRepId=elmntId.split(",")[1].split("~")[0];
                if(x.match(elmntRepId)==null){
                    msrArray.push(elmntId);
                    var childLI=document.createElement("li");
                    var uid=elmntId.split(",");
                    childLI.id=uid[1];
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='transparent';
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

                //   alert('else')
                for(var i=0;i<chkarr.length;i++){
                    // alert(chkarr[i])
                    msrArray.push(chkarr[i]);
                }
            }

            function dispUsers(orgId){
                var usersFolders="";
                var repIds="";
                var usersFoldersUl=document.getElementById("sortable");
                var userFoldersIds=usersFoldersUl.getElementsByTagName("li");
               // alert('userFoldersIds----'+userFoldersIds)

                for(var i=0;i<userFoldersIds.length;i++)
                {

                    var v="";
                    var fVal=userFoldersIds[i].id;
                   // alert(userFoldersIds[i].id)
                   // alert(fVal+' fVal ')
                    var f= fVal.split("~");
                    for(var t=0;t<f.length-1;t++)
                        {
                            if(t==0)
                               v=f[0];
                        }
                    repIds=repIds+","+v;
                    //usersFolders=usersFolders+","+userFoldersIds[i].id.split(",")[0];
                   // repIds=repIds+","+userFoldersIds[i].id.split(",")[1];
                }
                //alert('repIds  '+repIds);
               // if(userFoldersIds.length!=0){

                   // usersFolders=usersFolders.substr(1);
                   // var st=confirm('It will assign all the selected reports to all the users of this company');
                   // if(st==true)
                      //  {
                        $.ajax({
                            url: 'organisationDetails.do?param=accountAssignReports&orgId='+orgId+'&repIds='+repIds,
                            success: function(data) {
                                //alert(data)
                                if(data==1){
                                    //alert('ds')
                                    cancelAccountReports();
                                }
                            }
                        });
                  //  }



                //}

            }
           function cancelAccountReports()
           {
               parent.cancelAccountReportsAssign();
           }

            function checkExist1(chk){
                //alert('all')
                var chkarr=chk.split("*");

                // var grpIdsall=document.getElementById("grpIdsall").value;
                //  var folderIdsall=document.getElementById("folderIdsall").value;
               // alert(grpIdsall+'-----'+folderIdsall)
                $.ajax({
                    url: 'saveUserFolderAssignmentAll.do?grpId='+grpIdsall+'&userId='+userId+'&userFolderIds='+folderIdsall,
                    success: function(data) {

                    }
                });

                for(var i=0;i<chkarr.length;i++){

                  //  alert(chkarr[i])
                    msrArray.push(chkarr[i]);

                }

            }

            function moveAll(allFolders,allNames){

                /// alert('moveall'+allReports)
                // alert('names--'+allNames)
                var allList=allFolders.split("*");
                var allNamesList=allNames.split("*");

                for(var i=0;i<allList.length;i++){
                    // alert(allList[i]+'------------'+allNamesList[i])
                    createColumn(allList[i],"<font style='color:black;font-family:verdana'>"+allNamesList[i]+"</font>");
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
            *{font:11px verdana}
        </style>
    </head>
 <body>
    <%
        PbDb pbdb = new PbDb();
        String orgId = request.getParameter("orgId");
        ////////////////////////////.println.println(" userId "+orgId);
         String sql="  select buss_role from prg_org_master where org_id in('"+orgId+"')";
          PbReturnObject pbrorole = pbdb.execSelectSQL(sql);
           if(pbrorole.getRowCount()>0){
               String roleIdsList=pbrorole.getFieldValueString(0,0);
        String getrepssql = "select pd.report_id, pm.report_name from prg_ar_report_master pm, prg_ar_report_details pd where" +
        " pd.report_id= pm.report_id and pd.folder_id in("+roleIdsList+") order by pm.report_name";


       ////.println(" getrepssql "+getrepssql);

        PbReturnObject pbroreports = pbdb.execSelectSQL(getrepssql);

       // pbrousers.writeString();
        String getExistUserRepList = "select ar.report_id,pp.report_name,pm.organisation_name from account_report ar,prg_org_master pm,prg_ar_report_master pp where ar.org_id="+orgId+" and pm.org_id=ar.org_id and pp.report_id= ar.report_id order by pp.report_name";
        ////////////////////////////.println.println(" getExistUserRepList "+getExistUserRepList);
        PbReturnObject pbro1 = pbdb.execSelectSQL(getExistUserRepList);
        pbro1.writeString();
        String chks = "";
        for (int i = 0; i < pbroreports.getRowCount(); i++) {
            for (int j = 0; j < pbroreports.getRowCount(); j++) {

                if (pbro1.getFieldValueInt(j, 0) == pbroreports.getFieldValueInt(i, 0)) {
                    chks += "*u," + pbroreports.getFieldValueInt(i, 0) + "~" + pbroreports.getFieldValueString(i, 1);
                }
            }
        }
      /*  if (pbro1.getRowCount() > 0) {
            chks = chks.substring(1);
        } */

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
        ArrayList allRe=new ArrayList();
        for(int m=0;m<pbro1.getRowCount();m++)
            {
               allRe.add(pbro1.getFieldValueString(m,"REPORT_ID"));
            }

    %>


        <br>
        <center><b style="color:#369;font-size:11px;font-family:verdana;">Reports List (Drag reports to Right and click Save)</b></center>
        <br>
        <form name="myFormUser">
            <table align="center" border="0" width="100%" style="height:auto" >
               <tr><td align="left">
                    <a href="javascript:moveAll('<%=allReports%>','<%=allRepNames%>')" style="text-decoration:none;font-family:verdana;"> MoveAll</a>
                         &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;">Available Reports</b>
                </td> <td align="left">
                         <a href="javascript:deleteAll()" style="text-decoration:none;font-family:verdana;"> DeleteAll</a>
                         &nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp; &nbsp;&nbsp; <b style="color:black;font-size:11px;font-family:verdana;"> Assigned Reports</b>
                    </td>
                </tr>



            </table>

            <table align="center" border="1" width="100%" style="height:200px" valign="top">

                <tr>

                    <td  class="draggedTable1" valign="top">
                        <div style="height:250Px;width:100%;overflow-y:auto">
                        <table  border="0">
                            <%

        if (pbroreports.getRowCount() > 0) {
            for (int i = 0; i < pbroreports.getRowCount(); i++) {
                         // if(!allRe.contains(pbroreports.getFieldValueString(i, 0)))
                         //     {
                            %>
                            <input type="hidden" id="h" value="<%=request.getContextPath()%>">
                            <tr><td class="myDragTabs" class="ui-state-default3" style="width:200px" id="u,<%=pbroreports.getFieldValueInt(i, 0)%>~<%=pbroreports.getFieldValueString(i, 1)%>"><font style="color:black;font-family:verdana"><%=pbroreports.getFieldValueString(i, 1)%></font></td></tr>
                            <%
              //}
              }
           }
                            %>
                        </table >
                               </div>
                    </td>
                    <td id="dropTabs" style="width:50%;border:1px solid silver" class="myhead" valign="top">

                         <div style="height:250Px;width:100%;overflow-y:auto">
                        <ul id="sortable">
                            <%for(int i=0;i<pbro1.getRowCount();i++){%>
                             <li class="navtitle-hover" style="width:auto;height:auto;color:white" id="<%=pbro1.getFieldValueInt(i, 0)%>~<%=pbro1.getFieldValueString(i, 1)%>">
                                <table id="u,<%=pbro1.getFieldValueInt(i, 0)%>~<%=pbro1.getFieldValueString(i, 1)%>" >
                                    <tr><td style="backgroundColor:#e6e6e6">
                                            <a class="ui-icon ui-icon-close" href="javascript:deleteColumn('<%=pbro1.getFieldValueInt(i, 0)%>~<%=pbro1.getFieldValueString(i, 1)%>')"></a>
                                        </td><td style="backgroundColor:#e6e6e6">
                                            <font style="color:black"> <%=pbro1.getFieldValueString(i, 1)%></font>
                                        </td>
                                    </tr>
                                </table>
                                <%}%>
                            </li>
                     <%--       <% for (int i = 0; i < pbroreports.getRowCount(); i++) {
            for (int j = 0; j < pbroreports.getRowCount(); j++) {
                if (pbro1.getFieldValueInt(j, 0) == pbroreports.getFieldValueInt(i, 0)) {%>
                            <li class="navtitle-hover" style="width:auto;height:auto;color:white" id="<%=pbroreports.getFieldValueInt(i, 0)%>~<%=pbroreports.getFieldValueString(i, 1)%>">
                                <table id="u,<%=pbroreports.getFieldValueInt(i, 0)%>~<%=pbroreports.getFieldValueString(i, 1)%>" >
                                    <tr><td style="backgroundColor:#e6e6e6">
                                            <a class="ui-icon ui-icon-close" href="javascript:deleteColumn('u,<%=pbroreports.getFieldValueInt(i, 0)%>~<%=pbroreports.getFieldValueString(i, 1)%>')"></a>
                                        </td><td style="backgroundColor:#e6e6e6">
                                            <font style="color:black"> <%=pbroreports.getFieldValueString(i, 1)%></font>
                                        </td>
                                    </tr>
                                </table>
                            </li>
                            <% }
            }
        }%> --%>
                        </ul>
                        </div>
                    </td>
                </tr>

        </table>

        </form>
        <br><center>
            <input type="button" class="navtitle-hover" style="width:auto"  value="Save" onclick="dispUsers('<%=orgId%>')">

            </center>

    <%}else{%>
     Please Assign Business Role Before Assign Reports
<%}%>
    </body>
</html>
