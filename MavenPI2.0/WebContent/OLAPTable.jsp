<%-- 
    Document   : OLAPTable
    Created on : 28 Oct, 2012, 6:35:29 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
 String reportId = request.getParameter("tabId");
 String height = request.getParameter("height");
  String themeColor = "blue";
  String contextPath=request.getContextPath();
%>

<html>
    <head>
        <script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbTableMapJS.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>

<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/dashboardDesign.js"></script>-->
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script  type="text/javascript" src="<%=contextPath%>/javascript/jquery.contextMenu.js" ></script>

         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
         <link href="<%=contextPath%>/css/styles.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />


        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />-->
<!--        <link href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">-->
        <link href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link href="<%=contextPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style type="text/css">
           
            .custom-button-color{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                 background:#00FFFF url(images/ui-bg_glass_75_79c9ec_1x400.png) 50% 50% repeat-x;
                border:1px solid #448DAE;
                color:#000;
                FONT-SIZE: 11px;
                FONT-FAMILY: Verdana;
                VERTICAL-ALIGN: middle;
                HEIGHT: 20px;
                cursor:pointer;
            }
            tr.spaceUnder > td{
                padding-bottom: 1em;

            }
            #selectable .ui-selecting { background: #FECA40; }
/*            #selectable .ui-selected { background: #F39814; color: white; }*/
/*            #selectable { list-style-type: none; margin: 0; padding: 0; width: 60%; }*/

            #selectable .ui-selected {
                    background:#357EC7 url(images/ui-bg_glass_75_79c9ec_1x400.png) repeat-x scroll 50% 50%;
                    color:white;
                    }


               #selectable {
                    background:none repeat scroll 0 0 white;
                    list-style:none outside none;
                    width:60%;
                    }

                #selectable li {
                    font-family:verdana;
                    font-size:11px;
                    height:18px;
                    margin-bottom:1px;
                    margin-left:1px;
                    margin-right:1px;
                    margin-top:1px;
                    padding-bottom:0.4em;
                    padding-left:0.4em;
                    padding-right:0.4em;
                    padding-top:0.4em;
                    width:120px;
                    background:#CCCCCC;
                    border:1px solid #CCCCCC;
                    cursor:pointer;
                    color:black;
                    }


/*            #selectable li { margin: 3px; padding: 0.4em; font-size: 1.4em; height: 18px; }*/

/*            .ui-buttonset { margin-right: 7px; }
            .ui-buttonset .ui-button { margin-left: 0; margin-right: -.3em; }*/

        </style>
            <script type="text/javascript">
                 $(document).ready(function(){
                     var reportid='<%=reportId%>';
                     var height='<%=height%>';
                     var divIdObj="";
                     divIdObj = document.getElementById("prgTableFrm");
                     divIdObj.innerHTML ='<iframe name=\"iframe1\" id=\"iframe1\" width=\"100%\" height=\"'+height+'px\" frameborder=\"0\" style=\"overflow:auto;\" src=\"TableDisplay/pbDisplay.jsp?tabId='+reportid+'&fromOnview=true\"></iframe>';
                 });
            </script>
    </head>
    <body>
        <form action="" id="prgTableFrm" name="prgTableFrm"></form>
    </body>
</html>
