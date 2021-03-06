/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var graphCount=0;
var graphName='';
var typesDisplayStatus = '0';
var sizeDisplayStatus = '0';
var currentGraphId = '0';
var divStatus = '0';

var rowId=0;
var count=0;
var dimName="";
var dimNamesDrag="";
var dimNameArr=new  Array();
var dimIdArr=new Array();
var paramArr=new Array();
var paramNameArr=new Array();
var relationArray=new Array();
var relWithId="";
var autoSuggestArr=new Array();
var autoSuggestFlag=false;
var dimDetailsArray=new Array();
var delParamId="";
var addRelArr=new Array();
var addRelStr="";
var restRelStr="";
var restRelArr=new Array();
var tempStr="";
var tempStrRev="";
var restrictTableRow=new Array();
var relationIdStr=""
var liCount=0;
var paramArray=new Array();
var msrArray=new Array();
var arrInd=0;
var timeDimArray=new Array();
var paramIdArray=new Array();

var rowEdgeList='';
var columnEdgeList='';
var firstGraphId = '';
var secondGraphId = '';
var graphIds = '';


$(function() {

    $("#successMsg").dialog({
        bgiframe: true,
        autoOpen: false,
        modal: true,
        buttons: {
            Ok: function() {
                $(this).dialog('close');
            }
        }
    });
    $("#failureMsg").dialog({
        bgiframe: true,
        autoOpen: false,
        modal: true,
        buttons: {
            Ok: function() {
                $(this).dialog('close');
            }
        }
    });
    $("#warning").dialog({
        bgiframe: true,
        autoOpen: false,
        modal: true,
        buttons: {
            Ok: function() {
                $(this).dialog('close');
            }
        }
    });

});
function getDimDetails(dimId){
    
    var dimension=document.getElementById("dimName-"+dimId);
    var params=dimension.getElementsByTagName("li");
    dimension=createParams(params,dimId);

}
function createParams(params,dimId){
    var parentUL=document.getElementById("sortable");
    var i=0;
    // add for time dimension
    if(dimId=='Time-Period Basis'){
        if(timeDimArray.length!=0){
            for(var timedim=0;timedim<timeDimArray.length;timedim++){
                if(timeDimArray[timedim]=='AS_OF_DATE1')
                    deleteTimeDimParam(timeDimArray[timedim]);
                if(timeDimArray[timedim]=='AS_OF_DATE2')
                    deleteTimeDimParam(timeDimArray[timedim]);
                if(timeDimArray[timedim]=='CMP_AS_OF_DATE1')
                    deleteTimeDimParam(timeDimArray[timedim]);
                if(timeDimArray[timedim]=='CMP_AS_OF_DATE2')
                    deleteTimeDimParam(timeDimArray[timedim]);
            }
        }
        // end of adding
        for(var j=0;j<params.length;j++){
            var x=timeDimArray.toString();
            if(x.match(params[j])==null){
                timeDimArray.push(params[j]);
                var childLI=document.createElement("li");
                childLI.id=params[j];
                childLI.style.width='180px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                var table=document.createElement("table");
                table.id=dimId+j;
                var row=table.insertRow(0);
                var cell1=row.insertCell(0);
                //cell1.style.backgroundColor="#e6e6e6";
                if(params[j]=='PRG_COMPARE'){
                    var a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                var cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                if(j==0)
                    cell2.innerHTML='Date';
                else if(j==1)
                    cell2.innerHTML='Aggregation';
                else if(j==2)
                    cell2.innerHTML='Compare With';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else if(dimId=='Time-Range Basis'){
        if(timeDimArray.length!=0){
            for(var tdim=0;tdim<timeDimArray.length;tdim++){
                if(timeDimArray[tdim]=='AS_OF_DATE')
                    deleteTimeDimParam(timeDimArray[tdim]);
                if(timeDimArray[tdim]=='PRG_PERIOD_TYPE')
                    deleteTimeDimParam(timeDimArray[tdim])
                if(timeDimArray[tdim]=='PRG_COMPARE')
                    deleteTimeDimParam(timeDimArray[tdim])
            }
        }
        for(var k=0;k<params.length;k++){
            var y=timeDimArray.toString();
            if(y.match(params[k])==null){
                timeDimArray.push(params[k]);
                childLI=document.createElement("li");
                childLI.id=params[k];
                childLI.style.width='180px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                table=document.createElement("table");
                table.id=dimId+k;
                row=table.insertRow(0);
                cell1=row.insertCell(0);
                //cell1.style.backgroundColor="#e6e6e6";
                if(params[k]=='CMP_AS_OF_DATE1' || params[k]=='CMP_AS_OF_DATE2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                if(k==0)
                    cell2.innerHTML='From Date';
                else if(k==1)
                    cell2.innerHTML='To Date';
                else if(k==2)
                    cell2.innerHTML='Compare From Date';
                else if(k==3)
                    cell2.innerHTML='Compare To Date';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }

    }else if(dimId=='Time-Week Basis'){
        for( j=0;j<params.length;j++){
            x=timeDimArray.toString();
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
                //cell1.style.backgroundColor="#e6e6e6";
                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                if(j==0)
                    cell2.innerHTML='Week';
                else if(j==1)
                    cell2.innerHTML='Compare Week';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else if(dimId=='Time-Month Basis'){
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
                //cell1.style.backgroundColor="#e6e6e6";
                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                if(j==0)
                    cell2.innerHTML='Month';
                else if(j==1)
                    cell2.innerHTML='Aggregation';
                else if(j==2)
                    cell2.innerHTML='Compare With';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else if(dimId=='Time-Compare Month Basis'){
        if(timeDimArray.length!=0){
            for(var timedim=0;timedim<timeDimArray.length;timedim++){
                if(timeDimArray[timedim]=='AS_OF_MONTH')
                    deleteTimeDimParam(timeDimArray[timedim]);
                if(timeDimArray[timedim]=='PRG_PERIOD_TYPE')
                    deleteTimeDimParam(timeDimArray[timedim]);
                if(timeDimArray[timedim]=='PRG_COMPARE')
                    deleteTimeDimParam(timeDimArray[timedim]);
            }
        }
        for(var j=0;j<params.length;j++){
            var x=timeDimArray.toString();
            if(x.match(params[j])==null){
                timeDimArray.push(params[j]);
                var childLI=document.createElement("li");
                childLI.id=params[j];
                childLI.style.width='180px';
                childLI.style.height='auto';
                childLI.style.color='white';
                childLI.className='navtitle-hover';
                var table=document.createElement("table");
                table.id=dimId+j;
                var row=table.insertRow(0);
                var cell1=row.insertCell(0);
                //cell1.style.backgroundColor="#e6e6e6";
                if(params[j]=='PRG_COMPARE'){
                    var a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                var cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                if(j==0)
                    cell2.innerHTML='Month';
                else if(j==1)
                    cell2.innerHTML='Compare Month';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else if(dimId=='Time-Quarter Basis'){
        for( j=0;j<params.length;j++){
            x=timeDimArray.toString();
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
                //cell1.style.backgroundColor="#e6e6e6";
                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                if(j==0)
                    cell2.innerHTML='Quarter';
                else if(j==1)
                    cell2.innerHTML='Compare Quarter';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else if(dimId=='Time-Year Basis'){
        for( j=0;j<params.length;j++){
            x=timeDimArray.toString();
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
                //cell1.style.backgroundColor="#e6e6e6";
                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                if(j==0)
                    cell2.innerHTML='Year';
                else if(j==1)
                    cell2.innerHTML='Compare Year';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else {
        for(i=0;i<params.length;i++){
            var span=params[i].getElementsByTagName("span");
            x=paramArray.toString();
            y=paramIdArray.toString();
            var xarr=x.split(',');
            var count=0;
            if(x!=''){
                xarr=x.split(',');
                //
                for(var i1=0;i1<xarr.length;i1++){                   
                    if(xarr[i1]==span[0].innerHTML){
                        //
                        count=1;
                        break;
                    }
                }
            }

            if(count==0){
                //if(x.match(span[0].innerHTML)==null){
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
                //cell1.style.backgroundColor="#e6e6e6";
                a=document.createElement("a");
                a.href="javascript:deleteParam('"+spanId[1]+"')";
                a.innerHTML="a";
                a.className="ui-icon ui-icon-close";
                cell1.appendChild(a);
                cell2=row.insertCell(1);
                //cell2.style.backgroundColor="#e6e6e6";
                cell2.innerHTML=span[0].innerHTML;
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }
    buildParams();
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}
/*
 function deleteParam(index){
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
    }
    buildParams();
}
 */function deleteParam(index){
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
        }
        buildParams();
    }
    else{
        alert("You can delete parameter which is REP /CEP")
    }
}

function showParams(){
    var params="";
    var paramswithtime="";
    var pIds="";

    var hideTR=document.getElementById("dragDims");

    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");

    for(var i=0;i<paramIds.length;i++){

        if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_WEEK' || paramIds[i].id=='AS_OF_WEEK1'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_MONTH' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_MONTH1' || paramIds[i].id=='AS_OF_MONTH2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_QUARTER' || paramIds[i].id=='AS_OF_QUARTER1'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_YEAR' || paramIds[i].id=='AS_OF_YEAR1'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else {

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
function dispParameters(params){
    // var showTR=document.getElementById("lovParams");
    var paramDisp=document.getElementById("paramDisp");
    paramDisp.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportViewer.do?reportBy=dispParameters&params='+params+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            if(data!=""){
                paramDisp.innerHTML=data;
            }
            else{
                paramDisp.innerHTML="";
            }
        }
    });
}
function showMbrs(){
    var hideTR=document.getElementById("dragDims");
    hideTR.style.display='';

    var showTR=document.getElementById("lovParams");
    showTR.style.display = 'none';

}
function showRowTable(){
    var rowEdgeIds="";
    var rowEdgeParams="";
    var rowTable="";
    var colRow="";
    var rowCol="";
    var rowEdgeThead="";
    var paramUl=document.getElementById("sortable");
    var tabDiv=document.getElementById("tableDiv");
    var paramIds=paramUl.getElementsByTagName("li");
    var rowEdgeTable=document.getElementById("rowTable");
    //var rowEdgeThead=rowEdgeTable.getElementsByTagName("thead");
    rowEdgeTable.innerHTML='';
    rowEdgeThead=rowEdgeTable.createTHead();
    colRow=rowEdgeTable.insertRow(0);
    for(var i=0;i<paramIds.length;i++){
        if(document.getElementById("rowParamId"+i).checked){
            rowCol=document.createElement("th");
            //rowCol=colRow.insertCell(0);
            rowCol.className='header headerSortDown';
            rowCol.innerHTML=document.getElementById("rowParam"+i).innerHTML;
            colRow.appendChild(rowCol);
        }
    }
}

function showColTable(){
    var rowEdgeIds="";
    var rowEdgeParams="";
    var rowTable="";
    var colRow="";
    var rowCol="";
    var rowEdgeThead="";
    var paramUl=document.getElementById("sortable");
    var tabDiv=document.getElementById("tableDiv");
    var paramIds=paramUl.getElementsByTagName("li");
    var colEdgeTable=document.getElementById("rowTable")

    var colEdgeRow=colEdgeTable.rows;

    for(var i=0;i<paramIds.length;i++){
        if(document.getElementById("colParamId"+i).checked){
            rowCol=document.createElement("th");
            rowCol.className='header headerSortDown';
            rowCol.innerHTML=document.getElementById("colParam"+i).innerHTML;
            colEdgeRow[0].appendChild(rowCol);
        }
    //var colName=document.getElementById("addColParam"+i);
    }
//rowEdgeParams=rowEdgeParams.substr(1).split(",");
}

function showMeasures(){
    var prevREPIds=document.getElementById("REPIds").value
    var frameObj=document.getElementById("dataDispmem");
    var divObj=document.getElementById("measures");
    var source="reportViewer.do?reportBy=getMeasures&foldersIds="+parent.buildFldIds()+'&REPORTID='+document.getElementById("REPORTID").value;//var paramUl=document.getElementById("sortable");
    //var paramIds=paramUl.getElementsByTagName("li");
    if(prevREPIds!=""){
        frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';
        frameObj.src=source;
        frameObj.style.display='block';
        divObj.style.display='block';
        document.getElementById('fade').style.display='block';
    }
    else{
        if(prevREPIds=="" || prevREPIds==undefined ){
            alert("Please select Row Edge ")
        }


    }
}
/*

function createMeasures(measureName,elmntId){
    var i=0;
    var parentUL=document.getElementById("sortable");
    var x=msrArray.toString();
    if(x.match(elmntId)==null){
        msrArray.push(elmntId)
        var childLI=document.createElement("li");
        childLI.id=measureName+"-"+elmntId;
        childLI.style.width='auto';
        childLI.style.height='auto';
        childLI.style.color='white';
        childLI.className='navtitle-hover';
        var table=document.createElement("table");
        table.id=measureName+i;
        var row=table.insertRow(0);
        var cell1=row.insertCell(0);
        //cell1.style.backgroundColor="#e6e6e6";
        var a=document.createElement("a");
        a.href="javascript:deleteMeasure('"+measureName+','+elmntId+"')";
        a.innerHTML="a";
        a.className="ui-icon ui-icon-close";
        cell1.appendChild(a);
        var cell2=row.insertCell(1);
        //cell2.style.backgroundColor="#e6e6e6";
        cell2.style.color='black';
        cell2.innerHTML=measureName;
        childLI.appendChild(table);
        parentUL.appendChild(childLI);
        i++;
    }
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}

function deleteMeasure(msrName){
    var delelmnt=msrName.split(",");
    var LiObj=document.getElementById(delelmnt[0]+"-"+delelmnt[1]);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;

    var parentUL=document.getElementById("sortable");
    parentUL.removeChild(LiObj);
    var x=(LiObj.id.split("-"))[1]+"-"+(LiObj.id.split("-"))[2];
    var i=0;
    for(i=0;i<msrArray.length;i++){
        if(msrArray[i]==x)
            msrArray.splice(i,1);
    }
}
 */

function createMeasures(measureName,elmntId){
    var i=0;
    var parentUL=document.getElementById("sortable");
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
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}

function deleteMeasure(msrName){

    var LiObj=document.getElementById(msrName);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;
    var parentUL=document.getElementById("sortable");
    parentUL.removeChild(LiObj);
    var x=msrName.replace("Msr","");
    var i=0;
    for(i=0;i<msrArray.length;i++){
        if(msrArray[i]==x)
            msrArray.splice(i,1);
    }
}


function buildParams(){
    var params=getBuildedParams();
    var paramNames=getBuildedParamNames();
    $.ajax({
        url: 'reportViewer.do?reportBy=buildParams&params='+params+'&foldersIds='+buildFldIds()+'&paramNames='+paramNames+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {
        }
    });
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

                if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
                }else if(paramIds[j].id=='AS_OF_DATE1' || paramIds[j].id=='AS_OF_DATE2' || paramIds[j].id=='CMP_AS_OF_DATE1' || paramIds[j].id=='CMP_AS_OF_DATE2'){
                }else if(paramIds[j].id=='AS_OF_WEEK' || paramIds[j].id=='AS_OF_WEEK1'){
                }else if(paramIds[j].id=='AS_OF_MONTH' || paramIds[j].id=='AS_OF_MONTH1'){
                }else if(paramIds[j].id=='AS_OF_QUARTER' || paramIds[j].id=='AS_OF_QUARTER1'){
                }else if(paramIds[j].id=='AS_OF_YEAR' || paramIds[j].id=='AS_OF_YEAR1'){
                }else {
                    var row=divTable.insertRow(k);
                    var cell1=row.insertCell(0);
                    var divinput=document.createElement("input");
                    divinput.type='checkbox';
                    divinput.name='rowParamId';
                    divinput.id='rowParamId'+j;
                    //divinput.onchange=showRowTable;
                    pIds=(paramIds[j].id).split("-");

                    //
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
            if(timeDimArray.length!=0){
                row=divTable.insertRow(0);
                cell1=row.insertCell(0);
                divinput=document.createElement("input");
                divinput.type='checkbox';
                divinput.name='rowParamId';
                divinput.id='rowParamId-Time';
                //divinput.onchange=showRowTable;
                pIds=(divinput.id).split("-");

                if(prevRowParams.search(pIds[1])!=-1){
                    divinput.checked="true";
                }

                divinput.value='Time';
                cell1.appendChild(divinput);
                cell2=row.insertCell(0);
                cell2.id='rowParam-Time';
                //var paramName=paramIds[j].getElementsByTagName("td");
                cell2.innerHTML='TIME';
                k++;
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
function showColParams(){
    var params="";
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

        var tabColDiv=document.getElementById("cepDiv");
        if(tabColDiv.style.display=='none'){
            tabColDiv.style.display='block';

            var divColTable=document.createElement("table");
            var prevColParams= document.getElementById("CEPIds").value

            for(var j=0;j<paramIds.length;j++){
                if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
                }else if(paramIds[j].id=='AS_OF_DATE1' || paramIds[j].id=='AS_OF_DATE2' || paramIds[j].id=='CMP_AS_OF_DATE1' || paramIds[j].id=='CMP_AS_OF_DATE2'){
                }else if(paramIds[j].id=='AS_OF_WEEK' || paramIds[j].id=='AS_OF_WEEK1'){
                }else if(paramIds[j].id=='AS_OF_MONTH' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
                }else if(paramIds[j].id=='AS_OF_MONTH1' || paramIds[j].id=='AS_OF_MONTH2'){
                }else if(paramIds[j].id=='AS_OF_QUARTER' || paramIds[j].id=='AS_OF_QUARTER1'){
                }else if(paramIds[j].id=='AS_OF_YEAR' || paramIds[j].id=='AS_OF_YEAR1'){
                }else {
                    var colRow=divColTable.insertRow(k);
                    var colCell1=colRow.insertCell(0);
                    var divColinput=document.createElement("input");
                    divColinput.type='checkbox';
                    divColinput.name='colParamId';
                    divColinput.id='colParamId'+j;
                    //divColinput.onchange=showColTable;
                    pIds=(paramIds[j].id).split("-");
                    // alert("Param ids is "+pIds[1]+" prevColParams is "+prevColParams)

                    //

                    if(prevColParams.search(pIds[1])!=-1){
                        divColinput.checked="true";
                    }

                    divColinput.value=pIds[1];
                    colCell1.appendChild(divColinput);
                    var colCell2=colRow.insertCell(0);
                    colCell2.id='colParam'+j;
                    var paramName=paramIds[j].getElementsByTagName("td");
                    colCell2.innerHTML=paramName[1].innerHTML;
                    k++;
                }
            }
            if(timeDimArray.length!=0){
                colRow=divColTable.insertRow(0);
                colCell1=colRow.insertCell(0);
                divColinput=document.createElement("input");
                divColinput.type='checkbox';
                divColinput.name='colParamId';
                divColinput.id='colParamId-Time';
                //divColinput.onchange=showColTable;
                pIds=(divColinput.id).split("-");

                if(prevColParams.search(pIds[1])!=-1){
                    divColinput.checked="true";
                }

                divColinput.value='TIME';
                colCell1.appendChild(divColinput);
                colCell2=colRow.insertCell(0);
                colCell2.id='colParam-Time';
                //var paramName=paramIds[j].getElementsByTagName("td");
                colCell2.innerHTML='TIME';
                k++;
            }
            tabColDiv.innerHTML='';
            tabColDiv.appendChild(divColTable);
        }
        else{
            tabColDiv.style.display='none';
            getColumnEdgeParams();
        }
    }
    else{
        alert("Please select Parameters")
    }
}

function dispParameters(){
    var params=getBuildedParams();
    var paramNames=getBuildedParamNames();
    var showTR=document.getElementById("lovParams");
    showTR.style.display = '';
    var paramDisp=document.getElementById("paramDisp");

    paramDisp.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportViewer.do?reportBy=dispParams&params='+params+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {
            if(data!=""){

             
                paramDisp.innerHTML=data;
            }
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
        if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_WEEK' || paramIds[i].id=='AS_OF_WEEK1'){
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_MONTH' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_MONTH1' || paramIds[i].id=='AS_OF_MONTH2'){
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_QUARTER' || paramIds[i].id=='AS_OF_QUARTER1'){
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_YEAR' || paramIds[i].id=='AS_OF_YEAR1'){
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else {
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

        if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2'){
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

function getRowEdgeParams(){
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
        url: 'reportViewer.do?reportBy=buildTable&buildTableChange=REP&rowEdgeParams='+rowEdgeParams+'&rowEdgeParamsNames='+REPNames+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
        }
    });
}
function getColumnEdgeParams(){
    var colEdgeParams="";
    var CEPNames="";
    var colParamIdObj=document.getElementsByName("colParamId");

    var chkCount=0;

    for(var i=0;i<colParamIdObj.length;i++){
        if(colParamIdObj[i].checked){
            colEdgeParams=colEdgeParams+","+colParamIdObj[i].value;

            var partdObj=colParamIdObj[i].parentNode;
            var parTrObj=partdObj.parentNode;
            var childObj=parTrObj.childNodes

            CEPNames=CEPNames+","+childObj[0].innerHTML;
            chkCount++;

        }
    }
    if(colEdgeParams!=""){
        colEdgeParams=colEdgeParams.substring(1);
        CEPNames=CEPNames.substring(1);
    }
    document.getElementById("CEPIds").value=colEdgeParams;
    //if(colEdgeParams!=""){
    if(chkCount==1 || chkCount==0){
        $.ajax({
            url: 'reportViewer.do?reportBy=buildTable&buildTableChange=CEP&colEdgeParams='+colEdgeParams+'&colEdgeParamsNames='+CEPNames+'&REPORTID='+document.getElementById("REPORTID").value,
            success: function(data){

            }
        });
    }
    else{
        alert("Please Select only one Column Edge")
    }
//}
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

/*
 function dispMeasures(){
    var msrs="";
    var msrsName="";
    var msrNameOnly="";
    var msrUl=document.getElementById("sortable");
    var msrIds=msrUl.getElementsByTagName("li");
    for(var i=0;i<msrIds.length;i++){
        var measureIds=(msrIds[i].id).split("-");
        msrs=msrs+","+measureIds[2];
        msrsName=msrsName+","+measureIds[0]+"-"+measureIds[2];
        msrNameOnly=msrNameOnly+","+measureIds[0];
    }

    if(msrIds.length!=0){
        msrs=msrs.substring(1);
        msrsName=msrsName.substring(1);
        msrNameOnly=msrNameOnly.substring(1);

        msrNameOnly=msrNameOnly.replace("%","^");
        parent.document.getElementById("MsrIds").value=msrs;
        parent.document.getElementById("Measures").value=msrsName;

        parent.document.getElementById("paramDisp").innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';

        $.ajax({
            url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+msrNameOnly+'&REPORTID='+parent.document.getElementById("REPORTID").value,
            success: function(data) {
                if(data!=""){
                    parent.document.getElementById("paramDisp").innerHTML=data;
                }
                else{
                    parent.document.getElementById("paramDisp").innerHTML="";
                }
            }
        });
    }
    //
    parent.cancelTabMeasure();
}
 */
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
}
function showGraphs(){

    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    if(paramIds.length!=0){

        var grpIdsStrArray=graphIds.split(",");
        if(grpIdsStrArray.length<=2){
            document.getElementById('graphList').style.display='block';
            document.getElementById('fade').style.display='block';
        }
        else{
            alert("You can select maximum of  two Graphs")
        }
    }
    else{
        alert("Please select Parameters")
    }
}

function getGraphName(name,typeValue){   
    if(document.getElementById("allGraphIds").value==''){      
    }
    else{
        graphCount=document.getElementById("allGraphIds").value.split(",").length;
        graphIds=document.getElementById("allGraphIds").value;
    }
    
     

    graphCount++;    
    graphIds = graphIds+","+graphCount;
    
    if(graphIds!=""){
        graphIds=graphIds.substring(1);
        document.getElementById("allGraphIds").value=graphIds.substring(1);
    }
    else{
        document.getElementById("allGraphIds").value=graphIds;
    }
    parentDiv = document.getElementById('previewDispGraph');
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';


    $.ajax({
        url: 'reportViewer.do?reportBy=buildGraphs&gid='+graphCount+'&grptypid='+typeValue+'&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            parentDiv.innerHTML=data;
        }
    });

    var previewGraphObj=document.getElementById("previewGraph");
    previewGraphObj.style.display='';
    document.getElementById('fade').style.display = 'none'
    document.getElementById('graphList').style.display = 'none'

    document.getElementById("allGraphIds").value=graphIds;
}

function showGraphsList()
{
    if(document.getElementById('grpList').style.display=='none')
        document.getElementById('grpList').style.display='';
    else
        document.getElementById('grpList').style.display='none';
}
function getColumns(currentGraphId,allGraphIds){
    var frameObj=document.getElementById("graphCols");
    var source = "pbgraphColumnsCustomize.jsp?graphId="+currentGraphId+"&folderIds="+buildFldIds()+"&grpIds="+allGraphIds+'&REPORTID='+document.getElementById("REPORTID").value;
    frameObj.src=source;
    frameObj.style.display='block';
    document.getElementById('fade').style.display='block';
}
function currGraph(val)
{//
    currentGraphId = val.id;
}
function cancelTabMeasure(){
    var frameObj=document.getElementById("dataDispmem");
    var divObj=document.getElementById("measures");
    frameObj.style.display='none';
    divObj.style.display='none';
    document.getElementById('fade').style.display='none';
}
function hideDivs()
{
//document.getElementById("getGraphTypes").style.display='none';
//document.getElementById("getGraphSizes").style.display='none';
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

function getUserDims(){
    var foldersIds=buildFldIds();
    var branches="";
    var str;
    //

    //if(foldersIds!=""){
    $.ajax({
        url: 'reportViewer.do?reportBy=getUserDims&foldersIds='+foldersIds+'&REPORTID='+document.getElementById("REPORTID").value,
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
                 accept:'#userDims > li >span',
                drop: function(ev, ui) {
                    var dim=ui.draggable.html();
                    if(dim=='Time-Period Basis'){
                        var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                        var timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    } else if(dim=='Time-Range Basis'){
                        timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
                        timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    }else if(dim=='Time-Week Basis'){
                        timeDim='AS_OF_WEEK,AS_OF_WEEK1';
                        timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    } else if(dim=='Time-Month Basis'){
                        timeDim='AS_OF_MONTH,PRG_PERIOD_TYPE,PRG_COMPARE';
                        timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    }else if(dim=='Time-Compare Month Basis'){
                        timeDim='AS_OF_MONTH1,AS_OF_MONTH2';
                        timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    } else if(dim=='Time-Quarter Basis'){
                        timeDim='AS_OF_QUARTER,AS_OF_QUARTER1';
                        timeDimension=timeDim.split(",");
                        createParams(timeDimension,ui.draggable.html());
                    } else if(dim=='Time-Year Basis'){
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
// }
}
function getBuckets(){
    var foldersIds=buildFldIds();
    var branches1="";
    var str;
    // if(foldersIds!=""){
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=getBuckets&foldersIds='+foldersIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {
            

            $("#userBuckets").html("");
            str=data;
            branches1 = $(str).appendTo("#userBuckets");
            $("#userBuckets").treeview({
                add: branches1
            });

            $('#userBuckets > li >span').draggable({
                helper:"clone",
                effect:["", "fade"]
            });
        }
    });
//}
}

var xmlHttp;
var suggestList="";

/* removed fiunctions from here*/


function cancelCols()
{
    document.getElementById('fade').style.display='none';
    document.getElementById('graphCols').style.display='none';
}

function getColDivs(num)
{
    divStatus = num;
}
function getDimensions(){
//
}
$(function() {
    $("#sortable").sortable();

    $("#sortable").disableSelection();

});
function PreviewTable(){
    //dispTable();
  
    var prevREPIds=document.getElementById("REPIds").value;
    var prevCEPIds=document.getElementById("CEPIds").value;
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
function  dispTable(){
    var previewDispTableDiv;

    var previewTable=document.getElementById("previewTable");
    previewTable.style.display = '';
    previewDispTableDiv=document.getElementById("previewDispTable");
    previewDispTableDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportViewer.do?reportBy=dispTable'+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            //
            if(data!=""){
                previewDispTableDiv.innerHTML=data;
            }
            else{
                previewDispTableDiv.innerHTML="";
            }
        /*
             $("#tablesorter").columnFilters().tablesorter({
                widthFixed: true,
                widgets: ['zebra']
            });
            */
        }
       
    });
}

function previewGraphs(){
    /*
    var editGraphObj=document.getElementById("editGraph");
    editGraphObj.style.display='none';
         */
    var parentDiv = document.getElementById('previewDispGraph');
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    if(paramIds.length!=0){
        var previewGraphObj=document.getElementById("previewGraph");
        previewGraphObj.style.display='';
        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';
        $.ajax({
            url: 'reportViewer.do?reportBy=refreshGraphs'+'&REPORTID='+document.getElementById("REPORTID").value,
            success: function(data){
                parentDiv.innerHTML=data;
            }
        });
    }
    else{
        alert("Please select Parameters")
    }
}
function editGraphs(){
    var editGraphObj=document.getElementById("editGraph");
    editGraphObj.style.display='';

    var previewGraphObj=document.getElementById("previewGraph");
    previewGraphObj.style.display='none';


//
}
function prevMeasures(){
    var prevMsrs=parent.document.getElementById("Measures").value;
    if(prevMsrs.length!=0){
        prevMsrs=prevMsrs.split(",");
        for(var m=0;m<prevMsrs.length;m++){
            var msrElmnts=prevMsrs[m].split("-");
            createMeasures(msrElmnts[0],"elmnt-"+msrElmnts[1]);
        }
    }
}

/* Graph related javascript function added by santhosh.kumar@progenbusiness.com on 01/10/2009*/
function graphColumnsDisp(grpId,allGrpIds){
    getColumns(grpId,allGrpIds);
}

function changeGrpColumns(grpId){
}

function graphTypesDisp(dispgrptypObj){
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
}

function changeGrpType(grpTypeId,grpId,graphIds){
    var parentDiv = document.getElementById('previewDispGraph');
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportViewer.do?reportBy=buildGraphs&gid='+grpId+'&grptypid='+grpTypeId+'&graphChange=GrpType&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            parentDiv.innerHTML=data;
        }
    });
}

function graphSizesDisp(dispgrpsizesObj){
    if(dispgrpsizesObj.style.display=='none'){
        dispgrpsizesObj.style.display='';
    }
    else{
        dispgrpsizesObj.style.display='none';
    }
}

function changeGrpSize(grpSizeId,grpId,graphIds){
    var parentDiv = document.getElementById('previewDispGraph');
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportViewer.do?reportBy=buildGraphs&gid='+grpId+'&grpsizeid='+grpSizeId+'&graphChange=GrpSize&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            parentDiv.innerHTML=data;
        }
    });

}
function deleteGraph(graphId,GraphIds){
    var parentDiv = document.getElementById('previewDispGraph');
    var res=confirm("Are you sure you want to delete the graph");
    if(res){
        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';
        $.ajax({
            url: 'reportViewer.do?reportBy=buildGraphs&gid='+graphId+'&grpIds='+GraphIds+'&graphChange=DeleteGraph'+'&REPORTID='+document.getElementById("REPORTID").value,
            success: function(data){
                parentDiv.innerHTML=data;
            }
        });
        var temp=graphIds;
        var temp2="";
        var grpIdsStrArray=temp.split(",");

        for(var i=0;i<grpIdsStrArray.length;i++){
            if(grpIdsStrArray[i]!=graphId){
                temp2=temp2+","+grpIdsStrArray[i];
            }
        }
        if(temp2!=""){
            temp2=temp2.substring(1,temp2.length);
            graphIds=temp2;
        }
        document.getElementById("allGraphIds").value=graphIds;
    }
    else{
}
}
function dispSwapGraphAnalysis(dispColSeqObj){
    if(dispColSeqObj.style.display=='none'){
        dispColSeqObj.style.display='';
    }
    else{
        dispColSeqObj.style.display='none';
    }
}
function swapGraphAnalysis(grpId,swapBy){
    document.getElementById('gid').value=grpId;
    document.getElementById('swapBy').value=swapBy;
    document.getElementById('graphChange').value='SwapGraph';
    document.forms.myGrpForm.submit();
}
function dispZoomImg(grpId){
    document.getElementById(grpId).style.display='block';
    document.getElementById('fade').style.display='block';

}
function closeZoomImg(grpId){
    document.getElementById(grpId).style.display='none';
    document.getElementById('fade').style.display='none';
}
function dispGraphDetails(currentGraphId,allGraphIds){
    getGraphDetails(currentGraphId,allGraphIds);

}
function getGraphDetails(currentGraphId,allGraphIds){
    var frameObj=document.getElementById("graphDtls");
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="50px" height="50px"  style="position:absolute" ></center>';
    var source = "PbGraphDetails.jsp?graphId="+currentGraphId+"&grpIds="+allGraphIds+'&REPORTID='+document.getElementById("REPORTID").value;
    frameObj.src=source;
    frameObj.style.display='block';
    document.getElementById('fade').style.display='block';
}
function cancelGraphDetails(){
    var frameObj=document.getElementById("graphDtls");
    frameObj.style.display='none';
    document.getElementById('fade').style.display='none';
}
function saveGraphDetails(currentGraphId,allGraphIds){
}
//code related to New Customize report

function displayPrevParams(){
    var PrevParams=document.getElementById("PrevParams").value;
    var PrevTimeParams=document.getElementById("PrevTimeParams").value;

    if(PrevParams.length!=0){
        PrevParams=PrevParams.split(",");

        for(var m=0;m<PrevParams.length;m++){
            var msrElmnts=PrevParams[m].split("-");
            var pp=paramArray.toString();
            if(pp.match(msrElmnts[0])==null){
                paramArray.push(msrElmnts[0])
            }
        }
    }
    if(PrevTimeParams.length!=0){
        PrevTimeParams=PrevTimeParams.split(",");

        for( m=0;m<PrevTimeParams.length;m++){
            msrElmnts=PrevTimeParams[m].split("-");
            pp=timeDimArray.toString();
            if(pp.match(msrElmnts[0])==null){
                timeDimArray.push(msrElmnts[0])
            }
        }
    }
}

