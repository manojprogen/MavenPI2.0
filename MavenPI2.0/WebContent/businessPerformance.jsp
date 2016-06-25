<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,prg.db.PbDb,prg.db.PbReturnObject"%>
<%-- 
    Document   : businessPerformance
    Created on : 10 Dec, 2012, 10:47:32 AM
    Author     : progen
--%>

<!DOCTYPE html>
<%//for clearing cache
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

                //added by Dinanath
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");

    String themeColor = "blue";
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
    String contextPath = request.getContextPath();
%>

<html>
    <head>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/whatIfScenario.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/JS/stickyNote.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportviewer/ReportViewer.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/jQuery/jquery.mb.containerPlus.2.5.1/css/mbContainer.css" title="style"  media="screen"/>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery.mb.containerPlus.2.5.1/inc/jquery.metadata.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery.mb.containerPlus.2.5.1/inc/mbContainer.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.slider.js"></script>
        <script type="text/javascript" src="<%= contextPath%>/dragAndDropTable.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
    </head>
    

    
    <style type="text/css">
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
        *{font:11px verdana}
    </style>

    <%
        try {
            PbDb pbdb = new PbDb();
            String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
            AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID"));
            AvailableFiolers += " union ";
            AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
            AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID"));
            AvailableFiolers += "and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + String.valueOf(request.getSession(false).getAttribute("USERID")) + ")))";
            PbReturnObject folderpbro = pbdb.execSelectSQL(AvailableFiolers);
    %>
    <body>
        <ul id="mymenu" class="contextMenu">
            <li class="select"><a href="#select">Enable Target</a></li>
        </ul>
        <ul id="defineDayLevelTargets" class="contextMenu">
            <li class="select"><a href="#defineDayLevel">Day Level Allocation </a></li>
            <li class="select"><a href="#weekLevelAllocation">Week Level Allocation </a></li>
        </ul>
        <ul id="targetMeasId" class="contextMenu">
           
            <li class="select"><a href="#Define Period Target">Define Period Type</a></li>
            <li class="select"><a href="#Add/Edit Period Duration">Add/Edit Target Duration</a></li>
            <li class="select"><a href="#Target Parameters" >Target Parameters</a></li>
            <li class="select"><a href="#least timeperiod" >Least Time Period</a></li>
            <li class="select"><a href="#Add SimpleMeasures">Add Supportive Measures</a></li>
            <li class="select"><a href="#Additional Info" >Additional Info</a></li>
            <li class="select"><a href="#Upload Excel" >Upload Excel</a></li>
            <li class="select"><a href="#Download Excel" >Download Excel</a></li>
        </ul>
        <ul id="definedTargets" class="contextMenu">
            <li class="select"><a href="#Add Parameters">Add Parameters</a></li>
        </ul>
        <ul id="defineTargetsParams" class="contextMenu">
<!--            <li class="select"><a href="#download">Download</a></li>-->
<!--            <li class="select"><a href="#download dayLevelData">Download Data</a></li>-->
            <li class="select"><a href="#download dayLevelData">Download DayLevelData</a></li>
            <li class="select"><a href="#download weekLevelData">Download WeekLevelData</a></li>
<!--            <li class="select"><a href="#upload dayLevelData">Upload DayLevelData</a></li>-->
            <li class="select"><a href="#upload weekLevelData">Upload WeekLevelData</a></li>
            <!--            <li class="select"><a href="#upload">Upload</a></li>-->
        </ul>
        <form name="targetForm" method="post">
            <table style="width:100%">  <tr>
                   <td align="left" width='10%'><h5><%=TranslaterHelper.getTranslatedInLocale("business_role", cle)%></h5></td>
                    <td align="left">
                        <h5><select id="bsrSelect" name="BusinessRoles">
                                <option value=""> ---Select---</option>
                                <%
                                    for (int i = 0; i < folderpbro.getRowCount(); i++) {%>
                                <option value="<%=folderpbro.getFieldValueString(i, 0)%>">
                                    <%=folderpbro.getFieldValueString(i, 1)%></option>
                                    <%}
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }%>
                            </select>&nbsp;<input class="navtitle-hover" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("go", cle)%>" onclick="getBussRole()"></h5>
                    </td>
                </tr>
            </table>
            <div id="dataDivId" align="left"> </div>
            <div id="targetDivId" align="left">
                <table  border="1" style="width: 50%; height: 250px;">
                    <tr>
                        <td class="draggedTable1" width="50%" valign="top">
                            <div class="ui-state-default draggedDivs ui-corner-all" style="height: 20px;">
                                <font size="2" style="font-weight: bold;"><%=TranslaterHelper.getTranslatedInLocale("targets", cle)%></font>
                            </div>
                            <div id="masertDivTestId" class="masterDiv" style="height: 200px; overflow-y: auto;" >
                                <ul >
                                    <span >
                                        <li class='closed' id="2_tagetTable1">
                                            <img src='<%=contextPath%>/icons pinvoke/table.png' />
                                            <span style='font-family:verdana;' id='2~tagetTables ' class="2_Targets" title="targetTable"><%=TranslaterHelper.getTranslatedInLocale("TARGET_MEASURES", cle)%></span>
                                            <ul id="2_TagetTable">
                                            </ul>
                                        </li>
                                    </span>  
                                </ul>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
        <div id="enableTargetTimeId" class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;" title="Define Period type">
            <br>
            <table  width="100%" id="timeBasedTable">
                <tr>
                    <td class="myhead"> Period Type </td>
                    <td>
                        <select id="level" onChange="changeRangeLevel(this.id)" style="width:150px;">
                            <option value="">--SELECT--</option>
                            <option value="Month">Month</option>
                            <option value="Quarter">Quarter</option>
                            <option value="Year" >Year</option>
                        </select>  
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%">
                <tr>
                    <td align="center">
                        <input class="navtitle-hover" type="button" onclick="displayPeriodDetails()" value=" Done ">
                    </td>
                </tr>
            </table>
        </div>
        <!--                                            <div id="defineTargetId"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;" title="Add/Edit Target Duration"></div>-->
        <div id="defineTargetId"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;" title="Add/Edit Target Duration">
            <br>
            <table  width="100%">
                <tr>
                    <td class="myhead"> Select Year </td>
                    <td>
                        <select id="selectYear" style="width:150px;">
                            <option value="1">Last 1 Year</option>
                            <option value="2">Last 2 Years</option>
                            <option value="3">Last 3 Years</option>
                            <option value="4">Last 4 Years</option>
                            <option value="5">Last 5 Years</option>
                        </select>  
                    </td>
                </tr>
                <!--                <tr>
                                    <td class="myhead"> Select Rolling Type </td>
                                    <td>
                                        <select id="rollingTypeId11" style="width:150px;">
                                            <option value="month">Rolling Month</option>
                                            <option value="year">Rolling Year</option>
                                        </select>  
                                    </td>
                                </tr>-->
            </table>
            <br>
            <table width="100%" id="displayYearId">

            </table>
        </div>      
        <div id="downLoadExcelId" style="display:none;" >
            <IFRAME NAME="dFrame" id="oneFrame" STYLE="display:none;width:0px;height:0px"  frameborder="0"></IFRAME>
        </div>
        <div id="uploadExcel" style="display:none;" >
            <IFRAME NAME="iframeName" id="uploadSheet" STYLE="width:100%;height:100%"  frameborder="0"></IFRAME>
        </div>
        <div id="targetViewsDivId" style="display: none"></div>
        <div id="descreteMonthsDivID" style="display: none"></div>
        <div id="downLoadTargetRange"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;" title="Select Target Period Range">

            <table id="targetExceldownloadId"  width="100%">
            </table>
            <table id="targetDownloadId" width="100%" >
            </table>
        </div>    
        <div id="uploadLoadTargetRange"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;" title="Target Period ExcelUpload">   </div>    

        <div id="defineRollingTypeId"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;" title="Target Excel">
            <br>
            <table  width="100%">
                <tr>
                    <td class="myhead"> Select Rolling Months </td>
                    <td>
                        <select id="rollingTypeId" style="width:150px;">
                            <option value="1">One Month</option>
                            <option value="2">Two Month</option>
                            <option value="3">Three Month</option>
                            <option value="4">Four Month</option>
                            <option value="5">Five Month</option>
                            <option value="6">Six Month</option>
                        </select>  
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" id="displayRolingTypeId">
            </table>
        </div>    
        <div id="addtionalInfo"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;" title="Additional Information">
            <br>
            <table  width="100%">
                <tr>
                    <td class="myhead"> YOY </td>
                    <td width="50%">
                        <select id="rolingYearId" style="width:150px;" onchange="rolingYearType()">
                            <option value="no">No</option>
                            <option value="yes">Yes</option>
                        </select>  
                    </td>
                </tr>
                <tr style="display:none;" id="displayRollingyearsId">
                    <td class="myhead"> Rolling Years </td>
                    <td width="50%">
                        <select id="noofRollingyearsId" style="width:150px;">
                            <option value="1">One Year</option>
                            <option value="2">Two Years</option>
                            <option value="3">Three Years</option>
                            <option value="4">Four Years</option>
                            <option value="5">Five Years</option>
                        </select>  
                    </td>
                </tr>
                <tr>
                    <td class="myhead"> MOM </td>
                    <td width="50%">
                        <select id="typerollingMonthsId" style="width:150px;" onchange="selectRollingMonthsTypes()">
                            <option value="no">No</option>
<!--                            <option value="rolingMonths">Rolling Months</option>-->
                            <option value="descrMonths">Discrete Months </option>
                        </select>  
                    </td>
                </tr>
