<%--
    Document   : pbReportDrillPopUp
    Created on : 12 Mar, 2013, 12:30:56 PM
    Author     : srikanth.p@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    String themeColor = "blue";
    if (session.getAttribute("theme") == null) {
         session.setAttribute("theme", themeColor);
       } else {
            themeColor = String.valueOf(session.getAttribute("theme"));
      }
    String useragent = request.getHeader("User-Agent");
    String browserType = "";
    String user = useragent.toLowerCase();
    if (user.indexOf("msie") != -1) {
        browserType = "IE";
    } else if (user.indexOf("netscape6") != -1) {
      browserType = "Net";
    } else if (user.indexOf("mozilla") != -1) {
       browserType = "Moz";
    }
    String reportId=request.getParameter("reportId");
    String contextPath=request.getContextPath();
    String reportUrl=request.getParameter("reportUrl");
    String width=request.getParameter("width");
    String height=request.getParameter("height");
    int level=request.getParameter("level") != null?Integer.parseInt(request.getParameter("level")):0;

    String isreportdrillPopUp=request.getParameter("isreportdrillPopUp");
    



%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi</title>
        <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/JS/pbTableMapJSForPopUp.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/dashboardDesign.js"></script>-->
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>

        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js" ></script>

         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
         <link href="<%=request.getContextPath()%>/css/styles.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />


        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />-->
<!--        <link href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">-->
        <link href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/style.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link href="<%=request.getContextPath()%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">
            var winWidth=<%=width%>;
            var winHight=<%=height%>;
            var level=<%=level%>;
            var ctxPath='<%=request.getContextPath()%>';
            $(document).ready(function(){
                var frameObjName='reportDrillPopUpIframe'+<%=level%>;
                setframeObj(frameObjName);
//                alert('parent.tableframeObj '+tableframeObj);
            });

             function submiturlsPopUp($ch){
               var url = $ch;
               var drillUrl=url.replace("reportViewer.do?reportBy=viewReport", "reportViewer.do?reportBy=viewReportPopUp");
               $.ajax({
                    url: drillUrl,
                    success: function(data){
                        var frameObj=document.getElementById("reportDrillPopUpIframe"+level);
//                        edited by manik
//                      var source="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportId%>&isreportdrillPopUp=<%=isreportdrillPopUp%>";
                        var source="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportId%>&isreportdrillPopUp=<%=isreportdrillPopUp%>&widpopup=<%=width%>&heipopup=<%=height%>";
                        frameObj.src=source;
                    }
                });
            }

        </script>

    </head>
    <body>
        <div id="reportDrillPopUpDiv<%=level%>" style="width:<%=width%>;height: <%=height%>">
            <iframe id="reportDrillPopUpIframe<%=level%>" width="<%=width%>" height="<%=height%>" src="<%=request.getContextPath()%>/TableDisplay/pbDisplay.jsp?tabId=<%=reportId%>&isreportdrillPopUp=<%=isreportdrillPopUp%>&widpopup=<%=width%>&heipopup=<%=height%>"></iframe>
        </div>
        <div id="reportDrillPopUpDiv<%=(level+1)%>" style="display: none">

        </div>
    </body>
</html>