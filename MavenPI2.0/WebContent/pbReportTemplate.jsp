<%--
    Document   : pbReportTemplate
    Created on : Sep 9, 1809, 1:56:55 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.util.screenDimensions,com.progen.charts.ProGenChartUtilities,prg.db.Container,java.util.*,prg.db.PbReturnObject,prg.db.PbDb,com.progen.users.PrivilegeManager"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<%           //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String ViewFrom = "Designer";
            session.setAttribute("ViewFrom", ViewFrom);
            boolean showExtraTabs = true;

            String loguserId = String.valueOf(session.getAttribute("USERID"));
            HashMap ParametersHashMap = null;
            HashMap TableHashMap = null;
            HashMap GraphHashMap = null;
            HashMap ReportHashMap = null;

            String ReportId = "";
            Container container = null;
            String reportName = "";
            String reportDesc = "";
            HashMap map = new HashMap();
            String ReportFolders = "";
            String ReportDimensions = "";
            String ParamRegion = "";
            String ParamDispRegion = "";
            String TableRegion = "";
            String GraphRegion = "";
            String prevParamArray = "";
            String prevTimeParams = "";

            String prevREP = "";
            String prevCEP = "";
            String prevMeasures = "";
            String prevMeasureNames = "";
            String prevMeasureNamesList = "";

            ArrayList Parameters = new ArrayList();
            ArrayList ParametersNames = new ArrayList();

            ArrayList TimeParameters = new ArrayList();
            ArrayList TimeParametersNames = new ArrayList();


            ArrayList REP = new ArrayList();
            ArrayList CEP = new ArrayList();
            ArrayList Measures = new ArrayList();
            ArrayList MeasuresNames = new ArrayList();

            ArrayList repExclude = new ArrayList();
            ArrayList cepExclude = new ArrayList();

            String prevRepExclude = "";
            String prevCepExclude = "";
            HashMap GraphTypesHashMap = null;
            String[] grpTypeskeys = new String[0];
            String graphIds = "";
            String sqlStr = "";
            if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {

                try {
                    GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                    grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);

                    ReportId = String.valueOf(request.getAttribute("ReportId"));
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");

                    if (map.get(ReportId) != null) {
                        container = (prg.db.Container) map.get(ReportId);
                    } else {
                        container = new prg.db.Container();
                    }
                    reportName = container.getReportName();
                    reportDesc = container.getReportDesc();
                    // reportName=reportName.replace("'", "\'");
                    // reportDesc=reportDesc.replace("'", "\'");

                    ParametersHashMap = container.getParametersHashMap();
                    TableHashMap = container.getTableHashMap();
                    GraphHashMap = container.getGraphHashMap();
                    ReportHashMap = container.getReportHashMap();
                    sqlStr = container.getSqlStr();
                    // //("sqlstr in jsp is : " + sqlStr);
                    if (request.getAttribute("ReportFolders") != null) {
                        ReportFolders = String.valueOf(request.getAttribute("ReportFolders"));
                    }
                    if (request.getAttribute("ReportDimensions") != null) {
                        ReportDimensions = String.valueOf(request.getAttribute("ReportDimensions"));
                    }
                    if (request.getAttribute("ParamRegion") != null) {
                        ParamRegion = String.valueOf(request.getAttribute("ParamRegion"));
                    }
                    if (request.getAttribute("ParamDispRegion") != null) {
                        ParamDispRegion = String.valueOf(request.getAttribute("ParamDispRegion"));
                    }

                    if (request.getAttribute("TableRegion") != null) {
                        TableRegion = String.valueOf(request.getAttribute("TableRegion"));
                    }
                    if (request.getAttribute("GraphRegion") != null) {
                        GraphRegion = String.valueOf(request.getAttribute("GraphRegion"));
                    }

                    if (TableHashMap != null && TableHashMap.size() != 0) {
                        REP = (ArrayList) TableHashMap.get("REP");
                        CEP = (ArrayList) TableHashMap.get("CEP");
                        Measures = (ArrayList) TableHashMap.get("Measures");
                        MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");

                        if (REP != null) {
                            for (int j = 0; j < REP.size(); j++) {
                                prevREP = prevREP + "," + REP.get(j);
                            }
                            if (!(prevREP.equalsIgnoreCase(""))) {
                                prevREP = prevREP.substring(1);
                            }
                        }
                        if (CEP != null) {
                            for (int j = 0; j < CEP.size(); j++) {
                                prevCEP = prevCEP + "," + CEP.get(j);
                            }
                            if (!(prevCEP.equalsIgnoreCase(""))) {
                                prevCEP = prevCEP.substring(1);
                            }
                        }
                        if (Measures != null && MeasuresNames != null) {
                            for (int j = 0; j < Measures.size(); j++) {
                                prevMeasures = prevMeasures + "," + Measures.get(j);
                                prevMeasureNames = prevMeasureNames + "," + MeasuresNames.get(j);
                                prevMeasureNamesList = prevMeasureNamesList + "," + MeasuresNames.get(j) + "-" + Measures.get(j);
                            }
                            if (!(prevMeasures.equalsIgnoreCase(""))) {
                                prevMeasures = prevMeasures.substring(1);
                                prevMeasureNames = prevMeasureNames.substring(1);
                                prevMeasureNamesList = prevMeasureNamesList.substring(1);
                            }
                        }
                    }
                    if (ParametersHashMap.get("Parameters") != null && ParametersHashMap.size() != 0) {
                        Parameters = (ArrayList) container.getParametersHashMap().get("Parameters");
                        ParametersNames = (ArrayList) container.getParametersHashMap().get("ParametersNames");
                    }

                    if (Parameters.size() != 0) {
                        for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                            prevParamArray = prevParamArray + "," + String.valueOf(ParametersNames.get(paramIndex)) + "-" + String.valueOf(Parameters.get(paramIndex));
                        }
                        if (!(prevParamArray.equalsIgnoreCase(""))) {
                            prevParamArray = prevParamArray.substring(1);
                        }
                    }
                    if (ParametersHashMap.get("TimeParameters") != null) {
                        TimeParameters = (ArrayList) ParametersHashMap.get("TimeParameters");
                        TimeParametersNames = (ArrayList) ParametersHashMap.get("TimeParametersNames");
                    }
                    if (Parameters.size() != 0) {
                        for (int paramIndex = 0; paramIndex < Parameters.size(); paramIndex++) {
                            prevTimeParams = prevTimeParams + "," + String.valueOf(ParametersNames.get(paramIndex)) + "-" + String.valueOf(Parameters.get(paramIndex));
                        }
                        if (!(prevTimeParams.equalsIgnoreCase(""))) {
                            prevTimeParams = prevTimeParams.substring(1);
                        }
                    }

                    String UserFldsData = "";
                    if (request.getAttribute("UserFlds") != null) {
                        UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
                    }

                    if (ParametersHashMap.get("repExclude") != null && ParametersHashMap.get("cepExclude") != null) {
                        repExclude = (ArrayList) ParametersHashMap.get("repExclude");
                        cepExclude = (ArrayList) ParametersHashMap.get("cepExclude");

                        if (repExclude != null && repExclude.size() != 0) {
                            for (int j = 0; j < repExclude.size(); j++) {
                                prevRepExclude = prevRepExclude + "," + repExclude.get(j);
                            }
                            if (!(prevRepExclude.equalsIgnoreCase(""))) {
                                prevRepExclude = prevRepExclude.substring(1);
                            }
                        }
                        if (cepExclude != null && cepExclude.size() != 0) {
                            for (int j = 0; j < cepExclude.size(); j++) {
                                prevCepExclude = prevCepExclude + "," + cepExclude.get(j);
                            }
                            if (!(prevCepExclude.equalsIgnoreCase(""))) {
                                prevCepExclude = prevCepExclude.substring(1);
                            }
                        }
                    }
                    if (GraphHashMap != null && GraphHashMap.get("graphIds") != null) {
                        graphIds = String.valueOf(GraphHashMap.get("graphIds"));
                    }
                    //for clearing cache
                    response.setHeader("Cache-Control", "no-store");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);

                    screenDimensions dims = new screenDimensions();
                    int pageFont, anchorFont;
                    HashMap screenMap = dims.getFontSize(session, request, response);
                    ////.println("screenMap --" + screenMap.size());
                    if (!String.valueOf(screenMap.get("pageFont")).equalsIgnoreCase("NULL")) {
                        pageFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont")));
                        anchorFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont"))) + 1;
                        ////.println("pageFont--" + pageFont + "---anchorFont--" + anchorFont);
                    } else {
                        pageFont = 11;
                        anchorFont = 12;
                        ////.println("pageFont--" + pageFont + "---anchorFont--" + anchorFont);
                    }

                    PbDb pbdb = new PbDb();
                    String existparamvalues = "";
                    //String eledetsexistsQuery = "select MEMBER_VALUE from PRG_AR_PARAMETER_SECURITY where report_id=" + ReportId;
                    String eledetsexistsQuery = "delete from PRG_AR_PARAMETER_SECURITY where report_id=" + ReportId;
                    pbdb.execModifySQL(eledetsexistsQuery);

                     String ddformT = null;
                     if(session.getAttribute("dateFormat")!=null){
                    ddformT = session.getAttribute("dateFormat").toString();
                    }

                    //if (pbroexist.getRowCount() > 0) {

                    //}

                    if (session.getAttribute("USERID") == null || String.valueOf(screenMap.get("Redirect")).equalsIgnoreCase("Yes")) {
                        response.sendRedirect(request.getContextPath() + "/newpbLogin.jsp");
                    } else {
                         String contextPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.columnfilters.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascrip        <script type="text/javascript" src="<%=contextPath%>/javascript/queryDesign.js"></script>t/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>-->

<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js"></script>
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/chili-1.8b.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/docs.js"></script>-->
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <link href="<%=contextPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<%=contextPath%>/javascript/toolTip.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/toolTip.css" type="text/css" />

        <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/reportviewer/ReportViewer.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/pbreporttemplateframejs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbTableMapJS.js"></script>
         <script  type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>
         <%--<script type="text/javascript" src="<%=contextPath%>/TableDisplay/JS/pbGraphDisplayRegionJS.js"></script>--%>
<!--         <script  type="text/javascript" src="<%=contextPath%>/javascript/reportDesign.js" ></script>-->
        <script type="text/javascript">
              $(document).ready(function(){
                 $("#GraphsRegOfReport").hide();

            });
            var repExist='N';
            function copyDate(eleId){
                //alert(eleId);
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
            function shwDate(){
                $('#datepicker1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
            }
            function initialog(){
                if ($.browser.msie == true){
                    $("#changeNameDialog").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }
                else{
                    $("#changeNameDialog").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }
            }
            function changeReportNameTemp(){
                var repId=document.getElementById("REPORTID").value;
                var  newRepName=document.getElementById("repName2").value;
                // newRepName=newRepName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var  newRepDesc= document.getElementById("repDesc2").value;

                // newRepDesc=newRepDesc.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                var encodednewRepDesc=encodeURIComponent(newRepDesc);
                var encodednewRepName=encodeURIComponent(newRepName);
                $.ajax({
                    url: "reportTemplateAction.do?templateParam=checkReportNameBeforeUpdate&newRepName="+encodednewRepName+"&REPORTID="+repId+'&newRepDesc='+encodednewRepDesc,
                    success: function(data){
                        if(data==1){
                            document.getElementById("reportNameH").value=newRepName;
                            document.getElementById("reportDescH").value=newRepDesc;
                            document.getElementById('repMsg').innerHTML ='';
                            closeChangeName(newRepName,newRepDesc,repExist);
                        }
                        else{
                            document.getElementById('repMsg').innerHTML = "Report Name already exists";
                        }
                    }
                });
            }

            function closeChangeName(newRepName,newRepDesc,repExist){
                var repId=document.getElementById("REPORTID").value;
                var encodednewRepDesc=encodeURIComponent(newRepDesc);
                var encodednewRepName=encodeURIComponent(newRepName);
                $.ajax({
                    url: "reportTemplateAction.do?templateParam=changeReportName&reportName="+encodednewRepName+"&reportID="+repId+'&newRepDesc='+encodednewRepDesc,
                    success: function(data){
                    }
                });
                // newRepName=newRepName.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');

                document.getElementById("reportName").innerHTML=newRepName;
                $("#changeNameDialog").dialog('close');
                if(repExist=='Y'){
                    createReport();
                }
            }
            function changeReportName(){
                initialog();
                var repId=document.getElementById("REPORTID").value;
                document.getElementById("repName2").value= document.getElementById("reportNameH").value;
                document.getElementById("repDesc2").value=document.getElementById("reportDescH").value;
                $("#changeNameDialog").dialog('open');
            }
            function checkReportNameatRoleLevel(){
                var repId=document.getElementById("REPORTID").value;
                var params=getBuildedParams();
                var repName=document.getElementById("REPORTNAMEbeforeSave").value;
                // repName=repName.replace('&','^').replace('+','~').replace('#','`').replace('%','_');
                repName=encodeURIComponent(repName);
                var folders=buildFldIds();
                $.ajax({
                    url: "reportTemplateAction.do?templateParam=checkReportNameatRoleLevel&repName="+repName+"&REPORTID="+repId+"&folderIds="+folders+"&params="+params,
                    success: function(data){
                        if(data==1){
                            createReport();
                        }
                        else{
                            alert('Report Name Already Exists,Enter New Reportname')
                            initialog();
                            repExist='Y';
                            $("#changeNameDialog").dialog('open');
                        }
                    }
                });
            }

            function resetCurrRep(){

        var restCon=confirm("Do You want to Reset?");
              if(restCon){
                var reportID=document.getElementById("REPORTID").value;
                $.ajax({
                    url:"reportTemplateAction.do?templateParam=resetCurrReport&reportId="+reportID,
                    success:function(data){
                        if(data!=null){
                            document.location.href=document.location.href;
                        }
                    }
                })
                 }

            }
            function enableMap(){
                var reportId=document.getElementById("REPORTID").value;
                enableMaps(reportId);
//                var flag=true;
//
//                if($("#enableMapId").val()=="Disable Map") {
//                    $("#enableMapId").attr("mapEnabled","false");
//                    flag=$("#enableMapId").attr("mapEnabled").valueOf();
//                    $("#enableMapId").val("Enable Map");
//                    alert("Geo Map has been disabled for this report");
//                }
//                else{
//                    $("#enableMapId").attr("mapEnabled","true");
//                    flag=$("#enableMapId").attr("mapEnabled").valueOf();
//                    $("#enableMapId").val("Disable Map")
//                    alert("Geo Map has been enabled for this report");
//                }
//
//
//                $.ajax({
//                    url:"reportTemplateAction.do?templateParam=enableMap&reportId="+reportId+"&mapFlag="+flag,
//                    success:function(data){
//                        if(data!=null){
//                        }
//                    }
//                });
            }

            function updateFavouriteParams(){
                var folderId=parent.buildFldIds();
                var reportId=document.getElementById("REPORTID").value;
                var favParamName=document.getElementById("favouriteParamName").value;
                var favParamDesc=document.getElementById("favouriteParamDesc").value;
                var ulObj=document.getElementById("favouriteDims");
                var liObjs=ulObj.getElementsByTagName('li');
                var elementIds="";
                for(var i=0;i<liObjs.length;i++){
                    elementIds+=liObjs[i].id.replace("drop","");
                    if(i!=liObjs.length-1)
                        elementIds+=",";
                }
                if(elementIds!=""){
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=updateUserFavouriteDims&foldersIds='+folderId+'&elementIds='+elementIds+'&favName='+favParamName+'&favParamDesc='+favParamDesc+'&REPORTID='+reportId+'&USERID=<%=loguserId%>',
                        success: function(data) {
                            alert("Favourite Parameters Saved Successfully")
                            $("#favouriteParamsDiv").dialog('close');
                            parent.getFavParams();
                        }
                    });
                }else{
                    alert("Please select atleast one Dimension")
                }
            }

            function closeDiv(){
                $("#viewFavouriteDiv").dialog('close')
            }

            function openContextMenu(id,favName){
                $("#"+id).contextMenu({ menu: 'favouriteParamsMenu', leftButton: true }, function(action, el, pos) {

                    viewEditFavouriteParams(action, id, pos, favName); });


            }
            function delfavparam(favName)
                    {                        
                       $(".closed").contextMenu({
                       menu: 'parmsMenu'
                       }, function(action, el, pos) {
                       contextMenuFavParam(action, el, pos,favName);
                     });
                    }
            function contextMenuFavParam(action, el, pos,favName) {
                        switch (action) {
                            case "deleteFavParams":                                
                                {                                                                        
                                    var confirmText= confirm("Are you sure you want to delete "+ favName);                                    
                    if(confirmText==true){                        
                       $.ajax({
                        url: "<%=request.getContextPath()%>/dashboardTemplateAction.do?templateParam2=deleteFavoParam&favName="+favName,                        
                        success: function(data){                            
                            if(data=="true"){                                
                                alert(favName+" deleted successfully");
                                window.location.href = window.location.href;
                            }else{
                                alert(favName+" not deleted successfully");
                            }
                        }
                    });                                 
                                    break;
                                }
                                }
                        }                        
                    }
            function deleteFavouriteDim(elementId){
                // alert("elementid--"+elementId)
                var liObj=document.getElementById(elementId);
                var ulObj=document.getElementById("DimensionsUL");
                // var ulObj=divObj.getElementsByTagName("ul");
                var id=liObj.id.replace("drop","");
                //  alert("id--"+id);
                var tableObjs = liObj.getElementsByTagName("table");
                var tableId=tableObjs[0].id;
                var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                var tdObjs =  trObjs[0].getElementsByTagName("td");
                var content = tdObjs[1].innerHTML;
                //  alert("conent--"+content)
                var childLI=document.createElement("li");
                childLI.id=id;
                childLI.style.width='200px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover DimensionULClass';

                var tableStr="<table  id="+tableId+">";
                tableStr+="<tbody>";
                tableStr+="<tr align='center'>";
                tableStr+="<td style='color: black;'>"+content+"</td>";
                tableStr+="</tr>";
                tableStr+="</tbody>";
                tableStr+="</table>";
                childLI.innerHTML=tableStr;
                ulObj.appendChild(childLI)
                $("#"+elementId).remove();
            }

            function viewEditFavouriteParams(action, id, pos, favName){
                switch(action){
                    case "viewFavParams":{
                            $("#viewFavouriteDiv").dialog('open')
                            var favparam=id.replace("favouriteSpan","");
                            $.ajax({
                                url: 'reportTemplateAction.do?templateParam=editUserFavouriteDims&foldersIds='+parent.buildFldIds()+'&favName='+favparam+'&REPORTID='+document.getElementById("REPORTID").value+'&USERID=<%=loguserId%>',
                                success: function(data) {
                                    //var table= "<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input align='center' type='button' onclick='closeDiv()' value='Done' class='navtitle-hover'>";
                                    var result=data.split("@");
                                    $("#viewFavouriteUL").html(result[0]);
                                    // var html=$("#viewFavouriteDiv").html()
                                    // $("#viewFavouriteDiv").html(html);

                                    var ulObj=document.getElementById("viewFavouriteUL");
                                    var liObjs=ulObj.getElementsByTagName("li");
                                    for(var i=0;i<liObjs.length;i++){
                                        liObjs[i].id="";
                                        var tableObj=liObjs[i].getElementsByTagName('table');
                                        var trObjs=tableObj[0].getElementsByTagName('tr');
                                        var tdObj=trObjs[0].getElementsByTagName('td');
                                        tdObj[0].innerHTML="";
                                    }
                                }

                            });

                            break;
                        }
                    case "editFavParams":
                        {
                            $("#favouriteParamsDiv").dialog('open')
                            var favparam=id.replace("favouriteSpan","");

                            $.ajax({
                                url: 'reportTemplateAction.do?templateParam=editUserFavouriteDims&foldersIds='+parent.buildFldIds()+'&favName='+favparam+'&REPORTID='+document.getElementById("REPORTID").value+'&USERID=<%=loguserId%>',
                                success: function(data) {
                                    // alert("data--"+data)
                                    var result=data.split("@");
                                    $("#favouriteDims").html(result[0]);
                                    $("#DimensionsUL").html(result[1]);
                                    initDrag();
                                }
                            });

                            break;

                        }
                     case "deleteFavParams":                                
                                {                                                                        
                                    var confirmText= confirm("Are you sure you want to delete "+ favName);                                    
                    if(confirmText==true){                        
                       $.ajax({
                        url: "<%=request.getContextPath()%>/dashboardTemplateAction.do?templateParam2=deleteFavoParam&favName="+favName,                        
                        success: function(data){                            
                            if(data=="true"){                                
                                alert(favName+" deleted successfully");
                                window.location.href = window.location.href;
                            }else{
                                alert(favName+" not deleted successfully");
                            }
                        }
                    });                                 
                                    break;
                                }
                                }
                    }
                }
                function initDrag(){

                    $(".DimensionULClass").draggable({
                        helper:"clone",
                        effect:["", "fade"]
                    });
                    $("#favouriteDimsDiv").droppable({
                        activeClass:"blueBorder",
                        accept:'.DimensionULClass',

                        drop: function(ev, ui) {
                            var divObj=document.getElementById(this.id);
                            var dropUlObj=divObj.getElementsByTagName("ul");
                            var dropUlId=dropUlObj[0].id;
                            var dragLiId=ui.draggable.attr('id');

                            var liObjs = document.getElementById(dragLiId);
                            var tableObjs = liObjs.getElementsByTagName("table");
                            var tableId=tableObjs[0].id;
                            var tbodyObj = tableObjs[0].getElementsByTagName("tbody");
                            var trObjs=  tbodyObj[0].getElementsByTagName("tr");
                            var tdObjs =  trObjs[0].getElementsByTagName("td");
                            var content = tdObjs[0].innerHTML;

                            var childLI=document.createElement("li");
                            childLI.id="drop"+dragLiId;
                            childLI.style.width='200px';
                            childLI.style.height='auto';
                            childLI.style.color='white';
                            childLI.className='navtitle-hover';

                            var tableStr="<table  id="+tableId+">";
                            tableStr+="<tbody>";
                            tableStr+="<tr align='center'>";
                            tableStr+="<td><a class='ui-icon ui-icon-close' href=javascript:deleteFavouriteDim('drop" + dragLiId + "')></a></td>"
                            tableStr+="<td style='color: black;'>"+content+"</td>";
                            tableStr+="</tr>";
                            tableStr+="</tbody>";
                            tableStr+="</table>";
                            childLI.innerHTML=tableStr;
                            dropUlObj[0].appendChild(childLI);
                            $("#"+dragLiId).remove()
                        }
                    })

                }

                $(document).ready(function(){
                    $(".sortable").sortable();



                    if ($.browser.msie == true){
                        $(".navigateDialog").dialog({
                            autoOpen: false,
                            height: 620,
                            width: 820,
                            position: 'justify',
                            modal: true
                        });
                        $("#favouriteParamsDialog").dialog({
                            autoOpen: false,
                            height: 380,
                            width: 420,
                            position: 'justify',
                            modal: true
                        });
                        $("#sqlStrDialog").dialog({
                            autoOpen: false,
                            height: 620,
                            width: 640,
                            position: 'justify',
                            modal: true
                        });

                        $("#favouriteParamsDiv").dialog({
                            //bgiframe: true,
                            autoOpen: false,
                            height: 400,
                            width: 550,
                            position: 'justify',
                            modal: true,
                            resizable:false
                        });
                        $("#viewFavouriteDiv").dialog({
                            //bgiframe: true,
                            autoOpen: false,
                            height: 350,
                            width: 300,
                            position: 'justify',
                            modal: true,
                            resizable:false
                        });
                    }
                    else{
                        $(".navigateDialog").dialog({
                            autoOpen: false,
                            height: 460,
                            width: 820,
                            position: 'justify',
                            modal: true
                        });
                        $("#favouriteParamsDialog").dialog({
                            autoOpen: false,
                            height: 200,
                            width: 430,
                            position: 'justify',
                            modal: true
                        });
                        $("#sqlStrDialog").dialog({
                            autoOpen: false,
                            height: 450,
                            width: 650,
                            position: 'justify',
                            modal: true
                        });

                        $("#favouriteParamsDiv").dialog({
                            //bgiframe: true,
                            autoOpen: false,
                            height: 400,
                            width: 550,
                            position: 'justify',
                            modal: true,
                            resizable:false
                        });
                        $("#viewFavouriteDiv").dialog({
                            //bgiframe: true,
                            autoOpen: false,
                            height: 350,
                            width: 300,
                            position: 'justify',
                            modal: true,
                            resizable:false
                        });

                    }
                });
                jQuery(document).ready(function()
                {
                    $("#breadCrumb").jBreadCrumb();
                });
                $(document).ready(function() {

                    var currentTime = new Date()
                       var month = currentTime.getMonth() + 01
                       var day = currentTime.getDate()
                       var year = currentTime.getFullYear()
                        if($("#cdate").val()==""){
                      if('<%=ddformT%>'=='null' || '<%=ddformT%>'=='dd/mm/yy' ){
                          $("#cdate").val(day + "/" + month + "/" + year);
                        }
                       else{
                            $("#cdate").val(month + "/" + day + "/" + year);
                         $('#cdate').datepicker()
		         $("#cdate").datepicker( "option", "dateFormat", '<%=ddformT%>');
                       }
                       }

                    $("#repTemTree").treeview({
                        animated: "normal",
                        unique:true
                    });
                    /*
                 $("#repTemTree2").treeview({
                    animated: "normal",
                    unique:true
                });
                     */
                    $("#graphTree").treeview({
                        animated: "normal",
                        unique:true
                    });
                    $("#tableTree").treeview({
                        animated: "normal",
                        unique:true
                    });
                    $(".column").sortable({
                        connectWith: '.column'
                    });

                    $("#custmemDispDia").dialog({
                        // bgiframe: true,
                        autoOpen: false,
                        height: 650,
                        width: 550,
                        modal: true
                    });
                    $("#paramSecurity").dialog({
                        // bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 600,
                        modal: true
                    });
                    $("#paramDefaultVal").dialog({
                        // bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 600,
                        modal: true
                    });

                });
                function contMenu(Obj){

                    //                alert('document.getElementById("repExc3").value is : '+document.getElementById("repExc3").value)

            <%--alert('document.getElementById("repExc3").value is : '+document.getElementById("repExc3").value)--%>

                        if(document.getElementById("repExc3").value==''){
                            var childUL=document.getElementById("parampotionsListMenu");
                            var LIchld=childUL.getElementsByTagName("li");
                            childUL.removeChild(LIchld[2]);
                            var childLI=document.createElement("li");
                            childLI.id="ExclREP";
                            childLI.className='addcomboBox';
                            var a=document.createElement("a");
                            a.href="#excludeREP";
                            a.innerHTML="Exclude from Row Edge";
                            childLI.appendChild(a);
                            childUL.appendChild(childLI);
            <%--alert('LIchld[2] is : '+(childUL.removeChild(LIchld[2])))--%>
            <%--document.getElementById("ExclREP").innerHTML=a;--%>
                        }else{

                            //                    alert('repexc3 is : '+document.getElementById("repExc3").value)
                            var repIncl=document.getElementById("repExc3").value;
                            var repInclId=repIncl.split(",");
                            for(var i=0;i<repInclId.length;i++){
            <%--alert('Obj.id.split("-")[1] is : '+Obj.id.split("-")[1])--%>
            <%--alert('repInclId[i] is : '+repInclId[i])--%>

                                if(Obj.id.split("-")[1]==repInclId[i]){
                                    var childUL=document.getElementById("parampotionsListMenu");
                                    var LIchld=childUL.getElementsByTagName("li");
                                    childUL.removeChild(LIchld[2]);
                                    var childLI=document.createElement("li");
                                    childLI.id="ExclREP";
                                    childLI.className='addcomboBox';
                                    var a=document.createElement("a");
                                    a.href="#includeREP";
                                    a.innerHTML="Include to Row Edge";
                                    childLI.appendChild(a);
                                    childUL.appendChild(childLI);
            <%--document.getElementById("ExclREP").innerHTML=a;--%>
                                    break;
                                }else{
                                    var childUL=document.getElementById("parampotionsListMenu");
                                    var LIchld=childUL.getElementsByTagName("li");
                                    childUL.removeChild(LIchld[2]);
                                    var childLI=document.createElement("li");
                                    childLI.id="ExclREP";
                                    childLI.className='addcomboBox';
                                    var a=document.createElement("a");
                                    a.href="#excludeREP";
                                    a.innerHTML="Exclude from Row Edge";
                                    childLI.appendChild(a);
                                    childUL.appendChild(childLI);
            <%--document.getElementById("ExclREP").innerHTML=a;--%>
                                }
                            }
                        }
                        if(document.getElementById("cepExc3").value==''){
                            var childUL=document.getElementById("parampotionsListMenu");
                            var LIchld=childUL.getElementsByTagName("li");
                            childUL.removeChild(LIchld[2]);
                            var childLI=document.createElement("li");
                            childLI.id="ExclCEP";
                            childLI.className='addcomboBox';
                            var a=document.createElement("a");
                            a.href="#excludeCEP";
                            a.innerHTML="Exclude from Column Edge";
                            childLI.appendChild(a);
                            childUL.appendChild(childLI);
            <%--document.getElementById("ExclCEP").innerHTML='<a href="#excludeCEP">Exclude from Column Edge</a>';--%>
                        }else{
                            //                    alert('cepexc3 is : '+document.getElementById("cepExc3").value)
                            var cepIncl=document.getElementById("cepExc3").value;
                            var cepInclId=cepIncl.split(",");
                            //                    alert('repinclid lenght is : '+repInclId.length)
                            for(var i=0;i<cepInclId.length;i++){
                                //                        alert('Obj.id.split("-")[1] is : '+Obj.id.split("-")[1])
                                //                        alert('cepInclId[i] is : '+cepInclId[i])
                                if(Obj.id.split("-")[1]==cepInclId[i]){
                                    var LIchld=childUL.getElementsByTagName("li");
                                    childUL.removeChild(LIchld[2]);
                                    var childLI=document.createElement("li");
                                    childLI.id="ExclCEP";
                                    childLI.className='addcomboBox';
                                    var a=document.createElement("a");
                                    a.href="#includeCEP";
                                    a.innerHTML="Include to Column Edge";
                                    childLI.appendChild(a);
                                    childUL.appendChild(childLI);
            <%--document.getElementById("ExclCEP").innerHTML='<a href="#includeCEP">Include to Column Edge</a>';--%>
                                    break;
                                }else{
                                    var LIchld=childUL.getElementsByTagName("li");
                                    childUL.removeChild(LIchld[2]);
                                    var childLI=document.createElement("li");
                                    childLI.id="ExclCEP";
                                    childLI.className='addcomboBox';
                                    var a=document.createElement("a");
                                    a.href="#excludeCEP";
                                    a.innerHTML="Exclude from Column Edge";
                                    childLI.appendChild(a);
                                    childUL.appendChild(childLI);
            <%--document.getElementById("ExclCEP").innerHTML='<a href="#excludeCEP">Exclude from Column Edge</a>';--%>
                                }
                            }
                        }
                        $("#"+Obj.id).contextMenu({
                            menu: 'parampotionsListMenu'
                        }, function(action, el, pos) {
                            contextMenuParamWork(action, el, pos);
                        });
                        /*  $("#"+Obj.id).contextMenu({
                    menu: 'myMenu'
                },function(){

                });
                         */
                    }
                    function createReport(){
                       var REPORTID=document.getElementById('REPORTID').value;
                        var graphTableHidden = document.getElementById("graphTableHidden").value
                        $.ajax({
                            url: 'reportTemplateAction.do?templateParam=checkRportNGraph&REPORTID='+REPORTID,
                            success: function(data){
                                //alert(data)
                                if(data=='Please Select Table'){
                                    alert(data)
                                    return false;
                                }else if(data=='Please Select Graph'){
                                    alert(data)
                                    return false;
                                }else if(data=='Please Select Graph Columns To Add Graph'){
                                    alert(data)
                                }else if(data=='Please Select Table and Graph'){
                                    alert(data);
                                    return false;
                                    //alert("else every thing satisfied");
                                    /* document.myForm2.action="reportTemplateAction.do?templateParam=saveReport";
                            document.myForm2.submit();*/
                                }else if(data=='3'){
                                    alert("Please Select Table and Graph");
                                    return false;
                                }else if(data=='4'){
                                    alert("please Select table");
                                    return false;
                                }else if(data=='5'){
                                    alert("please select measures");
                                    return false;
                                }else{
                                    //  alert("every thing satisfied");
                                    document.myForm2.action="reportTemplateAction.do?templateParam=saveReport&graphTableHidden="+graphTableHidden;
                                    document.myForm2.submit();
                                }
                            }
                        });

                    }
                    //                    function gohome(){
                    //                        document.forms.myForm2.action="baseAction.do?param=goHome";
                    //                        document.forms.myForm2.submit();
                    //                    }
                    //                    function logout(){
                    //                        document.forms.myForm2.action="baseAction.do?param=logoutApplication";
                    //                        document.forms.myForm2.submit();
                    //                    }
                    function cancelReport(){
                        document.forms.myForm2.action="baseAction.do?param=goHome";
                        document.forms.myForm2.submit();
                    }


                    function viewDashboardG(path){
                        document.forms.myForm2.action=path;
                        document.forms.myForm2.submit();
                    }
                    function viewReportG(path){
                        document.forms.myForm2.action=path;
                        document.forms.myForm2.submit();
                    }

                    //                    function goGlobe(){
                    //                        $(".navigateDialog").dialog('open');
                    //                    }
                    //                    function closeStart(){
                    //                        $(".navigateDialog").dialog('close');
                    //                    }
                    function goPaths(path){
                        parent.closeStart();
                        document.forms.myForm2.action=path;
                        document.forms.myForm2.submit();
                    }


                    //for adding custom measure by bharathi reddy
                    function contextMenuWorkCustMeasure(action, el, pos) {

                        switch (action) {

                            case "addCustMeasure":
                                {
                                    addCustomMeasure();
                                    break;
                                }


                        }
                    }

                    function createCustMeasure(Obj){

                        $("#"+Obj.id).contextMenu({
                            menu: 'myMenu'
                        },function(action, el, pos) {

                            contextMenuWorkCustMeasure(action, el, pos);
                        });

                    }

                    function addCustomMeasure(reportId)
                    {
                        var ctxPath='<%=request.getContextPath()%>'
                        $.ajax({
                            url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&folderIds='+buildFldIds()+'&reportId='+reportId+'&from=designer',
                            success:function(data) {
                                $("#custmemDispDia").dialog('open');
                            }
                        });
                        //  var f=document.getElementById('custmemDisp');

                        //var s="createCustMember.jsp?folderIds="+buildFldIds()+"&reportId="+reportId;
                        //f.src=s;

                        // document.getElementById('custmemDisp').style.display='block';
                        //  document.getElementById('fade').style.display='block';

                    }
                    function cancelCustMember(){
                        //document.getElementById('custmemDisp').style.display='none';
                        // document.getElementById('fade').style.display='none';
                        $("#custmemDispDia").dialog('close');

                    }
                    function cancelCustMembersave(columnname){
                        // document.getElementById('custmemDisp').style.display='none';
                        // document.getElementById('fade').style.display='none';
                        $("#custmemDispDia").dialog('close');

                        var branches = $("<li><img src='icons pinvoke/table.png' ><span >&nbsp;"+columnname+"</span></li>").appendTo("#customMeasure");
                        $("#customMeasure").treeview({
                            add: branches
                        });


                    }


                    //ends

                    function contextMenuParamWork(action, el, pos) {

                        switch (action) {

                            case "addDefaultValue":
                                {

                                    var elementId=el.attr('id').split("-")[1];
                                    addParamDefaultValues(elementId);
                                    break;
                                }

                            case "addParamSecurity":
                                {

                                    var elementId=el.attr('id').split("-")[1];

                                    addParamSecurity(elementId);
                                    break;
                                }
                            case "addcomboBox":
                                {

                                    var elementId=el.attr('id').split("-")[1];

                                    break;
                                }
                            case "excludeCEP":{

                                    var elementId=el.attr('id').split("-")[1];
                                    var REPORTID=document.getElementById('REPORTID').value;
                                    var ExclCEPconfirm=confirm("Parameter Removed from Column Edge,Are you Sure");
                                    if(ExclCEPconfirm==true){
                                        $.ajax({
                                            url: 'reportTemplateAction.do?templateParam=excludeCEP&elementId='+elementId+'&REPORTID='+REPORTID+'&IncludeCEP=N',
                                            success: function(data){

                                                document.getElementById("cepExc").value=data;
                                                document.getElementById("cepExc3").value=data;
                                            }
                                        });
                                    }
                                    break;
                                }
                            case "includeCEP":
                                {
                                    var elementId=el.attr('id').split("-")[1];
                                    var REPORTID=document.getElementById('REPORTID').value;
                                    var InclCEPconfirm=confirm("Insert Parameter to Column Edge,Are you Sure");
                                    if(InclCEPconfirm==true){
                                        $.ajax({
                                            url: 'reportTemplateAction.do?templateParam=excludeCEP&elementId='+elementId+'&REPORTID='+REPORTID+'&IncludeCEP=Y',
                                            success: function(data){

                                                document.getElementById("cepExc").value=data.split("~")[1].replace("[","").replace("]","");
                                                document.getElementById("cepExc3").value='';
                                                document.getElementById("cepExc3").value=document.getElementById("cepExc").value;
                                            }
                                        });
                                    }
                                    break;

                                }

                            case "excludeREP":
                                {
                                    var elementId=el.attr('id').split("-")[1];
                                    var REPORTID=document.getElementById('REPORTID').value;
                                    var ExclREPconfirm=confirm("Parameter Removed from Row Edge,Are you Sure");
                                    if(ExclREPconfirm==true){
                                        $.ajax({
                                            url: 'reportTemplateAction.do?templateParam=excludeREP&elementId='+elementId+'&REPORTID='+REPORTID+'&IncludeREP=N',
                                            success: function(data){

                                                document.getElementById("repExc").value=data;
                                                document.getElementById("repExc3").value=data;
                                            }
                                        });
                                    }
                                    break;
                                }

                            case "includeREP":
                                {
                                    var elementId=el.attr('id').split("-")[1];
                                    var REPORTID=document.getElementById('REPORTID').value;
                                    var InclREPconfirm=confirm("Insert Parameter to Row Edge,Are you Sure");
                                    if(InclREPconfirm==true){
                                        $.ajax({
                                            url: 'reportTemplateAction.do?templateParam=excludeREP&elementId='+elementId+'&REPORTID='+REPORTID+'&IncludeREP=Y',
                                            success: function(data){

                                                document.getElementById("repExc").value=data.split("~")[1].replace("[","").replace("]","");
                                                document.getElementById("repExc3").value='';
                                                document.getElementById("repExc3").value=document.getElementById("repExc").value;
                                            }
                                        });
                                    }
                                    break;

                                }
                        }
                    }

                    function addParamSecurity(elementId){
                        var f=document.getElementById('paramSecurityDisp');
                        var REPORTID=document.getElementById('REPORTID').value;
                        var s="pbParamSecurity.jsp?elementId="+elementId+'&REPORTID='+REPORTID+"&ReportType=R";
                        f.src=s;

                        $("#paramSecurity").dialog('open');
                    }

                    function cancelParamSecurity(){

                        $("#paramSecurity").dialog('close');

                    }

                    function addParamDefaultValues(elementId){

                        var f=document.getElementById('paramDefaultValDisp');
                        var REPORTID=document.getElementById('REPORTID').value;

                        var s="pbParamDefaultValues.jsp?elementId="+elementId+'&REPORTID='+REPORTID+"&ReportType=R";
                        f.src=s;
                       
                        $("#paramDefaultVal").dialog('open');
                    }
                    function cancelParamdefSecurity(){
                        $("#paramDefaultVal").dialog('close');

                    }
                    function formatStr(EL,maxchars){
                        strbuff=EL.innerHTML;
                        newstr='';
                        startI = 0;
                        max=maxchars;
                        str='';
                        subarr=new Array(parseInt(strbuff.length/max+1));
                        for (i=0;i<subarr.length;i++)
                        {
                            subarr[i]=strbuff.substr(startI,max);
                            startI+=max;
                        }
                        for (i=0;i<subarr.length-1;i++)
                        {
                            newstr+=subarr[i]+'<br/>';
                        }
                        newstr+=subarr[subarr.length-1];
                        if(subarr.length==1){
                            EL.innerHTML=EL.innerHTML;
                        }else{
                            EL.innerHTML=newstr;
                        }
                    }
                    function closeColumnProperties(){
                        $("#columnPropertiesdiv").dialog('close');
                    }
                    function closeapplycolr(){
                        $("#applycolrdiv").dialog('close');
                    }
                    function getDisplayTablesInDesigner(ctxpath,paramslist,repId){
var frameObj=document.getElementById("dataDispmem");
//        getdimmap.getFact(Parameters);
        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
                var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+parent.buildFldIds()+'&REPORTID='+document.getElementById("REPORTID").value+'&tableList=true';
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
            //$("#tableList").attr('checked',true);
        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+parent.buildFldIds(),
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+parent.buildFldIds()+'&REPORTID='+document.getElementById("REPORTID").value;
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
                });

        }
    }
    function showListInDesigner(ctxpath,paramslist){
        if(document.getElementById("paramVals").style.display=='none'){
            $("#paramVals").show();
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+parent.buildFldIds(),
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);

                });
        }else{
           $("#paramVals").hide();
        }

    }
    function selectTablesInDesigner(tdId,tname){
        //alert(tname)
        document.getElementById(tdId).style.display='none';
        if($("#tabsListVals").val() == ""){
            $("#tabsListVals").val(tname)
            $("#tabsListIds").val(tdId)
        }else{
            var Ids = $("#tabsListIds").val()+","+tdId
            var value = $("#tabsListVals").val()+","+tname
            $("#tabsListIds").val(Ids)
            $("#tabsListVals").val(value)
        }
    }
    function setValueToContainerInDesigner(ctxpath,repId,bizRoles){
        var frameObj=document.getElementById("dataDispmem");
        $("#paramVals").hide();
        var tabLst = $("#tabsListIds").val();
        $("#tabsListVals").val('')
            $("#tabsListIds").val('')
        $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+parent.buildFldIds()+'&REPORTID='+document.getElementById("REPORTID").value;
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
                });
    }
        </script>
        <style type="text/css">
            .column { width:auto; float: left; padding-bottom: 5px; }
            .portlet { margin: 0 1em 1em 0;width:auto }
            .portlet-header { margin: 0.3em; padding-bottom: 4px; padding-left: 0.2em;width:auto }
            .portlet-header .ui-icon { float: right;width:auto }
            .portlet-content { padding: 0.4em;width:auto; }
            .ui-sortable-placeholder { border: 1px dotted black; visibility: visible !important; height: 50px !important; }
            .ui-sortable-placeholder * { visibility: hidden; }
            .ajaxboxstyle {
                position: absolute;
                background-color: #FFFFFF;
                text-align: left;
                border: 1px solid #000000;
                border-top-width:1px;
                height:150px;
                width:230px;
                overflow:auto;
                overflow:hidden;
                margin:0em 0.5em;
                z-index: 9999999;
            }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            .divClass{
                display:none;width:auto;height:auto;background-color:#ffffff;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;
            }
            .white_content {
                display: none;
                position: absolute;
                top: 15%;
                left: 25%;
                width: 550px;
                height:650px;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }
            .black_start{
                display:none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                overflow:auto;
            }
            .paramRegion{
                background-color:#e6e6e6;
            }
            a {font-family:Verdana;cursor:pointer;color:#369;font-size:<%=anchorFont%>px}
            *{font:<%=pageFont%>px verdana}
            #reportName{text-decoration:underline}
            .flagDiv{
                width:auto;
                height:auto;
                background-color:#ffffff;
                overflow:auto;
                position:absolute;
                text-align:left;
                border:1px solid #000000;
                border-top-width: 0px;
                z-index:1001
            }

            #ui-datepicker-div
            {
                z-index: 9999999;
            }


        </style>
        <script type="text/javascript">
                graphIds ="<%=graphIds%>";
                graphTableIds = "<%=graphIds%>";
        </script>
    </head>
    <!-- <body class="ReportTemplate" >-->
    <body class="body">
        <table style="width:100%;">
            <tr>
                <td valign="top" style="width:50%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <form name="myForm2" method="post" action="">
            <%String Pagename = reportName;
                                    //String url = request.getAttribute("reportdesignerurl").toString();
                                    //brdcrmb.inserting(Pagename, url);

            %>

            <input type="hidden" name="PrevParams" id="PrevParams" value="<%=prevParamArray%>">
            <input type="hidden" name="PrevTimeParams" id="PrevTimeParams" value="<%=prevTimeParams%>">
            <input type="hidden" name="REPORTID" id="REPORTID" value="<%=ReportId%>">
            <input type="hidden" name="REPORTNAMEbeforeSave" id="REPORTNAMEbeforeSave" value="<%=reportName%>">
            <table width="100%" >
                <tr>
                </tr>

                <tr style="height:15px;width:100%;max-height:100%">
                    <td>
                        <table width="100%" class="ui-corner-all">
                            <tr>
                                <td style="height:10px;width:10%" >
                                    <%
                                                            com.progen.reportview.action.showReportName repname = new com.progen.reportview.action.showReportName();
                                                            ArrayList repNameList = repname.buildReportName(reportName);
                                                            int fntsize = anchorFont + 1;
                                                            for (int i = 0; i < repNameList.size(); i++) {
                                                                //String jsReportName=reportName.replace("\"", "\\\"");
                                                                //String jsReportDesc=reportDesc.replace("\"", "\\\"");
                                                                // String jsReportName=reportName;
                                                                //String jsReportDesc=reportDesc;
                                                               // String jsReportName = reportName.replace("'", "\\'");
                                                                //String jsReportDesc = reportDesc.replace("'", "\\'");
                                                                //

                                    %>

                                    <span id="reportName"  style="color: #4F4F4F;font-family:verdana;font-size:<%=fntsize%>px;font-weight:bold;text-decoration:none"   title="<%=reportName%>"><%=repNameList.get(i)%></span>&nbsp;<a href="javascript:void(0)"  style="text-decoration:underline" onclick=changeReportName()>edit</a>
                                    <%
                                                                                                   //jsReportName = reportName.replace("\\'", "'");
                                                                                                  // jsReportDesc = reportDesc.replace("\\'", "'");
                                    %>
                                    <Input TYPE="hidden" name="reportNameH" id="reportNameH" value="<%=reportName%>">
                                    <Input TYPE="hidden" name="reportDescH" id="reportDescH" value="<%=reportDesc%>">

                                    <br/>
                                    <%}%>
                                </td>
<!--                                <td valign="top" style="height:10px;width:30%">
                                    <div id='breadCrumb' class='breadCrumb module' style="width:500px">
                                        <ul>
                                            <li style="display:none;"></li>
                                            <li style="display:none;"></li>
                                            <% String pgnam = "";
                                                                    if (brdcrmb.getPgname1() != null) {
                                                                        pgnam = brdcrmb.getPgname1().toString();

                                                                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname1()%>
                                            </li>

                                            <%
                                                                                                                    } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl1()%>'><%=brdcrmb.getPgname1()%></a>
                                            </li>
                                            <%
                                                                        }
                                                                    }
                                                                    if (brdcrmb.getPgname2() != null) {
                                                                        pgnam = brdcrmb.getPgname2().toString();

                                                                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname2()%>
                                            </li>
                                            <%
                                                                                                                    } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl2()%>'><%=brdcrmb.getPgname2()%></a>
                                            </li>
                                            <%                    }
                                                                    }
                                                                    if (brdcrmb.getPgname3() != null) {
                                                                        pgnam = brdcrmb.getPgname3().toString();
                                                                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname3()%>
                                            </li>
                                            <%
                                                                                                                    } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl3()%>'><%=brdcrmb.getPgname3()%></a>
                                            </li>
                                            <%
                                                                        }
                                                                    }
                                                                    if (brdcrmb.getPgname4() != null) {
                                                                        pgnam = brdcrmb.getPgname4().toString();
                                                                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname4()%>
                                            </li>
                                            <%
                                                                                                                    } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl4()%>'><%=brdcrmb.getPgname4()%></a>
                                            </li>
                                            <%
                                                                        }
                                                                    }
                                                                    if (brdcrmb.getPgname5() != null) {
                                                                        pgnam = brdcrmb.getPgname5().toString();
                                                                        if (pgnam.equalsIgnoreCase(Pagename)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname5()%>
                                            </li>
                                            <%
                                                                                                                    } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl5()%>'><%=brdcrmb.getPgname5()%></a>
                                            </li>
                                            <%
                                                                        }
                                                                    }
                                            %>
                                            <li style="display:none;"></li>
                                            <li style="display:none;"></li>
                                        </ul>
                                    </div>
                                    <div class="chevronOverlay main"></div>
                                </td>-->
                            <input type="hidden" name="sqlString" value="" id="sqlString">
                            <td style="height:10px;width:20%" align="right">
                                <!--                                <a href="javascript:void(0)" onclick="javascript:goGlobe()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Navigation </a> |
                                                                <a href="javascript:void(0)" onclick="javascript:gohome('<%=loguserId%>')" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Home </a> |-->
                                <%-- <a href="#" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Help </a> |--%>
                                <%if (showExtraTabs) {%>
                                <%--<a href="bugDetailsList.jsp" style="font-size:10px;color:#2191C0;;font-weight:bold;text-decoration: none;font-family:Georgia"> Bug </a> |--%>
                                <%}%>
                                <!--                                <a href="javascript:void(0)" onclick="javascript:logout()" style="font-size:10px;color:#2191C0;font-weight:bold;text-decoration: none;font-family:Georgia"> Logout </a>-->
                            </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table style="width:100%" border="solid black 1px" >
    <tr>
        <td width="15%" valign="top" class="draggedTable1" >
            <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all">
                &nbsp;<font style="font-weight:bold" face="verdana" size="1px">Report Designer Menu</font>
            </div>
            <div class="masterDiv" style="overflow: auto;height: 400px">
                <ul id="repTemTree" class="filetree treeview-famfamfam">
                    <li class="closed" ><img alt=""  src="icons pinvoke/folder-horizontal.png" />&nbsp;<span>Business Roles</span>
                        <ul id="userFlds" class="background">
                            <%=UserFldsData%>
                        </ul>
                    </li>

                    <li class="closed"><img alt=""  src="icons pinvoke/folder-horizontal.png" />&nbsp;<span>Dimensions</span>
                        <ul id="userDims" class="background">
                        </ul>
                    </li>


                    <li class="closed">
                        <ul id="favourParams" class="background">

                        </ul>
                    </li>
                </ul>

            </div>
        </td>

        <td  valign="top" width="85%" >
            <table style="height:auto;max-height:100%;width:100%"  border="0">
                <tr>
                    <td valign="top" width="100%" height="100px" class="paramRegion">
                        <table style="height:100%" border="0" class="draggedTable"  id="newDragTables">
                            <tr id="dragDims">
                                <td style="height:100%" id="draggableDims" valign="top">
                                    <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                        &nbsp;&nbsp;<font size="2" style="font-weight:bold">Drag Parameters To Here </font> &nbsp;&nbsp;<a id="showParams" href="javascript:showParams()" title="Click to Set Parameters" >Preview</a>
                                       &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                                      &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                                     &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                                     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                                          <a id="clearParams" href="javascript:clearParams()" title="Clear Parameters" >Clear</a>
                                    </h3>
                                    <div id="dragDiv" style="min-width:800px;min-height:100px;max-width:100%">
                                        <ul id="sortable" style="width:800px;max-width:100%">
                                            <%=ParamRegion%>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <tr id="favParams">
                                <td align="right"  id="favParams">
                                    <input type="button" align="right" id="saveFavParams" name="saveFavParams" disabled value="Save as Favourite" onclick="saveFavouriteParams()">
                                </td>
                            </tr>
                </tr>
                <tr id="lovParams" style="height:100%;display:none">
                    <td valign="top" style="height:100%">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            &nbsp;&nbsp;<font size="2" style="font-weight:bold">Parameters</font>&nbsp;&nbsp;<a id="editParams" href="javascript:showMbrs()" title="Click to Edit Parameters" >Edit</a>
                        </h3>
                        <div align="left" id="paramDisp" style="min-width:800px;min-height:100px;max-width:100%">
                            <%=ParamDispRegion%>
                        </div>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
    <tr id="GraphsRegOfReport">
        <td align="left" width="100%" valign="top">
            <h3 style="height:20px;width:100%" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                &nbsp;&nbsp;<font size="2" style="font-weight:bold">Graphs </font> &nbsp;&nbsp;
                <a href="javascript:void(0)" onclick="showGraphs()" title="Click Here To Add Graphs">Add Graphs</a>&nbsp;&nbsp;
                <a href="javascript:void(0)" onclick="showGraphTable()" title="Click Here To Add Graph With Table">Add Graph With Table</a>&nbsp;&nbsp;
                <a href="javascript:void(0)" id="editGraphs" title="Preview Graphs" onclick="previewGraphs()">Preview</a>
            </h3>
            <table style="height:100%" class="draggedTable" border="0" >
                <tr id="previewGraph">
                    <td  style="height:100%" valign="top" >
                        <div id="previewDispGraph" style="width:100%;height:auto;max-height:100%">
                            <%=GraphRegion%>
                        </div>

                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td align="left" width="100%" height="180px" valign="top">
            <table style="height:100%" class="draggedTable" border="0" >
                <tr id="editTable">
                    <td style="height:250px" valign="top">
                        <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            &nbsp;&nbsp;<font size="2" style="font-weight:bold">Table </font>&nbsp;&nbsp;<a id="prevTab"  href="javascript:PreviewTable()" title="Preview Table" >Preview</a>
                        </h3>

                        <div id="tableDiv" style="width:100%;min-height:230px;">
                            <Table>
                                <Tr>
                                    <Td>

                                        <table>
                                            <tr>
                                                <td>
                                                    <a id="rep"  href="javascript:void(0)"  onclick="showRowParams2()"    title="Click to Select Row Parameters" >Row Edge</a>&nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div style="display:none;width:100px;height:100px;overflow:auto;color:black;position:absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;" id="repDiv">
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </Td>
                                    <Td>
                                        <table>
                                            <tr>
                                                <td>
                                                    <a id="cep"  href="javascript:void(0)"  onclick="showColParams2()"   title="Click to Select Column Parameters"  >Column Edge</a>&nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div style="display:none;width:100px;height:100px;overflow:auto;position: absolute;text-align: left;border: 1px solid #000000;border-top-width: 0px;" id="cepDiv">
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </Td>
                                    <Td>
                                        <a id="Measure"  href="javascript:void(0)" onclick="showMeasures()" title="Click to Select Measures" >Measures</a>&nbsp;
                                    </Td>
                                </Tr>
                            </Table>
                        </div>

                    </td>
                </tr>
                <tr id="previewTable" style="display:none;">
                    <td  style="height:250px" valign="top" >
                        <h3  style="height:20px;width:100%" align="left"  tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Table</font>
                            &nbsp;&nbsp;<a id="prevTab"  href="javascript:EditTable()" title="Click to Edit Table" >Edit</a>

                        </h3>

                        <iframe id="iframe1" style="width:100%;height:320px;overflow:auto"></iframe>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</td>
