/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mayank
 */
function loadMeasures(chartId){
    var html="";
    var allMeasures=JSON.parse(parent.$("#measure").val());
    var allMeasureIds=JSON.parse(parent.$("#measureIds").val());
    var chartData=JSON.parse(parent.$("#chartData").val());
    var measures=chartData[chartId]["meassures"];
    var comparedMeasure = chartData[chartId]["comparedMeasure"];
    var comparedMeasureId = chartData[chartId]["comparedMeasureId"];
    var measureIds=[];
    for(var i in measures){
        measureIds.push(allMeasureIds[allMeasures.indexOf(measures[i])]);
    }
    if(typeof comparedMeasureId!=="undefined"){
        for(var i=0;i<measureIds.length;i++){
            if(measures.indexOf(comparedMeasure[i])!==-1){
                measures.splice(measures.indexOf(comparedMeasure[i]),1);
                measureIds.splice(measureIds.indexOf(comparedMeasureId[i]),1);
            
            }
    
        } 
    }
    //    var list = ["Default",""]
    html = html + "<ul id='measure"+chartId+"' class='dropdown-menu'>";
    for(i in measures)
    {
        html+="<li id='compare"+chartId+i+"'  class='dropdown-submenu pull-right1 class='gFontFamily gFontSize13'><a name='"+measures[i]+":"+measureIds[i]+"' onmouseover='enableCompMenu(\""+chartId+"\",this.name,\""+i+"\")' >"+measures[i]+"</a>";
    }
    html+="</ul>";
    $("#measure"+chartId).remove();
    $("#measuresList"+ chartId).append(html);
}

function enableCompMenu(chartId,idArr,m){
  
    var measureName = idArr.split(":")[0];
    var measureId = idArr.split(":")[1];
    
    var html = "";
    var list = ["Default","Custom","Reset"];
    html = html + "<ul id='measureCompMenu"+chartId+"' class='dropdown-menu'>";
    for(var i in list)
    {
        html+="<li id='menu"+chartId+i+"'  class='gFontFamily gFontSize13'><a name='"+measureName+":"+measureId+"' onclick='enableMenu"+list[i]+"(\""+chartId+"\",this.name)' >"+list[i]+"</a>";
    }
    html+="</ul>";
    $("#measureCompMenu"+chartId).remove();
    $("#compare"+ chartId+m).append(html);
}

