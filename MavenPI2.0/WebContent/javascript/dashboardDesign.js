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
var grpArray=new Array();
var kpiArray=new Array();
var kpiDivArray=new Array();

var rowEdgeList='';
var columnEdgeList='';
var firstGraphId = '';
var secondGraphId = '';
var graphIds = '';
var comntDivId='';


//added by k
var comndimId='';
var commonparamArray=new Array();
var graphid = '';
var dashId='';
var dashletid = '';
var refreportId = '';
var kpimasterId = '';
var dispseq = '';
var disptype = '';
var fromdesigner = '';
var folderid = ''
var selectedgrRepId = '';
var drillid = '';
var elementids = '';
var flag;
var newDashletName = '';
var folderidGlobal = '';
var dashboardid = '';
var kpiGrname='';
var editdbrd='';
var initialText = "ENTER YOUR NOTES HERE";
var prevTxtValue = "";
var insertedNew = null;
var divids=null;
var drillviewbyids = new Array();
var drillviewbynames = new Array();
var ids = new Array();
var names = new Array();
var strOperators = ["<", ">", "<=", ">=", "=", "!=", "<>"];
var alertTypes = ["high","medium","low"];
var allClmnHeaderNames="";
isIE=document.all;
isNN=!document.all&&document.getElementById;
isN4=document.layers;
isHot=false;

document.onmousedown=ddInit;
document.onmouseup=xy;

//stickNoteCount = " =rowCount ";
stickNoteCount = 1;
//stickNoteCount++;

var radioValue='Jfree';
var grpId="";



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
$("#HideColumns").dialog({
        autoOpen: false,
        height: 250,
        width: 200,
        position: 'justify',
         title:'HideColumns',
        modal: true
    });



});


function changeTargetValue(val)
{
    var values=val.split(":");
    var eleId=values[0];
    var masterKpiId=values[1];
    var timeLevel=values[2];
    var targetValue=values[3];
    var reportId=values[4];
    var dashletId = values[5];
    var typeKpi = values[6];

    $("#kpiTarget").dialog('open');
    var frameObj = document.getElementById("kpiTargetFrame");
    frameObj.src = "changeTargetValuesForKpi.jsp?elementId="+eleId+'&masterKpiId='+masterKpiId+'&timeLevel='+timeLevel+'&targetValue='+targetValue+'&reportId='+reportId+'&dashletId='+dashletId+'&typeKpi='+typeKpi;
}

function closeTarget()
{     ////alert("close target kpi")
   window.location.reload(true);
   ////alert("close target kpi inner  ")
   parent.$("#kpiTarget").dialog('close');
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

    if(foldersIds!=""){
        $.ajax({
            url: 'dashboardTemplateAction.do?templateParam2=getUserDims&foldersIds='+foldersIds+'&dashboardId='+document.getElementById("dbrdId").value,
            success: function(data) {
                $("#userDims").html("");
                str=data;
                branches = $(str).appendTo("#userDims");
                $("#userDims").treeview({
                    add: branches
                });

                var dragUserDim=$('#userDims > li >span');
                var draggableDims=$('#draggableDims');

                $(dragUserDim).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $('#favourParams > li >span').draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(draggableDims).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#userDims > li >span,#favourParams> li >span',
                    drop: function(ev, ui) {
                        var dim=ui.draggable.html();

                        if(dim=='Time-Period Basis'){
                            document.getElementById("timeDimension").value=dim;
                            var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                            var timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        } else if(dim=='Time-Range Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Rolling Period'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_DATE,PRG_DAY_ROLLING';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Month Range Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_DMONTH1,AS_OF_DMONTH2,CMP_AS_OF_DMONTH1,CMP_AS_OF_DMONTH2';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Quarter Range Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_DQUARTER1,AS_OF_DQUARTER2,CMP_AS_OF_DQUARTER1,CMP_AS_OF_DQUARTER2';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Year Range Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_DYEAR1,AS_OF_DYEAR2,CMP_AS_OF_DYEAR1,CMP_AS_OF_DYEAR2';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Week Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_WEEK,AS_OF_WEEK1';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        } else if(dim=='Time-Month Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_MONTH,PRG_PERIOD_TYPE,PRG_COMPARE';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Compare Month Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_MONTH1,AS_OF_MONTH2';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        } else if(dim=='Time-Quarter Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_QUARTER,AS_OF_QUARTER1';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        } else if(dim=='Time-Year Basis'){
                            document.getElementById("timeDimension").value=dim;
                            timeDim='AS_OF_YEAR,AS_OF_YEAR1';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else {
                            // end of adding
                            getDimDetails(ui.draggable.html());
                        }
                    }
                });
            }
        });
    }
}
function getDimDetails(dimId){
    var dimension=document.getElementById("dimName-"+dimId);
    var params=dimension.getElementsByTagName("li");
    dimension=createParams(params,dimId);
}
function createParams(params,dimId){
    //////alert("createParams")
    var parentUL=document.getElementById("sortable");
    var i=0;

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

                if(params[j]=='PRG_COMPARE'){
                    var a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                var cell2=row.insertCell(1);

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

                if(params[k]=='CMP_AS_OF_DATE1' || params[k]=='CMP_AS_OF_DATE2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);

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

    }else if(dimId=='Time-Rolling Period'){
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
                cell2=row.insertCell(1);
                if(k==0)
                    cell2.innerHTML='Date';
                else if(k==1)
                    cell2.innerHTML='Rolling Period';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }

    }else if(dimId=='Time-Month Range Basis'){



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

                if(params[k]=='CMP_AS_OF_DMONTH1' || params[k]=='CMP_AS_OF_DMONTH2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);

                if(k==0)
                    cell2.innerHTML='From Month';
                else if(k==1)
                    cell2.innerHTML='To Month';
                else if(k==2)
                    cell2.innerHTML='Compare From Month';
                else if(k==3)
                    cell2.innerHTML='Compare To Month';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }

    }else if(dimId=='Time-Quarter Range Basis'){
        /*if(timeDimArray.length!=0){
            for(var tdim=0;tdim<timeDimArray.length;tdim++){
                deleteTimeDimParam(timeDimArray[tdim],dimId);
            }
             /*   if(timeDimArray[tdim]=='AS_OF_DATE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
                if(timeDimArray[tdim]=='PRG_PERIOD_TYPE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
                if(timeDimArray[tdim]=='PRG_COMPARE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
            }*/

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

                if(params[k]=='CMP_AS_OF_DQUARTER1' || params[k]=='CMP_AS_OF_DQUARTER2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);

                if(k==0)
                    cell2.innerHTML='From Quarter';
                else if(k==1)
                    cell2.innerHTML='To Quarter';
                else if(k==2)
                    cell2.innerHTML='Compare From Quarter';
                else if(k==3)
                    cell2.innerHTML='Compare To Quarter';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }

    }else if(dimId=='Time-Year Range Basis'){
        /*if(timeDimArray.length!=0){
            for(var tdim=0;tdim<timeDimArray.length;tdim++){
                if(timeDimArray[tdim]=='AS_OF_DATE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
                if(timeDimArray[tdim]=='PRG_PERIOD_TYPE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
                if(timeDimArray[tdim]=='PRG_COMPARE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
            }
        }*/
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

                if(params[k]=='CMP_AS_OF_DYEAR1' || params[k]=='CMP_AS_OF_DYEAR2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);

                if(k==0)
                    cell2.innerHTML='From Year';
                else if(k==1)
                    cell2.innerHTML='To Year';
                else if(k==2)
                    cell2.innerHTML='Compare From Year';
                else if(k==3)
                    cell2.innerHTML='Compare To Year';
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

                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);

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

                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);

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

                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);

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

                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);

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

                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
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
                for(var i1=0;i1<xarr.length;i1++){
                    if(xarr[i1]==span[0].innerHTML){
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
                childLI.className='navtitle-hover paramMenu';
                childLI.onmouseup=function(){
                    contMenu(this);
                };
                table=document.createElement("table");
                table.id=dimId+i;
                row=table.insertRow(0);
                cell1=row.insertCell(0);
                // cell1.style.backgroundColor="#e6e6e6";
                a=document.createElement("a");
                a.href="javascript:deleteParam('"+spanId[1]+"','"+dimId+"')";
                a.innerHTML="a";
                a.className="ui-icon ui-icon-close";
                cell1.appendChild(a);
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#e6e6e6";
                cell2.innerHTML=span[0].innerHTML;
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }
    setVals(paramIdArray,paramArray);
    buildParams(dimId);
    $("#sortable").sortable({
        update : function(event,ui){
            buildParams(dimId);
        }
    }

);

    $("#sortable").disableSelection();

commonparamArray=paramArray;

//////alert("commonparamArray="+commonparamArray)

}




function deleteParam(index,dimId){
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
    buildParams(dimId);
}
function showParams(){
    var params="";
    var paramswithtime="";
    var paramtimeIds="";
    var hideTR=document.getElementById("dragDims");
    hideTR.style.display='none';
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");

    for(var i=0;i<paramIds.length;i++){
        if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DMONTH1' || paramIds[i].id=='AS_OF_DMONTH2' || paramIds[i].id=='CMP_AS_OF_DMONTH1' || paramIds[i].id=='CMP_AS_OF_DMONTH2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DQUARTER1' || paramIds[i].id=='AS_OF_DQUARTER2' || paramIds[i].id=='CMP_AS_OF_DQUARTER1' || paramIds[i].id=='CMP_AS_OF_DQUARTER2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DYEAR1' || paramIds[i].id=='AS_OF_DYEAR2' || paramIds[i].id=='CMP_AS_OF_DYEAR1' || paramIds[i].id=='CMP_AS_OF_DYEAR2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_DAY_ROLLING'){
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
        }else{
            //////alert('in showparams params  '+paramIds[i])
            var pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
    }
    //////alert('paramswithtime  '+paramswithtime)
    params=params.substr(1);

    //////alert('params='+params)
    dispParams(params);
}


//added by k
function UpdateParams(){
    var params="";
    var paramswithtime="";
    var paramtimeIds="";
    var hideTR=document.getElementById("dragDims");
   // hideTR.style.display='none';
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");

    for(var i=0;i<paramIds.length;i++){
        if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DMONTH1' || paramIds[i].id=='AS_OF_DMONTH2' || paramIds[i].id=='CMP_AS_OF_DMONTH1' || paramIds[i].id=='CMP_AS_OF_DMONTH2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DQUARTER1' || paramIds[i].id=='AS_OF_DQUARTER2' || paramIds[i].id=='CMP_AS_OF_DQUARTER1' || paramIds[i].id=='CMP_AS_OF_DQUARTER2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DYEAR1' || paramIds[i].id=='AS_OF_DYEAR2' || paramIds[i].id=='CMP_AS_OF_DYEAR1' || paramIds[i].id=='CMP_AS_OF_DYEAR2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_DAY_ROLLING'){
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
        }else{
            //////alert('in showparams params  '+paramIds[i])
            var pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
    }
    //////alert('paramswithtime  '+paramswithtime)
    params=params.substr(1);

    //////alert('params='+params)
    ///dispParams(params);
if(params!="")
    $.post('dashboardTemplateAction.do?templateParam2=resetParameters&params='+params+'&dashboardId='+parent.document.getElementById("dbrdId").value,function(data){
    });
//    $.ajax({
//            url: 'dashboardTemplateAction.do?templateParam2=resetParameters&params='+params+'&dashboardId='+parent.document.getElementById("dbrdId").value,
//          success: function(data) {
//            }
//        });



}



function dispParams(params){


    var timeparams=getBuildedTimeParams();
    var showTR=document.getElementById("lovParams");
    showTR.style.display = '';
    var paramDisp=document.getElementById("paramDisp");
    document.getElementById("favParams").style.display='none';
    paramDisp.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="80px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=dispDbrdParams&params='+params+'&timeparams='+timeparams+'&paramArray='+paramArray+'&dashboardId='+document.getElementById("dbrdId").value,
        success: function(data){
            if(data!=""){


                paramDisp.innerHTML=data;
            }
        }
    });
}
function showMbrs()
{
    var hideTR=document.getElementById("dragDims");
    hideTR.style.display='';
    document.getElementById("favParams").style.display='';

    var showTR=document.getElementById("lovParams");
    showTR.style.display = 'none';
}
function buildParams(dimId){

    //////alert("buildParams")
    //comndimId=dimId;
    var params=getBuildedParams();
    var timeparams=getBuildedTimeParams();
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=buildDbrdParams&dimId='+dimId+'&params='+params+'&timeparams='+timeparams+'&paramArray='+paramArray+'&foldersIds='+buildFldIds()+'&dashboardId='+document.getElementById("dbrdId").value,
        success: function(data) {
        }
    });
}
function getBuildedParams()
{
    var params="";
    var paramswithtime="";
    var paramtimeIds="";
    var paramUl=document.getElementById("sortable");
   // ////alert("paramUl="+paramUl)
    var paramIds=paramUl.getElementsByTagName("li");
   // ////alert("paramIds="+paramIds)
    for(var i=0;i<paramIds.length;i++){
        var pIds=""
        if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DMONTH1' || paramIds[i].id=='AS_OF_DMONTH2' || paramIds[i].id=='CMP_AS_OF_DMONTH1' || paramIds[i].id=='CMP_AS_OF_DMONTH2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DQUARTER1' || paramIds[i].id=='AS_OF_DQUARTER2' || paramIds[i].id=='CMP_AS_OF_DQUARTER1' || paramIds[i].id=='CMP_AS_OF_DQUARTER2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DYEAR1' || paramIds[i].id=='AS_OF_DYEAR2' || paramIds[i].id=='CMP_AS_OF_DYEAR1' || paramIds[i].id=='CMP_AS_OF_DYEAR2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_WEEK' || paramIds[i].id=='AS_OF_WEEK1'){
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_DAY_ROLLING'){
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
        }else{
             pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
    }
    if(params!="" && pIds.length!=0){
        document.getElementById("saveFavParams").disabled=false;
        params=params.substr(1);
    }
    if(timeDimArray.length!=0)
        paramswithtime=paramswithtime.substr(1);
    //////alert('getBuildedParams params are  '+params)
    //////alert('getBuildedParams paramswithtime are  '+paramswithtime)
    return params;
}

function getBuildedTimeParams()
{
    var params="";
    var paramswithtime="";
    var paramtimeIds="";
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    for(var i=0;i<paramIds.length;i++){
        if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_PERIOD_TYPE' || paramIds[i].id=='PRG_COMPARE'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DMONTH1' || paramIds[i].id=='AS_OF_DMONTH2' || paramIds[i].id=='CMP_AS_OF_DMONTH1' || paramIds[i].id=='CMP_AS_OF_DMONTH2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DQUARTER1' || paramIds[i].id=='AS_OF_DQUARTER2' || paramIds[i].id=='CMP_AS_OF_DQUARTER1' || paramIds[i].id=='CMP_AS_OF_DQUARTER2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DYEAR1' || paramIds[i].id=='AS_OF_DYEAR2' || paramIds[i].id=='CMP_AS_OF_DYEAR1' || paramIds[i].id=='CMP_AS_OF_DYEAR2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_DAY_ROLLING'){
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
        }else{
            var pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
    }
    if(params!="" && pIds.length!=0){
        document.getElementById("saveFavParams").disabled=false;
        params=params.substr(1);
    }
    if(timeDimArray.length!=0)
        paramswithtime=paramswithtime.substr(1);
    //////alert('getBuildedParams params are  '+params)
    //////alert('getBuildedParams paramswithtime are  '+paramswithtime)
    return paramswithtime;
}

function deleteTimeDimParam(index,dimId){
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
    buildParams(dimId);
}
function showGraphs(divId){
    UpdateParams();
    var frameObj=document.getElementById("dataDispmem");
    var source="dashboardTemplateAction.do?templateParam2=getGraphReports&buzRoles="+buildFldIds()+'&dashboardId='+parent.document.getElementById("dbrdId").value;
    frameObj.src=source;
    document.getElementById('divId').value=divId;
    $("#graphsDialog").dialog('open');
}

function cancelRepGraph(){
    $("#graphsDialog").dialog('close');
}
function createKpis(kpiName,elmntId){
    var i=0;
    var parentUL=document.getElementById("sortable");
    var parentDiv=parent.document.getElementById("editDispKpi");
    var x=kpiArray.toString();
    if(x.match(elmntId)==null){
        kpiArray.push(kpiName+"^"+elmntId)
        var childLI=document.createElement("li");
        childLI.id=kpiName+"^"+elmntId;
        childLI.style.width='auto';
        childLI.style.height='auto';
        childLI.style.color='white';
        childLI.className='navtitle-hover';
        var table=document.createElement("table");
        table.id=kpiName+i;
        var row=table.insertRow(0);
        var cell1=row.insertCell(0);
        // cell1.style.backgroundColor="#e6e6e6";
        var a=document.createElement("a");
        a.href="javascript:deleteKpi('"+kpiName+'^'+elmntId+"')";
        a.innerHTML="a";
        a.className="ui-icon ui-icon-close";
        cell1.appendChild(a);
        var cell2=row.insertCell(1);
        cell2.style.color='black';
        cell2.innerHTML=kpiName;
        childLI.appendChild(table);
        parentUL.appendChild(childLI);
        i++;
    }
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}
function createKpisGraph(kpiName,elmntId){
    var i=0;
    var parentUL=document.getElementById("sortable");
    var parentDiv=parent.document.getElementById("editDispKpi");
    var x=kpiArray.toString();
    if(x==""){
        if(x.match(elmntId)==null){
            kpiArray.push(kpiName+"^"+elmntId)
            var childLI=document.createElement("li");
            childLI.id=kpiName+"^"+elmntId;
            childLI.style.width='auto';
            childLI.style.height='auto';
            childLI.style.color='white';
            childLI.className='navtitle-hover';
            var table=document.createElement("table");
            table.id=kpiName+i;
            var row=table.insertRow(0);
            var cell1=row.insertCell(0);
            // cell1.style.backgroundColor="#e6e6e6";
            var a=document.createElement("a");
            a.href="javascript:deleteKpi('"+kpiName+'^'+elmntId+"')";
            a.innerHTML="a";
            a.className="ui-icon ui-icon-close";
            cell1.appendChild(a);
            var cell2=row.insertCell(1);
            // cell2.style.backgroundColor="#e6e6e6";
            cell2.style.color='black';
            cell2.innerHTML=kpiName;
            childLI.appendChild(table);
            parentUL.appendChild(childLI);
            i++;
        }
    }else{
        ////alert('Please Select one Measure')
    }
    $("#sortable").sortable();
    $("#sortable").disableSelection();
}
function deleteKpi(kpiName){
    var LiObj=document.getElementById(kpiName);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;

    var parentUL=document.getElementById("sortable");
    parentUL.removeChild(LiObj);
    var x=(LiObj.id);
    var i=0;
    for(i=0;i<kpiArray.length;i++){
        if(kpiArray[i]==x)
            kpiArray.splice(i,1);
    }
}
var showKpisdivId = "";
var showKpistype = "";
function showKpis(divId,type){
    showKpisdivId = divId;
    showKpistype = type;
    //$("#tableList").attr('checked',true);
    document.getElementById('tableList').checked=false;
    $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
    document.getElementById('divId').value=divId;
    var frameObj=document.getElementById("kpidataDispmem");
    var source="dashboardTemplateAction.do?templateParam2=getKpis&foldersIds="+parent.buildFldIds()+'&divId='+divId+'&kpiType='+type+'&dashboardId='+parent.document.getElementById("dbrdId").value;
    frameObj.src=source;
    $("#kpisDialog").dialog('option','title','Edit KPI')
    $("#kpisDialog").dialog('open');
}
function showKpiGraphs(divId){
    UpdateParams();
    document.getElementById('divId').value=divId;
    var frameObj=document.getElementById("kpigraphdataDispmem");
    var source="dashboardTemplateViewerAction.do?templateParam2=getKpisGraphs&foldersIds="+parent.buildFldIds()+'&dashboardId='+parent.document.getElementById("dbrdId").value;
    frameObj.src=source;
    $("#kpisGraphsDialog").dialog('open');
}
function cancelRepKpiGraph(){
    $("#kpisGraphsDialog").dialog('close');
}
function cancelRepKpi(){
    $("#kpisDialog").dialog('close');
}
function cancelReorderKpi(){
    $("#ReorderkpisDialog").dialog('close');
}


