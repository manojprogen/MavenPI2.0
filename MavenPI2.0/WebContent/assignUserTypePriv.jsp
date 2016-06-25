<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject,java.util.ArrayList" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            String userId = String.valueOf(session.getAttribute("userId"));
            //////.println("userid in assignusertypeprev is : " + userId);
            String userType = request.getParameter("userTypeId");
            //////.println.println("userType" + userType);
            ArrayList repPrevList = new ArrayList();
            String prvQ = "select * from user_type_priveleges where user_type=" + userType;
            //////.println.println(" prvQ "+prvQ);
            String repPrevQry = "select PREVILAGE_NAME from prg_ar_previlage_master where previlage_type='Report'";
            String grpPrevQry = "select PREVILAGE_NAME from prg_ar_previlage_master where previlage_type='Graph'";
            String tabPrevQry = "select PREVILAGE_NAME from prg_ar_previlage_master where previlage_type='Table'";
            String homePrevQry = "select PREVILAGE_NAME from prg_ar_previlage_master where previlage_type='Home'";
            //////.println.println(" repPrevQry "+repPrevQry);
            PbReturnObject pbro = new PbReturnObject();
            PbReturnObject repPrevObj = new PbReturnObject();
            PbReturnObject grpPrevObj = new PbReturnObject();
            PbReturnObject tabPrevObj = new PbReturnObject();
            PbReturnObject homePrevObj = new PbReturnObject();
            PbDb pbdb = new PbDb();
            pbro = pbdb.execSelectSQL(prvQ);
            repPrevObj = pbdb.execSelectSQL(repPrevQry);
            grpPrevObj = pbdb.execSelectSQL(grpPrevQry);
            tabPrevObj = pbdb.execSelectSQL(tabPrevQry);
            homePrevObj = pbdb.execSelectSQL(homePrevQry);
            ArrayList al = new ArrayList();
            for (int m = 0; m < pbro.getRowCount(); m++) {
                al.add(pbro.getFieldValueString(m, "PRIVILEGE"));
            }
            //////.println.println(" al "+al);
            String isSelected = "";
            if (repPrevObj.getRowCount() > 0) {
                for (int i = 0; i < repPrevObj.getRowCount(); i++) {
                    repPrevList.add(repPrevObj.getFieldValueString(i, "PREVILAGE_NAME"));
                }
            }
            //////.println("repPrevList in assignusertypeprevlist is : " + repPrevList);
            boolean showExtraTabs = true;
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi EE</title>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            *{font:11px verdana}
        </style>
      
    </head>
    <body>
        <form name="myUserTypeForm" method="post" action="">
            <table align="center" id="tablesorter" class="tablesorter" width="100%">
                <thead>
                    <tr style="width:100%">
                        <th nowrap style="font-weight:bold;color:#369"><INPUT type="CHECKBOX" id="homePriv" name="homePriv" ONCLICK="changeHomePriv()"> Home Privileges</th>
                        <th nowrap style="font-weight:bold;color:#369"><INPUT type="CHECKBOX" id="reportPriv" name="reportPriv" ONCLICK="changeReportPriv()"> Report Privileges</th>
                        <th nowrap style="font-weight:bold;color:#369"><INPUT type="CHECKBOX" id="tablePriv" name="tablePriv" ONCLICK="changeTablePriv()"> Table Privileges</th>
                        <th nowrap style="font-weight:bold;color:#369"><INPUT type="CHECKBOX" id="graphPriv" name="graphPriv" ONCLICK="changeGraphPriv()"> Graph Privileges</th>
                    </tr>
                </thead>
                <tbody>
                    <tr style="width:100%">
                        <td width="25%">
                            <table width="100%" border="0px" >
                                <%
            for (int homeprev = 0; homeprev < homePrevObj.getRowCount(); homeprev++) {
                    isSelected = "";
                    if (al.contains(homePrevObj.getFieldValueString(homeprev, "PREVILAGE_NAME"))) {
                        isSelected = "checked";
                    }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="<%=homePrevObj.getFieldValueString(homeprev, "PREVILAGE_NAME")%>Check" name="homePrevileges" value="<%=homePrevObj.getFieldValueString(homeprev, "PREVILAGE_NAME")%>">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369"><%=homePrevObj.getFieldValueString(homeprev, "PREVILAGE_NAME")%></b>
                                        <input type="hidden" name="<%=homePrevObj.getFieldValueString(homeprev, "PREVILAGE_NAME")%>check" id="<%=homePrevObj.getFieldValueString(homeprev, "PREVILAGE_NAME")%>check">
                                    </td>
                                </tr>
                                <%
            }%>
                                <%--<%
                                    isSelected = "";
                                    if (al.contains("All Reports")) {
                                        isSelected = "checked";
                                    }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="AllCheck" name="homePrevileges"  value="All Reports">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">All Reports</b>
                                        <input type="hidden" name="allcheck" id="allcheck">
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Administrator")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox"  <%=isSelected%>id="AdminCheck" name="homePrevileges"  value="Administrator">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Administrator</b>
                                        <input type="hidden" name="admincheck" id="admincheck">
                                    </td>
                                </tr>
                                <%
            if (showExtraTabs) {
                isSelected = "";
                if (al.contains("Dashboard Studio")) {
                    isSelected = "checked";
                }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="DsCheck" name="homePrevileges"  value="Dashboard Studio">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Dashboard Studio</b>
                                        <input type="hidden" name="dscheck" id="dscheck">
                                    </td>
                                </tr>
                                <%
            }
            isSelected = "";
            if (al.contains("Report Studio")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox"  <%=isSelected%> id="RsCheck" name="homePrevileges"  value="Report Studio">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Report Studio</b>
                                        <input type="hidden" name="rscheck" id="rscheck">
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Query Studio")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="QsCheck" name="homePrevileges"  value="Query Studio">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Query Studio</b>
                                        <input type="hidden" name="qscheck" id="qscheck">
                                </tr>
                                <%
            if (showExtraTabs) {
                isSelected = "";
                if (al.contains("Messages")) {
                    isSelected = "checked";
                }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="MsgCheck" name="homePrevileges"  value="Messages">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Messages</b>
                                        <input type="hidden" name="msgcheck" id="msgcheck">
                                    </td>
                                </tr>
                                <%
                isSelected = "";
                if (al.contains("Alerts")) {
                    isSelected = "checked";
                }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="AlrtCheck" name="homePrevileges"  value="Alerts">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Alerts</b>
                                        <input type="hidden" name="alrtcheck" id="alrtcheck">
                                        <input type="hidden" name="userId" id="userId">
                                    </td>
                                </tr>
                                <%
                isSelected = "";
                if (al.contains("Portals")) {
                    isSelected = "checked";
                }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="PrtlCheck" name="homePrevileges"  name="PrtlCheck" value="Portals">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Portals</b>
                                        <input type="hidden" name="prtlcheck" id="prtlcheck">
                                    </td>
                                </tr>
                                <%
                isSelected = "";
                if (al.contains("Admin")) {
                    isSelected = "checked";
                }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="AdminCheckCopy" name="homePrevileges" value="Admin">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Admin</b>
                                        <input type="hidden" name="adminCheckcopy" id="adminCheckcopy">
                                    </td>
                                </tr>
                                <%
                isSelected = "";
                if (al.contains("DataCorrection")) {
                    isSelected = "checked";
                }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="DataCorrectionCheck" name="homePrevileges" value="DataCorrection">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Data Correction</b>
                                        <input type="hidden" name="datacorrection" id="datacorrection">
                                    </td>
                                </tr>
                                <%}%>--%>
                            </table>
                        </td>
                        <td width="25%">
                            <table width="100%" border="0px" >
                                <%
            for (int repprev = 0; repprev < repPrevObj.getRowCount(); repprev++) {
                
                    isSelected = "";
                    if (al.contains(repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME"))) {
                        isSelected = "checked";
                    }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <%if (repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME").equalsIgnoreCase("BizRoles")) {%>
                                        <input type="checkbox" <%=isSelected%> id="<%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%>Check" name="reportPrevileges" value="<%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%>">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Business Roles</b>
                                        <input type="hidden" name="<%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%>Check" id="<%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%>Check" >
                                        <%} else {%>
                                        <input type="checkbox" <%=isSelected%> id="<%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%>Check" name="reportPrevileges" value="<%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%>">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369"><%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%></b>
                                        <input type="hidden" name="<%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%>Check" id="<%=repPrevObj.getFieldValueString(repprev, "PREVILAGE_NAME")%>Check" >
                                        <%}%>
                                    </td>
                                </tr>
                                <%
            }%>
                                <%--
                                <%
            isSelected = "";
            if (al.contains("Compose Message")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">

                                        <input type="checkbox" <%=isSelected%> id="composeCheck" name="reportPrevileges" value="Compose Message">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Compose Message</b>
                                        <input type="hidden" name="composeCheck" id="composeCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("TopBottom")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="topCheck" name="reportPrevileges" value="TopBottom">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Top/Bottom</b>
                                        <input type="hidden" name="topCheck" id="topCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Customize Report")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">

                                        <input type="checkbox" <%=isSelected%> id="customCheck"  name="reportPrevileges" value="Customize Report">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Customize Report</b>
                                        <input type="hidden" name="customCheck" id="customCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("SnapShot")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">

                                        <input type="checkbox" <%=isSelected%> id="snapCheck" name="reportPrevileges" value="SnapShot">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">SnapShot</b>
                                        <input type="hidden" name="snapCheck" id="snapCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Scheduler")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="schedCheck" name="reportPrevileges" value="Scheduler">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Scheduler</b>
                                        <input type="hidden" name="schedCheck" id="schedCheck" >
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("MessgTab")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="messageCheck" name="reportPrevileges" value="MessgTab">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Messages</b>
                                        <input type="hidden" name="messageCheck" id="messageCheck">
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Tracker")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="trakCheck" name="reportPrevileges" value="Tracker">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Tracker</b>
                                        <input type="hidden" name="trakCheck" id="trakCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("BizRoles")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="bizRoleCheck" name="reportPrevileges" value="BizRoles">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Business Roles</b>
                                        <input type="hidden" name="bizRoleCheck" id="bizRoleCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("StickyNote")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="stickyNoteCheck" name="reportPrevileges" value="StickyNote">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Sticky Notes</b>
                                        <input type="hidden" name="stickyNoteCheck" id="stickyNoteCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Save as New Report")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox"<%=isSelected%>  id="editCheck" name="reportPrevileges" value="Save as New Report">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Save as New Report</b>
                                        <input type="hidden" name="editCheck" id="editCheck" >
                                        <input type="hidden" name="userId" id="userId" >
                                    </td>
                                </tr>--%>
                            </table>
                        </td>
                        <td width="25%">
                            <table width="100%" border="0px">
                                <%
            for (int tabprev = 0; tabprev < tabPrevObj.getRowCount(); tabprev++) {
                
                    isSelected = "";
                    if (al.contains(tabPrevObj.getFieldValueString(tabprev, "PREVILAGE_NAME"))) {
                        isSelected = "checked";
                    }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="<%=tabPrevObj.getFieldValueString(tabprev, "PREVILAGE_NAME")%>Check" name="tablePrevileges" value="<%=tabPrevObj.getFieldValueString(tabprev, "PREVILAGE_NAME")%>">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369"><%=tabPrevObj.getFieldValueString(tabprev, "PREVILAGE_NAME")%></b>
                                        <input type="hidden" name="<%=tabPrevObj.getFieldValueString(tabprev, "PREVILAGE_NAME")%>Check" id="<%=tabPrevObj.getFieldValueString(tabprev, "PREVILAGE_NAME")%>Check" >
                                    </td>
                                </tr>
                                <%
            }%>
                                <%--<%
            isSelected = "";
            if (al.contains("Hide/Show Columns")) {
                isSelected = "checked";
            }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="hideShowColsCheck" name="tablePrevileges" value="Hide/Show Columns">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Hide/Show Columns</b>
                                        <input type="hidden" name="hideShowColsCheck" id="hideShowColsCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Table Properties")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="TablePropertiesCheck" name="tablePrevileges" value="Table Properties">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Table Properties</b>
                                        <input type="hidden" name="TablePropertiesCheck" id="TablePropertiesCheck" >
                                    </td>
                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Hide Table")) {
                isSelected = "checked";
            }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="hideTableCheck" name="tablePrevileges" value="Hide Table">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Hide Table</b>
                                        <input type="hidden" name="hideTableCheck" id="hideTableCheck" >
                                    </td>
                                </tr>
                                <%
            if (showExtraTabs) {
                isSelected = "";
                if (al.contains("Transpose Table")) {
                    isSelected = "checked";
                }
                                 %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="transTableCheck" name="tablePrevileges" value="Transpose Table">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Transpose Table</b>
                                        <input type="hidden" name="transTableCheck" id="transTableCheck" >
                                        <input type="hidden" name="userId" id="userId" >
                                    </td>
                                </tr>
                                <%}%>--%>
                            </table>
                        </td>
                        <td width="25%">
                            <table width="100%" border="0px">
                                <%
            for (int grpprev = 0; grpprev < grpPrevObj.getRowCount(); grpprev++) {
               // if (grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME").equalsIgnoreCase("Graph Properties")) {
                   // if (showExtraTabs) {
                     //   isSelected = "";
                      //  if (al.contains(grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME"))) {
                      //      isSelected = "checked";
                      //  }
                                %><%--
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="<%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%>Check" name="graphPrevileges" value="<%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%>">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369"><%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%></b>
                                        <input type="hidden" name="<%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%>Check" id="<%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%>Check" >
                                    </td>
                                </tr>
                                --%><%//}
               // } else {
            isSelected = "";
                    if (al.contains(grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME"))) {
                isSelected = "checked";
            }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="<%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%>Check" name="graphPrevileges" value="<%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%>">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369"><%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%></b>
                                        <input type="hidden" name="<%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%>Check" id="<%=grpPrevObj.getFieldValueString(grpprev, "PREVILAGE_NAME")%>Check" >
                                    </td>
                    </tr>
                                <%//}
            }%>
                                <%--<%
            isSelected = "";
            if (al.contains("Graph Columns")) {
                isSelected = "checked";
            }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="grpcolsCheck" name="graphPrevileges" value="Graph Columns">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Graph Columns</b>
                                        <input type="hidden" name="grpcolsCheck" id="grpcolsCheck" >
                                    </td>

                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Rows")) {
                isSelected = "checked";
            }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="rowsCheck" name="graphPrevileges" value="Rows">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Rows</b>
                                        <input type="hidden" name="rowsCheck" id="rowsCheck" >
                                    </td>
                                </tr>
                                <%
            if (showExtraTabs) {
        isSelected = "";
        if (al.contains("Graph Properties")) {
            isSelected = "checked";
        }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="grpprpCheck" name="graphPrevileges" value="Graph Properties">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Graph Properties</b>
                                        <input type="hidden" name="grpprpCheck" id="grpprpCheck" >
                                    </td>

                                </tr>
                                <%
            }
            isSelected = "";
            if (al.contains("Add Graphs")) {
                isSelected = "checked";
            }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="addGraphsCheck" name="graphPrevileges" value="Add Graphs">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Add Graphs</b>
                                        <input type="hidden" name="addGraphsCheck" id="addGraphsCheck" >
                                    </td>
                                </tr>
                                <%
        isSelected = "";
        if (al.contains("Edit Graph Title")) {
            isSelected = "checked";
        }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" <%=isSelected%> id="editGrpNameCheck" name="graphPrevileges" value="Edit Graph Title">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Edit Graph Name</b>
                                        <input type="hidden" name="editGrpNameCheck" id="editGrpNameCheck" >
                                    </td>

                                </tr>
                                <%
            isSelected = "";
            if (al.contains("Delete Graph")) {
                isSelected = "checked";
            }
                                %>
                                <tr style="width:100%">
                                    <td width="100%">
                                        <input type="checkbox" id="deleteGraphCheck" name="graphPrevileges" value="Delete Graph">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Delete Graph</b>
                                        <input type="hidden" name="deleteGraphCheck" id="deleteGraphCheck" >
                                    </td>
                                </tr>--%>
                            </table>
                        </td>
                    </tr>
                </tbody>
            </table>
            <br/>
            <table align="center"  width="100%"><tr >
                    <td width="50%" align="center">
                        <center>
                            <input type="button" class="navtitle-hover" value="Save" onclick="saveUserTypePriv()">
                        </center>
                    </td>
                </tr></table>
        </form>
                              <script type="text/javascript">            
            function changeHomePriv()
            {
                var homePriv=document.getElementsByName("homePrevileges");
                 for(var i=0;i<homePriv.length;i++){
                    if(document.getElementById("homePriv").checked){
                        homePriv[i].checked=true;
                    }
                    else{
                        homePriv[i].checked=false;
                    }
                }
            }
            function changeReportPriv()
            {
                var reportPriv=document.getElementsByName("reportPrevileges");
                 for(var i=0;i<reportPriv.length;i++){
                    if(document.getElementById("reportPriv").checked){
                        reportPriv[i].checked=true;
                    }
                    else{
                        reportPriv[i].checked=false;
                    }
                }
            }

            function changeTablePriv()
            {
                 var tablePrevileges=document.getElementsByName("tablePrevileges");
                 for(var i=0;i<tablePrevileges.length;i++){
                    if(document.getElementById("tablePriv").checked){
                        tablePrevileges[i].checked=true;
                    }
                    else{
                        tablePrevileges[i].checked=false;
                    }
                }
            }
            function changeGraphPriv()
            {
                 var graphPriv=document.getElementsByName("graphPrevileges");
                 for(var i=0;i<graphPriv.length;i++){
                    if(document.getElementById("graphPriv").checked){
                        graphPriv[i].checked=true;
                    }
                    else{
                        graphPriv[i].checked=false;
                    }
                }
            }
            function saveUserTypePriv(){
                document.forms.myUserTypeForm.action="saveUserTypePrivileges.do?previlegesType=saveUserTypePrevileges&userType=<%=userType%>";
                document.forms.myUserTypeForm.method="post";
                document.forms.myUserTypeForm.submit();
                parent.$("#assignPriviDialog").dialog('close');
            }
            <%--
                var busVar = document.getElementById("BusCheck").checked;
                var allVar = document.getElementById("AllCheck").checked;
                var adminVar = document.getElementById("AdminCheck").checked;
                var dsVar = document.getElementById("DsCheck").checked;
                var rsVar = document.getElementById("RsCheck").checked;
                var qsVar = document.getElementById("QsCheck").checked;
                var msgVar = document.getElementById("MsgCheck").checked;
                var alrtVar = document.getElementById("AlrtCheck").checked;
                var prtlVar = document.getElementById("PrtlCheck").checked;
                var dataCorrectVar = document.getElementById("DataCorrectionCheck").checked;
                var admincopyVar = document.getElementById("AdminCheck").checked;

                var FavouriteLinks = document.getElementById("linkCheck").checked;
                var ComposeMessage = document.getElementById("composeCheck").checked;
                var TopBottom = document.getElementById("topCheck").checked;
                var CustomizeReport = document.getElementById("customCheck").checked;
                var SnapShot = document.getElementById("snapCheck").checked;
                var Scheduler = document.getElementById("schedCheck").checked;
                var MessgTab = document.getElementById("messageCheck").checked;
                var Tracker = document.getElementById("trakCheck").checked;
                var BizRole = document.getElementById("bizRoleCheck").checked;
                var StickyNote = document.getElementById("stickyNoteCheck").checked;
                var edit = document.getElementById("editCheck").checked;

                var expCheck = document.getElementById("ExpCheck").checked;
                var hideShowColsCheck = document.getElementById("hideShowColsCheck").checked;
                var TablePropertiesCheck = document.getElementById("TablePropertiesCheck").checked;
                var hideTableCheck = document.getElementById("hideTableCheck").checked;
                var transTableCheck = document.getElementById("transTableCheck").checked;

                if(expCheck == true)
                {
                    var expCheck=document.getElementById("ExpCheck").value //= document.getElementById("composeCheck").value;
                }
                if(hideShowColsCheck == true)
                {
                    var hideShowColsCheck=document.getElementById("hideShowColsCheck").value //= document.getElementById("topCheck").value;
                }
                if(TablePropertiesCheck == true)
                {
                    var TablePropertiesCheck=document.getElementById("TablePropertiesCheck").value //= document.getElementById("customCheck").value;
                }
                if(hideTableCheck == true)
                {
                    var hideTableCheck=document.getElementById("hideTableCheck").value //= document.getElementById("snapCheck").value;
                }
                if(transTableCheck == true)
                {
                    var transTableCheck=document.getElementById("transTableCheck").value //= document.getElementById("schedCheck").value;
                }

                if(FavouriteLinks == true)
                {
                    var flink=document.getElementById("linkCheck").value //= document.getElementById("linkCheck").value;
                }
                if(ComposeMessage == true)
                {
                    var cmsg=document.getElementById("composeCheck").value //= document.getElementById("composeCheck").value;
                }
                if(TopBottom == true)
                {
                    var topbot=document.getElementById("topCheck").value //= document.getElementById("topCheck").value;
                }
                if(CustomizeReport == true)
                {
                    var custrep=document.getElementById("customCheck").value //= document.getElementById("customCheck").value;
                }
                if(SnapShot == true)
                {
                    var snap=document.getElementById("snapCheck").value //= document.getElementById("snapCheck").value;
                }
                if(Scheduler == true)
                {
                    var sch=document.getElementById("schedCheck").value //= document.getElementById("schedCheck").value;
                }
                if(MessgTab == true)
                {
                    var msgtab=document.getElementById("messageCheck").value //= document.getElementById("messageCheck").value;
                }
                if(Tracker == true)
                {
                    var track=document.getElementById("trakCheck").value //= document.getElementById("trakCheck").value;
                }
                if(BizRole == true)
                {
                    var bRole=document.getElementById("bizRoleCheck").value //= document.getElementById("trakCheck").value;
                }
                if(StickyNote == true)
                {
                    var sticky=document.getElementById("stickyNoteCheck").value //= document.getElementById("trakCheck").value;
                }
                if(edit == true)
                {
                    var edit=document.getElementById("editCheck").value //= document.getElementById("trakCheck").value;
                }

                if(busVar == true)
                {
                    var buscheck = document.getElementById("buscheck").value = document.getElementById("BusCheck").value;
                }
                if(allVar == true)
                {
                    var allcheck =   document.getElementById("allcheck").value = document.getElementById("AllCheck").value;
                }
                if(adminVar == true)
                {
                    var admincheck = document.getElementById("admincheck").value = document.getElementById("AdminCheck").value;
                }
                if(dsVar == true)
                {
                    var dscheck = document.getElementById("dscheck").value = document.getElementById("DsCheck").value;
                }
                if(rsVar == true)
                {
                    var rscheck = document.getElementById("rscheck").value = document.getElementById("RsCheck").value;
                }
                if(qsVar == true)
                {
                    var qscheck = document.getElementById("qscheck").value = document.getElementById("QsCheck").value;
                }
                if(msgVar == true)
                {
                    var msgcheck =  document.getElementById("msgcheck").value = document.getElementById("MsgCheck").value;
                }
                if(alrtVar == true)
                {
                    var alrtcheck =  document.getElementById("alrtcheck").value = document.getElementById("AlrtCheck").value;

                }
                if(prtlVar == true)
                {
                    var prtlcheck = document.getElementById("prtlcheck").value = document.getElementById("PrtlCheck").value;
                }
                if(dataCorrectVar == true)
                {
                    var dataCorrectCheck = document.getElementById("datacorrection").value = document.getElementById("DataCorrectionCheck").value;
                }
                if(admincopyVar == true)
                {
                    var adminCheckcopy = document.getElementById("datacorrection").value = document.getElementById("AdminCheckCopy").value;
                }

            

            if(busVar == false && allVar == false && adminVar == false && dsVar == false && rsVar == false && qsVar == false && msgVar ==false && alrtVar == false && prtlVar==false  && admincopyVar==false && dataCorrectVar==false && expCheck == false && hideShowColsCheck == false && TablePropertiesCheck == false && hideTableCheck == false && transTableCheck == false && FavouriteLinks == false && ComposeMessage == false && TopBottom == false && CustomizeReport == false && SnapShot == false && Scheduler == false && MessgTab ==false && Tracker == false && BizRole == false && StickyNote == false && edit == false)
            {
                alert('Please Select Atleastone')
            }else{
                //previlegesType
                document.forms.myUserTypeForm.action="saveUserTypePrivileges.do?previlegesType=saveUserTypePrevileges";
                document.forms.myUserTypeForm.method="post";
                alert(document.forms.myUserTypeForm.action)
                document.forms.myUserTypeForm.submit();
            }--%>

        </script>
    </body>
</html>
