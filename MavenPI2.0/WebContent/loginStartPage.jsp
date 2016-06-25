
<%@page contentType="text/html" pageEncoding="windows-1252" import="utils.db.*,com.progen.reportdesigner.db.ReportTemplateDAO,prg.db.PbDb,java.util.*,prg.db.Session,prg.db.PbReturnObject,java.sql.*,prg.db.PbReturnObject,prg.db.Session"%>

<%
         String themeColor = "blue";
if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
         String contextPath=request.getContextPath();
%>
<html>
    <head>
        <title>pi 1.0</title>
        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

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
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/ui.tabs.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/css.css" rel="stylesheet"/>


        <script type="text/javascript">
            $(function() {
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});
            });
            function viewReport(path){
                document.forms.myForm.action=path;
                document.forms.myForm.submit();
            }
        </script>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="../JS//ui.core.js"></script>
        <script type="text/javascript" src="../JS/ui.tabs.js"></script>
        <script type="text/javascript" src="../JS/ui.sortable.js"></script>
        <link type="text/css" href="../css/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="../css/jquery/ui.all.css" rel="stylesheet" />
        <style>
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .tabsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            a {font-family:Verdana;cursor:pointer;}
            .startpage {
                display:none;
                position: absolute;
                top: 15%;
                left: 24%;
                width:800px;
                height:400px;
                padding: 5px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }
            .black_start{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                overflow:auto;
            }

        </style>
        <script type="text/javascript">
         $(document).ready(function(){

                $("#hr").treeview({
                    animated: "normal",
                    unique:true
                });

                $("#mr").treeview({
                    animated: "normal",
                    unique:true
                });

                $("#lr").treeview({
                    animated: "normal",
                    unique:true
                });
            });
            </script>
        
    </head>
    <body>

        <%
        try{


        String strpage=request.getParameter("strpage");
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("strpage");
        String strlist[]=strpage.split(",");
        String repType;
        if(strlist.length>1){
           repType=strlist[1].split("~")[0];
           ////////////////////////////////////////////////////////////////////////////////////////.println.println("repTye==="+repType);
         }
         PbDb pbdb = new PbDb();
            String userId = "";


            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            //String userId="82";
            //session.setAttribute("USERID",userId);
            //  String userFoldersql = "SELECT FOLDER_ID, FOLDER_NAME FROM PRG_USER_FOLDER where folder_id in(SELECT  USER_FOLDER_ID FROM PRG_GRP_USER_FOLDER_ASSIGNMENT where user_id=" + userId + ")";
            String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
            AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userId;
            AvailableFiolers += " union ";
            AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
            AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userId;
            AvailableFiolers += "and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userId + ")))";

            // PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
            PbReturnObject folderpbro = pbdb.execSelectSQL(AvailableFiolers);

            //String userreports = "SELECT REPORT_ID, REPORT_NAME FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_USER_REPORTS where  user_id=" + userId + ")";
            String userreports = "SELECT A.REPORT_ID,A.REPORT_NAME FROM PRG_AR_REPORT_MASTER A,(SELECT distinct REPORT_ID FROM PRG_AR_USER_REPORTS WHERE user_id=" + userId + ") B WHERE A.REPORT_ID =b.REPORT_ID";

            PbReturnObject reportpbro = pbdb.execSelectSQL(userreports);

            PbReturnObject rolereppbro = null;
            if (request.getParameter("roleId") != null) {
                String folderId = request.getParameter("roleId");
                ////////////////////////////////////////////////////////////////////////////////////////////.println.println("folderId----" + folderId);
                String rolerepdashs = "SELECT REPORT_ID, REPORT_NAME,REPORT_TYPE FROM PRG_AR_REPORT_MASTER where REPORT_ID in(SELECT  REPORT_ID FROM PRG_AR_REPORT_DETAILS where  folder_id=" + folderId + ")";
                rolereppbro = pbdb.execSelectSQL(rolerepdashs);

            }

            // added by susheela start
            Connection con = ProgenConnection.getInstance().getCustomerConn();
            Statement st = con.createStatement();
            ResultSet rs = null;

            String alertQ = "select alert_id,alert_name, risk_status from alert_master where user_id=" + userId;
            rs = st.executeQuery(alertQ);
            rs.next();           
            PbReturnObject alertReturn2 = new PbReturnObject(rs);
            rs.close();
            st.close();
            con.close();
           

            //added by chiranjeevi end
