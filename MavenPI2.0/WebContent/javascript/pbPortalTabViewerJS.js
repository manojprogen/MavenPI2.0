
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var swapping = false;
var count=0;
var kCounter=0;
var gCounter=0;
var varVal=0;
var rowCount = 0;
var tdCount = 0;
var PortalTabId=""
var PortLetId=""

var currentColumnNum=0;
var flagObj="false";
var selectedreportId="";
            var drillReportId="";
            var portlId="";
            var portltId="";
            var targetDivid="";
            var targetId="";
            var tval="";
            var elemntId="";
            var kpimasterId="";
            var reportId="";
            var editDiv = "";
            var tgtVal = "";
function displaylink(){
    $("#linkTable").toggle(500);
}
function dispParameters(){
    $("#tabParameters").toggle(500);
}
function dispPortlet(){
    $("#tabPortlet").toggle(500);
}
function Close(){

}
function addPortlet(portletId,portletName,portletDesc,portletTabId){

    var DivObj=document.getElementById("PortalColumn1_"+portletTabId);


    var str="";
    var innerTableStr="";
    if(DivObj!=null ){
        var ParentDiv = document.createElement('div');//main div
        ParentDiv.className = 'portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all';
        ParentDiv.id='portlet-'+portletId;
        ParentDiv.style.height="350px";
        ParentDiv.style.width="420px";

      //  var txtDiv = document.createElement('div');//header info div
       // txtDiv.className = 'portlet-header portletHeader ui-corner-all';

//        str=str+"<table  style='height:10px;width:100%'>";
//        str=str+"<tr valign='top' align='right'>";
//        str=str+"<td align=\"left\" style='color:#369;font-weight:bold'>";
//        //tableBuffer.append("<a href='javascript:void(0)' onclick=\"editPortletName('" + portletId + ",'"+ rs.getString(4)+"','"+ rs.getString(2)+"')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Edit Name\"><font size=\"1px\"><b>"+ rs.getString(2)+"</b> </font></a>");
//
//        str=str+"<a href='javascript:void(0)' onclick=\"editPortletName('" + portletId + "','"+ portletTabId+"','"+ portletName+"')\"  style='text-decoration:none' class=\"calcTitle\" title=\"Edit Name\"><font size=\"1px\"><b>"+ portletName+"</b> </font></a>";
//        //str=str+portletName;
//
//        str=str+"</td>&nbsp;&nbsp;";
//        str=str+"<td align=\"right\">";
//        str=str+"<table border='0'>";
//        str=str+"<tr><td>&nbsp;&nbsp;</td>";
//        str=str+"<td align='right'>";
//        str=str+"<a style='text-decoration:none' href='javascript:void(0)' onclick=\"openTablesDiv('Table-" + portletId + "')\" title='Table'><font size=\"1px\"><b>Table</font></a>";
//
//        str=str+"<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id ='Table-" + portletId + "'>";
//        str=str+"<Table>";
//        str=str+"<Tr valign='top'>";
//        str=str+"<Td valign='top'>";
//        str=str+"<a style='text-decoration:none' href=\"javascript:getPortletTableTemplate('" + portletId + "','" + portletName+ "','" + portletDesc + "','" + portletTabId + "')\" title='Standard table'><font size=\"1px\"><b>Standard Table</font></a>";
//        str=str+"</Td>";
//        str=str+"</Tr>";
//        str=str+"<Tr>";
//        str=str+"<Td>";
//        str=str+"<a style='text-decoration:none' href=\"javascript:getPortletKPITableTemplate('" + portletId + "','" + portletName + "','" + portletDesc + "','" +portletTabId + "')\" title='KPI Table'><font size=\"1px\"><b>KPI Table</font></a>";
//        str=str+"</Td>";
//        str=str+"</Tr>";
//        str=str+"</Table>";
//        str=str+" </div>";
//
//
//        str=str+"</td>&nbsp;";
//        str=str+"<td align='right'>";
//        str=str+"<a style='text-decoration:none' href='javascript:void(0)' onclick=\"openTablesDiv('Graph-" + portletId + "')\" title='Graph'><font size=\"1px\"><b>Graph</font></a>";
//
//        str=str+"<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id ='Graph-" + portletId + "'>";
//        str=str+"<Table>";
//        str=str+"<Tr valign='top'>";
//        str=str+"<Td valign='top'>";
//        str=str+"<a style='text-decoration:none' href=\"javascript:getPortletGraphTemplate('" + portletId + "','" + portletName + "','" + portletDesc + "','" + portletTabId + "')\" title='Standard Graph'><font size=\"1px\"><b>Standard Graph</font></a>";
//        str=str+"</Td>";
//        str=str+"</Tr>";
//        str=str+"<Tr>";
//        str=str+"<Td>";
//        str=str+"<a style='text-decoration:none' href=\"javascript:getPortletKPIGraphTemplate('" + portletId + "','" + portletName + "','" + portletDesc + "','" + portletTabId + "')\" title='KPI Graph'><font size=\"1px\"><b>KPI Graph</font></a>";
//        str=str+"</Td>";
//        str=str+"</Tr>";
//        str=str+"</Table>";
//        str=str+" </div>";
//
//        str=str+"</td>&nbsp";
//        str=str+"<td align='right'>";
//        str=str+"<a style='text-decoration:none' href=\"javascript:checkGedgets('"+portletId+"','"+portletName+"','"+portletDesc+"','"+portletTabId+"')\" title='Gadgets' ><font size=\"1px\"><b>Gadgets</b></font></a>";
//
//        str=str+"<div style='display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;border-top-width: 0px;' id='Gedget-" + portletId + "'>";
//        str=str+"<Table>";
//        str=str+"<Tr valign='top'>";
//        str=str+"<Td valign='top'>";
//        str=str+"<input type='radio' name='Gedget"+ portletId +"' id='Gedgetc"+ portletId +"' value='Clock'";
//        str=str+"<b><font size='1px'>Clock<font size='1px'></b>";
//        str=str+"</Td>";
//        str=str+"</Tr>";
//        str=str+"<Tr>";
//        str=str+"<Td>";
//        str=str+"<input type='radio' name='Gedget"+ portletId +"' id='Gedgetc"+ portletId +"' value='Calender'";
//        str=str+"<b><font size='1px'>Calender<font size='1px'></b>";
//        str=str+"</Td>";
//        str=str+"</Tr>";
//        str=str+"<Tr>";
//        str=str+"<Td>";
//        str=str+"<input type='radio' name='Gedget"+ portletId +"' id='Gedgetc"+ portletId +"' value='Msgbox'";
//        str=str+"<b><font size='1px'>Message Box<font size='1px'></b>";
//        str=str+"</Td>";
//        str=str+"</Tr>";
//        str=str+"</Table>";
//        str=str+" </div>";
//
//        str=str+"</td>&nbsp";
//        str=str+"<td align='right'>";
//        str=str+"<a style='text-decoration:none' href=\"javascript:deletePortlet('"+portletId+"','"+portletName+"','"+portletTabId+"')\" title='Delete '"+portletName+"' ><font size=\"1px\"><b>Delete</b></font></a>";
//        str=str+"</td>";
//
//        str=str+"</tr>";
//        str=str+"</table>";
//
//        str=str+"</td>";
//        str=str+"</tr>";
//        str=str+"</table>";

       // txtDiv.innerHTML = str


        var cntDiv = document.createElement('div');
        cntDiv.className = 'portlet-content';

        innerTableStr+="<script type='text/javascript'>getPortletDetails(portletId,'','','','',portletTabId)</script>";
        innerTableStr+="<table width=\"100%\">";
        innerTableStr+="<tr>";
        innerTableStr+="<td align='left'></td>";
        //innerTableStr+="<td align=\"right\"> </td>";
        innerTableStr+="</tr>";
        innerTableStr+="</table>";

        cntDiv.innerHTML = innerTableStr;
//        ParentDiv.appendChild(txtDiv);
        ParentDiv.appendChild(cntDiv);

        var childDivs=DivObj.getElementsByTagName("div");
        if(childDivs){
            DivObj.insertBefore(ParentDiv,  childDivs[0]);
        }
        else{
            DivObj.appendChild(ParentDiv);
        }
    }
    else{

    }
    cancelPortlet(portletTabId);

    $(".column").sortable({
        connectWith: '.column'
    });

    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
    .find(".portlet-header")
    .addClass("portletHeader ui-corner-all")
    .end()
    .find(".portlet-content");

    $(".portlet-header .ui-icon").click(function() {
        $(this).toggleClass();
        $(this).parents(".portlet:first").find(".portlet-content").toggle();
    });

    $(".column").disableSelection();
    changePortletsOrder(portletTabId);


}
function goPortlet(crtPortId){
    //var portName="portletName"+crtPortId;
    //var portletDesc="portletDesc"+crtPortId
//    document.getElementById("portletName"+crtPortId).value="";
//    document.getElementById("portletDesc"+crtPortId).value="";
//    var divId="#portlet"+crtPortId;
//    $(divId).dialog({
//        autoOpen: false,
//        height: 180,
//        width: 350,
//        position: 'justify',
//        modal: true
//    });
//    $("#portlet"+crtPortId).dialog('open')
//alert(crtPortId)
parent.addPortlesfromExt(crtPortId)
}

