<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,com.progen.report.PbReportCollection,java.util.ArrayList"%>
<%@ page import="prg.db.Container,prg.db.ContainerConstants,prg.db.PbReturnObject,java.util.HashMap,java.util.Set" %>
<%--
    Document   : PbTableProperties
    Created on : Jan 16, 2010, 11:27:13 AM
    Author     : Administrator
--%>
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            Container container = null;
            String reportId = request.getParameter("reportId");
            String[] tabSymbols = {"", "$", "Rs", "€", "¥ ", "%"};//dollar,inr,euro,yen and percent
            int rowViewByCount = -1;
            int columnViewBycount = -1;
            int ViewByCount=-1;
            boolean showExtraTabs = true;
            String themeColor="blue";
            String[] tableHeight={"100%","200%","300%","400%","ALL"};
            String[] tableHeightValue={"25","50","75","100","All"};
            String[] adhocDrill={"none","drilldown","drillside"};
            String[] msrDrill={"none","ReportDrill","ReportDrillPopUP","MeasureDrill","RelatedMeasures","SelfDrill"};
            String[] msrDrillLebels={"none","Report Drill","Report Drill (Pop-up)","Fact Drill","Related Measures","Self Drill"};
            String[] customTotaldispNames={"Grand Total","Sub Total","Overall Max","Overall Min","Category Max","Category Min","Overall Avg","Category Avg","LastRow"};
            String[] customTotalNames={"GT","CATST","OVEMAX","OVEMIN","CATMAX","CATMIN","AVG","CATAVG","LR"};
            String[] customRowTotalNames={"GT"};
            String[] customRowTotaldispNames={"Grand Total"};
            String isSummerized="";


            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
            if (reportId != null) {
                //added by Mohit Gupta for default locale
                   Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
                //ended By Mohit Gupta
                if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                    container = (Container) map.get(reportId);
                    columnViewBycount = Integer.parseInt(container.getColumnViewByCount());
                    rowViewByCount = (container.getViewByCount());
                    ViewByCount=(container.getViewByCount());
                    if(container.isSummarizedMeasuresEnabled()){
                        isSummerized="checked";
                }
                    HashMap<String,String> crossalignment=new HashMap<String,String>();
                    crossalignment=container.getcrossalign();
                    String l1="";
                    String l2="";
                    l1=crossalignment.get("l1");
                    l2=crossalignment.get("l2");
                       Boolean isCross=container.isReportCrosstab();
                       String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Table Properties</title>
        <style type="text/css" >
            *{font-size: 12px}
        </style>
            <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/jquery-1.3.2.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
         <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/css/global.css" rel="stylesheet" />
        <script  type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbTableMapJS.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" />
        
    </head>
    <body>
        <form action="" name="grpProForm" id="grpProForm" method="post">
            <Table width="100%" >

                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Grand_Total", cL)%></Td>
                    <Td>
                        <%if (container.getGrandTotalReq()) {%>
                        <input  type="checkbox" checked name="GrandTotalReq" id="GrandTotalReq" value="true" onclick="dispGrandTotal()">
                        <%} else {%>
                        <input type="checkbox" name="GrandTotalReq" id="GrandTotalReq" value="false" onclick="dispGrandTotal()">
                        <%}%>
                    </Td>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Sub_Total", cL)%></Td>
                    <Td>
                        <%if (container.getNetTotalReq()) {%>
                        <input type="checkbox" checked  name="NetTotalReq" id="NetTotalReq" value="true" onclick="dispNetTotal()">
                        <%} else {%>
                        <input type="checkbox" name="NetTotalReq" id="NetTotalReq" value="false" onclick="dispNetTotal()">
                        <%}%>
                    </Td>
                </Tr>
                <%if(showExtraTabs){%>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Overall_Max", cL)%></Td>
                    <Td>
                        <%if (container.getOverAllMaxValueReq()) {%>
                        <input type="checkbox"  checked name="OverAllMaxValueReq" id="OverAllMaxValueReq" value="true" onclick="dispOverAllMaxValue()">
                        <%} else {%>
                        <input type="checkbox" name="OverAllMaxValueReq" id="OverAllMaxValueReq" value="false" onclick="dispOverAllMaxValue()">
                        <%}%>
                    </Td>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Overall_Min", cL)%></Td>
                    <Td>
                        <%if (container.getOverAllMinValueReq()) {%>
                        <input type="checkbox" checked name="OverAllMinValueReq" id="OverAllMinValueReq" value="true" onclick="dispOverAllMinValue()">
                        <%} else {%>
                        <input type="checkbox" name="OverAllMinValueReq" id="OverAllMinValueReq" value="false" onclick="dispOverAllMinValue()">
                        <%}%>
                    </Td>
                </Tr>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Category_Max", cL)%></Td>
                    <Td>
                        <%if (container.getCatMaxValueReq()) {%>
                        <input type="checkbox" checked name="CatMaxValueReq" id="CatMaxValueReq" value="true" onclick="dispCatMaxValue()">
                        <%} else {%>
                        <input type="checkbox" name="CatMaxValueReq" id="CatMaxValueReq" value="false" onclick="dispCatMaxValue()">
                        <%}%>
                    </Td>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Category_Min", cL)%></Td>
                    <Td>
                        <%if (container.getCatMinValueReq()) {%>
                        <input type="checkbox" checked name="CatMinValueReq" id="CatMinValueReq" value="true" onclick="dispCatMinValue()">
                        <%} else {%>
                        <input type="checkbox" name="CatMinValueReq" id="CatMinValueReq" value="false" onclick="dispCatMinValue()">
                        <%}%>

                    </Td>
                </Tr>
                <%}%>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Overall_Avg", cL)%></Td>
                    <Td>
                        <%if (container.getAvgTotalReq()) {%>
                        <input type="checkbox" checked  name="AvgTotalReq" id="AvgTotalReq" value="true" onclick="dispAvgtTotal()">
                        <%} else {%>
                        <input type="checkbox" name="AvgTotalReq" id="AvgTotalReq" value="false" onclick="dispAvgtTotal()">
                        <%}%>
                    </Td>
                    <%if(showExtraTabs){%>
<!--                    <Td><%=TranslaterHelper.getTranslatedInLocale("advance_search", cL)%></Td>
                    <Td>
                        <%if (container.getSearchReq()) {%>
                        <input  type="checkbox" checked name="SearchReq" id="SearchReq" value="true" onclick="dispSearchReq()">
                        <%} else {%>
                        <input type="checkbox" name="SearchReq" id="SearchReq" value="false" onclick="dispSearchReq()">
                        <%}%>
                    </Td>-->
                    <%}%>
                      <td><%=TranslaterHelper.getTranslatedInLocale("Hide_Grand_Zero", cL)%></td>
                     <%if(container.getGrandTotalZero()){%>
                     <Td><input type="checkbox" checked name="grandTotalZero" id="grandTotalZero" value="true" onclick="grandTotalZero1()" /></Td>
                   <% } else {%>
                   <Td><input type="checkbox" name="grandTotalZero" onclick="grandTotalZero1()"  id="grandTotalZero" value="false" /></Td>
                   <% } %>
                </Tr>
                <Tr>

                </Tr>
                <%if (columnViewBycount > 0) {%>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Row_Grand_Total", cL)%></Td>
                    <%
                       String first="";
                       String last="";
                       String none="";
                      if(container.getCrosstabGrandTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST)){
                            first="selected";
                        }else if(container.getCrosstabGrandTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)){
                            last="selected";
                        }else if(container.getCrosstabGrandTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_NONE)){
                            none="selected";
                        }
                    %>
                    <Td> <select name="CrosstabGrandTotalDisplayed" id="CrosstabGrandTotalDisplayed">
                            <option value="First" <%=first%> >First</option>
                            <option value="Last"  <%=last%> >Last</option>
                            <option value="None" <%=none%> >None</option>
                        </select>

                    </Td>
                    <% if ( columnViewBycount > 1 ) {%>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Row_Sub_Total", cL)%></Td>
                    <%
                     String before="";
                       String after="";
                       String allFirst="";
                       String allLast="";
                       String sNone="";
                      if(container.getCrosstabSubTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_BEFORE)){
                            before="selected";
                        }else if(container.getCrosstabSubTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_AFTER)){
                            after="selected";
                        }else if(container.getCrosstabSubTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_FIRST)){
                            allFirst="selected";
                        }else if(container.getCrosstabSubTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_LAST)){
                            allLast="selected";
                        }else if(container.getCrosstabSubTotalDisplayPosition().equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_NONE)){
                            sNone="selected";
                        }
                     %>
                    <Td> <select name="CrosstabSubTotalDisplayed" id="CrosstabSubTotalDisplayed">
                                        <option value="Before" <%=before%>>Before</option>
                                        <option value="After" <%=after%>>After</option>
                                        <option value="AllFirst" <%=allFirst%>>All First</option>
                                        <option value="AllLast" <%=allLast%>>All last</option>
                                        <option value="None" <%=sNone%>>None</option>
                         </select>
                    </Td>
                    <% } %>
                </Tr>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Measure_Position", cL)%></Td>
                    <Td><select name="MeasurePos" id="MeasurePos">
                            <% if(container.getMeasurePosition()==Integer.parseInt(container.getColumnViewByCount())){%>
                            <option value='<%=columnViewBycount%>' selected>Bottom</option>
                            <%} else{%>
                            <option value= '<%=columnViewBycount%>' >Bottom</option>
                            <%}
                            if ( columnViewBycount > 1 ) {
                            for(int colCount=1;colCount<columnViewBycount;colCount++)
                            {
                                if(container.getMeasurePosition()==colCount){%>
                            <option value=<%=colCount%> selected><%=colCount%></option>
                            <%}else{%>
                            <option value='<%=colCount%>'><%=colCount%></option>
                            <% }
                              } }if(container.getMeasurePosition()==0) {%>
                              <option value=0 selected>Top</option>
                              <%}else{%>
                              <option value=0>Top</option>
                              <%}%>
                        </select>

                    </Td>
                </Tr>
                <%}%>
                 <% if(ViewByCount > 1) {%>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Wrap_to_Single_View_By", cL)%></Td>
                    <% if ( container.getRowViewDisplayMode().equals(ContainerConstants.TABLE_ROWVIEW_DISPLAY_WRAPPED) ) {%>
                        <Td><input type="checkbox" checked name="WrapToSingleViewBy" id="WrapToSingleViewBy" value="true" onclick="dispSingleViewBy()"></Td>
                        <% } else {%>
                        <Td><input type="checkbox" name="WrapToSingleViewBy" id="WrapToSingleViewBy" value="false" onclick="dispSingleViewBy()"></Td>
                        <% } %>
                </Tr>
                <% } %>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Category_Avg", cL)%></Td>
                    <% if ( container.getCatAvgTotalReq() ) {%>
                    <Td><input type="checkbox" checked name="categoryAvg" value="true" id="categoryAvg" onclick="dispCategoryAvg()" /></Td>
                    <% } else {%>
                    <Td><input type="checkbox" name="categoryAvg" value="false" id="categoryAvg" onclick="dispCategoryAvg()" /></Td>
                    <% } %>
                </Tr>
                 <!-- Privilege control needs to be added -->
                <tr>
                <td><%=TranslaterHelper.getTranslatedInLocale("Multi_Select", cL)%></td>
                <% if ( container.isDrillAcrossSupported() ) {%>
                <Td><input type="checkbox" checked name="drillAcrossViewBy" id="drillAcrossViewBy" value="true" onclick="drillAcrossChanged()"></Td>
                <% } else {%>
                <Td><input type="checkbox" name="drillAcrossViewBy" id="drillAcrossViewBy" value="false" onclick="drillAcrossChanged()"></Td>
                <% } %>
                <td><%=TranslaterHelper.getTranslatedInLocale("Table_Height", cL)%></td>
                <td>
                    <select style="width:80px" id="pagesPerSlide" name="pagesPerSlide">
                     <%for(int i=0;i<tableHeight.length;i++){
                         if(tableHeightValue[i].equalsIgnoreCase(container.getPagesPerSlide())){%>
                         <option selected value="<%=tableHeightValue[i]%>"><%=tableHeight[i]%></option>
                         <%}else{%>
                     <option value="<%=tableHeightValue[i]%>"><%=tableHeight[i]%></option>
                     <%}
                     }%>
                    </select>
                </td>
                </tr>