function enableMenuDefault(chartId,idArr){
    var measureName = idArr.split(":")[0];
    var measureId = idArr.split(":")[1];
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
    //    var timeDetails =   JSON.parse($("#timeDetailsArray").val());
    var globalMeasures = JSON.parse(parent.$("#measure").val());
    var globalMeasureIds = JSON.parse(parent.$("#measureIds").val());
    var globalAggregation = JSON.parse(parent.$("#aggregation").val());
    var measure = chartDetails["meassures"];
    var chartMeasureIds = chartDetails["meassureIds"];
    var chartAggregation = chartDetails["aggregation"];
    var aggIndex = chartMeasureIds.indexOf(measureId);
    var aggType = chartAggregation[aggIndex];
    var comparedMeasure = [];
    var comparedMeasureId = [];
    var comparedMeasureAgg = [];
    if(chartData[chartId]["chartType"]==='Stacked-KPI' || chartData[chartId]["chartType"]==='Trend-KPI'){
        for(var i1 in chartMeasureIds){
            comparedMeasure.push(measure[i1]+" (PRIOR)");
            comparedMeasureId.push(chartMeasureIds[i1]+"_PRIOR");
            comparedMeasureAgg.push(aggType);
            measure.push(measure[i1]+" (PRIOR)");
            globalMeasures.push(measure[i1]+" (PRIOR)");
            chartMeasureIds.push(chartMeasureIds[i1]+"_PRIOR");
            globalMeasureIds.push(chartMeasureIds[i1]+"_PRIOR")
            chartAggregation.push(aggType);
            globalAggregation.push(aggType);
        }
    }
    else{
    comparedMeasure.push(measureName+" (PRIOR)");
    comparedMeasureId.push(measureId+"_PRIOR");
    comparedMeasureAgg.push(aggType);
    measure.push(measureName+" (PRIOR)");
    globalMeasures.push(measureName+" (PRIOR)");
    chartMeasureIds.push(measureId+"_PRIOR");
    globalMeasureIds.push(measureId+"_PRIOR")
    chartAggregation.push(aggType);
    globalAggregation.push(aggType);
    }
    chartDetails["meassures"] = measure;
    chartDetails["meassureIds"] = chartMeasureIds;
    chartDetails["aggregation"] = chartAggregation;
    chartDetails["comparisonType"] = "Default";
    chartDetails["enableComparison"] = "true";
    chartDetails["comparedMeasure"]=comparedMeasure;
    chartDetails["comparedMeasureId"]=comparedMeasureId;
    chartDetails["comparedMeasureAgg"]=comparedMeasureAgg;
    parent.$("#measure").val(JSON.stringify(globalMeasures));
    parent.$("#measureIds").val(JSON.stringify(globalMeasureIds));
    parent.$("#aggregation").val(JSON.stringify(globalAggregation));
    parent.$("#chartData").val(JSON.stringify(chartData));
    var ctxPath=parent.$("#ctxpath").val();
    $.ajax({
        type:'POST',
        async: false,
        data:parent.$("#graphForm").serialize()+"&reportId="+$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartID="+chartId+"&chartFlag=true",
        url: ctxPath+"/reportViewer.do?reportBy=drillCharts",
        success: function(data){
             var meta = JSON.parse(JSON.parse(data)["meta"]);
//                            $("#viewby").val(JSON.stringify(meta["viewbys"]));
                            $("#measure").val(JSON.stringify(meta["measures"]));
                            $("#chartData").val(JSON.stringify(meta["chartData"])); 
            generateSingleChart(data,chartId);
        }
        
    })
   
    
}

function enableMenuReset(chartId,idArr){
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
    var comparedMeasure = chartDetails["comparedMeasure"];
    var comparedMeasureId = chartDetails["comparedMeasureId"];
    var comparedMeasureAgg = chartDetails["comparedMeasureAgg"];
    var globalMeasures = JSON.parse(parent.$("#measure").val());
    var globalMeasureIds = JSON.parse(parent.$("#measureIds").val());
    var globalAggregation = JSON.parse(parent.$("#aggregation").val());
    var measureName = chartDetails["meassures"];
    var measureId = chartDetails["meassureIds"];
    var aggregation = chartDetails["aggregation"];
    
    for(var i=0;i<measureId.length;i++){
        if(measureName.indexOf(comparedMeasure[i])!==-1){
            measureName.splice(measureName.indexOf(comparedMeasure[i]),1);
            measureId.splice(measureId.indexOf(comparedMeasureId[i]),1);
            aggregation.splice(aggregation.indexOf(comparedMeasureAgg[i]),1);
            
        }
    
    }
    for(var i=0;i<globalMeasureIds.length;i++){
        if(globalMeasures.indexOf(comparedMeasure[i])!==-1){
            globalMeasures.splice(globalMeasures.indexOf(comparedMeasure[i]),1);
            globalMeasureIds.splice(globalMeasureIds.indexOf(comparedMeasureId[i]),1);
            globalAggregation.splice(globalAggregation.indexOf(comparedMeasureAgg[i]),1);
            
        }
    
    }

    chartDetails["meassures"]=measureName;
    chartDetails["meassureIds"]=measureId;
    chartDetails["aggregation"]=aggregation;
    chartDetails["enableComparison"]="false";
    parent.$("#measure").val(JSON.stringify(globalMeasures));
    parent.$("#measureIds").val(JSON.stringify(globalMeasureIds));
    parent.$("#aggregation").val(JSON.stringify(globalAggregation));
    parent.$("#chartData").val(JSON.stringify(chartData));
    var ctxPath=parent.$("#ctxpath").val();
    $.ajax({
        type:'POST',
        async: false,
        data:$("#graphForm").serialize()+"&reportId="+$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartID="+chartId+"&chartFlag=true",
        url: ctxPath+"/reportViewer.do?reportBy=drillCharts",
        success: function(data){
            generateSingleChart(data,chartId);
        }
        
    })
}


