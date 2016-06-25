/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mayank
 */
var strOperators = ["<", ">", "<=", ">=", "=", "!=", "<>"];
var scheduleday = ["Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"];
    var sday = ["2", "3", "4", "5", "6", "7", "1"];
   // var frequency = ["Daily", "Weekly", "Monthly", "Hourly"];
    var frequency = ["Daily", "Monthly"];
    var medium = ["High", "Low", "Medium"];
    var usermailId;
    var isSendAnyWays;
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
   
    if(typeof chartDetails["comparedMeasureId"] !=="undefined" && chartDetails["comparedMeasureId"].length !=0){
        return alert("Default Comparison Already Enabled.")
    }
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
    
     for(var i=0;i<comparedMeasureId.length;i++){
        if(comparedMeasure.indexOf(comparedMeasure[i])!==-1){
            comparedMeasure.splice(comparedMeasure.indexOf(comparedMeasure[i]),1);
            comparedMeasureId.splice(comparedMeasureId.indexOf(comparedMeasureId[i]),1);
            comparedMeasureAgg.splice(comparedMeasureAgg.indexOf(comparedMeasureAgg[i]),1);
            
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
    chartDetails["comparedMeasure"] = comparedMeasure;
    chartDetails["comparedMeasureId"] = comparedMeasureId;
    chartDetails["comparedMeasureAgg"] =  comparedMeasureAgg;   
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
    timeComparisonArray.push("MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD","WTD","PWTD","PYWTD","MOM","MOYM","QOQ","QOYQ","YOY","WOW","WOYW","MOMPer","MOYMPer","QOQPer","QOYQPer","YOYPer","WOWPer","WOYWPer","PT","GT");
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



function enableMenuCustom(chartId,idArr,chWidth,chHeight,flag){
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
    timeComparisonArray.push("MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD","WTD","PWTD","PYWTD","MOM","MOYM","QOQ","QOYQ","YOY","WOW","WOYW","MOMPer","MOYMPer","QOQPer","QOYQPer","YOYPer","WOWPer","WOYWPer","PT","GT");
    timeComparisonArrayLabels.push("MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD","WTD","PWTD","PYWTD","MOM","MOYM","QOQ","QOYQ","YOY","WOW","WOYW","MOM %","MOYM %","QOQ %","QOYQ %","YOY %","WOW %","WOYW %","PT","GT");
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
    if(chartDetails["chartType"]==="KPI-Dashboard" || chartDetails["chartType"]==="XYZ-Dashboard"){
         html += "<table><tr><td><input type='button' value='done' onClick='generateKPIDashboard(\""+chartId+"\",\""+chWidth+"\",\""+chHeight+"\",\"\","+flag+")' /></td>";
    }else{
    
    html += "<table><tr><td><input type='button' value='done' onClick='generateComparison(\""+chartId+"\",\""+idArr+"\")' /></td>";
    }
    html +="</tr></table>";
    
    parent.$("#tempDashletDiv").html(html);
    parent.$("#tempDashletDiv").dialog('open');
    
}



function TrendComboDrill(measureName,chartID,parentMeasFlagCurrencySym){
  var measure  = JSON.parse(parent.$("#measure").val());
  var measureIds = JSON.parse(parent.$("#measureIds").val());
  var aggregation = JSON.parse(parent.$("#aggregation").val());
  var measureId = "";
  var radius = 0;
  var measIds = []
  var measName = []
  var aggType = []
   var chartNum=  chartID.replace ( /[^\d.]/g, '' );
    var chWidth=$("#divchart"+chartNum).width();
    var chHeight=$("#divchart"+chartNum).height();
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
	         var childMeasureIds = []
                var childMeasureName=[]
                var childAggType=[]
                 for(var i in childMeasure){ 
                for(var j in measureIds){
		//alert("::"+childMeasure[i]+"::"+measureIds[j])
                if(childMeasure[i]!="" && measureIds[j] ==childMeasure[i]){
		//	alert(measure[j])
                  childMeasureName.push(measure[j])
                  childAggType.push(aggregation[j])
		   childMeasureIds.push(measureIds[j]) 
                }

                }
                }
//alert(childMeasureName+"::"+childMeasureIds+"::"+childAggType)
                chartData[chartID]["meassures"] = childMeasureName
                chartData[chartID]["meassureIds"] = childMeasureIds
                chartData[chartID]["aggregation"] = childAggType;
		 $("#driver").val("");
	       $("#drills").val("");
		$("#drillFormat").val("none");

                parent.$("#chartData").val(JSON.stringify(chartData));
		//  alert(chartData[chartID]["meassures"]+"::"+chartData[chartID]["meassureIds"]+"::"+chartData[chartID]["aggregation"])
$.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
              //  $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                function(data){
			//alert(data)
			var jsonData = JSON.parse(data)["data"]
			
		if(typeof parentMeasFlagCurrencySym!="undefined" && parentMeasFlagCurrencySym!="undefined" && parentMeasFlagCurrencySym!=""){ 
		 buildTrendCombo(chartID,JSON.parse(jsonData)[chartID], chartData[chartID]["viewBys"], chartData[chartID]["meassures"],chWidth,chHeight,radius,"childMeasureFlag",parentMeasFlagCurrencySym)
		}else{
		 buildTrendCombo(chartID,JSON.parse(jsonData)[chartID], chartData[chartID]["viewBys"], chartData[chartID]["meassures"],chWidth,chHeight,radius,"childMeasureFlag",measureName)
		}
                
                })
                // chartTypeFunction(chartID,chartData[chartID]["chartType"],chartData[chartID]["Name"]);
                }else{
			 var measNam1 = []
                      var measId1 = []
                      var measAgg1 = []
                  measNam1.push(measName[0])
                  measId1.push(measIds[0])
                  measAgg1.push(aggType[0]) 
                  chartData[chartID]["meassures"] = measNam1
                chartData[chartID]["meassureIds"] = measId1
                chartData[chartID]["aggregation"] = measAgg1
		$("#driver").val("");
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
     var radius = 0;
     var chartNum=  chartID.replace ( /[^\d.]/g, '' );
    var chWidth=$("#divchart"+chartNum).width();
    var chHeight=$("#divchart"+chartNum).height();
     //alert("hiii"+chartData[chartID]["ParentMeasures"])
     if(typeof chartData[chartID]["ParentMeasures"]!="undefined" && chartData[chartID]["ParentMeasures"]!=""){
         for(var j in chartData[chartID]["ParentMeasures"]){
         for(var i in measure){
         if(chartData[chartID]["ParentMeasures"][j].indexOf(measure[i])!=-1){
          measureArr.push(measure[i])
          measureIdsArr.push(measureIds[i])
          aggType.push(aggregation[i])
         }
         }
         }
               var temp1=[];
                 label:for(var ih=0;ih<measureIdsArr.length;ih++){
        for(var jh=0; jh<temp1.length;jh++ ){//check duplicates
            if(temp1[jh]==measureIdsArr[ih])//skip if already present
               continue label;
        }

        temp1[temp1.length] = measureIdsArr[ih];
          }
  
                  var meastemp2 = [];
		  var aggtemp3 = [];
                 label:for(var ih=0;ih<measureArr.length;ih++){
        for(var jh=0; jh<meastemp2.length;jh++ ){//check duplicates
            if(meastemp2[jh]==measureArr[ih])//skip if already present
               continue label;
        }

        meastemp2[meastemp2.length] = measureArr[ih];
aggtemp3.push(aggType[ih])
          }


      //      alert(meastemp2+"::::"+temp1+"::::"+aggtemp3)
       chartData[chartID]["meassures"] = meastemp2
       chartData[chartID]["meassureIds"] = temp1
       chartData[chartID]["aggregation"] = aggtemp3;
	   var drillsvalue = {};
     //  alert(chartData[chartID]["meassures"])
	  $("#driver").val("");
	       $("#drills").val("")
		$("#drillFormat").val("none")

       parent.$("#chartData").val(JSON.stringify(chartData));
     //  chartTypeFunction(chartID,chartData[chartID]["chartType"],chartData[chartID]["Name"]);
     }else{
     //  chartTypeFunction(chartID,chartData[chartID]["chartType"],chartData[chartID]["Name"]);
     }
     $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
     function(data){
          var jsonData = JSON.parse(data)["data"]
		//	alert(JSON.parse(jsonData)[chartID])
                 buildTrendCombo(chartID,JSON.parse(jsonData)[chartID], chartData[chartID]["viewBys"], chartData[chartID]["meassures"],chWidth,chHeight,radius,"Pie")     })
 }
 
 
 function drillWithinchartReport(id,div){
     
     var chartData = JSON.parse(parent.$("#chartData").val());
  var idArr = id.split("::")[1];
  var drillType = chartData[div]["drillType"];
   if(typeof idArr!=="undefined" && idArr ==="header" && (typeof drillType!=="undefined" || typeof drillType =="undefined") && drillType!=="reportDrill"){
       alert("This Drill is not allowed");
       return;
   }
  
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
function isAxisChart(chartId){
     var chartData=JSON.parse(parent.$("#chartData").val());
     var chartType=chartData[chartId]["chartType"];
     if(chartType==='Vertical-Bar' || chartType==='Vertical-Negative-Bar' || chartType==="Horizontal-Bar" || chartType==='Filled-Horizontal' || chartType==='StackedBar' || chartType==='StackedBar-%' || chartType==='StackedBarH-%' || chartType==='Horizontal-StackedBar' || chartType==='OverLaid-Bar-Line' || chartType==='MultiMeasure-Bar' || chartType==='StackedBarLine' || chartType==='Multi-Layered-Bar' || chartType==='Line' || chartType==='SmoothLine' || chartType==='MultiMeasure-Line' || chartType==='MultiMeasureSmooth-Line' || chartType==='Cumulative-Line' || chartType==='Cumulative-Bar' || chartType==='Grouped-Bar' || chartType==='GroupedHorizontal-Bar' || chartType==='GroupedStacked-Bar' || chartType==='GroupedStacked-BarLine' || chartType==='GroupedStacked-Bar%' || chartType==='Grouped-Line' || chartType==='Grouped-MultiMeasureBar' || chartType==='OverLaid-Bar-Bubble' || chartType==='XY-Bubble' || chartType==='DualAxis-Bar' || chartType==='Scatter' || chartType==='TopBottom-Chart' || chartType==='Trend-Combo' || chartType==='Trend-Table-Combo'){
         return true;
     }
     else{
         return false;
     }
 }
 
 function containsNegativeValue(data,measureArray,type,isSelectiveMeasure,chartDetails){
    var measLen=0;
    if(typeof type!=='undefined' && type==='single'){
        measLen=1;
    }
    else{
        measLen=measureArray.length;
    }
     for(var i in data){
        for(var k=0;k<measLen;k++){
            if(typeof type!='undefined' && type=='multi' && typeof isSelectiveMeasure!='undefined' && isSelectiveMeasure==='y' ){
                if(k==0){
                    if (!(typeof chartDetails["createBarLine"] === "undefined" || typeof chartDetails["createBarLine"][measureArray[k]] === "undefined" || chartDetails["createBarLine"][measureArray[k]] === "Yes")) {
                        continue;
                    }
                }else{
                    if (typeof chartDetails["createBarLine"] === "undefined" || typeof chartDetails["createBarLine"][measureArray[k]] === "undefined" || chartDetails["createBarLine"][measureArray[k]] === "No") {
                        continue;
                    }
                }
            }
            if(parseInt(data[i][measureArray[k]])<0){
             return true;
         }
     }
    }
     return false;
 }
 function createCustomMeasureForGraphs(elementId,reportId){
     $.ajax({
            async:false,
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:$("#ctxpath").val() +"/reportViewerAction.do?reportBy=createCustomMeasureForGraphs&elementId="+elementId+"&reportId="+reportId,
                success:function(measureInfo){
                    var allMeasures=JSON.parse(parent.$("#measure").val());
                    var allMeasureIds=JSON.parse(parent.$("#measureIds").val());
                    var aggType=JSON.parse(parent.$("#aggregation").val());
                    allMeasures.push(JSON.parse(measureInfo)["measureName"]);
                    allMeasureIds.push(JSON.parse(measureInfo)["elementId"]);
                    aggType.push(JSON.parse(measureInfo)["aggType"]);
                    parent.$("#measure").val(JSON.stringify(allMeasures));
                    parent.$("#measureIds").val(JSON.stringify(allMeasureIds));
                    parent.$("#aggregation").val(JSON.stringify(aggType));
                    $("#loading").hide();
                }
     });
 }
 
 function timeDrillURL(div,reportIdtoDrill,drillValues){
     var timeDetails = JSON.parse(parent.$("#timeDetailsArray").val());
     var chartData = JSON.parse(parent.$("#chartData").val());
     var url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&CBOARP"+chartData[div]["dimensions"][0]+"=['"+drillValues+"']&reportDrill=Y&action=open"; 
     var timeType = timeDetails[1];
     var timePattern = timeDetails[3];
     if(typeof timeType!=="undefined" && timeType==="PRG_STD"){
         if(timePattern.toString().toLowerCase()==="month"){
          url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&DDrill=Y&DrillMonth="+drillValues+"&reportDrill=Y&action=open";    
         }else if(timePattern.toString().toLowerCase()==="qtr"){
            url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&DDrill=Y&DrillQtr="+drillValues+"&reportDrill=Y&action=open";      
         }else if(timePattern.toString().toLowerCase()==="year"){
            url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&DDrill=Y&DrillYear="+drillValues+"&reportDrill=Y&action=open";      
         }
     }
     return url;
//     eg:
//     localhost:8084/pi_01_Feb/reportViewer.do?reportBy=viewReport&REPORTID=1989&DDrill=Y&DrillMonth=Sep-2012&reportDrill=Y
 }

function monthDrillURL(div,reportIdtoDrill,drillValues){
     var timeDetails = JSON.parse(parent.$("#timeDetailsArray").val());
//     var chartData = JSON.parse(parent.$("#chartData").val());
     var url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&DDrill=Y&DrillMonth="+drillValues+"&reportDrill=Y&action=open";    
     var timeType = timeDetails[1];
     var timePattern = timeDetails[4];
     if(typeof timeType!=="undefined" && timeType==="PRG_STD"){
//         if(timePattern.toString().toLowerCase()==="month"){
          url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&CBO_PRG_PERIOD_TYPE=Month&DDrill=Y&DrillMonth="+drillValues+"&DDrillAcross=Y&reportDrill=Y&CBO_PRG_COMPARE=Last+Month";    
//         }
     }
     return url;
//     eg:&CBO_PRG_PERIOD_TYPE=Month&DDrill=Y&DrillMonth=Feb-2012&DDrillAcross=Y&reportDrill=Y&CBO_PRG_COMPARE=Last+Year localhost:8084/pi_01_Feb/reportViewer.do?reportBy=viewReport&REPORTID=1989&DDrill=Y&DrillMonth=Sep-2012&reportDrill=Y
 }
 
 function qtrDrillURL(div,reportIdtoDrill,drillValues){
     var timeDetails = JSON.parse(parent.$("#timeDetailsArray").val());
//     var chartData = JSON.parse(parent.$("#chartData").val());
     var url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&DDrill=Y&DrillQtr="+drillValues+"&reportDrill=Y&action=open";
     var timeType = timeDetails[1];
     var timePattern = timeDetails[3];
//alert(JSON.stringify(timeDetails))
     if(typeof timeType!=="undefined" && timeType==="PRG_STD"){
//        if(timePattern.toString().toLowerCase()==="qtr"){
if(typeof timePattern!="undefined" && timePattern=="Year" ){
      parent.$('#gCompq').val("Same Qtr Last Year");
   url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&CBO_PRG_PERIOD_TYPE=Qtr&DDrill=Y&DrillQtr="+drillValues+"&DDrillAcross=Y&reportDrill=Y";       
}else{
     parent.$('#gCompq').val("Last Qtr");
            url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&CBO_PRG_PERIOD_TYPE=Qtr&DDrill=Y&DrillQtr="+drillValues+"&DDrillAcross=Y&reportDrill=Y";      
}        //    url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&CBO_PRG_PERIOD_TYPE=Qtr&DDrill=Y&DrillQtr="+drillValues+"&DDrillAcross=Y&reportDrill=Y&CBO_PRG_COMPARE=Last+Qtr";      
//         }
     }
     return url;
//     eg:
//   REPORTID=1989&CBO_PRG_PERIOD_TYPE=Qtr&DDrill=Y&DrillQtr=Q1-2012&DDrillAcross=Y&reportDrill=Y&CBO_PRG_COMPARE=Last+Qtr
 }
 
 function yearDrillURL(div,reportIdtoDrill,drillValues){
     var timeDetails = JSON.parse(parent.$("#timeDetailsArray").val());
     var chartData = JSON.parse(parent.$("#chartData").val());
     var url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&DDrill=Y&DrillYear="+drillValues+"&reportDrill=Y&action=open";      
     var timeType = timeDetails[1];
     var timePattern = timeDetails[4];
     if(typeof timeType!=="undefined" && timeType==="PRG_STD"){
//        if(timePattern.toString().toLowerCase()==="year"){
            url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&CBO_PRG_PERIOD_TYPE=Year&DDrill=Y&DrillYear="+drillValues+"&DDrillAcross=Y&reportDrill=Y&CBO_PRG_COMPARE=Last+Year";      
//         }
     }
     return url;
//     eg:
//     localhost:8084/pi_01_Feb/reportViewer.do?reportBy=viewReport&REPORTID=1989&DDrill=Y&DrillMonth=Sep-2012&reportDrill=Y
 }
 
 function drillWithinTable(idArr,div,flag,nextViewById){
    $("#viewByList").hide();
    $("#loading").show();
    $("#drillFormat").val("");
    breadCrump = div;
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var chartData = JSON.parse($("#chartData").val());
    if($("#type").val()=="graph" || $("#type").val()=="trend"){
        var drilltype = chartData[div]["drillType"];
        if(typeof drilltype !="undefined" && drilltype==="within"){
            var yaxisFilter = chartData[div]["yaxisrange"];
            if(typeof yaxisFilter!="undefined" && yaxisFilter!=""){
                yaxisFilter["YaxisRangeType"] = "Default";
            }
            var y1axisFilter = chartData[div]["y1axisrange"];  // add by mayank sharma
            if(typeof y1axisFilter!="undefined" && y1axisFilter!=""){
                y1axisFilter["Y1axisRangeType"] = "Default";
            }
            
            var drills = {};
            if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
                try{
                    drills=JSON.parse($("#drills").val());
                }catch(e){
                }
            }
            var drillValues = [];

            drillValues.push(idArr.split(":"))
            var drillMap = [];
            drillMap.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillMap;
            //            drills[chartData[div]["viewBys"][0]] = idArr.split(":")[0];
            $("#driver").val(div);
            flag = false;

            try{
                for(var i=0;i<chartData[div]["dimensions"].length;i++){
                    if(chartData[div]["viewIds"][0]===chartData[div]["dimensions"][i]){
                        var view = [];
                        var viewName = [];
//                        if(typeof chartData[div]["dimensions"][i+1] !="undefined"){
                            view.push(nextViewById);
                            flag = true;
                            for(var m=0;m<view.length;m++){
                                viewName.push(viewOvName[viewOvIds.indexOf(view[m])]);
                                chartData[div]["viewIds"]= view;
                                chartData[div]["viewBys"]= viewName;
                            }
//                        }
//                        else{
//                            $("#loading").hide();
//                            alert("Please select more views");
//                            return "";
//                        }
                        break;
                    }
                }
            }
            catch(e){
                $("#loading").hide();
                //            if(flag){
                alert("Please select more views");
            //            }
            }
         
            if(flag){
                $("#chartData").val(JSON.stringify(chartData));
                $("#drills").val(JSON.stringify(drills));
                $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                    function(data){
                        //                        generateChart(data,div);
                        generateSingleChart(data,div);
                    });
            }
        }
    }

}

 function generateKPIDashboard(chartId,chWidth,chHeight,newcomparisonMeasureList,flag){
if(typeof flag==="undefined" || flag==="undefined"){
    flag=true;
}
     var timeComparisonArray=[];
    var list=[];
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
    var savedList;
    if(chartDetails["isKPI"]!=="undefined" && chartDetails["isKPI"]==true){
        savedList= chartDetails["customTimeComparisons"];
        list=[];
    }else{
           parent.$("#tempDashletDiv").dialog('close');
    }
    timeComparisonArray.push("MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD","WTD","PWTD","PYWTD","MOM","MOYM","QOQ","QOYQ","YOY","WOW","WOYW","MOMPer","MOYMPer","QOQPer","QOYQPer","YOYPer","WOWPer","WOYWPer");
    for(var i in timeComparisonArray){
        try{
        if(parent.document.getElementById(timeComparisonArray[i]).checked){
            list.push(timeComparisonArray[i]);
            }
        }catch(e){}
        }
     if(chartDetails["isKPI"]!=="undefined" && chartDetails["isKPI"]==true && flag){
        list=[];
        for(var k in savedList){
        for(var i in timeComparisonArray){
                
                    if(timeComparisonArray[i]==savedList[k]){
                        
                        list.push(savedList[k]);
                    }
                }
         }
     }
           if(!flag){
          parent.$("#tempDashletDiv").dialog('close');
     }
        if(typeof newcomparisonMeasureList!=="undefined" && newcomparisonMeasureList!=="undefined" && newcomparisonMeasureList.length>0){
//            alert("previous list "+list);
            list=newcomparisonMeasureList;
//            alert(typeof list+"new list "+list);
        }        
        
//    var chartData=JSON.parse(parent.$("#chartData").val())
   var measureName;
    var measureId;
//    alert(JSON.stringify(list))
    chartDetails["customTimeComparisons"]=list;
    chartDetails["isKPI"] = true;
//    var list = ["MTD","QTD","YTD"];
    var globalMeasures = JSON.parse(parent.$("#measure").val());
    var globalMeasureIds = JSON.parse(parent.$("#measureIds").val());
    var globalAggregation = JSON.parse(parent.$("#aggregation").val());
    var measure = chartDetails["meassures"];
    var chartMeasureIds = chartDetails["meassureIds"];
    var chartAggregation = chartDetails["aggregation"];
//    var aggIndex = chartMeasureIds.indexOf(measureId);
    var aggType;
         var comparedMeasure = chartDetails["comparedMeasure"];
    var comparedMeasureId = chartDetails["comparedMeasureId"];
    var comparedMeasureAgg =chartDetails["comparedMeasureAgg"];
//    if(chartDetails["chartType"]!="XYZ-Dashboard"){
    for(var k in comparedMeasure){
measure.splice(measure.indexOf(comparedMeasure[k]),1);
chartMeasureIds.splice(chartMeasureIds.indexOf(comparedMeasureId[k]),1);
chartAggregation.splice(chartAggregation.indexOf(comparedMeasureAgg[k]),1);
    }
//    }
//    if(typeof comparedMeasure=="undefined" || comparedMeasure.length!=list.length){
          comparedMeasure=[];
           comparedMeasureId=[];
           comparedMeasureAgg=[];
//    }/
// if(chartDetails["chartType"]==="Stacked-KPI" || chartDetails["chartType"]==="Column-Pie" || chartDetails["chartType"]==="Column-Donut"){
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
     
// }
 
   chartDetails["meassures"] = measure;
    chartDetails["meassureIds"] = chartMeasureIds;
    chartDetails["aggregation"] = chartAggregation;
    chartDetails["comparisonType"] = "Custom";
    chartDetails["enableComparison"] = "true";
    chartDetails["comparedMeasure"]=comparedMeasure;
    chartDetails["comparedMeasureId"]=comparedMeasureId;
    chartDetails["comparedMeasureAgg"]=comparedMeasureAgg;
    parent.$("#defaultMeasure").val(JSON.stringify(globalMeasures));
    parent.$("#defaultMeasureId").val(JSON.stringify(globalMeasureIds));
//    parent.$("#aggregation").val(JSON.stringify(globalAggregation));
    parent.$("#chartData").val(JSON.stringify(chartData));
      var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
     if(chartDetails["chartType"]=="KPI-Dashboard"){
      $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
                success:function(kpidata){
                    
      buildKPIDashboard(chartId,JSON.parse(kpidata)["data"],JSON.parse(kpidata)["comparedMeasure"],chWidth,chHeight);
                }})
            }else{
              $.ajax({
            async:false,
            type:"POST",
            data :$("#graphForm").serialize(),
            url: $("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&chartID="+chartId+"&isEdit=Y"+"&editId="+chartId+"&action=localfilterGraphs"+"&chartFlag=true"+"&reportId="+parent.$("#graphsId").val(),
            success: function(localData){
            var data = JSON.parse(JSON.parse(localData)["data"]);
             var meta = JSON.parse(JSON.parse(localData)["meta"]);
                         parent.$("#chartData").val(JSON.stringify(meta["chartData"]));
//var jsondata1 = JSON.parse(localData)["data"];
//            generateSingleChart(localData,chartId);
              buildKPIViewDashboard(chartId,data,chWidth,chHeight);
                        }
                        });    
                
            }
 }
 
 function getDataForSingleView(chartId){
     var chartData = JSON.parse(parent.$("#chartData").val());
     var chartMeta = {};
     var chartDetails = chartData[chartId];
     var viewBy = chartDetails["viewBys"];
     var viewId = chartDetails["viewIds"];
     var measureId = chartDetails["meassureIds"];
     var measure = chartDetails["meassures"];
     var aggregation = chartDetails["aggregation"];
     var measList = [];
     var gtMeasList = [];
     var measIdList = [];
     var gtMeasIdList = [];
     var aggList= [];
     var gtAggList= [];
     var viewList = [];
     var viewIdList = [];
     var gtFlag = false;
     if(chartDetails["groupedBarWith"]!=="undefined" && chartDetails["groupedBarWith"]["GT"]=="GT"){
         gtFlag = true;
     }
     for(var k in measureId){
         if(k!=0){
             measList.push(measure[k]);
             measIdList.push(measureId[k]);
             aggList.push(aggregation[k]);
         }else{
              gtMeasList.push(measure[k]);
             gtMeasIdList.push(measureId[k]);
             gtAggList.push(aggregation[k]);
         }
     }
     for(var j in viewId){
         if(j==0){
             viewList.push(viewBy[j]);
             viewIdList.push(viewId[j]);
         }
     }
    
     var chartMetaDetails = {};
     chartMetaDetails["viewBys"]=viewList;
     chartMetaDetails["viewIds"]=viewIdList;
     if(gtFlag){
         chartMetaDetails["meassureIds"]=gtMeasIdList;
     chartMetaDetails["meassures"]=gtMeasList;
     chartMetaDetails["aggregation"]=gtAggList;
     }else{
     chartMetaDetails["meassureIds"]=measIdList;
     chartMetaDetails["meassures"]=measList;
     chartMetaDetails["aggregation"]=aggList;
     }
     chartMetaDetails["chartType"]= "Pie"
     chartMeta[chartId] = chartMetaDetails;
     parent.$("#chartMeta").val(JSON.stringify(chartMeta));
     var jsonData;
     var emptyList = [];
      $.ajax({
                    async:false,
                    type:"POST",
                    data:
                    parent.$('#graphForm').serialize(),
                    url:  $("#ctxpath").val()+"/reportViewerAction.do?reportBy=getGraphData&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(),

                    success:function(data) {
                    
              jsonData=  JSON.parse(data);
                    }
       
            });
            if(typeof jsonData !=="undefined"){
                
            return jsonData[chartId];
 }
            else{
              return emptyList;  
            } 
 }
  /*Added by Ashutosh*/
 function resequenceKPIDashboard(chartId){
    var chWidth=$("#divchart"+chartId.replace ( /[^\d.]/g, '' )).width();
    var chHeight=$("#divchart"+chartId.replace ( /[^\d.]/g, '' )).height();
//    alert(chWidth+"edgge"+chWidth)
    $("#reportSequence").dialog({
        autoOpen: false,
        height: 350,
        width: 350,
        position: 'justify',
        modal: true,
        title: 'Resequence'
});
    var html="";
    
    var chartData = JSON.parse(parent.$("#chartData").val());
    var comparisonMeasureList =chartData[chartId]["customTimeComparisons"]
    html += "<ul id='sortable' style='padding-top:0px;'>";
    for (var i=0;i<comparisonMeasureList.length;i++) {
        html += "<li class='btn-custom3' style='background-color: #3CC; margin-bottom: 1px; height: 16px; font-size: 12px; padding: 3px;' id='" + i + "'   )\">\n\
<span style='cursor: pointer;width=100%;margin-left:4px;'>" + comparisonMeasureList[i] + "</span></li>";
    }
    html += "</ul>";
    html += "<input type='button' value='Save' onclick='changeSequenceKPIDashboard(\""+chartId+"\",\""+chWidth+"\",\""+chHeight+"\",\""+comparisonMeasureList+"\")'>";
//    html += "<input type='button' value='Save' onclick='generateKPIDashboard(\""+chartId+"\",\""+chWidth+"\",\""+chHeight+"\",\""+comparisonMeasureList+"\")'>";
//    html += "<input type='button' value='Save' onclick='generateKPIDashboard(\""+chartId+"\",\""+chWidth+"\",\""+chHeight+"\",\""+JSON.parse($("#sortable").val())+"\")'>";
    $("#reportSequence").html("");
    $("#reportSequence").append(html);
    $("#reportSequence").dialog('open');
    setDropable("folderSeq");
}
 /*Added by Ashutosh*/
