/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//added by susheela
function saveReportDrillDiv()
{
    // alert('ioo ')
    $('#customReportDrill').dialog('close');
}

$(document).ready(function(){
   
    $("#msgframe").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
    $("#applycolrdiv").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        width: 600,
        modal: true
    });

    $("#cstLinksFrame").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        width: 700,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
    $("#prtLinksFrame").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
    $("#Scheduler").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });



    $('#defineSchedulerDiv').click(function() {
        $('#reportSchedulerDialog').dialog('open');
    });
    $('#defineTrackerDiv').click(function() {
        $('#TrackerSchedulerDialog').dialog('open');
    });
    $('#composeMessageDiv').click(function() {

        $('#composeMessageDialog').dialog('open');
    });

    $('#Customize').click(function() {

        $('#favLinksDialog').dialog('open');
    });

    $('#Prioritize').click(function() {
        $('#prtLinksFrame').dialog('open');
    });
    $('#sanpShotDiv').click(function() {

        $('#snapShotDialog').dialog('open');
    });

    $test=$(".navtitle");

    $test.hover(
        function() {
            $(this).addClass('navtitle-hover');
        },
        function() {
            $(this).removeClass('navtitle-hover');
        }
        );

    $test1=$(".navtitle1");
    $test1.hover(
        function() {
            $(this).addClass('navtitle1-hover');
        },
        function() {
            $(this).removeClass('navtitle1-hover');
        }
        );
    $test=$(".ui-state-default ");

    $test.hover(
        function(){
            this.style.background="#454545";
            this.style.color="white";
        },
        function(){
            this.style.background="#e6e6e6";
            this.style.color="#000"
        }

        );
});
$(document).ready(function(){
    if ($.browser.msie == true){
        //added by susheela
        $("#customReportDrill").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 500,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#favLinksDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 640,
            width: 660,
            position: 'top',
            modal: true
        });
        $("#composeMessageDialog").dialog({
            autoOpen: false,
            height: 460,
            width: 740,
            position: 'justify',
            modal: true
        });
        $("#snapShotDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 600,
            position: 'justify',
            modal: true
        });
        $("#reportSchedulerDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 380,
            width: 580,
            position: 'justify',
            modal: true
        });
        $("#TrackerSchedulerDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            width: 700,
            height:610,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#busRoleDiv").dialog({
            autoOpen: false,
            height: 600,
            width: 350,
            position: 'justify',
            modal: true
        });
        $(".navigateDialog").dialog({
            autoOpen: false,
            height: 620,
            width: 820,
            position: 'justify',
            modal: true
        });
        $("#showExports").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 250,
            width: 300,
            modal: true
        });
        $("#dispTabProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 470,
            width: 400,
            modal: true,
            position:'justify'
        });
        $("#dispGrpProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 390,
            width: 350,
            modal: true,
            // title: grpName+" Graph Properties"
            title: "Graph Properties"
        });
        $("#iTextDiv").dialog({
            autoOpen: false,
            height: 300,
            width: 350,
            position: 'justify',
            modal: true
        });
        $("#showExports").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 250,
            width: 300,
            modal: true
        });
        $("#dispTabProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 470,
            width: 400,
            modal: true,
            position:'justify'
        });
        $("#saveNewReport").dialog({
            autoOpen: false,
            height: 300,
            width: 350,
            position: 'justify',
            modal: true
        });
        $("#replyMessageDialog").dialog({
            autoOpen: false,
            height: 460,
            width: 740,
            position: 'justify',
            modal: true
        });
        $("#graphColsDialog").dialog({
            autoOpen: false,
            height: 520,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#tableColsDialog").dialog({
            autoOpen: false,
            height: 520,
            width: 720,
            position: 'justify',
            modal: true
        });
        //added by uday on 23-mar-2010
        $("#selectiveMeasuresDisplay").dialog({
            autoOpen: false,
            height: 335,
            width: 500,
            position: 'justify',
            modal:true
        });
        $("#selectiveParamValuesDisplay").dialog({
            autoOpen: false,
            height: 335,
            width: 500,
            position: 'justify',
            modal:true
        });


        $("#selectiveWhatIfMeasures").dialog({
            autoOpen: false,
            height: 350,
            width: 550,
            position: 'justify',
            modal:true
        });
    }
    else{
        $("#customReportDrill").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 500,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#busRoleDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 340,
            width: 340,
            position: 'justify',
            modal: true
        });
        $("#snapShotDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 600,
            position: 'justify',
            modal: true
        });
        $("#reportSchedulerDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 280,
            width: 580,
            position: 'justify',
            modal: true
        });

        $("#favLinksDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 520,
            width: 660,
            position: 'justify',
            modal: true
        });
        $(".navigateDialog").dialog({
            autoOpen: false,
            height: 460,
            width: 820,
            position: 'justify',
            modal: true
        });
        $("#composeMessageDialog").dialog({
            autoOpen: false,
            height: 360,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#TrackerSchedulerDialog").dialog({
            //bgiframe: true,
            autoOpen: false,
            width: 700,
            height:450,
            modal: true,
            Cancel: function() {
                $(this).dialog('close');
            }
        });
        $("#dispGrpProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 290,
            width: 350,
            modal: true,
            // title: grpName+" Graph Properties"
            title: "Graph Properties"
        });
        $("#iTextDiv").dialog({
            autoOpen: false,
            height: 200,
            width: 350,
            position: 'justify',
            modal: true
        });
        $("#showExports").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 150,
            width: 250,
            modal: true
        });
        $("#dispTabProp").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 285,
            width: 350,
            modal: true
        });
        $("#saveNewReport").dialog({
            autoOpen: false,
            height: 200,
            width: 350,
            position: 'justify',
            modal: true
        });
        $("#replyMessageDialog").dialog({
            autoOpen: false,
            height: 360,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#graphColsDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#tableColsDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });

        //added by uday on 23-mar-2010
        $("#selectiveMeasuresDisplay").dialog({
            autoOpen: false,
            height: 335,
            width: 500,
            position: 'justify',
            modal:true
        });
        $("#selectiveParamValuesDisplay").dialog({
            autoOpen: false,
            height: 335,
            width: 500,
            position: 'justify',
            modal:true
        });


        $("#selectiveWhatIfMeasures").dialog({
            autoOpen: false,
            height: 350,
            width: 550,
            position: 'justify',
            modal:true
        });
    }
});

