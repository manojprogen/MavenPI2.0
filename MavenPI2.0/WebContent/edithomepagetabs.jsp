
<%--<%@page contentType="text/html" pageEncoding="windows-1252"%>--%>

<%@page contentType="text/html" pageEncoding="UTF-8" contentType="text/html" import="prg.db.PbDb,java.util.*,prg.db.Session,prg.db.PbReturnObject,java.sql.*,prg.db.PbReturnObject,utils.db.*,prg.db.Session"%>

<%
    String folderId = request.getParameter("roleId");

    boolean isCompanyValid=false;

    String themeColor="blue";
     if (session.getAttribute("theme") == null) {
    session.setAttribute("theme", themeColor);
} else {
    themeColor = String.valueOf(session.getAttribute("theme"));
}
String contextPath=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
       <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
       <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
       <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
       <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet"/>
        <title>piEE</title>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
         	<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>


        <script type="text/javascript">
              var reportIdforsending=new Array();
              var reportNamesforsending=new Array();
              var ListArray=new Array
              var roleId="";
              var roleName="";
               $(document).ready(function() {
                $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getRoles',
                success: function(data) {
                    var json=eval('('+data+')');
                    var roleId=json.roleId;
                    var roleName=json.roleName;
                    var roleHtml="";
                    var i=0;
                    for(var i=0;i<roleId.length;i++)
                     {
                         roleHtml+="<ul id='Role-" + roleId[i]+ "'>";
                         roleHtml+="<li class='closed' id='Role-" + roleId[i]+ "'>";
//                         roleHtml+="<font style='color: white'>.</font><img alt='' src='<%=request.getContextPath()%>/icons pinvoke/table.png' />";
                         roleHtml+="<font style='color: white'>.</font><i class='fa fa-table'></i>";
                         roleHtml+="<span style='font-size:13px;padding-left:5px;' title=''>"+roleName[i]+"</span>";

                         roleHtml+="<ul id='Reports-" + roleId[i]+ "'>";
                         roleHtml+="<li class='closed' id='rep-" + roleId[i]+ "' onclick=getReportBasedOnRoleRep('"+roleId[i]+"')>";
//                         roleHtml+="<font style='color: white'>.</font><img alt='' src='<%=request.getContextPath()%>/icons pinvoke/minus-small-circle.png' />";
                         roleHtml+="<font style='color: white'>.</font><span style='color:gray;'><i   class='fa fa-minus-circle'></i></span>";
                         roleHtml+="<span style='font-size:12px;padding-left:5px;' title=''>Reports</span>";
                         roleHtml+="<ul  id='roleRept"+roleId[i]+"' >";
                         roleHtml+="</ul></li></ul>";

                         roleHtml+="<ul id='Dashboard-" + roleId[i]+ "'>";
                         roleHtml+="<li class='closed' id='dash-" + roleId[i]+ "' onclick=getReportBasedOnRoleDash('"+roleId[i]+"')>";
//                         roleHtml+="<font style='color: white'>.</font><img alt='' src='<%=request.getContextPath()%>/icons pinvoke/minus-small-circle.png' />";
                          roleHtml+="<font style='color: white'>.</font><span style='color:gray;'><i   class='fa fa-minus-circle'></i></span>";
                         roleHtml+="<span style='font-size:12px;padding-left:5px;' title=''>Dashboard</span>";
                         roleHtml+="<ul  id='roleDash"+roleId[i]+"' >";
                         roleHtml+="</ul></li></ul>";
                         roleHtml+="</li></ul>";
                       }
                     $('#roleReportsUL1').html(roleHtml);
                     $("#roleReportsUL1").treeview({
                        animated:"slow"
                        });
                    }
                 });

                 $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getFavReports1',
                success: function(data) {
                    var json=eval('('+data+')');
                    var favReportHtml="";
                    for(var i=0;i<json.length;i++)
                    {
                     reportIdforsending.push(json[i].reportId);
                     reportNamesforsending.push(json[i].reportName);
                    }
                    $("#jsonLength").val(json.length);
                    for(var i=0;i<json.length;i++)
                    {
                          favReportHtml+="<li style='color: white;' class='closed'id='fav_"+json[i].reportId+"'> <table id='table"+json[i].reportId+"'  \n\
                                            <tr> <td><span class='ui-icon ui-icon-circle-close' title='click to remove from fav list' onclick=deleteColumn('fav_"+json[i].reportId+"')></span></td>";

                         if(json[i].reportType=='R'){
                            favReportHtml+="<td><a  href='javascript:void(0)'>" + json[i].reportName + "</a> </td>";
                            ListArray.push(json[i].reportId);
                            }else{
                            favReportHtml+=" <td> <a  href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+ json[i].reportId+"&pagename=" + json[i].reportName  + " '>" + json[i].reportName +"</a> </td>" ;
                            ListArray.push(json[i].reportId);
                            }
                           favReportHtml+="</tr> </table></li> ";

                     }
                    if(favReportHtml=="")
                        {
                         $("#favReportUl1").html("<li id='dummyLi' style='color:white'></li>");
                        }
                    else
                        {
                        $('#favReportUl1').html(favReportHtml);
                        }

                        $('#favReportUl1').sortable();
                }
                 });
        });

              
         </script>
        <style type="text/css">
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .tabsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            .startpage {
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:400px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_start{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                overflow:auto;
            }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }

        </style>

    </head>
    <body>
        <form action=""  id="roleFavForm" >
          <div id="RolesTab"  style="width:99%;min-height:135px;max-height:100%;height:auto">
            <table border="1px solid "  align="center" width="100%">
                <tr><td class="bgcolor" style="width:50%" align="center">
                            <div class="prgtableheader2" style="width:50%;color:black"><b>Business Roles</b></div>
                    </td>
                    <td class="bgcolor" style="width:50%" align="center">
                            <div class="prgtableheader2" style="width:50%;color:black"><b>Favorites</b></div>
                    </td>
                </tr>
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:400px;overflow-y:auto;overflow-x:hidden;">
                            <ul id="reptList" class="filetree treeview-famfamfam">
                                <ul id="roleReportsUL1">

                                </ul>

                            </ul>
                        </div>
                    </td>
                    <td id="favReptTd1" width="50%" valign="top">
                        <div id="favReptTd" style="height:400px;overflow-y:auto ;overflow-x:hidden;">
                             <ul id="favReportUl1" class="droppable" style="width:100%; height:90%">

                             </ul>
                         </div>
                     </td>
                </tr><input type="hidden" value="" id="jsonLength">

            </table><br>
            <table align="center">
                <tr align="center">
                    <td><input type="button" id="EditHomeSaveFavReports" class="navtitle-hover" style="width:auto;height: 30px; background-color: #C8C8C8;" onmouseout="myMouseOutJS(this);" onmouseover="myHoverJS(this);" value="Save" onclick="saveFavoriteReports()"></td>
                </tr>
            </table>
          </div>
        </form>
        <script type="text/javascript">
        function initClasses(i)
              {
                   $(".DimensionULClass").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#favReportUl1").droppable({
                        activeClass:"blueBorder",
                        accept:'.DimensionULClass',

                     drop: function(ev, ui) {
                        var dragLiId=ui.draggable.attr('id');
                        var ancObj=document.getElementById(dragLiId).getElementsByTagName("a");
                        var reportName=$("#"+dragLiId+" a").html();
                        var type=ancObj[0].id;
                        var favReportHtml="";
                        if(jQuery.inArray(dragLiId,ListArray )== -1){

                        favReportHtml+="<li class='closed' style='color: white' id='select"+dragLiId+"'> <table id='table"+dragLiId+"'> <tr> <td><span title='delete the selected report' class='ui-icon ui-icon-circle-close' onclick=deleteColumn('"+dragLiId+"')></span></td>"
                       if(type=='R')
                            favReportHtml+="<td ><a  href='reportViewer.do?reportBy=viewReport&REPORTID="+ dragLiId +"&action=open'>" +reportName+ "</a><td>";
                        else
                            favReportHtml+="<td ><a  href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+dragLiId+"&pagename=" +reportName  + " '>" + reportName +"</a></td>" ;

                            favReportHtml+="</tr></table></li>";
                        ListArray.push(dragLiId)
                       $('#favReportUl1').append(favReportHtml);
                        i++;
                        }else{
                              alert("Already existed report")
                  }
                  }
                });
            }

            function deleteColumn(index)
            {
               ListArray = jQuery.grep(ListArray, function(value) {
                  return value != index;
                });
                var LiObj=null
                    if(document.getElementById("select"+index)!=null)
                     LiObj=document.getElementById("select"+index);
                    else
                        LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);;
            }

         function getReportBasedOnRoleRep(roleId)
         {
           $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getReportBasedOnRole&roleId='+roleId,
                success: function(data) {
                    var json=eval('('+data+')');
                    var reportHtml="";
                     for(var i=0;i<json.length;i++)
                     {
                        if(json[i].reportType=='R'){
                            reportHtml+="<li class='closed DimensionULClass' id="+json[i].reportId+">";
                            reportHtml+="<img alt='' src='<%=request.getContextPath()%>/icons pinvoke/report.png'>";
                            reportHtml+="<a style='color:#a8a8a8;' id='R' href='reportViewer.do?reportBy=viewReport&REPORTID="+ json[i].reportId +"&action=open' title=\""+json[i].reportDesc+"\">" + json[i].reportName + "</a>";
                            reportHtml+="</li>";
                        }
                     }
                      $("#roleRept_"+roleId).html(reportHtml);
                      $("#roleRept"+roleId).html(reportHtml);
                       var i=$("#jsonLength").val();

                           initClasses(i);
                }
                 });

        }
        function getReportBasedOnRoleDash(roleId)
        {
           $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getReportBasedOnRole&roleId='+roleId,
                success: function(data) {
                    var json=eval('('+data+')');
                    var reportHtml="";
                     for(var i=0;i<json.length;i++)
                     {
//
                        if(json[i].reportType=='D'){
                            reportHtml+="<li class='closed DimensionULClass' id="+json[i].reportId+">";
                            reportHtml+="<img alt='' src='<%=request.getContextPath()%>/icons pinvoke/report.png'>";
                            reportHtml+="<a  style='color:#a8a8a8;'  id='D' href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+ json[i].reportId+"&pagename=" + json[i].reportName  + " ' title=\""+json[i].reportDesc+"\">" + json[i].reportName +"</a>" ;
                        reportHtml+="</li>";
                        }
                     }

                      $("#roleDash_"+roleId).html(reportHtml);
                        $("#roleDash"+roleId).html(reportHtml);
                       var i=$("#jsonLength").val();
                           initClasses(i);
                }
                 });

        }

