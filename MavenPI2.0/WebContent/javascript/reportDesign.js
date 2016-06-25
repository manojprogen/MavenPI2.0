/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var graphCount=0;
var graphTableCount=0;
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
var graphIds ='';
var sqlStr='';
var graphFlag = true;
var graphTableFlag = true;
var graphFlagCount = 0;
var exclParamViewbyFlag=false;

var rowparamsarr = new Array();
var colparamsarr = new Array();
var rowparamsNamesarr = new Array();
var colparamsNamesarr = new Array();
var name1 = '';
var currGrpId1 = '';
var typeValue1 = '';

$(document).ready(function(){
    if ($.browser.msie == true){
        $("#graphCols").dialog({
            autoOpen: false,
            height: 520,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#measuresDialog").dialog({
            autoOpen: false,
            height:400,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#rowParamDisplay").dialog({
            autoOpen: false,
            height: 450,
            width: 550,
            position: 'justify',
            modal:true
        });
        $("#colParamDisplay").dialog({
            autoOpen: false,
            height: 450,
            width: 550,
            position: 'justify',
            modal:true
        });
        $("#customDate").dialog({
            autoOpen: false,
            height: 150,
            width: 400,
            position: 'justify',
            modal: true
        });
        $("#customAggregation").dialog({
            autoOpen: false,
            height: 330,
            width: 330,
            position: 'justify',
            modal: true
        });
    }else{
        $("#graphCols").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#measuresDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#rowParamDisplay").dialog({
            autoOpen: false,
            height: 350,
            width: 500,
            position: 'justify',
            modal:true
        });
        $("#colParamDisplay").dialog({
            autoOpen: false,
            height: 335,
            width: 500,
            position: 'justify',
            modal:true
        });
        $("#customDate").dialog({
            autoOpen: false,
            height: 150,
            width: 330,
            //            position: 'justify',
            modal: true
        });
        $("#customAggregation").dialog({
            autoOpen: false,
            height: 150,
            width: 350,
            position: 'justify',
            modal: true
        });
    }