</tr>
</table>


<div id="measuresDialog" style="display:none" title="Add Measures">
    <table><tr><td>
            <input id="tableList" type="checkbox" onclick="getDisplayTablesInDesigner('<%=request.getContextPath() %>','','<%=ReportId%>')">All</td>
                    <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                        <div id="paramVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showListInDesigner('<%=request.getContextPath() %>','')" ></a></td>
                    <td id="goButton" onclick="setValueToContainerInDesigner('<%=request.getContextPath() %>','<%=ReportId%>')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>
    <iframe id="dataDispmem" NAME='dataDispmem' frameborder="0" width="100%" height="100%" SRC='#'></iframe>
</div>

<input type="hidden" name="allGraphIds" value="" id="allGraphIds">
<input type="hidden" name="allGraphTableIds" value="" id="allGraphTableIds">
<input type="hidden" name="graphTableHidden" value="" id="graphTableHidden">
<input type="hidden" name="REPIds" value="<%=prevREP%>" id="REPIds">
<input type="hidden" name="CEPIds" value="<%=prevCEP%>" id="CEPIds">
<input type="hidden" name="MsrIds" value="<%=prevMeasures%>" id="MsrIds">
<input type="hidden" name="Measures" value="<%=prevMeasureNames%>" id="Measures">
<input type="hidden" name="repExc" value="<%=prevRepExclude%>" id="repExc">
<input type="hidden" name="cepExc" value="<%=prevCepExclude%>" id="cepExc">
<input type="hidden" name="graphColumns" value="" id="graphColumns">
<input type="hidden" name="currGrpColId" value="" id="currGrpColId">
<input type="hidden" name="repExc3" id="repExc3">
<input type="hidden" name="cepExc3" id="cepExc3">
<input type="hidden" id="Designer" name="Designer" value="">


