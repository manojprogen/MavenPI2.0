<%--
    Document   : metadataScheduler.jsp
    Created on : 12 Jan, 2011, 5:03:59 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.PbDb,com.progen.reportdesigner.db.ReportTemplateDAO,utils.db.ProgenConnection"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);
             ReportTemplateDAO repDAO = new ReportTemplateDAO();
             PbReturnObject retObj=repDAO.getSchedulerDetails();

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
      <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>

      <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
<!--       <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>-->
          <script type="text/javascript">
        $(document).ready(function(){
                $("#tableScheduler").tablesorter({headers : {0:{sorter:false}}})
                $("#tableScheduler").tablesorterPager({container: $('#pagerScheduler')})
        });

         function selectAllReps()
            {
                var RepSelectObj=document.getElementsByName("schedulerSelect");
                for(var i=0;i<RepSelectObj.length;i++){
                    if(RepSelectObj[0].checked){
                        RepSelectObj[i].checked=true;
                    }
                    else{
                        RepSelectObj[i].checked=false;
                    }
                }
            }
        </script>


    </head>
    <body id="mainBody">
    <form name="schedulerForm" id="schedulerForm"  method="post" action="" >

            <table  border="0px solid" width="100%">
                <tr valign="top">
                    <td align="left" width="25%">
                        <div id="pagerScheduler" class="pager" align="left" >
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                            <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt="" src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option value="<%=retObj.getRowCount()%>">All</option>
                            </select>
                        </div>
                    </td>
                    <td align="right" width="38%">
                        <input type="button" value=" Schedule New Scorecard" class="navtitle-hover" style="width:auto"  onclick="viewScorecardScheduler()">
                        <input type="button" value="New Tracker" class="navtitle-hover" style="width:auto"  onclick="javascript:viewScheduler('Tracker','','','false')">
                        <input type="button" value="New Scheduler" class="navtitle-hover" style="width:auto"  onclick="javascript:viewScheduler('Scheduler','','','false')">
                        <input type="button" value="Edit" class="navtitle-hover" style="width:auto"  onclick="javascript:editScheduler()">
                        <input type="button" value="Run/Execute" class="navtitle-hover" style="width:auto"  onclick="javascript:runScheduler('<%=request.getContextPath()%>')">
                        <input type="button" value="Delete " class="navtitle-hover" style="width:auto"  onclick="javascript:deleteScheduler('<%=request.getContextPath()%>')">
                    </td>
                </tr>

            </table>

            <table align="center" id="tableScheduler" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">
                <thead>
                    <tr>

                        <th nowrap><input type="checkbox" name="schedulerSelect" id="schedulerSelect1" onclick="return selectAllReps()">Select All</th>
                        <th nowrap>Report Name</th>
                        <th nowrap>Type</th>
                        <th nowrap>Status</th>
                        <th nowrap>Created On</th>
                        <th nowrap>Updated On</th>
                        <th nowrap>Business Role</th>
                        <th nowrap>Last Executed On</th>
                    </tr>
                </thead>
                <tbody>
                    <%int i = 0;
                                for (i = 0; i < retObj.getRowCount(); i++) {%>
                    <tr>
                        <td style="width:30px"><input type="checkbox" name="schedulerSelect" id="RepSelect<%=retObj.getFieldValueString(i,4)%>" value="<%=retObj.getFieldValueString(i, 4)%>~<%=retObj.getFieldValueString(i, 1)%>" onclick="unselAll()"></td>
                        <td class="wordStyle" id="hyperRep" style="font-size:12px;cursor:pointer" onclick="javascript:viewScheduler('<%=retObj.getFieldValueString(i, 1)%>','<%=retObj.getFieldValueString(i,4)%>','<%=retObj.getFieldValueString(i, 0)%>','readOnly')">
                            <%=retObj.getFieldValueString(i, 0)%>
                        </td>
                        <td>
                            <%=retObj.getFieldValueString(i, 1)%>
                        </td>
                        <td>
                            <%=retObj.getFieldValueString(i, 7)%>
                        </td>
                        <td>
                            <%=retObj.getFieldValueDateString(i, 2)%>
                        </td>
                        <td>
                            <%=retObj.getFieldValueDateString(i, 3)%>
                        </td>
                        <td>
                            <%=retObj.getFieldValueString(i, 5)%>
                        </td>
                        <td>
                            <%=retObj.getFieldValueDateString(i, 6)%>
                        </td>

                    </tr>
                    <%}%>
                </tbody>
            </table>
                   </form>
    </body>
</html>

