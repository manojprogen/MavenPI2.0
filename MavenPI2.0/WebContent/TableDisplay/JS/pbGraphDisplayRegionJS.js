/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//$(document).ready(function(){
//    var shwTable = document.getElementById("showTable").value;
//    if(shwTable == "GTM"){
//        document.getElementById("addGraphTr").style.display = "none";
//    }else{
//        document.getElementById("addGraphTr").style.display = "block";
//    }p
//

 var graphtype="";
 var graphtypeid="";
 var graphtypename="";
 var selectedgraphtype="";
 var tablecolnames="";
function changeGrpType(grpTypeId,grpId,repId){    
    var dispgrptypObj=document.getElementById("dispgrptypes"+grpId);
    dispgrptypObj.style.display='none';
    document.getElementById('grptypid').value=grpTypeId;
    document.getElementById('gid').value=grpId;
    document.getElementById('graphChange').value='GrpType';
    var ctxPath=document.getElementById("ctxPath").value;
    if(grpTypeId=='TimeSeries'){
        $.ajax({
            type: 'GET',
            async: false,
            cache: false,
            timeout: 30000,
            url: ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+repId+"&grptypid="+grpTypeId+"&gid="+grpId+"&graphChange=GrpType",
            success: function(data){
            }
        });
        parent.submitformTimeSeries('true');
    }else{
        document.forms.myGrpForm.action=ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+repId+"&selectedgraphtype="+graphtype;
        document.forms.myGrpForm.submit();
    }

}
function changeGrpSize(grpSizeId,grpId,repId){
    document.getElementById('grpsizeid').value=grpSizeId;
    document.getElementById('gid').value=grpId;
    document.getElementById('graphChange').value='GrpSize';
    var ctxPath=document.getElementById("ctxPath").value;
    document.forms.myGrpForm.action=ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+repId;
    document.forms.myGrpForm.submit();
}
function graphTypesDisp(dispgrptypObj,selectgraph){    
  graphtype=selectgraph;
    if(dispgrptypObj.style.display=='none'){
        dispgrptypObj.style.display='';
    }
    else{
        dispgrptypObj.style.display='none';
    }
}
function graphSizesDisp(dispgrpsizesObj){
    if(dispgrpsizesObj.style.display=='none'){
        dispgrpsizesObj.style.display='';
    }
    else{
        dispgrpsizesObj.style.display='none';
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
function swapGraphAnalysis(grpId,swapBy,repId){
    document.getElementById('gid').value=grpId;
    document.getElementById('swapBy').value=swapBy;
    document.getElementById('graphChange').value='SwapGraph';
    var ctxPath=document.getElementById("ctxPath").value;
    document.forms.myGrpForm.action=ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+repId;
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
function dispChangeGrpColumns(ctxPath,allGraphIds,currentGraphId,bizRoles,reportId){

    $.post(ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&graphId='+currentGraphId+'&folderIds='+bizRoles+'&grpIds='+allGraphIds+'&REPORTID='+reportId, $("#myGrpForm").serialize() ,
    function(data){
             parent.$("#graphColsDialog").dialog('open');
    });

//    $.ajax({
//        url:ctxPath+'/reportViewer.do?reportBy=loadDialogs&loadDialogs=true&graphId='+currentGraphId+'&folderIds='+bizRoles+'&grpIds='+allGraphIds+'&REPORTID='+reportId,
//        success:function(data) {
//             parent.$("#graphColsDialog").dialog('open');
//        }
//    });
     if($.browser.msie == true)
    {
        parent.document.getElementById("graphColsFrame").src="TableDisplay/PbChangeGraphColumnsRT.jsp";
    }
//    var frameObj=parent.document.getElementById("graphColsFrame");
//    var source = "TableDisplay/PbChangeGraphColumnsRT.jsp?graphId="+currentGraphId+"&folderIds="+bizRoles+"&grpIds="+allGraphIds+'&REPORTID='+reportId;
//    frameObj.src=source;
//    parent.$("#graphColsDialog").dialog('open');

//frameObj.style.display='block';
//parent.document.getElementById('fadestart').style.display='block';
}
function dispRowValues(rowValuesDiv,rowValues,grpId,repId){
    var rowValuesDivObj=document.getElementById(rowValuesDiv);
    if(rowValuesDivObj!=null && rowValuesDivObj!=undefined){
        if(rowValuesDivObj.style.display=="none"){
            rowValuesDivObj.style.display="";
        }else{
            var rowVals=new Array();
            var rowsDispObj=document.getElementsByName(rowValues);
            for(var i=0;i<rowsDispObj.length;i++){
                if(rowsDispObj[i].checked){
                    rowVals.push(rowsDispObj[i].value);
                }
            }
            rowValuesDivObj.style.display="none";
            document.getElementById('gid').value=grpId;
            document.getElementById('graphChange').value='RowValues';
            var ctxPath=document.getElementById("ctxPath").value;
            document.forms.myGrpForm.action=ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+repId+"&rowValues="+rowValues;
            document.forms.myGrpForm.submit();
        }
    }
}

function dispGrpProps(dispGrpPropsDiv){

    var dispGrpPropsDivObj=document.getElementById(dispGrpPropsDiv);

    if(dispGrpPropsDivObj!=null && dispGrpPropsDivObj!=undefined){
        if(dispGrpPropsDivObj.style.display=="none"){
            dispGrpPropsDivObj.style.display="";
        }else{
            dispGrpPropsDivObj.style.display="none";
        }
    }
}

function showLegends(dispGrpPropsDiv,showLegends,grpId,repId){
    var dispGrpPropsDivObj=document.getElementById(dispGrpPropsDiv);
    var ctxPath=document.getElementById("ctxPath").value;

    document.getElementById('gid').value=grpId;
    document.getElementById('graphChange').value='showLegends';

    document.forms.myGrpForm.action=ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+repId+"&showLegends="+showLegends;
    document.forms.myGrpForm.submit();
    dispGrpPropsDivObj.style.display="none";
}

function showGT(dispGrpPropsDiv,showGT,grpId,repId){
    var dispGrpPropsDivObj=document.getElementById(dispGrpPropsDiv);
    var ctxPath=document.getElementById("ctxPath").value;

    document.getElementById('gid').value=grpId;
    document.getElementById('graphChange').value='showGT';

    document.forms.myGrpForm.action=ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+repId+"&showGT="+showGT;

    document.forms.myGrpForm.submit();
    dispGrpPropsDivObj.style.display="none";
}
function showGrpProperties(reportId,graphId,ctxPath,grpName){
    parent.showGrpProperties(reportId, graphId, ctxPath,grpName,graphtype);
}
function cancelGrpProperties(){
    parent.cancelGrpProperties();
}
function refreshReportGraphs(ctxPath,tabId){
    parent.refreshReportGraphs(ctxPath,tabId);
}
function deleteGraph(graphId,reportId){
    var res= confirm("Are you sure, you want to delete the graph");
    if(res){
        document.getElementById('gid').value=graphId;
        document.getElementById('graphChange').value='DeleteGraph';
        var ctxPath=document.getElementById("ctxPath").value;
        document.forms.myGrpForm.action=ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+reportId;
        document.forms.myGrpForm.submit();
    }

}
//#############
function showGraphsList(){
    if ($.browser.msie == true){
        $("#graphList").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 600,
            modal: true
        });
    }else{
        $("#graphList").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 300,
            width: 600,
            modal: true
        });
    }

    $("#graphList").dialog('open');
}
//function addMoreGraphs(reportId,graphIds,grptype,ctxPath){
function addMoreGraphs(reportId,graphIds,grptype,ctxPath,currGrpId){
    /*
     var graphIdsStr;
     var currGrpId;
     if(graphIds!=''){
        graphIdsStr=graphIds.split(",");
        currGrpId=parseInt(graphIdsStr[graphIdsStr.length-1])+1;
        graphIds=graphIds+","+currGrpId
    }else{
        currGrpId="1";
        graphIds=currGrpId;
    }
     */
    if(graphIds!=''){
        graphIds=graphIds+","+currGrpId
    }else{
        graphIds=currGrpId;
    }

    //alert(ctxPath+'/reportViewer.do?reportBy=graphChanges&REPORTID='+reportId+'&gid='+currGrpId+'&grptypid='+grptype+'&graphChange=AddGraph&grpIds='+graphIds);
    $.ajax({
        type: 'GET',
            async: false,
            cache: false,
            timeout: 30000,
        url: ctxPath+'/reportViewer.do?reportBy=graphChanges&REPORTID='+reportId+'&gid='+currGrpId+'&grptypid='+grptype+'&graphChange=AddGraph&grpIds='+graphIds,
        success: function(data){
            parent.refreshGraphs1(ctxPath,reportId);
        }
    });
    $("#graphList").dialog('close');
}
function editGraphTitle(graphId,grpTitle){
    if ($.browser.msie == true){
        $("#editGraphTitle").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 200,
            width: 300,
            modal: true
        });
    }else{
        $("#editGraphTitle").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 50,
            width: 300,
            modal: true
        });
    }

    document.getElementById("graphId").value=graphId;
    document.getElementById("grpTitle").value=grpTitle
    $("#editGraphTitle").dialog('open');
}

