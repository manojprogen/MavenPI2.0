<%@page contentType="text/html" pageEncoding="UTF-8" import="java.math.BigDecimal,java.util.Arrays,com.google.common.collect.Iterables,com.progen.report.DashletDetail,java.util.List,com.progen.report.pbDashboardCollection"%>
<%@page import="java.util.ArrayList,java.util.HashMap,prg.db.*,com.progen.report.display.util.NumberFormatter,com.progen.db.ProgenDataSet,com.progen.dashboard.DashboardTableColorGroupHelper,com.progen.reportdesigner.db.ReportTemplateDAO"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- 
    Document   : applycolors
    Created on : Jan 8, 2010, 1:00:55 PM
    Author     : Administrator
--%>

<%
    String columnName = request.getParameter("colmnname");
    String labelName = request.getParameter("labelName");
    String disColumnName = request.getParameter("dispcolmname");
    String reportid = request.getParameter("reportid");
    String dashletId = request.getParameter("dashletId");
    boolean isCrosstab = false;
    boolean flag = Boolean.parseBoolean(request.getParameter("flag"));
    HashMap<String, ArrayList> tempdbrMap = new HashMap<String, ArrayList>();
    boolean fromMap = Boolean.parseBoolean(request.getParameter("fromMap"));
    HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
    Container container = (Container) map.get(reportid);
    if (container.isReportCrosstab()) {
        columnName = columnName.replace("A_", "");
    }
    //Added by Ram
    String   applyColorProperty="";
    boolean isColorForAllViewBys = false;
    boolean isColorAppyAcrossCurrentData=false;
    ReportTemplateDAO colorObj=new ReportTemplateDAO();
    applyColorProperty=colorObj.isColorApplyForAllViewBys(reportid);
    String[] value = applyColorProperty.split("_");
    isColorForAllViewBys=Boolean.valueOf(value[0]);
    isColorAppyAcrossCurrentData=Boolean.valueOf(value[1]);
    ArrayList<String> mainMeasureIds = new ArrayList();
    mainMeasureIds.add(columnName);
    char[] dataTypes = null;
    if (null == disColumnName || "" == disColumnName) {
        dataTypes = container.getColumnDataTypes(mainMeasureIds);
        disColumnName = String.valueOf(dataTypes[0]);
    }
    isCrosstab = container.isReportCrosstab();
    ProgenDataSet retObj = container.getRetObj();
    // String nmbrSmbl=container.getNumberSymbol(columnName);
    // List<BigDecimal>minAvgMax=container.getReportCollect().getMinmaxavgValues();
    // BigDecimal minval=BigDecimal.ZERO;
    // BigDecimal avgval=BigDecimal.ZERO;
    //  BigDecimal maxval=BigDecimal.ZERO;
    //  if(minAvgMax.size()!=0){
    //  minval=minval.add(minAvgMax.get(0));
    // avgval=avgval.add(minAvgMax.get(1));
    //maxval=maxval.add(minAvgMax.get(2));
    //}

    String colorData = "";
    if (request.getParameter("colorData") != null) {
        colorData = request.getParameter("colorData");
    }

    HashMap ColorCodeMap = (HashMap) container.getTableHashMap().get("ColorCodeMap");
    //String[] strOperators = {"&lt", "&gt", "&lt=", "&gt=", "=", "!=", "&lt&gt"};
    String[] strOperators = {"<", ">", "<=", ">=", "=", "!=", "<>"};
    String[] StrColors = {"Red", "Green", "Blue", "Orange"};
    String fromModule = "";
    if (request.getParameter("fromModule") != null) {
        fromModule = request.getParameter("fromModule");
    }
    if (fromModule.equalsIgnoreCase("dashboard")) {
        ColorCodeMap = new HashMap();
        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
        List<DashletDetail> dashletList = collect.dashletDetails;
        DashletDetail dashlet = Iterables.find(dashletList, DashletDetail.getDashletDetailPredicatBaseOnDashLetID(dashletId));
        List<DashboardTableColorGroupHelper> tableColorGrpList = dashlet.getDashbrdTableColor();
        ArrayList colorCodes = new ArrayList();
        ArrayList operators = new ArrayList();
        ArrayList sValues = new ArrayList();
        ArrayList eValues = new ArrayList();
        for (int i = 0; i < tableColorGrpList.size(); i++) {
            if (tableColorGrpList.get(i).getElementId().equalsIgnoreCase(columnName.replace("A_", ""))) {
                colorCodes.addAll(tableColorGrpList.get(i).getColorVal());
                operators.addAll(tableColorGrpList.get(i).getColorCondOper());
                sValues.addAll(tableColorGrpList.get(i).getCondStartValue());
                eValues.addAll(tableColorGrpList.get(i).getCondEndValue());
            }
        }
        if (!colorCodes.isEmpty()) {
            tempdbrMap.put("colorCodes", colorCodes);
            tempdbrMap.put("operators", operators);
            tempdbrMap.put("sValues", sValues);
            tempdbrMap.put("eValues", eValues);
        }
        if (!tableColorGrpList.isEmpty() && !tempdbrMap.isEmpty()) {
            ColorCodeMap.put(disColumnName, tempdbrMap);
        }

    }
    String contextPath = request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply Color Based Grouping</title>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            *{
                font: 11px verdana;
            }
        </style>
        <script  type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbTableMapJS.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" />
    </head>
    <body>
        <center>
            <form action="" name="colorfrm" id="colorfrm" method="post">
                <table border="0">
                    <% if (!container.getReportCollect().getMinmaxavgValues().isEmpty() && columnName.equals(container.getDisplayColumns().get(0))) {%>

                    <tr align="left">
                        <td>Maximum Value:</td>
                        <td><input readonly type="text" name="max" id="max" value="<%=NumberFormatter.getModifiedNumberColor(container.getReportCollect().getMinmaxavgValues().get(0), container.getNumberSymbol(columnName))%>" ></td>
                    </tr>
                    <tr align="left">
                        <td>Minimum Value:</td>
                        <td><input readonly type="text" name="min" id="min" value="<%=NumberFormatter.getModifiedNumberColor(container.getReportCollect().getMinmaxavgValues().get(1), container.getNumberSymbol(columnName))%>" ></td>
                    </tr>
                    <tr align="left">
                        <td>Average Value:</td>
                        <td><input readonly type="text" name="avg" id="avg" value="<%=NumberFormatter.getModifiedNumberColor(container.getReportCollect().getMinmaxavgValues().get(2), container.getNumberSymbol(columnName))%>" ></td>
                    </tr>

                    <%} else if(retObj.getColumnMaximumValue(columnName) ==null) {%>
                    <% BigDecimal bd= new BigDecimal ("0"); %>
                    <tr align="left">
                        <td>Maximum Value:</td>
                        <td><input readonly type="text" name="max" id="max" value="<%=NumberFormatter.getModifiedNumberColor(bd, container.getNumberSymbol(columnName))%>" ></td>
                    </tr>
                    <tr align="left">
                        <td>Minimum Value:</td>
                        <td><input readonly type="text" name="min" id="min" value="<%=NumberFormatter.getModifiedNumberColor(bd, container.getNumberSymbol(columnName))%>" ></td>
                    </tr>
                    <tr align="left">
                        <td>Average Value:</td>
                        <td><input readonly type="text" name="avg" id="avg" value="<%=NumberFormatter.getModifiedNumberColor(bd, container.getNumberSymbol(columnName))%>" ></td>
                    </tr>
                    <%}else{%>
                     <tr align="left">
                        <td>Maximum Value:</td>
                        <td><input readonly type="text" name="max" id="max" value="<%=NumberFormatter.getModifiedNumberColor(retObj.getColumnMaximumValue(columnName), container.getNumberSymbol(columnName))%>" ></td>
                    </tr>
                    <tr align="left">
                        <td>Minimum Value:</td>
                        <td><input readonly type="text" name="min" id="min" value="<%=NumberFormatter.getModifiedNumberColor(retObj.getColumnMinimumValue(columnName), container.getNumberSymbol(columnName))%>" ></td>
                    </tr>
                    <tr align="left">
                        <td>Average Value:</td>
                        <td><input readonly type="text" name="avg" id="avg" value="<%=NumberFormatter.getModifiedNumberColor(retObj.getColumnAverageValue(columnName), container.getNumberSymbol(columnName))%>" ></td>
                    </tr>
                    <%}%>
                    <tr>
                        <td>Gradient Based</td>
                        <td><input type="checkbox" name="gradientBased" id="gradientId" onclick="displayGradientColors()" value=""></td>
                    </tr>
                    <tr>
                        <td>Apply condition on all parameters</td>
                        <td><input type="checkbox" name="isAllParameter" id="isAllParameter" onclick="applyForAllParametersColors()" value=""></td>
                    </tr>
                    <tr>
                        <td>Apply condition across current data</td>
                        <td><input type="checkbox" name="isAcrossCurrentData" id="isAcrossCurrentData" onclick="AppyColorAcrossCurrentData()" value=""></td>
                    </tr>
                    <tr>
                        <td>Absolute Based</td>
                        <td colspan="3">
                            <table><tr>
                                    <td><input type="radio" name="colorcodetype" id="AbsoluteColorgrouptype" onclick="ChangeColordodetype('obsulute')" value="obsulute" checked></td>
                        <td>Percentage Based</td>
                        <td><input type="radio" name="colorcodetype" id="AverageColorgrouptype" onclick="ChangeColordodetype('Average')" value="Average"></td>
                        <td>Average Based</td>
                        <td><input type="radio" name="colorcodetype" id="PercentageColorgrouptype" onclick="ChangeColordodetype('Percentage')" value="Percentage"></td>
                        <%if (isCrosstab) {%> 
                        <td>MinMax Based</td>
                        <td><input type="radio" name="colorcodetype" id="minMaxColorgrouptype" onclick="ChangeColordodetype('minmax')" value="minmax"></td>
                       <%}%>
                                </tr>
                            </table>
                        </td>    
                    </tr>
                    <%//if (isCrosstab) {%>