//        function saveFavoriteReports()
//        {
//
//        }
            function saveFavoriteReports()
            {

                var valListUl=document.getElementById("favReportUl1");
                var valListUlIds=valListUl.getElementsByTagName("li");
                var valList=[];
                for(var i=0;i<valListUlIds.length;i++){
                    valList.push(valListUlIds[i].id)
                  }
              $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=saveTabs&tempArray='+valList+'&userid='+<%=session.getAttribute("USERID").toString()%>,
                success: function(data) {
                     parent.refreshpage();


                }
                });
                parent.$("#hometabs").dialog('close');
//                 parent.$("#Report_Studio").hide();
//                        parent.$("#Dashboard_Studio").hide();
//                        parent.$("#All_Reports").hide();
//                        parent.$("#Scorecard").hide();
//                        parent.$("#Html_Reports").hide();
//                        parent.$("#Headlines").hide();
//                        parent.$("#Global_Parameterss").hide();
//               parent.refreshpage();
              }
            function viewReport(path){
                document.forms.myForm.action=path;
                document.forms.myForm.submit();
            }

            function myHoverJS(obj){
                 obj.style.backgroundColor = "#8BC34A";
                   obj.style.color = "white";
            }
            function myMouseOutJS(obj){
                  obj.style.backgroundColor = "#C3C3C3";
                    obj.style.color = "black";
            }
            </script>
    </body>
</html>