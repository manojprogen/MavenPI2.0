var mapDrill = false;
var mapId;
var mapData;
var drillDivId=["topDiv0"];
//Added by shobhit for multi dashboard on 22/09/15 
//Start
function buildMultiDBLayout1(div,data,columns,measureArray){
    $("#viewMeasureBlock").css("display", "none");
     $("#"+div).append(createMultiDBLayout1(div,measureArray,columns, data["chart1"]));
     var chartData=JSON.parse($("#chartData").val());
        var chWidth = 200;
        var chHeight = 150;
    var barData = data["chart1"];
//    alert(JSON.stringify(data));
    var wid=$("#topDiv0").width();
    var hgt=$("#topDiv0").height();
    var parentColumns=[];
    var allViewBys=JSON.parse($("#viewby").val());
     var allViewByIds=JSON.parse($("#viewbyIds").val());
    if(typeof chartData["chart1"]["isDashboardDefined"]!='undefined' && chartData["chart1"]["isDashboardDefined"]==='Y')
        {
            parentColumns.push(allViewBys[allViewByIds.indexOf($("#parentViewBy").val())]);
        }
        else{
            parentColumns.push(columns[0]);
        }
//        alert("Data : "+JSON.stringify(barData));
    var allData=[];
    var allDataMap={};
    allDataMap[parentColumns[0]]="All";
    allDataMap[measureArray[0]]=chartData["chart1"]["GTValueList"][0];
    allData.push(allDataMap);
    buildHalfPieDB("chart1","topDiv0", allData, parentColumns, measureArray, wid, hgt,hgt*0.6,0);
    var pieRadius=0;
    var maxValue=0;
    var maxHgt=0;
    for(var i=0; i< data["chart1"].length;i++){
        var measure =[];
        var barDataSlice=[];
        barDataSlice.push(barData[i]);
        measure.push(measureArray[0]);
    var index=0;
    index=parseInt(i)+1;
//    var gtValuesList=chartData["chart1"]["GTValueList"][i];
    if(i==0){
        maxValue=barDataSlice[0][measure[0]];
        maxHgt=$("#topDiv1").height();
    }
    var currValue=barDataSlice[0][measure[0]];
    var hgt1=currValue/(maxValue/maxHgt);
//    alert(JSON.stringify(barDataSlice))
//           if(i==0){
//               buildHalfPieDB("chart1","topDiv"+index, barDataSlice, columns, measure, wid, hgt,hgt*0.75); 
//           }
//           else{
            if(barData.length<=3){
               buildHalfPieDB("chart1","topDiv"+index, barDataSlice, parentColumns, measure, wid, hgt, hgt1*0.75, i); 
            }
            else{
               buildHalfPieDB("chart1","topDiv"+index, barDataSlice, parentColumns, measure, wid, hgt/2, hgt1*0.9, i); 
            }
//           }
//        LiquidFilledGaugeDash("chart1", data, columns, measureArray[i],chWidth,chHeight,gtValuesList,"topDiv"+i);
    
    }
    var divId = [];
    for(var k in columns){
        divId.push("mainDiv"+(parseInt(k)+1));
    }
//    added by shivam
    if (screen.width<1000){
      chWidth = 300;
      chHeight = 190;
    }else{
    chWidth = 500;
    chHeight = 230;
}
    for(var k in columns){
       var index=0;
       index=parseInt(k)+2;
       var barData = data["chart"+index];
       var measure =[];
       measure.push(measureArray[0]);
     if(typeof chartData["chart1"]["isDashboardDefined"]!=='undefined' && chartData["chart1"]["isDashboardDefined"]==='Y'){
        var currViewBy=[];
        currViewBy.push(columns[k]);
        var chartId="chart"+(parseInt(k)+2);
        barData=data[chartId.toString().trim()];
//        alert("Data--"+JSON.stringify(data));
//        alert("Bar Data--"+JSON.stringify(barData));
        buildBarAdvance("chart"+index,divId[k], barData, currViewBy, measure, chWidth, chHeight);
        buildBarAdvance(chartId,divId[k], barData, currViewBy, measure, chWidth, chHeight);
     }
     else{
         var currViewBy=[];
        currViewBy.push(columns[k]);
         var chartId="chart"+(parseInt(k)+1);
         var barData = data[chartId.toString().trim()];
         var index=0;
       index=parseInt(k)+1;
//     buildBarAdvance("chart"+index,divId[k], barData, currViewBy, measure, chWidth, chHeight);
     buildBarAdvance(chartId,divId[k], barData, currViewBy, measure, chWidth, chHeight);
    }
    }
    
}
function createMultiDBLayout1(div,measure,columns, data) {
    var layoutHtml="";
    layoutHtml += "<div id='mainLayout' style='width=100%;'>";
    layoutHtml += "<div id='selectMeasureDiv' style='height:30px;width:100%;'>";
    layoutHtml += "<select id='selectMeasure' style='float:right;font-size:9pt' onchange='changeMeasureArray(this.value)' name='Dashname'>";
    var chartData=JSON.parse(parent.$("#chartData").val());
    var measures=chartData["chart1"]["meassures"];
    for(var i in measures){
        if(i==0){
            layoutHtml += "<option value='"+i+"' selected>"+measures[i]+"</option>";
        }
        else{
            layoutHtml += "<option value='"+i+"'>"+measures[i]+"</option>";
        }
    }
    layoutHtml += "</select>";
    layoutHtml += "</div>";
    layoutHtml += "<div id='topDiv' style='width:100%;'>";
    if(drillDivId.indexOf("topDiv0")!=-1){
        layoutHtml += "<div id='topDiv0' style='background-color:#cccccc;height:167px;width:20%;float:left;border-bottom: dotted 1px #808080;'>";
    }
    else{
    layoutHtml += "<div id='topDiv0' style='height:167px;width:20%;float:left;border-bottom: dotted 1px #808080;'>";
    }
        layoutHtml += "</div>";
    layoutHtml += "<div id='topInnerDiv' style='width:80%;float:right;'>";
    var length=data.length;
//    alert(length);
//    alert(JSON.stringify(data));
    for(var i=0;i<(length>6?6:length);i++){
        var index=parseInt(i)+1;
        var len=Object.keys(data[0]).length;
        var hgt=0;
        var wid=0;
        if(length<=3){
            wid=100/(len+0.05);
            hgt=165;
    }
        else{
            wid=100/3.05;
            hgt=165/2;
        }
        if(drillDivId.indexOf("topDiv"+index)!=-1){
            layoutHtml += "<div id='topDiv"+index+"' style='background-color:#cccccc;width:"+(wid)+"%;border: dotted 1px #808080;float:left;height:"+hgt+"px'>";
        }
        else{
        layoutHtml += "<div id='topDiv"+index+"' style='width:"+(wid)+"%;border: dotted 1px #808080;float:left;height:"+hgt+"px'>";
        }
            
    layoutHtml += "</div>";
    }
    layoutHtml += "</div>";
    layoutHtml += "</div>";
    layoutHtml += "<div id='bottomDiv' style='width:100%;'>";
    
    layoutHtml += "<div style='width=100%;'>";
      for(var i in columns){
          
      if(i%2 ==0){
//          added by shivam
          if(window.width<1000){
            layoutHtml += "<div id='mainDiv"+(parseInt(i)+1)+"' style='width:50%;border-bottom: dotted 1px #808080;'>";
    layoutHtml += "</div>";}
else{
            layoutHtml += "<div id='mainDiv"+(parseInt(i)+1)+"' style='width:50%;float:left;border-bottom: dotted 1px #808080;'>";
    layoutHtml += "</div>";
}
      }else{
          
    layoutHtml += "<div id='mainDiv"+(parseInt(i)+1)+"' style='width:50%;float:right;border-bottom: dotted 1px #808080;'>";
    layoutHtml += "</div>";
      }
      }
//    layoutHtml += "<div id='mainDiv2' style='width:49.5%;border: dotted 1px #808080;float:right'>";
//    layoutHtml += "</div>";
//    
//    layoutHtml += "</div>";
//    
//    layoutHtml += "<div style='width=100%;border: dotted 1px #808080;'>";
//    
//    layoutHtml += "<div id='mainDiv3' style='width:49.5%;border: dotted 1px #808080;float:left'>";
//    layoutHtml += "</div>";
//    
//    layoutHtml += "<div id='mainDiv4' style='width:49.5%;border: dotted 1px #808080;float:right'>";
//    layoutHtml += "</div>";
//    
    layoutHtml += "</div>";
    
    layoutHtml += "</div>";
    
    layoutHtml += "</div>";
    return layoutHtml;
   
}

//End by shobhit
function createMultiDBLayout2(div,dataMap,columns,measures1,dataId,colIds,measureIds) {
    var layoutHtml="";
    layoutHtml += "<div id='mainLayout' style='width=100%;border: dotted 1px #808080;'>";
    layoutHtml += "<div id='topDiv' style='width:100%;border: dotted 1px #808080;'>";
    for(var i in measures1){
        layoutHtml += "<div id='topDiv"+i+"' style='width:"+(100/measures1.length)+"%;border: dotted 1px #808080;'>";
        layoutHtml += "</div>";
    }
    layoutHtml += "</div>";
    layoutHtml += "<div id='bottomDiv' style='width:100%;border: dotted 1px #808080;'>";
    
    layoutHtml += "<div id='mainDiv' style='width=75%;border: dotted 1px #808080;'>";
    layoutHtml += "</div>";
    
    layoutHtml += "<div style='width=25%;border: dotted 1px #808080;'>";
    
    layoutHtml += "<div id='sideDiv1' style='width=100%;border: dotted 1px #808080;'>";
    layoutHtml += "</div>";
    
    layoutHtml += "<div id='sideDiv2' style='width=100%;border: dotted 1px #808080;'>";
    layoutHtml += "</div>";
    
    layoutHtml += "<div id='sideDiv3' style='width=100%;border: dotted 1px #808080;'>";
    layoutHtml += "</div>";
    
    layoutHtml += "</div>";
    
    layoutHtml += "</div>";
    
    layoutHtml += "</div>";
    
    return layoutHtml;
}

function LiquidFilledGaugeDash(div, data, columns, measureArray, divWidth, divHeight,KPIResult,newDiv){
  var chartData = JSON.parse(parent.$("#chartData").val());
  var specialCharacter = "%";
  var showData = 0;
  var KPIResult1 = 0;
  var perFlag = "";
  var DataSum = 0;
    var colorPicker = "";
    var nameArrCurrent = "";
    var nameArrPrior = "";
    var textData = 0;
 // data retrieveing
for(var i in data){
 DataSum+= parseFloat(data[i][measureArray[0]]);
}
var dashletid=div;
var fromoneview=parent.$("#fromoneview").val();
var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
         div=div1;
     }
var tableData = {};
tableData[div] = data;

showData = KPIResult;
KPIResult1 = KPIResult;
 //data retrieving end
 //colorpicker
   
 if(typeof chartData[div]["colorPicker"]!="undefined" && chartData[div]["colorPicker"]!=""){
  colorPicker = chartData[div]["colorPicker"];
   if(showData>0){
        textData = showData;

}else{
        textData = showData*(-1);
     } 
   if(showData>0 && colorPicker=="#084c61"){
        colorPicker = "#38d976";

    }else if(showData<=0 && colorPicker=="#084c61"){
        colorPicker = "#eb1337";
     }else{
      colorPicker = chartData[div]["colorPicker"];
     }
}else{
    if(showData>0){
        textData = showData;
         colorPicker = "#38d976";
    }else{
        textData = showData*(-1);
        colorPicker = "#eb1337";
}

}
 if(typeof chartData[div]["MaxRange"]!=="undefined" && chartData[div]["MaxRange"] !=""){
    textData = (textData/chartData[div]["MaxRange"])*100;

  }
//colorpicker end
//measure or kpi name
if(typeof chartData[div]["KPIName"]!="undefined"  && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
for(var i in measureArray){
    if(chartData[div]["KPIName"]!=measureArray[i]){
}else{
     chartData[div]["KPIName"] = measureArray[0];
}
}}else{

  chartData[div]["KPIName"] = measureArray[0];
}

 parent.$("#chartData").val(JSON.stringify(chartData));
//measure or kpi name end
var margin = {
        top: 10,
        right: 0,
        bottom: 70,
        left: -7
    }
     if(fromoneview!='null'&& fromoneview=='true'){
         div=dashletid;
     }
     var divId = newDiv;
 svg = d3.select("#" + divId).append("svg")
            .attr("width", divWidth )
            .attr("height", divHeight+12)
//              .attr("id", "svg_" + div)
//             .attr("viewBox", "0 0 "+(divWidth*1.3)+" "+(divHeight*1.3 )+" ")
            .append("g")
            .attr("transform", "translate("+margin.left+","+margin.top+")");
    var   config = liquidFillGaugeDefaultSettings();
     config.circleColor = colorPicker;
     config.waveColor = colorPicker;
    config.waveAnimateTime = 3000;
    config.waveHeight = 0.1;
    config.waveCount = 1;
      svg.append("g")
      .attr("width", divWidth)
       .attr("height", divHeight)
       .attr("id","liquidChart"+divId);
       var fontsize = "30";
       if(typeof chartData[div]["kpiGTFont"]!=="undefined" && chartData[div]["kpiGTFont"] !== '' && chartData[div]["kpiGTFont"] !=="4"){
           fontsize = chartData[div]["kpiGTFont"];
        }else {
           fontsize = "10";
       }
      chartData[div]["kpiGTFont"]=fontsize;
       $("#chartData").val(JSON.stringify(chartData));
      
        if(fromoneview!='null'&& fromoneview=='true'){
         div=div1;
     }
  openTableDiv = function(){
        $("#openTableTile").dialog({
         autoOpen: false,
         height: 440,
         width: 620,
         position: 'justify',
         modal: true,
         resizable:true,
        title:'Data Table'
    });
   $("#openTableTile").html("");
   if(fromoneview!='null'&& fromoneview=='true'){
        buildTable(dashletid+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
   }else{
    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
   }
    $("#openTableTile").dialog('open');
}
      
      
          loadLiquidFillGauge("liquidChart"+divId,textData,KPIResult1,config,divWidth,divHeight,fontsize,measureArray,div)



}

function buildDialDash(div,data, columns, measureArray,wid,hgt,KPIResult,divId){
//    initialize();
var specialCharacter = "%";
var htmlDiv ="";
//    var min1 = minimumValue(data, measureArray[0]);
//    var max1 = maximumValue(data, measureArray[0]);
    //    var min1 = minimumValue(data, measureArray[0]);
   
var min = -50;
var max = 25;
var chartData = JSON.parse(parent.$("#chartData").val());
  var dialTargetValue = 0;

//if(typeof chartData[div]["dialValues"] !="undefined" && chartData[div]["dialValues"] !=""){
//if(typeof chartData[div]["dialValues"]["dialGuageTarget"] !="undefined" && chartData[div]["dialValues"]["dialGuageTarget"] !=""){
//
//  dialTargetValue = chartData[div]["dialValues"]["dialGuageTarget"]
//}}
dialTargetValue=KPIResult;

if(typeof chartData[div]["dialValues"] !="undefined" && chartData[div]["dialValues"] !=""){
if(typeof chartData[div]["dialValues"]["minTarget"] !="undefined" && chartData[div]["dialValues"]["minTarget"] !=""){
    min = chartData[div]["dialValues"]["minTarget"];
}else {
    min = -50;
}

if(typeof chartData[div]["dialValues"]["maxTarget"] !="undefined" && chartData[div]["dialValues"]["maxTarget"] !=""){
    max = chartData[div]["dialValues"]["maxTarget"];
}else {
    max = 25;
}

}

var sum1=0;
var sum2=0;
//        $.each(data[div], function (d) {
//            sum1 += parseInt(data[div][d][measureArray[0]]);
//            sum2 += parseInt(data[div][d][measureArray[0]]);
//        });

        if (measureArray[0].indexOf(specialCharacter)!=-1 || measureArray[0].indexOf("Percent")!=-1 ||measureArray[0].indexOf("percent")!=-1 ||measureArray[0].indexOf("PERCENT")!=-1 ||chartData[div]["aggregation"][0]=="AVG" || chartData[div]["aggregation"][0]=="avg") {
                sum1 =   parseFloat(KPIResult)
                sum2 =   parseFloat(KPIResult)
                 } else{
                  sum1 = 0;
                   sum2 =   parseFloat(KPIResult)
                 }
        sum1 = sum1.toFixed(2);
        sum2 = sum2.toFixed(2);
//        sum1.replace(".00", "");
//dialTargetValue = parseInt(sum1);
//var fontsize=wid*.03;
//if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== ''){
//    fontsize=chartData[div]["kpiGTFont"];
//}else {
//    fontsize = wid *.03;
//}
htmlDiv += "<div id='dialDiv"+divId+"' style='width:"+wid+"px;height:"+hgt*.75+"px'></div>";
//  if (measureArray[0].indexOf(specialCharacter)!=-1 || measureArray[0].indexOf("Percent")!=-1 ||measureArray[0].indexOf("percent")!=-1 ||measureArray[0].indexOf("PERCENT")!=-1 ||chartData[div]["aggregation"][0]=="AVG" || chartData[div]["aggregation"][0]=="avg") {
//sum2 += "%";
//  }
if(typeof chartData[div]["dialMeasureSuffix"]!="undefined" && chartData[div]["dialMeasureSuffix"]!=""){
sum2=(sum2+" "+chartData[div]["dialMeasureSuffix"]);
}else{
sum2=(sum2);
  }

     $("#"+divId).append(htmlDiv);
//    var name = "Pi";
    var name = "dialDiv"+divId;
    if(min!="undefined"){

    }else{
       min =-50;
    }
    if(max!="undefined"){

    }else{
       max =25;
    }
    var divSize = 0;
    hgt = hgt*1.2;
if(wid>hgt){
  divSize = parseInt(hgt)
}else{
   divSize =  parseInt(wid)
}
    var config = {
		  size: divSize,
		  label: measureArray[0],
		  min: parseInt(min),
		  max: parseInt(max),
		  minorTicks: 5
		}
                 var range = parseInt(config.max - config.min);
    var confingMinRed =  parseInt(config.min) ;
    var confingMaxRed =  parseInt(config.min+ range*0.4) ;
    var confingMinOrange =  parseInt(config.min + range*0.4);
    var confingMaxOrange =  parseInt(config.min + range*0.85) ;
    var confingMinGreen =  parseInt(config.min + range*0.85);
    var confingMaxGreen =  parseInt(config.max) ;

 if(typeof chartData[div]["dialValues"] !="undefined" && chartData[div]["dialValues"] !=""){
if(typeof chartData[div]["dialValues"]["minGreeen"] !="undefined" && chartData[div]["dialValues"]["minGreeen"] !=""){
    confingMinRed = chartData[div]["dialValues"]["minGreeen"];
}else{
    confingMinRed =  config.min;
}
if(typeof chartData[div]["dialValues"]["maxGreeen"] !="undefined" && chartData[div]["dialValues"]["maxGreeen"] !=""){
confingMaxRed = chartData[div]["dialValues"]["maxGreeen"];
}else{

    confingMaxRed =   parseInt(config.min+ range*0.4) ;
}
if(typeof chartData[div]["dialValues"]["minOrange"] !="undefined" && chartData[div]["dialValues"]["minOrange"] !=""){
confingMinOrange = chartData[div]["dialValues"]["minOrange"];
}else{
confingMinOrange =  parseInt(config.min + range*0.4) ;
}
if(typeof chartData[div]["dialValues"]["maxOrange"] !="undefined" && chartData[div]["dialValues"]["maxOrange"] !=""){
confingMaxOrange = chartData[div]["dialValues"]["maxOrange"];
}else{
confingMaxOrange =  parseInt(config.min + range*0.85) ;
}
if(typeof chartData[div]["dialValues"]["minRed"] !="undefined" && chartData[div]["dialValues"]["minRed"] !=""){
confingMinGreen = chartData[div]["dialValues"]["minRed"];
}else{
confingMinGreen =  parseInt(config.min + range*0.85) ;
}
if(typeof chartData[div]["dialValues"]["maxRed"] !="undefined" && chartData[div]["dialValues"]["maxGreeen"] !=""){
confingMaxGreen = chartData[div]["dialValues"]["maxRed"];
}else{
confingMaxGreen =  config.max ;
}}
var gauges=[];
if(typeof chartData[div]["dialType"]==='undefined' || chartData[div]["dialType"]==='std'){
                                config.redZones = [{ from: confingMinRed, to: confingMaxRed }];
				config.yellowZones = [{ from: confingMinOrange, to: confingMaxOrange }];
                                config.greenZones = [{ from: confingMinGreen, to: confingMaxGreen }];
}
else{
                                config.greenZones = [{ from: confingMinRed, to: confingMaxRed }];
				config.yellowZones = [{ from: confingMinOrange, to: confingMaxOrange }];
                                config.redZones = [{ from: confingMinGreen, to: confingMaxGreen }];
}

//				gauges[name] = new Gauge(name + "GaugeContainer", config);
confingMinRed=parseInt(confingMinRed);
confingMaxGreen=parseInt(confingMaxGreen);
dialTargetValue=dialTargetValue<confingMinRed?confingMinRed:dialTargetValue;
dialTargetValue=dialTargetValue>confingMaxGreen?confingMaxGreen:dialTargetValue;
		gauges[name] = new Gauge(name , config ,dialTargetValue,"dialDiv" ,sum2,divId);
				gauges[name].render(4000);
                                
      openTableDiv = function(){
        $("#openTableTile").dialog({
         autoOpen: false,
         height: 440,
         width: 620,
         position: 'justify',
         modal: true,
         resizable:true,
        title:'Graph Properties'
    });
   $("#openTableTile").html("");
   if(fromoneview!='null'&& fromoneview=='true'){
        buildTable(dashledid+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
   }else{
    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
}
    $("#openTableTile").dialog('open');
}

}

// added by mayank on 17 Sep
function buildIndiaMapDashboard(div,data,columns,measureArray,divWidth,divHeight){
     $("#viewMeasureBlock").css("display", "none");
       $("#"+div).html("");  
      $("#"+div).append(Layout1());
    
      var chartData = JSON.parse($("#chartData").val());
      var key = Object.keys(chartData);

    for(var k=0;k<key.length;k++){
        
   
    if(k==0){
    var div = "part1"
    $("#"+div).html('');
     buildIndiaDashboard(div,data,columns,measureArray,divWidth,divHeight);
    }
if(k==1){    
    var divId = "section1";
    div = "chart1";
    var column = chartData["chart2"]["viewBys"];
    var measureArray = chartData["chart2"]["meassures"];
    var currData = data["chart2"]
    var height =300;
        if(mapDrill==false){
            
        buildBarAdvance(div, divId, currData, column, measureArray, divWidth, height)
        }else{
             showStateMap(mapId, mapData,data["chart1"],columns,data["chart2"]);
            
        }
    }
    if(k==2){
        divWidth = 600
        divHeight= 190;
    buildGroupedLine("chart3",data["chart3"], chartData["chart3"]["viewBys"], chartData["chart3"]["meassures"],divWidth,divHeight);
    }
    if(k==2){
       buildTable("chart3",data, data["chart3"], chartData["chart3"]["viewBys"], chartData["chart3"]["meassures"],divWidth,divHeight); 
    }
     }
    
}
function buildIndiaMapWithTrend(div,data,columns,measureArray,divWidth,divHeight){
    
     $("#viewMeasureBlock").css("display", "none");
       $("#"+div).html("");  
      $("#"+div).append(Layout1());
    
      var chartData = JSON.parse($("#chartData").val());
      var key = Object.keys(chartData)

    for(var k=0;k<key.length;k++){
        
   
    if(k==0){
    var div = "part1"
    $("#"+div).html('');
     buildIndiaDashboard(div,data,columns,measureArray,divWidth,divHeight);
    }
if(k==1){    
    var divId = "section1";
    div = "chart1";
    var column = chartData["chart2"]["viewBys"];
    var measureArray = chartData["chart2"]["meassures"];
    var currData = data["chart2"]
    var height =300;
        if(mapDrill==false){
            
        buildBarAdvance(div, divId, currData, column, measureArray, divWidth, height)
        }else{
             showStateMap(mapId, mapData,data["chart1"],columns,data["chart2"]);
            
        }
    }
    if(k==2){
        divWidth = 600
        divHeight= 190;
    buildMultiMeasureTrLineAdvance("chart3",data["chart3"], chartData["chart3"]["viewBys"], chartData["chart3"]["meassures"],divWidth,divHeight);
    }
    if(k==2){
       
       buildTable("chart3",data, data["chart3"], chartData["chart3"]["viewBys"], chartData["chart3"]["meassures"],divWidth,divHeight); 
    }
     }
    
}
// end by mayank
function Layout1() {   
        var innerHtml="";
    innerHtml+="<div id='mainDiv' style='width:100%'> ";
    innerHtml+="<div id='part1' style='width:40%;float:left;border: dotted 1px #808080;height:530px'></div>";
    innerHtml+="<div id='part2' style='width:59.6%;border: dotted 1px #808080;float:right;height:550px'>";
    innerHtml+="<div id='section1' style='width:100%;border: dotted 1px #808080;height:300px'></div>";
    innerHtml+="<div id='section2' style='width:100%;border: dotted 1px #808080;height:228px'></div>";
    innerHtml+="</div>";
    innerHtml+="<div id='part3' style='width:100%;border: dotted 1px #808080;float:left;height:220px'></div>";
    innerHtml+="</div>";
    return innerHtml;
}
// added by mayank
function buildIndiaDashboard(div,data,columns,measureArray,divWid,divHgt){
     var color = d3.scale.category12();
    
var fun = "drillWithinchart(this.id,\"chart1\")";
     var districtmapchartData = data;
var chart12 = data["chart1"];
var districtData = data["chart2"];
var chartMapData = data["chart1"];
var chartData = JSON.parse(parent.$("#chartData").val());


columns = chartData["chart1"]["viewBys"];
mapColumns = chartData["chart1"]["viewBys"];
measureArray = chartData["chart1"]["meassures"];
mapColumnsDistrict = chartData["chart2"]["viewBys"];
measureArrayDistrict = chartData["chart2"]["meassures"];
var colIds = chartData["chart1"]["viewIds"];
var w = 580;
var h = 450;
var proj = d3.geo.mercator();
var path = d3.geo.path().projection(proj);
var t = proj.translate(); // the projection's default translation
var s = proj.scale() // the projection's default scale
var colorFlag = true;
 var color="#188adb";
        var measure= measureArray[0];
        var isShadedColor=true;
        //    $("#gradShade").val(color);
        //    shadingType="gradient";
        //    $("#shadeType").val(shadingType);
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
      
        var colorMap={};
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));
var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};
//            var isShadedColor = false;
            var conditionalShading = false;
             if (parent.$("#isShaded").val() == "true" || colorFlag) {
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalShading = true;
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        conditionalMeasure = conditionalMap["measure"];
                        conditionalMeasure = $("#conditionalMeasure").val();
                    } else if (parent.$("#shadeType").val() == "standard") {

                    } else {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];
                       
                        isShadedColor = true;
                        var measureData = [];
                        for (var i = 0; i < chartMapData.length; i++) {
                            measureData.push(chartMapData[i][shadingMeasure]);
                        }
                        color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range(["rgb(240,240,240)", map[map["measure"]]]);
                    }
                } else {
                    if (parent.isCustomColors) {
                        color = d3.scale.ordinal().range(parent.customColorList);
                    }
                }


//color

var html1 = "<div id = 'mapDiv"+div+"' style='height:auto;float:left;width:100%;'></div>";
$("#"+div).append(html1);
var map = d3.select("#mapDiv"+div).append("svg:svg")
.attr("width", w)
.attr("height", h+50)
.style("margin-top","5px")
// .call(d3.behavior.zoom().on("zoom", redraw))
.call(initialize);
var india = map.append("svg:g")
.attr("id", "india");
d3.json("JS/states-tel.json", function (json) {
//d3.json("JS/states-tel.json", function (json) {
india.selectAll("path")

.data(json.features)
.enter().append("path")
.attr("d", path)
.attr("fill", function(d,i){
var currName = d.id;
var colorShad;
var drilledvalue;

                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
}

 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                    colorShad = drillShade;
                } else {
for (var l = 0; l < chartMapData.length; l++) {
 if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && chartMapData[l][mapColumns[0]].toLowerCase() == "chhattisgarh")) {

                                    var strVal = chartMapData[l][measureArray[0]];
                                  
                                    if (isShadedColor) {
                                         var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                        strVal = chartMapData[l][shadingMeasure];
                                        colorShad = color(strVal);
                                    } else if (conditionalShading) {
                                        return getConditionalColorIndia(color(l), chartMapData[l][conditionalMeasure]);
                                    } else {
                                        colorShad = color(l);
                                    }

                                    return colorShad;
                                }
}
  return "#fff";
   }


return colorShad;
})
.on("mouseover", function(d, i) {
                                  d3.select(this).attr("stroke", "grey");
                var content;
                var title;
                title = columns;

                var currName = (d.id).toUpperCase();
                for (var i = 0; i < mapColumns.length; i++) {
                    content = "<span class=\"name\">" + mapColumns[i] + ":</span><span class=\"value\"> " + currName + "</span><br/>";
                    break;
                }

                for (var i = 0; i < measureArray.length; i++) {
                    var msrData;
                    for (var l = 0; l < chartMapData.length; l++) {
                        if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && chartMapData[l][mapColumns[0]].toLowerCase() == "chhattisgarh")) {
                            for (t = 0; t < measureArray.length; t++) {
                                if (parent.isFormatedMeasure) {
                                    msrData = numberFormat(chartMapData[l][measureArray[t]], round, precition)
                                }
                                else {
                                    msrData = addCurrencyType(div, getMeasureId(measureArray[t])) + addCommas(chartMapData[l][measureArray[t]]);
                                }
                                content += "<span class=\"name\">" + measureArray[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                            }
                            return tooltip.showTooltip(content, d3.event);
                        }
                        else
                        {
                            msrData = "";
                        }


                    }
                    //        content += "<span class=\"name\">"+measureArray[i]+":</span><span class=\"value\"> " + msrData + "</span><br/>";

                }
})
                            .on("mouseout", function(d, i) {
                                hide_details(d, i, this);
                            })