var whatIfScenarioId;
var ctxPath;

function displayfavlink(){
    $("#favlinkcont").toggle({
        persist: "cookie"
    });
}
function displayWidgets(){
    $("#Widgets").toggle({
        persist: "cookie"
    });
}
function displayTopBottomRes(){
    $("#topBot").toggle(500);

}
function dispParameters(){
    $("#tabParameters").toggle({
        persist: "cookie"
    });
}
function dispGraphs(){
    $("#tabGraphs").toggle({
        persist: "cookie"
    });
}
function dispTables(){
    $("#tabTable").toggle({
        persist: "cookie"
    });
    function dispMessages(){
        $("#messages").toggle({
            persist: "cookie"
        });
    }
    function dispSnapShots(){
        $("#snapshots").toggle({
            persist: "cookie"
        });
    }
}
function cancelGrpProperties(){
    $("#dispGrpProp").dialog('close');

}
function refreshReportGraphs(ctxPath,whatIfScenarioId){
    $("#dispGrpProp").dialog('close');
    var source = ctxPath+"/TableDisplay/pbWhatIfGraphDisplayRegion.jsp?whatIfScenarioId="+whatIfScenarioId;
    document.getElementById("iframe4").src= source;

}
function showGrpProperties(whatIfScenarioId,graphId,ctxPath,grpName){
    $('#dispGrpProp').data('title.dialog', grpName);
    $("#dispGrpProp").dialog('open');
    var frameObj=document.getElementById("dispGrpPropFrame");
    var source =ctxPath+'/TableDisplay/PbWhatIfGraphProperties.jsp?whatIfScenarioId='+whatIfScenarioId+'&graphId='+graphId;
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
}
function refreshWhatIfScenarioTables(ctxPath,whatIfScenarioId){
    $("#dispTabProp").dialog('close');
    var source = ctxPath+"/TableDisplay/pbWhatIfDisplay.jsp?whatIfScenarioId="+whatIfScenarioId;
    document.getElementById("iframe1").src= source;

}
function showTableProperties(whatIfScenarioId,ctxPath){
    $("#dispTabProp").dialog('open');
    var frameObj=document.getElementById("dispTabPropFrame");
    var source =ctxPath+'/TableDisplay/PbWhatIfScenarioTableProperties.jsp?whatIfScenarioId='+whatIfScenarioId;
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
}