function deleteEmptyPortlet(crtPortId){
    parent.deletePortlesfromExt(crtPortId)
}

function cancelPortlet(crtPortId){
    var divId="#portlet"+crtPortId;


    $(divId).dialog('close')
//    document.getElementById("portlet"+crtPortId).style.display='none';
//    document.getElementById('fade').style.display='none';
}
function checkGedgets(portletId,portletName,portletDesc,portletTabId){
    var portlet='Gedget-'+portletId;
    var optionchk='Gedget'+portletId;
    var divId=document.getElementById(portlet);
    if(divId.style.display=='none'){
        divId.style.display='';
    }
    else{
        divId.style.display='none';
        var val="";
        var startVar = document.getElementsByName(optionchk);
        var count=0;
        for(var i=0;i<startVar.length;i++){
            if(startVar[i].checked){
                count=1;
                val= startVar[i].value;
                break;
            }else{
        }
        }
        if(count==0){


        }else{
            addPortletGadgets(portletId,portletName,portletDesc,portletTabId,val)
        }
    }
}

function savePortlet(currentTabId){
    var portletNameObj="portletName"+currentTabId;
    var portletDescObj="portletDesc"+currentTabId
    //var sharableObj="sharable"+currentTabId

    var portletName = document.getElementById(portletNameObj).value;
    var portletDesc = document.getElementById(portletDescObj).value;
    var sharable ="N";
    //var sharable = document.getElementById(sharableObj).value;
    var portletId="";
    if(trim(portletName)!=""){
    $.ajax({
        url: 'portalTemplateAction.do?paramportal=insertPortalDetails&portalTabId='+currentTabId+'&ColNum=1&SeqNum=1&portletName='+portletName+'&portletDesc='+portletDesc+'&sharable='+sharable,
        success: function(data){
            portletId=data;
            addPortlet(portletId,portletName,portletDesc,currentTabId);
        }
    });

}else{
    alert("please enter portlet name")
}

}


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
function tabmsgportlet(crtPortId){

    document.getElementById("portletDesc"+crtPortId).value = document.getElementById("portletName"+crtPortId).value;
}
function addGraph(){

    var tableObj = document.getElementById("tablePortlet");
    var tdObjs = tableObj.getElementsByTagName("td");
    var tdNum = parseInt(tdObjs.length);
    if(parseInt(tdNum%2)==0)
    {
        var newRow = tableObj.insertRow(rowCount);
        newRow.id = "rowPortlet"+rowCount;
        var cell = newRow.insertCell(0);
        var inDivG = document.createElement('div');
        inDivG.className = 'portlet';
        var txtDivG = document.createElement('div');
        txtDivG.className = 'portlet-header ';
        txtDivG.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graphregion</td><td align="right"> <a href="#"  onclick="Close()"><img title="close" width="16px" height="16px" border="none" src="<%=request.getContextPath()%>/icons pinvoke/cross.png"/></a></td></tr></table>'
        var cntDivG = document.createElement('div');
        cntDivG.className = 'portlet-content';
        cntDivG.innerHTML = 'this is Graph region'
        inDivG.appendChild(txtDivG);
        inDivG.appendChild(cntDivG);
        cell.appendChild(inDivG);
        newRow.appendChild(cell);
        tdCount++;
        cancelPortlet();
    }
    else if(parseInt((tdNum%2))!=0)
    {
        var rowId = document.getElementById("rowPortlet"+rowCount);
        cell = rowId.insertCell(0);
        var ParentDiv = document.createElement('div');
        ParentDiv.className = 'portlet';
        var txtDiv = document.createElement('div');
        txtDiv.className = 'portlet-header';
        txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">Graphregion</td><td align="right"> <a href="#"  onclick="Close()"><img title="close" width="16px" height="16px" border="none" src="<%=request.getContextPath()%>/icons pinvoke/cross.png"/></a></td></tr></table>'
        var cntDiv = document.createElement('div');
        cntDiv.className = 'portlet-content';
        cntDiv.innerHTML = 'this is Graph region'
        ParentDiv.appendChild(txtDiv);
        ParentDiv.appendChild(cntDiv);
        cell.appendChild(ParentDiv);

        rowCount++;
        cancelPortlet();

    }
    $(".column").sortable({
        connectWith: '.column'
    });

    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
    .find(".portlet-header")
    .addClass("portletHeader ui-corner-all")
    .end()
    .find(".portlet-content");

    $(".portlet-header .ui-icon").click(function() {
        $(this).toggleClass();
        $(this).parents(".portlet:first").find(".portlet-content").toggle();
    });

    $(".column").disableSelection();

}

function addPortletTable(){


}
function addPortletGraph(){

}
function addPortletGadgets(portletId,portletName,portletDesc,portletTabId,xmlstr){

    var portlet="portlet-"+portletId;
    document.getElementById(portlet).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>';
    $.ajax({
        url: 'portalViewer.do?portalBy=viewPortletGadgets&PORTLETID='+portletId+'&portletName='+portletName+'&portletTabId='+portletTabId+'&xmlstr='+xmlstr,
        success: function(data){
            var str="<div class=\"portlet-header portletHeader ui-corner-all\">"//header div starts
            str+="<table style='height:10px'>"//header table
            str+=" <tr valign='top'>"
            str+=" <td valign='top' style='color:#369;font-weight:bold'>"
            str+=portletName
            str+="</Td>"
            str+="</Tr>"
            str+="</table>"
            str+="</div>"
            str+="<div style='width:420px;height:350px;overflow:auto'>"
            str+="<table height='100%'>"
            str+=" <tr valign='top'>"
            str+="<td valign='top' style='color:#369;font-weight:bold'>"
//               alert("portletId\t"+portletId)
            str+="<iframe id='iframe-90' frameborder=0 height=325px width=410px src=pbExternalPortlet.jsp?portletId="+portletId+">"
            str+=" </td>"
            str+=" <td valign='top' align=\"right\"> </td>"
            str+="</tr>"
            str+="</table>"
            str+="</div>"
            str+="</Table>"
            str+=" </div>"

            document.getElementById(portlet).innerHTML=str;
        }
    });

}
function getPortletGraphTemplate(portletId,portletName,portletDesc,portalTabId){

    window.open("portalViewer.do?portalBy=goToPortletGraph&portletName="+portletName+"&portletDesc="+portletDesc+"&portletId="+portletId+"&portalTabId="+portalTabId,"Add Graph", "scrollbars=1,width=1200,height=500,address=no");
//var frameObj=document.getElementById("portletGraphFrame");
//var source="portalViewer.do?portalBy=goToPortletGraph&portletName="+portletName+"&portletDesc="+portletDesc+"&portletId="+portletId+"&portalTabId="+portalTabId;
//frameObj.src=source;
//frameObj.style.display='block';
//document.getElementById('fade').style.display='block';
}