function changeSequenceKPIDashboard(chartId,chWidth,chHeight,comparisonMeasureList){
    var newSequence=[];
    var newComparisonMeasureList=[];
    var chartData = JSON.parse(parent.$("#chartData").val());
   $('#sortable li').map(function(i,n) {
        newSequence.push($(n).attr('id'));
    }).get().join(',');
//    alert("newSequence"+newSequence);
    for(var i=0;i<newSequence.length;i++){ 
      newComparisonMeasureList.push((comparisonMeasureList.split(","))[newSequence[i]])   
     }
     chartData[chartId]["customTimeComparisons"]=newComparisonMeasureList;
     parent.$("#chartData").val(JSON.stringify(chartData));
//      alert("newComparisonMeasureList"+newComparisonMeasureList);
      generateKPIDashboard(chartId,chWidth,chHeight,newComparisonMeasureList);
     $("#reportSequence").dialog('close');

 } 
 function addPage(){
    
    if(typeof parent.comboTopChartType["comboTopChartType"]!=='undefined'  && parent.comboTopChartType["comboTopChartType"]!==null){ 
    var    pageLabel=parent.comboTopChartType["comboTopChartType"];
    
    }else{  
    var pageLabel = prompt("Please enter page name", "");
    if(pageLabel === null){
        return;
    }
    }
//    var pageLabel = prompt("Please enter page name", "");
//    if(pageLabel === null){
//        return;
//    }
    var REPORTID='';
    try{
        
    REPORTID = parent.document.getElementById("REPORTID").value;
    }catch(e){
        REPORTID = parent.$("#graphsId").val();
    }
    $.ajax({
        async:false,
        url: "reportViewerAction.do?reportBy=addNewPage&pageLabel="+pageLabel+"&reportId="+REPORTID,
        success: function(data){
            parent.$("#"+currentPage).css("border-bottom","");
            var pageId=data.split(":")[0];
            currentPage=pageId;
           if(typeof parent.comboTopChartType["comboTopChartType"]!=='undefined'  && parent.comboTopChartType["comboTopChartType"]!==null){ 
               
           }else{
               
            alert("Page added successfully.");
           }
            if(parent.$("#pageList > div").length>=3){
                var reportPageMapping=JSON.parse(parent.$("#reportPageMapping").val());
                var map={};
                map["pageSequence"]=data.split(":")[1];
                map["pageLabel"]=pageLabel;
                reportPageMapping[REPORTID][pageId]=map;
                parent.$("#reportPageMapping").val(JSON.stringify(reportPageMapping));
                var pageSequence=JSON.parse(parent.$("#pageSequence").val());
//                pageSequence=pageSequence.split(",");
                pageSequence.push(data.split(":")[1]);
                parent.$("#pageSequence").val(JSON.stringify(pageSequence));
                parent.$("#pageList").css("width","40%");
                parent.$("#morePages").show();
            }
            else{
                if(pageId==='_page2'){
                    parent.$("#pageList").append("<div id='default' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>Page 1</div>");
                }
                parent.$("#pageList").append("<div id='"+pageId+"' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='border-bottom:3px solid #8bc34a;width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>"+pageLabel+"</div>");
            }
            var liCount=$('#gridUL li').length;
            for(var i=0 ;i<liCount;i++){
                var id = "#divchart"+(i+1);
                parent.$(id).hide();
            }
            if (window.mgmtDBFlag === 'true') {
                var gtValues;
                $.ajax({
                    async: false,
                    data: parent.$("#graphForm").serialize(),
                    url: $("#ctxpath").val() + "/reportViewer.do?reportBy=GTKPICalculateFunction&action=template",
                    success: function(data) {
                        alert("2nd" + JSON.stringify(JSON.parse(parent.$("#reportPageMapping").val())));
                        gtValues = JSON.parse(data);
                        showMagTemp(gtValues);
                    }
                });
            }
            buildcomboTopKPI(parent.comboTopChartType)
        }
    });
}
function changePage(pageId,flag){
    if(typeof flag!=='undefined' && flag==='moreList'){
        var REPORTID = parent.document.getElementById("REPORTID").value;
        $.ajax({
            async:false,
            url: "reportViewerAction.do?reportBy=updatePageSequence&pageId="+pageId+"&reportId="+REPORTID,
            success: function(data){
            }
        });
    }
    $("#pagesList").hide();
    var reportId = parent.document.getElementById("REPORTID").value;
    currentPage=pageId;
    parent.generateJsonDataReset(reportId,pageId)
}

