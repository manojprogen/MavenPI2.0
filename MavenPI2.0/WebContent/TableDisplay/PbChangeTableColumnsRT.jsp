<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,com.progen.query.RTMeasureElement,prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb,java.util.HashMap,java.util.ArrayList"%>
<%@page import="java.util.List,com.progen.reportview.db.PbReportViewerDAO,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.db.SelectDbSpecificFunc"%>

<%--
    Document   : PbChangeTableColumnsRT
    Created on : Dec 29, 2009, 12:50:59 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if ( (session.getAttribute("loadDialogs") != null && session.getAttribute("loadDialogs").equals("true") )
                 || (request.getParameter("loadDialogs") != null && request.getParameter("loadDialogs").equalsIgnoreCase("TRUE") )
                )
            {
            PbReportViewerDAO viewDAO = new PbReportViewerDAO();
            ReportTemplateDAO templateDAO = new ReportTemplateDAO();
            //String folderIds = request.getParameter("folderIds");
            //String reportId = request.getParameter("REPORTID");

            String  reportId = (String)request.getParameter("currentReportId");
            String  from = (String)request.getParameter("from");
            String  isKpiDashboard1 = (String)request.getParameter("isKPIDashboard");
            //if(request.getSession().getAttribute("currentReportId")==null || reportId.trim().equalsIgnoreCase("")){
               // reportId=(String)session.getAttribute("REPORTID");
            //}
             //added by Mohit Gupta for default locale
                   Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
                //ended By Mohit Gupta
            String folderIds = (String)request.getParameter("currentBizRoles");
            String tablistFlag = (String)request.getParameter("tableList");
            HashMap map = null;
            Container container = null;
            String graphCols = "";
            String prevColumns = "";
            HashMap ParametersHashMap = null;
            String factsHtml = "";
            ArrayList Parameters = null;
            ArrayList ParametersNames = null;
            StringBuffer prevMeasures = new StringBuffer("");
            ArrayList<String> displayColumns = null;
            ArrayList<String> displayLabels = null;
            ArrayList<String> hideColumns = null;
            ArrayList<String> summerizedColumns =new ArrayList<String>();
            ArrayList<String> summerizedLabels = new ArrayList<String>();
            String ctMeasures="";
            String sumMeasures="";
            int viewByCount = 1;
            SelectDbSpecificFunc checknull = new SelectDbSpecificFunc();
            String themeColor="blue";
            boolean isSummarized=false;
            HashMap<String, ArrayList<String>> summerizedTableHashMap=new HashMap<String, ArrayList<String>>();

            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                if (map.get(reportId) != null) {
                    container = (Container) map.get(reportId);
                    ParametersHashMap = container.getParametersHashMap();
                    isSummarized=container.isSummarizedMeasuresEnabled();
                    hideColumns = container.getReportCollect().getHideMeasures();//added by bhargavi
                    if (ParametersHashMap.get("ParametersNames") != null) {
                        Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                        ParametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
                    }

                    if (container.isReportCrosstab()) {
                        displayColumns = container.getTableDisplayMeasures();
                        displayLabels = container.getReportMeasureNames();
                    }
                    else
                    {
                        displayColumns = container.getDisplayColumns();
                        displayLabels = container.getDisplayLabels();
                    }
                    if (container.isReportCrosstab() && container.isSummarizedMeasuresEnabled()){
                         summerizedTableHashMap=container.getSummerizedTableHashMap();
                        if(summerizedTableHashMap != null &&(List<String>)summerizedTableHashMap.get("summerizedQryeIds")!=null && !((List<String>)summerizedTableHashMap.get("summerizedQryeIds")).isEmpty()){
                        summerizedColumns.addAll((List<String>)summerizedTableHashMap.get("summerizedQryeIds"));
                        summerizedLabels.addAll((List<String>)summerizedTableHashMap.get("summerizedQryColNames"));
                    }
                    }
                    if (displayColumns != null ) {
                        if (container.isReportCrosstab() && !container.isSummarizedMeasuresEnabled() ) {
                            viewByCount = 0;
                            graphCols = viewDAO.buildTableMeasures(viewByCount, displayColumns, displayLabels,true,hideColumns);
                        } else if(container.isSummarizedMeasuresEnabled()){
                            viewByCount = 0;
                            ctMeasures = viewDAO.buildTableMeasuresWithTarget(viewByCount, displayColumns, displayLabels,true,"sortable1",hideColumns);
                            if(summerizedTableHashMap != null &&(List<String>)summerizedTableHashMap.get("summerizedQryeIds")!=null && !((List<String>)summerizedTableHashMap.get("summerizedQryeIds")).isEmpty()){
                            sumMeasures= viewDAO.buildTableMeasuresWithTarget(viewByCount,summerizedColumns,summerizedLabels ,true,"sortable2",hideColumns);}

                         }else{
                            viewByCount = (container.getViewByCount());
                            if(container.isIskpidasboard() && container.isComparisionEnabled()){
                            graphCols = viewDAO.buildTableMeasures(viewByCount, displayColumns, displayLabels,false,hideColumns);
                          //graphCols = viewDAO.buildTableMeasures(viewByCount, displayColumns, displayLabels,false,hideColumns);

                            }else{
                            graphCols = viewDAO.buildTableMeasures(viewByCount, displayColumns, displayLabels,false,hideColumns);
                        }
                        }

                        for (int j = viewByCount; j < displayColumns.size(); j++) {
                            if (! RTMeasureElement.isRunTimeMeasure(displayColumns.get(j).toString()))
                            prevMeasures.append("," + String.valueOf(displayColumns.get(j)).replace("A_", ""));
                        }
                        if (!prevMeasures.toString().equalsIgnoreCase("")) {
                            prevColumns = prevMeasures.toString().substring(1);
                        }
                    }

                    if(Parameters==null){
                        Parameters=new ArrayList();
                    }
                    }
                }
             if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
            String contextPath=request.getContextPath();
         %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=9" />
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
               <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-1.8.23-custom.min.css" rel="stylesheet" />

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
         <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>

        
       <style type="text/css" >
            *{font:11px verdana;}
            .navtitle-hover{
                height:28px;
                line-height: 10%;
                 vertical-align: text-top;
}
        </style>
                <link type="text/css" href="<%=request.getContextPath()%>/css/global.css" rel="stylesheet" />
    </head>
    <body>
        <script type="text/javascript" >
            var grpColArray=new Array();
            var isSummerized='<%=isSummarized%>';
            var crossColArray=new Array();
            var summerColArray=new Array();
            var tableDispName = "";
            var isKpiDashboard = '<%=isKpiDashboard1%>'.toString();
            $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow"
                    //persist: "cookie"
                });

                //added by anitha
