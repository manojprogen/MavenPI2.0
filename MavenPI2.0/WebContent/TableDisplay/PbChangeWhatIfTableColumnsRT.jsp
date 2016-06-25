<%--
    Document   : PbChangeTableColumnsRT
    Created on : Dec 29, 2009, 12:50:59 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb" %>
<%@page import="java.util.HashMap,java.util.ArrayList" %>
<%@page import="com.progen.reportview.db.PbReportViewerDAO" %>

<%
            PbReportViewerDAO viewDAO = new PbReportViewerDAO();
            String folderIds = request.getParameter("folderIds");
            String whatIfScenarioId = request.getParameter("whatIfScenarioId");
            HashMap map = null;
            Container container = null;
            String graphCols = "";
            String prevColumns = "";
            StringBuffer prevMeasures = new StringBuffer("");
            ArrayList displayColumns = null;
            ArrayList displayLabels = null;
            int viewByCount = 1;
            HashMap TableHashMap = null;
            ArrayList Measures = null;

            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(whatIfScenarioId) != null) {
                    container = (Container) map.get(whatIfScenarioId);
                    displayColumns = container.getDisplayColumns();
                    displayLabels = container.getDisplayLabels();
                    TableHashMap = container.getTableHashMap();
                    Measures = (ArrayList)TableHashMap.get("Measures");

                    if (displayColumns != null ) {
                        viewByCount = (container.getViewByCount());
                        graphCols = viewDAO.buildWhatIfTableMeasures(viewByCount, displayColumns, displayLabels, Measures);

                        for (int j = viewByCount; j < displayColumns.size(); j++) {
                            prevMeasures.append("," + String.valueOf(displayColumns.get(j)).replace("A_", ""));
                        }
                        if (!prevMeasures.toString().equalsIgnoreCase("")) {
                            prevColumns = prevMeasures.toString().substring(1);
                        }
                    }%>

<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />

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
        <script type="text/JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/watifDesign.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        

        <script type="text/javascript">
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
            });

            var prevColsStr="<%=prevColumns%>"
            var prevCols=prevColsStr.split(",");

            for(var k=0;k<prevCols.length;k++){
                var pr=grpColArray.toString();
                if(pr.match(prevCols[k])==null){
                    grpColArray.push(prevCols[k]);
                }
            }

            function saveCols(){
                parent.$("#tableColsDialog").dialog('close');
                var cols="";
                var colsUl=document.getElementById("sortable");
                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                        }
                        if(cols!=""){
                            cols = cols.substring(1);
                        }
                        alert("cols are:: "+cols)
                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=whatIfTableMeasureChanges&tableMsrs='+cols+'&whatIfScenarioId=<%=whatIfScenarioId%>',
                            success: function(data){
                                parent.submitform2();
                            }
                        });
                        cancelChangeGrpColumns();
                    }
                    else{
                        //alert("Please Select Measures");
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
                var frameObj=parent.document.getElementById("tableColsFrame");
                frameObj.style.display='none';
                parent.document.getElementById('fadestart').style.display='none';
            }
        </script>
        <style>
            *{font:11px verdana;}
        </style>
    </head>
    <body>
        <%try {
                        String Query = "select DISTINCT NVL(disp_name,sub_folder_type),sub_folder_tab_id  from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type in('Facts') and disp_name not in('Calculated Facts','Formula')";
                        String Query1 = "select DISTINCT element_id, NVL(disp_name,sub_folder_type),nvl(USER_COL_DESC, user_col_name),ref_element_id as column_name,BUSS_COL_NAME, ref_element_type,REFFERED_ELEMENTS from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type in('Facts','Formula') and use_report_flag='Y'  order by BUSS_COL_NAME, ref_element_type";

                        PbDb pbdb = new PbDb();
                        PbReturnObject pbro1 = pbdb.execSelectSQL(Query);
                        PbReturnObject pbro2 = pbdb.execSelectSQL(Query1);

        %>


        <table style="width:100%;height:250px" border="solid black 1px">
            <form name="myForm3" method="post">
                <input type="hidden" name="prevColumns" id="prevColumns" value="<%=prevColumns%>">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Measures from below</font></div>
                        <div style="height:250px;overflow-y:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">

                                <li class="open" style="background-image:url('<%=request.getContextPath()%>/images/treeViewImages/plus.gif')">
                                    <% for (int i = 0; i < pbro1.getRowCount(); i++) {
                                    %>
                                    <ul>
                                        <li class="closed"><img src='<%=request.getContextPath()%>/icons pinvoke/table.png'></img><span ><font size="1px" face="verdana"><%=pbro1.getFieldValueString(i, 0)%></font></span>
                                            <ul>
                                                <%for (int j = 0; j < pbro2.getRowCount(); j++) {
         String isit = "YES";
         if ((pbro1.getFieldValueString(i, 0).equalsIgnoreCase(pbro2.getFieldValueString(j, 1)) || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Calculated Facts") || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Formula") || pbro2.getFieldValueString(j, 1) == null || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("")) && (pbro2.getFieldValueInt(j, 0) == pbro2.getFieldValueInt(j, 3))) {

             if (pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Calculated Facts") || pbro2.getFieldValueString(j, 1).equalsIgnoreCase("Formula")) {
                 String testQuery = "select distinct sub_folder_tab_id  from prg_user_all_info_details where element_id in(" + pbro2.getFieldValueString(j, 6) + ") and sub_folder_tab_id=" + pbro1.getFieldValueInt(i, 1);

                 PbReturnObject testpbro = pbdb.execSelectSQL(testQuery);
                 if (!(testpbro.getRowCount() > 0)) {
                     isit = "NO";
                 }
             }
             if (isit.equalsIgnoreCase("YES")) {

                                                %>
                                                <li class="closed" ><img src='<%=request.getContextPath()%>/icons pinvoke/table.png'></img><span class="myDragTabs" class="ui-state-default" id="<%=pbro2.getFieldValueString(j, 0)%>" ><%=pbro2.getFieldValueString(j, 2)%></span>
                                                    <ul>
                                                        <%for (int k = 0; k < pbro2.getRowCount(); k++) {
                     if (pbro2.getFieldValueInt(j, 0) == pbro2.getFieldValueInt(k, 3) && pbro2.getFieldValueInt(j, 0) != pbro2.getFieldValueInt(k, 0)) {
                                                        %>

                                                        <li><img src='<%=request.getContextPath()%>/icons pinvoke/report.png'></img><span class="myDragTabs" class="ui-state-default" id="<%=pbro2.getFieldValueString(k, 0)%>"><%=pbro2.getFieldValueString(k, 2)%></span></li>

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
                    <td id="dropTabs" width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Measures to here</font></div>
                        <div style="height:250px;overflow-y:auto">
                            <ul id="sortable">
                                <%=graphCols%>
                            </ul>
                        </div>
                    </td>
                </tr>
            </form>
        </table>
        <table style="width:100%" align="center">
            <tr>
                <td colspan="2" style="height:10px"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveCols()">
                </td>
            </tr>
        </table>
        <%} catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
            }
        %>
    </body>
</html>