function generateComparison(chartId,idArr){
      parent.$("#tempDashletDiv").dialog('close');
     var timeComparisonArray=[];
    var list=[];
    timeComparisonArray.push("MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD","MOM","MOYM","QOQ","QOYQ","YOY","MOMPer","MOYMPer","QOQPer","QOYQPer","YOYPer");
    for(var i in timeComparisonArray){
        if(parent.document.getElementById(timeComparisonArray[i]).checked){
            list.push(timeComparisonArray[i]);
            }
        }
//    var chartData=JSON.parse(parent.$("#chartData").val())
   var measureName = idArr.split(":")[0];
    var measureId = idArr.split(":")[1];
    var chartData = JSON.parse($("#chartData").val());
    chartData[chartId]["customTimeComparisons"]=list;
    var chartDetails = chartData[chartId];
//    var list = ["MTD","QTD","YTD"];
    var globalMeasures = JSON.parse(parent.$("#measure").val());
    var globalMeasureIds = JSON.parse(parent.$("#measureIds").val());
    var globalAggregation = JSON.parse(parent.$("#aggregation").val());
    var measure = chartDetails["meassures"];
    var chartMeasureIds = chartDetails["meassureIds"];
    var chartAggregation = chartDetails["aggregation"];
    var aggIndex = chartMeasureIds.indexOf(measureId);
    var aggType = chartAggregation[aggIndex];
    var comparedMeasure = [];
    var comparedMeasureId = [];
    var comparedMeasureAgg = [];
 if(chartDetails["chartType"]==="Stacked-KPI" || chartDetails["chartType"]==="Column-Pie" || chartDetails["chartType"]==="Column-Donut"){
        for(var j in measure){
        measureName=measure[j];
        measureId=chartMeasureIds[j];
        aggType=chartAggregation[j];
   for(var i in list){
       
       comparedMeasure.push(measureName+" ("+list[i]+")");
    comparedMeasureId.push(measureId+"_"+list[i]);
    comparedMeasureAgg.push(aggType);
    measure.push(measureName+" ("+list[i]+")");
    globalMeasures.push(measureName+" ("+list[i]+")");
    chartMeasureIds.push(measureId+"_"+list[i]);
    globalMeasureIds.push(measureId+"_"+list[i])
    chartAggregation.push(aggType);
    globalAggregation.push(aggType);
   }
    }
     
 }else{
      for(var i in list){
       
       comparedMeasure.push(measureName+" ("+list[i]+")");
    comparedMeasureId.push(measureId+"_"+list[i]);
    comparedMeasureAgg.push(aggType);
    measure.push(measureName+" ("+list[i]+")");
    globalMeasures.push(measureName+" ("+list[i]+")");
    chartMeasureIds.push(measureId+"_"+list[i]);
    globalMeasureIds.push(measureId+"_"+list[i])
    chartAggregation.push(aggType);
    globalAggregation.push(aggType);
   }
 }
 

  
     chartDetails["meassures"] = measure;
    chartDetails["meassureIds"] = chartMeasureIds;
    chartDetails["aggregation"] = chartAggregation;
    chartDetails["comparisonType"] = "Custom";
    chartDetails["enableComparison"] = "true";
    chartDetails["comparedMeasure"]=comparedMeasure;
    chartDetails["comparedMeasureId"]=comparedMeasureId;
    chartDetails["comparedMeasureAgg"]=comparedMeasureAgg;
    parent.$("#measure").val(JSON.stringify(globalMeasures));
    parent.$("#measureIds").val(JSON.stringify(globalMeasureIds));
    parent.$("#aggregation").val(JSON.stringify(globalAggregation));
    parent.$("#chartData").val(JSON.stringify(chartData));
    var ctxPath=parent.$("#ctxpath").val();
    $.ajax({
        type:'POST',
        async: false,
        data:$("#graphForm").serialize()+"&reportId="+$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartID="+chartId+"&chartFlag=true",
        url: ctxPath+"/reportViewer.do?reportBy=drillCharts",
        success: function(data){
                var meta = JSON.parse(JSON.parse(data)["meta"]);
//                            $("#viewby").val(JSON.stringify(meta["viewbys"]));
                            $("#measure").val(JSON.stringify(meta["measures"]));
                            $("#chartData").val(JSON.stringify(meta["chartData"])); 
            generateSingleChart(data,chartId);
        }
        
    })
}