//    $("#PRG_PERIOD_TYPE").contextMenu({
//        menu: 'myMenuTime1'
//    }, function(action, el, pos) {
//        showAggregate(action, el, pos);
//    });


     $("#cdate").datepicker({
                   changeMonth: true,
                   changeYear: true,
                showButtonPanel: true,
                numberOfMonths: 1,
             stepMonths: 1
           });


});
function contMenuDate(){
    $("#AS_OF_DATE").contextMenu({
        menu: 'DateMenu'
    }, function(action, el, pos){
        showDate(action, el, pos);
    });
    $("#AS_OF_DATE1").contextMenu({
        menu: 'DateMenu'
    }, function(action, el, pos){
        showDate(action, el, pos);
    });
    $("#AS_OF_DATE2").contextMenu({
        menu: 'DateMenu'
    }, function(action, el, pos){
        showDate(action, el, pos);
    });
  
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
    if(dimId=='Time-Period Basis'){
        if(timeDimArray.length!=0){
            for(var timedim=0;timedim<timeDimArray.length;timedim++){
                if(timeDimArray[timedim]=='AS_OF_DATE1')
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
                if(timeDimArray[timedim]=='AS_OF_DATE2')
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
                if(timeDimArray[timedim]=='CMP_AS_OF_DATE1')
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
                if(timeDimArray[timedim]=='CMP_AS_OF_DATE2')
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
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
                childLI.onmouseup=function(){
                    contMenuDate();
                };
                var table=document.createElement("table");
                table.id=dimId+j;
                var row=table.insertRow(0);
                var cell1=row.insertCell(0);
                //////cell1.style.backgroundColor="#79C9EC";
                if(params[j]=='PRG_COMPARE'){
                    var a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                var cell2=row.insertCell(1);
                //// cell2.style.backgroundColor="#79C9EC";
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
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
                if(timeDimArray[tdim]=='PRG_PERIOD_TYPE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId)
                if(timeDimArray[tdim]=='PRG_COMPARE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId)
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
                 childLI.onmouseup=function(){
                    contMenuDate();
                };
                table=document.createElement("table");
                table.id=dimId+k;
                row=table.insertRow(0);
                cell1=row.insertCell(0);
                //////cell1.style.backgroundColor="#79C9EC";
                if(params[k]=='CMP_AS_OF_DATE1' || params[k]=='CMP_AS_OF_DATE2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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
        //if(timeDimArray.length!=0){
        //timeDimArray.splice(0,timeDimArray.length);
        //}
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
                //////cell1.style.backgroundColor="#79C9EC";
                if(params[k]=='CMP_AS_OF_DMONTH1' || params[k]=='CMP_AS_OF_DMONTH2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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
        //}
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
                //////cell1.style.backgroundColor="#79C9EC";
                if(params[k]=='CMP_AS_OF_DQUARTER1' || params[k]=='CMP_AS_OF_DQUARTER2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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
        if(timeDimArray.length!=0){
            for(var tdim=0;tdim<timeDimArray.length;tdim++){
                if(timeDimArray[tdim]=='AS_OF_DATE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
                if(timeDimArray[tdim]=='PRG_PERIOD_TYPE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
                if(timeDimArray[tdim]=='PRG_COMPARE')
                    deleteTimeDimParam(timeDimArray[tdim],dimId);
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
                //////cell1.style.backgroundColor="#79C9EC";
                if(params[k]=='CMP_AS_OF_DYEAR1' || params[k]=='CMP_AS_OF_DYEAR2'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[k]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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
                ////cell1.style.backgroundColor="#79C9EC";
                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
                if(timeDimArray[timedim]=='AS_OF_MONTH2')
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
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
                ////cell1.style.backgroundColor="#79C9EC";
                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
                if(timeDimArray[timedim]=='PRG_PERIOD_TYPE')
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
                if(timeDimArray[timedim]=='PRG_COMPARE')
                    deleteTimeDimParam(timeDimArray[timedim],dimId);
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
                ////cell1.style.backgroundColor="#79C9EC";
                if(params[j]=='PRG_COMPARE'){
                    var a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                var cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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
                ////cell1.style.backgroundColor="#79C9EC";
                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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
                ////cell1.style.backgroundColor="#79C9EC";
                if(params[j]=='PRG_COMPARE'){
                    a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
                if(j==0)
                    cell2.innerHTML='Year';
                else if(j==1)
                    cell2.innerHTML='Compare Year';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else if(dimId=='Time-Cohort Basis')
    {
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
                childLI.onmouseup=function(){
                    contMenuDate();
                };
                var table=document.createElement("table");
                table.id=dimId+j;
                var row=table.insertRow(0);
                var cell1=row.insertCell(0);
                //////cell1.style.backgroundColor="#79C9EC";
                if(params[j]=='PRG_COMPARE'){
                    var a=document.createElement("a");
                    a.href="javascript:deleteTimeDimParam('"+params[j]+"','"+dimId+"')";
                    a.innerHTML="a";
                    a.className="ui-icon ui-icon-close";
                    cell1.appendChild(a);
                }
                var cell2=row.insertCell(1);
                //// cell2.style.backgroundColor="#79C9EC";
                if(j==0)
                    cell2.innerHTML='From Date';
                else if(j==1)
                    cell2.innerHTML='To Date';
                else if(j==2)
                    cell2.innerHTML='Future From Date';
                else if(j==3)
                    cell2.innerHTML='Future To Date';
                else if(j==4)
                    cell2.innerHTML='Duration';
                childLI.appendChild(table);
                parentUL.appendChild(childLI);
            }
        }
    }else {
        for(i=0;i<params.length;i++){
            var span=params[i].getElementsByTagName("span");

            // x=paramArray.toString();
            // y=paramIdArray.toString();
            y=paramArray.toString();//change by bharu
            x=paramIdArray.toString();//change by bharu

            var xarr=x.split(',');
            var count=0;
            if(x!=''){
                xarr=x.split(',');
                //
                for(var i1=0;i1<xarr.length;i1++){
                    if(xarr[i1]==(span[0].id).split("-")[1]){
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
                childLI.className='navtitle-hover paramMenu';
                childLI.onmouseup=function(){
                    contMenu(this);
                };

                table=document.createElement("table");
                table.id=dimId+i;
                row=table.insertRow(0);
                cell1=row.insertCell(0);
                ////cell1.style.backgroundColor="#79C9EC";
                a=document.createElement("a");
                a.href="javascript:deleteParam('"+spanId[1]+"','"+dimId+"')";
                a.innerHTML="a";
                a.className="ui-icon ui-icon-close";
                cell1.appendChild(a);
                cell2=row.insertCell(1);
                // cell2.style.backgroundColor="#79C9EC";
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

function showDate(action, el, pos)
{
    switch(action)
    {
        case "addDefaultTime":
        {
            alert("Date Is Set To Today's Date");
            //   var frameobj=document.getElementById("defaultDateDisp");
            //   alert('frameobj--->'+frameobj)
            //        var source="setDefaultTime.jsp";
            //        frameobj.src=source;
            //        $("#defaultDate").dialog('open');
            break;
        }
        case "addCustomTime":
        {
//            alert("id\t"+$(el).attr('id'))
            var dateType=$(el).attr('id')
            $("#customDate").dialog('open');
            $("#dateType").val(dateType)
            break;
        }

        case "excludeParamViewbys":
        {
//            alert('in excludeParamViewbys')
            var frameobj=document.getElementById("customDateDisp");
            var reportid=document.getElementById("REPORTID").value;
            var ExclParamconfirm=confirm("All Parameters Removed from Viewbys Except Time,Are you Sure");
            if(ExclParamconfirm==true){
//                $.ajax({
//                    url: 'reportTemplateAction.do?templateParam=excludeParamViewbys&REPORTID='+REPORTID,
//                    success: function(data){
//
//                        document.getElementById("cepExc").value=data;
//                        document.getElementById("cepExc3").value=data;
//                    }
//                });
                exclParamViewbyFlag=true;
            }
//            var source="DatePicker.jsp?reportid="+reportid;
//            frameobj.src=source;
//            $("#customDate").dialog('open');
//            break;
        }
    }
}


function showAggregate(action, el, pos)
{
    switch(action)
    {
        case "addDefaultAggregation":
        {
            break;
        }
        case "addCustomAggregation":
        {
            var frameobj=document.getElementById("customAggregationDisp");
            var reportid=document.getElementById("REPORTID").value;
            var selecteddata="";
            $.ajax({
                url:'reportTemplateAction.do?templateParam=setAggregation&reportid='+reportid,
                success: function(data){
                    //alert('data-->'+data);
                    // selecteddata=data;
                    document.getElementById("selecteddata").value = data

                //                        if(data==1)
                //                            parent.$("#customDate").dialog('close');
                }
            });
            //        alert("reportid--->"+reportid);
            //        alert('frameobj--->'+frameobj)

            selecteddata =  document.getElementById("selecteddata").value;
            //alert("selecteddata--->"+selecteddata)
            var source="setAggregation.jsp?reportid="+reportid+"&selecteddata="+selecteddata;
            frameobj.src=source;
            $("#customAggregation").dialog('open');
            break;
        }

    }
}

function deleteParam(index,dimId){
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
        buildParams(dimId);
    }
    else{
        alert("You can't delete parameter which is REP /CEP")
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
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2' || paramIds[i].id=='PRG_PERIOD_TYPE'  ){
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
        dispParametersDesigner();
    }
    else{
        alert("Please select Parameters")
    }


}
function dispParameters(params){
    // var showTR=document.getElementById("lovParams");
    var timeparams=getBuildedTimeParams();
    var paramDisp=document.getElementById("paramDisp");
    document.getElementById("favParams").style.display='none';
    paramDisp.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=dispParameters&params='+params+'&timeparams='+timeparams+'&REPORTID='+document.getElementById("REPORTID").value,
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
    document.getElementById("favParams").style.display='';

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
//code to create measure li in Table Paramaters.jsp
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
//code to delete measure li in Table Paramaters.jsp
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
    //getTableColumns();
}

function buildParams(dimId){
    var params=getBuildedParams();
    var timeparams=getBuildedTimeParams();
    var paramNames=getBuildedParamNames();
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildParams&dimId='+dimId+'&params='+params+'&timeparams='+timeparams+'&foldersIds='+buildFldIds()+'&paramNames='+paramNames+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {
        }
    });
}
function showRowParams(){
    var params="";
    //var REPNames="";
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
                }else if(paramIds[j].id=='AS_OF_DMONTH1' || paramIds[j].id=='AS_OF_DMONTH2' || paramIds[j].id=='CMP_AS_OF_DMONTH1' || paramIds[j].id=='CMP_AS_OF_DMONTH2'){
                }else if(paramIds[j].id=='AS_OF_DQUARTER1' || paramIds[j].id=='AS_OF_DQUARTER2' || paramIds[j].id=='CMP_AS_OF_DQUARTER1' || paramIds[j].id=='CMP_AS_OF_DQUARTER2'){
                }else if(paramIds[j].id=='AS_OF_DYEAR1' || paramIds[j].id=='AS_OF_DYEAR2' || paramIds[j].id=='CMP_AS_OF_DYEAR1' || paramIds[j].id=='CMP_AS_OF_DYEAR2'){
                }else if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_DAY_ROLLING'){
                }else if(paramIds[j].id=='AS_OF_WEEK' || paramIds[j].id=='AS_OF_WEEK1'){
                }else if(paramIds[j].id=='AS_OF_MONTH' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
                }else if(paramIds[j].id=='AS_OF_MONTH1' || paramIds[j].id=='AS_OF_MONTH2'){
                }else if(paramIds[j].id=='AS_OF_QUARTER' || paramIds[j].id=='AS_OF_QUARTER1'){
                }else if(paramIds[j].id=='AS_OF_YEAR' || paramIds[j].id=='AS_OF_YEAR1'){
                }else {
                    var row=divTable.insertRow(k);
                    var cell1=row.insertCell(0);
                    var divinput=document.createElement("input");
                    divinput.type='checkbox';
                    divinput.name='rowParamId';
                    divinput.id='rowParamId'+j;
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
            if(timeDimArray.length!=0){
                row=divTable.insertRow(0);
                cell1=row.insertCell(0);
                divinput=document.createElement("input");
                divinput.type='checkbox';
                divinput.name='rowParamId';
                divinput.id='rowParamId-Time';
                pIds=(divinput.id).split("-");
                if(prevRowParams.search(pIds[1])!=-1){
                    divinput.checked="true";
                }
                divinput.value='Time';
                cell1.appendChild(divinput);
                cell2=row.insertCell(0);
                cell2.id='rowParam-Time';
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
function getRowEdgeParams(){

    var rowEdgeParams="";
    var REPNames="";

    //var rowParamIdObj=document.getElementsByName("rowParamId");
    var rowParamIdObj= document.myForm2.rowParamId;

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
        url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=REP&rowEdgeParams='+rowEdgeParams+'&rowEdgeParamsNames='+REPNames+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
        }
    });
}
function hideRowParams(){
    var tabDiv=document.getElementById("repDiv");
    tabColDiv.style.display='none';
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
                }else if(paramIds[j].id=='AS_OF_DMONTH1' || paramIds[j].id=='AS_OF_DMONTH2' || paramIds[j].id=='CMP_AS_OF_DMONTH1' || paramIds[j].id=='CMP_AS_OF_DMONTH2'){
                }else if(paramIds[j].id=='AS_OF_DQUARTER1' || paramIds[j].id=='AS_OF_DQUARTER2' || paramIds[j].id=='CMP_AS_OF_DQUARTER1' || paramIds[j].id=='CMP_AS_OF_DQUARTER2'){
                }else if(paramIds[j].id=='AS_OF_DYEAR1' || paramIds[j].id=='AS_OF_DYEAR2' || paramIds[j].id=='CMP_AS_OF_DYEAR1' || paramIds[j].id=='CMP_AS_OF_DYEAR2'){
                }else if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_DAY_ROLLING'){
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
                    pIds=(paramIds[j].id).split("-");

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
                pIds=(divColinput.id).split("-");
                if(prevColParams.search(pIds[1])!=-1){
                    divColinput.checked="true";
                }
                divColinput.value='TIME';
                colCell1.appendChild(divColinput);
                colCell2=colRow.insertCell(0);
                colCell2.id='colParam-Time';
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
function getColumnEdgeParams(){
    var colEdgeParams="";
    var CEPNames="";

    //var colParamIdObj=document.getElementsByName("colParamId");
    var colParamIdObj=document.myForm2.colParamId;
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


//    if(chkCount==1 || chkCount==0){
        $.ajax({
            url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=CEP&colEdgeParams='+colEdgeParams+'&colEdgeParamsNames='+CEPNames+'&REPORTID='+document.getElementById("REPORTID").value,
            success: function(data){
            }
        });
//    }
//    else{
//        alert("Please Select only one Column Edge");

//    }
//}
}
function hideColParams(){
    var tabColDiv=document.getElementById("cepDiv");
    tabColDiv.style.display='none';
}
function showMeasures(){    
    var prevREPIds=document.getElementById("REPIds").value
    var frameObj=document.getElementById("dataDispmem");
    var prevCEPIds=document.getElementById("CEPIds").value
    var RepIdsArray = new Array()
    var CepIdsArray = new Array()
   RepIdsArray = prevREPIds.split(",")
   CepIdsArray = prevCEPIds.split(",")
   var flag = 0;
   for(var i=0;i<RepIdsArray.length;i++){
      for(var j=0;j<CepIdsArray.length;j++){
          if(RepIdsArray[i] == CepIdsArray[j]){
             flag=flag+1;
          }
      }
   }
    if(prevREPIds!=""){
        if(flag==0){
        $("#measuresDialog").dialog('open');
        var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+parent.buildFldIds()+'&REPORTID='+document.getElementById("REPORTID").value;
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
        }else{
            alert("Please select different Row Edge and Col Edge")
        }

    }
    else{
        if(prevREPIds=="" || prevREPIds==undefined ){
            alert("Please select Row Edge ")
        }
    }
}
function dispParametersDesigner(){
    var params=getBuildedParams();
    var paramNames=getBuildedParamNames();
    var timeparams=getBuildedTimeParams();
    var showTR=document.getElementById("lovParams");
    showTR.style.display = '';
    var paramDisp=document.getElementById("paramDisp");
    document.getElementById("favParams").style.display='none';

    paramDisp.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=dispParams&params='+params+'&paramNames='+paramNames+'&timeparams='+timeparams+'&REPORTID='+document.getElementById("REPORTID").value,
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
        document.getElementById("saveFavParams").disabled=false;
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
            paramNames=paramNames+","+tdObj[1].innerHTML;
        }
    }

    if(paramNames!="" && pIds.length!=0){
        paramNames=paramNames.substr(1);
    }
    return paramNames;
//return paramIdArray;
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

        document.getElementById("paramDisp").innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

        $.ajax({
            url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+msrNameOnly+'&REPORTID='+parent.document.getElementById("REPORTID").value,
            success: function(data) {
                if(data!=""){
                    document.getElementById("paramDisp").innerHTML=data;
                }
                else{
                    document.getElementById("paramDisp").innerHTML="";
                }
            }
        });
    }
    //
    parent.cancelTabMeasure();
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

function showGraphs(){
//    alert("in showGraphs() in reportDesign.js")
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    if(paramIds.length!=0){
        if(graphFlag == true){
            var grpIdsStrArray=graphIds.toString().split(",");
            if(grpIdsStrArray.length<2){
                if ($.browser.msie == true){
                    $("#graphList").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 800,
                        modal: true
                    });
                }else{
                    $("#graphList").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 800,
                        modal: true
                    });
                }
                $("#graphList").dialog('open');
            }else{
                alert("You Can Select Maximum of  Two Graphs")
            }
        }else{
            alert("You Can Add Either Graphs or Graph With Table")
        }
    }
    else{
        alert("Please select Parameters")
    }
}

function getGraphName(name,typeValue){
   $("#graphList").dialog('close');
    graphCount++;
    graphIds = graphIds+","+graphCount;
    if(graphIds!=""){
        document.getElementById("allGraphIds").value=graphIds.substring(1);
    }
    else{
        document.getElementById("allGraphIds").value=graphIds;
    }
    graphIds=graphIds.substring(1);

    document.getElementById("allGraphIds").value=graphIds;
    parentDiv = document.getElementById('previewDispGraph');
    parentDiv.style.height = '250'
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphCount+'&grptypid='+typeValue+'&graphChange=default&grpIds='+graphIds.substring(1)+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            parentDiv.innerHTML=data;
            parentDiv.style.height = 'auto'
        }
    });

    var previewGraphObj=document.getElementById("previewGraph");
    previewGraphObj.style.display='';
    $("#graphList").dialog('close');
    document.getElementById("allGraphIds").value=graphIds;
    graphTableFlag = false
    graphFlagCount++;
    document.getElementById("graphTableHidden").value = "GM"
}
//added by santhosh.k on 09-03-2010
function getGraphName(name,typeValue,currGrpId){
    name1 = name;
    typeValue1 = typeValue;
    currGrpId1 = currGrpId;
//    alert("in getGraphName in reportDesign.js")
    $("#graphList").dialog('close');
    if(graphIds!=''){
        if(graphIds==currGrpId){
            currGrpId=currGrpId+1;
        }
        graphIds=graphIds+","+currGrpId
    }else{
        graphIds=currGrpId;
    }
    document.getElementById("allGraphIds").value=graphIds;
    parentDiv =parent.document.getElementById('previewDispGraph');
    document.getElementById("previewDispGraph").style.height = '250';
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
//    $.ajax({
//        url: 'reportTemplateAction.do?templateParam=dispTable&REPORTID='+document.getElementById("REPORTID").value+'&action=design',
//        success: function(data){
//
//        }
//    });


    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+currGrpId+'&grptypid='+typeValue+'&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            parentDiv.innerHTML=data;
            document.getElementById("previewDispGraph").style.height = 'auto';
        }
    });

    var previewGraphObj=document.getElementById("previewGraph");
    previewGraphObj.style.display='';
    $("#graphList").dialog('close');
    document.getElementById("allGraphIds").value=graphIds;
    graphTableFlag = false
    graphFlagCount++;
    document.getElementById("graphTableHidden").value = "GM"
}

function showGraphTable(){
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    if(paramIds.length!=0){
        if(graphTableFlag == true){
            var grpTableIdsStrArray= (graphIds.length > 0) ? graphIds.toString().split(",") : graphIds.toString();
            if(grpTableIdsStrArray.length<=1){
                if ($.browser.msie == true){
                    $("#graphTableList").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 800,
                        modal: true
                    });
                }else{
                    $("#graphTableList").dialog({
                        bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 800,
                        modal: true
                    });
                }
                $("#graphTableList").dialog('open');
            }else{
                alert("You Can Select Only One Graph With Table")
            }
        }else{
            alert("You Can Add Either Graphs or Graph With Table")
        }
    }
    else{
        alert("Please select Parameters")
    }
}