.attr("id",function(d,i){
        var currName = (d.id).toUpperCase();
for (var l = 0; l < chartMapData.length; l++) {
if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase()) {
    return chartMapData[l][mapColumns[0]]+":"+chartMapData[l][mapColumns[0]];
}
}
return d.id;
})
//.attr("onclick", fun)
.on("dblclick", function(d, i) {
    
  mapDrill=true;
  mapData = d;
  mapId = this.id;
   drillWithinchart(this.id,"chart1");
 $("#section1").html('');
   showState(this.id, d);
    });
    
    var html = "<div id='legendsScale' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-41%;margin-left:60%;'></div>";
                    $('#'+div).append(html);
                    var svg1 = d3.select("#legendsScale").append("svg")
                            .attr("width", "100%")
                            .attr("height", "100%");
                            
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
                        var selectedMeasure = $("#conditionalMeasure").val();
                        var keys = Object.keys(conditionalMap);
                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(selectedMeasure);
                        for (var no = 0; no < (keys.length ); no++) {
                            var legend = svg1.append("g")
                                    .attr("transform", function() {
                                        return "translate(10," + ((no * 20) + 20) + ")";
                                    });
                            legend.append("rect")
                                    .attr("width", 24)
                                    .attr("height", 14)
                                    .attr("cy", 9)
                                    .attr("stroke", "black")
                                    .attr("stroke-width", ".5")
                                    .style("fill", conditionalMap[no]["color"]);
//                                        .attr("r", 5);
                            legend.append("text")
                                    .attr("x", 28)
                                    .attr("y", 9)
                                    .attr("dy", ".35em")
                                    .text(function() {
                                        if (conditionalMap[no]["operator"] === "<>") {
                                            return conditionalMap[no]["limit1"] + conditionalMap[no]["operator"] + conditionalMap[no]["limit2"];
                                        } else {
                                            return conditionalMap[no]["operator"] + conditionalMap[no]["limit1"];
                                        }
                                    });
                        }
                        $("#legendsScale").css("width", 500 * .15);
                    } else  {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legendsScale").height() - 10;
                        var shadingMeasure = colorMap["measure"];
                     
                        var max = maximumValue(chart12, shadingMeasure);
                        var min = minimumValue(chart12, shadingMeasure);
                       
                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend1")
                                .attr("x1", "0%")
                                .attr("y1", "30%")
                                .attr("x2", "50%")
                                .attr("y2", "30%")
                                .attr("spreadMethod", "pad")
                                .attr("gradientTransform", "rotate(90)");

                        gradient.append("svg:stop")
                                .attr("offset", "0%")
                                .attr("stop-color", colorMap[shadingMeasure])
                                .attr("stop-opacity", 1);
                        gradient.append("svg:stop")
                                .attr("offset", "100%")
                                .attr("stop-color", "rgb(230,230,230)")
                                .attr("stop-opacity", 1);

                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(shadingMeasure);
                        svg1.append("g").append("rect")
                                .attr("width", 10)
                                .attr("height", "90%")
                                .attr("fill", "url(#gradientWrdMapLegend1)")
                                .attr("x", 0)
                                .attr("y", 15);
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", height)
                                .attr("dy", ".35em").text(addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(max));


                        $("#legendsScale").css("width", "50%");
                    }
    
});


function initialize() {
proj.scale(5030);
proj.translate([-920, 580]);
}




var chart123=[];
for(i=0;i<10;i++){
   chart123.push(chart12[i]);
}
 var centered;

    function showState(state12, d) {

     var state1 = state12.split(":")
     var state = state1[0];
    var x, y, k;
                if (d && centered !== d) {
                    var centroid = path.centroid(d);
                    x = centroid[0];
                    y = centroid[1];
//                    var x1 = centroid[0];
//                    var y1 = centroid[1];
                    k = 2.3;
                    y = y + parseInt(y * (40 / 100)) + 150;

                    centered = d;
                } else {
                    x = w / 2;
                    y = h / 2;
                    k = 1;
                    centered = null;
                }
                tooltip.hideTooltip();
//            $("#mapDiv" + div).html("");

              //  $("#chart1").show();
             //   parent.$("#mapView").val(state);
             //   $('#mapDiv'+div).empty();
                var html1 = "<img width='35' align='left' style='cursor: pointer;' height='35' onclick='getAdvanceVisuals(\""+parent.$("#graphsId").val()+"\")' class='ui-icon ui-icon-circle-arrow-w' id='mapIcon' alt='dgt' title='Back To Chart'/>\n\
                                <span id='stName'>"+state+"</span>"
               $("#mapDiv" + div).append(html1);
                var html = "";
                var svgcanvas;
//                    d3.json(parent.$("#tarPath").val() + "/" + columns[1] + ".json", function(data) {

                var data = chart12[0][columns];
//                if (parent.$("#isShaded").val() == "true") {
////                    map = d3.select('#maptDiv'+div).append("svg:svg")
//                    map = d3.select('#section1').append("svg:svg")
//                            .attr("width", w * 1.7)
//                            .attr("height", h);
//                    svgcanvas = map.append("g")
//                            .attr("width", w * 1.7)
//                            .attr("height", h);
//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:35%;margin-left:-55%;'></div>";
//                    $('#section1').append(html);
//                    $("#legends").show();
//                    var svg1 = d3.select("#legends").append("svg")
//                            .attr("width", "100%")
//                            .attr("height", "100%");
//                    if (parent.$("#shadeType").val() == "conditional") {
//                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        var selectedMeasure = conditionalMap["measure"];
//                        var keys = Object.keys(conditionalMap);
//                        svg1.append("g").append("text").attr("x", 20)
//                                .attr("y", 9)
//                                .attr("dy", ".35em").text(selectedMeasure);
//                        for (var no = 0; no < (keys.length - 1); no++) {
//                            var legend = svg1.append("g")
//                                    .attr("transform", function() {
//                                        return "translate(10," + ((no * 20) + 20) + ")";
//                                    });
//                            legend.append("rect")
//                                    .attr("width", 24)
//                                    .attr("height", 14)
//                                    .attr("cy", 9)
//                                    .attr("stroke", "black")
//                                    .attr("stroke-width", ".5")
//                                    .style("fill", conditionalMap[no]["color"]);
////                                        .attr("r", 5);
//                            legend.append("text")
//                                    .attr("x", 28)
//                                    .attr("y", 9)
//                                    .attr("dy", ".35em")
//                                    .text(function() {
//                                        if (conditionalMap[no]["operator"] === "<>") {
//                                            return autoFormating(conditionalMap[no]["limit1"]) + conditionalMap[no]["operator"] + autoFormating(conditionalMap[no]["limit2"]);
//                                        } else {
//                                            return conditionalMap[no]["operator"] + autoFormating(conditionalMap[no]["limit1"]);
//                                        }
//                                    });
////                                        .attr("fill", conditionalMap[no]["color"]);
//                        }
//                        $("#legends").css("width", w * .15);
////                            $("#legends").css("margin", "3% 84% auto");
//                    } else if (parent.$("#shadeType").val() === "gradient") {
//                        var colorMap = JSON.parse(parent.$("#measureColor").val());
//                        var height = $("#legends").height() - 10;
//                        var shadingMeasure = colorMap["measure"];
//                        var max = maximumValue(data, shadingMeasure);
//                        var min = minimumValue(data, shadingMeasure);
//                        var gradient = svg1.append("svg:defs")
//                                .append("svg:linearGradient")
//                                .attr("id", "gradientWrdMapLegend")
//                                .attr("x1", "0%")
//                                .attr("y1", "30%")
//                                .attr("x2", "50%")
//                                .attr("y2", "30%")
//                                .attr("spreadMethod", "pad")
//                                .attr("gradientTransform", "rotate(90)");
//
//                        gradient.append("svg:stop")
//                                .attr("offset", "0%")
//                                .attr("stop-color", colorMap[shadingMeasure])
//                                .attr("stop-opacity", 1);
//                        gradient.append("svg:stop")
//                                .attr("offset", "100%")
//                                .attr("stop-color", "rgb(230,230,230)")
//                                .attr("stop-opacity", 1);
//
//                        svg1.append("g").append("text").attr("x", 0)
//                                .attr("y", 9)
//                                .attr("dy", ".35em").text(shadingMeasure);
//                        svg1.append("g").append("rect")
//                                .attr("width", 10)
//                                .attr("height", "90%")
//                                .attr("fill", "url(#gradientWrdMapLegend)")
//                                .attr("x", 0)
//                                .attr("y", 15);
//                        svg1.append("g").append("text").attr("x", 10)
//                                .attr("y", height)
//                                .attr("dy", ".35em").text(addCommas(min));
//                        svg1.append("g").append("text").attr("x", 10)
//                                .attr("y", 25)
//                                .attr("dy", ".35em").text(addCommas(max));
//
//
//                        $("#legends").css("width", "8%");
//                    }
//                } else {
//                    map = d3.select('#maptDiv'+div).append("svg:svg")
//                            .attr("width", w * 2)
//                            .attr("height", h);
//                    svgcanvas = map.append("g")
//                            .attr("width", w * 2)
//                            .attr("height", h);
//                }


//
//               var svgcanvas1 = svgcanvas.append("g")
//                        .attr("id", "places1");

              var  data1 = [];
                var cordMap = {};
                var count = -1;
                var currNameDistrict ;
                var htmlframe = "<div style='height:300px'><iframe id='maps' frameborder='0' width='500px' height='450px' style='margin-top:-30px;'></iframe></div>";
                $("#chart2").attr("style", "display:block");
                $("#section1").append(htmlframe);
                $("#maps").attr("src", "svg/" + state.toLowerCase() + ".svg").load(function() {
                    var $map = $(this).contents();
                    var titles = $map.find('path').map(function() {

                        var parenttitle = $(this).parent().attr('title') || '';
                        var stateMap = {};
                        stateMap["title"] = $(this).attr('title');
//                        stateMap["path"] = $(this).attr('d');
                        data1.push(stateMap);

                        return '0\t' + parenttitle + '\t' + $(this).attr('title');
                    }).get();
                    $(this).attr("stroke", "steelblue")
                                .attr("stroke-width", ".2")
//                        $map.append("text").attr("x",10).attr("y",10).text("SADSAD");
                    $map.find('g').each(function() {
                     var svgG=d3.select(this);
//                     var counter=0;
                     d3.select(this).selectAll("path").each(function(d, i){
                        var counter = i;
                                    var bbox = this.getBBox();
                        svgG.append("text").style("text-anchor","middle").attr("transform", "translate(" + (bbox.x + bbox.width / 2)  + "," + (bbox.y + bbox.height / 2) + ")").text(function(d,j){
                            return data1[counter]["title"];
                        }).style("font-size","7px");
                      var district = $(this).attr('title');
                        $(this).attr('fill', function(){
                          count++;

                    //    var currName = (d.id).toUpperCase();
                        var colorShad;
                        var drilledvalue;
//
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
//
                if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && district ==drilledvalue) {
                    colorShad = drillShade;
                } else {
                    if(districtData!="undefined" && districtData!=""){
                    for(var i in districtData){

                        if(district.toLowerCase()== districtData[i][mapColumnsDistrict[0]].toLowerCase()){


                       return color(i);
                        }else{
                          colorShad = "white";
                        }
                    }
                        }}return colorShad;

                       })

                     }); $(this).attr('onclick', function(){
                         var districtArray = [];
                       $map.find('g').each(function() {
                     $(this).find('path').each(function(d,i) {
                      var district = $(this).attr('title');
                      districtArray.push(district)
                       $(this).on("click", function(){

//                           showDistrictTable(district,districtData,"districtClick")
                       })

                      })
                 })
//                  showDistrictTable(districtArray,districtData,"stateClick")
 })

                    })


                    var abc = state.toLowerCase();
});
//function  showDistrictTable(districtArray,districtData,flag){
//
//
//     if(typeof districtmapchartData["chart3"]!= "undefined" && flag=="stateClick1"){
//    chart12 = districtmapchartData["chart3"];
//
//    columns = chartData["chart3"]["viewBys"];
//    measureArray = chartData["chart3"]["meassures"];
//}else{
//      var districtLevelData = [];
//      for(var i in districtData){
//          if(flag!="districtClick"){
//    for(var k in districtArray){
//
//   if(districtArray[k].toLowerCase()== districtData[i][chartData["chart2"]["viewBys"][0]].toLowerCase()){
//
//       districtLevelData.push(districtData[i])
//   }}}else{
//if(districtArray.toLowerCase()== districtData[i][chartData["chart2"]["viewBys"][0]].toLowerCase()){
//
//       districtLevelData.push(districtData[i])
//   }
//   }
//      }
//
//     chart12 = districtLevelData;
//
//    columns = chartData["chart2"]["viewBys"];
//    measureArray = chartData["chart2"]["meassures"];
//}
//
//
//$("#mapTable" + div).html("");
//var htmlstr = mapTableFunction(chart12,columns,measureArray);
//$("#mapTable" + div).append(htmlstr);
// }
//
                function show_detailsOfMap(d, columnVal, measureArray, con, i, data) {
                    var content;
                    content = "<span class=\"name\">" + columnVal + ":</span><span class=\"value\"> " + d + "</span><br/>";
                    var msrData;
                    var val;
                    for (var l = 0; l < data.length; l++) {
                        if (d.toLowerCase() == data[l][columnVal].toLowerCase() || (d.toLowerCase() == "chatisgarh" && data[l][columnVal].toLowerCase() == "chhattisgarh")) {
                            for (t = 0; t < measureArray.length; t++) {
                                if (parent.isFormatedMeasure) {
                                    msrData = numberFormat(data[l][measureArray[t]], round, precition)
                                }
                                else {
                                    msrData = addCurrencyType(div, getMeasureId(measureArray[t])) + addCommas(data[l][measureArray[t]]);
                                }
                                content += "<span class=\"name\">" + measureArray[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                            }
                            return tooltip.showTooltip(content, d3.event);
                        }
                        else
                        {
                            msrData = "";
                        }

                    }
                }

            }
            
}

function showStateMap(state12, d,data,columns,districtData) {
     $('#section1').append('');
    var centered;
    var chart12 = data;
//    var districtData = data["chart2"];
    var w=250;
    var h=150;
    var div = "part2";
    var proj = d3.geo.mercator();
var path = d3.geo.path().projection(proj);
     var state1 = state12.split(":")
     var state = state1[0];
    var x, y, k;
                if (d && centered !== d) {
                    var centroid = path.centroid(d);
                    x = centroid[0];
                    y = centroid[1];
//                    var x1 = centroid[0];
//                    var y1 = centroid[1];
                    k = 2.3;
                    y = y + parseInt(y * (40 / 100)) + 150;

                    centered = d;
                } else {
                    x = w / 2;
                    y = h / 2;
                    k = 1;
                    centered = null;
                }
                tooltip.hideTooltip();
//            $("#mapDiv" + div).html("");

              //  $("#chart1").show();
             //   parent.$("#mapView").val(state);
             //   $('#mapDiv'+div).empty();
//                var html1 = "<img width='35' align='left' style='cursor: pointer;' height='35' onclick='getAdvanceVisuals(\""+parent.$("#graphsId").val()+"\")' class='ui-icon ui-icon-circle-arrow-w' id='mapIcon' alt='dgt' title='Back To Chart'/>\n\
//                                <span id='stName'>//"+state+"</span>"
//               $("#mapDiv" + div).append(html1);
                var html = "";
                var svgcanvas;
//                    d3.json(parent.$("#tarPath").val() + "/" + columns[1] + ".json", function(data) {

                var data = chart12[0][columns];
//                if (parent.$("#isShaded").val() == "true") {
////                    map = d3.select('#maptDiv'+div).append("svg:svg")
//                    map = d3.select('#section1').append("svg:svg")
//                            .attr("width", w * 1.7)
//                            .attr("height", h);
//                    svgcanvas = map.append("g")
//                            .attr("width", w * 1.7)
//                            .attr("height", h);
//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:35%;margin-left:-55%;'></div>";
//                    $('#section1').append(html);
//                    $("#legends").show();
//                    var svg1 = d3.select("#legends").append("svg")
//                            .attr("width", "100%")
//                            .attr("height", "100%");
//                    if (parent.$("#shadeType").val() == "conditional") {
//                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        var selectedMeasure = conditionalMap["measure"];
//                        var keys = Object.keys(conditionalMap);
//                        svg1.append("g").append("text").attr("x", 20)
//                                .attr("y", 9)
//                                .attr("dy", ".35em").text(selectedMeasure);
//                        for (var no = 0; no < (keys.length - 1); no++) {
//                            var legend = svg1.append("g")
//                                    .attr("transform", function() {
//                                        return "translate(10," + ((no * 20) + 20) + ")";
//                                    });
//                            legend.append("rect")
//                                    .attr("width", 24)
//                                    .attr("height", 14)
//                                    .attr("cy", 9)
//                                    .attr("stroke", "black")
//                                    .attr("stroke-width", ".5")
//                                    .style("fill", conditionalMap[no]["color"]);
////                                        .attr("r", 5);
//                            legend.append("text")
//                                    .attr("x", 28)
//                                    .attr("y", 9)
//                                    .attr("dy", ".35em")
//                                    .text(function() {
//                                        if (conditionalMap[no]["operator"] === "<>") {
//                                            return autoFormating(conditionalMap[no]["limit1"]) + conditionalMap[no]["operator"] + autoFormating(conditionalMap[no]["limit2"]);
//                                        } else {
//                                            return conditionalMap[no]["operator"] + autoFormating(conditionalMap[no]["limit1"]);
//                                        }
//                                    });
////                                        .attr("fill", conditionalMap[no]["color"]);
//                        }
//                        $("#legends").css("width", w * .15);
////                            $("#legends").css("margin", "3% 84% auto");
//                    } else if (parent.$("#shadeType").val() === "gradient") {
//                        var colorMap = JSON.parse(parent.$("#measureColor").val());
//                        var height = $("#legends").height() - 10;
//                        var shadingMeasure = colorMap["measure"];
////                        var max = maximumValue(data, shadingMeasure);
////                        var min = minimumValue(data, shadingMeasure);
//                        var gradient = svg1.append("svg:defs")
//                                .append("svg:linearGradient")
//                                .attr("id", "gradientWrdMapLegend")
//                                .attr("x1", "0%")
//                                .attr("y1", "30%")
//                                .attr("x2", "50%")
//                                .attr("y2", "30%")
//                                .attr("spreadMethod", "pad")
//                                .attr("gradientTransform", "rotate(90)");
//
//                        gradient.append("svg:stop")
//                                .attr("offset", "0%")
//                                .attr("stop-color", colorMap[shadingMeasure])
//                                .attr("stop-opacity", 1);
//                        gradient.append("svg:stop")
//                                .attr("offset", "100%")
//                                .attr("stop-color", "rgb(230,230,230)")
//                                .attr("stop-opacity", 1);
//
//                        svg1.append("g").append("text").attr("x", 0)
//                                .attr("y", 9)
//                                .attr("dy", ".35em").text(shadingMeasure);
//                        svg1.append("g").append("rect")
//                                .attr("width", 10)
//                                .attr("height", "90%")
//                                .attr("fill", "url(#gradientWrdMapLegend)")
//                                .attr("x", 0)
//                                .attr("y", 15);
//                        svg1.append("g").append("text").attr("x", 10)
//                                .attr("y", height)
////                                .attr("dy", ".35em").text(addCommas(min));
//                        svg1.append("g").append("text").attr("x", 10)
//                                .attr("y", 25)
////                                .attr("dy", ".35em").text(addCommas(max));
//
//
//                        $("#legends").css("width", "8%");
//                    }
//                } else {
//                    map = d3.select('#maptDiv'+div).append("svg:svg")
//                            .attr("width", w * 2)
//                            .attr("height", h);
//                    svgcanvas = map.append("g")
//                            .attr("width", w * 2)
//                            .attr("height", h);
//                }



//               var svgcanvas1 = svgcanvas.append("g")
//                        .attr("id", "places1");

              var  data1 = [];
                var cordMap = {};
                var count = -1;
                var currNameDistrict ;
                var htmlframe = "<div id='stateLegend' style='height:300px'><iframe id='mapss' frameborder='0' width='500px' height='450px' style='margin-top:-30px;'></iframe></div>";
                $("#chart2").attr("style", "display:block");
                $("#section1").append(htmlframe);
                $("#mapss").attr("src", "svg/" + state.toLowerCase() + ".svg").load(function() {
                    var $map = $(this).contents();
                    var titles = $map.find('path').map(function() {

                        var parenttitle = $(this).parent().attr('title') || '';
                        var stateMap = {};
                        stateMap["title"] = $(this).attr('title');
//                        stateMap["path"] = $(this).attr('d');
                        data1.push(stateMap);

                        return '0\t' + parenttitle + '\t' + $(this).attr('title');
                    }).get();
                    $(this).attr("stroke", "steelblue")
                                .attr("stroke-width", ".2")
//                        $map.append("text").attr("x",10).attr("y",10).text("SADSAD");
                    $map.find('g').each(function() {
                     var svgG=d3.select(this);
//                     var counter=0;
                     d3.select(this).selectAll("path").each(function(d, i){
                        var counter = i;
                                    var bbox = this.getBBox();
                        svgG.append("text").style("text-anchor","middle").attr("transform", "translate(" + (bbox.x + bbox.width / 2)  + "," + (bbox.y + bbox.height / 2) + ")").text(function(d,j){
                            return data1[counter]["title"];
                        }).style("font-size","7px");
                      var district = $(this).attr('title');
                        $(this).attr('fill', function(){
                          count++;

                    //    var currName = (d.id).toUpperCase();
                        var colorShad;
                        var drilledvalue;
//
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
//
                if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && district ==drilledvalue) {
                    colorShad = drillShade;
                } else {
                    if(districtData!="undefined" && districtData!=""){
                    for(var i in districtData){

                        if(district.toLowerCase()== districtData[i][mapColumnsDistrict[0]].toLowerCase()){


                       return color(i);
                        }else{
                          colorShad = "white";
                        }
                    }
                        }}return colorShad;

                       })

                     }); $(this).attr('onclick', function(){
                         var districtArray = [];
                       $map.find('g').each(function() {
                     $(this).find('path').each(function(d,i) {
                      var district = $(this).attr('title');
                      districtArray.push(district)
                       $(this).on("click", function(){

//                           showDistrictTable(district,districtData,"districtClick")
                       })

                      })
                 })
//                  showDistrictTable(districtArray,districtData,"stateClick")
 })

                    })


                    var abc = state.toLowerCase();
});
//function  showDistrictTable(districtArray,districtData,flag){
//
//
//     if(typeof districtmapchartData["chart3"]!= "undefined" && flag=="stateClick1"){
//    chart12 = districtmapchartData["chart3"];
//
//    columns = chartData["chart3"]["viewBys"];
//    measureArray = chartData["chart3"]["meassures"];
//}else{
//      var districtLevelData = [];
//      for(var i in districtData){
//          if(flag!="districtClick"){
//    for(var k in districtArray){
//
//   if(districtArray[k].toLowerCase()== districtData[i][chartData["chart2"]["viewBys"][0]].toLowerCase()){
//
//       districtLevelData.push(districtData[i])
//   }}}else{
//if(districtArray.toLowerCase()== districtData[i][chartData["chart2"]["viewBys"][0]].toLowerCase()){
//
//       districtLevelData.push(districtData[i])
//   }
//   }
//      }
//
//     chart12 = districtLevelData;
//
//    columns = chartData["chart2"]["viewBys"];
//    measureArray = chartData["chart2"]["meassures"];
//}
//
//
//$("#mapTable" + div).html("");
//var htmlstr = mapTableFunction(chart12,columns,measureArray);
//$("#mapTable" + div).append(htmlstr);
// }
//
                function show_detailsOfMap(d, columnVal, measureArray, con, i, data) {
                    var content;
                    content = "<span class=\"name\">" + columnVal + ":</span><span class=\"value\"> " + d + "</span><br/>";
                    var msrData;
                    var val;
                    for (var l = 0; l < data.length; l++) {
                        if (d.toLowerCase() == data[l][columnVal].toLowerCase() || (d.toLowerCase() == "chatisgarh" && data[l][columnVal].toLowerCase() == "chhattisgarh")) {
                            for (t = 0; t < measureArray.length; t++) {
                                if (parent.isFormatedMeasure) {
                                    msrData = numberFormat(data[l][measureArray[t]], round, precition)
                                }
                                else {
                                    msrData = addCommas(data[l][measureArray[t]]);
                                }
                                content += "<span class=\"name\">" + measureArray[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                            }
                            return tooltip.showTooltip(content, d3.event);
                        }
                        else
                        {
                            msrData = "";
                        }

                    }
                }

            }
// end by mayank

//Added by shobhit for multi dashboard on 22/09/15
//Start
function buildHalfPieDB(div,chartId, data, columns, measureArray,wid,hgt,pieRadius,sliceIndex) {
     var color = d3.scale.category10();
     //      added by shivam     
            d3.selection.prototype.dblTap = function(callback) {
      var last = 0;
      return this.each(function() {
        d3.select(this).on("touchstart", function(e) {
            if ((d3.event.timeStamp - last) < 500) {
//                alert("touchstart id: "+this.id)
              return callback(e,this.id);
            }
//            alert("touchstart1111 id: "+this.id)
            last = d3.event.timeStamp;
        });
      });
    } 
     
     
    var divWidth, divHeight, rad;
    var w = $(window).width()/2+"px";
    divWidth=wid;
    var dashletid;
     divHeight=hgt;
     var widthRadius = wid;
     var heightRadius = hgt;
    var fromoneview=parent.$("#fromoneview").val();
     if(fromoneview!='null' && fromoneview=='true'){
divWid=divWidth;
     }
     else{
      divWid=wid;
     rad=divHeight*.50;
     }


    var autoRounding1 = "1d";
var chartData = JSON.parse(parent.$("#chartData").val());
var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
//     var prop = graphProp(div1);
colIds=chartData[div1]["viewIds"];
dashletid=div1;
var legendAlign;
if(typeof chartData[div1]["legendLocation"]==='undefined' || chartData[div1]["legendLocation"]==='Bottom')
{
    legendAlign='Bottom';
}
else
{
    legendAlign='Right';
}
}else{
//    var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
var legendAlign;
if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Bottom')
{
    legendAlign='Bottom';
}
else
{
    legendAlign='Right';
}
}

//    divWidth=$("#"+div).width();
//    divHeight=$("#"+div).height();
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var pi = Math.PI;
//    var fun = "drillWithinMultiDB(this.id,\""+div+"\")";
     //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
             fun = "drillWithinMultiDB(this.id,\""+div+"\")";
	
	}
             function drillFunct(id1){
             drillWithinMultiDB(id1,div);
	}
    
     if(fromoneview!='null'&& fromoneview=='true'){
var regid=div.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();

 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
var olap=div.substring(0, 9);
if(olap=='olapchart'){
fun = "viewAdhoctypes(this.id)";
    }
    }
    var divnum = parseFloat(div.replace("chart", "", "gi"));
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    var drillStates = parent.$("#drillStatus").val();
    var chartDiv;
    if (typeof drillStates !== "undefined" && drillStates !== "" && parent.$("#dashBoardType").val() === "drilldash") {
        chartDiv = JSON.parse(parent.$("#chartData").val());
        if (div === Object.keys(chartDiv)[Object.keys(chartDiv).length - 1] || (Object.keys(chartDiv).length > 6 && divnum >= 6)) {
            fun = "drillWithinchart(this.id)";
        }
        else {
            fun = "";
        }
    }
    var height = Math.min(divWidth, divHeight);
    var width = Math.min(divWidth, divHeight);
    var width = divWidth;
    var width2;
    var margintop;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
    {
        height = divHeight * 1.7;
        //        margintop = "30px";
        radius = rad;
        height = Math.min(width, height);
        width = Math.min(width, height);
        radius = (Math.min(width, height) / 2.7);
         width2=width*.7;
    }
    else if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
        height = divHeight * 1.8;
        margintop = "120px";
        radius = rad;
         width2=width*.7;
    }
    else {
        height = divHeight;
        margintop = "0px";
        radius = rad *.90;
         if(fromoneview!='null' && fromoneview=='true'){
             divHeight=divHeight ;
              rad=divWidth*.25;
              width=divWidth;
//               if(divWidth<=500){
//                height = divHeight;
//                rad=divWidth*.28;
//               }else{
//              height = divHeight;
              margintop = "margin-left:6em";
//    }
//                margintop = "margin-left:1em";
                  radius = rad;
                  width2=width;
         }else{
             width2=width*.7;
         }

    }


//    var outerRadius = 0;
//if(widthRadius<heightRadius){
//  if(parseInt(heightRadius-widthRadius)<(100)){
//
//     outerRadius = parseInt(widthRadius*.25);
//  }
//
//  else{
//
//    outerRadius = parseInt(widthRadius*.27);
//} }
//else{
//    if(parseInt(widthRadius-heightRadius)<(100)){
//
//      outerRadius = parseInt(heightRadius*.31);
//  }
//
//  else{
//
//     outerRadius = parseInt(heightRadius*.33);
//}
//
//}
//if(legendAlign==='Right')
//{
//    outerRadius *= 0.7;
//}
////alert(outerRadius*2+" : "+width*0.62);
//if(outerRadius*2>width*0.62)
//{
//    outerRadius=((width*0.63)/2)*0.9;
//}
    graphProp("chart1");
    var arc = d3.svg.arc()
            .outerRadius(pieRadius*0.8);