//                parent.$("#tabListDiv").hide();
//            parent.$("#tablistLink").hide();
//            parent.$("#goButton").hide();
//            parent.$("#tabsListIds").val("");
//            parent.$("#tabsListVals").val("");
            <%
                if(from != null && from.equalsIgnoreCase("true")){%>
                    parent.$("#tableList").attr('checked',false);
             <%   }
            %>
//            parent.$("#tableList").attr('checked',true);

                //addeb by bharathi reddy for search option
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
            var formula = $(el).attr('title');
            var measureName = document.getElementById($(el).attr('id')).innerHTML;
            var elementId = $(el).attr('id');
            switch(action){
                case "view" :{
                        contextMenuWorkFormulaView(action, el, pos);
                        break;
                    }
                case "edit" :{

                        parent.$("#tableColsDialog").dialog('close');
                        var ctxPath='<%=request.getContextPath()%>';
                        $.ajax({
                            url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&reportId='+''+'&from=viewer',
                            success:function(data) {
                                editMeasure(elementId,formula,measureName,ctxPath,<%=reportId%>);
                            }
                        });
//                            }
//                        });
                        break;

                    }
                }
            });

            //code for double click to add measures in Edit Table option of ReportViewer

                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });

             //End

     //       $(function() {

                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#selectedMeasures');
                var sort1Measures=$('#sortable1');
                var sort2Measures=$('#sortable2');


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


                $("#dropTabs").droppable({
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                }
            );
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
            if(isSummerized == 'true' || isSummerized == true){
//                alert(isSummerized)
                for(var k=0;k<prevCols.length;k++){
                var pr=crossColArray.toString();
                if(pr.match(prevCols[k])==null){
                    crossColArray.push(prevCols[k]);
                }
//                alert(crossColArray)
              }
            }



            function saveCols(){
                parent.$("#tableColsDialog").dialog('close');
                //alert('saveCols')
                // alert(isSummerized);

                var ViewFrom="<%=session.getAttribute("ViewFrom")%>";
                if(isSummerized == 'true'  || isSummerized==true){
                    var crossCols='';
                    var summCols='';
                    var crossUl=document.getElementById("sortable1");
                    var summUl=document.getElementById("sortable2");
                    if(crossUl!=undefined || crossUl!=null){
                    var crossColIds=crossUl.getElementsByTagName("li");
                    if(crossColIds!=null && crossColIds.length!=0){
                        for(var i=0;i<crossColIds.length;i++){
                            crossCols = crossCols+","+crossColIds[i].id.replace("GrpCol","");
                        }
                        if(crossCols!=""){
                            crossCols = crossCols.substring(1);
                        }
                    }
                    }
                    if(summUl!=undefined || summUl!=null){
                    var sumColIds=summUl.getElementsByTagName("li");
                    if(sumColIds!=null && sumColIds.length!=0){
                        for(var i=0;i<sumColIds.length;i++){
                            summCols = summCols+","+sumColIds[i].id.replace("GrpCol","");
                        }
                        if(summCols!=""){
                            summCols = summCols.substring(1);
                        }
                    }
                 }
                // alert(crossCols+' '+summCols);
                  $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&crossCols='+crossCols+'&summCols='+summCols+'&isSummerized='+isSummerized+'&REPORTID=<%=reportId%>',
                            success: function(data){
                                if(data=='false'){
                                    var source ="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportId%>";
                                    var dSrc = parent.document.getElementById("iframe1");
                                    dSrc.src = source;
                                }else{
                                    if(ViewFrom=="Designer"){
                                        parent.dispTable("measChange");
                                    }else

                                        if( isKpiDashboard=="true"){
                                             parent.submitFormMeasChangekpi();
                                        }else{
                                        parent.submitFormMeasChange();
                                }
                            }
                            }
                        });
                        cancelChangeGrpColumns();
                }else{
                var cols="";
                var colsUl=document.getElementById("sortable");
                 var ViewFrom="<%=session.getAttribute("ViewFrom")%>";
                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                        }
                        if(cols!=""){
                            cols = cols.substring(1);
                        }

                        $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+cols+'&REPORTID=<%=reportId%>'+'&isSummerized='+isSummerized,
                            success: function(data){
                                if(data=='false'){
                                    if(isKpiDashboard=="true"){

                                   parent.submitFormMeasChangekpi();

                                }else{
                                    var source ="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportId%>";
                                    var dSrc = parent.document.getElementById("iframe1");
                                    dSrc.src = source;
                                    }
                                }else{
                                    if(ViewFrom=="Designer"){
                                        parent.dispTable("measChange");
                                    }else

                                        if( isKpiDashboard=="true"){
                                             parent.submitFormMeasChangekpi();
                                        }else{
                                        parent.submitFormMeasChange();
                                }
                            }
                            }
                        });
                        cancelChangeGrpColumns();
                    }
                    else{
                        alert("Please Select Measures");
                        parent.$("#tableColsDialog").dialog('open');
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
            function deleteColumn1(index,tarLoc){
                //alert("calling1"+tarLoc)
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);;
                var x=index.replace("GrpCol","");
                var i=0;
                if(tarLoc=='sortable1'){
//                    alert('sortable1')
//                    alert(crossColArray);
                    for(i=0;i<crossColArray.length;i++){
                    if(crossColArray[i]==x)
                        crossColArray.splice(i,1);
                    }
//                    alert(crossColArray);
                }else if(tarLoc=='sortable2'){
//                    alert('sortable2');
                    for(i=0;i<summerColArray.length;i++){
                    if(summerColArray[i]==x)
                        summerColArray.splice(i,1);
                    }
                }
            }

            function createColumn(elmntId,elementName){
               $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getElementBussTabName&elmntId="+elmntId,function(data){
                    var jsonVar=eval('('+data+')')
                    tableDispName=jsonVar.tableDispName[0];
                });
                var parentUL=document.getElementById("sortable");
                var x=grpColArray.toString();
                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId);

                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;
                    childLI.style.width='auto';
                    childLI.style.height='28px';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    //added by Nazneen
                    childLI.addEventListener("mouseover",function(){
                         $("#GrpCol"+elmntId).attr('title', tableDispName);
                    });
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
                 $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getElementBussTabName&elmntId="+elmntId,function(data){
                    var jsonVar=eval('('+data+')')
                    tableDispName=jsonVar.tableDispName[0];
                });
                var parentUL=document.getElementById(tarLoc);
                var x=grpColArray.toString();
                if(tarLoc=='sortable1'){
                    x=crossColArray.toString();
                }else if(tarLoc=='sortable2'){
                    x=summerColArray.toString();
                }