function getPortletTableTemplate(portletId,portletName,portletDesc,portalTabId){

    window.open("portalViewer.do?portalBy=goToPortletTable&portletName="+portletName+"&portletDesc="+portletDesc+"&portletId="+portletId+"&portalTabId="+portalTabId,"Add Table", "scrollbars=1,width=1200,height=500,address=no");
//var frameObj=document.getElementById("portletTableFrame");
//var source="portalViewer.do?portalBy=goToPortletTable&portletName="+portletName+"&portletDesc="+portletDesc+"&portletId="+portletId+"&portalTabId="+portalTabId

//frameObj.src=source;
//frameObj.style.display='block';
//document.getElementById('fade').style.display='block';
}
function cancelPortletGraphTemplate(portletId,REP,CEP){
    //document.getElementById('fade').style.display='none';
    //document.getElementById('portletGraphFrame').style.display='none';
    //document.getElementById("portlet-"+portletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>';

    getPortletDetails(portletId, REP, CEP,'','');
}
function cancelPortletTableTemplate(portletId,REP,CEP){
    //document.getElementById('fade').style.display='none';
    //document.getElementById('portletTableFrame').style.display='none';
    //document.getElementById("portlet-"+portletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>';
    getPortletDetails(portletId, REP, CEP,'','');

}
function getPortletDetails(portletId,REP,CEP,perBy,gpType,portalTabId){

       document.getElementById('portlet-'+portalTabId+'-'+portletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>';
   $.ajax({
        url: 'portalViewer.do?portalBy=viewPortlet&PORTLETID='+portletId+'&REP='+REP+'&CEP='+CEP+'&perBy='+perBy+'&gpType='+gpType+'&portalTabId='+portalTabId,
        success: function(data){
            document.getElementById("portlet-"+portalTabId+"-"+portletId).innerHTML=data;
            var id="tablesorter"+portletId + "-" + portalTabId;
            $("#"+id).tablesorter({});
//            $("#tablesorter"+portletId + "-" + portalTabId).columnFilters().tablesorter({
//                widthFixed: true,
//                widgets: ['zebra']
//            }).tablesorterPager({
//                container: $("#pager"+portletId)
//            });
        }
    });
//setTimeout(dispdata(portletId,REP,CEP,perBy,gpType,portalTabId),'100000'); to refresh for every 10 mins
}

function getParentPortletDetails(divId,REP,CEP){

    parent.document.getElementById(divId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>';
    $.ajax({
        url: 'portalViewer.do?portalBy=viewPortlet&PORTLETID='+portletId+'&REP='+REP+'&CEP='+CEP,
        success: function(data){
            parent.document.getElementById(divId).innerHTML=data;

            $(".column").sortable({
                connectWith: '.column'
            });

            $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
            .find(".portlet-header")
            .addClass("portletHeader ui-corner-all")
            .end()
            .find(".portlet-content");

            $(".portlet-header .ui-icon").click(function() {
                $(this).toggleClass();
                $(this).parents(".portlet:first").find(".portlet-content").toggle();
            });

            $(".column").disableSelection();


        }
    });
}

function openREPs(dispgrptypObj){
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
}

function openCEPs(dispgrptypObj){
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
}

function openPreviews(portletId,REPId,CEPId){
    var rowEdgeParams="";
    var colEdgeParams="";
    var REPNames="";

    var rowParamIdObj=REPId;
    for(var i=0;i<rowParamIdObj.length;i++){
        if(rowParamIdObj[i].checked){
            rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
            REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
        }
    }
    if(rowEdgeParams!=""){
        rowEdgeParams=rowEdgeParams.substring(1);
        REPNames=REPNames.substring(1);
    }
    getPortletDetails(portletId, rowEdgeParams, colEdgeParams);
}

$(function() {
    $(".column").sortable({
        connectWith: '.column'
    });

    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
    .find(".portlet-header")
    .addClass("portletHeader ui-corner-all")
    .prepend('<span class="ui-icon ui-icon-plusthick"></span>')
    .end()
    .find(".portlet-content");

    $(".portlet-header .ui-icon").click(function() {
        $(this).toggleClass("ui-icon-minusthick");
        $(this).parents(".portlet:first").find(".portlet-content").toggle();
    });

    $(".column").disableSelection();

});

function openREPPreviews(portletId,REPId,dispgrptypObj,perBy,gpType,portalTabId,date){
    var openREPPreviews = "openREPPreviews";
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
        var rowEdgeParams="";
        var colEdgeParams="";
        var REPNames="";
        var rowParamIdObj=REPId;
        for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,openREPPreviews);

    }

}
function getNewPortLetName(){
    var nameofNewPortlet=prompt("Please enter Portlet Name","new Portlet");
     return nameofNewPortlet
}


function saveXmalOfPortlet(portletId,REPId,dispgrptypObj,perBy,gpType,portalTabId,date){
    portaltabId=portalTabId
    var saveXmalOfPortlet = "saveXmalOfPortlet";
    var divName= "saveXmalOfPortlet"+portletId+"-" +portalTabId
     var nameofNewPortlet="";
    if(document.getElementById(divName).style.display=='none'){
        document.getElementById(divName).style.display=''
    }else{
         document.getElementById(divName).style.display='none'
        var saveStatus="";
        var selectOption=document.getElementsByName('saveRediooption'+portletId+'-'+ portalTabId)
        for(var i=0;i<selectOption.length;i++){
         if (selectOption[i].checked == true)
             saveStatus=selectOption[i].value
    }

    if(trim(saveStatus)=="saveAsNew")
        nameofNewPortlet= getNewPortLetName()
   // alert("nameofNewPortlet\t"+nameofNewPortlet)

        var rowEdgeParams="";
        var colEdgeParams="";
        var rowParamIdObj=REPId;
        var columnParmObject=''
        var CEPNames=''
        var REPNames=''
        if(rowParamIdObj!=null){
       //alert("in if")
         for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
//        if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+portletId+"-"+portalTabId)
          //  alert("columnParmObject\t"+columnParmObject.length)
            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }
             if(colEdgeParams!=""){
            colEdgeParams=colEdgeParams.substring(1);
            CEPNames=CEPNames.substring(1);
        }

//        }
        }
//        alert("gpType\t"+gpType)
//        alert("rowEdgeParams\t"+rowEdgeParams)
//        alert("colEdgeParams\t"+colEdgeParams)
//        alert("CEPNames"+CEPNames)
//        alert("REPNames"+REPNames)
        // alert("nameofNewPortlet---"+nameofNewPortlet+"----")
     if(trim(saveStatus)!="saveAsNew" ||nameofNewPortlet!=null)
        $.post('portalViewer.do?portalBy=saveXmalOfPortlet&portletId='+portletId+'&portalTabId='+portalTabId+'&rowEdgeParams='+rowEdgeParams+'&colEdgeParams='+colEdgeParams+'&gpType='+gpType+'&saveStatus='+saveStatus+'&nameofNewPortlet='+nameofNewPortlet,function(data){
                //alert("data\t"+data)
                if(data=="true"){
                     getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,saveXmalOfPortlet);
                 }else{
                     var datavar=data.split("~")
                       document.forms.frmParameter.action="pbPortalViewer.jsp#"+trim(datavar[1].replace(" ","_","gi"))
                       document.forms.frmParameter.submit()
                 }

            });
                }
}

function openCEPPreviews(portletId,CEPId,dispgrptypObj,perBy,gpType,portalTabId,date){
    var openCEPPreviews = "openCEPPreviews";
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
        var rowEdgeParams="";
        var colEdgeParams="";
        var CEPNames="";
        var ColParamIdObj=CEPId;
        for(var i=0;i<ColParamIdObj.length;i++){
            if(ColParamIdObj[i].checked){
                colEdgeParams=colEdgeParams+","+ColParamIdObj[i].value.split("~")[0];
                CEPNames=CEPNames+","+ColParamIdObj[i].value.split("~")[1];
            }
        }
        if(colEdgeParams!=""){
            colEdgeParams=colEdgeParams.substring(1);
            CEPNames=CEPNames.substring(1);
        }
        getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,openCEPPreviews);
    }

}

function openTableREPPreviews(portletId,REPId,CEPId,dispgrptypObj,perBy,gpType,portalTabId,date){
    var openTableREPPreviews = "openTableREPPreviews";
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
        var rowEdgeParams="";
        var colEdgeParams="";
        //var REPNames="";
        var rowParamIdObj=REPId;
        for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
            //REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        var ColParamIdObj=CEPId;
        for( i=0;i<ColParamIdObj.length;i++){
            if(ColParamIdObj[i].checked){
                colEdgeParams=colEdgeParams+","+ColParamIdObj[i].value.split("~")[0];
            //CEPNames=CEPNames+","+ColParamIdObj[i].value.split("~")[1];
            }
        }
        if(colEdgeParams!=""){
            colEdgeParams=colEdgeParams.substring(1);
        //CEPNames=CEPNames.substring(1);
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
        //REPNames=REPNames.substring(1);
        }
        getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,openTableREPPreviews);

    }

}

function openTableCEPPreviews(portletId,REPId,CEPId,dispgrptypObj,perBy,gpType,portalTabId,date){
    var openTableCEPPreviews = "openTableCEPPreviews";
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
        var rowEdgeParams="";
        var colEdgeParams="";
        //var REPNames="";
        var rowParamIdObj=REPId;
        for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
            //REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        var ColParamIdObj=CEPId;
        for( i=0;i<ColParamIdObj.length;i++){
            if(ColParamIdObj[i].checked){
                colEdgeParams=colEdgeParams+","+ColParamIdObj[i].value.split("~")[0];
            //CEPNames=CEPNames+","+ColParamIdObj[i].value.split("~")[1];
            }
        }
        if(colEdgeParams!=""){
            colEdgeParams=colEdgeParams.substring(1);
        //CEPNames=CEPNames.substring(1);
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
        //REPNames=REPNames.substring(1);
        }
        getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,openTableCEPPreviews);

    }

}

