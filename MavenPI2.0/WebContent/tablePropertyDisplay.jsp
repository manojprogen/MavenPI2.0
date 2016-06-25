<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.lang.String,prg.business.group.MeasureType,com.progen.i18n.TranslaterHelper,java.lang.Object,java.sql.*,java.util.*,prg.db.PbReturnObject,prg.business.group.MeasurePropertySet"%>
<%@page import="com.google.gson.Gson,com.progen.metadata.MeasureProperty,com.google.gson.GsonBuilder"%>


<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            MeasurePropertySet measureProperty = null;
            Locale locale = null;
            HashSet<MeasureType> measureType = null;
            Iterator iterator;
            HashMap<String, HashSet<String>> measureUnitDetails = new HashMap<String, HashSet<String>>();
            Gson gson = new GsonBuilder().serializeNulls().create();
            try {
                locale = (Locale) session.getAttribute("userLocale");
                String table_name = (String) request.getAttribute("table_name");
                String table_description = (String) request.getAttribute("table_description");
                String table_disp_name = (String) request.getAttribute("table_disp_name");
                String table_tooltip_name = (String) request.getAttribute("table_tooltip_name");
                ArrayList sources = (ArrayList) session.getAttribute("sources");
                int table_id = ((Integer) request.getAttribute("table_id")).intValue();

                PbReturnObject returnObject = (PbReturnObject) request.getAttribute("columndetails");
                String sourceslines = "";
                for (int i = 0; i < sources.size(); i++) {
                    sourceslines = sourceslines + (String) sources.get(i) + "\n";
                }
                ArrayList relatedTables = (ArrayList) request.getAttribute("relatedTables");
                measureProperty = (MeasurePropertySet) request.getAttribute("measureProperty");
                HashSet<String> measureCategory = measureProperty.getMeasureCategory();
                measureType = new HashSet<MeasureType>();
                measureType = measureProperty.getMeasureTypes();

                //<select> TranslaterHelper.getTranslatedString(tempStr, locale)
                String meaCategoryhtml = "";
                iterator = measureCategory.iterator();
                while (iterator.hasNext()) {
                    String tempStr = iterator.next().toString();
                    meaCategoryhtml += "<option value=" + tempStr + "> " + TranslaterHelper.getTranslatedString(tempStr, locale) + " </option>";
                }

                String measureTypeHtml = "";
                iterator = measureType.iterator();
                while (iterator.hasNext()) {
                    MeasureType measureType1 = (MeasureType) iterator.next();
                    measureTypeHtml += "<option value=" + measureType1.getMeasureType() + "> " + TranslaterHelper.getTranslatedString(measureType1.getMeasureType(), locale) + " </option>";
                    measureUnitDetails.put(measureType1.getMeasureType(), measureType1.getMeasureUnits());
                }
                session.setAttribute("measureUnitDetails", measureUnitDetails);
                MeasureProperty property = null;
                int var=0;
String contextPath=request.getContextPath();
                //////////////////////////////////////////////////////////////////////////////////.println.println(" relatedTables "+relatedTables);
%>


<HTML>
    <HEAD>
        <%--
		<script type="text/javascript" src="tabs/js/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="tabs/js/jquery-ui-1.7.2.custom.min.js"></script>
                <script type="text/javascript" src="javascript/ui.tabs.js"></script>
                <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
                 <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        --%>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.slider.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
      
        <style>
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;

                background-color:#b4d9ee;
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
    </HEAD>

    <BODY>


        <!-- Tabs -->
        <div id="tabs" style="width:98%">
            <ul style="width:99%">
                <li><a href="#tables" style="color: black;">Tables</a></li>
                <li><a href="#columns" style="color: black;">Columns</a></li>
                <li><a href="#columnsproperties" style="color: black;"><%= TranslaterHelper.getTranslatedString("COLUMNS_PROPERTIES", locale)%></a></li>
            </ul>
            <div id="tables" style="width:99%">

                <form name="myForm">
                      <%-- <img id="imgId" align="middle" width="75px" height="75px" style="position: relative; top: 120px; left: 30px;" src="images/ajax.gif"--%>
                    <table border="0" style="border-width:thin;width:100%">
                        <tr>
                            <td width="50%" class="myHead" >
                                Table Name
                            </td>
                            <td width="50%" class="myHead">
                                <%=table_name%>
                            </td>
                        </tr>
                        <tr>
                            <td width="50%" class="myHead" >
                                Table Display Name
                            </td>
                            <td><Input name="tableDispName" type="text" value="<%=table_disp_name%>"></td>

                        </tr>
                        <tr>
                            <td width="50%" class="myHead" >
                                Table Tooltip Name
                            </td>
                            <td><Input name="tableTooltipName" type="text" value="<%=table_tooltip_name%>"></td>
                        </tr>
                    </table>
                    <!--2) Table Sources-->
                    <input type="hidden" name="tableid" value="<%=table_id%>">
                    <table border="0" style="border-width:thin;width:100%">
                        <tr>
                            <td class="myHead" align="center" width="100%">
                                Table Sources
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" height="100px" width="100%">
                                <table width="100%"><tr><td width="100%">
                                            <%=sourceslines%>
                                        </td></tr></table>
                            </td>
                        </tr>
                    </table>
                    <!--3) Joins-->
                    <table border="0" style="border-width:thin" width="100%" id="test">
                        <tr>
                            <td class="myHead" width="100%" align="center">
                                Related Table Level 1
                            </td>
                        </tr>
                        <tr>
                            <td height="100px" width="100%">
                                <%--added by susheeela start --%>
                                <table>
                                    <%for (int m = 0; m < relatedTables.size(); m++) {%>
                                    <tr><td><%=relatedTables.get(m).toString()%></td> </tr>
                                    <%}%>
                                </table>
                                <%-- added by susheela over --%>
                            </td>
                        </tr>
                    </table>
                    <!--4) Description-->
                    <table border="0" style="border-width:thin" width="100%">
                        <tr>
                            <td class="myHead" width="100%" align="center">
                                Description
                            </td>
                        </tr>
                        <tr>
                            <td height="100%" width="100%">
                                <textarea name="tabledesc" style="height:100px;width:100%;border:0px;overflow:auto" ><%=table_description%></textarea>
                            </td>
                        </tr>
                    </table>
                </form>
                <center><input type="submit" class="navtitle-hover" style="width:auto;color:black" value="Save" id="btnn" onclick="saveTables()">&nbsp;&nbsp;<input type="button"  class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelBuckets()"></center>
            </div>
            <div id="columns" style="width:99%;height:60%">
                <form name="columnForm" id="columnForm" method="post" action="">
                    <table style="border-width:thin" border="0">
                        <tr>
                            <th class="myHead">Column Name</th>
                            <th class="myHead">Display Name</th>
                            <th class="myHead">Column Type</th>
                            <th class="myHead">Default Aggregation</th>
                            <th class="myHead">Role Flag</th>
                        </tr>
                        <%
                                        for (int i = 0; i < returnObject.getRowCount(); i++) {

                        %>

                        <tr>
                            <td style="border-width:thin"><%= returnObject.getFieldValueString(i, 1)%></td>
                            <%-- modified by krishan start --%>
                            <%String desc = returnObject.getFieldValueString(i, 4);
                             String descr=null;
                                                                        if (desc == null) {
                                                                            descr = "";
                                                                        }
                                                                       else{
                                                                               String[] arr = desc.split(" ");
                                                                              StringBuffer sb = new StringBuffer();

                                                                      for (int count = 0; count < arr.length; count++) {
                                                                            sb.append(Character.toUpperCase(arr[count].charAt(0))).append(arr[count].substring(1)).append(" ");
                                                                    }
                                                                                      descr= sb.toString().trim();
                                                                       }
                            %>
                            <td><Input name="<%=returnObject.getFieldValueString(i, 0) + ":desc"%>" type="text" value="<%=descr%>"></td>
                                <%-- modified by krishan over --%>

                            <td><%= returnObject.getFieldValueString(i, 3)%></td>
                            <%
                                                                        String dataAggr = returnObject.getFieldValueString(i, 5);
                                                                        String selectedAggr = "selected";
                                                                        if (dataAggr == null || dataAggr.equalsIgnoreCase("null")) {
                                                                            dataAggr = "";
                                                                            selectedAggr = "selected";
                                                                        }
                                                                        // ////////////////////////////////////////////////////////////////////////////////////.println.println(" dataAggr -- "+dataAggr);
                            %>
                            <%-- modified by susheela start --%>
<!--                             modified by krishan pratap -->
                            <td align="right"><select name="<%=returnObject.getFieldValueString(i, 0)%>">
                                    <%if (dataAggr.equalsIgnoreCase("sum")) {
                                    %>
                                    <option <%=selectedAggr%> value="sum">sum</option>
                                    <%} else if (!dataAggr.equalsIgnoreCase("sum")) {%>
                                    <option value="sum">sum</option>
                                    <%   }
                                                                        else if("float".equalsIgnoreCase(returnObject.getFieldValueString(i, 3))||"int".equalsIgnoreCase(returnObject.getFieldValueString(i, 3))||"Numeric".equalsIgnoreCase(returnObject.getFieldValueString(i, 3))||"BIGINT ".equalsIgnoreCase(returnObject.getFieldValueString(i, 3))){  %>
                                                                       <option <%=selectedAggr%> value="sum">sum</option>
                                                                        <% }
                                                                                if (dataAggr.equalsIgnoreCase("count")) {
                                    %>
                                    <option <%=selectedAggr%> value="count">count</option>
                                    <%} else if (!dataAggr.equalsIgnoreCase("count")) {%>
                                    <option value="count">count</option>
                                    <%   }
                                                                                if (dataAggr.equalsIgnoreCase("avg")) {
                                    %>
                                    <option <%=selectedAggr%> value="avg">avg</option>
                                    <%
                                                                                                                    } else if (!dataAggr.equalsIgnoreCase("avg")) {%>
                                    <option value="avg">avg</option>
                                    <%}%>
                                    <%if (dataAggr.equalsIgnoreCase("min")) {
                                    %>
                                    <option <%=selectedAggr%> value="min">min</option>
                                    <%
                                    } else if (!dataAggr.equalsIgnoreCase("min")) {%>
                                    <option value="min">min</option>
                                    <%}%>
                                    <%if (dataAggr.equalsIgnoreCase("max")) {
                                    %>
                                    <option <%=selectedAggr%> value="max">max</option>
                                    <%
                                    } else if (!dataAggr.equalsIgnoreCase("max")) {%>
                                    <option value="max">max</option>
                                    <%}%>
                                    <%if (dataAggr.equalsIgnoreCase("COUNTDISTINCT")) {
                                    %>
                                    <option <%=selectedAggr%>  value="COUNTDISTINCT">COUNTDISTINCT</option>
                                    <%
                                    } else {%>
                                    <option value="COUNTDISTINCT">COUNTDISTINCT</option>
                                    <%}%>
                                    <%if (dataAggr.equalsIgnoreCase("")) {
                                    %>
                                    <option <%=selectedAggr%> value="null"></option>
                                    <%} else if (!dataAggr.equalsIgnoreCase("")) {
                                    %>
                                    <option value="null"></option>
                                    <%}%>
                                    <%-- modified by susheela over --%>
                                </select></td>
                                <%String roleFlag = returnObject.getFieldValueString(i, 6);
                                                                            String isChecked = "true";
                                                                            if (roleFlag != null && roleFlag.equalsIgnoreCase("Y")) {
                                                                                isChecked = "checked";
                                                                            } else {
                                                                                isChecked = "";
                                                                            }
                                                                            // ////////////////////////////////////////////////////////////////////////////////////.println.println(" isChecked-- "+isChecked);
                                %>
                            <Td><Input id="<%=returnObject.getFieldValueString(i, 0) + ":roleFl"%>" name="<%=returnObject.getFieldValueString(i, 0) + ":roleFl"%>" <%=isChecked%> type="checkbox"></Td>


                            <%--
                             <%if(rs.getString(4).equalsIgnoreCase("Number")){%>
                             <td align="right"><select name="<%=rs.getString(1)%>"><option value="sum">sum</option><option value="count">count</option><option value="avg">avg</option><option value="min">min</option><option value="max">max</option><option value="null">     </option></select></td>
                             <%}else{%>
                             <td align="right"><select name="<%=rs.getString(1)%>"><option value="sum">sum</option><option value="count">count</option><option value="avg">avg</option><option value="min">min</option><option value="max">max</option><option value="null" selected>       </option></select>
                             <%}%>
                            --%>
                        </tr>

                        <%}%>
                    </table>
                    <input type="hidden" name="tableid" value="<%=table_id%>">
                    <center><input type="button" class="navtitle-hover" style="width:auto" name="saveColumns" value="Save" onclick="saveCols()">&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelBuckets()"></center>
                </form></div>
                   

            <form id="columnspropertiesFrm" id="columnspropertiesFrm" method="post" action="">
                <div id="columnsproperties" style="width:96%;overflow:auto" >


                   
                    <table id="sortid" class="tablesorter">
                        <thead ><tr >
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("COLUMN_NAME", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_CATEGORY", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_TYPE", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_UNITS", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_lABEL", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_ROUNDING_TYPE", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_ROUNDING", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_PREFIX_DISPLAY", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_SUFFIX_DISPLAY", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("VALUE_IN", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("DEFAULT_DISPLAY_IN", locale)%></th>
                                <th class="myHead"><%= TranslaterHelper.getTranslatedString("MEASURE_DISPLAY", locale)%></th>

                            </tr></thead><tbody style="overflow: auto; height: 400px;" >

                            <% for (int colI = 0; colI < returnObject.getRowCount(); colI++) {


                                                //                COLUMN_PROPERTIES
                                                MeasureProperty meaProperty = null;
                                                // 
                                                if (returnObject.getFieldValueClobString(colI, "COLUMN_PROPERTIES") != null) {
                                                    meaProperty = gson.fromJson(returnObject.getFieldValueClobString(colI, "COLUMN_PROPERTIES"), MeasureProperty.class);

                                                } else {
                                                    meaProperty = null;
                                                }


                            %>

                            <tr>
                                <td style="border-width:thin"><%= returnObject.getFieldValueString(colI, 1)%></td>
                        <Input name="<%=returnObject.getFieldValueString(colI, 0)%>" type="hidden" value="<%=returnObject.getFieldValueString(colI, 0)%>">
                        <td style="border-width:thin">
                            <select id="measCategory_<%=colI%>" name="measCategory_<%=colI%>" >
                                <option value="" >--select--</option>
                                <%if (meaProperty != null && meaProperty.getMeasureCategory() != null) {%>
                                <%=meaCategoryhtml.replace("<option value=" + meaProperty.getMeasureCategory() + "> " + TranslaterHelper.getTranslatedString(meaProperty.getMeasureCategory(), locale) + " </option>", "<option value=" + meaProperty.getMeasureCategory() + " selected > " + TranslaterHelper.getTranslatedString(meaProperty.getMeasureCategory(), locale) + " </option>")%>
                                <%} else {%>
                                <%=meaCategoryhtml.replace("<option value=STD> Standard </option>", "<option value='STD' selected > Standard </option>")%>

                                <%}%>

                            </select>
                        </td>
                        <td style="border-width:thin">
                            <select id="measureType_<%=colI%>" name="measureType_<%=colI%>" onchange="getMeasureUnits('measureType_<%=colI%>')">
                                <option value="">--select--</option>
                                <%if (meaProperty != null && meaProperty.getMeasureType() != null) {%>
                                <%=measureTypeHtml.replace("<option value=" + meaProperty.getMeasureType() + "> " + TranslaterHelper.getTranslatedString(meaProperty.getMeasureType(), locale) + " </option>", "<option value=" + meaProperty.getMeasureType() + " selected> " + TranslaterHelper.getTranslatedString(meaProperty.getMeasureType(), locale) + " </option>")%>
                                <%} else {%>
                                <%=measureTypeHtml%>
                                <%}%>
                            </select>
                        </td>
                        <td>  <%if (meaProperty != null && meaProperty.getMeasureUnits() != null) {
                                 boolean check = meaProperty.ismeasureUnit(measureUnitDetails);
                                 if (check == true) {%>
                            <select id="measureUnit_<%=colI%>" name="measureUnit_<%=colI%>" style="width:155px" onclick="getMeasureUnits('measureType_<%=colI%>')">
                                <!--                                <option value="">--Select--</option>-->
                                <option value="<%=meaProperty.getMeasureUnits()%>" selected>  <%=TranslaterHelper.getTranslatedString(meaProperty.getMeasureUnits(), locale)%></option>

                            </select>
                            <%} else {%>
                            <input type="text" id="measureUnit_<%=colI%>" name="measureUnit_<%=colI%>" value="<%=meaProperty.getMeasureUnits()%>"  style="width:155px">

                            <% }
                            } else {%>
                            <select id="measureUnit_<%=colI%>" name="measureUnit_<%=colI%>"  style="width:155px">
                                <option value="">--Select--</option>-->
                            </select>
                            <%}%>

                        </td>
                        <td>  <%if (meaProperty != null && meaProperty.getMeasureLable() != null) {%>
                            <input type="text" id="measureLable_<%=colI%>" value="<%=meaProperty.getMeasureLable()%>" name="measureLable_<%=colI%>">
                            <%} else {%>
                            <input type="text" id="measureLable_<%=colI%>" value="" name="measureLable_<%=colI%>"></td>
                            <%}%>

                        <td> <select id="meaRoundingTypeSel_<%=colI%>" name="meaRoundingTypeSel_<%=colI%>"><option value="" ><%= TranslaterHelper.getTranslatedString("SELECT", locale)%></option>
                                <%if (meaProperty != null && meaProperty.getMeasureRoundingType() != null) {
                                         if (meaProperty.getMeasureRoundingType().equalsIgnoreCase("CURRENCY_INDEPENDENT")) {%>
                                <option value="CURRENCY_INDEPENDENT" selected><%= TranslaterHelper.getTranslatedString("CURRENCY_INDEPENDENT", locale)%></option>
                                <option value="CURRENCY_DEPENDENT"><%= TranslaterHelper.getTranslatedString("CURRENCY_DEPENDENT", locale)%></option>
                                <option value="OTHER"><%= TranslaterHelper.getTranslatedString("OTHER", locale)%></option>
                                <% } else if (meaProperty.getMeasureRoundingType().equalsIgnoreCase("CURRENCY_DEPENDENT")) {%>
                                <option value="CURRENCY_INDEPENDENT"><%= TranslaterHelper.getTranslatedString("CURRENCY_INDEPENDENT", locale)%></option>
                                <option value="CURRENCY_DEPENDENT" selected><%= TranslaterHelper.getTranslatedString("CURRENCY_DEPENDENT", locale)%></option>
                                <option value="OTHER"><%= TranslaterHelper.getTranslatedString("OTHER", locale)%></option>
                                <% } else {%>
                                <option value="CURRENCY_INDEPENDENT"><%= TranslaterHelper.getTranslatedString("CURRENCY_INDEPENDENT", locale)%></option>
                                <option value="CURRENCY_DEPENDENT"><%= TranslaterHelper.getTranslatedString("CURRENCY_DEPENDENT", locale)%></option>
                                <option value="OTHER"><%= TranslaterHelper.getTranslatedString("OTHER", locale)%></option>
                                <% }%>

                                <%} else {%>
                                <option value="CURRENCY_INDEPENDENT"><%= TranslaterHelper.getTranslatedString("CURRENCY_INDEPENDENT", locale)%></option>
                                <option value="CURRENCY_DEPENDENT"><%= TranslaterHelper.getTranslatedString("CURRENCY_DEPENDENT", locale)%></option>
                                <option value="OTHER"><%= TranslaterHelper.getTranslatedString("OTHER", locale)%></option>
                                <%}%>

                            </select>
                        </td>
                        <%if (meaProperty != null && meaProperty.getMeasureRounding() != 0) {%>
                        <td><input type="text" id="measuRoundin_<%=colI%>" name="measuRoundin_<%=colI%>" value="<%= meaProperty.getMeasureRounding()%>" onkeypress="return isNumber()">
                        </td>
                        <%} else {%>
                        <td>
                            <input type="text" id="measuRoundin_<%=colI%>" name="measuRoundin_<%=colI%>" value="" onkeypress="return isNumber()">
                        </td>
                        <%}
                             if (meaProperty != null && meaProperty.getMeasurePrefixDisplay() != null) {%>
                        <td><input type="text" id="mesPreFix_<%=colI%>" name="mesPreFix_<%=colI%>" value="<%=meaProperty.getMeasurePrefixDisplay()%>">
                        </td>
                        <%} else {%>
                        <td><input type="text" id="mesPreFix_<%=colI%>" name="mesPreFix_<%=colI%>" value="">
                        </td>
                        <%}
                             if (meaProperty != null && meaProperty.getMeasuresuffixDisplay() != null) {%>
                        <td><input type="text" id="mesPreFix_<%=colI%>" name="messufFix_<%=colI%>" value="<%=meaProperty.getMeasuresuffixDisplay()%>">
                        </td>
                        <%} else {%>
                        <td><input type="text" id="mesPreFix_<%=colI%>" name="messufFix_<%=colI%>" value="">
                        </td>
                        <%}
                             if (meaProperty != null && meaProperty.getValueIn() != null) {%>
                        <td><input type="text" id="textvalueIn_<%=colI%>" name="textvalueIn_<%=colI%>" value="<%=meaProperty.getValueIn()%>" ></td>
                            <%} else {%>
                        <td><input type="text" id="textvalueIn_<%=colI%>" name="textvalueIn_<%=colI%>" value="" ></td>
                            <%}
                                 if (meaProperty != null && meaProperty.getDefaultDisplayin() != null) {%>
                        <td><input type="text" id="defaultDisplayIn_<%=colI%>" name="defaultDisplayIn_<%=colI%>" value="<%=meaProperty.getDefaultDisplayin()%>">
                        </td>
                        <%} else {%>
                        <td><input type="text" id="defaultDisplayIn_<%=colI%>" name="defaultDisplayIn_<%=colI%>" value="">
                        </td>
                        <%}%>
                        <%if (meaProperty != null && meaProperty.getMeasureDisplay() != null) {%>
                        <td><input type="text" id="measureDisplay_<%=colI%>" name="measureDisplay_<%=colI%>" value="<%=meaProperty.getMeasureDisplay()%>">
                        </td>
                        <%} else {%>
                        <td><input type="text" id="measureDisplay_<%=colI%>" name="measureDisplay_<%=colI%>" value="">
                        </td>
                        <%}%>
                        </tr>
                        <%}%></tbody>
                    </table>
                    <table align="center">
                        <tr>
                            <td>
                                <input type="button" id="saveColumnPro" name="saveColumnPro" value="<%= TranslaterHelper.getTranslatedString("SAVE", locale)%>" onclick="saveColumnProperties()" style="width: 140px; height: 25px; font-size: medium" class="navtitle-hover">
                            </td>
                        </tr>

                    </table>

                </div>
                <input type="hidden" name="tableid" value="<%=table_id%>">
                
            </form> 

        </div>
                  <script type="text/javascript">
            $(function(){



                // Tabs
                //$('#tabs').tabs();
                

                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});


                // Dialog

                $('#dialog').dialog({
                    autoOpen: false,
                    width: 600,
                    buttons: {
                        "Ok": function() {
                            $(this).dialog("close");
                        },
                        "Cancel": function() {
                            $(this).dialog("close");
                        }
                    }
                });

                // Dialog Link
                $('#dialog_link').click(function(){
                    $('#dialog').dialog('open');
                    return false;
                });

                // Datepicker
                $('#datepicker').datepicker({
                    inline: true
                });

                // Slider
                $('#slider').slider({
                    range: true,
                    values: [17, 67]
                });
                $("#sortid").tablesorter( {headers : {0:{sorter:false}}} );


                
                // Progressbar
                <%--$("#progressbar").progressbar({
                    value: 20
                });--%>
                


                //hover states on the static widgets
                $('#dialog_link, ul#icons li').hover(
                function() { $(this).addClass('ui-state-hover'); },
                function() { $(this).removeClass('ui-state-hover'); }
            );
                

            });

            function disableButton()
            {
                document.getElementById('btnn').disabled = false;

                // alert(document.getElementById('btnn').disabled)
            }
            function saveTables()
            {
                document.myForm.action = "saveBusinessTables.do";
                document.myForm.submit();
               alert("Successfully Updated")

                window.location.reload(true);
                parent.parentCancelTableProperties1();
            }

            function cancelBuckets()
            {
                //alert("kkk");
                parent.parentCancelTableProperties();
            }

            function saveCols()
            {
            $.post("<%=request.getContextPath()%>/updateTableColumns.do", $("#columnForm").serialize(),function(data)
                {
                 alert("Successfully Updated...")
                });
                parent.parentCancelTableProperties();
            }
            function saveColumnProperties(){
                $.post("<%=request.getContextPath()%>/businessgroupeditaction.do?groupdetails=saveColumnProperties", $("#columnspropertiesFrm").serialize(),function(data){});
                parent.parentCancelTableProperties();

            }

            function getMeasureUnits(valID){
                var measureType=$("#"+valID).val()

                var idval=valID.replace("measureType_", "measureUnit_")
                var measureunit=$("#"+idval).val()
                //                alert("measureunit\t"+measureunit)
                $.ajax({
                    url: 'businessgroupeditaction.do?groupdetails=getMeasureUnits&measureType='+measureType,
                    success:function(data){
                        // alert(data)
                        //                       </option><option value="EUR"> EUR </option>
                        data=data.replace("<option value="+measureunit+"> "+measureunit+" </option>", "<option value="+measureunit+" selected > "+measureunit+" </option>", "gi")
                        if(data==""){

                            document.getElementById(idval).parentNode.innerHTML=" <input type='text' id='"+idval+"' name='"+idval+"'  value=''>"
                        }else{

                            document.getElementById(idval).parentNode.innerHTML="<select id='"+idval+"' name='"+idval+"'style='width:155px'>"+data+"</select>"
                            //                          $("#"+idval).html(data)
                        }


                    }

                });

                //  alert("selectmeasuType\t"+Map.selectmeasuType.get(obj.value))





            }
        </script>

        <%} catch (Exception e) {
                        e.printStackTrace();
                    }%>

    </BODY>
</HTML>
