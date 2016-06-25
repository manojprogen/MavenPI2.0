<%--
    Document   : createScorecard
    Created on : Nov 12, 2010, 2:34:47 PM
    Author     : progen
--%>
<%@page import="prg.db.PbDb,prg.db.PbReturnObject"%>

<%          int userId = Integer.parseInt((String) session.getAttribute("USERID"));

            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

          String itemList=null;
          if (request.getAttribute("itemList") != null) {
                      itemList = (String) request.getAttribute("itemList");
                      }

          String userIdStr = "";
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
             String contextPath=request.getContextPath();

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--                  <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
                <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen">
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />-->
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />-->
         <link href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" type="text/css" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>

         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script><!--
-->        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/RangeTest.js"></script>

        <title>Scorecard</title>
        <style type="text/css">
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
            .loading_image{
                display: block;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:hidden;
            }
        </style>
      
    </head>
    <body>
        <form   name="scorecardForm" id="scorecardForm" action="scorecardDesign.do?scorecardParam=viewScorecard" method="post">
            <input type="hidden" value="" id="scorecardHiddenId" name="scorecardHiddenId">
            <input type="hidden" value="" id="scorecardId" name="scorecardId">
        
            <table width="98%">
                <tr>
                    <td align="left" width="25%">
                        <input type="text" class="myTextbox3" id="srchTextScorecard" name="srchTextScorecard" align="middle" style="width:280px;height:20px;">
                        <input type="button"  value="Search" onclick="displayStudioItem('srchTextScorecard',<%=userId%>,'Scorecard','tablesorterScorecard','<%=request.getContextPath()%>')" style="width:50px;height:20px;" class="navtitle-hover"/>

                    </td>

                    <td align="right" width="35%">

                        <input type="button" onclick="createScore()" id="createScorecard" class="navtitle-hover" value="Create Scorecard">
                        <input type="button" onclick="editScoreCard()" id="editScorecard" class="navtitle-hover" value="Edit Scorecard">
                        <input type="button" onclick="openCopyScoreDiv()" id="copyScorecardId" class="navtitle-hover" value="Copy Scorecard">
                        <input type="button" onclick="deleteScoreCard()" id="deleteScorecard" class="navtitle-hover" value="Delete Scorecard">
                        <input type="button" onclick="purgeScoreCard()" id="purgeScorecard" class="navtitle-hover" value="Purge Scorecard">
                        <input type="button" onclick="shareScoreCard()" id="shareScorecard" class="navtitle-hover" value="Share Scorecard">
                    </td>
                </tr>
            </table>
            <!--        </form>-->
            <div style="display: none;" title="Create Scorecard" id="createScoreDiv">
                <table align="center" id="masterTable">
                    <tr>
                        <td valign="top" align="center" style="width: 45%;" class="myHead">Business Roles</td>
                        <td><select id="selectBusRoles"  onchange="getMeasures()">
                                <option id="select" >select</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" align="center" style="width: 45%;" class="myHead">Scorecard Name</td>
                        <td><input type="text" id="scoreNameId" onkeyup="tabmsg()" onchange="checkScorecardName(this.id)"></td>
                    </tr>
                    <tr>
                        <td valign="top" align="center"  style="width: 45%;" class="myHead">Description</td>
                        <td><input type="text" id="areaId"></td>
                    </tr>
                    <tr>
                        <td valign="top" align="center"  style="width: 45%;" class="myHead">Target Score</td>
                        <td><input type="text" id="targetScoreId"></td>
                    </tr>
                    <tr>
                        <td colspan=""><input type="checkbox" id="dimCheckId"  onclick="isDimensionBased()">Dimension Based</td>
                        <td align="right"><input type="button" id="applyColor" onclick="applyColors()" class="navtitle-hover"  value="Scorecard Rules (Colors)"></td>
                    </tr>
                </table>
                <div id="measuresDiv" title="Measures"  >

                    <table style="width:100%;height:250px" border="solid black 1px">
                        <tr>
                            <td width="50%" valign="top" class="draggedTable1">
                                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Measures from below</font></div>
                                <div style="height:250px;overflow:scroll">
                                    <ul id="myList3" class="filetree treeview-famfamfam">

                                        <%--<li class="open" style="background-image:url('<%=request.getContextPath()%>/images/treeViewImages/plus.gif')">--%>
                                        <ul id="measures" >


                                        </ul>
                                        <%--</li>--%>
                                    </ul>
                                </div>
                            </td>
                            <td id="selectedMeasures" width="50%" valign="top">
                                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Measures to here</font></div>
                                <div style="height:250px;overflow:auto">
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
                            <td colspan="2" align="center" id="nextMaster">
                                <input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="saveCols()">
                            </td>
                            <td colspan="2" align="center" id="doneTarget" style="display:none">
                                <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="saveTargetCols()">
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="applyRuleDiv" title="Apply Scorecard Rules" style="display: none;">
                <form id="applyRuleForm" name="applyRuleForm" method="post" action="">
                <table align="center">
                    <table align="center">
                        <tr>
                            <td valign="top" style="width: 50px;display: none;" id="displayMeasureTd" class="myHead">Measures</td>
                            <td  id="dispMeasSelectTd" style="display: none;"><select id="selectedMeasureMulti"  onchange=" saveScorecardRules(),addRow('changeMeasure')"></select></td>
                            <td valign="top" style="width: 50px;" id="displayTypeTd" class="myHead">Measures</td>
                            <td><select id="selectedMeasure"  onchange=" saveScorecardRules(),addRow('changeMeasure')"></select></td>
                            <td valign="top" style="width: 50px;" class="myHead">Period</td>
                            <td><select id="period" onchange="setMeasureValues()">
                                    <!--                                <option value="select">select</option>-->
                                    <option value="Day">Day</option>
                                    <option value="Month">Month</option>
                                    <option value="Quarter">Quarter</option>
                                    <option value="Year">Year</option>
                                </select>
                            </td>
                            <td valign="top" style="width: 50px;" class="myHead">Units</td>
                            <td><select id="unit" onchange="changeValuesAsPerUnit()">
                                    <option value="Absolute">Absolute</option>
                                    <option value="Thousands">Thousands(k)</option>
                                    <option value="Lakhs">Lakhs</option>
                                    <option value="Millions">Millions</option>
                                    <option value="Crores">Crores</option>
                                    <option value="Billions">Billions</option>
                                    <!--                                <option value="Year">Year</option>-->
                                </select>
                            </td>
                        </tr>
                    </table>
                    <table id="measureDetailsTable">
                        <tr>
                            <td valign="top" id="maximumTd" style="width: 50%;" class="myHead">Maximum in period</td>
                            <td><input type="text" id="maxInPeriod" readonly ></td>
                            <td valign="top" id="minimumTd" style="width: 50%;" class="myHead">Minimum in period</td>
                            <td><input type="text" id="minInPeriod" readonly ></td>
                        </tr>

                        <tr>
                            <td valign="top" id="averageTd" style="width: 50%;" class="myHead">Average in period</td>
                            <td><input type="text" id="avgInPeriod" readonly ></td>
                            <td valign="top" id="lastPeriodTd" style="width: 50%;" class="myHead">Last period value</td>
                            <td><input type="text" id="lastPeriodValue" readonly ></td>
                        </tr>
                    </table>
                    <table id="avgOfLast3Table" align="center" style="display: none;">
                        <tr>
                            <td valign="top" id="averageLast3Td" style="width: 50%;" class="myHead"></td>
                            <td><input type="text" id="avgLast3" readonly ></td>
                        </tr>
                    </table>
                    <br>
                    <table align="center" id="scoreBasisTable">
                        <tr>
                            <td valign="top" style="" align="center" class="myHead">Score Basis</td>
                            <td>
                                <select id="scoreBaseSelectId" onchange="changeScoreBasis()">
                                    <!--                            <option value="select">select</option>-->
                                    <option value="Absolute">Absolute</option>
                                    <option value="Change%">Change%</option>
                                    <option value="Target Deviation%">Target Deviation%</option>
                                    <option value="Target Deviation">Target Deviation</option>
                                    <option value="Change Amount">Change Amount</option>
                                </select>
                            </td>
                        </tr>
                        <tr id="targetTrType" style="display:none">
                            <td class='myhead' align='center'>
                                Target Measure Type
                            </td>
                             <td id="targetTdType1" >
                                 <select id="targetMeasureType" name="targetMeasureType" onchange="changeTargetMeasureType()">
                                     <option value="TargetMeasure">Target Measure</option>
                                     <option value="TargetValue">Target Value</option>
                                     <option value="LinkMeasure">Link Measure</option>
                                 </select>
                             </td>
                       </tr>
                        <tr id="targetTr" style="display:none">
                            <td class='myhead' align='center'>
                                Target Measure
                            </td>
                             <td id="targetTd1" >
                                <input type="text" name="targetMeasText" id="targetMeasText" value="" >
                             </td>

                             <td id="targetTd2" >
                                  <div title="Click to show measures" class="ui-state-default ui-corner-all" style="width:15px">
                                      <span class="ui-icon ui-icon-calculator" style="cursor:pointer" onclick="showTargetMeasures()">

                                      </span>
                                  </div>
                            </td>
                        </tr>
                    </table>
                </table>
                <br>
                <table id="scorecardTable" align="center" style="display: none;">
                    <tr></tr>
                </table>
                <table style="display: none;" align="center" id="AddRemove">
                    <tr>
                        <td><input type="button" class="navtitle-hover" onclick="goToBackDiv()" value="Back"></td>
                        <td><input type="button" class="navtitle-hover" onclick="addRow('add')" value="Add Row"></td>
                        <td><input type="button" class="navtitle-hover" onclick="deleteRow()" value="Delete Row"></td>
                        <td><input type="button" class="navtitle-hover" onclick="copyValuesToAll()" value="Copy to All"></td>
                        <!--                        <td><input type="button" class="navtitle-hover" onclick="applyWeightage()" value="Assign Weightage"></td>-->

                        <!--                        <td><input type="button" class="navtitle-hover" onclick="saveScorecardRules()" value="Save"></td>-->
                    </tr>
                    <br>
                    <tr>
                        &nbsp;&nbsp; <td  colspan="2" align='center'><input type="button" class="navtitle-hover" onclick="applyWeightage()" value="Next"></td>
                    </tr>
                </table>
                <div id="loading" class='loading_image'  >
                    <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 225px; top: 10px;' />
                </div>
                </form>
            </div>
            <table align="center" id="tablesorterScorecard" class="tablesorter" style="" width="100%" border="0px solid" cellpadding="0" cellspacing="1">
                
            </table>
  <div id="pagerScorecard" class="pager" align="left" >
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                             <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                               <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allScores" value="">All</option>
                            </select>
                        </div>
            <div id="applyColorsDiv"  title="Apply Colors to Scorecard" style="display:none;" >
                <table align="center">
                    <tr>
                        <td><input type="radio" name="Group" onclick="changeColorRuleType()" checked="true" id="priorBase"></td><td>Based on Prior values</td>
                    </tr>
                    <tr>
                        <td><input type="radio" name="Group" onclick="changeColorRuleType()" id="ruleBase"></td><td>Based on Rules</td>
                    </tr>
                    <tr>
                         <td><input type="radio" name="Group" onclick="changeColorRuleType()" id="trgtBase"></td><td>Based on Targets Deviation</td>
                    </tr>
                </table>
                <table align="center" id="sCardColorTable" >

                    <tr id="colorTR0">
                        <td>
                            Apply color as
                            <select id="colorCodeId0" >
                                <option value="select">select</option>
                                <option value="Red">Red</option>
                                <option value="Green">Green</option>
                                <option value="Blue">Blue</option>
                            </select>
                        </td>
                        <td>When score

                            <select id="colOperId0" onchange="showColEndValue(this.id)" >
                                <option value="<>"><></option>
                                <option value="<"><</option>
                                <option value=">">></option>
                                <option value="<="><=</option>
                                <option value=">=">>=</option>
                                <option value="==">==</option>
                                <option value="!=">!=</option>

                            </select>
                        </td>
                        <td>
                            Start value

                            <input type="text" id="colStartId0" style="width:100px" >
                        </td>
                        <td id="colEndTD0" >
                            End value

                            <input type="text" id="colEndId0" style="width:100px">
                        </td>
                    </tr>
                </table>
                <table align="center">
                    <tr>
                        <td><input type="button" class="navtitle-hover" id="addColRowButt"  value="Add Row" onclick="addColorRow()"></td>
                        <td><input type="button" class="navtitle-hover" id="deleteColRowButt"  value="Delete Row" onclick="deleteColorRow()"></td>
                        <td><input type="button" class="navtitle-hover" value="Done" onclick="saveColors()"></td>
                        <td><input type="button" class="navtitle-hover" value="Cancel" onclick="cancelColors()"></td>
                    </tr>
                </table>

            </div>
            <div id="weightageDiv" title="Apply Weightage">

            </div>
            <div id="copyScorecardDiv" style="display: none;" title="Copy Scorecard">
                <table align="center">
                    <tr>
                        <td valign="top" style="width: 45%;" class="myHead">Scorecard Name</td>
                        <td><input type="text" id="copyScoreName" onkeyup="tabMsgForCopy()"></td>
                    </tr>
                    <tr>
                        <td valign="top" style="width: 45%;" class="myHead">Description</td>
                        <td><textarea cols="" rows=""  style="width: 150px;" id="copyDescId" name="copyDescId"></textarea></td>
                        <!--                        <td><input type="text" id="copyDescId" ></td>-->
                    </tr>
                    <tr>
                        <td></td>  <td colspan="1"><input type="button" class="navtitle-hover" value="Done" onclick="copyScoreCard()"></td>
                    </tr>
                </table>
            </div>

            <div id="dimensionDiv" title="Dimensions"  style="display:none;">

                <table style="width:100%" border="solid black 1px">
                    <tr>
                        <td width="50%" valign="top" class="draggedTable1">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Dimensions from below</font></div>
                            <div style="height:250px;overflow:scroll">
                                <!--                                    <ul id="myList3" class="filetree treeview-famfamfam">-->

                                <ul id="dimensions" class="sortable" >


                                </ul>
                                <!--                                        </ul>-->
                            </div>
                        </td>
                        <td id="selectedDimensions" width="50%" valign="top">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Dimensions to here</font></div>
                            <div id="dimsDropDiv" style="height:250px;overflow:auto">
                                <ul id="dimSortable" class="droppable">

                                </ul>
                            </div>
                        </td>
                    </tr>

                </table>
                <table  align="center">
                    <tr>
                        <td colspan="2" style="height:10px"></td>
                    </tr>
                    <tr>

                        <td  align="center">
                            <input type="button" class="navtitle-hover" style="width:auto" value="Back" onclick="goBack()">
                        </td>
                        <td  align="center">
                            <input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="saveDimensions()">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="dimensionValuesDiv" title="Dimension Detials" style="display: none;">
                <iframe id="dimensionValuesFrame" src="about:blank" frameborder="0" style="width:99%;height:100%;overflow:auto" />
            </div>
            <div id="shareScoreCardDiv" title="Share Scorecard" style="display: none;">
                 <table style="width:100%" border="solid black 1px">
                    <tr>
                        <td width="50%" valign="top" class="draggedTable1">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Users from below</font></div>
                            <div style="height:250px;overflow:scroll">
                                <!--                                    <ul id="myList3" class="filetree treeview-famfamfam">-->

                                <ul id="shareDragUl" class="sortable" >


                                </ul>
                                <!--                                        </ul>-->
                            </div>
                        </td>
                        <td id="selectedDimensions" width="50%" valign="top">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Users to here</font></div>
                            <div id="shareDropDiv" style="height:250px;overflow:auto">
                                <ul id="shareDropUl" class="droppable">

                                </ul>
                            </div>
                        </td>
                    </tr>

                </table>
                <table  align="center">

                    <tr>

                        <td  align="center">
                            <input type="button" class="navtitle-hover" style="width:auto" value="Share" onclick="saveSharedScoreCards()">
                        </td>
                    </tr>
                </table>
            </div>
        </form>
                              <script type="text/javascript">
           // var hMap=new HashMap();
             $(document).ready(function(){
                      var options, a;
                        options = {
                            serviceUrl:'studioAction.do?studioParam=autoSuggest&tab=Scorecard&userId=<%=userId%>',
                            delimiter: /(,)\s*/, // regex or character
                            minChars:2,
                            deferRequestBy: 500, //miliseconds
                            maxHeight:400,
                            width:450,
                            zIndex: 9999,
                            noCache: false

                        };
                a = $("#srchTextScorecard").autocomplete(options);
                $("#srchTextScorecard").keyup(function(event){
                    if(event.keyCode == 13){
                       // searchVals();

                    }
                });
                 var data='<%=itemList%>'
                var length=bulidTable(data,'tablesorterScorecard','Scorecard','<%=request.getContextPath()%>','<%=themeColor%>')
                $("#allScores").val(length);
                });
           
             function selectAllScores()
            {
                var scoreSelectObj=document.getElementsByName("ScoreSelect");

                for(var i=0;i<scoreSelectObj.length;i++){
                    if(scoreSelectObj[0].checked){
                        scoreSelectObj[i].checked=true;
                    }
                    else{
                        scoreSelectObj[i].checked=false;
                    }
                }
            }
            function deleteScoreCard()
            {
                var scoreSelectObj=document.getElementsByName("ScoreSelect");
                var deletescoreids = new Array();
                for(var i=1;i<scoreSelectObj.length;i++){
                    if(scoreSelectObj[i].checked){
                        deletescoreids.push(scoreSelectObj[i].value);
                    }
                    else{
                        scoreSelectObj[i].checked=false;
                    }
                }
                if(deletescoreids.length!=0){
                    var confirmDel=confirm("Do you want to Delete Scorecard");
                    if(confirmDel==true){
                        $.ajax({
                            url: "scorecardDesign.do?scorecardParam=deleteScoreCard&deletescoreids="+deletescoreids.toString()+"&userId=<%=userId%>",
                            success: function(data){
                                document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                                document.forms.scorecardForm.submit();
                            }
                        });
                        // document.forms.reportForm.action="reportTemplateAction.do?templateParam=DeleteUserReports&deleterepids="+deleterepids.toString();
                        //  document.forms.reportForm.submit();
                    }else{
                        document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                        document.forms.scorecardForm.submit();
                    }
                }else{
                    alert("Please select Scorecard(s)")
                }
            }
            function purgeScoreCard(){
                var scoreSelectObj=document.getElementsByName("ScoreSelect");
                var deletescoreids = new Array();
                for(var i=1;i<scoreSelectObj.length;i++){
                    if(scoreSelectObj[i].checked){
                        deletescoreids.push(scoreSelectObj[i].value);
                    }
                    else{
                        scoreSelectObj[i].checked=false;
                    }
                }
                if(deletescoreids.length!=0){
                    var confirmDel=confirm("Do you want to Purge Scorecard");
                    if(confirmDel==true){
                        $.ajax({
                            url: "scorecardDesign.do?scorecardParam=purgeScoreCard&deletescoreids="+deletescoreids.toString()+"&userId=<%=userId%>",
                            success: function(data){
                                document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                                document.forms.scorecardForm.submit();
                            }
                        });
                        // document.forms.reportForm.action="reportTemplateAction.do?templateParam=DeleteUserReports&deleterepids="+deleterepids.toString();
                        //  document.forms.reportForm.submit();
                    }else{
                        document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                        document.forms.scorecardForm.submit();
                    }
                }else{
                    alert("Please select Scorecard(s)")
                }
            }


            function createScore(){
                mode="Create";
                $("#sortable").html("");
                $("#scoreNameId").val("")
                $("#areaId").val("")
                $("#scorecardTable").html('<tr></tr>')
                $("#sortable").html("")
                $("#AddRemove").hide();
                grpColArray=new Array();
              //  hMap=new HashMap();
                measIdsArry=new Array();
                measNameArry=new Array()
                measWeigh=new Array();
                isTargetBased=false;
                ct=0;
                $("#createScoreDiv").dialog('open')
                $("#masterTable").show();
            }
            var busFolderId;
            function getMeasures(){
                var selValue=document.getElementById("selectBusRoles");
                var id=selValue.id;
                var folderId=$("#"+id).val();
                busFolderId=folderId;

                if(folderId!="select"){
                    $.ajax({
                        url:"scorecardDesign.do?scorecardParam=getMeasures&userId=<%=userId%>&folderId="+folderId+"&ctxPath=<%=request.getContextPath()%>",
                        success: function(data){
                            if(data!=""){
                                $("#quicksearch").remove();

                                $("#measures").html(data);

                                initClasses();
                                document.getElementById("quicksearch").style.height='10px';
                                isDimBased=isDimensionBased();
                                if(isDimBased==true){
                                    $("#Scorecards").hide();
                                }else{
                                    $("#Scorecards").show();
                                }
                            }

                        }
                    });
                }
                // getCreatedScoreCards();
            }
            function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');


            }
            $(document).ready(function(){
                //initClasses();
                //                 $("#tablesorterReport")
                //                    .tablesorter({headers : {0:{sorter:false}}})
                //                    .tablesorterPager({container: $('#pagerReport')
                //                  })
                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=getAllBusRoles&userId=<%=userId%>",
                    success: function(data){
                        if(data!=""){
                            var html=$("#selectBusRoles").html();
                            $("#sortable").html("")
                            $("#selectBusRoles").html("<option value='select'>select</option>"+data);

                        }

                    }
                });

                $("#createScoreDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 550,
                    width: 650,
                    modal: true,
                    Cancel: function() {
                        // refreshPage();
                        $(this).dialog('close');
                    }

                    //                    close: function(ev, ui) {
                    //                        if(close)
                    //                        refreshPage();
                    //                    }

                });
                $("#applyRuleDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 450,
                    width: 850,
                    modal: true,
                    Cancel: function() {

                        $(this).dialog('close');
                    }
                    //                    close: function(ev, ui) {
                    //                        if(close)
                    //                        refreshPage();
                    //                    }
                    //close: function(ev, ui) {  refreshPage(); }
                });

                $("#applyColorsDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 250,
                    width: 660,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                    //                    close: function(ev, ui) {
                    //                        if(close)
                    //                        refreshPage();
                    //                    }
                    //close: function(ev, ui) {  refreshPage(); }
                });
                $("#weightageDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 250,
                    width: 400,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                    //                    close: function(ev, ui) {
                    //                        if(close)
                    //                        refreshPage();
                    //                    }
                    // close: function(ev, ui) {  refreshPage(); }
                });
                $("#copyScorecardDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 320,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                    //close: function(ev, ui) {  refreshPage(); }
                });
                $("#dimensionDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 400,
                    width: 600,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                    //close: function(ev, ui) {  refreshPage(); }
                });
                $("#dimensionValuesDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 480,
                    width: 600,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                    //close: function(ev, ui) {  refreshPage(); }
                });
                $("#shareScoreCardDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 400,
                    width: 550,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                    //close: function(ev, ui) {  refreshPage(); }
                });

            });
            function tabmsg(){
                document.getElementById('areaId').value = document.getElementById('scoreNameId').value;
            }
            function tabMsgForCopy(){
                document.getElementById('copyDescId').value = document.getElementById('copyScoreName').value;
            }
            var grpColArray=new Array();
            var ScoreCardIds=new Array();
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
                for(var j=0;j<ScoreCardIds.length;j++){
                    if(ScoreCardIds[j]==x)
                        ScoreCardIds.splice(j,1);
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
                //  alert(elmntId)
                var scorecard=elmntId.substring(0, 6);
                if(scorecard=="Scards")
                {
                    var y=ScoreCardIds.toString();
                    if(y.match(elmntId)==null){
                        ScoreCardIds.push(elmntId);
                    }
                }
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
            var measIds=new Array();
            var measWeigh=new Array();

            var measIdsArry=new Array();
            var measNameArry=new Array();

            var colCodeArry=new Array();
            var colOperArry=new Array();
            var colStartArry=new Array();
            var colEndArry=new Array();
            var colorType="";
            var mode;
            var sCardId="null";
            var isExists=false;
            var isDimBased=false;
            var dimMemsArry=new Array();
            var dimensionId="";
            var dims="";
            var isTargetBased=false;
            function saveCols(){
                var scorecardName=$("#scoreNameId").val();
                var area=$("#areaId").val();
                var measuresArry=new Array();
                var colsArray=new Array();
                isDimBased=isDimensionBased();
                var cols="";
                var colsUl=document.getElementById("sortable");
                var table;
                var trObj;
                var tdObj;
                var tdContent;
                var measureNames;
                var measureOptions="";
                var selMeasCt;

                   alert("colorType\t"+colorType)
                if(scorecardName==""){
                    alert("Please enter scorecard name")
                }else
                    if(isExists==true){
                        alert("Scorecard Name already exists,please enter different name")
                    }
                else
                    if(colsUl!=undefined || colsUl!=null){
                        var colIds=colsUl.getElementsByTagName("li");
                        if(colIds!=null && colIds.length!=0){
                            // close=0;

                            //  close=1;
                            for(var i=0;i<colIds.length;i++){
                                cols =colIds[i].id.replace("GrpCol","");
                                colsArray.push(cols);
                                table=document.getElementById("GrpTab"+colIds[i].id.replace("GrpCol",""));
                                trObj=table.getElementsByTagName("tr");
                                tdObj=trObj[0].getElementsByTagName("td");
                                tdContent=tdObj[1].innerHTML;
                                measuresArry.push(tdContent);
                            }
                            var targetScore=$("#targetScoreId").val();
                            if(isDimBased==false)
                            {
                                $("#createScoreDiv").dialog('close');
                                measIdsArry=colsArray;
                                measNameArry=measuresArry;
                                scorecardName=encodeURIComponent(scorecardName)  ;
                                area=encodeURIComponent(area)
                                 alert("measuresArry\t"+measuresArry)
                                 var complteUrl='<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=setScoreCardMasterDetails&mode='+mode+'&bussFolderId='+busFolderId+'&measId='+colsArray.toString()+'&measureName='+encodeURIComponent(measuresArry.toString())+'&sName='+scorecardName+'&userId=<%=userId%>&area='+area+'&colorType='+colorType+'&colCode='+colCodeArry+'&colOper='+colOperArry+'&stVal='+colStartArry+'&endVal='+colEndArry+'&targetScore='+targetScore
                                $.ajax({
                                    url:complteUrl,
                                    success: function(data){
                                           var scorecard;
                                        for(var i=0;i<colsArray.length;i++)
                                        {
                                            scorecard=colsArray[i].substring(0, 6);
                                            if(scorecard!="Scards")
                                            measureOptions+="<option id="+colsArray[i]+" value="+colsArray[i]+" >"+measuresArry[i]+"</option>"
                                        }
                                        var html="<option id='select'>select</option>";
                                        $("#selectedMeasure").html(html+measureOptions);
                                        getMeasureValues();
                                        if(measureOptions!="")
                                           $("#applyRuleDiv").dialog('open')
                                        else
                                           applyWeightage();
//                                        $("#scorecardTable").hide();
                                    }
                                });
                           
                        }
                        else
                        {
                            //                               if(measuresArry.length>1)
                            //                               {
                            //                                 alert("Please select only one measure")
                            //                               }
                            //                               else
                            //                               {
                            scorecardName=encodeURIComponent(scorecardName)  ;
                            area=encodeURIComponent(area)
                            alert("measuresArry\t"+measuresArry)
                            var complteUrl='<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=setScoreCardMasterDetails&mode='+mode+'&isDimBased='+isDimBased+'&bussFolderId='+busFolderId+'&measId='+colsArray.toString()+'&measureName='+measuresArry.toString()+'&sName='+scorecardName+'&userId=<%=userId%>&area='+area+'&colorType='+colorType+'&colCode='+colCodeArry+'&colOper='+colOperArry+'&stVal='+colStartArry+'&endVal='+colEndArry+'&targetScore='+targetScore
                            $.ajax({
                                url:complteUrl,
                                success: function(data){

                                }
                            });
                            $("#createScoreDiv").dialog('close');
                            measIdsArry=colsArray;
                            measNameArry=measuresArry;
                            for(var i=0;i<colsArray.length;i++){
                                measureOptions+="<option id="+colsArray[i]+" value="+colsArray[i]+" >"+measuresArry[i]+"</option>"
                            }
                            var html="<option id='select'>select</option>";
                            $("#selectedMeasure").html(html+measureOptions);
                            // getMeasureValues();
                            getDimensions();
                            $("#dimensionDiv").dialog('open')
                            // }
                        }

                    }
                    else{
                        alert("Please Select Measures");
                    }
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
                $("#DimensionsUL").sortable();


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
                        if(isTargetBased && grpColArray.length>0)
                        {
                           alert("You cannot select more than one target measure")
                        }else{
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
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
            function showEndValue(obj){
                var id=obj.id;
                var op=$("#"+id).val();
                var count=id.substring(4);
                // alert(count);
                if(op=="<>"){
                    $("#endTD"+count).show();
                }else{
                    $("#endTD"+count).hide();
                    $("#end"+count).val("");
                }
            }
            function getDimensions()
            {
                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=getDimensions&folderId="+busFolderId+"&userId=<%=userId%>",
                    success: function(data){
                        $("#dimensions").html(data)
                        initDrag('dimsDropDiv');
                        if(mode=="Edit")
                                getCreatedDimensions();
                    }

                });
            }
            var editDimensionEleId
            function getCreatedDimensions()
            {
                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=getCreatedDimensions",
                    success: function(data){
                    //alert("data--"+data)
                        var json=eval("("+data+")");
                        var dimElementId=json.DimElementId;
                        editDimensionEleId=dimElementId;
                        var dimULObj=document.getElementById("dimensions");
                        var liObjs=dimULObj.getElementsByTagName("li");
                        var tableObj,tBodyObj,trObj,tdObj;
                        var dimensionName;
                        for(var i=0;i<liObjs.length;i++)
                            {
                                if(dimElementId==liObjs[i].id)
                                    {
                                        tableObj=liObjs[i].getElementsByTagName("table");
                                        tBodyObj=tableObj[0].getElementsByTagName("tbody");
                                        trObj=tBodyObj[0].getElementsByTagName("tr");
                                        tdObj=trObj[0].getElementsByTagName("td");
                                        //dimensionName=$("#"+tdObj[0].id).html();
                                        createDimensionElement(dimElementId);
                                        break;
                                    }
                            }
                     
                                        for(var i=0;i<json.DimMembers.length;i++)
                                        {
                                           dimMemsArry.push(json.DimMembers[i]);

                                        }
                
                    }

                });
            }
            
            function getMeasureValues()
            {
                dims="";
                for(var i=0;i<dimMemsArry.length;i++){
                    dims+=dimMemsArry[i];
                    if(i!=dimMemsArry.length-1)
                        dims+="~";
                }
                $(".loading_image").show();
                // var measureId=$("#selectedMeasure").val();
                //   document.getElementById("loading").style.display='block';
                var periodType=$("#period").val();
                //  $(".loading_image").hide();
                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=getMeasureValues&dimId="+dimensionId+"&dims="+dims.toString()+"&isDimBased="+isDimBased+"&timeLevel="+periodType+"&measureIds="+measIdsArry.toString()+"&folderId="+busFolderId+"&userId=<%=userId%>",
                    success: function(data){
                        $(".loading_image").hide();
                    }

                });

            }

            function setMeasureValues(){
                var selMeasureId="";
                if(isDimBased==true){
                    selMeasureId=$("#selectedMeasureMulti").val();
                }else{
                    selMeasureId=$("#selectedMeasure").val();
                }
                var dimValue=$("#selectedMeasure  option:selected").text().trim();
                // alert(dimValue)
                var period=$("#period").val();
                var scoreBasis=$("#scoreBaseSelectId").val();
                if(scoreBasis!="Absolute")
                {
                    changeScoreBasis();
                }
                else{
                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=getSelectedMeasureValues&isDimBased="+isDimBased+"&dimValue="+dimValue+"&period="+period+"&measureId="+selMeasureId,
                    success: function(data){
                        if(data!="")
                        {
                           // alert("data--"+data)
                            var json=eval("("+data+")")
                            $("#measureDetailsTable").show();
                            $("#avgOfLast3Table").show();
                            $("#scoreBasisTable").show();

                            if(period=="Year"){
                                $("#maximumTd").html(json.measMetricsLst.Year[0].label);
                                $("#maxInPeriod").val(json.measMetricsLst.Year[0].measValue);
                                $("#minimumTd").html(json.measMetricsLst.Year[1].label);
                                $("#minInPeriod").val(json.measMetricsLst.Year[1].measValue);
                                $("#averageTd").html(json.measMetricsLst.Year[2].label);
                                $("#avgInPeriod").val(json.measMetricsLst.Year[2].measValue);
                                $("#lastPeriodTd").html(json.measMetricsLst.Year[3].label);
                                $("#lastPeriodValue").val(json.measMetricsLst.Year[3].measValue);
                                if(json.measMetricsLst.Year[4].measValue!="null")
                                {
                                    $("#averageLast3Td").html(json.measMetricsLst.Year[4].label);
                                    $("#avgLast3").val(json.measMetricsLst.Year[4].measValue);
                                    $("#avgOfLast3Table").show();
                                }else{
                                    $("#avgOfLast3Table").hide();
                                }

                            }
                            else if(period=="Quarter")
                            {
                                $("#maximumTd").html(json.measMetricsLst.Qtr[0].label);
                                $("#maxInPeriod").val(json.measMetricsLst.Qtr[0].measValue);
                                $("#minimumTd").html(json.measMetricsLst.Qtr[1].label);
                                $("#minInPeriod").val(json.measMetricsLst.Qtr[1].measValue);
                                $("#averageTd").html(json.measMetricsLst.Qtr[2].label);
                                $("#avgInPeriod").val(json.measMetricsLst.Qtr[2].measValue);
                                $("#lastPeriodTd").html(json.measMetricsLst.Qtr[3].label);
                                $("#lastPeriodValue").val(json.measMetricsLst.Qtr[3].measValue);
                                if(json.measMetricsLst.Qtr[4].measValue!="null")
                                {
                                    $("#averageLast3Td").html(json.measMetricsLst.Qtr[4].label);
                                    $("#avgLast3").val(json.measMetricsLst.Qtr[4].measValue);
                                    $("#avgOfLast3Table").show();
                                }else{
                                    $("#avgOfLast3Table").hide();
                                }
                            }
                            else if(period=="Month")
                            {
                                $("#maximumTd").html(json.measMetricsLst.Month[0].label);
                                $("#maxInPeriod").val(json.measMetricsLst.Month[0].measValue);
                                $("#minimumTd").html(json.measMetricsLst.Month[1].label);
                                $("#minInPeriod").val(json.measMetricsLst.Month[1].measValue);
                                $("#averageTd").html(json.measMetricsLst.Month[2].label);
                                $("#avgInPeriod").val(json.measMetricsLst.Month[2].measValue);
                                $("#lastPeriodTd").html(json.measMetricsLst.Month[3].label);
                                $("#lastPeriodValue").val(json.measMetricsLst.Month[3].measValue);
                                if(json.measMetricsLst.Month[4].measValue!="null")
                                {
                                    $("#averageLast3Td").html(json.measMetricsLst.Month[4].label);
                                    $("#avgLast3").val(json.measMetricsLst.Month[4].measValue);
                                    $("#avgOfLast3Table").show();
                                }else{
                                    $("#avgOfLast3Table").hide();
                                }
                            }
                            else if(period=="Day")
                            {
                                $("#maximumTd").html(json.measMetricsLst.Day[0].label);
                                $("#maxInPeriod").val(json.measMetricsLst.Day[0].measValue);
                                $("#minimumTd").html(json.measMetricsLst.Day[1].label);
                                $("#minInPeriod").val(json.measMetricsLst.Day[1].measValue);
                                $("#averageTd").html(json.measMetricsLst.Day[2].label);
                                $("#avgInPeriod").val(json.measMetricsLst.Day[2].measValue);
                                $("#lastPeriodTd").html(json.measMetricsLst.Day[3].label);
                                $("#lastPeriodValue").val(json.measMetricsLst.Day[3].measValue);
                                if(json.measMetricsLst.Day[4].measValue!="null")
                                {
                                    $("#averageLast3Td").html(json.measMetricsLst.Day[4].label);
                                    $("#avgLast3").val(json.measMetricsLst.Day[4].measValue);
                                    $("#avgOfLast3Table").show();
                                }else{
                                    $("#avgOfLast3Table").hide();
                                }
                            }
                            changeValuesAsPerUnit();

                        }
                        else{
                            //                          $("#measureDetailsTable").hide();
                            //                          $("#avgOfLast3Table").hide();
                            //                          $("#scoreBasisTable").hide();

                        }
                    }
                });
                }

            }



            var currMeasureId = "select";
            var ct=0;
            var rows=0;
            function addRow(param){
                
                var sCardValArry=new Array();
                var OperatorArry=new Array();
                var stValueArry=new Array();
                var endValueArry=new Array();
                var weightage;
                var scoreBasis;
                var units;
                var period;
                var selMeasureId=$("#selectedMeasure").val();
                var table = document.getElementById("scorecardTable");
                var rowCount = table.rows.length;

                var sel=$("#selectedMeasure").val();
                // currMeasureId=sel;
                var isExists=false;
                 if(sel=="select"){
                    $("#scorecardTable").hide();
                    $("#AddRemove").hide();
                }else{
                 var completeUrl;
                 if(isDimBased)
                 completeUrl='<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=getJsonForAssignedComponents&isDimBased='+isDimBased+'&currMeasId='+$("#selectedMeasureMulti").val()+'&currDimValue='+$("#selectedMeasure  option:selected").text();
                 else
                 completeUrl='<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=getJsonForAssignedComponents&isDimBased='+isDimBased+'&currMeasId='+$("#selectedMeasure").val();

                 $.ajax({
                        url:completeUrl,
                        success: function(data){
                        if(data!=""&& param!="add")
                         {
                            
                            ct=0;
                            var json=eval("("+data+")") ;
                            $("select#scoreBaseSelectId").attr('value',json.basis);
                            $("select#period").attr('value',json.period);
                            $("select#unit").attr('value',json.unit);
                           if(json.basis=="Target Deviation" || json.basis=="Target Deviation%")
                             {
                                 $("#maxInPeriod").val("");
                                 $("#minInPeriod").val("");
                                 $("#avgInPeriod").val("");
                                 $("#lastPeriodValue").val("");
                                 $("#avgLast3").val("");

                                 $("#targetTr").show();
                                 $("#targetTrType").show();
//                                 alert(json.targetMeasureType)
//                                 alert(json.targetMeasureValue)
                                if(json.targetMeasureType=="TargetValue")
                                 {
                                     $("select#targetMeasureType").attr('value',json.targetMeasureType);
                                     $("#targetMeasText").val(json.targetMeasureValue);
                                     $("#targetTd2").hide();
                                 }
                                 else if(json.targetMeasureType=="TargetMeasure")
                                 {
                                     $("select#targetMeasureType").attr('value',json.targetMeasureType);
                                     $("#targetMeasText").val(json.targetElementName)
                                     $("#targetMeasText").attr('elementid',json.targetElementId)
                                     $("#targetTd2").show();
                                 }
                                 changeTargetMeasureType();
                             }else{
                                 $("#targetTr").hide();
                                 $("#targetTrType").hide();
                             }
                            for(var i=0;i<json.ruleList.length;i++){
                               // rows=1;
                                isExists=true;
                                tableHtml+="<tr id='TR"+ct+"'>"
                                tableHtml+="<td>Scorecard Value <input type='text' style='width: 40px;'  value="+json.ruleList[i].score+" id='score"+ct+"'></td>";
                                tableHtml+="<td>Operator <select id='oper"+ct+"' onchange='showEndValue(this)' value="+json.ruleList[i].operator+" >";
                                tableHtml+="<option value='select'>select</option>";
                                tableHtml+="<option value='>'>></option>";
                                tableHtml+="<option value='<'><</option>";
                                tableHtml+="<option value='>='>>=</option>";
                                tableHtml+="<option value='<='><=</option>";
                                tableHtml+="<option value='=='>==</option>";
                                tableHtml+="<option value='!='>!=</option>";
                                tableHtml+="<option value='<>'><></option>";
                                tableHtml+="</select></td><td>Start Value <input type='text' value="+json.ruleList[i].startValue+" style='width: 60%;' id='start"+ct+"'></td>";
                                if(json.ruleList[i].operator.trim()=="<>")
                                    tableHtml+="<td  id='endTD"+ct+"' >End Value <input type='text' value="+json.ruleList[i].endValue+" style='width: 60%;' id='end"+ct+"'></td>"
                                else
                                    tableHtml+="<td style='display:none;' id='endTD"+ct+"' >End Value <input type='text' value="+json.ruleList[i].endValue+" style='width: 60%;' id='end"+ct+"'></td>"
                                tableHtml+="<tr>"
                                ct++;
                           }
                            $("#scorecardTable").html(tableHtml.replace("undefined","","gi"));
                            for(var i=0;i<json.ruleList.length;i++){
                                $("select#oper"+i).attr("value",json.ruleList[i].operator);
                            }
                           $("#AddRemove").show();
                           $("#scorecardTable").show();
                            setMeasureValues();
                     }

                    else if(rowCount>1 && param=="changeMeasure"){
                        setMeasureValues();
                        ct=0;
                        $("#scorecardTable").html("");
                        $("#weightage").val("");
                        var tableHtml="";
                        for(var i=0;i<3;i++)
                        {
                            tableHtml+="<tr id='TR"+ct+"'>"
                            tableHtml+="<td>Scorecard Value <input type='text' style='width: 40px;' value='' id='score"+ct+"'></td>";
                            tableHtml+="<td>Operator <select id='oper"+ct+"' value='' onchange='showEndValue(this)'>";
                            tableHtml+="<option value='select'>select</option>";
                            tableHtml+="<option value='>'>></option>";
                            tableHtml+="<option value='<'><</option>";
                            tableHtml+="<option value='>='>>=</option>";
                            tableHtml+="<option value='<='><=</option>";
                            tableHtml+="<option value='=='>==</option>";
                            tableHtml+="<option value='!='>!=</option>";
                            tableHtml+="<option value='<>'><></option>";
                            tableHtml+="</select></td><td>Start Value <input type='text' value='' style='width: 60%;' id='start"+ct+"'></td>"
                            tableHtml+="<td style='display:none;' id='endTD"+ct+"' >End Value <input type='text' value='' style='width: 60%;' id='end"+ct+"'></td>"
                            tableHtml+="<tr>"
                            ct++;
                            // var html=$("#scorecardTable").html();
                            $("#scorecardTable").html(tableHtml);
                        }
                    $("#AddRemove").show();
                    $("#scorecardTable").show();
                    //  }
                }else{
                    setMeasureValues();
                    var num;
                    if(rows==0)
                        num=3;
                    else
                        num=1;
                    rows++;
                    var tableHtml="";
                    for(var i=0;i<num;i++)
                    {
                        var tableHtml="";
                        tableHtml+="<tr id='TR"+ct+"'>"
                        tableHtml+="<td>Scorecard Value <input type='text' style='width: 40px;' value='' id='score"+ct+"'></td>";
                        tableHtml+="<td>Operator <select id='oper"+ct+"' value='' onchange='showEndValue(this)'>";
                        tableHtml+="<option value='select'>select</option>";
                        tableHtml+="<option value='>'>></option>";
                        tableHtml+="<option value='<'><</option>";
                        tableHtml+="<option value='>='>>=</option>";
                        tableHtml+="<option value='<='><=</option>";
                        tableHtml+="<option value='=='>==</option>";
                        tableHtml+="<option value='!='>!=</option>";
                        tableHtml+="<option value='<>'><></option>";
                        tableHtml+="</select></td><td>Start Value <input type='text' value='' style='width: 60%;' id='start"+ct+"'></td>";
                        tableHtml+="<td style='display:none;' id='endTD"+ct+"' >End Value <input type='text' value='' style='width: 60%;' id='end"+ct+"'></td>";
                        tableHtml+="<tr>"
                        $('#scorecardTable tr:last').after(tableHtml)
                        ct++;
                    }
                    $("#AddRemove").show();
                    $("#scorecardTable").show();
                }
                       }
                 });

               }
            }
            function deleteRow(){
                if(ct>1){
                    ct--;
                    $("#TR"+ct).remove();
                }
            }
            var currMeasMulti="select";
            function saveScorecardRules(){

                if(currMeasureId!="select" && $("#selectedMeasure").val()!="select")
                {
                    var selMultiMeasId=currMeasMulti;
                    currMeasMulti=$("#selectedMeasureMulti").val();

                    var selMeasureId=currMeasureId;
                    
                   // var selMeasName=$("#"+selMeasureId).html();
                   var selMeasName="";
                   if(isDimBased)
                       {
                          selMeasName=currMeasureId;
                          currMeasureId = $("#selectedMeasure option:selected").text();
                       }
                       else{
                           currMeasureId = $("#selectedMeasure").val();
                       }
                   //  alert("selMeasName"+selMeasName)
                   // var selMeasName=$("#"+selMeasureId+' option:selected').text();
                    var scoreBase =$("#scoreBaseSelectId").val();
                    scoreBase=encodeURIComponent(scoreBase);

                                   // alert("scoreBase--"+scoreBase)
                    // $("#selectedMeasure  option:selected").text()
                    // if(selMeasureId!="select"){
                    var period=$("#period").val();
                    var unit=$("#unit").val();
                    // var weightage=$("#weightage").val();
                    var sCardValArry=new Array();
                    var OperatorArry=new Array();
                    var stValueArry=new Array();
                    var endValueArry=new Array();
                    var scoreBaseArry=new Array();
                    var totData=new Array();

                    var sCard;
                    var op;
                    var stValue;
                    var endValue;

                    for(var i=0;i<ct;i++)
                    {
                        sCard=$("#score"+i).val();
                        op=$("#oper"+i).val();
                        stValue=$("#start"+i).val();
                        endValue=$("#end"+i).val();

                        sCardValArry.push(sCard);
                        OperatorArry.push(op);
                        stValueArry.push(stValue);
                        if(op.trim()!="<>")
                            endValueArry.push("null");
                        else
                            endValueArry.push(endValue);
                    }
                    
                       var tempOp=new Array();
                       var tempSt=new Array();
                       var tempEnd=new Array();
                       for(var i=0;i<OperatorArry.length;i++)
                           {
                               tempOp.push(OperatorArry[i])
                               tempSt.push(stValueArry[i])
                               tempEnd.push(endValueArry[i])
                           }
                      
                    var isValid=validateScorecardRules(sCardValArry,OperatorArry,stValueArry,endValueArry);
                          
                    if(isValid==true){
                        var isSeq=ValidateTargetValues(tempOp,tempSt,tempEnd)
                        var tMeasType=$("#targetMeasureType").val();
                        var tMeasVal=$("#targetMeasText").val();
                        var  complteUrl;
                        if(isSeq==false)
                        {
                            var conf=confirm("Values entered are not in sequentail order,you want to continue ?")
                            if(conf==true)
                            {
                                if(isDimBased==false)
                                    complteUrl= '<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=setScoreCardMemberDetails&isDimBased='+isDimBased+'&measId='+selMeasureId+'&measName='+selMeasName+'&units='+unit+'&period='+period+'&scoreBasis='+scoreBase+'&sCards='+sCardValArry.toString()+'&operators='+OperatorArry.toString()+'&stValues='+stValueArry.toString()+'&endValues='+endValueArry.toString()+'&currMeasId='+$("#selectedMeasure").val()+'&userId=<%=userId%>&tMeasType='+tMeasType+'&tMeasVal='+tMeasVal;
                                else
                                    complteUrl= '<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=setScoreCardMemberDetails&isDimBased='+isDimBased+'&measId='+selMultiMeasId+'&dimId='+selMeasureId+'&dimValue='+selMeasName+'&units='+unit+'&period='+period+'&scoreBasis='+scoreBase+'&sCards='+sCardValArry.toString()+'&operators='+OperatorArry.toString()+'&stValues='+stValueArry.toString()+'&endValues='+endValueArry.toString()+'&currMeasId='+$("#selectedMeasureMulti").val()+'&currDimValue='+$("#selectedMeasure").val()+'&userId=<%=userId%>&tMeasType='+tMeasType+'&tMeasVal='+tMeasVal;
                                $.post(complteUrl, $("#applyRuleForm").serialize() ,
                                function(data){
                                      $("#targetMeasText").attr('elementid','')
                                      $("#targetMeasText").val("")
                                 });
//                                $.ajax({
//                                    url:complteUrl,
//                                    success: function(data){
////                                        if(data!="")
////                                        {
//                                          $("#targetMeasText").attr('elementid','')
//                                          $("#targetMeasText").val("")
////                                        }
//                                    }
//                                });
                                 return true;
                            }else{
                              return false;
                            }
                        }
                        else
                        {
                                if(isDimBased==false)
                                    complteUrl= '<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=setScoreCardMemberDetails&isDimBased='+isDimBased+'&measId='+selMeasureId+'&measName='+selMeasName+'&units='+unit+'&period='+period+'&scoreBasis='+scoreBase+'&sCards='+sCardValArry.toString()+'&operators='+OperatorArry.toString()+'&stValues='+stValueArry.toString()+'&endValues='+endValueArry.toString()+'&currMeasId='+$("#selectedMeasure").val()+'&userId=<%=userId%>&tMeasType='+tMeasType+'&tMeasVal='+tMeasVal;
                                else
                                    complteUrl= '<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=setScoreCardMemberDetails&isDimBased='+isDimBased+'&measId='+selMultiMeasId+'&dimId='+selMeasureId+'&dimValue='+selMeasName+'&units='+unit+'&period='+period+'&scoreBasis='+scoreBase+'&sCards='+sCardValArry.toString()+'&operators='+OperatorArry.toString()+'&stValues='+stValueArry.toString()+'&endValues='+endValueArry.toString()+'&currMeasId='+$("#selectedMeasureMulti").val()+'&currDimValue='+$("#selectedMeasure").val()+'&userId=<%=userId%>&tMeasType='+tMeasType+'&tMeasVal='+tMeasVal;
                                 $.post(complteUrl, $("#applyRuleForm").serialize() ,
                                function(data){
                                      $("#targetMeasText").attr('elementid','')
                                      $("#targetMeasText").val("")
                                 }); 
//                                $.ajax({
//                                    url:complteUrl,
//                                    success: function(data){
////                                        if(data!="")
////                                        {
//                                           $("#targetMeasText").attr('elementid','')
//                                           $("#targetMeasText").val("")
////                                        }
//                                    }
//                                });
                                 return true;
                        }
                    }
                    else{
                        return false;
                    }
                }
                if(currMeasureId=="select")
                {
                       if(isDimBased)
                       {
                          currMeasureId = $("#selectedMeasure option:selected").text();
                       }
                       else
                         currMeasureId = $("#selectedMeasure").val();
                }
                if(currMeasMulti=="select")
                {
                  currMeasMulti = $("#selectedMeasureMulti").val();
                }

            }


            function validateScorecardRules(sVal,oper,stVal,endVal){

                var measure=$("#selectedMeasure").val();
                // var weightage=$("#weightage").val();
                if(measure=="select"){
                    alert("Please select measure")
                    return false
                }

                if(sVal.length>0){

                    for(var i=0;i<sVal.length;i++){
                        if(sVal[i]=="" || stVal[i]=="" || stVal[i]==""){
//                            alert("Please enter all the values");
                            return false;
                        }else if(oper[i].trim()=="<>" && (parseInt(stVal[i]))>=(parseInt(endVal[i]))){
                            alert("End value should be greater then the start value")
                            return false;
                        }else if(endVal[i]!="null" && (isNaN(sVal[i])|| isNaN(stVal[i])|| isNaN(endVal[i]))){
                            alert("Please enter numbers only!")
                            return false;
                        }else if(endVal[i]=="null" && (isNaN(sVal[i]) || isNaN(stVal[i]))){
                            alert("Please enter numbers only")
                            return false;
                        }else if(oper[i].trim()=="select"){
                            alert("Please select an operator")
                            return false;
                        }

                    }
                }
                return true;

            }
            function applyColors(){
                $("#applyColorsDiv").dialog('open')
             }
            function showColEndValue(selBoxId){
                var num=selBoxId.substring(9);
                var operator= $("#"+selBoxId).val();
                //alert(operator)
                if(operator=="<>"){
                    $("#colEndTD"+num).show();
                }else{
                    $("#colEndTD"+num).hide();
                }
            }
            var colCt=1;

            function addColorRow(){
                var tableHtml="";
                tableHtml+="<tr id='colorTR"+colCt+"'>"
                tableHtml+="<td> Apply color as <select id='colorCodeId"+colCt+"'>"
                tableHtml+="<option value='select'>select</option>"
                tableHtml+="<option value='Red'>Red</option>"
                tableHtml+="<option value='Green'>Green</option>"
                tableHtml+="<option value='Blue'>Blue</option>"
                tableHtml+="</select></td>";
                tableHtml+="<td>When score <select id='colOperId"+colCt+"' onchange='showColEndValue(this.id)'>";
                tableHtml+="<option value='<>'><></option>";
                tableHtml+="<option value='>'>></option>";
                tableHtml+="<option value='<'><</option>";
                tableHtml+="<option value='>='>>=</option>";
                tableHtml+="<option value='<='><=</option>";
                tableHtml+="<option value='=='>==</option>";
                tableHtml+="<option value='!='>!=</option>";
                //tableHtml+="<option value='<>'><></option>";
                tableHtml+="</select></td><td>Start Value <input type='text' value='' style='width:100px' id='colStartId"+colCt+"'></td>";
                tableHtml+="<td  id='colEndTD"+colCt+"' >End Value <input type='text' value='' style='width:100px' id='colEndId"+colCt+"'></td>";
                tableHtml+="<tr>"
                $('#sCardColorTable tr:last').after(tableHtml);
                colCt++;
            }

            function deleteColorRow(){
                if(colCt>1){
                    colCt--;
                    $("#colorTR"+colCt).remove();
                }
            }

            function saveColors(){
                alert("1")
                var colCode;
                var oper;
                var stVal;
                var endVal;
                var isColorValid
                var colType=changeColorRuleType();
                alert("colType\t"+colType)
                if(colType=="Prior"){
                    colorType="Prior";
                    $("#applyColorsDiv").dialog('close')
                }else{
                     //alert("2")
                    for(var i=0;i<colCt;i++){
                       //  alert("3")
                        isColorValid=false;
                        colCode=$("#colorCodeId"+i).val();
                        oper=$("#colOperId"+i).val();
                        stVal=$("#colStartId"+i).val();
                        endVal=$("#colEndId"+i).val();
                        isColorValid=validateColors(colCode,oper,stVal,endVal);
                        if(isColorValid==true){
                            colCodeArry.push(colCode);
                            colOperArry.push(oper);
                            colStartArry.push(stVal);
                            if(oper.trim()=="<>")
                                colEndArry.push(endVal);
                            else
                                colEndArry.push("null");

                            if(i==colCt-1 && isColorValid==true){
                                $("#applyColorsDiv").dialog('close')
                            }
                        }

                    }
                    if(colType=="TrgtDeviation")
                     colorType="TrgtDeviation";
                    else
                     colorType="Target";
                }
            }
            function cancelColors(){
                $("#applyColorsDiv").dialog('close')
                colCodeArry=new Array();
                colOperArry=new Array();
                colStartArry=new Array();
                colEndArry=new Array();
            }
            function changeColorRuleType(){
//                var type=$('#ruleBase').attr('checked')
//alert("colType\t"+$('#trgtBase').attr('checked'))
                if($('#ruleBase').attr('checked')==true ){
                    $('#addColRowButt').attr("disabled", false);
                    $('#deleteColRowButt').attr("disabled", false);
                    $('#sCardColorTable').show();
                    return "Rule";
                }else if($('#trgtBase').attr('checked')==true){
                     $('#addColRowButt').attr("disabled", false);
                    $('#deleteColRowButt').attr("disabled", false);
                    $('#sCardColorTable').show();
                    return "TrgtDeviation";
                }else{
                    $('#addColRowButt').attr("disabled", true);
                    $('#deleteColRowButt').attr("disabled", true);
                    $('#sCardColorTable').hide();
                    return "Prior";
                }

            }
            function validateColors(colCode,oper,stVal,endVal){
                if(colCode=="select"){
                    alert("Please select a color");
                    return false;
                }else if(stVal==""){
                    alert("Please enter start value ");
                    return false;
                }else if(oper.trim()!="<>" &&  isNaN(stVal)){
                    alert("Please enter numbers only")
                    return false;
                }else if(oper.trim()=="<>" && endVal=="" ){
                    alert("Please enter end value")
                    return false;
                }
                else if(oper.trim()=="select"){
                    alert("Please select an operator")
                    return false;
                }
                else if(oper.trim()=="<>" && (parseInt(stVal))>=(parseInt(endVal))){
                    alert("End value should be greater then the start value")
                    return false;
                }else if(oper.trim()=="<>" &&  (isNaN(endVal))){
                    alert("Please enter numbers only!")
                    return false;
                }else{
                    return true;
                }
            }
            function test(id)
            {
                var val=parseFloat($("#"+id).val());

            }
            function calculateTotal(id){
                var weigh=0;
                var measuresIdsInWeight=new Array();
                if(isDimBased && $("#weightageMeasSelected").val()!="weightForMeas")
                    measuresIdsInWeight=dimMemsArry;
                else
                    measuresIdsInWeight=measIdsArry;

                for(var i=0;i<measuresIdsInWeight.length;i++){
                    if($("#measWeigh"+i).val()!=""){
                        weigh+=parseFloat($("#measWeigh"+i).val());
                    }else{
                        weigh+=0;
                        $("#measWeigh"+i).val(0);
                    }
                }
                weigh=Math.round(weigh)

                //                if(weigh>100)
                //                {
                //                    var remVal=weigh-100;
                //                    var num=measIdsArry.length-1;
                //                    var currVal=$("#measWeigh"+num).val()
                //                    $("#measWeigh"+num).val(currVal-remVal)
                //                }
                //                //$("#totWeighTd").html(weigh);
                //                 var weigh=0;
                //                for(var i=0;i<measIdsArry.length;i++)
                //                {
                //                    if($("#measWeigh"+i).val()!=""){
                //                        weigh+=parseFloat($("#measWeigh"+i).val());
                //                    }else{
                //                        weigh+=0;
                //                        $("#measWeigh"+i).val(0);
                //                    }
                //                }
                $("#totWeighTd").html(weigh);
            }
            var selMeasId;
            function applyWeightageInDimensionLevel()
            {   //saveScorecardRules();
                $("#applyRuleDiv").dialog('close')
                $("#weightageDiv").dialog('open')
                $("#weightageDiv").attr("title","Apply Weightage for Dimension Members")
                var measCt=dimMemsArry.length;
                var avgWeigh=100/measCt;
                var weighTable="";
                if(avgWeigh.toString().indexOf(".")!=-1){
                       avgWeigh=avgWeigh.toString().substring(0,avgWeigh.toString().indexOf(".")+3);
                  }
                selMeasId=measIdsArry[0];
                weighTable="<table align='center'><tr><td style='width: 49%;' class='myHead'>Measure</td>";
                weighTable+="<td></td><select id='weightageMeasSelected' onchange='saveWeightageInDimLevel()'>";
                for(var i=0;i<measIdsArry.length;i++)
                {
                    weighTable+="<option value="+measIdsArry[i]+">"+measNameArry[i]+"</option>"
                }
                 weighTable+="<option value='weightForMeas'>Weightage For Measures</option>"
                weighTable+="</select></tr></table>";
                weighTable+="<table border='1' id='weightageTable' style='width: 98%;' align='center'>";
                var total=0;
                weighTable+="<thead><tr><td valign='top' style='width: 49%;' class='myHead'>Dimension Members</td><td valign='top' style='width: 49%;' class='myHead'>Weightage</td></thead>";
                weighTable+="<tbody id='weightageBody'>";
                var weight=0;
                for(var i=0;i<dimMemsArry.length;i++){
                    weighTable+="<tr>";
                    weighTable+="<td>"
                    weighTable+=dimMemsArry[i];
                    weighTable+="</td>";
                    weighTable+="<td id="+dimMemsArry[i].replace(" ","~","gi")+">";

                    weighTable+="<input type='text' style='width: 100%;' onchange='calculateTotal(this.id)' id='measWeigh"+i+"' value="+avgWeigh+">";

                    weighTable+="</td>";
                    weighTable+="</tr>";
                    total=total+avgWeigh;
                }
                weighTable+="<tr><td valign='top' style='width: 49%;' class='myHead'>Total</td><td id='totWeighTd'>100</td></tr>"
                weighTable+="</tbody>";
                weighTable+="</table>";
                weighTable+="<table align='center'><tr><td><input type='button' class='navtitle-hover' value='Back' onclick='goBackToRulesDiv()'></td>"
                weighTable+="<td><input type='button' value='Finish' class='navtitle-hover' onclick='saveAllScorecardDetails()'></td></tr></table>"
                 $("#weightageDiv").html(weighTable);
                var id="";
                if(mode=="Edit")
                {
//                   if($("#weightageMeasSelected").val()!=undefined && $("#weightageMeasSelected").val()!="")
                       id=$("#weightageMeasSelected").val();
//                   else
//                       id=selMeasId;
                   $.ajax({
                        url:"scorecardDesign.do?scorecardParam=getWeightageJsonForComponents&isDimBased="+isDimBased+"&currMeasId="+id,
                        success: function(data){
                              if(data!="")
                                  {
                                        var json=eval("("+data+")")
                                        if(id=="weightForMeas")
                                        {
                                            for(var i=0;i<measIdsArry.length;i++)
                                          {
                                              if(json[measIdsArry[i]]!="0.0")
                                              $("#measWeigh"+i).val(json[measIdsArry[i]]);
                                          }

                                        }else{
                                           for(var i=0;i<dimMemsArry.length;i++)
                                          {
                                              if(json[dimMemsArry[i].replace(" ","~","gi")]!="0.0")
                                              $("#measWeigh"+i).val(json[dimMemsArry[i].replace(" ","~","gi")]);
                                          }

                                        }
                                  }
                        }

                    });
                }
               
            }
            function saveWeightageInDimLevel()
            {
                saveWeightage();

                var selectedVal=$("#weightageMeasSelected").val();
                var weighTable="";
                var total=0;
                var avgWeigh=100/(measIdsArry.length);
                if(avgWeigh.toString().indexOf(".")!=-1){
                       avgWeigh=avgWeigh.toString().substring(0,avgWeigh.toString().indexOf(".")+3);
                   }
                if(selectedVal=="weightForMeas")
                {
                     weighTable+="<thead><tr><td valign='top' style='width: 49%;' class='myHead'>Measures</td><td valign='top' style='width: 49%;' class='myHead'>Weightage</td></thead>";
                     weighTable+="<tbody id='weightageBody'>";
                     for(var i=0;i<measIdsArry.length;i++){
                            weighTable+="<tr>";
                            weighTable+="<td>"
                            weighTable+=measNameArry[i];
                            weighTable+="</td>";
                            weighTable+="<td id="+measIdsArry[i]+">";

                                weighTable+="<input type='text' style='width: 100%;' onchange='calculateTotal(this.id)' id='measWeigh"+i+"' value="+avgWeigh+">";

                            weighTable+="</td>";
                            weighTable+="</tr>";
                            total=total+avgWeigh;
                        }
                    weighTable+="<tr><td valign='top' style='width: 49%;' class='myHead'>Total</td><td id='totWeighTd'>100</td></tr>"
                    weighTable+="</tbody>";
                    $("#weightageTable").html(weighTable);
                }
                else{
                    weighTable="";
                    avgWeigh=100/(dimMemsArry.length);
                    if(avgWeigh.toString().indexOf(".")!=-1){
                       avgWeigh=avgWeigh.toString().substring(0,avgWeigh.toString().indexOf(".")+3);
                     }
                    weighTable+="<thead><tr><td valign='top' style='width: 49%;' class='myHead'>Dimension Members</td><td valign='top' style='width: 49%;' class='myHead'>Weightage</td></thead>";
                    weighTable+="<tbody id='weightageBody'>";
                    var weight=0;
                    for(var i=0;i<dimMemsArry.length;i++){
                        weighTable+="<tr>";
                        weighTable+="<td>"
                        weighTable+=dimMemsArry[i];
                        weighTable+="</td>";
                        weighTable+="<td id="+dimMemsArry[i].replace(" ","~","gi")+">";

                        weighTable+="<input type='text' style='width: 100%;' onchange='calculateTotal(this.id)' id='measWeigh"+i+"' value="+avgWeigh+">";

                        weighTable+="</td>";
                        weighTable+="</tr>";
                        total=total+avgWeigh;
                    }
                    weighTable+="<tr><td valign='top' style='width: 49%;' class='myHead'>Total</td><td id='totWeighTd'>100</td></tr>"
                    weighTable+="</tbody>";
                    $("#weightageTable").html(weighTable);
                }
                $.ajax({
                        url:"scorecardDesign.do?scorecardParam=getWeightageJsonForComponents&isDimBased="+isDimBased+"&currMeasId="+$("#weightageMeasSelected").val(),
                        success: function(data){
                              if(data!="")
                                  {
                                        var json=eval("("+data+")")
                                        if(selectedVal=="weightForMeas")
                                        {
                                            for(var i=0;i<measIdsArry.length;i++)
                                          {
                                              if(json[measIdsArry[i]]!="0.0")
                                              $("#measWeigh"+i).val(json[measIdsArry[i]]);
                                          }

                                        }else{
                                           for(var i=0;i<dimMemsArry.length;i++)
                                          {
                                              if(json[dimMemsArry[i].replace(" ","~","gi")]!="0.0")
                                              $("#measWeigh"+i).val(json[dimMemsArry[i].replace(" ","~","gi")]);
                                          }

                                        }
                                  }
                        }

                    });
                
                   
            }

            

            function applyWeightage(){
                var isValid=saveScorecardRules();
                var start = new Date().getTime();
                var cur = start
                while(cur - start < 2000)
                {
                  cur = new Date().getTime();
                }

                $.ajax({
                        url:'scorecardDesign.do?scorecardParam=allRulesAppliedForComponents',
                        success: function(data){
                          if(data=="false")
                              alert("Please assign rules for all Components")
                          else
                          {
                            if(isDimBased){
                                applyWeightageInDimensionLevel();
                            }else{

                                var measuresIdsInWeight=new Array();
                                var measureNamesInWeight=new Array();
                                if(isDimBased==false && isValid==false ){
                                    alert("Please assign rules for all measures")
                                }
                                else
                                {
                                    $("#applyRuleDiv").dialog('close')
                                    $("#weightageDiv").dialog('open')
                                    $("#weightageDiv").attr("title","Apply Weightage for Measures")
                                    var measCt=0;
                                    measCt=measIdsArry.length;
                                    measuresIdsInWeight=measIdsArry;
                                    measureNamesInWeight=measNameArry;
                                    var avgWeigh=100/measCt;
                                    if(avgWeigh.toString().indexOf(".")!=-1){
                                        avgWeigh=avgWeigh.toString().substring(0,avgWeigh.toString().indexOf(".")+3);
                                    }
                                    var weighTable="<table border='1' id='weightageTable' style='width: 98%;' align='center'>";
                                    var total=0;
                                    weighTable+="<thead><tr><td valign='top' style='width: 49%;' class='myHead'>Measure</td><td valign='top' style='width: 49%;' class='myHead'>Weightage</td></thead>";
                                    weighTable+="<tbody>";
                                    var weight=0;
                                    for(var i=0;i<measuresIdsInWeight.length;i++){
                                        weighTable+="<tr>";
                                        weighTable+="<td>"
                                        weighTable+=measureNamesInWeight[i];
                                        weighTable+="</td>";
                                        weighTable+="<td id="+measuresIdsInWeight[i]+">";
                                        if(measWeigh.length!=0){
                                            if(measWeigh[i]==""||measWeigh[i]==undefined||measWeigh[i]==null)
                                                weight=0;
                                            else
                                                weight=measWeigh[i];
                                            weighTable+="<input type='text' style='width: 100%;' onchange='calculateTotal(this.id)' id='measWeigh"+i+"' value="+weight+">";
                                        }else{
                                            weighTable+="<input type='text' style='width: 100%;' onchange='calculateTotal(this.id)' id='measWeigh"+i+"' value="+avgWeigh+">";
                                        }
                                        weighTable+="</td>";
                                        weighTable+="</tr>";
                                        total=total+avgWeigh;
                                    }
                                    weighTable+="<tr><td valign='top' style='width: 49%;' class='myHead'>Total</td><td id='totWeighTd'>100</td></tr>"
                                    weighTable+="</tbody>";
                                    weighTable+="</table>";
                                    weighTable+="<table align='center'><tr><td><input type='button' class='navtitle-hover' value='Back' onclick='goBackToRulesDiv()'></td>"
                                    weighTable+="<td><input type='button' value='Finish' class='navtitle-hover' onclick='saveAllScorecardDetails()'></td></tr></table>"

                                    $("#weightageDiv").html(weighTable);
                                }

                                $.ajax({
                                    url:"scorecardDesign.do?scorecardParam=getWeightageJsonForMeasures",
                                    success: function(data){
                                          if(data!="")
                                              {
                                                  var json=eval("("+data+")")

                                                    for(var i=0;i<measIdsArry.length;i++)
                                                  {

                                                      if(mode=="Edit")
                                                          {
                                                             if(json[measIdsArry[i]]!="0.0")
                                                                $("#measWeigh"+i).val(json[measIdsArry[i]]);
                                                             else
                                                                $("#measWeigh"+i).val(0);
                                                          }
                                                          else{
                                                              if(json[measIdsArry[i]]!="0.0")
                                                                $("#measWeigh"+i).val(json[measIdsArry[i]]);
                                                              else
                                                                 $("#measWeigh"+i).val(avgWeigh);
                                                          }
                                                  }


                                              }
                                    }
                                });

                            }
                          }
                        }
                });

                
            }
            function cancelWeigtage(){
                // close=0;
                $("#weightageDiv").dialog('close') ;
                // close=1;
                measWeigh=new Array();
            }

            function saveWeightage()
            {
                //alert("selMeasId---"+selMeasId)
                 var weightType=selMeasId;
                 if(weightType=="weightForMeas")
                     weightType="weightForMeas";
                 else
                      weightType="weightForDims";

                measIds=new Array();
                measWeigh=new Array();
                var weighTabObj=document.getElementById("weightageTable");
                var tBodyObj=weighTabObj.getElementsByTagName('tbody');
                var trObjs=tBodyObj[0].getElementsByTagName('tr');
                var tdObj;
                var inputObj;

                var totWeigh=$("#totWeighTd").html();
                if(totWeigh*1!=100){
                    alert("Total weightage should be 100");
                    return false;
                }else{
                    for(var i=0;i<trObjs.length-1;i++){
                        tdObj=trObjs[i].getElementsByTagName('td');
                        //alert(tdObj[1].id);
                        measIds.push(tdObj[1].id.replace("~"," ","gi"));
                        inputObj=tdObj[1].getElementsByTagName('input');
                        // alert($("#"+inputObj[0].id).val())
                        measWeigh.push($("#"+inputObj[0].id).val());
                    }
                    $.ajax({
                        url:"scorecardDesign.do?scorecardParam=setWeightageForComponents&weightType="+weightType+"&isDimBased="+isDimBased+"&measIds="+measIds+"&measWeight="+measWeigh+"&selectedMeasId="+selMeasId+"&currMeasId="+$("#weightageMeasSelected").val(),
                        success: function(data){
                            selMeasId=$("#weightageMeasSelected").val();
                        }

                    });
                    return true;
                }
            }

            function saveAllScorecardDetails()
            {
                selMeasId=$("#weightageMeasSelected").val();
                var weightType=selMeasId;
                 if(weightType=="weightForMeas")
                     weightType="weightForMeas";
                 else
                      weightType="weightForDims";

                measIds=new Array();
                measWeigh=new Array();
                var weighTabObj=document.getElementById("weightageTable");
                var tBodyObj=weighTabObj.getElementsByTagName('tbody');
                var trObjs=tBodyObj[0].getElementsByTagName('tr');
                var tdObj;
                var inputObj;

                var totWeigh=$("#totWeighTd").html();
                if(totWeigh*1!=100){
                    alert("Total weightage should be 100");
                    return false;
                }else{
                    for(var i=0;i<trObjs.length-1;i++){
                        tdObj=trObjs[i].getElementsByTagName('td');
                        //alert(tdObj[1].id);
                        measIds.push(tdObj[1].id.replace("~"," ","gi"));
                        inputObj=tdObj[1].getElementsByTagName('input');
                        // alert($("#"+inputObj[0].id).val())
                        measWeigh.push($("#"+inputObj[0].id).val());
                    }
                    $.ajax({
                        url:"scorecardDesign.do?scorecardParam=setWeightageForComponents&weightType="+weightType+"&isDimBased="+isDimBased+"&measIds="+measIds+"&measWeight="+measWeigh+"&selectedMeasId="+selMeasId+"&currMeasId="+$("#weightageMeasSelected").val(),
                        success: function(data){
                            selMeasId=$("#weightageMeasSelected").val();

                                $.ajax({
                                    url:"scorecardDesign.do?scorecardParam=insertScoreCardDetails&mode="+mode,
                                    success: function(data){
                                        $("#weightageDiv").dialog('close');
                                        if(data=="Edit")
                                          alert("Scorecard updated successfully")
                                        else if(data=="Create")
                                           alert("Scorecard created successfully")
                                        else if(data=="failureCreate")
                                            alert("Scorecard creation failed, please create again")
                                        else
                                            alert("Scorecard updation failed, please try again")

                                        document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                                        document.forms.scorecardForm.submit();
                                    }

                                });
                                $("#weightageDiv").dialog('close');

                        }

                    });

                }
               
               
            }
            function goToBackDiv(){
                $("#applyRuleDiv").dialog('close')
                if(isDimBased){
                    saveDimensions();
                    //$("#dimensionValuesDiv").dialog('open');
                }
                else{
                    isTargetBased=false;
                    grpColArray=new Array();
                    $("#sortable").html("")
                    for(var i=0;i<measIdsArry.length;i++)
                    {
                            createColumn(measIdsArry[i],measNameArry[i],"sortable")
                    }
                    $("#createScoreDiv").dialog('open');
                    $("#ui-dialog-title-createScoreDiv").html('Create Scorecard')
                    $("#masterTable").show();
                    $("#doneTarget").hide();
                    $("#nextMaster").show();
                }


            }
            function goBack(){
                grpColArray=new Array();
                isTargetBased=false;
                $("#sortable").html("")
                for(var i=0;i<measIdsArry.length;i++)
                {
                        createColumn(measIdsArry[i],measNameArry[i],"sortable")
                }
                $("#dimensionDiv").dialog('close')
                $("#createScoreDiv").dialog('open');
                $("#ui-dialog-title-createScoreDiv").html('Create Scorecard')
                $("#masterTable").show();
                $("#doneTarget").hide();
                $("#nextMaster").show();

            }
            function goBackToRulesDiv(){
                var scorecard;
                var isNested=false;
//                $("#scorecardTable").hide();
                for( var i=0;i<measIdsArry.length;i++){
                   scorecard=measIdsArry[i].substring(0, 6);
                   if(scorecard=="Scards"){
                       isNested=true;
                      // break;
                   }else{
                       isNested=false;
                   }
                }

                $("#weightageDiv").dialog('close');
                if(isNested)
                 $("#createScoreDiv").dialog('open')
                else
                 $("#applyRuleDiv").dialog('open')
            }
            function editScoreCard(){
                mode="Edit";
                var scoreSelectObj=document.getElementsByName("ScoreSelect");
                var editscoreids = new Array();
                for(var i=1;i<scoreSelectObj.length;i++){
                    if(scoreSelectObj[i].checked){
                        editscoreids.push(scoreSelectObj[i].value);
                    }
                    else{
                        scoreSelectObj[i].checked=false;
                    }
                }
                if(editscoreids.length==0){
                    alert("Please select one Scorecard")
                }else if(editscoreids.length>1){
                    alert("Please select only one Scorecard")
                }else{
                    // $("select#selectBusRoles").attr("value","1203");
                    sCardId=editscoreids.toString();

                    $.ajax({
                        url:"scorecardDesign.do?scorecardParam=editScoreCard&editscoreid="+editscoreids+"&userId=<%=userId%>",
                        success: function(data){
                            if(data=="false")
                            {
                                    alert("Edit privilege not available");
                            }
                            else
                            if(data!=""){
                                var totalData=data.split("||");
                                var json=eval("("+totalData[0]+")")
                                var jsonMeas=eval("("+totalData[1]+")")

                                isDimBased=json.isDimensionBased;
                                if(isDimBased==true)
                                    $("#dimCheckId").attr('checked','checked');
                                $("#sortable").html("");
                                for(var i=0;i<jsonMeas.ElementIds.length;i++){
                                    createColumn(jsonMeas.ElementIds[i], jsonMeas.ElementNames[i],'sortable');
                                }
                                grpColArray=new Array();
                                $("#scoreNameId").val(json.scoreCardName);
                                $("#areaId").val(json.scoreCardArea);
                                $("#targetScoreId").val(json.targetScore);
                                $("select#selectBusRoles").attr("value",json.folderId);

                                getMeasures();


                                // alert(json.Lights[0].ColorScardValues[1])
                                if(json.colorList!=""||json.colorList.length!=0){

                                    for(var i=0;i<json.colorList.length;i++){
                                        if(i!=0)
                                            addColorRow();
                                        $("select#colorCodeId"+i).attr("value",json.colorList[i].color)
                                        $("select#colOperId"+i).attr("value",json.colorList[i].operator)
                                        $("#colStartId"+i).val(json.colorList[i].startValue);
                                        if(json.colorList[i].operator=="<>")
                                        $("#colEndId"+i).val(json.colorList[i].endValue);
                                        else
                                            $("#colEndTD"+i).hide();


                                    }
                                    $("#ruleBase").attr('checked',true);
                                }else{
                                    $("#priorBase").attr('checked',true);
                                    $('#sCardColorTable').hide();
                                }
                               $("#createScoreDiv").dialog('open')
                            }
                        }
                    });

                }
            }

            function changeValuesAsPerUnit()
            {
                var selMeasureId="";
                if(isDimBased==true){
                    selMeasureId=$("#selectedMeasureMulti").val();
                }else{
                    selMeasureId=$("#selectedMeasure").val();
                }
                var units=$("#unit").val();
                // var selMeasureId=$("#selectedMeasure").val();
                var period=$("#period").val();
                var dimValue=$("#selectedMeasure  option:selected").text().trim();
                // $("#yourdropdownid option:selected").text();

                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=changeValuesAsPerUnit&isDimBased="+isDimBased+"&dimValue="+dimValue+"&unitType="+units+"&measureId="+selMeasureId+"&period="+period,
                    success: function(data){
                        if(data!=""){

                            var jsondata=eval("("+data+")")
                            // alert(jsondata.Max)
                            $("#maxInPeriod").val(jsondata.Max);
                            $("#minInPeriod").val(jsondata.Min);
                            $("#avgInPeriod").val(jsondata.Avg);
                            $("#lastPeriodValue").val(jsondata.LastPeriodValue);
                            $("#avgLast3").val(jsondata.LastThreePeriodValue);
                        }else{
                            $("#maxInPeriod").val(0);
                            $("#minInPeriod").val(0);
                            $("#avgInPeriod").val(0);
                            $("#lastPeriodValue").val(0);
                            $("#avgLast3").val(0);
                        }
                    }
                });

            }
            function getCopyScoreCardId(){
                var scoreSelectObj=document.getElementsByName("ScoreSelect");
                var editscoreids = new Array();
                for(var i=1;i<scoreSelectObj.length;i++){
                    if(scoreSelectObj[i].checked){
                        editscoreids.push(scoreSelectObj[i].value);
                    }
                    else{
                        scoreSelectObj[i].checked=false;
                    }
                }
                if(editscoreids.length==0){
                    alert("Please select one Scorecard")
                    return "";
                }else if(editscoreids.length>1){
                    alert("Please select only one Scorecard")
                    return "" ;
                }else{
                    return editscoreids;
                }
            }
            function openCopyScoreDiv(){
                var copyScardId=getCopyScoreCardId();
                if(copyScardId!="")
                {
                    $("#copyScorecardDiv").dialog('open')
                }
            }
            function copyScoreCard()
            {

                var copyScardId=getCopyScoreCardId();
                var copyScardName=$("#copyScoreName").val();
                var copyScardDesc=$("#copyDescId").val();
//                checkScorecardName('copyDescId')
                if(copyScardId!="" && copyScardName!="")
                {
//                    if(isExists==false)
//                    {
//                             alert("Scorecard name already exists! Please enter different name")
//                    }else{
                    $.ajax({
                        url:"scorecardDesign.do?scorecardParam=copyScoreCard&copyscoreid="+copyScardId+"&copyScardName="+copyScardName+"&copyScardDesc="+copyScardDesc+"&userId=<%=userId%>",
                        success: function(data){

                            $("#copyScorecardDiv").dialog('close')
                            if(data=="failed")
                            {
                                alert("Scorecard copied Failed")
                                document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                                document.forms.scorecardForm.submit();
                            }else{
                                alert("Scorecard copied successfully")
                                document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                                document.forms.scorecardForm.submit();
                            }
                             
                        }
                    });
//                    }
                }
            }
            function refreshPage(){

                document.forms.scorecardForm.action = "<%=request.getContextPath()%>/home.jsp#Scorecard";
                document.forms.scorecardForm.submit();
            }

            function checkScorecardName(id)
            {
                var sCardName=$("#"+id).val();
                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=checkScorecardName&sCardName="+sCardName,
                    success: function(data)
                    {   //alert(data)
                        if(data=="false")
                            isExists= false;
                        else
                            isExists= true;
                    }
                });

            }

            function changeScoreBasis()
            {
                var selMeasureId=$("#selectedMeasure").val();
                var period=$("#period").val();
                var scoreBasis=$("#scoreBaseSelectId").val();
                var dimValue=$("#selectedMeasure  option:selected").text().trim();
                if(scoreBasis=="Target Deviation" || scoreBasis=="Target Deviation%")
                    {
//                        $("#maxInPeriod").val("");
//                        $("#minInPeriod").val("");
//                        $("#avgInPeriod").val("");
//                        $("#lastPeriodValue").val("");
//                        $("#avgLast3").val("");
                        
                        $("#targetTrType").show();
                        $("#targetTr").show();
                       
                    }else{
                        $("#targetTrType").hide();
                        $("#targetTr").hide();
                       
                    }
                dims="";
                for(var i=0;i<dimMemsArry.length;i++){
                    dims+=dimMemsArry[i];
                    if(i!=dimMemsArry.length-1)
                        dims+="~";
                }
                if(isDimBased){
                    selMeasureId=$("#selectedMeasureMulti").val();
                }
                
                if(scoreBasis=="Target Deviation" || scoreBasis=="Target Deviation%")
                    {
                        scoreBasis=encodeURIComponent("Absolute");
                    }
                        scoreBasis=encodeURIComponent(scoreBasis);
                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=changeScoreBasis&isDimBased="+isDimBased+"&dimId="+dimensionId+"&dimValues="+dims+"&selDim="+dimValue+"&scoreBasis="+scoreBasis+"&measureId="+selMeasureId+"&period="+period+"&folderId="+busFolderId+"&userId=<%=userId%>",
                    success: function(data)
                    {
                        var json=eval("("+data+")")
                        // alert(json[0].measMetricsLst.Year[0].label)

                        if(period=="Year"){
                                $("#maximumTd").html(json.measMetricsLst.Year[0].label);
                                $("#maxInPeriod").val(json.measMetricsLst.Year[0].measValue);
                                $("#minimumTd").html(json.measMetricsLst.Year[1].label);
                                $("#minInPeriod").val(json.measMetricsLst.Year[1].measValue);
                                $("#averageTd").html(json.measMetricsLst.Year[2].label);
                                $("#avgInPeriod").val(json.measMetricsLst.Year[2].measValue);
                                $("#lastPeriodTd").html(json.measMetricsLst.Year[3].label);
                                $("#lastPeriodValue").val(json.measMetricsLst.Year[3].measValue);
                                if(json.measMetricsLst.Year[4].measValue!="null")
                                {
                                    $("#averageLast3Td").html(json.measMetricsLst.Year[4].label);
                                    $("#avgLast3").val(json.measMetricsLst.Year[4].measValue);
                                    $("#avgOfLast3Table").show();
                                }else{
                                    $("#avgOfLast3Table").hide();
                                }

                            }
                            else if(period=="Quarter")
                            {
                                $("#maximumTd").html(json.measMetricsLst.Qtr[0].label);
                                $("#maxInPeriod").val(json.measMetricsLst.Qtr[0].measValue);
                                $("#minimumTd").html(json.measMetricsLst.Qtr[1].label);
                                $("#minInPeriod").val(json.measMetricsLst.Qtr[1].measValue);
                                $("#averageTd").html(json.measMetricsLst.Qtr[2].label);
                                $("#avgInPeriod").val(json.measMetricsLst.Qtr[2].measValue);
                                $("#lastPeriodTd").html(json.measMetricsLst.Qtr[3].label);
                                $("#lastPeriodValue").val(json.measMetricsLst.Qtr[3].measValue);
                                if(json.measMetricsLst.Qtr[4].measValue!="null")
                                {
                                    $("#averageLast3Td").html(json.measMetricsLst.Qtr[4].label);
                                    $("#avgLast3").val(json.measMetricsLst.Qtr[4].measValue);
                                    $("#avgOfLast3Table").show();
                                }else{
                                    $("#avgOfLast3Table").hide();
                                }
                            }
                            else if(period=="Month")
                            {
                                $("#maximumTd").html(json.measMetricsLst.Month[0].label);
                                $("#maxInPeriod").val(json.measMetricsLst.Month[0].measValue);
                                $("#minimumTd").html(json.measMetricsLst.Month[1].label);
                                $("#minInPeriod").val(json.measMetricsLst.Month[1].measValue);
                                $("#averageTd").html(json.measMetricsLst.Month[2].label);
                                $("#avgInPeriod").val(json.measMetricsLst.Month[2].measValue);
                                $("#lastPeriodTd").html(json.measMetricsLst.Month[3].label);
                                $("#lastPeriodValue").val(json.measMetricsLst.Month[3].measValue);
                                if(json.measMetricsLst.Month[4].measValue!="null")
                                {
                                    $("#averageLast3Td").html(json.measMetricsLst.Month[4].label);
                                    $("#avgLast3").val(json.measMetricsLst.Month[4].measValue);
                                    $("#avgOfLast3Table").show();
                                }else{
                                    $("#avgOfLast3Table").hide();
                                }
                            }
                            else if(period=="Day")
                            {
                                $("#maximumTd").html(json.measMetricsLst.Day[0].label);
                                $("#maxInPeriod").val(json.measMetricsLst.Day[0].measValue);
                                $("#minimumTd").html(json.measMetricsLst.Day[1].label);
                                $("#minInPeriod").val(json.measMetricsLst.Day[1].measValue);
                                $("#averageTd").html(json.measMetricsLst.Day[2].label);
                                $("#avgInPeriod").val(json.measMetricsLst.Day[2].measValue);
                                $("#lastPeriodTd").html(json.measMetricsLst.Day[3].label);
                                $("#lastPeriodValue").val(json.measMetricsLst.Day[3].measValue);
                                if(json.measMetricsLst.Day[4].measValue!="null")
                                {
                                    $("#averageLast3Td").html(json.measMetricsLst.Day[4].label);
                                    $("#avgLast3").val(json.measMetricsLst.Day[4].measValue);
                                    $("#avgOfLast3Table").show();
                                }else{
                                    $("#avgOfLast3Table").hide();
                                }
                            }
                    if(scoreBasis=="Absolute")
                    {
                        changeValuesAsPerUnit();
                    }
                        // changeValuesAsPerUnit();


                    }
                });
                
            }

            function viewScorecard(scardId){
//                alert("1\t"+scardId)
//
//           var ctxPath = '<%=request.getContextPath()%>';
//           alert("2")
           $("#scorecardId").val(scardId)
//                document.forms.scorecardForm.action = "scorecardDesign.do?scorecardParam=viewScorecard";
//                document.forms.scorecardForm.submit();
//                alert("3")
//               $('#other').click(function() {
            $('#scorecardForm').submit();
//});
            }

            function isDimensionBased(){
                var isChecked=document.getElementById("dimCheckId");
                if(isChecked.checked){
                    $("#Scorecards").hide();

                    return true;
                }
                else{
                    $("#Scorecards").show();
                    return false;
                }
            }
            function initDrag(id){

                $(".DimensionULClass").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                $("#"+id).droppable({
                    activeClass:"blueBorder",
                    accept:'.DimensionULClass',

                    drop: function(ev, ui) {
                        var divObj=document.getElementById(this.id);
                        var dropUlObj=divObj.getElementsByTagName("ul");
                        var dropUlId=dropUlObj[0].id;
                        var dragLiId=ui.draggable.attr('id');

                        var liObjs = document.getElementById(dragLiId);
                        var tableObjs = liObjs.getElementsByTagName("table");
                        var tableId=tableObjs[0].id;
                        var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                        var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                        var tdObjs =  trObjs[0].getElementsByTagName("td");
                        var content = tdObjs[0].innerHTML;

                        var childLI=document.createElement("li");
                        childLI.id="drop"+dragLiId;
                        childLI.style.width='200px';
                        childLI.style.height='auto';
                        childLI.style.color='white';
                        childLI.className='navtitle-hover';

                        var tableStr="<table  id="+tableId+">";
                        tableStr+="<tbody>";
                        tableStr+="<tr align='center'>";
                        tableStr+="<td><a class='ui-icon ui-icon-close' href=javascript:deleteDimension('drop" + dragLiId + "','"+id+"')></a></td>"
                        tableStr+="<td style='color: black;'>"+content+"</td>";
                        tableStr+="</tr>";
                        tableStr+="</tbody>";
                        tableStr+="</table>";
                        childLI.innerHTML=tableStr;
                        dropUlObj[0].appendChild(childLI);
                        $("#"+dragLiId).remove()
                    }
                })

            }
            function createDimensionElement(id)
            {
                       // var divObj=document.getElementById(this.id);
                        var dropUlObj=document.getElementById("dimSortable");
                        //var dropUlId=dropUlObj.id;
                        var dragUl=document.getElementById("dimensions")
                        var dragLiId=id;

                        var liObjs = document.getElementById(dragLiId);
                       // alert("liObjs-"+liObjs)
                        var tableObjs = liObjs.getElementsByTagName("table");
                        var tableId=tableObjs[0].id;
                        var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                        var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                        var tdObjs =  trObjs[0].getElementsByTagName("td");
                        var content = tdObjs[0].innerHTML;

                        var childLI=document.createElement("li");
                        childLI.id="drop"+dragLiId;
                        childLI.style.width='200px';
                        childLI.style.height='auto';
                        childLI.style.color='white';
                        childLI.className='navtitle-hover';

                        var tableStr="<table  id="+tableId+">";
                        tableStr+="<tbody>";
                        tableStr+="<tr align='center'>";
                        tableStr+="<td><a class='ui-icon ui-icon-close' href=javascript:deleteDimension('drop" + dragLiId + "')></a></td>"
                        tableStr+="<td style='color: black;'>"+content+"</td>";
                        tableStr+="</tr>";
                        tableStr+="</tbody>";
                        tableStr+="</table>";
                        childLI.innerHTML=tableStr;
                        dropUlObj.appendChild(childLI);
                        $("#"+dragLiId).remove()
            }
           
            function deleteDimension(elementId,id){
                // alert("elementid--"+elementId)
                var liObj=document.getElementById(elementId);
                var ulObj;
                if(id=='shareDropDiv')
                 ulObj=document.getElementById("shareDragUl");
                    else
                ulObj=document.getElementById("dimensions");
                // var ulObj=divObj.getElementsByTagName("ul");
                var id=liObj.id.replace("drop","");
                //  alert("id--"+id);
                var tableObjs = liObj.getElementsByTagName("table");
                var tableId=tableObjs[0].id;
                var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                var tdObjs =  trObjs[0].getElementsByTagName("td");
                var content = tdObjs[1].innerHTML;
                //  alert("conent--"+content)
                var childLI=document.createElement("li");
                childLI.id=id;
                childLI.style.width='200px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover DimensionULClass';

                var tableStr="<table  id="+tableId+">";
                tableStr+="<tbody>";
                tableStr+="<tr align='center'>";
                tableStr+="<td style='color: black;'>"+content+"</td>";
                tableStr+="</tr>";
                tableStr+="</tbody>";
                tableStr+="</table>";
                childLI.innerHTML=tableStr;
                ulObj.appendChild(childLI)
                $("#"+elementId).remove();
                initDrag(id);
            }


            function saveDimensions()
            {
                var ulObj=document.getElementById("dimSortable");
                var liObjs=ulObj.getElementsByTagName("li");
                var lis,tableObj,tbodyObj,trObj,tdObj;
                var dimIds=new Array();
                var dimNames=new Array();
                for(var i=0;i<liObjs.length;i++)
                {
                    dimIds.push(liObjs[i].id.replace("drop",""));
                    tableObj=liObjs[i].getElementsByTagName("table");
                    tbodyObj=tableObj[0].getElementsByTagName("tbody");
                    trObj=tbodyObj[0].getElementsByTagName("tr");
                    tdObj=trObj[0].getElementsByTagName("td");
                    dimNames.push(tdObj[1].innerHTML);
                }
                if(dimIds.length>1||dimIds.length==0){
                    alert("Please select only one dimension")
                }
                else {

                    //  alert("dimMemsArry--"+dimMemsArry)
                    dims="";
                    for(var i=0;i<dimMemsArry.length;i++){
                        dims+=dimMemsArry[i];
                        if(i!=dimMemsArry.length-1)
                            dims+="||";
                    }
                    // alert("dims--"+dims)
                    dimensionId=dimIds.toString();
                     if(mode=="Edit"){
                           if(editDimensionEleId!=dimensionId)
                               dims="";
                       }
                    $("#dimensionDiv").dialog('close')
                    $("#dimensionValuesDiv").dialog('open')
                    var frameObj=document.getElementById("dimensionValuesFrame");
                    frameObj.src="<%=request.getContextPath()%>/TableDisplay/pbParamFilterMembers.jsp?peviousDims="+dims+"&selectedParam="+dimIds.toString()+"&from=scorecard&measureId="+measIdsArry.toString()+"&measureName="+measNameArry.toString()+"";
                }

            }
            function goBackToDimensionValuesDiv(){
                $("#dimensionValuesDiv").dialog('close');
                $("#dimensionDiv").dialog('open')
            }
            function applyRulesForDims(mbrsArr,html)
            {
                //alert(mbrsArr)
                //var mbrsArr="";
                $("#dimensionValuesDiv").dialog('close');
                var modifiedVal="";
                dimMemsArry=mbrsArr;
                var measureOptions="";
                for(var i=0;i<mbrsArr.length;i++){
                    //modifiedVal=mbrsArr[i].replace(" ","_","gi").replace(";","","gi").replace(".","","gi").replace("(","","")
                    measureOptions+="<option id='Dim"+i+"' value='Dim"+i+"' >"+mbrsArr[i]+"</option>"
                    //  measureOptions+="<option id='Dim"+i+"' value='"+mbrsArr[i]+"' >"+mbrsArr[i]+"</option>"
                }
                var html="<option id='select'>select</option>";

                $("#applyRuleDiv").dialog('open');
                //alert(measureOptions)
                $("#selectedMeasure").html(html+measureOptions);
                $("#displayTypeTd").html("Dimensions")
                getMeasureValues();
                //if(mode!="Edit"){
                var complteUrl='<%=request.getContextPath()%>/scorecardDesign.do?scorecardParam=setScoreCardMembersForDimension&mode='+mode+'&isDimBased='+isDimBased+'&bussFolderId='+busFolderId+'&measId='+measIdsArry.toString()+'&measName='+measNameArry.toString()+'&dimId='+dimensionId+"&dimValues="+dimMemsArry+"&folderId="+busFolderId+"&userId=<%=userId%>";
                $.ajax({
                    url:complteUrl,
                    success: function(data){

                    }
                });
              //  }
                measureOptions="";
                for(var i=0;i<measIdsArry.length;i++){
                    measureOptions+="<option id="+measIdsArry[i]+" value="+measIdsArry[i]+" >"+measNameArry[i]+"</option>"
                }
                var html="";
                // var html="<option id='select'>select</option>";
                $("#selectedMeasureMulti").html(html+measureOptions);
                $("#displayMeasureTd").show();
                $("#dispMeasSelectTd").show();
                //$(".loading_image").hide();
               
            }

            function getCreatedScoreCards(){

                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=getCreatedScoreCards&userId=<%=userId%>",
                    success: function(data){
                        //  alert(data)
                        $("#measures").html($("#measures").html()+data);

                    }
                });
            }
            function shareScoreCard()
            {
                var scoreSelectObj=document.getElementsByName("ScoreSelect");
                var sharescoreids = new Array();
                for(var i=1;i<scoreSelectObj.length;i++)
                {
                    if(scoreSelectObj[i].checked)
                    {
                        sharescoreids.push(scoreSelectObj[i].value);
                    }
                    else{
                        scoreSelectObj[i].checked=false;
                    }
                }
                if(sharescoreids.length>0)
                    {
                        $.ajax({
                            url:"scorecardDesign.do?scorecardParam=getUserList&userId=<%=userId%>",
                            success: function(data){
                              $("#shareDragUl").html(data)
                               $("#shareScoreCardDiv").dialog('open')
                               initDrag('shareDropDiv');
                              }
                         });
                       
                    }else{
                          alert("please select atleast one scorecard")
                    }
            }
            function saveSharedScoreCards()
            {
                var scoreSelectObj=document.getElementsByName("ScoreSelect");
                var sharescoreids = new Array();
                for(var i=1;i<scoreSelectObj.length;i++)
                {
                    if(scoreSelectObj[i].checked)
                    {
                        sharescoreids.push(scoreSelectObj[i].value);
                    }
                    else{
                        scoreSelectObj[i].checked=false;
                    }
                }
                var ulObj=document.getElementById("shareDropUl");
                var liObjs=ulObj.getElementsByTagName("li");
                var userIds=new Array();
                for(var i=0;i<liObjs.length;i++)
                    {
                        userIds.push(liObjs[i].id.replace("drop",""))
                    }
                $.ajax({
                    url:"scorecardDesign.do?scorecardParam=shareScoreCards&scoreIds="+sharescoreids.toString()+"&shareUserIds="+userIds.toString(),
                       success: function(data){
                                 $("#shareScoreCardDiv").dialog('close')
                       }
                 });
            }
            function copyValuesToAll()
            {
                var isSaved=saveScorecardRules();
                if(isSaved==true)
                {
                    var conf=confirm("Are you sure , you want to copy same values to all the Measures/Dimensions")
                    if(conf==true)
                    {
                        var measureId="";
                        var dimValue="";
                        if(isDimBased)
                            {
                                 measureId=$("#selectedMeasureMulti").val();
                                 dimValue= $("#selectedMeasure option:selected").text();
                            }
                           
                        else
                            measureId=$("#selectedMeasure").val();
                       
                        $.ajax({
                            url:"scorecardDesign.do?scorecardParam=copySameValuesToAll&measId="+measureId+"&dimValue="+dimValue+"&isDimBased="+isDimBased+"&measures="+measIdsArry+"&dims="+dims,
                            success: function(data){

                              }
                         });
                    }
                }
            }
            function showTargetMeasures()
            {
                 isTargetBased=true;
                 grpColArray=new Array();
                 $.ajax({
                        url:"scorecardDesign.do?scorecardParam=getMeasures&type=target&userId=<%=userId%>&folderId="+busFolderId+"&ctxPath=<%=request.getContextPath()%>",
                        success: function(data){
                            if(data!=""){
                                $("#quicksearch").remove();

                                $("#measures").html(data);

                                initClasses();
                                document.getElementById("quicksearch").style.height='10px';
                                 $("#createScoreDiv").dialog('open')
                                 $("#ui-dialog-title-createScoreDiv").html('Select Target Measures')
                                 $("#masterTable").hide();
                                 $("#doneTarget").show();
                                 $("#nextMaster").hide();
                                 $("#sortable").html("");
                            }

                        }
                    });
                
               
            }
            function saveTargetCols()
            {
              var targetEleName="";
              var targetEleId="";
              var measureId="";
              var dimValue="";
              if(isDimBased)
                {
                   measureId=$("#selectedMeasureMulti").val();
                   dimValue= $("#selectedMeasure option:selected").text();
               }
                else
                   measureId=$("#selectedMeasure").val();

              targetEleId=$("#sortable >li").attr("id").replace("GrpCol","");
              $("#sortable >li > table > tbody > tr " ).each(function() {
                  targetEleName = $(this).find("td").eq(1).html();
              })
              $("#targetMeasText").val(targetEleName);
              $("#targetMeasText").attr('elementId',targetEleId);
              $.ajax({
                        url:"scorecardDesign.do?scorecardParam=saveTargetMeasure&isDimBased="+isDimBased+"&measId="+measureId+"&targetMeasId="+targetEleId+"&targetMeasName="+targetEleName+"&dimValue="+dimValue,
                        success: function(data){
                          $("#createScoreDiv").dialog('close')
                     }
              });
            }
            function changeTargetMeasureType()
            {
                var type=$("#targetMeasureType").val();
                if(type=="TargetMeasure")
                 {
                   $("#targetTr").show();
                   $("#targetTrType").show();
                   $("#targetTd1").show();
                   $("#targetTd2").show();
                 }
                 else if(type=="TargetValue")
                 {
                   $("#targetTr").show();
                   $("#targetTrType").show();
                   $("#targetTd1").show();
                   $("#targetTd2").hide();
                 }
                 else{
                   $("#targetTr").hide();
                 }
            }
            
        </script>
    </body>
</html>


 