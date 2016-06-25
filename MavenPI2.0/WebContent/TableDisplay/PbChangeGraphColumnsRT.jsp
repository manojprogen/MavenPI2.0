<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.Arrays,java.util.List,com.progen.charts.JqplotGraphProperty,prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb,java.util.HashMap,java.util.ArrayList,com.progen.reportview.db.PbReportViewerDAO,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.db.SelectDbSpecificFunc"%>
<%          //for clearing cache

            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if ( (session.getAttribute("loadDialogs") != null && session.getAttribute("loadDialogs").equals("true") )
                 || (request.getParameter("loadDialogs") != null && request.getParameter("loadDialogs").equalsIgnoreCase("TRUE") )
                ){
                PbReportViewerDAO viewDAO = new PbReportViewerDAO();
                ReportTemplateDAO templateDAO = new ReportTemplateDAO();
                String graphIds = (String) request.getSession().getAttribute("grpIds");
                String grpId = (String) request.getSession().getAttribute("graphId");
                String reportId = (String) request.getSession().getAttribute("REPORTID");
                String folderIds = (String) request.getSession().getAttribute("folderIds");
                String tablistFlag = (String)request.getParameter("tableList");
                String from = (String) request.getSession().getAttribute("from");

                HashMap map = null;
                Container container = null;

                if(reportId == null){
                    reportId = (String) request.getParameter("REPORTID");
                    if (session.getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) session.getAttribute("PROGENTABLES");

                    if (map.get(reportId) != null) {
                        container = (Container) map.get(reportId);
                    } else {
                        container = new Container();
                    }
                    if(container != null){
                        grpId = container.getGarphIdForFact();
                        graphIds = container.getGarphIdsForFact();
                        folderIds = container.getFolderIdsForFact();
                    }
                }
                    }
                
                HashMap GraphHashMap = new HashMap();
                HashMap GraphDetails = new HashMap();

                String[] barChartColumnNames = new String[0];
                String[] barChartColumnTitles = new String[0];
                String[] viewByElementIds = new String[0];

                String graphCols = "";
                String tabCols = "";
                String prevColumns = "";
                String grpType = "";

                String[] barChartColumnNames1 = new String[0];
                String[] barChartColumnTitles1 = new String[0];

                String[] barChartColumnNames2 = new String[0];
                String[] barChartColumnTitles2 = new String[0];

                String leftgraphCols = "";
                String rightgraphCols = "";
                HashMap ParametersHashMap = null;
                ArrayList Parameters = null;
                ArrayList ParametersNames = null;
                String factsHtml = "";


                boolean MultiAxisFlag = false;
                String themeColor="blue";
                if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
                     
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
                  String selectedgraphtype1=null;
                                  String graphTypename=null;
                                  String graphTypeid=null;
                                  String graphid=null;
                                  String graphdisptype="";
                                  String[] displaycolumns1=null;
                                  String[]  displaycolumns=null;
                                  String[] displaytitles=null;
                                  String[] displaytitles1=null;
                                  String tablecols="";
                                  String[] tablecol=null;
                         JqplotGraphProperty graphproperty=new JqplotGraphProperty();
                              HashMap singleGraphDetails = (HashMap) container.getGraphHashMap();
                              PbReportViewerDAO reportViewerdao=new PbReportViewerDAO();

                              if(singleGraphDetails.get("jqgraphproperty"+grpId)!=null){
                                 graphproperty= (JqplotGraphProperty)singleGraphDetails.get("jqgraphproperty"+grpId);
                                 selectedgraphtype1 = graphproperty.getSlectedGraphType(grpId);
                                 graphTypename=graphproperty.getGraphTypename();
                                 graphTypeid=graphproperty.getGraphTypeId();
                                 graphid=graphproperty.getGraphId();
                                 graphdisptype=graphproperty.getGraphdisptype();
                                 displaycolumns1=graphproperty.getGraphColumns();
                                  tablecol=graphproperty.getTableColumns();

                              }else{
                                 graphproperty = reportViewerdao.getJqGraphDetails(grpId);
                                 if(graphproperty!=null){
                                 selectedgraphtype1 = graphproperty.getSlectedGraphType(grpId);
                                 graphTypename=graphproperty.getGraphTypename();
                                 graphTypeid=graphproperty.getGraphTypeId();
                                 graphid=graphproperty.getGraphId();
                                 graphdisptype=graphproperty.getGraphdisptype();
                                 displaycolumns1=graphproperty.getGraphColumns();
                                  tablecol=graphproperty.getTableColumns();
                              }
                              }

                    GraphHashMap = container.getGraphHashMap();
                    GraphDetails = (HashMap) GraphHashMap.get(grpId);
                    grpType = String.valueOf(GraphDetails.get("graphTypeName"));
                     if (graphTypename!=null && (graphTypename.equalsIgnoreCase("DualAxis(Bar-Line)") || graphTypename.equalsIgnoreCase("Overlaid(Bar-Line)"))){
                    //if (graphTypename.equalsIgnoreCase("DualAxis(Bar-Line)") || graphTypename.equalsIgnoreCase("Overlaid(Bar-Line)") || grpType.equalsIgnoreCase("Dual Axis") || grpType.equalsIgnoreCase("OverlaidArea") || grpType.equalsIgnoreCase("FeverChart") || grpType.equalsIgnoreCase("OverlaidBar") || grpType.equalsIgnoreCase("TargetBar") || grpType.equalsIgnoreCase("HorizontalTargetBar")) {
                        //MultiAxisFlag = true;
                        //multiAxisfalg is setted false for jqplot graphs
                        MultiAxisFlag =true;
                    }

                    if (GraphDetails.get("viewByElementIds") != null && GraphDetails.get("barChartColumnNames") != null) {
                        barChartColumnNames = (String[]) GraphDetails.get("barChartColumnNames");
                        viewByElementIds = (String[]) GraphDetails.get("viewByElementIds");
                        barChartColumnTitles = (String[]) GraphDetails.get("barChartColumnTitles");
                        if (tablecol != null && !tablecol.toString().isEmpty() && tablecol.length > 0) {
                                String[] duplist = new String[barChartColumnNames.length + tablecol.length];
                                String[] dupname = new String[barChartColumnNames.length + tablecol.length];
                                for (int j = 0; j < barChartColumnNames.length; j++) {
                                    duplist[j] = barChartColumnNames[j];
                                    dupname[j] = barChartColumnTitles[j];
                                }
                                for (int i = 0; i < tablecol.length; i++) {
                                    List<String> list = Arrays.asList(duplist);
                                    if (!list.contains(tablecol[i])) {
                                        duplist[i + barChartColumnNames.length] = tablecol[i];
                                        dupname[i + barChartColumnNames.length] = container.getMeasureName(tablecol[i]);
                                    }
                                }
                                barChartColumnNames = duplist.clone();
                                barChartColumnTitles = dupname.clone();
                            }
                         if (Integer.parseInt(container.getColumnViewByCount()) != 0)
                             graphCols=viewDAO.buildGraphColumns(container,grpId);
                        else
                        graphCols = viewDAO.buildGraphColumns(viewByElementIds, barChartColumnNames, barChartColumnTitles);

                        for (int i = viewByElementIds.length; i < barChartColumnNames.length; i++) {
                            if(barChartColumnNames[i]!=null)
                            prevColumns = prevColumns + "," + barChartColumnNames[i].replace("A_", "");
                        }
                        if (!prevColumns.equalsIgnoreCase("")) {
                            prevColumns = prevColumns.substring(1);
                        }

                        if (MultiAxisFlag) {
                            if (GraphDetails.get("barChartColumnNames1") != null && GraphDetails.get("barChartColumnTitles1") != null) {
                                barChartColumnNames1 = (String[]) GraphDetails.get("barChartColumnNames1");
                                barChartColumnTitles1 = (String[]) GraphDetails.get("barChartColumnTitles1");
                                  java.util.List <String> tablecollist=new java.util.ArrayList<String> ();
                              if (tablecol != null && !tablecol.toString().isEmpty() && tablecol.length > 0) {
                                    tablecollist = Arrays.asList(tablecol);
                                String[] duplist = new String[barChartColumnNames1.length + tablecol.length];
                                String[] dupname = new String[barChartColumnNames1.length + tablecol.length];
                                for (int j = 0; j < barChartColumnNames1.length; j++) {
                                 if(!tablecollist.contains(barChartColumnNames1[j])){
                                    duplist[j] = barChartColumnNames1[j];
                                    dupname[j] = barChartColumnTitles1[j];
                                                                       }

                                }
                                for (int i = 0; i < tablecol.length/2; i++) {
                                    List<String> list = Arrays.asList(duplist);
                                    if (!list.contains(tablecol[i])) {
                                        duplist[i + barChartColumnNames1.length] = tablecol[i];
                                        dupname[i + barChartColumnNames1.length] = container.getMeasureName(tablecol[i]);
                                    }
                                }
                                barChartColumnNames1 = duplist.clone();
                                barChartColumnTitles1= dupname.clone();
                            }

                                leftgraphCols = viewDAO.buildGraphColumns(viewByElementIds, barChartColumnNames1, barChartColumnTitles1);
                            }

                            if (GraphDetails.get("barChartColumnNames2") != null && GraphDetails.get("barChartColumnTitles2") != null) {
                                barChartColumnNames2 = (String[]) GraphDetails.get("barChartColumnNames2");
                                barChartColumnTitles2 = (String[]) GraphDetails.get("barChartColumnTitles2");
                               if (tablecol != null && !tablecol.toString().isEmpty() && tablecol.length > 0) {
                                String[] duplist = new String[barChartColumnNames2.length + tablecol.length];
                                String[] dupname = new String[barChartColumnNames2.length + tablecol.length];
                                for (int j = 0; j < barChartColumnNames2.length; j++) {
                                    duplist[j] = barChartColumnNames2[j];
                                    dupname[j] = barChartColumnTitles2[j];

                                }
                                for (int i = tablecol.length/2; i < tablecol.length; i++) {
                                    List<String> list = Arrays.asList(duplist);
                                    if (!list.contains(tablecol[i])) {
                                        duplist[i + barChartColumnNames2.length] = tablecol[i];
                                        dupname[i + barChartColumnNames2.length] = container.getMeasureName(tablecol[i]);
                                    }
                                }
                                barChartColumnNames2= duplist.clone();
                                barChartColumnTitles2 = dupname.clone();
                            }
                                rightgraphCols = viewDAO.buildGraphColumns(viewByElementIds, barChartColumnNames2, barChartColumnTitles2);
                            }
                        }
                    }
                    if (Integer.parseInt(container.getColumnViewByCount()) != 0)
                        factsHtml = templateDAO.getMeasuresForCrosstab(container);
                        else{
                        ArrayList alist = new ArrayList();
                        if(container.getTableList() != null && !container.getTableList().isEmpty()){
                                          factsHtml = templateDAO.getMeasuresForReport(folderIds, Parameters,request.getContextPath(),container.getTableList());
                                          container.setTableList(alist);
                                        }else if(tablistFlag != null && tablistFlag.equalsIgnoreCase("true")){
                                            factsHtml = templateDAO.getMeasures(folderIds, Parameters,request.getContextPath());
                                        }
                        }
                     int columnViewbyCount=Integer.parseInt(container.getColumnViewByCount());


                              displaycolumns=container.getDisplayColumns().toString().split(",");
                              if(displaycolumns1!=null){
                                  displaytitles1=new String[displaycolumns1.length];
                                  for(int i=0;i<displaycolumns1.length;i++){
                                      if(displaycolumns1[i].contains("-"))
                                      displaytitles1[i]=container.getMeasureName(displaycolumns1[i].substring(0,displaycolumns1[i].lastIndexOf("-")).trim());

                                  }

                              }
                              if(displaytitles1==null || displaytitles1.length==0)
                              displaytitles1=container.getDisplayLabels().toString().split(",");
                             displaytitles=container.getDisplayLabels().toString().split(",");
                     if(!graphdisptype.isEmpty() && (graphdisptype.equalsIgnoreCase("st") || graphdisptype.equalsIgnoreCase("GT") || graphdisptype.equalsIgnoreCase("AVG"))){
                         graphCols=viewDAO.buildGttotals(viewByElementIds, displaycolumns, displaytitles1,graphdisptype,displaycolumns1);
                         factsHtml=viewDAO.buildGtmeasures(viewByElementIds, displaycolumns, displaytitles);
                     }
      String contextpath=request.getContextPath();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--         <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
 <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/jquery-1.8.23-custom.min.css" rel="stylesheet" />

        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/treeviewstyle/screen.css" />
        <link href="<%=contextpath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="<%=contextpath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextpath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript"  src="<%=contextpath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/jquery.simplemodal-1.1.1.js" ></script>
        <script  type="text/javascript" src="<%=contextpath%>/javascript/jquery.contextMenu.js" ></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/reportDesign.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <style type="text/css" >
            *{font:11px verdana;}
        </style>
        <link type="text/css" href="<%=contextpath%>/css/global.css" rel="stylesheet" />
        

    </head>

    <body>
        <%-- <img id="imgId" src="<%=request.getContextPath()%>/images/ajax.gif" width="75px" height="75px">--%>

        <%try {%>
        <form action=""  name="myForm3" method="post" >

            <input type="hidden" name="prevColumns" id="prevColumns" value="<%=prevColumns%>">
        <table style="width:100%;height:270px" border="solid black 1px">


                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font class="gFontFamily gFontSize12" size="2" style="font-weight:bold">Select Measures from below</font></div>
                        <div style="height:250px;overflow:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">


                                <ul id="measures">

                                <%=factsHtml%>
                                </ul>

                            </ul>
                        </div>
                    </td>
                    <%  if (MultiAxisFlag&&Integer.parseInt(container.getColumnViewByCount()) == 0) {%>
                    <td width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Graph Columns to here</font></div>
                        <div style="height:250px;overflow:auto">
                            <table width="100%">
                                <tr style="height:50%">
                                    <td>
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Graph Type1 Columns</font></div>
                                        <ul id="sortable1" class="sortable" style="height:100px;color:white" >

                                            <%=leftgraphCols%>

                                        </ul>
                                    </td>
                                </tr>
                                <tr style="height:50%">
                                    <td>
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Graph Type2 Columns</font></div>
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

        </table>
        <table style="width:100%" align="center">
            <tr>
                <td colspan="2" style="height:10px"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveCols('<%=grpId%>','<%=graphIds%>')">

                </td>
            </tr>
        </table>



<!--            <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
                <li class="view"><a href="#view">View</a></li>
            </ul>-->
            <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
                <table> <tr>
                        <td id="value">
                        </td>
                    </tr></table>
            </div>
        </form>
                    <script type="text/javascript" >

            var grpColArray=new Array();
            $(document).ready(function() {
                <%
                    if(from != null && from.equalsIgnoreCase("true")){%>
                        parent.$("#graphtableList").attr('checked',false);
                   <% }
                %>
                $("#myList3").treeview({
                    animated:"slow"
                    //persist: "cookie"
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


                 $(dragMeasure).dblclick(function(event) {
                            var elementId=event.target.id;
                            var pElementName=$("#"+elementId).html();
                            var obj=document.getElementById(elementId).parentNode;
                            var ulobj=obj.getElementsByTagName("ul");
                            if(ulobj!=null&&ulobj[0]!=null&&ulobj[0]!=undefined){
                            var liobjects=ulobj[0].getElementsByTagName("li");
                            var spanObj;
                            createColumn(elementId,pElementName,"sortable");
                            for(var i=0;i<liobjects.length;i++)
                               {
                                   spanObj=liobjects[i].getElementsByTagName("span");
                                  createColumn(spanObj[0].id,spanObj[0].innerHTML,"sortable");

                               }
                       }

             });



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
                //var parentDiv = parent.document.getElementById('previewDispGraph');
                var cols="";
                var leftgrpColNames="";
                var rightgrpColNames="";
                var elemNames="";
                var colsUl=document.getElementById("sortable");

                var leftcolsUl=document.getElementById("sortable1");
                var rightcolsUl=document.getElementById("sortable2");

                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");
                    var colViewByCnt='<%=columnViewbyCount%>';

//                    if(colIds.length>1&&colViewByCnt!=0)
//                        alert("Please select only one Measure");
//                    else{
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                           elemNames=elemNames+","+$("#"+colIds[i].id.replace("GrpCol","")).html();
                        }
                        if(cols!=""){
                            cols = cols.substring(1);
                        }
                        if(elemNames!=""){
                            elemNames=elemNames.substr(1)
                        }
                        //parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=graphColumnChanges&gid='+graphId+'&graphChange=GrpColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&elemNames='+elemNames+'&REPORTID=<%=reportId%>',
                            success: function(data){
                                //parent.$("#graphColsDialog").dialog('close');
                                parent.submitFormGraphMeasChange()

                            }
                        });
                        cancelChangeGrpColumns();
                    }
                    else{
                        alert("Please Select Graph Columns");
                    }
                                    parent.$("#graphColsDialog").dialog('close');

//                }
                }
                else{                parent.$("#graphColsDialog").dialog('close');

                    var leftcolIds=leftcolsUl.getElementsByTagName("li");
                    var rightcolIds=rightcolsUl.getElementsByTagName("li");

                    if(leftcolIds!=null && leftcolIds.length!=0 && rightcolIds!=null && rightcolIds.length!=0){
                        for(var i=0;i<leftcolIds.length;i++){
                            leftgrpColNames=leftgrpColNames+","+(leftcolIds[i].id).replace("GrpCol","");
                        }
                        for(var i=0;i<rightcolIds.length;i++){
                            rightgrpColNames=rightgrpColNames+","+(rightcolIds[i].id).replace("GrpCol","");
                        }

                        if(leftgrpColNames!=""){
                            leftgrpColNames = leftgrpColNames.substring(1);
                        }
                        if(rightgrpColNames!=""){
                            rightgrpColNames = rightgrpColNames.substring(1);
                        }
                        cols=leftgrpColNames+","+rightgrpColNames;

                        //parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=graphColumnChanges&gid='+graphId+'&graphChange=changeDualAxisColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&REPORTID=<%=reportId%>&leftColumns='+leftgrpColNames+'&rightColumns='+rightgrpColNames,
                            success: function(data){
                                //parent.$("#graphColsDialog").dialog('close');
                                parent.submitFormGraphMeasChange()
                            }
                        });
                        cancelChangeGrpColumns();
                    }
                    else{
                        if((leftcolIds==null || leftcolIds.length==0)){
                            alert("Please Select Graph Type1 Columns")
                        }
                        else{
                            alert("Please Select Graph Type2 Columns")
                        }
                    }
                }
            }

            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);;
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
                    childLI.style.height='25px';
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
                var value=$.inArray(elmntId,grpColArray);
                //if(x.match(elmntId)==null){
                if(value==-1){
                    grpColArray.push(elmntId);

                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;

                    childLI.style.width='auto';
                    childLI.style.height='25px';
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
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
            }

            function cancelCols(){
                cancelChangeGrpColumns();
            }
            function cancelChangeGrpColumns(){
                var frameObj=parent.document.getElementById("graphColsFrame");
                frameObj.style.display='none';
                parent.document.getElementById('fadestart').style.display='none';
            }
            function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');


            }
        </script>

        <%

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }

                request.getSession().removeAttribute("folderIds");
                request.getSession().removeAttribute("grpIds");
                request.getSession().removeAttribute("graphId");
                request.getSession().removeAttribute("REPORTID");
                request.getSession().removeAttribute("loadDialogs");
                request.getSession().removeAttribute("from");                
            }
        %>
                    </body>
                    </html>
