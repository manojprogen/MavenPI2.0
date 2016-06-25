
<%@page import="com.progen.report.pbDashboardCollection"%>
<%@page import="com.progen.report.PbReportCollection"%>
<%@page import="com.progen.reportdesigner.db.DashboardTemplateDAO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb" %>
<%@page import="java.util.HashMap,java.util.ArrayList" %>
<%@page import="com.progen.reportview.db.PbReportViewerDAO" %>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO" %>
<%@page import="com.progen.db.SelectDbSpecificFunc" %>

<%          //for clearing cache
                      String isEdit = request.getParameter("editMap");
                        if (isEdit == null) {
                            isEdit = "false";
                        }

                        response.setHeader("Cache-Control", "no-store");
                        response.setHeader("Pragma", "no-cache");
                        response.setDateHeader("Expires", 0);
                        //if (request.getSession().getAttribute("loadDialogs") != null && request.getSession().getAttribute("loadDialogs").equals("true")) {

                        String reportId = request.getParameter("REPORTID");
                        String folderIds = request.getParameter("folderIds");
                        String dashboardId = request.getParameter("dashboardId");
                        String divId = request.getParameter("divId");
                        String sortType = request.getParameter("sortType");
                        String mapView = request.getParameter("mapView");
                        String geoView = request.getParameter("geoView");

                       //String sortValue = request.getParameter("sortValue");
                        String reportType = "R";
                        if (dashboardId != null) {
                            reportType = "D";
                            reportId = dashboardId;
                        }

                        HashMap map = null;
                        Container container = null;
                        String factsHtml = "";

                        String prevColumns = "";

                        String themeColor = "blue";

                        List<String> measurename = new ArrayList<String>();
                        List<String> elementid = new ArrayList<String>();
                        HashMap ParametersHashMap = null;
                        ArrayList Parameters = null;
                        ReportTemplateDAO templateDAO = new ReportTemplateDAO();
                        List<String> mapSupportingMeasures = new ArrayList<String>();
                        List<String> mapSupportingMeasureLabel = new ArrayList<String>();
                        ArrayList<String> selectedMeasures = new ArrayList<String>();

                        if (session.getAttribute("PROGENTABLES") != null) {
                            map = (HashMap) session.getAttribute("PROGENTABLES");

                            if (dashboardId == null) {
                                if (map.get(reportId) != null) {
                                    container = (Container) map.get(reportId);
                                } else {
                                    container = new Container();
                                }
                            } else {
                                container = (Container) map.get(dashboardId);
                            }

                            if (session.getAttribute("theme") == null) {
                                session.setAttribute("theme", themeColor);
                            } else {
                                themeColor = String.valueOf(session.getAttribute("theme"));
                            }

                            if (dashboardId == null) {
                                if (container.getMapMainMeasure() != null) {

                                    elementid = container.getMapMainMeasure();
                                    measurename = container.getMapMainMeasureLabel();
                                }

                                if (container.getMapSupportingMeasures() != null) {

                                    mapSupportingMeasures = container.getMapSupportingMeasures();
                                    mapSupportingMeasureLabel = container.getMapSupportingMeasuresLabels();
                                }
                            } else {
                                if (isEdit.equalsIgnoreCase("true")) {
                                    if (container.getMapMainMeasure() != null) {

                                        elementid = container.getMapMainMeasure();
                                        measurename = container.getMapMainMeasureLabel();
                                    }
                                    if (container.getMapSupportingMeasures() != null) {

                                        mapSupportingMeasures = container.getMapSupportingMeasures();
                                        mapSupportingMeasureLabel = container.getMapSupportingMeasuresLabels();
                                    }
                                }
                            }
                            String enableViewSelection = "";
                            if (session.getAttribute("TopBtmEnable") != null)
                                enableViewSelection = session.getAttribute("TopBtmEnable").toString();

                            if ("D".equalsIgnoreCase(reportType) && "".equals(folderIds)){
                                pbDashboardCollection dbCollect = (pbDashboardCollection )container.getReportCollect();
                                folderIds = dbCollect.reportBizRoles[0];
                            }

                            String TopBtmEnable = "none";
                            int columnSize = 2;
                            String alertString = "You cannot add more than two column in main measure";
                            if(enableViewSelection=="block"){
                                columnSize = 1;
                                alertString = "You cannot add more than one column in main measure";
                            }

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.3.2.min.js"></script>-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />

        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link href="<%=request.getContextPath()%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />


        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.simplemodal-1.1.1.js" ></script>
        <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js" ></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css" >
            *{font:11px verdana;}
        </style>

        <script type="text/javascript" >

            var grpColArray=new Array();
            $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow"
                    //persist: "cookie"
                });


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


                // $(function() {
                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                //var dropMeasures=$('#selectedMeasures');
                $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                $("#sortable1").droppable(

                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable1");
                    }

                }
            );
                $("#sortable2").droppable({
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable2");
                    }
                }
            );

             // grpColArray.push('<%=elementid%>,<%=measurename%>');
              <%if(!elementid.isEmpty()){
                  for(int i = 0; i < elementid.size(); i++){
              %>
                       grpColArray.push('<%=elementid.get(i).substring(2)%>,<%=measurename.get(i).trim()%>')
                       <%}
                  }%>
            <%if (!mapSupportingMeasures.isEmpty()) {
                                for (int i = 0; i < mapSupportingMeasures.size(); i++) {
            %>
                    grpColArray.push('<%=mapSupportingMeasures.get(i).substring(2)%>,<%=mapSupportingMeasureLabel.get(i).trim()%>')
            <%}
                            }%>


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
                                   parent.$("#MapMeasures").dialog('close');
                                    //var parentDiv = parent.document.getElementById('previewDispGraph');
                                    var cols="";
                                    var colids = "";
                                    var colnames = "";
                                    var mainMeasure = "";
                                    var suppMeasures = "";
                                    var divId = '<%=divId%>';
                                    var reportType = '<%=reportType%>';
                                    var colsUl=document.getElementById("sortable");

                                    var leftcolsUl=document.getElementById("sortable1");
                                    var rightcolsUl=document.getElementById("sortable2");

                                    var measvalues =  new Array();;
                                    var mainmeasurename = "";
                                    var measid = new Array();
                                    var measname = new Array();
                                    var measureIdString = "";
                                    var measureNameString = "";
                                    var mainMeasureIdString = "";
                                    var mainMeasureLabelString = "";
                                    var suppMeasureIdString = "";
                                    var suppMeasureLabelString = "";

                                    if(colsUl!=undefined || colsUl!=null){
                                    }
                                    else{
                                        var leftcolIds=leftcolsUl.getElementsByTagName("li");
                                        var rightcolIds=rightcolsUl.getElementsByTagName("li");

                                        if(leftcolIds!=null && leftcolIds.length!=0){
                                            for(var i=0;i<leftcolIds.length;i++){
                                                //leftgrpColNames=leftgrpColNames+","+(leftcolIds[i].id).replace("GrpCol","");
                                                mainMeasure = leftcolIds[i].id.replace("GrpCol","");
                                                measvalues = mainMeasure.split(",");
                                                measureIdString = measureIdString+","+measvalues[0];
                                                mainMeasureIdString = mainMeasureIdString+","+measvalues[0];
                                                cols = cols+","+measvalues[0];
                                                measureNameString = measureNameString+","+measvalues[1];
                                                mainMeasureLabelString = mainMeasureLabelString+","+measvalues[1];

                                            }
                                            for(var i=0;i<rightcolIds.length;i++){
                                                //rightgrpColNames=rightgrpColNames+","+(rightcolIds[i].id).replace("GrpCol","");
                                                //suppMeasures = suppMeasures + "," + rightcolIds[i].id.replace("GrpCol","");
                                                suppMeasures = rightcolIds[i].id.replace("GrpCol","");
                                                measvalues = suppMeasures.split(",");
                                                suppMeasureIdString = suppMeasureIdString+","+measvalues[0];
                                                cols = cols+","+measvalues[0];
                                                measureIdString = measureIdString+","+measvalues[0];
                                                measureNameString = measureNameString+","+measvalues[1];
                                                suppMeasureLabelString = suppMeasureLabelString+","+measvalues[1];
                                            }

                                            if(measureIdString!=""){
                                                measureIdString = measureIdString.substring(1);
                                                mainMeasureIdString = mainMeasureIdString.substring(1);
                                            }
                                            if(measureNameString!=""){
                                                measureNameString = measureNameString.substring(1);
                                                mainMeasureLabelString = mainMeasureLabelString.substring(1);
                                            }
                                            if(suppMeasureIdString!=""){
                                                suppMeasureIdString = suppMeasureIdString.substring(1);
                                                suppMeasureLabelString = suppMeasureLabelString.substring(1);
                                            }

                                            //cols=mainMeasure+","+suppMeasures;
                                           // cols = encodeURIComponent(cols);
                                            measureNameString = encodeURIComponent(measureNameString);

                                            var editMap = '<%=isEdit%>';
                                            if(suppMeasures!=""){

                                                $.post('<%=request.getContextPath()%>/mapAction.do?reportBy=mapColumnChanges&mapMeasureids='+measureIdString+'&mapMeasurename='+measureNameString+'&REPORTID=<%=reportId%>&editMap='+editMap+'&reportType='+reportType+'&divId='+divId+'&mainMeasures='+mainMeasureIdString+'&mainMeasureLabels='+encodeURIComponent(mainMeasureLabelString)+'&supportingMeasures='+suppMeasureIdString+"&suppMeasureLabels="+encodeURIComponent(suppMeasureLabelString),function(data){
                                                        parent.submitFormMapMeasChange(divId,reportType,'<%=sortType%>','<%=mapView%>','<%=geoView%>','<%=reportId%>');
                                                });
                                            }
                                            else{
                                                $.post('<%=request.getContextPath()%>/mapAction.do?reportBy=mapColumnChanges&mapMeasureids='+measureIdString+'&mapMeasurename='+measureNameString+'&REPORTID=<%=reportId%>&editMap='+editMap+'&reportType='+reportType+'&divId='+divId+'&mainMeasures='+mainMeasureIdString+'&mainMeasureLabels='+encodeURIComponent(mainMeasureLabelString)+'&supportingMeasures='+suppMeasureIdString+"&suppMeasureLabels="+encodeURIComponent(suppMeasureLabelString),function(data){
                                                        parent.submitFormMapMeasChange(divId,reportType,'<%=sortType%>','<%=mapView%>','<%=geoView%>','<%=reportId%>');
                                                });
                                            }

                                            //cancelChangeGrpColumns();
                                        }
                                        else{
                                            if((leftcolIds==null || leftcolIds.length==0)){
                                                alert("Please Select Main Measure")
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
                                        if(grpColArray[i]==x){
                                            grpColArray.splice(i,1);
                                    }
                                    }
                                }

                                    //if((liobj.length<'<%//=columnSize%>'&&tarLoc=="sortable1")||tarLoc=="sortable2"){
                                    //if((tarLoc=="sortable1")||tarLoc=="sortable2"){

                                function createColumn(elmntId,elementName,tarLoc){
                                    var ulobj=document.getElementById("sortable1");
                                    var liobj=ulobj.getElementsByTagName("li");
                                    if((liobj.length<'<%=columnSize%>'&&tarLoc=="sortable1")||tarLoc=="sortable2"){
                                        var parentUL=document.getElementById(tarLoc);
                                        var x=grpColArray.toString();

                                        if(x.match(elmntId)==null){
                                            grpColArray.push(elmntId+","+elementName);
                                            var childLI=document.createElement("li");
                                            childLI.id='GrpCol'+elmntId+","+elementName;

                                            childLI.style.width='auto';
                                            childLI.style.height='auto';
                                            childLI.style.color='white';
                                            childLI.className='navtitle-hover';
                                            var table=document.createElement("table");
                                            table.id="GrpTab"+elmntId;
                                            var row=table.insertRow(0);
                                            var cell1=row.insertCell(0);

                                            var a=document.createElement("a");
                                            var deleteElement = 'GrpCol'+elmntId+","+elementName;
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
                                    else{
                                       alert("You cannot add more than one column in main measure");
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

    </head>
    <body>

            <form  action="" name="myForm3" method="post">
                <input type="hidden" name="prevColumns" id="prevColumns" value="<%=prevColumns%>">
             <table style="width:100%;height:270px" border="solid black 1px">
			<tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Map Columns from below</font></div>
                        <div style="height:250px;overflow:scroll">
                            <ul id="myList3" class="filetree treeview-famfamfam">

                                <%--<li class="open" style="background-image:url('<%=request.getContextPath()%>/images/treeViewImages/plus.gif')">--%>
                                <ul id="measures">
                                    <%                ParametersHashMap = container.getParametersHashMap();
                                                    if (ParametersHashMap.get("ParametersNames") != null || ParametersHashMap.get("ParameterNames") != null) {
                                                        Parameters = (ArrayList) ParametersHashMap.get("Parameters");
                                                        factsHtml = templateDAO.getMeasures(folderIds, Parameters, request.getContextPath());
                                                    }%>
                                    <%=factsHtml%>

                                </ul>
                                <%--</li>--%>
                            </ul>
                        </div>
                    </td>

                    <td width="50%" valign="top" >
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Map Columns to here</font></div>
                        <div style="height:250px;overflow:auto">
                            <table width="100%">
                                <tr style="height:50%">
                                    <td  id="selectedMeasures" >
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Main Measure</font></div>

                                        <ul id="sortable1" class="sortable" style="height:50px;color:white" >
                                            <%                 int j=0;
                                                            if (container.getMapMainMeasure().size()!=0) {
                                                                for(String eleId : elementid){
                                                                    if(eleId.startsWith("A_")){
                                                                        eleId = eleId.substring(2);
                                                                        }
                                                                    selectedMeasures.add(eleId);

                                            %>
                                            <li id="GrpCol<%=eleId%>,<%=measurename.get(j)%>" style="width: auto; height: auto; color: white;" class="navtitle-hover"><table id="GrpTab<%=eleId%>"><tbody><tr><td><a href="javascript:deleteColumn('GrpCol<%=eleId%>,<%=measurename.get(j)%>')"class="ui-icon ui-icon-close">a</a></td><td style="color: black;"><%=measurename.get(j)%></td></tr></tbody></table></li>

                                            <%   j++;
                                                       }
                        }%>

                                        </ul>

                                    </td>
                                </tr>
                                <tr style="height:50%">
                                    <td  id="selectedMeasures1" >
                                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Supporting Measures</font></div>
                                        <ul id="sortable2" class="sortable" style="height:100px;color:white" >
                                            <%
                                                            if (container.getMapSupportingMeasures() != null) {
                                                                for (int i = 0; i < mapSupportingMeasures.size(); i++) {
                                                                    String mapsuppmeas = mapSupportingMeasures.get(i).substring(2);
                                                                    selectedMeasures.add(mapsuppmeas);
                                            %>

                                            <li id="GrpCol<%=mapsuppmeas%>,<%=mapSupportingMeasureLabel.get(i)%>" style="width: auto; height: auto; color: white;" class="navtitle-hover"><table id="GrpTab<%=mapsuppmeas%>"><tbody><tr><td><a href="javascript:deleteColumn('GrpCol<%=mapsuppmeas%>,<%=mapSupportingMeasureLabel.get(i)%>')"class="ui-icon ui-icon-close">a</a></td><td style="color: black;"><%=mapSupportingMeasureLabel.get(i)%></td></tr></tbody></table></li>
                                            <%}%>
                                            <%}%>
                                        </ul>
                                    </td>
                                </tr>
                            </table>
                        </div>

                    </td>

                </tr>
                </table>
            </form>
        <table style="width:100%" align="center">
            <tr>
                <td colspan="2" style="height:10px"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveCols()"/>
                    <%--<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelCols()">--%>
                </td>
            </tr>
        </table>

        <div id="formulaViewDiv" title="View Custom Measure" style="display:none">

            <table >
                <tr>
                    <td id="value"></td>
                </tr>
            </table>

        </div>
    </body>
</html>
       <%
                    }
                    request.getSession().removeAttribute("folderIds");
                    request.getSession().removeAttribute("grpIds");
                    request.getSession().removeAttribute("graphId");
                    request.getSession().removeAttribute("REPORTID");
                    request.getSession().removeAttribute("loadDialogs");
        %>