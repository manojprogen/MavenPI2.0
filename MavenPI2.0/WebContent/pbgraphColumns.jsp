<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb" %>
<%@page import="java.util.HashMap,java.util.ArrayList" %>
<%@page import="com.progen.reportview.db.PbReportViewerDAO" %>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO" %>
<%@page import="com.progen.db.SelectDbSpecificFunc" %>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            PbReportViewerDAO viewDAO = new PbReportViewerDAO();
            ReportTemplateDAO templateDAO = new ReportTemplateDAO();
            String folderIds = request.getParameter("folderIds");
            String graphIds = request.getParameter("grpIds");
            String grpId = request.getParameter("graphId");
            String graphTableMethod = request.getParameter("graphTableMethod");
            String forwardFlag = "";
            String factsHtml = null;

            if (graphTableMethod != null) {
                forwardFlag = "GTM";
            } else {
                forwardFlag = "GM";
            }
            ////.println("******forwardFlag*****" + forwardFlag);

            String reportId = request.getParameter("REPORTID");
            HashMap map = null;
            Container container = null;
            HashMap GraphHashMap = new HashMap();
            HashMap GraphDetails = new HashMap();
            HashMap ParametersHashMap = null;
            ArrayList Parameters = null;
            ArrayList ParametersNames = null;

            String[] barChartColumnNames = new String[0];
            String[] barChartColumnTitles = new String[0];
            String[] viewByElementIds = new String[0];

            String graphCols = "";
            String prevColumns = "";
            String grpType = "";

            String[] barChartColumnNames1 = new String[0];
            String[] barChartColumnTitles1 = new String[0];

            String[] barChartColumnNames2 = new String[0];
            String[] barChartColumnTitles2 = new String[0];

            String leftgraphCols = "";
            String rightgraphCols = "";

            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");

                if (map.get(reportId) != null) {
                    container = (Container) map.get(reportId);
                } else {
                    container = new Container();
                }

                ParametersHashMap = container.getParametersHashMap();
                if (ParametersHashMap.get("ParametersNames") != null) {
                    Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                    ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                }

                GraphHashMap = container.getGraphHashMap();
                GraphDetails = (HashMap) GraphHashMap.get(grpId);

                grpType = String.valueOf(GraphDetails.get("graphTypeName"));
                boolean MultiAxisFlag = false;

                if (grpType.equalsIgnoreCase("Dual Axis") || grpType.equalsIgnoreCase("OverlaidBar") || grpType.equalsIgnoreCase("OverlaidArea") || grpType.equalsIgnoreCase("TargetBar") || grpType.equalsIgnoreCase("HorizontalTargetBar") || grpType.equalsIgnoreCase("FeverChart")) {
                    MultiAxisFlag = true;
                }

                if (GraphDetails.get("viewByElementIds") != null && GraphDetails.get("barChartColumnNames") != null) {
                    barChartColumnNames = (String[]) GraphDetails.get("barChartColumnNames");
                    viewByElementIds = (String[]) GraphDetails.get("viewByElementIds");
                    barChartColumnTitles = (String[]) GraphDetails.get("barChartColumnTitles");

                    graphCols = viewDAO.buildGraphColumns(viewByElementIds, barChartColumnNames, barChartColumnTitles);

                    for (int i = viewByElementIds.length; i < barChartColumnNames.length; i++) {
                        prevColumns = prevColumns + "," + barChartColumnNames[i].replace("A_", "");
                    }
                    if (!prevColumns.equalsIgnoreCase("")) {
                        prevColumns = prevColumns.substring(1);
                    }

                    if (grpType.equalsIgnoreCase("Dual Axis") || grpType.equalsIgnoreCase("OverlaidBar") || grpType.equalsIgnoreCase("OverlaidArea") || grpType.equalsIgnoreCase("FeverChart")) {
                        if (GraphDetails.get("barChartColumnNames1") != null && GraphDetails.get("barChartColumnTitles1") != null) {
                            barChartColumnNames1 = (String[]) GraphDetails.get("barChartColumnNames1");
                            barChartColumnTitles1 = (String[]) GraphDetails.get("barChartColumnTitles1");

                            leftgraphCols = viewDAO.buildGraphColumns(viewByElementIds, barChartColumnNames1, barChartColumnTitles1);
                        }

                        if (GraphDetails.get("barChartColumnNames2") != null && GraphDetails.get("barChartColumnTitles2") != null) {
                            barChartColumnNames2 = (String[]) GraphDetails.get("barChartColumnNames2");
                            barChartColumnTitles2 = (String[]) GraphDetails.get("barChartColumnTitles2");

                            rightgraphCols = viewDAO.buildGraphColumns(viewByElementIds, barChartColumnNames2, barChartColumnTitles2);
                        }
                    }
                }
                factsHtml = templateDAO.getMeasures(folderIds, Parameters, request.getContextPath());