<!-- Start by mayank -->
                <% if(ViewByCount >= 1 && !container.isReportCrosstab()) {%>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Transpose", cL)%></Td>
                    <% if ( container.isTransposed() ) {%>
                        <Td><input type="checkbox" checked name="TransposeViewBy" id="TransposeViewBy" value="true" onclick="transposeViewBy()"></Td>
                        <% } else {%>
                        <Td><input type="checkbox" name="TransposeViewBy" id="TransposeViewBy" value="false" onclick="transposeViewBy()"></Td>
                        <% } %>
                </Tr>
                <% } %>     <!-- End by Mayank -->
                <Tr>
                   <Td><%=TranslaterHelper.getTranslatedInLocale("Custom_Totals", cL)%></Td>
                  <% if(container.isCustomTotEnabled()) { %>
                   <Td><input type="checkbox" checked name="AddCustomTotal" value="true" id="AddCustomTotal" onclick="addCustomTotal()" /></Td>
                   <% } else {%>
                   <Td><input type="checkbox" name="AddCustomTotal" value="false" id="AddCustomTotal" onclick="addCustomTotal()" /></Td>
                   <% } %>
                   <Td><%=TranslaterHelper.getTranslatedInLocale("Show_Quick_Trend_Icon", cL)%></Td>
                  <% if(container.isToShowTrendIcon()) { %>
                   <Td><input type="checkbox" checked name="showIconTrend" value="true" id="showIconTrend" onclick="addshowIconTrend2()" /></Td>
                   <% } else {%>
                   <Td><input type="checkbox" name="showIconTrend" value="false" id="showIconTrend" onclick="addshowIconTrend2()" /></Td>
                   <% } %>
                </Tr>
              <% if(container.isCustomTotEnabled()) { %>
                <Tr id="CustTotName">
                    <% } else {%>
                <Tr id="CustTotName" style="display:none;">
                    <% } %>
                   <Td><%=TranslaterHelper.getTranslatedInLocale("Custom_Total_Name", cL)%>:</Td>
                   <Td>
                       <% if(container.getCustTotName()!=null) { %>
                       <input type="textbox"  name="CustomTotalname" value="<%=container.getCustTotName()%>" id="CustomTotalname"  />
                       <% } else {%>
                       <input type="textbox"  name="CustomTotalname" id="CustomTotalname"  />
                       <%}%>
                   </Td>
                </Tr>
            <% if(container.isCustomTotEnabled()) { %>
                <Tr id="MappingCustTot" >
                    <% } else {%>
                    <Tr id="MappingCustTot" style="display:none;">
                        <% } %>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Map_To", cL)%>:</Td>
                    <Td><select name="mapCustomTot" id="mapCustomTot">
                            <% for(int i=0;i<customTotalNames.length;i++){
                                if(customTotalNames[i].equalsIgnoreCase(container.getMappedTo())){%>
                                <option selected value="<%=customTotalNames[i]%>"><%=customTotaldispNames[i]%></option>
                                <%}else{%>
                            <option value="<%=customTotalNames[i]%>"><%=customTotaldispNames[i]%></option>
                            <%}
                            }%>
                        </select></Td>
                </Tr>

                <% if( container.isReportCrosstab() &&
                       container.getCrosstabGrandTotalDisplayPosition().equals(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST) &&
                       container.getCrosstabSubTotalDisplayPosition().equals(ContainerConstants.CROSSTAB_SUBTOTAL_FIRST) &&
                       Integer.parseInt(container.getColumnViewByCount()) > 1 ) {%>
                <Tr>
                    <Td><%=TranslaterHelper.getTranslatedInLocale("Wrap_Column_Headers", cL)%></Td>
                    <% if ( container.isCrosstabHeaderWrapped() ) {%>
                        <Td><input type="checkbox" checked name="WrapCrosstabHeader" id="WrapCrosstabHeader" value="true" onclick="wrapCrosstabHeader()"></Td>
                        <% } else {%>
                        <Td><input type="checkbox" name="WrapCrosstabHeader" id="WrapCrosstabHeader" value="false" onclick="wrapCrosstabHeader()"></Td>
                        <% } %>
                </Tr>
                <% } %>

                    <tr>
                    <td><%=TranslaterHelper.getTranslatedInLocale("Row_Count", cL)%></td>
                    <% if(container.isRowCountReq()){%>
                    <td><input type="checkbox"  checked name="rowCount" id="rowCount" value="true" onclick="disprowCount()"> </td>
                    <% } else {%>
                    <td><input type="checkbox" name="rowCount" id="rowCount" value="false" onclick="disprowCount()"> </td>
                    <%}%>
                     </tr>


                 <tr>