function getGraphTable(name,typeValue){
    $("#graphTableList").dialog('close');
    graphTableCount++;
    graphIds = graphIds+","+graphTableCount;
    if(graphIds!=""){
        document.getElementById("allGraphTableIds").value=graphIds.substring(1);
    }
    else{
        document.getElementById("allGraphTableIds").value=graphIds;
    }
    graphIds=graphIds.substring(1);

    document.getElementById("allGraphTableIds").value=graphIds;
    var parentDiv = document.getElementById('previewDispGraph');
    parentDiv.style.height = '250'
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphTableCount+'&grptypid='+typeValue+'&graphChange=default&grpIds='+graphIds.substring(1)+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GTM',
        success: function(data){
            parentDiv.innerHTML=data;
            parentDiv.style.height = 'auto'
        }
    });

    var previewGraphObj=document.getElementById("previewGraph");
    previewGraphObj.style.display='';
    $("#graphTableList").dialog('close');
    document.getElementById("allGraphTableIds").value=graphIds;
    graphFlag = false
    document.getElementById("graphTableHidden").value = "GTM"
}
//added for selecting Graph table in RD
function getGraphTable(name,typeValue,currGrpId){
    $("#graphTableList").dialog('close');
    if(graphIds!=''){
        if(graphIds==currGrpId){
            currGrpId=currGrpId+1;
        }
        graphIds=graphIds+","+currGrpId
    }else{
        graphIds=currGrpId;
    }
    document.getElementById("allGraphTableIds").value=graphIds;
    var parentDiv = document.getElementById('previewDispGraph');
    document.getElementById("previewDispGraph").style.height = '250';
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';


    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+currGrpId+'&grptypid='+typeValue+'&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GTM',
        success: function(data){
            parentDiv.innerHTML=data;
            document.getElementById("previewDispGraph").style.height = 'auto';
        }
    });

    var previewGraphObj=document.getElementById("previewGraph");
    previewGraphObj.style.display='';
    $("#graphTableList").dialog('close');
    document.getElementById("allGraphTableIds").value=graphIds;
    graphFlag = false
    document.getElementById("graphTableHidden").value = "GTM"
}

function showGraphsList()
{
    if(document.getElementById('grpList').style.display=='none')
        document.getElementById('grpList').style.display='';
    else
        document.getElementById('grpList').style.display='none';
}
function getColumns(currentGraphId,allGraphIds){
    var frameObj=document.getElementById("graphColsFrame");
    var source = "pbgraphColumns.jsp?graphId="+currentGraphId+"&folderIds="+buildFldIds()+"&grpIds="+allGraphIds+'&REPORTID='+document.getElementById("REPORTID").value;
    frameObj.src=source;
    //  frameObj.style.display='block';
    //  document.getElementById('fade').style.display='block';
    $("#graphCols").dialog('open');
}

function getGrpTableColumns(currentGraphId,allGraphIds){
    var frameObj=document.getElementById("graphColsFrame");
    var source = "pbgraphColumns.jsp?graphId="+currentGraphId+"&folderIds="+buildFldIds()+"&grpIds="+allGraphIds+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GTM';
    frameObj.src=source;
    //  frameObj.style.display='block';
    //  document.getElementById('fade').style.display='block';
    $("#graphCols").dialog('open');
}

function currGraph(val)
{//
    currentGraphId = val.id;
}

function cancelTabMeasure(){
    $("#measuresDialog").dialog('close');
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
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=getUserDims&foldersIds='+foldersIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {
            if(data!=""){
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

                $('#favourParams > li >span').draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(draggableDims).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#userDims > li >span,#userBuckets > li >span,#favourParams> li >span',
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
                        } else if(dim=='Time-Rolling Period'){
                            timeDim='AS_OF_DATE,PRG_DAY_ROLLING';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Month Range Basis'){
                            timeDim='AS_OF_DMONTH1,AS_OF_DMONTH2,CMP_AS_OF_DMONTH1,CMP_AS_OF_DMONTH2';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Quarter Range Basis'){
                            timeDim='AS_OF_DQUARTER1,AS_OF_DQUARTER2,CMP_AS_OF_DQUARTER1,CMP_AS_OF_DQUARTER2';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,ui.draggable.html());
                        }else if(dim=='Time-Year Range Basis'){
                            timeDim='AS_OF_DYEAR1,AS_OF_DYEAR2,CMP_AS_OF_DYEAR1,CMP_AS_OF_DYEAR2';
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
                        } else if(dim=='Time-Cohort Basis')
                        {
                            timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2,PRG_PERIOD_TYPE';
                            timeDimension=timeDim.split(",");
                            createParams(timeDimension,dim);
                        }else {
                            getDimDetails(ui.draggable.html());
                        }
                    }
                });
            }
        }
    });
}
function getBuckets(){
    var foldersIds=buildFldIds();
    var branches1="";
    var str;
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=getBuckets&foldersIds='+foldersIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {

            if(data!=""){
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

        }
    });
//}
}
function getFavParams(){
    var foldersIds=buildFldIds();
    var branches1="";
    var str;

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=getFavParams&foldersIds='+foldersIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {

            if(data!=""){
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
        }
    });
}