function pageDrill(chartId){
    $("#initializeCharts").dialog({
        autoOpen: false,
        height: 260,
        width: 300,
        position: 'justify',
        modal: true,
        resizable:true,
        title:'Define Page Drill'
    });
    var innerHtml = "<table style='padding:3px 3px 3px 3px;width:100%;border-collapse:separate;border-spacing:10px 10px;'>";
    innerHtml += "<tr><td><span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Please select page</span> </td></tr>";
    var repId=parent.$("#graphsId").val();
    var reportPageMapping = JSON.parse(parent.$("#reportPageMapping").val())[repId];
    var pages=Object.keys(reportPageMapping);
    innerHtml += "<tr>";
    innerHtml += "<td>";
    innerHtml += "<select id='drillPagesList'>";
    var c=0;
    for(var i in pages){
        if(pages[i]!==currentPage){
            if(c==0){
                innerHtml += "<option value='"+pages[i]+"' selected>"+reportPageMapping[pages[i]]["pageLabel"];
            }
            else{
                innerHtml += "<option value='"+pages[i]+"'>"+reportPageMapping[pages[i]]["pageLabel"];
            }
            c++;
            innerHtml += "</option>";
        }
    }
    innerHtml += "</select>";
    innerHtml += "</td>";
    innerHtml += "</tr>";
    innerHtml += "<tr>";
    innerHtml += "<td>";
    if(c==0){
        innerHtml += "<input type='button' value='Done' onclick='selectPageDrill(\""+chartId+"\")' disabled/>";
    }
    else{
        innerHtml += "<input type='button' value='Done' onclick='selectPageDrill(\""+chartId+"\")'/>";
    }
    innerHtml += "</td>";
    innerHtml += "</tr>";
    
    innerHtml += "</table>";
    $("#initializeCharts").html(innerHtml);
    $("#initializeCharts").dialog('open'); 
}
function selectPageDrill(chartId){
    var repId,reportPageMapping;
    var selectedPage=parent.$("#drillPagesList").val();
//    if(typeof selectedPage==='undefined' || selectedPage===''){
//        repId=parent.$("#graphsId").val();
//        reportPageMapping = JSON.parse(parent.$("#reportPageMapping").val())[repId];
//        if(currentPage===Object.keys(reportPageMapping)[0]){
//            selectedPage=Object.keys(reportPageMapping)[1];
//        }
//        else{
//            selectedPage=Object.keys(reportPageMapping)[0];
//        }
//    }
    var chartData=JSON.parse(parent.$("#chartData").val());
    chartData[chartId]["drillPage"]=selectedPage;
    parent.$("#chartData").val(JSON.stringify(chartData));
    selectedPage='';
    $("#initializeCharts").dialog('close'); 
}
//function setSelectedPage(page){
//    selectedPage=page;
//}

function getSelectedPage(pageId){
    changePage(pageId);
}
//added by DINANATH for kpi alert schedulers
function defineAlerts(chartName, reportId, userId) {
//    alert("chartName DefineAlerts  " + chartName);
//    alert("reportId DefineAlerts  " + reportId);
//    alert("userId DefineAlerts  " + userId);
    $("#definekpichartalerts").dialog({
        autoOpen: false,
        height: 550,
        width: 900,
        position: 'justify',
        modal: true,
        resizable: true,
        title: translateLanguage.Define_Alerts
    });
    var htmlTabFormat = "<div class='mainContainer'>";
    htmlTabFormat += "<div>";
    htmlTabFormat += "<span>Alert Name<span style='color:red;position: absolute;display: inline;width: auto;margin: -5px 0px 0px 3px;'>*</span></span>";
    htmlTabFormat += " <input type='text' id='kpialertname' name='kpialertname' placeholder='Alert Name'/>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div>";
    htmlTabFormat += "<span>Measure Type<span style='color:red;position: absolute;display: inline;width: auto;margin: -5px 0px 0px 3px;'>*</span></span>";
    htmlTabFormat += "<select id='measureType' name='measureType'onChange='getData(this)'>";
    htmlTabFormat += "<option value='SELECT'>SELECT</option>";
    htmlTabFormat += "<option value='Current Measure'>Current Measure</option>";
    htmlTabFormat += "<option value='Change %'>Change %</option>";
    htmlTabFormat += "</select>";
    htmlTabFormat += "<select id='measureSubType' name='absoluteType'>";
    htmlTabFormat += "</select>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div id='appendMore'>";
    htmlTabFormat += "<div id='0' name='addrowCount' class='col5'>";
    htmlTabFormat += "<span>Alert Condition1<span style='color:red;position: absolute;display: inline;width: auto;margin: -5px 0px 0px 3px;'>*</span></span>";
    htmlTabFormat += "<select name='operators' id='0operators' onchange='onbtwoperator(0,this.value)' >";
    htmlTabFormat += "<option value='SELECT'>SELECT</option>";
    for (var j = 0; j < strOperators.length; j++) {
        htmlTabFormat += "<option value='" + strOperators[j] + "'>" + strOperators[j] + "</option>";
    }
    htmlTabFormat += "</select>";
    htmlTabFormat += "<input type='text' name='BaselineValues' id='BaselineValues' placeholder='Baseline Value'/><span style='width: 30px'>%</span><i style='font-size:200%;color:#8BC34A;margin: -3px 30px 0px 3px;' class='fa fa-envelope' onclick='addMail()'> </i>";
    htmlTabFormat += "<select name='medium' id='medium'>";
    htmlTabFormat += "<option value='SELECT'>SELECT</option>";
    for (var i = 0; i < medium.length; i++) {
        htmlTabFormat += "<option value='" + medium[i] + "'>" + medium[i] + "</option>";
    }
    htmlTabFormat += "</select>";
//      htmlTabFormat += "<input type='text' name='sValues' id='eValues0' />";
//    htmlTabFormat += "<input type='text' name='eValues' id='eValues0' style='visibility: hidden;'/>";
//    htmlTabFormat += "<button>X</button>";
//    htmlTabFormat += "<div>";
//    htmlTabFormat += "<textarea id='usermailtextarea0' name='usermailtextarea' placeholder='Type Email IDs Here and for multiple email ids use comma '></textarea>";
//    htmlTabFormat += "</div>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div>";
    htmlTabFormat += "<span>Start Date<span style='color:red;position: absolute;display: inline;width: auto;margin: -5px 0px 0px 3px;'>*</span></span>";
    htmlTabFormat += "<input id='sDatepicker' value='' style='width: 120px;'  name='startdate' readonly='' type='text'>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div>";
    htmlTabFormat += "<span>End Date<span style='color:red;position: absolute;display: inline;width: auto;margin: -5px 0px 0px 3px;'>*</span></span>";
    htmlTabFormat += "<input id='eDatepicker' value='' style='width: 120px;'  name='enddate' readonly='' type='text'>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div>";
    htmlTabFormat += "<span>Time</span>";
    htmlTabFormat += "<select name='hours' id='hours'>";
    htmlTabFormat += "<option value='SELECT'>SELECT HOURS</option>";
    for (var i = 0; i <= 23; i++)
        htmlTabFormat += "<option value='" + i + "'>" + i + "</option>";
    htmlTabFormat += "</select>";
    htmlTabFormat += "<select name='minutes' id='minutes'>";
    htmlTabFormat += "<option value='SELECT'>SELECT MINUTES</option>";
    for (var i = 0; i <= 59; i++)
        htmlTabFormat += "<option value='" + i + "'>" + i + "</option>";
    htmlTabFormat += "</select>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div>";
    htmlTabFormat += "<span>Frequency<span style='color:red;position: absolute;display: inline;width: auto;margin: -5px 0px 0px 3px;'>*</span></span>";
    htmlTabFormat += "<select name='frequency' id='frequency' onchange='setToShowOrHideFrequency(this.id)'>";
    htmlTabFormat += "<option value='SELECT'>SELECT</option>";
    for (var i = 0; i < frequency.length; i++) {
    htmlTabFormat += "<option value='"+frequency[i]+"'>"+frequency[i]+"</option>";
    }
    htmlTabFormat += "</select>";
    
     htmlTabFormat += "<select style='width:20%' id='dailyType' name='dailyType'>";
    htmlTabFormat += "<option value='SELECT'>SELECT</option>";
    htmlTabFormat += "<option value='Absolute Value'>Absolute Value</option>";
    htmlTabFormat += "<option value='% Value'>% Value</option>";
    htmlTabFormat += "</select>";
    
    htmlTabFormat += "<select style='width:20%' id='monthalyType' name='monthalyType'>";
    htmlTabFormat += "<option value='SELECT'>SELECT</option>";
    htmlTabFormat += "<option value='Allocate'>Allocate</option>";
    htmlTabFormat += "<option value='commulate'>% commulate</option>";
    htmlTabFormat += "</select>";
    
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div id='setweekday' style='display:none;'>";
    htmlTabFormat += " <span class='myalertheader'>Week Day</span>";
    htmlTabFormat += "<select id='setparticularDay' name='setparticularDay'>";
    for (var i = 0; i < scheduleday.length; i++) {
        htmlTabFormat += " <option value=" + sday[i] + ">" + scheduleday[i] + "</option>";
    }
    htmlTabFormat += "</select>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div id='setmonthday' style='display:none;'>";
    htmlTabFormat += " <span class='myalertheader'>Month Day</span>";
    htmlTabFormat += "<select id='setmonthParticularDay' name='setmonthParticularDay'>";
    for (var i = 0; i <= 31; i++) {
        htmlTabFormat += " <option value=" + i + ">" + i + "</option>";
    }
    htmlTabFormat += "</select>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div id='sethourly' style='display:none;'>";
    htmlTabFormat += " <span class='myalertheader'>Hourly</span>";
    htmlTabFormat += "<select id='hourlyParticularDay' name='hourlyParticularDay'>";
    for (var i = 0; i <= 23; i++) {
        htmlTabFormat += " <option value=" + i + ">" + i + "</option>";
    }
    htmlTabFormat += "</select>";
    htmlTabFormat += "</div>";


    htmlTabFormat += "<div class='btn3'>";
    htmlTabFormat += "<button onclick='addMore()'>Add Row</button>";
    htmlTabFormat += "<button onclick='deleteRowFromLast()'>Delete Row</button>";
    htmlTabFormat += "<button onclick=\"saveDefineKpiAlerts('" + chartName + "','" + reportId + "','" + userId + "')\">Done</button>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "</div>";




    $("#definekpichartalerts").html("");
    $("#definekpichartalerts").html(htmlTabFormat);
    $("#definekpichartalerts").dialog('open');
    $('#sDatepicker').datepicker({
        changeMonth: true,
        changeYear: true
    });
    $('#eDatepicker').datepicker({
        changeMonth: true,
        changeYear: true
    });
}
//Added by Ram 26Apr2016
function getData(title){
                 var val =title.value;
                 var option1=document.createElement("option");
                 var option2=document.createElement("option");
                 var option3=document.createElement("option");
                 document.getElementById("measureSubType").options.length = 0;
                 var select = document.getElementById("measureSubType");
                 if(val==='Current Measure'){
                        option1.text = "MTD";
                        option1.value = "MTD";
                        option2.text = "YTD";
                        option2.value = "YTD";
                        option3.text = "QTD";
                        option3.value = "QTD";
                        select.appendChild(option1);
                        select.appendChild(option2);
                        select.appendChild(option3);
                 }else{
                      option1.text = "MOM%";
                        option1.value = "MOM%";
                        option2.text = "YOY%";
                        option2.value = "YOY%";
                        select.appendChild(option1);
                        select.appendChild(option2);
                 }
}
function addMail() {
    $("#addMailIds").dialog({
        autoOpen: false,
        height: 350,
        width: 700,
        position: 'justify',
        modal: true,
        resizable: true,
        title: translateLanguage.Add_MailIds
    });

    var htmlTabFormat = "<div class='subContainer'>";
 
    htmlTabFormat += "<div id='0' name='addMailCount' class='col5'>";
    htmlTabFormat += " <div id='heading' style='width:100%'>   <div style='width:20%;float:left'>  <p style='text-align:left'><b>S.No</b></p> </div> <div style='width:50%;float:left' > <p style='margin-left:100px'><b>Email-ID</b></p> </div>   <div style='width:30%;float:left'> <p style='text-align:center'><b>Send Any Ways</b></p></div>  </div>";
    htmlTabFormat += "<div id='appendMoreMails' style='max-height:145px;overflow:auto'>";
    htmlTabFormat += " <div style='width:100%'>   <div style='width:20%;float:left;margin-top:-4px'>  <p>1</p> </div> <div style='width:50%;float:left;margin-top:-12px' ><p align='left'><textarea style='height: 25px;width: 250px;font-size:15px' id='userMailId0' name='userMailId' placeholder='Type Email ID Here'></textarea></p></div>   <div style='width:30%;float:left;margin-top:-4px'> <input style='text-align:center;margin-top:10px;margin-left:70px' type='checkbox' id=checkAnyWays"+0+" name='checkAnyWays' onclick='checkSendAnyWays(this.id)' value='false'></div>  </div>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "<div class='btn3'>";
    htmlTabFormat += "<div style='margin-left:150px'><button onclick='addMoreMails()'>Add Row</button>";
    htmlTabFormat += "<button style='margin-left:10px' onclick='deleteMailRowFromLast()'>Delete Row</button>";
    htmlTabFormat += "<button style='margin-left:10px' onclick='closeDialog()'>Done</button></div>";
   
    htmlTabFormat += "</div>";
    htmlTabFormat += "</div>";
    htmlTabFormat += "</div>";

    $("#addMailIds").html("");
    $("#addMailIds").html(htmlTabFormat);
    $("#addMailIds").dialog('open');
    $('#sDatepicker').datepicker({
        changeMonth: true,
        changeYear: true
    });
    $('#eDatepicker').datepicker({
        changeMonth: true,
        changeYear: true
    });
} 
function addMoreMails(){
    
    var addMailCount = document.getElementsByName("addMailCount");
    var counter=addMailCount.length-1;
    counter=addMailCount[counter].id;
    counter++;
    var count=counter+1;
    var html = "";
    html += "<div id='" + counter + "' name='addMailCount' class='col5'>";
    html += " <div style='width:100%'>   <div style='width:20%;float:left;margin-top:-4px'>  <p>"+ count +"</p> </div> <div style='width:50%;float:left;margin-top:-12px' ><p align='left'><textarea style='height: 25px;width: 250px;font-size:15px' id='userMailId"+counter+"' name='userMailId' placeholder='Type Email ID Here'></textarea></p></div>   <div style='width:30%;float:left;margin-top:-4px'> <input style='text-align:center;margin-top:10px;margin-left:70px' type='checkbox' id=checkAnyWays"+counter+" name='checkAnyWays' onclick='checkSendAnyWays(this.id)' value='false'></div> </div>";
    html += "</div>";
  //  html += "<button onclick='deleteRow(" + counter + ")'>X</button>";
    $("#appendMoreMails").append(html);
}
function checkSendAnyWays(counter){
    alert("yes comming"+counter)
   if(document.getElementById(counter).checked) {
       alert("checked")
        document.getElementById(counter).value = true;
   }else{
       alert("UNchecked")
   document.getElementById(counter).value = false;
   }
}
function closeDialog() {
     userMailId = document.getElementsByName("userMailId");
     isSendAnyWays = document.getElementsByName("checkAnyWays");
     for (var i = 0; i <userMailId.length; i++)
    {
        alert(userMailId[i].value);
        alert(isSendAnyWays[i].value);
        
    }
    $('#addMailIds').dialog('close');
  
}
function deleteMailRowFromLast() {
        var addrowCount = document.getElementsByName("addMailCount");
    var counter=addrowCount.length-1;
    counter=addrowCount[counter].id;
    $("#" + counter).remove();
}
//Endded by Ram

