<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,java.util.Map,com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.report.whatIf.SensitivityFactor"%>
<%@page import="com.progen.report.whatIf.WhatIfSensitivity,com.progen.report.whatIf.WhatIfScenario,com.progen.report.whatIf.WhatIfScenarioAction,java.util.ArrayList,java.util.HashMap,prg.db.Container"%>
<%--
    Document   : pbWhatIfscenario
    Created on : 13 Aug, 2010, 2:25:46 PM
    Author     : sreekanth
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String PbReportId = request.getParameter("PbReportId");
            WhatIfScenarioAction whatIfScenerioAction = new WhatIfScenarioAction();
            ArrayList resultmesList = whatIfScenerioAction.selectMeasuresForWhatIf(request, PbReportId);
            ArrayList measuresList = null;
            ArrayList measuresNameList = null;
            ArrayList<String> selectedMeasureList = null;
            HashMap measureNamesAndElmIdHm = new HashMap();
            HashMap<String,String> stdNonStdDetails=new HashMap<String, String>();
            Container container = null;
            ArrayList<String> originalCols = null;
            String themeColor = "blue";
            ArrayList<String> whatIfTrgtLst = new ArrayList<String>();
            WhatIfSensitivity whatIfSensitivity =null;
            ReportTemplateDAO reportTemplateDAO=new ReportTemplateDAO();
            if (request.getSession(false) != null) {
                HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                if (map != null) {
                    container = (Container) map.get(PbReportId);
                }
            }



            if (resultmesList != null) {
                measuresList = (ArrayList) resultmesList.get(0);
                measuresNameList = (ArrayList) resultmesList.get(1);
                selectedMeasureList = (ArrayList) resultmesList.get(2);
                for (int i = 0; i < measuresList.size(); i++) {
                    measureNamesAndElmIdHm.put(measuresList.get(i), measuresNameList.get(i));
                }
              
                if(selectedMeasureList!=null){
                for(String whatifMes:selectedMeasureList){
                if(measureNamesAndElmIdHm.get(whatifMes)==null){
                       Map<String, String> elementDetails  =reportTemplateDAO.getElementsNames(whatifMes.replace("A_", ""));
                       measureNamesAndElmIdHm.put(whatifMes, elementDetails.get(whatifMes.replace("A_", "")));

                     }
                   
                }}
             

            }
            WhatIfScenario whatIfScenario = container.getWhatIfScenario();
            if (whatIfScenario != null) {
                whatIfTrgtLst = whatIfScenario.getWhatIfTargetMeasureList();
                if(whatIfScenario.getWhatIfSensitivity()!=null)
                    whatIfSensitivity=whatIfScenario.getWhatIfSensitivity();
                stdNonStdDetails=whatIfScenario.getStdnonstddetailsMap();
               
            }

            HashMap TableHashMap = null;
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

            originalCols = container.getOriginalColumns();
            TableHashMap = container.getTableHashMap();
            ArrayList<Double> sliderValues = null;
            if (container.getWhatIfScenario() != null) {
                sliderValues = container.getWhatIfScenario().getWhatIfMeasureSliderValues();


            }
            ArrayList<String> dependantMeasures = null;

            if (container != null && container.getWhatIfScenario() != null && container.getWhatIfScenario().getDependantMeasureList() != null) {
                dependantMeasures = container.getWhatIfScenario().getDependantMeasureList();
                for (String string : dependantMeasures) {
                    if (measuresList.contains(string)) {
                        measuresNameList.remove(measuresList.indexOf(string));
                        measuresList.remove(string);
                    }
                }

            }
            //added by Mohit Gupta for default locale
                   Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
                //ended By Mohit Gupta
            //
            //
            // 
  String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        
       

    </head>
    <body>
        <script type="text/javascript">
            var ctxPath ='<%=request.getContextPath()%>'
            var reportID='<%= PbReportId%>'
            var whatifOgject="";
                function dropDiv(dropId){
                    var dropId = dropId;
                }
               
                $(document).ready(function() {
                      
                    $(".MeasuresULClass").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $(".selectedMeasuresULClass").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $(".dependentMeasuresULClass").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });

                    $("#MeasuresDIV").droppable({

                        activeClass:"blueBorder",
                        accept:'.selectedMeasuresULClass,.dependentMeasuresULClass',
                        drop: function(ev, ui) {
                            //    alert("indddd11")
                            var dropDivID=this.id;
                            var dropDivobj =document.getElementById(dropDivID) ;
                            var dropUlobj= dropDivobj.getElementsByTagName("ul");
                            var dropUlID= dropUlobj[0].getAttribute("id");
                            var draggableLIid=ui.draggable.attr('id');
                            var liObjs = document.getElementById(draggableLIid);
                            var tableObjs = liObjs.getElementsByTagName("table");
                            var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                            var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                            var tdObjs =  trObjs[0].getElementsByTagName("td");
                            var content = tdObjs[0].innerHTML;
                            var draggableID =ui.draggable.attr('id')
                            //                              alert("indddd2--"+draggableID)
                            //                              alert("indddd2--"+document.getElementById(draggableID).parentNode.id)
                            createColumn(draggableLIid.replace(document.getElementById(draggableID).parentNode.id,"MeasuresUL"),ui.draggable.html(),dropUlID,content);
                            deleteLI(draggableID);
                        }
                    });


                    $("#selectedMeasuresDIV").droppable({

                        activeClass:"blueBorder",
                        accept:'.MeasuresULClass',

                        drop: function(ev, ui) {
                   
                            var dropDivID=this.id;
                            var dropDivobj =document.getElementById(dropDivID) ;
                            var dropUlobj= dropDivobj.getElementsByTagName("ul");
                            var dropUlID= dropUlobj[0].getAttribute("id");
                            var draggableLIid=ui.draggable.attr('id');
                          
                            var liObjs = document.getElementById(draggableLIid);
                            var tableObjs = liObjs.getElementsByTagName("table");
                            var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                            var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                            var tdObjs =  trObjs[0].getElementsByTagName("td");
                            var content = tdObjs[0].innerHTML;
                            var draggableID =ui.draggable.attr('id')
                            createColumn(draggableLIid.replace(document.getElementById(draggableID).parentNode.id, "selectedMeasuresUL"),ui.draggable.html(),dropUlID,content);
                            deleteLI(draggableID);
                        }
                    });
                    
                    $("#dependentMeasuresDIV").droppable({

                        activeClass:"blueBorder",
                        accept:'.MeasuresULClass',

                        drop: function(ev, ui) {

                            var dropDivID=this.id;
                            var dropDivobj =document.getElementById(dropDivID) ;
                            var dropUlobj= dropDivobj.getElementsByTagName("ul");
                            var dropUlID= dropUlobj[0].getAttribute("id");
                            var draggableLIid=ui.draggable.attr('id');

                            var liObjs = document.getElementById(draggableLIid);
                            var tableObjs = liObjs.getElementsByTagName("table");
                            var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                            var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                            var tdObjs =  trObjs[0].getElementsByTagName("td");
                            var content = tdObjs[0].innerHTML;
                            var draggableID =ui.draggable.attr('id')
                            createColumn(draggableLIid.replace(document.getElementById(draggableID).parentNode.id, "dependentMeasuresUL"),ui.draggable.html(),dropUlID,content);
                            deleteLI(draggableID);
                        }
                    });




                });
                   
                function createColumn(elmntId,elementName,tarLoc,content){
                    var parentUL=document.getElementById(tarLoc);
                    var x;
                    var count;
                    //  alert("indddd13")
                    var childLI=document.createElement("li");
                    childLI.id=elmntId;
                    childLI.style.width='200px';
                    childLI.style.height='auto';
                    childLI.style.color='white';
                    childLI.className=tarLoc+'Class ui-draggable';

                    var tableStr="<table width='100%'>";
                    tableStr+="<tbody>";
                    tableStr+="<tr valign='top' align='center'>";
                    tableStr+="<td width='70%' align='left'>"+content+"</td>";
//                    alert("tarLoc\t"+tarLoc)
                    if(tarLoc!="MeasuresUL")
                    tableStr+="<td width='30%' align='right'><input type='checkbox' name='Check"+elmntId+"' id='Check"+elmntId+"' checked title='If it is Non-Standard please uncheck the checkbox'></td>";
                    tableStr+="</tr>";
                    tableStr+="</tbody>";
                    tableStr+="</table>";
                    childLI.innerHTML=tableStr;
                    parentUL.appendChild(childLI);
                    $(".sortable").sortable();
                }

                function deleteLI(liID){
                    var LiObj=document.getElementById(liID);
                    try{
                         
                        var parentUL=document.getElementById(LiObj.parentNode.id);
                        parentUL.removeChild(LiObj);
                    }catch(err){
                        alert(err)
                    }
              
                
                }
                function setWhatIfMeasure()
                { 
                    if($("#selectedMeasuresUL").children().length=="0"){
                        alert("Please Select one Whatif measure");
                    }else{

                        var sensitivityFactor=0;
                        var selectedMeasures=new Array();
                        var dependentMeasures=new Array();
                        var selectedMeasuresnames=new Array();
                        var dependentMeasuresnames=new Array();
                        var selectedMeasuresStr;
                        var dependentMeasuresStr;
                        var liId = "";
                        var tableObjs = "";
                        var tbodyObj = "";
                        var trObjs="";
                        var tdObjs = "";
                        var fontObj=""
                        var content = "";
                        var isStandardWhatifMeasures=new Array
                        var isStandardDependentMeasures=new Array
                        if(parent. document.getElementById("sensitivityFactorid").value != ""){
                            sensitivityFactor=parent.$("#sensitivityFactorid").val();
                        }
                     
                        // alert("senFaVar\t"+sensitivityFactor)
                        $("#selectedMeasuresUL").children().each(
                        function(index, value) {
                            selectedMeasures[index]=$(this).attr('id').replace("selectedMeasuresUL","")
                            liId=$(this).attr('id')
                            tableObjs=document.getElementById(liId).getElementsByTagName("table");
                            tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                            trObjs=  tbodyObj[0].getElementsByTagName("tr")
                            tdObjs =  trObjs[0].getElementsByTagName("td")
                            fontObj=tdObjs[0].getElementsByTagName("font")
                            content = fontObj[0].innerHTML
                            selectedMeasuresnames[index]=content
//                             alert("content---"+selectedMeasures[index]+"-----")
                             var idOfLiCheck='#CheckselectedMeasuresUL'+selectedMeasures[index]
                            isStandardWhatifMeasures.push($(idOfLiCheck).is(':checked'))
                        });
                        $("#dependentMeasuresUL").children().each(
                        function(index, value) {
                            dependentMeasures[index]=$(this).attr('id').replace("dependentMeasuresUL","")
                            liId=$(this).attr('id')
                            tableObjs=document.getElementById(liId).getElementsByTagName("table");
                            tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                            trObjs=  tbodyObj[0].getElementsByTagName("tr")
                            tdObjs =  trObjs[0].getElementsByTagName("td")
                            fontObj=tdObjs[0].getElementsByTagName("font")
                            content = fontObj[0].innerHTML
                            dependentMeasuresnames[index]=content
//                            alert("content\t"+dependentMeasures[index])
                             var idOfLiCheck='#CheckdependentMeasuresUL'+dependentMeasures[index]
                            isStandardDependentMeasures.push($(idOfLiCheck).is(':checked'))
                        });


                        if ( selectedMeasures.length == 0 ){
                            selectedMeasuresStr = "";
                            dependentMeasuresStr="";
                           parent.$("#whatifLink").hide()
                        }
                        else{
                            selectedMeasuresStr = selectedMeasures.toString();
                            dependentMeasuresStr= dependentMeasures.toString()
                        }
//                        alert("selectedMeasuresStr\t"+selectedMeasuresStr)
//                        alert("dependentMeasuresStr\t"+dependentMeasuresStr)
//                        alert("isStandardWhatifMeasures\t"+isStandardWhatifMeasures )
//                        alert("isStandardDependentMeasures\t"+isStandardDependentMeasures )

                        //                    alert(selectedMeasuresStr)
                        parent.$("#performWhatIfDiv").dialog('close')
                        //   var whatifTarget = setWhatifTarget(selectedMeasures,selectedMeasuresnames);
                        //alert("selectedMeasuresliast=="+selectedMeasuresliast.length)
                        //  alert("dependentMeasuresnames\t"+dependentMeasuresnames)
                        // alert("dependentMeasures\t"+dependentMeasures)
                        $.ajax({
                            url:'whatIfScenerioAction.do?whatIfParam=setWhatIfMeasures&PbReportId=<%= PbReportId%>&selectedMeasuresliast='+selectedMeasuresStr+'&sensitivityFactor='+sensitivityFactor+'&dependentMeasures='+dependentMeasures+'&isStandardWhatifMeasures='+isStandardWhatifMeasures+'&isStandardDependentMeasures='+isStandardDependentMeasures,
                            success: function(data){
                                if( data == "refresh" ){
                                    var source =ctxPath+"/TableDisplay/pbDisplay.jsp?tabId="+reportID
                                    var dSrc = parent.document.getElementById("iframe1");
                                    dSrc.src = source;
                                    parent.$("#sensitivityFactorid").val("")
                                }

                            }
                        });
                    }
                }
                function openSenSitivity(measureid,measureName,reportid,path){
                    parent.openSenSitivity(measureid,measureName,reportid,path)
                }
                
                function openTargetWhatifScenario(PbReportId,conPath,fType){
                    $.ajax({
                        url:'whatIfScenerioAction.do?whatIfParam=getWhatIfdetails&PbReportId='+PbReportId,
                        success: function(data){
                            var dataJson=eval("("+data+")")
                            whatIfElementIDs=dataJson.whatIfElementIDs
                            measuresNames=dataJson.measuresNames
//                            alert("whatIfelementIdsArray\t"+whatIfElementIDs)
//                            alert("measuresnamesArray\t"+measuresNames)
                             parent.buildWhatifTrgt(whatIfElementIDs, measuresNames,fType,conPath);
                        }
                    });
                   
//                    whatIfElementIDs=whatIfElementIDs.replace("[","").replace("]","")
//                    measuresNames=measuresNames.replace("[","").replace("]","")
//                    var whatIfelementIdsArray=whatIfElementIDs.split(",")
//                    var measuresnamesArray=measuresNames.split(",")
                    //  alert("whatIfelementIdsArray\t"+whatIfelementIdsArray)
                   
                }

                function openEditSenSitivity(repID){
                parent.openEditSenSitivity(repID)
               
                }
                function openGoolSeekDilog(){
                    $("#whatifButtonsTab").hide();
                    whatifOgject=$("#whatifOrGoolseekTd").html()
                    var tempHtml=""
                    $("#whatifOrGoolseekTd").html($("#selectedMeasuresDIV"))
                    $("#goolSeektr").html("<td >Back</td><td><img id='goolSeekImg' src='<%=request.getContextPath()%>/icons pinvoke/arrow-return-180.png' alt='' onclick=goToWhatif()></td>")
                    $("#whatifGoolSeekFont").html("Gool Seek measures")
                      $(".selectedMeasuresULClass").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                }
             function goToWhatif(){
                // parent.window.frames["performWhatIfFrame"].location.href =parent.window.frames["performWhatIfFrame"].location.href
                 var source ="<%=request.getContextPath()%>/pbWhatIfscenario.jsp?PbReportId=<%=PbReportId%>"
                var dSrc = parent.document.getElementById("performWhatIfFrame");
                dSrc.src = source;
             }


        </script>

        <form action="" method="post" name="whatIfform">