<!--                    <tr>
                        <td></td><td></td>
                        <td>MinMax Based</td>
                        <td><input type="radio" name="colorcodetype" id="minMaxColorgrouptype" onclick="ChangeColordodetype('minmax')" value="minmax"></td>
                    </tr>-->
                    <%//}%>
                </table>
                <table border="0" id="colorTable">
                    <%
                        if (ColorCodeMap != null && ColorCodeMap.get(disColumnName) != null) {
                            HashMap tempMap = null;
                            tempMap = (HashMap) ColorCodeMap.get(disColumnName);
                            String[] colorCodes = null;
                            String[] operators = null;
                            String[] sValues = null;
                            String[] eValues = null;
                            if (fromModule.equalsIgnoreCase("dashboard")) {
                                colorCodes = Arrays.copyOf(tempdbrMap.get("colorCodes").toArray(), tempdbrMap.get("colorCodes").toArray().length, String[].class);
                                operators = Arrays.copyOf(tempdbrMap.get("operators").toArray(), tempdbrMap.get("operators").toArray().length, String[].class);
                                sValues = Arrays.copyOf(tempdbrMap.get("sValues").toArray(), tempdbrMap.get("sValues").toArray().length, String[].class);
                                eValues = Arrays.copyOf(tempdbrMap.get("eValues").toArray(), tempdbrMap.get("eValues").toArray().length, String[].class);

                            } else {
                                tempMap = (HashMap) ColorCodeMap.get(disColumnName);
                                colorCodes = (String[]) tempMap.get("colorCodes");
                                operators = (String[]) tempMap.get("operators");
                                sValues = (String[]) tempMap.get("sValues");
                                eValues = (String[]) tempMap.get("eValues");

                            }
                            if (colorCodes != null) {
                                for (int i = 0; i < colorCodes.length; i++) {
                    %>
                    <tr id="row<%=i%>">
                        <td>
                            Apply Color as
                            <input type="text" name="colorCodes" id="colorCodes<%=i%>" onclick="showColor(this.id)" style='width:50px;cursor:pointer;background-color:<%=colorCodes[i]%>' colorCode='<%=colorCodes[i]%>'>
<!--                            <select name="colorCodes" id="colorCodes<%=i%>">
                            <%for (String Str : StrColors) {
                                        if (Str.equalsIgnoreCase(colorCodes[i])) {%>
                            <option  selected value="<%=Str%>"><%=Str%></option>
                            <%} else {%>
                            <option  value="<%=Str%>"><%=Str%></option>
                            <%}
                                    }%>
                        </select>-->
                            When Value
                        </td>
                        <td>
                            <select name="operators" id="<%=i%>operators" onchange="onoperator(this.id, this.value)">
                                <option value="none">-select-</option>
                                <%for (String Str : strOperators) {
                                        if (Str.equalsIgnoreCase(operators[i])) {%>
                                <option  selected value="<%=Str%>"><%=Str%></option>
                                <%} else {%>
                                <option  value="<%=Str%>"><%=Str%></option>
                                <%}
                                    }%>
                            </select>
                        </td>
                        <td>
                            <input type="text" name="sValues" value="<%=sValues[i]%>" id="sValues<%=i%>">                            
                        </td>
                        <%if (operators[i].equalsIgnoreCase("<>")) {%>
                        <td>
                            <input type="text" name="eValues" value="<%=eValues[i]%>"  id="<%=i%>eValues">
                        </td>
                        <%} else {%>
                        <td>
                            <input type="hidden" name="eValues" value="<%=eValues[i]%>"  id="<%=i%>eValues">
                        </td>
                        <%}%>
                    </tr>
                    <%}
                        } else {
                        }
                    } else {%>
                    <tr id="row0">
                        <td>
                            Apply Color as
                            <input  name="colorCodes" id="colorCodes0" type="text" onclick="showColor(this.id)" value="" style='width:50px;cursor:pointer' colorCode=''>
                            <!--                            <select name="colorCodes" id="colorCodes0">
                            <%for (String Str : StrColors) {%>
                            <option value="<%=Str%>"><%=Str%></option>
                            <%}%>
                        </select>-->
                            When Value
                        </td>
                        <td>
                            <select name="operators" id="0operators" onchange="onoperator(this.id, this.value)">
                                <%for (String Str : strOperators) {%>
                                <option value="<%=Str%>"><%=Str%></option>
                                <%}%> 
                            </select>
                        </td>
                        <td>
                            <input type="text" name="sValues" id="sValues0" >                           
                        </td>
                        <td>
                            <input type="hidden" name="eValues" id="0eValues" >
                        </td>

                    </tr>
                    <%}
                    %>
                </table>
                <table border="0" id="AvgcolorTable" style="display:none">
                    <tr id="avgrow0">
                        <td>
                            Apply Color as
                            <input  name="AvgcolorCodes" id="AvgcolorCodes0" type="text" onclick="showColor(this.id)" value="" style='width:50px;cursor:pointer' colorCode=''>
                            When Value
                        </td>
                        <td>
                            <select name="0operatorsForAvg" id="0operatorsForAvg" onchange="onoperatorAvg(0, this.value)">
                                <%for (String Str : strOperators) {%>
                                <option value="<%=Str%>"><%=Str%></option>
                                <%}%> 
                            </select>
                        </td>
                        <td>
                            <input type="text" name="sAvgValues0" id="sAvgValues0" >
                             % 
                        </td>
                        <td id="0AvgeValuesTd" style="display:none">
                            <input type="hidden" name="0AvgeValues" id="0AvgeValues" >
                            %
                        </td>

                    </tr>
                       </table>  
                            
                         <!--added by Dinanath-->   
                 <table border="0" id="PercentcolorTable" style="display:none">          
                      <tr id="percentrow0">
                        <td>
                            Apply Color as
                            <input  name="PercentcolorCodes" id="PercentcolorCodes0" type="text" onclick="showColor(this.id)" value="" style='width:50px;cursor:pointer' colorCode=''>
                            When Value
                        </td>
                        <td>
                            <select name="0operatorsForPercent" id="0operatorsForPercent" onchange="onoperatorPercent(0, this.value)">
                                <%//for (String Str : strOperators) {%>
                                <option value=">">Above</option>
                                <%//}%> 
                            </select>
                        </td>
                        <td>
                            <input type="text" name="sPercentValues0" id="sPercentValues0" readonly value="<%=NumberFormatter.getModifiedNumberColor(retObj.getColumnAverageValue(columnName), container.getNumberSymbol(columnName))%>">
                              
                        </td>
                        <td id="0AvgeValuesTd" style="display:none">
                            <input type="hidden" name="0PercenteValues" id="0PercenteValues" value="">
                            
                        </td>

                    </tr>
                     <tr id="percentrow1">
                        <td>
                            Apply Color as
                            <input  name="PercentcolorCodes" id="PercentcolorCodes1" type="text" onclick="showColor(this.id)" value="" style='width:50px;cursor:pointer' colorCode=''>
                            When Value
                        </td>
                        <td>
                            <select name="0operatorsForPercent" id="1operatorsForPercent" onchange="onoperatorPercent(1, this.value)">
                                <%//for (String Str : strOperators) {%>
                                <option value="<">Below</option>
                                <%//}%> 
                            </select>
                        </td>
                        <td>
                            <input type="text" name="sPercentValues0" id="sPercentValues1" readonly value="<%=NumberFormatter.getModifiedNumberColor(retObj.getColumnAverageValue(columnName), container.getNumberSymbol(columnName))%>">
                            <!--%-->
                        </td>
                        <td id="0PercenteValuesTd" style="display:none">
                            <input type="hidden" name="0PercenteValues" id="1PercenteValues" value="" >
                            <!--%-->
                        </td>

                    </tr>
                </table>
                <table border="0">                    
                    <tr>
                        <td colspan="3" style="height:10px">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td align="center" colspan="3">
                            <input class="navtitle-hover" type="button" value="Add Row" id="addrow" onclick="addRow()">
                            <input class="navtitle-hover" type="button" value="Delete Row" id="deleterow" onclick="deleteRow()">
                            <input class="navtitle-hover" type="button" value="Done" onclick="savecolors('colorfrm')">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" style="height:10px">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="3"><font size="1" color="red">*</font>Values displayed are in Absolute format.Please enter Absolute values only.</td>
                    </tr>
                </table>
                <input type="hidden" name="isAvgbased" id="isAvgbased" value="false">
                <input type="hidden" name="isPercentbased" id="isPercentbased" value="false">
                <input type="hidden" name="isMinMaxbased" id="isMinMaxbased" value="false">
                <input type="hidden" name="colName" id="colName" value="<%=columnName%>">
                <input type="hidden" name="disColName" id="disColName" value="<%=disColumnName%>">
                <input type="hidden" name="reportid" id="reportid" value="<%=reportid%>">
                <input type="hidden" name="tableChange" id="tableChange" value="applycolor">
            </form>
        </center>
        <div id="colorsDiv" style="display: none" title="Select color">
            <center>
                <input type="text" id="color" style="" value="#12345" >
                <div id="colorpicker" style=""></div>
                <input type="button" align="center" value="Done" class="navtitle-hover" onclick="saveSelectedColor()">
                <input type="button" align="center" value="Cancel" class="navtitle-hover" onclick="cancelColor()">
                <input type="hidden" id="selectedId" value="">
            </center>
        </div>
        <script type="text/javascript">
            //This is the code of validating numeric keys.
            function isNumberKey(evt)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode
                if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46)//Here char code is 46 (.) which allows to press dot
                    return false;
                return true;
            }
            //            function savecolors(){
                    //                document.forms.colorfrm.action = "<%=request.getContextPath()%>/reportViewer.do?reportBy=tableChanges";
            //                document.forms.colorfrm.submit();
                    //                parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?&tabId=<%=reportid%>"
            //                parent.closeapplycolr();
            //            }