%>

<html>
    <head>
        <title>piEE</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" >
            var y='';
            var xmlHttp;
            var columnsList = '';
            var columnsList1 = '';
            var columnId = '';
            var columnName = '';
            var graphDetails = '';
            var thisGraphId = '';
            var grpColArray=new Array();
            var mesNameArray=new Array();
            var mesDualNameArray=new Array();
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
                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#dropTabs');
                var sort1Measures=$('#sortable1');
                var sort2Measures=$('#sortable2');
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
                $(sort1Measures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var measure=ui.draggable.html();
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable1");
                    }
                });
                $(sort2Measures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var measure=ui.draggable.html();
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable2");
                    }
                });

            });

            var prevColsStr="<%=prevColumns%>"
            var prevCols=prevColsStr.split(",");

            for(var k=0;k<prevCols.length;k++){
                var pr=grpColArray.toString();
                if(pr.match(prevCols[k])==null){
                    grpColArray.push(prevCols[k]);
                }
            }

            function saveCols(graphId,graphIds){
                var parentDiv = parent.document.getElementById('previewDispGraph');
                var forwardFlag = document.getElementById("forwardFlag").value;
                thisGraphId = graphId;
                var cols="";
                var leftgrpColNames="";
                var rightgrpColNames="";
                var colsUl=document.getElementById("sortable");
                var tdId ;
                var innerTd;
                var measureNames ;

                var leftcolsUl=document.getElementById("sortable1");
                var rightcolsUl=document.getElementById("sortable2");

                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");

                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                            //   alert("cols---"+cols)
                            tdId = colIds[i].id.replace("GrpCol","");
                            innerTd = document.getElementById("eleName"+tdId).innerHTML;
                            mesNameArray.push(innerTd);
                            measureNames = mesNameArray.toString().replace("&gt;","~").replace("&lt;","^");
                            //    alert("measureNames---"+measureNames)
                        }

                        if(cols!=""){
                            cols = cols.substring(1);                           
                        }
                        parentDiv.style.height = '250'
                        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" /></center>';
                        if(forwardFlag == "GTM"){
                            $.ajax({
                                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&graphChange=GrpColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&measureNames='+measureNames+'&grpColumnsNames='+cols+'&REPORTID='+<%=reportId%>+'&graphTableMethod=GTM',
                                success: function(data){
                                    parentDiv.innerHTML=data;
                                    parent.document.getElementById('previewDispGraph').style.height = 'auto';
                                }
                            });
                        } else{
                            $.ajax({
                                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&graphChange=GrpColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&measureNames='+measureNames+'&grpColumnsNames='+cols+'&REPORTID='+<%=reportId%>+'&graphTableMethod=GM',
                                success: function(data){
                                    parentDiv.innerHTML=data;
                                    parent.document.getElementById('previewDispGraph').style.height = 'auto';
                                }
                            });
                        }
                        parent.cancelCols();
                    }
                    else{
                        alert("Please Select Graph Columns");
                    }

                }else{

                    var leftcolIds=leftcolsUl.getElementsByTagName("li");
                    var rightcolIds=rightcolsUl.getElementsByTagName("li");
                    var leftTd,rightTd,leftTdId,rightTdId;

                    if(leftcolIds!=null && leftcolIds.length!=0 && rightcolIds!=null && rightcolIds.length!=0){
                        for(var i=0;i<leftcolIds.length;i++){
                            leftgrpColNames=leftgrpColNames+","+(leftcolIds[i].id).replace("GrpCol","");
                            leftTdId = leftcolIds[i].id.replace("GrpCol","");
                            leftTd =  document.getElementById("eleName"+leftTdId).innerHTML;
                            mesDualNameArray.push(leftTd);
                        }
                        for(var i=0;i<rightcolIds.length;i++){
                            rightgrpColNames=rightgrpColNames+","+(rightcolIds[i].id).replace("GrpCol","");
                            rightTdId = rightcolIds[i].id.replace("GrpCol","")
                            rightTd =  document.getElementById("eleName"+rightTdId).innerHTML;
                            mesDualNameArray.push(rightTd);
                        }

                        if(leftgrpColNames!=""){
                            leftgrpColNames = leftgrpColNames.substring(1);
                        }
                        if(rightgrpColNames!=""){
                            rightgrpColNames = rightgrpColNames.substring(1);
                        }
                        cols=leftgrpColNames+","+rightgrpColNames;
                        measureNames = mesDualNameArray.toString().replace("&gt;","~").replace("&lt;","^");

                        parentDiv.style.height = '250'
                        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" /></center>';
                        if(forwardFlag == "GTM"){
                            $.ajax({
                                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&graphChange=changeDualAxisColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&measureNames='+measureNames+'&REPORTID=<%=reportId%>&leftColumns='+leftgrpColNames+'&rightColumns='+rightgrpColNames+'&graphTableMethod=GTM',
                                success: function(data){
                                    parentDiv.innerHTML=data;
                                    parentDiv.style.height = 'auto'
                                }
                            });
                        } else {
                            $.ajax({
                                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&graphChange=changeDualAxisColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&measureNames='+measureNames+'&REPORTID=<%=reportId%>&leftColumns='+leftgrpColNames+'&rightColumns='+rightgrpColNames,
                                success: function(data){
                                    parentDiv.innerHTML=data;
                                    parentDiv.style.height = 'auto'
                                }
                            });
                        }
                        parent.cancelCols();
                    }
                    else{
                        if((leftcolIds==null || leftcolIds.length==0)){
                            alert("Please Select Bar Chart Columns")
                        }
                        else{
                            alert("Please Select Line Chart Columns")
                        }
                    }
                }
            }

            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);
                var x=index.replace("GrpCol","");
                var i=0;

                for(i=0;i<grpColArray.length;i++){
                    if(grpColArray[i]==x)
                        grpColArray.splice(i,1);
                }
                
            }

            function createColumn(elmntId,elementName){
                var parentUL=document.getElementById("sortable");
                var x=grpColArray.toString();
                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId);

                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;                
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id="GrpTab"+elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    var a=document.createElement("a");
                    var deleteElement = 'GrpCol'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.style.color='black';
                    cell2.id = "eleName"+elmntId;
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }  
                $(".sortable").sortable();
                $(".sortable").disableSelection();
            }

            function createColumn(elmntId,elementName,tarLoc){
                var parentUL=document.getElementById(tarLoc);
                var x=grpColArray.toString();
                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId);

                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id="GrpTab"+elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    var a=document.createElement("a");
                    var deleteElement = 'GrpCol'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.id = "eleName"+elmntId;
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
            }
            function cancelCols(){
                parent.cancelCols();
            }
            function loadGraphCols(currGrpId){
                var graphCols = parent.document.getElementById('graphColumns').value
                var currGrpId = graphCols.split("^")[1];
                if(graphCols!='' && (currGrpId==currGrpId)){
                    var graphCols1 = graphCols.split("/");
                    for(var i=0;i<graphCols1.length;i++){
                        var graphCols2 = graphCols1[i].split("~");
                        createColumn(graphCols2[1],graphCols2[0]);
                    }
                }
            }
            function contextMenuWorkFormulaView(action, el, pos){
                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');
            }
        </script>
        <style type="text/css">
            *{font:11px verdana}
        </style>

    </head>
    <body>
        <% try {%>
        <table style="width:100%;height:270px" border="solid black 1px">
            <form action=""  name="myForm3" method="post">
                <input type="hidden" name="prevColumns" id="prevColumns" value="<%=prevColumns%>">
                <input type="hidden" name="forwardFlag" id="forwardFlag" value="<%=forwardFlag%>">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Measures from below</font></div>
                        <div style="height:250px;overflow:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">

                                <%--<li class="open" style="background-image:url('<%=request.getContextPath()%>/images/treeViewImages/plus.gif')">--%>
                                <ul id="measures">
                                    <%=factsHtml%>

                                </ul>
                                <%--</li>--%>
                            </ul>
                        </div>
                    </td>
                    <%  if (MultiAxisFlag) {%>
                    <td width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Graph Columns to here</font></div>
                        <div style="height:250px;overflow:auto">
                            <table width="100%">
                                <tr style="height:50%">
                                    <td>
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Bar chart Columns</font></div>
                                        <ul id="sortable1" class="sortable" style="height:100px;color:white" >
                                            <%=leftgraphCols%>
                                        </ul>
                                    </td>
                                </tr>
                                <tr style="height:50%">
                                    <td>
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Line Chart Columns</font></div>
                                        <ul id="sortable2" class="sortable" style="height:100px;color:white" >
                                            <%=rightgraphCols%>
                                        </ul>
                                    </td>
                                </tr>
                            </table>
                        </div>

                    </td>
                    <%} else {%>
                    <td id="dropTabs" width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Graph Columns to here</font></div>
                        <div style="height:250px;overflow:auto">
                            <ul id="sortable">
                                <%=graphCols%>
                            </ul>
                        </div>
                    </td>
                    <%}%>
                </tr>
            </form>
        </table>

        <center><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveCols('<%=grpId%>','<%=graphIds%>')">&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelCols()"></center>

        <%} catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        %>
<!--        <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
            <li class="view"><a href="#view">View</a></li>
        </ul>-->
        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
            <table >
                <tr>
                    <td id="value"></td>
                </tr>
                </table>
                    </div>
                    </body>
                    </html>