<div id="fade" class="black_overlay"></div>

<!--new graphs list div starts here-->
<div id="graphList" title="Add Graphs" style="display:none">
    <table width="100%">
        <tbody>
            <%
                                    ProGenChartUtilities utilities = new ProGenChartUtilities();
                                    //String str = utilities.buildGraphTypesDiv(request.getContextPath(), grpTypeskeys, GraphTypesHashMap, "getGraphName");
                                    String str = utilities.buildGraphTypesForRD(request.getContextPath(), grpTypeskeys, GraphTypesHashMap, "getGraphName");
                                    //
            %>
            <%=str%>
        </tbody>
    </table>
</div>
<div id="graphTableList" title="Add Graphs" style="display:none">
    <table width="100%">
        <tbody>
            <%
                                    String graphTable = utilities.buildGraphTypesForRD(request.getContextPath(), grpTypeskeys, GraphTypesHashMap, "getGraphTable");
                                    //
            %>
            <%=graphTable%>
        </tbody>
    </table>
</div>
<input type="hidden"  id="h" value="<%=request.getContextPath()%>">
<div id="graphCols" STYLE="display:none" title="Graph Columns">
    <iframe  id="graphColsFrame" NAME='bucketDisp' frameborder="0" width="100%" height="100%" SRC=''></iframe>
