<%--
    Document   : pbPortalViewer
    Created on : Oct 10, 2009, 6:50:35 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>
<%@page pageEncoding="UTF-8" contentType="text/html" import="prg.db.Container,java.util.*,java.io.*,java.awt.*,utils.db.*,java.awt.Font,java.util.ArrayList,com.progen.action.UserStatusHelper,com.progen.portal.Portal"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>


<%
           String userType = null;
  //  boolean isPortalEnableforUser=false;
    boolean isQDEnableforUser=false;
    boolean isPowerAnalyserEnableforUser=false;
   // boolean isOneViewEnableforUser=false;
   // boolean isScoreCardsEnableforUser=false;
    ServletContext context = getServletContext();
     HashMap<String,UserStatusHelper> statushelper;
     statushelper=(HashMap)context.getAttribute("helperclass");
     UserStatusHelper helper=new UserStatusHelper();
     if(!statushelper.isEmpty()){
        helper=statushelper.get(request.getSession(false).getId());
        if(helper!=null){
        //isPortalEnableforUser=helper.getPortalViewer();
        isQDEnableforUser=helper.getQueryStudio();
        isPowerAnalyserEnableforUser=helper.getPowerAnalyser();
     //   isOneViewEnableforUser=helper.getOneView();
      //  isScoreCardsEnableforUser=helper.getScoreCards();
        userType=helper.getUserType();
        }}
            ArrayList tabIds = new ArrayList();
            String Pagename = "portals";
            String url = "";
            if (request.getAttribute("protalurl") != null) {
                url = request.getAttribute("protalurl").toString();
            }
            brdcrmb.inserting(Pagename, url);
             int PORTALCOUNT=0;
            if(session.getAttribute("PORTALCOUNT")!=null)
             PORTALCOUNT=(Integer) session.getAttribute("PORTALCOUNT");

String themeColor = "blue";
 if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
String conetxtPath=request.getContextPath();
%>

<html>
    <head>
        <title>pi EE</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<link rel="stylesheet" href="<%=conetxtPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=conetxtPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
       <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/jquery.tablesorter.js"></script>
       <script type="text/javascript" src="<%=conetxtPath%>/javascript/jquery.tablesorter.js"></script>
       <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=conetxtPath%>/stylesheets/tablesorterStyle.css" />
        <script type="text/javascript"  language="JavaScript" src="<%=conetxtPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>

