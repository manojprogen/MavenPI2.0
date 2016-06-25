
<%@page import="utils.db.*,prg.db.PbDb,utils.db.ProgenConnection,java.sql.*,prg.db.PbReturnObject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%   String dbType = "";
            if (session.getAttribute("MetadataDbType") != null) {
                dbType = (String) session.getAttribute("MetadataDbType");

String contextPath=request.getContextPath();                           }
%>
<html>
    <head>

        <link href="StyleSheet.css" rel="stylesheet" type="text/css" />

        <link href="myStyles.css" rel="stylesheet" type="text/css">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        
        <style>
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
        </style>

    </head>

    <%
                try {
                    String grpId = request.getParameter("grpId");
                   
                    PbDb pbdb = new PbDb();
                    //code  to get time dim details based on grpId and busstableid and busscol id of previous time dim

                    String timedimInfo = "SELECT TIME_DIM_ID, BUSINESS_TABLE_ID, MAIN_FACT_ID, MAIN_FACT_COL_ID, MIN_LEVEL, CREATED_BY, CREATED_ON,";
                    timedimInfo += "UPDATED_BY, UPDATED_ON, IS_MAIN, IS_AS_OF_DATE_JOIN";
                    timedimInfo += " FROM PRG_TIME_DIM_INFO where BUSINESS_TABLE_ID=(SELECT BUSS_TABLE_ID FROM PRG_GRP_BUSS_TABLE";
                    timedimInfo += " where grp_id=" + grpId + " and buss_table_name='PROGEN_TIME')";

                    PbReturnObject pbrotimedimInfo = pbdb.execSelectSQL(timedimInfo);
                    //String timedimInfoColNames[] = pbrotimedimInfo.getColumnNames();
                    String mainfactbussTableId = "";
                    String mainfactbussColId = "";
                    String timeDimInfoId = "";
                    for (int i = 0; i < pbrotimedimInfo.getRowCount(); i++) {
                        mainfactbussTableId += "," + String.valueOf(pbrotimedimInfo.getFieldValueInt(i, "MAIN_FACT_ID"));
                        mainfactbussColId += "," + String.valueOf(pbrotimedimInfo.getFieldValueInt(i, "MAIN_FACT_COL_ID"));
                        timeDimInfoId += "," + String.valueOf(pbrotimedimInfo.getFieldValueInt(i, "TIME_DIM_ID"));
                    }
                    if (pbrotimedimInfo.getRowCount() > 0) {
                        mainfactbussTableId = mainfactbussTableId.substring(1);
                        mainfactbussColId = mainfactbussColId.substring(1);
                        timeDimInfoId = timeDimInfoId.substring(1);
                    }

                    String timebussTableId = String.valueOf(pbrotimedimInfo.getFieldValueInt(0, "BUSINESS_TABLE_ID"));
                    String preminlevel = String.valueOf(pbrotimedimInfo.getFieldValueInt(0, "MIN_LEVEL"));

                    String bussTableId = request.getParameter("bussTableId");
                    String bussColId = request.getParameter("bussColId");
                    String tabName = request.getParameter("tabName");
                    String colName = request.getParameter("colName");
                    String colType = request.getParameter("colType");
                    String QueryDay = "";
                    if (dbType.equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        QueryDay = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS where upper(column_type) in ('DATE','DATETIME2','TIMESTAMP','DATETIME')  and  BUSS_TABLE_ID=" + bussTableId;

                    } else {

                        QueryDay = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS where upper(column_type) in ('DATE','DATETIME2','TIMESTAMP','DATETIME') and  BUSS_TABLE_ID=" + bussTableId;

                    }
                    PbReturnObject pbroday = pbdb.execSelectSQL(QueryDay);
                    String colNamesday[] = pbroday.getColumnNames();
                    String Query = "SELECT BUSS_COLUMN_ID,COLUMN_NAME FROM PRG_GRP_BUSS_TABLE_DETAILS where  BUSS_TABLE_ID=" + bussTableId;
                    PbReturnObject pbro = pbdb.execSelectSQL(Query);
                    String colNames[] = pbro.getColumnNames();

                    String alreadyExistDetails = "SELECT BUSS_RELATIONSHIP_ID, P_BUSS_TABLE_ID, P_BUSS_COL_ID1, ACTUAL_CLAUSE, S_COL1_FORMAT FROM PRG_TIME_DIM_INFO_RLT_DETAILS";
                    alreadyExistDetails += " where BUSS_RELATIONSHIP_ID=(SELECT TIME_DIM_ID FROM PRG_TIME_DIM_INFO where MAIN_FACT_ID=" + bussTableId;
                    alreadyExistDetails += " and MAIN_FACT_COL_ID=" + bussColId + ") order by buss_relationship_detail_id";
                    PbReturnObject exists = pbdb.execSelectSQL(alreadyExistDetails);

                    int no = 0;
                    int year = 0;
                    String existedFormat = "";
                    String existedPre = "";
                    String existedPost = "";
                    String isformatexist = "N";
                    if (exists.getRowCount() > 0) {
                        if (exists.getRowCount() == 1) {
                            no = exists.getFieldValueInt(0, "P_BUSS_COL_ID1");
                            if (Integer.parseInt(preminlevel) == 3) {
                                if (exists.getFieldValueString(0, "S_COL1_FORMAT") != null) {
                                    isformatexist = "Y";

                                    String z = exists.getFieldValueString(0, "S_COL1_FORMAT");
                                    String ttt[] = z.split("~");
                                    if (ttt.length == 2) {
                                        existedPost = ttt[1];
                                    }
                                    String tt[] = ttt[0].split("&");
                                    if (tt.length == 2) {
                                        existedFormat = tt[1];
                                        existedPre = tt[0];
                                    } else {
                                        existedFormat = tt[0];
                                    }

                                }
                            }
                        }
                        if (exists.getRowCount() == 2) {
                            no = exists.getFieldValueInt(0, "P_BUSS_COL_ID1");
                            year = exists.getFieldValueInt(1, "P_BUSS_COL_ID1");

                        }

                    }




    %>
    <body onload="changeLevel('<%=preminlevel%>','<%=exists.getRowCount()%>','<%=isformatexist%>')">
        <center>
            <form name="myForm" method="post">
                <table >

                    <tr><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr><td>&nbsp;</td></tr>

                    <tr>
                        <td class="myHead" style="width:150px" >
                            <label class="label" >Minimum Time Level</label>
                        </td>


                        <td style="width:58%">






                            <% String minlevelStr = "";
                                                if (preminlevel.equals("1")) {
                                                    minlevelStr = "Year";
                            %>
                            <%-- <option value="5">Day</option>
                                <option value="4">Week</option>
                                <option value="3">Month</option>
                                <option value="2">Quarter</option>
                                <option value="1" selected>Year</option> --%>
                            <%} else if (preminlevel.equals("2")) {
                                                                        minlevelStr = "Quarter";
                            %>
                            <%-- <option value="5">Day</option>
                                <option value="4">Week</option>
                                <option value="3">Month</option>
                                <option value="2" selected>Quarter</option>
                                <option value="1">Year</option> --%>
                            <%} else if (preminlevel.equals("3")) {
                                                                        minlevelStr = "Month";
                            %>
                            <%--  <option value="5">Day</option>
                                <option value="4">Week</option>
                                <option value="3" selected>Month</option>
                                <option value="2">Quarter</option>
                                <option value="1">Year</option> --%>
                            <%} else if (preminlevel.equals("4")) {
                                                                        minlevelStr = "Week";
                            %>
                            <%--  <option value="5">Day</option>
                                <option value="4" selected>Week</option>
                                <option value="3">Month</option>
                                <option value="2">Quarter</option>
                                <option value="1">Year</option> --%>
                            <%} else if (preminlevel.equals("5")) {
                                                                        minlevelStr = "Day";
                            %>
                            <%-- <option value="5" selected>Day</option>
                                <option value="4">Week</option>
                                <option value="3">Month</option>
                                <option value="2">Quarter</option>
                                <option value="1">Year</option>
                            --%>
                            <%}%>







                            <input type="text" id="minTimeLevel" name="minTimeLevel" class="myTextbox5" style="width:150px" readonly value="<%=minlevelStr%>">



                        </td>

                    </tr>

                </table>
                <div id="dayDiv">
                    <table>

                        <tr>
                            <td style="width:150px" style="display:none;">
                                <label class="label" >Date</label>
                            </td>
                            <td style="width:58%">
                                <select id="timeDimKeyValueDay" name="timeDimKeyValueDay" class="myTextbox5" style="width:150px">
                                    <%for (int i = 0; i < pbroday.getRowCount(); i++) {
                                                            if (pbroday.getFieldValueInt(i, colNamesday[0]) == no) {
                                    %>
                                    <option selected value="<%=pbroday.getFieldValueInt(i, colNamesday[0])%>~<%=pbroday.getFieldValueString(i, colNamesday[1])%>"><%=pbroday.getFieldValueString(i, colNamesday[1])%></option>
                                    <%} else {%>
                                    <option value="<%=pbroday.getFieldValueInt(i, colNamesday[0])%>~<%=pbroday.getFieldValueString(i, colNamesday[1])%>"><%=pbroday.getFieldValueString(i, colNamesday[1])%></option>
                                    <% }
                                                        }%>
                                </select>
                            </td>
                        </tr>


                    </table>
                </div>

                <div id="weekDiv" style="display:none;">
                    <table>

                        <tr>
                            <td style="width:150px">
                                <label class="label" >Join Type</label>
                            </td>
                            <td style="width:58%">
                                <select id="weekJoinType" name="weekJoinType" class="myTextbox5" style="width:150px" onchange="changeWeekJoin()">
                                    <%      if (exists.getRowCount() > 0) {
                                                            if (exists.getRowCount() == 1) {%>
                                    <option value="S" selected>Single</option>
                                    <option value="M">Multiple</option>
                                    <%} else if (exists.getRowCount() == 2) {%>
                                    <option value="S" >Single</option>
                                    <option value="M" selected>Multiple</option>
                                    <%}
                                          } else {%>
                                    <option value="S" >Single</option>
                                    <option value="M">Multiple</option>
                                    <%}%>
                                </select>
                            </td>
                        </tr>
                    </table>
                    <div id="weekSingleDiv"  style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    <label class="label" >Week</label>
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValuesingleWeekno" name="timeDimKeyValuesingleWeekno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == no) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>
                        </table>

                    </div>


                    <div id="weekMultipleDiv" style="display:none;">

                        <table>

                            <tr>
                                <td style="width:150px">
                                    <label class="label" >Week No</label>
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueWeekno" name="timeDimKeyValueWeekno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == no) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:150px">
                                    <label class="label" >Week Year</label>
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueWeekyr" name="timeDimKeyValueWeekyr" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == year) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>


                        </table>
                    </div>
                </div>
                <div id="monthDiv" style="display:none;">


                    <table>

                        <tr>
                            <td style="width:150px">
                                <label class="label" >Join Type</label>
                            </td>
                            <td style="width:58%">
                                <select id="monthJoinType" name="monthJoinType" class="myTextbox5" style="width:150px" onchange="monthFormatChange()">
                                    <%if (exists.getRowCount() > 0) {
                                                            if (exists.getRowCount() == 1) {
                                                                if (isformatexist.equalsIgnoreCase("Y")) {
                                    %>
                                    <option value="S">Single</option>
                                    <option value="M">Multiple</option>
                                    <option value="F" selected>Format</option>
                                    <%} else {%>
                                    <option value="S" selected>Single</option>
                                    <option value="M">Multiple</option>
                                    <option value="F">Format</option>
                                    <%}
                                                                                        } else if (exists.getRowCount() == 2) {%>
                                    <option value="S">Single</option>
                                    <option value="M" selected>Multiple</option>
                                    <option value="F">Format</option>
                                    <%}
                                    } else {%>
                                    <option value="S">Single</option>
                                    <option value="M">Multiple</option>
                                    <option value="F">Format</option>
                                    <%}%>
                                </select>
                            </td>
                        </tr>
                    </table>

                    <div id="monthSingleDiv"  style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    <label class="label" >Month</label>
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValuesingleMonthno" name="timeDimKeyValuesingleMonthno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == no) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>
                        </table>

                    </div>


                    <%--
                    <table>
                        <tr>
                            <td style="width:150px">
                                Format
                            </td>
                            <td style="width:58%">
                                <input type="checkbox" checked id="monthFormatyn" name="monthFormatyn" value="Y" onchange="monthFormatChange()">
                            </td>
                        </tr>
                    </table>
                    --%>
                    <div id="monthFormatDiv" style="display:none;">
                        <table align="center">
                            <tr>
                                <td style="width:150px">
                                    <label class="label" >Month</label>
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValuemonth" name="timeDimKeyValuemonth" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == no) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <table>
                            <tr>
                                <td style="width:60px">
                                    <label class="label" >Pre</label>
                                </td>

                                <td style="width:150px">
                                    &nbsp;&nbsp;  <label class="label" >Format</label>
                                </td>
                                <td style="width:60px">
                                    &nbsp;&nbsp; &nbsp;&nbsp;
                                    <label class="label" >Post</label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="text" checked id="monthPre" name="monthPre" style="width:70px" value="<%=existedPre%>">
                                </td>
                                <td>
                                    <input type="text"  id="monthFormat" name="monthFormat" value="<%=existedFormat%>">
                                </td>
                                <td>
                                    <input type="text"  id="monthPost" name="monthPost" style="width:70px" value="<%=existedPost%>">
                                </td>

                            </tr>


                        </table>
                    </div>

                    <div id="monthNormalDiv" style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    <label class="label" >Month No</label>
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueMonthno" name="timeDimKeyValueMonthno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == no) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:150px">
                                    <label class="label" >Month Year</label>
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueMonthyr" name="timeDimKeyValueMonthyr" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == year) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>

                        </table>
                    </div>


                </div>

                <div id="quarterDiv" style="display:none;">

                    <table>

                        <tr>
                            <td style="width:150px">
                                <label class="label" > Join Type</label>
                            </td>
                            <td style="width:58%">
                                <select id="quarterJoinType" name="quarterJoinType" class="myTextbox5" style="width:150px" onchange="quarterFormatChange()">

                                    <%      if (exists.getRowCount() > 0) {
                                                            if (exists.getRowCount() == 1) {%>
                                    <option value="S" selected>Single</option>
                                    <option value="M">Multiple</option>
                                    <%} else if (exists.getRowCount() == 2) {%>
                                    <option value="S" >Single</option>
                                    <option value="M" selected>Multiple</option>
                                    <%}
                                          } else {%>
                                    <option value="S" >Single</option>
                                    <option value="M">Multiple</option>
                                    <%}%>

                                </select>
                            </td>
                        </tr>
                    </table>


                    <div id="quarterSingleDiv"  style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    <label class="label" /> Week
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValuesingleQuaterno" name="timeDimKeyValuesingleQuaterno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == no) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>
                        </table>

                    </div>


                    <div id="quarterMultipleDiv" style="display:none;">
                        <table>

                            <tr>
                                <td style="width:150px">
                                    <label class="label" />Quarter No
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueQuaterno" name="timeDimKeyValueQuaterno" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == no) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:150px">
                                    <label class="label" />Quarter Year
                                </td>
                                <td style="width:58%">
                                    <select id="timeDimKeyValueQuateryr" name="timeDimKeyValueQuateryr" class="myTextbox5" style="width:150px">
                                        <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                                if (pbro.getFieldValueInt(i, colNames[0]) == year) {
                                        %>
                                        <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%} else {%>
                                        <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                        <%}
                                                            }%>
                                    </select>
                                </td>
                            </tr>


                        </table>
                    </div>
                </div>
                <div id="yearDiv" style="display:none;">
                    <table>

                        <tr>
                            <td style="width:150px">
                                <label class="label" />Year
                            </td>
                            <td style="width:58%">
                                <select id="timeDimKeyValueYear" name="timeDimKeyValueYear" class="myTextbox5" style="width:150px">
                                    <%for (int i = 0; i < pbro.getRowCount(); i++) {
                                                            if (pbro.getFieldValueInt(i, colNames[0]) == no) {
                                    %>
                                    <option selected value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                    <%} else {%>
                                    <option value="<%=pbro.getFieldValueInt(i, colNames[0])%>~<%=pbro.getFieldValueString(i, colNames[1])%>"><%=pbro.getFieldValueString(i, colNames[1])%></option>
                                    <%}
                                                        }%>
                                </select>
                            </td>
                        </tr>


                    </table>
                </div>
                <br>
                <table >
                    <tr>
                        <td align="center"><input type="button" class="navtitle-hover" style="width:auto"  value="Save" onclick="saveShowTimeDim('<%=preminlevel%>')"></td>
                        <td align="center"><input type="button" class="navtitle-hover" style="width:auto"  value="Cancel" onclick="cancelShowTimeDim()"></td>
                    </tr>
                </table>
                <input type="hidden" name="h" id="h" value="<%=request.getContextPath()%>">
                <input type="hidden" name="grpId" id="grpId" value="<%=grpId%>">
                <input type="hidden" name="bussTableId" id="bussTableId" value="<%=bussTableId%>">
                <input type="hidden" name="bussColId" id="bussColId" value="<%=bussColId%>">
                <input type="hidden" name="timebussTableId" id="timebussTableId" value="<%=timebussTableId%>">
                <input type="hidden" name="tabName" id="tabName" value="<%=tabName%>">
                <input type="hidden" name="colName" id="colName" value="<%=colName%>">
                <input type="hidden" name="colType" id="colType" value="<%=colType%>">
                <input type="hidden" name="preminlevel" id="preminlevel" value="<%=preminlevel%>">
                <input type="hidden" name="mainfactbussTableId" id="mainfactbussTableId" value="<%=mainfactbussTableId%>">
                <input type="hidden" name="mainfactbussColId" id="mainfactbussColId" value="<%=mainfactbussColId%>">
                <input type="hidden" name="timeDimInfoId" id="timeDimInfoId" value="<%=timeDimInfoId%>">
                <input type="hidden" name="monthcheck" id="monthcheck">
            </form>
        </center>