function Viewer_prevKpis(){
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=kpisBydivid&divId='+parent.document.getElementById("divId").value+'&dashboardId='+parent.document.getElementById("dbrdId").value,
        success: function(data) {
            if(data!=""){
                var prevKpis=data;
                if(prevKpis.length!=0){
                    prevKpis=prevKpis.substr(1, prevKpis.length-2).split(",");
                    for(var k=0;k<prevKpis.length;k++){
                        var kpiElmnts=prevKpis[k].split("^");
                        if(kpiElmnts[0]!='ul'){
                            createKpis(kpiElmnts[0],kpiElmnts[1]);
                        }
                    }
                }
            }
        }
    });
}



function prevKpis(divId){
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=kpisBydivid&divId='+divId+'&dashboardId='+parent.document.getElementById("dbrdId").value,
        success: function(data) {
            if(data!=""){
                var prevKpis=data;
                if(prevKpis.length!=0){
                    prevKpis=prevKpis.substr(1, prevKpis.length-2).split(",");
                    for(var k=0;k<prevKpis.length;k++){
                        var kpiElmnts=prevKpis[k].split("^");
                        if(kpiElmnts[0]!='ul'){
                            createKpis(kpiElmnts[0],kpiElmnts[1]);
                        }
                    }
                }
            }
        }
    });
}


function Viewer_dispKpis(dayDiff,kpiType,timeParams,params,timeDim,more, dashletId, kpiMasterId){
    var kpis="";
    var kpisName="";
    var timeDimid=timeDim;
    var kpiUl=document.getElementById("sortable");
    var kpiIds=kpiUl.getElementsByTagName("li");

    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        kpisName=kpisName+","+kpiIds[i].id;
    }
    if(kpiIds.length!=0){
        kpis=kpis.substr(1);
        kpisName=kpisName.substr(1);
        var divId=parent.document.getElementById("divId").value;
        var dbrdId = parent.document.getElementById("dbrdId").value
        comntDivId=parent.document.getElementById("divId").value;

        parent.document.getElementById("kpiIds").value=kpis;
        parent.document.getElementById("kpis").value=kpisName;
        parent.document.getElementById("kpiType").value=kpiType;

        kpisName=kpisName.replace("%","%".charCodeAt(0),"gi");
        $.ajax({
            //url: 'dashboardTemplateViewerAction.do?templateParam2=buildKpis&Kpis='+kpis+'&KpiNames='+kpisName+'&divId='+divId+'&kpiType='+kpiType+'&dashboardId='+dbrdId+'&countkpis='+more+"&kpiMasterId="+divId,
            url: 'dashboardViewer.do?reportBy=buildKPI&Kpis='+kpis+'&KpiNames='+kpisName+'&divId='+divId+'&kpiType='+kpiType+'&dashboardId='+dbrdId+'&countkpis='+more+"&kpiMasterId="+kpiMasterId+"&dashletId="+dashletId,
            success: function(data) {
                parent.cancelRepKpi();
                //parent.Viewer_previewKpis(timeDimid,dayDiff,kpiType,timeParams,params,more);
                parent.displayKPI(dashletId, dbrdId, "","",kpiMasterId, "","","", false);
            }
        });
    }
}

function dispKpis(dayDiff,kpiType,divId){
    var kpis="";
    var kpisName="";
    var kpiUl=document.getElementById("sortable");
    var kpiIds=kpiUl.getElementsByTagName("li");
    var dbrdId = parent.document.getElementById("dbrdId").value;
    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        kpisName=kpisName+","+kpiIds[i].id;
    }
    if(kpiIds.length!=0){
        kpis=kpis.substr(1);
        kpisName=kpisName.substr(1);

        comntDivId=parent.document.getElementById("divId").value;
        var dashletId = divId;
        parent.document.getElementById("kpiIds").value=kpis;
        parent.document.getElementById("kpis").value=kpisName;
        parent.document.getElementById("kpiType").value=kpiType;
        kpisName=kpisName.replace("%","%".charCodeAt(0),"gi");

//        $.ajax({
//            url: 'dashboardViewer.do?reportBy=buildKPI&Kpis='+encodeURIComponent(kpis)+'&KpiNames='+encodeURIComponent(kpisName)+'&divId='+divId+'&kpiType='+encodeURIComponent(kpiType)+'&dashboardId='+dbrdId+"&kpiMasterId="+divId+"&dashletId="+dashletId,
//            success: function(data) {
//                parent.cancelRepKpi();
//                parent.displayKPI(dashletId, dbrdId, "","",divId, "","","","true");
//            }
//        });
//alert("kpis"+kpis)
//alert("KpiNames"+kpisName)
         if(kpiType=='KpiWithGraph')
           {
                    var divIdObj=parent.document.getElementById("Dashlets-"+dashletId);
                    var grpSize='Small';
                    var grpType='Pie';


                if (divIdObj == null){
                divIdObj = parent.document.getElementById(dashletId);
//                if (parent.document.getElementById("hideDiv") != null){
//                    var  hideDiv=parent.document.getElementById("hideDiv").value;
//                    parent.document.getElementById(hideDiv).style.display='block';
//                }
            }
            divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
            $.ajax({
                url: 'dashboardViewer.do?reportBy=buildKPIWithGraph&grpColumns='+kpis+'&grpColumnsNames='+kpisName+'&divId='+divId+'&kpiType='+kpiType+'&dashboardId='+dbrdId+'&kpiMasterId='+divId+'&dashletId='+dashletId+'&grpSize='+grpSize+'&grpType='+grpType+'&grpId='+(divId+1)+'&viewby=DEFAULT',
                success: function(data){
                parent.cancelRepKpi();
                divIdObj.innerHTML =data;
        }
    });

        }
   else{
        $.post(
                     'dashboardViewer.do?reportBy=buildKPI&Kpis='+encodeURIComponent(kpis)+'&KpiNames='+encodeURIComponent(kpisName)+'&divId='+divId+'&kpiType='+encodeURIComponent(kpiType)+'&dashboardId='+dbrdId+"&kpiMasterId="+divId+"&dashletId="+dashletId,
                     function(data){
                       parent.cancelRepKpi();
                       parent.displayKPI(dashletId, dbrdId, "","",divId, "",kpiType,"","true");
                   });
    }
}
}

function buildString(array){
    var str = "";
    for (var i=0;i<array.length;i++){
        str = str + "," + array[i];
    }
    str = str.substr(1, str.length);
    return str;
}

function Viewer_dispKpisGraphsm(){
    var kpis="";
    var kpisName="";
    var kpiUl=document.getElementById("sortable");
    var kpiIds=kpiUl.getElementsByTagName("li");
    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        // kpisName=kpisName+","+kpiIds[i].id;
        kpisName=kpisName+","+dkpiIds[0];
    }
    if(kpiIds.length!=0){
        kpis=kpis.substr(1);
        kpisName=kpisName.substr(1);
        document.getElementById("kpiIds").value=kpis;
        document.getElementById("kpis").value=kpisName;
        $.ajax({
            url: 'dashboardTemplateViewerAction.do?templateParam2=getKpiNeedleValue&kpiIds='+kpis+'&dashboardId='+parent.document.getElementById("dbrdId").value,
            success: function(data) {
                if(data!=""){
                    document.forms.myForm2.action="pbKpiGraphTargetViewer.jsp?foldersIds="+parent.buildFldIds()+'&kpiIds='+kpis+'&kpiName='+kpisName+'&needleValue='+data+'&dashboardId='+parent.document.getElementById("dbrdId").value;
                    document.forms.myForm2.submit();
                }
            }
        });
    }
}



function dispKpisGraphsm(){
    var kpis="";
    var kpisName="";
    var kpiUl=document.getElementById("sortable");
    var kpiIds=kpiUl.getElementsByTagName("li");
    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        // kpisName=kpisName+","+kpiIds[i].id;
        kpisName=kpisName+","+dkpiIds[0];
    }
    if(kpiIds.length!=0){
        kpis=kpis.substr(1);
        kpisName=kpisName.substr(1);
        document.getElementById("kpiIds").value=kpis;
        document.getElementById("kpis").value=kpisName;
        $.ajax({
            url: 'dashboardTemplateViewerAction.do?templateParam2=getKpiNeedleValue&kpiIds='+kpis+'&dashboardId='+parent.document.getElementById("dbrdId").value,
            success: function(data) {
                if(data!=""){
                    var kpiValue=data;
                    document.forms.myForm2.action="pbKpiGraphTarget.jsp?foldersIds="+parent.buildFldIds()+'&kpiIds='+kpis+'&kpiName='+kpisName+'&needleValue='+data+'&dashboardId='+parent.document.getElementById("dbrdId").value;
                    document.forms.myForm2.submit();
                }
            }
        });
    }
}




function Viewer_dispKpisGraphsmTargetType(){
    var kpis="";
    var kpisName="";
    var timeDim="";
    var kpiUl=document.getElementById("sortable");
    var kpiIds=kpiUl.getElementsByTagName("li");
    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        // kpisName=kpisName+","+kpiIds[i].id;
        kpisName=kpisName+","+dkpiIds[0];
    }
    if(kpiIds.length!=0){
        kpis=kpis.substr(1);
        kpisName=kpisName.substr(1);
        document.getElementById("kpiIds").value=kpis;
        document.getElementById("kpis").value=kpisName;
        timeDim=document.getElementById("timeDim").value;
        document.forms.myForm2.action="pbKPITargetTypeViewer.jsp?kpiIds="+kpis+'&kpiName='+kpisName+'&timeDim='+timeDim+'&dashboardId='+parent.document.getElementById("dbrdId").value;
        document.forms.myForm2.submit();
    }
    else
        {
            alert("Please select a Measure");
        }
}




function dispKpisGraphsmTargetType(){
    var kpis="";
    var kpisName="";
    var timeDim="";
    var kpiUl=document.getElementById("sortable");
    var kpiIds=kpiUl.getElementsByTagName("li");
    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        // kpisName=kpisName+","+kpiIds[i].id;
        kpisName=kpisName+","+dkpiIds[0];
    }
    if(kpiIds.length!=0){
        kpis=kpis.substr(1);
        kpisName=kpisName.substr(1);
        document.getElementById("kpiIds").value=kpis;
        document.getElementById("kpis").value=kpisName;
        timeDim=document.getElementById("timeDim").value;
        document.forms.myForm2.action="pbKPITargetType.jsp?kpiIds="+kpis+'&kpiName='+kpisName+'&timeDim='+timeDim+'&dashboardId='+parent.document.getElementById("dbrdId").value;
        document.forms.myForm2.submit();
    }
}


function Viewer_getKPIType(val,kpis,kpisName,timeDim,measType,basis){
    var targetType=val;
    var aggType = '';
$.post(
                     'dashboardTemplateViewerAction.do?templateParam2=getAggregationType&kpiIds='+kpis+'&timeDim='+timeDim+'&dashboardId='+parent.document.getElementById("dbrdId").value,
                   function(data){
                       aggType = data;
                   });

    $.ajax({
            url: 'dashboardTemplateViewerAction.do?templateParam2=getKpiNeedleValue&kpiIds='+kpis+'&timeDim='+timeDim+'&dashboardId='+parent.document.getElementById("dbrdId").value,
        success: function(data) {
            if(data!=""){
                var folderId="";
                if(folderId==null || folderId=="")
                    folderId=parent.buildFldIds();
                document.forms.kpiTargetTypeForm.action="pbKpiGraphTargetViewer.jsp?foldersIds="+folderId+'&kpiIds='+kpis+'&kpiName='+kpisName+'&targetType='+targetType+'&needleValue='+data+'&dashboardId='+parent.document.getElementById("dbrdId").value+'&measType='+measType+'&basis='+basis+'&aggType='+aggType;
                document.forms.kpiTargetTypeForm.submit();


            }
        }
    });

//                 document.forms.kpiTargetForm.action="pbDashboardKpiGraphTypeViewer.jsp?kpiIds="+kpis+"&kpis="+kpisName+"&kpiNeeedleValue="+data+"&targetType="+targetType+"&kpiTargetXml="+""+'&perday='+perday+'&timeDim='+timeDim+'&measType='+measType;
//                 document.forms.kpiTargetForm.submit();
}




//function getKPIType(val,kpis,kpisName,timeDim){
//    var targetType=val;
//    $.ajax({
//        url: 'dashboardTemplateViewerAction.do?templateParam2=getKpiNeedleValue&kpiIds='+kpis+'&timeDim='+timeDim+'&dashboardId='+parent.document.getElementById("dbrdId").value,
//        success: function(data) {
//            if(data!=""){
//                var kpiValue=data;
//                document.forms.kpiTargetTypeForm.action="pbKpiGraphTarget.jsp?foldersIds="+parent.buildFldIds()+'&kpiIds='+kpis+'&kpiName='+kpisName+'&targetType='+targetType+'&needleValue='+data+'&dashboardId='+parent.document.getElementById("dbrdId").value;
//                document.forms.kpiTargetTypeForm.submit();
//            }
//        }
//    });
//}


function dispKpiGraphTarget(){
    alert("in dispKpiGraphTarget in dashboarddesign.js")
    var timeDim=document.getElementById("timeDim").value;
    var targetType=document.getElementById("targetType").value;
    var diffDays=document.getElementById("dayDiff").value;
    if(targetType=='proRated'){
        var targetvalday=document.getElementById("tgtValDay").value;
        var targetvalweek=targetvalday*7;
        var targetvalmnth=targetvalday*diffDays;
        var targetvalqtr=targetvalday*90;
        var targetvalyr=targetvalday*365;
    }else{
        if(document.getElementById("tgtValDay")!=null){
            var targetvalday=document.getElementById("tgtValDay").value;
        }else{
            var targetvalday="0";
        }
        if(document.getElementById("tgtValWeek")!=null){
            var targetvalweek=document.getElementById("tgtValWeek").value;
        }else{
            var targetvalweek="0";
        }
        if(document.getElementById("tgtValMonth")!=null){
            var targetvalmnth=document.getElementById("tgtValMonth").value;
        }else{
            var targetvalmnth="0";
        }
        if(document.getElementById("tgtValQtr")!=null){
            var targetvalqtr=document.getElementById("tgtValQtr").value;
        }else{
            var targetvalqtr="0";
        }
        if(document.getElementById("tgtValYr")!=null){
            var targetvalyr=document.getElementById("tgtValYr").value;
        }else{
            var targetvalyr="0";
        }
    }

    var kpiId=document.getElementById("kpiId").value;
    var kpiName=document.getElementById("kpiName").value;
    var perday=document.getElementById("perDay").value;
    var timeDim=document.getElementById("timeDim").value;
    var dayDiff=document.getElementById("dayDiff").value;

    if(targetvalday!=null || targetvalweek!=null || targetvalmnth!=null || targetvalqtr!=null || targetvalyr!=null) {
        $.ajax({
            url: 'dashboardTemplateAction.do?templateParam2=getKpiGraphTargets&kpiIds='+kpiId+'&dayDiff='+dayDiff+'&targetType='+targetType+'&timeDim='+timeDim+'&day='+targetvalday+'&week='+targetvalweek+'&month='+targetvalmnth+'&qtr='+targetvalqtr+'&year='+targetvalyr+'&dashboardId='+parent.document.getElementById("dbrdId").value,
            success: function(data) {
                if(data!=""){
                    var kpiTarget=data.split("~")[0];
                    var kpiTargetXml=data.split("~")[1];
                    document.forms.kpiTargetForm.action="pbDashboardKpiGraphType.jsp?kpiIds="+kpiId+"&kpis="+kpiName+"&kpiNeeedleValue="+kpiTarget+"&targetType="+targetType+"&kpiTargetXml="+kpiTargetXml+'&perday='+perday+'&timeDim='+timeDim;
                    document.forms.kpiTargetForm.submit();
                }
            }
        });
    }
}

function Viewer_dispKpiGraphTarget(){
    var timeDim=document.getElementById("timeDim").value;
    var targetType=document.getElementById("targetType").value;
    var diffDays=document.getElementById("dayDiff").value;
    if(targetType=='proRated'){
        var targetvalday=document.getElementById("tgtValDay").value;
        var aggType =  document.getElementById("KpiAggType").value;
        if(aggType=="avg"){
                   var targetvalweek=targetvalday;
        var targetvalmnth=targetvalday;
        var targetvalqtr=targetvalday;
        var targetvalyr=targetvalday;
        }else{
        var targetvalweek=targetvalday*7;
        var targetvalmnth=targetvalday*diffDays;
        var targetvalqtr=targetvalday*90;
        var targetvalyr=targetvalday*365;
        }
    }else{
        if(document.getElementById("tgtValDay")!=null){
            var targetvalday=document.getElementById("tgtValDay").value;
        }else{
            var targetvalday="0";
        }
        if(document.getElementById("tgtValWeek")!=null){
            var targetvalweek=document.getElementById("tgtValWeek").value;
        }else{
            var targetvalweek="0";
        }
        if(document.getElementById("tgtValMonth")!=null){
            var targetvalmnth=document.getElementById("tgtValMonth").value;
        }else{
            var targetvalmnth="0";
        }
        if(document.getElementById("tgtValQtr")!=null){
            var targetvalqtr=document.getElementById("tgtValQtr").value;
        }else{
            var targetvalqtr="0";
        }
        if(document.getElementById("tgtValYr")!=null){
            var targetvalyr=document.getElementById("tgtValYr").value;
        }else{
            var targetvalyr="0";
        }
    }

    var kpiId=document.getElementById("kpiId").value;
    var kpiName=document.getElementById("kpiName").value;
    var perday=document.getElementById("perDay").value;
    var timeDim=document.getElementById("timeDim").value;
    var dayDiff=document.getElementById("dayDiff").value;
    var measType=document.getElementById("measType").value;
    var basis=document.getElementById("basis").value;
    var needleValue=document.getElementById("needleValue").value;
    if(targetvalday!=null || targetvalweek!=null || targetvalmnth!=null || targetvalqtr!=null || targetvalyr!=null) {
        $.ajax({
            url: 'dashboardTemplateViewerAction.do?templateParam2=getKpiGraphTargets&kpiIds='+kpiId+'&dayDiff='+dayDiff+'&targetType='+targetType+'&timeDim='+timeDim+'&day='+targetvalday+'&week='+targetvalweek+'&month='+targetvalmnth+'&qtr='+targetvalqtr+'&year='+targetvalyr+'&dashboardId='+parent.document.getElementById("dbrdId").value+'&measType='+measType+'&basis='+basis,
            success: function(data) {
               // alert("data********"+data);

                if(data!=""){
                    var kpiTarget=data.split("~")[0];
                    var kpiTargetXml=data.split("~")[1];
                    if(basis=='absolute')
                        kpiTarget=needleValue;
                   // alert("kpiId*****"+kpiId+"***kpiname*****"+kpiName+"****kpiTarget*****"+kpiTarget+"*****targetType******"+targetType+"*****kpiTargetXml****"+kpiTargetXml+"*****perday****"+perday+"****timeDim***"+timeDim);
                    document.forms.kpiTargetForm.action="pbDashboardKpiGraphTypeViewer.jsp?kpiIds="+kpiId+"&kpis="+kpiName+"&kpiNeeedleValue="+kpiTarget+"&targetType="+targetType+"&kpiTargetXml="+kpiTargetXml+'&perday='+perday+'&timeDim='+timeDim+'&measType='+measType+'&basis='+basis;
                    document.forms.kpiTargetForm.submit();
                }
            }
        });
    }
}