<!--        <link rel="stylesheet" href="<%=conetxtPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=conetxtPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=conetxtPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <script src="<%=conetxtPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=conetxtPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/javascript/treeview/demo.js"></script>-->
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=conetxtPath%>/javascript/queryDesign.js"></script>
        <link type="text/css" href="<%=conetxtPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=conetxtPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=conetxtPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <!-- below five lines only added by bharathi reddy on 26-08-09 -->
        <link href="<%=conetxtPath%>/stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="<%=conetxtPath%>/stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="<%=conetxtPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="<%=conetxtPath%>/javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="<%=conetxtPath%>/javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=conetxtPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link href="<%=conetxtPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=conetxtPath%>/stylesheets/metadataButton.css" rel="stylesheet" />


        <style type="text/css">

            * {
                font-family: verdana;
                font-size: 12px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
            .tablesorter {
                background-color: #CDCDCD;
                font-family: arial;
                font-size: 8pt;
                margin: 10px 0 15px;
                text-align: left;
                width: 100%;
            }

        </style>
        <script type="text/javascript">
         $(document).ready(function(){
                //                if($("#portalTabs tabUl").html()==null){
                //                   $("#portalTabs").css('height','100%')
                //                }else{
                //                      $("#portalTabs").css('height','auto')
                //                }
                $("#breadCrumb").jBreadCrumb();
                $("#portalTabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});
                $("#portalTabs").tabs({ cookie: { expires: 30 } });
               //ver $tabs = $('#portalTabs').tabs();
               <%if(PORTALCOUNT==0){%>
                     $('#portalTabs').tabs('select', 1);
               <%}%>
                $("#filterDiv").dialog({
                    autoOpen: false,
                    height: 500,
                    width: 700,
                    position: 'justify',
                    modal: true
                });


            });
            </script>
       
        <style type="text/css">
            .liClass{
                color:white;
                width:300px;
            }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            a {font-family:Verdana;cursor:pointer;;text-decoration:none}
            a:link {color:#336699}
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }
            /*   .body{
            background-color:#e6e6e6;
            }*/
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
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .column { width: 420px; float: left; padding-bottom: 50px;padding-left:10px }
            .portlet { margin: 0 1em 1em 0; }
            .portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em;cursor:move }
            .portlet-header .ui-icon { float: right; }
            .portlet-content { padding: 0.4em; }
            .ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 100px !important; }
            .ui-sortable-placeholder * { visibility: hidden; }
            /* .body{
            background-color:#e6e6e6;
            }*/
            #ui-datepicker-div
            {
                z-index: 9999999;
            }

        </style>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbPortalTabViewerJS.js"></script>
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
         <script type="text/javascript"  src="<%=request.getContextPath()%>/tracker/JS/dateSelection.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>-->
    </head>
    <body class="body" >
        <table style="width:100%;">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>


        <form name="frmParameter" method="post" action="">

            <table  style="width:100%"  >
                <tr>
                    <td align="left" width="20%">
                        <a href="javascript:void(0);" onclick="goPortalTab()" style="text-decoration:none;color: #4F4F4F;" ><b>Add Portal</b></a>&nbsp;
                        <a href="javascript:void(0);" onclick="deletePortalTab()" style="text-decoration:none;color: #4F4F4F;" ><b>Delete Portal(s)</b></a>
                    </td>
                    <td width="60%" valign="top" align="left">
                        <div id='breadCrumb' class='breadCrumb module' style="width:500px">
                            <ul>
                                <li style="display:none"></li>
                                <li style="display:none"></li>
                                <% String pgnam = "";
                                            if (brdcrmb.getPgname1() != null) {
                                                pgnam = brdcrmb.getPgname1().toString();
                                                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:verdana;font-size:12px;color: #4F4F4F;font-weight:bold;">
                                    <%=brdcrmb.getPgname1()%>
                                </li>

                                <%
                                                                                } else {
                                %>
                                <li>
                                    <a href='<%=brdcrmb.getPgurl1()%>'><%=brdcrmb.getPgname1()%></a>
                                </li>
                                <%
                                                }
                                            }
                                            if (brdcrmb.getPgname2() != null) {
                                                pgnam = brdcrmb.getPgname2().toString();
                                                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:verdana;font-size:12px;color: #4F4F4F;font-weight:bold;">
                                    <%=brdcrmb.getPgname2()%>
                                </li>
                                <%
                                                                                } else {
                                %>
                                <li>
                                    <a href='<%=brdcrmb.getPgurl2()%>'><%=brdcrmb.getPgname2()%></a>
                                </li>
                                <%                    }
                                            }
                                            if (brdcrmb.getPgname3() != null) {
                                                pgnam = brdcrmb.getPgname3().toString();
                                                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:verdana;font-size:12px;color: #4F4F4F;font-weight:bold;">
                                    <%=brdcrmb.getPgname3()%>
                                </li>
                                <%
                                                                                } else {
                                %>
                                <li>
                                    <a href='<%=brdcrmb.getPgurl3()%>'><%=brdcrmb.getPgname3()%></a>
                                </li>
                                <%
                                                }
                                            }
                                            if (brdcrmb.getPgname4() != null) {
                                                pgnam = brdcrmb.getPgname4().toString();
                                                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:verdana;font-size:12px;color: #4F4F4F;font-weight:bold;">
                                    <%=brdcrmb.getPgname4()%>
                                </li>
                                <%
                                                                                } else {
                                %>
                                <li>
                                    <a href='<%=brdcrmb.getPgurl4()%>'><%=brdcrmb.getPgname4()%></a>
                                </li>
                                <%
                                                }
                                            }
                                            if (brdcrmb.getPgname5() != null) {
                                                pgnam = brdcrmb.getPgname5().toString();
                                                if (pgnam.equalsIgnoreCase(Pagename)) {
                                %>
                                <li style="font-family:verdana;font-size:12px;color: #4F4F4F;font-weight:bold;">
                                    <%=brdcrmb.getPgname5()%>
                                </li>
                                <%
                                                                                } else {
                                %>
                                <li>
                                    <a href='<%=brdcrmb.getPgurl5()%>'><%=brdcrmb.getPgname5()%></a>
                                </li>
                                <%
                                                }
                                            }
                                %>
                                <li style="display:none"></li>
                                <li style="display:none"></li>

                            </ul>
                        </div>

                        <div class="chevronOverlay main"></div>
                    </td>
                    <td  style="height:10px;width:30%" align="right">
                        <!--                        <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                                                <a href="javascript:void(0)" onclick="javascript:gohome()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |-->
                        <!--                        <a href="#" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |-->
                        <!--                        <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>-->
                    </td>
                </tr>
            </table>

            <%
                        StringBuffer portalsBuffer = new StringBuffer("");
                        ArrayList<Portal> portls = new ArrayList();
                        if (session.getAttribute("PORTALS") != null) {
                            portls = (ArrayList) session.getAttribute("PORTALS");
                            portalsBuffer.append("<li style='height:25px'><a title='Portlet Designer' href='metadataportlet.jsp'>Portal Designer</a></li>");
                            for (Portal portal : portls) {
                                if( portal.getPortalID()!=-1 && portal.getPortalID()!=-2)
                                portalsBuffer.append("<li style='height:25px'> <a title='" + portal.getPortalName().trim().replace(" ", "_") + "'  href='portalViewer.do?portalBy=viewPortalTab&TABID=" + portal.getPortalID() + "&TABNAME=" + portal.getPortalName().trim().replace(" ", "_") + "'>" + portal.getPortalName().trim() + "</a></li>");

                            }
                        }

            %>


            <div id="portalTabs" class="tabs" style="min-width: 1350px; max-width: 100%;min-height:inherit;cursor: auto;">
                <ul id="tabUl" style='height:30px'>
                    <%= portalsBuffer.toString()%>
                </ul>
            </div>
            <script>
                //defaultTab();
            </script>


            <table style="width:100%">
                <tr>
                    <td valign="top" style="width:100%;">
                        <jsp:include page="Headerfolder/footerPage.jsp"/>
                    </td>
                </tr>
            </table>

            <div id="createtab" title="Add Portal"  style="display:none">
                <table style="width:100%" border="0" cellpadding="0" cellspacing="0" >
                    <tr valign="top" >
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr valign="top"  align="center">
                        <td valign="top" class="myHead" style="width:30%" align="left">Portal Name</td>
                        <td  valign="top" style="width:70%" align="left">
                            <input type="text" maxlength="35" name="tabName" style="width:80%" id="tabName" onkeyup="tabmsg2()" onfocus="document.getElementById('portalTabsave').disabled = false;"><br>
                        </td>
                    </tr>
                    <%--<tr>
                        <td  valign="top" class="myHead" style="width:30%">Portal Description</td>
                        <td valign="top" style="width:70%">
                            <textarea name="tabDesc" id="tabDesc" style="width:80%"></textarea>
                        </td>
                    </tr>--%>
                    <tr valign="top" >
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr valign="top" >
                        <td colspan="2" align="center">
                            <input type="button" class="navtitle-hover" style="width:auto" value="Create" id="portalTabsave" onclick="saveportalTab()">
                        </td>
                    </tr>
                </table>
                <%-- <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="Create" id="portalTabsave" onclick="saveportalTab()"></td>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelPortalTab()"></td>
                    </tr>
                </table>--%>
            </div>
            <div id="fade" class="black_overlay"></div>
            <!--        <div id="navigateDialog" title="Navigation" STYLE='display:none' >
                        <iframe src="startPage.jsp" frameborder="0" height="100%" width="800px" STYLE='display:block'></iframe>
                    </div>-->
            <div id="fadestart" class="black_start"></div>
            <div id="deletetabs" title="Delete Portals" style="display:none">
                <table style="width:100%" border="0" cellpadding="0" cellspacing="0" >
                    <tr valign="top" >
                        <td colspan="4" align="center">&nbsp;</td>
                    </tr>
                    <%
                                StringBuffer SBPortals = new StringBuffer("");
                                if (session.getAttribute("PORTALS") != null) {
                                    int j = 0;
                                    for (Portal portal : portls) {

                                        int id = portal.getPortalID();
                                        String title = portal.getPortalName().trim().replace(" ", "_");
                                        if (j == 0) {
                                            SBPortals.append("<tr valign='top' align='left'>");
                                        } else if (j % 2 == 0) {
                                            SBPortals.append("</tr>");

                                            SBPortals.append("<tr valign=\"top\" >");
                                            SBPortals.append("<td colspan=\"4\" align=\"center\">&nbsp;</td>");
                                            SBPortals.append("</tr>");

                                            SBPortals.append("<tr  valign='top' align='left'>");
                                        }
                                        SBPortals.append("<td valign='top' align='left'>");
                                        SBPortals.append(title);
                                        SBPortals.append("</td>");
                                        SBPortals.append("<td valign='top' align='left'>");
                                        SBPortals.append("<input type=\"checkbox\" name=\"PortalDeletion\" id=\"PortalDeletion" + id + "\" value=\"" + id + "\" ");
                                        SBPortals.append("</td>");

                                        if (j == (tabIds.size() - 1)) {
                                            SBPortals.append("</tr>");
                                        }
                                        j++;
                                    }
                                }
                                out.print(SBPortals.toString());
                    %>
                    <tr valign="top" >
                        <td colspan="4" align="center">&nbsp;</td>
                    </tr>
                    <tr valign="top" >
                        <td colspan="4" align="center">
                            <input type="button" class="navtitle-hover" style="width:auto" value="Delete Selected Portal(s)" id="deletePortalsBtn" onclick="deletePortalTabs()">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="rulesDiv" style="display:none" title="Rules">
                <iframe id="filterFrame" name="filterFrame" src="about:blank" frameborder="0" width="100%" height="100%" ></iframe>
            </div>
                              <!-- code to edit portlet name-->

        <div id="editportlet-1" style="display:none" title="Edit Portlet Name" >
            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                <tr valign="top" >
                    <td valign="top" class="myHead" style="width:30%">Portlet Name</td>
                    <td valign="top" style="width:70%">
                        <input type="text" maxlength="35" name="editportletName-1" style="width:80%" id="editportletName-1" onkeyup="edittabmsgportlet('-1')" ><br>
                    </td>
                </tr>
                <tr valign="top"  style="height:2px">
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr valign="top" >
                    <td  valign="top" class="myHead" style="width:30%">Description</td>
                    <td valign="top" style="width:70%">
                        <textarea cols="" rows=""  name="editporletDesc-1" id="editportletDesc-1" style="width:80%"></textarea>
                    </td>
                </tr>
                <tr valign="top" >
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr valign="top" align="center">
                    <td valign="top"   colspan="2" align="center">
                        <input type="button" class="navtitle-hover" style="width:auto" value="Update" id="portletsave" onclick="updatePortletName('-1','')">&nbsp;
                        <%-- <input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelEditPortlet('<%=currentTabId%>')">--%>
                    </td>
                </tr>
            </table>
        </div>
        <!-- end of code to edit portlet name-->
            <div id="graphPropertyDiv" style="display:none" title="Graph Properties">
                <iframe id="graphPropertyFrame" frameborder="0" src="about:blank" name="addportletsFrame" width="100%" height="100%"></iframe>
            </div>
            <div id="addPortlets" style="display:none" title="Add Portlet">
                <iframe id="addportletsFrame" frameborder="0" src="about:blank" name="addportletsFrame" width="100%" height="100%"></iframe>
            </div>
                    <div id="rptTypeDialog" title="Portlet Details" style="display: none">
                <table>
                    <tr>
                        <td>Name</td>
                        <td><input type="text" id="portletName" onkeyup="portlettabmsg1()"></td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td><input type="text" id="portletDesc"></td>
                    </tr>
                    <tr>
                        <td>Type</td>
                        <td>
                            <select id="portletRptType">
                                <option value="table">Table</option>
                                <option value="graph">Graph</option>
                                <option value="kpi">Basic KPI</option>