//    var arcFinal = d3.svg.arc().innerRadius(radius).outerRadius(radius);
    var pie = d3.layout.pie().sort(null) //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            }).startAngle(-90 * (pi/180))
        .endAngle(90 * (pi/180));
       var lableColor;
    var svg = d3.select("#" + chartId).append("svg:svg")
            .datum(data)
            .attr("id", "svg_" + chartId)
//             .attr("viewBox", "0 "+widthRadius*.03+" "+(widthRadius*.8)+" "+(heightRadius*.8)+" ")
            .attr("width",(wid))
            .attr("height", (hgt))
//            .attr("style", margintop);
//    var remarks='';
//    if(typeof chartData[div]["remarks"]!=="undefined" && chartData[div]["remarks"]!=="" )
//    {
//        remarks=chartData[div]["remarks"];
//
//    }
//    else
//    {
//        remarks='No remarks for this chart';
//    }
//    var remarkHeight=$("#div"+div).height();
//    var div1=svg.append("foreignObject")
//        .attr("width", (widthRadius*.8))
//        .attr("height", remarkHeight/24.78)
//        .attr('x', 0)
//        .attr( 'y', remarkHeight-(remarkHeight/16)-(remarkHeight/5))
//        .append("xhtml:body")
//        .attr('xmlns','http://www.w3.org/1999/xhtml')
//        .html("<input id='txt"+div+"' type='text' style='float:left;height: "+(remarkHeight/19)+"px;width: "+((widthRadius*.8))+"px;display:none' value='"+remarks+"'/><label id='lbl"+div+"'  style='background-color:#F2F2F2;color:black;font-size:10px;text-align:left;height: "+(remarkHeight/19)+"px;width: "+(widthRadius*.8)+"px; ' value='"+remarks+"'><a id='a_"+div+"' style='margin-right:20px;float:right;width:16px;' onclick='editRemarks(\""+div+"\")' class='ui-icon ui-icon-pencil'></a>"+remarks+"</label>")
//        .on("change", function() {displayText('txt'+div,div)});
if(fromoneview!='null'&& fromoneview=='true'){
                 div=div1;
             }
    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
            .append("svg:linearGradient")
            //    .attr("id", function(d) {return color(d.name);)
            .attr("id", function(d) {
                return "gradient" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("x1", "0%")
            .attr("y1", "0%")
            .attr("x2", "50%")
            .attr("y2", "100%")
            .attr("spreadMethod", "reflect");

    gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "60%")
            .attr("stop-color", function(d, i) {
                var colorIndex=0;
                if(sliceIndex==0){
                    colorIndex=i;
                }
                else{
                    colorIndex=sliceIndex;
                }
                var colorShad;
                var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[columns[0]];
                } catch (e) {
                }
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    return getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                    colorShad = drillShade;
                }
                else if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
                    var drills = {};
                    if (parent.$("#drills").val() !== "") {
                        drills = JSON.parse(parent.$("#drills").val());
                    }
                    var keysOfdata = (Object.keys(drills));
                    var len = keysOfdata.length;
                    for (var j = divnum - 1; j < len; j++) {
                        if (drills[keysOfdata[j]].indexOf(d[columns[0]]) !== -1) {
                            colorShad = drillShade;
                        }
                        else {
                            colorShad = getDrawColor(div, parseInt(colorIndex));
                        }
                    }
                    colorShad = getDrawColor(div, parseInt(colorIndex));
                }
                else {
//                    if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
//                    } else {
                        colorShad = getDrawColor(div, parseInt(colorIndex));
//                        colorShad = "blue";
//                    }
                }
                chartMap[d[columns[0]]] = colorShad;
                return getDrawColor(div, parseInt(colorIndex));
//                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", width*.7)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var wDiv=2.5;
    if(legendAlign==='Right')
    {
        wDiv=3.45;
    }
    var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            // .attr("class", "arc")
            .attr("id", function(d) {
//                var drillValued = d.data[columns[0]];
                return columns[0] + ":" + d.data[columns[0]] + ":" + chartId;

            })

            .attr("transform", "translate(" + wid / 2 + "," + hgt*0.75  + ")")
            .attr("onclick", fun)
	//Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}); 
    if(pieRadius*0.8<20){
    svg.selectAll("dot")
            .data(pie)
            .enter().append("circle")
            .attr("r", function(d){
                    return 10;
            })
            .attr("fill", function(d,i) {
                var colorIndex=0;
                if(sliceIndex==0){
                    colorIndex=i;
                }
                else{
                    colorIndex=sliceIndex;
                }
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];

                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                 
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,colorIndex,color)
                return colorfill;
                }
//                return getDrawColor(div,parseInt(i));}//"url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("cx", function(d) {
                return $("#svg_"+chartId).width()/2;
            })
            .attr("cy", function(d) {
                return $("#svg_"+chartId).height()/2;
            })
            .attr("id", function(d) {
//                var drillValued = d.data[columns[0]];
                return columns[0] + ":" + d.data[columns[0]] + ":" + chartId;

            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
                var colorIndex=0;
                if(sliceIndex==0){
                    colorIndex=i;
                }
                else{
                    colorIndex=sliceIndex;
                }
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];

                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                 
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,colorIndex,color)
                return colorfill;
                }
			
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .on("mouseover", function(d, i) {
//
                if(chartId==='topDiv0'){
                    prevColor = getDrawColor(div,parseInt(colorIndex));
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                    return;
                }
                var columnList = [];
                columnList.push(columns[0]);
                var colorIndex=0;
                if(sliceIndex==0){
                    colorIndex=i;
                }
                else{
                    colorIndex=sliceIndex;
                }
                prevColor = getDrawColor(div,parseInt(colorIndex));

                if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d.data, columns, measureArray, this,chartData,div1);
                }else{

                       var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                show_details(d.data, columnList, measureArray, this,div);
                }

            })
            .on("mouseout", function(d, i) {
                if(fromoneview!='null'&& fromoneview=='true'){

                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
                     }
//                }
                hide_details(d, i, this);
            })
            .attr("onclick", fun)
//	//Added by shivam
//	.dblTap(function(e,id) {
//		drillFunct(id);
//        });
    }
    else{
    arcs.append("path")
            .attr("fill", function(d,i) {
                var colorIndex=0;
                if(sliceIndex==0){
                    colorIndex=i;
                }
                else{
                    colorIndex=sliceIndex;
                }
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];

                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                 
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,colorIndex,color)
                return colorfill;
                }
//                return getDrawColor(div,parseInt(i));}//"url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
                var colorIndex=0;
                if(sliceIndex==0){
                    colorIndex=i;
                }
                else{
                    colorIndex=sliceIndex;
                }
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];

                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                 
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,colorIndex,color)
                return colorfill;
                }
			
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .on("mouseover", function(d, i) {
//
                if(chartId==='topDiv0'){
                    prevColor = getDrawColor(div,parseInt(colorIndex));
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                    return;
                }
                var columnList = [];
                columnList.push(columns[0]);
                var colorIndex=0;
                if(sliceIndex==0){
                    colorIndex=i;
                }
                else{
                    colorIndex=sliceIndex;
                }
                prevColor = getDrawColor(div,parseInt(colorIndex));

                if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d.data, columns, measureArray, this,chartData,div1);
                }else{

                       var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                show_details(d.data, columnList, measureArray, this,div);
                }

            })
            .on("mouseout", function(d, i) {
                if(fromoneview!='null'&& fromoneview=='true'){

                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
                     }
//                }
                hide_details(d, i, this);
            })
            .transition()
            .ease("bounce")
            .duration(2000)
            .attrTween("d", tweenPie);
            }
    function angle(d) {

        return (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
    }
    //    if((typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"  )) && isDouble===false ){
    //    }else{
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
//            .append("svg:text")
//            .attr("dy", ".35em")
//            .attr("text-anchor", "start")
//            .attr("style", "font-family: lucida grande")
//            .attr("style", "font-size: 12px")
//            .attr("transform", function(d) {
//                return "translate(" + arcFinal.centroid(d) + ")rotate(" + angle(d) + ")";
//            });
//    arcs.filter(function(d) {
//        return d.endAngle - d.startAngle > 0.2;
//    })
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "middle")
            .attr("style", "font-family: lucida grande")
            .attr("style","font-size:"+12+"px")
            .attr("transform", function(d) {
                var a = angle(d);

//                if (a < -90) {
//                    a = a + 180;
                    d.outerRadius = 0; // Set Outer Coordinate
                    d.innerRadius = 0 ;
//                } else {
//                    d.outerRadius = radius; // Set Outer Coordinate
//                    d.innerRadius = radius / 2;
//                }
                // Set Inner Coordinate
                return "translate(" + arc.centroid(d) + ")";
            })
            .style("fill", function(d, i){
//               var lableColor;
//                   if (typeof chartData[div]["labelColor"]!=="undefined") {
//                              lableColor = chartData[div]["labelColor"];
////                               return "white";
//                          }else {
//                               lableColor = "#000000";
////                               return "black";
//                               }
                               return "#ffffff";
             })
            .text(function(d) {
//                var percentage = (d.value / parseFloat(sum)) * 100;
//                return percentage.toFixed(1) + "%";
//if (typeof chartData[div]["valueOf"] !== "undefined" && chartData[div]["valueOf"] === "Absolute") {
//                    if (isFormatedMeasure) {
//                        return numberFormat(d.data[measureArray[0]], round, precition);
//                    }
//                    else {
                        if(chartId=='topDiv0'){
                            return 'All';
                        }
////                    }
//                }
//                else {
//                if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes')
//                    {
//                   var percentage = (d.value / parseFloat(sum)) * 100;
//                return percentage.toFixed(1) + "%"
//                    }
//                    else
//                    {
//                        return '';
//                    }
//                }
            });
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        svg.append("svg:text")
//        .attr("x", "0")
//        .attr("y", "15")
//        .attr("fill", "")
//        .attr("dy", ".35em")
//        .attr("text-anchor", "middle")
//        .attr("style", "font-family: lucida grande")
//        .attr("style", "font-size: 12px")
//        .attr("transform", transformWord).text(columns[0] + "," + measureArray[0]);
//    }
var yAxisFormat = "";
   var yAxisRounding = 0;
  if(typeof chartData[div]["yAxisFormat"]!= "undefined" && chartData[div]["yAxisFormat"]!= ""){
      yAxisFormat = chartData[div]["yAxisFormat"];
  }else{
   yAxisFormat = "Auto";
  }
  if(typeof chartData[div]["rounding"]!= "undefined" && chartData[div]["rounding"]!= ""){
      yAxisRounding = chartData[div]["rounding"];
  }else{
   yAxisRounding =1;
  }
    svg.append("text")
    .attr("x",wid/2)
    .attr("y",function(d){
        if(chartId=='topDiv0'){
            return hgt-15;
        }
        else{
            return hgt-5;
        }
    })
    .attr("text-anchor","middle")
    .style("font-weight","bold")
    .text(function(d){
       if(chartId=='topDiv0'){
           if(yAxisRounding>0){
            return measureArray[0]+" : "+numberFormat(data[0][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")
           }
           else{
            return measureArray[0]+" : "+addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(data[0][measureArray[0]],yAxisFormat,yAxisRounding,"chart1"))
               
        }
        }
        else{
            if(yAxisRounding>0){
                return data[0][columns[0]]+" : "+numberFormat(data[0][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")
            }
            else{
            return data[0][columns[0]]+" : "+addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(data[0][measureArray[0]],yAxisFormat,yAxisRounding,"chart1"))
        }
        }
    })
    function tweenPie(b) {
        b.innerRadius = 0;
        var i = d3.interpolate({
            startAngle: 0,
            endAngle: 0
        }, b);
        return function(t) {
            return arc(i(t));
        };
    }
}
function drillWithinMultiDB(idArr,id){
	$("#loading").show();
    var allViewIds=JSON.parse($("#viewbyIds").val());
    var allViewBys=JSON.parse($("#viewby").val());
    
//    alert(allViewBys);
//    alert(idArr.split(":")[0]);
//    alert(allViewBys.indexOf(idArr.split(":")[0].trim()));
    var drillViewById=allViewIds[allViewBys.indexOf(idArr.split(":")[0])];
    var chartData = JSON.parse($("#chartData").val());
    var div = "chart1";
    var drillValues = [];
    var drills = {};
            if(idArr.split(":")[1]=="All"){
                $("#drills").val('');   
                $("driver").val('');
                drillDivId=["topDiv0"];
            }else {
           if(typeof chartData[div]["multiFilter"] !=="undefined" && chartData[div]["multiFilter"]=="Y"){
                var index=drillDivId.indexOf("topDiv0");
                if(index!=-1){
                    drillDivId.splice(index, 1);
                }
             
            if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
                drills = JSON.parse($("#drills").val());
                if(typeof drills[drillViewById]!="undefined"){
                   
                    drillValues = drills[drillViewById];
                }
            }
            drillValues.push(idArr.split(":")[1]);
            drills[drillViewById] = drillValues;
            $("#drills").val(JSON.stringify(drills));
           drillDivId .push(idArr.split(":")[2]);
           }else {
                drillValues.push(idArr.split(":")[1]);
    drills[drillViewById] = drillValues;
    $("#drills").val(JSON.stringify(drills)); 
    drillDivId=[];
    drillDivId .push(idArr.split(":")[2]);    
           }
           $("#driver").val('chart1');
           }
    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
        function(data){
            generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
            $("#driver").val("");
            $("#loading").hide();
        });
}
//end by shobhit

 function buildMultiMeasureTrLineAdvance(div, data, columns, measureArray, divWid, divHgt) {
    
    var record = 15;
var currData = [];
if($("#chartType").val()==="India-Map-With-Trend"){
    for(var k=0;k<(data.length < record ? data.length : record);k++){
        currData.push(data[k]);
    }
    data = currData;
}
    
      //      added by shivam     
            d3.selection.prototype.dblTap = function(callback) {
      var last = 0;
      return this.each(function() {
        d3.select(this).on("touchstart", function(e) {
            if ((d3.event.timeStamp - last) < 500) {
//                alert("touchstart id: "+this.id)
              return callback(e,this.id);
            }
//            alert("touchstart1111 id: "+this.id)
            last = d3.event.timeStamp;
        });
      });
    } 
if (typeof data!== 'undefined' && data.length==1) {
return buildMultiAxisBar(div, data, columns, measureArray, divWid, divHgt)

}
var divId = "";
if($("#chartType").val()==="India-Map-With-Trend"){
    divId = "section2";
}else{
    divId = div;
}
var color = d3.scale.category10();
 var fromoneview=parent.$("#fromoneview").val();
  var widthdr=divWid
    var divHgtdr=divHgt
    if(fromoneview!='null'&& fromoneview=='true'){
       divWid=divWid-80;
    }
//    else{
//     divWid=parseFloat($(window).width())*(.35);
//    }
    var measure1 = measureArray[0];
    var autoRounding1;
    var measArr = [];
     var chartData = JSON.parse(parent.$("#chartData").val());
     if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top'){
       divHgt -=35;
   }
      var customTicks = 5;
     if(fromoneview!='null'&& fromoneview=='true'){

     }else{
//         if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="") {
//     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
     }

var dashletid;
  var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
     dashletid=div;
     div=div1
colIds=chartData[div1]["viewIds"];
}else{
       var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
}
    var isDashboard = parent.$("#isDashboard").val();
//    var fun = "drillWithinchart(this.id,\""+div+"\")";

 //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
            fun = "drillWithinchart(this.id,\""+div+"\")";
	}function drillFunct(id1){
         drillWithinchart(id1,div);
            
	}
	

    var hide = "hideLabels(this.id,\""+div+"\")"
     if(fromoneview!='null'&& fromoneview=='true'){
var regid=div.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();

 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
var olap=div.substring(0, 9);
if(olap=='olapchart'){
fun = "viewAdhoctypes(this.id)";
    }
    }
    var botom = 70;
    var j = 0;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
        botom = 70;
    }

     var margin = {};
    var width = 0;
    var height = 0;
    if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
                 height = parseFloat(divHgt)* .75;
    }else{

            height = parseFloat(divHgt)* .80;
    }
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
         margin = {
        top: 20,
        right: 10,
        bottom: botom,
        left: 10
    };
    width = divWid; //- margin.left - margin.right

    }else{
          margin = {
        top: 20,
        right: 10,
        bottom: botom,
        left: 80
    };
    width = divWid; //- margin.left - margin.right

    }
//    width = parseFloat(divWid)-94, //- margin.left - margin.right
//    height = parseFloat(divHgt)* .55;
        var leftM=0,rightM=0,topM=0,bottomM=0;
        if( chartData[div]["labelPosition"]==='Top'){
            topM=20;
        }
        if(typeof chartData[div]["labelPosition"] === 'undefined' || (typeof chartData[div]["labelPosition"]!=='undefined' && chartData[div]["labelPosition"]==='Bottom')){
            bottomM=0;
        }
        if(chartData[div]["labelPosition"]==='Right'){
            rightM=75;
        }
        if(chartData[div]["labelPosition"]==='Left'){
            leftM=25;
        }
        margin={
          top:margin.top+topM,
          right:margin.right+rightM,
          left:margin.left+leftM,
          bottom:margin.bottom+bottomM
        };
    var max = 0;
var minVal = 0;
 if(fromoneview!='null'&& fromoneview=='true'){
       for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}

}
 for(var i in measureArray){
var min = parseFloat(minimumValue(data, measureArray[i]));
if(i==0)  {

minVal=parseFloat(min);
}
else if(i>0 && ( min<minVal)){
minVal = min;
}
}

    }else{
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}

}
}}else{
  for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}

}
}
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="") {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default" ){
   minVal = 0;
 }
 else{
     for(var i in measureArray){
var min = parseFloat(minimumValue(data, measureArray[i]));
if(i==0)  {

minVal=parseFloat(min);
}


else if(i>0 && ( min<minVal)){
minVal = min;
}
}
   }
}else{
    for(var i in measureArray){
var min = parseFloat(minimumValue(data, measureArray[i]));
if(i==0)  {

minVal=parseFloat(min);
}
else if(i>0 && ( min<minVal)){
minVal = min;
}
}
    }

}





//for(var i in measureArray){
//var max1 = parseFloat(maximumValue(data, measureArray[i]));
//var min = parseFloat(minimumValue(data, measureArray[i]));
//if(i==0)  {
//
//max=parseFloat(max1);
//minVal=parseFloat(min);
//}
//
//else if(i>0 && (max1 > max)){
//max = max1;
//}
//else if(i>0 && ( min<minVal)){
//minVal = min;
//}
//}
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
  //  var max = maximumValue(data, measure1);
 //   var minVal = minimumValue(data, measure1);
    var y = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
    // Axis setting
    if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
        make_x_axis = function() {
    return d3.svg.gridaxis()
        .scale(x)
         .orient("bottom")
         .ticks(5)
}

 make_y_axis = function() {
    return d3.svg.gridaxis()
        .scale(y)
        .orient("left")
        .ticks(customTicks)
}
}else{
       make_x_axis = function() {
    return d3.svg.axis()
        .scale(x)
         .orient("bottom")
         .ticks(5)
}

 make_y_axis = function() {
    return d3.svg.axis()
        .scale(y)
        .orient("left")
        .ticks(customTicks)
}
}
if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
    var xAxis = d3.svg.trendaxis()
            .scale(x)
            .orient("bottom");
}else{
    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
}
    if (isFormatedMeasure) {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d) {
//                    return numberFormat(d, round, precition);
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(d);
                });

    }
    else {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d, i) {
                     if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
//                    return d;
//                    return autoFormating(d, autoRounding1);
                });
    }
 var yAxis1 = d3.svg.axis1()
            .scale(y)
            .tickFormat(function(d, i) {
                measArr.push(d);
                return "";
            });
    var valueline = d3.svg.area().interpolate("monotone")
    var valueline = d3.svg.area()
            .x(function(d) {
                return x(d[columns[0]]);
            })
            .y(function(d) {
                return y(d[measureArray[j]]);
            });

     if(fromoneview!='null'&& fromoneview=='true'){
     div=dashletid;
     }
    var offset=0;
 if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top'){
     offset=30;
 }     
    var svg = d3.select("#" + divId)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + divId)
//            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")
//            .classed("svg-content-responsive", true)
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom + 17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");
//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");

svg.append("g")
    .append("svg:svg")
//    .style("margin-left", "3em")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right+17.5);

//for (var j=0; j <= height; j=j+50) {
//    svg.append("svg:line")
//        .attr("x1", 0)
//        .attr("y1", j)
//        .attr("x2", width)
//        .attr("y2", j)
//        .style("stroke", "#A69D9D")
//        .style("stroke-width", .5)
//        .style("z-index", "9999");
//};


    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");

 var len = 0;
var yvalue = -(height*.24);
var dyvalue = ".41em";
var count = 0;
var transform = "";
//alert($("#" + div).width())
var fontSSize=parseInt(($("#" + div).width())*.002)/2;
if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
     }

if(fontSSize>.5 ){
fontSSize=9;
}else if( fontSSize<=.5 ){
fontSSize=8;
}
//alert(fontSSize)
var step=0,xPos=0;
if(typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Left"){
    transform="rotate(270)";
    len=height*1.1;
    step=height/3;
    yvalue=-90;
     }
else if(typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Right"){
    transform="rotate(270)";
    len=height*1.1;
    step=height/3;
    yvalue=width*1.04;
     }
else if(typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Bottom"){
    len=width*.05;
    step=width/5;
    yvalue=(height + margin.top + margin.bottom+ 17.5 )-((height + margin.top + margin.bottom+ 17.5 )/10.66);
//    alert(height + margin.top + margin.bottom+ 17.5 );
//    yvalue=58;
}
else if(typeof chartData[div]["labelPosition"] === 'undefined' || (typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Top")){
    len=width*.05;
    step=width/5;
    yvalue=-20;
}
if(typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]!="" && chartData[div]["displayLegends"]!="Yes"){}
else{
var boxW=width + margin.left + margin.right;
var boxH=height + margin.top + margin.bottom+ 17.5 ;
var maxMeasure='';
for(var i in measureArray){
    var measureName='';
    if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
        measureName=chartData[div]["measureAlias"][measureArray[i]];
    }else{
        measureName=checkMeasureNameForGraph(measureArray[i]);
    }
    if(measureName.length>maxMeasure.length){
        maxMeasure=measureName;
    }
}
var len1=maxMeasure.length;
if(columns[0].length+2>len1){
    len1=columns[0].length+2;
}

var rectW=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectW=boxW-40;
}
else{
    rectW=30+(len1*6.5)+110;
}

rectW = rectW<170?170:rectW;
var viewByHgt=15;
var rectH=10+(17*measureArray.length)+viewByHgt;    ///trline
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectH=17*(parseInt((measureArray.length-1)/3)+1);
}
else{
    rectH=10+(17*measureArray.length)+viewByHgt;
}

var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
      rectX=30;
}
else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright" ){
    rectX=boxW-rectW;
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=5;
}
else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
    rectX=boxW/2-rectW/2;
}

var rectY=boxH-(boxH*1.03)-45;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectY=boxH-(boxH*1.03)-45;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)-5;
}
else if(chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.27)-(3*measureArray.length);
}

var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
//alert((boxH-(boxH*.98)-5)+":"+boxW);
var offset1=0;
var offset2=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    offset1=-40;
    offset2=10;
}
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .style("stroke", "grey")
            .style("stroke-dasharray", ("3, ")+border)
//            .attr("transform", "translate(" + width*.25  + "," + height*0.25 + ")")
            .attr("x", rectX+offset1)
            .attr("y", rectY+offset2)
            .attr("width", (rectW-85))
            .attr("height", rectH)
            .attr("rx", 10)         // set the x corner curve radius
            .attr("ry", 10)
            .attr("fill", backColor);
}
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){}
else{            
      svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+20+count*15))
            .attr("fill", 'black')
            .text(columns[0]);
}
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    var xStep=rectW/3.3;
    var yStep=15;
    
for(var i=0;i<=measureArray.length;i++){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
        var measureName1='';
   svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX-20+(xStep*(i%3)))
            .attr("y",(rectY+23+(yStep*parseInt(i/3))))
            .attr("fill",function(d){
                if(i==0){
                    return 'black';
                }
                else{
                getDrawColor(div, parseInt(i-1))
                }
                } )
            .text(function(d){
        if(i==0){
            return columns[0];
        }
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i-1]]!='undefined' && chartData[div]["measureAlias"][measureArray[i-1]]!=''){
            measureName1=chartData[div]["measureAlias"][measureArray[i-1]];
            if(measureName1==='undefined'){
                measureName1=measureArray[i-1];
            }
        }else{
            measureName1=checkMeasureNameForGraph(measureArray[i-1]);  // dual axis bar
        }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i-1]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i-1]] === "20") {
                    length=20;
                }
                else{
                    length=chartData[div]["measureLabelLength"][measureArray[i-1]];
                }
                if(measureName1.length>length){
                    return measureName1.substring(0, length)+"..";
                }else {
                    return measureName1;
          }
           }).attr("svg:title",function(d){
               return measureName1;
           })
           var rectsize;
           if(width<height){
              rectsize = parseInt(width/25);
           }
           else{
              rectsize = parseInt(height/25);
           }
           rectsize=rectsize>10?10:rectsize;
   if(i!=0){
   svg.append("g")
            .append("rect")
            .attr("x",rectX-33+(xStep*(i%3)))
            .attr("y",(rectY+15+(yStep*parseInt(i/3))))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i-1)))
   }
              count++
   }
}
else{
for(var i in  measureArray){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
        var measureName2='';
   svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+25)
            .attr("y",(rectY+20+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!='' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName2=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName2=checkMeasureNameForGraph(measureArray[i]);
        }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i]] === "20") {
                    length=20;
                }
                else{
                    length=chartData[div]["measureLabelLength"][measureArray[i]];
                }
                if(measureName2.length>length){
                    return measureName2.substring(0, length)+"..";
                }else {
                    return measureName2;
          }
           }).attr("svg:title",function(d){
               return measureName2;
           })
           var rectsize;
           if(width<height){
              rectsize = parseInt(width/25);
           }
           else{
              rectsize = parseInt(height/25);
           }
           rectsize=rectsize>10?10:rectsize;
           svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
//            .attr("style","margin-right:10")
//            .attr("style", "overflow:scroll")

            .attr("x",rectX+10)
            .attr("y",(rectY+12+viewByHgt+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
              count++
   }
}
}
    var min1 = [];
    var flag = "";
    for (var key in data) {
        for (var meas in measureArray) {
            min1.push(data[key][measureArray[meas]]);
        }
    }
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