function updateGraphTitle(){
    var graphName=encodeURIComponent(document.getElementById("grpTitle").value);
    var reportId=document.getElementById("tabId").value;
    var graphId=document.getElementById("graphId").value;
    var ctxPath=document.getElementById("ctxPath").value;

    $.ajax({
        type: 'GET',
            async: false,
            cache: false,
            timeout: 30000,
        url: ctxPath+'/reportViewer.do?reportBy=graphChanges&REPORTID='+reportId+'&gid='+graphId+'&graphName='+graphName+'&graphChange=updateGraphTitle',
        success: function(data){
            parent.refreshGraphs1(ctxPath,reportId);
        }
    });
    $("#editGraphTitle").dialog('close');
}

function buildJqGraph(grpTypeId,grpidfrmrep,grpId,repId,pcharts){

graphtypeid=grpId
graphtypename=grpTypeId;
    var selectedgrapg=graphtype;
    document.getElementById('grptypid').value=grpTypeId;
    document.getElementById('gid').value=grpId;
    document.getElementById('graphChange').value='GrpType';
    var ctxPath=document.getElementById("ctxPath").value;
    if(grpTypeId=='TimeSeries'){
        $.ajax({
            type: 'GET',
            async: false,
            cache: false,
            timeout: 30000,
            url: ctxPath+"/reportViewer.do?reportBy=graphChanges&REPORTID="+repId+"&grptypid="+grpTypeId+"&gid="+grpId+"&graphChange=GrpType",
            success: function(data){
            }
        });
        parent.submitformTimeSeries('true');
    }else{
        document.getElementById("jfchartid"+pcharts).innerHTML = "<iframe id='jqframe' class=frame1 frameBorder='0' STYLE='width:100%;left:100px;overflow:auto;height:500px' src="+ctxPath+"/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID="+repId+"&grptypid="+grpTypeId+"&gid="+grpId+"&selectedgraph="+selectedgrapg+"&grpidfrmrep="+grpidfrmrep+"&graphCount="+pcharts+"&graphChange=default&tableCols="+tablecolnames+"></iframe>";
         $("#jfchartid"+pcharts).show();
        $("#dispgrptypes125500").hide(); 
         $("#dispgrptypes125501").hide(); 
      
    
    };
    
    }