function enableMenuCustom(chartId,idArr){
    var divHeight = ($(window).height() - $(window).height() / 6);
    var divWidth = ($(window).width() - ($(window).width() / 1.4));
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
    parent.$("#tempDashletDiv").dialog({
        autoOpen: false,
        height: divHeight,
        width: divWidth,
        position: 'justify',
        modal: true,
        title: "Comparable Values: " //+ chartDetails["Name"].
    });
    var timeComparisonArray=[];
    var timeComparisonArrayLabels=[];
    timeComparisonArray.push("MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD","MOM","MOYM","QOQ","QOYQ","YOY","MOMPer","MOYMPer","QOQPer","QOYQPer","YOYPer");
    timeComparisonArrayLabels.push("MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD","MOM","MOYM","QOQ","QOYQ","YOY","MOM %","MOYM %","QOQ %","QOYQ %","YOY %");
    var html='';
    
    html += "<div class='expandDivComp expandDiv' name='expand' width='13%' onclick='expandDivComp(\""+chartId+"\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
    //     adaa   html += "<div width='100%' style='background-color:lightgrey;padding-bottom:20px;'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
    var timeComparisonConter=0;
    var appliedTimeComparisons=chartDetails["customTimeComparisons"];
//    if(typeof appliedTimeComparisons!='undefined'){
//        for(var i in appliedTimeComparisons){
//            if(Object.keys(appliedTimeComparisons[i])[0]===appliedTimeComparisons[i][Object.keys(appliedTimeComparisons[i])[0]]){
//                timeComparisonConter++;
//            }
//        }
//    }
    if (typeof appliedTimeComparisons==='undefined' || appliedTimeComparisons.length!==timeComparisonArray.length) {
        html += "<input type='checkbox' class='ckbox' id='selectAll' onclick='selectAllTimeComparisons(this.id,\""+chartId+"\")'/>";
    } else {
        html += "<input type='checkbox' class='ckbox' id='selectAll' checked onclick='selectAllTimeComparisons(this.id,\""+chartId+"\")'/>";
    }
    html += "<span>&nbsp;Select All</span></label></div>";
    html += "</div>";
    
    html += "<div id='localFilterComp' class='collapseDiv' style='display:none'><table style='' width='100%'>";
    for (var k=0;k<timeComparisonArray.length;k++){
        html += "<tr><td ><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
        if(typeof appliedTimeComparisons!=='undefined' && appliedTimeComparisons.indexOf(timeComparisonArray[k])!=-1){
            html +="<input type='checkbox' id='"+timeComparisonArray[k]+"' checked>";
            html += "<span class='box'>&nbsp;" + timeComparisonArrayLabels[k] + "</span>";
        }else{
            html +="<input type='checkbox' id='"+ timeComparisonArray[k]+"' >";
            html += "<span class='box'>&nbsp;" + timeComparisonArrayLabels[k] + "</span>";
        }
        html += "</span></label></td></tr>";
               
    }
    html += "</table></div>";
    
    html += "<table><tr><td><input type='button' value='done' onClick='generateComparison(\""+chartId+"\",\""+idArr+"\")' /></td>";
    html +="</tr></table>";
    
    parent.$("#tempDashletDiv").html(html);
    parent.$("#tempDashletDiv").dialog('open');
    
}