function selectPageDrill(chartId){
    var repId,reportPageMapping;
    var selectedPage=parent.$("#drillPagesList").val();
//    if(typeof selectedPage==='undefined' || selectedPage===''){
//        repId=parent.$("#graphsId").val();
//        reportPageMapping = JSON.parse(parent.$("#reportPageMapping").val())[repId];
//        if(currentPage===Object.keys(reportPageMapping)[0]){
//            selectedPage=Object.keys(reportPageMapping)[1];
//        }
//        else{
//            selectedPage=Object.keys(reportPageMapping)[0];
//        }
//    }
    var chartData=JSON.parse(parent.$("#chartData").val());
    chartData[chartId]["drillPage"]=selectedPage;
    parent.$("#chartData").val(JSON.stringify(chartData));
    selectedPage='';
    $("#initializeCharts").dialog('close'); 
}
//function setSelectedPage(page){
//    selectedPage=page;
//}

function getSelectedPage(pageId){
    changePage(pageId);
}


  //added by Dinanath
function addMore() {
    var addrowCount = document.getElementsByName("addrowCount");
    var counter=addrowCount.length-1;
    counter=addrowCount[counter].id;
    counter++;
    var count=counter+1;
    var html = "";
    html += "<div id='" + counter + "' name='addrowCount' class='col5'><span>Alert Condition"+ count +"</span>";
    html += "<select name='operators' id='" + counter + "operators' onchange='onbtwoperator(" + counter + ",this.value)' >";
    html += "<option value='SELECT'>SELECT</option>";
    for (var j = 0; j < strOperators.length; j++) {
        html += "<option value='" + strOperators[j] + "'>" + strOperators[j] + "</option>";
    }
    html += "</select>";
    html += "<input type='text' name='BaselineValues' id='BaselineValues' placeholder='Baseline Value'/><span style='width: 30px'>%</span><i style='font-size:200%;color:#8BC34A;margin: -3px 30px 0px 3px;' class='fa fa-envelope' onclick='addMail()'> </i>";
    html += "<select name='medium' id='medium'>";
    html += "<option value='SELECT'>SELECT</option>";
    for (var  i= 0; i < medium.length; i++) {
        html += "<option value='" + medium[i] + "'>" + medium[i] + "</option>";
    }
    html += "</select>";
//    html += "<input type='text' name='sValues' id='sValues" + counter + "' />";
//    html += "<input type='text' name='eValues' id='eValues" + counter + "' style='visibility: hidden;'/>";
    html += "<button onclick='deleteRow(" + counter + ")'>X</button>";
//    html += "<div><textarea id='usermailtextarea" + counter + "' name='usermailtextarea' placeholder='Type Email IDs Here and for multiple email ids use comma '></textarea></div></div>";
    $("#appendMore").append(html);
}
//added by Dinanath
function deleteRow(id) {
    $("#" + id).remove();
}
//added by Dinanath
function deleteRowFromLast() {
        var addrowCount = document.getElementsByName("addrowCount");
    var counter=addrowCount.length-1;
    counter=addrowCount[counter].id;
    $("#" + counter).remove();
}
//added by Dinanath
function saveDefineKpiAlerts(chartId, reportId, userId) {
// var kpialertname = document.getElementsByName("kpialertname").value;
    var operatorList = [];
    var sValuesList = [];
    var eValuesList = [];
    var usermailtextareaList = [];
    var kpialertname = document.getElementsByName("kpialertname")[0].value;
    var measureType = document.getElementsByName("measureType")[0].value;
    var operators = document.getElementsByName("operators");
    var sValues = document.getElementsByName("sValues");
    var eValues = document.getElementsByName("eValues");
    var usermailtextarea = document.getElementsByName("usermailtextarea");
    var hours = document.getElementsByName("hours")[0].value;
    var minutes = document.getElementsByName("minutes")[0].value;
    var startdate = document.getElementsByName("startdate")[0].value;
    var enddate = document.getElementsByName("enddate")[0].value;
    var frequency = document.getElementsByName("frequency")[0].value;
//    alert(operators.length)
    for (var i = 0; i < operators.length; i++)
    {
        operatorList.push(operators[i].value);
        sValuesList.push(sValues[i].value);
        eValuesList.push(eValues[i].value);
        usermailtextareaList.push(usermailtextarea[i].value);
    }
    var setparticularDay = document.getElementsByName("setparticularDay")[0].value;
//    alert(setparticularDay)
    var setmonthParticularDay = document.getElementsByName("setmonthParticularDay")[0].value;
//    alert(setmonthParticularDay)
    var hourlyParticularDay = document.getElementsByName("hourlyParticularDay")[0].value;
    
    //for validation of fields
    if(kpialertname==''){
        $('#kpialertname').css('border-color', 'red');
        alert("Alert Name is empty");
        return false;
    }else{
        $('#kpialertname').css('border-color', '#BEBFBD');
    }
    if(measureType=='SELECT'){
        alert("Please select measure type");
        return false;
    }
     for (var i = 0; i < operators.length; i++)
    {
    if(operatorList[i]=='SELECT'){
        alert("Please select Operation type");
        return false;
    }
    if(sValuesList[i]==''){
        alert("Target value is empty");
        return false;
    }
}
 if(usermailtextareaList[0]==''){
        alert("Please enter your email id");
        return false;
    }
    else{
        var regex = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
        var result = usermailtextareaList[0].toString().replace(/\s/g, "").split(/,|;/);        
        for(var i = 0;i < result.length;i++) {
            if(!regex.test(result[i])) {
                 alert("Please enter valid email id");
                return false;
            }
        }       
    }
//    if(hours=='SELECT'){
//        alert("Please select hours");
//        return false;
//    }
//    if(minutes=='SELECT'){
//        alert("Please select minute");
//        return false;
//    }
    if(startdate==''){
        alert("Start Date is empty");
        return false;
    }
//    if(enddate==''){
//        alert("End date is empty");
//        return false;
//    }
    if(frequency=='SELECT'){
        alert("Frequency is empty");
        return false;
    }
    
 var chartData = JSON.parse(parent.$("#chartData").val());
var allViewByIds = JSON.parse(parent.$("#viewbyIds").val());
 var allViewByNames = JSON.parse(parent.$("#viewby").val());
 var map = {};
 for(var i=0;i<allViewByIds.length;i++){
map[allViewByIds[i]] = allViewByNames[i];
 }
 var viewBys=[];
 var dimensions= chartData[chartId]["dimensions"];
for (var i = 0, keys = Object.keys(map), ii = keys.length; i < ii; i++) {
     for(var m=0;m<dimensions.length;m++){
           if(keys[i]==dimensions[m]){
               viewBys.push(map[keys[i]]);
           }
//            alert('key : ' + keys[i] + ' val : ' + map[keys[i]]);
     }
}

//var viewIds=chartData[chartId]["viewIds"];
var viewIds=chartData[chartId]["dimensions"];
// alert("dimensions viewIds "+viewIds)

var measName=chartData[chartId]["meassures"];
// alert(measName)
var measId=chartData[chartId]["meassureIds"];
// alert(measId)
var aggType=chartData[chartId]["aggregation"];
// alert(aggType)
var defaultMeasures=chartData[chartId]["defaultMeasures"];
// alert(defaultMeasures)
var defaultMeasureIds=chartData[chartId]["defaultMeasureIds"];
// alert(defaultMeasureIds)
var chartType=chartData[chartId]["chartType"];
// alert(chartType)
var KPIName=chartData[chartId]["KPIName"];
var reportName=$('#graphName').val();

    $.ajax({
        async: false,
        data:  {"kpialertname": kpialertname, "measureType": measureType, "operatorsList": operatorList.toString(), "sValuesList": sValuesList.toString(), "eValuesList": eValuesList.toString()
            , "usermailtextareaList": usermailtextareaList.toString(), "startdate": startdate, "enddate": enddate, "hours": hours, "minutes": minutes, "frequency": frequency
            , "weeklyparticularDay": setparticularDay,"monthlyParticularDay": setmonthParticularDay,"hourlyParticularDay": hourlyParticularDay,"chartName": chartId, "reportId": reportId
            , "userId": userId,"viewBys":viewBys.toString(),"viewIds":viewIds.toString(),"dimensions":dimensions.toString(),"measName":measName.toString(),
            "measId":measId.toString(),"aggType":aggType.toString(),"defaultMeasures":defaultMeasures.toString()
            ,"defaultMeasureIds":defaultMeasureIds.toString(),"chartType":chartType,"KPIName":KPIName,"reportName":reportName},
        url: "reportViewerAction.do?reportBy=defineKpiAlertSchedule",
//        url: "",
        success: function (data) {
            alert(data);
            $("#definekpichartalerts").dialog('close');
        }
    });

}
//added by DINANATH
function onbtwoperator(rowno, symbol)
{
//    var box = parseInt(rowno);
    var open = document.getElementById("eValues" + rowno);
    if (symbol == "<>") {
        open.type = "text";
//        document.getElementById("eValues"+rowno).style.display="block";
        document.getElementById("eValues" + rowno).style.visibility = "visible";
    } else {
        document.getElementById("eValues" + rowno).style.visibility = "hidden";
        open.type = "text";
//        open.type="hidden";
    }
}
//added by Dinanath
function setToShowOrHideFrequency(id) {
    if ($("#" + id).val() == "Weekly") {
        $("#setweekday").show();
        $("#setmonthday").hide();
        $("#sethourly").hide();
    } else if ($("#" + id).val() == "Monthly") {
        $("#setweekday").hide();
        $("#setmonthday").show();
        $("#sethourly").hide();
    } else if ($("#" + id).val() == "Hourly") {
        $("#setweekday").hide();
        $("#setmonthday").hide();
        $("#sethourly").show();
    } else {
        $("#setweekday").hide();
        $("#setmonthday").hide();
        $("#sethourly").hide();
    }
}

/*Added by Ashutosh*/
function isCircularChart(chartId){
     var chartData=JSON.parse(parent.$("#chartData").val());
     var chartType=chartData[chartId]["chartType"];
     if(chartType==='Column-Pie' || chartType==='Column-Donut'  ||  chartType==='Pie' || chartType==='Donut' || chartType==='Double-Donut'
    ||  chartType==='Donut-3D' || chartType==='Pie-3D' || chartType==='Half-Pie' ||  chartType==='Half-Donut'){
    
         return true;
    }else{
         return false;
     }
}

function isKPIChart(chartId){
     var chartData=JSON.parse(parent.$("#chartData").val());
     var chartType=chartData[chartId]["chartType"];
     if(chartType==="Radial-Chart" || chartType==="LiquidFilled-KPI"  ||  chartType==="Dial-Gauge" || chartType==="Emoji-Chart" || chartType==="Bullet-Horizontal"
    ||  chartType==="KPI Dashboard" ){
  return true;
    }else{
         return false;
     }
}

function isGroupedChart(chartId){
     var chartData=JSON.parse(parent.$("#chartData").val());
     var chartType=chartData[chartId]["chartType"];
     if(chartType==="Grouped-Bar" || chartType==="GroupedHorizontal-Bar"  ||  chartType==="GroupedStacked-Bar" || chartType==="GroupedStackedH-Bar" || chartType==="GroupedStacked-Bar%"
    ||  chartType==="GroupedStackedH-Bar%"  ||  chartType==="Grouped-Line" ||  chartType==="Grouped-MultiMeasureBar"  ||  chartType==="Scatter-Bubble" ||  chartType==="Multi-View-Tree"){
  return true;
    }else{
         return false;
     }
}

function isWorldMapChart(chartId){
     var chartData=JSON.parse(parent.$("#chartData").val());
     var chartType=chartData[chartId]["chartType"];
     if(chartType==="India-Map" || chartType==="IndiaCity-Map"  ||  chartType==="Combo-IndiaCity" ||  chartType==="Combo-India-Map" || chartType==="US-State-Map" || chartType==="US-City-Map"
    ||  chartType==="Combo-US-Map"  ||  chartType==="Combo-USCity-Map" ||  chartType==="Australia-State-Map"  ||  chartType==="Australia-City-Map" ||  chartType==="Combo-Aus-State"
	||  chartType==="Combo-Aus-State"||  chartType==="Combo-Aus-City"||  chartType==="world-map"||  chartType==="World-City"){
  return true;
    }else{
         return false;
     }
}

function isIndiaStateMapChart(chartId){
     var chartData=JSON.parse(parent.$("#chartData").val());
     var chartType=chartData[chartId]["chartType"];
     if(chartType==="Andhra-Pradesh" || chartType==="Telangana"){
    
         return true;
    }else{
         return false;
     }
}

function isBubbleChart(chartId){
     var chartData=JSON.parse(parent.$("#chartData").val());
     var chartType=chartData[chartId]["chartType"];
     if(chartType==="Bubble" || chartType==="Split-Bubble"|| chartType==="OverLaid-Bar-Bubble"|| chartType==="Centric-Bubble"|| chartType==="Multi-View-Bubble"
	 || chartType==="Horizontal-Bubble"|| chartType==="Tangent"){
    
         return true;
    }else{
         return false;
     }
}

function isOtherChart(chartId){
     var chartData=JSON.parse(parent.$("#chartData").val());
     var chartType=chartData[chartId]["chartType"];
     if(chartType==="Word-Cloud" || chartType==="Scatter-XY"|| chartType==="TopBottom-Chart"|| chartType==="Tree-Map"|| chartType==="Top-Analysis"
	 || chartType==="Combo-Analysis"|| chartType==="World-Top-Analysis"){
    
         return true;
    }else{
         return false;
     }
}
 function enableRootAnalysis(idArr,chartId,val){
    $("#onChartChange"+div).css('display','none');
    var column = [];
    var columnID = [];
    var dimensionId = [];
    var div = chartId;
//    var chartData ={};
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails=chartData[chartId];
    var measureName = [];
    var measureId = [];
    var aggType = [];
  var measure = chartData[div]["meassures"];
 var meassureId = chartData[div]["meassureIds"];
var aggregationType = chartData[div]["aggregation"];
    column.push(val.split(":")[0]);
    columnID.push(val.split(":")[1]);
    dimensionId = chartData[div]["dimensions"];
    dimensionId.push(JSON.stringify(JSON.parse(columnID)));
    measureName.push(measure[0]);
    measureName.push(measure[0]+" (PRIOR)");
    measureId.push(meassureId[0]);
    measureId.push(meassureId[0]+"_PRIOR");
    aggType.push(aggregationType[0]);
    aggType.push(aggregationType[0]);
//    chartDetails["chartType"] ="Trend-Analysis";
    chartDetails["viewBys"] = column;
    chartDetails["meassures"] = measureName;
    chartDetails["aggregation"] = aggType;
    chartDetails["viewIds"] = columnID;
    chartDetails["meassureIds"] = measureId;
    chartDetails["dimensions"] = dimensionId;
 
    chartData[div] = chartDetails;

    parent.$("#chartData").val(JSON.stringify(chartData));


 
    var drills = {};
    
    // previous drills
    if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
        try{
            drills=JSON.parse($("#drills").val());
        }catch(e){
        }
    }
    var drillValues = [];

    drillValues.push(idArr.split(":"))

    var drillMap = [];
    drillMap.push(idArr.split(":")[0]);
    drills[idArr.split(":")[1]] = drillMap;
        $("#drills").val(JSON.stringify(drills));
             var quickViewname = idArr.split(":")[1];
                
             if(quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month") {
                parent.$("#drillFormat").val("time"); 
             }else if(quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim().toLowerCase() == "time" || quickViewname.toString().trim() == "Time" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year" || quickViewname.toString().trim() == "Year"){
                 parent.$("#drillFormat").val("time"); 
             }else {
                parent.$("#drillFormat").val("none");  
             }
             parent.$("#shadeType").val("quickDrill");