function jqgraphSize(reportid,graphid,graphtype1,slectedgraphtype,graphtypeid1){
    var reportid1=reportid;
    var grpFrameHeight='500';
    var grpdivHeight='400';
    var grpdivwidth='1130';
      var ctxPath=document.getElementById("ctxPath").value;
       $.ajax({
            type: 'GET',
            async: false,
            cache: false,
            timeout: 30000,
            url: ctxPath+"/reportViewer.do?reportBy=JqGraphSize&grpFrameHeight="+grpFrameHeight+"&grpdivHeight="+grpdivHeight+"&grpdivwidth="+grpdivwidth+"&reportid="+reportid1+"&graphid="+graphid+"&graphtype="+graphtypename+"&slectedgraphtype="+graphtype+"&graphtypeid="+graphtypeid,
            success: function(data){
             parent.submit('reportViewer.do?reportBy=viewReport&REPORTID='+reportid1);
            }
        });
}
function dispRowValues1(selectedgrapg,graphid,grpTypeId,rowValuesDiv,rowValues,grpId,repId,pcharts){
var graphid="5500";
var selectedgrapg="jq";
    var rowValuesDivObj=document.getElementById(rowValuesDiv);
     var ctxPath=document.getElementById("ctxPath").value;
    if(rowValuesDivObj!=null && rowValuesDivObj!=undefined){
        if(rowValuesDivObj.style.display=="none"){
            rowValuesDivObj.style.display="";
        }else{
            var rowVals=new Array();
            var rowsDispObj=document.getElementsByName(rowValues);
            for(var i=0;i<rowsDispObj.length;i++){
                if(rowsDispObj[i].checked){
                    rowVals.push(rowsDispObj[i].value);
                }
            }
            rowValuesDivObj.style.display="none";
            document.getElementById('gid').value=grpId;
            document.getElementById('graphChange').value='RowValues';                
            document.getElementById("jfchartid"+pcharts).innerHTML = "<iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100px;overflow:auto;height:500px' src="+ctxPath+"/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID="+repId+"&grptypid="+grpTypeId+"&gid="+graphid+"&selectedgraph="+selectedgrapg+"&grpidfrmrep="+grpId+"&graphChange=default&rowValues="+encodeURI(rowVals)+"&graphCount="+pcharts+"></iframe>";


        }
    }
}
function tableColumns(tableColumnsDiv,tablecolNames,repId,grpId,pcharts,grpTypeId,graphTypename){
       if(graphtypename.toString()!=''){
        graphTypename=graphtypename;
        grpTypeId=graphtypeid;
    }
     var selectedgrapg="jq";
     var tableColumnsDivObj=document.getElementById(tableColumnsDiv);
     var ctxPath=document.getElementById("ctxPath").value;
    if(tableColumnsDivObj!=null && tableColumnsDivObj!=undefined){
        if(tableColumnsDivObj.style.display=="none"){
            tableColumnsDivObj.style.display="";
        }
        else{
            var rowVals=new Array();
            var rowsDispObj=document.getElementsByName(tablecolNames);
            for(var i=0;i<rowsDispObj.length;i++){
                if(rowsDispObj[i].checked){
                    rowVals.push(rowsDispObj[i].value);
                }
            }
            if(rowVals==''){
                rowVals.push('novalues')
            }
//            alert(rowVals)
            tablecolnames=rowVals;
             tableColumnsDivObj.style.display='none';
             document.getElementById("jfchartid"+pcharts).innerHTML = "<iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100px;overflow:auto;height:500px' src="+ctxPath+"/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID="+repId+"&grptypid="+graphTypename+"&gid="+grpTypeId+"&selectedgraph="+selectedgrapg+"&grpidfrmrep="+grpId+"&graphChange=default&tableCols="+rowVals+"&graphCount="+pcharts+"></iframe>";
        }
}
}