//                alert('grpColArray:'+grpColArray);
//                alert('x--> '+x);
                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId);
                 if(tarLoc=='sortable1'){
                    crossColArray.push(elmntId);
                }else if(tarLoc=='sortable2'){
                    summerColArray.push(elmntId);
                }
                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;
                    childLI.style.width='auto';
                    childLI.style.height='28px';//already
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    //added by Nazneen
                   childLI.addEventListener("mouseover",function(){
                         $("#GrpCol"+elmntId).attr('title', tableDispName);
                    });

                    var table=document.createElement("table");
                    table.id="GrpTab"+elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);

                    var a=document.createElement("a");
                    var deleteElement = 'GrpCol'+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    if(tarLoc=='sortable1'){
                        a.href="javascript:deleteColumn1('"+deleteElement+"','sortable1')";
                    }else if(tarLoc=='sortable2'){
                        a.href="javascript:deleteColumn1('"+deleteElement+"','sortable2')";
                    }
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
              //  frameObj.style.display='none';
              parent.$("#tableColsDialog").dialog('close');
                parent.document.getElementById('fadestart').style.display='none';
            }
            function contextMenuWorkFormulaView(action, el, pos){
                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');
            }
            //added by Nazneen
            function tooltipShow(id,val){
                $("#"+id).attr('title', val);
            }
        </script>
        <form action="" name="myForm3" method="post">
        <table style="width:100%;height:250px" border="solid black 1px">

                <input type="hidden" name="prevColumns" id="prevColumns" value="<%=prevColumns%>">
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font class="gFontFamily gFontSize12" size="2" style="font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Select_Measures_from_below", cL)%></font></div>
                        <div style="height:250px;overflow:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">

                                <%--<li class="open" style="background-image:url('<%=request.getContextPath()%>/images/treeViewImages/plus.gif')">--%>
                                    <ul id="measures">
                                        <%
                                        if (session.getAttribute("PROGENTABLES") != null) {
                                            ArrayList alist = new ArrayList();
                if (map.get(reportId) != null) {
                                        if(container.getReportCollect().AOId!=null && !container.getReportCollect().AOId.equalsIgnoreCase("") )
                                            {
                                            factsHtml = templateDAO.getMeasurescustomForA0(container.getReportCollect().AOId, Parameters,request.getContextPath());
                                            }
                    else if(container.getTableList() != null && !container.getTableList().isEmpty()){
                                          factsHtml = templateDAO.getMeasuresForReport(folderIds, Parameters,request.getContextPath(),container.getTableList());
                                          container.setTableList(alist);
                                        }else if(tablistFlag != null && tablistFlag.equalsIgnoreCase("true")){
                                            factsHtml = templateDAO.getMeasures(folderIds, Parameters,request.getContextPath());
                                        }}}
                                        %>
                                        <%=factsHtml%>

                                    </ul>
                                <%--</li>--%>
                            </ul>
                        </div>
                    </td>
                     <%if(!container.isSummarizedMeasuresEnabled()) { %>
                    <td id="selectedMeasures" width="50%" valign="top">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Drag_Measures_to_here", cL)%></font></div>
                        <div style="height:250px;overflow:auto">

                            <ul id="sortable">
                                <%=graphCols %>
                            </ul>
                        </div>
                    </td>
                            <% }else{ %>
                            <td>
                            <table width="100%">
                                <tr style="height:50%">
                                    <td>
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font class="gFontFamily gFontSize12" size="2" style="font-weight:bold"></font><%=TranslaterHelper.getTranslatedInLocale("Cross_Tab_Measures", cL)%></div>
                                        <div style="overflow: auto; ">
                                        <ul id="sortable1" class="sortable" style="height:100px;color:white" >
                                            <%=ctMeasures%>

                                        </ul>
                                        </div>
                                    </td>
                </tr>
                                <tr style="height:50%">
                                    <td>
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" class="gFontFamily gFontSize12" style="font-weight:bold"><%=TranslaterHelper.getTranslatedInLocale("Summarized_Measures", cL)%></font></div>
                                        <div style="overflow: auto; ">
                                        <ul id="sortable2" class="sortable" style="height:100px;color:white" >
                                            <%=sumMeasures%>
                                        </ul>
                                        </div>
                                    </td>
                                </tr>
                            </table></td>
                            <% } %>


                </tr>
        </table>
        <table style="width:100%" align="center">
            <tr>
                <td colspan="2" style="height:10px"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="button" class="gFontFamily gFontSize12 navtitle-hover" style="width:auto" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="saveCols()">
                </td>
            </tr>
        </table>
<!--        <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
            <li class="view"><a href="#view">View</a></li>
            <li class="edit"><a href="#edit">Edit</a></li>
        </ul>-->
        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
             <table><tr><td id="value"></td></tr></table>
        </div>
        </form>

    </body>

</html>
      <%
            request.getSession().removeAttribute("loadDialogs");
            request.getSession().removeAttribute("currentReportId");
            request.getSession().removeAttribute("currentBizRoles");
            request.getSession().removeAttribute("tableList");
            }
                    %>

