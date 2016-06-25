var paramArray=new Array();
var paramIdArray=new Array();
var timeDimArray=new Array();
var imageId="";
var imageFlag=0;
var imageFlag1=0;
var ajaxArr=new Array();
var suggestArr=new Array();
var scrollPayLoad="";
var scrollURL="";
var onScrollFlag=0;
var scrollFlag=true
var keyPress=true;
var tbz=0;
var prevCount=7;
var fieldValArray=new Array();
var paramValSel=false;
var ctxPath;
var query;
var testArray=new Array();
var tid;
var did;
var tabCode=0;
var checkId="";
var startVal=1;
var allParamIds="";

function getUserDims(){
    var foldersIds=buildFldIds();  
    var branches="";
    var str;  
    var path=document.getElementById('path').value;
    var scenarioName=document.getElementById('scenarioName').value;
    $.ajax({
        url: path+'/ScenarioTemplateAction.do?scnTemplateParam=getScenarioDims&foldersIds='+foldersIds+"&scenarioName="+scenarioName,
        success: function(data) {            
            $("#userDims").html("");
            str=data;
            branches = $(str).appendTo("#userDims");
            $("#userDims").treeview({
                add: branches
            });

            var dragUserDim=$('#userDims > li >span')
            var draggableDims=$('#draggableDims');

            $(dragUserDim).draggable({
                helper:"clone",
                effect:["", "fade"]
            });

            $(draggableDims).droppable(
            {
                activeClass:"blueBorder",
                accept:'#userDims > li >span,#userBuckets > li >span',
                drop: function(ev, ui) {
                    var dim=ui.draggable.html();
                    if(dim=='Time-Month Range Basis'){
                        var timeDim='AS_OF_MONTH,AS_OF_MONTH1,PRG_PERIOD_TYPE';
                        var timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    } else if(dim=='Time-Quarter Range Basis'){
                        timeDim='AS_OF_QUARTER,AS_OF_QUARTER1,PRG_PERIOD_TYPE';
                        timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    } else if(dim=='Time-Year Range Basis'){
                        timeDim='AS_OF_YEAR,AS_OF_YEAR1';
                        timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    } else {
                        getDimDetails(ui.draggable.html());                    
                    }
                }
            });
        }
    });
}

function buildFldIds(){
    var fldObj=document.getElementsByName("userfldsList");
    var foldersIds="";

    if(fldObj.length!=0){
        for(var i=0;i<fldObj.length;i++){
            if(fldObj[i].checked){
                foldersIds=foldersIds+","+fldObj[i].id;
            }
        }
    }
    if (foldersIds!=""){
        foldersIds=foldersIds.substr(1,foldersIds.length);
    }
    return foldersIds;
}

function getDimDetails(dimId){
    var dimension=document.getElementById("dimName-"+dimId);
    var params=dimension.getElementsByTagName("li");
    dimension=createParams(params,dimId);

}