</div>
<div id="graphDtlsDiv" title="Graph Details" style="display:none">
    <iframe  id="graphDtls" NAME='graphDtls' frameborder="0" width="100%" height="100%"  SRC='#'></iframe>
</div>
<div>
    <iframe  id="dispRowValues" NAME='dispRowValues'  class="white_content1" STYLE='display:none;' SRC=''></iframe>
</div>
<table width="100%" >
    <tr>
        <td height="10px">&nbsp;</td>
        <td height="10px">&nbsp;</td>
    </tr>
    <tr>
        <td height="10px">&nbsp;</td>
        <td align="center">
            <% if (PrivilegeManager.isModuleEnabledForUser("MAP", Integer.parseInt(loguserId))) {%>
<!--            <input type="button" id="enableMapId"class="navtitle-hover" value="Enable Map" mapEnabled="true" style="width:auto" onclick="javascript:enableMap();">-->
            <%}%>
            &nbsp;&nbsp;<input type="button" class="navtitle-hover" value="Next" onclick="return checkReportNameatRoleLevel()" style="width:auto">&nbsp;&nbsp;<input type ="button" class="navtitle-hover"title="Click here to reset"value="Reset" onclick="return resetCurrRep()"style="width:auto">&nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="javascript:cancelReport();"></td>
    </tr>
    <tr>
        <td height="10px">&nbsp;</td>
        <td height="10px">&nbsp;</td>
    </tr>