//added by chiranjeevi for iText
function iText(disColumnName,colName){
    $.ajax({
        url:'reportTemplateAction.do?templateParam=getitext&elementId='+colName,
        success: function(data){
            document.getElementById("itextarea").value=data;
        }
    });
    $('#iTextDiv').data('title.dialog', disColumnName);
    $("#iTextDiv").dialog('open')
}
function refreshGraphs1(ctxPath,tabId){
    var source = ctxPath+"/TableDisplay/pbGraphDisplayRegion.jsp?tabId="+tabId;
    document.getElementById("iframe4").src= source;
}
function openNewReportDtls(){
    $("#saveNewReport").dialog('open')
}
function saveAsNewReport(repId,ctxPath){
    var reportName = document.getElementById('reportName1').value;
    var reportDesc = document.getElementById('reportDesc1').value;

    if(reportName==''){
        alert("Please enter Report Name");
    }
    else  if(reportDesc==''){
        alert("Please enter Report Description")
    }
    else{
        //alert(ctxPath+'/reportTemplateAction.do?templateParam=checkReportName&reportName='+reportName)
        $.ajax({
            url: ctxPath+'/reportTemplateAction.do?templateParam=checkReportName&reportName='+reportName,
            success: function(data){
                //alert(data)
                if(data!=""){
                    document.getElementById('duplicate1').innerHTML = data;
                    document.getElementById('save').disabled = true;
                }
                else {
                    document.getElementById('REPORTNAME').value=reportName;
                    document.getElementById('REPORTDESC').value=reportDesc;
                    document.forms.frmParameter.action = ctxPath+"/reportViewer.do?reportBy=saveAsNewReport";
                    document.forms.frmParameter.method="POST";
                    document.forms.frmParameter.submit();
                    $("#saveNewReport").dialog('close')
                }
            }
        });
    }
}
function showExports(){    
    $("#showExports").dialog('open');
}
function downloadExport(){
    var fileType=document.getElementById("fileType").value;
    var expType=document.getElementById("expType").value;
    var expRec=document.getElementById("expRec").value;
    var REPORTID=document.getElementById("whatIfScenarioId").value;
    ctxPath=document.getElementById("h").value;

    $("#showExports").dialog('close');
    var source = ctxPath+"/TableDisplay/pbDownload.jsp?dType="+fileType+"&tabId="+REPORTID+"&displayType="+expType+"&expRec="+expRec;
    var dSrc = document.getElementById("dFrame");
    dSrc.src = source;

    if(Obj.style.display=='none'){
        Obj.style.display="block";
    }else{
        Obj.style.display="none";
    }

}

//added by uday on 9-mar-2010
    
var measureIdsArray;
var measureNamesArray;
var paramIdsArray;
var paramNamesArray;