if(fromoneview!='null'&& fromoneview=='true'){
div=dashletid;
     }
    svg = d3.select("#" + divId).select("g");
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){
                        flag = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
              }
           else{
                    flag = numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
              }
           else{
           flag =  columns[0];
           }
          return flag
             });
             if(fromoneview!='null'&& fromoneview=='true'){
div=div1;
     }
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{


    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
//Added By Ram
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i)
//                if(typeof d!="undefined"){
//                if ( i%k==0){
//           if(d.length < 13){
//                    return d;}
//                else
//                    return d.substring(0, 10) + "..";
//            }else
//            return "";
//            }
            
            })
            .attr('transform', 'rotate(-25)')
            .append("svg:title").text(function(d) {
        return d;
    });
}
    // Title of the graph
    //	svg.append("text")
    //		.attr("x", (width / 2))
    //		.attr("y", 0 - (margin.top / 2))
    //		.attr("text-anchor", "middle")
    //		.style("font-size", "16px")
    //		.text("Value vs Date Graph");

    var colorArr = [];
    colorMap = {};
    var chartMap = {};
    var perTotal;
    // Add the line 1
    if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
    svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-width, 0, 0)
            .tickFormat("")
        )
    }
    for(var i=0;i<measureArray.length;i++){
        var target = "";
        var labelLen=0,valueLen=0;
        labelLen=measureArray[i].length;
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
            target = chartData[div]["defineTargetline"][measureArray[i]];
           svg.append("text")
            .text(function(d){
                if(yAxisFormat==""){
                        valueLen=addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div)).length;
                        return addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
                    }
            else{
                        valueLen=numberFormat(target,yAxisFormat,yAxisRounding,div).length;
                    return numberFormat(target,yAxisFormat,yAxisRounding,div);
                }
            })       
            .attr("x", (width)*.90)
            .attr("y", y(parseInt(target))-12)
            .attr("style","font-size:8px");
            
             svg.append("text")       
            .attr("x", (width-labelLen*5.2))
            .attr("y", y(parseInt(target))-5)
            .attr("style","font-size:8px")
            .text("("+measureArray[i]+")");
        }
        var valuelineH = d3.svg.line()
        .x(function(d,i) {
            if(i==0){
                return x(d[columns[0]]) - x(0);
            }else{
                return x(d[columns[0]]) + x(i.length);
            }
        })
        .y(function(d) {
            if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){
                target = chartData[div]["defineTargetline"][measureArray[i]];
                return y(parseInt(target));
            }
        });
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){
            var path =   svg.append("path")
            .data(data)
            .attr("d", valuelineH(data))
            .attr("fill", "transparent")
            .attr("x", (width)*0.85)
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
    
            var totalLength = path.node().getTotalLength();
            path
            .attr("stroke-dasharray", totalLength + " " + totalLength)
            .attr("stroke-dashoffset", totalLength)
            .transition()
            .duration(2000)
            .ease("linear")
            .attr("stroke-dashoffset", 0);
        }
    }
   //add by mayank sh. for by default display one measure on chart
    for (var i = 0; i < measureArray.length; i++) {
//        if(i==0){
//        if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "Yes")) {
//        continue;
//    }}else{
//    if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "No") {
//        continue;
//    }}      

        j = i; //end by mayank sh.
        var path=svg.append("path")
                .data(data)
                .attr("d", valueline(data))
                .attr("fill", getDrawColor(div, parseInt(i)))
                 .style("stroke-width", "3px")

                 .style("stroke-dasharray",function(d){    // for dash line by mynk sh.
            //alert(chartData[div]["lineType"]);
        if(typeof chartData[div]["lineType"]!=="undefined" && chartData[div]["lineType"]==="dashed-Line"){
            return "6,2";
        }
        else{
            return "1,0";
        }} // for dash line by mynk sh.
            ) .style("stroke", getDrawColor(div, parseInt(i)));
//                var totalLength = path.node().getTotalLength();
//
//    path
//      .attr("stroke-dasharray", totalLength + " " + totalLength)
//      .attr("stroke-dashoffset", totalLength)
//      .transition()
//        .duration(4000)
//        .ease("linear")
//        .attr("stroke-dashoffset", 0);
        colorArr.push(getDrawColor(div, parseInt(i)));
        if (typeof chartMap[measureArray[i]] === "undefined") {
            chartMap[measureArray[i]] = getDrawColor(div, parseInt(i));
            colorMap[i] = measureArray[i] + "__" + getDrawColor(div, parseInt(i));
        }
        parent.$("#colorMap").val(JSON.stringify(colorMap));
     //LabelText

     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
     //Graph Label
        svg.selectAll("labelText")
             .data(data).enter().append("text")
//            .attr("d", valueline(data))
                   .attr("id", function(d,i){
                       return d[columns[0]] + ":" + d[measureArray[i]];
                   })
                   .attr("x", function(d){
                       return x(d[columns[0]]) - 15;
                   } ).attr("y", function(d){
                       return y(d[measureArray[i]]) -10;
                   }).attr("onclick", hide)

      .attr("dy", ".35em")
            .text(function(d)
                {
                    //aaaa
                    
            if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[i]]!='undefined'){
                yAxisFormat=numberFormatArr[measureArray[i]];
            }
            else{
                yAxisFormat="";
            }
            if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[i]]!='undefined'){
                yAxisRounding=numberRoundingArr[measureArray[i]];
            }
            else{
                yAxisRounding="0";
            }
        
                      if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='Absolute'){
                        if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){

                            return addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
                }else {
                    return "";
                }
                    }else {
                        return addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
                    }
                }
                else{
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                            var percentage = (d[measureArray[i]] / parseFloat(sum)) * 100;
                            return percentage.toFixed(1) + "%";

                        }else {
                            return "";
                        }
                    }else {
                        var percentage1 = (d[measureArray[i]] / parseFloat(sum)) * 100;
                        return percentage1.toFixed(1) + "%";
                    }
                }
                    }else{
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='%-wise'){
                        
                       if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                             var percentage = (d[measureArray[i]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";

                }else {
                    return "";
                        }
                    }else {
                    var percentage1 = (d[measureArray[i]] / parseFloat(sum)) * 100;
                    return percentage1.toFixed(1) + "%";
                }
            }
                else{
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                            
                            return addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));

                        }else {
                            return "";
                        }
                    }else {
                        return addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
                    }
                }
            }
        }else {
            return "";
        }
       })
       .attr("fill",function(d){
            var lableColor;
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                lableColor = chartData[div]["labelColor"];
            //                               return "white";
            }else {
                lableColor = "#000000";
            //                               return "black";
            }
            return lableColor;
       });


       var blueCircles = svg.selectAll("dot")
                .data(data)
                .enter().append("circle")
               .attr("r", function(d){
                var radius;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        radius = "6";
                    }else{
                        return radius = "3"
                    }
                    return radius;
                })      
                .attr("cx", function(d) {
                    return x(d[columns[0]]);
                })
                .attr("cy", function(d) {
                    return y(d[measureArray[i]]);
                })
               
               
		 .style("fill", function(d){
                var colorShad;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
                    }else{
                 // if(d[measure1] < 550){
                  // colorShad = color(1);
               // }else {
                   colorShad = getDrawColor(div, parseInt(i));

                  // } 
               }

                return colorShad;


            })
//				  .style("fill", getDrawColor(div, parseInt(i)))
                .style("stroke", getDrawColor(div, parseInt(i)))
                .style("stroke-width", "2px")
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measureArray[i]] + "#"+measureArray[i];
                })
                .attr("onclick", fun)
        
	//Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
        
                .on("mouseover", function(d) {
                   
                   var msrData;

var idArr = $(this).attr("id");
        var columnName = idArr.split(":")[0];
        var measureArr = idArr.split(":")[1];
      
        var measureName = measureArr.split("#")[1];
        var measureValue = measureArr.split("#")[0];
        
     
            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(measureValue);
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

               msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(measureValue);

                }
            else{

                 msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));
            }
        //               var msrData;
//                if (isFormatedMeasure) {
//
//                    msrData = numberFormat(d.value, round, precition,div);
//
//        }  else {
//
//                  msrData = addCommas(d.value);
//
//                }

                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + columnName + "</span><br/>";
                content += "<span class=\"name\"> " + measureName + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event); //m
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
    }
for(var i=0;i<measureArray.length;i++){
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
       svg.append("text")
              .text(addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.95)
              .attr("y", y(parseInt(avg))-5);
      var Averageline = d3.svg.line()
            .x(function(d,i) {
       if(i==0){
               return x(d[columns[0]]) - x(0);
               }else{
                   return x(d[columns[0]]) + x(i.length);
               }
            })
            .y(function(d) {
                return y(parseInt(avg));   
           } )

     	  var path =   svg.append("path")
            .data(data)
            .attr("d", Averageline(data))
            .attr("fill", "transparent")
            .attr("x", (width)*0.85)
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
    }
            }
    }
    
function buildTimeDB(div,data,columns,measureArray){
    $("#viewMeasureBlock").css("display", "none");
    $("#"+div).append(createTimeDBLayout(div,measureArray,columns, data["chart1"]));
     var chartData=JSON.parse($("#chartData").val());
        var chWidth = 200;
        var chHeight = 150;
    var barData = data["chart1"];
//    alert(JSON.stringify(data));
    var wid=$("#topDiv0").width();
    var hgt=$("#topDiv0").height();
    var parentColumns=[];
    var allViewBys=JSON.parse($("#viewby").val());
    var allViewByIds=JSON.parse($("#viewbyIds").val());
    if(typeof chartData["chart1"]["isDashboardDefined"]!='undefined' && chartData["chart1"]["isDashboardDefined"]==='Y')
        {
            parentColumns.push(allViewBys[allViewByIds.indexOf($("#parentViewBy").val())]);
        }
        else{
            parentColumns.push(columns[0]);
        }
//        alert("Data : "+JSON.stringify(barData));
    var allData=[];
    var allDataMap={};
    allDataMap[parentColumns[0]]="All";
    allDataMap[measureArray[0]]=chartData["chart1"]["GTValueList"][0];
    allData.push(allDataMap);
//    buildHalfPieDB("chart1","topDiv0", allData, parentColumns, measureArray, wid, hgt,hgt*0.75,0);
//    var pieRadius=0;
//    var maxValue=0;
//    var maxHgt=0;
//    for(var i=0; i< data["chart1"].length;i++){
//        var measure =[];
//        var barDataSlice=[];
//        barDataSlice.push(barData[i]);
//        measure.push(measureArray[0]);
//    var index=0;
//    index=parseInt(i)+1;
////    var gtValuesList=chartData["chart1"]["GTValueList"][i];
//    if(i==0){
//        maxValue=barDataSlice[0][measure[0]];
//        maxHgt=$("#topDiv1").height();
//    }
//    var currValue=barDataSlice[0][measure[0]];
//    var hgt1=currValue/(maxValue/maxHgt);
////    alert(JSON.stringify(barDataSlice))
////           if(i==0){
////               buildHalfPieDB("chart1","topDiv"+index, barDataSlice, columns, measure, wid, hgt,hgt*0.75); 
////           }
////           else{
//            if(barData.length<=3){
//               buildHalfPieDB("chart1","topDiv"+index, barDataSlice, parentColumns, measure, wid, hgt, hgt1*0.75, i); 
//            }
//            else{
//               buildHalfPieDB("chart1","topDiv"+index, barDataSlice, parentColumns, measure, wid, hgt/2, hgt1*0.9, i); 
//            }
////           }
////        LiquidFilledGaugeDash("chart1", data, columns, measureArray[i],chWidth,chHeight,gtValuesList,"topDiv"+i);
//    
//    }
    var divId = [];
    for(var k in columns){
        divId.push("mainDiv"+(parseInt(k)+1));
    }
    chWidth = 500;
    chHeight = 230;
    for(var k in columns){
       var index=0;
       index=parseInt(k)+1;
       var barData = data["chart"+index];
       var measure =[];
       measure.push(measureArray[0]);
//     if(typeof chartData["chart1"]["isDashboardDefined"]!=='undefined' && chartData["chart1"]["isDashboardDefined"]==='Y'){
        var currViewBy=[];
        currViewBy.push(columns[k]);
//        var chartId="chart"+(parseInt(k)+2);
//        barData=data[chartId.toString().trim()];
////        alert("Data--"+JSON.stringify(data));
////        alert("Bar Data--"+JSON.stringify(barData));
//        buildBarAdvance("chart"+index,divId[k], barData, currViewBy, measure, chWidth, chHeight);
//     }
//     else{
//        alert(JSON.stringify(data));
//         var barData = data["chart1"];
    if(k==columns.length-1 && k%2==0){
        chWidth = 1000;
        chHeight = 230;
    }
     buildBarAdvance("chart1",divId[k], barData, currViewBy, measure, chWidth, chHeight);
    }
//    }
    
}

function createTimeDBLayout(div,measure,columns, data) {
    var chartData=JSON.parse(parent.$("#chartData").val());
    var layoutHtml="";
    layoutHtml += "<div id='mainLayout' style='width=100%;'>";
    layoutHtml += "<div id='selectMeasureDiv' style='height:30px;width:100%;'>";
    layoutHtml += "<select id='selectMeasure' style='float:right;font-size:9pt' onchange='changeMeasureArray(this.value)' name='Dashname'>";
    var measures=chartData["chart1"]["meassures"];
    for(var i in measures){
        if(i==0){
            layoutHtml += "<option value='"+i+"' selected>"+measures[i]+"</option>";
        }
        else{
            layoutHtml += "<option value='"+i+"'>"+measures[i]+"</option>";
        }
    }
    layoutHtml += "</select>";
    layoutHtml += "</div>";
    layoutHtml += "<div id='timeDimDiv' style='margin-left:7px;width:100%;'>";
    var GTValueList=chartData["chart1"]["GTValueList"];
    if(typeof chartData["chart1"]["rounding"]!=="undefined"){
        yAxisRounding=chartData["chart1"]["rounding"];
    }else{
       yAxisRounding=1;
    }
    if(typeof chartData["chart1"]["yAxisFormat"]!=="undefined"){
        yAxisFormat=chartData["chart1"]["yAxisFormat"];
    }else{
       yAxisFormat="Auto";
    }
    var timeDimLabels=[];
    timeDimLabels.push("Month Till Date");
    timeDimLabels.push("Prior MTD");
    timeDimLabels.push("Qtr Till Date");
    timeDimLabels.push("Prior QTD"); 
    timeDimLabels.push("Year Till Date");
    for(var i=0;i<5;i++){
        if(timeDimLabels[i]===timeDBDrill){
            layoutHtml += "<div id='timeDimDiv"+i+"' onclick='getTrendDril(\""+timeDimLabels[i]+"\")'  style='border-radius:10px;margin-right:1px;display: table-cell;cursor: pointer;vertical-align: middle;background-color:"+drillShade+";height:60px;width:"+100/5.1+"%;float:left;>";
        }
        else{
        layoutHtml += "<div id='timeDimDiv"+i+"' onclick='getTrendDril(\""+timeDimLabels[i]+"\")'  style='border-radius:10px;margin-right:1px;display: table-cell;cursor: pointer;vertical-align: middle;background-color:#7B7B7B;height:60px;width:"+100/5.1+"%;float:left;>";
        }
        layoutHtml += "<table style='width:100%;text-align: center;border-bottom: solid 1px #ffffff;'>";
        layoutHtml += "<tr>";
        layoutHtml += "<td><span style='font-size:10pt;color:white;'>"+timeDimLabels[i];
        layoutHtml += "</span></td>";
        layoutHtml += "</tr>";
        layoutHtml += "</table>";
        layoutHtml += "<table style='width:100%;text-align: center;'>";
        layoutHtml += "<tr>";
        if(yAxisRounding>0){
            layoutHtml += "<td><span style='font-size:22pt;color:white;'>"+numberFormat(GTValueList[i], yAxisFormat, yAxisRounding, "chart1");
        }
        else{
            layoutHtml += "<td><span style='font-size:22pt;color:white;'>"+addCurrencyType(div, getMeasureId(measures[i])) + addCommas(numberFormat(GTValueList[i], yAxisFormat, yAxisRounding, "chart1"));
        }
        layoutHtml += "</span></td>";
        layoutHtml += "</tr>";
        layoutHtml += "</table>";
        layoutHtml += "</div>";
    }
    layoutHtml += "</div>";
    layoutHtml += "<div id='bottomDiv' style='width:100%;'>";
    
      for(var i in columns){
          
      if(i%2 ==0){
          if(i==columns.length-1){
            layoutHtml += "<div id='mainDiv"+(parseInt(i)+1)+"' style='width:100%;float:left;border-bottom: dotted 1px #808080;'>";
          }
            layoutHtml += "<div id='mainDiv"+(parseInt(i)+1)+"' style='width:50%;float:left;border-bottom: dotted 1px #808080;'>";
    layoutHtml += "</div>";
      }else{
          
    layoutHtml += "<div id='mainDiv"+(parseInt(i)+1)+"' style='width:50%;float:right;border-bottom: dotted 1px #808080;'>";
    layoutHtml += "</div>";
      }
      }
//    layoutHtml += "<div id='mainDiv2' style='width:49.5%;border: dotted 1px #808080;float:right'>";
//    layoutHtml += "</div>";
//    
//    layoutHtml += "</div>";
//    
//    layoutHtml += "<div style='width=100%;border: dotted 1px #808080;'>";
//    
//    layoutHtml += "<div id='mainDiv3' style='width:49.5%;border: dotted 1px #808080;float:left'>";
//    layoutHtml += "</div>";
//    
//    layoutHtml += "<div id='mainDiv4' style='width:49.5%;border: dotted 1px #808080;float:right'>";
//    layoutHtml += "</div>";
//    
    layoutHtml += "</div>";
    
    
    layoutHtml += "</div>";
    return layoutHtml;
   
}
function buildWorldMap(divID, data, columns, measureArray){
  $("#viewMeasureBlock").css({'display':'none'});
   var fun = "buildworldDrillMap(this.id,\""+divID+"\")";
//  var fun = buildworldDrillMap(divID, data, columns, measureArray,d.properties.name)
//  columns = [];
//   measureArray = [];
//   columns.push("Country Desc");
//   measureArray.push("Gross Sales");
//  data = [
//    {
//        "Country Desc": "France",
//        "Gross Sales": "1000"
//    },
//    {
//        "Country Desc": "Switzerland",
//        "Gross Sales": "900"
//    },
//    {
//        "Country Desc": "India",
//        "Gross Sales": "1500"
//    },
//    {
//        "Country Desc": "Canada",
//        "Gross Sales": "1200"
//    },
//    {
//        "Country Desc": "Pakistan",
//        "Gross Sales": "2000"
//    },
//    {
//        "Country Desc": "Australia",
//        "Gross Sales": "2500"
//    },
//    {
//        "Country Desc": "Japan",
//        "Gross Sales": "3000"
//    },
//    {
//        "Country Desc": "China",
//        "Gross Sales": "3500"
//    },
//    {
//        "Country Desc": "Germany",
//        "Gross Sales": "4000"
//    },
//    {
//        "Country Desc": "Russia",
//        "Gross Sales": "4500"
//    },
//    {
//        "Country Desc": "Greenland",
//        "Gross Sales": "5000"
//    },
//    {
//        "Country Desc": "Zimbabwe",
//        "Gross Sales": "5500"
//    },
//    {
//        "Country Desc": "Zimbabwe",
//        "Gross Sales": "6500"
//    },
//    
//    {
//        "Country Desc": "United States",
//        "Gross Sales": "6000"
//    }];
var chartMapData = data["chart1"];

//var chartMapData = data;
var colorMap={};
var color;
  
   
   var measure= measureArray[0];
//        var isShadedColor=true;
        //    $("#gradShade").val(color);
        //    shadingType="gradient";
        //    $("#shadeType").val(shadingType);
        var isShaded = parent.$("#isShaded").val();
       var shadeType =  parent.$("#shadeType").val();
      
       if(typeof shadeType =="undefined" || shadeType == "" ){
            color="#188adb";
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));    
       
       }else {
            color = d3.scale.category12();
       }
    //  --------------
       
//var shadingMeasure = "";
//            var conditionalMeasure = "";
//            var conditionalMap = {};
////            var isShadedColor = false;
//            var conditionalShading = false;
//---------
var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};
            var isShadedColor = false;
            var conditionalShading = false;
             if (parent.$("#isShaded").val() == "true" ) {
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalShading = true;
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        conditionalMeasure = conditionalMap["measure"];
                        conditionalMeasure = $("#conditionalMeasure").val();
                    } else if (parent.$("#shadeType").val() == "standard") {

                    } else {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];
                       
                        isShadedColor = true;
                        var measureData = [];
                        for (var i = 0; i < chartMapData.length; i++) {
                            measureData.push(chartMapData[i][shadingMeasure]);
                        }
                        color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range(["rgb(240,240,240)", map[map["measure"]]]);
                    }
                } else {
                    if (parent.isCustomColors) {
                        color = d3.scale.ordinal().range(parent.customColorList);
                    }
                }


    var maxMap = {};
    var minMap = {};
    for (var num = 0; num < measureArray.length; num++) {
        var measureData = [];
        for (var key in chartMapData) {
            measureData.push(chartMapData[key][measureArray[num]]);
        }
        maxMap[measureArray[num]] = Math.max.apply(Math, measureData);
        minMap[measureArray[num]] = Math.min.apply(Math, measureData);
    }
    parent.$("#maxMap").val(JSON.stringify(maxMap));
    parent.$("#minMap").val(JSON.stringify(minMap));
    
    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")) + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1"))+ "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }

    }
    function hide_details() {
        return tooltip.hideTooltip();
    }
    
    var width = $(window).width()-170;
    var height = $(window).height();
    var projection = d3.geo.mercator()
    //            .center([-20, 60])
                .scale(750).translate([width/2.5, height/1.8]);
    var path = d3.geo.path()
    .projection(projection);

    var svg = d3.select("#"+divID)
    .append("svg")
    .attr("width", width)
    .attr("height", height);
    
    var g = svg.append("g");
    
//    var colorMap = {};
    var ctxPath=$("#ctxpath").val();
    
    d3.json(ctxPath+"/JS/world-topo-min.json", function(error, world) {
        g.selectAll("path")
        .data(topojson.feature(world, world.objects.countries).features)
        .enter()
        .append("path")
        .attr("d", path)// for(var i=0;i<Object.keys(data).length;i++){if(if)}\
      
                    .style("fill", function(d, i) {
                        
                        for (var j = 0; j < chartMapData.length; j++) {
                            var colorShad;
                            if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                if (isShadedColor) {
                                    var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                                if (isShadedColor) {
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                        }
                        return "white";
                    })
                    .style("stroke", "rgb(70,70,70)").on("mouseover", function(d, i) {
                show_details(d);
            })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    })
                    .attr("id",function(d, i) {
                    return  d.properties.name  ;
                    })
					.on("click", function(d, i) {
                                           buildDrillMapFun(this.id,"chart1",divID)
				//	buildworldDrillMap(this.id,divID, data, columns, measureArray);
                    });
                //    .attr("onclick",fun);
//            parent.$("#colorMap").val(JSON.stringify(colorMap));
            
           var html = "<div id='legendsScale' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-41%;margin-left:87%;'></div>";
                    $('#'+divID).append(html);
                    
//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legendsScale").append("svg")
                            .attr("width", "100%")
                            .attr("height", "100%");
                            
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        var selectedMeasure = conditionalMap["measure"];
                        var selectedMeasure = $("#conditionalMeasure").val();
                        var keys = Object.keys(conditionalMap);
                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(selectedMeasure);
//                        for (var no = 0; no < (keys.length - 1); no++) {
                        for (var no = 0; no < (keys.length ); no++) {
                            var legend = svg1.append("g")
                                    .attr("transform", function() {
                                        return "translate(10," + ((no * 20) + 20) + ")";
                                    });
                            legend.append("rect")
                                    .attr("width", 24)
                                    .attr("height", 14)
                                    .attr("cy", 9)
                                    .attr("stroke", "black")
                                    .attr("stroke-width", ".5")
                                    .style("fill", conditionalMap[no]["color"]);
//                                        .attr("r", 5);
                            legend.append("text")
                                    .attr("x", 28)
                                    .attr("y", 9)
                                    .attr("dy", ".35em")
                                    .text(function() {
                                        if (conditionalMap[no]["operator"] === "<>") {
                                            return conditionalMap[no]["limit1"] + conditionalMap[no]["operator"] + conditionalMap[no]["limit2"];
                                        } else {
                                            return conditionalMap[no]["operator"] + conditionalMap[no]["limit1"];
                                        }
                                    });
//                                        .attr("fill", conditionalMap[no]["color"]);
                        }
                        $("#legendsScale").css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else  {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legendsScale").height() - 10;
                        var shadingMeasure = colorMap["measure"];
                     
                        var max = maximumValue(chartMapData, shadingMeasure);
                        var min = minimumValue(chartMapData, shadingMeasure);
                       
                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend1")
                                .attr("x1", "0%")
                                .attr("y1", "30%")
                                .attr("x2", "50%")
                                .attr("y2", "30%")
                                .attr("spreadMethod", "pad")
                                .attr("gradientTransform", "rotate(90)");

                        gradient.append("svg:stop")
                                .attr("offset", "0%")
                                .attr("stop-color", colorMap[shadingMeasure])
                                .attr("stop-opacity", 1);
                        gradient.append("svg:stop")
                                .attr("offset", "100%")
                                .attr("stop-color", "rgb(230,230,230)")
                                .attr("stop-opacity", 1);

                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(shadingMeasure);
                        svg1.append("g").append("rect")
                                .attr("width", 10)
                                .attr("height", "90%")
                                .attr("fill", "url(#gradientWrdMapLegend1)")
                                .attr("x", 0)
                                .attr("y", 15);
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", height)
                                .attr("dy", ".35em").text(addCommas(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(addCommas(max));


                        $("#legendsScale").css("width", "14%");
                    } 
            
    });
    var zoom = d3.behavior.zoom()
            .on("zoom", function() {
                g.attr("transform", "translate(" +
                        d3.event.translate.join(",") + ")scale(" + d3.event.scale + ")");
                g.selectAll("path")
                        .attr("d", path.projection(projection));
            });
    svg.call(zoom);
    d3.select(self.frameElement).style("height", height + "px");
}

function buildWorldCityMap(divID, data, columns, measureArray){
    $("#viewMeasureBlock").css({'display':'none'});

        var chartMapData = data["chart1"];
//    var color = d3.scale.category12();
   // add gradient
    var color;
   var measure= measureArray[0];
//        var isShadedColor=true;
        //    $("#gradShade").val(color);
        //    shadingType="gradient";
        //    $("#shadeType").val(shadingType);
//        $("#isShaded").val("true");
//        $("#shadeType").val("gradient");
//      
        var colorMap={};
//        colorMap[measure]=color;
//        colorMap["measure"]=measure;
//        parent.$("#measureColor").val(JSON.stringify(colorMap));

 var isShaded = parent.$("#isShaded").val();
       var shadeType =  parent.$("#shadeType").val();
      
       if(typeof shadeType =="undefined" || shadeType == "" ){
            color="#188adb";
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));    
       
       }else {
            color = d3.scale.category12();
       }
var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};
//            var isShadedColor = false;
            var conditionalShading = false;
             if (parent.$("#isShaded").val() == "true" || colorFlag) {
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalShading = true;
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        conditionalMeasure = conditionalMap["measure"];
                        conditionalMeasure = $("#conditionalMeasure").val();
                    } else if (parent.$("#shadeType").val() == "standard") {

                    } else {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];
                       
                        isShadedColor = true;
                        var measureData = [];
                        for (var i = 0; i < chartMapData.length; i++) {
                            measureData.push(chartMapData[i][shadingMeasure]);
                        }
                        color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range(["rgb(221,221,221)", map[map["measure"]]]);
                    }
                } else {
                    if (parent.isCustomColors) {
                        color = d3.scale.ordinal().range(parent.customColorList);
                    }
                }
    var maxMap = {};
    var minMap = {};
    for (var num = 0; num < measureArray.length; num++) {
        var measureData = [];
        for (var key in chartMapData) {
            measureData.push(chartMapData[key][measureArray[num]]);
        }
        maxMap[measureArray[num]] = Math.max.apply(Math, measureData);
        minMap[measureArray[num]] = Math.min.apply(Math, measureData);
    }
    parent.$("#maxMap").val(JSON.stringify(maxMap));
    parent.$("#minMap").val(JSON.stringify(minMap));
    
    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")) + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")) + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }

    }
    function hide_details() {
        return tooltip.hideTooltip();
    }
    
    var width = $(window).width()-170;
    var height = $(window).height();
    var projection = d3.geo.mercator()
    //            .center([-20, 60])
     .scale(750).translate([width/2.5, height/1.8]);
    var path = d3.geo.path()
    .projection(projection);

    var svg = d3.select("#"+divID)
    .append("svg")
    .attr("width", width)
    .attr("height", height);
    
    var g = svg.append("g");
    
    var colorMap = {};
    var ctxPath=$("#ctxpath").val();
    
    d3.json(ctxPath+"/JS/world-topo-min.json", function(error, world) {
    
        g.selectAll("path")
        .data(topojson.feature(world, world.objects.countries).features)
        //          .geometries)
        .enter()
        .append("path")
        .attr("d", path)
        .style("fill", function(d, i) {
            return "white";
        })
        .style("stroke", "rgb(70,70,70)");
        parent.$("#colorMap").val(JSON.stringify(colorMap));
    })
        
    setTimeout(function(){
        d3.json(ctxPath+"/JS/WorldCities.json", function(error, world) {
        g.selectAll("circle")
        .data(world)
        .enter()
//        .append("a")
//        .attr("xlink:href", function(d) {
//            return "https://www.google.com/search?q="+data[columns[0]];
//        }
//        )
        .append("circle")
        .attr("cx", function(d) {
//            alert(JSON.stringify(d))
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[0];
    })
        .attr("cy", function(d) {
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[1];
        })
        .attr("r", function(d,i){
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
                       if (typeof (d.properties.name) != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                     return 6;
                        }
                    }
                    return 0;
        })
        .style("fill", function(d,i){
             for (var j = 0; j < chartMapData.length; j++) {
                            var colorShad;
                            if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                if (isShadedColor) {
                                    var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                                if (isShadedColor) {
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                        }
                        return "white";
        }).on("mouseover", function(d, i) {
                show_details(d);
            })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    });
        
         
           var html = "<div id='legendsScale' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-41%;margin-left:87%;'></div>";
                    $('#'+divID).append(html);
                    
//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legendsScale").append("svg")
                            .attr("width", "100%")
                            .attr("height", "100%");
                            
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        var selectedMeasure = conditionalMap["measure"];
                        var selectedMeasure = $("#conditionalMeasure").val();
                        var keys = Object.keys(conditionalMap);
                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(selectedMeasure);
//                        for (var no = 0; no < (keys.length - 1); no++) {
                        for (var no = 0; no < (keys.length ); no++) {
                            var legend = svg1.append("g")
                                    .attr("transform", function() {
                                        return "translate(10," + ((no * 20) + 20) + ")";
                                    });
                            legend.append("rect")
                                    .attr("width", 24)
                                    .attr("height", 14)
                                    .attr("cy", 9)
                                    .attr("stroke", "black")
                                    .attr("stroke-width", ".5")
                                    .style("fill", conditionalMap[no]["color"]);
//                                        .attr("r", 5);
                            legend.append("text")
                                    .attr("x", 28)
                                    .attr("y", 9)
                                    .attr("dy", ".35em")
                                    .text(function() {
                                        if (conditionalMap[no]["operator"] === "<>") {
                                            return conditionalMap[no]["limit1"] + conditionalMap[no]["operator"] + conditionalMap[no]["limit2"];
                                        } else {
                                            return conditionalMap[no]["operator"] + conditionalMap[no]["limit1"];
                                        }
                                    });
//                                        .attr("fill", conditionalMap[no]["color"]);
                        }
                        $("#legendsScale").css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else  {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legendsScale").height() - 10;
                        var shadingMeasure = colorMap["measure"];
                     
                        var max = maximumValue(chartMapData, shadingMeasure);
                        var min = minimumValue(chartMapData, shadingMeasure);
                       
                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend1")
                                .attr("x1", "0%")
                                .attr("y1", "30%")
                                .attr("x2", "50%")
                                .attr("y2", "30%")
                                .attr("spreadMethod", "pad")
                                .attr("gradientTransform", "rotate(90)");

                        gradient.append("svg:stop")
                                .attr("offset", "0%")
                                .attr("stop-color", colorMap[shadingMeasure])
                                .attr("stop-opacity", 1);
                        gradient.append("svg:stop")
                                .attr("offset", "100%")
                                .attr("stop-color", "rgb(230,230,230)")
                                .attr("stop-opacity", 1);

                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(shadingMeasure);
                        svg1.append("g").append("rect")
                                .attr("width", 10)
                                .attr("height", "90%")
                                .attr("fill", "url(#gradientWrdMapLegend1)")
                                .attr("x", 0)
                                .attr("y", 15);
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", height)
                                .attr("dy", ".35em").text(addCommas(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(addCommas(max));


                        $("#legendsScale").css("width", "14%");
                    } 
        
       });
    },2000);
         

    var zoom = d3.behavior.zoom()
    .on("zoom", function() {
        g.attr("transform", "translate(" +
            d3.event.translate.join(",") + ")scale(" + d3.event.scale + ")");
        g.selectAll("path")
        .attr("d", path.projection(projection));
    });
    svg.call(zoom);
    d3.select(self.frameElement).style("height", height + "px");
}