function createParams(params,dimId){   
    var parentUL=document.getElementById("sortable");
    var i=0;
    // add for time dimension
    if(dimId=='Time-Month Range Basis'){
        if(timeDimArray.length!=0){
            for(var timedim=0;timedim<timeDimArray.length;timedim++){
                if(timeDimArray[timedim]=='AS_OF_MONTH1')
                    deleteTimeDimParam(timeDimArray[timedim]);
                if(timeDimArray[timedim]=='AS_OF_MONTH2')
                    deleteTimeDimParam(timeDimArray[timedim]);
            }
        }
        for(var j=0;j<params.length;j++){
            var x=timeDimArray.toString();
            if(x.match(params[j])==null){
                timeDimArray.push(params[j]);
                childLI=document.createElement("li");
                childLI.id=params[j];
                childLI.style.width='180px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                table=document.createElement("table");
                table.id=dimId+j;
                row=table.insertRow(0);
                cell1=row.insertCell(0);
                
                cell2=row.insertCell(1);               
                if(j==0)
                    cell2.innerHTML='From Month';
                else if(j==1)
                    cell2.innerHTML='To Month';
                else if(j==2)
                    cell2.innerHTML='Period';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else if(dimId=='Time-Year Range Basis'){
        if(timeDimArray.length!=0){
            for(var timedim=0;timedim<timeDimArray.length;timedim++){
                if(timeDimArray[timedim]=='AS_OF_MONTH1')
                    deleteTimeDimParam(timeDimArray[timedim]);
                if(timeDimArray[timedim]=='AS_OF_MONTH2')
                    deleteTimeDimParam(timeDimArray[timedim]);
            }
        }
        for(var j=0;j<params.length;j++){
            var x=timeDimArray.toString();
            if(x.match(params[j])==null){
                timeDimArray.push(params[j]);
                childLI=document.createElement("li");
                childLI.id=params[j];
                childLI.style.width='180px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                table=document.createElement("table");
                table.id=dimId+j;
                row=table.insertRow(0);
                cell1=row.insertCell(0);

                cell2=row.insertCell(1);
                if(j==0)
                    cell2.innerHTML='From Year';
                else if(j==1)
                    cell2.innerHTML='To Year';

                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else {
        for(i=0;i<params.length;i++){
            var span=params[i].getElementsByTagName("span");           
            y=paramArray.toString();//change by bharu
            x=paramIdArray.toString();//change by bharu
           
            var xarr=x.split(',');
            var count=0;
            if(x!=''){
                xarr=x.split(',');                
                for(var i1=0;i1<xarr.length;i1++){
                    if(xarr[i1]==(span[0].id).split("-")[1]){                        
                        count=1;
                        break;
                    }
                }
            }

            if(count==0){
                var spanId=(span[0].id).split("-");                
                paramArray.push(span[0].innerHTML);
                paramIdArray.push(spanId[1]);
                childLI=document.createElement("li");
                childLI.id="param-"+spanId[1];
                childLI.style.width='180px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                table=document.createElement("table");
                table.id=dimId+i;
                row=table.insertRow(0);
                cell1=row.insertCell(0);               
                a=document.createElement("a");
                a.href="javascript:deleteParam('"+spanId[1]+"')";
                a.innerHTML="a";
                a.className="ui-icon ui-icon-close";
                cell1.appendChild(a);
                cell2=row.insertCell(1);               
                cell2.innerHTML=span[0].innerHTML;
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }
    buildParams(dimId);
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}

function buildParams(dimId){
    var path=document.getElementById('path').value;    
    var scenarioName=document.getElementById('scenarioName').value;
    var scenarioDesc=document.getElementById('scenarioDesc').value;
    var params=getBuildedParams();
    //alert('params are : '+params)
    var paramNames=getBuildedParamNames();
    var timeparams=getBuildedTimeParams();    
    $.ajax({
        url:  path+'/ScenarioTemplateAction.do?scnTemplateParam=buildScenarioParams&dimId='+dimId+'&params='+params+'&foldersIds='+buildFldIds()+'&paramNames='+paramNames+'&timeparams='+timeparams+'&scenarioName='+scenarioName+'&scenarioDesc='+scenarioDesc,
        success: function(data) {
        }
    });
}

function getBuildedParams()
{
    var params="";
    var paramswithtime="";
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    for(var i=0;i<paramIds.length;i++){
        if(paramIds[i].id=='AS_OF_MONTH' || paramIds[i].id=='AS_OF_MONTH1' || paramIds[i].id=='PRG_PERIOD_TYPE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_YEAR' || paramIds[i].id=='AS_OF_YEAR1'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else {
            // alert('in getBuildedParams pids are ; '+paramIds[i].id)
            var pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
    }
    if(params!="" && pIds.length!=0){
        params=params.substr(1);
    }
    if(timeDimArray.length!=0)
        paramswithtime=paramswithtime.substr(1)+","+params;

    return params;
//return paramIdArray;
}

function getBuildedParamNames()
{
    var paramNames="";
    var paramswithtime="";
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");

    for(var i=0;i<paramIds.length;i++){
        var tabObj=paramIds[i].getElementsByTagName("table");
        var trObj=tabObj[0].getElementsByTagName("tr");
        var tdObj=trObj[0].getElementsByTagName("td");

        if(paramIds[i].id=='AS_OF_MONTH' || paramIds[i].id=='AS_OF_MONTH1' || paramIds[i].id=='PRG_PERIOD_TYPE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_YEAR' || paramIds[i].id=='AS_OF_YEAR1'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else{
            var pIds=(paramIds[i].id).split("-");
            paramNames=paramNames+","+tdObj[1].innerHTML;
        }
    }

    if(paramNames!="" && pIds.length!=0){
        paramNames=paramNames.substr(1);
    }
    
    return paramNames;
//return paramIdArray;
}

function getBuildedTimeParams()
{
    var params="";
    var paramswithtime="";
    var paramtimeIds="";
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    for(var i=0;i<paramIds.length;i++){
        if(paramIds[i].id=='AS_OF_MONTH' || paramIds[i].id=='AS_OF_MONTH1' || paramIds[i].id=='PRG_PERIOD_TYPE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else  if(paramIds[i].id=='AS_OF_YEAR' || paramIds[i].id=='AS_OF_YEAR1'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else{
            var pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
    }
    
    if(timeDimArray.length!=0)
        paramswithtime=paramswithtime.substr(1);
    
    return paramswithtime;
}

function deleteTimeDimParam(index){
    var LiObj=document.getElementById(index);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;

    var parentUL=document.getElementById("sortable");
    parentUL.removeChild(LiObj);
    var x=LiObj.id;
    var i=0;
    for(i=0;i<timeDimArray.length;i++){
        if(timeDimArray[i]==x)
            timeDimArray.splice(i,1);
    }
//buildParams();
}

function deleteParam(index){
    var prevRowParams= document.getElementById("REPIds").value
    var res="false";
    if(prevRowParams!=null){
        for(var k=0;k<prevRowParams.split(",").length;k++){
            if(prevRowParams.split(",")[k]==index){
                res="false";
                break;
            }
            else{
                res="true";
            }
        }
    }
    if(res=='true'){
        var LiObj=document.getElementById("param-"+index);
        var tab=LiObj.getElementsByTagName("table");
        var row=tab[0].rows;
        var cells=row[0].cells;

        var parentUL=document.getElementById("sortable");
        parentUL.removeChild(LiObj);
        var x=cells[1].innerHTML;
        var i=0;
        for(i=0;i<paramArray.length;i++){
            if(paramArray[i]==x)
                paramArray.splice(i,1);
        //paramIdArray.splice(i,1);
        }
        for(i=0;i<paramIdArray.length;i++){
            if(paramIdArray[i]==index)
                //paramArray.splice(i,1);
                paramIdArray.splice(i,1);
        }
        buildParams();
    }
    else{
        alert("You can delete parameter which is REP /CEP")
    }
}

function showRowParams(){
    var params="";
    var REPNames="";
    var pIds="";
    var k=0;
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    
    if(paramIds.length!=0){
        for(var i=0;i<paramIds.length;i++){
            pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
        if(params!=""){
            params=params.substr(1);
        }
        var tabDiv=document.getElementById("repDiv");
        var prevRowParams= document.getElementById("REPIds").value


        if(tabDiv.style.display=='none'){
            tabDiv.style.display='block';
            var divTable=document.createElement("table");
            for(var j=0;j<paramIds.length;j++){
                if(paramIds[j].id=='AS_OF_MONTH' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='AS_OF_MONTH1'){
                }else if(paramIds[j].id=='AS_OF_YEAR' || paramIds[j].id=='AS_OF_YEAR1'){
                }else{
                    var row=divTable.insertRow(k);
                    var cell1=row.insertCell(0);
                    var divinput=document.createElement("input");
                    divinput.type='checkbox';
                    divinput.name='rowParamId';
                    divinput.id='rowParamId'+j;
                    
                    //code to set Row edge params on clicking checkbox

                    pIds=(paramIds[j].id).split("-");
                    if(prevRowParams.search(pIds[1])!=-1){
                        divinput.checked="true";
                    }

                    divinput.value=pIds[1];
                    cell1.appendChild(divinput);
                    var cell2=row.insertCell(0);
                    cell2.id='rowParam'+j;
                    var paramName=paramIds[j].getElementsByTagName("td");
                    cell2.innerHTML=paramName[1].innerHTML;
                    k++;
                }
            }            
            tabDiv.innerHTML='';
            tabDiv.appendChild(divTable);
        }
        else{
            tabDiv.style.display='none';
            getRowEdgeParams();
        }
    }
    else{
        alert("Please select Parameters")
    }
}

function showMeasures(){
    // alert('showMeasures')
    var prevREPIds=document.getElementById("REPIds").value    
    var frameObj=document.getElementById("dataDispmem");
    var divObj=document.getElementById("measures");
    var path=document.getElementById('path').value;    
    var scenarioName = document.getElementById('scenarioName').value;
    var scenarioDesc = document.getElementById('scenarioDesc').value;
    var source=path+'/ScenarioTemplateAction.do?scnTemplateParam=getScenarioMeasure&foldersIds='+parent.buildFldIds()+'&scenarioName='+scenarioName+"&scenarioDesc="+scenarioDesc;
    if(prevREPIds!="")
    {
        frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
        frameObj.style.display='block';
        divObj.style.display='block';
        if(prevREPIds!="")
        {
            document.getElementById('fade').style.display='block';
        }
        else
        {
            if(prevREPIds=="" || prevREPIds==undefined )
            {
                alert("Please select Row Edge ")
            }
        }
    }
}
/*
function selectSeededModel()
{
    var selectedSeededModels = document.getElementById("selectedSeededModels").value;
    var frameObj=document.getElementById("seededmodelmem");
    var divObj=document.getElementById("seededmodeldiv");
    var path=document.getElementById('path').value;
    var scenarioName = document.getElementById('scenarioName').value;    
    var source=path+'/ScenarioTemplateAction.do?scnTemplateParam=getScenarioSeededModels&scenarioName='+scenarioName+"&selectedSeededModels="+selectedSeededModels;
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
    frameObj.style.display='block';
    divObj.style.display='block';
    document.getElementById('fade').style.display='block';
}
*/
/*
function showTimeRange()
{
    var historicalStartMonth = document.getElementById("historicalStartMonth").value;
    var historicalEndMonth = document.getElementById("historicalEndMonth").value;
    var scenarioStartMonth = document.getElementById("scenarioStartMonth").value;
    var scenarioEndMonth = document.getElementById("scenarioEndMonth").value;
    var frameObj=document.getElementById("TimeDispmem");
    var divObj=document.getElementById("timerangediv");
    var path=document.getElementById('path').value;
    var scenarioName = document.getElementById('scenarioName').value;
    var scenarioDesc = document.getElementById('scenarioDesc').value;
    var source=path+'/ScenarioTemplateAction.do?scnTemplateParam=getScenarioTimeRange&scenarioName='+scenarioName+"&scenarioDesc="+scenarioDesc+"&historicalStartMonth="+historicalStartMonth+"&historicalEndMonth="+historicalEndMonth+"&scenarioStartMonth="+scenarioStartMonth+"&scenarioEndMonth="+scenarioEndMonth;
    
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
    frameObj.style.display='block';
    divObj.style.display='block';
    document.getElementById('fade').style.display='block';

}
**/
function cancelMeasureParent()
{
    document.getElementById("measures").style.display='none';
    document.getElementById('fade').style.display='none';
}
/*
function cancelTimeRange()
{
    document.getElementById("timerangediv").style.display='none';
    document.getElementById('fade').style.display='none';
}
*/


function getRowEdgeParams(){
    var path=document.getElementById('path').value;
    var scenarioName = document.getElementById('scenarioName').value;
    var rowEdgeParams="";
    var REPNames="";
    var rowParamIdObj=document.getElementsByName("rowParamId");    
    for(var i=0;i<rowParamIdObj.length;i++){
        if(rowParamIdObj[i].checked){
            rowEdgeParams=rowEdgeParams+","+rowParamIdObj[i].value;
            var partdObj=rowParamIdObj[i].parentNode;
            var parTrObj=partdObj.parentNode;
            var childObj=parTrObj.childNodes

            REPNames=REPNames+","+childObj[0].innerHTML;
        }
    }
    if(rowEdgeParams!=""){
        rowEdgeParams=rowEdgeParams.substring(1);
        REPNames=REPNames.substring(1);
    }
    document.getElementById("REPIds").value=rowEdgeParams;    
    $.ajax({
        url: path+'/ScenarioTemplateAction.do?scnTemplateParam=buildScenarioTable&buildTableChange=REP&rowEdgeParams='+rowEdgeParams+'&rowEdgeParamsNames='+REPNames+'&scenarioName='+scenarioName,
        success: function(data){
        }
    }); 
}

function cancelTabMeasure(){
    var frameObj=document.getElementById("dataDispmem");
    var divObj=document.getElementById("measures");
    frameObj.style.display='none';
    divObj.style.display='none';
    document.getElementById('fade').style.display='none';
}

function deleteMeasure(msrName){

    var LiObj=document.getElementById(msrName);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;
    var parentUL=document.getElementById("sortable");
    parentUL.removeChild(LiObj);
    var x=msrName.replace("Msr","");
    //var x=msrName;
    var i=0;
    for(i=0;i<msrArray.length;i++){
        if(msrArray[i]==x)
            msrArray.splice(i,1);
    }
}

function createMeasures(measureName,elmntId){
    var i=0;
    var parentUL=document.getElementById("sortable");
    var liObjs = parentUL.getElementsByTagName("li");    
    if(liObjs.length <= 0) {
        var x=msrArray.toString();
        if(x.match(elmntId)==null){
            msrArray.push(elmntId)
            var childLI=document.createElement("li");
            childLI.id="Msr"+elmntId;
            childLI.style.width='auto';
            childLI.style.height='auto';
            childLI.style.color='white';
            childLI.className='navtitle-hover';
            var table=document.createElement("table");
            table.id="Tab"+elmntId;
            var row=table.insertRow(0);
            var cell1=row.insertCell(0);
            ////cell1.style.backgroundColor="#79C9EC";
            var a=document.createElement("a");
            a.href="javascript:deleteMeasure('Msr"+elmntId+"')";
            a.innerHTML="a";
            a.className="ui-icon ui-icon-close";
            cell1.appendChild(a);
            var cell2=row.insertCell(1);
            // cell2.style.backgroundColor="#79C9EC";
            cell2.style.color='black';
            cell2.innerHTML=measureName;
            childLI.appendChild(table);
            parentUL.appendChild(childLI);
            i++;
        }
    }else {
        alert("You can not drag more than one measure");
    }
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}

function createViewerMeasures(measureName,elmntId){
    // alert('in createViewerMeasures')
    var i=0;
    var parentUL=document.getElementById("sortable");
    var liObjs = parentUL.getElementsByTagName("li");
    //    if(liObjs.length <= 0) {
    var x=msrArray.toString();
    if(x.match(elmntId)==null){
        msrArray.push(elmntId)
        var childLI=document.createElement("li");
        childLI.id="Msr"+elmntId;
        childLI.style.width='auto';
        childLI.style.height='auto';
        childLI.style.color='white';
        childLI.className='navtitle-hover';
        var table=document.createElement("table");
        table.id="Tab"+elmntId;
        var row=table.insertRow(0);
        var cell1=row.insertCell(0);
        ////cell1.style.backgroundColor="#79C9EC";
        var a=document.createElement("a");
        a.href="javascript:deleteMeasure('Msr"+elmntId+"')";
        a.innerHTML="a";
        a.className="ui-icon ui-icon-close";
        cell1.appendChild(a);
        var cell2=row.insertCell(1);
        // cell2.style.backgroundColor="#79C9EC";
        cell2.style.color='black';
        cell2.innerHTML=measureName;
        childLI.appendChild(table);
        parentUL.appendChild(childLI);
        i++;
    }
    //    }else {
    //        alert("You can not drag more than one measure");
    //    }
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}

function GetXmlHttpObject()
{
    var xmlHttp=null;
    try
    {
        // Firefox, Opera 8.0+, Safari
        xmlHttp=new XMLHttpRequest();
    }
    catch (e)
    {
        // Internet Explorer
        try
        {
            xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    return xmlHttp;
}

function getScenarioMonths()
{
    var histEndMonth = document.getElementById("histEndMonth").value;
    //alert("histEndMonth--"+histEndMonth)
    var path=document.getElementById("path").value;    
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }    
    var url=path+"/GetScenarioMonths";
    url=url+"?q="+histEndMonth; 
    xmlHttp.onreadystatechange=stateChanged10;
    xmlHttp.open("GET",url,true);
    xmlHttp.send(null);
}
function getScenarioYears()
{
    var histEndYear = document.getElementById("histEndYear").value;
    // alert("histEndMonth--"+histEndYear)
    var path=document.getElementById("path").value;
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }
    var url=path+"/GetScenarioYears";
    url=url+"?q="+histEndYear;
    xmlHttp.onreadystatechange=stateChanged20;
    xmlHttp.open("GET",url,true);
    xmlHttp.send(null);
}

function stateChanged10()
{
    if (xmlHttp.readyState==4)
    {        
        var output=xmlHttp.responseText        
        var scnMonthList=output.split("\n");
        var scnStobj = document.getElementById("scenarioStartMonth");
        var scnEndobj = document.getElementById("scenarioEndMonth");
        
        for(var i=scnStobj.length-1;i>=0;i--)
        {
            scnStobj.options[i] = null;
        }
        scnStobj.options[0] = new Option("--Select--","--Select--");

        for(var j=scnEndobj.length-1;j>=0;j--)
        {
            scnEndobj.options[j] = null;
        }
        scnEndobj.options[0] = new Option("--Select--","--Select--");

        for(var k=0;k<scnMonthList.length-1;k++)
        {
            var monthName = scnMonthList[k];
            addOption(document.getElementById('scenarioStartMonth'),monthName,monthName);
            addOption(document.getElementById('scenarioEndMonth'),monthName,monthName);
        }
    }
}
function stateChanged20()
{
    if (xmlHttp.readyState==4)
    {
        var output=xmlHttp.responseText
        var scnMonthList=output.split("\n");
        var scnStobj = document.getElementById("scenarioStartYear");
        var scnEndobj = document.getElementById("scenarioEndYear");

        for(var i=scnStobj.length-1;i>=0;i--)
        {
            scnStobj.options[i] = null;
        }
        scnStobj.options[0] = new Option("--Select--","--Select--");

        for(var j=scnEndobj.length-1;j>=0;j--)
        {
            scnEndobj.options[j] = null;
        }
        scnEndobj.options[0] = new Option("--Select--","--Select--");

        for(var k=0;k<scnMonthList.length-1;k++)
        {
            var monthName = scnMonthList[k];
            addOption(document.getElementById('scenarioStartYear'),monthName,monthName);
            addOption(document.getElementById('scenarioEndYear'),monthName,monthName);
        }
    }
}

function addOption(selectbox,text,value)
{
    //var scenarioStartMonth = parent.document.getElementById("scenarioStartMonth").value;
    //var scenarioEndMonth = parent.document.getElementById("scenarioEndMonth").value;   
    var optn = document.createElement("OPTION");
    optn.text = text;
    optn.value = value;
    /*
    if(value==scenarioStartMonth)
    {
        optn.selected = true;
    }
    */
    selectbox.options.add(optn);
}

function showParams(){
    var params="";
    var paramswithtime="";
    var pIds="";

    var hideTR=document.getElementById("dragDims");
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");

    for(var i=0;i<paramIds.length;i++){

        if(paramIds[i].id=='AS_OF_MONTH' || paramIds[i].id=='AS_OF_MONTH1' || paramIds[i].id=='PRG_PERIOD_TYPE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_YEAR' || paramIds[i].id=='AS_OF_YEAR1'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else{
            pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
        if(params!="" && pIds.length!=0){
            params=params.substr(1);
        }
        if(timeDimArray.length!=0){
            paramswithtime=paramswithtime.substr(1)+","+params;
        }
    }

    if(params!=""){
        params=params.substr(1);
        hideTR.style.display='none';
        dispParameters();
    }
    else{
        alert("Please select Parameters")
    }
}

function dispParameters()
{
    var params=getBuildedParams();
    var paramNames=getBuildedParamNames();
    var timeparams=getBuildedTimeParams();
    var path=document.getElementById('path').value;
    var showTR=document.getElementById("lovParams");
    showTR.style.display = '';
    var paramDisp=document.getElementById("paramDisp");    

    paramDisp.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: path+'/ScenarioTemplateAction.do?scnTemplateParam=dispParams&params='+params+'&paramNames='+paramNames+'&timeparams='+timeparams+'&scenarioName='+document.getElementById("scenarioName").value,
        success: function(data) {
            if(data!=""){
                paramDisp.innerHTML=data;
            }
        }
    });
}

function submitform(){
    document.forms.frmParameter.action = "ScenarioViewerAction.do?scenarioParam=viewScenario&flag=viewScenario&scenarioId="+document.forms.frmParameter.scenarioId.value+"&scenarioName="+document.forms.frmParameter.scenarioName.value;
    document.forms.frmParameter.submit();
}
function submitform2(){
    //
    var anlyName3 = parent.document.getElementById("primaryViewBy");
    var anlyName3Text = anlyName3.options[anlyName3.selectedIndex].text;
    var  secondaryViewBy=document.getElementById("secondaryViewBy");
    var secondaryViewByText = secondaryViewBy.options[secondaryViewBy.selectedIndex].text;
//    alert('in submitform2'+anlyName3Text)
    document.forms.frmParameter.action = "ScenarioViewerAction.do?scenarioParam=analyzeScenario&flag=analyzeScenario&scenarioId="+document.forms.frmParameter.scenarioId.value+"&scenarioName="+document.forms.frmParameter.scenarioName.value+"&anlyName3Text="+anlyName3Text+"&secondaryViewByText="+secondaryViewByText;
    document.forms.frmParameter.submit();
}
function submitform3(){
    var scnIds = document.getElementById("scnIds").value;    
    var scnNames = document.getElementById("scnNames").value;
    var modelNames = document.getElementById("modelNames").value;
    var dimNames = document.getElementById("dimNames").value;
    var timeLevel = document.getElementById("timeLevel").value;
    var scnMonths = document.getElementById("scnMonths").value;
    var path = document.getElementById("path").value;
    document.forms.frmParameter.action = path+"/ScenarioViewerAction.do?scenarioParam=compareScenario&flag=compareScenario&scnIds="+scnIds+"&scnNames="+scnNames+"&modelNames="+modelNames+"&dimNames="+dimNames+"&timeLevel="+timeLevel+"&scnMonths="+scnMonths;
    document.forms.frmParameter.submit();
}

function hideAll(){
    //alert("in hideAll()")
    var ad;
    while(ajaxArr.length!=0){
        ad=ajaxArr.pop();
        ad.style.display = "none";
    }
    suggestArr.length=0;
}
function selId(id1,id2,id3)
{
    tid=id1;
    did=id2;
    query=id3;
    getAllImageNames();
    if(imageId=="")
        imageId=id1+"-plus";

    if(tid=='cboShade')
    {
        preservedvalue="";
    }
   
    document.getElementById(did).style.display = "none";
    document.getElementById(tid).onkeyup = function(e)
    {       
        if(document.getElementById(tid).value.indexOf('ALL')<0)
        {
            checkKey(e, this);
        }
    };
    document.onclick = checkClick;
    hideAll();
}
function changeImage(img,country1){
    //alert("in changeImage(img,country1)")
    tabCode=0;
    /* var divLoads=document.getElementsByClassName("imageLoading");
    for(i=0;i<divLoads.length;i++){
      divLoads[i].style.visibility="hidden";
        divLoads[i].style.display="none";
    }*/
    document.getElementById(country1).focus();
    if(ajaxArr.length!=0)
        hideAll();
    if(imageFlag1==0){
        scrollPayLoad="";
        scrollURL=""
        onScrollFlag=0;
        scrollFlag=true;
        keyPress=true;
        tbz++;
        prevCount=7;
        var con=document.getElementById(country1).value;
        var con2 = con.toString().replace(/ /g, '%20');
        con2 = con2.toString().replace("&", "%26");
        con = con2;       
        imageId=img.id;
        var img2=imageId.split("-");
        var imgId=img2[1];
        var imgSrc=img.src;
        var z;

        if((imgId=="minus")&& (con!="ALL")){
            z=imgSrc.replace("minus","plus");
            imgId="plus";
            imageFlag=2;
            imageId=img2[0]+"-"+imgId;
            img.id=imageId;
            img.src=z;
        }
        else{
            z=imgSrc.replace("plus","minus");
            imgId="minus"
            imageFlag=1;
            imageId=img2[0]+"-"+imgId;
            img.id=imageId;
            img.src=z;
            imageFlag1=1;
            var country = document.getElementById(country1);            
            country.focus();
            //alert("allParamIds are:: 2 "+allParamIds)
            getSuggestions(country);//In Change Image
        }
    }
    else{
        imageFlag1=0;
    }
}

function checkImg(tb){
    var jk=tb.id.split("-");
    var kl=imageId.split("-");
    if(imageId!=""){        
        if(jk[0]!=kl[0]){           
            imageFlag1=0;
            fieldValArray.length=0;
        }
        else{
            //alert("das");
            imageFlag1=1;
        }
    }
    if((imageFlag==2)&&(imageFlag1==1))
        imageFlag1=0;

    if(ajaxArr.length==0)
        imageFlag1=0;
    if(paramValSel)
        imageFlag1=0;
    
    scHeight=0;
    scTop=0;
    clHgt=0;
    startVal=1;
    endVal=0;

    gSelectedIndex = -1;
}

function getSuggestions(country)
{
    //alert("in getSuggestions(country)")
    var url;
    ctxPath=document.getElementById("h").value;   
    var searchKey=country.value;    
    var payload;
    payload = "q="+searchKey+"&"+"query="+query;
    url=ctxPath+"/GetScenarioParameters";
    
    if(imageFlag==1){
        if(searchKey==""){
            searchKey='@';
        }
        else if(searchKey.match("All")!=null){
            changePlusImage();
            searchKey="@";
        }
        else{
            changePlusImage();
            country.value=searchKey;
            var prevVal=testArray[tid].split(",");
            var currentVal=searchKey.split(",");

            for(j=0;j<prevVal.length;j++){
                if((currentVal[currentVal.length-1]==prevVal[j]) || (currentVal[currentVal.length-1]==""))
                    searchKey=searchKey+",@";
            }
        }
        payload = "q="+searchKey+"&"+"query="+query;
    }
    if(tabCode!=9){
        checkId=checkId+payload;
        if(searchKey.lastIndexOf(",")!=(searchKey.length-1)) {
            //alert("allParamIds are:: 4 "+allParamIds)
            sendRequest(url, payload);
        }
        scrollFlag=true;
        startVal=1;
        scrollPayLoad=payload;
    }
}

function sendRequest(url, payload)
{
    //alert("in sendRequest")
    scrollURL=url;   
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null)
    {
        alert ("Your browser does not support AJAX!");
        return;
    }
    //alert("allParamIds are:: 5 "+allParamIds)
    var parArr=allParamIds.split(",");
    var parArrVals=new Array();
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
    }
    //changed by susheela satrt
    var passVales1 = "";
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
        if(i==0)
            passVales1 =document.getElementById(parArr[i]).value;
        else
            passVales1 +=";"+document.getElementById(parArr[i]).value;
    }
    //changed by susheela over
    //alert("payload is:: "+payload)
    //alert("passVales1 are:: "+passVales1)
    var scenarioId=document.getElementsByName("scenarioId");    
    var mainUrl=url+"?"+payload+"&allParamIds="+allParamIds+"&parArrVals="+passVales1;
    if(scenarioId.length>0)
        mainUrl=mainUrl+"&scenarioId="+scenarioId[0].value;
   
    xmlHttp.onreadystatechange=stateChanged;
    xmlHttp.open("POST",mainUrl,true);
    xmlHttp.send(null);

}
function onScrollDiv(divObj){
    scHeight=(divObj.scrollHeight);
    scTop=(divObj.scrollTop);
    //alert(div.offsetTop);
    //alert(div.offsetHeight);
    clHgt=(divObj.clientHeight);
    if(scrollFlag){
        if(scTop==(scHeight-clHgt)){
            startVal=startVal+20;
            scrollPayLoad1=scrollPayLoad+"&startValue="+startVal;
            sendRequest(scrollURL, scrollPayLoad1);
            onScrollFlag=1;
        }

    }
}
function stateChanged()
{
    //alert("in stateChanged")
    if (xmlHttp.readyState==4)
    {
        document.getElementById('light').style.display='none';
        document.getElementById('fade').style.display='none';
        //alert("xmlHttp.responseText is:: "+xmlHttp.responseText)
        if(onScrollFlag==0) {
            handleResponse1(xmlHttp.responseText);
        }
        else if(onScrollFlag==1) {
            handleResponse2(xmlHttp.responseText);
        }
    }
    if (xmlHttp.readyState==3){
        document.getElementById('light').style.display='block';
        document.getElementById('fade').style.display='block';
    }
}

function changePlusImage(){
    //alert("in changePlusImage()")
    imageId=tid+"-plus";
    var img=document.getElementById(imageId);
    if(img!=null){
        var img2=imageId.split("-");
        var src=img.src;
        var newSrc;
        newSrc=src.replace("plus","minus");
        imageId=img2[0]+"-minus";
        img.id=imageId;
        img.src=newSrc;
        imageFlag=1;
    }
}

function changeMinusImage(){    
    imageId=tid+"-minus";
    var img=document.getElementById(imageId);
    if(img!=null){
        var img2=imageId.split("-");
        var src=img.src;
        var newSrc;
        newSrc=src.replace("minus","plus");
        imageId=img2[0]+"-plus";
        img.id=imageId;
        img.src=newSrc;
        imageFlag=2;
    }
}
function checkClick(e)
{
    var target = ((e && e.target) ||(window && window.event && window.event.srcElement));
    var tag = target.tagName;
    if (tag.toLowerCase() != "input" && tag.toLowerCase() != "div")
        clearList();
}
var preservedvalue="";
var gSelectedIndex = -1;
var selectedItem="";
var ENTER = 13;
var KEYUP = 38;
var KEYDOWN = 40;
var BACKSPACE = 8;
var TAB=9;
var selValAjax=0;
function checkKey(e, obj)
{
    var country = document.getElementById(tid);
    paramValSel=false;
    if(country.value.length==0)
    {        
        preservedvalue="";
    }
    if(country.value.length!=0)//if text field is not empty
    {
        if(country.value.lastIndexOf(',')==(country.value.length -1 ))
        {
            preservedvalue=country.value;        
        }
    }
    /* get key pressed */
    var code = (e && e.which) ? e.which : window.event.keyCode;
    var evt = e || window.event;
    var ecsKey= evt.keyCode;   
    /* if up or down move thru the suggestion list */
    if(ecsKey==27){
        hideAll();
        resetAllAjax();
    }
    else if (code == KEYDOWN || code == KEYUP)
    {
        var x=document.getElementById(did);
        var index = gSelectedIndex;
        var countId=index+2;
        if(countId==1)
            prevCount=7;
        var divHeight=0;
        divHeight=6*((x.scrollHeight-x.clientHeight)/suggestArr.length);
        if (code ==  KEYDOWN){
            if(index<suggestArr.length-1){
                index++;
                if(x.scrollTop<=(x.scrollHeight-x.clientHeight)){
                    if(x.scrollTop<=(x.scrollHeight-x.clientHeight-10)){
                        if((prevCount-countId)==0){
                            x.scrollTop=x.scrollTop+divHeight+5;
                            prevCount=prevCount+6;
                        }
                    }
                    else
                        x.scrollTop=x.scrollTop+5;
                }
            }
            else{
                x.scrollTop=(x.scrollHeight-x.clientHeight);
            }
        }else{
            index--;
            countId=index+1;
            if(((prevCount-7)-countId)==0){
                var g=x.scrollTop-(divHeight+5);
                if(g>0)
                    x.scrollTop=g;
                else
                    x.scrollTop=0;

                prevCount=prevCount-6;
            }
            if(index<0){
                index=0;
                x.scrollTop=0;
                prevCount=7;
            }
        }
        /* find item in suggestion list being looked at if any */
        selectedItem = document.getElementById("resultlist" + index);
        if (selectedItem)
        {
            selectItem(selectedItem);
        /* set the field to the suggestion */
        }
    }
    else if (code == ENTER)  /* clear list if enter key */
    {
        selValAjax=1;
        paramValSel=true;
        onScrollFlag=0;
        var onEnterSelVal=selectedItem.innerHTML;

        if(onEnterSelVal==undefined)
            onEnterSelVal=country.value;
        if(onEnterSelVal.match("&amp;")!=null)
            onEnterSelVal=onEnterSelVal.replace("&amp;","&");
        preservedvalue=country.value;
        var test=preservedvalue.split(",");
        if((preservedvalue.length==0) || (preservedvalue=="All") || (onEnterSelVal=="All"))
        {
            country.value = onEnterSelVal;        
        }
        else
        {           
            var prevValues=testArray[tid].split(",");
            var newStrValue="";
            for(i=0;i<test.length;i++){
                for(j=0;j<prevValues.length;j++){
                    if(test[i]==prevValues[j])
                        newStrValue=newStrValue+test[i]+",";
                }
            }

            if(newStrValue.charAt(newStrValue.length-1)==","){               
                if(newStrValue.charAt(newStrValue.length-2)==",")
                    newStrValue=newStrValue.substr(0, (newStrValue.length-1));
            }
            if(newStrValue.match(onEnterSelVal)==null)
                country.value = newStrValue+onEnterSelVal;
        }
        
        var tempVal=testArray[tid];
        if(tempVal.match(onEnterSelVal)==null){            
            testArray[tid]=testArray[tid]+onEnterSelVal+",";
        }
        changeMinusImage();
        clearList();
        imageFlag1=0;

    }

    else if (code == BACKSPACE)
    {
        var temp=country.value;
        if(country.value.indexOf(',')!=country.value.length)
            paramValSel=true;
        temp=temp.substring(0,(temp.lastIndexOf(',')+1));       
        preservedvalue=temp;       
        gSelectedIndex = -1;
        getSuggestions(obj);//In checkKey       
        keyPress=false;
        onScrollFlag=0;

    }
    else if(code==39 || code==37){
        checking123();
    }
    else if (country == obj) /* otherwise get more suggestions */
    {       
        onScrollFlag=0;
        keyPress=false;
        tabCode=0;
        if(code==TAB){
            tabCode=TAB;
            paramValSel=true;
            hideAll();
        }
        gSelectedIndex = -1;
        getSuggestions(obj);
    }

    var gd=country.value;
    var fg=gd.split(",");
    var commaFlag=0;
    var newStr;
    for(i=0;i<fg.length;i++){
        if(fg[i]=="")
            commaFlag++;
    }
    if(commaFlag>1){
        if((fg[fg.length-2]=="") &&(fg[fg.length-1]=="") )
            newStr = gd.substring(0, gd.length-(commaFlag-1));
        else
            newStr=gd;        
        country.value=newStr;
    }
}
function checking123(){

}
function handleResponse1(response)
{
    //alert("in handleResponse1");
    var suggestList = document.getElementById(did);
    if(response=="Search Exception"){
        document.getElementById(tid).value="";
        suggestList.innerHTML="<font style='font-size:10px;color:red'>Invalid Search Key<br> Please Use <font style='font-weight:thicker;color:black'>@</font> for Blind Search</font>";
        suggestList.style.display = "";
        ajaxArr.push(suggestList);
    }
    else{
        var tabId="divTable"+did;
        suggestList.innerHTML = "";
        var names1=new Array();
        var names = response.split("\n");
        for(var j=0;j<names.length-1;j++)
        {
            if(document.getElementById(tid).value.indexOf(names[j])<0)
            {
                names1.push(names[j]);
            }
        }
        var id323="myTable"+tid;
        suggestList.innerHTML="<table id='"+id323+"' style='word-wrap:breakword;' cellspacing=1 cellpadding=1 class='myAjaxTable'></table>"
        suggestArr.length=0;
        for(var i=0; i < names1.length; i++)
        {
            var suggestItem = document.createElement("div");
            suggestItem.id = "resultlist" + i;
            suggestItem.onmouseover = function(){
                selectItem(this);
            };
            suggestItem.onmouseout = function(){
                unselectItem(this);
            };
            suggestItem.onclick = function(){
                setCountry(this.innerHTML);
            };
            suggestItem.className = "suggestLink";
            suggestItem.appendChild(document.createTextNode(names1[i]));
            suggestArr.push(suggestItem.innerHTML);
            suggestArrLength=suggestArr.length;           

            /* ****************************** */

            var tbl = document.getElementById(id323);
            var lastRow = tbl.rows.length;
            // if there's no header row in the table, then iteration = lastRow + 1
            var iteration = lastRow;
            var row = tbl.insertRow(lastRow);
            if(lastRow%2==0){             
                var cellLeft = row.insertCell(0);               
                cellLeft.appendChild(suggestItem);
            }
            else{
                var z=tbl.rows;
                var f=z.length-2;               
                var cellRight = z[f].insertCell(1);                
                cellRight.appendChild(suggestItem);
            }
        }
        
        if (names1.length >= 1){
            suggestList.style.display = "";
            suggestList.scrollTop=0;
            ajaxArr.push(suggestList);
            changePlusImage();
        }
        else{
            changeMinusImage();
            suggestList.style.display = "none";        
        }
    }
}
function handleResponse2(response){
    //alert("in handleResponse2()")
    if(response=="Search Exception")
    {
        var suggestList = document.getElementById(did);
        document.getElementById(tid).value="";
        suggestList.innerHTML="<font style='font-size:10px;color:red'>Invalid Search Key<br> Please Use @ for Blind Search</font>";
        suggestList.style.display = "";
        ajaxArr.push(suggestList);
    }
    else
    {
        var x=document.getElementById("myTable"+tid).rows;      
        var names1=new Array();      
        var names = response.split("\n");       

        for(var j=0;j<names.length-1;j++)
        {            
            if(document.getElementById(tid).value.indexOf(names[j])<0)
            {                
                names1.push(names[j]);
            }
        }

        suggestArrLength=suggestArr.length;

        for(var i=0; i < names1.length; i++)
        {
            var suggestItem = document.createElement("div");
            var sugId=suggestArrLength+i;
            suggestItem.id = "resultlist"+sugId;           
            suggestItem.onmouseover = function(){
                selectItem(this);
            };
            suggestItem.onmouseout = function(){
                unselectItem(this);
            };
            suggestItem.onclick = function(){
                setCountry(this.innerHTML);
            };
            suggestItem.className = "suggestLink";
            suggestItem.appendChild(document.createTextNode(names1[i]));
            suggestArr.push(suggestItem.innerHTML);
           
            /* ****************************** */
            // if there's no header row in the table, then iteration = lastRow + 1
            var tbl = document.getElementById("myTable"+tid);
            var lastRow = tbl.rows.length;
            var iteration = lastRow;
            var row = tbl.insertRow(lastRow);
            if(lastRow%2==0){               
                var cellLeft = row.insertCell(0);               
                cellLeft.appendChild(suggestItem);
            }
            else{
                var z=tbl.rows;
                var f=z.length-2;
                var cellRight = z[f].insertCell(1);                
                cellRight.appendChild(suggestItem);
            }
        }
        if(names1.length==0){           
            scrollFlag=false;
        }   
    }
}
function selectItem(selectedItem)
{
    //alert("in selectItem()")
    var lastItem = document.getElementById("resultlist" + gSelectedIndex);
    if (lastItem != null)
        unselectItem(lastItem);
    selectedItem.className = 'suggestLinkOver';
    gSelectedIndex = parseInt(selectedItem.id.substring(10));
}
function unselectItem(selectedItem)
{
    //alert("in unselectItem()")
    selectedItem.className = 'suggestLink';
}
function setCountry(value)
{
    //alert("in setCountry()")
    if(value.match("&amp;")!=null)
        value=value.replace("&amp;","&");
    selValAjax=1;
    var textFieldId=document.getElementById(tid);
    if((textFieldId.value=="All") || (textFieldId.value=="") || (value=="All"))
    {
        textFieldId.value=value;
    }

    else
    {       
        var test=textFieldId.value.split(",");
        var prevValues=testArray[tid].split(",");
        var newStrValue="";
        for(i=0;i<test.length;i++){
            for(j=0;j<prevValues.length;j++){
                if(test[i]==prevValues[j])
                    newStrValue=newStrValue+test[i]+",";
            }
        }
        if(newStrValue.charAt(newStrValue.length-1)==","){           
            if(newStrValue.charAt(newStrValue.length-2)==",")
                newStrValue=newStrValue.substr(0, (newStrValue.length-1));
        }
        if(newStrValue.match(value)==null)
            textFieldId.value = newStrValue+value;
    }
   
    var tempVal=testArray[tid];
    if(tempVal.match(value)==null){       
        testArray[tid]=testArray[tid]+value+",";
    }
    imageFlag=0;
    imageFlag1=0;    
    paramValSel=true;
    onScrollFlag=0;
    changeMinusImage();
    clearList();
}
function clearList()
{
    //alert("in clearList()")
    var suggestList = document.getElementById(did);
    suggestList.innerHTML = '';
    suggestList.style.display = "none";
}
function getAllImageNames()
{
    var x=document.getElementsByTagName("img");
    var y="";
    var newArr=new Array();
    for(i=0;i<x.length;i++){
        if(x[i].id.match("plus")){
            y=x[i].id.split("-");
            newArr.push(y[0]);        
        }
    }
    allParamIds=newArr.toString();
    
    testArray.length=newArr.length;
    for(j=0;j<newArr.length;j++){
        if((document.getElementById(newArr[j]).value=="") || (document.getElementById(newArr[j]).value=="All"))
            testArray[newArr[j]]="";
        else
            testArray[newArr[j]]=document.getElementById(newArr[j]).value;
    }
}

function resetAllAjax(){
    changeMinusImage();
}

function showMbrs(){
    var hideTR=document.getElementById("dragDims");
    hideTR.style.display='';
    var showTR=document.getElementById("lovParams");
    showTR.style.display = 'none';
}


//23-jan-2010
function analyzeScenario()
{       
    var trObj;   
    var tdObj;
    var scenarioId;
    var scenarioName;    
    var i=0;
    var obj = document.forms.ec.chk1;    
    if(isNaN(obj.length))
    {
        if(document.forms.ec.chk1.checked)
        {
            tdObj = document.forms.ec.chk1.parentNode;
            trObj = tdObj.parentNode;
            scenarioId = document.forms.ec.chk1.value;
            scenarioName = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
            document.forms.ec.action="ScenarioViewerAction.do?scenarioParam=analyzeScenario&flag=analyzeScenario&scenarioId="+scenarioId+"&scenarioName="+scenarioName+"&newScnrFlag=Y";
            document.forms.ec.submit();
        }
        else
        {
            alert('Please select Scenario');
        }
    }
    else
    {
        for(var j=0;j<obj.length;j++)
        {
            if(document.forms.ec.chk1[j].checked==true)
            {
                tdObj = document.forms.ec.chk1[j].parentNode;
                trObj = tdObj.parentNode;                
                scenarioId = document.forms.ec.chk1[j].value;
                scenarioName = trObj.getElementsByTagName("td")[1].getElementsByTagName("a")[0].innerHTML;
                i++;
            }
        }

        if(i>1)
        {
            alert('Please select only one Scenario');
        }
        else if(i==0)
        {
            alert('Please select Scenario');
        }
        else
        {           
            document.forms.ec.action="ScenarioViewerAction.do?scenarioParam=analyzeScenario&flag=analyzeScenario&scenarioId="+scenarioId+"&scenarioName="+scenarioName+"&newScnrFlag=Y";
            document.forms.ec.submit();
        }
    }
}

function PreviewTable(){
    var prevREPIds=document.getElementById("REPIds").value;
    var prevCEPIds=document.getElementById("CEPIds").value;
    var prevMsrIds=document.getElementById("MsrIds").value;
    var prevMsrs=document.getElementById("Measures").value;
    var editTable=document.getElementById("editTable");
    if(prevREPIds!="" && prevMsrs.length!=""){
        if(prevCEPIds!=""){
            if(prevCEPIds.split(",").length==1){
                editTable.style.display='none';
                dispTable();
            }
            else{
                alert("Please Select only one Column Edge")
            }
        }else{
            editTable.style.display='none';
            dispTable();
        }
    }
    else{
        if(prevREPIds=="" || prevREPIds.length==0){
            alert("Please select Row Edge")
        }
        else if(prevMsrs=="" || prevMsrs.length==0){
            alert("Please select Measures")
        }
    }
}
function  dispTable(){    
    var path = document.getElementById("path").value;
    var scenarioName = document.getElementById("scenarioName").value;    
    var previewTable=document.getElementById("previewTable");
    previewTable.style.display = '';
    var previewDispTableDiv = document.getElementById("previewDispTable");
    previewDispTableDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
        url: path+'/ScenarioTemplateAction.do?scnTemplateParam=dispTable&scenarioName='+scenarioName,
        success: function(data){            
            if(data!=""){
                previewDispTableDiv.innerHTML=data;
            }
            else{
                previewDispTableDiv.innerHTML="";
            }       
        }
    });
}

function EditTable(){
    var editTable=document.getElementById("editTable");
    editTable.style.display='';

    var previewTable=document.getElementById("previewTable");
    previewTable.style.display = 'none';

    var hidea=document.getElementById("prevTab");
    hidea.href='javascript:PreviewTable()';
    hidea.innerHTML='Preview';
    hidea.title='Click Preview Table';
}

function showViewerMeasures(){
    // alert('showViewerMeasures')
    //    var prevREPIds=document.getElementById("REPIds").value
    var scenarioId=document.getElementById("scenarioId").value;
    //    alert('scenarioId is : '+scenarioId)
    var viewByName=document.getElementById("viewByName").value;
    var frameObj=document.getElementById("viewerMsrs");
    var divObj=document.getElementById("viewerMeasures");
    var path=document.getElementById('path').value;
    var scenarioName = document.getElementById('scenarioName').value;
    var scenarioDesc = document.getElementById('scenarioName').value;
    var source=path+'/ScenarioTemplateAction.do?scnTemplateParam=getViewerScenarioMeasure&scenarioId='+scenarioId+'&scenarioName='+scenarioName+"&scenarioDesc="+scenarioDesc;
    //    if(prevREPIds!="")
    //    {
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
    $("#viewerMeasures").dialog('open');
//        frameObj.style.display='block';
//        divObj.style.display='block';
        
//        if(prevREPIds!="")
//        {
//            document.getElementById('fade').style.display='block';
//        }
//        else
//        {
//            if(prevREPIds=="" || prevREPIds==undefined )
//            {
//                alert("Please select Row Edge ")
//            }
//        }
//    }
}