function selectTableMeasures(measureIds,measureNames) {
    measureIdsArray = new Array();
    measureNamesArray = new Array();
    document.getElementById("availableMeasuresSortable").innerHTML="";
    document.getElementById("existingMeasuresSortable").innerHTML="";
    var measureIdsStr = measureIds.split("£");
    var measureNamesStr = measureNames.split("£");

    
    for(var i=0;i<measureIdsStr.length;i++) {
        measureIdsArray.push(measureIdsStr[i]);
        measureNamesArray.push(measureNamesStr[i]);
        createMeasureElements(measureIdsStr[i],measureNamesStr[i],"availableMeasuresSortable","true","mydragRowTabs");
    }

    for(var cnt=0;cnt<measureIdsArray.length;cnt++){
        createMeasureElements(measureIdsArray[cnt], measureNamesArray[cnt], "existingMeasuresSortable","false","mydragRowTabs");
    }    
    dropSelectiveMeasures();
    $("#selectiveMeasuresDisplay").dialog('open');
}

function selectParameterValues() {
    paramIdsArray = new Array();
    paramNamesArray = new Array();

    document.getElementById("availableParamValuesSortable").innerHTML="";
    document.getElementById("existingParamValuesSortable").innerHTML="";
    
    var iFrameObj = document.getElementById("iframe1");
    var tbodyObj = iFrameObj.contentWindow.document.getElementById("getMainBody");    
    var trObjs = tbodyObj.getElementsByTagName("tr");

    //alert("trObjs len is:: "+trObjs.length);
    //alert("paramIdsArray len is:: "+paramIdsArray.length)


    for(var i=0;i<trObjs.length;i++) {
        if(trObjs[i].getAttribute("id") == null) {
            //alert(i+":"+trObjs[i].getElementsByTagName("td")[0].getElementsByTagName("a")[0].innerHTML);
            paramIdsArray.push(trObjs[i].getElementsByTagName("td")[0].getElementsByTagName("a")[0].innerHTML);
            paramNamesArray.push(paramIdsArray[i]);
            createParamValueElements(trObjs[i].getElementsByTagName("td")[0].getElementsByTagName("a")[0].innerHTML,trObjs[i].getElementsByTagName("td")[0].getElementsByTagName("a")[0].innerHTML,"availableParamValuesSortable","true","mydragRowTabs");
        }
    }

    for(var cnt=0;cnt<paramIdsArray.length;cnt++){
        createParamValueElements(paramIdsArray[cnt], paramNamesArray[cnt], "existingParamValuesSortable","false","mydragRowTabs");
    }

    dropSelectiveParamValues();
    $("#selectiveParamValuesDisplay").dialog('open');
}

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
        cell2.innerHTML=paramName;
        childLI.appendChild(table);
    }
    //alert("childLI is:: "+childLI)
    parentUL.appendChild(childLI);
}

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
        cell2.innerHTML=paramName;
        childLI.appendChild(table);
    }
    //alert("childLI is:: "+childLI)
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
                //alert("1 : "+ui.draggable.attr('id'))
                //alert("2 : "+measureIdsArray[i])
                if( measureIdsArray[i]==ui.draggable.attr('id')){
                    flag=true;
                }
            }
            if(!flag){
                measureIdsArray.push(ui.draggable.attr('id'));
                measureNamesArray.push(ui.draggable.html());
                //alert(ui.draggable.html());
                createMeasureElements(ui.draggable.attr('id'), ui.draggable.html(), "existingMeasuresSortable", "false","mydragRowTabs");
            }
        }
    });
    $("#existingMeasuresSortable").sortable();
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
                alert(ui.draggable.html());
                createParamValueElements(ui.draggable.attr('id'), ui.draggable.html(), "existingParamValuesSortable", "false","mydragRowTabs");
            }
        }
    });
    $("#existingParamValuesSortable").sortable();
}