</table>
<table style="width:100%">
    <tr>
        <td valign="top" style="width:100%;">
            <jsp:include page="Headerfolder/footerPage.jsp"/>
        </td>
    </tr>
</table>
<!--<div id="reportstart" class="navigateDialog" title="Navigation" style="display:none">
    <iframe src="startPage.jsp" frameborder="0" height="100%" width="800px" ></iframe>
</div>-->
<!--
<div id="dispTabProp" title="Table Properties" style="display:none">
    <iframe id="dispTabPropFrame" NAME='dispTabPropFrame' width="100%" height="100%"  frameborder="0" SRC='#'></iframe>
</div>-->

<div id="fadestart" class="black_start"></div>
<div id="showExports" title="Exports" style="display:none">
    <iframe id="showExportsFrame" NAME='showExportsFrame' width="100%" height="100%"  frameborder="0" SRC='#'></iframe>
</div>

<ul id=custMeasureListMenu" class="contextMenu" style="width:150px;text-align:left">
    <li class="addCustMeasure"><a href="#addCustMeasure">Add Measure</a></li>
</ul>
<ul id="parmsMenu" class="contextMenu" style="width:205px;height:100px;text-align:left;">
    <li class="deleteFavParams"><a href="#deleteFavParams">Delete Favourite</a></li>