function editKPITarget(dashletId,kpiId,kpiName,timeDim,targetType,divId,folderId,dayTargetVal,aggType){
    var dashboardId = parent.document.getElementById("dbrdId").value;
    parent.document.getElementById("divId").value = divId
    $.ajax({
            url: 'dashboardTemplateViewerAction.do?templateParam2=getKpiNeedleValue&kpiIds='+kpiId.replace("A_","")+'&dashboardId='+dashboardId,
        success: function(data) {
//            var details = eval('('+data+')');
//            var operators = details.operators;
//            alert("operators"+operators)
//            var startVals = details.startValue;
//            alert("startVals"+startVals)
//            var endVals = details.endValues;
//            alert("endVals"+endVals)
//            var kpiTarget = details.needleValue;
//            alert("kpiTarget"+kpiTarget)
            $('#kpisGraphsDialog').dialog('open');
            var frameObj=document.getElementById("kpigraphdataDispmem");
            frameObj.src = "pbKpiGraphTargetViewer.jsp?foldersIds="+folderId+'&kpiIds='+kpiId.replace("A_","")+'&kpiName='+kpiName+'&targetType='+targetType+'&needleValue='+data+'&dashboardId='+dashboardId+'&measType=Standard'+'&basis=deviation'+'&dayTargetVal='+dayTargetVal+'&aggType='+aggType;
            //    document.forms.kpiTargetTypeForm.action="pbKpiGraphTargetViewer.jsp?foldersIds="+folderId+'&kpiIds='+kpiId.replace("A_","")+'&kpiName='+kpiName+'&targetType='+targetType+'&needleValue='+data+'&dashboardId='+dashboardId+'&measType=Standard'+'&basis=deviation';

    }
});
}
function editKPIName(dashletId,dashboardId,folderId,graphId,kpiName,reportid,kpiMasterId,dispSequence,dispType,dashletName,forDesigner,editDbrd){
dashletid =   dashletId;
dashboardid = dashboardId;
folderidGlobal = folderId;
graphid = graphId;
kpiGrname = kpiName;
refreportId=reportid;
kpimasterid =kpiMasterId;
dispseq=dispSequence;
disptype=dispType;
newDashletName=kpiName;
fromdesigner=forDesigner;
editdbrd=editDbrd;
var td="<input type='text' value='"+kpiGrname+"' >";
$("#oldKpiGrname").html(td);
$("#newKpigrName1").val('');
$("#editKpiGrDialog").dialog('open');
}
function updateKpiGrName(){
   var newKpigr =  $("#newKpigrName1").val();
    $("#editKpiGrDialog").dialog('close');
   $.ajax({
            url: 'dashboardTemplateViewerAction.do?templateParam2=editKPIName&dashletid='+dashletid+'&dashboardid='+dashboardid+'&graphId='+graphid+'&kpiGrname='+newKpigr,
        success: function(data) {
         displayKPIDashGraph(dashletid,dashboardid,refreportId,graphid,kpimasterid,dispseq,disptype,newKpigr,fromdesigner,editdbrd)
}
});
}
/*
function Viewer_previewKpis(timeDimid,dayDiff,kpiType,timeParams1,params1,more)
{

    var kpiIds = document.getElementById("kpiIds").value;
    parent.document.getElementById("diffinDays").value=dayDiff;
    var params=params1;//document.getElementById("BuildedParamsStr").value; //getBuildedParams();
    var hideDiv;
    var  divId=parent.document.getElementById("divId").value;
      if(more!="more")
    {hideDiv=parent.document.getElementById("hideDiv").value;}
    var timeparams=timeParams1;//document.getElementById("BuildedParamsWithTimeStr").value;//getBuildedTimeParams();

 var reprtId=parent.document.getElementById("reportId").value;
 var folderId=parent.document.getElementById("folderId").value;

 parent.document.getElementById(divId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
    $.ajax({
         url: 'dashboardTemplateViewerAction.do?templateParam2=previewKpis&kpiIds='+kpiIds+'&parameters='+params+'&timeparams='+timeparams+'&timeDimid='+timeDimid+'&dayDiff='+dayDiff+'&kpiType='+kpiType+'&foldersIds='+folderId+'&dashboardId='+reprtId,
        success: function(data) {
            if(data!=""){
                if(more!="more"){
                parent.document.getElementById(hideDiv).style.display='block'}
                parent.document.getElementById(divId).innerHTML ="";
                parent.document.getElementById(divId).innerHTML = data;
            }
        }
    });
}

function previewKpis(timeDimid,dayDiff,kpiType)
{
    var kpiIds = document.getElementById("kpiIds").value;
    parent.document.getElementById("diffinDays").value=dayDiff;
    var params=getBuildedParams();
    var  divId=parent.document.getElementById("divId").value;
    var timeparams=getBuildedTimeParams();
    parent.document.getElementById(divId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=previewKpis&kpiIds='+kpiIds+'&parameters='+params+'&timeparams='+timeparams+'&timeDimid='+timeDimid+'&dayDiff='+dayDiff+'&kpiType='+kpiType+'&foldersIds='+parent.buildFldIds()+'&dashboardId='+parent.document.getElementById("dbrdId").value,
        success: function(data) {
            if(data!=""){
                parent.document.getElementById(divId).innerHTML ="";
                parent.document.getElementById(divId).innerHTML = data;
            }
        }
    });
}*/

function editKpis()
{
    document.getElementById("previewKpiTable").style.display='none';
    document.getElementById("editKpi").style.display='block';
    document.getElementById("editKpi").style.width='100%';
}

function createDbrdgraph(divId)
{
      $("#NewDbrdGraph").dialog('close');
      UpdateParams()//added by k
    var grpIdsStrArray=graphIds.split(",");
    if(grpIdsStrArray.length<=2){
        $("#graphListDialog").dialog('open');
    }
    else{
        ////alert("You can select maximum of  two Graphs")
    }
    document.getElementById('divId').value=divId;

    var id=divId.replace("dispGrp","");
    document.getElementById("HiddenNewDbrdGraphName").value= document.getElementById('NewDbrdGraphName').value;
    document.getElementById("innertd"+id).innerHTML= document.getElementById('NewDbrdGraphName').value;
}

function getDbrdTableColumns(divId,typeValue){

    document.getElementById('divId').value=divId;
//    var divId=document.getElementById('divId').value;
//    alert("divId"+divId)
    graphCount++;
    var dashboardId=document.getElementById('dbrdId').value;
    $("#graphListDialog").dialog('close');
    $("#tableColsDialog").dialog('open');
    var frameObj=document.getElementById("tablecols");
    var source = "pbDbrdGraphCols.jsp?folderIds="+buildFldIds()+"&grpType="+typeValue+"&gid="+graphCount+"&divId="+divId+"&dashboardId="+dashboardId;
    frameObj.src=source;
}


function getDbrdGraphColumns(divId,typeValue){

    var divId=document.getElementById('divId').value;
    graphCount++;
    var dashboardId=document.getElementById('dbrdId').value;
    $("#graphListDialog").dialog('close');
    $("#graphColsDialog").dialog('open');
    var frameObj=document.getElementById("graphCols");
    var source = "pbDbrdGraphCols.jsp?folderIds="+buildFldIds()+"&grpType="+typeValue+"&gid="+graphCount+"&divId="+divId+"&dashboardId="+dashboardId;
    frameObj.src=source;
}
function getDbrdGraphName(name,typeValue){
    var divId=document.getElementById("divId").value;
    graphCount++;

    graphIds = graphIds+","+graphCount;
    if(graphIds!=""){
        document.getElementById("allGraphIds").value=graphIds.substring(1);
    }
    else{
        document.getElementById("allGraphIds").value=graphIds;
    }

    parentDiv = document.getElementById(divId);
    parentDiv.innerHTML="";
    parentDiv.innerHTML=data;
    $("#graphListDialog").dialog('close');
    document.getElementById("allGraphIds").value=graphIds;
}
function canceldbrdGraph(){
    $("#graphColsDialog").dialog('close');
}
function canceldbrdTable(){
    $("#tableColsDialog").dialog('close');
}
function cancelScardDialog(){
    $("#scorecardDialog").dialog('close');
}

function graphTypesDisp(dispgrptypObj){


    if(radioValue == 'JQPlot'){
        var divObj=document.getElementById("dispgrptypes" + grpId + "-jqplot")
        if(divObj.style.display=='none'){
            divObj.style.display='';
        }
        else{
            divObj.style.display='none';
        }

    }
    else{
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }

}
}

function graphTypesDisp(dispgrptypObj){

    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
}

function graphTypesDisp(dispgrptypObj){

    if(radioValue == 'JQPlot'){
        var divObj=document.getElementById("dispgrptypes" + grpId + "-jqplot")
        if(divObj.style.display=='none'){
            divObj.style.display='';
        }
        else{
            divObj.style.display='none';
        }

    }
    else{
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }

}
}

function changeGrpType(cols,grpColNames,grpType,grpId,divId){
    var grpSize="Medium";
    parent.document.getElementById(divId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=buildDbrdGraphs&grpColumns='+cols+'&grpColumnsNames='+grpColNames+'&grpType='+grpType+'&grpSize='+grpSize+'&grpId='+grpId+'&divId='+divId,
        success: function(data){
            parent.document.getElementById(divId).innerHTML ="";
            parent.document.getElementById(divId).innerHTML = data;
            parent.canceldbrdGraph();
        }
    });
}

function changeDashboardGraphType(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,graphType,fromDesigner){
    var divIdObj=document.getElementById("Dashlets-"+dashletId);
    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
       url: 'dashboardViewer.do?reportBy=displayGraph&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&graphType='+graphType+'&fromDesigner='+fromDesigner,
      success: function(data){
            divIdObj.innerHTML =data;
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

function changeGrpSize(cols,grpColNames,grpType,grpSize,grpId,divId){
    parent.document.getElementById(divId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=buildDbrdGraphs&grpColumns='+cols+'&grpColumnsNames='+grpColNames+'&grpType='+grpType+'&grpSize='+grpSize+'&grpId='+grpId+'&divId='+divId,
        success: function(data){
            parent.document.getElementById(divId).innerHTML ="";
            parent.document.getElementById(divId).innerHTML = data;
            parent.canceldbrdGraph();
        }
    });
}

function graphColumnsDisp(grpId,grpType,divId){
    getnewGrpColumns(grpId,grpType,divId);
}
function getnewGrpColumns(grpId,grpType,divId){
    $("#graphListDialog").dialog('close');
    var frameObj=document.getElementById("graphCols");
    var source = "pbDbrdGraphCols.jsp?folderIds="+parent.buildFldIds()+"&grpType="+grpType+"&gid="+grpId+"&divId="+divId;
    frameObj.src=source;
    $("#graphColsDialog").dialog('open');
}

function deleteGraph(graphId,divId){
    document.getElementById(divId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=buildDbrdGraphs&gid='+graphId+'&grpChange=DeleteGraph',
        success: function(data){
            document.getElementById(divId).innerHTML =data;
        }
    });
}

function dispGraphDetails(graphId,divId){
    getGraphDetails(graphId,divId);
}

function getGraphDetails(graphId,divId){
    var frameObj=document.getElementById("graphDtls");
    var source = "PbGraphDetails.jsp?graphId="+graphId+"&divId="+divId;
    frameObj.src=source;
    frameObj.style.display='block';
    document.getElementById('fade').style.display='block';
}

function KpiDrilldown(elmntId,folderIds,reportId, dashletId, kpiMasterId,fromDesigner,grpStatus,kpiNames){
    var frameObj=document.getElementById("kpiDrillDispmem");
    if (folderIds == null || folderIds == "null" || folderIds == ""){
        folderIds = buildFldIds();
        fromDesigner=true;
    }
    frameObj.src="pbKPIDrillDown.jsp?kpiId="+encodeURIComponent(elmntId)+"&folderIds="+folderIds+"&reportId="+reportId+"&dashletId="+dashletId+"&kpiMasterId="+kpiMasterId+"&fromDesigner="+fromDesigner+"&grpStatus="+grpStatus+"&kpiNames="+encodeURIComponent(kpiNames);
    $("#kpiDrillDialog").dialog('open');
}

function logout(){
    document.forms.frmParameter.action="baseAction.do?param=logoutApplication";
    document.forms.frmParameter.submit();
}

function gohome(){
    document.forms.frmParameter.action="baseAction.do?param=goHome";
    document.forms.frmParameter.submit();
}

function reportParamsDrill(repId,userId){
    var path='&'+document.getElementById('reppath').value;
    path=path.replace('&',';');
    window.open('pbParameterDrill.jsp?userId='+userId+'&reportId='+repId+'&path='+path,"Parameter Drill", "scrollbars=1,width=550,height=350,address=no");
}

function submiturls1($ch)
{
    document.frmParameter.action = $ch;
    document.frmParameter.submit();
}

function modifyParams(columnId,checkBoxObj,tableObj){
    var trObj=tableObj.getElementsByTagName('tr');
    for(i=0;i<trObj.length;i++){
        var tdObj=trObj[i].getElementsByTagName('td');
        for(j=0;j<tdObj.length;j++){
            if(tdObj[j].id==columnId){
                if(checkBoxObj.checked)
                    tdObj[j].style.display='';
                else
                    tdObj[j].style.display='none';
            }
        }
    }
}

function paramDisp(modifyColumnsObj){
    if(modifyColumnsObj.style.display=='none'){
        modifyColumnsObj.style.display='';
    }
    else{
        modifyColumnsObj.style.display='none';
    }
}

function addParamList(addParamsListObj,addParamsObj,reportId,path,tableId){
    if(addParamsListObj.style.display=='none'){
        addParamsListObj.style.display='';
    }
    else{
        addParamsListObj.style.display='none';
        addParams(addParamsObj,reportId,path,tableId);
    }
}

function addParams(addParamsObj,reportId,path){
    var paramIds='';
    for(var i=0;i<addParamsObj.length;i++){
        if( addParamsObj[i].checked){
            paramIds=paramIds+','+addParamsObj[i].value;
        }
    }
    if(paramIds!=''){
        document.frmParameter.action='pbAddParams.jsp?reportId='+reportId+'&paramIds='+paramIds+'&path='+path;
        document.frmParameter.submit();
    }
}
function customizeReport(reportId)
{
    document.frmParameter.action="reportViewer.do?reportBy=editReport";
    document.frmParameter.target="_blank";
    document.frmParameter.submit();
}
function closeComposeDiv(){
    $('#msgframe').dialog('close');
}
function closesnapshot(){
    $('#snapshot').dialog('close');
}
function closeTracker(){
    $('#frametrack').dialog('close');
}
function closeScheduler(){
    $('#scheduler').dialog('close');
}

function displayWidgets(){
    $("#Widgets").toggle(500);
}
function cancelFavLinks(){
    $('#favLinksDialog').dialog('close');
    document.getElementById('favFrame').src = document.getElementById('favFrame').src;
}
function cancelPerLinks(){
    $('#snapShotDialog').dialog('close');

    window.location.reload();
}
function viewDashboardG(path){
    document.forms.frmParameter.action=path;
    document.forms.frmParameter.submit();
}
function viewReportG(path){
    document.forms.frmParameter.action=path;
    document.forms.frmParameter.submit();
}
function goGlobe(){
    $(".navigateDialog").dialog('open');
}
function closeStart(){
    $(".navigateDialog").dialog('close');
}
function goPaths(path){
    parent.closeStart();
    document.forms.frmParameter.action=path;
    document.forms.frmParameter.submit();
}
var rlsdiv;
function roleCancel(){

    document.getElementById('busRoleDiv').style.display='none';
    document.getElementById('fadeBusRole').style.display='none';
    document.getElementById('rep-'+rlsdiv).innerHTML= "";
    document.getElementById('mainBody').style.overflow='auto';
    rlsdiv.shift();
}
function displayBusRoles(){
    $('#BusRolesTab').toggle(500);
}
function rolesajax(path,roleid)
{
    if(rlsdiv!=null){
        document.getElementById('rep-'+rlsdiv).innerHTML = "";
        rlsdiv = roleid;
    }
    if(rlsdiv==null){
        rlsdiv = roleid;
    }

    $.ajax({
        url: path,
        success: function(path){
            if(path!=""){
                document.getElementById('rep-'+roleid).innerHTML = path;
            }
            else
            {
                document.getElementById('rep-'+roleid).innerHTML = "none";
            }
        }
    });
}

function cancelMessage1(){
    $('#composeMessageDialog').dialog('close');
}

function cancelSheduler()
{
    $('#reportSchedulerDialog').dialog('close');
    document.getElementById("Scheduler").style.display='none';
    document.getElementById('fade').style.display='none';
    document.getElementById('mainBody').style.overflow='auto';
}

function addMapMeasures(ctxPath, folderIds, reportId,divId){
     var sortType = "";//$("#sortValuesForMap").val();
     var mapView = "";//$("#ViewSelect").val();
     var geoView = "";//$("#GeoViewForMap").val();
    var frameObj=document.getElementById("mapMeasureFrame");
    var source=ctxPath+"/TableDisplay/PbChangeMapColumnsRT.jsp?folderIds="+folderIds+'&divId='+divId+'&dashboardId='+reportId+'&editMap=true&sortType='+sortType+'&mapView='+mapView+'&geoView='+geoView;
    frameObj.src=source;
    $("#MapMeasures").dialog('open');
}

function displayGraph(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,graphOrTable,editDbrd,flag,fromDesigner){
//    alert("editDbrd in displayGraph in dashboardDesign.js"+editDbrd)
    if(dispType=='KpiWithGraph')
        {
            var divIdObj=document.getElementById("Dashlets-"+dashletId+"-graph");
        }
        else{
    var divIdObj=document.getElementById("Dashlets-"+dashletId);
        }
        if(dispType=='jq'){
       divIdObj.innerHTML = '<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>'
       divIdObj.innerHTML ='<iframe name=\"graphdisplay-'+dashletId+'\" id=\"graphdisplay-'+dashletId+'\" width=\"100%\" height=\"350px\" frameborder=\"0\" style=\"overflow-x: hidden;overflow-y: hidden;\" src=\"PbDbrdJQplot.jsp?graphId='+graphId+'&dashboardId='+dashBoardId+'&dashletId='+dashletId+'&refReportId='+refReportId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&editDbrd='+editDbrd+'&fromDesigner='+fromDesigner+'&flag='+flag+'&Type=jqGraphProperty\"></iframe>';
        }else{
    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    var dispUrl = 'dashboardViewer.do?reportBy=displayGraph&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&editDbrd='+editDbrd+'&fromDesigner='+fromDesigner+'&flag='+flag;

    if (graphOrTable){
        if (graphOrTable != "" && graphOrTable != "undefined")
            dispUrl = dispUrl + '&graphOrTable=' + graphOrTable;
    }
    $.ajax({
        url: dispUrl,
        success: function(data){
            divIdObj.innerHTML = data;
        }
    });
}
}
function displayKPI(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,fromDesigner,editDbrd){
    var divIdObj=document.getElementById("Dashlets-"+dashletId);
    if (divIdObj == null){
        divIdObj = document.getElementById(dashletId);
        if (parent.document.getElementById("hideDiv") != null){
            var  hideDiv=parent.document.getElementById("hideDiv").value;
            parent.document.getElementById(hideDiv).style.display='block';
        }
    }
    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardViewer.do?reportBy=displayKPI&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&kpiMasterId='+kpiMasterId+'&kpiDrill=Y&fromDesigner='+fromDesigner+'&editDbrd='+editDbrd,
        success: function(data){
            divIdObj.innerHTML =data;
        }
    });
}
function displayScoreCard(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName){
    var divIdObj=document.getElementById("Dashlets-"+dashletId);
    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardViewer.do?reportBy=displayScoreCard&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&kpiDrill=Y',
        success: function(data){
            divIdObj.innerHTML =data;
        }
    });
}
function displayMap(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName){
 var sortType = "";//sortValue;
                     var mapView = "";//viewSelectValue;
                     var geoView = "";//dimensionValue;
                        if(sortType!=''){
                  $('#sortValuesForMap').val(sortType);
                $('#ViewSelect').val(mapView);
                $('#GeoViewForMap').val(geoView);
                        }
   var ctxPath=$("#h").val();
    var divId = "Dashlets-"+dashletId;
    var divIdObj=document.getElementById(divId);
    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="50px"  style="position:absolute" ></center>';
    $.ajax({
        url:'mapAction.do?reportBy=isMapEnabled&REPORTID='+dashBoardId+'&reportType=D&sortType='+sortType+'&mapView='+mapView+'&geoView='+geoView,
        success : function(data){
            if (data != null && data != ""){
                if(data=="dimensionerror"){
                    document.getElementById("mapMenu").style.display = "none";
                    document.getElementById("DashletsColumn1_1").style.display = "none";
                    $("#Dashlets-newMap").html("");
                    alert("Map is not applicable for this view");
                }
                else if(data=='error'){
                    alert("You have not configured Geography Dimension in Setup. Please configure the same for viewing Maps");
                }
                else{
                    var mapHtml="<div id=\"divHead\" class=\"portlet-header1 navtitle portletHeader ui-corner-all\""
                    + "style=\"width:100%;\">";
                    mapHtml+="<table width=\"100%\"><tr>";
                    mapHtml+="<td width=\"74%\"><strong>Map Region</strong></td></tr></table></div>";
                    mapHtml+="<table width=\"100%\"><tr>";
                    mapHtml+="<td align=\"left\" width=\"10%\">";
                    mapHtml+="<a href=\"javascript:void(0);\" style=\"text-decoration: none;\" onclick=\"addMapMeasures('"+ctxPath+"','','"+dashBoardId+"','"+divId+"')\">Add Measures</a>";
                    mapHtml+="</td></tr></table>";
                    mapHtml+="<div id=\"map_"+divId+"\" style=\"height:90%; width:100%\">";
                    $("#"+divId).html(mapHtml);
                    divId = "map_"+divId;
                    dispMap(data,divId,"D");
                }
            }
        }
    });
    parent.setMapDiv(divId);
}