function deleteMeasureElements(paramName,paramId,sortableDivName,deletefrom){
    var parentUL = document.getElementById(sortableDivName);
    var len = parentUL.childNodes.length;
    var i=0;
    for(i = 0; i < len; i++){
        if(parentUL.childNodes[i].id == paramId){
            parentUL.removeChild(parentUL.childNodes[i]);
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

function deleteParamValueElements(paramName,paramId,sortableDivName,deletefrom){
    var parentUL = document.getElementById(sortableDivName);
    var len = parentUL.childNodes.length;
    //alert("len is:: "+len)
    var i=0;
    for(i = 0; i < len; i++){
        if(parentUL.childNodes[i].id == paramId){
            parentUL.removeChild(parentUL.childNodes[i]);
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

function DesignPreviewTable() {
    //var divObj = document.getElementById("rangeregiondiv");
    var tableObj = document.getElementById("SliderRegion");
    var tbodyObj = tableObj.getElementsByTagName("tbody");
    var trObjs = tbodyObj[0].getElementsByTagName("tr");
    var trLength = trObjs.length;

    var tdObjs;
    var inputObj;
    var anchorObj;
    var idAttrValue;
    var innerName;
    var sliderValue;
    var temp;
    var calValue;

    var measureNames = new Array();
    var measureIds = "";
    var sliderValues = "";

    var tempStr;
    var tempValue;
    var sliderSplittedData;
    var sliderDetails = "";

    for(var i=0;i<trLength;i++) {
        if(i==0) {
            tdObjs = trObjs[i].getElementsByTagName("td");
            for(var cnt=0;cnt<tdObjs.length;cnt++) {
                if((cnt%2)!=0) {
                    measureNames.push(tdObjs[cnt].innerHTML);
                }
            }
        }

        if((i%2)!=0) {
            tdObjs = trObjs[i].getElementsByTagName("td");
            for(cnt=0;cnt<tdObjs.length;cnt++) {
                if((cnt%2)!=0) {
                    tempStr = tdObjs[cnt].getElementsByTagName("input")[0].getAttribute("id");
                    tempValue = tdObjs[cnt].getElementsByTagName("input")[0].value;
                    sliderSplittedData = tempStr.split("-");
                    if(sliderValues == "") {
                        sliderValues = sliderSplittedData[1]+"©"+sliderSplittedData[2]+"©"+tempValue;
                    }else {
                        sliderValues = sliderValues+"£"+sliderSplittedData[1]+"©"+sliderSplittedData[2]+"©"+tempValue;
                    }
                }
            }
        }
    }

    ctxPath = document.getElementById("h").value;
    whatIfScenarioId = document.forms.frmParameter.whatIfScenarioId.value;
    //alert("sliderValues are:: "+sliderValues)
    $.ajax({
        url: ctxPath+'/reportViewer.do?reportBy=updateWhatIfSliderValues&whatIfScenarioId='+whatIfScenarioId+'&sliderValues='+sliderValues,
        success: function(data){
            callAction();
        }
    });
}

function changeStepSize(currValue) {
    whatIfScenarioId = document.forms.frmParameter.whatIfScenarioId.value;
    ctxPath = document.getElementById("h").value;
    $.ajax({
        url: ctxPath+'/reportViewer.do?reportBy=updateWhatIfStepSize&whatIfScenarioId='+whatIfScenarioId+'&stepSize='+currValue,
        success: function(data){
            callAction();
        }
    });

    /*
        document.forms.frmParameter.action = "reportViewer.do?reportBy=viewWhatIfScenario&REPORTID="+whatIfScenarioId+"&stepSize="+currValue+"&changeStepSize=true";
        document.forms.frmParameter.method="POST";
        document.forms.frmParameter.submit();
    */
}

function processSelectiveMeasures() {
    $("#selectiveMeasuresDisplay").dialog('close');
}

function processSelectiveParamValues() {
    $("#selectiveParamValuesDisplay").dialog('close');
    ctxPath = document.getElementById("h").value;
    whatIfScenarioId = document.forms.frmParameter.whatIfScenarioId.value;
    //alert("measureIdsArray.toString() is:: "+measureIdsArray.toString())
    //alert("measureNamesArray.toString() is:: "+measureNamesArray.toString())
    //alert("paramNamesArray.toString() is:: "+paramNamesArray.toString())

    $.ajax({
        url: ctxPath+'/reportViewer.do?reportBy=whatIfSelectiveMeasureChanges&whatIfScenarioId='+whatIfScenarioId+'&measureIdsArray='+measureIdsArray.toString()+'&measureNamesArray='+measureNamesArray.toString()+'&paramNamesArray='+paramNamesArray.toString(),
        success: function(data){

        }
    });
}

function callAction() {
    whatIfScenarioId = document.forms.frmParameter.whatIfScenarioId.value;
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewWhatIfScenario&REPORTID="+whatIfScenarioId;
    document.forms.frmParameter.method="POST";
    document.forms.frmParameter.submit();
}

function whatIfSelectiveMeasures(measureIds,measureNames) {
    ctxPath=document.getElementById("h").value;
    whatIfScenarioId = document.forms.frmParameter.whatIfScenarioId.value;
    var frameObj = document.getElementById("selectiveWhatIfMeasuresFrame");
    //alert("measureIds is:: "+measureIds+" and measureNames is:: "+measureNames)
    var source = ctxPath+"/pbWhatIfSelectiveMeasures.jsp?measureIds="+measureIds+"&measureNames="+measureNames+"&whatIfScenarioId="+whatIfScenarioId;
    frameObj.src = source;
    $("#selectiveWhatIfMeasures").dialog('open');
}

function saveWhatIfSelectiveMeasures(nonAllName,nonAllDesc,measureIds,measureNames,paramNames) {
    ctxPath=document.getElementById("h").value;
    whatIfScenarioId = document.forms.frmParameter.whatIfScenarioId.value;
    //alert(ctxPath+'/reportViewer.do?reportBy=whatIfSelectiveMeasureChanges&whatIfScenarioId='+whatIfScenarioId+'&measureIds='+measureIds+'&measureNames='+measureNames+'&paramNames='+paramNames+'&nonAllName='+nonAllName+'&nonAllDesc='+nonAllDesc)
    $.ajax({
        url: ctxPath+'/reportViewer.do?reportBy=whatIfSelectiveMeasureChanges&whatIfScenarioId='+whatIfScenarioId+'&measureIds='+measureIds+'&measureNames='+measureNames+'&paramNames='+paramNames+'&nonAllName='+nonAllName+'&nonAllDesc='+nonAllDesc,
        success: function(data){
            callAction();
        }
    });
}

function sliderRegionSize(){
    var sliderDiv = document.getElementById("rangeregiondiv");
    if ($.browser.msie == true){
        if(screen.width >= 1440){
            sliderDiv.style.width='1200px';
            sliderDiv.style.position='relative';
        //ifrmesizes.style.overflow='auto';

        }
        else if(screen.width == 1400 ){
            sliderDiv.style.width='1000px';
            sliderDiv.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1280 ){
            sliderDiv.style.width='1000px';
            sliderDiv.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width == 1024 ){
            sliderDiv.style.width='950px';
            sliderDiv.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width < 1000 ){
            sliderDiv.style.width='700px';
            sliderDiv.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }else{
            sliderDiv.style.width=(ifrmesizes.style.width -400)+ 'px';
            sliderDiv.style.position='relative';
        }
    }else{
        if(screen.width >= 1440){
            sliderDiv.style.width='395%';
            sliderDiv.style.position='relative';
            sliderDiv.style.overflow='auto';
        }
        else if(screen.width == 1400 ){
            sliderDiv.style.width='377%';
            sliderDiv.style.position='relative';
            sliderDiv.style.overflow='auto';
        }
        else if(screen.width == 1280 ){
            sliderDiv.style.width='337%';
            sliderDiv.style.position='relative';
            sliderDiv.style.overflow='auto';
        }
        else if(screen.width == 1024 ){
            sliderDiv.style.width='250%';
            sliderDiv.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }
        else if(screen.width < 1000 ){
            sliderDiv.style.width='230%';
            sliderDiv.style.position='relative';
        //ifrmesizes.style.overflow='auto';
        }else{
            sliderDiv.style.width='200%';
            sliderDiv.style.position='relative';
        }
    }
}