/* removed fiunctions from here*/
function cancelCols()
{
    // document.getElementById('fade').style.display='none';
    //  document.getElementById('graphCols').style.display='none';
    $("#graphCols").dialog('close');
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
                   var From = "";
                   var isKpiDashboard=""
                if(parent.document.getElementById("Designer")!= null){
                    From=parent.document.getElementById("Designer").value;
                }
   // alert("in PreviewTable in reportDesign.js")
    var prevREPIds=document.getElementById("REPIds").value;
    var prevCEPIds=document.getElementById("CEPIds").value;
    var aoFlag=document.getElementById("AOFlag").value;
    if(document.getElementById("isKpiDashboard")!=null){
      isKpiDashboard=document.getElementById("isKpiDashboard").value;
    }
    var prevMsrs=document.getElementById("Measures").value;
    var editTable=document.getElementById("editTable");
    if(prevREPIds!="" && prevMsrs.length!=""){
        if(prevCEPIds!=""){
//            if(prevCEPIds.split(",").length==1){
                editTable.style.display='none';
                if(From != null && From=="fromDesigner"){
                    dispTable("fromDesigner");
                }else
                dispTable("design");
//            }
//            else{
//                alert("Please Select only one Column Edge")
//            }
        }else{
            if(editTable!=null){
            editTable.style.display='none';
            }
            if(From != null && From=="fromDesigner"){
                if(isKpiDashboard!="true"){
                    dispTable("fromDesigner");
                }else{
                    dispTableKPIDashboard("fromDesigner");
                }
                }else
            dispTable("design");
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
function  dispTable(action){
    var previewDispTableDiv;
    var previewTable=document.getElementById("previewTable");
    var aoflag=document.getElementById('AOFlag').value;
   // alert("vfrgfjhghjgb---"+aoflag);
    previewTable.style.display = '';
    //previewDispTableDiv=document.getElementById("previewDispTable");

    //document.getElementById("previewDispTableImg").style.visibility="visible";
    //document.getElementById("previewDispTableImg").style.display="";
    //previewDispTableDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $('#iframe1').contents().find('body').append('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>');


    $.ajax({
       async:false,
       url: 'reportTemplateAction.do?templateParam=dispTable'+'&REPORTID='+document.getElementById("REPORTID").value+'&action='+action+'&aoflag='+aoflag,
        success: function(data){
            //
            if(data!=""){
                //previewDispTableDiv.innerHTML=data.split("@!")[0];
                sqlStr=data.split("@!")[1];
                var source = "TableDisplay/pbDisplay.jsp?source=L&tabId="+document.getElementById("REPORTID").value;
                var dSrc = document.getElementById("iframe1");
                dSrc.src = source;
//                createReport();
//                alert("Report Created Successfully. If you want to add a Graph add it from 'Add Graph' option in Actions")
            }
            else{
                //previewDispTableDiv.innerHTML="";
            }
        /*
             $("#tablesorter").columnFilters().tablesorter({
                widthFixed: true,
                widgets: ['zebra']
            });
            */
        }
    });
//    saveRepotInDesignerAO();
//     setTimeout( saveRepotInDesignerAO(),1000);

//    alert("dispTable");
}
//Added by Amar on dec 2, 2015
//function  dispAO(action,isFromAOEdit){
//    //alert("dinanath edit ao "+isFromAOEdit);
//    var previewDispTableDiv;
//    var previewTable=document.getElementById("previewTable");
//    previewTable.style.display = '';
//    $('#iframe1').contents().find('body').append('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>');
            //
//    $.ajax({
//        async:false,
//       url: 'reportTemplateAction.do?templateParam=dispAO'+'&REPORTID='+document.getElementById("REPORTID").value+'&action='+action+'&isFromAOEdit='+isFromAOEdit,
//        success: function(data){
//            //
//            if(data!=""){
////alert("home.jsp#AO_Builder");
// $.ajax({
//        async:false,
//       url: 'reportTemplateAction.do?templateParam=removeSessionAttributes',
//        success: function(data){
//        }
//    });
//parent.goPaths('home.jsp#AO_Builder');
//            }
//            else{
//
//            }
//        }
//    });
//}
function dispTableKPIDashboard1(action){
    var iskpi = parent.isKPIDashboard1;
    if(iskpi=='true'){
         dispTableKPIDashboard(action)
    }

}
function  dispTableKPIDashboard(action){

    var previewDispTableDiv;
    var previewTable=document.getElementById("previewTable");
    previewTable.style.display = '';
     $('#iframe1').contents().find('body').append('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>');
   var divIdObj=document.getElementById("KpiDashboardDiv");
          divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

   $.ajax({
        url: 'reportTemplateAction.do?templateParam=dispTable'+'&REPORTID='+document.getElementById("REPORTID").value+'&action='+action+'&isKpiDashboard=true',
        success: function(data){
            if(data!=""){
                sqlStr=data.split("@!")[1];
//                 var divIdObj=document.getElementById("KpiDashboardDiv");
//          divIdObj.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

        $.ajax({
            url: 'reportViewer.do?reportBy=buildKpiDashboard&reportId='+parent.document.getElementById("REPORTID").value,
               success: function(data){


            divIdObj.innerHTML =data;
          }
        });

            }
            else{
                //previewDispTableDiv.innerHTML="";
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
    var graphTableHidden = document.getElementById("graphTableHidden").value
    var parentDiv = document.getElementById('previewDispGraph');
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    if(paramIds.length!=0){
        var previewGraphObj=document.getElementById("previewGraph");
        previewGraphObj.style.display='';
        document.getElementById("previewDispGraph").style.height = '250';
        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        if(graphTableHidden == "GTM"){
            $.ajax({
                url: 'reportTemplateAction.do?templateParam=refreshGraphs'+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GTM',
                success: function(data){
                    parentDiv.innerHTML=data;
                    document.getElementById("previewDispGraph").style.height = 'auto';
                }
            });
        }else{
            $.ajax({
                url: 'reportTemplateAction.do?templateParam=refreshGraphs'+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GM',
                success: function(data){
                    parentDiv.innerHTML=data;
                    document.getElementById("previewDispGraph").style.height = 'auto';
                }
            });
        }

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

function graphTableColumnsDisp(grpId,allGrpIds){
    getGrpTableColumns(grpId,allGrpIds);
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

function graphTableTypesDisp(dispgrptypObj){
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
}

function changeGrpType(grpTypeId,grpId,graphIds){
    var dispgrptypObj=document.getElementById("dispgrptypes"+grpId);
    dispgrptypObj.style.display='none';
    var parentDiv = document.getElementById('previewDispGraph');
    document.getElementById("previewDispGraph").style.height = '250';
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+grpId+'&grptypid='+grpTypeId+'&graphChange=GrpType&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            parentDiv.innerHTML=data;
            document.getElementById("previewDispGraph").style.height = 'auto';
        }
    });
}


function changeGrpTableType(grpTypeId,grpId,graphIds){
    var dispgrptypObj=document.getElementById("dispgrptypes"+grpId);
    dispgrptypObj.style.display='none';
    var parentDiv = document.getElementById('previewDispGraph');
    document.getElementById("previewDispGraph").style.height = '250';
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+grpId+'&grptypid='+grpTypeId+'&graphChange=GrpType&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GTM',
        success: function(data){
            parentDiv.innerHTML=data;
            document.getElementById("previewDispGraph").style.height = 'auto';
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

function graphTableSizesDisp(dispgrpsizesObj){
    if(dispgrpsizesObj.style.display=='none'){
        dispgrpsizesObj.style.display='';
    }
    else{
        dispgrpsizesObj.style.display='none';
    }
}

function changeGrpSize(grpSizeId,grpId,graphIds){
    var parentDiv = document.getElementById('previewDispGraph');
    document.getElementById("previewDispGraph").style.height = '250';
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+grpId+'&grpsizeid='+grpSizeId+'&graphChange=GrpSize&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            parentDiv.innerHTML=data;
            document.getElementById("previewDispGraph").style.height = 'auto';
        }
    });

}

function changeGrpTableSize(grpSizeId,grpId,graphIds){
    var parentDiv = document.getElementById('previewDispGraph');
    document.getElementById("previewDispGraph").style.height = '250';
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+grpId+'&grpsizeid='+grpSizeId+'&graphChange=GrpSize&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GTM',
        success: function(data){
            parentDiv.innerHTML=data;
            document.getElementById("previewDispGraph").style.height = 'auto';
        }
    });

}

function deleteGraph(graphId,GraphIds){
    var parentDiv = document.getElementById('previewDispGraph');
    var res=confirm("Are you sure you want to delete the graph");
    var grpIdsStrArray;
    if(res){
        document.getElementById("previewDispGraph").style.height = '250';
        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        $.ajax({
            url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&grpIds='+GraphIds+'&graphChange=DeleteGraph'+'&REPORTID='+document.getElementById("REPORTID").value,
            success: function(data){
                parentDiv.innerHTML=data;
                document.getElementById("previewDispGraph").style.height = 'auto';
            }
        });
        var temp=graphIds;
        var temp2="";
        grpIdsStrArray=graphIds.toString().split(",");
        for(var i=0;i<grpIdsStrArray.length;i++){
            if(grpIdsStrArray[i]!=graphId){
                temp2=temp2+","+grpIdsStrArray[i];
            }
        }
        if(temp2!=""){
            temp2=temp2.substring(1,temp2.length);
            graphIds=temp2;
        }else{
            graphIds=temp2;
        }
        document.getElementById("allGraphIds").value=graphIds;
        if(graphFlagCount<=1){
            graphFlag = true;
            graphTableFlag = true;
        }
        graphFlagCount--;
    }
}

function deleteGraphTable(graphId,GraphIds){
    var parentDiv = document.getElementById('previewDispGraph');
    var res=confirm("Are you sure you want to delete the graph");
    var grpIdsStrArray;
    if(res){
        document.getElementById("previewDispGraph").style.height = '250';
        parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        $.ajax({
            url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+graphId+'&grpIds='+GraphIds+'&graphChange=DeleteGraph'+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GTM',
            success: function(data){
                parentDiv.innerHTML=data;
                document.getElementById("previewDispGraph").style.height = 'auto';
            }
        });
        var temp=graphIds;
        var temp2="";
        grpIdsStrArray=graphIds.toString().split(",");
        for(var i=0;i<grpIdsStrArray.length;i++){
            if(grpIdsStrArray[i]!=graphId){
                temp2=temp2+","+grpIdsStrArray[i];
            }
        }
        if(temp2!=""){
            temp2=temp2.substring(1,temp2.length);
            graphIds=temp2;
        }else{
            graphIds=temp2;
        }
        document.getElementById("allGraphTableIds").value=graphIds;
        graphFlag = true;
        graphTableFlag = true;
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

function dispGraphTableDetails(currentGraphId,allGraphIds){
    getGraphTableDetails(currentGraphId,allGraphIds);

}


function getGraphDetails(currentGraphId,allGraphIds){

    if ($.browser.msie == true){
        $("#graphDtlsDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 430,
            width: 800,
            modal: true
        });
    }
    else{
        $("#graphDtlsDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 280,
            width: 800,
            modal: true
        });
    }


    $("#graphDtlsDiv").dialog('open');
    var frameObj=document.getElementById("graphDtls");
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    var source = "PbGraphDetails.jsp?graphId="+currentGraphId+"&grpIds="+allGraphIds+'&REPORTID='+document.getElementById("REPORTID").value;
    frameObj.src=source;
}

function getGraphTableDetails(currentGraphId,allGraphIds){

    if ($.browser.msie == true){
        $("#graphDtlsDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 800,
            modal: true
        });
    }
    else{
        $("#graphDtlsDiv").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 250,
            width: 800,
            modal: true
        });
    }


    $("#graphDtlsDiv").dialog('open');
    var frameObj=document.getElementById("graphDtls");
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    var source = "PbGraphDetails.jsp?graphId="+currentGraphId+"&grpIds="+allGraphIds+'&REPORTID='+document.getElementById("REPORTID").value+'&graphTableMethod=GTM';
    frameObj.src=source;
}


function cancelGraphDetails(){
    //var frameObj=document.getElementById("graphDtls");
    //frameObj.style.display='none';
    //document.getElementById('fade').style.display='none';
    parent.$("#graphDtlsDiv").dialog('close');
}
function saveGraphDetails(currentGraphId,allGraphIds){
}
function validateTP(){
    var prevREPIds=document.getElementById("REPIds").value;
    var prevCEPIds=document.getElementById("CEPIds").value;
    var prevMsrs=document.getElementById("Measures").value;

    if(prevREPIds=="" || prevREPIds==undefined){
        alert("Please select Row Edge");
        return false;
    }else if(prevMsrs=="" || prevMsrs==undefined){
        alert("Please select Measures");
        return false;
    }else{
        return true;
    }
}
//added by bharathi on 16-12-2009 fro retreiving custom measures

function getCustomMeasures(){
    var foldersIds=buildFldIds();
    var branches1="";
    var str;
    // if(foldersIds!=""){
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=getCustomMeasures&foldersIds='+foldersIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data) {
            $("#customMeasure").html("");
            str=data;
            branches1 = $(str).appendTo("#customMeasure");
            $("#customMeasure").treeview({
                add: branches1
            });
        }
    });