</ul>

<div style="display:none" id="custmemDispDia" title="Custom Measures">
    <iframe  id="custmemDisp" NAME='custmemDisp' height="100%" width="100%" frameborder="0" SRC='createCustMemberinviewer.jsp'></iframe>
</div>

<div style="display:none" id="paramDefaultVal" title="Parameter Default Values">
    
    <iframe  id="paramDefaultValDisp" NAME='paramDefaultValDisp' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
</div>
<div style="display:none" id="paramSecurity" title="Parameter Security">
    <iframe  id="paramSecurityDisp" NAME='paramSecurityDisp' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
</div>

<!--<div style="display:none" id="defaultDate" title="Set Date">
    <iframe  id="defaultDateDisp" NAME='defaultDateDisp' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
</div>-->



<div style="display:none" id="customDate" title="Set Date" >
    <div>
        <table>
            <tr>
                <td align="center" >Date:</td>
                <td>
                    <input type="text" id="cdate" name="cdate" class="datePicker" value="">
                </td>
                <td> <input type="button" name="save" class="navtitle-hover" value="save" onclick="rpsavedate('<%=ReportId%>')"></td>
            </tr>
        </table>
        <input type="hidden" id="dateType" name="dateType" value="">
    </div>
</div>

<div style="display:none" id="defaultAggregation" title="Set Date">
    <iframe  id="defaultAggregationDisp" NAME='defaultAggregationDisp' height="100%" width="100% " scrolling="no" frameborder="0" SRC='#'></iframe>