function TrendComboDrill(measureName,chartID){
  var measure  = JSON.parse(parent.$("#measure").val());
  var measureIds = JSON.parse(parent.$("#measureIds").val());
  var aggregation = JSON.parse(parent.$("#aggregation").val());
  var measureId = "";
  var measIds = []
  var measName = []
  var aggType = []
   var chartData = JSON.parse(parent.$("#chartData").val());
    for(var i in measure){
     if(measure[i].indexOf(measureName)!==-1){
      measureId = measureIds[i]
      measIds.push(measureIds[i])
      measName.push(measure[i])
      aggType.push(aggregation[i])
     }
    }
    //alert(measureId+""+measureName)
     $.ajax({
            async:false,
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:$("#ctxpath").val() +"/reportViewerAction.do?reportBy=GroupedMeasureCheck&measureId="+measureId,
                success:function(data){
                var childMeasure = JSON.parse(data)
                if(childMeasure.length>0){

                var childMeasureName=[]
                var childAggType=[]
                for(var i in measureIds){
                for(var j in childMeasure){
                if(measureIds[i].indexOf(childMeasure[j])!==-1){
                  childMeasureName.push(measure[i])
                  childAggType.push(aggregation[i])
                }
                }
                }
                chartData[chartID]["meassures"] = childMeasure
                chartData[chartID]["meassureIds"] = childMeasure
                chartData[chartID]["aggregation"] = childMeasure
                parent.$("#chartData").val(JSON.stringify(chartData));
                 chartTypeFunction(chartID,chartData[chartID]["chartType"],chartData[chartID]["Name"]);
                }else{
                  chartData[chartID]["meassures"] = measName
                chartData[chartID]["meassureIds"] = measIds
                chartData[chartID]["aggregation"] = aggType
                parent.$("#chartData").val(JSON.stringify(chartData));
                 chartTypeFunction(chartID,chartData[chartID]["chartType"],chartData[chartID]["Name"]);
                }
                }})
 }

 function getParentMeasure(chartID){
     var chartData = JSON.parse(parent.$("#chartData").val());
     var measure  = JSON.parse(parent.$("#measure").val());
     var measureIds = JSON.parse(parent.$("#measureIds").val());
     var aggregation = JSON.parse(parent.$("#aggregation").val());
     var measureArr = []
     var measureIdsArr = []
     var aggType = []
     //alert("hiii"+chartData[chartID]["ParentMeasures"])
     if(typeof chartData[chartID]["ParentMeasures"]!="undefined" && chartData[chartID]["ParentMeasures"]!=""){

         for(var i in measure){
         for(var j in chartData[chartID]["ParentMeasures"]){
         if(measure[i].indexOf(chartData[chartID]["ParentMeasures"][j])!=-1){
          measureArr.push(measure[i])
          measureIdsArr.push(measureIds[i])
          aggType.push(aggregation[i])
         }
         }
         }
       chartData[chartID]["meassures"] = measureArr
       chartData[chartID]["meassureIds"] = measureIdsArr
       chartData[chartID]["aggregation"] = aggType
       parent.$("#chartData").val(JSON.stringify(chartData));
       chartTypeFunction(chartID,chartData[chartID]["chartType"],chartData[chartID]["Name"]);
     }else{
       chartTypeFunction(chartID,chartData[chartID]["chartType"],chartData[chartID]["Name"]);
     }
 }
 
 
 function drillWithinchartReport(id,div){
     
     var chartData = JSON.parse(parent.$("#chartData").val());
  

if(typeof chartData[div]["enableGraphDrill"] !=="undefined" && chartData[div]["enableGraphDrill"] !=="" && chartData[div]["enableGraphDrill"]==="enableGraphDrill"){
    parent.enableAdvanceDrill = true;
    $("#my_tooltip").hide();
    drillWithinTabs(id,div);
}else if(typeof chartData[div]["drillType"] !=="undefined" && chartData[div]["drillType"] !=="" && chartData[div]["drillType"]==="reportDrill"){
drilltoReports(id,div);
}else {
    alert("No Report Assign To Header")
}
 }
 function getAbsoluteValue(data,columns,measureArray){

    var currData = [];
    for(var i in data){
        var map = {};
        map[columns[0]] = data[i][columns[0]];
        for(var j in measureArray){
            map[measureArray[j]] = checkNegative(data[i][measureArray[j]]);
        }
        currData.push(map);
    }
     return currData;
 }


 function checkNegative(msrValue){

     if(parseFloat(msrValue)<0){
         return -1 * parseFloat(msrValue);
     }else {
         return msrValue;
     }

 }