<!--                <tr style="display:none" id="rolingMothTypeId">
                    <td class="myhead">Rolling Months</td>
                    <td width="50%">
                        <select id="rollingMonthsTypesId" style="width:150px;" >
                            <option value="1">One Month</option>
                            <option value="2">Two Months</option>
                            <option value="3">Three Months </option>
                            <option value="4">Four Months </option>
                            <option value="5">Five Months </option>
                            <option value="6">Six Months </option>
                            <option value="7">Seven Months </option>
                            <option value="8">Eight Months </option>
                            <option value="9">Nine Months </option>
                            <option value="10">Ten Months </option>
                            <option value="11">Leven Months </option>
                            <option value="12">Twelve Months </option>
                        </select>  
                    </td>
                </tr>-->
                <tr style="display:none" id="descMonthtypeId">
                    <td class="myhead">Discrete Months </td>
                    <td width="50%">
                        <select id="desceteMonthsTypesId" style="width:150px;" onchange="selectDescreteMonths()">
                            <option value="1">One Month</option>
                            <option value="2">Two Months</option>
                            <option value="3">Three Months </option>
                            <option value="4">Four Months </option>
                            <option value="5">Five Months </option>
                            <option value="6">Six Months </option>
<!--                            <option value="7">Seven Months </option>
                            <option value="8">Eight Months </option>
                            <option value="9">Nine Months </option>
                            <option value="10">Ten Months </option>
                            <option value="11">Leven Months </option>
                            <option value="12">Twelve Months </option>-->
                        </select>  
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" id="rollingTypebuttonId">
                <tr>
                    <td align="center">
                        <input type="button"  name="" value="Done" onclick="additionalInfo()" >
                    </td>
                </tr>
            </table>
        </div> 
        <div id="defineSupportiveMeasId"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;">
            <br>
            <table width="100%" id="suppMeasureIds">

            </table>
            <br>
            <table width="100%" id="suppMeasureIdValue1">

            </table>
        </div>  
        <div id="dayLevelAllocationId"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;">
           
            <br>
            <table width="100%" id="daylevelAlocationId">
                <tr><td align='left'><input type='radio' id='manualAlloId' name='daylevel' value='' checked>Manual Allocation</td></tr>
                <tr><td align='left'><input type='radio' id='autoAlloId' name='daylevel' value='' >Auto Allocation</td></tr>
                <tr><td align='left'><input type='radio' id='autoAlloPerId' name='daylevel' value='' >Auto Allocation % </td></tr>
            </table>
            <br>
             <table width="100%" id="dayLevelDoneButtonId">
                
            </table>
        </div>  
        <div id="weekLevelAllocationId"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;">
           
            <br>
            <form id="weekLevelFormDataId" name="weekLevelFormData" action="" method="">
            <table width="100%" id="weeklevelAlocationId">
            </table>
            </form>
            <br>
             <table width="100%" id="weekLevelDoneButtonId">
                
            </table>
        </div>  
        <div id="dayLevelDisplayDataId"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;">
            <br>
            <table width="100%" id="absoulteOrPertype" style="display:none;">
            </table>
            <form id="dayLevelFormDataId" name="dayLevelFormData" action="" method="">
            <table width="100%" id="dayLevelDataId">
            </table>
            </form>
            <br>
             <table width="100%" id="dayLevelDoneId">
                
            </table>
        </div> 
        <div id="dayLevelEnableDisableId"  class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;">
           
            <br>
            <table width="100%" id="daylevelAlocationId">

            </table>
        </div> 
        <script type="text/javascript">
        $(document).ready(function(){
                  
            if ($.browser.msie == true){
                $("#fromMonthId").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $("#toMonthId").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker2').datepicker({
                    changeMonth: true,
                    changeYear: true
                    //showButtonPanel: true

                });

                $('#datepicker3').datepicker({
                    changeMonth: true,
                    changeYear: true
                    //showButtonPanel: true

                });
                $('#datepicker1').datepicker({
                    showOn: "button",
                    buttonImage: "icons pinvoke/calendar-select.png",
                    buttonImageOnly: true

                });
                $("#defineTargetId").dialog({
                    autoOpen: false,
                    height: 150,
                    width: 300,
                    position: 'absolute',
                    modal: true
                });
                $("#enableTargetTimeId").dialog({
                    autoOpen: false,
                    height: 250,
                    width: 400,
                    position: 'absolute',
                    modal: true
                });
                $("#targetViewsDivId").dialog({
                    autoOpen: false,
                    height: 520,
                    width: 520,
                    position: 'absolute',
                    modal: true,
                    title:'Target Parameters'
                });
                $("#uploadExcel").dialog({
                    autoOpen: false,
                    height: 260,
                    width: 450,
                    position: 'absolute',
                    modal: true,
                    title:'Target ExcelUpload'
                });
                $("#downLoadTargetRange").dialog({
                    autoOpen: false,
                    height: 220,
                    width: 450,
                    position: 'absolute',
                    modal: true
                });
                $("#uploadLoadTargetRange").dialog({
                    autoOpen: false,
                    height: 260,
                    width: 450,
                    position: 'absolute',
                    modal: true
                });
                $("#defineRollingTypeId").dialog({
                    autoOpen: false,
                    height: 260,
                    width: 450,
                    position: 'absolute',
                    modal: true
                });
                $("#addtionalInfo").dialog({
                    autoOpen: false,
                    height: 220,
                    width: 400,
                    position: 'absolute',
                    modal: true
                });
                $("#descreteMonthsDivID").dialog({
                    autoOpen: false,
                    height: 520,
                    width: 520,
                    position: 'absolute',
                    modal: true,
                    title:'Target DesceteMonths'
                });
                $("#defineSupportiveMeasId").dialog({
                    autoOpen: false,
                    height: 110,
                    width: 320,
                    position: 'absolute',
                    modal: true,
                    title:'Target Supportive Measures'
                });
//                 $("#dayLevelAllocationId").dialog({
//                    autoOpen: false,
//                    height: 110,
//                    width: 320,
//                    position: 'absolute',
//                    modal: true,
//                    title:'Day Level Allocation'
//                });
                $("#dayLevelDisplayDataId").dialog({
                    autoOpen: false,
                    height: 600,
                    width: 670,
                    position: 'absolute',
                    modal: true
                });
                 $("#dayLevelEnableDisableId").dialog({
                    autoOpen: false,
                    height: 600,
                    width: 598,
                    position: 'absolute',
                    modal: true,
                    title:'Day Level Data Allocation'
                });
                $("#weekLevelAllocationId").dialog({
                    autoOpen: false,
                    height: 300,
                    width: 550,
                    position: 'absolute',
                    modal: true
                 });
            }
            else{
                $("#fromMonthId").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $("#toMonthId").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker2').datepicker({
                    changeMonth: true,
                    changeYear: true
                    //showButtonPanel: true

                });

                $('#datepicker3').datepicker({
                    changeMonth: true,
                    changeYear: true
                    //showButtonPanel: true

                });
                $('#datepicker1').datepicker({
                    showOn: "button",
                    buttonImage: "icons pinvoke/calendar-select.png",
                    buttonImageOnly: true

                });
                $("#defineTargetId").dialog({
                    autoOpen: false,
                    height: 150,
                    width: 300,
                    position: 'absolute',
                    modal: true
                });
                $("#enableTargetTimeId").dialog({
                    autoOpen: false,
                    height: 250,
                    width: 400,
                    position: 'absolute',
                    modal: true
                });
                $("#targetViewsDivId").dialog({
                    autoOpen: false,
                    height: 520,
                    width: 520,
                    position: 'absolute',
                    modal: true,
                    title:'Target Parameters'
                });
                $("#uploadExcel").dialog({
                    autoOpen: false,
                    height: 260,
                    width: 450,
                    position: 'absolute',
                    modal: true,
                    title:'Target ExcelUpload'
                });
                $("#downLoadTargetRange").dialog({
                    autoOpen: false,
                    height: 220,
                    width: 450,
                    position: 'absolute',
                    modal: true
                });
                $("#uploadLoadTargetRange").dialog({
                    autoOpen: false,
                    height: 260,
                    width: 450,
                    position: 'absolute',
                    modal: true
                });
                $("#defineRollingTypeId").dialog({
                    autoOpen: false,
                    height: 220,
                    width: 400,
                    position: 'absolute',
                    modal: true
                });
                $("#addtionalInfo").dialog({
                    autoOpen: false,
                    height: 220,
                    width: 400,
                    position: 'absolute',
                    modal: true
                });
                $("#descreteMonthsDivID").dialog({
                    autoOpen: false,
                    height: 520,
                    width: 520,
                    position: 'absolute',
                    modal: true,
                    title:'Target DesceteMonths'
                });
                $("#defineSupportiveMeasId").dialog({
                    autoOpen: false,
                    height: 110,
                    width: 320,
                    position: 'absolute',
                    modal: true,
                    title:'Target Supportive Measures'
                });