function opentopbottomDisplay(dispgrptypObj){
    //alert("11")
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
}
    else{
        dispgrptypObj.style.display='none';
}
}
function opentopbottomPreviews(PortletId,REPId,dispgrptypObj,perBy,gpType,portalTabId,date){
         var checkval="false"
         var applySortOnTable = "applySortOnTable";
     var sortAll=document.getElementsByName("sortCheckbox"+PortletId+"-"+portalTabId);
      for(var i=0;i<sortAll.length;i++){
             if(sortAll[i].checked){
          checkval=sortAll[i].value

                        }
                    }

        var DivId='TopBottom'+PortletId+'-'+portalTabId;       
        var countVal=$("#sortTextbox"+PortletId+"-"+portalTabId).val();
        var sortByColumeVal=$("#sortBy"+PortletId+"-"+portalTabId).val();
        var sort='sortTable'+PortletId+"-"+portalTabId;
        var sortType=$("input[name="+sort+"]:checked").val();

       if(document.getElementById(DivId).style.display=='none')
        {
//             alert("DivId="+DivId)
         document.getElementById(DivId).style.display='';
        }else
            {
              document.getElementById(DivId).style.display='none';
           if(jQuery.trim(countVal)!=""||checkval=="true")
               {
               $.ajax({
                url: 'portalViewer.do?portalBy=applySortOnTable&countVal='+countVal+'&sortByColumeVal='+sortByColumeVal+'&sortType='+sortType+'&PortletId='+PortletId+'&portalTabId='+portalTabId+'&checkval='+checkval,
                success:function(data){
    var rowEdgeParams="";
    var colEdgeParams="";
    var REPNames="";
    var columnParmObject=''
    var rowParamIdObj=REPId;
    for(var i=0;i<rowParamIdObj.length;i++){
        if(rowParamIdObj[i].checked){
            rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
            REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
        }
    }
    if(rowEdgeParams!=""){
        rowEdgeParams=rowEdgeParams.substring(1);
        REPNames=REPNames.substring(1);
    }
if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+PortletId+"-"+portalTabId)

            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }

        }
    getPortletDetails(PortletId, rowEdgeParams, colEdgeParams,perBy, gpType,portalTabId,date,applySortOnTable);
  }} );
               }
}
}
function openGrpTypeDisplay(dispgrptypObj){
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
}
function openGrpTypePreviews(portletId,REPId,perBy,gpType,portletTabId,date){
    var openGrpTypePreviews = "openGrpTypePreviews";
    var rowEdgeParams="";
    var colEdgeParams="";
    var REPNames="";
    var rowParamIdObj=REPId;
    for(var i=0;i<rowParamIdObj.length;i++){
        if(rowParamIdObj[i].checked){
            rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
            REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
        }
    }
    if(rowEdgeParams!=""){
        rowEdgeParams=rowEdgeParams.substring(1);
        REPNames=REPNames.substring(1);
    }
    getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portletTabId,date,openGrpTypePreviews);

}
function deletePortlet(portletId,portletName,portletTabId){
    var res=confirm('Do you want to delete '+portletName);
    if(res){
        var DivObj=document.getElementById('portlet-'+portletTabId+'-'+portletId);
        var PortalColumn1Obj=document.getElementById("PortalColumn1_"+portletTabId);
        var PortalColumn2Obj=document.getElementById("PortalColumn2_"+portletTabId);
        var PortalColumn3Obj=document.getElementById("PortalColumn3_"+portletTabId);

        var childDivs1=PortalColumn1Obj.getElementsByTagName("div");
        var childDivs2=PortalColumn2Obj.getElementsByTagName("div");
        var childDivs3=PortalColumn3Obj.getElementsByTagName("div");

        if(DivObj!=null){
            $.ajax({
                url: 'portalViewer.do?portalBy=deletePortlet&portletId='+portletId+'&portalTabId='+portletTabId,
                success: function(data){
                    if(data=='true'){
                        if(childDivs1!=null){
                            for(var i=0;i<childDivs1.length;i++){
                                if(childDivs1[i].id==DivObj.id){
                                    PortalColumn1Obj.removeChild(DivObj);
                                }
                            }
                        }
                        if(childDivs2!=null){
                            for( i=0;i<childDivs2.length;i++){
                                if(childDivs2[i].id==DivObj.id){
                                    PortalColumn2Obj.removeChild(DivObj);
                                }
                            }
                        }
                        if(childDivs3!=null){
                            for( i=0;i<childDivs3.length;i++){
                                if(childDivs3[i].id==DivObj.id){
                                    PortalColumn3Obj.removeChild(DivObj);
                                }
                            }
                        }
                        alert(portletName +" Deleted Successfully");
                    }
                    else{
                        alert("Failed in Deleting "+portletName);
                    }
                }
            });
        }
    }else{

}
}
function openTablesDiv(tableDivId){
    var divId=document.getElementById(tableDivId);
    if(divId.style.display=='none'){
        divId.style.display='';
    }
    else{
        divId.style.display='none';
    }
}
function openGraphsDiv(graphDivId){
    var divId=document.getElementById(graphDivId);
    if(divId.style.display=='none'){
        divId.style.display='';
    }
    else{
        divId.style.display='none';
    }
}
function getPortletKPIGraphTemplate(portletId,portletName,portletDesc,portalTabId){
    var divId=document.getElementById("Graph-"+portletId);
    divId.style.display='none';
    window.open("portalViewer.do?portalBy=goToPortletKPIGraph&portletName="+portletName+"&portletDesc="+portletDesc+"&portletId="+portletId+"&portalTabId="+portalTabId,"Add Graph", "scrollbars=1,width=1200,height=500,address=no");
}

function getPortletKPITableTemplate(portletId,portletName,portletDesc,portalTabId){
    var divId=document.getElementById("Table-"+portletId);
    divId.style.display='none';
    window.open("portalViewer.do?portalBy=goToPortletKPITable&portletName="+portletName+"&portletDesc="+portletDesc+"&portletId="+portletId+"&portalTabId="+portalTabId,"Add Table", "scrollbars=1,width=1200,height=500,address=no");
}
function cancelPortletKPIGraphTemplate(portletId,REP,CEP){
    getPortletDetails(portletId, REP, CEP,'','');
}
function cancelPortletKPPITableTemplate(portletId,REP,CEP){
    getPortletDetails(portletId, REP, CEP,'','');

}
function openkpiGrpTypeDisplay(dispgrptypObj){
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
}
function openkpiGrpTypePreviews(portletId,REPId,perBy,gpType,portlaid){
    var rowEdgeParams="";
    var colEdgeParams="";
    getPortletDetails(portletId, rowEdgeParams, colEdgeParams, perBy, gpType, portlaid, undefined)
}
function editPortletName(PortletId,portalTabId,PortletName){
    document.getElementById("editportletName"+portalTabId).value=PortletName;
    document.getElementById("editportletDesc"+portalTabId).value=PortletName;
    document.getElementById("currportletId").value=PortletId;
    var divId="#editportlet"+portalTabId;
    $(divId).dialog({
        autoOpen: false,
        height: 180,
        width: 350,
        position: 'justify',
        modal: true
    });
    //$(divId).dialog('open');
    $("#editportlet"+portalTabId).dialog('open');
}

//function openFilter(PortletID){
//    var divId="#rulesDiv"+PortletID;
//     $(divId).dialog({
//        autoOpen: false,
//        height: 180,
//        width: 350,
//        position: 'justify',
//        modal: true
//    });
//      $("#rulesDiv"+PortletID).dialog('open')
//    alert("PortletID\t"+PortletID)
////      $("#filterFrame").attr("src","ruleHelp.jsp?fromModule=PORTAL&portletId="+PortletID)
//
//}

function edittabmsgportlet(portalTabId){
    document.getElementById("editportletDesc"+portalTabId).value = document.getElementById("editportletName"+portalTabId).value;
}
function cancelEditPortlet(crtPortId){
    document.getElementById("editportlet"+crtPortId).style.display='none';
    document.getElementById('fade').style.display='none';

    document.getElementById("editportletName"+portalTabId).value="";
    document.getElementById("editportletDesc"+portalTabId).value="";
}
function updatePortletName(portalTabId,date){
    var updatePortletName = "updatePortletName";
    var newname=document.getElementById("editportletName"+portalTabId).value
    $.ajax({
        url: 'portalViewer.do?portalBy=updatePortletName&portletId='+document.getElementById("currportletId").value+'&portalTabId='+portalTabId+'&portletName='+document.getElementById("editportletName"+portalTabId).value+'&portletDesc='+document.getElementById("editportletDesc"+portalTabId).value,
        success: function(data){
           // alert(data)
            var rowEdgeParams="";
            var colEdgeParams="";

            var rowParamIdObj=document.getElementsByName("chkREP-"+document.getElementById("currportletId").value);
            var ColParamIdObj=document.getElementsByName("chkCEP-"+document.getElementById("currportletId").value);
            if(rowParamIdObj!=null){
                for(var i=0;i<rowParamIdObj.length;i++){
                    if(rowParamIdObj[i].checked){
                        rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                    }
                }
                if(rowEdgeParams!=""){
                    rowEdgeParams=rowEdgeParams.substring(1);
                }
            }
            if(ColParamIdObj!=null){
                for( i=0;i<ColParamIdObj.length;i++){
                    if(ColParamIdObj[i].checked){
                        colEdgeParams=colEdgeParams+","+ColParamIdObj[i].value.split("~")[0];
                    }
                }
                if(colEdgeParams!=""){
                    colEdgeParams=colEdgeParams.substring(1);
                }
            }            
            getPortletDetails(document.getElementById("currportletId").value, rowEdgeParams, colEdgeParams,'','',portalTabId,date,'',updatePortletName);
            $(portalTabId+"_span > b").html(newname);
        }
    });
//    document.getElementById("editportlet"+portalTabId).style.display='none';
//    document.getElementById('fade').style.display='none';
    $("#editportlet"+portalTabId).dialog('close');
}