<!--                    <td>Enable Measure Drill</td>
                    <Td>
                        <%if (container.isMeasDrill()) {%>
                        <input  type="checkbox" checked name="enableDrillMeasure" id="enableDrillMeasure" value="true" onclick="enableDrillMeasureFun()">
                        <%} else {%>
                        <input type="checkbox" name="enableDrillMeasure" id="enableDrillMeasure" value="false" onclick="enableDrillMeasureFun()">
                        <%}%>
                    </Td>   -->
                        <td><%=TranslaterHelper.getTranslatedInLocale("Enable_Value_Drill", cL)%></td>
                        <td>
                            <select name="enableMsrDrill" id="enableMsrDrill">
                               <% for(int i=0;i<msrDrill.length;i++){
                                   if(container.getMeasureDrillType().equalsIgnoreCase(msrDrill[i])){%>
                                   <option selected value="<%=msrDrill[i]%>"><%=msrDrillLebels[i]%></option>
                                   <!--<option value=""></option>-->
                                   <%}else{%>
                               <option value="<%=msrDrill[i]%>"><%=msrDrillLebels[i]%></option>
                               
                               <%}
                                   }%>
                            </select>
                        </td>

                </tr>
                 <% //if(container.getReportCollect().reportRowViewbyValues.size()==1 && !container.isReportCrosstab()) {%>
                 <tr>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Adhoc_Drill", cL)%></td>
                     <td>
                         <select name="enableAdhocDrill" id="enableAdhocDrill">
                            <% for(int i=0;i<adhocDrill.length;i++){
                                if(container.getAdhocDrillType().equalsIgnoreCase(adhocDrill[i])){%>
                                <option selected value="<%=adhocDrill[i]%>"><%=adhocDrill[i]%></option>
                                <%}else{%>
                            <option value="<%=adhocDrill[i]%>"><%=adhocDrill[i]%></option>
                            <%}
                                }%>
                         </select>
                        </td>
                        <td><%=TranslaterHelper.getTranslatedInLocale("Show_ST_Time_Period", cL)%></td>
                          <% if ( container.isShowStTimePeriod() ) {%>
                      <td><input  type="checkbox" checked name="displayStTimePeriod" id="displayStTimePeriod" value="true" onclick="enableDisplayStTimePeriod()"> </td>
                      <% } else {%>
                     <td><input  type="checkbox"  name="displayStTimePeriod" id="displayStTimePeriod" value="false" onclick="enableDisplayStTimePeriod()"> </td>
                     <% } %>

                 </tr>
                 <% //}%>

                 <% if(ViewByCount == 1 && !container.isReportCrosstab()) {%>
                 <tr>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Parameter_Drill", cL)%></td>
                      <% if ( container.isParameterDrill() ) {%>
                      <td><input  type="checkbox" checked name="enableParameterDrill" id="enableParameterDrill" value="true" onclick="enableParameterDrillFun()"> </td>
                      <% } else {%>
                     <td><input  type="checkbox"  name="enableParameterDrill" id="enableParameterDrill" value="false" onclick="enableParameterDrillFun()"> </td>
                     <% } %>
                 </tr>

                 <%}%>
              <% if(ViewByCount == 1 && !container.isReportCrosstab()) {%>
                 <tr>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Change_To_TreeTable_Display_Insights_Style", cL)%></td>
                      <% if ( container.isTreeTableDisplay() ) {%>
                     <td><input  type="checkbox" checked name="treeTableDisplay" id="treeTableDisplay" value="true" onclick="enableTreeTableDisplay()"> </td>
                      <% } else {%>
                     <td><input  type="checkbox"  name="treeTableDisplay" id="treeTableDisplay" value="false" onclick="enableTreeTableDisplay()"> </td>
                      <% } %>
                     
                     
                 </tr>

                 <%}%>

                <%--<Tr>
                    <Td>Symbols</Td>
                    <Td>
                        <select name="TableSymbols" id="TableSymbols">
                            <%for (String str : tabSymbols) {
                        if (container.getTableSymbols() != null && container.getTableSymbols().equalsIgnoreCase(str)) {%>
                            <option selected value="<%=str%>"><%=str%></option>
                            <%} else {%>
                            <option value="<%=str%>"><%=str%></option>
                            <%}
                    }%>
                        </select>
                    </Td>
                    <Td>&nbsp;</Td>
                    <Td>&nbsp;</Td>
                </Tr>--%>
                <Tr>
                        <Td><%=TranslaterHelper.getTranslatedInLocale("Drill_values", cL)%></Td>
                        <% if( container.isdrillvalues() ) {%>
                        <Td><input type="checkbox" checked name="DrillViewBy" id="DrillViewBy" value="true" onclick="drillViewBy()"></Td>
                        <% } else {%>
                         <Td><input type="checkbox" name="DrillViewBy" id="DrillViewBy" value="false" onclick="drillViewBy()"></Td>
                        <% } %>
                         
                         <td><%=TranslaterHelper.getTranslatedInLocale("Grand_Total_Color", cL)%> </td>
                       <%if (container.getGrandTotalReq()) {%>
                         <td> <input type="text" style="width:50px;cursor:pointer;background-color:<%=container.getGrandTotalBGColor()%>;"  name="GTBgColor"   id="GTBgColor"  onclick="showColor(this.id)"> </td>
                        <%} else {%>
                        <td> <input type="text" style="width:50px;cursor:pointer;background-color:<%=container.getGrandTotalBGColor()%>;" name="GTBgColor"    id="GTBgColor"  onclick="showColor(this.id)"> </td>
                        <%}%>
                    
                </Tr>
                 <tr>
                   <td><%=TranslaterHelper.getTranslatedInLocale("Serial_Num", cL)%>:</td>
                   <% if( container.isSerialNumDisplay() ) {%>
                        <Td><input  type="checkbox" checked  name="serialnum" id="serialnum" value="true" onclick="enableSerialNum()"></Td>
                        <% } else {%>
                    <td><input  type="checkbox"  name="serialnum" id="serialnum" value="false" onclick="enableSerialNum()"></td>
                      <% } %>
                    <td><%=TranslaterHelper.getTranslatedInLocale("Sub_Total_Color", cL)%></td>
                    
                          <%if (container.getNetTotalReq()) {%>
                       <td> <input type="text" style="width:50px;cursor:pointer;background-color:<%=container.getSubTotalBGColor()%>;" name="SubTotalBgColor"   id="SubTotalBgColor"  onclick="showColor(this.id)"> </td>
                        <%} else {%>
                        <td> <input type="text" style="width:50px;cursor:pointer;background-color:<%=container.getSubTotalBGColor()%>;" name="SubTotalBgColor"    id="SubTotalBgColor"  onclick="showColor(this.id)"> </td>
                        <%}%>
                        
                 </tr>
                 <tr><td><%=TranslaterHelper.getTranslatedInLocale("Rename_Total", cL)%></td>
                      <% if( container.isRenameTotal()) {%>
                     <td><input type="checkbox" checked name="renameTotal" id="renameTotal" value="true" onclick="enableRenameTotal()"</td>
                     <%}else{%>
                     <td><input type="checkbox" name="renameTotal" id="renameTotal" value="false" onclick="enableRenameTotal()"</td>
                     <%}%>
                      <td><%=TranslaterHelper.getTranslatedInLocale("Header_Color", cL)%></td>
                       <%if ( container.isShowStTimePeriod()) {%>
                         <td> <input type="text" style="width:50px;cursor:pointer;background-color:<%=container.getHeaderBgColor()%>;"  name="HeaderBgColor"   id="HeaderBgColor"  onclick="showColor(this.id)"> </td>
                        <%} else {%>
                        <td> <input type="text" style="width:50px;cursor:pointer;background-color:<%=container.getHeaderBgColor()%>;" name="HeaderBgColor"    id="HeaderBgColor"  onclick="showColor(this.id)"> </td>
                        <%}%>
                 </tr>
                  <% if( container.isRenameTotal()) {%>
                 <tr id="renameTotalTR">
                     <%}else{%>
                 <tr id="renameTotalTR" style="display:none;">
                     <%}%>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Name", cL)%>:</td>
                     <td>
                         <select name="originalTotalName" id="originalTotalName" onchange="editRename()">
                            <% for(int i=0;i<customTotalNames.length;i++){%>
                            <option value="<%=customTotalNames[i]%>"><%=customTotaldispNames[i]%></option>
                            <%}%>
                        </select>
                     </td>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Rename_To", cL)%>:</td>
                     <% if(container.getRenamedTotalName()!=null && !container.getRenamedTotalName().isEmpty() && !container.getRenamedTotalName().equalsIgnoreCase("none")){%>
                       <td><input type="textbox"  value="<%=container.getRenamedTotalName()%>" name="RenamedTotalName" id="RenamedTotalName"/></td>
                     <%}else{%>
                     <td><input type="textbox"  value="<%=customTotaldispNames[0]%>" name="RenamedTotalName" id="RenamedTotalName"/></td>
                     <%}%>

                 </tr>
                 <tr><td><%=TranslaterHelper.getTranslatedInLocale("Row_Rename_Total", cL)%></td>
                     <% if( container.isrowRenameTotal()) {%>
                     <td><input type="checkbox" checked name="rowGTRename" id="rowGTRename" value="true" onclick="enableRowGTRename()"</td>
                     <%}else{%>
                     <td><input type="checkbox" name="rowGTRename" id="rowGTRename" value="false" onclick="enableRowGTRename()"</td>
                     <%}%>
                 </tr>

                     <% if( container.isrowRenameTotal()) {%>
                       <tr id="rowGTRenametr">
                     <%}else{%>
                       <tr id="rowGTRenametr" style="display:none;">
                     <%}%>

                     <td><%=TranslaterHelper.getTranslatedInLocale("Name", cL)%>:</td>
                     <td>
                         <select name="originalRowTotalName" id="originalRowTotalName" onchange="editRowRename()">
                            <% for(int i=0;i<customRowTotalNames.length;i++){%>
                            <option value="<%=customRowTotalNames[i]%>"><%=customRowTotaldispNames[i]%></option>
                            <%}%>
                        </select>
                     </td>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Rename_To", cL)%>:</td>
                     <% if(container.getRowRenamedTotalName()!=null && !container.getRowRenamedTotalName().isEmpty() && !container.getRowRenamedTotalName().equalsIgnoreCase("none")){%>
                       <td><input type="textbox"  value="<%=container.getRowRenamedTotalName()%>" name="RowRenamedTotalName" id="RowRenamedTotalName"/></td>
                     <%}else{%>
                     <td><input type="textbox"  value="<%=customRowTotaldispNames[0]%>" name="RowRenamedTotalName" id="RowRenamedTotalName"/></td>
                     <%}%>

                     </tr>

                 <tr>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Summarized_Measures", cL)%></td>
                      <% if(container.isSummarizedMeasuresEnabled()) { %>
                    <td><input type="checkbox" name="summarizedMeasure" id="summarizedMeasure" value="true" onclick="enableSummarizedMeasure(this.id)" <%=isSummerized%> ></td>
                   <% } else {%>
                     <td><input type="checkbox" name="summarizedMeasure" id="summarizedMeasure" value="false" onclick="enableSummarizedMeasure(this.id)" <%=isSummerized%> ></td>
                  <% } %>
                      <Td>Reset Color</Td>
                    <Td>
                 <%--       <%if(!container.getNetTotalReq()) {%>
                        <input type="checkbox" checked  name="reSetColor" id="reSetColor" value="true" onclick="enableResetColor()">
                        <%} else {%>--%>
                        <input type="checkbox" name="reSetColor" id="reSetColor" value="false" onclick="enableResetColor()">
                      <%--  <%}%> --%>
                    </Td>
                     
                 </tr>
                  <tr>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Mask_Zero", cL)%></td>
                     <% if(container.isMaskZeros()) { %>
                   <Td><input type="checkbox" checked name="maskZero" id="maskZero" value="true" onclick="maskZeroValue()" /></Td>
                   <% } else {%>
                   <Td><input type="checkbox" name="maskZero" id="maskZero" value="false" onclick="maskZeroValue()" /></Td>
                   <% } %>

                 </tr>
                 <!--                    //added by sruthi-->