//}
}
//added by santhosh.kumar@progenbusiness.com on 24/12/2009 fro adding row values
function dispRowValues(rowValuesDiv,rowValues,grpId,grpIds){
    var rowValuesDivObj=document.getElementById(rowValuesDiv);
    if(rowValuesDivObj!=null && rowValuesDivObj!=undefined){
        if(rowValuesDivObj.style.display=="none"){
            rowValuesDivObj.style.display="";
        }else{
            var rowVals=new Array();
            var temp="";

            var rowsDispObj=document.getElementsByName(rowValues);
            for(var i=0;i<rowsDispObj.length;i++){
                if(rowsDispObj[i].checked){
                    temp=rowsDispObj[i].value;
                    temp=temp.replace("&","^");//'&' is  replaced with '^'
                    temp=temp.replace(",","~");//',' is  replaced with '~'
                    rowVals.push(temp);
                }
            }
            rowValuesDivObj.style.display="none";
            var parentDiv = document.getElementById('previewDispGraph');
            $.ajax({
                url: "reportTemplateAction.do?templateParam=buildGraphs&graphChange=RowValues&REPORTID="+document.getElementById("REPORTID").value+"&rowValues="+rowVals.toString()+"&gid="+grpId+"&grpIds="+grpIds,
                success: function(data){
                    parentDiv.innerHTML=data;
                }
            });
        }
    }

}

function dispRowTableValues(rowValuesDiv,rowValues,grpId,grpIds){
    var rowValuesDivObj=document.getElementById(rowValuesDiv);
    if(rowValuesDivObj!=null && rowValuesDivObj!=undefined){
        if(rowValuesDivObj.style.display=="none"){
            rowValuesDivObj.style.display="";
        }else{
            var rowVals=new Array();
            var temp="";

            var rowsDispObj=document.getElementsByName(rowValues);
            for(var i=0;i<rowsDispObj.length;i++){
                if(rowsDispObj[i].checked){
                    temp=rowsDispObj[i].value;
                    temp=temp.replace("&","^");//'&' is  replaced with '^'
                    temp=temp.replace(",","~");//',' is  replaced with '~'
                    rowVals.push(temp);
                }
            }
            rowValuesDivObj.style.display="none";
            var parentDiv = document.getElementById('previewDispGraph');
            $.ajax({
                url: "reportTemplateAction.do?templateParam=buildGraphs&graphChange=RowValues&REPORTID="+document.getElementById("REPORTID").value+"&rowValues="+rowVals.toString()+"&gid="+grpId+"&grpIds="+grpIds+'&graphTableMethod=GTM',
                success: function(data){
                    parentDiv.innerHTML=data;
                }
            });
        }
    }

}

/*
function dispRowValues(currentGraphId,allGraphIds){
    var frameObj=document.getElementById("dispRowValues");
    var source = "pbgraphColumns.jsp?graphId="+currentGraphId+"&folderIds="+buildFldIds()+"&grpIds="+allGraphIds+'&REPORTID='+document.getElementById("REPORTID").value;
    frameObj.src=source;
    frameObj.style.display='block';
    document.getElementById('fade').style.display='block';
        }
*/
//addded by Mahesh for save Favourite Parameters

function saveFavouriteParams(){
    $("#favouriteParamsDialog").dialog('open');

//    document.getElementById("favouriteParams").style.display='block';
//    document.getElementById("fade").style.display='block';

/*document.forms.myForm2.getElementById('favParams').style.display='block';
    document.forms.myForm2.getElementById('fade').style.display='block';
    document.forms.myForm2.getElementById('mainBody').style.overflow='hidden';
    document.forms.myForm2.action="pbFavParameters.jsp";
    document.forms.myForm2.submit();*/

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
//return paramswithtime;
//return paramIdArray;
}
//added for timedimension
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
        }else if(paramIds[i].id=='AS_OF_DATE1' || paramIds[i].id=='AS_OF_DATE2' || paramIds[i].id=='CMP_AS_OF_DATE1' || paramIds[i].id=='CMP_AS_OF_DATE2' || paramIds[i].id=='PRG_PERIOD_TYPE' ){
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


    return paramswithtime;
}
function showTableProperties(){
    if(validateTP()){
        if ($.browser.msie == true){
            $("#dispTabProp").dialog({
                bgiframe: true,
                autoOpen: false,
                height: 400,
                width: 900,
                modal: true
            });
        }else{
            $("#dispTabProp").dialog({
                bgiframe: true,
                autoOpen: false,
                height: 260,
                width: 900,
                modal: true
            });
        }

        $("#dispTabProp").dialog('open');
        var frameObj=document.getElementById("dispTabPropFrame");
        var source ='PbTableProperties.jsp?REPORTID='+document.getElementById("REPORTID").value
        frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
    }
}
function cancelTableProperties(){
    $("#dispTabProp").dialog('close');
}
function showRowParams1(){
    var data=document.getElementById("repExc").value;
    var params="";
    var pIds="";
    var k=0;
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    var exreplist="";
    if(data!=""){
        exreplist=data.split(",");
    }

    if(paramIds.length!=0){
        for(var i=0;i<paramIds.length;i++){
            pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
        if(params!=""){
            params=params.substr(1);
        }

        var tabColDiv=document.getElementById("repDiv");
        if(tabColDiv.style.display=='none'){
            tabColDiv.style.display='block';

            var divColTable=document.createElement("table");
            var prevRowParams= document.getElementById("REPIds").value;
            var prevRowParamsStr=prevRowParams.split(",");



            for(var j=0;j<paramIds.length;j++){
                if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
                }else if(paramIds[j].id=='AS_OF_DATE1' || paramIds[j].id=='AS_OF_DATE2' || paramIds[j].id=='CMP_AS_OF_DATE1' || paramIds[j].id=='CMP_AS_OF_DATE2'){
                }else if(paramIds[j].id=='AS_OF_DMONTH1' || paramIds[j].id=='AS_OF_DMONTH2' || paramIds[j].id=='CMP_AS_OF_DMONTH1' || paramIds[j].id=='CMP_AS_OF_DMONTH2'){
                }else if(paramIds[j].id=='AS_OF_DQUARTER1' || paramIds[j].id=='AS_OF_DQUARTER2' || paramIds[j].id=='CMP_AS_OF_DQUARTER1' || paramIds[j].id=='CMP_AS_OF_DQUARTER2'){
                }else if(paramIds[j].id=='AS_OF_DYEAR1' || paramIds[j].id=='AS_OF_DYEAR2' || paramIds[j].id=='CMP_AS_OF_DYEAR1' || paramIds[j].id=='CMP_AS_OF_DYEAR2'){
                }else if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_DAY_ROLLING'){
                }else if(paramIds[j].id=='AS_OF_WEEK' || paramIds[j].id=='AS_OF_WEEK1'){
                }else if(paramIds[j].id=='AS_OF_MONTH' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
                }else if(paramIds[j].id=='AS_OF_MONTH1' || paramIds[j].id=='AS_OF_MONTH2'){
                }else if(paramIds[j].id=='AS_OF_QUARTER' || paramIds[j].id=='AS_OF_QUARTER1'){
                }else if(paramIds[j].id=='AS_OF_YEAR' || paramIds[j].id=='AS_OF_YEAR1'){
                }else {
                    pIds=(paramIds[j].id).split("-");
                    if(exreplist!=""){
                        var count=0;
                        for(var j1=0;j1<exreplist.length;j1++){
                            if(pIds[1]==exreplist[j1]){
                                count++;
                            }
                        }

                        if(!(count>0)){
                            var colRow=divColTable.insertRow(k);
                            var colCell1=colRow.insertCell(0);
                            var divColinput=document.createElement("input");
                            divColinput.type='checkbox';
                            divColinput.name='rowParamId';
                            divColinput.id='rowParamId';
                            //divColinput.id='rowParamId'+j;

                            //                            if(prevRowParams.search(pIds[1])!=-1){
                            //                                divColinput.checked="true";
                            //                            }



                            divColinput.value=pIds[1];
                            colCell1.appendChild(divColinput);
                            for(var cnt=0;cnt<prevRowParamsStr.length;cnt++){
                                if(prevRowParamsStr[cnt]==pIds[1]){
                                    divColinput.checked="true";


                                }
                            }

                            var colCell2=colRow.insertCell(0);
                            colCell2.name='rowParam';
                            //colCell2.id='rowParam'+j;
                            colCell2.id='rowParam';
                            var paramName=paramIds[j].getElementsByTagName("td");
                            colCell2.innerHTML=paramName[1].innerHTML;
                            k++;
                        }
                    }else{
                        var colRow=divColTable.insertRow(k);
                        var colCell1=colRow.insertCell(0);
                        var divColinput=document.createElement("input");
                        divColinput.type='checkbox';
                        divColinput.name='rowParamId';
                        divColinput.id='rowParamId';
                        //divColinput.id='rowParamId'+j;
                        pIds=(paramIds[j].id).split("-");



                        //                        if(prevRowParams.search(pIds[1])!=-1){
                        //                            divColinput.checked="true";
                        //                        }


                        divColinput.value=pIds[1];
                        colCell1.appendChild(divColinput);
                        for(var cnt=0;cnt<prevRowParamsStr.length;cnt++){
                            if(prevRowParamsStr[cnt]==pIds[1]){
                                divColinput.checked="true";

                            }
                        }

                        var colCell2=colRow.insertCell(0);
                        //colCell2.id='rowParam'+j;
                        colCell2.id='rowParam';
                        colCell2.name='rowParam';
                        var paramName=paramIds[j].getElementsByTagName("td");
                        colCell2.innerHTML=paramName[1].innerHTML;
                        k++;
                    }
                }
            }
            if(timeDimArray.length!=0){
                colRow=divColTable.insertRow(0);
                colCell1=colRow.insertCell(0);
                divColinput=document.createElement("input");
                divColinput.type='checkbox';
                divColinput.name='rowParamId';
                divColinput.id='rowParamId';
                //divColinput.id='rowParamId-Time';
                pIds=(divColinput.id).split("-");

                //if(prevRowParams.search(pIds[1])!=-1){
                //divColinput.checked="true";
                //}


                divColinput.value='TIME';
                colCell1.appendChild(divColinput);
                for(var cnt=0;cnt<prevRowParamsStr.length;cnt++){
                    if(prevRowParamsStr[cnt]==pIds[1]){
                        divColinput.checked="true";

                    }
                }

                colCell2=colRow.insertCell(0);
                //colCell2.id='rowParam-Time';
                colCell2.id='rowParam';
                colCell2.name='rowParam';
                colCell2.innerHTML='TIME';
                k++;
            }
            tabColDiv.innerHTML='';
            tabColDiv.appendChild(divColTable);
        }
        else{
            tabColDiv.style.display='none';
            getRowEdgeParams();
        }
    }
    else{
        alert("Please select Parameters")
    }
}

