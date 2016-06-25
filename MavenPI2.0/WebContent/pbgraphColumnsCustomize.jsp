<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb" %>
<%@page import="java.util.HashMap" %>
<%@page import="com.progen.reportview.db.PbReportViewerDAO" %>

<%      //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            PbReportViewerDAO viewDAO = new PbReportViewerDAO();
            String folderIds = request.getParameter("folderIds");
            String graphIds = request.getParameter("grpIds");
            String grpId = request.getParameter("graphId");

            String reportId = request.getParameter("REPORTID");
            HashMap map = null;
            Container container = null;
            HashMap GraphHashMap = new HashMap();
            HashMap GraphDetails = new HashMap();

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

                GraphHashMap = container.getGraphHashMap();
                GraphDetails = (HashMap) GraphHashMap.get(grpId);

                grpType = String.valueOf(GraphDetails.get("graphTypeName"));

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

%>

<html>
    <head>
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

        <script>
            var y='';
            var xmlHttp;
            var columnsList = '';
            var columnsList1 = '';
            var columnId = '';
            var columnName = '';
            var graphDetails = '';
            var thisGraphId = '';
            var grpColArray=new Array();
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

            });

            $(function() {

                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                }
            );

                $("#sortable1").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable1");
                    }
                }
            );

                $("#sortable2").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable2");
                    }
                }
            );
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
                thisGraphId = graphId;
                var cols="";
                //var grpColNames="";
                var leftgrpColNames="";
                var rightgrpColNames="";
                var colsUl=document.getElementById("sortable");

                var leftcolsUl=document.getElementById("sortable1");
                var rightcolsUl=document.getElementById("sortable2");

                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");

                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){

                            //columnId = (colIds[i].id).split("~")[1];
                            //columnName = (colIds[i].id).split("~")[0];
                            //columnsList1 = columnsList1+'/'+colIds[i].id;

                            cols = cols+","+(colIds[i].id).replace("GrpCol","");
                            //grpColNames=grpColNames+","+columnName;

                        }

                        if(cols!=""){
                            cols = cols.substring(1);
                            //grpColNames=grpColNames.substring(1);
                        }
                        //grpColNames=grpColNames.replace("%","^");

                        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                        $.ajax({
                            url: 'reportViewer.do?reportBy=buildGraphs&gid='+graphId+'&graphChange=GrpColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&grpColumnsNames='+cols+'&REPORTID=<%=reportId%>',
                            success: function(data){
                                parentDiv.innerHTML=data;
                            }
                        });
                        //columnsList1 = columnsList1.substring(1);
                        //columnsList1 = columnsList1+'^'+graphId;
                        //parent.document.getElementById('graphColumns').value = columnsList1;
                        //parent.document.getElementById('currGrpColId').value = graphId;
                        parent.cancelCols();
                    }
                    else{
                        alert("Please Select Graph Columns");
                    }
                }
                else{
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

                        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';

                        $.ajax({
                            url: 'reportViewer.do?reportBy=buildGraphs&gid='+graphId+'&graphChange=changeDualAxisColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&REPORTID=<%=reportId%>&leftColumns='+leftgrpColNames+'&rightColumns='+rightgrpColNames,
                            success: function(data){
                                parentDiv.innerHTML=data;
                            }
                        });
                        //columnsList1 = columnsList1.substring(1);
                        //columnsList1 = columnsList1+'^'+graphId;
                        //parent.document.getElementById('graphColumns').value = columnsList1;
                        //parent.document.getElementById('currGrpColId').value = graphId;
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

                //var x=LiObj.id.split("~");
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
                    //cell1.style.backgroundColor="#e6e6e6";
                    var a=document.createElement("a");
                    var deleteElement = 'GrpCol'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    //cell2.style.backgroundColor="#e6e6e6";
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
                  

                $(".sortable").sortable();
                $(".sortable").disableSelection();
                //alert('columnsList '+columnsList);
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
                    //cell1.style.backgroundColor="#e6e6e6";
                    var a=document.createElement("a");
                    var deleteElement = 'GrpCol'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    //cell2.style.backgroundColor="#e6e6e6";
                    cell2.style.color='black';
                    cell2.innerHTML=elementName;
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }
               
                /*
                 $(".sortable").sortable();
                $(".sortable").disableSelection();
                 */

            }

            function cancelCols()
            {
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

        </script>

    </head>
    <%--<body onload="loadGraphCols('<%=grpId%>')">--%>
    <body>
        <%
            try {
                PbDb pbdb = new PbDb();
                String Query = "select DISTINCT   NVL(disp_name,sub_folder_type),sub_folder_tab_id from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type in('Facts') and disp_name not in('Calculated Facts','Formula')";
                String Query1 = "select DISTINCT element_id, NVL(disp_name,sub_folder_type),nvl(USER_COL_DESC, user_col_name),ref_element_id as column_name,REFFERED_ELEMENTS from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type in('Facts','Formula') order by nvl(USER_COL_DESC, user_col_name),REF_ELEMENT_ID";
                PbReturnObject pbro1 = pbdb.execSelectSQL(Query);
                PbReturnObject pbro2 = pbdb.execSelectSQL(Query1);
                //////////////////////////////////////////////////.println.println("Query---"+Query);

        %>


        <table style="width:100%;height:270px" border="solid black 1px">
            <form name="myForm3" method="post">
                <input type="hidden" name="prevColumns" id="prevColumns" value="<%=prevColumns%>">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Graph Columns from below</font></div>
                        <div style="height:250px;overflow-y:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <% for (int i = 0; i < pbro1.getRowCount(); i++) {
                                    %>
                                    <ul>
                                        <li class="closed"><img src='icons pinvoke/table.png'></img><span ><font size="1px" face="verdana"><%=pbro1.getFieldValueString(i, 0)%></font></span>


                                            <ul>
                                                <%for (int j = 0; j < pbro2.getRowCount(); j++) {
         String isit = "YES";
         if ((pbro1.getFieldValueString(i, 0).equalsIgnoreCase(pbro2.getFieldValueString(j, 1)) || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Calculated Facts") || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Formula") || pbro2.getFieldValueString(j, 1) == null || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("")) && (pbro2.getFieldValueInt(j, 0) == pbro2.getFieldValueInt(j, 3))) {
             if (pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Calculated Facts") || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Formula") || pbro2.getFieldValueString(j, 1) == null || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("")) {
                 String testQuery = "select distinct sub_folder_tab_id  from prg_user_all_info_details where element_id in(" + pbro2.getFieldValueString(j, 4) + ") and sub_folder_tab_id=" + pbro1.getFieldValueInt(i, 1);
                 //////////////////////////////////////////////////.println.println("sql-----" + testQuery);
                 PbReturnObject testpbro = pbdb.execSelectSQL(testQuery);
                 if (!(testpbro.getRowCount() > 0)) {
                     isit = "NO";
                 }


             }
             if (isit.equalsIgnoreCase("YES")) {
                                                %>

                                                <li class="closed" ><img src='icons pinvoke/table.png'></img><span class="myDragTabs" class="ui-state-default" id="<%=pbro2.getFieldValueString(j, 0)%>"><%=pbro2.getFieldValueString(j, 2)%></span>
                                                    <ul>
                                                        <%for (int k = 0; k < pbro2.getRowCount(); k++) {
                                                                               if (pbro2.getFieldValueInt(j, 0) == pbro2.getFieldValueInt(k, 3) && pbro2.getFieldValueInt(j, 0) != pbro2.getFieldValueInt(k, 0)) {
                                                        %>

                                                        <li><img src='icons pinvoke/report.png'></img><span class="myDragTabs" class="ui-state-default" id="<%=pbro2.getFieldValueString(k, 0)%>"><%=pbro2.getFieldValueString(k, 2)%></span></li>


                                                        <%}
                                                }%>
                                                    </ul>
                                                </li>





                                                <%}
         }
     }%>
                                            </ul>


                                        </li>
                                    </ul>
                                    <%
        }
                                    %>

                                </li>
                            </ul>
                        </div>
                    </td>
                    <%  if (grpType.equalsIgnoreCase("Dual Axis") || grpType.equalsIgnoreCase("OverlaidBar") || grpType.equalsIgnoreCase("OverlaidArea") || grpType.equalsIgnoreCase("FeverChart")) {%>
                    <td width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Graph Columns to here</font></div>
                        <div style="height:250px;overflow-y:scroll">
                            <table width="100%">
                                <tr style="height:50%">
                                    <td>
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Bar chart Columns</font></div>
                                        <ul id="sortable1" class="sortable" style="height:100px" >
                                            <%=leftgraphCols%>
                                        </ul>
                                    </td>
                                </tr>
                                <tr style="height:50%">
                                    <td>
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Line Chart Columns</font></div>
                                        <ul id="sortable2" class="sortable" style="height:100px" >
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
                        <div style="height:250px;overflow-y:scroll">
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
    </body>
</html>