//         parent.$("#chartData").val(JSON.stringify(chartData));
   $.ajax({
            async:false,
            type:"POST",
            data :$("#graphForm").serialize(),
            url: $("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&initializeFlag=true"+"&action=localfilterGraphs",
            success: function(localData){
                var meta = JSON.parse(JSON.parse(localData)["meta"]);
                  $("#chartData").val(JSON.stringify(meta["chartData"])); 
//                         parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(localData)["meta"])["chartData"]));
//var jsondata1 = JSON.parse(localData)["data"];
            window.idArr = idArr;
            generateSingleChart(localData,chartId,idArr);
            
                        }
                        }); 
 }
 
 
 function renamePage(){
     
     var pageLabel = prompt("Please enter page name", "");
    if(pageLabel == null){
        return;
    }
    var REPORTID='';
    try{
        
    REPORTID = parent.document.getElementById("REPORTID").value;
    }catch(e){
        REPORTID = parent.$("#graphsId").val();
    }
    $.ajax({
        async:false,
        url: "reportViewerAction.do?reportBy=renamePage&pageLabel="+pageLabel+"&reportId="+REPORTID+"&currentPage="+currentPage,
        success: function(data){
            alert("Page Renamed Succesfully");
                parent.generateJsonDataReset(REPORTID,currentPage);

        }
        
    });
  }

function saveFilters1(chartId) {
    parent.$("#tempDashletDiv").dialog('close');
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartDetails = chartData[chartId];
    var viewIds = chartDetails["viewIds"];
        var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var filterMap = {};
    var filterKeys = chartData[chartId]["dimensions"];
    var temp;
    if(typeof chartData[chartId]["dimensions"]=="undefined" || chartData[chartId]["dimensions"]==""){
        filterKeys=chartData[chartId]["viewBys"];
    }else{
        temp = filterKeys;
        filterKeys=[];
        for (var key in temp) {
            filterKeys.push(viewOvName[viewOvIds.indexOf(temp[key])]);
        }
    }
// for getting checked value by prabal
    var selectedMap = {};
    for(var i=0;i<viewOvName.length;i++)
    {// alert("viewOvName[i].replace())="+viewOvName[i].replace(" ",""));
        if($("#L_"+viewOvName[i].replace(" ","")).is(":checked"))
        {
//            alert("checked");
        }
        else{
        
       var ids= document.getElementsByClassName("selectclass"+viewOvName[i].replace(" ",""));
         var aray=[];
          for(var i1=0;i1<ids.length;i1++)
          {
             if(ids[i1].checked)
             {              
//                  aray.push(ids[i1].value.trim());
//                  aray.push(ids[i1].value);
                  aray[i1]=ids[i1].value;
             }
             selectedMap[viewOvIds[i]]=aray;
      }
    }
    }
//    alert("filters:"+JSON.stringify(selectedMap));

    chartDetails["filters"] = selectedMap;
//    alert(JSON.stringify(selectedMap));
    if(Object.keys(selectedMap).length==0){
       $("#newLocalFilter"+chartId).hide();
    }else{
         $("#newLocalFilter"+chartId).show();
    }
       
    chartData[chartId] = chartDetails;

    var others = "";
    if($("#"+chartId+"_othersL").prop("checked")){
        others="Y";
    }else{
        others="N";
    }
    var globalfilter = "";
    if($("#"+chartId+"_globalfilter").prop("checked")){
        globalfilter="Y";
    }else{
        globalfilter="N";
    }
    chartData[chartId]["othersL"] = others;
    chartData[chartId]["globalEnable"] = globalfilter;
  
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
        function(data){
            var resultset = data
            var data = JSON.parse(resultset)["data"];
            var chartData = JSON.parse($("#chartData").val());
            var chartFlag = "false";
            var GTdiv = [];
            for(var t in chartData){
                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash"  ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="Standard-KPI" ||chartData[t]["chartType"]=="Radial-Chart"||chartData[t]["chartType"]=="LiquidFilled-KPI" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
                    chartFlag = "true";
                  $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                }
            }

            generateSingleChart(resultset,chartId);
        });

}
function renameViewBys(){
    parent.$("#tempDashletDiv").dialog({
        autoOpen: false,
        height: 300,
        width: 400,
        position: 'justify',
        modal: true,
        title: translateLanguage.RENAME_ATTRIBUTES+' :'  //+ chartDetails["Name"]
    });
    var allViewBys  = JSON.parse(parent.$("#viewby").val());
//    var allViewIds = JSON.parse(parent.$("#viewbyIds").val());
    var html="";
    html += "<table style='border-collapse: separate;border-spacing: 10px 10px;'>";
    html += "<tr>";
    html += "<td>";
    html += "</td>";
    html += "<td>New Name";
    html += "</td>";
    html += "</tr>";
    for(var i in allViewBys){
        html += "<tr>";
        html += "<td>";
        html += "<label>"+allViewBys[i].replace(/\s/g, '');
        html += "<label>";
        html += "</td>";
        html += "<td>";
        html += "<input id='"+allViewBys[i].replace(/\s/g, '')+"_newName' placeholder='"+allViewBys[i]+"'><br>";
        html += "</td>";
        html += "</tr>";
    }
    html += "<tr>";
    html += "<td>";
    html += "<input type='button' value='Done' onclick='changeAttributeNames()'>";
    html += "</td>";
    html += "</tr>";
    html += "</table>";
    parent.$("#tempDashletDiv").html(html);
    parent.$("#tempDashletDiv").dialog('open');
}
 
function changeAttributeNames(){
    var allViewBys  = JSON.parse(parent.$("#viewby").val());
    var allViewIds = JSON.parse(parent.$("#viewbyIds").val());
    var newViewByNames  = [];
    for(var i in allViewBys){
        var newName=parent.$("#"+allViewBys[i].replace(/\s/g, '')+"_newName").val();
        if(typeof newName==='undefined' || newName==='undefined' || newName.trim()===''){
            newName=allViewBys[i];
        }
        newViewByNames.push(newName);
    }
    parent.$("#viewby").val(JSON.stringify(newViewByNames));
    var chartData=JSON.parse(parent.$("#chartData").val());
    var charts=Object.keys(chartData);
    for(var i in charts){
        var chartId=charts[i];
        chartData[chartId]["viewBys"][0]=newViewByNames[allViewIds.indexOf(chartData[chartId]["viewIds"][0])];
    }
    parent.$("#chartData").val(JSON.stringify(chartData));
    parent.$("#tempDashletDiv").dialog('close');
    saveLocalXtCharts('rename');
}


function downloadExcel(chartId)
{
    var chartData = JSON.parse(parent.$("#chartData").val());
    parent.$("#chartMeta").val(JSON.stringify(chartData));
     var ctxPath=parent.$("#ctxpath").val();
    $.ajax({
        type:'POST',
        async: false,
        data:parent.$("#graphForm").serialize()+"&reportId="+$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartID="+chartId+"&chartFlag=true",
        url: ctxPath+"/reportViewerAction.do?reportBy=getGraphData",
        success: function(data){
          alert(data)  
        }
    });
}

function getComboChartFlag(chartType){  // add by mayank sharma
    if(chartType==="Combo-India-Map" || chartType==="Combo-IndiaCity" || chartType==="Combo-Horizontal" || chartType==="Combo-US-Map" || chartType==="Combo-USCity-Map" || chartType==="Combo-Aus-State" || chartType==="Combo-Aus-City"){
        return true;
    }else{
        return false;
    }
}
function removePage(flag){
    var r=confirm('Are you sure,you want to delete this page?');
    if(r==true){
        var REPORTID='';
        try{
        
            REPORTID = parent.document.getElementById("REPORTID").value;
        }catch(e){
            REPORTID = parent.$("#graphsId").val();
        }
        $.ajax({
            async:false,
            url: "reportViewerAction.do?reportBy=removePage&reportId="+REPORTID+"&currentPage="+currentPage+"&flag="+flag,
            success: function(data){
                if(data=='failure'){
                    alert("Can't delete last page!");
                    return;
                }
                alert("Page Removed Succesfully");
                currentPage=data;
                parent.generateJsonDataReset(REPORTID,data);
            }
        
        });
    }
    else{
    }
}