//added for drill in portal
function getPortletDetailsforDrill(path,portletId,portalTabId){
//    alert("path\t"+path)
//    modified by anitha
//    document.getElementById('portlet-'+portalTabId+'-'+portletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>';
//   $.ajax({
//        url: path,
//        success: function(data){
//            document.getElementById('portlet-'+portalTabId+'-'+portletId).innerHTML=data;
            var drillid = document.getElementById('portlet-'+portalTabId+'-'+portletId);

        //drillid.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle" width="75px" height="75px" style="position:relative;top:120px;left:30px" /></center>';
        
        $(drillid).html('<center><img id="imgId" src="images/ajax.gif" align="middle" width="75px" height="75px" style="position:relative;top:120px;left:30px" /></center>');

        $.ajax({
        url: path,
        success: function(data){
                $(drillid).html(data);
//            $("#tablesorter"+portletId).columnFilters().tablesorter({
//                widthFixed: true,
//                widgets: ['zebra']
//            }).tablesorterPager({
//                container: $("#pager"+portletId)
//            });
        }
    });
//setTimeout(dispdata(portletId,REP,CEP,perBy,gpType,portalTabId),'100000'); to refresh for every 10 mins
}

function goDate(currDate){
    //var portName="portletName"+crtPortId;
    //var portletDesc="portletDesc"+crtPortId
//    document.getElementById("portletName"+crtPortId).value="";
//    document.getElementById("portletDesc"+crtPortId).value="";

    $("#custDate").datepicker({
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        numberOfMonths: 1,
        stepMonths: 1
    });
    $("#custDate").dialog('open');
}

function shwDate(){
    $('#datepicker').datepicker({
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        numberOfMonths: 1,
        stepMonths: 1
    });
}

function copyDate(eleId){
    var hideneleId = eleId.substring(0, eleId.length-1);
    var frntEndVal = document.getElementById(eleId).value;
    //alert(frntEndVal);
    var ddval = frntEndVal.substr(0, frntEndVal.indexOf("/"));
    //alert(ddval);
    frntEndVal = frntEndVal.substr(frntEndVal.indexOf("/")+1,frntEndVal.length);
    //alert(frntEndVal);
    var mmval = frntEndVal.substr(0, frntEndVal.indexOf("/"));
    // alert(mmval);
    frntEndVal = frntEndVal.substr(frntEndVal.indexOf("/")+1,frntEndVal.length);
    //alert(frntEndVal);
    var yearval =frntEndVal;
    //alert(yearval);
    var bckndDate = mmval+"/"+ddval+"/"+yearval;
    //alert(bckndDate);
    document.getElementById(hideneleId).value = bckndDate;
    //alert(document.getElementById(hideneleId).value);
}

function resetPortlet(portletId,REP,CEP,perBy,gpType,portalTabId,date,sortByColumeVal){
    REP=REP.replace("[", "").replace("]", "");
    CEP=CEP.replace("[", "").replace("]", "");
    var sortType = perBy.split("-")[0];
    var countVal = perBy.split("-")[1];
    var checkval = false;
    var resetGraphPortlet = "resetGraphPortlet";
    var resetId = document.getElementById('portlet-'+portalTabId+'-'+portletId);
    if(resetId!=null){
    $(resetId).html('<center><img id="imgId" src="images/ajax.gif" align="middle" width="75px" height="75px" style="position:relative;top:120px;left:30px"/></center>');
//if(document.getElementById('portlet-'+portalTabId+'-'+portletId)!=null){
//    document.getElementById('portlet-'+portalTabId+'-'+portletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>';
   $.ajax({
        url: 'portalViewer.do?portalBy=resetGraphPortlet&countVal='+countVal+'&sortByColumeVal='+sortByColumeVal+'&sortType='+sortType+'&PortletId='+portletId+'&portalTabId='+portalTabId+'&checkval='+checkval+'&gpType='+gpType,
        success: function(data){
//            document.getElementById('portlet-'+portalTabId+'-'+portletId).innerHTML=data;
//            var id="tablesorter"+portletId + "-" + portalTabId;
//            $("#"+id).tablesorter({});
            var rowEdgeParams="";
        var colEdgeParams="";
        var rowParamIdObj=REP;
        var columnParmObject=''
        var CEPNames=''
        var REPNames=''
        if(rowParamIdObj!=null){

         for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+PortletId+"-"+portalTabId)

            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }

        }
        }

        getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,resetGraphPortlet);
//            $("#tablesorter"+portletId + "-" + portalTabId).columnFilters().tablesorter({
//                widthFixed: true,
//                widgets: ['zebra']
//            }).tablesorterPager({
//                container: $("#pager"+portletId)
//            });
        }
    });
}else{
    //alert("else")
       $("#portletPreviewDivinDesiner").html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>');
        $.ajax({
        url: 'portalViewer.do?portalBy=resetGraphPortlet&countVal='+countVal+'&sortByColumeVal='+sortByColumeVal+'&sortType='+sortType+'&PortletId='+portletId+'&portalTabId='+portalTabId+'&checkval='+checkval,
        success: function(data){
//            $("#portletPreviewDivinDesiner").html(data);
//            var id="tablesorter"+portletId + "-" + portalTabId;
//            $("#"+id).tablesorter({});
            var rowEdgeParams="";
        var colEdgeParams="";
        var rowParamIdObj=REP;
        var columnParmObject=''
        var CEPNames=''
        var REPNames=''
        if(rowParamIdObj!=null){

         for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+PortletId+"-"+portalTabId)

            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }

        }
        }


        getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,resetGraphPortlet);
//            $("#tablesorter"+portletId + "-" + portalTabId).columnFilters().tablesorter({
//                widthFixed: true,
//                widgets: ['zebra']
//            }).tablesorterPager({
//                container: $("#pager"+portletId)
//            });
        }
    });
    }
//setTimeout(dispdata(portletId,REP,CEP,perBy,gpType,portalTabId),'100000'); to refresh for every 10 mins
}

function goPeriod(){
    var tabDiv=document.getElementById("periodDiv");
    if(tabDiv.style.display=='none'){
        tabDiv.innerHTML="";
        tabDiv.style.display='block';
        var divTable=document.createElement("table");
        var row=divTable.insertRow(0);
        var cell1=row.insertCell(0);
        var aelemnt1=document.createElement("a");
        aelemnt1.id="periodId-Date";
        aelemnt1.name="periodId-Date";
        aelemnt1.href="javascript:goPeriodbyDate()";
        aelemnt1.innerHTML="Date";
        aelemnt1.style.textDecoration="none";
        cell1.appendChild(aelemnt1);
        var row=divTable.insertRow(1);
        var cell2=row.insertCell(0);
        var aelemnt2=document.createElement("a");
        aelemnt2.id="periodId-Month";
        aelemnt2.name="periodId-Month";
        aelemnt2.href="javascript:goPeriodbyMonth()";
        aelemnt2.innerHTML='Month';
        aelemnt2.style.textDecoration="none";
        cell2.appendChild(aelemnt2);
        var row=divTable.insertRow(2);
        var cell3=row.insertCell(0);
        var aelemnt3=document.createElement("a");
        aelemnt3.id="periodId-Qtr";
        aelemnt3.name="periodId-Qtr";
        aelemnt3.href="javascript:goPeriodbyQtr()";
        aelemnt3.innerHTML='Quarter';
        aelemnt3.style.textDecoration="none";
        cell3.appendChild(aelemnt3);
        var row=divTable.insertRow(3);
        var cell4=row.insertCell(0);
        var aelemnt4=document.createElement("a");
        aelemnt4.id="periodId-Year";
        aelemnt4.name="periodId-Year";
        aelemnt4.href="javascript:goPeriodbyYear()";
        aelemnt4.innerHTML='Year';
        aelemnt4.style.textDecoration="none";
        cell4.appendChild(aelemnt4);
        tabDiv.appendChild(divTable);
    } else{
        tabDiv.style.display='none';
    }
}

function goPeriodbyDate(){
    var tabDiv=document.getElementById("periodDiv");
    if(tabDiv.style.display=='none'){
        tabDiv.style.display='block';
    }else{
        tabDiv.style.display='none';
    }
    $.ajax({
        url: 'portalTemplateAction.do?paramportal=checkUserPortalExist',
        success: function(data){
            if(data!=""){
                var portallist=data.split("~");
                var portalname=portallist[0];
                var portalId=portallist[1];

                parent.document.forms.frmParameter.action='portalViewer.do?portalBy=viewPortal&PORTALID='+portalId+'&PORTALNAME='+portalname;
                parent.document.forms.frmParameter.submit();
            }
            else if(data==''){
                document.getElementById('portal').style.display='block';
                document.getElementById('fade').style.display='block';
            }
        }
    });
}
function goPeriodbyMonth(){
    var tabDiv=document.getElementById("periodDiv");
    if(tabDiv.style.display=='none'){
        tabDiv.style.display='block';
    }else{
        tabDiv.style.display='none';
    }
}
function goPeriodbyQtr(){
    var tabDiv=document.getElementById("periodDiv");
    if(tabDiv.style.display=='none'){
        tabDiv.style.display='block';
    }else{
        tabDiv.style.display='none';
    }
}
function goPeriodbyYear(){
    var tabDiv=document.getElementById("periodDiv");
    if(tabDiv.style.display=='none'){
        tabDiv.style.display='block';
    }else{
        tabDiv.style.display='none';
    }
}

