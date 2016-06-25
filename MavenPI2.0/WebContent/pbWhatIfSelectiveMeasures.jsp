

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String measureIds = null;
            String measureNames = null;
            String whatIfScenarioId = null;

            if (request.getParameter("measureIds") != null) {
                measureIds = request.getParameter("measureIds");
            }
            if (request.getParameter("measureNames") != null) {
                measureNames = request.getParameter("measureNames");
            }

            if (request.getParameter("whatIfScenarioId") != null) {
                whatIfScenarioId = request.getParameter("whatIfScenarioId");
            }

            ////.println("measureIds in sm is:: "+measureIds);
            ////.println("measureNames in sm is:: "+measureNames);
            ////.println("whatIfScenarioId in sm is:: "+whatIfScenarioId);

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
            var measureIdsArray;
            var measureNamesArray;
            var measureIds = "";
            var measureNames = "";

            $(function() {
                measureIdsArray = new Array();
                measureNamesArray = new Array();
                document.getElementById("availableMeasuresSortable").innerHTML="";
                document.getElementById("existingMeasuresSortable").innerHTML="";
                //alert("measureIds in js is:: "+'<%=measureIds%>'+" and measureNames in js is:: "+'<%=measureNames%>')
                var measureIdsStr = '<%=measureIds%>'.split("Â©");
                var measureNamesStr = '<%=measureNames%>'.split("Â©");

                for(var i=0;i<measureIdsStr.length;i++) {
                    measureIdsArray.push(measureIdsStr[i]);
                    measureNamesArray.push(measureNamesStr[i]);
                    createMeasureElements(measureIdsStr[i],measureNamesStr[i],"availableMeasuresSortable","true","mydragRowTabs");
                }
                //alert("measureIdsArray.length is:: "+measureIdsArray.length)
                for(var cnt=0;cnt<measureIdsArray.length;cnt++){
                    createMeasureElements(measureIdsArray[cnt], measureNamesArray[cnt], "existingMeasuresSortable","false","mydragRowTabs");
                }
                dropSelectiveMeasures();                
            });

            function createMeasureElements(paramId,paramName,sortableDivName,flag,tdclsname){
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
                    a.href="javascript:deleteMeasureElements('"+paramName+"','"+paramId+"','"+sortableDivName+"','"+tdclsname+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    cell2=row.insertCell(1);
                    cell2.style.color='black';
                    cell2.innerHTML=paramName;
                    childLI.appendChild(table);
                }               
                parentUL.appendChild(childLI);
            }

            function dropSelectiveMeasures()
            {
                $(".mydragRowTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });
                $("#dropingSelectiveMeasures").droppable({
                    accept:'.mydragRowTabs',
                    drop: function(ev, ui) {
                        var flag=false;
                        for(var i=0;i<measureIdsArray.length;i++){
                            //alert("ui.draggable.attr('id') is: "+ui.draggable.attr('id'))
                            //alert("measureIdsArray[i] is:: "+measureIdsArray[i])
                            if( measureIdsArray[i]==ui.draggable.attr('id')){
                                flag=true;
                            }
                        }
                        if(!flag){
                            measureIdsArray.push(ui.draggable.attr('id'));
                            measureNamesArray.push(ui.draggable.html());                            
                            createMeasureElements(ui.draggable.attr('id'), ui.draggable.html(), "existingMeasuresSortable", "false","mydragRowTabs");
                        }
                    }
                });
                $("#existingMeasuresSortable").sortable();
            }

            function deleteMeasureElements(paramName,paramId,sortableDivName,deletefrom){
                var parentUL = document.getElementById(sortableDivName);
                var len = parentUL.childNodes.length;
                var i=0;
                //alert("len is:: "+len);
                for(i = 0; i < len; i++){
                    //alert("paramId is:: "+paramId)
                    //alert("parentUL.childNodes[i].id is:: "+parentUL.childNodes[i].id)
                    if(parentUL.childNodes[i].id == paramId){
                        parentUL.removeChild(parentUL.childNodes[i]);
                        break;
                    }                    
                }
                if(deletefrom=="mydragRowTabs"){
                    for(i = 0; i < measureIdsArray.length; i++){
                        if(measureIdsArray[i] == paramId){
                            measureIdsArray.splice(i,1);
                            measureNamesArray.splice(i,1);
                        }
                    }
                }
            }
            
            
            function processSelectiveMeasures() {
                //alert("measureIdsArray.length is:: "+measureIdsArray.length)
                for(var i=0;i<measureIdsArray.length;i++) {
                    if(measureIds == "") {
                        measureIds = measureIdsArray[i];
                        measureNames = measureNamesArray[i];
                    }else {
                        measureIds = measureIds+"©"+measureIdsArray[i];
                        measureNames = measureNames+"©"+measureNamesArray[i];
                    }
                    //alert("measureIds is:: "+measureIds)
                }
                <%--alert("pbWhatIfSelectiveParams.jsp?measureIds="+measureIds+"&measureNames="+measureNames);--%>
                document.forms.myForm.action = "pbWhatIfSelectiveParams.jsp?measureIds="+measureIds+"&measureNames="+measureNames+"&whatIfScenarioId=<%=whatIfScenarioId%>";
                document.forms.myForm.method="POST";
                document.forms.myForm.submit();
            }
        </script>
    </head>
    <body>
        <form name="myForm" id="myForm">
            <div id="selectiveMeasuresDisplay" title="Selective Measures">
                <table style="width:100%;height:auto" border="1">
                    <tr>
                        <td width="50%" valign="top">
                            <div style="height:20px;background-color:#b4d9ee" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Available Measures</font></div>
                            <div style="height:230px;overflow:auto">
                                <ul id="availableMeasuresSortable" class="availableMeasures">

                                </ul>
                            </div>
                        </td>
                        <td width="50%" valign="top">
                            <div style="height:20px;background-color:#b4d9ee" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Existing Measures</font></div>
                            <div id="dropingSelectiveMeasures" style="height:230px;overflow:auto">
                                <ul id="existingMeasuresSortable"  class="existingMeasures">

                                </ul>
                            </div>
                        </td>
                    </tr>
                </table>
                <center><input type="button" name="Next" class="navtitle-hover" value="Next" onclick="processSelectiveMeasures()"></center>
            </div>
        </form>
    </body>
</html>