function displayDashboardGraph(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,editDbrd){
//    alert("editDbrd in displayDashboardGraph in dashboardDesign.js "+editDbrd)
    var divIdObj=document.getElementById("Dashlets-"+dashletId);
    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    if(dispType=="groupMeassure")
        {
//            alert("groupMeassure");
            divIdObj.innerHTML='<iframe name="groupMeasure" id="groupMeasure-'+dashletId+'" src="" width="100%" height="100%" scrolling="auto" frameborder="0"></iframe>';
            var frameObj = document.getElementById("groupMeasure-"+dashletId);
            var source = 'pbNewGroupMeasureDashlet.jsp?dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&editDbrd='+editDbrd;
            frameObj.src=source;
        }
    else{
//        alert(" Else groupMeassure");
    $.ajax({
        url: 'dashboardViewer.do?reportBy=displayGraph&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&editDbrd='+editDbrd,
        success: function(data){
            divIdObj.innerHTML =data;
        }
    });
}
}
function displayKPIDashGraph(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,forDesigner,editDbrd){
//    alert("editDbrd in displayKPIDashGraph in dashboardDesign"+editDbrd)
    var divIdObj=document.getElementById("Dashlets-"+dashletId);

    if (divIdObj == null){
        divIdObj = document.getElementById("Dashlets-"+dashletId);
        if (parent.document.getElementById("hideDiv") != null){
            var  hideDiv=parent.document.getElementById("hideDiv").value;
            parent.document.getElementById(hideDiv).style.display='block';
        }
    }

    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardViewer.do?reportBy=displayKPIDashGraph&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&forDesigner='+forDesigner+'&editDbrd='+editDbrd,
        success: function(data){
            divIdObj.innerHTML =data;
        }
    });
}


function saveFavouriteParams(){
    $("#favouriteParamsDialog").dialog('open');
}

function getBuildedParamswithTime()
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
        }else if(paramIds[i].id=='AS_OF_DMONTH1' || paramIds[i].id=='AS_OF_DMONTH2' || paramIds[i].id=='CMP_AS_OF_DMONTH1' || paramIds[i].id=='CMP_AS_OF_DMONTH2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DQUARTER1' || paramIds[i].id=='AS_OF_DQUARTER2' || paramIds[i].id=='CMP_AS_OF_DQUARTER1' || paramIds[i].id=='CMP_AS_OF_DQUARTER2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DYEAR1' || paramIds[i].id=='AS_OF_DYEAR2' || paramIds[i].id=='CMP_AS_OF_DYEAR1' || paramIds[i].id=='CMP_AS_OF_DYEAR2'){
            paramtimeIds=(paramIds[i].id);
            paramswithtime=paramswithtime+","+paramIds[i].id;
        }else if(paramIds[i].id=='AS_OF_DATE' || paramIds[i].id=='PRG_DAY_ROLLING'){
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
}

function getFavParams(){
    var foldersIds=buildFldIds();
    var branches1="";
    var str;

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=getFavParams&foldersIds='+foldersIds+'&REPORTID='+document.getElementById("dbrdId").value,
        success: function(data) {

            $("#favourParams").html("");
            str=data;
            branches1 = $(str).appendTo("#favourParams");
            $("#favourParams").treeview({
                add: branches1
            });

            $('#favourParams > li >span').draggable({
                helper:"clone",
                effect:["", "fade"]
            });
        }
    });
}

function showDesignCommentDialog(elementId,masterId,kpiComment){
   var commentid=this.document.getElementById("divId").value;

    $("#kpiComment").dialog({
        autoOpen: false,
        height: 350,
        width: 400,
        position: 'justify',
        modal: true
    });
    $("#kpiComment").dialog('open');
    var frameObj = document.getElementById("kpiCommentFrame");
    frameObj.src = "kpiTableComments.jsp?elementId="+elementId+'&masterId='+masterId+'&kpiComment='+kpiComment+'&commentFlag=Y'+'&commentid='+commentid  ;
}

/*function saveDbrdDesignComment(elementId,masterId,kpiComment,commentid){
    var comntText=document.getElementById("commentArea").value;
    parent.document.getElementById("kpiCommenttext").value=comntText;
    parent.document.getElementById("kpiCommentelmntid").value=elementId;
    parent.document.getElementById("kpiCommentmasterid").value=masterId;
    var kpiIds=parent.document.getElementById("kpiIds").value;
    var kpiNames=parent.document.getElementById("kpis").value;

    kpiNames=kpiNames.replace("%","%".charCodeAt(0),"gi");

    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=saveDbrdDesignComments&elementId='+elementId+'&masterId='+masterId+'&commentText='+comntText+'&kpiComment='+kpiComment+'&REPORTID='+parent.document.getElementById("dbrdId").value+'&commentid='+commentid ,
        success: function(data){
            refreshComment(divId,kpiIds,kpiNames);
        }
    });
    parent.$("#kpiComment").dialog('close');
}
function refreshComment(divId,kpiIds,kpiNames){

    var diffinDays=parent.document.getElementById("diffinDays").value;
    var timeDimension=parent.document.getElementById("timeDimension").value;
    var kpiType=parent.document.getElementById("kpiType").value;

    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=buildKpis&Kpis='+kpiIds+'&KpiNames='+kpiNames+'&divId='+divId+'&kpiType='+kpiType+'&dashboardId='+parent.document.getElementById("dbrdId").value,
        success: function(data) {
        }
    });
    parent.previewKpis(timeDimension,diffinDays,kpiType);
}*/

function showCommentDialog(elementId,masterId,dashletId){
    var dashboardId = parent.document.getElementById("dbrdId").value;
    $("#kpiComment").dialog('open');
    var frameObj = document.getElementById("kpiCommentFrame");

    frameObj.src = "kpiTableComments.jsp?elementId="+elementId+'&masterId='+masterId+'&dashboardId='+dashboardId+'&commentFlag=N&dashletId='+dashletId;
}

function ZoomTargetGraph(viewbycolumnsstr,barchartcolumntitlesstr,keyvaluesstr,datavaluesstr)
{
  parent.$("#kpizoom").dialog('open');
  var dashBoardID=$("#dbrdId").val()
//  alert("dashBoardID\t"+dashBoardID)
    $.ajax({

        url: 'dashboardTemplateViewerAction.do?templateParam2=ZoomTargetGraph&viewbycolumnsstr='+viewbycolumnsstr+'&barchartcolumntitlesstr='+barchartcolumntitlesstr+'&keyvaluesstr='+keyvaluesstr+'&datavaluesstr='+datavaluesstr+'&dashBoardID='+dashBoardID,
      success: function(data){
          parent.$("#kpizoom").html(data)
//          document.getElementById("kpizoom").innerHTML=data;
        }
    });

//     parent.$("#kpizoom").dialog('close');
}


function showDesignCommentfordesigner()
{
 alert("Comments can be entered from Dashboard Viewer");
}

function saveDbrdComment(elementId,masterId,dashletId){
//      alert('in dashboarddesign.js in saveDbrdComment')
    var comntText=document.getElementById("commentArea").value;
    var reportId = parent.document.getElementById("REPORTID").value;
    $.ajax({
        url: 'dashboardTemplateViewerAction.do?templateParam2=saveDbrdComments&elementId='+elementId+'&masterId='+masterId+'&commentText='+comntText+'&REPORTID='+reportId+'&dashletId='+dashletId,
        success: function(data){
            parent.displayKPI(dashletId, reportId, "","",masterId, "","","");
        }
    });
    parent.$("#kpiComment").dialog('close');
}
function popDiv(){
    $("#busRoleDiv").dialog('open');
}
$(document).ready(function(){
    if ($.browser.msie == true){
        $("#favLinksDialog").dialog({

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
        $("#reportSchedulerDialog").dialog({

            autoOpen: false,
            height: 380,
            width: 580,
            position: 'justify',
            modal: true
        });
        $("#busRoleDiv").dialog({
            autoOpen: false,
            height: 600,
            width: 350,
            position: 'justify',
            modal: true
        });
 $("#kpiTarget").dialog({
        autoOpen: false,
        height: 250,
        width: 360,
        position: 'justify',
        modal: true
    });

    $("#editKpiCustomColor").dialog({
        autoOpen: false,
        height: 350,
        width: 390,
        position: 'justify',
        modal: true
    });

    }else{
        $("#busRoleDiv").dialog({

            autoOpen: false,
            height: 340,
            width: 340,
            position: 'justify',
            modal: true
        });
        $("#favLinksDialog").dialog({

            autoOpen: false,
            height: 520,
            width: 660,
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
        $("#reportSchedulerDialog").dialog({

            autoOpen: false,
            height: 280,
            width: 580,
            position: 'justify',
            modal: true
        });
        $("#kpiTarget").dialog({
        autoOpen: false,
        height: 250,
        width: 360,
        position: 'justify',
        modal: true
    });

    $("#editKpiCustomColor").dialog({
        autoOpen: false,
        height: 350,
        width: 390,
        position: 'justify',
        modal: true
    });
  

    }

    $('#composeMessageDiv').click(function() {
        var frameObj = document.getElementById("composeMessageFrame");
        frameObj.src = "pbTakeMailAddress1.jsp";
        $('#composeMessageDialog').dialog('open');
    });

    $('#CustomizeFav').click(function() {
        $('#favLinksDialog').dialog('open');
    });
    $('#defineSchedulerDiv').click(function() {
        $('#reportSchedulerDialog').dialog('open');
    });
});

function savedelDbrdComment(elementId,masterId){
    var comntText="";
    var kpiComment="";
    $.ajax({
        url: 'dashboardTemplateAction.do?templateParam2=saveDbrdComments&elementId='+elementId+'&masterId='+masterId+'&commentText='+comntText+'&kpiComment='+kpiComment+'&REPORTID='+parent.document.getElementById("REPORTID").value,
        success: function(data){
            var kpiData=data.substring(1).split("|");
            for(var i=0;i<kpiData.length;i++){
                var kpiDisp=kpiData[i].split(",");
                parent.displayKPI(kpiDisp[0],kpiDisp[1],kpiDisp[2],kpiDisp[3],kpiDisp[4],kpiDisp[5],kpiDisp[6],kpiDisp[7]);
            }
        }
    });
    parent.$("#kpiComment").dialog('close');
}

function clearComments(elmntId,masterId,userId, dashletId){
    $.ajax({
            url: 'dashboardTemplateViewerAction.do?templateParam2=isAuthorizedUser&elementId='+elmntId+'&masterId='+masterId+'&userId='+userId+'&dashletId='+dashletId,
            success: function(data){
                if(data){
                      var delConfirm=confirm('All the Comments will be deleted,want to continue');
    var reportId = parent.document.getElementById("REPORTID").value;
    if(delConfirm==true){
        $.ajax({
            url: 'dashboardTemplateViewerAction.do?templateParam2=delDbrdComments&elementId='+elmntId+'&masterId='+masterId+'&userId='+userId+'&REPORTID='+reportId,
            success: function(data){
                parent.$("#kpiComment").dialog('close');
                parent.displayKPI(dashletId, reportId, "","",masterId, "","","");
            }
        });
    }
                }
                else{
                    alert("You Are Not allowed to Delete the Comments")
                }
            }
        });
}

function getTargetType(val,elmntId,masterkpiId,timeLevel,targetVal,reportId,timeDimVal,currVal,kpiName){
    var targetType=val;
    document.forms.TargetTypeForm.action="pbChangeTarget.jsp?elementId="+elmntId+'&masterKpiId='+masterkpiId+'&timeLevel='+timeLevel+'&targetValue='+targetVal+'&reportId='+reportId+'&timeDimVal='+timeDimVal+'&targetType='+targetType+'&currVal='+currVal+'&kpiName='+kpiName;
    document.forms.TargetTypeForm.submit();
}

function dbrdTable(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,graphOrTable,editDbrd,flag,jqgraphtypename,jqgraphtypeid,jqtype,height){

    /*var dashletHeader= parent.document.getElementById("DashletHeader-"+dashletId);
    var dashletHeaderTd= parent.document.getElementById("DbrdTable-"+dashletId);
    var HeaderatagContent=dashletHeaderTd.getElementsByTagName("a");
    parent.document.getElementById("zoom"+graphId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"   ></center>';

    if(HeaderatagContent[0].innerHTML=="Switch to Table"){
        HeaderatagContent[0].innerHTML="Switch to Graph";
        HeaderatagContent[0].className="ui-icon ui-icon-image";
        HeaderatagContent[0].title="Switch to Graph";
        $.ajax({
            url: 'dashboardViewer.do?reportBy=dispDbrdTable'+'&REPORTID='+dashBoardId+'&graphId='+graphId+'&dashBoardId='+dashBoardId+'&dispSequence='+dispSequence+'&dashletId='+dashletId,
            success: function(data){
                if(data!=""){
                    parent.document.getElementById("zoom"+graphId).innerHTML ="";
                    parent.document.getElementById("zoom"+graphId).innerHTML = data;
                }
            }
        });
    }
    else{
        HeaderatagContent[0].innerHTML="Switch to Table";
        HeaderatagContent[0].className="ui-icon ui-icon-calculator";
        HeaderatagContent[0].title="Switch to Table";*/
//    alert("jqtype"+jqtype)
    if(jqtype == true){
        buildDBJqplot(jqgraphtypename, jqgraphtypeid, dashBoardId, dashletId, height,dispType);
    }
    else{
        displayGraph(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,graphOrTable,editDbrd,flag);
}
    //}
}

function dbrdViewerTable(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName){
    var divIdObj=document.getElementById("zoom"+graphId);
    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

        $.ajax({
            url: 'dashboardViewer.do?reportBy=dispDbrdTable'+'&REPORTID='+dashBoardId+'&graphId='+graphId+'&dashBoardId='+dashBoardId+'&dispSequence='+dispSequence+'&dashletId='+dashletId,
            success: function(data){
                if(data!=""){
                    divIdObj.innerHTML =data;
                }
            }
        });
}

function displayDbrdTable(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName){
    var divIdObj=document.getElementById("Dashlets-"+dashletId);
   divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardViewer.do?reportBy=displayTable&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName,
        success: function(data){
            divIdObj.innerHTML =data;
        }
    });
}


function editTarget(elementId,kpiMasterid,kpiName,userId,changePercentVal,reportId, dashletId,fromDesigner){
    $("#editKpiCustomColor").dialog('open');
    var frameObj = document.getElementById("editKpiCustomColorFrame");
    frameObj.src = "pbEditKPIColorValues.jsp?elementId="+elementId+'&kpiMasterid='+kpiMasterid+'&kpiName='+kpiName+'&userId='+userId+'&changePercentVal='+changePercentVal+'&reportId='+reportId+'&dashletId='+dashletId+'&fromDesigner='+fromDesigner;
}
function closeKPICustColor(){
    parent.$("#editKpiCustomColor").dialog('close');
}
function sortByNumber(dashletId){

    if(document.getElementById("sortId"+dashletId).style.display=='none')
        {
            $("#sortId"+dashletId).show();
        }
        else
            {
                $("#sortId"+dashletId).hide();
            }
}
function goButtonForSort(dashletId,flag){

    var SortMeasure=$("#SortMeasure"+dashletId).val();
    var SortOrder=$("#SortingOrder"+dashletId).val();

     $.ajax({
               url: 'dashboardTemplateViewerAction.do?templateParam2=goButtonForSort&dashletId='+dashletId+'&flag='+flag+'&SortMeasure='+SortMeasure+'&SortOrder='+SortOrder+'&PbReportId='+PbReportId,
               beforeSend:function(){
                document.getElementById("Dashlets-"+dashletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                                },
               success: function(data){

                document.getElementById("Dashlets-"+dashletId).innerHTML=data
                        }
                    });

}

function showComplexKPIs(divId,dashboardId)
{
     $("#createKPIDiv").dialog('open');
     $("#createKPIDiv").html('')
     $("#createKPIDiv").html("<iframe id=\"createKPIDivFrame\" frameborder=\"0\" width=\"100%\" height=\"100%\" name=\"createKPIDivFrame\"  src=\"about:blank\" ></iframe>")

    var source='dashboardTemplateAction.do?templateParam2=getCreateKPIs&divId='+divId+'&dashboardId='+dashboardId;
    var frameObj=document.getElementById("createKPIDivFrame");
    frameObj.src=source;

}
function GraphRename(dashletName,dashboardid,graphId,dashletId,refReportId,kpiMasterId,dispSequence,dispType,fromDesigner)
{
dashId=dashboardid
graphid=graphId
dashletid = dashletId
refreportId = refReportId
kpimasterId = kpiMasterId
dispseq = dispSequence
disptype = dispType
fromdesigner = fromDesigner
$("#GraphRenameDialog").dialog({
autoOpen: false,
height:200,
width: 300,
position: 'justify',
modal: true
});
$("#graphNameVal").attr('value',dashletName)
$("#NewgraphName").attr('value','')
$("#GraphRenameDialog").dialog('open')
}
function updateGraphName()
{
  var newgraphname = $("#NewgraphName").val()
    $.ajax({
        url: 'dashboardTemplateViewerAction.do?templateParam2=updateGraphName&dashboardid='+dashId+'&graphId='+graphid+'&NewGraphName='+newgraphname+'&dashletid='+dashletid,
        success: function(data){
          $("#GraphRenameDialog").dialog('close')
         displayGraph(dashletid,dashId,refreportId,graphid,kpimasterId,dispseq,disptype,newgraphname,'','','',fromdesigner);
        }
    });
}
function RelatedGraphs(dashletName,folderId,graphId,dashletId,ElementIds)
{
var innerdiv = "<table><tr><td> <a onclick='drillToReports('"+dashletName+"')' class='ui-icon ui-icon-gear'></a></td><td><a onclick='drillToReports()'>Drill Down</a></td></tr>&nbsp;&nbsp;&nbsp;&nbsp;<tr></tr><tr><td><a class='ui-icon ui-icon-copy' onclick='defineRelatedgrphs()'></a></td><td><a onclick='defineRelatedgrphs()'>Define Related Graphs</a></td></tr>&nbsp;&nbsp;&nbsp;&nbsp;<tr></tr><tr><td><a class='ui-icon ui-icon-extlink'></a></td><td><a onclick='viewRelatedgrphs()'>view Related Graphs</a></td></tr></table>";
//var innerdiv = "<table><tr><td> <a onclick='drillToReports('"+dashletName+"','"+folderid+"')' class='ui-icon ui-icon-gear'></a></td><td><a onclick='drillToReports()'>Drill Down</a></td></tr></table>"
$("#RelatedGraphsDialog").html(innerdiv)
elementids = ElementIds
 dashletid =  dashletId
 graphid = graphId
 newgDashletName = dashletName
folderidGlobal = folderId
$("#RelatedGraphsDialog").dialog({
autoOpen: false,
height:200,
width: 300,
position: 'justify',
modal: true
});
 $("#RelatedGraphsDialog").dialog('open')
}
function drillToReports(dashletName,folderid)
{
var innerdivofdrill = "<table><tbody id='grTorepTbody'></tbody><tfoot><tr><td>&nbsp;&nbsp;</td></tr><tr colspan='4' align='center'><td colspan='4' align='center'><input type='button' value='GO' onclick='graphToRep()' align='center'/></td></tr></tfoot></table>";
$("#GraphdrillToRep").html(innerdivofdrill)
if($("#GraphdrillToRep").html()!=null){
$("#GraphdrillToRep").dialog({
autoOpen: false,
height:200,
width: 400,
position: 'justify',
modal: true
});
}
if(folderid=='undefined'){
$.ajax({
        url: 'dashboardTemplateViewerAction.do?templateParam2=GetReportNamesforGraph&foldersIds='+folderid+'&graphid='+graphid+'&newGraphName='+dashletName,
        success: function(data){
            if(data!='')
            {
                                var htmlVal=data.split("~")
                                $("#grTorepTbody").html(htmlVal[0]);
                                selectedgrRepId=htmlVal[1];
//                                alert("selectedgrRepId\t"+selectedgrRepId)
            }
        }
    });
}
else{
 $.ajax({
        url: 'dashboardTemplateViewerAction.do?templateParam2=GetReportNamesforGraph&foldersIds='+folderid+'&graphid='+graphid+'&newGraphName='+dashletName,
        success: function(data){
            if(data!='')
            {
                                var htmlVal=data.split("~")
                                $("#grTorepTbody").html(htmlVal[0]);
                                selectedgrRepId=htmlVal[1];
//                                alert("selectedgrRepId\t"+selectedgrRepId)
            }
        }
    });
}
$("#GraphdrillToRep").dialog('option','title','Drill To Report')
$("#GraphdrillToRep").dialog('open')
}
function KPIDrillindb(folderId,graphId,dashletName)
{
var innerdivofdrill = "<table><tbody id='grTorepTbody'></tbody><tfoot><tr><td><input type='button' value='GO' onclick='graphToRep()' /></td></tr></tfoot></table>";
$("#GraphdrillToRep").html(innerdivofdrill)
if($("#GraphdrillToRep").html()!=null){
$("#GraphdrillToRep").dialog({
autoOpen: false,
height:200,
width: 400,
position: 'justify',
modal: true
});
}
$.ajax({
        url: 'dashboardTemplateViewerAction.do?templateParam2=GetReportNamesforGraph&foldersIds='+folderId+'&graphid='+graphId+'&newGraphName='+dashletName,
        success: function(data){
            if(data!='')
            {
                                var htmlVal=data.split("~")
                                $("#grTorepTbody").html(htmlVal[0]);
                                selectedgrRepId=htmlVal[1];
//                                alert("selectedgrRepId\t"+selectedgrRepId)
            }
        }
    });
$("#GraphdrillToRep").dialog('option','title','Drill To Report')
$("#GraphdrillToRep").dialog('open')
}
function graphToRep()
{
$("#GraphdrillToRep").dialog('close')
 $("#RelatedGraphsDialog").dialog('close')
var idval = "#selectReportgr"+selectedgrRepId
drillid = $(idval).val();
//alert("drillid\t"+drillid)
var url= 'reportViewer.do?reportBy=viewReport&REPORTID=' + drillid;
document.frmParameter.action = url;
document.frmParameter.submit();
}
function defineRelatedgrphs()
{
$("#DbrdRelatedGraphsDialog").html("")
var divId="addRelatedGraphs"
var dragDivId="dragdivGraphs"
$("#"+divId).remove();
 $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=getRelatedGraphs&elementids='+elementids+'&folderidGlobal='+folderidGlobal,
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
 var innerhtmlofdiv="<div id='"+divId+"'  title='Design Related Graphs' style='overflow:auto'><div id='"+dragDivId+"'style='overflow:auto'></div><table><tr colspan='2' align='center'><td colspan='2' align='center'><input type='button' class='navtitle-hover' style='width:auto' value='Save' id='relatedgraphssave' onclick='SaveRelatedGraphsName()'></td></tr></table></div>";
$("#DbrdRelatedGraphsDialog").html(innerhtmlofdiv)
if($("#"+divId).html()!=null){
$("#"+divId).dialog({
        autoOpen: false,
        height: 550,
        width: 700,
        position: 'justify',
        modal: true,close: function(){
                       $("#DbrdRelatedGraphsDialog").html("")
                   }
    });
}
$("#"+divId).dialog('open');
}

 function addRegion()
                {
                    flag=true
                    $("#noOfRows").val("");
                    $("#noOfColumns").val("");
                    $("#regionDialog").dialog('open');
                }

function clearParams(){
  var confirmtext = confirm("Parameters Are Going To Be Cleared");
  if(confirmtext==true){
   $("#sortable").html("");
   timeDimArray=new Array
   y=new Array
   x=new Array
   paramArray=new Array
   paramIdArray=new Array
  }
}
function editGraph(dispgrptypObj){
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }

}
function openkpiGrpType(grpType,dashletId,dbrdId,folderId,graphId,dashletName,reportid,kpiMasterId,dispSequence,dispType,kpiName,forDesigner,editDbrd)
        {
         $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=getKpiGraphType&dbrdId='+dbrdId+'&dashletId='+dashletId+'&grpType='+grpType,
                   function(data){
                         displayKPIDashGraph(dashletId,dbrdId,reportid,graphId,kpiMasterId,dispSequence,dispType,dashletName,forDesigner,editDbrd)

                   });
}
        function displayDbrdTimeInfo(repId){
    $("#dbrdTimecont").toggle(500);
     $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=getDbrdTimeDisplay&repId='+repId+'&from=fromtab',
                   function(data){
                  $("#DbrdTimeinfo").html("");
                  $("#DbrdTimeinfo").html(data);

                   });


        }
        function setVals(paramids,paramnames){
         $.post(
                    'dashboardTemplateAction.do?templateParam2=setParamhashmap&paramids='+paramids+'&paramnames='+paramnames+'&dashboardId='+document.getElementById("dbrdId").value,
                   function(data){


               });
          }
       function getDbrdTimeDisplay(repId){
                 $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=getDbrdTimeDisplay&repId='+repId+'&from=fromclock',
                     function(data){
                         $('#DashTimeSpan').html(data);
              document.getElementById('DashTimeSpan').style.display="";
                     });
          }
       function setNewId(did)
       {
    insertedNew.lang = did;
       }
       function divMove(objId){
    whichDog=document.getElementById(objId);
       }

       function ddInit(e){
    topDog=isIE ? "BODY" : "HTML";
    hotDog=isIE ? event.srcElement : e.target;
    while (hotDog.id!="titleBar"&&hotDog.tagName!=topDog){
        hotDog=isIE ? hotDog.parentElement : hotDog.parentNode;
      }

    if (hotDog.id=="titleBar"){

        offsetx=isIE ? event.clientX : e.clientX;
        offsety=isIE ? event.clientY : e.clientY;
        nowX=parseInt(whichDog.style.left);
        nowY=parseInt(whichDog.style.top);
        ddEnabled=true;
        document.onmousemove=dd;

        }
      }
       function dd(e){
    if (!ddEnabled) return;
    whichDog.style.left=isIE ? nowX+event.clientX-offsetx : nowX+e.clientX-offsetx;
    whichDog.style.top=isIE ? nowY+event.clientY-offsety : nowY+e.clientY-offsety;
    xpost=isIE ? nowX+event.clientX-offsetx : nowX+e.clientX-offsetx;
    ypost=isIE ? nowY+event.clientY-offsety : nowY+e.clientY-offsety;

    whichDog.style.left = xpost+"px";
    whichDog.style.top = ypost+"px";

    return false;
       }
       function xy(){
    ddEnabled=false;
       }
       function closeRepSpan(repId){
    document.getElementById('reptimeDetails').style.display="none";
       }
       function closeDashSpan(repId){
    document.getElementById('DashTimeSpan').style.display="none";
       }
       function submiturls12($ch,dashbrdId)
       {

    $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=saveDbrdUrl&repId='+dashbrdId,
                   function(data){
      });
    document.frmParameter.action = $ch;
    document.frmParameter.submit();
}
      function getDrillReportViewBys(drillrepId,elementId,drillReptype)
      {
//          alert("elementId"+elementId)
          $("#MyDialogbox").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 200,
                        position: 'justify',
                        modal: true,
//                        title:dashletName,
                        resizable:false
             });

            $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=getViewbysfromReport&drillrepId='+drillrepId+'&elementId='+elementId+'&drillReptype='+drillReptype,
                   function(data){
                        $("#MyDialogbox").html(data);

                  });
