<%@page import="java.util.*" %>
<%@page import="prg.db.Container"%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String measureIds = null;
            String measureNames = null;
            String[] measureIdsArray = null;
            String[] measureNamesArray = null;
            String measureIdsStr = null;
            String measureNamesStr = null;
            String whatIfScenarioId = null;
            HashMap map = new HashMap();
            Container container = null;
            HashMap TableHashMap = null;
            ArrayList REP = null;
            HashMap REPHashMapDetails = null;
            HashMap AllHashMap = null;
            String[] AllHashMapKeys = null;
            ArrayList AllREPValues = null;
            String AllREPValuesStr = null;
            HashMap AllREPValuesHashMap = null;

            if (request.getParameter("measureIds") != null) {
                measureIds = request.getParameter("measureIds");
            }
            if (request.getParameter("measureNames") != null) {
                measureNames = request.getParameter("measureNames");
            }
            if (request.getParameter("whatIfScenarioId") != null) {
                whatIfScenarioId = request.getParameter("whatIfScenarioId");
            }

            //("measureIds in sp is:: " + measureIds);
            //("measureNames in sp is:: " + measureNames);
            //("whatIfScenarioId in sp is:: " + whatIfScenarioId);

            measureIdsArray = measureIds.split("Â©");
            measureNamesArray = measureNames.split("Â©");

            for (int i = 0; i < measureIdsArray.length; i++) {
                if (measureIdsStr == null) {
                    measureIdsStr = measureIdsArray[i];
                    measureNamesStr = measureNamesArray[i];
                } else {
                    measureIdsStr = measureIdsStr + "©" + measureIdsArray[i];
                    measureNamesStr = measureNamesStr + "©" + measureNamesArray[i];
                }
            }

            if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                }

                if (map.get(whatIfScenarioId) != null) {
                    container = (prg.db.Container) map.get(whatIfScenarioId);
                } else {
                    container = new prg.db.Container();
                }

                if (container.getTableHashMap() != null) {
                    TableHashMap = container.getTableHashMap();
                }

                AllREPValuesHashMap = (HashMap) TableHashMap.get("AllREPValues");
                REP = (ArrayList)TableHashMap.get("REP");
                if(AllREPValuesHashMap.containsKey(REP.get(0))) {
                    AllREPValues = (ArrayList)AllREPValuesHashMap.get(REP.get(0));
                }
                //("AllREPValues in sp is:: "+AllREPValues);
                for (int i = 0; i < AllREPValues.size(); i++) {
                    if (AllREPValuesStr == null) {
                        AllREPValuesStr = String.valueOf(AllREPValues.get(i));
                    } else {
                        AllREPValuesStr = AllREPValuesStr + "©" + String.valueOf(AllREPValues.get(i));
                    }
                }
                //("AllREPValuesStr in sp is:: "+AllREPValuesStr);
            }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>piEE</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script type="text/javascript">
            var paramIdsArray;
            var paramNamesArray;
            var paramNames = "";
            var measureIds = "";
            var measureNames = "";
            $(function() {
                paramIdsArray = new Array();
                paramNamesArray = new Array();
                document.getElementById("availableParamValuesSortable").innerHTML="";
                document.getElementById("existingParamValuesSortable").innerHTML="";

                var iFrameObj = parent.document.getElementById("iframe1");
                var tbodyObj = iFrameObj.contentWindow.document.getElementById("getMainBody");
                var trObjs = tbodyObj.getElementsByTagName("tr");

                //alert("trObjs len is:: "+trObjs.length);
                var AllREPValuesStr = "<%=AllREPValuesStr%>";
                var AllREPValuesArray = AllREPValuesStr.split("©");
                for(var i=0;i<AllREPValuesArray.length;i++) {
                    paramIdsArray.push(AllREPValuesArray[i]);
                    paramNamesArray.push(AllREPValuesArray[i]);
                    createParamValueElements(AllREPValuesArray[i],AllREPValuesArray[i],"availableParamValuesSortable","true","mydragRowTabs");
                }
                
                /*
                for(var i=0;i<trObjs.length;i++) {
                    if(trObjs[i].getAttribute("id") == null) {
                        paramIdsArray.push(trObjs[i].getElementsByTagName("td")[0].getElementsByTagName("a")[0].innerHTML);
                        paramNamesArray.push(paramIdsArray[i]);
                        createParamValueElements(trObjs[i].getElementsByTagName("td")[0].getElementsByTagName("a")[0].innerHTML,trObjs[i].getElementsByTagName("td")[0].getElementsByTagName("a")[0].innerHTML,"availableParamValuesSortable","true","mydragRowTabs");
                    }
                }
                 */

                for(var cnt=0;cnt<paramIdsArray.length;cnt++){
                    createParamValueElements(paramIdsArray[cnt], paramNamesArray[cnt], "existingParamValuesSortable","false","mydragRowTabs");
                }
                dropSelectiveParamValues();
            });

            function createParamValueElements(paramId,paramName,sortableDivName,flag,tdclsname){
                //alert("in create")
                var parentUL=document.getElementById(sortableDivName);
                var childLI=document.createElement("li");
                childLI.id=paramId;
                childLI.style.width='180px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                var table=document.createElement("table");
                var row=table.insertRow(0);
                if(flag=='true'){
                    var cell2=row.insertCell(0);
                    cell2.style.width='180px';
                    cell2.style.height='auto';
                    cell2.style.color='black';
                    cell2.id=paramId;
                    cell2.className=tdclsname;
                    cell2.innerHTML=paramName;
                    childLI.appendChild(table);
                }else{
                    var cell1=row.insertCell(0);
                    var a=document.createElement("a");
                    a.href="javascript:deleteParamValueElements('"+paramName+"','"+paramId+"','"+sortableDivName+"','"+tdclsname+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    cell2=row.insertCell(1);
                    cell2.style.color='black';
                    cell2.innerHTML=paramName;
                    childLI.appendChild(table);
                }
                //alert("childLI is:: "+childLI)
                parentUL.appendChild(childLI);
            }

            function deleteParamValueElements(paramName,paramId,sortableDivName,deletefrom){
                var parentUL = document.getElementById(sortableDivName);
                var len = parentUL.childNodes.length;
                //alert("len is:: "+len)
                var i=0;
                for(i = 0; i < len; i++){
                    if(parentUL.childNodes[i].id == paramId){
                        parentUL.removeChild(parentUL.childNodes[i]);
                        break;
                    }                    
                }
                if(deletefrom=="mydragRowTabs"){
                    for(i = 0; i < paramIdsArray.length; i++){
                        if(paramIdsArray[i] == paramId){
                            paramIdsArray.splice(i,1);
                            paramNamesArray.splice(i,1);
                        }
                    }
                }
            }

            function dropSelectiveParamValues()
            {
                $(".mydragRowTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                $("#dropingSelectiveParamValues").droppable({
                    accept:'.mydragRowTabs',
                    drop: function(ev, ui) {
                        var flag=false;
                        for(var i=0;i<paramIdsArray.length;i++){
                            //alert("1 : "+ui.draggable.attr('id'))
                            //alert("2 : "+measureIdsArray[i])
                            if( paramIdsArray[i]==ui.draggable.attr('id')){
                                flag=true;
                            }
                        }
                        if(!flag){
                            paramIdsArray.push(ui.draggable.attr('id'));
                            paramNamesArray.push(ui.draggable.html());
                            //alert(ui.draggable.html());
                            createParamValueElements(ui.draggable.attr('id'), ui.draggable.html(), "existingParamValuesSortable", "false","mydragRowTabs");
                        }
                    }
                });
                $("#existingParamValuesSortable").sortable();
            }

            function processSelectiveParamValues() {
                for(var i=0;i<paramNamesArray.length;i++) {
                    if(paramNames == "") {
                        paramNames = paramNamesArray[i];
                    }else {
                        paramNames = paramNames+"©"+paramNamesArray[i];
                    }
                }
            <%--alert('pbWhatIfNonAllName.jsp?measureIds='+'<%=measureIdsStr%>'+'&measureNames='+'<%=measureNamesStr%>'+'&paramNames='+paramNames)--%>
                document.forms.myForm.action = 'pbWhatIfNonAllName.jsp?measureIds='+'<%=measureIdsStr%>'+'&measureNames='+'<%=measureNamesStr%>'+'&paramNames='+paramNames;
                document.forms.myForm.method="POST";
                document.forms.myForm.submit();
            }
        </script>
    </head>
    <body>
        <form name="myForm" id="myForm">
            <div id="selectiveParamValuesDisplay" title="Selective Parameter Values">
                <table style="width:100%;height:auto" border="1">
                    <tr>
                        <td width="50%" valign="top">
                            <div style="height:20px;background-color:#b4d9ee" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Available Parameter Values</font></div>
                            <div style="height:230px;overflow:auto">
                                <ul id="availableParamValuesSortable" class="availableParamValues">

                                </ul>
                            </div>
                        </td>
                        <td width="50%" valign="top">
                            <div style="height:20px;background-color:#b4d9ee" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Existing Parameter Values</font></div>
                            <div id="dropingSelectiveParamValues" style="height:230px;overflow:auto">
                                <ul id="existingParamValuesSortable"  class="existingParamValues">

                                </ul>
                            </div>
                        </td>
                    </tr>
                </table>
                <center><input type="button" name="Next" class="navtitle-hover" value="Next" onclick="processSelectiveParamValues()"></center>
            </div>
        </form>
    </body>
</html>