<!--                                <option value="BasicTarget">Basic Target KPI</option>-->
                                <option value="kpigraph">KPI Graph</option>
                            </select>
                        </td>
                    </tr>
                    <tr/>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="button" value="Create" class="navtitle-hover" style="width:auto"  onclick="javascript:createPortletReport()">
                        </td>
                    </tr>
                </table>
            </div>
        </form>
        <div id="graphMeasuresDiv" title="Add Graph Measures" style="display:none">
            <iframe  id="graphMeasures" NAME='graphMeasures' frameborder="0" marginheight="0" marginwidth="0" src='about:blank' width="100%" height="100%"></iframe>
        </div>
         <div style='display:none;' id="time" title="Time">
             <table>
             <tr><td>Date</td>
             <td><input type='text' name="timeOption" style="width:60px;"  id="timeOption"></td>
             </tr>
             <tr><td>PeriodType</td>
                 <td><select id="periodType" name='periodType' title="PeriodType">
                   <option value='Year'>Year</option>
                   <option value='Qtr'>Qtr</option>
                   <option value='Month'>Month</option>
                   <option value='Day'>Day</option>
                     </select></td>
             </tr>
             <tr><td>Compare with</td>
                 <td><select class="myTextbox3" name="CBO_PRG_COMPARE" id="CBO_PRG_COMPARE">
                    <option value="Last Period" selected=""> Last Period </option>
                    <option value="Last Year"> Last Year </option>
                    <option value="Period Complete"> Period Complete </option>
                    <option value="Year Complete"> Year Complete</option>
                </select></td></tr>
              <tr>
             <td><input type=button class="navtitle-hover" name=done id="done" value="Done" onClick="getTimePeriodDetails()"></td>
             <td><input type=button class="navtitle-hover" name=Reset id="Reset" value="Reset" onClick="resetTimePeriodDetails()"></td>
              </tr>
             </table>
                     
        </div>
                     <script type="text/javascript">
            var portletid=""
            var portaltabId=""
            var repid=""
            var dispgrptypObj=""
            var perBy=""
            var gpType=""
            var date=""


           

            var dyna_tabs = {
                tabs: $("#portalTabs"),
                add: function (tab_id, tab_name) {
                    var tabs = $("#portalTabs");
                    if (this.tabs != null) {
                        if (tabs.css('display') == 'none') {
                            tabs.show();
                        }
                        var data = $('<div id="'+tab_id+'" style="margin:0px;padding:0px"></div>');
                        //href='portalViewer.do?portalBy=viewPortalTab&TABID=" + id + "&tab_name=" + title.trim().replace(" ", "_") + "'
                        tabs.append(data).tabs('add', '#'+tab_id, tab_name);
                        tabs.tabs('select', '#'+tab_id);
                    } else {
                        alert('Tabs not initialized!');
                    }
                }
            };
            $(function(){
                //$('#portalTabs').tabs({ cache: true });
                //$("#portalTabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});
            });

            //$(document).ready(function() {
            //var selectedTab = $('.selector').tabs('option', 'selected');
            //alert(selectedTab.className)
            //selectedTab.className="ui-corner-top ui-tabs-selected ui-state-active"
            //var $tabs = $('#tabs').tabs(); // first tab selected

            //$("#tabs").tabs().find(".ui-tabs-nav").sortable({axis:'x'});
            //$("#tabs").tabs();
            /*
             *var $tabs = $('#tabs').tabs(); // first tab selected
                 $('#tabs').bind('tabsselect', function(event, ui) {
                    alert("Index is "+ui.index);
                    $tabs.tabs('select', ui.index)
                });
             */

            //});

            function defaultTab(){
                //var $tabs = $('#portalTabs').tabs(); // first tab selected
                //alert($tabs)
                /*
                 $('#portalTabs').bind('tabsselect', function(event, ui) {
                    alert(ui.index)
                    $tabs.tabs('select', 0);
                });
                 */
                //$("#portalTabs").tabs()

                //$('#portalTabs').tabs({ selected: 0 });//to open selected tab to be opened always
                //ui-corner-top ui-tabs-selected ui-state-active
            }

            function logout(){
                document.forms.frmParameter.action="baseAction.do?param=logoutApplication";
                document.forms.frmParameter.submit();
            }
            function gohome(){
                document.forms.frmParameter.action="baseAction.do?param=goHome";
                document.forms.frmParameter.submit();
            }
            function createPortlet(){
               // alert("in createportlet")
               $("#portletName").val("");
               $("#portletDesc").val("");

                $("#rptTypeDialog").dialog({
                    autoOpen: false,
                    height: 200,
                    width: 300,
                    position: 'justify',
                    modal: true
                });
                if(!<%=isPowerAnalyserEnableforUser%>)
                   alert("You do not have the sufficient previlages")
                 else
                $("#rptTypeDialog").dialog('open');
            }
            function createPortletReport(){
                var rptType = $("#portletRptType").val();
                var portletName = $("#portletName").val();
                var portletDesc = $("#portletDesc").val();

                $("#rptTypeDialog").dialog('close');
                //changed by anitha
                              window.open("portalViewer.do?portalBy=goToPortletCreation&portletName="+portletName+"&portletDesc="+portletDesc+"&rptType="+rptType,"Add_Table",
                "scrollbars=1,width=1200,height=500,address=no");

            }

            function saveportalTab(){
                var parentUL=document.getElementById("tabUl");
                var name=document.getElementById("tabName").value;
                var desc=document.getElementById("tabName").value;

                var PortalTabObj=$('#portalTabs');
                var newTabId=name.replace(' ','').replace(' ','_');



                var childLI=document.createElement("li");
                a=document.createElement("a");
                //href='portalViewer.do?portalBy=viewPortalTab&TABID=" + id + "&TABNAME=" + title.trim().replace(" ", "_") + "'
                a.href="#"+newTabId;
                a.innerHTML=name;
                a.title=name;
                childLI.appendChild(a);
                parentUL.appendChild(childLI);
                var $tabs = PortalTabObj.tabs({
                    add: function(event, ui) {
                        $tabs.tabs('select', '#'+ newTabId);
                    }
                });

                if(jQuery.trim(name)!=""){
                    $.ajax({
                        url: 'portalTemplateAction.do?paramportal=insertTabMasterDet&name='+name+'&desc='+desc,
                        success: function(data) {
                            PortalTabObj.tabs();
                            PortalTabObj.tabs().find(".ui-tabs-nav").sortable({axis:'x'});
                            $("#createtab").dialog('close');
                            var title=name.replace(' ','').replace(' ','_');
                            document.forms.frmParameter.action="pbPortalViewer.jsp#"+jQuery.trim(newTabId.replace(" ","_","gi"))
                            document.forms.frmParameter.submit()
                            //var selected = $("#portalTabs").tabs('option', 'selected');
                            //$("#portalTabs").tabs('option', 'selected', selected);

                            //                        $tabs.tabs('select', '#'+ newTabId);
                            //                         window.location.href=window.location.href;
                            // window.location.reload(true);
                            //dyna_tabs.init('portalTabs',title);
                            //dyna_tabs.add(name,'#' + title);

                        }
                    });
                }else{
                    alert("Please enter portal name")
                }
                //                 $("#portalTabs").css('height','')
            }
            function cancelPortalTab(){
                document.getElementById("tabName").value="";
                $("#createtab").dialog('close');
            }
            function tabmsg2(){
                //document.getElementById('tabDesc').value = document.getElementById('tabName').value;
            }
            function goPortalTab(){
                $("#createtab").dialog({
                    autoOpen: false,
                    height: 50,
                    width: 350,
                    position: 'justify',
                    modal: true
                });
                $("#createtab").dialog('open')
            }
            function deletePortalTab(){
                $("#deletetabs").dialog({
                    autoOpen: false,
                    height: 200,
                    width: 400,
                    position: 'justify',
                    modal: true
                });
                var portalsObj=document.getElementsByName("PortalDeletion");
                for(var i=0;i<portalsObj.length;i++){
                    portalsObj[i].checked=false;
                }
                $("#deletetabs").dialog('open');
            }
            $(function() {
                $(".column").sortable({
                    connectWith: '.column'
                });
                $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                .find(".portlet-header")
                .addClass("ui-widget-header ui-corner-all")
                .prepend('<span class="ui-icon ui-icon-plusthick"></span>')
                .end()
                .find(".portlet-content");

                $(".portlet-header .ui-icon").click(function() {
                    $(this).toggleClass("ui-icon-minusthick");
                    $(this).parents(".portlet:first").find(".portlet-content").toggle();
                });

                $(".column").disableSelection();
                $(".tablesorter").columnFilters().tablesorter({
                    widthFixed: true,
                    widgets: ['zebra']
                }).tablesorterPager({
                    container: $(".pager")
                });
            });
            function viewDashboardG(path){
                document.forms.frmParameter.action=path;
                document.forms.frmParameter.submit();
            }
            function viewReportG(path){
                document.forms.frmParameter.action=path;
                document.forms.frmParameter.submit();
            }
            //
            //            function goGlobe(){
            //                /*document.getElementById('reportstart').style.display='block';
            //                document.getElementById('fadestart').style.display='block';*/
            //                $("#navigateDialog").dialog('open');
            //
            //            }
            //            function closeStart(){
            //                $("#navigateDialog").dialog('close');
            //
            //            }
            function gotoDBCON(ctxPath){
                if(!<%=isQDEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
                document.forms.frmParameter.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                document.forms.frmParameter.submit();
                }
            }
            function goPaths(path){
                var modulecode=path.replace("home.jsp#","");
                var userType='<%=userType%>'
                if(modulecode=='Dashboard_Studio' || modulecode=='Report_Studio'){
                    if(!<%=isPowerAnalyserEnableforUser%>)
                        alert("You do not have the sufficient previlages")
                }else if(modulecode=='pbBIManager.jsp'){
                    if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                        alert("You do not have the sufficient previlages")
                    }
                }
                parent.closeStart();
                document.forms.frmParameter.action=path;
                document.forms.frmParameter.submit();
            }
            function pbBiManager(){
                var path = "";
                if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                    alert("You do not have the sufficient previlages")
                }
                else {
                    path = "srchQueryAction.do?srchParam=pbBiManager";
                }
                parent.closeStart();
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
            function changePortletsOrder(portletTabId){
                var str=$('#PortalColumn1'+portletTabId).sortable('toArray')+";"+$('#PortalColumn2'+portletTabId).sortable('toArray')+";"+$('#PortalColumn3'+portletTabId).sortable('toArray');
                $.ajax({
                    url: 'portalViewer.do?portalBy=savePortletOrder&portletIds='+str+'&tabId='+portletTabId,
                    success: function(data){
                    }
                });
            }
            $(document).ready(function(){
                $("#navigateDialog").dialog({
                    autoOpen: false,
                    height: 460,
                    width: 820,
                    position: 'justify',
                    modal: true
                });
            });
            function deletePortalTabs(){
                var portalsObj=document.getElementsByName("PortalDeletion");
                var portalIds=new Array();
                for(var i=0;i<portalsObj.length;i++){
                    if(portalsObj[i].checked){
                        portalIds.push(portalsObj[i].value);
                    }
                }
                if(portalIds.length!=0){
                    var res=confirm("Are you sure do you want to delete the selected Portals ?");
                    if(res){
                        var portalsObj=document.getElementsByName("PortalDeletion");
                        var portalIds=new Array();
                        for(var i=0;i<portalsObj.length;i++){
                            if(portalsObj[i].checked){
                                portalIds.push(portalsObj[i].value);
                            }
                        }
                        $.ajax({
                            url: 'portalViewer.do?portalBy=deletePortals&portalIds='+portalIds,
                            success: function(data){
                                //                                window.location.href=window.location.href;
                                //                                window.location.reload(true);
                                document.forms.frmParameter.action="pbPortalViewer.jsp#Portlet_Designer"
                                document.forms.frmParameter.submit()
                            }
                        });
                    }
                    $("#deletetabs").dialog('close');
                }else{
                    alert("Please select Portals");
                }
            }
            function openFilter(PortletID,portalTabId1){
             closeOpenDivs(PortletID,portaltabId)
             var portaltab = portaltabId 
             var portalTabName;
             $.ajax({
                            url: 'portalViewer.do?portalBy=getPortalName&portaltabId='+portalTabId1,
                            success: function(data){
                                var temp = data
                                var divId="#rulesDiv";
                $(divId).dialog({
                    autoOpen: false,
                    height: 650,
                    width: 600,
                    position: 'justify',
                    modal: true
                });
                $("#rulesDiv").dialog('open')
                //    alert("PortletID\t"+PortletID)
                $("#filterFrame").attr("src","ruleHelp.jsp?fromModule=PORTAL&portletId="+PortletID+"&portaltabId="+portaltab+"&portalTabName="+temp)
                                 }
                            });                                            

            }

            function openGraphProperty(PortletID,portalID,graphType){
                 closeOpenDivs(PortletID,portalID)
                var divId="#graphPropertyDiv";
                $(divId).dialog({
                    autoOpen: false,
                    height: 400,
                    width: 600,
                    position: 'justify',
                    modal: true
                });
                $("#graphPropertyDiv").dialog('open')
                $("#graphPropertyFrame").attr("src","portletProperty.jsp?PortletID="+PortletID+"&portalID="+portalID+"&graphType="+graphType)


            }
            function addPortlesfromExt(crtPortId){
                var divId="#addPortlets"
                $(divId).dialog({
                    autoOpen: false,
                    height: 500,
                    width: 900,
                    position: 'justify',
                    modal: true
                });
                $("#addPortlets").dialog('open')
                $("#addportletsFrame").attr("src","addportletsFromExisting.jsp?portalID="+crtPortId)
            }
             function portlettabmsg1(){
                document.getElementById('portletDesc').value = document.getElementById('portletName').value;
            }

            function submiturls1(ch){
//                alert("1\t"+ch)
                ch=ch+"&fromModule=drill"
                var portalTabId=ch.split("&")[6].split("=")[1];
                var portletId=(ch.split("&")[1].split("=")[1]);
                getPortletDetailsforDrill(ch,portletId,portalTabId.replace("=","").substr(0,3));
            }
               function showGraphColumns(portletId,portalTabId,folderId,measureId,measureName)
                {
                   var divId="#graphMeasuresDiv";
                   $(divId).dialog({
                       autoOpen: false,
                       height: 350,
                       width: 550,
                       position: 'justify',
                       modal: true
                        });
                       $(divId).dialog('open')
                       $("#graphMeasures").attr("src","pbPortletGraphMeasures.jsp?graphId=0&folderIds="+folderId+"&grpIds=0&measureId="+measureId+"&measureName="+encodeURI(measureName)+"&portletId="+portletId+"&portalTabId="+portalTabId)

                  }

                  function timePortlet(portletId,portalId,repId,dispgrptyp,perby,gptype,Date)
                   {
                       closeOpenDivs(portletId,portalId)
                       portletid=portletId
                       portaltabId=portalId
                       repid=repId
                       dispgrptypObj=dispgrptyp
                       perBy=perby
                       gpType=gptype
                       date=Date
                    var divId="#time";
                        $(divId).dialog({
                       autoOpen: false,
                       height: 150,
                       width: 250,
                       position: 'justify',
                       modal: true
                        });
                        $.ajax({
                         url: 'portalViewer.do?portalBy=editTimePeriodDetails&PortletId='+portletid+'&portalTabId='+portaltabId,
                         success: function(data){
                                if(data!='null'){
                                    var jsonVar=eval('('+data+')')
                                    var date=jsonVar.date;
                                    var periodType=jsonVar.periodType;
                                    var comparePeriod=jsonVar.comparePeriod;
                                   // alert(date+"...."+periodType+"...."+comparePeriod)
                                     $("#timeOption").val(date);
                                     $('#periodType').val(periodType);
                                     $("#CBO_PRG_COMPARE").val(comparePeriod);
                                    }
                                }
                            });
                             //alert("getTime")
                       $(divId).dialog('open')
                       $("#timeOption").datepicker({
                            changeMonth: true,
                            changeYear: true,
                            showButtonPanel: true,
                            numberOfMonths: 1,
                            stepMonths: 1

                        });
                    }

    function getTimePeriodDetails()
    {
        //alert("getTime")
        var time=$("#timeOption").val();
        var period=$('#periodType').val();
        var comparePeriod=$("#CBO_PRG_COMPARE").val();        
        var rowEdgeParams="";
        var colEdgeParams="";
        var rowParamIdObj=repid;
        var columnParmObject=''
        var CEPNames=''
        var REPNames=''
        if(rowParamIdObj!=null){

         for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+portletid+"-"+portaltabId)

            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }

        }
        }

 if(document.getElementById('portlet-'+portaltabId+'-'+portletid)!=null){
    $('#portlet-'+portaltabId+'-'+portletid).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>');
       $("#time").dialog("close")
  $.ajax({
        url: 'portalViewer.do?portalBy=getTimePeriodDetails&PortletId='+portletid+'&portalTabId='+portaltabId+'&time='+time+'&period='+period+'&comparePeriod='+comparePeriod,
        success: function(data){

         getPortletDetails(portletid, rowEdgeParams, colEdgeParams,perBy,gpType,portaltabId,date,"getTimePeriodDetails");

    }
    });
    }

       
    }
    function resetTimePeriodDetails(){
    var rowEdgeParams="";
        var colEdgeParams="";
        var rowParamIdObj=repid;
        var columnParmObject=''
        var CEPNames=''
        var REPNames=''
        if(rowParamIdObj!=null){

         for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+portletid+"-"+portaltabId)

            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }

        }
        }

    if(document.getElementById('portlet-'+portaltabId+'-'+portletid)!=null){
    $('#portlet-'+portaltabId+'-'+portletid).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>');
     $("#time").dialog("close")
    $.ajax({
        url: 'portalViewer.do?portalBy=resetTimePeriodDetails&PortletId='+portletid+'&portalTabId='+portaltabId+'',
        success: function(data){

         getPortletDetails(portletid, rowEdgeParams, colEdgeParams,perBy,gpType,portaltabId,date,"resetTimePeriodDetails");

    }
    });
    }

    }

            function closeOpenDivs(portletId,portalTabId){
             var divName= "saveXmalOfPortlet"+portletId+"-" +portalTabId
            if(document.getElementById(divName)!=null && document.getElementById(divName).style.display!='none'){
                document.getElementById(divName).style.display='none'
            }
        }
        </script>
    </body>
</html>




