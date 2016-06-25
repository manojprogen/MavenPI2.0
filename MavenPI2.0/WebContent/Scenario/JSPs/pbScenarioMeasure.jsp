<%@page import="com.progen.scenariodesigner.db.ScenarioTemplateDAO"%>

<%
            //String foldersIds = request.getParameter("foldersIds");
            //////////////////////////////////////////.println.println(" foldersIds in m js p " + foldersIds);
%>

<%@page import="java.util.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO" %>
<%@page import="prg.db.PbReturnObject,prg.db.Container" %>
<%
            String MeasureRegion = "";
            StringBuffer prevColumns = new StringBuffer("");
            String PrevMsrStr = null;
            HashMap map = null;

            ArrayList MeasureIds = null;
            ArrayList MeasureNames = null;
            HashMap TableHashMap = null;
            Container container = null;
            
            ScenarioTemplateDAO tempDAO = new ScenarioTemplateDAO();

            String scenarioId = "";
            String scenarioName = "";
            scenarioName = (String) request.getAttribute("scenarioName");            

            if (session.getAttribute("SCENARIOTAB") != null) {
                map = (HashMap) session.getAttribute("SCENARIOTAB");
                if (map.get(scenarioName) != null) {
                    container = (Container) map.get(scenarioName);
                    TableHashMap = container.getTableHashMap();
                    MeasureIds = (ArrayList) TableHashMap.get("Measures");
                    MeasureNames = (ArrayList) TableHashMap.get("MeasuresNames");
                    ////////////////////////////////////////.println.println("MeasureIds are:::::::::::::::::::::: "+MeasureIds);
                    ////////////////////////////////////////.println.println("MeasureNames are:::::::::::::::::::: "+MeasureNames);
                    if (MeasureIds != null && MeasureIds != null) {
                        MeasureRegion = tempDAO.buildTableColumns((String[]) MeasureIds.toArray(new String[0]), (String[]) MeasureNames.toArray(new String[0]), prevColumns);
                        for (int i = 0; i < MeasureIds.size(); i++) {
                            prevColumns.append("," + MeasureIds.get(i).toString().replace("A_", ""));
                        }
                        PrevMsrStr = prevColumns.toString().substring(1);
                    } else {
                        PrevMsrStr = "";
                    }
                }
            }

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Table Meaures</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>

        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>


        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>



        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=request.getContextPath()%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />

        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></script>

        <script>
            var y="";
            var xmlHttp;
            var ctxPath;
            var msrArray=new Array();

            var prevColsStr="<%=PrevMsrStr%>"
            var prevCols=prevColsStr.split(",");

            for(var k=0;k<prevCols.length;k++){
                var pr=msrArray.toString();
                if(pr.match(prevCols[k])==null){
                    msrArray.push(prevCols[k]);
                }
            }

            function saveMeasures(){  
                dispMeasures();
            }
            function dispMeasures(){
                var msrs="";
                var path=document.getElementById('path').value;
                var msrUl=document.getElementById("sortable");
                var msrIds=msrUl.getElementsByTagName("li");
                var measureNames = "";
                var temp;
                for(var i=0;i<msrIds.length;i++) {
                    temp = msrIds[i].getElementsByTagName("table")[0].getElementsByTagName("tr")[0].getElementsByTagName("td")[1].innerHTML;
                    if(measureNames=="") {
                        measureNames = temp;
                    }else {
                        measureNames = measureNames + "," + temp;
                    }
                }
                
                var scenarioName=document.getElementById('scenarioName').value;                
                for(var i=0;i<msrIds.length;i++){
                    var measureIds=(msrIds[i].id).replace("Msr","");
                    msrs=msrs+","+measureIds;                   
                }

                if(msrIds.length!=0){
                    msrs=msrs.substring(1);                 
                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;                    
                    $.ajax({
                        url: path+'/ScenarioTemplateAction.do?scnTemplateParam=buildScenarioTable&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+measureNames+'&scenarioName='+scenarioName,
                        success: function(data) {                          
                            if(data!=""){                                
                            }
                        }
                    });
                    parent.cancelTabMeasure();
                }
                else{
                    alert("Please Select Measures")
                }
            }
            function cancelMeasure(){
                parent.cancelTabMeasure();
            }


            $(document).ready(function() {
                $("#measureTree").treeview({
                    animated: "normal",
                    unique:true
                });
            });

            $(function() {
                var dragMeasure=$('#measures > li > ul > li > span')
                var dropMeasures=$('#draggableMeasures');

                $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropMeasures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > span',
                    drop: function(ev, ui) {
                        var measure=ui.draggable.html();
                        createMeasures(ui.draggable.html(),ui.draggable.attr('id'));
                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
            function prevMeasures(){
                var prevMsrs=parent.document.getElementById("Measures").value;
                if(prevMsrs.length!=0){
                    prevMsrs=prevMsrs.split(",");
                    for(var m=0;m<prevMsrs.length;m++){
                        var msrElmnts=prevMsrs[m].split("-");
                        createMeasures(msrElmnts[0],"elmnt-"+msrElmnts[1]);
                    }
                }
            }

        </script>

        <style>
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#EAF2F7;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
        </style>
    </head>
    <body>
        <%-- <body onload="javascript:prevMeasures();">--%>
        <%
            String MeasuresData = "";
            String path = request.getContextPath();
            if (request.getAttribute("Measures") != null) {
                MeasuresData = String.valueOf(request.getAttribute("Measures"));
            }


        %>
        <form name="myForm2" method="post">
            <INPUT TYPE="hidden" name="path" id="path" value="<%=path%>">
            <INPUT TYPE="hidden" name="scenarioName" id="scenarioName" value="<%=scenarioName%>">
            <br>
            <table style="width:100%;height:150px" border="solid black 1px" >
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Measures</font></div>
                        <div class="masterDiv" style="height:200px;overflow-y:auto">
                            <ul id="measureTree" class="filetree treeview-famfamfam">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <ul id="measures">
                                        <%=MeasuresData%>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td width="50%" valign="top"  id="draggableMeasures" valign="top">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Drag Measures To Here </font>
                        </h3>
                        <div id="dragDiv" style="height:200px;overflow-y:auto">
                            <ul id="sortable">
                                <%=MeasureRegion%>                                
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <br>
        <center>
            <table>
                <tr>
                    <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveMeasures()"></td>
                    <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelMeasure()"></td>
                </tr>
            </table>
        </center>
    </body>
</html>