//                 $("#dayLevelAllocationId").dialog({
//                    autoOpen: false,
//                    height: 110,
//                    width: 320,
//                    position: 'absolute',
//                    modal: true,
//                    title:'Day Level Allocation'
//                });
                $("#dayLevelDisplayDataId").dialog({
                    autoOpen: false,
                    height: 600,
                    width: 670,
                    position: 'absolute',
                    modal: true
                });
                 $("#dayLevelEnableDisableId").dialog({
                    autoOpen: false,
                    height: 600,
                    width: 598,
                    position: 'absolute',
                    modal: true,
                    title:'Day Level Data Allocation'
                });
               $("#weekLevelAllocationId").dialog({
                    autoOpen: false,
                    height:300,
                    width: 550,
                    position: 'absolute',
                    modal: true
                 }); 
            }
        });
          
    </script>
    <script type="text/javascript">
       
        var eleID = '';
        var elementID = '';
        var elemtName = '';
        var bsrSelectedID = '';
        var cust_name;
        var cust_st_date;
        var cust_end_date;
        var cust_year_array =  new Array();
        var custom_year =  new Array();
        var cust_month_array =  new Array();
        var cust_quarter_array = new Array();
        var startValue = '';
        var endValue = '';
        function getBussRole(){
            bsrSelectedID= $("#bsrSelect").val();
            if(bsrSelectedID!=''){
                $.post(
                'userLayerAction.do?userParam=getBusinessRolesMethod&bsrSelectedID='+bsrSelectedID+"&fromBPM=true"+"&contextPath="+"<%=contextPath%>",
                function(data){
                    $("#dataDivId").html(data);
                
                    $("#kpiTree").treeview({
                        animated: "normal",
                        unique:true
                    });
                    $('ul#kpiTree li').quicksearch({
                        position: 'before',
                        attached: 'ul#kpiTree',
                        loaderText: '',
                        delay: 100
                    })
                
                    $(".1_span").contextMenu({menu:"mymenu",rightButton: true},
                    function(action, el, pos) {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        var name=names[1];
                        var html = '';
                        if($('ul #2_TagetTable li:contains("'+name+'")').length=='0') {
                            html+="<li class='closed' >";
                            //html+="<img src='<%=contextPath%>/icons pinvoke/document-attribute-f.png'></img>";
                            html+="<span class=\""+2+"_span\" id='" + eleID +"~"+ name +" '  title='TargetMeasure' style='font-family:verdana;'>" + name + "</span>";
                            html+="</li>";
                            $("#2_TagetTable").append(html);
                        }
                        else{
                            alert(name+" Already Exist in Target Measures")
                        }
                      
                        $(".2_span").contextMenu({menu:"targetMeasId",rightButton: true},
                        function(action, el, pos) {
                            contextMenuWork(action, el, pos);
                        });
                        $(".4_span").contextMenu({menu:"defineTargetsParams",rightButton: true},
                        function(action, el, pos) {
                            contextMenuWork1(action, el, pos);
                        });
                        $(".3_span").contextMenu({menu:"defineDayLevelTargets",rightButton: true},
                        function(action, el, pos) {
                            contextMenuWork2(action, el, pos);
                        });
                    });
                });
                $.ajax({
                    url:'userLayerAction.do?userParam=getTargetMeasuresDetails&bussRoleId='+bsrSelectedID,
                    success: function(data) {
                        if(data!='No Measures'){
                            $("#masertDivTestId").html(data);
                              
                            $(".2_span").contextMenu({menu:"targetMeasId",rightButton: true},
                            function(action, el, pos) {
                                contextMenuWork(action, el, pos);
                            });
                            $(".4_span").contextMenu({menu:"defineTargetsParams",rightButton: true},
                            function(action, el, pos) {
                                contextMenuWork1(action, el, pos);
                            });
                             $(".3_span").contextMenu({menu:"defineDayLevelTargets",rightButton: true},
                               function(action, el, pos) {
                               contextMenuWork2(action, el, pos);
                             });
                            
                            $("#kpiTree1").treeview({
                                animated: "normal",
                                unique:true
                            });
                        }
                        else{
                            $("#2_TagetTable").html("");       
                        }
                    }
                });
            }
            else{
                alert("Please Select Business Role!");
            }
        }
        function contextMenuWork(action, el, pos) {
        
            switch (action) {
                case "Add SimpleMeasures":
                    {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        var testvalue=names[1];
                        elemtName = testvalue.replace("_"," ","gi")
                        var html = '';
                        supportiveMeasure(eleID,elemtName,bsrSelectedID)
                        break;
                    }
                case "Define Period Target":
                    {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        elemtName=names[1];
                        $.ajax({
                            url:'userLayerAction.do?userParam=testTargetsExistorNot&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName,
                            success:function(data){
                                if(data!='No PeriodDetails'){
                                    var evalJson = eval('('+data+')');
                                    var periodtype = '';
                                    periodtype = evalJson.TargetPeriodDetails[0];                              
                                    changeRangeLevel(periodtype);                                  
                                    $("#level").val(periodtype)
                                    startValue = evalJson.TargetPeriodDetails[1];
                                    endValue = evalJson.TargetPeriodDetails[2];
                                    $("#enableTargetTimeId").dialog('open')
                                }
                                else{
                                    $('#level option[value=""]').attr('selected','selected');
                                    $("#timeBasedTable").find("tr:gt(0)").remove();
                                    $("#enableTargetTimeId").dialog('open')
                                }
                            }
                        });
                        break;
                    }
                case "Add/Edit Period Duration":
                    {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        var testvalue=names[1];
                        elemtName = testvalue.replace("_"," ","gi")
                        var html = '';
                        $.ajax({
                            url:'userLayerAction.do?userParam=testTargetsExistorNot&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName,
                            success:function(data){
                                if(data!='No PeriodDetails'){
                                    var evalJson = eval('('+data+')');
                                    var cmpr = evalJson.TargetPeriodDetails[3];
                                    //                                    var rolling = evalJson.TargetPeriodDetails[4];
                                    if(cmpr!=''){
                                        $('#selectYear option[value='+cmpr+']').attr('selected','selected');
                                        displayYearsforExecelUpload(eleID,elemtName,bsrSelectedID,cmpr);
                                        //displayYearsforExecelUpload(eleID,elemtName,bsrSelectedID,cmpr,rolling);
                                    }
                                    else{
                                        $('#selectYear option[value=1]').attr('selected','selected'); 
                                        var selyear = "1";
                                        var roleType = "1";
                                        
                                        //                                        html+="<tr><td align='center'><input class='navtitle-hover' type='button' onclick=\"displayYearsforExecelUpload("+eleID+",'"+elemtName+"',"+bsrSelectedID+","+selyear+","+roleType+")\" value=' Done '></td></tr>";                
                                        html+="<tr><td align='center'><input class='navtitle-hover' type='button' onclick=\"displayYearsforExecelUpload("+eleID+",'"+elemtName+"',"+bsrSelectedID+","+selyear+")\" value=' Done '></td></tr>";                
                                        $("#displayYearId").html(html);
                                        $("#defineTargetId").dialog('open')
                                    }
                                    
                                }
                                else{
                                    alert("Please Define Target Period Type!")
                                }
                            }
                        });
                        break;
                    }
                
                case "Target Parameters":
                    {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        var testvalue=names[1];
                        elemtName = testvalue.replace("_"," ","gi")
                        var html = '';
                        $.ajax({
                            url:'userLayerAction.do?userParam=testTargetsExistorNot&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName,
                            success:function(data){
                                if(data!="No PeriodDetails"){
                                    $.ajax({
                                        url:'userLayerAction.do?userParam=getallDimViewBys&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName,
                                        success:function(data){
                                            var jsonVar=eval('('+data+')');
                                            var html = '';
                                            html+="<br><center><table align='center'><tr><td><input type='button' name='Done' value='Done' onclick=\"getDimviewBys("+bsrSelectedID+","+eleID+",'"+elemtName+"')\"></td></tr></table></center>";
                                            $("#targetViewsDivId").html(jsonVar.htmlStr+html);
                      
                                            $("#myList3").treeview({
                                                animated:"slow",
                                                persist: "cookie"
                                            });

                                            $('ul#myList3 li').quicksearch({
                                                position: 'before',
                                                attached: 'ul#myList3',
                                                loaderText: '',
                                                delay: 100
                                            })
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
                                            grpColArray=jsonVar.memberValues;
                                            $(".sortable").sortable();
                                        }
                                    });
                                    $("#targetViewsDivId").dialog('open');
                                }
                                else{
                                    alert("Please Define Target Period Type and Duration!")
                                }
                            }
                        });  
                        break;
                    }
                case "Upload Excel":
                    {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        var testvalue=names[1];
                        elemtName = testvalue.replace("_"," ","gi")
                        var html = '';
                        targetExecelUpload(eleID,elemtName,bsrSelectedID)
                        break;
                    }
                    case "least timeperiod":
                    {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        var testvalue=names[1];
                        elemtName = testvalue.replace("_"," ","gi")
                        var html = '';
                        leastTimePeriod(eleID,elemtName,bsrSelectedID)
                        break;
                    }
                case "Additional Info":
                    {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        var testvalue=names[1];
                        elemtName = testvalue.replace("_"," ","gi")
                       
                         $.ajax({
                            url:'userLayerAction.do?userParam=testTargetsExistorNot&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName,
                            success:function(data){
                                if(data!='No PeriodDetails'){
                                  $("#addtionalInfo").dialog("open");
                                }
                                else{
                                    alert("Please Define Target Period and Duration Type!")
                                }
                            }
                        });
                        break;
                    }
                case "Download Excel":
                    {
                        var val=el.attr("id");
                        var names=val.split("~");
                        eleID=names[0];
                        var testvalue=names[1];
                        elemtName = testvalue.replace("_"," ","gi")
                        var html = '';
                        $.ajax({
                            url:'userLayerAction.do?userParam=testTargetsExistorNot&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName,
                            success:function(data){
                                if(data!='No PeriodDetails'){
                                    var evalJson = eval('('+data+')');
                                    var periodtype = '';
                                    periodtype = evalJson.TargetPeriodDetails[0];  
                                    var startValue = evalJson.TargetPeriodDetails[1];
                                    var endValue = evalJson.TargetPeriodDetails[2];
                                    $.ajax({
                                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetails&elementId='+eleID+'&levelType='+periodtype,
                                        success: function(data){
                                            var details = eval('('+data+')');
                                            var cust_name = details.cust_name;
                                            var cust_st_date = details.cust_st_date;
                                            var cust_end_date =  details.cust_end_date;
                                            var rangeHtml = '';
                                            var doneFunction = '';
                                            rangeHtml +="<tr> <td class='myhead' align='right'> Select Target Period  </td></tr><br>"
                                            rangeHtml += "<tr><td width='40%' class='myhead'><font>From</td><td width='60%'>";
                                            rangeHtml =  rangeHtml+"<select name='selectQuarter1' id='startDateId' name='startDate'  style='width:150px;'>";
                                            for(var i=0;i<cust_st_date.length;i++){
                                                if(startValue!='' && cust_st_date[i]==startValue)
                                                    rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+" selected>"+cust_name[i]+"</option>";
                                                else
                                                    rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+">"+cust_name[i]+"</option>";
                                            }
                                            rangeHtml = rangeHtml+"</select></td>"
                                            rangeHtml = rangeHtml+"</tr>"
                                            rangeHtml += "<tr><td width='40%' class='myhead'><font>To </td><td width='60%'>";
                                            rangeHtml = rangeHtml+"<select id='endDateId' name='endDate' style='width:150px;'>";
                                            for(var i=0;i<cust_end_date.length;i++){
                                                if(endValue!='' && cust_end_date[i]==endValue)
                                                    rangeHtml = rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_name[i]+"<option>";
                                                else
                                                    rangeHtml = rangeHtml+"<option value="+cust_end_date[i]+" >"+cust_name[i]+"<option>";
                                            }
                                            rangeHtml = rangeHtml+"</select></td></tr>"
                                            doneFunction +="<tr><td align='center'><input type='button' name='' value='Done' onclick=\"downloadTargetExcelData("+eleID+","+bsrSelectedID+",'"+startValue.replace("_"," ","gi")+"','"+endValue.replace("_"," ","gi")+"','"+periodtype.replace("_"," ","gi")+"')\"></td></tr>";
                                     
                                            $("#targetExceldownloadId").html(rangeHtml);
                                            $("#targetDownloadId").html(doneFunction);
                                        }
                                    });
                                    $("#downLoadTargetRange").dialog('open');
                                }
                                else{
                                    alert("Define Target Period Type and Duration!")
                                }
                            }
                        });
                    
                        break;
                    }
                }
            }
            function contextMenuWork1(action, el, pos) {
                switch (action) {
                    case "download":
                        {
                            var val=el.attr("id");
                            var names=val.split("*");
                            eleID=names[0];
                            var testvalue=names[1];
                            var monthVal=names[2];
                            var monthName=names[3];
                            var regionName = testvalue.replace("_"," ","gi")
                            var html = '';
                            html+="<tr><td align='center'><input type='button' name='' value='Next' onclick=\"downLoadRollingTypeData("+eleID+","+bsrSelectedID+",'"+regionName+"','"+monthVal+"','"+monthName+"')\"></td></tr>";
                            $("#displayRolingTypeId").html(html);
                            $("#defineRollingTypeId").dialog('open');
                            break;
                        }
                    case "upload dayLevelData":
                        {
                            
                            var val=el.attr("id");
                            var names=val.split("*");
                            eleID=names[0];
                            var leveltypeData = 'dayLevelAlloction'
                            var viewByname=names[1];
                            var eleName=names[2];
                            var monthName=names[3];
                            downLoadandUploadDayLevelData(eleID,viewByname,eleName,monthName,leveltypeData);
                            break;
                        }   
                       case "upload weekLevelData":
                        {
                            
                            var val=el.attr("id");
                            var names=val.split("*");
                            eleID=names[0];
                            var leveltypeData = 'weekLevelAlloction'
                            var viewByname=names[1];
                            var eleName=names[2];
                            var monthName=names[3];
                            downLoadandUploadDayLevelData(eleID,viewByname,eleName,monthName,leveltypeData);
                            break;
                        }   
                        case "download dayLevelData":
                        {
                            var val=el.attr("id");
                            var names=val.split("*");
                            eleID=names[0];
                            var leveltypeData = 'dayLevelAlloction'
                            var viewByname=names[1];
                            var eleName=names[2];
                            var monthName=names[3];
                            downLoadDayLevelData(eleID,viewByname,eleName,monthName,leveltypeData);
                            break;
                        }    
                        case "download weekLevelData":
                        {
                            var val=el.attr("id");
                            var names=val.split("*");
                            eleID=names[0];
                            var leveltypeData = 'weekLevelAlloction'
                            var viewByname=names[1];
                            var eleName=names[2];
                            var monthName=names[3];
                            downLoadDayLevelData(eleID,viewByname,eleName,monthName,leveltypeData);
                            break;
                        }  
                }
            }
            var nooWeeks = ''
            function contextMenuWork2(action, el, pos) {
                switch (action) {
                    case "defineDayLevel":
                        {
                            var val=el.attr("id");
                            var names=val.split("@");
                            eleID=names[0];
                            var monthName=names[1];
                            var regionName = monthName.replace("_"," ","gi")
                            var html1 = '';
                            html1+="<tr> <td align='center'><input type='button'  name='' value='Done' onclick=\"displayDayLevelData("+eleID+",'"+regionName+"')\" ></td><tr>"
                            $("#dayLevelDoneButtonId").html(html1);
                             $("#dayLevelAllocationId").dialog({
                                autoOpen: false,
                                height: 200,
                                width: 320,
                                position: 'absolute',
                                modal: true
                               });
                             $("#dayLevelAllocationId").dialog('option','title',monthName+' Level Allocation');
                             $("#manualAlloId").attr('checked','checked')
                            $("#dayLevelAllocationId").dialog('open');
                            break;
                        }
                    case "weekLevelAllocation":
                        {
                            var val=el.attr("id");
                            var names=val.split("@");
                            eleID=names[0];
                            var monthName=names[1];
                            var regionName = monthName.replace("_"," ","gi")
                           
                               $.ajax({
                                   url:'userLayerAction.do?userParam=getWeekLevelData&monthName='+regionName+"&elementID="+eleID,
                                   success:function(data){
                                      var datavalue = data;
                                      var name=datavalue.split(",");
                                      var targetVal=name[0];
                                      nooWeeks = name[1];
                                      var weeValue=parseFloat(targetVal/nooWeeks);
                                      weeValue=Math.round(weeValue);
                                    var percentVal1 = parseFloat((((weeValue*100)/targetVal)*100)/100);
                                    var percentVal = Math.round(percentVal1);
                                    
                                    var weeKValue = weeValue*nooWeeks;
                                    var weekdiffernVal =weeValue+parseInt(targetVal-parseInt(weeKValue))
                                    
                                    var weeklevelVal = percentVal*nooWeeks;
                                    var weekPerDiff = percentVal+parseInt(100-parseInt(weeklevelVal))
                                    
                                      var html1 = '';
                                      var html = '';
                                      html+="<tr><td width='25%'><h5>Week</h5></td><td width='25%'><h5>Target</h5></td><td width='25%'><h5>Week %</h5></td></tr>";
                                     for(var i=1;i<=nooWeeks;i++){
                                         if(i==nooWeeks){
                                          html+="<tr><td width='25%'><input type='text' name='nameofWeek' value='W"+i+"' readonly></td><td width='25%'><input type='text' id='weekTargetVal"+i+"' name='targetValues' value="+weekdiffernVal+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotTarget(this.id)\"></td><td width='25%'><input type='text' id='weekPerVal"+i+"' name='targetPerValue' value="+weekPerDiff+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotalPer(this.id)\"></td></tr>";
                                         }
                                         else{
                                            html+="<tr><td width='25%'><input type='text' name='nameofWeek' value='W"+i+"' readonly></td><td width='25%'><input type='text' id='weekTargetVal"+i+"' name='targetValues' value="+weeValue+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotTarget(this.id)\"></td><td width='25%'><input type='text' id='weekPerVal"+i+"' name='targetPerValue' value="+Math.round(percentVal*100)/100+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotalPer(this.id)\"></td></tr>";  
                                         }
                                     }
                                      html+="<tr><td width='25%'><h5>Total</h5></td><td width='25%'><input type='text' id='weekTotalTargetId' name='' value="+targetVal+" readonly></td><td width='25%'><input type='text' id='weekTotalPerId' name='' value='100' readonly></td></tr>";
//                                      html1+="<tr> <td align='center'><input type='button'  name='' value='Done' onclick=\"displayWeekLevelData("+eleID+",'"+regionName+"',"+targetVal+","+nooWeeks+")\" ></td><tr>"
                                      html1+="<tr> <td align='center'><input type='button'  name='' value='Done' onclick=\"insertdisplayWeekLevelData("+eleID+",'"+regionName+"',"+targetVal+","+nooWeeks+")\" ></td><tr>"
                                      $("#weeklevelAlocationId").html(html);
                                      $("#weekLevelDoneButtonId").html(html1);
                                         
                                    }
                                })
                                       
                                 $("#weekLevelAllocationId").dialog('option','title',monthName+' Week Level Allocation');
                                 $("#weekLevelAllocationId").dialog('open');
                            break;
                        }    
                }
            }
            var supportivemeaIds ='';
            function supportiveMeasure(eleId,eleName,bussroleId){
                $.ajax({
                    url:'userLayerAction.do?userParam=supportMeasures&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName,
                    success:function(data){
                        if(data!='No TargetValues'){
                            var evalJson = eval('('+data+')');
                            var measnames = '';
                            var measureIds = '';
                            measnames = evalJson.MeasureNames; 
                            measureIds = evalJson.MeasureIds;
                            var html = "";
                            var html1 = "";
                            html +="<tr><td class='myhead'>Select Measure</td>";
                            html +="<td><select id='supportiveMeasureId' name=''>";
                            $.ajax({
                                url:'userLayerAction.do?userParam=getSupportiveMeasures&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName,
                                success:function(data){
                                    if(data!='no supportiveMeas'){
                                        supportivemeaIds = data;
                                    }
                                }
                            });
                            for(var i=0;i<measureIds.length;i++){
                                if(supportivemeaIds!='' && supportivemeaIds==measureIds[i]){
                                    html +="<option value='"+measureIds[i]+","+measnames[i].replace("_"," ","gi")+"' selected>"+measnames[i].replace("_"," ","gi")+"</option>";
                                }
                                else{
                                    html +="<option value='"+measureIds[i]+","+measnames[i].replace("_"," ","gi")+"'>"+measnames[i].replace("_"," ","gi")+"</option>";  
                                }
                            }
                            html+="</select></td></tr>"
                            html1+="<tr><td align='center'><input type='button' name='' value='Done' onclick=\"supportiveMeasures()\"></td></tr>"
                            $("#suppMeasureIds").html(html);
                            $("#suppMeasureIdValue1").html(html1);
                            $("#defineSupportiveMeasId").dialog('open')
                        }
                        else{
                            alert(eleName+" Does not have supportive measures!");
                        }
                    }
                });
            }
            function supportiveMeasures(){
                var supportMeasure = $("#supportiveMeasureId").val()
                $.ajax({
                    url:'userLayerAction.do?userParam=updateSupportiveMeasures&bussRoleId='+bsrSelectedID+"&elementID="+eleID+"&elementName="+elemtName+"&supportMeasure="+supportMeasure,
                    success:function(data){
                    }
                })
                $("#defineSupportiveMeasId").dialog('close');
            }
            function changeRangeLevel(periodtype){
                var level = '';
                if(periodtype=='level'){
                    level = $("#level").val();
                    if(endValue!='' && startValue!=''){
                        endValue='';
                        startValue='';
                    }
                }
                else{
                    level = periodtype
                }
                if(level=='Day'){
                    $("#timeBasedTable").find("tr:gt(0)").remove();
                    var tableObj = document.getElementById("timeBasedTable");
                    var rowCount = tableObj.rows.length;
                    var row = tableObj.insertRow(rowCount);
                    var rangeHtml = "<td width='40%' class='myhead'><font>From Day<font></td><td width='60%'><input type='text'  name='datepicker2' id='datepicker2'value=''></td>";
                    row.innerHTML = rangeHtml;
                    rowCount++;
                    var row1 = tableObj.insertRow(rowCount);
                    rangeHtml = "<td width='40%' class='myhead'><font>To Day</font></td><td width = '60%'><input type='text'  name='datepicker3' id='datepicker3' value=''></td>";
                    row1.innerHTML = rangeHtml;
                    test();
                }else if(level=='Quarter'){
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetails&elementId='+eleID+'&levelType=Quarter',
                        success: function(data){
                            var details = eval('('+data+')');
                            cust_name = details.cust_name;
                            cust_st_date = details.cust_st_date;
                            cust_end_date =  details.cust_end_date;

                            $("#timeBasedTable").find("tr:gt(0)").remove();
                            var tableObj = document.getElementById("timeBasedTable");
                            var rowCount = tableObj.rows.length;
                            var row = tableObj.insertRow(rowCount);
                            var rangeHtml = "<td width='40%' class='myhead'><font>From Quarter</td><td width='60%'>";
                            rangeHtml =  rangeHtml+"<select name='selectQuarter1' id='selectQuarter1' name='selectQuarter1'  style='width:150px;'>";
                            for(var i=0;i<cust_st_date.length;i++){
                                if(startValue!='' && cust_st_date[i]==startValue)
                                    rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+" selected>"+cust_name[i]+"</option>";
                                else
                                    rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+">"+cust_name[i]+"</option>";
                            }
                            rangeHtml = rangeHtml+"</select></td>"
                            row.innerHTML = rangeHtml;
                            rowCount++;
                            var row1 = tableObj.insertRow(rowCount);
                            rangeHtml = "<td width='40%' class='myhead'><font>To Quarter</td><td width='60%'>";
                            rangeHtml = rangeHtml+"<select name='selectQuarter2' id='selectQuarter2'  style='width:150px;'>";
                             for(var i=0;i<cust_end_date.length;i++){
                                if(endValue!='' && cust_st_date[i]==endValue)
                                    rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_name[i]+"</option>";
                                else
                                    rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+">"+cust_name[i]+"</option>";
                            }
                            rangeHtml = rangeHtml+"</select></td>"
                            row1.innerHTML = rangeHtml;

                        }
                    });
                }else if(level=='Month'){
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetails&elementId='+eleID+'&levelType=Month',
                        success: function(data){
                            var details = eval('('+data+')');
                            cust_name = details.cust_name;
                            cust_st_date = details.cust_st_date;
                            cust_end_date =  details.cust_end_date;

                            $("#timeBasedTable").find("tr:gt(0)").remove();
                            var tableObj = document.getElementById("timeBasedTable");
                            var rowCount = tableObj.rows.length;
                            var row = tableObj.insertRow(rowCount);
                            var rangeHtml = "<td width='40%' class='myhead'><font>From Month</td><td width='60%'>";
                            rangeHtml =  rangeHtml+"<select name='selectMonth1' id='selectMonth1' name='selectMonth1'  style='width:150px;'>";
                            for(var i=0;i<cust_st_date.length;i++){
                                if(startValue!='' && cust_st_date[i]==startValue)
                                    rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+" selected>"+cust_name[i]+"</option>";
                                else
                                    rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+">"+cust_name[i]+"</option>";
                            }
                            rangeHtml = rangeHtml+"</select></td>"
                            row.innerHTML = rangeHtml;
                            rowCount++;
                            var row1 = tableObj.insertRow(rowCount);
                            rangeHtml = "<td width='40%' class='myhead'><font>To Month</td><td width='60%'>";
                            rangeHtml = rangeHtml+"<select name='selectMonth2' id='selectMonth2' name='selectMonth2' style='width:150px;'>";
                            for(var i=0;i<cust_end_date.length;i++){
                                if(endValue!='' && cust_end_date[i]==endValue)
                                    rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_name[i]+"</option>";
                                else
                                    rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+">"+cust_name[i]+"</option>";
                            }
                            rangeHtml = rangeHtml+"</select></td>"
                            row1.innerHTML = rangeHtml;
                        }
                    });

                }else if(level=='Week'){
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetails&elementId='+eleID+'&levelType=Week',
                        success: function(data){
                            var details = eval('('+data+')');
                            cust_name = details.cust_name;
                            cust_st_date = details.cust_st_date;
                            cust_end_date =  details.cust_end_date;

                            $("#timeBasedTable").find("tr:gt(0)").remove();
                            var tableObj = document.getElementById("timeBasedTable");
                            var rowCount = tableObj.rows.length;
                            //var row = tableObj.insertRow(rowCount);
                            var rangeHtml = "<td width='40%' class='myhead'><font>From Week</td><td width='60%'>";
                            rangeHtml =  rangeHtml+"<select name='selectWeek1' id='selectWeek1' name='selectWeek1'  style='width:150px;'>";
                            for(var i=0;i<cust_st_date.length;i++){
                                if(startValue!='' && cust_st_date[i]==startValue)
                                    rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+" selected>"+cust_name[i]+"</option>";
                                else
                                    rangeHtml =  rangeHtml+"<option value="+cust_st_date[i]+">"+cust_name[i]+"</option>";
                            }

                            rangeHtml = rangeHtml+"</select></td>"
                            $('#timeBasedTable tr:last').after('<tr>'+rangeHtml+'</tr>');
                            rangeHtml = "<td width='40%' class='myhead'><font>To Week</td><td width='60%'>";
                            rangeHtml = rangeHtml+"<select name='selectWeek2' id='selectWeek2' style='width:150px;'>";

                            for(var i=0;i<cust_st_date.length;i++){
                                if(endValue!='' && cust_st_date[i]==endValue)
                                    rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+" selected>"+cust_name[i]+"</option>";
                                else
                                    rangeHtml =  rangeHtml+"<option value="+cust_end_date[i]+">"+cust_name[i]+"</option>";
                            }
                            rangeHtml = rangeHtml+"</select></td>"
                            $('#timeBasedTable tr:last').after('<tr>'+rangeHtml+'</tr>');
                        }
                    });

                }else if(level=='Year'){
                    $.ajax({
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getLevelDetailsForDiscreteFromTargetMe&elementId='+eleID+'&levelType=Year',
                        success: function(data){
                            var details = eval('('+data+')');
                            cust_year_array= details.cust_year_details;
                            custom_year= details.cust_year;
                            $("#timeBasedTable").find("tr:gt(0)").remove();
                            var tableObj = document.getElementById("timeBasedTable");
                            var rowCount = tableObj.rows.length;
                            //var row = tableObj.insertRow(rowCount);
                            var rangeHtml = "<td width='40%' class='myhead'><font>From Year</td><td width='60%'>";
                            rangeHtml =  rangeHtml+"<select id='selectYear1' name='selectYear1'  style='width:150px;'>";
                            for(var i=0;i<cust_year_array.length;i++){
                                if(startValue!='' && cust_year_array[i]==startValue)
                                    rangeHtml =  rangeHtml+"<option value="+cust_year_array[i]+" selected>"+custom_year[i]+"</option>";
                                else
                                    rangeHtml =  rangeHtml+"<option value="+cust_year_array[i]+">"+custom_year[i]+"</option>";
                            }

                            rangeHtml = rangeHtml+"</select></td>"
                            $('#timeBasedTable tr:last').after('<tr>'+rangeHtml+'</tr>');
                            rangeHtml = "<td width='40%' class='myhead'><font>To Year</td><td width='60%'>";
                            rangeHtml = rangeHtml+"<select name='selectYear2' id='selectYear2' style='width:150px;'>";
                             for(var i=0;i<cust_year_array.length;i++){
                                if(endValue!='' && cust_year_array[i]==endValue)
                                    rangeHtml =  rangeHtml+"<option value="+cust_year_array[i]+" selected>"+custom_year[i]+"</option>";
                                else
                                    rangeHtml =  rangeHtml+"<option value="+cust_year_array[i]+">"+custom_year[i]+"</option>";
                            }
                            rangeHtml = rangeHtml+"</select></td>"
                            $('#timeBasedTable tr:last').after('<tr>'+rangeHtml+'</tr>');
                        }
                    });
                }
            
            }
            function displayPeriodDetails(){
                var periodTypeId = $("#level").val();
                var bussRoleId = $('#bsrSelect').val();
                if(periodTypeId!=''){
                    if(periodTypeId=='Year'){
                        var year1 = $("#selectYear1").val();
                        var year2 = $("#selectYear2").val();
                        $.ajax({
                            url:'userLayerAction.do?userParam=insertTargetMeasureDetails&bussRoleId='+bussRoleId+"&periodTypeId="+periodTypeId+"&elementID="+eleID+"&elementName="+elemtName+"&startValue="+year1+"&endValue="+year2,
                            success: function(data) {
                            }
                        });
                    }
                    if(periodTypeId=='Quarter'){
                        var quarter1 = $("#selectQuarter1").val();
                        var quarter2 = $("#selectQuarter2").val();
                        $.ajax({
                            url:'userLayerAction.do?userParam=insertTargetMeasureDetails&bussRoleId='+bussRoleId+"&periodTypeId="+periodTypeId+"&elementID="+eleID+"&elementName="+elemtName+"&startValue="+quarter1+"&endValue="+quarter2,
                            success: function(data) {
                            }
                        });
                    }
                    if(periodTypeId=='Month'){
                        var month1 = $("#selectMonth1").val();
                        var month2 = $("#selectMonth2").val();
                        $.ajax({
                            url:'userLayerAction.do?userParam=insertTargetMeasureDetails&bussRoleId='+bussRoleId+"&periodTypeId="+periodTypeId+"&elementID="+eleID+"&elementName="+elemtName+"&startValue="+month1+"&endValue="+month2,
                            success: function(data) {
                            }
                        });
                    }
                    if(periodTypeId=='Day'){
                        var html = "";
                        var startDate = document.getElementById("datepicker2").value;
                        var endDate =  document.getElementById("datepicker3").value;
                    }
                    $("#enableTargetTimeId").dialog('close');
                }
                else{
                    alert("Please Select Period Type!")
                }
            }
            function displyTargetMeasurePeriod(periodtype,startValue,endValue,selectYear,eleName,bussId,rollingtype){
                var htmltype = "targetMeasure";
            
                $.ajax({
                    url:'userLayerAction.do?userParam=testWhetherExcelFileExist&elementID='+eleID+"&periodtype="+periodtype+"&bussId="+bussId,
                    success: function(data) {
                        if(data!='File Exist'){
                            $.ajax({
                                url:'userLayerAction.do?userParam=getTargetMeasureTimeDetails&elementID='+eleID+"&eleName="+eleName+"&periodtype="+periodtype+"&startValue="+startValue+"&endValue="+endValue+"&selectYear="+selectYear+"&bussId="+bussId+"&rollingtype="+rollingtype,
                                success: function(data) {
                                    var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                                    var dSrc = document.getElementById("oneFrame");
                                    dSrc.src = source;
                                }
                            });
                        }else{
                            var dSrc = document.getElementById("uploadSheet");
                            dSrc.src ='<%=request.getContextPath()%>/uploadExcelData.jsp?elementID='+eleID+"&periodtype="+periodtype+"&bussId="+bussId+"&startValue="+startValue+"&endValue="+endValue+"&elemtName="+eleName+"&fromExcelUpload=false";
                            $("#uploadExcel").dialog('open');
                            
                        }
                    }
                });
           
            }
            
            function defineTargets(elementId,bussroleId,startvalue,endvalue,periodType){
            
                $("#defineTargetId").dialog('close');
            
                $.post('<%=request.getContextPath()%>/userLayerAction.do?userParam=insertTargetMeasureValueDetails&elementId='+elementId+"&bussroleId="+bussroleId+"&startvalue="+startvalue+"&endvalue="+endvalue+"&periodType="+periodType+"&elemtName="+elemtName,$("#targetPeriodId").serialize(),
                function(data){
                });
            }
            function isNumberKey(evt)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode
                if (charCode != 46 && charCode > 31
                    && (charCode < 48 || charCode > 57))
                    return false;

                return true;
            }
            function displayYearsforExecelUpload(eleId,eleName,bussId,selYear){
                $("#defineTargetId").dialog('close');
                var selectYear =''
                // var rollingtype =''
                if(selYear!=""){
                    selectYear = $("#selectYear").val();
                    //                    rollingtype = $("#rollingTypeId11").val();
                }
                else{
                    selectYear = selYear;
                    //                    rollingtype = rolltype;
                }
                $.ajax({
                    url:'userLayerAction.do?userParam=testTargetsExistorNot&bussRoleId='+bussId+"&elementID="+eleId+"&elementName="+eleName+"&selectYear="+selectYear,
                    success:function(data){
                        var evalJson = eval('('+data+')');
                        var periodtype = '';
                        var startValue = '';
                        var endValue = '';
                        periodtype = evalJson.TargetPeriodDetails[0];
                        startValue = evalJson.TargetPeriodDetails[1];
                        endValue = evalJson.TargetPeriodDetails[2];
                        displyTargetMeasurePeriod(periodtype,startValue,endValue,selectYear,eleName,bussId);
                    }
                });
            }
            function getDimviewBys(bussRole,elemId,elemName){
                var viewByVals="";
                var colsUl=document.getElementById("sortable");
                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            viewByVals=viewByVals+","+colIds[i].id.replace("_li","");
                        }
                        if(viewByVals!=""){
                            viewByVals = viewByVals.substring(1);
                        }
                    }
                }
                if(viewByVals!=""){
                    $.ajax({
                        url:'userLayerAction.do?userParam=insertTargetParameters&bussRoleId='+bussRole+"&elementID="+elemId+"&viewByVals="+viewByVals,
                        success:function(data){
                        }
                    });
                    $("#targetViewsDivId").dialog('close');
                    getBussRole()
                }
                else{
                    alert("Please Select Elements!");
                }
            }
            function downloadTargetExcelData(eleId,bussId,startval,endval,periodtype){
                var startDate = $("#startDateId").val();
                var endDate = $("#endDateId").val();
                var htmltype = "targetMeasure";
                if(startDate==startval && endDate==endval){
                    $.ajax({
                        url:'userLayerAction.do?userParam=getTargetMeasureTimeDetails&elementID='+eleId+"&periodtype="+periodtype+"&startValue="+startDate+"&endValue="+endDate+"&eleName="+elemtName+"&bussId="+bussId+"&fromdownLoadOption=true",
                        success: function(data) {
                            var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                            var dSrc = document.getElementById("oneFrame");
                            dSrc.src = source;
                        }
                    });
                    $("#downLoadTargetRange").dialog('close');
                }
                else{
                    alert("Target period range not matching with target Duration")
                }
            }
            function targetExecelUpload(eleId,eleName,bussId){
                $("#defineTargetId").dialog('close');
                var selectYear = $("#selectYear").val();
                $.ajax({
                    url:'userLayerAction.do?userParam=testTargetsExistorNot&bussRoleId='+bussId+"&elementID="+eleId+"&elementName="+eleName+"&selectYear="+selectYear,
                    success:function(data){
                        if(data!="No PeriodDetails"){
                            var evalJson = eval('('+data+')');
                            var periodtype = '';
                            var startValue = '';
                            var endValue = '';
                            periodtype = evalJson.TargetPeriodDetails[0];
                            startValue = evalJson.TargetPeriodDetails[1];
                            endValue = evalJson.TargetPeriodDetails[2];
                            downloadandUpload(periodtype,startValue,endValue,selectYear,eleName,bussId);
                        }
                        else{
                            alert("Please Define Target Period Type and Duration!")
                        }
                    }
                });  
            }
            function downloadandUpload(periodtype,startValue,endValue,selectYear,eleName,bussId){
                var htmltype = "targetMeasure";
                $.ajax({
                    url:'userLayerAction.do?userParam=downLoadTargetValuesDuration&elementID='+eleID+"&eleName="+eleName+"&periodtype="+periodtype+"&startValue="+startValue+"&endValue="+endValue+"&selectYear="+selectYear+"&bussId="+bussId,
                    success: function(data) {
                        var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                        var dSrc = document.getElementById("oneFrame");
                        dSrc.src = source;
                    }
                });
                var dSrc = document.getElementById("uploadSheet");
                dSrc.src ='<%=request.getContextPath()%>/uploadExcelData.jsp?elementID='+eleID+"&periodtype="+periodtype+"&bussId="+bussId+"&startValue="+startValue+"&endValue="+endValue+"&elemtName="+eleName+"&fromExcelUpload=true";
                $("#uploadExcel").dialog('open');
            }
            function downLoadRollingTypeData(eleId,bussId,regName,monthVal,monthName){
                var rollType = $("#rollingTypeId").val();
                var htmltype = "targetMeasure";
                $.ajax({
                    url:'userLayerAction.do?userParam=downLoadMonthWiseData&elementID='+eleId+"&regionName="+regName+"&monthVal="+monthVal+"&bussId="+bussId+"&rollType="+rollType+"&monthName="+monthName,
                    success:function(data){
                        var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                        var dSrc = document.getElementById("oneFrame");
                        dSrc.src = source;
                    }
                })
                $("#defineRollingTypeId").dialog('close');
            }
            function selectRollingMonthsTypes(){
                var roleType =  $("#typerollingMonthsId").val();
                if(roleType=='rolingMonths'){
                    $("#descMonthtypeId").hide();
                    $("#rolingMothTypeId").show();
                }else if(roleType=='descrMonths'){
                    $("#descMonthtypeId").show();
                    $("#rolingMothTypeId").hide();
                }
                else if(roleType=='no'){
                    $("#descMonthtypeId").hide();
                    $("#rolingMothTypeId").hide();
                }
            }
            function rolingYearType(){
                var yoyval = $("#rolingYearId").val();
                if(yoyval=='no'){
                    $("#displayRollingyearsId").hide();
                }
                else{
                    $("#displayRollingyearsId").show();
                }
            }
            function selectDescreteMonths(){
                var eleid = eleID;
                viewBylent=''
                viewByValmonts=''
                var noofDesmonths = $("#desceteMonthsTypesId").val();
                $.ajax({
                    url:'userLayerAction.do?userParam=getallDimViewBys&elementID='+eleID+"&bussRoleId="+bsrSelectedID+"&fromDescrete=true",
                    success:function(data){
                        var jsonVar=eval('('+data+')');
                        var html = '';
                        html+="<br><center><table align='center'><tr><td><input type='button' name='Done' value='Done' onclick=\"testForDesceteMonths("+bsrSelectedID+","+eleID+",'"+elemtName+"',"+noofDesmonths+")\"></td></tr></table></center>";
                        $("#descreteMonthsDivID").html(jsonVar.htmlStr+html);
                      
                        $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });

                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        })
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
                        grpColArray=jsonVar.memberValues;
                        $(".sortable").sortable();
                        $("#descreteMonthsDivID").dialog('open');
                    }
                });
            } 
            var viewBylent='';
            var viewByValmonts='';
            function testForDesceteMonths(bullroleId,eleId,elemnName,noofMonths){
                var viewByVals="";
                var colsUl=document.getElementById("sortable");
                if(colsUl!=undefined || colsUl!=null){
                    var colIds=colsUl.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            viewByVals=viewByVals+","+colIds[i].id.replace("_li","");
                        }
                        if(viewByVals!=""){
                            viewByVals = viewByVals.substring(1);
                        }
                    }
                }
                viewByValmonts = viewByVals;
                viewBylent = viewByVals.split(",")
                if(viewBylent.length==noofMonths){
                    $("#descreteMonthsDivID").dialog('close');
                }
                else{
                    alert("Please Select only "+noofMonths+" Months")
                }
            }
            function additionalInfo(){
                var htmltype = "targetMeasure";
                var rolingYearId =$("#rolingYearId").val();
                var typeofRollingmonthId =$("#typerollingMonthsId").val();
                var rolingMonths=''
                var noofRollingyears=''
                var rolingDescr=''
                if(rolingYearId!='no'){
                    noofRollingyears =$("#noofRollingyearsId").val();
                }
                if(typeofRollingmonthId!='no' && typeofRollingmonthId=='rolingMonths'){
                    rolingMonths =$("#rollingMonthsTypesId").val();
                }
                else if(typeofRollingmonthId!='no' && typeofRollingmonthId=='descrMonths'){
                    rolingDescr =$("#desceteMonthsTypesId").val();
                }
                if(typeofRollingmonthId!='no' || rolingYearId!='no'){
                    $.ajax({
                        url:'userLayerAction.do?userParam=downLoadYoyandMomOfTarge&elementID='+eleID+"&bussRoleId="+bsrSelectedID+"&rolingYearId="+rolingYearId+"&typeofRollingmonthId="+typeofRollingmonthId+"&rolingMonths="+rolingMonths+"&noofRollingyears="+noofRollingyears+"&rolingDescr="+rolingDescr+"&viewByValmonts="+viewByValmonts,
                        success:function(data){
                             var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                                    var dSrc = document.getElementById("oneFrame");
                                    dSrc.src = source;
                        }
                    });
                    $("#addtionalInfo").dialog("close");
                }
                else{
                    alert("Please Select Rolling Types")
                }
            }
            var jsonValue = '';
            var monthTarget = '';
            function displayDayLevelData(eleId,monthName){
            $("#dayLevelAllocationId").dialog("close");
            var autoAlloChecked = false;
            var manualAlloChecked = false
            var autoAlloPerId = false;
              if($('#autoAlloId').is(':checked')){
                autoAlloChecked=true;
              }
              else if($('#autoAlloPerId').is(':checked')){
                 autoAlloPerId = true;
               }
               else{
                   manualAlloChecked = true;
               }
               $.ajax({
                       url:'userLayerAction.do?userParam=getdayLevelData&elementID='+eleId+"&monthName="+monthName+"&autoAlloChecked="+autoAlloChecked+"&manualAlloChecked="+manualAlloChecked,
                       success:function(data){
                           jsonValue = eval('('+data+')');
                           var html='';
                           var html1='';
                           var html2='';
                           var autoAllocation = 0;
                           var autoPecent = 0;
                           monthTarget = jsonValue.TargetValue[0];
                            autoAllocation=parseFloat(monthTarget/jsonValue.MonthDays.length);
                            autoAllocation=Math.round(autoAllocation)
                            var autoalocate = Math.round(autoAllocation*jsonValue.MonthDays.length);
                            var diff = autoAllocation+parseInt(monthTarget-parseInt(autoalocate))
                            
                            autoPecent = parseFloat((autoAllocation*100)/monthTarget)
                            autoPecent= Math.round((autoPecent*100)/100)
                            var totalPer = autoPecent*jsonValue.MonthDays.length;
                            var differnVal =autoPecent+parseInt(100-parseInt(totalPer))
                           
                           html1+="<tr><td width='50%' align='center'>Absolute Allocation:<input type='radio'  name='absouteorPercentBase' value='' checked></td><td width='50%' align='center'>% Allocation:<input type='radio'  name='absouteorPercentBase' value=''></td></tr>";
                           $("#absoulteOrPertype").html(html1);
                           if(autoAlloPerId){
                           html+="<tr><td width='25%'><h4>Week</h4></td><td width='25%'><h4>Date</h4></td><td width='25%'><h4>Days</h4></td><td width='25%'><h4>Target %</h4></td><tr>";
                           }
                           else{
                             html+="<tr><td width='25%'><h4>Week</h4></td><td width='25%'><h4>Date</h4></td><td width='25%'><h4>Days</h4></td><td width='25%'><h4>Target</h4></td><tr>";  
                           }
                           //html+="<tr><td width='25%'><h5>Total</h5></td><td width='25%'></td><td width='25%'><input type='text' id='totalId1' name='' value='' readonly></td></tr>";  
                           for(var i=0;i<jsonValue.MonthDays.length;i++){
                               if(autoAlloChecked){
                                  if(i==jsonValue.MonthDays.length-1){
                                     html+="<tr><td width='25%'><input type='text' name='weekNames' value="+jsonValue.WeekNumbers[i]+" readonly size='10'></td><td width='25%'><input type='text' name='monthDates' value="+jsonValue.MonthDays[i]+"-"+monthName+" readonly></td><td width='25%'><input type='text' name='monthDayNames' value="+jsonValue.MonthNames[i]+" readonly></td><td width='25%' ><input type='text' id='percentClomn"+i+"'  name='daylevelTargetValues' value="+diff+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotal(this.id)\"></td></tr>";  
                                   }
                                  else{
                                     html+="<tr><td width='25%'><input type='text' name='weekNames' value="+jsonValue.WeekNumbers[i]+" readonly size='10'></td><td width='25%'><input type='text' name='monthDates' value="+jsonValue.MonthDays[i]+"-"+monthName+" readonly></td><td width='25%'><input type='text' name='monthDayNames' value="+jsonValue.MonthNames[i]+" readonly></td><td width='25%' ><input type='text' id='percentClomn"+i+"'  name='daylevelTargetValues' value="+autoAllocation+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotal(this.id)\"></td></tr>";     
                                  }
                               }
                               else if(manualAlloChecked){
                                 html+="<tr><td width='25%'><input type='text' name='weekNames' value="+jsonValue.WeekNumbers[i]+" readonly size='10'></td><td width='25%'><input type='text' name='monthDates' value="+jsonValue.MonthDays[i]+"-"+monthName+" readonly></td><td width='25%'><input type='text' name='monthDayNames' value="+jsonValue.MonthNames[i]+" readonly></td><td width='25%' ><input type='text' id='percentClomn"+i+"'  name='daylevelTargetValues' value='' onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotal(this.id)\"></td></tr>";     
                               }
                               else{
                                  if(i==jsonValue.MonthDays.length-1){
                                  html+="<tr><td width='25%'><input type='text' name='weekNames' value="+jsonValue.WeekNumbers[i]+" readonly size='10'></td><td width='25%'><input type='text' name='monthDates' value="+jsonValue.MonthDays[i]+"-"+monthName+" readonly></td><td width='25%'><input type='text' name='monthDayNames' value="+jsonValue.MonthNames[i]+" readonly></td><td width='25%' ><input type='text' id='percentClomn"+i+"'  name='daylevelTargetValues' value="+differnVal+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotal(this.id)\"></td></tr>";      
                                  }
                                  else{
                                   html+="<tr><td width='25%'><input type='text' name='weekNames' value="+jsonValue.WeekNumbers[i]+" readonly size='10'></td><td width='25%'><input type='text' name='monthDates' value="+jsonValue.MonthDays[i]+"-"+monthName+" readonly></td><td width='25%'><input type='text' name='monthDayNames' value="+jsonValue.MonthNames[i]+" readonly></td><td width='25%' ><input type='text' id='percentClomn"+i+"'  name='daylevelTargetValues' value="+autoPecent+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotal(this.id)\"></td></tr>";         
                                  }
                               }
                           }
                           html+="<tr><td width='25%'><h5>Total</h5></td><td width='25%'></td><td width='25%'></td><td width='25%'><input type='text' id='totalId' name='' value='' readonly></td></tr>";  
                           html2+="<tr><td align='center'><input type='button' name='' value='Save' onclick=\"insertDayLevelAllocation("+eleId+",'"+monthName+"',"+autoAlloChecked+","+autoAlloPerId+","+manualAlloChecked+")\"></td></tr>";
                           $("#dayLevelDataId").html(html);
                           $("#dayLevelDoneId").html(html2);
                           if(autoAlloChecked){
                               $("#totalId").val(monthTarget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                           }
                           if(autoAlloPerId){
                               $("#totalId").val('100');
                           }
                            $("#dayLevelDisplayDataId").dialog('option','title',monthName+' Day Level Allocation');
                           $("#dayLevelDisplayDataId").dialog("open");
                       }
                    });
            }
        function calculateTotal(id){
          var total=0;
                     for(var i=0;i<jsonValue.MonthDays.length;i++){
                        var mesval=$("#percentClomn"+i).val();
                       if(mesval!="")
                            total=total+parseFloat(mesval);
                        }
                        total=Math.round(total*100)/100
                        if(total>monthTarget){
                            $("#totalId").css({'color':'green'})
                            $("#totalId").val(total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                        }
                        else if(total==monthTarget){
                             $("#totalId").css({'color':'blue'})  
                             $("#totalId").val(total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                        }
                        else if(total<monthTarget){
                             $("#totalId").css({'color':'red'})  
                             $("#totalId").val(total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                        }
                         else{
                             $("#totalId").val(total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                        }
       }
       function insertDayLevelAllocation(eleId,monthName,autoMan,autoPer,manual){
           var dayLevelallocation = 'dayLevelAlloction';
           var totalVal = $("#totalId").val();
           if(autoMan && totalVal.replace(/\,/g,'')!=monthTarget){
               alert("Total Value Should be "+monthTarget);
           }
           else if(autoPer && totalVal!='100'){
               alert("Total Percent(%) Should be 100");
           }
           else if(manual && totalVal.replace(/\,/g,'')!=monthTarget){
               alert("Total Value Should be "+monthTarget);
           }
         else{
                $.post(
                '<%=contextPath%>/userLayerAction.do?userParam=insertDeayLevelDataFrom&elementID='+eleId+"&monthName="+monthName+"&autoMan="+autoMan+"&autoPer="+autoPer+"&manual="+manual+"&dayLevelallocation="+dayLevelallocation,$("#dayLevelFormDataId").serialize(),
                       function(data){
                    }
                );
              $("#dayLevelDisplayDataId").dialog("close");
          }
             
         }
        function calculateTotTarget(id){
            var total=0;
                      for(var i=1;i<=nooWeeks;i++){
                        var mesval=$("#weekTargetVal"+i).val();
                       if(mesval!="")
                            total=total+parseFloat(mesval);
                        }
                        total=Math.round(total)
                        $("#weekTotalTargetId").val(total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
           }
        function calculateTotalPer(id){
            var total=0;
                     for(var i=1;i<=nooWeeks;i++){
                        var mesval=$("#weekPerVal"+i).val();
                       if(mesval!="")
                            total=total+parseFloat(mesval);
                        }
                        total=Math.round(total)
                         $("#weekTotalPerId").val(total);
          }
//        function displayWeekLevelData(eleId,regionName,targetVal,noofWeeks){ 
//            
//            var totalTargetVal = $("#weekTotalTargetId").val()
//            var totalPercentVal = $("#weekTotalPerId").val()
//            var target = true;
//            var percent = true;
//            if(totalTargetVal.replace(/\,/g,'')!=targetVal){
//                target=false;
//                alert("Target Values Should be "+targetVal);
//            }
//            if(totalPercentVal!='100'){
//                percent=false;
//                alert("Target Total Percent(%) Should be 100");
//            }
//            if(target==true && percent==true){
//                $("#weekLevelAllocationId").dialog("close");
//               $.post(
//                     '<%=contextPath%>/userLayerAction.do?userParam=getdayLevelData&elementID='+eleId+"&monthName="+regionName+"&targetVal="+targetVal+"&noofWeeks="+noofWeeks,$("#weekLevelFormDataId").serialize(),
//                     function(data){
//                          jsonValue = eval('('+data+')');
//                           var html='';
//                           var html1='';
//                           var html2='';
//                           monthTarget = targetVal;
//                           html1+="<tr><td width='50%' align='center'>Absolute Allocation:<input type='radio'  name='absouteorPercentBase' value='' checked></td><td width='50%' align='center'>% Allocation:<input type='radio'  name='absouteorPercentBase' value=''></td></tr>";
//                           $("#absoulteOrPertype").html(html1);
//                             html+="<tr><td width='25%'><h4>Week</h4></td><td width='25%'><h4>Date</h4></td><td width='25%'><h4>Days</h4></td><td width='25%'><h4>Target</h4></td><tr>";  
//                         
//                           for(var i=0;i<jsonValue.MonthDays.length;i++){
//                                html+="<tr><td width='25%'><input type='text' name='weekNames' value="+jsonValue.WeekNumbers[i]+" readonly size='10'></td><td width='25%'><input type='text' name='monthDates' value="+jsonValue.MonthDays[i]+"-"+regionName+" readonly></td><td width='25%'><input type='text' name='monthDayNames' value="+jsonValue.MonthNames[i]+" readonly></td><td width='25%' ><input type='text' id='percentClomn"+i+"'  name='daylevelTargetValues' value="+jsonValue.WeekLevelDistru[i]+" onkeypress=\"return isNumberKey(event)\" onchange=\"calculateTotal(this.id)\"></td></tr>";                                         
//                           }
//                           html+="<tr><td width='25%'><h5>Total</h5></td><td width='25%'></td><td width='25%'></td><td width='25%'><input type='text' id='totalId' name='' value='' readonly></td></tr>";  
//                           html2+="<tr><td align='center'><input type='button' name='' value='Save' onclick=\"insertWeekLevelAllocation("+eleId+",'"+regionName+"')\"></td></tr>";
//                           $("#dayLevelDataId").html(html);
//                           $("#dayLevelDoneId").html(html2);
//                          
//                           $("#totalId").val(monthTarget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
//                           $("#dayLevelDisplayDataId").dialog('option','title',regionName+' Week Level Allocation');
//                           $("#dayLevelDisplayDataId").dialog("open");
//                   });
//             }
//          }       
          
           function insertdisplayWeekLevelData(eleId,regionName,targetVal,noofWeeks){ 
            
            var totalTargetVal = $("#weekTotalTargetId").val()
            var totalPercentVal = $("#weekTotalPerId").val()
            var target = true;
            var percent = true;
            if(totalTargetVal.replace(/\,/g,'')!=targetVal){
                target=false;
                alert("Target Values Should be "+targetVal);
            }
            if(totalPercentVal!='100'){
                percent=false;
                alert("Target Total Percent(%) Should be 100");
            }
            if(target==true && percent==true){
                $("#weekLevelAllocationId").dialog("close");
               $.post(
                     '<%=contextPath%>/userLayerAction.do?userParam=insertTargetWeeklevelData&elementID='+eleId+"&monthName="+regionName+"&targetVal="+targetVal+"&noofWeeks="+noofWeeks,$("#weekLevelFormDataId").serialize(),
                     function(data){
                          
                   });
             }
          }       
          
          function downLoadDayLevelData(eleId,regName,eleName,monthName,levelTypeData){
             var htmltype = "targetMeasure";
                $.ajax({
                    url:'userLayerAction.do?userParam=downLoadMonthDayLevelData&elementID='+eleId+"&regionName="+regName+"&monthVal="+eleName+"&bussId="+bsrSelectedID+"&monthName="+monthName+"&levelTypeData="+levelTypeData,
                    success:function(data){
                        if(data!='No Data'){
                        var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                        var dSrc = document.getElementById("oneFrame");
                        dSrc.src = source;
                        }
                        else if(levelTypeData=='dayLevelAlloction'){
                            alert("Day Level Data not defined on "+monthName)
                        }
                        else if(levelTypeData=='weekLevelAlloction'){
                            alert("Week Level Data not defined on "+monthName)
                        }
                    }
                })
          }
          function downLoadandUploadDayLevelData(eleId,regName,eleName,monthName,levelTypeData){
             var htmltype = "targetMeasure";
                $.ajax({
                    url:'userLayerAction.do?userParam=downLoadMonthDayLevelData&elementID='+eleId+"&regionName="+regName+"&monthVal="+eleName+"&bussId="+bsrSelectedID+"&monthName="+monthName+"&levelTypeData="+levelTypeData,
                    success:function(data){
                        if(data!='No Data'){
                        var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                        var dSrc = document.getElementById("oneFrame");
                        dSrc.src = source;
                        }
                        else if(levelTypeData=='dayLevelAlloction'){
                            alert("Day Level Data not defined on "+monthName)
                        }
                        else if(levelTypeData=='weekLevelAlloction'){
                            alert("Week Level Data not defined on "+monthName)
                        }
                        if(data!='No Data'){
                             var dSrc1 = document.getElementById("uploadSheet");
                           //dSrc1.src ='<%=request.getContextPath()%>/uploadExcelData.jsp?elementID='+eleID+"&periodtype="+periodtype+"&bussId="+bsrSelectedID+"&startValue="+startValue+"&endValue="+endValue+"&elemtName="+eleName+"&fromExcelUpload=true";
                           dSrc1.src ='<%=request.getContextPath()%>/uploadExcelData.jsp?elementID='+eleId+"&bussId="+bsrSelectedID+"&elemtName="+eleName+"&monthName="+monthName+"&regName="+regName+"&dayandWeekLevelDistribution=true";
                           $("#uploadExcel").dialog('open');
                        }
                    }
                })
          }
          function insertWeekLevelAllocation(eleId,monthName){
            var autoPer = false;
            var autoMan = false
            var manual = false;
           var dayLevelallocation = 'weekLevelAlloction';
           var totalVal = $("#totalId").val();
          
           if(autoMan && totalVal.replace(/\,/g,'')!=monthTarget){
               alert("Total Value Should be "+monthTarget);
           }
         else{
                $.post(
                '<%=contextPath%>/userLayerAction.do?userParam=insertDeayLevelDataFrom&elementID='+eleId+"&monthName="+monthName+"&autoMan="+autoMan+"&autoPer="+autoPer+"&manual="+manual+"&dayLevelallocation="+dayLevelallocation,$("#dayLevelFormDataId").serialize(),
                       function(data){
                    }
                );
              $("#dayLevelDisplayDataId").dialog("close");
          }
             
          }
          function leastTimePeriod(eleId,eleName,busrolId){
                            var html = "";
                            var html1 = "";
                            html +="<tr><td class='myhead'>Select Least Time Level</td>";
                            html +="<td><select id='supportiveMeasureId' name='' style='width:150px;'>";
                            html +="<option value=''>--Select--</option>"; 
                            html +="<option value='month'>Month</option>"; 
                            html +="<option value='week/day'>Week/Day</option>"; 
                            html+="</select></td></tr>"
                            html1+="<tr><td align='center'><input type='button' name='' value='Done' onclick=\"leastValueTest()\"></td></tr>"
                            $("#suppMeasureIds").html(html);
                            $("#suppMeasureIdValue1").html(html1);
                            $("#defineSupportiveMeasId").dialog('option','title','LeastTimePeriod level');
                            $("#defineSupportiveMeasId").dialog('open')
          }
          function leastValueTest(){
              var leastvalType = $("#supportiveMeasureId").val();
              if(leastvalType==''){
                  alert("Please Select Least Time Level")
              }
              else{
                  $("#defineSupportiveMeasId").dialog('close')
              }
          }
    </script>

    </body>
</html>