<script>
            function saveShowTimeDim(minTimeLevel)
            {   //var minTimeLevel=document.getElementById("minTimeLevel").value;
                // alert('save'+minTimeLevel)
                if(minTimeLevel==3){
                    if(document.getElementById("monthJoinType").value=="F"){

                        if(document.getElementById("monthFormat").value!=""){

                            var n = document.getElementById("monthFormat").value.length;
                            var numlen=0;
                            for (i=0;i<n; i++){
                                cchar=document.getElementById("monthFormat").value.charAt(i);
                                if ((cchar=='0') ||(cchar=='1')||(cchar=='2')||(cchar=='3')||(cchar=='4')||(cchar=='5')||(cchar=='6')||(cchar=='7') ||(cchar=='8')||(cchar=='9'))
                                {    numlen=1;

                                }
                            }
                            if(numlen==1){
                                alert('Please don\'t enter numeric values  for Format');
                            }
                            else
                            {
                                if(document.getElementById("monthPre").value=="" && document.getElementById("monthPost").value==""){
                                    alert('Please Select Atleast one format type Pre or Post');
                                }
                                else{
                                    document.getElementById("monthcheck").value='Y';
                                    // alert( document.getElementById("monthcheck").value);
                                    document.myForm.action="saveShowTimeDimension.do";
                                    document.myForm.submit();
                                    parent.parentCancelShowTimeDim();
                                }
                            }

                        }else if(document.getElementById("monthPre").value=="" && document.getElementById("monthPost").value==""){
                            alert('Please Enter Atleast one format type Pre or Post');
                        }
                        else{
                            // alert(document.getElementById("monthPre").value+'-------'+document.getElementById("monthPost").value)
                            alert('Please enter Format');
                        }
                    }else{
                        document.getElementById("monthcheck").value='N';
                        // alert( document.getElementById("monthcheck").value);
                        document.myForm.action="saveShowTimeDimension.do";
                        document.myForm.submit();
                        parent.parentCancelShowTimeDim();
                    }
                }else{
                    if(minTimeLevel==5){

                        if(document.getElementById("timeDimKeyValueDay").value==""){
                            alert('This Fact does not support this Minimum Time Level \nPlease Select other Fact')
                        }else{
                            document.myForm.action="saveShowTimeDimension.do";

                            document.myForm.submit();
                            parent.parentCancelShowTimeDim();
                        }
                    }else{
                        document.myForm.action="saveShowTimeDimension.do";

                        document.myForm.submit();
                        parent.parentCancelShowTimeDim();
                    }
                }
            }
            function cancelShowTimeDim()
            {
                parent.parentCancelShowTimeDim();
            }

            function changeLevel(minTimeLevel,rcount,isformatexist)
            {   //alert(minTimeLevel)
                //alert('-----rowcount-----'+rcount+"---------isformatexist----------"+isformatexist);
                //var minTimeLevel=document.getElementById("minTimeLevel").value;
                if(minTimeLevel==5){
                    document.getElementById("dayDiv").style.display='';
                    document.getElementById("weekDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='none';
                    document.getElementById("quarterDiv").style.display='none';
                    document.getElementById("yearDiv").style.display='none';
                }
                else if(minTimeLevel==4){
                    document.getElementById("weekDiv").style.display='';
                    if(rcount==1){
                        document.getElementById("weekSingleDiv").style.display='';
                    }else if(rcount==2){
                        document.getElementById("weekMultipleDiv").style.display='';
                    }else{
                        document.getElementById("weekSingleDiv").style.display='';
                    }
                    document.getElementById("dayDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='none';
                    document.getElementById("quarterDiv").style.display='none';
                    document.getElementById("yearDiv").style.display='none';
                }
                else if(minTimeLevel==3){
                    document.getElementById("dayDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='';
                    if(rcount==1){
                        if(isformatexist=="Y"){
                            document.getElementById("monthFormatDiv").style.display='';
                        }
                        else{
                           
                            document.getElementById("monthSingleDiv").style.display='';
                        }
                    }else if(rcount==2){
                        document.getElementById("monthNormalDiv").style.display='';
                    }else{
                        document.getElementById("monthSingleDiv").style.display='';
                    }

                    document.getElementById("weekDiv").style.display='none';
                    document.getElementById("quarterDiv").style.display='none';
                    document.getElementById("yearDiv").style.display='none';
                }
                else if(minTimeLevel==2){
                    document.getElementById("quarterDiv").style.display='';
                    if(rcount==1){
                        document.getElementById("quarterSingleDiv").style.display='';
                    }else if(rcount==2){
                        document.getElementById("quarterMultipleDiv").style.display='';
                    }else{
                        document.getElementById("quarterSingleDiv").style.display='';
                    }

                    document.getElementById("dayDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='none';
                    document.getElementById("yearDiv").style.display='none';
                    document.getElementById("weekDiv").style.display='none';
                }
                else if(minTimeLevel==1){
                    document.getElementById("yearDiv").style.display='';
                    document.getElementById("dayDiv").style.display='none';
                    document.getElementById("monthDiv").style.display='none';
                    document.getElementById("quarterDiv").style.display='none';
                    document.getElementById("weekDiv").style.display='none';
                }
            }
            function monthFormatChange(){
                if(document.getElementById("monthJoinType").value=="S"){
                    document.getElementById("monthSingleDiv").style.display='';
                    document.getElementById("monthFormatDiv").style.display='none';
                    document.getElementById("monthNormalDiv").style.display='none';
                }else if(document.getElementById("monthJoinType").value=="M"){
                    document.getElementById("monthFormatDiv").style.display='none';
                    document.getElementById("monthNormalDiv").style.display='';
                    document.getElementById("monthSingleDiv").style.display='none';
                }
                else if(document.getElementById("monthJoinType").value=="F"){
                    document.getElementById("monthFormatDiv").style.display='';
                    document.getElementById("monthNormalDiv").style.display='none';
                    document.getElementById("monthSingleDiv").style.display='none';
                }
            }

            function changeWeekJoin(){
                if(document.getElementById("weekJoinType").value=="S"){
                    document.getElementById("weekSingleDiv").style.display='';
                    document.getElementById("weekMultipleDiv").style.display='none';
                }else{
                    document.getElementById("weekSingleDiv").style.display='none';
                    document.getElementById("weekMultipleDiv").style.display='';
                }

            }


            function quarterFormatChange(){
                if(document.getElementById("quarterJoinType").value=="S"){
                    document.getElementById("quarterSingleDiv").style.display='';
                    document.getElementById("quarterMultipleDiv").style.display='none';
                }else{
                    document.getElementById("quarterSingleDiv").style.display='none';
                    document.getElementById("quarterMultipleDiv").style.display='';
                }

            }


        </script>
    </body>
    <%} catch (Exception ex) {
                    ex.printStackTrace();
                }%>
</html>