</div>

<div style="display:none" id="customAggregation" title="Set User Defined Aggregation">
    <iframe  id="customAggregationDisp" NAME='customAggregationDisp' height="100%" width="100% " scrolling="no" frameborder="0" SRC='#'></iframe>
</div>

<div style="display:none;height:330px;" id="rowParamDisplay" title="Row Parameters">
    <table style="width:100%;height:auto" border="1">
        <tr>
            <td width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Available Row Parameters</font></div>
                <div style="height:230px;overflow:auto">
                    <ul id="availableRowParamSortable" class="availableRowParameters">

                    </ul>
                </div>
            </td>
            <td width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Existing Row Parameters</font></div>
                <div id="dropingrowprms" style="height:230px;overflow:auto">
                    <ul id="existingRowParamSortable"  class="existingRowParameters">

                    </ul>
                </div>
            </td>
        </tr>
    </table>

    <center><input type="button" name="Done" class="navtitle-hover" value="Done" onclick="getRowEdgeParams2()"></center>


</div>
<div style="display:none" id="colParamDisplay" title="Column Parameters">
    <table style="width:100%;height:auto" border="1">
        <tr>
            <td width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Available Column Parameters</font></div>
                <div style="height:230px;overflow:auto">
                    <ul id="availableColParamSortable" class="availableColParameters">

                    </ul>
                </div>
            </td>
            <td width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Existing Column Parameters</font></div>
                <div id="dropingcolprms" style="height:230px;overflow:auto">
                    <ul id="existingColParamSortable"  class="existingColParameters">

                    </ul>
                </div>
            </td>
        </tr>
    </table>
    <center><input type="button" name="Done" class="navtitle-hover" value="Done" onclick="getColumnEdgeParams2()"></center>
