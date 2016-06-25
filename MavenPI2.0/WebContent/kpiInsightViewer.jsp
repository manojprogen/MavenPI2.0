<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.Session,prg.db.PbReturnObject,prg.db.PbDb,java.util.ArrayList,com.progen.reportdesigner.db.ReportTemplateDAO,java.util.Map,com.progen.report.insights.InsightBaseDetails"%>

<%
            String themeColor = "blue";
            String dashBoardId = request.getParameter("dashBoardId");
            InsightBaseDetails baseDetails=(InsightBaseDetails)session.getAttribute("insightBaseDetails");
            String folderDetails = "";
            folderDetails = baseDetails.getBizRoles()[0];
            String url=baseDetails.getCallBackUrl();
            String contextpath=request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>

        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <script src="<%=contextpath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/quicksearch.js"></script>
        <script  type="text/javascript" src="<%=contextpath%>/javascript/jquery.contextMenu.js" ></script>

        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextpath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
<!--        <link href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">-->
        <link href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/style.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link href="<%=contextpath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <style type="text/css" >
            .ui-progressbar-value { background-image: url(images/barchart.gif); }
            .ajaxboxstyle {
                background-color:#FFFFFF;
                border: 0.1em solid #0000FF;
                height:50px;
                margin:0 0.5em;
                overflow-x:hidden;
                overflow-y:auto;
                position:absolute;
                text-align:left;
                border-top: 1px groove #848484;
                border-right: 1px inset #999999;
                border-bottom: 1px inset #999999;
                border-left: 1px groove #848484;
                background-color:#f0f0f0;
                width:450px;
            }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 110%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                overflow:auto;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }

            table.grid .collapsible {
                padding: 0 0 3px 0;
            }

            .collapsible a.collapsed {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/addImg.gif) no-repeat 3px 3px;
                outline: 0;
            }

            .collapsible a.expanded {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/deleteImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insights Page</title>
        <script type="text/javascript">
            $(document).ready(function(){
        if($.browser.msie==true)
           {
            $(".navigateDialog").dialog({
                autoOpen: false,
                height: 720,
                width: 820,
                position: 'justify',
                modal: true
            });
            $("#measuresDiv").dialog({
                autoOpen: false,
                height:520,
                width: 700,
                position: 'justify',
                modal: true
            });
            $("#customInsightDrill").dialog({
                autoOpen: false,
                height:450,
                width: 400,
                position: 'justify',
                modal: true

            });
              $("#insightsActionsDialog").dialog({
                autoOpen: false,
                height: 450,
                width: 400,
                position: 'justify',
                modal: true,
                resizable:false
            });
             $("#indicatorDiv").dialog({
                autoOpen: false,
                height:30,
                width: 450,
                position: 'justify',
                modal: true
            });

        }
        else
        {
            $(".navigateDialog").dialog({
                autoOpen: false,
                height: 620,
                width: 820,
                position: 'justify',
                modal: true
            });
            $("#measuresDiv").dialog({
                autoOpen: false,
                height:420,
                width: 700,
                position: 'justify',
                modal: true
            });
            $("#customInsightDrill").dialog({
                autoOpen: false,
                height:350,
                width: 400,
                position: 'justify',
                modal: true

            });
              $("#insightsActionsDialog").dialog({
                autoOpen: false,
                height: 350,
                width: 400,
                position: 'justify',
                modal: true,
                resizable:false
            });
            $("#indicatorDiv").dialog({
                autoOpen: false,
                height:30,
                width: 450,
                position: 'justify',
                modal: true
            });


        }


    
            });
            </script>
        
    </head>
    <body >

        <table style="width:100%">
            <tr>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <br>
        <form name="insightForm" method="post" action="">

            <div id="Insightsdiv" class="tabs" style=" max-width: 100%; cursor: auto;">
                <div class="navtitle1" style=" max-width: 100%; cursor: auto; height: 20px;"><span> <font style="font-size: 12px;font-family: verdana;"> Insights Analyzer </font></span> </div>
                <br><br>
                View : <select id="dispType" onchange="loadInsights()">
                    <option value="both">All Values</option>
                    <option value="decrease">Only decreasing values</option>
                    <option value="increase">Only increasing values</option>
                </select>
                <table align="right">
                    <tr>
                        <td><input type="button" value="Edit Indicator Measure" class="navtitle-hover" onclick="openIndicatorDiv()"/></td>
<!--                        <td><input type="button" value="Customize Drill Down"  class="navtitle-hover" onclick="displayDrilldownOptions('<%=dashBoardId%>')" style="width:150px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"></td>-->
                        <td><input type="button" value="Add Measures"  class="navtitle-hover" style="width:100px" onclick="getInsightMeasures('<%=dashBoardId%>')" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia">
                        </td>
                        <td align="right">
                        <input type="button" value="Back"  class="navtitle-hover" style="width:50px" onclick="javascript: goback()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia">
                           
                        </td>

                    </tr>
                </table>
                <br><br>
                <div id="insightsTable">
                </div>
                <script  type="text/javascript">
                    loadInsights();
                </script>
            </div>

            <div id="measuresDiv" title="Add Measures" style="display:none" >
                <table style="width:100%;height:250px" border="solid black 1px">
                    <tr>
                        <td width="50%" valign="top" class="draggedTable1">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Measures from below</font></div>
                            <div style="height:250px;overflow-y:scroll">
                                <ul id="myList3" class="filetree treeview-famfamfam">
                                    <ul id="measures" ></ul>
                                </ul>
                            </div>
                        </td>
                        <td id="selectedMeasures" width="50%" valign="top">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Measures to here</font></div>
                            <div style="height:250px;overflow-y:auto">
                                <ul id="sortable">
                                  
                                </ul>
                            </div>
                        </td>
                    </tr>

                </table>
                <table style="width:100%" align="center">
                    <tr>
                        <td colspan="2" style="height:10px"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveMeasureCols()">
                        </td>
                    </tr>
                </table>
            </div>

            <div id="insightsActionsDialog" title="Insights Mail"style="display: none">
                <table border="0" width="100%">
                    <tr>
                        <Td style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 8pt;font-weight: bold;color: #3A457C;padding-left:12px;background-color:#B4D9EE;"width="40%">To </Td>
                        <Td> <input type="text" id="toAddress" class="myTextbox5" maxlength=100 style="width:150px"></Td>
                    </tr>
                    <tr>
                        <Td style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 8pt;font-weight: bold;color: #3A457C;padding-left:12px;background-color:#B4D9EE;" width="40%">Subject</Td>
                        <Td><input type="text" id="subject" class="myTextbox5" maxlength=100 style="width:150px"></Td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <Td colspan="2"><strong>Content</strong></Td>
                    </tr>
                    <tr>
                        <Td colspan="2"><textarea rows="10" cols="35" id="mailContent" style="width: 350px;height:100px "></textarea></Td>
                    </tr>
                    <tr/>
                    <tr align="center">
                        <Td colspan="2">
                            <input class="navtitle-hover" type="button" onclick="sendInsightMail()" value="Send" style="width: auto">
                        </Td>
                    </tr>
                </table>
                <input type="hidden" id="dimData" name="dimData">
                <input type="hidden" id="dimName" name="dimName">
                <input type="hidden" id="amount" name="amount">
            </div>
            <div id="customInsightDrill" title="Edit Insight Customize Drill Down" style="display:none">
                <iframe  id="customInsightDrillFrame" NAME='customInsightDrillFrame' width="100%" height="100%" frameborder="0"   SRC='#'></iframe>

            </div>
            <div id="indicatorDiv" title="Select Indicator Measure" style="display:none">
                <br>
                <center><b>Select Indicator Measure:</b> <select id="indicator" style="width:auto"></select></center>
                <br>
                <center><input type="button" class="navtitle-hover" value="save" onclick="setIndicatorMeasure()"/></center>
            </div>
            <div id='loading' class='loading_image' style="display:none;">
                <img id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
            </div>
        </form>
                        <script type="text/javascript">

        var grpColArray=new Array();

        
            function initCollapser(divId){
                if (divId == ""){
                    $(".tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
                else{
                    $("#"+divId+" > .tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
            }

            function initClasses(){
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

                $("#sortable").sortable();

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
                //         });

                //       $(function() {

                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#selectedMeasures');

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

            }
            
            function createColumn(elmntId,elementName,tarLoc){

                var parentUL=document.getElementById(tarLoc);
                var x=grpColArray.toString();
                if(x.match(elmntId)==null){
                    grpColArray.push(elmntId);
                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;
                    childLI.style.width='220px';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.style.align='center';
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

            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);
                var x=index.replace("GrpCol","");
                var i=0;

                for(i=0;i<grpColArray.length;i++){
                    if(grpColArray[i]==x){
                        grpColArray.splice(i,1);
                    }
                        
                }
            }
            function loadInsights(){
                var dispType = $("#dispType").val();
                $("#insightsTable").html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                $.ajax({
                    url: "insights.do?reportBy=getInitialInsightsTable&displayType="+dispType,
                    success: function(data){
                        $("#insightsTable").html(data);
                        initCollapser("");
                    }
                });
            }

            function loadChildData(currRowId, dimId, childDivId, paramVals)
            {
                var dispType = $("#dispType").val();
                var currRow = $("#"+currRowId);
                var isInitialized = currRow.attr("initialized");
                $( "#"+childDivId+"prgBar").progressbar({value: 37});
                if (!isInitialized){

                    $.ajax({
                        url: "insights.do?reportBy=getInsightsDataForViewBy&viewByDim="+dimId+"&displayType="+dispType+"&paramVals="+paramVals,
                        success: function(data){
                            $( "#"+childDivId+"prgBar").remove();
                            $("#"+childDivId).html(data);
                            currRow.attr("initialized","true");
                            initCollapser(childDivId);
                        }
                    });
                }
            }

            function loadChildDrillData(currRowId, masterDimId, dimId, dimData, childDivId, paramVals, nextLevel,childDimId, expand){
          
                var dispType = $("#dispType").val();
                var currRow = $("#"+currRowId);
                var isInitialized = currRow.attr("initialized");
                $( "#"+childDivId+"prgBar").progressbar({value: 37});
                if (isInitialized && !expand)
                    return;
                if (expand){
                    var anchorImg = currRow.find("td.collapsible a");
                    if (anchorImg.hasClass("collapsed")){
                        anchorImg.removeClass("collapsed");
                        anchorImg.addClass("expanded");

                        var childRow = currRow.next();
                        if (childRow.hasClass("expand-child")){
                            childRow.find("td").show();
                        }
                    }
                }
                $.ajax({
                    url: "insights.do?reportBy=getInsightsDrillData&viewByDim="+dimId+"&dimValue="+dimData+"&displayType="+dispType+"&paramVals="+paramVals+"&nextLevel="+nextLevel+"&masterDimension="+masterDimId+"&childDimId="+childDimId,
                    success: function(data){
                        $( "#"+childDivId+"prgBar").remove();
                        $("#"+childDivId).html(data);
                        currRow.attr("initialized","true");
                        initCollapser(childDivId);
                    }
                });
            }

            function goback()
            {
                        document.forms.searchForm.action='<%=url%>';
                        document.forms.searchForm.submit();
              
            }
           
            function getInsightMeasures()
            {
                var folderIds = <%=folderDetails%>;
                $.ajax({
                    url:"insights.do?reportBy=getInsightMeasures&folderId="+folderIds+"&ctxPath=<%=request.getContextPath()%>",
                    success: function(data){
                        if(data!=""){
                            var insightData=data;
                            var availableMeasures=insightData.substring(0,insightData.indexOf('|')-1);
                            var selectedMeasures=insightData.substring(insightData.indexOf('|')+2,insightData.length);
                            $("#quicksearch").remove();
                            $("#measuresDiv").dialog('open');
                            $("#measures").html(availableMeasures);
                          
                            $("#sortable").html(selectedMeasures);
                          
                            initClasses();
                        }
                    }
                });
            }
            function saveMeasureCols()
            {
                var cols="";
                var dispType = $("#dispType").val();
                var colsUl=document.getElementById("sortable");
                if(colsUl!=undefined || colsUl!=null)
                {
                    var colIds=colsUl.getElementsByTagName("li");
                  
                    if(colIds!=null && colIds.length!=0)
                    {
                        for(var i=0;i<colIds.length;i++){
                            cols = cols+","+colIds[i].id.replace("GrpCol","");
                        }
                        if(cols!=""){
                            cols = cols.substring(1);
                        }
                        $("#loading").show();
                        $.ajax({
                            url:" insights.do?reportBy=getInitialInsightsTable&displayType="+dispType+"&changedMeasures="+cols,
                            success: function(data){
                                $("#loading").hide();
                                $("#insightsTable").html(data);
                                initCollapser("");
                            }
                        });
                         $("#measuresDiv").dialog('close');
                    }
                    else
                    {
                    alert("Please select atleast one Measure ");
                   
//                        $("#loading").show();
//                        $.ajax({
//                            url:" insights.do?reportBy=getInitialInsightsTable&displayType="+dispType+"&changedMeasures="+cols,
//                            success: function(data){
//                                $("#loading").hide();
//                                $("#insightsTable").html(data);
//                                initCollapser("");
//                            }
//                        });

                    }
                }
                
            }
            
            function goPaths(path){
                parent.closeStart();
                document.forms.insightForm.action=path;
                document.forms.insightForm.submit();
            }
            function viewReportG(path){
                document.forms.insightForm.action=path;
                document.forms.insightForm.submit();
            }
            function viewDashboardG(path){
                document.forms.insightForm.action=path;
                document.forms.insightForm.submit();
            }
            function displayDrilldownOptions(dashboardId){
                // $.ajax({
                //  url:" insights.do?reportBy=displayDrilldownOptions&dashboardId="+dashboardId,
                //  success: function(data){
                var frameObj = document.getElementById("customInsightDrillFrame");
                frameObj.src= "<%=request.getContextPath()%>/PbEditReportDrill.jsp?reportId="+dashboardId+"&fromPage=insights";
                // var reportId=dashboardId;
                $("#customInsightDrill").dialog('open');
            }
            function closeDrillDialog()
            {

                $('#customInsightDrill').dialog('close');
                loadInsights();
            }
             function openMailDialog(dimData,dimName,amount){
                 $("#toAddress").val("");
                 $("#subject").val("");
                 $("#mailContent").val("");
                $("#insightsActionsDialog").dialog('open');
                $("#dimData").val(dimData);
                $("#amount").val(amount);
                $("#dimName").val(dimName);
                
            }
            function sendInsightMail()
            {
                var dimData=$("#dimData").val();
                var dimName=$("#dimName").val();
                var mailContent=encodeURIComponent( $("#mailContent").val());
                var subject=encodeURIComponent( $("#subject").val());
                var toAddress=encodeURIComponent( $("#toAddress").val());
                var amount=$("#amount").val();
                if(toAddress=="")
                    alert("Please specify recipient email address");
                else
                {
                    $("#insightsActionsDialog").dialog('close'); 
                    $("#loading").show();
                    $.ajax({
                        url:"insights.do?reportBy=sendInsightMail&toAddress="+toAddress+"&subject="+subject+"&mailContent="+mailContent
                            +"&dimName="+dimName+"&amount="+amount+"&dimData="+dimData,
                        success:function(data){
                             $("#loading").hide();
                            alert("Mail Sent")
                        }
                    });

                }
            }
            function openIndicatorDiv()
            {
                $.ajax({
                    url:"insights.do?reportBy=getIndicatorMeasures",
                    success:function(data)
                    {
                      
                       $("#indicator").html("");
                       $("#indicator").html(data);
                    }
                });
                 $("#indicatorDiv").dialog('open');
            }
            function setIndicatorMeasure()
            {
                var indicatorId=$("select#indicator").val();
                $.ajax({
                    url:"insights.do?reportBy=setIndicatorMeasure&indicatorId="+indicatorId,
                    success:function(data)
                    {
                        
                    }
                })
                 $("#indicatorDiv").dialog('close');
                 loadInsights();
            }
        </script>
    </body>
</html>