function PortletGrpTable(portletId,REP,CEP,perBy,gpType,portalTabId,date){
    REP=REP.replace("[", "").replace("]", "").replace(" ","", "gi");
    CEP=CEP.replace("[", "").replace("]", "");
    getPortletDetails(portletId, REP, CEP, perBy, gpType, portalTabId,date,"PortletGrpTable");
}

function getPortletDetails(portletId,REP,CEP,perBy,gpType,portalTabId,date,periodType,dateCheck){
    //alert(dateCheck)   
    if(document.getElementById('portlet-'+portalTabId+'-'+portletId)!=null){
    $('#portlet-'+portalTabId+'-'+portletId).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>');

  $.ajax({
        url: 'portalViewer.do?portalBy=viewPortlet&PORTLETID='+portletId+'&REP='+REP+'&CEP='+CEP+'&perBy='+perBy+'&gpType='+gpType+'&portalTabId='+portalTabId+'&currDate='+date+'&periodType='+periodType+'&dateCheck='+dateCheck,
        success: function(data){
            $('#portlet-'+portalTabId+'-'+portletId).html(data);

//            var id="tablesorter"+portletId + "-" + portalTabId;
//            $("#"+id).tablesorter({});
//            $("#"+id).tablesorterPager({container: $('#pagerPortlet')});

         /*   $("#tablesorter"+portletId + "-" + portalTabId).columnFilters().tablesorter({
                widthFixed: true,
                widgets: ['zebra']
            }).tablesorterPager({
                container: $("#pager"+portletId)
            });*/
        }
    });
}else{
    //alert("else")
       $("#portletPreviewDivinDesiner").html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>');
        $.ajax({
        url: 'portalViewer.do?portalBy=viewPortlet&PORTLETID='+portletId+'&REP='+REP+'&CEP='+CEP+'&perBy='+perBy+'&gpType='+gpType+'&portalTabId='+portalTabId+'&currDate='+date+'&periodType='+periodType,
        success: function(data){
            $("#portletPreviewDivinDesiner").html(data);
             var id="tablesorter"+portletId + "-" + portalTabId;
            $("#"+id).tablesorter({});
//            $("#"+id).tablesorterPager({container: $('#pagerPortlet')});
//
//            $("#tablesorter"+portletId).columnFilters().tablesorter({
//                widthFixed: true,
//                widgets: ['zebra']
//            }).tablesorterPager({
//                container: $("#pager"+portletId)
//            });
        }
    });
    }
//setTimeout(dispdata(portletId,REP,CEP,perBy,gpType,portalTabId),'100000'); to refresh for every 10 mins

  //displayDimentions(portletId,portalTabId)
}
function  displayDimentions(portletId,portalTabId){

}
function editPortletName(PortletId,portalTabId,PortletName,date,callfrom){
   // alert("portalTabId"+portalTabId+"-----")
    document.getElementById("editportletName"+portalTabId).value=PortletName;
    document.getElementById("editportletDesc"+portalTabId).value=PortletName;
    document.getElementById("currportletId").value=PortletId;
    var divId="#editportlet"+portalTabId;
    $(divId).dialog({
        autoOpen: false,
        height: 180,
        width: 350,
        position: 'justify',
        modal: true
    });
    //$(divId).dialog('open');
    $("#editportlet"+portalTabId).dialog('open');
}

function extendTable(portletId,dispdiv,selectId,portalTabId,date){
    if(dispdiv.style.display=='none'){
        dispdiv.style.display='';
    }
    else{
        dispdiv.style.display='none';
    }
}

function closeTable(potletId,dispdiv,portalTabId){
    dispdiv.style.display='none';
}

//function timePortlet(PortletId,portalTabId)
//{
//   var divName='time-'+PortletId+'-'+portalTabId;
//
//    if(document.getElementById(divName).style.display=='none')
//        {
//           document.getElementById(divName).style.display='';
//            $('#timeOption'+PortletId+'-'+portalTabId).datepicker({
//        changeMonth: true,
//        changeYear: true,
//        showButtonPanel: true,
//        numberOfMonths: 1,
//        stepMonths: 1
//    });
//
//   }
//        else
//            {
//          document.getElementById(divName).style.display='none'
//
//
//
//  }
//  }
//function getTimePeriodDetails(PortletId,REPId,dispgrptypObj,perBy,gpType,portalTabId,date){
//
//    var time=$('#timeOption'+PortletId+'-'+portalTabId).val();
//    var period=$('#periodType'+PortletId+'-'+portalTabId).val();
//   if(time=="")
//       alert("Please Enter Date")
//   else
//       {
//      var rowEdgeParams="";
//        var colEdgeParams="";
//        var rowParamIdObj=REPId;
//        var columnParmObject=''
//        var CEPNames=''
//        var REPNames=''
//        if(rowParamIdObj!=null){
//
//         for(var i=0;i<rowParamIdObj.length;i++){
//            if(rowParamIdObj[i].checked){
//                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
//                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
//            }
//        }
//        if(rowEdgeParams!=""){
//            rowEdgeParams=rowEdgeParams.substring(1);
//            REPNames=REPNames.substring(1);
//        }
//        if(gpType==""){
//            columnParmObject=document.getElementsByName("chkCEP-"+PortletId+"-"+portalTabId)
//
//            for(var j=0;j<columnParmObject.length;j++){
//                 if(columnParmObject[j].checked){
//                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
//                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
//                 }
//             }
//
//        }
//        }
//
// if(document.getElementById('portlet-'+portalTabId+'-'+PortletId)!=null){
//    $('#portlet-'+portalTabId+'-'+PortletId).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>');
//
//  $.ajax({
//        url: 'portalViewer.do?portalBy=getTimePeriodDetails&PortletId='+PortletId+'&portalTabId='+portalTabId+'&time='+time+'&period='+period+'',
//        success: function(data){
//
//         getPortletDetails(PortletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date);
//
//    }
//    });
//}
//
//       }
//
//}

function getPortletOptions(PortletId,portalTabId){
var divId='PortletOptions-'+PortletId+'-'+portalTabId;

 if(document.getElementById(divId).style.display=='none')
        {
           document.getElementById(divId).style.display='';

    }
        else
      {

         document.getElementById(divId).style.display='none'
      }



}

//function resetTimePeriodDetails(PortletId,REPId,dispgrptypObj,perBy,gpType,portalTabId,date){
//var rowEdgeParams="";
//        var colEdgeParams="";
//        var rowParamIdObj=REPId;
//        var columnParmObject=''
//        var CEPNames=''
//        var REPNames=''
//        if(rowParamIdObj!=null){
//
//         for(var i=0;i<rowParamIdObj.length;i++){
//            if(rowParamIdObj[i].checked){
//                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
//                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
//            }
//        }
//        if(rowEdgeParams!=""){
//            rowEdgeParams=rowEdgeParams.substring(1);
//            REPNames=REPNames.substring(1);
//        }
//        if(gpType==""){
//            columnParmObject=document.getElementsByName("chkCEP-"+PortletId+"-"+portalTabId)
//
//            for(var j=0;j<columnParmObject.length;j++){
//                 if(columnParmObject[j].checked){
//                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
//                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
//                 }
//             }
//
//        }
//        }
//
//if(document.getElementById('portlet-'+portalTabId+'-'+PortletId)!=null){
//    $('#portlet-'+portalTabId+'-'+PortletId).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>');
//
//  $.ajax({
//        url: 'portalViewer.do?portalBy=resetTimePeriodDetails&PortletId='+PortletId+'&portalTabId='+portalTabId+'',
//        success: function(data){
//
//         getPortletDetails(PortletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date);
//
//    }
//    });
//}
//
//}
function openTopBottomForTable(PortletId,REPId,dispgrptypObj,perBy,gpType,portalTabId,date)
{
     var checkval="false"
     var applySortOnTable = "applySortOnTable"
     var sortAll=document.getElementsByName("sortCheckbox"+PortletId+"-"+portalTabId);
      for(var i=0;i<sortAll.length;i++){
             if(sortAll[i].checked){
          checkval=sortAll[i].value

                        }
                    }
           
        var DivId='TopBottom'+PortletId+'-'+portalTabId;
        var countVal=$("#sortTextbox"+PortletId+"-"+portalTabId).val();
        var sortByColumeVal=$("#sortBy"+PortletId+"-"+portalTabId).val();
        var sort='sortTable'+PortletId+"-"+portalTabId;
        var sortType=$("input[name="+sort+"]:checked").val();

       if(document.getElementById(DivId).style.display=='none')
        {
         document.getElementById(DivId).style.display='';
        }else
            {
              document.getElementById(DivId).style.display='none';
           if(jQuery.trim(countVal)!=""||checkval=="true")
               {
               $.ajax({
                url: 'portalViewer.do?portalBy=applySortOnTable&countVal='+countVal+'&sortByColumeVal='+sortByColumeVal+'&sortType='+sortType+'&PortletId='+PortletId+'&portalTabId='+portalTabId+'&checkval='+checkval,
                success:function(data){
                 var rowEdgeParams="";
        var colEdgeParams="";
        var rowParamIdObj=REPId;
        var columnParmObject=''
        var CEPNames=''
        var REPNames=''
        if(rowParamIdObj!=null){

         for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        if(gpType==""){
            columnParmObject=document.getElementsByName("chkCEP-"+PortletId+"-"+portalTabId)

            for(var j=0;j<columnParmObject.length;j++){
                 if(columnParmObject[j].checked){
                colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                 }
             }

        }
        }


        getPortletDetails(PortletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,applySortOnTable);

  }} );





               }



}

}
function onChangeCheckbox(PortletId,portalTabId)
{

    var checkboxId='sortCheckbox'+PortletId+"-"+portalTabId;
    var sortAll=document.getElementsByName(checkboxId);
   for(var i=0;i<sortAll.length;i++)
        {
            if(sortAll[i].checked)
                {
                    $("#sortTextbox"+PortletId+"-"+portalTabId).hide();
                }
            else
                {
                     $("#sortTextbox"+PortletId+"-"+portalTabId).show();
                 }
        }

}