function australiaMap(div,data,columns,measureArray,divWid,divHgt){
      awesomeness = {"New South Wales":8,
    "Victoria":6,
    "Queensland":3,
    "South Australia":7,
    "Western Australia":4,
    "Tasmania": 6,
    "Northern Territory": 7, // + arnhem land, cumulo nimbus clouds over Darwin, - mosquitoes
    "Other Territories":5 // I'm sure they're ok
    };
        var color = d3.scale.category12();
var chart12 = data["chart1"];
var chartMapData = data["chart1"];
var chartData = JSON.parse(parent.$("#chartData").val());
 columns = chartData["chart1"]["viewBys"];
var mapColumns = chartData["chart1"]["viewBys"];
var colIds = chartData["chart1"]["viewIds"];
measureArray = chartData["chart1"]["meassures"];
    var w = 560,
    h = 500;
    var z = d3.scale.category10();
    var fill = d3.scale.log()
    .domain(d3.extent(d3.values(awesomeness)))
    .range(["brown", "steelblue"]);

    var projection = d3.geo.azimuthal()
    .origin([135, -26])
    .translate([250,180])
    .scale(700);
    var path = d3.geo.path()
    .projection(projection);
    var svg = d3.select("#"+div).append("svg")
    .attr("width", w)
    .attr("height", h);

    var states = svg.append("g")
    .attr("id", "states");
    d3.json("JS/australia.json", function(collection) {

    states.selectAll("path")
    .data(collection.features)
    .enter().append("path")
    .attr("fill", function(d) {
    return fill(awesomeness[(d.properties["STATE_NAME"])]);
    })
//.on("mouseover", function(d, i) {
//                                  d3.select(this).attr("stroke", "grey");
//                var content;
//                var title;
//                title = columns;
//
//                var currName = (d.id).toUpperCase();
//                for (var i = 0; i < mapColumns.length; i++) {
//                    content = "<span class=\"name\">" + mapColumns[i] + ":</span><span class=\"value\"> " + currName + "</span><br/>";
//                    break;
//                }
//
//                for (var i = 0; i < measureArray.length; i++) {
//                    var msrData;
//                    for (var l = 0; l < chartMapData.length; l++) {
//                        if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && chartMapData[l][mapColumns[0]].toLowerCase() == "chhattisgarh")) {
//                            for (t = 0; t < measureArray.length; t++) {
//                                if (parent.isFormatedMeasure) {
//                                    msrData = numberFormat(chartMapData[l][measureArray[t]], round, precition,div)
//                                }
//                                else {
//                                    msrData = addCommas(chartMapData[l][measureArray[t]]);
//                                }
//                                content += "<span class=\"name\">" + measureArray[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
//                            }
//                            return tooltip.showTooltip(content, d3.event);
//                        }
//                        else
//                        {
//                            msrData = "";
//                        }
//
//
//                    }
//                    //        content += "<span class=\"name\">"+measureArray[i]+":</span><span class=\"value\"> " + msrData + "</span><br/>";
//
//                }
//})
//                            .on("mouseout", function(d, i) {
//                                hide_details(d, i, this);
//                            })
//.attr("color_value", colorVal)
//    .on("mouseover", function(d, i) {
//    show_detailsPie(d, columns, measureArray, this, i)
//    })
//    .on("mouseout", function(d, i) {
//    hide_details(d, i, this);
//    })
    .attr("d", path);
    var tabDiv="chart_2";

if(typeof $("#tabDiv").val()!="undefined" && $("#tabDiv").val()!=""){
   tabDiv =$("#tabDiv").val();
}
//function

var htmlDiv = "<div id='pieWithMap' style = 'height:auto;overflow-y:auto;margin-top:0px;margin-right:20px;float:right;width:45%;'></div>";
$("#"+div).append(htmlDiv);
pieWithTable1=function(id){pieWithTable(id);}
pieWithTable(tabDiv);
    });

function pieWithTable(divId){
    $("#tabDiv").val(divId);
    $("#pieWithMap").html("");
    var tabDivVal = "chart"+divId.split("_")[1];
    var pieDivId = "chartAd"+divId.split("_")[1];
if(data[tabDivVal]!= "undefined"){
    chart12 = data[tabDivVal];
    columns = chartData[tabDivVal]["viewBys"];
measureArray = chartData[tabDivVal]["meassures"];
}
var daatPie=[];
for(i=0;i<(chart12.length < 10 ? chart12.length : 10);i++){
   daatPie.push(chart12[i]);
}
var allColumns = [];
for(var ch in chartData){
   allColumns.push(chartData[ch]["viewBys"][0]);
}
var htmlstr="";
htmlstr += "<div style = 'height:auto;overflow-y:auto;margin-top:20px;float:right;width:98%;'>";
                htmlstr += "<table style='float:left;'><tr>"
                for (var i = 0; i < allColumns.length; i++) {
                    if (columns[0] == allColumns[i]) {
                        htmlstr += "<td class='mapViewBy1' id='chart_" + parseInt(i+1) + "' onclick='pieWithTable1(this.id)'>" + allColumns[i] + "</td>"
                    }
                    else {
                        htmlstr += "<td class='mapViewBys' id='chart_" + parseInt(i+1) + "' onclick='pieWithTable1(this.id)'>" + allColumns[i] + "</td>"
                    }
                }
                htmlstr += "</tr></table>";;
htmlstr +="<table style='width:100%'><tr><td>"
htmlstr+="<div id='"+pieDivId+"'><div>"
htmlstr+="</tr></td>"
htmlstr+="<tr><td><div id = 'mapTable' style ='overflow-y:auto;height:250px;margin-right:10px'>"
htmlstr += "<table style='width:100%' class = 'table table-condensed table-stripped table-bordered' style = 'float:right'>";
htmlstr += "<tr class=\"thClass\" align='center'>";
//for(var i in columns){
htmlstr += "<th >"+columns[0]+"</th>";
//}
for(var i in measureArray){
htmlstr += "<th>"+measureArray[i]+"</th>";
}
htmlstr += "</tr>";

 var kpicount = 0;
var avgData = [];
for (var j in chart12) {

avgData.push(chart12[j]);
kpicount++;
if (kpicount % 2 == 1) {
 htmlstr += "<tr class=\"tdClass1\">";
   htmlstr += "<td class='tdClass' align='left'  id='' onmouseover='tableHover1(this.id)' onmouseout='hoverOut1(this.id)'>" + chart12[j][columns[0]] + "</td>";
for (var n = 0; n < measureArray.length; n++) {
    var measVal = parseFloat(chart12[j][measureArray[n]]).toFixed(2);
    htmlstr += "<td class='tdClass' align='right'>" + addCurrencyType(div, getMeasureId(measureArray[n])) + addCommas(measVal) + "</td>";
}
htmlstr += "</tr>";
}
else {
 htmlstr += "<tr class=\"tdClass1\">";
for (var k in columns) {
   htmlstr += "<td class='tdClass' align='left' onclick='getState(this.id)' id='" + chart12[j][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' onmouseover='tableHover1(this.id)' onmouseout='hoverOut1(this.id)'>" + chart12[j][columns[0]] + "</td>";
}
for (var n = 0; n < measureArray.length; n++) {
    var measVal = parseFloat(chart12[j][measureArray[n]]).toFixed(2);
    htmlstr += "<td class='tdClass' align='right'>" + addCurrencyType(div, getMeasureId(measureArray[n])) + addCommas(measVal) + "</td>";
}
htmlstr += "</tr>";
}
}
htmlstr += "</table></div></td></tr></table>";
//htmlstr += "</div>";

$("#pieWithMap").html(htmlstr);
//  if(screen.width<1000){
//
//buildPieMap(tabDivVal,pieDivId,daatPie, columns, measureArray,divWid*.4,divHgt*.3);
//  }else{
//buildPieMap(tabDivVal,pieDivId,daatPie, columns, measureArray,divWid*.5,divHgt*.5);
//  }
}
}
function buildworldDrillMap(name,divID, data, columns, measureArray){
var name = name.split(":")[0]
  var chartData = JSON.parse($("#chartData").val());
$("#"+divID).html("");
var htmlvar = "";
htmlvar +="<div style='width:100%;height:300px'>";
htmlvar +="<div id='worldMapDiv' style='width:49%;height:300px;border-style: solid;border-width: 1px;float:left' ></div>";
htmlvar +="<div id='worldMapDrilledDiv' style='width:50%;height:300px;border-style: solid;border-width: 1px;float:right' ></div>";

htmlvar +="</div>";
htmlvar +="<div id='worldMapTableDiv' style='width:100%;height:300px;margin-top:310px;border-style: solid;border-width: 1px; margin-top:1%' ></div>";
$("#"+divID).html(htmlvar);
var width = 550;
var height = 300;
var tablewidth = 800;
var tableheight = 300;
var currdata = data["chart2"]
var mapData =  {}

buildWorldMapDrill("worldMapDiv", data, columns, measureArray,width,height,name)
if(name.toUpperCase()=="INDIA"){

buildWorldIndiaMap("worldMapDrilledDiv", data, chartData["chart2"]["viewBys"], chartData["chart2"]["meassures"],width,height,"chart2")
}else if(name.toUpperCase()=="UNITED STATES"){

buildUSWorldCityMap("worldMapDrilledDiv", data, chartData["chart2"]["viewBys"], chartData["chart2"]["meassures"],width,height,"chart2")
}else if(name.toUpperCase()=="AUSTRALIA"){
buildWorldAustraliaMap("worldMapDrilledDiv", data, chartData["chart2"]["viewBys"], chartData["chart2"]["meassures"],width,height,"chart2")

}else if(name.toUpperCase()=="CHINA"){
buildChinaMap("worldMapDrilledDiv", data, chartData["chart2"]["viewBys"], chartData["chart2"]["meassures"],width,height,"chart2")
}
buildWorldMapTable("chart2",data, currdata, chartData["chart2"]["viewBys"], chartData["chart2"]["meassures"], tablewidth, tableheight,"worldMapTableDiv")
}

function buildWorldMapDrill(divID, data, columns, measureArray,width,height,drillCountryName){
  $("#viewMeasureBlock").css({'display':'none'});
   var fun = "buildDrillMapFun(this.id,\"chart1\",divID)";
//   var fun = "buildworldDrillMap(this.id,\""+divID+"\")";
//  var fun = buildworldDrillMap(divID, data, columns, measureArray,d.properties.name)
//  columns = [];
//   measureArray = [];
//   columns.push("Country Desc");
//   measureArray.push("Gross Sales");
var drillFunData = data
var chartMapData = data["chart1"];

//var chartMapData = data;
var colorMap={};
var color;


   var measure= measureArray[0];
//        var isShadedColor=true;
        //    $("#gradShade").val(color);
        //    shadingType="gradient";
        //    $("#shadeType").val(shadingType);
        var isShaded = parent.$("#isShaded").val();
       var shadeType =  parent.$("#shadeType").val();

       if(typeof shadeType =="undefined" || shadeType == "" ){
            color="#188adb";
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));

       }else {
            color = d3.scale.category12();
       }
    //  --------------

//var shadingMeasure = "";
//            var conditionalMeasure = "";
//            var conditionalMap = {};
////            var isShadedColor = false;
//            var conditionalShading = false;
//---------
var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};
            var isShadedColor = false;
            var conditionalShading = false;
             if (parent.$("#isShaded").val() == "true" ) {
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalShading = true;
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        conditionalMeasure = conditionalMap["measure"];
                        conditionalMeasure = $("#conditionalMeasure").val();
                    } else if (parent.$("#shadeType").val() == "standard") {

                    } else {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];

                        isShadedColor = true;
                        var measureData = [];
                        for (var i = 0; i < chartMapData.length; i++) {
                            measureData.push(chartMapData[i][shadingMeasure]);
                        }
                        color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range(["rgb(240,240,240)", map[map["measure"]]]);
                    }
                } else {
                    if (parent.isCustomColors) {
                        color = d3.scale.ordinal().range(parent.customColorList);
                    }
                }


    var maxMap = {};
    var minMap = {};
    for (var num = 0; num < measureArray.length; num++) {
        var measureData = [];
        for (var key in chartMapData) {
            measureData.push(chartMapData[key][measureArray[num]]);
        }
        maxMap[measureArray[num]] = Math.max.apply(Math, measureData);
        minMap[measureArray[num]] = Math.min.apply(Math, measureData);
    }
    parent.$("#maxMap").val(JSON.stringify(maxMap));
    parent.$("#minMap").val(JSON.stringify(minMap));

    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")) + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1"))+ "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }

    }
    function hide_details() {
        return tooltip.hideTooltip();
    }

    
    var projection = d3.geo.mercator()
    //            .center([-20, 60])
                .scale(450).translate([width/2.3, height/1.4]);
    var path = d3.geo.path()
    .projection(projection);

    var svg = d3.select("#"+divID)
    .append("svg")
    .attr("width", width)
    .attr("height", height);

    var g = svg.append("g");

//    var colorMap = {};
    var ctxPath=$("#ctxpath").val();

    d3.json(ctxPath+"/JS/world-topo-min.json", function(error, world) {
        g.selectAll("path")
        .data(topojson.feature(world, world.objects.countries).features)
        .enter()
        .append("path")
        .attr("d", path)// for(var i=0;i<Object.keys(data).length;i++){if(if)}\

                    .style("fill", function(d, i) {

                        for (var j = 0; j < chartMapData.length; j++) {
                            var colorShad;
                            if(drillCountryName.toLowerCase()=== (d.properties.name).toLowerCase()){
                                return drillShade;
                            }
                           else if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                if (isShadedColor) {
                                    var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                                if (isShadedColor) {
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                        }
                        return "white";
                    })
                    .style("stroke", "rgb(70,70,70)").on("mouseover", function(d, i) {
                show_details(d);
            })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    })
                    .attr("id",function(d, i) {
                    return  d.properties.name+":"+d.properties.name  ;
                    })
				//	.on("click", function(d, i) {

				//	buildworldDrillMap(this.id,"Hchart1", drillFunData, columns, measureArray);
				//	});
					.on("click", function(d, i) {
                                           buildDrillMapFun(this.id,"chart1","Hchart1")
				//	buildworldDrillMap(this.id,divID, data, columns, measureArray);
					});
                //    .attr("onclick",fun);
//            parent.$("#colorMap").val(JSON.stringify(colorMap));

           var html = "<div id='legendsScale' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-41%;margin-left:87%;'></div>";
                    $('#'+divID).append(html);

//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legendsScale").append("svg")
                            .attr("width", "100%")
                            .attr("height", "100%");

                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        var selectedMeasure = conditionalMap["measure"];
                        var selectedMeasure = $("#conditionalMeasure").val();
                        var keys = Object.keys(conditionalMap);
                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(selectedMeasure);
//                        for (var no = 0; no < (keys.length - 1); no++) {
                        for (var no = 0; no < (keys.length ); no++) {
                            var legend = svg1.append("g")
                                    .attr("transform", function() {
                                        return "translate(10," + ((no * 20) + 20) + ")";
                                    });
                            legend.append("rect")
                                    .attr("width", 24)
                                    .attr("height", 14)
                                    .attr("cy", 9)
                                    .attr("stroke", "black")
                                    .attr("stroke-width", ".5")
                                    .style("fill", conditionalMap[no]["color"]);
//                                        .attr("r", 5);
                            legend.append("text")
                                    .attr("x", 28)
                                    .attr("y", 9)
                                    .attr("dy", ".35em")
                                    .text(function() {
                                        if (conditionalMap[no]["operator"] === "<>") {
                                            return conditionalMap[no]["limit1"] + conditionalMap[no]["operator"] + conditionalMap[no]["limit2"];
                                        } else {
                                            return conditionalMap[no]["operator"] + conditionalMap[no]["limit1"];
                                        }
                                    });
//                                        .attr("fill", conditionalMap[no]["color"]);
                        }
                        $("#legendsScale").css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else  {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legendsScale").height() - 10;
                        var shadingMeasure = colorMap["measure"];

                        var max = maximumValue(chartMapData, shadingMeasure);
                        var min = minimumValue(chartMapData, shadingMeasure);

                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend1")
                                .attr("x1", "0%")
                                .attr("y1", "30%")
                                .attr("x2", "50%")
                                .attr("y2", "30%")
                                .attr("spreadMethod", "pad")
                                .attr("gradientTransform", "rotate(90)");

                        gradient.append("svg:stop")
                                .attr("offset", "0%")
                                .attr("stop-color", colorMap[shadingMeasure])
                                .attr("stop-opacity", 1);
                        gradient.append("svg:stop")
                                .attr("offset", "100%")
                                .attr("stop-color", "rgb(230,230,230)")
                                .attr("stop-opacity", 1);

                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(shadingMeasure);
                        svg1.append("g").append("rect")
                                .attr("width", 10)
                                .attr("height", "90%")
                                .attr("fill", "url(#gradientWrdMapLegend1)")
                                .attr("x", 0)
                                .attr("y", 15);
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", height)
                                .attr("dy", ".35em").text(addCommas(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(addCommas(max));


                        $("#legendsScale").css("width", "14%");
                    }

    });

//    var zoom = d3.behavior.zoom()
//            .on("zoom", function() {
//                g.attr("transform", "translate(" +
//                        d3.event.translate.join(",") + ")scale(" + d3.event.scale + ")");
//                g.selectAll("path")
//                        .attr("d", path.projection(projection));
//            });
//    svg.call(zoom);
    d3.select(self.frameElement).style("height", height + "px");
//var buildworldDrillMap =  function(id,div) {
//       alert(id+"::"+div)
//    }
}

function buildWorldIndiaMap(div, data, columns, measureArray,divWid,divHgt,drillchartID){


        d3.selection.prototype.dblTap = function(callback) {
      var last = 0;
      return this.each(function() {
        d3.select(this).on("touchstart", function(e) {
            if ((d3.event.timeStamp - last) < 500) {
//                alert("touchstart id: "+this.id)
              return callback(e,this.id);
            }
//            alert("touchstart1111 id: "+this.id)
            last = d3.event.timeStamp;
        });
      });
    } 
      
// var fun = "drillWithinchart(this.id,\"chart1\")";
  //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
            fun = "drillWithinchart(this.id,\"chart2\")";
	}
        function drillFunct(id1){
//            alert("hello");
            drillWithinchart(id1,chart1);
	}

  var data1 = [];
  data1 = data["chart2"];
     var color = d3.scale.category12();
var chart12 = data["chart2"];
var chartMapData = data["chart2"];
var chartData = JSON.parse(parent.$("#chartData").val());
columns = chartData["chart2"]["viewBys"];
mapColumns = chartData["chart2"]["viewBys"];
measureArray = chartData["chart2"]["meassures"];
var colIds = chartData["chart2"]["viewIds"];
if(screen.width<1000){
var w = divWid*2;}
else{
   w = divWid;
}
var h = 550;
var proj = d3.geo.mercator();
var path = d3.geo.path().projection(proj);
var t = proj.translate(); // the projection's default translation
var s = proj.scale() // the projection's default scale


var map = d3.select("#"+div).append("svg:svg")
.attr("width", w)
.attr("height", h)
.style("margin-top","15px")
// .call(d3.behavior.zoom().on("zoom", redraw))
.call(initialize);
var indiaPlaces = map.append("svg:svg")
    .attr("id", "indiap");
var india = map.append("svg:g")
.attr("id", "india");
d3.json("JS/coordinate.json", function(error, data) {

 try{
   drawStates1()
 }
  catch(e){}


 drawIndiaPlaces1(data);

  });

function initialize() {
   
proj.scale(3000);
proj.translate([-400, 340]);

}


function drawStates1() {
        var str="features";
        d3.json("JS/states-tel.json", function (json) {
        indiaPlaces.selectAll("path")

.data(json.features)
.enter()
.append("path")
.attr("d", path)

.attr("fill", function(d,i){

return "white";
})
.attr("id",function(d,i){
        var currName = (d.id).toUpperCase();
for (var l = 0; l < chartMapData.length; l++) {
if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase()) {
    return chartMapData[l][mapColumns[0]]+":"+chartMapData[l][mapColumns[0]];
}
}
return "";
})
.attr("onclick", fun)
//Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
 });

 });

 }

function donothing(){}

function drawIndiaPlaces1(data) {
      subData=data;
        var min1=[];
//alert(donothing())
//alert(JSON.stringify(data1))
        for(var key in data1) {
//alert(JSON.stringify(data1[key][measureArray[0]]))
            min1.push(data1[key][measureArray[0]]);
        }

        var minRange=1;
        if(data1.length==1){
            minRange=12;
        }
       var min, max;
    min = minimumValue(chartMapData, measureArray[0]);
    max = maximumValue(chartMapData, measureArray[0]);
    var pointsize = d3.scale.linear()
            .domain([min, max])
            .range([6, 12]);
//        var fun = "drillWithinchart(this.id,\"chart1\")";

    //Added by shivam
	var fun="drilledWorldData(this.id,\""+div+"\")";

	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
	if(hasTouch){
		fun="";
        }
	else{
            fun = "drillWithinchart(this.id,\"chart1\")";
	}
	function drillFunct(id1){
//            alert("hi")
            drillWithinchart(id1,chart1);
	}


        var str="features";
        india.selectAll("path.place")
        .data(data)
        .enter().append("path")
        .attr("d", path.pointRadius(function(d,i) {
            var c1=(d.properties.name);

            for(var l=0;l<data1.length;l++){

                if(typeof c1!="undefined" && data1[l][mapColumns[0]].toLowerCase()==c1.toLowerCase()){

                    return (pointsize(data1[l][measureArray[0]]));
                }
            }
            return 0;
        }))


//                if(typeof c1!="undefined" &&  data1[l][mapColumns[0]].toLowerCase()==c1.toLowerCase()){
//
//                    return "place";
//                }
//            }
//            return "place1";
//        })
        .attr("id", function(d,j) {
            var c1=(d.properties.name);
            for(var l=0;l<data1.length;l++){
                if(typeof c1!="undefined" &&  data1[l][mapColumns[0]].toLowerCase()==c1.toLowerCase()){
                    return data1[l][mapColumns[0]]+ ":" + mapColumns[0]+":map";
                }
            }
            return c1;
        })
        .attr("fill", function(d,i){


var currName = (d["properties"]["name"]);


var colorShad;
var drilledvalue;

                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }

 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                    colorShad = drillShade;
                } else {
                  for (var l = 0; l < data1.length; l++) {

           //     alert(""+data1[l][mapColumns[0]])
if (currName.toLowerCase() == data1[l][mapColumns[0]].toLowerCase()) {
//     colorShad = color(i);

     return color(i);
}else{
    colorShad = "white";
}
}
   }


return colorShad;
})
        .attr("onclick", fun)
//Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	})
//        .attr("fill",function(d){
//            return "lightgrey";
//        })sss
//        .style("opacity",".7")
        .on("mouseover", function(d,i){
//            alert(JSON.stringify(d["properties"]["name"]))
            d3.select(this).attr("stroke", "grey");
                var content;
                var title;
                title = columns;

                var currName = (d["properties"]["name"]).toUpperCase();
                for (var i = 0; i < mapColumns.length; i++) {
                    content = "<span class=\"name\">" + mapColumns[i] + ":</span><span class=\"value\"> " + currName + "</span><br/>";
                    break;
                }

                for (var i = 0; i < measureArray.length; i++) {
                    var msrData;
                    for (var l = 0; l < data1.length; l++) {
                        if (currName.toLowerCase() == data1[l][mapColumns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && data1[l][mapColumns[0]].toLowerCase() == "chhattisgarh")) {
                            for (t = 0; t < measureArray.length; t++) {
                                if (parent.isFormatedMeasure) {
                                    msrData = numberFormat(data1[l][measureArray[t]], round, precition)
                                }
                                else {
                                    msrData = addCurrencyType(div, getMeasureId(measureArray[t])) + addCommas(data1[l][measureArray[t]]);
                                }
                                content += "<span class=\"name\">" + measureArray[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                            }
                            return tooltip.showTooltip(content, d3.event);
                        }
                        else
                        {
                            msrData = "";
                        }


                    }
                    //        content += "<span class=\"name\">"+measureArray[i]+":</span><span class=\"value\"> " + msrData + "</span><br/>";

                }
//            show_details(d,mapColumns,measureArray,this,i)
        })
        .on("mouseout", function(d,i){
            hide_details(d, i, this);
        })
        .on("click", fun)
//Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) ;

}
}