//            function savecolors(formId) {
//                alert("Hi....")
//                        if ('<%=fromModule%>' != "") {
////                alert("In Save Mode of DashBoard");
//                    var colorCodeObjs = document.getElementsByName("colorCodes");
//                    var chkObj = document.getElementById("gradientId");
//                    if (!chkObj.checked)
//                        for (var i = 0; i < colorCodeObjs.length; i++)
//                        {
//                            var colorCode = $("#" + colorCodeObjs[i].id).attr('colorCode')
//                            colorCodeObjs[i].value = colorCode
//                        }
//                    $.post("<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=saveDbrColors&colmnlabelName=<%=labelName%>&reportid=<%=reportid%>&dashletId=<%=dashletId%>", $("#" + formId).serialize(),
//                                        function (data) {
//                                            parent.$("#applydbrcolrdiv").dialog('close');
//                                            parent.resetTableData('<%=dashletId%>', '<%=flag%>')
                    parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportid%>"
////                    parent.closeapplycolr();
//                                        });
//                            } else {
//                                var colorCodeObjs = document.getElementsByName("colorCodes");
//                                var chkObj = document.getElementById("gradientId");
//                                var isavgbasedObj = document.getElementById("isAvgbased").value;
//                                var ispercentbasedObj = document.getElementById("isPercentbased").value;
////                                alert("ispercentbasedObj "+ispercentbasedObj);
//                                if (isavgbasedObj == "true") {
//                                    var AvgcolorCodeObjs = document.getElementsByName("AvgcolorCodes");
//                                    for (var i = 0; i < AvgcolorCodeObjs.length; i++)
//                                    {
//                                        var colorCode = $("#" + AvgcolorCodeObjs[i].id).attr('colorCode');
////                        alert("colorcode is"+colorCode);
////                        alert(colorCode)
//                                        AvgcolorCodeObjs[i].value = colorCode
//                                    }
//                                }
//                               if (ispercentbasedObj == "true") {
//                                    var AvgcolorCodeObjs = document.getElementsByName("PercentcolorCodes");
//                                    for (var i = 0; i < AvgcolorCodeObjs.length; i++)
//                                    {
//                                        var colorCode = $("#" + AvgcolorCodeObjs[i].id).attr('colorCode');
////                        alert("colorcode is"+colorCode);
////                        alert(colorCode)
//                                        AvgcolorCodeObjs[i].value = colorCode
//                                    }
//                                }
//                                if (!chkObj.checked)
//                                    for (var i = 0; i < colorCodeObjs.length; i++)
//                                    {
//                                        var colorCode = $("#" + colorCodeObjs[i].id).attr('colorCode')
////                        alert(colorCode)
//                                        colorCodeObjs[i].value = colorCode
//                                    }
//                                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=tableChanges", $("#" + formId).serialize(),
//                                        function (data) {
//                                            parent.document.getElementById("iframe1").src = "<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportid%>"
//                                                                    parent.closeapplycolr();
//                                                                });
//                                                        parent.showMap();
//                                                    }
//                                                }

                                                function validatecolor()
                                                {

                                                    var max = document.getElementById("max").value;
                                                    var min = document.getElementById("min").value;
                                                    var color1, color2, color3, svalue1, svalue2, svalue3, evalue1, evalue2, evalue3;
                                                    color1 = document.getElementById("color1").value;

                                                    color2 = document.getElementById("color2").value;

                                                    color3 = document.getElementById("color3").value;
                                                    if (color1 == color2 || color2 == color3 || color3 == color1)
                                                    {
                                                        alert("Please Select different colors");
                                                        return false;
                                                    }

                                                    svalue1 = document.getElementById("svalue1").value;

                                                    svalue2 = document.getElementById("svalue2").value;

                                                    svalue3 = document.getElementById("svalue3").value;

                                                    evalue1 = document.getElementById("evalue1").value;

                                                    evalue2 = document.getElementById("evalue2").value;

                                                    evalue3 = document.getElementById("evalue3").value;

                                                    if (parseInt(svalue1) > parseInt(max) || parseInt(svalue2) > parseInt(max) || parseInt(svalue3) > parseInt(max) || parseInt(evalue1) > parseInt(max) || parseInt(evalue2) > parseInt(max) || parseInt(evalue3) > parseInt(max))
                                                    {
                                                        alert("please enter svalues then the max vlaues");
                                                        return false;
                                                    }
                                                    if (parseInt(svalue1) < parseInt(min) || parseInt(svalue2) < parseInt(min) || parseInt(svalue3) < parseInt(min) || parseInt(evalue1) < parseInt(min) || parseInt(evalue2) < parseInt(min) || parseInt(evalue3) < parseInt(min))
                                                    {
                                                        alert("Values should not be less then minimum values");
                                                        return false;
                                                    }
                                                    if (parseInt(svalue1) > parseInt(evalue1))
                                                    {
                                                        alert("Value between should be less then grter value");
                                                        return false;
                                                    }
                                                    var op1 = document.getElementById("operator1").value;
                                                    var op2 = document.getElementById("operator2").value;
                                                    var op3 = document.getElementById("operator3").value;

                                                    if (op1 == op2 && svalue1 == svalue2)
                                                    {
                                                        alert("No Two operators and values should  be same");
                                                        return false;
                                                    }
                                                    if (op2 == op3 && svalue2 == svalue3)
                                                    {
                                                        alert("No Two operators and values should be same");
                                                        return false;
                                                    }
                                                    if (op3 == op1 && svalue3 == svalue1)
                                                    {
                                                        alert("No Two operators and values should not be same");
                                                        return false;
                                                    }

                                                    if (parseInt(svalue3) > parseInt(evalue3)) {
                                                        alert("Value between should be less then grter value");
                                                        return false;
                                                    }
                                                    savecolors('colorfrm');
                                                }
                                                function onoperator(id, symbol)
                                                {
                                                    var box = parseInt(id);
                                                    var open = document.getElementById(box + "eValues");
                                                    if (symbol == "<>") {
                                                        open.type = "text";
                                                        open.style.display = "block";
                                                    } else {
                                                        open.style.display = "none";
                                                        open.type = "hidden";
                                                    }
                                                }
                                                function onoperatorAvg(id, symbol)
                                                {
                                                    var box = parseInt(id);
                                                    var open = document.getElementById(id + "AvgeValues");
                                                    if (symbol == "<>") {
                                                        open.type = "text";
                                                        document.getElementById(id + "AvgeValuesTd").style.display = "block";
                                                    } else {
                                                        document.getElementById(id + "AvgeValuesTd").style.display = "none";
                                                        open.style.display = "none";
                                                        open.type = "hidden";
                                                    }
                                                }
                                                function onoperatorPercent(id, symbol)
                                                {
                                                    var box = parseInt(id);
                                                    var open = document.getElementById(id + "PercenteValues");
                                                    if (symbol == "<>") {
                                                        open.type = "text";
                                                        document.getElementById(id + "PercenteValuesTd").style.display = "block";
                                                    } else {
                                                        document.getElementById(id + "PercenteValuesTd").style.display = "none";
                                                        open.style.display = "none";
                                                        open.type = "hidden";
                                                    }
                                                }
                                                function showColor(id)
                                                {
//             parent.$("#applycolrdiv").dialog('close')
                                                    $("#selectedId").val(id);
                                                    var colorCode = "";
                                                    colorCode = $("#" + id).attr('colorCode');
//             alert(colorCode)
                                                    $("#colorsDiv").dialog('open')
                                                    if (colorCode != undefined && colorCode != "")
                                                    {
                                                        $("#color").val(colorCode)
                                                        $("#color").css("background-color", colorCode)
//                 jQuery.updateValue('event')
                                                        $.farbtastic("#color").setColor(colorCode)
                                                    }


                                                }
                                                function saveSelectedColor()
                                                {
                                                    var seletedTextId = $("#selectedId").val();
                                                    var colorCode = $("#color").val();
//              alert(colorCode);
                                                    $("#" + seletedTextId).css("background-color", colorCode)
                                                    $("#" + seletedTextId).attr('colorCode', colorCode);
                                                    $("#colorsDiv").dialog('close')
                                                }
                                                function cancelColor()
                                                {
                                                    $("#colorsDiv").dialog('close')
                                                }
        </script>
        <script type="text/javascript" >
            var defaultAbsFlag=false;
            var defaultAvgFlag=false;
            var defaultPctFlag=false;
            $(document).ready(function () {

                $("#colorsDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 300,
                    width: 300,
                    modal: true,
                    Cancel: function () {
                        $(this).dialog('close');
                    }
                });
                var colorData = '<%=colorData%>';
//                alert(colorData);
                if (colorData != "") {
                    var colorObj = eval('(' + colorData + ')');
//                    alert(colorObj);
//                    alert(colorObj.isMinMaxBased);
//                    alert(colorObj.isPercentBased);
//                    alert(colorObj.isAvgBased);
                    if (colorObj.isMinMaxBased == "true") {
                        document.getElementById("colorTable").style.display = "none";
                        document.getElementById("AvgcolorTable").style.display = "none";
                        document.getElementById("PercentcolorTable").style.display = "none";
                        document.getElementById("isAvgbased").value = "false";
                        document.getElementById("isMinMaxbased").value = "true";
                    }else if (colorObj.isPercentBased == "true") {
                        if (colorObj.isGradientBased == "true")
                        {
                            $("#gradientId").attr("checked", true)
                            $("#gradientId").val(true);
                        } else {
                            $("#gradientId").attr("checked", false)
                            $("#gradientId").val(false);
                        }
                        if (colorObj.isPercentBased == "true")
                        {
                            $("#PercentageColorgrouptype").attr("checked", true)
                            $("#PercentageColorgrouptype").val(true);
                        } else {
                            $("#PercentageColorgrouptype").attr("checked", false)
                            $("#PercentageColorgrouptype").val(false);
                        }
                        document.getElementById("colorTable").style.display = "none";
                        document.getElementById("AvgcolorTable").style.display = "none";
                        document.getElementById("PercentcolorTable").style.display = "";
                        document.getElementById("isAvgbased").value = "false";
                        document.getElementById("isPercentbased").value = "true";
                        document.getElementById("isMinMaxbased").value = "false";
                          $("#PercentcolorTable").html("");
                          addRow();
                        for (var i = 0; i < colorObj.Colors.length; i++) {
                            defaultAvgFlag=true;
                            
                            $("#PercentcolorCodes" + i).attr("colorCode", colorObj.Colors[i]);
                            $("#PercentcolorCodes" + i).css("background-color", colorObj.Colors[i])
                            $("select#" + i + "operatorsForPercent").attr("value", colorObj.Operators[i].replace("_", "").replace("NOT", "!"))
                            $("input#sPercentValues" + i).attr("value", colorObj.StartValues[i])
                            $("input#" + i + "PercenteValues").attr("value", colorObj.EndValues[i])
                            if (colorObj.Operators[i] == '<>') {
                                var id = i + "PercenteValues";
                                onoperatorAvg(i, '<>')
                            } else {
                                $("input#" + i + "PercenteValues").attr("value", "")
                            }
                        }
                    } else if (colorObj.isAvgBased == "true") {
                        
                        document.getElementById("colorTable").style.display = "none";
                        document.getElementById("AvgcolorTable").style.display = "";
                        document.getElementById("PercentcolorTable").style.display = "none";
                        document.getElementById("isAvgbased").value = "true";
                        document.getElementById("AverageColorgrouptype").checked = true;
                        $("#AvgcolorTable").html("");
                        for (var i = 0; i < colorObj.Colors.length; i++) {
                            defaultAvgFlag=true;
                            addRow();
                            $("#AvgcolorCodes" + i).attr("colorCode", colorObj.Colors[i]);
                            $("#AvgcolorCodes" + i).css("background-color", colorObj.Colors[i])
                            $("select#" + i + "operatorsForAvg").attr("value", colorObj.Operators[i].replace("_", "").replace("NOT", "!"))
                            $("input#sAvgValues" + i).attr("value", colorObj.StartValues[i])
                            $("input#" + i + "AvgeValues").attr("value", colorObj.EndValues[i])
                            if (colorObj.Operators[i] == '<>') {
                                var id = i + "AvgeValues";
                                onoperatorAvg(i, '<>')
                            } else {
                                $("input#" + i + "AvgeValues").attr("value", "")
                            }
                        }
                    } else {
                        if (colorObj.isGradientBased == "true")
                        {
                            $("#gradientId").attr("checked", true)
                            $("#gradientId").val(true);
                        } else {
                            $("#gradientId").attr("checked", false)
                            $("#gradientId").val(false);
                        }
                        $("#colorTable").html("");
                        for (var i = 0; i < colorObj.Colors.length; i++) {
                            defaultAbsFlag=true;
                            addRow();
                        }
                        for (var i = 0; i < colorObj.Colors.length; i++) {
                            if (colorObj.isGradientBased == "false")
                            {
                                $("#colorCodes" + i).attr("colorCode", colorObj.Colors[i]);
                                $("#colorCodes" + i).css("background-color", colorObj.Colors[i])
                            } else {
                                $("#colorCodes" + i).val(colorObj.Colors[i]);
                                $("select#colorCodes" + i).attr("value", colorObj.Colors[i]);
                            }
                            $("select#" + i + "operators").attr("value", colorObj.Operators[i].replace("_", "").replace("NOT", "!"))
                            $("input#sValues" + i).attr("value", colorObj.StartValues[i])
                            $("input#" + i + "eValues").attr("value", colorObj.EndValues[i])
                            if (colorObj.Operators[i] == '<>') {
                                var id = i + "eValues";
                                onoperator(id, '<>')
                            } else {
                                $("input#" + i + "eValues").attr("value", "")
                            }
                        }
                    }
                }
                $('#colorpicker').farbtastic('#color');
                 //added by Ram for checking Apply color for all Parameters
                if(<%=isColorForAllViewBys%> || <%=container.isColorAppyForAllParameters()%>){
                document.getElementById("isAllParameter").checked=true;
                document.getElementById("isAllParameter").value=true;
                }
                if(<%=isColorAppyAcrossCurrentData%> || <%=container.isColorAppyAcrossCurrentData()%>){
                document.getElementById("isAcrossCurrentData").checked=true;
                document.getElementById("isAcrossCurrentData").value=true;
                }
            });
            function addRow() {
                var chkObj=document.getElementById("gradientId");
                var isavgbasedObj = document.getElementById("isAvgbased").value;
                var ispercentbasedObj = document.getElementById("isPercentbased").value;
//                alert("gredientBased "+chkObj.checked);
//                alert("isavgbasedObj "+isavgbasedObj);
//                alert("ispercentbasedObj "+ispercentbasedObj);
                if (isavgbasedObj == "true"){
                    var table = document.getElementById("AvgcolorTable");
                    var rowCount = table.rows.length;
                    var idx = rowCount;

                    var tableHtml = "";
                    tableHtml = tableHtml + "<tr id='avgrow" + idx + "'>";
                    if (chkObj.checked)
                    {
                        tableHtml = tableHtml + "<td>Apply Color as <select name=\"AvgcolorCodes\" id=\"AvgcolorCodes" + idx + "\">";
                             <%for (String Str : StrColors) {%>
                        tableHtml = tableHtml + "<option value='<%=Str%>'><%=Str%></option>";
                             <%}%>
                        tableHtml = tableHtml + "</select>When Value</td>";
                    }else{
                    tableHtml = tableHtml + " <td>Apply Color as <input type='text' name=\"AvgcolorCodes\" id=\"AvgcolorCodes" + idx + "\" style='width:50px;cursor:pointer' onclick=\"showColor(this.id)\"  colorCode='' >When Value</td>"
                    }
                    tableHtml = tableHtml + "<td><select name=\"0operatorsForAvg\" id=\"" + idx + "operatorsForAvg\" onchange=onoperatorAvg(" + idx + ",this.value)>";
                    <%for (String Str : strOperators) {%>
                    tableHtml = tableHtml + "<option value='<%=Str%>'><%=Str%></option>";
                    <%}%>
                    tableHtml = tableHtml + "</select></td>";
                    tableHtml = tableHtml + "<td><input type=\"text\" name=\"sAvgValues0\" value='' id=\"sAvgValues" + idx + "\">%</td>";
                    tableHtml = tableHtml + " <td id=\"" + idx + "AvgeValuesTd\" style=\"display:none\"><input type=\"hidden\" name=\"0AvgeValues\" value='' id=\"" + idx + "AvgeValues\">%</td></tr>";
//                    alert("isavg based html : ");
                    if (idx == 0) {
                        $('#AvgcolorTable').html('');
                        $('#AvgcolorTable').html(tableHtml);
                    } else {
                        $('#AvgcolorTable > tbody:last').append(tableHtml);
                    }

                }else if (ispercentbasedObj == "true") {
//                    alert("ispercentbasedObj*************"+ispercentbasedObj);
                    var table = document.getElementById("PercentcolorTable");
                    var rowCount = table.rows.length;
//                    var idx = rowCount;
                    var tableHtml = "";
                    for(var idx=0;idx<2;idx++){
                    tableHtml = tableHtml + "<tr id='percentrow" + idx + "'>";
                    if (chkObj.checked)
                    {
                        tableHtml = tableHtml + "<td>Apply Color as <select name=\"PercentcolorCodes\" id=\"PercentcolorCodes" + idx + "\">";
                             <%for (String Str : StrColors) {%>
                        tableHtml = tableHtml + "<option value='<%=Str%>'><%=Str%></option>";
                             <%}%>
                        tableHtml = tableHtml + "</select>When Value</td>";
                    }else{
                    tableHtml = tableHtml + " <td>Apply Color as <input type='text' name=\"PercentcolorCodes\" id=\"PercentcolorCodes" + idx + "\" style='width:50px;cursor:pointer' onclick=\"showColor(this.id)\"  colorCode='' >When Value</td>"
                    }
                    tableHtml = tableHtml + "<td><select name=\"0operatorsForPercent\" id=\"" + idx + "operatorsForPercent\" onchange=onoperatorPercent(" + idx + ",this.value)>";
                    <%//for (String Str : strOperators) {%>
                    if(idx==0)
                    tableHtml = tableHtml + "<option value=\">\">Above</option>";
                    if(idx==1)
                    tableHtml = tableHtml + "<option value=\"<\">Below</option>";
                    //tableHtml = tableHtml + "<option value='<%//=Str%>'><%//=Str%></option>";
                    <%//}%>
                    tableHtml = tableHtml + "</select></td>";
                    tableHtml = tableHtml + "<td><input type=\"text\" readonly name=\"sPercentValues0\" value='<%=NumberFormatter.getModifiedNumberColor(retObj.getColumnAverageValue(columnName), container.getNumberSymbol(columnName))%>' id=\"sPercentValues" + idx + "\"></td>";
                    tableHtml = tableHtml + " <td id=\"" + idx + "PercenteValuesTd\" style=\"display:none\"><input type=\"hidden\" name=\"0PercenteValues\" value='' id=\"" + idx + "PercenteValues\"> </td></tr>";
                    }
//                    alert(tableHtml);
//                    if (idx == 0) {
                        $('#PercentcolorTable').html('');
                        $('#PercentcolorTable').html(tableHtml);
//                    } else {
//                        $('#AvgcolorTable > tbody:last').append(tableHtml);
//                    }

                } else {
                    var table = document.getElementById("colorTable");
                    var rowCount = table.rows.length;
                    var idx = rowCount;
//                    var chkObj = document.getElementById("gradientId");
                    var tableHtml = "";

                    tableHtml = tableHtml + "<tr id='row" + idx + "'>";

                    if (chkObj.checked)
                    {

                        tableHtml = tableHtml + "<td>Apply Color as <select name=\"colorCodes\" id=\"colorCodes" + idx + "\">";
                             <%for (String Str : StrColors) {%>
                        tableHtml = tableHtml + "<option value='<%=Str%>'><%=Str%></option>";
                             <%}%>
                        tableHtml = tableHtml + "</select>When Value</td>";

                    } else {
                        tableHtml = tableHtml + " <td>Apply Color as <input type='text' name=\"colorCodes\" id=\"colorCodes" + idx + "\" style='width:50px;cursor:pointer' onclick=\"showColor(this.id)\"  colorCode='' >When Value</td>"

                    }

                    tableHtml = tableHtml + "<td><select name=\"operators\" id=\"" + idx + "operators\" onchange=onoperator(this.id,this.value)>";
                        <%for (String Str : strOperators) {%>
                    tableHtml = tableHtml + "<option value='<%=Str%>'><%=Str%></option>";
                        <%}%>
                    tableHtml = tableHtml + "</select></td>";
                    tableHtml = tableHtml + "<td><input type=\"text\" name=\"sValues\" value=\"\" id=\"sValues" + idx + "\"></td>";
                    tableHtml = tableHtml + "<td><input type=\"hidden\" name=\"eValues\" value=\"\" id=\"" + idx + "eValues\"></td></tr>";
//                    alert("rows no:"+idx);
                    if (idx == 0) {
                        $('#colorTable').html("");
                        $('#colorTable').html(tableHtml);
                    } else {
                        $('#colorTable > tbody:last').append(tableHtml);
                    }
                }
            }
            function deleteRow() {
                var isavgbasedObj = document.getElementById("isAvgbased").value;
                if (isavgbasedObj == "true") {
                    try {
                        var table = document.getElementById("AvgcolorTable");
                        var rowCount = table.rows.length;
                        if (rowCount > 1) {
                            table.deleteRow(rowCount - 1);
                        }
                    } catch (e) {
                        alert(e);
                    }
                } else {
                    try {
                        var table = document.getElementById("colorTable");
                        var rowCount = table.rows.length;

                        if (rowCount > 1) {
                            table.deleteRow(rowCount - 1);
                        }
                    } catch (e) {
                        alert(e);
                    }
                }
            }
            function savecolors(formId) {
                        if ('<%=fromModule%>' != "") {
//                alert("In Save Mode of DashBoard");
                    var colorCodeObjs = document.getElementsByName("colorCodes");
                    var chkObj = document.getElementById("gradientId");
                    if (!chkObj.checked)
                        for (var i = 0; i < colorCodeObjs.length; i++)
                        {
                            var colorCode = $("#" + colorCodeObjs[i].id).attr('colorCode')
                            colorCodeObjs[i].value = colorCode
                        }
                    $.post("<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=saveDbrColors&colmnlabelName=<%=labelName%>&reportid=<%=reportid%>&dashletId=<%=dashletId%>", $("#" + formId).serialize(),
                                        function (data) {
                                            parent.$("#applydbrcolrdiv").dialog('close');
                                            parent.resetTableData('<%=dashletId%>', '<%=flag%>')
//                    parent.document.getElementById("iframe1").src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportid%>"
//                    parent.closeapplycolr();
                                        });
                            } else {
                                var colorCodeObjs = document.getElementsByName("colorCodes");
                                var chkObj = document.getElementById("gradientId");
                                var isavgbasedObj = document.getElementById("isAvgbased").value;
                                var ispercentbasedObj = document.getElementById("isPercentbased").value;
//                                alert("ispercentbasedObj "+ispercentbasedObj);
                                if (isavgbasedObj == "true") {
                                    var AvgcolorCodeObjs = document.getElementsByName("AvgcolorCodes");
                                    for (var i = 0; i < AvgcolorCodeObjs.length; i++)
                                    {
                                        var colorCode = $("#" + AvgcolorCodeObjs[i].id).attr('colorCode');
//                        alert("colorcode is"+colorCode);
//                        alert(colorCode)
                                        AvgcolorCodeObjs[i].value = colorCode
                                    }
                                }
                               if (ispercentbasedObj == "true") {
                                    var AvgcolorCodeObjs = document.getElementsByName("PercentcolorCodes");
                                    for (var i = 0; i < AvgcolorCodeObjs.length; i++)
                                    {
                                        var colorCode = $("#" + AvgcolorCodeObjs[i].id).attr('colorCode');
//                        alert("colorcode is"+colorCode);
//                        alert(colorCode)
                                        AvgcolorCodeObjs[i].value = colorCode
                                    }
                                }
                                if (!chkObj.checked)
                                    for (var i = 0; i < colorCodeObjs.length; i++)
                                    {
                                        var colorCode = $("#" + colorCodeObjs[i].id).attr('colorCode')
//                        alert(colorCode)
                                        colorCodeObjs[i].value = colorCode
                                    }
                                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=tableChanges", $("#" + formId).serialize(),
                                        function (data) {
                                            parent.document.getElementById("iframe1").src = "<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportid%>"
                                                                    parent.closeapplycolr();
                                                                });
                                                        parent.showMap();
                                                    }
                                                }

            //Added By Ram for apply color on all viewbys
            function applyForAllParametersColors()
            {
              var x = document.getElementById("isAllParameter").checked;
              if(x){
                  document.getElementById("isAllParameter").value=true;
              }else{
                  document.getElementById("isAllParameter").value=false;
              }
            }
            function AppyColorAcrossCurrentData()
            {
              var x = document.getElementById("isAcrossCurrentData").checked;
              if(x){
                  document.getElementById("isAcrossCurrentData").value=true;
              }else{
                  document.getElementById("isAcrossCurrentData").value=false;
              }
            }
            //Endded By Ram
            function displayGradientColors()
            {
                var chkObj = document.getElementById("gradientId");
                var isavgbasedObj = document.getElementById("isAvgbased").value;
                var ispercentbasedObj = document.getElementById("isPercentbased").value;
                var rowCount = 0;
//                alert("gradient.checked "+chkObj.checked);
//                alert("isavgbasedObj dg "+isavgbasedObj);
//                alert("ispercentbasedObj dg"+ispercentbasedObj);
                if (isavgbasedObj == "true") {
                    try {
                        
                        var table = document.getElementById("AvgcolorTable");
                        rowCount = table.rows.length;
//                        alert("defaultAvgFlag "+defaultAvgFlag)
                        if(defaultAvgFlag!=true)
                         $("#AvgcolorTable").html("");
                    } catch (e) {
                        alert(e);
                    }
                }else if (ispercentbasedObj == "true") {
                    try {
                        var table = document.getElementById("PercentcolorTable");
                        rowCount = table.rows.length;
                    } catch (e) {
                        alert(e);
                    }
                }else{
                    var tableObj = document.getElementById("colorTable")
                    rowCount = tableObj.rows.length;
//                    alert("defaultAbsFlag "+defaultAbsFlag)
                    if(defaultAbsFlag!=true)
                    $("#colorTable").html("");
                }
//                alert("rowCount "+rowCount);
//                alert("defaultAbsFlag "+defaultAbsFlag)
//                alert("defaultAvgFlag "+defaultAvgFlag)
//                var html = "";
                if (chkObj.checked)
                {
                    chkObj.value = true;
//                    for (var i = 0; i < rowCount; i++)
                        if(defaultAbsFlag==true && isavgbasedObj=="false"){
                            
                        }else if(defaultAvgFlag==true && isavgbasedObj=="true"){
                            
                        }else{
                        addRow();
                        }
                }else {
                    chkObj.value = false;
//                    for (var i = 0; i < rowCount; i++)
                        if(defaultAbsFlag==true && isavgbasedObj=="false"){
                            
                        }else if(defaultAvgFlag==true && isavgbasedObj=="true"){
                            
                        }else{
                        addRow();
                        }
                }
            }
            function ChangeColordodetype(colorcodetype) {
//              var Colorcodetype= document.getElementById("Colorgrouptype").value;
//              alert("value is"+colorcodetype);
                if (colorcodetype == "obsulute") {
                    document.getElementById("colorTable").style.display = "";
                    document.getElementById("AvgcolorTable").style.display = "none";
                    document.getElementById("PercentcolorTable").style.display = "none";
                    document.getElementById("addrow").style.display = "";
                    document.getElementById("deleterow").style.display = "";
                    document.getElementById("isAvgbased").value = "false";
                    document.getElementById("isPercentbased").value = "false";
                    document.getElementById("isMinMaxbased").value = "false";
                    displayGradientColors();
                } else if (colorcodetype == "Average") {
                    document.getElementById("colorTable").style.display = "none";
                    document.getElementById("PercentcolorTable").style.display = "none";
                    document.getElementById("AvgcolorTable").style.display = "";
                    document.getElementById("addrow").style.display = "";
                    document.getElementById("deleterow").style.display = "";
                    document.getElementById("isAvgbased").value = "true";
                    document.getElementById("isPercentbased").value = "false";
                    document.getElementById("isMinMaxbased").value = "false";
                    var chkObj = document.getElementById("isAvgbased");
//                    alert("isAvgbased "+chkObj.checked);
                        displayGradientColors();
//                    }
                } else if (colorcodetype == "minmax") {
                    document.getElementById("colorTable").style.display = "none";
                    document.getElementById("PercentcolorTable").style.display = "none";
                    document.getElementById("AvgcolorTable").style.display = "none";
                    document.getElementById("addrow").style.display = "";
                    document.getElementById("deleterow").style.display = "";
                    document.getElementById("isAvgbased").value = "false";
                    document.getElementById("isPercentbased").value = "false";
                    document.getElementById("isMinMaxbased").value = "true";
                
                } else if (colorcodetype == "Percentage") {
                    document.getElementById("colorTable").style.display = "none";
                    document.getElementById("AvgcolorTable").style.display = "none";
                    document.getElementById("PercentcolorTable").style.display = "";
                    document.getElementById("addrow").style.display = "none";
                    document.getElementById("deleterow").style.display = "none";
                    document.getElementById("isAvgbased").value = "false";
                    document.getElementById("isPercentbased").value = "true";
                    document.getElementById("isMinMaxbased").value = "false";
                    var chkObj = document.getElementById("isPercentbased");
//                    alert("percent  "+chkObj.checked)
                    displayGradientColors();
                }
            }
        </script>
    </body>
</html>
