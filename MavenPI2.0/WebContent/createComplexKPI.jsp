<%-- 
    Document   : createComplexKPI
    Created on : 19 Nov, 2011, 2:38:41 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.userlayer.action.GenerateDragAndDrophtml"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
String divId=String.valueOf(request.getAttribute("divId"));
String dashboardId=String.valueOf(request.getAttribute("dashboardId"));
String from=String.valueOf(request.getParameter("from"));
String oneRoleId=String.valueOf(request.getParameter("oneRoleId"));
String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
     <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
<!--        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>-->
<!--        <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>-->
                

    </head>
    <body>
        <form name="createKPIForm" action="">
            <div id="createKPIData">
                <table style="width:100%;height:220px" border="solid black 1px" >
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag KPI from below</font></div>
                        <div class="masterDiv" style="height:200px;overflow-y:auto">
                            <ul id="createKpis" class="filetree treeview-famfamfam">
                             
                            </ul>
                        </div>
                    </td>
                    <td width="50%" valign="top"  id="dropTabs" valign="top">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Drop KPI Here </font>
                        </h3>
                        <div style="height:200px;overflow-y:auto">
                            <ul id="createKPISortable" class="sortable ui-sortable">
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
            </div>
            <table align="center">
                <tr>
                    <% if(from != "null" && from.equalsIgnoreCase("oneview")){ %>
                    <td><input type="button" value="Next" class="navtitle-hover" onclick="displayOneviewComplexKPI()"/></td>
                    <%}else{%>
                    <td><input type="button" value="Next" class="navtitle-hover" onclick="displayCreateKPI()"/></td>
                    <%}%>
                </tr>
            </table>
        </form>
                 <script type="text/javascript" >
                   var grpColArray=new Array();
                     $(document).ready(function() {

                         $.get('<%= request.getContextPath()%>/'+'dashboardTemplateAction.do?templateParam2=getCreateKPIData&from=<%=from%>'+'&oneRoleId='+<%=oneRoleId%>,function(data){                             
//                   alert(data+"****");
                   $("#createKpis").html(data);
                     $("#createKpis").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });
                    $('ul#createKpis li').quicksearch({
                        position: 'before',
                        attached: 'ul#createKpis',
                        loaderText: '',
                        delay: 100
                    })
                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
//                     alert("3")
                    $("#dropTabs").droppable({
                        activeClass:"createKpis > li > span",
                        accept:'.myDragTabs',
                    drop: function(ev, ui) {
                            createColumn(ui.draggable.html(),ui.draggable.attr('id'));
                        }
                    }
                );
                $("#dropTabs").sortable();
                });
//                       alert("1")
                        
// alert("2")

                   
                });
               
                function createColumn(kpiName,elmntId){
    var i=0;
    var parentUL=document.getElementById("createKPISortable");

    var x=grpColArray.toString();
    if(x.match(elmntId)==null){
        grpColArray.push(kpiName+"^"+elmntId)
        var childLI=document.createElement("li");
        childLI.id=kpiName+"^"+elmntId;
        childLI.style.width='180px';
        childLI.style.color='white';
        childLI.className='navtitle-hover';
        var table=document.createElement("table");
        table.id=kpiName+i;
        var row=table.insertRow(0);
        var cell1=row.insertCell(0);
        var a=document.createElement("a");
        a.href="javascript:deleteKpi('"+kpiName+'^'+elmntId+"')";
        a.innerHTML="a";
        a.className="ui-icon ui-icon-close";
        cell1.appendChild(a);
        var cell2=row.insertCell(1);
        cell2.style.color='black';
        cell2.innerHTML=kpiName;
        childLI.appendChild(table);
        parentUL.appendChild(childLI);
        i++;
       
    }
    $("#createKPISortable").sortable();
    $("#createKPISortable").disableSelection();
}

function deleteKpi(kpiName){
    var LiObj=document.getElementById(kpiName);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;

    var parentUL=document.getElementById("createKPISortable");
    parentUL.removeChild(LiObj);
    var x=(LiObj.id);
    var i=0;
    for(i=0;i<grpColArray.length;i++){
        if(grpColArray[i]==x)
            grpColArray.splice(i,1);
    }
}

function displayCreateKPI()
{
    var kpis="";
    var kpisName="";

    var kpiUl=document.getElementById("createKPISortable");
    var kpiIds=kpiUl.getElementsByTagName("li");

    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        kpisName=kpisName+","+kpiIds[i].id;
    }
    $.get('<%= request.getContextPath()%>/'+'dashboardTemplateAction.do?templateParam2=buildCreateKPI&kpis='+kpis+'&divId=<%=divId%>&dashboardId='+<%=dashboardId%>,
    function(data){
         parent.$("#createKPIDiv").dialog('close');
        //var divId=document.getElementById("Dashlets-"+'<%=divId%>');
        parent.$("#Dashlets-"+'<%=divId%>').html("");
        parent.$("#Dashlets-"+'<%=divId%>').html(data);
    });

}
function displayOneviewComplexKPI(){    
    var kpis="";
    var kpisName="";
    var regionId=parent.dashletId;                    
    var regionName=parent.regionName; 
    var regionId=parent.dashletId;  

    var kpiUl=document.getElementById("createKPISortable");
    var kpiIds=kpiUl.getElementsByTagName("li");

    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        kpisName=dkpiIds[0];
    }
    parent.$("#complexDialogId").dialog('close');
    parent.$("#regionId"+parent.colNumber).show();
     parent.document.getElementById(regionId).innerHTML='<center><img id="imgId" src="<%=request.getContextPath()%>/images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.get('<%= request.getContextPath()%>/'+'dashboardTemplateAction.do?templateParam2=buildOneviewComplexKPI&kpis='+kpis+'&divId='+parent.colNumber+'&dashboardId='+<%=dashboardId%>+'&height='+parent.height+'&width='+parent.width+'&oneViewId='+parent.oneViewIdValue,
    function(data){         
        //var divId=document.getElementById("Dashlets-"+'<%=divId%>');
        parent.$(regionId).css({'font-size':'15pt'});
        parent.document.getElementById(regionId).innerHTML ="";
        parent.document.getElementById(regionId).innerHTML = data;        
//        parent.$("#Dashlets-"+'<%=divId%>').html("");
//        parent.$("#Dashlets-"+'<%=divId%>').html(data);
        parent.document.getElementById(regionName).innerHTML=kpisName
    });
}
   </script>
    </body>
</html>