var totalViewbys;//edit by shivam
function buildWorldMapTable(chartId,data, currdata, columns, measureArray, divwidth, divHght,divId,totalViewbys) {//edit by shivam
 

 if(typeof totalViewbys=="undefined" ||totalViewbys==""){

     totalViewbys = JSON.parse(parent.$("#viewby").val());
 }
  
    var totalviewbyIds = JSON.parse(parent.$("#viewbyIds").val());
  //  var totalView = totalViewbys.split()
     var chartIdTile = chartId;
     chartId = chartId.replace("Tile","");
     var chartData = JSON.parse(parent.$("#chartData").val());
//  var colIds = chartData[chartId]["viewIds"];
  var columnsHeader = []
var keys = Object.keys(data[chartId][0])
columnsHeader .push(keys[0])
     var div1=parent.$("#chartname").val()
var fromoneview=parent.$("#fromoneview").val()
var colIds= [];
var div;
var yAxisFormat = "";
   var yAxisRounding = 0;
  if(typeof chartData[chartId]["yAxisFormat"]!= "undefined" && chartData[chartId]["yAxisFormat"]!= ""){
      yAxisFormat = chartData[chartId]["yAxisFormat"];
  }else{
   yAxisFormat = "Auto";
  }
  if(typeof chartData[chartId]["rounding"]!= "undefined" && chartData[chartId]["rounding"]!= ""){
      yAxisRounding = chartData[chartId]["rounding"];
  }else{
   yAxisRounding =1;
  }
  
 var dimensionId;
if(fromoneview!='null'&& fromoneview=='true'){
    colIds = chartData[div1]["viewIds"];
    viewOvName = chartData[div1]["viewBys"];
  viewOvIds = chartData[div1]["viewIds"];
  dimensionId = chartData[div1]["viewIds"];
  div=chartId
  chartId=div1;
  dimensionId = chartData[div1]["viewIds"];
  var prop = graphProp(chartId);
}else{
    colIds = chartData[chartId]["viewIds"];
    div=chartId
    dimensionId = chartData[chartId]["viewIds"];
     var prop = graphProp(chartId);
}
//  alert(tabDivVal)
//       var prop = graphProp(chartId);
        var trWidth = divwidth / 8;
//   $("#pieWithMap").html("");
//if(data[tabDivVal]!== "undefined"){
//    chart12 = data[tabDivVal];
if(typeof columns=="undefined" ||columns==""){
    columns = chartData[chartId]["viewBys"];

}
//measureArray = chartData[chartId]["meassures"];
//}

var allColumns = [];
for(var ch in chartData){
   allColumns.push(chartData[ch]["viewBys"][0]);
}
 var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
   var chartData = JSON.parse(parent.$("#chartData").val());
   var  colIds = chartData[chartId]["viewIds"];
//   var dimensionId = chartData[chartId]["dimensions"];
   var k = chartId.replace("chart","");
   var tableId = "table" + (parseInt(k) + 1);
   var divHeight = $(window).height();
   var viewBys = [];
   var rowCount=0;
   var prop = graphProp(chartId);
        var trWidth = divwidth / 8;
        divHght = divHght-50;
        if(typeof dimensionId !=="undefined"){
        for(var m=0;m<dimensionId.length;m++){
                            viewBys.push(viewOvName[viewOvIds.indexOf(dimensionId[m])])
                        }
           }else {
               viewBys = columns;
           }  
        if($("#chartType").val()=="India-Map-With-Trend"){
            viewBys = columns; 
        }
var htmlstr="";

// var html = "";
        if(data[chartId].length == 1){
        htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"max-height:" + divHght + "px; \">";
        }else{
             htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"max-height:" + divHght + "px; width:100%\">";
        }
        htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='height:400px;width:100%;margin-left:auto;margin-top:auto'>";
        if(data[chartId].length == 1){
        htmlstr += "<table id=\"chartTable" + div + "\" class='table table-condensed table-bordered ' width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: auto;border-collapse:collapse;font-size:10px;\">";
        var rowspan = columns.length;
             for(var a=0;a<viewBys.length;a++){
             htmlstr += "<tr style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black;white-space:nowrap;\">";
              htmlstr += "<th  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><strong>"+columns[a]+"</strong></th>"
      data[chartId].forEach(function(d, i) {


         htmlstr = htmlstr + "<th id='"+d[columns[a]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+div+"\")'  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\" width=" + trWidth + " >" + d[viewBys[a]] + "</th>";
        });
                 htmlstr = htmlstr + "</tr>";
             }

        for(var b=0;b<measureArray.length;b++){
             var num = (b + 1) % 2;
            var color;
            if (num === 0) {
                color = "rgba(240, 245, 248, 1)";
            } else {
                color = "white";
            }
         htmlstr += "<tr style=\"background-color:" + color + ";white-space:nowrap;\">";
         htmlstr += "<td style=\"background-color:#A6BEE9;color:black\"><strong>"+measureArray[b];
      data[chartId].forEach(function(d, i) {

          htmlstr += "<td >"+addCurrencyType(div, getMeasureId(measureArray[b])) + addCommas(d[measureArray[b]])+"</td>";

     });
        htmlstr +="</strong></td>"
         htmlstr += "</tr>";
        }
         htmlstr += "</table>";
        }//Transpose of Table
        else{

//htmlstr += "<div class='hiddenscrollbar' style = 'height:auto;overflow-y:auto;overflow-x:none;margin-top:20px;float:right;width:100%;'>";
               
   htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-condensed' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
//htmlstr+="<tr><td><div id = 'mapTable' class='innerhiddenscrollbar' style ='overflow-y:auto;overflow-x:none;height:350px;margin-right:10px'>"
//htmlstr += "<table style='width:100%' class = 'table table-condensed table-stripped ' style = 'float:right'>";
htmlstr += "<thead>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
//for(var i in columns){
//htmlstr += "<th ></th>";
for(var q in columns){
//if(columns[0].length > 20){
//htmlstr += "<th style='white-space:nowrap;font-weight:bold;text-align:left' >"+columns[q].substring(0, 20)+"..</th>";
//}else {
if(chartIdTile.indexOf("Tile")!=-1){
htmlstr += "<th style='font-weight:bold;text-align:left' >";
//}
//Added by shivam
htmlstr += "<div class='dropdowncss'><span id='showtable' style='width' onclick='showmydpview();'>Select Viewbys</span><ul id='mydpview' style='display:none;'>";
for(var w in totalViewbys){
htmlstr += "<li onclick ='changeviewsonthefly(this.id)' id='"+totalViewbys[w]+"_"+viewOvIds[w]+"' value='"+totalViewbys[w]+"'>"+totalViewbys[w]+"</li>";
}
htmlstr += "</ul></div></th>";
}else{
htmlstr += "<th style='font-weight:bold;text-align:left' >"+columns[q]+"</th>";
}
}
for(var i in measureArray){
if(measureArray[i].length > 15){
htmlstr += "<th title='"+measureArray[i]+"' style='white-space:nowrap;font-weight:bold;cursor:pointer;text-align:left'>"+measureArray[i].substring(0,15)+"..</th>";
}else {
htmlstr += "<th style='white-space:nowrap;font-weight:bold;text-align:left'>"+measureArray[i]+"</th>";
}
 
        if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!='hidden'){
                    if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!=='absolute'){
                        htmlstr += "<th width='5%' style=\" color:black\"></th>"
         }
                    else{
                        if( measureArray[i]===chartData[chartId]["emojiAbsValue"][0]){
        htmlstr += "<th width='5%' style=\" color:black\"></th>"
                        }
                    }
         }
}
htmlstr += "</tr>";
htmlstr += "</thead>";
  var colorValue = d3.scale.category10();
//  alert(JSON.stringifycurrdata)
data[chartId].forEach(function(d, i) {
           var drawColor="#A9A9A5";    
         var num = (i + 1) % 2;
            var color;
            if (num === 0) {
                color = "white";
            } else {
                color = "white";
            }
              
            var indexValue = "index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            var classValue = "bars-Bubble-index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            htmlstr = htmlstr + "<tr>";
//                <td  style=\"background-color:" + color + ";\" width=25px>"  ;
//            htmlstr += '<svg width="25" height="15"><circle class="'+classValue+'" cx="15" cy="10" r="5" index_value="'+indexValue+'" color_value="#A9A9A5" stroke="" onmouseover="tableGraphShow(\''+d[columns[0]] +'\')" onmouseout="tableGraphHide(\''+d[columns[0]] +'\')" stroke-width="3" fill="#A9A9A5"; /></svg>';
//            htmlstr += "</td>";
            var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
              
                   for(var l=0;l<viewBys.length;l++){
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewBys[l]]) !== -1) {

                           htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + " >" + d[columns[l]] + "</td>";

               }else{
                  
                               htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + color + ";cursor:pointer;\" width=" + trWidth + " ><u>" + d[columns[l]] + "</u></td>";
               }
                    }
                    var emojiWdith=divwidth/27;
            emojiWdith=emojiWdith>22?22:emojiWdith;
            for(var j=0;j<measureArray.length;j++){
            if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[j]]!='undefined'){
                yAxisFormat=numberFormatArr[measureArray[j]];
            }
            else{
                yAxisFormat="Auto";
            }
            if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[j]]!='undefined'){
                yAxisRounding=numberRoundingArr[measureArray[j]];
            }
            else{
                yAxisRounding=1;
            }
            if(yAxisRounding>0){
                htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:left;color:black;font-size:smaller\" width=" + trWidth + ">" +  addCurrencyType(div, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
//        alert("1")    
            }
            else{
            htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:left;color:black;font-size:smaller\" width=" + trWidth + ">" + addCurrencyType(div, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
            }  
        if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!='hidden'){
                    if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!=='absolute'){
                        var targetValue=chartData[chartId]["emojiValue"];
                        var ctxPath=parent.document.getElementById("h").value;
                        var imageName;
                        if(chartData[chartId]["emojiVisible"][j]===true){
                        if(parseInt(d[measureArray[j]])>targetValue[j]){
                            if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='happy_face.png';
            }
                else{
                    imageName='green.png';
                }
                            htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
                        else if(parseInt(d[measureArray[j]])==targetValue[i]){
                            if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='normal_face.png';
            }
                else{
                    imageName='amber.png';
                }
                            htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
        else{
            if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='sad_face.png';
            }
                else{
                    imageName='red.png';
                }
                            htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
                        }
                    }
                    else{
                            htmlstr += "<td></td>"
                        }
                    }
                    else{
                        if(measureArray[j]==chartData[chartId]["emojiAbsValue"][0]){
                            
                            var targetValue=chartData[chartId]["emojiAbsValue"];
            var ctxPath=parent.document.getElementById("h").value;
                            if(parseInt(d[targetValue[0]])>targetValue[1]){
                                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='happy_face.png';
            }
                else{
                    imageName='green.png';
                }
                                htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
                            else if(parseInt(d[targetValue[0]])==targetValue[1]){
                                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='normal_face.png';
            }
                else{
                    imageName='amber.png';
                }
                                htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
            else{
                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='sad_face.png';
            }
                else{
                    imageName='red.png';
                }
                                htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
        }
                    }
                }    
        }

            htmlstr = htmlstr + "</tr>";
        });    
                var colspan = columns.length +1;
if(typeof chartData[chartId]["showGT"]!=='undefined' && chartData[chartId]["showGT"]!=='' && chartData[chartId]["showGT"]==='Y'){   
        
          htmlstr += "<tr style='background-color:#D1D1D7;font-weight:bold;color:black'>";
          htmlstr += "<td colspan='"+ colspan  +"' style=\"background-color:\" width=" + trWidth + "><strong>Grand Total</strong></td>";
        // for calculating Gt
          for (var no = 0; no < measureArray.length; no++) {
//        var sum1=0;
      
//        if (measures[no].indexOf(specialCharacter)!=-1 || measures[no].indexOf("Percent")!=-1 ||measures[no].indexOf("percent")!=-1 ||measures[no].indexOf("PERCENT")!=-1) {
//                sum1 =   parseFloat(sum1/(data[chartId].length))
//                 }
//        sum1 = sum1.toFixed(2);
//        sum1.replace(".00", "");
//        if(ch/artData[chartId]["aggregation"][no]=='avg' || chartData[chartId]["aggregation"][no]=='AVG'){
//        sum1/=rowCount;
//          }
         htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:left\" width=" + trWidth + " >" +addCurrencyType(div, getMeasureId(measureArray[no])) + addCommas(KPIresult[no])+ "</td>";
          }
            htmlstr += "</tr>";
        }
        
            

htmlstr += "</table></div></td></tr></table>";
        }
        if(chartIdTile==div+"Tile" ){
        $("#openTableTile").html(htmlstr)
    }else if($("#chartType").val()=="India-Map-Dashboard" || $("#chartType").val()=="India-Map-With-Trend"){
     $("#part3").html(htmlstr);
    }else{
        $("#" + divId).html(htmlstr);
    }
         var table = $('#chartTable'+div).dataTable( {
//          "filter":true,
          "iDisplayLength":12,
          "bPaginate": true,
           "dom": 'T<"clear">rtp',
//        sDom: '<"datatable-exc-msg"><"row">flTrtip',
          "ordering": true,
          "jQueryUI": false,
          "bAutoWidth": false,
//          "scrollY": ""+divHght *.90+"px",
          "order": [[ 1, "desc" ]]  
          } );
          
 changeviewsonthefly=function(viewby){
  
var columnviews = [];
var totalviews = [];
var columnviewids = [];
var viewbyid = viewby.split("_");
viewby = viewbyid[0];
var id = viewbyid[1];
//document.getElementById("selectedtext").innerHTML=viewby;

//alert(id)
columnviews.push(viewby)
totalviews.push(viewby)
columnviewids.push(id)
for(var w in totalViewbys){
   if(totalViewbys[w]!=viewby){
    totalviews.push( totalViewbys[w])
 }

 }
totalViewbys = totalviews
//alert(totalViewbys)
chartData[chartId]["viewBys"] =columnviews
chartData[chartId]["viewIds"] =columnviewids
chartData[chartId]["dimensions"] = columnviewids
parent.$("#chartData").val(JSON.stringify(chartData));

var divIndex=parseInt(chartId.replace("chart", ""));
    var h=$("#divchart"+divIndex).height();
    var w=$("#divchart"+divIndex).width();
    var top=(h/2)-25;
    var left=(w/2)-25;
    $("#openTableTile").html("<div id='chart_loading' style='position:absolute;top:"+top+"px;left:"+left+"px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='"+$("#ctxpath").val()+"/images/chart_loading.gif' alt='Loading...' /></div>");

//alert(JSON.stringify(chartData[chartId]))
// $.ajax({
//        type:"POST",
//        data:parent.$("#graphForm").serialize(),
//        url: $("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs"+"&chartFlag=true"+"&chartID="+chartId,
        $.ajax({
                          type:'POST',
                          data:parent.$("#graphForm").serialize() +"&reportId="+$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val()+"",
                     url: $("#ctxpath").val()+"/reportViewer.do?reportBy=buildchartsWithObject",
       success: function(datamap){
       var data = JSON.parse(datamap)
//        alert(data)
        $("#openTableTile").html("")
        buildWorldMapTable(chartIdTile,data, data[chartId], columnviews, measureArray, divwidth, divHght,divId,totalViewbys)//edit by shivam
        }
 })


}
//Added by shivam
$("#showtable").text(columnsHeader[0]);
$("#showtable").css({
   "font-weight":"bold",
   "cursor": "pointer"
});

//$("#showtable").css("display","none");

 }

 function showmydpview(){
 var x=document.getElementById("mydpview").style.display;
 var id= document.getElementById("mydpview");
 if(x=='none'){


id.style.display="block";
 }else{
     id.style.display="none";
 }
 }

function buildUSWorldCityMap(div, data, columns, measureArray,divWid,divHgt){
var chId=div.replace("H","");
var color = d3.scale.category12();
var chartMapData = data["chart2"]
var fun="drilledWorldData(this.id,\"chart2\")";
 var chartData = JSON.parse($("#chartData").val());
    if(typeof data["chart2"]!="undefined" && data["chart2"]!="" ){
   var chartMapDataTable = data["chart2"]
   var tabColumns = chartData["chart2"]["viewBys"]
   var colIds = chartData["chart2"]["viewIds"];
}else{
  var chartMapDataTable = data["chart1"]
  var tabColumns = chartData[chId]["viewBys"]
  var colIds = chartData["chart1"]["viewIds"];

}
//    $("#"+div).append("<div id ='KPIdiv' style = 'height:100px;overflow-y:auto;margin-top:20px;border:2px inset grey;float:left;width:30%;'>");
//    $("#"+div).append("<div id ='mapdiv' style = 'height:auto;margin-top:-5%;float:right;width:65%;'>");
//     $("#"+div).append("<div id ='tableDiv' class='hiddenscrollbar' style = 'margin-top:20px;float:left;width:32%;'>");
//    KPIMapDefaultDiv()
//    kpiMapTableDiv(div,chartMapDataTable,tabColumns,measureArray)

    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    var min, max;
    min = minimumValue(chartMapData, measureArray[0]);
    max = maximumValue(chartMapData, measureArray[0]);
    var pointsize = d3.scale.linear()
            .domain([min, max])
            .range([6, 12]);
    parent.$("#sumValue").val(sum);

    width = divWid;
    height = divHgt;
    active = d3.select(null);
    var projection = d3.geo.albersUsa()
            .scale(570)
            .translate([width / 2.2, height / 2.1]);
    var projection1 = d3.geo.albersUsa()
//    .center([98, 23.5])
            .scale(900);

    var path1 = d3.geo.path()
            .projection(projection1);
    if (screen.width == 1024 && screen.height == 768) {
        projection = d3.geo.albersUsa()
                .scale(325)
                .translate([width / 1, height / 1.5]);
    }

    var path = d3.geo.path()
            .projection(projection);

    var svg = d3.select("#"+div ).append("svg")
            .attr("width", width)
            .attr("height", height)
            .style("float", "right");
    var g = svg.append("g")
            .style("stroke-width", "1.5px");
    d3.json("JS/us-States.json", function(error, us) {
        g.selectAll("path")
                .data(us.features)
                .enter().append("path")
                .attr("d", path)
                .attr("fill", function(d, i) {
                    var colorShad = "white";
                    return colorShad;
                })
                .attr("stroke", "#000")
//                .on("click", clicked)
                .on("mouseover", function(d, i) {
                    for (var k = 0; k < data.length; k++) {
                        show_detailsUS(d);
                    }
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
    });

    d3.json("JS/usCities.json", function(cities) {
      svg.selectAll('.cities')
                .data(cities)
                .enter()
                .append('path')
                .attr('d', path.pointRadius(function(d, i) {
                   for (var l2 = 0; l2 < chartMapData.length; l2++) {
                       if (typeof (d.properties.name) != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                     return pointsize(chartMapData[l2][measureArray[0]]);
                        }
                    }
                    return 0;
                }))
//      .attr('class', 'cities')
                .attr("class", function(d) {
                    var c1 = (d.properties.name);
                    for (var l1 = 0; l1 < chartMapData.length; l1++) {
                        if (typeof (d.properties.name) != "undefined" && chartMapData[l1][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                            return "place";
                        }
                    }
                    return "place1";
                })
                .attr("id", function(d, j) {
                    var c1 = (d.properties.name);
                    for (var l2 = 0; l2 < chartMapData.length; l2++) {
                        if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                            return chartMapData[l2][columns[0]] + ":" + columns[0] + ":map";
                        }
                    }
                    return c1;
                })
        .attr("onclick", fun)
//                .attr("fill", function(d) {
//                    for (var l2 = 0; l2 < chartMapData.length; l2++) {
//                        if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
//                            if (chartMapData[l2][measureArray[0]] == max) {
//                                return "#99FF00";
//                            } else {
//                                return "darkgreen";
//                            }
//                        }
//                    }
//                })
   .attr("fill", function(d,i){

            var currName = d.properties.name;
            var colorShad;
	    var drilledvalue;
	 try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
         } catch (e) {}
         if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
            colorShad = drillShade;
         }else{
         for (var l=0; l<chartMapData.length;l++) {
         if (currName.toLowerCase() == chartMapData[l][columns[0]].toLowerCase()) {
//          colorShad = color(i);
            return color(i);
         }else{
            colorShad = "white";
         }}}
            return colorShad;
                })
                .style("opacity", ".9")
                .on("mouseover", function(d, i) {
                    for (var k = 0; k < chartMapData.length; k++) {
                        if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                           show_details(d, columns, measureArray, this, i)
                            KPIMapDiv(chartMapData[k], columns, measureArray, this);
                        }
                    }

                })
                .on("mouseout", function(d, i) {
                    KPIMapDefaultDiv()
                    hide_details(d, i, this);
                })

    });


        clicked = function(id,divId) {
drillWithinchart1(id,divId)
for (var k = 0; k < chartMapData.length; k++) {
                        if (id.toLowerCase() === chartMapData[k][id].toLowerCase()) {

                            KPIMapDiv(chartMapData[k], id, measureArray, this);
    }
                    }
    }


    function reset() {
        active.classed("active", false);
        active = d3.select(null);

        g.transition()
                .duration(750)
                .style("stroke-width", "1.5px")
                .attr("transform", "");
    }
    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            for (var m in measureArray) {
                if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measureArray[m])) + addCommas(chartMapData[j][measureArray[m]]) + "</span><br/>";
                    count++;

                }
                else if (isShortForm(d, chartMapData[j][columns[0]])) {
                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measureArray[m])) + addCommas(chartMapData[j][measureArray[m]]) + "</span><br/>";
                    count++;

                }
            }
            if (count > 0) {
                return tooltip.showTooltip(content, d3.event);
            }

        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }
    }
    function show_detailsUS(d) {
        var content;
        var count = 0;
//        for (var j = 0; j < data.length; j++) {
        content = "<span class=\"name\">State : </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
//                content += "<span class=\"name\">State: </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//                count++;
        return tooltip.showTooltip(content, d3.event);
//            }
//            else if (isShortForm(d, data[j][columns[0]])) {
//                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + data[j][measureArray[0]] + "</span><br/>";
//                count++;
//                return tooltip.showTooltip(content, d3.event);
//            }
//        }
//        if (count === 0) {
//            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
//            return tooltip.showTooltip(content, d3.event);
//        }
    }
    function hide_details() {
        return tooltip.hideTooltip();
    }
}

function buildWorldAustraliaMap(div, data, columns, measureArray,divWid,divHgt){
         awesomeness = {"New South Wales":8,
    "Victoria":6,
    "Queensland":3,
    "South Australia":7,
    "Western Australia":4,
    "Tasmania": 6,
    "Northern Territory": 7, // + arnhem land, cumulo nimbus clouds over Darwin, - mosquitoes
    "Other Territories":5 // I'm sure they're ok
    };
        var color = d3.scale.category12();
    var fun="drilledWorldData(this.id,\"chart2\")";
var chart12 = data["chart2"];
var chartMapData = data["chart2"];
var chartData = JSON.parse(parent.$("#chartData").val());
// columns = chartData["chart1"]["viewBys"];
var mapColumns = chartData["chart2"]["viewBys"];
var colIds = chartData["chart2"]["viewIds"];
//measureArray = chartData["chart1"]["meassures"];
 var min, max;
    min = minimumValue(chartMapData, measureArray[0]);
    max = maximumValue(chartMapData, measureArray[0]);
    var pointsize = d3.scale.linear()
            .domain([min, max])
            .range([6, 10]);
    var w = divWid,
    h = divHgt;
    var z = d3.scale.category10();
    var fill = d3.scale.log()
    .domain(d3.extent(d3.values(awesomeness)))
    .range(["brown", "steelblue"]);

    var projection = d3.geo.azimuthal()
    .origin([135, -26])
      .translate([w / 2.2, h / 2.1])
    .scale(550);
    var path = d3.geo.path()
    .projection(projection);
    var svg = d3.select("#"+div).append("svg")
    .attr("width", w)
    .attr("height", h);
// var g = svg.append("g")
    var states = svg.append("g")
    .attr("id", "states")
            .style("stroke-width", "1.5px");
    d3.json("JS/australia.json", function(collection) {

    states.selectAll("path")
    .data(collection.features)
    .enter().append("path")
    .attr("d", path)
    .attr("fill", function(d) {
    var colorShad = "white";
    return colorShad;
    })

    .attr("d", path);
    })
       d3.json("JS/ausCities.json", function(cities) {
      svg.selectAll('.cities')
                .data(cities)
                .enter()
                .append("circle")
                .attr("cx", function(d) {
//            alert(JSON.stringify(d))
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[0];
    })
        .attr("cy", function(d) {
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[1];
        })
        .attr("r", function(d,i){
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
                       if (typeof (d.properties.name) != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                     return 6;
                        }
                    }
                    return 0;
        })
//      .attr('class', 'cities')
                .attr("class", function(d) {
                    var c1 = (d.properties.name);
                    for (var l1 = 0; l1 < chartMapData.length; l1++) {
                        if (typeof (d.properties.name) != "undefined" && chartMapData[l1][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                            return "place";
                        }
                    }
                    return "place1";
                })
                .attr("id", function(d, j) {
                    var c1 = (d.properties.name);
                    for (var l2 = 0; l2 < chartMapData.length; l2++) {
                        if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                            return chartMapData[l2][columns[0]] + ":" + columns[0] + ":map";
                        }
                    }
                    return c1;
                })
        .attr("onclick", fun)
                 .attr("fill", function(d,i){

            var currName = d.properties.name;
            var colorShad;
	    var drilledvalue;
	 try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
         } catch (e) {}
         if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
            colorShad = drillShade;
         }else{
         for (var l=0; l<chartMapData.length;l++) {
         if (currName.toLowerCase() == chartMapData[l][columns[0]].toLowerCase()) {
//          colorShad = color(i);
            return color(i);
         }else{
            colorShad = "white";
         }}}
            return colorShad;
                })
                .style("opacity", ".9")
                .on("mouseover", function(d, i) {
                    for (var k = 0; k < chartMapData.length; k++) {
                        if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                           show_details(d, columns, measureArray, this, i)
                            KPIMapDiv(chartMapData[k], columns, measureArray, this);
                        }
                    }

                })
                .on("mouseout", function(d, i) {
                    KPIMapDefaultDiv()
                    hide_details(d, i, this);
                })

    });
    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            for (var m in measureArray) {
                if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + addCommas(chartMapData[j][measureArray[m]]) + "</span><br/>";
                    count++;

                }
                else if (isShortForm(d, chartMapData[j][columns[0]])) {
                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + addCommas(chartMapData[j][measureArray[m]]) + "</span><br/>";
                    count++;

                }
            }
            if (count > 0) {
                return tooltip.showTooltip(content, d3.event);
            }

        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }
    }
    function show_detailsUS(d) {
        var content;
        var count = 0;
//        for (var j = 0; j < data.length; j++) {
        content = "<span class=\"name\">State : </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
//                content += "<span class=\"name\">State: </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//                count++;
        return tooltip.showTooltip(content, d3.event);
//            }
//            else if (isShortForm(d, data[j][columns[0]])) {
//                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + data[j][measureArray[0]] + "</span><br/>";
//                count++;
//                return tooltip.showTooltip(content, d3.event);
//            }
//        }
//        if (count === 0) {
//            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
//            return tooltip.showTooltip(content, d3.event);
//        }
    }
    function hide_details() {
        return tooltip.hideTooltip();
    }

}

function buildChinaMap(div, data, columns, measureArray,divWid,divHgt){
    var w = divWid;
    var h = divHgt;
    var fun = "drilledWorldData(this.id,'chart2')"
    var chartMapData = data["chart2"];
    var min = minimumValue(chartMapData, measureArray[0]);
   var max = maximumValue(chartMapData, measureArray[0]);
       var pointsize = d3.scale.linear()
            .domain([min, max])
            .range([6, 10]);
    var projection = d3.geo.mercator();
    var path = d3.geo.path().projection(projection);
    var t = projection.translate([-w*.55 , h *1.3]); // the projection's default translation
    var s = projection.scale(2200) // the projection's default scale
//  var projection = d3.geo.azimuthal()
//    .origin([135, -26])
//      .translate([w / 2.2, h / 2.1])
//    .scale(550);
//    var path = d3.geo.path()
//    .projection(projection);
 var svg = d3.select("#"+div).append("svg")
    .attr("width", w)
    .attr("height", h);
// var g = svg.append("g")
    var states = svg.append("g")
    .attr("id", "states")
            .style("stroke-width", "1.5px");
    d3.json("JS/china.json", function(collection) {

    states.selectAll("path")
    .data(collection.features)
    .enter().append("path")
    .attr("d", path)
    .attr("fill", function(d) {
    var colorShad = "white";
    return colorShad;
    })

    .attr("d", path);
    })
//    var svg = d3.select("#"+div).append("svg:svg")
//        .attr("width", w)
//        .attr("height", h)
//
//    var uk = svg.append("svg:g").attr("id", "uk");
//
//    d3.json("JS/china.json", function (json) {
//      uk.selectAll("path")
//          .data(json.features)
//        .enter().append("svg:path")
//          .attr("d", path)
//           .attr("fill", function(d) {
//    var colorShad = "white";
//    return colorShad;
//    });
//    });
  var ctxPath=$("#ctxpath").val();
   d3.json(ctxPath+"/JS/chinaCities.json", function(cities) {
      svg.selectAll('.cities')
                .data(cities)
                .enter()
                        .append("circle")
                .attr("cx", function(d) {
//            alert(JSON.stringify(d))
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[0];
    })
        .attr("cy", function(d) {
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[1];
        })
        .attr("r", function(d,i){
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
            
                       if (typeof (d.properties.name) != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                     return 6;
                        }
                    }
                    return 0;
        })
//      .attr('class', 'cities')
                .attr("class", function(d) {
                    var c1 = (d.properties.name);
                    for (var l1 = 0; l1 < chartMapData.length; l1++) {
                        if (typeof (d.properties.name) != "undefined" && chartMapData[l1][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                            return "place";
                        }
                    }
                    return "place1";
                })
                .attr("id", function(d, j) {
                    var c1 = (d.properties.name);
                    for (var l2 = 0; l2 < chartMapData.length; l2++) {
                        if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                            return chartMapData[l2][columns[0]] + ":" + columns[0] + ":map";
                        }
                    }
                    return c1;
                })
       // .attr("onclick", fun)
                  .attr("fill", function(d,i){

            var currName = d.properties.name;
            var colorShad;
	    var drilledvalue;
	 try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
         } catch (e) {}
         if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
            colorShad = drillShade;
         }else{
         for (var l=0; l<chartMapData.length;l++) {
         if (currName.toLowerCase() == chartMapData[l][columns[0]].toLowerCase()) {
//          colorShad = color(i);
            return color(i);
         }else{
            colorShad = "white";
         }}}
            return colorShad;
                })
                .style("opacity", ".9")
                .on("mouseover", function(d, i) {
                    for (var k = 0; k < chartMapData.length; k++) {
                        if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                           show_details(d, columns, measureArray, this, i)
//                            KPIMapDiv(chartMapData[k], columns, measureArray, this);
                        }
                    }

                })
                .on("mouseout", function(d, i) {
//                    KPIMapDefaultDiv()
                    hide_details(d, i, this);
                })
              .attr("onclick", fun)

    });
//    function redraw() {
//      // d3.event.translate (an array) stores the current translation from the parent SVG element
//      // t (an array) stores the projection's default translation
//      // we add the x and y vales in each array to determine the projection's new translation
//      var tx = t[0] * d3.event.scale + d3.event.translate[0];
//      var ty = t[1] * d3.event.scale + d3.event.translate[1];
//      proj.translate([tx, ty]);
//
//      // now we determine the projection's new scale, but there's a problem:
//      // the map doesn't 'zoom onto the mouse point'
//      proj.scale(s * d3.event.scale);
//
//      // redraw the map
//      uk.selectAll("path").attr("d", path);
//
//      // redraw the x axis
////      xAxis.attr("x1", tx).attr("x2", tx);
////
////      // redraw the y axis
////      yAxis.attr("y1", ty).attr("y2", ty);
//    }
    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            for (var m in measureArray) {
                if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + addCommas(chartMapData[j][measureArray[m]]) + "</span><br/>";
                    count++;

                }
                else if (isShortForm(d, chartMapData[j][columns[0]])) {
                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + addCommas(chartMapData[j][measureArray[m]]) + "</span><br/>";
                    count++;

                }
            }
            if (count > 0) {
                return tooltip.showTooltip(content, d3.event);
            }

        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }
    }
    function show_detailsUS(d) {
        var content;
        var count = 0;
//        for (var j = 0; j < data.length; j++) {
        content = "<span class=\"name\">State : </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
//                content += "<span class=\"name\">State: </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//                count++;
        return tooltip.showTooltip(content, d3.event);
//            }
//            else if (isShortForm(d, data[j][columns[0]])) {
//                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + data[j][measureArray[0]] + "</span><br/>";
//                count++;
//                return tooltip.showTooltip(content, d3.event);
//            }
//        }
//        if (count === 0) {
//            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
//            return tooltip.showTooltip(content, d3.event);
//        }
    }
    function hide_details() {
        return tooltip.hideTooltip();
    }

}

function drilledWorldData(id,div){

    drillWithinchart1(id,div)
    //alert(drilleData)
}
function drilleData(data){

    $("#worldMapTableDiv").html("")
   var chartId = "chart3";
   var 	columns = []
   var measureArray = []
   var tablewidth = 800;
var tableheight = 300;
var currdata = data["chart2"]
   var chartData=JSON.parse($("#chartData").val());
   if(typeof chartData["chart3"]["viewBys"]!="undefined" && chartData["chart3"]["viewBys"]!=""){
columns = chartData["chart3"]["viewBys"]
measureArray= chartData["chart3"]["meassures"]
currdata = data[chartId ]
   }else{
columns = chartData["chart2"]["viewBys"]
measureArray= chartData["chart2"]["meassures"]
currdata = data["chart2"]
   }
buildWorldMapTable(chartId,data, currdata, columns, measureArray, tablewidth, tableheight,"worldMapTableDiv")

}