%>
      
        <form name="myFormH" method="post">
            <table>
                <tr>
                    <td valign="top" style="height:30px;width:8%;">
                        <img width="100%" height="100%"  title="pi " src="<%=request.getContextPath()%>/images/pi_logo.gif"/>
                    </td>
                    <td valign="top" style="height:30px;" align="right">

                    </td>
                    <td valign="top" style="height:30px;width:8%;">
                        <img width="100%" height="100%"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/ProGen_Logo.jpg"/>
                    </td>
                </tr>
            </table>
            <table width="100%" class="ui-corner-all">
                <tr>
                    <td valign="top" style="height:10px;width:10%" align="right">
                        <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                        <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |
                        <a href="#" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |
                        <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#369;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>
                    </td>

                </tr>
            </table>
            <div id="tabs" style="width:100%;min-height:500px;max-height:100%">
                <ul>
                    <%if (strlist.length==1) {%>

<% if (strpage.equalsIgnoreCase("Business Roles")) {%>
                    <li><a href="#RolesTab" title="Business Roles">Business Roles</a></li>
                    <%} else {%>
                    <li><a href="#RolesTab" title="Business Roles" style="display:none">Business Roles</a></li>
                    <%}
    if (strpage.equalsIgnoreCase("All Reports")) {
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("All Reports");%>
                    <li ><a href="reportTemplateAction.do?templateParam=getAllReportshome" title="All Reports">All Reports</a></li>
                    <%} else {%>
                    <li ><a href="reportTemplateAction.do?templateParam=getAllReportshome" title="All Reports" style="display:none">All Reports</a></li>
                    <%}
    if (strpage.equalsIgnoreCase("Administrator")) {
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("Administrator");%>
                    <!--Added by chiranjeevi for tabs-->
                    <li><a href="#AdminTab" title="Administrator">Administrator</a></li>
                    <%} else {%>
                    <li><a href="#AdminTab" style="display:none" title="Administrator">Administrator</a></li>
                    <%}
    if (strpage.equalsIgnoreCase("Dashboard Studio")) {
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("Dashboard Studio");%>
                    <li><a href="reportTemplateAction.do?templateParam=getAllDashs" title="Dashboard Studio">Dashboard Studio</a></li>
                    <%} else {%>
                    <li><a href="reportTemplateAction.do?templateParam=getAllDashs" title="Dashboard Studio" style="display:none">Dashboard Studio</a></li>
                    <%}
    if (strpage.equalsIgnoreCase("Report Studio")) {
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("Report Studio");%>
                    <li><a href="reportTemplateAction.do?templateParam=getAllreps" title="Report Studio">Report Studio</a></li>
                    <%} else {%>
                    <li><a href="reportTemplateAction.do?templateParam=getAllreps" title="Report Studio" style="display:none">Report Studio</a></li>
                    <%}
    if (strpage.equalsIgnoreCase("Query Studio")) {
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("Query Studio");%>
                    <li><a href="#QryTab" title="Query Studio">Query Studio</a></li>
                    <%} else {%>
                    <li><a href="#QryTab" title="Query Studio" style="display:none">Query Studio</a></li>
                    <%}
    if (strpage.equalsIgnoreCase("Messages")) {
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("Messages");%>
                    <li><a href="#MsgTab" title="Messages">Messages</a></li>
                    <%} else {%>
                    <li><a href="#MsgTab" title="Messages" style="display:none">Messages</a></li>
                    <%}
    if (strpage.equalsIgnoreCase("Alerts")) {
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("Alerts");%>
                    <li><a href="#AlrtTab" title="Alerts">Alerts</a></li>
                    <%} else {%>
                    <li><a href="#AlrtTab" title="Alerts" style="display:none">Alerts</a></li>
                    <%}if (strpage.equalsIgnoreCase("Database Connections")) {%>

                     <%} %>
                    <%} else {%>
                    <li><a href="#RolesTab" title="Business Roles">Business Roles</a></li>
                    <li ><a href="reportTemplateAction.do?templateParam=getAllReportshome" title="All Reports">All Reports</a></li>
                    <li><a href="#AdminTab" title="Administrator">Administrator</a></li>
                    <li><a href="reportTemplateAction.do?templateParam=getAllDashs" title="Dashboard Studio">Dashboard Studio</a></li>
                    <li><a href="reportTemplateAction.do?templateParam=getAllreps" title="Report Studio">Report Studio</a></li>
                    <li><a href="#QryTab" title="Query Studio">Query Studio</a></li>
                    <li><a href="#MsgTab" title="Messages">Messages</a></li>
                    <li><a href="#AlrtTab" title="Alerts">Alerts</a></li>

                    <%}%>
                </ul>
                <%if (strpage.equalsIgnoreCase("Business Roles")) {%>
                <div id="RolesTab"  style="width:99%;min-height:125px;max-height:100%;height:auto">
                    <table border="1px solid " class="tabsty" align="center">
                        <tr><td bgcolor="silver">
                                <center>
                                    <div class="prgtableheader2" style="width:570px;background-color:silver;color:black"><b>Business Roles</b></div>
                                </center>
                            </td>
                            <td bgcolor="silver">
                                <center>
                                    <div class="prgtableheader2" style="width:570px;background-color:silver;color:black"><b>Business Role Based Reports</b></div>
                                </center>
                            </td>
                        </tr>
                        <tr><td>
                                <div id="rolebasedrep" style="overflow:auto">
                                    <table width="100%">

                                        <tr>
                                            <td width="50%">

                                                <br>
                                                <div style="height:250px" id="Roles">
                                                    <table border="0" width="570px"  >

                                                        <%
    for (int i = 0; i < folderpbro.getRowCount(); i++) {
                                                        %>
                                                        <tr>
                                                            <td class="welcome" style="font-size:12px">
                                                            <a href="home.jsp?roleId=<%=folderpbro.getFieldValueString(i, 0)%>"> <%=folderpbro.getFieldValueString(i, 1)%> </a>                                             </td>
                                                        </tr>


                                                        <% }
                                                        %>
                                                        <tr><td>&nbsp;</td></tr>

                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </td>
                            <td>
                                <%if (request.getParameter("roleId") != null) {%>
                                <div id="rolereps"   style="overflow-y:auto;overflow-x:hidden">
                                    <table width="100%">
                                        <tr>
                                            <td width="50%">

                                                <br>
                                                <div style="height:250px" id="Roles">
                                                    <table border="0" width="570px"  >

                                                        <%
    for (int i = 0; i < rolereppbro.getRowCount(); i++) {
                                                        %>
                                                        <tr>
                                                            <td style="font-size:12px">
                                                                <%if (rolereppbro.getFieldValueString(i, 2).equalsIgnoreCase("R")) {%>
                                                                <a href="reportViewer.do?reportBy=viewReport&REPORTID=<%=rolereppbro.getFieldValueInt(i, 0)%>"> <%=rolereppbro.getFieldValueString(i, 1)%> </a>    <b></b>
                                                                <%} else {%>
                                                                <a href="dashboardViewer.do?reportBy=viewDashboard&REPORTID=<%=rolereppbro.getFieldValueInt(i, 0)%>&pagename=<%=rolereppbro.getFieldValueString(i, 1)%>"> <%=rolereppbro.getFieldValueString(i, 1)%> </a>    <b></b>
                                                                <%}%>
                                                            </td>
                                                        </tr>


                                                        <% }
                                                        %>

                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <%}%>
                        </td></tr>
                    </table>
                </div>
                <%}%>
                <div id="QryTab"  style="width:99%;min-height:125px;max-height:100%;height:auto">
                    <table cellspacing="30" cellpadding="20" >
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Database Connection#Database_Connection" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/DB Connection.gif"  style="cursor:pointer" title="Database Connection"></a></td>

                                    </tr>

                                </table>
                            </td>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Dimensions#Dimensions" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/Dim.gif"  style="cursor:pointer" title="Dimensions"></a></td>
                                    </tr>

                                </table>
                            </td>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Time SetUp#Time_SetUp" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/Time.gif"  style="cursor:pointer" title="Time Setup"></a></td>
                                    </tr>

                                </table>
                            </td>
                        </tr>
                        <tr align="center">
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Business Groups#Business_Groups" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/Biz Groups.gif"  style="cursor:pointer" title="Business Groups"></a></td>
                                    </tr>

                                </table>
                            </td>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="pbBase.jsp?&pagename=Query Studio&curntpage=Business Roles#Business_Roles" style="text-decoration:none"><img src="<%=request.getContextPath()%>/images/Biz Roles.gif"  style="cursor:pointer" title="Business Roles"></a></td>
                                    </tr>

                                </table>
                            </td>

                        </tr>
                    </table>
                </div>
                <div id="AdminTab"  style="width:99%;min-height:125px;max-height:100%;height:auto">

                    <table cellspacing="50" cellpadding="30" >
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="javascript:void(0)" onclick="gouser()" ><img src="<%=request.getContextPath()%>/images/User.gif"></a></td>
                                    </tr>

                                </table>
                            </td>
                            <td>
                                <table>
                                    <tr> <%
                    String targetUrl = "/reportRunner/Target/newJsps/pbTargetList.jsp?userId=" + userId;

                                        %>
                                        <td><a href=<%=targetUrl%>><img src="<%=request.getContextPath()%>/images/Targets.gif"></a></td>
                                    </tr>

                                </table>
                            </td>
                            <td>
                                <table>
                                    <tr> <%
                    String alertUrl = "/reportRunner/alerts/JSPS/pbAlertList.jsp?userId=" + userId;
                                        %>
                                        <td><a href=<%=alertUrl%>><img src="<%=request.getContextPath()%>/images/Alerts.gif"></a></td>
                                    </tr>

                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td><a href="reportRunner/Target/newJsps/pbTargetList.jsp&pagename=Administrator"><img src="<%=request.getContextPath()%>/images/Deviation.gif"></a></td>
                                    </tr>

                                </table>
                            </td>
                            <td>
                                <table>
                                    <tr>
                                        <% String scenarioUrl = "/reportRunner/Scenario/JSPs/pbScenarioList.jsp?userId=" + userId;%>
                                        <td><a href=<%=scenarioUrl%>><img src="<%=request.getContextPath()%>/images/Scenario.gif"></a></td>
                                    </tr>

                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
                <div id="MsgTab"  style="width:99%;min-height:125px;max-height:100%;height:auto">

                </div>
                <center><div id="AlrtTab"  style="width:99%;min-height:125px;max-height:100%;height:auto">


                        <%-- added by susheela  start--%>
                        <% Connection con2 = ProgenConnection.getInstance().getCustomerConn();
                    Statement st2 = con2.createStatement();
                    ResultSet rs2 = null;
                        %>
                        <table>
                            <tr><td>
                                    <div style="height:400px;overflow:auto" id="highRiskAlerts">
                                        <table border="0" width="400px">
                                            <tr>
                                                <td class="prgtableheader2" style="background-color:red" align="center">
                                                    High Risk Alerts
                                                </td>
                                            </tr>
                                            <tr><td>
                                                    <ul id="hr" class="filetree">
                                                        <li class="closed"><span style="color:red;font-weight:bold;font-size:12px">High Risk Alerts</span>
                                                            <ul id="hr" class="filetree">
                                                                <%  for (int m = 0; m < alertReturn2.getRowCount(); m++) {
                        String aName = alertReturn2.getFieldValueString(m, "ALERT_NAME");
                        String alertRisk = alertReturn2.getFieldValueString(m, "RISK_STATUS");
                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("alertRisk -- " + alertRisk);
                        // String risk = alertRisks.get(n).toString();


                        if (alertRisk.equalsIgnoreCase("HIGH")) {
                            String localAelrtId = String.valueOf(alertReturn2.getFieldValueInt(m, "ALERT_ID"));
                            Session alertsSession2 = new Session();
                            String alertDet = "select * from alert_details where alert_id=" + localAelrtId;
                            rs2 = st2.executeQuery(alertDet);
                            PbReturnObject alerts = new PbReturnObject(rs2);
                            alerts.writeString();

                            ////////////////////////////////////////////////////////////////////////////////////////.println.println("in high risk  aName -" + aName);
                                                                %>
                                                                <li class="closed"><a  style="font-size:12px;color:red" onclick="openMessagewindow('<%=alertReturn2.getFieldValueInt(m, "ALERT_ID")%>')"><%=aName%></a>

                                                                    <ul>
                                                                        <%for (int i = 0; i < alerts.getRowCount(); i++) {
                                                                      ////////////////////////////////////////////////////////////////////////////////////////.println.println(alerts.getFieldValueString(i, "RISK_STATUS") + " --/p value//-- " + alerts.getFieldValueString(i, "PARAMETER_VALUE"));
                                                                      if (alerts.getFieldValueString(i, "RISK_STATUS").equals("HIGH")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:red" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          } else if (alerts.getFieldValueString(i, "RISK_STATUS").equals("MEDIUM")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:orange" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          } else if (alerts.getFieldValueString(i, "RISK_STATUS").equals("LOW")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:green" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          }
                                                                        %>
                                                                        <%}%>

                                                                    </ul>
                                                                </li>
                                                                <%
      }%>

                                                                <%}%>
                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                                <td>
                                    <div style="height:400px;overflow:auto" id="MediumRiskAlerts">
                                        <table border="0" width="400px">
                                            <tr>
                                                <td class="prgtableheader2" style="background-color:orange" align="center">
                                                    Medium Risk Alerts
                                                </td>
                                            </tr>
                                            <tr><td>

                                                    <ul id="mr" class="filetree">
                                                        <li class="closed"><span style="color:orange;font-weight:bold;font-size:12px">Medium Risk Alerts</span>
                                                            <ul id="mr" class="filetree">

                                                                <%  for (int m = 0; m < alertReturn2.getRowCount(); m++) {
                        String aName = alertReturn2.getFieldValueString(m, "ALERT_NAME");
                        String alertRisk = alertReturn2.getFieldValueString(m, "RISK_STATUS");
                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("alertRisk //-- " + alertRisk);
                        // String risk = alertRisks.get(n).toString();

                        if (alertRisk.equalsIgnoreCase("MEDIUM")) {

                            String localAelrtId = String.valueOf(alertReturn2.getFieldValueInt(m, "ALERT_ID"));
                            Session alertsSession2 = new Session();
                            String alertDet = "select * from alert_details where alert_id=" + localAelrtId;
                            rs2 = st2.executeQuery(alertDet);
                            PbReturnObject alerts = new PbReturnObject(rs2);
                            // PbReturnObject alerts = alertClient2.getUserAlertsDetails(alertsSession2);


                                                                %>
                                                                <li class="closed"><a  style="font-size:12px;color:orange" onclick="openMessagewindow('<%=alertReturn2.getFieldValueString(m, "ALERT_ID")%>')"><%=aName%></a>
                                                                    <ul>
                                                                        <%for (int i = 0; i < alerts.getRowCount(); i++) {
                                                                     ////////////////////////////////////////////////////////////////////////////////////////.println.println(alerts.getFieldValueString(i, "RISK_STATUS") + " --///-- ...." + alerts.getFieldValueString(i, "PARAMETER_VALUE"));
                                                                     if (alerts.getFieldValueString(i, "RISK_STATUS").equals("HIGH")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:red" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          } else if (alerts.getFieldValueString(i, "RISK_STATUS").equals("MEDIUM")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:orange" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          } else if (alerts.getFieldValueString(i, "RISK_STATUS").equals("LOW")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:green" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          }
                                                                        %>
                                                                        <%}%>

                                                                    </ul>
                                                                </li>
                                                                <%
                        }

                    }%>
                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                                <td>
                                    <div style="height:400px;overflow:auto" id="LowRiskAlerts">
                                        <table border="0" width="400px">
                                            <tr>
                                                <td class="prgtableheader2" style="background-color:green" align="center">
                                                    Low Risk Alerts
                                                </td>
                                            </tr>
                                            <tr><td>

                                                    <ul id="lr" class="filetree">
                                                        <li class="closed"><span style="color:green;font-weight:bold;font-size:12px">Low Risk Alerts</span>
                                                            <ul id="lr" class="filetree">

                                                                <%  for (int m = 0; m < alertReturn2.getRowCount(); m++) {
                        String aName = alertReturn2.getFieldValueString(m, "ALERT_NAME");
                        String alertRisk = alertReturn2.getFieldValueString(m, "RISK_STATUS");
                        ////////////////////////////////////////////////////////////////////////////////////////.println.println("alertRisk //-- " + alertRisk);
                        // String risk = alertRisks.get(n).toString();

                        if (alertRisk.equalsIgnoreCase("LOW")) {

                            String localAelrtId = String.valueOf(alertReturn2.getFieldValueInt(m, "ALERT_ID"));
                            Session alertsSession2 = new Session();
                            String alertDet = "select * from alert_details where alert_id=" + localAelrtId;
                            rs2 = st2.executeQuery(alertDet);
                            PbReturnObject alerts = new PbReturnObject(rs2);


                                                                %>
                                                                <li class="closed"><a  style="font-size:12px;color:orange" onclick="openMessagewindow('<%=alertReturn2.getFieldValueString(m, "ALERT_ID")%>')"><%=aName%></a>
                                                                    <ul>
                                                                        <%for (int i = 0; i < alerts.getRowCount(); i++) {
                                                                     ////////////////////////////////////////////////////////////////////////////////////////.println.println(alerts.getFieldValueString(i, "RISK_STATUS") + " --///-- " + alerts.getFieldValueString(i, "PARAMETER_VALUE"));
                                                                     if (alerts.getFieldValueString(i, "RISK_STATUS").equals("HIGH")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:red" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          } else if (alerts.getFieldValueString(i, "RISK_STATUS").equals("MEDIUM")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:orange" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          } else if (alerts.getFieldValueString(i, "RISK_STATUS").equals("LOW")) {
                                                                        %>
                                                                        <li class="closed"><a style="font-size:10px;color:green" onclick="openMessagewindowchild('<%=alerts.getFieldValueString(i, "ALERT_ID")%>','<%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%>')"><%=alertReturn2.getFieldValueString(m, "ALERT_NAME")%><%=" '"%><%=alerts.getFieldValueString(i, "PARAMETER_VALUE")%><%="'"%></a>

                                                                        <%
                                                                          }
                                                                        %>
                                                                        <%}%>

                                                                    </ul>
                                                                </li>
                                                                <%
                        }

                    }%>

                                                            </ul>
                                                        </li>
                                                    </ul>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>

                            </td></tr>
                        </table>
                        <%--added by susheela over --%>

                </div></center>
            </div>
            <br>
            <%
                    ////////////////////////////////////////////////////////////////////////////////////////.println.println("session.getAttribute(repList)  " + session.getAttribute("repList"));

                    PbReturnObject Dlist = new ReportTemplateDAO().getAllDashs();
                    PbReturnObject Rlist = new ReportTemplateDAO().getAllreps();
                //PbReturnObject Dlist = null;
                // PbReturnObject Rlist = null;
                // if (session.getAttribute("dashList") != null) {
                //     Dlist = (PbReturnObject) session.getAttribute("dashList");
                // }
                //  if (session.getAttribute("repList") != null) {
                //     Rlist = (PbReturnObject) session.getAttribute("repList");
                //  }
%>
            <div id="reportstart" class="startpage">

                <iframe src="startPage.jsp" frameborder="0" height="100%" width="800px" STYLE='display:block'></iframe>
            </div>

            <div id="fadestart" class="black_start"></div>

            <table width="100%" class="fontsty" >
                <tr style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                    <td style="height:10px;width:100%" bgcolor="#bdbdbd">
                        <center ><font  style="color:#fff;font-size:10px;font-family:verdana;" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold">Progen Business Solutions.</a> All Rights Reserved</font></center>
                    </td>
                </tr>
            </table>

            <%} catch (Exception e) {
            e.printStackTrace();
        }%>

        </form>
<script type="text/javascript">
            $(function() {
                $("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});;
            });        
            function logout(){
                document.forms.myFormH.action="baseAction.do?param=logoutApplication";
                document.forms.myFormH.submit();
            }
            function gohome(){
                document.forms.myFormH.action="baseAction.do?param=goHome";
                document.forms.myFormH.submit();
            }
            function gouser(){
                document.forms.myFormH.action="userList.jsp";
                document.forms.myFormH.submit();
            }
            function goTabs(path){

                document.forms.justSubmit.action=path;
                document.forms.justSubmit.submit();
            }
            function goTest(){

            }

            function hideremain()
            {
                document.getElementById("RolesTab").style.display = "none";
            }
            function showhiden()
            {
                document.getElementById("RolesTab").style.display = "block";
            }
            //added by chiranjeevi start
            function viewDashboardG(path){
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
            function viewReportG(path){
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }

            function goGlobe(){
                document.getElementById('reportstart').style.display='block';
                document.getElementById('fadestart').style.display='block';

            }
            function closeStart(){
                document.getElementById('reportstart').style.display='none';
                document.getElementById('fadestart').style.display='none';

            }
            function goPaths(path){
                parent.closeStart();
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
            //added by chiranjeevi end


            //added by susheela start

           


            function openMessagewindow(value)
            {
                window.open('pbGetAlertMessage.jsp?alertId='+value,'welcome','width=750,height=300',menubar=1)
            }
            function openMessagewindowchild(value,name)
            {
                // alert(name);
                window.open('pbGetAlertMessageChild.jsp?alertId='+value+"&paramVal="+name,'welcome','width=750,height=300',menubar=1)
            }

            //added by susheela over
        </script>
    </body>
</html>

    </body>
</html>
