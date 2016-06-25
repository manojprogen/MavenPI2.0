<%--
    Document   : PbColumnProperties
    Created on : Jan 16, 2010, 12:04:54 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.HashMap,java.util.Set,java.util.ArrayList,prg.db.PbReturnObject,prg.db.Container"%>



<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            Container container = null;
            String reportId = request.getParameter("reportId");
            String colmnname = request.getParameter("colmnname");
            String dispcolmname = request.getParameter("dispcolmname");
            boolean showExtraTabs = true;
            String[] NbrFormats = {"", "Nf", "K", "M","L","Cr"};
            String[] NbrFormatsDisp = {"Absolute", "NoFormat", "Thousands(K)", "Millions(M)","Lakhs(L)","Crores(C)"};
            String[] graphSymbols = {" ", " $ ", " Rs ", " Euro ", " Yen ","%","AED"};//dollar,inr,euro,yen,Aed and percent
            String[] indicatorFormats = {"None","Simple Indicator","Script Indicator"};
            String[] dataAlignments = {"Center","Right","Left"};
            String[] measureAlignments = {"Center","Right","Left"};
            String[] measureTypes = {"Standard","Non-Standard"};
            String[] averagecalculationtype = {"Include 0","Exclude 0"};
            String[] dateFormat={"--SELECT--","DD-MM-YY","DD-MMM-YY","MM-DD-YY","MMM-DD-YY","MM-DD-YYYY","dd-MMM-YYYY"};//changed by sruthi to add date format
            String[] dateFormatValues={"","dd-MM-yy","dd-MMM-yy","MM-dd-yy","MMM-dd-yy","MM-dd-yyyy","dd-MMM-YYYY"};//changed by sruthi to add date format
            HashMap TableHashMap = null;
           // HashMap TableProperties = null;
            HashMap ColumnProperties = null;
            ArrayList singleColProp = null;
             ArrayList singleColProp1 = null;
            HashMap NFMap = null;
            String showIndicators = "N";
            String showTotal = "N";
            String showSubTotal = "N";
            String avgTotal = "N";
            String overAllMax = "N";
            String overAllMin = "N";
            String catMax = "N";
            String catMin = "N";
            String colSymbol = " ";
            String colIndicator = "";
            String dataAlign = "";
            String measureAlign = "";
            String measureType =null;
            String NbrFormat = "";
            String subTotalDeviation = "N";
              String numberformate="Y";//added by sruthi for numberformate
            String grandTotDis="";
            String subTotDis="";
            String catMinDis="";
            String catMaxDis="";
            String overMinDis="";
            String overMaxDis="";
            String avgDis="";
            String subTotalDeviationDis="";

            String grandTotCol="";
            String subTotCol="";
            String catMinCol="";
            String catMaxCol="";
            String overMinCol="";
            String overMaxCol="";
            String avgCol="";
            String subTotalDeviationCol="";

            String measure1="";
            String colIndicator1="";
            String colIndicator12="";
            String gtCTAvgType="";
            String gtCTcolAvgType="";

            if (reportId != null && colmnname != null) {
                if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                    container = (Container) map.get(reportId);
                    TableHashMap = container.getTableHashMap();
                    HashMap<String, String> crosstabMeasureId=((PbReturnObject)container.getRetObj()).crosstabMeasureId;
                    NFMap = (TableHashMap.get("NFMap") == null) ? new HashMap() : (HashMap) TableHashMap.get("NFMap");

                    colIndicator1=container.getscriptIndicator(colmnname);
                  //  
                    boolean colIndicator11=container.isIndicatorEnabled(colmnname);
                    colIndicator12 =String.valueOf(colIndicator11);// By Prabal
//                     colIndicator12 = new Boolean(colIndicator11).toString();
                    if(colIndicator1!=null){
                        if(colIndicator1.equalsIgnoreCase("Y")){
                            colIndicator="Script Indicator";
                        }

                        else if(colIndicator12.equalsIgnoreCase("true")){
                            colIndicator="Simple Indicator";
                        }
                        }
                        else{
                            colIndicator="None";
                        }
                    measure1=container.getmeasureType(colmnname);
                    if(measure1!=null){
                        if(measure1.equalsIgnoreCase("Non-Standard")){
                        measureType="Non-Standard";
                        }
                        else if(measure1.equalsIgnoreCase("Standard")){
                            measureType="Standard";
                        }
                        else{
                        measureType="Standard";
                        }
                        }
                     else{
                        measureType="Standard";
                        }

                   // TableProperties = (TableHashMap.get("TableProperties") == null) ? new HashMap() : (HashMap) TableHashMap.get("TableProperties");
                    ColumnProperties = (container.getColumnProperties() == null) ? new HashMap() : container.getColumnProperties();
                    if(container.isReportCrosstab()){
                        if(crosstabMeasureId!=null && crosstabMeasureId.containsKey(colmnname))
                    singleColProp = (ArrayList) ColumnProperties.get(crosstabMeasureId.get(colmnname));
                        else
                             singleColProp = (ArrayList) ColumnProperties.get(colmnname);
                                      } else
                    singleColProp = (ArrayList) ColumnProperties.get(colmnname);
                    if (singleColProp != null) {
                        showTotal = String.valueOf(singleColProp.get(0));
                        showSubTotal = String.valueOf(singleColProp.get(1));
                        avgTotal = String.valueOf(singleColProp.get(2));
                        overAllMax = String.valueOf(singleColProp.get(3));
                        overAllMin = String.valueOf(singleColProp.get(4));
                        catMax = String.valueOf(singleColProp.get(5));
                        catMin = String.valueOf(singleColProp.get(6));
                        colSymbol = String.valueOf(singleColProp.get(7));
                          numberformate=String.valueOf(singleColProp.get(9));//added by sruthi for numberformate
                        //start of code by Nazneen for sub total deviation
                        String subTotalDev= container.getSubTotalDeviation(colmnname);
                        if(subTotalDev!=null){
                            if(subTotalDev.equalsIgnoreCase("Y")){
                                subTotalDeviation = "Y";
                            }
                        }
                        //end of code by Nazneen for sub total deviation
                        //start of code by Nazneen for GT as Avg for Sum Measures
                        if( container.isReportCrosstab()){
                            if(colmnname.contains("A_")){
                                gtCTAvgType = container.getCTGtAggType(colmnname);
                                gtCTcolAvgType = container.getCTcolGtAggType(colmnname);
                            }
                        }
                         //end of code by Nazneen for GT as Avg for Sum Measures

                        if (container.isIndicatorEnabled(colmnname))
                            showIndicators = "Y";
                    }

                      if( container.isReportCrosstab()){
                           if(crosstabMeasureId!=null && crosstabMeasureId.containsKey(colmnname))
                    NbrFormat = (NFMap.get(crosstabMeasureId.get(colmnname)) == null) ? " " : NFMap.get(crosstabMeasureId.get(colmnname)).toString();
                           else
                                NbrFormat = (NFMap.get(colmnname) == null) ? " " : NFMap.get(colmnname).toString();
                                       }else{
                         NbrFormat = (NFMap.get(colmnname) == null) ? " " : NFMap.get(colmnname).toString();
                                       }
                    //added by sruthi for numberformat
                    if(NbrFormat.trim().equalsIgnoreCase("")||NbrFormat==null){
                        numberformate="y";
                        }
                    //ended by sruthi
                    if(!container.getGrandTotalReq()){
                        grandTotDis="disabled";
                        grandTotCol="#8b8378";
                     }
                    if(!container.getNetTotalReq()){
                        subTotDis="disabled";
                         subTotCol="#8b8378";
                        }
                    if(!container.getCatMinValueReq()){
                        catMinDis="disabled";
                         catMinCol="#8b8378";
                        }
                    if(!container.getCatMaxValueReq()){
                        catMaxDis="disabled";
                         catMaxCol="#8b8378";
                        }
                    if(!container.getOverAllMinValueReq()){
                        overMinDis="disabled";
                         overMinCol="#8b8378";
                        }
                    if(!container.getOverAllMaxValueReq()){
                        overMaxDis="disabled";
                         overMaxCol="#8b8378";
                        }
                    if(!container.getAvgTotalReq()){
                        avgDis="disabled";
                         avgCol="#8b8378";
                        }
                   String ContxPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=dispcolmname%> Column Properties</title>
        <style>
            *{font : 11px verdana}
        </style>
        <link type="text/css" href="<%=ContxPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
               <script type="text/javascript" src="<%=ContxPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="<%=ContxPath%>/javascript/pbReportViewerJS.js"></script>
         <script type="text/javascript" src="<%= ContxPath%>/JS/jquery-ui.min.js"></script>
    <script type="text/javascript" src="<%= ContxPath%>/JS/reportgbl/jquery.ui.dialog.js"></script>
        
    </head>
    <body>
        <form action="<%=request.getContextPath()%>/reportViewer.do?reportBy=tableChanges&tableChange=columnProperties" name="colProForm" method="post">
            <input type="hidden" name="colName" id="colName" value="<%=colmnname%>">
            <input type="hidden" name="disColName" id="disColName" value="<%=dispcolmname%>">
            <input type="hidden" name="reportid" id="reportid" value="<%=reportId%>">
            <Table  width="100%" border="0">
                <tbody>
                    <Tr>
                        <%if (showTotal.equalsIgnoreCase("Y")) {%>
                        <Td>Grand Total</Td>
                        <Td><input  type="checkbox" checked name="GrandTotalReq" id="GrandTotalReq" value="Y" onclick="dispGrandTotal()"></Td>
                            <%} else {%>
                        <td><font color="<%=grandTotCol%>">Grand Total</font></td>
                        <td> <input type="checkbox" <%=grandTotDis%> name="GrandTotalReq"  id="GrandTotalReq" value="N" onclick="dispGrandTotal()" >
                            <%}%>
                        </td>

                        <%if (showSubTotal.equalsIgnoreCase("Y")) {%>
                        <Td>Sub Total</Td>
                        <Td><input type="checkbox" checked  name="NetTotalReq" id="NetTotalReq" value="Y" onclick="dispNetTotal()"></Td>
                            <%} else {%>
                        <td><font color="<%=subTotCol%>">Sub Total</font></td>
                        <td><input type="checkbox" <%=subTotDis%> name="NetTotalReq" id="NetTotalReq" value="N" onclick="dispNetTotal()">
                            <%}%>
                        </td>
                    </Tr>
                    <%if (showExtraTabs) {%>
                    <Tr>
                        <%if (overAllMax.equalsIgnoreCase("Y")) {%>
                        <Td>Overall Max</Td>
                        <Td> <input type="checkbox"  checked name="OverAllMaxValueReq" id="OverAllMaxValueReq" value="Y" onclick="dispOverAllMaxValue()"></Td>
                            <%} else {%>
                        <td><font color="<%=overMaxCol%>">Overall Max</font></td>
                        <td><input type="checkbox" <%=overMaxDis%> name="OverAllMaxValueReq" id="OverAllMaxValueReq"  value="N" onclick="dispOverAllMaxValue()">
                            <%}%>
                        </td>
                        <%if (overAllMin.equalsIgnoreCase("Y")) {%>
                        <Td>Overall Min </Td>
                        <Td> <input type="checkbox" checked name="OverAllMinValueReq" id="OverAllMinValueReq" value="Y" onclick="dispOverAllMinValue()"></Td>
                            <%} else {%>
                        <td><font color="<%=overMinCol%>">Overall Min</font></td>
                        <td>  <input type="checkbox" <%=overMinDis%> name="OverAllMinValueReq" id="OverAllMinValueReq" value="N" onclick="dispOverAllMinValue()" >
                            <%}%>
                        </td>
                    </Tr>
                    <Tr>
                        <%if (catMax.equalsIgnoreCase("Y")) {%>
                        <Td>Category Max</Td>
                        <Td> <input type="checkbox" checked name="CatMaxValueReq" id="CatMaxValueReq" value="Y" onclick="dispCatMaxValue()"></Td>
                            <%} else {%>
                        <td><font color="<%=catMaxCol%>">Category Max</font></td>
                        <td>  <input type="checkbox" <%=catMaxDis%> name="CatMaxValueReq" id="CatMaxValueReq" value="N" onclick="dispCatMaxValue()">
                            <%}%>
                        </td>
                        <%if (catMin.equalsIgnoreCase("Y")) {%>
                        <Td>Category Min</Td>
                        <Td> <input type="checkbox" checked name="CatMinValueReq" id="CatMinValueReq" value="Y" onclick="dispCatMinValue()" >
                            <%} else {%>
                        <td><font color="<%=catMinCol%>">Category Min</font></td>
                        <td><input type="checkbox" <%=catMinDis%> name="CatMinValueReq" id="CatMinValueReq" value="N" onclick="dispCatMinValue()" >
                            <%}%>
                        </td>
                    </Tr>
                    <Tr>
                        <Td width="25%">Symbols</Td>
                        <Td  width="25%">
                            <select name="columnSymbol" id="columnSymbol" style="width:50px">
                                <%for (String str : graphSymbols) {
                                        if (colSymbol.equalsIgnoreCase(str)) {%>
                                <option selected value="<%=str%>"><%=str%></option>
                                <%} else {%>
                                <option value="<%=str%>"><%=str%></option>
                                <%}
                                    }%>
                            </select>
                        </Td>
                        <%}%>
                        <%if (avgTotal.equalsIgnoreCase("Y")) {%>
                        <Td  width="25%">Average</Td>
                        <Td  width="25%"><input type="checkbox" checked  name="AvgTotalReq" id="AvgTotalReq" value="Y" onclick="dispAvgtTotal()">
                            <%} else {%>
                        <td><font color="<%=avgCol%>">Average</font></td>
                        <td>  <input type="checkbox" <%=avgDis%> name="AvgTotalReq" id="AvgTotalReq" value="N" onclick="dispAvgtTotal()">
                            <%}%>
                        </td>
                    </Tr>
                    <Tr>
                        <Td width="25%">Number Format</Td>
                        <Td  width="25%">
                            <select name="NbrFormat" id="NbrFormat" style="width:110px">
                                <%for (int i = 0; i < NbrFormats.length; i++) {
                                                        if (NbrFormat.equalsIgnoreCase(NbrFormats[i])) {%>
                                <option selected value="<%=NbrFormats[i]%>"><%=NbrFormatsDisp[i]%></option>
                                <%} else {%>
                                <option value="<%=NbrFormats[i]%>"><%=NbrFormatsDisp[i]%></option>
                                <%}
                                                    }%>
                            </select>
                        </Td>

                        <%--<%if (showIndicators.equalsIgnoreCase("Y")) {%>
                        <td><font>Show Indicator</font></td>
                        <td>  <input type="checkbox" checked name="Indicator" id="Indicator" value="N" onclick="">
                        </td>
                        <%} else {%>
                        <td><font>Show Indicator</font></td>
                        <td>  <input type="checkbox" name="Indicator" id="Indicator" value="N" onclick="">
                        </td>
                        <%}%>--%>
                      <Td width="25%">Indicator</Td>
                        <Td  width="25%">
                            <select name="colIndicator" id="colIndicator" style="width:100px">
                                <%for (String str : indicatorFormats) {
                                        if (colIndicator.equalsIgnoreCase(str)) {%>
                                <option selected value="<%=str%>"><%=str%></option>
                                <%} else {%>
                                <option value="<%=str%>"><%=str%></option>
                                <%}
                                    }%>
                            </select>
                        </Td>
                    </Tr>
                    <Tr>
                       <%--Data Align  --%>
  <%--                      <Td  width="25%">Data Align</Td>
                         <Td  width="25%">
                          <select name="dataAlign" id="dataAlign" style="width:100px">
                                <%for (String str : dataAlignments) {
                                        if (dataAlign.equalsIgnoreCase(str)) {%>
                                <option selected value="<%=str%>"><%=str%></option>
                                <%} else {%>
                                <option value="<%=str%>"><%=str%></option>
                                <%}
                                    }%>
                            </select>
                        </td>--%>
                       <%--Measure Type  --%>
                        <Td  width="25%">Measure Type</Td>
                         <td>
                             <select name="measureType" id="measureType" style="width:100px">
                                <%for (String str : measureTypes) {
                                        if (measureType.equalsIgnoreCase(str)) {%>
                                <option selected value="<%=str%>"><%=str%></option>
                                <%} else {%>
                                <option value="<%=str%>"><%=str%></option>
                                <%}
                                    }%>
                            </select>
                        </td>
                        <Td  width="25%">Time Conversion</Td>
                         <td>
                           <input type="checkbox" name="timeconversion" id="timeconversion" value="Y" onclick="disptimeconversion()">
                        </td>
                    </Tr>
                   <%-- <Tr>

                       <Td  width="25%">Measure Align</Td>
                         <td>
                            <select name="measureAlignments" id="measureAlignments" style="width:100px">
                                <%for (String str : measureAlignments) {
                                        if (measureType.equalsIgnoreCase(str)) {%>
                                <option selected value="<%=str%>"><%=str%></option>
                                <%} else {%>
                                <option value="<%=str%>"><%=str%></option>
                                <%}
                                    }%>
                            </select>
                        </td>
                    </Tr>--%>
<!--                   start of code by Nazneen for sub total deviation-->
                       <tr>
                           <%if( container.isReportCrosstab()){%>
                               <td><font color="<%=grandTotCol%>">Subtotal Deviation</font></td>
                               <td> <input type="checkbox" <%=subTotalDeviationDis%> name="subTotalDeviation"  id="subTotalDeviation" value="N" onclick="dispSubTotalDeviation()" disabled  >
                             <%}else{%>
                           <%if (subTotalDeviation.equalsIgnoreCase("Y")) {%>
                            <Td>Subtotal Deviation</Td>
                            <Td><input  type="checkbox" checked name="subTotalDeviation" id="subTotalDeviation" value="Y" onclick="dispSubTotalDeviation()"></Td>
                                <%} else {%>
                            <td><font color="<%=grandTotCol%>">Subtotal Deviation</font></td>
                            <td> <input type="checkbox" <%=subTotalDeviationDis%> name="subTotalDeviation"  id="subTotalDeviation" value="N" onclick="dispSubTotalDeviation()" >
                                <%}%>
                            </td>
                            <%}%>
<!-- added by sruthi for numberformate                           -->
                              <td>Number Format Header</td>
                              <%if(numberformate.equalsIgnoreCase("Y")){%>
                     <Td><input type="checkbox" checked name="NumberHeader" id="NumberHeader" value="Y" onclick="numberFormateHeader()" /></Td>
                   <% } else {%>
                   <Td><input type="checkbox" name="NumberHeader"   id="NumberHeader" value="N" onclick="numberFormateHeader()"/></Td>
                   <% } %>
<!-- ended by sruthi                  -->
                       </tr>

<!--                    end of code by Nazneen for sub total deviation-->

<!--start of code by mayank on 12/05/14 -->
<!--                    <tr> <td>Date With Time</td><td>
                  <input type="radio" name="dateoption" value="Date With Time" checked></td></tr>
                           <%//if (container.getDateandTimeOptions(colmnname) != null && container.getDateandTimeOptions(colmnname).equals("Only Date")) {%>
                   <tr><td>Only Date</td><td><input type="radio" name="dateoption"  value="Only Date" checked></td></tr>
                           <% //} else {%>
                <tr><td>Only Date</td><td><input type="radio" name="dateoption"  value="Only Date"></td></tr>-->
                    <tr> <td>Only Date</td><td>
                  <input type="radio" name="dateoption" value="Date With Time"  checked ></td>
                       
                        <td>
                            <div id="CustomHeaderTd"><table><tr>
                      <td>Custom Header</td>
                     <td> <input type="text"  name="CustomHeader" id="CustomHeader" value="" /></td>
                                    </tr></table>
                                     </div>
                        </td>
                          
                       
                    </tr>
                           <%if (container.getDateandTimeOptions(colmnname) != null && container.getDateandTimeOptions(colmnname).equals("Only Date")) {%>
                   <tr><td>Date With Time</td><td><input type="radio" name="dateoption"  value="Only Date" checked></td></tr>
                           <% } else {%>
                <tr><td>Date With Time</td><td><input type="radio" name="dateoption"  value="Only Date"></td></tr>
                <!--end of code by mayank on 12/05/14 -->
                           <%}%>
                           <%if (container.getDateandTimeOptions(colmnname) != null && container.getDateandTimeOptions(colmnname).equals("Only Time")) {%>
                   <tr><td>Only Time</td><td><input type="radio" name="dateoption" value="Only Time" checked></td></tr>
                           <%} else {%>
                <tr><td>Only Time</td><td><input type="radio" name="dateoption" value="Only Time"></td></tr>
                           <%}%>
                <tr><td>Date Selection</td><td><label for="from">from</label><br><Input type="text"  class="myTextbox5" name="substrvalue1" onkeypress="return isNumberKey(event)" id="noOfchars" size="1"></td><td><label for="to">To</label><br><Input type="text"  class="myTextbox6" name="substrvalue2" onkeypress="return isNumberKey(event)" id="noOfchar" size="1"></td></tr>
                <tr><td>Date Format</td><td><select name="dateformat" id="dateformat">
                                <%
                        for (int i = 0; i < dateFormat.length; i++) {
                        if (container.getDateFormatt(colmnname)!=null && container.getDateFormatt(colmnname).equalsIgnoreCase(dateFormat[i])) {%>
                                <option selected value="<%=dateFormatValues[i]%>"><%=dateFormat[i]%></option>
                                <%} else {%>
                                <option value="<%=dateFormatValues[i]%>"><%=dateFormat[i]%></option>
                                <%}
                        }%>
                      </select> </td>
                <!--                        Started by Bhargavi for col GT as Avg for Sum measures-->
                      <%if( container.isReportCrosstab()){
                        if(colmnname.contains("A_")){%>
                        <Td  width="30%">Cross-Tab column GT Agg Type</Td>
                         <td>
                             <select name="gtCTcolAvgType" id="gtCTcolAvgType" style="width:100px">
                                 <%if (gtCTcolAvgType.equalsIgnoreCase("AVG")) {%>
                                 <option selected value="NONE">NONE</option>
                                 <%} else {%>
                                 <option selected value="NONE" SELECTED>NONE</option>
                                 <%} if(gtCTcolAvgType.equalsIgnoreCase("AVG")){%>
                                 <option value="AVG" SELECTED>AVG</option>
                                 <%}else{%>
                                  <option value="AVG">AVG</option>
                                  <%}%>
                            </select>
                        </td>
                        <%}}%>
                        <!-- Ended by Bhargavi for col GT as Avg for Sum measures-->
                </tr>
                <Td  width="30%">Average Calculation Type</Td>
                         <td>
                             <select name="averagecalculationType" id="averagecalculationType" style="width:100px">
                                <%for (String str : averagecalculationtype) {
                                         if (container.getaveragecalculationtype(colmnname).equalsIgnoreCase(str)) {%>
                                <option selected value="<%=str%>"><%=str%></option>
                                <%} else {%>
                                <option value="<%=str%>"><%=str%></option>
                                <%}
                                    }%>
                            </select>
                        </td>
<!--                        Started by Nazneen for GT as Avg for Sum measures-->
                        <%if( container.isReportCrosstab()){
                        if(colmnname.contains("A_")){%>
                        <Td  width="30%">Cross-Tab GT Agg Type</Td>
                         <td>
                             <select name="gtCTAvgType" id="gtCTAvgType" style="width:100px">
                                 <%if (gtCTAvgType.equalsIgnoreCase("AVG")) {%>
                                 <option selected value="NONE">NONE</option>
                                 <%} else {%>
                                 <option selected value="NONE" SELECTED>NONE</option>
                                 <%} if(gtCTAvgType.equalsIgnoreCase("AVG")){%>
                                 <option value="AVG" SELECTED>AVG</option>
                                 <%}else{%>
                                  <option value="AVG">AVG</option>
                                  <%}%>
                            </select>
                        </td>
                        <%}}%>

<!-- Ended by Nazneen for GT as Avg for Sum measures-->

<!--                <tr><td>TimeFormat</td><td><select>
                                <option value="">DD-MM-YY</option>
                                <option value="">DD-MON-YY</option>
                                <option value="">MM-DD-YY</option>
                               <option value="">MON-DD-YY</option>
                      </select> </td></tr>-->
                    <Tr>
                        <td  align="center" colspan="4" height="10px" ></td>
                    </Tr>
                </tbody>
                <tfoot>
                    <Tr>
                        <td align="center"  colspan="4">
                            <input type="button" name="Save" value="Done" class="navtitle-hover" onclick="goSave('<%=reportId%>')">
                        </td>
                    </Tr>
                </tfoot>
            </Table>
        </form>
                        <script type="text/javascript">
            function goSave(reportId){
                document.forms.colProForm.submit();
                parent.closeColumnProperties();
                refreshReportTables('<%=request.getContextPath()%>',reportId);
            }
            function refreshReportTables(ctxPath,reportId){
                var source = ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+reportId;
                parent.document.getElementById("iframe1").src= source;
            }
            function isNumberKey(evt)
           {
   var charCode = (evt.which) ? evt.which : evt.keyCode
    if(charCode==46 || charCode==44)
        return true;
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
       }
       //added by sruthi for numberformat
    function numberFormateHeader()
            {
                var NumberHeader=document.getElementById("NumberHeader");
                if(NumberHeader.checked)
                    {
                         hideCustomHeader();//added by sruthi for customheader
                      NumberHeader.value="true";
                    }else
                        {    //alert("bjgbjg")
                            hideCustomHeader();//added by sruthi for customheader
                            NumberHeader.value="false"
                        }
            }
            //ended by sruthi
        </script>
        <!--added by sruthi for numberformat-->
  <script type="text/javascript">
        $(document).ready(function() {
                <%if(!numberformate.equalsIgnoreCase("Y")){%>
                  $("#CustomHeaderTd").hide();
            <%}%>
            });


  </script>
    </body>
</html>

<!--//ended by sruthi-->
<%}
            }%>