$("#MyDialogbox").dialog('open')

      }
      function getAllViewBysAfterDrill(drillrepId,viewbyids,viewbynames,elementId){
          var ids1 = new Array();
          var names1 = new Array();
             ids = ids1;
             names = names1;
          drillviewbyids = viewbyids;
          drillviewbynames = viewbynames;
          drillviewbyids = drillviewbyids.toString().replace("[", "", "gi");
                    drillviewbyids = drillviewbyids.toString().replace("]", "", "gi");
                    drillviewbynames = drillviewbynames.toString().replace("[", "", "gi");
                    drillviewbynames = drillviewbynames.toString().replace("]", "", "gi");
                    for(var i=0;i<drillviewbyids.toString().split(",").length;i++){
                        ids.push(drillviewbyids.toString().split(",")[i])
                    }
                    for(var j=0;j<drillviewbynames.toString().split(",").length;j++){
                        names.push(drillviewbynames.toString().split(",")[j])
                    }
          $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=getAllViewBysAfterDrill&repIdAfterDrill='+drillrepId+"&elementId="+elementId,
                   function(data){
                   $("#MyDialogbox").html(data);
                  });
                  $("#MyDialogbox").dialog('open');
      }
function deleteColumn1(index,name){
                    var LiObj=document.getElementById(index+name);
                    var ULObj=document.getElementById(index);
                    ULObj.removeChild(LiObj);
                    var i=0
                        for(i=0;i<ids.length;i++){
                        var idVals = ids[i].replace(" ", "", "gi");
                        var idVal = index.trim();
                            if(idVals==idVal){
                                ids.splice(i, 1);
                            }
                        }
                        for(i=0;i<names.length;i++){
                            var nameVals = names[i].trim();
                            var nameVal = name.trim();
                            if(nameVals==nameVal){
                                names.splice(i, 1);
                            }
                        }
}


      function updateViewBys(drillrepId,elementId){
          $("#MyDialogbox").dialog("close");
          $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=getSelectedDrillViewBys&drillrepId='+drillrepId+'&drillviewbyids='+ids+'&drillviewbynames='+names+'&elementId='+elementId,
                   function(data){
//                        if(data==1){
                            alert("View Bys updated successfully")
                            window.location.href = window.location.href;
//                        }else{
//                            alert("View Bys not updated successfully")
//                        }
                   });
      }

      function drillToRepWithVieBy(viewbyId,repId,currentviewbyid,currentviewbyvalues,elementId,drillreptype)
      {
         $("#MyDialogbox").dialog("close")
          $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=getViewbyURL&repId='+repId+'&viewbyId='+viewbyId+"&currentviewbyid="+currentviewbyid+"&Cboarpvalues="+currentviewbyvalues+"&drillreptype="+drillreptype+"&dashboardId="+parent.document.getElementById("dbrdId").value,
                   function(data){
                   if(drillreptype=='D'){
                       document.forms.frmParameter.action=data;
                       document.forms.frmParameter.submit();
                   }
                   else{
                      window.location.href = data;
                   }
                  });


      }
      function showGroupKpis(divId)
      {
         document.getElementById('divId').value=divId;
         var frameObj=document.getElementById("groupdataDispmem");
         var source="dashboardTemplateAction.do?templateParam2=getGroupMeasures&foldersIds="+parent.buildFldIds()+'&divId='+divId+'&dashboardId='+parent.document.getElementById("dbrdId").value;
         frameObj.src=source;
         $("#groupMeasureDialog").dialog('option','title','Edit GroupMeasures')
         $("#groupMeasureDialog").dialog('open');
      }

      function getDbGroupMeassure(divId,typeValue)
      {
          alert("in getDbrdGrpColumns() of dashBoardDesignJs")
          document.getElementById('divId').value=divId;
//    var divId=document.getElementById('divId').value;
//    alert("divId"+divId)
        graphCount++;
        var dashboardId=document.getElementById('dbrdId').value;
//        $("#graphListDialog").dialog('close');
        $("#grpMeassureDialog").dialog('open');
        var frameObj=document.getElementById("grpMeassure");
        var source = "GroupMeassure.jsp?folderIds="+buildFldIds()+"&grpType="+typeValue+"&gid="+graphCount+"&divId="+divId+"&dashboardId="+dashboardId;
        frameObj.src=source;

      }
      function canceldbrdGroup()
      {
          $("#grpMeassureDialog").dialog('close');
      }

      // added by ramesh
function getviewby(tdid,grptype)
{
    var source = "pbDbtextkpi.jsp?paramArray="+paramArray+"&paramIdArray="+paramIdArray+"&tdid="+tdid+"&grptype="+grptype+"&dashboardId="+document.getElementById("dbrdId").value;
    var frameObj=document.getElementById("viewbyframe");
    frameObj.src=source;
   $("#viewbydivid").dialog('open');
}
function getDbrdKpiTableColumns(divId,typeValue,selectedDim){
    document.getElementById('divId').value=divId;
//    var divId=document.getElementById('divId').value;
//    alert("divId"+divId)
    graphCount++;
    var dashboardId=document.getElementById('dbrdId').value;
    $("#graphListDialog").dialog('close');
    $("#tableColsDialog").dialog('open');
    var frameObj=document.getElementById("tablecols");
    var source = "pbDbrdGraphCols.jsp?folderIds="+buildFldIds()+"&grpType="+typeValue+"&gid="+graphCount+"&divId="+divId+"&dashboardId="+dashboardId+'&selectedDim='+selectedDim;
    frameObj.src=source;
}
function TextKpiDrilldown(elmntId,folderIds,reportId, dashletId, kpiMasterId,fromDesigner,grpStatus,kpiNames,kpiType){
    var frameObj=document.getElementById("TextkpiDrillDispmem");
    if (folderIds == null || folderIds == "null" || folderIds == ""){
        folderIds = buildFldIds();
        fromDesigner=true;
    }
    frameObj.src="pbTextKPIDrillDown.jsp?kpiId="+encodeURIComponent(elmntId)+"&folderIds="+folderIds+"&reportId="+reportId+"&dashletId="+dashletId+"&kpiMasterId="+kpiMasterId+"&fromDesigner="+fromDesigner+"&grpStatus="+grpStatus+"&kpiNames="+encodeURIComponent(kpiNames)+"&kpiType="+kpiType;
    $("#TextkpiDrillDialog").dialog('open');
}
      //added by ramesh
 function showTextKpiCommentDialog(dashletId,id){
    var paramid=id
    var paramvalue=parent.$("#param"+paramid).val()
    $("#kpiComment").dialog('open');
    var frameObj = document.getElementById("kpiCommentFrame");
    frameObj.src = "textKpiTableComments.jsp?paramid="+paramid+"&dashletId="+dashletId+"&paramvalue="+paramvalue;
}
function saveTextKpiComment(paramid,dashletId,userid){

    var paramvalue=parent.$("#param"+paramid).val()
    var comntText=document.getElementById("commentArea").value;
    var reportId = parent.document.getElementById("REPORTID").value;
    var divIdObj=parent.document.getElementById("Dashlets-"+dashletId);
    $.ajax({
        url: 'dashboardTemplateViewerAction.do?templateParam2=saveTextKpiComments&paramvalue='+paramvalue+'&commentText='+comntText+'&REPORTID='+reportId+'&dashletId='+dashletId+'&userid='+userid,
       beforeSend:function(){
      parent.document.getElementById("Dashlets-"+dashletId).innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="100px"  style="position:absolute" ></center>';
                                },
       success: function(data){
//            parent. getRefreshPage(reportId,dashletId)g
               divIdObj.innerHTML =data;

        }
    });
    parent.$("#kpiComment").dialog('close');

}
//function getRefreshPage(reportId,dashletId)
//{
//        $.ajax({
//        url: 'dashboardTemplateViewerAction.do?templateParam2=refreshTextKpiCommentsPage&REPORTID='+reportId+'&dashletId='+dashletId,
//        success: function(data){
//
//        }
//    });
//}
function cancelRepTargetKpi(){
   $("#addTargetkpisDialog").dialog('close');
}
function dispTargetKpis(daydiff,kpiType,divId,elementId,kpiMasterId,repId,existingKpis,existkpiNames){
    var kpis="";
    var kpisName="";
    var kpiUl=document.getElementById("sortable");
    var kpiIds=kpiUl.getElementsByTagName("li");
    var dbrdId = parent.document.getElementById("dbrdId").value;
    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        kpisName=kpisName+","+kpiIds[i].id;
    }
//    alert("kpis"+kpis)
//    alert("kpisName"+kpisName)
    $.post(
                     'dashboardTemplateViewerAction.do?templateParam2=saveTargetMapping&Kpis='+encodeURIComponent(kpis)+'&KpiNames='+encodeURIComponent(kpisName)+"&dashletId="+divId+"&kpiMasterId="+kpiMasterId+"&dbrdId="+repId+"&elementId="+elementId,
                     function(data){
                    alert("Your Changes Will Be Shown When You Login Next Time")
//                      $.ajax({
//            //url: 'dashboardTemplateViewerAction.do?templateParam2=buildKpis&Kpis='+kpis+'&KpiNames='+kpisName+'&divId='+divId+'&kpiType='+kpiType+'&dashboardId='+dbrdId+'&countkpis='+more+"&kpiMasterId="+divId,
//            url: 'dashboardViewer.do?reportBy=buildKPI&Kpis='+encodeURIComponent(existingKpis)+'&KpiNames='+encodeURIComponent(existkpiNames)+'&divId='+divId+'&kpiType='+kpiType+'&dashboardId='+repId+"&kpiMasterId="+kpiMasterId+"&dashletId="+divId,
//            success: function(data) {
                parent.cancelRepTargetKpi();
                //parent.Viewer_previewKpis(timeDimid,dayDiff,kpiType,timeParams,params,more);
//                parent.displayKPI(divId, repId, "","",kpiMasterId, "","","", false);
//            }
//        });

//                       parent.displayKPI(dashletId, dbrdId, "","",divId, "","","","true");
                   });

}


// added by ramesh janakuttu