function showColParams1(){
    var data=document.getElementById("cepExc").value;
    var params="";
    var pIds="";
    var k=0;
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    var exreplist="";
    if(data!=""){
        exreplist=data.split(",");
    }

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
            var prevColParams= document.getElementById("CEPIds").value;
            var prevColParamsStr=prevColParams.split(",");


            for(var j=0;j<paramIds.length;j++){
                if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
                }else if(paramIds[j].id=='AS_OF_DATE1' || paramIds[j].id=='AS_OF_DATE2' || paramIds[j].id=='CMP_AS_OF_DATE1' || paramIds[j].id=='CMP_AS_OF_DATE2'){
                }else if(paramIds[j].id=='AS_OF_DMONTH1' || paramIds[j].id=='AS_OF_DMONTH2' || paramIds[j].id=='CMP_AS_OF_DMONTH1' || paramIds[j].id=='CMP_AS_OF_DMONTH2'){
                }else if(paramIds[j].id=='AS_OF_DQUARTER1' || paramIds[j].id=='AS_OF_DQUARTER2' || paramIds[j].id=='CMP_AS_OF_DQUARTER1' || paramIds[j].id=='CMP_AS_OF_DQUARTER2'){
                }else if(paramIds[j].id=='AS_OF_DYEAR1' || paramIds[j].id=='AS_OF_DYEAR2' || paramIds[j].id=='CMP_AS_OF_DYEAR1' || paramIds[j].id=='CMP_AS_OF_DYEAR2'){
                }else if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_DAY_ROLLING'){
                }else if(paramIds[j].id=='AS_OF_WEEK' || paramIds[j].id=='AS_OF_WEEK1'){
                }else if(paramIds[j].id=='AS_OF_MONTH' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
                }else if(paramIds[j].id=='AS_OF_MONTH1' || paramIds[j].id=='AS_OF_MONTH2'){
                }else if(paramIds[j].id=='AS_OF_QUARTER' || paramIds[j].id=='AS_OF_QUARTER1'){
                }else if(paramIds[j].id=='AS_OF_YEAR' || paramIds[j].id=='AS_OF_YEAR1'){
                }else {
                    pIds=(paramIds[j].id).split("-");
                    if(exreplist!=""){
                        var count=0;
                        for(var j1=0;j1<exreplist.length;j1++){
                            if(pIds[1]==exreplist[j1]){
                                count++;
                            }
                        }

                        if(!(count>0)){
                            var colRow=divColTable.insertRow(k);
                            var colCell1=colRow.insertCell(0);
                            var divColinput=document.createElement("input");
                            divColinput.type='checkbox';
                            divColinput.name='colParamId';
                            divColinput.id='colParamId';
                            //divColinput.id='colParamId'+j;

                            //  if(prevColParams.search(pIds[1])!=-1){
                            //  divColinput.checked="true";
                            // }

                            divColinput.value=pIds[1];
                            colCell1.appendChild(divColinput);
                            for(var cnt=0;cnt<prevColParamsStr.length;cnt++){
                                if(prevColParamsStr[cnt]==pIds[1]){
                                    divColinput.checked="true";

                                }
                            }

                            var colCell2=colRow.insertCell(0);
                            //colCell2.id='colParam'+j;
                            colCell2.id='colParam';
                            var paramName=paramIds[j].getElementsByTagName("td");
                            colCell2.innerHTML=paramName[1].innerHTML;
                            k++;
                        }
                    }else{
                        var colRow=divColTable.insertRow(k);
                        var colCell1=colRow.insertCell(0);
                        var divColinput=document.createElement("input");
                        divColinput.type='checkbox';
                        divColinput.name='colParamId';
                        divColinput.id='colParamId';
                        //divColinput.id='colParamId'+j;
                        pIds=(paramIds[j].id).split("-");

                        //                        if(prevColParams.search(pIds[1])!=-1){
                        //                            divColinput.checked="true";
                        //                        }


                        divColinput.value=pIds[1];
                        colCell1.appendChild(divColinput);
                        for(var cnt=0;cnt<prevColParamsStr.length;cnt++){
                            if(prevColParamsStr[cnt]==pIds[1]){
                                divColinput.checked="true";

                            }
                        }

                        var colCell2=colRow.insertCell(0);
                        //colCell2.id='colParam'+j;
                        colCell2.id='colParam';
                        colCell2.name='colParam';
                        var paramName=paramIds[j].getElementsByTagName("td");
                        colCell2.innerHTML=paramName[1].innerHTML;
                        k++;
                    }
                }
            }
            if(timeDimArray.length!=0){
                colRow=divColTable.insertRow(0);
                colCell1=colRow.insertCell(0);
                divColinput=document.createElement("input");
                divColinput.type='checkbox';
                divColinput.name='colParamId';
                divColinput.id='colParamId';
                pIds=(divColinput.id).split("-");

                //                if(prevColParams.search(pIds[1])!=-1){
                //                    divColinput.checked="true";
                //                }


                divColinput.value='TIME';
                colCell1.appendChild(divColinput);

                for(var cnt=0;cnt<prevColParamsStr.length;cnt++){
                    if(prevColParamsStr[cnt]==pIds[1]){
                        divColinput.checked="true";

                    }
                }


                colCell2=colRow.insertCell(0);
                //colCell2.id='colParam-TIME';
                colCell2.id='colParam';
                colCell2.name='colParam';
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


/*Added by Praveen and Santhosh for changing REP and CEP layout in report designing*/


function showRowParams2(){
    rowparamsNamesarr = new Array();
    document.getElementById("availableRowParamSortable").innerHTML="";
    document.getElementById("existingRowParamSortable").innerHTML="";
    var data=document.getElementById("repExc").value;
    var params="";
    var pIds="";
    var k=0;
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    var exreplist="";

    if(data!=""){
        exreplist=data.split(",");
    }

if(exclParamViewbyFlag==false){

    if(paramIds.length!=0){
        for(var i=0;i<paramIds.length;i++){
            pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
        if(params!=""){
            params=params.substr(1);
        }
        var prevRowParams= document.getElementById("REPIds").value;
        if(prevRowParams !="" && prevRowParams !=null && prevRowParams != " ")
        {
            var prevRowParamsStr=prevRowParams.split(",");
            rowparamsarr=new Array();
            rowparamsNamesarr=new Array();
            for(var i=0;i<prevRowParamsStr.length;i++){
                rowparamsarr.push(prevRowParamsStr[i]);
            }
        }

        for(var j=0;j<paramIds.length;j++){
            if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
            }else if(paramIds[j].id=='AS_OF_DATE1' || paramIds[j].id=='AS_OF_DATE2' || paramIds[j].id=='CMP_AS_OF_DATE1' || paramIds[j].id=='CMP_AS_OF_DATE2'){
            }else if(paramIds[j].id=='AS_OF_DMONTH1' || paramIds[j].id=='AS_OF_DMONTH2' || paramIds[j].id=='CMP_AS_OF_DMONTH1' || paramIds[j].id=='CMP_AS_OF_DMONTH2'){
            }else if(paramIds[j].id=='AS_OF_DQUARTER1' || paramIds[j].id=='AS_OF_DQUARTER2' || paramIds[j].id=='CMP_AS_OF_DQUARTER1' || paramIds[j].id=='CMP_AS_OF_DQUARTER2'){
            }else if(paramIds[j].id=='AS_OF_DYEAR1' || paramIds[j].id=='AS_OF_DYEAR2' || paramIds[j].id=='CMP_AS_OF_DYEAR1' || paramIds[j].id=='CMP_AS_OF_DYEAR2'){
            }else if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_DAY_ROLLING'){
            }else if(paramIds[j].id=='AS_OF_WEEK' || paramIds[j].id=='AS_OF_WEEK1'){
            }else if(paramIds[j].id=='AS_OF_MONTH' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
            }else if(paramIds[j].id=='AS_OF_MONTH1' || paramIds[j].id=='AS_OF_MONTH2'){
            }else if(paramIds[j].id=='AS_OF_QUARTER' || paramIds[j].id=='AS_OF_QUARTER1'){
            }else if(paramIds[j].id=='AS_OF_YEAR' || paramIds[j].id=='AS_OF_YEAR1'){
            }else {
                pIds=(paramIds[j].id).split("-");
                if(exreplist!=""){
                    var count=0;
                    for(var j1=0;j1<exreplist.length;j1++){
                        if(pIds[1]==exreplist[j1]){
                            count++;
                        }
                    }

                    if(!(count>0)){
                        createParamElements(pIds[1],paramIds[j].getElementsByTagName("td")[1].innerHTML, "availableRowParamSortable","true","mydragRowTabs");
                        for(var cnt=0;cnt<rowparamsarr.length;cnt++){
                            if(rowparamsarr[cnt]==pIds[1]){
                                rowparamsNamesarr.push(paramIds[j].getElementsByTagName("td")[1].innerHTML);

                                createParamElements(pIds[1], paramIds[j].getElementsByTagName("td")[1].innerHTML, "existingRowParamSortable","false","mydragRowTabs");
                            }
                        }
                        k++;
                    }
                }else{
                    pIds=(paramIds[j].id).split("-");
                    createParamElements(pIds[1], paramIds[j].getElementsByTagName("td")[1].innerHTML, "availableRowParamSortable","true","mydragRowTabs");
//                    alert('rowparamsarr.length is : '+rowparamsarr.length)

//                    for(var cnt=0;cnt<rowparamsarr.length;cnt++){
//                        if(rowparamsarr[cnt]==pIds[1]){
//                            rowparamsNamesarr.push(paramIds[j].getElementsByTagName("td")[1].innerHTML);
//                            createParamElements(pIds[1], paramIds[j].getElementsByTagName("td")[1].innerHTML, "existingRowParamSortable","false","mydragRowTabs");
//                        }
//                    }
                    k++;
                }
            }
        }
            for(var cnt=0;cnt<rowparamsarr.length;cnt++){
                for(var j=0;j<paramIds.length;j++){
                    pIds=(paramIds[j].id).split("-");
                    if(rowparamsarr[cnt]==pIds[1]){
                        rowparamsNamesarr.push(paramIds[j].getElementsByTagName("td")[1].innerHTML);
                        createParamElements(pIds[1], paramIds[j].getElementsByTagName("td")[1].innerHTML, "existingRowParamSortable","false","mydragRowTabs");
                    }
                }
            }
        if(timeDimArray.length!=0){
            createParamElements('TIME', 'Time', "availableRowParamSortable","true","mydragRowTabs");
            for(var cnt=0;cnt<rowparamsarr.length;cnt++){
                if(rowparamsarr[cnt]=='TIME'){
                    rowparamsNamesarr.push('Time');
                    createParamElements('TIME', 'Time', "existingRowParamSortable","false","mydragRowTabs");
                }
            }
            k++;
        }
        dropRowParameters();
        $("#rowParamDisplay").dialog('open');
    }
    }else if(exclParamViewbyFlag==true){
        prevRowParams= document.getElementById("REPIds").value;
        if(prevRowParams !="" && prevRowParams !=null && prevRowParams != " ")
        {
            prevRowParamsStr=prevRowParams.split(",");
            rowparamsarr=new Array();
            rowparamsNamesarr=new Array();
            for(var i=0;i<prevRowParamsStr.length;i++){
                rowparamsarr.push(prevRowParamsStr[i]);
            }
        }
        if(timeDimArray.length!=0){
            createParamElements('TIME', 'Time', "availableRowParamSortable","true","mydragRowTabs");
            for(var cnt=0;cnt<rowparamsarr.length;cnt++){
                if(rowparamsarr[cnt]=='TIME'){
                    rowparamsNamesarr.push('Time');
                    createParamElements('TIME', 'Time', "existingRowParamSortable","false","mydragRowTabs");
                }
            }
            k++;
        }
        dropRowParameters();
        $("#rowParamDisplay").dialog('open');
    }
    else{
        alert("Please select Parameters")
    }

}
function showColParams2(){
    // colparamsNamesarr = new Array();
    document.getElementById("availableColParamSortable").innerHTML="";
    document.getElementById("existingColParamSortable").innerHTML="";

    var data=document.getElementById("cepExc").value;
    var params="";
    var pIds="";
    var k=0;
    var paramUl=document.getElementById("sortable");
    var paramIds=paramUl.getElementsByTagName("li");
    var exreplist="";
    if(data!=""){
        exreplist=data.split(",");
    }
    if(exclParamViewbyFlag==false){
    if(paramIds.length!=0){
        for(var i=0;i<paramIds.length;i++){
            pIds=(paramIds[i].id).split("-");
            params=params+","+pIds[1];
        }
        if(params!=""){
            params=params.substr(1);
        }
        var prevColParams= document.getElementById("CEPIds").value;
        //alert(document.getElementById("CEPIds").value+"value")
        if(prevColParams!="" && prevColParams!=null && prevColParams!= " ")
        {
            var prevColParamsStr=prevColParams.split(",");
            colparamsarr = new Array();
            colparamsNamesarr=new Array();
            for(var i=0;i<prevColParamsStr.length;i++){

                colparamsarr.push(prevColParamsStr[i]);
            }
        }


        for(var j=0;j<paramIds.length;j++){
            if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
            }else if(paramIds[j].id=='AS_OF_DATE1' || paramIds[j].id=='AS_OF_DATE2' || paramIds[j].id=='CMP_AS_OF_DATE1' || paramIds[j].id=='CMP_AS_OF_DATE2'){
            }else if(paramIds[j].id=='AS_OF_DMONTH1' || paramIds[j].id=='AS_OF_DMONTH2' || paramIds[j].id=='CMP_AS_OF_DMONTH1' || paramIds[j].id=='CMP_AS_OF_DMONTH2'){
            }else if(paramIds[j].id=='AS_OF_DQUARTER1' || paramIds[j].id=='AS_OF_DQUARTER2' || paramIds[j].id=='CMP_AS_OF_DQUARTER1' || paramIds[j].id=='CMP_AS_OF_DQUARTER2'){
            }else if(paramIds[j].id=='AS_OF_DYEAR1' || paramIds[j].id=='AS_OF_DYEAR2' || paramIds[j].id=='CMP_AS_OF_DYEAR1' || paramIds[j].id=='CMP_AS_OF_DYEAR2'){
            }else if(paramIds[j].id=='AS_OF_DATE' || paramIds[j].id=='PRG_DAY_ROLLING'){
            }else if(paramIds[j].id=='AS_OF_WEEK' || paramIds[j].id=='AS_OF_WEEK1'){
            }else if(paramIds[j].id=='AS_OF_MONTH' || paramIds[j].id=='PRG_PERIOD_TYPE' || paramIds[j].id=='PRG_COMPARE'){
            }else if(paramIds[j].id=='AS_OF_MONTH1' || paramIds[j].id=='AS_OF_MONTH2'){
            }else if(paramIds[j].id=='AS_OF_QUARTER' || paramIds[j].id=='AS_OF_QUARTER1'){
            }else if(paramIds[j].id=='AS_OF_YEAR' || paramIds[j].id=='AS_OF_YEAR1'){
            }else {
                pIds=(paramIds[j].id).split("-");
                if(exreplist!=""){
                    var count=0;
                    for(var j1=0;j1<exreplist.length;j1++){
                        if(pIds[1]==exreplist[j1]){
                            count++;
                        }
                    }

                    if(!(count>0)){

                        createParamElements(pIds[1],paramIds[j].getElementsByTagName("td")[1].innerHTML, "availableColParamSortable","true","mydragColTabs");
                        for(var cnt=0;cnt<colparamsarr.length;cnt++){
                            if(colparamsarr[cnt]==pIds[1]){
                                colparamsNamesarr.push(pIds[1],paramIds[j].getElementsByTagName("td")[1].innerHTML);

                                createParamElements(pIds[1], paramIds[j].getElementsByTagName("td")[1].innerHTML, "existingColParamSortable","false","mydragColTabs");
                            }
                        }
                        k++;
                    }
                }else{

                    pIds=(paramIds[j].id).split("-");
                    createParamElements(pIds[1], paramIds[j].getElementsByTagName("td")[1].innerHTML, "availableColParamSortable","true","mydragColTabs");

                    for(var cnt=0;cnt<colparamsarr.length;cnt++){
                        if(colparamsarr[cnt]==pIds[1]){
                            colparamsNamesarr.push(pIds[1], paramIds[j].getElementsByTagName("td")[1].innerHTML);

                            createParamElements(pIds[1], paramIds[j].getElementsByTagName("td")[1].innerHTML, "existingColParamSortable","false","mydragColTabs");
                        }
                    }
                    k++;
                }
            }
        }
        if(timeDimArray.length!=0){
            createParamElements('TIME', 'Time', "availableColParamSortable","true","mydragColTabs");
            for(var cnt=0;cnt<colparamsarr.length;cnt++){
                if(colparamsarr[cnt]=='Time'){
                    colparamsNamesarr.push('Time');
                    createParamElements('TIME', 'Time', "existingColParamSortable","false","mydragColTabs");
                }
            }
            k++;
        }
        //getColumnEdgeParams();
        $("#colParamDisplay").dialog('open');
        dropColumnParameters()
    }
    }else if(exclParamViewbyFlag==true){
        prevColParams= document.getElementById("CEPIds").value;
        if(prevColParams!="" && prevColParams!=null && prevColParams!= " ")
        {
            prevColParamsStr=prevColParams.split(",");
            colparamsarr = new Array();
            colparamsNamesarr=new Array();
            for(var i=0;i<prevColParamsStr.length;i++){

                colparamsarr.push(prevColParamsStr[i]);
            }
        }

        if(timeDimArray.length!=0){
            createParamElements('TIME', 'Time', "availableColParamSortable","true","mydragColTabs");
            for(var cnt=0;cnt<colparamsarr.length;cnt++){
                if(colparamsarr[cnt]=='TIME'){
                    colparamsNamesarr.push('Time');
                    createParamElements('TIME', 'Time', "existingColParamSortable","false","mydragColTabs");
                }
            }
            k++;
        }
        //getColumnEdgeParams();
        $("#colParamDisplay").dialog('open');
        dropColumnParameters()
    }
    else{
        alert("Please select Parameters")
    }

}
function createParamElements(paramId,paramName,sortableDivName,flag,tdclsname){

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
        a.href="javascript:deleteParamElements('"+paramName+"','"+paramId+"','"+sortableDivName+"','"+tdclsname+"')";
        a.innerHTML="a";
        a.className="ui-icon ui-icon-close";
        cell1.appendChild(a);
        cell2=row.insertCell(1);
        cell2.innerHTML=paramName;
        childLI.appendChild(table);
    }
    parentUL.appendChild(childLI);
}
function dropRowParameters()
{
    $(".mydragRowTabs").draggable({
        helper:"clone",
        effect:["", "fade"]
    });
    $("#dropingrowprms").droppable({
        accept:'.mydragRowTabs',
        drop: function(ev, ui) {
            var flag=false;

            for(var i=0;i<rowparamsarr.length;i++){
                if( rowparamsarr[i]==ui.draggable.attr('id')){
                    flag=true;
                }
            }
            if(!flag){
                rowparamsarr.push(ui.draggable.attr('id'));
                rowparamsNamesarr.push(ui.draggable.html());

                createParamElements(ui.draggable.attr('id'), ui.draggable.html(), "existingRowParamSortable", "false","mydragRowTabs");
            }
        }
    });
    $("#existingRowParamSortable").sortable();
}
function dropColumnParameters(){
    $(".mydragColTabs").draggable({
        helper:"clone",
        effect:["", "fade"]
    });
    $("#dropingcolprms").droppable({
        accept:'.mydragColTabs',
        drop: function(ev, ui) {
            var flag=false;

//            if(colparamsarr.length==0){
                for(var i=0;i<colparamsarr.length;i++){
                    if( colparamsarr[i]==ui.draggable.attr('id')){
                        flag=true;
                    }
                }
                if(!flag){
                    colparamsarr.push(ui.draggable.attr('id'));

                    colparamsNamesarr.push(ui.draggable.html());
                    createParamElements(ui.draggable.attr('id'), ui.draggable.html(), "existingColParamSortable", "false","mydragColTabs");
                }
//            }else{
//                alert("You can select maximum of one Column Parameter")
//            }
        }
    });
    $("#existingColParamSortable").sortable();
}
function deleteParamElements(paramName,paramId,sortableDivName,deletefrom){
    var parentUL = document.getElementById(sortableDivName);
    var len = parentUL.childNodes.length;

    for(var i = 0; i < len; i++){
        if(parentUL.childNodes[i].id == paramId){
            parentUL.removeChild(parentUL.childNodes[i]);
            break;
        }
    }
    if(deletefrom=="mydragRowTabs"){
        for(var j = 0; j < rowparamsarr.length; j++){
            if(rowparamsarr[j] == paramId){
                rowparamsarr.splice(j,1);
                rowparamsNamesarr.splice(j,1);
            }
        }
    }
    else{
        for(var k = 0; k < colparamsarr.length; k++){
            if(colparamsarr[k] == paramId){
                colparamsarr.splice(k,1);
                colparamsNamesarr.splice(k,1);
            }
        }
    }
}
function getRowEdgeParams2(){
    

    var ul = document.getElementById("existingRowParamSortable");
    var viewByVals=new Array;
    if(ul!=undefined || ul!=null){
                    var colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            //cols = cols+","+colIds[i].id.replace("GrpCol","");
                            viewByVals.push(colIds[i].id);
                        }

                    }
         }
     var rowEdgeParams=viewByVals.toString();
    var REPNames=rowparamsNamesarr.toString();

    document.getElementById("REPIds").value=rowEdgeParams;
    $("#rowParamDisplay").dialog('close');
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=REP&rowEdgeParams='+rowEdgeParams+'&rowEdgeParamsNames='+REPNames+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
        }
    });
}
function getColumnEdgeParams2(){
    var colEdgeParams= colparamsarr.toString();
    var CEPNames= colparamsNamesarr.toString();

    document.getElementById("CEPIds").value=colEdgeParams;

//    if(colparamsarr.length<=1){
        $.ajax({
            url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=CEP&colEdgeParams='+colEdgeParams+'&colEdgeParamsNames='+CEPNames+'&REPORTID='+document.getElementById("REPORTID").value,
            success: function(data){
            }
        });
//    }else{
//        alert("Please Select only one Column Parameters")
//    }
    $("#colParamDisplay").dialog('close');

}
/*end of code by Praveen and Santhosh for changing REP and CEP layout in report designing*/

