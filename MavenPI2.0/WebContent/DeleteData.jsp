<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%--
    Document   : DeleteData
    Created on : 2 Nov, 2012, 12:28:37 PM
    Author     : arun
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
 //added by Dinanath
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");

            String themeColor = "";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String DefaultArrregations[] = {"--", "sum", "avg", "min", "max", "count", "COUNTDISTINCT"};
            String userId = "";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String contextPath=request.getContextPath();

%>

<html>
    <head>
         <%--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        <script type="text/javascript" src="javascript/treeview/demo.js"></script>

        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/queryDesign.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>

        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
--%>
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
           
<style type="text/css">
            * {
                font-family: verdana;
                font-size: 11px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
        </style>
       
    </head>
    <body>
         <script type="text/javascript">
            $(document).ready(function(){

                if ($.browser.msie == true){

                    $("#tableCol").dialog({
                        autoOpen: false,
                        height: 560,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });

                }
                else{

                    $("#tableCol").dialog({
                        autoOpen: false,
                        height: 560,
                        width: 700,
                        position: 'justify',
                        modal: true
                    });

                }
            });
        </script>
         <script type="text/javascript">
            //var assinIdAndVales=new Array
            //var isMemberUseInOtherLevel="false"
            $(document).ready(function()
            {

                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                        $("#connections").html(data)
                    });
                });

                function getGroupDetails(){
                    var connectionID= $("#connections").val()

                    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getGroupDetails&connectionID="+connectionID,function(data){
                        $("#groupSelect").html(data)
                    });
                }

            function getTableDetails(){
                   var connectionID= $("#connections").val()
                   var groupID= $("#groupSelect").val()
                   $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getTableDetails1&groupID="+groupID,function(data){
                       $("#TablesSelect").html(data)
                  });
                }
function getAllColumns(){
    var connectionID= $("#connections").val()
    var tablesSelected= $("#TablesSelect").val()
    var groupID= $("#groupSelect").val()
    $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllColumns&tablesSelected="+tablesSelected+"&connectionID="+connectionID+"&grpId="+groupID,function(data){
         $("#colTable").html(data)
    });


}
function deleteRow(rowid)
{
    document.getElementById(rowid).style.display='none';
     var tablesSelected= $("#TablesSelect").val()
    var connectionID= $("#connections").val()
    var tabcol=$("#tabCol").val();
    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=deleteRow1&tableCol="+tabcol+"&tablesSelected="+tablesSelected+"&rowID="+rowid+"&connectionID="+connectionID,function(data){

    });
}
        </script>
        <form id="measureModifyForm" name="measureModifyForm" method="post" action="">
            <table align="left"><tr valign="top"><td>
                        <%=TranslaterHelper.getTranslatedInLocale("connection", cle)%>:<select name="connections" id="connections" onchange="getGroupDetails()">
                        </select>
                    </td>
                     <td> &nbsp;&nbsp;&nbsp;<%=TranslaterHelper.getTranslatedInLocale("Groups", cle)%>:&nbsp;&nbsp;&nbsp;<select name="groupSelect" id="groupSelect" onchange="getTableDetails()"></select> </td>
                    <td> &nbsp;&nbsp;&nbsp; <%=TranslaterHelper.getTranslatedInLocale("tables", cle)%>: &nbsp;&nbsp;&nbsp;<select name="TablesSelect" id="TablesSelect" ></select> </td>
                    <td><input type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("go", cle)%>" onclick="getAllColumns()"></td></tr></table><br/><br/>
                    <table id="colTable" border="1">
                    </table>
        </form>

                        </body>
</html>