</div>
<div id="changeNameDialog" title="Edit Report Name" style="display:none">
    <table width="100%" align="center">
        <tr>
            <td>Report Name :</td>
            <td><input type="text" name="repName2" style="font:11px verdana;background-color:white" id="repName2"></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><span  style="font-family:verdana;color:red;font-size:11px;background-color:white" id="repMsg"></span></td>
        </tr>
        <tr>
            <td>Report Description :</td>
            <td><input type="text" name="repDesc2" style="font:11px verdana;background-color:white" id="repDesc2"></td>
        </tr>
        <tr>
            <td colspan="2" style="width:10px">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="button" value="Done" class="navtitle-hover" onclick="changeReportNameTemp();"></td>
        </tr>
    </table>
</div>
<div id="applycolrdiv" title="Apply Color Based Grouping" style="display:none">
    <iframe id="applycolorframe" name="applycolorframe" frameborder="0" marginheight="0" marginwidth="0" src='#'width="100%" height="100%"></iframe>
</div>
<div id="tableColsDialog" title="Add Measures" style="display:none" >
    <iframe  id="tableColsFrame" NAME='tableColsFrame' width="100%" height="100%" frameborder="0"   SRC='TableDisplay/PbChangeTableColumnsRT.jsp'></iframe>
</div>
<div id="dispTabProp" title="Table Properties" style="display:none">
    <iframe id="dispTabPropFrame" NAME='dispTabPropFrame' width="100%" height="100%"  frameborder="0" SRC='#'></iframe>
</div>
<div id="paramFilterDiv" title="Parameter Filter" style="display:none">
    <iframe  id="paramFilter" NAME='paramFilter' width="100%" height="100%" frameborder="0"   SRC='#'></iframe>
</div>
<div id="paramFilterMemberDiv" title="Parameter Members" style="display:none">
    <iframe  id="paramFilterMember" NAME='paramFilterMember' width="100%" height="100%" frameborder="0"   SRC='#'></iframe>
</div>

<div id="createSegmentDiv" style="display: none" title="Segment" align="center">

    <br>
    <table border="0">
        <tr>
            <th>Measure Name  :  </th>
            <td id="measurename"></td>

        </tr>

        <tr>
            <th>Maximum Value  :  </th>
            <td id="maximumvalue"> </td>

        </tr>
        <tr>
            <th>Minimum Value  :  </th>
            <td id="minimumvalue"></td>

        </tr>
        <tr>
            <th>Average Value  :  </th>
            <td id="averagevalue"> </td>

        </tr>
    </table>
    <br>

    <br>

    <table id="segmentTable" align="center">
        <thead>
            <tr >
                <th style="background-color: rgb(180, 217, 238); font-size: small;">
                    Segment Name
                </th >
                <th style="background-color: rgb(180, 217, 238); font-size: small;">
                    Lower Limit
                </th>
                <th style="background-color: rgb(180, 217, 238); font-size: small;">
                    Upper Limit
                </th>
            </tr>
        </thead>

        <tbody  id="segmentvalues">
            <tr>
                <td>
                    <input type="text" name="segmentInput0" value="" id="segmentInput0">
                </td>
                <td>
                    <input type="text" name="minInput0"  id="minInput0">
                </td>
                <td>
                    <input type="text" name="maxInput0"  id="maxInput0">
                </td>
            </tr>
        </tbody>

    </table>
    <br>
    <table align="center">
        <tr align="center" >
            <td colspan="1">
                <input type="button" class="navtitle-hover" style="width:auto" value="Add Row" onclick="addsegSingleRow()"/>

            </td >
            <td colspan="1">
                <input type="button"   class="navtitle-hover" style="width:auto" value="Delete Row" onclick="deleteSegSingleRow()" />
            </td>
            <td colspan="1">
                <input type="button"   class="navtitle-hover" style="width:auto" value="Go" onclick="saveSegmentValues('<%=request.getContextPath()%>')">
            </td>
        </tr>
    </table>
    <input type="hidden" id="segmentReportId">
    <input type="hidden" id="segmentMeasureId">
</div>
<div id="createSegmentDialogDiv" style="display: none" title="CreateSegment" align="center">
    <br>
    <table border="0">
        <tr>
            <td><b>Select Measures :  </b></td>
            <td>
                <select style="width: 130px;" align="right" name="SegmentMeasures"class="myTextbox5" id="SegmentMeasures" >
                </select>
            </td>
        </tr>
    </table>
    <br> <br>
    <input type="button" class="navtitle-hover" value="Save" onclick="openCreateSegmentDialog('<%=ReportId%>','<%=request.getContextPath()%>')" >
</div>
<div id="columnPropertiesdiv" title="Column Properties" style="display:none">
    <iframe id="columnPropertiesframe" name="columnPropertiesframe" frameborder="0" marginheight="0" marginwidth="0" src='#'width="100%" height="100%"></iframe>
</div>
<div id="showSqlStrDialog" title="SQL Query" style="display:none">
    <iframe  id="sqlQueryStr" NAME='sqlQueryStr' width="100%" height="100%" frameborder="0"   SRC='#'></iframe>
</div>
 <div style="display:none" id="custmemMeasureDispDia" title="Custom Measures">
            <iframe  id="custmemMeasDisp"  height="100%" width="100%" frameborder="0" src='about:blank'></iframe>
        </div>

<%--added by bhharathi reddy for adding param potions ends--%>

<%--added for Favourite Parameters--%>
<div id="favouriteParamsDialog" title="Save As Favourite" style="display:none">
    <iframe  id="favouriteParams" NAME='favouriteParams' width="100%" height="100%" frameborder="0" SRC='pbFavParameters.jsp'></iframe>
</div>
<div id="sqlStrDialog" title="SQL Query" style="display:none">
    <iframe  id="showSqlStr" NAME='showSqlStr' width="100%" height="100%" frameborder="0" SRC='#'></iframe>
</div>
</form>

<%--added by bhharathi reddy for adding param potions--%>
<ul id="parampotionsListMenu" class="contextMenu" style="width:205px;height:100px;text-align:left;">
    <li class="addDefaultValue"><a href="#addDefaultValue">Set Default Value</a></li>
    <li class="addParamSecurity"><a href="#addParamSecurity">Set Parameter Security</a></li>
    <li id="ExclREP" class="addcomboBox"><a href="#excludeREP">Exclude from Row Edge</a></li>
    <li id="ExclCEP" class="addcomboBox"><a href="#excludeCEP">Exclude from Column Edge</a></li>
</ul>
<ul id="favouriteParamsMenu" class="contextMenu" style="width:205px;height:100px;text-align:left;">
    <li class="viewFavParams"><a href="#viewFavParams">View Paramemters </a></li>
    <li class="editFavParams"><a href="#editFavParams">Edit Paramemters</a></li>
    <li class="deleteFavParams"><a href="#deleteFavParams">Delete Favourite</a></li>
</ul>

<div id="viewFavouriteDiv" title="Favourite Parameters" style="display: none;">
    <ul id="viewFavouriteUL" class="droppable">

    </ul>
    <br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input align='center' type='button' onclick='closeDiv()' value='Done' class='navtitle-hover'>

</div>


<div id="favouriteParamsDiv" title="Favourite Parameters" style="display:none;" >

    <table style="width:100%;height:250px" border="solid black 1px">
        <tr>
            <td width="50%" valign="top" >
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Select Dimensions from below</font></div>
                <div style="height:250px;overflow:scroll" id="favouriteParamsInnerDiv" >
                    <ul id="DimensionsUL" class="sortable"  >

                    </ul>
                </div>
            </td>
            <td id="selectedMeasures" width="50%" valign="top">
                <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Drag Dimensions to here</font></div>
                <div id="favouriteDimsDiv" style="height:250px;overflow:auto">
                    <ul id="favouriteDims" class="droppable">

                    </ul>
                </div>
            </td>
        </tr>
    </table>
    <table style="width:100%" align="center">
        <tr>
            <td colspan="2" style="height:10px"></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="button" class="navtitle-hover" style="width:auto" value="Done" onclick="updateFavouriteParams()">
            </td>
        </tr>
    </table>
</div>
  <div id="dispGrpProp" title="Graph Properties" style="display:none">
      <iframe id="dispGrpPropFrame"  name="dispGrpPropFrame" width="100%" height="100%"  frameborder="0" src='about:blank'></iframe>
        </div>
<iframe name="widgetframe" id="widgetframe" style="display:none" src=""></iframe>
 <IFRAME NAME='iframe4' ID='iframe4'  SRC='' STYLE='width:100%;overflow:auto' frameborder="0"></IFRAME>

<%-- ends--%>

<ul id="DateMenu" class="contextMenu">

    <li  class="addDefaultTime"><a href="#addDefaultTime">Choose Today's Date</a></li>
    <li  class="addCustomTime"><a href="#addCustomTime">Choose Another Date</a></li>
</ul>
</body>
</html>
<%             }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            } else {
                response.sendRedirect(request.getContextPath() + "/pbSessionExpired.jsp");
            }
%>

