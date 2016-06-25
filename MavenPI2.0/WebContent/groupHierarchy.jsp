<%--
    Document   : groupHierarchy
    Created on : 30 May, 2012, 6:49:28 PM
    Author     : arun
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.business.group.BusinessGroupEditAction,java.util.ArrayList,java.util.HashMap,javax.servlet.http.HttpSession,prg.db.PbDb" %>





<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
  String themeColor = "blue";
    String conid=request.getParameter("connId");
    String groupid=request.getParameter("grpId");
    String tabid=request.getParameter("tabId");
    HttpSession session1=request.getSession();
      session1.setAttribute("tabid",tabid);     
       
    BusinessGroupEditAction groupaction=new BusinessGroupEditAction();
    //added by nazneen
    String groupName=groupaction.getGroupName(tabid);
    String groupTabId=groupaction.getGroupTabId(tabid);
    session1.setAttribute("groupTabId",groupTabId);
      
    HashMap HierarchyColumnsNames=groupaction.getHierarchyColumnsNames(groupTabId);
    ArrayList parentcollist= (ArrayList)HierarchyColumnsNames.get("parentcollist");
    ArrayList childcollist=(ArrayList)HierarchyColumnsNames.get("childcollist");
    
   
   HashMap factcolnames=groupaction.getFactColumnsNames(request, groupTabId,tabid);
   ArrayList idlist= (ArrayList)factcolnames.get("bussid");
   ArrayList namelist=(ArrayList)factcolnames.get("bussname");
   //end by Nazneen
String conextPath=request.getContextPath();
%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <script type="text/javascript" src="<%=conextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>

        <script type="text/javascript" src="<%=conextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=conextPath%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
        <link type="text/css" href="<%=conextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <title>JSP Page</title>
        <script type="text/javascript">
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
                    });});
                </script>
         
    </head>
    <body>
        <center>
            <table>
                <tr>
                <td style="font-weight:bold" align="left"> <div class="navtitle-hover" style="height: 20px;"><font size="2" style="font-weight: bold;">&nbsp;&nbsp;Group Name:&nbsp;&nbsp;<%= groupName%> &nbsp;&nbsp;</font> </div></td>
                </tr>                
            </table><br>
               <div  style="width: 90%; display:block"  class="ui-tabs ui-widget ui-widget-content ui-corner-all">
                    <div class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="width:89%;" >

                        <table  align="center" border="1" style="width: 100%;height:auto ">
                            <tr  align="center" valign="top" style="min-width:40%; max-width: 100%"  >
                                <td align="left" valign="top" width="50%">
                                    <div style="height:400px; overflow:auto" id="MeasuresDIV"  align="left" onmouseup="dropDiv('MeasuresUL')">
                                        <div> <table  align="center" border="0" width="100%">
                                                <tr  align="left" valign="top" style=" background-color: rgb(180, 217, 238); color: black;">
                                                    <td style="font-weight:bold" align="center"> <div class="navtitle-hover" style="height: 20px;"><font size="2" style="font-weight: bold;">measure Names</font> </div></td>

                                                </tr></table></div><div style="overflow: auto">
                                            <ul id="MeasuresUL" class="sortable" >
                                               <%
                                                            for (int liCount = 0; liCount < namelist.size(); liCount++) {
                                                                 {%>

                                                <li  class="MeasuresULClass" id="MeasuresUL<%= idlist.get(liCount)%>"  style="width:200px;height:auto;color:white">
                                                    <table width="100%">
                                                        <tr align="left" valign="top">
                                                            <td width="70%" align="left" colspan="1"><font size="1" style="font-weight:normal ;color: black;font-style:normal "><%= namelist.get(liCount)%></font></td>
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
                                                    <td style="font-weight:bold" align="center">  <div class="navtitle-hover" style="height: 20px;"><font id="whatifGoolSeekFont" size="2" style="font-weight: bold;">Perent Hierarchy</font> </div></td>
                                                </tr></table></div><div style="overflow: auto">
                                            <ul id="selectedMeasuresUL" class="sortable">

                                            </ul></div>
                                    </div>
                                    <div style="height:200px; overflow:auto" id="dependentMeasuresDIV" align="left" onmouseup="dropDiv('dependentMeasuresUL')">
                                        <div> <table  align="center" border="0" width="100%">
                                                <tr  align="left" valign="top" style=" background-color: rgb(180, 217, 238); color: black;">
                                                    <td style="font-weight:bold" align="center">  <div class="navtitle-hover" style="height: 20px;"><font size="2" style="font-weight: bold;">Child Hierarchy</font> </div></td>
                                                </tr></table></div><div style="overflow: auto">
                                            <ul id="dependentMeasuresUL" class="sortable">

                                            <%--    <li class="dependentMeasuresULClass" id="dependentMeasuresUL"  style="width:200px;height:auto;color:white">

                                                </li>--%>

                                            </ul>
                                        </div></div>
                                </td>
                            </tr>
                        </table>
                         <table id="whatifButtonsTab"><br>
                            <tr>
                                <td>
                                    <input type="button" onclick="saveGropMeasure()" name="save" value="Save"  class="navtitle-hover" >
                                </td>

                            </tr>
                        </table>
             
                    </div> </div></center>
                                            <script type="text/javascript">
         function dropDiv(dropId){
                    var dropId = dropId;
                }

               

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
                   <%-- tableStr+="<td width='30%' align='right'><input type='checkbox' name='Check"+elmntId+"' id='Check"+elmntId+"' checked title='If it is Non-Standard please uncheck the checkbox'></td>";--%>
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
                    function saveGropMeasure()
                {

                    if($("#selectedMeasuresUL").children().length=="0"){
                        alert("Please Select one Group measure");
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
                        <%--if(parent. document.getElementById("sensitivityFactorid").value != ""){
                            sensitivityFactor=parent.$("#sensitivityFactorid").val();
                        }--%>

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
                          var tempvalues=dependentMeasuresStr.substr(1,dependentMeasuresStr.length);
                          var childmeasures=tempvalues.split(",");

                   <%--

                        alert("dependentMeasuresStr\t"+dependentMeasuresStr)
                        alert('grpvalues--->'+grpvalues)
                        alert("isStandardWhatifMeasures\t"+isStandardWhatifMeasures )
                        alert("isStandardDependentMeasures\t"+isStandardDependentMeasures )--%>

                        //                    alert(selectedMeasuresStr)
                        parent.$("#groupHierarchyDialog").dialog('close')
                        //   var whatifTarget = setWhatifTarget(selectedMeasures,selectedMeasuresnames);
                        //alert("selectedMeasuresliast=="+selectedMeasuresliast.length)
                        //  alert("dependentMeasuresnames\t"+dependentMeasuresnames)
                        // alert("dependentMeasures\t"+dependentMeasures)
                       
                        $.ajax({
                            url:'businessgroupeditaction.do?groupdetails=saveGroupHierarchy&selectedMeasuresliast='+selectedMeasuresStr+'&childmeasures='+dependentMeasuresStr+'&busstabid='+<%=groupTabId%>+'&tabid='+<%=tabid%>,
                            success: function(data){
                              if(data=="false")
                                alert("Hierarchy not Created")
                            else
                                alert("Hierarchy created")  
                            
                            window.location.href = window.location.href;
                               
                            }
                        });
                    }
                }

         </script>
    </body>
</html>
