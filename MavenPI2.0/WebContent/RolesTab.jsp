
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,java.util.*,prg.db.Session,prg.db.PbReturnObject,java.sql.*,prg.db.PbReturnObject,prg.db.Session,utils.db.*,com.progen.i18n.TranslaterHelper"%>
<%--<%@page contentType="text/html" pageEncoding="windows-1252"%>--%>
<%
    String folderId = request.getParameter("roleId");
    String fromnewHome = request.getParameter("fromnewHome");
    boolean testFor = false;
    if(fromnewHome!=null && !fromnewHome.equalsIgnoreCase("")){
        testFor = true;
    }

boolean isCompanyValid=false;

    String themeColor="blue";
     if (session.getAttribute("theme") == null) {
    session.setAttribute("theme", themeColor);
} else {
    themeColor = String.valueOf(session.getAttribute("theme"));
}
            //added by Dinanath for default locale
                    Locale currentLocale=null;
                   currentLocale=(Locale)session.getAttribute("UserLocaleFormat");
                   String contextPah=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

      <%if(testFor){%>
      <title>piEE</title>
         <script src="<%=contextPah%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPah%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPah%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
       <script src="<%=contextPah%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
       <script  type="text/javascript" src="<%=contextPah%>/javascript/jquery.contextMenu.js" ></script>
        <link rel="stylesheet" href="<%=contextPah%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPah%>/stylesheets/treeviewstyle/screen.css" />
    <%}else{%>
       <script src="<%=contextPah%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
       <script  type="text/javascript" src="<%=contextPah%>/javascript/jquery.contextMenu.js" ></script>
        <link type="text/css" href="<%=contextPah%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPah%>/stylesheets/themes/<%=themeColor %>/css.css" rel="stylesheet"/>
        <title>piEE</title>
        <link rel="stylesheet" href="<%=contextPah%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPah%>/stylesheets/treeviewstyle/screen.css" />
     <%}%>
       
          <script type="text/javascript">

              var reportIdforsending=new Array();
              var reportNamesforsending=new Array();
              var ListArray=new Array
            $(document).ready(function() {
              $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getRoles',
                success: function(data) {
                    var json=eval('('+data+')');
                    var roleId=json.roleId;
                    var roleName=json.roleName;
                    var roleHtml="";
                     for(var i=0;i<roleId.length;i++)
                     {

                         roleHtml+="<li class='closed' id='" + roleId[i]+ "' onclick=getReportBasedOnRole('"+roleId[i]+"')>";
                         roleHtml+="<span style='font-size:17px;'><i class='fa fa-building-o'></i></span>";
                         roleHtml+="<span style='font-size:12px' title='" + roleName[i] + "'>" + roleName[i] + "</span>";
                         roleHtml+="<ul  id='roleRept_"+roleId[i]+"' >";
                         roleHtml+="</ul></li>";

                     }
                     $('#roleReportsUL').html(roleHtml);
                     $("#roleReportsUL").treeview({
                        animated:"slow"
                        });
                    }
                 });

                 $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getFavReports',
                success: function(data) {//alert(data);
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
                          favReportHtml+="<li style='color: gray;font-size:12px;' class='closed'id='fav_"+json[i].reportId+"'> <table id='table"+json[i].reportId+"'  <tr> <td><span class='fa fa-times-circle' title='click to remove from fav list' onclick=deleteColumn('fav_"+json[i].reportId+"')></span></td>";

                         if(json[i].reportType=='R'){
                            favReportHtml+="<td title='" + json[i].reportName + "'><a  href='reportViewer.do?reportBy=viewReport&fromreport=report&REPORTID="+ json[i].reportId +"&action=open' style='color:#a8a8a8'>" + json[i].reportName + "</a> </td>";
                            ListArray.push(json[i].reportId);
                            }else{
                               // alert("dash")
                            favReportHtml+=" <td title='" + json[i].reportName + "'> <a  href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+ json[i].reportId+"&pagename=" + json[i].reportName  + " 'style='color:#a8a8a8' >" + json[i].reportName +"</a> </td>" ;
                            ListArray.push(json[i].reportId);
                            }
                           favReportHtml+="</tr> </table></li> ";

                     }
                    if(favReportHtml=="")
                         $("#favReportUl").html("<li id='dummyLi' style='color:white'></li>");
                    else
                        $('#favReportUl').html(favReportHtml);
                     $('#favReportUl').sortable();
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
          <div id="RolesTab"  style="width:99%;min-height:125px;max-height:100%;height:auto">
            <table border="1px solid "  align="center" width="100%">
                <tr><td class="bgcolor" style="width:50%">
                        <center>
                            <div class="prgtableheader2" style="width:50%;color:black"><b><%=TranslaterHelper.getTranslatedInLocale("business_role", currentLocale)%></b></div>
                        </center>
                    </td>
                    <td class="bgcolor" style="width:50%" align="center">
                            <div class="prgtableheader2" style="width:50%;color:black">
                                <table width="100%" align="right">
                                    <tr>
                                        <td width="100%" ><b><%=TranslaterHelper.getTranslatedInLocale("favourite_report", currentLocale)%></b></td>
                                        <td>
                                            <table align="right">
                                                <tr>
                                                    <td style="cursor:pointer;padding-left:5px; ">
                                                        <span title="Click here to save the favorite reports" class="fa fa-floppy-o " onclick="saveFavoriteReports()" ></span>
                                                    </td>
                                                    <td align="right"  style="cursor:pointer;padding-left:5px; ">
                                                        <span title="Configure Tabs" class="fa fa-plus " onclick="salesReportFormulaDiv1()"></span>
                                                    </td>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                    </td>
                </tr>
                <tr>
                     <td width="50%" valign="top" class="draggedTable1">