function getSlider1(div,data,wid,hgt,minVal,maxVal,measureIndex,measureName,slidercount){ 
//    alert("Actual Data::"+JSON.stringify(data));
window.changedMeasureId=measureIndex;
if(window.measurechange){
    var chartData = JSON.parse(parent.$("#chartData").val());
//    measureIndex=window.changedMeasureId;
    chartData[div]["measureFilters"]={};
    window.flag=true;
    parent.range.axisVal={};
    parent.range.slidingVal={};
    parent.range.map={};
    
}

//d3.select("svg").append("div").attr("id","container123");
//d3.select('svg').append("<div style='width: 95%;'><span style='margin-bottom: -34px;margin-left: -90%;margin-top: 0%;'class='badge'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");
//$("#"+div).append("<div style='width: 92%; height: 33px;'><span style='margin-bottom: -34px;margin-left: -90%;margin-top: 0%;'class='badge'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");
if(slidercount===1){
        if(wid <700){
        d3.select("#svg_"+div).style('height',(hgt*.90)+'px');  
            $("#slideingdiv"+div).append("<div style='width: 92%; height: 33px;margin-top: 0px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");
        }else if(wid < 800){
            d3.select("#svg_"+div).style('height',(hgt*.90)+'px');
            $("#slideingdiv"+div).append("<div style='width: 92%; height: 33px;margin-top: 5px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");
        }
        else if(wid < 900){
            d3.select("#svg_"+div).style('height',(hgt*.85)+'px');
            $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_1' style='width: 92%; height: 33px;margin-top: 30px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");
        }else if(wid < 1100){
            d3.select("#svg_"+div).style('height',(hgt*.85)+'px');
            $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_1' style='width: 92%; height: 33px;margin-top: 35px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");
        }else if(wid < 1200){
            d3.select("#svg_"+div).style('height',(hgt*.85)+'px');
            $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_1' style='width: 92%; height: 33px;margin-top: 45px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");
}else{
            d3.select("#svg_"+div).style('height',(hgt*.85)+'px');
            $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_1' style='width: 92%; height: 33px;margin-top: 50px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");
}
    }else{
        if(wid <700){
          d3.select("#svg_"+div).style('height',(hgt*.75)+'px'); 
          $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_2' style='width: 92%; height: 33px;margin-top: 24px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");  
        }else if(wid <800){
          d3.select("#svg_"+div).style('height',(hgt*.78)+'px'); 
          $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_2' style='width: 92%; height: 33px;margin-top: 24px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");  
        }else if(wid <900){
          d3.select("#svg_"+div).style('height',(hgt*.78)+'px');
          $("#slideingdiv"+div+"_1").css({"margin-top":10});
          $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_2' style='width: 92%; height: 33px;margin-top: 24px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");  
        }else if(wid <1100){
          d3.select("#svg_"+div).style('height',(hgt*.80)+'px');
          $("#slideingdiv"+div+"_1").css({"margin-top":14});
          $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_2' style='width: 92%; height: 33px;margin-top: 24px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");  
        }else if(wid <1200){
          d3.select("#svg_"+div).style('height',(hgt*.80)+'px');
          $("#slideingdiv"+div+"_1").css({"margin-top":22});
          $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_2' style='width: 92%; height: 33px;margin-top: 24px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");  
        }else {
          d3.select("#svg_"+div).style('height',(hgt*.80)+'px');
          $("#slideingdiv"+div+"_1").css({"margin-top":28});
          $("#slideingdiv"+div).append("<div id='slideingdiv"+div+"_2' style='width: 92%; height: 33px;margin-top: 24px;'><span style='margin-bottom: -34px;margin-left: -95%;margin-top: 0%;'class='badgelabel'>"+measureName+"</span><input type='text' id='slider"+div+"_"+measureIndex+"'style='bottom: 10px;'/></div>");  
        }

    }

//d3.select('#slider'+div+'_'+measureIndex+'').html("");
var chartData = JSON.parse(parent.$("#chartData").val());
//    alert("dataSliderMinMaxValue: "+JSON.stringify(chartData[div]["dataSliderMinMaxValue"]));
var elementId= chartData[div]["meassureIds"];
var dataSliderMinMaxValue=chartData[div]["dataSliderMinMaxValue"];
var dataSlider=chartData[div]["dataSlider"];
var viewById = chartData[div]["viewIds"];
viewById=viewById[0];
var sliderAxisVal = chartData[div]["sliderAxisVal"];


//alert("viewById: "+JSON.stringify(viewById));

// alert(parent.range.slidingVal[measureIndex]+"measureIndexmeasureIndex  "+measureIndex)
//alert("chartData[div]['noDataInRange']"+chartData[div]['noDataInRange'])
if(chartData[div]['noDataInRange']==='true'){
    alert("No Data Available is This Range");
    window.flag=true;
}
//alert("measureCount"+window.measureCount);
//alert("Object"+Object.keys(dataSliderMinMaxValue).length)
    //if(window.measurechange){
//  measureIndex=window.changedMeasureId;  
//  parent.range.axisVal=dataSliderMinMaxValue[measureIndex][1]+"__"+dataSliderMinMaxValue[measureIndex][0];
//  parent.range.axisMap[measureIndex]=parent.range.axisVal;  
//  alert("parent.range.axisMap"+JSON.stringify(parent.range.axisMap))
//  parent.range.slidingVal[measureIndex]=parent.range.axisVal;
//  parent.range.measureMap["<>"]=parent.range.axisVal;
//parent.range.map[measureIndex]=parent.range.measureMap;
//window.measurechange=false;
//}else{
//alert(measureIndex+":measureIndex:");
//alert("measureFilters: "+JSON.stringify(chartData[div]["measureFilters"]))
parent.range.measureMap={};
parent.range.measureVal="";
//parent.range.map={};
//parent.range.axisMinVal=[];
//parent.range.axisMaxVal=[];
//alert(window.flag)
//alert("index"+dataSliderMinMaxValue[measureIndex][0]);
//if(!(measureIndex in dataSliderMinMaxValue)){
//    delete parent.range.axisMap[measureIndex];
//    delete parent.range.slidingVal[measureIndex];
//    delete parent.range.map[measureIndex];
//}
var axisVal;
if(!(measureIndex in parent.range.axisMap)|| window.flag){
  parent.range.axisVal=dataSliderMinMaxValue[measureIndex][1]+"__"+dataSliderMinMaxValue[measureIndex][0];
  parent.range.axisMap[measureIndex]=parent.range.axisVal;  
//        parent.range.sliderAxisVal={};
//            alert(JSON.stringify(parent.range.sliderAxisVal)+"***")
//            alert(JSON.stringify(sliderAxisVal)+"^^^")
//            alert(JSON.stringify(parent.range.axisMap)+"%%%")
//       alert(!(viewById in parent.range.sliderAxisVal)+"::: "+viewById)
       try{
           if(typeof sliderAxisVal!=='undefined'){
//               alert("using sliderAxisVal");
               parent.range.sliderAxisVal=sliderAxisVal;
           }else if(parent.range.sliderAxisVal!=='undefined'){
               if(!(viewById in parent.range.sliderAxisVal)){
            parent.range.sliderAxisVal[viewById]=parent.range.axisMap;
            
}
           }
//        else if(viewById in parent.range.sliderAxisVal){
//            var tempmap =parent.range.sliderAxisVal[viewById];
//            if(!(measureIndex in tempmap)){
//                parent.range.sliderAxisVal[viewById]=parent.range.axisMap;
//            }
//        }
        }catch(e){}
    }
//alert(typeof parent.range.slidingVal[measureIndex])
if(!(measureIndex in parent.range.slidingVal)|| window.flag){
    parent.range.slidingVal[measureIndex]=parent.range.axisVal;
}
if(!(measureIndex in parent.range.map)|| window.flag){
   // parent.range.map[measureIndex]=parent.range.axisVal;
//   parent.range.measureMap["<>"]=parent.range.axisVal;
   parent.range.measureMap["<>"]=parent.range.axisMap[measureIndex];
parent.range.map[measureIndex]=parent.range.measureMap;
//window.flag=false;
//window.measurechange=false;
}








//setting slider axis    
//alert("Object typeof"+Object.keys(parent.range.sliderAxisVal).length)
if(typeof parent.range.sliderAxisVal!=='undefined' && Object.keys(parent.range.sliderAxisVal).length!==0){
  if(typeof parent.range.sliderAxisVal[viewById]!=='undefined'){
    if( viewById in parent.range.sliderAxisVal ){
//alert(typeof sliderAxisVal[viewById])
        var tempAxismap=parent.range.sliderAxisVal[viewById];
        if(measureIndex in tempAxismap){
//            alert("in if "+JSON.stringify(tempAxismap))
            axisVal=tempAxismap;
            
        }
    }
    }else{
//            alert("in else")
            axisVal=parent.range.axisMap;
            parent.range.sliderAxisVal[viewById]=parent.range.axisMap;
        }
}else{
//        alert("in else2 "+JSON.stringify(parent.range.axisMap))
        axisVal=parent.range.axisMap;
        parent.range.sliderAxisVal[viewById]=parent.range.axisMap;
    }
if(typeof axisVal==='undefined'){
    axisVal=parent.range.axisMap;
        parent.range.sliderAxisVal[viewById]=parent.range.axisMap;
}
 //setting slider value   
var tempsliderVal;
var sliderVal={};
tempsliderVal=chartData[div]["measureFilters"];
//alert("tempsliderVal"+JSON.stringify(tempsliderVal))
if(typeof tempsliderVal!=='undefined'){
//    alert("LOL "+measureIndex in tempsliderVal)
    if(measureIndex in tempsliderVal){
    tempsliderVal=tempsliderVal[measureIndex];
    sliderVal[measureIndex]=tempsliderVal["<>"];
}else{
//    sliderVal={};
    sliderVal[measureIndex]=dataSliderMinMaxValue[measureIndex][1]+"__"+dataSliderMinMaxValue[measureIndex][0];;
}
}else{
// alert("this is else")
    sliderVal[measureIndex]=dataSliderMinMaxValue[measureIndex][1]+"__"+dataSliderMinMaxValue[measureIndex][0];;
}
    

    
    
    
//alert("parent.range.map[measureIndex]"+JSON.stringify(parent.range.map[measureIndex]));
//parent.range.axisMinVal.push(parseInt(minVal));
//parent.range.axisMaxVal.push(parseInt(maxVal));
//alert("float:"+parseFloat(parent.range.axisMap[measureArray[measureIndex]].split(",")[0]))
//alert("min:"+JSON.stringify(parent.range.min))
//min: parseFloat(parent.range.axisMap[measureIndex].split("__")[0]),
//    max: parseFloat(parent.range.axisMap[measureIndex].split("__")[1]),
//    from: parseFloat(parent.range.slidingVal[measureIndex].split("__")[0]),
//    to: parseFloat(parent.range.slidingVal[measureIndex].split("__")[1]),
//alert("slidingVal"+JSON.stringify(parent.range.slidingVal))
//alert("parent.range.map11 "+JSON.stringify(parent.range.map)+" slidercount "+slidercount);
var tempmap = parent.range.map;
        var tempval="";
        delete tempmap[measureIndex];
        for(var k in tempmap){
            tempval=tempmap[k];
        }
        const TEMVAL=tempval;
        graphProp(div);
        
// alert("sliderAxisVal: "+JSON.stringify(parent.range.sliderAxisVal));   
// alert("sliderVal: "+JSON.stringify(sliderVal));   
$('#slider'+div+'_'+measureIndex).ionRangeSlider({
    type: "double",
    min: (parseFloat(axisVal[measureIndex].split("__")[0])).toFixed(2),
    max: (parseFloat(axisVal[measureIndex].split("__")[1])).toFixed(2),
    from: (parseFloat(sliderVal[measureIndex].split("__")[0])).toFixed(2),
    to: (parseFloat(sliderVal[measureIndex].split("__")[1])).toFixed(2),
    grid: true,
    grid_num: 6,
//    postfix: addCommas(numberFormat(parseFloat(axisVal[measureIndex].split("__")[0]),yAxisFormat,yAxisRounding,div)),
    prefix: "",
    prettify_separator: ",",
    decorate_both: true,
    force_edges: true,
    drag_interval: true,
    onFinish: function (data) {
        window.sliderDrag=true;
        window.flag=false;
        window.measurechange=false;
        console.log("Sliding data:::"+JSON.stringify(data));
        parent.range.slidingVal[measureIndex]=data["from"]+"__"+data["to"];
        parent.range.measureMap["<>"]=parent.range.slidingVal[measureIndex];
        parent.range.clausemap[measureIndex]=parent.range.measureMap;
        parent.range.map[measureIndex]=parent.range.measureMap;
//        alert("parent.range.clausemap "+JSON.stringify(parent.range.clausemap))
//        alert("parent.range.map 22"+JSON.stringify(parent.range.map));
//        alert("tempval "+JSON.stringify(TEMVAL));
        for(measureIndex in parent.range.map){
        if(measureIndex in parent.range.clausemap){
            parent.range.map[measureIndex]=parent.range.clausemap[measureIndex];
        }else{
            parent.range.map[measureIndex]=TEMVAL;
        }
        }
        var preMeasureFilters=chartData[div]["measureFilters"];
        if(typeof preMeasureFilters!=='undefined' && Object.keys(preMeasureFilters).length > Object.keys(parent.range.clausemap).length){
            for(var k in preMeasureFilters){
            if(k in parent.range.clausemap){
                preMeasureFilters[k]=parent.range.clausemap[k];
            }
            chartData[div]["measureFilters"]=preMeasureFilters;
        }
        }else{
        chartData[div]["measureFilters"]=parent.range.clausemap;
        }
        
//        alert(JSON.stringify(chartData[div]["measureFilters"]))
//        alert("map:"+JSON.stringify(chartData[div]["measureFilters"]));
chartData[div]["Pattern"]='Gradient';
//        alert("id in val "+!(viewById in parent.range.sliderAxisVal))
//        if(!(viewById in parent.range.sliderAxisVal)){
        chartData[div]["sliderAxisVal"]=parent.range.sliderAxisVal;
//        }
       
        parent.$("#chartData").val(JSON.stringify(chartData));
        var ctxPath=parent.$("#ctxpath").val();
        $.ajax({
            type:'POST',
            async: false,
            data:parent.$("#graphForm").serialize()+"&reportId="+$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartID="+div+"&chartFlag=true",
            url: ctxPath+"/reportViewer.do?reportBy=drillCharts",
            success: function(data){
                         console.log("data:::"+JSON.parse(data)["data"])
                //             var meta = JSON.parse(JSON.parse(data)["meta"]);
                ////                            $("#viewby").val(JSON.stringify(meta["viewbys"]));
                //                            $("#measure").val(JSON.stringify(meta["measures"]));
                //                            $("#chartData").val(JSON.stringify(meta["chartData"])); 
                generateSingleChart(data,div);
            }

        })
    }
});
//}
}

function JSONToCSVConvertor(JSONData, ReportTitle,hideDate, ShowLabel) {
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    
    var CSV = '';    
    //Set Report title in first row or line
    
    CSV += ReportTitle + '\r\n\n';
    CSV += hideDate + '\r\n\n';
    //This condition will generate the Label/Header
    if (ShowLabel) {
        var row = "";
        
        //This loop will extract the label from 1st index of on array
        for (var index in arrData[0]) {
            
            //Now convert each value to string and comma-seprated
            row += index + ',';
        }

        row = row.slice(0, -1);
        
        //append Label row with line break
        CSV += row + '\r\n';
    }
    
    //1st loop is to extract each row
    for (var i = 0; i < arrData.length; i++) {
        var row = "";
        
        //2nd loop will extract each column and convert it in string comma-seprated
        for (var index in arrData[i]) {
            row += '"' + arrData[i][index] + '",';
        }

        row.slice(0, row.length - 1);
        
        //add a line break after each row
        CSV += row + '\r\n';
    }

    if (CSV == '') {        
        alert("Invalid data");
        return;
    }   
    
    //Generate a file name
    var fileName = "MyReport_";
    //this will remove the blank-spaces from the title and replace it with an underscore
    fileName += ReportTitle.replace(/ /g,"_");   
    
    //Initialize file format you want csv or xls
    var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
    
    // Now the little tricky part.
    // you can use either>> window.open(uri);
    // but this will not work in some browsers
    // or you will not get the correct file extension    
    
    //this trick will generate a temp <a /> tag
    var link = document.createElement("a");    
    link.href = uri;
    
    //set the visibility hidden so it will not effect on your web-layout
    link.style = "visibility:hidden";
    link.download = fileName + ".csv";
    
    //this part will append the anchor tag and remove it after automatic click
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function editMgmtTemplate(chartId) {
    $("#initializeCharts").dialog({
        autoOpen: false,
        height: $(window).height() - 20,
        width: $(window).width() - 100,
        position: 'justify',
        modal: true,
        resizable: true,
        title: 'Edit Management Template'
    });
    var templateMeta=JSON.parse(parent.$("#templateMeta").val());
    var templateDetails=templateMeta["template1"];
    var measures = templateDetails["defaultMeasures"];
    var measureIds = templateDetails["defaultMeasureIds"];
    var chartData = JSON.parse(parent.$("#templateMeta").val());
    var reportsData;
    var innerHtml="";
     var graphDrillMap=templateDetails["graphDrillMap"];
     $.ajax({
        type:'POST',
        async: false,
        url: parent.$("#ctxpath").val() + '/reportViewer.do?reportBy=reportDrillAssignment&reportId=' + parent.$("#graphsId").val(),
        success: function(data){
     
         reportsData = data;  
      
                                if (reportsData !== 'null') {
                                    var jsonVar = eval('(' + reportsData + ')')
                                    var reportIds = jsonVar.reportIds;
                                    var reportNames = jsonVar.reportNames;
                                    var assignRepIds = jsonVar.assignRepIds;


     innerHtml = "<table style='padding:3px 3px 3px 3px;width:100%;border-collapse:separate;border-spacing:10px 10px;'>";
    innerHtml += "<tr>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Measure</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Alias</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Fonts</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Number Format</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Rounding</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Prefix</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Suffix</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Comparison</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Change %</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Time</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span id='calenderIcon' style='display:none' class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Calender</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span id='periodIcon' style='display:none' class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Period</span> ";
    innerHtml += "</td>";
    innerHtml += "<td>";
    innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>Report Drill</span> ";
    innerHtml += "</td>";
    innerHtml += "</tr>";
    var chartId = "template1"
    for (var i = 0; i < (measures.length < 7 ? measures.length : 7); i++) {
        var timeFlag='false';
        innerHtml += "<tr>";
        innerHtml += "<td>";
        innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>" + measures[i] + "</span> ";
        innerHtml += "</td>";
//        innerHtml += "<td>";
//        innerHtml += "<td>";
        var alias = '';
        var alias2 = '';
        if (typeof chartData[chartId]["alias"] !== "undefined" && typeof chartData[chartId]["alias"][measureIds[i]+"_alias"] !== "undefined" && chartData[chartId]["alias"][measureIds[i]+"_alias"].trim()!=='') {
            alias = chartData[chartId]["alias"][measureIds[i]+"_alias"];
        }
        else {
            alias = measures[i];
        }
        if (typeof chartData[chartId]["alias2"] !== "undefined" && typeof chartData[chartId]["alias2"][measureIds[i]+"_alias2"] !== "undefined" && chartData[chartId]["alias2"][measureIds[i]+"_alias2"].trim()!=='') {
            alias2 = chartData[chartId]["alias2"][measureIds[i]+"_alias2"];
        }
        else {
            alias2 = "";
        }
        innerHtml += "<td>";
        innerHtml += "<input maxlength='20' placeholder='Alias 1' type='text' id='" + measureIds[i] + "_alias" + "'" + " value='" + alias + "' /><br>";
        innerHtml += "<input maxlength='20' placeholder='Alias 2' style='margin-top:5px' type='text' id='" + measureIds[i] + "_alias2" + "'" + " value='" + alias2 + "' />";
        innerHtml += "</td>";

var fontSize = "13";
            if (typeof chartData[chartId]["fontSize"] !== "undefined" && chartData[chartId]["fontSize"] != "") {
                fontSize = chartData[chartId]["fontSize"][measureIds[i]+"_fontSize"];
            }
        innerHtml += "<td>";
    innerHtml += "<select style='width: 40px;' id='" + measureIds[i] + "_fontSize" + "'" + " value='" + fontSize + "'>";
for(var f=8;f<=13;f++){
if(f == fontSize){innerHtml += "<option selected>"+f+"</option>";}
else{innerHtml += "<option>"+f+"</option>";}
}
innerHtml += "</select>";
        
var fontColor = "#a1a1a1";
            if (typeof chartData[chartId]["fontColor"] !== "undefined" && chartData[chartId]["fontColor"] != "") {
                fontColor = chartData[chartId]["fontColor"][measureIds[i]+"_fontColor"];
            }
    innerHtml += "<input style='background-color:" + fontColor + ";width: 40px;margin-top: 7px' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='" + measureIds[i] + "_fontColor" + "'" + " readonly>";

//    if (typeof chartData[chartId]["fontColor"] !== "undefined" && chartData[chartId]["fontColor"] != "") {
//        fontColor = chartData[chartId]["fontColor"][measureIds[i]+"_fontColor"];
//    }
//
//    innerHtml += "<input style='background-color:" + fontColor + ";width: 40px;margin-top: 7px' type='button' value='' onclick='moreColors(\"" + chartId + "\",this.id)' id='" + measureIds[i] + "fontColor" + "'" + " value='" + fontColor + "' readonly>";
innerHtml += "</td>";

        innerHtml += "<td>";
        
        innerHtml += "<select id='" + measureIds[i] + "_format" + "'>";
        if (typeof chartData[chartId]["format"] === 'undefined' ||
                typeof chartData[chartId]["format"][measureIds[i] + "_format"] === "undefined" ||
                chartData[chartId]["format"][measureIds[i] + "_format"] === "" ||
                chartData[chartId]["format"][measureIds[i] + "_format"] === "Auto") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Auto' selected>Auto</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Auto'>Auto</option>";
        }
        if (typeof chartData[chartId]["format"] !== 'undefined' &&
                typeof chartData[chartId]["format"][measureIds[i] + "_format"] !== "undefined" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] !== "" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] === "Absolute") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
        }
        if (typeof chartData[chartId]["format"] !== 'undefined' &&
                typeof chartData[chartId]["format"][measureIds[i] + "_format"] !== "undefined" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] !== "" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] === "K") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='K' selected>Thousand</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='K'>Thousand</option>";
        }
        if (typeof chartData[chartId]["format"] !== 'undefined' &&
                typeof chartData[chartId]["format"][measureIds[i] + "_format"] !== "undefined" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] !== "" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] === "M") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='M' selected>Million</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='M'>Million</option>";
        }
        if (typeof chartData[chartId]["format"] !== 'undefined' &&
                typeof chartData[chartId]["format"][measureIds[i] + "_format"] !== "undefined" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] !== "" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] === "Cr") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Cr' selected>Crore</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Cr'>Crore</option>";
        }
        if (typeof chartData[chartId]["format"] !== 'undefined' &&
                typeof chartData[chartId]["format"][measureIds[i] + "_format"] !== "undefined" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] !== "" &&
                chartData[chartId]["format"][measureIds[i] + "_format"] === "L") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='L' selected>Lakh</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12'value='L'>Lakh</option>";
        }
        innerHtml += "</select>";
        innerHtml += "</td>";
        innerHtml += "<td>";
        innerHtml += "<select id='" + measureIds[i] + "_rounding" + "'>";
        if (typeof chartData[chartId]["rounding"] === 'undefined' ||
                typeof chartData[chartId]["rounding"][measureIds[i] + "_rounding"] === "undefined" ||
                chartData[chartId]["rounding"][measureIds[i] + "_rounding"] === "1") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='1' selected>1 Decimal</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='1'>1 Decimal</option>";
        }

        if (typeof chartData[chartId]["rounding"] !== 'undefined' &&
                typeof chartData[chartId]["rounding"][measureIds[i] + "_rounding"] !== "undefined" &&
                chartData[chartId]["rounding"][measureIds[i] + "_rounding"] === "0") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='0' selected>No Decimal</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='0'>No Decimal</option>";
        }

        if (typeof chartData[chartId]["rounding"] !== 'undefined' &&
                typeof chartData[chartId]["rounding"][measureIds[i] + "_rounding"] !== "undefined" &&
                chartData[chartId]["rounding"][measureIds[i] + "_rounding"] === "2") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='2' selected>2 Decimal</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='2'>2 Decimal</option>";
        }
        innerHtml += "</select>";
        innerHtml += "</td>";
        var prefix = '';
        if (typeof chartData[chartId]["prefix"] !== "undefined" && typeof chartData[chartId]["prefix"][measureIds[i]+"_prefix"] !== "undefined") {
            prefix = chartData[chartId]["prefix"][measureIds[i]+"_prefix"];
        }
        else {
            prefix = "";
        }
        innerHtml += "<td>";
        innerHtml += "<input type='text' id='" + measureIds[i] + "_prefix" + "'" + " value='" + prefix + "' />";
        innerHtml += "</td>";
        var suffix = '';
        if (typeof chartData[chartId]["suffix"] !== "undefined" && typeof chartData[chartId]["suffix"][measureIds[i]+"_suffix"] !== "undefined") {
            suffix = chartData[chartId]["suffix"][measureIds[i]+"_suffix"];
        }
        else {
            suffix = "";
        }
        innerHtml += "<td>";
        innerHtml += "<input type='text' id='" + measureIds[i] + "_suffix" + "'" + " value='" + suffix + "' />";
        innerHtml += "</td>";
        innerHtml += "<td>";
        innerHtml += "<select id='" + measureIds[i] + "_comparison" + "'>";
        if (typeof chartData[chartId]["comparison"] === "undefined" ||
                typeof chartData[chartId]["comparison"][measureIds[i] + "_comparison"] === "undefined" ||
                chartData[chartId]["comparison"][measureIds[i] + "_comparison"] === "no") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='no' selected>No</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='no'>No</option>";
        }

        if (typeof chartData[chartId]["comparison"] !== 'undefined' &&
                typeof chartData[chartId]["comparison"][measureIds[i] + "_comparison"] !== "undefined" &&
                chartData[chartId]["comparison"][measureIds[i] + "_comparison"] === "yes") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='yes' selected>Yes</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='yes'>Yes</option>";
        }

        innerHtml += "</select>";
        innerHtml += "</td>";
        innerHtml += "<td>";
        innerHtml += "<select id='" + measureIds[i] + "_changePercent" + "'>";
        if (typeof chartData[chartId]["changePercent"] === "undefined" ||
                typeof chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] === "undefined" ||
                chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] === "no") {
                innerHtml += "<option class='gFontFamily gFontSize12' value='no' selected>No</option>";
        }
        else{
                innerHtml += "<option class='gFontFamily gFontSize12' value='no'>No</option>";
        }
        if (typeof chartData[chartId]["changePercent"] !== "undefined" &&
                typeof chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] !== "undefined" &&
                chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] === "MOMPer") {
                innerHtml += "<option class='gFontFamily gFontSize12' value='MOMPer' selected>MOM %</option>";
        }
        else{
                innerHtml += "<option class='gFontFamily gFontSize12' value='MOMPer'>MOM %</option>";
        }
	if (typeof chartData[chartId]["changePercent"] !== "undefined" &&
                typeof chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] !== "undefined" &&
                chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] === "MOYMPer") {
                innerHtml += "<option class='gFontFamily gFontSize12' value='MOYMPer' selected>MOYM %</option>";
        }
        else{
                innerHtml += "<option class='gFontFamily gFontSize12' value='MOYMPer'>MOYM %</option>";
        }
        if (typeof chartData[chartId]["changePercent"] !== "undefined" &&
                typeof chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] !== "undefined" &&
                chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] === "QOQPer") {
                innerHtml += "<option class='gFontFamily gFontSize12' value='QOQPer' selected>QOQ %</option>";
        }
        else{
                innerHtml += "<option class='gFontFamily gFontSize12' value='QOQPer'>QOQ %</option>";
        }
	if (typeof chartData[chartId]["changePercent"] !== "undefined" &&
                typeof chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] !== "undefined" &&
                chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] === "QOYQPer") {
                innerHtml += "<option class='gFontFamily gFontSize12' value='QOYQPer' selected>QOYQ %</option>";
        }
        else{
                innerHtml += "<option class='gFontFamily gFontSize12' value='QOYQPer'>QOYQ %</option>";
        }
        if (typeof chartData[chartId]["changePercent"] !== "undefined" &&
                typeof chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] !== "undefined" &&
                chartData[chartId]["changePercent"][measureIds[i] + "_changePercent"] === "YOYPer") {
                innerHtml += "<option class='gFontFamily gFontSize12' value='YOYPer' selected>YOY %</option>";
        }
        else{
                innerHtml += "<option class='gFontFamily gFontSize12' value='YOYPer'>YOY %</option>";
        }
        innerHtml += "</select>";
        innerHtml += "</td>";
        innerHtml += "<td>";
        innerHtml += "<span class='gFontFamily gFontSize12 fontWeightBold' align:left;'><select id='calenIcon" + i + "' onchange='showDateIcon(this.id)'>"
