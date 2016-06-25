<%--
    Document   : ruleHelp
    Created on : 16 Mar, 2011, 2:00:58 PM
    Author     :sreekanth
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
            String fromModule = "";
            String referenceID = "";
            String portalTabName = "";
            if (request.getParameter("fromModule") != null) {
                fromModule = request.getParameter("fromModule");
            }
            if (request.getParameter("portletId") != null) {
                referenceID = request.getParameter("portletId");
            }
            if (request.getParameter("portalTabName") != null) {
                portalTabName = request.getParameter("portalTabName");
            }
String contXPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">-->
        <script type="text/javascript" src="<%=contXPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contXPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contXPath%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contXPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
         <script src="<%=contXPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contXPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <link rel="stylesheet" href="<%=contXPath%>/stylesheets/treeviewstyle/screen.css" />
        <link href="<%=contXPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contXPath%>/javascript/jquery.contextMenu.js" ></script>
        <script type="text/javascript" src="<%=contXPath%>/javascript/hashMapScript.js"></script>
        <script type="text/javascript" src="<%=contXPath%>/javascript/quicksearch.js"></script>

        <script type="text/javascript" src="<%=contXPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contXPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contXPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contXPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contXPath%>/tablesorter/docs/js/docs.js"></script>

        <script type="text/javascript" src="<%=contXPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>
        <link type="text/css" href="<%=contXPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contXPath%>/javascript/pi.js"></script>

        <title>Rules</title>
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
        <form name="rulesForm" id="rulesForm" method="post" action="">
            <div align="center">
                <table>
                    <tr><% if (!fromModule.equalsIgnoreCase("PORTAL")) {%>
                        <td style="font-size: 13px" style="display:none">
                            Rule Name:
                        </td>
                        <td>
                            <input type="text" id="ruleName" name="ruleName" value="" >
                        </td><%}%>
                        <td style="font-size: 13px">Rule on:</td>
                        <td><select id="ruleType" name="ruleType" onclick="changeRuleList()" >
                                <option value="Measure-Dimension" selected>Measure-Dimension</option>
                                <option value="Measure">Measure</option>
                                <option value="Dimension">Dimension</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </div>
            <div  id="rulesdiv" style="">
                <table border="1" width="100%">
                    <tr>
                                                <td>
                                                    <table width="100%">
                                                        <tr valign="top" id="measuresTr">
                        <td width="50%" valign="top" class="draggedTable1">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Measures from below</font></div>
                            <div id="measureDiv" style="height:150px;overflow:scroll">
                                <ul id="measuresList" class="filetree treeview-famfamfam">
                                    <ul id="measures" >


                                    </ul>
                                </ul>
                            </div>
                        </td>
                            </tr>
                            <tr id="dimensionsTr">
                                <td width="50%" valign="top" class="draggedTable1" >
                                    <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Dimensions from below</font></div>
                                    <div  id="dimensionDiv" style="height:150px;overflow:scroll">
                                        <ul id="dimensions" class="sortable">
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                        <td id="selectedMeasures" width="50%" valign="top">
                            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag to here</font></div>
                            <div style="height:350px;overflow:auto" id="filterselectedDiv">
                                <ul id="sortableUL">

                                </ul>
                            </div>
                        </td>
                    </tr>

                </table>
                <table border="1" width="100%">
                    <tr><td>
                            <textarea id="rulesRegionShow" name="rulesRegionShow" style="width:99%;height:100px" readonly></textarea>
                            <textarea id="rulesRegionForSave" name="rulesRegionForSave" style="width:99%;;height:100px;display:none" readonly></textarea>
                        </td>

                        <td>
                            <div id="oprationsPad">
                                <table cellspacing="4" border="0" align="center">
                                    <tbody><tr align="center" style="width: 100%;">
                                            <td>
                                                <table>
                                                    <tbody>
                                                        <tr>
                                                            <td>
                                                                <table>
                                                                    <tr>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="1" id="1" onclick="ruleOperators('1')"></td>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="2" id="2" onclick="ruleOperators('2')"></td>
                                                                        <td><input type="button" value="3" class="navtitle-hover" style="width: 40px;" id="3" onclick="ruleOperators('3')"></td>
                                                                        <td><input type="button" value="/" class="navtitle-hover" style="width: 40px;" id="/" onclick="ruleOperators('/')"></td>
                                                                        <td><input type="button" value="Clr" class="navtitle-hover" style="width: 40px;" id="clear" onclick="clearRule()"></td>
                                                                        <td><input type="button" value="<" class="navtitle-hover" style="width: 40px;" id="clear" onclick="ruleOperators('<')"></td>
                                                                    </tr> <tr>
                                                                        <td><input type="button" value="4" class="navtitle-hover" style="width: 40px;" id="4" onclick="ruleOperators('4')"></td>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="5" id="5" onclick="ruleOperators('5')"></td>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="6" id="6" onclick="ruleOperators('6')"></td>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="*" id="*" onclick="ruleOperators('*')"></td>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="(" id="(" onclick="ruleOperators('(')"></td>
                                                                        <td><input type="button" value=">" class="navtitle-hover" style="width: 40px;" id="clear" onclick="ruleOperators('>')"></td>
                                                                    </tr></table> </td> </tr> <tr> <td>
                                                                <table> <tr>
                                                                        <td><input type="button" value="7" class="navtitle-hover" style="width: 40px;" id="7" onclick="ruleOperators('7')"></td>
                                                                        <td><input type="button" value="8" class="navtitle-hover" style="width: 40px;" id="8" onclick="ruleOperators('8')"></td>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="9" id="9" onclick="ruleOperators('9')"></td>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="-" id="-" onclick="ruleOperators('-')"></td>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value=")" id=")" onclick="ruleOperators(')')"></td>
                                                                        <td><input type="button" value=">=" class="navtitle-hover" style="width: 40px;" id="clear" onclick="ruleOperators('>=')"></td>
                                                                    </tr><tr>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="0" id="0" onclick="ruleOperators('0')"></td>
                                                                        <td><input type="button" value="." class="navtitle-hover" style="width: 40px;" id="." onclick="ruleOperators('.')"></td>
                                                                        <td><input type="button" value="=" class="navtitle-hover" style="width: 40px;" id="=" onclick="ruleOperators('=')"></td>
                                                                        <td><input type="button" value="+" class="navtitle-hover" style="width: 40px;" id="+" onclick="ruleOperators('+')"></td>
                                                                        <td><input type="button" value="%" class="navtitle-hover" style="width: 40px;" id="%" onclick="ruleOperators('%')"></td>
                                                                        <td><input type="button" value="<=" class="navtitle-hover" style="width: 40px;" id="clear" onclick="ruleOperators('<=')"></td>
                                                                    </tr><tr>
                                                                        <td><input type="button" class="navtitle-hover" style="width: 40px;" value="And" id="And" onclick="ruleOperators('And')"></td>
                                                                        <td><input type="button" value="Or" class="navtitle-hover" style="width: 40px;" id="Or" onclick="ruleOperators('Or')"></td>
                                                                        <td><input type="button" value="Like" class="navtitle-hover" style="width: 40px;" id="Like" onclick="ruleOperators('Like')"></td>
                                                                        <td><input type="button" value="As" class="navtitle-hover" style="width: 40px;" id="As" onclick="ruleOperators('As')"></td>
                                                                        <td><input type="button" value="Cl_All" class="navtitle-hover" style="width: 40px;" id="Cl_All" onclick="clearAll()"></td>
                                                                        <td><input type="button" value="==" class="navtitle-hover" style="width: 40px;" id="clear" onclick="ruleOperators('==')"></td>
                                                                    </tr> </table></td>
                                                        </tr>

                                                    </tbody></table>
                                            </td>

                                        </tr>

                                    </tbody></table>
                            </div>

                        </td>
                    </tr>
                </table>
                <br/><table align="center">
                    <tr>
                        <td>
                            <input type="button" id="saveRule" name="saveRule" value="Save" onclick="saveRules()"  class="navtitle-hover">
                        </td>