function buildDrillMapFun(idArr,div,divID){
     var drills = {};
  $("#loading").show();
            // previous drills
       //     if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
         //       try{
          //          drills=JSON.parse($("#drills").val());
          //      }catch(e){
          //      }
          //  }
             var chartData = JSON.parse($("#chartData").val());
      var dimensions = chartData[div]["dimensions"];
                var viewOvName = JSON.parse(parent.$("#viewby").val());
                var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
                var currId = parseInt(dimensions.indexOf(chartData[div]["viewIds"][0]));
                var drillValues = [];
            drillValues.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
             var view = [];
                var chartId = div;
                var chartDetails = {};
                var viewName = [];
                 if(typeof dimensions[currId+1]!=="undefined"){
                    viewName.push(viewOvName[viewOvIds.indexOf(dimensions[currId+1])]);
                    view.push(dimensions[currId+1]);
                }else{
                    $("#loading").hide();
                    alert("Please Select More Parameters");
                    return "";
                }
                 $("#driver").val(div);
        $("#drills").val(JSON.stringify(drills));
        $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
            function(data){
var 	columns = []
   var measureArray = []
//alert(JSON.stringify(data))
columns = chartData["chart1"]["viewBys"]
measureArray= chartData["chart1"]["meassures"]
$("#worldMapDiv").html("");
$("#worldMapDrilledDiv").html("");
$("#worldMapTableDiv").html("");
  $("#loading").hide();
buildworldDrillMap(idArr,divID, JSON.parse(data), columns, measureArray)

            })
}

function buildWorldMapChart(divID, data, columns, measureArray,width,height,drillCountryName){

    var chartData = JSON.parse($("#chartData").val());
    var drills = parent.$("#drills").val();
    var driver = parent.$("#driver").val();
    var viewGBIds = JSON.parse(parent.$("#viewbyIds").val());
    var viewGBName = JSON.parse(parent.$("#viewby").val());
    var actionGO = parent.$("#actionGO").val();
    var dimension  = [];
    var initialViewByIds = chartData[divID]["viewIds"];
    var initialViewBys = chartData[divID]["viewBys"];
    var initialDimensions = chartData[divID]["dimensions"];
    if(typeof filterValue !=="undefined" && filterValue!==""){
    actionGO = "paramChange";
}   
    var viewbyFlag = [];
    var viewByIdFlag = [];
//alert(JSON.stringify(data))

    if(((typeof drills!=undefined  && typeof driver!="undefined" && divID==driver)  && (typeof chartData[divID]["mapdrill"]=="undefined" || chartData[divID]["mapdrill"]=="" || chartData[divID]["mapdrill"]=="yes") && chartData[divID]["mapMultiLevelDrill"]!="Yes")  ){ 
    if(chartData[divID]["dimensions"][1]!="undefined" && chartData[divID]["dimensions"][1]!=""){
viewByIdFlag.push(chartData[divID]["dimensions"][1]);
for(var i in viewGBIds){
if(viewGBIds[i]==chartData[divID]["dimensions"][1]){
viewbyFlag.push(viewGBName[i]);
dimension.push(chartData[divID]["dimensions"][1])
}
}
dimension.push(chartData[divID]["dimensions"][0]);
chartData[divID]["viewIds"] =viewByIdFlag;
chartData[divID]["viewBys"] =viewbyFlag;       
chartData[divID]["dimensions"]=dimension;
var flag = 'viewByChange';

if(chartData[divID]["drillType"]=="multi" && (typeof chartData[divID]["mapdrill"]=="undefined" || chartData[divID]["mapdrill"]=="" || chartData[divID]["mapdrill"]=="yes")){
chartData[divID]["mapdrill"]="yes";
}
 parent.$("#chartData").val(JSON.stringify(chartData));
     $.ajax({
        async:false,
        type:"POST",
            data:parent.$("#graphForm").serialize(),
        url:parent.$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+parent.$("#graphName").val()+"&reportId="+parent.$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+divID+'&actionGo='+actionGO,   
    success: function(data1){
     var  secondLevelData = JSON.parse(data1)["data"];
	//alert(JSON.stringify(secondLevelData ))
          buildWorldCityMapChart(divID, JSON.parse(secondLevelData), chartData[divID]["viewBys"], measureArray,width,height,drillCountryName) 
	//alert(chartData[divID]["drillType"])
	if(chartData[divID]["drillType"]!="multi"){
	chartData[divID]["viewIds"] =initialViewByIds;
	chartData[divID]["viewBys"] =initialViewBys;       
	chartData[divID]["dimensions"]=initialDimensions;
	}
	//alert(chartData[divID]["viewBys"])
	parent.$("#chartData").val(JSON.stringify(chartData));
       }})   
    }
    
    }
else if(chartData[divID]["mapMultiLevelDrill"]=="yes" && divID!=driver){
 if(chartData[divID]["dimensions"][1]!="undefined" && chartData[divID]["dimensions"][1]!=""){
viewByIdFlag.push(chartData[divID]["dimensions"][1]);
for(var i in viewGBIds){
if(viewGBIds[i]==chartData[divID]["dimensions"][1]){
viewbyFlag.push(viewGBName[i]);
dimension.push(chartData[divID]["dimensions"][1])
}
}
dimension.push(chartData[divID]["dimensions"][0]);
chartData[divID]["viewIds"] =viewByIdFlag;
chartData[divID]["viewBys"] =viewbyFlag;       
chartData[divID]["dimensions"]=dimension;
var flag = 'viewByChange';

if(chartData[divID]["drillType"]=="multi" && (typeof chartData[divID]["mapdrill"]=="undefined" || chartData[divID]["mapdrill"]=="" || chartData[divID]["mapdrill"]=="yes")){
chartData[divID]["mapdrill"]="yes";
}
 parent.$("#chartData").val(JSON.stringify(chartData));
     $.ajax({
        async:false,
        type:"POST",
            data:parent.$("#graphForm").serialize(),
        url:parent.$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+parent.$("#graphName").val()+"&reportId="+parent.$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+divID+'&actionGo='+actionGO,   
    success: function(data1){
     var  secondLevelData = JSON.parse(data1)["data"];
	//alert(JSON.stringify(secondLevelData ))
          buildWorldCityMapChart(divID, JSON.parse(secondLevelData), chartData[divID]["viewBys"], measureArray,width,height,drillCountryName) 
	
	if(chartData[divID]["drillType"]!="multi"){
	chartData[divID]["viewIds"] =initialViewByIds;
	chartData[divID]["viewBys"] =initialViewBys;       
	chartData[divID]["dimensions"]=initialDimensions;
	}
	
	parent.$("#chartData").val(JSON.stringify(chartData));
       }})   
    }


}

else{

 if(chartData[divID]["mapdrill"]=="yes"){
chartData[divID]["MapTransformation"] =  {}
     buildWorldCityMapChart(divID, data, columns, measureArray,width,height,drillCountryName)    
     } else{
     chartData[divID]["MapTransformation"] =  {}
     buildWorldMapChartData(divID, data, columns, measureArray,width,height,drillCountryName)    
     }
        
    
    }
    
//buildWorldMapChartData(divID, data, columns, measureArray,width,height,drillCountryName)    
}

function buildWorldMapChartData(divID, data, columns, measureArray,width,height,drillCountryName){
    var fun="";
    var cityColors = d3.scale.category10();
 graphProp(divID);
//    $("#"+divID).css("background-color","#ECEFF1")
    var DELAY = 700, clicks = 0, timer = null;
//	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
////	alert("hastuch1 : "+hasTouch)
//
//	if(hasTouch){
//    fun="";
//    }else{
        fun = "drillWithinchart(this.id,\""+divID+"\")";
//    }
if(typeof drillCountryName =="undefined"){
    drillCountryName= "";
}
var drillFunData = data
var chartMapData = data[divID];
 var chartData = JSON.parse($("#chartData").val());
 
 
     width = $(window).width()*.5;
    //    width = 1000;
    height = $(window).height()*.80;
    active = d3.select(null);
    
    
var yAxisFormat = "";
   var yAxisRounding = 0;
  if(typeof chartData[divID]["yAxisFormat"]!= "undefined" && chartData[divID]["yAxisFormat"]!= ""){
      yAxisFormat = chartData[divID]["yAxisFormat"];
  }else{
   yAxisFormat = "Auto";
  }
  if(typeof chartData[divID]["rounding"]!= "undefined" && chartData[divID]["rounding"]!= ""){
      yAxisRounding = chartData[divID]["rounding"];
  }else{
   yAxisRounding =1;
  }
//var chartMapData = data;
var colorMap={};
var color;
var transformWidth = 0
var transformHeight = 0
var transformScale = 0
var transformX = 0
var transformY = 0
var translateFunction = "";
var viewbyIds = chartData[divID]["viewIds"]  //Added by shivam
if(typeof chartData[divID]["MapTransformation"] !="undefined" && chartData[divID]["MapTransformation"] !="" && typeof chartData[divID]["MapTransformation"]["MapTransformation"] !="undefined" && chartData[divID]["MapTransformation"]["MapTransformation"] !=""){

transformWidth= parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][0])
transformHeight = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][1])
transformScale = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][2])
transformX = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][3])
transformY = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][4])
translateFunction = "translate(" + transformWidth / 2 + "," + transformHeight / 2 + ")scale(" + transformScale + ")translate(-" + transformX + ",-" + transformY + ")"
}else{
 chartData[divID]["MapTransformation"] =  {}
}

   var measure= measureArray[0];
//        var isShadedColor=true;
        //    $("#gradShade").val(color);
        //    shadingType="gradient";
        //    $("#shadeType").val(shadingType);
        var isShaded = parent.$("#isShaded").val();
       var shadeType =  parent.$("#shadeType").val();

        var FilledColor1 = "";

       if(typeof shadeType =="undefined" || shadeType == "" ){
          if(typeof chartData[divID]["FilledColor1"]!=="undefined" && chartData[divID]["FilledColor1"] !=""){
          FilledColor1=chartData[divID]["FilledColor1"];
          color=FilledColor1;
          }else{
           color="#b86e00";
          }
//            color="#b86e00";
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));

       }else if(typeof chartData[divID]["FilledColor1"]!=="undefined" && chartData[divID]["FilledColor1"] !=""){
        FilledColor1=chartData[divID]["FilledColor1"];
                color = FilledColor1;
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));
    
    
       } else { 
            color = d3.scale.category12();
       }
//       }
    //  --------------

//var shadingMeasure = "";
//            var conditionalMeasure = "";
//            var conditionalMap = {};
////            var isShadedColor = false;
//            var conditionalShading = false;
//---------
var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};
            var isShadedColor = false;
            var conditionalShading = false;
             if (parent.$("#isShaded").val() == "true" ) {
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalShading = true;
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        conditionalMeasure = conditionalMap["measure"];
                        conditionalMeasure = $("#conditionalMeasure").val();
                    } else if (parent.$("#shadeType").val() == "standard") {

                    } else {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];

                        isShadedColor = true;
                        var measureData = [];
                        for (var i = 0; i < chartMapData.length; i++) {
                            measureData.push(chartMapData[i][shadingMeasure]);
                        }
                        
                           var FilledColor2="";
                 if(typeof chartData[divID]["FilledColor2"]!=="undefined" && chartData[divID]["FilledColor2"] !=""){
             FilledColor2 =  FilledColor2=chartData[divID]["FilledColor2"];
//           alert(FilledColor2);
                            
                        }
          else { 
              FilledColor2 =  "#ffbf00";
                        }
                        
                 
                        color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range([FilledColor2, map[map["measure"]]]);
                    }
                } else {
                    if (parent.isCustomColors) {
                        color = d3.scale.ordinal().range(parent.customColorList);
                    }
                }


//                        color = d3.scale.linear()
//                                .domain([0, Math.max.apply(Math, measureData)])
//                                .range(["rgb(255,206,133)", map[map["measure"]]]);
//                    }
//                } else {
//                    if (parent.isCustomColors) {
//                        color = d3.scale.ordinal().range(parent.customColorList);
//                    }
//                }


    var maxMap = {};
    var minMap = {};
    for (var num = 0; num < measureArray.length; num++) {
        var measureData = [];
        for (var key in chartMapData) {
            measureData.push(chartMapData[key][measureArray[num]]);
        }
        maxMap[measureArray[num]] = Math.max.apply(Math, measureData);
        minMap[measureArray[num]] = Math.min.apply(Math, measureData);
    }
    parent.$("#maxMap").val(JSON.stringify(maxMap));
    parent.$("#minMap").val(JSON.stringify(minMap));

    function show_details(d,columns) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            if (chartMapData[j][columns].toLowerCase() === (d.properties.name).toLowerCase()) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCurrencyType(divID, getMeasureId(measureArray[0])) + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")) + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
            else if (isShortForm(d, chartMapData[j][columns])) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCurrencyType(divID, getMeasureId(measureArray[0])) + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1"))+ "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }

    }
    function hide_details() {
        return tooltip.hideTooltip();
    }


    var projection = d3.geo.mercator()
    //            .center([-20, 60])
                 .scale(490).translate([width/2.1, height/1.6]);
    var path = d3.geo.path()
    .projection(projection);

    var svg = d3.select("#"+divID)
    .append("svg")
     .attr("viewBox", "0 95 "+(width*.92)+" "+(height*.62)+" ")
        .style('background-color', function() {
                 var FilledColor4="";
                if(typeof chartData[divID]["FilledColor4"]!=="undefined" && chartData[divID]["FilledColor4"] !=""){
                return FilledColor4=chartData[divID]["FilledColor4"];
//                  
                } else {
                
                 return "white";
                        }
                                    })
//    .style("background-color","red")
//    .attr("width", width)
//    .attr("height", height*.95);

    var g = svg.append("g");

//    var colorMap = {};c
    var ctxPath=$("#ctxpath").val();

    d3.json(ctxPath+"/JS/world-topo-min.json", function(error, world) {
        g.selectAll("path")
        .data(topojson.feature(world, world.objects.countries).features)
        .enter()
        .append("path")
        .attr("d", path)// for(var i=0;i<Object.keys(data).length;i++){if(if)}\
         .attr("transform", translateFunction)

    //    .on("click", fun)
        .on("dblclick", clicked)
                    .style("fill", function(d, i) {
//   Added by shivam                             
                    var drilledvalue;
			//alert(parent.$("#drills").val()+"::"+parent.$("#drills").val()+"::"+viewbyIds[0])
                      
                            var colorShad;
                           // try {
			if(typeof parent.$("#drills").val()!="undefined" && parent.$("#drills").val()!=""){
                       var drilledvalue1 = JSON.parse(parent.$("#drills").val());
			var keys = Object.keys(drilledvalue1)
		//	alert(JSON.stringify(drilledvalue1))
			for(var i in keys){
		//		alert(drilledvalue1[keys[i]])
			if(keys[i]==viewbyIds[0]){
		//	drilledvalue = ShortFormName(d, drilledvalue1[keys[i]]);
		//	alert(drilledvalue1[keys[i]])
		//	alert(JSON.stringify(parent.lookupMap))
			if(drilledvalue1[keys[i]].toString().toUpperCase() in parent.lookupMap){

                     drilledvalue=parent.lookupMap[drilledvalue1[keys[i]].toString().toUpperCase()]
                    }
		//	alert(drilledvalue)
			}
                    
		//	alert(drilledvalue)
}				}
                  //  } catch (e) {
                  //  }
		//	alert(JSON.stringify(chartMapData)+"::"+columns[0])
			  for (var j = 0; j < chartMapData.length; j++) {
				
			
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(((d.properties.name).toUpperCase())) !== -1) {
                                return drillShade;
                            }
                           else if (typeof chartMapData[j][columns[0]]!="undefined" && chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                 if(parseInt(chartMapData[j][measureArray[0]])>0){
                                if (isShadedColor) {
                                    var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
				}else{
				    return "#fff";	
                            }
                            }
                            else if (isShortForm(d, chartMapData[j][columns[0]])) {
				 if(parseInt(chartMapData[j][measureArray[0]])>0){
                                if (isShadedColor) {
                                     var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
			}else{
				    return "#fff";	
                            }
                        }
                         
                        }                                  var FilledColor3="";
                 if(typeof chartData[divID]["FilledColor3"]!=="undefined" && chartData[divID]["FilledColor3"] !=""){
             return FilledColor3=chartData[divID]["FilledColor3"];
//           alert(FilledColor3);
                            
                        }
          else { 
              return "#D0D0D0";
                        }
                        
                    })
//                        return "#fff";
//                    })
                    .style("stroke", "#000").style("stroke-width","0.3px").on("mouseover", function(d, i) {
                show_details(d,columns[0]);
            })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    })
                    .attr("id",function(d, i) {
                    return  d.properties.name+":"+d.properties.name  ;
                    })
                    .on("click",function(d){
                      clicks++;  //count clicks

        if(clicks === 1) {

            timer = setTimeout(function() {
		chartData[divID]["mapdrill"]="yes"
		parent.$("#chartData").val(JSON.stringify(chartData));

		clicked(d)
              for (var j = 0; j < chartMapData.length; j++) {
                    if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase() && parseInt(chartMapData[j][measureArray[0]])>0) {
                       
            drillWithinchart(d.properties.name+":"+d.properties.name,divID)
         break;
                    }else{
//                        ""
                    }
                }
           // drillWithinchart(d.properties.name+":"+d.properties.name,divID)
           //     alert("Single Click");  //perform single-click action
                clicks = 0;             //after action performed, reset counter

            }, DELAY);

        } else {

            clearTimeout(timer);    //prevent single-click action
         //   alert("Double Click");  //perform double-click action
            clicks = 0;             //after action performed, reset counter
        }
                    } )
                 
				//	.on("click", function(d, i) {

				//	buildworldDrillMap(this.id,"Hchart1", drillFunData, columns, measureArray);
				//	});
					//.on("click", function(d, i) {
                                     //      buildDrillMapFun(this.id,"chart1","Hchart1")
				//	buildworldDrillMap(this.id,divID, data, columns, measureArray);
					//});
                //    .attr("onclick",fun);
//            parent.$("#colorMap").val(JSON.stringify(colorMap));

           var html = "<div id='legendsScale"+divID+"' onclick='zoom()'class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-41%;margin-left:87%;'></div>";
//                    $('#'+divID).append(html);

//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legendsScale"+divID).append("svg")
                            .attr("width", "100%")
                            .attr("height", "100%");

                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        var selectedMeasure = conditionalMap["measure"];
                        var selectedMeasure = $("#conditionalMeasure").val();
                        var keys = Object.keys(conditionalMap);
                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(selectedMeasure)
                                
//                        for (var no = 0; no < (keys.length - 1); no++) {
                        for (var no = 0; no < (keys.length ); no++) {
                            var legend = svg1.append("g")
                                    .attr("transform", function() {
                                        return "translate(10," + ((no * 20) + 20) + ")";
                                    });
                            legend.append("rect")
                                    .attr("width", 24)
                                    .attr("height", 14)
                                    .attr("cy", 9)
                                    .attr("stroke", "black")
                                    .attr("stroke-width", ".5")
                                    .style("fill", conditionalMap[no]["color"]);
//                                        .attr("r", 5);
                            legend.append("text")
                                    .attr("x", 28)
                                    .attr("y", 9)
                                    .attr("dy", ".35em")
                                    .text(function() {
                                        if (conditionalMap[no]["operator"] === "<>") {
                                            return conditionalMap[no]["limit1"] + conditionalMap[no]["operator"] + conditionalMap[no]["limit2"];
                                        } else {
                                            return conditionalMap[no]["operator"] + conditionalMap[no]["limit1"];
                                        }
                                    });
//                                        .attr("fill", conditionalMap[no]["color"]);
                        }
                        $("#legendsScale"+divID).css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else  {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legendsScale"+divID).height() - 10;
                        var shadingMeasure = colorMap["measure"];

                        var max = maximumValue(chartMapData, shadingMeasure);
                        var min = minimumValue(chartMapData, shadingMeasure);

                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend1")
                                .attr("x1", "0%")
                                .attr("y1", "30%")
                                .attr("x2", "50%")
                                .attr("y2", "30%")
                                .attr("spreadMethod", "pad")
                                .attr("gradientTransform", "rotate(90)");

                        gradient.append("svg:stop")
                                .attr("offset", "0%")
                                .attr("stop-color", colorMap[shadingMeasure])
                                .attr("stop-opacity", 1);
                        gradient.append("svg:stop")
                                .attr("offset", "100%")
                                .attr("stop-color", "rgb(230,230,230)")
                                .attr("stop-opacity", 1);

                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(shadingMeasure);
                        svg1.append("g").append("rect")
                                .attr("width", 10)
                                .attr("height", "90%")
                                .attr("fill", "url(#gradientWrdMapLegend1)")
                                .attr("x", 0)
                                .attr("y", 15);
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", height)
                                .attr("dy", ".35em").text(addCurrencyType(divID, chartData[divID]["meassureIds"][0]) + addCommas(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(addCurrencyType(divID, chartData[divID]["meassureIds"][0]) + addCommas(max));


                        $("#legendsScale"+divID).css("width", "14%");
                    }
//    svg1.append("g").append("rect").attr("x", 10)
//                                .attr("width", 10)
//                                .attr("height", "20%")
//                                 .attr("fill", "url(#gradientWrdMapLegend1)")
//                                .attr("x", 0)
//                                .attr("y", 15)
//    var zoom = d3.behavior.zoom()
});
if(typeof chartData[divID]["mapCountryName"]!="undefined" && chartData[divID]["mapCountryName"]!=""){

 //   Buildcities(chartData[divID]["mapCountryName"],translateFunction);

}
 var svg2 = svg.append("g");
if(typeof chartData[divID]["dataDisplay"]!="undefined" && chartData[divID]["dataDisplay"]!="" && chartData[divID]["dataDisplay"]=="Yes"){
    d3.json(ctxPath+"/JS/world-topo-min.json", function(error, world) {
        svg2.selectAll("path")
        .data(topojson.feature(world, world.objects.countries).features)
        .enter()
        .append("text")
        .text(function(d,i) {
           
            var returnData = "";
        for (var j = 0; j < chartMapData.length; j++) {
    if (chartMapData[j][columns[0]].toLowerCase() == (d.properties.name).toLowerCase()) {
   return addCurrencyType(divID, getMeasureId(measureArray[0])) + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,divID))
    }
    }
    })
      .attr("transform", function(d) {return "translate(" + path.centroid(d) + ")";
    });
        });
}
var centered;

  function clicked(d) {
   var countryName= d.properties.name;
  // alert(countryName)

  var x, y, k;
  if (d && centered !== d) {
    var centroid = path.centroid(d);
    x = centroid[0];
    y = centroid[1];
    k = 2;
    centered = d;
  } else {
    x = width / 2;
    y = height / 2;
    k = 1;
    centered = null;
  }
      g.selectAll("circle").remove();

if((typeof chartData[divID]["MapTransformation"]!="undefined"  && chartData[divID]["MapTransformation"]!="" && Object.keys(chartData[divID]["MapTransformation"]).length==0)||((typeof chartData[divID]["MapTransformation"]!="undefined"  && chartData[divID]["MapTransformation"]!="" && typeof chartData[divID]["MapTransformation"]["MapTransformation"] !="undefined" && chartData[divID]["MapTransformation"]["MapTransformation"] !="") && (chartData[divID]["MapTransformation"]["MapTransformation"][2]=="1" || chartData[divID]["mapCountryName"] !=countryName || chartData[divID]["mapCountryName"] ==""))){
//if(typeof chartData[divID]["MapTransformation"]!="undefined"  && chartData[divID]["MapTransformation"]!="" && typeof chartData[divID]["MapTransformation"]["MapTransformation"] !="undefined" && chartData[divID]["MapTransformation"]["MapTransformation"] !="" && (chartData[divID]["MapTransformation"]["MapTransformation"][2]=="1" || chartData[divID]["mapCountryName"] !=countryName)){
  chartData[divID]["mapCountryName"] = countryName;

//Buildcities(chartData[divID]["mapCountryName"],"")
}
//alert("width"+width+"height"+height+"k"+k+"x"+x+"y"+y)
var zoomVariable = [];
var map ={}
zoomVariable.push(width.toString())
zoomVariable.push(height.toString())
zoomVariable.push(k.toString())
zoomVariable.push(x.toString())
zoomVariable.push(y.toString())

map["MapTransformation"] = zoomVariable
chartData[divID]["MapTransformation"] = map

parent.$("#chartData").val(JSON.stringify(chartData));
//alert("width"+width+"height"+height+"k"+k+"x"+x+"y"+y)
  g.selectAll("path")
      .classed("active", centered && function(d) { return d === centered; });

  g.transition()
      .duration(750)
      .attr("transform",null)
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")scale(" + k + ")translate(" + -x + "," + -y + ")")
      .style("stroke-width", 1.5 / k + "px")

  svg2.selectAll("path")
      .classed("active", centered && function(d) {return d === centered;});

  svg2.transition()
      .duration(750)
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")scale(" + k + ")translate(" + -x + "," + -y + ")")
      .style("stroke-width", 1.5 / k + "px");
}
 function Buildcities(countryName,translateFunction){
        g.selectAll("circle").remove();

  d3.json(ctxPath+"/JS/WorldCities.json", function(error, world) {
        g.selectAll("circle")
        .data(world)
        .enter()

        .append("circle")
        .attr("cx", function(d) {
//            alert(JSON.stringify(d))
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[0];
    })
        .attr("cy", function(d) {
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[1];
        })
        .attr("r", function(d,i){
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
                       if (typeof (d.properties.name) != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == countryName.toLowerCase() && chartMapData[l2][columns[1]].toLowerCase() == d.properties.name.toLowerCase() ) {

                     return 2;
                        }
                    }
                    return 0;
        })
        .style("fill", function(d,i){
              var drilledvalue;
             try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[viewbyIds[0]];
                    } catch (e) {
                    }

                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf((d.properties.name)) !== -1) {
                                return drillShade;
                            }else{

        return "#0A3257";
                            }
//        return cityColors(i);
        })
        .on("click",function(d,i){drillWithinchart(d.properties.name+":"+d.properties.name,divID)})
        .attr("transform", translateFunction)
        .on("mouseover", function(d, i) {
                show_details(d,columns[1]);
            })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    })
        })
}
//         function zoom(){
//                g.attr("transform", "translate(" +
//                        d3.event.translate.join(",") + ")scale(" + d3.event.scale + ")");
//                g.selectAll("path")
//                        .attr("d", path.projection(projection))
//
//             svg.call(zoom);
//    d3.select(self.frameElement).style("height", height + "px");
//         }
//var buildworldDrillMap =  function(id,div) {
//       alert(id+"::"+div)
//    }
}
function buildWorldCityMapChart(divID, data, columns, measureArray,width,height){
var drillsvalue = JSON.parse($("#drills").val())
var drillKeys = Object.keys(JSON.parse($("#drills").val()));
for(var drillin=0;drillin< drillKeys.length;drillin++ ){
var chartId = divID.replace("chart","");

if(chartId ==(drillin+1)||chartId ==(drillin+2)){
var countryName = drillsvalue[drillKeys[drillin]]
var jsonvar='';
if( countryName=='USA'||countryName=='UNITEDSTATES'){
jsonvar="usCities.json";
}else if(countryName=='AUS'||countryName=='AUSTRALIA'){
jsonvar="ausCities.json";
}else if(countryName=='CHN'||countryName=='CHINA'){
jsonvar="chinaCities.json";
}
else if(countryName=='FIN'||countryName=='FINLAND'){
jsonvar="finlandCities.json"
}
else if(countryName=='FRA'||countryName=='FRANCE'){
jsonvar="franceCities.json"
}
else if(countryName=='ESP'||countryName=='SPAIN'){
jsonvar="spainCities.json"
}
else if(countryName=='GBR'||countryName=='UNITED KINGDOM'){
jsonvar="ukCities.json"
}
else if(countryName=='SAU'||countryName=='SAUDI ARABIA'){
jsonvar="saudi_cities.json"
}
else if(countryName=='BRA'||countryName=='BRAZIL'){
jsonvar="Brazil_cities.json"
}
else if(countryName=='CHL'||countryName=='CHILE'){
jsonvar="chli.json"
}
else if(countryName=='COL'||countryName=='COLUMBIA'){
jsonvar="Columbia.json"
}
else if(countryName=='ECU'||countryName=='EUCADOR'){
jsonvar="ecqudor.json"
}
else if(countryName=='ISL'||countryName=='ICELAND'){
jsonvar="Iceland.json"
}
else if(countryName=='IDN'||countryName=='INDONESIA'){
jsonvar="Indonesia.json"
}
else if(countryName=='MEX'||countryName=='MEXICO'){
jsonvar="Mexico.json"
}
else if(countryName=='NAM'||countryName=='NAMIBIA'){
jsonvar="Namibia.json"
}
else if(countryName=='NOR'||countryName=='NORWAY'){
jsonvar="Norway.json"
}
else if(countryName=='PER'||countryName=='PERU'){
jsonvar="Peru.json"
}
else if(countryName=='PRT'||countryName=='PORTUGAL'){
jsonvar="Portugal.json"
}
else if(countryName=='TUR'||countryName=='TURKEY'){
jsonvar="turky.json"
}
else if(countryName=='NZL'||countryName=='NEWZEALAND'){
jsonvar="NEWZELAND.json"
}
else if(countryName=='DEU'||countryName=='GERMANY'){
jsonvar="GERMANY.json"
}
else if(countryName=='SWE'||countryName=='SWEDEN'){
jsonvar="SWEDEN.json"
}
else if(countryName=='IRL'||countryName=='IRELAND'){
jsonvar="IRELAND.json"
}
else if(countryName=='DNK'||countryName=='DENMARK'){
jsonvar="DENMARK.json"
}
else if(countryName=='BEL'||countryName=='BELGIUM'){
jsonvar="Belgium.json"
}
else if(countryName=='NLD'||countryName=='NETHERLAND'){
jsonvar="NETHERLANDS.json"
}
else if(countryName=='CZE'||countryName=='CZECH REPUBLIC'){
jsonvar="CzechRepublic.json"
}
else if(countryName=='CAN'||countryName=='CANADA'){
jsonvar="CanadaCities.json"
}
else if(countryName=='IND'||countryName=='INDIA'){
jsonvar="IndiaCities.json"
}
else if(countryName=='BGD'||countryName=='BANGLADESH'){
jsonvar="BANGLADESH.json"
}
else if(countryName=='KHM'||countryName=='COMBODIA'){
jsonvar="COMBODIA.json"
}
else if(countryName=='EGY'||countryName=='egypt'){
jsonvar="egypt.json"
}
else if(countryName=='KOR'||countryName=='southkorea'){
jsonvar="southkorea_cities.json"
}
else if(countryName=='CHE'||countryName=='SWITZERLAND'){
jsonvar="SWITZERLAND.json"
}
else if(countryName=='BHS'||countryName=='BAHAMAS'){
jsonvar="bahamas_cities.json"
}
else if(countryName=='BRB'||countryName=='BARBADOS'){
jsonvar="BARBODAS_CITIES.json"
}
else if(countryName=='BRN'||countryName=='BRUNEI'){
jsonvar="BRUNEI_DARSSULEM_CITIES.json"
}
else if(countryName=='CYM'||countryName=='CAYMANISLANDS'){
jsonvar="caymanislands_cities.json"
}
else if(countryName=='SWR'||countryName=='CYPRUS'){
jsonvar="cyprus_cities.json"
}
else if(countryName=='HTI'||countryName=='DOMNICANREPUBLIC'){
jsonvar="domnican_republic_cities.json"
}
else if(countryName=='HKG'||countryName=='HONGKONG'){
jsonvar="HONGKONG_CITIES.json"
}
else if(countryName=='JPN'||countryName=='JAPAN'){
jsonvar="japan_cities.json"
}
else if(countryName=='ARE'||countryName=='UAE'){
jsonvar="uae_cities.json"
}
else if(countryName=='GBR'||countryName=='UNITED KINGDOM'){
jsonvar="UNITED_KINGDOM.json"
}
else{
jsonvar="WorldCities.json";

}
}else{
jsonvar="WorldCities.json";

}
}


//    $("#viewMeasureBlock").css({'display':'none'});
//$("#"+divID).css("background-color","#ECEFF1")
    var DELAY = 700, clicks = 0, timer = null;
        var chartMapData = data[divID];
     //  alert(JSON.stringify(chartMapData))
//    var color = d3.scale.category12();
   // add gradient
    var colorMap={};
var color;
    var transformWidth = 0
var transformHeight = 0
var transformScale = 0
var transformX = 0
var transformY = 0
var translateFunction = "";
var chartData = JSON.parse($("#chartData").val());
var viewbyIds = chartData[divID]["viewIds"]  //Added by shivam

if(typeof chartData[divID]["MapTransformation"] !="undefined" && chartData[divID]["MapTransformation"] !="" && typeof chartData[divID]["MapTransformation"]["MapTransformation"] !="undefined" && chartData[divID]["MapTransformation"]["MapTransformation"] !=""){

transformWidth= parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][0])
transformHeight = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][1])
transformScale = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][2])
transformX = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][3])
transformY = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][4])
translateFunction = "translate(" + transformWidth / 2 + "," + transformHeight / 2 + ")scale(" + transformScale + ")translate(-" + transformX + ",-" + transformY + ")"
}else{
 chartData[divID]["MapTransformation"] =  {}
}
   var measure= measureArray[0];