function reorderKpis(dayDiff,kpiType,divId){

    var kpis="";
    var kpisName="";
    var kpiUl=document.getElementById("sortable");
    var kpiIds=kpiUl.getElementsByTagName("li");
    var dbrdId = parent.document.getElementById("dbrdId").value;
    for(var i=0;i<kpiIds.length;i++){
        var dkpiIds=(kpiIds[i].id).split("^");
        kpis=kpis+","+dkpiIds[1];
        kpisName=kpisName+","+kpiIds[i].id;
    }
    if(kpiIds.length!=0){
        kpis=kpis.substr(1);
        kpisName=kpisName.substr(1);

        comntDivId=parent.document.getElementById("divId").value;
        var dashletId = divId;
        parent.document.getElementById("kpiIds").value=kpis;
        parent.document.getElementById("kpis").value=kpisName;
        parent.document.getElementById("kpiType").value=kpiType;
        kpisName=kpisName.replace("%","%".charCodeAt(0),"gi");

//        $.ajax({
//            url: 'dashboardViewer.do?reportBy=buildKPI&Kpis='+encodeURIComponent(kpis)+'&KpiNames='+encodeURIComponent(kpisName)+'&divId='+divId+'&kpiType='+encodeURIComponent(kpiType)+'&dashboardId='+dbrdId+"&kpiMasterId="+divId+"&dashletId="+dashletId,
//            success: function(data) {
//                parent.cancelRepKpi();
//                parent.displayKPI(dashletId, dbrdId, "","",divId, "","","","true");
//            }
//        });
//alert("kpis"+kpis)
//alert("KpiNames"+kpisName)
        if(kpiType=="KpiWithGraph"){
//            alert($("#Dashlets-"+divId+"-kpi").val());

            var divIdObj=parent.document.getElementById("Dashlets-"+divId+"-kpi");
            divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        $.post(
                     'dashboardViewer.do?reportBy=reOrderKpi&Kpis='+encodeURIComponent(kpis)+'&KpiNames='+encodeURIComponent(kpisName)+'&divId='+divId+'&kpiType='+encodeURIComponent(kpiType)+'&dashboardId='+dbrdId+"&kpiMasterId="+divId+"&dashletId="+dashletId,
                     function(data){
                       parent.cancelReorderKpi();
                       $("#Dashlets-"+divId+"-kpi").empty();
                       divIdObj.innerHTML=data;
                   });
        }else{

        $.post(
                     'dashboardViewer.do?reportBy=buildKPI&Kpis='+encodeURIComponent(kpis)+'&KpiNames='+encodeURIComponent(kpisName)+'&divId='+divId+'&kpiType='+encodeURIComponent(kpiType)+'&dashboardId='+dbrdId+"&kpiMasterId="+divId+"&dashletId="+dashletId,
                     function(data){
                       parent.cancelReorderKpi();
                       parent.displayKPI(dashletId, dbrdId, "","",divId, "","","","true");
                   });
    }
}
}
function genrateandsubmiturls(reportid)
{
        var $ch ;
        $ch = 'reportViewer.do?reportBy=viewReport&REPORTID='+reportid;
        submiturls1($ch)
}
function submitDbrdUrl(path){
          document.forms.frmParameter.action=path;
//                alert("path "+path)
          document.forms.frmParameter.submit();
}
function displayKpiWithGraph(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,editDbrd){
    //alert("displayKpiWithGraph");
    var divIdObj=document.getElementById("Dashlets-"+dashletId);
    divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'dashboardViewer.do?reportBy=displayKpiWithGraph&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&editDbrd='+editDbrd,
        success: function(data){
            divIdObj.innerHTML =data;
        }
    });
}
     function updateKpiheads()
     {
         var ctxpath = parent.document.getElementById("ctxPath1").value;
          $("#KpiHeadsRename").dialog('close')
     var kpinewheads = '';
     var j=0;
     for(var i=0;i<idx;i++){
       kpinewheads=kpinewheads+","+ $("#kpiheads"+i).val()
     }
     $.post(ctxpath+"/dashboardTemplateViewerAction.do?templateParam2=updateKpiHeads&dbrdId="+repIdSch+"&dashletId="+dashId+"&kpiMasterId="+kpiGlMasterId+"&kpiNewHeads="+encodeURIComponent(kpinewheads),
     function(data){
         displayKPI(dashId, repIdSch, "","",kpiGlMasterId, "","","","false")
     })
     }
              function saveDashboard(){
                    ////////alert("checking username and password")
                    var dashboardName = document.getElementById('dashboardName').value;
                    var dashboardDesc = document.getElementById('dashboardDesc').value;
                    $.ajax({ type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
                        url: 'dashboardTemplateAction.do?templateParam2=checkDashboardName&dashboardName='+encodeURIComponent(dashboardName)+'&dashboardDesc='+encodeURIComponent(dashboardDesc),
                        success: function(data){
                            if(data!=""){
                                document.getElementById('duplicateDashboard').innerHTML = data;
                                document.getElementById('dashboardsave').disabled = true;
                            }
                            else if(data==''){
                                //document.forms.dashboardForm.action = "dashboardTemplateViewerAction.do?templateParam2=goToDashboardDesigner&dashboardName="+dashboardName+"&dashboardDesc="+dashboardDesc;
                                //document.forms.dashboardForm.method="POST";
                                //document.forms.dashboardForm.submit();

                                document.forms.frmParameter.action="dashboardTemplateViewerAction.do?templateParam2=saveDashboard&divCnt="+grpCnt+'&dashboardId='+document.getElementById("dbrdId").value+'&dashboardName='+dashboardName+'&dashboardDesc='+dashboardDesc+'&dashletIds='+DashIds+'&NewDashIds='+NewDashIds;
                                document.forms.frmParameter.submit();
                            }
                        }
                    });
                }
     function dispParameters(){
                    if(document.getElementById("tabParameters").style.display=="none"){
                     document.getElementById("tabParameters").style.display = "block";
                     var frameObj = document.getElementById("widgetframe");
                     var source = 'divPersistent.jsp?method=forDashParameters&block=yes';
                     $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=hideTdDash&repId='+document.getElementById("dbrdId").value+'&block=yes'+'&tdType=paramtd',
                         function(data){

                           });
                    }else{
                    document.getElementById("tabParameters").style.display = "none";
                    var frameObj = document.getElementById("widgetframe");
                    var source = 'divPersistent.jsp?method=forDashParameters&block=no';

                    frameObj.src = source;
                     $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=hideTdDash&repId='+document.getElementById("dbrdId").value+'&block=no'+'&tdType=paramtd',
                         function(data){

                           });

                }
                }
   function hideTdDash(){
                    if(document.getElementById("leftTd").style.display=="none"){
                        document.getElementById("leftTd").style.display = "";
                        var frameObj = document.getElementById("widgetframe");
                        var source = 'divPersistent.jsp?method=forleftTdDash&block=no';
                        frameObj.src = source;
//                        document.getElementById("tdImage").src = "<%=request.getContextPath()%>/icons pinvoke/control-180.png";
                        $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=hideTdDash&repId='+document.getElementById("dbrdId").value+'&block=no'+'&tdType=lefttd',
                         function(data){

                           });
                    }else{
                        document.getElementById("leftTd").style.display = "none";
                        var frameObj = document.getElementById("widgetframe");
                        var source = 'divPersistent.jsp?method=forleftTdDash&block=yes';
                        frameObj.src = source;
//                        document.getElementById("tdImage").src = "<%=request.getContextPath()%>/icons pinvoke/control.png";
                         $.post(
                    'dashboardTemplateViewerAction.do?templateParam2=hideTdDash&repId='+document.getElementById("dbrdId").value+'&block=yes'+'&tdType=lefttd',
                         function(data){

                           });
                    }
                }
   function createDbrdGraphs(divId,hideDiv){

                    document.getElementById('dbrdgraph1').value=divId;
                    document.getElementById('hidedbrdgraph1').value=hideDiv;
                    $("#NewDbrdGraphViewer").dialog('open');
                }
      function saveDbrdSchedule(runFlag){
                    var stDATE = $("#stDatepicker").val();
                    var edDATE = $("#edDatepicker").val();
                     $("#schedulerActionDialog").dialog('close');
                        $("#ScheduleTrackerDialog").dialog('close');
                        $.post(""+parent.contextPath+"/calenderSetUpAction.do?timeCalenders=valiDateCalnder&startYear="+stDATE+'&endYear='+edDATE+'&scheduler='+"scheduler",
                     function(data){
                            if(data=='true')
                            {
                    if(runFlag=="true"){
                        //               $("#loading").show();
                        $.post(""+parent.contextPath+"/dashboardTemplateViewerAction.do?templateParam2=runScheduler&elmntId="+elmntIdSch+'&KpimasterSc='+KpimasterSc+'&dashId='+dashId+'&repIdSch='+repIdSch,$("#scheduleDbrdForm").serialize(),
                        function(data){
   //                                      $("#loading").hide();
                            alert("KPI has been sent Successfully")
                        });
                    }
                    else{
                        $.post(""+parent.contextPath+"/dashboardTemplateViewerAction.do?templateParam2=saveScheduler&elmntId="+elmntIdSch+'&KpimasterSc='+KpimasterSc+'&dashId='+dashId+'&repIdSch='+repIdSch,$("#scheduleDbrdForm").serialize(),
                        function(data) {
                            alert("KPI has been saved Successfully ")
                        });
                    }

                    }else
                    {
                                alert("Please select End Year correctly.")
                    }
                    });
                }

                function showActions(){
                    $("#scoreCardActionTypesDialog").dialog('close');
                    var pastorNew = $("input[@name=pastOrNew]:checked").val();
                    var action = $("#ActionTypes").val();
                    var ctxPath = '<%= request.getContextPath() %>';

                    if (pastorNew == "new"){
                        $("#scoreCardActionsDialog").dialog('open');
                        var source = ctxPath+"/scoreCardActions.jsp?scoreCardId="+selectedScoreCard+"&score="+currentScore+"&pastOrNew="+pastorNew+"&action="+action;
                        var actionsFrame = document.getElementById("scardActions");
                        actionsFrame.src = source;
                    }
                    else{
                        $("#pastScoreCardActionsDialog").dialog('open');
                        var source = ctxPath+"/scoreCardActions.jsp?scoreCardId="+selectedScoreCard+"&score="+currentScore+"&pastOrNew="+pastorNew+"&action="+action;
                        var actionsFrame = document.getElementById("scardPastActions");
                        actionsFrame.src = source;
                    }
                }
                /* added by srikanth.p for OLAPGraph*/
function OLAPGraph(dashBoardId,dashletId,dashletName){
    $("#OLAPGraphDialog").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 1000,
                        position: 'justify',
                        modal: true,
//                        title:dashletName,
                        resizable:false,
                        close: function(){
                            $.ajax({
                                    url: "dashboardViewer.do?reportBy=closeOLAPView&dashletId="+dashletId+"&dashBoardId="+dashBoardId,
                                    success: function(data){
//                                        parent.$("#OLAPGraphDialog").dialog('close');
                                    }
                                });
                        }
             });

    $("#OLAPGraphDialog").dialog('option', 'title', dashletName);

    $("#OLAPGraphDialog").dialog('open');
