<%--
    Document   : PbTableProperties
    Created on : Dec 2, 2009, 5:14:51 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.Container,prg.db.PbDb,java.util.HashMap,java.util.ArrayList"%>
<%              //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            HashMap map = null;
            Container container = null;
            //HashMap TableHashMap = null;
            HashMap TableProperties = null;
            ArrayList alist = null;
            String reportId = null;
            String tableDisplayRows = "10";
            String defaultSortedCol = null;
            String chkTtlValues = null;
            String chkSubTtlValues = null;
            String chkAvgValues = null;
            String selSymbolsValues = null;

            String chkOvrAllMaxValues = null;
            String chkOvrAllMinValues = null;
            String chkCatMaxValues = null;
            String chkCatMinValues = null;

            ArrayList REP = null;
            ArrayList REPNames = null;

            ArrayList CEP = null;
            ArrayList CEPNames = null;

            ArrayList Measures = null;
            ArrayList MeasuresNames = null;

            ArrayList displayColumns = new ArrayList();
            ArrayList displayLabels = new ArrayList();
            String[] tabSymbols = {"", "$", "Rs", "€", "¥ ", "%"};//dollar,inr,euro,yen and percent
            String[] tabSymbolsDisp = {"Select", "$", "Rs", "€", "¥ ", "%"};//dollar,inr,euro,yen and percent
            String[] displayTableRows = {"10", "5", "15", "25", "50", "All"};

            boolean showExtraTabs = true;
            String ctsPath=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Table Properties</title>
        <link type="text/css" href="<%=ctsPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=ctsPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        
        <style type="text/css">
            *{font : 11px verdana}
            th{background-color:#b4d9ee;padding:4px}
        </style>
    </head>
    <%

            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");
                reportId = request.getParameter("REPORTID");
                ArrayList singleColPropList = new ArrayList();

                HashMap columnProperties = new HashMap();
                if (map.get(reportId) != null) {
                    container = (Container) map.get(reportId);
                    REP = (ArrayList) container.getTableHashMap().get("REP");
                    if (container.getTableHashMap().get("CEP") != null) {
                        CEP = (ArrayList) container.getTableHashMap().get("CEP");
                    } else {
                        CEP = new ArrayList();
                    }
                    REPNames = (ArrayList) container.getTableHashMap().get("REPNames");//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com
                    Measures = (ArrayList) container.getTableHashMap().get("Measures");
                    MeasuresNames = (ArrayList) container.getTableHashMap().get("MeasuresNames");//newly added on 15-10-2009 by santhosh.kumar@progenbusiness.com

                    displayColumns = (ArrayList) REP.clone();
                    displayLabels = (ArrayList) REPNames.clone();
                    if (container.getTableHashMap() != null) {
                        if (container.getTableHashMap().get("TableProperties") != null) {
                            TableProperties = (HashMap) container.getTableHashMap().get("TableProperties");

                            defaultSortedCol = String.valueOf(TableProperties.get("DefaultSortedColumn"));
                            chkTtlValues = (String) TableProperties.get("ShowTotalValues");
                            chkSubTtlValues = (String) TableProperties.get("ShowSubTotalValues");
                            chkAvgValues = (String) TableProperties.get("ShowAvgValues");
                            selSymbolsValues = (String) TableProperties.get("ColumnSymbols");
                            chkOvrAllMinValues = (String) TableProperties.get("ShowOvrAllMinValues");
                            chkOvrAllMaxValues = (String) TableProperties.get("ShowOvrAllMaxValues");
                            chkCatMinValues = (String) TableProperties.get("ShowCatMinValues");
                            chkCatMaxValues = (String) TableProperties.get("ShowCatMaxValues");
                            //columnProperties = (HashMap) TableProperties.get("ColumnProperties");
                            columnProperties = container.getColumnProperties();
                            tableDisplayRows = String.valueOf(TableProperties.get("tableDisplayRows"));
                        }
                        for (int i = 0; i < Measures.size(); i++) {
                            displayColumns.add(String.valueOf(Measures.get(i)));
                            displayLabels.add(String.valueOf(MeasuresNames.get(i)));
                        }%>

    <body>
        <form name="tabProForm" action="" method="Post">
            <input type="hidden" name="grdTtlAy" id="grdTtlAy" >
            <input type="hidden" name="subTtlAy" id="subTtlAy" >
            <input type="hidden" name="avgAy" id="avgAy" >
            <input type="hidden" name="maxAy" id="maxAy" >
            <input type="hidden" name="minAy" id="minAy" >
            <input type="hidden" name="catMaxAy" id="catMaxAy" >
            <input type="hidden" name="catMinAy" id="catMinAy" >
            <input type="hidden" name="syblAy" id="syblAy" >
            <%--  <input type="hidden" name="disprowAy" id="disprowAy" value="">--%>

            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="100%">
                        <table width="60%">
                            <tr>
                                <td align="left" valign="top">Default Sorted Column</td>
                                <td align="left" valign="top">
                                    <select name="dftSortCol" id="">
                                        <%if (CEP.size() == 0) {
                            for (int i = 0; i < displayColumns.size(); i++) {
                                if (defaultSortedCol != null && defaultSortedCol.equalsIgnoreCase(String.valueOf(displayColumns.get(i)))) {%>
                                        <option selected value="<%=displayColumns.get(i)%>"><%=displayLabels.get(i)%></option>
                                        <%} else {%>
                                        <option value="<%=displayColumns.get(i)%>"><%=displayLabels.get(i)%></option>
                                        <%}
    }
} else {
    for (int i = 0; i < REP.size(); i++) {
        if (defaultSortedCol != null && defaultSortedCol.equalsIgnoreCase(String.valueOf(displayColumns.get(i)))) {%>
                                        <option selected value="<%=displayColumns.get(i)%>"><%=displayLabels.get(i)%></option>
                                        <%} else {%>
                                        <option value="<%=displayColumns.get(i)%>"><%=displayLabels.get(i)%></option>
                                        <%}
                            }
                        }%>
                                    </select>
                                </td>

                                <Td>Display Rows</Td>
                                <Td>
                                    <select name="TableDisplayRows" id="TableDisplayRows"  style="width:150px">
                                        <%
                        for (int i = 0; i < displayTableRows.length; i++) {%>
                                        <%  if (tableDisplayRows.equalsIgnoreCase(displayTableRows[i])) {%>
                                        <option selected value="<%=displayTableRows[i]%>"><%=displayTableRows[i]%></option>
                                        <%} else {%>
                                        <option value="<%=displayTableRows[i]%>"><%=displayTableRows[i]%></option>
                                        <%}
                        }%>
                                    </select>
                                </Td>
                            </tr>
                        </table>
                    </td>

                </tr>
                <tr>
                    <td style="height:10px"></td>
                </tr>
                <tr>
                    <td>
                        <table class="tablesorter" cellspacing="1" cellpadding="0" width="100%">
                            <tr>
                                <th align="left" valign="top">Column Name</th>
                                <th align="left" valign="top">
                                    <select name="selSymbols" id="selSymbols" onchange="applyTableDisplayRows();">
                                        <%for (int i = 0; i < tabSymbolsDisp.length; i++) {
                            if (selSymbolsValues != null && selSymbolsValues.equalsIgnoreCase(tabSymbols[i])) {%>
                                        <option selected value="<%=tabSymbols[i]%>"><%=tabSymbolsDisp[i]%></option>
                                        <%} else {%>
                                        <option value="<%=tabSymbols[i]%>"><%=tabSymbolsDisp[i]%></option>
                                        <%}
                        }%>
                                    </select>Symbol
                                </th>
                                <th align="left" valign="top">
                                    <%if (chkTtlValues != null && chkTtlValues.equalsIgnoreCase("Y")) {%>
                                    <input type="checkbox" checked name="chkTtl"  value="Y"  onclick="chkTotal();">
                                    <%} else {%>
                                    <input type="checkbox" name="chkTtl"  value="N"   onclick="chkTotal();">
                                    <%}%>Total
                                </th>
                                <th align="left" valign="top">
                                    <%if (chkSubTtlValues != null && chkSubTtlValues.equalsIgnoreCase("Y")) {%>
                                    <input type="checkbox" checked name="chkSubTtl"  value="Y" onclick="chkSubTotal();">
                                    <%} else {%>
                                    <input type="checkbox" name="chkSubTtl"  value="N" onclick="chkSubTotal();">
                                    <%}%>Sub Total
                                </th>
                                <th align="left" valign="top">
                                    <%if (chkAvgValues != null && chkAvgValues.equalsIgnoreCase("Y")) {%>
                                    <input type="checkbox" checked name="chkAvg"  value="Y" onclick="chkAverage();">
                                    <%} else {%>
                                    <input type="checkbox" name="chkAvg"  value="N" onclick="chkAverage();">
                                    <%}%>Avg
                                </th>
                                <%if (showExtraTabs) {%>
                                <th align="left" valign="top">
                                    <%if (chkOvrAllMaxValues != null && chkOvrAllMaxValues.equalsIgnoreCase("Y")) {%>
                                    <input type="checkbox" checked name="chkOvrAllMaxValues"  value="Y"  onclick="chkOverAllMaximumValues();">
                                    <%} else {%>
                                    <input type="checkbox" name="chkOvrAllMaxValues"  value="N"   onclick="chkOverAllMaximumValues();">
                                    <%}%>Max
                                </th>
                                <th align="left" valign="top">
                                    <%if (chkOvrAllMinValues != null && chkOvrAllMinValues.equalsIgnoreCase("Y")) {%>
                                    <input type="checkbox" checked name="chkOvrAllMinValues"  value="Y" onclick="chkOverAllMinimumValues();">
                                    <%} else {%>
                                    <input type="checkbox" name="chkOvrAllMinValues"  value="N" onclick="chkOverAllMinimumValues();">
                                    <%}%>Min
                                </th>
                                <th align="left" valign="top">
                                    <%if (chkCatMaxValues != null && chkCatMaxValues.equalsIgnoreCase("Y")) {%>
                                    <input type="checkbox" checked name="chkCatMaxValues"  value="Y"  onclick="chkCategoryMaximumValues();">
                                    <%} else {%>
                                    <input type="checkbox" name="chkCatMaxValues"  value="N"   onclick="chkCategoryMaximumValues();">
                                    <%}%>Cat Max
                                </th>
                                <th align="left" valign="top">
                                    <%if (chkCatMinValues != null && chkCatMinValues.equalsIgnoreCase("Y")) {%>
                                    <input type="checkbox" checked name="chkCatMinValues"  value="Y" onclick="chkCategoryMinimumValues();">
                                    <%} else {%>
                                    <input type="checkbox" name="chkCatMinValues"  value="N" onclick="chkCategoryMinimumValues();">
                                    <%}%>Cat Min
                                </th>
                                <%}%>
                            </tr>
                            <%for (int i = REP.size(); i < displayColumns.size(); i++) {
                            if (columnProperties != null) {
                                singleColPropList = (ArrayList) columnProperties.get("A_" + String.valueOf(displayColumns.get(i)));
                                if (singleColPropList == null) {
                                    singleColPropList = new ArrayList();
                                }
                            }%>
                            <tr>
                                <td align="left" valign="top"><%=displayLabels.get(i)%></td>
                                <td align="left" valign="top">
                                    <select name="selSymbols"  onchange="applySymbolSingle(this);">
                                        <%for (int j = 0; j < tabSymbolsDisp.length; j++) {
        if (singleColPropList.size() != 0 && singleColPropList.get(7) != null && singleColPropList.get(7).toString().equalsIgnoreCase(tabSymbols[j])) {%>
                                        <option selected value="<%=tabSymbols[j]%>"><%=tabSymbolsDisp[j]%></option>
                                        <%} else {%>
                                        <option value="<%=tabSymbols[j]%>"><%=tabSymbolsDisp[j]%></option>
                                        <%}
    }%>
                                    </select>
                                </td>
                                <td align="left" valign="top">
                                    <%if (singleColPropList.size() != 0 && singleColPropList.get(0) != null && singleColPropList.get(0).toString().equalsIgnoreCase("Y")) {%>
                                    <input checked type="checkbox" name="chkTtl"  value="Y" onclick="chkTotalSingle(this);">
                                    <%} else {%>
                                    <input type="checkbox" name="chkTtl" value="N" onclick="chkTotalSingle(this);">
                                    <%}%>
                                </td>
                                <td align="left" valign="top">
                                    <%if (singleColPropList.size() != 0 && singleColPropList.get(1) != null && singleColPropList.get(1).toString().equalsIgnoreCase("Y")) {%>
                                    <input checked type="checkbox" name="chkSubTtl"  value="Y" onclick="chkSubTotalSingle(this);">
                                    <%} else {%>
                                    <input type="checkbox" name="chkSubTtl"  value="N" onclick="chkSubTotalSingle(this);">
                                    <%}%>
                                </td>
                                <td align="left" valign="top">
                                    <%if (singleColPropList.size() != 0 && singleColPropList.get(2) != null && singleColPropList.get(2).toString().equalsIgnoreCase("Y")) {%>
                                    <input checked type="checkbox" name="chkAvg"  value="Y" onclick="chkAverageSingle(this);">
                                    <%} else {%>
                                    <input type="checkbox" name="chkAvg"  value="N" onclick="chkAverageSingle(this);">
                                    <%}%>
                                </td>
                                <%if (showExtraTabs) {%>
                                <td align="left" valign="top">
                                    <%if (singleColPropList.size() != 0 && singleColPropList.get(3) != null && singleColPropList.get(3).toString().equalsIgnoreCase("Y")) {%>
                                    <input checked type="checkbox" name="chkOvrAllMaxValues"  value="Y" onclick="chkOverAllMaximumValuesSingle(this);">
                                    <%} else {%>
                                    <input type="checkbox" name="chkOvrAllMaxValues"  value="N" onclick="chkOverAllMaximumValuesSingle(this);">
                                    <%}%>
                                </td>
                                <td align="left" valign="top">
                                    <%if (singleColPropList.size() != 0 && singleColPropList.get(4) != null && singleColPropList.get(4).toString().equalsIgnoreCase("Y")) {%>
                                    <input checked type="checkbox" name="chkOvrAllMinValues"  value="Y" onclick="chkOverAllMinimumValuesSingle(this);">
                                    <%} else {%>
                                    <input type="checkbox" name="chkOvrAllMinValues"  value="N" onclick="chkOverAllMinimumValuesSingle(this);">
                                    <%}%>
                                </td>

                                <td align="left" valign="top">
                                    <%if (singleColPropList.size() != 0 && singleColPropList.get(5) != null && singleColPropList.get(5).toString().equalsIgnoreCase("Y")) {%>
                                    <input checked type="checkbox" name="chkCatMaxValues"  value="Y" onclick="chkCategoryMaximumValuesSingle(this);">
                                    <%} else {%>
                                    <input type="checkbox" name="chkCatMaxValues"  value="N" onclick="chkCategoryMaximumValuesSingle(this);">
                                    <%}%>
                                </td>
                                <td align="left" valign="top">
                                    <%if (singleColPropList.size() != 0 && singleColPropList.get(6) != null && singleColPropList.get(6).toString().equalsIgnoreCase("Y")) {%>
                                    <input checked type="checkbox" name="chkCatMinValues"  value="Y" onclick="chkCategoryMinimumValuesSingle(this);">
                                    <%} else {%>
                                    <input type="checkbox" name="chkCatMinValues"  value="N" onclick="chkCategoryMinimumValuesSingle();">
                                    <%}%>
                                </td>
                                <%}%>
                            </tr>
                            <%}%>
                        </table>
                    </td>
                </tr>
            </table>
            <br/>
            <table width="100%">
                <tr>
                    <td align="center">
                        <input type="button" name="Save" value="Done" class="navtitle-hover" onclick="goSave('<%=reportId%>')">
                    </td>
                </tr>
            </table>
        </form>
                    <script type="text/javascript">
            function chkTotal(){
                var chkTtlObj=document.getElementsByName("chkTtl");
                if(chkTtlObj!=null){
                    if(chkTtlObj[0].checked){
                        chkTtlObj[0].value='Y'
                        for(var i=1;i<chkTtlObj.length;i++){
                            chkTtlObj[i].checked=true;
                            chkTtlObj[i].value='Y'
                        }
                    }else{
                        chkTtlObj[0].value='N'
                        for(var i=1;i<chkTtlObj.length;i++){
                            chkTtlObj[i].checked=false;
                            chkTtlObj[i].value='N'
                        }
                    }
                }
            }
            function chkSubTotal(){
                var chkSubTtlObj=document.getElementsByName("chkSubTtl");
                if(chkSubTtlObj!=null && chkSubTtlObj!=undefined){
                    if(chkSubTtlObj[0].checked){
                        chkSubTtlObj[0].value='Y'
                        for(var i=1;i<chkSubTtlObj.length;i++){
                            chkSubTtlObj[i].checked=true;
                            chkSubTtlObj[i].value='Y'
                        }
                    }else{
                        chkSubTtlObj[0].value='N'
                        for(var i=1;i<chkSubTtlObj.length;i++){
                            chkSubTtlObj[i].checked=false;
                            chkSubTtlObj[i].value='N'
                        }
                    }
                }

            }
            function chkAverage(){
                var chkAvgObj=document.getElementsByName("chkAvg");
                if(chkAvgObj!=null && chkAvgObj!=undefined){
                    if(chkAvgObj[0].checked){
                        chkAvgObj[0].value='Y'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=true;
                            chkAvgObj[i].value='Y'
                        }
                    }else{
                        chkAvgObj[0].value='N'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=false;
                            chkAvgObj[i].value='N'
                        }
                    }
                }
            }
            function chkOverAllMaximumValues(){
                var chkAvgObj=document.getElementsByName("chkOvrAllMaxValues");
                if(chkAvgObj!=null && chkAvgObj!=undefined){
                    if(chkAvgObj[0].checked){
                        chkAvgObj[0].value='Y'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=true;
                            chkAvgObj[i].value='Y'
                        }
                    }else{
                        chkAvgObj[0].value='N'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=false;
                            chkAvgObj[i].value='N'
                        }
                    }
                }
            }
            function chkOverAllMinimumValues(){
                var chkAvgObj=document.getElementsByName("chkOvrAllMinValues");
                if(chkAvgObj!=null && chkAvgObj!=undefined){
                    if(chkAvgObj[0].checked){
                        chkAvgObj[0].value='Y'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=true;
                            chkAvgObj[i].value='Y'
                        }
                    }else{
                        chkAvgObj[0].value='N'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=false;
                            chkAvgObj[i].value='N'
                        }
                    }
                }
            }
            function chkCategoryMaximumValues(){
                var chkAvgObj=document.getElementsByName("chkCatMaxValues");
                if(chkAvgObj!=null && chkAvgObj!=undefined){
                    if(chkAvgObj[0].checked){
                        chkAvgObj[0].value='Y'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=true;
                            chkAvgObj[i].value='Y'
                        }
                    }else{
                        chkAvgObj[0].value='N'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=false;
                            chkAvgObj[i].value='N'
                        }
                    }
                }
            }
            function chkCategoryMinimumValues(){
                var chkAvgObj=document.getElementsByName("chkCatMinValues");
                if(chkAvgObj!=null && chkAvgObj!=undefined){
                    if(chkAvgObj[0].checked){
                        chkAvgObj[0].value='Y'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=true;
                            chkAvgObj[i].value='Y'
                        }
                    }else{
                        chkAvgObj[0].value='N'
                        for(var i=1;i<chkAvgObj.length;i++){
                            chkAvgObj[i].checked=false;
                            chkAvgObj[i].value='N'
                        }
                    }
                }
            }
            function applySymbol(){
                var chkAvgObj=document.getElementsByName("selSymbols");                
                if(chkAvgObj!=null && chkAvgObj!=undefined){
                    for(var i=1;i<chkAvgObj.length;i++){
                        chkAvgObj[i].selectedIndex=chkAvgObj[0].selectedIndex
                    }
                    
                }
            }
            function goSave(reportId){
                var chkTtlObj=document.getElementsByName("chkTtl");
                var chkSubTtlObj=document.getElementsByName("chkSubTtl");
                var chkAvgObj=document.getElementsByName("chkAvg");               
                var chkOvrAllMaxValuesObj=document.getElementsByName("chkOvrAllMaxValues");
                var chkOvrAllMinValuesObj=document.getElementsByName("chkOvrAllMinValues");
                var chkCatMaxValuesObj=document.getElementsByName("chkCatMaxValues");
                var chkCatMinValuesObj=document.getElementsByName("chkCatMinValues");
                var selSymbolsObj=document.getElementsByName("selSymbols");
                //var/ TableDisplayRows=document.getElementsByName("TableDisplayRows");

                var grdTtlAy=new Array();
                var subTtlAy=new Array();
                var avgAy=new Array();
                var maxAy=new Array();
                var minAy=new Array();
                var catMaxAy=new Array();
                var catMinAy=new Array();
                var syblAy=new Array();
                // var disprowAy=new Array();

                for(var i=0;i<chkTtlObj.length;i++){
                    grdTtlAy.push(chkTtlObj[i].value);
                    subTtlAy.push(chkSubTtlObj[i].value);
                    avgAy.push(chkAvgObj[i].value);
                    if(chkOvrAllMaxValuesObj[i]=='undefined'){
                        maxAy.push(chkOvrAllMaxValuesObj[i].value);
                        minAy.push(chkOvrAllMinValuesObj[i].value);
                        catMaxAy.push(chkCatMaxValuesObj[i].value);
                        catMinAy.push(chkCatMinValuesObj[i].value);
                    }else if(chkOvrAllMaxValuesObj[i]!='undefined'){
                        maxAy.push(chkOvrAllMaxValuesObj[i].value);
                        minAy.push(chkOvrAllMinValuesObj[i].value);
                        catMaxAy.push(chkCatMaxValuesObj[i].value);
                        catMinAy.push(chkCatMinValuesObj[i].value);
                    }
                    syblAy.push(selSymbolsObj[i].value);
                    // disprowAy.push(selSymbolsObj[i].value);
                    /*
                     alert("chkTtlObj["+i+"].value is "+chkTtlObj[i].value);
                    alert("chkSubTtlObj["+i+"].value is "+chkSubTtlObj[i].value)
                    alert("chkAvgObj["+i+"].value is "+chkAvgObj[i].value);
                    alert("chkCatMaxValuesObj["+i+"].value is "+chkCatMaxValuesObj[i].value)
                    alert("chkCatMinValuesObj["+i+"].value is "+chkCatMinValuesObj[i].value)
                    alert("chkOvrAllMaxValuesObj["+i+"].value is "+chkOvrAllMaxValuesObj[i].value);
                    alert("chkOvrAllMinValuesObj["+i+"].value is "+chkOvrAllMinValuesObj[i].value);
                    alert("selSymbolsObj["+i+"].value is "+selSymbolsObj[i].value)
                     */
                }
                if(grdTtlAy.length>0){
                    document.getElementById("grdTtlAy").value=grdTtlAy.toString();
                }if(subTtlAy.length>0){
                    document.getElementById("subTtlAy").value=subTtlAy.toString();
                }if(avgAy.length>0){
                    document.getElementById("avgAy").value=avgAy.toString();
                }if(maxAy.length>0){
                    document.getElementById("maxAy").value=maxAy.toString();
                }if(minAy.length>0){
                    document.getElementById("minAy").value=minAy.toString();
                }if(catMaxAy.length>0){
                    document.getElementById("catMaxAy").value=catMaxAy.toString();
                }if(catMinAy.length>0){
                    document.getElementById("catMinAy").value=catMinAy.toString();
                }if(syblAy.length>0){
                    document.getElementById("syblAy").value=syblAy.toString();
                }
                // document.getElementById("disprowAy").value=syblAy.toString()

                /*
                 alert(document.getElementById("grdTtlAy").value);
                alert(document.getElementById("subTtlAy").value);
                alert(document.getElementById("avgAy").value);
                alert(document.getElementById("maxAy").value);
                alert(document.getElementById("minAy").value);
                alert(document.getElementById("catMaxAy").value);
                alert(document.getElementById("catMinAy").value);
                alert(document.getElementById("syblAy").value);
                 */

                document.forms.tabProForm.action="<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=buildTable&buildTableChange=TableProperties&REPORTID="+reportId;
                document.forms.tabProForm.submit();
                parent.PreviewTable();
                parent.cancelTableProperties();

            <%-- $.ajax({
                url: "<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=buildTable&buildTableChange=TableProperties",
                success: function(data){
                    parentDiv.innerHTML=data;
                }
            });--%>

                }

                function goCancel(){
                    // parent.document.getElementById("dispTabProp").style.display='none';
                    // parent.document.getElementById("dispTabPropFrame").style.display="none"
                    // parent.document.getElementById('fade').style.display='none';
                    $("#dispTabProp").dialog('close');
                }


                ////////////////////////
                function chkTotalSingle(Obj){
                    var chkTtlObj=document.getElementsByName("chkTtl");
                    if(Obj.checked){
                        chkTtlObj[0].checked=Obj.checked;
                        chkTtlObj[0].value='Y';
                        Obj.value='Y';
                    }else{
                        Obj.value='N';
                    }
                }
                function chkSubTotalSingle(Obj){
                    var chkSubTtlObj=document.getElementsByName("chkSubTtl");
                    if(Obj.checked){
                        chkSubTtlObj[0].checked=Obj.checked;
                        chkSubTtlObj[0].value='Y';
                        Obj.value='Y';
                    }else{
                        Obj.value='N';
                    }
                }
                function chkAverageSingle(Obj){
                    var chkAvgObj=document.getElementsByName("chkAvg");
                    if(Obj.checked){
                        chkAvgObj[0].checked=Obj.checked;
                        chkAvgObj[0].value='Y';
                        Obj.value='Y';
                    }else{
                        Obj.value='N';
                    }
                }
                function chkOverAllMaximumValuesSingle(Obj){
                    var chkAvgObj=document.getElementsByName("chkOvrAllMaxValues");
                    if(Obj.checked){
                        chkAvgObj[0].checked=Obj.checked;
                        chkAvgObj[0].value='Y';
                        Obj.value='Y';
                    }else{
                        Obj.value='N';
                    }
                }
                function chkOverAllMinimumValuesSingle(Obj){
                    var chkAvgObj=document.getElementsByName("chkOvrAllMinValues");
                    if(Obj.checked){
                        chkAvgObj[0].checked=Obj.checked;
                        chkAvgObj[0].value='Y';
                        Obj.value='Y';
                    }else{
                        Obj.value='N';
                    }
                }
                function chkCategoryMaximumValuesSingle(Obj){
                    var chkAvgObj=document.getElementsByName("chkCatMaxValues");
                    if(Obj.checked){
                        chkAvgObj[0].checked=Obj.checked;
                        chkAvgObj[0].value='Y';
                        Obj.value='Y';
                    }else{
                        Obj.value='N';
                    }
                }
                function chkCategoryMinimumValuesSingle(Obj){
                    var chkAvgObj=document.getElementsByName("chkCatMinValues");
                    if(Obj.checked){
                        chkAvgObj[0].checked=Obj.checked;
                        chkAvgObj[0].value='Y';
                        Obj.value='Y';
                    }else{
                        Obj.value='N';
                    }
                }
                function applySymbolSingle(Obj){
                    var chkAvgObj=document.getElementsByName("selSymbols");
                    if(Obj.selectedIndex!=""){
                        if(chkAvgObj[0].selectedIndex==""){
                            chkAvgObj[0].selectedIndex=Obj.selectedIndex;
                        }
                    }
                }
                /*  function applyTableDisplayRows(Obj){
                    var chkAvgObj=document.getElementsByName("TableDisplayRows");
                    if(Obj.selectedIndex!=""){
                        if(chkAvgObj[0].selectedIndex==""){
                            chkAvgObj[0].selectedIndex=Obj.selectedIndex;
                        }
                    }
                }*/
                //////////////////////
        </script>
    </body>
    <%}
                }
            } else {
            }
    %>
</html>
