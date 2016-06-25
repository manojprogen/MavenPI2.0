

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,utils.db.ProgenConnection,com.progen.reportview.db.PbReportViewerDAO,java.util.HashMap,java.util.ArrayList,com.progen.reportdesigner.db.ReportTemplateDAO,java.sql.*,prg.db.PbReturnObject,prg.db.PbDb" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
        String folderIds = request.getParameter("folderIds");
        String grpType = request.getParameter("grpType");
        String grpId = request.getParameter("gid");
        String divId = request.getParameter("divId");
            String factsHtml = null;
            ArrayList Parameters = new ArrayList();
            ReportTemplateDAO templateDAO = new ReportTemplateDAO();
            String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
     <!--   <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <!--<script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>-->

        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
<!--           <script type="text/javascript" src="<%=contextPath%>/javascript/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
       
        <%
            factsHtml = templateDAO.getMeasures(folderIds, Parameters, request.getContextPath());
        %>
        <script>
            var divId=parent.document.getElementById("divId").value;
            prevGrpCols(divId);
        </script>
        <style type="text/css">
            *{font:11px verdana}
        </style>

    </head>
    <%--<body onload="loadGraphCols('<%=grpId%>')">--%>
    <body>
        <%try {%>

        <table style="width:100%;height:auto" align="center" >
            <tr>
             <td align="center">
                 <input type="radio" id="viewby1" checked name="viewBy" value="DEFAULT">viewByDefault

                <input type="radio"   id="viewby2" name="viewBy" value="TIME">ViewByTime
            </td>
            </tr>
        </table>

        <table style="width:100%;height:220px" border="solid black 1px">
            <form name="myForm3" method="post">
                <input type="hidden" name="grpType" id="grpType" value="<%=grpType%>">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Graph Columns from below</font></div>
                        <div style="height:200px;overflow-y:auto">
                            <ul id="myList3" class="filetree treeview-famfamfam">
                                <ul id="measures">
                                    <%=factsHtml%>

                                                    </ul>
                                            </ul>
                        </div>
                    </td>

                    <td id="dropTabs" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Graph Columns to here</font></div>
                        <div style="height:200px;overflow-y:auto">

                            <ul id="sortable">

                            </ul>
                        </div>

                    </td>
        </tr></form>
        </table>
                                    <br/>
        <center><input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveCols('<%=grpId%>','<%=grpType%>','<%=divId%>')">
            <%--<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="parent.canceldbrdGraph()">--%>
            </center>

        <%} catch (Exception e) {
            e.printStackTrace();
        }%>
        <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
            <li class="view"><a href="#view">View</a></li>
        </ul>
        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">

            <table >
                <tr>
                    <td id="value"></td>
                </tr>
                <table>

                    </div>
         <script>
            var y='';
            var xmlHttp;
            var columnsList = '';
            var columnsList1 = '';
            var columnId = '';
            var columnName = '';
            var graphDetails = '';
            var thisGraphId = '';
            $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });

                 //addeb by bharathi reddy fro search option
                  $('ul#myList3 li').quicksearch({
                    position: 'before',
                    attached: 'ul#myList3',
                    loaderText: '',
                    delay: 100
                })
                $(".formulaViewMenu").contextMenu({
                    menu: 'formulaViewListMenu',
                    leftButton: true },
                function(action, el, pos) {
                    contextMenuWorkFormulaView(action, el, pos);
            });
                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });

            });

            $(function() {

            <%--                $("#measures").draggable({
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
            );--%>
                    var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                    var dropMeasures=$('#dropTabs');

                    $(dragMeasure).draggable({
                        helper:"clone",
                        effect:["", "fade"]
            });

                    $(dropMeasures).droppable(
                    {
                        activeClass:"blueBorder",
                        accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
                        drop: function(ev, ui) {
                            var measure=ui.draggable.html();
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    });


                });

            function saveCols(grpId,grpType,divId){
                thisGraphId = grpId;
                var cols="";
                var grpColNames="";
                var colsUl=document.getElementById("sortable");
                var colIds=colsUl.getElementsByTagName("li");

                //added by k
                var viewby;
                if(document.getElementById("viewby1").checked)
                {
                  viewby=document.getElementById("viewby1").value;

                }
                else
                  {
                   viewby=document.getElementById("viewby2").value;

                  }


                for(var i=0;i<colIds.length;i++){

                    columnId = (colIds[i].id).split("~")[1];
                    columnName = (colIds[i].id).split("~")[0];
                    columnsList1 = columnsList1+'/'+colIds[i].id;

                    cols = cols+","+columnId
                    grpColNames=grpColNames+","+columnName;

                }

                if(cols!=""){
                    cols = cols.substring(1);
                    grpColNames=grpColNames.substring(1);
                }
                grpColNames=grpColNames.replace("%","^");
                var grpSize="Medium";
                var  hideDiv=parent.document.getElementById("hideDiv").value;
                parent.document.getElementById(divId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                $.ajax({
                    url: 'dashboardTemplateViewerAction.do?templateParam2=buildDbrdGraphs&grpColumns='+cols+'&grpColumnsNames='+grpColNames+'&grpType='+grpType+'&grpSize='+grpSize+'&grpId='+grpId+'&divId='+divId+'&dashboardId='+parent.document.getElementById("dbrdId").value+'&viewby='+viewby+'&DashboardGraphName='+parent.document.getElementById("NewDbrdGraphName").value,
                    success: function(data){
                        parent.document.getElementById(hideDiv).style.display='block'
                        parent.document.getElementById(divId).innerHTML ="";
                        parent.document.getElementById(divId).innerHTML = data;
                        parent.canceldbrdGraph();
                    }
                });
                columnsList1 = columnsList1.substring(1);
                columnsList1 = columnsList1+'^'+grpId;
            }

            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById("sortable");
                parentUL.removeChild(LiObj);
            }

            function createColumn(elmntId,elementName){

                var parentUL=document.getElementById("sortable");

                if(columnsList.indexOf(elmntId)==-1)
                {
                    columnsList = columnsList+"~"+elmntId;

                    var childLI=document.createElement("li");
                    childLI.id=elementName+'~'+elmntId;
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id=elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    //cell1.style.backgroundColor="#e6e6e6";
                    var a=document.createElement("a");
                    var deleteElement = elementName+'~'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                   // cell2.style.backgroundColor="#e6e6e6";
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }

                // i++;
                // }
                $("#sortable").sortable();
                $("#sortable").disableSelection();
                //alert('columnsList '+columnsList);
            }

            function cancelCols()
            {
                parent.cancelCols();
            }

            function prevGrpCols(divId){
                $.ajax({
                    url: 'dashboardTemplateAction.do?templateParam2=grpColsBydivid&divId='+divId+'&dashboardId='+parent.document.getElementById("dbrdId").value,
                    success: function(data) {
                        if(data!=""){
                            var grpCols=data;
                            var prevgrpCols=grpCols.split("^");
                            var grpColIds=prevgrpCols[0].split(",");
                            var grpColNames=prevgrpCols[1].split(",");
                            if(grpColIds.length!=0){
                                for(var k=0;k<grpColIds.length;k++){
                                    createColumn(grpColIds[k],grpColNames[k]);
                                }
                            }
                        }
                    }
                });
            }
            function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');
                }

        </script>
    </body>
</html>
