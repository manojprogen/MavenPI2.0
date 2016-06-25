<%--
    Document   : compareReports
    Created on : May 3, 2011, 10:45:03 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.PbDb,com.progen.datasnapshots.DataSnapshot,java.sql.Clob,java.io.Reader,prg.db.Container,java.util.List,java.util.HashMap"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


        <%
                String themeColor = "blue";
                String snapShotName = "";
                Container firstContainer = null;
                Container secondContainer = null;
                HashMap map = new HashMap();
           //     DataSnapshot snapShot = (DataSnapshot) session.getAttribute("snapShot");
          //      snapShotName = snapShot.getSnapShotName();
                String frstRepId=String.valueOf(request.getAttribute("frstRepId"));
 //               
                String secndRepId=String.valueOf(request.getAttribute("secndRepId"));
//                
                String paramHtml=String.valueOf(request.getAttribute("paramHtml"));
                String firstColViewBy=String.valueOf(request.getAttribute("firstColViewBy"));
                String secondColViewBy=String.valueOf(request.getAttribute("secondColViewBy"));
                String reportTime=String.valueOf(request.getAttribute("reportTime"));
                String viewBy=String.valueOf(request.getAttribute("viewBy"));
                List<Container> containerList=(List<Container>) request.getAttribute("containerList");
                 String[] repIdList=(String[]) request.getAttribute("repIdList");

                  request.setAttribute("containerList", containerList);
        request.setAttribute("repIdList", repIdList);

                
 //               
 //               
//                String firstContainer=String.valueOf(request.getAttribute("firstContainer"));
//                String secondContainer=String.valueOf(request.getAttribute("secondContainer"));


  //              map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
   //             firstContainer = (prg.db.Container) map.get(frstRepId);
   //            secondContainer = (prg.db.Container) map.get(secndRepId);

 //               firstContainer=Container.getContainerFromSession(request, firstRepId);
 //               secondContainer=Container.getContainerFromSession(request, secondRepId);
String contxPath=request.getContextPath();
        %>
<html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>piEE</title>
        <script src="<%=contxPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contxPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contxPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contxPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contxPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <link rel="stylesheet" href="<%=contxPath%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css">
<!--              <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">-->
       <link type="text/css" href="<%=contxPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet" />
      

    </head>
    <body>
        <table style="width:100%">
            <tr>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <form name="compareReportsForm" id="compareReportsForm" method="post" action="">
            <div class="navtitle1" style=" max-width: 100%; cursor: auto; height: 20px;">
                <span> <font size="2">Compare Reports </font><b> </b></span>
            </div>
            <table width="100%"><tr><td>
            <table align="left">
                <tr>
                    <td><select id="paramNames"><%=paramHtml%></select></td>
                    <td><input type="button" value="Go" onclick="javascript:comparisonForChangedViewBy()" class="navtitle-hover" style="width:50px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"/></td>
                    <td><input type="button" value="Show Time Details" onclick="showReportTime()" class="navtitle-hover" style="width:150px"/></td>

                </tr>
            </table></td><td>
            <table align="right"><tr>
                    <td>
                                    <input type="button" value="Save" onclick="saveComparedReports()" class="navtitle-hover" style="display:none;width:50px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"/>

                    </td>
                    <td>
            <input type="button" value="Back" onclick="javascript:gotoReportHome()" class="navtitle-hover" style="width:50px"  style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"/>
                    </td></tr></table></td>
            </tr>
            </table><br>

                <%
                        String html=String.valueOf(request.getAttribute("html"));
                              /*  Clob htmlTbl = snapShot.getHtmlView();

                                Reader clobReader = htmlTbl.getCharacterStream();
                                char[] cbuf;
                                int toRead = 0;
                                do {
                                    cbuf = new char[5196];
                                    toRead = clobReader.read(cbuf, 0, 5196);
                                    if (toRead == -1) {
                                        break;
                                    }
                                    out.print(cbuf);
                                } while (true);
                                clobReader.close();
                                session.removeAttribute("snapShot");   */
                    %>
                    <div id="compDiv" style="width: 100%">
                   <%=html%>
                    </div>
                    <div id="reportTimeDetailsDiv" style="display: none">
                        <%=reportTime%>
                        <br>
                        <table align="center">
                            <tr>
                                <td>
                                    <input type="button" value="Ok" onclick="closeTimeDiv()" class="navtitle-hover" style="width:50px"/>
                                </td>
                            </tr>
                        </table>
                    </div>
        </form>
                          <script type="text/javascript">
             $(document).ready(function(){
                 $("#reportTimeDetailsDiv").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
             });
             $("select#paramNames").val('<%=viewBy%>');
//             $("#compDiv").html('')
             });
//            function goPaths(path){
//                parent.closeStart();
//                document.forms.snapShotForm.action=path;
//                document.forms.snapShotForm.submit();
//            }
//            function viewReportG(path){
//                document.forms.snapShotForm.action=path;
//                document.forms.snapShotForm.submit();
//            }
//            function viewDashboardG(path){
//                document.forms.snapShotForm.action=path;
//                document.forms.snapShotForm.submit();
//            }
            function gotoReportHome(){
                 document.forms.compareReportsForm.action = "<%=request.getContextPath()%>/home.jsp#Report_Studio";
                document.forms.compareReportsForm.submit();
            }

//            function comparisonForChangedViewBy(){
//                var viewBy = $("#paramNames").val();
//                document.forms.snapShotForm.action = "reportTemplateAction.do?templateParam=changeViewByCompare&firstRepId="+<%=frstRepId%>+"&secondRepId="+<%=secndRepId%>+"&viewBy="+viewBy+"&secondColViewBy="+<%=secondColViewBy%>+"&firstColViewBy="+<%=firstColViewBy%>;
//                document.forms.snapShotForm.submit();
//            }
            function comparisonForChangedViewBy()
            {
                 var viewBy = $("#paramNames").val();
//                    $.post('<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=changeViewByCompare1&viewBy='+viewBy,$("#compareReportsForm").s,
//                        function(data){
//
//
//                      });
//
                       document.forms.compareReportsForm.action = "reportTemplateAction.do?templateParam=changeViewByCompare1&viewBy="+viewBy;
                document.forms.compareReportsForm.submit();

            }

            function showReportTime()
            {
                        $("#reportTimeDetailsDiv").dialog('open');
            }
            function closeTimeDiv()
            {
                 $("#reportTimeDetailsDiv").dialog('close');
            }
            function saveComparedReports()
            {
                $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=saveComparedReports&viewBy=<%=viewBy%>', $("#compareReportsForm").serialize() ,
         function(data){

      });
            }
        </script>
    </body>
</html>