<!--                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Roles Based Reports</font></div>-->
                        <div style="height:400px;overflow-y:auto;overflow-x:hidden;">
                                <ul id="reptList" class="filetree treeview-famfamfam">
                                     <ul id="roleReportsUL">
                                    </ul>
                                </ul>
                        </div>
                    </td>
                    <td id="favReptTd1" width="50%" valign="top">
<!--                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Favorite Reports</font></div>-->
                        <div id="favReptTd" style="height:400px;overflow-y:auto ;overflow-x:hidden;">
                            <ul id="favReportUl" class="droppable" style="width:100%; height:90%">
                            </ul>
                        </div>
                    </td>
                </tr>
               <input type="hidden" value="" id="jsonLength">
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
                    $("#favReportUl").droppable({
                        activeClass:"blueBorder",
                        accept:'.DimensionULClass',

                     drop: function(ev, ui) {
//                        var ulObj=document.getElementById(this.id);
//                        var id=ulObj.id
//                        var privHtml=$("#"+id).html();
//                        alert(ui.draggable.attr('id'))
                        var dragLiId=ui.draggable.attr('id');
                        var ancObj=document.getElementById(dragLiId).getElementsByTagName("a");
//                        alert(json.stringify(ancObj))
                        var reportName=$("#"+dragLiId+" a").html();
//                          alert(reportName)
                        var type=ancObj[0].id;
                        var favReportHtml="";
//                        alert(ListArray)
                          if(jQuery.inArray(dragLiId.replace("fav_",""),ListArray )== -1){
                        favReportHtml+="<li class='closed' style='color: #d1d1d1;' id='"+dragLiId+"'> <table id='table"+dragLiId+"'> <tr> <td><span title='delete the selected report' class='fa fa-times-circle' onclick=deleteColumn('"+dragLiId+"')></span></td>"
                       if(type=='R')
                            favReportHtml+="<td ><a style='color: gray;' href='reportViewer.do?reportBy=viewReport&REPORTID="+ dragLiId +"&action=open' title='"+reportName+ "'>" +reportName+ "</a><td>";
                        else
                            favReportHtml+="<td ><a style='color: gray;' href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+dragLiId+"&pagename=" +reportName  + " 'title='" +reportName+ "'>" + reportName +"</a></td>" ;

                        favReportHtml+="</tr></table></li>";
                        ListArray.push(dragLiId.replace("fav_",""))
                       $('#favReportUl').append(favReportHtml);
                        i++;
                          }else{
                              alert("Already existed report")
                  }
                  }
                })
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

        function getReportBasedOnRole(roleId)
        {
           $.ajax({
                url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getReportBasedOnRole&roleId='+roleId,
                success: function(data) {
                    var json=eval('('+data+')');
                    var reportHtml="";
                     for(var i=0;i<json.length;i++)
                     {//alert(json[i].reportId);
                        reportHtml+="<li class='closed DimensionULClass' id='fav_"+json[i].reportId+"'>";
                        reportHtml+="<i class='fa fa-file-text-o '></i>";
                        if(json[i].reportType=='R')
                            reportHtml+="<a  id='R' href='reportViewer.do?reportBy=viewReport&REPORTID="+ json[i].reportId +"&action=open' style='padding-left: 7px;color:#a8a8a8' title=\""+json[i].reportDesc+"\">" + json[i].reportName + "</a>";
                        else
                            reportHtml+="<a  id='D' href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+ json[i].reportId+"&pagename=" + json[i].reportName  + " 'style='padding-left: 7px;color:gray;' title=\""+json[i].reportDesc+"\">" + json[i].reportName +"</a>" ;
                        reportHtml+="</li>";

                     }

                      $("#roleRept_"+roleId).html(reportHtml);
                       var i=$("#jsonLength").val();
                           initClasses(i);
                }
               
                 });

        }

        function saveFavoriteReports()
        {
            var uLObj=document.getElementById("favReportUl");
            var liObjs=uLObj.getElementsByTagName("li");
            var idsAndSeq=new Array();
            var names=new Array();
            var url="";
            for(var i=0;i<liObjs.length;i++)
            {
//                var tabObj=liObjs[i].getElementsByTagName("table")[0];
//                var tbodyObj=tabObj.getElementsByTagName("tbody")[0];
//                var trObj=tbodyObj.getElementsByTagName("tr")[0];
//                var tdObj=trObj.getElementsByTagName("td")[1];
                var name=$("#favReportUl li table tbody tr td a").html();
//                alert(name)
                if(liObjs[i].id!='dummyLi')
                {//alert(liObjs[i].id).toString())
                    idsAndSeq.push((i)+"~"+(liObjs[i].id).toString());
                    names.push(name);
                }
            }
//            alert(idsAndSeq)
           if(idsAndSeq.length>0)
            {
                for(var i=0;i<idsAndSeq.length;i++)
                {
                 url+="favouriteSelect="+idsAndSeq[i];
//                 url+="&custname"+idsAndSeq[i].split("~")[1]+"="+names[i]
                 if(i!=idsAndSeq.length-1)
                     url+="&"
                }

//                

            
            $.post("<%=request.getContextPath()%>/savePrioritizeLinks.do?userId=<%=session.getAttribute("USERID").toString()%>&"+url, $("#roleFavForm").serialize() ,
                 function(data){
                   if(data=="true"){
                      alert("Favourite links updated successfully")
                   }else{
                       alert("Favourite links are not updated successfully")
                   }
                 });
          }else{
                 $.post("<%=request.getContextPath()%>/savePrioritizeLinks.do?userId=<%=session.getAttribute("USERID").toString()%>&"+url, $("#roleFavForm").serialize() ,
                 function(data){
                 //  if(data=="true"){
                      alert("Favourite links updated successfully else")
//                   }
                 });
          }

        }

            function viewReport(path){
                document.forms.myForm.action=path;
                document.forms.myForm.submit();
            }
              </script>         
    </body>
</html>