function saveGraphRegion(ctxPath,reportId){
//         $.ajax({
//       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
//       success:function(data){
//            var jsonVar=eval('('+data+')')
//            var userType=jsonVar.userType;
//            var isPowerAnalyserEnableforUser=jsonVar.isPAEnableforUser;
//           if(userType=="Admin" || isPowerAnalyserEnableforUser=="true"){
    var confirmDel=confirm("Do you Want Save The Graph Changes Permanently");
                    if(confirmDel==true){
    $.post(ctxPath+"/reportViewer.do?reportBy=saveGraphRegion&REPORTID="+reportId,
                function(data){
                            alert("GraphRegion changes Saved");
            var path=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&action=reset";
            submiturls1(path);                            
                        });
}
//   }else{
//              alert("You do not have the sufficient previlages")
//}
//       }});
}
function zoomgraph(reportId,graphTypename,selectedgraphtype1,grpTypeId,graphIds,count){
           if(graphtypename.toString()!=''){
        graphTypename=graphtypename;
        grpTypeId=graphtypeid;
    }
      var ctxPath=document.getElementById("ctxPath").value;
     parent.zoomgraph(reportId,graphTypename,selectedgraphtype1,grpTypeId,graphIds,count,ctxPath);

}
 function secondGraph(reportId,graphid,hometabid,graphtype,ctxPath1,graphcount){
      var ctxPath=parent.document.getElementById("ctxPath").value;
                  parent.document.getElementById(""+hometabid+"").innerHTML ="<iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100px;overflow:auto;height:250px' src="+ctxPath+"/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID="+reportId+"&grptypid="+graphtype+"&gid=50001&selectedgraph=jq&grpidfrmrep="+graphid+"&graphChange=default&graphCount="+graphcount+"&fromHome=true&hometabid="+hometabid+"&graphSlide=true></iframe>";

             }

             function localRefresh(reportId,graphid,hometabid,ctxPath1){
                  var ctxPath=parent.document.getElementById("ctxPath").value;
                          $.ajax({
       url:ctxPath+"/reportTemplateAction.do?templateParam=refreshLocalHomePage"+"&reportId="+reportId+"&graphId="+graphid,
       success:function(data){
            var jsonVar=eval('('+data+')')
            var graphtype=jsonVar.graphtype;
             parent.document.getElementById(""+hometabid+"").innerHTML ="<iframe class=frame1 frameBorder='0' STYLE='width:100%;left:100px;overflow:auto;height:250px' src="+ctxPath+"/TableDisplay/progenJqplotGraphBulider.jsp?REPORTID="+reportId+"&grptypid="+graphtype+"&gid=50001&selectedgraph=jq&grpidfrmrep="+graphid+"&graphChange=default&fromHome=true&hometabid="+hometabid+"&graphSlide=true></iframe>";
       }});


             }
//###############

//added by santhosh.k on 16-02-2010