//        var isShadedColor=true;
        //    $("#gradShade").val(color);
        //    shadingType="gradient";
        //    $("#shadeType").val(shadingType);
//        $("#isShaded").val("true");
//        $("#shadeType").val("gradient");
//
        var colorMap={};
//        colorMap[measure]=color;
//        colorMap["measure"]=measure;
//        parent.$("#measureColor").val(JSON.stringify(colorMap));

 var isShaded = parent.$("#isShaded").val();
       var shadeType =  parent.$("#shadeType").val();

    var FilledColor1 = "";
       if(typeof shadeType =="undefined" || shadeType == "" ){
            color="#b86e00";
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));

       }else if(typeof chartData[divID]["FilledColor1"]!=="undefined" && chartData[divID]["FilledColor1"] !=""){
        FilledColor1=chartData[divID]["FilledColor1"];
                color = FilledColor1;
       $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));
    
       }else {
            color = d3.scale.category12();
       }
var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};
//            var isShadedColor = false;
            var conditionalShading = false;
             if (parent.$("#isShaded").val() == "true" || colorFlag) {
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalShading = true;
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        conditionalMeasure = conditionalMap["measure"];
                        conditionalMeasure = $("#conditionalMeasure").val();
                    } else if (parent.$("#shadeType").val() == "standard") {

                    } else {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];

                        isShadedColor = true;
                        var measureData = [];
                        for (var i = 0; i < chartMapData.length; i++) {
                            measureData.push(chartMapData[i][shadingMeasure]);
                        }
                         var FilledColor2="";
                 if(typeof chartData[divID]["FilledColor2"]!=="undefined" && chartData[divID]["FilledColor2"] !=""){
             FilledColor2 =  FilledColor2=chartData[divID]["FilledColor2"];
//           alert(FilledColor2);
                            
                        }
          else { 
              FilledColor2 =  "#ffbf00";
                        }
                        
                 
                        color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range([FilledColor2, map[map["measure"]]]);
                    }
                } else {
                    if (parent.isCustomColors) {
                        color = d3.scale.ordinal().range(parent.customColorList);
                    }
                }

                        
//                        color = d3.scale.linear()
//                                .domain([0, Math.max.apply(Math, measureData)])
//                                .range(["rgb(255,206,133)", map[map["measure"]]]);
//                    }
//                } else {
//                    if (parent.isCustomColors) {
//                        color = d3.scale.ordinal().range(parent.customColorList);
//                    }
//                }
    var maxMap = {};
    var minMap = {};
    for (var num = 0; num < measureArray.length; num++) {
        var measureData = [];
        for (var key in chartMapData) {
            measureData.push(chartMapData[key][measureArray[num]]);
        }
        maxMap[measureArray[num]] = Math.max.apply(Math, measureData);
        minMap[measureArray[num]] = Math.min.apply(Math, measureData);
    }
    parent.$("#maxMap").val(JSON.stringify(maxMap));
    parent.$("#minMap").val(JSON.stringify(minMap));

    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCurrencyType(divID, getMeasureId(measureArray[0])) + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")) + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCurrencyType(divID, getMeasureId(measureArray[0])) + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")) + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }

    }
    function hide_details() {
        return tooltip.hideTooltip();
    }

   var projection = d3.geo.mercator()
    //            .center([-20, 60])
                .scale(450).translate([width/2.1, height/1.6]);
    var path = d3.geo.path()
    .projection(projection); //

    var svg = d3.select("#"+divID)
    .append("svg")
     .style('background-color', function() {
                 var FilledColor4="";
                if(typeof chartData[divID]["FilledColor4"]!=="undefined" && chartData[divID]["FilledColor4"] !=""){
                return FilledColor4=chartData[divID]["FilledColor4"];
//                  
                } else {
                
                 return "white";
                        }
                                    })
    .attr("width", width)
    .attr("height", height*.95);

    var g = svg.append("g");

    var colorMap = {};
    var ctxPath=$("#ctxpath").val();

    d3.json(ctxPath+"/JS/world-topo-min.json", function(error, world) {

        g.selectAll("path")
        .data(topojson.feature(world, world.objects.countries).features)
        //          .geometries)
        .enter()
        .append("path")
        .attr("d", path)
          .attr("transform", translateFunction)
         .on("dblclick", clicked)
        .style("fill", function(d, i) {
            return "white";
        })
        .style("stroke", "#a1a1a1").style("stroke-width","0.3px");
        parent.$("#colorMap").val(JSON.stringify(colorMap));
    })

    setTimeout(function(){
//alert(ctxPath+"/JS/"+jsonvar)
        d3.json(ctxPath+"/JS/"+jsonvar, function(error, world) { 
        g.selectAll("circle")
        .data(world)
        .enter()
//        .append("a")
//        .attr("xlink:href", function(d) {
//            return "https://www.google.com/search?q="+data[columns[0]];
//        }
//        )
        .append("circle")
        .attr("cx", function(d) {
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[0];
    })
        .attr("cy", function(d) {
            return projection([d.geometry.coordinates[1], d.geometry.coordinates[0]])[1];
        })
        .attr("r", function(d,i){ 
            for (var l2 = 0; l2 < chartMapData.length; l2++) { 
                       if (typeof (d.properties.name) != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                     return 2;
                        }
                    }
                    return 0;
        })
	 .attr("transform", translateFunction)
        .style("fill", function(d,i){
//             Added by shivam                             
                    var drilledvalue;
             for (var j = 0; j < chartMapData.length; j++) {
                            var colorShad;
                            try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[viewbyIds[0]];
                    } catch (e) {
                    }
                    
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf((d.properties.name)) !== -1) {
                        return drillShade;
                    }
                       
                            if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                if (isShadedColor) {
                                    var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                                if (isShadedColor) {
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                        }
                        
                           var FilledColor3="";
                 if(typeof chartData[divID]["FilledColor3"]!=="undefined" && chartData[divID]["FilledColor3"] !=""){
             return FilledColor3=chartData[divID]["FilledColor3"];
//           alert(FilledColor3);
                            
                        }
          else { 
              return "#D0D0D0";
                        }
                        
//                    })
                        
//                        return "white";
        }).attr("opacity","0.6")
        .on("mouseover", function(d, i) {
                show_details(d);
            })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    })
     .on("click",function(d){
                      clicks++;  //count clicks

        if(clicks === 1) {

            timer = setTimeout(function() {
		 chartData[divID]["mapMultiLevelDrill"] = "Yes";
                parent.$("#chartData").val(JSON.stringify(chartData));
             drillWithinchart((d.properties.name).toUpperCase()+":"+(d.properties.name).toUpperCase(),divID)
           //     alert("Single Click");  //perform single-click action
                clicks = 0;             //after action performed, reset counter

            }, DELAY);

        } else {

            clearTimeout(timer);    //prevent single-click action
         //   alert("Double Click");  //perform double-click action
            clicks = 0;             //after action performed, reset counter
        }
                    } );

           var html = "<div id='legendsScale' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-41%;margin-left:87%;'></div>";
//                    $('#'+divID).append(html);

//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legendsScale").append("svg")
                            .attr("width", "100%")
                            .attr("height", "100%");

                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        var selectedMeasure = conditionalMap["measure"];
                        var selectedMeasure = $("#conditionalMeasure").val();
                        var keys = Object.keys(conditionalMap);
                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(selectedMeasure);
//                        for (var no = 0; no < (keys.length - 1); no++) {
                        for (var no = 0; no < (keys.length ); no++) {
                            var legend = svg1.append("g")
                                    .attr("transform", function() {
                                        return "translate(10," + ((no * 20) + 20) + ")";
                                    });
                            legend.append("rect")
                                    .attr("width", 24)
                                    .attr("height", 14)
                                    .attr("cy", 9)
                                    .attr("stroke", "black")
                                    .attr("stroke-width", ".5")
                                    .style("fill", conditionalMap[no]["color"]);
//                                        .attr("r", 5);
                            legend.append("text")
                                    .attr("x", 28)
                                    .attr("y", 9)
                                    .attr("dy", ".35em")
                                    .text(function() {
                                        if (conditionalMap[no]["operator"] === "<>") {
                                            return conditionalMap[no]["limit1"] + conditionalMap[no]["operator"] + conditionalMap[no]["limit2"];
                                        } else {
                                            return conditionalMap[no]["operator"] + conditionalMap[no]["limit1"];
                                        }
                                    });
//                                        .attr("fill", conditionalMap[no]["color"]);
                        }
                        $("#legendsScale").css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else  {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legendsScale").height() - 10;
                        var shadingMeasure = colorMap["measure"];

                        var max = maximumValue(chartMapData, shadingMeasure);
                        var min = minimumValue(chartMapData, shadingMeasure);

                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend1")
                                .attr("x1", "0%")
                                .attr("y1", "30%")
                                .attr("x2", "50%")
                                .attr("y2", "30%")
                                .attr("spreadMethod", "pad")
                                .attr("gradientTransform", "rotate(90)");

                        gradient.append("svg:stop")
                                .attr("offset", "0%")
                                .attr("stop-color", colorMap[shadingMeasure])
                                .attr("stop-opacity", 1);
                        gradient.append("svg:stop")
                                .attr("offset", "100%")
                                .attr("stop-color", "rgb(230,230,230)")
                                .attr("stop-opacity", 1);

                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(shadingMeasure);
                        svg1.append("g").append("rect")
                                .attr("width", 10)
                                .attr("height", "90%")
                                .attr("fill", "url(#gradientWrdMapLegend1)")
                                .attr("x", 0)
                                .attr("y", 15);
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", height)
                                .attr("dy", ".35em").text(addCurrencyType(divID, chartData[divID]["meassureIds"][0]) + addCommas(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(addCurrencyType(divID, chartData[divID]["meassureIds"][0]) + addCommas(max));


                        $("#legendsScale").css("width", "14%");
                    }

    });
    },3000);

var centered;
 var clicked = function(d) {
  var x, y, k;
//alert(JSON.stringify(d))
  if (d && centered !== d) {
    var centroid = path.centroid(d);
    x = centroid[0];
    y = centroid[1];
    k = 3;
    centered = d;
  } else {
    x = width / 2;
    y = height / 2;
    k = 1;
    centered = null;
  }
//alert("width"+width+"height"+height+"k"+k+"x"+x+"y"+y)
var zoomVariable = [];
var map ={}
zoomVariable.push(width.toString())
zoomVariable.push(height.toString())
zoomVariable.push(k.toString())
zoomVariable.push(x.toString())
zoomVariable.push(y.toString())

map["MapTransformation"] = zoomVariable
chartData[divID]["MapTransformation"] = map

parent.$("#chartData").val(JSON.stringify(chartData));
  g.selectAll("path")
      .classed("active", centered && function(d) { return d === centered; });

  g.transition()
      .duration(750)
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")scale(" + k + ")translate(" + -x + "," + -y + ")")
      .style("stroke-width", 1.5 / k + "px");
}
//    var zoom = d3.behavior.zoom()
//    .on("zoom", function() {
//        g.attr("transform", "translate(" +
//            d3.event.translate.join(",") + ")scale(" + d3.event.scale + ")");
//        g.selectAll("path")
//        .attr("d", path.projection(projection));
//    });
//    svg.call(zoom);
//    d3.select(self.frameElement).style("height", height + "px");
}

function buildWorldTopAnalysisChart(divID, data, columns, measureArray,width,height,drillCountryName){
//    width*=0.5;
graphProp(divID);
    var fun="";
    buildWorldTopAnalysis(divID, data[divID], columns, measureArray, width*0.4, height,"worldMap");
    width*=0.7;
    height*=0.9;
//    $("#"+divID).css("background-color","#ECEFF1")
    var DELAY = 700, clicks = 0, timer = null;
//	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
////	alert("hastuch1 : "+hasTouch)
//
//	if(hasTouch){
//    fun="";
//    }else{
        fun = "drillWithinchart(this.id,\""+divID+"\")";
//    }
if(typeof drillCountryName =="undefined"){
    drillCountryName= "";
}
var drillFunData = data
var chartMapData = data[divID];
 var chartData = JSON.parse($("#chartData").val());
//var chartMapData = data;
var colorMap={};
var color;
var transformWidth = 0
var transformHeight = 0
var transformScale = 0
var transformX = 0
var transformY = 0
var translateFunction = "";
var viewbyIds = chartData[divID]["viewIds"]  //Added by shivam
if(typeof chartData[divID]["MapTransformation"] !="undefined" && chartData[divID]["MapTransformation"] !="" && typeof chartData[divID]["MapTransformation"]["MapTransformation"] !="undefined" && chartData[divID]["MapTransformation"]["MapTransformation"] !=""){

transformWidth= parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][0])
transformHeight = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][1])
transformScale = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][2])
transformX = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][3])
transformY = parseInt(chartData[divID]["MapTransformation"]["MapTransformation"][4])
translateFunction = "translate(" + transformWidth / 2 + "," + transformHeight / 2 + ")scale(" + transformScale + ")translate(-" + transformX + ",-" + transformY + ")"
}else{
 chartData[divID]["MapTransformation"] =  {}
}

   var measure= measureArray[0];
//        var isShadedColor=true;
        //    $("#gradShade").val(color);
        //    shadingType="gradient";
        //    $("#shadeType").val(shadingType);
        var isShaded = parent.$("#isShaded").val();
       var shadeType =  parent.$("#shadeType").val();

       var FilledColor1 = "";

       if(typeof shadeType =="undefined" || shadeType == "" ){
            color = "#b86e00";
//             color="#7EC0EE";
//             color="#b86e00";
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));

       }else if(typeof chartData[divID]["FilledColor1"]!=="undefined" && chartData[divID]["FilledColor1"] !=""){
        FilledColor1=chartData[divID]["FilledColor1"];
                color = FilledColor1;
        $("#isShaded").val("true");
        $("#shadeType").val("gradient");
        colorMap[measure]=color;
        colorMap["measure"]=measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));
       }else{
            color = d3.scale.category12();
       }
//   }
    //  --------------

//var shadingMeasure = "";
//            var conditionalMeasure = "";
//            var conditionalMap = {};
////            var isShadedColor = false;
//            var conditionalShading = false;
//---------
var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};
            var isShadedColor = false;
            var conditionalShading = false;
             if (parent.$("#isShaded").val() == "true" ) {
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalShading = true;
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        conditionalMeasure = conditionalMap["measure"];
                        conditionalMeasure = $("#conditionalMeasure").val();
                    } else if (parent.$("#shadeType").val() == "standard") {

                    } else {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];

                        isShadedColor = true;
                        var measureData = [];
                        var measureDataGrad = [];
                        for (var i = 0; i < chartMapData.length; i++) {
                            measureData.push(chartMapData[i][shadingMeasure]);
                        }
//                        for (var i = chartMapData.length; i > 0; i--) {
//                            measureDataGrad.push(i);
//                        }
//                        
                         var FilledColor2="";
                 if(typeof chartData[divID]["FilledColor2"]!=="undefined" && chartData[divID]["FilledColor2"] !=""){
             FilledColor2 =  FilledColor2=chartData[divID]["FilledColor2"];
//           alert(FilledColor2);
                            
                        }
          else { 
              FilledColor2 =  "#ffbf00";
                        }
                        
                 
                        color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range([FilledColor2, map[map["measure"]]]);
                    }
                } else {
                    if (parent.isCustomColors) {
                        color = d3.scale.ordinal().range(parent.customColorList);
                    }
                }


    var maxMap = {};
    var minMap = {};
    for (var num = 0; num < measureArray.length; num++) {
        var measureData = [];
        for (var key in chartMapData) {
            measureData.push(chartMapData[key][measureArray[num]]);
        }
        maxMap[measureArray[num]] = Math.max.apply(Math, measureData);
        minMap[measureArray[num]] = Math.min.apply(Math, measureData);
    }
    parent.$("#maxMap").val(JSON.stringify(maxMap));
    parent.$("#minMap").val(JSON.stringify(minMap));

    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCurrencyType(divID, getMeasureId(measureArray[0])) + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1")) + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
            else if (isShortForm(d, chartMapData[j][columns[0]])) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + addCurrencyType(divID, getMeasureId(measureArray[0])) + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,"chart1"))+ "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
        }
        if (count === 0) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }

    }
    function hide_details() {
        return tooltip.hideTooltip();
    }


    var projection = d3.geo.mercator()
    //            .center([-20, 60])
                 .scale(430).translate([width/1.9, height/1.6]);
    var path = d3.geo.path()
    .projection(projection);

    var svg = d3.select("#"+divID)
    .append("svg")
    .attr("width", width)
    .attr("height", height)
    .style("float","right")
    .style("margin-right","7px")
    .style("margin-top","-20px")
    
    .style('background-color', function() {
                 var FilledColor4="";
                if(typeof chartData[divID]["FilledColor4"]!=="undefined" && chartData[divID]["FilledColor4"] !=""){
                return FilledColor4=chartData[divID]["FilledColor4"];
//                  
                } else {
                
                 return "white";
                        }
                                    });
    
//     .style("background-color",FilledColor4)
//            var FilledColor4="";
//                 if(typeof chartData[divID]["FilledColor4"]!=="undefined" && chartData[divID]["FilledColor4"] !=""){
//              FilledColor4=chartData[divID]["FilledColor4"];
////           alert(FilledColor3);
//                               }
                     
    

//    .style("margin-top","-20px")
    ;

    var g = svg.append("g");

//    var colorMap = {};c
    var ctxPath=$("#ctxpath").val();

    d3.json(ctxPath+"/JS/world-topo-min.json", function(error, world) {
        g.selectAll("path")
        .data(topojson.feature(world, world.objects.countries).features)
        .enter()
        .append("path")
        .attr("d", path)// for(var i=0;i<Object.keys(data).length;i++){if(if)}\
         .attr("transform", translateFunction)

    //    .on("click", fun)
        .on("dblclick", clicked)
                    .style("fill", function(d, i) {
 //   Added by shivam                             
                    var drilledvalue;
                        for (var j = 0; j < chartMapData.length; j++) {
                            var colorShad;
                            try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[viewbyIds[0]];
                    } catch (e) {
                    }
                    
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf((d.properties.name)) !== -1) {
                                return drillShade;
                            }
                       

                           else if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
				  if(parseInt(chartMapData[j][measureArray[0]])>0){

                                if (isShadedColor) {
                                    var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
//                                            alert(JSON.stringify(shadingMeasure))
//                                            alert(JSON.stringify(chartMapData[j][shadingMeasure]))
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                            }
                            else if (isShortForm(d, chartMapData[j][columns[0]])) {
				  if(parseInt(chartMapData[j][measureArray[0]])>0){

                                if (isShadedColor) {
                                     var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                    colorShad = color(chartMapData[j][shadingMeasure]);
                                    return colorShad;
                                } else if (conditionalShading) {
                                    return getConditionalColor(color(j), chartMapData[j][conditionalMeasure]);
                                } else {
                                    colorShad = color(j);
                                    colorMap[j] = chartMapData[j][columns[0]] + "__" + colorShad;
                                    return colorShad;
                                }
                            }
                        }
                        }
//                        return "#fff";
             var FilledColor3="";
                 if(typeof chartData[divID]["FilledColor3"]!=="undefined" && chartData[divID]["FilledColor3"] !=""){
             return FilledColor3=chartData[divID]["FilledColor3"];
//           alert(FilledColor3);
                            
                        }
          else { 
                        return "#D0D0D0";
                        }
                        
                    })
                    .style("stroke", "#000").style("stroke-width","0.3px").on("mouseover", function(d, i) {
                show_details(d);
            })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    })
                    .attr("id",function(d, i) {
                    return  d.properties.name+":"+d.properties.name  ;
                    })
                    .on("click",function(d){
                      clicks++;  //count clicks

        if(clicks === 1) {

            timer = setTimeout(function() {
  if(parseInt(chartMapData[j][measureArray[0]])>0){

            drillWithinchart(d.properties.name+":"+d.properties.name,divID)
}
           //     alert("Single Click");  //perform single-click action
                clicks = 0;             //after action performed, reset counter

            }, DELAY);

        } else {

            clearTimeout(timer);    //prevent single-click action
         //   alert("Double Click");  //perform double-click action
            clicks = 0;             //after action performed, reset counter
        }
                    } )

				//	.on("click", function(d, i) {

				//	buildworldDrillMap(this.id,"Hchart1", drillFunData, columns, measureArray);
				//	});
					//.on("click", function(d, i) {
                                     //      buildDrillMapFun(this.id,"chart1","Hchart1")
				//	buildworldDrillMap(this.id,divID, data, columns, measureArray);
					//});
                //    .attr("onclick",fun);
//            parent.$("#colorMap").val(JSON.stringify(colorMap));

           var html = "<div id='legendsScale"+divID+"' onclick='zoom()'class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-41%;margin-left:87%;'></div>";
//                    $('#'+divID).append(html);

//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legendsScale"+divID).append("svg")
                            .attr("width", "100%")
                            .attr("height", "100%");

                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        var selectedMeasure = conditionalMap["measure"];
                        var selectedMeasure = $("#conditionalMeasure").val();
                        var keys = Object.keys(conditionalMap);
                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(selectedMeasure)

//                        for (var no = 0; no < (keys.length - 1); no++) {
                        for (var no = 0; no < (keys.length ); no++) {
                            var legend = svg1.append("g")
                                    .attr("transform", function() {
                                        return "translate(10," + ((no * 20) + 20) + ")";
                                    });
                            legend.append("rect")
                                    .attr("width", 24)
                                    .attr("height", 14)
                                    .attr("cy", 9)
                                    .attr("stroke", "black")
                                    .attr("stroke-width", ".5")
                                    .style("fill", conditionalMap[no]["color"]);
//                                        .attr("r", 5);
                            legend.append("text")
                                    .attr("x", 28)
                                    .attr("y", 9)
                                    .attr("dy", ".35em")
                                    .text(function() {
                                        if (conditionalMap[no]["operator"] === "<>") {
                                            return conditionalMap[no]["limit1"] + conditionalMap[no]["operator"] + conditionalMap[no]["limit2"];
                                        } else {
                                            return conditionalMap[no]["operator"] + conditionalMap[no]["limit1"];
                                        }
                                    });
//                                        .attr("fill", conditionalMap[no]["color"]);
                        }
                        $("#legendsScale"+divID).css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else  {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legendsScale"+divID).height() - 10;
                        var shadingMeasure = colorMap["measure"];

                        var max = maximumValue(chartMapData, shadingMeasure);
                        var min = minimumValue(chartMapData, shadingMeasure);

                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend1")
                                .attr("x1", "0%")
                                .attr("y1", "30%")
                                .attr("x2", "50%")
                                .attr("y2", "30%")
                                .attr("spreadMethod", "pad")
                                .attr("gradientTransform", "rotate(90)");

                        gradient.append("svg:stop")
                                .attr("offset", "0%")
                                .attr("stop-color", colorMap[shadingMeasure])
                                .attr("stop-opacity", 1);
                        gradient.append("svg:stop")
                                .attr("offset", "100%")
                                .attr("stop-color", "rgb(230,230,230)")
                                .attr("stop-opacity", 1);

                        svg1.append("g").append("text").attr("x", 0)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(shadingMeasure);
                        svg1.append("g").append("rect")
                                .attr("width", 10)
                                .attr("height", "90%")
                                .attr("fill", "url(#gradientWrdMapLegend1)")
                                .attr("x", 0)
                                .attr("y", 15);
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", height)
                                .attr("dy", ".35em").text(addCurrencyType(divID, chartData[divID]["meassureIds"][0]) + addCommas(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(addCurrencyType(divID, chartData[divID]["meassureIds"][0]) + addCommas(max));


                        $("#legendsScale"+divID).css("width", "14%");
                    }
//    svg1.append("g").append("rect").attr("x", 10)
//                                .attr("width", 10)
//                                .attr("height", "20%")
//                                 .attr("fill", "url(#gradientWrdMapLegend1)")
//                                .attr("x", 0)
//                                .attr("y", 15)
//    var zoom = d3.behavior.zoom()

    });
    if(typeof chartData[divID]["dataDisplay"]!="undefined" && chartData[divID]["dataDisplay"]!="" && chartData[divID]["dataDisplay"]=="Yes"){
    var svg2 = svg.append("g");
    d3.json(ctxPath+"/JS/world-topo-min.json", function(error, world) {
        svg2.selectAll("path")
        .data(topojson.feature(world, world.objects.countries).features)
        .enter()
        .append("text")
        .text(function(d,i) {

            var returnData = "";
        for (var j = 0; j < chartMapData.length; j++) {
    if (chartMapData[j][columns[0]].toLowerCase() == (d.properties.name).toLowerCase()) {
   return addCurrencyType(divID, getMeasureId(measureArray[0])) + addCommas(numberFormat(chartMapData[j][measureArray[0]],yAxisFormat,yAxisRounding,divID))
    }
    }
    })
      .attr("transform", function(d) { return "translate(" + path.centroid(d) + ")";
        });
        });
    }
var centered;
 var clicked = function(d) {
  var x, y, k;

  if (d && centered !== d) {
    var centroid = path.centroid(d);
    x = centroid[0];
    y = centroid[1];
    k = 3;
    centered = d;
  } else {
    x = width / 2;
    y = height / 2;
    k = 1;
    centered = null;
  }
//alert("width"+width+"height"+height+"k"+k+"x"+x+"y"+y)

  g.selectAll("path")
      .classed("active", centered && function(d) { return d === centered; });

  g.transition()
      .duration(750)
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")scale(" + k + ")translate(" + -x + "," + -y + ")")
      .style("stroke-width", 1.5 / k + "px");

  svg2.selectAll("path")
      .classed("active", centered && function(d) { return d === centered; });

  svg2.transition()
      .duration(750)
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")scale(" + k + ")translate(" + -x + "," + -y + ")")
      .style("stroke-width", 1.5 / k + "px");
}
         function zoom(){
                g.attr("transform", "translate(" +
                        d3.event.translate.join(",") + ")scale(" + d3.event.scale + ")");
                g.selectAll("path")
                        .attr("d", path.projection(projection))

             svg.call(zoom);
    d3.select(self.frameElement).style("height", height + "px");
         }
//var buildworldDrillMap =  function(id,div) {
//       alert(id+"::"+div)
//    }
}
function isShortForm(d, countryName) {

    var flag = false;
    if ((d.properties.name === "United States" && (countryName.toLowerCase().trim() === "united states of america" || countryName.toLowerCase().trim() === "usa")) || (d.properties.name === "United Arab Emirates" && (countryName.toLowerCase().trim() === "uae")) || (d.properties.name === "Germany" && (countryName.toLowerCase().trim() === "ger"))
            || (d.properties.name === "Sri Lanka" && (countryName.toLowerCase().trim() === "srl")) || (d.properties.name === "Thailand" && (countryName.toLowerCase().trim() === "thl"))) {
        flag = true;
    }
    return flag;
}