function openCEPPreviews(portletId,REPId,CEPId,dispgrptypObj,perBy,gpType,portalTabId,date){
//        alert("rep");
var openCEPPreviews = "openCEPPreviews";
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
        var rowEdgeParams="";
        var colEdgeParams="";
        var REPNames="";
        var CEPNames="";
        var rowParamIdObj=REPId;
        var colParamIdObj=CEPId;
        for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        for( i=0;i<colParamIdObj.length;i++){
            if(colParamIdObj[i].checked){
                colEdgeParams=colEdgeParams+","+colParamIdObj[i].value.split("~")[0];
                CEPNames=CEPNames+","+colParamIdObj[i].value.split("~")[1];
            }
        }
        if(colEdgeParams!=""){
            colEdgeParams=colEdgeParams.substring(1);
            CEPNames=CEPNames.substring(1);
        }
        getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,openCEPPreviews);

    }

}
function openREPPreviews(portletId,REPId,CEPId,dispgrptypObj,perBy,gpType,portalTabId,date){

    var openREPPreviews = "openREPPreviews";
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
        var rowEdgeParams="";
        var colEdgeParams="";
        var REPNames="";
        var CEPNames="";
        var rowParamIdObj=REPId;
        var colParamIdObj=CEPId;
        for(var i=0;i<rowParamIdObj.length;i++){
            if(rowParamIdObj[i].checked){
                rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
            }
        }
        if(rowEdgeParams!=""){
            rowEdgeParams=rowEdgeParams.substring(1);
            REPNames=REPNames.substring(1);
        }
        for( i=0;i<colParamIdObj.length;i++){
            if(colParamIdObj[i].checked){
                colEdgeParams=colEdgeParams+","+colParamIdObj[i].value.split("~")[0];
                CEPNames=CEPNames+","+colParamIdObj[i].value.split("~")[1];
            }
        }
        if(colEdgeParams!=""){
            colEdgeParams=colEdgeParams.substring(1);
            CEPNames=CEPNames.substring(1);
        }
        getPortletDetails(portletId, rowEdgeParams, colEdgeParams,perBy,gpType,portalTabId,date,openREPPreviews);
    }

}