//        try{
//            alert(chartData[chartId]["timeDetails"][measureIds[i] + "_date"]);
//        }
//        catch(e){}
        var timeValue='',timeType='';
        if (typeof chartData[chartId]["timeDetails"] === 'undefined' ||
                typeof chartData[chartId]["timeDetails"][measureIds[i] + "_date"] === "undefined") {
            innerHtml += "<option value='no' selected>No</option>"
        }
        else{
            innerHtml += "<option value='no'>No</option>"
        }
        if (typeof chartData[chartId]["timeDetails"] !== 'undefined' &&
                typeof chartData[chartId]["timeDetails"][measureIds[i] + "_date"] !== "undefined") {
            innerHtml += "<option value='yes' selected>Yes</option>";
            timeValue=chartData[chartId]["timeDetails"][measureIds[i] + "_date"][2];
            timeType=chartData[chartId]["timeDetails"][measureIds[i] + "_date"][3];
        }
        else {
            innerHtml += "<option value='yes'>Yes</option>";
        }
        innerHtml += "</select></span></td>";
        innerHtml += "<td>";
        innerHtml += "<span id='calenIcon"+i+"date' style='display:none;cursor:pointer;width: 130px;' class='gFontFamily gFontSize12 fontWeightBold dateIcons' align:left;'><input id='customDate"+i+"' type='text' style='width: 100px;' value='"+timeValue+"'/></span> ";
        innerHtml += "</td>";
        innerHtml += "<td>";
        innerHtml += "<span id='calenIcon"+i+"period' style='display:none;cursor:pointer;' class='gFontFamily gFontSize12 fontWeightBold' align:left;'><select id='timeType"+i+"'>"
        if(typeof timeType==='undefiend' || timeType==='month'){
            innerHtml += "<option value='month' selected>Month</option>"
        }
        else{
            innerHtml += "<option value='month'>Month</option>"
        }
        if(timeType==='quarter'){
            innerHtml += "<option value='quarter' selected>Quarter</option>"
        }
        else{
            innerHtml += "<option value='quarter'>Quarter</option>"
        }
        if(timeType==='year'){
            innerHtml += "<option value='year' selected>Year</option>"
        }
        else{
            innerHtml += "<option value='year'>Year</option>"
        }
        innerHtml += "</select></span> ";
        innerHtml += "</td>";
//        innerHtml += "<td>";
//        innerHtml += "<select id='" + measureIds[i] + "_comparison" + "'>";
//        if (typeof chartData[chartId]["comparison"] === "undefined" ||
//                typeof chartData[chartId][measureIds[i] + "_comparison"] === "undefined" ||
//                chartData[chartId][measureIds[i] + "_comparison"] === "no") {
//            innerHtml += "<option class='gFontFamily gFontSize12' value='no' selected>No</option>";
//        } else {
//            innerHtml += "<option class='gFontFamily gFontSize12' value='no'>No</option>";
//        }
//
//        if (typeof chartData[chartId]["comparison"] !== 'undefined' &&
//                typeof chartData[chartId]["comparison"][measureIds[i] + "_comparison"] !== "undefined" &&
//                chartData[chartId]["comparison"][measureIds[i] + "_comparison"] === "yes") {
//            innerHtml += "<option class='gFontFamily gFontSize12' value='yes' selected>Yes</option>";
//        } else {
//            innerHtml += "<option class='gFontFamily gFontSize12' value='yes'>Yes</option>";
//        }
//
//        innerHtml += "</select>";
//        innerHtml += "</td>";
//        innerHtml += "<td>";
            innerHtml += "<td id='singleReportTd" + measureIds[i] + "'><select id='reportToDrill_" + measureIds[i] + "' style='width:100%;' name='reportToDrill'>";
            var index='';
                    if(i==0){
                        index='';
                    }
                    else{
                        index=i;
                    }
            if (typeof graphDrillMap!=='undefined' && typeof graphDrillMap[measureIds[i]]!=='undefined')
                innerHtml += "<option selected class='gFontFamily ' value='0'>NOT_SELECTED</option>";
            else
                innerHtml += "<option class='gFontFamily ' value='0'>NOT_SELECTED</option>";
            
            for (var j = 0; j < reportIds.length; j++) {
                if (typeof graphDrillMap!=='undefined' && typeof graphDrillMap[measureIds[i]]!=='undefined' && graphDrillMap[measureIds[i]] == reportIds[j])
                    innerHtml += "<option selected value='" + reportIds[j] + "'>" + reportNames[j] + "</option>";
                else
                    innerHtml += "<option value='" + reportIds[j] + "'>" + reportNames[j] + "</option>";
            }
            innerHtml += "</select></td>";
//            innerHtml += "</td>";
            innerHtml += "</tr>";
    }
    innerHtml += "</tr>";
    innerHtml += "<tr>";
    innerHtml += "<td colspan='7' style='text-align:center'>";
    innerHtml += "<input  type='button' onclick='setTemplateDetails()' value=' Done '/>";
    innerHtml += "</td>";
    innerHtml += "</tr>";
    innerHtml += "</table>";
                                }
                                 }
                             })
    $("#initializeCharts").html(innerHtml);
    $("#initializeCharts").dialog('open');
    $( ".dateIcons>input" ).datepicker({
        showOn: "button",
        buttonText: "<i class='fa fa-calendar' style='font-size: 18px;margin-left: 5px;'></i>",
        changeMonth: true,
        changeYear: true
    });
    $(".ui-datepicker-trigger").css({
        "border":"none",
        "background":"none"
    });
    for (var i = 0; i < (measures.length < 7 ? measures.length : 7); i++) {
        showDateIcon('calenIcon'+i);
    }
}

function showDateIcon(id){
    //alert($("#"+id).val());
    if($("#"+id).val() == "yes"){
        $("#"+id+"date").css("display","block").show();
        $("#"+id+"period").css("display","block").show();
        $("#calenderIcon,#periodIcon").show();
    }
    else{
        $("#"+id+"date").css("display","none").hide();
        $("#"+id+"period").css("display","none").hide();
    }
    if(! $(".dateIcons").is(":visible")){
        $("#calenderIcon,#periodIcon").hide();
    }
    
}

function drillTemplateToReport(measureId){
    
    var templateMeta = JSON.parse(parent.$("#templateMeta").val());
    var templateDetails = templateMeta["template1"];
    var graphMapToDrill = templateDetails["graphDrillMap"];
    if(typeof graphMapToDrill==='undefined' || typeof graphMapToDrill[measureId]==='undefined' || graphMapToDrill[measureId]==='0'){
        return;
    }
    var reportIdtoDrill = graphMapToDrill[measureId];
    var url = "";
     url = "/reportViewer.do?reportBy=viewReport&REPORTID=" + reportIdtoDrill + "&action=open&reportDrill=Y";
     document.frmParameter.action = parent.$("#ctxpath").val() + url;
    document.frmParameter.target = "_blank";
    document.frmParameter.submit();
    document.frmParameter.target = "";
}