//    document.forms.frmParameter.action="dashboardViewer.do?reportBy=OLAPGraphViewer&dashletId="+dashletId+"&dashBoardId="+dashBoardId;
//    document.forms.frmParameter.submit();
    var frameObj = document.getElementById("OLAPGraphFrame");
    var source ="OLAPGraph.jsp?dashBoardId="+dashBoardId+"&dashletId="+dashletId;
    frameObj.src=source;
}
function buildGraphOnKpi(elementId, dashletId,dashboardId,graphId,kpiMasterId,dispType,graphType){
               // alert('buildGraphOnKpi');
                var divIdObj=document.getElementById("Dashlets-"+dashletId+"-graph");
                divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({ type: 'GET',
               async: false,
               cache: false,
               timeout: 30000,
                   url: 'dashboardViewer.do?reportBy=buildGraphOnKPI&dashletId='+dashletId+'&dashBoardId='+dashboardId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispType='+dispType+'&graphType='+graphType+'&elementId='+elementId,
                  success: function(data){
                        divIdObj.innerHTML =data;
                    }
                });
            }

            function kpiWithGraphDrilldown(dashboardId,dashletId,kpiMasterId,folderIds){

                $.post(
                'dashboardViewer.do?reportBy=drillToReportForKpiWithGraph&dashboardId='+dashboardId+'&dashletId='+dashletId+'&kpiMasterId='+kpiMasterId+'&folderIds='+folderIds,
                function(data){

                    $("#DrillToReportDialoge").html(data);
                    $("#DrillToReportDialoge").dialog('open');

                });
            }
            function chartType(divId){
//    alert('');
//    $("#"+divId).toggle(500);
var divObj=document.getElementById("chartType-"+divId);
 $("#chart-"+divId).attr('disabled', true);
if(divObj.style.display=='none'){
        divObj.style.display='';
    }
    else{
        divObj.style.display='none';
    }

}
function saveChartType(dashletId,graphId){
    var checkedType=$("input[name='chartTypeButton-"+graphId+"']:checked").val();
    grpId=graphId;
    var divObj=document.getElementById("chartType-"+dashletId);
    divObj.style.display='none';
}
function buildDBJqplot(graphType,graphId,dashboardId,dashletId,height,dispType){
//    alert("builbdBJqplot")
    var divIdObj = "";
    if(dispType == "KpiWithGraph"){
       divIdObj = parent.document.getElementById("Dashlets-"+dashletId+"-graph");
//       alert("divIdObj"+divIdObj)
       if(divIdObj == null){
           divIdObj = parent.document.getElementById("Dashlets-"+dashletId);
       }
    }else{
//        alert("normal gr")
       divIdObj = parent.document.getElementById("Dashlets-"+dashletId);
    }


//    alert("divIdObj"+divIdObj)
       divIdObj.innerHTML = '<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>'
  divIdObj.innerHTML ='<iframe name=\"graphdisplay-'+dashletId+'\" id=\"graphdisplay-'+dashletId+'\" width=\"100%\" height=\"'+height+'px\" frameborder=\"0\" style=\"overflow-x: hidden;overflow-y: hidden;\" src=\"PbDbrdJQplot.jsp?graphType='+graphType+'&graphId='+graphId+'&dashboardId='+dashboardId+'&dashletId='+dashletId+'&height='+height+'px;\"></iframe>';
//        $.post(
//           'dashboardViewer.do?reportBy=buildDBJqplotGraph&graphType='+graphType+'&graphId='+graphId+'&dashboardId='+dashboardId+'&dashletId='+dashletId,
//           function(data){
////               alert(data);
////               var dashletObj=document.getElementById("Dashlets-"+dashletId)
////               dashletObj.innerHTML="<div id='chart-"+dashletId+"' style=\"width:600px; height:280px;\" align=\"left\" ></div>";
////               dashletObj.innerHTML=data;
////               var jsonVar=eval(data);
//
//               $("#Dashlets-"+dashletId).html(data);
//           });

}
function graphTypesDispForDb(graphId,dashletId){
//    alert(graphId);
    var divObj=document.getElementById("chartType-"+dashletId);
    divObj.style.display='none';
    var checkedType=$("input[name='chartTypeButton-"+graphId+"']:checked").val();
//    alert(checkedType);
    if(checkedType == 'JQPlot'){
        var divObj=document.getElementById("dispgrptypes" + graphId + "-jqplot");
        if(divObj.style.display=='none'){
            divObj.style.display='';
        }
        else{
            divObj.style.display='none';
        }

    }
    else{
        var divObj=document.getElementById("dispgrptypes" + graphId);
         if(divObj.style.display=='none'){
            divObj.style.display='';
        }
        else{
            divObj.style.display='none';
        }

    }
}
function drilltoRepForJQPLot(url,viewbyList,pointIndex,data){
//    alert('url:'+url+' viewbyList:'+viewbyList+' pointIndex:'+pointIndex+' data:'+data);
    var temp1=viewbyList.split("[");
    var temp2=temp1[1].split("]");
    var viewbys=temp2[0].split(",");
    var urltoSubmit;
    if(pointIndex != 'null'){
        urltoSubmit=url+viewbys[pointIndex].replace(/\s+/g,"");
    }
//    alert(urltoSubmit);
    parent.submiturls1(urltoSubmit);

}
    function jqplotMeterchart(ids, dashletId,dashboardid)
            {

                 $.ajax({
                            url: 'dashboardTemplateViewerAction.do?templateParam2=bulidJqMeterChart&dashboardId='+dashboardid+'&dashletId='+dashletId+'&ids='+ids+'&graphtype=metter',
                            success: function(data){
                               // alert(data)
                                 //var jsonVar=eval(data)
                                // var meter=jsonVar.meterchart
                                  //$("#Dashlets-"+dashletId).hide();
                                  $("#Dashlets-"+dashletId).html(data);
                                 //$("#Dashlets-"+dashletId).html(data.htmlStr);
                                  //$("#"+ids).html(htmlVar.htmlStr);
                                  //$("#"+ids).show();                           }
                        }
                 });

            }
            function changeGraphTypeOnKpi(dashletId,dashBoardId,refReportId,graphId,kpiMasterId,dispSequence,dispType,dashletName,graphType,fromDesigner){
//                alert("changeGraphTypeOnKpi")
                var divIdObj=parent.document.getElementById("Dashlets-"+dashletId+"-graph");
                alert("divIdObj"+divIdObj)
                divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({
                    type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                   url: 'dashboardViewer.do?reportBy=displayGraph&dashletId='+dashletId+'&dashBoardId='+dashBoardId+'&refReportId='+refReportId+'&graphId='+graphId+'&kpiMasterId='+kpiMasterId+'&dispSequence='+dispSequence+'&dispType='+dispType+'&dashletName='+dashletName+'&graphType='+graphType+'&fromDesigner='+fromDesigner,
                  success: function(data){
                        divIdObj.innerHTML =data;
                    }
                });
            }
            //modified by Dinanath
 function viewAlertCondition(currentValue,ElementId,kpiMasterid,dashletId,reportIdSch,targetValue,devper,kpiType,elemtName, columnHeaderNames,firstViewByN,secondViewByN){
    parent.currentvalue = currentValue;
    parent.elmntIdSch = ElementId;
    parent.KpimasterSc = kpiMasterid;
    parent.dashId = dashletId;
    parent.repIdSch = reportIdSch;
    parent.targetVal=targetValue;
    parent.devPercent=devper;
    parent.kpitype=kpiType;
    parent.elemtName=elemtName;
    parent.allHeaderNames=columnHeaderNames;
    var columnHeaderNames2=columnHeaderNames.toString().split(",");
    allClmnHeaderNames=columnHeaderNames2;
    parent.firstViewByName=firstViewByN;
    parent.secondViewByName=secondViewByN;

    var alertsHtml1 = '';
    alertsHtml1+="<table border='0' id='AlertConditionTable' ><tr><td></td><td></td><td></td><td>Alert type</td><td>Column Header</td></tr><br><tr id='row0'>";
    //  alertsHtml1+=" <td>When Value</td><td><select name='0operatorsForAvg' id='0operatorsForAvg' onchange=\"onoperatorAvg(0,this.value)\">";
    alertsHtml1+=" <td> Value should be </td><td><select name='0operators' id='0operators' onchange='onoperator(0,this.value)'>";
    for (var j=0; j < strOperators.length; j++) {
        alertsHtml1+="<option value='"+strOperators[j]+"'>"+strOperators[j]+"</option>";

    }
    alertsHtml1+="</select></td><td><input type='text' name='sValues0' id='sValues0' ></td>";
    alertsHtml1+="<td id='0eValuesT' style='display:none'><input type='hidden' name='0eValues' id='0eValues' ></td><td><select name='alertType0' id='alertType0'>";
    for (var i=0; i < alertTypes.length; i++) {
        alertsHtml1+="<option value='"+alertTypes[i]+"'>"+alertTypes[i]+"</option>";

    }
    alertsHtml1+="</select></td><td><select name='columnHeaderNames0' id='columnHeaderNames0'>";
    for (var m=0; m < columnHeaderNames2.length; m++) {
        alertsHtml1+="<option value='"+columnHeaderNames2[m]+"'>"+columnHeaderNames2[m]+"</option>";

    }
    alertsHtml1+="</select></td></tr></table>";
    alertsHtml1+="<table border='0'><tr><td colspan='3' style='height:10px'>&nbsp;</td></tr>";
    alertsHtml1+="<tr align='center'><td align='center' colspan='3'>";
    alertsHtml1+="<input class='navtitle-hover' type='button' style='margin-right: 3%;' value='Add Row' onclick='addRow()'>";
    alertsHtml1+="<input class='navtitle-hover' type='button' style='margin-right: 3%;' value='Delete Row' onclick='deleteRow()'>";
    alertsHtml1+="<input class='navtitle-hover' type='button' name='Done' value='Done' onclick=\"saveCondition('"+ElementId+"')\">";
    alertsHtml1+="</td></tr><tr><td colspan='3' style='height:10px'>&nbsp;</td></tr>";
    alertsHtml1+="<tr><td colspan='3'><font size='1' color='red'>*</font>Values displayed are in Absolute format.Please enter Absolute values only.</td></tr></table>";
    parent.$("#scheduleAlertForm").html(alertsHtml1);
    // parent.$("#AlertConditionDialog").html(alertsHtml1);
    parent.$("#AlertConditionDialog").dialog('open')

}
function onoperator(id,symbol)
{
    //                alert(symbol);
    var box = parseInt(id);
    var open = document.getElementById(id+"eValues");
    if(symbol=="<>"){
        open.type="text";
        document.getElementById(id+"eValuesT").style.display="block";
    }
    else{
        document.getElementById(id+"eValuesT").style.display="none";
        open.type="hidden";
    }
}
//modified by Dinaanth
function addRow(){
    var table = document.getElementById("AlertConditionTable");
    var rowCount = table.rows.length;
    var idx = rowCount-1;
    var alertsHtml = '';
    var addHeight = $("#AlertConditionDialog").height();
    $("#AlertConditionDialog").height(addHeight+20);
    alertsHtml = "<tr id='row"+idx+"'><td>&nbsp;</td><td><select name='"+idx+"operators' id='"+idx+"operators' onchange=\"onoperator('"+idx+"',this.value)\">";
    for (var i=0; i < strOperators.length; i++) {
        alertsHtml+="<option value='"+strOperators[i]+"'>"+strOperators[i]+"</option>";

    }
    // alertsHtml+="</select></td><td><input type='text' name='sAvgValues0' id='sAvgValues"+idx+"'></td>";
    alertsHtml+="</select></td><td><input type='text' name='sValues"+idx+"' id='sValues"+idx+"'></td>";
    alertsHtml+="<td id='"+idx+"eValuesT' style='display:none'><input type='hidden' name='"+idx+"eValues' id='"+idx+"eValues' ></td><td><select name='alertType"+idx+"' id='alertType"+idx+"'>";
    for (var k=0; k < alertTypes.length; k++) {
        alertsHtml+="<option value='"+alertTypes[k]+"'>"+alertTypes[k]+"</option>";

    }
    alertsHtml+="</select></td><td><select name='columnHeaderNames"+idx+"' id='columnHeaderNames"+idx+"'>";
    for (var m=0; m < allClmnHeaderNames.length; m++) {
        alertsHtml+="<option value='"+allClmnHeaderNames[m]+"'>"+allClmnHeaderNames[m]+"</option>";

    }
    alertsHtml+="</select></td></tr>";
    $('#AlertConditionTable > tbody:last').append(alertsHtml)

}
function deleteRow(){
    try {
        var table = document.getElementById("AlertConditionTable");
        var rowCount = table.rows.length;

        if(rowCount > 1) {
            table.deleteRow(rowCount - 1);
        }
        var addHeight = $("#AlertConditionDialog").height();
       $("#AlertConditionDialog").height(addHeight-20);
    }catch(e) {
        alert(e);
    }
}
function saveCondition(ElementId){
     parent.$("#AlertConditionDialog").dialog('close')
    var condition=$("#0operatorsForAvg").val();
    var elementid=ElementId;
    var Value=$("#sAvgValues0").val();
//    $.ajax({
//                    url: "dashboardViewer.do?reportBy=applyAlert&ElementId="+elementid+"&condition="+condition+"&Value="+Value,
//                    success: function(data){
////                        alert(data);
//
//                        alert("");
//                        $("#ScheduleTrackerDialog1").show();
//                    }
//                });
parent.$("#ScheduleTrackerDialog1").dialog('open')
}
             function viewSchedulerAndTracker(currentValue,ElementId,kpiMasterid,dashletId,reportIdSch,targetValue,devper,kpiType,elemtName){
                     parent.currentvalue = currentValue;
                     parent.elmntIdSch = ElementId;
                     parent.KpimasterSc = kpiMasterid;
                     parent.dashId = dashletId;
                     parent.repIdSch = reportIdSch;
                     parent.targetVal=targetValue;
                     parent.devPercent=devper;
                     parent.kpitype=kpiType;
                     parent.elemtName=elemtName;

                    var alertsHtml = ''
                    alertsHtml+="<table align='center' border='0' width='100%'><tr><td><input type='checkbox' id='dayId'  name='' align='left'>&nbsp;&nbsp;&nbsp;Daily</td></tr>";
                    alertsHtml+="<tr><td><input type='checkbox' id='mtdId'  name='' checked align='left'>&nbsp;&nbsp;&nbsp;MTD</td><tr>"
                    alertsHtml+="<tr><td><input type='checkbox' id='wtdId'  name='' align='left'>&nbsp;&nbsp;&nbsp;WTD</td></tr>";
                    alertsHtml+="<tr><td><input type='checkbox' id='qtdId'  name='' align='left'>&nbsp;&nbsp;&nbsp;QTD</td></tr>";
                    alertsHtml+="<tr><td><input type='checkbox' id='ytdId'  name='' align='left'>&nbsp;&nbsp;&nbsp;YTD</td></tr>";
                    alertsHtml+="</table><br>";

                    alertsHtml+="<table align='center'><tr><td><input type='button' id='' name='' value='Next' onclick=\"trackerAction()\"></td>"
                    alertsHtml+="</tr></table>"
                    parent.$("#ScheduleTrackerDialog").html(alertsHtml);

                    parent.$("#ScheduleTrackerDialog").dialog('open')

                }
                function getDisplayTables(ctxpath,paramslist,dashBoardIdFact,dashletIdFact,kpiMasterIdFact,kpitypFact,kpidashidFact,folderids){
//        getdimmap.getFact(Parameters);
        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
            var frameObj=parent.document.getElementById("kpidataDispmem1");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpis&foldersIds="+folderids+"&divId="+kpidashidFact+"&kpiType="+kpitypFact+"&dashboardId="+dashBoardIdFact+"&kpiMasterId="+kpiMasterIdFact+"&dashletId="+dashletIdFact+"&tableList=true";
                    frameObj.src=source;
                    ////////alert(frameObj.src)
            //$("#tableList").attr('checked',true);
        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+folderids,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var frameObj=parent.document.getElementById("kpidataDispmem1");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpis&foldersIds="+folderids+"&divId="+kpidashidFact+"&kpiType="+kpitypFact+"&dashboardId="+dashBoardIdFact+"&kpiMasterId="+kpiMasterIdFact+"&dashletId="+dashletIdFact;
                    frameObj.src=source;
                });

        }
    }
    function showList(ctxpath,paramslist,folderids){
        if(document.getElementById("paramVals").style.display=='none'){
            $("#paramVals").show();
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+folderids,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);

                });
        }else{
           $("#paramVals").hide();
        }

    }
    function setValueToContainer(ctxpath,repId,dashBoardIdFact,dashletIdFact,kpiMasterIdFact,kpitypFact,kpidashidFact,folderids){
        $("#paramVals").hide();
        var tabLst = $("#tabsListIds").val();
        $("#tabsListVals").val('')
            $("#tabsListIds").val('')
        $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    var frameObj=parent.document.getElementById("kpidataDispmem1");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpis&foldersIds="+folderids+"&divId="+kpidashidFact+"&kpiType="+kpitypFact+"&dashboardId="+dashBoardIdFact+"&kpiMasterId="+kpiMasterIdFact+"&dashletId="+dashletIdFact;
                    frameObj.src=source;
                });
    }
    function selectTables(tdId,tname){
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
    function getDisplayTablesTargert(ctxpath,paramslist,targetkpityp,targetreportId,targetkpimasterid,targetdashletId,targetelementId,targettargetelem,targettargetElemname,folderids){
//        getdimmap.getFact(Parameters);
        var check = $("#tableListtarget").is(":checked")
        if($("#tableListtarget").is(":checked")){
            $("#tabListDivtarget").hide();
            $("#tablistLinktarget").hide();
            $("#goButtontarget").hide();
            $("#tabsListIdstarget").val("");
            $("#tabsListValstarget").val("");
            var frameObj=document.getElementById("addTargetkpidataDispmem");
            var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpisForTarget&foldersIds="+folderids+"&kpiType="+targetkpityp+"&dashboardId="+targetreportId+"&kpiMasterId="+targetkpimasterid+"&dashletId="+targetdashletId+"&elementId="+targetelementId+"&targetelem="+targettargetelem+"&targetElemName="+targettargetElemname+"&tableList=true";
            frameObj.src=source;
        }else{
            $("#tabListDivtarget").show();
            $("#tablistLinktarget").show();
            $("#goButtontarget").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+folderids,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablestarget('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramValstarget").html(htmlVar);
                    var frameObj=document.getElementById("addTargetkpidataDispmem");
                    var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpisForTarget&foldersIds="+folderids+"&kpiType="+targetkpityp+"&dashboardId="+targetreportId+"&kpiMasterId="+targetkpimasterid+"&dashletId="+targetdashletId+"&elementId="+targetelementId+"&targetelem="+targettargetelem+"&targetElemName="+targettargetElemname;
                    frameObj.src=source;
                });

        }
    }
    function showListTargert(ctxpath,paramslist,folderids){
        if(document.getElementById("paramValstarget").style.display=='none'){
            $("#paramValstarget").show();
            $("#tabListDivtarget").show();
            $("#tablistLinktarget").show();
            $("#goButtontarget").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+folderids,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablestarget('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramValstarget").html(htmlVar);

                });
        }else{
           $("#paramValstarget").hide();
        }

    }
    function setValueToContainerTargert(ctxpath,repId,targetkpityp,targetreportId,targetkpimasterid,targetdashletId,targetelementId,targettargetelem,targettargetElemname,folderids){
        $("#paramValstarget").hide();
        var tabLst = $("#tabsListIdstarget").val();
        $("#tabsListValstarget").val('')
            $("#tabsListIdstarget").val('')
        $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+repId+'&tabLst='+tabLst,
                function(data){
                    var frameObj=document.getElementById("addTargetkpidataDispmem");
            var source="dashboardTemplateViewerAction.do?templateParam2=getMoreKpisForTarget&foldersIds="+folderids+"&kpiType="+targetkpityp+"&dashboardId="+targetreportId+"&kpiMasterId="+targetkpimasterid+"&dashletId="+targetdashletId+"&elementId="+targetelementId+"&targetelem="+targettargetelem+"&targetElemName="+targettargetElemname;
            frameObj.src=source;
                });
    }
    function selectTablestarget(tdId,tname){
        //alert(tname)
        document.getElementById(tdId).style.display='none';
        if($("#tabsListValstarget").val() == ""){
            $("#tabsListValstarget").val(tname)
            $("#tabsListIdstarget").val(tdId)
        }else{
            var Ids = $("#tabsListIdstarget").val()+","+tdId
            var value = $("#tabsListValstarget").val()+","+tname
            $("#tabsListIdstarget").val(Ids)
            $("#tabsListValstarget").val(value)
        }
    }
    function getDisplayTablesdesigner(ctxpath,paramslist){
//        getdimmap.getFact(Parameters);
        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
            var frameObj=document.getElementById("kpidataDispmem");
            var source="dashboardTemplateAction.do?templateParam2=getKpis&foldersIds="+parent.buildFldIds()+'&divId='+showKpisdivId+'&kpiType='+showKpistype+'&dashboardId='+parent.document.getElementById("dbrdId").value+"&tableList=true";
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
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesdesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var frameObj=document.getElementById("kpidataDispmem");
                    var source="dashboardTemplateAction.do?templateParam2=getKpis&foldersIds="+parent.buildFldIds()+'&divId='+showKpisdivId+'&kpiType='+showKpistype+'&dashboardId='+parent.document.getElementById("dbrdId").value;
                    frameObj.src=source;
                });

        }
    }
    function showListdesigner(ctxpath,paramslist){
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
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesdesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);

                });
        }else{
           $("#paramVals").hide();
        }

    }
    function selectTablesdesigner(tdId,tname){
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
    function setValueToContainerdesigner(ctxpath){
        $("#paramVals").hide();
        var tabLst = $("#tabsListIds").val();
        $("#tabsListVals").val('')
        $("#tabsListIds").val('')
        $.post(ctxpath+"/reportViewer.do?reportBy=setTableListToContainer&repId="+parent.document.getElementById("dbrdId").value+'&tabLst='+tabLst,
                function(data){
                    var frameObj=document.getElementById("kpidataDispmem");
        var source="dashboardTemplateAction.do?templateParam2=getKpis&foldersIds="+parent.buildFldIds()+'&divId='+showKpisdivId+'&kpiType='+showKpistype+'&dashboardId='+parent.document.getElementById("dbrdId").value;
        frameObj.src=source;
                });
    }
    function resetdash(dashboardid,dashbaordname){
       var path="dashboardViewer.do?reportBy=viewDashboard&REPORTID="+dashboardid+"&pagename="+dashbaordname+"&editDbrd=false&resetFlag=true"
        document.forms.frmParameter.action=path;
//                alert("path "+path)
                document.forms.frmParameter.submit();
    }
    function localSaveInDbRegions(dashBoardId,dashletId,kpiMasterId,kpiType,idsStr,namesStr){
        var ctxpath = parent.document.getElementById("ctxPath1").value;

           $.ajax({
                        type:'GET',
                        async:false,
                        cache:false,
                        timeout:30000,
            url:ctxpath+'/dashboardTemplateViewerAction.do?templateParam2=localSaveInDbRegions&dashBoardId='+dashBoardId +"&dashletId="+dashletId+"&kpiMasterId="+kpiMasterId+"&kpiType="+kpiType,
                    success: function(data){
                        if(data != ''){
                        alert("Updated Successfully")
                        //$("#Dashlets-"+dashletId).html(reload(true));
//                        window.location.reload(true);
                        window.location.href = window.location.href;
                        }
                    }
          });
    }

     function cancelDim(){
               $("#AddMoreParamsDiv").dialog('close');
            }
     function dashbardQuery(reportId){
      $("#showSqlStrDialog").dialog('open');
      $("#showSqlStrDialog").html(document.getElementById("showSqlbox").value);
     }
     //sandeep
          var query;
     var allParamIds="";
     var ctxPath;
     var checkId="";
     var scrollFlag=true
     var onScrollFlag=0;
     var scrollcheck=false
     startVal=1;
     var unceckall=false
      var firstscroll=true
     var scrollPayLoad="";
     var scrollURL=""
     var elementname=""
     var scHeight=""
     var scTop=""
     var clHgt=""
     var xmlHttp;
     function getlovfilters(id,elementid){
               elementname=id;
               scrollcheck=false
                 firstscroll=true
                 unceckall=false
//       parent.getlovparams(id,elementid)
query=elementid
var country="";
 var newArr=new Array();
 newArr.push(elementid)
 allParamIds=newArr.toString();
getSuggestionsglobal(country)

}
function onScrollDiv1(divObj){
 scHeight=(divObj.scrollHeight);
    scTop=(divObj.scrollTop);

    clHgt=(divObj.clientHeight);
    if(scrollFlag){
        if(scTop==(scHeight-clHgt)){
            scrollcheck=true
              firstscroll=false
            startVal=startVal+20;
            scrollPayLoad1=scrollPayLoad+"&startValue="+startVal;

            sendRequest1(scrollURL, scrollPayLoad1);
            onScrollFlag=1;

        }
    }
}
function getlovfilters12(){
    for(var i=0; i<50;i++){


    }


}
function getSuggestionsglobal(searchKey){

    var url;
    ctxPath=document.getElementById("h").value;
    var searchKey=searchKey;
    var payload;
    url=ctxPath+"/dsrv";
        var temp = encodeURIComponent(searchKey); //encodeURIComponent(
        payload = "q="+temp+"&"+"query="+query
        checkId=checkId+payload;
        sendRequest1(url, payload);
        scrollFlag=true;
        startVal=1;
        scrollPayLoad=payload;
}
function sendRequest1(url, payload)
{
    scrollURL=url;
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null){
        alert ("Your browser does not support AJAX!");
        return;
    }
    var parValues=allParamIds.split(",");
    var idsList=new Array();
    var passValues1=new Array();
    var passValues;
    for(var i=0;i<parValues.length;i++){
        var id=parValues[i].replace("CBOARP","");
//        var tempArray=new Array();
        idsList.push(id);
        if(i==0){
passValues1.push("ALL");


        }else{
            passValues += ";[ALL]";
        }
passValues=encodeURIComponent(passValues1[0]);
    }
    var elementid=idsList[0];
//    var searchKey="";
//    if(searchKey==""){
        searchKey='@';

//    }
    var mainUrl=url+"?"+payload+"&allParamIds="+idsList+"&parArrVals="+(passValues)+"&REPORTID="+$("#REPORTID").val()+"&orderVal="+orderVal+"&fromglobal=true&scrollFlag="+startVal;
    getLOVS(mainUrl,elementid);


}
var globalfiltvalues="";
var globalfiltwsplit="";
function getLOVS(mainURL,elementid){

    $.ajax({
        url: mainURL,
        success: function(data){
//            if(onScrollFlag==0){
//               var value="MAR - 2013_0_14101";
//    var value1="Apr - 2013_0_14101";
      var html="";
//        html = html + "<option  value='"+value+"'>MAR - 2013</option>";
//        html = html + "<option  value='"+value1+"'>APR - 2013</option>";

      var jsondata = JSON.parse(data)["data"];
 var selectedlist
 selectedlist=parent.filterMapNew[query];
     var dependtype = JSON.parse(data)["dependtype"];
         var names = jsondata.split("\n");
          if(firstscroll){
          globalfiltvalues=names;
            globalfiltwsplit=jsondata;
          }
          if(scrollcheck){
              if(unceckall){
              globalfiltwsplit=globalfiltwsplit+jsondata;
                globalfiltvalues = globalfiltwsplit.split("\n");
           }
           }
if(selectedlist!=="" && selectedlist!==undefined && selectedlist.length>=1 && selectedlist[0]!="All"){
     if(firstscroll){
for(var k=0;k<selectedlist.length;k++){
     var  value=selectedlist[k].replace("'", " ")+"_"+k+"_"+query

              value=value+"_selecttrue";

              if(selectedlist[k]!="All" ||selectedlist[k]!="ALL"){
             html = html + "<option   value='"+value+"'>"+selectedlist[k]+"</option>";
    }
}
    }else{if(scrollcheck){
        for(var k=0;k<selectedlist.length;k++){
             var  value=selectedlist[k].replace("'", " ")+"_"+k+"_"+query

              value=value+"_selecttrue";

              if(selectedlist[k]!="All" ||selectedlist[k]!="ALL"){
             html = html + "<option   value='"+value+"'>"+selectedlist[k]+"</option>";
    }
              }
    }
    for(var j=0;j<globalfiltvalues.length-1;j++)
        {

               var flag="true";
                for(var k=0;k<selectedlist.length;k++){
                    if(selectedlist[k]==globalfiltvalues[j]){
                        flag="false";
                    }
                }
               if(flag=="true"){

         value=globalfiltvalues[j].replace("'", " ")+"_"+j+"_"+query


             html = html + "<option  value='"+value+"'>"+globalfiltvalues[j]+"</option>";
                         flag="false";
}


        }

        $("#"+elementname).html("");
    }
for(var j=0;j<names.length-1;j++)
        {
             var flag="true";
             for(var g=0;g<selectedlist.length;g++){
                 if(names[j]==selectedlist[g]){

     flag="false";

     }
             }
             if(flag=="true"){
                  var  value=names[j].replace("'", " ")+"_"+j+"_"+query
                    html = html + "<option  value='"+value+"'>"+names[j]+"</option>";
                         flag="false";
             }
        }
}else{
 if(scrollcheck &&  unceckall){
         for(var j=0;j<globalfiltvalues.length-1;j++)
        {
            var id;
             var value;

               var flag="true";
               if(flag=="true"){
//    var value=names[j]+"_"+j+"_"+query
     var value;
     var id;

         value=globalfiltvalues[j].replace("'", " ")+"_"+j+"_"+query


             html = html + "<option  value='"+value+"'>"+globalfiltvalues[j]+"</option>";
                         flag="false";
}


        }

        $("#"+elementname).html("");
        }
        for(var j=0;j<names.length-1;j++)
        {
             var value=names[j]+"_"+j+"_"+elementid
             if(unceckall && scrollcheck ){
                 value=value
             }else{
              value=value+"_selecttrue";
             }
             html = html + "<option  value='"+value+"'>"+names[j]+"</option>";
        }
}
        if(html!="" && names!=""){
            if(scrollcheck){
                $("#"+elementname).append(html);
            }else{
                  $("#"+elementname).html('');
               $("#"+elementname).html(html);
            }
                $("#"+elementname).multiselect('refresh');
        }
//            }
//            else if(onScrollFlag==1){
//                handleResponse2(data);
//            }

        }
    });

}
function editViewBydash(){
$("#editViewByDiv").dialog({
            autoOpen: false,
            height: 400,
            width: 520,
            position: 'justify',
            modal: true
        });
    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath = parent.document.getElementById("ctxPath1").value;
    var frameObj = document.getElementById("editViewByFrame");
     $.post(ctxPath+'/dashboardViewer.do?reportBy=showViewBydb&fromdashboard=true&REPORTID='+REPORTID+'&ctxPath='+ctxPath,
         function(data){
            var source = ctxPath+"/Report/Viewer/ChangeViewBy.jsp?REPORTID="+REPORTID+"&fromdashboard=true&ctxPath="+ctxPath;
            frameObj.src = source;
      });
     $("#editViewByDiv").dialog('open');
        }
