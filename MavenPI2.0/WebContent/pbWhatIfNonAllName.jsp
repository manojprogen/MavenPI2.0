

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String ctxPath = request.getContextPath();
            String measureIds = null;
            String measureNames = null;
            String paramNames = null;
            String[] measureIdsArray = null;
            String[] measureNamesArray = null;
            String[] paramNamesArray = null;
            String measureIdsStr = null;
            String measureNamesStr = null;
            String paramNamesStr = null;

            if (request.getParameter("measureIds") != null) {
                measureIds = request.getParameter("measureIds");
            }
            if (request.getParameter("measureNames") != null) {
                measureNames = request.getParameter("measureNames");
            }
            if (request.getParameter("paramNames") != null) {
                paramNames = request.getParameter("paramNames");
            }

            ////.println("measureIds in np is:: "+measureIds);
            ////.println("measureNames in np is:: "+measureNames);
            ////.println("paramNames in np is:: "+paramNames);

            measureIdsArray = measureIds.split("Â©");
            measureNamesArray = measureNames.split("Â©");
            paramNamesArray = paramNames.split("Â©");

            for (int i = 0; i < measureIdsArray.length; i++) {
                if (measureIdsStr == null) {
                    measureIdsStr = measureIdsArray[i];
                    measureNamesStr = measureNamesArray[i];
                } else {
                    measureIdsStr = measureIdsStr + "©" + measureIdsArray[i];
                    measureNamesStr = measureNamesStr + "©" + measureNamesArray[i];
                }
            }

            for (int i = 0; i < paramNamesArray.length; i++) {
                if (paramNamesStr == null) {
                    paramNamesStr = paramNamesArray[i];
                } else {
                    paramNamesStr = paramNamesStr + "©" + paramNamesArray[i];
                }
            }
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>piEE</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">
            function saveWhatIfNonAllName() {
                var nonAllName = document.getElementById("whatIfName").value;
                var nonAllDesc = document.getElementById("whatIfDesc").value;
                parent.$("#selectiveWhatIfMeasures").dialog('close');
                
                parent.saveWhatIfSelectiveMeasures(nonAllName,nonAllDesc,'<%=measureIdsStr%>','<%=measureNamesStr%>','<%=paramNamesStr%>');
            }
            function tabmsg2(){
                document.getElementById('whatIfDesc').value = document.getElementById('whatIfName').value;
            }
        </script>
    </head>
    <body onload='document.getElementById("whatIfName").focus()'>
        <form name="myForm" id="myForm">
            <div id="whatIfNonAllName" title="Enter Name">
                <center>
                    <br>
                    <table style="width:80%" border="0">
                        <tr>
                            <td valign="top" nowrap style="width:30%">Enter Name</td>
                            <td valign="top" style="width:80%">
                                <input type="text" maxlength="35" name="whatIfName" style="width:80%" id="whatIfName" onkeyup="tabmsg2()" onfocus="document.getElementById('whatIfNonAllSave').disabled = false;"><br><span id="whatIfDuplicate" style="color:red"></span>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" style="width:30%">Description</td>
                            <td valign="top" style="width:70%">
                                <textarea cols="" rows="4" name="whatIfDesc" id="whatIfDesc" style="width:80%"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Done" id="whatIfNonAllSave" onclick="saveWhatIfNonAllName()"></td>
                        </tr>
                    </table>
                </center>
            </div>
        </form>
    </body>
</html>