<!--                        <td>
                            <input type="button" id="ruleResult" name="ruleResult" value="Show Result" onclick="showResult()"  class="navtitle-hover">
                        </td>-->
                        <td>
                            <input type="button" id="resetRule" name="resetRule" value="Clear" onclick="deleteRule()"  class="navtitle-hover">
                        </td>
                    </tr>
                </table>
            </div>
                        <div id="dimensionMembersDiv" title="Dimention Members" style="display:none">
                            <div id="pamMembersLis">

                            </div>
                            <table align="center" ><tr><td><input type="button" class="navtitle-hover" name="sve" value="Next" onclick="saveDimMemBers()"></td> </tr>

                            </table>
                        </div>
        </form>
                         <script type="text/javascript">
            var actualRuleArray=new Array();
            var displayRuleArray=new Array();
            var grpColArrayforRule=new Array();
             var assinIdAndVales=new Array
            var isMemberUseInOtherLevel="false"
            var rulesElementsArray=new Array("And","Or","Like","As","==","<=",">=","/","*","-","=","<",">","+","%");
            var measureIds=new Array();
            var dimensionIds=new Array();
            var grpColArray=new Array();
            var ruleOn="";
            var dimIDs=new Array()
            var dimMembersArray=new Array();
            function LTrim( value ) {

                var re = /\s*((\S+\s*)*)/;
                return value.replace(re, "$1");

            }

            // Removes ending whitespaces
            function RTrim( value ) {

                var re = /((\s*\S+)*)\s*/;
                return value.replace(re, "$1");

            }

            // Removes leading and ending whitespaces
            function trim( value ) {

                return LTrim(RTrim(value));
            }
            $(document).ready(function(){
                getMeasures()
                getDimensions()
                $.get("<%= request.getContextPath()%>/rulesHelpAction.do?rulesHelp=getFilterDetails&refID=<%=referenceID%>",function(data){
                    // alert("data\t"+data)
                    if(trim(data)!=""){
                        var dataJson=eval("("+data+")")
                        $("#ruleName").val(dataJson.ruleName[0])
                        actualRuleArray=dataJson.actValRule
                        displayRuleArray=dataJson.displayRule
                        measureIds=dataJson.measureIds
                        $("#rulesRegionShow").val(displayRuleArray.toString().replace(","," ","gi"))
                        for(var i=0;i<measureIds.length;i++){
                            for(var j=0;j<actualRuleArray.length;j++){
                                if(jQuery.trim(actualRuleArray[j])==jQuery.trim(measureIds[i]))
                                    createColumnruleMember(measureIds[i],displayRuleArray[j],"sortableUL","measure");

                            } }}
                });

            })
            Array.prototype.last = function() {return this[this.length-1];}
            Array.prototype.contains=function(val){
                var returnVal=false
                for(var i=0;i<this.length;i++){
                    if(this[i]==val){
                        returnVal=true;
                        break;
                    }
                    return returnVal;
                }
            }

            function changeRuleList(){
                    ruleOn=$("#ruleType").val()

            //var ruletype=$("select#ruleType").val
                                var ruletype=$("#ruleType option:selected").text();

                   if(ruletype=='Measure')
                       {
                       $("#dimensionsTr").hide();
                       $("#measuresTr").show();
                       $("#measureDiv").attr("style", "height:300px;overflow:scroll");
                       }
                   else if(ruletype=='Dimension')
                       {
                       $("#measuresTr").hide();
                        $("#dimensionsTr").show();
                       $("#dimensionDiv").attr("style", "height:300px;overflow:scroll");

                       }
                       else if(ruletype=='Measure-Dimension')
                           {
                                $("#measuresTr").show();
                                $("#dimensionsTr").show();
                                $("#dimensionDiv").attr("style", "height:150px;overflow:scroll");
                                $("#measureDiv").attr("style", "height:150px;overflow:scroll");
                           }

            }
            function getMeasures(){

                var urlVar="";
                if(<%=fromModule.equalsIgnoreCase("PORTAL")%>){
                    urlVar="rulesHelpAction.do?rulesHelp=getMeasureForRule&fromModule=PORTAL&portletId=<%=referenceID%>"
                }else{
                    urlVar=""
                }

                $.ajax({
                    url:urlVar,
                    success: function(data){
                        if(data!=""){
                            $("#quicksearch").remove();
                            $("#measures").html("");
                            $("#measures").html(data);
                            $("#Scorecards").remove()
                            $("#measuresList").treeview({
                                animated:"slow"
                                //persist: "cookie"
                            });
                            $('ul#measuresList li').quicksearch({
                                position: 'before',
                                attached: 'ul#measuresList',
                                loaderText: '',
                                delay: 100
                            })
                            var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                            $(dragMeasure).draggable({
                                helper:"clone",
                                effect:["", "fade"]
                            });

                        }

                    }
                });


            }
            function getDimensions()
            {
                var urlVal=""
                if(<%=fromModule.equalsIgnoreCase("PORTAL")%>){
                    urlVal="rulesHelpAction.do?rulesHelp=getDimentionsforRule&fromModule=PORTAL&portletId=<%=referenceID%>";
                }else{
                    urlVal=""
                }

                $.ajax({
                    url:urlVal,
                    success: function(data){
                        $("#dimensions").html('')
                        $("#dimensions").html(data)
                        initDrag();

                    }

                });

            }
            function initDrag(){

                $(".DimensionULClass").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $("#selectedMeasures").droppable({
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span, .DimensionULClass',

                    drop: function(ev, ui) {
                        var type=$("#"+ui.draggable.attr("id")).parent().attr("id");
                        if(jQuery.trim(type)=="dimensions"){
                            createColumnruleMember(ui.draggable.attr('id'),$("#"+ui.draggable.attr('id')+" > table > tbody > tr > td " ).html(),"sortableUL",type);

                        }else{
                            createColumnruleMember(ui.draggable.attr('id'),ui.draggable.html(),"sortableUL","measure");
                        }


                    }
                })

                $("#sortable").sortable();
                $("#DimensionsUL").sortable();

            }
            
            function openDimMemberDiv(elementID){
//            alert("elementID\t"+elementID)
             $("#dimensionMembersDiv").dialog({
                    autoOpen: false,
                    height: 450,
                    width: 450,
                    position: 'justify',
                    modal: true
                });
                var contextPathVar='<%=request.getContextPath() %>'
                $.post("rulesHelpAction.do?rulesHelp=getruleDimMembers&elementID="+elementID+"&path="+contextPathVar,function(data){
                         var jsonVar=eval('('+data+')')

                       $("#dimensionMembersDiv").dialog('open')
                       $("#pamMembersLis").html("")
                       $("#pamMembersLis").html(jsonVar.htmlStr)
                     isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                    $("#myList3").treeview({
                        animated:"slow",
                        persist: "cookie"
                    });

                    $('ul#myList3 li').quicksearch({
                        position: 'before',
                        attached: 'ul#myList3',
                        loaderText: '',
                        delay: 100
                    })
                    $(".myDragTabs").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#dropTabs").droppable({
                        activeClass:"blueBorder",
                        accept:'.myDragTabs',
                        drop: function(ev, ui) {
                            createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                        }
                    }
                );
                     grpColArray=jsonVar.memberValues
                    $(".sortable").sortable();
                       
                })
            }

            function createColumnruleMember(elmntId,elementName,tarLoc,type){

                var scorecard=elmntId.substring(0, 6);
                if(scorecard=="Scards")
                {
                    var y=ScoreCardIds.toString();
                    if(y.match(elmntId)==null){
                        ScoreCardIds.push(elmntId);
                    }
                }
                var parentUL=document.getElementById(tarLoc);
                var x=grpColArrayforRule.toString();
                if(x.match(elmntId)==null){
                    grpColArrayforRule.push(elmntId);

                    var childLI=document.createElement("li");
                    childLI.id='GrpCol'+elmntId;

                    childLI.style.width='auto';
                    childLI.style.height='auto';
                    childLI.style.color='white';
//
//                    if(jQuery.trim(type)=="dimensions")
//                    childLI.className='navtitle-hover accordion';
//                    else
                    childLI.className='navtitle-hover';

                    var table=document.createElement("table");
                    table.id="GrpTab"+elmntId;
                    var row=table.insertRow(0);
                    var cell1=row.insertCell(0);

                    var a=document.createElement("a");
                    var deleteElement = 'GrpCol'+elmntId;
                    a.href="javascript:deleteColumnOfRule('"+deleteElement+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                    var cell2=row.insertCell(1);
                    cell2.style.color='black';
                    var b=document.createElement("a");
                    b.href="javascript:selectRuleElement('"+elmntId+"','"+elementName+"','"+jQuery.trim(type)+"')";
                    //                      alert("elementName\t"+elementName)
                    if(jQuery.trim(type)=="dimensions"){
                        b.innerHTML=elementName+"(D)"
                        var cell3=row.insertCell(2);
                         cell3.innerHTML="<img src='<%= request.getContextPath() %>/images/jquerybubblepopup-theme/all-grey/tail-bottom.png' alt='' onclick=openDimMemberDiv('"+elmntId+"')>";
                    }else if(jQuery.trim(type)=="DimMember"){
                         b.innerHTML=elementName+"(DM)"
                    }else{
                        b.innerHTML=elementName+"(M)"
                        //cell2.innerHTML=elementName+"(M)";
                    }
                    cell2.appendChild(b)
                    childLI.appendChild(table);
                    parentUL.appendChild(childLI);
//                    $(".accordion").accordion()
                }
            }

            function deleteColumnOfRule(index){
                var LiObj=document.getElementById(index);
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);;
                var x=index.replace("GrpCol","");
                var i=0;

                for(i=0;i<grpColArrayforRule.length;i++){
                    if(grpColArrayforRule[i]==x)
                        grpColArrayforRule.splice(i,1);
                }

            }
            function selectRuleElement(elemtId,elemtName,type){
                             dimIDs.push(elemtId)
                             //  alert("elemtName\t"+elemtName)
                               var rule="";
                if(actualRuleArray.last()!==elemtId){
                     rule=$("#rulesRegionShow").val()
                    //               alert("rule\t"+rule)
                    rule+=" "+elemtName
                    
                    actualRuleArray.push(elemtId)
                    displayRuleArray.push(elemtName)
                    if(jQuery.trim(type)=="measure"){
                        if(!measureIds.contains(elemtId))
                            measureIds.push(elemtId)
                    }else{
                        rule+=" in"
                        //$("#rulesRegionShow").val(rule)
                        if(!dimensionIds.contains(elemtId))
                            dimensionIds.push(elemtId)
                    }

                }
                $("#rulesRegionShow").val(rule)
            }
            function saveRules(){
                //                alert("actualRuleArray\t"+actualRuleArray)
                //                alert("displayRuleArray\t"+displayRuleArray)
                //                alert("<%=referenceID%>")
                var ruleName
                var ruleDesc
                if('<%=fromModule%>'=="PORTAL"){

                    ruleName=displayRuleArray[0]+"PortalRule"
                    ruleDesc=displayRuleArray[0]+"PortalRule"
                }else{
                    ruleName=$("#ruleName").val()
                    ruleDesc=$("#ruleName").val()
                }
                var portaTabName = '<%=portalTabName%>'               
                if(jQuery.trim(ruleName)!=""){
                    $.ajax({
                        url:"rulesHelpAction.do?rulesHelp=saveRule&ruleType=<%=fromModule%>&refId=<%=referenceID%>&ruleName="+ruleName+'&ruleDesc='+ruleDesc+'&actualRuleArray='+actualRuleArray+'&displayRuleArray='+displayRuleArray+'&measureIds='+measureIds+'&ruleOn='+ruleOn+'&dimIDs='+dimIDs+'&dimMembersArray='+dimMembersArray,
                        success:function(data){
                            if(data=="true" && '<%=fromModule%>'=="PORTAL"){
                                parent.$("#rulesDiv").dialog('close')
                                parent.document.forms.frmParameter.action="pbPortalViewer.jsp#"+jQuery.trim(portaTabName.replace(" ","_","gi"))
                                parent.document.forms.frmParameter.submit();
                            }
                        }
                    });
                }else{
                    alert("Please enter rule name")
                }

            }

            function ruleOperators(operator)
            {
                var lastValInactualRuleArray = actualRuleArray.last()
                if(rulesElementsArray.contains(lastValInactualRuleArray)){
                    alert("Already operator is selected .If you want to Change delete previous operator.")
                }else{
                    var rule=$("#rulesRegionShow").val()
                    //               alert("rule\t"+rule)
                    rule+=operator
                    $("#rulesRegionShow").val(rule)
                    var lastValndisplayRuleArray=displayRuleArray.last()

                    if(!isNaN(lastValndisplayRuleArray) && !isNaN(operator)){
                        // alert("in if")
                        var temp=actualRuleArray.last()
                        actualRuleArray[actualRuleArray.length-1]= temp+operator
                        displayRuleArray[displayRuleArray.length-1]=temp+operator
                    }else{
                        //  alert("in else")
                        actualRuleArray.push(" "+operator)
                        displayRuleArray.push(" "+operator)
                    }

                }

            }
            function clearRule(){
                actualRuleArray.pop()
                displayRuleArray.pop()
                $("#rulesRegionShow").val("")
                $.each(displayRuleArray, function(index, value) {
                    var rule=$("#rulesRegionShow").val()
                    rule+=" "+value
                    $("#rulesRegionShow").val(rule)
                });


            }
            function deleteRule(){
                var refid='<%=referenceID%>'
                var portaTabName = '<%=portalTabName%>'
                $('#filterselectedDiv').html("");
                $.ajax({
                    url:"rulesHelpAction.do?rulesHelp=deleteRule&ruleType=<%=fromModule%>&refId="+refid,
                    success:function(data){                        
                        if(data=="true" && '<%=fromModule%>'=="PORTAL"){                            
                            alert("Filter has been deleted permanently.Now you can create new filter")
                             parent.document.forms.frmParameter.action="pbPortalViewer.jsp#"+jQuery.trim(portaTabName.replace(" ","_","gi"));
                             parent.document.forms.frmParameter.submit();
                        }else{
                            alert("Filter has not been deleted");
                        }
                    }
                });               
            }
            function showResult(){

            }

            function createColumn(elmntId,elementName,tarLoc){

                if(isMemberUseInOtherLevel=="true"){
                 alert("You can not drag this member,this Dimention Restricted in UserLevel")
                }else{
                   // alert("in elase")
                    var parentUL=document.getElementById(tarLoc);
                    var x=grpColArray.toString();
                    if(x.match(elmntId)==null){
                        grpColArray.push(elmntId);

                        var childLI=document.createElement("li");
                        childLI.id=elmntId+"_li";
                        childLI.style.width='180px';
                        childLI.style.height='auto';
                        childLI.style.color='white';
                        childLI.className='navtitle-hover';
                        var table=document.createElement("table");
                        table.id=elmntId+"_table";
                        var row=table.insertRow(0);
                        var cell1=row.insertCell(0);
                        //cell1.style.backgroundColor="#e6e6e6";
                        var a=document.createElement("a");
                        var deleteElement = elmntId;
                        a.href="javascript:deleteColumn('"+deleteElement+"')";
                        a.innerHTML="a";
                        a.className="ui-icon ui-icon-close";
                        cell1.appendChild(a);
                        var cell2=row.insertCell(1);
                        // cell2.style.backgroundColor="#e6e6e6";
                        cell2.style.color='black';
                        cell2.innerHTML=elementName;
                        childLI.appendChild(table);
                        parentUL.appendChild(childLI);
                    }else{
                        alert("Already assigned")
                    }

                    $(".sortable").sortable();
                /*$(".sortable").disableSelection();
                             */

                }
            }
            function deleteColumn(index)
            {
               
                var LiObj=document.getElementById(index+"_li");
                var parentUL=document.getElementById(LiObj.parentNode.id);
                parentUL.removeChild(LiObj);

                var i=0;
                for(i=0;i<grpColArray.length;i++){
                    if(grpColArray[i]==index)
                        grpColArray.splice(i,1);
                }
           

            }
            function saveDimMemBers(){
                $("#dimensionMembersDiv").dialog('close')
                var dimMembers=new Array
                var DimMember="";
                 $("#sortable li").each(function(){
                      DimMember= $(this).attr("id").replace("_li","", "gi")
                     dimMembers.push(DimMember)
                      createColumnruleMember(DimMember,DimMember,"sortableUL","DimMember");
                    
                        })
                       var rule=$("#rulesRegionShow").val()
                       rule+="("+dimMembers+")";
                       $("#rulesRegionShow").val(rule)
                       dimMembersArray.push(dimMembers)
                       dimMembersArray.push("~")
                       displayRuleArray.push(dimMembers)
            }
            function clearAll(){
                $("#rulesRegionShow").val("")
                 measureIds=new Array();
             dimensionIds=new Array();
             grpColArray=new Array();
            // ruleOn="";
             dimIDs=new Array()
            dimMembersArray=new Array();
            }
        </script>
    </body>
</html>