function getTableColumns(){
    var tabmsrs="";
    var tabmsrsName="";
    var tabmsrNameOnly="";
    var msrUl=document.getElementById("sortable");
    var msrIds=msrUl.getElementsByTagName("li");
    if(msrIds.length!=0){
        for(var i=0;i<msrIds.length;i++){
            var msrIdtds=msrIds[i].getElementsByTagName("td");
            var measureIds=(msrIds[i].id).replace("Msr", "", "gi");
            tabmsrs=tabmsrs+","+measureIds;
            tabmsrsName=tabmsrsName+","+msrIdtds[1].innerHTML;
        }
        tabmsrs=tabmsrs.substr(1);
        tabmsrsName=tabmsrsName.substr(1);
        $.ajax({
            url: 'reportTemplateAction.do?templateParam=getTabColumns&Msrs='+tabmsrs+'&MsrsNames='+tabmsrsName+'&REPORTID='+parent.document.getElementById("REPORTID").value,
            success: function(data) {
                if(data!=""){
                    document.getElementById("TabCols").innerHTML=data;
                }
                else{
                    document.getElementById("TabCols").innerHTML="";
                }
            }
        });
    }
}

function showSqlCommand(){
    //    alert('in showSqlCommand() sqlstr is : '+sqlStr)
    $("#sqlStrDialog").dialog('open');
    $("#sqlStrDialog").html(sqlStr);
}
function clearParams(){
    var zero=0
    var rowParamsLength=rowparamsarr.length
    var colParamsLength=colparamsarr.length
//    alert(colparamsarr.length+"****"+rowparamsarr.length+"!!!"+rowparamsarr.toString())
//   if(rowParamsLength!=zero || colParamsLength!=zero){
//       alert("unable to clear the Parameter Region")
//   }
//   else{
//   if(paramArray.length!=0){
//   alert("Dragged Parameters are going to be cleared")
var confirmText = confirm("Do You Want To Clear The Dragged Parameters")
if(confirmText==true){
$("#sortable").html("");
   timeDimArray=new Array
   y=new Array
   x=new Array
   paramArray=new Array
   paramIdArray=new Array
}
//   }else{
//       alert("Parameters Does Not Exists To Clear")
//   }
// }
}
function isGraphExists(){
//    alert("in reportDesign.js in isGraphExists()")
    if(document.getElementById("allGraphIds").value!=''){
       document.getElementById("allGraphIds").value=graphIds;
    parentDiv = document.getElementById('previewDispGraph');
    document.getElementById("previewDispGraph").style.height = '250';
    parentDiv.innerHTML='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    $.ajax({
        url: 'reportTemplateAction.do?templateParam=buildGraphs&gid='+currGrpId1+'&grptypid='+typeValue1+'&graphChange=default&grpIds='+graphIds+'&REPORTID='+document.getElementById("REPORTID").value,
        success: function(data){
            parentDiv.innerHTML=data;
            document.getElementById("previewDispGraph").style.height = 'auto';
        }
    });

    var previewGraphObj=document.getElementById("previewGraph");
    previewGraphObj.style.display='';
    $("#graphList").dialog('close');
    document.getElementById("allGraphIds").value=graphIds;

    }
}
function getDisplayTables(ctxpath,paramslist,repId){
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
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+parent.buildFldIds()+'&REPORTID='+document.getElementById("REPORTID").value;
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
                });

        }
    }
    function showList(ctxpath,paramslist){
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
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTables('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);

                });
        }else{
           $("#paramVals").hide();
        }

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
    function setValueToContainer(ctxpath,repId,bizRoles){
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
    function timeBasis(){
           parent.$("#selectTime").hide();
//           var timeDetail = parent.document.getElementsByName("time");
//           alert(timeDetail);
           var timeDetail = parent.document.getElementById("time").value
//           var selectdDetails = "";
//           if(timeDetail.length!=0){
//                    for(var i=0;i<timeDetail.length;i++){
//                        if(timeDetail[i].checked){
//                            selectdDetails=selectdDetails+","+timeDetail[i].id;
//                        }
//                    }
//                }
//                if (selectdDetails!=""){
//                    selectdDetails=selectdDetails.substr(1,selectdDetails.length);
//                }
                if(timeDetail == "StandardTime"){
                    var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                    var timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }  else if(timeDetail == "RangeBasis"){
                    timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }
                 else {
                    timeDim='';
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                 }
       }
       function showMeasuresInDesigner(){
    var prevREPIds=parent.document.getElementById("REPIds").value
    var frameObj=parent.document.getElementById("dataDispmem");
    var prevCEPIds=parent.document.getElementById("CEPIds").value
    var roleid=parent.document.getElementById("roleid").value
    var RepIdsArray = new Array()
    var CepIdsArray = new Array()
   RepIdsArray = prevREPIds.split(",")
   CepIdsArray = prevCEPIds.split(",")
   var flag = 0;
   for(var i=0;i<RepIdsArray.length;i++){
      for(var j=0;j<CepIdsArray.length;j++){
          if(RepIdsArray[i] == CepIdsArray[j]){
             flag=flag+1;
          }
      }
   }
    if(prevREPIds!=""){
        if(flag==0){
        $("#measuresDialog").dialog('open');
        var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleid+'&REPORTID='+parent.document.getElementById("REPORTID").value;
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
        }else{
            alert("Please select different Row Edge and Col Edge")
        }

    }
    else{
        if(prevREPIds=="" || prevREPIds==undefined ){
            alert("Please select Row Edge ")
        }
    }
}
//added by krishan
function createMeasures1(measureName,elmntId){
    // alert("reportdesignjs")
    var i=0;
    var parentUL=document.getElementById("sortable1");
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
        a.href="javascript:deleteMeasure1('Msr"+elmntId+"')"; //Modified by Ram fn name deleteMeasure1
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
    $("#sortable1").sortable();
    $("#sortable1").disableSelection();
}

//Added by Ram 06Aug15
function deleteMeasure1(msrName){
 var c = document.getElementById("sortable1").childNodes;
    if(c.length==2){
    document.getElementById('dropimage1').style.display='block';
    document.getElementById("dropmeasures").style.backgroundColor='#FFFFFF';
    }
    var LiObj=document.getElementById(msrName);
    var tab=LiObj.getElementsByTagName("table");
    var row=tab[0].rows;
    var cells=row[0].cells;
    var parentUL=document.getElementById("sortable1");
    parentUL.removeChild(LiObj);
    var x=msrName.replace("Msr","");
    //var x=msrName;
    var i=0;
    for(i=0;i<msrArray.length;i++){
        if(msrArray[i]==x)
            msrArray.splice(i,1);
    }
    //getTableColumns();
}

//function PreviewAO(isFromAOEdit){
////    alert("PriviewAO");
//                   var From = "";
//                   var isKpiDashboard=""
//                if(parent.document.getElementById("Designer")!= null){
//                    From=parent.document.getElementById("Designer").value;
//                }
//   // alert("in PreviewTable in reportDesign.js")
//    var prevREPIds=document.getElementById("REPIds").value;
//    var prevCEPIds=document.getElementById("CEPIds").value;
//
//    var prevMsrs=document.getElementById("Measures").value;
//    var editTable=document.getElementById("editTable");
//    if(prevREPIds!="" && prevMsrs.length!=""){
//        if(prevCEPIds!=""){
////            if(prevCEPIds.split(",").length==1){
//                editTable.style.display='none';
//                if(From != null && From=="fromDesigner"){
//                    dispAO("fromDesigner",isFromAOEdit);
//                }else
//                dispAO("design",isFromAOEdit);
////            }
////            else{
////                alert("Please Select only one Column Edge")
////            }
//        }else{
//            if(editTable!=null){
//            editTable.style.display='none';
//            }
//            if(From != null && From=="fromDesigner"){
//                    dispAO("fromDesigner",isFromAOEdit);
//                }else
//            dispAO("design",isFromAOEdit);
//        }
//    }
//    else{
//        if(prevREPIds=="" || prevREPIds.length==0){
//            alert("Please select Row Edge")
//        }
//        else if(prevMsrs=="" || prevMsrs.length==0){
//            alert("Please select Measures")
//        }
//    }
//}