function buildcomboTopKPI(comboMap){
    var chartId = comboMap["newchartid"];
    var data = comboMap["currData"];
    var columns = comboMap["viewBys"];
    var columnsId = comboMap["viewIds"];
    var measureArray = comboMap["meassures"];
    var measureId = comboMap["meassureIds"];
    var width = comboMap["chWidth"];
    var height = comboMap["chHeight"];
    var agType = [];
    agType.push(comboMap["aggregation"]);
    var chartData= {};
    var advanceChartData= {};

                   var reportId = "";
                   try{
                   reportId  = document.getElementById("reportId").value;
                   }catch(e){
                     reportId = parent.$("#graphsId").val();  
                   }
    
   var draggableViewBys=[];
   for(var i=0;i<5;i++){
       columns;
                        var chartDetails={}; 
                        var viewBys=[];
                        var viewIds=[];
                        var meassures=[];
                        
                            draggableViewBys.push(columns);
                            viewBys.push(columns);
                            viewIds.push(columnsId);
                            
                            chartDetails["defaultMeasures"] = measureArray;
                            chartDetails["defaultMeasureIds"] = measureId;
                            chartDetails["viewBys"] = viewBys;
                            
                            if(i<4){
                            chartDetails["chartType"] = "Standard-KPI1";
                            chartDetails["row"] = "1";
                            chartDetails["col"] = (i)*8+"1";
                            chartDetails["size_x"] = "7";
                            chartDetails["size_y"] = "3";
                            var meas =[];
                            var measId =[];
                            meas.push(measureArray[i]);
                            measId.push(measureId[i]);
                             chartDetails["meassures"] = meas;
                            chartDetails["meassureIds"] = measId; 
                             window.complexId = "chart" +(i+1);
                            }else{ 
                            chartDetails["chartType"] = "Vertical-Bar";
                            chartDetails["size_x"] = "28";
                            chartDetails["size_y"] = "10";
                            chartDetails["row"] = "4";
                            chartDetails["col"] = "1";
//                            var meas =[];
//                            var measId =[];
//                            meas.push(measureArray[i]);
//                            measId.push(measureId[i]);
                             chartDetails["meassures"] = measureArray;
                            chartDetails["meassureIds"] = measureId;                    
                              }
                            chartDetails["viewByLevel"] = "single";
                           
                            chartDetails["aggregation"] = agType;
                            chartDetails["viewIds"] = viewIds;
                            chartDetails["dimensions"] = viewIds;
                            
                            chartDetails["records"] = "12";
                            
                            chartDetails["size"] = "S";
                            chartDetails["othersL"]="N";
                            chartData["chart" + (i + 1)] = chartDetails;
                           
                   
                    }
          for(var i in Object.keys(chartData)){
              if(i<4){
            var chartMap = {};
            chartMap["chart5"] = chartData["chart5"];
    advanceChartData["chart" +(parseInt(i)+1)]=chartMap;;
              }
          }          
    parent.$("#draggableViewBys").val(JSON.stringify(draggableViewBys));
    parent.$("#chartData").val(JSON.stringify(chartData));
    parent.$("#advanceChartData").val(JSON.stringify(advanceChartData));
    parent.$("#advanceChartType").val("advanceGraph");

    $.ajax({
        type: 'POST',
        data: parent.$("#graphForm").serialize(),
        url: $("#ctxpath").val() + "/reportViewer.do?reportBy=buildchartsWithObject&reportId=" + reportId + "&reportName=" + encodeURIComponent(parent.$("#graphName").val()) + "&chartData=" + encodeURIComponent(parent.$("#chartData").val()),
        success: function(data) {
            parent.generateChart(data);
        }
    })
                    
}

 function callInitializeChart(chartId){
//     var advanceChartData = JSON.parse(parent.$("#advanceChartData").val());
     var chartData = JSON.parse(parent.$("#chartData").val());
    var chartDetails = chartData["chart5"];
    window.complexId = chartId;
     var advanceChartData;
    try{
        
    advanceChartData = JSON.parse(parent.$("#advanceChartData").val());
    }catch(e){
        
    }       
    if(typeof advanceChartData !=="undefined"){
       var advanceDetails = advanceChartData[window.complexId]["chart5"]; 
       chartDetails["chartType"]=advanceDetails["chartType"];
       parent.$("#advanceChartData").val(JSON.stringify(advanceChartData));
       parent.$("#chartData").val(JSON.stringify(chartData));
    }
    var index = chartId.replace("chart","");
      generateSingleChartAdvance("chart5",parseInt(index));
 }
 
 
 function generateSingleChartAdvance(chartId,pos){
  
     var chartData = JSON.parse(parent.$("#chartData").val());
     var chartDetails = chartData["chart5"];
     var advanceChartData;
    try{
        
    advanceChartData = JSON.parse(parent.$("#advanceChartData").val());
    }catch(e){
        
    }       
    var ch = chartId;
    var currData=[];
    var records = 10;
    $("#"+ch).html("");
   var index = pos-1;
    var measName = chartData["chart"+pos]["meassures"];
    var measNameOfInit = chartData[chartId]["meassures"];
//    var viewby=chartData["chart"+pos]["viewBys"];
//    var viewId=chartData["chart"+pos]["viewIds"];
    var index1 = measNameOfInit.indexOf(measName[0]);
    if (index1 > -1) {
    measNameOfInit.splice(index1, 1);
}
var list=[];
list.push(measName[0]);
var measId = [];
measId.push(getMeasureIds(measName[0]));
//for(var k in measNameOfInit){
//   if(list.indexOf(measNameOfInit[k])===-1)
//         list.push(measNameOfInit[k]);
//}

 if(typeof advanceChartData !=="undefined"){
       var advanceDetails = advanceChartData[window.complexId]["chart5"]; 
       advanceDetails["meassures"]=list;
 advanceDetails["meassureIds"]= measId;
       chartDetails = advanceDetails;
         
       parent.$("#advanceChartData").val(JSON.stringify(advanceChartData));
//       parent.$("#chartData").val(JSON.stringify(chartData));
 }
 chartData[chartId]=chartDetails;
       parent.$("#chartData").val(JSON.stringify(chartData));
       
 var isEdit="Y";
   var chartFlag = "true";
    $.ajax({
        type: 'POST',
        async: false,
        data: $("#graphForm").serialize(),
        url: parent.$("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&chartID="+chartId+"&isEdit="+isEdit+"&editId="+chartId+"&action=localfilterGraphs"+"&chartFlag="+chartFlag,
        success: function(data) {
//            alert(JSON.stringify(data))
        generateSingleChart(data,chartId);
    }
       })      
}

function getMeasureIds(measureName){
    var allMeasures=JSON.parse(parent.$("#measure").val());
    var allMeasureIds=JSON.parse(parent.$("#measureIds").val());
    return allMeasureIds[allMeasures.indexOf(measureName)];
}

/*Added by Ashutosh*/
function setActionDrillCharts(chartId,n,add){
    var chartData = JSON.parse(parent.$("#chartData").val());
//    alert(JSON.stringify(tempActionDrillMap))
    try{
    var tempActionDrillMap=chartData[chartId]["actionDrillMap"];
     if(typeof window["actionCount_" + chartId]==='undefined'){
    var tempactionCount=tempActionDrillMap["drill1"];
    if(typeof tempactionCount["actionCount"]!=='undefined'){
        var actionCount_arr=tempactionCount["actionCount"];
        window["actionCount_" + chartId]=parseInt(actionCount_arr[0]);
        }
     }
     }catch(e){}
      if(typeof window["actionCount_" + chartId]==='undefined' || window["actionCount_" + chartId]===0){
        window["actionCount_" + chartId] = 1;
     }
    $("#addActionChartDiv").dialog({
        autoOpen: false,
        height: 400,
        width: 900,
        position: 'justify',
        modal: true,
        resizable: true,
        title: "Add Action Chart"
    });
    
//    $("#addActionChartDiv").html("");
    $("#addActionChartDiv").dialog('open');
    if(typeof window["actionCount_" + chartId]!=='undefined'&& window["actionCount_" + chartId] > 3){
        return;
    }
    if(add){
        setActionChartDetails(chartId);
        window["actionCount_" + chartId]=window["actionCount_" + chartId]+1;
    }
    n=window["actionCount_" + chartId];
   
    var chartData = JSON.parse(parent.$("#chartData").val());
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var measIds = JSON.parse(parent.$("#measureIds").val());
    var aggregNames = JSON.parse(parent.$("#aggregation").val());
    var chartG=parent.chartG;
    
    //    var htmlvar =  "<table style='' width='100%'><tr><td class='gFontFamily gFontSize12' >";
    //    htmlvar += "<div id='actionDiv1' style='float:left;width:"+divWidth/3+"px;height:"+divHeight+"px;background-color:#d2d2d2;'>";
    //    htmlvar += "<ul><lable style='padding: 100px;font-size: 14px;font-weight: 700;'>Viewbys</lable>"
    //    for(var k=0; k < viewOvName.length; k++){
    //    htmlvar += "<li class='gFontFamily gFontSize12 fontWeight400' style='margin-left: 15px;'>"
    //    htmlvar += "<input type='checkbox' class='ckbox' id='L_" + viewOvIds[k] + "'  value='" + viewOvName[k] + "' name='" + viewOvName[k]+ "'/>"+ viewOvName[k]+"";
    //    htmlvar += "</li>"
    //    }
    //    htmlvar += "</ul></div></td>"
    //    
    //    htmlvar += "<td class='gFontFamily gFontSize12'>"
    //    htmlvar += "<div id='actionDiv2' style='float:left;width:"+divWidth/3+"px;height:"+divHeight+"px;background-color:#d2d2d2;'>";
    //    htmlvar += "<ul><lable style='padding: 100px;font-size: 14px;font-weight: 700;'>Measures</lable>"
    //    for(var k=0; k < measName.length; k++){
    //    htmlvar += "<li class='gFontFamily gFontSize12 fontWeight400' style='margin-left: 15px;'>"
    //    htmlvar += "<input type='checkbox' class='ckbox' id='L_" + measIds[k] + "'  value='" + measName[k] + "' name='" + measName[k]+ "'/>"+ measName[k]+"";
    //    htmlvar += "</li>"
    //    }
    //    htmlvar += "</ul></div></td>"
    //    
    //    
    //    htmlvar += "<td class='gFontFamily gFontSize12'>"
    //    htmlvar += "<div id='actionDiv2' style='float:left;width:"+divWidth/3+"px;height:"+divHeight+"px;background-color:#d2d2d2;'>";
    //    htmlvar += "<lable style='padding: 100px;font-size: 14px;font-weight: 700;'>Chart Type</lable>"
    //    htmlvar += "<div id='vaccordian' style='box-shadow: 0 0px 0px 0px;'>"
    //    htmlvar += "<ul id='chartTypeMenu' style='overflow-y: hidden;'><li><ul id='chartType_nav' style='display: block;'></ul></li></ul>"
    //    htmlvar += "</div>"
    //    htmlvar += "</div></td>"
    //    htmlvar += "</tr></table>"
    //    htmlvar +="<div style='position: absolute; bottom: 10px; text-align: center; margin-left: 45%;'><input value='"+translateLanguage.Done+"' class='gFontFamily gFontSize12' onclick='saveFilters1(\"" + chartId + "\")'  type='button'></div>";
    //     $("#addActionChartDiv").html(htmlvar);
    //  var vNav="";  
    //    for(var i=0;i<parent.chartGroups.length;i++){
    //    vNav += "<h3 '><li><a style='color: #585D62;' href='#'  onclick='slide_ul(chart_sub_menu"+ i +")'>" + parent.chartGroups[i] + "</a><ul id='chart_sub_menu" + i + "' style='display: none;'></ul></li></h3>";
    //    }
    //    $("#chartType_nav").append(vNav);
    //    for(var j=0;j<parent.chartG.length;j++){
    //    
    ////        alert(":::"+parent.chartG[j])
    //    var tags="";
    //        for(var i=0;i < eval(chartG[j]).length;i++){
    //tags += "<li style='margin-left: 15px;font-weight: initial;'><input type='radio'  id='L_" +eval(chartG[j])[i] + "'  value='" + eval(chartG[j])[i]+ "' name='group1'/><a style='color: #585D62;' 'href='#' >" + eval(chartG[j])[i] + "</a></li>";
    //        }
    //      $("#chart_sub_menu"+j).append(tags);  
    //    }
    
    
    var htmlvar =  "<table style='' width='100%'>";
    htmlvar += "<tr class='gFontFamily gFontSize12' style='padding: 10px;font-size: 14px;font-weight: 600;color: rgb(144, 141, 136);'><td></td><td style='padding: 10px;'>ViewBys</td><td>Measures</td><td>ChartType</td></tr>"
    for(var l=0; l<n; l++){
        htmlvar += "<tr class='gFontFamily gFontSize12'>";
        htmlvar += "<td><lable style='font-size:14px;'>Action Chart-"+(l+1)+"</lable></td>";
        
        htmlvar += "<td><h3  onclick='slideVibys(event,\"" + (l+1) + "\",\"" + chartId + "\")'>Select ViewBys</h3>";
        htmlvar += "<ul id='viewby_"+(l+1)+"_"+chartId+"' class='dropdown-menu repTblMnu hideDropDown' style='background-color: rgb(253, 253, 253);top:auto;margin-left: 20%;'>";
        for(var k=0; k < viewOvName.length; k++){
         htmlvar += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input id='viewby_"+(l+1)+"_"+chartId+"_"+k+"'type='checkbox' value='"+viewOvIds[k]+"' style='margin-left:3px;' onclick=storearray(event,'TIME','TIME') ><span style='color:black;margin-left:1em;'>"+viewOvName[k]+"</span></div></li>";
        }
        htmlvar +=  "</ul></td>";
        
        htmlvar +="<td><h3  onclick='slideMeasure(event,\""+(l+1)+"\",\""+chartId+"\")'>Select Measure</h3>";
        htmlvar +="<ul id='measure_"+(l+1)+"_"+chartId+"' class='dropdown-menu repTblMnu hideDropDown' style='background-color: rgb(253, 253, 253);top:auto;margin-left: 44%;'>";
        for(var k=0; k < measName.length; k++){
        htmlvar += "<li style='padding:2px;'><div style=\"border: 1px solid #F4F4F4; padding:3px; border-radius: 1px;\"><input id='measure_"+(l+1)+"_"+chartId+"_"+k+"' type='checkbox' value='"+measIds[k]+"' style='margin-left:3px;' onclick=storearray(event,'TIME','TIME') ><span style='color:black;margin-left:1em;'>"+measName[k]+"</span></div></li>";
        }
        htmlvar +=  "</ul></td>";
        
        htmlvar +="<td><select id='chartType_" + (l+1) +"_"+chartId+"' style='font-size:9pt;' name='Dashname'>";
        for(var j=0;j<parent.actionChartsType.length;j++){
            htmlvar += "<option class='gFontFamily gFontSize12' id='L_" + parent.actionChartsType[j] + "'  value='" + parent.actionChartsType[j] + "' name='" + parent.actionChartsType[j]+ "' selected>" + parent.actionChartsType[j]+ "</option>";    
        }
        htmlvar +=  "</td></tr><tr><td><br></td></tr>";
    }
    htmlvar +=  "</table>";
    
    htmlvar +="<div style='position: absolute; bottom: 10px; text-align: center; width:100%;'>";
    htmlvar +="<input name='"+ chartId +"' style='width:20%;height:25px;border-radius: 25px;font-size:14px;' value='"+translateLanguage.Done+"' class='gFontFamily gFontSize12' onclick='setActionChartDetails(this.name)'  type='button'/>&nbsp&nbsp";
    htmlvar +="<input style='width:20%;height:25px;border-radius: 25px;font-size:14px;' value='Add' class='gFontFamily gFontSize12' onclick='setActionDrillCharts(\"" + chartId + "\",\"" + window["actionCount_" + chartId] + "\",\"" + true + "\")'  type='button'/>";
    htmlvar +="</div>";
    $("#addActionChartDiv").html(htmlvar);
try{
        var tempActionDrillMap=chartData[chartId]["actionDrillMap"];
        var tempVibys;
        var temMeasures;
        var tempChartType;
        var tempDrillMap;
        for(var i=0; i<Object.keys(tempActionDrillMap).length; i++){
           tempDrillMap=tempActionDrillMap["drill"+(i+1)];
           tempVibys= tempDrillMap["viewBys"];
           temMeasures=tempDrillMap["measures"];
           tempChartType=tempDrillMap["chartType"];
           for(var k=0; k < viewOvName.length; k++){
               if(IsIdInArray(tempVibys,$("#viewby_"+(i+1)+"_"+chartId+"_"+k).val())){
                    document.getElementById("viewby_"+(i+1)+"_"+chartId+"_"+k).checked = true;
                    
               }
           }
           for(var k=0; k < measName.length; k++){
               if(IsIdInArray(temMeasures,$("#measure_"+(i+1)+"_"+chartId+"_"+k).val())){
                    document.getElementById("measure_"+(i+1)+"_"+chartId+"_"+k).checked = true;
              }
           }
            $("#chartType_"+(i+1)+"_"+chartId).val(tempChartType[0])
        }
    }catch(err){}

    
    $("#addActionChartDiv").dialog('open');
   
}
function IsIdInArray(arr,obj) {
    return (arr.indexOf(obj) != -1);
}
function slideVibys(event,num,id){
    event.stopPropagation();
        $(".hideDropDown").slideUp("fast");
        $("#viewby_" + num +"_"+id).slideToggle("slow");
   }
function slideMeasure(event,num,id){
    event.stopPropagation();
        $(".hideDropDown").slideUp("fast");
        $("#measure_" + num +"_"+id).slideToggle("slow");
   }
   
   function setActionChartDetails(chartId){
    var chartData = JSON.parse(parent.$("#chartData").val());
    var viewBys = JSON.parse(parent.$("#viewby").val());
    var viewByIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var measures = chartData[chartId]["meassures"];
    var meassureIds = chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var chartType=chartData[chartId]["chartType"];
//    alert(window["actionCount_" + chartId])
    var actionDrillMap={};
    var actionCount=[];
    
    for(var i=1;i <= window["actionCount_" + chartId]; i++){
        var drillValueMap={};
        var viewbyArr=[];
        var measureArr=[];
        var chartTypeArr=[];
        for(var j=0; j < viewBys.length; j++){
            if(document.getElementById("viewby_"+i+"_"+chartId+"_"+j).checked) {
               viewbyArr.push($("#viewby_"+i+"_"+chartId+"_"+j).val());
            } 
        }
        for(var j=0; j < measName.length; j++){
            if(document.getElementById("measure_"+i+"_"+chartId+"_"+j).checked) {
               measureArr.push($("#measure_"+i+"_"+chartId+"_"+j).val());
            } 
        }
             chartTypeArr.push($("#chartType_"+i+"_"+chartId).val());
         actionCount[0]=window["actionCount_" + chartId];
         drillValueMap["viewBys"]=viewbyArr;
         drillValueMap["measures"]=measureArr;
         drillValueMap["chartType"]=chartTypeArr;
         drillValueMap["actionCount"]=actionCount;
         actionDrillMap["drill"+i]=drillValueMap;
      
    }
    chartData[chartId]["actionDrillMap"]=actionDrillMap;  
    parent.$("#addActionChartDiv").dialog().dialog('close');
    $("#driver").val("");
    $("#chartData").val(JSON.stringify(chartData));
//    alert("2"+JSON.stringify(chartData[chartId]["actionDrillMap"]));
}