<!--                    <tr>
                     <td><%=TranslaterHelper.getTranslatedInLocale("Hide_Grand_Zero", cL)%></td>
                     <%if(container.getGrandTotalZero()){%>
                     <Td><input type="checkbox" checked name="grandTotalZero" id="grandTotalZero" value="true" onclick="grandTotalZero1()" /></Td>
                   <% } else {%>
                   <Td><input type="checkbox" name="grandTotalZero" onclick="grandTotalZero1()"  id="grandTotalZero" value="false" /></Td>
                   <% } %>
                 </tr>-->
<!--    ended by sruthi             -->
                  <% if(container.isReportCrosstab()) { %>
                 <td><%=TranslaterHelper.getTranslatedInLocale("Hide_Measure_Heading", cL)%></td>
                     <% if(container.isHideMsrHeading()) { %>
                   <Td><input type="checkbox" checked name="HideMsrHdng" id="hideMsrHdng" value="true" onclick="testHideMsr()" /></Td>
                   <% } else {%>
                   <Td><input type="checkbox" name="HideMsrHdng" id="hideMsrHdng" value="false" onclick="testHideMsr()" /></Td>
                   <% }
                     }%>

                   <tr>
<!--                       Added by Faiz Ansari-->
                     <% if(container.isReportCrosstab()) { %>
                   <table style='border:1px solid #808080;width:100%'>
                        <tr>
                            <Td ><%=TranslaterHelper.getTranslatedInLocale("Report_Header_Separator", cL)%></Td>
                            <td ><%=TranslaterHelper.getTranslatedInLocale("First_Row", cL)%></td>
                            <td ><%=TranslaterHelper.getTranslatedInLocale("Second_Row", cL)%></td>
                        </tr>
                        <tr>
                            <td><%=TranslaterHelper.getTranslatedInLocale("None", cL)%></td>
                            <td><input id='l1N' onclick="setSepL1Val('N')" type='radio' name='separator1' value='N'/></td>
                            <td><input id='l2N' onclick="setSepL2Val('N')" type='radio' name='separator2' value='N'/></td>
                        </tr>
                        <tr>
                            <td><%=TranslaterHelper.getTranslatedInLocale("Vertical", cL)%></td>
                            <td><input id='l1V' onclick="setSepL1Val('V')" type='radio' name='separator1' value='V'/></td>
                            <td><input id='l2E' onclick="setSepL2Val('E')" type='radio' name='separator2' value='E'/></td>                  
                        </tr>
                        <tr>
                            <td><%=TranslaterHelper.getTranslatedInLocale("Horizontal", cL)%></td>
                            <td><input id='l1H' onclick="setSepL1Val('H')" type='radio' name='separator1' value='H'/></td>
                        </tr>
                        <tr>
                            <td><%=TranslaterHelper.getTranslatedInLocale("Both", cL)%></td>
                            <td><input id='l1B' onclick="setSepL1Val('B')" type='radio' name='separator1' value='B'/></td>
                        </tr>
                   </table>
                   <% }%>