function DrillToReport(portletId,portalTabId)
{
     closeOpenDivs(portletId,portalTabId)
    $("#drilldownSuperdiv-"+portalTabId).html("")
        //    alert("1")
portltId=portletId;
portlId=portalTabId;
var divId="drillToReport-"+portalTabId+"-"+portletId
//alert("1\t"+$("#drilldownSuperdiv").html())
$("#"+divId).remove()

 var innerhtmlofdiv="<div id='"+divId+"' style='display:none' title='Drill To Report'><form id='DrilltoRepForm' name ='DrilltoRepForm' method='post'><table id='PortletDrillTable' name ='PortletDrillTab'><tbody id='getDrillReportsinportlet-"+portalTabId+"-"+portletId+"'></tbody><tfoot><tr></tr><tr></tr><tr></tr><tr><td colspan='4' align='center' rowspan='2'><input type='button' class='navtitle-hover' style='width:auto' name='Done' value='GO' onclick='portletDrillToReport()' /><br><br></td></tr></tfoot></table></form></div>"
$("#drilldownSuperdiv-"+portalTabId).html(innerhtmlofdiv)
//alert("2\t"+$("#drilldownSuperdiv").html())
if($("#"+divId).html()!=null){
$("#"+divId).dialog({
        autoOpen: false,
        height: 180,
        width: 420,
        position: 'justify',
        modal: true
    });
}
$.post('portalViewer.do?portalBy=DrillToReport&PORTLETID='+portletId+'&portalTabId='+portalTabId,
              function(data){
                    if(data != ""){
                        var htmlVal=data.split("~")
                        $("#getDrillReportsinportlet-"+portalTabId+"-"+portletId).html(htmlVal[0]);
                          selectedreportId=htmlVal[1];
                    }

                });
$("#"+divId).dialog('open');
}
function portletDrillToReport()
{
            $("#drillToReport-"+portlId+"-"+portltId).dialog('close');
            var IDVal="#selectReport"+selectedreportId
            drillReportId =  $(IDVal).val();
            var url= 'reportViewer.do?reportBy=viewReport&REPORTID=' + drillReportId;
            document.frmParameter.action = url;
            document.frmParameter.submit();
}
function relatedPortlets(portletId,portalTabId)
{
    var divId='RELATEDPORTLET-'+portletId+'-'+portalTabId;
 if(document.getElementById(divId).style.display=='none')
        {
           document.getElementById(divId).style.display='';

    }
        else
      {

         document.getElementById(divId).style.display='none'
      }

}
function DesignrelatedPortlets(portletId,portalTabId)
{

    PortalTabId=portalTabId
    PortLetId=portletId
$("#drilldownSuperdiv-"+portalTabId).html("")
var divId="addRelatedPortlets-"+portalTabId+"-"+portletId
var dragDivId="dragdiv-"+portalTabId+"-"+portletId
// var assinIdAndVales=new Array
//            var isMemberUseInOtherLevel="false"
$("#"+divId).remove();
                        $.post(
                    'portalViewer.do?portalBy=getRelatedPortlets&portletId='+portletId+'&portalTabId='+portalTabId,
                   function(data){
                        var jsonVar=eval('('+data+')')
                        $("#"+dragDivId).html("")
                        $("#"+dragDivId).html(jsonVar.htmlStr)
                         isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                        $(".myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });

                        $('ul.myList3 li').quicksearch({
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
                        });
                        $(".sortable").sortable();
                        }
                        );
var innerhtmlofdiv="<div id='"+divId+"'  title='Design Related Portlets' style='overflow:auto'><div id='"+dragDivId+"'style='overflow:auto'></div><table><tr colspan='2' align='center'><td colspan='2' align='center'><input type='button' class='navtitle-hover' style='width:auto' value='Save' id='relatedportletsave' onclick='SaveRelatedPortletsName()'></td></tr></table></div>";
$("#drilldownSuperdiv-"+portalTabId).html(innerhtmlofdiv)
if($("#"+divId).html()!=null){
$("#"+divId).dialog({
        autoOpen: false,
        height: 550,
        width: 700,
        position: 'justify',
        modal: true,close: function(){
                       $("#drilldownSuperdiv-"+portalTabId).html("");
                        window.location.href =window.location.href;
                   }
    });
}
 var relatedId='RELATEDPORTLET-'+PortLetId+'-'+PortalTabId;
document.getElementById(relatedId).style.display='none'
$("#"+divId).dialog('open');
}
function SaveRelatedPortletsName()
{
    var divId="addRelatedPortlets-"+PortalTabId+"-"+PortLetId
                var mbrIds="";
                var mbrs="";
                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");
                //alert('liObj length is : '+liObj.length)
                for(var i=0;i<liObj.length;i++){
                    mbrIds=(liObj[i].id).split("~");
                    mbrs=mbrs+","+mbrIds[0].replace("_li", "", "gi");
                }
                if(mbrs!="" && mbrIds.length!=0){
                    mbrs=mbrs.substr(1);
                }

                var mbrsArr = mbrs.split(",");
                mbrs = "";
//                 alert("mbrsArr\t"+mbrsArr)


  $.ajax({
                    url:'portalViewer.do?portalBy=saveRelatedPortlets&PortalTabId='+PortalTabId+'&PortLetId='+PortLetId+'&reletedIds='+mbrsArr.toString(),
                    success:function(data){
                        document.forms.frmParameter.action="pbPortalViewer.jsp#"+trim(data.replace(" ","_","gi"))
                       document.forms.frmParameter.submit()

$("#"+divId).dialog('close');
                    }
  });

}
function ShowrelatedPortlets(portletId,portalTabId)
{
     var relatedId='RELATEDPORTLET-'+portletId+'-'+portalTabId;
document.getElementById(relatedId).style.display='none'
$("#drilldownSuperdiv-"+portalTabId).html("")
var divId="showRelatedPortlets-"+portalTabId+"-"+portletId
var iframeId="showRelatedPortletsFrame-"+portletId+"-"+portalTabId
$("#"+divId).remove()
var innerhtmlofdiv="<div id='"+divId+"'  title='Show Related Portlets'><iframe  id='"+iframeId+"' name='addRelatedPortletFrame'  frameborder=\"0\" style=\"width:900px; height:100%;\"  src='about:blank'></iframe></div>";
$("#drilldownSuperdiv-"+portalTabId).html(innerhtmlofdiv)
if($("#"+divId).html()!=null){
$("#"+divId).dialog({
        autoOpen: false,
        height: 650,
        width: 900,
        position: 'justify',
        modal: true
    });
}
$.ajax({
                        url: 'portalViewer.do?portalBy=setRelatedPortlet&portletId='+portletId+'&portalTabId='+portalTabId,
                        success: function(data){
                            if(data=="true"){
                                $("#"+divId).dialog('open');
                                openRelatedPortlets(portletId,portalTabId)
                            }
                            else{
                                alert("no related portlets")                               
                            }
                        }
});
}

function openRelatedPortlets(portletId,portalTabId){
    var iframeId="showRelatedPortletsFrame-"+portletId+"-"+portalTabId
    $("#"+iframeId).attr("src",'RealatedPortlets.jsp')

}
function changeTargetValue(portletId,portalTabId,ElementId,kpimasterid,reportid)
{
$("#drilldownSuperdiv-"+portalTabId).html("")
portltId=portletId;
portlId=portalTabId;
elemntId=ElementId;
kpimasterId=kpimasterid;
reportId=reportid;
targetDivid="changeTarget-"+portalTabId+"-"+portletId+"-"+ElementId
targetId = "Target-"+portalTabId+"-"+portletId+"-"+ElementId
$("#"+targetDivid).remove()

 var innerhtmlofdiv="<div id='"+targetDivid+"' style='display:none' title='Enter Target'><form id='ChangeTargetForm' name ='ChangeTargetForm' method='post'><table id='TargetTable' name ='TargetTable'><tbody id='TargetValue'><tr><td>Enter Target</td><td><input type='text' id='"+targetId+"' name='TargetVal' value=''></td></tr></tbody><tfoot><tr></tr><tr></tr><tr></tr><tr><td colspan='4' align='center' rowspan='2'><input type='button' class='navtitle-hover' style='width:auto' name='Done' value='GO' onclick='saveTargetValue()' /><br><br></td></tr></tfoot></table></form></div>"
$("#drilldownSuperdiv-"+portalTabId).html(innerhtmlofdiv)
if($("#"+targetDivid).html()!=null){
$("#"+targetDivid).dialog({
        autoOpen: false,
        height: 180,
        width: 420,
        position: 'justify',
        modal: true
    });
}
$("#"+targetDivid).dialog('open');

}
function saveTargetValue()
{
//   tval=document.getElementById('targetId').value;
    $("#"+targetDivid).dialog('close');
   $.ajax({
       url: 'portalViewer.do?portalBy=saveTargetValue&portletId='+portltId+'&portalTabId='+portlId+'&ElementId='+elemntId+'&tval='+tval+'&ReportId='+reportId+'&kpiMaterId='+kpimasterId,
       Success:function(data){

       }
   });}

function resetTablePortlet(portletId,REP,CEP,portalTabId,currDate){
    var rowEdgeParams="";
                    var colEdgeParams="";
                    var rowParamIdObj=document.getElementsByName("chkREP-"+portletId+ "-" +portalTabId)
                    var columnParmObject=''
                    var CEPNames=''
                    var REPNames=''
                     columnParmObject=document.getElementsByName("chkCEP-"+portletId+"-"+portalTabId)
                     if(rowParamIdObj!=null){

                    for(var i=0;i<rowParamIdObj.length;i++){
                       if(rowParamIdObj[i].checked){
                            rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value.split("~")[0];
                            REPNames=REPNames+","+rowParamIdObj[i].value.split("~")[1];
                        }
                    }
                    if(rowEdgeParams!=""){
                        rowEdgeParams=rowEdgeParams.substring(1);
                        REPNames=REPNames.substring(1);
                         }
                      }
                    if(columnParmObject!=null){

                            for(var j=0;j<columnParmObject.length;j++){
                             if(columnParmObject[j].checked){
                            colEdgeParams=colEdgeParams+","+columnParmObject[j].value.split("~")[0];
                            CEPNames=CEPNames+","+columnParmObject[j].value.split("~")[1];
                             }
                            }
                              if(colEdgeParams!=""){
                             colEdgeParams=colEdgeParams.substring(1);
                             CEPNames=CEPNames.substring(1);
                            }
                         }



     $.ajax({
        url: 'portalViewer.do?portalBy=resetTablePortlet&portletId='+portletId+'&portalTabId='+portalTabId,
        success: function(data){
    getPortletDetails(portletId, rowEdgeParams, colEdgeParams,"","",portalTabId,"");
    }
    });
}
function editTarget(portlId,portlTabId,perby,kpigrpType,measurename,daytargetval){
var divid = 'editTgtDiv-'+portlId+'-'+portlTabId
editDiv = divid;
$("#drilldownSuperdiv-"+portlTabId).html("")
$("#"+divid).remove()

 var innerhtmlofdiv="<div id='"+divid+"' style='display:none' title='Edit Target'><table width='100%'><tr></tr><td width='25%' align='left'></td><td width='75%' align='left'>Per Day</td><tr><td width='75%' align='left'>"+measurename+" Value</td><td width='25%' align='left'><Input type='text' name='edit Target'  maxlength=100  style='width:160px' value='"+daytargetval+"' readonly></td></tr><br/><tr><td width='75%' align='left'>Enter Target Value</td><td width='25%' align='left'><Input type='text' name='edit Target' maxlength=100  style='width:160px' id='edtTgtVal' value='"+daytargetval+"' ></td></tr><br/><br/><tr><td align ='center' colspan='4'><input type='button' name='Save' value='Done' class='navtitle-hover' onclick=SaveKpiTarget('"+portlId+"','"+portlTabId+"','"+perby+"','"+kpigrpType+"')></td></tr></table></div>"
$("#drilldownSuperdiv-"+portlTabId).html(innerhtmlofdiv)
//alert("2\t"+$("#drilldownSuperdiv").html())
if($("#"+divid).html()!=null){
$("#"+divid).dialog({
        autoOpen: false,
            height: 250,
            width: 370,
            title:'Enter target',
            position: 'justify',
            modal: true
    });
}
$("#"+divid).dialog('open');
}
function SaveKpiTarget(portltId,portlTabId,perby,grptype){
 tgtVal = $("#edtTgtVal").val();
 $.ajax({
        url: 'portalViewer.do?portalBy=setTargetVal&portletId='+portltId+'&portalTabId='+portlTabId+'&targetVal='+tgtVal,
        success: function(data){
    }
    });
 $("#"+editDiv).dialog('close');
 var measId=$("select#MeasId").val();
     var rowEdgeParams="";
    var colEdgeParams="";
 getPortletDetails(portltId, rowEdgeParams, colEdgeParams, perby, grptype, portlTabId);
}

//function showGraphColumns(portletId,portalTabId,folderId,measureId,measureName){
//      // alert(measureId+","+measureName)
//       $("#graphMeasuresDiv").dialog("open");
//      var frameObj=document.getElementById("graphMeasures");
//      frameObj.src = "pbPortletGraphMeasures.jsp?graphId=0&folderIds="+folderId+"&grpIds=0&measureId="+measureId+"&measureName="+encodeURI(measureName)+"&portletId="+portletId+"&portalTabId="+portalTabId;
//
//}
//function cancelCols(){
//    //$("graphColsDiv").dialog('open');
//    document.getElementById('fade').style.display='none';
//     $("#graphMeasuresDiv").dialog("close");
//
//}

function showTimeInfo(portlId,portlTabId){       
    var divId=document.getElementById("timeInfo-"+portlId+"-"+portlTabId);   
    $(divId).dialog({
        autoOpen: false,
        height: 240,
        width: 200,
        position: 'justify',
        modal: true                   
    });
    $(divId).dialog('open');
}
