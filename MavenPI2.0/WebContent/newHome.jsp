<%--
    Document   : newHome
    Created on : Apr 24, 2013, 5:42:15 PM
    Author     : Administrator
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.progen.scheduler.ReportDetails"%>
<%@page import="java.util.List"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    boolean isCompanyValid=false;
    String themeColor="blue";
    if(session.getAttribute("theme")==null)
        session.setAttribute("theme",themeColor);
    else
        themeColor=String.valueOf(session.getAttribute("theme"));
    /* boolean isOneViewEnableforUser=false;
     boolean isPortalEnableforUser=false;
     String userType = null;
    ServletContext context = getServletContext();
    HashMap<String, UserStatusHelper> statushelper;
    statushelper = (HashMap) context.getAttribute("helperclass");
    UserStatusHelper helper = new UserStatusHelper();
    if (!statushelper.isEmpty()) {
     helper = statushelper.get(request.getSession(false).getId());
       if (helper != null) {
          isOneViewEnableforUser = helper.getOneView();
          userType = helper.getUserType();
          isPortalEnableforUser=helper.getPortalViewer();
            }
          }*/

       String useid= String.valueOf(session.getAttribute("USERID"));
       ReportTemplateDAO dao=new ReportTemplateDAO();
        List<ReportDetails> favReportDetails=dao.getFavouriteRept(useid);
        ArrayList repid=new ArrayList();
        ArrayList repName=new ArrayList();
        ArrayList reptype=new ArrayList();
        for(ReportDetails reportDetails:favReportDetails){
            if(reportDetails.getReportType().equalsIgnoreCase("R")){
                 repid.add(reportDetails.getReportId());
                 repName.add(reportDetails.getReportName());
                 reptype.add(reportDetails.getReportType());
               }
          }
        session.setAttribute("repId", repid);
        session.setAttribute("repNames", repName);

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi</title>
        <head>

      <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>

    </head>
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
                         roleHtml+="<font style='color: white'>.</font><img alt='' src='<%=request.getContextPath()%>/icons pinvoke/table.png' />";
                         roleHtml+="<span style='font-family:verdana;font-size:12px' title='" + roleName[i] + "'>" + roleName[i] + "</span>";
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
                          favReportHtml+="<li style='color: white' class='closed'id='fav_"+json[i].reportId+"'> <table id='table"+json[i].reportId+"'  <tr> <td><span class='ui-icon ui-icon-circle-close' title='click to remove from fav list' onclick=deleteColumn('fav_"+json[i].reportId+"')></span></td>";

                         if(json[i].reportType=='R'){
                            favReportHtml+="<td title='" + json[i].reportName + "'><a  href='reportViewer.do?reportBy=viewReport&fromreport=report&REPORTID="+ json[i].reportId +"&action=open' style='color:#336699'>" + json[i].reportName + "</a> </td>";
                            ListArray.push(json[i].reportId);
                            }else{
                               // alert("dash")
                            favReportHtml+=" <td title='" + json[i].reportName + "'> <a  href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+ json[i].reportId+"&pagename=" + json[i].reportName  + " 'style='color:#336699' >" + json[i].reportName +"</a> </td>" ;
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
                        var dragLiId=ui.draggable.attr('id');
                        var ancObj=document.getElementById(dragLiId).getElementsByTagName("a");
                        var reportName=$("#"+dragLiId+" a").html();
                        var type=ancObj[0].id;
                        var favReportHtml="";
                          if(jQuery.inArray(dragLiId,ListArray )== -1){
                        favReportHtml+="<li class='closed' style='color: white' id='select"+dragLiId+"'> <table id='table"+dragLiId+"'> <tr> <td><span title='delete the selected report' class='ui-icon ui-icon-circle-close' onclick=deleteColumn('"+dragLiId+"')></span></td>"
                       if(type=='R')
                            favReportHtml+="<td ><a  href='reportViewer.do?reportBy=viewReport&REPORTID="+ dragLiId +"&action=open' title='"+reportName+ "'>" +reportName+ "</a><td>";
                        else
                            favReportHtml+="<td ><a  href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+dragLiId+"&pagename=" +reportName  + " 'title='" +reportName+ "'>" + reportName +"</a></td>" ;

                        favReportHtml+="</tr></table></li>";
                        ListArray.push(dragLiId)
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
                     {
                        reportHtml+="<li class='closed DimensionULClass' id="+json[i].reportId+">";
                        reportHtml+="<img alt='' src='<%=request.getContextPath()%>/icons pinvoke/report.png'>";
                        if(json[i].reportType=='R')
                            reportHtml+="<a  id='R' href='reportViewer.do?reportBy=viewReport&REPORTID="+ json[i].reportId +"&action=open'style='color:#336699' title=\""+json[i].reportDesc+"\">" + json[i].reportName + "</a>";
                        else
                            reportHtml+="<a  id='D' href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+ json[i].reportId+"&pagename=" + json[i].reportName  + " 'style='color:#336699' title=\""+json[i].reportDesc+"\">" + json[i].reportName +"</a>" ;
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
                if(liObjs[i].id!='dummyLi')
                {
                    idsAndSeq.push((i)+"~"+(liObjs[i].id).toString());
                    names.push(name);
                }
            }
           if(idsAndSeq.length>0)
            {
                for(var i=0;i<idsAndSeq.length;i++)
                {
                 url+="favouriteSelect="+idsAndSeq[i];
                 url+="&custname"+idsAndSeq[i].split("~")[1]+"="+names[i]
                 if(i!=idsAndSeq.length-1)
                     url+="&"
                }
                 $.post("<%=request.getContextPath()%>/savePrioritizeLinks.do?userId=<%=session.getAttribute("USERID").toString()%>&"+url, $("#roleFavForm").serialize() ,
                 function(data){
//                   if(data=="true"){
                      alert("Favourite links updated successfully")
//                   }
                 });
          }else{
                               $.post("<%=request.getContextPath()%>/savePrioritizeLinks.do?userId=<%=session.getAttribute("USERID").toString()%>&"+url, $("#roleFavForm").serialize() ,
                 function(data){
                 //  if(data=="true"){
                      alert("Favourite links updated successfully")
//                   }
                 });
          }

        }
        $(document).ready(function(){
           $("#OLAPGraphDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'justify',
                        modal: true,
//                        title:dashletName,
                        resizable:true

//
             });

});

         function goPaths(path){
               parent.closeStart();
               document.forms.hometab.action=path;
               document.forms.hometab.submit();
           }
        function homeSlideRoles(){
            if($("#homeRolesTabId").is(":visible")){
              $("#homeRolesTabId").hide("slide",{direction:'left'},"slow");

            }else
             $("#homeRolesTabId").show("slide",{direction:'left'},"slow");

     }
     function viewReportG(path){
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
             function gotoDBCON1(ctxPath,repid){
               document.forms.hometab.action="<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID="+repid+"&action=open";
               document.forms.hometab.submit();

            }
            function refreshHomePage(){
        var res=confirm("It May Take Some Time Would You Like To Proceed ?");
            if(res){
        document.forms.hometab.action = "<%=request.getContextPath()%>/reportViewer.do?reportBy=refreshHomePage",
        document.forms.hometab.submit();
        }
            }

    </script>
    <body>
                    <table style="width:100%;">
                        <tr>
                            <td valign="top" style="width:50%;">
                                <jsp:include page="Headerfolder/headerPage.jsp"/>
                            </td>
                        </tr>
                    </table>
                            <form action=""  name="hometab" method="post" style="padding:0pt">
       <div id="homeTabRepId" style="width:99%;height:720px;">
           <table><tr><td>
                       <a class="ui-icon ui-icon-triangle-1-e" onclick="homeSlideRoles()" href="#"></a></td>
                   <td>
                       <a class="ui-icon ui-icon-gear" onclick="refreshHomePage()" title="Refresh Home Page" href="#"></a>
                   </td>
               </tr>
           </table>
           <br>
                          <hr color="#2191C0" size="1">
                           <div id="homeRolesTabId" style='display: none;position:absolute;text-align:left;background-color:white ;border:1px solid #000000;direction:ltr;z-index: 1000;float: left;height: auto;width: 50%'>

                               <table border="1px solid "  align="center" width="100%">
                <tr><td class="bgcolor" style="width:50%">
                        <center>
                            <div class="prgtableheader2" style="width:50%;color:black"><b>Business Roles</b></div>
                        </center>
                    </td>
                    <td class="bgcolor" style="width:50%" align="center">
<!--                        <center>-->
                            <div class="prgtableheader2" style="width:50%;color:black"><table width="100%" align="right"><tr><td width="100%" ><b>Favorite Reports</b></td><td><table align="right"><tr><td><span title="Click here to save the favorite reports" class="ui-icon ui-icon-disk" onclick="saveFavoriteReports()" ></span></td> <td align="right"><span title="Configure Tabs" class=" ui-icon ui-icon-plusthick" onclick="salesReportFormulaDiv1()"></span></td></table></td></tr></table></div>
<!--                        </center>-->
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

                               <!--                               <iframe id="homeIframeTest" NAME='homeIframeTest' width="100%" height="100%" frameborder="0" SRC='about:blank'></iframe>-->
                           </div>
                            <iframe id="fileNameTestId" NAME='fileNameTestname' width="100%" height="710px" frameborder="0" SRC='loginPageTest.jsp'></iframe>
                  </div>
                            </form>
                                    <table style="width:100%">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>
                   <input type="hidden" name="ctxPath" id="ctxPath" value="<%=request.getContextPath()%>">
    </body>
</html>