<!--                   End!!!-->
                   </tr>
                   </br>
                <Tr>
                    <td align="center" colspan="9">
                        <input type="button" name="Save" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" class="navtitle-hover" onclick="goSave('<%=reportId%>')">
                    </td>
                </Tr>
            </Table>
        </form>
                    <div id="colorsDiv" style="display: none" title="Select color">
                    <center>
                        <input type="text" id="color" style="" value="#12345" >
                        <div id="colorpicker" style=""></div>
                        <input type="button" align="center" value="Done" class="navtitle-hover" onclick="saveSelectedColor()">
                        <input type="button" align="center" value="Cancel" class="navtitle-hover" onclick="cancelColor()">
                        <input type="hidden" id="selectedId" value="">
                    </center>
        </div>
                    <script type="text/javascript" >
            //                Added by Faiz Ansari
            
           
            
            $(document).ready(function(){
                if('<%=isCross%>'==true)
                    {
                if('<%=l1%>' == 'n' || '<%=l1%>' == 'null' || '<%=l1%>' == null)
                {
                    
                    document.getElementById("l1N").checked = true;                    
                }
                if('<%=l2%>' == 'n' || '<%=l1%>' == 'null' || '<%=l2%>' == null)
                {
                    document.getElementById("l2N").checked = true;                    
                }
                if('<%=l1%>'!='null' && '<%=l2%>'!='null')
                {
                document.getElementById("l1"+'<%=l1%>').checked = true;
                document.getElementById("l2"+'<%=l2%>').checked = true;
                }
                }
              $("#colorsDiv").dialog({
                            bgiframe: true,
                            autoOpen: false,
                            height:300,
                            width: 300,
                            modal: true,
                            Cancel: function() {
                                $(this).dialog('close');
                            }

            });
               $('#colorpicker').farbtastic('#color');
            });
            var l1='<%=l1%>';
            var l2='<%=l2%>';
            var sep="";
            function setSepL1Val(val){
                l1=val;
            }
            function setSepL2Val(val){
                l2=val;
            }
            //                End!!!
            function goSave(reportId,pageSize){
                sep=l1+""+l2;
                var ViewFrom="<%=session.getAttribute("ViewFrom")%>";
                var From = "";
                if(parent.document.getElementById("Designer")!= null){
                    From=parent.document.getElementById("Designer").value;
                }
                //added By Mohit Gupta
                var GTcolor=colorToHex($("#GTBgColor").css('backgroundColor'));
              GTcolor=encodeURIComponent(GTcolor);
              var SubTotalColor=colorToHex($("#SubTotalBgColor").css('backgroundColor'));
              SubTotalColor=encodeURIComponent(SubTotalColor);
              //added by manoj 
              var headerColor=colorToHex($("#HeaderBgColor").css('backgroundColor'));
              headerColor=encodeURIComponent(headerColor);
              if($("#reSetColor").is(":checked"))
                  {
                     
                      headerColor="#f1f1f1";  headerColor=encodeURIComponent(headerColor);
                      GTcolor="#fff";  GTcolor=encodeURIComponent(GTcolor);
                      SubTotalColor="#fff";   SubTotalColor=encodeURIComponent(SubTotalColor);
                  }
                parent.$("#dispTabProp").dialog('close');
                 var pageSize="<%=request.getParameter("slidePages")%>";
                $.post("<%=request.getContextPath()%>/reportViewer.do?reportBy=tableChanges&tableChange=tableProperties&reportid="+reportId+"&seprator="+sep+"&slidePages="+pageSize+"&slidePages="+pageSize+"&GTcolor="+GTcolor+"&SubTotalColor="+SubTotalColor+"&headerBGColor="+headerColor, $("#grpProForm").serialize(),
                function(data){
                    if ( data == "refresh")
                    {
                        if(ViewFrom=="Designer" &&(From== null || From!="fromDesigner")){
                            parent.dispTable("none");
                        }
                        else
                            parent.refreshReportTables('<%=request.getContextPath()%>',reportId,pageSize);
                    }
                });
            }

            function setRecordSize(sVal,tabId){
    var source = "TableDisplay/pbDisplay.jsp?slidePages="+sVal+"&tabId="+tabId;
       var dSrc = document.getElementById("iframe1");
        dSrc.src = source;
            }
            function goCancel(){
                parent.document.getElementById("dispGrpProp").style.display='none';
                parent.document.getElementById("dispGrpPropFrame").style.display="none"
                parent.document.getElementById('fade').style.display='none';
            }

            function dispGrandTotal(){
                var GrandTotalReqObj=document.getElementById("GrandTotalReq");
                if(GrandTotalReqObj.checked){
                    GrandTotalReqObj.value="true"
                }else{
                    GrandTotalReqObj.value="false";
                }
            }
            function dispNetTotal(){
                var NetTotalReqObj=document.getElementById("NetTotalReq");
                if(NetTotalReqObj.checked){
                    NetTotalReqObj.value="true"
                }else{
                    NetTotalReqObj.value="false";
                }
            }
            function dispAvgtTotal(){
                var AvgTotalReqObj=document.getElementById("AvgTotalReq");
                if(AvgTotalReqObj.checked){
                    AvgTotalReqObj.value="true"
                }else{
                    AvgTotalReqObj.value="false";
                }
            }
            function dispOverAllMaxValue(){
                var OverAllMaxValueReqObj=document.getElementById("OverAllMaxValueReq");
                if(OverAllMaxValueReqObj.checked){
                    OverAllMaxValueReqObj.value="true"
                }else{
                    OverAllMaxValueReqObj.value="false";
                }
            }
            function dispOverAllMinValue(){
                var OverAllMinValueReqObj=document.getElementById("OverAllMinValueReq");
                if(OverAllMinValueReqObj.checked){
                    OverAllMinValueReqObj.value="true"
                }else{
                    OverAllMinValueReqObj.value="false";
                }
            }

            function dispCatMaxValue(){
                var CatMaxValueReqObj=document.getElementById("CatMaxValueReq");
                if(CatMaxValueReqObj.checked){
                    CatMaxValueReqObj.value="true"
                }else{
                    CatMaxValueReqObj.value="false";
                }
            }
            function dispCatMinValue(){
                var CatMinValueReqObj=document.getElementById("CatMinValueReq");
                if(CatMinValueReqObj.checked){
                    CatMinValueReqObj.value="true"
                }else{
                    CatMinValueReqObj.value="false";
                }
            }
            function dispRowGrandTotal(){
                var CrosstabGrandTotalDisplayedObj=document.getElementById("CrosstabGrandTotalDisplayed");
                if(CrosstabGrandTotalDisplayedObj.checked){
                    CrosstabGrandTotalDisplayedObj.value="true"
                }else{
                    CrosstabGrandTotalDisplayedObj.value="false";
                }
            }
            function dispRowNetTotal(){
                var CrosstabSubTotalDisplayedObj=document.getElementById("CrosstabSubTotalDisplayed");
                if(CrosstabSubTotalDisplayedObj.checked){
                    CrosstabSubTotalDisplayedObj.value="true"
                }else{
                    CrosstabSubTotalDisplayedObj.value="false";
                }
            }
            function dispSearchReq(){
                var SearchReqObj=document.getElementById("SearchReq");
                if(SearchReqObj.checked){
                    SearchReqObj.value="true"
                }else{
                    SearchReqObj.value="false";
                }
            }
            function dispSingleViewBy()
            {
                var WrapToSingleViewBy=document.getElementById("WrapToSingleViewBy");
                if(WrapToSingleViewBy.checked)
                    WrapToSingleViewBy.value="true"
                else
                    WrapToSingleViewBy.value="false";
            }

            function transposeViewBy()
            {
                var TransposeViewBy=document.getElementById("TransposeViewBy");
                if(TransposeViewBy.checked)
                    TransposeViewBy.value="true"
                else
                    TransposeViewBy.value="false";
            }
            function drillViewBy()
            {
                 var DrillViewBy=document.getElementById("DrillViewBy");

                 if(DrillViewBy.checked)
                     DrillViewBy.value="true";
                  else
                     DrillViewBy.value="false";


            }

            function drillAcrossChanged()
            {
                var drillAcrossViewBy=document.getElementById("drillAcrossViewBy");
                if(drillAcrossViewBy.checked)
                    drillAcrossViewBy.value="true"
                else
                    drillAcrossViewBy.value="false";
            }

            function wrapCrosstabHeader()
            {
                var WrapCrosstabHeader=document.getElementById("WrapCrosstabHeader");
                if(WrapCrosstabHeader.checked)
                    WrapCrosstabHeader.value="true"
                else
                    WrapCrosstabHeader.value="false";
            }
            function disprowCount(){
                var rowCountObj=document.getElementById("rowCount");
                if(rowCountObj.checked){
                    rowCountObj.value="true"
                }else{
                    rowCountObj.value="false";
                }
              }

              function enableDrillMeasureFun(){
                var enablDrillMeas=document.getElementById("enableDrillMeasure");
                if(enablDrillMeas.checked){
                    enablDrillMeas.value="true"
                }else{
                    enablDrillMeas.value="false";
                }
              }
                  function enableTreeTableDisplay(){
                  var treeTableDisplay=document.getElementById("treeTableDisplay");
                  if(treeTableDisplay.checked){
                      treeTableDisplay.value="true";
                  }else{
                      treeTableDisplay.value="false";
                  }

              }
              function enableDisplayStTimePeriod(){
                  var displayStTimePeriod=document.getElementById("displayStTimePeriod");
                  if(displayStTimePeriod.checked){
                     displayStTimePeriod.value="true";
                  }else{
                      displayStTimePeriod.value="false";
                  }

              }
              function enableParameterDrillFun(){
                  var enableParameterDrill=document.getElementById("enableParameterDrill");
                  if(enableParameterDrill.checked){
                     enableParameterDrill.value="true";
                  }else{
                      enableParameterDrill.value="false";
                  }

              }
              function disptimeconversion(){
                  var disptimeconversionObj=document.getElementById("timeconversion");
                  if(disptimeconversionObj.checked){
                     disptimeconversionObj.value="true";
                  }else{
                      disptimeconversionObj.value="false";
                  }

              }
              function dispCategoryAvg(){
                var catAvgTotReqObj = document.getElementById("categoryAvg");
                if(catAvgTotReqObj.checked){
                    catAvgTotReqObj.value="true";
                }else{
                    catAvgTotReqObj.value="false";
                }
            }
            function addCustomTotal(){
                var addcusTotObj = document.getElementById("AddCustomTotal");
                if(addcusTotObj.checked){
                 $("#CustTotName").show();
                 $("#MappingCustTot").show();
                    addcusTotObj.value="true";
                }else{
                    addcusTotObj.value="false";
                $("#CustTotName").hide();
                $("#MappingCustTot").hide();

                }
            }
            function addshowIconTrend2(){
                  var showIconTr = document.getElementById("showIconTrend");
//                  alert(showIconTr);
                  if(showIconTr.checked){
                showIconTr.value="true";
                }else{
                    showIconTr.value="false";
                }
            }
            function dispSortOpt(){
              $("#SortOpt").toggle();
            }
           function validateOrderForView(id){
               var sorttype = $("#"+id).val();
               alert(sorttype);
           }
           function enableSerialNum(){
                  var serialnumDisplay=document.getElementById("serialnum");
                  if(serialnumDisplay.checked){
                      serialnumDisplay.value="true";
                  }else{
                      serialnumDisplay.value="false";
                  }

              }
          function enableRenameTotal(){
              var renameTotaldisplay=document.getElementById("renameTotal");
              if(renameTotaldisplay.checked){
                  renameTotaldisplay.value="true";
                  $("#renameTotalTR").show();
              }else{
                  renameTotaldisplay.value="false";
                  $("#renameTotalTR").hide();
              }
          }
          function editRename(){
            $("#RenamedTotalName").val($("#originalTotalName option:selected").text());
          }
          function enableSummarizedMeasure(id){
              if(!$("#DefineDialChartId").is(":checked")){
                  $("#"+id).attr('value','true');
              }else{
                  $("#"+id).attr('value','false');
              }
          }
          
           function enableResetColor(){
               $("#GTBgColor").empty();
                $("#SubTotalBgColor").empty();
                $("#HeaderBgColor").empty();
                var resSetColorNew=document.getElementById("reSetColor");
                if(resSetColorNew!=='null'&& resSetColorNew!=='undefined' && resSetColorNew!=='')
                    {
                if(resSetColorNew.checked){
                    resSetColorNew.value="true"
                }else{
                    resSetColorNew.value="false";
                }
          }
          }
          
           function maskZeroValue(){
              var maskZero = document.getElementById("maskZero");
                if(maskZero.checked){
                    maskZero.value="true";
                }else{
                    maskZero.value="false";
                }
          }
    //added Code by Govardhan
           function testHideMsr(){
              var maskZero = document.getElementById("hideMsrHdng");

                if(maskZero.checked){
                    maskZero.value="true";
                }else{
                    maskZero.value="false";
                }
                  alert("maskZero"+maskZero.value);
          }
     //end of Code By Govardhan
          //start of code by Nazneen for sub total deviation
          function dispSubTotalDeviation(){
                var subTotalDeviation=document.getElementById("subTotalDeviation");
                if(subTotalDeviation.checked){
                    subTotalDeviation.value="true"
                }else{
                    subTotalDeviation.value="false";
                }
            }
            function enableRowGTRename(){
                var rowGTRenamedisplay = document.getElementById("rowGTRename");
                if(rowGTRenamedisplay.checked){
                    rowGTRenamedisplay.value = "true";
                    $("#rowGTRenametr").show();
                }else{
                    rowGTRenamedisplay.value = "false";
                    $("#rowGTRenametr").hide();
                }
            }
            function editRowRename(){
            $("#RowRenamedTotalName").val($("#originalRowTotalName option:selected").text());
            }
            //end of code by Nazneen for sub total deviation

                 //added by sruthi for gtzero
           function grandTotalZero1()
            {
//                alert("grandTotalZero")
                var grandTotalZero=document.getElementById("grandTotalZero");
                if(grandTotalZero.checked)
                    {
                      grandTotalZero.value="true";
                    }else
                        {
                            grandTotalZero.value="false"
                        }
            }
            //ended by sruthi
            
            
            
             function showColor(id)
          {            
            $("#selectedId").val(id);
             var colorCode="";
             colorCode=$("#"+id).attr('colorCode');
              $("#colorsDiv").dialog('open')
            if(colorCode!=undefined && colorCode!="")
              {
                $("#color").val(colorCode)
                $("#color").css("background-color",colorCode)
               $.farbtastic("#color").setColor(colorCode)
             }
          }
          function saveSelectedColor()
          {
              var seletedTextId= $("#selectedId").val();
              var colorCode=$("#color").val();
              $("#"+seletedTextId).css("background-color",colorCode)
              $("#"+seletedTextId).attr('colorCode',colorCode);
              $("#colorsDiv").dialog('close')
          }
          function cancelColor()
          {
               $("#colorsDiv").dialog('close')
          }
          
          // Added By Mohit Gupta
          function colorToHex(color) {
               if(color != undefined){
           if (color.substr(0, 1) === '#') {
             return color;
              }      
            var digits = /(.*?)rgb\((\d+), (\d+), (\d+)\)/.exec(color);
             var red = parseInt(digits[2]);
             var green = parseInt(digits[3]);
             var blue = parseInt(digits[4]);

             var rgb = blue | (green << 8) | (red << 16);
             return digits[1] + '#' + rgb.toString(16);
               }
             };
      </script>
    </body>
</html>
<%}
            }%>

