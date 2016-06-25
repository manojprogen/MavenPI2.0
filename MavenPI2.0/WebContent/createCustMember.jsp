<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,prg.db.PbReturnObject,prg.db.PbDb,prg.db.Container,java.util.HashMap,java.util.ArrayList,java.sql.*,utils.db.ProgenConnection" %>
<link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
<% String contextPath=request.getContextPath();%>
<html>
    <head>
        <title></title>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
<!--        <script type="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
        <!--addeb by bharathi reddy fro search option-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
    </head>
    <%            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
             if (request.getSession().getAttribute("loadDialogs") != null && request.getSession().getAttribute("loadDialogs").equals("true")) {
            String reportId = (String) request.getSession().getAttribute("reportId");

            String dbType = "";
            if (session.getAttribute("MetadataDbType") != null) {
                dbType = (String) session.getAttribute("MetadataDbType");
            }
            try {
                PbDb pbdb = new PbDb();
                Container container = null;
                HashMap ParametersHashMap = null;
                HashMap TableHashMap = null;
                HashMap GraphHashMap = null;
                ArrayList REP_Elements = new ArrayList();
                ArrayList CEP_Elements = new ArrayList();
                ArrayList REP_Elements_names = new ArrayList();
                ArrayList CEP_Elements_names = new ArrayList();
                ArrayList measures = new ArrayList();
                ArrayList measuresNames = new ArrayList();
                ArrayList params = new ArrayList();
                ArrayList params_names = new ArrayList();
                String folderIds = (String) request.getSession().getAttribute("folderIds");
                // ////////////////////////////////////////////////.println.println("folderIds===" + folderIds);
                //String reportId = request.getParameter("reportId");
                //////////////////////////////////////////////.println("reportId---" + reportId);
                HashMap map = new HashMap();
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                container = (prg.db.Container) map.get(reportId);
                ParametersHashMap = container.getParametersHashMap();
                TableHashMap = container.getTableHashMap();
                //////////////////////////////////////////////.println("tableHashMap---" + TableHashMap);
                //////////////////////////////////////////////.println(" ParametersHashMap" + ParametersHashMap);
                if (ParametersHashMap != null) {
                    if (ParametersHashMap.get("Parameters") != null) {
                        params = (ArrayList) ParametersHashMap.get("Parameters");
                    }
                    if (ParametersHashMap.get("ParametersNames") != null) {
                        params_names = (ArrayList) ParametersHashMap.get("ParametersNames");
                    }


                }
                // ArrayList selectedElementIds=new ArrayList();
                // ArrayList selectedElementIdsNaems=new ArrayList();
                String elementIdsList = "";
                if (TableHashMap != null) {
                    if (TableHashMap.get("REP") != null) {
                        REP_Elements = (ArrayList) TableHashMap.get("REP");
                        REP_Elements_names = (ArrayList) TableHashMap.get("REPNames");
                    } /*else {
                    if (params != null && params.size() != 0) {
                    REP_Elements.add(params.get(0));
                    REP_Elements_names.add(params_names.get(0));
                    }
                    }*/
                    if (TableHashMap.get("CEP") != null) {
                        CEP_Elements = (ArrayList) TableHashMap.get("CEP");
                        CEP_Elements_names = (ArrayList) TableHashMap.get("CEPNames");
                    }
                    if (TableHashMap.get("Measures") != null && TableHashMap.get("MeasuresNames") != null) {
                        measures = (ArrayList) TableHashMap.get("Measures");
                        measuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
                    }

                    if (REP_Elements.size() > 0) {
                        for (int i = 0; i < REP_Elements.size(); i++) {
                            //selectedElementIds.add(REP_Elements.get(i));
                            //  selectedElementIdsNaems.add(REP_Elements_names.get(i));
                            if (!String.valueOf(REP_Elements.get(i)).equalsIgnoreCase("TIME")) {
                                // elementIdsList += "," + REP_Elements.get(i);
                                }
                        }
                    }

                    if (CEP_Elements.size() > 0) {
                        for (int i = 0; i < CEP_Elements.size(); i++) {
                            //  selectedElementIds.add(CEP_Elements.get(i));
                            //   selectedElementIdsNaems.add(CEP_Elements_names.get(i));
                            if (!String.valueOf(REP_Elements.get(i)).equalsIgnoreCase("TIME")) {
                                //  elementIdsList += "," + CEP_Elements.get(i);
                                }
                        }
                    }
                    if (measures.size() > 0) {

                        for (int i = 0; i < measures.size(); i++) {
                            //  selectedElementIds.add(measures.get(i));
                            //   selectedElementIdsNaems.add(measuresNames.get(i));
                            elementIdsList += "," + measures.get(i);
                        }
                    }

                }
                PbReturnObject pbro = null;
                String isSelected = "NO";
                if (!"".equalsIgnoreCase(elementIdsList)) {
                    elementIdsList = elementIdsList.substring(1);
                    String selecteditemsQuery = "";
                    if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {

                        selecteditemsQuery = "select DISTINCT element_id, isnull(disp_name,sub_folder_type)," + "isnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, " + "user_col_type,actual_col_formula,folder_id,isnull(BUSS_COL_NAME,user_col_name),isnull(AGGREGATION_TYPE,'0')" + " from PRG_USER_ALL_INFO_DETAILS where element_id in(" + elementIdsList + ")";
                    } else {
                        selecteditemsQuery = "select DISTINCT element_id, nvl(disp_name,sub_folder_type)," + "nvl(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, " + "user_col_type,actual_col_formula,folder_id,nvl(BUSS_COL_NAME,user_col_name),nvl(AGGREGATION_TYPE,'0')" + " from PRG_USER_ALL_INFO_DETAILS where element_id in(" + elementIdsList + ")";

                    }

                    //////////////////////////////////////////////.println("selecteditemsQuery---"+selecteditemsQuery);
                    pbro = pbdb.execSelectSQL(selecteditemsQuery);
                    isSelected = "YES";
                }

                String Query = "";
                String Query1 = "";
                String Query2 = "";
                if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {

                    Query = "select DISTINCT isnull(disp_name,sub_folder_type), connection_id,folder_name  from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type='Facts'  and disp_name not in('Calculated Facts','Formula') ";
                    Query1 = "select DISTINCT element_id, isnull(disp_name,sub_folder_type),isnull(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, user_col_type,actual_col_formula,folder_id,isnull(BUSS_COL_NAME,user_col_name),isnull(AGGREGATION_TYPE,'0') from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type='Facts' and ref_element_id=element_id and AGGREGATION_TYPE not in('calculated','summarised' )order by isnull(BUSS_COL_NAME,user_col_name),ref_element_id";
                    Query2 = "select DISTINCT folder_name  from prg_user_all_info_details where folder_id in (" + folderIds + ")";

                } else {

                    Query = "select DISTINCT nvl(disp_name,sub_folder_type), connection_id,folder_name  from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type='Facts'  and disp_name not in('Calculated Facts','Formula') ";
                    Query1 = "select DISTINCT element_id, nvl(disp_name,sub_folder_type),nvl(USER_COL_DESC, user_col_name) as column_name,ref_element_id as column_id, user_col_type,actual_col_formula,folder_id,nvl(BUSS_COL_NAME,user_col_name),nvl(AGGREGATION_TYPE,'0') from PRG_USER_ALL_INFO_DETAILS where folder_id in (" + folderIds + ") and sub_folder_type='Facts' and ref_element_id=element_id and AGGREGATION_TYPE not in('calculated','summarised' )order by nvl(BUSS_COL_NAME,user_col_name),ref_element_id";
                    Query2 = "select DISTINCT folder_name  from prg_user_all_info_details where folder_id in (" + folderIds + ")";




                }
                String tableNameList = "";
                String connectionId = "";
                String eleList = "";
                // //////////////////////////////////////////////.println("Query---" + Query);
                //////////////////////////////////////////////.println("Query1---" + Query1);
                // //////////////////////////////////////////////.println("Query2---" + Query2);
                PbReturnObject pbro1 = pbdb.execSelectSQL(Query);
                PbReturnObject pbro2 = pbdb.execSelectSQL(Query1);
                PbReturnObject pbro3 = pbdb.execSelectSQL(Query2);
    %>
    <body onload="javascript:activateDivs('<%=isSelected%>')">
        <form id="f1" name="myForm">

            <table align="center" width="100%" border="0" >
                <tr style="width:100%">
                    <td style="width:100%">
                        <table style="width:100%">

                            <tr >
                                <td >
                                    <label class="label"><b>Name</b></label>
                                </td>

                                <td>
                                    <input type="text" name="columnName" id="columnName" size="30">
                                </td>
                                <td width="100%" align="center" valign="top">
                                    <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveCustMember('<%=tableNameList%>','<%=connectionId%>')">
                                </td>

                            </tr>

                        </table>
                    </td>

                </tr>
            </table>


            <table align="center" width="100%" border="0" >
                <tr style="width:100%">
                    <td style="width:100%">
                        <table style="width:100%" border="1px solid">
                            <tr style="width:100%">

                                <td width="50%" valign="top" class="draggedTable1">
                                    <div style="height:15px" class="ui-state-default draggedDivs ui-corner-all"><font size="1" style="font-weight:bold">Drag Columns From here</font></div>
                                    <div id="afterTable" style="height:150px;overflow-y:auto;display:none">
                                        <ul>

                                            <%
                    if (pbro != null) {
                        for (int i = 0; i < pbro.getRowCount(); i++) {

                            String colType = pbro.getFieldValueString(i, 4);
                            //   //////////////////////////////////////////////////.println("colname==="+pbro2.getFieldValueString(j,2));

                                            %>

                                            <li style="background:white;color:transparent"><img src='icons pinvoke/report.png'/>

                                                <span class="myDragTabs" class="ui-state-default"  style="color:#000" id="<%=pbro.getFieldValueString(i, 0)%>^<%=pbro.getFieldValueString(i, 2)%>" ><%=pbro.getFieldValueString(i, 2)%></span>
                                                <%if (colType.equalsIgnoreCase("NUMBER")) {%>
                                                <label class="label" style="color:green"><b>{N}</b></label>
                                                <%} else if (colType.equalsIgnoreCase("VARCHAR2")) {%>
                                                <label class="label" style="color:green"><b>{T}</b></label>
                                                <%} else if (colType.equalsIgnoreCase("DATE")) {%>
                                                <label class="label" style="color:green"><b>{D}</b></label>
                                                <%} else if (colType.equalsIgnoreCase("calculated")) {%>
                                                <label class="label" style="color:green"><b>{C}</b></label>
                                                <%} else if (colType.equalsIgnoreCase("summarised") || colType.equalsIgnoreCase("SUMMARIZED")) {%>
                                                <label class="label" style="color:green"><b>{S}</b></label>
                                                <%}%>
                                            </li>
                                            <%}
                    }
                                            %>
                                        </ul>
                                    </div>


                                    <div style="height:150px;overflow-y:auto;display:none" id="beforeTable">
                                        <ul id="myList3" class="filetree treeview-famfamfam">
                                            <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                                <%for (int k = 0; k < pbro3.getRowCount(); k++) {%>
                                                <ul>

                                                    <li class="closed"><img src="icons pinvoke/folder-horizontal.png"/><span ><font size="1px" face="verdana"><%=pbro3.getFieldValueString(k, 0)%></font></span>

                                                        <% for (int i = 0; i < pbro1.getRowCount(); i++) {

        connectionId = String.valueOf(pbro1.getFieldValueInt(i, 1));
        if (pbro3.getFieldValueString(k, 0).equalsIgnoreCase(pbro1.getFieldValueString(i, 2))) {
                                                        %>
                                                        <ul>
                                                            <li class="closed"><img src='icons pinvoke/table.png'></img><span ><font size="1px" face="verdana"><%=pbro1.getFieldValueString(i, 0)%></font></span>
                                                                <ul>
                                                                    <%for (int j = 0; j < pbro2.getRowCount(); j++) {
                                                                        if (pbro1.getFieldValueString(i, 0).equalsIgnoreCase(pbro2.getFieldValueString(j, 1))) {


                                                                            String colType = pbro2.getFieldValueString(j, 4);
                                                                            //   //////////////////////////////////////////////////.println("colname==="+pbro2.getFieldValueString(j,2));

                                                                    %>

                                                                    <li><img src='icons pinvoke/report.png'/>

                                                                        <span class="myDragTabs" class="ui-state-default" id="<%=pbro2.getFieldValueString(j, 0)%>^<%=pbro2.getFieldValueString(j, 2)%>"><%=pbro2.getFieldValueString(j, 2)%></span>
                                                                        <%if (colType.equalsIgnoreCase("NUMBER")) {%>
                                                                        <label class="label" style="color:green"><b>{N}</b></label>
                                                                        <%} else if (colType.equalsIgnoreCase("VARCHAR2")) {%>
                                                                        <label class="label" style="color:green"><b>{T}</b></label>
                                                                        <%} else if (colType.equalsIgnoreCase("DATE")) {%>
                                                                        <label class="label" style="color:green"><b>{D}</b></label>
                                                                        <%} else if (colType.equalsIgnoreCase("CALCULATED")) {%>
                                                                        <label class="label" style="color:green"><b>{C}</b></label>
                                                                        <%} else if (colType.equalsIgnoreCase("SUMMARIZED") || colType.equalsIgnoreCase("SUMMARISED")) {%>
                                                                        <label class="label" style="color:green"><b>{S}</b></label>
                                                                        <%}%>
                                                                    </li>
                                                                    <%
    }%>

                                                                    <%
                                                                    }%>
                                                                </ul>
                                                            </li>
                                                        </ul>
                                                        <%

        }

    }%>

                                                    </li>
                                                </ul>
                                                <%}%>
                                            </li>
                                        </ul>
                                    </div>
                                </td>


                                <td id="dropTabs" width="50%" valign="top">
                                    <div style="height:15px" class="ui-state-default draggedDivs ui-corner-all"><font size="1" style="font-weight:bold">Drag Columns to here</font></div>
                                    <div style="height:150px;overflow-y:auto" id="draggedcolumn">
                                        <ul id="sortable">
                                        </ul>
                                    </div>
                                </td>



                            </tr>
                        </table><table>
                            <tr>
                                <td>

                                    <div id="moreColumns" style="display:none">
                                        <table>
                                            <tr><td>
                                                    <%if (isSelected.equalsIgnoreCase("YES")) {%>
                                                    <input type="radio" id="selColsradio"  name="selcols" value="SelectedCols" checked onclick="moreColumns(this)">Table Columns
                                                    <%} else {%>
                                                    <input type="radio" id="selColsradio"  name="selcols" value="SelectedCols" onclick="moreColumns(this)">Table Columns
                                                    <%}%>
                                                    <%if (isSelected.equalsIgnoreCase("NO")) {%>
                                                    <input type="radio" id="moreColsradio" name="selcols" value="MoreCols" checked onclick="moreColumns(this)">More Columns
                                                    <%} else {%>
                                                    <input type="radio" id="moreColsradio" name="selcols" value="MoreCols" onclick="moreColumns(this)">More Columns
                                                    <%}%>
                                                </td>

                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table align="center" width="100%" border="0" >
                <tr>   <td width="50%" style="height:150px" valign="top">
                        <table>

                            <tr>
                                <td width="50%"  valign="top">Operator Type</td>

                            </tr>
                            <tr>
                                <td width="50%"  valign="top">
                                    <select id="opeType" name="opeType" onchange="changeOperators()">
                                        <option value="basic">Basic(Arithmetic)</option>
                                        <option value="advanced">Advanced/Statistical</option>
                                    </select>

                                </td>
                            </tr>
                            <tr>
                                <td width="50%" valign="top">Operators</td>
                            </tr>
                            <tr>
                                <td width="50%"  valign="top">
                                    <div id="basicopre">
                                        <select id="basicoperators" name="basicoperators" onchange="buildbasicoperation()">
                                            <option value="">--select--</option>
                                            <option value="+">Sum(+)</option>
                                            <option value="-">Difference(-)</option>
                                            <option value="*">Multiplication(*)</option>
                                            <option value="/">Division(/)</option>
                                            <option value="pwr">Power(^)</option>
                                            <option value="sqrt">Square Root</option>
                                            <option value="Round">Round</option>
                                            <option value="RoundDown">Round DOwn</option>
                                            <option value="abs">Absolute Value</option>
                                            <option value="count">count</option>
                                        </select>
                                    </div>
                                    <div id="advopre" style="display:none">
                                        <select id="advoperators" name="advoperators" onchange="buildadvoperation()">
                                            <option value="">--select--</option>
                                            <option value="avg">Average</option>
                                            <option value="sd">Std Deviation</option>
                                            <option value="min">Min</option>
                                            <option value="max">Max</option>
                                            <option value="sdper">Deviation(%)</option>
                                            <%-- <option value="totper">% of Total</option> --%>
                                        </select>
                                    </div>

                                </td>
                            </tr>

                            <tr>
                                <td width="50%" valign="top">Number</td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="text" id="Numer" name="Numer" onblur="test()">
                                </td>
                            </tr>
                            <tr>
                                <td width="50%" valign="top"><label class="label">{Press tab after enter number}</label></td>
                            </tr>

                        </table>
                    </td>

                    <td width="50%" style="height:150px" valign="top">
                        <center><label class="label"><b>Formula</b>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="editformula()">Edit</a>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="clearboxes()">Clear Formula</a></label ><hr></center>
                        <textarea style="width:99%;height:100%" id="txt2" name="txt2" readonly  cols="60" onkeyup="addtoothervals()" style="background-color:white;bbackground:white" rows="1"></textarea>
                    </td>
                </tr>

            </table>




            <table align="center" width="100%" border="1" >
                <tr style="width:100%" align="center">
                    <td width="100%" align="center" valign="top">
                        <table border="0" width="100%" align="center" cellspacing="5">
                            <tr style="width:100%" align="center">
                                <td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="+"  id="+" onclick="addValue('+','+','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="-"  id="-" onclick="addValue('-','-','Operators')"></td></tr>
                                        <tr><td><input type="button" value="*" class="navtitle-hover" style="width:auto" id="*" onclick="addValue('*','*','Operators')"></td></tr>


                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" value="/" class="navtitle-hover" style="width:auto" id="/" onclick="addValue('/','/','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="("  id="(" onclick="addValue('( ','(','OpenOper')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value=")"   id=")" onclick="addValue(' )',')','CloseOper')"></td></tr>

                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr><td><input type="button" value="()" class="navtitle-hover" style="width:auto"  id="( )" onclick="addValue('( )','( )','SpecOper')"></td></tr>
                                        <tr><td><input type="button" value="=" class="navtitle-hover" style="width:auto" id="=" onclick="addValue('=','=','Operators')"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="," onclick="addValue(',',',','Operators')"></td></tr>
                                    </table>
                                </td><td>
                                    <table>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="Select Column"  onclick="selectColumn()"></td></tr>
                                                <%--  <tr><td><input type="button" class="navtitle-hover" style="width:auto" value="case"  onclick="addCase()"></td></tr>--%>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Undo" value="Undo" class="btn" onclick="undoFun()"></td></tr>
                                        <tr><td><input type="button" class="navtitle-hover" style="width:auto" id="Redo" value="Redo" class="btn" onclick="redoFun()" ></td></tr>

                                    </table>
                                </td>
                                <td>

                                    <%--   <table>
                                      <tr><td valign="top"><input type="button" value="nvl" class="navtitle-hover" style="width:auto" id="nvl" onclick="addValue('nvl(','nvl(','Operatorsfun')"></td></tr>
                                        <tr><td valign="top"><input type="button" value="sum" class="navtitle-hover" style="width:auto" id="-" onclick="addValue('sum(','sum(','Operatorsfun')"></td></tr>

                                      <tr><td valign="top"><input type="button" value="count" class="navtitle-hover" style="width:auto" id="count" onclick="addValue('count(','count(','Operatorsfun')"></td></tr>
                                    </table>
                                    --%>

                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>





            <br>
            <table align="center" width="100%" border="0" >
                <tr style="width:100%" align="center">
                    <td width="100%" align="center" valign="top">
                        <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveCustMember('<%=tableNameList%>','<%=connectionId%>')">
                    </td>
                </tr>
            </table>
            <textarea id="tArea" name="tArea" style="display:none"> </textarea>
            <textarea id="tArea1" name="tArea1" style="display:none"> </textarea>
            <input type="hidden" name="folderIds" id="folderIds" value="<%=folderIds%>">
            <input type="hidden" name="iscalculate" id="iscalculate" >
            <input type="hidden" name="eleList" id="eleList" value="<%=eleList%>">
        </form>

        <div id="dialog" title="Cases"><center>
                <form name="caseForm">
                    <table>
                        <tr>
                            <td><label class="label" >When</label> &nbsp; <input type="text" id="when"  readonly name="when"  onfocus="focusedElement('when')">

                                <input type="hidden" id="when1" name="when1" onfocus="focusedElement('when1')">
                            </td>
                        </tr>
                        <tr>
                            <td><label class="label" >Then</label> &nbsp;
                                <input type="text" id="then" name="then" readonly onfocus="focusedElement('then')">
                                <input type="hidden" id="then1" name="then1" onfocus="focusedElement('then1')">
                            </td>
                        </tr>
                        <tr>
                            <td><label class="label" >Else</label> &nbsp;
                                <input type="text" id="else" name="else"  readonly onfocus="focusedElement('else')">
                                <input type="hidden" id="else1" name="else1" onfocus="focusedElement('else1')">
                            </td>
                        </tr>
                    </table>
                    <input type="button" value="Save" onclick="saveCase()"></form></center>

        </div>

        <%} catch (Exception ex) {
                ex.printStackTrace();
            }%>


        <script type="text/javascript">
            var formArray=new Array();// used for external formula
            var formArray1=new Array();// used for internal formula
            var redoArray=new Array();
            var redoArray1=new Array();
            var redoprevClassType=new Array();;//used for storing prevClassType

            var prevClass="";
            var prevClassType=new Array();;//used for storing prevClassType
            var colArray=new Array();
            var formula='';
            var formula1='';

            var arrIndex=0;
            var arrIndex1=0;
            var prevIndex=0;
            var redoIndex=0;
            var redoIndex1=0;
            var redoprevIndex=0;
            var Flag=1;

            var arrIndexLength=0;
            var arrIndex1Length=0;
            var prevIndexlength=0;

            var prevStr=null;
            var curStr=null;
            var txtlength=0;
            var caseStr = 'case when {        }'+"\n"+'then {       }'+"\n"+'else  {       }'+"\n"+'end';
            var caseWindowStatus = '0';
            var focussed='';
            var focussed1='';
            var output='';
            $(document).ready(function() {
                $("#myList3").treeview({
                    animated:"slow",
                    persist: "cookie"
                });

                //addeb by bharathi reddy fro search option
                $('ul#myList3 li').quicksearch({
                    position: 'before',
                    attached: 'ul#myList3',
                    loaderText: '',
                    delay: 100
                });
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



            });
            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });

            function createColumn(elmntId,elementName,tarLoc){

                var parentUL=document.getElementById(tarLoc);
                var x=colArray.toString();
                var c=elmntId.split("^");
                //alert(x+"==="+c[0])
                if(x.match(c[0])==null){
                    var s=elmntId.split("^");
                    colArray.push(s[0]);
                    var childLI=document.createElement("li");
                    childLI.id="For-"+elmntId;
                    childLI.style.width='180px';
                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className='navtitle-hover';
                    var table=document.createElement("table");
                    table.id="ForTab"+elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);
                    var chk=document.createElement("input");
                    chk.type="checkbox";
                    //chk.checked=true;
                    chk.name="ChkFor";
                    chk.id="Chk-"+elmntId;
                    chk.value=elmntId;
                    cell1.appendChild(chk);
                    chk.setAttribute("checked","checked");
                    var cell2=row.insertCell(1);
                    //cell1.style.backgroundColor="#e6e6e6";

                    var a=document.createElement("a");
                    var deleteElement = "For-"+elmntId;
                    a.href="javascript:deleteColumn('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell2.appendChild(a);
                    var cell3=row.insertCell(2);
                    // cell2.style.backgroundColor="#e6e6e6";
                    var a1=document.createElement("a");
                    var addElement = "Fora-"+elmntId;
                    a1.href="javascript:selectColumn1('"+elmntId+"')";
                    a1.innerHTML=elementName;
                    // a1.className="ui-icon ui-icon-close";
                    cell3.style.color='black';
                    //cell3.innerHTML=elementName;
                    cell3.appendChild(a1);
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
                }


            }
            function deleteColumn(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById('sortable');
                //   alert(parentUL+"---"+LiObj)
                parentUL.removeChild(LiObj);
                //var x=LiObj.id.split("~");
                //var x=LiObj.id.replace("GrpCol","");
                var s=index.split("^");
                var x=s[0].split("-")[1];
                var i=0;
                // alert(colArray+'7--7'+x);
                for(i=0;i<colArray.length;i++){
                    if(colArray[i]==x)
                        colArray.splice(i,1);
                }
                //alert('-'+colArray);
            }
            /*
                 $(".sortable").sortable();
                $(".sortable").disableSelection();
             */



            function addValue(str,str2,classType)
            {
                if(classType=="Operators"){
                    // if(prevClass=="Query" || prevClass=="Numer" ||  prevClass=="CloseOper"){
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+ str;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+ str;
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    prevClass="Operators";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="Operators";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    /*  }else{
                        alert('Please add formula using default operators or enter number ')
                    }*/
                }else if(classType=="OpenOper"){
                    //  if(prevClass=="Operators" || prevClass==""){
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+ str;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+ str;
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    prevClass="OpenOper";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="OpenOper";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    /*  }else{
                        alert('Please select operator')
                    }*/

                }else if(classType=="CloseOper"){
                    // if(prevClass=="Query" || prevClass=="Numer" || prevClass=="CloseOper"){
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+ str;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+ str;
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    prevClass="CloseOper";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="CloseOper";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    /*  }else{
                        alert('Please add formula using default operators or enter number ')
                    }*/

                }else  if(classType=="SpecOper"){
                    // if(prevClass=="Query" || prevClass=="Numer" || prevClass=="CloseOper"){
                    var actformula=document.getElementById("txt2").value;
                    actformula="("+actformula+")";
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula="("+acteleformula+")";
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    prevClass="SpecOper";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="SpecOper";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    /*  }else{
                          alert('Please add formula  or number properly to enclose in brackets');
                      }*/

                }
            }




            function undoFun(){
                if(arrIndex>0){
                    arrIndex--;
                    arrIndex1--;
                    prevIndex--;
                    // alert('inside arrIndexLength is '+arrIndex)
                    //   alert(formArray[parseInt(arrIndex)]);
                    //  alert(formArray1[parseInt(arrIndex1Length)]);
                    if(arrIndex==0){
                        document.forms.f1.txt2.value ="";
                        document.forms.f1.tArea.value ="";
                        prevClassType="";
                    }else{

                        document.forms.f1.txt2.value =formArray[parseInt(arrIndex)-1];
                        document.forms.f1.tArea.value =formArray1[parseInt(arrIndex1)-1];
                        prevClass=prevClassType[parseInt(prevIndex-1)];
                        redoArray[redoIndex]=formArray[parseInt(arrIndex)];
                        redoArray1[redoIndex1]=formArray1[parseInt(arrIndex1)];
                        redoprevClassType[redoprevIndex]=prevClassType[parseInt(prevIndex)]
                        redoIndex1++;
                        redoIndex++;
                        redoprevIndex++;

                        //alert(document.forms.f1.tArea.value);
                    }

                }
                else{
                    alert('nothing to undo')
                }

                return (Flag);
            }
            function redoFun(){
                // alert(redoIndex);
                if(redoIndex>0){
                    // alert(redoArray[parseInt(redoIndex)-1]+"--"+redoArray1[parseInt(redoIndex1)-1]+"--"+redoprevClassType[parseInt(redoprevIndex)-1])
                    document.forms.f1.txt2.value =redoArray[parseInt(redoIndex)-1];
                    document.forms.f1.tArea.value =redoArray1[parseInt(redoIndex1)-1];
                    prevClass=redoprevClassType[parseInt(redoprevIndex)-1];
                    formArray[parseInt(arrIndex)]=redoArray[parseInt(redoIndex)-1];
                    formArray1[parseInt(arrIndex1)]=redoArray1[parseInt(redoIndex1)-1];
                    prevClassType[parseInt(prevIndex)]=redoprevClassType[parseInt(redoprevIndex)-1];
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    redoIndex--;
                    redoIndex1--;
                    redoprevIndex--;
                }else{

                    alert('Nothing to redo')
                }
            }


            function clearboxes(){
                document.getElementById('txt2').value='';
                document.getElementById('tArea').value='';
                document.getElementById('tArea1').value='';
                document.getElementById('Numer').value='';
                prevClass=null;
                prevClassType="";
            }



            function checkExpectedClassType(prevClassType1,classType1){
                //alert('prevClassType1 is '+prevClassType1+',classType1 is'+classType1)
                if(prevClassType!=null){
                    if(prevClassType1=='Query'){
                        if(classType1=='Operators'|| classType1=='OpenOper'|| classType1=='CloseOper' || classType1=='SpecOper' || classType1=='Operatorsfun')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }

                    }
                    if(prevClassType1=='Operators'){

                        if(classType1=='Query' || classType1=='Numerics' || classType1=='OpenOper' ||  classType1=='Operatorsfun')
                            return true;
                        else{
                            alert('Please select column')
                            return false;
                        }
                    }

                    if(prevClassType1=='Operatorsfun'){

                        if(classType1=='Query' || classType1=='Numerics' || classType1=='OpenOper')
                            return true;
                        else{
                            alert('Please select column')
                            return false;
                        }
                    }
                    if(prevClassType1=='Numerics'){
                        if(classType1=='Operators'|| classType1=='OpenOper' || classType1=='CloseOper' || classType1=='SpecOper')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                    if(prevClassType1=='OpenOper'){
                        if(classType1=='Query' || classType1=='Numerics'|| classType1=='OpenOper')
                            return true;
                        else{
                            alert('Please select column')
                            return false;
                        }
                    }
                    if(prevClassType1=='CloseOper'){
                        if(classType1=='Operators' || classType1=='SpecOper' || classType1=='CloseOper')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                    if(prevClassType1=='SpecOper'){
                        if(classType1=='Operators')
                            return true;
                        else{
                            alert('Please select an Operator')
                            return false;
                        }
                    }
                }
                else{
                    return true;
                }
            }
            function test(){

                var x=document.getElementById("Numer").value;
                if(x.match(/\D/g)!=null){
                    alert("Please Enter Only Digits From 0-9");
                    x=x.replace(/\D/g,"");
                    document.getElementById("Numer").value=x;
                }else{

                    //if(prevClass=="Operators" || prevClass=="" ||prevClass=="OpenOper"){
                    var actformula=document.getElementById("txt2").value;
                    actformula=actformula+ document.getElementById("Numer").value;
                    var acteleformula=document.getElementById("tArea").value;
                    acteleformula=acteleformula+ document.getElementById("Numer").value;
                    //var tArea1val= document.getElementById('tArea1').value;
                    //tArea1val=tArea1val+eleIdsList;
                    //alert(actformula+"===="+acteleformula);
                    document.getElementById("txt2").value=actformula;
                    document.getElementById("tArea").value=acteleformula;
                    document.getElementById("Numer").value="";
                    prevClass="Numer";
                    formArray[arrIndex]=actformula;
                    formArray1[arrIndex1]=acteleformula;
                    prevClassType[prevIndex]="Numer";
                    arrIndex++;
                    arrIndex1++;
                    prevIndex++;
                    // document.getElementById('tArea1').value=tArea1val;

                    /*  }else{
                        alert('Please select operator from buttons pad')
                        document.getElementById("Numer").value="";
                    }*/


                }
            }

            function saveCustMember1(tableList,ConnectionId)
            {
                var query = document.getElementById('txt2').value;

                if(query.indexOf('+')>=0){
                    query=query.replace("+","@","gi");
                }
                //alert(query)




                var tArea1val= document.getElementById('tArea1').value;



                var tareaval= document.getElementById('tArea').value;
                if(tareaval.indexOf('+')>=0){
                    tareaval=tareaval.replace("+","@","gi");
                }

                var url = "CheckCustMeasureQuery?query="+query+"&tableList="+tableList+"&ConnectionId="+ConnectionId+"&tArea1="+tArea1val+"&tArea="+tareaval;

                xmlHttp=GetXmlHttpObject();
                if (xmlHttp==null)
                {
                    alert ("Your browser does not support AJAX!");
                    return;
                }

                xmlHttp.onreadystatechange=stateChanged;
                xmlHttp.open("GET",url,true);
                xmlHttp.send(null);

            }

            function saveCustMember(tableList,ConnectionId){
                if(document.getElementById("columnName").value==""){
                    alert('Please enter Formula Name');
                }
                else{
                    var x=confirm('Do you wish  to apply Prior,Change and Change%/n for calculated measure');
                    if(x==true){
                        document.getElementById("iscalculate").value="Y";
                    }
                    else{
                        document.getElementById("iscalculate").value="N";
                    }
                    var query = document.getElementById('txt2').value;
                    var name=document.getElementById("columnName").value;
                    var txt2=document.getElementById('txt2').value;
                    var folderIds=document.getElementById('folderIds').value;
                    var columnName=document.getElementById('columnName').value;
                    var iscalculate=document.getElementById('iscalculate').value;
                    var tArea=document.getElementById('tArea').value;
                    var tArea1=document.getElementById('tArea1').value;
                    var eleList=document.getElementById('eleList').value;
                    txt2=txt2.replace("+","@","gi");
                    txt2=txt2.replace("&","||chr(38)||","gi");

                    $.ajax({

                        //url: '<%=request.getContextPath()%>/saveCustMemberRD.do?saveCustomMeasure=saveCustomMeasure&txt2='+txt2+'&folderIds='+folderIds+'&columnName='+columnName+'&iscalculate='+iscalculate+'&tArea='+tArea+'&tArea1='+tArea1+'&eleList='+eleList,
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=saveViewerCustomFormula&txt2='+txt2+'&folderIds='+folderIds+'&columnName='+columnName+'&iscalculate='+iscalculate+'&tArea='+tArea+'&tArea1='+tArea1+'&eleList='+eleList,
                        success: function(data){
                            if(data!="")
                            {
                                /* var measList=parent.document.getElementById("Measures");
                                    alert("Parent measList "+measList+" "+measList.value);
                                    measList.value = measList.value + ","+data;
                                    if ( parent.PreviewTable1 )
                                        {
                                            alert("From Parent");
                                            parent.PreviewTable1();

                                        }
                                        else if ( top.PreviewTable1 )
                                        {
                                            alert("From Top");
                                            top.PreviewTable1();

                                        }*/
                                var measList=parent.document.getElementById("Measures").value;
                                measList = measList +","+data;
                                parent.document.getElementById("MsrIds").value=measList;
                                parent.document.getElementById("Measures").value=measList;


                                $.ajax({
                                    url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=Measures&Msrs='+measList+'&MsrsNames='+measList+'&REPORTID=<%=reportId%>',
                                    success: function(data) {
                                        parent.PreviewTable();
                                        if(data!=""){
                                        }
                                    }
                                });

                            }

                        }
                    });


                    // if(query.indexOf('+')>=0){
                    //     document.getElementById('txt2').value=query.replace("+","@","gi");
                    //   }
                    //   alert(document.getElementById('txt2').value+"===="+document.getElementById('tArea').value+"==="+document.getElementById('tArea1').value)
                    //   document.myForm.action = "saveCustMemberRD.do";
                    //   document.myForm.submit();
                    var name=document.getElementById("columnName").value;
                    parent.cancelCustMembersave(name);
                }

            }

            function cancelCustMember()
            {

                parent.cancelCustMember();
            }

            function addCase()
            {

                $('#dialog').dialog('open');
                caseWindowStatus = 1;
            }



            $.ui.dialog.defaults.bgiframe = true;
            $(function() {
                $("#dialog").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 200,
                    width: 200,
                    position: 'top'

                });
            });


            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    // Firefox, Opera 8.0+, Safari
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    // Internet Explorer
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }

            function stateChanged()
            {
                if (xmlHttp.readyState==4)
                {

                    var output1=xmlHttp.responseText;
                    // alert("next of output "+output1+"continue");
                    // if(output1=="Correct")
                    // {
                    if(document.getElementById("columnName").value==""){
                        alert('Please enter Formula Name');
                    }
                    else{
                        var x=confirm('Are you want to calculate Prior,Change and Change%');
                        if(x==true){
                            document.getElementById("iscalculate").value="Y";
                        }
                        else{
                            document.getElementById("iscalculate").value="N";
                        }
                        var query = document.getElementById('txt2').value;

                        // if(query.indexOf('+')>=0){
                        //     document.getElementById('txt2').value=query.replace("+","@","gi");
                        //   }
                        //   alert(document.getElementById('txt2').value+"===="+document.getElementById('tArea').value+"==="+document.getElementById('tArea1').value)
                        document.myForm.action = "saveCustMemberRD.do";
                        document.myForm.submit();
                        var name=document.getElementById("columnName").value;
                        parent.cancelCustMembersave(name);
                    }
                    // }
                    // else
                    // {
                    //     alert(output1);
                    //  }

                }
            }

            function saveCase()
            {

                var when = ' case when '+document.getElementById('when').value+' '+"\n";
                var then = 'then '+document.getElementById('then').value+' '+"\n";
                var elsee = 'else '+document.getElementById('else').value+' '+"\n"+'end'+"\n";

                var when1 = ' case when '+document.getElementById('when1').value+' '+"\n";
                var then1 = 'then '+document.getElementById('then1').value+' '+"\n";
                var elsee1 = 'else '+document.getElementById('else1').value+' '+"\n"+'end'+"\n";
                //alert(when+then+elsee);

                document.getElementById('txt2').value = document.getElementById('txt2').value+when+then+elsee;
                document.getElementById('when').value="";
                document.getElementById('then').value="";
                document.getElementById('else').value="";
                document.getElementById('tArea').value = document.getElementById('tArea').value+when1+then1+elsee1;
                document.getElementById('when1').value="";
                document.getElementById('then1').value="";
                document.getElementById('else1').value="";
                //alert(document.getElementById('tArea').value);
                caseWindowStatus = '0';
                $('#dialog').dialog('close');
            }
            function clearCase(){
                document.getElementById('when').value="";
                document.getElementById('then').value="";
                document.getElementById('else').value="";
                document.getElementById('when1').value="";
                document.getElementById('then1').value="";
                document.getElementById('else1').value="";


            }
            function addtoothervals(){
                if(document.getElementById("txt2").readOnly){

                }else{
                    var val=document.getElementById("txt2").value;
                    var val2=val.substring(val.length-1);
                    var val3=document.getElementById("tArea").value;
                    val3+=val2;
                    document.getElementById("tArea").value=val3;
                    /*  var val=document.getElementById("txt2").value;
               var stlen=val.length;
                if(stlen<txtlength && txtlength!=stlen){
                var val2=val.substring(val.length-1);
                var val3=document.getElementById("tArea").value;
               // alert(val3)
                val3=val3.substring(0,val3.length-1);
            }else{
                var val2=val.substring(val.length-1);
                var val3=document.getElementById("tArea").value;
                val3+=val2;
                }
               // alert(val3)
                document.getElementById("tArea").value=val3;
                txtlength=val.length;
                     */
                }
            }

            function changeOperators(){
                //  alert('hi');
                var opeType=document.getElementById("opeType").value;
                if(opeType=="basic"){
                    document.getElementById("basicopre").style.display='';
                    document.getElementById("advopre").style.display='none';
                }else{
                    document.getElementById("basicopre").style.display='none';
                    document.getElementById("advopre").style.display='';
                }
            }
            function activateDivs(isSelected){
                // alert('hi')
                if(isSelected=="NO"){
                    document.getElementById("beforeTable").style.display='';
                    document.getElementById("afterTable").style.display='none';
                    document.getElementById("moreColumns").style.display='none';
                }else{
                    document.getElementById("beforeTable").style.display='none';
                    document.getElementById("afterTable").style.display='';
                    document.getElementById("moreColumns").style.display='';

                }

            }
            function moreColumns(chkval){
                //alert(chkval)
                if(chkval.value=="MoreCols"){
                    // alert('in if')
                    document.getElementById("beforeTable").style.display='';
                    document.getElementById("afterTable").style.display='none';
                } else{
                    //  alert('in else')
                    document.getElementById("beforeTable").style.display='none';
                    document.getElementById("afterTable").style.display='';
                }


            }
            function editformula(){
                // alert("jj")
                document.getElementById("txt2").readOnly=false;

            }

            /*    function buildbasicoperation(){
                var seloperator=document.getElementById("basicoperators").value;
                var obj = document.myForm.ChkFor;
                if(obj==undefined){
                alert('Please Drag atleast two columns');
                document.getElementById("basicoperators").value="";

                }else if(seloperator==""){
                alert('Please select operator');

                }else{

                var i=0;
                var eleIdsList="";
                var eleNamesList="";
                for(var j=0;j<obj.length;j++)
                {
                    if(document.myForm.ChkFor[j].checked==true)
                    {
                        var eleidname=document.myForm.ChkFor[j].value.split("^");
                        eleIdsList+=","+eleidname[0];
                         eleNamesList+="^"+eleidname[1];
                        i++;
                    }
                }
                if(eleIdsList!=""){
                    eleIdsList=eleIdsList.substring(1);
                    eleNamesList=eleNamesList.substring(1);
                }
                if(i<=1 && (seloperator!="+" || seloperator!="-" || seloperator!="*"  || seloperator!="/") )
                {
                    alert("Please select at least two columns ");
                    document.getElementById("basicoperators").value="";
                }else if(seloperator!="+" || seloperator!="-" || seloperator!="*"  || seloperator!="/"){
                    var eleIdsListarr=eleIdsList.split(",");
                    var eleNamesListarr=eleNamesList.split("^");
                    var formulaNames="";
                    var formulaIds="";
                  if(seloperator=="pwr"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(pwr("+eleNamesListarr[i]+"))";
                       formulaIds+="(pwr("+eleIdsListarr[i]+"))";
                      }
                    }else if(seloperator=="sqrt"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(sqrt("+eleNamesListarr[i]+"))";
                       formulaIds+="(sqrt("+eleIdsListarr[i]+"))";
                      }
                    }else if(seloperator=="Round"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(Round("+eleNamesListarr[i]+"))";
                       formulaIds+="(Round("+eleIdsListarr[i]+"))";
                      }
                    }else if(seloperator=="sqrt"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(RoundDown("+eleNamesListarr[i]+"))";
                       formulaIds+="(RoundDown("+eleIdsListarr[i]+"))";
                      }
                    }
                    else if(seloperator=="abs"){
                      for(var i=0;i<1;i++){
                       formulaNames+="(RoundDown("+eleNamesListarr[i]+"))";
                       formulaIds+="(RoundDown("+eleIdsListarr[i]+"))";
                      }
                    }
                }
                else
                {
                    var eleIdsListarr=eleIdsList.split(",");
                    var eleNamesListarr=eleNamesList.split("^");
                    var formulaNames="";
                    var formulaIds="";
                     if(seloperator=="+"){
                      for(var i=0;i<eleIdsListarr.length;i++){
                       formulaNames+="+"+eleNamesListarr[i];
                       formulaIds+="+"+eleIdsListarr[i];
                      }
                     if(formulaNames!=""){
                           formulaNames=formulaNames.substring(1);
                           formulaIds=formulaIds.substring(1);
                           formulaNames="("+formulaNames+")";
                           formulaIds="("+formulaIds+")";
                     }
                    }
                    if(seloperator=="-"){
                      for(var i=0;i<eleIdsListarr.length;i++){
                       formulaNames+="-"+eleNamesListarr[i];
                       formulaIds+="+"+eleIdsListarr[i];
                      }
                     if(formulaNames!=""){
                         formulaNames=formulaNames.substring(1);
                         formulaIds=formulaIds.substring(1);
                         formulaNames="("+formulaNames+")";
                         formulaIds="("+formulaIds+")";

                     }
                    }
                    if(seloperator=="*"){
                      for(var i=0;i<eleIdsListarr.length;i++){
                       formulaNames+="*"+eleNamesListarr[i];
                      }
                     if(formulaNames!=""){
                         formulaNames=formulaNames.substring(1);
                         formulaIds=formulaIds.substring(1);
                         formulaNames="("+formulaNames+")";
                         formulaIds="("+formulaIds+")";

                     }
                    }
                    if(seloperator=="/"){
                      if(eleIdsListarr.length>2){
                      alert('Please select only two columns');
                      }else{

                      for(var i=0;i<eleIdsListarr.length;i++){
                       formulaNames+="/"+eleNamesListarr[i]+"";
                      }
                     if(formulaNames!=""){
                         formulaNames=formulaNames.substring(1);
                         formulaIds=formulaIds.substring(1);

                     }
                      }
                    }
                 }
                 var actformula=document.getElementById("txt2").value;
                 actformula=actformula+formulaNames;
                 var acteleformula=document.getElementById("tArea").value;
                 acteleformula=acteleformula+formulaIds;

                 alert(actformula+"===="+acteleformula);
                 document.getElementById("txt2").value=actformula;
                 document.getElementById("tArea").value=acteleformula;

                }

            }*/

            function buildbasicoperation(){
                //  if(prevClass=="" || prevClass=="Operators" ||prevClass=="OpenOper"){
                var checkflag="NO";

                var seloperator=document.getElementById("basicoperators").value;
                var number=document.getElementById("Numer").value;
                //var obj = document.myForm.ChkFor;
                var obj= document.getElementById('draggedcolumn').getElementsByTagName('input');

                if(obj==undefined || obj=="undefined"){
                    alert('Please Drag atleast one columns');
                    document.getElementById("basicoperators").value="";

                }
                else if(seloperator==""){
                    alert('Please select operator');

                }else{
                    var i=0;
                    var eleIdsList="";
                    var eleNamesList="";
                    if(obj.length!=undefined){
                        for(var j=0;j<obj.length;j++)
                        {     //alert(j)
                            if(document.myForm.ChkFor[j].checked==true)
                            {    //alert(document.myForm.ChkFor[j].value)
                                var eleidname=document.myForm.ChkFor[j].value.split("^");
                                //alert(eleidname[0])
                                eleIdsList+=","+eleidname[0];
                                eleNamesList+="^"+eleidname[1];
                                i++;
                            }
                        }
                        if(i==0){
                            alert('Please select columns');
                            document.getElementById("basicoperators").value="";

                        }
                    }else{
                        if(document.myForm.ChkFor.checked==true){
                            var eleidname=document.myForm.ChkFor.value.split("^");
                            eleIdsList+=","+eleidname[0];
                            eleNamesList+="^"+eleidname[1];
                            i++;
                        }else{
                            alert('Please select columns');
                            document.getElementById("basicoperators").value="";

                        }

                    }
                    if(eleNamesList!=""){
                        eleIdsList=eleIdsList.substring(1);
                        eleNamesList=eleNamesList.substring(1);
                    }
                    // alert(i)
                    if(eleIdsList!=""){
                        if(i<1)
                        {
                            alert("Please select at least one column ");
                            document.getElementById("basicoperators").value="";
                        }else {
                            //  alert('else if')
                            var eleIdsListarr=eleIdsList.split(",");
                            var eleNamesListarr=eleNamesList.split("^");
                            var formulaNames="";
                            var formulaIds="";
                            if(seloperator=="pwr"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one column')
                                }else{
                                    for(var i=0;i<1;i++){
                                        formulaNames+="(POWER("+eleNamesListarr[i]+",2))";
                                        formulaIds+="(POWER("+eleIdsListarr[i]+",2))";
                                    }
                                    checkflag="YES";
                                }
                            }else if(seloperator=="sqrt"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one column')
                                }else{
                                    for(var i=0;i<1;i++){
                                        formulaNames+="(SQRT("+eleNamesListarr[i]+"))";
                                        formulaIds+="(SQRT("+eleIdsListarr[i]+"))";
                                    }
                                    checkflag="YES";
                                }
                            }else if(seloperator=="count"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one column')
                                }else{
                                    for(var i=0;i<1;i++){
                                        formulaNames+="(COUNT("+eleNamesListarr[i]+"))";
                                        formulaIds+="(COUNT("+eleIdsListarr[i]+"))";
                                    }
                                    checkflag="YES";
                                }
                            }
                            else if(seloperator=="Round"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one column')
                                }else{
                                    for(var i=0;i<1;i++){
                                        formulaNames+="(ROUND("+eleNamesListarr[i]+"))";
                                        formulaIds+="(ROUND("+eleIdsListarr[i]+"))";
                                    }
                                    checkflag="YES";
                                }
                            }else if(seloperator=="RoundDown"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one column')
                                }else{

                                    for(var i=0;i<1;i++){
                                        formulaNames+="(ROUND("+eleNamesListarr[i]+",2))";
                                        formulaIds+="(ROUND("+eleIdsListarr[i]+",2))";
                                    }
                                    checkflag="YES";
                                }
                            }
                            else if(seloperator=="abs"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one column')
                                }else{
                                    for(var i=0;i<1;i++){
                                        formulaNames+="(ABS("+eleNamesListarr[i]+"))";
                                        formulaIds+="(ABS("+eleIdsListarr[i]+"))";
                                    }
                                    checkflag="YES";
                                }
                            }
                            else if(seloperator=="+"){
                                if(eleIdsListarr.length==1 && number==""){
                                    formulaNames+="(SUM("+eleNamesListarr[0]+"))";
                                    formulaIds+="(SUM("+eleIdsListarr[0]+"))";
                                    checkflag="YES";
                                }else{

                                    for(var i=0;i<eleIdsListarr.length;i++){
                                        formulaNames+="+"+eleNamesListarr[i];
                                        formulaIds+="+"+eleIdsListarr[i];
                                    }
                                    if(number!=""){
                                        formulaNames+="+"+number;
                                        formulaIds+="+"+number;

                                    }
                                    if(formulaNames!=""){
                                        formulaNames=formulaNames.substring(1);
                                        formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";
                                    }
                                    checkflag="YES";
                                }
                            }
                            else if(seloperator=="-"){
                                if(eleIdsListarr.length<2 ){
                                    alert('Please select atleast two colums')
                                }else{
                                    for(var i=0;i<eleIdsListarr.length;i++){
                                        formulaNames+="-"+eleNamesListarr[i];
                                        formulaIds+="-"+eleIdsListarr[i];
                                    }
                                    if(number!=""){
                                        formulaNames+="-"+number;
                                        formulaIds+="-"+number;

                                    }
                                    if(formulaNames!=""){
                                        formulaNames=formulaNames.substring(1);
                                        formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";

                                    }
                                    checkflag="YES";
                                }
                            }
                            else  if(seloperator=="*"){
                                if(eleIdsListarr.length<2){
                                    alert('Please select atleast two colums')
                                }else{
                                    for(var i=0;i<eleIdsListarr.length;i++){
                                        formulaNames+="*"+eleNamesListarr[i];
                                        formulaIds+="*"+eleIdsListarr[i];
                                    }
                                    if(formulaNames!=""){
                                        formulaNames=formulaNames.substring(1);
                                        formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";

                                    }
                                    checkflag="YES";
                                }
                            }
                            else if(seloperator=="/"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select only two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please Select two Columns')
                                }else{

                                    // for(var i=0;i<eleIdsListarr.length;i++){
                                    formulaNames+=eleNamesListarr[0]+"/NVL("+eleNamesListarr[1]+",0)";
                                    //  formulaIds+="/nvl("+eleIdsListarr[i]+",0)";
                                    formulaIds+=eleIdsListarr[0]+"/NVL("+eleIdsListarr[1]+",0)";
                                    // }
                                    if(formulaNames!=""){
                                        // formulaNames=formulaNames.substring(1);
                                        //  formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";
                                    }
                                    checkflag="YES";
                                }
                            }

                            document.getElementById("basicoperators").value="";
                            if( checkflag=="YES"){
                                var actformula=document.getElementById("txt2").value;
                                actformula=actformula+formulaNames;
                                var acteleformula=document.getElementById("tArea").value;
                                acteleformula=acteleformula+formulaIds;
                                var tArea1val= document.getElementById('tArea1').value;
                                // alert(eleIdsList)
                                tArea1val=tArea1val+","+eleIdsList;
                                //alert(actformula+"===="+acteleformula);
                                document.getElementById("txt2").value=actformula;
                                document.getElementById("tArea").value=acteleformula;
                                document.getElementById('tArea1').value=tArea1val;
                                prevClass="Query";
                                formArray[arrIndex]=actformula;
                                formArray1[arrIndex1]=acteleformula;
                                prevClassType[prevIndex]="Query";
                                arrIndex++;
                                arrIndex1++;
                                prevIndex++;
                            }
                        }
                    }

                }
                /* }else{
                    alert('Please select operator from buttons pad');
                    document.getElementById("basicoperators").value="";

                }*/

            }

            function buildadvoperation(){
                // alert('hi')
                // if(prevClass=="" || prevClass=="Operators" ||prevClass=="OpenOper"){
                var checkflag="NO";
                var seloperator=document.getElementById("advoperators").value;
                //var obj = document.myForm.ChkFor;
                var obj= document.getElementById('draggedcolumn').getElementsByTagName('input');
                if(obj==undefined){
                    alert('Please Drag atleast one columns');
                    document.getElementById("advoperators").value="";

                }else if(seloperator==""){
                    alert('Please select operator');

                }else{
                    var i=0;
                    var eleIdsList="";
                    var eleNamesList="";
                    if(obj.length!=undefined){
                        for(var j=0;j<obj.length;j++)
                        {     //alert(j)
                            if(document.myForm.ChkFor[j].checked==true)
                            {    //alert(document.myForm.ChkFor[j].value)
                                var eleidname=document.myForm.ChkFor[j].value.split("^");
                                //alert(eleidname[0])
                                eleIdsList+=","+eleidname[0];
                                eleNamesList+="^"+eleidname[1];
                                i++;
                            }
                        }
                        if(i==0){
                            alert('Please select columns');
                            document.getElementById("advoperators").value="";

                        }
                    }else{
                        if(document.myForm.ChkFor.checked==true){
                            var eleidname=document.myForm.ChkFor.value.split("^");
                            eleIdsList+=","+eleidname[0];
                            eleNamesList+="^"+eleidname[1];
                            i++;
                        }else{
                            alert('Please select columns');
                            document.getElementById("advoperators").value="";

                        }

                    }
                    if(eleIdsList!=""){
                        eleIdsList=eleIdsList.substring(1);
                        eleNamesList=eleNamesList.substring(1);
                    }
                    // alert(eleIdsList+"'==eleIdsList")
                    if(eleIdsList!=""){
                        if(i<1)
                        {
                            alert("Please select at least one column ");
                            document.getElementById("advoperators").value="";
                        }
                        else
                        {
                            var eleIdsListarr=eleIdsList.split(",");
                            var eleNamesListarr=eleNamesList.split("^");
                            var formulaNames="";
                            var formulaIds="";
                            // alert(seloperator)
                            if(seloperator=="avg"){
                                //alert(eleIdsListarr.length)
                                if(eleIdsListarr.length==1){


                                    $.ajax({
                                        url: 'reportTemplateAction.do?templateParam=getElementDataType&dependenteleids='+eleIdsListarr,
                                        success: function(data){
                                            if(data!=""){
                                                var  list=data.split(';');
                                                var list1=list[0].split('-');
                                                var first="";


                                                if(list1[0]!="SUMMARISED" && list1[0]!="SUMMARIZED"){
                                                    for(var i=0;i<1;i++){
                                                        formulaNames+="AVG("+eleNamesListarr[i]+")";
                                                        formulaIds+="AVG("+eleIdsListarr[i]+")";
                                                    }
                                                    checkflag="YES";
                                                }else{
                                                    first=list1[1];

                                                    formulaNames+=first+"("+eleNamesListarr[0]+")/(case when "+first+"("+eleNamesListarr[0]+")=0 then null else "+first+"("+eleNamesListarr[0]+") end )";
                                                    formulaIds+=first+"("+eleIdsListarr[0]+")/(case when "+first+"("+eleIdsListarr[0]+")=0 then null else "+first+"("+eleIdsListarr[0]+") end )";


                                                    if(formulaNames!=""){
                                                        formulaNames="("+formulaNames+")";
                                                        formulaIds="("+formulaIds+")";
                                                    }
                                                }
                                                var actformula=document.getElementById("txt2").value;
                                                actformula=actformula+formulaNames;
                                                var acteleformula=document.getElementById("tArea").value;
                                                acteleformula=acteleformula+formulaIds;
                                                var tArea1val= document.getElementById('tArea1').value;
                                                tArea1val=tArea1val+","+eleIdsList;

                                                document.getElementById("txt2").value=actformula;
                                                document.getElementById("tArea").value=acteleformula;
                                                document.getElementById('tArea1').value=tArea1val;
                                                prevClass="Query";
                                                formArray[arrIndex]=actformula;
                                                formArray1[arrIndex1]=acteleformula;
                                                prevClassType[prevIndex]="Query";
                                                arrIndex++;
                                                arrIndex1++;
                                                prevIndex++;
                                            }
                                            else {
                                                for(var i=0;i<1;i++){
                                                    formulaNames+="AVG("+eleNamesListarr[i]+")";
                                                    formulaIds+="AVG("+eleIdsListarr[i]+")";
                                                }
                                                checkflag="YES";
                                            }
                                        }
                                    });



                                }else{

                                    if(eleIdsListarr.length==2){



                                        $.ajax({
                                            url: 'reportTemplateAction.do?templateParam=checkavgTwoTables&dependenteleids='+eleIdsListarr,
                                            success: function(data){
                                                // alert('data'+data)
                                                if(data!=""){
                                                    var  list=data.split(';');
                                                    var list1=list[0].split('-');
                                                    var list2=list[1].split('-');
                                                    var first="";
                                                    var sec="";
                                                    var third="";
                                                    if(list1[0]==eleNamesListarr[0]){
                                                        if(list1[2]=="SUMMARIZED" ||list1[2]=="SUMMARISED" ){
                                                            first=list1[1];
                                                        }
                                                        if(list2[2]=="SUMMARIZED" ||list1[2]=="SUMMARISED" ){
                                                            sec=list2[1];
                                                        }

                                                    }else{
                                                        if(list2[2]=="SUMMARIZED" ||list1[2]=="SUMMARISED" ){
                                                            first=list2[1];
                                                        }
                                                        if(list1[2]=="SUMMARIZED" ||list1[2]=="SUMMARISED" ){
                                                            sec=list1[1];
                                                        }

                                                    }
                                                    formulaNames+=first+"("+eleNamesListarr[0]+")/(case when "+sec+"("+eleNamesListarr[1]+")=0 then null else "+sec+"("+eleNamesListarr[1]+") end )";
                                                    formulaIds+=first+"("+eleIdsListarr[0]+")/(case when "+sec+"("+eleIdsListarr[1]+")=0 then null else "+sec+"("+eleIdsListarr[1]+") end )";


                                                    if(formulaNames!=""){
                                                        // formulaNames=formulaNames.substring(1);
                                                        // formulaIds=formulaIds.substring(1);
                                                        // formulaNames="("+formulaNames+")/count("+eleNamesListarr[0]+")";
                                                        // formulaIds="("+formulaIds+")/count("+eleIdsListarr[0]+")";
                                                        formulaNames="("+formulaNames+")";
                                                        formulaIds="("+formulaIds+")";
                                                    }
                                                    var actformula=document.getElementById("txt2").value;
                                                    actformula=actformula+formulaNames;
                                                    var acteleformula=document.getElementById("tArea").value;
                                                    acteleformula=acteleformula+formulaIds;
                                                    var tArea1val= document.getElementById('tArea1').value;
                                                    tArea1val=tArea1val+","+eleIdsList;
                                                    // alert(actformula+"===="+acteleformula);
                                                    document.getElementById("txt2").value=actformula;
                                                    document.getElementById("tArea").value=acteleformula;
                                                    document.getElementById('tArea1').value=tArea1val;
                                                    prevClass="Query";
                                                    formArray[arrIndex]=actformula;
                                                    formArray1[arrIndex1]=acteleformula;
                                                    prevClassType[prevIndex]="Query";
                                                    arrIndex++;
                                                    arrIndex1++;
                                                    prevIndex++;
                                                }
                                                else {
                                                    alert('Average is not supported \n Please select division')
                                                }
                                            }
                                        });
                                    }else{
                                        alert('Please select two columns only ')
                                    }
                                }
                                // alert('vgbhgn')
                            }
                            if(seloperator=="min"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one columns');
                                }else{
                                    for(var i=0;i<1;i++){
                                        formulaNames+="MIN("+eleNamesListarr[i]+")";
                                        formulaIds+="MIN("+eleIdsListarr[i]+")";
                                    }
                                    checkflag="YES";
                                    if(formulaNames!=""){
                                        //formulaNames=formulaNames.substring(1);
                                        //formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";

                                    }
                                }
                            }
                            if(seloperator=="max"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one columns');
                                }else{
                                    for(var i=0;i<1;i++){
                                        formulaNames+="MAX("+eleNamesListarr[i]+")";
                                        formulaIds+="MAX("+eleIdsListarr[i]+")";
                                    }
                                    checkflag="YES";
                                    if(formulaNames!=""){
                                        //formulaNames=formulaNames.substring(1);
                                        // formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";
                                    }
                                }
                            }
                            if(seloperator=="totper"){
                                if(eleIdsListarr.length>1){
                                    alert('Please select only one columns');
                                }else{

                                    for(var i=0;i<1;i++){
                                        formulaNames+="/"+eleNamesListarr[i]+"";
                                        formulaIds+="/"+eleIdsListarr[i]+"";
                                    }
                                    checkflag="YES";
                                    if(formulaNames!=""){
                                        // formulaNames=formulaNames.substring(1);
                                        //formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";

                                    }
                                }
                            }
                            if(seloperator=="sd"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please select two columns');
                                }else{


                                    formulaNames+="(SUM("+eleNamesListarr[0]+")-SUM("+eleNamesListarr[1]+"))/(case when SUM("+eleNamesListarr[0]+")=0 then null else SUM("+eleNamesListarr[0]+") end )";
                                    // formulaIds+="("+eleIdsListarr[0]+"-"+eleIdsListarr[1]+")/nvl("+eleIdsListarr[0]+",0)";
                                    formulaIds+="(SUM("+eleIdsListarr[0]+")-SUM("+eleIdsListarr[1]+"))/(case when SUM("+eleIdsListarr[0]+")=0 then null else SUM("+eleIdsListarr[0]+") end )";
                                    checkflag="YES";
                                    if(formulaNames!=""){
                                        // formulaNames=formulaNames.substring(1);
                                        // formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";
                                    }
                                }
                            }
                            if(seloperator=="sdper"){
                                if(eleIdsListarr.length>2){
                                    alert('Please select two columns');
                                }else if(eleIdsListarr.length<2){
                                    alert('Please select two columns');
                                }else{


                                    formulaNames+="((SUM("+eleNamesListarr[0]+")-SUM("+eleNamesListarr[1]+"))/(case when SUM("+eleNamesListarr[0]+")=0 then null else SUM("+eleNamesListarr[0]+") end ))*100";
                                    // formulaIds+="("+eleIdsListarr[0]+"-"+eleIdsListarr[1]+")/nvl("+eleIdsListarr[0]+",0)";
                                    formulaIds+="((SUM("+eleIdsListarr[0]+")-SUM("+eleIdsListarr[1]+"))/(case when SUM("+eleIdsListarr[0]+")=0 then null else SUM("+eleIdsListarr[0]+") end ))*100";
                                    checkflag="YES";
                                    if(formulaNames!=""){
                                        // formulaNames=formulaNames.substring(1);
                                        // formulaIds=formulaIds.substring(1);
                                        formulaNames="("+formulaNames+")";
                                        formulaIds="("+formulaIds+")";
                                    }
                                }
                            }
                            document.getElementById("advoperators").value="";
                            //  alert('--------'+checkflag)
                            if(checkflag=="YES"){
                                var actformula=document.getElementById("txt2").value;
                                actformula=actformula+formulaNames;
                                var acteleformula=document.getElementById("tArea").value;
                                acteleformula=acteleformula+formulaIds;
                                var tArea1val= document.getElementById('tArea1').value;
                                tArea1val=tArea1val+","+eleIdsList;
                                //  alert(actformula+"===="+acteleformula);
                                document.getElementById("txt2").value=actformula;
                                document.getElementById("tArea").value=acteleformula;
                                document.getElementById('tArea1').value=tArea1val;
                                prevClass="Query";
                                formArray[arrIndex]=actformula;
                                formArray1[arrIndex1]=acteleformula;
                                prevClassType[prevIndex]="Query";
                                arrIndex++;
                                arrIndex1++;
                                prevIndex++;
                            }
                        }

                    }
                }
                /* }else{
                    alert('Please select operator from buttons pad');
                    document.getElementById("basicoperators").value="";

                }*/

            }
            function selectColumn(){
                //  var obj = document.myForm.ChkFor;
                var obj= document.getElementById('draggedcolumn').getElementsByTagName('input');
                if(obj==undefined){
                    alert('Please Drag atleast one columns');
                    document.getElementById("advoperators").value="";

                }else{
                    var i=0;
                    var eleIdsList="";
                    var eleNamesList="";
                    if(obj.length!=undefined){
                        for(var j=0;j<obj.length;j++)
                        {     //alert(j)
                            if(document.myForm.ChkFor[j].checked==true)
                            {    //alert(document.myForm.ChkFor[j].value)
                                var eleidname=document.myForm.ChkFor[j].value.split("^");
                                //alert(eleidname[0])
                                eleIdsList+=","+eleidname[0];
                                eleNamesList+="^"+eleidname[1];
                                i++;
                            }
                        }
                        if(i==0){
                            alert('Please select columns');

                        }else if(i>1){
                            alert('Please select only one column at a time');
                        }else{
                            if(eleIdsList!=""){
                                eleIdsList=eleIdsList.substring(1);
                                eleNamesList=eleNamesList.substring(1);
                            }
                            var actformula=document.getElementById("txt2").value;
                            actformula=actformula+eleNamesList;
                            var acteleformula=document.getElementById("tArea").value;
                            acteleformula=acteleformula+eleIdsList;
                            var tArea1val= document.getElementById('tArea1').value;
                            tArea1val=tArea1val+","+eleIdsList;
                            //alert(actformula+"===="+acteleformula);
                            document.getElementById("txt2").value=actformula;
                            document.getElementById("tArea").value=acteleformula;
                            document.getElementById('tArea1').value=tArea1val;
                            prevClass="Query";
                            formArray[arrIndex]=actformula;
                            formArray1[arrIndex1]=acteleformula;
                            prevClassType[prevIndex]="Query";
                            arrIndex++;
                            arrIndex1++;
                            prevIndex++;

                        }
                    }else{
                        if(document.myForm.ChkFor.checked==true){
                            var eleidname=document.myForm.ChkFor.value.split("^");
                            eleIdsList+=","+eleidname[0];
                            eleNamesList+="^"+eleidname[1];
                            if(eleIdsList!=""){
                                eleIdsList=eleIdsList.substring(1);
                                eleNamesList=eleNamesList.substring(1);
                            }
                            var actformula=document.getElementById("txt2").value;
                            actformula=actformula+eleNamesList;
                            var acteleformula=document.getElementById("tArea").value;
                            acteleformula=acteleformula+eleIdsList;
                            var tArea1val= document.getElementById('tArea1').value;
                            tArea1val=tArea1val+","+eleIdsList;
                            //alert(actformula+"===="+acteleformula);
                            document.getElementById("txt2").value=actformula;
                            document.getElementById("tArea").value=acteleformula;
                            document.getElementById('tArea1').value=tArea1val;
                            prevClass="Query";
                            formArray[arrIndex]=actformula;
                            formArray1[arrIndex1]=acteleformula;
                            prevClassType[prevIndex]="Query";
                            arrIndex++;
                            arrIndex1++;
                            prevIndex++;

                        }else{
                            alert('Please select columns');

                        }
                    }
                }

            }
            function selectColumn1(str){

                var i=0;
                var eleIdsList="";
                var eleNamesList="";

                var eleidname=str.split("^");
                eleIdsList+=","+eleidname[0];
                eleNamesList+="^"+eleidname[1];
                if(eleIdsList!=""){
                    eleIdsList=eleIdsList.substring(1);
                    eleNamesList=eleNamesList.substring(1);
                }
                var actformula=document.getElementById("txt2").value;
                actformula=actformula+eleNamesList;
                var acteleformula=document.getElementById("tArea").value;
                acteleformula=acteleformula+eleIdsList;
                var tArea1val= document.getElementById('tArea1').value;
                tArea1val=tArea1val+","+eleIdsList;
                //alert(actformula+"===="+acteleformula);
                document.getElementById("txt2").value=actformula;
                document.getElementById("tArea").value=acteleformula;
                document.getElementById('tArea1').value=tArea1val;
                prevClass="Query";
                formArray[arrIndex]=actformula;
                formArray1[arrIndex1]=acteleformula;
                prevClassType[prevIndex]="Query";
                arrIndex++;
                arrIndex1++;
                prevIndex++;



            }

        </script>
<%
   request.getSession().removeAttribute("reportId");
   request.getSession().removeAttribute("folderIds");
    request.getSession().removeAttribute("loadDialogs");
}%>

    </body>
</html>
