
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.Container,java.util.*,com.progen.reportview.db.PbReportViewerDAO" %>

<%

            PbReportViewerDAO viewDAO = new PbReportViewerDAO();
            String whatIfScenarioId = request.getParameter("whatIfScenarioId");
            ////.println("whatIfScenarioId in pbTableWhatIfParameters is:::::: "+whatIfScenarioId);
            HashMap map = null;
            Container container = null;

            ArrayList MeasureIds = null;
            ArrayList MeasureNames = null;
            HashMap TableHashMap = null;
            String MeasureRegion = "";
            StringBuffer prevColumns = new StringBuffer("");
            String PrevMsrStr = null;

            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");

                if (map.get(whatIfScenarioId) != null) {
                    container = (Container) map.get(whatIfScenarioId);
                    TableHashMap = container.getTableHashMap();
                    ////.println("TableHashMap uday is:: "+TableHashMap);
                    MeasureIds = (ArrayList) TableHashMap.get("Measures");
                    MeasureNames = (ArrayList) TableHashMap.get("MeasuresNames");
                    ////.println("TableHashMap--Measures" + MeasureIds);
                    ////.println("TableHashMap--MeasureNames" + MeasureNames);

                    if (MeasureIds != null && MeasureIds != null) {
                        MeasureRegion = viewDAO.buildWhatIfTableColumns((String[]) MeasureIds.toArray(new String[0]), (String[]) MeasureNames.toArray(new String[0]), prevColumns);
                        for (int i = 0; i < MeasureIds.size(); i++) {
                            prevColumns.append("," + MeasureIds.get(i).toString().replace("A_", ""));
                        }
                        PrevMsrStr = prevColumns.toString().substring(1);
                    } else {
                        PrevMsrStr = "";
                    }
                }
            }
            String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Table Meaures</title>
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
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.slider.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/watifDesign.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/docs.js"></script>
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">
 $(document).ready(function() {
                $("#measureTree").treeview({
                    animated: "normal",
                    unique:true
                });
                $('ul#measureTree li').quicksearch({
                    position: 'before',
                    attached: 'ul#measureTree',
                    loaderText: '',
                    delay: 100
                })
            });
            </script>
     

        <style type="text/css" >
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
            if (request.getAttribute("whatIfMeasures") != null) {
                MeasuresData = String.valueOf(request.getAttribute("whatIfMeasures"));
            }
            ////.println("MeasuresData uday is:: "+MeasuresData);
        %>
        <form name="myForm2" method="post">
            <table style="width:100%;height:220px" border="solid black 1px" >
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Measures</font></div>
                        <div class="masterDiv" style="height:200px;overflow-y:scroll">
                            <ul id="measureTree" class="filetree treeview-famfamfam">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <ul id="measures"><%=MeasuresData%></ul>
                                </li>
                            </ul>
                        </div>
                    </td>
                    <td width="50%" valign="top"  id="draggableMeasures" valign="top">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Drag Measures To Here </font>
                        </h3>
                        <div id="dragDiv" style="height:200px;overflow-y:scroll">
                            <ul id="sortable">
                                <%=MeasureRegion%>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <center>
            <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveWhatIfMeasures()">
            <%--<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelMeasure()">--%>
        </center>
           <script>
            var y="";
            var xmlHttp;
            var ctxPath;

            var prevColsStr="<%=PrevMsrStr%>"
            var prevCols=prevColsStr.split(",");

            for(var k=0;k<prevCols.length;k++){
                var pr=msrArray.toString();
                if(pr.match(prevCols[k])==null){
                    msrArray.push(prevCols[k]);
                }
            }

            function saveWhatIfMeasures(){
                dispWhatIfMeasures();
            }
            function dispWhatIfMeasures(){
                var msrs="";                
                var msrUl=document.getElementById("sortable");
                var msrIds=msrUl.getElementsByTagName("li");
                
                for(var i=0;i<msrIds.length;i++){
                    var measureIds=(msrIds[i].id).replace("Msr","");
                    msrs=msrs+","+measureIds;
                }

                if(msrIds.length!=0){
                    msrs=msrs.substring(1);                    
                    var obj = document.myForm2.ChkFor;                    
                    var watifmeasureIds="";
                    var watifmeasureNames="";
                    if(obj!=undefined){
                        if(obj.length!=undefined){                     
                            for(var j=0;j<obj.length;j++)
                            {    
                                if(document.myForm2.ChkFor[j].checked==true)
                                {  
                                    var eleid=document.myForm2.ChkFor[j].value.split("-");                                    
                                    watifmeasureIds+=","+eleid[0];
                                    watifmeasureNames+="~"+eleid[1];

                                }
                            }
                        }else{
                            if(document.myForm2.ChkFor.checked==true){                               
                                var eleid=document.myForm2.ChkFor.value.split("-");
                                watifmeasureIds+=","+eleid[0];
                                watifmeasureNames+="~"+eleid[1];
                            }
                        }
                        if(watifmeasureIds.length!=0){
                            watifmeasureIds=watifmeasureIds.substring(1);
                            watifmeasureNames=watifmeasureNames.substring(1);
                        }
                    }
                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;

                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=buildWhatIfTable&buildTableChange=Measures&Msrs='+watifmeasureIds+'&MsrsNames='+watifmeasureNames+'&whatIfScenarioId='+parent.document.getElementById("whatIfScenarioId").value,
                        success: function(data) {
                            parent.PreviewTable();                            
                        }
                    });                    
                    //parent.saveTabMeasure(watifmeasureIds,watifmeasureNames);
                    parent.cancelTabMeasure();
                }
                else{
                    alert("Please Select Measures")
                }
            }
            function cancelMeasure(){
                parent.cancelTabMeasure();
            }
            <%--function cancelMeasure(){

                var obj = document.myForm2.ChkFor;                
                var watifmeasureIds="";
                var watifmeasureNames="";
                if(obj!=undefined){
                    if(obj.length!=undefined){

                        for(var j=0;j<obj.length;j++)
                        {   
                            if(document.myForm2.ChkFor[j].checked==true)
                            {  
                                var eleid=document.myForm2.ChkFor[j].value.split("-");
                                watifmeasureIds+=","+eleid[0];
                                watifmeasureNames+="~"+eleid[1];
                            }
                        }
                    }else{


                        if(document.myForm2.ChkFor.checked==true){                            
                            var eleid=document.myForm2.ChkFor.value.split("-");
                            watifmeasureIds+=","+eleid[0];
                            watifmeasureNames+="~"+eleid[1];
                        }
                    }
                    if(watifmeasureIds.length!=0){
                        watifmeasureIds=watifmeasureIds.substring(1);
                        watifmeasureNames=watifmeasureNames.substring(1);
                    }
                }
            
                parent.cancelTabMeasure(watifmeasureIds,watifmeasureNames);
            }--%>

           

            $(function() {
                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#draggableMeasures');

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
                        createMeasures(measure,ui.draggable.attr('id'));

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
    </body>
</html>