function applyGlobalFilterinOneview(id,nameArr,chekid){
//  alert("hello")
                                            var id=$("#"+chekid).val().split("_")[2]
//                                          alert("applyGlobalFilterinOneview")
                                            var filterValues=[];

                                            if(document.getElementById(chekid).checked){
                                                filterValues.push($("#"+chekid).val().split("_")[0]);
                                                parent.filterMapNew[$("#"+chekid).val().split("_")[2]].push($("#"+chekid).val().split("_")[0]);
                                            }
                                            else{


var filterValues=[];
                                                 filterValues=parent.filterMapNew[$("#"+chekid).val().split("_")[2]];
                                                 if(filterValues==undefined || filterValues==""){
                                                    for(var j=0;j<globalfiltvalues.length-1;j++)
        {
            filterValues.push(globalfiltvalues[j]);
        }

                                                 }
                                                  var index = filterValues.indexOf($("#"+chekid).val().split("_")[0]);
        filterValues.splice(index, 1);
                                                parent.filterMapNew[$("#"+chekid).val().split("_")[2]]=  filterValues;                                                //        filterValues.splice(index, 1);
                                            }

                                            $("#CBOARP"+id).val(JSON.stringify(parent.filterMapNew[$("#"+chekid).val().split("_")[2]]))

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
function unchekdashall(id,flag){
  var selectedFilters = [];
 var selectedFilters1 = [];
  parent.filterMapNew[id]=selectedFilters1;
  selectedFilters.push("ALL")
    if(flag=="checkall"){
//         parent.filterMapNew[id].push(selectedFilters)
    $("#CBOARP"+id).val(JSON.stringify(selectedFilters));
    unceckall=false
//     alert($("#CBOARP"+id).val())
    }else{
//  parent.filterMapNew[id].push(selectedFilters)
    $("#CBOARP"+id).val(JSON.stringify(selectedFilters));
    unceckall=true
//     alert($("#CBOARP"+id).val())
    }
}
function dashgenerateQuickTrend(dashboardId,dashletId,ElementId,measurename,targetElementId,targetElemName,priorid,viewbyname,viewbyid){ //changed by sruthi for prior jqplotgraph
               $("#dashquickTrend").dialog('open');
                 var frameObj = document.getElementById("dashgraphframe");
                        var source = 'dashboardgraph.jsp?dashboardId='+dashboardId+'&dashletId='+dashletId+'&ElementId='+ElementId+'&targetElementId='+targetElementId+'&targetElemName='+targetElemName+'&priorid='+priorid+'&viewbyname='+viewbyname+'&viewbyid='+viewbyid+'&measurename='+encodeURIComponent(measurename);//changed by sruthi for jqplotgraph
                        frameObj.src = source;
//               dashgenerateQuickTrend12(dashboardId,dashletId,ElementId,measurename)
           }
     //sandeep for graphs in target dashboard -- measure level
      function dashgenerateQuickTrend12(dashboardId,dashletId,ElementId,measurename){
//  parent.$("#chartData").val("");
 var ctxpath = parent.document.getElementById("ctxPath1").value;
 var htmlVar="";

  var paramDisp=parent.document.getElementById("dashquickTrend");
    $("#dashquickTrend").dialog('open');
//    document.getElementById("favParams").style.display='none';
  var frameObj = parent.document.getElementById("dashgraphframe");
    paramDisp.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="100px" height="80px"  style="position:absolute" ></center>';

   $.ajax({
            //url: 'dashboardTemplateViewerAction.do?templateParam2=buildKpis&Kpis='+kpis+'&KpiNames='+kpisName+'&divId='+divId+'&kpiType='+kpiType+'&dashboardId='+dbrdId+'&countkpis='+more+"&kpiMasterId="+divId,
           url:ctxpath+'/dashboardViewer.do?reportBy=buildqucktrend&dashboardId='+dashboardId+'&dashletId='+dashletId+'&Kpis='+ElementId+'&measurename='+measurename,
            success: function(data) {
            if(data=="false"){
            $("#quickTrend").dialog('open');
               htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
               $("#quickTrend").append(htmlVar);
//                $("#xtendChartTD").show();
//                $("#loading").hide();
            }
            else{
                       $("#dashquickTrend").dialog('open');

//                var jsondata = JSON.parse(data)["data"];
                  var chartData ={};
            var rowMeasId =[];
            var rowMeasName =[];
            var rowViewIds = [];
            var rowViewName =[];
            var aggregation =[];
            if(typeof JSON.parse(data)["measureids"] !== "undefined"){
                rowMeasId = JSON.parse(data)["measureids"];
            }
            if(typeof JSON.parse(data)["measures"] !="undefined"){
                rowMeasName = JSON.parse(data)["measures"];
            }
            if(typeof JSON.parse(data)["viewbyids"] !== "undefined"){
                rowViewIds = JSON.parse(data)["viewbyids"];
            }
            if(typeof JSON.parse(data)["viewbynames"] !="undefined"){
                rowViewName = JSON.parse(data)["viewbynames"];
            }
            if(typeof JSON.parse(data)["Aggregation"] !="undefined"){
                aggregation= JSON.parse(data)["Aggregation"];
            }
//                $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
//                var meta = JSON.parse(JSON.parse(data)["meta"]);
                $("#viewby").val(rowViewName);
                $("#viewbyIds").val(rowViewIds);
                $("#measure").val(rowMeasName);
                $("#measureIds").val(rowMeasId);
                $("#aggregation").val(aggregation);
//                $("#drilltype").val((meta["drillType"]));
//              $("#draggableViewBys").val('');
                $("#type").val("quick");



            var chartDetails={};
                        var viewBys=rowViewName;
                        var viewIds=rowViewIds;


                        chartDetails["viewBys"] = viewBys;
                            chartDetails["chartType"] = "quickAnalysis";
                            chartDetails["viewByLevel"] = "single";
                            chartDetails["meassures"] = rowMeasName;
                            chartDetails["aggregation"] = aggregation;
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["meassureIds"] = rowMeasId;
                            chartDetails["dimensions"] = viewIds;
                            chartDetails["size"] = "S";
                            chartData["chart1"] = chartDetails;

              var ctxPath=parent.document.getElementById("h").value;
             parent.$("#chartData").val(JSON.stringify(chartData));

                $.post(ctxpath+'/reportViewer.do?reportBy=designGraphInDesigner&reportId='+dashboardId,
             function(data){
            var jsonVar=eval('('+data+')')
            var graphIds ='';
            var graphCount=0;
            var parentDiv = '';
            graphCount++;
            graphIds = graphIds+","+graphCount;

            graphIds=jsonVar;
//            parent.$("#graphlist").hide();
//            parentDiv = parent.document.getElementById('tabGraphs');
//            parentDiv.style.height = '250'
            $.ajax({
//                url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphIds+'&grptypid=Bar&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
                url: 'reportTemplateAction.do?templateParam=buildGraphs&height=450&width=750&gid='+graphIds+'&grptypid=Line&isdashboard=true&graphChange=default&grpIds='+graphIds+'&REPORTID='+dashboardId+'&measurename='+measurename+'&measureid='+ElementId,
                success: function(data){
                    if(data != ""){
                       generateQuickTrendChart12(data);
                }
}
            });


        })
//
//             $.ajax({
//                          type:'POST',
//                          data:parent.$("#graphForm").serialize(),
//                     url: ctxPath+"/reportViewer.do?reportBy=buildchartsWithObject&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+encodeURIComponent(parent.$("#chartData").val()),
////                     url: ctxPath+"/reportViewer.do?reportBy=buildCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
//                            success: function(data){
//                                 parent.$("#loading").hide();
//                                 alert(grid+"   lkiyu")

//                            }
//                    });


                    }
  }
        });
//   parent.$("#dashquickTrend").html("");
//$("#dashquickTrend").dialog('open');
}
function generateQuickTrendChart12(data){
$("#dashquickTrend").html("");
//$("#quickTrend").html('');
var parameterName = "";
if(typeof viewByFilterName !="undefined"){
    parameterName = viewByFilterName;
}
var chartId = "chart1";
var chartData = JSON.parse(parent.$("#chartData").val());
var rowViewBy = parent.$("#viewby").val();
var rowViewId = parent.$("#viewbyIds").val();
var quickViewname = [];
var quickViewId =[];
var selectedView = chartData[chartId]["viewBys"];
var selectedViewId = chartData[chartId]["viewIds"];
//alert(JSON.stringify(chartData))
var timeDim = ["Month Year","Month - Year","Month-Year","Time","Year","year","Month","Qtr","qtr","Month "];
for(var i=0;i<rowViewId.length;i++){

    for(var k=0;k<timeDim.length;k++){

//    alert(rowViewBy[i]+"::::"+timeDim[k])
    if(rowViewBy[i] == timeDim[k] ){
    quickViewname.push(rowViewBy[i]);
    quickViewId.push(rowViewId[i]);
    }
}
}

var html = "";
html += "<div style='height:20px; width:100%'>";
//html += "<ul style='float:left'><li><strong>"+parameterName+"</strong></li></ul>";
html += "<div style='float:right'>"
html += "<select style='float:right;margin-right:10px'>";
for(var i=0;i<chartData[chartId]["meassures"].length;i++){
    html += "<option id='"+i+"' value='"+chartData[chartId]["meassures"][i]+"' onclick='changeMeasureArray(this.id)'>"+chartData[chartId]["meassures"][i]+"</option>";
}
html += "</select></div>";

//if(chartCount>0){
//    html += "<div style='float:right;display:none'>"
//}
//else{
//html += "<div style='float:right'>"
//}
//html += "<select style='float:right;margin-right:10px'>";
//for(var l=0;l< quickViewname.length; l++ ){
//    if(l==0)
//    html += "<option id='"+l+"' value='"+selectedView+":"+selectedViewId+"' onclick='changeQuickGroup(this.value)'>"+selectedView+"</option>";
//   if(quickViewname[l] !=selectedView)
//   html += "<option id='"+l+"' value='"+quickViewname[l]+":"+quickViewId[l]+"' onclick='changeQuickGroup(this.value)'>"+quickViewname[l]+"</option>";
//}
//html += "</select></div>";
//html += "</div>";
//html +="<div id='Hchart1' style='display:block;float:left;width:85%'></div>";

$("#dashquickTrend").html(html);
  $("#dashquickTrend").parent().css({ top: '30px' });
generateTrendChart12(data);
}

function generateTrendChart12(jsonData){
//  graphData=JSON.parse(jsonData);
// $("#Hchart1").html('');

$("#dashquickTrend").html("");
//$("#quickTrend").html('');
var parameterName = "";
if(typeof viewByFilterName !="undefined"){
    parameterName = viewByFilterName;
}
var chartId = "chart1";
var chartData = JSON.parse(parent.$("#chartData").val());
var rowViewBy = parent.$("#viewby").val();
var rowViewId = parent.$("#viewbyIds").val();
var quickViewname = [];
var quickViewId =[];
var selectedView = chartData[chartId]["viewBys"];
var selectedViewId = chartData[chartId]["viewIds"];
//alert(JSON.stringify(chartData))
var timeDim = ["Month Year","Month - Year","Month-Year","Time","Year","year","Month","Qtr","qtr","Month "];
for(var i=0;i<rowViewId.length;i++){

    for(var k=0;k<timeDim.length;k++){
//alert(rowViewBy[i])
//    alert(rowViewBy[i]+"::::"+timeDim[k])
    if(rowViewBy[i] == timeDim[k] ){
    quickViewname.push(rowViewBy[i]);
    quickViewId.push(rowViewId[i]);
    }
}
}

var html = "";
html += "<div style='height:20px; width:100%'>";
//html += "<ul style='float:left'><li><strong>"+parameterName+"</strong></li></ul>";
html += "<div style='float:right'>"
html += "<select style='float:right;margin-right:10px'>";
for(var i=0;i<chartData[chartId]["meassures"].length;i++){
    html += "<option id='"+i+"' value='"+chartData[chartId]["meassures"][i]+"' onclick='changeMeasureArray(this.id)'>"+chartData[chartId]["meassures"][i]+"</option>";
}
html += "</select></div>";
//if(chartCount>0){
//    html += "<div style='float:right;display:none'>"
//}
//else{
//html += "<div style='float:right'>"
//}
//html += "<select style='float:right;margin-right:10px'>";
//for(var l=0;l< quickViewname.length; l++ ){
//    if(l==0)
//    html += "<option id='"+l+"' value='"+selectedView+":"+selectedViewId+"' onclick='changeQuickGroup(this.value)'>"+selectedView+"</option>";
//   if(quickViewname[l] !=selectedView)
//   html += "<option id='"+l+"' value='"+quickViewname[l]+":"+quickViewId[l]+"' onclick='changeQuickGroup(this.value)'>"+quickViewname[l]+"</option>";
//}
//html += "</select></div>";
//html += "</div>";
//html +="<div id='Hchart1' style='display:block;float:left;width:85%'></div>";

//$("#quickTrend").html(html);

//  var chartData = JSON.parse(parent.$("#chartData").val());
$("#loading").hide();
 var charts = Object.keys(chartData);
// var data = JSON.parse(jsonData);
 for(var l=0;l<charts.length;l++){
 var divId = chartId;
html +="<div id='"+divId+"' style='display:block;float:left;width:95%'></div>";
html += "<div class='tooltip' id='my_tooltip1' style='display: none'></div>";

}
$("#dashquickTrend").html(html);
$("#"+divId).html(jsonData);



// for (var kq = 0; kq < charts.length; kq++) {
//              var ch = "chart1";
//              var html = "";
//              var currData=[];
//              var records = 12;
//              var chartType = chartData[ch]["chartType"];
//              var chartSize = chartData[ch]["size"];
//              var width = $(window).width() * .45;
//              if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
//            records = chartData[ch]["records"];
//        }
//              for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
//                  currData.push(data[ch][m]);
//              }
//
//
////     measureData = currData;
//   if(chartType == "quickAnalysis"){
//
//       if(chartSize == "SS"){
//            buildQuickLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width*1.5,"250");
//       }else {
////           buildLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"500");
//           parent.generateQuickTrendChart11(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"500");
////buildQuickLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"500");
//   }
//   }else {
//       if(chartSize=="SS"){
//           $("#Hchart1").css("width","50%");
//           $("#Hchart2").css("width","50%");
//           buildQuickBar(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"200");
//       }
//       else{
//       buildQuickBar(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"],width,"300");
//   }
// }
//}
        }
    //added by sruthi for hidecolumns
  function HideColumns(kpidashid,dashBoardId,hidecolumns,customkpiheads){
      var kpiheads=customkpiheads.replace("[","").replace("","").replace("]","");
      var parameters1=kpiheads.split(",");
      var parameters=[];
      var countval=0
      for(var p=0;p<parameters1.length;p++){
           parameters[countval] = parameters1[p].trim();
              countval++;
      }
    var json=null;
    if(hidecolumns!=null){
      var json1=hidecolumns.replace("[","").replace("","").replace("]","");
      json = json1.split(",");
    }
   var index=[];
    var count=0;
    var count1=0;
  var data=[];
//$("#HideColumns").dialog({
//        autoOpen: false,
//        height: 250,
//        width: 200,
//        position: 'justify',
//         title:'HideColumns',
//        modal: true
//    });
    if(json!=null){
    for(var k=0;k<json.length;k++) {
             index[count]=parameters.indexOf(json[k].trim());
             count++;
    }}
    for(var m=0;m<parameters.length;m++){
        data[count1]='undefind';
        count1++;
    }
   if(json!=null){
  for(var n=0;n<json.length;n++){
      data[index[n]]=parameters[index[n]];
  }
   }
    var html="";
    html+="<form><table>"
    if(json!=null){
       for(var k=0;k<parameters.length;k++) {
                        if(data[k]!='undefind'){
                           html+=" <tr><td><label><input type=\"checkbox\" name=\"parameters\" checked value='"+parameters[k]+"' >"+parameters[k]+"</label></td></tr>";
                        }else
                          html+=" <tr><td><label><input type=\"checkbox\" name=\"parameters\" value='"+parameters[k]+"' >"+parameters[k]+"</label></td></tr>";
         }
    }else{
        for(var i=0;i<parameters.length;i++){
          html+=" <tr><td><label><input type=\"checkbox\" name=\"parameters\" value='"+parameters[i]+"' >"+parameters[i]+"</label></td></tr>";
        }
    }
    html+="<tr><td><label><input type=\"button\" name=\"\" value=\"Done\" onclick=\"saveHideColumns(this.form.parameters,'"+kpidashid+"','"+dashBoardId+"')\"></label></td></tr>";
    html+="</table></form>"
    $("#HideColumnsdata").html(html);
//    $("#HideColumns").dialog().dialog('open');
   $("#HideColumns").dialog('open');
   }
function saveHideColumns(parameters,kpidashid,dashBoardId){

     $("#HideColumns").dialog('close');
//$("#HideColumns").dialog().dialog('close');
     var selectedItems = [];
     var count=0;
     for(var i=0;i<parameters.length;i++) {
        if(parameters[i].checked) {
            selectedItems[count] = parameters[i].value;
              count++;
        }
    }
    selectedItems=encodeURI(selectedItems)
      $.ajax({
                url: 'dashboardTemplateAction.do?templateParam2=hideColumns&selectedItems='+selectedItems+'&kpidashid='+kpidashid+'&dashBoardId='+dashBoardId,
                success: function(data){
                       displayKPI(kpidashid,dashBoardId, "","","", "","","","false")
}
            });
}
//ended by sruthi