<!--            <table align="right"><tr id="goolSeektr"><td >Gool Seek</td><td><img id="goolSeekImg" src="<%=request.getContextPath()%>/icons pinvoke/arrow.png" alt="" onclick="openGoolSeekDilog()"/></td> </tr> </table>-->
            <br/><br/>
            <center>
                <div  style="width: 90%; display:block"  class="ui-tabs ui-widget ui-widget-content ui-corner-all">
                    <div class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="width:89%;" >

                        <table  align="center" border="1" style="width: 100%;height:auto ">
                            <tr  align="center" valign="top" style="min-width:40%; max-width: 100%"  >
                                <td align="left" valign="top" width="50%">
                                    <div style="height:400px; overflow:auto" id="MeasuresDIV"  align="left" onmouseup="dropDiv('MeasuresUL')">
                                        <div> <table  align="center" border="0" width="100%">
                                                <tr  align="left" valign="top" style=" background-color: rgb(180, 217, 238); color: black;">
                                                    <td style="font-weight:bold" align="center"> <div class="navtitle-hover" style="height: 20px;"><font size="2" style="font-weight: bold;"><%=TranslaterHelper.getTranslatedInLocale("Base_Measures", cL)%>  </font> </div></td>

                                                </tr></table></div><div style="overflow: auto">
                                            <ul id="MeasuresUL" class="sortable" >
                                                <%
                                                            for (int liCount = 0; liCount < measuresList.size(); liCount++) {
                                                                if (selectedMeasureList == null || !selectedMeasureList.contains(measuresList.get(liCount))) {%>

                                                <li  class="MeasuresULClass" id="MeasuresUL<%= measuresList.get(liCount)%>"  style="width:200px;height:auto;color:white">
                                                    <table width="100%">
                                                        <tr align="left" valign="top">
                                                            <td width="70%" align="left" colspan="1"><font size="1" style="font-weight:normal ;color: black;font-style:normal "><%= measuresNameList.get(liCount)%></font></td>
                                                        </tr>
                                                    </table>
                                                </li>
                                                <% }
                                                            }

                                                %>
                                            </ul></div>
                                    </div>
                                </td>
                                <td  align="left" valign="top" width="50%" id="whatifOrGoolseekTd">
                                    <div style="height:200px; overflow:auto" id="selectedMeasuresDIV" align="left" onmouseup="dropDiv('selectedMeasuresUL')">
                                        <div> <table  align="center" border="0" width="100%">
                                                <tr  align="left" valign="top" style=" background-color: rgb(180, 217, 238); color: black;">
                                                    <td style="font-weight:bold" align="center">  <div class="navtitle-hover" style="height: 20px;"><font id="whatifGoolSeekFont" size="2" style="font-weight: bold;"><%=TranslaterHelper.getTranslatedInLocale("WhatIf_Measures", cL)%>   </font> </div></td>
                                                </tr></table></div><div style="overflow: auto">
                                            <ul id="selectedMeasuresUL" class="sortable">
                                                <% if (selectedMeasureList != null) {
                                                            for (int SelmesCount = 0; SelmesCount < selectedMeasureList.size(); SelmesCount++) {%>
                                                <li class="selectedMeasuresULClass" id="selectedMeasuresUL<%= selectedMeasureList.get(SelmesCount)%>"  style="width:200px;height:auto;color:white">
                                                    <table width="100%">
                                                        <tr align="left" valign="top">
                                                            <%-- <td width="30%" align="left"><a href="javascript:deletecolumnli('MeasuresUL<%= measures.get(mlen)%>')"  class="ui-icon ui-icon-close"></a></td>--%>
                                                            <td width="70%" align="left" colspan="1"><font size="1" style="font-weight:normal ;color: black;font-style:normal "><%= measureNamesAndElmIdHm.get(selectedMeasureList.get(SelmesCount))%></font></td>
                                                           <%if(!stdNonStdDetails.isEmpty() && !stdNonStdDetails.get(selectedMeasureList.get(SelmesCount)).equalsIgnoreCase("true") ){%>
                                                            <td width="30%" align="left" colspan="1"><input type='checkbox'  name="CheckselectedMeasuresUL<%= selectedMeasureList.get(SelmesCount)%>" id="CheckselectedMeasuresUL<%= selectedMeasureList.get(SelmesCount)%>" title="If it is Non-Standard please uncheck the checkbox"></td>
                                                        <%}else{%>
                                                             <td width="30%" align="left" colspan="1"><input type='checkbox' checked name="CheckselectedMeasuresUL<%= selectedMeasureList.get(SelmesCount)%>" id="CheckselectedMeasuresUL<%= selectedMeasureList.get(SelmesCount)%>" title="If it is Non-Standard please uncheck the checkbox"></td>
                                                            <%}%>
                                                        </tr>
                                                    </table>
                                                </li>
                                                <% }
                                                            }

                                                %>
                                            </ul></div>
                                    </div>
                                    <div style="height:200px; overflow:auto" id="dependentMeasuresDIV" align="left" onmouseup="dropDiv('dependentMeasuresUL')">
                                        <div> <table  align="center" border="0" width="100%">
                                                <tr  align="left" valign="top" style=" background-color: rgb(180, 217, 238); color: black;">
                                                    <td style="font-weight:bold" align="center">  <div class="navtitle-hover" style="height: 20px;"><font size="2" style="font-weight: bold;"> <%=TranslaterHelper.getTranslatedInLocale("Dependent_Measures", cL)%>  </font> </div></td>
                                                </tr></table></div><div style="overflow: auto">
                                            <ul id="dependentMeasuresUL" class="sortable">
                                                <% if (dependantMeasures != null && dependantMeasures.size() != 0) {
                                                                    for (String string : dependantMeasures) {%>
                                                <li class="dependentMeasuresULClass" id="dependentMeasuresUL<%= string%>"  style="width:200px;height:auto;color:white">
                                                    <table width="100%">
                                                        <tr align="left" valign="top">
                                                            <%-- <td width="30%" align="left"><a href="javascript:deletecolumnli('MeasuresUL<%= measures.get(mlen)%>')"  class="ui-icon ui-icon-close"></a></td>--%>
                                                            <td width="70%" align="left" colspan="1"><font size="1" style="font-weight:normal ;color: black;font-style:normal "><%= measureNamesAndElmIdHm.get(string)%></font></td>
                                                            <%if(!stdNonStdDetails.isEmpty() && !stdNonStdDetails.get(string).equalsIgnoreCase("true") ){%>
                                                            <td width="30%" align="left" colspan="1"><input type='checkbox'  name="CheckdependentMeasuresUL<%= string%>" id="CheckdependentMeasuresUL<%= string%>"  title="If it is Non-Standard please uncheck the checkbox"></td>
                                                          <%}else{%>
                                                             <td width="30%" align="left" colspan="1"><input type='checkbox' checked name="CheckdependentMeasuresUL<%= string%>" id="CheckdependentMeasuresUL<%= string%>"  title="If it is Non-Standard please uncheck the checkbox"></td>
                                                            <%}%>
                                                        </tr>
                                                    </table>
                                                </li>
                                                <%}
                                                                }%>
                                            </ul>
                                        </div></div>
                                </td>
                            </tr>
                        </table>
                                            <table id="whatifButtonsTab">
                            <tr>
                                <td>
                                    <input type="button" onclick="setWhatIfMeasure()" name="save" value="<%=TranslaterHelper.getTranslatedInLocale("save", cL)%>"  class="navtitle-hover" >
                                </td>
                                <td>
                                    <input type="button" onclick="openSenSitivity('<%=originalCols.get(0)%>','<%=container.getDisplayLabels().get(0)%>','<%=PbReportId%>','<%= request.getContextPath()%>')" name="defineSensitivity" value="<%=TranslaterHelper.getTranslatedInLocale("Define_Sensitivity", cL)%>"  class="navtitle-hover">
                                </td>
                                <td>
                                    <input type="button" onclick="openTargetWhatifScenario('<%=PbReportId%>','<%=request.getContextPath()%>','Define')" name="DefineMeasure" value="<%=TranslaterHelper.getTranslatedInLocale("Define_Target", cL)%>" class="navtitle-hover" title="Define WhatIf Target Measure" >
                                </td>
                                <% if (!whatIfTrgtLst.isEmpty()) {%>
                                <td>
                                    <input type="button" onclick="openTargetWhatifScenario('<%=PbReportId%>','<%=request.getContextPath()%>','Edit')" name="editTarget" value="<%=TranslaterHelper.getTranslatedInLocale("Edit_Target", cL)%>" class="navtitle-hover" title="Edit WhatIf Target Measure" >
                                </td>
                                <%} if( whatIfSensitivity!=null && whatIfSensitivity.isSensitivitySetForDimension() ){%>
                                 <td>
                                     <input type="button" onclick="openEditSenSitivity('<%=PbReportId%>')" name="editSensitivity" id="editSensitivity" value="<%=TranslaterHelper.getTranslatedInLocale("Edit_Sensitivity", cL)%>"  class="navtitle-hover">
                                </td>
                                <%}%>
                            </tr>
                        </table>
                    </div> </div></center></form>
                               
    </body>
</html>
