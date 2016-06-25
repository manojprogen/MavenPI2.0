<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb,java.util.HashMap,com.progen.reportview.db.PbReportViewerDAO"%>
<%
        PbReportViewerDAO viewDAO = new PbReportViewerDAO();
         String folderIds = request.getParameter("folderIds");
        String graphIds = request.getParameter("grpIds");
        String grpId = request.getParameter("graphId");

        String whatIfScenarioId = request.getParameter("whatIfScenarioId");
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

            if (map.get(whatIfScenarioId) != null) {
                container = (Container) map.get(whatIfScenarioId);
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

                if (grpType.equalsIgnoreCase("Dual Axis") || grpType.equalsIgnoreCase("Overlaid")) {
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
String contextPath=request.getContextPath();
%>

<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/watifDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />



    </head>
    <%--<body onload="loadGraphCols('<%=grpId%>')">--%>
    <body>
        <%
    try {
        //////////////////////////.println("-------------999-------------");

        String Query = "select DISTINCT NVL(disp_name,sub_folder_type),sub_folder_tab_id from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type in('Facts') and disp_name not in('Calculated Facts','Formula')";
        String Query1 = "select DISTINCT element_id, NVL(disp_name,sub_folder_type),nvl(USER_COL_DESC, user_col_name),ref_element_id as column_name,BUSS_COL_NAME, ref_element_type,REFFERED_ELEMENTS from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type in('Facts','Formula') and use_report_flag='Y'  order by BUSS_COL_NAME, ref_element_type";

        PbDb pbdb=new PbDb();

        PbReturnObject pbro1 =pbdb.execSelectSQL(Query);
        PbReturnObject pbro2 = pbdb.execSelectSQL(Query1);
       // //////////////////////////.println("Query---"+Query);
       // //////////////////////////.println("Query1---"+Query1);
//pbro1.writeString();
//pbro2.writeString();
        %>


        <table style="width:100%;height:270px" border="solid black 1px">
            <form name="myForm3" method="post">
                <input type="hidden" name="prevColumns" id="prevColumns" value="<%=prevColumns%>">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Graph Columns from below</font></div>
                        <div style="height:250px;overflow-y:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">
                                <li  class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <% for (int i = 0; i < pbro1.getRowCount(); i++) {
                                    %>
                                    <ul>
                                        <li class="closed" ><img src='icons pinvoke/table.png'></img><span ><font size="1px" face="verdana"><%=pbro1.getFieldValueString(i, 0)%></font></span>
                                            <ul>
                                                <%for (int j = 0; j < pbro2.getRowCount(); j++) {
                                                    String isit="YES";
                                 if ((pbro1.getFieldValueString(i, 0).equalsIgnoreCase(pbro2.getFieldValueString(j, 1))||pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Calculated Facts") || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Formula")||pbro2.getFieldValueString(j, 1)==null ||pbro2.getFieldValueString(j, 1).equalsIgnoreCase("") ) && (pbro2.getFieldValueInt(j, 0)==pbro2.getFieldValueInt(j, 3))) {
                                      //////////////////////////.println("pbro2.getFieldValueString(i, 0)--"+pbro2.getFieldValueString(j, 1));
                                     if(pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Calculated Facts") || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Formula") ){
                                      String testQuery="select distinct sub_folder_tab_id  from prg_user_all_info_details where element_id in("+pbro2.getFieldValueString(j, 6)+") and sub_folder_tab_id="+pbro1.getFieldValueInt(i, 1);
                                       //////////////////////////.println("sql-----"+testQuery);
                                       PbReturnObject testpbro=pbdb.execSelectSQL(testQuery);
                                       if(!(testpbro.getRowCount()>0)){
                                         isit="NO";
                                       }
                                      }
                                     if(isit.equalsIgnoreCase("YES")){
                                                %>

                                          <li class="closed" ><img src='icons pinvoke/table.png'></img><span class="myDragTabs" class="ui-state-default" id="<%=pbro2.getFieldValueString(j, 0)%>"><%=pbro2.getFieldValueString(j, 2)%></span>
                                            <ul>
                             <%for (int k = 0; k < pbro2.getRowCount(); k++) {
                                 if (pbro2.getFieldValueInt(j, 0)==pbro2.getFieldValueInt(k, 3) && pbro2.getFieldValueInt(j, 0)!=pbro2.getFieldValueInt(k, 0)) {
                                                %>

                                                <li><img src='icons pinvoke/report.png'></img><span class="myDragTabs" class="ui-state-default" id="<%=pbro2.getFieldValueString(k, 0)%>"><%=pbro2.getFieldValueString(k, 2)%></span></li>


                                                <%}
     }%>
                                            </ul>
                                        </li>





                                                <%

                                 }
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
                    <%  if (grpType.equalsIgnoreCase("Dual Axis") || grpType.equalsIgnoreCase("Overlaid"))  {%>
                    <td width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Graph Columns to here</font></div>
                        <div style="height:250px;overflow-y:auto">
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
                        <div style="height:250px;overflow-y:auto">
                            <ul id="sortable">
                                <%=graphCols%>
                            </ul>
                        </div>
                    </td>
                    <%}%>
                </tr>
            </form>
        </table>

        <center><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveCols('<%=grpId%>','<%=graphIds%>')"><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelCols()"></center>

        <%} catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        }
        %>
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
               // var grpColNames="";
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

                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                            //grpColNames=grpColNames+","+columnName;

                        }

                        if(cols!=""){
                            cols = cols.substring(1);
                            //grpColNames=grpColNames.substring(1);
                        }
                        //grpColNames=grpColNames.replace("%","^");

                        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                        $.ajax({
                            url: 'reportTemplateAction.do?templateParam=buildWhatIfGraphs&gid='+graphId+'&graphChange=GrpColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&grpColumnsNames='+cols+'&whatIfScenarioId=<%=whatIfScenarioId%>',
                            success: function(data){
                                parentDiv.innerHTML=data;
                            }
                        });
                        //columnsList1 = columnsList1.substring(1);
                        //columnsList1 = columnsList1+'^'+graphId;
                        //parent.document.getElementById('graphColumns').value = cols;
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
                            url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&graphChange=changeDualAxisColumns'+'&grpColumns='+cols+'&grpIds='+graphIds+'&whatIfScenarioId=<%=whatIfScenarioId%>&leftColumns='+leftgrpColNames+'&rightColumns='+rightgrpColNames,
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
                //var x=LiObj.id.replace("GrpCol","");
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
                    //childLI.style.width='180px';
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
                   // cell2.style.backgroundColor="#e6e6e6";
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
                    //childLI.style.width='180px';
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
                   // cell2.style.backgroundColor="#e6e6e6";
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
    </body>
</html>
