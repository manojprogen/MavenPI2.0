<%
        String themeColor = "blue";
        if (session.getAttribute("theme") == null) {
            session.setAttribute("theme", themeColor);
        } else {
            themeColor = String.valueOf(session.getAttribute("theme"));
        }
        String dashBoardId =request.getParameter("dashBoardId");
        String scoreCardId=String.valueOf(session.getAttribute("ScoreCardId"));
        String contextpath=request.getContextPath();

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery.bubblepopup.v2.3.1.min.js"></script>

        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">

        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/javascript/lib/jquery/css/jquery.bubblepopup.v2.3.1.css" rel="stylesheet" />
       <link rel="stylesheet" href="<%=contextpath%>/tablesorter/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">
        <style type="text/css">
            .ui-progressbar-value { background-image: url(images/barchart.gif); }
            .ajaxboxstyle {
                background-color:#FFFFFF;
                border: 0.1em solid #0000FF;
                height:50px;
                margin:0 0.5em;               
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
            .myHead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#b4d9ee;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Scorecard</title>
        
    </head>
    <body>
        <table style="width:100%">
            <tr>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <br>
        <form  name="scorecardForm" id="scorecardForm" action="" method="post">
        <div id="scarddiv" class="tabs" style=" max-width: 100%; cursor: auto;">
            <div class="navtitle-hover" style=" max-width: 100%; cursor: auto; height: 20px;"><span> <font style="font-size: 12px;font-family: verdana;"> Scorecard </font></span> </div>
            <br>

        <div id="paramRegion">
            <table id="paramRegionTable" width="100%">
                <tr>
                    <td align="left" style="width: 44%"><table id ="paramDiv"></table></td>
                    <td id="goButtonTd" style="width: 5%;display:none"><input type="button" onclick="loadScorecard()" value="Go" class="navtitle-hover"></td>
                    <td  align="right"  style="width: 43%;"><input type="button"  class="navtitle-hover"  style="width: 20%;" value="Add Adhoc Rows" onclick="addAdhocRows()"></td>
                    <td align="right"><a onclick="javascript:gotoScorecardHome()" style="cursor:pointer">Scorecard Home</a></td>
                </tr>
                <script  type="text/javascript">
                    loadParamRegion();
                </script>
            </table>
        </div>
            <div id="scorecardTable">
            </div>
        </div>

            <div id="scoreCardActionTypesDialog" title="Select Action" style="display:none">
                       <br>
                <table border="0">
                    <tr>
<!--                        <td><input type="radio" id="pastOrNew" name="pastOrNew" value="new"> Create New Action</td>-->
                        <td>
                           <select class="myTextbox5"  name="ActionTypes" id="ActionTypes" >
                           </select>
                        </td>
                    </tr>
                    <tr/>
<!--                    <tr><td><input type="radio" id="pastOrNew" name="pastOrNew" value="past"> View Past action</td></tr>-->
                </table>
                <br> <br>
                <center><input type="button" class="navtitle-hover" value="Go" onclick="showActions()" style="width: 15%" ></center>
            </div>

            <div id="scoreCardActionsDialog" title="Score Card Action" style="display:none">
                <iframe  id="scardActions" NAME='ScoreCard Action'  width="100%" height="100%" frameborder="0" SRC=''></iframe>
            </div>

            <div id="pastScoreCardActionsDialog" title="Past Score Card Action" style="display:none">
                <iframe  id="scardPastActions" NAME='ScoreCard Action'  width="100%" height="100%" frameborder="0" SRC=''></iframe>
            </div>

            <div id="scardHistoryDialog" title="Score Card History" style="display:none">
                <iframe  id="scardHistory" NAME='ScoreCard'  width="100%" height="100%" frameborder="0" SRC=''></iframe>
            </div>
            
            
            </form>
        <div id="adHocDialog" style="display: none" title="Adhoc Rows">
                <form id="adHocForm" name="adHocForm" method="post" action="">
                <div id="actualMeasDiv">

                </div>
                <div id="addHocRowsDiv"  align='center'>
                    <table align="center" width="100%">
                        <tr>
                            <td align='center' ><font style="font-weight: bolder">Adhoc Rows</font></td>
                        </tr>
                    </table>
                    <table id="addHocTable" align="center" class="tablesorter" width="100%" >
                        <thead>
                            <tr>
                                <th align="center">Name</th>
                                <th align="center">Score</th>
                                <th align="center">Weightage</th>
                            </tr>
                        </thead>
                        <tbody id="addHocTableBody">
                        <tr id="row0">
                            <td align="center">
                                <input type="text" id="adHocMeas0" name='adHocMeas' value="">
                            </td>
                            <td align="center">
                                <input type="text" id="adHocScore0" name='adHocScore' value="">
                            </td>
                            <td align="center">
                                <input type="text" id="adHocWeigh0" name='adHocWeight' value="0" onchange='calculateTotalWeightage()'>
                            </td>
                        </tr>
                        </tbody>
                  </table>
                    <br>
                    <table>
                        <tr>
                            <td class="myHead" height='20px' align="center">Total Weightage</td>
                           <td id="totWeighTd" class="myHead" align="center">100</td>
                       </tr>
                    </table>
                    <br>
                   <table align="center" cellspacing="7">

                        <tr>
                            <td  align="center"><input type="button"  value="Add Row" onclick="addRow()" class="navtitle-hover"></td>
                            <td  align="center"><input type="button" value="Delete Row" onclick="deleteRow()" class="navtitle-hover"></td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center"><input type="button" style='width:60px'  value="Save" onclick="saveAdhocRows()" class="navtitle-hover"></td>
                        </tr>
                    </table>
                    <center> <font id="warningText" style="color: red;display: none;">Total Weightage should be 100</font></center>
                </div>
                </form>
            </div>
          <script type="text/javascript">

            var actionTypesInitialized = false;
            var selectedScoreCard;
            var selectedMember;
            var currentScore;

            $(document).ready(function(){

                $(".navigateDialog").dialog({
                    autoOpen: false,
                    height: 620,
                    width: 820,
                    position: 'justify',
                    modal: true
                });

                if ($.browser.msie == true){
                    $("#scoreCardActionTypesDialog").dialog({
                        autoOpen: false,
                        height: 150,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scoreCardActionsDialog").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#pastScoreCardActionsDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scardHistoryDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#adHocDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                }
                else{
                    $("#scoreCardActionTypesDialog").dialog({
                        autoOpen: false,
                        height: 150,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scoreCardActionsDialog").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#pastScoreCardActionsDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    $("#scardHistoryDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#adHocDialog").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 700,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                }
            });

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

                $(".bubbleAnchor").each(function(i){
                    var tooltip = $(this).attr("tooltip");
                    $(this).CreateBubblePopup({
                        selectable: false,
                        position : 'top',
                        align	 : 'center',
                        innerHtml : tooltip,
                        themeName: 	'all-grey',
                        themePath: 	'images/jquerybubblepopup-theme'
                    });
                });
            }

            function loadScorecard(){
                var dateval = $("#datepicker11").val();
                var cmpval = $("#CBO_PRG_COMPARE").val();
                var duration = $("#CBO_PRG_PERIOD_TYPE").val();

                $("#scorecardTable").html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                $.ajax({
                    url: 'scoreCardViewer.do?reportBy=loadScorecard&dateVal='+dateval+'&cmpVal='+cmpval+'&duration='+duration,
                    success: function(data){
                        $("#scorecardTable").html(data);
                        initCollapser("");
//                        $(function() {
//                            $('tr.parent')
//                                    .css("cursor","pointer")
//                                    .attr("title","Click to expand/collapse")
//                                    .click(function(){
//                                            $(this).siblings('.child-'+this.id).toggle();
//                                    });
//                            $('tr[class^=child-]').hide().children('td');
//                        });
                    }
                });
            }

            function viewHistory(scardId, scardMemId){
                var ctxPath = '<%= request.getContextPath() %>';
                $("#scardHistoryDialog").dialog('open');
                var timeLevel = $("#CBO_PRG_PERIOD_TYPE").val();
                var source = ctxPath+"/scoreCardHistory.jsp?timeLevel="+timeLevel+"&scorecardId="+scardId+"&scorecardMemberId="+scardMemId;

                var actionsFrame = document.getElementById("scardHistory");
                actionsFrame.src = source;
            }

            function shwDate(){
                $('#datepicker1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
            }

            function copyDate(eleId){
                document.getElementById(eleId+"1").value = document.getElementById(eleId).value;
            }

            function loadParamRegion(){
                $.ajax({
                    url: 'scoreCardViewer.do?reportBy=loadParamRegion',
                    success: function(data){
                        $("#paramDiv").html(data);
                        shwDate();
                        $("#goButtonTd").show();
                        loadScorecard();
                    }
                });
            }

            function showActionTypesDialog(scoreCardId, scorecardMemberId, score){
                selectedScoreCard = scoreCardId;
                selectedMember = scorecardMemberId;
                currentScore = score;
                if (actionTypesInitialized == false){
                    $.ajax({
                        url: 'scoreCardViewer.do?reportBy=getScorecardActionTypes',
                        success: function(data){
                            if(data!=""){
                                var json = eval('('+data+')');
                                var actionTypes = json.ActionType;
                                var dispLbls = json.DisplayLabel;
                                var html = "";
                                for (var i=0;i<actionTypes.length;i++){
                                    html = html + "<option value='"+actionTypes[i]+"'>"+dispLbls[i]+"</option>";
                                }

                                $("#ActionTypes").html(html);
                                actionTypesInitialized = true;
                            }
                        }
                    });
                }

                $("#scoreCardActionTypesDialog").dialog('open');
            }

            function showActions(scoreCardId, scorecardMemberId, score,elementName){
                //$("#scoreCardActionTypesDialog").dialog('close');
                var action = "Email";//$("#ActionTypes").val();
                var ctxPath = '<%= request.getContextPath() %>';
                var pastOrNew = "new";
                $("#scoreCardActionsDialog").dialog('open');
                var source = "";
                if (scorecardMemberId != null)
                    source = ctxPath+"/scoreCardActions.jsp?scoreCardId="+scoreCardId+"&score="+score+"&scoreCardMemberId="+scorecardMemberId+"&action="+action+"&pastOrNew="+pastOrNew+"&elementName="+elementName;
                else
                    source = ctxPath+"/scoreCardActions.jsp?scoreCardId="+scoreCardId+"&score="+score+"&action="+action+"&pastOrNew="+pastOrNew+"&elementName="+elementName;
                var actionsFrame = document.getElementById("scardActions");
                actionsFrame.src = source;
            }

            function showPastActions(scardId, scardMemberId){
                var ctxPath = '<%= request.getContextPath() %>';
                $("#pastScoreCardActionsDialog").dialog('open');
                var source = "";

                if (scardMemberId != null)
                    source = ctxPath+"/scoreCardActions.jsp?scoreCardId="+scardId+"&scoreCardMemberId="+scardMemberId+"&pastOrNew=past";
                else
                    source = ctxPath+"/scoreCardActions.jsp?scoreCardId="+scardId+"&pastOrNew=past";

                var actionsFrame = document.getElementById("scardPastActions");
                actionsFrame.src = source;
            }

            function closeActionsDialog(){
                $("#scoreCardActionsDialog").dialog('close');
            }
            function closePastActionsDialog(){
                $("#pastScoreCardActionsDialog").dialog('close');
            }

            function gotoScorecardHome(){
                document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                document.forms.scorecardForm.submit();
            }
              function goPaths(path){
                parent.closeStart();
                document.forms.scorecardForm.action=path;
                document.forms.scorecardForm.submit();
            }
            function viewReportG(path){
                document.forms.scorecardForm.action=path;
                document.forms.scorecardForm.submit();
            }
            function viewDashboardG(path){
                document.forms.scorecardForm.action=path;
                document.forms.scorecardForm.submit();
            }
            function gotoInsight(elementId,userId,folderId,insightDimId,insightDimValue)
            {
                var scorecardUrl="scorecardDesign.do?scorecardParam=viewScorecard&scorecardId=";
                var dateval = $("#datepicker11").val();
                var cmpval = $("#CBO_PRG_COMPARE").val();
                var duration = $("#CBO_PRG_PERIOD_TYPE").val();
                var scorecardId = '<%= scoreCardId %>';
                document.forms.scorecardForm.action="scoreCardViewer.do?reportBy=getScorecardKPIInsightViewerPage&elementId="+elementId+"&userId="+userId+"&scorecardId="+scorecardId+"&folderId="+folderId+"&dateval="+dateval+"&cmpval="+cmpval+"&duration="+duration+"&scorecardUrl="+encodeURIComponent(scorecardUrl)+"&insightDimId="+insightDimId+"&insightDimValue="+insightDimValue;

                document.forms.scorecardForm.submit();
            }
            function nestedInsights()
            {
                alert("Insights can be viewed for only individual members");
            }
            function addAdhocRows()
            {
                $("#adHocDialog").dialog('open');
                 $.ajax({
                        url: 'scoreCardViewer.do?reportBy=addAdhocRows',
                        success: function(data){
                            if(data!=""){
                                var html=data.split("~");
//                                $("#originalMeasTable").remove();
                               $("#actualMeasDiv").html(html[0])
                               if(html[1]!=""){
                                   $("#addHocTableBody").html(html[1])
                               }
//                               else{
//                                   $("#addHocTableBody").html("")
//                                   addRow();
//                               }
                            }
                        }
                    });
            }
            function addRow()
            {
               var tableObj=document.getElementById("addHocTable")
               var rows=tableObj.rows.length;
//               var html="<tr id='row"+(rows-1)+"'>";
               var html="";
                   html+="<td align='center'>"
                   html+="<input type='text' id='adHocMeas"+(rows-1)+"' name='adHocMeas' value=''></td>"
                   html+="<td align='center'>"
                   html+="<input type='text' id='adHocScore"+(rows-1)+"' name='adHocScore' value=''></td>"
                   html+="<td align='center'>"
                   html+="<input type='text' id='adHocWeigh"+(rows-1)+"' name='adHocWeight' value='0' onchange='calculateTotalWeightage()' ></td>"
//                   html+="</tr>";

               var row=tableObj.insertRow(rows);
               row.id='row'+(rows-1);
               $(row).html(html);

//               $("#addHocTableBody").html($("#addHocTableBody").html()+html)
            }
            function deleteRow()
            {
               var tableObj=document.getElementById("addHocTable")
               var rows=tableObj.rows.length;
               rows--;
               if(rows>=1){
                   $("#row"+(rows-1)).remove();
               }
            }
            function calculateTotalWeightage()
            {
                     try
                     {
                         var origMeasTable=document.getElementById("originalMeasTable");
                         var origRows=origMeasTable.rows.length;
                         var origWeight=0;
                         for(var i=0;i<origRows-1;i++)
                         {
                            origWeight=origWeight+parseInt($("#origWeigh"+i).val());
                         }
                         var adHocMeasTable=document.getElementById("addHocTable");
                         var adHocRows=adHocMeasTable.rows.length;
                         var adHocWeight=0;
                         for(var i=0;i<adHocRows-1;i++)
                         {
                            adHocWeight=adHocWeight+parseInt($("#adHocWeigh"+i).val());
                         }
                         var total=origWeight+adHocWeight;
                         $("#totWeighTd").html(total)
                         if(total!=100){
                             $("#warningText").show();
                             return false;
                         }else{
                             $("#warningText").hide();
                             return true;
                         }

                     }catch(err)
                     {
                         return false;
                         alert(err);
                     }
                     return true;
            }
            function saveAdhocRows()
            {
               var flag=calculateTotalWeightage();
               if(flag==true)
               {
                  $.post("<%=request.getContextPath()%>/scoreCardViewer.do?reportBy=saveAdhocRows",$("#adHocForm").serialize(),
                    function(data){
                       $("#adHocDialog").dialog('close');
                       loadScorecard();
                    }
                  )
               }
            }
        </script>
    </body>
</html>
